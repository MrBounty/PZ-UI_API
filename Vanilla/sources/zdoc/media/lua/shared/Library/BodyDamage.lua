---@class BodyDamage : zombie.characters.BodyDamage.BodyDamage
---@field public BodyParts ArrayList|BodyPart
---@field public BodyPartsLastState ArrayList|Unknown
---@field public DamageModCount int
---@field public InfectionGrowthRate float
---@field public InfectionLevel float
---@field public IsInfected boolean
---@field public InfectionTime float
---@field public InfectionMortalityDuration float
---@field public FakeInfectionLevel float
---@field public IsFakeInfected boolean
---@field public OverallBodyHealth float
---@field public StandardHealthAddition float
---@field public ReducedHealthAddition float
---@field public SeverlyReducedHealthAddition float
---@field public SleepingHealthAddition float
---@field public HealthFromFood float
---@field public HealthReductionFromSevereBadMoodles float
---@field public StandardHealthFromFoodTime int
---@field public HealthFromFoodTimer float
---@field public BoredomLevel float
---@field public BoredomDecreaseFromReading float
---@field public InitialThumpPain float
---@field public InitialScratchPain float
---@field public InitialBitePain float
---@field public InitialWoundPain float
---@field public ContinualPainIncrease float
---@field public PainReductionFromMeds float
---@field public StandardPainReductionWhenWell float
---@field public OldNumZombiesVisible int
---@field public CurrentNumZombiesVisible int
---@field public PanicIncreaseValue float
---@field public PanicIncreaseValueFrame float
---@field public PanicReductionValue float
---@field public DrunkIncreaseValue float
---@field public DrunkReductionValue float
---@field public IsOnFire boolean
---@field public BurntToDeath boolean
---@field public Wetness float
---@field public CatchACold float
---@field public HasACold boolean
---@field public ColdStrength float
---@field public ColdProgressionRate float
---@field public TimeToSneezeOrCough int
---@field public MildColdSneezeTimerMin int
---@field public MildColdSneezeTimerMax int
---@field public ColdSneezeTimerMin int
---@field public ColdSneezeTimerMax int
---@field public NastyColdSneezeTimerMin int
---@field public NastyColdSneezeTimerMax int
---@field public SneezeCoughActive int
---@field public SneezeCoughTime int
---@field public SneezeCoughDelay int
---@field public UnhappynessLevel float
---@field public ColdDamageStage float
---@field public ParentChar IsoGameCharacter
---@field private FoodSicknessLevel float
---@field private RemotePainLevel int
---@field private Temperature float
---@field private lastTemperature float
---@field private PoisonLevel float
---@field private reduceFakeInfection boolean
---@field private painReduction float
---@field private coldReduction float
---@field private thermoregulator Thermoregulator
---@field public InfectionLevelToZombify float
---@field behindStr String
---@field leftStr String
---@field rightStr String
BodyDamage = {}

---@public
---@return float @the DrunkIncreaseValue
function BodyDamage:getDrunkIncreaseValue() end

---@public
---@param BodyPart BodyPartType
---@param Wounded boolean
---@return void
---@overload fun(BodyPartIndex:int, Wounded:boolean)
function BodyDamage:SetWounded(BodyPart, Wounded) end

---@public
---@param BodyPartIndex int
---@param Wounded boolean
---@return void
function BodyDamage:SetWounded(BodyPartIndex, Wounded) end

---@public
---@param BodyPart BodyPartType
---@return boolean
---@overload fun(BodyPartIndex:int)
function BodyDamage:IsScratched(BodyPart) end

---@public
---@param BodyPartIndex int
---@return boolean
function BodyDamage:IsScratched(BodyPartIndex) end

---@public
---@param MildColdSneezeTimerMax int @the MildColdSneezeTimerMax to set
---@return void
function BodyDamage:setMildColdSneezeTimerMax(MildColdSneezeTimerMax) end

---@public
---@param ColdSneezeTimerMax int @the ColdSneezeTimerMax to set
---@return void
function BodyDamage:setColdSneezeTimerMax(ColdSneezeTimerMax) end

---@public
---@return float
function BodyDamage:getHealth() end

---@public
---@param BoredomLevel float @the BoredomLevel to set
---@return void
function BodyDamage:setBoredomLevel(BoredomLevel) end

---@public
---@param arg0 BodyPartType
---@param arg1 BodyPartType
---@return boolean
function BodyDamage:doBodyPartsHaveInjuries(arg0, arg1) end

---@public
---@param arg0 int
---@return float
function BodyDamage:getSicknessFromCorpsesRate(arg0) end

---@public
---@return int @the CurrentNumZombiesVisible
function BodyDamage:getCurrentNumZombiesVisible() end

---@public
---@return float @the CatchACold
function BodyDamage:getCatchACold() end

---@public
---@param BodyPartIndex int
---@param Scratched boolean
---@return void
---@overload fun(BodyPart:BodyPartType, Scratched:boolean)
function BodyDamage:SetScratched(BodyPartIndex, Scratched) end

---@public
---@param BodyPart BodyPartType
---@param Scratched boolean
---@return void
function BodyDamage:SetScratched(BodyPart, Scratched) end

---@public
---@param arg0 float
---@return void
function BodyDamage:setInfectionTime(arg0) end

---@public
---@return int @the NastyColdSneezeTimerMax
function BodyDamage:getNastyColdSneezeTimerMax() end

---@public
---@param HasACold boolean @the HasACold to set
---@return void
function BodyDamage:setHasACold(HasACold) end

---@public
---@param SneezeCoughActive int @the SneezeCoughActive to set
---@return void
function BodyDamage:setSneezeCoughActive(SneezeCoughActive) end

---@public
---@param type BodyPartType
---@return BodyPart
function BodyDamage:getBodyPart(type) end

---@public
---@param InfectionLevel float @the InfectionLevel to set
---@return void
function BodyDamage:setInfectionLevel(InfectionLevel) end

---@public
---@return IsoGameCharacter @the ParentChar
function BodyDamage:getParentChar() end

---@public
---@return void
function BodyDamage:UpdateCold() end

---@public
---@param InitialWoundPain float @the InitialWoundPain to set
---@return void
function BodyDamage:setInitialWoundPain(InitialWoundPain) end

---@public
---@return boolean
---@overload fun(BodyPartIndex:int)
function BodyDamage:IsFakeInfected() end

---@public
---@param BodyPartIndex int
---@return boolean
function BodyDamage:IsFakeInfected(BodyPartIndex) end

---@public
---@return float
function BodyDamage:getUnhappynessLevel() end

---@public
---@param NastyColdSneezeTimerMin int @the NastyColdSneezeTimerMin to set
---@return void
function BodyDamage:setNastyColdSneezeTimerMin(NastyColdSneezeTimerMin) end

---@public
---@param NastyColdSneezeTimerMax int @the NastyColdSneezeTimerMax to set
---@return void
function BodyDamage:setNastyColdSneezeTimerMax(NastyColdSneezeTimerMax) end

---@public
---@param HealthFromFood float @the HealthFromFood to set
---@return void
function BodyDamage:setHealthFromFood(HealthFromFood) end

---@public
---@param BodyPart BodyPartType
---@return boolean
---@overload fun(BodyPartIndex:int)
function BodyDamage:IsBitten(BodyPart) end

---@public
---@param BodyPartIndex int
---@return boolean
function BodyDamage:IsBitten(BodyPartIndex) end

---@public
---@param Pill InventoryItem
---@return void
function BodyDamage:JustTookPill(Pill) end

---@public
---@return float @the StandardHealthAddition
function BodyDamage:getStandardHealthAddition() end

---@public
---@return float @the InfectionGrowthRate
function BodyDamage:getInfectionGrowthRate() end

---@public
---@return int @the ColdSneezeTimerMax
function BodyDamage:getColdSneezeTimerMax() end

---@public
---@param weapon HandWeapon
---@return void
function BodyDamage:DamageFromWeapon(weapon) end

---@public
---@param SeverlyReducedHealthAddition float @the SeverlyReducedHealthAddition to set
---@return void
function BodyDamage:setSeverlyReducedHealthAddition(SeverlyReducedHealthAddition) end

---@public
---@param BodyPart BodyPartType
---@param Cortorised boolean
---@return void
---@overload fun(BodyPartIndex:int, Cortorised:boolean)
function BodyDamage:SetCortorised(BodyPart, Cortorised) end

---@public
---@param BodyPartIndex int
---@param Cortorised boolean
---@return void
function BodyDamage:SetCortorised(BodyPartIndex, Cortorised) end

---@public
---@param arg0 float
---@return void
function BodyDamage:increaseBodyWetness(arg0) end

---@public
---@return float @the PanicIncreaseValue
function BodyDamage:getPanicIncreaseValue() end

---@public
---@param ColdProgressionRate float @the ColdProgressionRate to set
---@return void
function BodyDamage:setColdProgressionRate(ColdProgressionRate) end

---@public
---@return int @the MildColdSneezeTimerMax
function BodyDamage:getMildColdSneezeTimerMax() end

---@public
---@return int @the StandardHealthFromFoodTime
function BodyDamage:getStandardHealthFromFoodTime() end

---@public
---@return float
function BodyDamage:getInfectionMortalityDuration() end

---@public
---@return float @the BoredomDecreaseFromReading
function BodyDamage:getBoredomDecreaseFromReading() end

---@public
---@param BodyPart BodyPartType
---@param Val float
---@return void
---@overload fun(BodyPartIndex:int, val:float)
function BodyDamage:AddDamage(BodyPart, Val) end

---@public
---@param BodyPartIndex int
---@param val float
---@return void
function BodyDamage:AddDamage(BodyPartIndex, val) end

---@public
---@param BodyPart BodyPartType
---@return float
---@overload fun(BodyPartIndex:int)
function BodyDamage:getBodyPartHealth(BodyPart) end

---@public
---@param BodyPartIndex int
---@return float
function BodyDamage:getBodyPartHealth(BodyPartIndex) end

---@public
---@return boolean
function BodyDamage:isInfected() end

---@public
---@param DrunkIncreaseValue float @the DrunkIncreaseValue to set
---@return void
function BodyDamage:setDrunkIncreaseValue(DrunkIncreaseValue) end

---@public
---@return float @the ContinualPainIncrease
function BodyDamage:getContinualPainIncrease() end

---@public
---@return int
function BodyDamage:getNumPartsBleeding() end

---@public
---@param HealthReductionFromSevereBadMoodles float @the HealthReductionFromSevereBadMoodles to set
---@return void
function BodyDamage:setHealthReductionFromSevereBadMoodles(HealthReductionFromSevereBadMoodles) end

---@public
---@param arg0 float
---@return void
function BodyDamage:setColdDamageStage(arg0) end

---@public
---@param foodSicknessLevel float
---@return void
function BodyDamage:setFoodSicknessLevel(foodSicknessLevel) end

---@public
---@return boolean
function BodyDamage:WasBurntToDeath() end

---@public
---@return float
function BodyDamage:getPoisonLevel() end

---@public
---@return float @the HealthFromFood
function BodyDamage:getHealthFromFood() end

---@public
---@param arg0 float
---@return void
function BodyDamage:setColdReduction(arg0) end

---@public
---@param BodyPart BodyPartType
---@return boolean
---@overload fun(BodyPartIndex:int)
function BodyDamage:IsWounded(BodyPart) end

---@public
---@param BodyPartIndex int
---@return boolean
function BodyDamage:IsWounded(BodyPartIndex) end

---@private
---@return float
function BodyDamage:getDamageFromPills() end

---@public
---@return float @the InitialWoundPain
function BodyDamage:getInitialWoundPain() end

---@public
---@return void
function BodyDamage:ReducePanic() end

---@public
---@return BodyPart
function BodyDamage:setScratchedWindow() end

---@public
---@return boolean @the IsFakeInfected
function BodyDamage:isIsFakeInfected() end

---@public
---@param NewFood Food
---@return void
---@overload fun(NewFood:Food, percentage:float)
function BodyDamage:JustAteFood(NewFood) end

---@public
---@param NewFood Food
---@param percentage float
---@return void
function BodyDamage:JustAteFood(NewFood, percentage) end

---@public
---@param arg0 float
---@return void
function BodyDamage:setPainReduction(arg0) end

---@public
---@param arg0 BodyPartType
---@return boolean
function BodyDamage:isBodyPartBleeding(arg0) end

---@public
---@return int @the TimeToSneezeOrCough
function BodyDamage:getTimeToSneezeOrCough() end

---@public
---@param CatchACold float @the CatchACold to set
---@return void
function BodyDamage:setCatchACold(CatchACold) end

---@public
---@param arg0 BodyPartType
---@return BodyPartLast
function BodyDamage:getBodyPartsLastState(arg0) end

---@public
---@return float
function BodyDamage:getPainReduction() end

---@public
---@return void
function BodyDamage:JustTookPainMeds() end

---@public
---@param StandardHealthFromFoodTime int @the StandardHealthFromFoodTime to set
---@return void
function BodyDamage:setStandardHealthFromFoodTime(StandardHealthFromFoodTime) end

---throws java.io.IOException
---@public
---@param output ByteBuffer
---@return void
function BodyDamage:save(output) end

---@public
---@return boolean
function BodyDamage:isReduceFakeInfection() end

---@public
---@param BodyPart BodyPartType
---@param Bleeding boolean
---@return void
---@overload fun(BodyPartIndex:int, Bleeding:boolean)
function BodyDamage:SetBleeding(BodyPart, Bleeding) end

---@public
---@param BodyPartIndex int
---@param Bleeding boolean
---@return void
function BodyDamage:SetBleeding(BodyPartIndex, Bleeding) end

---@public
---@return float @the HealthFromFoodTimer
function BodyDamage:getHealthFromFoodTimer() end

---@public
---@return int @the SneezeCoughDelay
function BodyDamage:getSneezeCoughDelay() end

---@public
---@param PainReductionFromMeds float @the PainReductionFromMeds to set
---@return void
function BodyDamage:setPainReductionFromMeds(PainReductionFromMeds) end

---@public
---@return boolean
---@overload fun(BodyPart:BodyPartType)
---@overload fun(BodyPartIndex:int)
function BodyDamage:IsInfected() end

---@public
---@param BodyPart BodyPartType
---@return boolean
function BodyDamage:IsInfected(BodyPart) end

---@public
---@param BodyPartIndex int
---@return boolean
function BodyDamage:IsInfected(BodyPartIndex) end

---@public
---@param t float
---@return void
function BodyDamage:setTemperature(t) end

---@public
---@param Wetness float @the Wetness to set
---@return void
function BodyDamage:setWetness(Wetness) end

---@public
---@return float @the FakeInfectionLevel
function BodyDamage:getFakeInfectionLevel() end

---@public
---@param BodyPartIndex int
---@return boolean
---@overload fun(BodyPart:BodyPartType)
function BodyDamage:IsBleedingStemmed(BodyPartIndex) end

---@public
---@param BodyPart BodyPartType
---@return boolean
function BodyDamage:IsBleedingStemmed(BodyPart) end

---@public
---@return boolean
function BodyDamage:HasInjury() end

---@public
---@param arg0 BodyPartType
---@return boolean
function BodyDamage:IsCut(arg0) end

---@public
---@return float
function BodyDamage:getBoredomLevel() end

---@public
---@return void
function BodyDamage:splatBloodFloorBig() end

---@public
---@param StandardPainReductionWhenWell float @the StandardPainReductionWhenWell to set
---@return void
function BodyDamage:setStandardPainReductionWhenWell(StandardPainReductionWhenWell) end

---throws java.io.IOException
---@public
---@param input ByteBuffer
---@param WorldVersion int
---@return void
function BodyDamage:load(input, WorldVersion) end

---@public
---@return float @the HealthReductionFromSevereBadMoodles
function BodyDamage:getHealthReductionFromSevereBadMoodles() end

---@public
---@return int @the SneezeCoughActive
function BodyDamage:getSneezeCoughActive() end

---@public
---@return void
function BodyDamage:RestoreToFullHealth() end

---@public
---@return void
function BodyDamage:UpdateBoredom() end

---@public
---@param PanicReductionValue float @the PanicReductionValue to set
---@return void
function BodyDamage:setPanicReductionValue(PanicReductionValue) end

---@public
---@param BodyPart BodyPartType
---@param Bitten boolean
---@return void
---@overload fun(BodyPartIndex:int, Bitten:boolean)
---@overload fun(BodyPartIndex:int, Bitten:boolean, Infected:boolean)
function BodyDamage:SetBitten(BodyPart, Bitten) end

---@public
---@param BodyPartIndex int
---@param Bitten boolean
---@return void
function BodyDamage:SetBitten(BodyPartIndex, Bitten) end

---@public
---@param BodyPartIndex int
---@param Bitten boolean
---@param Infected boolean
---@return void
function BodyDamage:SetBitten(BodyPartIndex, Bitten, Infected) end

---@public
---@param BodyPartIndex int
---@return String
---@overload fun(BodyPart:BodyPartType)
function BodyDamage:getBodyPartName(BodyPartIndex) end

---@public
---@param BodyPart BodyPartType
---@return String
function BodyDamage:getBodyPartName(BodyPart) end

---@public
---@param MildColdSneezeTimerMin int @the MildColdSneezeTimerMin to set
---@return void
function BodyDamage:setMildColdSneezeTimerMin(MildColdSneezeTimerMin) end

---@public
---@return int @the SneezeCoughTime
function BodyDamage:getSneezeCoughTime() end

---@public
---@param arg0 boolean
---@return void
function BodyDamage:setInfected(arg0) end

---@public
---@param arg0 float
---@return void
function BodyDamage:IncreasePanicFloat(arg0) end

---@public
---@param X int
---@param Y int
---@param Width int
---@param Height int
---@param r float
---@param g float
---@param b float
---@param a float
---@return void
function BodyDamage:DrawUntexturedQuad(X, Y, Width, Height, r, g, b, a) end

---@public
---@param BurntToDeath boolean @the BurntToDeath to set
---@return void
function BodyDamage:setBurntToDeath(BurntToDeath) end

---@public
---@param ContinualPainIncrease float @the ContinualPainIncrease to set
---@return void
function BodyDamage:setContinualPainIncrease(ContinualPainIncrease) end

---@public
---@return int
function BodyDamage:getNumPartsScratched() end

---@public
---@param BodyPart BodyPartType
---@return boolean
---@overload fun(BodyPartIndex:int)
function BodyDamage:IsStitched(BodyPart) end

---@public
---@param BodyPartIndex int
---@return boolean
function BodyDamage:IsStitched(BodyPartIndex) end

---@public
---@param arg0 int
---@param arg1 boolean
---@return void
function BodyDamage:SetCut(arg0, arg1) end

---@public
---@param OnFire boolean
---@return void
function BodyDamage:OnFire(OnFire) end

---@private
---@return void
function BodyDamage:UpdateTemperatureState() end

---@public
---@return float
function BodyDamage:getInfectionLevel() end

---@public
---@param BodyPartIndex int
---@return boolean
---@overload fun(BodyPart:BodyPartType)
function BodyDamage:IsBleeding(BodyPartIndex) end

---@public
---@param BodyPart BodyPartType
---@return boolean
function BodyDamage:IsBleeding(BodyPart) end

---@public
---@param arg0 int
---@return void
function BodyDamage:setRemotePainLevel(arg0) end

---@public
---@return float @the SleepingHealthAddition
function BodyDamage:getSleepingHealthAddition() end

---@public
---@return void
function BodyDamage:ShowDebugInfo() end

---@public
---@param poisonLevel float
---@return void
function BodyDamage:setPoisonLevel(poisonLevel) end

---@private
---@return float
function BodyDamage:getHealthFromFoodTimeByHunger() end

---@public
---@return boolean
function BodyDamage:IsOnFire() end

---@public
---@param DamageModCount int @the DamageModCount to set
---@return void
function BodyDamage:setDamageModCount(DamageModCount) end

---@public
---@return float
function BodyDamage:getColdStrength() end

---@public
---@param BodyPartIndex int
---@return boolean
---@overload fun(BodyPart:BodyPartType)
function BodyDamage:IsCortorised(BodyPartIndex) end

---@public
---@param BodyPart BodyPartType
---@return boolean
function BodyDamage:IsCortorised(BodyPart) end

---@public
---@param InitialScratchPain float @the InitialScratchPain to set
---@return void
function BodyDamage:setInitialScratchPain(InitialScratchPain) end

---@public
---@return float @the InitialScratchPain
function BodyDamage:getInitialScratchPain() end

---@public
---@param SneezeCoughDelay int @the SneezeCoughDelay to set
---@return void
function BodyDamage:setSneezeCoughDelay(SneezeCoughDelay) end

---@public
---@return boolean @the HasACold
function BodyDamage:isHasACold() end

---@public
---@return void
function BodyDamage:UpdateStrength() end

---@public
---@param SneezeCoughTime int @the SneezeCoughTime to set
---@return void
function BodyDamage:setSneezeCoughTime(SneezeCoughTime) end

---@public
---@return float @the OverallBodyHealth
function BodyDamage:getOverallBodyHealth() end

---@public
---@return ArrayList|BodyPart @the BodyParts
function BodyDamage:getBodyParts() end

---@public
---@return float @the StandardPainReductionWhenWell
function BodyDamage:getStandardPainReductionWhenWell() end

---@public
---@return int @the DamageModCount
function BodyDamage:getDamageModCount() end

---@public
---@return float
function BodyDamage:getInfectionTime() end

---@public
---@param BodyPart BodyPartType
---@return boolean
---@overload fun(BodyPartIndex:int)
function BodyDamage:IsBandaged(BodyPart) end

---@public
---@param BodyPartIndex int
---@return boolean
function BodyDamage:IsBandaged(BodyPartIndex) end

---@public
---@return boolean @the inf
function BodyDamage:isInf() end

---@public
---@return float
function BodyDamage:getFoodSicknessLevel() end

---@public
---@param IsFakeInfected boolean @the IsFakeInfected to set
---@return void
function BodyDamage:setIsFakeInfected(IsFakeInfected) end

---@public
---@return float @the body temperature (updated by lua)
function BodyDamage:getTemperature() end

---@public
---@return int
function BodyDamage:IsSneezingCoughing() end

---@public
---@return int @the ColdSneezeTimerMin
function BodyDamage:getColdSneezeTimerMin() end

---@public
---@param UnhappynessLevel float @the UnhappynessLevel to set
---@return void
function BodyDamage:setUnhappynessLevel(UnhappynessLevel) end

---@public
---@return float
function BodyDamage:getApparentInfectionLevel() end

---@public
---@param InfectionGrowthRate float @the InfectionGrowthRate to set
---@return void
function BodyDamage:setInfectionGrowthRate(InfectionGrowthRate) end

---@public
---@param inf boolean @the inf to set
---@return void
function BodyDamage:setInf(inf) end

---@public
---@return float @the DrunkReductionValue
function BodyDamage:getDrunkReductionValue() end

---@public
---@param HealthFromFoodTimer float @the HealthFromFoodTimer to set
---@return void
function BodyDamage:setHealthFromFoodTimer(HealthFromFoodTimer) end

---@public
---@param lit Literature
---@return void
function BodyDamage:JustReadSomething(lit) end

---@public
---@param reduceFakeInfection boolean
---@return void
function BodyDamage:setReduceFakeInfection(reduceFakeInfection) end

---@public
---@param DrunkReductionValue float @the DrunkReductionValue to set
---@return void
function BodyDamage:setDrunkReductionValue(DrunkReductionValue) end

---@public
---@return void
function BodyDamage:setBodyPartsLastState() end

---@private
---@return float
function BodyDamage:getCurrentTimeForInfection() end

---@public
---@param BodyPart BodyPartType
---@return boolean
function BodyDamage:IsDeepWounded(BodyPart) end

---@public
---@return void
function BodyDamage:UpdateWetness() end

---@public
---@param BodyPartIndex int
---@param BleedingStemmed boolean
---@return void
---@overload fun(BodyPart:BodyPartType, BleedingStemmed:boolean)
function BodyDamage:SetBleedingStemmed(BodyPartIndex, BleedingStemmed) end

---@public
---@param BodyPart BodyPartType
---@param BleedingStemmed boolean
---@return void
function BodyDamage:SetBleedingStemmed(BodyPart, BleedingStemmed) end

---@public
---@param Val float
---@return void
function BodyDamage:AddGeneralHealth(Val) end

---@public
---@param FakeInfectionLevel float @the FakeInfectionLevel to set
---@return void
function BodyDamage:setFakeInfectionLevel(FakeInfectionLevel) end

---@public
---@param arg0 float
---@return void
function BodyDamage:setInfectionMortalityDuration(arg0) end

---@public
---@return boolean @the BurntToDeath
function BodyDamage:isBurntToDeath() end

---@public
---@param BodyPartIndex int
---@param Bandaged boolean
---@param bandageLife float
---@param isAlcoholic boolean
---@param bandageType String
---@return void
function BodyDamage:SetBandaged(BodyPartIndex, Bandaged, bandageLife, isAlcoholic, bandageType) end

---@public
---@return Thermoregulator
function BodyDamage:getThermoregulator() end

---@public
---@return float
function BodyDamage:getColdReduction() end

---@public
---@return void
function BodyDamage:AddRandomDamage() end

---@public
---@param ParentChar IsoGameCharacter @the ParentChar to set
---@return void
function BodyDamage:setParentChar(ParentChar) end

---@public
---@param CurrentNumZombiesVisible int @the CurrentNumZombiesVisible to set
---@return void
function BodyDamage:setCurrentNumZombiesVisible(CurrentNumZombiesVisible) end

---@public
---@return int @the MildColdSneezeTimerMin
function BodyDamage:getMildColdSneezeTimerMin() end

---@public
---@param BodyPartIndex int
---@return void
function BodyDamage:DisableFakeInfection(BodyPartIndex) end

---@public
---@return float @the SeverlyReducedHealthAddition
function BodyDamage:getSeverlyReducedHealthAddition() end

---@public
---@return float @the InitialBitePain
function BodyDamage:getInitialBitePain() end

---@public
---@param arg0 BodyPartType
---@param arg1 BodyPartType
---@return boolean
function BodyDamage:areBodyPartsBleeding(arg0, arg1) end

---@public
---@param arg0 float
---@return void
function BodyDamage:decreaseBodyWetness(arg0) end

---@public
---@return float @the ReducedHealthAddition
function BodyDamage:getReducedHealthAddition() end

---@public
---@param ReducedHealthAddition float @the ReducedHealthAddition to set
---@return void
function BodyDamage:setReducedHealthAddition(ReducedHealthAddition) end

---@public
---@return int
function BodyDamage:getRemotePainLevel() end

---@public
---@return float @the PanicReductionValue
function BodyDamage:getPanicReductionValue() end

---@public
---@param TimeToSneezeOrCough int @the TimeToSneezeOrCough to set
---@return void
function BodyDamage:setTimeToSneezeOrCough(TimeToSneezeOrCough) end

---@public
---@param SleepingHealthAddition float @the SleepingHealthAddition to set
---@return void
function BodyDamage:setSleepingHealthAddition(SleepingHealthAddition) end

---@public
---@return float
function BodyDamage:getWetness() end

---@public
---@param arg0 IsoZombie
---@param arg1 String
---@return boolean
function BodyDamage:AddRandomDamageFromZombie(arg0, arg1) end

---@public
---@param BoredomDecreaseFromReading float @the BoredomDecreaseFromReading to set
---@return void
function BodyDamage:setBoredomDecreaseFromReading(BoredomDecreaseFromReading) end

---@public
---@return float @the ColdProgressionRate
function BodyDamage:getColdProgressionRate() end

---@public
---@param StandardHealthAddition float @the StandardHealthAddition to set
---@return void
function BodyDamage:setStandardHealthAddition(StandardHealthAddition) end

---@public
---@param arg0 BodyPartType
---@return boolean
function BodyDamage:doesBodyPartHaveInjury(arg0) end

---@public
---@return int @the NastyColdSneezeTimerMin
function BodyDamage:getNastyColdSneezeTimerMin() end

---@public
---@param Val float
---@return void
function BodyDamage:ReduceGeneralHealth(Val) end

---@public
---@return float
function BodyDamage:getColdDamageStage() end

---@public
---@return float
function BodyDamage:getTemperatureChangeTick() end

---@public
---@return int @the OldNumZombiesVisible
function BodyDamage:getOldNumZombiesVisible() end

---@public
---@return boolean
function BodyDamage:UseBandageOnMostNeededPart() end

---@public
---@param OldNumZombiesVisible int @the OldNumZombiesVisible to set
---@return void
function BodyDamage:setOldNumZombiesVisible(OldNumZombiesVisible) end

---@public
---@return float
function BodyDamage:getPanicIncreaseValueFrame() end

---@public
---@param InitialThumpPain float @the InitialThumpPain to set
---@return void
function BodyDamage:setInitialThumpPain(InitialThumpPain) end

---@public
---@return void
function BodyDamage:UpdatePanicState() end

---@public
---@return float @the PainReductionFromMeds
function BodyDamage:getPainReductionFromMeds() end

---@private
---@return void
function BodyDamage:UpdateIllness() end

---@public
---@param IsOnFire boolean @the IsOnFire to set
---@return void
function BodyDamage:setIsOnFire(IsOnFire) end

---@public
---@param PanicIncreaseValue float @the PanicIncreaseValue to set
---@return void
function BodyDamage:setPanicIncreaseValue(PanicIncreaseValue) end

---@public
---@param NumNewZombiesSeen int
---@return void
function BodyDamage:IncreasePanic(NumNewZombiesSeen) end

---@public
---@param ColdStrength float @the ColdStrength to set
---@return void
function BodyDamage:setColdStrength(ColdStrength) end

---@public
---@return int
function BodyDamage:getNumPartsBitten() end

---@public
---@return boolean @the IsOnFire
function BodyDamage:isIsOnFire() end

---@public
---@return float
function BodyDamage:pickMortalityDuration() end

---@private
---@return void
function BodyDamage:calculateOverallHealth() end

---@public
---@return float @the InitialThumpPain
function BodyDamage:getInitialThumpPain() end

---@public
---@param BodyPartIndex int
---@param Scratched boolean
---@return void
function BodyDamage:SetScratchedFromWeapon(BodyPartIndex, Scratched) end

---@public
---@param food Food
---@param percentage float
---@return void
function BodyDamage:JustDrankBooze(food, percentage) end

---@public
---@param OverallBodyHealth float @the OverallBodyHealth to set
---@return void
function BodyDamage:setOverallBodyHealth(OverallBodyHealth) end

---@public
---@param ColdSneezeTimerMin int @the ColdSneezeTimerMin to set
---@return void
function BodyDamage:setColdSneezeTimerMin(ColdSneezeTimerMin) end

---@public
---@param InitialBitePain float @the InitialBitePain to set
---@return void
function BodyDamage:setInitialBitePain(InitialBitePain) end

---@public
---@return void
function BodyDamage:Update() end

---@public
---@return void
function BodyDamage:TriggerSneezeCough() end
