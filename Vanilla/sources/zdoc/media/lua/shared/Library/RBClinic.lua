---@class RBClinic : zombie.randomizedWorld.randomizedBuilding.RBClinic
RBClinic = {}

---@public
---@param arg0 BuildingDef
---@return void
function RBClinic:randomizeBuilding(arg0) end

---@public
---@param arg0 BuildingDef
---@param arg1 boolean
---@return boolean
function RBClinic:isValid(arg0, arg1) end

---@public
---@param arg0 IsoGridSquare
---@return boolean
function RBClinic:roomValid(arg0) end
