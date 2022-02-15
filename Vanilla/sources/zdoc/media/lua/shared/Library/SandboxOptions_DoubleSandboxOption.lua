---@class SandboxOptions.DoubleSandboxOption : zombie.SandboxOptions.DoubleSandboxOption
---@field protected translation String
---@field protected tableName String
---@field protected shortName String
---@field protected bCustom boolean
---@field protected pageName String
SandboxOptions_DoubleSandboxOption = {}

---@public
---@param arg0 String
---@return SandboxOptions.SandboxOption
function SandboxOptions_DoubleSandboxOption:setPageName(arg0) end

---@public
---@return ConfigOption
function SandboxOptions_DoubleSandboxOption:asConfigOption() end

---@public
---@return String
function SandboxOptions_DoubleSandboxOption:getTooltip() end

---@public
---@return void
function SandboxOptions_DoubleSandboxOption:setCustom() end

---@public
---@param arg0 String
---@return SandboxOptions.SandboxOption
function SandboxOptions_DoubleSandboxOption:setTranslation(arg0) end

---@public
---@return boolean
function SandboxOptions_DoubleSandboxOption:isCustom() end

---@public
---@param arg0 KahluaTable
---@return void
function SandboxOptions_DoubleSandboxOption:toTable(arg0) end

---@public
---@return String
function SandboxOptions_DoubleSandboxOption:getShortName() end

---@public
---@return String
function SandboxOptions_DoubleSandboxOption:getTableName() end

---@public
---@param arg0 KahluaTable
---@return void
function SandboxOptions_DoubleSandboxOption:fromTable(arg0) end

---@public
---@return String
function SandboxOptions_DoubleSandboxOption:getTranslatedName() end

---@public
---@return String
function SandboxOptions_DoubleSandboxOption:getPageName() end
