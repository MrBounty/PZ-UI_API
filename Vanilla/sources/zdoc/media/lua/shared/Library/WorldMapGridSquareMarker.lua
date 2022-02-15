---@class WorldMapGridSquareMarker : zombie.worldMap.markers.WorldMapGridSquareMarker
---@field m_texture1 Texture
---@field m_texture2 Texture
---@field m_r float
---@field m_g float
---@field m_b float
---@field m_a float
---@field m_worldX int
---@field m_worldY int
---@field m_radius int
---@field m_minScreenRadius int
---@field m_blink boolean
WorldMapGridSquareMarker = {}

---@public
---@param arg0 int
---@return void
function WorldMapGridSquareMarker:setMinScreenRadius(arg0) end

---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@return WorldMapGridSquareMarker
function WorldMapGridSquareMarker:init(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@param arg0 UIWorldMap
---@return void
function WorldMapGridSquareMarker:render(arg0) end

---@public
---@param arg0 boolean
---@return void
function WorldMapGridSquareMarker:setBlink(arg0) end
