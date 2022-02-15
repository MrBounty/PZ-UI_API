---@class SleepingEvent : zombie.ai.sadisticAIDirector.SleepingEvent
---@field public instance SleepingEvent
---@field public zombiesInvasion boolean
SleepingEvent = {}

---@private
---@param arg0 IsoPlayer
---@return void
function SleepingEvent:updateSnow(arg0) end

---@private
---@param arg0 IsoPlayer
---@return void
function SleepingEvent:updateRain(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
---@overload fun(arg0:IsoGameCharacter, arg1:boolean)
function SleepingEvent:wakeUp(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 boolean
---@return void
function SleepingEvent:wakeUp(arg0, arg1) end

---@private
---@param arg0 IsoPlayer
---@return void
function SleepingEvent:spawnZombieIntruders(arg0) end

---@private
---@param arg0 IsoPlayer
---@return void
function SleepingEvent:updateTemperature(arg0) end

---@private
---@param arg0 IsoPlayer
---@return void
function SleepingEvent:doDelayToSleep(arg0) end

---@private
---@param arg0 IsoPlayer
---@param arg1 int
---@return void
function SleepingEvent:checkNightmare(arg0, arg1) end

---@public
---@param arg0 IsoPlayer
---@param arg1 int
---@return void
function SleepingEvent:setPlayerFallAsleep(arg0, arg1) end

---@private
---@param arg0 IsoPlayer
---@return void
function SleepingEvent:updateWetness(arg0) end

---@private
---@param arg0 IsoGameCharacter
---@return boolean
function SleepingEvent:isExposedToPrecipitation(arg0) end

---@private
---@param arg0 IsoPlayer
---@return IsoWindow
function SleepingEvent:getWeakestWindow(arg0) end

---@private
---@param arg0 IsoWindow
---@return int
function SleepingEvent:checkWindowStatus(arg0) end

---@public
---@param arg0 IsoPlayer
---@return void
function SleepingEvent:update(arg0) end
