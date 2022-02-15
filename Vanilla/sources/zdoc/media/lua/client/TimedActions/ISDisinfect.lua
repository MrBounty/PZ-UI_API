--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISDisinfect : ISBaseTimedAction
ISDisinfect = ISBaseTimedAction:derive("ISDisinfect");

function ISDisinfect:isValid()
	if ISHealthPanel.DidPatientMove(self.character, self.otherPlayer, self.bandagedPlayerX, self.bandagedPlayerY) then
		return false
	end
	return self.character:getInventory():contains(self.alcohol)
end

function ISDisinfect:waitToStart()
    if self.character == self.otherPlayer then
        return false
    end
    self.character:faceThisObject(self.otherPlayer)
    return self.character:shouldBeTurning()
end

function ISDisinfect:update()
    if self.character ~= self.otherPlayer then
        self.character:faceThisObject(self.otherPlayer)
    end
    local jobType = getText("ContextMenu_Disinfect")
    ISHealthPanel.setBodyPartActionForPlayer(self.otherPlayer, self.bodyPart, self, jobType, { disinfect = true })

    self.character:setMetabolicTarget(Metabolics.LightDomestic);
end

function ISDisinfect:start()
    if self.character == self.otherPlayer then
        self:setActionAnim(CharacterActionAnims.Bandage);
        self:setAnimVariable("BandageType", ISHealthPanel.getBandageType(self.bodyPart));
        self.character:reportEvent("EventBandage");
    else
        self:setActionAnim("Loot")
        self.character:SetVariable("LootPosition", "Mid")
        self.character:reportEvent("EventLootItem");
    end
	self:setOverrideHandModels(nil, nil);
end

function ISDisinfect:stop()
    ISHealthPanel.setBodyPartActionForPlayer(self.otherPlayer, self.bodyPart, nil, nil, nil)
    ISBaseTimedAction.stop(self);
end

function ISDisinfect:perform()
    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self);
    self.bodyPart:setAlcoholLevel(self.bodyPart:getAlcoholLevel() + self.alcohol:getAlcoholPower());
    local addPain = (self.alcohol:getAlcoholPower() * 13) - (self.doctorLevel / 2)
    if self.doctor:getAccessLevel() ~= "None" then
        self.bodyPart:setAdditionalPain(self.bodyPart:getAdditionalPain() + addPain);
    end
    if(instanceof(self.alcohol, "Food")) then
        self.alcohol:setThirstChange(self.alcohol:getThirstChange() + 0.1);
        if(self.alcohol:getBaseHunger() < 0) then
            self.alcohol:setHungChange(self.alcohol:getHungChange() + 0.1);
        end

        if self.alcohol:getThirstChange() > -0.01 or self.alcohol:getHungerChange() > -0.01 then
            self.alcohol:Use();
        end
    elseif (instanceof(self.alcohol, "DrainableComboItem")) then
        self.alcohol:Use();
    end

    if isClient() then
        sendDisinfect(self.otherPlayer:getOnlineID(), self.bodyPart:getIndex(), self.alcohol:getAlcoholPower());
        if self.doctor:getAccessLevel() ~= "None" then
            sendAdditionalPain(self.otherPlayer:getOnlineID(), self.bodyPart:getIndex(), addPain);
        end
    end

    ISHealthPanel.setBodyPartActionForPlayer(self.otherPlayer, self.bodyPart, nil, nil, nil)
end

function ISDisinfect:new(doctor, otherPlayer, alcohol, bodyPart)
	local o = {}
	setmetatable(o, self)
	self.__index = self;
	o.character = doctor;
    o.doctor = doctor;
    o.otherPlayer = otherPlayer;
    o.doctorLevel = doctor:getPerkLevel(Perks.Doctor);
	o.alcohol = alcohol;
	o.bodyPart = bodyPart;
	o.stopOnWalk = bodyPart:getIndex() > BodyPartType.ToIndex(BodyPartType.Groin);
	o.stopOnRun = true;
    o.bandagedPlayerX = otherPlayer:getX();
    o.bandagedPlayerY = otherPlayer:getY();
    o.maxTime = 120 - (o.doctorLevel * 4);
    if doctor:isTimedActionInstant() then
        o.maxTime = 1;
    end
    if doctor:getAccessLevel() ~= "None" then
        o.doctorLevel = 10;
    end
	return o;
end
