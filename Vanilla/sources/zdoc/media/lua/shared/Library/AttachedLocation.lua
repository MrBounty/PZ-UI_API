---@class AttachedLocation : zombie.characters.AttachedItems.AttachedLocation
---@field protected group AttachedLocationGroup
---@field protected id String
---@field protected attachmentName String
AttachedLocation = {}

---@public
---@return String
function AttachedLocation:getId() end

---@public
---@param arg0 String
---@return void
function AttachedLocation:setAttachmentName(arg0) end

---@public
---@return String
function AttachedLocation:getAttachmentName() end
