---@class BooleanConfigOption : zombie.config.BooleanConfigOption
---@field protected value boolean
---@field protected defaultValue boolean
BooleanConfigOption = {}

---@public
---@return void
function BooleanConfigOption:resetToDefault() end

---@public
---@return boolean
function BooleanConfigOption:getDefaultValue() end

---@public
---@param arg0 String
---@return boolean
function BooleanConfigOption:isValidString(arg0) end

---@public
---@return String
function BooleanConfigOption:getValueAsString() end

---@public
---@return Object
function BooleanConfigOption:getValueAsObject() end

---@public
---@param arg0 String
---@return void
function BooleanConfigOption:parse(arg0) end

---@public
---@param arg0 Object
---@return void
function BooleanConfigOption:setValueFromObject(arg0) end

---@public
---@return void
function BooleanConfigOption:setDefaultToCurrentValue() end

---@public
---@return String
function BooleanConfigOption:getType() end

---@public
---@param arg0 boolean
---@return void
function BooleanConfigOption:setValue(arg0) end

---@public
---@return boolean
function BooleanConfigOption:getValue() end
