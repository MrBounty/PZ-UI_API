---@class ObjectTooltip : zombie.ui.ObjectTooltip
---@field public alphaStep float
---@field public bIsItem boolean
---@field public Item InventoryItem
---@field public Object IsoObject
---@field alpha float
---@field showDelay int
---@field targetAlpha float
---@field texture Texture
---@field public padRight int
---@field public padBottom int
---@field private character IsoGameCharacter
---@field private measureOnly boolean
---@field private weightOfStack float
---@field public lineSpacing int
---@field private fontSize String
---@field private font UIFont
---@field private freeLayouts Stack|Unknown
ObjectTooltip = {}

---@public
---@return float
function ObjectTooltip:getWeightOfStack() end

---@public
---@param arg0 float
---@return void
function ObjectTooltip:setWeightOfStack(arg0) end

---@public
---@param arg0 double
---@param arg1 double
---@return Boolean
function ObjectTooltip:onMouseMove(arg0, arg1) end

---@public
---@return void
function ObjectTooltip:hide() end

---@public
---@param x int
---@param y int
---@param w int
---@param h int
---@param f float
---@param r double
---@param g double
---@param b double
---@param a double
---@return void
function ObjectTooltip:DrawProgressBar(x, y, w, h, f, r, g, b, a) end

---@public
---@param arg0 IsoObject
---@param arg1 double
---@param arg2 double
---@return void
---@overload fun(arg0:InventoryItem, arg1:int, arg2:int)
function ObjectTooltip:show(arg0, arg1, arg2) end

---@param arg0 InventoryItem
---@param arg1 int
---@param arg2 int
---@return void
function ObjectTooltip:show(arg0, arg1, arg2) end

---@public
---@param arg0 UIFont
---@param arg1 String
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@param arg7 double
---@return void
function ObjectTooltip:DrawTextRight(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@param arg0 UIFont
---@param arg1 String
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@param arg7 double
---@return void
function ObjectTooltip:DrawText(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@return ObjectTooltip.Layout
function ObjectTooltip:beginLayout() end

---@public
---@return int
function ObjectTooltip:getLineSpacing() end

---@public
---@param value float
---@param x int
---@param y int
---@return void
---@overload fun(value:int, x:int, y:int)
function ObjectTooltip:DrawValueRightNoPlus(value, x, y) end

---@public
---@param value int
---@param x int
---@param y int
---@return void
function ObjectTooltip:DrawValueRightNoPlus(value, x, y) end

---@public
---@return void
function ObjectTooltip:checkFont() end

---@public
---@param arg0 Texture
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@return void
function ObjectTooltip:DrawTextureScaled(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param layout ObjectTooltip.Layout
---@return void
function ObjectTooltip:endLayout(layout) end

---@public
---@return IsoGameCharacter
function ObjectTooltip:getCharacter() end

---@public
---@param arg0 double
---@param arg1 double
---@return void
function ObjectTooltip:onMouseMoveOutside(arg0, arg1) end

---Overrides:
---
---update in class UIElement
---@public
---@return void
function ObjectTooltip:update() end

---Overrides:
---
---render in class UIElement
---@public
---@return void
function ObjectTooltip:render() end

---@public
---@param arg0 Texture
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@param arg7 double
---@param arg8 double
---@return void
function ObjectTooltip:DrawTextureScaledAspect(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@param value int
---@param x int
---@param y int
---@param highGood boolean
---@return void
function ObjectTooltip:DrawValueRight(value, x, y, highGood) end

---@public
---@return boolean
function ObjectTooltip:isMeasureOnly() end

---@public
---@return UIFont
function ObjectTooltip:getFont() end

---@public
---@param arg0 UIFont
---@param arg1 String
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@param arg7 double
---@return void
function ObjectTooltip:DrawTextCentre(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@param textX int
---@param text String
---@return void
function ObjectTooltip:adjustWidth(textX, text) end

---@public
---@param arg0 boolean
---@return void
function ObjectTooltip:setMeasureOnly(arg0) end

---@public
---@return Texture
function ObjectTooltip:getTexture() end

---@public
---@param chr IsoGameCharacter
---@return void
function ObjectTooltip:setCharacter(chr) end
