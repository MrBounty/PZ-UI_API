---@class GameSounds : zombie.GameSounds
---@field public VERSION int
---@field protected soundByName HashMap|Unknown|Unknown
---@field protected sounds ArrayList|Unknown
---@field private previewBank GameSounds.BankPreviewSound
---@field private previewFile GameSounds.FilePreviewSound
---@field public soundIsPaused boolean
---@field private previewSound GameSounds.IPreviewSound
GameSounds = {}

---@public
---@param arg0 GameSound
---@return void
function GameSounds:addSound(arg0) end

---@public
---@return void
function GameSounds:Reset() end

---@private
---@param arg0 GameSound
---@return void
function GameSounds:initClipEvents(arg0) end

---@public
---@param arg0 String
---@return ArrayList|Unknown
function GameSounds:getSoundsInCategory(arg0) end

---@public
---@param arg0 String
---@return void
function GameSounds:previewSound(arg0) end

---@public
---@return void
function GameSounds:loadINI() end

---@public
---@return void
function GameSounds:ScriptsLoaded() end

---@public
---@return void
function GameSounds:stopPreview() end

---@public
---@return void
function GameSounds:saveINI() end

---@public
---@param arg0 String
---@return GameSound
function GameSounds:getSound(arg0) end

---@public
---@param arg0 String
---@return GameSound
function GameSounds:getOrCreateSound(arg0) end

---@public
---@return boolean
function GameSounds:isPreviewPlaying() end

---@public
---@param arg0 String
---@return void
function GameSounds:ReloadFile(arg0) end

---@public
---@param arg0 boolean
---@return void
function GameSounds:fix3DListenerPosition(arg0) end

---@public
---@return ArrayList|Unknown
function GameSounds:getCategories() end

---@public
---@param arg0 String
---@return boolean
function GameSounds:isKnownSound(arg0) end

---@private
---@return void
function GameSounds:loadNonBankSounds() end
