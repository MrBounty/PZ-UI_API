---@class ZomboidRadio : zombie.radio.ZomboidRadio
---@field public SAVE_FILE String
---@field private devices ArrayList|Unknown
---@field private broadcastDevices ArrayList|Unknown
---@field private scriptManager RadioScriptManager
---@field private DaysSinceStart int
---@field private lastRecordedHour int
---@field private playerLastLine String[]
---@field private channelNames Map|Unknown|Unknown
---@field private categorizedChannels Map|Unknown|Unknown
---@field private knownFrequencies List|Unknown
---@field private debugConsole RadioDebugConsole
---@field private hasRecievedServerData boolean
---@field private storySoundManager SLSoundManager
---@field private staticSounds String[]
---@field public DEBUG_MODE boolean
---@field public DEBUG_XML boolean
---@field public DEBUG_SOUND boolean
---@field public POST_RADIO_SILENCE boolean
---@field public DISABLE_BROADCASTING boolean
---@field private instance ZomboidRadio
---@field private recordedMedia RecordedMedia
---@field public LOUISVILLE_OBFUSCATION boolean
---@field private lastSaveFile String
---@field private lastSaveContent String
---@field private freqlist HashMap|Unknown|Unknown
---@field private hasAppliedRangeDistortion boolean
---@field private stringBuilder StringBuilder
---@field private hasAppliedInterference boolean
---@field private obfuscateChannels int[]
ZomboidRadio = {}

---@public
---@return String
function ZomboidRadio:getRandomBzztFzzt() end

---@public
---@param arg0 int
---@return void
function ZomboidRadio:removeChannelName(arg0) end

---@public
---@return RadioScriptManager
function ZomboidRadio:getScriptManager() end

---@private
---@return void
function ZomboidRadio:LouisvilleObfuscationCheck() end

---@public
---@param arg0 boolean
---@return void
function ZomboidRadio:setDisableBroadcasting(arg0) end

---@private
---@param arg0 String
---@param arg1 int
---@param arg2 boolean
---@return String
---@overload fun(arg0:String, arg1:int, arg2:boolean, arg3:String)
function ZomboidRadio:scrambleString(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 boolean
---@param arg3 String
---@return String
function ZomboidRadio:scrambleString(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 String
---@param arg4 String
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 int
---@param arg9 boolean
---@return void
function ZomboidRadio:ReceiveTransmission(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) end

---@public
---@param arg0 String
---@return String
function ZomboidRadio:computerize(arg0) end

---@public
---@param arg0 boolean
---@return void
function ZomboidRadio:setDisableMediaLineLearning(arg0) end

---@public
---@return void
function ZomboidRadio:Save() end

---@public
---@return boolean
function ZomboidRadio:hasInstance() end

---@public
---@return int
---@overload fun(arg0:int, arg1:int)
function ZomboidRadio:getRandomFrequency() end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function ZomboidRadio:getRandomFrequency(arg0, arg1) end

---@private
---@param arg0 String
---@param arg1 int
---@return String
function ZomboidRadio:applyWeatherInterference(arg0, arg1) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@return boolean
function ZomboidRadio:DeviceInRange(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 ChatMessage
---@param arg3 int
---@return void
---@overload fun(arg0:int, arg1:int, arg2:int, arg3:String, arg4:String, arg5:float, arg6:float, arg7:float, arg8:int, arg9:boolean)
function ZomboidRadio:SendTransmission(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 String
---@param arg4 String
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 int
---@param arg9 boolean
---@return void
function ZomboidRadio:SendTransmission(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) end

---@public
---@return boolean
function ZomboidRadio:getDisableMediaLineLearning() end

---@public
---@return void
function ZomboidRadio:render() end

---@public
---@return void
function ZomboidRadio:Reset() end

---@public
---@param arg0 ByteBufferWriter
---@return void
function ZomboidRadio:WriteRadioServerDataPacket(arg0) end

---@private
---@return void
function ZomboidRadio:checkGameModeSpecificStart() end

---@public
---@return Object
function ZomboidRadio:clone() end

---@public
---@param arg0 WaveSignalDevice
---@return void
function ZomboidRadio:RegisterDevice(arg0) end

---@public
---@return GameMode
function ZomboidRadio:getGameMode() end

---@public
---@param arg0 int
---@return String
function ZomboidRadio:getChannelName(arg0) end

---@private
---@param arg0 IsoPlayer
---@param arg1 IsoPlayer
---@return void
function ZomboidRadio:checkPlayerForDevice(arg0, arg1) end

---@public
---@return boolean
function ZomboidRadio:Load() end

---@public
---@param arg0 boolean
---@return void
function ZomboidRadio:setHasRecievedServerData(arg0) end

---@private
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@return String
function ZomboidRadio:doDeviceRangeDistortion(arg0, arg1, arg2) end

---@public
---@return ArrayList|Unknown
function ZomboidRadio:getBroadcastDevices() end

---@public
---@param arg0 String
---@return boolean
function ZomboidRadio:isStaticSound(arg0) end

---@public
---@param arg0 int
---@param arg1 boolean
---@param arg2 boolean
---@return void
function ZomboidRadio:PlayerListensChannel(arg0, arg1, arg2) end

---@public
---@return ArrayList|Unknown
function ZomboidRadio:getDevices() end

---@public
---@param arg0 RadioChannel
---@return void
function ZomboidRadio:ObfuscateChannelCheck(arg0) end

---@public
---@param arg0 WaveSignalDevice
---@return void
function ZomboidRadio:UnRegisterDevice(arg0) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 String
---@param arg4 String
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 int
---@param arg9 boolean
---@return void
function ZomboidRadio:DistributeTransmission(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return int
function ZomboidRadio:GetDistance(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function ZomboidRadio:UpdateScripts(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 String
---@return void
---@overload fun(arg0:String, arg1:int, arg2:String, arg3:boolean)
function ZomboidRadio:addChannelName(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 String
---@param arg3 boolean
---@return void
function ZomboidRadio:addChannelName(arg0, arg1, arg2, arg3) end

---@public
---@return int
function ZomboidRadio:getDaysSinceStart() end

---@private
---@param arg0 boolean
---@param arg1 DeviceData
---@param arg2 int
---@param arg3 int
---@return void
function ZomboidRadio:addFrequencyListEntry(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 IsoPlayer
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 String
---@param arg5 String
---@param arg6 float
---@param arg7 float
---@param arg8 float
---@param arg9 int
---@param arg10 boolean
---@return void
function ZomboidRadio:DistributeToPlayer(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) end

---@public
---@return boolean
function ZomboidRadio:getDisableBroadcasting() end

---@public
---@return void
function ZomboidRadio:update() end

---@public
---@return ZomboidRadio
function ZomboidRadio:getInstance() end

---@public
---@return RecordedMedia
function ZomboidRadio:getRecordedMedia() end

---@public
---@param arg0 String
---@return Map|Unknown|Unknown
function ZomboidRadio:GetChannelList(arg0) end

---@public
---@return Map|Unknown|Unknown
function ZomboidRadio:getFullChannelList() end

---@public
---@param arg0 int
---@return void
function ZomboidRadio:Init(arg0) end
