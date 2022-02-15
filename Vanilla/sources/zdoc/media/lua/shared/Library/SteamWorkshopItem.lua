---@class SteamWorkshopItem : zombie.core.znet.SteamWorkshopItem
---@field private workshopFolder String
---@field private PublishedFileId String
---@field private title String
---@field private description String
---@field private visibility String
---@field private tags ArrayList|Unknown
---@field private changeNote String
---@field private bHasMod boolean
---@field private bHasMap boolean
---@field private modIDs ArrayList|Unknown
---@field private mapFolders ArrayList|Unknown
---@field private VERSION1 int
---@field private LATEST_VERSION int
SteamWorkshopItem = {}

---@public
---@return String
function SteamWorkshopItem:getChangeNote() end

---@public
---@param arg0 Path
---@return String
function SteamWorkshopItem:validatePreviewImage(arg0) end

---@private
---@param arg0 Path
---@return String
function SteamWorkshopItem:validateFileTypes(arg0) end

---@public
---@return ArrayList|Unknown
function SteamWorkshopItem:getTags() end

---@public
---@return ArrayList|Unknown
function SteamWorkshopItem:getAllowedTags() end

---@private
---@param arg0 Path
---@return String
function SteamWorkshopItem:validateModFolder(arg0) end

---@public
---@param arg0 String
---@return void
function SteamWorkshopItem:setDescription(arg0) end

---@public
---@return String
function SteamWorkshopItem:getDescription() end

---@public
---@param arg0 String
---@return void
function SteamWorkshopItem:setChangeNote(arg0) end

---@private
---@param arg0 Path
---@return String
function SteamWorkshopItem:validateMediaFolder(arg0) end

---@public
---@param arg0 KahluaTable
---@return boolean
function SteamWorkshopItem:getUpdateProgress(arg0) end

---@public
---@return int
function SteamWorkshopItem:getUpdateProgressTotal() end

---@private
---@param arg0 Path
---@return String
function SteamWorkshopItem:validateMapsFolder(arg0) end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function SteamWorkshopItem:setTags(arg0) end

---@private
---@param arg0 Path
---@return String
function SteamWorkshopItem:validateModDotInfo(arg0) end

---@public
---@param arg0 String
---@return void
function SteamWorkshopItem:setVisibility(arg0) end

---@private
---@param arg0 Path
---@return String
function SteamWorkshopItem:validateModsFolder(arg0) end

---@public
---@return String
function SteamWorkshopItem:getSubmitDescription() end

---@public
---@param arg0 String
---@return void
function SteamWorkshopItem:setID(arg0) end

---@public
---@return String
function SteamWorkshopItem:getVisibility() end

---@public
---@param arg0 int
---@return void
function SteamWorkshopItem:setVisibilityInteger(arg0) end

---@public
---@return String[]
function SteamWorkshopItem:getSubmitTags() end

---@public
---@return boolean
function SteamWorkshopItem:writeWorkshopTxt() end

---@private
---@param arg0 Path
---@return String
function SteamWorkshopItem:validateMapFolder(arg0) end

---@public
---@param arg0 String
---@return void
function SteamWorkshopItem:setTitle(arg0) end

---@public
---@return String
function SteamWorkshopItem:getID() end

---@public
---@return String
function SteamWorkshopItem:getContentFolder() end

---@public
---@return String
function SteamWorkshopItem:getPreviewImage() end

---@public
---@return String
function SteamWorkshopItem:getTitle() end

---@public
---@return String
function SteamWorkshopItem:getFolderName() end

---@public
---@return int
function SteamWorkshopItem:getVisibilityInteger() end

---@public
---@return boolean
function SteamWorkshopItem:create() end

---@public
---@return String
function SteamWorkshopItem:validateContents() end

---@public
---@return boolean
function SteamWorkshopItem:readWorkshopTxt() end

---@public
---@param arg0 String
---@return String
function SteamWorkshopItem:getExtendedErrorInfo(arg0) end

---@private
---@param arg0 Path
---@return String
function SteamWorkshopItem:validateCreativeFolder(arg0) end

---@private
---@param arg0 Path
---@return String
function SteamWorkshopItem:validateMapDotInfo(arg0) end

---@private
---@param arg0 Path
---@return String
function SteamWorkshopItem:validateBuildingsFolder(arg0) end

---@public
---@return boolean
function SteamWorkshopItem:submitUpdate() end
