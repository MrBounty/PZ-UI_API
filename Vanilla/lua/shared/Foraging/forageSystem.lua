--[[---------------------------------------------
-------------------------------------------------
--
-- forageSystem
--
-- eris
--
-------------------------------------------------
--]]---------------------------------------------
if isServer() then return; end;  --temp block server from loading this module
-------------------------------------------------
-------------------------------------------------
require "Foraging/forageDefinitions";
-------------------------------------------------
-------------------------------------------------
local table = table;
-------------------------------------------------
-------------------------------------------------
--LuaEventManager.AddEvent("onUpdateIcon");
--LuaEventManager.AddEvent("preAddForageDefs");
--LuaEventManager.AddEvent("preAddZoneDefs");
--LuaEventManager.AddEvent("preAddCatDefs");
--LuaEventManager.AddEvent("preAddItemDefs");
--LuaEventManager.AddEvent("onAddForageDefs");
--LuaEventManager.AddEvent("onFillSearchIconContextMenu");
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
--[[---------------------------------------------
--
-- forageSystem
--
--]]---------------------------------------------

--[[--======== forageSystem ========--]]--

forageSystem = {
	isInitialised		 = false,
	-- definition tables
	itemDefs             = {}, -- item table
	catDefs    		     = {}, -- category definitions
	zoneDefs             = {}, -- zone definitions
	-- forage system loot tables
	lootTables			 = {},  -- the loot table - see generateLootTable for structure
	lootTableMonth   	 = nil, -- used to keep the loot table in line with the game date
	--
	maxIconsPerZone      = 2000,
	-- sandbox settings
	-- (Abundance Settings)
	NatureAbundance         = { -75, -50, 0, 50, 100 }, -- bonus percent density per zone
	OtherLoot       		= { -75, -50, 0, 50, 100 }, -- bonus percent density per zone
	-- extended chance settings (percents)
	monthBonus           = 20,  -- good forage months
	monthMalus           = -20, -- bad forage months
	-- vision settings (squares)
	levelBonus           = 0.5, -- bonus per perk level
	-- vision settings (radius multipliers)
	aimMultiplier		 = 1.33,
	sneakMultiplier		 = 1.10,
	sizeMultiplier		 = 5.0,
	onSquareDistance	 = 1.5, --if the player is closer than this, the item is always visible
	maxVisionRadius      = 10,
	-- bonus vision extended
	professionBonuses				= {	-- job bonus vision (squares)
		["ParkRanger"]		= 1,
		["Lumberjack"]		= 1,
		["Chef"]			= 1,
		["Farmer"]			= 1,
		["Fisherman"]		= 1,
		["Veteran"]			= 1,
		["Unemployed"]		= 1,
	},
	traitBonuses					= {	-- traits bonus vision (squares)
		["Lucky"]			= 1.0,
		["Hiker"]			= 1.0,
		["Formerscout"]		= 1.0,
		["Hunter"]			= 0.7,
		["EagleEyed"]		= 0.5,
		["Gardener"]		= 0.4,
		["Outdoorsman"]		= 0.4,
		["Cook"]			= 0.2,
		["NightVision"]		= 0.2,
		["Nutritionist"]	= 0.2,
		["Herbalist"]		= 0.2,
		["Unlucky"]			= -1.0,
		["Agoraphobic"]		= -1.5,
		["ShortSighted"]	= -2.0,
	},
	weatherEffectModifiers			= { -- job and traits weather effect reduction (percent)
		--professions
		["ParkRanger"]		= 33,
		["Lumberjack"]		= 33,
		["Farmer"]			= 33,
		["Fisherman"]		= 33,
		["Veteran"]			= 33,
		--traits
		["Unemployed"]		= 10,
		["Outdoorsman"]		= 13,
		["Herbalist"]		= 7,
		["Hunter"]			= 7,
		["Hiker"]			= 7,
		["Formerscout"]		= 13,
		["Gardener"]		= 13,
	},
	darknessEffectModifiers			= { -- job and traits darkness effect reduction (percent)
		--professions
		["ParkRanger"]		= 10,
		["Lumberjack"]		= 10,
		["Veteran"]			= 10,
		--traits
		["Outdoorsman"]		= 7,
		["Hunter"]			= 5,
		--note for those looking here for NightVision/cat eyes trait: it has a "built in" bonus multiplier as it increases ambient brightness
	},
	-- foraging penalty
	endurancePenalty     = 0.015,
	fatiguePenalty       = -0.001,

	-- vision settings (percents) (search radius)
	lightPenaltyMax      = 95, -- darkness effect on search radius (light level)
	weatherPenaltyMax    = 50, -- weather effect (rain, fog, snow)
	exhaustionPenaltyMax = 75, -- exhaustion effect (endurance, fatigue)
	panicPenaltyMax      = 95, -- panic effect (fear, panic, stress)
	bodyPenaltyMax       = 75, -- body effect (drunk, pain, sickness, food sickness)
	clothingPenaltyMax   = 95, -- clothing effect (helmet, glasses, blindfolds)
	hungerBonusMax       = 33, -- hunger effect (finding food when hungry)

	-- specific to lighting level - if penalty is higher than this, cannot spot anything (radius will still change)
	lightPenaltyCutoff	 = 60,

	clothingPenalties 	  = {  -- clothing vision penalties (percents)
		FullHat		= 75,
		MaskEyes	= 50,
		MaskFull	= 62,
		Eyes		= 10,
		LeftEye		= 5,
		RightEye	= 5,
	};

	isForageableFuncs = {
		"isItemExist",		"isValidMonth",		 "isItemInZone", 		"hasNeededPerks",
		"hasNeededTraits",	"hasNeededRecipes",	 "hasRequiredItems",
	},

	-- world object sprites (used by ISSearchManager scanner)
	spriteAffinities	 = {},

	-- global base XP reduction modifier for foraging (percent)
	globalXPModifier     = 40,
	-- diminishing base XP returns per level for foraging items below skill level (percent)
	levelXPModifier      = 5,
};

function forageSystem.integrityCheck()
	print("[forageSystem][integrityCheck] checking all zoneData.");
	local zonesChecked = 0;
	local zonesRemoved = 0;
	local iconsChecked = 0;
	local iconsRemoved = 0;
	local isInRect = isInRect;
	local metaGrid = getWorld():getMetaGrid();
	local wx1, wx2, wy1, wy2 = metaGrid:getMinX() * 300, metaGrid:getMaxX() * 300, metaGrid:getMinY() * 300, metaGrid:getMaxY() * 300;
	for zoneID, zoneData in pairs(forageData) do
		local removeZone = false;
		local x1, x2, y1, y2;
		if zoneData.bounds then
			x1, x2, y1, y2 = zoneData.bounds.x1, zoneData.bounds.x2, zoneData.bounds.y1, zoneData.bounds.y2;
		end;
		--todo: must check if zone actually exists/refers to existing zone (need method for this)
		if not forageSystem.zoneDefs[zoneData.name] then removeZone = true;
			print("[forageSystem][integrityCheck] zoneDef does not exist for " .. (zoneData.name or "[no type]"));
			print("[forageSystem][integrityCheck] removing zone " .. zoneID .. ".");
		elseif not (x1 and x2 and y1 and y2) then removeZone = true;
			print("[forageSystem][integrityCheck] zoneDef does not have valid bounds " .. (zoneData.name or "[no type]"));
			print("[forageSystem][integrityCheck] removing zone " .. zoneID .. ".");
		elseif not (isInRect(x1, y1, wx1, wx2, wy1, wy2) and isInRect(x2, y2, wx1, wx2, wy1, wy2)) then removeZone = true;
			print("[forageSystem][integrityCheck] zone is outside the world boundary " .. (zoneData.name or "[no type]"));
			print("[forageSystem][integrityCheck] removing zone " .. zoneID .. ".");
		end;
		if not removeZone then
			--ensure itemsLeft is within limits
			if zoneData.itemsLeft < 0 then zoneData.itemsLeft = 0; end;
			if zoneData.itemsLeft > zoneData.itemsTotal then zoneData.itemsLeft = zoneData.itemsTotal; end;
			---
			for iconID, iconData in pairs(zoneData.forageIcons) do
				local removeIcon = false;
				if not forageSystem.itemDefs[iconData.itemType] then removeIcon = true;
					print("[forageSystem][integrityCheck] itemDef does not exist for " .. (iconData.itemType or "[no type]"));
					print("[forageSystem][integrityCheck] removing icon " .. iconID .. ".");
				elseif not forageSystem.catDefs[iconData.catName] then removeIcon = true;
					print("[forageSystem][integrityCheck] catDef does not exist for " .. (iconData.catName or "[no category]"));
					print("[forageSystem][integrityCheck] removing icon " .. iconID .. ".");
				elseif not (iconData.x and iconData.y and iconData.z) then removeIcon = true;
					print("[forageSystem][integrityCheck] icon has invalid or missing x/y/z location " .. iconID .. ".");
					print("[forageSystem][integrityCheck] removing icon " .. iconID .. ".");
				elseif not isInRect(iconData.x, iconData.y, x1, x2,	y1, y2) then removeIcon = true;
					print("[forageSystem][integrityCheck] icon is out of zone bounds " .. iconID .. ".");
					print("[forageSystem][integrityCheck] removing icon " .. iconID .. ".");
				end;
				---
				if removeIcon then
					zoneData.forageIcons[iconID] = nil;
					iconsRemoved = iconsRemoved + 1;
				end;
				---
				iconsChecked = iconsChecked + 1;
			end;
		end;
		if removeZone then
			forageData[zoneID] = nil;
			zonesRemoved = zonesRemoved + 1;
		end;
		zonesChecked = zonesChecked + 1;
	end;
	print("[forageSystem][integrityCheck] checked integrity of " .. zonesChecked .. " zones.");
	if zonesRemoved > 0 then
		print("[forageSystem][integrityCheck] " .. zonesRemoved .. " zones failed integrity check and were removed.");
	else
		print("[forageSystem][integrityCheck] all zones passed testing.");
	end;
	print("[forageSystem][integrityCheck] checked integrity of " .. iconsChecked .. " icons.");
	if iconsRemoved > 0 then
		print("[forageSystem][integrityCheck] " .. iconsRemoved .. " icons failed integrity check and were removed.");
	else
		print("[forageSystem][integrityCheck] all icons passed testing.");
	end;
end

function forageSystem.init()
	--prevent multiple initialisations
	if forageSystem.isInitialised then return; end;
	---
	triggerEvent("preAddForageDefs", forageSystem);
	---
	triggerEvent("preAddZoneDefs", forageSystem);
	forageSystem.populateZoneDefs();
	---
	triggerEvent("preAddCatDefs", forageSystem);
	forageSystem.populateCatDefs();
	---
	triggerEvent("preAddItemDefs", forageSystem);
	forageSystem.populateScavengeDefs();
	forageSystem.populateItemDefs();
	---
	triggerEvent("onAddForageDefs", forageSystem);
	forageSystem.generateLootTable();
	---
	--initialise forageData table
	if isClient() then
		forageClient.init();
		forageClient.updateData();
	end;
	---
	--check integrity of forageData
	forageSystem.integrityCheck();
	---
	--forageSystem.statisticsDebug();
	--forageClient.clearData(); --debug clear forageData database
	--forageClient.syncForageData();
	---
	Events.EveryHours.Add(forageSystem.recreateIcons);
	Events.EveryDays.Add(forageSystem.recreateIcons);
	Events.EveryDays.Add(forageSystem.lootTableUpdate);
	Events.OnWeatherPeriodStart.Add(forageSystem.recreateIcons);
	Events.OnWeatherPeriodStage.Add(forageSystem.recreateIcons);
	Events.OnWeatherPeriodComplete.Add(forageSystem.recreateIcons);
	---
	forageSystem.isInitialised = true;
end

Events.OnLoadMapZones.Add(forageSystem.init);

--[[---------------------------------------------
--
-- zoneData
--
--]]---------------------------------------------

--[[--======== createZoneData ========--
	@param _forageZone
	@param _zoneDef
]]--

function forageSystem.createZoneData(_forageZone, _zoneDef)
	local zoneData = {};
	zoneData.id = _forageZone:getName();
	_forageZone:setName(zoneData.id);
	_forageZone:setOriginalName(zoneData.id);
	zoneData.name = _zoneDef.name;
	zoneData.x = _forageZone:getX();
	zoneData.y = _forageZone:getY();
	zoneData.size = _forageZone:getWidth() * _forageZone:getHeight();
	zoneData.bounds = {
		x1			= zoneData.x,
		y1			= zoneData.y,
		x2			= zoneData.x + _forageZone:getWidth(),
		y2			= zoneData.y + _forageZone:getHeight(),
	};
	zoneData.forageIcons = {};
	forageSystem.fillZone(zoneData);
	forageClient.addZone(zoneData);
	return zoneData;
end

function forageSystem.fillZone(_zoneData)
	local zoneDef = forageSystem.zoneDefs[_zoneData.name];
	local refillValue = zoneDef.densityMin;
	if zoneDef.densityMin ~= zoneDef.densityMax then
		refillValue = ZombRand(zoneDef.densityMin, zoneDef.densityMax * forageSystem.getRefillBonus(_zoneData));
	end;
	refillValue = math.floor((_zoneData.size / 1000) * refillValue);
	if refillValue == 0 and ZombRand(10) <= 3 then refillValue = 1; end;
	_zoneData.itemsLeft = refillValue;
	_zoneData.itemsTotal = _zoneData.itemsLeft;
	_zoneData.lastRefill = forageSystem.getWorldAge();
end

--[[--======== checkRefillZone ========--
	@param _zoneData
]]--

function forageSystem.checkRefillZone(_zoneData, _age)
	local worldAge = forageSystem.getWorldAge();
	if _zoneData.itemsTotal > _zoneData.itemsLeft then
		local zoneDef = forageSystem.zoneDefs[_zoneData.name];
		if zoneDef then
			local lastRefillDays = math.floor((worldAge - _zoneData.lastRefill) / 24);
			local refillAmount = _zoneData.itemsTotal * (lastRefillDays * (zoneDef.refillPercent / 100));
			if refillAmount >= 1 then
				_zoneData.itemsLeft = math.floor(_zoneData.itemsLeft + refillAmount);
				if _zoneData.itemsLeft > _zoneData.itemsTotal then _zoneData.itemsLeft = _zoneData.itemsTotal; end;
				_zoneData.lastRefill = worldAge
				forageSystem.updateZone(_zoneData);
			end;
		end;
	elseif _zoneData.itemsTotal <= _zoneData.itemsLeft then
		----if the zone is full or has too many items, re-roll the density and icons
		forageSystem.fillZone(_zoneData);
	end;
end

--[[--======== updateZone ========--]]--

function forageSystem.updateZone(_zoneData)
	_zoneData.lastAction = forageSystem.getWorldAge();
	forageClient.updateZone(_zoneData);
end

--[[--======== takeItem ========--
	@param _zoneData -zoneData
	@param _number - (optional)

	Returns number of forages remaining
]]--

function forageSystem.takeItem(_zoneData, _number)
	_zoneData.itemsLeft = math.max(_zoneData.itemsLeft - (_number or 1), 0);
	forageClient.updateZone(_zoneData);
	return _zoneData.itemsLeft;
end

function forageSystem.getWorldAge()
	return getGameTime():getWorldAgeHours();
end

--[[--======== statisticsDebug ========--
	Gathers and prints item spawn statistics for each loot table

	For debugging and balancing loot rates.
]]--

function forageSystem.statisticsDebug()
	local numberOfTests = 10000;
	local category, itemType, lootTable;
	local categorySpawned = {};
	local itemsSpawned = {};
	--local fileWriterObj = getFileWriter("statisticsDebug.log", true, false);
	for zoneName, zoneLoot in pairs(forageSystem.lootTables) do
		for month, monthLoot in pairs(zoneLoot) do
			for weatherType, weatherLoot in pairs(monthLoot) do
				if weatherType == "isNormal" then
					--fileWriterObj:write(zoneName.." - "..month.." - "..weatherType.."\r\n");
					print("[forageSystem][statisticsDebug] TESTING ZONE: " .. zoneName .. " - (" .. numberOfTests .. " ROLLS)");
					print("[forageSystem][statisticsDebug] MONTH : " .. month);
					print("[forageSystem][statisticsDebug] WEATHER TYPE: " .. weatherType);
					lootTable = weatherLoot;
					for i = 1, numberOfTests do
						itemType, category = forageSystem.pickRandomItemType(lootTable);
						categorySpawned[category] = (categorySpawned[category] or 0) + 1;
						itemsSpawned[itemType] = (itemsSpawned[itemType] or 0) + 1;
					end;
					print("[forageSystem][statisticsDebug] CATEGORIES PICKED");
					for category, amount in pairs(categorySpawned) do
						--fileWriterObj:write(category..","..amount.."\r\n");
						print(category.." = "..amount.." (".. tonumber(string.format("%.3f",  (amount/numberOfTests) * 100)) .."%)");
					end;
					print("[forageSystem][statisticsDebug] ITEMS PICKED");
					for itemType, amount in pairs(itemsSpawned) do
						--fileWriterObj:write(itemType..","..amount.."\r\n");
						--fileWriterObj:write(itemType..","..amount.."\r\n");
						print(itemType .. " = " .. amount);
					end;
					itemsSpawned = {};
					categorySpawned = {};
					print("[forageSystem][statisticsDebug] FINISHED TESTING: " .. zoneName);
					--fileWriterObj:write("\r\n");
				end;
			end;
		end;
	end;
	--fileWriterObj:close();
end

--[[--======== pickRandomItemType ========--
	@param _lootTable - the loot table to use

	 See forageSystem.generateLootTable for loot table structure
]]--

function forageSystem.pickRandomItemType(_lootTable)
	if #_lootTable > 0 then
		local rolledCategory = _lootTable[ZombRand(#_lootTable) + 1];
		if rolledCategory then
			local rolledItem = rolledCategory.items[ZombRand(#rolledCategory.items) + 1];
			if rolledItem then
				return rolledItem, rolledCategory.category;
			end;
		end;
	end;
	return nil, nil;
end

--[[--======== createForageIcons ========--
	@param _zoneData
	@param _recreate
	@param _count - number of items to create
]]--

function forageSystem.createForageIcons(_zoneData, _recreate, _count)
	forageSystem.lootTableUpdate();
	local maxIconsPerZone = forageSystem.maxIconsPerZone;
	local count = _count or maxIconsPerZone;
	local forageIcons = {};
	if _recreate then _zoneData.forageIcons = {}; end;
	for _, icon in pairs(_zoneData.forageIcons) do
		if forageSystem.itemDefs[icon.itemType] then
			table.insert(forageIcons, icon);
		else
			print("[forageSystem][createForageIcons] itemDef not defined for itemType, skipping " .. icon.itemType);
		end;
	end;
	if (not _zoneData) or (not _zoneData.name) or (not forageSystem.zoneDefs[_zoneData.name]) then
		print("[forageSystem][createForageIcons] zoneDef not defined for zoneData type, skipping " .. _zoneData.name or "undefined");
		return forageIcons;
	end;
	if (not forageSystem.lootTables[_zoneData.name]) then
		print("[forageSystem][createForageIcons] a loot table is not generated for zoneData type, skipping " .. _zoneData.name);
		return forageIcons;
	end;
	---
	local month = getGameTime():getMonth() + 1;
	local weatherType = forageSystem.getWeatherType() or "isNormal";
	local lootTable = forageSystem.lootTables[_zoneData.name][month][weatherType];
	local getRandomUUID = getRandomUUID;
	local itemsLeft = math.floor(_zoneData.itemsLeft);
	if itemsLeft > 0 and #forageIcons < itemsLeft then
		--create icons
		local i = 0;
		local zoneid    = _zoneData.id;
		local x1, x2    = _zoneData.bounds.x1, _zoneData.bounds.x2;
		local y1, y2    = _zoneData.bounds.y1, _zoneData.bounds.y2;
		local forageIcon, itemType, catName;
		repeat
			itemType, catName = forageSystem.pickRandomItemType(lootTable);
			if itemType and catName then
				forageIcon = {
					id       = getRandomUUID(),
					zoneid   = zoneid,
					x        = ZombRand(x1, x2) + 1,
					y        = ZombRand(y1, y2) + 1,
					z		 = 0,
					catName  = catName,
					itemType = itemType,
				};
				table.insert(forageIcons, forageIcon);
				_zoneData.forageIcons[forageIcon.id] = forageIcon;
			end;
			i = i + 1;
		until	i >= count
		or		i >= maxIconsPerZone
		or		#forageIcons >= maxIconsPerZone
		or		#forageIcons >= itemsLeft
	end;
	forageClient.updateZone(_zoneData);
	return forageIcons;
end

--[[--======== recreateIcons ========--

]]--

function forageSystem.recreateIcons()
	forageSystem.lootTableUpdate();
	--todo: use ISSearchManager.activeManagers here
	local activeManagers = {};
	for _, manager in pairs(ISSearchManager.players) do
		if manager then
			if manager.isSearchMode then
				activeManagers[manager] = true;
			else
				manager:reset();
			end;
		end;
	end;
	---
	local icon, zoneIcons;
	for zoneID, zoneData in pairs(forageData) do
		zoneIcons = zoneData.forageIcons;
		if not zoneIcons then zoneData.forageIcons = {}; end;
		for manager in pairs(activeManagers) do
			if not manager.activeZones[zoneID] then
				manager:removeZoneAndIcons(zoneData);
			end;
		end;
		for iconID in pairs(zoneIcons) do
			local removeIcon = true;
			for manager in pairs(activeManagers) do
				icon = manager.forageIcons[iconID];
				if icon then
					if icon.isNoticed or ((icon:getIsSeen() and icon:getAlpha() > 0) or icon.spotTimer > 0) then
						removeIcon = false;
						icon.isNoticed = false;
					else
						icon:remove();
					end;
				end;
			end;
			if removeIcon then zoneData.forageIcons[iconID] = nil; end;
		end;
		forageSystem.checkRefillZone(zoneData);
		forageClient.updateZone(zoneData);
		for manager in pairs(activeManagers) do
			if manager.activeZones[zoneID] then
				manager.activeZones[zoneID] = nil;
			end;
		end;
	end;
	for manager in pairs(activeManagers) do
		manager.currentZone = nil;
		manager.iconStack = {};
		manager.movedIconsSquares = {};
		manager.checkedSquares = {};
		manager.lastActivationZone = nil;
		manager.lastActivationSquare = nil;
		manager.loadedIconActivation = true;
	end;
end

--[[--======== getZoneData ========--
	@param _forageZone - IsoZone

]]--

function forageSystem.getZoneData(_forageZone, _zoneDef, _x, _y)
	if not _forageZone then return false; end;
	if _forageZone:getType() == "ForageZone" then
		local zoneData = forageData[_forageZone:getName()];
		if zoneData then
			forageClient.updateZone(zoneData);
			return zoneData;
		else
			if _zoneDef then
				print("[forageSystem][getZoneData] zoneData will be initialised for ".. _forageZone:getType());
				return forageSystem.createZoneData(_forageZone, _zoneDef);
			end;
			if _x and _y then
				local defZone, zoneDef = forageSystem.getDefinedZoneAt(_x, _y);
				if (defZone and zoneDef) then
					print("[forageSystem][getZoneData] zoneData will be initialised for ".. _forageZone:getType());
					return forageSystem.createZoneData(_forageZone, zoneDef);
				end;
			end;
		end;
	else
		print("[forageSystem][getZoneData] tried to get zoneData for non scavenge zone: ".. _forageZone:getType());
	end;
	print("[forageSystem][getZoneData] zoneData not found, removing ".. _forageZone:getType());
	return false;
end

--[[---------------------------------------------
--
-- itemDefs
--
--]]---------------------------------------------

--[[--======== lootTableUpdate ========--]]--

function forageSystem.lootTableUpdate()
	--if the month has changed then we need to generate a new loot table
	if forageSystem.lootTableMonth ~= getGameTime():getMonth() + 1 then
		forageSystem.generateLootTable();
	end;
end

--[[--======== generateLootTable ========--]]--

function forageSystem.generateLootTable()
	--todo: more error info for modders in deep loops
	--reset loot tables
	forageSystem.lootTables = {};
	local month = getGameTime():getMonth() + 1;
	forageSystem.lootTableMonth = month;
	--generate for these types of weather - the tables here are passed as parameters to forageSystem.getWeatherBonus
	local weatherTypes = {
		isNormal  = {false, false},
		isRaining = {true, false},
		isSnowing = {false, true},
	};
	--create the table structure
	print("[forageSystem][generateLootTable] Begin generating loot tables");
	-- do final loot table for zone
	local zoneLootTable = {};
	for zoneName in pairs(forageSystem.zoneDefs) do
		forageSystem.lootTables[zoneName] = {};
		forageSystem.lootTables[zoneName][month] = {};
		for weatherType in pairs(weatherTypes) do
			forageSystem.lootTables[zoneName][month][weatherType] = {};
		end;
		-- do final loot table for category
		zoneLootTable[zoneName] = {};
		zoneLootTable[zoneName][month] = {};
		for weatherType in pairs(weatherTypes) do
			zoneLootTable[zoneName][month][weatherType] = {};
			for catName in pairs(forageSystem.catDefs) do
				zoneLootTable[zoneName][month][weatherType][catName] = {category = catName, items = {}};
			end;
		end;
		--
	end;
	print("[forageSystem][generateLootTable] Finished generating loot table structure");
	print("[forageSystem][generateLootTable] Begin populating loot table");
	---
	local getMonthBonus = forageSystem.getMonthBonus;
	local getWeatherBonus = forageSystem.getWeatherBonus;
	--populate the zoneLootTable
	for itemType, itemDef in pairs(forageSystem.itemDefs) do
		for zoneName, zoneChance in pairs(itemDef.zones) do
			if forageSystem.zoneDefs[zoneName] then
				if forageSystem.isValidMonth(nil, itemDef, month) then
					for weatherType, weatherBonusParams in pairs(weatherTypes) do
						for _, catName in ipairs(itemDef.categories) do
							if forageSystem.catDefs[catName] then
								for i = 1, zoneChance * getMonthBonus(itemDef, month) * getWeatherBonus(itemDef, unpack(weatherBonusParams)) do
									table.insert(zoneLootTable[zoneName][month][weatherType][catName].items, itemType);
								end;
							else
								print("[forageSystem][generateLootTable] no such category is defined "..catName..", ignoring for definition "..itemType);
							end;
						end;
					end;
				end;
			else
				print("[forageSystem][generateLootTable] no such zone is defined "..zoneName..", ignoring for definition "..itemType);
			end;
		end;
	end;
	---
	-- inject categories
	for zoneName in pairs(forageSystem.zoneDefs) do
		for weatherType in pairs(weatherTypes) do
			for catName, catDef in pairs(forageSystem.catDefs) do
				if #zoneLootTable[zoneName][month][weatherType][catName].items > 0 then
					for _ = 1, catDef.zoneChance[zoneName] or catDef.chance or 0 do
						table.insert(forageSystem.lootTables[zoneName][month][weatherType], zoneLootTable[zoneName][month][weatherType][catName]);
					end;
				end;
			end;
		end;
	end;
	---
	print("[forageSystem][generateLootTable] Finished populating loot tables");
end

--[[--======== addItemDef ========--
	@param _itemDef - itemDef

	Adds a definition to forageSystem.itemDefs

	example:

	an apple
	only in Forest zone
	chance is 1
	only available in July
	only in the ForestGoods category
	granting 10 xp

	(All missing definition info will be filled in automatically!)
	(See forageDefaultDefs.defaultItemDef for the possible values)

	local appleDef = {
		type = "Base.Apple",
        zones = {
            Forest      = 1,
        },
        categories = { "ForestGoods" },
		months = { 7, },
		xp = 10,
	};

	forageSystem.addItemDef(appleDef);

	if the definition exists, it will overwrite the existing one.
	only one definition may exist per item.

	To modify a definition: see forageSystem.modifyItemDef
]]--

function forageSystem.addItemDef(_itemDef)
	if not (_itemDef and _itemDef.type) then return; end;
	if forageSystem.isItemExist(nil, _itemDef) then
		local def = forageSystem.importDef(_itemDef, forageDefaultDefs.defaultItemDef);
		local defType = def.type;
		if not forageSystem.itemDefs[defType] then
			--add definition
			forageSystem.itemDefs[defType] = def;
			return defType, true; -- defType is added
		else
			print("[forageSystem][addItemDef] item is already defined! ".._itemDef.type);
			print("[forageSystem][addItemDef] using forageSystem.modifyItemDef to change a defined itemDef for ".._itemDef.type);
			forageSystem.modifyItemDef(_itemDef);
		end;
		return defType, false; -- defType was not added
	else
		print("[forageSystem][removeItemDef] no such item, ignoring ".._itemDef.type);
	end;
	return _itemDef.type, false; -- _itemDef.type could not be added
end

--[[--======== removeItemDef ========--
	@param _itemDef - itemDef

	Removes a definition from forageSystem.itemDefs (matching _itemDef.type)

	example:

	to remove the definition for Base.Apple - this is all that is strictly required.

	forageSystem.removeItemDef({type = "Base.Apple"})

	an existing itemDef may be passed to this function too.

	example:

	local appleDef = forageSystem.itemDefs["Base.Apple"];

	forageSystem.removeItemDef(appleDef);
]]--

function forageSystem.removeItemDef(_itemDef)
	if _itemDef and forageSystem.isItemExist(nil, _itemDef) then
		forageSystem.itemDefs[_itemDef.type] = nil; --wipe the definition
	else
		print("[forageSystem][removeItemDef] no such item, ignoring ".._itemDef.type);
	end;
end

--[[--======== modifyItemDef ========--
	@param _itemDef - itemDef

	Removes a definition from forageSystem.itemDefs and replaces it with _itemDef.

	example:

	local appleDef = {
		type = "Base.Apple",
        zones = {
            Forest      = 10,
        },
        categories = { "ForestGoods", "Junk" },
		months = { 7, 8, 9 },
		xp = 1000,
	};

	forageSystem.modifyItemDef(appleDef);

	this can also be done with forageSystem.addItemDef
	both methods are provided for convenience
	if the itemDef does not exist, it will not be added via this method.
]]--

function forageSystem.modifyItemDef(_itemDef)
	if _itemDef and forageSystem.itemDefs[_itemDef] and forageSystem.isItemExist(nil, _itemDef) then
		forageSystem.removeItemDef(_itemDef.type); --cleans up category entries
		forageSystem.addItemDef(_itemDef); --adds the definition fresh
	else
		print("[forageSystem][modifyItemDef] no such item, ignoring ".._itemDef.type);
	end;
end


--[[--======== populateScavengeDefs ========--
	The main method for bulk adding old foraging system definitions to the new system.

	This should always be called before populateItemDefs, so any new definitions can overwrite the old ones!
]]--

function forageSystem.populateScavengeDefs()
	--add backwards compatible definitions
	for categoryName, category in pairs(scavenges) do
		for _, def in ipairs(category) do
			if not def.categories then
				local categoryToUse = categoryName;
				for catName in pairs(forageSystem.catDefs) do
					if string.lower(catName) == string.lower(categoryName) then
						categoryToUse = catName;
					end;
				end;
				def.categories = {tostring(categoryToUse)};
				if not forageSystem.catDefs[categoryToUse] then
					print("[forageSystem][populateScavengeDefs] no such category and did not find a match for " .. categoryName);
					print("[forageSystem][populateScavengeDefs] adding a new category "..categoryToUse.." with the default definition for "..categoryName);
					forageSystem.addCatDef({name = categoryToUse});
				end;
			end;
			forageSystem.addItemDef(def);
		end;
	end;
end

--[[--======== populateItemDefs ========--
	@param _itemDefs - (optional) a table of itemsDefs to add
	@param _clearAllExisting - (optional) clear all existing definitions

	The main method for bulk adding definitions. It can also be used to clear and recreate the entire itemDef table.
	A table full of itemDefs may be added via this method. See forageDefs for how to structure bulk tables.
]]--

function forageSystem.populateItemDefs(_itemDefs, _clearAllExisting)
	--clear the tables
	if (not _itemDefs) or _clearAllExisting then
		forageSystem.itemDefs = {};
	end;
	--populate itemDefs
	for _, def in pairs(_itemDefs or forageDefs) do
		forageSystem.addItemDef(def);
	end;
end

--[[---------------------------------------------
--]]---------------------------------------------

--[[--======== createForageZone ========--
	@param _x, _y - coordinates for zone
	@param _definedZone - (optional) IsoZone - use this defZone instead

	Create a scavenge zone at x/y, optionally sets number of forages remaining
]]--

function forageSystem.createForageZone(_x, _y, _defZone)
	local zoneDef, defZone;
	if _defZone then
		defZone = _defZone;
		zoneDef = forageSystem.getZoneDef(_defZone);
	else
		zoneDef, defZone = forageSystem.getDefinedZoneAt(_x, _y);
	end;
	if not (zoneDef and defZone) then return false; end;
	local forageZone = getWorld():registerZone(getRandomUUID(), "ForageZone", defZone:getX(), defZone:getY(), 0, defZone:getWidth(), defZone:getHeight());
	local zoneData = forageSystem.createZoneData(forageZone, zoneDef);
	forageClient.updateZone(zoneData);
	return zoneData;
end

function forageSystem.getForageZoneAt(_x, _y)
	local zones = getZones(_x, _y, 0);
	local forageZone, defZone;
	if zones then
		for zone in iterList(zones) do
			local zoneName = zone:getType();
			if forageSystem.zoneDefs[zoneName] then defZone = zone; end;
			if zoneName == "ForageZone" then forageZone = zone end;
			if forageZone then return forageSystem.getZoneData(forageZone, defZone and forageSystem.zoneDefs[defZone:getType()], _x, _y); end;
		end;
	end;
	if defZone then return forageSystem.createForageZone(_x, _y, defZone); end;
	return false;
end

function forageSystem.getZoneRandomCoord(_zoneData)
	local x1, x2 = _zoneData.bounds.x1, _zoneData.bounds.x2;
	local y1, y2 = _zoneData.bounds.y1, _zoneData.bounds.y2;
	return ZombRand(x1, x2) + 1, ZombRand(y1, y2) + 1;
end

function forageSystem.getDefinedZoneAt(_x, _y)
	local zones = getZones(_x, _y, 0);
	if zones then
		for zone in iterList(zones) do
			if forageSystem.zoneDefs[zone:getType()] then
				return forageSystem.zoneDefs[zone:getType()], zone;
			end;
		end;
	end;
	return false, false;
end

function forageSystem.isValidFloor(_square, _itemDef, _catDef)
	if not _square then return false; end;
	if _square:Is(IsoFlagType.water) then return _itemDef.isOnWater; end;
	local floorTexture = _square:getFloor():getTextureName();
	if floorTexture then
		for _, floorType in ipairs(_catDef.validFloors) do
			if floorType == "ANY" then return true; end;
			if luautils.stringStarts(floorTexture, floorType) then return true; end;
		end;
	end;
	return false;
end

function forageSystem.isValidSquare(_square, _itemDef, _catDef)
	if not _square then return false; end;
	if _square and _square:getFloor() then
		if _square:Is(IsoFlagType.solid) then return false; end;
		if _square:Is(IsoFlagType.solidtrans) then return false; end;
		if not _square:isNotBlocked(false) then return false; end;
		if _itemDef.forceOutside and not _square:isOutside() then return false; end;
		if _itemDef.forceOnWater and not _square:Is(IsoFlagType.water) then return false; end;
		if not _itemDef.canBeOnTreeSquare and _square:HasTree() then return false; end;
		if _catDef.validFunc then
			_catDef.validFunc(_square, _itemDef, _catDef);
		else
			return forageSystem.isValidFloor(_square, _itemDef, _catDef);
		end;
	end;
	return false;
end

--[[--======== importDef ========--]]--

function forageSystem.importDef(_def, _defaultDef)
	for key, value in pairs(_defaultDef) do
		if _def[key] == nil then _def[key] = value; end;
	end;
	return _def;
end

--[[--======== getZoneDefByType ========--]]--

function forageSystem.getZoneDefByType(_zoneName)
	return forageSystem.zoneDefs[_zoneName];
end

--[[--======== getZoneDef ========--]]--

function forageSystem.getZoneDef(_definedZone)
	return forageSystem.zoneDefs[_definedZone:getType()];
end

--[[--======== addZoneDef ========--]]--

function forageSystem.addZoneDef(_zoneDef, _overwrite)
	local def = forageSystem.importDef(_zoneDef, forageDefaultDefs.defaultZoneDef);
	if forageSystem.zoneDefs[def.name] then
		if _overwrite then
			forageSystem.zoneDefs[def.name] = def;
			print("[forageSystem][addZoneDef] overwriting definition for "..def.name);
		else
			print("[forageSystem][addZoneDef] definition for "..def.name.." exists, ignoring");
		end;
	else
		forageSystem.zoneDefs[def.name] = def;
	end;
end

--[[--======== populateZoneDefs ========--
	@param _zoneDefs - (optional) override default (forageZones) with a new table

	Initialises the zone list, clears forageSystem.zoneDefs
]]--

function forageSystem.populateZoneDefs(_zoneDefs)
	--clear the table
	forageSystem.zoneDefs = {};
	--populate zones
	for _, def in pairs(_zoneDefs or forageZones) do
		print("[forageSystem][populateZoneDefs] Adding zoneDef: " .. def.name)
		forageSystem.addZoneDef(def);
	end;
end;

--[[--======== addCatDef ========--
	@param _zoneDef - category definition as table
	@param _overwrite - (optional) force overwrite if definition exists

	Adds category definition to global table, optionally overwrites existing definition
]]--

function forageSystem.addCatDef(_catDef, _overwrite)
	local def = forageSystem.importDef(_catDef, forageDefaultDefs.defaultCatDef);
	local categoryName = def.name;
	if forageSystem.catDefs[categoryName] then
		if _overwrite then
			print("[forageSystem][addCatDef] overwriting definition for "..categoryName);
		else
			print("[forageSystem][addCatDef] definition for "..categoryName.." exists, ignoring");
			return;
		end;
	end;
	local woSprites = def.spriteAffinities;
	if woSprites and #woSprites > 0 then
		for _, spriteName in ipairs(woSprites) do
			forageSystem.spriteAffinities[spriteName] = def.name;
		end;
	end;
	forageSystem.catDefs[categoryName] = def;
end

--[[--======== populateCatDefs ========--
	@param _catDefs - (optional) override default (forageCategories) with a new table

	Initialises the category list, clears all entries if they exist.
]]--

function forageSystem.populateCatDefs(_catDefs)
	--clear the table
	forageSystem.catDefs = {};
	--populate zones
	for _, def in pairs(_catDefs or forageCategories) do
		forageSystem.addCatDef(def);
	end;
end

--[[---------------------------------------------
--
-- Item
--
--]]---------------------------------------------

--[[--======== addOrDropItems ========--
	@param _character - IsoPlayer
	@param _inventory - inventory used
	@param _itemDef - itemDef
	@param _count - number to add
]]--

function forageSystem.addOrDropItems(_character, _inventory, _itemDef, _count, _discardItems)
	local inv = _inventory;
	local items = inv:AddItems(_itemDef.type, _count);
	if _itemDef.spawnFuncs then
		for _, spawnFunc in ipairs(_itemDef.spawnFuncs) do
			items = spawnFunc(_character, _inventory, _itemDef, items) or items;
		end;
	end;
	if _discardItems or inv:getCapacityWeight() > inv:getEffectiveCapacity(_character) then
		for item in iterList(items) do
			if not _discardItems then _character:getCurrentSquare():AddWorldInventoryItem(item, 0.0, 0.0, 0.0); end;
			inv:Remove(item);
		end;
	end;
	return items;
end

function forageSystem.isValidMonth(_, _itemDef, _zoneDef, _month)
	local month = _month or getGameTime():getMonth() + 1;
	for _, thisMonth in ipairs(_itemDef.months) do
		if month == thisMonth then return true; end;
	end;
	return false;
end

--[[--======== getRefillBonus ========--
	@param _value - (optional) alternate value

	Returns refill bonus value for sandbox setting NatureAbundance
]]--

function forageSystem.getRefillBonus(_zoneData)
	local zoneDef = forageSystem.zoneDefs[_zoneData.name];
	if not zoneDef then
		print("[forageSystem][getRefillBonus] could not find a zoneDef for "..tostring(_zoneData.name));
		print("[forageSystem][getRefillBonus] using bonus value of 0 for " .. tostring(_zoneData.name));
		return 0;
	end;
	---
	local abundanceSetting = zoneDef.abundanceSetting or "NatureAbundance";
	if not (forageSystem[abundanceSetting] and SandboxVars[abundanceSetting]) then
		print("[forageSystem][getRefillBonus] could not find an abundance setting or invalid value for "..tostring(_zoneData.name));
		print("[forageSystem][getRefillBonus] using bonus value of 0 for " .. tostring(_zoneData.name));
		return 0;
	end;
	---
	return 1 + (forageSystem[abundanceSetting][SandboxVars[abundanceSetting]] / 100) or 0;
end

--[[--======== getAimBonus ========--
	@param _character - IsoPlayer
]]--

function forageSystem.getAimBonus(_character)
	if _character then
		if _character:isAiming() then
			return forageSystem.aimMultiplier;
		end;
		if _character:isSneaking() then
			return forageSystem.sneakMultiplier;
		end;
	end;
	return 1.0;
end

--[[--======== getSneakBonus ========--
	@param _character - IsoPlayer
]]--

function forageSystem.getSneakBonus(_character)
	if _character then
		if _character:isSneaking() then
			return forageSystem.sneakMultiplier;
		end;
	end;
	return 1.0;
end

--[[--======== getMovementPenalty ========--
	@param _character - IsoPlayer
]]--

function forageSystem.getMovementPenalty(_character)
	local movementPenalty = 0;
	if _character:isSneaking() then
		movementPenalty = 0;
	elseif _character:isRunning() then
		movementPenalty = 0.8
	elseif _character:isSprinting() then
		movementPenalty = 1;
	end;
	return 1 - movementPenalty;
end

--[[--======== getBodyPenalty ========--
	@param _character - IsoPlayer

	Returns bonus to spot food items when hungry as float 1 - (0-hungerBonusMax)
]]--

function forageSystem.getHungerBonus(_character, _itemDef)
	if not (_itemDef and _itemDef.type) then return 1; end;
	local itemObj = InventoryItemFactory.CreateItem(_itemDef.type);
	local hungerBonus = 0;
	if itemObj and itemObj:IsFood() then
		local hungerLevel = _character:getStats():getHunger();
		hungerBonus = (forageSystem.hungerBonusMax * hungerLevel) / 100;
	end;
	return 1 + hungerBonus;
end

--[[--======== getBodyPenalty ========--
	@param _character - IsoPlayer

	Returns penalty for body conditions as float 1 - (0-bodyPenaltyMax)
]]--

function forageSystem.getBodyPenalty(_character)
	local painLevel = _character:getStats():getPain();
	local sickLevel = _character:getStats():getSickness();
	local foodSickLevel = _character:getBodyDamage():getFoodSicknessLevel() / 100;
	local drunkLevel = _character:getStats():getDrunkenness() / 100;
	local bodyPenalty = 0;
	bodyPenalty = (painLevel + sickLevel + foodSickLevel + drunkLevel) / 4;
	bodyPenalty = math.min(bodyPenalty, forageSystem.bodyPenaltyMax);
	return 1 - bodyPenalty;
end

--[[--======== getClothingPenalty ========--
	@param _character - IsoPlayer

	Returns penalty for clothing as float 1 - (0-clothingPenaltyMax)
]]--

function forageSystem.getClothingPenalty(_character)
	local clothingPenalty = 0;
	local wornItems = _character:getWornItems();
	for wornItem in iterList(wornItems) do
		if wornItem and wornItem:getLocation() then
			clothingPenalty = clothingPenalty + (forageSystem.clothingPenalties[wornItem:getLocation()] or 0);
		end;
	end;
	clothingPenalty = math.min(clothingPenalty, forageSystem.clothingPenaltyMax) / 100;
	return 1 - clothingPenalty;
end

--[[--======== getPanicPenalty ========--
	@param _character - IsoPlayer

	Returns penalty for panic conditions as float 1 - (0-panicPenaltyMax)
]]--

function forageSystem.getPanicPenalty(_character)
	local panicLevel = _character:getStats():getPanic() / 100;
	local fearLevel = _character:getStats():getFear();
	local stressLevel = _character:getStats():getStress();
	local panicPenalty = 0;
	panicPenalty = (panicLevel + fearLevel + stressLevel) / 2; --panic, fear, stress have an extreme malus effect
	panicPenalty = math.min(panicPenalty, forageSystem.panicPenaltyMax);
	return 1 - panicPenalty;
end

--[[--======== getExhaustionPenalty ========--
	@param _character - IsoPlayer

	Returns penalty for exhaustion conditions as float 1 - (0-exhaustionPenaltyMax)
]]--

function forageSystem.getExhaustionPenalty(_character)
	local enduranceLevel = 1 - _character:getStats():getEndurance();
	local fatigueLevel = _character:getStats():getFatigue();
	local exhaustionPenalty = 0;
	exhaustionPenalty = (enduranceLevel + fatigueLevel) / 2;
	exhaustionPenalty = math.min(exhaustionPenalty, forageSystem.exhaustionPenaltyMax);
	return 1 - exhaustionPenalty;
end

--[[--======== getWeatherPenalty ========--
	@param _character - IsoPlayer
	@param _square - IsoGridSquare

	Returns penalty for weather conditions as float 1 - (0-weatherPenaltyMax)
]]--

function forageSystem.getWeatherPenalty(_character, _square)
	if not (_character and _square) then return 1; end;
	if not _square:isOutside() then return 1; end;
	local weatherPenalty = 0;
	local climateManager = getClimateManager();
	local rainLevel = climateManager:getPrecipitationIntensity();
	local fogLevel = climateManager:getFogIntensity();
	local snowLevel = math.min(climateManager:getSnowStrength(), 1);
	--average of fog + rain + snow / 2
	weatherPenalty = (rainLevel + fogLevel + snowLevel) / 2;
	--add up to 33% for cloudy days
	local cloudLevel = climateManager:getCloudIntensity();
	weatherPenalty = weatherPenalty + (cloudLevel / 3);
	local effectReduction = forageSystem.getWeatherEffectReduction(_character);
	weatherPenalty = math.min(weatherPenalty * effectReduction, forageSystem.weatherPenaltyMax);
	return 1 - weatherPenalty;
end

--[[--======== getLightLevelPenalty ========--
	@param _character - IsoPlayer
	@param _square - IsoGridSquare

	Returns penalty for IsoGridSquare as float (0 to 1)
]]--

function forageSystem.getLightLevelPenalty(_character, _square)
	if not (_square and _character) then return 0; end;
	local lightLevel = _square:getLightLevel(_character:getPlayerNum());
	local dayLightStrength = getClimateManager():getDayLightStrength();
	--just make it fully bright if over 80%
	if lightLevel > 0.8 then lightLevel = 1; end;
	local effectReduction = forageSystem.getDarknessEffectReduction(_character);
	if _square:isOutside() then
		return math.max(dayLightStrength, lightLevel) * effectReduction;
	else
		return lightLevel * effectReduction;
	end;
end

--[[--======== getProfessionBonus ========--
	@param _character - IsoPlayer

	Returns trait bonus total in squares
]]--

function forageSystem.getProfessionBonus(_character)
	return forageSystem.professionBonuses[_character:getDescriptor():getProfession()] or 0;
end

--[[--======== getTraitBonus ========--
	@param _character - IsoPlayer

	Returns trait bonus total in squares
]]--

function forageSystem.getTraitBonus(_character)
	local traitBonus = 0;
	for trait, bonus in pairs(forageSystem.traitBonuses) do
		if _character:HasTrait(trait) then traitBonus = traitBonus + bonus; end;
	end;
	return traitBonus;
end

--[[--======== getWeatherEffectReduction ========--
	@param _character - IsoPlayer

	Returns weather effect total reduction for character as percent
]]--

function forageSystem.getWeatherEffectReduction(_character)
	local effectReduction = 0;
	for modifier, bonus in pairs(forageSystem.weatherEffectModifiers) do
		if _character:HasTrait(modifier) then effectReduction = effectReduction + bonus; end;
		if forageSystem.weatherEffectModifiers[_character:getDescriptor():getProfession()] then effectReduction = effectReduction + bonus; end;
	end;
	effectReduction = math.min(math.max(effectReduction, 0), 100);
	return 1 - (effectReduction / 100);
end

--[[--======== getDarknessEffectReduction ========--
	@param _character - IsoPlayer

	Returns darkness effect total reduction for character as percent
]]--

function forageSystem.getDarknessEffectReduction(_character)
	local effectReduction = 0;
	for modifier, bonus in pairs(forageSystem.darknessEffectModifiers) do
		if _character:HasTrait(modifier) then effectReduction = effectReduction + bonus; end;
		if forageSystem.darknessEffectModifiers[_character:getDescriptor():getProfession()] then effectReduction = effectReduction + bonus; end;
	end;
	effectReduction = math.min(math.max(effectReduction, 0), 100);
	return 1 - (effectReduction / 100);
end

--[[--======== getMonthBonus ========--
	@param _itemDef - itemDef
	@param _month - (optional) month to check

	Returns month bonus total for itemDef as percent
]]--

function forageSystem.getMonthBonus(_itemDef, _month)
	if not _itemDef then return 1; end;
	local month = _month or getGameTime():getMonth() + 1;
	local monthBonus, monthMalus = 0, 0;
	for _, bonusMonth in ipairs(_itemDef.bonusMonths) do
		if month == bonusMonth then monthBonus = forageSystem.monthBonus; break; end;
	end;
	for _, malusMonth in ipairs(_itemDef.malusMonths) do
		if month == malusMonth then monthMalus = forageSystem.monthMalus; break; end;
	end;
	return 1 - ((monthBonus + monthMalus) / 100);
end

--[[--======== getWeatherType ========--]]--

function forageSystem.getWeatherType()
	if getClimateManager():getPrecipitationIntensity() > 0 then return "isRaining"; end;
	if getClimateManager():getSnowStrength() > 0 then return "isSnowing"; end;
	return "isNormal";
end

--[[--======== getWeatherBonus ========--
	@param _itemDef - itemDef

	Returns weather bonus total in percent for itemDef
]]--

function forageSystem.getWeatherBonus(_itemDef, _isRaining, _isSnowing)
	if not _itemDef then return 1; end;
	local isRaining = _isRaining or getClimateManager():getPrecipitationIntensity() > 0;
	local isSnowing = _isSnowing or getClimateManager():getSnowStrength() > 0;
	local bonusChance = 100;
	if isSnowing then bonusChance = bonusChance + _itemDef.snowChance; end;
	if isRaining then bonusChance = bonusChance + _itemDef.rainChance; end;
	return (bonusChance / 100)
end

--[[--======== hasRequiredItems ========--
	@param _character - IsoPlayer
	@param _itemDef - itemDef

	Returns true if all items in itemDef are in inventory (matching by tag)
]]--

function forageSystem.hasRequiredItems(_character, _itemDef)
	local itemTest = function(_item, _tag)
		return not _item:isBroken() and _item:hasTag(_tag);
	end;
	local playerInv = _character:getInventory();
	local requiredItems = 0;
	for _, itemTag in ipairs(_itemDef.itemTags) do
		if playerInv:getFirstEvalArgRecurse(itemTest, itemTag) then
			requiredItems = requiredItems + 1;
		end;
	end;
	return #_itemDef.itemTags == requiredItems;
end

--[[--======== hasNeededTraits ========--
	@param _character - IsoPlayer
	@param _itemDef - itemDef

	Returns true if all traits in itemDef are known
]]--

function forageSystem.hasNeededTraits(_character, _itemDef)
	local knownTraits = 0;
	for _, trait in ipairs(_itemDef.traits) do
		if _character:HasTrait(trait) then
			knownTraits = knownTraits + 1;
		end;
	end;
	return #_itemDef.traits == knownTraits;
end

--[[--======== hasNeededRecipes ========--
	@param _character - IsoPlayer
	@param _itemDef - itemDef

	Returns true if all recipes in itemDef are known
]]--

function forageSystem.hasNeededRecipes(_character, _itemDef)
	local knownRecipes = 0;
	for _, recipe in ipairs(_itemDef.recipes) do
		if _character:isRecipeKnown(recipe) then
			knownRecipes = knownRecipes + 1;
		end;
	end;
	return #_itemDef.recipes == knownRecipes;
end

--[[--======== getPerkLevel ========--
	@param _character - IsoPlayer
	@param _itemDef - itemDef

	Returns perk level / number of perks
]]--

function forageSystem.getPerkLevel(_character, _itemDef)
	local perkLevel = 0;
	local numPerks = #_itemDef.perks;
	if numPerks <= 0 then return 0; end;
	for _, perk in ipairs(_itemDef.perks) do
		perkLevel = perkLevel + _character:getPerkLevel(Perks.FromString(perk));
	end;
	perkLevel = math.ceil(perkLevel / numPerks);
	return perkLevel;
end

--[[--======== isItemTypeExist ========--
@param _itemDef - itemDef

Returns true if an item type exists
]]--

function forageSystem.isItemTypeExist(_itemType)
	return (_itemType and ScriptManager.instance:FindItem(_itemType) and true) or false;
end;

--[[--======== hasNeededPerks ========--
@param _character - IsoPlayer
@param _itemDef - itemDef

Returns true if player is sufficient level for all perk requirements
]]--

function forageSystem.hasNeededPerks(_character, _itemDef, _zoneDef)
	return (_itemDef and _itemDef.skill <= forageSystem.getPerkLevel(_character, _itemDef)) or false;
end


--[[--======== isItemExist ========--
	@param _itemDef - itemDef
	@param _zoneDef - zoneDef

	Returns true if an item type exists
]]--

function forageSystem.isItemExist(_character, _itemDef, _zoneDef)
	local itemObj = _itemDef and ScriptManager.instance:FindItem(_itemDef.type);
	return (itemObj and (not itemObj:getObsolete()) and true) or false;
end

--[[--======== isItemInZone ========--
	@param _itemDef - itemDef
	@param _zoneDef - zoneDef

	Returns true if an item spawns in this zone
]]--

function forageSystem.isItemInZone(_character, _itemDef, _zoneDef)
	for zoneName in pairs(_itemDef.zones) do
		if zoneName == _zoneDef.name then return true; end;
	end;
	return false;
end

--[[--======== isForageable ========--
	@param _character - IsoPlayer
	@param _zoneDef - zoneDef
	@param _itemDef - itemDef
]]--

function forageSystem.isForageable(_character, _itemDef, _zoneDef)
	for _, testFunc in ipairs(forageSystem.isForageableFuncs) do
		if type(testFunc) == "function" then
			if not testFunc(_character, _itemDef, _zoneDef) then
				return false;
			end;
		elseif type(testFunc) == "string" then
			if forageSystem[testFunc] then
				if not forageSystem[testFunc](_character, _itemDef, _zoneDef) then
					return false;
				end;
			else
				print("[forageSystem][isForageable] could not find function forageSystem."..testFunc);
			end;
		else
			print("[forageSystem][isForageable] not string or function "..type(testFunc));
			return false;
		end;
	end;
	return true;
end

--[[---------------------------------------------
--
--	Character
--
--]]---------------------------------------------

--[[--======== giveItemXP ========--
	@param _character - IsoPlayer
	@param _itemDef - itemDef
	@param _amount - (optional) override amount of xp

	Awards the _character an _amount or _itemDef defined xp value
]]--

function forageSystem.giveItemXP(_character, _itemDef, _amount)
	local manager = ISSearchManager.getManager(_character);
	if not manager then return; end;
	local pfPerk, currentXP, gainedXP;
	local xpAmount = (_amount or _itemDef.xp or 1);
	local globalXPModifier = 1 - (forageSystem.globalXPModifier / 100);
	local levelXPModifier =  (forageSystem.levelXPModifier / 100);
	local perkLevel = forageSystem.getPerkLevel(_character, _itemDef);
	local diminishingReturn = 1 - ((perkLevel - _itemDef.skill) * levelXPModifier);
	---
	xpAmount = math.max((xpAmount * globalXPModifier) * diminishingReturn, 1);
	for _, perk in ipairs(_itemDef.perks) do
		pfPerk = Perks.FromString(perk);
		if pfPerk then
			currentXP = _character:getXp():getXP(pfPerk);
			_character:getXp():AddXP(pfPerk, xpAmount);
			gainedXP = _character:getXp():getXP(pfPerk) - currentXP;
			if gainedXP > 0 then
				gainedXP = string.format("%.2f", gainedXP);
				table.insert(manager.haloNotes, "[col=137,232,148]"..pfPerk:getName().." "..getText("Challenge_Challenge2_CurrentXp", gainedXP) .. "[/] [img=media/ui/ArrowUp.png]");
			end;
		end;
	end;
end

--[[--======== doEndurancePenalty ========--
	@param _character - IsoPlayer
	@param _amount - (optional) amount to endure

	Returns endurance level
]]--

function forageSystem.doEndurancePenalty(_character, _amount)
	local enduranceLevel = _character:getStats():getEndurance();
	enduranceLevel = enduranceLevel - (_amount or forageSystem.endurancePenalty);
	_character:getStats():setEndurance(enduranceLevel);
	return enduranceLevel;
end

--[[--======== doFatiguePenalty ========--
	@param _character - IsoPlayer
	@param _amount - (optional) amount to fatigue

	Returns fatigue level
]]--

function forageSystem.doFatiguePenalty(_character, _amount)
	local fatigueLevel = _character:getStats():getFatigue();
	fatigueLevel = fatigueLevel + (_amount or forageSystem.fatiguePenalty);
	_character:getStats():setFatigue(fatigueLevel);
	return fatigueLevel;
end
