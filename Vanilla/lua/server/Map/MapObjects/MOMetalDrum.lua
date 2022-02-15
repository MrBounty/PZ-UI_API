--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

local function noise(message) SMetalDrumSystem.instance:noise(message) end

local function CreateObject(sq, spriteName, waterAmount, haveLogs, isLit)
	local modData = {}
	modData["haveCharcoal"] = false
	modData["haveLogs"] = haveLogs
	modData["isLit"] = isLit
	modData["waterAmount"] = waterAmount
	modData["waterMax"] = ISMetalDrum.waterMax
	modData["taintedWater"] = sq:isOutside()

	local cell = getWorld():getCell()
	local north = false
	local javaObject = IsoThumpable.new(cell, sq, spriteName, north, modData)

	javaObject:setCanPassThrough(false)
	javaObject:setCanBarricade(false)
	javaObject:setThumpDmg(8)
	javaObject:setIsContainer(false)
	javaObject:setIsDoor(false)
	javaObject:setIsDoorFrame(false)
	javaObject:setCrossSpeed(1.0)
	javaObject:setBlockAllTheSquare(true)
	javaObject:setName("MetalDrum")
	javaObject:setIsDismantable(true)
	javaObject:setCanBePlastered(false)
	javaObject:setIsHoppable(false)
	javaObject:setIsThumpable(true)
	javaObject:setModData(copyTable(modData))
	javaObject:setMaxHealth(200)
	javaObject:setHealth(200)
	javaObject:setBreakSound("BreakObject")
	javaObject:setSpecialTooltip(true)

	javaObject:setWaterAmount(waterAmount)

	return javaObject
end

local function ReplaceExistingObject(isoObject, waterAmount, haveLogs, isLit)
	local sq = isoObject:getSquare()
	noise('replacing isoObject at '..sq:getX()..','..sq:getY()..','..sq:getZ())
	local javaObject = CreateObject(sq, isoObject:getSprite():getName(), waterAmount, haveLogs, isLit)
	local index = isoObject:getObjectIndex()
	sq:transmitRemoveItemFromSquare(isoObject)
	sq:AddSpecialObject(javaObject, index)
	javaObject:transmitCompleteItemToClients()
	return javaObject
end

local function NewEmpty(isoObject)
	local waterAmount = isoObject:getSquare():isOutside() and ZombRand(0, ISMetalDrum.waterMax * 0.75) or 0
	local haveLogs = false
	local isLit = false
	ReplaceExistingObject(isoObject, waterAmount, haveLogs, isLit)
end

local function NewWater(isoObject)
	local waterAmount = isoObject:getSquare():isOutside() and ZombRand(ISMetalDrum.waterMax * 0.75, ISMetalDrum.waterMax) or 0
	local haveLogs = false
	local isLit = false
	ReplaceExistingObject(isoObject, waterAmount, haveLogs, isLit)
end

local function NewFireExtinguished(isoObject)
	local waterAmount = 0
	local haveLogs = true
	local isLit = false
	ReplaceExistingObject(isoObject, waterAmount, haveLogs, isLit)
end

local function NewFireBurning(isoObject)
	local waterAmount = 0
	local haveLogs = true
	local isLit = true
	ReplaceExistingObject(isoObject, waterAmount, haveLogs, isLit)
end

local PRIORITY = 5

MapObjects.OnNewWithSprite("crafted_01_24", NewEmpty, PRIORITY)
MapObjects.OnNewWithSprite("crafted_01_25", NewWater, PRIORITY)
MapObjects.OnNewWithSprite("crafted_01_26", NewFireExtinguished, PRIORITY)
MapObjects.OnNewWithSprite("crafted_01_27", NewFireBurning, PRIORITY)

MapObjects.OnNewWithSprite("crafted_01_28", NewEmpty, PRIORITY)
MapObjects.OnNewWithSprite("crafted_01_29", NewWater, PRIORITY)
MapObjects.OnNewWithSprite("crafted_01_30", NewFireExtinguished, PRIORITY)
MapObjects.OnNewWithSprite("crafted_01_31", NewFireBurning, PRIORITY)

-- -- -- -- --

local function LoadObject(isoObject, waterAmount, haveLogs, isLit)
	local sq = isoObject:getSquare()
	if instanceof(isoObject, "IsoThumpable") then
	else
		isoObject = ReplaceExistingObject(isoObject, waterAmount, haveLogs, isLit)
	end
	SMetalDrumSystem.instance:loadIsoObject(isoObject)
end

local function LoadEmpty(isoObject)
	local waterAmount = isoObject:getSquare():isOutside() and ZombRand(0, ISMetalDrum.waterMax * 0.75) or 0
	local haveLogs = false
	local isLit = false
	LoadObject(isoObject, waterAmount, haveLogs, isLit)
end

local function LoadWater(isoObject)
	local waterAmount = isoObject:getSquare():isOutside() and ZombRand(ISMetalDrum.waterMax * 0.75, ISMetalDrum.waterMax) or 0
	local haveLogs = false
	local isLit = false
	LoadObject(isoObject, waterAmount, haveLogs, isLit)
end

local function LoadFireExtinguished(isoObject)
	local waterAmount = 0
	local haveLogs = true
	local isLit = false
	LoadObject(isoObject, waterAmount, haveLogs, isLit)
end

local function LoadFireBurning(isoObject)
	local waterAmount = 0
	local haveLogs = true
	local isLit = true
	LoadObject(isoObject, waterAmount, haveLogs, isLit)
end

MapObjects.OnLoadWithSprite("crafted_01_24", LoadEmpty, PRIORITY)
MapObjects.OnLoadWithSprite("crafted_01_25", LoadWater, PRIORITY)
MapObjects.OnLoadWithSprite("crafted_01_26", LoadFireExtinguished, PRIORITY)
MapObjects.OnLoadWithSprite("crafted_01_27", LoadFireBurning, PRIORITY)

MapObjects.OnLoadWithSprite("crafted_01_28", LoadEmpty, PRIORITY)
MapObjects.OnLoadWithSprite("crafted_01_29", LoadWater, PRIORITY)
MapObjects.OnLoadWithSprite("crafted_01_30", LoadFireExtinguished, PRIORITY)
MapObjects.OnLoadWithSprite("crafted_01_31", LoadFireBurning, PRIORITY)


