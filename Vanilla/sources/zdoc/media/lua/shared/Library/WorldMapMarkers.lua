---@class WorldMapMarkers : zombie.worldMap.markers.WorldMapMarkers
---@field private s_gridSquareMarkerPool Pool|Unknown
---@field private m_markers ArrayList|Unknown
WorldMapMarkers = {}

---@public
---@return void
function WorldMapMarkers:clear() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@return WorldMapGridSquareMarker
function WorldMapMarkers:addGridSquareMarker(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 UIWorldMap
---@return void
function WorldMapMarkers:render(arg0) end

---@public
---@param arg0 WorldMapMarker
---@return void
function WorldMapMarkers:removeMarker(arg0) end
