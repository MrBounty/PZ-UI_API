--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "Map/CGlobalObject"

---@class CPlantGlobalObject : CGlobalObject
CPlantGlobalObject = CGlobalObject:derive("CPlantGlobalObject")

function CPlantGlobalObject:new(luaSystem, globalObject)
	local o = CGlobalObject.new(self, luaSystem, globalObject)
	return o
end

function CPlantGlobalObject:getObject()
	return self:getIsoObject()
end

function CPlantGlobalObject:isAlive()
	return self.state ~= "destroy" and self.state ~= "dry" and self.state ~= "rotten"
end

function CPlantGlobalObject:canHarvest()
	return self:isAlive() and self.hasVegetable
end

