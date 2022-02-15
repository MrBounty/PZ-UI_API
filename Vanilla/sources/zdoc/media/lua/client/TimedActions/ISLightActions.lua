--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISLightActions : ISBaseTimedAction
ISLightActions = ISBaseTimedAction:derive("ISLightActions");
ISLightActions.perkLevel = 5; --Electrical perkLevel needed for modifying lamps

function ISLightActions:isValid()
    if self.character and self.lightswitch and self.mode then
        if self["isValid"..self.mode] then
            return self["isValid"..self.mode](self);
        end
    end
end

function ISLightActions:perform()
    if self.character and self.lightswitch and self.mode then
        if self["perform"..self.mode] then
            self["perform"..self.mode](self);
        end
    end

    ISBaseTimedAction.perform(self)
end

-- AddLightBulb
function ISLightActions:isValidAddLightBulb()
    return self.item and self.lightswitch:getCanBeModified() and self.lightswitch:hasLightBulb()==false;
end

function ISLightActions:performAddLightBulb()
    if self:isValidAddLightBulb() then
        self.lightswitch:addLightBulb(self.character, self.item);
    end
end

-- RemoveLightBulb
function ISLightActions:isValidRemoveLightBulb()
    return self.lightswitch:getCanBeModified() and self.lightswitch:hasLightBulb();
end

function ISLightActions:performRemoveLightBulb()
    if self:isValidRemoveLightBulb() then
        self.lightswitch:removeLightBulb(self.character);
    end
end

-- ModifyLamp
function ISLightActions:isValidModifyLamp()
    return self.item and self.lightswitch:getCanBeModified() and self.lightswitch:getUseBattery()==false and self.character:getPerkLevel(Perks.Electricity) >= ISLightActions.perkLevel;
end

function ISLightActions:performModifyLamp()
    if self:isValidModifyLamp() and self.character:getInventory():contains(self.item) and self.item:getFullType()=="Base.ElectronicsScrap" then
        self.character:removeFromHands(self.item)
        self.character:getInventory():Remove(self.item);
        self.lightswitch:setUseBattery(true);
    end
end

-- AddBattery
function ISLightActions:isValidAddBattery()
    return self.item and self.lightswitch:getCanBeModified() and self.lightswitch:getUseBattery() and self.lightswitch:getHasBattery()==false;
end

function ISLightActions:performAddBattery()
    if self:isValidAddBattery() then
        self.lightswitch:addBattery(self.character, self.item);
    end
end

-- RemoveLightBulb
function ISLightActions:isValidRemoveBattery()
    return self.lightswitch:getCanBeModified() and self.lightswitch:getUseBattery() and self.lightswitch:getHasBattery();
end

function ISLightActions:performRemoveBattery()
    if self:isValidRemoveBattery() then
        self.lightswitch:removeBattery(self.character);
    end
end

function ISLightActions:new(mode, character, lightswitch, item)
    local o             = {};
    setmetatable(o, self);
    self.__index        = self;
    o.mode              = mode;
    o.character         = character;
    o.lightswitch       = lightswitch;
    o.item              = item;
    o.stopOnWalk        = true;
    o.stopOnRun         = true;
    o.maxTime           = 300;
    if mode=="AddLightBulb" or mode=="RemoveLightBulb" then
        o.maxTime = 120;
    elseif mode=="AddBattery" or mode=="RemoveBattery" then
        o.maxTime = 60;
    end

    return o;
end
