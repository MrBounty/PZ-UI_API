---@class EventSound : zombie.radio.StorySounds.EventSound
---@field protected name String
---@field protected color Color
---@field protected dataPoints ArrayList|Unknown
---@field protected storySounds ArrayList|Unknown
EventSound = {}

---@public
---@param arg0 ArrayList|Unknown
---@return void
function EventSound:setStorySounds(arg0) end

---@public
---@return ArrayList|Unknown
function EventSound:getStorySounds() end

---@public
---@return Color
function EventSound:getColor() end

---@public
---@return String
function EventSound:getName() end

---@public
---@return ArrayList|Unknown
function EventSound:getDataPoints() end

---@public
---@param arg0 Color
---@return void
function EventSound:setColor(arg0) end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function EventSound:setDataPoints(arg0) end

---@public
---@param arg0 String
---@return void
function EventSound:setName(arg0) end
