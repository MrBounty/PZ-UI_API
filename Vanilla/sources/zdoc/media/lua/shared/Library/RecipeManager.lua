---@class RecipeManager : zombie.inventory.RecipeManager
---@field private RecipeList ArrayList|Unknown
RecipeManager = {}

---@private
---@param arg0 JRecipe
---@param arg1 String
---@param arg2 Set|Unknown
---@param arg3 String
---@return Item
function RecipeManager:resolveItemModuleDotType(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@return JRecipe
function RecipeManager:getDismantleRecipeFor(arg0) end

---@public
---@param itemToUse String
---@param recipe JRecipe
---@param chr IsoGameCharacter
---@return float
function RecipeManager:UseAmount(itemToUse, recipe, chr) end

---@public
---@return void
---@overload fun(arg0:JRecipe, arg1:ArrayList|Unknown)
---@overload fun(arg0:Recipe.Source, arg1:ArrayList|Unknown)
---@overload fun(arg0:JRecipe, arg1:String, arg2:String)
function RecipeManager:LoadedAfterLua() end

---@private
---@param arg0 JRecipe
---@param arg1 ArrayList|Unknown
---@return void
function RecipeManager:LoadedAfterLua(arg0, arg1) end

---@private
---@param arg0 Recipe.Source
---@param arg1 ArrayList|Unknown
---@return void
function RecipeManager:LoadedAfterLua(arg0, arg1) end

---@private
---@param arg0 JRecipe
---@param arg1 String
---@param arg2 String
---@return void
function RecipeManager:LoadedAfterLua(arg0, arg1, arg2) end

---@public
---@param arg0 InventoryItem
---@param arg1 IsoGameCharacter
---@param arg2 ArrayList|Unknown
---@param arg3 boolean
---@return ArrayList|Unknown
function RecipeManager:getEvolvedRecipe(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 JRecipe
---@param arg1 IsoGameCharacter
---@param arg2 InventoryItem
---@param arg3 ArrayList|Unknown
---@return boolean
function RecipeManager:HasAllRequiredItems(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 InventoryItem
---@return int
function RecipeManager:AvailableUses(arg0) end

---@private
---@return void
function RecipeManager:DebugPrintAllRecipes() end

---@public
---@param itemToUse String
---@param recipe JRecipe
---@return boolean
function RecipeManager:IsItemDestroyed(itemToUse, recipe) end

---@private
---@param arg0 JRecipe
---@param arg1 InventoryItem
---@return boolean
function RecipeManager:RecipeContainsItem(arg0, arg1) end

---@public
---@param arg0 JRecipe
---@param arg1 int
---@param arg2 IsoGameCharacter
---@param arg3 ArrayList|Unknown
---@param arg4 InventoryItem
---@param arg5 ArrayList|Unknown
---@return ArrayList|Unknown
function RecipeManager:getSourceItemsAll(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 JRecipe
---@param arg1 InventoryItem
---@param arg2 IsoGameCharacter
---@param arg3 ArrayList|Unknown
---@return InventoryItem
function RecipeManager:PerformMakeItem(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 JRecipe
---@param arg1 IsoGameCharacter
---@param arg2 ArrayList|Unknown
---@param arg3 InventoryItem
---@param arg4 ArrayList|Unknown
---@return ArrayList|Unknown
function RecipeManager:getAvailableItemsNeeded(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 IsoGameCharacter
---@return int
function RecipeManager:getKnownRecipesNumber(arg0) end

---@private
---@param arg0 JRecipe
---@param arg1 IsoGameCharacter
---@param arg2 InventoryItem
---@return boolean
function RecipeManager:CanPerform(arg0, arg1, arg2) end

---@public
---@param arg0 boolean
---@param arg1 JRecipe
---@param arg2 InventoryItem
---@param arg3 IsoGameCharacter
---@param arg4 ArrayList|Unknown
---@return InventoryItem
function RecipeManager:GetMovableRecipeTool(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param itemToUse String
---@param itemToMake String
---@return boolean
function RecipeManager:DoesWipeUseDelta(itemToUse, itemToMake) end

---@public
---@param arg0 JRecipe
---@param arg1 int
---@param arg2 IsoGameCharacter
---@param arg3 ArrayList|Unknown
---@param arg4 InventoryItem
---@param arg5 ArrayList|Unknown
---@return ArrayList|Unknown
function RecipeManager:getSourceItemsNeeded(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@return ArrayList|Unknown
function RecipeManager:getAllEvolvedRecipes() end

---@private
---@param arg0 JRecipe
---@param arg1 IsoGameCharacter
---@return boolean
function RecipeManager:isNearItem(arg0, arg1) end

---@public
---@param arg0 JRecipe
---@param arg1 IsoGameCharacter
---@param arg2 InventoryItem
---@param arg3 ArrayList|Unknown
---@return boolean
function RecipeManager:IsRecipeValid(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 JRecipe
---@param arg1 IsoGameCharacter
---@param arg2 ArrayList|Unknown
---@param arg3 InventoryItem
---@return int
function RecipeManager:getNumberOfTimesRecipeCanBeDone(arg0, arg1, arg2, arg3) end

---@public
---@param itemToUse String
---@param recipe JRecipe
---@return boolean
function RecipeManager:DoesUseItemUp(itemToUse, recipe) end

---@private
---@param arg0 JRecipe
---@param arg1 IsoGameCharacter
---@return boolean
function RecipeManager:HasRequiredSkill(arg0, arg1) end

---@private
---@param arg0 InventoryItem
---@param arg1 float
---@param arg2 IsoGameCharacter
---@return boolean
function RecipeManager:ReduceUses(arg0, arg1, arg2) end

---@private
---@param arg0 JRecipe
---@param arg1 IsoGameCharacter
---@param arg2 ArrayList|Unknown
---@param arg3 InventoryItem
---@param arg4 ArrayList|Unknown
---@param arg5 boolean
---@return RecipeManager.SourceItems
function RecipeManager:getAvailableItems(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 JRecipe
---@param arg1 IsoGameCharacter
---@param arg2 ArrayList|Unknown
---@param arg3 InventoryItem
---@param arg4 ArrayList|Unknown
---@return ArrayList|Unknown
function RecipeManager:getAvailableItemsAll(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 JRecipe
---@param arg1 InventoryItem
---@param arg2 ArrayList|Unknown
---@param arg3 IsoGameCharacter
---@return boolean
function RecipeManager:hasHeat(arg0, arg1, arg2, arg3) end

---@public
---@return void
function RecipeManager:Loaded() end

---@public
---@param arg0 InventoryItem
---@param arg1 IsoGameCharacter
---@param arg2 ArrayList|Unknown
---@return ArrayList|Unknown
function RecipeManager:getUniqueRecipeItems(arg0, arg1, arg2) end

---@private
---@param arg0 JRecipe
---@param arg1 ArrayList|Unknown
---@param arg2 InventoryItem
---@param arg3 IsoGameCharacter
---@return void
function RecipeManager:GivePlayerExperience(arg0, arg1, arg2, arg3) end
