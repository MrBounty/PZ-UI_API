---@class VehicleScript.Part : zombie.scripting.objects.VehicleScript.Part
---@field public id String
---@field public parent String
---@field public itemType ArrayList|Unknown
---@field public container VehicleScript.Container
---@field public area String
---@field public wheel String
---@field public tables HashMap|Unknown|Unknown
---@field public luaFunctions HashMap|Unknown|Unknown
---@field public models ArrayList|Unknown
---@field public door VehicleScript.Door
---@field public window VehicleScript.Window
---@field public anims ArrayList|Unknown
---@field public category String
---@field public specificItem boolean
---@field public mechanicRequireKey boolean
---@field public repairMechanic boolean
---@field public hasLightsRear boolean
VehicleScript_Part = {}

---@return VehicleScript.Part
function VehicleScript_Part:makeCopy() end

---@public
---@param arg0 boolean
---@return void
function VehicleScript_Part:setMechanicRequireKey(arg0) end

---@public
---@param arg0 boolean
---@return void
function VehicleScript_Part:setRepairMechanic(arg0) end

---@public
---@return boolean
function VehicleScript_Part:isMechanicRequireKey() end

---@public
---@return boolean
function VehicleScript_Part:isRepairMechanic() end
