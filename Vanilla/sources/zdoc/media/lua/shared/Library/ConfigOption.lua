---@class ConfigOption : zombie.config.ConfigOption
---@field protected name String
ConfigOption = {}

---@public
---@return String
function ConfigOption:getValueAsLuaString() end

---@public
---@param arg0 String
---@return boolean
function ConfigOption:isValidString(arg0) end

---@public
---@return String
function ConfigOption:getValueAsString() end

---@public
---@return String
function ConfigOption:getType() end

---@public
---@param arg0 Object
---@return void
function ConfigOption:setValueFromObject(arg0) end

---@public
---@return String
function ConfigOption:getName() end

---@public
---@return Object
function ConfigOption:getValueAsObject() end

---@public
---@return void
function ConfigOption:resetToDefault() end

---@public
---@param arg0 String
---@return void
function ConfigOption:parse(arg0) end

---@public
---@return void
function ConfigOption:setDefaultToCurrentValue() end
