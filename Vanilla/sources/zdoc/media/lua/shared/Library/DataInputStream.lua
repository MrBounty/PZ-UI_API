---@class DataInputStream : java.io.DataInputStream
---@field private bytearr byte[]
---@field private chararr char[]
---@field private readBuffer byte[]
---@field private lineBuffer char[]
DataInputStream = {}

---@public
---@param arg0 byte[]
---@return int
---@overload fun(arg0:byte[], arg1:int, arg2:int)
function DataInputStream:read(arg0) end

---@public
---@param arg0 byte[]
---@param arg1 int
---@param arg2 int
---@return int
function DataInputStream:read(arg0, arg1, arg2) end

---@public
---@return char
function DataInputStream:readChar() end

---@public
---@return byte
function DataInputStream:readByte() end

---@public
---@return String
---@overload fun(arg0:DataInput)
function DataInputStream:readUTF() end

---@public
---@param arg0 DataInput
---@return String
function DataInputStream:readUTF(arg0) end

---@public
---@return int
function DataInputStream:readUnsignedShort() end

---@public
---@param arg0 int
---@return int
function DataInputStream:skipBytes(arg0) end

---@public
---@return short
function DataInputStream:readShort() end

---@public
---@param arg0 byte[]
---@return void
---@overload fun(arg0:byte[], arg1:int, arg2:int)
function DataInputStream:readFully(arg0) end

---@public
---@param arg0 byte[]
---@param arg1 int
---@param arg2 int
---@return void
function DataInputStream:readFully(arg0, arg1, arg2) end

---@public
---@return double
function DataInputStream:readDouble() end

---@public
---@return String
function DataInputStream:readLine() end

---@public
---@return long
function DataInputStream:readLong() end

---@public
---@return boolean
function DataInputStream:readBoolean() end

---@public
---@return int
function DataInputStream:readInt() end

---@public
---@return float
function DataInputStream:readFloat() end

---@public
---@return int
function DataInputStream:readUnsignedByte() end
