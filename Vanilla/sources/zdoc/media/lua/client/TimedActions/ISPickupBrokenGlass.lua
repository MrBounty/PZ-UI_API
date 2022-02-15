require "TimedActions/ISBaseTimedAction"
require "Moveables/ISMoveableTools"
require "Moveables/ISMoveableSpriteProps"

---@class ISPickupBrokenGlass : ISBaseTimedAction
ISPickupBrokenGlass = ISBaseTimedAction:derive("ISPickupBrokenGlass")

function ISPickupBrokenGlass:isValid()
	return true
end

function ISPickupBrokenGlass:waitToStart()
	self.character:faceThisObject(self.glass)
	return self.character:shouldBeTurning()
end

function ISPickupBrokenGlass:update()
	self.character:faceThisObject(self.glass)
end

function ISPickupBrokenGlass:start()
--	getSoundManager():PlayWorldSound("RemoveBrokenGlass", false, self.window:getSquare(), 1, 20, 1, false)
    self.glass:getSquare():playSound("RemoveBrokenGlass");
    addSound(self.character, self.character:getX(), self.character:getY(), self.character:getZ(), 20, 1)
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Low")
	self:setOverrideHandModels(nil, nil)
	self.character:reportEvent("EventLootItem");
end

function ISPickupBrokenGlass:stop()
	ISBaseTimedAction.stop(self)
end

function ISPickupBrokenGlass:perform()
	-- add random damage to hands if no gloves (done in pickUpMoveable)
	if ISMoveableTools.isObjectMoveable(self.glass) then
        local moveable = ISMoveableTools.isObjectMoveable(self.glass)
        moveable:pickUpMoveable( self.character, self.square, self.glass, true )
    end

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISPickupBrokenGlass:new(character, glass, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.glass = glass
    o.square = glass:getSquare()
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = time
    o.caloriesModifier = 8;
    if character:isTimedActionInstant() then
        o.maxTime = 1;
    end

    return o
end
