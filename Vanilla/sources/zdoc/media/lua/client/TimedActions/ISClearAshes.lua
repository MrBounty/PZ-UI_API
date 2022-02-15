--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISClearAshes : ISBaseTimedAction
ISClearAshes = ISBaseTimedAction:derive("ISClearAshes")

function ISClearAshes:isValid()
	return (self.weapon and self.weapon:getCondition() > 0) or not self.weapon;
end

function ISClearAshes:waitToStart()
	self.character:faceThisObject(self.ashes)
	return self.character:shouldBeTurning()
end

function ISClearAshes:update()
	self.character:faceThisObject(self.ashes)
    self.character:setMetabolicTarget(Metabolics.LightWork);
end

function ISClearAshes:start()
	local handItem = self.character:getPrimaryHandItem()
	if handItem then
		local anim = ISFarmingMenu.getShovelAnim(handItem)
		if handItem:getType() == "Broom" or handItem:getType() == "Mop" then
			anim = "Rake"
		end
		self:setActionAnim(anim)
	else
		self:setActionAnim("Loot")
		self.character:SetVariable("LootPosition", "Low")
	end
end

function ISClearAshes:stop()
    ISBaseTimedAction.stop(self)
end

function ISClearAshes:perform()
    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self)
    self.ashes:getSquare():transmitRemoveItemFromSquare(self.ashes);
    self.ashes:getSquare():getObjects():remove(self.ashes);
end

function ISClearAshes:new(character, ashes, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = time
	o.spriteFrame = 0
	o.ashes = ashes
    if character:isTimedActionInstant() then
        o.maxTime = 1;
    end
	return o
end
