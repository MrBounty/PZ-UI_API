---@class AttachedLocationGroup : zombie.characters.AttachedItems.AttachedLocationGroup
---@field protected id String
---@field protected locations ArrayList|Unknown
AttachedLocationGroup = {}

---@public
---@param arg0 String
---@return int
function AttachedLocationGroup:indexOf(arg0) end

---@public
---@param arg0 int
---@return AttachedLocation
function AttachedLocationGroup:getLocationByIndex(arg0) end

---@public
---@param arg0 String
---@return void
function AttachedLocationGroup:checkValid(arg0) end

---@public
---@param arg0 String
---@return AttachedLocation
function AttachedLocationGroup:getOrCreateLocation(arg0) end

---@public
---@param arg0 String
---@return AttachedLocation
function AttachedLocationGroup:getLocation(arg0) end

---@public
---@return int
function AttachedLocationGroup:size() end
