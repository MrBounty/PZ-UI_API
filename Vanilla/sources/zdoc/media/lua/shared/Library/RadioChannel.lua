---@class RadioChannel : zombie.radio.scripting.RadioChannel
---@field private GUID String
---@field private radioData RadioData
---@field private isTimeSynced boolean
---@field private scripts Map|Unknown|Unknown
---@field private frequency int
---@field private name String
---@field private isTv boolean
---@field private category ChannelCategory
---@field private playerIsListening boolean
---@field private currentScript RadioScript
---@field private currentScriptLoop int
---@field private currentScriptMaxLoops int
---@field private airingBroadcast RadioBroadCast
---@field private airCounter float
---@field private lastAiredLine String
---@field private lastBroadcastID String
---@field private airCounterMultiplier float
---@field private louisvilleObfuscate boolean
---@field minmod float
---@field maxmod float
RadioChannel = {}

---@public
---@return String
function RadioChannel:getGUID() end

---@public
---@return boolean
function RadioChannel:GetPlayerIsListening() end

---@private
---@param arg0 int
---@return void
function RadioChannel:getNextScript(arg0) end

---@public
---@return float
function RadioChannel:getAirCounterMultiplier() end

---@public
---@return RadioData
function RadioChannel:getRadioData() end

---@public
---@param arg0 RadioData
---@return void
function RadioChannel:setRadioData(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function RadioChannel:UpdateScripts(arg0, arg1) end

---@public
---@return int
function RadioChannel:getCurrentScriptLoop() end

---@public
---@param arg0 RadioScript
---@return void
function RadioChannel:AddRadioScript(arg0) end

---@public
---@param arg0 RadioBroadCast
---@return void
function RadioChannel:setAiringBroadcast(arg0) end

---@public
---@param arg0 boolean
---@return void
function RadioChannel:SetPlayerIsListening(arg0) end

---@public
---@return RadioScript
function RadioChannel:getCurrentScript() end

---@public
---@return boolean
function RadioChannel:isVanilla() end

---@public
---@return void
function RadioChannel:update() end

---@public
---@return ChannelCategory
function RadioChannel:GetCategory() end

---@public
---@return RadioBroadCast
function RadioChannel:getAiringBroadcast() end

---@public
---@param arg0 String
---@param arg1 int
---@return void
---@overload fun(arg0:String, arg1:int, arg2:int, arg3:int)
function RadioChannel:setActiveScript(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return void
function RadioChannel:setActiveScript(arg0, arg1, arg2, arg3) end

---@public
---@return void
function RadioChannel:setActiveScriptNull() end

---@public
---@return String
function RadioChannel:getLastAiredLine() end

---@public
---@param arg0 boolean
---@return void
function RadioChannel:setLouisvilleObfuscate(arg0) end

---@public
---@param arg0 String
---@param arg1 int
---@return void
function RadioChannel:LoadAiringBroadcast(arg0, arg1) end

---@public
---@param arg0 float
---@return void
function RadioChannel:setAirCounterMultiplier(arg0) end

---@public
---@return String
function RadioChannel:getLastBroadcastID() end

---@public
---@return boolean
function RadioChannel:isTimeSynced() end

---@public
---@return String
function RadioChannel:GetName() end

---@public
---@return int
function RadioChannel:getCurrentScriptMaxLoops() end

---@public
---@param arg0 boolean
---@return void
function RadioChannel:setTimeSynced(arg0) end

---@public
---@return boolean
function RadioChannel:IsTv() end

---@public
---@param arg0 String
---@return RadioScript
function RadioChannel:getRadioScript(arg0) end

---@public
---@return int
function RadioChannel:GetFrequency() end
