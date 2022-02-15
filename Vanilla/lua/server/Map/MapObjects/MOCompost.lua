--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

local PRIORITY = 5

local function ReplaceExistingObject(object, compost)
	local cell = getWorld():getCell()
	local square = object:getSquare()
	local javaObject = IsoCompost.new(cell, square)
	javaObject:setCompost(compost)

	local index = object:getObjectIndex()
	square:transmitRemoveItemFromSquare(object)
	square:AddSpecialObject(javaObject, index)
	javaObject:transmitCompleteItemToClients()
end

local function Empty(object)
	ReplaceExistingObject(object, ZombRand(0, 10))
end

local function Full(object)
	ReplaceExistingObject(object, ZombRand(10, 100))
end

local PRIORITY = 5

MapObjects.OnNewWithSprite("camping_01_19", Empty, PRIORITY)
MapObjects.OnNewWithSprite("camping_01_20", Full, PRIORITY)

