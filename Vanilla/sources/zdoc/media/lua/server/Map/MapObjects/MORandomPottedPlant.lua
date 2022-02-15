--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

local RANDOM_SPRITES = {
	"vegetation_indoor_01_7",
	"vegetation_indoor_01_8",
	"vegetation_indoor_01_9",
	"vegetation_indoor_01_10",
	"vegetation_indoor_01_11",
	"vegetation_indoor_01_12"
}

local function NewObject(isoObject)
	local index = ZombRand(#RANDOM_SPRITES) + 1
	local spriteName = RANDOM_SPRITES[index]
	isoObject:setSpriteFromName(spriteName)
	isoObject:transmitUpdatedSpriteToClients()
end

local PRIORITY = 5

MapObjects.OnNewWithSprite("vegetation_indoor_01_7", NewObject, PRIORITY)

