---@class IsoCell : zombie.iso.IsoCell
---@field public MaxHeight int
---@field private m_floorRenderShader Shader
---@field private m_wallRenderShader Shader
---@field public Trees ArrayList|Unknown
---@field stchoices ArrayList|Unknown
---@field public ChunkMap IsoChunkMap[]
---@field public BuildingList ArrayList|IsoBuilding
---@field private WindowList ArrayList|Unknown
---@field private ObjectList ArrayList|Unknown
---@field private PushableObjectList ArrayList|Unknown
---@field private BuildingScores HashMap|Unknown|Unknown
---@field private RoomList ArrayList|Unknown
---@field private StaticUpdaterObjectList ArrayList|Unknown
---@field private ZombieList ArrayList|Unknown
---@field private RemoteSurvivorList ArrayList|Unknown
---@field private removeList ArrayList|Unknown
---@field private addList ArrayList|Unknown
---@field private ProcessIsoObject ArrayList|Unknown
---@field private ProcessIsoObjectRemove ArrayList|Unknown
---@field private ProcessItems ArrayList|Unknown
---@field private ProcessItemsRemove ArrayList|Unknown
---@field private ProcessWorldItems ArrayList|Unknown
---@field public ProcessWorldItemsRemove ArrayList|Unknown
---@field private gridSquares IsoGridSquare[][]
---@field public ENABLE_SQUARE_CACHE boolean
---@field private height int
---@field private width int
---@field private worldX int
---@field private worldY int
---@field public DangerScore IntGrid
---@field private safeToAdd boolean
---@field private LamppostPositions Stack|Unknown
---@field public roomLights ArrayList|Unknown
---@field private heatSources ArrayList|Unknown
---@field public addVehicles ArrayList|Unknown
---@field public vehicles ArrayList|Unknown
---@field public ISOANGLEFACTOR int
---@field private ZOMBIESCANBUDGET int
---@field private NEARESTZOMBIEDISTSQRMAX float
---@field private zombieScanCursor int
---@field private nearestVisibleZombie IsoZombie[]
---@field private nearestVisibleZombieDistSqr float[]
---@field private buildingscores Stack|Unknown
---@field GridStack ArrayList|Unknown
---@field public RTF_SolidFloor int
---@field public RTF_VegetationCorpses int
---@field public RTF_MinusFloorCharacters int
---@field public RTF_ShadedFloor int
---@field public RTF_Shadows int
---@field private ShadowSquares ArrayList|Unknown
---@field private MinusFloorCharacters ArrayList|Unknown
---@field private SolidFloor ArrayList|Unknown
---@field private ShadedFloor ArrayList|Unknown
---@field private VegetationCorpses ArrayList|Unknown
---@field public perPlayerRender IsoCell.PerPlayerRender[]
---@field private StencilXY int[]
---@field private StencilXY2z int[]
---@field public StencilX1 int
---@field public StencilY1 int
---@field public StencilX2 int
---@field public StencilY2 int
---@field private m_stencilTexture Texture
---@field private diamondMatrixIterator DiamondMatrixIterator
---@field private diamondMatrixPos Vector2i
---@field public DeferredCharacterTick int
---@field private hasSetupSnowGrid boolean
---@field private snowGridTiles_Square IsoCell.SnowGridTiles
---@field private snowGridTiles_Strip IsoCell.SnowGridTiles[]
---@field private snowGridTiles_Edge IsoCell.SnowGridTiles[]
---@field private snowGridTiles_Cove IsoCell.SnowGridTiles[]
---@field private snowGridTiles_Enclosed IsoCell.SnowGridTiles
---@field private m_snowFirstNonSquare int
---@field private snowNoise2D Noise2D
---@field private snowGridCur IsoCell.SnowGrid
---@field private snowGridPrev IsoCell.SnowGrid
---@field private snowFracTarget int
---@field private snowFadeTime long
---@field private snowTransitionTime float
---@field private raport int
---@field private SNOWSHORE_NONE int
---@field private SNOWSHORE_N int
---@field private SNOWSHORE_E int
---@field private SNOWSHORE_S int
---@field private SNOWSHORE_W int
---@field public recalcFloors boolean
---@field wx int
---@field wy int
---@field drag KahluaTable[]
---@field SurvivorList ArrayList|Unknown
---@field private texWhite Texture
---@field private instance IsoCell
---@field private currentLX int
---@field private currentLY int
---@field private currentLZ int
---@field recalcShading int
---@field lastMinX int
---@field lastMinY int
---@field private rainScroll float
---@field private rainX int[]
---@field private rainY int[]
---@field private rainTextures Texture[]
---@field private rainFileTime long[]
---@field private rainAlphaMax float
---@field private rainAlpha float[]
---@field protected rainIntensity int
---@field protected rainSpeed int
---@field lightUpdateCount int
---@field public bRendering boolean
---@field bHideFloors boolean[]
---@field unhideFloorsCounter int[]
---@field bOccludedByOrphanStructureFlag boolean
---@field playerPeekedRoomId int
---@field playerOccluderBuildings ArrayList|Unknown
---@field playerOccluderBuildingsArr IsoBuilding[][]
---@field playerWindowPeekingRoomId int[]
---@field playerHidesOrphanStructures boolean[]
---@field playerCutawaysDirty boolean[]
---@field tempCutawaySqrVector JVector2
---@field tempPrevPlayerCutawayRoomIDs ArrayList|Unknown
---@field tempPlayerCutawayRoomIDs ArrayList|Unknown
---@field lastPlayerSquare IsoGridSquare[]
---@field lastPlayerSquareHalf boolean[]
---@field lastPlayerDir IsoDirections[]
---@field lastPlayerAngle JVector2[]
---@field hidesOrphanStructuresAbove int
---@field buildingRectTemp Rectangle
---@field zombieOccluderBuildings ArrayList|Unknown
---@field zombieOccluderBuildingsArr IsoBuilding[][]
---@field lastZombieSquare IsoGridSquare[]
---@field lastZombieSquareHalf boolean[]
---@field otherOccluderBuildings ArrayList|Unknown
---@field otherOccluderBuildingsArr IsoBuilding[][]
---@field mustSeeSquaresRadius int
---@field mustSeeSquaresGridSize int
---@field gridSquaresTempLeft ArrayList|Unknown
---@field gridSquaresTempRight ArrayList|Unknown
---@field private weatherFX IsoWeatherFX
---@field private minX int
---@field private maxX int
---@field private minY int
---@field private maxY int
---@field private minZ int
---@field private maxZ int
---@field private dangerUpdate OnceEvery
---@field private LightInfoUpdate Thread
---@field private SpottedRooms Stack|Unknown
---@field private fakeZombieForHit IsoZombie
IsoCell = {}

---@public
---@param x int
---@param y int
---@param z int
---@return float
function IsoCell:DistanceFromSupport(x, y, z) end

---@private
---@return void
function IsoCell:updateWeatherFx() end

---@private
---@param arg0 IsoGridSquare
---@param arg1 IsoGridOcclusionData.OcclusionFilter
---@return ArrayList|Unknown
function IsoCell:GetBuildingsInFrontOfMustSeeSquare(arg0, arg1) end

---throws java.io.FileNotFoundException, java.io.IOException
---@public
---@param WorldVersion int
---@return boolean
function IsoCell:LoadPlayer(WorldVersion) end

---@public
---@param newSquare IsoGridSquare
---@param getter IsoGridSquare.GetSquare
---@return void
function IsoCell:DoGridNav(newSquare, getter) end

---@private
---@param arg0 IsoGridSquare
---@param arg1 IsoDirections
---@return IsoBuilding
function IsoCell:GetPeekedInBuilding(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return IsoLightSource
function IsoCell:getLightSourceAt(arg0, arg1, arg2) end

---@public
---@return ArrayList|Unknown
function IsoCell:getProcessWorldItems() end

---@public
---@param newSquare IsoGridSquare
---@param bDoSurrounds boolean
---@return IsoGridSquare
---@overload fun(arg0:IsoGridSquare, arg1:boolean, arg2:boolean)
function IsoCell:ConnectNewSquare(newSquare, bDoSurrounds) end

---@param arg0 IsoGridSquare
---@param arg1 boolean
---@param arg2 boolean
---@return IsoGridSquare
function IsoCell:ConnectNewSquare(arg0, arg1, arg2) end

---@public
---@param arg0 IsoChunk
---@return void
---@overload fun(arg0:IsoChunk, arg1:int)
function IsoCell:setCacheChunk(arg0) end

---@public
---@param arg0 IsoChunk
---@param arg1 int
---@return void
function IsoCell:setCacheChunk(arg0, arg1) end

---@private
---@return void
function IsoCell:ObjectDeletionAddition() end

---@public
---@param x int
---@param y int
---@param z int
---@param square IsoGridSquare
---@param playerIndex int
---@return void
function IsoCell:setCacheGridSquareLocal(x, y, z, square, playerIndex) end

---Specified by:
---
---isNull in interface TileBasedMap
---@public
---@param x int
---@param y int
---@param z int
---@return boolean
function IsoCell:isNull(x, y, z) end

---@public
---@param arg0 InventoryItem
---@return void
---@overload fun(arg0:ArrayList|Unknown)
function IsoCell:addToProcessItems(arg0) end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function IsoCell:addToProcessItems(arg0) end

---@private
---@param arg0 Iterator|Unknown
---@return void
function IsoCell:ProcessRemoveItems(arg0) end

---@public
---@param LightInfoUpdate Thread @the LightInfoUpdate to set
---@return void
function IsoCell:setLightInfoUpdate(LightInfoUpdate) end

---@public
---@param col int
---@return int
function IsoCell:getBComponent(col) end

---@private
---@param arg0 IsoCell.PerPlayerRender
---@param arg1 int
---@return void
function IsoCell:flattenAnyFoliage(arg0, arg1) end

---@private
---@param arg0 IsoCell.PerPlayerRender
---@param arg1 int
---@param arg2 int
---@param arg3 long
---@return void
function IsoCell:performRenderTiles(arg0, arg1, arg2, arg3) end

---@public
---@param dangerUpdate OnceEvery @the dangerUpdate to set
---@return void
function IsoCell:setDangerUpdate(dangerUpdate) end

---@public
---@return IsoZombie
function IsoCell:getFakeZombieForHit() end

---@public
---@return ArrayList|IsoMovingObject @the addList
function IsoCell:getAddList() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return short
function IsoCell:getStencilValue(arg0, arg1, arg2) end

---@private
---@param arg0 IsoCell.PerPlayerRender
---@param arg1 int
---@param arg2 int
---@param arg3 long
---@return void
function IsoCell:recalculateAnyGridStacks(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return int
function IsoCell:getHeatSourceTemperature(arg0, arg1, arg2) end

---@public
---@return int @the maxX
function IsoCell:getMaxX() end

---@public
---@return ArrayList|IsoBuilding @the BuildingList
function IsoCell:getBuildingList() end

---@public
---@return int @the MaxHeight
function IsoCell:getMaxHeight() end

---@public
---@param lot IsoLot
---@param sx int
---@param sy int
---@param sz int
---@param bClearExisting boolean
---@return void
---@overload fun(filename:String, sx:int, sy:int, sz:int, bClearExisting:boolean)
---@overload fun(arg0:IsoLot, arg1:int, arg2:int, arg3:int, arg4:IsoChunk, arg5:int, arg6:int)
function IsoCell:PlaceLot(lot, sx, sy, sz, bClearExisting) end

---@public
---@param filename String
---@param sx int
---@param sy int
---@param sz int
---@param bClearExisting boolean
---@return void
function IsoCell:PlaceLot(filename, sx, sy, sz, bClearExisting) end

---@public
---@param arg0 IsoLot
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 IsoChunk
---@param arg5 int
---@param arg6 int
---@return void
function IsoCell:PlaceLot(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@return void
function IsoCell:DeleteAllMovingObjects() end

---@public
---@param arg0 int
---@return boolean
function IsoCell:IsPlayerWindowPeeking(arg0) end

---@public
---@param currentLZ int @the currentLZ to set
---@return void
function IsoCell:setCurrentLightZ(currentLZ) end

---@public
---@param player int
---@return KahluaTable
function IsoCell:getDrag(player) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return short
function IsoCell:getStencilValue2z(arg0, arg1, arg2) end

---@public
---@param player int
---@param bRender boolean
---@return boolean
function IsoCell:DoBuilding(player, bRender) end

---@private
---@param arg0 IsoGridSquare
---@return boolean
function IsoCell:IsBehindStuff(arg0) end

---@private
---@param arg0 IsoGridSquare
---@return int
function IsoCell:getShoreInt(arg0) end

---@public
---@return int @the worldX
function IsoCell:getWorldX() end

---@public
---@param x double
---@param y double
---@param z double
---@return IsoGridSquare
function IsoCell:getOrCreateGridSquare(x, y, z) end

---@public
---@param arg0 int
---@return void
function IsoCell:setRainSpeed(arg0) end

---@public
---@return OnceEvery @the dangerUpdate
function IsoCell:getDangerUpdate() end

---@private
---@return Texture
function IsoCell:getStencilTexture() end

---@public
---@return ArrayList|IsoSurvivor
function IsoCell:getSurvivorList() end

---throws java.io.IOException
---@public
---@return void
function IsoCell:savePlayer() end

---@public
---@param light IsoLightSource
---@return void
---@overload fun(x:int, y:int, z:int)
function IsoCell:removeLamppost(light) end

---@public
---@param x int
---@param y int
---@param z int
---@return void
function IsoCell:removeLamppost(x, y, z) end

---@public
---@param arg0 IsoWindow
---@return void
function IsoCell:removeFromWindowList(arg0) end

---@public
---@return KahluaTable
function IsoCell:getLuaObjectList() end

---@public
---@return ArrayList|IsoObject
function IsoCell:getProcessIsoObjectRemove() end

---@private
---@param arg0 Iterator|Unknown
---@return void
function IsoCell:ProcessItems(arg0) end

---@private
---@param arg0 IsoGridSquare
---@param arg1 int
---@return boolean
function IsoCell:IsDissolvedSquare(arg0, arg1) end

---@public
---@param col int
---@return int
function IsoCell:getRComponent(col) end

---@public
---@return int @the currentLY
function IsoCell:getCurrentLightY() end

---@public
---@return ArrayList|IsoGameCharacter @the RemoteSurvivorList
function IsoCell:getRemoteSurvivorList() end

---@public
---@param item KahluaTable
---@param player int
---@return void
function IsoCell:setDrag(item, player) end

---@public
---@param maxZ int @the maxZ to set
---@return void
function IsoCell:setMaxZ(maxZ) end

---@public
---@param x int
---@param y int
---@param z int
---@return IsoGridSquare
---@overload fun(x:double, y:double, z:double)
---@overload fun(x:Double, y:Double, z:Double)
function IsoCell:getGridSquare(x, y, z) end

---@public
---@param x double
---@param y double
---@param z double
---@return IsoGridSquare
function IsoCell:getGridSquare(x, y, z) end

---@public
---@param x Double
---@param y Double
---@param z Double
---@return IsoGridSquare
function IsoCell:getGridSquare(x, y, z) end

---@private
---@param arg0 IsoCell.SnowGrid
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 IsoGridSquare
---@param arg5 int
---@param arg6 Texture
---@param arg7 int
---@param arg8 int
---@param arg9 float
---@return void
function IsoCell:renderSnowTile(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) end

---@public
---@param worldY int @the worldY to set
---@return void
function IsoCell:setWorldY(worldY) end

---@public
---@param arg0 IsoObject
---@return void
function IsoCell:addToProcessIsoObject(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function IsoCell:resumeVehicleSounds(arg0) end

---@private
---@param arg0 int
---@return void
function IsoCell:RenderSnow(arg0) end

---@public
---@param xx int
---@param yy int
---@param zz int
---@return void
function IsoCell:EnsureSurroundNotNull(xx, yy, zz) end

---@private
---@param arg0 int
---@return void
function IsoCell:updateSnow(arg0) end

---@public
---@return IsoDirections
function IsoCell:FromMouseTile() end

---@public
---@param criteria IsoCell.BuildingSearchCriteria
---@param count int
---@return Stack|BuildingScore
function IsoCell:getBestBuildings(criteria, count) end

---@param arg0 BuildingScore
---@param arg1 BuildingScore[]
---@param arg2 IsoCell.BuildingSearchCriteria
---@return void
function IsoCell:Place(arg0, arg1, arg2) end

---@public
---@param maxX int @the maxX to set
---@return void
function IsoCell:setMaxX(maxX) end

---@param arg0 IsoGridSquare
---@param arg1 IsoGridSquare
---@return boolean
function IsoCell:isBlocked(arg0, arg1) end

---Description copied from interface: TileBasedMap
---
---get the height of the tile map. The slightly odd name is used to distiguish this method from commonly used names in game maps.
---
---Specified by:
---
---getHeightInTiles in interface TileBasedMap
---@public
---@return int @The number of tiles down the map
function IsoCell:getHeightInTiles() end

---@public
---@param arg0 InventoryItem
---@return void
---@overload fun(arg0:ArrayList|Unknown)
function IsoCell:addToProcessItemsRemove(arg0) end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function IsoCell:addToProcessItemsRemove(arg0) end

---@public
---@return boolean @the safeToAdd
function IsoCell:isSafeToAdd() end

---@private
---@return void
function IsoCell:initTileShaders() end

---@public
---@return int @the minZ
function IsoCell:getMinZ() end

---@public
---@return ArrayList|IsoZombie @the ZombieList
function IsoCell:getZombieList() end

---@public
---@param pl int
---@return IsoChunkMap
function IsoCell:getChunkMap(pl) end

---@public
---@return void
function IsoCell:ProcessSpottedRooms() end

---@private
---@param arg0 IsoBuilding
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return int
function IsoCell:GetBuildingHeightAt(arg0, arg1, arg2, arg3) end

---@private
---@return boolean
function IsoCell:SetCutawayRoomsForPlayer() end

---@public
---@param x int
---@param y int
---@param z int
---@param playerIndex int
---@return IsoGridSquare
function IsoCell:getGridSquareDirect(x, y, z, playerIndex) end

---@private
---@param arg0 IsoGridStack
---@param arg1 boolean[][][]
---@param arg2 boolean[][]
---@return void
function IsoCell:CullFullyOccludedSquares(arg0, arg1, arg2) end

---@public
---@param r float
---@param g float
---@param b float
---@param a float
---@return int
function IsoCell:toIntColor(r, g, b, a) end

---@public
---@return ArrayList|Unknown
function IsoCell:getVehicles() end

---@public
---@return void
function IsoCell:update() end

---@public
---@param minY int @the minY to set
---@return void
function IsoCell:setMinY(minY) end

---@private
---@param arg0 IsoCell.PerPlayerRender
---@param arg1 int
---@return void
function IsoCell:renderDebugLighting(arg0, arg1) end

---Description copied from interface: TileBasedMap
---
---get the width of the tile map. The slightly odd name is used to distiguish this method from commonly used names in game maps.
---
---Specified by:
---
---getWidthInTiles in interface TileBasedMap
---@public
---@return int @The number of tiles across the map
function IsoCell:getWidthInTiles() end

---@public
---@return ArrayList|Unknown
function IsoCell:getWindowList() end

---@public
---@param currentLX int @the currentLX to set
---@return void
function IsoCell:setCurrentLightX(currentLX) end

---@private
---@param arg0 IsoGridSquare
---@param arg1 IsoGridSquare
---@param arg2 int
---@param arg3 long
---@return boolean
function IsoCell:DoesSquareHaveValidCutawayPlayerWalls(arg0, arg1, arg2, arg3) end

---@public
---@return ArrayList|IsoMovingObject @the ObjectList
function IsoCell:getObjectList() end

---@public
---@param arg0 IsoHeatSource
---@return void
function IsoCell:addHeatSource(arg0) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 boolean
---@param arg4 ArrayList|Unknown
---@return void
function IsoCell:GetBuildingsInFrontOfCharacterSquare(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param playerIndex int
---@return void
function IsoCell:clearCacheGridSquare(playerIndex) end

---@private
---@param arg0 IsoGridSquare
---@param arg1 long
---@return boolean
function IsoCell:IsCutawaySquare(arg0, arg1) end

---@public
---@param safeToAdd boolean @the safeToAdd to set
---@return void
function IsoCell:setSafeToAdd(safeToAdd) end

---@public
---@return Stack|IsoLightSource @the LamppostPositions
function IsoCell:getLamppostPositions() end

---@private
---@return void
function IsoCell:renderWeatherFx() end

---Overrides:
---
---Dispose in class Bucket
---@public
---@return void
function IsoCell:Dispose() end

---@public
---@param zza int
---@return void
function IsoCell:RenderFloorShading(zza) end

---@private
---@param arg0 int
---@return void
function IsoCell:renderDebugPhysics(arg0) end

---@public
---@param arg0 IsoWindow
---@return void
function IsoCell:addToWindowList(arg0) end

---@public
---@return void
function IsoCell:DrawStencilMask() end

---@public
---@return LotHeader
function IsoCell:getCurrentLotHeader() end

---@public
---@param def RoomDef
---@return IsoGridSquare
function IsoCell:getFreeTile(def) end

---@public
---@param ID int
---@return IsoRoom
function IsoCell:getRoom(ID) end

---@public
---@return int
function IsoCell:getMaxFloors() end

---@public
---@return ArrayList|InventoryItem @the ProcessItems
function IsoCell:getProcessItems() end

---@private
---@param arg0 Texture
---@param arg1 int
---@param arg2 int
---@param arg3 float
---@param arg4 boolean
---@return void
function IsoCell:renderSnowTileBase(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param room IsoRoom
---@return void
function IsoCell:roomSpotted(room) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function IsoCell:putInVehicle(arg0) end

---@public
---@return IsoGridSquare
function IsoCell:getRandomOutdoorTile() end

---@public
---@param arg0 IsoGridSquare
---@param arg1 int
---@return boolean
function IsoCell:CanBuildingSquareOccludePlayer(arg0, arg1) end

---@private
---@return boolean
function IsoCell:initWeatherFx() end

---@public
---@return int @the maxZ
function IsoCell:getMaxZ() end

---@public
---@param x int
---@param y int
---@return boolean
function IsoCell:isInChunkMap(x, y) end

---@public
---@return IsoSpriteManager
function IsoCell:getSpriteManager() end

---@public
---@return int @the currentLX
function IsoCell:getCurrentLightX() end

---@public
---@param arg0 int
---@return void
function IsoCell:setRainAlpha(arg0) end

---@return IsoGridSquare
function IsoCell:getRandomFreeTile() end

---@public
---@param arg0 IsoHeatSource
---@return void
function IsoCell:removeHeatSource(arg0) end

---@private
---@param arg0 ArrayList|Unknown
---@param arg1 IsoBuilding
---@return void
function IsoCell:AddUniqueToBuildingList(arg0, arg1) end

---@param arg0 IsoPlayer
---@param arg1 IsoGridSquare
---@param arg2 ArrayList|Unknown
---@param arg3 ArrayList|Unknown
---@return void
function IsoCell:GetSquaresAroundPlayerSquare(arg0, arg1, arg2, arg3) end

---@public
---@param light IsoLightSource
---@return void
---@overload fun(x:int, y:int, z:int, r:float, g:float, b:float, rad:int)
function IsoCell:addLamppost(light) end

---@public
---@param x int
---@param y int
---@param z int
---@param r float
---@param g float
---@param b float
---@param rad int
---@return IsoLightSource
function IsoCell:addLamppost(x, y, z, r, g, b, rad) end

---@private
---@return void
function IsoCell:updateInternal() end

---@public
---@param RemoteID int
---@return IsoSurvivor
function IsoCell:getNetworkPlayer(RemoteID) end

---@public
---@return IsoWeatherFX
function IsoCell:getWeatherFX() end

---@public
---@return Stack|BuildingScore @the getBuildings
function IsoCell:getBuildings() end

---@public
---@return ArrayList|IsoPushableObject @the PushableObjectList
function IsoCell:getPushableObjectList() end

---@return IsoGridSquare
function IsoCell:getRandomOutdoorFreeTile() end

---@public
---@param arg0 int
---@return IsoZombie
function IsoCell:getNearestVisibleZombie(arg0) end

---@public
---@return int
function IsoCell:GetEffectivePlayerRoomId() end

---@public
---@return HashMap|Integer|BuildingScore @the BuildingScores
function IsoCell:getBuildingScores() end

---@public
---@param MaxHeight int
---@return void
function IsoCell:RenderTiles(MaxHeight) end

---@public
---@return ArrayList|InventoryItem @the ProcessItemsRemove
function IsoCell:getProcessItemsRemove() end

---@public
---@return int @the worldY
function IsoCell:getWorldY() end

---@public
---@return int @the minY
function IsoCell:getMinY() end

---@private
---@param arg0 int
---@return IsoCell.PerPlayerRender
function IsoCell:getPerPlayerRenderAt(arg0) end

---@public
---@param worldX int @the worldX to set
---@return void
function IsoCell:setWorldX(worldX) end

---@public
---@param sqThis IsoGridSquare
---@param x int
---@param y int
---@param zz int
---@param playerIndex int
---@return void
function IsoCell:CalculateVertColoursForTile(sqThis, x, y, zz, playerIndex) end

---@private
---@return void
function IsoCell:renderInternal() end

---@private
---@return void
function IsoCell:ProcessIsoObject() end

---@private
---@param arg0 IsoGridSquare
---@param arg1 int
---@param arg2 int
---@return boolean
function IsoCell:isSnowShore(arg0, arg1, arg2) end

---@public
---@param minX int @the minX to set
---@return void
function IsoCell:setMinX(minX) end

---@public
---@param x int
---@param y int
---@return int
function IsoCell:getDangerScore(x, y) end

---@public
---@param currentLY int @the currentLY to set
---@return void
function IsoCell:setCurrentLightY(currentLY) end

---@public
---@param arg0 int
---@param arg1 int
---@return IsoChunk
function IsoCell:getChunk(arg0, arg1) end

---@public
---@return ArrayList|IsoObject
function IsoCell:getProcessIsoObjects() end

---@public
---@return Thread @the LightInfoUpdate
function IsoCell:getLightInfoUpdate() end

---@public
---@param obj IsoMovingObject
---@return void
function IsoCell:Remove(obj) end

---@public
---@return int @the maxY
function IsoCell:getMaxY() end

---@public
---@return ArrayList|IsoRoom @the RoomList
function IsoCell:getRoomList() end

---@public
---@param x int
---@param y int
---@param z int
---@param square IsoGridSquare
---@return void
function IsoCell:setCacheGridSquare(x, y, z, square) end

---@public
---@param minZ int @the minZ to set
---@return void
function IsoCell:setMinZ(minZ) end

---throws java.io.IOException
---@public
---@param output DataOutputStream
---@param bDoChars boolean
---@return void
function IsoCell:save(output, bDoChars) end

---@public
---@param o IsoMovingObject
---@return void
function IsoCell:addMovingObject(o) end

---@private
---@param arg0 int
---@return void
function IsoCell:renderTilesInternal(arg0) end

---@private
---@param arg0 int
---@param arg1 boolean
---@return boolean
function IsoCell:doBuildingInternal(arg0, arg1) end

---@private
---@param arg0 IsoGridSquare
---@return boolean
function IsoCell:IsCollapsibleBuildingSquare(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return boolean
function IsoCell:gridSquareIsSnow(arg0, arg1, arg2) end

---@private
---@param arg0 int
---@param arg1 BuildingScore
---@param arg2 BuildingScore[]
---@return void
function IsoCell:InsertAt(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@return void
function IsoCell:setRainIntensity(arg0) end

---@public
---@param arg0 IsoWorldInventoryObject
---@return void
function IsoCell:addToProcessWorldItemsRemove(arg0) end

---@public
---@param arg0 int
---@return void
function IsoCell:setSnowTarget(arg0) end

---@public
---@return IsoCell
function IsoCell:getInstance() end

---@public
---@param scores Stack|BuildingScore
---@return void
function IsoCell:setBuildings(scores) end

---@private
---@param arg0 IsoCell.SnowGrid
---@param arg1 float
---@param arg2 IsoGridSquare
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 int
---@param arg7 int
---@param arg8 int
---@return void
function IsoCell:renderSnowTileGeneral(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@return ArrayList|IsoObject @the StaticUpdaterObjectList
function IsoCell:getStaticUpdaterObjectList() end

---@public
---@return int @the height
function IsoCell:getHeight() end

---@private
---@param arg0 Iterator|Unknown
---@return void
function IsoCell:ProcessObjects(arg0) end

---@public
---@param arg0 float
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return float
function IsoCell:getHeatSourceHighestTemperature(arg0, arg1, arg2, arg3) end

---@public
---@return void
function IsoCell:reloadRainTextures() end

---@public
---@param arg0 IsoObject
---@return void
function IsoCell:addToStaticUpdaterObjectList(arg0) end

---@public
---@param x int
---@param y int
---@param z int
---@return IsoGridSquare
function IsoCell:getRelativeGridSquare(x, y, z) end

---@private
---@param arg0 ArrayList|Unknown
---@param arg1 IsoGridSquare
---@param arg2 boolean
---@return void
function IsoCell:GetBuildingsInFrontOfCharacter(arg0, arg1, arg2) end

---throws java.lang.InterruptedException
---@public
---@return void
function IsoCell:render() end

---@public
---@param chr IsoGameCharacter
---@param except IsoRoom
---@return IsoBuilding
function IsoCell:getClosestBuildingExcept(chr, except) end

---@public
---@param arg0 IsoWorldInventoryObject
---@return void
function IsoCell:addToProcessWorldItems(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 boolean
---@return IsoGridSquare
function IsoCell:createNewGridSquare(arg0, arg1, arg2, arg3) end

---@public
---@return int @the minX
function IsoCell:getMinX() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return void
function IsoCell:setStencilValue2z(arg0, arg1, arg2, arg3) end

---@public
---@param maxY int @the maxY to set
---@return void
function IsoCell:setMaxY(maxY) end

---@public
---@return IsoGridSquare
function IsoCell:getRandomFreeTileInRoom() end

---@public
---@param col int
---@return int
function IsoCell:getGComponent(col) end

---@private
---@param arg0 IsoGridSquare
---@param arg1 IsoGridSquare
---@param arg2 IsoGridSquare
---@param arg3 IsoGridSquare
---@param arg4 int
---@param arg5 int
---@return int
function IsoCell:CalculateColor(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param width int @the width to set
---@return void
function IsoCell:setWidth(width) end

---Description copied from interface: TileBasedMap
---
---Check if the given location is blocked, i.e. blocks movement of the supplied mover.
---
---Specified by:
---
---blocked in interface TileBasedMap
---@public
---@param mover Mover @The mover that is potentially moving through the specified tile.
---@param x int @The x coordinate of the tile to check
---@param y int @The y coordinate of the tile to check
---@param z int
---@param lx int
---@param ly int
---@param lz int
---@return boolean @True if the location is blocked
function IsoCell:blocked(mover, x, y, z, lx, ly, lz) end

---@private
---@return void
function IsoCell:renderShadows() end

---@public
---@param arg0 IsoObject
---@return void
function IsoCell:addToProcessIsoObjectRemove(arg0) end

---@private
---@return void
function IsoCell:ProcessStaticUpdaters() end

---@public
---@param height int @the height to set
---@return void
function IsoCell:setHeight(height) end

---@public
---@return ArrayList|IsoMovingObject @the removeList
function IsoCell:getRemoveList() end

---@public
---@param x int
---@param y int
---@param z int
---@return IsoChunk
function IsoCell:getChunkForGridSquare(x, y, z) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return void
function IsoCell:setStencilValue(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@return void
function IsoCell:invalidatePeekedRoom(arg0) end

---@public
---@return void
function IsoCell:updateHeatSources() end

---@private
---@return void
function IsoCell:renderRain() end

---@public
---@return int @the currentLZ
function IsoCell:getCurrentLightZ() end

---@public
---@param x int
---@param y int
---@return void
function IsoCell:checkHaveRoof(x, y) end

---@public
---@return int @the width
function IsoCell:getWidth() end
