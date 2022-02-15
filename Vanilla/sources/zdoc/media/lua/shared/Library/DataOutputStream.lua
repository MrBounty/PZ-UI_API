---@class DataOutputStream : java.io.DataOutputStream
---@field protected written int
---@field private bytearr byte[]
---@field private writeBuffer byte[]
DataOutputStream = {}

---@public
---@param arg0 int
---@return void
---@overload fun(arg0:byte[], arg1:int, arg2:int)
function DataOutputStream:write(arg0) end

---@public
---@param arg0 byte[]
---@param arg1 int
---@param arg2 int
---@return void
function DataOutputStream:write(arg0, arg1, arg2) end

---@public
---@param arg0 double
---@return void
function DataOutputStream:writeDouble(arg0) end

---@public
---@param arg0 String
---@return void
---@overload fun(arg0:String, arg1:DataOutput)
function DataOutputStream:writeUTF(arg0) end

---@param arg0 String
---@param arg1 DataOutput
---@return int
function DataOutputStream:writeUTF(arg0, arg1) end

---@public
---@param arg0 int
---@return void
function DataOutputStream:writeShort(arg0) end

---@public
---@return int
function DataOutputStream:size() end

---@private
---@param arg0 int
---@return void
function DataOutputStream:incCount(arg0) end

---@public
---@return void
function DataOutputStream:flush() end

---@public
---@param arg0 int
---@return void
function DataOutputStream:writeInt(arg0) end

---@private
---@param arg0 String
---@param arg1 int
---@return String
function DataOutputStream:tooLongMsg(arg0, arg1) end

---@public
---@param arg0 long
---@return void
function DataOutputStream:writeLong(arg0) end

---@public
---@param arg0 float
---@return void
function DataOutputStream:writeFloat(arg0) end

---@public
---@param arg0 int
---@return void
function DataOutputStream:writeChar(arg0) end

---@public
---@param arg0 int
---@return void
function DataOutputStream:writeByte(arg0) end

---@public
---@param arg0 String
---@return void
function DataOutputStream:writeBytes(arg0) end

---@public
---@param arg0 String
---@return void
function DataOutputStream:writeChars(arg0) end

---@public
---@param arg0 boolean
---@return void
function DataOutputStream:writeBoolean(arg0) end
