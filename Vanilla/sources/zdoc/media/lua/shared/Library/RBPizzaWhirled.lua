---@class RBPizzaWhirled : zombie.randomizedWorld.randomizedBuilding.RBPizzaWhirled
RBPizzaWhirled = {}

---@public
---@param arg0 IsoGridSquare
---@return boolean
function RBPizzaWhirled:roomValid(arg0) end

---@public
---@param arg0 BuildingDef
---@param arg1 boolean
---@return boolean
function RBPizzaWhirled:isValid(arg0, arg1) end

---@public
---@param arg0 BuildingDef
---@return void
function RBPizzaWhirled:randomizeBuilding(arg0) end
