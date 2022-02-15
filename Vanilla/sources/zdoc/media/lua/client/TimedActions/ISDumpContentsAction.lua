--***********************************************************
--**                    Erasmus Crowley                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISDumpContentsAction : ISBaseTimedAction
ISDumpContentsAction = ISBaseTimedAction:derive("ISDumpContentsAction");

function ISDumpContentsAction:isValid()
	return self.character:getInventory():contains(self.item);
end

function ISDumpContentsAction:start()
    if self.item ~= nil then
	    self.item:setJobType(getText("IGUI_JobType_PourOut"));
	    self.item:setJobDelta(0.0);
	
		self:setActionAnim(CharacterActionAnims.Pour);
		self:setAnimVariable("FoodType", self.item:getEatType());
		self:setOverrideHandModels(self.item, nil);
	
		self.character:reportEvent("EventTakeWater");
    end
end

function ISDumpContentsAction:update()
	if self.item ~= nil then
        self.item:setJobDelta(self:getJobDelta());
    end
end

function ISDumpContentsAction:stop()
    ISBaseTimedAction.stop(self);
    if self.item ~= nil then
        self.item:setJobDelta(0.0);
     end
end

function ISDumpContentsAction:perform()
	if self.item ~= nil then
		self.item:getContainer():setDrawDirty(true);
		self.item:setJobDelta(0.0);
		local itemType = self:finalItem(self.item:getFullType())
		if itemType then
			if self.item:getReplaceOnUse() then
				self.item:setReplaceOnUse(itemType)
			elseif instanceof(self.item, "DrainableComboItem") and self.item:getReplaceOnDeplete() then
				self.item:setReplaceOnDeplete(itemType)
				self.item:setUseDelta(1)
			end
		end
		self.item:Use();
	end
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

	-- RemouladeFull -> RemouladeHalf -> RemouladeEmpty
function ISDumpContentsAction:finalItem(itemType)
	local item = ScriptManager.instance:FindItem(itemType)
	if item == nil then return nil end
	if item:getCanStoreWater() then
		return itemType
	end
	if item:getReplaceOnUse() then
		itemType = moduleDotType(item:getModuleName(), item:getReplaceOnUse())
		return self:finalItem(itemType)
	end
	if (item:getType() == Type.Drainable) and item:getReplaceOnDeplete() then
		itemType = moduleDotType(item:getModuleName(), item:getReplaceOnDeplete())
		return self:finalItem(itemType)
	end
	return nil
end

function ISDumpContentsAction:new (character, item, time)
	local o = {}
		setmetatable(o, self)
		self.__index = self
	o.character = character;
	o.item = item;
	o.stopOnWalk = false;
	o.stopOnRun = false;
	o.maxTime = time;
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	return o
end
