--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISBaseObject"

---@class CGlobalObject : ISBaseObject
CGlobalObject = ISBaseObject:derive("CGlobalObject")

function CGlobalObject:noise(message)
	self.luaSystem:noise(message)
end

function CGlobalObject:new(luaSystem, globalObject)
	-- NOTE: The table for this object is the *same* one the GlobalObject.class
	-- object created in Java.  Doing it this way means we don't have to worry
	-- about syncing this Lua object's fields with the GlobalObject in Java.
	-- Derived classes should not initialize any fields here that are saved,
	-- because they are already loaded from disk when this method is called.
	-- Override initNew() to initialize a brand-new CGlobalObject.
	local o = globalObject:getModData()
	setmetatable(o, self)
	self.__index = self
	o.luaSystem = luaSystem
	o.globalObject = globalObject
	o.x = globalObject:getX()
	o.y = globalObject:getY()
	o.z = globalObject:getZ()
--	o:fromModData(isoObject:getModData())
	return o
end

function CGlobalObject:getIsoObject()
	if not self.luaSystem then
		return;
	end
	return self.luaSystem:getIsoObjectAt(self.x, self.y, self.z)
end

function CGlobalObject:getSquare()
	return getCell():getGridSquare(self.x, self.y, self.z)
end

function CGlobalObject:fromModData(modData)
	for k,v in pairs(modData) do
		self[k] = v
	end
end

-- TimedAction and UI code should call this repeatedly to keep this luaObject
-- in sync with the isoObject, which may be updated at any time.
function CGlobalObject:updateFromIsoObject()
	local isoObject = self:getIsoObject()
	if isoObject then
		self:fromModData(isoObject:getModData())
	end
end

