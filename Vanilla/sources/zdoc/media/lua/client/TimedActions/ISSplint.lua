--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISSplint : ISBaseTimedAction
ISSplint = ISBaseTimedAction:derive("ISSplint");

function ISSplint:isValid()
	if ISHealthPanel.DidPatientMove(self.character, self.otherPlayer, self.bandagedPlayerX, self.bandagedPlayerY) then
		return false
	end
    if self.rippedSheet and self.plank then
        return self.character:getInventory():contains(self.rippedSheet) and self.character:getInventory():contains(self.plank)
    else
        return true
    end
end

function ISSplint:waitToStart()
    if self.character == self.otherPlayer then
        return false
    end
    self.character:faceThisObject(self.otherPlayer)
    return self.character:shouldBeTurning()
end

function ISSplint:update()
    if self.character ~= self.otherPlayer then
        self.character:faceThisObject(self.otherPlayer)
    end
    if self.rippedSheet and self.plank then
        self.rippedSheet:setJobDelta(self:getJobDelta());
        self.plank:setJobDelta(self:getJobDelta());
    end
    local jobType = self.doIt and getText("ContextMenu_Splint") or getText("ContextMenu_Remove_Splint")
    ISHealthPanel.setBodyPartActionForPlayer(self.otherPlayer, self.bodyPart, self, jobType, { splint = true })

    self.character:setMetabolicTarget(Metabolics.LightDomestic);
end

function ISSplint:start()
    if self.rippedSheet and self.plank then
        self.rippedSheet:setJobType(self.doIt and getText("ContextMenu_Splint") or getText("ContextMenu_Remove_Splint"));
        self.rippedSheet:setJobDelta(0.0);
        self.plank:setJobType(self.doIt and getText("ContextMenu_Splint") or getText("ContextMenu_Remove_Splint"));
        self.plank:setJobDelta(0.0);
    end
    if self.character == self.otherPlayer then
        self:setActionAnim(CharacterActionAnims.Bandage);
        self:setAnimVariable("BandageType", ISHealthPanel.getBandageType(self.bodyPart));
        self.character:reportEvent("EventBandage");
    else
        self:setActionAnim("Loot")
        self.character:SetVariable("LootPosition", "Mid");
        self.character:reportEvent("EventLootItem");
    end
    self:setOverrideHandModels(nil, nil);
end

function ISSplint:stop()
    if self.rippedSheet and self.plank then
        self.rippedSheet:setJobDelta(0.0);
        self.plank:setJobDelta(0.0);
    end
    ISHealthPanel.setBodyPartActionForPlayer(self.otherPlayer, self.bodyPart, nil, nil, nil)
    ISBaseTimedAction.stop(self);
end

function ISSplint:perform()
    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self);
    if self.rippedSheet and self.plank then
        self.rippedSheet:setJobDelta(0.0);
        self.plank:setJobDelta(0.0);
    end
    -- random a bandage life depending on the doctor skill
    if self.doIt then
        local splintFactor = (self.doctorLevel + 1) / 2;
        if self.bodyPart:isGetSplintXp() then
            self.character:getXp():AddXP(Perks.Doctor, 15);
        end
        self.bodyPart:setSplint(true, splintFactor);
        if self.rippedSheet then
            self.character:getInventory():Remove(self.rippedSheet);
        end
        self.character:getInventory():Remove(self.plank);
        self.bodyPart:setSplintItem(self.plank:getModule() .. "." .. self.plank:getType());
        if isClient() then
            sendSplint(self.otherPlayer:getOnlineID(), self.bodyPart:getIndex(), true, splintFactor, self.plank:getModule() .. "." .. self.plank:getType());
        end
    else
        if self.bodyPart:getSplintItem() then
            if self.bodyPart:getSplintItem() ~= "Base.Splint" then
                self.character:getInventory():AddItem("Base.RippedSheets");
            end
            self.character:getInventory():AddItem(self.bodyPart:getSplintItem());
        end
        self.bodyPart:setSplint(false, 0);
        if isClient() then
            sendSplint(self.otherPlayer:getOnlineID(), self.bodyPart:getIndex(), false, 0, null);
        end
    end
    ISHealthPanel.setBodyPartActionForPlayer(self.otherPlayer, self.bodyPart, nil, nil, nil)
end

function ISSplint:new(doctor, otherPlayer, rippedSheet, plank, bodyPart, doIt)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = doctor;
    o.otherPlayer = otherPlayer;
    o.doctorLevel = doctor:getPerkLevel(Perks.Doctor);
	o.rippedSheet = rippedSheet;
    o.plank = plank;
    o.doIt = doIt;
    o.doctor = doctor;
	o.bodyPart = bodyPart;
	o.stopOnWalk = true;
	o.stopOnRun = true;
    o.bandagedPlayerX = otherPlayer:getX();
    o.bandagedPlayerY = otherPlayer:getY();
    o.maxTime = 140 - (o.doctorLevel * 4);
    if doctor:isTimedActionInstant() then
        o.maxTime = 1;
    end
    if doctor:getAccessLevel() ~= "None" then
        o.doctorLevel = 10;
    end
	return o;
end
