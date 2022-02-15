---@class Vector2f : org.joml.Vector2f
---@field private serialVersionUID long
---@field public x float
---@field public y float
Vector2f = {}

---@public
---@param arg0 Vector2dc
---@return Vector2f
---@overload fun(arg0:ByteBuffer)
---@overload fun(arg0:float[])
---@overload fun(arg0:double)
---@overload fun(arg0:FloatBuffer)
---@overload fun(arg0:float)
---@overload fun(arg0:Vector2fc)
---@overload fun(arg0:Vector2ic)
---@overload fun(arg0:float, arg1:float)
---@overload fun(arg0:int, arg1:FloatBuffer)
---@overload fun(arg0:int, arg1:ByteBuffer)
---@overload fun(arg0:double, arg1:double)
function Vector2f:set(arg0) end

---@public
---@param arg0 ByteBuffer
---@return Vector2f
function Vector2f:set(arg0) end

---@public
---@param arg0 float[]
---@return Vector2f
function Vector2f:set(arg0) end

---@public
---@param arg0 double
---@return Vector2f
function Vector2f:set(arg0) end

---@public
---@param arg0 FloatBuffer
---@return Vector2f
function Vector2f:set(arg0) end

---@public
---@param arg0 float
---@return Vector2f
function Vector2f:set(arg0) end

---@public
---@param arg0 Vector2fc
---@return Vector2f
function Vector2f:set(arg0) end

---@public
---@param arg0 Vector2ic
---@return Vector2f
function Vector2f:set(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@return Vector2f
function Vector2f:set(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 FloatBuffer
---@return Vector2f
function Vector2f:set(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 ByteBuffer
---@return Vector2f
function Vector2f:set(arg0, arg1) end

---@public
---@param arg0 double
---@param arg1 double
---@return Vector2f
function Vector2f:set(arg0, arg1) end

---@public
---@param arg0 Object
---@return boolean
---@overload fun(arg0:float, arg1:float)
---@overload fun(arg0:Vector2fc, arg1:float)
function Vector2f:equals(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@return boolean
function Vector2f:equals(arg0, arg1) end

---@public
---@param arg0 Vector2fc
---@param arg1 float
---@return boolean
function Vector2f:equals(arg0, arg1) end

---@public
---@param arg0 Vector2fc
---@return Vector2f
---@overload fun(arg0:Matrix2dc)
---@overload fun(arg0:float)
---@overload fun(arg0:Matrix2fc)
---@overload fun(arg0:Vector2fc, arg1:Vector2f)
---@overload fun(arg0:float, arg1:float)
---@overload fun(arg0:Matrix2fc, arg1:Vector2f)
---@overload fun(arg0:float, arg1:Vector2f)
---@overload fun(arg0:Matrix2dc, arg1:Vector2f)
---@overload fun(arg0:float, arg1:float, arg2:Vector2f)
function Vector2f:mul(arg0) end

---@public
---@param arg0 Matrix2dc
---@return Vector2f
function Vector2f:mul(arg0) end

---@public
---@param arg0 float
---@return Vector2f
function Vector2f:mul(arg0) end

---@public
---@param arg0 Matrix2fc
---@return Vector2f
function Vector2f:mul(arg0) end

---@public
---@param arg0 Vector2fc
---@param arg1 Vector2f
---@return Vector2f
function Vector2f:mul(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@return Vector2f
function Vector2f:mul(arg0, arg1) end

---@public
---@param arg0 Matrix2fc
---@param arg1 Vector2f
---@return Vector2f
function Vector2f:mul(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 Vector2f
---@return Vector2f
function Vector2f:mul(arg0, arg1) end

---@public
---@param arg0 Matrix2dc
---@param arg1 Vector2f
---@return Vector2f
function Vector2f:mul(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 Vector2f
---@return Vector2f
function Vector2f:mul(arg0, arg1, arg2) end

---@public
---@return boolean
function Vector2f:isFinite() end

---@public
---@return float
---@overload fun(arg0:float, arg1:float)
function Vector2f:length() end

---@public
---@param arg0 float
---@param arg1 float
---@return float
function Vector2f:length(arg0, arg1) end

---@public
---@param arg0 ByteBuffer
---@return ByteBuffer
---@overload fun(arg0:Vector2d)
---@overload fun(arg0:Vector2f)
---@overload fun(arg0:int)
---@overload fun(arg0:FloatBuffer)
---@overload fun(arg0:int, arg1:ByteBuffer)
---@overload fun(arg0:int, arg1:Vector2i)
---@overload fun(arg0:int, arg1:FloatBuffer)
function Vector2f:get(arg0) end

---@public
---@param arg0 Vector2d
---@return Vector2d
function Vector2f:get(arg0) end

---@public
---@param arg0 Vector2f
---@return Vector2f
function Vector2f:get(arg0) end

---@public
---@param arg0 int
---@return float
function Vector2f:get(arg0) end

---@public
---@param arg0 FloatBuffer
---@return FloatBuffer
function Vector2f:get(arg0) end

---@public
---@param arg0 int
---@param arg1 ByteBuffer
---@return ByteBuffer
function Vector2f:get(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 Vector2i
---@return Vector2i
function Vector2f:get(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 FloatBuffer
---@return FloatBuffer
function Vector2f:get(arg0, arg1) end

---@public
---@return Vector2f
---@overload fun(arg0:Vector2f)
function Vector2f:absolute() end

---@public
---@param arg0 Vector2f
---@return Vector2f
function Vector2f:absolute(arg0) end

---@public
---@param arg0 Vector2fc
---@return Vector2f
---@overload fun(arg0:float, arg1:float)
---@overload fun(arg0:Vector2fc, arg1:Vector2f)
---@overload fun(arg0:float, arg1:float, arg2:Vector2f)
function Vector2f:sub(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@return Vector2f
function Vector2f:sub(arg0, arg1) end

---@public
---@param arg0 Vector2fc
---@param arg1 Vector2f
---@return Vector2f
function Vector2f:sub(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 Vector2f
---@return Vector2f
function Vector2f:sub(arg0, arg1, arg2) end

---@public
---@return float
---@overload fun(arg0:float, arg1:float)
function Vector2f:lengthSquared() end

---@public
---@param arg0 float
---@param arg1 float
---@return float
function Vector2f:lengthSquared(arg0, arg1) end

---@public
---@param arg0 Matrix3x2fc
---@return Vector2f
---@overload fun(arg0:Matrix3x2fc, arg1:Vector2f)
function Vector2f:mulPosition(arg0) end

---@public
---@param arg0 Matrix3x2fc
---@param arg1 Vector2f
---@return Vector2f
function Vector2f:mulPosition(arg0, arg1) end

---@public
---@return float
function Vector2f:x() end

---@public
---@param arg0 int
---@param arg1 float
---@return Vector2f
function Vector2f:setComponent(arg0, arg1) end

---@public
---@param arg0 Vector2fc
---@return Vector2f
---@overload fun(arg0:float, arg1:float)
---@overload fun(arg0:Vector2fc, arg1:Vector2f)
---@overload fun(arg0:float, arg1:float, arg2:Vector2f)
function Vector2f:add(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@return Vector2f
function Vector2f:add(arg0, arg1) end

---@public
---@param arg0 Vector2fc
---@param arg1 Vector2f
---@return Vector2f
function Vector2f:add(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 Vector2f
---@return Vector2f
function Vector2f:add(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 Vector2fc
---@return Vector2f
---@overload fun(arg0:Vector2fc, arg1:Vector2fc)
---@overload fun(arg0:float, arg1:Vector2fc, arg2:Vector2f)
---@overload fun(arg0:Vector2fc, arg1:Vector2fc, arg2:Vector2f)
function Vector2f:fma(arg0, arg1) end

---@public
---@param arg0 Vector2fc
---@param arg1 Vector2fc
---@return Vector2f
function Vector2f:fma(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 Vector2fc
---@param arg2 Vector2f
---@return Vector2f
function Vector2f:fma(arg0, arg1, arg2) end

---@public
---@param arg0 Vector2fc
---@param arg1 Vector2fc
---@param arg2 Vector2f
---@return Vector2f
function Vector2f:fma(arg0, arg1, arg2) end

---@public
---@return String
---@overload fun(arg0:NumberFormat)
function Vector2f:toString() end

---@public
---@param arg0 NumberFormat
---@return String
function Vector2f:toString(arg0) end

---@public
---@param arg0 Vector2fc
---@return Vector2f
---@overload fun(arg0:Vector2fc, arg1:Vector2f)
function Vector2f:min(arg0) end

---@public
---@param arg0 Vector2fc
---@param arg1 Vector2f
---@return Vector2f
function Vector2f:min(arg0, arg1) end

---@public
---@param arg0 Vector2fc
---@return float
---@overload fun(arg0:float, arg1:float)
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float)
function Vector2f:distanceSquared(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@return float
function Vector2f:distanceSquared(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return float
function Vector2f:distanceSquared(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 Vector2fc
---@return Vector2f
---@overload fun(arg0:float)
---@overload fun(arg0:float, arg1:float)
---@overload fun(arg0:Vector2fc, arg1:Vector2f)
---@overload fun(arg0:float, arg1:Vector2f)
---@overload fun(arg0:float, arg1:float, arg2:Vector2f)
function Vector2f:div(arg0) end

---@public
---@param arg0 float
---@return Vector2f
function Vector2f:div(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@return Vector2f
function Vector2f:div(arg0, arg1) end

---@public
---@param arg0 Vector2fc
---@param arg1 Vector2f
---@return Vector2f
function Vector2f:div(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 Vector2f
---@return Vector2f
function Vector2f:div(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 Vector2f
---@return Vector2f
function Vector2f:div(arg0, arg1, arg2) end

---@public
---@param arg0 Matrix3x2fc
---@return Vector2f
---@overload fun(arg0:Matrix3x2fc, arg1:Vector2f)
function Vector2f:mulDirection(arg0) end

---@public
---@param arg0 Matrix3x2fc
---@param arg1 Vector2f
---@return Vector2f
function Vector2f:mulDirection(arg0, arg1) end

---@public
---@param arg0 Matrix2fc
---@return Vector2f
---@overload fun(arg0:Matrix2fc, arg1:Vector2f)
function Vector2f:mulTranspose(arg0) end

---@public
---@param arg0 Matrix2fc
---@param arg1 Vector2f
---@return Vector2f
function Vector2f:mulTranspose(arg0, arg1) end

---@public
---@param arg0 ObjectInput
---@return void
function Vector2f:readExternal(arg0) end

---@public
---@return Vector2f
---@overload fun(arg0:Vector2f)
---@overload fun(arg0:float)
---@overload fun(arg0:float, arg1:Vector2f)
function Vector2f:normalize() end

---@public
---@param arg0 Vector2f
---@return Vector2f
function Vector2f:normalize(arg0) end

---@public
---@param arg0 float
---@return Vector2f
function Vector2f:normalize(arg0) end

---@public
---@param arg0 float
---@param arg1 Vector2f
---@return Vector2f
function Vector2f:normalize(arg0, arg1) end

---@public
---@return Vector2f
---@overload fun(arg0:Vector2f)
function Vector2f:round() end

---@public
---@param arg0 Vector2f
---@return Vector2f
function Vector2f:round(arg0) end

---@public
---@param arg0 Vector2fc
---@return Vector2f
---@overload fun(arg0:Vector2fc, arg1:Vector2f)
function Vector2f:max(arg0) end

---@public
---@param arg0 Vector2fc
---@param arg1 Vector2f
---@return Vector2f
function Vector2f:max(arg0, arg1) end

---@public
---@return Vector2f
---@overload fun(arg0:Vector2f)
function Vector2f:ceil() end

---@public
---@param arg0 Vector2f
---@return Vector2f
function Vector2f:ceil(arg0) end

---@public
---@return Vector2f
---@overload fun(arg0:Vector2f)
function Vector2f:floor() end

---@public
---@param arg0 Vector2f
---@return Vector2f
function Vector2f:floor(arg0) end

---@public
---@return Vector2f
---@overload fun(arg0:Vector2f)
function Vector2f:negate() end

---@public
---@param arg0 Vector2f
---@return Vector2f
function Vector2f:negate(arg0) end

---@public
---@param arg0 Vector2fc
---@param arg1 float
---@return Vector2f
---@overload fun(arg0:Vector2fc, arg1:float, arg2:Vector2f)
function Vector2f:lerp(arg0, arg1) end

---@public
---@param arg0 Vector2fc
---@param arg1 float
---@param arg2 Vector2f
---@return Vector2f
function Vector2f:lerp(arg0, arg1, arg2) end

---@public
---@return int
function Vector2f:minComponent() end

---@public
---@return int
function Vector2f:maxComponent() end

---@public
---@param arg0 long
---@return Vector2fc
function Vector2f:getToAddress(arg0) end

---@public
---@return float
function Vector2f:y() end

---@public
---@param arg0 long
---@return Vector2f
function Vector2f:setFromAddress(arg0) end

---@public
---@param arg0 ObjectOutput
---@return void
function Vector2f:writeExternal(arg0) end

---@public
---@param arg0 Vector2fc
---@return float
---@overload fun(arg0:float, arg1:float)
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float)
function Vector2f:distance(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@return float
function Vector2f:distance(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return float
function Vector2f:distance(arg0, arg1, arg2, arg3) end

---@public
---@return int
function Vector2f:hashCode() end

---@public
---@return Vector2f
function Vector2f:perpendicular() end

---@public
---@param arg0 Vector2fc
---@return float
function Vector2f:angle(arg0) end

---@public
---@return Vector2f
function Vector2f:zero() end

---@public
---@param arg0 Vector2fc
---@return float
function Vector2f:dot(arg0) end
