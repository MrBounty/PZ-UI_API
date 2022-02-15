---@class PZMath : zombie.core.math.PZMath
---@field public PI float
---@field public PI2 float
---@field public degToRads float
---@field public radToDegs float
---@field public microsToNanos long
---@field public millisToMicros long
---@field public secondsToMillis long
---@field public secondsToNanos long
PZMath = {}

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
---@overload fun(arg0:Vector3f, arg1:Vector3f, arg2:Vector3f, arg3:float)
---@overload fun(arg0:JVector2, arg1:JVector2, arg2:JVector2, arg3:float)
function PZMath:lerp(arg0, arg1, arg2) end

---@public
---@param arg0 Vector3f
---@param arg1 Vector3f
---@param arg2 Vector3f
---@param arg3 float
---@return Vector3f
function PZMath:lerp(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 JVector2
---@param arg1 JVector2
---@param arg2 JVector2
---@param arg3 float
---@return JVector2
function PZMath:lerp(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 float
---@return float
function PZMath:roundFromEdges(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@return boolean
---@overload fun(arg0:float, arg1:float, arg2:float)
function PZMath:equal(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return boolean
function PZMath:equal(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
---@overload fun(arg0:float, arg1:float)
function PZMath:max(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@return float
function PZMath:max(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 float
---@return float
function PZMath:tryParseFloat(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function PZMath:lerpAngle(arg0, arg1, arg2) end

---@public
---@param arg0 Matrix4f
---@param arg1 Matrix4f
---@return Matrix4f
function PZMath:convertMatrix(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@return float
function PZMath:getClosestAngleDegrees(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@return float
---@overload fun(arg0:float, arg1:float, arg2:float)
function PZMath:wrap(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function PZMath:wrap(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function PZMath:almostIdentity(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@return float
function PZMath:abs(arg0) end

---@public
---@param arg0 float
---@return int
function PZMath:roundToInt(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return int
---@overload fun(arg0:long, arg1:long, arg2:long)
---@overload fun(arg0:float, arg1:float, arg2:float)
function PZMath:clamp(arg0, arg1, arg2) end

---@public
---@param arg0 long
---@param arg1 long
---@param arg2 long
---@return long
function PZMath:clamp(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function PZMath:clamp(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function PZMath:c_lerp(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@return float
function PZMath:roundToNearest(arg0) end

---@public
---@param arg0 float
---@return int
function PZMath:sign(arg0) end

---@public
---@param arg0 float
---@return float
function PZMath:clamp_01(arg0) end

---@public
---@param arg0 float
---@return float
function PZMath:degToRad(arg0) end

---@public
---@param arg0 float
---@return float
function PZMath:lerpFunc_EaseOutQuad(arg0) end

---@public
---@param arg0 float
---@return float
function PZMath:frac(arg0) end

---@public
---@param arg0 float
---@return float
function PZMath:roundToIntPlus05(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@return float
function PZMath:getClosestAngle(arg0, arg1) end

---@public
---@param arg0 Quaternion
---@param arg1 Quaternion
---@param arg2 Quaternion
---@param arg3 float
---@return Quaternion
function PZMath:slerp(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 float
---@return float
function PZMath:radToDeg(arg0) end

---@public
---@param arg0 float
---@return float
function PZMath:ceil(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function PZMath:step(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@return PZMath.SideOfLine
function PZMath:testSideOfLine(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 float
---@return float
function PZMath:lerpFunc_EaseOutInQuad(arg0) end

---@public
---@param arg0 String
---@param arg1 int
---@return int
function PZMath:tryParseInt(arg0, arg1) end

---@public
---@param arg0 float
---@return float
function PZMath:almostUnitIdentity(arg0) end

---@public
---@param arg0 String
---@return boolean
function PZMath:canParseFloat(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
---@overload fun(arg0:float, arg1:float)
function PZMath:min(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@return float
function PZMath:min(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@return float
function PZMath:gain(arg0, arg1) end

---@public
---@param arg0 float
---@return float
function PZMath:floor(arg0) end

---@public
---@param arg0 float
---@return float
function PZMath:sqrt(arg0) end

---@public
---@param arg0 float
---@return float
function PZMath:lerpFunc_EaseInQuad(arg0) end
