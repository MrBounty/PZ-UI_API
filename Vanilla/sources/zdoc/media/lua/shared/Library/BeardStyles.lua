---@class BeardStyles : zombie.core.skinnedmodel.population.BeardStyles
---@field public m_Styles ArrayList|Unknown
---@field public instance BeardStyles
BeardStyles = {}

---@public
---@return BeardStyles
function BeardStyles:getInstance() end

---@public
---@param arg0 String
---@return String
function BeardStyles:getRandomStyle(arg0) end

---@public
---@param arg0 String
---@return BeardStyles
function BeardStyles:Parse(arg0) end

---@public
---@param arg0 String
---@return BeardStyles
function BeardStyles:parse(arg0) end

---@public
---@return void
function BeardStyles:Reset() end

---@public
---@return ArrayList|Unknown
function BeardStyles:getAllStyles() end

---@public
---@return void
function BeardStyles:init() end

---@public
---@param arg0 String
---@return BeardStyle
function BeardStyles:FindStyle(arg0) end
