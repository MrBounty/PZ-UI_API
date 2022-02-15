--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISFixAction : ISBaseTimedAction
ISFixAction = ISBaseTimedAction:derive("ISFixAction");

function ISFixAction:isValid()
	if not self.vehiclePart then
    	return self.character:getInventory():contains(self.item) and self.character:getInventory():contains(self.fixer:getFixerName());
	else
		return self.character:getInventory():contains(self.fixer:getFixerName());
	end
end

function ISFixAction:update()
	self.item:setJobDelta(self:getJobDelta());

    self.character:setMetabolicTarget(Metabolics.UsingTools);
end

function ISFixAction:start()
	self.item:setJobType(getText("IGUI_JobType_Repair"));
	self.item:setJobDelta(0.0);
end

function ISFixAction:stop()
    ISBaseTimedAction.stop(self);
    self.item:setJobDelta(0.0);
end

function ISFixAction:perform()
	if self.item:getContainer() then
    	self.item:getContainer():setDrawDirty(true);
	end
    self.item:setJobDelta(0.0);
    FixingManager.fixItem(self.item, self.character, self.fixing, self.fixer);
	if self.vehiclePart then
		local part = self.vehiclePart
		if isClient() then
			-- The server should call FixingManager.fixItem() but doesn't have all the info it needs.
			local args = { vehicle = part:getVehicle():getId(), part = part:getId(),
				condition = self.item:getCondition(), haveBeenRepaired = self.item:getHaveBeenRepaired() }
			sendClientCommand(self.character, 'vehicle', 'fixPart', args)
		else
			part:setCondition(self.item:getCondition())
			part:doInventoryItemStats(self.item, part:getMechanicSkillInstaller())
			if part:isContainer() and not part:getItemContainer() then
				-- Changing condition might change capacity.
				-- This limits content amount to max capacity.
				part:setContainerContentAmount(part:getContainerContentAmount())
			end
			part:getVehicle():updatePartStats()
			part:getVehicle():updateBulletStats()
		end
	end
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISFixAction:new(character, item, time, fixing, fixer, vehiclePart)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
    o.item = item;
	o.fixing = fixing;
	o.fixer = fixer;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
    o.caloriesModifier = 4;
	o.vehiclePart = vehiclePart;
	o.jobType = getText("IGUI_Vehicle_Repairing", item:getName());
    if character:isTimedActionInstant() then
        o.maxTime = 1;
    end
	return o;
end
