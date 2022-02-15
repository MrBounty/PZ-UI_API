--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 19/02/14
-- Time: 16:43
-- To change this template use File | Settings | File Templates.
--

--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISToggleSafetyAction : ISBaseTimedAction
ISToggleSafetyAction = ISBaseTimedAction:derive("ISToggleSafetyAction");

function ISToggleSafetyAction:isValid()
    return true;
end

function ISToggleSafetyAction:update()
end

function ISToggleSafetyAction:start()
    if self.isSafety then
        toggleSafetyServer(self.character)
    end
end

function ISToggleSafetyAction:stop()
    if self.isSafety then
        toggleSafetyServer(self.character)
    end
    self.character:setSafetyCooldown(0)
    ISBaseTimedAction.stop(self);
end

function ISToggleSafetyAction:perform()
    self.character:setSafety(not self.isSafety);
    if not self.isSafety then
        toggleSafetyServer(self.character)
    end
    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self);
end

function ISToggleSafetyAction:new (character)
    local o = {}
    setmetatable(o, self)
    self.__index = self
    o.character = character;
    o.isSafety = character:isSafety();
    o.stopOnWalk = false;
    o.stopOnRun = false;
    o.ignoreHandsWounds = true;
    o.maxTime = getServerOptions():getInteger("SafetyToggleTimer") * 30 * 1.6;
    return o
end
