---@class SandboxOptions.StringSandboxOption : zombie.SandboxOptions.StringSandboxOption
---@field protected translation String
---@field protected tableName String
---@field protected shortName String
---@field protected bCustom boolean
---@field protected pageName String
SandboxOptions_StringSandboxOption = {}

---@public
---@param arg0 String
---@return SandboxOptions.SandboxOption
function SandboxOptions_StringSandboxOption:setTranslation(arg0) end

---@public
---@return String
function SandboxOptions_StringSandboxOption:getTableName() end

---@public
---@return String
function SandboxOptions_StringSandboxOption:getTooltip() end

---@public
---@return void
function SandboxOptions_StringSandboxOption:setCustom() end

---@public
---@return ConfigOption
function SandboxOptions_StringSandboxOption:asConfigOption() end

---@public
---@param arg0 KahluaTable
---@return void
function SandboxOptions_StringSandboxOption:fromTable(arg0) end

---@public
---@return String
function SandboxOptions_StringSandboxOption:getPageName() end

---@public
---@return String
function SandboxOptions_StringSandboxOption:getTranslatedName() end

---@public
---@param arg0 String
---@return SandboxOptions.SandboxOption
function SandboxOptions_StringSandboxOption:setPageName(arg0) end

---@public
---@return boolean
function SandboxOptions_StringSandboxOption:isCustom() end

---@public
---@param arg0 KahluaTable
---@return void
function SandboxOptions_StringSandboxOption:toTable(arg0) end

---@public
---@return String
function SandboxOptions_StringSandboxOption:getShortName() end
