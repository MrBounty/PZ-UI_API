---@class Math : java.lang.Math
---@field public E double
---@field public PI double
---@field private DEGREES_TO_RADIANS double
---@field private RADIANS_TO_DEGREES double
---@field private negativeZeroFloatBits long
---@field private negativeZeroDoubleBits long
---@field twoToTheDoubleScaleUp double
---@field twoToTheDoubleScaleDown double
Math = {}

---@public
---@param arg0 float
---@param arg1 double
---@return float
---@overload fun(arg0:double, arg1:double)
function Math:nextAfter(arg0, arg1) end

---@public
---@param arg0 double
---@param arg1 double
---@return double
function Math:nextAfter(arg0, arg1) end

---@public
---@param arg0 double
---@return double
function Math:exp(arg0) end

---@public
---@param arg0 long
---@param arg1 long
---@return long
---@overload fun(arg0:int, arg1:int)
---@overload fun(arg0:long, arg1:int)
function Math:floorDiv(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Math:floorDiv(arg0, arg1) end

---@public
---@param arg0 long
---@param arg1 int
---@return long
function Math:floorDiv(arg0, arg1) end

---@public
---@param arg0 long
---@return long
---@overload fun(arg0:int)
function Math:negateExact(arg0) end

---@public
---@param arg0 int
---@return int
function Math:negateExact(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@return float
---@overload fun(arg0:int, arg1:int)
---@overload fun(arg0:long, arg1:long)
---@overload fun(arg0:double, arg1:double)
function Math:max(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Math:max(arg0, arg1) end

---@public
---@param arg0 long
---@param arg1 long
---@return long
function Math:max(arg0, arg1) end

---@public
---@param arg0 double
---@param arg1 double
---@return double
function Math:max(arg0, arg1) end

---@public
---@param arg0 long
---@param arg1 long
---@return long
---@overload fun(arg0:int, arg1:int)
function Math:addExact(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Math:addExact(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@return float
---@overload fun(arg0:double, arg1:double)
function Math:copySign(arg0, arg1) end

---@public
---@param arg0 double
---@param arg1 double
---@return double
function Math:copySign(arg0, arg1) end

---@public
---@param arg0 long
---@param arg1 int
---@return int
---@overload fun(arg0:int, arg1:int)
---@overload fun(arg0:long, arg1:long)
function Math:floorMod(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Math:floorMod(arg0, arg1) end

---@public
---@param arg0 long
---@param arg1 long
---@return long
function Math:floorMod(arg0, arg1) end

---@public
---@param arg0 double
---@return double
function Math:cosh(arg0) end

---@public
---@param arg0 long
---@return long
---@overload fun(arg0:double)
---@overload fun(arg0:float)
---@overload fun(arg0:int)
function Math:abs(arg0) end

---@public
---@param arg0 double
---@return double
function Math:abs(arg0) end

---@public
---@param arg0 float
---@return float
function Math:abs(arg0) end

---@public
---@param arg0 int
---@return int
function Math:abs(arg0) end

---@public
---@param arg0 double
---@return double
function Math:floor(arg0) end

---@param arg0 int
---@return double
function Math:powerOfTwoD(arg0) end

---@public
---@param arg0 long
---@param arg1 long
---@return long
---@overload fun(arg0:int, arg1:int)
---@overload fun(arg0:long, arg1:int)
function Math:multiplyExact(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Math:multiplyExact(arg0, arg1) end

---@public
---@param arg0 long
---@param arg1 int
---@return long
function Math:multiplyExact(arg0, arg1) end

---@public
---@param arg0 long
---@return long
---@overload fun(arg0:int)
function Math:decrementExact(arg0) end

---@public
---@param arg0 int
---@return int
function Math:decrementExact(arg0) end

---@public
---@param arg0 double
---@return double
function Math:atan(arg0) end

---@public
---@return double
function Math:random() end

---@public
---@param arg0 double
---@return double
function Math:sqrt(arg0) end

---@public
---@param arg0 double
---@return double
function Math:cos(arg0) end

---@public
---@param arg0 float
---@return float
---@overload fun(arg0:double)
function Math:nextUp(arg0) end

---@public
---@param arg0 double
---@return double
function Math:nextUp(arg0) end

---@public
---@param arg0 long
---@param arg1 long
---@return long
function Math:multiplyHigh(arg0, arg1) end

---@public
---@param arg0 float
---@return float
---@overload fun(arg0:double)
function Math:signum(arg0) end

---@public
---@param arg0 double
---@return double
function Math:signum(arg0) end

---@public
---@param arg0 double
---@return double
function Math:asin(arg0) end

---@public
---@param arg0 double
---@return double
function Math:tanh(arg0) end

---@public
---@param arg0 long
---@param arg1 long
---@return long
---@overload fun(arg0:int, arg1:int)
function Math:subtractExact(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Math:subtractExact(arg0, arg1) end

---@public
---@param arg0 int
---@return int
---@overload fun(arg0:long)
function Math:absExact(arg0) end

---@public
---@param arg0 long
---@return long
function Math:absExact(arg0) end

---@public
---@param arg0 double
---@return double
function Math:log10(arg0) end

---@public
---@param arg0 double
---@param arg1 double
---@return double
function Math:pow(arg0, arg1) end

---@public
---@param arg0 double
---@return double
function Math:toDegrees(arg0) end

---@public
---@param arg0 double
---@return double
function Math:acos(arg0) end

---@public
---@param arg0 double
---@return long
---@overload fun(arg0:float)
function Math:round(arg0) end

---@public
---@param arg0 float
---@return int
function Math:round(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@return long
function Math:multiplyFull(arg0, arg1) end

---@public
---@param arg0 double
---@return double
function Math:expm1(arg0) end

---@public
---@param arg0 float
---@return int
---@overload fun(arg0:double)
function Math:getExponent(arg0) end

---@public
---@param arg0 double
---@return int
function Math:getExponent(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@return float
---@overload fun(arg0:double, arg1:double)
---@overload fun(arg0:long, arg1:long)
---@overload fun(arg0:int, arg1:int)
function Math:min(arg0, arg1) end

---@public
---@param arg0 double
---@param arg1 double
---@return double
function Math:min(arg0, arg1) end

---@public
---@param arg0 long
---@param arg1 long
---@return long
function Math:min(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Math:min(arg0, arg1) end

---@public
---@param arg0 double
---@param arg1 double
---@return double
function Math:IEEEremainder(arg0, arg1) end

---@public
---@param arg0 double
---@param arg1 double
---@param arg2 double
---@return double
---@overload fun(arg0:float, arg1:float, arg2:float)
function Math:fma(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function Math:fma(arg0, arg1, arg2) end

---@public
---@param arg0 double
---@return double
function Math:log(arg0) end

---@param arg0 int
---@return float
function Math:powerOfTwoF(arg0) end

---@public
---@param arg0 double
---@return double
---@overload fun(arg0:float)
function Math:nextDown(arg0) end

---@public
---@param arg0 float
---@return float
function Math:nextDown(arg0) end

---@public
---@param arg0 long
---@return long
---@overload fun(arg0:int)
function Math:incrementExact(arg0) end

---@public
---@param arg0 int
---@return int
function Math:incrementExact(arg0) end

---@public
---@param arg0 double
---@param arg1 double
---@return double
function Math:atan2(arg0, arg1) end

---@public
---@param arg0 double
---@return double
function Math:toRadians(arg0) end

---@public
---@param arg0 double
---@return double
function Math:ceil(arg0) end

---@public
---@param arg0 double
---@return double
function Math:sinh(arg0) end

---@public
---@param arg0 double
---@param arg1 double
---@return double
function Math:hypot(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 int
---@return float
---@overload fun(arg0:double, arg1:int)
function Math:scalb(arg0, arg1) end

---@public
---@param arg0 double
---@param arg1 int
---@return double
function Math:scalb(arg0, arg1) end

---@public
---@param arg0 double
---@return double
function Math:log1p(arg0) end

---@public
---@param arg0 double
---@return double
function Math:tan(arg0) end

---@public
---@param arg0 long
---@return int
function Math:toIntExact(arg0) end

---@public
---@param arg0 double
---@return double
function Math:rint(arg0) end

---@public
---@param arg0 double
---@return double
function Math:cbrt(arg0) end

---@public
---@param arg0 double
---@return double
function Math:sin(arg0) end

---@public
---@param arg0 double
---@return double
---@overload fun(arg0:float)
function Math:ulp(arg0) end

---@public
---@param arg0 float
---@return float
function Math:ulp(arg0) end
