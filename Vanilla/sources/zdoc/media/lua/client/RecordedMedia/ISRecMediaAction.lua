--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

--[[
require "TimedActions/ISBaseTimedAction"

---@class ISRecMediaAction : ISBaseTimedAction
ISRecMediaAction = ISBaseTimedAction:derive("ISRecMediaAction")

function ISRecMediaAction:isValid()
    if self.character and self.device and self.deviceData and self.mode then
        if self["isValid"..self.mode] then
            return self["isValid"..self.mode](self);
        end
    end
end

function ISRecMediaAction:update()
    if self.character and self.deviceData and self.deviceData:isIsoDevice() then
        self.character:faceThisObject(self.deviceData:getParent())
    end
end

function ISRecMediaAction:perform()
    if self.character and self.device and self.deviceData and self.mode then
        if self["perform"..self.mode] then
            self["perform"..self.mode](self);
        end
    end

    ISBaseTimedAction.perform(self)
end

-- Play Media
function ISRecMediaAction:isValidPlayMedia()
    return self.deviceData:getIsTurnedOn();
end

function ISRecMediaAction:performPlayMedia()
    if self:isValidPlayMedia() then
        print("start playing media");
        --todo play the media
        --self.deviceData:setIsTurnedOn( not self.deviceData:getIsTurnedOn() );
    end
end

function ISRecMediaAction:new(mode, character, device, secondaryItem)
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
--]]
