--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISOvenUITimedAction : ISBaseTimedAction
ISOvenUITimedAction = ISBaseTimedAction:derive("ISOvenUITimedAction");

function ISOvenUITimedAction:isValid()
    return true;
end

function ISOvenUITimedAction:update()
	self.character:faceThisObject(self.mcwave or self.stove)
end

function ISOvenUITimedAction:start()
end

function ISOvenUITimedAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISOvenUITimedAction:perform()
	local ui
    ISBaseTimedAction.perform(self);
    if self.mcwave then
        ui = ISMicrowaveUI:new(0,0,430,280, self.mcwave, self.character);
        ui:initialise();
        ui:addToUIManager();
    else
        ui = ISOvenUI:new(0,0,430,310, self.stove, self.character);
        ui:initialise();
        ui:addToUIManager();
    end
	local player = self.character:getPlayerNum()
	
    if JoypadState.players[player+1] then
        ui.prevFocus = JoypadState.players[player+1].focus
        setJoypadFocus(player, ui)
    end
end

function ISOvenUITimedAction:new(character, stove, mcwave)
    local o = {}
    setmetatable(o, self)
    self.__index = self
    o.character = character;
    o.stove = stove;
    o.mcwave = mcwave;
    o.stopOnWalk = true;
    o.stopOnRun = true;
    o.maxTime = 0;
    return o;
end
