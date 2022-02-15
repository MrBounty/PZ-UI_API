-------------------------------------------------
-------------------------------------------------
--
-- ISSearchManager
--
-- eris
--
-------------------------------------------------
-------------------------------------------------
require "Foraging/forageSystem";
require "ISUI/ISPanel";
-------------------------------------------------
-------------------------------------------------
local eyeTex = {
	eyeconOn = getTexture("media/textures/Foraging/eyeconOn_Shade.png"),
	eyeconOff = getTexture("media/textures/Foraging/eyeconOff_Shade.png"),
};
-------------------------------------------------
-------------------------------------------------
local function iterList(_list)
	local list = _list;
	local size = list:size() - 1;
	local i = -1;
	return function()
		i = i + 1;
		if i <= size and not list:isEmpty() then
			return list:get(i), i;
		end;
	end;
end

local function isInRect(_x, _y, _x1, _x2, _y1, _y2)
	return (_x >= _x1 and _x <= _x2 and _y >= _y1 and _y <= _y2) or false;
end

local getTimestampMs = getTimestampMs;
-------------------------------------------------
-------------------------------------------------
LuaEventManager.AddEvent("onToggleSearchMode");
LuaEventManager.AddEvent("onEnableSearchMode");
LuaEventManager.AddEvent("onDisableSearchMode");
-------------------------------------------------
-------------------------------------------------
---@class ISSearchManager : ISPanel
ISSearchManager         			= ISPanel:derive("ISSearchManager");
ISSearchManager.players 			= {};
ISSearchManager.activeManagers 		= {};
ISSearchManager.showDebug 			= false;
ISSearchManager.showDebugExtended	= false;
ISSearchManager.showDebugVision		= false;
-------------------------------------------------
-------------------------------------------------
function ISSearchManager.getManager(_character)
	if not ISSearchManager.players[_character] then
		ISSearchManager.setManager(_character, ISSearchManager:new(_character));
	end;
	return ISSearchManager.players[_character];
end;

function ISSearchManager.setManager(_character, _manager)
	if ISSearchManager.players[_character] then
		ISSearchManager.destroyUI(_character);
	end;
	ISSearchManager.players[_character] = _manager;
end;
-------------------------------------------------
-------------------------------------------------
function ISSearchManager:onMouseDown()		 return false; 							end;
function ISSearchManager:onRightMouseUp() 	 return false; 							end;
function ISSearchManager:onRightMouseDown()  return false; 							end;
-------------------------------------------------
-------------------------------------------------
function ISSearchManager:getAlpha()			 return self.textureColor.a; 			end;
function ISSearchManager:getColor()			 return self.textureColor; 				end;
function ISSearchManager:setAlpha(_a)		 self.textureColor.a = _a;				end;
function ISSearchManager:setColor(_rgba) 	 self.textureColor = _rgba;				end;
function ISSearchManager:flashEye(_amount)
	self.textureColor.a = math.max(_amount or 1, self.textureColor.a);
end;
-------------------------------------------------
-------------------------------------------------
function ISSearchManager:getScreenDelta() return -getPlayerScreenLeft(self.player), -getPlayerScreenTop(self.player); end;
function ISSearchManager:updateZoom() self.zoom = getCore():getZoom(self.player); 	end;

function ISSearchManager:updatePosition()
	local dx, dy = self:getScreenDelta();
	self:setX(isoToScreenX(self.player, self.character:getX(), self.character:getY(), self.character:getZ()) - (self.textureWidth / 2) + dx);
	self:setY(isoToScreenY(self.player, self.character:getX(), self.character:getY(), self.character:getZ()) + dy - (140 / self.zoom) - self.textureHeight);
end
-------------------------------------------------
-------------------------------------------------
function ISSearchManager:prerender() end;
-------------------------------------------------
-------------------------------------------------
function ISSearchManager:renderEye()
	local tc = self.textureColor;
	self:drawTextureScaled(self.texture, 0, 0, self.textureWidth, self.textureHeight, tc.a, tc.r, tc.g, tc.b);
end

function ISSearchManager:render()
	self:updateZoom();
	self:updatePosition();
	self:renderEye();
	if ISSearchManager.showDebug then self:renderDebugInfo(); end;
end

function ISSearchManager:renderDebugInfo()
	for iconID, icon in pairs(self.stashIcons) do
		--renderIsoCircle(icon.xCoord, icon.yCoord, icon.zCoord, icon.viewDistance, 1, 1, 0, 1, 1);
		icon:drawTextCentre("stash", 0, 50, 1, 1, 1, 1, UIFont.NewMedium);
	end;
	for iconID, icon in pairs(self.worldObjectIcons) do
		--renderIsoCircle(icon.xCoord, icon.yCoord, icon.zCoord, icon.viewDistance, 1, 1, 0, 1, 1);
		icon:drawTextCentre(icon.itemType, 0, 50, 1, 1, 1, 1, UIFont.NewMedium);
		if ISSearchManager.showDebugVision then
			local y = 60;
			y = y + 15;
			icon:drawTextCentre("Vision Data", 0, y, 1, 1, 1, 1, UIFont.NewSmall);
			for k, v in pairs(icon.visionData) do
				y = y + 15;
				icon:drawTextCentre(tostring(k).." = "..tostring(v), 0, y, 1, 1, 1, 1, UIFont.NewSmall);
			end;
		end;
	end;
	for iconID in pairs(self.activeIcons) do
		local icon = self.forageIcons[iconID];
		--debug icons
		icon:renderWorldIcon();
		--Render3DItem(icon.itemObj, icon.square, icon.xCoord, icon.yCoord, 0, icon.itemRotation);
		icon:drawTextCentre(icon.itemType, 0, 50, 1, 1, 1, 1, UIFont.NewMedium);
		local y = 60;
		if ISSearchManager.showDebugExtended then
			if icon.itemDef then
				local itemDef = icon.itemDef;
				y = y + 15;
				icon:drawTextCentre("Perk level required: " .. itemDef.skill, 0, y, 1, 1, 1, 1, UIFont.NewSmall);
				y = y + 15;
				icon:drawTextCentre("Perks required: " .. tostring(unpack(itemDef.perks) or "none"), 0, y, 1, 1, 1, 1, UIFont.NewSmall);
				y = y + 15;
				icon:drawTextCentre("Traits required: " .. tostring(unpack(itemDef.traits) or "none"), 0, y, 1, 1, 1, 1, UIFont.NewSmall);
				y = y + 15;
				icon:drawTextCentre("Recipe required: " .. tostring(unpack(itemDef.recipes) or "none"), 0, y, 1, 1, 1, 1, UIFont.NewSmall);
				y = y + 15;
				icon:drawTextCentre("Itemtags required: " .. tostring(unpack(itemDef.itemTags) or "none"), 0, y, 1, 1, 1, 1, UIFont.NewSmall);
			end;
		end;
		if ISSearchManager.showDebugVision then
			--if not self.debugMarkers[iconID] then
			--	self.debugMarkers[iconID] = getWorldMarkers():addGridSquareMarker("circle_center", "circle_only_highlight", icon.square, 0.5, 0.5, 0.5, true, icon.viewDistance * 2);
			--	self.debugMarkers[iconID]:setAlpha(0.33);
			--else
			--	self.debugMarkers[iconID]:setPosAndSize(icon.xCoord, icon.yCoord, icon.zCoord, icon.viewDistance);
			--end;
			--renderIsoCircle(icon.xCoord, icon.yCoord, icon.zCoord, icon.viewDistance, 1, 1, 0, 0.5, 1);
			--renderIsoCircle(icon.xCoord, icon.yCoord, icon.zCoord, icon.identifyDistance, 0, 1, 0, 0.5, 1);
			y = y + 15;
			icon:drawTextCentre("Vision Data", 0, y, 1, 1, 1, 1, UIFont.NewSmall);
			for k, v in pairs(icon.visionData) do
				y = y + 15;
				icon:drawTextCentre(tostring(k).." = "..tostring(v), 0, y, 1, 1, 1, 1, UIFont.NewSmall);
			end;
			y = y + 15;
			icon:drawTextCentre("Distance 2D: " .. tostring(icon.distanceToPlayer or "none"), 0, y, 1, 1, 1, 1, UIFont.NewSmall);
			y = y + 15;
			icon:drawTextCentre("View Distance: " .. tostring(icon.viewDistance or "none"), 0, y, 1, 1, 1, 1, UIFont.NewSmall);
		else
			self:checkMarkers();
		end;
	end;
end
-------------------------------------------------
-------------------------------------------------
function ISSearchManager:isIconOnSquare(_square, _iconList)
	if not (_square and _iconList) then return; end;
	for iconID, icon in pairs(_iconList) do
		if icon.square == _square then return true; end;
	end;
	return false;
end
-------------------------------------------------
-------------------------------------------------
function ISSearchManager:addHaloNote(_text)
	table.insert(self.haloNotes, _text);
end

function ISSearchManager:displayHaloNotes()
	if #self.haloNotes > 0 then
		if self.character:getHaloTimerCount() == 0 then
			self.character:setHaloNote(table.remove(self.haloNotes), 220, 220, 220, 100);
			self.triggerHalo = false;
		end;
	end;
end
-------------------------------------------------
-------------------------------------------------
function ISSearchManager:removeAllZoneData()
	for zoneID, zoneData in pairs(self.activeZones) do
		for iconID in pairs(zoneData.forageIcons) do
			if self.forageIcons[iconID] then self:removeIcon(self.forageIcons[iconID]); end;
		end;
		self.activeZones[zoneID] = nil;
	end;
	self.currentZone = nil;
end

function ISSearchManager:removeZoneAndIcons(_zoneData)
	self.activeZones[_zoneData.id] = nil;
	for iconID in pairs(_zoneData.forageIcons) do
		if self.forageIcons[iconID] then
			self:removeIcon(self.forageIcons[iconID]);
		end;
	end;
end

function ISSearchManager:removeItem(_icon)
	forageSystem.takeItem(_icon.zoneData);
	forageClient.updateIcon(_icon.zoneData, _icon.iconID, nil);
	forageClient.updateZone(_icon.zoneData);
end
-------------------------------------------------
-------------------------------------------------
function ISSearchManager:addIcon(_id, _iconClass, _itemType, _itemObj, _x, _y, _z)
	local id = _id or getRandomUUID();
	--create icon class/category if it does not exist so it can be managed/removed
	if not self[_iconClass] then
		self[_iconClass] = {};
		self.iconCategories[_iconClass] = _iconClass;
	end;
	if not self[_iconClass][id] then
		local itemObj = _itemObj or InventoryItemFactory.CreateItem(_itemType or "Base.Plank");
		local itemType = _itemType or itemObj:getFullType();
		local icon = {
			id         = id,
			x          = _x,
			y          = _y,
			z		   = _z,
			itemObj    = itemObj,
			itemType   = itemType,
		};
		self[_iconClass][id] = ISBaseIcon:new(self, icon);
		self[_iconClass][id]:finishLoadingIn();
		self[_iconClass][id]:update();
		self[_iconClass][id]:addToUIManager();
		self[_iconClass][id]:findTextureCenter();
		self.iconCategories[_iconClass] = _iconClass;
		return self[_iconClass][id];
	end;
end

function ISSearchManager:removeAllIcons()
	for iconClass in pairs(self.iconCategories) do
		for iconID, icon in pairs(self[iconClass]) do
			icon:reset();
			icon:removeFromUIManager();
			self[iconClass][iconID] = nil;
		end;
	end;
end

function ISSearchManager:removeIcon(_icon)
	if not _icon and _icon.iconID then return; end;
	for iconClass in pairs(self.iconCategories) do
		self[iconClass][_icon.iconID] = nil;
	end;
	_icon:reset();
	_icon:removeFromUIManager();
	self.iconStack = {};
	self.worldIconStack = {};
end
-------------------------------------------------
-------------------------------------------------
function ISSearchManager:worldItemTest(_itemObj)
	local itemCategory = _itemObj:getCategory();
	local itemType = _itemObj:getFullType();
	local itemWeight = _itemObj:getActualWeight();
	local isContainer = itemCategory == "Container";
	local isClothing = itemCategory == "Clothing";
	local isLightweight = itemWeight <= 0.5;
	local isVeryLightweight = itemWeight <= 0.2;
	---
	local doIcon = false;
	if (not self.ignoredItemTypes[itemType]) and (not self.ignoredItemCategories[itemCategory]) then
		if isContainer then doIcon = true; end;
		if isVeryLightweight then doIcon = true; end;
		if isLightweight and not isClothing then doIcon = true; end;
	end;
	---
	return doIcon;
end

function ISSearchManager:createIconsForWorldItems()
	---- look for new icons
    local square;
	local plX, plY, plZ = self.character:getX(), self.character:getY(), self.character:getZ();
	local iconCount = 0;
	local newIconMax = 10000;  --limit new icons queued/processed each call
	local cell = getCell();
	if not cell then return; end
	local spriteName, spriteCategory, zD;
	local affinityTable = forageSystem.spriteAffinities;
	local catDef;
	---
	for x = -10, 10 do
		for y = -10, 10 do
			square = cell:getGridSquare(plX + x, plY + y, plZ);
			if square then
				for worldObject in iterList(square:getWorldObjects()) do
					local itemObj = worldObject:getItem();
					if itemObj and self:worldItemTest(itemObj) then
						local itemType = itemObj:getFullType();
						local iconID = tostring(square).."-"..itemType;
						if not (self.worldObjectIcons[iconID] and self.worldIconStack[iconID]) then
							local icon = {
								id       		= iconID,
								x        		= worldObject:getWorldPosX(),
								y        		= worldObject:getWorldPosY(),
								z			 	= worldObject:getWorldPosZ(),
								itemObj  		= itemObj,
								itemObjTable	= {},
								itemType 		= itemType,
							};
							icon.itemObjTable[itemObj] = itemObj;
							self.worldIconStack[iconID] = icon;
							iconCount = iconCount + 1;
							if iconCount >= newIconMax then return; end;
						else
							local icon = self.worldObjectIcons[iconID] or self.worldIconStack[iconID];
							if icon then
								icon.itemObjTable[itemObj] = itemObj;
								--we find center x/y by averaging the pile
								--then put at highest item Z location so there is no overlapping
								if icon.xCoord and icon.yCoord and icon.zCoord then
									--we're adding to an existing icon
									icon.xCoord = (icon.xCoord + worldObject:getWorldPosX()) / 2;
									icon.yCoord = (icon.yCoord + worldObject:getWorldPosY()) / 2;
									icon.zCoord = math.max(icon.zCoord, worldObject:getWorldPosZ());
								else
									--this is a new icon, use the table we've just created
									icon.x = (icon.x + worldObject:getWorldPosX()) / 2;
									icon.y = (icon.y + worldObject:getWorldPosY()) / 2;
									icon.z = math.max(icon.z, worldObject:getWorldPosZ());
								end;
							end;
						end;
					end;
				end;
				---
				local doSpriteCheck = false;
				local doContainerCheck = false;
				if not self.checkedSquares[square] then
					if (self.currentZone and self.currentZone.bounds) and (not self.movedIconsSquares[square]) then
						zD = self.currentZone.bounds;
						if isInRect(plX + x, plY + y, zD.x1, zD.x2, zD.y1, zD.y2) then
							doSpriteCheck = true;
						end;
					end;
					doContainerCheck = true;
				end;
				if doSpriteCheck or doContainerCheck then
					for object in iterList(square:getObjects()) do
						if doSpriteCheck then
							spriteName = object:getSprite() and object:getSprite():getName();
							if spriteName then
								spriteCategory = affinityTable[spriteName];
								catDef = spriteCategory and forageSystem.catDefs[spriteCategory] or false;
								if spriteCategory and catDef then
									if (not self:isIconOnSquare(square, self.activeIcons)) then
										doSpriteCheck = false;
										if catDef.chanceToMoveIcon >= ZombRand(100) then
											for iconID, icon in pairs(self.activeIcons) do
												if (not self.movedIcons[iconID]) and icon.catDef.name == spriteCategory then
													--make sure it's from this zone and not seen before
													if icon.icon.zoneid == self.currentZone.id and (not icon:getIsSeen()) and (not icon.isNoticed) then
														--move it to this appropriate square!
														icon.xCoord, icon.yCoord, icon.zCoord = square:getX(), square:getY(), square:getZ();
														icon.icon.x, icon.icon.y = icon.xCoord, icon.yCoord;
														icon:removeFromUIManager();
														icon:addToUIManager();
														self.activeIcons[iconID] = icon;
														if not icon.hasFullyLoaded then icon:finishLoadingIn(); end;
														icon:update();
														self.movedIcons[iconID] = iconID;
														self.movedIconsSquares[square] = true;
														break;
													end;
												end;
											end;
										end;
									end;
								end;
							end;
						else
							self.movedIconsSquares[square] = true;
							--break;
						end;
						---
						if doContainerCheck then
							if object:getContainer() then
								if self.stashTypes[object:getContainer():getType()] then
									if not self.stashIcons[object] then
										local itemObj = InventoryItemFactory.CreateItem("Base.Plank");
										local icon = {
											id         = object,
											x          = square:getX() + 0.5,
											y          = square:getY() + 0.5,
											z		   = square:getZ(),
											itemObj    = itemObj,
											itemType   = itemObj:getFullType(),
										};
										self.stashIcons[object] = ISWorldItemIcon:new(self, icon);
										self.stashIcons[object].iconClass = "stashObject";
										if self.stashIcons[object] then
											self.stashIcons[object]:finishLoadingIn();
											self.stashIcons[object]:addToUIManager();
											self.stashIcons[object]:findTextureCenter();
											self.stashIcons[object].itemTexture = getTexture("carpentry_01_16");
										end;
										iconCount = iconCount + 1;
										if iconCount >= newIconMax then return; end;
									end;
								end;
							end;
						end;
					end;
				end;
				self.checkedSquares[square] = true;
				self.movedIconsSquares[square] = true;
			end;
		end;
	end;
end
-------------------------------------------------
-------------------------------------------------
function ISSearchManager:getDistance2D(_x1, _y1, _x2, _y2)
	local absX = math.abs(_x2 - _x1);
	local absY = math.abs(_y2 - _y1);
	return math.sqrt(absX^2 + absY^2);
end

function ISSearchManager:checkShouldMoveIcon()
	if self.timeSinceFind < self.timeToMoveIcon then return; end;
	if self.distanceSinceFind < self.distanceMoveThreshold + self.distanceMoveExtra then return; end;
	if not self.currentZone then return; end;
	self:doMoveIconNearPlayer();
end

function ISSearchManager:updateTimestamp()
	self.currentTimestamp = getTimestampMs();
	if self.lastTimestamp <= 0 then self.lastTimestamp = self.currentTimestamp; end;
	self.timeDelta = self.currentTimestamp - self.lastTimestamp;
	self.lastTimestamp = self.currentTimestamp;
	if self.character:isPlayerMoving() then
		self.timeSinceFind = self.timeSinceFind + self.timeDelta;
	else
		self.timeSinceFind = self.timeSinceFind - self.timeDelta;
	end;
	self.timeSinceFind = math.max(math.min(self.timeSinceFind, self.timeToMoveIcon), 0);
end

function ISSearchManager:updateForceFindSystem()
	self:updateTimestamp();
	--slow down the icon moving for higher level, as it is less needed for skilled players
	self.timeToMoveIcon = self.timeToMoveIconMax - (self.perkLevel * self.reducedTimePerLevel) + self.timeToMoveIconExtra;
	---
	if self.timeSinceFind <= 0 then self:resetForceFindTracker(); return; end;
	--only move an icon if the player is in motion
	if self.character:isPlayerMoving() then
		local pX, pY = self.lastUpdateX, self.lastUpdateY;
		self.lastUpdateX, self.lastUpdateY = self.character:getX(), self.character:getY();
		--prevent player just walking in a circle to find stuff
		if  self:getDistance2D(pX, pY, self.lastFoundX, self.lastFoundY) > self.distanceMoveThreshold + self.distanceMoveExtra then
			--increase distance moved tracker value
			self.distanceSinceFind = self.distanceSinceFind + self:getDistance2D(pX, pY, self.lastUpdateX, self.lastUpdateY);
			self:checkShouldMoveIcon();
		end;
	end;
end

function ISSearchManager:resetForceFindTracker()
	self.timeDelta = 0;
	self.distanceSinceFind = 0;
	self.timeSinceFind = 0;
	--add some extra random distance and time
	self.distanceMoveExtra = ZombRand(10);
	self.timeToMoveIconExtra = ZombRand(15000);
end

function ISSearchManager:doMoveIconNearPlayer()
	if not (self.currentZone and self.currentZone.bounds) then self:resetForceFindTracker(); return; end;
	local iconList = {};
	local insert = table.insert;
	local remove = table.remove;
	for iconID, icon in pairs(self.forageIcons) do
		--make sure it's from this zone and not seen before
		if icon.icon.zoneid == self.currentZone.id and (not icon:getIsSeen()) and (not icon.isNoticed) then
			--make sure it's actually forageable
			if forageSystem.isForageable(self.character, icon.itemDef, self.currentZone) then
				insert(iconList, icon);
			end;
		end;
	end;
	if #iconList > 1 then
		local pickedIcon = iconList[ZombRand(#iconList) + 1];
		if pickedIcon then
			local squareList = {};
			local square;
			local plX, plY, plZ = self.character:getX(), self.character:getY(), self.character:getZ();
			--get zoneData boundaries to ensure icons are kept inside zone
			local zD = self.currentZone.bounds;
			local radius = math.min(self.radius, 3);
			for x = -radius, radius do
				for y = -radius, radius do
					--check is inside zone
					if isInRect(plX + x, plY + y, zD.x1, zD.x2, zD.y1, zD.y2) then
						square = getCell():getGridSquare(plX + x, plY + y, plZ);
						if square and square:isCanSee(self.player) then
							insert(squareList, square);
						end;
					end;
				end;
			end;
			local pickedSquare;
			if #squareList > 1 then
				pickedSquare = remove(squareList, ZombRand(#squareList) + 1);
				if pickedSquare and forageSystem.isValidSquare(pickedSquare, pickedIcon.itemDef, pickedIcon.catDef) then
					pickedIcon.isValidSquare = true;
					pickedIcon.xCoord, pickedIcon.yCoord, pickedIcon.zCoord = pickedSquare:getX(), pickedSquare:getY(), pickedSquare:getZ();
					pickedIcon.icon.x, pickedIcon.icon.y = pickedIcon.xCoord, pickedIcon.yCoord;
					pickedIcon:removeFromUIManager();
					pickedIcon:addToUIManager();
					self.activeIcons[pickedIcon.iconID] = pickedIcon;
					if not pickedIcon.hasFullyLoaded then pickedIcon:finishLoadingIn(); end;
					pickedIcon:update();
					--prevent player just walking in a circle to find stuff
					self.lastFoundX, self.lastFoundY = pickedIcon.xCoord, pickedIcon.yCoord;
					--flash eye slightly to indicate something around
					self:flashEye(0.13);
					pickedIcon.isNoticed = true;
				end;
			end;
		end;
	end;
	--even if we fail to move an icon it should reset the system here and try again later
	self:resetForceFindTracker();
end

function ISSearchManager:doChangePosition(_icon)
	while _icon and not _icon.isValidSquare do
		if forageSystem.isValidSquare(_icon.square, _icon.itemDef, _icon.catDef) then
			_icon.isValidSquare = true;
			break;
		else
			_icon.posChanges = _icon.posChanges + 1;
			if _icon.posChanges < 10 then
				_icon.xCoord, _icon.yCoord = forageSystem.getZoneRandomCoord(_icon.zoneData);
				_icon.icon.x, _icon.icon.y = _icon.xCoord, _icon.yCoord;
				if not _icon:getGridSquare() then break; end;
			else
				self:removeItem(_icon);
				self:removeIcon(_icon);
				break;
			end;
		end;
	end;
end

function ISSearchManager:doChangeZone(_zoneData)
	if not self.isSearchMode then return; end;
	if _zoneData then
		self.texture = eyeTex.eyeconOn;
		if self.currentZoneName ~= _zoneData.name then
			self.currentZoneName = _zoneData.name;
			self.updateTick = 0;
		end;
	else
		self.currentZoneName = nil;
	end;
end

function ISSearchManager:checkCellZones()
	local zD;
	local eW = self.activeIconRadius; --edge width
	local plX, plY = self.character:getX(), self.character:getY();
	for zoneID, zoneData in pairs(self.activeZones) do
		zD = zoneData.bounds;
		if not isInRect(plX, plY, zD.x1 - eW, zD.x2 + eW, zD.y1 - eW, zD.y2 + eW) then
			for iconID in pairs(zoneData.forageIcons) do
				if self.forageIcons[iconID] then self:removeIcon(self.forageIcons[iconID]); end;
			end;
			self.activeZones[zoneID] = nil;
		end;
	end;
end

function ISSearchManager:checkIcons()
	----wipe closeIcons table, used for radial menu pickup
	self.closeIcons = {};
	---
	local UI = UIManager.getUI();
	if not UI then return; end;
	---
	local doActivateIcons = false;
	local plX, plY = self.character:getX(), self.character:getY();
	local eW = self.activeIconRadius; --edge width
	local x1, x2 = plX - eW, plX + eW;
	local y1, y2 = plY - eW, plY + eW
	if self.loadedIconActivation then doActivateIcons = true; self.loadedIconActivation = false; end;
	if self.lastActivationZone == nil then doActivateIcons = true; end;
	if self.lastActivationSquare == nil then doActivateIcons = true; end;
	if self.currentZone == nil then doActivateIcons = true; end;
	if self.currentZone ~= self.lastActivationZone then doActivateIcons = true; end;
	if (not doActivateIcons) and self.lastActivationSquare ~= nil then
		local sqX, sqY = self.lastActivationSquare:getX(), self.lastActivationSquare:getY();
		if self:getDistance2D(plX, plY, sqX, sqY) >= (self.activeIconRadius / 3) then
			doActivateIcons = true;
		end;
	end;
	if doActivateIcons then
		self.lastActivationZone = self.currentZone;
		self.lastActivationSquare = self.character:getCurrentSquare();
		for iconID, icon in pairs(self.forageIcons) do
			if icon:isInRangeOfPlayer(3) and icon:getIsSeen() then self.closeIcons[iconID] = icon; end;
			if isInRect(icon.xCoord, icon.yCoord, x1, x2, y1, y2) then
				if not UI:contains(icon.javaObject) then
					icon:addToUIManager();
				end;
				self.activeIcons[iconID] = icon;
				if not icon.hasFullyLoaded then icon:finishLoadingIn(); end;
			else
				self.activeIcons[iconID] = nil;
				icon:removeFromUIManager();
			end;
		end;
	end;
	---
	for iconID, icon in pairs(self.worldObjectIcons) do
		if isInRect(icon.xCoord, icon.yCoord, x1, x2, y1, y2) then
			if not UI:contains(icon.javaObject) then
				icon:addToUIManager();
			end;
			if not icon.hasFullyLoaded then icon:finishLoadingIn(); end;
		else
			self:removeIcon(icon);
			self.seenIcons[iconID] = nil;
			icon:removeFromUIManager();
		end;
	end;
end

function ISSearchManager:createIconsForZone(_zoneData, _recreate)
	local zoneData = _zoneData;
	local forageIcons = forageSystem.createForageIcons(zoneData, _recreate);
	for _, icon in ipairs(forageIcons) do
		if not self.iconStack[icon] then
			self.iconStack[icon] = zoneData;
		end;
	end;
end

function ISSearchManager:checkCharacterZone()
	local zoneData = forageSystem.getForageZoneAt(self.character:getX(), self.character:getY(), true);
	if zoneData then
		self:createIconsForZone(zoneData);
		self.activeZones[zoneData.id] = zoneData;
	end;
	self:doChangeZone(zoneData);
	self.currentZone = zoneData;
end

function ISSearchManager:loadIcons()
	local i = 0;
	for icon, zoneData in pairs(self.iconStack) do
		if not self.forageIcons[icon.id] then
			i = i + 1;
			if i > self.iconLoadRate then break; end;
			self.forageIcons[icon.id] = ISForageIcon:new(self, icon, zoneData);
			self.forageIcons[icon.id]:update();
			self.loadedIconActivation = true;
		end;
		self.iconStack[icon] = nil;
	end;
	---
	local j = 0;
	for iconID, icon in pairs(self.worldIconStack) do
		if not self.worldObjectIcons[icon.id] then
			j = j + 1;
			if j > self.iconLoadRate then break; end;
			self.worldObjectIcons[iconID] = ISWorldItemIcon:new(self, icon);
			self.worldObjectIcons[iconID].iconClass = "worldObject";
			if self.worldObjectIcons[iconID] then self.worldObjectIcons[iconID]:findTextureCenter(); end;
			self.loadedIconActivation = true;
		end;
		self.worldIconStack[icon] = nil;
	end;
end
-------------------------------------------------
-------------------------------------------------
function ISSearchManager:getOverlayRadius()
	local character = self.character;
	self.perkLevel = forageSystem.getPerkLevel(character, {perks = {"PlantScavenging"}});
	self.square = self.character:getCurrentSquare();
	-- overlay minimum maximum radius settings
	local minRadius = self.minRadius;
	local maxRadius = self.maxRadius;
	-- begin calculations
	local overlayRadius = maxRadius;
	-- apply bonuses
	overlayRadius = overlayRadius + (self.perkLevel * forageSystem.levelBonus)
	overlayRadius = overlayRadius + forageSystem.getTraitBonus(character)
	overlayRadius = overlayRadius + forageSystem.getProfessionBonus(character);
	-- apply penalties
	local modifiers = {
		weather 	= forageSystem.getWeatherBonus(itemDef),
		month 		= forageSystem.getMonthBonus(itemDef),
		panic 		= forageSystem.getPanicPenalty(character),
		body		= forageSystem.getBodyPenalty(character),
		exhaustion	= forageSystem.getExhaustionPenalty(character),
		clothing	= forageSystem.getClothingPenalty(character),
		difficulty 	= (self.perkLevel + 1) / 10,
		size 		= ((self.itemSize + 1) / forageSystem.sizeMultiplier),
		weather 	= forageSystem.getWeatherPenalty(character, self.square),
		lighting 	= forageSystem.getLightLevelPenalty(character, self.square),
		movement 	= forageSystem.getMovementPenalty(character),
	};
	local aiming = math.max(forageSystem.getAimBonus(character) * self.aimMulti, 1);
	local sneaking = math.max(forageSystem.getSneakBonus(character) * self.sneakMulti, 1);
	--only the highest modifier applies for aim or sneak
	modifiers.visionBonus = math.max(aiming, sneaking);
	---
	for _, modifier in pairs(modifiers) do overlayRadius = overlayRadius * modifier; end;
	---
	if self.isSpotting then overlayRadius = math.max(overlayRadius, self.isSpotting.distanceToPlayer * forageSystem.aimMultiplier); end;
	return math.max(math.min(overlayRadius, maxRadius), minRadius);
end

function ISSearchManager:updateOverlay()
	local radius = self:getOverlayRadius();
	local sm = self.searchModeOverlay;
	local overlayEffectOption = getCore():getOptionSearchModeOverlayEffect();
	if overlayEffectOption == 1 or overlayEffectOption == 2 then
		sm:getBlur():setTargets(0.5, 0.5);
	else
		sm:getBlur():setTargets(0, 0);
	end
	if overlayEffectOption == 1 or overlayEffectOption == 3 then
		sm:getDesat():setTargets(0.5, 0.5);
	else
		sm:getDesat():setTargets(0.0, 0.0);
	end
	sm:getRadius():setTargets(1 + radius, 1 + radius);
	sm:getGradientWidth():setTargets(2, 2);
	local panicLevel = math.min(1 - forageSystem.getPanicPenalty(self.character), 0.5);
	local exhaustionLevel = math.min(1 - forageSystem.getExhaustionPenalty(self.character), 0.5);
	local darkLevel = 1 - forageSystem.getLightLevelPenalty(self.character, self.square);
	local overlayDark = math.max(panicLevel, exhaustionLevel, darkLevel);
	sm:getDarkness():setTargets(overlayDark, overlayDark);
	self.radius = radius;
end
-------------------------------------------------
-------------------------------------------------
function ISSearchManager:reset()
	self:resetForceFindTracker();
	self:removeAllZoneData();
	self:removeAllIcons();
	self.iconStack = {};
	self.worldIconStack = {};
	self.loadedIconActivation = true;
end

function ISSearchManager:checkShouldDisable()
	if ISSearchManager.showDebug then return false; end;
	local plStats = self.character:getStats();
	if plStats then
		--basic in-combat detection
		if (plStats:getNumVisibleZombies() >=3) and (plStats:getNumChasingZombies() >=3) then
			return true;
		end;
	end;
	if (self.character:isRunning() or self.character:isSprinting()) then
		return true;
	end;
	return false;
end
-------------------------------------------------
-------------------------------------------------
function ISSearchManager:deferredUpdate()
	self:checkCharacterZone();
	self:checkCellZones();
	self:checkIcons();
	self:updateForceFindSystem();
	----
	self:updateOverlay();
end

function ISSearchManager:checkMarkers()
	--for iconID, debugMarker in pairs(self.debugMarkers) do
	--	if not self.activeIcons[iconID] or not ISSearchManager.showDebugVision then
	--		debugMarker:remove();
	--		self.debugMarkers[iconID] = nil;
	--	end;
	--end;
	for iconID, isoMarker in pairs(self.isoMarkers) do
		if not self.forageIcons[iconID] then
			isoMarker:remove();
			self.isoMarkers[iconID] = nil;
		end;
	end;
	for iconID, worldMarker in pairs(self.worldMarkers) do
		if not self.forageIcons[iconID] then
			worldMarker:remove();
			self.worldMarkers[iconID] = nil;
		end;
	end;
end

function ISSearchManager:resetVisionBonuses()
	self.aimMulti              = 0;
	self.aimBonusTick          = 0;
	self.sneakMulti            = 0;
	self.sneakBonusTick        = 0;
end

function ISSearchManager:updateVisionBonuses()
	if self.character:isAiming() then
		if self.aimBonusTick < self.aimBonusTickMax then
			self.aimBonusTick = self.aimBonusTick + 1;
		end;
	else
		self.aimBonusTick = 0;
	end;
	if self.character:isSneaking() then
		if self.sneakBonusTick < self.sneakBonusTickMax then
			self.sneakBonusTick = self.sneakBonusTick + 1;
		end;
	else
		self.sneakBonusTick = 0;
	end;
	self.aimMulti = self.aimBonusTick / self.aimBonusTickMax;
	self.sneakMulti = self.sneakBonusTick / self.sneakBonusTickMax;
end

function ISSearchManager:update()
	self:displayHaloNotes();
	self:checkMarkers();
	----
	self.updateTick = self.updateTick + 1;
	self:setAlpha(math.max(self:getAlpha() - self.alphaStep, self.minAlpha));
	----
	if not self.isSearchMode then return; end;
	if UIManager and UIManager.getSpeedControls then
		if UIManager.getSpeedControls():getCurrentGameSpeed() == 0 then
			--if the game is paused it should reset this;
			self:resetForceFindTracker();
			return;
		end;
	end
	self:loadIcons();
	----
	if self.character:getVehicle() then self:toggleSearchMode(); end;
	if self:checkShouldDisable() then
		self.disableTick = self.disableTick + 1;
		if self.disableTick >= self.disableTickMax then
			self:toggleSearchMode();
		end;
	else
		if self.disableTick > 0 then self.disableTick = self.disableTick - 1; end;
	end;
	---
	self:updateVisionBonuses();
	---
	if self.updateTick % 10 == 0 then self:deferredUpdate(); end;
	if self.updateTick % 20 == 0 then self:createIconsForWorldItems(); end;
	if self.updateTick >= self.updateTickMax then self.updateTick = 0; end;
	----
	self.isSpotting = false;
end
-------------------------------------------------
-------------------------------------------------
function ISSearchManager:initialise()
	ISPanel.initialise(self);
	self:addToUIManager();
	self:setVisible(true);
	self:bringToTop();
	self:update();
	self:deferredUpdate();
	self:setFollowGameWorld(true);
	self:setRenderThisPlayerOnly(self.player);
end

function ISSearchManager:new(_character)
	local o = {};
	local w, h = eyeTex.eyeconOn:getWidth(), eyeTex.eyeconOn:getHeight();
	o = ISPanel:new(0, 0, 0, 0);
	setmetatable(o, self)
	self.__index      = self;
	o.baseWidth     		= w;
	o.baseHeight    		= h;
	o.textureWidth     		= w;
	o.textureHeight    		= h;
	o.width          		= 0;
	o.height         		= 0;

	o.texture         		= eyeTex.eyeconOn;
	o.textureColor    		= { r = 1, g = 1, b = 1, a = 0 };

	o.alphaStep				= 0.05;
	o.minAlpha				= 0;	 --minimum alpha value (debug)
	o.activeAlpha			= 0;	 --mode is active
	o.spotAlpha				= 0.33;  --something is in view not center view

	o.character       		= _character;
	o.player          		= _character:getPlayerNum();
	o.square				= _character:getCurrentSquare();
	o.cell            		= _character:getCell();
	o.perkLevel				= forageSystem.getPerkLevel(_character, {perks = {"PlantScavenging"}});

	o.isSearchMode    		= false;
	o.isSpotting      		= false;

	o.haloNotes       		= {};

	o.iconCategories		= {
		forageIcons			= "forageIcons",
		activeIcons 		= "activeIcons",
		closeIcons 			= "closeIcons",
		worldObjectIcons	= "worldObjectIcons",
		stashIcons 			= "stashIcons",
	};

	o.forageIcons			= {};
	o.activeIcons			= {};

	o.closeIcons			= {};

	o.worldObjectIcons		= {};
	o.stashIcons			= {};

	o.seenIcons				= {};
	o.xpIcons				= {};

	o.iconStack				= {};
	o.worldIconStack		= {};

	o.iconLoadRate			= 10;
	o.activeIconRadius		= 15;
	o.lastActivationSquare  = nil;
	o.lastActivationZone    = nil;
	o.loadedIconActivation  = true;

	o.activeZones			= {};
	o.currentZoneName		= nil;
	o.currentZone			= nil;

	o.updateTick			= 0;
	o.updateTickMax			= 200;

	o.disableTick			= 0;
	o.disableTickMax		= 15;

	o.currentTimestamp		= getTimestampMs();
	o.lastTimestamp			= 0;
	o.timeDelta				= 0;
	o.timeSinceFind			= 0;
	o.timeToMoveIcon		= 30000;
	o.timeToMoveIconMax		= 30000;
	o.timeToMoveIconExtra	= 1000;
	o.reducedTimePerLevel	= -1500;
	o.distanceSinceFind		= 0;
	o.distanceMoveThreshold	= 10;
	o.distanceMoveExtra		= 10;
	o.lastUpdateX			= _character:getX();
	o.lastUpdateY			= _character:getY();
	o.lastFoundX			= 0;
	o.lastFoundY			= 0;

	o.movedIcons			= {};
	o.movedIconsSquares		= {};
	o.checkedSquares		= {};

	o.searchModeOverlay		= getSearchMode():getSearchModeForPlayer(o.player);
	o.radius				= 0;
	o.minRadius             = 3;
	o.maxRadius             = 10;
	o.itemSize              = 1.0; --item size used for overlay radius calculations

	--containers matching these types will be added as a stash icon
	o.stashTypes			= {
		["ShotgunBox"]	= true,
		["GunBox"] 		= true,
		["ToolsBox"] 	= true,
	};

	--skip these item types when adding world pins, they are large enough to see in 3D.
	o.ignoredItemTypes = {
		["Base.UnusableWood"] = true,
		["Base.UnusableMetal"] = true,
	};

	o.ignoredItemCategories = {};

	o.zoom                  = 1;

	o.isoMarkers            = {};
	o.worldMarkers          = {};
	o.debugMarkers          = {};

	o.aimMulti              = 0;
	o.aimBonusTick          = 0;
	o.aimBonusTickMax       = 10;

	o.sneakMulti            = 0;
	o.sneakBonusTick        = 0;
	o.sneakBonusTickMax     = 10;

	o:initialise();
	return o;
end
-------------------------------------------------
-------------------------------------------------
function ISSearchManager.createUI(_player)
	local character = getSpecificPlayer(_player);
	if not ISSearchManager.players[character] then
		ISSearchManager.setManager(character, ISSearchManager:new(character));
		ISSearchManager.activeManagers[ISSearchManager.players[character]] = false;
	end;
	print("[ISSearchManager] created UI for player " .. _player);
end

function ISSearchManager.destroyUI(_character)
	local manager = ISSearchManager.players[_character];
	getSearchMode():setEnabled(_character:getPlayerNum(), false);
	if manager then
		manager.isSearchMode = false;
		ISSearchManager.activeManagers[manager] = false;
		manager:reset();
		manager:checkMarkers();
		manager:removeFromUIManager();
		ISSearchManager.players[_character] = nil;
	end;
	print("[ISSearchManager] removed UI for player " .. _character:getPlayerNum());
end

Events.OnCreatePlayer.Add(ISSearchManager.createUI);
Events.OnPlayerDeath.Add(ISSearchManager.destroyUI);
-------------------------------------------------
-------------------------------------------------
function ISSearchManager:toggleSearchMode()
	self.updateTick = 0;
	self.isSearchMode = not self.isSearchMode;
	ISSearchManager.activeManagers[self] = self.isSearchMode;
	if self.isSearchMode then
		self.minAlpha = self.activeAlpha;
		self:resetForceFindTracker();
		self:update();
		self:deferredUpdate();
		self:createIconsForWorldItems();
		self:checkMarkers();
		self:resetVisionBonuses();
		self.loadedIconActivation = true;
		triggerEvent("onEnableSearchMode", self.character, self.isSearchMode);
	else
		self.minAlpha = 0;
		self:reset();
		self:checkMarkers();
		self:resetVisionBonuses();
		triggerEvent("onDisableSearchMode", self.character, self.isSearchMode);
	end;
	triggerEvent("onToggleSearchMode", self.character, self.isSearchMode);
	getSearchMode():setEnabled(self.player, self.isSearchMode);
end

function ISSearchManager.handleKeyPressed(_keyPressed)
	if _keyPressed == getCore():getKey("Toggle Search Mode") then
		for _, manager in pairs(ISSearchManager.players) do
			manager:toggleSearchMode();
			manager:bringToTop();
		end;
	end;
end

function ISSearchManager.initBinds()
	table.insert(keyBinding, { value = "[Search Mode]" } );
	table.insert(keyBinding, { value = "Toggle Search Mode", key = 207 } );
	Events.OnKeyPressed.Add(ISSearchManager.handleKeyPressed);
end

Events.OnGameBoot.Add(ISSearchManager.initBinds);
-------------------------------------------------
-------------------------------------------------
function ISSearchManager.onUpdateIcon(_zoneData, _iconID, _icon)
	for _, manager in pairs(ISSearchManager.players) do
		if _icon then
			manager.forageIcons[_iconID] = _icon;
			manager.activeIcons[_iconID] = _icon;
		else
			manager.forageIcons[_iconID] = nil;
			manager.activeIcons[_iconID] = nil;
		end;
	end;
end

Events.onUpdateIcon.Add(ISSearchManager.onUpdateIcon);
-------------------------------------------------
-------------------------------------------------
function ISSearchManager.doWorldObjectContextMenuCheck(_player, _context, _worldObjects)
	if not _player and _context then return; end;
	local character = getSpecificPlayer(_player);
	local clickedX = screenToIsoX(_player, _context.x, _context.y, character:getZ());
	local clickedY = screenToIsoY(_player, _context.x, _context.y, character:getZ());
	local manager = ISSearchManager.getManager(character);
	local clickedSquareTable = {};
	for x = -1, 1 do
		for y = -1, 1 do
			local square = getCell():getGridSquare(clickedX + x, clickedY + y, character:getZ());
			if square then clickedSquareTable[square] = true; end;
		end;
	end
	for iconID, icon in pairs(manager.activeIcons) do
		if clickedSquareTable[icon.square] and icon:getIsSeen() then
			icon:doContextMenu(_context);
		end;
	end;
end

Events.OnFillWorldObjectContextMenu.Add(ISSearchManager.doWorldObjectContextMenuCheck);
-------------------------------------------------
-------------------------------------------------
