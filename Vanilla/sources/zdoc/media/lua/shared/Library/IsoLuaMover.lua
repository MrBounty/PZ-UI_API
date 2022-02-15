---@class IsoLuaMover : zombie.iso.IsoLuaMover
---@field public luaMoverTable KahluaTable
IsoLuaMover = {}

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoLuaMover:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param name String
---@param seconds float
---@param looped boolean
---@param playing boolean
---@return void
function IsoLuaMover:playAnim(name, seconds, looped, playing) end

---Overrides:
---
---update in class IsoGameCharacter
---@public
---@return void
function IsoLuaMover:update() end

---Overrides:
---
---getObjectName in class IsoMovingObject
---@public
---@return String
function IsoLuaMover:getObjectName() end
