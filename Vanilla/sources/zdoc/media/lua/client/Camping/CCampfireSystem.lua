--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "Map/CGlobalObjectSystem"

---@class CCampfireSystem : CGlobalObjectSystem
CCampfireSystem = CGlobalObjectSystem:derive("CCampfireSystem")

function CCampfireSystem:new()
	local o = CGlobalObjectSystem.new(self, "campfire")
	return o
end

function CCampfireSystem:isValidIsoObject(isoObject)
	return instanceof(isoObject, "IsoObject") and isoObject:getName() == "Campfire"
end

function CCampfireSystem:newLuaObject(globalObject)
	return CCampfireGlobalObject:new(self, globalObject)
end

CGlobalObjectSystem.RegisterSystemClass(CCampfireSystem)

