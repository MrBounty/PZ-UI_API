---@class RadioScript : zombie.radio.scripting.RadioScript
---@field private broadcasts ArrayList|Unknown
---@field private exitOptions ArrayList|Unknown
---@field private GUID String
---@field private name String
---@field private startDay int
---@field private startDayStamp int
---@field private loopMin int
---@field private loopMax int
---@field private internalStamp int
---@field private currentBroadcast RadioBroadCast
---@field private currentHasAired boolean
RadioScript = {}

---@public
---@return int
function RadioScript:getLoopMin() end

---@public
---@return ArrayList|Unknown
function RadioScript:getExitOptions() end

---@public
---@return int
function RadioScript:getStartDayStamp() end

---@public
---@return void
function RadioScript:Reset() end

---@private
---@return RadioBroadCast
function RadioScript:getNextBroadcast() end

---@public
---@param arg0 RadioBroadCast
---@return void
---@overload fun(arg0:RadioBroadCast, arg1:boolean)
function RadioScript:AddBroadcast(arg0) end

---@public
---@param arg0 RadioBroadCast
---@param arg1 boolean
---@return void
function RadioScript:AddBroadcast(arg0, arg1) end

---@public
---@return ArrayList|Unknown
function RadioScript:getBroadcastList() end

---@public
---@return RadioBroadCast
function RadioScript:getCurrentBroadcast() end

---@public
---@param arg0 String
---@return RadioBroadCast
function RadioScript:getBroadcastWithID(arg0) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@return void
function RadioScript:AddExitOption(arg0, arg1, arg2) end

---@public
---@return void
function RadioScript:clearExitOptions() end

---@public
---@return RadioBroadCast
function RadioScript:getValidAirBroadcastDebug() end

---@public
---@return String
function RadioScript:GetName() end

---@public
---@param arg0 int
---@return boolean
function RadioScript:UpdateScript(arg0) end

---@public
---@return RadioBroadCast
function RadioScript:getValidAirBroadcast() end

---@public
---@return int
function RadioScript:getLoopMax() end

---@public
---@return RadioScript.ExitOption
function RadioScript:getNextScript() end

---@public
---@return String
function RadioScript:GetGUID() end

---@public
---@return int
function RadioScript:getStartDay() end

---@public
---@param arg0 int
---@return void
function RadioScript:setStartDayStamp(arg0) end
