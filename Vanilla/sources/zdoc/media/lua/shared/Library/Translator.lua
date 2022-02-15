---@class Translator : zombie.core.Translator
---@field private availableLanguage ArrayList|Unknown
---@field public debug boolean
---@field private debugFile FileWriter
---@field private debugErrors boolean
---@field private debugItemEvolvedRecipeName HashSet|Unknown
---@field private debugItem HashSet|Unknown
---@field private debugMultiStageBuild HashSet|Unknown
---@field private debugRecipe HashSet|Unknown
---@field private moodles HashMap|Unknown|Unknown
---@field private ui HashMap|Unknown|Unknown
---@field private survivalGuide HashMap|Unknown|Unknown
---@field private contextMenu HashMap|Unknown|Unknown
---@field private farming HashMap|Unknown|Unknown
---@field private recipe HashMap|Unknown|Unknown
---@field private igui HashMap|Unknown|Unknown
---@field private sandbox HashMap|Unknown|Unknown
---@field private tooltip HashMap|Unknown|Unknown
---@field private challenge HashMap|Unknown|Unknown
---@field private missing HashSet|Unknown
---@field private azertyLanguages ArrayList|Unknown
---@field private news HashMap|Unknown|Unknown
---@field private stash HashMap|Unknown|Unknown
---@field private multiStageBuild HashMap|Unknown|Unknown
---@field private moveables HashMap|Unknown|Unknown
---@field private makeup HashMap|Unknown|Unknown
---@field private gameSound HashMap|Unknown|Unknown
---@field private dynamicRadio HashMap|Unknown|Unknown
---@field private items HashMap|Unknown|Unknown
---@field private itemName HashMap|Unknown|Unknown
---@field private itemEvolvedRecipeName HashMap|Unknown|Unknown
---@field private recordedMedia HashMap|Unknown|Unknown
---@field private recordedMedia_EN HashMap|Unknown|Unknown
---@field public language Language
---@field private newsHeader String
Translator = {}

---@public
---@param arg0 String
---@return String
function Translator:getTextMediaEN(arg0) end

---@public
---@param arg0 String
---@return String
function Translator:getMoveableDisplayName(arg0) end

---Return the translated text for the selected language
---
---If we don't fnid any translation for the selected language, we return the default text (in English)
---@public
---@param desc String
---@return String
---@overload fun(desc:String, arg1:Object)
---@overload fun(desc:String, arg1:Object, arg2:Object)
---@overload fun(desc:String, arg1:Object, arg2:Object, arg3:Object)
---@overload fun(desc:String, arg1:Object, arg2:Object, arg3:Object, arg4:Object)
function Translator:getText(desc) end

---@public
---@param desc String
---@param arg1 Object
---@return String
function Translator:getText(desc, arg1) end

---@public
---@param desc String
---@param arg1 Object
---@param arg2 Object
---@return String
function Translator:getText(desc, arg1, arg2) end

---@public
---@param desc String
---@param arg1 Object
---@param arg2 Object
---@param arg3 Object
---@return String
function Translator:getText(desc, arg1, arg2, arg3) end

---@public
---@param desc String
---@param arg1 Object
---@param arg2 Object
---@param arg3 Object
---@param arg4 Object
---@return String
function Translator:getText(desc, arg1, arg2, arg3, arg4) end

---@private
---@param arg0 String
---@param arg1 ArrayList|Unknown
---@return void
function Translator:addNewsLine(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 String
---@return void
function Translator:setDefaultItemEvolvedRecipeName(arg0, arg1) end

---@public
---@param desc String
---@return String
---@overload fun(arg0:String, arg1:Object)
---@overload fun(arg0:String, arg1:Object, arg2:Object)
---@overload fun(arg0:String, arg1:Object, arg2:Object, arg3:Object)
---@overload fun(arg0:String, arg1:Object, arg2:Object, arg3:Object, arg4:Object)
function Translator:getTextOrNull(desc) end

---@public
---@param arg0 String
---@param arg1 Object
---@return String
function Translator:getTextOrNull(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 Object
---@param arg2 Object
---@return String
function Translator:getTextOrNull(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 Object
---@param arg2 Object
---@param arg3 Object
---@return String
function Translator:getTextOrNull(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@param arg1 Object
---@param arg2 Object
---@param arg3 Object
---@param arg4 Object
---@return String
function Translator:getTextOrNull(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param languageId int
---@return void
---@overload fun(newlanguage:Language)
function Translator:setLanguage(languageId) end

---@public
---@param newlanguage Language
---@return void
function Translator:setLanguage(newlanguage) end

---@public
---@return void
function Translator:debugItemEvolvedRecipeNames() end

---@public
---@return ArrayList|Unknown
function Translator:getAzertyMap() end

---@public
---@param arg0 String
---@return String
function Translator:getItemEvolvedRecipeName(arg0) end

---@public
---@return void
function Translator:debugRecipeNames() end

---@public
---@param arg0 String
---@return String
function Translator:getItemNameFromFullType(arg0) end

---@private
---@param arg0 String
---@return void
function Translator:debugwrite(arg0) end

---@public
---@param name String
---@return String
function Translator:getRecipeName(name) end

---@public
---@param arg0 String
---@return String
function Translator:getMultiStageBuild(arg0) end

---@public
---@param arg0 Language
---@param arg1 ArrayList|Unknown
---@return void
function Translator:addLanguageToList(arg0, arg1) end

---@public
---@return ArrayList|Unknown
function Translator:getNewsVersions() end

---@private
---@param arg0 HashMap|Unknown|Unknown
---@return void
function Translator:fillNewsFromFile(arg0) end

---@private
---@param arg0 File
---@param arg1 HashMap|Unknown|Unknown
---@param arg2 Language
---@return void
function Translator:doNews(arg0, arg1, arg2) end

---@private
---@param arg0 String
---@param arg1 String
---@param arg2 HashMap|Unknown|Unknown
---@param arg3 Language
---@return void
function Translator:tryFillMapFromFile(arg0, arg1, arg2, arg3) end

---@public
---@return String
function Translator:getCharset() end

---@private
---@param arg0 String
---@param arg1 HashMap|Unknown|Unknown
---@return void
function Translator:fillMapFromFile(arg0, arg1) end

---@private
---@param arg0 String
---@param arg1 HashMap|Unknown|Unknown
---@param arg2 Language
---@return void
function Translator:tryFillMapFromMods(arg0, arg1, arg2) end

---@public
---@param trim String
---@return String
function Translator:getDisplayItemName(trim) end

---@private
---@param arg0 String
---@return String
function Translator:getDefaultText(arg0) end

---@public
---@return ArrayList|Language
function Translator:getAvailableLanguage() end

---@private
---@param arg0 String
---@param arg1 boolean
---@return String
function Translator:getTextInternal(arg0, arg1) end

---@public
---@return void
function Translator:debugItemNames() end

---@public
---@return void
function Translator:debugMultiStageBuildNames() end

---@private
---@param arg0 String
---@return int
function Translator:countSubstitutions(arg0) end

---@public
---@return void
function Translator:loadFiles() end

---@public
---@return Language
function Translator:getLanguage() end

---@private
---@param arg0 File
---@param arg1 HashMap|Unknown|Unknown
---@param arg2 Language
---@return void
function Translator:parseFile(arg0, arg1, arg2) end

---@private
---@param arg0 String
---@param arg1 String
---@param arg2 Object
---@return String
function Translator:subst(arg0, arg1, arg2) end

---@private
---@param arg0 String
---@param arg1 HashMap|Unknown|Unknown
---@param arg2 HashMap|Unknown|Unknown
---@param arg3 Language
---@return void
function Translator:tryFillNewsFromFile(arg0, arg1, arg2, arg3) end

---@public
---@return Language
function Translator:getDefaultLanguage() end

---@private
---@param arg0 String
---@return String
function Translator:changeSomeStuff(arg0) end

---@public
---@param arg0 String
---@return String
function Translator:getRadioText(arg0) end
