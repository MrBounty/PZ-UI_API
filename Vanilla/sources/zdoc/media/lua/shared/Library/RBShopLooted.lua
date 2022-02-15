---@class RBShopLooted : zombie.randomizedWorld.randomizedBuilding.RBShopLooted
---@field private buildingList ArrayList|Unknown
RBShopLooted = {}

---@public
---@param arg0 BuildingDef
---@return void
function RBShopLooted:randomizeBuilding(arg0) end

---@public
---@param arg0 BuildingDef
---@param arg1 boolean
---@return boolean
function RBShopLooted:isValid(arg0, arg1) end
