---@class VehicleScript.Model : zombie.scripting.objects.VehicleScript.Model
---@field public id String
---@field public file String
---@field public scale float
---@field public offset Vector3f
---@field public rotate Vector3f
VehicleScript_Model = {}

---@public
---@return String
function VehicleScript_Model:getId() end

---@public
---@return Vector3f
function VehicleScript_Model:getOffset() end

---@return VehicleScript.Model
function VehicleScript_Model:makeCopy() end

---@public
---@return Vector3f
function VehicleScript_Model:getRotate() end
