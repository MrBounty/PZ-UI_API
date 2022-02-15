--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISMoveablesAction : ISBaseTimedAction
ISMoveablesAction = ISBaseTimedAction:derive("ISMoveablesAction")

function ISMoveablesAction:isValid()
    local plSquare = self.character:getSquare();
    if plSquare and self.square then
        if self.square:isSomethingTo(plSquare) or self.square:getZ() ~= plSquare:getZ() then
            self:stop();
            return false;
        end;
    else
        self:stop();
        return false;
    end;
    return true;
end

function ISMoveablesAction:waitToStart()
    if self.mode and self.mode=="scrap" and self.moveProps and self.moveProps.object then
        self.character:faceThisObject(self.moveProps.object)
    else
        self.character:faceLocation(self.square:getX(), self.square:getY())
    end
    return self.character:shouldBeTurning()
end

function ISMoveablesAction:update()
    if self.mode and self.mode=="scrap" and self.moveProps and self.moveProps.object then
        self.character:faceThisObject(self.moveProps.object);
    else
        self.character:faceLocation(self.square:getX(), self.square:getY());
    end
    if self.sound and not self.character:getEmitter():isPlaying(self.sound) then
        self:setActionSound();
    end

    self.character:setMetabolicTarget(Metabolics.UsingTools);
end

function ISMoveablesAction:setActionSound()
    if self.mode == "scrap" then
        self.sound = self.moveProps:getScrapSound( self.character );
    else
        self.sound = self.moveProps:getSoundFromTool( self.square, self.character, self.mode );
    end
end

function ISMoveablesAction:start()
    self:setActionSound();
    if self.sound and self.sound ~= 0 then
        --self.sound = sound;
        self.character:stopOrTriggerSound(self.sound);
    end
    if self.mode and self.mode=="scrap" then
        if self.moveProps and self.moveProps:startScrapAction(self) then
            -- Hack for scrapping curtains
        elseif self.character:hasEquipped("BlowTorch") then
            self:setActionAnim("BlowTorch")
            self:setOverrideHandModels(self.character:getPrimaryHandItem(), nil)
        elseif self.character:hasEquippedTag("Hammer") then
            self:setActionAnim(CharacterActionAnims.Build)
            self:setOverrideHandModels(self.character:getPrimaryHandItem(), nil)
        else
            self:setActionAnim(CharacterActionAnims.Disassemble);
            self:setOverrideHandModels("Screwdriver", nil);
        end
    end
end

function ISMoveablesAction:stop()
    if self.sound and self.sound ~= 0 then
        self.character:stopOrTriggerSound(self.sound);
    end
    ISBaseTimedAction.stop(self)
end

--[[
-- The moveprops of the new facing (where applies) are always used to perform the actions, the origSpriteName is passed to retrieve the original object from tile or inventory.
 ]]
function ISMoveablesAction:perform()
    if self.sound and self.sound ~= 0 then
        self.character:stopOrTriggerSound(self.sound);
    end

    if self.moveProps and self.moveProps.isMoveable and self.mode and self.mode ~= "scrap" then
        self.moveProps.cursorFacing = self.cursorFacing
        if self.mode == "pickup" then
            self.moveProps:pickUpMoveableViaCursor( self.character, self.square, self.origSpriteName, self.moveCursor ); --OrigSpriteName currently not used in this one.
        elseif self.mode == "place" then
            self.moveProps:placeMoveableViaCursor( self.character, self.square, self.origSpriteName, self.moveCursor );
        elseif self.mode == "rotate" then
            self.moveProps:rotateMoveableViaCursor( self.character, self.square, self.origSpriteName, self.moveCursor );
        end
        self.moveProps.cursorFacing = nil
    elseif self.mode and self.mode=="scrap" then
        self.moveProps:scrapObjectViaCursor( self.character, self.square, self.origSpriteName, self.moveCursor );
    end

    ISBaseTimedAction.perform(self)
end

function ISMoveablesAction:new(character, _sq, _moveProps, _mode, _origSpriteName, _moveCursor )
    local o             = {};
    setmetatable(o, self);
    self.__index        = self;
    o.character         = character;
    o.square            = _sq;
    o.origSpriteName    = _origSpriteName;
    o.stopOnWalk        = true;
    o.stopOnRun         = true;
    o.maxTime           = 50;
    o.spriteFrame       = 0;
    o.mode              = _mode;
    o.moveProps         = _moveProps;
    o.moveCursor        = _moveCursor;

    if _moveCursor and (_mode == "place" or _mode == "rotate") and _moveProps:canRotateDirection() then
        o.cursorFacing = _moveCursor.cursorFacing or _moveCursor.joypadFacing
    end

    if ISMoveableDefinitions.cheat then
        o.maxTime = 10;
    else
        if o.moveProps and o.moveProps.isMoveable and _mode and _mode~="rotate" and _mode~= "scrap" then
            o.maxTime = o.moveProps:getActionTime( character, _mode );
        elseif o.moveProps and _mode == "scrap" then
            o.maxTime = o.moveProps:getScrapActionTime( character );
        end
    end
    return o;
end
