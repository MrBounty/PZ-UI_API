---@class AlarmClockClothing : zombie.inventory.types.AlarmClockClothing
---@field private alarmHour int
---@field private alarmMinutes int
---@field private alarmSet boolean
---@field private ringSound long
---@field private ringSince double
---@field private forceDontRing int
---@field private alarmSound String
---@field private soundRadius int
---@field private isDigital boolean
---@field public PacketPlayer short
---@field public PacketWorld short
---@field private sendEvery OnceEvery
AlarmClockClothing = {}

---@public
---@return void
function AlarmClockClothing:update() end

---@public
---@return boolean
function AlarmClockClothing:isRinging() end

---@public
---@return int
function AlarmClockClothing:getMinute() end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function AlarmClockClothing:save(arg0, arg1) end

---@private
---@param arg0 IsoPlayer
---@return void
function AlarmClockClothing:wakeUp(arg0) end

---@public
---@return void
function AlarmClockClothing:syncAlarmClock_World() end

---@private
---@return void
function AlarmClockClothing:randomizeAlarm() end

---@public
---@param arg0 BaseSoundEmitter
---@return void
function AlarmClockClothing:updateSound(arg0) end

---@public
---@return boolean
function AlarmClockClothing:isAlarmSet() end

---@public
---@param arg0 int
---@return void
function AlarmClockClothing:setHour(arg0) end

---@public
---@param arg0 int
---@return void
function AlarmClockClothing:setMinute(arg0) end

---@public
---@return void
function AlarmClockClothing:stopRinging() end

---@public
---@param arg0 String
---@return void
function AlarmClockClothing:setAlarmSound(arg0) end

---@public
---@return int
function AlarmClockClothing:getHour() end

---@public
---@return String
function AlarmClockClothing:getAlarmSound() end

---@private
---@param arg0 ItemContainer
---@return IsoPlayer
function AlarmClockClothing:getOwnerPlayer(arg0) end

---@public
---@param arg0 IsoPlayer
---@return void
function AlarmClockClothing:syncAlarmClock_Player(arg0) end

---@public
---@return int
function AlarmClockClothing:getSaveType() end

---@public
---@param arg0 ObjectTooltip
---@param arg1 ObjectTooltip.Layout
---@return void
function AlarmClockClothing:DoTooltip(arg0, arg1) end

---@public
---@return void
function AlarmClockClothing:syncStopRinging() end

---@public
---@return IsoGridSquare
function AlarmClockClothing:getAlarmSquare() end

---@public
---@return boolean
function AlarmClockClothing:finishupdate() end

---@private
---@param arg0 IsoGridSquare
---@return void
function AlarmClockClothing:wakeUpPlayers(arg0) end

---@public
---@param arg0 int
---@return void
function AlarmClockClothing:setSoundRadius(arg0) end

---@public
---@return int
function AlarmClockClothing:getSoundRadius() end

---@public
---@return boolean
function AlarmClockClothing:shouldUpdateInWorld() end

---@public
---@return String
function AlarmClockClothing:getCategory() end

---@public
---@param arg0 boolean
---@return void
function AlarmClockClothing:setAlarmSet(arg0) end

---@public
---@return boolean
function AlarmClockClothing:isDigital() end

---@public
---@return void
function AlarmClockClothing:syncAlarmClock() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function AlarmClockClothing:load(arg0, arg1) end
