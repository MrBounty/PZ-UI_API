---@class BodyPart : zombie.characters.BodyDamage.BodyPart
---@field Type BodyPartType
---@field private BiteDamage float
---@field private BleedDamage float
---@field private DamageScaler float
---@field private Health float
---@field private bandaged boolean
---@field private bitten boolean
---@field private bleeding boolean
---@field private IsBleedingStemmed boolean
---@field private IsCortorised boolean
---@field private scratched boolean
---@field private stitched boolean
---@field private deepWounded boolean
---@field private IsInfected boolean
---@field private IsFakeInfected boolean
---@field private ParentChar IsoGameCharacter
---@field private bandageLife float
---@field private scratchTime float
---@field private biteTime float
---@field private alcoholicBandage boolean
---@field private stiffness float
---@field private woundInfectionLevel float
---@field private infectedWound boolean
---@field private ScratchDamage float
---@field private CutDamage float
---@field private WoundDamage float
---@field private BurnDamage float
---@field private BulletDamage float
---@field private FractureDamage float
---@field private bleedingTime float
---@field private deepWoundTime float
---@field private haveGlass boolean
---@field private stitchTime float
---@field private alcoholLevel float
---@field private additionalPain float
---@field private bandageType String
---@field private getBandageXp boolean
---@field private getStitchXp boolean
---@field private getSplintXp boolean
---@field private fractureTime float
---@field private splint boolean
---@field private splintFactor float
---@field private haveBullet boolean
---@field private burnTime float
---@field private needBurnWash boolean
---@field private lastTimeBurnWash float
---@field private splintItem String
---@field private plantainFactor float
---@field private comfreyFactor float
---@field private garlicFactor float
---@field private cutTime float
---@field private cut boolean
---@field private scratchSpeedModifier float
---@field private cutSpeedModifier float
---@field private burnSpeedModifier float
---@field private deepWoundSpeedModifier float
---@field private wetness float
---@field protected thermalNode Thermoregulator.ThermalNode
BodyPart = {}

---@public
---@param biteTime float
---@return void
function BodyPart:setBiteTime(biteTime) end

---@public
---@param Bitten boolean
---@return void
---@overload fun(Bitten:boolean, Infected:boolean)
function BodyPart:SetBitten(Bitten) end

---@public
---@param Bitten boolean
---@param Infected boolean
---@return void
function BodyPart:SetBitten(Bitten, Infected) end

---@public
---@param alcoholLevel float
---@return void
function BodyPart:setAlcoholLevel(alcoholLevel) end

---@public
---@return float
function BodyPart:getBurnTime() end

---@public
---@param haveBullet boolean
---@param doctorLevel int
---@return void
function BodyPart:setHaveBullet(haveBullet, doctorLevel) end

---@public
---@return void
function BodyPart:generateDeepShardWound() end

---@public
---@param arg0 boolean
---@param arg1 boolean
---@return void
function BodyPart:setScratched(arg0, arg1) end

---@public
---@return String
function BodyPart:getSplintItem() end

---@public
---@return float
function BodyPart:getFractureTime() end

---@public
---@param NewHealth float
---@return void
function BodyPart:SetHealth(NewHealth) end

---@public
---@return boolean
function BodyPart:bitten() end

---@public
---@return boolean
function BodyPart:isBurnt() end

---@public
---@param arg0 float
---@return void
function BodyPart:setBurnSpeedModifier(arg0) end

---@public
---@param additionalPain float
---@return void
function BodyPart:setAdditionalPain(additionalPain) end

---@public
---@param splint boolean
---@param splintFactor float
---@return void
function BodyPart:setSplint(splint, splintFactor) end

---@public
---@return boolean
function BodyPart:isGetSplintXp() end

---@public
---@return float
function BodyPart:getDeepWoundTime() end

---@public
---@return float
function BodyPart:getHealth() end

---@public
---@param arg0 float
---@return void
function BodyPart:setCutTime(arg0) end

---@public
---@param arg0 float
---@return void
function BodyPart:setCutSpeedModifier(arg0) end

---@public
---@param arg0 float
---@return void
function BodyPart:setWetness(arg0) end

---@public
---@return float
---@overload fun(arg0:boolean)
function BodyPart:getAdditionalPain() end

---@public
---@param arg0 boolean
---@return float
function BodyPart:getAdditionalPain(arg0) end

---@public
---@param Val float
---@return void
function BodyPart:AddHealth(Val) end

---@public
---@param arg0 float
---@return void
function BodyPart:setComfreyFactor(arg0) end

---@public
---@return boolean
function BodyPart:scratched() end

---@public
---@param Val float
---@return void
function BodyPart:ReduceHealth(Val) end

---@public
---@param getStitchXp boolean
---@return void
function BodyPart:setGetStitchXp(getStitchXp) end

---@public
---@param BleedingStemmed boolean
---@return void
function BodyPart:SetBleedingStemmed(BleedingStemmed) end

---@public
---@return boolean
function BodyPart:bleeding() end

---@public
---@return int
function BodyPart:getIndex() end

---@public
---@param arg0 boolean
---@return void
---@overload fun(arg0:boolean, arg1:boolean)
function BodyPart:setCut(arg0) end

---@public
---@param arg0 boolean
---@param arg1 boolean
---@return void
function BodyPart:setCut(arg0, arg1) end

---@public
---@return float
function BodyPart:getSplintFactor() end

---@public
---@return String
function BodyPart:getBandageType() end

---@public
---@return float
function BodyPart:getBiteTime() end

---@public
---@param scratchTime float
---@return void
function BodyPart:setScratchTime(scratchTime) end

---@public
---@return boolean
function BodyPart:bandaged() end

---@public
---@return boolean
function BodyPart:haveGlass() end

---@public
---@return float
function BodyPart:getDistToCore() end

---@public
---@param bandageType String
---@return void
function BodyPart:setBandageType(bandageType) end

---@public
---@return float
function BodyPart:getInnerTemperature() end

---@public
---@return boolean
function BodyPart:haveBullet() end

---@public
---@param bandageLife float
---@return void
function BodyPart:setBandageLife(bandageLife) end

---@public
---@param Val float
---@return void
function BodyPart:AddDamage(Val) end

---@public
---@param infectedWound boolean
---@return void
function BodyPart:setInfectedWound(infectedWound) end

---@public
---@return void
function BodyPart:generateDeepWound() end

---@public
---@return boolean
function BodyPart:IsBleedingStemmed() end

---@public
---@return float
function BodyPart:getGarlicFactor() end

---@public
---@param arg0 float
---@return void
function BodyPart:setGarlicFactor(arg0) end

---@public
---@return float
function BodyPart:getAlcoholLevel() end

---@public
---@param damage float
---@return void
function BodyPart:damageFromFirearm(damage) end

---@public
---@param needBurnWash boolean
---@return void
function BodyPart:setNeedBurnWash(needBurnWash) end

---@public
---@return boolean
function BodyPart:IsCortorised() end

---@public
---@param Bandaged boolean
---@param bandageLife float
---@return void
---@overload fun(Bandaged:boolean, bandageLife:float, isAlcoholic:boolean, bandageType:String)
function BodyPart:setBandaged(Bandaged, bandageLife) end

---@public
---@param Bandaged boolean
---@param bandageLife float
---@param isAlcoholic boolean
---@param bandageType String
---@return void
function BodyPart:setBandaged(Bandaged, bandageLife, isAlcoholic, bandageType) end

---@public
---@return boolean
function BodyPart:isSplint() end

---@public
---@return boolean
function BodyPart:IsFakeInfected() end

---@public
---@return float
function BodyPart:getLastTimeBurnWash() end

---@public
---@param bleedingTime float
---@return void
function BodyPart:setBleedingTime(bleedingTime) end

---@public
---@return boolean
function BodyPart:isDeepWounded() end

---@public
---@return float
function BodyPart:getPlantainFactor() end

---@public
---@return float
function BodyPart:getBandageLife() end

---@public
---@param arg0 BodyPart
---@param arg1 BodyDamageSync.Updater
---@return void
---@overload fun(arg0:ByteBuffer, arg1:byte)
function BodyPart:sync(arg0, arg1) end

---@public
---@param arg0 ByteBuffer
---@param arg1 byte
---@return void
function BodyPart:sync(arg0, arg1) end

---@public
---@param Scratched boolean
---@return void
function BodyPart:SetScratchedWeapon(Scratched) end

---@public
---@param Wounded boolean
---@return void
function BodyPart:setDeepWounded(Wounded) end

---@public
---@return float
function BodyPart:getWoundInfectionLevel() end

---@public
---@param arg0 float
---@return void
function BodyPart:setScratchSpeedModifier(arg0) end

---@public
---@param Scratched boolean
---@return void
function BodyPart:SetScratchedWindow(Scratched) end

---@public
---@param arg0 float
---@return void
function BodyPart:setDeepWoundSpeedModifier(arg0) end

---@public
---@return Thermoregulator.ThermalNode
function BodyPart:getThermalNode() end

---@public
---@return float
function BodyPart:getCutTime() end

---@public
---@return float
function BodyPart:getSkinTemperature() end

---@public
---@param splintItem String
---@return void
function BodyPart:setSplintItem(splintItem) end

---@public
---@return void
function BodyPart:setBurned() end

---@public
---@param fractureTime float
---@return void
function BodyPart:setFractureTime(fractureTime) end

---@public
---@param infectedWound float
---@return void
function BodyPart:setWoundInfectionLevel(infectedWound) end

---@public
---@return void
function BodyPart:DisableFakeInfection() end

---@public
---@return boolean
function BodyPart:isGetBandageXp() end

---@public
---@param Stitched boolean
---@return void
function BodyPart:setStitched(Stitched) end

---@public
---@param arg0 float
---@return void
function BodyPart:setStiffness(arg0) end

---@public
---@return float
function BodyPart:getBleedingTime() end

---@public
---@return boolean
function BodyPart:deepWounded() end

---@public
---@return boolean
function BodyPart:IsInfected() end

---@public
---@return float
function BodyPart:getSkinSurface() end

---@public
---@param inf boolean
---@return void
function BodyPart:SetInfected(inf) end

---@public
---@return void
function BodyPart:RestoreToFullHealth() end

---@public
---@param deepWoundTime float
---@return void
function BodyPart:setDeepWoundTime(deepWoundTime) end

---@public
---@param burnTime float
---@return void
function BodyPart:setBurnTime(burnTime) end

---@public
---@return boolean
function BodyPart:isInfectedWound() end

---@public
---@param getSplintXp boolean
---@return void
function BodyPart:setGetSplintXp(getSplintXp) end

---@public
---@return float
function BodyPart:getScratchSpeedModifier() end

---@public
---@return float
function BodyPart:getComfreyFactor() end

---@public
---@param arg0 float
---@return void
function BodyPart:setPlantainFactor(arg0) end

---@public
---@return float
function BodyPart:getDeepWoundSpeedModifier() end

---@public
---@param splintFactor float
---@return void
function BodyPart:setSplintFactor(splintFactor) end

---@public
---@param haveGlass boolean
---@return void
function BodyPart:setHaveGlass(haveGlass) end

---@public
---@return boolean
function BodyPart:HasInjury() end

---@public
---@param Cortorised boolean
---@return void
function BodyPart:SetCortorised(Cortorised) end

---@public
---@return float
function BodyPart:getBurnSpeedModifier() end

---@public
---@param Bleeding boolean
---@return void
function BodyPart:setBleeding(Bleeding) end

---@public
---@return boolean
function BodyPart:isCut() end

---@public
---@return float
function BodyPart:getPain() end

---@public
---@return boolean
function BodyPart:isGetStitchXp() end

---@public
---@return void
function BodyPart:DamageUpdate() end

---@public
---@return boolean
function BodyPart:isBandageDirty() end

---@public
---@return BodyPartType
function BodyPart:getType() end

---@public
---@param inf boolean
---@return void
function BodyPart:SetFakeInfected(inf) end

---@public
---@return float
function BodyPart:getCutSpeedModifier() end

---@public
---@param arg0 int
---@return void
function BodyPart:generateZombieInfection(arg0) end

---@public
---@return float
function BodyPart:getScratchTime() end

---@public
---@return boolean
function BodyPart:stitched() end

---@public
---@param stitchTime float
---@return void
function BodyPart:setStitchTime(stitchTime) end

---@public
---@return float
function BodyPart:getStitchTime() end

---@public
---@param lastTimeBurnWash float
---@return void
function BodyPart:setLastTimeBurnWash(lastTimeBurnWash) end

---@public
---@return float
function BodyPart:getStiffness() end

---@public
---@return void
function BodyPart:generateBleeding() end

---@public
---@param getBandageXp boolean
---@return void
function BodyPart:setGetBandageXp(getBandageXp) end

---@public
---@return boolean
function BodyPart:isNeedBurnWash() end

---@public
---@return float
function BodyPart:getWetness() end
