---@class ProfessionFactory.Profession : zombie.characters.professions.ProfessionFactory.Profession
---@field public type String
---@field public name String
---@field public cost int
---@field public description String
---@field public IconPath String
---@field public texture Texture
---@field public FreeTraitStack Stack|String
---@field private freeRecipes List|Unknown
---@field public XPBoostMap HashMap|PerkFactory.Perks|Integer
ProfessionFactory_Profession = {}

---Specified by:
---
---getRightLabel in interface IListBoxItem
---@public
---@return String
function ProfessionFactory_Profession:getRightLabel() end

---@public
---@param name String @the name to set
---@return void
function ProfessionFactory_Profession:setName(name) end

---@public
---@return HashMap|PerkFactory.Perks|Integer
function ProfessionFactory_Profession:getXPBoostMap() end

---@public
---@return String @the name
function ProfessionFactory_Profession:getName() end

---@public
---@return List|String
function ProfessionFactory_Profession:getFreeRecipes() end

---@public
---@param FreeTraitStack Stack|String @the FreeTraitStack to set
---@return void
function ProfessionFactory_Profession:setFreeTraitStack(FreeTraitStack) end

---@public
---@return Stack|String @the FreeTraitStack
function ProfessionFactory_Profession:getFreeTraitStack() end

---@public
---@param type String @the type to set
---@return void
function ProfessionFactory_Profession:setType(type) end

---@public
---@return int @the cost
function ProfessionFactory_Profession:getCost() end

---@public
---@param freeRecipes List|String
---@return void
function ProfessionFactory_Profession:setFreeRecipes(freeRecipes) end

---@public
---@return ArrayList|String
function ProfessionFactory_Profession:getFreeTraits() end

---@public
---@return String
function ProfessionFactory_Profession:getIconPath() end

---@public
---@param IconPath String @the IconPath to set
---@return void
function ProfessionFactory_Profession:setIconPath(IconPath) end

---@public
---@return String @the description
function ProfessionFactory_Profession:getDescription() end

---@public
---@param arg0 PerkFactory.Perk
---@param arg1 int
---@return void
function ProfessionFactory_Profession:addXPBoost(arg0, arg1) end

---@public
---@return String @the type
function ProfessionFactory_Profession:getType() end

---@public
---@param trait String
---@return void
function ProfessionFactory_Profession:addFreeTrait(trait) end

---@public
---@return Texture
function ProfessionFactory_Profession:getTexture() end

---Specified by:
---
---getLeftLabel in interface IListBoxItem
---@public
---@return String
function ProfessionFactory_Profession:getLeftLabel() end

---Specified by:
---
---getLabel in interface IListBoxItem
---@public
---@return String
function ProfessionFactory_Profession:getLabel() end

---@public
---@param cost int @the cost to set
---@return void
function ProfessionFactory_Profession:setCost(cost) end

---@public
---@param description String @the description to set
---@return void
function ProfessionFactory_Profession:setDescription(description) end
