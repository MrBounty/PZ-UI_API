---@class BuildingDef : zombie.iso.BuildingDef
---@field squareChoices ArrayList|Unknown
---@field public emptyoutside ArrayList|Unknown
---@field public _table KahluaTable
---@field public seen boolean
---@field public hasBeenVisited boolean
---@field public stash String
---@field public lootRespawnHour int
---@field public overlappedChunks TShortArrayList
---@field public bAlarmed boolean
---@field public x int
---@field public y int
---@field public x2 int
---@field public y2 int
---@field public rooms ArrayList|RoomDef
---@field public zone IsoMetaGrid.Zone
---@field public food int
---@field public items ArrayList|Unknown
---@field public itemTypes HashSet|Unknown
---@field ID int
---@field private keySpawned int
---@field private keyId int
BuildingDef = {}

---@public
---@return int
function BuildingDef:getY2() end

---@public
---@return KahluaTable
function BuildingDef:getTable() end

---@public
---@return boolean
function BuildingDef:isAlarmed() end

---@public
---@param keyId int
---@return void
function BuildingDef:setKeyId(keyId) end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function BuildingDef:CalculateBounds(arg0) end

---@public
---@return IsoMetaGrid.Zone
function BuildingDef:getZone() end

---@public
---@return void
function BuildingDef:refreshSquares() end

---@public
---@param arg0 String
---@return RoomDef
function BuildingDef:getRoom(arg0) end

---@public
---@return int
function BuildingDef:getX() end

---@public
---@param keySpawned int
---@return void
function BuildingDef:setKeySpawned(keySpawned) end

---@public
---@return int
function BuildingDef:getChunkX() end

---@public
---@return RoomDef
function BuildingDef:getFirstRoom() end

---@public
---@return ArrayList|RoomDef
function BuildingDef:getRooms() end

---@public
---@return int
function BuildingDef:getID() end

---@public
---@param arg0 String
---@return boolean
function BuildingDef:containsRoom(arg0) end

---@public
---@return boolean
function BuildingDef:isAllExplored() end

---@public
---@return int
function BuildingDef:getKeyId() end

---@public
---@return int
function BuildingDef:getW() end

---@public
---@param b boolean
---@return void
function BuildingDef:setAllExplored(b) end

---@public
---@return void
function BuildingDef:Dispose() end

---@public
---@return void
function BuildingDef:recalculate() end

---@public
---@param arg0 boolean
---@return void
function BuildingDef:setAlarmed(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@return boolean
function BuildingDef:overlapsChunk(arg0, arg1) end

---@public
---@param arg0 int
---@return RoomDef
function BuildingDef:getRandomRoom(arg0) end

---@public
---@return boolean
function BuildingDef:isHasBeenVisited() end

---@public
---@return IsoGridSquare
function BuildingDef:getFreeSquareInRoom() end

---@public
---@return int
function BuildingDef:getX2() end

---@public
---@return boolean
function BuildingDef:isFullyStreamedIn() end

---@public
---@return int
function BuildingDef:getKeySpawned() end

---@public
---@param arg0 boolean
---@return void
function BuildingDef:setHasBeenVisited(arg0) end

---@public
---@return int
function BuildingDef:getH() end

---@public
---@return int
function BuildingDef:getY() end

---@public
---@return int
function BuildingDef:getChunkY() end
