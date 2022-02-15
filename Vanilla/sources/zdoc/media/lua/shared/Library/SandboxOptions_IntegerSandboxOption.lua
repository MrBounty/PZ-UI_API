---@class SandboxOptions.IntegerSandboxOption : zombie.SandboxOptions.IntegerSandboxOption
---@field protected translation String
---@field protected tableName String
---@field protected shortName String
---@field protected bCustom boolean
---@field protected pageName String
SandboxOptions_IntegerSandboxOption = {}

---@public
---@param arg0 String
---@return SandboxOptions.SandboxOption
function SandboxOptions_IntegerSandboxOption:setPageName(arg0) end

---@public
---@return void
function SandboxOptions_IntegerSandboxOption:setCustom() end

---@public
---@param arg0 KahluaTable
---@return void
function SandboxOptions_IntegerSandboxOption:toTable(arg0) end

---@public
---@return String
function SandboxOptions_IntegerSandboxOption:getShortName() end

---@public
---@return String
function SandboxOptions_IntegerSandboxOption:getTooltip() end

---@public
---@return boolean
function SandboxOptions_IntegerSandboxOption:isCustom() end

---@public
---@param arg0 String
---@return SandboxOptions.SandboxOption
function SandboxOptions_IntegerSandboxOption:setTranslation(arg0) end

---@public
---@param arg0 KahluaTable
---@return void
function SandboxOptions_IntegerSandboxOption:fromTable(arg0) end

---@public
---@return String
function SandboxOptions_IntegerSandboxOption:getTableName() end

---@public
---@return ConfigOption
function SandboxOptions_IntegerSandboxOption:asConfigOption() end

---@public
---@return String
function SandboxOptions_IntegerSandboxOption:getTranslatedName() end

---@public
---@return String
function SandboxOptions_IntegerSandboxOption:getPageName() end
