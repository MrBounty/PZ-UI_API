---@class WorldMapMarkersV1 : zombie.worldMap.markers.WorldMapMarkersV1
---@field private m_ui UIWorldMap
---@field private m_markers ArrayList|Unknown
WorldMapMarkersV1 = {}

---@public
---@return void
function WorldMapMarkersV1:clear() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@return WorldMapMarkersV1.WorldMapGridSquareMarkerV1
function WorldMapMarkersV1:addGridSquareMarker(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 LuaManager.Exposer
---@return void
function WorldMapMarkersV1:setExposed(arg0) end

---@public
---@param arg0 WorldMapMarkersV1.WorldMapMarkerV1
---@return void
function WorldMapMarkersV1:removeMarker(arg0) end
