---@class SoundManager.AmbientSoundEffect : zombie.SoundManager.AmbientSoundEffect
---@field public name String
---@field public eventInstance long
---@field public gain float
---@field public clip GameSoundClip
---@field public effectiveVolume float
SoundManager_AmbientSoundEffect = {}

---@public
---@return void
function SoundManager_AmbientSoundEffect:update() end

---@public
---@return void
function SoundManager_AmbientSoundEffect:start() end

---@public
---@return void
function SoundManager_AmbientSoundEffect:stop() end

---@public
---@return boolean
function SoundManager_AmbientSoundEffect:isPlaying() end

---@public
---@return String
function SoundManager_AmbientSoundEffect:getName() end

---@public
---@param arg0 float
---@return void
function SoundManager_AmbientSoundEffect:setVolume(arg0) end

---@public
---@return void
function SoundManager_AmbientSoundEffect:pause() end

---@public
---@param arg0 String
---@return void
function SoundManager_AmbientSoundEffect:setName(arg0) end
