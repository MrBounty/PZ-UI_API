---@class AngelCodeFont : zombie.core.fonts.AngelCodeFont
---@field private DISPLAY_LIST_CACHE_SIZE int
---@field private MAX_CHAR int
---@field private baseDisplayListID int
---@field public chars AngelCodeFont.CharDef[] @The characters building up the font
---@field private displayListCaching boolean
---@field private eldestDisplayList AngelCodeFont.DisplayList
---@field private eldestDisplayListID int
---@field private displayLists LinkedHashMap|Unknown|Unknown
---@field private fontImage Texture
---@field private lineHeight int
---@field private pages HashMap|Unknown|Unknown
---@field private fntFile File
---@field public xoff int
---@field public yoff int
---@field public curCol Color
---@field public curR float
---@field public curG float
---@field public curB float
---@field public curA float
---@field private s_scale float
---@field private data char[]
AngelCodeFont = {}

---Description copied from interface: Font
---
---Draw a string to the screen
---
---Specified by:
---
---drawString in interface Font
---@public
---@param x float @The x location at which to draw the string
---@param y float @The y location at which to draw the string
---@param text String @The text to be displayed
---@return void
---@overload fun(x:float, y:float, text:String, col:Color)
---@overload fun(x:float, y:float, text:String, col:Color, startIndex:int, endIndex:int)
---@overload fun(x:float, y:float, text:String, r:float, g:float, b:float, a:float)
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:String, arg4:float, arg5:float, arg6:float, arg7:float)
---@overload fun(x:float, y:float, text:String, r:float, g:float, b:float, a:float, startIndex:int, endIndex:int)
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:String, arg4:float, arg5:float, arg6:float, arg7:float, arg8:int, arg9:int)
function AngelCodeFont:drawString(x, y, text) end

---Description copied from interface: Font
---
---Draw a string to the screen
---
---Specified by:
---
---drawString in interface Font
---@public
---@param x float @The x location at which to draw the string
---@param y float @The y location at which to draw the string
---@param text String @The text to be displayed
---@param col Color @The colour to draw with
---@return void
function AngelCodeFont:drawString(x, y, text, col) end

---Description copied from interface: Font
---
---Draw part of a string to the screen. Note that this will still position the text as though it's part of the bigger string.
---
---Specified by:
---
---drawString in interface Font
---@public
---@param x float @The x location at which to draw the string
---@param y float @The y location at which to draw the string
---@param text String @The text to be displayed
---@param col Color @The colour to draw with
---@param startIndex int @The index of the first character to draw
---@param endIndex int @The index of the last character from the string to draw
---@return void
function AngelCodeFont:drawString(x, y, text, col, startIndex, endIndex) end

---@public
---@param x float
---@param y float
---@param text String
---@param r float
---@param g float
---@param b float
---@param a float
---@return void
function AngelCodeFont:drawString(x, y, text, r, g, b, a) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 String
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@return void
function AngelCodeFont:drawString(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@param x float
---@param y float
---@param text String
---@param r float
---@param g float
---@param b float
---@param a float
---@param startIndex int
---@param endIndex int
---@return void
function AngelCodeFont:drawString(x, y, text, r, g, b, a, startIndex, endIndex) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 String
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 int
---@param arg9 int
---@return void
function AngelCodeFont:drawString(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) end

---Description copied from interface: Font
---
---get the maximum height of any line drawn by this font
---
---Specified by:
---
---getLineHeight in interface Font
---@public
---@return int @The maxium height of any line drawn by this font
function AngelCodeFont:getLineHeight() end

---@public
---@return boolean
function AngelCodeFont:isEmpty() end

---@public
---@param arg0 Asset.State
---@param arg1 Asset.State
---@param arg2 Asset
---@return void
function AngelCodeFont:onStateChanged(arg0, arg1, arg2) end

---Description copied from interface: Font
---
---get the width of the given string
---
---Specified by:
---
---getWidth in interface Font
---@public
---@param text String @The string to obtain the rendered with of
---@return int
---@overload fun(arg0:String, arg1:boolean)
---@overload fun(arg0:String, arg1:int, arg2:int)
---@overload fun(arg0:String, arg1:int, arg2:int, arg3:boolean)
function AngelCodeFont:getWidth(text) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return int
function AngelCodeFont:getWidth(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@return int
function AngelCodeFont:getWidth(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@param arg3 boolean
---@return int
function AngelCodeFont:getWidth(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 String
---@return AngelCodeFont.CharDef
function AngelCodeFont:parseChar(arg0) end

---@private
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@return void
function AngelCodeFont:render(arg0, arg1, arg2) end

---@public
---@return void
function AngelCodeFont:destroy() end

---Returns the distance from the y drawing location to the top most pixel of the specified text.
---@public
---@param text String @The text that is to be tested
---@return int @The yoffset from the y draw location at which text will start
function AngelCodeFont:getYOffset(text) end

---@private
---@param arg0 InputStream
---@return void
function AngelCodeFont:parseFnt(arg0) end

---Description copied from interface: Font
---
---get the height of the given string
---
---Specified by:
---
---getHeight in interface Font
---@public
---@param text String @The string to obtain the rendered with of
---@return int @The width of the given string
function AngelCodeFont:getHeight(text) end
