---@class GameSound : zombie.audio.GameSound
---@field public name String
---@field public category String
---@field public loop boolean
---@field public is3D boolean
---@field public clips ArrayList|Unknown
---@field private userVolume float
---@field public master GameSound.MasterVolume
---@field public reloadEpoch short
GameSound = {}

---@public
---@return boolean
function GameSound:isLooped() end

---@public
---@return GameSoundClip
function GameSound:getRandomClip() end

---@public
---@param arg0 float
---@return void
function GameSound:setUserVolume(arg0) end

---@public
---@return String
function GameSound:getName() end

---@public
---@return String
function GameSound:getCategory() end

---@public
---@return String
function GameSound:getMasterName() end

---@public
---@return void
function GameSound:reset() end

---@public
---@return float
function GameSound:getUserVolume() end
