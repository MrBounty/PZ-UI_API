---@class Double : java.lang.Double
---@field public POSITIVE_INFINITY double
---@field public NEGATIVE_INFINITY double
---@field public NaN double
---@field public MAX_VALUE double
---@field public MIN_NORMAL double
---@field public MIN_VALUE double
---@field public MAX_EXPONENT int
---@field public MIN_EXPONENT int
---@field public SIZE int
---@field public BYTES int
---@field public TYPE Class|Unknown
---@field private value double
---@field private serialVersionUID long
Double = {}

---@public
---@return short
function Double:shortValue() end

---@public
---@param arg0 Object
---@return boolean
function Double:equals(arg0) end

---@public
---@return int
---@overload fun(arg0:double)
function Double:hashCode() end

---@public
---@param arg0 double
---@return int
function Double:hashCode(arg0) end

---@public
---@return boolean
---@overload fun(arg0:double)
function Double:isNaN() end

---@public
---@param arg0 double
---@return boolean
function Double:isNaN(arg0) end

---@public
---@param arg0 double
---@return Double
---@overload fun(arg0:String)
function Double:valueOf(arg0) end

---@public
---@param arg0 String
---@return Double
function Double:valueOf(arg0) end

---@public
---@param arg0 double
---@return long
function Double:doubleToLongBits(arg0) end

---@public
---@param arg0 String
---@return double
function Double:parseDouble(arg0) end

---@public
---@param arg0 double
---@param arg1 double
---@return double
function Double:min(arg0, arg1) end

---@public
---@return String
---@overload fun(arg0:double)
function Double:toString() end

---@public
---@param arg0 double
---@return String
function Double:toString(arg0) end

---@public
---@param arg0 double
---@return boolean
function Double:isFinite(arg0) end

---@public
---@return boolean
---@overload fun(arg0:double)
function Double:isInfinite() end

---@public
---@param arg0 double
---@return boolean
function Double:isInfinite(arg0) end

---@public
---@param arg0 long
---@return double
function Double:longBitsToDouble(arg0) end

---@public
---@param arg0 double
---@return String
function Double:toHexString(arg0) end

---@public
---@param arg0 double
---@param arg1 double
---@return double
function Double:max(arg0, arg1) end

---@public
---@param arg0 double
---@param arg1 double
---@return int
function Double:compare(arg0, arg1) end

---@public
---@return double
function Double:doubleValue() end

---@public
---@return float
function Double:floatValue() end

---@public
---@param arg0 MethodHandles.Lookup
---@return Double
function Double:resolveConstantDesc(arg0) end

---@public
---@param arg0 double
---@param arg1 double
---@return double
function Double:sum(arg0, arg1) end

---@public
---@return Optional|Unknown
function Double:describeConstable() end

---@public
---@return int
function Double:intValue() end

---@public
---@return long
function Double:longValue() end

---@public
---@param arg0 double
---@return long
function Double:doubleToRawLongBits(arg0) end

---@public
---@return byte
function Double:byteValue() end

---@public
---@param arg0 Double
---@return int
function Double:compareTo(arg0) end
