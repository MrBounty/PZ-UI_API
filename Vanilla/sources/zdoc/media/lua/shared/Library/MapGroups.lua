---@class MapGroups : zombie.MapGroups
---@field private groups ArrayList|Unknown
---@field private realDirectories ArrayList|Unknown
MapGroups = {}

---@private
---@param arg0 String
---@return ArrayList|Unknown
function MapGroups:getLotDirectories(arg0) end

---@public
---@return boolean
function MapGroups:checkMapConflicts() end

---@public
---@param arg0 String
---@return String
function MapGroups:addMissingVanillaDirectories(arg0) end

---@public
---@return void
---@overload fun(arg0:ActiveMods, arg1:boolean)
---@overload fun(arg0:ActiveMods, arg1:boolean, arg2:boolean)
function MapGroups:createGroups() end

---@public
---@param arg0 ActiveMods
---@param arg1 boolean
---@return void
function MapGroups:createGroups(arg0, arg1) end

---@public
---@param arg0 ActiveMods
---@param arg1 boolean
---@param arg2 boolean
---@return void
function MapGroups:createGroups(arg0, arg1, arg2) end

---@private
---@param arg0 String
---@param arg1 String
---@return void
function MapGroups:handleMapDirectory(arg0, arg1) end

---@private
---@param arg0 boolean
---@return ArrayList|Unknown
function MapGroups:getVanillaMapDirectories(arg0) end

---@public
---@return ArrayList|Unknown
function MapGroups:getAllMapsInOrder() end

---@private
---@param arg0 ArrayList|Unknown
---@return MapGroups.MapGroup
function MapGroups:findGroupWithAnyOfTheseDirectories(arg0) end

---@public
---@param arg0 int
---@return void
function MapGroups:setWorld(arg0) end

---@private
---@param arg0 MapGroups.MapDirectory
---@param arg1 ArrayList|Unknown
---@return void
function MapGroups:getDirsRecursively(arg0, arg1) end

---@public
---@return int
function MapGroups:getNumberOfGroups() end

---@public
---@param arg0 int
---@return ArrayList|Unknown
function MapGroups:getMapDirectoriesInGroup(arg0) end

---@public
---@param arg0 String
---@return ArrayList|Unknown
function MapGroups:getMapConflicts(arg0) end
