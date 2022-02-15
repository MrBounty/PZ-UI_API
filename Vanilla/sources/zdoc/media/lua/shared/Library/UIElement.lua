---@class UIElement : zombie.ui.UIElement
---@field tempcol Color
---@field toAdd ArrayList|Unknown
---@field white Texture
---@field StencilLevel int
---@field public capture boolean
---@field public IgnoreLossControl boolean
---@field public clickedValue String
---@field public Controls ArrayList|UIElement
---@field public defaultDraw boolean
---@field public followGameWorld boolean
---@field private renderThisPlayerOnly int
---@field public height float
---@field public Parent UIElement
---@field public visible boolean
---@field public width float
---@field public x double
---@field public y double
---@field public _table KahluaTable
---@field public alwaysBack boolean
---@field public bScrollChildren boolean
---@field public bScrollWithParent boolean
---@field private bRenderClippedChildren boolean
---@field public anchorTop boolean
---@field public anchorLeft boolean
---@field public anchorRight boolean
---@field public anchorBottom boolean
---@field public playerContext int
---@field alwaysOnTop boolean
---@field maxDrawHeight int
---@field yScroll Double
---@field xScroll Double
---@field scrollHeight int
---@field lastheight double
---@field lastwidth double
---@field bResizeDirty boolean
---@field enabled boolean
---@field private toTop ArrayList|Unknown
---@field private bConsumeMouseEvents boolean
---@field private leftDownTime long
---@field private clicked boolean
---@field private clickX double
---@field private clickY double
---@field private uiname String
---@field private bWantKeyEvents boolean
---@field private bForceCursorVisible boolean
UIElement = {}

---@public
---@return void
function UIElement:onresize() end

---@public
---@param tex Texture
---@param x double
---@param y double
---@param r double
---@param g double
---@param b double
---@param a double
---@return void
function UIElement:DrawTextureColor(tex, x, y, r, g, b, a) end

---@public
---@return int
function UIElement:getPlayerContext() end

---@public
---@param arg0 int
---@return void
function UIElement:setRenderThisPlayerOnly(arg0) end

---@public
---@param arg0 int
---@return void
function UIElement:onKeyRepeat(arg0) end

---@private
---@param arg0 UIElement
---@return void
function UIElement:addBringToTop(arg0) end

---@public
---@param arg0 double
---@return Double
function UIElement:clampToParentX(arg0) end

---@public
---@param el UIElement
---@return void
function UIElement:RemoveControl(el) end

---@public
---@return Boolean
function UIElement:isDefaultDraw() end

---@public
---@return Boolean
function UIElement:isAnchorRight() end

---@public
---@return Boolean
function UIElement:isIgnoreLossControl() end

---@public
---@param tex Texture
---@param x double
---@param y double
---@param width double
---@param height double
---@param alpha double
---@return void
function UIElement:DrawTextureScaled(tex, x, y, width, height, alpha) end

---@public
---@param Parent UIElement @the Parent to set
---@return void
function UIElement:setParent(Parent) end

---@public
---@param tex Texture
---@param x double
---@param y double
---@param width int
---@param height int
---@param col Color
---@return void
function UIElement:DrawTexture_FlippedX(tex, x, y, width, height, col) end

---@public
---@return void
function UIElement:render() end

---@public
---@return Double
function UIElement:getX() end

---@public
---@param arg0 Texture
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@param arg7 double
---@param arg8 double
---@return void
function UIElement:DrawTextureTiledX(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@param width double @the width to set
---@return void
function UIElement:setWidth(width) end

---@public
---@param bScroll boolean
---@return void
function UIElement:setScrollChildren(bScroll) end

---@public
---@param nPlayer int
---@return void
function UIElement:setPlayerContext(nPlayer) end

---@public
---@param name String
---@return void
function UIElement:ButtonClicked(name) end

---@public
---@param tex Texture
---@param x double
---@param y double
---@param width double
---@param height double
---@param r double
---@param g double
---@param b double
---@param alpha double
---@return void
function UIElement:DrawTextureScaledAspect(tex, x, y, width, height, r, g, b, alpha) end

---@public
---@param _table KahluaTable @the table to set
---@return void
function UIElement:setTable(_table) end

---@public
---@param arg0 double
---@return void
function UIElement:setScrollHeight(arg0) end

---@public
---@param visible boolean @the visible to set
---@return void
function UIElement:setVisible(visible) end

---@public
---@param arg0 Texture
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@param arg7 double
---@param arg8 double
---@param arg9 double
---@param arg10 double
---@param arg11 double
---@param arg12 double
---@return void
function UIElement:DrawSubTextureRGBA(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12) end

---@public
---@param height double
---@return void
function UIElement:setHeightOnly(height) end

---@public
---@param x double
---@return void
function UIElement:setXScroll(x) end

---@public
---@param el UIElement
---@return void
function UIElement:AddChild(el) end

---@public
---@param arg0 boolean
---@return void
function UIElement:setForceCursorVisible(arg0) end

---@public
---@param arg0 double
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@return void
function UIElement:setStencilRect(arg0, arg1, arg2, arg3) end

---@public
---@return Double
function UIElement:getHeight() end

---@public
---@return boolean
function UIElement:isEnabled() end

---@public
---@param text String
---@param x double
---@param y double
---@param r double
---@param g double
---@param b double
---@param alpha double
---@return void
---@overload fun(font:UIFont, text:String, x:double, y:double, r:double, g:double, b:double, alpha:double)
---@overload fun(arg0:String, arg1:double, arg2:double, arg3:double, arg4:double, arg5:double, arg6:double, arg7:double, arg8:double)
---@overload fun(arg0:UIFont, arg1:String, arg2:double, arg3:double, arg4:double, arg5:double, arg6:double, arg7:double, arg8:double)
function UIElement:DrawText(text, x, y, r, g, b, alpha) end

---@public
---@param font UIFont
---@param text String
---@param x double
---@param y double
---@param r double
---@param g double
---@param b double
---@param alpha double
---@return void
function UIElement:DrawText(font, text, x, y, r, g, b, alpha) end

---@public
---@param arg0 String
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@param arg7 double
---@param arg8 double
---@return void
function UIElement:DrawText(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@param arg0 UIFont
---@param arg1 String
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@param arg7 double
---@param arg8 double
---@return void
function UIElement:DrawText(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@param followGameWorld boolean @the followGameWorld to set
---@return void
function UIElement:setFollowGameWorld(followGameWorld) end

---@public
---@param tex Texture
---@param x double
---@param y double
---@param width double
---@param height double
---@param col Color
---@return void
---@overload fun(tex:Texture, x:double, y:double, width:double, height:double, r:double, g:double, b:double, a:double)
function UIElement:DrawTextureScaledCol(tex, x, y, width, height, col) end

---@public
---@param tex Texture
---@param x double
---@param y double
---@param width double
---@param height double
---@param r double
---@param g double
---@param b double
---@param a double
---@return void
function UIElement:DrawTextureScaledCol(tex, x, y, width, height, r, g, b, a) end

---@public
---@param font UIFont
---@param text String
---@param x double
---@param y double
---@param r double
---@param g double
---@param b double
---@param alpha double
---@return void
function UIElement:DrawTextUntrimmed(font, text, x, y, r, g, b, alpha) end

---@public
---@param width double
---@return void
function UIElement:setWidthSilent(width) end

---@public
---@param height double @the height to set
---@return void
function UIElement:setHeight(height) end

---@public
---@param IgnoreLossControl boolean @the IgnoreLossControl to set
---@return void
function UIElement:setIgnoreLossControl(IgnoreLossControl) end

---@public
---@return void
function UIElement:backMost() end

---@public
---@param arg0 double
---@param arg1 double
---@return Boolean
function UIElement:onMouseMove(arg0, arg1) end

---@public
---@return Double
function UIElement:getAbsoluteX() end

---@public
---@return void
function UIElement:suspendStencil() end

---@public
---@param arg0 boolean
---@return void
function UIElement:setEnabled(arg0) end

---@public
---@param tex Texture
---@param x Double
---@param y Double
---@param width Double
---@param height Double
---@param r Double
---@param g Double
---@param b Double
---@param a Double
---@return void
function UIElement:DrawTextureScaledColor(tex, x, y, width, height, r, g, b, a) end

---@public
---@param text String
---@param x double
---@param y double
---@param r double
---@param g double
---@param b double
---@param alpha double
---@return void
---@overload fun(font:UIFont, text:String, x:double, y:double, r:double, g:double, b:double, alpha:double)
function UIElement:DrawTextRight(text, x, y, r, g, b, alpha) end

---@public
---@param font UIFont
---@param text String
---@param x double
---@param y double
---@param r double
---@param g double
---@param b double
---@param alpha double
---@return void
function UIElement:DrawTextRight(font, text, x, y, r, g, b, alpha) end

---@public
---@return void
function UIElement:onResize() end

---@public
---@return void
function UIElement:ignoreWidthChange() end

---@public
---@param el UIElement
---@return void
function UIElement:RemoveChild(el) end

---@public
---@param anchorBottom boolean @the anchorBottom to set
---@return void
function UIElement:setAnchorBottom(anchorBottom) end

---@public
---@return int
function UIElement:getRenderThisPlayerOnly() end

---@public
---@return boolean
function UIElement:isWantKeyEvents() end

---@public
---@return String
function UIElement:getUIName() end

---@public
---@param arg0 Texture
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@param arg7 double
---@param arg8 double
---@return void
function UIElement:DrawTextureTiled(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@param x double @the x to set
---@return void
function UIElement:setX(x) end

---@public
---@param clickedValue String @the clickedValue to set
---@return void
function UIElement:setClickedValue(clickedValue) end

---@public
---@param anchorTop boolean @the anchorTop to set
---@return void
function UIElement:setAnchorTop(anchorTop) end

---@public
---@param arg0 double
---@return Boolean
function UIElement:onMouseWheel(arg0) end

---@public
---@return Double
function UIElement:getXScroll() end

---@public
---@return String
function UIElement:getClickedValue() end

---@public
---@return Boolean
function UIElement:isFollowGameWorld() end

---@public
---@return void
function UIElement:clearMaxDrawHeight() end

---@public
---@return KahluaTable @the table
function UIElement:getTable() end

---@public
---@param tex Texture
---@param x double
---@param y double
---@param width int
---@param height int
---@param col Color
---@return void
function UIElement:DrawTextureIgnoreOffset(tex, x, y, width, height, col) end

---@public
---@param height double
---@return void
function UIElement:setHeightSilent(height) end

---@public
---@return void
function UIElement:resumeStencil() end

---@public
---@param arg0 Texture
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@param arg7 double
---@param arg8 double
---@return void
function UIElement:DrawTextureScaledAspect2(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@return void
function UIElement:clearStencilRect() end

---@public
---@param arg0 boolean
---@return void
function UIElement:setRenderClippedChildren(arg0) end

---@public
---@return Double
function UIElement:getWidth() end

---@public
---@param width double
---@return void
function UIElement:setWidthOnly(width) end

---@public
---@param text String
---@param x double
---@param y double
---@param r double
---@param g double
---@param b double
---@param alpha double
---@return void
---@overload fun(font:UIFont, text:String, x:double, y:double, r:double, g:double, b:double, alpha:double)
function UIElement:DrawTextCentre(text, x, y, r, g, b, alpha) end

---@public
---@param font UIFont
---@param text String
---@param x double
---@param y double
---@param r double
---@param g double
---@param b double
---@param alpha double
---@return void
function UIElement:DrawTextCentre(font, text, x, y, r, g, b, alpha) end

---@public
---@param capture boolean @the capture to set
---@return void
function UIElement:setCapture(capture) end

---@public
---@param name String
---@return void
function UIElement:setUIName(name) end

---@public
---@param arg0 double
---@param arg1 double
---@return void
function UIElement:onMouseMoveOutside(arg0, arg1) end

---@public
---@param arg0 Texture
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@param arg7 double
---@param arg8 double
---@return void
function UIElement:DrawTextureTiledY(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@return Double
function UIElement:getYScroll() end

---@public
---@return UIElement @the Parent
function UIElement:getParent() end

---@public
---@return boolean
function UIElement:isForceCursorVisible() end

---@public
---@param tex Texture
---@param x double
---@param y double
---@param col Color
---@return void
function UIElement:DrawTextureCol(tex, x, y, col) end

---@public
---@param tex Texture
---@param x double
---@param y double
---@param alpha double
---@return void
---@overload fun(arg0:Texture, arg1:double, arg2:double, arg3:double, arg4:double, arg5:double, arg6:double, arg7:double, arg8:double, arg9:double, arg10:double, arg11:double, arg12:double)
function UIElement:DrawTexture(tex, x, y, alpha) end

---@public
---@param arg0 Texture
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@param arg7 double
---@param arg8 double
---@param arg9 double
---@param arg10 double
---@param arg11 double
---@param arg12 double
---@return void
function UIElement:DrawTexture(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12) end

---@public
---@param arg0 boolean
---@return void
function UIElement:setWantKeyEvents(arg0) end

---@public
---@param arg0 int
---@return boolean
function UIElement:isKeyConsumed(arg0) end

---@public
---@param arg0 int
---@return void
function UIElement:onKeyPress(arg0) end

---@public
---@param arg0 double
---@param arg1 double
---@return Boolean
function UIElement:onMouseUp(arg0, arg1) end

---@param arg0 double
---@param arg1 double
---@return void
function UIElement:onRightMouseDownOutside(arg0, arg1) end

---@public
---@param Controls Vector|UIElement @the Controls to set
---@return void
function UIElement:setControls(Controls) end

---@public
---@return Double
function UIElement:getAbsoluteY() end

---@public
---@return Double
function UIElement:getScrollHeight() end

---@public
---@return Boolean
function UIElement:isCapture() end

---@public
---@param arg0 Texture
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@param arg7 double
---@return void
function UIElement:DrawTextureScaledUniform(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@return boolean @the anchorTop
function UIElement:isAnchorTop() end

---@protected
---@param arg0 String
---@return Object
function UIElement:tryGetTableValue(arg0) end

---@public
---@param anchorLeft boolean @the anchorLeft to set
---@return void
function UIElement:setAnchorLeft(anchorLeft) end

---@public
---@return Double
function UIElement:getY() end

---@public
---@param arg0 double
---@param arg1 double
---@return void
function UIElement:onMouseUpOutside(arg0, arg1) end

---@public
---@param tex Texture
---@param x double
---@param y double
---@param width double
---@param height double
---@param col Color
---@param xStart double
---@param yStart double
---@param xEnd double
---@param yEnd double
---@return void
function UIElement:DrawUVSliceTexture(tex, x, y, width, height, col, xStart, yStart, xEnd, yEnd) end

---@public
---@param height double
---@return void
function UIElement:setMaxDrawHeight(height) end

---@public
---@return Boolean
function UIElement:isAnchorLeft() end

---@public
---@param arg0 double
---@param arg1 double
---@return Boolean
function UIElement:onMouseDown(arg0, arg1) end

---@private
---@param arg0 double
---@param arg1 double
---@return Boolean
function UIElement:onMouseDoubleClick(arg0, arg1) end

---@public
---@param arg0 double
---@param arg1 double
---@return Boolean
function UIElement:onRightMouseUp(arg0, arg1) end

---@public
---@return Boolean
function UIElement:getScrollChildren() end

---@public
---@param bConsume boolean
---@return void
function UIElement:setConsumeMouseEvents(bConsume) end

---@public
---@param arg0 UIElement
---@return Double
function UIElement:getXScrolled(arg0) end

---@public
---@param tex Texture
---@param x double
---@param y double
---@param width int
---@param height int
---@param col Color
---@return void
function UIElement:DrawTexture_FlippedXIgnoreOffset(tex, x, y, width, height, col) end

---@public
---@param defaultDraw boolean @the defaultDraw to set
---@return void
function UIElement:setDefaultDraw(defaultDraw) end

---@public
---@param arg0 int
---@return void
function UIElement:onKeyRelease(arg0) end

---@param arg0 double
---@param arg1 double
---@return void
function UIElement:onMouseDownOutside(arg0, arg1) end

---@public
---@return Boolean
function UIElement:isAnchorBottom() end

---@public
---@param arg0 double
---@param arg1 double
---@return Boolean
function UIElement:onRightMouseDown(arg0, arg1) end

---@public
---@return void
function UIElement:bringToTop() end

---@public
---@param b boolean
---@return void
function UIElement:setAlwaysOnTop(b) end

---@public
---@param y double
---@return void
function UIElement:setYScroll(y) end

---@public
---@param arg0 Texture
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@return void
---@overload fun(arg0:Texture, arg1:double, arg2:double, arg3:double, arg4:double, arg5:double, arg6:double, arg7:double)
function UIElement:DrawTextureAngle(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 Texture
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@param arg7 double
---@return void
function UIElement:DrawTextureAngle(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@param arg0 UIElement
---@return Double
function UIElement:getYScrolled(arg0) end

---@public
---@return void
function UIElement:ignoreHeightChange() end

---@public
---@param y double @the y to set
---@return void
function UIElement:setY(y) end

---@public
---@return boolean
function UIElement:isConsumeMouseEvents() end

---@public
---@param arg0 double
---@param arg1 double
---@return Boolean
function UIElement:isPointOver(arg0, arg1) end

---@public
---@return Boolean
function UIElement:getScrollWithParent() end

---@public
---@return ArrayList|UIElement @the Controls
function UIElement:getControls() end

---@public
---@param arg0 UIElement
---@return void
function UIElement:BringToTop(arg0) end

---@public
---@return Double
function UIElement:getMaxDrawHeight() end

---@public
---@param anchorRight boolean @the anchorRight to set
---@return void
function UIElement:setAnchorRight(anchorRight) end

---@public
---@return Boolean
function UIElement:isMouseOver() end

---@public
---@param bScroll boolean
---@return void
function UIElement:setScrollWithParent(bScroll) end

---@public
---@return Boolean
function UIElement:isVisible() end

---@param arg0 double
---@param arg1 double
---@return void
function UIElement:onRightMouseUpOutside(arg0, arg1) end

---@public
---@return void
function UIElement:ClearChildren() end

---@public
---@param arg0 double
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@return void
function UIElement:repaintStencilRect(arg0, arg1, arg2, arg3) end

---@public
---@return void
function UIElement:update() end

---@public
---@param arg0 double
---@return Double
function UIElement:clampToParentY(arg0) end
