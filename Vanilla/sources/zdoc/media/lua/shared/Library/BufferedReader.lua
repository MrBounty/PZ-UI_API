---@class BufferedReader : java.io.BufferedReader
---@field private _in Reader
---@field private cb char[]
---@field private nChars int
---@field private nextChar int
---@field private INVALIDATED int
---@field private UNMARKED int
---@field private markedChar int
---@field private readAheadLimit int
---@field private skipLF boolean
---@field private markedSkipLF boolean
---@field private defaultCharBufferSize int
---@field private defaultExpectedLineLength int
BufferedReader = {}

---@public
---@return Stream|Unknown
function BufferedReader:lines() end

---@public
---@return void
function BufferedReader:close() end

---@public
---@return String
---@overload fun(arg0:boolean, arg1:boolean[])
function BufferedReader:readLine() end

---@param arg0 boolean
---@param arg1 boolean[]
---@return String
function BufferedReader:readLine(arg0, arg1) end

---@public
---@return void
function BufferedReader:reset() end

---@private
---@param arg0 char[]
---@param arg1 int
---@param arg2 int
---@return int
function BufferedReader:read1(arg0, arg1, arg2) end

---@public
---@return int
---@overload fun(arg0:char[], arg1:int, arg2:int)
function BufferedReader:read() end

---@public
---@param arg0 char[]
---@param arg1 int
---@param arg2 int
---@return int
function BufferedReader:read(arg0, arg1, arg2) end

---@public
---@param arg0 long
---@return long
function BufferedReader:skip(arg0) end

---@public
---@return boolean
function BufferedReader:ready() end

---@public
---@return boolean
function BufferedReader:markSupported() end

---@private
---@return void
function BufferedReader:fill() end

---@private
---@return void
function BufferedReader:ensureOpen() end

---@public
---@param arg0 int
---@return void
function BufferedReader:mark(arg0) end
