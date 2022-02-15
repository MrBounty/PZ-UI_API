---@class ObjectTooltip.LayoutItem : zombie.ui.ObjectTooltip.LayoutItem
---@field public label String
---@field public r0 float
---@field public g0 float
---@field public b0 float
---@field public a0 float
---@field public hasValue boolean
---@field public value String
---@field public rightJustify boolean
---@field public r1 float
---@field public g1 float
---@field public b1 float
---@field public a1 float
---@field public progressFraction float
---@field public labelWidth int
---@field public valueWidth int
---@field public valueWidthRight int
---@field public progressWidth int
---@field public height int
ObjectTooltip_LayoutItem = {}

---@public
---@param value float
---@return void
---@overload fun(value:int)
function ObjectTooltip_LayoutItem:setValueRightNoPlus(value) end

---@public
---@param value int
---@return void
function ObjectTooltip_LayoutItem:setValueRightNoPlus(value) end

---@public
---@param x int
---@param y int
---@param mid int
---@param right int
---@param ui ObjectTooltip
---@return void
function ObjectTooltip_LayoutItem:render(x, y, mid, right, ui) end

---@public
---@param label String
---@param r float
---@param g float
---@param b float
---@param a float
---@return void
function ObjectTooltip_LayoutItem:setValue(label, r, g, b, a) end

---@public
---@param value int
---@param highGood boolean
---@return void
function ObjectTooltip_LayoutItem:setValueRight(value, highGood) end

---@public
---@param label String
---@param r float
---@param g float
---@param b float
---@param a float
---@return void
function ObjectTooltip_LayoutItem:setLabel(label, r, g, b, a) end

---@public
---@return void
function ObjectTooltip_LayoutItem:reset() end

---@public
---@return void
function ObjectTooltip_LayoutItem:calcSizes() end

---@public
---@param fraction float
---@param r float
---@param g float
---@param b float
---@param a float
---@return void
function ObjectTooltip_LayoutItem:setProgress(fraction, r, g, b, a) end
