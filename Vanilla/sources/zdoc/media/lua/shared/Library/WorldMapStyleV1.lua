---@class WorldMapStyleV1 : zombie.worldMap.styles.WorldMapStyleV1
---@field public m_ui UIWorldMap
---@field public m_api UIWorldMapV1
---@field public m_style WorldMapStyle
---@field public m_layers ArrayList|Unknown
WorldMapStyleV1 = {}

---@public
---@param arg0 String
---@return WorldMapStyleV1.WorldMapStyleLayerV1
function WorldMapStyleV1:getLayerByName(arg0) end

---@public
---@return int
function WorldMapStyleV1:getLayerCount() end

---@public
---@param arg0 String
---@return WorldMapStyleV1.WorldMapStyleLayerV1
function WorldMapStyleV1:newTextureLayer(arg0) end

---@public
---@param arg0 LuaManager.Exposer
---@return void
function WorldMapStyleV1:setExposed(arg0) end

---@public
---@param arg0 int
---@return WorldMapStyleV1.WorldMapStyleLayerV1
function WorldMapStyleV1:getLayerByIndex(arg0) end

---@public
---@return void
function WorldMapStyleV1:clear() end

---@public
---@param arg0 String
---@return void
function WorldMapStyleV1:removeLayerById(arg0) end

---@public
---@param arg0 String
---@return WorldMapStyleV1.WorldMapStyleLayerV1
function WorldMapStyleV1:newLineLayer(arg0) end

---@public
---@param arg0 String
---@return int
function WorldMapStyleV1:indexOfLayer(arg0) end

---@public
---@param arg0 int
---@return void
function WorldMapStyleV1:removeLayerByIndex(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function WorldMapStyleV1:moveLayer(arg0, arg1) end

---@public
---@param arg0 String
---@return WorldMapStyleV1.WorldMapStyleLayerV1
function WorldMapStyleV1:newPolygonLayer(arg0) end
