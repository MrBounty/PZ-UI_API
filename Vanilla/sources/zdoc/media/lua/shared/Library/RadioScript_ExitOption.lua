---@class RadioScript.ExitOption : zombie.radio.scripting.RadioScript.ExitOption
---@field private scriptname String
---@field private chance int
---@field private startDelay int
RadioScript_ExitOption = {}

---@public
---@return int
function RadioScript_ExitOption:getStartDelay() end

---@public
---@return String
function RadioScript_ExitOption:getScriptname() end

---@public
---@return int
function RadioScript_ExitOption:getChance() end
