--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISRemoveGlass : ISBaseTimedAction
ISRemoveGlass = ISBaseTimedAction:derive("ISRemoveGlass");

function ISRemoveGlass:isValid()
	if ISHealthPanel.DidPatientMove(self.character, self.otherPlayer, self.bandagedPlayerX, self.bandagedPlayerY) then
		return false
	end
	return true
end

function ISRemoveGlass:waitToStart()
    if self.character == self.otherPlayer then
        return false
    end
    self.character:faceThisObject(self.otherPlayer)
    return self.character:shouldBeTurning()
end

function ISRemoveGlass:update()
    if self.character ~= self.otherPlayer then
        self.character:faceThisObject(self.otherPlayer)
    end
    local jobType = getText("ContextMenu_Remove_Glass")
    ISHealthPanel.setBodyPartActionForPlayer(self.otherPlayer, self.bodyPart, self, jobType, { removeGlass = true })

    self.character:setMetabolicTarget(Metabolics.UsingTools);
end

function ISRemoveGlass:start()
    if self.character == self.otherPlayer then
        self:setActionAnim(CharacterActionAnims.Bandage);
        self:setAnimVariable("BandageType", ISHealthPanel.getBandageType(self.bodyPart));
    else
        self:setActionAnim("Loot")
        self.character:SetVariable("LootPosition", "Mid")
    end
    self:setOverrideHandModels(nil, nil);
end

function ISRemoveGlass:stop()
    ISHealthPanel.setBodyPartActionForPlayer(self.otherPlayer, self.bodyPart, nil, nil, nil)
    ISBaseTimedAction.stop(self);
end

function ISRemoveGlass:perform()
    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self);

    if self.character:HasTrait("Hemophobic") then
        self.character:getStats():setPanic(self.character:getStats():getPanic() + 50);
    end

    self.character:getXp():AddXP(Perks.Doctor, 15);
    local addPain = (30 - (self.doctorLevel * 1))
    if self.doctor:getAccessLevel() ~= "None" then
        self.bodyPart:setAdditionalPain(self.bodyPart:getAdditionalPain() + addPain);
    end

    if self.handPain then
        self.bodyPart:setAdditionalPain(self.bodyPart:getAdditionalPain() + 30);
    end
    self.bodyPart:setHaveGlass(false);

    if isClient() then
        sendRemoveGlass(self.otherPlayer:getOnlineID(), self.bodyPart:getIndex());
        if self.doctor:getAccessLevel() ~= "None" then
            sendAdditionalPain(self.otherPlayer:getOnlineID(), self.bodyPart:getIndex(), addPain);
        end
    end

    ISHealthPanel.setBodyPartActionForPlayer(self.otherPlayer, self.bodyPart, nil, nil, nil)
end

function ISRemoveGlass:new(doctor, otherPlayer, bodyPart)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = doctor;
    o.otherPlayer = otherPlayer;
    o.doctorLevel = doctor:getPerkLevel(Perks.Doctor);
	o.bodyPart = bodyPart;
	o.stopOnWalk = true;
	o.stopOnRun = true;
    o.bandagedPlayerX = otherPlayer:getX();
    o.bandagedPlayerY = otherPlayer:getY();
    o.doctor = doctor;
    o.handPain = false;
    o.maxTime = 150 - (o.doctorLevel * 4);
    if doctor:getAccessLevel() ~= "None" then
        o.maxTime = 1;
        o.doctorLevel = 10;
    end

    return o;
end

function ISRemoveGlass:new(doctor, otherPlayer, bodyPart, hands)
    local o = {}
    setmetatable(o, self)
    self.__index = self
    o.character = doctor;
    o.otherPlayer = otherPlayer;
    o.doctorLevel = doctor:getPerkLevel(Perks.Doctor);
    o.bodyPart = bodyPart;
    o.stopOnWalk = true;
    o.stopOnRun = true;
    o.bandagedPlayerX = otherPlayer:getX();
    o.bandagedPlayerY = otherPlayer:getY();
    o.doctor = doctor;
    o.maxTime = 150 - (o.doctorLevel * 4);
    if doctor:isTimedActionInstant() then
        o.maxTime = 1;
    end
    if doctor:getAccessLevel() ~= "None" then
        o.doctorLevel = 10;
    end

    if (hands) then
        o.handPain = true;
        o.maxTime = 300 - (o.doctorLevel * 4)
    end

    return o;
end
