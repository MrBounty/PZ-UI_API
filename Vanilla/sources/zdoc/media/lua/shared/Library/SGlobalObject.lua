---@class SGlobalObject : zombie.globalObjects.SGlobalObject
---@field private tempTable KahluaTable
SGlobalObject = {}

---@public
---@param arg0 ByteBuffer
---@return void
function SGlobalObject:save(arg0) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function SGlobalObject:load(arg0, arg1) end
