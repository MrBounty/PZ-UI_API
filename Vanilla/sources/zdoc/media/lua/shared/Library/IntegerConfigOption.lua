---@class IntegerConfigOption : zombie.config.IntegerConfigOption
---@field protected value int
---@field protected defaultValue int
---@field protected min int
---@field protected max int
IntegerConfigOption = {}

---@public
---@return int
function IntegerConfigOption:getValue() end

---@public
---@return String
function IntegerConfigOption:getValueAsString() end

---@public
---@return double
function IntegerConfigOption:getMax() end

---@public
---@return String
function IntegerConfigOption:getType() end

---@public
---@param arg0 String
---@return void
function IntegerConfigOption:parse(arg0) end

---@public
---@param arg0 int
---@return void
function IntegerConfigOption:setValue(arg0) end

---@public
---@return void
function IntegerConfigOption:setDefaultToCurrentValue() end

---@public
---@return void
function IntegerConfigOption:resetToDefault() end

---@public
---@return Object
function IntegerConfigOption:getValueAsObject() end

---@public
---@param arg0 Object
---@return void
function IntegerConfigOption:setValueFromObject(arg0) end

---@public
---@param arg0 String
---@return boolean
function IntegerConfigOption:isValidString(arg0) end

---@public
---@return int
function IntegerConfigOption:getDefaultValue() end

---@public
---@return double
function IntegerConfigOption:getMin() end
