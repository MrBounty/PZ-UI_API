---@class PresetEntry : zombie.radio.devices.PresetEntry
---@field public name String
---@field public frequency int
PresetEntry = {}

---@public
---@return int
function PresetEntry:getFrequency() end

---@public
---@param arg0 int
---@return void
function PresetEntry:setFrequency(arg0) end

---@public
---@return String
function PresetEntry:getName() end

---@public
---@param arg0 String
---@return void
function PresetEntry:setName(arg0) end
