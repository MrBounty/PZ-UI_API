require "TimedActions/ISBaseTimedAction"

---@class ISSitOnChairAction : ISBaseTimedAction
ISSitOnChairAction = ISBaseTimedAction:derive("ISSitOnChairAction");

function ISSitOnChairAction:isValid()
    return true;
end

function ISSitOnChairAction:update()
    self.character:setMetabolicTarget(Metabolics.SeatedResting);
end

function ISSitOnChairAction:start()
end

function ISSitOnChairAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISSitOnChairAction:perform()
    --self.item:ToggleWindow(self.character);
    self.character:satOnChair(self.item);
    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self);
end

function ISSitOnChairAction:new(character, item)
    local o = {}
    setmetatable(o, self)
    self.__index = self
    o.character = character;
    o.item = item;
    o.stopOnWalk = true;
    o.stopOnRun = true;
    o.maxTime = 0;
    return o;
end
