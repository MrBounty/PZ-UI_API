---@class SandboxOptions.BooleanSandboxOption : zombie.SandboxOptions.BooleanSandboxOption
---@field protected translation String
---@field protected tableName String
---@field protected shortName String
---@field protected bCustom boolean
---@field protected pageName String
SandboxOptions_BooleanSandboxOption = {}

---@public
---@return String
function SandboxOptions_BooleanSandboxOption:getTableName() end

---@public
---@return String
function SandboxOptions_BooleanSandboxOption:getTooltip() end

---@public
---@return void
function SandboxOptions_BooleanSandboxOption:setCustom() end

---@public
---@return ConfigOption
function SandboxOptions_BooleanSandboxOption:asConfigOption() end

---@public
---@return String
function SandboxOptions_BooleanSandboxOption:getTranslatedName() end

---@public
---@param arg0 KahluaTable
---@return void
function SandboxOptions_BooleanSandboxOption:fromTable(arg0) end

---@public
---@return String
function SandboxOptions_BooleanSandboxOption:getPageName() end

---@public
---@param arg0 String
---@return SandboxOptions.SandboxOption
function SandboxOptions_BooleanSandboxOption:setPageName(arg0) end

---@public
---@param arg0 KahluaTable
---@return void
function SandboxOptions_BooleanSandboxOption:toTable(arg0) end

---@public
---@return String
function SandboxOptions_BooleanSandboxOption:getShortName() end

---@public
---@return boolean
function SandboxOptions_BooleanSandboxOption:isCustom() end

---@public
---@param arg0 String
---@return SandboxOptions.SandboxOption
function SandboxOptions_BooleanSandboxOption:setTranslation(arg0) end
