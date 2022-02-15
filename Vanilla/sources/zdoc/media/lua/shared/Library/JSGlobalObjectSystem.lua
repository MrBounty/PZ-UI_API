---@class JSGlobalObjectSystem : zombie.globalObjects.SGlobalObjectSystem
---@field private tempTable KahluaTable
---@field protected loadedWorldVersion int
---@field protected modDataKeys HashSet|Unknown
---@field protected objectModDataKeys HashSet|Unknown
---@field protected objectSyncKeys HashSet|Unknown
JSGlobalObjectSystem = {}

---@private
---@return String
function JSGlobalObjectSystem:getFileName() end

---@public
---@param arg0 SGlobalObject
---@return void
function JSGlobalObjectSystem:removeGlobalObjectOnClient(arg0) end

---@public
---@return void
function JSGlobalObjectSystem:Reset() end

---@public
---@param arg0 String
---@param arg1 KahluaTable
---@return void
function JSGlobalObjectSystem:sendCommand(arg0, arg1) end

---@public
---@param arg0 KahluaTable
---@return void
function JSGlobalObjectSystem:setModDataKeys(arg0) end

---@protected
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return GlobalObject
function JSGlobalObjectSystem:makeObject(arg0, arg1, arg2) end

---@public
---@param arg0 KahluaTable
---@return void
function JSGlobalObjectSystem:setObjectModDataKeys(arg0) end

---@public
---@return void
function JSGlobalObjectSystem:update() end

---@public
---@return void
---@overload fun(arg0:ByteBuffer, arg1:int)
function JSGlobalObjectSystem:load() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function JSGlobalObjectSystem:load(arg0, arg1) end

---@public
---@return KahluaTable
function JSGlobalObjectSystem:getInitialStateForClient() end

---@public
---@return int
function JSGlobalObjectSystem:loadedWorldVersion() end

---@public
---@param arg0 SGlobalObject
---@return void
function JSGlobalObjectSystem:addGlobalObjectOnClient(arg0) end

---@public
---@return void
---@overload fun(arg0:ByteBuffer)
function JSGlobalObjectSystem:save() end

---@public
---@param arg0 ByteBuffer
---@return void
function JSGlobalObjectSystem:save(arg0) end

---@public
---@param arg0 KahluaTable
---@return void
function JSGlobalObjectSystem:setObjectSyncKeys(arg0) end

---@public
---@param arg0 IsoObject
---@return void
function JSGlobalObjectSystem:OnIsoObjectChangedItself(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function JSGlobalObjectSystem:chunkLoaded(arg0, arg1) end

---@public
---@param arg0 SGlobalObject
---@return void
function JSGlobalObjectSystem:updateGlobalObjectOnClient(arg0) end

---@public
---@param arg0 String
---@param arg1 IsoPlayer
---@param arg2 KahluaTable
---@return void
function JSGlobalObjectSystem:receiveClientCommand(arg0, arg1, arg2) end
