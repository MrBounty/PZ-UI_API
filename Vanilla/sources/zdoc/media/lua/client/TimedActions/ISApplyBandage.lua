--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISApplyBandage : ISBaseTimedAction
ISApplyBandage = ISBaseTimedAction:derive("ISApplyBandage");

function ISApplyBandage:isValid()
	if ISHealthPanel.DidPatientMove(self.character, self.otherPlayer, self.bandagedPlayerX, self.bandagedPlayerY) then
		return false
	end
    if self.item then
        return self.character:getInventory():contains(self.item);
    else
        if not self.bodyPart:bandaged() then return false end
        return true
    end
end

function ISApplyBandage:waitToStart()
    if self.character == self.otherPlayer then
        return false
    end
    self.character:faceThisObject(self.otherPlayer)
    return self.character:shouldBeTurning()
end

function ISApplyBandage:update()
    if self.character ~= self.otherPlayer then
        self.character:faceThisObject(self.otherPlayer)
    end
    if self.item then
        self.item:setJobDelta(self:getJobDelta());
    end
    local jobType = self.doIt and getText("ContextMenu_Bandage") or getText("ContextMenu_Remove_Bandage")
    ISHealthPanel.setBodyPartActionForPlayer(self.otherPlayer, self.bodyPart, self, jobType, { bandage = true })
    self.character:setMetabolicTarget(Metabolics.LightDomestic);
end

function ISApplyBandage:start()
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
    if self.item then
        self.item:setJobType(getText("ContextMenu_Apply_Bandage"));
        self.item:setJobDelta(0.0);
    end
end

function ISApplyBandage:stop()
    ISHealthPanel.setBodyPartActionForPlayer(self.otherPlayer, self.bodyPart, nil, nil, nil)
    ISBaseTimedAction.stop(self);
    if self.item then
        self.item:setJobDelta(0.0);
    end
end

function ISApplyBandage:perform()
    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self);
    if self.item then
        self.item:setJobDelta(0.0);
    end

    if self.character:HasTrait("Hemophobic") and self.bodyPart:getBleedingTime() > 0 then
        self.character:getStats():setPanic(self.character:getStats():getPanic() + 50);
    end
    -- random a bandage life depending on the doctor skill
    if self.doIt then
        local bandageLife = ZombRandFloat((self.doctorLevel + 1) * 0.5, (self.doctorLevel + 1) * 1.0) + self.item:getBandagePower();
        if string.match(self.item:getType(), "Dirty") then
            bandageLife = 0;
        end
        if self.bodyPart:isGetBandageXp() and bandageLife > 0 then
            self.character:getXp():AddXP(Perks.Doctor, 5);
        end
        self.otherPlayer:getBodyDamage():SetBandaged(self.bodyPart:getIndex(), true, bandageLife, self.item:isAlcoholic(), self.item:getModule() .. "." .. self.item:getType());
        self.character:getInventory():Remove(self.item);
        if self.item:isInfected() then
            self.bodyPart:SetInfected(true);
        end
        -- apply bandage to model
--        if self.bodyPart:getType():getBandageModel() then
--            self.otherPlayer:addBandageModel(self.bodyPart:getType():getBandageModel());
--        end
        if isClient() then
            sendBandage(self.otherPlayer:getOnlineID(), self.bodyPart:getIndex(), true, bandageLife, self.item:isAlcoholic(), self.item:getModule() .. "." .. self.item:getType());
        end
    else
        if self.bodyPart:getBandageType() then
            local bandage = self.character:getInventory():AddItem(self.bodyPart:getBandageType())
            if self.bodyPart:getBandageLife() <= 0 then
                bandage:Use();
            end
--            else
--                self.character:getInventory():AddItem(self.bodyPart:getBandageType() .. "Dirty");
--            end
        end
        self.otherPlayer:getBodyDamage():SetBandaged(self.bodyPart:getIndex(), false, 0, false, nil);
--        self.otherPlayer:removeBandageModel(self.bodyPart:getType():getBandageModel());
        if isClient() then
            sendBandage(self.otherPlayer:getOnlineID(), self.bodyPart:getIndex(), false, 0, false, nil);
        end
    end
    ISHealthPanel.setBodyPartActionForPlayer(self.otherPlayer, self.bodyPart, nil, nil, nil)
end

function ISApplyBandage:new(doctor, otherPlayer, item, bodyPart, doIt)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = doctor;
    o.otherPlayer = otherPlayer;
    o.doctorLevel = doctor:getPerkLevel(Perks.Doctor);
	o.item = item;
    o.doIt = doIt;
	o.bodyPart = bodyPart;
	o.stopOnWalk = bodyPart:getIndex() > BodyPartType.ToIndex(BodyPartType.Groin);
	o.stopOnRun = true;
    o.bandagedPlayerX = otherPlayer:getX();
    o.bandagedPlayerY = otherPlayer:getY();
    o.maxTime = 120 - (o.doctorLevel * 4);
    if doctor:isTimedActionInstant() then
        o.maxTime = 1;
        o.doctorLevel = 10;
    end
	return o;
end
