---@class WorldMarkers.PlayerHomingPoint : zombie.iso.WorldMarkers.PlayerHomingPoint
---@field private ID int
---@field private texture Texture
---@field private x int
---@field private y int
---@field private r float
---@field private g float
---@field private b float
---@field private a float
---@field private angle float
---@field private targetAngle float
---@field private customTargetAngle boolean
---@field private angleLerpVal float
---@field private movementLerpVal float
---@field private dist int
---@field private targRenderX float
---@field private targRenderY float
---@field private renderX float
---@field private renderY float
---@field private renderOffsetX float
---@field private renderOffsetY float
---@field private renderWidth float
---@field private renderHeight float
---@field private renderSizeMod float
---@field private targetScreenX float
---@field private targetScreenY float
---@field private targetOnScreen boolean
---@field private stickToCharDist float
---@field private active boolean
---@field private homeOnTargetInView boolean
---@field private homeOnTargetDist int
---@field private homeOnOffsetX float
---@field private homeOnOffsetY float
---@field private isRemoved boolean
WorldMarkers_PlayerHomingPoint = {}

---@public
---@return void
function WorldMarkers_PlayerHomingPoint:setHighCounter() end

---@public
---@param arg0 float
---@return void
function WorldMarkers_PlayerHomingPoint:setTargetAngle(arg0) end

---@public
---@param arg0 float
---@return void
function WorldMarkers_PlayerHomingPoint:setRenderHeight(arg0) end

---@public
---@return float
function WorldMarkers_PlayerHomingPoint:getTargetAngle() end

---@public
---@param arg0 float
---@return void
function WorldMarkers_PlayerHomingPoint:setA(arg0) end

---@public
---@return int
function WorldMarkers_PlayerHomingPoint:getID() end

---@public
---@return float
function WorldMarkers_PlayerHomingPoint:getRenderOffsetX() end

---@public
---@return float
function WorldMarkers_PlayerHomingPoint:getA() end

---@public
---@return int
function WorldMarkers_PlayerHomingPoint:getY() end

---@public
---@param arg0 boolean
---@return void
function WorldMarkers_PlayerHomingPoint:setCustomTargetAngle(arg0) end

---@public
---@return float
function WorldMarkers_PlayerHomingPoint:getMovementLerpVal() end

---@public
---@param arg0 float
---@return void
function WorldMarkers_PlayerHomingPoint:setStickToCharDist(arg0) end

---@public
---@param arg0 float
---@return void
function WorldMarkers_PlayerHomingPoint:setYOffsetScaled(arg0) end

---@public
---@return void
function WorldMarkers_PlayerHomingPoint:setTableSurface() end

---@public
---@return boolean
function WorldMarkers_PlayerHomingPoint:isCustomTargetAngle() end

---@public
---@return float
function WorldMarkers_PlayerHomingPoint:getRenderOffsetY() end

---@public
---@param arg0 float
---@return void
function WorldMarkers_PlayerHomingPoint:setRenderOffsetY(arg0) end

---@public
---@param arg0 float
---@return void
function WorldMarkers_PlayerHomingPoint:setRenderWidth(arg0) end

---@public
---@param arg0 int
---@return void
function WorldMarkers_PlayerHomingPoint:setY(arg0) end

---@public
---@return float
function WorldMarkers_PlayerHomingPoint:getRenderHeight() end

---@public
---@return float
function WorldMarkers_PlayerHomingPoint:getRenderWidth() end

---@public
---@return int
function WorldMarkers_PlayerHomingPoint:getX() end

---@public
---@param arg0 String
---@return void
function WorldMarkers_PlayerHomingPoint:setTexture(arg0) end

---@public
---@param arg0 boolean
---@return void
function WorldMarkers_PlayerHomingPoint:setActive(arg0) end

---@public
---@param arg0 float
---@return void
function WorldMarkers_PlayerHomingPoint:setR(arg0) end

---@public
---@param arg0 float
---@return void
function WorldMarkers_PlayerHomingPoint:setHomeOnOffsetY(arg0) end

---@public
---@return float
function WorldMarkers_PlayerHomingPoint:getAngleLerpVal() end

---@public
---@param arg0 float
---@return void
function WorldMarkers_PlayerHomingPoint:setMovementLerpVal(arg0) end

---@public
---@param arg0 float
---@return void
function WorldMarkers_PlayerHomingPoint:setXOffsetScaled(arg0) end

---@public
---@return void
function WorldMarkers_PlayerHomingPoint:remove() end

---@public
---@return float
function WorldMarkers_PlayerHomingPoint:getHomeOnOffsetY() end

---@public
---@param arg0 int
---@return void
function WorldMarkers_PlayerHomingPoint:setX(arg0) end

---@public
---@return float
function WorldMarkers_PlayerHomingPoint:getR() end

---@public
---@return boolean
function WorldMarkers_PlayerHomingPoint:isRemoved() end

---@public
---@return int
function WorldMarkers_PlayerHomingPoint:getHomeOnTargetDist() end

---@public
---@param arg0 float
---@return void
function WorldMarkers_PlayerHomingPoint:setG(arg0) end

---@public
---@param arg0 float
---@return void
function WorldMarkers_PlayerHomingPoint:setB(arg0) end

---@public
---@param arg0 boolean
---@return void
function WorldMarkers_PlayerHomingPoint:setHomeOnTargetInView(arg0) end

---@public
---@return boolean
function WorldMarkers_PlayerHomingPoint:isActive() end

---@public
---@return float
function WorldMarkers_PlayerHomingPoint:getStickToCharDist() end

---@public
---@return boolean
function WorldMarkers_PlayerHomingPoint:isHomeOnTargetInView() end

---@public
---@return float
function WorldMarkers_PlayerHomingPoint:getHomeOnOffsetX() end

---@public
---@return float
function WorldMarkers_PlayerHomingPoint:getG() end

---@public
---@param arg0 float
---@return void
function WorldMarkers_PlayerHomingPoint:setHomeOnOffsetX(arg0) end

---@public
---@return float
function WorldMarkers_PlayerHomingPoint:getB() end

---@public
---@param arg0 float
---@return void
function WorldMarkers_PlayerHomingPoint:setRenderOffsetX(arg0) end

---@public
---@param arg0 int
---@return void
function WorldMarkers_PlayerHomingPoint:setHomeOnTargetDist(arg0) end

---@public
---@param arg0 float
---@return void
function WorldMarkers_PlayerHomingPoint:setAngleLerpVal(arg0) end
