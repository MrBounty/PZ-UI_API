---@class IsoMetaChunk : zombie.iso.IsoMetaChunk
---@field public zombiesMinPerChunk float
---@field public zombiesFullPerChunk float
---@field private ZombieIntensity int
---@field private zones IsoMetaGrid.Zone[]
---@field private zonesSize int
---@field private rooms RoomDef[]
---@field private roomsSize int
IsoMetaChunk = {}

---@public
---@return float
---@overload fun(arg0:boolean)
function IsoMetaChunk:getZombieIntensity() end

---@public
---@param arg0 boolean
---@return float
function IsoMetaChunk:getZombieIntensity(arg0) end

---@public
---@param zombieIntensity int
---@return void
function IsoMetaChunk:setZombieIntensity(zombieIntensity) end

---@public
---@return int
function IsoMetaChunk:getUnadjustedZombieIntensity() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 ArrayList|Unknown
---@return void
function IsoMetaChunk:getRoomsIntersecting(arg0, arg1, arg2, arg3, arg4) end

---@public
---@return int
function IsoMetaChunk:getNumRooms() end

---@public
---@param x int
---@param y int
---@param z int
---@param result ArrayList|IsoMetaGrid.Zone
---@return ArrayList|IsoMetaGrid.Zone
function IsoMetaChunk:getZonesAt(x, y, z, result) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return RoomDef
function IsoMetaChunk:getEmptyOutsideAt(arg0, arg1, arg2) end

---@public
---@return float
function IsoMetaChunk:getLootZombieIntensity() end

---@public
---@param zone IsoMetaGrid.Zone
---@return void
function IsoMetaChunk:removeZone(zone) end

---@public
---@param room RoomDef
---@return void
function IsoMetaChunk:addRoom(room) end

---@public
---@param x int
---@param y int
---@param z int
---@return IsoMetaGrid.Zone
function IsoMetaChunk:getZoneAt(x, y, z) end

---@public
---@param x int
---@param y int
---@param z int
---@param w int
---@param h int
---@param result ArrayList|IsoMetaGrid.Zone
---@return void
function IsoMetaChunk:getZonesIntersecting(x, y, z, w, h, result) end

---@public
---@return int
function IsoMetaChunk:numZones() end

---@public
---@return void
function IsoMetaChunk:clearZones() end

---@public
---@param arg0 Set|Unknown
---@return void
function IsoMetaChunk:getZonesUnique(arg0) end

---@public
---@param x int
---@param y int
---@param z int
---@return RoomDef
function IsoMetaChunk:getRoomAt(x, y, z) end

---@public
---@return void
function IsoMetaChunk:clearRooms() end

---@public
---@return void
function IsoMetaChunk:Dispose() end

---@public
---@param arg0 int
---@return IsoMetaGrid.Zone
function IsoMetaChunk:getZone(arg0) end

---@public
---@param zone IsoMetaGrid.Zone
---@return void
function IsoMetaChunk:addZone(zone) end
