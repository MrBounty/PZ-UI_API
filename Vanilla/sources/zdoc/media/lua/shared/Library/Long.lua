---@class Long : java.lang.Long
---@field public MIN_VALUE long
---@field public MAX_VALUE long
---@field public TYPE Class|Unknown
---@field private value long
---@field public SIZE int
---@field public BYTES int
---@field private serialVersionUID long
Long = {}

---@public
---@return double
function Long:doubleValue() end

---@public
---@param arg0 long
---@return String
---@overload fun(arg0:long, arg1:int)
function Long:toUnsignedString(arg0) end

---@public
---@param arg0 long
---@param arg1 int
---@return String
function Long:toUnsignedString(arg0, arg1) end

---@private
---@param arg0 long
---@param arg1 int
---@param arg2 byte[]
---@param arg3 int
---@param arg4 int
---@return void
function Long:formatUnsignedLong0UTF16(arg0, arg1, arg2, arg3, arg4) end

---@param arg0 long
---@param arg1 long
---@return String
function Long:fastUUID(arg0, arg1) end

---@public
---@return long
function Long:longValue() end

---@public
---@param arg0 String
---@return long
---@overload fun(arg0:String, arg1:int)
---@overload fun(arg0:CharSequence, arg1:int, arg2:int, arg3:int)
function Long:parseUnsignedLong(arg0) end

---@public
---@param arg0 String
---@param arg1 int
---@return long
function Long:parseUnsignedLong(arg0, arg1) end

---@public
---@param arg0 CharSequence
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return long
function Long:parseUnsignedLong(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 long
---@param arg1 int
---@return String
function Long:toStringUTF16(arg0, arg1) end

---@public
---@param arg0 long
---@return Long
---@overload fun(arg0:String)
---@overload fun(arg0:String, arg1:int)
function Long:valueOf(arg0) end

---@public
---@param arg0 String
---@return Long
function Long:valueOf(arg0) end

---@public
---@param arg0 String
---@param arg1 int
---@return Long
function Long:valueOf(arg0, arg1) end

---@private
---@param arg0 long
---@return BigInteger
function Long:toUnsignedBigInteger(arg0) end

---@public
---@param arg0 long
---@return String
function Long:toHexString(arg0) end

---@public
---@return byte
function Long:byteValue() end

---@param arg0 long
---@param arg1 int
---@return String
function Long:toUnsignedString0(arg0, arg1) end

---@public
---@param arg0 long
---@return String
function Long:toBinaryString(arg0) end

---@public
---@param arg0 String
---@return long
---@overload fun(arg0:String, arg1:int)
---@overload fun(arg0:CharSequence, arg1:int, arg2:int, arg3:int)
function Long:parseLong(arg0) end

---@public
---@param arg0 String
---@param arg1 int
---@return long
function Long:parseLong(arg0, arg1) end

---@public
---@param arg0 CharSequence
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return long
function Long:parseLong(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@return Long
---@overload fun(arg0:String, arg1:Long)
---@overload fun(arg0:String, arg1:long)
function Long:getLong(arg0) end

---@public
---@param arg0 String
---@param arg1 Long
---@return Long
function Long:getLong(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 long
---@return Long
function Long:getLong(arg0, arg1) end

---@public
---@return int
---@overload fun(arg0:long)
function Long:hashCode() end

---@public
---@param arg0 long
---@return int
function Long:hashCode(arg0) end

---@public
---@param arg0 Object
---@return boolean
function Long:equals(arg0) end

---@public
---@param arg0 long
---@param arg1 int
---@return long
function Long:rotateLeft(arg0, arg1) end

---@public
---@param arg0 long
---@param arg1 long
---@return int
function Long:compareUnsigned(arg0, arg1) end

---@public
---@return String
---@overload fun(arg0:long)
---@overload fun(arg0:long, arg1:int)
function Long:toString() end

---@public
---@param arg0 long
---@return String
function Long:toString(arg0) end

---@public
---@param arg0 long
---@param arg1 int
---@return String
function Long:toString(arg0, arg1) end

---@public
---@param arg0 long
---@param arg1 long
---@return int
function Long:compare(arg0, arg1) end

---@public
---@param arg0 Long
---@return int
function Long:compareTo(arg0) end

---@public
---@param arg0 long
---@return int
function Long:bitCount(arg0) end

---@public
---@param arg0 long
---@param arg1 long
---@return long
function Long:sum(arg0, arg1) end

---@public
---@param arg0 MethodHandles.Lookup
---@return Long
function Long:resolveConstantDesc(arg0) end

---@public
---@return float
function Long:floatValue() end

---@param arg0 long
---@return int
function Long:stringSize(arg0) end

---@private
---@param arg0 long
---@param arg1 int
---@param arg2 byte[]
---@param arg3 int
---@param arg4 int
---@return void
function Long:formatUnsignedLong0(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 long
---@return long
function Long:reverse(arg0) end

---@public
---@param arg0 long
---@param arg1 long
---@return long
function Long:divideUnsigned(arg0, arg1) end

---@public
---@param arg0 long
---@return long
function Long:highestOneBit(arg0) end

---@param arg0 long
---@param arg1 int
---@param arg2 byte[]
---@return int
function Long:getChars(arg0, arg1, arg2) end

---@public
---@param arg0 long
---@return long
function Long:lowestOneBit(arg0) end

---@public
---@param arg0 String
---@return Long
function Long:decode(arg0) end

---@public
---@return int
function Long:intValue() end

---@public
---@param arg0 long
---@return int
function Long:signum(arg0) end

---@public
---@return short
function Long:shortValue() end

---@public
---@param arg0 long
---@return int
function Long:numberOfTrailingZeros(arg0) end

---@public
---@param arg0 long
---@return int
function Long:numberOfLeadingZeros(arg0) end

---@public
---@param arg0 long
---@return String
function Long:toOctalString(arg0) end

---@public
---@param arg0 long
---@param arg1 int
---@return long
function Long:rotateRight(arg0, arg1) end

---@public
---@param arg0 long
---@param arg1 long
---@return long
function Long:max(arg0, arg1) end

---@public
---@param arg0 long
---@param arg1 long
---@return long
function Long:min(arg0, arg1) end

---@public
---@param arg0 long
---@return long
function Long:reverseBytes(arg0) end

---@public
---@param arg0 long
---@param arg1 long
---@return long
function Long:remainderUnsigned(arg0, arg1) end

---@public
---@return Optional|Unknown
function Long:describeConstable() end
