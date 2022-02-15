---@class BodyLocation : zombie.characters.WornItems.BodyLocation
---@field protected group BodyLocationGroup
---@field protected id String
---@field protected aliases ArrayList|Unknown
---@field protected exclusive ArrayList|Unknown
---@field protected hideModel ArrayList|Unknown
---@field protected bMultiItem boolean
BodyLocation = {}

---@public
---@param arg0 String
---@return BodyLocation
function BodyLocation:setHideModel(arg0) end

---@private
---@param arg0 String
---@param arg1 String
---@return void
function BodyLocation:checkId(arg0, arg1) end

---@public
---@return String
function BodyLocation:getId() end

---@public
---@param arg0 boolean
---@return BodyLocation
function BodyLocation:setMultiItem(arg0) end

---@public
---@param arg0 String
---@return boolean
function BodyLocation:isHideModel(arg0) end

---@public
---@return boolean
function BodyLocation:isMultiItem() end

---@public
---@param arg0 String
---@return boolean
function BodyLocation:isExclusive(arg0) end

---@public
---@param arg0 String
---@return BodyLocation
function BodyLocation:setExclusive(arg0) end

---@public
---@param arg0 String
---@return BodyLocation
function BodyLocation:addAlias(arg0) end

---@public
---@param arg0 String
---@return boolean
function BodyLocation:isID(arg0) end
