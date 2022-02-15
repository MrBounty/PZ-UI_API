---@class StorySoundEvent : zombie.radio.StorySounds.StorySoundEvent
---@field protected name String
---@field protected eventSounds ArrayList|Unknown
StorySoundEvent = {}

---@public
---@param arg0 String
---@return void
function StorySoundEvent:setName(arg0) end

---@public
---@return ArrayList|Unknown
function StorySoundEvent:getEventSounds() end

---@public
---@return String
function StorySoundEvent:getName() end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function StorySoundEvent:setEventSounds(arg0) end
