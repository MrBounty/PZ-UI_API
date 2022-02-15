--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISRepairClothing : ISBaseTimedAction
ISRepairClothing = ISBaseTimedAction:derive("ISRepairClothing");

function ISRepairClothing:isValid()
	return self.character:getInventory():contains(self.clothing) and
		self.character:getInventory():contains(self.fabric) and
		self.character:getInventory():contains(self.needle) and
		self.character:getInventory():contains(self.thread) and
		(self.clothing:getPatchType(self.part) == nil)
end

function ISRepairClothing:update()
	
end

function ISRepairClothing:start()
	self:setActionAnim(CharacterActionAnims.Craft);
end

function ISRepairClothing:stop()
    ISBaseTimedAction.stop(self);
end

function ISRepairClothing:perform()
    self.clothing:addPatch(self.character, self.part, self.fabric);
	self.character:resetModel();
	self.character:getInventory():Remove(self.fabric);
	self.thread:Use();
	
	self.character:getXp():AddXP(Perks.Tailoring, ZombRand(1, 3));

	triggerEvent("OnClothingUpdated", self.character)

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISRepairClothing:new(character, clothing, part, fabric, thread, needle)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
    o.clothing = clothing;
	o.part = part;
	o.fabric = fabric;
	o.thread = thread;
	o.needle = needle;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = 150 - (character:getPerkLevel(Perks.Tailoring) * 6);
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	return o;
end
