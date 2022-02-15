---@class IsoMetaCell : zombie.iso.IsoMetaCell
---@field public vehicleZones ArrayList|Unknown
---@field public ChunkMap IsoMetaChunk[]
---@field public info LotHeader
---@field public triggers ArrayList|IsoMetaGrid.Trigger
---@field private wx int
---@field private wy int
---@field public mannequinZones ArrayList|Unknown
IsoMetaCell = {}

---@public
---@return void
function IsoMetaCell:checkTriggers() end

---@public
---@param x int
---@param y int
---@return IsoMetaChunk
function IsoMetaCell:getChunk(x, y) end

---@public
---@param arg0 Set|Unknown
---@return void
function IsoMetaCell:getZonesUnique(arg0) end

---@public
---@param x int
---@param y int
---@param z int
---@param w int
---@param h int
---@param result ArrayList|IsoMetaGrid.Zone
---@return void
function IsoMetaCell:getZonesIntersecting(x, y, z, w, h, result) end

---@public
---@return void
function IsoMetaCell:Dispose() end

---@public
---@param room RoomDef
---@param cellX int
---@param cellY int
---@return void
function IsoMetaCell:addRoom(room, cellX, cellY) end

---@public
---@param def BuildingDef
---@param triggerRange int
---@param zombieExclusionRange int
---@param type String
---@return void
function IsoMetaCell:addTrigger(def, triggerRange, zombieExclusionRange, type) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 ArrayList|Unknown
---@return void
function IsoMetaCell:getRoomsIntersecting(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param zone IsoMetaGrid.Zone
---@param cellX int
---@param cellY int
---@return void
function IsoMetaCell:addZone(zone, cellX, cellY) end

---@public
---@param zone IsoMetaGrid.Zone
---@return void
function IsoMetaCell:removeZone(zone) end
