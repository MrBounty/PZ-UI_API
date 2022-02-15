---@class RadioBroadCast : zombie.radio.scripting.RadioBroadCast
---@field private pauseLine RadioLine
---@field private lines ArrayList|Unknown
---@field private ID String
---@field private startStamp int
---@field private endStamp int
---@field private lineCount int
---@field private preSegment RadioBroadCast
---@field private postSegment RadioBroadCast
---@field private hasDonePreSegment boolean
---@field private hasDonePostSegment boolean
---@field private hasDonePostPause boolean
RadioBroadCast = {}

---@public
---@return RadioLine
function RadioBroadCast:getCurrentLine() end

---@public
---@return String
function RadioBroadCast:PeekNextLineText() end

---@public
---@return void
---@overload fun(arg0:boolean)
function RadioBroadCast:resetLineCounter() end

---@public
---@param arg0 boolean
---@return void
function RadioBroadCast:resetLineCounter(arg0) end

---@public
---@return RadioLine
---@overload fun(arg0:boolean)
function RadioBroadCast:getNextLine() end

---@public
---@param arg0 boolean
---@return RadioLine
function RadioBroadCast:getNextLine(arg0) end

---@public
---@return String
function RadioBroadCast:getID() end

---@public
---@return int
function RadioBroadCast:getEndStamp() end

---@public
---@param arg0 RadioBroadCast
---@return void
function RadioBroadCast:setPreSegment(arg0) end

---@public
---@return int
function RadioBroadCast:getStartStamp() end

---@public
---@return int
function RadioBroadCast:getCurrentLineNumber() end

---@public
---@return ArrayList|Unknown
function RadioBroadCast:getLines() end

---@public
---@param arg0 RadioBroadCast
---@return void
function RadioBroadCast:setPostSegment(arg0) end

---@public
---@param arg0 RadioLine
---@return void
function RadioBroadCast:AddRadioLine(arg0) end

---@public
---@param arg0 int
---@return void
function RadioBroadCast:setCurrentLineNumber(arg0) end
