--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

require "RainBarrel/BuildingObjects/RainCollectorBarrel"

local function noise(message) SRainBarrelSystem.instance:noise(message) end

local function CreateBarrel(sq, spriteName, health, waterAmount, waterMax)
	local modData = {}
	modData["need:Base.Plank"] = "4"
	modData["need:Base.Nails"] = "4"
	modData["need:Base.Garbagebag"] = "4"
	modData["waterAmount"] = waterAmount
	modData["waterMax"] = waterMax

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
	javaObject:setName("Rain Collector Barrel")
	javaObject:setIsDismantable(true)
	javaObject:setCanBePlastered(false)
	javaObject:setIsHoppable(false)
	javaObject:setIsThumpable(true)
	javaObject:setModData(copyTable(modData))
	javaObject:setMaxHealth(health)
	javaObject:setHealth(health)
	javaObject:setBreakSound("BreakObject")
	javaObject:setSpecialTooltip(true)

	javaObject:setWaterAmount(waterAmount)
	javaObject:setTaintedWater(waterAmount > 0 and sq:isOutside())

	return javaObject
end

local function ReplaceExistingObject(isoObject, health, waterAmount, waterMax)
	local sq = isoObject:getSquare()
	noise('replacing isoObject at '..sq:getX()..','..sq:getY()..','..sq:getZ())
	local javaObject = CreateBarrel(sq, isoObject:getSprite():getName(), health, waterAmount, waterMax)
	local index = isoObject:getObjectIndex()
	sq:transmitRemoveItemFromSquare(isoObject)
	sq:AddSpecialObject(javaObject, index)
	javaObject:transmitCompleteItemToClients()
	return javaObject
end

local function NewLargeEmpty(isoObject)
	local health = 200 + 5 * 50 -- Level 5 carpentry.  TODO: Randomize?
	local waterAmount = ZombRand(0, RainCollectorBarrel.largeWaterMax * 0.75)
	local waterMax = RainCollectorBarrel.largeWaterMax
	ReplaceExistingObject(isoObject, health, waterAmount, waterMax)
end

local function NewLargeFull(isoObject)
	local health = 200 + 5 * 50 -- Level 5 carpentry.  TODO: Randomize?
	local waterAmount = ZombRand(RainCollectorBarrel.largeWaterMax * 0.75, RainCollectorBarrel.largeWaterMax)
	local waterMax = RainCollectorBarrel.largeWaterMax
	ReplaceExistingObject(isoObject, health, waterAmount, waterMax)
end

local function NewSmallEmpty(isoObject)
	local health = 200 + 5 * 50 -- Level 5 carpentry.  TODO: Randomize?
	local waterAmount = ZombRand(0, RainCollectorBarrel.smallWaterMax * 0.75)
	local waterMax = RainCollectorBarrel.smallWaterMax
	ReplaceExistingObject(isoObject, health, waterAmount, waterMax)
end

local function NewSmallFull(isoObject)
	local health = 200 + 5 * 50 -- Level 5 carpentry.  TODO: Randomize?
	local waterAmount = ZombRand(RainCollectorBarrel.smallWaterMax * 0.75, RainCollectorBarrel.smallWaterMax)
	local waterMax = RainCollectorBarrel.smallWaterMax
	ReplaceExistingObject(isoObject, health, waterAmount, waterMax)
end

local PRIORITY = 5

MapObjects.OnNewWithSprite("carpentry_02_52", NewLargeEmpty, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_02_53", NewLargeFull, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_02_54", NewSmallEmpty, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_02_55", NewSmallFull, PRIORITY)

-- -- -- -- --

local function LoadObject(isoObject, health, waterAmount, waterMax)
	local sq = isoObject:getSquare()
	if instanceof(isoObject, "IsoThumpable") then
	else
		isoObject = ReplaceExistingObject(isoObject, health, waterAmount, waterMax)
	end
	SRainBarrelSystem.instance:loadIsoObject(isoObject)
end

local function LoadLargeEmpty(isoObject)
	local health = 200 + 5 * 50 -- Level 5 carpentry.  TODO: Randomize?
	local waterAmount = ZombRand(0, RainCollectorBarrel.largeWaterMax * 0.75)
	local waterMax = RainCollectorBarrel.largeWaterMax
	LoadObject(isoObject, health, waterAmount, waterMax)
end

local function LoadLargeFull(isoObject)
	local health = 200 + 5 * 50 -- Level 5 carpentry.  TODO: Randomize?
	local waterAmount = ZombRand(RainCollectorBarrel.largeWaterMax * 0.75, RainCollectorBarrel.largeWaterMax)
	local waterMax = RainCollectorBarrel.largeWaterMax
	LoadObject(isoObject, health, waterAmount, waterMax)
end

local function LoadSmallEmpty(isoObject)
	local health = 200 + 5 * 50 -- Level 5 carpentry.  TODO: Randomize?
	local waterAmount = ZombRand(0, RainCollectorBarrel.smallWaterMax * 0.75)
	local waterMax = RainCollectorBarrel.smallWaterMax
	LoadObject(isoObject, health, waterAmount, waterMax)
end

local function LoadSmallFull(isoObject)
	local health = 200 + 5 * 50 -- Level 5 carpentry.  TODO: Randomize?
	local waterAmount = ZombRand(RainCollectorBarrel.smallWaterMax * 0.75, RainCollectorBarrel.smallWaterMax)
	local waterMax = RainCollectorBarrel.smallWaterMax
	LoadObject(isoObject, health, waterAmount, waterMax)
end

MapObjects.OnLoadWithSprite("carpentry_02_52", LoadLargeEmpty, PRIORITY)
MapObjects.OnLoadWithSprite("carpentry_02_53", LoadLargeFull, PRIORITY)
MapObjects.OnLoadWithSprite("carpentry_02_54", LoadSmallEmpty, PRIORITY)
MapObjects.OnLoadWithSprite("carpentry_02_55", LoadSmallFull, PRIORITY)

