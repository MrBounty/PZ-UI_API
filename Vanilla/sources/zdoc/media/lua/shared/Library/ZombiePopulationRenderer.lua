---@class ZombiePopulationRenderer : zombie.popman.ZombiePopulationRenderer
---@field private xPos float
---@field private yPos float
---@field private offx float
---@field private offy float
---@field private zoom float
---@field private draww float
---@field private drawh float
---@field private VERSION int
---@field private options ArrayList|Unknown
---@field private CellGrid ZombiePopulationRenderer.BooleanDebugOption
---@field private MetaGridBuildings ZombiePopulationRenderer.BooleanDebugOption
---@field private ZombiesStanding ZombiePopulationRenderer.BooleanDebugOption
---@field private ZombiesMoving ZombiePopulationRenderer.BooleanDebugOption
---@field private MCDObstacles ZombiePopulationRenderer.BooleanDebugOption
---@field private MCDRegularChunkOutlines ZombiePopulationRenderer.BooleanDebugOption
---@field private MCDRooms ZombiePopulationRenderer.BooleanDebugOption
---@field private Vehicles ZombiePopulationRenderer.BooleanDebugOption
ZombiePopulationRenderer = {}

---@private
---@param arg0 int
---@param arg1 int
---@return void
function ZombiePopulationRenderer:n_setWallFollowerStart(arg0, arg1) end

---@public
---@return void
function ZombiePopulationRenderer:save() end

---@public
---@param arg0 String
---@return boolean
function ZombiePopulationRenderer:getBoolean(arg0) end

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
function ZombiePopulationRenderer:outlineRect(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@private
---@param arg0 float
---@param arg1 int
---@param arg2 int
---@param arg3 float
---@param arg4 float
---@param arg5 int
---@param arg6 int
---@return void
function ZombiePopulationRenderer:n_render(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

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
function ZombiePopulationRenderer:renderLine(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@return int
function ZombiePopulationRenderer:getOptionCount() end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function ZombiePopulationRenderer:setWallFollowerStart(arg0, arg1) end

---@public
---@param arg0 UIElement
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function ZombiePopulationRenderer:render(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 String
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@return void
function ZombiePopulationRenderer:renderString(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@private
---@param arg0 String
---@param arg1 String
---@return void
function ZombiePopulationRenderer:n_setDebugOption(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 float
---@return void
function ZombiePopulationRenderer:renderCellInfo(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 float
---@return float
function ZombiePopulationRenderer:worldToScreenX(arg0) end

---@private
---@param arg0 int
---@param arg1 int
---@return void
function ZombiePopulationRenderer:n_setWallFollowerEnd(arg0, arg1) end

---@private
---@param arg0 int
---@param arg1 int
---@return void
function ZombiePopulationRenderer:n_wallFollowerMouseMove(arg0, arg1) end

---@public
---@return void
function ZombiePopulationRenderer:load() end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function ZombiePopulationRenderer:wallFollowerMouseMove(arg0, arg1) end

---@public
---@param arg0 float
---@return float
function ZombiePopulationRenderer:uiToWorldX(arg0) end

---@private
---@param arg0 UIElement
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function ZombiePopulationRenderer:_render(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@return ConfigOption
function ZombiePopulationRenderer:getOptionByName(arg0) end

---@public
---@param arg0 float
---@return float
function ZombiePopulationRenderer:worldToScreenY(arg0) end

---@public
---@param arg0 float
---@return float
function ZombiePopulationRenderer:uiToWorldY(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@return void
function ZombiePopulationRenderer:renderZombie(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 int
---@return ConfigOption
function ZombiePopulationRenderer:getOptionByIndex(arg0) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return void
function ZombiePopulationRenderer:setBoolean(arg0, arg1) end

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
function ZombiePopulationRenderer:renderRect(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function ZombiePopulationRenderer:setWallFollowerEnd(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@return void
function ZombiePopulationRenderer:renderVehicle(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@return void
function ZombiePopulationRenderer:renderCircle(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end
