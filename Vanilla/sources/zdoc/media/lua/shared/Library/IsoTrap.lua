---@class IsoTrap : zombie.iso.objects.IsoTrap
---@field private timerBeforeExplosion int
---@field private FPS int
---@field private sensorRange int
---@field private firePower int
---@field private fireRange int
---@field private explosionPower int
---@field private explosionRange int
---@field private smokeRange int
---@field private noiseRange int
---@field private noiseDuration int
---@field private noiseStartTime float
---@field private lastWorldSoundTime float
---@field private extraDamage float
---@field private remoteControlID int
---@field private countDownSound String
---@field private explosionSound String
---@field private lastBeep int
---@field private weapon HandWeapon
---@field private instantExplosion boolean
IsoTrap = {}

---@public
---@return float
function IsoTrap:getExtraDamage() end

---@public
---@return int
function IsoTrap:getNoiseRange() end

---@public
---@return boolean
function IsoTrap:isInstantExplosion() end

---@public
---@param firePower int
---@return void
function IsoTrap:setFirePower(firePower) end

---@public
---@return int
function IsoTrap:getSmokeRange() end

---@public
---@return int
function IsoTrap:getExplosionPower() end

---Overrides:
---
---update in class IsoObject
---@public
---@return void
function IsoTrap:update() end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function IsoTrap:save(arg0, arg1) end

---@public
---@param fireRange int
---@return void
function IsoTrap:setFireRange(fireRange) end

---@public
---@param extraDamage float
---@return void
function IsoTrap:setExtraDamage(extraDamage) end

---@public
---@return int
function IsoTrap:getFireRange() end

---@public
---@param remoteControlID int
---@return void
function IsoTrap:setRemoteControlID(remoteControlID) end

---@public
---@param sensor boolean
---@return void
function IsoTrap:triggerExplosion(sensor) end

---@public
---@param sensorRange int
---@return void
function IsoTrap:setSensorRange(sensorRange) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoTrap:load(arg0, arg1, arg2) end

---Overrides:
---
---getObjectName in class IsoObject
---@public
---@return String
function IsoTrap:getObjectName() end

---@public
---@return InventoryItem
function IsoTrap:getItem() end

---@private
---@param arg0 HandWeapon
---@return void
function IsoTrap:initSprite(arg0) end

---@public
---@return void
function IsoTrap:removeFromWorld() end

---@public
---@param explosionPower int
---@return void
function IsoTrap:setExplosionPower(explosionPower) end

---@public
---@param smokeRange int
---@return void
function IsoTrap:setSmokeRange(smokeRange) end

---@private
---@return BaseSoundEmitter
function IsoTrap:getOrCreateEmitter() end

---@public
---@param noiseRange int
---@return void
function IsoTrap:setNoiseRange(noiseRange) end

---@public
---@return int
function IsoTrap:getTimerBeforeExplosion() end

---@public
---@return void
function IsoTrap:playExplosionSound() end

---@public
---@param timerBeforeExplosion int
---@return void
function IsoTrap:setTimerBeforeExplosion(timerBeforeExplosion) end

---@public
---@return String
function IsoTrap:getCountDownSound() end

---@public
---@param arg0 String
---@return void
function IsoTrap:setCountDownSound(arg0) end

---@public
---@return int
function IsoTrap:getSensorRange() end

---@private
---@return void
function IsoTrap:updateSounds() end

---Overrides:
---
---addToWorld in class IsoObject
---@public
---@return void
function IsoTrap:addToWorld() end

---@public
---@param explosionRange int
---@return void
function IsoTrap:setExplosionRange(explosionRange) end

---@public
---@return int
function IsoTrap:getExplosionRange() end

---@public
---@return int
function IsoTrap:getFirePower() end

---@public
---@return String
function IsoTrap:getExplosionSound() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoTrap:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param explosionSound String
---@return void
function IsoTrap:setExplosionSound(explosionSound) end

---@public
---@param arg0 IsoPlayer
---@param arg1 int
---@param arg2 int
---@return void
function IsoTrap:triggerRemote(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@return void
function IsoTrap:setNoiseDuration(arg0) end

---@public
---@return int
function IsoTrap:getRemoteControlID() end

---@public
---@return int
function IsoTrap:getNoiseDuration() end
