---@class SGlobalObjects : zombie.globalObjects.SGlobalObjects
---@field protected systems ArrayList|Unknown
SGlobalObjects = {}

---@public
---@param arg0 String
---@return JSGlobalObjectSystem
function SGlobalObjects:registerSystem(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function SGlobalObjects:chunkLoaded(arg0, arg1) end

---@public
---@param arg0 ByteBuffer
---@return void
function SGlobalObjects:saveInitialStateForClient(arg0) end

---@public
---@param arg0 String
---@return void
function SGlobalObjects:noise(arg0) end

---@public
---@param arg0 String
---@return JSGlobalObjectSystem
function SGlobalObjects:newSystem(arg0) end

---@public
---@param arg0 String
---@param arg1 IsoObject
---@return void
function SGlobalObjects:OnIsoObjectChangedItself(arg0, arg1) end

---@public
---@return void
function SGlobalObjects:initSystems() end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 IsoPlayer
---@param arg3 KahluaTable
---@return boolean
function SGlobalObjects:receiveClientCommand(arg0, arg1, arg2, arg3) end

---@public
---@return void
function SGlobalObjects:load() end

---@public
---@return int
function SGlobalObjects:getSystemCount() end

---@public
---@return void
function SGlobalObjects:save() end

---@public
---@param arg0 String
---@return JSGlobalObjectSystem
function SGlobalObjects:getSystemByName(arg0) end

---@public
---@return void
function SGlobalObjects:update() end

---@public
---@param arg0 int
---@return JSGlobalObjectSystem
function SGlobalObjects:getSystemByIndex(arg0) end

---@public
---@return void
function SGlobalObjects:Reset() end
