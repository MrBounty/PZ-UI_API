--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISMedicalCheckAction : ISBaseTimedAction
ISMedicalCheckAction = ISBaseTimedAction:derive("ISMedicalCheckAction");

ISMedicalCheckAction.HealthWindows = {}

function ISMedicalCheckAction.getHealthWindowForPlayer(playerObj)
    return ISMedicalCheckAction.HealthWindows[playerObj]
end

function ISMedicalCheckAction:isValid()
	return self.character:getAccessLevel() ~= "None" or (self.otherPlayerX == self.otherPlayer:getX() and self.otherPlayerY == self.otherPlayer:getY());
end

function ISMedicalCheckAction:waitToStart()
    self.character:faceThisObject(self.otherPlayer)
    return self.character:shouldBeTurning()
end

function ISMedicalCheckAction:update()
    self.character:faceThisObject(self.otherPlayer)
end

function ISMedicalCheckAction:start()
    self:setActionAnim("MedicalCheck")
end

function ISMedicalCheckAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISMedicalCheckAction:perform()

    local playerNum = self.character:getPlayerNum()
    local x = getPlayerScreenLeft(playerNum) + 70
    local y = getPlayerScreenTop(playerNum) + 50
    local healthPanel = nil
    local healthWindow = ISMedicalCheckAction.getHealthWindowForPlayer(self.otherPlayer)
    if healthWindow then
        healthPanel = healthWindow.nested
        healthWindow:addToUIManager()
        healthWindow:setVisible(true)
    else
    healthPanel = ISHealthPanel:new(self.otherPlayer, x, y, 400, 400)
    healthPanel:initialise()

    local title = getText("IGUI_health_playerHealth", self.otherPlayer:getDescriptor():getForename().." "..self.otherPlayer:getDescriptor():getSurname())
    if isClient() then
        title = getText("IGUI_health_playerHealth", self.otherPlayer:getUsername())
    end
    local wrap = healthPanel:wrapInCollapsableWindow(title);
    wrap:setResizable(false)
    wrap:addToUIManager();

    wrap.visibleTarget = self;

    ISMedicalCheckAction.HealthWindows[self.otherPlayer] = wrap
    end

    healthPanel.doctorLevel = self.character:getPerkLevel(Perks.Doctor);
    healthPanel:setOtherPlayer(self.character)

--    self.otherPlayer:setFullHealthRemote(true);

    if JoypadState.players[playerNum+1] then
        JoypadState.players[playerNum+1].focus = healthPanel
        updateJoypadFocus(JoypadState.players[playerNum+1])
    end

    if self.otherPlayer then
        self.character:startReceivingBodyDamageUpdates(self.otherPlayer)
    end

    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self);
end

function ISMedicalCheckAction:new(character, otherPlayer)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.otherPlayer = otherPlayer;
    o.otherPlayerX = otherPlayer:getX();
    o.otherPlayerY = otherPlayer:getY();
    o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = 150 - (character:getPerkLevel(Perks.Doctor) * 2.5);
    o.forceProgressBar = true;
    if character:isTimedActionInstant() then
        o.maxTime = 1;
    end
	return o;
end
