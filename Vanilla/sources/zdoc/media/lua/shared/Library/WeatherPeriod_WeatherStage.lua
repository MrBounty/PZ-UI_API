---@class WeatherPeriod.WeatherStage : zombie.iso.weather.WeatherPeriod.WeatherStage
---@field protected previousStage WeatherPeriod.WeatherStage
---@field protected nextStage WeatherPeriod.WeatherStage
---@field private stageStart double
---@field private stageEnd double
---@field private stageDuration double
---@field protected stageID int
---@field protected entryStrength float
---@field protected exitStrength float
---@field protected targetStrength float
---@field protected lerpMidVal WeatherPeriod.StrLerpVal
---@field protected lerpEndVal WeatherPeriod.StrLerpVal
---@field protected hasStartedCloud boolean
---@field protected fogStrength float
---@field protected linearT float
---@field protected parabolicT float
---@field protected isCycleFirstHalf boolean
---@field protected creationFinished boolean
---@field protected modID String
---@field private m float
---@field private e float
WeatherPeriod_WeatherStage = {}

---@protected
---@param arg0 WeatherPeriod.StrLerpVal
---@return void
---@overload fun(arg0:int, arg1:int)
---@overload fun(arg0:WeatherPeriod.StrLerpVal, arg1:WeatherPeriod.StrLerpVal)
function WeatherPeriod_WeatherStage:lerpEntryTo(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function WeatherPeriod_WeatherStage:lerpEntryTo(arg0, arg1) end

---@protected
---@param arg0 WeatherPeriod.StrLerpVal
---@param arg1 WeatherPeriod.StrLerpVal
---@return void
function WeatherPeriod_WeatherStage:lerpEntryTo(arg0, arg1) end

---@public
---@return String
function WeatherPeriod_WeatherStage:getModID() end

---@private
---@param arg0 double
---@return float
function WeatherPeriod_WeatherStage:getPeriodLerpT(arg0) end

---@public
---@return double
function WeatherPeriod_WeatherStage:getStageStart() end

---@public
---@param arg0 DataOutputStream
---@return void
function WeatherPeriod_WeatherStage:save(arg0) end

---@public
---@param arg0 int
---@return void
function WeatherPeriod_WeatherStage:setStageID(arg0) end

---@private
---@param arg0 double
---@return WeatherPeriod.WeatherStage
function WeatherPeriod_WeatherStage:updateT(arg0) end

---@protected
---@return void
function WeatherPeriod_WeatherStage:reset() end

---@public
---@return double
function WeatherPeriod_WeatherStage:getStageDuration() end

---@public
---@return boolean
function WeatherPeriod_WeatherStage:getHasStartedCloud() end

---@protected
---@param arg0 double
---@return WeatherPeriod.WeatherStage
function WeatherPeriod_WeatherStage:overrideStageDuration(arg0) end

---@public
---@return float
function WeatherPeriod_WeatherStage:getLinearT() end

---@private
---@param arg0 WeatherPeriod.StrLerpVal
---@return float
function WeatherPeriod_WeatherStage:getLerpValue(arg0) end

---@public
---@param arg0 boolean
---@return void
function WeatherPeriod_WeatherStage:setHasStartedCloud(arg0) end

---@public
---@return float
function WeatherPeriod_WeatherStage:getStageCurrentStrength() end

---@protected
---@param arg0 double
---@return WeatherPeriod.WeatherStage
function WeatherPeriod_WeatherStage:startStage(arg0) end

---@protected
---@param arg0 double
---@return WeatherPeriod.WeatherStage
function WeatherPeriod_WeatherStage:setStageDuration(arg0) end

---@public
---@param arg0 DataInputStream
---@param arg1 int
---@return void
function WeatherPeriod_WeatherStage:load(arg0, arg1) end

---@public
---@return double
function WeatherPeriod_WeatherStage:getStageEnd() end

---@protected
---@param arg0 double
---@return double
function WeatherPeriod_WeatherStage:setStageStart(arg0) end

---@public
---@return int
function WeatherPeriod_WeatherStage:getStageID() end

---@public
---@return float
function WeatherPeriod_WeatherStage:getParabolicT() end

---@public
---@param arg0 float
---@return void
function WeatherPeriod_WeatherStage:setTargetStrength(arg0) end
