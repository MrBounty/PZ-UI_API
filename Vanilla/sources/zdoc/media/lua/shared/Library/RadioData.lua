---@class RadioData : zombie.radio.RadioData
---@field PRINTDEBUG boolean
---@field private isVanilla boolean
---@field private GUID String
---@field private version int
---@field private xmlFilePath String
---@field private radioChannels ArrayList|Unknown
---@field private translationDataList ArrayList|Unknown
---@field private currentTranslation RadioTranslationData
---@field private rootNode Node
---@field private advertQue Map|Unknown|Unknown
---@field private fieldStart String
---@field private fieldEnd String
---@field private regex String
---@field private pattern Pattern
RadioData = {}

---@private
---@param arg0 String
---@return void
function RadioData:print(arg0) end

---@private
---@param arg0 String
---@return RadioData
function RadioData:ReadFile(arg0) end

---@public
---@return ArrayList|Unknown
function RadioData:fetchAllRadioData() end

---@private
---@param arg0 Node
---@param arg1 RadioScript
---@return void
function RadioData:loadExitOptions(arg0, arg1) end

---@public
---@return boolean
function RadioData:isVanilla() end

---@private
---@param arg0 String
---@return String
function RadioData:toLowerLocaleSafe(arg0) end

---@private
---@return boolean
function RadioData:loadRadioScripts() end

---@private
---@param arg0 Node
---@param arg1 String
---@return boolean
function RadioData:nodeNameIs(arg0, arg1) end

---@private
---@param arg0 Node
---@param arg1 String
---@return String
---@overload fun(arg0:Node, arg1:String, arg2:boolean)
---@overload fun(arg0:Node, arg1:String, arg2:boolean, arg3:boolean)
function RadioData:getAttrib(arg0, arg1) end

---@private
---@param arg0 Node
---@param arg1 String
---@param arg2 boolean
---@return String
function RadioData:getAttrib(arg0, arg1, arg2) end

---@private
---@param arg0 Node
---@param arg1 String
---@param arg2 boolean
---@param arg3 boolean
---@return String
function RadioData:getAttrib(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 Node
---@param arg1 ArrayList|Unknown
---@param arg2 boolean
---@return ArrayList|Unknown
function RadioData:loadScripts(arg0, arg1, arg2) end

---@public
---@param arg0 Language
---@return ArrayList|Unknown
function RadioData:getTranslatorNames(arg0) end

---@private
---@param arg0 String
---@param arg1 String
---@return String
function RadioData:checkForTranslation(arg0, arg1) end

---@private
---@param arg0 boolean
---@return ArrayList|Unknown
---@overload fun(arg0:boolean, arg1:boolean)
function RadioData:fetchRadioData(arg0) end

---@private
---@param arg0 boolean
---@param arg1 boolean
---@return ArrayList|Unknown
function RadioData:fetchRadioData(arg0, arg1) end

---@public
---@return ArrayList|Unknown
function RadioData:getRadioChannels() end

---@private
---@param arg0 String
---@param arg1 RadioLine
---@return String
function RadioData:checkForCustomAirTimer(arg0, arg1) end

---@private
---@return boolean
function RadioData:loadRootInfo() end

---@private
---@param arg0 Node
---@return ArrayList|Unknown
function RadioData:getChildNodes(arg0) end

---@private
---@param arg0 Node
---@return void
function RadioData:loadAdverts(arg0) end

---@private
---@param arg0 File
---@param arg1 String
---@param arg2 ArrayList|Unknown
---@return void
function RadioData:searchForFiles(arg0, arg1, arg2) end

---@private
---@param arg0 Node
---@return void
function RadioData:loadChannels(arg0) end

---@private
---@param arg0 Node
---@param arg1 RadioScript
---@return RadioBroadCast
function RadioData:loadBroadcast(arg0, arg1) end
