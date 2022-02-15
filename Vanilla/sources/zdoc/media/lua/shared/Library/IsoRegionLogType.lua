---@class IsoRegionLogType : zombie.iso.areas.isoregion.IsoRegionLogType
---@field public Normal IsoRegionLogType
---@field public Warn IsoRegionLogType
IsoRegionLogType = {}

---@public
---@return IsoRegionLogType[]
function IsoRegionLogType:values() end

---@public
---@param arg0 String
---@return IsoRegionLogType
function IsoRegionLogType:valueOf(arg0) end
