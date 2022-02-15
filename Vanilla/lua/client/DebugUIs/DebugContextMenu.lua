--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

DebugContextMenu = {};
DebugContextMenu.staggerBacking = false;
DebugContextMenu.stagTime = 0;
DebugContextMenu.ticked = false;

DebugContextMenu.doDebugMenu = function(player, context, worldobjects, test)
	if not isDebugEnabled() then return true; end
	if test and ISWorldObjectContextMenu.Test then return true end
	
	local square = nil;
	for i,v in ipairs(worldobjects) do
		square = v:getSquare();
		break;
	end
	
	if getCore():getGameMode()=="LastStand" then
		return;
	end
	
	local playerObj = getSpecificPlayer(player)
	local playerInv = playerObj:getInventory()

	local building = square:getBuilding();
--	if building then
--		context:addOption("RBBASIC", building:getDef(), DebugContextMenu.doRandomizedBuilding, getWorld():getRBBasic());
--	end
	context:addOption("Missing 3D items", nil, DebugContextMenu.do3DItem);

--		for i = 0, getWorld():getRandomizedVehicleStoryList():size()-1 do
--			local rvs = getWorld():getRandomizedVehicleStoryList():get(i);

--	local rbOption = context:addOption("Remove All Vehicles on Zone", square:getZone(), DebugContextMenu.onRemoveVehicles);
--	for i = 0, getWorld():getRandomizedVehicleStoryList():size()-1 do
--		local rvs = getWorld():getRandomizedVehicleStoryList():get(i);
--
--		if rvs:getName() == "Basic Car Crash" then
--			local rbOption = context:addOption("ADD BURNT CAR CRASHED", square, DebugContextMenu.doRandomizedVehicleStory, rvs);
--			if not square:getZone() or not rvs:isValid(square:getZone(), square:getChunk(), true) then
--				rbOption.notAvailable = true;
--				local tooltip = ISWorldObjectContextMenu.addToolTip()
--				tooltip:setName("Zone not valid");
--				tooltip.description = rvs:getDebugLine();
--				rbOption.toolTip = tooltip;
--			end
--			break;
--		end
--	end

	context:addOption("Debug interpolation UI", square, DebugContextMenu.onDebugInterpolationUI);
	context:addOption("Debug player UI", square, DebugContextMenu.onDebugPlayerInterpolationUI);
	
	local debugOption = context:addOption("[DEBUG] UIs", worldobjects, nil);
	local subMenu = ISContextMenu:getNew(context);
	context:addSubMenu(debugOption, subMenu);
	
	subMenu:addOption("Tiles Picker", playerObj, DebugContextMenu.onTilesPicker);
	subMenu:addOption("Teleport", playerObj, DebugContextMenu.onTeleportUI);
	subMenu:addOption("Generate Loot UI", playerObj, DebugContextMenu.onGenerateLootUI);
	subMenu:addOption("Running UI", playerObj, DebugContextMenu.onRunningUI);
	subMenu:addOption("Spawn survivor horde in chunk", playerObj, DebugContextMenu.onSpawnSurvivorHorde);
	subMenu:addOption("Attached Items", playerObj, DebugContextMenu.onAttachedItems);
	
	
	if square:getBuilding() then
		DebugContextMenu.addRBDebugMenu(subMenu, square:getBuilding());
		local def = square:getBuilding():getDef();
		local alarm = "(Off)";
		if def:isAlarmed() then
			alarm = "(On)";
		end
		subMenu:addOption("Set Alarm " .. alarm, def, DebugContextMenu.onSetAlarm);
	end

	if square:getZone() then
		DebugContextMenu.addRVSDebugMenu(subMenu, square);
	end
	
	DebugContextMenu.addRZSDebugMenu(subMenu, square);
	
	subMenu:addOption("Make noise", square, DebugContextMenu.onMakeNoise, playerObj);
	
	DebugContextMenu.doCheatMenu(subMenu, playerObj);
	
	subMenu:addOption("Horde Manager", square, DebugContextMenu.onHordeManager, playerObj);

	subMenu:addOption("Spawn Points", square, DebugContextMenu.onSpawnPoints, playerObj);

	subMenu:addOption("Spawn Vehicle", playerObj, DebugContextMenu.onSpawnVehicle);

	DebugContextMenu.doDebugObjectMenu(player, context, worldobjects, test)
	DebugContextMenu.doDebugCorpseMenu(player, context, worldobjects, test)
	DebugContextMenu.doDebugZombieMenu(player, context, worldobjects, test)
	
--	if not DebugContextMenu.staggerBacking then
--		subMenu:addOption("Start Stagger Back", playerObj, DebugContextMenu.stagger, true);
--	else
--		subMenu:addOption("Stop Stagger Back", playerObj, DebugContextMenu.stagger, false);
--	end
end

function DebugContextMenu.do3DItem()
	getCore():countMissing3DItems();
end

function DebugContextMenu.onGenerateLootUI(playerObj)
	local ui = ISLootStreetTestUI:new(0, 0, playerObj);
	ui:initialise();
	ui:addToUIManager();
end

function DebugContextMenu.stagger(player, stag)
	DebugContextMenu.staggerBacking = stag;
	if stag and not DebugContextMenu.ticked then
		DebugContextMenu.ticked = true;
		Events.OnTick.Add(DebugContextMenu.onTick);
	end
end

local function removeDuplicates(list)
	local result = {}
	local seen = {}
	for _,item in ipairs(list) do
		if not seen[item] then
			seen[item] = true
			table.insert(result, item)
		end
	end
	return result
end

function DebugContextMenu.doDebugObjectMenu(player, context, worldobjects, test)
	local x = getMouseX()
	local y = getMouseY()

	local playerObj = getSpecificPlayer(player)
	local playerInv = playerObj:getInventory()

	worldobjects = removeDuplicates(worldobjects)

	local debugOption = context:addOption("[DEBUG] Objects", worldobjects, nil);
	local subMenu = ISContextMenu:getNew(context);
	context:addSubMenu(debugOption, subMenu);

	local sq = getSpecificPlayer(player):getCurrentSquare()
	if sq and sq:getBuilding() then
		if test then return ISWorldObjectContextMenu.setTest() end
		subMenu:addOption("Get Building Key", worldobjects, DebugContextMenu.OnGetBuildingKey, player)
	end

	local window = IsoObjectPicker.Instance:PickWindow(x, y)
	if instanceof(window, "IsoWindow") then
		if test then return ISWorldObjectContextMenu.setTest() end
		subMenu:addOption(window:isLocked() and "Window Unlock" or "Window Lock", worldobjects, DebugContextMenu.OnWindowLock, window)
		subMenu:addOption(window:isPermaLocked() and "Window Perm Unlock" or "Window Perm Lock", worldobjects, DebugContextMenu.OnWindowPermLock, window)
		subMenu:addOption("Window ~Smashed", worldobjects, DebugContextMenu.OnWindowSmash, window)
		subMenu:addOption("Window ~GlassRemoved", worldobjects, DebugContextMenu.OnWindowGlassRemoved, window)
	end

	local metalDrum = nil
	local rainBarrel = nil

	for _,obj in ipairs(worldobjects) do
		if instanceof(obj, "IsoDoor") or (instanceof(obj, "IsoThumpable") and obj:isDoor()) then
			subMenu:addOption(obj:isLocked() and "Door Unlock" or "Door Lock", worldobjects, DebugContextMenu.OnDoorLock, obj)
			subMenu:addOption(string.format("Set Door Key ID (%d)", obj:getKeyId()), worldobjects, DebugContextMenu.OnSetDoorKeyID, obj)
		end
		if instanceof(obj, "IsoGenerator") then
			subMenu:addOption("Generator: Set Fuel", obj, DebugContextMenu.OnGeneratorSetFuel)
		end
		if instanceof(obj, "IsoBarbecue") then
			subMenu:addOption("BBQ: Zero Fuel", obj, DebugContextMenu.OnBBQZeroFuel)
			subMenu:addOption("BBQ: Set Fuel", obj, DebugContextMenu.OnBBQSetFuel)
		end
		if instanceof(obj, "IsoFireplace") then
			subMenu:addOption("Fireplace: Zero Fuel", obj, DebugContextMenu.OnFireplaceZeroFuel)
			subMenu:addOption("Fireplace: Set Fuel", obj, DebugContextMenu.OnFireplaceSetFuel)
		end
		if CCampfireSystem.instance:isValidIsoObject(obj) then
			subMenu:addOption("Campfire: Zero Fuel", obj, DebugContextMenu.OnCampfireZeroFuel)
			subMenu:addOption("Campfire: Set Fuel", obj, DebugContextMenu.OnCampfireSetFuel)
		end
		if not metalDrum and CMetalDrumSystem:isValidIsoObject(obj) then
			if obj:hasModData() and not obj:getModData().haveLogs and not obj:getModData().haveCharcoal then
				subMenu:addOption("Metal Drum: Zero Water", obj, DebugContextMenu.OnMetalDrumZeroWater)
				subMenu:addOption("Metal Drum: Set Water", obj, DebugContextMenu.OnMetalDrumSetWater)
			end
			metalDrum = obj
		end
		if not rainBarrel and CRainBarrelSystem:isValidIsoObject(obj) then
			subMenu:addOption("Rain Barrel: Zero Water", obj, DebugContextMenu.OnRainBarrelZeroWater)
			subMenu:addOption("Rain Barrel: Set Water", obj, DebugContextMenu.OnRainBarrelSetWater)
			rainBarrel = obj
		end
	end

	square = DebugContextMenu.pickSquare(x, y)
	if square then
		for i=1,square:getObjects():size() do
			local obj = square:getObjects():get(i-1)
			if BentFences.getInstance():isBentObject(obj) then
				subMenu:addOption("Un-bend Fence", worldobjects, DebugContextMenu.OnUnbendFence, obj)
			end
			if BentFences.getInstance():isUnbentObject(obj) then
				subMenu:addOption("Bend Fence", worldobjects, DebugContextMenu.OnBendFence, obj)
			end
			if BrokenFences.getInstance():isBreakableObject(obj) then
				subMenu:addOption("Break Fence", worldobjects, DebugContextMenu.OnBreakFence, obj)
			end
			if instanceof(obj, "IsoCompost") then
				subMenu:addOption("Set Compost", worldobjects, DebugContextMenu.OnSetCompost, obj)
			end
		end
	end

	if #subMenu.options == 0 then
		context:removeLastOption()
	end
end

function DebugContextMenu.doDebugCorpseMenu(player, context, worldobjects, test)
	local x = getMouseX()
	local y = getMouseY()

	local body = IsoObjectPicker.Instance:PickCorpse(x, y)
	if not body then return end

	local playerObj = getSpecificPlayer(player)
	local playerInv = playerObj:getInventory()

	if test then return ISWorldObjectContextMenu.setTest() end

	local option = context:addOption("[DEBUG] DeadBody", worldobjects, nil);
	local subMenu = ISContextMenu:getNew(context);
	context:addSubMenu(option, subMenu);

	local text = body:isFakeDead() and "Reanimate (Zombie)" or "Reanimate (Player)"
	subMenu:addOption(text, body, DebugContextMenu.OnReanimateCorpse)

	option = subMenu:addOption("~Crawling", body, DebugContextMenu.OnDeadBodyToggleCrawling)
	subMenu:setOptionChecked(option, body:isCrawling())

	option = subMenu:addOption("~FakeDead", body, DebugContextMenu.OnDeadBodyToggleFakeDead)
	subMenu:setOptionChecked(option, body:isFakeDead())

	subMenu:addOption("Remove", body, DebugContextMenu.OnDeadBodyRemove)

	if #subMenu.options == 0 then
		context:removeLastOption()
	end
end

function DebugContextMenu.doDebugZombieMenu(player, context, worldobjects, test)
	local x = getMouseX()
	local y = getMouseY()

	local debugOption = context:addOption("[DEBUG] Zombies", worldobjects, nil);
	local subMenu = ISContextMenu:getNew(context);
	context:addSubMenu(debugOption, subMenu);

	if isClient() then
		subMenu:addOption("Remove All", obj, DebugContextMenu.OnRemoveAllZombiesClient)
	else
		subMenu:addOption("Remove All", obj, DebugContextMenu.OnRemoveAllZombies)
	end

	local square,sqX,sqY,sqZ = DebugContextMenu.pickSquare(x, y)
	if square then
		for i=1,square:getMovingObjects():size() do
			local obj = square:getMovingObjects():get(i - 1)
			if instanceof(obj, "IsoZombie") then
				subMenu:addOption("Select Zombie", obj, DebugContextMenu.OnSelectZombie)
				subMenu:addOption("Set On Fire", obj, DebugContextMenu.OnSetZombieOnFire)
				break
			end
		end
		if DebugContextMenu.selectedZombie and DebugContextMenu.selectedZombie:getCurrentSquare() == nil then
			DebugContextMenu.selectedZombie = nil
		end
		if DebugContextMenu.selectedZombie then
			local option
			subMenu:addOption("Selected: Walk Here", square, DebugContextMenu.OnSelectedZombieWalk)

			option = subMenu:addOption("Selected: ~Crawling", nil, DebugContextMenu.OnSelectedZombieToggleCrawling)
			subMenu:setOptionChecked(option, DebugContextMenu.selectedZombie:isCrawling())

			option = subMenu:addOption("Selected: ~CanWalk", nil, DebugContextMenu.OnSelectedZombieToggleCanWalk)
			subMenu:setOptionChecked(option, DebugContextMenu.selectedZombie:isCanWalk())

			option = subMenu:addOption("Selected: ~CanCrawlUnderVehicle", nil, DebugContextMenu.OnSelectedZombieToggleCanCrawlUnderVehicle)
			subMenu:setOptionChecked(option, DebugContextMenu.selectedZombie:isCanCrawlUnderVehicle())

			option = subMenu:addOption("Selected: ~FakeDead", nil, DebugContextMenu.OnSelectedZombieToggleFakeDead)
			subMenu:setOptionChecked(option, DebugContextMenu.selectedZombie:isFakeDead())

			option = subMenu:addOption("Selected: Knock Backward", false, DebugContextMenu.OnSelectedZombieKnockDown)
			option = subMenu:addOption("Selected: Knock Forward", true, DebugContextMenu.OnSelectedZombieKnockDown)

			option = subMenu:addOption("Selected: ~Useless", nil, DebugContextMenu.OnSelectedZombieToggleUseless)
			subMenu:setOptionChecked(option, DebugContextMenu.selectedZombie:isUseless())
		end
	end
end

function DebugContextMenu.OnRemoveAllZombies(zombie)
	local zombies = getCell():getObjectList()
	for i=zombies:size(),1,-1 do
		local zombie = zombies:get(i-1)
		if instanceof(zombie, "IsoZombie") then
			zombie:removeFromWorld()
			zombie:removeFromSquare()
		end
	end
end

function DebugContextMenu.OnRemoveAllZombiesClient(zombie)
	SendCommandToServer(string.format("/removezombies -remove true"))
end

function DebugContextMenu.OnSelectZombie(zombie)
	DebugContextMenu.selectedZombie = zombie
end

function DebugContextMenu.OnSelectedZombieWalk(square)
	DebugContextMenu.selectedZombie:pathToLocation(square:getX(), square:getY(), square:getZ())
	if not square:TreatAsSolidFloor() and square:getZ() == DebugContextMenu.selectedZombie:getSquare():getZ() then
		DebugContextMenu.selectedZombie:setVariable("bPathfind", false)
		DebugContextMenu.selectedZombie:setVariable("bMoving", true)
	end
end

function DebugContextMenu.OnSelectedZombieToggleCrawling()
	DebugContextMenu.selectedZombie:toggleCrawling()
end

function DebugContextMenu.OnSelectedZombieToggleCanWalk()
	local zombie = DebugContextMenu.selectedZombie
	zombie:setCanWalk(not zombie:isCanWalk())
end

function DebugContextMenu.OnSelectedZombieToggleCanCrawlUnderVehicle()
	local zombie = DebugContextMenu.selectedZombie
	zombie:setCanCrawlUnderVehicle(not zombie:isCanCrawlUnderVehicle())
end

function DebugContextMenu.OnSelectedZombieToggleFakeDead()
	local zombie = DebugContextMenu.selectedZombie
	zombie:setFakeDead(not zombie:isFakeDead())
end

function DebugContextMenu.OnSelectedZombieKnockDown(hitFromBehind)
	local zombie = DebugContextMenu.selectedZombie
	zombie:knockDown(hitFromBehind)
end

function DebugContextMenu.OnSelectedZombieToggleUseless()
	local zombie = DebugContextMenu.selectedZombie
	zombie:setUseless(not zombie:isUseless())
end

function DebugContextMenu.OnSetZombieOnFire(zombie)
	zombie:SetOnFire()
end

function DebugContextMenu.OnReanimateCorpse(body)
	body:reanimateNow()
end

function DebugContextMenu.OnDeadBodyToggleCrawling(body)
	body:setCrawling(not body:isCrawling())
end

function DebugContextMenu.OnDeadBodyToggleFakeDead(body)
	body:setFakeDead(not body:isFakeDead())
end

function DebugContextMenu.OnDeadBodyRemove(body)
	body:removeFromWorld()
	body:removeFromSquare()
end

function DebugContextMenu.OnGetBuildingKey(worldobjects, player)
	local sq = getSpecificPlayer(player):getCurrentSquare()
	if sq and sq:getBuilding() then
		getSpecificPlayer(player):getInventory():AddItem("Base.Key1"):setKeyId(sq:getBuilding():getDef():getKeyId())
	end
end

function DebugContextMenu.OnDoorLock(worldobjects, door)
	door:setIsLocked(not door:isLocked())
	if instanceof(door, "IsoDoor") and door:checkKeyId() ~= -1 then
		door:setLockedByKey(door:isLocked())
	end
	if instanceof(door, "IsoThumpable") and door:getKeyId() ~= -1 then
		door:setLockedByKey(door:isLocked())
	end
	getPlayer():getMapKnowledge():setKnownBlockedDoor(door, door:isLocked())

	local doubleDoorObjects = buildUtil.getDoubleDoorObjects(door)
	for i=1,#doubleDoorObjects do
		local object = doubleDoorObjects[i]
		object:setLockedByKey(door:isLocked())
	end

	local garageDoorObjects = buildUtil.getGarageDoorObjects(door)
	for i=1,#garageDoorObjects do
		local object = garageDoorObjects[i]
		object:setLockedByKey(door:isLocked())
	end
end

local function OnDoorSetKeyID2(target, button, obj)
	if button.internal == "OK" then
		local text = button.parent.entry:getText()
		local keyId = tonumber(text)
		if not keyId then return end
		obj:setKeyId(keyId)
		
		local doubleDoorObjects = buildUtil.getDoubleDoorObjects(door)
		for i=1,#doubleDoorObjects do
			local object = doubleDoorObjects[i]
			object:setKeyId(keyId)
		end

		local garageDoorObjects = buildUtil.getGarageDoorObjects(door)
		for i=1,#garageDoorObjects do
			local object = garageDoorObjects[i]
			object:setKeyId(keyId)
		end
	end
end

function DebugContextMenu.OnSetDoorKeyID(worldobjects, door)
	local modal = ISTextBox:new(0, 0, 280, 180, "Key ID:", tostring(door:getKeyId()), nil, OnDoorSetKeyID2, nil, door)
	modal:initialise()
	modal:addToUIManager()
end

function DebugContextMenu.OnWindowLock(worldobjects, window)
	window:setIsLocked(not window:isLocked())
end

function DebugContextMenu.OnWindowPermLock(worldobjects, window)
	window:setPermaLocked(not window:isPermaLocked())
end

function DebugContextMenu.OnWindowSmash(worldobjects, window)
	window:setSmashed(not window:isSmashed())
end

function DebugContextMenu.OnWindowGlassRemoved(worldobjects, window)
	window:setGlassRemoved(not window:isGlassRemoved())
end

function DebugContextMenu.pickSquare(x, y)
	local zoom = getCore():getZoom(0)
	local z = getSpecificPlayer(0):getSquare():getZ()
	local worldX = IsoUtils.XToIso(x * zoom, y * zoom, z)
	local worldY = IsoUtils.YToIso(x * zoom, y * zoom, z)
	return getCell():getGridSquare(worldX, worldY, z), worldX, worldY, z
end

function DebugContextMenu.OnBendFence(worldobjects, fence)
	local playerObj = getSpecificPlayer(0)
	local props = fence:getProperties()
	local dir = nil
	if props:Is(IsoFlagType.collideN) and props:Is(IsoFlagType.collideW) then
		dir = (playerObj:getY() >= fence:getY()) and IsoDirections.N or IsoDirections.S
	elseif props:Is(IsoFlagType.collideN) then
		dir = (playerObj:getY() >= fence:getY()) and IsoDirections.N or IsoDirections.S
	else
		dir = (playerObj:getX() >= fence:getX()) and IsoDirections.W or IsoDirections.E
	end
	BentFences.getInstance():bendFence(fence, dir)
end

function DebugContextMenu.OnUnbendFence(worldobjects, fence)
	BentFences.getInstance():unbendFence(fence)
end

function DebugContextMenu.OnBreakFence(worldobjects, fence)
	local playerObj = getSpecificPlayer(0)
	local props = fence:getProperties()
	local dir = nil
	if props:Is(IsoFlagType.collideN) and props:Is(IsoFlagType.collideW) then
		dir = (playerObj:getY() >= fence:getY()) and IsoDirections.N or IsoDirections.S
	elseif props:Is(IsoFlagType.collideN) then
		dir = (playerObj:getY() >= fence:getY()) and IsoDirections.N or IsoDirections.S
	else
		dir = (playerObj:getX() >= fence:getX()) and IsoDirections.W or IsoDirections.E
	end
	fence:destroyFence(dir)
end

function DebugContextMenu.OnBBQZeroFuel(obj)
	local playerObj = getSpecificPlayer(0)
	local args = { x = obj:getX(), y = obj:getY(), z = obj:getZ(), fuelAmt = 0 }
	sendClientCommand(playerObj, 'bbq', 'setFuel', args)
end

local function OnBBQSetFuel2(target, button, obj)
	if button.internal == "OK" then
		local playerObj = getSpecificPlayer(0)
		local text = button.parent.entry:getText()
		if tonumber(text) then
			local fuelAmt = math.min(tonumber(text), 100.0)
			fuelAmt = math.max(fuelAmt, 0.0)
			local args = { x = obj:getX(), y = obj:getY(), z = obj:getZ(), fuelAmt = fuelAmt }
			sendClientCommand(playerObj, 'bbq', 'setFuel', args)
		end
	end
end

function DebugContextMenu.OnBBQSetFuel(obj)
	local modal = ISTextBox:new(0, 0, 280, 180, "Fuel (Minutes):", tostring(obj:getFuelAmount()), nil, OnBBQSetFuel2, nil, obj)
	modal:initialise()
	modal:addToUIManager()
end

function DebugContextMenu.OnCampfireZeroFuel(obj)
	local playerObj = getSpecificPlayer(0)
	local args = { x = obj:getX(), y = obj:getY(), z = obj:getZ(), fuelAmt = 0 }
	CCampfireSystem.instance:sendCommand(playerObj, 'setFuel', args)
end

local function OnCampfireSetFuel2(target, button, obj)
	if button.internal == "OK" then
		local playerObj = getSpecificPlayer(0)
		local text = button.parent.entry:getText()
		if tonumber(text) then
			local fuelAmt = math.min(tonumber(text), 100.0)
			fuelAmt = math.max(fuelAmt, 0.0)
			local args = { x = obj:getX(), y = obj:getY(), z = obj:getZ(), fuelAmt = fuelAmt }
			CCampfireSystem.instance:sendCommand(playerObj, 'setFuel', args)
		end
	end
end

function DebugContextMenu.OnCampfireSetFuel(obj)
	local luaObject = CCampfireSystem.instance:getLuaObjectOnSquare(obj:getSquare())
	if not luaObject then return end
	local modal = ISTextBox:new(0, 0, 280, 180, "Fuel (Minutes):", tostring(luaObject.fuelAmt), nil, OnCampfireSetFuel2, nil, obj)
	modal:initialise()
	modal:addToUIManager()
end

function DebugContextMenu.OnFireplaceZeroFuel(obj)
	local playerObj = getSpecificPlayer(0)
	local args = { x = obj:getX(), y = obj:getY(), z = obj:getZ(), fuelAmt = 0 }
	sendClientCommand(playerObj, 'fireplace', 'setFuel', args)
end

local function OnFireplaceSetFuel2(target, button, obj)
	if button.internal == "OK" then
		local playerObj = getSpecificPlayer(0)
		local text = button.parent.entry:getText()
		if tonumber(text) then
			local fuelAmt = math.min(tonumber(text), 100.0)
			fuelAmt = math.max(fuelAmt, 0.0)
			local args = { x = obj:getX(), y = obj:getY(), z = obj:getZ(), fuelAmt = fuelAmt }
			sendClientCommand(playerObj, 'fireplace', 'setFuel', args)
		end
	end
end

function DebugContextMenu.OnFireplaceSetFuel(obj)
	local modal = ISTextBox:new(0, 0, 280, 180, "Fuel (Minutes):", tostring(obj:getFuelAmount()), nil, OnFireplaceSetFuel2, nil, obj)
	modal:initialise()
	modal:addToUIManager()
end


local function OnSetCompost2(target, button, obj)
	if button.internal == "OK" then
		local text = button.parent.entry:getText()
		if tonumber(text) then
			local compost = math.min(tonumber(text), 100.0)
			compost = math.max(compost, 0.0)
			obj:setCompost(compost)
		end
	end
end

function DebugContextMenu.OnSetCompost(worldobjects, obj)
	local modal = ISTextBox:new(0, 0, 280, 180, "Compost (0-100):", tostring(obj:getCompost()), nil, OnSetCompost2, nil, obj)
	modal:initialise()
	modal:addToUIManager()
end

local function OnGeneratorSetFuel2(target, button, obj)
	if button.internal == "OK" then
		local text = button.parent.entry:getText()
		if tonumber(text) then
			local compost = math.min(tonumber(text), 100.0)
			compost = math.max(compost, 0.0)
			obj:setFuel(compost)
		end
	end
end

function DebugContextMenu.OnGeneratorSetFuel(obj)
	local modal = ISTextBox:new(0, 0, 280, 180, "Fuel (0-100):", tostring(obj:getFuel()), nil, OnGeneratorSetFuel2, nil, obj)
	modal:initialise()
	modal:addToUIManager()
end

function DebugContextMenu.OnMetalDrumZeroWater(obj)
	local playerObj = getSpecificPlayer(0)
	local args = { x = obj:getX(), y = obj:getY(), z = obj:getZ(), index = obj:getObjectIndex(), amount = 0 }
	sendClientCommand(playerObj, 'object', 'setWaterAmount', args)
end

local function OnMetalDrumSetWater2(target, button, obj)
	if button.internal == "OK" then
		local playerObj = getSpecificPlayer(0)
		local text = button.parent.entry:getText()
		if tonumber(text) then
			local waterAmt = math.min(tonumber(text), obj:getWaterMax())
			waterAmt = math.max(waterAmt, 0.0)
			local args = { x = obj:getX(), y = obj:getY(), z = obj:getZ(), index = obj:getObjectIndex(), amount = waterAmt }
			sendClientCommand(playerObj, 'object', 'setWaterAmount', args)
		end
	end
end

function DebugContextMenu.OnMetalDrumSetWater(obj)
	local luaObject = CMetalDrumSystem.instance:getLuaObjectOnSquare(obj:getSquare())
	if not luaObject then return end
	local modal = ISTextBox:new(0, 0, 280, 180, string.format("Water (0-%d):", obj:getWaterMax()), tostring(obj:getWaterAmount()), nil, OnMetalDrumSetWater2, nil, obj)
	modal:initialise()
	modal:addToUIManager()
end

function DebugContextMenu.OnRainBarrelZeroWater(obj)
	local playerObj = getSpecificPlayer(0)
	local args = { x = obj:getX(), y = obj:getY(), z = obj:getZ(), index = obj:getObjectIndex(), amount = 0 }
	sendClientCommand(playerObj, 'object', 'setWaterAmount', args)
end

local function OnRainBarrelSetWater2(target, button, obj)
	if button.internal == "OK" then
		local playerObj = getSpecificPlayer(0)
		local text = button.parent.entry:getText()
		if tonumber(text) then
			local waterAmt = math.min(tonumber(text), obj:getWaterMax())
			waterAmt = math.max(waterAmt, 0.0)
			local args = { x = obj:getX(), y = obj:getY(), z = obj:getZ(), index = obj:getObjectIndex(), amount = waterAmt }
			sendClientCommand(playerObj, 'object', 'setWaterAmount', args)
		end
	end
end

function DebugContextMenu.OnRainBarrelSetWater(obj)
	local luaObject = CRainBarrelSystem.instance:getLuaObjectOnSquare(obj:getSquare())
	if not luaObject then return end
	local modal = ISTextBox:new(0, 0, 280, 180, string.format("Water (0-%d):", obj:getWaterMax()), tostring(obj:getWaterAmount()), nil, OnRainBarrelSetWater2, nil, obj)
	modal:initialise()
	modal:addToUIManager()
end

DebugContextMenu.onHordeManager = function(square, player)
	local ui = ISSpawnHordeUI:new(0, 0, player, square);
	ui:initialise();
	ui:addToUIManager();
end

DebugContextMenu.onSpawnPoints = function(square, player)
	local ui = ISSpawnPointsEditor:new()
	ui:initialise()
	ui:instantiate()
	ui:addToUIManager()
end

DebugContextMenu.doCheatMenu = function(context, playerObj)
	local cheatOption = context:addOption("Cheat", nil, nil);
	local cheatSubMenu = ISContextMenu:getNew(context)
	context:addSubMenu(cheatOption, cheatSubMenu)
	
	local text = "(Off)";
	if playerObj:isGhostMode() then
		text = "(On)";
	end
	cheatSubMenu:addOption("Set Invisible " .. text, playerObj, DebugContextMenu.onCheat, "INVISIBLE");
	text = "(Off)";
	if playerObj:isInvincible() then
		text = "(On)";
	end
	cheatSubMenu:addOption("Invincible " .. text, playerObj, DebugContextMenu.onCheat, "INVINCIBLE");
end

DebugContextMenu.onCheat = function(player, cheat)
	if cheat == "INVISIBLE" then
		player:setGhostMode(not player:isGhostMode());
	end
	if cheat == "INVINCIBLE" then
		player:setInvincible(not player:isInvincible());
	end
end

DebugContextMenu.onSetAlarm = function(def)
	def:setAlarmed(not def:isAlarmed());
end

DebugContextMenu.onMakeNoise = function(square, playerObj)
	addSound(playerObj, square:getX(), square:getY(), square:getZ(), 10, 10)
end

DebugContextMenu.onAttachedItems = function(playerObj)
	local ui = ISAttachedItemsUI:new(0, 0, playerObj);
	ui:initialise();
	ui:addToUIManager();
end

DebugContextMenu.onTeleportUI = function(playerObj)
	local ui = ISTeleportDebugUI:new(0, 0, 300, 200, playerObj, nil, DebugContextMenu.onTeleportValid);
	ui:initialise();
	ui:addToUIManager();
end

DebugContextMenu.onSpawnVehicle = function(playerObj)
	local ui = ISSpawnVehicleUI:new(0, 0, 200, 300, playerObj, nil);
	ui:initialise();
	ui:addToUIManager();
end

DebugContextMenu.onTilesPicker = function(playerObj)
	local ui = ISTilesPickerDebugUI:new(0, 0, playerObj);
	ui:initialise();
	ui:addToUIManager();
end

DebugContextMenu.onTeleportValid = function(button, x, y)
	print("going to ", x, y)
	getPlayer():setX(tonumber(x));
	getPlayer():setY(tonumber(y));
	getPlayer():setLx(tonumber(x));
	getPlayer():setLy(tonumber(y));
end

DebugContextMenu.onDebugInterpolationUI = function(square)
	local zombie = square:getZombie()
	if zombie ~= nil then
		InterpolationPeriodDebug.OnOpenPanel(zombie)
	end
end

DebugContextMenu.onDebugPlayerInterpolationUI = function(square)
	local player = square:getPlayer()
	if player ~= nil then
		InterpolationPlayerPeriodDebug.OnOpenPanel(player)
	end
end

DebugContextMenu.onRunningUI = function(playerObj)
	local ui = ISRunningDebugUI:new(0, 0, playerObj);
	ui:initialise();
	ui:addToUIManager();
end

DebugContextMenu.onSpawnSurvivorHorde = function(playerObj)
	playerObj:getCurrentSquare():getChunk():addSurvivorInHorde(true);
end

DebugContextMenu.addRVSDebugMenu = function(context, square)
	local mainOption = context:addOption("Randomized Vehicle Story", nil, nil);
	local mainSubMenu = ISContextMenu:getNew(context)
	context:addSubMenu(mainOption, mainSubMenu)
	
	local rbOption = mainSubMenu:addOption("Remove All Vehicles on Zone", square:getZone(), DebugContextMenu.onRemoveVehicles);
	
	for i = 0, getWorld():getRandomizedVehicleStoryList():size()-1 do
		local rvs = getWorld():getRandomizedVehicleStoryList():get(i);
		
		local rbOption = mainSubMenu:addOption(rvs:getName(), square, DebugContextMenu.doRandomizedVehicleStory, rvs);
		if not rvs:isValid(square:getZone(), square:getChunk(), true) then
			rbOption.notAvailable = true;
			local tooltip = ISWorldObjectContextMenu.addToolTip()
			tooltip:setName("Zone not valid");
			tooltip.description = rvs:getDebugLine();
			rbOption.toolTip = tooltip;
		end
	end
end

DebugContextMenu.addRZSDebugMenu = function(context, square)
	local mainOption = context:addOption("Randomized Zone Story", nil, nil);
	local mainSubMenu = ISContextMenu:getNew(context)
	context:addSubMenu(mainOption, mainSubMenu)
	
	for i = 0, getWorld():getRandomizedZoneList():size()-1 do
		local rzs = getWorld():getRandomizedZoneList():get(i);
		
		local rbOption = mainSubMenu:addOption(rzs:getName(), square, DebugContextMenu.doRandomizedZoneStory, rzs);
		if not rzs:isValid() then
			rbOption.notAvailable = true;
			local tooltip = ISWorldObjectContextMenu.addToolTip()
			tooltip:setName("Zone not valid");
			tooltip.description = rzs:getDebugLine();
			rbOption.toolTip = tooltip;
		end
	end
end

DebugContextMenu.onRemoveVehicles = function(zone)
	RandomizedVehicleStoryBase.removeAllVehiclesOnZone(zone);
end

DebugContextMenu.doRandomizedZoneStory = function(square, rzs)
	local zone = Zone.new("debugstoryzone", "debugstoryzone", square:getX() - 20, square:getY() - 20, square:getZ(), square:getX() + 20, square:getX() + 20);
	zone:setPickedXForZoneStory(square:getX())
	zone:setPickedYForZoneStory(square:getY())
	zone:setX(square:getX() - (rzs:getMinimumWidth() / 2));
	zone:setY(square:getY() - (rzs:getMinimumHeight() / 2));
	zone:setW(rzs:getMinimumWidth() + 2);
	zone:setH(rzs:getMinimumHeight() + 2);

	rzs:randomizeZoneStory(zone);
end

DebugContextMenu.doRandomizedVehicleStory = function(square, rvs)
	rvs:randomizeVehicleStory(square:getZone(), square:getChunk());
end

DebugContextMenu.addRBDebugMenu = function(context, building)
	local RBBasic = getWorld():getRBBasic();
	
	local mainOption = context:addOption("Randomized Building", nil, nil);
	local mainSubMenu = ISContextMenu:getNew(context)
	context:addSubMenu(mainOption, mainSubMenu)
	
	-- Do survivor stories
	local survivorStoriesOption = mainSubMenu:addOption("Survivor Stories", nil, nil);
	local survivorStoriesSubMenu = ISContextMenu:getNew(mainSubMenu)
	mainSubMenu:addSubMenu(survivorStoriesOption, survivorStoriesSubMenu)
	
	for i=0,RBBasic:getSurvivorStories():size()-1 do
		if RBBasic:getSurvivorStories():get(i):getName() then
			local storyOption = survivorStoriesSubMenu:addOption(RBBasic:getSurvivorStories():get(i):getName(), building:getDef(), DebugContextMenu.doRandomizedBuilding, RBBasic:getSurvivorStories():get(i));
			if not RBBasic:getSurvivorStories():get(i):isValid(building:getDef(), true) then
				storyOption.notAvailable = true;
				local tooltip = ISWorldObjectContextMenu.addToolTip()
				tooltip:setName("Building not valid");
				tooltip.description = RBBasic:getSurvivorStories():get(i):getDebugLine();
				storyOption.toolTip = tooltip;
			end
		end
	end
	
	-- Profession (spawn container with profession-related stuff)
	local professionOption = mainSubMenu:addOption("Profession", nil, nil);
	local professionSubMenu = ISContextMenu:getNew(mainSubMenu)
	mainSubMenu:addSubMenu(professionOption, professionSubMenu)
	for i=0, RBBasic:getSurvivorProfession():size()-1 do
		professionSubMenu:addOption(RBBasic:getSurvivorProfession():get(i), building:getDef(), DebugContextMenu.doRandomizedBuilding, RBBasic:getSurvivorProfession():get(i));
	end
	
	mainSubMenu:addOption("RBBASIC", building:getDef(), DebugContextMenu.doRandomizedBuilding, getWorld():getRBBasic());

	-- now do randomized building stories (ignore RBBasic as it's done on top)
	for i = 0, getWorld():getRandomizedBuildingList():size()-1 do
		local rb = getWorld():getRandomizedBuildingList():get(i);
		if not instanceof(rb, "RBBasic") and rb:getName() then
			local rbOption = mainSubMenu:addOption(rb:getName(), building:getDef(), DebugContextMenu.doRandomizedBuilding, rb);
			if not rb:isValid(building:getDef(), true) then
				rbOption.notAvailable = true;
				local tooltip = ISWorldObjectContextMenu.addToolTip()
				tooltip:setName("Building not valid");
				tooltip.description = RBBasic:getSurvivorStories():get(i):getDebugLine();
				rbOption.toolTip = tooltip;
			end
		end
	end
end

DebugContextMenu.doRandomizedBuilding = function(building, RBdef)
	if instanceof(RBdef, "RandomizedDeadSurvivorBase") then
		local RBBasic = getWorld():getRBBasic();
		RBBasic:doRandomDeadSurvivorStory(building, RBdef);
	elseif instanceof(RBdef, "RandomizedBuildingBase") then
		RBdef:randomizeBuilding(building);
	else
		local RBBasic = getWorld():getRBBasic();
		RBBasic:doProfessionStory(building, RBdef);
	end
end

DebugContextMenu.onTick = function()
	if DebugContextMenu.staggerBacking then
		DebugContextMenu.stagTime = DebugContextMenu.stagTime - 1;
		if DebugContextMenu.stagTime < 0 then
			local chr = IsoPlayer:getInstance();
			DebugContextMenu.stagTime = 300;
			chr:setBumpType("stagger");
			chr:setVariable("BumpDone", false);
			chr:setVariable("BumpFall", true);
			chr:setVariable("BumpFallType", "pushedFront");
		end
	end
end

Events.OnFillWorldObjectContextMenu.Add(DebugContextMenu.doDebugMenu);
