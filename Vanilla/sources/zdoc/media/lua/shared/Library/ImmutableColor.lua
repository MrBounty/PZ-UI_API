---@class ImmutableColor : zombie.core.ImmutableColor
---@field public transparent ImmutableColor
---@field public white ImmutableColor
---@field public yellow ImmutableColor
---@field public red ImmutableColor
---@field public purple ImmutableColor
---@field public blue ImmutableColor
---@field public green ImmutableColor
---@field public black ImmutableColor
---@field public gray ImmutableColor
---@field public cyan ImmutableColor
---@field public darkGray ImmutableColor
---@field public lightGray ImmutableColor
---@field public pink ImmutableColor
---@field public orange ImmutableColor
---@field public magenta ImmutableColor
---@field public darkGreen ImmutableColor
---@field public lightGreen ImmutableColor
---@field public a float
---@field public b float
---@field public g float
---@field public r float
ImmutableColor = {}

---@public
---@return int
function ImmutableColor:hashCode() end

---@public
---@param arg0 float
---@return ImmutableColor
function ImmutableColor:scale(arg0) end

---@public
---@return float
function ImmutableColor:getGreenFloat() end

---@public
---@return ImmutableColor
---@overload fun(arg0:float)
function ImmutableColor:darker() end

---@public
---@param arg0 float
---@return ImmutableColor
function ImmutableColor:darker(arg0) end

---@public
---@return int
function ImmutableColor:getBlueInt() end

---@public
---@return String
function ImmutableColor:toString() end

---@public
---@param arg0 String
---@return ImmutableColor
function ImmutableColor:decode(arg0) end

---@public
---@return ImmutableColor
function ImmutableColor:random() end

---@public
---@return Color
function ImmutableColor:toMutableColor() end

---@public
---@return float
function ImmutableColor:getAlphaFloat() end

---@public
---@return byte
function ImmutableColor:getAlphaByte() end

---@public
---@return float
function ImmutableColor:getRedFloat() end

---@public
---@param arg0 Object
---@return boolean
function ImmutableColor:equals(arg0) end

---@public
---@return int
function ImmutableColor:getGreenInt() end

---@public
---@param arg0 Color
---@return ImmutableColor
function ImmutableColor:multiply(arg0) end

---@public
---@return ImmutableColor
---@overload fun(arg0:float)
function ImmutableColor:brighter() end

---@public
---@param arg0 float
---@return ImmutableColor
function ImmutableColor:brighter(arg0) end

---@public
---@return byte
function ImmutableColor:getGreenByte() end

---@public
---@return int
function ImmutableColor:getRedInt() end

---@public
---@return int
function ImmutableColor:getAlphaInt() end

---@public
---@return byte
function ImmutableColor:getRedByte() end

---@public
---@return float
function ImmutableColor:getBlueFloat() end

---@public
---@param arg0 ImmutableColor
---@return ImmutableColor
function ImmutableColor:add(arg0) end

---@public
---@param arg0 ImmutableColor
---@param arg1 float
---@return ImmutableColor
function ImmutableColor:interp(arg0, arg1) end

---@public
---@return byte
function ImmutableColor:getBlueByte() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return Integer[]
function ImmutableColor:HSBtoRGB(arg0, arg1, arg2) end
