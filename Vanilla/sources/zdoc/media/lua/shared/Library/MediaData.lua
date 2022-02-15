---@class MediaData : zombie.radio.media.MediaData
---@field private id String
---@field private itemDisplayName String
---@field private title String
---@field private subtitle String
---@field private author String
---@field private extra String
---@field private index short
---@field private category String
---@field private spawning int
---@field private lines ArrayList|Unknown
MediaData = {}

---@public
---@return String
function MediaData:getCategory() end

---@public
---@param arg0 String
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 String
---@return void
function MediaData:addLine(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 String
---@return void
function MediaData:setSubtitle(arg0) end

---@public
---@return int
function MediaData:getLineCount() end

---@public
---@return byte
function MediaData:getMediaType() end

---@public
---@return String
function MediaData:getExtraEN() end

---@public
---@return boolean
function MediaData:hasTitle() end

---@public
---@param arg0 String
---@return void
function MediaData:setExtra(arg0) end

---@public
---@return boolean
function MediaData:hasAuthor() end

---@public
---@param arg0 String
---@return void
function MediaData:setAuthor(arg0) end

---@public
---@return String
function MediaData:getTitleEN() end

---@protected
---@param arg0 short
---@return void
function MediaData:setIndex(arg0) end

---@public
---@return String
function MediaData:getSubtitleEN() end

---@public
---@return String
function MediaData:getTranslatedTitle() end

---@public
---@return boolean
function MediaData:hasSubTitle() end

---@public
---@return String
function MediaData:getTranslatedSubTitle() end

---@public
---@return String
function MediaData:getAuthorEN() end

---@protected
---@param arg0 String
---@return void
function MediaData:setCategory(arg0) end

---@public
---@return String
function MediaData:getTranslatedExtra() end

---@public
---@param arg0 int
---@return MediaData.MediaLineData
function MediaData:getLine(arg0) end

---@public
---@return boolean
function MediaData:hasExtra() end

---@public
---@return String
function MediaData:getTranslatedAuthor() end

---@public
---@return int
function MediaData:getSpawning() end

---@public
---@param arg0 String
---@return void
function MediaData:setTitle(arg0) end

---@public
---@return String
function MediaData:getTranslatedItemDisplayName() end

---@public
---@return String
function MediaData:getId() end

---@public
---@return short
function MediaData:getIndex() end
