---@class IsoWorld : zombie.iso.IsoWorld
---@field private weather String
---@field public MetaGrid IsoMetaGrid
---@field private randomizedBuildingList ArrayList|Unknown
---@field private randomizedZoneList ArrayList|Unknown
---@field private randomizedVehicleStoryList ArrayList|Unknown
---@field private RBBasic RandomizedBuildingBase
---@field private spawnedZombieZone HashMap|Unknown|Unknown
---@field private allTiles HashMap|Unknown|Unknown
---@field private flashIsoCursorA float
---@field private flashIsoCursorInc boolean
---@field public sky SkyBox
---@field private m_setAnimationRecordingTriggerWatcher PredicatedFileWatcher
---@field private m_animationRecorderActive boolean
---@field private m_animationRecorderDiscard boolean
---@field private timeSinceLastSurvivorInHorde int
---@field private m_frameNo int
---@field public helicopter Helicopter
---@field public Characters ArrayList|Unknown
---@field private freeEmitters ArrayDeque|Unknown
---@field private currentEmitters ArrayList|Unknown
---@field private emitterOwners HashMap|Unknown|Unknown
---@field public x int
---@field public y int
---@field public CurrentCell IsoCell
---@field public instance IsoWorld
---@field public TotalSurvivorsDead int
---@field public TotalSurvivorNights int
---@field public SurvivorSurvivalRecord int
---@field public SurvivorDescriptors HashMap|Integer|SurvivorDesc
---@field public AddCoopPlayers ArrayList|AddCoopPlayer
---@field private compScoreToPlayer IsoWorld.CompScoreToPlayer
---@field compDistToPlayer IsoWorld.CompDistToPlayer
---@field public mapPath String
---@field public mapUseJar boolean
---@field bLoaded boolean
---@field public PropertyValueMap HashMap|Unknown|Unknown
---@field private WorldX int
---@field private WorldY int
---@field private luaDesc SurvivorDesc
---@field private luatraits ArrayList|Unknown
---@field private luaSpawnCellX int
---@field private luaSpawnCellY int
---@field private luaPosX int
---@field private luaPosY int
---@field private luaPosZ int
---@field public WorldVersion int
---@field public WorldVersion_Barricade int
---@field public WorldVersion_SandboxOptions int
---@field public WorldVersion_FliesSound int
---@field public WorldVersion_LootRespawn int
---@field public WorldVersion_OverlappingGenerators int
---@field public WorldVersion_ItemContainerIdenticalItems int
---@field public WorldVersion_VehicleSirenStartTime int
---@field public WorldVersion_CompostLastUpdated int
---@field public WorldVersion_DayLengthHours int
---@field public WorldVersion_LampOnPillar int
---@field public WorldVersion_AlarmClockRingSince int
---@field public WorldVersion_ClimateAdded int
---@field public WorldVersion_VehicleLightFocusing int
---@field public WorldVersion_GeneratorFuelFloat int
---@field public WorldVersion_InfectionTime int
---@field public WorldVersion_ClimateColors int
---@field public WorldVersion_BodyLocation int
---@field public WorldVersion_CharacterModelData int
---@field public WorldVersion_CharacterModelData2 int
---@field public WorldVersion_CharacterModelData3 int
---@field public WorldVersion_HumanVisualBlood int
---@field public WorldVersion_ItemContainerIdenticalItemsInt int
---@field public WorldVersion_PerkName int
---@field public WorldVersion_Thermos int
---@field public WorldVersion_AllPatches int
---@field public WorldVersion_ZombieRotStage int
---@field public WorldVersion_NewSandboxLootModifier int
---@field public WorldVersion_KateBobStorm int
---@field public WorldVersion_DeadBodyAngle int
---@field public WorldVersion_ChunkSpawnedRooms int
---@field public WorldVersion_DeathDragDown int
---@field public WorldVersion_CanUpgradePerk int
---@field public WorldVersion_ItemVisualFullType int
---@field public WorldVersion_VehicleBlood int
---@field public WorldVersion_DeadBodyZombieRotStage int
---@field public WorldVersion_Fitness int
---@field public WorldVersion_DeadBodyFakeDead int
---@field public WorldVersion_Fitness2 int
---@field public WorldVersion_NewFog int
---@field public WorldVersion_DeadBodyPersistentOutfitID int
---@field public WorldVersion_VehicleTowingID int
---@field public WorldVersion_VehicleJNITransform int
---@field public WorldVersion_VehicleTowAttachment int
---@field public WorldVersion_ContainerMaxCapacity int
---@field public WorldVersion_TimedActionInstantCheat int
---@field public WorldVersion_ClothingPatchSaveLoad int
---@field public WorldVersion_AttachedSlotType int
---@field public WorldVersion_NoiseMakerDuration int
---@field public WorldVersion_ChunkVehicles int
---@field public WorldVersion_PlayerVehicleSeat int
---@field public WorldVersion_MediaDisksAndTapes int
---@field public WorldVersion_AlreadyReadBooks1 int
---@field public WorldVersion_LampOnPillar2 int
---@field public WorldVersion_AlreadyReadBooks2 int
---@field public WorldVersion_PolygonZone int
---@field public WorldVersion_PolylineZone int
---@field public SavedWorldVersion int
---@field public bDrawWorld boolean
---@field private zombieWithModel ArrayList|Unknown
---@field private zombieWithoutModel ArrayList|Unknown
---@field public NoZombies boolean
---@field public TotalWorldVersion int
---@field public saveoffsetx int
---@field public saveoffsety int
---@field public bDoChunkMapUpdate boolean
---@field private emitterUpdateMS long
---@field public emitterUpdate boolean
IsoWorld = {}

---@public
---@param mode String
---@return void
function IsoWorld:setGameMode(mode) end

---@public
---@return HashMap|Unknown|Unknown
---@overload fun(arg0:String)
function IsoWorld:getAllTiles() end

---@public
---@param arg0 String
---@return ArrayList|Unknown
function IsoWorld:getAllTiles(arg0) end

---@public
---@return int
function IsoWorld:getTimeSinceLastSurvivorInHorde() end

---@public
---@return String
function IsoWorld:getDifficulty() end

---throws java.io.EOFException, java.io.IOException
---@public
---@param _in RandomAccessFile
---@return int
---@overload fun(arg0:InputStream)
function IsoWorld:readInt(_in) end

---@public
---@param arg0 InputStream
---@return int
function IsoWorld:readInt(arg0) end

---@public
---@return IsoPuddles
function IsoWorld:getPuddlesManager() end

---throws java.io.EOFException, java.io.IOException
---@public
---@param _in RandomAccessFile
---@return String
---@overload fun(arg0:InputStream)
function IsoWorld:readString(_in) end

---@public
---@param arg0 InputStream
---@return String
function IsoWorld:readString(arg0) end

---@public
---@param luaSpawnCellY int
---@return void
function IsoWorld:setLuaSpawnCellY(luaSpawnCellY) end

---@public
---@param wx int
---@param wy int
---@return IsoMetaChunk
function IsoWorld:getMetaChunk(wx, wy) end

---@public
---@param arg0 BaseSoundEmitter
---@param arg1 IsoObject
---@return void
function IsoWorld:setEmitterOwner(arg0, arg1) end

---@public
---@return IsoCell
function IsoWorld:getCell() end

---@public
---@param world String
---@return void
function IsoWorld:setWorld(world) end

---@public
---@param arg0 String
---@return RandomizedVehicleStoryBase
function IsoWorld:getRandomizedVehicleStoryByName(arg0) end

---@public
---@param trait String
---@return void
function IsoWorld:addLuaTrait(trait) end

---@public
---@param globalTemperature float
---@return void
function IsoWorld:setGlobalTemperature(globalTemperature) end

---@public
---@return BaseSoundEmitter
---@overload fun(arg0:float, arg1:float, arg2:float)
function IsoWorld:getFreeEmitter() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return BaseSoundEmitter
function IsoWorld:getFreeEmitter(arg0, arg1, arg2) end

---@public
---@return ClimateManager
function IsoWorld:getClimateManager() end

---@public
---@param luaPosZ int
---@return void
function IsoWorld:setLuaPosZ(luaPosZ) end

---@public
---@return void
function IsoWorld:transmitWeather() end

---@public
---@return int
function IsoWorld:getLuaSpawnCellX() end

---@public
---@return float
function IsoWorld:getWorldAgeDays() end

---@private
---@param arg0 IsoSpriteManager
---@param arg1 int
---@param arg2 String
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@return void
function IsoWorld:addJumboTreeTileset(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@return float
function IsoWorld:getGlobalTemperature() end

---@public
---@return ArrayList|Unknown
function IsoWorld:getRandomizedBuildingList() end

---@public
---@return int
function IsoWorld:getLuaPosZ() end

---@public
---@return int
function IsoWorld:getFrameNo() end

---@private
---@param arg0 IsoSpriteManager
---@param arg1 int
---@return void
function IsoWorld:JumboTreeDefinitions(arg0, arg1) end

---@public
---@param b boolean
---@return void
function IsoWorld:setDrawWorld(b) end

---@public
---@return HashMap|Unknown|Unknown
function IsoWorld:getSpawnedZombieZone() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 KahluaTable
---@return void
function IsoWorld:registerSpawnOrigin(arg0, arg1, arg2, arg3, arg4) end

---@private
---@return void
function IsoWorld:DrawIsoCursorHelper() end

---@public
---@return boolean
function IsoWorld:getZombiesEnabled() end

---@public
---@return String
function IsoWorld:getWeather() end

---@public
---@return void
function IsoWorld:KillCell() end

---@private
---@param arg0 Map|Unknown|Unknown
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@return void
function IsoWorld:saveMovableStats(arg0, arg1, arg2, arg3, arg4, arg5) end

---@private
---@return void
function IsoWorld:updateInternal() end

---@public
---@return ArrayList|Unknown
function IsoWorld:getAllTilesName() end

---@public
---@return ArrayList|String
function IsoWorld:getLuaTraits() end

---@public
---@return IsoMetaGrid
function IsoWorld:getMetaGrid() end

---@public
---@return int
function IsoWorld:getWorldSquareY() end

---@public
---@param luaSpawnCellX int
---@return void
function IsoWorld:setLuaSpawnCellX(luaSpawnCellX) end

---@public
---@param name String
---@param type String
---@param x int
---@param y int
---@param z int
---@param width int
---@param height int
---@return IsoMetaGrid.Zone
function IsoWorld:registerZone(name, type, x, y, z, width, height) end

---@public
---@param desc SurvivorDesc
---@return void
function IsoWorld:setLuaPlayerDesc(desc) end

---@public
---@return int
function IsoWorld:getLuaPosX() end

---@public
---@return void
function IsoWorld:render() end

---@public
---@return void
function IsoWorld:checkVehiclesZones() end

---@public
---@return void
function IsoWorld:sceneCullZombies() end

---@private
---@return void
function IsoWorld:renderInternal() end

---@public
---@return boolean
function IsoWorld:getZombiesDisabled() end

---@private
---@return void
function IsoWorld:GenerateTilePropertyLookupTables() end

---@public
---@return String
function IsoWorld:getMap() end

---@public
---@return void
function IsoWorld:ForceKillAllZombies() end

---throws java.io.FileNotFoundException, java.io.IOException
---@public
---@return void
function IsoWorld:init() end

---@public
---@return String
function IsoWorld:getWorld() end

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
function IsoWorld:registerMannequinZone(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@return void
function IsoWorld:update() end

---@public
---@return String
function IsoWorld:getGameMode() end

---@public
---@param arg0 String
---@return void
function IsoWorld:setDifficulty(arg0) end

---@public
---@param name String
---@param type String
---@param x int
---@param y int
---@param z int
---@param width int
---@param height int
---@return IsoMetaGrid.Zone
function IsoWorld:registerZoneNoOverlap(name, type, x, y, z, width, height) end

---@return int
function IsoWorld:readWorldVersion() end

---@public
---@param arg0 BaseSoundEmitter
---@return void
function IsoWorld:takeOwnershipOfEmitter(arg0) end

---@public
---@param luaPosX int
---@return void
function IsoWorld:setLuaPosX(luaPosX) end

---@public
---@return RandomizedBuildingBase
function IsoWorld:getRBBasic() end

---@public
---@param wx int
---@param wy int
---@return IsoMetaChunk
function IsoWorld:getMetaChunkFromTile(wx, wy) end

---@private
---@param arg0 TriggerSetAnimationRecorderFile
---@return void
function IsoWorld:onTrigger_setAnimationRecorderTriggerFile(arg0) end

---@public
---@param bb ByteBuffer
---@return IsoObject
function IsoWorld:getItemFromXYZIndexBuffer(bb) end

---@private
---@return void
function IsoWorld:SetCustomPropertyValues() end

---@public
---@return void
function IsoWorld:renderTerrain() end

---@public
---@param arg0 IsoSpriteManager
---@param arg1 String
---@param arg2 int
---@return void
function IsoWorld:LoadTileDefinitionsPropertyStrings(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@return void
function IsoWorld:removeZonesForLotDirectory(arg0) end

---@public
---@return int
function IsoWorld:getWorldSquareX() end

---@public
---@param num int
---@param x1 int
---@param y1 int
---@param x2 int
---@param y2 int
---@return void
function IsoWorld:CreateSwarm(num, x1, y1, x2, y2) end

---@public
---@return ArrayList|Unknown
function IsoWorld:getRandomizedZoneList() end

---@public
---@return int
function IsoWorld:getLuaPosY() end

---@public
---@return ArrayList|Unknown
function IsoWorld:getRandomizedVehicleStoryList() end

---@public
---@return int
function IsoWorld:getWorldVersion() end

---@public
---@param arg0 BaseSoundEmitter
---@return void
function IsoWorld:returnOwnershipOfEmitter(arg0) end

---@public
---@param arg0 int
---@return void
function IsoWorld:setTimeSinceLastSurvivorInHorde(arg0) end

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
function IsoWorld:registerVehiclesZone(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@return boolean
function IsoWorld:isAnimRecorderActive() end

---@public
---@return boolean
function IsoWorld:isAnimRecorderDiscardTriggered() end

---@public
---@return SurvivorDesc
function IsoWorld:getLuaPlayerDesc() end

---@public
---@param sprMan IsoSpriteManager
---@param filename String
---@param fileNumber int
---@return void
function IsoWorld:LoadTileDefinitions(sprMan, filename, fileNumber) end

---@public
---@param desc SurvivorDesc
---@param sq IsoGridSquare
---@param player IsoPlayer
---@return IsoSurvivor
function IsoWorld:CreateRandomSurvivor(desc, sq, player) end

---@private
---@return void
function IsoWorld:initMessaging() end

---@public
---@param weather String
---@return void
function IsoWorld:setWeather(weather) end

---throws java.io.FileNotFoundException, java.io.IOException
---@public
---@return boolean
function IsoWorld:LoadPlayerForInfo() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function IsoWorld:registerWaterFlow(arg0, arg1, arg2, arg3) end

---@private
---@return void
function IsoWorld:PopulateCellWithSurvivors() end

---@public
---@return int
function IsoWorld:getLuaSpawnCellY() end

---@public
---@param x int
---@param y int
---@param z int
---@return boolean
function IsoWorld:isValidSquare(x, y, z) end

---@public
---@param world String
---@return void
function IsoWorld:setMap(world) end

---@public
---@param luaPosY int
---@return void
function IsoWorld:setLuaPosY(luaPosY) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@return void
function IsoWorld:registerWaterZone(arg0, arg1, arg2, arg3, arg4, arg5) end
