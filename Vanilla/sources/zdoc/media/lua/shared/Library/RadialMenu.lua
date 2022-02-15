---@class RadialMenu : zombie.ui.RadialMenu
---@field protected outerRadius int
---@field protected innerRadius int
---@field protected slices ArrayList|Unknown
---@field protected highlight int
---@field protected joypad int
---@field protected transition UITransition
---@field protected select UITransition
---@field protected deselect UITransition
---@field protected selectIndex int
---@field protected deselectIndex int
RadialMenu = {}

---@private
---@param arg0 String
---@return void
function RadialMenu:formatTextInsideCircle(arg0) end

---@public
---@return void
function RadialMenu:update() end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function RadialMenu:getSliceIndexFromMouse(arg0, arg1) end

---@public
---@return void
function RadialMenu:render() end

---@private
---@param arg0 int
---@return RadialMenu.Slice
function RadialMenu:getSlice(arg0) end

---@public
---@param arg0 int
---@param arg1 Texture
---@return void
function RadialMenu:setSliceTexture(arg0, arg1) end

---@private
---@return float
function RadialMenu:getStartAngle() end

---@public
---@param arg0 int
---@param arg1 String
---@return void
function RadialMenu:setSliceText(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 Texture
---@return void
function RadialMenu:addSlice(arg0, arg1) end

---@public
---@param arg0 int
---@return int
function RadialMenu:getSliceIndexFromJoypad(arg0) end

---@public
---@return void
function RadialMenu:clear() end

---@public
---@param arg0 int
---@return void
function RadialMenu:setJoypad(arg0) end
