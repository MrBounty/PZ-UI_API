---@class EnumMap : java.util.EnumMap
---@field private keyType Class|Unknown
---@field private keyUniverse Enum[]
---@field private vals Object[]
---@field private size int
---@field private NULL Object
---@field private entrySet Set|Unknown
---@field private serialVersionUID long
EnumMap = {}

---@private
---@param arg0 Class|Unknown
---@return Enum[]
function EnumMap:getKeyUniverse(arg0) end

---@public
---@param arg0 Object
---@return boolean
function EnumMap:containsValue(arg0) end

---@private
---@param arg0 ObjectInputStream
---@return void
function EnumMap:readObject(arg0) end

---@public
---@return int
function EnumMap:size() end

---@public
---@param arg0 Object
---@return Object
function EnumMap:get(arg0) end

---@private
---@param arg0 Object
---@param arg1 Object
---@return boolean
function EnumMap:removeMapping(arg0, arg1) end

---@public
---@return Set|Unknown
function EnumMap:entrySet() end

---@private
---@param arg0 Enum|Unknown
---@return void
function EnumMap:typeCheck(arg0) end

---@public
---@param arg0 Enum|Unknown
---@param arg1 Object
---@return Object
function EnumMap:put(arg0, arg1) end

---@private
---@param arg0 Object
---@return boolean
function EnumMap:isValidKey(arg0) end

---@public
---@return EnumMap|Unknown|Unknown
function EnumMap:clone() end

---@public
---@return Set|Unknown
function EnumMap:keySet() end

---@private
---@param arg0 EnumMap|Unknown|Unknown
---@return boolean
---@overload fun(arg0:Object)
function EnumMap:equals(arg0) end

---@public
---@param arg0 Object
---@return boolean
function EnumMap:equals(arg0) end

---@private
---@param arg0 Object
---@return Object
function EnumMap:unmaskNull(arg0) end

---@public
---@param arg0 Map|Unknown|Unknown
---@return void
function EnumMap:putAll(arg0) end

---@public
---@return void
function EnumMap:clear() end

---@public
---@return int
function EnumMap:hashCode() end

---@private
---@param arg0 ObjectOutputStream
---@return void
function EnumMap:writeObject(arg0) end

---@public
---@param arg0 Object
---@return Object
function EnumMap:remove(arg0) end

---@public
---@return Collection|Unknown
function EnumMap:values() end

---@private
---@param arg0 int
---@return int
function EnumMap:entryHashCode(arg0) end

---@public
---@param arg0 Object
---@return boolean
function EnumMap:containsKey(arg0) end

---@private
---@param arg0 Object
---@param arg1 Object
---@return boolean
function EnumMap:containsMapping(arg0, arg1) end

---@private
---@param arg0 Object
---@return Object
function EnumMap:maskNull(arg0) end
