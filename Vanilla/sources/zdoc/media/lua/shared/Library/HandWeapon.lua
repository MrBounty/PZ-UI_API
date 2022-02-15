---@class HandWeapon : zombie.inventory.types.HandWeapon
---@field public WeaponLength float
---@field public SplatSize float
---@field private ammoPerShoot int
---@field private magazineType String
---@field protected angleFalloff boolean
---@field protected bCanBarracade boolean
---@field protected doSwingBeforeImpact float
---@field protected impactSound String
---@field protected knockBackOnNoDeath boolean
---@field protected maxAngle float
---@field protected maxDamage float
---@field protected maxHitCount int
---@field protected maxRange float
---@field protected ranged boolean
---@field protected minAngle float
---@field protected minDamage float
---@field protected minimumSwingTime float
---@field protected minRange float
---@field protected noiseFactor float
---@field protected otherHandRequire String
---@field protected otherHandUse boolean
---@field protected physicsObject String
---@field protected pushBackMod float
---@field protected rangeFalloff boolean
---@field protected shareDamage boolean
---@field protected soundRadius int
---@field protected soundVolume int
---@field protected splatBloodOnNoDeath boolean
---@field protected splatNumber int
---@field protected swingSound String
---@field protected swingTime float
---@field protected toHitModifier float
---@field protected useEndurance boolean
---@field protected useSelf boolean
---@field protected weaponSprite String
---@field private originalWeaponSprite String
---@field protected otherBoost float
---@field protected DoorDamage int
---@field protected doorHitSound String
---@field protected ConditionLowerChance int
---@field protected MultipleHitConditionAffected boolean
---@field protected shareEndurance boolean
---@field protected AlwaysKnockdown boolean
---@field protected EnduranceMod float
---@field protected KnockdownMod float
---@field protected CantAttackWithLowestEndurance boolean
---@field public bIsAimedFirearm boolean
---@field public bIsAimedHandWeapon boolean
---@field public RunAnim String
---@field public IdleAnim String
---@field public HitAngleMod float
---@field private SubCategory String
---@field private Categories ArrayList|Unknown
---@field private AimingPerkCritModifier int
---@field private AimingPerkRangeModifier float
---@field private AimingPerkHitChanceModifier float
---@field private HitChance int
---@field private AimingPerkMinAngleModifier float
---@field private RecoilDelay int
---@field private PiercingBullets boolean
---@field private soundGain float
---@field private scope WeaponPart
---@field private canon WeaponPart
---@field private clip WeaponPart
---@field private recoilpad WeaponPart
---@field private sling WeaponPart
---@field private stock WeaponPart
---@field private ClipSize int
---@field private reloadTime int
---@field private aimingTime int
---@field private minRangeRanged float
---@field private treeDamage int
---@field private bulletOutSound String
---@field private shellFallSound String
---@field private triggerExplosionTimer int
---@field private canBePlaced boolean
---@field private explosionRange int
---@field private explosionPower int
---@field private fireRange int
---@field private firePower int
---@field private smokeRange int
---@field private noiseRange int
---@field private extraDamage float
---@field private explosionTimer int
---@field private placedSprite String
---@field private canBeReused boolean
---@field private sensorRange int
---@field private critDmgMultiplier float
---@field private baseSpeed float
---@field private bloodLevel float
---@field private ammoBox String
---@field private insertAmmoStartSound String
---@field private insertAmmoSound String
---@field private insertAmmoStopSound String
---@field private ejectAmmoStartSound String
---@field private ejectAmmoSound String
---@field private ejectAmmoStopSound String
---@field private rackSound String
---@field private clickSound String
---@field private containsClip boolean
---@field private weaponReloadType String
---@field private rackAfterShoot boolean
---@field private roundChambered boolean
---@field private bSpentRoundChambered boolean
---@field private spentRoundCount int
---@field private jamGunChance float
---@field private isJammed boolean
---@field private modelWeaponPart ArrayList|Unknown
---@field private haveChamber boolean
---@field private bulletName String
---@field private damageCategory String
---@field private damageMakeHole boolean
---@field private hitFloorSound String
---@field private insertAllBulletsReload boolean
---@field private fireMode String
---@field private fireModePossibilities ArrayList|Unknown
---@field public ProjectileCount int
---@field public aimingMod float
---@field public CriticalChance float
---@field private hitSound String
HandWeapon = {}

---@public
---@return boolean @the shareDamage
function HandWeapon:isShareDamage() end

---@public
---@return float
---@overload fun(owner:IsoGameCharacter)
function HandWeapon:getMaxRange() end

---@public
---@param owner IsoGameCharacter
---@return float
function HandWeapon:getMaxRange(owner) end

---@public
---@param pushBackMod float @the pushBackMod to set
---@return void
function HandWeapon:setPushBackMod(pushBackMod) end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function HandWeapon:setFireModePossibilities(arg0) end

---@public
---@param arg0 String
---@return void
function HandWeapon:setDamageCategory(arg0) end

---@public
---@param maxAngle float @the maxAngle to set
---@return void
function HandWeapon:setMaxAngle(maxAngle) end

---@public
---@return int
function HandWeapon:getClipSize() end

---@public
---@return float
function HandWeapon:getCritDmgMultiplier() end

---@public
---@return String @the impactSound
function HandWeapon:getImpactSound() end

---@public
---@return int
function HandWeapon:getSensorRange() end

---@public
---@param soundGain float
---@return void
function HandWeapon:setSoundGain(soundGain) end

---@public
---@return float @the minRange
function HandWeapon:getMinRange() end

---@public
---@param shellFallSound String
---@return void
function HandWeapon:setShellFallSound(shellFallSound) end

---@public
---@return float
function HandWeapon:getContentsWeight() end

---@public
---@param useEndurance boolean @the useEndurance to set
---@return void
function HandWeapon:setUseEndurance(useEndurance) end

---@public
---@return float @the noiseFactor
function HandWeapon:getNoiseFactor() end

---@public
---@param arg0 int
---@return void
function HandWeapon:setAmmoPerShoot(arg0) end

---@public
---@param arg0 float
---@return void
function HandWeapon:setJamGunChance(arg0) end

---@public
---@param arg0 String
---@return void
function HandWeapon:setClickSound(arg0) end

---@public
---@return float
function HandWeapon:getSoundGain() end

---@public
---@return boolean @the knockBackOnNoDeath
function HandWeapon:isKnockBackOnNoDeath() end

---@public
---@return boolean @the AlwaysKnockdown
function HandWeapon:isAlwaysKnockdown() end

---@public
---@param arg0 String
---@return void
function HandWeapon:setMagazineType(arg0) end

---@public
---@return int
function HandWeapon:getReloadTime() end

---@public
---@return float
function HandWeapon:getAimingPerkRangeModifier() end

---@public
---@return String @the otherHandRequire
function HandWeapon:getOtherHandRequire() end

---@public
---@return String
function HandWeapon:getPlacedSprite() end

---@public
---@return String
function HandWeapon:getWeaponReloadType() end

---@public
---@param arg0 boolean
---@return void
function HandWeapon:setContainsClip(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return boolean
function HandWeapon:isReloadable(arg0) end

---@public
---@return boolean
function HandWeapon:IsWeapon() end

---@public
---@return int @the maxHitCount
function HandWeapon:getMaxHitCount() end

---@public
---@return float @the maxAngle
function HandWeapon:getMaxAngle() end

---@public
---@param arg0 String
---@return void
function HandWeapon:setOriginalWeaponSprite(arg0) end

---@public
---@param AlwaysKnockdown boolean @the AlwaysKnockdown to set
---@return void
function HandWeapon:setAlwaysKnockdown(AlwaysKnockdown) end

---@public
---@return String
function HandWeapon:getFireMode() end

---@public
---@return String
function HandWeapon:getHitFloorSound() end

---@public
---@return float @the minDamage
function HandWeapon:getMinDamage() end

---@public
---@return ArrayList|Unknown
function HandWeapon:getModelWeaponPart() end

---@public
---@param chr IsoGameCharacter
---@return float
function HandWeapon:getSpeedMod(chr) end

---@public
---@param arg0 String
---@return void
function HandWeapon:setAmmoBox(arg0) end

---@public
---@return String
function HandWeapon:getStaticModel() end

---@public
---@param aimingPerkRangeModifier float
---@return void
function HandWeapon:setAimingPerkRangeModifier(aimingPerkRangeModifier) end

---@public
---@param arg0 float
---@return void
function HandWeapon:setWeaponLength(arg0) end

---@public
---@return float @the minAngle
function HandWeapon:getMinAngle() end

---@public
---@return String
function HandWeapon:getRunAnim() end

---@public
---@param useSelf boolean @the useSelf to set
---@return void
function HandWeapon:setUseSelf(useSelf) end

---@public
---@param physicsObject String @the physicsObject to set
---@return void
function HandWeapon:setPhysicsObject(physicsObject) end

---@public
---@return String
function HandWeapon:getZombieHitSound() end

---@public
---@param triggerExplosionTimer int
---@return void
function HandWeapon:setTriggerExplosionTimer(triggerExplosionTimer) end

---@public
---@param part WeaponPart
---@return void
function HandWeapon:detachWeaponPart(part) end

---@public
---@param splatNumber int @the splatNumber to set
---@return void
function HandWeapon:setSplatNumber(splatNumber) end

---@public
---@param doSwingBeforeImpact float @the doSwingBeforeImpact to set
---@return void
function HandWeapon:setDoSwingBeforeImpact(doSwingBeforeImpact) end

---@public
---@param firePower int
---@return void
function HandWeapon:setFirePower(firePower) end

---@public
---@param swingTime float @the swingTime to set
---@return void
function HandWeapon:setSwingTime(swingTime) end

---@public
---@param placedSprite String
---@return void
function HandWeapon:setPlacedSprite(placedSprite) end

---@public
---@return boolean @the otherHandUse
function HandWeapon:isOtherHandUse() end

---@public
---@return boolean
function HandWeapon:canBeReused() end

---@public
---@param hitSound String
---@return void
function HandWeapon:setZombieHitSound(hitSound) end

---@public
---@return float @the KnockdownMod
function HandWeapon:getKnockdownMod() end

---@public
---@param splatBloodOnNoDeath boolean @the splatBloodOnNoDeath to set
---@return void
function HandWeapon:setSplatBloodOnNoDeath(splatBloodOnNoDeath) end

---@public
---@return int
function HandWeapon:getAimingTime() end

---@public
---@param hitChance int
---@return void
function HandWeapon:setHitChance(hitChance) end

---@public
---@return boolean
function HandWeapon:isPiercingBullets() end

---@public
---@return boolean
function HandWeapon:isAimedHandWeapon() end

---@public
---@param arg0 boolean
---@return void
function HandWeapon:setJammed(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return InventoryItem
function HandWeapon:getBestMagazine(arg0) end

---@public
---@param maxHitCount int @the maxHitCount to set
---@return void
function HandWeapon:setMaxHitCount(maxHitCount) end

---@public
---@param piercingBullets boolean
---@return void
function HandWeapon:setPiercingBullets(piercingBullets) end

---@public
---@param otherBoost float @the otherBoost to set
---@return void
function HandWeapon:setOtherBoost(otherBoost) end

---@public
---@param otherHandRequire String @the otherHandRequire to set
---@return void
function HandWeapon:setOtherHandRequire(otherHandRequire) end

---@public
---@param bulletOutSound String
---@return void
function HandWeapon:setBulletOutSound(bulletOutSound) end

---@public
---@return boolean @the ranged
function HandWeapon:isRanged() end

---@public
---@param impactSound String @the impactSound to set
---@return void
function HandWeapon:setImpactSound(impactSound) end

---@public
---@return boolean @the bCanBarracade
function HandWeapon:isCanBarracade() end

---Overrides:
---
---DoTooltip in class InventoryItem
---@public
---@param tooltipUI ObjectTooltip
---@param layout ObjectTooltip.Layout
---@return void
function HandWeapon:DoTooltip(tooltipUI, layout) end

---@public
---@return boolean @the angleFalloff
function HandWeapon:isAngleFalloff() end

---@public
---@return boolean
function HandWeapon:isInsertAllBulletsReload() end

---@public
---@return String
function HandWeapon:getSubCategory() end

---@public
---@param type String
---@return WeaponPart
function HandWeapon:getWeaponPart(type) end

---@public
---@param minRange float @the minRange to set
---@return void
function HandWeapon:setMinRange(minRange) end

---@public
---@return float
function HandWeapon:getCriticalChance() end

---throws java.io.IOException
---
---Overrides:
---
---save in class InventoryItem
---@public
---@param output ByteBuffer
---@param net boolean
---@return void
function HandWeapon:save(output, net) end

---@public
---@param CantAttackWithLowestEndurance boolean @the CantAttackWithLowestEndurance to set
---@return void
function HandWeapon:setCantAttackWithLowestEndurance(CantAttackWithLowestEndurance) end

---@public
---@param explosionRange int
---@return void
function HandWeapon:setExplosionRange(explosionRange) end

---@public
---@param maxDamage float @the maxDamage to set
---@return void
function HandWeapon:setMaxDamage(maxDamage) end

---@public
---@return boolean @the rangeFalloff
function HandWeapon:isRangeFalloff() end

---@public
---@param soundRadius int @the soundRadius to set
---@return void
function HandWeapon:setSoundRadius(soundRadius) end

---@public
---@return int
function HandWeapon:getTriggerExplosionTimer() end

---@public
---@param arg0 boolean
---@return void
function HandWeapon:setSpentRoundChambered(arg0) end

---@public
---@return String
function HandWeapon:getShellFallSound() end

---@public
---@return int @the ConditionLowerChance
function HandWeapon:getConditionLowerChance() end

---@public
---@return String
function HandWeapon:getEjectAmmoSound() end

---@public
---@return boolean
function HandWeapon:isDamageMakeHole() end

---@public
---@return String
function HandWeapon:getAmmoBox() end

---@public
---@param arg0 float
---@return void
function HandWeapon:setBloodLevel(arg0) end

---@public
---@return int
function HandWeapon:getSpentRoundCount() end

---@public
---@return boolean @the useSelf
function HandWeapon:isUseSelf() end

---@public
---@return boolean
function HandWeapon:isInstantExplosion() end

---@public
---@return float
function HandWeapon:getExtraDamage() end

---@public
---@param arg0 boolean
---@return void
function HandWeapon:setCanBeReused(arg0) end

---@public
---@return boolean @the MultipleHitConditionAffected
function HandWeapon:isMultipleHitConditionAffected() end

---@public
---@param stock WeaponPart
---@return void
function HandWeapon:setStock(stock) end

---@public
---@param aimingPerkMinAngleModifier float
---@return void
function HandWeapon:setAimingPerkMinAngleModifier(aimingPerkMinAngleModifier) end

---@public
---@return int @the soundVolume
function HandWeapon:getSoundVolume() end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function HandWeapon:setModelWeaponPart(arg0) end

---@public
---@return int
function HandWeapon:getProjectileCount() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function HandWeapon:load(arg0, arg1) end

---@public
---@param aimingTime int
---@return void
function HandWeapon:setAimingTime(aimingTime) end

---@public
---@param doorHitSound String @the doorHitSound to set
---@return void
function HandWeapon:setDoorHitSound(doorHitSound) end

---@public
---@return float @the pushBackMod
function HandWeapon:getPushBackMod() end

---@public
---@param maxRange float @the maxRange to set
---@return void
function HandWeapon:setMaxRange(maxRange) end

---@public
---@return int
function HandWeapon:getSmokeRange() end

---@public
---@return int
function HandWeapon:getFirePower() end

---@public
---@param soundVolume int @the soundVolume to set
---@return void
function HandWeapon:setSoundVolume(soundVolume) end

---@public
---@return int
function HandWeapon:getExplosionTimer() end

---@public
---@param criticalChance float
---@return void
function HandWeapon:setCriticalChance(criticalChance) end

---@public
---@return int
function HandWeapon:getNoiseDuration() end

---@public
---@return int
function HandWeapon:getExplosionRange() end

---Overrides:
---
---getCategory in class InventoryItem
---@public
---@return String
function HandWeapon:getCategory() end

---@public
---@return ArrayList|Unknown
function HandWeapon:getFireModePossibilities() end

---@public
---@param DoorDamage int @the DoorDamage to set
---@return void
function HandWeapon:setDoorDamage(DoorDamage) end

---@public
---@return String
function HandWeapon:getBulletOutSound() end

---@public
---@param ConditionLowerChance int @the ConditionLowerChance to set
---@return void
function HandWeapon:setConditionLowerChance(ConditionLowerChance) end

---@public
---@param sensorRange int
---@return void
function HandWeapon:setSensorRange(sensorRange) end

---@public
---@param arg0 boolean
---@return void
function HandWeapon:setDamageMakeHole(arg0) end

---@public
---@param noiseRange int
---@return void
function HandWeapon:setNoiseRange(noiseRange) end

---@public
---@return boolean
function HandWeapon:isRoundChambered() end

---@public
---@param arg0 String
---@return void
function HandWeapon:setRackSound(arg0) end

---@public
---@param chr IsoGameCharacter
---@return float
function HandWeapon:getKnockbackMod(chr) end

---@public
---@param arg0 String
---@return void
function HandWeapon:setHitFloorSound(arg0) end

---@public
---@param canon WeaponPart
---@return void
function HandWeapon:setCanon(canon) end

---@public
---@return WeaponPart
function HandWeapon:getStock() end

---@public
---@return WeaponPart
function HandWeapon:getClip() end

---@public
---@param knockBackOnNoDeath boolean @the knockBackOnNoDeath to set
---@return void
function HandWeapon:setKnockBackOnNoDeath(knockBackOnNoDeath) end

---@public
---@param type String
---@param part WeaponPart
---@return void
function HandWeapon:setWeaponPart(type, part) end

---@public
---@return float
function HandWeapon:getMinRangeRanged() end

---@public
---@return boolean
function HandWeapon:isJammed() end

---@public
---@return String @the physicsObject
function HandWeapon:getPhysicsObject() end

---@public
---@return String @the weaponSprite
function HandWeapon:getWeaponSprite() end

---@public
---@return float
function HandWeapon:getAimingPerkHitChanceModifier() end

---@public
---@return boolean @the shareEndurance
function HandWeapon:isShareEndurance() end

---@public
---@return float @the minimumSwingTime
function HandWeapon:getMinimumSwingTime() end

---@public
---@param shareEndurance boolean @the shareEndurance to set
---@return void
function HandWeapon:setShareEndurance(shareEndurance) end

---@public
---@return String
function HandWeapon:getRackSound() end

---@public
---@param noiseFactor float @the noiseFactor to set
---@return void
function HandWeapon:setNoiseFactor(noiseFactor) end

---@public
---@param part WeaponPart
---@return void
---@overload fun(arg0:WeaponPart, arg1:boolean)
function HandWeapon:attachWeaponPart(part) end

---@public
---@param arg0 WeaponPart
---@param arg1 boolean
---@return void
function HandWeapon:attachWeaponPart(arg0, arg1) end

---@public
---@param arg0 boolean
---@return void
function HandWeapon:setInsertAllBulletsReload(arg0) end

---@public
---@param arg0 String
---@return void
function HandWeapon:setFireMode(arg0) end

---@public
---@return String @the swingSound
function HandWeapon:getSwingSound() end

---@public
---@return int
function HandWeapon:getAimingPerkCritModifier() end

---@public
---@return float
function HandWeapon:getSplatSize() end

---@public
---@param minimumSwingTime float @the minimumSwingTime to set
---@return void
function HandWeapon:setMinimumSwingTime(minimumSwingTime) end

---@public
---@return String
function HandWeapon:getMagazineType() end

---@public
---@return boolean @the useEndurance
function HandWeapon:isUseEndurance() end

---@public
---@param arg0 int
---@return void
function HandWeapon:setProjectileCount(arg0) end

---@public
---@param EnduranceMod float @the EnduranceMod to set
---@return void
function HandWeapon:setEnduranceMod(EnduranceMod) end

---@public
---@return WeaponPart
function HandWeapon:getSling() end

---@public
---@return boolean
function HandWeapon:isManuallyRemoveSpentRounds() end

---@public
---@return int @the splatNumber
function HandWeapon:getSplatNumber() end

---@public
---@return String
function HandWeapon:getInsertAmmoStartSound() end

---@public
---@return float @the EnduranceMod
function HandWeapon:getEnduranceMod() end

---@public
---@return int
function HandWeapon:getNoiseRange() end

---@public
---@return boolean @the CantAttackWithLowestEndurance
function HandWeapon:isCantAttackWithLowestEndurance() end

---@public
---@param chr IsoGameCharacter
---@return float
function HandWeapon:getRangeMod(chr) end

---@public
---@param arg0 boolean
---@return void
function HandWeapon:setRoundChambered(arg0) end

---@public
---@return ArrayList|Unknown
function HandWeapon:getAllWeaponParts() end

---@public
---@param minDamage float @the minDamage to set
---@return void
function HandWeapon:setMinDamage(minDamage) end

---@public
---@param recoilDelay int
---@return void
function HandWeapon:setRecoilDelay(recoilDelay) end

---@public
---@param weaponSprite String @the weaponSprite to set
---@return void
function HandWeapon:setWeaponSprite(weaponSprite) end

---@public
---@param recoilpad WeaponPart
---@return void
function HandWeapon:setRecoilpad(recoilpad) end

---@public
---@return String
function HandWeapon:getEjectAmmoStartSound() end

---@public
---@param smokeRange int
---@return void
function HandWeapon:setSmokeRange(smokeRange) end

---@public
---@param arg0 String
---@return void
function HandWeapon:setWeaponReloadType(arg0) end

---@public
---@return int
function HandWeapon:getTreeDamage() end

---@public
---@return boolean
function HandWeapon:isAimedFirearm() end

---@public
---@param arg0 int
---@return void
function HandWeapon:setSpentRoundCount(arg0) end

---@public
---@return float
function HandWeapon:getBaseSpeed() end

---@public
---@return float @the doSwingBeforeImpact
function HandWeapon:getDoSwingBeforeImpact() end

---@public
---@param sling WeaponPart
---@return void
function HandWeapon:setSling(sling) end

---@public
---@return boolean
function HandWeapon:isRackAfterShoot() end

---@public
---@param otherHandUse boolean @the otherHandUse to set
---@return void
function HandWeapon:setOtherHandUse(otherHandUse) end

---@public
---@return String
function HandWeapon:getDamageCategory() end

---@public
---@param arg0 boolean
---@return void
function HandWeapon:setRackAfterShoot(arg0) end

---@public
---@return float @the swingTime
function HandWeapon:getSwingTime() end

---@public
---@return boolean
function HandWeapon:isAimed() end

---@public
---@param minRangeRanged float
---@return void
function HandWeapon:setMinRangeRanged(minRangeRanged) end

---@public
---@return boolean
function HandWeapon:haveChamber() end

---@public
---@param KnockdownMod float @the KnockdownMod to set
---@return void
function HandWeapon:setKnockdownMod(KnockdownMod) end

---Overrides:
---
---getScore in class InventoryItem
---@public
---@param desc SurvivorDesc
---@return float
function HandWeapon:getScore(desc) end

---@public
---@param categories ArrayList|String
---@return void
function HandWeapon:setCategories(categories) end

---@public
---@return float @the maxDamage
function HandWeapon:getMaxDamage() end

---@public
---@return String
function HandWeapon:getInsertAmmoSound() end

---@public
---@param explosionPower int
---@return void
function HandWeapon:setExplosionPower(explosionPower) end

---@public
---@return float
function HandWeapon:getJamGunChance() end

---@public
---@param MultipleHitConditionAffected boolean @the MultipleHitConditionAffected to set
---@return void
function HandWeapon:setMultipleHitConditionAffected(MultipleHitConditionAffected) end

---@public
---@param bCanBarracade boolean @the bCanBarracade to set
---@return void
function HandWeapon:setCanBarracade(bCanBarracade) end

---@public
---@param canBePlaced boolean
---@return void
function HandWeapon:setCanBePlaced(canBePlaced) end

---@public
---@return int
function HandWeapon:getSaveType() end

---@public
---@return boolean
function HandWeapon:isContainsClip() end

---@public
---@param scope WeaponPart
---@return void
function HandWeapon:setScope(scope) end

---@public
---@return WeaponPart
function HandWeapon:getRecoilpad() end

---@public
---@return boolean
function HandWeapon:isSpentRoundChambered() end

---@public
---@param angleFalloff boolean @the angleFalloff to set
---@return void
function HandWeapon:setAngleFalloff(angleFalloff) end

---@public
---@return int
function HandWeapon:getHitChance() end

---@public
---@param reloadTime int
---@return void
function HandWeapon:setReloadTime(reloadTime) end

---@public
---@return float
function HandWeapon:getStopPower() end

---@public
---@param arg0 float
---@return void
function HandWeapon:setBaseSpeed(arg0) end

---@public
---@param explosionTimer int
---@return void
function HandWeapon:setExplosionTimer(explosionTimer) end

---@public
---@return float @the toHitModifier
function HandWeapon:getToHitModifier() end

---@public
---@param chr IsoGameCharacter
---@return float
function HandWeapon:getFatigueMod(chr) end

---@public
---@param shareDamage boolean @the shareDamage to set
---@return void
function HandWeapon:setShareDamage(shareDamage) end

---@public
---@return WeaponPart
function HandWeapon:getScope() end

---@public
---@return ArrayList|String
function HandWeapon:getCategories() end

---@public
---@return String @the doorHitSound
function HandWeapon:getDoorHitSound() end

---@public
---@return float
function HandWeapon:getAimingPerkMinAngleModifier() end

---@public
---@return String
function HandWeapon:getEjectAmmoStopSound() end

---@public
---@return String
function HandWeapon:getClickSound() end

---@public
---@return String
function HandWeapon:getInsertAmmoStopSound() end

---@public
---@param extraDamage float
---@return void
function HandWeapon:setExtraDamage(extraDamage) end

---@public
---@param clip WeaponPart
---@return void
function HandWeapon:setClip(clip) end

---@public
---@param chr IsoGameCharacter
---@return float
function HandWeapon:getDamageMod(chr) end

---@public
---@return int
function HandWeapon:getAmmoPerShoot() end

---@public
---@return int @the DoorDamage
function HandWeapon:getDoorDamage() end

---@public
---@param chr IsoGameCharacter
---@return float
function HandWeapon:getToHitMod(chr) end

---@public
---@param arg0 boolean
---@return void
function HandWeapon:setHaveChamber(arg0) end

---@public
---@param fireRange int
---@return void
function HandWeapon:setFireRange(fireRange) end

---@public
---@param treeDamage int
---@return void
function HandWeapon:setTreeDamage(treeDamage) end

---@public
---@param arg0 float
---@return void
function HandWeapon:setCritDmgMultiplier(arg0) end

---@public
---@return boolean
function HandWeapon:canBePlaced() end

---Overrides:
---
---CanStack in class InventoryItem
---@public
---@param item InventoryItem
---@return boolean
function HandWeapon:CanStack(item) end

---@public
---@return int @the soundRadius
function HandWeapon:getSoundRadius() end

---@public
---@return float
function HandWeapon:getBloodLevel() end

---@public
---@return boolean @the splatBloodOnNoDeath
function HandWeapon:isSplatBloodOnNoDeath() end

---@public
---@return String
function HandWeapon:getOriginalWeaponSprite() end

---@public
---@return void
function HandWeapon:randomizeBullets() end

---@public
---@return float @the otherBoost
function HandWeapon:getOtherBoost() end

---@public
---@return float
function HandWeapon:getAimingMod() end

---@public
---@param aimingPerkHitChanceModifier float
---@return void
function HandWeapon:setAimingPerkHitChanceModifier(aimingPerkHitChanceModifier) end

---@public
---@return int
function HandWeapon:getRecoilDelay() end

---@public
---@param subcategory String
---@return void
function HandWeapon:setSubCategory(subcategory) end

---@public
---@param minAngle float @the minAngle to set
---@return void
function HandWeapon:setMinAngle(minAngle) end

---@public
---@param capacity int
---@return void
function HandWeapon:setClipSize(capacity) end

---@public
---@param toHitModifier float @the toHitModifier to set
---@return void
function HandWeapon:setToHitModifier(toHitModifier) end

---@public
---@param rangeFalloff boolean @the rangeFalloff to set
---@return void
function HandWeapon:setRangeFalloff(rangeFalloff) end

---@public
---@param ranged boolean @the ranged to set
---@return void
function HandWeapon:setRanged(ranged) end

---@public
---@param aimingPerkCritModifier int
---@return void
function HandWeapon:setAimingPerkCritModifier(aimingPerkCritModifier) end

---@public
---@return int
function HandWeapon:getExplosionPower() end

---@public
---@return int
function HandWeapon:getFireRange() end

---@public
---@param swingSound String @the swingSound to set
---@return void
function HandWeapon:setSwingSound(swingSound) end

---@private
---@param arg0 String
---@param arg1 ArrayList|Unknown
---@return void
function HandWeapon:addPartToList(arg0, arg1) end

---@public
---@return WeaponPart
function HandWeapon:getCanon() end
