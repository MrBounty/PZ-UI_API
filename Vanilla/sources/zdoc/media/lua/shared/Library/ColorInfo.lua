---@class ColorInfo : zombie.core.textures.ColorInfo
---@field public a float
---@field public b float
---@field public g float
---@field public r float
ColorInfo = {}

---@public
---@return float
function ColorInfo:getR() end

---@public
---@return float
function ColorInfo:getG() end

---@public
---@return float
function ColorInfo:getB() end

---@public
---@param s float
---@return void
function ColorInfo:desaturate(s) end

---@public
---@param arg0 ColorInfo
---@return ColorInfo
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float)
function ColorInfo:set(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return ColorInfo
function ColorInfo:set(arg0, arg1, arg2, arg3) end

---@public
---@return Color
function ColorInfo:toColor() end

---@public
---@return float
function ColorInfo:getA() end

---@public
---@return ImmutableColor
function ColorInfo:toImmutableColor() end

---@public
---@return String
function ColorInfo:toString() end

---@public
---@param arg0 ColorInfo
---@param arg1 float
---@param arg2 ColorInfo
---@return void
function ColorInfo:interp(arg0, arg1, arg2) end
