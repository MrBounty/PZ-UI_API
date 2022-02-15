---@class ClimateManager.ClimateColor : zombie.iso.weather.ClimateManager.ClimateColor
---@field protected internalValue ClimateColorInfo
---@field protected finalValue ClimateColorInfo
---@field protected isOverride boolean
---@field protected override ClimateColorInfo
---@field protected interpolate float
---@field private isModded boolean
---@field private moddedValue ClimateColorInfo
---@field private modInterpolate float
---@field private isAdminOverride boolean
---@field private adminValue ClimateColorInfo
---@field private ID int
---@field private name String
ClimateManager_ClimateColor = {}

---@public
---@return ClimateColorInfo
function ClimateManager_ClimateColor:getInternalValue() end

---@public
---@return ClimateColorInfo
function ClimateManager_ClimateColor:getModdedValue() end

---@public
---@param arg0 ClimateColorInfo
---@return void
function ClimateManager_ClimateColor:setModdedValue(arg0) end

---@public
---@param arg0 boolean
---@return void
function ClimateManager_ClimateColor:setEnableModded(arg0) end

---@private
---@param arg0 ByteBuffer
---@return void
function ClimateManager_ClimateColor:writeAdmin(arg0) end

---@public
---@return ClimateColorInfo
function ClimateManager_ClimateColor:getFinalValue() end

---@public
---@param arg0 int
---@param arg1 String
---@return ClimateManager.ClimateColor
function ClimateManager_ClimateColor:init(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function ClimateManager_ClimateColor:setAdminValueExterior(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 float
---@return void
function ClimateManager_ClimateColor:setModdedInterpolate(arg0) end

---@public
---@return float
function ClimateManager_ClimateColor:getOverrideInterpolate() end

---@public
---@return boolean
function ClimateManager_ClimateColor:isEnableAdmin() end

---@private
---@param arg0 ByteBuffer
---@return void
function ClimateManager_ClimateColor:readAdmin(arg0) end

---@public
---@param arg0 ClimateColorInfo
---@return void
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float, arg4:float, arg5:float, arg6:float, arg7:float)
function ClimateManager_ClimateColor:setAdminValue(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@return void
function ClimateManager_ClimateColor:setAdminValue(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@private
---@return void
function ClimateManager_ClimateColor:calculate() end

---@public
---@param arg0 ByteBuffer
---@param arg1 float
---@return void
---@overload fun(arg0:ClimateColorInfo, arg1:float)
function ClimateManager_ClimateColor:setOverride(arg0, arg1) end

---@public
---@param arg0 ClimateColorInfo
---@param arg1 float
---@return void
function ClimateManager_ClimateColor:setOverride(arg0, arg1) end

---@public
---@param arg0 boolean
---@return void
function ClimateManager_ClimateColor:setEnableAdmin(arg0) end

---@public
---@param arg0 ClimateColorInfo
---@return void
function ClimateManager_ClimateColor:setFinalValue(arg0) end

---@public
---@return int
function ClimateManager_ClimateColor:getID() end

---@public
---@return ClimateColorInfo
function ClimateManager_ClimateColor:getAdminValue() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function ClimateManager_ClimateColor:setAdminValueInterior(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 DataInputStream
---@param arg1 int
---@return void
function ClimateManager_ClimateColor:loadAdmin(arg0, arg1) end

---@public
---@return ClimateColorInfo
function ClimateManager_ClimateColor:getOverride() end

---@public
---@return String
function ClimateManager_ClimateColor:getName() end

---@public
---@return boolean
function ClimateManager_ClimateColor:isEnableOverride() end

---@public
---@param arg0 boolean
---@return void
function ClimateManager_ClimateColor:setEnableOverride(arg0) end

---@private
---@param arg0 DataOutputStream
---@return void
function ClimateManager_ClimateColor:saveAdmin(arg0) end
