--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "Map/CGlobalObjectSystem"

---@class CTrapSystem : CGlobalObjectSystem
CTrapSystem = CGlobalObjectSystem:derive("CTrapSystem")

function CTrapSystem:new()
	local o = CGlobalObjectSystem.new(self, "trap")
	return o
end

function CTrapSystem:isValidIsoObject(isoObject)
	return instanceof(isoObject, "IsoThumpable") and isoObject:getName() == "Trap"
end

function CTrapSystem:newLuaObject(globalObject)
	return CTrapGlobalObject:new(self, globalObject)
end

function CTrapSystem.initObjectModData(isoObject, trapDef, north, player)
	local modData = isoObject:getModData()
	local square = isoObject:getSquare()
	modData.trapType = trapDef.type
	modData.trapBait = ""
	modData.trapBaitDay = 0
	modData.lastUpdate = 0
	modData.baitAmountMulti = 0
	modData.animal = {}
	modData.animalHour = 0
	modData.openSprite = north and trapDef.northSprite or trapDef.sprite
	modData.closedSprite = north and trapDef.northClosedSprite or trapDef.closedSprite
	modData.zone = square:getZone() and square:getZone():getType() or "TownZone"
	if player then
		modData.player = player:getUsername()
		modData.trappingSkill = player:getPerkLevel(Perks.Trapping)
	else
		modData.player = "unknown"
		modData.trappingSkill = 5 -- TODO: Randomize
	end
	modData.destroyed = false;
end

CGlobalObjectSystem.RegisterSystemClass(CTrapSystem)

