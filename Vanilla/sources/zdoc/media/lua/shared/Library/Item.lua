---@class Item : zombie.scripting.objects.Item
---@field public clothingExtraSubmenu String
---@field public DisplayName String
---@field public Hidden boolean
---@field public CantEat boolean
---@field public Icon String
---@field public Medical boolean
---@field public CannedFood boolean
---@field public SurvivalGear boolean
---@field public MechanicsItem boolean
---@field public UseWorldItem boolean
---@field public ScaleWorldIcon float
---@field public CloseKillMove String
---@field public WeaponLength float
---@field public ActualWeight float
---@field public WeightWet float
---@field public WeightEmpty float
---@field public HungerChange float
---@field public CriticalChance float
---@field public Count int
---@field public DaysFresh int
---@field public DaysTotallyRotten int
---@field public MinutesToCook int
---@field public MinutesToBurn int
---@field public IsCookable boolean
---@field private CookingSound String
---@field public StressChange float
---@field public BoredomChange float
---@field public UnhappyChange float
---@field public AlwaysWelcomeGift boolean
---@field public ReplaceOnDeplete String
---@field public Ranged boolean
---@field public CanStoreWater boolean
---@field public MaxRange float
---@field public MinRange float
---@field public ThirstChange float
---@field public FatigueChange float
---@field public MinAngle float
---@field public RequiresEquippedBothHands boolean
---@field public MaxDamage float
---@field public MinDamage float
---@field public MinimumSwingTime float
---@field public SwingSound String
---@field public WeaponSprite String
---@field public AngleFalloff boolean
---@field public SoundVolume int
---@field public ToHitModifier float
---@field public SoundRadius int
---@field public OtherCharacterVolumeBoost float
---@field public Categories ArrayList|String
---@field public Tags ArrayList|Unknown
---@field public ImpactSound String
---@field public SwingTime float
---@field public KnockBackOnNoDeath boolean
---@field public SplatBloodOnNoDeath boolean
---@field public SwingAmountBeforeImpact float
---@field public AmmoType String
---@field public maxAmmo int
---@field public GunType String
---@field public DoorDamage int
---@field public ConditionLowerChance int
---@field public ConditionMax int
---@field public CanBandage boolean
---@field public name String
---@field public moduleDotType String
---@field public MaxHitCount int
---@field public UseSelf boolean
---@field public OtherHandUse boolean
---@field public OtherHandRequire String
---@field public PhysicsObject String
---@field public SwingAnim String
---@field public WeaponWeight float
---@field public EnduranceChange float
---@field public IdleAnim String
---@field public RunAnim String
---@field public attachmentType String
---@field public makeUpType String
---@field public consolidateOption String
---@field public RequireInHandOrInventory ArrayList|Unknown
---@field public DoorHitSound String
---@field public ReplaceOnUse String
---@field public DangerousUncooked boolean
---@field public Alcoholic boolean
---@field public PushBackMod float
---@field public SplatNumber int
---@field public NPCSoundBoost float
---@field public RangeFalloff boolean
---@field public UseEndurance boolean
---@field public MultipleHitConditionAffected boolean
---@field public ShareDamage boolean
---@field public ShareEndurance boolean
---@field public CanBarricade boolean
---@field public UseWhileEquipped boolean
---@field public UseWhileUnequipped boolean
---@field public TicksPerEquipUse int
---@field public DisappearOnUse boolean
---@field public UseDelta float
---@field public AlwaysKnockdown boolean
---@field public EnduranceMod float
---@field public KnockdownMod float
---@field public CantAttackWithLowestEndurance boolean
---@field public ReplaceOnUseOn String
---@field public IsWaterSource boolean
---@field public attachmentsProvided ArrayList|Unknown
---@field public FoodType String
---@field public Poison boolean
---@field public PoisonDetectionLevel Integer
---@field public PoisonPower int
---@field public DefaultModData KahluaTable
---@field public IsAimedFirearm boolean
---@field public IsAimedHandWeapon boolean
---@field public CanStack boolean
---@field public AimingMod float
---@field public ProjectileCount int
---@field public HitAngleMod float
---@field public SplatSize float
---@field public Temperature float
---@field public NumberOfPages int
---@field public LvlSkillTrained int
---@field public NumLevelsTrained int
---@field public SkillTrained String
---@field public Capacity int
---@field public WeightReduction int
---@field public SubCategory String
---@field public ActivatedItem boolean
---@field public LightStrength float
---@field public TorchCone boolean
---@field public LightDistance int
---@field public CanBeEquipped String
---@field public TwoHandWeapon boolean
---@field public CustomContextMenu String
---@field public Tooltip String
---@field public ReplaceOnCooked List|String
---@field public DisplayCategory String
---@field public Trap Boolean
---@field public OBSOLETE boolean
---@field public FishingLure boolean
---@field public canBeWrite boolean
---@field public AimingPerkCritModifier int
---@field public AimingPerkRangeModifier float
---@field public AimingPerkHitChanceModifier float
---@field public HitChance int
---@field public AimingPerkMinAngleModifier float
---@field public RecoilDelay int
---@field public PiercingBullets boolean
---@field public SoundGain float
---@field public ProtectFromRainWhenEquipped boolean
---@field private maxRangeModifier float
---@field private minRangeRangedModifier float
---@field private damageModifier float
---@field private recoilDelayModifier float
---@field private clipSizeModifier int
---@field private mountOn ArrayList|Unknown
---@field private partType String
---@field private ClipSize int
---@field private reloadTime int
---@field private reloadTimeModifier int
---@field private aimingTime int
---@field private aimingTimeModifier int
---@field private hitChanceModifier int
---@field private angleModifier float
---@field private weightModifier float
---@field private PageToWrite int
---@field private RemoveNegativeEffectOnCooked boolean
---@field private treeDamage int
---@field private alcoholPower float
---@field private PutInSound String
---@field private OpenSound String
---@field private CloseSound String
---@field private breakSound String
---@field private customEatSound String
---@field private bulletOutSound String
---@field private ShellFallSound String
---@field private bandagePower float
---@field private ReduceInfectionPower float
---@field private OnCooked String
---@field private OnlyAcceptCategory String
---@field private AcceptItemFunction String
---@field private padlock boolean
---@field private digitalPadlock boolean
---@field private teachedRecipes List|Unknown
---@field private triggerExplosionTimer int
---@field private canBePlaced boolean
---@field private explosionRange int
---@field private explosionPower int
---@field private fireRange int
---@field private firePower int
---@field private smokeRange int
---@field private noiseRange int
---@field private noiseDuration int
---@field private extraDamage float
---@field private explosionTimer int
---@field private PlacedSprite String
---@field private canBeReused boolean
---@field private sensorRange int
---@field private canBeRemote boolean
---@field private remoteController boolean
---@field private remoteRange int
---@field private countDownSound String
---@field private explosionSound String
---@field private fluReduction int
---@field private ReduceFoodSickness int
---@field private painReduction int
---@field private rainFactor float
---@field public torchDot float
---@field public colorRed int
---@field public colorGreen int
---@field public colorBlue int
---@field public twoWay boolean
---@field public transmitRange int
---@field public micRange int
---@field public baseVolumeRange float
---@field public isPortable boolean
---@field public isTelevision boolean
---@field public minChannel int
---@field public maxChannel int
---@field public usesBattery boolean
---@field public isHighTier boolean
---@field public HerbalistType String
---@field private carbohydrates float
---@field private lipids float
---@field private proteins float
---@field private calories float
---@field private packaged boolean
---@field private cantBeFrozen boolean
---@field private evolvedRecipeName String
---@field private ReplaceOnRotten String
---@field private metalValue float
---@field private AlarmSound String
---@field private itemWhenDry String
---@field private wetCooldown float
---@field private isWet boolean
---@field private onEat String
---@field private cantBeConsolided boolean
---@field private BadInMicrowave boolean
---@field private GoodHot boolean
---@field private BadCold boolean
---@field public map String
---@field private keepOnDeplete boolean
---@field public vehicleType int
---@field private maxCapacity int
---@field private itemCapacity int
---@field private ConditionAffectsCapacity boolean
---@field private brakeForce float
---@field private chanceToSpawnDamaged int
---@field private conditionLowerNormal float
---@field private conditionLowerOffroad float
---@field private wheelFriction float
---@field private suspensionDamping float
---@field private suspensionCompression float
---@field private engineLoudness float
---@field public ClothingItem String
---@field private clothingItemAsset ClothingItem
---@field private staticModel String
---@field public primaryAnimMask String
---@field public secondaryAnimMask String
---@field public primaryAnimMaskAttachment String
---@field public secondaryAnimMaskAttachment String
---@field public replaceInSecondHand String
---@field public replaceInPrimaryHand String
---@field public replaceWhenUnequip String
---@field public replacePrimaryHand ItemReplacement
---@field public replaceSecondHand ItemReplacement
---@field public worldObjectSprite String
---@field public ItemName String
---@field public NormalTexture Texture
---@field public SpecialTextures List|Unknown
---@field public SpecialWorldTextureNames List|Unknown
---@field public WorldTextureName String
---@field public WorldTexture Texture
---@field public eatType String
---@field private IconsForTexture ArrayList|Unknown
---@field private baseSpeed float
---@field private bloodClothingType ArrayList|Unknown
---@field private stompPower float
---@field public runSpeedModifier float
---@field public combatSpeedModifier float
---@field public clothingItemExtra ArrayList|Unknown
---@field public clothingItemExtraOption ArrayList|Unknown
---@field private removeOnBroken Boolean
---@field public canHaveHoles Boolean
---@field private cosmetic boolean
---@field private ammoBox String
---@field public hairDye boolean
---@field private insertAmmoStartSound String
---@field private insertAmmoSound String
---@field private insertAmmoStopSound String
---@field private ejectAmmoStartSound String
---@field private ejectAmmoSound String
---@field private ejectAmmoStopSound String
---@field private rackSound String
---@field private clickSound String
---@field private equipSound String
---@field private unequipSound String
---@field private bringToBearSound String
---@field private magazineType String
---@field private weaponReloadType String
---@field private rackAfterShoot boolean
---@field private jamGunChance float
---@field private modelWeaponPart ArrayList|Unknown
---@field private haveChamber boolean
---@field private manuallyRemoveSpentRounds boolean
---@field private biteDefense float
---@field private scratchDefense float
---@field private bulletDefense float
---@field private damageCategory String
---@field private damageMakeHole boolean
---@field public neckProtectionModifier float
---@field private attachmentReplacement String
---@field private insertAllBulletsReload boolean
---@field private chanceToFall int
---@field public fabricType String
---@field public equippedNoSprint boolean
---@field public worldStaticModel String
---@field private critDmgMultiplier float
---@field private insulation float
---@field private windresist float
---@field private waterresist float
---@field private fireMode String
---@field private fireModePossibilities ArrayList|Unknown
---@field public RemoveUnhappinessWhenCooked boolean
---@field private registry_id short
---@field private existsAsVanilla boolean
---@field private modID String
---@field private fileAbsPath String
---@field public stopPower float
---@field private recordedMediaCat String
---@field private acceptMediaType byte
---@field private noTransmit boolean
---@field private worldRender boolean
---@field public HitSound String
---@field public hitFloorSound String
---@field public BodyLocation String
---@field public PaletteChoices Stack|String
---@field public SpriteName String
---@field public PalettesStart String
---@field public NetIDToItem HashMap|Integer|String
---@field public NetItemToID HashMap|String|Integer
---@field IDMax int
---@field public type Item.Type
---@field private Spice boolean
---@field private UseForPoison int
Item = {}

---@public
---@param DangerousUncooked boolean @the DangerousUncooked to set
---@return void
function Item:setDangerousUncooked(DangerousUncooked) end

---@public
---@return float @the EnduranceMod
function Item:getEnduranceMod() end

---@public
---@return String
function Item:getUnequipSound() end

---@public
---@return int
function Item:getChanceToFall() end

---@public
---@return int @the ConditionLowerChance
function Item:getConditionLowerChance() end

---@public
---@return int
function Item:getNumLevelsTrained() end

---@public
---@return String @the SwingSound
function Item:getSwingSound() end

---@public
---@return float @the WeaponWeight
function Item:getWeaponWeight() end

---@public
---@return boolean @the UseEndurance
function Item:isUseEndurance() end

---@public
---@return boolean @the ShareEndurance
function Item:isShareEndurance() end

---@public
---@param MinutesToCook int @the MinutesToCook to set
---@return void
function Item:setMinutesToCook(MinutesToCook) end

---@public
---@return String @the SwingAnim
function Item:getSwingAnim() end

---@public
---@param ImpactSound String @the ImpactSound to set
---@return void
function Item:setImpactSound(ImpactSound) end

---@public
---@return float @the NPCSoundBoost
function Item:getNPCSoundBoost() end

---@public
---@param EnduranceMod float @the EnduranceMod to set
---@return void
function Item:setEnduranceMod(EnduranceMod) end

---@public
---@param Categories ArrayList|String @the Categories to set
---@return void
function Item:setCategories(Categories) end

---@public
---@return boolean @the CantAttackWithLowestEndurance
function Item:isCantAttackWithLowestEndurance() end

---@public
---@return boolean @the UseSelf
function Item:isUseSelf() end

---@public
---@param DaysFresh int @the DaysFresh to set
---@return void
function Item:setDaysFresh(DaysFresh) end

---@public
---@param DaysTotallyRotten int @the DaysTotallyRotten to set
---@return void
function Item:setDaysTotallyRotten(DaysTotallyRotten) end

---@public
---@return int @the SplatNumber
function Item:getSplatNumber() end

---@public
---@param arg0 float
---@return void
function Item:setWaterresist(arg0) end

---@public
---@return float @the StressChange
function Item:getStressChange() end

---@public
---@param arg0 short
---@return void
function Item:setRegistry_id(arg0) end

---@public
---@return String
function Item:getEquipSound() end

---@public
---@return boolean @the Alcoholic
function Item:isAlcoholic() end

---@public
---@return boolean
function Item:getObsolete() end

---@public
---@return int
function Item:getNoiseDuration() end

---@public
---@return String
function Item:toString() end

---@public
---@param RangeFalloff boolean @the RangeFalloff to set
---@return void
function Item:setRangeFalloff(RangeFalloff) end

---@public
---@return boolean
function Item:isCosmetic() end

---@public
---@param name String @the name to set
---@return void
function Item:setName(name) end

---@public
---@param DoorDamage int @the DoorDamage to set
---@return void
function Item:setDoorDamage(DoorDamage) end

---@public
---@return String @the PalettesStart
function Item:getPalettesStart() end

---@public
---@param ConditionLowerChance int @the ConditionLowerChance to set
---@return void
function Item:setConditionLowerChance(ConditionLowerChance) end

---@public
---@return int @the Count
function Item:getCount() end

---@public
---@return boolean
function Item:getExistsAsVanilla() end

---@public
---@param AmmoType String @the AmmoType to set
---@return void
function Item:setAmmoType(AmmoType) end

---@public
---@param arg0 String
---@return void
function Item:setBodyLocation(arg0) end

---@public
---@return int
function Item:getMaxLevelTrained() end

---@public
---@return String
function Item:getOpenSound() end

---@public
---@param EnduranceChange float @the EnduranceChange to set
---@return void
function Item:setEnduranceChange(EnduranceChange) end

---@public
---@param SwingAmountBeforeImpact float @the SwingAmountBeforeImpact to set
---@return void
function Item:setSwingAmountBeforeImpact(SwingAmountBeforeImpact) end

---@public
---@param KnockdownMod float @the KnockdownMod to set
---@return void
function Item:setKnockdownMod(KnockdownMod) end

---@public
---@return boolean @the IsCookable
function Item:isIsCookable() end

---@public
---@return String
function Item:getBulletOutSound() end

---@public
---@return boolean @the UseWhileEquipped
function Item:isUseWhileEquipped() end

---@public
---@return boolean @the CanBandage
function Item:isCanBandage() end

---@public
---@return float
function Item:getThirstChange() end

---@public
---@return String
function Item:getModuleName() end

---@public
---@return String
function Item:getFullName() end

---@public
---@param CantAttackWithLowestEndurance boolean @the CantAttackWithLowestEndurance to set
---@return void
function Item:setCantAttackWithLowestEndurance(CantAttackWithLowestEndurance) end

---@public
---@param DoorHitSound String @the DoorHitSound to set
---@return void
function Item:setDoorHitSound(DoorHitSound) end

---@public
---@param PalettesStart String @the PalettesStart to set
---@return void
function Item:setPalettesStart(PalettesStart) end

---@public
---@return Stack|String @the PaletteChoices
function Item:getPaletteChoices() end

---@public
---@param arg0 String
---@return void
function Item:setReplaceOnDeplete(arg0) end

---@public
---@return boolean
function Item:isManuallyRemoveSpentRounds() end

---@public
---@param SplatBloodOnNoDeath boolean @the SplatBloodOnNoDeath to set
---@return void
function Item:setSplatBloodOnNoDeath(SplatBloodOnNoDeath) end

---@public
---@return String
function Item:getEatType() end

---@public
---@return int @the MinutesToCook
function Item:getMinutesToCook() end

---@public
---@return String @the ReplaceOnUse
function Item:getReplaceOnUse() end

---@public
---@return float @the MinimumSwingTime
function Item:getMinimumSwingTime() end

---@public
---@return String
function Item:getCookingSound() end

---@public
---@return String @the WeaponSprite
function Item:getWeaponSprite() end

---@public
---@return int @the DoorDamage
function Item:getDoorDamage() end

---@public
---@return String
function Item:getReplaceOnDeplete() end

---@public
---@return boolean @the DisappearOnUse
function Item:isDisappearOnUse() end

---@public
---@return float @the MinDamage
function Item:getMinDamage() end

---@public
---@return String
function Item:getEjectAmmoStartSound() end

---@public
---@return String
function Item:getEjectAmmoStopSound() end

---@public
---@return String
function Item:getSkillTrained() end

---@public
---@param PushBackMod float @the PushBackMod to set
---@return void
function Item:setPushBackMod(PushBackMod) end

---@public
---@param OtherHandUse boolean @the OtherHandUse to set
---@return void
function Item:setOtherHandUse(OtherHandUse) end

---@public
---@return ClothingItem
function Item:getClothingItemAsset() end

---@public
---@return float
function Item:getWindresist() end

---@public
---@return float
function Item:getTicksPerEquipUse() end

---@public
---@param UseDelta float @the UseDelta to set
---@return void
function Item:setUseDelta(UseDelta) end

---@public
---@param BoredomChange float @the BoredomChange to set
---@return void
function Item:setBoredomChange(BoredomChange) end

---@public
---@return float @the UnhappyChange
function Item:getUnhappyChange() end

---@public
---@return int @the SoundRadius
function Item:getSoundRadius() end

---@public
---@param WeaponSprite String @the WeaponSprite to set
---@return void
function Item:setWeaponSprite(WeaponSprite) end

---@public
---@return String @the AmmoType
function Item:getAmmoType() end

---@public
---@return float @the OtherCharacterVolumeBoost
function Item:getOtherCharacterVolumeBoost() end

---@public
---@param PhysicsObject String @the PhysicsObject to set
---@return void
function Item:setPhysicsObject(PhysicsObject) end

---@public
---@return String
function Item:getClothingItem() end

---@public
---@param MinutesToBurn int @the MinutesToBurn to set
---@return void
function Item:setMinutesToBurn(MinutesToBurn) end

---@public
---@return float @the KnockdownMod
function Item:getKnockdownMod() end

---@public
---@param MaxRange float @the MaxRange to set
---@return void
function Item:setMaxRange(MaxRange) end

---@public
---@param AlwaysWelcomeGift boolean @the AlwaysWelcomeGift to set
---@return void
function Item:setAlwaysWelcomeGift(AlwaysWelcomeGift) end

---@public
---@return boolean
function Item:getCanStoreWater() end

---@public
---@return ArrayList|Unknown
function Item:getClothingItemExtraOption() end

---@public
---@return float @the SwingTime
function Item:getSwingTime() end

---@public
---@param MaxDamage float @the MaxDamage to set
---@return void
function Item:setMaxDamage(MaxDamage) end

---@public
---@return String
function Item:getCountDownSound() end

---@public
---@param Ranged boolean @the Ranged to set
---@return void
function Item:setRanged(Ranged) end

---@public
---@return float @the MaxDamage
function Item:getMaxDamage() end

---@public
---@return void
function Item:resolveItemTypes() end

---@public
---@return String
function Item:getPutInSound() end

---@public
---@param ShareDamage boolean @the ShareDamage to set
---@return void
function Item:setShareDamage(ShareDamage) end

---@public
---@param SwingSound String @the SwingSound to set
---@return void
function Item:setSwingSound(SwingSound) end

---@public
---@return float @the ToHitModifier
function Item:getToHitModifier() end

---@public
---@return List|Unknown
function Item:getTeachedRecipes() end

---@public
---@return String @the ImpactSound
function Item:getImpactSound() end

---@public
---@return float
function Item:getRainFactor() end

---@public
---@return ArrayList|String @the Categories
function Item:getCategories() end

---@public
---@return String
function Item:getInsertAmmoSound() end

---@public
---@param Count int @the Count to set
---@return void
function Item:setCount(Count) end

---@public
---@return ArrayList|Unknown
function Item:getIconsForTexture() end

---@public
---@param arg0 boolean
---@return void
function Item:setUseWhileUnequipped(arg0) end

---@public
---@param OtherCharacterVolumeBoost float @the OtherCharacterVolumeBoost to set
---@return void
function Item:setOtherCharacterVolumeBoost(OtherCharacterVolumeBoost) end

---@public
---@return int
function Item:getLevelSkillTrained() end

---@public
---@param UnhappyChange float @the UnhappyChange to set
---@return void
function Item:setUnhappyChange(UnhappyChange) end

---@public
---@param type Item.Type @the type to set
---@return void
function Item:setType(type) end

---@public
---@return boolean @the SplatBloodOnNoDeath
function Item:isSplatBloodOnNoDeath() end

---@public
---@return float
function Item:getWeightWet() end

---@public
---@param Icon String @the Icon to set
---@return void
function Item:setIcon(Icon) end

---@public
---@return Item.Type @the type
function Item:getType() end

---@public
---@return boolean
function Item:isUseWhileUnequipped() end

---@public
---@return String @the DoorHitSound
function Item:getDoorHitSound() end

---@public
---@return boolean @the AlwaysWelcomeGift
function Item:isAlwaysWelcomeGift() end

---@public
---@return String
function Item:getInsertAmmoStopSound() end

---@public
---@param UseSelf boolean @the UseSelf to set
---@return void
function Item:setUseSelf(UseSelf) end

---Overrides:
---
---Load in class BaseScriptObject
---@public
---@param name String
---@param strArray String[]
---@return void
function Item:Load(name, strArray) end

---@public
---@param arg0 float
---@return void
function Item:setInsulation(arg0) end

---@public
---@param temperature float
---@return void
function Item:setTemperature(temperature) end

---@public
---@return String
function Item:getAcceptItemFunction() end

---@public
---@param MaxHitCount int @the MaxHitCount to set
---@return void
function Item:setMaxHitCount(MaxHitCount) end

---@public
---@param arg0 float
---@return void
function Item:setThirstChange(arg0) end

---@public
---@return String @the name
function Item:getName() end

---@public
---@return int @the ConditionMax
function Item:getConditionMax() end

---@public
---@param IsCookable boolean @the IsCookable to set
---@return void
function Item:setIsCookable(IsCookable) end

---@public
---@return String @the DisplayName
function Item:getDisplayName() end

---@public
---@param SplatNumber int @the SplatNumber to set
---@return void
function Item:setSplatNumber(SplatNumber) end

---@public
---@param WeaponWeight float @the WeaponWeight to set
---@return void
function Item:setWeaponWeight(WeaponWeight) end

---@public
---@return Texture
function Item:getNormalTexture() end

---@public
---@return float
function Item:getWaterresist() end

---@public
---@param MinAngle float @the MinAngle to set
---@return void
function Item:setMinAngle(MinAngle) end

---@public
---@param arg0 float
---@return void
function Item:setWindresist(arg0) end

---@public
---@return boolean @the MultipleHitConditionAffected
function Item:isMultipleHitConditionAffected() end

---@public
---@return Boolean
function Item:isWorldRender() end

---@public
---@param arg0 float
---@return void
function Item:setWeightWet(arg0) end

---@public
---@return String
function Item:getRecordedMediaCat() end

---@public
---@return String
function Item:getBodyLocation() end

---@public
---@param ActualWeight float @the ActualWeight to set
---@return void
function Item:setActualWeight(ActualWeight) end

---@public
---@param UseEndurance boolean @the UseEndurance to set
---@return void
function Item:setUseEndurance(UseEndurance) end

---@public
---@return float
function Item:getTemperature() end

---@public
---@return int @the DaysFresh
function Item:getDaysFresh() end

---@public
---@param SpriteName String @the SpriteName to set
---@return void
function Item:setSpriteName(SpriteName) end

---@public
---@return boolean @the ShareDamage
function Item:isShareDamage() end

---@public
---@return boolean @the OtherHandUse
function Item:isOtherHandUse() end

---@public
---@return int @the MinutesToBurn
function Item:getMinutesToBurn() end

---@public
---@return String
function Item:getDisplayCategory() end

---@public
---@param arg0 ClothingItem
---@return void
function Item:setClothingItemAsset(arg0) end

---@public
---@return float @the HungerChange
function Item:getHungerChange() end

---@public
---@return short
function Item:getRegistry_id() end

---@public
---@return boolean @the CanBarricade
function Item:isCanBarricade() end

---@public
---@return String
function Item:getStaticModel() end

---@public
---@return boolean @the AlwaysKnockdown
function Item:isAlwaysKnockdown() end

---@public
---@return boolean @the RangeFalloff
function Item:isRangeFalloff() end

---@public
---@return String
function Item:getModID() end

---@public
---@param arg0 int
---@return void
function Item:setTicksPerEquipUse(arg0) end

---@public
---@return String
function Item:getExplosionSound() end

---@public
---@return boolean
function Item:isConditionAffectsCapacity() end

---@public
---@return boolean @the KnockBackOnNoDeath
function Item:isKnockBackOnNoDeath() end

---@public
---@param MultipleHitConditionAffected boolean @the MultipleHitConditionAffected to set
---@return void
function Item:setMultipleHitConditionAffected(MultipleHitConditionAffected) end

---@public
---@return float @the ActualWeight
function Item:getActualWeight() end

---@public
---@return boolean @the DangerousUncooked
function Item:isDangerousUncooked() end

---@public
---@return String
function Item:getBreakSound() end

---@public
---@param CanBandage boolean @the CanBandage to set
---@return void
function Item:setCanBandage(CanBandage) end

---@public
---@return String
function Item:getReplaceWhenUnequip() end

---@public
---@param DisplayName String @the DisplayName to set
---@return void
function Item:setDisplayName(DisplayName) end

---@public
---@return float
function Item:getInsulation() end

---@public
---@return String
function Item:getBringToBearSound() end

---@public
---@param MinimumSwingTime float @the MinimumSwingTime to set
---@return void
function Item:setMinimumSwingTime(MinimumSwingTime) end

---@public
---@return ArrayList|Unknown
function Item:getTags() end

---@public
---@param param String
---@return InventoryItem
function Item:InstanceItem(param) end

---@public
---@param SwingTime float @the SwingTime to set
---@return void
function Item:setSwingTime(SwingTime) end

---@public
---@return String
function Item:getFabricType() end

---@public
---@return String
function Item:getTypeString() end

---@public
---@return String @the OtherHandRequire
function Item:getOtherHandRequire() end

---@public
---@param Alcoholic boolean @the Alcoholic to set
---@return void
function Item:setAlcoholic(Alcoholic) end

---@public
---@return String
function Item:getFileAbsPath() end

---@public
---@return float @the SwingAmountBeforeImpact
function Item:getSwingAmountBeforeImpact() end

---@public
---@param StressChange float @the StressChange to set
---@return void
function Item:setStressChange(StressChange) end

---@public
---@return float @the MaxRange
function Item:getMaxRange() end

---@public
---@param HungerChange float @the HungerChange to set
---@return void
function Item:setHungerChange(HungerChange) end

---@public
---@return float @the MinAngle
function Item:getMinAngle() end

---@public
---@param AlwaysKnockdown boolean @the AlwaysKnockdown to set
---@return void
function Item:setAlwaysKnockdown(AlwaysKnockdown) end

---@public
---@return float
function Item:getWeightEmpty() end

---@public
---@param MinDamage float @the MinDamage to set
---@return void
function Item:setMinDamage(MinDamage) end

---@public
---@param arg0 float
---@return void
function Item:setWeightEmpty(arg0) end

---@public
---@param ConditionMax int @the ConditionMax to set
---@return void
function Item:setConditionMax(ConditionMax) end

---@public
---@return String
function Item:getShellFallSound() end

---@public
---@return int @the SoundVolume
function Item:getSoundVolume() end

---@public
---@param CanBarricade boolean @the CanBarricade to set
---@return void
function Item:setCanBarricade(CanBarricade) end

---@public
---@param ToHitModifier float @the ToHitModifier to set
---@return void
function Item:setToHitModifier(ToHitModifier) end

---@public
---@return float @the PushBackMod
function Item:getPushBackMod() end

---@public
---@return boolean
function Item:isHidden() end

---@public
---@return String
function Item:getCustomEatSound() end

---@public
---@return int @the MaxHitCount
function Item:getMaxHitCount() end

---@public
---@param arg0 String
---@return void
function Item:setModID(arg0) end

---@public
---@param SoundVolume int @the SoundVolume to set
---@return void
function Item:setSoundVolume(SoundVolume) end

---@public
---@return String @the SpriteName
function Item:getSpriteName() end

---@public
---@param KnockBackOnNoDeath boolean @the KnockBackOnNoDeath to set
---@return void
function Item:setKnockBackOnNoDeath(KnockBackOnNoDeath) end

---@public
---@return String
function Item:getInsertAmmoStartSound() end

---@public
---@return ArrayList|Unknown
function Item:getClothingItemExtra() end

---@public
---@return String
function Item:getEjectAmmoSound() end

---@public
---@return String
function Item:getCloseSound() end

---@public
---@return float @the EnduranceChange
function Item:getEnduranceChange() end

---@public
---@param SwingAnim String @the SwingAnim to set
---@return void
function Item:setSwingAnim(SwingAnim) end

---@public
---@param PaletteChoices Stack|String @the PaletteChoices to set
---@return void
function Item:setPaletteChoices(PaletteChoices) end

---@public
---@param ReplaceOnUse String @the ReplaceOnUse to set
---@return void
function Item:setReplaceOnUse(ReplaceOnUse) end

---@public
---@return Boolean
function Item:isCantEat() end

---@public
---@return float @the BoredomChange
function Item:getBoredomChange() end

---@public
---@return String @the Icon
function Item:getIcon() end

---@public
---@return int
function Item:getNumberOfPages() end

---@public
---@return int @the DaysTotallyRotten
function Item:getDaysTotallyRotten() end

---@public
---@param ShareEndurance boolean @the ShareEndurance to set
---@return void
function Item:setShareEndurance(ShareEndurance) end

---@public
---@return float @the UseDelta
function Item:getUseDelta() end

---@public
---@return String
function Item:getMapID() end

---@public
---@return ArrayList|Unknown
function Item:getBloodClothingType() end

---@public
---@return boolean @the Ranged
function Item:isRanged() end

---@public
---@param OtherHandRequire String @the OtherHandRequire to set
---@return void
function Item:setOtherHandRequire(OtherHandRequire) end

---@public
---@param str String
---@return void
function Item:DoParam(str) end

---@public
---@param DisappearOnUse boolean @the DisappearOnUse to set
---@return void
function Item:setDisappearOnUse(DisappearOnUse) end

---@public
---@return boolean @the AngleFalloff
function Item:isAngleFalloff() end

---@public
---@return String @the PhysicsObject
function Item:getPhysicsObject() end

---@public
---@param SoundRadius int @the SoundRadius to set
---@return void
function Item:setSoundRadius(SoundRadius) end

---@public
---@param UseWhileEquipped boolean @the UseWhileEquipped to set
---@return void
function Item:setUseWhileEquipped(UseWhileEquipped) end

---@public
---@param NPCSoundBoost float @the NPCSoundBoost to set
---@return void
function Item:setNPCSoundBoost(NPCSoundBoost) end

---@public
---@param AngleFalloff boolean @the AngleFalloff to set
---@return void
function Item:setAngleFalloff(AngleFalloff) end
