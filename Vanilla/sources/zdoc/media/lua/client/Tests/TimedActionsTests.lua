---@class TimedActionTests
TimedActionTests = {}

local Tests = {}

local PLAYER_NUM = 0
local PLAYER_OBJ = nil
local PLAYER_INV = nil
local PLAYER_SQR = nil
local PLAYER_SQR_ORIG = nil
local DURATION = 100

local function getSquareDelta(dx, dy, dz)
	local square = getCell():getGridSquare(PLAYER_SQR:getX() + dx, PLAYER_SQR:getY() + dy, PLAYER_SQR:getZ() + dz)
	return square, square:getX(), square:getY(), square:getZ()
end

local function removeAllButFloor(square)
	if not square then return nil end
	for i=square:getObjects():size(),2,-1 do
		local isoObject = square:getObjects():get(i-1)
		square:transmitRemoveItemFromSquare(isoObject)
	end
	for i=square:getStaticMovingObjects():size(),1,-1 do
		local isoObject = square:getStaticMovingObjects():get(i-1)
		isoObject:removeFromWorld()
		isoObject:removeFromSquare()
	end
end

local function removeAllButFloorAt(x, y, z)
	local square = getCell():getGridSquare(x, y, z)
	removeAllButFloor(square)
end

local function newObject(x, y, z, spriteName, objectName)
	local square = getCell():getGridSquare(x, y, z)
	if not square then return nil end
	local isoObject = IsoObject.new(square, spriteName, objectName, false)
	square:AddTileObject(isoObject)
	return isoObject
end

local function newMapObject(x, y, z, spriteName)
	newObject(x, y, z, spriteName)
	MapObjects.debugLoadChunk(math.floor(x / 10), math.floor(y / 10))
end

local function newInventoryItem(type)
	local item = InventoryItemFactory.CreateItem(type)
	PLAYER_INV:AddItem(item)
	return item
end

local function newPrimaryItem(type)
	local item = newInventoryItem(type)
	PLAYER_OBJ:setPrimaryHandItem(item)
	return item
end

local function newSecondaryItem(type)
	local item = newInventoryItem(type)
	PLAYER_OBJ:setSecondaryHandItem(item)
	return item
end

local function newDoorFrame(x, y, z)
	return newObject(x, y, z, "walls_exterior_house_01_11", "")
end

local function newDoor(x, y, z)
	local square = getCell():getGridSquare(x, y, z)
	local door = IsoDoor.new(getCell(), square, getSprite("fixtures_doors_01_1"), true)
	square:AddSpecialObject(door)
	return door
end

local function newWindowFrame(x, y, z)
	return newObject(x, y, z, "walls_exterior_house_01_9", "")
end

local function newWindow(x, y, z)
	local square = getCell():getGridSquare(x, y, z)
	local window = IsoWindow.new(getCell(), square, getSprite("fixtures_windows_01_1"), true)
	square:AddSpecialObject(window)
	return window
end

local function newBBQCharcoal(x, y, z)
	local square = getCell():getGridSquare(x, y, z)
	local object = IsoBarbecue.new(getCell(), square, getSprite("appliances_cooking_01_35"))
	square:AddTileObject(object)
	return object
end

local function newBBQPropane(x, y, z)
	local square = getCell():getGridSquare(x, y, z)
	local object = IsoBarbecue.new(getCell(), square, getSprite("appliances_cooking_01_37"))
	square:AddTileObject(object)
	return object
end

-----

Tests.apply_bandage = {
	run = function(self)
		local item = newInventoryItem("Base.Bandage")
		local bodyPart = PLAYER_OBJ:getBodyDamage():getBodyPart(BodyPartType.ForeArm_L)
		-- TODO: disable god mode to see bandage
		ISTimedActionQueue.add(ISApplyBandage:new(PLAYER_OBJ, PLAYER_OBJ, item, bodyPart, DURATION))
	end
}

Tests.activate_car_battery_charger = {
	run = function(self)
		local square = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local item = InventoryItemFactory.CreateItem("Base.CarBatteryCharger")
		local object = IsoCarBatteryCharger.new(item, getCell(), square)
		square:AddSpecialObject(object)
		item = InventoryItemFactory.CreateItem("Base.CarBattery1")
		object:setBattery(item)
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISActivateCarBatteryChargerAction:new(PLAYER_OBJ, object, true, DURATION))
	end,
	validate = function(self)
	end
}

Tests.activate_generator = {
	run = function(self)
		local square = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local item = InventoryItemFactory.CreateItem("Base.Generator")
		local object = IsoGenerator.new(item, getCell(), square)
		object:setConnected(true)
		object:setFuel(100)
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISActivateGenerator:new(PLAYER_NUM, object, true, DURATION))
	end,
	validate = function(self)
	end
}

Tests.fix_generator = {
	run = function(self)
		local square = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local item = InventoryItemFactory.CreateItem("Base.Generator")
		local object = IsoGenerator.new(item, getCell(), square)
		object:setCondition(50)
		newInventoryItem("ElectronicsScrap")
		PLAYER_OBJ:setPerkLevelDebug(PerksElectronics, 4)
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISFixGenerator:new(PLAYER_OBJ, object, DURATION))
	end,
	validate = function(self)
	end
}

Tests.bbq_remove_propane_tank = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local bbq = newBBQPropane(x, y, z)
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISBBQRemovePropaneTank:new(PLAYER_OBJ, bbq, DURATION))
	end
}

Tests.bbq_insert_propane_tank_ground = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local bbq = newBBQPropane(x, y, z)
		local square2 = getSquareDelta(3, 2, 0)
		local tankItem = square2:AddWorldInventoryItem(bbq:removePropaneTank(), 0.5, 0.5, 0)
		ISTimedActionQueue.add(ISWalkToTimedAction:new(PLAYER_OBJ, square2))
		ISTimedActionQueue.add(ISBBQInsertPropaneTank:new(PLAYER_OBJ, bbq, tankItem:getWorldItem(), DURATION))
	end
}

Tests.bbq_insert_propane_tank_inventory = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local bbq = newBBQPropane(x, y, z)
		local square2 = getSquareDelta(3, 2, 0)
		local tankItem = bbq:removePropaneTank()
		PLAYER_OBJ:getInventory():AddItem(tankItem)
		ISTimedActionQueue.add(ISWalkToTimedAction:new(PLAYER_OBJ, square2))
		ISTimedActionQueue.add(ISBBQInsertPropaneTank:new(PLAYER_OBJ, bbq, tankItem, DURATION))
	end
}

Tests.bbq_turn_on = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local bbq = newBBQPropane(x, y, z)
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISBBQToggle:new(PLAYER_OBJ, bbq, DURATION))
	end
}

Tests.bbq_turn_off = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local bbq = newBBQPropane(x, y, z)
		bbq:turnOn()
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISBBQToggle:new(PLAYER_OBJ, bbq, DURATION))
	end
}

Tests.bbq_add_charcoal = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local bbq = newBBQCharcoal(x, y, z)
		local item = newInventoryItem("Base.Charcoal")
		local fuelAmt = 30 -- campingFuelType[item:getType()] * 60
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISBBQAddFuel:new(PLAYER_OBJ, bbq, item, fuelAmt, DURATION))
	end
}

Tests.bbq_add_book = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local bbq = newBBQCharcoal(x, y, z)
		local item = newInventoryItem("Base.Book")
		local fuelAmt = campingFuelCategory[item:getCategory()] * 60
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISBBQAddFuel:new(PLAYER_OBJ, bbq, item, fuelAmt, DURATION))
	end
}

Tests.bbq_light_book_lighter = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local bbq = newBBQCharcoal(x, y, z)
		local book = newInventoryItem("Base.Book")
		local lighter = newInventoryItem("Base.Lighter")
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISBBQLightFromLiterature:new(PLAYER_OBJ, book, lighter, bbq, DURATION))
	end
}

Tests.bbq_light_gas_lighter = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local bbq = newBBQCharcoal(x, y, z)
		bbq:setFuelAmount(20)
		local petrol = newInventoryItem("Base.PetrolCan")
		local lighter = newInventoryItem("Base.Lighter")
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISBBQLightFromPetrol:new(PLAYER_OBJ, bbq, lighter, petrol, DURATION))
	end
}

Tests.grab_corpse = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local corpseItem = InventoryItemFactory.CreateItem("Base.CorpseMale")
		square:AddWorldInventoryItem(corpseItem, 0.5, 0.5, 0)
		local corpse = square:getStaticMovingObjects():get(0)
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISGrabCorpseAction:new(PLAYER_OBJ, corpse, DURATION))
	end
}

Tests.burn_corpse = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local corpseItem = InventoryItemFactory.CreateItem("Base.CorpseMale")
		square:AddWorldInventoryItem(corpseItem, 0.5, 0.5, 0)
		local corpse = square:getStaticMovingObjects():get(0)
		newPrimaryItem("Base.Lighter")
		newSecondaryItem("Base.PetrolCan")
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISBurnCorpseAction:new(PLAYER_OBJ, corpse, DURATION))
	end
}

Tests.place_pipebomb = {
	run = function(self)
		local square = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local item = newInventoryItem("Base.PipeBombRemote")
--		ISTimedActionQueue.add(ISWalkToTimedAction:new(PLAYER_OBJ, square))
		ISTimedActionQueue.add(ISPlaceTrap:new(PLAYER_OBJ, item, DURATION))
	end
}

Tests.take_pipebomb = {
	run = function(self)
		local square = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local item = InventoryItemFactory.CreateItem("Base.PipeBombRemote")
		local trap = IsoTrap.new(item, getCell(), square)
		square:AddTileObject(trap)
		luautils.walkAdj(PLAYER_OBJ, square)
--		ISTimedActionQueue.add(ISWalkToTimedAction:new(PLAYER_OBJ, square))
		ISTimedActionQueue.add(ISTakeTrap:new(PLAYER_OBJ, trap, DURATION))
	end
}

Tests.connect_generator = {
	run = function(self)
		local square = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local item = InventoryItemFactory.CreateItem("Base.Generator")
		local object = IsoGenerator.new(item, getCell(), square)
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISPlugGenerator:new(PLAYER_NUM, object, true, DURATION))
	end,
	validate = function(self)
	end
}

Tests.disconnect_generator = {
	run = function(self)
		local square = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local item = InventoryItemFactory.CreateItem("Base.Generator")
		local object = IsoGenerator.new(item, getCell(), square)
		object:setConnected(true)
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISPlugGenerator:new(PLAYER_NUM, object, false, DURATION))
	end,
	validate = function(self)
	end
}

Tests.add_fuel_to_generator = {
	run = function(self)
		local square = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local item = InventoryItemFactory.CreateItem("Base.Generator")
		local object = IsoGenerator.new(item, getCell(), square)
		local petrol = newInventoryItem("Base.PetrolCan")
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISAddFuel:new(PLAYER_NUM, object, petrol, DURATION))
	end
}

Tests.take_generator = {
	run = function(self)
		local square = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local item = InventoryItemFactory.CreateItem("Base.Generator")
		local object = IsoGenerator.new(item, getCell(), square)
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISTakeGenerator:new(PLAYER_NUM, object, DURATION))
	end,
	validate = function(self)
	end
}

Tests.add_water_to_rainbarrel = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		-- small empty rainbarrel (has some random amount of water)
		newMapObject(x, y, z, "carpentry_02_54")
		local object = CRainBarrelSystem.instance:getIsoObjectOnSquare(square)
		local item = newInventoryItem("Base.WaterBottleFull")
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISAddWaterFromItemAction:new(PLAYER_OBJ, item, object))
	end
}

Tests.add_sheet_to_window = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		newWindowFrame(x, y, z)
		local window = newWindow(x, y, z)
		local sheet = newInventoryItem("Base.Sheet")
		luautils.walkAdjWindowOrDoor(PLAYER_OBJ, square, window)
		ISTimedActionQueue.add(ISAddSheetAction:new(PLAYER_OBJ, window, DURATION))
	end
}

Tests.remove_sheet_from_window = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		newWindowFrame(x, y, z)
		local window = newWindow(x, y, z)
		local sheet = newInventoryItem("Base.Sheet")
		window:addSheet(PLAYER_OBJ)
		luautils.walkAdjWindowOrDoor(PLAYER_OBJ, square, window)
		ISTimedActionQueue.add(ISRemoveSheetAction:new(PLAYER_OBJ, window:HasCurtains(), DURATION))
	end
}

Tests.remove_broken_glass = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		newWindowFrame(x, y, z)
		local window = newWindow(x, y, z)
		window:setSmashed(true)
		luautils.walkAdjWindowOrDoor(PLAYER_OBJ, square, window)
		ISTimedActionQueue.add(ISRemoveBrokenGlass:new(PLAYER_OBJ, window, DURATION))
	end
}

Tests.close_door = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		newDoorFrame(x, y, z)
		local door = newDoor(x, y, z)
		door:ToggleDoor(PLAYER_OBJ)
		luautils.walkAdjWindowOrDoor(PLAYER_OBJ, square, door)
		ISTimedActionQueue.add(ISOpenCloseDoor:new(PLAYER_OBJ, door, 0))
	end
}

Tests.open_door = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		newDoorFrame(x, y, z)
		local door = newDoor(x, y, z)
		luautils.walkAdjWindowOrDoor(PLAYER_OBJ, square, door)
		ISTimedActionQueue.add(ISOpenCloseDoor:new(PLAYER_OBJ, door, 0))
	end
}

Tests.close_curtain_on_closed_door = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		newDoorFrame(x, y, z)
		local door = newDoor(x, y, z)
		door:addSheet(true, nil)
		luautils.walkAdjWindowOrDoor(PLAYER_OBJ, square, door)
		ISTimedActionQueue.add(ISOpenCloseCurtain:new(PLAYER_OBJ, door, 0))
	end
}

Tests.close_curtain_on_open_door = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		newDoorFrame(x, y, z)
		local door = newDoor(x, y, z)
		door:ToggleDoor(PLAYER_OBJ)
		door:addSheet(false, nil)
		luautils.walkAdjWindowOrDoor(PLAYER_OBJ, square, door)
		ISTimedActionQueue.add(ISOpenCloseCurtain:new(PLAYER_OBJ, door, 0))
	end
}

Tests.close_window = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		newWindowFrame(x, y, z)
		local window = newWindow(x, y, z)
		window:ToggleWindow(PLAYER_OBJ)
		luautils.walkAdjWindowOrDoor(PLAYER_OBJ, square, window)
		ISTimedActionQueue.add(ISOpenCloseWindow:new(PLAYER_OBJ, window, 0))
	end
}

Tests.open_window = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		newWindowFrame(x, y, z)
		local window = newWindow(x, y, z)
		luautils.walkAdjWindowOrDoor(PLAYER_OBJ, square, window)
		ISTimedActionQueue.add(ISOpenCloseWindow:new(PLAYER_OBJ, window, 0))
	end
}

Tests.barricade_window_plank = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		newWindowFrame(x, y, z)
		local window = newWindow(x, y, z)
		local hammer = newPrimaryItem("Base.Hammer")
		local plank = newSecondaryItem("Base.Plank")
		local nails = newInventoryItem("Base.Nails")
		local nails = newInventoryItem("Base.Nails")
		luautils.walkAdjWindowOrDoor(PLAYER_OBJ, square, window)
		ISTimedActionQueue.add(ISBarricadeAction:new(PLAYER_OBJ, window, false, false, DURATION))
	end
}

Tests.barricade_window_metal = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		newWindowFrame(x, y, z)
		local window = newWindow(x, y, z)
		local blowTorch = newPrimaryItem("Base.BlowTorch")
		local metalSheet = newSecondaryItem("Base.SheetMetal")
		luautils.walkAdjWindowOrDoor(PLAYER_OBJ, square, window)
		ISTimedActionQueue.add(ISBarricadeAction:new(PLAYER_OBJ, window, true, false, DURATION))
	end
}

Tests.unbarricade_window_plank = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		newWindowFrame(x, y, z)
		local window = newWindow(x, y, z)
		local barricade = IsoBarricade.AddBarricadeToObject(window, false)
		barricade:addPlank(nil, nil)
		local hammer = newPrimaryItem("Base.Hammer")
		luautils.walkAdjWindowOrDoor(PLAYER_OBJ, square, window)
		ISTimedActionQueue.add(ISUnbarricadeAction:new(PLAYER_OBJ, window, DURATION))
	end
}

Tests.unbarricade_window_metal = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		newWindowFrame(x, y, z)
		local window = newWindow(x, y, z)
		local barricade = IsoBarricade.AddBarricadeToObject(window, false)
		barricade:addMetal(nil, nil)
		local hammer = newPrimaryItem("Base.BlowTorch")
		luautils.walkAdjWindowOrDoor(PLAYER_OBJ, square, window)
		ISTimedActionQueue.add(ISUnbarricadeAction:new(PLAYER_OBJ, window, DURATION))
	end
}

Tests.connect_battery_to_charger = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local item = InventoryItemFactory.CreateItem("Base.CarBatteryCharger")
		local object = IsoCarBatteryCharger.new(item, getCell(), square)
		square:AddSpecialObject(object)

		item = newInventoryItem("Base.CarBattery1")
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISConnectCarBatteryToChargerAction:new(PLAYER_OBJ, object, item, DURATION))
	end
}

Tests.place_battery_charger = {
	run = function(self)
		removeAllButFloor(PLAYER_SQR)
		local item = newInventoryItem("Base.CarBatteryCharger")
		ISTimedActionQueue.add(ISPlaceCarBatteryChargerAction:new(PLAYER_OBJ, item, DURATION))
	end
}

Tests.clean_blood_dishcloth = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
--		square:getChunk():addBloodSplat(x+0.5, y+0.5, z, ZombRand(20))
		newPrimaryItem("Base.DishCloth")
		newSecondaryItem("Base.Bleach")
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISCleanBlood:new(PLAYER_OBJ, square, DURATION))
	end
}

Tests.clean_blood_mop = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
--		square:getChunk():addBloodSplat(x+0.5, y+0.5, z, ZombRand(20))
		newPrimaryItem("Base.Mop")
		newSecondaryItem("Base.Bleach")
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISCleanBlood:new(PLAYER_OBJ, square, DURATION))
	end
}

Tests.clear_ashes = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local ashes = newObject(x, y, z, "floors_burnt_01_1", "")
		newPrimaryItem("Base.Shovel")
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISClearAshes:new(PLAYER_OBJ, ashes, DURATION))
	end
}

Tests.remove_bush_axe = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local object = newObject(x, y, z, "f_bushes_1_8", "")
--		square:getErosionData():reset()
		ErosionMain.LoadGridsquare(square)
		newPrimaryItem("Base.Axe")
		luautils.walkAdj(PLAYER_OBJ, square, window)
		ISTimedActionQueue.add(ISRemoveBush:new(PLAYER_OBJ, square, nil))
	end
}

Tests.remove_bush_knife = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local object = newObject(x, y, z, "f_bushes_1_8", "")
--		square:getErosionData():reset()
		ErosionMain.LoadGridsquare(square)
		newPrimaryItem("Base.HuntingKnife")
		luautils.walkAdj(PLAYER_OBJ, square, window)
		ISTimedActionQueue.add(ISRemoveBush:new(PLAYER_OBJ, square, nil))
	end
}

Tests.remove_bush_hands = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local object = newObject(x, y, z, "f_bushes_1_8", "")
--		square:getErosionData():reset()
		ErosionMain.LoadGridsquare(square)
		luautils.walkAdj(PLAYER_OBJ, square, window)
		ISTimedActionQueue.add(ISRemoveBush:new(PLAYER_OBJ, square, nil))
	end
}

Tests.remove_vine_axe = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		local object = newWindowFrame(x, y, z)
		object:setAttachedAnimSprite(ArrayList.new())
		object:getAttachedAnimSprite():add(getSprite("f_wallvines_1_39"):newInstance())
		newPrimaryItem("Base.Axe")
		ISTimedActionQueue.add(ISWalkToTimedAction:new(PLAYER_OBJ, square))
		ISTimedActionQueue.add(ISRemoveBush:new(PLAYER_OBJ, square, true))
	end
}

Tests.remove_vine_knife = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		local object = newWindowFrame(x, y, z)
		object:setAttachedAnimSprite(ArrayList.new())
		object:getAttachedAnimSprite():add(getSprite("f_wallvines_1_39"):newInstance())
		newPrimaryItem("Base.HuntingKnife")
		ISTimedActionQueue.add(ISWalkToTimedAction:new(PLAYER_OBJ, square))
		ISTimedActionQueue.add(ISRemoveBush:new(PLAYER_OBJ, square, true))
	end
}

Tests.remove_grass = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		newObject(x, y, z, "e_newgrass_1_48", "Grass")
		luautils.walkAdj(PLAYER_OBJ, square, window)
		ISTimedActionQueue.add(ISRemoveGrass:new(PLAYER_OBJ, square))
	end
}

Tests.chop_tree = {
	run = function(self)
		local square,x,y,z = getSquareDelta(0, -2, 0)
		removeAllButFloor(square)
		local tree = IsoTree.new(square, "e_americanholly_1_3")
		square:AddTileObject(tree)
		local item = newPrimaryItem("Base.Axe")
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISChopTreeAction:new(PLAYER_OBJ, tree))
	end
}

Tests.dig_grave = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		local square2,x2,y2,z2 = getSquareDelta(1, 2, 0)
		removeAllButFloor(square)
		removeAllButFloor(square2)
		square:getFloor():setSprite(getSprite("blends_natural_01_21"))
		square2:getFloor():setSprite(getSprite("blends_natural_01_21"))
		local item = newPrimaryItem("Base.Shovel")
		local bo = ISEmptyGraves:new("location_community_cemetary_01_33", "location_community_cemetary_01_32", "location_community_cemetary_01_34", "location_community_cemetary_01_35", item)
		bo.player = PLAYER_NUM
		bo.build = true
		bo.canBeBuild = bo:isValid(square, bo.north)
		bo:tryBuild(x, y, z)
	end
}

Tests.bury_corpse = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		local square2,x2,y2,z2 = getSquareDelta(1, 2, 0)
		removeAllButFloor(square)
		removeAllButFloor(square2)
		square:getFloor():setSprite(getSprite("blends_natural_01_21"))
		square2:getFloor():setSprite(getSprite("blends_natural_01_21"))
		local shovel = newInventoryItem("Base.Shovel")
		local bo = ISEmptyGraves:new("location_community_cemetary_01_33", "location_community_cemetary_01_32", "location_community_cemetary_01_34", "location_community_cemetary_01_35", shovel)
		bo.player = PLAYER_NUM
		bo:create(x, y, z, false, bo.sprite)
		local grave = square2:getObjects():get(1)
		newPrimaryItem("Base.CorpseMale")
		luautils.walkAdj(PLAYER_OBJ, square2)
		ISTimedActionQueue.add(ISBuryCorpse:new(PLAYER_NUM, grave, DURATION, shovel))
	end
}

Tests.fill_grave = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		local square2,x2,y2,z2 = getSquareDelta(1, 2, 0)
		removeAllButFloor(square)
		removeAllButFloor(square2)
		square:getFloor():setSprite(getSprite("blends_natural_01_21"))
		square2:getFloor():setSprite(getSprite("blends_natural_01_21"))
		local shovel = newPrimaryItem("Base.Shovel")
		local bo = ISEmptyGraves:new("location_community_cemetary_01_33", "location_community_cemetary_01_32", "location_community_cemetary_01_34", "location_community_cemetary_01_35", shovel)
		bo.player = PLAYER_NUM
		bo:create(x, y, z, false, bo.sprite)
		local grave = square2:getObjects():get(1)
		luautils.walkAdj(PLAYER_OBJ, square2)
		ISTimedActionQueue.add(ISFillGrave:new(PLAYER_NUM, grave, DURATION, shovel))
	end
}

Tests.destroy_crate = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local bo = ISWoodenContainer:new("carpentry_01_19", "carpentry_01_19")
		bo.player = PLAYER_NUM
		bo:create(x, y, z, bo.north, bo.sprite)
		local crate = square:getSpecialObjects():get(0)
		-- test items aren't destroyed
		crate:getContainer():AddItem("Base.PetrolCan")
		newPrimaryItem("Base.Sledgehammer")
		luautils.walkAdj(PLAYER_OBJ, square, crate)
		ISTimedActionQueue.add(ISDestroyStuffAction:new(PLAYER_OBJ, crate))
	end
}

Tests.destroy_curtain = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		local frame = newWindowFrame(x, y, z)
		local window = newWindow(x, y, z)
		-- test curtain is dropped
		window:addSheet(PLAYER_OBJ)
		local curtain = window:HasCurtains()
		newPrimaryItem("Base.Sledgehammer")
		luautils.walkAdjWindowOrDoor(PLAYER_OBJ, square, window)
		ISTimedActionQueue.add(ISDestroyStuffAction:new(PLAYER_OBJ, curtain))
	end
}

-- TODO: test ISDestroyStuffAction stairs, garage doors, graves

Tests.destroy_window_frame = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		local frame = newWindowFrame(x, y, z)
		local window = newWindow(x, y, z)
		-- test curtain is dropped
		window:addSheet(PLAYER_OBJ)
		-- test barricade is destroyed
		local barricade = IsoBarricade.AddBarricadeToObject(window, false)
		barricade:addPlank(nil, nil)
		newPrimaryItem("Base.Sledgehammer")
		luautils.walkAdjWindowOrDoor(PLAYER_OBJ, square, frame)
		ISTimedActionQueue.add(ISDestroyStuffAction:new(PLAYER_OBJ, frame))
	end
}

Tests.dismantle_crate = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local bo = ISWoodenContainer:new("carpentry_01_19", "carpentry_01_19")
		bo.player = PLAYER_NUM
		bo:create(x, y, z, bo.north, bo.sprite)
		local crate = square:getSpecialObjects():get(0)
		-- test items aren't destroyed
		crate:getContainer():AddItem("Base.PetrolCan")
		newPrimaryItem("Base.Saw")
		newSecondaryItem("Base.Screwdriver")
		luautils.walkAdj(PLAYER_OBJ, square, crate)
		ISTimedActionQueue.add(ISDismantleAction:new(PLAYER_OBJ, crate))
	end
}

Tests.place_compost = {
	run = function(self)
	--[[
		newInventoryItem("Base.Plank")
		newInventoryItem("Base.Plank")
		newInventoryItem("Base.Plank")
		newInventoryItem("Base.Plank")
		newInventoryItem("Base.Plank")
		newInventoryItem("Base.Nails")
		newInventoryItem("Base.Nails")
		newInventoryItem("Base.Nails")
		newInventoryItem("Base.Nails")
	--]]
		local bo = ISCompost:new(PLAYER_NUM, "camping_01_19")
		bo.player = PLAYER_NUM
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		bo.build = true
		bo.canBeBuild = bo:isValid(square, bo.north)
		bo:tryBuild(x, y, z)
	end
}

Tests.get_compost = {
	run = function(self)
		local bo = ISCompost:new(PLAYER_NUM, "camping_01_19")
		bo.player = PLAYER_NUM
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		bo:create(x, y, z, bo.north, bo.sprite)
		local compost = square:getSpecialObjects():get(0)
		compost:setCompost(100)
		local sandbag = newPrimaryItem("Base.EmptySandbag")
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISGetCompost:new(PLAYER_OBJ, compost, sandbag, DURATION))
	end
}

Tests.place_campfire = {
	run = function(self)
		newInventoryItem("camping.CampfireKit")
		local bo = campingCampfire:new(PLAYER_OBJ)
		bo.player = PLAYER_NUM
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		bo.build = true
		bo.canBeBuild = bo:isValid(square, bo.north)
		bo:tryBuild(x, y, z)
	end
}

Tests.remove_campfire = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		SCampfireSystem.instance:addCampfire(square)
		local campfire = CCampfireSystem.instance:getLuaObjectAt(x, y, z)
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISRemoveCampfireAction:new(PLAYER_OBJ, campfire, DURATION))
	end
}

Tests.campfire_add_book = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		SCampfireSystem.instance:addCampfire(square)
		local campfire = CCampfireSystem.instance:getLuaObjectAt(x, y, z)
		local item = newInventoryItem("Base.Book")
		local fuelAmt = campingFuelCategory[item:getCategory()] * 60
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISAddFuelAction:new(PLAYER_OBJ, campfire, item, fuelAmt, DURATION))
	end
}

Tests.campfire_light_book_lighter = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		SCampfireSystem.instance:addCampfire(square)
		local campfire = CCampfireSystem.instance:getLuaObjectAt(x, y, z)
		local book = newInventoryItem("Base.Book")
		local fuelAmt = campingFuelCategory[book:getCategory()] * 60
		local lighter = newInventoryItem("Base.Lighter")
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISLightFromLiterature:new(PLAYER_OBJ, book, lighter, campfire, fuelAmt, DURATION))
	end
}

Tests.campfire_light_gas_lighter = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local serverObject = SCampfireSystem.instance:addCampfire(square)
		serverObject:addFuel(20)
		local campfire = CCampfireSystem.instance:getLuaObjectAt(x, y, z)
		local petrol = newInventoryItem("Base.PetrolCan")
		local lighter = newInventoryItem("Base.Lighter")
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISLightFromPetrol:new(PLAYER_OBJ, campfire, lighter, petrol, DURATION))
	end
}

Tests.campfire_light_notched = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local serverObject = SCampfireSystem.instance:addCampfire(square)
		serverObject:addFuel(20)
		local campfire = CCampfireSystem.instance:getLuaObjectAt(x, y, z)
		local plank = newInventoryItem("Base.PercedWood")
		local stick = newInventoryItem("Base.WoodenStick")
		newInventoryItem("Base.WoodenStick")
		newInventoryItem("Base.WoodenStick")
		newInventoryItem("Base.WoodenStick")
		newInventoryItem("Base.WoodenStick")
		luautils.walkAdj(PLAYER_OBJ, square)
		-- FIXME: this might fail if the WoodenStick breaks
		ISTimedActionQueue.add(ISLightFromKindle:new(PLAYER_OBJ, plank, stick, campfire, 1500))
	end
}

Tests.fireplace_add_book = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		local fireplace = IsoFireplace.new(getCell(), square, getSprite("fixtures_fireplaces_01_0"))
		square:AddTileObject(fireplace)
		local item = newInventoryItem("Base.Book")
		local fuelAmt = campingFuelCategory[item:getCategory()] * 60
		ISTimedActionQueue.add(ISWalkToTimedAction:new(PLAYER_OBJ, square))
		ISTimedActionQueue.add(ISFireplaceAddFuel:new(PLAYER_OBJ, fireplace, item, fuelAmt, DURATION))
	end
}

Tests.fireplace_light_book_lighter = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		local fireplace = IsoFireplace.new(getCell(), square, getSprite("fixtures_fireplaces_01_0"))
		square:AddTileObject(fireplace)
		local book = newInventoryItem("Base.Book")
		local fuelAmt = campingFuelCategory[book:getCategory()] * 60
		local lighter = newInventoryItem("Base.Lighter")
		ISTimedActionQueue.add(ISWalkToTimedAction:new(PLAYER_OBJ, square))
		ISTimedActionQueue.add(ISFireplaceLightFromLiterature:new(PLAYER_OBJ, book, lighter, fireplace, fuelAmt, DURATION))
	end
}

Tests.fireplace_light_gas_lighter = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		local fireplace = IsoFireplace.new(getCell(), square, getSprite("fixtures_fireplaces_01_0"))
		square:AddTileObject(fireplace)
		fireplace:setFuelAmount(20)
		local petrol = newInventoryItem("Base.PetrolCan")
		local lighter = newInventoryItem("Base.Lighter")
		ISTimedActionQueue.add(ISWalkToTimedAction:new(PLAYER_OBJ, square))
		ISTimedActionQueue.add(ISFireplaceLightFromPetrol:new(PLAYER_OBJ, fireplace, lighter, petrol, DURATION))
	end
}

Tests.fireplace_light_notched = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		local fireplace = IsoFireplace.new(getCell(), square, getSprite("fixtures_fireplaces_01_0"))
		square:AddTileObject(fireplace)
		fireplace:setFuelAmount(20)
		local plank = newInventoryItem("Base.PercedWood")
		local stick = newInventoryItem("Base.WoodenStick")
		ISTimedActionQueue.add(ISWalkToTimedAction:new(PLAYER_OBJ, square))
		-- FIXME: this might fail if the WoodenStick breaks
		ISTimedActionQueue.add(ISFireplaceLightFromKindle:new(PLAYER_OBJ, plank, stick, fireplace, 1500))
	end
}

Tests.fireplace_extinguish = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, -2, 0)
		removeAllButFloor(square)
		local fireplace = IsoFireplace.new(getCell(), square, getSprite("fixtures_fireplaces_01_0"))
		square:AddTileObject(fireplace)
		fireplace:setFuelAmount(60)
		fireplace:setLit(true)
		ISTimedActionQueue.add(ISWalkToTimedAction:new(PLAYER_OBJ, square))
		ISTimedActionQueue.add(ISFireplaceExtinguish:new(PLAYER_OBJ, fireplace, DURATION))
	end
}

Tests.place_tent = {
	run = function(self)
		newInventoryItem("camping.CampingTentKit")
		local bo = campingTent:new(PLAYER_OBJ, camping.tentSprites.tarp)
		bo.player = PLAYER_NUM
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		removeAllButFloorAt(x, y - 1, z)
		bo.build = true
		bo.canBeBuild = bo:isValid(square, bo.north)
		bo:tryBuild(x, y, z)
	end
}

Tests.remove_tent = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		removeAllButFloorAt(x, y - 1, z)
		local tent = camping.addTent(square, "camping_01_0")
		luautils.walkAdj(PLAYER_OBJ, tent:getSquare())
		ISTimedActionQueue.add(ISRemoveTentAction:new(PLAYER_OBJ, tent, DURATION))
	end
}

Tests.place_trap = {
	run = function(self)
		local item = newInventoryItem("Base.TrapCage")
		local bo = TrapBO:new(PLAYER_OBJ, item)
		bo.player = PLAYER_NUM
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		bo.build = true
		bo.canBeBuild = bo:isValid(square, bo.north)
		bo:tryBuild(x, y, z)
	end
}

Tests.remove_trap = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local item = newInventoryItem("Base.TrapCage")
		local bo = TrapBO:new(PLAYER_OBJ, item)
		bo.player = PLAYER_NUM
		bo:create(x, y, z, false, bo.sprite)
		local luaObject = CTrapSystem.instance:getLuaObjectAt(x, y, z)
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISRemoveTrapAction:new(PLAYER_OBJ, luaObject, DURATION))
	end
}

Tests.add_bait_to_trap = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local item = newInventoryItem("Base.TrapCage")
		local bo = TrapBO:new(PLAYER_OBJ, item)
		bo.player = PLAYER_NUM
		bo:create(x, y, z, false, bo.sprite)
		local luaObject = CTrapSystem.instance:getLuaObjectAt(x, y, z)
		luautils.walkAdj(PLAYER_OBJ, square)
		item = newInventoryItem("Base.Carrots")
		ISTimedActionQueue.add(ISAddBaitAction:new(PLAYER_OBJ, item, luaObject, DURATION))
	end
}

Tests.remove_animal_from_trap = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local item = newInventoryItem("Base.TrapCage")
		local bo = TrapBO:new(PLAYER_OBJ, item)
		bo.player = PLAYER_NUM
		bo:create(x, y, z, false, bo.sprite)
		local sysObject = STrapSystem.instance:getLuaObjectAt(x, y, z)
		sysObject:setAnimal(Animals[1])
		local clientObject = CTrapSystem.instance:getLuaObjectAt(x, y, z)
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISCheckTrapAction:new(PLAYER_OBJ, clientObject, DURATION))
	end
}

Tests.remove_bait_from_trap = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		local item = newInventoryItem("Base.TrapCage")
		local bo = TrapBO:new(PLAYER_OBJ, item)
		bo.player = PLAYER_NUM
		bo:create(x, y, z, false, bo.sprite)
		local sysObject = STrapSystem.instance:getLuaObjectAt(x, y, z)
		sysObject:addBait("Base.Carrots", 0.0, PLAYER_OBJ)
		local clientObject = CTrapSystem.instance:getLuaObjectAt(x, y, z)
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISRemoveBaitAction:new(PLAYER_OBJ, clientObject, DURATION))
	end
}

Tests.farming_plow = {
	run = function(self)
		local item = newInventoryItem("farming.HandShovel")
		local bo = farmingPlot:new(item, PLAYER_OBJ)
		bo.player = PLAYER_NUM
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		square:getFloor():setSprite(getSprite("blends_natural_01_21"))
		bo.build = true
		bo.canBeBuild = bo:isValid(square, bo.north)
		bo:tryBuild(x, y, z)
	end
}

Tests.farming_seed = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		square:getFloor():setSprite(getSprite("blends_natural_01_21"))
		SFarmingSystem.instance:plow(square)
		local plant = CFarmingSystem.instance:getLuaObjectAt(x, y, z)
		local item = newInventoryItem("farming.HandShovel")
		local nbOfSeed = farming_vegetableconf.props["Tomato"].seedsRequired
		local seeds = {}
		for i=1,nbOfSeed do
			table.insert(seeds, newInventoryItem("farming.TomatoSeed"))
		end
		local typeOfSeed = "Tomato"
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISSeedAction:new(PLAYER_OBJ, seeds, nbOfSeed, typeOfSeed, plant, DURATION))
	end
}

Tests.farming_fertilize = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		square:getFloor():setSprite(getSprite("blends_natural_01_21"))
		SFarmingSystem.instance:plow(square)
		SFarmingSystem.instance:getLuaObjectAt(x, y, z):seed("Tomato")
		local plant = CFarmingSystem.instance:getLuaObjectAt(x, y, z)
		local fertilizer = newInventoryItem("Fertilizer")
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISFertilizeAction:new(PLAYER_OBJ, fertilizer, plant, DURATION))
	end
}

Tests.farming_water = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		square:getFloor():setSprite(getSprite("blends_natural_01_21"))
		SFarmingSystem.instance:plow(square)
		SFarmingSystem.instance:getLuaObjectAt(x, y, z):seed("Tomato")
		local plant = CFarmingSystem.instance:getLuaObjectAt(x, y, z)
		local wateringCan = newInventoryItem("farming.WateredCanFull")
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISWaterPlantAction:new(PLAYER_OBJ, wateringCan, 10, square, DURATION))
	end
}

Tests.farming_harvest = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		square:getFloor():setSprite(getSprite("blends_natural_01_21"))
		SFarmingSystem.instance:plow(square)
		local serverLuaObj = SFarmingSystem.instance:getLuaObjectAt(x, y, z)
		serverLuaObj.waterLvl = 100
		serverLuaObj:seed("Tomato")
		SFarmingSystem.instance:growPlant(serverLuaObj, nil, true)
		SFarmingSystem.instance:growPlant(serverLuaObj, nil, true)
		SFarmingSystem.instance:growPlant(serverLuaObj, nil, true)
		SFarmingSystem.instance:growPlant(serverLuaObj, nil, true)
		SFarmingSystem.instance:growPlant(serverLuaObj, nil, true)
		SFarmingSystem.instance:growPlant(serverLuaObj, nil, true)
		serverLuaObj:saveData()
		local plant = CFarmingSystem.instance:getLuaObjectAt(x, y, z)
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISHarvestPlantAction:new(PLAYER_OBJ, plant, DURATION))
	end
}

Tests.farming_cure_flies = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		square:getFloor():setSprite(getSprite("blends_natural_01_21"))
		SFarmingSystem.instance:plow(square)
		local serverLuaObj = SFarmingSystem.instance:getLuaObjectAt(x, y, z)
		serverLuaObj:seed("Tomato")
		serverLuaObj.fliesLvl = 5
		serverLuaObj:saveData()
		local plant = CFarmingSystem.instance:getLuaObjectAt(x, y, z)
		local cure = newInventoryItem("farming.GardeningSprayCigarettes")
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISCureFliesAction:new(PLAYER_OBJ, cure, 5, plant, DURATION))
	end
}

Tests.farming_remove = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
		square:getFloor():setSprite(getSprite("blends_natural_01_21"))
		SFarmingSystem.instance:plow(square)
		local serverLuaObj = SFarmingSystem.instance:getLuaObjectAt(x, y, z)
		serverLuaObj:seed("Tomato")
		local plant = CFarmingSystem.instance:getLuaObjectAt(x, y, z)
		local shovel = newPrimaryItem("farming.Shovel")
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISShovelAction:new(PLAYER_OBJ, shovel, plant, DURATION))
	end
}

Tests.fishing_rod = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
--		square:getFloor():setSprite(getSprite("blends_natural_02_0"))
		local rod = newPrimaryItem("Base.FishingRod")
		local lure = newSecondaryItem("Base.FishingTackle")
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISFishingAction:new(PLAYER_OBJ, square:getFloor(), rod, lure))
	end
}

Tests.fishing_spear = {
	run = function(self)
		local square,x,y,z = getSquareDelta(2, 2, 0)
		removeAllButFloor(square)
--		square:getFloor():setSprite(getSprite("blends_natural_02_0"))
		local rod = newPrimaryItem("Base.WoodenLance")
		local lure = nil
		luautils.walkAdj(PLAYER_OBJ, square)
		ISTimedActionQueue.add(ISFishingAction:new(PLAYER_OBJ, square:getFloor(), rod, lure))
	end
}

-----

local tickRegistered = false
local testsToRun = {}
local currentTest = nil
local pause = 0

local wasBuildMenuCheat = ISBuildMenu.cheat

local function RunTest(name)
	currentTest = name
	pause = getPerformance():getFramerate() * 1
	print('TEST: '..name)
	PLAYER_OBJ = getSpecificPlayer(PLAYER_NUM)
	PLAYER_INV = PLAYER_OBJ:getInventory()
	PLAYER_SQR = PLAYER_OBJ:getCurrentSquare()
	PLAYER_SQR_ORIG = PLAYER_SQR

	-- Some actions use endurance
	PLAYER_OBJ:getBodyDamage():RestoreToFullHealth()

	PLAYER_INV:removeAllItems()
	PLAYER_OBJ:clearWornItems()
	PLAYER_OBJ:setPrimaryHandItem(nil)
	PLAYER_OBJ:setSecondaryHandItem(nil)

	PLAYER_OBJ:Say(name)

	wasBuildMenuCheat = ISBuildMenu.cheat
	ISBuildMenu.cheat = false

	Tests[name]:run()
end

local function PostValidate(name)
	if Tests[name].validate then
		local msg = Tests[name]:validate()
	end

	ISBuildMenu.cheat = wasBuildMenuCheat
end

local function ShouldSkipTest(name)
	return false
end

local function OnTick()
	local playerObj = getSpecificPlayer(PLAYER_NUM)
	local queue = ISTimedActionQueue.getTimedActionQueue(playerObj)
	if not queue or not queue.queue or not queue.queue[1] then
		if currentTest then
			PostValidate(currentTest)
			currentTest = nil
			pause = getTimestampMs() + 1000
		end
		if pause > getTimestampMs() then -- pause so we can view the result
			return
		end
		if PLAYER_OBJ and PLAYER_OBJ:getCurrentSquare() ~= PLAYER_SQR_ORIG then
			PLAYER_OBJ:setX(PLAYER_SQR_ORIG:getX() + 0.5)
			PLAYER_OBJ:setY(PLAYER_SQR_ORIG:getY() + 0.5)
			PLAYER_OBJ:setLx(PLAYER_OBJ:getX())
			PLAYER_OBJ:setLy(PLAYER_OBJ:getY())
			PLAYER_OBJ:setCurrent(PLAYER_SQR_ORIG)
		end
		if #testsToRun == 0 then
			PLAYER_OBJ = nil
			return
		end
		local testName = testsToRun[1]
		table.remove(testsToRun, 1)
		if not ShouldSkipTest(test) then
			RunTest(testName)
		end
	end
end

TimedActionTests.runOne = function(name)
	if not Tests[name] then
		return
	end
	table.insert(testsToRun, name)
	if not tickRegistered then
		Events.OnTick.Add(OnTick)
		tickRegistered = true
	end
end

TimedActionTests.runAll = function()
	table.wipe(testsToRun)
	for name,func in pairs(Tests) do
		table.insert(testsToRun, name)
	end
	if not tickRegistered then
		Events.OnTick.Add(OnTick)
		tickRegistered = true
	end
end

TimedActionTests.stop = function()
	table.wipe(testsToRun)
end
