---@class VehicleScript.PhysicsShape : zombie.scripting.objects.VehicleScript.PhysicsShape
---@field public type int
---@field public offset Vector3f
---@field public rotate Vector3f
---@field public extents Vector3f
---@field public radius float
VehicleScript_PhysicsShape = {}

---@public
---@return String
function VehicleScript_PhysicsShape:getTypeString() end

---@public
---@param arg0 float
---@return void
function VehicleScript_PhysicsShape:setRadius(arg0) end

---@public
---@return Vector3f
function VehicleScript_PhysicsShape:getExtents() end

---@public
---@return float
function VehicleScript_PhysicsShape:getRadius() end

---@public
---@return Vector3f
function VehicleScript_PhysicsShape:getOffset() end

---@public
---@return Vector3f
function VehicleScript_PhysicsShape:getRotate() end
