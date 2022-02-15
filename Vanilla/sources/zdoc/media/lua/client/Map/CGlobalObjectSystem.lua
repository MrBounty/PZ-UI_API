--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISBaseObject"

---@class CGlobalObjectSystem : ISBaseObject
CGlobalObjectSystem = ISBaseObject:derive("CGlobalObjectSystem")

function CGlobalObjectSystem:noise(message)
	if self.wantNoise then print('C ' .. self.systemName..': '..message) end
end

function CGlobalObjectSystem:new(name)
	local system = CGlobalObjects.registerSystem(name)
	-- NOTE: The table for this Lua object is the same one the CGlobalObjectSystem
	-- Java object created.  The Java class calls some of this Lua object's methods.
	local o = system:getModData()
	setmetatable(o, self)
	self.__index = self
	o.system = system
	o.systemName = name
	o.wantNoise = getDebug()
	o:initSystem()
	o:initLuaObjects()
	return o
end

function CGlobalObjectSystem:initSystem()
end

function CGlobalObjectSystem:initLuaObjects()
	for i=1,self.system:getObjectCount() do
		local globalObject = self.system:getObjectByIndex(i-1)
		local luaObject = self:newLuaObject(globalObject)
		self:noise('added luaObject '..luaObject.x..','..luaObject.y..','..luaObject.z)
	end
end

function CGlobalObjectSystem:getLuaObjectCount()
	return self.system:getObjectCount()
end

function CGlobalObjectSystem:getLuaObjectByIndex(index)
	return self.system:getObjectByIndex(index-1):getModData()
end

function CGlobalObjectSystem:isValidIsoObject(isoObject)
	error "override this method"
end

function CGlobalObjectSystem:getIsoObjectOnSquare(square)
	if not square then return nil end
	for i=1,square:getObjects():size() do
		local isoObject = square:getObjects():get(i-1)
		if self:isValidIsoObject(isoObject) then
			return isoObject
		end
	end
	return nil
end

function CGlobalObjectSystem:getIsoObjectAt(x, y, z)
	local square = getCell():getGridSquare(x, y, z)
	return self:getIsoObjectOnSquare(square)
end

function CGlobalObjectSystem:newLuaObject(globalObject)
	-- Return an object derived from CGlobalObject
	error "override this method"
end

function CGlobalObjectSystem:newLuaObjectAt(x, y, z)
	local globalObject = self.system:newObject(x, y, z)
	return self:newLuaObject(globalObject)
end

function CGlobalObjectSystem:removeLuaObject(luaObject)
	if not luaObject or (luaObject.luaSystem ~= self) then return end
	self:noise('removing luaObject '..luaObject.x..','..luaObject.y..','..luaObject.z)
	self.system:removeObject(luaObject.globalObject)
	self:noise('#objects='..self.system:getObjectCount())
end

function CGlobalObjectSystem:removeLuaObjectAt(x, y, z)
	local luaObject = self:getLuaObjectAt(x, y, z)
	self:removeLuaObject(luaObject)
end

function CGlobalObjectSystem:getLuaObjectAt(x, y, z)
	local globalObject = self.system:getObjectAt(x, y, z)
-- This used to be done in CGlobalObject:new()
if globalObject then
	local luaObject = globalObject:getModData()
	luaObject:updateFromIsoObject()
end
	return globalObject and globalObject:getModData() or nil
end

function CGlobalObjectSystem:getLuaObjectOnSquare(square)
	if not square then return nil end
	return self:getLuaObjectAt(square:getX(), square:getY(), square:getZ())
end

function CGlobalObjectSystem:OnLuaObjectUpdated(luaObject)
	-- luaObject fields were updated with new values from the server
	self:noise('OnLuaObjectUpdated')
end

function CGlobalObjectSystem:sendCommand(playerObj, command, args)
	self.system:sendCommand(command, playerObj, args)
end

function CGlobalObjectSystem:OnServerCommand(command, args)
	-- SGlobalObjectSystem:sendCommand() arguments are routed to this method
	-- in both singleplayer *and* multiplayer.
	if "xxx" == command then
	else
		error("unknown server command '"..command.."'")
	end
end

local function OnCGlobalObjectSystemInit(luaClass)
	luaClass.instance = luaClass:new()
end

function CGlobalObjectSystem.RegisterSystemClass(luaClass)
	if luaClass == CGlobalObjectSystem then error "replace : with . before RegisterSystemClass" end

	-- This is to support reloading a derived class file in the Lua debugger.
	for i=1,CGlobalObjects.getSystemCount() do
		local system = CGlobalObjects.getSystemByIndex(i-1)
		if system:getModData().Type == luaClass.Type then
			luaClass.instance = system:getModData()
			return
		end
	end

	Events.OnCGlobalObjectSystemInit.Add(function() OnCGlobalObjectSystemInit(luaClass) end)
end

