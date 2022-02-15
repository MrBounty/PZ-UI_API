---@class ThunderStorm : zombie.iso.weather.ThunderStorm
---@field public MAP_MIN_X int
---@field public MAP_MIN_Y int
---@field public MAP_MAX_X int
---@field public MAP_MAX_Y int
---@field private hasActiveThunderClouds boolean
---@field private cloudMaxRadius float
---@field private events ThunderStorm.ThunderEvent[]
---@field private clouds ThunderStorm.ThunderCloud[]
---@field private climateManager ClimateManager
---@field private cloudCache ArrayList|Unknown
---@field private donoise boolean
---@field private strikeRadius int
---@field private lightningInfos ThunderStorm.PlayerLightningInfo[]
---@field private networkThunderEvent ThunderStorm.ThunderEvent
---@field private dummyCloud ThunderStorm.ThunderCloud
ThunderStorm = {}

---@public
---@return boolean
function ThunderStorm:isModifyingNight() end

---@private
---@param arg0 int
---@return ThunderStorm.ThunderCloud
function ThunderStorm:getCloud(arg0) end

---@public
---@param arg0 ByteBuffer
---@return void
function ThunderStorm:readNetThunderEvent(arg0) end

---@public
---@param arg0 int
---@return void
function ThunderStorm:stopCloud(arg0) end

---@public
---@return ArrayList|Unknown
function ThunderStorm:getClouds() end

---@public
---@param arg0 ByteBuffer
---@return void
function ThunderStorm:writeNetThunderEvent(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 double
---@param arg6 boolean
---@return void
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float, arg4:float, arg5:double, arg6:boolean, arg7:float)
function ThunderStorm:startThunderCloud(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 double
---@param arg6 boolean
---@param arg7 float
---@return ThunderStorm.ThunderCloud
function ThunderStorm:startThunderCloud(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@private
---@return ThunderStorm.ThunderEvent
function ThunderStorm:getFreeEvent() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 boolean
---@param arg3 boolean
---@param arg4 boolean
---@return void
function ThunderStorm:enqueueThunderEvent(arg0, arg1, arg2, arg3, arg4) end

---@public
---@return boolean
function ThunderStorm:HasActiveThunderClouds() end

---@public
---@param arg0 RenderSettings.PlayerRenderSettings
---@param arg1 int
---@param arg2 IsoPlayer
---@return void
function ThunderStorm:applyLightningForPlayer(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@return void
function ThunderStorm:noise(arg0) end

---@private
---@return ThunderStorm.ThunderCloud
function ThunderStorm:getFreeCloud() end

---@private
---@param arg0 float
---@param arg1 float
---@return float
function ThunderStorm:addToAngle(arg0, arg1) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return int
function ThunderStorm:GetDistance(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 double
---@return void
function ThunderStorm:update(arg0) end

---@public
---@return int
function ThunderStorm:getMapDiagonal() end

---@public
---@param arg0 DataOutputStream
---@return void
function ThunderStorm:save(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 boolean
---@param arg3 boolean
---@param arg4 boolean
---@return void
function ThunderStorm:triggerThunderEvent(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 DataInputStream
---@return void
function ThunderStorm:load(arg0) end

---@public
---@return void
function ThunderStorm:stopAllClouds() end
