---@class ObjectTooltip.Layout : zombie.ui.ObjectTooltip.Layout
---@field public items ArrayList|ObjectTooltip.LayoutItem
---@field public minLabelWidth int
---@field public minValueWidth int
---@field public next ObjectTooltip.Layout
---@field public nextPadY int
---@field private freeItems Stack|Unknown
ObjectTooltip_Layout = {}

---@public
---@param left int
---@param top int
---@param ui ObjectTooltip
---@return int
function ObjectTooltip_Layout:render(left, top, ui) end

---@public
---@return void
function ObjectTooltip_Layout:free() end

---@public
---@return ObjectTooltip.LayoutItem
function ObjectTooltip_Layout:addItem() end

---@public
---@param minWidth int
---@return void
function ObjectTooltip_Layout:setMinLabelWidth(minWidth) end

---@public
---@param minWidth int
---@return void
function ObjectTooltip_Layout:setMinValueWidth(minWidth) end
