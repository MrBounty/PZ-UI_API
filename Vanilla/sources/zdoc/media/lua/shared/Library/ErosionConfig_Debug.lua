---@class ErosionConfig.Debug : zombie.erosion.ErosionConfig.Debug
---@field enabled boolean
---@field startday int
---@field startmonth int
ErosionConfig_Debug = {}

---@public
---@return int
function ErosionConfig_Debug:getStartDay() end

---@public
---@return boolean
function ErosionConfig_Debug:getEnabled() end

---@public
---@return int
function ErosionConfig_Debug:getStartMonth() end
