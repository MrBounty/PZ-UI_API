---@class InventoryItem : zombie.inventory.InventoryItem
---@field protected previousOwner IsoGameCharacter
---@field protected ScriptItem Item
---@field protected cat ItemType
---@field protected container ItemContainer
---@field protected containerX int
---@field protected containerY int
---@field protected name String
---@field protected replaceOnUse String
---@field protected replaceOnUseFullType String
---@field protected ConditionMax int
---@field protected rightClickContainer ItemContainer
---@field protected texture Texture
---@field protected texturerotten Texture
---@field protected textureCooked Texture
---@field protected textureBurnt Texture
---@field protected type String
---@field protected fullType String
---@field protected uses int
---@field protected Age float
---@field protected LastAged float
---@field protected IsCookable boolean
---@field protected CookingTime float
---@field protected MinutesToCook float
---@field protected MinutesToBurn float
---@field public Cooked boolean
---@field protected Burnt boolean
---@field protected OffAge int
---@field protected OffAgeMax int
---@field protected Weight float
---@field protected ActualWeight float
---@field protected WorldTexture String
---@field protected Description String
---@field protected Condition int
---@field protected OffString String
---@field protected FreshString String
---@field protected CookedString String
---@field protected UnCookedString String
---@field protected FrozenString String
---@field protected BurntString String
---@field private brokenString String
---@field protected module String
---@field protected boredomChange float
---@field protected unhappyChange float
---@field protected stressChange float
---@field protected Taken ArrayList|Unknown
---@field protected placeDir IsoDirections
---@field protected newPlaceDir IsoDirections
---@field private _table KahluaTable
---@field public ReplaceOnUseOn String
---@field public col Color
---@field public IsWaterSource boolean
---@field public CanStoreWater boolean
---@field public CanStack boolean
---@field private activated boolean
---@field private isTorchCone boolean
---@field private lightDistance int
---@field private Count int
---@field public fatigueChange float
---@field public worldItem IsoWorldInventoryObject
---@field private customMenuOption String
---@field private tooltip String
---@field private displayCategory String
---@field private haveBeenRepaired int
---@field private broken boolean
---@field private originalName String
---@field public id int
---@field public RequiresEquippedBothHands boolean
---@field public byteData ByteBuffer
---@field public extraItems ArrayList|String
---@field private customName boolean
---@field private breakSound String
---@field protected alcoholic boolean
---@field private alcoholPower float
---@field private bandagePower float
---@field private ReduceInfectionPower float
---@field private customWeight boolean
---@field private customColor boolean
---@field private keyId int
---@field private taintedWater boolean
---@field private remoteController boolean
---@field private canBeRemote boolean
---@field private remoteControlID int
---@field private remoteRange int
---@field private colorRed float
---@field private colorGreen float
---@field private colorBlue float
---@field private countDownSound String
---@field private explosionSound String
---@field private equipParent IsoGameCharacter
---@field private evolvedRecipeName String
---@field private metalValue float
---@field private itemHeat float
---@field private meltingTime float
---@field private worker String
---@field private isWet boolean
---@field private wetCooldown float
---@field private itemWhenDry String
---@field private favorite boolean
---@field protected requireInHandOrInventory ArrayList|Unknown
---@field private map String
---@field private stashMap String
---@field public keepOnDeplete boolean
---@field private zombieInfected boolean
---@field private rainFactorZero boolean
---@field private itemCapacity float
---@field private maxCapacity int
---@field private brakeForce float
---@field private chanceToSpawnDamaged int
---@field private conditionLowerNormal float
---@field private conditionLowerOffroad float
---@field private wheelFriction float
---@field private suspensionDamping float
---@field private suspensionCompression float
---@field private engineLoudness float
---@field protected visual ItemVisual
---@field protected staticModel String
---@field private iconsForTexture ArrayList|Unknown
---@field private bloodClothingType ArrayList|Unknown
---@field private stashChance int
---@field private ammoType String
---@field private maxAmmo int
---@field private currentAmmoCount int
---@field private gunType String
---@field private attachmentType String
---@field private attachmentsProvided ArrayList|Unknown
---@field private attachedSlot int
---@field private attachedSlotType String
---@field private attachmentReplacement String
---@field private attachedToModel String
---@field private m_alternateModelName String
---@field private registry_id short
---@field public worldZRotation int
---@field public worldScale float
---@field private recordedMediaIndex short
---@field private mediaType byte
---@field public jobDelta float
---@field public jobType String
---@field tempBuffer ByteBuffer
---@field public mainCategory String
---@field private canBeActivated boolean
---@field private lightStrength float
---@field public CloseKillMove String
---@field private beingFilled boolean
InventoryItem = {}

---@public
---@param desc SurvivorDesc
---@return float
function InventoryItem:getScore(desc) end

---@public
---@return boolean
function InventoryItem:isRequiresEquippedBothHands() end

---@public
---@return String @the replaceOnUse
function InventoryItem:getReplaceOnUse() end

---@public
---@return int
function InventoryItem:getChanceToSpawnDamaged() end

---@public
---@return Texture @the texture
function InventoryItem:getTexture() end

---@public
---@return String
function InventoryItem:getModName() end

---@public
---@return float
function InventoryItem:getA() end

---@public
---@param module String @the module to set
---@return void
function InventoryItem:setModule(module) end

---@public
---@param activated boolean
---@return void
function InventoryItem:setActivated(activated) end

---@public
---@param arg0 String
---@return void
function InventoryItem:setAmmoType(arg0) end

---@public
---@param uses int @the uses to set
---@return void
function InventoryItem:setUses(uses) end

---@public
---@param custom boolean
---@return void
function InventoryItem:setCustomWeight(custom) end

---@public
---@return String
function InventoryItem:getAttachmentType() end

---@public
---@return float
function InventoryItem:getItemCapacity() end

---@public
---@param breakSound String
---@return void
function InventoryItem:setBreakSound(breakSound) end

---@public
---@param cat ItemType @the cat to set
---@return void
function InventoryItem:setCat(cat) end

---@public
---@param arg0 BloodBodyPartType
---@return float
function InventoryItem:getBlood(arg0) end

---@public
---@return float
function InventoryItem:getConditionLowerOffroad() end

---@public
---@return IsoDirections @the newPlaceDir
function InventoryItem:getNewPlaceDir() end

---@public
---@param arg0 String
---@return void
function InventoryItem:setEvolvedRecipeName(arg0) end

---@public
---@param textureCooked Texture @the textureCooked to set
---@return void
function InventoryItem:setTextureCooked(textureCooked) end

---@public
---@param arg0 float
---@return void
function InventoryItem:setMeltingTime(arg0) end

---@public
---@return boolean
function InventoryItem:IsFood() end

---@public
---@param arg0 float
---@return void
function InventoryItem:setSuspensionCompression(arg0) end

---@public
---@return float
function InventoryItem:getAlcoholPower() end

---@public
---@return int
function InventoryItem:getSaveType() end

---@public
---@return String
function InventoryItem:getWorldStaticItem() end

---@public
---@return boolean
function InventoryItem:isTrap() end

---@public
---@param arg0 short
---@return void
function InventoryItem:setRecordedMediaIndex(arg0) end

---@public
---@return float
function InventoryItem:getConditionLowerNormal() end

---@param arg0 DataInputStream
---@return InventoryItem
function InventoryItem:LoadFromFile(arg0) end

---@public
---@return String @the CookedString
function InventoryItem:getCookedString() end

---@public
---@param arg0 String
---@return void
function InventoryItem:setGunType(arg0) end

---throws java.io.IOException
---@public
---@param output ByteBuffer
---@param net boolean
---@return void
function InventoryItem:saveWithSize(output, net) end

---@public
---@return boolean
function InventoryItem:isTorchCone() end

---@public
---@return String
function InventoryItem:getEquipSound() end

---@public
---@param OffString String @the OffString to set
---@return void
function InventoryItem:setOffString(OffString) end

---@public
---@return String
function InventoryItem:getJobType() end

---@public
---@param CookedString String @the CookedString to set
---@return void
function InventoryItem:setCookedString(CookedString) end

---@public
---@return Texture @the textureBurnt
function InventoryItem:getTextureBurnt() end

---@public
---@param arg0 int
---@return void
function InventoryItem:setAttachedSlot(arg0) end

---@public
---@param containerY int @the containerY to set
---@return void
function InventoryItem:setContainerY(containerY) end

---@public
---@return boolean @the Burnt
function InventoryItem:isBurnt() end

---@public
---@return int
function InventoryItem:getCurrentAmmoCount() end

---@public
---@return int
function InventoryItem:getMaxAmmo() end

---@public
---@param arg0 int
---@return void
function InventoryItem:setWorldZRotation(arg0) end

---@public
---@return boolean
function InventoryItem:isBroken() end

---@public
---@return String
function InventoryItem:getTooltip() end

---@public
---@return boolean
function InventoryItem:isInLocalPlayerInventory() end

---@public
---@return boolean @the Cooked
function InventoryItem:isCooked() end

---@public
---@param color Color
---@return void
function InventoryItem:setColor(color) end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function InventoryItem:setBloodClothingType(arg0) end

---@public
---@return boolean @the DisappearOnUse
function InventoryItem:isDisappearOnUse() end

---@public
---@param arg0 boolean
---@return void
function InventoryItem:setFavorite(arg0) end

---@public
---@param tooltipUI ObjectTooltip
---@return void
---@overload fun(tooltipUI:ObjectTooltip, layout:ObjectTooltip.Layout)
function InventoryItem:DoTooltip(tooltipUI) end

---@public
---@param tooltipUI ObjectTooltip
---@param layout ObjectTooltip.Layout
---@return void
function InventoryItem:DoTooltip(tooltipUI, layout) end

---@public
---@return ItemContainer
function InventoryItem:getOutermostContainer() end

---@public
---@param arg0 String
---@return void
function InventoryItem:setAttachmentReplacement(arg0) end

---@public
---@param Burnt boolean @the Burnt to set
---@return void
function InventoryItem:setBurnt(Burnt) end

---@public
---@return int
function InventoryItem:getHaveBeenRepaired() end

---@public
---@return int @the containerX
function InventoryItem:getContainerX() end

---@public
---@param v boolean
---@return void
function InventoryItem:setBeingFilled(v) end

---throws java.io.IOException
---@public
---@param output ByteBuffer
---@param net boolean
---@return void
function InventoryItem:save(output, net) end

---@public
---@param arg0 String
---@return void
function InventoryItem:setWorker(arg0) end

---@public
---@return int
function InventoryItem:getID() end

---@public
---@param arg0 float
---@return void
function InventoryItem:setSuspensionDamping(arg0) end

---@public
---@return boolean
function InventoryItem:IsDrainable() end

---@public
---@param customMenuOption String
---@return void
function InventoryItem:setCustomMenuOption(customMenuOption) end

---@public
---@return IsoGameCharacter @the previousOwner
function InventoryItem:getPreviousOwner() end

---@public
---@return boolean
function InventoryItem:isCustomName() end

---@public
---@return int
function InventoryItem:getMechanicType() end

---@public
---@param arg0 float
---@return void
function InventoryItem:setWheelFriction(arg0) end

---@public
---@param lightStrength float
---@return void
function InventoryItem:setLightStrength(lightStrength) end

---@public
---@param other InventoryItem
---@return void
function InventoryItem:copyConditionModData(other) end

---@public
---@return float
function InventoryItem:getWheelFriction() end

---@public
---@return boolean
function InventoryItem:isTaintedWater() end

---@public
---@param arg0 boolean
---@return void
function InventoryItem:setWet(arg0) end

---@public
---@return String
function InventoryItem:getGunType() end

---@public
---@return boolean
function InventoryItem:isEquippedNoSprint() end

---@public
---@return String @the module
function InventoryItem:getModule() end

---@public
---@param container ItemContainer @the container to set
---@return void
function InventoryItem:setContainer(container) end

---@public
---@return boolean @the IsCookable
function InventoryItem:isIsCookable() end

---@public
---@param lightDistance int
---@return void
function InventoryItem:setLightDistance(lightDistance) end

---@public
---@return String @the BurntString
function InventoryItem:getBurntString() end

---@public
---@return String @the OffString
function InventoryItem:getOffString() end

---@public
---@param newPlaceDir IsoDirections @the newPlaceDir to set
---@return void
function InventoryItem:setNewPlaceDir(newPlaceDir) end

---@public
---@param arg0 float
---@return void
function InventoryItem:setConditionLowerNormal(arg0) end

---@public
---@return String
function InventoryItem:getCategory() end

---@public
---@param ActualWeight float @the ActualWeight to set
---@return void
function InventoryItem:setActualWeight(ActualWeight) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return InventoryItem
---@overload fun(arg0:ByteBuffer, arg1:int, arg2:boolean)
function InventoryItem:loadItem(arg0, arg1) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return InventoryItem
function InventoryItem:loadItem(arg0, arg1, arg2) end

---@public
---@return boolean
function InventoryItem:isFishingLure() end

---@public
---@param remoteController boolean
---@return void
function InventoryItem:setRemoteController(remoteController) end

---@public
---@return int @the OffAge
function InventoryItem:getOffAge() end

---@public
---@param bandagePower float
---@return void
function InventoryItem:setBandagePower(bandagePower) end

---@public
---@param remoteRange int
---@return void
function InventoryItem:setRemoteRange(remoteRange) end

---Return the real condition of the weapon, based on this calcul : Condition/ConditionMax * 100
---@public
---@return float @float
function InventoryItem:getCurrentCondition() end

---@public
---@param arg0 boolean
---@return void
function InventoryItem:setInfected(arg0) end

---@public
---@param arg0 BaseSoundEmitter
---@return void
function InventoryItem:updateSound(arg0) end

---@public
---@return byte
function InventoryItem:getMediaType() end

---@public
---@return float
function InventoryItem:getContentsWeight() end

---@public
---@return float @the CookingTime
function InventoryItem:getCookingTime() end

---@public
---@return ColorInfo
function InventoryItem:getColorInfo() end

---@public
---@return float
function InventoryItem:getLightStrength() end

---@public
---@param displayCategory String
---@return void
function InventoryItem:setDisplayCategory(displayCategory) end

---@public
---@return float @the unhappyChange
function InventoryItem:getUnhappyChange() end

---@public
---@return String
function InventoryItem:getDisplayCategory() end

---@public
---@param Weight float @the Weight to set
---@return void
function InventoryItem:setWeight(Weight) end

---@public
---@return int @the OffAgeMax
function InventoryItem:getOffAgeMax() end

---@public
---@return String
function InventoryItem:getStaticModel() end

---@public
---@return String
function InventoryItem:getCountDownSound() end

---@public
---@return ArrayList|Unknown
function InventoryItem:getRequireInHandOrInventory() end

---@public
---@param Cooked boolean @the Cooked to set
---@return void
function InventoryItem:setCooked(Cooked) end

---@public
---@return float
function InventoryItem:getR() end

---@public
---@param arg0 int
---@return void
function InventoryItem:setChanceToSpawnDamaged(arg0) end

---@public
---@param x int
---@param y int
---@return void
function InventoryItem:SetContainerPosition(x, y) end

---@public
---@return boolean
function InventoryItem:isConditionAffectsCapacity() end

---@public
---@return boolean
function InventoryItem:isAlcoholic() end

---@public
---@param texturerotten Texture @the texturerotten to set
---@return void
function InventoryItem:setTexturerotten(texturerotten) end

---@public
---@param IsWaterSource boolean
---@return void
function InventoryItem:setIsWaterSource(IsWaterSource) end

---@public
---@param type String
---@return void
function InventoryItem:addExtraItem(type) end

---@public
---@param item InventoryItem
---@return boolean
function InventoryItem:CanStack(item) end

---@public
---@return String
function InventoryItem:getAttachmentReplacement() end

---@public
---@return String
function InventoryItem:getAttachedSlotType() end

---@public
---@return ItemContainer @the container
function InventoryItem:getContainer() end

---@public
---@param unhappyChange float @the unhappyChange to set
---@return void
function InventoryItem:setUnhappyChange(unhappyChange) end

---@public
---@return boolean
function InventoryItem:isCustomWeight() end

---@public
---@return String @the name
function InventoryItem:getName() end

---@public
---@return boolean
function InventoryItem:IsWeapon() end

---@public
---@param arg0 BloodBodyPartType
---@param arg1 float
---@return void
function InventoryItem:setBlood(arg0, arg1) end

---@public
---@param arg0 float
---@return void
function InventoryItem:setColorRed(arg0) end

---@public
---@return String
function InventoryItem:getDisplayName() end

---@public
---@return Texture @the textureCooked
function InventoryItem:getTextureCooked() end

---@public
---@return String @the swingAnim
function InventoryItem:getSwingAnim() end

---@public
---@return boolean
function InventoryItem:isUseWorldItem() end

---@public
---@return short
function InventoryItem:getRecordedMediaIndex() end

---@public
---@return float
function InventoryItem:getInvHeat() end

---@public
---@param arg0 boolean
---@return void
function InventoryItem:setActivatedRemote(arg0) end

---@public
---@return int @the containerY
function InventoryItem:getContainerY() end

---@public
---@return KahluaTable
function InventoryItem:getModData() end

---@public
---@return int
function InventoryItem:getMaxCapacity() end

---@public
---@return ItemVisual
function InventoryItem:getVisual() end

---@public
---@return float
function InventoryItem:getBrakeForce() end

---@public
---@return int
function InventoryItem:getRemoteControlID() end

---@public
---@return float
function InventoryItem:getHotbarEquippedWeight() end

---@public
---@param o IsoObject
---@return void
function InventoryItem:storeInByteData(o) end

---@public
---@param arg0 String
---@return void
function InventoryItem:setAttachedToModel(arg0) end

---@public
---@param alcoholic boolean
---@return void
function InventoryItem:setAlcoholic(alcoholic) end

---@public
---@param Age float @the Age to set
---@return void
function InventoryItem:setAge(Age) end

---@public
---@param Description String @the Description to set
---@return void
function InventoryItem:setDescription(Description) end

---@public
---@return String
function InventoryItem:getReplaceOnUseFullType() end

---@public
---@param fatigueChange float
---@return void
function InventoryItem:setFatigueChange(fatigueChange) end

---@public
---@return ArrayList|Unknown
function InventoryItem:getIconsForTexture() end

---@public
---@param arg0 String
---@return void
function InventoryItem:setStashMap(arg0) end

---@public
---@param arg0 String
---@return void
function InventoryItem:setCountDownSound(arg0) end

---@public
---@return int
function InventoryItem:getStashChance() end

---@public
---@return boolean @the IsWaterSource
function InventoryItem:isWaterSource() end

---@public
---@param type String
---@return void
function InventoryItem:setJobType(type) end

---@public
---@param OffAgeMax int @the OffAgeMax to set
---@return void
function InventoryItem:setOffAgeMax(OffAgeMax) end

---@public
---@param arg0 boolean
---@return void
function InventoryItem:setCustomColor(arg0) end

---@public
---@return String
function InventoryItem:getItemWhenDry() end

---@public
---@return boolean
function InventoryItem:shouldUpdateInWorld() end

---@public
---@return void
function InventoryItem:setAutoAge() end

---@public
---@return boolean @the AlwaysWelcomeGift
function InventoryItem:isAlwaysWelcomeGift() end

---@public
---@param boredomChange float @the boredomChange to set
---@return void
function InventoryItem:setBoredomChange(boredomChange) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function InventoryItem:setEquipParent(arg0) end

---@public
---@param arg0 String
---@return void
function InventoryItem:setAttachedSlotType(arg0) end

---@public
---@return ItemType @the cat
function InventoryItem:getCat() end

---@public
---@return boolean
function InventoryItem:isCookable() end

---@public
---@return float
function InventoryItem:getReduceInfectionPower() end

---@public
---@param reduceInfectionPower float
---@return void
function InventoryItem:setReduceInfectionPower(reduceInfectionPower) end

---@public
---@param taintedWater boolean
---@return void
function InventoryItem:setTaintedWater(taintedWater) end

---@public
---@return float
function InventoryItem:getFatigueChange() end

---@public
---@return float @the Weight
function InventoryItem:getWeight() end

---@public
---@param isTorchCone boolean
---@return void
function InventoryItem:setTorchCone(isTorchCone) end

---@public
---@return String
function InventoryItem:getCustomMenuOption() end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function InventoryItem:setAttachmentsProvided(arg0) end

---@public
---@return void
function InventoryItem:UseItem() end

---@public
---@return String @the Description
function InventoryItem:getDescription() end

---@public
---@return boolean
function InventoryItem:isActivated() end

---@public
---@param arg0 int
---@return void
function InventoryItem:setID(arg0) end

---@public
---@return boolean
function InventoryItem:canBeRemote() end

---@public
---@param count int
---@return void
function InventoryItem:setCount(count) end

---@public
---@return ClothingItem
function InventoryItem:getClothingItem() end

---@public
---@return float
function InventoryItem:getBandagePower() end

---@public
---@return float
function InventoryItem:getExtraItemsWeight() end

---@public
---@param type String @the type to set
---@return void
function InventoryItem:setType(type) end

---@public
---@return void
function InventoryItem:doBuildingStash() end

---@public
---@return boolean
function InventoryItem:isHidden() end

---@public
---@param arg0 String
---@return boolean
function InventoryItem:hasTag(arg0) end

---@public
---@param arg0 float
---@return void
function InventoryItem:setConditionLowerOffroad(arg0) end

---@public
---@param stressChange float @the stressChange to set
---@return void
function InventoryItem:setStressChange(stressChange) end

---@public
---@return ItemReplacement
function InventoryItem:getItemReplacementPrimaryHand() end

---@public
---@param arg0 float
---@return void
function InventoryItem:setItemHeat(arg0) end

---@public
---@param item InventoryItem
---@return boolean
function InventoryItem:ModDataMatches(item) end

---@public
---@return ArrayList|Unknown
function InventoryItem:getClothingItemExtraOption() end

---@public
---@param MinutesToBurn float @the MinutesToBurn to set
---@return void
function InventoryItem:setMinutesToBurn(MinutesToBurn) end

---@public
---@return boolean
function InventoryItem:isRemoteController() end

---@public
---@return float
function InventoryItem:getMeltingTime() end

---@public
---@param CookingTime float @the CookingTime to set
---@return void
function InventoryItem:setCookingTime(CookingTime) end

---@public
---@return void
---@overload fun(bCrafting:boolean)
---@overload fun(bCrafting:boolean, bInContainer:boolean)
function InventoryItem:Use() end

---@public
---@param bCrafting boolean
---@return void
function InventoryItem:Use(bCrafting) end

---@public
---@param bCrafting boolean
---@param bInContainer boolean
---@return void
function InventoryItem:Use(bCrafting, bInContainer) end

---@public
---@return String
function InventoryItem:getBreakSound() end

---@public
---@return float @the ActualWeight
function InventoryItem:getActualWeight() end

---@public
---@param arg0 BloodBodyPartType
---@param arg1 float
---@return void
function InventoryItem:setDirt(arg0, arg1) end

---@public
---@param arg0 float
---@return void
function InventoryItem:setColorGreen(arg0) end

---@public
---@return int
function InventoryItem:getLightDistance() end

---@public
---@return String
function InventoryItem:getMakeUpType() end

---@public
---@return Texture
function InventoryItem:getTex() end

---@public
---@return boolean
function InventoryItem:isEmittingLight() end

---@public
---@param arg0 int
---@return void
function InventoryItem:setStashChance(arg0) end

---@public
---@return float
function InventoryItem:getSuspensionCompression() end

---@public
---@return String
function InventoryItem:getBodyLocation() end

---@public
---@return String
function InventoryItem:getFullType() end

---@public
---@param ReplaceOnUseOn String
---@return void
function InventoryItem:setReplaceOnUseOn(ReplaceOnUseOn) end

---@public
---@param placeDir IsoDirections @the placeDir to set
---@return void
function InventoryItem:setPlaceDir(placeDir) end

---@public
---@return float
function InventoryItem:getMetalValue() end

---@public
---@param BurntString String @the BurntString to set
---@return void
function InventoryItem:setBurntString(BurntString) end

---@public
---@return String
function InventoryItem:getClothingItemName() end

---@public
---@param ConditionMax int @the ConditionMax to set
---@return void
function InventoryItem:setConditionMax(ConditionMax) end

---@public
---@return ItemContainer @the rightClickContainer
function InventoryItem:getRightClickContainer() end

---@public
---@return boolean
function InventoryItem:hasDirt() end

---@public
---@param arg0 float
---@return String
function InventoryItem:getCleanString(arg0) end

---@public
---@return ArrayList|Unknown
function InventoryItem:getTags() end

---@public
---@return boolean
function InventoryItem:haveExtraItems() end

---@public
---@param arg0 KahluaTable
---@return void
function InventoryItem:copyModData(arg0) end

---@public
---@return boolean
function InventoryItem:isFavorite() end

---@public
---@return float
function InventoryItem:getTorchDot() end

---@public
---@return int
function InventoryItem:getRemoteRange() end

---@public
---@return int @the uses
function InventoryItem:getUses() end

---@public
---@return void
function InventoryItem:synchWithVisual() end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function InventoryItem:setIconsForTexture(arg0) end

---@public
---@param time float
---@return void
function InventoryItem:setLastAged(time) end

---@public
---@param MinutesToCook float @the MinutesToCook to set
---@return void
function InventoryItem:setMinutesToCook(MinutesToCook) end

---@public
---@param containerX int @the containerX to set
---@return void
function InventoryItem:setContainerX(containerX) end

---@public
---@return String @the WorldTexture
function InventoryItem:getWorldTexture() end

---@public
---@return String
function InventoryItem:getModID() end

---@public
---@return String
function InventoryItem:getReplaceOnUseOn() end

---@public
---@return ArrayList|IsoObject @the Taken
function InventoryItem:getTaken() end

---@public
---@return void
function InventoryItem:update() end

---@public
---@return float
function InventoryItem:getColorGreen() end

---@public
---@return float
function InventoryItem:getEquippedWeight() end

---@public
---@return String
function InventoryItem:getEvolvedRecipeName() end

---@public
---@return boolean
function InventoryItem:isProtectFromRainWhileEquipped() end

---@public
---@param OffAge int @the OffAge to set
---@return void
function InventoryItem:setOffAge(OffAge) end

---@public
---@param arg0 MediaData
---@return void
function InventoryItem:setRecordedMediaData(arg0) end

---@public
---@return IsoWorldInventoryObject
function InventoryItem:getWorldItem() end

---@public
---@return String
function InventoryItem:getExplosionSound() end

---@public
---@return boolean
function InventoryItem:canStoreWater() end

---@public
---@return String
function InventoryItem:getAmmoType() end

---@public
---@param keyId int
---@return void
function InventoryItem:setKeyId(keyId) end

---@public
---@return short
function InventoryItem:getRegistry_id() end

---@public
---@param arg0 int
---@return void
function InventoryItem:setCurrentAmmoCount(arg0) end

---@public
---@return ArrayList|Unknown
function InventoryItem:getAttachmentsProvided() end

---@public
---@return boolean
function InventoryItem:isTwoHandWeapon() end

---@public
---@return float
function InventoryItem:getColorRed() end

---@public
---@return String
function InventoryItem:getReplaceOnUseOnString() end

---@public
---@return boolean
function InventoryItem:hasBlood() end

---@public
---@param arg0 float
---@return void
function InventoryItem:setWorldScale(arg0) end

---@public
---@return String
function InventoryItem:getAlternateModelName() end

---@public
---@return String
function InventoryItem:getStringItemType() end

---@public
---@param canBeRemote boolean
---@return void
function InventoryItem:setCanBeRemote(canBeRemote) end

---@public
---@return boolean
function InventoryItem:canEmitLight() end

---@public
---@return boolean @the CanBandage
function InventoryItem:isCanBandage() end

---@public
---@param customName boolean
---@return void
function InventoryItem:setCustomName(customName) end

---@public
---@param alcoholPower float
---@return void
function InventoryItem:setAlcoholPower(alcoholPower) end

---@public
---@param UnCookedString String @the UnCookedString to set
---@return void
function InventoryItem:setUnCookedString(UnCookedString) end

---@public
---@return boolean
function InventoryItem:IsInventoryContainer() end

---@public
---@param arg0 float
---@return void
function InventoryItem:setWetCooldown(arg0) end

---@public
---@param arg0 int
---@return void
function InventoryItem:setMaxAmmo(arg0) end

---@public
---@param name String @the name to set
---@return void
function InventoryItem:setName(name) end

---@public
---@return String
function InventoryItem:getWorker() end

---@public
---@return float
function InventoryItem:getWetCooldown() end

---@public
---@return ByteBuffer
function InventoryItem:getByteData() end

---@public
---@return ArrayList|String
function InventoryItem:getExtraItems() end

---@param arg0 InventoryItem
---@return boolean
function InventoryItem:CanStackNoTemp(arg0) end

---@public
---@return float @the boredomChange
function InventoryItem:getBoredomChange() end

---@public
---@param ScriptItem Item @the ScriptItem to set
---@return void
function InventoryItem:setScriptItem(ScriptItem) end

---@public
---@param arg0 byte
---@return void
function InventoryItem:setMediaType(arg0) end

---@public
---@param DefaultModData KahluaTable
---@return void
function InventoryItem:CopyModData(DefaultModData) end

---@public
---@return String
function InventoryItem:getEatType() end

---@public
---@param w IsoWorldInventoryObject
---@return void
function InventoryItem:setWorldItem(w) end

---@public
---@return Texture @the texturerotten
function InventoryItem:getTexturerotten() end

---@public
---@return String
function InventoryItem:getBringToBearSound() end

---@public
---@return String
function InventoryItem:getType() end

---@public
---@return float
function InventoryItem:getUnequippedWeight() end

---@public
---@return boolean
function InventoryItem:allowRandomTint() end

---@public
---@return float @the MinutesToBurn
function InventoryItem:getMinutesToBurn() end

---@public
---@param arg0 BloodBodyPartType
---@return float
function InventoryItem:getDirt(arg0) end

---@public
---@return boolean
function InventoryItem:isInfected() end

---@public
---@param textureBurnt Texture @the textureBurnt to set
---@return void
function InventoryItem:setTextureBurnt(textureBurnt) end

---@public
---@param arg0 float
---@return void
function InventoryItem:setEngineLoudness(arg0) end

---@public
---@return int
function InventoryItem:getAttachedSlot() end

---@public
---@param arg0 String
---@return void
function InventoryItem:setItemWhenDry(arg0) end

---@public
---@return IsoGameCharacter
function InventoryItem:getEquipParent() end

---@public
---@param arg0 float
---@return void
function InventoryItem:setItemCapacity(arg0) end

---@public
---@param arg0 float
---@return void
function InventoryItem:setMetalValue(arg0) end

---@public
---@return boolean
function InventoryItem:isBeingFilled() end

---@public
---@param rightClickContainer ItemContainer @the rightClickContainer to set
---@return void
function InventoryItem:setRightClickContainer(rightClickContainer) end

---@public
---@return int @the ConditionMax
function InventoryItem:getConditionMax() end

---@public
---@param Taken ArrayList|IsoObject @the Taken to set
---@return void
function InventoryItem:setTaken(Taken) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function InventoryItem:load(arg0, arg1) end

---@public
---@param IsCookable boolean @the IsCookable to set
---@return void
function InventoryItem:setIsCookable(IsCookable) end

---@public
---@return boolean
function InventoryItem:isHairDye() end

---@public
---@param haveBeenRepaired int
---@return void
function InventoryItem:setHaveBeenRepaired(haveBeenRepaired) end

---@public
---@return float
function InventoryItem:getJobDelta() end

---@public
---@param activatedItem boolean
---@return void
function InventoryItem:setCanBeActivated(activatedItem) end

---@public
---@return float
function InventoryItem:getB() end

---@public
---@return String
function InventoryItem:getAttachedToModel() end

---@public
---@return float
function InventoryItem:getLastAged() end

---@public
---@return ItemReplacement
function InventoryItem:getItemReplacementSecondHand() end

---@public
---@return float @the Age
function InventoryItem:getAge() end

---@public
---@param arg0 float
---@return void
function InventoryItem:setColorBlue(arg0) end

---@public
---@return boolean
function InventoryItem:isRecordedMedia() end

---@public
---@return String
function InventoryItem:getFabricType() end

---@public
---@return int
function InventoryItem:getKeyId() end

---@public
---@param explosionSound String
---@return void
function InventoryItem:setExplosionSound(explosionSound) end

---@public
---@return boolean
function InventoryItem:IsMap() end

---@public
---@param replaceOnUse String @the replaceOnUse to set
---@return void
function InventoryItem:setReplaceOnUse(replaceOnUse) end

---@public
---@return MediaData
function InventoryItem:getMediaData() end

---@public
---@param WorldTexture String @the WorldTexture to set
---@return void
function InventoryItem:setWorldTexture(WorldTexture) end

---@public
---@return boolean
function InventoryItem:isWet() end

---@public
---@param arg0 int
---@return void
function InventoryItem:setMaxCapacity(arg0) end

---@public
---@return boolean
function InventoryItem:hasModData() end

---@public
---@return float
function InventoryItem:getG() end

---@public
---@return float
function InventoryItem:getItemHeat() end

---@public
---@param remoteControlID int
---@return void
function InventoryItem:setRemoteControlID(remoteControlID) end

---@public
---@return ArrayList|Unknown
function InventoryItem:getClothingItemExtra() end

---@public
---@param texture Texture @the texture to set
---@return void
function InventoryItem:setTexture(texture) end

---@public
---@param arg0 Item
---@return void
function InventoryItem:setRegistry_id(arg0) end

---@public
---@param other InventoryItem
---@return void
function InventoryItem:setConditionFromModData(other) end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function InventoryItem:setRequireInHandOrInventory(arg0) end

---@public
---@return String
function InventoryItem:getUnequipSound() end

---@public
---@param arg0 String
---@return void
function InventoryItem:setAttachmentType(arg0) end

---@public
---@param broken boolean
---@return void
function InventoryItem:setBroken(broken) end

---@public
---@param delta float
---@return void
function InventoryItem:setJobDelta(delta) end

---@public
---@return int @the Condition
function InventoryItem:getCondition() end

---@public
---@return int
function InventoryItem:getCount() end

---@public
---@return Color
function InventoryItem:getColor() end

---@public
---@return float
function InventoryItem:HowRotten() end

---@public
---@return float
function InventoryItem:getSuspensionDamping() end

---@public
---@return boolean
function InventoryItem:isInPlayerInventory() end

---@public
---@param arg0 float
---@return void
function InventoryItem:setBrakeForce(arg0) end

---@public
---@return boolean
function InventoryItem:IsClothing() end

---@public
---@param tooltip String
---@return void
function InventoryItem:setTooltip(tooltip) end

---@public
---@return Item @the ScriptItem
function InventoryItem:getScriptItem() end

---@public
---@return float
function InventoryItem:getEngineLoudness() end

---@public
---@return boolean
function InventoryItem:isCustomColor() end

---@public
---@return boolean
function InventoryItem:isEquipped() end

---@public
---@return boolean
function InventoryItem:IsLiterature() end

---@public
---@return float @the stressChange
function InventoryItem:getStressChange() end

---@public
---@return IsoDirections @the placeDir
function InventoryItem:getPlaceDir() end

---@public
---@param Condition int @the Condition to set
---@return void
---@overload fun(Condition:int, doSound:boolean)
function InventoryItem:setCondition(Condition) end

---@public
---@param Condition int
---@param doSound boolean
---@return void
function InventoryItem:setCondition(Condition, doSound) end

---@public
---@return String @the UnCookedString
function InventoryItem:getUnCookedString() end

---@public
---@return String
function InventoryItem:getConsolidateOption() end

---@public
---@return float
function InventoryItem:getColorBlue() end

---@public
---@return float @the MinutesToCook
function InventoryItem:getMinutesToCook() end

---@public
---@return void
function InventoryItem:updateAge() end

---@public
---@return boolean
function InventoryItem:finishupdate() end

---@public
---@param previousOwner IsoGameCharacter @the previousOwner to set
---@return void
function InventoryItem:setPreviousOwner(previousOwner) end

---@public
---@return boolean
function InventoryItem:isVanilla() end

---@public
---@return boolean
function InventoryItem:IsRotten() end

---@public
---@return boolean
function InventoryItem:canBeActivated() end

---@public
---@return ArrayList|Unknown
function InventoryItem:getBloodClothingType() end
