--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

local function noise(message) print('MOLampOnPillar.lua: '..message) end

local function CreateObject(sq, spriteName, dir)
	local modData = {}
	modData["need:Base.Plank"] = "2"
	modData["need:Base.Rope"] = "1"
	modData["need:Base.Nails"] = "4"
	modData["need:Base.Torch"] = "1"

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
	javaObject:setName("Lamp on Pillar")
	javaObject:setIsDismantable(true)
	javaObject:setCanBePlastered(false)
	javaObject:setIsHoppable(false)
	javaObject:setIsThumpable(true)
	javaObject:setModData(copyTable(modData))
	javaObject:setMaxHealth(200)
	javaObject:setHealth(200)
	javaObject:setBreakSound("BreakObject")
	javaObject:setSpecialTooltip(false)

	-- light stuff
	local offsetX = 0
	local offsetY = 0
	if dir == "E" then
		offsetX = 5
	elseif dir == "W" then
		offsetX = -5
	elseif dir == "S" then
		offsetY = 5
	elseif dir == "N" then
		offsetY = -5
	end
	javaObject:createLightSource(10, offsetX, offsetY, 0, 0, "Base.Battery", nil, nil)

	local fuel = InventoryItemFactory.CreateItem("Base.Battery")
	if fuel then
		fuel:setUsedDelta(1.0)
		local previous = javaObject:insertNewFuel(fuel, nil)
	end

	return javaObject
end

local function ReplaceExistingObject(isoObject, dir)
	local cell = getWorld():getCell()
	local sq = isoObject:getSquare()
	noise('replacing isoObject at '..sq:getX()..','..sq:getY()..','..sq:getZ())
	local javaObject = CreateObject(sq, isoObject:getSprite():getName(), dir)
	local index = isoObject:getObjectIndex()
	sq:transmitRemoveItemFromSquare(isoObject)
	sq:AddSpecialObject(javaObject, index)
	javaObject:transmitCompleteItemToClients()
end

local function NewObjectN(isoObject)
	ReplaceExistingObject(isoObject, "N")
end
local function NewObjectS(isoObject)
	ReplaceExistingObject(isoObject, "S")
end
local function NewObjectW(isoObject)
	ReplaceExistingObject(isoObject, "W")
end
local function NewObjectE(isoObject)
	ReplaceExistingObject(isoObject, "E")
end

local PRIORITY = 5

MapObjects.OnNewWithSprite("carpentry_02_59", NewObjectN, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_02_60", NewObjectS, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_02_61", NewObjectW, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_02_62", NewObjectE, PRIORITY)

