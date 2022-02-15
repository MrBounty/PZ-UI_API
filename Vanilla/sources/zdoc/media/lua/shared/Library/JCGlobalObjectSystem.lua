---@class JCGlobalObjectSystem : zombie.globalObjects.CGlobalObjectSystem
JCGlobalObjectSystem = {}

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 KahluaTable
---@return void
function JCGlobalObjectSystem:receiveNewLuaObjectAt(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function JCGlobalObjectSystem:receiveRemoveLuaObjectAt(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 IsoPlayer
---@param arg2 KahluaTable
---@return void
function JCGlobalObjectSystem:sendCommand(arg0, arg1, arg2) end

---@protected
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return GlobalObject
function JCGlobalObjectSystem:makeObject(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 KahluaTable
---@return void
function JCGlobalObjectSystem:receiveUpdateLuaObjectAt(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@param arg1 KahluaTable
---@return void
function JCGlobalObjectSystem:receiveServerCommand(arg0, arg1) end

---@public
---@return void
function JCGlobalObjectSystem:Reset() end
