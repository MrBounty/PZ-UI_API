--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISRadioAction : ISBaseTimedAction
ISRadioAction = ISBaseTimedAction:derive("ISRadioAction")

function ISRadioAction:isValid()
    if self.character and self.device and self.deviceData and self.mode then
        if self["isValid"..self.mode] then
            return self["isValid"..self.mode](self);
        end
    end
end

function ISRadioAction:update()
    if self.character and self.deviceData and self.deviceData:isIsoDevice() then
        self.character:faceThisObject(self.deviceData:getParent())
    end
end

function ISRadioAction:perform()
    if self.character and self.device and self.deviceData and self.mode then
        if self["perform"..self.mode] then
            self["perform"..self.mode](self);
        end
    end

    ISBaseTimedAction.perform(self)
end

-- ToggleOnOff
function ISRadioAction:isValidToggleOnOff()
    return self.deviceData:getIsBatteryPowered() and self.deviceData:getPower()>0 or self.deviceData:canBePoweredHere();
end

function ISRadioAction:performToggleOnOff()
    if self:isValidToggleOnOff() then
        self.deviceData:setIsTurnedOn( not self.deviceData:getIsTurnedOn() );
    end
end

-- RemoveBattery
function ISRadioAction:isValidRemoveBattery()
    return self.deviceData:getIsBatteryPowered() and self.deviceData:getHasBattery();
end

function ISRadioAction:performRemoveBattery()
    if self:isValidRemoveBattery() and self.character:getInventory() then
        self.deviceData:getBattery(self.character:getInventory());
    end
end

-- AddBattery
function ISRadioAction:isValidAddBattery()
    return self.deviceData:getIsBatteryPowered() and self.deviceData:getHasBattery() == false;
end

function ISRadioAction:performAddBattery()
    if self:isValidAddBattery() and self.secondaryItem then
        self.deviceData:addBattery(self.secondaryItem);
    end
end

-- SetChannel
function ISRadioAction:isValidSetChannel()
    if (not self.secondaryItem) and type(self.secondaryItem)~="number" then return false; end
    return self.deviceData:getIsTurnedOn() and self.deviceData:getPower()>0;
end

function ISRadioAction:performSetChannel()
    if self:isValidSetChannel() then
        self.deviceData:setChannel(self.secondaryItem);
    end
end

-- SetVolume
function ISRadioAction:isValidSetVolume()
    if (not self.secondaryItem) and type(self.secondaryItem)~="number" then return false; end
    return self.deviceData:getIsTurnedOn() and self.deviceData:getPower()>0;
end

function ISRadioAction:performSetVolume()
    if self:isValidSetVolume() then
        self.deviceData:setDeviceVolume(self.secondaryItem);
    end
end

-- MuteMicrophone
function ISRadioAction:isValidMuteMicrophone()
    if (not self.secondaryItem) and type(self.secondaryItem)~="boolean" then return false; end
    return self.deviceData:getIsTurnedOn() and self.deviceData:getPower()>0;
end

function ISRadioAction:performMuteMicrophone()
    if self:isValidMuteMicrophone() then
        self.deviceData:setMicIsMuted(self.secondaryItem);
    end
end

-- RemoveHeadphones
function ISRadioAction:isValidRemoveHeadphones()
    return self.deviceData:getHeadphoneType() >= 0;
end

function ISRadioAction:performRemoveHeadphones()
    if self:isValidRemoveHeadphones() and self.character:getInventory() then
        self.deviceData:getHeadphones(self.character:getInventory());
    end
end

-- AddHeadphones
function ISRadioAction:isValidAddHeadphones()
    return self.deviceData:getHeadphoneType() < 0;
end

function ISRadioAction:performAddHeadphones()
    if self:isValidAddHeadphones() and self.secondaryItem then
        self.deviceData:addHeadphones(self.secondaryItem);
    end
end

-- TogglePlayMedia
function ISRadioAction:isValidTogglePlayMedia()
    return self.deviceData:getIsTurnedOn() and self.deviceData:hasMedia();
end

function ISRadioAction:performTogglePlayMedia()
    if self:isValidTogglePlayMedia() then
        if self.deviceData:isPlayingMedia() then
            self.deviceData:StopPlayMedia();
        else
            self.deviceData:StartPlayMedia();
        end
    end
end

-- AddMedia
function ISRadioAction:isValidAddMedia()
    return (not self.deviceData:hasMedia()) and self.deviceData:getMediaType() == self.secondaryItem:getMediaType();
end

function ISRadioAction:performAddMedia()
    if self:isValidAddMedia() and self.secondaryItem then
        self.deviceData:addMediaItem(self.secondaryItem);
    end
end

-- RemoveMedia
function ISRadioAction:isValidRemoveMedia()
    return self.deviceData:hasMedia();
end

function ISRadioAction:performRemoveMedia()
    if self:isValidRemoveMedia() and self.character:getInventory() then
        self.deviceData:removeMediaItem(self.character:getInventory());
    end
end

function ISRadioAction:new(mode, character, device, secondaryItem)
    local o             = {};
    setmetatable(o, self);
    self.__index        = self;
    o.mode              = mode;
    o.character         = character;
    o.device            = device;
    o.deviceData        = device and device:getDeviceData();
    o.secondaryItem     = secondaryItem;

    o.stopOnWalk        = false;
    o.stopOnRun         = true;
    o.maxTime           = 30;

    return o;
end
