--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISDropWorldItemAction : ISBaseTimedAction
ISDropWorldItemAction = ISBaseTimedAction:derive("ISDropWorldItemAction");

function ISDropWorldItemAction:isValid()
	return self.character:getInventory():contains(self.item);
end

function ISDropWorldItemAction:update()
	self.item:setJobDelta(self:getJobDelta());
end

function ISDropWorldItemAction:start()
	self.item:setJobType(getText("IGUI_JobType_Dropping"));
	self.item:setJobDelta(0.0);
end

function ISDropWorldItemAction:stop()
    ISBaseTimedAction.stop(self);
    self.item:setJobDelta(0.0);

end

function ISDropWorldItemAction:perform()

	if self.item:getType() == "CandleLit" then
		local candle = self.character:getInventory():AddItem("Base.Candle");
		candle:setUsedDelta(self.item:getUsedDelta());
		candle:setCondition(self.item:getCondition());
		candle:setFavorite(self.item:isFavorite());
		self.character:getInventory():Remove(self.item);
		self.item = candle;
	end

    self.item:getContainer():setDrawDirty(true);
    self.item:setJobDelta(0.0);

	local worldItem = self.sq:AddWorldInventoryItem(self.item, self.xoffset, self.yoffset, self.zoffset, false)
	if worldItem then
		worldItem:setWorldZRotation(self.rotation);
		worldItem:getWorldItem():setIgnoreRemoveSandbox(true); -- avoid the item to be removed by the SandboxOption WorldItemRemovalList
	end
	worldItem:getWorldItem():transmitCompleteItemToServer();
	self.character:getInventory():Remove(self.item);


	ISInventoryPage.renderDirty = true
    
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISDropWorldItemAction:new (character, item, sq, xoffset, yoffset, zoffset, rotation)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.sq = sq;
	o.xoffset = xoffset;
	o.yoffset = yoffset;
	o.zoffset = zoffset;
	o.rotation = rotation;
	o.stopOnWalk = false;
	o.stopOnRun = false;
	o.maxTime = 50;
	o.srcContainer = item:getContainer();

	local w = item:getActualWeight();
	if w > 3 then w = 3; end;
	o.maxTime = o.maxTime * (w)

	o.maxTime = o.maxTime * 0.1;

	if character:HasTrait("Dextrous") then
		o.maxTime = o.maxTime * 0.5
	end
	if character:HasTrait("AllThumbs") then
		o.maxTime = o.maxTime * 4.0
	end

	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	return o
end
