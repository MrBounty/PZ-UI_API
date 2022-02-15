---@class LuaManager.GlobalObject.LuaFileWriter : zombie.Lua.LuaManager.GlobalObject.LuaFileWriter
---@field private writer PrintWriter
LuaManager_GlobalObject_LuaFileWriter = {}

---throws java.io.IOException
---@public
---@param str String
---@return void
function LuaManager_GlobalObject_LuaFileWriter:write(str) end

---@public
---@param arg0 String
---@return void
function LuaManager_GlobalObject_LuaFileWriter:writeln(arg0) end

---throws java.io.IOException
---@public
---@return void
function LuaManager_GlobalObject_LuaFileWriter:close() end
