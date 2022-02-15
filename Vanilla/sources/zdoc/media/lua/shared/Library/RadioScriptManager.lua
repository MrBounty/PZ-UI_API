---@class RadioScriptManager : zombie.radio.scripting.RadioScriptManager
---@field private channels Map|Unknown|Unknown
---@field private instance RadioScriptManager
---@field private currentTimeStamp int
---@field private channelsList ArrayList|Unknown
RadioScriptManager = {}

---@public
---@param arg0 int
---@return void
function RadioScriptManager:init(arg0) end

---@public
---@param arg0 String
---@return RadioChannel
function RadioScriptManager:getRadioChannel(arg0) end

---@public
---@param arg0 int
---@param arg1 boolean
---@param arg2 boolean
---@return void
function RadioScriptManager:PlayerListensChannel(arg0, arg1, arg2) end

---@public
---@return void
function RadioScriptManager:update() end

---@public
---@return Map|Unknown|Unknown
function RadioScriptManager:getChannels() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function RadioScriptManager:UpdateScripts(arg0, arg1, arg2) end

---@public
---@return ArrayList|Unknown
function RadioScriptManager:getChannelsList() end

---@public
---@return int
function RadioScriptManager:getCurrentTimeStamp() end

---@public
---@return boolean
function RadioScriptManager:hasInstance() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 boolean
---@return void
function RadioScriptManager:simulateChannelUntil(arg0, arg1, arg2) end

---@public
---@param arg0 DataInputStream
---@return void
function RadioScriptManager:LoadOLD(arg0) end

---@public
---@param arg0 Writer
---@return void
function RadioScriptManager:Save(arg0) end

---@public
---@return RadioScriptManager
function RadioScriptManager:getInstance() end

---@public
---@param arg0 int
---@return void
function RadioScriptManager:RemoveChannel(arg0) end

---@public
---@param arg0 int
---@param arg1 boolean
---@return void
function RadioScriptManager:simulateScriptsUntil(arg0, arg1) end

---@public
---@return void
function RadioScriptManager:reset() end

---@public
---@param arg0 DataOutputStream
---@return void
function RadioScriptManager:SaveOLD(arg0) end

---@public
---@param arg0 RadioChannel
---@param arg1 boolean
---@return void
function RadioScriptManager:AddChannel(arg0, arg1) end

---@public
---@param arg0 BufferedReader
---@return void
function RadioScriptManager:Load(arg0) end
