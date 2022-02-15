---@class Core : zombie.core.Core
---@field public bDemo boolean
---@field public bTutorial boolean
---@field private fakefullscreen boolean
---@field private gameVersion GameVersion
---@field public steamServerVersion String
---@field public bMultithreadedRendering boolean
---@field public bAltMoveMethod boolean
---@field private rosewoodSpawnDone boolean
---@field private objectHighlitedColor ColorInfo
---@field private flashIsoCursor boolean
---@field private isoCursorVisibility int
---@field public OptionShowCursorWhileAiming boolean
---@field private collideZombies boolean
---@field public OffscreenBuffer MultiTextureFBO2
---@field private saveFolder String
---@field public OptionZoom boolean
---@field public OptionModsEnabled boolean
---@field public OptionFontSize int
---@field public OptionContextMenuFont String
---@field public OptionInventoryFont String
---@field public OptionTooltipFont String
---@field public OptionMeasurementFormat String
---@field public OptionClockFormat int
---@field public OptionClockSize int
---@field public OptionClock24Hour boolean
---@field public OptionVSync boolean
---@field public OptionSoundVolume int
---@field public OptionMusicVolume int
---@field public OptionAmbientVolume int
---@field public OptionMusicLibrary int
---@field public OptionVoiceEnable boolean
---@field public OptionVoiceMode int
---@field public OptionVoiceVADMode int
---@field public OptionVoiceRecordDeviceName String
---@field public OptionVoiceVolumeMic int
---@field public OptionVoiceVolumePlayers int
---@field public OptionVehicleEngineVolume int
---@field public OptionReloadDifficulty int
---@field public OptionRackProgress boolean
---@field public OptionBloodDecals int
---@field public OptionBorderlessWindow boolean
---@field public OptionLockCursorToWindow boolean
---@field public OptionTextureCompression boolean
---@field public OptionModelTextureMipmaps boolean
---@field public OptionTexture2x boolean
---@field private OptionZoomLevels1x String
---@field private OptionZoomLevels2x String
---@field public OptionEnableContentTranslations boolean
---@field public OptionUIFBO boolean
---@field public OptionUIRenderFPS int
---@field public OptionRadialMenuKeyToggle boolean
---@field public OptionReloadRadialInstant boolean
---@field public OptionPanCameraWhileAiming boolean
---@field public OptionPanCameraWhileDriving boolean
---@field public OptionShowChatTimestamp boolean
---@field public OptionShowChatTitle boolean
---@field public OptionChatFontSize String
---@field public OptionMinChatOpaque float
---@field public OptionMaxChatOpaque float
---@field public OptionChatFadeTime float
---@field public OptionChatOpaqueOnFocus boolean
---@field public OptionTemperatureDisplayCelsius boolean
---@field public OptionDoWindSpriteEffects boolean
---@field public OptionDoDoorSpriteEffects boolean
---@field public OptionRenderPrecipIndoors boolean
---@field public OptionAutoProneAtk boolean
---@field public Option3DGroundItem boolean
---@field public OptionRenderPrecipitation int
---@field public OptionUpdateSneakButton boolean
---@field public OptiondblTapJogToSprint boolean
---@field private OptionAimOutline int
---@field private OptionCycleContainerKey String
---@field private OptionDropItemsOnSquareCenter boolean
---@field private OptionTimedActionGameSpeedReset boolean
---@field private OptionShoulderButtonContainerSwitch int
---@field private OptionProgressBar boolean
---@field private OptionLanguageName String
---@field private OptionSingleContextMenu boolean[]
---@field private OptionCorpseShadows boolean
---@field private OptionSimpleClothingTextures int
---@field private OptionSimpleWeaponTextures boolean
---@field private OptionAutoDrink boolean
---@field private OptionLeaveKeyInIgnition boolean
---@field private OptionSearchModeOverlayEffect int
---@field private OptionIgnoreProneZombieRange int
---@field private showPing boolean
---@field private forceSnow boolean
---@field private zombieGroupSound boolean
---@field private blinkingMoodle String
---@field private tutorialDone boolean
---@field private vehiclesWarningShow boolean
---@field private poisonousBerry String
---@field private poisonousMushroom String
---@field private doneNewSaveFolder boolean
---@field private difficulty String
---@field public TileScale int
---@field private isSelectingAll boolean
---@field private showYourUsername boolean
---@field private mpTextColor ColorInfo
---@field private isAzerty boolean
---@field private seenUpdateText String
---@field private toggleToAim boolean
---@field private toggleToRun boolean
---@field private toggleToSprint boolean
---@field private celsius boolean
---@field private riversideDone boolean
---@field private noSave boolean
---@field private showFirstTimeVehicleTutorial boolean
---@field private showFirstTimeWeatherTutorial boolean
---@field private showFirstTimeSneakTutorial boolean
---@field private newReloading boolean
---@field private gotNewBelt boolean
---@field private bAnimPopupDone boolean
---@field private bModsPopupDone boolean
---@field public blinkAlpha float
---@field public blinkAlphaIncrease boolean
---@field private optionsOnStartup HashMap|Unknown|Unknown
---@field private bChallenge boolean
---@field public width int
---@field public height int
---@field public MaxJukeBoxesActive int
---@field public NumJukeBoxesActive int
---@field public GameMode String
---@field private glVersion String
---@field private glMajorVersion int
---@field private core Core
---@field public bDebug boolean
---@field public CurrentTextEntryBox UITextBox2
---@field public RenderShader Shader
---@field private keyMaps Map|Unknown|Unknown
---@field public bUseShaders boolean
---@field private iPerfSkybox int
---@field private iPerfSkybox_new int
---@field public iPerfSkybox_High int
---@field public iPerfSkybox_Medium int
---@field public iPerfSkybox_Static int
---@field private iPerfPuddles int
---@field private iPerfPuddles_new int
---@field public iPerfPuddles_None int
---@field public iPerfPuddles_GroundOnly int
---@field public iPerfPuddles_GroundWithRuts int
---@field public iPerfPuddles_All int
---@field private bPerfReflections boolean
---@field private bPerfReflections_new boolean
---@field public vidMem int
---@field public bSupportsFBO boolean
---@field public UIRenderAccumulator float
---@field public UIRenderThisFrame boolean
---@field public version int
---@field public fileversion int
---@field public fullScreen boolean
---@field private bAutoZoom boolean[]
---@field public GameMap String
---@field public GameSaveWorld String
---@field public SafeMode boolean
---@field public SafeModeForced boolean
---@field public SoundDisabled boolean
---@field public frameStage int
---@field private stack int
---@field public xx int
---@field public yy int
---@field public zz int
---@field public FloatParamMap HashMap|Unknown|Unknown
---@field private tempMatrix4f Matrix4f
---@field private isoAngle float
---@field private scale float
---@field public bLastStand boolean
---@field public ChallengeID String
---@field public bLoadedWithMultithreaded boolean
---@field public bExiting boolean
---@field private m_delayResetLua_activeMods String
---@field private m_delayResetLua_reason String
Core = {}

---@public
---@param playerIndex int
---@param del int
---@return float
function Core:getNextZoom(playerIndex, del) end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionModelTextureMipmaps(arg0) end

---@public
---@param b boolean
---@return void
function Core:setOptionRackProgress(b) end

---@private
---@param arg0 boolean[]
---@return String
function Core:getPerPlayerBooleanString(arg0) end

---@public
---@return void
function Core:RenderOffScreenBuffer() end

---@public
---@param arg0 boolean
---@return void
function Core:setShowFirstTimeWeatherTutorial(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 float
---@param arg3 int
---@return void
---@overload fun(arg0:int, arg1:int, arg2:float, arg3:int, arg4:boolean)
function Core:DoStartFrameStuff(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 float
---@param arg3 int
---@param arg4 boolean
---@return void
function Core:DoStartFrameStuff(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 boolean
---@return void
function Core:setDoneNewSaveFolder(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:setIsSelectingAll(arg0) end

---@public
---@return String
function Core:getMyDocumentFolder() end

---@public
---@return boolean
function Core:isToggleToAim() end

---@public
---@return boolean
function Core:supportNPTTexture() end

---@public
---@return int
function Core:getOptionVoiceRecordDevice() end

---@public
---@param arg0 boolean
---@return void
function Core:setRenderPrecipIndoors(arg0) end

---@public
---@param playerIndex int
---@return int
function Core:getOffscreenWidth(playerIndex) end

---@public
---@return float
function Core:getIgnoreProneZombieRange() end

---@public
---@return int
function Core:getScreenWidth() end

---@public
---@return void
function Core:DoFrameReady() end

---@public
---@return void
function Core:quitToDesktop() end

---@public
---@return boolean
function Core:getOptionTieredZombieUpdates() end

---@public
---@return boolean
function Core:getOptionCorpseShadows() end

---@public
---@return String
function Core:getOptionLanguageName() end

---@public
---@param arg0 boolean
---@return void
function Core:setToggleToRun(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionDoWindSpriteEffects(arg0) end

---@public
---@param arg0 int
---@return void
function Core:setOptionFontSize(arg0) end

---@public
---@return boolean
function Core:getOptionTimedActionGameSpeedReset() end

---@private
---@param arg0 String
---@return void
function Core:copyPasteFolders(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:setOption3DGroundItem(arg0) end

---@private
---@param arg0 int
---@return boolean
function Core:isFunctionKey(arg0) end

---@public
---@return boolean
function Core:isOptionAutoProneAtk() end

---@public
---@param arg0 String
---@param arg1 String
---@return void
---@overload fun(sp:boolean, reason:String)
function Core:ResetLua(arg0, arg1) end

---@public
---@param sp boolean
---@param reason String
---@return void
function Core:ResetLua(sp, reason) end

---@public
---@param keyName String
---@param key Integer
---@return void
function Core:addKeyBinding(keyName, key) end

---@public
---@return int
function Core:getPerfSkyboxOnLoad() end

---@public
---@param arg0 int
---@return void
function Core:setOptionVoiceMode(arg0) end

---@public
---@param playerIndex int
---@return boolean
function Core:getAutoZoom(playerIndex) end

---@public
---@return boolean
function Core:getOptionZoom() end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionCorpseShadows(arg0) end

---@public
---@return boolean
function Core:isDedicated() end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionProgressBar(arg0) end

---@public
---@param index int
---@return void
function Core:setFramerate(index) end

---@public
---@param done boolean
---@return void
function Core:setTutorialDone(done) end

---@public
---@param zombieGroupSound boolean
---@return void
function Core:setZombieGroupSound(zombieGroupSound) end

---@public
---@return int
function Core:getOptionUIRenderFPS() end

---@public
---@param bChallenge boolean
---@return void
function Core:setChallenge(bChallenge) end

---@public
---@return boolean
function Core:isToggleToSprint() end

---@private
---@return boolean
function Core:setupMultiFBO() end

---@public
---@return boolean
function Core:isDoneNewSaveFolder() end

---@public
---@return float
function Core:getMaxZoom() end

---@public
---@return boolean
function Core:isForceSnow() end

---@public
---@param volume int
---@return void
function Core:setOptionSoundVolume(volume) end

---@public
---@param arg0 int
---@return void
function Core:EndFrameText(arg0) end

---@public
---@param arg0 String
---@return void
function Core:setOptionLanguageName(arg0) end

---@public
---@param arg0 int
---@return void
function Core:setOptionIgnoreProneZombieRange(arg0) end

---@public
---@return void
function Core:shadersOptionChanged() end

---@public
---@return int
function Core:getIsoCursorVisibility() end

---@public
---@return int
function Core:getOptionAimOutline() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 boolean
---@return void
function Core:DoPushIsoStuff(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionAutoProneAtk(arg0) end

---@public
---@param arg0 int
---@return void
function Core:setOptionVoiceRecordDevice(arg0) end

---@public
---@return ColorInfo
function Core:getMpTextColor() end

---@public
---@return boolean
function Core:isNoSave() end

---@public
---@return void
function Core:reinitKeyMaps() end

---@public
---@return String
function Core:getSteamServerVersion() end

---@public
---@param name String
---@return boolean
function Core:getOptionActiveController(name) end

---@public
---@param arg0 int
---@return boolean
function Core:getOptionSingleContextMenu(arg0) end

---@public
---@return int
function Core:getOptionShoulderButtonContainerSwitch() end

---@public
---@param arg0 int
---@return void
function Core:setOptionRenderPrecipitation(arg0) end

---@public
---@return boolean
function Core:isOption3DGroundItem() end

---@public
---@return int
function Core:getScreenHeight() end

---@public
---@return boolean
function Core:isMultiThread() end

---@public
---@param directory String
---@return void
function Core:deleteDirectoryRecusrively(directory) end

---@public
---@return Core
function Core:getInstance() end

---@public
---@return boolean
function Core:isShowFirstTimeSneakTutorial() end

---@public
---@param arg0 float
---@return void
function Core:setOptionMinChatOpaque(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionReloadRadialInstant(arg0) end

---@public
---@return void
function Core:DoPopIsoStuff() end

---@public
---@return boolean
function Core:gotNewBelt() end

---@public
---@return boolean
function Core:isSelectingAll() end

---@public
---@return void
---@overload fun(arg0:int, arg1:int, arg2:int)
---@overload fun(arg0:int, arg1:int, arg2:int, arg3:int, arg4:int)
function Core:TakeScreenshot() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function Core:TakeScreenshot(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@return void
function Core:TakeScreenshot(arg0, arg1, arg2, arg3, arg4) end

---@public
---@return void
---@overload fun(nPlayer:int)
function Core:EndFrame() end

---@public
---@param nPlayer int
---@return void
function Core:EndFrame(nPlayer) end

---@public
---@return boolean
function Core:getOptionTexture2x() end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionChatOpaqueOnFocus(arg0) end

---@public
---@return int
function Core:getOptionVoiceVolumePlayers() end

---@public
---@param arg0 int
---@param arg1 float
---@return int
function Core:getXAngle(arg0, arg1) end

---@public
---@param arg0 float
---@return void
function Core:setOptionChatFadeTime(arg0) end

---@public
---@return String
function Core:getPoisonousBerry() end

---@public
---@return String
function Core:getOptionInventoryFont() end

---@public
---@return boolean
function Core:getOptionRackProgress() end

---@public
---@return int
function Core:getPerfSkybox() end

---@public
---@return float
function Core:getOptionMaxChatOpaque() end

---@public
---@return boolean
function Core:isModsPopupDone() end

---@public
---@param blinkingMoodle String
---@return void
function Core:setBlinkingMoodle(blinkingMoodle) end

---@public
---@param arg0 int
---@return void
function Core:setPerfSkybox(arg0) end

---@public
---@return boolean
function Core:isRiversideDone() end

---throws java.io.IOException
---@public
---@return boolean
function Core:loadOptions() end

---@public
---@return boolean
function Core:isChallenge() end

---@public
---@return boolean
function Core:isAzerty() end

---@public
---@return boolean
function Core:getPerfReflectionsOnLoad() end

---@public
---@return float
function Core:getOptionMinChatOpaque() end

---@public
---@return void
function Core:zoomLevelsChanged() end

---@public
---@return float
function Core:getRealOptionSoundVolume() end

---@public
---@return float
function Core:getMinZoom() end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionUIFBO(arg0) end

---@public
---@return void
function Core:UnfocusActiveTextEntryBox() end

---@public
---@param arg0 boolean
---@return void
function Core:setCollideZombies(arg0) end

---@public
---@return String
function Core:getOptionZoomLevels1x() end

---@public
---@return boolean
function Core:isShowFirstTimeVehicleTutorial() end

---@public
---@return boolean
function Core:getOptionModelTextureMipmaps() end

---@public
---@return int
function Core:getOptionFontSize() end

---@public
---@return String
function Core:getChallengeID() end

---@public
---@param arg0 String
---@return void
function Core:setOptionContextMenuFont(arg0) end

---@public
---@param playerIndex int
---@return float
function Core:getZoom(playerIndex) end

---@public
---@return void
function Core:initPoisonousBerry() end

---@public
---@return boolean
function Core:isFullScreen() end

---@public
---@param arg0 int
---@return void
function Core:setPerfPuddles(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:setPerfReflections(arg0) end

---@public
---@param filename String
---@return void
function Core:TakeFullScreenshot(filename) end

---@public
---@param val boolean
---@return void
function Core:setMultiThread(val) end

---@public
---@param arg0 int
---@return void
function Core:setOptionShoulderButtonContainerSwitch(arg0) end

---@public
---@return boolean
function Core:getOptionDoDoorSpriteEffects() end

---@public
---@return boolean
function Core:isShowYourUsername() end

---@public
---@return void
function Core:countMissing3DItems() end

---@public
---@param arg0 int
---@return void
function Core:setOptionAmbientVolume(arg0) end

---@public
---@param arg0 int
---@return void
function Core:StartFrameText(arg0) end

---@public
---@param arg0 int
---@return void
function Core:setOptionUIRenderFPS(arg0) end

---@public
---@param arg0 String
---@return void
function Core:setPoisonousBerry(arg0) end

---@public
---@param playerIndex int
---@return int
function Core:getOffscreenHeight(playerIndex) end

---@public
---@param mem int
---@return void
function Core:setVidMem(mem) end

---@public
---@return boolean
function Core:isTutorialDone() end

---@public
---@param arg0 boolean
---@return boolean
function Core:isOptionSimpleClothingTextures(arg0) end

---@public
---@return String
function Core:getOptionZoomLevels2x() end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionTexture2x(arg0) end

---@public
---@return int
function Core:getOptionVoiceMode() end

---@public
---@return void
function Core:initPoisonousMushroom() end

---@public
---@return boolean
function Core:isNewReloading() end

---@public
---@return Map|String|Integer
function Core:getKeyMaps() end

---@public
---@return float
function Core:getOptionChatFadeTime() end

---@public
---@return int
function Core:getTileScale() end

---@public
---@return boolean
function Core:getContentTranslationsEnabled() end

---@public
---@return String
function Core:getBlinkingMoodle() end

---@public
---@return int
function Core:getOptionVoiceVolumeMic() end

---@public
---@param arg0 ColorInfo
---@return void
function Core:setMpTextColor(arg0) end

---@public
---@param arg0 float
---@return void
function Core:setOptionMaxChatOpaque(arg0) end

---@public
---@return int
function Core:getVidMem() end

---@public
---@return boolean
function Core:isLastStand() end

---@public
---@return int
function Core:getOptionMusicLibrary() end

---@public
---@return boolean
function Core:isCollideZombies() end

---@public
---@param arg0 int
---@return void
function Core:setOptionClockFormat(arg0) end

---@public
---@return boolean
function Core:getOptionLeaveKeyInIgnition() end

---@public
---@return boolean
function Core:getOptionModsEnabled() end

---@public
---@return GameVersion
function Core:getGameVersion() end

---@public
---@param arg0 boolean
---@return void
function Core:setToggleToAim(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionSimpleWeaponTextures(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:setCelsius(arg0) end

---@public
---@return boolean
function Core:loadedShader() end

---@public
---@return boolean
function Core:isShowFirstTimeWeatherTutorial() end

---@public
---@return boolean
function Core:getOptionTextureCompression() end

---@public
---@return int
function Core:getOptionClockSize() end

---@public
---@param fbo TextureFBO
---@return void
function Core:setLastRenderedFBO(fbo) end

---@public
---@param forceSnow boolean
---@return void
function Core:setForceSnow(forceSnow) end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionAutoDrink(arg0) end

---@public
---@param arg0 String
---@return void
function Core:setDifficulty(arg0) end

---@public
---@param arg0 int
---@return void
function Core:setOptionVoiceVolumeMic(arg0) end

---@public
---@return void
function Core:EndFrameUI() end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionDoDoorSpriteEffects(arg0) end

---@public
---@param playerIndex int
---@param auto boolean
---@return void
function Core:setAutoZoom(playerIndex, auto) end

---throws java.io.IOException
---@public
---@return void
function Core:saveOptions() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 boolean
---@return void
function Core:setResolutionAndFullScreen(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@return void
function Core:setOptionVehicleEngineVolume(arg0) end

---@public
---@return int
function Core:getOptionRenderPrecipitation() end

---@public
---@param arg0 boolean
---@return void
function Core:setRiversideDone(arg0) end

---@public
---@param arg0 int
---@param arg1 boolean
---@return void
function Core:setOptionSingleContextMenu(arg0, arg1) end

---@public
---@return boolean
function Core:StartFrameUI() end

---@public
---@param arg0 boolean
---@return void
function Core:setNewReloading(arg0) end

---@public
---@return int
function Core:getOptionVoiceVADMode() end

---@public
---@return boolean
function Core:getOptionLockCursorToWindow() end

---@public
---@return boolean
function Core:getOptionVoiceEnable() end

---@public
---@return boolean
function Core:supportCompressedTextures() end

---@public
---@return TextureFBO
---@overload fun(nPlayer:int)
function Core:getOffscreenBuffer() end

---@public
---@param nPlayer int
---@return TextureFBO
function Core:getOffscreenBuffer(nPlayer) end

---@public
---@return boolean
function Core:getUseShaders() end

---@public
---@return int
function Core:getOptionIgnoreProneZombieRange() end

---@public
---@return String
function Core:getOptionVoiceRecordDeviceName() end

---@public
---@return boolean
function Core:supportsFBO() end

---@public
---@param arg0 String
---@return void
function Core:setOptionTooltipFont(arg0) end

---@public
---@param arg0 String
---@return void
function Core:setOptionCycleContainerKey(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionRadialMenuKeyToggle(arg0) end

---@public
---@return int
function Core:getOptionMusicVolume() end

---@public
---@return int
function Core:getOptionAmbientVolume() end

---@public
---@return boolean
function Core:isAnimPopupDone() end

---@public
---@return boolean
function Core:getOptionBorderlessWindow() end

---@public
---@return int
function Core:getPerfPuddlesOnLoad() end

---@public
---@return boolean
function Core:getDebug() end

---throws org.lwjgl.LWJGLException
---@public
---@param width int
---@param height int
---@return void
function Core:init(width, height) end

---@public
---@return void
---@overload fun(nPlayer:int, clear:boolean)
function Core:StartFrame() end

---@public
---@param nPlayer int
---@param clear boolean
---@return void
function Core:StartFrame(nPlayer, clear) end

---throws org.lwjgl.LWJGLException
---@public
---@param width int
---@param height int
---@return boolean
function Core:supportRes(width, height) end

---@public
---@return ArrayList|Unknown
function Core:getDefaultZoomLevels() end

---@public
---@return float
function Core:getCurrentPlayerZoom() end

---@public
---@return String
function Core:getOptionChatFontSize() end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionLockCursorToWindow(arg0) end

---@public
---@return String
function Core:getGameMode() end

---@public
---@return boolean
function Core:getOptionDropItemsOnSquareCenter() end

---@public
---@return boolean
function Core:isShowPing() end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionPanCameraWhileDriving(arg0) end

---@public
---@return String
function Core:getSaveFolder() end

---@public
---@param controllerIndex int
---@param active boolean
---@return void
function Core:setOptionActiveController(controllerIndex, active) end

---@public
---@param width int
---@param height int
---@return void
function Core:setScreenSize(width, height) end

---@public
---@param zoom boolean
---@return void
function Core:setOptionZoom(zoom) end

---@public
---@return void
function Core:initShaders() end

---@public
---@return boolean
function Core:getOptionDisplayAsCelsius() end

---@public
---@param arg0 int
---@return void
function Core:setOptionSearchModeOverlayEffect(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:setAzerty(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return void
function Core:DoPushIsoParticleStuff(arg0, arg1, arg2) end

---@public
---@return boolean
function Core:getMicVolumeError() end

---@public
---@return boolean
function Core:getOptionSimpleWeaponTextures() end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionDropItemsOnSquareCenter(arg0) end

---@public
---@param arg0 int
---@return void
function Core:setOptionSimpleClothingTextures(arg0) end

---@public
---@param width int
---@param height int
---@param fullscreen boolean
---@return void
function Core:setDisplayMode(width, height, fullscreen) end

---@private
---@param arg0 String
---@param arg1 boolean[]
---@return void
function Core:readPerPlayerBoolean(arg0, arg1) end

---@public
---@return int
function Core:getOptionVehicleEngineVolume() end

---@public
---@param arg0 String
---@param arg1 String
---@return void
function Core:DelayResetLua(arg0, arg1) end

---@public
---@return boolean
function Core:isInDebug() end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionTieredZombieUpdates(arg0) end

---@public
---@return int
function Core:getOptionSearchModeOverlayEffect() end

---@public
---@param arg0 String
---@return void
function Core:setOptionChatFontSize(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function Core:DoStartFrameStuffSmartTextureFx(arg0, arg1, arg2) end

---@public
---@return boolean
function Core:getOptionClock24Hour() end

---@public
---@return boolean
function Core:getOptionAutoDrink() end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionPanCameraWhileAiming(arg0) end

---@public
---@return String
function Core:getVersionNumber() end

---@public
---@param b boolean
---@return void
function Core:setWindowed(b) end

---@public
---@param arg0 int
---@return void
function Core:setOptionVoiceVolumePlayers(arg0) end

---@public
---@param arg0 String
---@return void
function Core:setOptionZoomLevels2x(arg0) end

---@public
---@param keyName String
---@return int
function Core:getKey(keyName) end

---@public
---@return boolean
function Core:getOptionPanCameraWhileDriving() end

---@public
---@param arg0 String
---@return void
function Core:setOptionMeasurementFormat(arg0) end

---@public
---@return void
function Core:MoveMethodToggle() end

---@public
---@return void
function Core:CheckDelayResetLua() end

---@public
---@return boolean
function Core:getOptionPanCameraWhileAiming() end

---@public
---@param arg0 boolean
---@return void
function Core:setTestingMicrophone(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionVoiceEnable(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:setNoSave(arg0) end

---@public
---@return String
function Core:getPoisonousMushroom() end

---@public
---@param arg0 boolean
---@return void
function Core:setVehiclesWarningShow(arg0) end

---@public
---@return int
function Core:getOptionReloadDifficulty() end

---@public
---@param arg0 boolean
---@return void
function Core:setShowYourUsername(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionShowChatTimestamp(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionShowChatTitle(arg0) end

---@public
---@return int
function Core:getOffscreenTrueHeight() end

---@public
---@return boolean
function Core:isZoomEnabled() end

---@public
---@param keyMaps Map|String|Integer
---@return void
function Core:setKeyMaps(keyMaps) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function Core:DoEndFrameStuffFx(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 Object
---@return void
function Core:setOptionOnStartup(arg0, arg1) end

---@public
---@param d int
---@return void
function Core:setOptionReloadDifficulty(d) end

---@public
---@param arg0 boolean
---@return void
function Core:setModsPopupDone(arg0) end

---@public
---@return boolean
function Core:isOptionProgressBar() end

---@public
---@return String
function Core:getOptionContextMenuFont() end

---@public
---@return int
function Core:getOffscreenTrueWidth() end

---@public
---@param arg0 int
---@return void
function Core:setOptionBloodDecals(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:zoomOptionChanged(arg0) end

---@public
---@return boolean
function Core:getOptionUIFBO() end

---@public
---@param enabled boolean
---@return void
function Core:setOptionModsEnabled(enabled) end

---@public
---@return boolean
function Core:getOptionRadialMenuKeyToggle() end

---@public
---@return boolean
function Core:isZombieGroupSound() end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionClock24Hour(arg0) end

---@public
---@param arg0 int
---@return void
function Core:setOptionClockSize(arg0) end

---@public
---@param res String
---@return void
function Core:setResolution(res) end

---@public
---@return boolean
function Core:getOptionVSync() end

---@public
---@return int
function Core:getOptionClockFormat() end

---@public
---@param arg0 String
---@return Object
function Core:getOptionOnStartup(arg0) end

---@public
---@return String
function Core:getSeenUpdateText() end

---@public
---@return void
function Core:quit() end

---@public
---@return boolean
function Core:getOptionShowCursorWhileAiming() end

---@public
---@param arg0 ColorInfo
---@return void
function Core:setObjectHighlitedColor(arg0) end

---@public
---@param arg0 int
---@param arg1 float
---@return int
function Core:getYAngle(arg0, arg1) end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionUpdateSneakButton(arg0) end

---@public
---@return void
function Core:updateKeyboard() end

---@public
---@param newSaveFolder String
---@return void
function Core:setSaveFolder(newSaveFolder) end

---@public
---@return void
function Core:getOpenGLVersions() end

---@private
---@param arg0 UITextBox2
---@param arg1 int
---@return void
function Core:updateKeyboardAux(arg0, arg1) end

---@public
---@param imgPixels int[]
---@param imgw int
---@param imgh int
---@return int[]
function Core:flipPixels(imgPixels, imgw, imgh) end

---@public
---@return void
function Core:initFBOs() end

---@public
---@return boolean
function Core:isOptiondblTapJogToSprint() end

---@public
---@return void
function Core:exitToMenu() end

---@public
---@return int
function Core:getMicVolumeIndicator() end

---@private
---@param arg0 File
---@param arg1 String
---@return void
function Core:searchFolders(arg0, arg1) end

---@public
---@param volume int
---@return void
function Core:setOptionMusicVolume(volume) end

---@public
---@param arg0 boolean
---@return void
function Core:setToggleToSprint(arg0) end

---@public
---@param arg0 int
---@return void
function Core:setOptionVoiceVADMode(arg0) end

---@public
---@param bUse boolean
---@return void
function Core:setUseShaders(bUse) end

---@public
---@return boolean
function Core:isCelsius() end

---@private
---@param arg0 File
---@param arg1 String
---@return void
function Core:copyPasteFile(arg0, arg1) end

---@public
---@return ColorInfo
function Core:getObjectHighlitedColor() end

---@public
---@return int
function Core:getOptionBloodDecals() end

---@public
---@param arg0 boolean
---@return void
function Core:setGotNewBelt(arg0) end

---@public
---@return boolean
function Core:isDoingTextEntry() end

---@public
---@return String
function Core:getOptionCycleContainerKey() end

---@public
---@return boolean
function Core:isOptionShowChatTitle() end

---@public
---@return boolean
function Core:getServerVOIPEnable() end

---@public
---@param showPing boolean
---@return void
function Core:setShowPing(showPing) end

---@public
---@param arg0 String
---@return void
function Core:setSeenUpdateText(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionBorderlessWindow(arg0) end

---@public
---@return int
function Core:getGLMajorVersion() end

---@public
---@param arg0 String
---@return void
function Core:setOptionZoomLevels1x(arg0) end

---@public
---@return boolean
function Core:isFlashIsoCursor() end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionDisplayAsCelsius(arg0) end

---@public
---@return int
function Core:getOptionSimpleClothingTextures() end

---@public
---@param font String
---@return void
function Core:setOptionInventoryFont(font) end

---@public
---@return boolean
function Core:getOptionReloadRadialInstant() end

---@public
---@param arg0 int
---@return void
function Core:setOptionMusicLibrary(arg0) end

---@public
---@return boolean
function Core:getOptionDoWindSpriteEffects() end

---@public
---@return boolean
function Core:isVehiclesWarningShow() end

---@public
---@param arg0 String
---@return void
function Core:setOptionVoiceRecordDeviceName(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:setShowFirstTimeSneakTutorial(arg0) end

---@public
---@return String
function Core:getOptionTooltipFont() end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 float
---@param arg3 int
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 boolean
---@return void
function Core:DoStartFrameStuffInternal(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@return boolean
function Core:getOptionUpdateSneakButton() end

---@public
---@param arg0 int
---@return void
function Core:setOptionAimOutline(arg0) end

---@public
---@param arg0 String
---@return void
function Core:setPoisonousMushroom(arg0) end

---@public
---@return boolean
function Core:isToggleToRun() end

---@public
---@return boolean
function Core:isRenderPrecipIndoors() end

---@public
---@return int
function Core:getPerfPuddles() end

---@public
---@return KahluaTable
function Core:getScreenModes() end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionTimedActionGameSpeedReset(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionTextureCompression(arg0) end

---@public
---@return String
function Core:getDifficulty() end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionLeaveKeyInIgnition(arg0) end

---@public
---@return boolean
function Core:getOptionChatOpaqueOnFocus() end

---@public
---@param playerIndex int
---@param del int
---@return void
function Core:doZoomScroll(playerIndex, del) end

---@public
---@param arg0 boolean
---@return void
function Core:setOptiondblTapJogToSprint(arg0) end

---@public
---@param w int
---@param h int
---@return void
function Core:DoEndFrameStuff(w, h) end

---@public
---@param arg0 boolean
---@return void
function Core:setContentTranslationsEnabled(arg0) end

---@public
---@return String
function Core:getOptionMeasurementFormat() end

---@private
---@return void
function Core:sharedInit() end

---@public
---@param arg0 int
---@return void
function Core:setIsoCursorVisibility(arg0) end

---@public
---@return int
function Core:getOptionSoundVolume() end

---@public
---@param arg0 boolean
---@return void
function Core:setShowFirstTimeVehicleTutorial(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:setFlashIsoCursor(arg0) end

---@public
---@return String
function Core:getGLVersion() end

---@public
---@param sync boolean
---@return void
function Core:setOptionVSync(sync) end

---@public
---@return boolean
function Core:isOptionShowChatTimestamp() end

---@public
---@param arg0 boolean
---@return void
function Core:setOptionShowCursorWhileAiming(arg0) end

---@public
---@param arg0 boolean
---@return void
function Core:setAnimPopupDone(arg0) end

---@public
---@return boolean
function Core:getPerfReflections() end

---throws org.lwjgl.LWJGLException
---@public
---@param bool boolean
---@return void
function Core:setFullScreen(bool) end
