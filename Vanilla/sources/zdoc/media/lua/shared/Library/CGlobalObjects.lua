---@class CGlobalObjects : zombie.globalObjects.CGlobalObjects
---@field protected systems ArrayList|Unknown
---@field protected initialState HashMap|Unknown|Unknown
CGlobalObjects = {}

---@public
---@param arg0 String
---@return void
function CGlobalObjects:noise(arg0) end

---@public
---@param arg0 String
---@return JCGlobalObjectSystem
function CGlobalObjects:newSystem(arg0) end

---@public
---@param arg0 String
---@return JCGlobalObjectSystem
function CGlobalObjects:registerSystem(arg0) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 KahluaTable
---@return boolean
function CGlobalObjects:receiveServerCommand(arg0, arg1, arg2) end

---@public
---@param arg0 ByteBuffer
---@return void
function CGlobalObjects:loadInitialState(arg0) end

---@public
---@return int
function CGlobalObjects:getSystemCount() end

---@public
---@param arg0 String
---@return JCGlobalObjectSystem
function CGlobalObjects:getSystemByName(arg0) end

---@public
---@return void
function CGlobalObjects:initSystems() end

---@public
---@return void
function CGlobalObjects:Reset() end

---@public
---@param arg0 int
---@return JCGlobalObjectSystem
function CGlobalObjects:getSystemByIndex(arg0) end
