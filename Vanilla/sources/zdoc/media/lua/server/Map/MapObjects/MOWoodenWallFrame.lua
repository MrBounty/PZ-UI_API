--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

local function noise(message) print('MOWoodenWalFrame.lua: '..message) end

local function CreateObject(sq, spriteName, dir)
	local modData = {}
	modData["need:Base.Plank"] = "2"
	modData["need:Base.Nails"] = "2"

	local cell = getWorld():getCell()
	local north = dir == "N"
	local javaObject = IsoThumpable.new(cell, sq, spriteName, north, modData)

	javaObject:setCanPassThrough(false)
	javaObject:setCanBarricade(true)
	javaObject:setThumpDmg(8)
	javaObject:setIsContainer(false)
	javaObject:setIsDoor(false)
	javaObject:setIsDoorFrame(false)
	javaObject:setCrossSpeed(1.0)
	javaObject:setBlockAllTheSquare(false)
	javaObject:setName("WoodenWallFrame")
	javaObject:setIsDismantable(false)
	javaObject:setCanBePlastered(false)
	javaObject:setIsHoppable(false)
	javaObject:setIsThumpable(true)
	javaObject:setModData(copyTable(modData))
	javaObject:setMaxHealth(50)
	javaObject:setHealth(50)
	javaObject:setBreakSound("BreakObject")
	javaObject:setSpecialTooltip(false)

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
	return javaObject
end

local function NewObjectN(isoObject)
	ReplaceExistingObject(isoObject, "N")
end
local function NewObjectW(isoObject)
	ReplaceExistingObject(isoObject, "W")
end
local function NewObjectNW(isoObject)
	local cell = getWorld():getCell()
	local sq = isoObject:getSquare()
	local index = isoObject:getObjectIndex()
	noise('replacing isoObject at '..sq:getX()..','..sq:getY()..','..sq:getZ())

	sq:transmitRemoveItemFromSquare(isoObject)

	local javaObject = CreateObject(sq, "carpentry_02_101", "N")
	sq:AddSpecialObject(javaObject, index)
	javaObject:transmitCompleteItemToClients()

	javaObject = CreateObject(sq, "carpentry_02_100", "W")
	sq:AddSpecialObject(javaObject, index + 1)
	javaObject:transmitCompleteItemToClients()
end

local PRIORITY = 5

MapObjects.OnNewWithSprite("carpentry_02_100", NewObjectW, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_02_101", NewObjectN, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_02_102", NewObjectNW, PRIORITY)

