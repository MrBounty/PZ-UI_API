---@class RadarPanel : zombie.ui.RadarPanel
---@field private playerIndex int
---@field private xPos float
---@field private yPos float
---@field private offx float
---@field private offy float
---@field private zoom float
---@field private draww float
---@field private drawh float
---@field private mask Texture
---@field private border Texture
---@field private zombiePos ArrayList|Unknown
---@field private zombiePosPool RadarPanel.ZombiePosPool
---@field private zombiePosFrameCount int
---@field private zombiePosOccupied boolean[]
RadarPanel = {}

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@return void
function RadarPanel:renderRect(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@private
---@return void
function RadarPanel:renderZombies() end

---@public
---@return void
function RadarPanel:update() end

---@private
---@return void
function RadarPanel:renderBuildings() end

---@public
---@return void
function RadarPanel:render() end

---@private
---@return void
function RadarPanel:stencilOn() end

---@private
---@param arg0 float
---@return float
function RadarPanel:worldToScreenY(arg0) end

---@private
---@return void
function RadarPanel:stencilOff() end

---@private
---@param arg0 float
---@return float
function RadarPanel:worldToScreenX(arg0) end
