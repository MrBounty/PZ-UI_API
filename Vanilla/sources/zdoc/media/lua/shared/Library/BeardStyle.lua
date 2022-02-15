---@class BeardStyle : zombie.core.skinnedmodel.population.BeardStyle
---@field public name String
---@field public model String
---@field public texture String
---@field public level int
---@field public trimChoices ArrayList|Unknown
---@field public growReference boolean
BeardStyle = {}

---@public
---@return int
function BeardStyle:getLevel() end

---@public
---@return boolean
function BeardStyle:isValid() end

---@public
---@return String
function BeardStyle:getName() end

---@public
---@return ArrayList|Unknown
function BeardStyle:getTrimChoices() end
