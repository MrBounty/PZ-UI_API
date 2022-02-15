---@class UIWorldMapV1 : zombie.worldMap.UIWorldMapV1
---@field m_ui UIWorldMap
---@field protected m_worldMap WorldMap
---@field protected m_style WorldMapStyle
---@field protected m_renderer WorldMapRenderer
---@field protected m_markers WorldMapMarkers
---@field protected m_symbols WorldMapSymbols
---@field protected m_markersV1 WorldMapMarkersV1
---@field protected m_styleV1 WorldMapStyleV1
---@field protected m_symbolsV1 WorldMapSymbolsV1
UIWorldMapV1 = {}

---@public
---@param arg0 String
---@return void
function UIWorldMapV1:addData(arg0) end

---@public
---@return int
function UIWorldMapV1:getMinXInSquares() end

---@public
---@return void
function UIWorldMapV1:setBoundsFromWorld() end

---@public
---@param arg0 float
---@param arg1 float
---@return float
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float, arg4:float, arg5:Matrix4f, arg6:Matrix4f)
function UIWorldMapV1:worldToUIY(arg0, arg1) end

---@protected
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 Matrix4f
---@param arg6 Matrix4f
---@return float
function UIWorldMapV1:worldToUIY(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@return int
function UIWorldMapV1:getImagesCount() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return void
function UIWorldMapV1:setBoundsInSquares(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return void
function UIWorldMapV1:setBoolean(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@return void
function UIWorldMapV1:centerOn(arg0, arg1) end

---@public
---@return int
function UIWorldMapV1:getHeightInCells() end

---@public
---@return float
function UIWorldMapV1:getCenterWorldX() end

---@public
---@param arg0 int
---@return String
function UIWorldMapV1:getDataFileByIndex(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@return float
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float, arg4:float, arg5:Matrix4f, arg6:Matrix4f)
function UIWorldMapV1:worldToUIX(arg0, arg1) end

---@protected
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 Matrix4f
---@param arg6 Matrix4f
---@return float
function UIWorldMapV1:worldToUIX(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@return int
function UIWorldMapV1:getMaxXInCells() end

---@public
---@return int
function UIWorldMapV1:getMaxXInSquares() end

---@public
---@return float
function UIWorldMapV1:worldOriginX() end

---@public
---@param arg0 String
---@return void
function UIWorldMapV1:addImages(arg0) end

---@public
---@return int
function UIWorldMapV1:getMinXInCells() end

---@public
---@return float
function UIWorldMapV1:getCenterWorldY() end

---@public
---@return int
function UIWorldMapV1:getWidthInCells() end

---@protected
---@param arg0 float
---@param arg1 float
---@return float
function UIWorldMapV1:worldOriginUIY(arg0, arg1) end

---@public
---@return int
function UIWorldMapV1:getMaxYInCells() end

---@public
---@param arg0 String
---@param arg1 double
---@return void
function UIWorldMapV1:setDouble(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 double
---@return double
function UIWorldMapV1:getDouble(arg0, arg1) end

---@public
---@return void
function UIWorldMapV1:resetView() end

---@protected
---@param arg0 float
---@param arg1 float
---@return float
function UIWorldMapV1:worldOriginUIX(arg0, arg1) end

---@public
---@return float
function UIWorldMapV1:getBaseZoom() end

---@public
---@param arg0 String
---@return boolean
function UIWorldMapV1:getBoolean(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function UIWorldMapV1:setBackgroundRGBA(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@return ConfigOption
function UIWorldMapV1:getOptionByIndex(arg0) end

---@public
---@return int
function UIWorldMapV1:getMinYInSquares() end

---@public
---@return int
function UIWorldMapV1:getOptionCount() end

---@public
---@param arg0 float
---@param arg1 float
---@return float
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float, arg4:float)
function UIWorldMapV1:uiToWorldX(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@return float
function UIWorldMapV1:uiToWorldX(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function UIWorldMapV1:setUnvisitedRGBA(arg0, arg1, arg2, arg3) end

---@public
---@return WorldMapMarkersV1
function UIWorldMapV1:getMarkersAPI() end

---@public
---@return float
---@overload fun(arg0:float)
function UIWorldMapV1:getWorldScale() end

---@protected
---@param arg0 float
---@return float
function UIWorldMapV1:getWorldScale(arg0) end

---@public
---@param arg0 float
---@return void
function UIWorldMapV1:setZoom(arg0) end

---@public
---@param arg0 MapItem
---@return void
function UIWorldMapV1:setMapItem(arg0) end

---@public
---@return int
function UIWorldMapV1:getMinYInCells() end

---@public
---@return int
function UIWorldMapV1:getWidthInSquares() end

---@public
---@param arg0 float
---@param arg1 float
---@return float
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float, arg4:float)
function UIWorldMapV1:uiToWorldY(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@return float
function UIWorldMapV1:uiToWorldY(arg0, arg1, arg2, arg3, arg4) end

---@public
---@return int
function UIWorldMapV1:getMaxYInSquares() end

---@public
---@param arg0 int
---@return void
function UIWorldMapV1:setDropShadowWidth(arg0) end

---@public
---@return WorldMapSymbolsV1
function UIWorldMapV1:getSymbolsAPI() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return void
function UIWorldMapV1:zoomAt(arg0, arg1, arg2) end

---@public
---@return void
function UIWorldMapV1:endDirectoryData() end

---@public
---@return float
function UIWorldMapV1:worldOriginY() end

---@public
---@return float
function UIWorldMapV1:mouseToWorldX() end

---@public
---@return WorldMapStyle
function UIWorldMapV1:getStyle() end

---@public
---@return int
function UIWorldMapV1:getHeightInSquares() end

---@public
---@return int
function UIWorldMapV1:getDataCount() end

---@public
---@return WorldMapRenderer
function UIWorldMapV1:getRenderer() end

---@public
---@return WorldMapMarkers
function UIWorldMapV1:getMarkers() end

---@public
---@return void
function UIWorldMapV1:clearData() end

---@public
---@return float
function UIWorldMapV1:mouseToWorldY() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function UIWorldMapV1:setUnvisitedGridRGBA(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 float
---@param arg1 float
---@return void
function UIWorldMapV1:moveView(arg0, arg1) end

---@public
---@return WorldMapStyleV1
function UIWorldMapV1:getStyleAPI() end

---@public
---@return float
function UIWorldMapV1:getZoomF() end

---@public
---@return void
function UIWorldMapV1:setBoundsFromData() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return void
function UIWorldMapV1:setBoundsInCells(arg0, arg1, arg2, arg3) end

---@protected
---@return float
function UIWorldMapV1:zoomMult() end
