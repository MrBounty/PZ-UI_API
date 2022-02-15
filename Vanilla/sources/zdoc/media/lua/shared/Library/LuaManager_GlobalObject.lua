---@class LuaManager.GlobalObject : zombie.Lua.LuaManager.GlobalObject
---@field outStream FileOutputStream
---@field inStream FileInputStream
---@field inFileReader FileReader
---@field inBufferedReader BufferedReader
---@field timeLastRefresh long
---@field private timSortComparator LuaManager.GlobalObject.TimSortComparator
LuaManager_GlobalObject = {}

---@public
---@param module String
---@param command String
---@param args KahluaTable
---@return void
---@overload fun(player:IsoPlayer, module:String, command:String, args:KahluaTable)
function sendClientCommand(module, command, args) end

---@public
---@param player IsoPlayer
---@param module String
---@param command String
---@param args KahluaTable
---@return void
function sendClientCommand(player, module, command, args) end

---@public
---@return boolean
function isServer() end

---@public
---@param arg0 String
---@return void
function ProcessSafehouseMessage(arg0) end

---@public
---@param joypad int
---@return float
function getJoypadAimingAxisX(joypad) end

---@public
---@param x float
---@param ui UIElement
---@param zoom float
---@param xpos float
---@return float
function translatePointXInOverheadMapToWindow(x, ui, zoom, xpos) end

---@public
---@param mod ChooseGameInfo.Mod
---@return boolean
function isModActive(mod) end

---@public
---@param arg0 int
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return float
function screenToIsoX(arg0, arg1, arg2, arg3) end

---@public
---@return void
function reloadControllerConfigFiles() end

---@public
---@param arg0 String
---@return void
function connectToServerStateCallback(arg0) end

---@public
---@return IsoCell
function getCell() end

---@public
---@param onlineID int
---@param i int
---@param doctorLevel int
---@return void
function sendRemoveBullet(onlineID, i, doctorLevel) end

---@public
---@param command String
---@return void
function SendCommandToServer(command) end

---@public
---@return KahluaTable
function getFriendsList() end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 String
---@return void
function removeUserlog(arg0, arg1, arg2) end

---@public
---@return void
function reloadSoundFiles() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 boolean
---@return void
function addZombiesEating(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 IsoObject
---@param arg1 ItemContainer
---@return void
function sendItemsInContainer(arg0, arg1) end

---@public
---@param arg0 String
---@return void
function showFolderInDesktop(arg0) end

---@public
---@return int
function getMouseYScaled() end

---@public
---@return void
function scoreboardUpdate() end

---@public
---@param id int
---@param bActive boolean
---@return void
function setPlayerMovementActive(id, bActive) end

---@public
---@return KahluaTable
function getSaveDirectoryTable() end

---@public
---@param arg0 boolean
---@return void
function setBehaviorStep(arg0) end

---throws java.io.IOException
---@public
---@param filename String
---@param createIfNull boolean
---@return BufferedReader
function getFileReader(filename, createIfNull) end

---@public
---@return String
function getOnlineUsername() end

---@public
---@param loggerName String
---@param logs String
---@return void
function writeLog(loggerName, logs) end

---@public
---@param file String
---@param line int
---@return boolean
function isCurrentExecutionPoint(file, line) end

---@public
---@param source IsoObject
---@param x int
---@param y int
---@param z int
---@param radius int
---@param volume int
---@return void
function addSound(source, x, y, z, radius, volume) end

---@public
---@param arg0 String
---@return void
function ProcessAdminChatMessage(arg0) end

---@public
---@return void
---@overload fun(fileName:String)
function takeScreenshot() end

---@public
---@param fileName String
---@return void
function takeScreenshot(fileName) end

---@public
---@param arg0 long
---@param arg1 IsoPlayer
---@return void
function InvMngRemoveItem(arg0, arg1) end

---@public
---@param arg0 String
---@return ArrayList|Unknown
function getMapFoldersForMod(arg0) end

---@public
---@return IsoRegionsRenderer
function isoRegionsRenderer() end

---@public
---@return boolean
function isAdmin() end

---@public
---@return KahluaTable
function getServerSpawnRegions() end

---@public
---@param filename String
---@return boolean
function fileExists(filename) end

---@public
---@return long
function getTimestamp() end

---@public
---@param arg0 IsoPlayer
---@return void
function sendVisual(arg0) end

---@public
---@return ArrayList|Unknown
function getAllBeardStyles() end

---@public
---@return GameClient
function getGameClient() end

---@public
---@return void
function breakpoint() end

---@public
---@param arg0 String
---@return String
function getSteamProfileNameFromSteamID(arg0) end

---@public
---@return void
---@overload fun(arg0:Predicate|Unknown)
function addAllVehicles() end

---@public
---@param arg0 Predicate|Unknown
---@return void
function addAllVehicles(arg0) end

---@public
---@param arg0 String
---@return ArrayList|Unknown
function getSteamWorkshopItemMods(arg0) end

---@public
---@param joypad int
---@return boolean
function isJoypadUp(joypad) end

---@public
---@param txt String
---@return String
---@overload fun(txt:String, arg1:Object)
---@overload fun(txt:String, arg1:Object, arg2:Object)
---@overload fun(txt:String, arg1:Object, arg2:Object, arg3:Object)
---@overload fun(txt:String, arg1:Object, arg2:Object, arg3:Object, arg4:Object)
function getText(txt) end

---@public
---@param txt String
---@param arg1 Object
---@return String
function getText(txt, arg1) end

---@public
---@param txt String
---@param arg1 Object
---@param arg2 Object
---@return String
function getText(txt, arg1, arg2) end

---@public
---@param txt String
---@param arg1 Object
---@param arg2 Object
---@param arg3 Object
---@return String
function getText(txt, arg1, arg2, arg3) end

---@public
---@param txt String
---@param arg1 Object
---@param arg2 Object
---@param arg3 Object
---@param arg4 Object
---@return String
function getText(txt, arg1, arg2, arg3, arg4) end

---@public
---@param joypad int
---@return int
function getJoypadAButton(joypad) end

---@public
---@param modDir String
---@return ChooseGameInfo.Mod
function getModInfo(modDir) end

---@public
---@param arg0 String
---@param arg1 int
---@return void
function getTableResult(arg0, arg1) end

---@public
---@param joypad int
---@return int
function getJoypadRBumper(joypad) end

---@public
---@return String
function getClientUsername() end

---@public
---@param arg0 String
---@return boolean
function isAccessLevel(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function addZombieSitting(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@return void
function showWorldMapEditor(arg0) end

---@public
---@param url String
---@return DataInputStream
function getUrlInputStream(url) end

---@public
---@param arg0 String
---@return IsoSpriteManager
function getSpriteManager(arg0) end

---@public
---@param o Object
---@param field Field
---@return Object
function getClassFieldVal(o, field) end

---@public
---@param doCharacter boolean
---@return void
function save(doCharacter) end

---@public
---@param arg0 int
---@return int
function getControllerAxisCount(arg0) end

---@public
---@param arg0 IsoPlayer
---@param arg1 IsoPlayer
---@param arg2 boolean
---@return void
function acceptTrading(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return float
function screenToIsoY(arg0, arg1, arg2, arg3) end

---@public
---@return KahluaTable
function createRegionFile() end

---@public
---@return KahluaTable
function getGameVersionInfo() end

---@public
---@param file String
---@param line int
---@return void
function toggleBreakpoint(file, line) end

---@public
---@return void
function screenZoomOut() end

---@public
---@return void
function activateSteamOverlayToWorkshop() end

---@public
---@param file String
---@return void
function deleteSave(file) end

---@public
---@param _table KahluaTable
---@return KahluaTable
function copyTable(_table) end

---@public
---@param arg0 KahluaTable
---@param arg1 Object
---@return void
function timSort(arg0, arg1) end

---@public
---@param arg0 String
---@return Boolean
function checkPlayerCanUseChat(arg0) end

---@public
---@param player int
---@param x float
---@param y float
---@param z float
---@return float
function isoToScreenX(player, x, y, z) end

---@public
---@param joypad int
---@return float
function getJoypadAimingAxisY(joypad) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 String
---@return Model
function loadSkinnedZomboidModel(arg0, arg1, arg2) end

---@public
---@return void
function endTextFileInput() end

---@public
---@return String
function getServerAddressFromArgs() end

---@public
---@param doIt boolean
---@return void
function doKeyPress(doIt) end

---@public
---@param player int
---@param joypad int
---@param playerObj IsoPlayer
---@param username String
---@return void
function setPlayerJoypad(player, joypad, playerObj, username) end

---@public
---@param obj Object
---@param name String
---@return boolean
function instof(obj, name) end

---@public
---@param _table KahluaTable
---@param key Object
---@return boolean
function hasDataReadBreakpoint(_table, key) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 String
---@param arg5 Integer
---@return ArrayList|Unknown
---@overload fun(arg0:int, arg1:int, arg2:int, arg3:int, arg4:String, arg5:Integer, arg6:boolean, arg7:boolean, arg8:boolean, arg9:boolean, arg10:float)
function addZombiesInOutfit(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 String
---@param arg5 Integer
---@param arg6 boolean
---@param arg7 boolean
---@param arg8 boolean
---@param arg9 boolean
---@param arg10 float
---@return ArrayList|Unknown
function addZombiesInOutfit(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) end

---@public
---@param arg0 long
---@return void
function stopSound(arg0) end

---@public
---@return boolean
function canModifyPlayerStats() end

---@public
---@param arg0 IsoPlayer
---@param arg1 ArrayList|Unknown
---@param arg2 IsoPlayer
---@param arg3 String
---@param arg4 String
---@return boolean
function sendItemListNet(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 SafeHouse
---@param arg1 String
---@return void
function acceptSafehouseInvite(arg0, arg1) end

---@public
---@param newName String
---@param oldName String
---@return Item
function cloneItemType(newName, oldName) end

---@public
---@param mapDir String
---@return KahluaTable
function getMapInfo(mapDir) end

---@public
---@return void
function addAllSmashedVehicles() end

---@public
---@param arg0 int
---@return BaseVehicle
function getVehicleById(arg0) end

---@public
---@return long
function getTimestampMs() end

---@public
---@param arg0 Faction
---@param arg1 IsoPlayer
---@param arg2 String
---@return void
function sendFactionInvite(arg0, arg1, arg2) end

---@public
---@param x float
---@param ui UIElement
---@param zoom float
---@param xpos float
---@return float
function translatePointXInOverheadMapToWorld(x, ui, zoom, xpos) end

---@public
---@param arg0 String
---@return void
function processGeneralMessage(arg0) end

---@public
---@return void
function resumeSoundAndMusic() end

---@public
---@param c Coroutine
---@param n int
---@return Object
function getCoroutineObjStack(c, n) end

---@public
---@return KahluaTable
function getMapDirectoryTable() end

---@public
---@return DebugOptions
function getDebugOptions() end

---@public
---@param x float
---@param y float
---@param x2 float
---@param y2 float
---@param z float
---@param count int
---@return void
function spawnHorde(x, y, x2, y2, z, count) end

---@public
---@param arg0 int
---@return boolean
function isJoypadRTPressed(arg0) end

---@public
---@return int
function getNumActivePlayers() end

---@public
---@param b boolean
---@return void
function setShowPausedMessage(b) end

---@public
---@param arg0 int
---@return IsoPlayer
function getPlayerByOnlineID(arg0) end

---@public
---@param _table KahluaTable
---@param key Object
---@return void
function toggleBreakOnRead(_table, key) end

---@public
---@param arg0 int
---@return boolean
function isJoypadLTPressed(arg0) end

---@public
---@param playerObj IsoPlayer
---@return void
function setPlayerMouse(playerObj) end

---@public
---@return ClimateManager
function getClimateManager() end

---@public
---@return long
function getTimeInMillis() end

---@public
---@param arg0 boolean
---@return ArrayList|Unknown
function getAllHairStyles(arg0) end

---@public
---@return void
function resetRegionFile() end

---@public
---@param arg0 String
---@return void
function processSayMessage(arg0) end

---@public
---@param challenge KahluaTable
---@return void
function doChallenge(challenge) end

---@public
---@param arg0 IsoPlayer
---@param arg1 IsoPlayer
---@param arg2 int
---@return void
function tradingUISendUpdateState(arg0, arg1, arg2) end

---@public
---@return IsoGameCharacter
function getBehaviourDebugPlayer() end

---@public
---@param fileName String
---@return void
function deletePlayerSave(fileName) end

---@public
---@return ArrayList|Unknown
function getConnectedPlayers() end

---@public
---@param filename String
---@return Texture
function getTexture(filename) end

---@public
---@return IsoPuddles
function getPuddlesManager() end

---@public
---@param arg0 String
---@return void
function processShoutMessage(arg0) end

---@public
---@param bUse boolean
---@return void
function useTextureFiltering(bUse) end

---@public
---@param txt String
---@return String
---@overload fun(arg0:String, arg1:Object)
---@overload fun(arg0:String, arg1:Object, arg2:Object)
---@overload fun(arg0:String, arg1:Object, arg2:Object, arg3:Object)
---@overload fun(arg0:String, arg1:Object, arg2:Object, arg3:Object, arg4:Object)
function getTextOrNull(txt) end

---@public
---@param arg0 String
---@param arg1 Object
---@return String
function getTextOrNull(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 Object
---@param arg2 Object
---@return String
function getTextOrNull(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 Object
---@param arg2 Object
---@param arg3 Object
---@return String
function getTextOrNull(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@param arg1 Object
---@param arg2 Object
---@param arg3 Object
---@param arg4 Object
---@return String
function getTextOrNull(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 String
---@return void
function serverConnectCoop(arg0) end

---@public
---@param player IsoPlayer
---@param value int
---@return void
function setProgressBarValue(player, value) end

---throws java.io.IOException
---@public
---@return List|BufferedReader
function getSandboxPresets() end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 int
---@return void
function addTicket(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@return String
function proceedPM(arg0) end

---@public
---@param arg0 String
---@return int
function getKeyCode(arg0) end

---@public
---@param arg0 String
---@return String
function getAbsoluteSaveFolderName(arg0) end

---@public
---@return IsoWorld
function getWorld() end

---@public
---@param arg0 int
---@return void
function removeTicket(arg0) end

---@public
---@param event String
---@return void
---@overload fun(event:String, param:Object)
---@overload fun(event:String, param:Object, param2:Object)
---@overload fun(event:String, param:Object, param2:Object, param3:Object)
---@overload fun(event:String, param:Object, param2:Object, param3:Object, param4:Object)
function triggerEvent(event) end

---@public
---@param event String
---@param param Object
---@return void
function triggerEvent(event, param) end

---@public
---@param event String
---@param param Object
---@param param2 Object
---@return void
function triggerEvent(event, param, param2) end

---@public
---@param event String
---@param param Object
---@param param2 Object
---@param param3 Object
---@return void
function triggerEvent(event, param, param2, param3) end

---@public
---@param event String
---@param param Object
---@param param2 Object
---@param param3 Object
---@param param4 Object
---@return void
function triggerEvent(event, param, param2, param3, param4) end

---@public
---@param arg0 IsoPlayer
---@param arg1 PerkFactory.Perk
---@param arg2 int
---@param arg3 boolean
---@param arg4 boolean
---@return void
function sendAddXp(arg0, arg1, arg2, arg3, arg4) end

---@public
---@return ClimateMoon
function getClimateMoon() end

---@public
---@param arg0 String
---@param arg1 int
---@return boolean
function steamRequestServerRules(arg0, arg1) end

---@public
---@param module String
---@param command String
---@param args KahluaTable
---@return void
---@overload fun(player:IsoPlayer, module:String, command:String, args:KahluaTable)
function sendServerCommand(module, command, args) end

---@public
---@param player IsoPlayer
---@param module String
---@param command String
---@param args KahluaTable
---@return void
function sendServerCommand(player, module, command, args) end

---@public
---@param worldName String
---@return String
function sanitizeWorldName(worldName) end

---@public
---@param arg0 String
---@return int
function getServerSavedWorldVersion(arg0) end

---@public
---@param player int
---@param x float
---@param y float
---@param z float
---@return float
function isoToScreenY(player, x, y, z) end

---@public
---@return BaseVehicle
function addPhysicsObject() end

---@public
---@param onlineID int
---@param i int
---@param infected boolean
---@return void
function sendWoundInfection(onlineID, i, infected) end

---@public
---@param filename String
---@return String
function getLastPlayedDate(filename) end

---@public
---@param arg0 String
---@param arg1 IsoGridSquare
---@return void
function playServerSound(arg0, arg1) end

---@public
---@param arg0 String
---@return String
function checkServerName(arg0) end

---@public
---@return boolean
function isSystemWindows() end

---@public
---@param arg0 SafeHouse
---@param arg1 IsoPlayer
---@param arg2 String
---@return void
function sendSafehouseInvite(arg0, arg1, arg2) end

---@public
---@return boolean
function isIngameState() end

---@public
---@return int
function steamRequestInternetServersCount() end

---@public
---@return String
function getServerPasswordFromArgs() end

---@public
---@return ContainerOverlays
function getContainerOverlays() end

---@public
---@return Stack|EvolvedRecipe
function getEvolvedRecipes() end

---@public
---@return ArrayList|Unknown
function getAllRecipes() end

---@public
---@param arg0 String
---@param arg1 boolean
---@param arg2 boolean
---@return void
function updateChatSettings(arg0, arg1, arg2) end

---@public
---@return void
function activateSteamOverlayToWorkshopUser() end

---@public
---@param arg0 IsoZombie
---@return KahluaTable
function getZombieInfo(arg0) end

---@public
---@return String
function getServerName() end

---@public
---@return KahluaTable
function getPublicServersList() end

---@public
---@return SLSoundManager
function getSLSoundManager() end

---@public
---@param _number int
---@return boolean
function isMouseButtonDown(_number) end

---@public
---@return KahluaTable
function getServerList() end

---@public
---@param arg0 String
---@return void
function getTickets(arg0) end

---@public
---@param y float
---@param ui UIElement
---@param zoom float
---@param ypos float
---@return float
function translatePointYInOverheadMapToWorld(y, ui, zoom, ypos) end

---@public
---@return IsoPlayer
function getPlayer() end

---@public
---@param arg0 int
---@return float
function getControllerPovX(arg0) end

---@public
---@return void
function getServerModData() end

---@public
---@return void
function showChunkDebugger() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 int
---@return void
function renderIsoCircle(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@return boolean
function canSeePlayerStats() end

---@public
---@param arg0 String
---@param arg1 String
---@return void
function manipulateSavefile(arg0, arg1) end

---@public
---@param c Coroutine
---@param n int
---@return int
function getLocalVarStack(c, n) end

---@public
---@return KahluaTable
function getPing() end

---@public
---@param joypad int
---@return int
function getJoypadLBumper(joypad) end

---@public
---@param arg0 String
---@return Texture
function getSteamAvatarFromSteamID(arg0) end

---@public
---@param arg0 int
---@return String
function getKeyName(arg0) end

---@public
---@return IsoMarkers
function getIsoMarkers() end

---@public
---@param arg0 int
---@return boolean
function isJoypadConnected(arg0) end

---@public
---@return List|String
function getMods() end

---@public
---@param x int
---@param y int
---@param z int
---@return IsoMetaGrid.Zone
function getZone(x, y, z) end

---@public
---@return void
function endFileInput() end

---@public
---@param onlineID int
---@param i int
---@param level float
---@return void
function sendAdditionalPain(onlineID, i, level) end

---@public
---@param player int
---@return int
function getPlayerScreenHeight(player) end

---@public
---@param arg0 String
---@return KahluaTable
function getAllItemsForBodyLocation(arg0) end

---@public
---@param NewSpeed int
---@return void
function setGameSpeed(NewSpeed) end

---@public
---@return String
function getCurrentUserProfileName() end

---@public
---@return ErosionMain
function getErosion() end

---@public
---@param arg0 IsoPlayer
---@return void
function sendPlayerStatsChange(arg0) end

---@public
---@return void
function steamRequestInternetServersList() end

---@public
---@param state GameState
---@return void
function forceChangeState(state) end

---@public
---@return boolean
function isCoopHost() end

---@public
---@param arg0 int
---@param arg1 int
---@return float
function getControllerAxisValue(arg0, arg1) end

---@public
---@param c Coroutine
---@return int
function getLocalVarCount(c) end

---@public
---@param player int
---@return IsoPlayer
function getSpecificPlayer(player) end

---@public
---@return int
function getMouseY() end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function debugFullyStreamedIn(arg0, arg1) end

---@public
---@return void
function pauseSoundAndMusic() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return IsoMetaGrid.VehicleZone
function getVehicleZoneAt(arg0, arg1, arg2) end

---@public
---@return ArrayList|IsoPlayer
function getOnlinePlayers() end

---@public
---@return TextManager
function getTextManager() end

---@public
---@return void
function reloadEngineRPM() end

---@public
---@param c LuaCallFrame
---@return int
function getLineNumber(c) end

---@public
---@return boolean
function getDebug() end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 int
---@return void
function setSavefilePlayer1(arg0, arg1, arg2) end

---@public
---@return void
function testHelicopter() end

---@public
---@return ZomboidRadio
function getZomboidRadio() end

---@public
---@param min double
---@param max double
---@return double
function ZombRandBetween(min, max) end

---@public
---@param c Coroutine
---@return int
function getCoroutineTop(c) end

---@public
---@return Boolean
function getSteamModeActive() end

---@public
---@return void
function luaDebug() end

---@public
---@return boolean
function isDebugEnabled() end

---throws java.io.IOException
---@public
---@param filename String
---@return DataInputStream
function getFileInput(filename) end

---@public
---@return KahluaTable
function getFullSaveDirectoryTable() end

---@public
---@param player IsoPlayer
---@return void
function toggleSafetyServer(player) end

---@public
---@param cellX int
---@param cellY int
---@return void
function zpopSpawnTimeToZero(cellX, cellY) end

---@public
---@return void
function stopPing() end

---@public
---@param arg0 String
---@return ChooseGameInfo.Mod
function getModInfoByID(arg0) end

---@public
---@param arg0 boolean
---@return ArrayList|Unknown
function getAllOutfits(arg0) end

---@public
---@return long
function getGametimeTimestamp() end

---@public
---@return SandboxOptions
function getSandboxOptions() end

---@public
---@param c Coroutine
---@param n int
---@return Object
function getCoroutineObjStackWithBase(c, n) end

---@public
---@return void
function addAllBurntVehicles() end

---@public
---@param x float
---@param y float
---@param z float
---@param desc SurvivorDesc
---@param palette int
---@param dir IsoDirections
---@return IsoZombie
function createZombie(x, y, z, desc, palette, dir) end

---@public
---@param spawnX int
---@param spawnY int
---@param spawnW int
---@param spawnH int
---@param targetX int
---@param targetY int
---@param count int
---@return void
function createHordeInAreaTo(spawnX, spawnY, spawnW, spawnH, targetX, targetY, count) end

---@public
---@param param Object
---@return void
---@overload fun(param:Object, depth:int)
function debugLuaTable(param) end

---@public
---@param param Object
---@param depth int
---@return void
function debugLuaTable(param, depth) end

---@public
---@return boolean
function isAltKeyDown() end

---@public
---@param onlineID int
---@param i int
---@param bandaged boolean
---@param bandageLife float
---@param isAlcoholic boolean
---@param bandageType String
---@return void
function sendBandage(onlineID, i, bandaged, bandageLife, isAlcoholic, bandageType) end

---@public
---@return int
function getGameSpeed() end

---@public
---@param arg0 int
---@return float
function getControllerPovY(arg0) end

---@public
---@param arg0 InventoryItem
---@param arg1 IsoGridSquare
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@return void
function Render3DItem(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 String
---@param arg1 String
---@return String
function moduleDotType(arg0, arg1) end

---@public
---@return GameTime
function getGameTime() end

---@public
---@param arg0 String
---@param arg1 String
---@return boolean
function spawnpointsExistsForMod(arg0, arg1) end

---@public
---@param arg0 int
---@return String
function getControllerGUID(arg0) end

---@public
---@param id int
---@param x int
---@param y int
---@return void
function setAggroTarget(id, x, y) end

---@public
---@return boolean
function isPublicServerListAllowed() end

---@public
---@return BaseAmbientStreamManager
function getAmbientStreamManager() end

---@public
---@param modId String
---@param filename String
---@param createIfNull boolean
---@param append boolean
---@return LuaManager.GlobalObject.LuaFileWriter
function getModFileWriter(modId, filename, createIfNull, append) end

---@public
---@param a String
---@param tabX int
---@return String
function tabToX(a, tabX) end

---@public
---@return boolean
function canInviteFriends() end

---@public
---@param map HashMap|Object|Object
---@return KahluaTable
function transformIntoKahluaTable(map) end

---@public
---@param c Coroutine
---@param n int
---@return LuaCallFrame
function getCoroutineCallframeStack(c, n) end

---@public
---@return EditVehicleState
function getEditVehicleState() end

---@public
---@param arg0 String
---@return void
function activateSteamOverlayToWorkshopItem(arg0) end

---@public
---@return int
function getMouseXScaled() end

---@public
---@param arg0 String
---@return void
function reloadVehicleTextures(arg0) end

---@public
---@return KahluaTable
function getServerStatistic() end

---@public
---@param joypad int
---@return boolean
function isJoypadLeft(joypad) end

---@public
---@return boolean
function isClient() end

---@public
---@return int
function getLoadedLuaCount() end

---@public
---@param arg0 String
---@return Texture
function getSteamAvatarFromUsername(arg0) end

---@public
---@return ZombiePopulationRenderer
function zpopNewRenderer() end

---@public
---@param arg0 Short
---@return void
function focusOnTab(arg0) end

---@public
---@param spawnX float
---@param spawnY float
---@param targetX float
---@param targetY float
---@param count int
---@return void
function createHordeFromTo(spawnX, spawnY, targetX, targetY, count) end

---@public
---@param filename String
---@param createIfNull boolean
---@param append boolean
---@return LuaManager.GlobalObject.LuaFileWriter
function getSandboxFileWriter(filename, createIfNull, append) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 int
---@return void
function addWarningPoint(arg0, arg1, arg2) end

---@public
---@return void
function forceDisconnect() end

---@public
---@return ServerSettingsManager
function getServerSettingsManager() end

---@public
---@return ArrayList|Unknown
function getAllVehicles() end

---@public
---@return boolean
function isGamePaused() end

---@public
---@param _table KahluaTable
---@param key Object
---@return void
function toggleBreakOnChange(_table, key) end

---@public
---@param arg0 IsoGridSquare
---@param arg1 int
---@return IsoDeadBody
function createRandomDeadBody(arg0, arg1) end

---@public
---@param arg0 String
---@return String
function getRadioText(arg0) end

---@public
---@return boolean
function isDesktopOpenSupported() end

---@public
---@param arg0 int
---@return boolean
function isJoypadLBPressed(arg0) end

---@public
---@return BaseSoundManager
function getSoundManager() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@return void
function sendCataplasm(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param c Coroutine
---@param n int
---@return String
function localVarName(c, n) end

---@public
---@return boolean
function canModifyPlayerScoreboard() end

---@public
---@return boolean
function isXBOXController() end

---@public
---@return String
function getHourMinute() end

---@public
---@param arg0 Language
---@return ArrayList|Unknown
function getRadioTranslators(arg0) end

---@public
---@param x int
---@param y int
---@param z int
---@return ArrayList|IsoMetaGrid.Zone
function getZones(x, y, z) end

---@public
---@return void
function setAdmin() end

---@public
---@return TileOverlays
function getTileOverlays() end

---@public
---@return ArrayList|Unknown
function getAllItems() end

---@public
---@return KahluaTable
function getModDirectoryTable() end

---@public
---@param sq IsoGridSquare
---@param radius int
---@return void
function AddNoiseToken(sq, radius) end

---@public
---@return boolean
function checkSavePlayerExists() end

---@public
---@return String
function getAccessLevel() end

---@public
---@param arg0 ArrayList|Unknown
---@param arg1 LuaClosure
---@param arg2 Object
---@return void
function querySteamWorkshopItemDetails(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@return boolean
function isJoypadRBPressed(arg0) end

---@public
---@param arg0 Object
---@return boolean
function isSoundPlaying(arg0) end

---@public
---@param arg0 String
---@return IsoPlayer
function getPlayerFromUsername(arg0) end

---@public
---@return void
function initUISystem() end

---@public
---@param arg0 String
---@return void
function activateSteamOverlayToWebPage(arg0) end

---@public
---@return ArrayList|Unknown
function getLuaDebuggerErrors() end

---@public
---@param arg0 IsoPlayer
---@return void
function sendRequestInventory(arg0) end

---@public
---@param saveDir String
---@return KahluaTable
function getSaveInfo(saveDir) end

---@public
---@param o Object
---@return int
function getNumClassFunctions(o) end

---@public
---@param arg0 boolean
---@return void
function setServerStatisticEnable(arg0) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 String
---@param arg3 String
---@param arg4 boolean
---@return Model
function loadZomboidModel(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param joypad int
---@param button int
---@return boolean
function isJoypadPressed(joypad, button) end

---@public
---@return float
function getCameraOffY() end

---@public
---@param o Method
---@param i int
---@return String
function getMethodParameter(o, i) end

---@public
---@param f String
---@return Object
function require(f) end

---@public
---@param val double
---@return int
function toInt(val) end

---@public
---@return void
function steamReleaseInternetServersRequest() end

---@public
---@return void
function backToSinglePlayer() end

---@public
---@return boolean
function isDemo() end

---@public
---@param sprite String
---@return IsoSprite
function getSprite(sprite) end

---@public
---@param arg0 String
---@return String
function getSteamProfileNameFromUsername(arg0) end

---@public
---@param joypad int
---@return float
function getJoypadMovementAxisY(joypad) end

---@public
---@param arg0 double
---@param arg1 double
---@param arg2 double
---@return IsoGridSquare
function getSquare(arg0, arg1, arg2) end

---@public
---@param joypad int
---@return String
function getControllerName(joypad) end

---@public
---@param arg0 int
---@return boolean
function isControllerConnected(arg0) end

---@public
---@return void
function saveGame() end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 String
---@return Model
function loadStaticZomboidModel(arg0, arg1, arg2) end

---@public
---@return String
function getFileSeparator() end

---@public
---@param joypad int
---@return boolean
function isJoypadDown(joypad) end

---@public
---@param arg0 IsoPlayer
---@param arg1 int
---@param arg2 int
---@return void
function AddWorldSound(arg0, arg1, arg2) end

---@public
---@return ScriptManager
function getScriptManager() end

---@public
---@param arg0 Faction
---@param arg1 String
---@return void
function acceptFactionInvite(arg0, arg1) end

---@public
---@return boolean
function isCtrlKeyDown() end

---@public
---@param arg0 String
---@return BaseVehicle
function addVehicle(arg0) end

---@public
---@param arg0 boolean
---@return void
function refreshAnimSets(arg0) end

---@public
---@param object IsoObject
---@return void
function sledgeDestroy(object) end

---@public
---@param arg0 IsoPlayer
---@return void
function SyncXp(arg0) end

---@public
---@param joypad int
---@return int
function getJoypadBackButton(joypad) end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function setMouseXY(arg0, arg1) end

---@public
---@return SleepingEvent
function getSleepingEvent() end

---@public
---@return void
function updateFire() end

---@public
---@param onlineID int
---@param i int
---@param level float
---@return void
function sendDisinfect(onlineID, i, level) end

---@public
---@param arg0 String
---@param arg1 IsoDirections
---@param arg2 Integer
---@param arg3 IsoGridSquare
---@return BaseVehicle
function addVehicleDebug(arg0, arg1, arg2, arg3) end

---@public
---@param chara IsoGameCharacter
---@param objTarget IsoObject
---@return IsoDirections
function getDirectionTo(chara, objTarget) end

---@private
---@param arg0 int
---@param arg1 IsoPlayer
---@param arg2 boolean
---@return void
function addPlayerToWorld(arg0, arg1, arg2) end

---@public
---@return ArrayList|String
function getLotDirectories() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 String
---@param arg7 Integer
---@return ArrayList|Unknown
function addZombiesInOutfitArea(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@return void
function toggleVehicleRenderToTexture() end

---@public
---@param arg0 String
---@param arg1 int
---@return boolean
function steamRequestServerDetails(arg0, arg1) end

---@public
---@param cellX int
---@param cellY int
---@return void
function zpopSpawnNow(cellX, cellY) end

---@public
---@return boolean
function isSteamOverlayEnabled() end

---@public
---@return WorldMarkers
function getWorldMarkers() end

---@public
---@return void
function screenZoomIn() end

---@public
---@param storyName String
---@return void
function createStory(storyName) end

---@public
---@return KahluaTable
function getStatistics() end

---@public
---@param arg0 String
---@param arg1 IsoGridSquare
---@return void
function createTile(arg0, arg1) end

---@public
---@param c LuaClosure
---@return String
function getFilenameOfClosure(c) end

---@public
---@param base String
---@param name String
---@param display String
---@param type String
---@param icon String
---@return Item
function createNewScriptItem(base, name, display, type, icon) end

---@public
---@param arg0 IsoPlayer
---@return void
function sendPersonalColor(arg0) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 String
---@param arg3 String
---@param arg4 String
---@param arg5 String
---@return void
function serverConnect(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@return BaseSoundBank
function getFMODSoundBank() end

---@public
---@return void
function saveModsFile() end

---@public
---@param filename String
---@param saveName String
---@return Texture
function getTextureFromSaveDir(filename, saveName) end

---@public
---@param arg0 String
---@return String
function getSteamIDFromUsername(arg0) end

---@public
---@param arg0 IsoPlayer
---@param arg1 IsoPlayer
---@param arg2 InventoryItem
---@return void
function tradingUISendAddItem(arg0, arg1, arg2) end

---@public
---@return String
function getRandomUUID() end

---@public
---@return void
function endHelicopter() end

---@public
---@return void
function getDBSchema() end

---@public
---@param filename String
---@return DataOutputStream
function getFileOutput(filename) end

---@public
---@return boolean
function isServerSoftReset() end

---@public
---@param arg0 int
---@return boolean
function isKeyPressed(arg0) end

---@public
---@param joypad int
---@return float
function getJoypadMovementAxisX(joypad) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 float
---@return void
function setControllerDeadZone(arg0, arg1, arg2) end

---@public
---@param filename String
---@return BufferedReader
function getGameFilesTextInput(filename) end

---@public
---@param onlineID int
---@param i int
---@param doIt boolean
---@param factor float
---@param splintItem String
---@return void
function sendSplint(onlineID, i, doIt, factor, splintItem) end

---@public
---@param c Coroutine
---@return int
function getCallframeTop(c) end

---@public
---@return int
function getMouseX() end

---@public
---@param item String
---@return InventoryItem
---@overload fun(item:Item)
function instanceItem(item) end

---@public
---@param item Item
---@return InventoryItem
function instanceItem(item) end

---@public
---@return WorldSoundManager
function getWorldSoundManager() end

---@public
---@param joypad int
---@return int
function getJoypadStartButton(joypad) end

---@public
---@return boolean
function is64bit() end

---@public
---@param tutorial KahluaTable
---@return void
function doTutorial(tutorial) end

---@public
---@param arg0 int
---@return KahluaTable
function getPacketCounts(arg0) end

---@public
---@return void
function sendPing() end

---@public
---@param file String
---@param line int
---@return boolean
function hasBreakpoint(file, line) end

---@public
---@param url String
---@return void
function openURl(url) end

---@public
---@return Core
function getCore() end

---@public
---@param arg0 String
---@return void
function requestUserlog(arg0) end

---@public
---@return String
function getMyDocumentFolder() end

---@public
---@return void
function reloadActionGroups() end

---@public
---@return boolean
function isSystemLinux() end

---@public
---@return float
function getCameraOffX() end

---@public
---@param player int
---@return int
function getPlayerScreenTop(player) end

---@public
---@param arg0 float
---@return void
function setPuddles(arg0) end

---@public
---@return ServerOptions
function getServerOptions() end

---@public
---@return int
function getLuaDebuggerErrorCount() end

---@public
---@return String
function getLastStandPlayersDirectory() end

---@public
---@param arg0 String
---@param arg1 int
---@return void
function rainConfig(arg0, arg1) end

---@public
---@return String
function getCurrentUserSteamID() end

---@public
---@param arg0 boolean
---@return void
function useStaticErosionRand(arg0) end

---@private
---@param arg0 File
---@param arg1 String
---@return void
function deleteSavefileFilesMatching(arg0, arg1) end

---@public
---@return RadioAPI
function getRadioAPI() end

---@public
---@return void
function assaultPlayer() end

---@public
---@param max double
---@return double
---@overload fun(min:double, max:double)
function ZombRand(max) end

---@public
---@param min double
---@param max double
---@return double
function ZombRand(min, max) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 String
---@param arg3 String
---@param arg4 boolean
---@return void
function setModelMetaData(arg0, arg1, arg2, arg3, arg4) end

---@public
---@return BeardStyles
function getBeardStylesInstance() end

---@public
---@param o Object
---@param i int
---@return Field
function getClassField(o, i) end

---@public
---@return int
function getControllerCount() end

---@public
---@param y float
---@param ui UIElement
---@param zoom float
---@param ypos float
---@return float
function translatePointYInOverheadMapToWindow(y, ui, zoom, ypos) end

---@public
---@param ui UIElement
---@param zoom float
---@param xpos float
---@param ypos float
---@return void
function drawOverheadMap(ui, zoom, xpos, ypos) end

---@public
---@return KahluaTable
function getLatestSave() end

---@public
---@param arg0 String
---@return void
function inviteFriend(arg0) end

---@public
---@param id int
---@return void
function setActivePlayer(id) end

---@public
---@param f String
---@return boolean
function checkSaveFileExists(f) end

---@public
---@return void
function testSound() end

---@public
---@param arg0 int
---@return Server
function steamGetInternetServerDetails(arg0) end

---@public
---@param arg0 String
---@return void
function ProceedFactionMessage(arg0) end

---@public
---@param onlineID int
---@param i int
---@return void
function sendCleanBurn(onlineID, i) end

---@public
---@return SpriteRenderer
function getRenderer() end

---@public
---@param n int
---@return String
function getLoadedLua(n) end

---@public
---@return void
function forceSnowCheck() end

---@public
---@return AnimationViewerState
function getAnimationViewerState() end

---@public
---@param c LuaCallFrame
---@return String
function getFilenameOfCallframe(c) end

---@public
---@param obj Object
---@param name String
---@return boolean
function isType(obj, name) end

---@public
---@return int
function getMaxActivePlayers() end

---@public
---@return Coroutine
function getCurrentCoroutine() end

---@public
---@param arg0 IsoGridSquare
---@param arg1 int
---@return void
function addBloodSplat(arg0, arg1) end

---@public
---@return boolean
function reactivateJoypadAfterResetLua() end

---@public
---@param arg0 File
---@return String
function getSaveName(arg0) end

---@public
---@param arg0 String
---@return boolean
function isValidSteamID(arg0) end

---@public
---@param arg0 InventoryItem
---@return ArrayList|Unknown
function getAllDecalNamesForItem(arg0) end

---@public
---@param joypad int
---@return int
function getJoypadXButton(joypad) end

---@public
---@return AttachmentEditorState
function getAttachmentEditorState() end

---@public
---@param arg0 int
---@return void
function attachTrailerToPlayerVehicle(arg0) end

---@public
---@param o Object
---@return int
function getNumClassFields(o) end

---@public
---@param filename String
---@param createIfNull boolean
---@param append boolean
---@return LuaManager.GlobalObject.LuaFileWriter
function getFileWriter(filename, createIfNull, append) end

---@public
---@param onlineID int
---@param i int
---@return void
function sendRemoveGlass(onlineID, i) end

---@public
---@return PerformanceSettings
function getPerformance() end

---@public
---@param filename String
---@return boolean
function serverFileExists(filename) end

---@public
---@return String
function getServerListFile() end

---@public
---@return void
function showAttachmentEditor() end

---@public
---@return void
function showAnimationViewer() end

---@public
---@param player int
---@return int
function getPlayerScreenWidth(player) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 String
---@return void
function showWrongChatTabMessage(arg0, arg1, arg2) end

---@public
---@param c LuaClosure
---@return int
function getFirstLineOfClosure(c) end

---@public
---@return void
function showGlobalObjectDebugger() end

---@public
---@param player int
---@return int
function getPlayerScreenLeft(player) end

---@public
---@return void
function reloadVehicles() end

---@public
---@param x int
---@param y int
---@return void
function addVirtualZombie(x, y) end

---@public
---@return ArrayList|Unknown
function getSteamWorkshopStagedItems() end

---@public
---@return HairStyles
function getHairStylesInstance() end

---@public
---@param arg0 String
---@return String
function getTextMediaEN(arg0) end

---@public
---@param o Method
---@return int
function getMethodParameterCount(o) end

---@public
---@return void
function addCarCrash() end

---@public
---@param o Object
---@param i int
---@return Method
function getClassFunction(o, i) end

---@public
---@param arg0 String
---@return void
function deleteSandboxPreset(arg0) end

---@public
---@param arg0 float
---@return void
function configureLighting(arg0) end

---@public
---@param user String
---@return boolean
function isValidUserName(user) end

---@public
---@return boolean
function isSystemMacOS() end

---@public
---@param txt String
---@return String
function getItemText(txt) end

---@public
---@param mod ChooseGameInfo.Mod
---@param activate boolean
---@return void
function toggleModActive(mod, activate) end

---@public
---@param arg0 String
---@return String
function getRecipeDisplayName(arg0) end

---@public
---@param min float
---@param max float
---@return float
function ZombRandFloat(min, max) end

---@public
---@param worldName String
---@return void
function createWorld(worldName) end

---@public
---@param onlineID int
---@param i int
---@param stitched boolean
---@param stitchLevel float
---@return void
function sendStitch(onlineID, i, stitched, stitchLevel) end

---@public
---@param arg0 String
---@return boolean
function checkSaveFolderExists(arg0) end

---@public
---@param arg0 BuildingDef
---@param arg1 int
---@param arg2 String
---@param arg3 RoomDef
---@param arg4 Integer
---@return ArrayList|Unknown
function addZombiesInBuilding(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 IsoPlayer
---@return void
function sendPlayerExtraInfo(arg0) end

---@public
---@param arg0 long
---@param arg1 String
---@param arg2 IsoPlayer
---@return void
function InvMngGetItem(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@return void
function showVehicleEditor(arg0) end

---@public
---@param arg0 String
---@param arg1 KahluaTable
---@return void
function executeQuery(arg0, arg1) end

---@public
---@param arg0 IsoPlayer
---@return KahluaTable
function getPlayerInfo(arg0) end

---throws java.io.IOException
---@public
---@return List|BufferedReader
function getAllSavedPlayers() end

---@public
---@return SearchMode
function getSearchMode() end

---@public
---@return boolean
function wasMouseActiveMoreRecentlyThanJoypad() end

---@public
---@param joypad int
---@return int
function getButtonCount(joypad) end

---@public
---@return ArrayList|String
function getActivatedMods() end

---@public
---@param _table KahluaTable
---@param key Object
---@return boolean
function hasDataBreakpoint(_table, key) end

---@public
---@param arg0 int
---@return void
function setDebugToggleControllerPluggedIn(arg0) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 String
---@return Model
function loadVehicleModel(arg0, arg1, arg2) end

---@public
---@param cellX int
---@param cellY int
---@return void
function zpopClearZombies(cellX, cellY) end

---@public
---@param arg0 String
---@return String
function getItemNameFromFullType(arg0) end

---@public
---@return ArrayList|Unknown
function getSteamWorkshopItemIDs() end

---@public
---@return void
function disconnect() end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 String
---@return void
function addUserlog(arg0, arg1, arg2) end

---@public
---@param toReplace String
---@param regex String
---@param by String
---@return String
function replaceWith(toReplace, regex, by) end

---@public
---@param arg0 int
---@return boolean
function wasKeyDown(arg0) end

---@public
---@return void
function revertToKeyboardAndMouse() end

---@public
---@param key int
---@return boolean
function isKeyDown(key) end

---@public
---@param filename String
---@return DataInputStream
function getGameFilesInput(filename) end

---@public
---@return List|Unknown
function getLastStandPlayerFileNames() end

---@public
---@param c Coroutine
---@param n int
---@return String
function getLocalVarName(c, n) end

---@public
---@param str String
---@return String
function getShortenedFilename(str) end

---@public
---@param joypad int
---@return int
function getJoypadBButton(joypad) end

---@public
---@param username String
---@param pwd String
---@param ip String
---@param port String
---@return void
function ping(username, pwd, ip, port) end

---@public
---@return boolean
function getServerStatisticEnable() end

---@public
---@return Double
function getAverageFSP() end

---throws java.io.IOException
---@public
---@param modId String
---@param filename String
---@param createIfNull boolean
---@return BufferedReader
function getModFileReader(modId, filename, createIfNull) end

---@public
---@param arg0 Language
---@return ArrayList|Unknown
function getTranslatorCredits(arg0) end

---@public
---@param arg0 IsoPlayer
---@return void
function sendClothing(arg0) end

---@public
---@param arg0 int
---@return int
function getControllerButtonCount(arg0) end

---@public
---@param arg0 IsoPlayer
---@param arg1 IsoPlayer
---@return void
function requestTrading(arg0, arg1) end

---@public
---@param filename String
---@return void
function reloadLuaFile(filename) end

---@public
---@param arg0 int
---@return void
function saveControllerSettings(arg0) end

---@public
---@param arg0 String
---@return void
function reloadModelsMatching(arg0) end

---@public
---@param arg0 String
---@return ArrayList|Unknown
function getSaveDirectory(arg0) end

---@public
---@return void
function endFileOutput() end

---@public
---@param arg0 String
---@return void
function deleteAllGameModeSaves(arg0) end

---@public
---@param arg0 BaseVehicle
---@return KahluaTable
function getVehicleInfo(arg0) end

---@public
---@param joypad int
---@return boolean
function isJoypadRight(joypad) end

---@public
---@param filename String
---@return void
function reloadServerLuaFile(filename) end

---@public
---@param joypad int
---@return int
function getJoypadYButton(joypad) end

---@public
---@return boolean
function isShiftKeyDown() end

---@public
---@return boolean
function getSteamScoreboard() end

---@public
---@return void
function requestPacketCounts() end

---@public
---@param arg0 IsoPlayer
---@param arg1 IsoPlayer
---@param arg2 int
---@return void
function tradingUISendRemoveItem(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@param arg1 int
---@return float
function getControllerDeadZone(arg0, arg1) end
