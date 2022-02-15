---@class WorldMarkers.DirectionArrow : zombie.iso.WorldMarkers.DirectionArrow
---@field public doDebug boolean
---@field private debugStuff WorldMarkers.DirectionArrow.DebugStuff
---@field private ID int
---@field private active boolean
---@field private isRemoved boolean
---@field private isDrawOnWorld boolean
---@field private renderTexture Texture
---@field private texture Texture
---@field private texStairsUp Texture
---@field private texStairsDown Texture
---@field private texDown Texture
---@field private x int
---@field private y int
---@field private z int
---@field private r float
---@field private g float
---@field private b float
---@field private a float
---@field private renderWidth float
---@field private renderHeight float
---@field private angle float
---@field private angleLerpVal float
---@field private lastWasWithinView boolean
---@field private renderScreenX float
---@field private renderScreenY float
---@field private renderWithAngle boolean
---@field private renderSizeMod float
WorldMarkers_DirectionArrow = {}

---@public
---@param arg0 float
---@return void
function WorldMarkers_DirectionArrow:setRenderHeight(arg0) end

---@public
---@param arg0 float
---@return void
function WorldMarkers_DirectionArrow:setA(arg0) end

---@public
---@return int
function WorldMarkers_DirectionArrow:getZ() end

---@public
---@return int
function WorldMarkers_DirectionArrow:getID() end

---@public
---@param arg0 int
---@return void
function WorldMarkers_DirectionArrow:setZ(arg0) end

---@public
---@return int
function WorldMarkers_DirectionArrow:getY() end

---@public
---@return float
function WorldMarkers_DirectionArrow:getA() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function WorldMarkers_DirectionArrow:setRGBA(arg0, arg1, arg2, arg3) end

---@public
---@return float
function WorldMarkers_DirectionArrow:getRenderHeight() end

---@public
---@param arg0 boolean
---@return void
function WorldMarkers_DirectionArrow:setActive(arg0) end

---@public
---@param arg0 String
---@return void
function WorldMarkers_DirectionArrow:setTexDown(arg0) end

---@public
---@param arg0 int
---@return void
function WorldMarkers_DirectionArrow:setY(arg0) end

---@public
---@param arg0 float
---@return void
function WorldMarkers_DirectionArrow:setRenderWidth(arg0) end

---@public
---@return float
function WorldMarkers_DirectionArrow:getRenderWidth() end

---@public
---@param arg0 String
---@return void
function WorldMarkers_DirectionArrow:setTexture(arg0) end

---@public
---@return int
function WorldMarkers_DirectionArrow:getX() end

---@public
---@param arg0 String
---@return void
function WorldMarkers_DirectionArrow:setTexStairsUp(arg0) end

---@public
---@param arg0 int
---@return void
function WorldMarkers_DirectionArrow:setX(arg0) end

---@public
---@return void
function WorldMarkers_DirectionArrow:remove() end

---@public
---@param arg0 String
---@return void
function WorldMarkers_DirectionArrow:setTexStairsDown(arg0) end

---@public
---@param arg0 float
---@return void
function WorldMarkers_DirectionArrow:setR(arg0) end

---@public
---@return boolean
function WorldMarkers_DirectionArrow:isRemoved() end

---@public
---@return float
function WorldMarkers_DirectionArrow:getR() end

---@public
---@return boolean
function WorldMarkers_DirectionArrow:isActive() end

---@public
---@param arg0 float
---@return void
function WorldMarkers_DirectionArrow:setB(arg0) end

---@public
---@return float
function WorldMarkers_DirectionArrow:getG() end

---@public
---@return float
function WorldMarkers_DirectionArrow:getB() end

---@public
---@param arg0 float
---@return void
function WorldMarkers_DirectionArrow:setG(arg0) end
