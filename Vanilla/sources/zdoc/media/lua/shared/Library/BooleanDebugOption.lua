---@class BooleanDebugOption : zombie.debug.BooleanDebugOption
---@field private m_parent IDebugOptionGroup
---@field private m_debugOnly boolean
BooleanDebugOption = {}

---@public
---@param arg0 IDebugOptionGroup
---@return void
function BooleanDebugOption:setParent(arg0) end

---@public
---@return IDebugOptionGroup
function BooleanDebugOption:getParent() end

---@public
---@return boolean
function BooleanDebugOption:isDebugOnly() end

---@public
---@return boolean
function BooleanDebugOption:getValue() end
