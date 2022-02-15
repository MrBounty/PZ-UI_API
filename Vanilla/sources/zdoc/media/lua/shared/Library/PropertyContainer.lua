---@class PropertyContainer : zombie.core.properties.PropertyContainer
---@field private SpriteFlags1 long
---@field private SpriteFlags2 long
---@field private Properties TIntIntHashMap
---@field private keyArray int[]
---@field public test NonBlockingHashMap|Unknown|Unknown
---@field public sorted List|Unknown
---@field private Surface byte
---@field private SurfaceFlags byte
---@field private StackReplaceTileOffset short
---@field private SURFACE_VALID byte
---@field private SURFACE_ISOFFSET byte
---@field private SURFACE_ISTABLE byte
---@field private SURFACE_ISTABLETOP byte
PropertyContainer = {}

---@public
---@return ArrayList|String
function PropertyContainer:getPropertyNames() end

---@public
---@param flag IsoFlagType
---@return boolean
---@overload fun(flag:Double)
---@overload fun(isoPropertyType:String)
function PropertyContainer:Is(flag) end

---@public
---@param flag Double
---@return boolean
function PropertyContainer:Is(flag) end

---@public
---@param isoPropertyType String
---@return boolean
function PropertyContainer:Is(isoPropertyType) end

---@public
---@return boolean
function PropertyContainer:isTable() end

---@public
---@return boolean
function PropertyContainer:isSurfaceOffset() end

---@public
---@return void
function PropertyContainer:CreateKeySet() end

---@private
---@return void
function PropertyContainer:initSurface() end

---@public
---@return void
function PropertyContainer:Clear() end

---@public
---@return ArrayList|Unknown
function PropertyContainer:getFlagsList() end

---@public
---@return int
function PropertyContainer:getStackReplaceTileOffset() end

---@public
---@param property String
---@return String
function PropertyContainer:Val(property) end

---@public
---@param propName IsoFlagType
---@return void
---@overload fun(propName:IsoFlagType, lazy:String)
---@overload fun(propName:String, propName2:String)
---@overload fun(propName:String, propName2:String, checkIsoFlagType:boolean)
function PropertyContainer:Set(propName) end

---@public
---@param propName IsoFlagType
---@param lazy String
---@return void
function PropertyContainer:Set(propName, lazy) end

---@public
---@param propName String
---@param propName2 String
---@return void
function PropertyContainer:Set(propName, propName2) end

---@public
---@param propName String
---@param propName2 String
---@param checkIsoFlagType boolean
---@return void
function PropertyContainer:Set(propName, propName2, checkIsoFlagType) end

---@public
---@return boolean
function PropertyContainer:isTableTop() end

---@public
---@return int
function PropertyContainer:getSurface() end

---@public
---@param other PropertyContainer
---@return void
function PropertyContainer:AddProperties(other) end

---@public
---@param propName IsoFlagType
---@return void
---@overload fun(propName:String)
function PropertyContainer:UnSet(propName) end

---@public
---@param propName String
---@return void
function PropertyContainer:UnSet(propName) end
