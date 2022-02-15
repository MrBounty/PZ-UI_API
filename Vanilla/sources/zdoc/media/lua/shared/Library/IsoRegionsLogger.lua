---@class IsoRegionsLogger : zombie.iso.areas.isoregion.IsoRegionsLogger
---@field private pool ConcurrentLinkedQueue|Unknown
---@field private loggerQueue ConcurrentLinkedQueue|Unknown
---@field private consolePrint boolean
---@field private logs ArrayList|Unknown
---@field private maxLogs int
---@field private isDirtyUI boolean
IsoRegionsLogger = {}

---@public
---@return ArrayList|Unknown
function IsoRegionsLogger:getLogs() end

---@public
---@return void
function IsoRegionsLogger:unsetDirtyUI() end

---@protected
---@param arg0 String
---@return void
---@overload fun(arg0:String, arg1:Color)
function IsoRegionsLogger:log(arg0) end

---@protected
---@param arg0 String
---@param arg1 Color
---@return void
function IsoRegionsLogger:log(arg0, arg1) end

---@private
---@return IsoRegionsLogger.IsoRegionLog
function IsoRegionsLogger:getLog() end

---@public
---@return boolean
function IsoRegionsLogger:isDirtyUI() end

---@protected
---@param arg0 String
---@return void
function IsoRegionsLogger:warn(arg0) end

---@protected
---@return void
function IsoRegionsLogger:update() end
