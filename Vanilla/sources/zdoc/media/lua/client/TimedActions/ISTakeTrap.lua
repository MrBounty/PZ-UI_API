--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISTakeTrap : ISBaseTimedAction
ISTakeTrap = ISBaseTimedAction:derive("ISTakeTrap");

function ISTakeTrap:isValid()
	return self.trap:getObjectIndex() ~= -1 and self.trap:getItem() ~= nil
end

function ISTakeTrap:waitToStart()
	self.character:faceThisObject(self.trap)
	return self.character:shouldBeTurning()
end

function ISTakeTrap:update()
	self.character:faceThisObject(self.trap)

    self.character:setMetabolicTarget(Metabolics.LightDomestic);
end

function ISTakeTrap:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
end

function ISTakeTrap:stop()
    ISBaseTimedAction.stop(self);
end

function ISTakeTrap:perform()
	self.character:getInventory():AddItem(self.trap:getItem())
	self.trap:getSquare():transmitRemoveItemFromSquare(self.trap)
	self.trap:removeFromWorld()
	self.trap:removeFromSquare()
--    self.character:getInventory():setDrawDirty(true);

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISTakeTrap:new(character, trap, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.trap = trap;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	return o;
end
