---@class AlarmClock : zombie.inventory.types.AlarmClock
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
AlarmClock = {}

---@public
---@return void
function AlarmClock:stopRinging() end

---@public
---@param arg0 IsoPlayer
---@return void
function AlarmClock:syncAlarmClock_Player(arg0) end

---@public
---@return String
function AlarmClock:getCategory() end

---@public
---@param arg0 boolean
---@return void
function AlarmClock:setAlarmSet(arg0) end

---@public
---@return void
function AlarmClock:syncAlarmClock() end

---@public
---@return boolean
function AlarmClock:isRinging() end

---@public
---@return int
function AlarmClock:getSaveType() end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function AlarmClock:save(arg0, arg1) end

---@public
---@param arg0 ObjectTooltip
---@param arg1 ObjectTooltip.Layout
---@return void
function AlarmClock:DoTooltip(arg0, arg1) end

---@private
---@return void
function AlarmClock:randomizeAlarm() end

---@private
---@param arg0 ItemContainer
---@return IsoPlayer
function AlarmClock:getOwnerPlayer(arg0) end

---@private
---@param arg0 IsoPlayer
---@return void
function AlarmClock:wakeUp(arg0) end

---@public
---@param arg0 int
---@return void
function AlarmClock:setMinute(arg0) end

---@public
---@return boolean
function AlarmClock:isAlarmSet() end

---@public
---@param arg0 int
---@return void
function AlarmClock:setSoundRadius(arg0) end

---@public
---@return int
function AlarmClock:getSoundRadius() end

---@public
---@return boolean
function AlarmClock:shouldUpdateInWorld() end

---@private
---@param arg0 IsoGridSquare
---@return void
function AlarmClock:wakeUpPlayers(arg0) end

---@public
---@return String
function AlarmClock:getAlarmSound() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function AlarmClock:load(arg0, arg1) end

---@public
---@return IsoGridSquare
function AlarmClock:getAlarmSquare() end

---@public
---@return boolean
function AlarmClock:isDigital() end

---@public
---@return int
function AlarmClock:getHour() end

---@public
---@param arg0 String
---@return void
function AlarmClock:setAlarmSound(arg0) end

---@public
---@return int
function AlarmClock:getMinute() end

---@public
---@return boolean
function AlarmClock:finishupdate() end

---@public
---@return void
function AlarmClock:syncAlarmClock_World() end

---@public
---@param arg0 BaseSoundEmitter
---@return void
function AlarmClock:updateSound(arg0) end

---@public
---@return void
function AlarmClock:update() end

---@public
---@param arg0 int
---@return void
function AlarmClock:setHour(arg0) end

---@public
---@return void
function AlarmClock:syncStopRinging() end
