--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

require "Farming/SFarmingSystem"
require "Farming/farming_vegetableconf"

local function noise(message) SFarmingSystem.instance:noise(message) end

local function randomSeedType()
	local seedTypes = {}
	for seedType,itemSprite in pairs(farming_vegetableconf.icons) do
		if seedType ~= "Tomato" then
			table.insert(seedTypes, seedType)
		end
	end
	return seedTypes[ZombRand(#seedTypes)+1]
end

local function removeExistingLuaObject(square)
	local luaObject = SFarmingSystem.instance:getLuaObjectOnSquare(square)
	if luaObject then
		noise('removing luaObject at same location as newly-loaded isoObject')
		SFarmingSystem.instance:removeLuaObject(luaObject)
	end
end

local function NewPlow(isoObject)
	local square = isoObject:getSquare()
	removeExistingLuaObject(square)
	SPlantGlobalObject.initModData(isoObject:getModData())
	isoObject:getModData().exterior = square:isOutside()
	isoObject:setName(getText("Farming_Plowed_Land"))
end

local function NewDestroyed(isoObject)
	local square = isoObject:getSquare()
	removeExistingLuaObject(square)
	local modData = isoObject:getModData()
	SPlantGlobalObject.initModData(modData)
	modData.spriteName = isoObject:getSprite():getName()
	modData.state = "destroy"
	modData.exterior = square:isOutside()
	local typeOfSeed = nil
	if isoObject:getSprite():getName() == "vegetation_farming_01_14" then
		typeOfSeed = "Tomato"
	else
		-- We don't know what type of plant this was, pick a random one.
		typeOfSeed = randomSeedType()
	end
	isoObject:setName(getText("Farming_Destroyed") .. " " .. getText("Farming_" .. typeOfSeed))
	-- deadPlant()
	modData.nextGrowing = 0
	modData.waterLvl = 0
	modData.nbOfGrow = 0
	modData.mildewLvl = 0
	modData.aphidLvl = 0
	modData.fliesLvl = 0
	modData.health = 0
end

local function NewPlant(isoObject, typeOfSeed)
	local square = isoObject:getSquare()
	removeExistingLuaObject(square)

	local spriteName = isoObject:getSprite():getName()
	local luaObject = SFarmingSystem.instance:newLuaObjectOnSquare(square)
	luaObject:initNew()
	luaObject.exterior = square:isOutside()
	isoObject:setSpecialTooltip(true)

	luaObject:seed(typeOfSeed)
	luaObject.waterLvl = ZombRand(luaObject.waterNeeded, luaObject.waterNeededMax or 100)

	-- plow: nbOfGrow == -1
	-- seed: nbOfGrow == 0 -> 1
	-- rotten: nbOfGrow == 1

	local sprites = farming_vegetableconf.sprite[typeOfSeed]
	local nbOfGrow = 1
	for i=1,#sprites do
		if sprites[i] == spriteName then
			break
		end
		nbOfGrow = nbOfGrow + 1
	end

	for i=1,nbOfGrow-1 do
		SFarmingSystem.instance:growPlant(luaObject, nil, true)
		luaObject.waterLvl = ZombRand(luaObject.waterNeeded, luaObject.waterNeededMax or 100)
	end

	isoObject:setName(luaObject.objectName)
	isoObject:setSprite(luaObject.spriteName)
	luaObject:toModData(isoObject:getModData())
	noise('created luaObject from pre-existing isoObject '..luaObject.x..','..luaObject.y)
end

local function NewBroccoli(isoObject)
	NewPlant(isoObject, "Broccoli")
end

local function NewCarrots(isoObject)
	NewPlant(isoObject, "Carrots")
end

local function NewCabbages(isoObject)
	NewPlant(isoObject, "Cabbages")
end

local function NewRadishes(isoObject)
	NewPlant(isoObject, "Radishes")
end

local function NewStrawberry(isoObject)
	NewPlant(isoObject, "Strawberry plant")
end

local function NewPotatoes(isoObject)
	NewPlant(isoObject, "Potatoes")
end

local function NewTomato(isoObject)
	NewPlant(isoObject, "Tomato")
end

local PRIORITY = 5

MapObjects.OnNewWithSprite("vegetation_farming_01_1", NewPlow, PRIORITY)
MapObjects.OnNewWithSprite("vegetation_farming_01_13", NewDestroyed, PRIORITY)
MapObjects.OnNewWithSprite("vegetation_farming_01_14", NewDestroyed, PRIORITY)
MapObjects.OnNewWithSprite(farming_vegetableconf.sprite.Broccoli, NewBroccoli, PRIORITY)
MapObjects.OnNewWithSprite(farming_vegetableconf.sprite.Cabbages, NewCabbages, PRIORITY)
MapObjects.OnNewWithSprite(farming_vegetableconf.sprite.Carrots, NewCarrots, PRIORITY)
MapObjects.OnNewWithSprite(farming_vegetableconf.sprite.Potatoes, NewPotatoes, PRIORITY)
MapObjects.OnNewWithSprite(farming_vegetableconf.sprite.Radishes, NewRadishes, PRIORITY)
MapObjects.OnNewWithSprite(farming_vegetableconf.sprite["Strawberry plant"], NewStrawberry, PRIORITY)
MapObjects.OnNewWithSprite(farming_vegetableconf.sprite.Tomato, NewTomato, PRIORITY)

-- -- -- -- --

local function LoadPlow(isoObject)
	if not SFarmingSystem.instance:isValidIsoObject(isoObject) then
		noise("couldn't find valid modData for existing isoObject; recreating it")
		NewPlow(isoObject)
	end
	SFarmingSystem.instance:loadIsoObject(isoObject)
end

local function LoadDestroyed(isoObject)
	if not SFarmingSystem.instance:isValidIsoObject(isoObject) then
		noise("couldn't find valid modData for existing isoObject; recreating it")
		NewDestroyed(isoObject)
	end
	SFarmingSystem.instance:loadIsoObject(isoObject)
end

local function LoadPlant(isoObject, typeOfSeed)
	if not SFarmingSystem.instance:isValidIsoObject(isoObject) then
		noise("couldn't find valid modData for existing isoObject; recreating it")
		NewPlant(isoObject, typeOfSeed)
	end
	SFarmingSystem.instance:loadIsoObject(isoObject)
end

local function LoadBroccoli(isoObject)
	LoadPlant(isoObject, "Broccoli")
end

local function LoadCarrots(isoObject)
	LoadPlant(isoObject, "Carrots")
end

local function LoadCabbages(isoObject)
	LoadPlant(isoObject, "Cabbages")
end

local function LoadRadishes(isoObject)
	LoadPlant(isoObject, "Radishes")
end

local function LoadStrawberry(isoObject)
	LoadPlant(isoObject, "Strawberry plant")
end

local function LoadPotatoes(isoObject)
	LoadPlant(isoObject, "Potatoes")
end

local function LoadTomato(isoObject)
	LoadPlant(isoObject, "Tomato")
end

MapObjects.OnLoadWithSprite("vegetation_farming_01_1", LoadPlow, PRIORITY)
MapObjects.OnLoadWithSprite("vegetation_farming_01_13", LoadDestroyed, PRIORITY)
MapObjects.OnLoadWithSprite("vegetation_farming_01_14", LoadDestroyed, PRIORITY)
MapObjects.OnLoadWithSprite(farming_vegetableconf.sprite.Broccoli, LoadBroccoli, PRIORITY)
MapObjects.OnLoadWithSprite(farming_vegetableconf.sprite.Cabbages, LoadCabbages, PRIORITY)
MapObjects.OnLoadWithSprite(farming_vegetableconf.sprite.Carrots, LoadCarrots, PRIORITY)
MapObjects.OnLoadWithSprite(farming_vegetableconf.sprite.Potatoes, LoadPotatoes, PRIORITY)
MapObjects.OnLoadWithSprite(farming_vegetableconf.sprite.Radishes, LoadRadishes, PRIORITY)
MapObjects.OnLoadWithSprite(farming_vegetableconf.sprite["Strawberry plant"], LoadStrawberry, PRIORITY)
MapObjects.OnLoadWithSprite(farming_vegetableconf.sprite.Tomato, LoadTomato, PRIORITY)

