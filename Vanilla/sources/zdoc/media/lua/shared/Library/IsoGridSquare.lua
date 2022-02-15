---@class IsoGridSquare : zombie.iso.IsoGridSquare
---@field private hasTree boolean
---@field private LightInfluenceB ArrayList|Unknown
---@field private LightInfluenceG ArrayList|Unknown
---@field private LightInfluenceR ArrayList|Unknown
---@field public nav IsoGridSquare[]
---@field public collideMatrix int
---@field public pathMatrix int
---@field public visionMatrix int
---@field public room IsoRoom
---@field public w IsoGridSquare
---@field public nw IsoGridSquare
---@field public sw IsoGridSquare
---@field public s IsoGridSquare
---@field public n IsoGridSquare
---@field public ne IsoGridSquare
---@field public se IsoGridSquare
---@field public e IsoGridSquare
---@field public haveSheetRope boolean
---@field private isoWorldRegion IWorldRegion
---@field private hasSetIsoWorldRegion boolean
---@field public ObjectsSyncCount int
---@field public roofHideBuilding IsoBuilding
---@field public bFlattenGrassEtc boolean
---@field private VisiFlagTimerPeriod_ms long
---@field private playerCutawayFlags boolean[]
---@field private playerCutawayFlagLockUntilTimes long[]
---@field private targetPlayerCutawayFlags boolean[]
---@field private playerIsDissolvedFlags boolean[]
---@field private playerIsDissolvedFlagLockUntilTimes long[]
---@field private targetPlayerIsDissolvedFlags boolean[]
---@field private water IsoWaterGeometry
---@field private puddles IsoPuddlesGeometry
---@field private puddlesCacheSize float
---@field private puddlesCacheLevel float
---@field public lighting IsoGridSquare.ILighting[]
---@field public x int
---@field public y int
---@field public z int
---@field private CachedScreenValue int
---@field public CachedScreenX float
---@field public CachedScreenY float
---@field private torchTimer long
---@field public SolidFloorCached boolean
---@field public SolidFloor boolean
---@field private CacheIsFree boolean
---@field private CachedIsFree boolean
---@field public chunk IsoChunk
---@field public roomID int
---@field public ID Integer
---@field public zone IsoMetaGrid.Zone
---@field private DeferedCharacters ArrayList|Unknown
---@field private DeferredCharacterTick int
---@field private StaticMovingObjects ArrayList|Unknown
---@field private MovingObjects ArrayList|Unknown
---@field protected Objects PZArrayList|Unknown
---@field protected localTemporaryObjects PZArrayList|Unknown
---@field private WorldObjects ArrayList|Unknown
---@field hasTypes ZomboidBitFlag
---@field private Properties PropertyContainer
---@field private SpecialObjects ArrayList|Unknown
---@field public haveRoof boolean
---@field private burntOut boolean
---@field private bHasFlies boolean
---@field private OcclusionDataCache IsoGridOcclusionData
---@field public isoGridSquareCache ConcurrentLinkedQueue|Unknown
---@field public loadGridSquareCache ArrayDeque|Unknown
---@field private overlayDone boolean
---@field private _table KahluaTable
---@field private trapPositionX int
---@field private trapPositionY int
---@field private trapPositionZ int
---@field private haveElectricity boolean
---@field public gridSquareCacheEmptyTimer int
---@field private darkStep float
---@field public RecalcLightTime int
---@field private lightcache int
---@field public choices ArrayList|IsoGridSquare
---@field public USE_WALL_SHADER boolean
---@field private cutawayY int
---@field private cutawayNWWidth int
---@field private cutawayNWHeight int
---@field private cutawaySEXCut int
---@field private cutawaySEXUncut int
---@field private cutawaySEWidth int
---@field private cutawaySEHeight int
---@field private cutawayNXFullyCut int
---@field private cutawayNXCutW int
---@field private cutawayNXUncut int
---@field private cutawayNXCutE int
---@field private cutawayWXFullyCut int
---@field private cutawayWXCutS int
---@field private cutawayWXUncut int
---@field private cutawayWXCutN int
---@field private cutawayFenceXOffset int
---@field private cutawayLogWallXOffset int
---@field private cutawaySpiffoWindowXOffset int
---@field private cutawayRoof4XOffset int
---@field private cutawayRoof17XOffset int
---@field private cutawayRoof28XOffset int
---@field private cutawayRoof41XOffset int
---@field private lightInfoTemp ColorInfo
---@field private doorWindowCutawayLightMin float
---@field private bWallCutawayW boolean
---@field private bWallCutawayN boolean
---@field public isSolidFloorCache boolean
---@field public isExteriorCache boolean
---@field public isVegitationCache boolean
---@field public hourLastSeen int
---@field lastLoaded IsoGridSquare
---@field public IDMax int
---@field col int
---@field path int
---@field pathdoor int
---@field vision int
---@field public hashCodeObjects long
---@field tr Color
---@field tl Color
---@field br Color
---@field bl Color
---@field interp1 Color
---@field interp2 Color
---@field finalCol Color
---@field public cellGetSquare IsoGridSquare.CellGetSquare
---@field public propertiesDirty boolean
---@field public UseSlowCollision boolean
---@field private bDoSlowPathfinding boolean
---@field private comp Comparator|Unknown
---@field public isOnScreenLast boolean
---@field private splashX float
---@field private splashY float
---@field private splashFrame float
---@field private splashFrameNum int
---@field public lightInfo ColorInfo[]
---@field rainsplashCache String[]
---@field private defColorInfo ColorInfo
---@field private blackColorInfo ColorInfo
---@field colu int
---@field coll int
---@field colr int
---@field colu2 int
---@field coll2 int
---@field colr2 int
---@field public CircleStencil boolean
---@field public rmod float
---@field public gmod float
---@field public bmod float
---@field tempo JVector2
---@field tempo2 JVector2
---@field private RainDrop IsoRaindrop
---@field private RainSplash IsoRainSplash
---@field private erosion ErosionData.Square
---@field public WALL_TYPE_N int
---@field public WALL_TYPE_S int
---@field public WALL_TYPE_W int
---@field public WALL_TYPE_E int
IsoGridSquare = {}

---@public
---@param arg0 ArrayDeque|Unknown
---@param arg1 int
---@return void
function IsoGridSquare:getSquaresForThread(arg0, arg1) end

---@public
---@return int
function IsoGridSquare:getRoomID() end

---@public
---@param CachedIsFree boolean @the CachedIsFree to set
---@return void
function IsoGridSquare:setCachedIsFree(CachedIsFree) end

---@public
---@param next IsoGridSquare
---@return IsoObject
function IsoGridSquare:getDoorFrameTo(next) end

---@public
---@param arg0 int
---@param arg1 long
---@return boolean
function IsoGridSquare:getIsDissolved(arg0, arg1) end

---@public
---@return KahluaTable
function IsoGridSquare:getLuaMovingObjectList() end

---@public
---@param arg0 IsoObject
---@param arg1 int
---@param arg2 boolean
---@param arg3 boolean
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 boolean
---@param arg7 boolean
---@param arg8 boolean
---@param arg9 Shader
---@return int
function IsoGridSquare:DoWallLightingNW(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) end

---@public
---@return boolean
function IsoGridSquare:HasSlopedRoofNorth() end

---@public
---@return IsoGenerator
function IsoGridSquare:getGenerator() end

---@private
---@param arg0 IsoObject
---@param arg1 boolean
---@return boolean
function IsoGridSquare:isWindowOrWindowFrame(arg0, arg1) end

---@public
---@return int
function IsoGridSquare:getHourLastSeen() end

---@public
---@return int
function IsoGridSquare:getHashCodeObjectsInt() end

---@public
---@return IsoBuilding
function IsoGridSquare:getBuilding() end

---@public
---@return void
function IsoGridSquare:ClearTileObjectsExceptFloor() end

---@public
---@param trapPositionX int
---@return void
function IsoGridSquare:setTrapPositionX(trapPositionX) end

---@public
---@return String
function IsoGridSquare:getZoneType() end

---Get the IsoWindow window between this grid and the next in parameter
---@public
---@param next IsoGridSquare
---@return IsoWindow
function IsoGridSquare:getWindowTo(next) end

---@public
---@param north boolean
---@param level int
---@param sprite String
---@param pillarSprite String
---@param _table KahluaTable
---@return IsoThumpable
function IsoGridSquare:AddStairs(north, level, sprite, pillarSprite, _table) end

---@public
---@return PZArrayList|Unknown
function IsoGridSquare:getObjects() end

---@public
---@param arg0 int
---@param arg1 float
---@return void
function IsoGridSquare:setTargetDarkMulti(arg0, arg1) end

---@public
---@return boolean
function IsoGridSquare:isOverlayDone() end

---@public
---@param SolidFloorCached boolean @the SolidFloorCached to set
---@return void
function IsoGridSquare:setSolidFloorCached(SolidFloorCached) end

---@public
---@param arg0 int
---@param arg1 boolean
---@return void
function IsoGridSquare:setCouldSee(arg0, arg1) end

---@public
---@param other IsoGridSquare
---@return boolean
function IsoGridSquare:isDoorTo(other) end

---@public
---@param sq IsoGridSquare
---@return float
---@overload fun(other:IsoMovingObject)
---@overload fun(x:int, y:int)
function IsoGridSquare:DistTo(sq) end

---@public
---@param other IsoMovingObject
---@return float
function IsoGridSquare:DistTo(other) end

---@public
---@param x int
---@param y int
---@return float
function IsoGridSquare:DistTo(x, y) end

---@public
---@param north boolean
---@return boolean
function IsoGridSquare:hasBlockedDoor(north) end

---@public
---@return ArrayList|IsoGameCharacter @the DeferedCharacters
function IsoGridSquare:getDeferedCharacters() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return boolean
function IsoGridSquare:getCollideMatrix(arg0, arg1, arg2) end

---@public
---@return void
---@overload fun(arg0:boolean)
function IsoGridSquare:Burn() end

---@public
---@param arg0 boolean
---@return void
function IsoGridSquare:Burn(arg0) end

---@public
---@param drop IsoRaindrop
---@return void
function IsoGridSquare:setRainDrop(drop) end

---@public
---@return int
function IsoGridSquare:getTrapPositionY() end

---@public
---@param arg0 int
---@param arg1 byte
---@param arg2 byte
---@param arg3 byte
---@return boolean
---@overload fun(arg0:int, arg1:int, arg2:int, arg3:int)
function IsoGridSquare:getMatrixBit(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return boolean
function IsoGridSquare:getMatrixBit(arg0, arg1, arg2, arg3) end

---@public
---@return boolean @the SolidFloor
function IsoGridSquare:isSolidFloor() end

---@private
---@param arg0 Shader
---@return int
function IsoGridSquare:renderFloorInternal(arg0) end

---@public
---@return ArrayList|IsoMovingObject @the MovingObjects
function IsoGridSquare:getMovingObjects() end

---@public
---@return int @the lightcache
function IsoGridSquare:getLightcache() end

---@public
---@return int @the RecalcLightTime
function IsoGridSquare:getRecalcLightTime() end

---@param arg0 IsoGridSquare
---@return void
---@overload fun(arg0:IsoGridSquare, arg1:IsoGridSquare.GetSquare)
---@overload fun(arg0:boolean, arg1:IsoGridSquare, arg2:IsoGridSquare.GetSquare)
function IsoGridSquare:ReCalculateAll(arg0) end

---@param arg0 IsoGridSquare
---@param arg1 IsoGridSquare.GetSquare
---@return void
function IsoGridSquare:ReCalculateAll(arg0, arg1) end

---@param arg0 boolean
---@param arg1 IsoGridSquare
---@param arg2 IsoGridSquare.GetSquare
---@return void
function IsoGridSquare:ReCalculateAll(arg0, arg1, arg2) end

---@public
---@param z int @the z to set
---@return void
function IsoGridSquare:setZ(z) end

---@public
---@param item InventoryItem
---@param x float
---@param y float
---@param height float
---@return InventoryItem
---@overload fun(String:String, x:float, y:float, height:float)
---@overload fun(arg0:InventoryItem, arg1:float, arg2:float, arg3:float, arg4:boolean)
---@overload fun(arg0:String, arg1:float, arg2:float, arg3:float, arg4:int)
function IsoGridSquare:AddWorldInventoryItem(item, x, y, height) end

---@public
---@param String String
---@param x float
---@param y float
---@param height float
---@return InventoryItem
function IsoGridSquare:AddWorldInventoryItem(String, x, y, height) end

---@public
---@param arg0 InventoryItem
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 boolean
---@return InventoryItem
function IsoGridSquare:AddWorldInventoryItem(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 String
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 int
---@return void
function IsoGridSquare:AddWorldInventoryItem(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param type IsoObjectType
---@return boolean
function IsoGridSquare:Has(type) end

---@public
---@return float
function IsoGridSquare:getTotalWeightOfItemsOnFloor() end

---@public
---@return boolean
function IsoGridSquare:HasStairs() end

---@public
---@return boolean
function IsoGridSquare:isVehicleIntersecting() end

---@public
---@param square IsoGridSquare
---@return void
---@overload fun(square:IsoGridSquare, getter:IsoGridSquare.GetSquare)
function IsoGridSquare:ReCalculateVisionBlocked(square) end

---@public
---@param square IsoGridSquare
---@param getter IsoGridSquare.GetSquare
---@return void
function IsoGridSquare:ReCalculateVisionBlocked(square, getter) end

---@public
---@return ArrayList|IsoObject @the SpecialObjects
function IsoGridSquare:getSpecialObjects() end

---@public
---@param other IsoGridSquare
---@return boolean
function IsoGridSquare:isSomethingTo(other) end

---@public
---@return boolean
function IsoGridSquare:HasSlopedRoof() end

---@public
---@param arg0 int
---@return float
function IsoGridSquare:getDarkMulti(arg0) end

---@public
---@return void
function IsoGridSquare:ClearTileObjects() end

---@public
---@param LightInfluenceB ArrayList|Float @the LightInfluenceB to set
---@return void
function IsoGridSquare:setLightInfluenceB(LightInfluenceB) end

---@public
---@return void
function IsoGridSquare:clearPuddles() end

---@param arg0 IsoGridSquare.GetSquare
---@return void
function IsoGridSquare:setBlockedGridPointers(arg0) end

---@public
---@param arg0 String
---@return boolean
---@overload fun(flag:IsoFlagType)
function IsoGridSquare:Is(arg0) end

---@public
---@param flag IsoFlagType
---@return boolean
function IsoGridSquare:Is(flag) end

---@public
---@return void
function IsoGridSquare:ResetIsoWorldRegion() end

---@public
---@param arg0 IsoGridSquare
---@return IsoObject
function IsoGridSquare:getWindowFrameTo(arg0) end

---@public
---@param north boolean
---@return IsoObject
function IsoGridSquare:getDoor(north) end

---@public
---@return boolean @the SolidFloorCached
function IsoGridSquare:isSolidFloorCached() end

---@return void
function IsoGridSquare:cacheLightInfo() end

---throws java.io.IOException
---@public
---@param output ByteBuffer
---@param outputObj ObjectOutputStream
---@return void
---@overload fun(arg0:ByteBuffer, arg1:ObjectOutputStream, arg2:boolean)
function IsoGridSquare:save(output, outputObj) end

---@public
---@param arg0 ByteBuffer
---@param arg1 ObjectOutputStream
---@param arg2 boolean
---@return void
function IsoGridSquare:save(arg0, arg1, arg2) end

---@param arg0 boolean[][][]
---@param arg1 DataInputStream
---@return void
---@overload fun(arg0:boolean[][][], arg1:ByteBuffer)
---@overload fun(arg0:boolean[][][], arg1:byte[], arg2:int)
function IsoGridSquare:loadmatrix(arg0, arg1) end

---@private
---@param arg0 boolean[][][]
---@param arg1 ByteBuffer
---@return void
function IsoGridSquare:loadmatrix(arg0, arg1) end

---@private
---@param arg0 boolean[][][]
---@param arg1 byte[]
---@param arg2 int
---@return int
function IsoGridSquare:loadmatrix(arg0, arg1, arg2) end

---@public
---@return IsoObject
function IsoGridSquare:getWallSE() end

---@public
---@return float @the lampostTotalR
function IsoGridSquare:getLampostTotalR() end

---@public
---@param arg0 boolean
---@return IsoObject
function IsoGridSquare:getThumpableWall(arg0) end

---@public
---@param i int
---@return boolean
function IsoGridSquare:isSeen(i) end

---@public
---@return int
function IsoGridSquare:getZ() end

---@public
---@param bCountOtherCharacters boolean
---@return boolean
---@overload fun(bCountOtherCharacters:boolean, bDoZombie:boolean)
function IsoGridSquare:isFreeOrMidair(bCountOtherCharacters) end

---@public
---@param bCountOtherCharacters boolean
---@param bDoZombie boolean
---@return boolean
function IsoGridSquare:isFreeOrMidair(bCountOtherCharacters, bDoZombie) end

---@public
---@param arg0 int
---@return float
function IsoGridSquare:getTargetDarkMulti(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return boolean
function IsoGridSquare:isSameStaircase(arg0, arg1, arg2) end

---@public
---@param trapPositionZ int
---@return void
function IsoGridSquare:setTrapPositionZ(trapPositionZ) end

---@public
---@return boolean
function IsoGridSquare:isSolidTrans() end

---@public
---@return void
function IsoGridSquare:DirtySlice() end

---@public
---@return IsoGridSquare @the n
function IsoGridSquare:getN() end

---@public
---@return void
function IsoGridSquare:setCollisionMode() end

---@public
---@return void
function IsoGridSquare:disableErosion() end

---@public
---@return IsoObject
function IsoGridSquare:getFloor() end

---@public
---@param north boolean
---@return IsoObject
function IsoGridSquare:getDoorOrWindow(north) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 boolean
---@return LosUtil.TestResults
function IsoGridSquare:DoDiagnalCheck(arg0, arg1, arg2, arg3) end

---@public
---@return boolean
function IsoGridSquare:isCommonGrass() end

---@public
---@param square IsoGridSquare
---@return void
---@overload fun(square:IsoGridSquare, getter:IsoGridSquare.GetSquare)
function IsoGridSquare:ReCalculatePathFind(square) end

---@public
---@param square IsoGridSquare
---@param getter IsoGridSquare.GetSquare
---@return void
function IsoGridSquare:ReCalculatePathFind(square, getter) end

---@public
---@return boolean
function IsoGridSquare:haveFire() end

---@private
---@param arg0 IsoObject
---@param arg1 int
---@param arg2 boolean
---@param arg3 boolean
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 boolean
---@param arg7 boolean
---@param arg8 boolean
---@param arg9 boolean
---@param arg10 boolean
---@param arg11 IsoObjectType
---@param arg12 IsoObjectType
---@param arg13 boolean
---@param arg14 IsoFlagType
---@param arg15 IsoFlagType
---@param arg16 IsoFlagType
---@param arg17 IsoDirections
---@param arg18 boolean
---@param arg19 WallShaperWhole
---@param arg20 Shader
---@return int
function IsoGridSquare:performDrawWallSegmentSingle(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20) end

---@public
---@param roomID int
---@return void
function IsoGridSquare:setRoomID(roomID) end

---@public
---@return IsoDoor
function IsoGridSquare:getIsoDoor() end

---@public
---@return IsoRainSplash
function IsoGridSquare:getRainSplash() end

---@public
---@return IsoWindow
---@overload fun(north:boolean)
function IsoGridSquare:getWindow() end

---@public
---@param north boolean
---@return IsoWindow
function IsoGridSquare:getWindow(north) end

---@public
---@return ArrayList|Float @the LightInfluenceG
function IsoGridSquare:getLightInfluenceG() end

---@private
---@param arg0 String
---@param arg1 String
---@return boolean
function IsoGridSquare:validateUser(arg0, arg1) end

---@public
---@return boolean
function IsoGridSquare:HasPushable() end

---@public
---@param other IsoGridSquare
---@return boolean
function IsoGridSquare:isWallTo(other) end

---@public
---@param obj IsoObject
---@return void
function IsoGridSquare:DeleteTileObject(obj) end

---@public
---@param arg0 IsoGridSquare
---@return IsoObject
---@overload fun(arg0:IsoMovingObject, arg1:IsoGridSquare, arg2:IsoGridSquare)
function IsoGridSquare:testCollideSpecialObjects(arg0) end

---@private
---@param arg0 IsoMovingObject
---@param arg1 IsoGridSquare
---@param arg2 IsoGridSquare
---@return boolean
function IsoGridSquare:testCollideSpecialObjects(arg0, arg1, arg2) end

---Get the door between this grid and the next in parameter
---@public
---@param next IsoGridSquare
---@return IsoObject
function IsoGridSquare:getDoorTo(next) end

---@public
---@param arg0 IsoSprite
---@param arg1 IsoDirections
---@param arg2 boolean
---@param arg3 boolean
---@param arg4 boolean
---@return void
function IsoGridSquare:DoCutawayShaderSprite(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param s IsoGridSquare @the s to set
---@return void
function IsoGridSquare:setS(s) end

---@public
---@param inf ColorInfo
---@param x float
---@param y float
---@return void
function IsoGridSquare:interpolateLight(inf, x, y) end

---@public
---@return boolean
---@overload fun(sq:IsoGridSquare, depth:int)
function IsoGridSquare:isSafeToSpawn() end

---@public
---@param sq IsoGridSquare
---@param depth int
---@return void
function IsoGridSquare:isSafeToSpawn(sq, depth) end

---@public
---@return ArrayList|IsoWorldInventoryObject
function IsoGridSquare:getWorldObjects() end

---@public
---@return float @the lampostTotalG
function IsoGridSquare:getLampostTotalG() end

---@public
---@param dist int
---@param alpha float
---@return void
function IsoGridSquare:splatBlood(dist, alpha) end

---@public
---@param lampostTotalR float @the lampostTotalR to set
---@return void
function IsoGridSquare:setLampostTotalR(lampostTotalR) end

---@public
---@param LightInfluenceR ArrayList|Float @the LightInfluenceR to set
---@return void
function IsoGridSquare:setLightInfluenceR(LightInfluenceR) end

---@public
---@param other IsoGridSquare
---@return boolean
function IsoGridSquare:isDoorBlockedTo(other) end

---@public
---@return IsoRoom @the room
function IsoGridSquare:getRoom() end

---@public
---@param obj IsoObject
---@return void
---@overload fun(arg0:IsoObject, arg1:int)
function IsoGridSquare:AddSpecialObject(obj) end

---@public
---@param arg0 IsoObject
---@param arg1 int
---@return void
function IsoGridSquare:AddSpecialObject(arg0, arg1) end

---@public
---@param square IsoGridSquare
---@return void
---@overload fun(square:IsoGridSquare, getter:IsoGridSquare.GetSquare)
function IsoGridSquare:ReCalculateCollide(square) end

---@public
---@param square IsoGridSquare
---@param getter IsoGridSquare.GetSquare
---@return void
function IsoGridSquare:ReCalculateCollide(square, getter) end

---@public
---@param arg0 IsoDirections
---@return IsoGridSquare
function IsoGridSquare:getAdjacentSquare(arg0) end

---@public
---@return boolean
function IsoGridSquare:HasStairsWest() end

---@public
---@return boolean
function IsoGridSquare:HasStairsBelow() end

---@public
---@return BaseVehicle
function IsoGridSquare:getVehicleContainer() end

---@public
---@param obj IsoObject
---@return void
function IsoGridSquare:transmitRemoveItemFromSquareOnServer(obj) end

---@public
---@param username String
---@param pw char[]
---@return boolean
function IsoGridSquare:auth(username, pw) end

---@private
---@return IsoObject
function IsoGridSquare:getSpecialSolid() end

---@public
---@param i int
---@param col int
---@param playerIndex int
---@return void
function IsoGridSquare:setVertLight(i, col, playerIndex) end

---@public
---@return List|IsoDeadBody
function IsoGridSquare:getDeadBodys() end

---@public
---@param arg0 ColorInfo
---@return void
function IsoGridSquare:setLightInfoServerGUIOnly(arg0) end

---@public
---@param north boolean
---@return boolean
function IsoGridSquare:hasBlockedWindow(north) end

---@public
---@return ErosionData.Square
function IsoGridSquare:getErosionData() end

---@public
---@return int
function IsoGridSquare:getTrapPositionZ() end

---@public
---@param bCountOtherCharacters boolean
---@return boolean
function IsoGridSquare:isFree(bCountOtherCharacters) end

---@public
---@return int
function IsoGridSquare:getY() end

---@return IsoObject
---@overload fun(bNorth:boolean)
function IsoGridSquare:getWall() end

---@public
---@param bNorth boolean
---@return IsoObject
function IsoGridSquare:getWall(bNorth) end

---@public
---@param other IsoGridSquare
---@return boolean
function IsoGridSquare:isWindowBlockedTo(other) end

---@public
---@param arg0 int
---@return boolean
function IsoGridSquare:getSeen(arg0) end

---@public
---@param arg0 int
---@param arg1 boolean
---@param arg2 boolean
---@return void
function IsoGridSquare:renderCharacters(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@param arg1 long
---@return boolean
function IsoGridSquare:getPlayerCutawayFlag(arg0, arg1) end

---@public
---@return IsoBrokenGlass
function IsoGridSquare:getBrokenGlass() end

---@public
---@return IsoPlayer
function IsoGridSquare:getPlayer() end

---@public
---@return boolean
function IsoGridSquare:haveBlood() end

---@public
---@param gridSquare IsoGridSquare
---@return boolean
---@overload fun(gridSquare:IsoGridSquare, getter:IsoGridSquare.GetSquare)
function IsoGridSquare:CalculateVisionBlocked(gridSquare) end

---@public
---@param gridSquare IsoGridSquare
---@param getter IsoGridSquare.GetSquare
---@return boolean
function IsoGridSquare:CalculateVisionBlocked(gridSquare, getter) end

---@public
---@return ZomboidBitFlag
function IsoGridSquare:getHasTypes() end

---@public
---@param arg0 int
---@return void
function IsoGridSquare:checkRoomSeen(arg0) end

---@param arg0 boolean[][][]
---@param arg1 DataOutputStream
---@return void
---@overload fun(arg0:boolean[][][], arg1:ByteBuffer)
---@overload fun(arg0:boolean[][][], arg1:byte[], arg2:int)
function IsoGridSquare:savematrix(arg0, arg1) end

---@private
---@param arg0 boolean[][][]
---@param arg1 ByteBuffer
---@return void
function IsoGridSquare:savematrix(arg0, arg1) end

---@private
---@param arg0 boolean[][][]
---@param arg1 byte[]
---@param arg2 int
---@return int
function IsoGridSquare:savematrix(arg0, arg1, arg2) end

---@public
---@param mover IsoMovingObject
---@param x int
---@param y int
---@param z int
---@return boolean
---@overload fun(mover:IsoMovingObject, x:int, y:int, z:int, getter:IsoGridSquare.GetSquare)
function IsoGridSquare:testPathFindAdjacent(mover, x, y, z) end

---@public
---@param mover IsoMovingObject
---@param x int
---@param y int
---@param z int
---@param getter IsoGridSquare.GetSquare
---@return boolean
function IsoGridSquare:testPathFindAdjacent(mover, x, y, z, getter) end

---@public
---@param arg0 int
---@param arg1 float
---@return void
function IsoGridSquare:setDarkMulti(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 boolean
---@param arg2 long
---@return void
function IsoGridSquare:setIsDissolved(arg0, arg1, arg2) end

---@public
---@return IsoGridSquare @the s
function IsoGridSquare:getS() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 boolean
---@return int
---@overload fun(arg0:int, arg1:byte, arg2:byte, arg3:byte, arg4:boolean)
function IsoGridSquare:setMatrixBit(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 int
---@param arg1 byte
---@param arg2 byte
---@param arg3 byte
---@param arg4 boolean
---@return int
function IsoGridSquare:setMatrixBit(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param body IsoDeadBody
---@param bRemote boolean
---@return void
function IsoGridSquare:addCorpse(body, bRemote) end

---@public
---@param arg0 boolean
---@param arg1 boolean
---@return void
function IsoGridSquare:removeBlood(arg0, arg1) end

---@public
---@return IsoCompost
function IsoGridSquare:getCompost() end

---@public
---@return ArrayList|Float @the LightInfluenceB
function IsoGridSquare:getLightInfluenceB() end

---@param arg0 int
---@param arg1 boolean
---@param arg2 boolean
---@param arg3 boolean
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return boolean
---@overload fun(arg0:PZArrayList|Unknown, arg1:int, arg2:boolean, arg3:boolean, arg4:boolean, arg5:boolean, arg6:boolean, arg7:Shader)
function IsoGridSquare:renderMinusFloor(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@param arg0 PZArrayList|Unknown
---@param arg1 int
---@param arg2 boolean
---@param arg3 boolean
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 boolean
---@param arg7 Shader
---@return boolean
function IsoGridSquare:renderMinusFloor(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@return void
function IsoGridSquare:recalcHashCodeObjects() end

---@public
---@return boolean
function IsoGridSquare:shouldSave() end

---@public
---@param y int @the y to set
---@return void
function IsoGridSquare:setY(y) end

---@public
---@return KahluaTable
function IsoGridSquare:getModData() end

---@public
---@return boolean
function IsoGridSquare:isOutside() end

---@public
---@param arg0 IsoPlayer
---@param arg1 boolean
---@return boolean
function IsoGridSquare:damageSpriteSheetRopeFromBottom(arg0, arg1) end

---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return boolean
function IsoGridSquare:IsWindow(arg0, arg1, arg2) end

---@public
---@return IsoTree
function IsoGridSquare:getTree() end

---@public
---@param arg0 float
---@param arg1 float
---@return float
function IsoGridSquare:getApparentZ(arg0, arg1) end

---@public
---@param lampostTotalG float @the lampostTotalG to set
---@return void
function IsoGridSquare:setLampostTotalG(lampostTotalG) end

---@public
---@param other IsoMovingObject
---@return float
---@overload fun(sq:IsoGridSquare)
function IsoGridSquare:DistToProper(other) end

---@public
---@param sq IsoGridSquare
---@return float
function IsoGridSquare:DistToProper(sq) end

---@public
---@return boolean
function IsoGridSquare:HasElevatedFloor() end

---@public
---@return boolean
function IsoGridSquare:HasSlopedRoofWest() end

---@public
---@return IsoObject
function IsoGridSquare:getPlayerBuiltFloor() end

---@public
---@return void
function IsoGridSquare:BurnWallsTCOnly() end

---@private
---@param arg0 IsoObject
---@param arg1 Color
---@return void
function IsoGridSquare:fudgeShadowsToAlpha(arg0, arg1) end

---@public
---@return IWorldRegion
function IsoGridSquare:getIsoWorldRegion() end

---@public
---@return IsoWaterGeometry
function IsoGridSquare:getWater() end

---@public
---@param arg0 IsoDirections
---@param arg1 boolean
---@return IsoObject
function IsoGridSquare:getDoorOrWindowOrWindowFrame(arg0, arg1) end

---@public
---@param LightInfluenceG ArrayList|Float @the LightInfluenceG to set
---@return void
function IsoGridSquare:setLightInfluenceG(LightInfluenceG) end

---@public
---@return void
function IsoGridSquare:FixStackableObjects() end

---@public
---@return boolean
function IsoGridSquare:HasStairsNorth() end

---@public
---@param id String
---@param bFlip boolean
---@param prop IsoFlagType
---@param offX float
---@param offZ float
---@param alpha float
---@return void
function IsoGridSquare:DoSplat(id, bFlip, prop, offX, offZ, alpha) end

---Get the IsoThumpable window between this grid and the next in parameter
---@public
---@param next IsoGridSquare
---@return IsoThumpable
function IsoGridSquare:getWindowThumpableTo(next) end

---@public
---@return void
function IsoGridSquare:clearWater() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return boolean
function IsoGridSquare:getPathMatrix(arg0, arg1, arg2) end

---@public
---@param arg0 IsoWorldRegion
---@return void
function IsoGridSquare:setIsoWorldRegion(arg0) end

---@public
---@param arg0 IsoObject
---@return int
function IsoGridSquare:RemoveTileObjectErosionNoRecalc(arg0) end

---@public
---@param north boolean
---@return IsoThumpable
function IsoGridSquare:getThumpableWindow(north) end

---@public
---@param x int
---@param y int
---@param z int
---@param specialDiag boolean
---@param bIgnoreDoors boolean
---@return LosUtil.TestResults
function IsoGridSquare:testVisionAdjacent(x, y, z, specialDiag, bIgnoreDoors) end

---@public
---@return boolean @the bDoSlowPathfinding
function IsoGridSquare:isbDoSlowPathfinding() end

---@public
---@param x int
---@param y int
---@param z int
---@param ignoreDoors boolean
---@return boolean
function IsoGridSquare:testCollideAdjacentAdvanced(x, y, z, ignoreDoors) end

---@public
---@param cell IsoCell
---@param slice SliceY
---@param x int
---@param y int
---@param z int
---@return IsoGridSquare
---@overload fun(arg0:ArrayDeque|Unknown, arg1:IsoCell, arg2:SliceY, arg3:int, arg4:int, arg5:int)
function IsoGridSquare:getNew(cell, slice, x, y, z) end

---@public
---@param arg0 ArrayDeque|Unknown
---@param arg1 IsoCell
---@param arg2 SliceY
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@return IsoGridSquare
function IsoGridSquare:getNew(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function IsoGridSquare:addDeferredCharacter(arg0) end

---@return boolean
function IsoGridSquare:HasNoCharacters() end

---@public
---@return Boolean
function IsoGridSquare:getWallFull() end

---@public
---@return int
function IsoGridSquare:hashCodeNoOverride() end

---@public
---@return int
function IsoGridSquare:getPuddlesDir() end

---@public
---@param CacheIsFree boolean @the CacheIsFree to set
---@return void
function IsoGridSquare:setCacheIsFree(CacheIsFree) end

---@public
---@return void
function IsoGridSquare:EnsureSurroundNotNull() end

---@public
---@param g IsoGameCharacter
---@param range int
---@param EnemyList ArrayList|IsoMovingObject
---@return IsoGameCharacter
---@overload fun(g:IsoGameCharacter, range:int, EnemyList:ArrayList|IsoMovingObject, RangeTest:IsoGameCharacter, TestRangeMax:int)
function IsoGridSquare:FindEnemy(g, range, EnemyList) end

---@public
---@param g IsoGameCharacter
---@param range int
---@param EnemyList ArrayList|IsoMovingObject
---@param RangeTest IsoGameCharacter
---@param TestRangeMax int
---@return IsoGameCharacter
function IsoGridSquare:FindEnemy(g, range, EnemyList, RangeTest, TestRangeMax) end

---@public
---@param arg0 int
---@param arg1 IsoTrap
---@param arg2 IsoTrap.ExplosionMode
---@return void
function IsoGridSquare:drawCircleExplosion(arg0, arg1, arg2) end

---@public
---@param aLightcache int @the lightcache to set
---@return void
function IsoGridSquare:setLightcache(aLightcache) end

---@public
---@param bDoReverse boolean
---@return void
---@overload fun(bDoReverse:boolean, getter:IsoGridSquare.GetSquare)
function IsoGridSquare:RecalcAllWithNeighbours(bDoReverse) end

---@public
---@param bDoReverse boolean
---@param getter IsoGridSquare.GetSquare
---@return void
function IsoGridSquare:RecalcAllWithNeighbours(bDoReverse, getter) end

---@param arg0 IsoGridSquare
---@return void
function IsoGridSquare:ReCalculateMineOnly(arg0) end

---@public
---@param type String
---@return IsoObject
function IsoGridSquare:getContainerItem(type) end

---@public
---@param arg0 boolean
---@return void
function IsoGridSquare:BurnWalls(arg0) end

---@public
---@return KahluaTable
function IsoGridSquare:getLuaTileObjectList() end

---@public
---@param playerIndex int
---@return boolean @the bCouldSee
function IsoGridSquare:isCouldSee(playerIndex) end

---@public
---@param object IsoWorldInventoryObject
---@return void
function IsoGridSquare:removeWorldObject(object) end

---@public
---@return void
function IsoGridSquare:smoke() end

---@public
---@return IsoChunk
function IsoGridSquare:getChunk() end

---@public
---@param arg0 IsoObject
---@param arg1 IsoDirections
---@param arg2 boolean
---@param arg3 boolean
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 boolean
---@param arg7 boolean
---@param arg8 boolean
---@param arg9 WallShaperWhole
---@return void
function IsoGridSquare:DoCutawayShader(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) end

---@private
---@param arg0 IsoObject
---@param arg1 ColorInfo
---@return void
function IsoGridSquare:renderAttachedSpritesWithNoWallLighting(arg0, arg1) end

---@public
---@return IsoBrokenGlass
function IsoGridSquare:addBrokenGlass() end

---@public
---@param arg0 IsoGridSquare
---@return IsoObject
function IsoGridSquare:getTransparentWallTo(arg0) end

---@public
---@param x int
---@param y int
---@return float
function IsoGridSquare:scoreAsWaypoint(x, y) end

---@public
---@return void
function IsoGridSquare:RecalcPropertiesIfNeeded() end

---@public
---@param i int
---@param playerIndex int
---@return int
function IsoGridSquare:getVertLight(i, playerIndex) end

---@public
---@return boolean
function IsoGridSquare:isAdjacentToWindow() end

---@public
---@param playerIndex int
---@return boolean @the canSee
function IsoGridSquare:isCanSee(playerIndex) end

---@public
---@return void
function IsoGridSquare:RenderOpenDoorOnly() end

---@public
---@param playerIndex int
---@return void
function IsoGridSquare:CalcVisibility(playerIndex) end

---@public
---@return void
function IsoGridSquare:StartFire() end

---@public
---@return boolean
function IsoGridSquare:hasSupport() end

---@public
---@return IsoRaindrop
function IsoGridSquare:getRainDrop() end

---@public
---@return PropertyContainer @the Properties
function IsoGridSquare:getProperties() end

---Specified by:
---
---getID in interface INode
---@public
---@return Integer @the ID
function IsoGridSquare:getID() end

---@public
---@return boolean @the CacheIsFree
function IsoGridSquare:isCacheIsFree() end

---@public
---@return int
function IsoGridSquare:getX() end

---@public
---@return boolean
function IsoGridSquare:isInARoom() end

---@public
---@return boolean
function IsoGridSquare:TreatAsSolidFloor() end

---@public
---@param aDarkStep float @the darkStep to set
---@return void
function IsoGridSquare:setDarkStep(aDarkStep) end

---@public
---@param arg0 int
---@return boolean
function IsoGridSquare:getCanSee(arg0) end

---@public
---@return void
function IsoGridSquare:RecalcProperties() end

---@public
---@param trapPositionY int
---@return void
function IsoGridSquare:setTrapPositionY(trapPositionY) end

---@public
---@return void
function IsoGridSquare:transmitStopFire() end

---@private
---@param arg0 IsoObject
---@return boolean
function IsoGridSquare:isSpriteOnSouthOrEastWall(arg0) end

---@public
---@return boolean
function IsoGridSquare:connectedWithFloor() end

---@public
---@param arg0 IsoGridSquare
---@return IsoObject
function IsoGridSquare:getHoppableTo(arg0) end

---@public
---@param directions IsoDirections
---@return IsoGridSquare
function IsoGridSquare:getTileInDirection(directions) end

---@public
---@return boolean
---@overload fun(arg0:boolean)
function IsoGridSquare:IsOnScreen() end

---@public
---@param arg0 boolean
---@return boolean
function IsoGridSquare:IsOnScreen(arg0) end

---@param arg0 boolean
---@return boolean
function IsoGridSquare:HasDoor(arg0) end

---@public
---@param arg0 boolean
---@return IsoObject
function IsoGridSquare:getWindowFrame(arg0) end

---@private
---@param arg0 IsoObject
---@param arg1 boolean
---@param arg2 boolean
---@param arg3 boolean
---@param arg4 int
---@param arg5 boolean
---@param arg6 boolean
---@return void
function IsoGridSquare:calculateWallAlphaCommon(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 int
---@return void
function IsoGridSquare:renderDeferredCharacters(arg0) end

---@public
---@param data byte[]
---@return boolean
function IsoGridSquare:toBoolean(data) end

---@public
---@return void
function IsoGridSquare:setHourSeenToCurrent() end

---@param arg0 Shader
---@return int
function IsoGridSquare:renderFloor(arg0) end

---@public
---@param arg0 IsoPlayer
---@param arg1 boolean
---@return boolean
function IsoGridSquare:removeSheetRopeFromBottom(arg0, arg1) end

---@public
---@return IsoGridOcclusionData
function IsoGridSquare:getOcclusionData() end

---@public
---@param arg0 IsoGridSquare
---@return boolean
function IsoGridSquare:isHoppableTo(arg0) end

---@public
---@param next IsoGridSquare
---@return IsoObject
function IsoGridSquare:getBedTo(next) end

---@public
---@param lampostTotalB float @the lampostTotalB to set
---@return void
function IsoGridSquare:setLampostTotalB(lampostTotalB) end

---@public
---@return float @the lampostTotalB
function IsoGridSquare:getLampostTotalB() end

---@public
---@param sprite String
---@return IsoObject
function IsoGridSquare:addFloor(sprite) end

---@public
---@return PZArrayList|Unknown
function IsoGridSquare:getLocalTemporaryObjects() end

---@public
---@return IsoDeadBody
function IsoGridSquare:getDeadBody() end

---@private
---@param arg0 IsoObject
---@param arg1 boolean
---@param arg2 boolean
---@param arg3 boolean
---@param arg4 IsoFlagType
---@param arg5 IsoFlagType
---@param arg6 IsoFlagType
---@param arg7 boolean
---@param arg8 int
---@param arg9 boolean
---@param arg10 boolean
---@return boolean
function IsoGridSquare:calculateWallAlphaAndCircleStencilEdge(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) end

---@public
---@return void
function IsoGridSquare:removeAllWorldObjects() end

---@public
---@param arg0 IsoObject
---@return int
function IsoGridSquare:transmitRemoveItemFromSquare(arg0) end

---@public
---@param arg0 boolean
---@return IsoObject
function IsoGridSquare:getHoppableWall(arg0) end

---@public
---@return boolean
function IsoGridSquare:HasTree() end

---@public
---@param arg0 boolean
---@return IsoObject
function IsoGridSquare:getHoppable(arg0) end

---@public
---@return void
function IsoGridSquare:explodeTrap() end

---@public
---@return void
function IsoGridSquare:discard() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return boolean
function IsoGridSquare:getVisionMatrix(arg0, arg1, arg2) end

---@private
---@param arg0 IsoObject
---@param arg1 int
---@param arg2 int
---@param arg3 boolean
---@param arg4 Consumer|Unknown
---@param arg5 Shader
---@return int
function IsoGridSquare:performDrawWallOnly(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param ID int @the ID to set
---@return void
function IsoGridSquare:setID(ID) end

---@public
---@param arg0 IsoDirections
---@return IsoObject
function IsoGridSquare:getOpenDoor(arg0) end

---@public
---@param arg0 IsoObject
---@return int
function IsoGridSquare:RemoveTileObject(arg0) end

---@public
---@return void
function IsoGridSquare:transmitModdata() end

---@public
---@return IsoGridSquare @the e
function IsoGridSquare:getE() end

---@public
---@param obj IsoObject
---@return void
---@overload fun(arg0:IsoObject, arg1:int)
function IsoGridSquare:AddTileObject(obj) end

---@public
---@param arg0 IsoObject
---@param arg1 int
---@return void
function IsoGridSquare:AddTileObject(arg0, arg1) end

---@public
---@return void
function IsoGridSquare:RecalcAllWithNeighboursMineOnly() end

---@public
---@return boolean
function IsoGridSquare:isSolid() end

---@public
---@param arg0 HandWeapon
---@return void
function IsoGridSquare:syncIsoTrap(arg0) end

---@public
---@param SolidFloor boolean @the SolidFloor to set
---@return void
function IsoGridSquare:setSolidFloor(SolidFloor) end

---@public
---@param gridSquare IsoGridSquare
---@param bVision boolean
---@param bPathfind boolean
---@param bIgnoreSolidTrans boolean
---@return boolean
---@overload fun(gridSquare:IsoGridSquare, bVision:boolean, bPathfind:boolean, bIgnoreSolidTrans:boolean, bIgnoreSolid:boolean)
---@overload fun(gridSquare:IsoGridSquare, bVision:boolean, bPathfind:boolean, bIgnoreSolidTrans:boolean, bIgnoreSolid:boolean, getter:IsoGridSquare.GetSquare)
function IsoGridSquare:CalculateCollide(gridSquare, bVision, bPathfind, bIgnoreSolidTrans) end

---@public
---@param gridSquare IsoGridSquare
---@param bVision boolean
---@param bPathfind boolean
---@param bIgnoreSolidTrans boolean
---@param bIgnoreSolid boolean
---@return boolean
function IsoGridSquare:CalculateCollide(gridSquare, bVision, bPathfind, bIgnoreSolidTrans, bIgnoreSolid) end

---@public
---@param gridSquare IsoGridSquare
---@param bVision boolean
---@param bPathfind boolean
---@param bIgnoreSolidTrans boolean
---@param bIgnoreSolid boolean
---@param getter IsoGridSquare.GetSquare
---@return boolean
function IsoGridSquare:CalculateCollide(gridSquare, bVision, bPathfind, bIgnoreSolidTrans, bIgnoreSolid, getter) end

---@public
---@return IsoPuddlesGeometry
function IsoGridSquare:getPuddles() end

---@public
---@return void
function IsoGridSquare:restackSheetRope() end

---@public
---@param overlayDone boolean
---@return void
function IsoGridSquare:setOverlayDone(overlayDone) end

---@public
---@return ColorInfo @the defColorInfo
function IsoGridSquare:getDefColorInfo() end

---@public
---@param e IsoGridSquare @the e to set
---@return void
function IsoGridSquare:setE(e) end

---@public
---@return float
function IsoGridSquare:getHoursSinceLastSeen() end

---@public
---@param g IsoGameCharacter
---@param range int
---@param EnemyList Stack|IsoGameCharacter
---@return IsoGameCharacter
function IsoGridSquare:FindFriend(g, range, EnemyList) end

---@public
---@return IsoGridSquare @the w
function IsoGridSquare:getW() end

---@public
---@param arg0 IsoGridSquare
---@return IsoObject
function IsoGridSquare:getWallHoppableTo(arg0) end

---@public
---@param splash IsoRainSplash
---@return void
function IsoGridSquare:setRainSplash(splash) end

---@public
---@param n IsoGridSquare @the n to set
---@return void
function IsoGridSquare:setN(n) end

---@public
---@param type String
---@return void
function IsoGridSquare:removeErosionObject(type) end

---@public
---@param arg0 boolean
---@return boolean
function IsoGridSquare:hasFloor(arg0) end

---@public
---@param north boolean
---@return IsoThumpable
function IsoGridSquare:getHoppableThumpable(north) end

---@public
---@return ArrayList|IsoMovingObject @the StaticMovingObjects
function IsoGridSquare:getStaticMovingObjects() end

---@public
---@param arg0 String
---@return long
---@overload fun(arg0:String, arg1:boolean)
function IsoGridSquare:playSound(arg0) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return long
function IsoGridSquare:playSound(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 boolean
---@return void
function IsoGridSquare:setIsSeen(arg0, arg1) end

---@public
---@param arg0 IsoObject
---@param arg1 int
---@param arg2 boolean
---@param arg3 boolean
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return int
function IsoGridSquare:DoWallLightingW(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param x int @the x to set
---@return void
function IsoGridSquare:setX(x) end

---@public
---@param arg0 boolean
---@return void
function IsoGridSquare:setHasFlies(arg0) end

---@public
---@return float
function IsoGridSquare:getPuddlesInGround() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
---@overload fun(arg0:ByteBuffer, arg1:int, arg2:boolean)
function IsoGridSquare:load(arg0, arg1) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoGridSquare:load(arg0, arg1, arg2) end

---@public
---@param aRecalcLightTime int @the RecalcLightTime to set
---@return void
function IsoGridSquare:setRecalcLightTime(aRecalcLightTime) end

---@public
---@return int
function IsoGridSquare:getTrapPositionX() end

---@public
---@return float @the darkStep
function IsoGridSquare:getDarkStep() end

---@public
---@param bCountOtherCharacters boolean
---@return boolean
function IsoGridSquare:isNotBlocked(bCountOtherCharacters) end

---@public
---@return boolean @the CachedIsFree
function IsoGridSquare:isCachedIsFree() end

---@public
---@param arg0 IsoObjectType
---@return IsoCurtain
function IsoGridSquare:getCurtain(arg0) end

---@public
---@return boolean
function IsoGridSquare:haveElectricity() end

---@public
---@return boolean
function IsoGridSquare:hasWindowFrame() end

---@public
---@param arg0 IsoObject
---@param arg1 int
---@param arg2 boolean
---@param arg3 boolean
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return int
function IsoGridSquare:DoWallLightingN(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@param arg0 IsoObject
---@return void
function IsoGridSquare:RereouteWallMaskTo(arg0) end

---@public
---@param arg0 boolean
---@return IsoObject
function IsoGridSquare:getWallHoppable(arg0) end

---@private
---@param arg0 boolean
---@return IsoObject
function IsoGridSquare:getSpecialWall(arg0) end

---@public
---@return int
function IsoGridSquare:getWallType() end

---@public
---@return boolean
function IsoGridSquare:haveDoor() end

---@private
---@param arg0 IsoObject
---@param arg1 int
---@param arg2 int
---@param arg3 boolean
---@param arg4 Consumer|Unknown
---@param arg5 Shader
---@return int
function IsoGridSquare:performDrawWall(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@return IsoMetaGrid.Zone
function IsoGridSquare:getZone() end

---@public
---@return boolean
function IsoGridSquare:hasWindowOrWindowFrame() end

---@public
---@param room IsoRoom @the room to set
---@return void
function IsoGridSquare:setRoom(room) end

---@public
---@param abDoSlowPathfinding boolean @the bDoSlowPathfinding to set
---@return void
function IsoGridSquare:setbDoSlowPathfinding(abDoSlowPathfinding) end

---@public
---@param arg0 int
---@param arg1 boolean
---@return void
function IsoGridSquare:setCanSee(arg0, arg1) end

---@public
---@return void
function IsoGridSquare:explode() end

---@public
---@return ArrayList|Float @the LightInfluenceR
function IsoGridSquare:getLightInfluenceR() end

---@public
---@return void
function IsoGridSquare:InvalidateSpecialObjectPaths() end

---@public
---@return IsoBuilding
function IsoGridSquare:getRoofHideBuilding() end

---@public
---@param active boolean
---@return void
function IsoGridSquare:switchLight(active) end

---@public
---@param arg0 int
---@return float
function IsoGridSquare:getLightLevel(arg0) end

---@param arg0 IsoFlagType
---@return void
function IsoGridSquare:RemoveAllWith(arg0) end

---@public
---@return boolean
function IsoGridSquare:hasModData() end

---@public
---@param arg0 int
---@param arg1 boolean
---@param arg2 boolean
---@return boolean
function IsoGridSquare:RenderMinusFloorFxMask(arg0, arg1, arg2) end

---@public
---@param obj IsoObject
---@return void
function IsoGridSquare:AddSpecialTileObject(obj) end

---@public
---@return long
function IsoGridSquare:getHashCodeObjects() end

---@private
---@param arg0 IsoObject
---@param arg1 boolean
---@param arg2 boolean
---@param arg3 boolean
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 boolean
---@param arg7 boolean
---@param arg8 boolean
---@param arg9 int
---@param arg10 boolean
---@param arg11 boolean
---@param arg12 boolean
---@param arg13 boolean
---@return boolean
function IsoGridSquare:calculateWallAlphaAndCircleStencilCorner(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13) end

---@public
---@param arg0 int
---@param arg1 boolean
---@param arg2 long
---@return void
function IsoGridSquare:setPlayerCutawayFlag(arg0, arg1, arg2) end

---@public
---@return void
function IsoGridSquare:softClear() end

---@public
---@param next IsoGridSquare
---@return IsoThumpable
function IsoGridSquare:getHoppableThumpableTo(next) end

---@public
---@return void
function IsoGridSquare:stopFire() end

---@public
---@param collideObject IsoMovingObject
---@param x int
---@param y int
---@param z int
---@return boolean
function IsoGridSquare:testCollideAdjacent(collideObject, x, y, z) end

---@public
---@param other IsoGridSquare
---@return boolean
function IsoGridSquare:isBlockedTo(other) end

---@public
---@param other IsoGridSquare
---@return boolean
function IsoGridSquare:isWindowTo(other) end

---@public
---@return IsoZombie
function IsoGridSquare:getZombie() end

---@public
---@param arg0 IsoDirections
---@return IsoGridSquare
function IsoGridSquare:getAdjacentPathSquare(arg0) end

---@public
---@param body IsoDeadBody
---@param bRemote boolean
---@return void
function IsoGridSquare:removeCorpse(body, bRemote) end

---@public
---@return IsoGridOcclusionData
function IsoGridSquare:getOrCreateOcclusionData() end

---@public
---@return void
function IsoGridSquare:BurnTick() end

---@public
---@param trap IsoTrap
---@return void
function IsoGridSquare:explosion(trap) end

---@public
---@param arg0 boolean
---@return IsoObject
function IsoGridSquare:getThumpableWallOrHoppable(arg0) end

---@public
---@param arg0 IsoObject
---@param arg1 int
---@return void
function IsoGridSquare:transmitAddObjectToSquare(arg0, arg1) end

---@public
---@return boolean
function IsoGridSquare:hasFlies() end

---@public
---@return IsoObject
function IsoGridSquare:getSheetRope() end

---@public
---@param w IsoGridSquare @the w to set
---@return void
function IsoGridSquare:setW(w) end

---@public
---@param arg0 boolean
---@return float
function IsoGridSquare:getGridSneakModifier(arg0) end

---@public
---@param arg0 IsoObject
---@param arg1 int
---@return int
function IsoGridSquare:placeWallAndDoorCheck(arg0, arg1) end

---@public
---@param haveElectricity boolean
---@return void
function IsoGridSquare:setHaveElectricity(haveElectricity) end

---@public
---@return IsoCell @the getCell()
function IsoGridSquare:getCell() end

---@private
---@return void
function IsoGridSquare:debugPrintGridSquare() end
