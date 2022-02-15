require "TimedActions/ISBaseTimedAction"

---@class ISRemoveBrokenGlass : ISBaseTimedAction
ISRemoveBrokenGlass = ISBaseTimedAction:derive("ISRemoveBrokenGlass")

function ISRemoveBrokenGlass:isValid()
	return self.window:getObjectIndex() ~= -1 and self.window:isSmashed() and not self.window:isGlassRemoved()
end

function ISRemoveBrokenGlass:waitToStart()
	self.character:faceThisObject(self.window)
	return self.character:shouldBeTurning()
end

function ISRemoveBrokenGlass:update()
	self.character:faceThisObject(self.window)

    self.character:setMetabolicTarget(Metabolics.LightWork);
end

function ISRemoveBrokenGlass:start()
--	getSoundManager():PlayWorldSound("RemoveBrokenGlass", false, self.window:getSquare(), 1, 20, 1, false)
    self.window:getSquare():playSound("RemoveBrokenGlass");
    addSound(self.character, self.character:getX(), self.character:getY(), self.character:getZ(), 20, 1)
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Mid")
	self:setOverrideHandModels(nil, nil)
	self.character:reportEvent("EventLootItem");
end

function ISRemoveBrokenGlass:stop()
	ISBaseTimedAction.stop(self)
end

function ISRemoveBrokenGlass:perform()
	self.window:removeBrokenGlass()

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISRemoveBrokenGlass:new(character, window, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.window = window
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = time
    o.caloriesModifier = 8;
    if character:isTimedActionInstant() then
        o.maxTime = 1;
    end

    return o
end
