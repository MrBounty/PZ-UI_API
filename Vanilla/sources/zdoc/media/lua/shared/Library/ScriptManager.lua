---@class ScriptManager : zombie.scripting.ScriptManager
---@field public instance ScriptManager
---@field public currentFileName String
---@field public scriptsWithVehicles ArrayList|Unknown
---@field public scriptsWithVehicleTemplates ArrayList|Unknown
---@field public ModuleMap HashMap|String|ScriptModule
---@field public ModuleList ArrayList|Unknown
---@field private FullTypeToItemMap HashMap|Unknown|Unknown
---@field private SoundTimelineMap HashMap|Unknown|Unknown
---@field public CurrentLoadingModule ScriptModule
---@field public ModuleAliases HashMap|String|String
---@field private buf StringBuilder
---@field private CachedModules HashMap|Unknown|Unknown
---@field private recipesTempList ArrayList|Unknown
---@field private evolvedRecipesTempList Stack|Unknown
---@field private uniqueRecipesTempList Stack|Unknown
---@field private itemTempList ArrayList|Unknown
---@field private tagToItemMap HashMap|Unknown|Unknown
---@field private modelScriptTempList ArrayList|Unknown
---@field private vehicleScriptTempList ArrayList|Unknown
---@field private clothingToItemMap HashMap|Unknown|Unknown
---@field private visualDamagesList ArrayList|Unknown
---@field private Base String
---@field private checksum String
---@field private tempFileToModMap HashMap|Unknown|Unknown
---@field private currentLoadFileMod String
---@field private currentLoadFileAbsPath String
---@field public VanillaID String
ScriptManager = {}

---@public
---@return ArrayList|Unknown
function ScriptManager:getAllModelScripts() end

---@public
---@return Stack|UniqueRecipe
function ScriptManager:getAllUniqueRecipes() end

---@private
---@return void
function ScriptManager:createClothingItemMap() end

---@public
---@return void
function ScriptManager:Reset() end

---@public
---@param arg0 String
---@return VehicleScript
function ScriptManager:getVehicle(arg0) end

---@public
---@param arg0 URI
---@param arg1 File
---@param arg2 ArrayList|Unknown
---@return void
function ScriptManager:searchFolders(arg0, arg1, arg2) end

---@public
---@return String
function ScriptManager:getCurrentLoadFileAbsPath() end

---@public
---@param arg0 ScriptModule
---@param arg1 String
---@return String
function ScriptManager:resolveItemType(arg0, arg1) end

---@private
---@param arg0 String
---@return void
function ScriptManager:CreateFromToken(arg0) end

---@public
---@return ArrayList|Unknown
function ScriptManager:getZedDmgMap() end

---@public
---@param totalFile String
---@return void
function ScriptManager:ParseScript(totalFile) end

---@public
---@return void
function ScriptManager:CheckExitPoints() end

---@public
---@param arg0 String
---@return ModelScript
function ScriptManager:getModelScript(arg0) end

---@public
---@return void
function ScriptManager:Load() end

---@public
---@return ArrayList|Unknown
function ScriptManager:getAllVehicleScripts() end

---@public
---@return ArrayList|Unknown
function ScriptManager:getAllGameSounds() end

---@public
---@return void
function ScriptManager:update() end

---Specified by:
---
---getItem in interface IScriptObjectStore
---@public
---@param name String
---@return Item
function ScriptManager:getItem(name) end

---@public
---@return ArrayList|Unknown
function ScriptManager:getAllRecipes() end

---@public
---@param name String
---@return String
function ScriptManager:getItemName(name) end

---@private
---@return void
function ScriptManager:resolveItemTypes() end

---@private
---@return void
function ScriptManager:debugItems() end

---@public
---@param arg0 String
---@return SoundTimelineScript
function ScriptManager:getSoundTimeline(arg0) end

---Specified by:
---
---getRecipe in interface IScriptObjectStore
---@public
---@param name String
---@return JRecipe
function ScriptManager:getRecipe(name) end

---throws java.io.FileNotFoundException
---@public
---@param filename String
---@param bLoadJar boolean
---@return void
function ScriptManager:LoadFile(filename, bLoadJar) end

---@public
---@param name String
---@return Item
function ScriptManager:FindItem(name) end

---@public
---@return ArrayList|Unknown
function ScriptManager:getAllItems() end

---@public
---@param arg0 String
---@return VehicleEngineRPM
function ScriptManager:getVehicleEngineRPM(arg0) end

---@public
---@param arg0 String
---@return ArrayList|Unknown
function ScriptManager:getItemsTag(arg0) end

---@public
---@param arg0 List|Unknown
---@return List|Unknown
function ScriptManager:getAllFixing(arg0) end

---@public
---@param arg0 String
---@return boolean
function ScriptManager:isDrainableItemType(arg0) end

---@private
---@return void
function ScriptManager:createZedDmgMap() end

---@public
---@return ArrayList|Unknown
function ScriptManager:getAllRuntimeAnimationScripts() end

---@public
---@return Stack|EvolvedRecipe
function ScriptManager:getAllEvolvedRecipes() end

---@public
---@param arg0 String
---@return VehicleTemplate
function ScriptManager:getVehicleTemplate(arg0) end

---@public
---@param arg0 String
---@return ArrayList|Unknown
function ScriptManager:getAllRecipesFor(arg0) end

---@public
---@return String
function ScriptManager:getCurrentLoadFileMod() end

---@public
---@param name String
---@return ScriptModule
function ScriptManager:getModuleNoDisableCheck(name) end

---@public
---@return String
function ScriptManager:getChecksum() end

---@public
---@param arg0 String
---@return String
function ScriptManager:getItemTypeForClothingItem(arg0) end

---@public
---@param name String
---@return ScriptModule
function ScriptManager:getModule(name) end

---@public
---@param arg0 String
---@return Item
function ScriptManager:getItemForClothingItem(arg0) end
