---@class VehicleType : zombie.vehicles.VehicleType
---@field public vehiclesDefinition ArrayList|Unknown
---@field public chanceToSpawnNormal int
---@field public chanceToSpawnBurnt int
---@field public spawnRate int
---@field public chanceOfOverCar int
---@field public randomAngle boolean
---@field public baseVehicleQuality float
---@field public name String
---@field private chanceToSpawnKey int
---@field public chanceToPartDamage int
---@field public isSpecialCar boolean
---@field public isBurntCar boolean
---@field public chanceToSpawnSpecial int
---@field public vehicles HashMap|Unknown|Unknown
---@field public specialVehicles ArrayList|Unknown
VehicleType = {}

---@public
---@param arg0 int
---@return void
function VehicleType:setChanceToSpawnKey(arg0) end

---@public
---@param arg0 String
---@return boolean
function VehicleType:hasTypeForZone(arg0) end

---@public
---@param arg0 String
---@return VehicleType
---@overload fun(arg0:String, arg1:Boolean)
function VehicleType:getRandomVehicleType(arg0) end

---@public
---@param arg0 String
---@param arg1 Boolean
---@return VehicleType
function VehicleType:getRandomVehicleType(arg0, arg1) end

---@private
---@param arg0 Collection|Unknown
---@return void
function VehicleType:validate(arg0) end

---@public
---@return float
function VehicleType:getBaseVehicleQuality() end

---@public
---@return float
function VehicleType:getRandomBaseVehicleQuality() end

---@public
---@return void
function VehicleType:init() end

---@public
---@return void
function VehicleType:Reset() end

---@public
---@param arg0 String
---@return VehicleType
function VehicleType:getTypeFromName(arg0) end

---@private
---@return void
function VehicleType:initNormal() end

---@public
---@return int
function VehicleType:getChanceToSpawnKey() end
