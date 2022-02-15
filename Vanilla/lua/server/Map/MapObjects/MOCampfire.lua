--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

local function noise(message) SCampfireSystem.instance:noise(message) end

local function removeExistingLuaObject(square)
	local luaObject = SCampfireSystem.instance:getLuaObjectOnSquare(square)
	if luaObject then
		noise('removing luaObject at same location as newly-loaded isoObject')
		SCampfireSystem.instance:removeLuaObject(luaObject)
	end
end

local function ReplaceExistingObject(isoObject, fuelAmount)
	local square = isoObject:getSquare()
	removeExistingLuaObject(square)
	local modData = isoObject:getModData()
	modData.exterior = square:isOutside()
	modData.isLit = fuelAmount > 0
	modData.fuelAmt = fuelAmount
	return isoObject
end

local function NewBurning(object)
	-- This sprite isn't used, an IsoFire object draws the flame.
	ReplaceExistingObject(object, ZombRand(3, 6) * 60)
end

local function NewSmouldering(object)
	ReplaceExistingObject(object, ZombRand(30, 60))
end

local function NewExtinguished(object)
	ReplaceExistingObject(object, 0)
end

local PRIORITY = 5

MapObjects.OnNewWithSprite("camping_01_4", NewBurning, PRIORITY)
MapObjects.OnNewWithSprite("camping_01_5", NewSmouldering, PRIORITY)
MapObjects.OnNewWithSprite("camping_01_6", NewExtinguished, PRIORITY)

-- -- -- -- --

local function LoadCampfire(isoObject, fuelAmount)
	local square = isoObject:getSquare()

	-- FIXME: Two of the campfire sprites create IsoLightSwitch objects :-(
	if instanceof(isoObject, "IsoLightSwitch") then
		noise("replacing IsoLightSwitch with IsoObject " .. square:getX() .. " " .. square:getY());
		local spriteName = isoObject:getSprite():getName()
		local index = isoObject:getObjectIndex()
		square:transmitRemoveItemFromSquare(isoObject)
		isoObject = IsoObject.new(square, spriteName, "Campfire")
		local modData = isoObject:getModData()
		modData.exterior = square:isOutside()
		modData.isLit = fuelAmount > 0
		modData.fuelAmt = fuelAmount
		square:AddTileObject(isoObject, index)
		isoObject:transmitCompleteItemToClients()
	end

	if not SCampfireSystem.instance:isValidModData(isoObject:getModData()) then
		isoObject = ReplaceExistingObject(isoObject, fuelAmount)
	end

	if "Campfire" ~= isoObject:getName() then
		noise('set object name')
		isoObject:setName("Campfire")
		if isServer() then
			isoObject:sendObjectChange("name")
		end
	end
	SCampfireSystem.instance:loadIsoObject(isoObject)
end

local function LoadBurning(object)
	LoadCampfire(object, ZombRand(3, 6) * 60)
end

local function LoadSmouldering(object)
	LoadCampfire(object, ZombRand(30, 60))
end

local function LoadExtinguished(object)
	LoadCampfire(object, 0)
end

MapObjects.OnLoadWithSprite("camping_01_4", LoadBurning, PRIORITY)
MapObjects.OnLoadWithSprite("camping_01_5", LoadSmouldering, PRIORITY)
MapObjects.OnLoadWithSprite("camping_01_6", LoadExtinguished, PRIORITY)

