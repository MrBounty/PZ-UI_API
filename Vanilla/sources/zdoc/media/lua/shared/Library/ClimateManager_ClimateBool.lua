---@class ClimateManager.ClimateBool : zombie.iso.weather.ClimateManager.ClimateBool
---@field protected internalValue boolean
---@field protected finalValue boolean
---@field protected isOverride boolean
---@field protected override boolean
---@field private isModded boolean
---@field private moddedValue boolean
---@field private isAdminOverride boolean
---@field private adminValue boolean
---@field private ID int
---@field private name String
ClimateManager_ClimateBool = {}

---@public
---@param arg0 int
---@param arg1 String
---@return ClimateManager.ClimateBool
function ClimateManager_ClimateBool:init(arg0, arg1) end

---@public
---@return boolean
function ClimateManager_ClimateBool:isEnableAdmin() end

---@public
---@return int
function ClimateManager_ClimateBool:getID() end

---@private
---@param arg0 DataOutputStream
---@return void
function ClimateManager_ClimateBool:saveAdmin(arg0) end

---@private
---@param arg0 ByteBuffer
---@return void
function ClimateManager_ClimateBool:readAdmin(arg0) end

---@public
---@param arg0 boolean
---@return void
function ClimateManager_ClimateBool:setOverride(arg0) end

---@public
---@return boolean
function ClimateManager_ClimateBool:getAdminValue() end

---@public
---@return boolean
function ClimateManager_ClimateBool:getInternalValue() end

---@public
---@param arg0 boolean
---@return void
function ClimateManager_ClimateBool:setEnableModded(arg0) end

---@private
---@param arg0 ByteBuffer
---@return void
function ClimateManager_ClimateBool:writeAdmin(arg0) end

---@public
---@return boolean
function ClimateManager_ClimateBool:isEnableOverride() end

---@private
---@param arg0 DataInputStream
---@param arg1 int
---@return void
function ClimateManager_ClimateBool:loadAdmin(arg0, arg1) end

---@public
---@param arg0 boolean
---@return void
function ClimateManager_ClimateBool:setEnableAdmin(arg0) end

---@public
---@param arg0 boolean
---@return void
function ClimateManager_ClimateBool:setAdminValue(arg0) end

---@public
---@param arg0 boolean
---@return void
function ClimateManager_ClimateBool:setModdedValue(arg0) end

---@public
---@param arg0 boolean
---@return void
function ClimateManager_ClimateBool:setEnableOverride(arg0) end

---@public
---@return boolean
function ClimateManager_ClimateBool:getModdedValue() end

---@public
---@param arg0 boolean
---@return void
function ClimateManager_ClimateBool:setFinalValue(arg0) end

---@public
---@return boolean
function ClimateManager_ClimateBool:getOverride() end

---@public
---@return String
function ClimateManager_ClimateBool:getName() end

---@private
---@return void
function ClimateManager_ClimateBool:calculate() end
