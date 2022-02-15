---@class RecordedMedia : zombie.radio.media.RecordedMedia
---@field public DISABLE_LINE_LEARNING boolean
---@field private SPAWN_COMMON int
---@field private SPAWN_RARE int
---@field private SPAWN_EXCEPTIONAL int
---@field public VERSION int
---@field public SAVE_FILE String
---@field private indexes ArrayList|Unknown
---@field private mediaDataMap Map|Unknown|Unknown
---@field private categorizedMap Map|Unknown|Unknown
---@field private categories ArrayList|Unknown
---@field private listenedLines HashSet|Unknown
---@field private homeVhsSpawned HashSet|Unknown
---@field private retailVhsSpawnTable Map|Unknown|Unknown
---@field private retailCdSpawnTable Map|Unknown|Unknown
---@field private REQUIRES_SAVING boolean
RecordedMedia = {}

---@public
---@param arg0 String
---@return String
function RecordedMedia:toAscii(arg0) end

---@public
---@return void
function RecordedMedia:load() end

---@public
---@param arg0 byte
---@return ArrayList|Unknown
function RecordedMedia:getAllMediaForType(arg0) end

---@public
---@param arg0 String
---@return byte
function RecordedMedia:getMediaTypeForCategory(arg0) end

---@public
---@return ArrayList|Unknown
function RecordedMedia:getCategories() end

---@public
---@param arg0 String
---@return MediaData
function RecordedMedia:getMediaData(arg0) end

---@public
---@param arg0 short
---@return MediaData
function RecordedMedia:getMediaDataFromIndex(arg0) end

---@public
---@param arg0 IsoPlayer
---@param arg1 MediaData
---@return boolean
function RecordedMedia:hasListenedToAll(arg0, arg1) end

---@public
---@param arg0 MediaData
---@return short
function RecordedMedia:getIndexForMediaData(arg0) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 String
---@param arg3 int
---@return MediaData
function RecordedMedia:register(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@return boolean
function RecordedMedia:hasListenedLineAndAdd(arg0) end

---@public
---@return void
function RecordedMedia:save() end

---@public
---@param arg0 String
---@return MediaData
function RecordedMedia:getRandomFromCategory(arg0) end

---@public
---@return void
function RecordedMedia:init() end

---@public
---@param arg0 IsoPlayer
---@param arg1 String
---@return boolean
function RecordedMedia:hasListenedToLine(arg0, arg1) end
