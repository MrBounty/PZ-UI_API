---@class Moveable : zombie.inventory.types.Moveable
---@field protected worldSprite String
---@field private isLight boolean
---@field private lightUseBattery boolean
---@field private lightHasBattery boolean
---@field private lightBulbItem String
---@field private lightPower float
---@field private lightDelta float
---@field private lightR float
---@field private lightG float
---@field private lightB float
---@field private isMultiGridAnchor boolean
---@field private spriteGrid IsoSpriteGrid
---@field private customNameFull String
---@field private movableFullName String
---@field protected canBeDroppedOnFloor boolean
---@field private hasReadWorldSprite boolean
---@field protected customItem String
Moveable = {}

---@public
---@return boolean
function Moveable:isLightUseBattery() end

---@public
---@return boolean
function Moveable:isLight() end

---@public
---@return String
function Moveable:getDisplayName() end

---@public
---@param arg0 float
---@return void
function Moveable:setLightG(arg0) end

---@public
---@return boolean
function Moveable:isMultiGridAnchor() end

---@public
---@return int
function Moveable:getSaveType() end

---@public
---@param arg0 float
---@return void
function Moveable:setLightB(arg0) end

---@public
---@param arg0 float
---@return void
function Moveable:setLightDelta(arg0) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function Moveable:load(arg0, arg1) end

---@public
---@return float
function Moveable:getLightB() end

---@public
---@return boolean
function Moveable:isLightHasBattery() end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function Moveable:save(arg0, arg1) end

---@public
---@return String
function Moveable:getName() end

---@public
---@return String
function Moveable:getCustomNameFull() end

---@public
---@param arg0 boolean
---@return void
function Moveable:setLightHasBattery(arg0) end

---@public
---@param arg0 String
---@return void
function Moveable:setWorldSprite(arg0) end

---@public
---@return String
function Moveable:getWorldSprite() end

---@public
---@param arg0 boolean
---@return void
function Moveable:setLightUseBattery(arg0) end

---@public
---@return float
function Moveable:getLightR() end

---@public
---@param arg0 boolean
---@return void
function Moveable:setLight(arg0) end

---@public
---@param arg0 String
---@return void
function Moveable:setLightBulbItem(arg0) end

---@public
---@return String
function Moveable:getLightBulbItem() end

---@public
---@return float
function Moveable:getLightG() end

---@public
---@return boolean
function Moveable:CanBeDroppedOnFloor() end

---@public
---@return float
function Moveable:getLightDelta() end

---@public
---@return IsoSpriteGrid
function Moveable:getSpriteGrid() end

---@public
---@param arg0 float
---@return void
function Moveable:setLightPower(arg0) end

---@public
---@param arg0 float
---@return void
function Moveable:setLightR(arg0) end

---@public
---@return String
function Moveable:getMovableFullName() end

---@public
---@return float
function Moveable:getLightPower() end

---@public
---@param arg0 String
---@return boolean
function Moveable:ReadFromWorldSprite(arg0) end
