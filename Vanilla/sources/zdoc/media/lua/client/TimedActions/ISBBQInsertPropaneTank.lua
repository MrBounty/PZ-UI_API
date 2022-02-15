--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISBBQInsertPropaneTank : ISBaseTimedAction
ISBBQInsertPropaneTank = ISBaseTimedAction:derive("ISBBQInsertPropaneTank");

function ISBBQInsertPropaneTank:isValid()
	if instanceof(self.tank, "IsoWorldInventoryObject") then
		return self.bbq:getObjectIndex() ~= -1 and self.tank:getWorldObjectIndex() ~= -1
	end
	return self.bbq:getObjectIndex() ~= -1 and
			self.character:getInventory():contains(self.tank)
end

function ISBBQInsertPropaneTank:waitToStart()
	self.character:faceThisObject(self.bbq)
	return self.character:shouldBeTurning()
end

function ISBBQInsertPropaneTank:update()
	self.character:faceThisObject(self.bbq)
    self.character:setMetabolicTarget(Metabolics.MediumWork);
end

function ISBBQInsertPropaneTank:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
	self:setOverrideHandModels(nil, nil)
end

function ISBBQInsertPropaneTank:stop()
    ISBaseTimedAction.stop(self);
end

function ISBBQInsertPropaneTank:perform()
	local tank = self.tank
	if instanceof(self.tank, "IsoWorldInventoryObject") then
		tank = self.tank:getItem()
		self.tank:getSquare():transmitRemoveItemFromSquare(self.tank)
	else
		self.character:removeFromHands(self.tank)
		self.character:getInventory():Remove(self.tank) -- TODO: server controls inventory
	end
	local bbq = self.bbq
	local args = { x = bbq:getX(), y = bbq:getY(), z = bbq:getZ(), delta = tank:getUsedDelta() }
	sendClientCommand(self.character, 'bbq', 'insertPropaneTank', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISBBQInsertPropaneTank:new (character, bbq, tank, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = time
	-- custom fields
	o.bbq = bbq
	o.tank = tank
	return o
end
