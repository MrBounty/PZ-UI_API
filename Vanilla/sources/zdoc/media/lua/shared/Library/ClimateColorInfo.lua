---@class ClimateColorInfo : zombie.iso.weather.ClimateColorInfo
---@field private interior Color
---@field private exterior Color
---@field private writer BufferedWriter
ClimateColorInfo = {}

---@public
---@param arg0 ClimateColorInfo
---@param arg1 float
---@param arg2 ClimateColorInfo
---@return ClimateColorInfo
---@overload fun(arg0:ClimateColorInfo, arg1:ClimateColorInfo, arg2:float, arg3:ClimateColorInfo)
function ClimateColorInfo:interp(arg0, arg1, arg2) end

---@public
---@param arg0 ClimateColorInfo
---@param arg1 ClimateColorInfo
---@param arg2 float
---@param arg3 ClimateColorInfo
---@return ClimateColorInfo
function ClimateColorInfo:interp(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 Color
---@return void
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float)
function ClimateColorInfo:setExterior(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function ClimateColorInfo:setExterior(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 Color
---@return void
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float)
function ClimateColorInfo:setInterior(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function ClimateColorInfo:setInterior(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 String
---@return void
---@overload fun(arg0:ByteBuffer)
---@overload fun(arg0:int, arg1:String)
function ClimateColorInfo:write(arg0) end

---@public
---@param arg0 ByteBuffer
---@return void
function ClimateColorInfo:write(arg0) end

---@private
---@param arg0 int
---@param arg1 String
---@return void
function ClimateColorInfo:write(arg0, arg1) end

---@public
---@param arg0 float
---@return void
function ClimateColorInfo:scale(arg0) end

---@private
---@param arg0 int
---@param arg1 ClimateColorInfo
---@return void
function ClimateColorInfo:writeColor(arg0, arg1) end

---@public
---@return Color
function ClimateColorInfo:getInterior() end

---@public
---@param arg0 DataOutputStream
---@return void
function ClimateColorInfo:save(arg0) end

---@public
---@param arg0 DataInputStream
---@param arg1 int
---@return void
function ClimateColorInfo:load(arg0, arg1) end

---@public
---@return Color
function ClimateColorInfo:getExterior() end

---@private
---@param arg0 int
---@param arg1 ClimateColorInfo
---@param arg2 String
---@param arg3 String
---@param arg4 String
---@return void
function ClimateColorInfo:writeSeasonColor(arg0, arg1, arg2, arg3, arg4) end

---@public
---@return boolean
function ClimateColorInfo:writeColorInfoConfig() end

---@public
---@param arg0 ByteBuffer
---@return void
function ClimateColorInfo:read(arg0) end

---@public
---@param arg0 ClimateColorInfo
---@return void
function ClimateColorInfo:setTo(arg0) end
