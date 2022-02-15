---@class MultiStageBuilding.Stage : zombie.iso.MultiStageBuilding.Stage
---@field public name String
---@field public previousStage ArrayList|Unknown
---@field public recipeName String
---@field public sprite String
---@field public northSprite String
---@field public timeNeeded int
---@field public bonusHealth int
---@field public bonusHealthSkill boolean
---@field public xp HashMap|Unknown|Unknown
---@field public perks HashMap|Unknown|Unknown
---@field public items HashMap|Unknown|Unknown
---@field public itemsToKeep ArrayList|Unknown
---@field public knownRecipe String
---@field public thumpSound String
---@field public wallType String
---@field public canBePlastered boolean
---@field public craftingSound String
---@field public completionSound String
---@field public ID String
---@field public canBarricade boolean
MultiStageBuilding_Stage = {}

---@public
---@return String
function MultiStageBuilding_Stage:getThumpSound() end

---@public
---@return String
function MultiStageBuilding_Stage:getRecipeName() end

---@public
---@param arg0 String
---@param arg1 String[]
---@return void
function MultiStageBuilding_Stage:Load(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 IsoObject
---@param arg2 boolean
---@return boolean
function MultiStageBuilding_Stage:canBeDone(arg0, arg1, arg2) end

---@public
---@return ArrayList|Unknown
function MultiStageBuilding_Stage:getItemsToKeep() end

---@public
---@return String
function MultiStageBuilding_Stage:getSprite() end

---@public
---@return ArrayList|Unknown
function MultiStageBuilding_Stage:getPreviousStages() end

---@public
---@return String
function MultiStageBuilding_Stage:getKnownRecipe() end

---@public
---@return String
function MultiStageBuilding_Stage:getCraftingSound() end

---@public
---@return String
function MultiStageBuilding_Stage:getNorthSprite() end

---@public
---@param arg0 IsoGameCharacter
---@return int
function MultiStageBuilding_Stage:getTimeNeeded(arg0) end

---@public
---@return String
function MultiStageBuilding_Stage:getDisplayName() end

---@public
---@return KahluaTable
function MultiStageBuilding_Stage:getItemsLua() end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 IsoThumpable
---@param arg2 boolean
---@return void
function MultiStageBuilding_Stage:doStage(arg0, arg1, arg2) end

---@public
---@return KahluaTable
function MultiStageBuilding_Stage:getPerksLua() end

---@public
---@return String
function MultiStageBuilding_Stage:getName() end
