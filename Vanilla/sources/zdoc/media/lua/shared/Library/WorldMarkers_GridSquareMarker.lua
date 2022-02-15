---@class WorldMarkers.GridSquareMarker : zombie.iso.WorldMarkers.GridSquareMarker
---@field private ID int
---@field private sprite IsoSpriteInstance
---@field private spriteOverlay IsoSpriteInstance
---@field private orig_x float
---@field private orig_y float
---@field private orig_z float
---@field private x float
---@field private y float
---@field private z float
---@field private scaleRatio float
---@field private r float
---@field private g float
---@field private b float
---@field private a float
---@field private size float
---@field private doBlink boolean
---@field private doAlpha boolean
---@field private bScaleCircleTexture boolean
---@field private fadeSpeed float
---@field private alpha float
---@field private alphaMax float
---@field private alphaMin float
---@field private alphaInc boolean
---@field private active boolean
---@field private isRemoved boolean
WorldMarkers_GridSquareMarker = {}

---@public
---@param arg0 float
---@return void
function WorldMarkers_GridSquareMarker:setA(arg0) end

---@public
---@return float
function WorldMarkers_GridSquareMarker:getX() end

---@public
---@return float
function WorldMarkers_GridSquareMarker:getA() end

---@public
---@param arg0 boolean
---@return void
function WorldMarkers_GridSquareMarker:setDoAlpha(arg0) end

---@public
---@return boolean
function WorldMarkers_GridSquareMarker:isDoBlink() end

---@public
---@return float
function WorldMarkers_GridSquareMarker:getAlphaMax() end

---@public
---@param arg0 float
---@return void
function WorldMarkers_GridSquareMarker:setSize(arg0) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 float
---@return void
function WorldMarkers_GridSquareMarker:init(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@return float
function WorldMarkers_GridSquareMarker:getAlphaMin() end

---@public
---@return float
function WorldMarkers_GridSquareMarker:getZ() end

---@public
---@return boolean
function WorldMarkers_GridSquareMarker:isDoAlpha() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 float
---@return void
function WorldMarkers_GridSquareMarker:setPosAndSize(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function WorldMarkers_GridSquareMarker:setPos(arg0, arg1, arg2) end

---@public
---@param arg0 boolean
---@return void
function WorldMarkers_GridSquareMarker:setActive(arg0) end

---@public
---@param arg0 float
---@return void
function WorldMarkers_GridSquareMarker:setAlpha(arg0) end

---@public
---@return float
function WorldMarkers_GridSquareMarker:getY() end

---@public
---@param arg0 float
---@return void
function WorldMarkers_GridSquareMarker:setFadeSpeed(arg0) end

---@public
---@return float
function WorldMarkers_GridSquareMarker:getAlpha() end

---@public
---@param arg0 float
---@return void
function WorldMarkers_GridSquareMarker:setAlphaMin(arg0) end

---@public
---@return float
function WorldMarkers_GridSquareMarker:getFadeSpeed() end

---@public
---@return int
function WorldMarkers_GridSquareMarker:getID() end

---@public
---@param arg0 float
---@return void
function WorldMarkers_GridSquareMarker:setR(arg0) end

---@public
---@return boolean
function WorldMarkers_GridSquareMarker:isRemoved() end

---@public
---@return boolean
function WorldMarkers_GridSquareMarker:isScaleCircleTexture() end

---@public
---@param arg0 float
---@return void
function WorldMarkers_GridSquareMarker:setAlphaMax(arg0) end

---@public
---@param arg0 boolean
---@return void
function WorldMarkers_GridSquareMarker:setScaleCircleTexture(arg0) end

---@public
---@return float
function WorldMarkers_GridSquareMarker:getR() end

---@public
---@return float
function WorldMarkers_GridSquareMarker:getG() end

---@public
---@return void
function WorldMarkers_GridSquareMarker:remove() end

---@public
---@param arg0 boolean
---@return void
function WorldMarkers_GridSquareMarker:setDoBlink(arg0) end

---@public
---@param arg0 float
---@return void
function WorldMarkers_GridSquareMarker:setG(arg0) end

---@public
---@return float
function WorldMarkers_GridSquareMarker:getSize() end

---@public
---@param arg0 float
---@return void
function WorldMarkers_GridSquareMarker:setB(arg0) end

---@public
---@return float
function WorldMarkers_GridSquareMarker:getB() end

---@public
---@return boolean
function WorldMarkers_GridSquareMarker:isActive() end
