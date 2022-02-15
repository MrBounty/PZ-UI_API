--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

local function noise(message) print('MOTent.lua: '..message) end

local function ReplaceExistingObject(isoObject)
	local square = isoObject:getSquare()
	noise('replacing isoObject at '..square:getX()..','..square:getY()..','..square:getZ())
	local spriteName = isoObject:getSprite():getName()
	camping.removeTent(isoObject)
	camping.addTent(square, spriteName)
end

local function NewTarp(isoObject)
	ReplaceExistingObject(isoObject)
end

local PRIORITY = 5

MapObjects.OnNewWithSprite("camping_01_0", NewTarp, PRIORITY)
MapObjects.OnNewWithSprite("camping_01_3", NewTarp, PRIORITY)

-- -- -- -- --

local function LoadObject(isoObject)
	local square = isoObject:getSquare()
	noise('loaded isoObject at '..square:getX()..','..square:getY()..','..square:getZ())
	if instanceof(isoObject, "IsoThumpable") then
		return
	end
	ReplaceExistingObject(isoObject)
end

local function LoadTarp(isoObject)
	LoadObject(isoObject)
end

MapObjects.OnLoadWithSprite("camping_01_0", LoadTarp, PRIORITY)
MapObjects.OnLoadWithSprite("camping_01_3", LoadTarp, PRIORITY)

