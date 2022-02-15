---@class RBSchool : zombie.randomizedWorld.randomizedBuilding.RBSchool
RBSchool = {}

---@public
---@param arg0 IsoGridSquare
---@return boolean
function RBSchool:roomValid(arg0) end

---@public
---@param arg0 BuildingDef
---@param arg1 boolean
---@return boolean
function RBSchool:isValid(arg0, arg1) end

---@public
---@param arg0 BuildingDef
---@return void
function RBSchool:randomizeBuilding(arg0) end
