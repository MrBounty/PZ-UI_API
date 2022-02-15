---@class Server : zombie.network.Server
---@field private name String
---@field private ip String
---@field private localIP String
---@field private port String
---@field private serverpwd String
---@field private description String
---@field private userName String
---@field private pwd String
---@field private lastUpdate int
---@field private players String
---@field private maxPlayers String
---@field private open boolean
---@field private bPublic boolean
---@field private version String
---@field private mods String
---@field private passwordProtected boolean
---@field private steamId String
---@field private ping String
---@field private hosted boolean
Server = {}

---@public
---@return String
function Server:getServerPassword() end

---@public
---@param players String
---@return void
function Server:setPlayers(players) end

---@public
---@param version String
---@return void
function Server:setVersion(version) end

---@public
---@return String
function Server:getVersion() end

---@public
---@param lastUpdate int
---@return void
function Server:setLastUpdate(lastUpdate) end

---@public
---@param userName String
---@return void
function Server:setUserName(userName) end

---@public
---@return String
function Server:getMaxPlayers() end

---@public
---@return String
function Server:getPwd() end

---@public
---@param open boolean
---@return void
function Server:setOpen(open) end

---@public
---@return boolean
function Server:isHosted() end

---@public
---@param maxPlayers String
---@return void
function Server:setMaxPlayers(maxPlayers) end

---@public
---@return String
function Server:getName() end

---@public
---@return String
function Server:getUserName() end

---@public
---@return String
function Server:getMods() end

---@public
---@param arg0 boolean
---@return void
function Server:setPublic(arg0) end

---@public
---@return String
function Server:getLocalIP() end

---@public
---@param arg0 String
---@return void
function Server:setSteamId(arg0) end

---@public
---@param port String
---@return void
function Server:setPort(port) end

---@public
---@return String
function Server:getPlayers() end

---@public
---@param arg0 boolean
---@return void
function Server:setPasswordProtected(arg0) end

---@public
---@param arg0 String
---@return void
function Server:setServerPassword(arg0) end

---@public
---@return String
function Server:getDescription() end

---@public
---@return boolean
function Server:isPublic() end

---@public
---@return int
function Server:getLastUpdate() end

---@public
---@param pwd String
---@return void
function Server:setPwd(pwd) end

---@public
---@param arg0 String
---@return void
function Server:setLocalIP(arg0) end

---@public
---@return boolean
function Server:isOpen() end

---@public
---@return String
function Server:getSteamId() end

---@public
---@return boolean
function Server:isPasswordProtected() end

---@public
---@param name String
---@return void
function Server:setName(name) end

---@public
---@param arg0 boolean
---@return void
function Server:setHosted(arg0) end

---@public
---@return String
function Server:getPing() end

---@public
---@param ip String
---@return void
function Server:setIp(ip) end

---@public
---@return String
function Server:getPort() end

---@public
---@param arg0 String
---@return void
function Server:setPing(arg0) end

---@public
---@param mods String
---@return void
function Server:setMods(mods) end

---@public
---@return String
function Server:getIp() end

---@public
---@param description String
---@return void
function Server:setDescription(description) end
