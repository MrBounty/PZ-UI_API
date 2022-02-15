--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISPaintSignAction : ISBaseTimedAction
ISPaintSignAction = ISBaseTimedAction:derive("ISPaintSignAction");

function ISPaintSignAction:isValid()
	return true;
end

function ISPaintSignAction:update()
    self.character:setMetabolicTarget(Metabolics.HeavyDomestic);
end

function ISPaintSignAction:start()
    self:setActionAnim(CharacterActionAnims.Paint)
    self:setOverrideHandModels("PaintBrush", nil);
    self.character:faceThisObject(self.wall)
    self.sound = self.character:playSound("Painting")
end

function ISPaintSignAction:stop()
    if self.sound then self.character:stopOrTriggerSound(self.sound) end
    ISBaseTimedAction.stop(self);
end

function ISPaintSignAction:perform()
    if self.sound then self.character:stopOrTriggerSound(self.sound) end
    if self.wall:getProperties():Is("WallW") then
        self.sign = self.sign + 8;
    end
    self.wall:setOverlaySprite("constructedobjects_signs_01_" .. self.sign, self.r,self.g,self.b,1);
    if not ISBuildMenu.cheat then
        self.paintPot:Use();
    end
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISPaintSignAction:new(character, wall, paintPot, sign,r,g,b)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.wall = wall;
	o.paintPot = paintPot;
	o.sign = sign;
    o.r = r;
    o.g = g;
    o.b = b;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = 100;
    if ISBuildMenu.cheat then o.maxTime = 1; end
    o.caloriesModifier = 4;
	return o;
end
