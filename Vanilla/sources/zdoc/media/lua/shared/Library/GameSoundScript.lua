---@class GameSoundScript : zombie.scripting.objects.GameSoundScript
---@field public gameSound GameSound
GameSoundScript = {}

---@private
---@param arg0 ScriptParser.Block
---@return GameSoundClip
function GameSoundScript:LoadClip(arg0) end

---@public
---@param arg0 String
---@param arg1 String
---@return void
function GameSoundScript:Load(arg0, arg1) end

---@public
---@return void
function GameSoundScript:reset() end
