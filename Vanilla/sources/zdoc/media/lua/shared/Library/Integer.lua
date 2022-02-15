---@class Integer : java.lang.Integer
---@field public MIN_VALUE int
---@field public MAX_VALUE int
---@field public TYPE Class|Unknown
---@field digits char[]
---@field DigitTens byte[]
---@field DigitOnes byte[]
---@field sizeTable int[]
---@field private value int
---@field public SIZE int
---@field public BYTES int
---@field private serialVersionUID long
Integer = {}

---@public
---@return String
---@overload fun(arg0:int)
---@overload fun(arg0:int, arg1:int)
function Integer:toString() end

---@public
---@param arg0 int
---@return String
function Integer:toString(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@return String
function Integer:toString(arg0, arg1) end

---@public
---@return int
function Integer:intValue() end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Integer:min(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Integer:rotateRight(arg0, arg1) end

---@public
---@param arg0 int
---@return long
function Integer:toUnsignedLong(arg0) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 byte[]
---@param arg3 int
---@return void
function Integer:formatUnsignedIntUTF16(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@return String
---@overload fun(arg0:int, arg1:int)
function Integer:toUnsignedString(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@return String
function Integer:toUnsignedString(arg0, arg1) end

---@public
---@param arg0 String
---@return int
---@overload fun(arg0:String, arg1:int)
---@overload fun(arg0:CharSequence, arg1:int, arg2:int, arg3:int)
function Integer:parseInt(arg0) end

---@public
---@param arg0 String
---@param arg1 int
---@return int
function Integer:parseInt(arg0, arg1) end

---@public
---@param arg0 CharSequence
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return int
function Integer:parseInt(arg0, arg1, arg2, arg3) end

---@param arg0 int
---@return int
function Integer:stringSize(arg0) end

---@public
---@return float
function Integer:floatValue() end

---@public
---@param arg0 int
---@return int
function Integer:numberOfTrailingZeros(arg0) end

---@public
---@param arg0 String
---@return int
---@overload fun(arg0:String, arg1:int)
---@overload fun(arg0:CharSequence, arg1:int, arg2:int, arg3:int)
function Integer:parseUnsignedInt(arg0) end

---@public
---@param arg0 String
---@param arg1 int
---@return int
function Integer:parseUnsignedInt(arg0, arg1) end

---@public
---@param arg0 CharSequence
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return int
function Integer:parseUnsignedInt(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@return Integer
---@overload fun(arg0:int)
---@overload fun(arg0:String, arg1:int)
function Integer:valueOf(arg0) end

---@public
---@param arg0 int
---@return Integer
function Integer:valueOf(arg0) end

---@public
---@param arg0 String
---@param arg1 int
---@return Integer
function Integer:valueOf(arg0, arg1) end

---@param arg0 int
---@param arg1 int
---@param arg2 byte[]
---@return int
function Integer:getChars(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@return Integer
---@overload fun(arg0:String, arg1:int)
---@overload fun(arg0:String, arg1:Integer)
function Integer:getInteger(arg0) end

---@public
---@param arg0 String
---@param arg1 int
---@return Integer
function Integer:getInteger(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 Integer
---@return Integer
function Integer:getInteger(arg0, arg1) end

---@public
---@param arg0 int
---@return int
function Integer:bitCount(arg0) end

---@public
---@return int
---@overload fun(arg0:int)
function Integer:hashCode() end

---@public
---@param arg0 int
---@return int
function Integer:hashCode(arg0) end

---@public
---@param arg0 MethodHandles.Lookup
---@return Integer
function Integer:resolveConstantDesc(arg0) end

---@public
---@param arg0 int
---@return int
function Integer:reverseBytes(arg0) end

---@private
---@param arg0 int
---@param arg1 int
---@return String
function Integer:toUnsignedString0(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Integer:compare(arg0, arg1) end

---@public
---@return short
function Integer:shortValue() end

---@public
---@param arg0 Object
---@return boolean
function Integer:equals(arg0) end

---@public
---@param arg0 Integer
---@return int
function Integer:compareTo(arg0) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 byte[]
---@param arg3 int
---@return void
function Integer:formatUnsignedInt(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@return String
function Integer:toBinaryString(arg0) end

---@public
---@param arg0 int
---@return String
function Integer:toHexString(arg0) end

---@public
---@return long
function Integer:longValue() end

---@public
---@param arg0 int
---@return String
function Integer:toOctalString(arg0) end

---@public
---@param arg0 int
---@return int
function Integer:reverse(arg0) end

---@public
---@return Optional|Unknown
function Integer:describeConstable() end

---@public
---@return double
function Integer:doubleValue() end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Integer:rotateLeft(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Integer:sum(arg0, arg1) end

---@public
---@param arg0 int
---@return int
function Integer:highestOneBit(arg0) end

---@public
---@param arg0 int
---@return int
function Integer:numberOfLeadingZeros(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Integer:remainderUnsigned(arg0, arg1) end

---@private
---@param arg0 int
---@param arg1 int
---@return String
function Integer:toStringUTF16(arg0, arg1) end

---@public
---@param arg0 String
---@return Integer
function Integer:decode(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Integer:compareUnsigned(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Integer:max(arg0, arg1) end

---@public
---@param arg0 int
---@return int
function Integer:signum(arg0) end

---@public
---@return byte
function Integer:byteValue() end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Integer:divideUnsigned(arg0, arg1) end

---@public
---@param arg0 int
---@return int
function Integer:lowestOneBit(arg0) end
