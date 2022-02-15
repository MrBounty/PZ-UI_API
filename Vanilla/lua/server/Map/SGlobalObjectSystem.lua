--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

require "ISBaseObject"

SGlobalObjectSystem = ISBaseObject:derive("SGlobalObjectSystem")

function SGlobalObjectSystem:noise(message)
	if self.wantNoise then print(self.systemName..': '..message) end
end

function SGlobalObjectSystem:new(name)
	-- Create the GlobalObjectSystem called NAME and load gos_NAME.bin if it exists.
	local system = SGlobalObjects.registerSystem(name)
	-- NOTE: The table for this Lua object is the same one the SGlobalObjectSystem
	-- Java object created.  The Java class calls some of this Lua object's methods.
	-- At this point, system:getModData() has already been read from disk if the
	-- gos_name.bin file existed.
	local o = system:getModData()
	setmetatable(o, self)
	self.__index = self
	o.system = system
	o.systemName = name
	o.wantNoise = getDebug()
	o:initSystem()
	o:initLuaObjects()
	o:noise('#objects='..system:getObjectCount())
	return o
end

function SGlobalObjectSystem:initSystem()
end

function SGlobalObjectSystem:getInitialStateForClient()
	-- Return a Lua table that is used to initialize the client-side system.
	-- This is called when a client connects in multiplayer, and after
	-- server-side systems are created in singleplayer.
	return nil
end

function SGlobalObjectSystem:getLuaObjectCount()
	return self.system:getObjectCount()
end

function SGlobalObjectSystem:getLuaObjectByIndex(index)
	return self.system:getObjectByIndex(index-1):getModData()
end

function SGlobalObjectSystem:initLuaObjects()
	for i=1,self.system:getObjectCount() do
		local globalObject = self.system:getObjectByIndex(i-1)
		local luaObject = self:newLuaObject(globalObject)
		self:noise('added luaObject '..luaObject.x..','..luaObject.y..','..luaObject.z)
	end
end

function SGlobalObjectSystem:isValidIsoObject(isoObject)
	error "override this method"
end

function SGlobalObjectSystem:getIsoObjectOnSquare(square)
	if not square then return nil end
	for i=1,square:getObjects():size() do
		local isoObject = square:getObjects():get(i-1)
		if self:isValidIsoObject(isoObject) then
			return isoObject
		end
	end
	return nil
end

function SGlobalObjectSystem:getIsoObjectAt(x, y, z)
	local square = getCell():getGridSquare(x, y, z)
	return self:getIsoObjectOnSquare(square)
end

function SGlobalObjectSystem:newLuaObject(globalObject)
	-- Return an object derived from SGlobalObject
	error "override this method"
end

function SGlobalObjectSystem:newLuaObjectAt(x, y, z)
	local globalObject = self.system:newObject(x, y, z)
	local luaObject = self:newLuaObject(globalObject)
	self:newLuaObjectOnClient(luaObject)
	return luaObject
end

function SGlobalObjectSystem:newLuaObjectOnSquare(square)
	return self:newLuaObjectAt(square:getX(), square:getY(), square:getZ())
end

function SGlobalObjectSystem:removeLuaObject(luaObject)
	if not luaObject or (luaObject.luaSystem ~= self) then return end
	self:noise('removing luaObject '..luaObject.x..','..luaObject.y..','..luaObject.z)
	luaObject:aboutToRemoveFromSystem()
	self:removeLuaObjectOnClient(luaObject)
	self.system:removeObject(luaObject.globalObject)
	self:noise('#objects='..self.system:getObjectCount())
end

function SGlobalObjectSystem:removeLuaObjectAt(x, y, z)
	local luaObject = self:getLuaObjectAt(x, y, z)
	self:removeLuaObject(luaObject)
end

function SGlobalObjectSystem:removeLuaObjectOnSquare(square)
	local luaObject = self:getLuaObjectOnSquare(square)
	self:removeLuaObject(luaObject)
end

function SGlobalObjectSystem:newLuaObjectOnClient(luaObject)
	self.system:addGlobalObjectOnClient(luaObject.globalObject)
end

function SGlobalObjectSystem:removeLuaObjectOnClient(luaObject)
	self.system:removeGlobalObjectOnClient(luaObject.globalObject)
end

function SGlobalObjectSystem:getLuaObjectAt(x, y, z)
	local globalObject = self.system:getObjectAt(x, y, z)
	return globalObject and globalObject:getModData() or nil
end

function SGlobalObjectSystem:getLuaObjectOnSquare(square)
	if not square then return nil end
	return self:getLuaObjectAt(square:getX(), square:getY(), square:getZ())
end

function SGlobalObjectSystem:loadIsoObject(isoObject)
	if not isoObject or not isoObject:getSquare() then return end
	if not self:isValidIsoObject(isoObject) then return end
	local square = isoObject:getSquare()
	local luaObject = self:getLuaObjectOnSquare(square)
	if luaObject then
		self:noise('found isoObject with a luaObject '..luaObject.x..','..luaObject.y..','..luaObject.z)
		luaObject:stateToIsoObject(isoObject)
	else
		self:noise('found isoObject without a luaObject '..square:getX()..','..square:getY()..','..square:getZ())
		local globalObject = self.system:newObject(square:getX(), square:getY(), square:getZ())
		local luaObject = self:newLuaObject(globalObject)
		luaObject:stateFromIsoObject(isoObject)
		self:noise('#objects='..self.system:getObjectCount())
		self:newLuaObjectOnClient(luaObject)
	end
end

function SGlobalObjectSystem:sendCommand(command, args)
	self.system:sendCommand(command, args)
end

function SGlobalObjectSystem:OnClientCommand(command, playerObj, args)
	-- CGlobalObjectSystem:sendCommand() arguments are routed to this method
	-- in both singleplayer *and* multiplayer.
end

function SGlobalObjectSystem:OnDestroyIsoThumpable(isoObject, playerObj)
	self:OnObjectAboutToBeRemoved(isoObject)
end

function SGlobalObjectSystem:OnObjectAdded(isoObject)
	if not self:isValidIsoObject(isoObject) then return end
	self:loadIsoObject(isoObject)
end

function SGlobalObjectSystem:OnObjectAboutToBeRemoved(isoObject)
	if not self:isValidIsoObject(isoObject) then return end
	local luaObject = self:getLuaObjectOnSquare(isoObject:getSquare())
	if not luaObject then return end
	self:removeLuaObject(luaObject)
end

function SGlobalObjectSystem:OnIsoObjectChangedItself(isoObject)
	-- A Java object changed it's state. Sync the global object.
	-- For example, after a generator runs out of fuel and shuts itself off.
	if not isoObject or not isoObject:getSquare() then return end
	self:noise('OnIsoObjectChangedItself')
	if not self:isValidIsoObject(isoObject) then return end
	local square = isoObject:getSquare()
	local luaObject = self:getLuaObjectOnSquare(square)
	if luaObject then
		luaObject:OnIsoObjectChangedItself(isoObject)
--		self.system:updateGlobalObjectOnClient(luaObject.globalObject)
	end
end

-- Java calls this method when a chunk with GlobalObjects managed by this system is loaded.
-- This is how GlobalObjects with a missing IsoObject are removed.
-- Instead of using the LoadGridSquare event and checking every location,
-- this event is triggered only for chunks that have GlobalObjects belonging
-- to this particular system.
function SGlobalObjectSystem:OnChunkLoaded(wx, wy)
	local globalObjects = self.system:getObjectsInChunk(wx, wy)
	for i=1,globalObjects:size() do
		local globalObject = globalObjects:get(i-1)
		local square = getCell():getGridSquare(globalObject:getX(), globalObject:getY(), globalObject:getZ())
		local isoObject = self:getIsoObjectOnSquare(square)
		if not isoObject then
			self:noise('found luaObject without an isoObject')
			self:removeLuaObject(globalObject:getModData())
		end
	end
	-- This returns the ArrayList to a pool for reuse.  There's no harm if
	-- you forget to call it.
	self.system:finishedWithList(globalObjects)
end



local function OnDestroyIsoThumpable(luaClass, isoObject, playerObj)
	luaClass.instance:OnDestroyIsoThumpable(isoObject, playerObj)
end

local function OnObjectAdded(luaClass, isoObject)
	luaClass.instance:OnObjectAdded(isoObject)
end

local function OnObjectAboutToBeRemoved(luaClass, isoObject)
	luaClass.instance:OnObjectAboutToBeRemoved(isoObject)
end

local function OnGameBoot(luaClass)
	if not isServerSoftReset() then return end
	luaClass.instance = luaClass:new()
end

local function OnSGlobalObjectSystemInit(luaClass)
	luaClass.instance = luaClass:new()
end

local function OnClientCommand(module, command, player, args)
	if module ~= 'SFarmingSystem' then return end
	if Commands[command] then
		local argStr = ''
		for k,v in pairs(args) do argStr = argStr..' '..k..'='..v end
		noise('OnClientCommand '..module..' '..command..argStr)
		SFarmingSystem.instance:receiveCommand(player, command, args)
	end
end

Events.OnClientCommand.Add(OnClientCommand)

function SGlobalObjectSystem.RegisterSystemClass(luaClass)
	if luaClass == SGlobalObjectSystem then error "replace : with . before RegisterSystemClass" end

	-- This is to support reloading a derived class file in the Lua debugger.
	for i=1,SGlobalObjects.getSystemCount() do
		local system = SGlobalObjects.getSystemByIndex(i-1)
		if system:getModData().Type == luaClass.Type then
			luaClass.instance = system:getModData()
			return
		end
	end
	
	Events.OnDestroyIsoThumpable.Add(function(isoObject, playerObj) OnDestroyIsoThumpable(luaClass, isoObject, playerObj) end)
	Events.OnObjectAdded.Add(function(isoObject) OnObjectAdded(luaClass, isoObject) end)
	Events.OnObjectAboutToBeRemoved.Add(function(isoObject) OnObjectAboutToBeRemoved(luaClass, isoObject) end)
	Events.OnGameBoot.Add(function() OnGameBoot(luaClass) end)
	Events.OnSGlobalObjectSystemInit.Add(function() OnSGlobalObjectSystemInit(luaClass) end)
end

