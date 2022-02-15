require "TimedActions/ISBaseTimedAction"

---@class ISSmashWindow : ISBaseTimedAction
ISSmashWindow = ISBaseTimedAction:derive("ISSmashWindow");

function ISSmashWindow:isValid()
    return true;
end

function ISSmashWindow:update()
    self.character:setMetabolicTarget(Metabolics.UsingTools);
end

function ISSmashWindow:start()
end

function ISSmashWindow:stop()
    ISBaseTimedAction.stop(self);
end

function ISSmashWindow:perform()
    --self.item:ToggleWindow(self.character);
    self.character:smashWindow(self.item);
    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self);
end

function ISSmashWindow:new(character, item, time)
    local o = {}
    setmetatable(o, self)
    self.__index = self
    o.character = character;
    o.item = item;
    o.stopOnWalk = true;
    o.stopOnRun = true;
    o.maxTime = time;
    return o;
end
