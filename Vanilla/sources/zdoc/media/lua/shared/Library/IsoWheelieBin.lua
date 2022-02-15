---@class IsoWheelieBin : zombie.iso.objects.IsoWheelieBin
---@field velx float
---@field vely float
IsoWheelieBin = {}

---Overrides:
---
---getObjectName in class IsoPushableObject
---@public
---@return String
function IsoWheelieBin:getObjectName() end

---Overrides:
---
---getWeight in class IsoPushableObject
---@public
---@param x float
---@param y float
---@return float
function IsoWheelieBin:getWeight(x, y) end

---Overrides:
---
---update in class IsoPushableObject
---@public
---@return void
function IsoWheelieBin:update() end
