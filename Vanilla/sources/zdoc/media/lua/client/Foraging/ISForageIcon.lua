--[[---------------------------------------------
-------------------------------------------------
--
-- ISForageIcon
--
-- eris
--
-------------------------------------------------
--]]---------------------------------------------
require "Foraging/forageSystem";
require "ISUI/ISPanel";
require "Foraging/ISBaseIcon";
---@class ISForageIcon : ISBaseIcon
ISForageIcon = ISBaseIcon:derive("ISForageIcon");
-------------------------------------------------
-------------------------------------------------
function ISForageIcon:onRightMouseUp()		return self:doContextMenu();							end;
function ISForageIcon:onRightMouseDown()	return (self:getIsSeen() and self:getAlpha() > 0);		end;
-------------------------------------------------
-------------------------------------------------
function ISForageIcon:onClickDiscard(_x, _y, _contextOption)
	if _contextOption then _contextOption:hideAndChildren(); end;
	local targetSquare = getCell():getGridSquare(self.xCoord, self.yCoord, self.zCoord);
	if not targetSquare then return; end;
	---
	ISTimedActionQueue.add(ISForageAction:new(self, self.character:getInventory(), true));
end

function ISForageIcon:doForage(_x, _y, _contextOption, _targetContainer)
	if _contextOption then _contextOption:hideAndChildren(); end;
	local targetSquare = getCell():getGridSquare(self.xCoord, self.yCoord, self.zCoord);
	if not targetSquare then return; end;
	---
	local targetContainer = _targetContainer or self.character:getInventory();
	if targetContainer and targetSquare and luautils.walkAdj(self.character, targetSquare) then
		ISTimedActionQueue.add(ISForageAction:new(self, targetContainer, false));
	end;
end
-------------------------------------------------
-------------------------------------------------
function ISForageIcon:updatePinIconPosition()
	local dx, dy = self:getScreenDelta();
	self:setX(isoToScreenX(self.player, self.xCoord, self.yCoord, self.zCoord) + dx + (-self.textureCenter / 2 / self.zoom));
	self:setY(isoToScreenY(self.player, self.xCoord, self.yCoord, self.zCoord) + dy + (self.pinOffset / self.zoom));
	self:setY(self.y - (30 / self.zoom) - (self.height) + (math.sin(self.bounceStep) * self.bounceHeight));
end
-------------------------------------------------
-------------------------------------------------
function ISForageIcon:checkIsIdentified()
	local perkLevel = self.character:getPerkLevel(Perks.FromString(self.catDef.showOptionPerk));
	if perkLevel >= self.catDef.showCategoryLevel then
		self.identified = self.distanceToPlayer <= self.identifyDistance;
	else
		self.identified = false;
	end;
end

function ISForageIcon:initItemCount()
	if self.itemDef then
		if self.itemDef.minCount == self.itemDef.maxCount then
			self.itemCount = self.itemDef.minCount;
		else
			self.itemCount = ZombRand(self.itemDef.minCount, self.itemDef.maxCount) + 1;
		end;
	end;
end

function ISForageIcon:initialise()
	ISBaseIcon.initialise(self);
	self:checkIsIdentified();
	self:setRenderThisPlayerOnly(self.player);
	----
	if self.render3DTexture then
		self.renderWorldIcon = self.render3DItem;
	elseif self.altWorldTexture then
		self.renderWorldIcon = self.renderAltWorldTexture;
		self:initAltTexture();
	elseif self.itemTexture then
		self.renderWorldIcon = self.renderWorldItemTexture;
	end;
	---
	self:findTextureCenter();
	self:findPinOffset();
	self:initItemCount();
end
-------------------------------------------------
-------------------------------------------------
function ISForageIcon:new(_manager, _forageIcon, _zoneData)
	local o = {};
	o = ISBaseIcon:new(_manager, _forageIcon, _zoneData);
	setmetatable(o, self)
	self.__index = self;
	o.zoneData = _zoneData;
	o.zoneDef = forageSystem.zoneDefs[_zoneData.name];
	o.catDef = forageSystem.catDefs[_forageIcon.catName];
	o.itemDef = forageSystem.itemDefs[_forageIcon.itemType];
	o.onMouseDoubleClick = ISForageIcon.doForage;
	o.identifyDistance = 0;
	o.altWorldTexture = o.itemDef.altWorldTexture;
	o.render3DTexture = o.itemDef.render3DTexture;
	o.textureCenter = 0;
	o.itemCount = 1;
	o.isMover = o.itemDef.isMover or false;
	o.onClickContext = ISForageIcon.doForage;
	o.identified = false;
	o.canMoveVertical = true;
	o.iconClass = "forageIcon";
	o:initialise();
	return o;
end
-------------------------------------------------
-------------------------------------------------
