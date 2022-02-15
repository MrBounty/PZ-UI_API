---@class BufferedWriter : java.io.BufferedWriter
---@field private out Writer
---@field private cb char[]
---@field private nChars int
---@field private nextChar int
---@field private defaultCharBufferSize int
BufferedWriter = {}

---@return void
function BufferedWriter:flushBuffer() end

---@public
---@param arg0 int
---@return void
---@overload fun(arg0:String, arg1:int, arg2:int)
---@overload fun(arg0:char[], arg1:int, arg2:int)
function BufferedWriter:write(arg0) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@return void
function BufferedWriter:write(arg0, arg1, arg2) end

---@public
---@param arg0 char[]
---@param arg1 int
---@param arg2 int
---@return void
function BufferedWriter:write(arg0, arg1, arg2) end

---@public
---@return void
function BufferedWriter:newLine() end

---@public
---@return void
function BufferedWriter:close() end

---@private
---@param arg0 int
---@param arg1 int
---@return int
function BufferedWriter:min(arg0, arg1) end

---@private
---@return void
function BufferedWriter:ensureOpen() end

---@public
---@return void
function BufferedWriter:flush() end
