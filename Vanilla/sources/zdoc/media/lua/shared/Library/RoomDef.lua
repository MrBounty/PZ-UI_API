---@class RoomDef : zombie.iso.RoomDef
---@field private squareChoices ArrayList|Unknown
---@field public bExplored boolean
---@field public bDoneSpawn boolean
---@field public IndoorZombies int
---@field public spawnCount int
---@field public bLightsActive boolean
---@field public name String
---@field public level int
---@field public building BuildingDef
---@field public ID int
---@field public rects ArrayList|RoomDef.RoomRect
---@field public objects ArrayList|MetaObject
---@field public x int
---@field public y int
---@field public x2 int
---@field public y2 int
---@field public area int
---@field private proceduralSpawnedContainer HashMap|Unknown|Unknown
---@field private roofFixed boolean
RoomDef = {}

---@public
---@param def BuildingDef
---@return void
function RoomDef:setBuilding(def) end

---@public
---@return HashMap|Unknown|Unknown
function RoomDef:getProceduralSpawnedContainer() end

---@public
---@return int
function RoomDef:getY2() end

---@public
---@return int
function RoomDef:getX() end

---@public
---@return void
function RoomDef:refreshSquares() end

---@public
---@return boolean
function RoomDef:isEmptyOutside() end

---@public
---@param arg0 Predicate|Unknown
---@return IsoGridSquare
function RoomDef:getRandomSquare(arg0) end

---@public
---@param arg0 boolean
---@return void
function RoomDef:setRoofFixed(arg0) end

---@public
---@param arg0 IsoChunk
---@return float
---@overload fun(arg0:int, arg1:int, arg2:int, arg3:int)
function RoomDef:getAreaOverlapping(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return float
function RoomDef:getAreaOverlapping(arg0, arg1, arg2, arg3) end

---@public
---@return int
function RoomDef:getID() end

---@public
---@return ArrayList|MetaObject
function RoomDef:getObjects() end

---@public
---@return int
function RoomDef:getZ() end

---@public
---@param arg0 boolean
---@return void
function RoomDef:setExplored(arg0) end

---@public
---@return int
function RoomDef:getW() end

---@public
---@param x int
---@param y int
---@param z int
---@return boolean
function RoomDef:isInside(x, y, z) end

---@public
---@param arg0 BiConsumer|Unknown|Unknown
---@return void
function RoomDef:forEachChunk(arg0) end

---@public
---@return int
function RoomDef:getArea() end

---@public
---@return void
function RoomDef:Dispose() end

---@public
---@return ArrayList|MetaObject
function RoomDef:getMetaObjects() end

---@public
---@return IsoRoom
function RoomDef:getIsoRoom() end

---@public
---@return void
function RoomDef:CalculateBounds() end

---@public
---@return boolean
function RoomDef:isExplored() end

---@public
---@return BuildingDef
function RoomDef:getBuilding() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return boolean
function RoomDef:intersects(arg0, arg1, arg2, arg3) end

---@public
---@return ArrayList|RoomDef.RoomRect
function RoomDef:getRects() end

---@public
---@return int
function RoomDef:getX2() end

---@public
---@return IsoGridSquare
function RoomDef:getFreeSquare() end

---@public
---@return boolean
function RoomDef:isRoofFixed() end

---@public
---@return String
function RoomDef:getName() end

---@public
---@return int
function RoomDef:getH() end

---@public
---@return int
function RoomDef:getY() end
