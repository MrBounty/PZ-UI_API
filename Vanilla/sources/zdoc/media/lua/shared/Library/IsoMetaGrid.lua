---@class IsoMetaGrid : zombie.iso.IsoMetaGrid
---@field private NUM_LOADER_THREADS int
---@field private s_PreferredZoneTypes ArrayList|Unknown
---@field private s_clipper Clipper
---@field private s_clipperOffset ClipperOffset
---@field private s_clipperBuffer ByteBuffer
---@field a Rectangle
---@field b Rectangle
---@field roomChoices ArrayList|Unknown
---@field private tempRooms ArrayList|Unknown
---@field private tempZones1 ArrayList|Unknown
---@field private tempZones2 ArrayList|Unknown
---@field private threads IsoMetaGrid.MetaGridLoaderThread[]
---@field public minX int
---@field public minY int
---@field public maxX int
---@field public maxY int
---@field public Zones ArrayList|IsoMetaGrid.Zone
---@field public Buildings ArrayList|BuildingDef
---@field public VehiclesZones ArrayList|Unknown
---@field public Grid IsoMetaCell[][]
---@field public MetaCharacters ArrayList|Unknown
---@field HighZombieList ArrayList|Unknown
---@field private width int
---@field private height int
---@field private sharedStrings SharedStrings
---@field private createStartTime long
IsoMetaGrid = {}

---@public
---@param x int
---@param y int
---@param z int
---@param w int
---@param h int
---@return ArrayList|IsoMetaGrid.Zone
---@overload fun(arg0:int, arg1:int, arg2:int, arg3:int, arg4:int, arg5:ArrayList|Unknown)
function IsoMetaGrid:getZonesIntersecting(x, y, z, w, h) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 ArrayList|Unknown
---@return ArrayList|Unknown
function IsoMetaGrid:getZonesIntersecting(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return IsoMetaGrid.VehicleZone
function IsoMetaGrid:getVehicleZoneAt(arg0, arg1, arg2) end

---@public
---@return ArrayList|String
---@overload fun(arg0:String, arg1:ArrayList|Unknown)
function IsoMetaGrid:getLotDirectories() end

---@private
---@param arg0 String
---@param arg1 ArrayList|Unknown
---@return void
function IsoMetaGrid:getLotDirectories(arg0, arg1) end

---@public
---@return int
function IsoMetaGrid:getMaxY() end

---@public
---@param x int
---@param y int
---@param z int
---@return RoomDef
function IsoMetaGrid:getRoomAt(x, y, z) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 int
---@param arg3 String
---@param arg4 KahluaTable
---@param arg5 KahluaTable
---@return IsoMetaGrid.Zone
function IsoMetaGrid:registerGeometryZone(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function IsoMetaGrid:loadZone(arg0, arg1) end

---@public
---@param wx int
---@param wy int
---@return boolean
function IsoMetaGrid:isValidChunk(wx, wy) end

---@public
---@param x int
---@param y int
---@param z int
---@return ArrayList|IsoMetaGrid.Zone
---@overload fun(arg0:int, arg1:int, arg2:int, arg3:ArrayList|Unknown)
function IsoMetaGrid:getZonesAt(x, y, z) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 ArrayList|Unknown
---@return ArrayList|Unknown
function IsoMetaGrid:getZonesAt(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function IsoMetaGrid:AddToMeta(arg0) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 int
---@param arg7 KahluaTable
---@return IsoMetaGrid.Zone
function IsoMetaGrid:registerVehiclesZone(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@param x int
---@param y int
---@param z int
---@return IsoMetaGrid.Zone
function IsoMetaGrid:getZoneAt(x, y, z) end

---@private
---@param arg0 TIntArrayList
---@param arg1 int
---@param arg2 int[]
---@return void
function IsoMetaGrid:calculatePolylineOutlineBounds(arg0, arg1, arg2) end

---@public
---@return IsoMetaChunk
function IsoMetaGrid:getCurrentChunkData() end

---@public
---@param arg0 IsoPlayer
---@return void
function IsoMetaGrid:RemoveFromMeta(arg0) end

---@public
---@return void
function IsoMetaGrid:Dispose() end

---@private
---@param arg0 IsoMetaGrid.Zone
---@return void
function IsoMetaGrid:addZone(arg0) end

---@public
---@param arg0 String
---@return boolean
function IsoMetaGrid:isPreferredZoneForSquare(arg0) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoMetaGrid:savePart(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@param arg1 int
---@return BuildingDef
function IsoMetaGrid:getBuildingAt(arg0, arg1) end

---@public
---@param x int
---@param y int
---@return IsoMetaChunk
function IsoMetaGrid:getChunkDataFromTile(x, y) end

---@public
---@return void
function IsoMetaGrid:Create() end

---@private
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoMetaGrid.Zone
---@return boolean
function IsoMetaGrid:isAdjacent(arg0, arg1) end

---@public
---@return void
function IsoMetaGrid:CreateStep2() end

---@private
---@param arg0 IsoMetaGrid.Zone
---@param arg1 BuildingDef
---@return boolean
function IsoMetaGrid:isInside(arg0, arg1) end

---@public
---@param x float
---@param y float
---@param range int
---@return RoomDef
function IsoMetaGrid:getRandomRoomNotInRange(x, y, range) end

---@public
---@return void
function IsoMetaGrid:CreateStep1() end

---@public
---@param arg0 String
---@return void
function IsoMetaGrid:removeZonesForLotDirectory(arg0) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@return void
function IsoMetaGrid:removeZone(arg0) end

---@public
---@return int
function IsoMetaGrid:getWidth() end

---@public
---@param name String
---@param type String
---@param x int
---@param y int
---@param z int
---@param width int
---@param height int
---@return IsoMetaGrid.Zone
---@overload fun(arg0:String, arg1:String, arg2:int, arg3:int, arg4:int, arg5:int, arg6:int, arg7:IsoMetaGrid.ZoneGeometryType, arg8:TIntArrayList, arg9:int)
function IsoMetaGrid:registerZone(name, type, x, y, z, width, height) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 int
---@param arg7 IsoMetaGrid.ZoneGeometryType
---@param arg8 TIntArrayList
---@param arg9 int
---@return IsoMetaGrid.Zone
function IsoMetaGrid:registerZone(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) end

---@public
---@return void
---@overload fun(output:ByteBuffer)
function IsoMetaGrid:save() end

---@public
---@param output ByteBuffer
---@return void
function IsoMetaGrid:save(output) end

---@public
---@return void
function IsoMetaGrid:processZones() end

---@public
---@return void
function IsoMetaGrid:checkVehiclesZones() end

---@public
---@param wx int
---@param wy int
---@return IsoMetaCell
function IsoMetaGrid:getMetaGridFromTile(wx, wy) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return RoomDef
function IsoMetaGrid:getEmptyOutsideAt(arg0, arg1, arg2) end

---@public
---@return int
function IsoMetaGrid:getMinX() end

---@public
---@param x int
---@param y int
---@return boolean
function IsoMetaGrid:isValidSquare(x, y) end

---@public
---@param cx int
---@param cy int
---@return IsoMetaChunk
function IsoMetaGrid:getChunkData(cx, cy) end

---@public
---@param arg0 int
---@param arg1 int
---@return BuildingDef
function IsoMetaGrid:getBuildingAtRelax(arg0, arg1) end

---@public
---@return void
---@overload fun(input:ByteBuffer)
function IsoMetaGrid:load() end

---@public
---@param input ByteBuffer
---@return void
function IsoMetaGrid:load(input) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 int
---@param arg7 KahluaTable
---@return IsoMetaGrid.Zone
function IsoMetaGrid:registerMannequinZone(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@return void
function IsoMetaGrid:loadZones() end

---@public
---@param x int
---@param y int
---@return IsoMetaCell
function IsoMetaGrid:getCellDataAbs(x, y) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 ArrayList|Unknown
---@return void
function IsoMetaGrid:getRoomsIntersecting(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param name String
---@param type String
---@param x int
---@param y int
---@param z int
---@param width int
---@param height int
---@return IsoMetaGrid.Zone
function IsoMetaGrid:registerZoneNoOverlap(name, type, x, y, z, width, height) end

---@public
---@param arg0 IsoPlayer
---@return int
function IsoMetaGrid:countNearbyBuildingsRooms(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function IsoMetaGrid:removeZonesForCell(arg0, arg1) end

---@public
---@return JVector2
function IsoMetaGrid:getRandomIndoorCoord() end

---@public
---@param x float
---@param y float
---@param min float
---@param max float
---@return RoomDef
function IsoMetaGrid:getRandomRoomBetweenRange(x, y, min, max) end

---@public
---@return IsoMetaCell
function IsoMetaGrid:getCurrentCellData() end

---@public
---@return int
function IsoMetaGrid:getHeight() end

---@public
---@return int
function IsoMetaGrid:getMaxX() end

---@public
---@return int
function IsoMetaGrid:getMinY() end

---@public
---@param arg0 ByteBuffer
---@return void
function IsoMetaGrid:saveZone(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return int
function IsoMetaGrid:countRoomsIntersecting(arg0, arg1, arg2, arg3) end

---@public
---@param x int
---@param y int
---@return IsoMetaCell
function IsoMetaGrid:getCellData(x, y) end
