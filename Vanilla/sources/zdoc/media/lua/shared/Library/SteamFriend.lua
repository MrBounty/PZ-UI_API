---@class SteamFriend : zombie.core.znet.SteamFriend
---@field private name String
---@field private steamID long
---@field private steamIDString String
SteamFriend = {}

---@public
---@return Texture
function SteamFriend:getAvatar() end

---@public
---@return String
function SteamFriend:getState() end

---@public
---@return String
function SteamFriend:getName() end

---@public
---@return String
function SteamFriend:getSteamID() end
