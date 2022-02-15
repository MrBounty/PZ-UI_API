---@class StringConfigOption : zombie.config.StringConfigOption
---@field protected value String
---@field protected defaultValue String
StringConfigOption = {}

---@public
---@param arg0 String
---@return boolean
function StringConfigOption:isValidString(arg0) end

---@public
---@return String
function StringConfigOption:getValueAsString() end

---@public
---@param arg0 String
---@return void
function StringConfigOption:setValue(arg0) end

---@public
---@param arg0 String
---@return void
function StringConfigOption:parse(arg0) end

---@public
---@return void
function StringConfigOption:resetToDefault() end

---@public
---@return String
function StringConfigOption:getValue() end

---@public
---@param arg0 Object
---@return void
function StringConfigOption:setValueFromObject(arg0) end

---@public
---@return String
function StringConfigOption:getType() end

---@public
---@return void
function StringConfigOption:setDefaultToCurrentValue() end

---@public
---@return Object
function StringConfigOption:getValueAsObject() end

---@public
---@return String
function StringConfigOption:getDefaultValue() end

---@public
---@return String
function StringConfigOption:getValueAsLuaString() end
