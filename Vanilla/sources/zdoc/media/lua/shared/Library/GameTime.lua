---@class GameTime : zombie.GameTime
---@field public instance GameTime
---@field public MULTIPLIER float
---@field private serverTimeShift long
---@field private serverTimeShiftIsSet boolean
---@field private isUTest boolean
---@field public TimeOfDay float
---@field public NightsSurvived int
---@field public Calender PZCalendar
---@field public FPSMultiplier float
---@field public Moon float
---@field public ServerTimeOfDay float
---@field public ServerLastTimeOfDay float
---@field public ServerNewDays int
---@field public lightSourceUpdate float
---@field public multiplierBias float
---@field public LastLastTimeOfDay float
---@field private HelicopterTime1Start int
---@field public PerObjectMultiplier float
---@field private HelicopterTime1End int
---@field private HelicopterDay1 int
---@field public Ambient float
---@field public AmbientMax float
---@field public AmbientMin float
---@field public Day int
---@field public StartDay int
---@field public MaxZombieCountStart float
---@field public MinZombieCountStart float
---@field public MaxZombieCount float
---@field public MinZombieCount float
---@field public Month int
---@field public StartMonth int
---@field public StartTimeOfDay float
---@field public ViewDistMax float
---@field public ViewDistMin float
---@field public Year int
---@field public StartYear int
---@field public HoursSurvived double
---@field public MinutesPerDayStart float
---@field public MinutesPerDay float
---@field public LastTimeOfDay float
---@field public TargetZombies int
---@field public RainingToday boolean
---@field public bGunFireEventToday boolean
---@field public GunFireTimes float[]
---@field public NumGunFireEvents int
---@field public lastPing long
---@field public lastClockSync long
---@field private _table KahluaTable
---@field private minutesMod int
---@field private thunderDay boolean
---@field private randomAmbientToday boolean
---@field private Multiplier float
---@field private dusk int
---@field private dawn int
---@field private NightMin float
---@field private NightMax float
---@field private minutesStamp long
---@field private previousMinuteStamp long
GameTime = {}

---@public
---@return float @the ViewDistMin
function GameTime:getViewDistMin() end

---@public
---@return int @the StartDay
function GameTime:getStartDay() end

---@public
---@return float
function GameTime:getNightMax() end

---@public
---@return float
function GameTime:getRealworldSecondsSinceLastUpdate() end

---@public
---@return int @the Year
function GameTime:getYear() end

---@public
---@return int
function GameTime:getHour() end

---@public
---@return float
function GameTime:getTrueMultiplier() end

---@public
---@param arg0 long
---@return void
function GameTime:setServerTimeShift(arg0) end

---@public
---@param year int
---@param month int
---@return int
function GameTime:daysInMonth(year, month) end

---@public
---@param arg0 PZCalendar
---@return void
function GameTime:setCalender(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@return void
function GameTime:updateCalendar(arg0, arg1, arg2, arg3, arg4) end

---@public
---@return GameTime @the instance
function GameTime:getInstance() end

---@public
---@param arg0 IsoPlayer
---@return String
function GameTime:getZombieKilledText(arg0) end

---@public
---@param arg0 long
---@param arg1 long
---@param arg2 long
---@return void
function GameTime:syncServerTime(arg0, arg1, arg2) end

---@public
---@return long
function GameTime:getServerTime() end

---@public
---@param StartDay int @the StartDay to set
---@return void
function GameTime:setStartDay(StartDay) end

---@public
---@return long
function GameTime:getServerTimeMills() end

---@public
---@param MaxZombieCount float @the MaxZombieCount to set
---@return void
function GameTime:setMaxZombieCount(MaxZombieCount) end

---@public
---@param TargetZombies int @the TargetZombies to set
---@return void
function GameTime:setTargetZombies(TargetZombies) end

---@public
---@param NightTint float @the NightTint to set
---@return void
function GameTime:setNightTint(NightTint) end

---@public
---@return int
function GameTime:getDayPlusOne() end

---@public
---@param dusk int
---@return void
function GameTime:setDusk(dusk) end

---@public
---@param arg0 IsoPlayer
---@return String
function GameTime:getDeathString(arg0) end

---@public
---@return KahluaTable
function GameTime:getModData() end

---@public
---@return void
---@overload fun(input:ByteBuffer)
---@overload fun(input:DataInputStream)
function GameTime:load() end

---throws java.io.IOException
---@public
---@param input ByteBuffer
---@return void
function GameTime:load(input) end

---throws java.io.IOException
---@public
---@param input DataInputStream
---@return void
function GameTime:load(input) end

---@public
---@param StartMonth int @the StartMonth to set
---@return void
function GameTime:setStartMonth(StartMonth) end

---@public
---@return float @the Multiplier
function GameTime:getMultiplier() end

---@public
---@param HoursSurvived double @the HoursSurvived to set
---@return void
function GameTime:setHoursSurvived(HoursSurvived) end

---@public
---@return float @the MinZombieCount
function GameTime:getMinZombieCount() end

---@public
---@return int
function GameTime:getHelicopterStartHour() end

---@public
---@param ViewDistMin float @the ViewDistMin to set
---@return void
function GameTime:setViewDistMin(ViewDistMin) end

---@public
---@return boolean @the RainingToday
function GameTime:isRainingToday() end

---@public
---@return int @the NightsSurvived
function GameTime:getNightsSurvived() end

---@public
---@return PZCalendar
function GameTime:getCalender() end

---@public
---@return float
function GameTime:getInvMultiplier() end

---@public
---@return int
function GameTime:getDawn() end

---@public
---@return void
---@overload fun(output:DataOutputStream)
---@overload fun(output:ByteBuffer)
function GameTime:save() end

---throws java.io.IOException
---@public
---@param output DataOutputStream
---@return void
function GameTime:save(output) end

---throws java.io.IOException
---@public
---@param output ByteBuffer
---@return void
function GameTime:save(output) end

---@public
---@return boolean
function GameTime:isThunderDay() end

---@private
---@return void
function GameTime:sendTimeSync() end

---@public
---@return int @the StartYear
function GameTime:getStartYear() end

---@public
---@return float
function GameTime:getAnimSpeedFix() end

---@public
---@param arg0 IsoPlayer
---@return String
function GameTime:getTimeSurvived(arg0) end

---@public
---@param max float
---@return void
function GameTime:setNightMax(max) end

---@public
---@param LastTimeOfDay float @the LastTimeOfDay to set
---@return void
function GameTime:setLastTimeOfDay(LastTimeOfDay) end

---@public
---@return boolean
function GameTime:getThunderStorm() end

---@public
---@param Ambient float @the Ambient to set
---@return void
function GameTime:setAmbient(Ambient) end

---@public
---@param thunderDay boolean
---@return void
function GameTime:setThunderDay(thunderDay) end

---@public
---@return float @the TimeOfDay
function GameTime:getTimeOfDay() end

---@public
---@return int @the Day
function GameTime:getDay() end

---@public
---@param arg0 int
---@return void
function GameTime:setHelicopterStartHour(arg0) end

---@public
---@return float @the MaxZombieCount
function GameTime:getMaxZombieCount() end

---@public
---@return float @the ViewDist
function GameTime:getViewDist() end

---@public
---@return int @the Month
function GameTime:getMonth() end

---@public
---@param Year int @the Year to set
---@return void
function GameTime:setYear(Year) end

---@public
---@return float
function GameTime:getGameWorldSecondsSinceLastUpdate() end

---@public
---@return String
function GameTime:getGameModeText() end

---@public
---@param AmbientMax float @the AmbientMax to set
---@return void
function GameTime:setAmbientMax(AmbientMax) end

---@public
---@param i int
---@return void
function GameTime:RemoveZombiesIndiscriminate(i) end

---@public
---@param MinZombieCount float @the MinZombieCount to set
---@return void
function GameTime:setMinZombieCount(MinZombieCount) end

---@public
---@return int
function GameTime:getHelicopterEndHour() end

---@public
---@param Day int @the Day to set
---@return void
function GameTime:setDay(Day) end

---@public
---@return float @the MinutesPerDay
function GameTime:getMinutesPerDay() end

---@public
---@return float
function GameTime:getMultipliedSecondsSinceLastUpdate() end

---throws java.io.IOException
---@public
---@param bb ByteBuffer
---@return void
function GameTime:saveToPacket(bb) end

---@public
---@return int
function GameTime:getDaysSurvived() end

---@public
---@return float @the MinZombieCountStart
function GameTime:getMinZombieCountStart() end

---@public
---@return float @the NightTint
function GameTime:getNight() end

---@public
---@param NightsSurvived int @the NightsSurvived to set
---@return void
function GameTime:setNightsSurvived(NightsSurvived) end

---@public
---@return double @the HoursSurvived
function GameTime:getHoursSurvived() end

---@public
---@param Multiplier float @the Multiplier to set
---@return void
function GameTime:setMultiplier(Multiplier) end

---@public
---@return float
function GameTime:getTimeDelta() end

---@public
---@return int
function GameTime:getHelicopterDay1() end

---@public
---@return float
function GameTime:getServerMultiplier() end

---@public
---@param StartYear int @the StartYear to set
---@return void
function GameTime:setStartYear(StartYear) end

---@public
---@param Month int @the Month to set
---@return void
function GameTime:setMonth(Month) end

---@public
---@return float @the MaxZombieCountStart
function GameTime:getMaxZombieCountStart() end

---@public
---@param min float
---@return void
function GameTime:setNightMin(min) end

---@public
---@param dawn int
---@return void
function GameTime:setDawn(dawn) end

---@public
---@return boolean
function GameTime:getServerTimeShiftIsSet() end

---@public
---@return float @the NightTint
function GameTime:getNightTint() end

---@public
---@return boolean
function GameTime:isGamePaused() end

---@public
---@return float @the StartTimeOfDay
function GameTime:getStartTimeOfDay() end

---@public
---@param start float
---@param _end float
---@param delta float
---@return float
function GameTime:Lerp(start, _end, delta) end

---@public
---@return float @the LastTimeOfDay
function GameTime:getLastTimeOfDay() end

---@public
---@param ViewDistMax float @the ViewDistMax to set
---@return void
function GameTime:setViewDistMax(ViewDistMax) end

---@public
---@return float
function GameTime:getUnmoddedMultiplier() end

---@private
---@return void
function GameTime:doMetaEvents() end

---@public
---@return long
function GameTime:getMinutesStamp() end

---@public
---@return int
function GameTime:getMinutes() end

---@public
---@param MinutesPerDay float @the MinutesPerDay to set
---@return void
function GameTime:setMinutesPerDay(MinutesPerDay) end

---@public
---@return double
function GameTime:getWorldAgeHours() end

---@private
---@return void
function GameTime:updateRoomLight() end

---@public
---@return int
function GameTime:getDusk() end

---@public
---@return float @the AmbientMax
function GameTime:getAmbientMax() end

---@public
---@return void
function GameTime:init() end

---@public
---@return float @the AmbientMin
function GameTime:getAmbientMin() end

---@public
---@param moon float
---@return void
function GameTime:setMoon(moon) end

---@public
---@param arg0 ByteBuffer
---@param arg1 UdpConnection
---@return void
function GameTime:receiveTimeSync(arg0, arg1) end

---@public
---@param startVal float
---@param endVal float
---@param startTime float
---@param endTime float
---@return float
function GameTime:TimeLerp(startVal, endVal, startTime, endTime) end

---@public
---@param arg0 int
---@return void
function GameTime:setHelicopterDay(arg0) end

---@public
---@param aInstance GameTime @the instance to set
---@return void
function GameTime:setInstance(aInstance) end

---@public
---@return int
function GameTime:getHelicopterDay() end

---@private
---@return void
function GameTime:setMinutesStamp() end

---@public
---@param NightTint float @the NightTint to set
---@return void
function GameTime:setNight(NightTint) end

---@public
---@return int @the StartMonth
function GameTime:getStartMonth() end

---@public
---@param MaxZombieCountStart float @the MaxZombieCountStart to set
---@return void
function GameTime:setMaxZombieCountStart(MaxZombieCountStart) end

---@public
---@return float @the Ambient
function GameTime:getAmbient() end

---@public
---@return float
function GameTime:getNightMin() end

---@public
---@return float @the ViewDistMax
function GameTime:getViewDistMax() end

---@public
---@param arg0 int
---@return void
function GameTime:setHelicopterEndHour(arg0) end

---@public
---@param bSleeping boolean
---@return void
function GameTime:update(bSleeping) end

---@public
---@param AmbientMin float @the AmbientMin to set
---@return void
function GameTime:setAmbientMin(AmbientMin) end

---@public
---@param StartTimeOfDay float @the StartTimeOfDay to set
---@return void
function GameTime:setStartTimeOfDay(StartTimeOfDay) end

---@public
---@param MinZombieCountStart float @the MinZombieCountStart to set
---@return void
function GameTime:setMinZombieCountStart(MinZombieCountStart) end

---@public
---@return float
function GameTime:getDeltaMinutesPerDay() end

---@public
---@param TimeOfDay float @the TimeOfDay to set
---@return void
function GameTime:setTimeOfDay(TimeOfDay) end
