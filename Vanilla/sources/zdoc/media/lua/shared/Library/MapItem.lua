---@class MapItem : zombie.inventory.types.MapItem
---@field public WORLD_MAP_INSTANCE MapItem
---@field private FILE_MAGIC byte[]
---@field private m_mapID String
---@field private m_symbols WorldMapSymbols
MapItem = {}

---@public
---@return void
function MapItem:SaveWorldMap() end

---@public
---@return String
function MapItem:getMapID() end

---@public
---@return WorldMapSymbols
function MapItem:getSymbols() end

---@public
---@return int
function MapItem:getSaveType() end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function MapItem:save(arg0, arg1) end

---@public
---@return void
function MapItem:LoadWorldMap() end

---@public
---@param arg0 String
---@return void
function MapItem:setMapID(arg0) end

---@public
---@return boolean
function MapItem:IsMap() end

---@public
---@return MapItem
function MapItem:getSingleton() end

---@public
---@return void
function MapItem:Reset() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function MapItem:load(arg0, arg1) end
