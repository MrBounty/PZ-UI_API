--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "Map/CGlobalObject"

---@class CMetalDrumGlobalObject : CGlobalObject
CMetalDrumGlobalObject = CGlobalObject:derive("CMetalDrumGlobalObject")

function CMetalDrumGlobalObject:new(luaSystem, globalObject)
	local o = CGlobalObject.new(self, luaSystem, globalObject)
	return o
end

function CMetalDrumGlobalObject:getObject()
	return self:getIsoObject()
end

