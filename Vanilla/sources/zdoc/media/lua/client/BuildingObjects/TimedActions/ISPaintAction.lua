--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISPaintAction : ISBaseTimedAction
ISPaintAction = ISBaseTimedAction:derive("ISPaintAction");

function ISPaintAction:isValid()
	return true;
end

function ISPaintAction:waitToStart()
    self.character:faceThisObject(self.thumpable)
    return self.character:shouldBeTurning()
end

function ISPaintAction:update()
    self.character:faceThisObject(self.thumpable)
    self.character:setMetabolicTarget(Metabolics.LightWork);
end

function ISPaintAction:start()
    self:setActionAnim(CharacterActionAnims.Paint)
    self:setOverrideHandModels("PaintBrush", nil)
    self.character:faceThisObject(self.thumpable)
    self.sound = self.character:playSound("Painting")
end

function ISPaintAction:stop()
    if self.sound then self.character:stopOrTriggerSound(self.sound) end
    ISBaseTimedAction.stop(self);
end

function ISPaintAction:perform()
    if self.sound then self.character:stopOrTriggerSound(self.sound) end
    if self.thumpable:getSprite() == getSprite("carpentry_01_16") then
        self.thumpable:setSpriteFromName("carpentry_02_104")
    end
	local modData = self.thumpable:getModData();
    local north = "";
    if self.isThump then
        if self.thumpable:getNorth() then
            north = "North";
        end
    else
        if self.thumpable:getSprite():getProperties():Is("WallN") == true or self.thumpable:getSprite():getProperties():Is(IsoFlagType.WindowN) == true or self.thumpable:getSprite():getProperties():Is("DoorWallN") == true then
            north = "North";
        end
        if self.thumpable:getSprite():getProperties():Is("WallNW") == true then
            north = "Corner";
        end
    end
    local sprite = nil;
    local paintingType = self.thumpable:getSprite():getProperties():Val("PaintingType");
    if self.isThump then
        if Painting[modData["wallType"]] then
            sprite = Painting[modData["wallType"]][self.painting .. north];
        end
    elseif Painting[paintingType] then
        sprite = Painting[paintingType][self.painting .. north];
    end
	self.thumpable:cleanWallBlood();
    if not sprite then
        local color = OtherPainting[paintingType][self.painting];
        self.thumpable:setCustomColor(ColorInfo.new(color.r, color.g, color.b, 1));
        self.thumpable:transmitCustomColor();
	else
	    self.thumpable:setSpriteFromName(sprite);
        self.thumpable:transmitUpdatedSpriteToServer();
    end
    if not ISBuildMenu.cheat then
        self.paintPot:Use();
    end
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISPaintAction:new(character, thumpable, paintPot, painting, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.thumpable = thumpable;
	o.painting = painting;
	o.paintPot = paintPot;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
    o.isThump = true;
    if not instanceof(thumpable, "IsoThumpable") then
        o.isThump = false;
    end
    if ISBuildMenu.cheat then o.maxTime = 1; end
    o.caloriesModifier = 4;
	return o;
end
