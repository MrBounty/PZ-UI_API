---@class Thermoregulator : zombie.characters.BodyDamage.Thermoregulator
---@field private DISABLE_ENERGY_MULTIPLIER boolean
---@field private bodyDamage BodyDamage
---@field private character IsoGameCharacter
---@field private player IsoPlayer
---@field private stats Stats
---@field private nutrition Nutrition
---@field private climate ClimateManager
---@field private itemVisuals ItemVisuals
---@field private itemVisualsCache ItemVisuals
---@field private coveredParts ArrayList|Unknown
---@field private SIMULATION_MULTIPLIER float
---@field private setPoint float
---@field private metabolicRate float
---@field private metabolicRateReal float
---@field private metabolicTarget float
---@field private fluidsMultiplier double
---@field private energyMultiplier double
---@field private fatigueMultiplier double
---@field private bodyHeatDelta float
---@field private coreHeatDelta float
---@field private thermalChevronUp boolean
---@field private core Thermoregulator.ThermalNode
---@field private nodes Thermoregulator.ThermalNode[]
---@field private totalHeatRaw float
---@field private totalHeat float
---@field private primTotal float
---@field private secTotal float
---@field private externalAirTemperature float
---@field private airTemperature float
---@field private airAndWindTemp float
---@field private rateOfChangeCounter float
---@field private coreCelciusCache float
---@field private coreRateOfChange float
---@field private thermalDamage float
---@field private damageCounter float
Thermoregulator = {}

---@public
---@return double
function Thermoregulator:getFluidsMultiplier() end

---@public
---@param arg0 BloodBodyPartType
---@return Thermoregulator.ThermalNode
function Thermoregulator:getNodeForBloodType(arg0) end

---@public
---@return float
---@overload fun(arg0:Thermoregulator.Multiplier)
function Thermoregulator:getSimulationMultiplier() end

---@private
---@param arg0 Thermoregulator.Multiplier
---@return float
function Thermoregulator:getSimulationMultiplier(arg0) end

---@public
---@return float
function Thermoregulator:getSkinCelciusMin() end

---@public
---@return float
function Thermoregulator:getMetabolicRateReal() end

---@public
---@return float
function Thermoregulator:getDefaultMultiplier() end

---@public
---@return float
function Thermoregulator:getMetabolicRate() end

---@public
---@return float
function Thermoregulator:getCoreTemperature() end

---@public
---@return boolean
function Thermoregulator:thermalChevronUp() end

---@private
---@return void
function Thermoregulator:updateMetabolicRate() end

---@public
---@param arg0 Metabolics
---@return void
---@overload fun(arg0:float)
function Thermoregulator:setMetabolicTarget(arg0) end

---@public
---@param arg0 float
---@return void
function Thermoregulator:setMetabolicTarget(arg0) end

---@public
---@return float
function Thermoregulator:getHeatGeneration() end

---@public
---@return double
function Thermoregulator:getEnergyMultiplier() end

---@public
---@return void
function Thermoregulator:reset() end

---@public
---@return float
function Thermoregulator:getDbg_totalHeat() end

---@public
---@return float
function Thermoregulator:getSetPoint() end

---@private
---@return void
function Thermoregulator:updateHeatDeltas() end

---@public
---@return int
function Thermoregulator:thermalChevronCount() end

---@public
---@return float
function Thermoregulator:getThermalDamage() end

---@private
---@return void
function Thermoregulator:updateSetPoint() end

---@private
---@return void
function Thermoregulator:updateCoreRateOfChange() end

---@public
---@return float
function Thermoregulator:getSkinCelciusMultiplier() end

---@public
---@return void
function Thermoregulator:update() end

---@public
---@return float
function Thermoregulator:getCoreCelcius() end

---@public
---@return float
function Thermoregulator:getSkinCelciusMax() end

---@public
---@return float
function Thermoregulator:getMovementModifier() end

---@public
---@return float
function Thermoregulator:getHeatGenerationUI() end

---@public
---@return float
function Thermoregulator:getEnergy() end

---@public
---@return float
function Thermoregulator:getCoreHeatContractMultiplier() end

---@public
---@return float
function Thermoregulator:getMetabolicRateDecMultiplier() end

---@public
---@return float
function Thermoregulator:getCoreRateOfChange() end

---@public
---@return float
function Thermoregulator:getCombatModifier() end

---@public
---@param arg0 ByteBuffer
---@return void
function Thermoregulator:save(arg0) end

---@public
---@param arg0 BodyPartType
---@return Thermoregulator.ThermalNode
function Thermoregulator:getNodeForType(arg0) end

---@public
---@return float
function Thermoregulator:getCoreTemperatureUI() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function Thermoregulator:load(arg0, arg1) end

---@private
---@return void
function Thermoregulator:updateNodesHeatDelta() end

---@private
---@return void
function Thermoregulator:updateNodes() end

---@public
---@return float
function Thermoregulator:getTemperatureAirAndWind() end

---@public
---@return float
function Thermoregulator:getDbg_totalHeatRaw() end

---@public
---@return double
function Thermoregulator:getFatigueMultiplier() end

---@public
---@return float
function Thermoregulator:getBodyHeatDelta() end

---@public
---@return float
function Thermoregulator:getDbg_primTotal() end

---@private
---@return void
function Thermoregulator:updateBodyMultipliers() end

---@public
---@return float
function Thermoregulator:getTemperatureAir() end

---@public
---@return float
function Thermoregulator:getCatchAColdDelta() end

---@private
---@return void
function Thermoregulator:initNodes() end

---@public
---@return float
function Thermoregulator:getDbg_secTotal() end

---@public
---@return float
function Thermoregulator:getExternalAirTemperature() end

---@public
---@return float
function Thermoregulator:getCoreHeatExpandMultiplier() end

---@public
---@return float
function Thermoregulator:getMetabolicRateIncMultiplier() end

---@public
---@return float
function Thermoregulator:getMetabolicTarget() end

---@public
---@return float
function Thermoregulator:getBodyHeatMultiplier() end

---@public
---@return float
function Thermoregulator:getCoreHeatDelta() end

---@private
---@return float
function Thermoregulator:getSicknessValue() end

---@public
---@return float
function Thermoregulator:getTimedActionTimeModifier() end

---@private
---@return void
function Thermoregulator:updateClothing() end

---@public
---@param arg0 float
---@return void
function Thermoregulator:setSimulationMultiplier(arg0) end

---@private
---@param arg0 float
---@return void
function Thermoregulator:updateThermalDamage(arg0) end

---@public
---@return float
function Thermoregulator:getSkinCelciusFavorable() end

---@public
---@return float
function Thermoregulator:getBodyFluids() end
