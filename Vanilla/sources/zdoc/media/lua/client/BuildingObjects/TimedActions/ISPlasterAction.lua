--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISPlasterAction : ISBaseTimedAction
ISPlasterAction = ISBaseTimedAction:derive("ISPlasterAction");

function ISPlasterAction:isValid()
	return ISBuildMenu.cheat or self.character:getInventory():contains("BucketPlasterFull");
end

function ISPlasterAction:update()
    self.character:setMetabolicTarget(Metabolics.MediumWork);
end

function ISPlasterAction:start()
	self.sound = self.character:playSound("Plastering")
end

function ISPlasterAction:stop()
    if self.sound then self.character:stopOrTriggerSound(self.sound) end
    ISBaseTimedAction.stop(self);
end

function ISPlasterAction:perform()
    if self.sound then self.character:stopOrTriggerSound(self.sound) end
	local modData = self.thumpable:getModData();
    local north = "";
    if self.thumpable:getNorth() then
        north = "North";
    end
    local sprite = Painting[modData["wallType"]]["plasterTile" .. north];
	local obj = self.thumpable
	local index = obj:getObjectIndex()
	local args = { x=obj:getX(), y=obj:getY(), z=obj:getZ(), index=index, sprite = sprite }
	sendClientCommand(self.character, 'object', 'plaster', args)

	if not ISBuildMenu.cheat then
		self.plasterBucket:Use();
	end
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISPlasterAction:new(character, thumpable, plasterBucket, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.thumpable = thumpable;
	o.plasterBucket = plasterBucket;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
    o.caloriesModifier = 8;
    if ISBuildMenu.cheat then o.maxTime = 1; end
	return o;
end
