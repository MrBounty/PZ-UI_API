---@class AmbientStreamManager : zombie.AmbientStreamManager
---@field public OneInAmbienceChance int
---@field public MaxAmbientCount int
---@field public MaxRange float
---@field private alarmList ArrayList|Unknown
---@field public instance BaseAmbientStreamManager
---@field public ambient ArrayList|AmbientStreamManager.Ambient
---@field public worldEmitters ArrayList|Unknown
---@field public freeEmitters ArrayDeque|Unknown
---@field public allAmbient ArrayList|AmbientStreamManager.AmbientLoop
---@field public nightAmbient ArrayList|AmbientStreamManager.AmbientLoop
---@field public dayAmbient ArrayList|AmbientStreamManager.AmbientLoop
---@field public rainAmbient ArrayList|AmbientStreamManager.AmbientLoop
---@field public indoorAmbient ArrayList|AmbientStreamManager.AmbientLoop
---@field public outdoorAmbient ArrayList|AmbientStreamManager.AmbientLoop
---@field public windAmbient ArrayList|AmbientStreamManager.AmbientLoop
---@field public initialized boolean
---@field private parameterFogIntensity ParameterFogIntensity
---@field private parameterRainIntensity ParameterRainIntensity
---@field private parameterSeason ParameterSeason
---@field private parameterSnowIntensity ParameterSnowIntensity
---@field private parameterStorm ParameterStorm
---@field private parameterTimeOfDay ParameterTimeOfDay
---@field private parameterTemperature ParameterTemperature
---@field private parameterWeatherEvent ParameterWeatherEvent
---@field private parameterWindIntensity ParameterWindIntensity
---@field private parameterZoneDeepForest ParameterZone
---@field private parameterZoneFarm ParameterZone
---@field private parameterZoneForest ParameterZone
---@field private parameterZoneNav ParameterZone
---@field private parameterZoneTown ParameterZone
---@field private parameterZoneTrailerPark ParameterZone
---@field private parameterZoneVegetation ParameterZone
---@field private parameterZoneWaterSide ParameterZoneWaterSide
---@field private parameterCameraZoom ParameterCameraZoom
---@field private parameterClosestWallDistance ParameterClosestWallDistance
---@field private parameterHardOfHearing ParameterHardOfHearing
---@field private parameterInside ParameterInside
---@field private parameterMoodlePanic ParameterMoodlePanic
---@field private parameterRoomSize ParameterRoomSize
---@field private tempo JVector2
AmbientStreamManager = {}

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 int
---@param arg3 String
---@return void
function AmbientStreamManager:addAmbientEmitter(arg0, arg1, arg2, arg3) end

---@public
---@return void
function AmbientStreamManager:addRandomAmbient() end

---Specified by:
---
---addAmbient in class BaseAmbientStreamManager
---@public
---@param name String
---@param x int
---@param y int
---@param radius int
---@param volume float
---@return void
function AmbientStreamManager:addAmbient(name, x, y, radius, volume) end

---@public
---@return BaseAmbientStreamManager
function AmbientStreamManager:getInstance() end

---Specified by:
---
---init in class BaseAmbientStreamManager
---@public
---@return void
function AmbientStreamManager:init() end

---Specified by:
---
---doAlarm in class BaseAmbientStreamManager
---@public
---@param room RoomDef
---@return void
function AmbientStreamManager:doAlarm(room) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 int
---@param arg3 String
---@return void
function AmbientStreamManager:addDaytimeAmbientEmitter(arg0, arg1, arg2, arg3) end

---Specified by:
---
---doGunEvent in class BaseAmbientStreamManager
---@public
---@return void
function AmbientStreamManager:doGunEvent() end

---Specified by:
---
---update in class BaseAmbientStreamManager
---@public
---@return void
function AmbientStreamManager:update() end

---Specified by:
---
---addBlend in class BaseAmbientStreamManager
---@public
---@param name String
---@param vol float
---@param bIndoors boolean
---@param bRain boolean
---@param bNight boolean
---@param bDay boolean
---@return void
function AmbientStreamManager:addBlend(name, vol, bIndoors, bRain, bNight, bDay) end

---Specified by:
---
---doOneShotAmbients in class BaseAmbientStreamManager
---@public
---@return void
function AmbientStreamManager:doOneShotAmbients() end

---Specified by:
---
---stop in class BaseAmbientStreamManager
---@public
---@return void
function AmbientStreamManager:stop() end
