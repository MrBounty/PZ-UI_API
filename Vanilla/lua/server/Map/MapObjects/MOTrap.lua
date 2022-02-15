--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

require "Traps/TrapDefinition"
require "Traps/STrapSystem"

local function noise(message) STrapSystem.instance:noise(message) end

local function getTrapSprites(trapDef)
	local sprites = {}
	table.insert(sprites, trapDef.sprite)
	if trapDef.closedSprite then table.insert(sprites, trapDef.closedSprite) end
	if trapDef.northSprite then table.insert(sprites, trapDef.northSprite) end
	if trapDef.northClosedSprite then table.insert(sprites, trapDef.northClosedSprite) end
	return sprites
end

local function getTrapDefForSprite(spriteName)
	if not spriteName then return nil end
	for _,trapDef in pairs(Traps) do
		if trapDef.sprite == spriteName or
				trapDef.closedSprite == spriteName then
			return trapDef,false
		end
		if trapDef.northSprite == spriteName or
				trapDef.northClosedSprite == spriteName then
			return trapDef,true
		end
	end
	return nil,false
end

local function removeExistingLuaObject(square)
	local luaObject = STrapSystem.instance:getLuaObjectOnSquare(square)
	if luaObject then
		noise('removing luaObject at same location as newly-loaded isoObject')
		STrapSystem.instance:removeLuaObject(luaObject)
	end
end

local function CreateTrap(sq, spriteName)
	local modData = {}
	
	local cell = getWorld():getCell()
	local north = false
	local javaObject = IsoThumpable.new(cell, sq, spriteName, north, modData)

	javaObject:setCanPassThrough(false)
	javaObject:setCanBarricade(false)
	javaObject:setThumpDmg(1)
	javaObject:setIsContainer(false)
	javaObject:setIsDoor(false)
	javaObject:setIsDoorFrame(false)
	javaObject:setCrossSpeed(1.0)
	javaObject:setBlockAllTheSquare(true)
	javaObject:setName("Trap")
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

local function initObjectModData(isoObject, trapDef, north)
	local modData = isoObject:getModData()
	local square = isoObject:getSquare()
	modData.trapType = trapDef.type
	-- TODO: Random bait
	modData.trapBait = ""
	modData.trapBaitDay = 0
	modData.lastUpdate = 0
	modData.baitAmountMulti = 0
	-- TODO: Random animal (for closed traps only)
	modData.animal = {}
	modData.animalHour = 0
	modData.openSprite = north and trapDef.northSprite or trapDef.sprite
	modData.closedSprite = north and trapDef.northClosedSprite or trapDef.closedSprite
	modData.zone = square:getZone() and square:getZone():getType() or "TownZone"
	modData.player = "unknown"
	 -- TODO: Random player skill
	modData.trappingSkill = 5
	modData.destroyed = false;
end

local function NewTrap(isoObject)
	local spriteName = isoObject:getSprite():getName()
	local trapDef,north = getTrapDefForSprite(spriteName)
	if not trapDef then return end

	local sq = isoObject:getSquare()
	removeExistingLuaObject(sq)
	
	local javaObject = CreateTrap(sq, isoObject:getSprite():getName())
	local index = isoObject:getObjectIndex()
	sq:transmitRemoveItemFromSquare(isoObject)
	sq:AddSpecialObject(javaObject, index)
	
	initObjectModData(javaObject, trapDef, north)

	javaObject:transmitCompleteItemToClients()
	return javaObject
end

local function LoadTrap(isoObject)
	local spriteName = isoObject:getSprite():getName()
	local trapDef = getTrapDefForSprite(spriteName)
	if not trapDef then return end
	if not instanceof(isoObject, "IsoThumpable") then
		isoObject = NewTrap(isoObject)
	end
	STrapSystem.instance:loadIsoObject(isoObject)
end

local PRIORITY = 5

for _,trapDef in pairs(Traps) do
	local sprites = getTrapSprites(trapDef)
	MapObjects.OnNewWithSprite(sprites, NewTrap, PRIORITY)
	MapObjects.OnLoadWithSprite(sprites, LoadTrap, PRIORITY)
end

