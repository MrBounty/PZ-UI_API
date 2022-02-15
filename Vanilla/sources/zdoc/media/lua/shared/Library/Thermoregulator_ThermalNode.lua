---@class Thermoregulator.ThermalNode : zombie.characters.BodyDamage.Thermoregulator.ThermalNode
---@field private distToCore float
---@field private skinSurface float
---@field private bodyPartType BodyPartType
---@field private bloodBPT BloodBodyPartType
---@field private bodyPart BodyPart
---@field private isCore boolean
---@field private insulationLayerMultiplierUI float
---@field private upstream Thermoregulator.ThermalNode
---@field private downstream Thermoregulator.ThermalNode[]
---@field private insulation float
---@field private windresist float
---@field private celcius float
---@field private skinCelcius float
---@field private heatDelta float
---@field private primaryDelta float
---@field private secondaryDelta float
---@field private clothingWetness float
---@field private bodyWetness float
---@field private clothing ArrayList|Unknown
Thermoregulator_ThermalNode = {}

---@public
---@return float
function Thermoregulator_ThermalNode:getSecondaryDelta() end

---@public
---@return float
function Thermoregulator_ThermalNode:getBodyResponseUI() end

---@public
---@return float
function Thermoregulator_ThermalNode:getSkinSurface() end

---@public
---@return float
function Thermoregulator_ThermalNode:getPrimaryDelta() end

---@public
---@return float
function Thermoregulator_ThermalNode:getHeatDelta() end

---@public
---@return float
function Thermoregulator_ThermalNode:getHeatDeltaUI() end

---@public
---@return float
function Thermoregulator_ThermalNode:getWindresistUI() end

---@public
---@return float
function Thermoregulator_ThermalNode:getBodyResponse() end

---@public
---@return float
function Thermoregulator_ThermalNode:getSkinCelcius() end

---@public
---@return float
function Thermoregulator_ThermalNode:getBodyWetness() end

---@private
---@return void
function Thermoregulator_ThermalNode:calculateInsulation() end

---@public
---@return float
function Thermoregulator_ThermalNode:getClothingWetnessUI() end

---@public
---@return boolean
function Thermoregulator_ThermalNode:hasUpstream() end

---@public
---@return float
function Thermoregulator_ThermalNode:getBodyWetnessUI() end

---@public
---@return float
function Thermoregulator_ThermalNode:getInsulation() end

---@public
---@return float
function Thermoregulator_ThermalNode:getCelcius() end

---@public
---@return boolean
function Thermoregulator_ThermalNode:isCore() end

---@public
---@return float
function Thermoregulator_ThermalNode:getPrimaryDeltaUI() end

---@public
---@return float
function Thermoregulator_ThermalNode:getClothingWetness() end

---@public
---@return float
function Thermoregulator_ThermalNode:getSecondaryDeltaUI() end

---@public
---@return float
function Thermoregulator_ThermalNode:getSkinCelciusUI() end

---@public
---@return float
function Thermoregulator_ThermalNode:getInsulationUI() end

---@public
---@return float
function Thermoregulator_ThermalNode:getDistToCore() end

---@public
---@return float
function Thermoregulator_ThermalNode:getWindresist() end

---@public
---@return boolean
function Thermoregulator_ThermalNode:hasDownstream() end
