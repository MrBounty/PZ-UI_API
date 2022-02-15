---@class Food : zombie.inventory.types.Food
---@field protected bBadCold boolean
---@field protected bGoodHot boolean
---@field private MIN_HEAT float
---@field private MAX_HEAT float
---@field protected Heat float
---@field protected endChange float
---@field protected hungChange float
---@field protected useOnConsume String
---@field protected rotten boolean
---@field protected bDangerousUncooked boolean
---@field protected LastCookMinute int
---@field public thirstChange float
---@field public Poison boolean
---@field private ReplaceOnCooked List|Unknown
---@field private baseHunger float
---@field public spices ArrayList|String
---@field private isSpice boolean
---@field private poisonDetectionLevel int
---@field private PoisonLevelForRecipe Integer
---@field private UseForPoison int
---@field private PoisonPower int
---@field private FoodType String
---@field private CustomEatSound String
---@field private RemoveNegativeEffectOnCooked boolean
---@field private Chef String
---@field private OnCooked String
---@field private WorldTextureCooked String
---@field private WorldTextureRotten String
---@field private WorldTextureOverdone String
---@field private fluReduction int
---@field private ReduceFoodSickness int
---@field private painReduction float
---@field private HerbalistType String
---@field private carbohydrates float
---@field private lipids float
---@field private proteins float
---@field private calories float
---@field private packaged boolean
---@field private freezingTime float
---@field private frozen boolean
---@field private canBeFrozen boolean
---@field protected LastFrozenUpdate float
---@field public FreezerAgeMultiplier float
---@field private replaceOnRotten String
---@field private forceFoodTypeAsName boolean
---@field private rottenTime float
---@field private compostTime float
---@field private onEat String
---@field private badInMicrowave boolean
---@field private cookedInMicrowave boolean
---@field private m_cookingSound long
---@field private m_cookingParameter int
---@field private COOKING_STATE_COOKING int
---@field private COOKING_STATE_BURNING int
Food = {}

---@public
---@return boolean
function Food:canBeFrozen() end

---@public
---@param replaceOnCooked List|String
---@return void
function Food:setReplaceOnCooked(replaceOnCooked) end

---Overrides:
---
---update in class InventoryItem
---@public
---@return void
function Food:update() end

---@public
---@return float
function Food:getInvHeat() end

---Overrides:
---
---updateAge in class InventoryItem
---@public
---@return void
function Food:updateAge() end

---@public
---@return Texture
function Food:getTex() end

---@public
---@param arg0 int
---@return void
function Food:setFluReduction(arg0) end

---@public
---@return boolean
function Food:isThawing() end

---@public
---@param customEatSound String
---@return void
function Food:setCustomEatSound(customEatSound) end

---@public
---@return boolean
function Food:IsFood() end

---@public
---@return float
function Food:getCompostTime() end

---@public
---@return boolean
function Food:isGoodHot() end

---@public
---@param arg0 float
---@return void
function Food:setCompostTime(arg0) end

---@public
---@param hungChange float
---@return void
function Food:setHungChange(hungChange) end

---Overrides:
---
---getWorldTexture in class InventoryItem
---@public
---@return String @the WorldTexture
function Food:getWorldTexture() end

---@public
---@return boolean
function Food:isFresh() end

---@public
---@return float
function Food:getCalories() end

---@private
---@param arg0 BaseSoundEmitter
---@return void
function Food:setCookingParameter(arg0) end

---@public
---@return boolean
function Food:isFreezing() end

---@public
---@param poisonDetectionLevel int
---@return void
function Food:setPoisonDetectionLevel(poisonDetectionLevel) end

---This method is annotated as @Deprecated
---
---Deprecated. 
---@public
---@return float
function Food:getBaseHungChange() end

---@public
---@return boolean
function Food:isBadCold() end

---@public
---@param arg0 float
---@return void
function Food:setPainReduction(arg0) end

---@public
---@param useOnConsume String
---@return void
function Food:setUseOnConsume(useOnConsume) end

---@public
---@return void
function Food:setAutoAge() end

---@public
---@return boolean
function Food:shouldUpdateInWorld() end

---@public
---@return float
function Food:getProteins() end

---@public
---@param foodType String
---@return void
function Food:setFoodType(foodType) end

---@public
---@return String
function Food:getChef() end

---@public
---@return float
function Food:getThirstChange() end

---@public
---@return String
function Food:getUseOnConsume() end

---@public
---@return String
function Food:getFoodType() end

---@public
---@param LastCookMinute int
---@return void
function Food:setLastCookMinute(LastCookMinute) end

---@public
---@return float
function Food:getHungChange() end

---Overrides:
---
---DoTooltip in class InventoryItem
---@public
---@param tooltipUI ObjectTooltip
---@param layout ObjectTooltip.Layout
---@return void
function Food:DoTooltip(tooltipUI, layout) end

---@public
---@param arg0 BaseSoundEmitter
---@return void
function Food:updateSound(arg0) end

---@private
---@param arg0 ItemContainer
---@return void
function Food:updateRotting(arg0) end

---@public
---@param arg0 float
---@return void
function Food:setLipids(arg0) end

---@public
---@return int
function Food:getPoisonPower() end

---@public
---@return float
function Food:getBaseHunger() end

---@public
---@param arg0 float
---@return void
function Food:setCalories(arg0) end

---@public
---@param arg0 float
---@return void
function Food:setCarbohydrates(arg0) end

---@public
---@param thirstChange float
---@return void
function Food:setThirstChange(thirstChange) end

---@public
---@param bBadCold boolean
---@return void
function Food:setBadCold(bBadCold) end

---@public
---@return float
function Food:getActualWeight() end

---throws java.io.IOException
---
---Overrides:
---
---save in class InventoryItem
---@public
---@param output ByteBuffer
---@param net boolean
---@return void
function Food:save(output, net) end

---@public
---@param arg0 boolean
---@return void
function Food:setPackaged(arg0) end

---@public
---@return String
function Food:getCookingSound() end

---@public
---@param onCooked String
---@return void
function Food:setOnCooked(onCooked) end

---@public
---@return boolean
function Food:isBadInMicrowave() end

---@public
---@param removeNegativeEffectOnCooked boolean
---@return void
function Food:setRemoveNegativeEffectOnCooked(removeNegativeEffectOnCooked) end

---@private
---@return void
function Food:destroyThisItem() end

---@public
---@return boolean
function Food:isPackaged() end

---@public
---@param arg0 int
---@return void
function Food:setReduceFoodSickness(arg0) end

---@public
---@param endChange float
---@return void
function Food:setEndChange(endChange) end

---@public
---@return boolean
function Food:isRemoveNegativeEffectOnCooked() end

---@public
---@return String
function Food:getReplaceOnRotten() end

---@public
---@return String
function Food:getOnCooked() end

---@public
---@return boolean
function Food:isbDangerousUncooked() end

---@public
---@return float
function Food:getHungerChange() end

---@public
---@param isSpice boolean
---@return void
function Food:setSpice(isSpice) end

---@public
---@return float
function Food:getLipids() end

---@public
---@param arg0 ItemContainer
---@return void
function Food:updateFreezing(arg0) end

---@public
---@return int
function Food:getFluReduction() end

---@public
---@return String
function Food:getName() end

---@public
---@param arg0 boolean
---@return void
function Food:setBadInMicrowave(arg0) end

---@public
---@param useForPoison int
---@return void
function Food:setUseForPoison(useForPoison) end

---@public
---@return int
function Food:getReduceFoodSickness() end

---@public
---@param arg0 boolean
---@return void
function Food:setFrozen(arg0) end

---@public
---@return boolean
function Food:isRotten() end

---@public
---@return float
function Food:getCarbohydrates() end

---@private
---@return boolean
function Food:shouldPlayCookingSound() end

---@public
---@return ArrayList|Unknown
function Food:getSpices() end

---@public
---@return String
function Food:getOnEat() end

---Overrides:
---
---finishupdate in class InventoryItem
---@public
---@return boolean
function Food:finishupdate() end

---@public
---@return float
function Food:getPainReduction() end

---@public
---@param bDangerousUncooked boolean
---@return void
function Food:setbDangerousUncooked(bDangerousUncooked) end

---Overrides:
---
---getStressChange in class InventoryItem
---@public
---@return float @the stressChange
function Food:getStressChange() end

---@public
---@return float
function Food:getHeat() end

---Overrides:
---
---getCategory in class InventoryItem
---@public
---@return String
function Food:getCategory() end

---@public
---@param bGoodHot boolean
---@return void
function Food:setGoodHot(bGoodHot) end

---@public
---@param arg0 float
---@return void
function Food:multiplyFoodValues(arg0) end

---@public
---@param arg0 boolean
---@return void
function Food:setCanBeFrozen(arg0) end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function Food:setSpices(arg0) end

---@public
---@param chef String
---@return void
function Food:setChef(chef) end

---@public
---@return int
function Food:getPoisonDetectionLevel() end

---@public
---@return int
function Food:getUseForPoison() end

---@public
---@return Integer
function Food:getPoisonLevelForRecipe() end

---@public
---@return float
function Food:getRottenTime() end

---Overrides:
---
---getBoredomChange in class InventoryItem
---@public
---@return float @the boredomChange
function Food:getBoredomChange() end

---@public
---@param arg0 float
---@return void
function Food:setProteins(arg0) end

---@public
---@param rotten boolean
---@return void
function Food:setRotten(rotten) end

---@public
---@return List|String
function Food:getReplaceOnCooked() end

---@public
---@return boolean
function Food:isFrozen() end

---@public
---@param arg0 float
---@return void
function Food:setRottenTime(arg0) end

---@public
---@param poisonLevelForRecipe Integer
---@return void
function Food:setPoisonLevelForRecipe(poisonLevelForRecipe) end

---@public
---@return float
function Food:getEnduranceChange() end

---@public
---@param baseHunger float
---@return void
function Food:setBaseHunger(baseHunger) end

---@public
---@return float
function Food:getFreezingTime() end

---@public
---@return void
function Food:freeze() end

---@public
---@return boolean
function Food:isPoison() end

---@public
---@return int
function Food:getSaveType() end

---@public
---@return float
function Food:getEndChange() end

---@public
---@param arg0 String
---@return void
function Food:setReplaceOnRotten(arg0) end

---@public
---@param Heat float
---@return void
function Food:setHeat(Heat) end

---@public
---@param arg0 String
---@return void
function Food:setOnEat(arg0) end

---@public
---@return int
function Food:getLastCookMinute() end

---Overrides:
---
---CanStack in class InventoryItem
---@public
---@param item InventoryItem
---@return boolean
function Food:CanStack(item) end

---@public
---@param arg0 float
---@return void
function Food:setFreezingTime(arg0) end

---@public
---@param arg0 String
---@return void
function Food:setHerbalistType(arg0) end

---@public
---@return String
function Food:getCustomEatSound() end

---@public
---@return boolean
function Food:isSpice() end

---@public
---@return String
function Food:getHerbalistType() end

---Overrides:
---
---getScore in class InventoryItem
---@public
---@param desc SurvivorDesc
---@return float
function Food:getScore(desc) end

---Overrides:
---
---getUnhappyChange in class InventoryItem
---@public
---@return float @the unhappyChange
function Food:getUnhappyChange() end

---@public
---@return boolean
function Food:isCookedInMicrowave() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function Food:load(arg0, arg1) end

---@public
---@return float
function Food:getWeight() end

---@public
---@param poisonPower int
---@return void
function Food:setPoisonPower(poisonPower) end
