---@class IsoUtils : zombie.iso.IsoUtils
IsoUtils = {}

---@public
---@param fromX float
---@param fromY float
---@param toX float
---@param toY float
---@return float
---@overload fun(fromX:float, fromY:float, toX:float, toY:float, fromZ:float, toZ:float)
function IsoUtils:DistanceManhatten(fromX, fromY, toX, toY) end

---@public
---@param fromX float
---@param fromY float
---@param toX float
---@param toY float
---@param fromZ float
---@param toZ float
---@return float
function IsoUtils:DistanceManhatten(fromX, fromY, toX, toY, fromZ, toZ) end

---@public
---@param objectX int
---@param objectY int
---@param objectZ int
---@param screenZ int
---@return float
function IsoUtils:YToScreenInt(objectX, objectY, objectZ, screenZ) end

---@public
---@param screenX float
---@param screenY float
---@param floor float
---@return float
function IsoUtils:XToIso(screenX, screenY, floor) end

---@public
---@param screenX float
---@param screenY float
---@param floor float
---@return float
function IsoUtils:YToIso(screenX, screenY, floor) end

---@public
---@param fromX float
---@param fromY float
---@param toX float
---@param toY float
---@return float
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float, arg4:float, arg5:float)
function IsoUtils:DistanceToSquared(fromX, fromY, toX, toY) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@return float
function IsoUtils:DistanceToSquared(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function IsoUtils:smoothstep(arg0, arg1, arg2) end

---@public
---@param objectX float
---@param objectY float
---@param objectZ float
---@param screenZ int
---@return float
function IsoUtils:YToScreen(objectX, objectY, objectZ, screenZ) end

---@public
---@param fromX float
---@param fromY float
---@param toX float
---@param toY float
---@return float
---@overload fun(fromX:float, fromY:float, fromZ:float, toX:float, toY:float, toZ:float)
function IsoUtils:DistanceTo(fromX, fromY, toX, toY) end

---@public
---@param fromX float
---@param fromY float
---@param fromZ float
---@param toX float
---@param toY float
---@param toZ float
---@return float
function IsoUtils:DistanceTo(fromX, fromY, fromZ, toX, toY, toZ) end

---@public
---@param objectX float
---@param objectY float
---@param objectZ float
---@param screenZ int
---@return float
function IsoUtils:XToScreenExact(objectX, objectY, objectZ, screenZ) end

---@public
---@param objectX float
---@param objectY float
---@param objectZ float
---@param screenZ int
---@return float
function IsoUtils:YToScreenExact(objectX, objectY, objectZ, screenZ) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@return boolean
function IsoUtils:isSimilarDirection(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param fromX float
---@param fromY float
---@param toX float
---@param toY float
---@return float
function IsoUtils:DistanceTo2D(fromX, fromY, toX, toY) end

---@public
---@param fromX float
---@param fromY float
---@param toX float
---@param toY float
---@return float
function IsoUtils:DistanceManhattenSquare(fromX, fromY, toX, toY) end

---@public
---@param screenX float
---@param screenY float
---@param floor int
---@return float
function IsoUtils:XToIsoTrue(screenX, screenY, floor) end

---@public
---@param objectX int
---@param objectY int
---@param objectZ int
---@param screenZ int
---@return float
function IsoUtils:XToScreenInt(objectX, objectY, objectZ, screenZ) end

---@public
---@param objectX float
---@param objectY float
---@param objectZ float
---@param screenZ int
---@return float
function IsoUtils:XToScreen(objectX, objectY, objectZ, screenZ) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function IsoUtils:clamp(arg0, arg1, arg2) end
