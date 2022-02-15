---@class UIManager : zombie.ui.UIManager
---@field public lastMouseX int
---@field public lastMouseY int
---@field public Picked IsoObjectPicker.ClickObject
---@field public clock Clock
---@field public UI ArrayList|Unknown
---@field public toolTip ObjectTooltip
---@field public mouseArrow Texture
---@field public mouseExamine Texture
---@field public mouseAttack Texture
---@field public mouseGrab Texture
---@field public speedControls SpeedControls
---@field public DebugConsole UIDebugConsole
---@field public ServerToolbox UIServerToolbox
---@field public MoodleUI MoodlesUI[]
---@field public bFadeBeforeUI boolean
---@field public ProgressBar ActionProgressBar[]
---@field public FadeAlpha float
---@field public FadeInTimeMax int
---@field public FadeInTime int
---@field public FadingOut boolean
---@field public lastMouseTexture Texture
---@field public LastPicked IsoObject
---@field public DoneTutorials ArrayList|Unknown
---@field public lastOffX float
---@field public lastOffY float
---@field public Modal ModalDialog
---@field public KeyDownZoomIn boolean
---@field public KeyDownZoomOut boolean
---@field public doTick boolean
---@field public VisibleAllUI boolean
---@field public UIFBO TextureFBO
---@field public useUIFBO boolean
---@field public black Texture
---@field public bSuspend boolean
---@field public lastAlpha float
---@field public PickedTileLocal JVector2
---@field public PickedTile JVector2
---@field public RightDownObject IsoObject
---@field public uiUpdateTimeMS long
---@field public uiUpdateIntervalMS long
---@field public uiRenderTimeMS long
---@field public uiRenderIntervalMS long
---@field private tutorialStack ArrayList|Unknown
---@field public toTop ArrayList|UIElement
---@field public defaultthread KahluaThread
---@field public previousThread KahluaThread
---@field toRemove ArrayList|Unknown
---@field toAdd ArrayList|Unknown
---@field wheel int
---@field lastwheel int
---@field debugUI ArrayList|Unknown
---@field bShowLuaDebuggerOnError boolean
---@field sync UIManager.Sync
---@field private showPausedMessage boolean
---@field private playerInventoryUI UIElement
---@field private playerLootUI UIElement
---@field private playerInventoryTooltip UIElement
---@field private playerLootTooltip UIElement
---@field private playerFadeInfo UIManager.FadeInfo[]
UIManager = {}

---@public
---@return void
function UIManager:closeContainers() end

---@public
---@return JVector2 @the PickedTileLocal
function UIManager:getPickedTileLocal() end

---@public
---@param arg0 double
---@return void
---@overload fun(arg0:double, arg1:double)
function UIManager:FadeOut(arg0) end

---@public
---@param arg0 double
---@param arg1 double
---@return void
function UIManager:FadeOut(arg0, arg1) end

---@public
---@param aPicked IsoObjectPicker.ClickObject @the Picked to set
---@return void
function UIManager:setPicked(aPicked) end

---@public
---@param filename String
---@param pc long
---@return void
function UIManager:debugBreakpoint(filename, pc) end

---@public
---@param aMouseAttack Texture @the mouseAttack to set
---@return void
function UIManager:setMouseAttack(aMouseAttack) end

---@public
---@param showPausedMessage boolean
---@return void
function UIManager:setShowPausedMessage(showPausedMessage) end

---@public
---@param aMouseGrab Texture @the mouseGrab to set
---@return void
function UIManager:setMouseGrab(aMouseGrab) end

---@public
---@param aPickedTile JVector2 @the PickedTile to set
---@return void
function UIManager:setPickedTile(aPickedTile) end

---@public
---@return Texture @the black
function UIManager:getBlack() end

---@public
---@return float @the lastOffX
function UIManager:getLastOffX() end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function UIManager:setDoneTutorials(arg0) end

---@public
---@return Double
function UIManager:getDoubleClickInterval() end

---@public
---@return float @the lastAlpha
function UIManager:getLastAlpha() end

---@public
---@param el UIElement
---@return void
function UIManager:AddUI(el) end

---@public
---@param arg0 int
---@return boolean
function UIManager:onKeyRepeat(arg0) end

---@public
---@return boolean
function UIManager:isMouseOverInventory() end

---@public
---@param abFadeBeforeUI boolean @the bFadeBeforeUI to set
---@return void
function UIManager:setbFadeBeforeUI(abFadeBeforeUI) end

---@public
---@return ArrayList|Unknown
function UIManager:getUI() end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function UIManager:CreateFBO(arg0, arg1) end

---@public
---@return UIServerToolbox
function UIManager:getServerToolbox() end

---@public
---@return Texture @the lastMouseTexture
function UIManager:getLastMouseTexture() end

---@public
---@return UIDebugConsole @the DebugConsole
function UIManager:getDebugConsole() end

---@public
---@param arg0 int
---@return boolean
function UIManager:onKeyRelease(arg0) end

---@public
---@param arg0 double
---@return void
function UIManager:setFadeInTime(arg0) end

---@public
---@return Double
---@overload fun(arg0:double)
function UIManager:getFadeAlpha() end

---@public
---@param arg0 double
---@return float
function UIManager:getFadeAlpha(arg0) end

---@public
---@return void
function UIManager:resize() end

---@public
---@param aDebugConsole UIDebugConsole @the DebugConsole to set
---@return void
function UIManager:setDebugConsole(aDebugConsole) end

---@public
---@return ObjectTooltip @the toolTip
function UIManager:getToolTip() end

---@public
---@param arg0 double
---@param arg1 double
---@param arg2 double
---@return JVector2
function UIManager:getTileFromMouse(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@param arg1 UIElement
---@param arg2 UIElement
---@return void
function UIManager:setPlayerInventoryTooltip(arg0, arg1, arg2) end

---@public
---@return boolean
function UIManager:isShowLuaDebuggerOnError() end

---@public
---@param aToolTip ObjectTooltip @the toolTip to set
---@return void
function UIManager:setToolTip(aToolTip) end

---@public
---@return boolean
function UIManager:isFBOActive() end

---@public
---@return Texture @the mouseAttack
function UIManager:getMouseAttack() end

---@public
---@return Double
function UIManager:getLastMouseY() end

---@public
---@param aLastMouseTexture Texture @the lastMouseTexture to set
---@return void
function UIManager:setLastMouseTexture(aLastMouseTexture) end

---@public
---@return double
function UIManager:getMillisSinceLastUpdate() end

---@public
---@param arg0 double
---@return MoodlesUI
function UIManager:getMoodleUI(arg0) end

---@public
---@return Double
function UIManager:getFadeInTime() end

---@public
---@param arg0 double
---@return void
function UIManager:setLastMouseX(arg0) end

---@public
---@param arg0 double
---@return void
function UIManager:setFadeInTimeMax(arg0) end

---@public
---@param playerIndex int
---@param bFadeBeforeUI boolean
---@return void
function UIManager:setFadeBeforeUI(playerIndex, bFadeBeforeUI) end

---@public
---@return Double
function UIManager:getLastMouseX() end

---@public
---@param aMouseExamine Texture @the mouseExamine to set
---@return void
function UIManager:setMouseExamine(aMouseExamine) end

---@param arg0 UIElement
---@return void
function UIManager:pushToTop(arg0) end

---@public
---@return ModalDialog @the Modal
function UIManager:getModal() end

---@public
---@return Double
function UIManager:getFadeInTimeMax() end

---@public
---@return SpeedControls @the speedControls
function UIManager:getSpeedControls() end

---@public
---@param arg0 int
---@param arg1 UIElement
---@param arg2 UIElement
---@return void
function UIManager:setPlayerInventory(arg0, arg1, arg2) end

---@public
---@return boolean
function UIManager:isForceCursorVisible() end

---@public
---@return void
function UIManager:updateBeforeFadeOut() end

---@public
---@param arg0 boolean
---@return void
function UIManager:setVisibleAllUI(arg0) end

---@public
---@return ArrayList|Unknown
function UIManager:getDoneTutorials() end

---@public
---@return void
function UIManager:CloseContainers() end

---@public
---@param arg0 UIServerToolbox
---@return void
function UIManager:setServerToolbox(arg0) end

---@public
---@param arg0 double
---@param arg1 MoodlesUI
---@return void
function UIManager:setMoodleUI(arg0, arg1) end

---@public
---@return double
function UIManager:getSecondsSinceLastRender() end

---@public
---@param arg0 Texture
---@param arg1 double
---@param arg2 double
---@return void
---@overload fun(arg0:Texture, arg1:double, arg2:double, arg3:double, arg4:double, arg5:double)
function UIManager:DrawTexture(arg0, arg1, arg2) end

---@public
---@param arg0 Texture
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@return void
function UIManager:DrawTexture(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@return void
function UIManager:init() end

---@public
---@return double
function UIManager:getSecondsSinceLastUpdate() end

---@public
---@return Double
function UIManager:getDoubleClickDist() end

---@public
---@return JVector2 @the PickedTile
function UIManager:getPickedTile() end

---@private
---@param arg0 ArrayList|Unknown
---@param arg1 boolean
---@param arg2 int
---@return void
function UIManager:executeGame(arg0, arg1, arg2) end

---@public
---@return Texture @the mouseGrab
function UIManager:getMouseGrab() end

---@public
---@param arg0 double
---@param arg1 ActionProgressBar
---@return void
function UIManager:setProgressBar(arg0, arg1) end

---@public
---@param arg0 int
---@return boolean
function UIManager:onKeyPress(arg0) end

---@public
---@return float @the lastOffY
function UIManager:getLastOffY() end

---@protected
---@param arg0 double
---@param arg1 double
---@return void
function UIManager:updateTooltip(arg0, arg1) end

---@public
---@param aPickedTileLocal JVector2 @the PickedTileLocal to set
---@return void
function UIManager:setPickedTileLocal(aPickedTileLocal) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 boolean
---@return TextureFBO
function UIManager:createTexture(arg0, arg1, arg2) end

---@public
---@return Boolean
function UIManager:isFadingOut() end

---@public
---@param aBlack Texture @the black to set
---@return void
function UIManager:setBlack(aBlack) end

---@public
---@return double
function UIManager:getMillisSinceLastRender() end

---@private
---@return boolean
function UIManager:checkPicked() end

---@public
---@return IsoObject @the LastPicked
function UIManager:getLastPicked() end

---@public
---@param arg0 double
---@return void
function UIManager:setFadeAlpha(arg0) end

---@public
---@param arg0 double
---@return ActionProgressBar
function UIManager:getProgressBar(arg0) end

---@public
---@param arg0 double
---@return void
function UIManager:setLastMouseY(arg0) end

---@public
---@param el UIElement
---@return void
function UIManager:RemoveElement(el) end

---@public
---@return Texture @the mouseArrow
function UIManager:getMouseArrow() end

---@public
---@param aLastOffX float @the lastOffX to set
---@return void
function UIManager:setLastOffX(aLastOffX) end

---@public
---@param aLastPicked IsoObject @the LastPicked to set
---@return void
function UIManager:setLastPicked(aLastPicked) end

---@public
---@param arg0 boolean
---@return void
function UIManager:setShowLuaDebuggerOnError(arg0) end

---@public
---@return boolean @the bFadeBeforeUI
function UIManager:isbFadeBeforeUI() end

---@public
---@return boolean
function UIManager:isShowPausedMessage() end

---@public
---@return void
function UIManager:render() end

---@public
---@param aModal ModalDialog @the Modal to set
---@return void
function UIManager:setModal(aModal) end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function UIManager:setUI(arg0) end

---@public
---@param aClock Clock @the clock to set
---@return void
function UIManager:setClock(aClock) end

---@public
---@return void
function UIManager:update() end

---@private
---@return void
function UIManager:handleZoomKeys() end

---@public
---@param aSpeedControls SpeedControls @the speedControls to set
---@return void
function UIManager:setSpeedControls(aSpeedControls) end

---@public
---@param aLastOffY float @the lastOffY to set
---@return void
function UIManager:setLastOffY(aLastOffY) end

---@public
---@param arg0 double
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@return Boolean
function UIManager:isDoubleClick(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param aRightDownObject IsoObject @the RightDownObject to set
---@return void
function UIManager:setRightDownObject(aRightDownObject) end

---@public
---@param arg0 double
---@return void
---@overload fun(arg0:double, arg1:double)
function UIManager:FadeIn(arg0) end

---@public
---@param arg0 double
---@param arg1 double
---@return void
function UIManager:FadeIn(arg0, arg1) end

---@public
---@param arg0 double
---@param arg1 double
---@return void
function UIManager:setFadeTime(arg0, arg1) end

---@public
---@return IsoObject @the RightDownObject
function UIManager:getRightDownObject() end

---@public
---@param aLastAlpha float @the lastAlpha to set
---@return void
function UIManager:setLastAlpha(aLastAlpha) end

---@public
---@param aMouseArrow Texture @the mouseArrow to set
---@return void
function UIManager:setMouseArrow(aMouseArrow) end

---@public
---@return IsoObjectPicker.ClickObject @the Picked
function UIManager:getPicked() end

---@public
---@return Clock @the clock
function UIManager:getClock() end

---@public
---@param aFadingOut boolean @the FadingOut to set
---@return void
function UIManager:setFadingOut(aFadingOut) end

---@public
---@return Texture @the mouseExamine
function UIManager:getMouseExamine() end

---@public
---@return KahluaThread
function UIManager:getDefaultThread() end
