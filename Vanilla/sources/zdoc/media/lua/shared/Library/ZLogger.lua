---@class ZLogger : zombie.core.logger.ZLogger
---@field private name String
---@field private outputStreams ZLogger.OutputStreams
---@field private file File
---@field private s_fileNameSdf SimpleDateFormat
---@field private s_logSdf SimpleDateFormat
---@field private s_maxSizeKo long
ZLogger = {}

---@public
---@param arg0 Exception
---@return void
---@overload fun(logs:String)
---@overload fun(logs:String, level:String)
function ZLogger:write(arg0) end

---@public
---@param logs String
---@return void
function ZLogger:write(logs) end

---@public
---@param logs String
---@param level String
---@return void
function ZLogger:write(logs, level) end

---@private
---@return void
function ZLogger:checkSizeUnsafe() end

---@public
---@param arg0 String
---@param arg1 String
---@return void
function ZLogger:writeUnsafe(arg0, arg1) end

---@private
---@param arg0 String
---@return String
function ZLogger:getLoggerName(arg0) end

---@private
---@return void
function ZLogger:checkSize() end
