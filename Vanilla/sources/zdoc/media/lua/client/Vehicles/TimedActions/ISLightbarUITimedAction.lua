--***********************************************************
--**                    Yuri Yakovlev                      **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISLightbarUITimedAction : ISBaseTimedAction
ISLightbarUITimedAction = ISBaseTimedAction:derive("ISLightbarUITimedAction");

function ISLightbarUITimedAction:isValid()
    return true;
end

function ISLightbarUITimedAction:update()
end

function ISLightbarUITimedAction:start()
end

function ISLightbarUITimedAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISLightbarUITimedAction:perform()
	local ui
    ISBaseTimedAction.perform(self);
	ui = ISLightbarUI:new(0,0,430,280, self.character);
	ui:initialise();
	ui:addToUIManager();
	local player = self.character:getPlayerNum()
    if JoypadState.players[player+1] then
        setJoypadFocus(player, ui)
    end
end

function ISLightbarUITimedAction:new(character)
    local o = {}
    setmetatable(o, self)
    self.__index = self
    o.character = character;
    o.maxTime = 0;
    return o;
end
