---@class FMODAudio : fmod.fmod.FMODAudio
---@field public emitter BaseSoundEmitter
FMODAudio = {}

---@public
---@return void
function FMODAudio:stop() end

---@public
---@return boolean
function FMODAudio:isPlaying() end

---@public
---@return void
function FMODAudio:pause() end

---@public
---@return String
function FMODAudio:getName() end

---@public
---@return void
function FMODAudio:start() end

---@public
---@param arg0 float
---@return void
function FMODAudio:setVolume(arg0) end

---@public
---@param arg0 String
---@return void
function FMODAudio:setName(arg0) end
