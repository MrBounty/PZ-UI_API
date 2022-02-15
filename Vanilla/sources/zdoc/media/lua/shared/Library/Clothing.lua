---@class Clothing : zombie.inventory.types.Clothing
---@field private temperature float
---@field private insulation float
---@field private windresistance float
---@field private waterResistance float
---@field patches HashMap|Unknown|Unknown
---@field protected SpriteName String
---@field protected palette String
---@field public bloodLevel float
---@field private dirtyness float
---@field private wetness float
---@field private WeightWet float
---@field private lastWetnessUpdate float
---@field private dirtyString String
---@field private bloodyString String
---@field private wetString String
---@field private soakedString String
---@field private wornString String
---@field private ConditionLowerChance int
---@field private stompPower float
---@field private runSpeedModifier float
---@field private combatSpeedModifier float
---@field private removeOnBroken Boolean
---@field private canHaveHoles Boolean
---@field private biteDefense float
---@field private scratchDefense float
---@field private bulletDefense float
---@field public CONDITION_PER_HOLES int
---@field private neckProtectionModifier float
---@field private chanceToFall int
Clothing = {}

---@public
---@param arg0 float
---@return void
function Clothing:setCombatSpeedModifier(arg0) end

---@public
---@param arg0 float
---@return void
function Clothing:setWindresistance(arg0) end

---@public
---@return String @the palette
function Clothing:getPalette() end

---@public
---@param arg0 int
---@return void
function Clothing:setCondition(arg0) end

---@public
---@param arg0 BloodBodyPartType
---@return Clothing.ClothingPatch
function Clothing:getPatchType(arg0) end

---@public
---@return float
function Clothing:getBloodLevel() end

---Overrides:
---
---Use in class InventoryItem
---@public
---@param bCrafting boolean
---@param bInContainer boolean
---@return void
function Clothing:Use(bCrafting, bInContainer) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 InventoryItem
---@return int
function Clothing:getBiteDefenseFromItem(arg0, arg1) end

---@public
---@return void
---@overload fun(arg0:boolean)
function Clothing:updateWetness() end

---@public
---@param arg0 boolean
---@return void
function Clothing:updateWetness(arg0) end

---@public
---@return int
function Clothing:getChanceToFall() end

---@public
---@return int
function Clothing:getNbrOfCoveredParts() end

---@public
---@param arg0 Boolean
---@return void
function Clothing:setRemoveOnBroken(arg0) end

---@public
---@return float
function Clothing:getBulletDefense() end

---@public
---@return int
function Clothing:getConditionLowerChance() end

---@public
---@param arg0 float
---@return void
function Clothing:setBloodLevel(arg0) end

---@public
---@param arg0 int
---@return void
function Clothing:setChanceToFall(arg0) end

---@public
---@return float
function Clothing:getNeckProtectionModifier() end

---@public
---@return int
function Clothing:getCondLossPerHole() end

---@public
---@param arg0 Boolean
---@return void
function Clothing:setCanHaveHoles(arg0) end

---@public
---@return int
function Clothing:getPatchesNumber() end

---@public
---@param arg0 BloodBodyPartType
---@return float
function Clothing:getBloodLevelForPart(arg0) end

---@public
---@return String
function Clothing:getClothingExtraSubmenu() end

---@public
---@return String
function Clothing:getName() end

---Overrides:
---
---CanStack in class InventoryItem
---@public
---@param item InventoryItem
---@return boolean
function Clothing:CanStack(item) end

---@public
---@param arg0 float
---@return void
function Clothing:setScratchDefense(arg0) end

---@public
---@param arg0 float
---@return void
function Clothing:setStompPower(arg0) end

---@public
---@return boolean
function Clothing:canBe3DRender() end

---@public
---@param arg0 float
---@return void
function Clothing:setBiteDefense(arg0) end

---@public
---@param arg0 float
---@return void
function Clothing:setRunSpeedModifier(arg0) end

---@public
---@return float
function Clothing:getScratchDefense() end

---@public
---@param arg0 float
---@return void
function Clothing:setNeckProtectionModifier(arg0) end

---@public
---@return void
function Clothing:flushWetness() end

---@public
---@param arg0 BloodBodyPartType
---@param arg1 boolean
---@param arg2 boolean
---@return float
function Clothing:getDefForPart(arg0, arg1, arg2) end

---@public
---@return boolean
function Clothing:IsClothing() end

---@public
---@return float
function Clothing:getStompPower() end

---@public
---@return float
function Clothing:getInsulation() end

---@public
---@param arg0 Clothing
---@return void
function Clothing:copyPatchesTo(arg0) end

---@public
---@return void
function Clothing:update() end

---@public
---@return boolean
function Clothing:finishupdate() end

---@public
---@return float
function Clothing:getWaterResistance() end

---@public
---@param temperature float
---@return void
function Clothing:setTemperature(temperature) end

---throws java.io.IOException
---
---Overrides:
---
---save in class InventoryItem
---@public
---@param output ByteBuffer
---@param net boolean
---@return void
function Clothing:save(output, net) end

---@public
---@return float
function Clothing:getBloodlevel() end

---@public
---@param arg0 float
---@return void
function Clothing:setInsulation(arg0) end

---@public
---@return boolean
function Clothing:isDirty() end

---@public
---@return float
function Clothing:getDirtyness() end

---@public
---@param arg0 float
---@return void
function Clothing:setWetness(arg0) end

---@public
---@return float
function Clothing:getWindresistance() end

---Overrides:
---
---getCategory in class InventoryItem
---@public
---@return String
function Clothing:getCategory() end

---@public
---@return float
function Clothing:getWeight() end

---@public
---@return Boolean
function Clothing:isRemoveOnBroken() end

---@public
---@param SpriteName String @the SpriteName to set
---@return void
function Clothing:setSpriteName(SpriteName) end

---@public
---@return boolean
function Clothing:isBloody() end

---@public
---@return String @the SpriteName
function Clothing:getSpriteName() end

---@public
---@return int
function Clothing:getSaveType() end

---@public
---@param palette String @the palette to set
---@return void
function Clothing:setPalette(palette) end

---@public
---@param arg0 float
---@return void
function Clothing:setBulletDefense(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 BloodBodyPartType
---@param arg2 InventoryItem
---@return boolean
function Clothing:canFullyRestore(arg0, arg1, arg2) end

---@public
---@return float
function Clothing:getWetness() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function Clothing:load(arg0, arg1) end

---@public
---@return int
function Clothing:getHolesNumber() end

---@public
---@param arg0 ObjectTooltip
---@param arg1 ObjectTooltip.Layout
---@return void
function Clothing:DoTooltip(arg0, arg1) end

---@public
---@return float
function Clothing:getRunSpeedModifier() end

---@public
---@return ArrayList|Unknown
function Clothing:getCoveredParts() end

---@public
---@param arg0 int
---@return void
function Clothing:setConditionLowerChance(arg0) end

---@public
---@param arg0 float
---@return void
function Clothing:setWaterResistance(arg0) end

---@public
---@return float
function Clothing:getCombatSpeedModifier() end

---@public
---@param arg0 float
---@return void
function Clothing:setWeightWet(arg0) end

---@public
---@return float
function Clothing:getWeightWet() end

---@public
---@return void
function Clothing:Unwear() end

---@public
---@param arg0 BloodBodyPartType
---@return void
function Clothing:removePatch(arg0) end

---@public
---@return String
function Clothing:toString() end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 InventoryItem
---@return int
function Clothing:getScratchDefenseFromItem(arg0, arg1) end

---@public
---@return float
function Clothing:getBiteDefense() end

---@public
---@param arg0 BloodBodyPartType
---@return float
function Clothing:getBloodlevelForPart(arg0) end

---@private
---@return Clothing.WetDryState
function Clothing:getWetDryState() end

---@public
---@return float
function Clothing:getClothingDirtynessIncreaseLevel() end

---@public
---@return Boolean
function Clothing:getCanHaveHoles() end

---@public
---@param Sprite String
---@return Clothing
function Clothing:CreateFromSprite(Sprite) end

---@public
---@return boolean
function Clothing:isCosmetic() end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 BloodBodyPartType
---@param arg2 InventoryItem
---@return void
function Clothing:addPatch(arg0, arg1, arg2) end

---@public
---@return float
function Clothing:getTemperature() end

---@public
---@param arg0 float
---@return void
function Clothing:setDirtyness(arg0) end
