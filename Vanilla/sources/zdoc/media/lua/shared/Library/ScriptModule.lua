---@class ScriptModule : zombie.scripting.objects.ScriptModule
---@field public name String
---@field public value String
---@field public ItemMap HashMap|String|Item
---@field public GameSoundMap HashMap|Unknown|Unknown
---@field public GameSoundList ArrayList|Unknown
---@field public ModelScriptMap TreeMap|Unknown|Unknown
---@field public RuntimeAnimationScriptMap HashMap|Unknown|Unknown
---@field public SoundTimelineMap HashMap|Unknown|Unknown
---@field public VehicleMap HashMap|Unknown|Unknown
---@field public VehicleTemplateMap HashMap|Unknown|Unknown
---@field public VehicleEngineRPMMap HashMap|Unknown|Unknown
---@field public RecipeMap ArrayList|JRecipe
---@field public RecipeByName HashMap|Unknown|Unknown
---@field public RecipesWithDotInName HashMap|Unknown|Unknown
---@field public EvolvedRecipeMap ArrayList|EvolvedRecipe
---@field public UniqueRecipeMap ArrayList|UniqueRecipe
---@field public FixingMap HashMap|Unknown|Unknown
---@field public Imports ArrayList|Unknown
---@field public disabled boolean
ScriptModule = {}

---@public
---@param arg0 String
---@return VehicleEngineRPM
function ScriptModule:getVehicleEngineRPM(arg0) end

---@public
---@param name String
---@param strArray String
---@return void
function ScriptModule:Load(name, strArray) end

---@public
---@return String
function ScriptModule:getName() end

---Specified by:
---
---getRecipe in interface IScriptObjectStore
---@public
---@param name String
---@return JRecipe
function ScriptModule:getRecipe(name) end

---@public
---@param arg0 String
---@return VehicleScript
function ScriptModule:getVehicle(arg0) end

---@public
---@return boolean
function ScriptModule:CheckExitPoints() end

---Specified by:
---
---getItem in interface IScriptObjectStore
---@public
---@param name String
---@return Item
function ScriptModule:getItem(name) end

---@public
---@param totalFile String
---@return void
function ScriptModule:ParseScript(totalFile) end

---@public
---@param totalFile String
---@return void
function ScriptModule:ParseScriptPP(totalFile) end

---@public
---@param arg0 String
---@return VehicleTemplate
function ScriptModule:getVehicleTemplate(arg0) end

---@private
---@param arg0 String
---@return String
function ScriptModule:GetTokenType(arg0) end

---@public
---@return void
function ScriptModule:Reset() end

---@private
---@param arg0 String
---@return void
function ScriptModule:CreateFromTokenPP(arg0) end

---@private
---@param arg0 String
---@return void
function ScriptModule:CreateFromToken(arg0) end
