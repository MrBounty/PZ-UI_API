---@class SandboxOptions.EnumSandboxOption : zombie.SandboxOptions.EnumSandboxOption
---@field protected translation String
---@field protected tableName String
---@field protected shortName String
---@field protected bCustom boolean
---@field protected pageName String
---@field protected valueTranslation String
SandboxOptions_EnumSandboxOption = {}

---@public
---@return String
function SandboxOptions_EnumSandboxOption:getTranslatedName() end

---@public
---@return String
function SandboxOptions_EnumSandboxOption:getPageName() end

---@public
---@return void
function SandboxOptions_EnumSandboxOption:setCustom() end

---@public
---@return String
function SandboxOptions_EnumSandboxOption:getTooltip() end

---@public
---@param arg0 KahluaTable
---@return void
function SandboxOptions_EnumSandboxOption:toTable(arg0) end

---@public
---@return String
function SandboxOptions_EnumSandboxOption:getValueTranslation() end

---@public
---@return ConfigOption
function SandboxOptions_EnumSandboxOption:asConfigOption() end

---@public
---@return boolean
function SandboxOptions_EnumSandboxOption:isCustom() end

---@public
---@return String
function SandboxOptions_EnumSandboxOption:getShortName() end

---@public
---@param arg0 int
---@return String
function SandboxOptions_EnumSandboxOption:getValueTranslationByIndex(arg0) end

---@public
---@param arg0 String
---@return SandboxOptions.SandboxOption
function SandboxOptions_EnumSandboxOption:setTranslation(arg0) end

---@public
---@param arg0 String
---@return SandboxOptions.SandboxOption
function SandboxOptions_EnumSandboxOption:setPageName(arg0) end

---@public
---@param arg0 String
---@return SandboxOptions.EnumSandboxOption
function SandboxOptions_EnumSandboxOption:setValueTranslation(arg0) end

---@public
---@param arg0 KahluaTable
---@return void
function SandboxOptions_EnumSandboxOption:fromTable(arg0) end

---@public
---@return String
function SandboxOptions_EnumSandboxOption:getTableName() end
