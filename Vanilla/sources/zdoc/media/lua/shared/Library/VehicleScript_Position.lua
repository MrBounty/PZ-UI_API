---@class VehicleScript.Position : zombie.scripting.objects.VehicleScript.Position
---@field public id String
---@field public offset Vector3f
---@field public rotate Vector3f
---@field public area String
VehicleScript_Position = {}

---@return VehicleScript.Position
function VehicleScript_Position:makeCopy() end

---@public
---@return String
function VehicleScript_Position:getId() end

---@public
---@return Vector3f
function VehicleScript_Position:getRotate() end

---@public
---@return String
function VehicleScript_Position:getArea() end

---@public
---@return Vector3f
function VehicleScript_Position:getOffset() end
