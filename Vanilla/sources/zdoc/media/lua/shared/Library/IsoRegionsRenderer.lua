---@class IsoRegionsRenderer : zombie.iso.areas.isoregion.IsoRegionsRenderer
---@field private tempChunkList List|Unknown
---@field private debugLines List|Unknown
---@field private xPos float
---@field private yPos float
---@field private offx float
---@field private offy float
---@field private zoom float
---@field private draww float
---@field private drawh float
---@field private hasSelected boolean
---@field private validSelection boolean
---@field private selectedX int
---@field private selectedY int
---@field private selectedZ int
---@field private drawnCells HashSet|Unknown
---@field private editSquareInRange boolean
---@field private editSquareX int
---@field private editSquareY int
---@field private editOptions ArrayList|Unknown
---@field private EditingEnabled boolean
---@field private EditWallN IsoRegionsRenderer.BooleanDebugOption
---@field private EditWallW IsoRegionsRenderer.BooleanDebugOption
---@field private EditDoorN IsoRegionsRenderer.BooleanDebugOption
---@field private EditDoorW IsoRegionsRenderer.BooleanDebugOption
---@field private EditFloor IsoRegionsRenderer.BooleanDebugOption
---@field private zLevelOptions ArrayList|Unknown
---@field private zLevelPlayer IsoRegionsRenderer.BooleanDebugOption
---@field private zLevel0 IsoRegionsRenderer.BooleanDebugOption
---@field private zLevel1 IsoRegionsRenderer.BooleanDebugOption
---@field private zLevel2 IsoRegionsRenderer.BooleanDebugOption
---@field private zLevel3 IsoRegionsRenderer.BooleanDebugOption
---@field private zLevel4 IsoRegionsRenderer.BooleanDebugOption
---@field private zLevel5 IsoRegionsRenderer.BooleanDebugOption
---@field private zLevel6 IsoRegionsRenderer.BooleanDebugOption
---@field private zLevel7 IsoRegionsRenderer.BooleanDebugOption
---@field private VERSION int
---@field private options ArrayList|Unknown
---@field private CellGrid IsoRegionsRenderer.BooleanDebugOption
---@field private MetaGridBuildings IsoRegionsRenderer.BooleanDebugOption
---@field private IsoRegionRender IsoRegionsRenderer.BooleanDebugOption
---@field private IsoRegionRenderChunks IsoRegionsRenderer.BooleanDebugOption
---@field private IsoRegionRenderChunksPlus IsoRegionsRenderer.BooleanDebugOption
IsoRegionsRenderer = {}

---@private
---@param arg0 String
---@return void
function IsoRegionsRenderer:debugLine(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 float
---@return void
function IsoRegionsRenderer:renderCellInfo(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 String
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@return void
function IsoRegionsRenderer:renderString(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function IsoRegionsRenderer:setEditSquareCoord(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 String
---@param arg3 Color
---@return void
---@overload fun(arg0:float, arg1:float, arg2:String, arg3:double, arg4:double, arg5:double, arg6:double)
function IsoRegionsRenderer:renderStringUI(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 String
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@return void
function IsoRegionsRenderer:renderStringUI(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 String
---@return ConfigOption
function IsoRegionsRenderer:getZLevelOptionByName(arg0) end

---@public
---@param arg0 float
---@return float
function IsoRegionsRenderer:worldToScreenX(arg0) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return void
function IsoRegionsRenderer:setBoolean(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@return void
function IsoRegionsRenderer:outlineRect(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@return void
function IsoRegionsRenderer:save() end

---@public
---@return int
function IsoRegionsRenderer:getZLevel() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@return void
function IsoRegionsRenderer:renderRect(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@param arg0 int
---@param arg1 boolean
---@return void
function IsoRegionsRenderer:setZLevelOption(arg0, arg1) end

---@public
---@return void
function IsoRegionsRenderer:editRotate() end

---@public
---@return int
function IsoRegionsRenderer:getEditOptionCount() end

---@private
---@param arg0 int
---@param arg1 int
---@return boolean
function IsoRegionsRenderer:editCoordInRange(arg0, arg1) end

---@public
---@param arg0 String
---@return ConfigOption
function IsoRegionsRenderer:getEditOptionByName(arg0) end

---@public
---@return boolean
function IsoRegionsRenderer:isHasSelected() end

---@public
---@return boolean
function IsoRegionsRenderer:isEditingEnabled() end

---@public
---@param arg0 String
---@return boolean
function IsoRegionsRenderer:getBoolean(arg0) end

---@private
---@param arg0 UIElement
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function IsoRegionsRenderer:_render(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@return void
function IsoRegionsRenderer:renderLine(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@return void
function IsoRegionsRenderer:unsetSelected() end

---@public
---@param arg0 int
---@return ConfigOption
function IsoRegionsRenderer:getOptionByIndex(arg0) end

---@public
---@param arg0 int
---@param arg1 boolean
---@return void
function IsoRegionsRenderer:setEditOption(arg0, arg1) end

---@public
---@return int
function IsoRegionsRenderer:getZLevelOptionCount() end

---@public
---@param arg0 int
---@param arg1 int
---@return boolean
function IsoRegionsRenderer:hasChunkRegion(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function IsoRegionsRenderer:setSelected(arg0, arg1) end

---@public
---@return void
function IsoRegionsRenderer:load() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@return void
function IsoRegionsRenderer:renderZombie(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 float
---@return float
function IsoRegionsRenderer:uiToWorldY(arg0) end

---@public
---@return void
function IsoRegionsRenderer:recalcSurroundings() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@return void
function IsoRegionsRenderer:renderSquare(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 int
---@return ConfigOption
function IsoRegionsRenderer:getZLevelOptionByIndex(arg0) end

---@public
---@param arg0 String
---@return ConfigOption
function IsoRegionsRenderer:getOptionByName(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function IsoRegionsRenderer:setSelectedWorld(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@return IsoChunkRegion
function IsoRegionsRenderer:getChunkRegion(arg0, arg1) end

---@public
---@param arg0 float
---@return float
function IsoRegionsRenderer:worldToScreenY(arg0) end

---@public
---@param arg0 UIElement
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function IsoRegionsRenderer:render(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 float
---@return float
function IsoRegionsRenderer:uiToWorldX(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@return void
function IsoRegionsRenderer:renderEntity(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 int
---@return ConfigOption
function IsoRegionsRenderer:getEditOptionByIndex(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function IsoRegionsRenderer:editSquare(arg0, arg1) end

---@public
---@return int
function IsoRegionsRenderer:getOptionCount() end
