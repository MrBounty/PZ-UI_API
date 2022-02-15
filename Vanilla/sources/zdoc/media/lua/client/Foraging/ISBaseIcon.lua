--[[---------------------------------------------
-------------------------------------------------
--
-- ISBaseIcon
--
-- eris
--
-------------------------------------------------
--]]---------------------------------------------
require "Foraging/forageSystem";
require "ISUI/ISPanel";
---@class ISBaseIcon : ISPanel
ISBaseIcon = ISPanel:derive("ISBaseIcon");
-------------------------------------------------
-------------------------------------------------
local pinIconBlank = getTexture("media/textures/Foraging/pinIconBlank.png");
local pinIconUnknown = getTexture("media/textures/Foraging/pinIconUnknown.png");
-------------------------------------------------
-------------------------------------------------
local math = math;
local getTimestampMs = getTimestampMs;
-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:isValid() 				return true; 					end;

function ISBaseIcon:onRightMouseUp() 		return false; 					end;
function ISBaseIcon:onRightMouseDown()		return false;					end;

function ISBaseIcon:getAlpha()		 		return self.textureColor.a; 	end;
function ISBaseIcon:getColor()			 	return self.textureColor; 		end;
function ISBaseIcon:setAlpha(_a)			self.textureColor.a = _a;		end;
function ISBaseIcon:setColor(_rgba) 		self.textureColor = _rgba;		end;

function ISBaseIcon:getGridSquare()			return self:initGridSquare();	end;

function ISBaseIcon:updateMover() 			 								end;

function ISBaseIcon:prerender() 											end;
function ISBaseIcon:renderWorldIcon() 										end;
-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:setIsBeingRemoved(_isBeingRemoved)
	self.isBeingRemoved = _isBeingRemoved;
end;
-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:doPickup()				return false;					end;
-------------------------------------------------
-------------------------------------------------
--todo: refactor - move grab menu constructor to a new method
function ISBaseIcon:doContextMenu(_context)
	if self:getIsSeen() and self:getAlpha() > 0 then
		self:getGridSquare();
		if not self.square then return; end;
		---
		local contextMenu;
		if _context then
			local forageSubOption = _context:addOption(getText("IGUI_perks_Foraging"), self);
			contextMenu = ISContextMenu:getNew(_context);
			contextMenu:addSubMenu(forageSubOption, contextMenu);
		else
			contextMenu = ISContextMenu.get(self.player, getMouseX(), getMouseY());
		end
		---
		if not contextMenu then return; end;
		---
		local plInventory = self.character:getInventory();
		local contextName = getText("IGUI_Pickup").." "..getText("UI_foraging_UnknownItem");
		---
		if self.identified then contextName = getText("IGUI_Pickup").." "..self.itemObj:getDisplayName(); end;
		---
		local contextOption = contextMenu:addOption(contextName, self, nil, contextMenu, plInventory);
		local subMenu = ISContextMenu:getNew(contextMenu);
		local bpList = getPlayerInventory(self.player).backpacks;
		---
		local plInvOption = subMenu:addOption(getText("ContextMenu_PutInContainer", getText("ContextMenu_MoveToInventory")), self, self.onClickContext, 0, 0, contextMenu, plInventory, {self.itemObj});
		if plInventory:hasRoomFor(self.character, self.itemObj) then
			if self.itemObjTable then
				local itemTable = {};
				for _, itemObj in pairs(self.itemObjTable) do
					if itemObj and itemObj:getWorldItem() then
						table.insert(itemTable, itemObj);
					end;
				end;
				if #itemTable > 1 then
					plInvOption.onSelect = nil
					local subMenuGrab = ISContextMenu:getNew(contextMenu);
					contextMenu:addSubMenu(plInvOption, subMenuGrab);
					subMenuGrab:addOption(getText("ContextMenu_Grab_one"), self, self.onClickContext, 0, 0, contextMenu, plInventory, {self.itemObj});
					if #itemTable > 2 then
						subMenuGrab:addOption(getText("ContextMenu_Grab_half"), self, self.onClickContext, 0, 0, contextMenu, plInventory, {unpack(itemTable, 1, math.ceil(#itemTable / 2))});
					end;
					subMenuGrab:addOption(getText("ContextMenu_Grab_all"), self, self.onClickContext, 0, 0, contextMenu, plInventory, itemTable);
				end;
			end;
		else
			plInvOption.onSelect = nil;
			plInvOption.notAvailable = true;
		end;
		for _, backpack in ipairs(bpList) do
			local bpItem = backpack and backpack.inventory and backpack.inventory:getContainingItem();
			if bpItem then
				if not backpack.inventory:getOnlyAcceptCategory() or (self.itemObj:getCategory() == backpack.inventory:getOnlyAcceptCategory()) then
					local backPackOption = subMenu:addOption(getText("ContextMenu_PutInContainer", bpItem:getDisplayName()), self, self.onClickContext, 0, 0, contextMenu, backpack.inventory, {self.itemObj});
					if not backpack.inventory:hasRoomFor(self.character, self.itemObj) then
						backPackOption.onSelect = nil;
						backPackOption.notAvailable = true;
					else
						if self.itemObjTable then
							local itemTable = {};
							for _, itemObj in pairs(self.itemObjTable) do
								if itemObj and itemObj:getWorldItem() then
									table.insert(itemTable, itemObj);
								end;
							end;
							---
							if #itemTable > 1 then
								backPackOption.onSelect = nil
								local subMenuGrab = ISContextMenu:getNew(contextMenu);
								contextMenu:addSubMenu(backPackOption, subMenuGrab);
								subMenuGrab:addOption(getText("ContextMenu_Grab_one"), self, self.onClickContext, 0, 0, contextMenu, backpack.inventory, {self.itemObj});
								if #itemTable > 2 then
									subMenuGrab:addOption(getText("ContextMenu_Grab_half"), self, self.onClickContext, 0, 0, contextMenu, backpack.inventory, {unpack(itemTable, 1, math.ceil(#itemTable / 2))});
								end;
								subMenuGrab:addOption(getText("ContextMenu_Grab_all"), self, self.onClickContext, 0, 0, contextMenu, backpack.inventory, itemTable);
							end;
						end;
					end;
				end;
			end;
		end;
		contextMenu:addSubMenu(contextOption, subMenu);
		if self.onClickDiscard then
			if self.identified then
				contextName = getText("UI_foraging_DiscardItem").." "..self.itemObj:getDisplayName();
			else
				contextName = getText("UI_foraging_DiscardItem").." "..getText("UI_foraging_UnknownItem");
			end;
			contextOption = contextMenu:addOption(contextName, self, self.onClickDiscard, contextMenu);
		end;
		---
		triggerEvent("onFillSearchIconContextMenu", contextMenu, self);
		return false;
	end;
	return false;
end
-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:render3DItem()
	if self.itemObj and self.square then
		Render3DItem(self.itemObj, self.square, self.xCoord, self.yCoord, self.zCoord, self.itemRotation);
	end;
end

function ISBaseIcon:renderAltWorldTexture()
	self:updateZoom();
	self:updateAlpha();
	local dx, dy = self:getScreenDelta();
	self:setX(isoToScreenX(self.player, self.xCoord, self.yCoord, self.zCoord) + dx);
	self:setY(isoToScreenY(self.player, self.xCoord, self.yCoord, self.zCoord) + dy);
	--local tc = self.textureColor;
	--for _, texture in ipairs(self.altWorldTexture) do
	--	self:drawTextureScaled(texture, -(texture:getWidth() / 2 / self.zoom), -(texture:getHeight() / self.zoom), texture:getWidth() / self.zoom, texture:getHeight() / self.zoom, tc.a, tc.r, tc.g, tc.b);  --render world texture
	--end;
end

function ISBaseIcon:renderWorldItemTexture()
	self:updateZoom();
	self:updateAlpha();
	local dx, dy = self:getScreenDelta();
	self:setX(isoToScreenX(self.player, self.xCoord, self.yCoord, self.zCoord) - self.itemTexture:getWidth() / 2 / self.zoom + dx);
	self:setY(isoToScreenY(self.player, self.xCoord, self.yCoord, self.zCoord) + dy);
	local tc = self.textureColor;
	--self:drawTextureScaled(self.itemTexture, 0, 0, self.itemTexture:getWidth() / self.zoom, self.itemTexture:getHeight() / self.zoom, tc.a, tc.r, tc.g, tc.b);  --render world icon
end

function ISBaseIcon:renderPinIcon()
	local tc = self.textureColor;
	if self.itemTexture and self.identified then
		local textureCenter = (self.itemTexture:getWidth() / 1.5 / self.zoom) / 2;
		local pinCenter = self.width / 2;
		self:drawTextureScaled(self.texture, 0, 0, self.width, self.height, self.pinAlpha, tc.r, tc.g, tc.b);
		self:drawTextureScaled(self.itemTexture, (pinCenter - textureCenter), 5 / self.zoom, self.itemTexture:getWidth() / 1.5 / self.zoom, self.itemTexture:getHeight() / 1.5 / self.zoom, self.pinAlpha, tc.r, tc.g, tc.b);
	else
		self:drawTextureScaled(pinIconUnknown, 0, 0, self.width, self.height, self.pinAlpha, tc.r, tc.g, tc.b);
	end;
end

function ISBaseIcon:render()
	if self:getIsSeen() then
		self:updateAlpha();
		self:updateBounce();
		self:updateZoom();
		self:updatePinIconPosition();
		self:updatePinIconSize();
		self:renderPinIcon();
	else
		self:resetBounce();
	end;
end
-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:getAngleOffset2D(_angle1, _angle2)
	return 180 - math.abs(math.abs(_angle1 - _angle2) - 180);
end

function ISBaseIcon:getAngle2D(_x1, _y1, _x2, _y2)
	local angle = math.atan2(_x1 - _x2, -(_y1 - _y2));
	if angle < 0 then angle = math.abs(angle) else angle = 2 * math.pi - angle end;
	return math.deg(angle);
end

function ISBaseIcon:isCenterView(_bonusAngle)
	local objAngle1 = math.deg(self.character:getForwardDirection():getDirection() + math.pi / 2); --rotate 90 degrees CW
	if objAngle1 < 0 then objAngle1 = math.abs(360 + objAngle1); end; --fix to 0-360
	local objAngle2 = self:getAngle2D(self.xCoord, self.yCoord, self.character:getX(), self.character:getY()); --find angle to item
	objAngle2 = math.abs(objAngle2 - 360); --invert direction
	local offset2D = self:getAngleOffset2D(objAngle1, objAngle2); --find difference between angles
	if offset2D >= -(self.viewAngle + _bonusAngle + self.expandView) and offset2D <= (self.viewAngle + _bonusAngle + self.expandView) then
		return true;
	end;
	return false;
end
-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:getDistance3D(_x1, _y1, _z1, _x2, _y2, _z2)
	local absX = math.abs(_x2 - _x1);
	local absY = math.abs(_y2 - _y1);
	local absZ = math.abs(_z2 - _z1) * self.zSize;
	return math.sqrt(absX^2 + absY^2) + absZ;
end

function ISBaseIcon:isInRangeOfPlayer(_range)
	return self:getDistance3D(self.character:getX(), self.character:getY(), self.character:getZ(), self.xCoord, self.yCoord, self.zCoord) <= _range;
end
-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:getIsSeen()			 			return self.isSeen; 				end;
function ISBaseIcon:getIsSeenThisUpdate() 			return self.isSeenThisUpdate; 		end;
function ISBaseIcon:setIsSeen(_isSeen)				self.isSeen = _isSeen; 				end;
function ISBaseIcon:setIsSeenThisUpdate(_isSeen) 	self.isSeenThisUpdate = _isSeen; 	end;

function ISBaseIcon:getCanSeeThisUpdate()
	if UIManager and UIManager.getSpeedControls and UIManager.getSpeedControls():getCurrentGameSpeed() ~= 0 then
		if (self.square and self.character) then
			if self.square:getZ() == self.character:getZ() then

				local currentSquare = self.character:getCurrentSquare();
				local canSeeSquare = self.square:isCanSee(self.player);
				local isOnSquare = self.distanceToPlayer <= self.onSquareDistance;
				local isBlockedTo = self.square:isBlockedTo(currentSquare);
				local lightPenalty = 1 - forageSystem.getLightLevelPenalty(self.character, self.square);
				local squareDarkMulti = self.square:getDarkMulti(self.player);

				if ISSearchManager.showDebug then
					self.visionData = {
						canSeeSquare = canSeeSquare and "true" or "false",
						isOnSquare = isOnSquare and "true" or "false",
						isBlockedTo = isBlockedTo and "true" or "false",
						lightPenalty = lightPenalty,
						squareDarkMulti = squareDarkMulti,
					};
				end;

				if isBlockedTo then return false; end;
				if isOnSquare then return true; end;

				if lightPenalty >= (forageSystem.lightPenaltyCutoff / 100) then
					if squareDarkMulti <= 2.0 then
						return false;
					end;
				end;

				if self.perkLevel >= 5 then
					--also check adjacent squares
					for _, square in pairs(self.adjacentSquares) do
						canSeeSquare = square:isCanSee(self.player);
						if canSeeSquare and not square:isBlockedTo(currentSquare) then break; end;
					end;
				end;

				return canSeeSquare;
			end;
		end;
	end;
	return false;
end

function ISBaseIcon:doVisionCheck()
	local character, itemDef = self.character, self.itemDef;
	---
	self.perkLevel = (itemDef and forageSystem.getPerkLevel(character, itemDef))
			      or self.character:getPerkLevel(Perks.PlantScavenging);
	---
	local maxVisionRadius = self.maxVisionRadius;
	local viewDistance = maxVisionRadius;
	---
	local itemSize = self.itemSize;
	if itemDef and itemDef.itemSizeModifier then
		if itemDef.isItemOverrideSize then
			itemSize = itemDef.itemSizeModifier + 1;  --the +1 here ensures the value is above 1 and consistent
		else
			itemSize = self.itemSize + itemDef.itemSizeModifier + 1; --the +1 ensures the value is above 1 and consistent
		end
	end;
	---
	viewDistance = viewDistance + (self.perkLevel * forageSystem.levelBonus);
	viewDistance = viewDistance + forageSystem.getTraitBonus(character);
	viewDistance = viewDistance + forageSystem.getProfessionBonus(character);
	---
	local modifiers = {
		weather 	= forageSystem.getWeatherBonus(itemDef),
		month 		= forageSystem.getMonthBonus(itemDef),
		panic 		= forageSystem.getPanicPenalty(character),
		body		= forageSystem.getBodyPenalty(character),
		exhaustion	= forageSystem.getExhaustionPenalty(character),
		clothing	= forageSystem.getClothingPenalty(character),
		difficulty 	= (self.perkLevel + 1) / 10,
		size 		= (itemSize / forageSystem.sizeMultiplier),
		weather 	= forageSystem.getWeatherPenalty(character, self.square),
		movement 	= forageSystem.getMovementPenalty(character),
		hunger   	= forageSystem.getHungerBonus(character, itemDef),
	};
	local aiming = math.max(forageSystem.getAimBonus(character) * self.manager.aimMulti, 1);
	local sneaking = math.max(forageSystem.getSneakBonus(character) * self.manager.sneakMulti, 1);
	--only the highest modifier applies for aim or sneak
	modifiers.visionBonus = math.max(aiming, sneaking);
	for _, modifier in pairs(modifiers) do viewDistance = viewDistance * modifier; end;
	local lightLevelPenalty = forageSystem.getLightLevelPenalty(character, self.square);
	viewDistance = viewDistance * lightLevelPenalty;
	viewDistance = math.max(math.min(viewDistance, maxVisionRadius), self.onSquareDistance);
	if itemDef and self.checkIsIdentified then
		self.identifyDistance = math.max((0.25 + ((self.perkLevel * 0.05)) * viewDistance), self.onSquareDistance);
		self:checkIsIdentified();
	end;
	return viewDistance;
end
-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:remove() self.manager:removeIcon(self); self:reset(); end;

function ISBaseIcon:reset()
	self.isNoticed = false;
	self:setIsSeen(false);
	self:setIsSeenThisUpdate(false);
	self:setAlpha(0);
	self.alphaTarget = 0;
	self:resetBounce();
	self:removeWorldMarker();
	self:removeIsoMarker();
end
-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:updateBounce()
	if self.bounce then
		self.bounceStep = self.bounceStep + self.bounceSpeed;
		if self.bounceStep >= self.bounceMax then
			self.bounce = false;
		end;
	end;
end
-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:getScreenDelta() return -getPlayerScreenLeft(self.player), -getPlayerScreenTop(self.player); end;

function ISBaseIcon:updateZoom() self.zoom = getCore():getZoom(self.player); end;

function ISBaseIcon:updateAlpha()
	self.alphaTarget = math.max(math.min(1 - self.distanceToPlayer / self.maxVisionRadius, 1), 0);
	---
	if self:getAlpha() ~= self.alphaTarget then
		self:setAlpha(self:getAlpha() + (self.alphaTarget - self:getAlpha()) / 60);
	end;
	if self.pinAlpha ~= self.alphaTarget then
		self.pinAlpha = (self.pinAlpha + (self.alphaTarget - self.pinAlpha ) / 60);
	end;
	---
	if self:getAlpha() <= 0 then self:reset(); end;
	if self.isoMarker then self.isoMarker:setAlpha(self:getAlpha()); end;
end

function ISBaseIcon:updatePinIconPosition()
	local dx, dy = self:getScreenDelta();
	self:setX(isoToScreenX(self.player, self.xCoord, self.yCoord, self.zCoord) + dx + (-self.textureCenter / self.zoom)); --for item texture
	self:setY(isoToScreenY(self.player, self.xCoord, self.yCoord, self.zCoord) + dy);
	self:setY(self.y - (30 / self.zoom) - (self.height) + (math.sin(self.bounceStep) * self.bounceHeight));
end

function ISBaseIcon:updatePinIconSize()
	self:setWidth(self.baseWidth / self.zoom);
	self:setHeight(self.baseHeight / self.zoom);
end
-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:spotIcon()
	if not self:getIsSeen() then
		self.spotTimer = self.spotTimerMax;
		self:updateAlpha();
		self.pinAlpha = 1;
		self:setIsSeen(true);
		self:setIsSeenThisUpdate(true);
		if not self.manager.seenIcons[self.iconID] then
			self.manager.seenIcons[self.iconID] = self.iconID;
			self.manager.lastFoundX = self.xCoord;
			self.manager.lastFoundY = self.yCoord;
			self.manager:resetForceFindTracker();
		end;
		if not self.manager.xpIcons[self.iconID] then
			if self.itemDef then
				self.manager.xpIcons[self.iconID] = self.iconID;
				forageSystem.giveItemXP(self.character, self.itemDef, math.max(self.itemDef.xp / 3, 1));
			end;
		end;
		self:addIsoMarker();
	end;
end
-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:updateTimestamp()
	self.currentTimestamp = getTimestampMs();
	if self.lastTimestamp <= 0 then self.lastTimestamp = self.currentTimestamp; end;
	self.timeDelta = self.currentTimestamp - self.lastTimestamp;
	self.lastTimestamp = self.currentTimestamp;
end
-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:updateSpotTimer()
	if self.distanceToPlayer <= self.viewDistance and self.manager.isSearchMode and not self:getIsSeen() then
		if UIManager and UIManager.getSpeedControls and UIManager.getSpeedControls():getCurrentGameSpeed() ~= 0 then
			self.manager.isSpotting = self;
			if self.distanceToPlayer <= self.onSquareDistance or self:isCenterView((self.perkLevel or 0) * 6) then
				self.isNoticed = true;
				self.spotTimer = math.max(self.spotTimer + self.timeDelta, 0);
				self.stareVal = math.max(self.spotTimer / self.spotTimerMax, self.manager:getAlpha());
				self.manager:setAlpha(math.min(self.stareVal, 1));
				self.manager:resetForceFindTracker();
			else
				self.expandView = self.expandView + self.expandViewStep; --anti frustration, the view will expand gradually until the item is spotted if being actively hunted down by player.
				self.manager:setAlpha(math.max(self.manager.spotAlpha, self.manager:getAlpha()));
			end;
			self.lastSeenHours = forageSystem.getWorldAge();
		end;
	else
		self.expandView = 0;
		self.spotTimer = math.max(self.spotTimer - self.timeDelta, 0);
	end;
end
-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:getIsSearchModeActive()
	if (self.manager.isSearchMode) and (not self.character:getVehicle()) then
		return true;
	end;
	self:reset();
	return false;
end

function ISBaseIcon:updateViewDistance()
	self.viewDistance = self:doVisionCheck();
end

function ISBaseIcon:updateDistanceToPlayer()
	self.distanceToPlayer = self:getDistance3D(self.character:getX(), self.character:getY(), self.character:getZ(), self.xCoord, self.yCoord, self.zCoord);
end

function ISBaseIcon:removeIsoMarker()
	if self.isoMarker then self.isoMarker:remove(); self.isoMarker = nil; end;
end

function ISBaseIcon:addIsoMarker()
	self:updateManagerMarkers();
	if self.isBeingRemoved then return; end;
	if self.iconClass and self.iconClass == "forageIcon" and (not self.isoMarker) then
		if self.altWorldTexture then
			local altTextures = {};
			local altName;
			for _, texture in ipairs(self.altWorldTexture) do
				altName = texture and texture:getName() or false;
				if altName then table.insert(altTextures, altName); end;
			end;
			if #altTextures > 0 then
				if self.itemDef.doIsoMarkerSprite then
					self.isoMarker = getIsoMarkers():addIsoMarker(self.itemDef.doIsoMarkerSprite, self.square, 1, 1, 1, false, self.itemDef.doIsoMarkerObject );
				else
					self.isoMarker = getIsoMarkers():addIsoMarker(altTextures, { }, self.square, 1, 1, 1, false, self.itemDef.doIsoMarkerObject );
				end;
			else
				self.isoMarker = getIsoMarkers():addIsoMarker({self.itemTexture:getName()}, {}, self.square, 1, 1, 1, false, false);
			end;
		else
			self.isoMarker = getIsoMarkers():addIsoMarker({self.itemTexture:getName()}, {}, self.square, 1, 1, 1, false, false);
		end;
	end;
end

function ISBaseIcon:addWorldMarker()
	self:updateManagerMarkers();
	if self.isBeingRemoved then return; end;
	self.worldMarker = getWorldMarkers():addPlayerHomingPoint(self.character, self.xCoord, self.yCoord, 0.8, 0.8, 0.8, 1);
end

function ISBaseIcon:removeWorldMarker()
	if self.worldMarker then self.worldMarker:remove(); self.worldMarker = nil; end;
end

function ISBaseIcon:updateWorldMarker()
	if
		((self:getIsSeen() and self:getIsSeenThisUpdate() and self.distanceToPlayer <= self.manager.radius - 1)
		or (self.manager.isSearchMode and self:getSpotTimer() >= self.spotTimerMax / 2))
		and (UIManager and UIManager.getSpeedControls and UIManager.getSpeedControls():getCurrentGameSpeed() ~= 0)
	then
		if self:isNearby() and (not self.worldMarker) then
			if not (self.iconClass and (self.iconClass == "stashObject" or self.iconClass == "worldObject")) then
				self:addWorldMarker();
			end;
		end;
	else
		self:removeWorldMarker();
	end;
end
-------------------------------------------------
-------------------------------------------------
--function ISBaseIcon:attemptToMoveVertically()
--	if self.square then
--
--	end;
--	self.canMoveVertical = false;
--end;
-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:getSpotTimer()			return self.spotTimer; 								end;
function ISBaseIcon:setSpotTimer(_time)		self.spotTimer = _time; 							end;
function ISBaseIcon:isNearby()				return self.distanceToPlayer <= self.maxVisionRadius; 	end;

function ISBaseIcon:checkIsSpotted()
	if self:getIsSeen() then return; end;
	if self:getSpotTimer() >= self.spotTimerMax then self:spotIcon(); end;
end

function ISBaseIcon:updateIsForageable()
	self.isForageable = forageSystem.isForageable(self.character, self.itemDef, self.zoneDef);
	return self.isForageable;
end

function ISBaseIcon:updateManagerMarkers()
	local manager = self.manager;
	local managedMarkers = self.managedMarkers;
	for markerType, managedType in pairs(managedMarkers) do
		if self[markerType] then
			if manager[managedType][self.iconID] then
				if manager[managedType][self.iconID] ~= self[markerType] then
					manager[managedType][self.iconID]:remove();
					manager[managedType][self.iconID] = self[markerType];
				end;
			else
				manager[managedType][self.iconID] = self[markerType];
			end;
		else
			if manager[managedType][self.iconID] then
				manager[managedType][self.iconID]:remove();
				manager[managedType][self.iconID] = nil;
			end;
		end;
	end;
end

function ISBaseIcon:update()
	self:updateManagerMarkers();
	self:setIsSeenThisUpdate(false);
	if (not self:getGridSquare()) or (not self:getIsSearchModeActive()) then
		self:removeIsoMarker();
		self:removeWorldMarker();
		return;
	end;
	----
	self:updateTimestamp();
	self:updateDistanceToPlayer();
	self:updateViewDistance();
	----
	if not self:isValid() then
		self:removeWorldMarker();
		self:remove();
		return;
	end;
	----
	if self:isNearby() then
		--if self.canMoveVertical then self:attemptToMoveVertically(); end;
		if not self.isValidSquare then self.manager:doChangePosition(self); return; end;
		if self.manager.seenIcons[self.iconID] then	self:spotIcon(); end;
		self:setIsSeenThisUpdate(self:getCanSeeThisUpdate());
		if self:getIsSeenThisUpdate() then
			if not (self.character:isRunning() or self.character:isSprinting()) then
				if self:updateIsForageable() then
					if self.isValidSquare then
						self:updateSpotTimer();
						self:checkIsSpotted();
					else
						self.manager:doChangePosition(self);
					end;
				end;
			end;
		else
			self:setSpotTimer(0);
		end;
	else
		self:removeWorldMarker();
	end;
	---
	self:updateWorldMarker();
	self:updateMover();
end

-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:initItem()
	if self.icon then
		if self.icon.itemObj then
			self.itemObj = self.icon.itemObj;
			self.itemTexture = self.itemObj:getTexture();
		elseif self.icon.itemType then
			self.itemObj = InventoryItemFactory.CreateItem(self.icon.itemType);
			self.itemTexture = self.itemObj:getTexture();
		else
			print("[ISBaseIcon][initItem] no item or type for "..self.icon.id);
		end;
	end;
end
-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:findPinOffset()
	if self.altWorldTexture then
		local tallestTexture = 0;
		for _, texture in ipairs(self.altWorldTexture) do
			if tallestTexture < texture:getHeight() then tallestTexture = texture:getHeight(); end;
		end;
		self.pinOffset = -(tallestTexture);
	end;
end

function ISBaseIcon:findTextureCenter()
	if self.altWorldTexture then
		local widestTexture = 0;
		for _, texture in ipairs(self.altWorldTexture) do
			if widestTexture < texture:getWidth() then widestTexture = texture:getWidth(); end;
		end;
		self.textureCenter = widestTexture / 2;
	elseif self.itemTexture then
		self.textureCenter = self.itemTexture:getWidth() / 2;
	else
		self.textureCenter = 0;
	end;
end
-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:resetBounce()
	self.bounce = true;
	self.bounceStep = 1 * math.pi;
	self.bounceMax = 2 * math.pi;
	self.bounceHeight = 24;
	self.bounceSpeed = 0.1;
end

function ISBaseIcon:initGridSquare()
	local cell = getCell();
	if cell then
		local square = (getCell():getGridSquare(self.icon.x, self.icon.y, self.icon.z));
		if square then
			self.square = square;
			self.adjacentSquares = {
				north   = square:getN(),
				south   = square:getS(),
				east	= square:getE(),
				west	= square:getW(),
			};
			return self.square;
		end;
	end;
	return false;
end

function ISBaseIcon:initAltTexture()
	if type(self.altWorldTexture) == 'string' then
		self.altWorldTexture = {getTexture(texture)};
	elseif type(self.altWorldTexture) == 'table' then
		if type(self.altWorldTexture[1]) == 'table' then
			if #self.altWorldTexture > 1 then
				self.altWorldTexture = self.altWorldTexture[ZombRand(#self.altWorldTexture) + 1];
			else
				self.altWorldTexture = self.altWorldTexture[1];
			end;
		end;
		for i, texture in ipairs(self.altWorldTexture) do
			if type(texture) == 'string' then
				self.altWorldTexture[i] = getTexture(texture);
			end;
		end;
	else
		self.altWorldTexture = {self.altWorldTexture};
	end;
end

function ISBaseIcon:initSpotTimer()
	self.spotTimerMax = 900 + ((10 - self.perkLevel + 1) * 200);
end

function ISBaseIcon:finishLoadingIn()
	if self.hasFullyLoaded then return; end;
	--self:doVisionCheck();
	self.perkLevel = (self.itemDef and forageSystem.getPerkLevel(self.character, self.itemDef)) or self.character:getPerkLevel(Perks.PlantScavenging);
	self:initSpotTimer();
	self:resetBounce();
	self.hasFullyLoaded = true;
end

function ISBaseIcon:initialise()
	ISPanel.initialise(self);
	self:setAlpha(0);
	self:initGridSquare();
	self:initItem();
	self:setVisible(true);
	self:setFollowGameWorld(true);
end
-------------------------------------------------
-------------------------------------------------
function ISBaseIcon:new(_manager, _icon)
	local o = {};
	o = ISPanel:new(0, 0, 30, 45);
	setmetatable(o, self)
	self.__index = self;
	o.width = 30;
	o.height = 45;
	o.baseWidth = o.width;
	o.baseHeight = o.height;
	o.manager = _manager;
	o.icon = _icon;
	o.iconID = _icon.id;
	o.xCoord = _icon.x or 0;
	o.yCoord = _icon.y or 0;
	o.zCoord = _icon.z or 0;
	o.zSize = 10;
	o.texture = pinIconBlank;
	o.textureColor = {r = 1, g = 1, b = 1, a = 0};
	o.alphaTarget = 0;
	o.pinAlpha = 1;
	o.character = _manager.character;
	o.player = _manager.character:getPlayerNum();
	o.square = nil;
	o.adjacentSquares = {};
	o.spotTimer = 0;
	o.spotTimerMax = 10000;
	o.lastTimestamp = 0;
	o.currentTimestamp = 0;
	o.lastSeenHours = 0;
	o.onSquareDistance = forageSystem.onSquareDistance;
	o.maxVisionRadius = forageSystem.maxVisionRadius;
	o.loadedArea = 20;
	o.distanceToPlayer = 0;
	o.viewDistance = 0;
	o.isNoticed = false;
	o.isSeen = false;
	o.isSeenThisUpdate = false;
	o.keepOnScreen = false;
	o.posChanges = 0;
	o.itemRotation = ZombRand(360);
	o.onMouseDoubleClick = ISBaseIcon.doPickup;
	o.viewAngle = 30;
	o.expandView = 0;
	o.expandViewStep = 0.5;
	o.textureCenter = 0;
	o.pinOffset = 0;
	o.itemType = _icon.itemType;
	o.itemSize = o.itemType and ScriptManager.instance:FindItem(o.itemType):getActualWeight() or 0;
	o.isMover = false;
	o.moveState = "idle";
	o.moveTargetX = o.xCoord;
	o.moveTargetY = o.yCoord;
	o.identified = true;
	o.isOnSurface = false;
	o.visionData = {};
	o.canMoveVertical = false;
	o.hasFullyLoaded = false;
	o.isBeingRemoved = false
	o.managedMarkers = {
		isoMarker = "isoMarkers",
		worldMarker = "worldMarkers",
	};
	o:initialise();
	return o;
end
