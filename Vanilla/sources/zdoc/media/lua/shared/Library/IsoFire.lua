---@class IsoFire : zombie.iso.objects.IsoFire
---@field public Age int
---@field public Energy int
---@field public Life int
---@field public LifeStage int
---@field public LifeStageDuration int
---@field public LifeStageTimer int
---@field public MaxLife int
---@field public MinLife int
---@field public SpreadDelay int
---@field public SpreadTimer int
---@field public numFlameParticles int
---@field public perm boolean
---@field public bSmoke boolean
---@field public LightSource IsoLightSource
---@field public LightRadius int
---@field public LightOscillator float
---@field private heatSource IsoHeatSource
---@field private accum float
IsoFire = {}

---@public
---@return int
function IsoFire:getLightRadius() end

---Overrides:
---
---saveChange in class IsoObject
---@public
---@param change String
---@param tbl KahluaTable
---@param bb ByteBuffer
---@return void
function IsoFire:saveChange(change, tbl, bb) end

---@public
---@return int
function IsoFire:getLife() end

---@public
---@return int
function IsoFire:getEnergy() end

---@public
---@param radius int
---@return void
function IsoFire:setLightRadius(radius) end

---@public
---@param obj IsoMovingObject
---@param PassedObjectSquare IsoGridSquare
---@return boolean
function IsoFire:TestCollide(obj, PassedObjectSquare) end

---The more this number is low, the faster it's gonna spread
---@public
---@return int
function IsoFire:getSpreadDelay() end

---The more this number is low, the faster it's gonna spread
---@public
---@param SpreadDelay int
---@return void
function IsoFire:setSpreadDelay(SpreadDelay) end

---@public
---@return boolean
function IsoFire:isPermanent() end

---Overrides:
---
---addToWorld in class IsoObject
---@public
---@return void
function IsoFire:addToWorld() end

---Overrides:
---
---TestVision in class IsoObject
---@public
---@param from IsoGridSquare
---@param to IsoGridSquare
---@return IsoObject.VisionResult
function IsoFire:TestVision(from, to) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoFire:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---Overrides:
---
---HasTooltip in class IsoObject
---@public
---@return boolean
function IsoFire:HasTooltip() end

---@public
---@return void
function IsoFire:Spread() end

---Overrides:
---
---update in class IsoObject
---@public
---@return void
function IsoFire:update() end

---@public
---@param arg0 IsoGridSquare
---@param arg1 boolean
---@return boolean
function IsoFire:CanAddSmoke(arg0, arg1) end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function IsoFire:save(arg0, arg1) end

---@public
---@param lifeStage int
---@return void
function IsoFire:setLifeStage(lifeStage) end

---@public
---@param gridSquare IsoGridSquare
---@param CanBurnAnywhere boolean
---@return boolean
---@overload fun(arg0:IsoGridSquare, arg1:boolean, arg2:boolean)
function IsoFire:CanAddFire(gridSquare, CanBurnAnywhere) end

---@public
---@param arg0 IsoGridSquare
---@param arg1 boolean
---@param arg2 boolean
---@return boolean
function IsoFire:CanAddFire(arg0, arg1, arg2) end

---@public
---@param gridSquare IsoGridSquare
---@return boolean
function IsoFire:Fire_IsSquareFlamable(gridSquare) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoFire:load(arg0, arg1, arg2) end

---Overrides:
---
---getObjectName in class IsoObject
---@public
---@return String
function IsoFire:getObjectName() end

---Overrides:
---
---removeFromWorld in class IsoObject
---@public
---@return void
function IsoFire:removeFromWorld() end

---Up this number to make the fire life longer
---@public
---@param Life int
---@return void
function IsoFire:setLife(Life) end

---Overrides:
---
---loadChange in class IsoObject
---@public
---@param change String
---@param bb ByteBuffer
---@return void
function IsoFire:loadChange(change, bb) end

---@param arg0 IsoGridSquare
---@return int
function IsoFire:getSquaresEnergyRequirement(arg0) end

---@public
---@return void
function IsoFire:extinctFire() end
