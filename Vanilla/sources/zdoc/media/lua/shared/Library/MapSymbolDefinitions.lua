---@class MapSymbolDefinitions : zombie.worldMap.symbols.MapSymbolDefinitions
---@field private instance MapSymbolDefinitions
---@field private m_symbolList ArrayList|Unknown
---@field private m_symbolByID HashMap|Unknown|Unknown
MapSymbolDefinitions = {}

---@public
---@return int
function MapSymbolDefinitions:getSymbolCount() end

---@public
---@param arg0 String
---@param arg1 String
---@return void
---@overload fun(arg0:String, arg1:String, arg2:int, arg3:int)
function MapSymbolDefinitions:addTexture(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 int
---@param arg3 int
---@return void
function MapSymbolDefinitions:addTexture(arg0, arg1, arg2, arg3) end

---@public
---@return void
function MapSymbolDefinitions:Reset() end

---@public
---@param arg0 int
---@return MapSymbolDefinitions.MapSymbolDefinition
function MapSymbolDefinitions:getSymbolByIndex(arg0) end

---@public
---@param arg0 String
---@return MapSymbolDefinitions.MapSymbolDefinition
function MapSymbolDefinitions:getSymbolById(arg0) end

---@public
---@return MapSymbolDefinitions
function MapSymbolDefinitions:getInstance() end
