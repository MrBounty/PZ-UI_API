---@class MapObjects : zombie.Lua.MapObjects
---@field private onNew HashMap|Unknown|Unknown
---@field private onLoad HashMap|Unknown|Unknown
---@field private tempObjects ArrayList|Unknown
---@field private params Object[]
MapObjects = {}

---@private
---@param arg0 String
---@return MapObjects.Callback
function MapObjects:getOnLoad(arg0) end

---@public
---@param arg0 KahluaTable
---@param arg1 LuaClosure
---@param arg2 int
---@return void
---@overload fun(arg0:String, arg1:LuaClosure, arg2:int)
function MapObjects:OnNewWithSprite(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 int
---@return void
function MapObjects:OnNewWithSprite(arg0, arg1, arg2) end

---@public
---@param arg0 KahluaTable
---@param arg1 LuaClosure
---@param arg2 int
---@return void
---@overload fun(arg0:String, arg1:LuaClosure, arg2:int)
function MapObjects:OnLoadWithSprite(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 LuaClosure
---@param arg2 int
---@return void
function MapObjects:OnLoadWithSprite(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function MapObjects:debugLoadSquare(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function MapObjects:debugNewSquare(arg0, arg1, arg2) end

---@public
---@param arg0 IsoGridSquare
---@return void
function MapObjects:newGridSquare(arg0) end

---@public
---@param arg0 IsoGridSquare
---@return void
function MapObjects:loadGridSquare(arg0) end

---@public
---@param arg0 Prototype
---@param arg1 LuaClosure
---@return void
function MapObjects:reroute(arg0, arg1) end

---@public
---@return void
function MapObjects:Reset() end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function MapObjects:debugLoadChunk(arg0, arg1) end

---@private
---@param arg0 String
---@return MapObjects.Callback
function MapObjects:getOnNew(arg0) end
