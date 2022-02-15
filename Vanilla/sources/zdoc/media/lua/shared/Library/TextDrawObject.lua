---@class TextDrawObject : zombie.ui.TextDrawObject
---@field private validImages String[]
---@field private validFonts String[]
---@field private lines ArrayList|Unknown
---@field private width int
---@field private height int
---@field private maxCharsLine int
---@field private defaultFontEnum UIFont
---@field private defaultFont AngelCodeFont
---@field private original String
---@field private unformatted String
---@field private currentLine TextDrawObject.DrawLine
---@field private currentElement TextDrawObject.DrawElement
---@field private hasOpened boolean
---@field private drawBackground boolean
---@field private allowImages boolean
---@field private allowChatIcons boolean
---@field private allowColors boolean
---@field private allowFonts boolean
---@field private allowBBcode boolean
---@field private allowAnyImage boolean
---@field private allowLineBreaks boolean
---@field private equalizeLineHeights boolean
---@field private enabled boolean
---@field private visibleRadius int
---@field private scrambleVal float
---@field private outlineR float
---@field private outlineG float
---@field private outlineB float
---@field private outlineA float
---@field private defaultR float
---@field private defaultG float
---@field private defaultB float
---@field private defaultA float
---@field private hearRange int
---@field private internalClock float
---@field private customTag String
---@field private customImageMaxDim int
---@field private defaultHorz TextDrawHorizontal
---@field private drawMode int
---@field private renderBatch ArrayList|Unknown
---@field private renderBatchPool ArrayDeque|Unknown
---@field private elemText String
TextDrawObject = {}

---@public
---@return float
function TextDrawObject:getScrambleVal() end

---@public
---@param arg0 boolean
---@return void
function TextDrawObject:setAllowLineBreaks(arg0) end

---@public
---@param arg0 boolean
---@return void
function TextDrawObject:setAllowChatIcons(arg0) end

---@public
---@param arg0 double
---@param arg1 double
---@return void
---@overload fun(arg0:double, arg1:double, arg2:boolean)
---@overload fun(arg0:double, arg1:double, arg2:boolean, arg3:float)
---@overload fun(arg0:double, arg1:double, arg2:double, arg3:double, arg4:double, arg5:double, arg6:boolean)
---@overload fun(arg0:TextDrawHorizontal, arg1:double, arg2:double, arg3:double, arg4:double, arg5:double, arg6:double, arg7:boolean)
function TextDrawObject:AddBatchedDraw(arg0, arg1) end

---@public
---@param arg0 double
---@param arg1 double
---@param arg2 boolean
---@return void
function TextDrawObject:AddBatchedDraw(arg0, arg1, arg2) end

---@public
---@param arg0 double
---@param arg1 double
---@param arg2 boolean
---@param arg3 float
---@return void
function TextDrawObject:AddBatchedDraw(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 double
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 boolean
---@return void
function TextDrawObject:AddBatchedDraw(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 TextDrawHorizontal
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@param arg7 boolean
---@return void
function TextDrawObject:AddBatchedDraw(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@param arg0 float
---@return void
function TextDrawObject:setScrambleVal(arg0) end

---@public
---@return String
function TextDrawObject:getOriginal() end

---@private
---@param arg0 String
---@return void
function TextDrawObject:addWord(arg0) end

---@public
---@return int
function TextDrawObject:getVisibleRadius() end

---@public
---@param arg0 String
---@return void
---@overload fun(arg0:String, arg1:int)
---@overload fun(arg0:UIFont, arg1:String, arg2:int)
function TextDrawObject:ReadString(arg0) end

---@public
---@param arg0 String
---@param arg1 int
---@return void
function TextDrawObject:ReadString(arg0, arg1) end

---@public
---@param arg0 UIFont
---@param arg1 String
---@param arg2 int
---@return void
function TextDrawObject:ReadString(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@return void
function TextDrawObject:setInternalTickClock(arg0) end

---@public
---@param arg0 double
---@param arg1 double
---@return void
---@overload fun(arg0:double, arg1:double, arg2:boolean)
---@overload fun(arg0:double, arg1:double, arg2:boolean, arg3:float)
---@overload fun(arg0:double, arg1:double, arg2:double, arg3:double, arg4:double, arg5:double, arg6:boolean)
---@overload fun(arg0:TextDrawHorizontal, arg1:double, arg2:double, arg3:double, arg4:double, arg5:double, arg6:double, arg7:boolean)
function TextDrawObject:Draw(arg0, arg1) end

---@public
---@param arg0 double
---@param arg1 double
---@param arg2 boolean
---@return void
function TextDrawObject:Draw(arg0, arg1, arg2) end

---@public
---@param arg0 double
---@param arg1 double
---@param arg2 boolean
---@param arg3 float
---@return void
function TextDrawObject:Draw(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 double
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 boolean
---@return void
function TextDrawObject:Draw(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 TextDrawHorizontal
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@param arg7 boolean
---@return void
function TextDrawObject:Draw(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@param arg0 boolean
---@return void
function TextDrawObject:setAllowBBcode(arg0) end

---@public
---@param arg0 String[]
---@return void
function TextDrawObject:setValidFonts(arg0) end

---@private
---@return void
function TextDrawObject:addNewLine() end

---@public
---@return int
function TextDrawObject:getWidth() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return void
---@overload fun(arg0:int, arg1:int, arg2:int)
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float)
---@overload fun(arg0:int, arg1:int, arg2:int, arg3:int)
function TextDrawObject:setDefaultColors(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function TextDrawObject:setDefaultColors(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function TextDrawObject:setDefaultColors(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return void
function TextDrawObject:setDefaultColors(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return void
---@overload fun(arg0:int, arg1:int, arg2:int)
---@overload fun(arg0:int, arg1:int, arg2:int, arg3:int)
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float)
function TextDrawObject:setOutlineColors(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function TextDrawObject:setOutlineColors(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return void
function TextDrawObject:setOutlineColors(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function TextDrawObject:setOutlineColors(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 boolean
---@return void
function TextDrawObject:setEnabled(arg0) end

---@private
---@param arg0 String
---@return void
function TextDrawObject:addText(arg0) end

---@public
---@param arg0 UIFont
---@return void
function TextDrawObject:setDefaultFont(arg0) end

---@public
---@return boolean
function TextDrawObject:getEnabled() end

---@public
---@param arg0 TextDrawHorizontal
---@return void
---@overload fun(arg0:String)
function TextDrawObject:setHorizontalAlign(arg0) end

---@public
---@param arg0 String
---@return void
function TextDrawObject:setHorizontalAlign(arg0) end

---@public
---@param arg0 boolean
---@param arg1 boolean
---@param arg2 boolean
---@param arg3 boolean
---@param arg4 boolean
---@param arg5 boolean
---@return void
function TextDrawObject:setSettings(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 int
---@return void
function TextDrawObject:NoRender(arg0) end

---@public
---@param arg0 int
---@return void
function TextDrawObject:setCustomImageMaxDimensions(arg0) end

---@public
---@param arg0 boolean
---@return void
function TextDrawObject:setDrawBackground(arg0) end

---@private
---@return void
function TextDrawObject:addNewElement() end

---@private
---@param arg0 char[]
---@param arg1 int
---@param arg2 String
---@return int
function TextDrawObject:readTag(arg0, arg1, arg2) end

---@private
---@param arg0 String
---@return boolean
function TextDrawObject:isValidFont(arg0) end

---@public
---@return String
function TextDrawObject:getCustomTag() end

---@public
---@param arg0 int
---@return void
function TextDrawObject:RenderBatch(arg0) end

---@private
---@param arg0 String
---@return int
function TextDrawObject:tryColorInt(arg0) end

---@public
---@return float
function TextDrawObject:getInternalClock() end

---@public
---@return boolean
function TextDrawObject:isNullOrZeroLength() end

---@public
---@return int
function TextDrawObject:getHearRange() end

---@public
---@param arg0 int
---@return void
function TextDrawObject:setHearRange(arg0) end

---@public
---@param arg0 TextDrawHorizontal
---@param arg1 double
---@param arg2 double
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 boolean
---@return void
function TextDrawObject:DrawRaw(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@private
---@param arg0 UIFont
---@return void
function TextDrawObject:setDefaultFontInternal(arg0) end

---@public
---@param arg0 int
---@return void
function TextDrawObject:setVisibleRadius(arg0) end

---@public
---@param arg0 boolean
---@return void
function TextDrawObject:setAllowFonts(arg0) end

---@public
---@return int
function TextDrawObject:getHeight() end

---@private
---@param arg0 char[]
---@param arg1 int
---@return String
function TextDrawObject:readTagValue(arg0, arg1) end

---@public
---@param arg0 boolean
---@return void
function TextDrawObject:setAllowAnyImage(arg0) end

---@public
---@param arg0 String[]
---@return void
function TextDrawObject:setValidImages(arg0) end

---@public
---@return void
function TextDrawObject:calculateDimensions() end

---@public
---@param arg0 boolean
---@return void
function TextDrawObject:setEqualizeLineHeights(arg0) end

---@public
---@return TextDrawHorizontal
function TextDrawObject:getHorizontalAlign() end

---@public
---@param arg0 boolean
---@return void
function TextDrawObject:setAllowImages(arg0) end

---@public
---@return float
---@overload fun(arg0:float)
function TextDrawObject:updateInternalTickClock() end

---@public
---@param arg0 float
---@return float
function TextDrawObject:updateInternalTickClock(arg0) end

---@private
---@param arg0 String
---@return boolean
function TextDrawObject:isValidImage(arg0) end

---@public
---@param arg0 int
---@return void
function TextDrawObject:setMaxCharsPerLine(arg0) end

---@private
---@return void
function TextDrawObject:reset() end

---@public
---@return void
function TextDrawObject:Clear() end

---@public
---@return String
function TextDrawObject:getUnformatted() end

---@public
---@param arg0 boolean
---@return void
function TextDrawObject:setAllowColors(arg0) end

---@public
---@return UIFont
function TextDrawObject:getDefaultFontEnum() end

---@public
---@param arg0 String
---@return void
function TextDrawObject:setCustomTag(arg0) end
