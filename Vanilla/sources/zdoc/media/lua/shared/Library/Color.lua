---@class Color : zombie.core.Color
---@field private serialVersionUID long
---@field public transparent Color @The fixed color transparent
---@field public white Color @The fixed colour white
---@field public yellow Color @The fixed colour yellow
---@field public red Color @The fixed colour red
---@field public purple Color
---@field public blue Color @The fixed colour blue
---@field public green Color @The fixed colour green
---@field public black Color @The fixed colour black
---@field public gray Color @The fixed colour gray
---@field public cyan Color @The fixed colour cyan
---@field public darkGray Color @The fixed colour dark gray
---@field public lightGray Color @The fixed colour light gray
---@field public pink Color @The fixed colour dark pink
---@field public orange Color @The fixed colour dark orange
---@field public magenta Color @The fixed colour dark magenta
---@field public darkGreen Color
---@field public lightGreen Color
---@field public a float @The alpha component of the colour
---@field public b float @The blue component of the colour
---@field public g float @The green component of the colour
---@field public r float @The red component of the colour
Color = {}

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Color:tintABGR(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Color:multiplyBGR(arg0, arg1) end

---@public
---@param value int
---@return void
function Color:fromColor(value) end

---@public
---@param arg0 int
---@param arg1 float
---@return int
function Color:setRedChannelToABGR(arg0, arg1) end

---Make a darker instance of this colour
---@public
---@return Color
---@overload fun(scale:float)
function Color:darker() end

---Make a darker instance of this colour
---@public
---@param scale float @The scale down of RGB (i.e. if you supply 0.03 the colour will be darkened by 3%)
---@return Color @The darker version of this colour
function Color:darker(scale) end

---@public
---@param arg0 float
---@return Color
function Color:scale(arg0) end

---Add another colour to this one
---@public
---@param c Color @The colour to add
---@return Color @The copy which has had the color added to it
function Color:addToCopy(c) end

---@public
---@param arg0 int
---@param arg1 float
---@return int
function Color:setGreenChannelToABGR(arg0, arg1) end

---@public
---@return float
function Color:getRedFloat() end

---@public
---@param to Color
---@param delta float
---@param dest Color
---@return void
function Color:interp(to, delta, dest) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 float
---@return int
function Color:lerpABGR(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return void
function Color:changeHSBValue(arg0, arg1, arg2) end

---Scale the components of the colour by the given value
---@public
---@param value float @The value to scale by
---@return Color @The copy which has been scaled
function Color:scaleCopy(value) end

---@public
---@param arg0 int
---@param arg1 float
---@return int
function Color:setBlueChannelToABGR(arg0, arg1) end

---Overrides:
---
---toString in class java.lang.Object
---@public
---@return String
function Color:toString() end

---@public
---@param arg0 int
---@param arg1 float
---@return int
function Color:setAlphaChannelToABGR(arg0, arg1) end

---get the red byte component of this colour
---@public
---@return int @The red component (range 0-255)
function Color:getRedByte() end

---@public
---@param arg0 int
---@return float
function Color:getAlphaChannelFromABGR(arg0) end

---get the green byte component of this colour
---@public
---@return int @The green component (range 0-255)
function Color:getGreenByte() end

---Overrides:
---
---hashCode in class java.lang.Object
---@public
---@return int
function Color:hashCode() end

---@public
---@param arg0 Color
---@return int
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float)
function Color:colorToABGR(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return int
function Color:colorToABGR(arg0, arg1, arg2, arg3) end

---@public
---@return float
function Color:getAlphaFloat() end

---Make a brighter instance of this colour
---@public
---@return Color
---@overload fun(scale:float)
function Color:brighter() end

---Make a brighter instance of this colour
---@public
---@param scale float @The scale up of RGB (i.e. if you supply 0.03 the colour will be brightened by 3%)
---@return Color @The brighter version of this colour
function Color:brighter(scale) end

---@public
---@param arg0 Color
---@return Color
---@overload fun(arg0:float, arg1:float, arg2:float)
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float)
function Color:set(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return Color
function Color:set(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return Color
function Color:set(arg0, arg1, arg2, arg3) end

---get the blue byte component of this colour
---@public
---@return int @The blue component (range 0-255)
function Color:getBlue() end

---get the green byte component of this colour
---@public
---@return int @The green component (range 0-255)
function Color:getGreen() end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Color:blendABGR(arg0, arg1) end

---Multiply this color by another
---@public
---@param c Color @the other color
---@return Color @product of the two colors
function Color:multiply(c) end

---@public
---@param arg0 int
---@return float
function Color:getBlueChannelFromABGR(arg0) end

---Overrides:
---
---equals in class java.lang.Object
---@public
---@param other Object
---@return boolean
function Color:equals(other) end

---@public
---@return float
function Color:getBlueFloat() end

---Add another colour to this one
---@public
---@param c Color @The colour to add
---@return void
function Color:add(c) end

---@public
---@param arg0 int
---@return void
function Color:setABGR(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Color:blendBGR(arg0, arg1) end

---@public
---@return Color
function Color:random() end

---get the blue byte component of this colour
---@public
---@return int @The blue component (range 0-255)
function Color:getBlueByte() end

---@public
---@param arg0 int
---@return float
function Color:getGreenChannelFromABGR(arg0) end

---@public
---@param A Color
---@param B Color
---@param delta float
---@return void
function Color:setColor(A, B, delta) end

---Decode a number in a string and process it as a colour
---
---reference.
---@public
---@param nm String @The number string to decode
---@return Color @The color generated from the number read
function Color:decode(nm) end

---@public
---@param arg0 int
---@return float
function Color:getRedChannelFromABGR(arg0) end

---get the alpha byte component of this colour
---@public
---@return int @The alpha component (range 0-255)
function Color:getAlpha() end

---@public
---@return float
function Color:getGreenFloat() end

---get the red byte component of this colour
---@public
---@return int @The red component (range 0-255)
function Color:getRed() end

---@public
---@param arg0 int
---@param arg1 Color
---@return Color
function Color:abgrToColor(arg0, arg1) end

---get the alpha byte component of this colour
---@public
---@return int @The alpha component (range 0-255)
function Color:getAlphaByte() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return Color
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:Color)
function Color:HSBtoRGB(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 Color
---@return Color
function Color:HSBtoRGB(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function Color:multiplyABGR(arg0, arg1) end
