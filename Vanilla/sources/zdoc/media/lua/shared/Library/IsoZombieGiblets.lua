---@class IsoZombieGiblets : zombie.iso.objects.IsoZombieGiblets
---@field public tintb float
---@field public tintg float
---@field public tintr float
---@field public time float
---@field invis boolean
IsoZombieGiblets = {}

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoZombieGiblets:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---Overrides:
---
---Serialize in class IsoObject
---@public
---@return boolean
function IsoZombieGiblets:Serialize() end

---Overrides:
---
---getObjectName in class IsoMovingObject
---@public
---@return String
function IsoZombieGiblets:getObjectName() end

---Overrides:
---
---update in class IsoPhysicsObject
---@public
---@return void
function IsoZombieGiblets:update() end
