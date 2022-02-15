---@class IsoRegionsLogger.IsoRegionLog : zombie.iso.areas.isoregion.IsoRegionsLogger.IsoRegionLog
---@field private str String
---@field private type IsoRegionLogType
---@field private col Color
IsoRegionsLogger_IsoRegionLog = {}

---@public
---@return Color
function IsoRegionsLogger_IsoRegionLog:getColor() end

---@public
---@return IsoRegionLogType
function IsoRegionsLogger_IsoRegionLog:getType() end

---@public
---@return String
function IsoRegionsLogger_IsoRegionLog:getStr() end
