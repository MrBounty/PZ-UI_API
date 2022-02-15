---@class SteamUGCDetails : zombie.core.znet.SteamUGCDetails
---@field private ID long
---@field private title String
---@field private timeCreated long
---@field private timeUpdated long
---@field private fileSize int
---@field private childIDs long[]
SteamUGCDetails = {}

---@public
---@return String
function SteamUGCDetails:getTitle() end

---@public
---@return long
function SteamUGCDetails:getTimeUpdated() end

---@public
---@return long
function SteamUGCDetails:getID() end

---@public
---@param arg0 int
---@return long
function SteamUGCDetails:getChildID(arg0) end

---@public
---@return long[]
function SteamUGCDetails:getChildren() end

---@public
---@return String
function SteamUGCDetails:getState() end

---@public
---@return int
function SteamUGCDetails:getFileSize() end

---@public
---@return long
function SteamUGCDetails:getTimeCreated() end

---@public
---@return String
function SteamUGCDetails:getIDString() end

---@public
---@return int
function SteamUGCDetails:getNumChildren() end
