---@class ClimateManager.ClimateFloat : zombie.iso.weather.ClimateManager.ClimateFloat
---@field protected internalValue float
---@field protected finalValue float
---@field protected isOverride boolean
---@field protected override float
---@field protected interpolate float
---@field private isModded boolean
---@field private moddedValue float
---@field private modInterpolate float
---@field private isAdminOverride boolean
---@field private adminValue float
---@field private min float
---@field private max float
---@field private ID int
---@field private name String
ClimateManager_ClimateFloat = {}

---@public
---@return float
function ClimateManager_ClimateFloat:getModdedValue() end

---@public
---@param arg0 float
---@return void
function ClimateManager_ClimateFloat:setModdedValue(arg0) end

---@public
---@param arg0 float
---@return void
function ClimateManager_ClimateFloat:setAdminValue(arg0) end

---@public
---@param arg0 int
---@param arg1 String
---@return ClimateManager.ClimateFloat
function ClimateManager_ClimateFloat:init(arg0, arg1) end

---@public
---@param arg0 boolean
---@return void
function ClimateManager_ClimateFloat:setEnableModded(arg0) end

---@public
---@return float
function ClimateManager_ClimateFloat:getAdminValue() end

---@private
---@param arg0 ByteBuffer
---@return void
function ClimateManager_ClimateFloat:writeAdmin(arg0) end

---@public
---@return float
function ClimateManager_ClimateFloat:getInternalValue() end

---@public
---@param arg0 float
---@return void
function ClimateManager_ClimateFloat:setModdedInterpolate(arg0) end

---@public
---@return float
function ClimateManager_ClimateFloat:getOverride() end

---@public
---@return float
function ClimateManager_ClimateFloat:getOverrideInterpolate() end

---@public
---@return boolean
function ClimateManager_ClimateFloat:isEnableAdmin() end

---@private
---@return void
function ClimateManager_ClimateFloat:calculate() end

---@private
---@param arg0 ByteBuffer
---@return void
function ClimateManager_ClimateFloat:readAdmin(arg0) end

---@public
---@return float
function ClimateManager_ClimateFloat:getMax() end

---@public
---@return float
function ClimateManager_ClimateFloat:getFinalValue() end

---@public
---@param arg0 boolean
---@return void
function ClimateManager_ClimateFloat:setEnableAdmin(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@return void
function ClimateManager_ClimateFloat:setOverride(arg0, arg1) end

---@public
---@return int
function ClimateManager_ClimateFloat:getID() end

---@public
---@return float
function ClimateManager_ClimateFloat:getMin() end

---@private
---@param arg0 DataInputStream
---@param arg1 int
---@return void
function ClimateManager_ClimateFloat:loadAdmin(arg0, arg1) end

---@public
---@return String
function ClimateManager_ClimateFloat:getName() end

---@public
---@param arg0 float
---@return void
function ClimateManager_ClimateFloat:setFinalValue(arg0) end

---@public
---@return boolean
function ClimateManager_ClimateFloat:isEnableOverride() end

---@public
---@param arg0 boolean
---@return void
function ClimateManager_ClimateFloat:setEnableOverride(arg0) end

---@private
---@param arg0 DataOutputStream
---@return void
function ClimateManager_ClimateFloat:saveAdmin(arg0) end
