--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISStitch : ISBaseTimedAction
ISStitch = ISBaseTimedAction:derive("ISStitch");

function ISStitch:isValid()
	if ISHealthPanel.DidPatientMove(self.character, self.otherPlayer, self.bandagedPlayerX, self.bandagedPlayerY) then
		return false
	end
    if self.item then
        return self.character:getInventory():contains(self.item)
    else
        if not self.bodyPart:stitched() then return false end
        return true
    end
end

function ISStitch:waitToStart()
    if self.character == self.otherPlayer then
        return false
    end
    self.character:faceThisObject(self.otherPlayer)
    return self.character:shouldBeTurning()
end

function ISStitch:update()
    if self.character ~= self.otherPlayer then
        self.character:faceThisObject(self.otherPlayer)
    end
    if self.item then
        self.item:setJobDelta(self:getJobDelta());
    end
    local jobType = self.doIt and getText("ContextMenu_Stitch") or getText("ContextMenu_Remove_Stitch")
    ISHealthPanel.setBodyPartActionForPlayer(self.otherPlayer, self.bodyPart, self, jobType, { stitch = true })

    self.character:setMetabolicTarget(Metabolics.LightDomestic);
end

function ISStitch:start()
    if self.item then
        self.item:setJobType(self.doIt and getText("ContextMenu_Stitch") or getText("ContextMenu_Remove_Stitch"));
        self.item:setJobDelta(0.0);
    end
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

function ISStitch:stop()
    ISHealthPanel.setBodyPartActionForPlayer(self.otherPlayer, self.bodyPart, nil, nil, nil)
    ISBaseTimedAction.stop(self);
    if self.item then
        self.item:setJobDelta(0.0);
    end
end

function ISStitch:perform()
    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self);
    if self.item then
        self.item:setJobDelta(0.0);
    end

    local basePain = 20;
    if self.doIt then
        if self.character:getInventory():contains("SutureNeedleHolder") or self.item:getType() == "SutureNeedle" then
            basePain = 10;
        end
    else
        basePain = 5;
    end

    if self.character:HasTrait("Hemophobic") then
        self.character:getStats():setPanic(self.character:getStats():getPanic() + 50);
    end

    if self.item then
        self.item:Use();
    end
    if self.bodyPart:isGetStitchXp() then
        self.character:getXp():AddXP(Perks.Doctor, 15);
    end
    self.bodyPart:setStitched(self.doIt);

    local endPain = (basePain - (self.doctorLevel * 1));
    if endPain < 0 then
        endPain = 0;
    end
    if self.doctor:getAccessLevel() ~= "None" then
        self.bodyPart:setAdditionalPain(self.bodyPart:getAdditionalPain() + endPain);
    end
    -- boost the stitch depending on the doctor's level
    if self.doIt then
        self.bodyPart:setStitchTime(((1 + self.doctorLevel) / 2) * ZombRandFloat(2.0, 5.0));
    end

    -- chance of wound infection
    if ZombRand(5 + self.doctorLevel * 2.5) == 0 then
        self.bodyPart:setInfectedWound(true);
        if isClient() then
            sendWoundInfection(self.otherPlayer:getOnlineID(), self.bodyPart:getIndex(), true);
        end
    end

    if isClient() then
        sendStitch(self.otherPlayer:getOnlineID(), self.bodyPart:getIndex(), self.doIt, self.bodyPart:getStitchTime());
        if self.doctor:getAccessLevel() ~= "None" then
            sendAdditionalPain(self.otherPlayer:getOnlineID(), self.bodyPart:getIndex(), endPain);
        end
    end

    ISHealthPanel.setBodyPartActionForPlayer(self.otherPlayer, self.bodyPart, nil, nil, nil)
end

function ISStitch:new(doctor, otherPlayer, item, bodyPart, doIt)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = doctor;
    o.otherPlayer = otherPlayer;
    o.doctorLevel = doctor:getPerkLevel(Perks.Doctor);
	o.item = item;
    o.doIt = doIt;
	o.bodyPart = bodyPart;
    o.doctor = doctor;
	o.stopOnWalk = true;
	o.stopOnRun = true;
    o.bandagedPlayerX = otherPlayer:getX();
    o.bandagedPlayerY = otherPlayer:getY();
    local baseTime = 200;
    -- a suture needle or a suture needle holder make it faster and less painy
    if doctor:getInventory():contains("SutureNeedleHolder") or (item and item:getType() == "SutureNeedle") then
        baseTime = 150;
    end
    o.maxTime = baseTime - (o.doctorLevel * 4);
    if doctor:isTimedActionInstant() then
        o.maxTime = 1;
    end
    if doctor:getAccessLevel() ~= "None" then
        o.doctorLevel = 10;
    end
	return o;
end
