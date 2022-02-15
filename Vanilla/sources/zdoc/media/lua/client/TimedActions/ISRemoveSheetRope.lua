require "TimedActions/ISBaseTimedAction"

---@class ISRemoveSheetRope : ISBaseTimedAction
ISRemoveSheetRope = ISBaseTimedAction:derive("ISRemoveSheetRope");

function ISRemoveSheetRope:isValid()
    if IsoWindowFrame.isWindowFrame(self.window) then
        return IsoWindowFrame.haveSheetRope(self.window)
    end
    return self.window and self.window:haveSheetRope()
end

function ISRemoveSheetRope:waitToStart()
    self.character:faceThisObject(self.window)
    return self.character:shouldBeTurning()
end

function ISRemoveSheetRope:update()
    self.character:faceThisObject(self.window)

    self.character:setMetabolicTarget(Metabolics.LightWork);
end

function ISRemoveSheetRope:start()
	self:setActionAnim("Loot")
	self.character:SetVariable("LootPosition", "Mid")
end

function ISRemoveSheetRope:stop()
    ISBaseTimedAction.stop(self);
end

function ISRemoveSheetRope:perform()
    local obj = self.window
    local index = obj:getObjectIndex()
    local args = { x=obj:getX(), y=obj:getY(), z=obj:getZ(), index=index }
    sendClientCommand(self.character, 'object', 'removeSheetRope', args)

    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self);
end

function ISRemoveSheetRope:new(character, window)
    local o = {}
    setmetatable(o, self)
    self.__index = self
    o.character = character;
    o.stopOnWalk = true;
    o.stopOnRun = true;
    o.maxTime = 20;
    -- custom fields
    o.window = window;
    return o;
end
