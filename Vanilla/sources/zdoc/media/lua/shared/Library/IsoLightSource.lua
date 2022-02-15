---@class IsoLightSource : zombie.iso.IsoLightSource
---@field public NextID int
---@field public ID int
---@field public x int
---@field public y int
---@field public z int
---@field public r float
---@field public g float
---@field public b float
---@field public rJNI float
---@field public gJNI float
---@field public bJNI float
---@field public radius int
---@field public bActive boolean
---@field public bWasActive boolean
---@field public bActiveJNI boolean
---@field public life int
---@field public startlife int
---@field public localToBuilding IsoBuilding
---@field public bHydroPowered boolean
---@field public switches ArrayList|IsoLightSwitch
---@field public chunk IsoChunk
---@field public lightMap Object
IsoLightSource = {}

---@public
---@param bActive boolean @the bActive to set
---@return void
function IsoLightSource:setActive(bActive) end

---@public
---@return int @the z
function IsoLightSource:getZ() end

---@public
---@return boolean
function IsoLightSource:isHydroPowered() end

---@public
---@return void
function IsoLightSource:clearInfluence() end

---@public
---@return int @the y
function IsoLightSource:getY() end

---@public
---@return void
function IsoLightSource:update() end

---@public
---@return boolean @the bWasActive
function IsoLightSource:wasActive() end

---@public
---@param g float @the g to set
---@return void
function IsoLightSource:setG(g) end

---@public
---@return boolean
---@overload fun(minX:int, minY:int, maxX:int, maxY:int)
function IsoLightSource:isInBounds() end

---@public
---@param minX int
---@param minY int
---@param maxX int
---@param maxY int
---@return boolean
function IsoLightSource:isInBounds(minX, minY, maxX, maxY) end

---@public
---@return ArrayList|IsoLightSwitch @the switches
function IsoLightSource:getSwitches() end

---@public
---@return IsoBuilding
function IsoLightSource:getLocalToBuilding() end

---@public
---@param bWasActive boolean @the bWasActive to set
---@return void
function IsoLightSource:setWasActive(bWasActive) end

---@public
---@return int @the x
function IsoLightSource:getX() end

---@public
---@param z int @the z to set
---@return void
function IsoLightSource:setZ(z) end

---@public
---@return float @the r
function IsoLightSource:getR() end

---@public
---@return int @the radius
function IsoLightSource:getRadius() end

---@public
---@param y int @the y to set
---@return void
function IsoLightSource:setY(y) end

---@public
---@param radius int @the radius to set
---@return void
function IsoLightSource:setRadius(radius) end

---@public
---@return float @the b
function IsoLightSource:getB() end

---@public
---@return float @the g
function IsoLightSource:getG() end

---@public
---@return boolean @the bActive
function IsoLightSource:isActive() end

---@public
---@param x int @the x to set
---@return void
function IsoLightSource:setX(x) end

---@public
---@param b float @the b to set
---@return void
function IsoLightSource:setB(b) end

---@public
---@param switches ArrayList|IsoLightSwitch @the switches to set
---@return void
function IsoLightSource:setSwitches(switches) end

---@public
---@param r float @the r to set
---@return void
function IsoLightSource:setR(r) end
