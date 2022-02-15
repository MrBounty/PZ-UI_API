---@class HairStyle : zombie.core.skinnedmodel.population.HairStyle
---@field public name String
---@field public model String
---@field public texture String
---@field public alternate ArrayList|Unknown
---@field public level int
---@field public trimChoices ArrayList|Unknown
---@field public growReference boolean
---@field public attachedHair boolean
---@field public noChoose boolean
HairStyle = {}

---@public
---@return boolean
function HairStyle:isValid() end

---@public
---@return boolean
function HairStyle:isAttachedHair() end

---@public
---@return String
function HairStyle:getName() end

---@public
---@return ArrayList|Unknown
function HairStyle:getTrimChoices() end

---@public
---@return int
function HairStyle:getLevel() end

---@public
---@param arg0 String
---@return String
function HairStyle:getAlternate(arg0) end

---@public
---@return boolean
function HairStyle:isNoChoose() end

---@public
---@return boolean
function HairStyle:isGrowReference() end
