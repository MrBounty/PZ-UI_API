---@class ClimateManager.AirFront : zombie.iso.weather.ClimateManager.AirFront
---@field private days float
---@field private maxNoise float
---@field private totalNoise float
---@field private type int
---@field private strength float
---@field private tmpNoiseAbs float
---@field private noiseCache float[]
---@field private noiseCacheValue float
---@field private frontWindAngleDegrees float
ClimateManager_AirFront = {}

---@public
---@param arg0 float
---@return void
function ClimateManager_AirFront:setStrength(arg0) end

---@public
---@param arg0 ClimateManager.AirFront
---@return void
function ClimateManager_AirFront:copyFrom(arg0) end

---@public
---@param arg0 DataInputStream
---@return void
function ClimateManager_AirFront:load(arg0) end

---@public
---@param arg0 DataOutputStream
---@return void
function ClimateManager_AirFront:save(arg0) end

---@public
---@return float
function ClimateManager_AirFront:getStrength() end

---@public
---@return int
function ClimateManager_AirFront:getType() end

---@protected
---@param arg0 float
---@return void
function ClimateManager_AirFront:setFrontWind(arg0) end

---@protected
---@return void
function ClimateManager_AirFront:reset() end

---@public
---@return float
function ClimateManager_AirFront:getDays() end

---@public
---@return float
function ClimateManager_AirFront:getAngleDegrees() end

---@public
---@param arg0 int
---@return void
function ClimateManager_AirFront:setFrontType(arg0) end

---@public
---@return float
function ClimateManager_AirFront:getMaxNoise() end

---@public
---@return float
function ClimateManager_AirFront:getTotalNoise() end

---@public
---@param arg0 float
---@return void
function ClimateManager_AirFront:addDaySample(arg0) end
