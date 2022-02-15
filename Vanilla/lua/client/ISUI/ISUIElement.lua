require "ISBaseObject"

ISUIElement = ISBaseObject:derive("ISUIElement");

ISUIElement.IDMax = 1;

--************************************************************************--
--** ISUIElement:initialise
--**
--************************************************************************--
function ISUIElement:initialise()
	-- FIXME: need to avoid calling this method more than once, ID changes
	self.children = {}
	--	Give each UI element a unique ID.
	self.ID = ISUIElement.IDMax;
	 ISUIElement.IDMax = ISUIElement.IDMax + 1;
end

function ISUIElement:setController(c)
    self.controller = c;
end

function ISUIElement:getController()
    return self.controller;
end

--************************************************************************--
--** ISUIElement:setAnchorBottom
--**
--************************************************************************--
function ISUIElement:setAnchorBottom(bAnchor)
	self.anchorBottom = bAnchor;
	if self.javaObject ~= nil then
		self.javaObject:setAnchorBottom(bAnchor);
	end
end

--************************************************************************--
--** ISUIElement:setAnchorTop
--**
--************************************************************************--
function ISUIElement:setAnchorTop(bAnchor)
	self.anchorTop = bAnchor;
	if self.javaObject ~= nil then
		self.javaObject:setAnchorTop(bAnchor);
	end
end

--************************************************************************--
--** ISUIElement:setAnchorLeft
--**
--************************************************************************--
function ISUIElement:setAnchorLeft(bAnchor)
	self.anchorLeft = bAnchor;
	if self.javaObject ~= nil then
		self.javaObject:setAnchorLeft(bAnchor);
	end
end

--************************************************************************--
--** ISUIElement:setAnchorRight
--**
--************************************************************************--
function ISUIElement:setAnchorRight(bAnchor)
	self.anchorRight = bAnchor;
	if self.javaObject ~= nil then
		self.javaObject:setAnchorRight(bAnchor);
	end
end

--************************************************************************--
--** ISUIElement:getKeepOnScreen
--**
--************************************************************************--
function ISUIElement:getKeepOnScreen()
	if self.keepOnScreen ~= nil then
		return self.keepOnScreen
	end
	return not self.parent
end

--************************************************************************--
--** ISUIElement:setX
--**
--************************************************************************--
function ISUIElement:setX(x)
	local xs = x
	if self:getKeepOnScreen() then
		local maxX = getCore():getScreenWidth();
		xs = math.max(0, math.min(x, maxX - self.width));
	end

   self.x = xs;
   
	if self.javaObject ~= nil then
		self.javaObject:setX(xs);
	end
end

--************************************************************************--
--** ISUIElement:setY
--**
--************************************************************************--
function ISUIElement:setY(y)
	local ys = y
	if self:getKeepOnScreen() then
		local maxY = getCore():getScreenHeight();
		ys = math.max(0, math.min(y, maxY - self.height));
	end
	
	self.y = ys;
	if self.javaObject ~= nil then
		self.javaObject:setY(ys);
	end
end

--************************************************************************--
--** ISUIElement:setWidth
--**
--************************************************************************--
function ISUIElement:setWidth(w)

   self.width = w;

	if self:getKeepOnScreen() then
		local maxX = getCore():getScreenWidth();
		self.x = math.max(0, math.min(self.x, maxX - self.width));
	end

	if self.javaObject ~= nil then
		self.javaObject:setWidth(w);
		self.javaObject:setX(self.x);
	end
end

--************************************************************************--
--** ISUIElement:setHeight
--**
--************************************************************************--
function ISUIElement:setHeight(h)
   self.height = h;

	if self:getKeepOnScreen() then
		local maxY = getCore():getScreenHeight();
		self.y = math.max(0, math.min(self.y, maxY - self.height));
	end

	if self.javaObject ~= nil then
		self.javaObject:setHeight(h);
		self.javaObject:setY(self.y);
	end
end

--************************************************************************--
--** ISUIElement:getWidth
--**
--************************************************************************--
function ISUIElement:getWidth()
	if self.javaObject == nil then
		self:instantiate();
	end
	return self.javaObject:getWidth();
end

--************************************************************************--
--** ISUIElement:getHeight
--**
--************************************************************************--
function ISUIElement:getHeight()
	if self.javaObject == nil then
		self:instantiate();
	end
	return self.javaObject:getHeight();
end

--************************************************************************--
--** ISUIElement:getRight
--**
--************************************************************************--
function ISUIElement:getRight()
	if self.javaObject == nil then
		self:instantiate();
	end
	return self.javaObject:getX() + self.javaObject:getWidth();
end

--************************************************************************--
--** ISUIElement:getBottom
--**
--************************************************************************--
function ISUIElement:getBottom()
	if self.javaObject == nil then
		self:instantiate();
	end
	return self.javaObject:getY() + self.javaObject:getHeight();
end

--************************************************************************--
--** ISUIElement:getScrollX
--**
--************************************************************************--
function ISUIElement:getXScroll()
	if self.javaObject == nil then
		self:instantiate();
	end
	return self.javaObject:getXScroll();
end

--************************************************************************--
--** ISUIElement:setWidthAndParentWidth
--**
--************************************************************************--
function ISUIElement:setWidthAndParentWidth(wi)
	ISPanel.setWidth(self, wi)
	local parent = self.parent
	local child = self
	while parent do
		parent:setWidth(child:getX() + child:getWidth())
		child = parent
		parent = parent.parent
	end
end

--************************************************************************--
--** ISUIElement:setHeightAndParentHeight
--**
--************************************************************************--
function ISUIElement:setHeightAndParentHeight(h)
	ISPanel.setHeight(self, h)
	local parent = self.parent
	local child = self
	while parent do
		parent:setHeight(child:getY() + child:getHeight())
		child = parent
		parent = parent.parent
	end
end

--************************************************************************--
--** ISUIElement:getScrollY
--**
--************************************************************************--
function ISUIElement:getYScroll()
	if self.javaObject == nil then
		self:instantiate();
	end
	return self.javaObject:getYScroll();
end

--************************************************************************--
--** ISUIElement:getMouseX
--**
--************************************************************************--
function ISUIElement:getMouseX()
	if self.javaObject == nil then
		self:instantiate();
	end
	return (getMouseX()-self.javaObject:getXScroll()) - self:getAbsoluteX();
end

--************************************************************************--
--** ISUIElement:getMouseY
--**
--************************************************************************--
function ISUIElement:getMouseY()
	if self.javaObject == nil then
		self:instantiate();
	end
	return (getMouseY()-self.javaObject:getYScroll()) - self:getAbsoluteY();
end

--************************************************************************--
--** ISUIElement:getCentreX
--**
--************************************************************************--
function ISUIElement:getCentreX()
	return self:getWidth() / 2.0;
end

--************************************************************************--
--** ISUIElement:getCentreY
--**
--************************************************************************--
function ISUIElement:getCentreY()
	return self:getHeight() / 2.0;
end

--************************************************************************--
--** ISUIElement:getX
--**
--************************************************************************--
function ISUIElement:getX()
	if self.javaObject == nil then
		self:instantiate();
	end
	return self.javaObject:getX();
end

--************************************************************************--
--** ISUIElement:getY
--**
--************************************************************************--
function ISUIElement:getY()
	if self.javaObject == nil then
		self:instantiate();
	end
	return self.javaObject:getY();
end



function ISUIElement:isEnabled()

	    if self.javaObject == nil then
    		self:instantiate();
    	end

        if self.enabled == nil then
    	    self.enabled = self.javaObject:isEnabled();
    	end

    	return self.enabled;

end

function ISUIElement:setEnabled(en)
	if self.javaObject == nil then
    		self:instantiate();
    	end

    	self.javaObject:setEnabled(en);
    	self.enabled = en;
end


--************************************************************************--
--** ISUIElement:getAbsoluteX
--**
--************************************************************************--
function ISUIElement:getAbsoluteX()
	if self.javaObject == nil then
		self:instantiate();
	end

	return self.javaObject:getAbsoluteX();
end

function ISUIElement:isMouseOver()
	if self.javaObject == nil then
		self:instantiate();
	end

	return self.javaObject:isMouseOver();
end

function ISUIElement:suspendStencil()
	if self.javaObject == nil then
		self:instantiate();
	end

	self.javaObject:suspendStencil();
end

function ISUIElement:resumeStencil()
	if self.javaObject == nil then
		self:instantiate();
	end

	self.javaObject:resumeStencil();
end

function ISUIElement:setStencilRect(x, y, w, h)
	if self.javaObject == nil then
		self:instantiate();
	end

	return self.javaObject:setStencilRect(x, y, w, h);
end

function ISUIElement:clearStencilRect()
	if self.javaObject == nil then
		self:instantiate();
	end

	return self.javaObject:clearStencilRect();
end

function ISUIElement:repaintStencilRect(x, y, w, h)
	if self.javaObject == nil then
		self:instantiate();
	end
	return self.javaObject:repaintStencilRect(x, y, w, h);
end

function ISUIElement:clampStencilRectToParent(x, y, w, h)
	if self.javaObject == nil then
		self:instantiate()
	end
	if self.parent then
		local absX,absY = self:getAbsoluteX(),self:getAbsoluteY()
		local stencilX,stencilY = x,y
		local stencilX2,stencilY2 = x+w,y+h
		if self.parent:isVScrollBarVisible() then
			stencilX2 = math.min(stencilX2, self.parent.width - self.parent.vscroll.width)
		end
		stencilX = self.javaObject:clampToParentX(absX + stencilX) - absX
		stencilX2 = self.javaObject:clampToParentX(absX + stencilX2) - absX
		stencilY = self.javaObject:clampToParentY(absY + stencilY) - absY
		stencilY2 = self.javaObject:clampToParentY(absY + stencilY2) - absY
		self:setStencilRect(stencilX, stencilY, stencilX2 - stencilX, stencilY2 - stencilY)
		return stencilX,stencilY,stencilX2-stencilX,stencilY2-stencilY
	end
	self.javaObject:setStencilRect(x, y, w, h)
	return x,y,w,h
end

function ISUIElement:ignoreWidthChange()
	if self.javaObject == nil then
		self:instantiate();
	end

	return self.javaObject:ignoreWidthChange();
end

function ISUIElement:getMaxDrawHeight()
    if self.javaObject == nil then
        self:instantiate();
    end

    return self.javaObject:getMaxDrawHeight();
end
function ISUIElement:setMaxDrawHeight(height)
    if self.javaObject == nil then
        self:instantiate();
    end

    return self.javaObject:setMaxDrawHeight(height);
end
function ISUIElement:clearMaxDrawHeight()
    if self.javaObject == nil then
        self:instantiate();
    end

    return self.javaObject:clearMaxDrawHeight();
end
function ISUIElement:ignoreHeightChange()
	if self.javaObject == nil then
		self:instantiate();
	end

	return self.javaObject:ignoreHeightChange();
end

--************************************************************************--
--** ISUIElement:getAbsoluteY
--**
--************************************************************************--
function ISUIElement:getAbsoluteY()
	if self.javaObject == nil then
		self:instantiate();
	end
	return self.javaObject:getAbsoluteY();
end

function ISUIElement:recalcSize()
	if self.javaObject == nil then
		self:instantiate();
	end
	return self.javaObject:onResize();

end


function ISUIElement:onResize()
	self.x = self:getX()
	self.y = self:getY()
	self.width = self:getWidth();
	self.height = self:getHeight();
	if(self.minimumWidth == nil) then
		self.minimumWidth = 0;
		self.minimumHeight = 0;
	end
	if(self.width < self.minimumWidth)  then
		self.width = self.minimumWidth
		if self.javaObject then
			self.javaObject:setWidthOnly(self.width)
		end
	end
	if(self.height < self.minimumHeight)  then
		self.height = self.minimumHeight
		if self.javaObject then
			self.javaObject:setHeightOnly(self.height)
		end
	end

	self:updateScrollbars();
end


--************************************************************************--
--** ISUIElement:setCapture
--**
--************************************************************************--
function ISUIElement:setCapture(bCapture)
	if self.javaObject == nil then
		self:instantiate();
	end
	self.javaObject:setCapture(bCapture);
end

--************************************************************************--
--** ISUIElement:getCapture
--**
--************************************************************************--
function ISUIElement:getIsCaptured()
	if self.javaObject == nil then
		self:instantiate();
	end
	return self.javaObject:isCapture();
end

--************************************************************************--
--** ISUIElement:setFollowGameWorld
--**
--************************************************************************--
function ISUIElement:setFollowGameWorld(bFollow)
	if self.javaObject == nil then
		self:instantiate();
	end
	self.javaObject:setFollowGameWorld(bFollow);
end

function ISUIElement:getIsFollowGameWorld()
	if self.javaObject == nil then
		self:instantiate();
	end
	return self.javaObject:isFollowGameWorld();
end

function ISUIElement:isFollowGameWorld()
	if self.javaObject == nil then
		self:instantiate();
	end
	return self.javaObject:isFollowGameWorld();
end

function ISUIElement:setRenderThisPlayerOnly(playerNum)
	if self.javaObject == nil then
		self:instantiate();
	end
	self.javaObject:setRenderThisPlayerOnly(playerNum);
end

function ISUIElement:getRenderThisPlayerOnly()
	if self.javaObject == nil then
		self:instantiate();
	end
	return self.javaObject:getRenderThisPlayerOnly();
end

function ISUIElement:onLoseJoypadFocus(joypadData)
    self.joyfocus = nil;
end

function ISUIElement:onGainJoypadFocus(joypadData)
    self.joyfocus = joypadData;
end

--************************************************************************--
--** ISUIElement:setVisible
--**
--************************************************************************--
function ISUIElement:setVisible(bVisible)
	if self.javaObject == nil then
		self:instantiate();
	end
	self.javaObject:setVisible(bVisible);
    if self.visibleTarget and self.visibleFunction then
        self.visibleFunction(self.visibleTarget, self);
    end
end

function ISUIElement:getJavaObject()
	return self.javaObject;
end

--************************************************************************--
--** ISUIElement:getIsVisible
--**
--************************************************************************--
function ISUIElement:getIsVisible()
	if self.javaObject == nil then
		self:instantiate();
	end
	return self.javaObject:isVisible();
end

function ISUIElement:isVisible()
    if self.javaObject == nil then
        self:instantiate();
    end
    return self.javaObject:isVisible();
end

function ISUIElement:isReallyVisible()
	if not self:getIsVisible() then return false end
--	if not UIManager.getUI():contains(self.javaObject) then return false end
	if self:getParent() then
		local parentJavaObject = self:getParent():getJavaObject()
		if parentJavaObject and not parentJavaObject:getControls():contains(self:getJavaObject()) then return false end
		return self:getParent():isReallyVisible()
	end
	return UIManager.getUI():contains(self.javaObject)
end

function ISUIElement:onJoypadDown(button)

end

function ISUIElement:onJoypadDirUp()

end

function ISUIElement:onJoypadDirDown()

end

function ISUIElement:onJoypadDirLeft()

end

function ISUIElement:onJoypadDirRight()

end


--************************************************************************--
--** ISUIElement:instantiate
--**
--************************************************************************--
function ISUIElement:instantiate()
	self.javaObject = UIElement.new(self);
	self.javaObject:setX(self.x);
	self.javaObject:setY(self.y);
	self.javaObject:setHeight(self.height);
	self.javaObject:setWidth(self.width);
	self.javaObject:setAnchorLeft(self.anchorLeft);
	self.javaObject:setAnchorRight(self.anchorRight);
	self.javaObject:setAnchorTop(self.anchorTop);
	self.javaObject:setAnchorBottom(self.anchorBottom);
	self.javaObject:setWantKeyEvents(self.wantKeyEvents or false);
	self.javaObject:setForceCursorVisible(self.forceCursorVisible or false);
	self:createChildren();
end

function ISUIElement:createChildren()

end

function ISUIElement:drawTextureAllPoint(texture, tlx, tly, trx, try, brx, bry, blx, bly, r,g,b,a)
    if self.javaObject ~= nil then
        self.javaObject:DrawTexture(texture, tlx, tly, trx, try, brx, bry, blx, bly, r,g,b,a);
    end
end

--************************************************************************--
--** ISUIElement:drawTextureScaled
--**
--************************************************************************--
function ISUIElement:drawTextureScaled(texture, x, y, w, h, a, r, g, b)
	if self.javaObject ~= nil then

		if r==nil then
			self.javaObject:DrawTextureScaled(texture, x, y, w, h, a);
		else
			self.javaObject:DrawTextureScaledColor(texture, x, y, w, h, r, g, b, a);
		end
	end
end

--************************************************************************--
--** ISUIElement:drawTextureScaledUniform
--**
--************************************************************************--
function ISUIElement:drawTextureScaledUniform(texture, x, y, scale, a, r, g, b)
	if self.javaObject ~= nil then

		if r==nil then
			self.javaObject:DrawTextureScaledUniform(texture, x, y, scale, 1, 1, 1, a);
		else
			self.javaObject:DrawTextureScaledUniform(texture, x, y, scale, r, g, b, a);
		end
	end
end

--************************************************************************--
--** ISUIElement:drawTextureScaledAspect
--**
--************************************************************************--
function ISUIElement:drawTextureScaledAspect(texture, x, y, w, h, a, r, g, b)
	if self.javaObject ~= nil then

		if r==nil then
			self.javaObject:DrawTextureScaledAspect(texture, x, y, w, h, 1, 1, 1, a);
		else
			self.javaObject:DrawTextureScaledAspect(texture, x, y, w, h, r, g, b, a);
		end
	end
end

--************************************************************************--
--** ISUIElement:drawTextureScaledAspect2
--**
--************************************************************************--
function ISUIElement:drawTextureScaledAspect2(texture, x, y, w, h, a, r, g, b)
	if self.javaObject ~= nil then

		if r==nil then
			self.javaObject:DrawTextureScaledAspect2(texture, x, y, w, h, 1, 1, 1, a);
		else
			self.javaObject:DrawTextureScaledAspect2(texture, x, y, w, h, r, g, b, a);
		end
	end
end

--************************************************************************--
--** ISUIElement:drawTexture
--**
--************************************************************************--
function ISUIElement:drawTexture(texture, x, y, a, r, g, b)
	if self.javaObject ~= nil then
		if r==nil then
			self.javaObject:DrawTexture(texture, x, y, a);
		else
			self.javaObject:DrawTextureColor(texture, x, y, r, g, b, a);
		end
	end
end

--************************************************************************--
--** ISUIElement:drawTextureTiledX
--**
--************************************************************************--
function ISUIElement:drawTextureTiledX(texture, x, y, w, h, r, g, b, a)
	if self.javaObject ~= nil then
		if not r then
			r,g,b,a = 1,1,1,1
		end
		self.javaObject:DrawTextureTiledX(texture, x, y, w, h, r, g, b, a);
	end
end

--************************************************************************--
--** ISUIElement:drawTextureTiledY
--**
--************************************************************************--
function ISUIElement:drawTextureTiledY(texture, x, y, w, h, r, g, b, a)
	if self.javaObject ~= nil then
		if not r then
			r,g,b,a = 1,1,1,1
		end
		self.javaObject:DrawTextureTiledY(texture, x, y, w, h, r, g, b, a);
	end
end

--************************************************************************--
--** ISUIElement:DrawTextureAngle
--**
--************************************************************************--
function ISUIElement:DrawTextureAngle(tex, centerX, centerY, angle)
    if self.javaObject ~= nil then
       self.javaObject:DrawTextureAngle(tex, centerX, centerY, angle);
    end
end

--************************************************************************--
--** ISUIElement:drawTextureStatic
--**
--************************************************************************--
function ISUIElement:drawTextureScaledStatic(texture, x, y, w, h, a, r, g, b)
	if self.javaObject ~= nil then

		if r==nil then
			self.javaObject:DrawTextureScaled(texture, x, y, w, h, a);
		else
			self.javaObject:DrawTextureScaledColor(texture, x, y, w, h, r, g, b, a);
		end
	end
end

--************************************************************************--
--** ISUIElement:drawTextureStatic
--**
--************************************************************************--
function ISUIElement:drawTextureStatic(texture, x, y, a, r, g, b)
	if self.javaObject ~= nil then
		if r==nil then
			self.javaObject:DrawTexture(texture, x, y, a);
		else
			self.javaObject:DrawTextureColor(texture, x-self:getXScroll(), y-self:getYScroll(), r, g, b, a);
		end
	end
end

--************************************************************************--
--** ISUIElement:drawRect
--**
--************************************************************************--
function ISUIElement:drawRect( x, y, w, h, a, r, g, b)
	if self.javaObject ~= nil then

		self.javaObject:DrawTextureScaledColor(nil,x, y, w, h, r, g, b, a);

	end
end

--************************************************************************--
--** ISUIElement:drawRectStatic
--**
--************************************************************************--
function ISUIElement:drawRectStatic( x, y, w, h, a, r, g, b)
	if self.javaObject ~= nil then

		self.javaObject:DrawTextureScaledColor(nil, x-self:getXScroll(), y-self:getYScroll(), w, h, r, g, b, a);

	end
end



--************************************************************************--
--** ISUIElement:drawRectBorder
--**
--************************************************************************--
function ISUIElement:drawRectBorderStatic( x, y, w, h, a, r, g, b)
	if self.javaObject ~= nil then

		self.javaObject:DrawTextureScaledColor(nil, x-self:getXScroll(), y-self:getYScroll(), 1, h, r, g, b, a);
		self.javaObject:DrawTextureScaledColor(nil, x-self:getXScroll()+1, y-self:getYScroll(), w-2, 1, r, g, b, a);
		self.javaObject:DrawTextureScaledColor(nil, x-self:getXScroll()+w-1, y-self:getYScroll(), 1, h, r, g, b, a);
		self.javaObject:DrawTextureScaledColor(nil, x-self:getXScroll()+1, y-self:getYScroll()+h-1, w-2, 1, r, g, b, a);

	end
end
--************************************************************************--
--** ISUIElement:drawRectBorder
--**
--************************************************************************--
function ISUIElement:drawRectBorder( x, y, w, h, a, r, g, b)
	if self.javaObject ~= nil then

		self.javaObject:DrawTextureScaledColor(nil, x, y, 1, h, r, g, b, a);
		self.javaObject:DrawTextureScaledColor(nil, x+1, y, w-2, 1, r, g, b, a);
		self.javaObject:DrawTextureScaledColor(nil, x+w-1, y, 1, h, r, g, b, a);
		self.javaObject:DrawTextureScaledColor(nil, x+1, y+h-1, w-2, 1, r, g, b, a);

	end
end

function ISUIElement:drawLine2( x, y, x2, y2, a, r, g, b)
	if self.javaObject ~= nil then
		self.javaObject:DrawTexture(nil, x, y, x2, y2, x2+1, y2, x, y+1, r, g, b, a);
	end
end


--************************************************************************--
--** ISUIElement:drawTextZoomed
--**
--************************************************************************--
function ISUIElement:drawTextZoomed(str, x, y, zoom, r, g, b, a, font)
    if self.javaObject ~= nil then
        if font ~= nil then
            self.javaObject:DrawText(font, str, x, y, zoom, r, g, b, a);
        else
            self.javaObject:DrawText(UIFont.Small, str, x, y, zoom, r, g, b, a);
        end
    end
end

--************************************************************************--
--** ISUIElement:drawTextUntrimmed
--**
--************************************************************************--
function ISUIElement:drawTextUntrimmed(str, x, y, r, g, b, a, font)
    if self.javaObject ~= nil then
        if font ~= nil then
            self.javaObject:DrawTextUntrimmed(font, str, x, y, r, g, b, a);
        else
            self.javaObject:DrawText(UIFont.Small, str, x, y, r, g, b, a);
        end
    end
end

--************************************************************************--
--** ISUIElement:drawTextCentre
--**
--************************************************************************--
function ISUIElement:drawTextCentre(str, x, y, r, g, b, a, font)
	if self.javaObject ~= nil then
		if font ~= nil then
			self.javaObject:DrawTextCentre(font, str, x, y, r, g, b, a);
		else
			self.javaObject:DrawTextCentre(UIFont.Small, str, x, y, r, g, b, a);
		end
	end
end

--************************************************************************--
--** ISUIElement:drawText
--**
--************************************************************************--
function ISUIElement:drawText(str, x, y, r, g, b, a, font)
	if self.javaObject ~= nil then
		if font ~= nil then
			self.javaObject:DrawText(font, str, x, y, r, g, b, a);
		else
			self.javaObject:DrawText(UIFont.Small, str, x, y, r, g, b, a);
		end
	end
end

--************************************************************************--
--** ISUIElement:drawTextRight
--**
--************************************************************************--
function ISUIElement:drawTextRight(str, x, y, r, g, b, a, font)

	if self.javaObject ~= nil and str ~= nil then
		if font ~= nil then
			self.javaObject:DrawTextRight(font, str, x, y, r, g, b, a);
		else
			self.javaObject:DrawTextRight(UIFont.Small, str, x, y, r, g, b, a);
		end
	end
end
--************************************************************************--
function ISUIElement:setAlwaysOnTop(b)
    if self.javaObject ~= nil then
        self.javaObject:setAlwaysOnTop(b);
    end
end

--


--************************************************************************--
--** ISUIElement:drawTextStatic
--**
--************************************************************************--
function ISUIElement:drawTextStatic(str, x, y, r, g, b, a, font)
	if self.javaObject ~= nil then
		if font ~= nil then
			self.javaObject:DrawText(font, str, x-self:getScrollX(), y-self:getScrollY(), r, g, b, a);
		else
			self.javaObject:DrawText(UIFont.Small, str, x-self:getScrollX(), y-self:getScrollY(), r, g, b, a);
		end
	end
end

--************************************************************************--
--** ISUIElement:drawTextCentreStatic
--**
--************************************************************************--
function ISUIElement:drawTextCentreStatic(str, x, y, r, g, b, a, font)
	if self.javaObject ~= nil then
		if font ~= nil then
			self.javaObject:DrawTextCentre(font, str, x-self:getScrollX(), y-self:getScrollY(), r, g, b, a);
		else
			self.javaObject:DrawTextCentre(UIFont.Small, str, x-self:getScrollX(), y-self:getScrollY(), r, g, b, a);
		end
	end
end

--************************************************************************--
--** ISUIElement:drawTextStatic
--**
--************************************************************************--
function ISUIElement:drawTextStatic(str, x, y, r, g, b, a, font)
	if self.javaObject ~= nil then
		if font ~= nil then
			self.javaObject:DrawText(font, str, x-self:getScrollX(), y-self:getScrollY(), r, g, b, a);
		else
			self.javaObject:DrawText(UIFont.Small, str, x-self:getScrollX(), y-self:getScrollY(), r, g, b, a);
		end
	end
end

--************************************************************************--
--** ISUIElement:drawTextRightStatic
--**
--************************************************************************--
function ISUIElement:drawTextRightStatic(str, x, y, r, g, b, a, font)
	if self.javaObject ~= nil then
		if font ~= nil then
			self.javaObject:DrawTextRight(font, str, x-self:getScrollX(), y-self:getScrollY(),r, g, b, a);
		else
			self.javaObject:DrawTextRight(UIFont.Small, str, x-self:getScrollX(), y-self:getScrollY(), r, g, b, a);
		end
	end
end

--************************************************************************--
--** ISUIElement:addToUIManager
--**
--************************************************************************--
function ISUIElement:addToUIManager()
	if self.javaObject == nil then
		self:instantiate();
	end

	UIManager.AddUI(self.javaObject);
end

--************************************************************************--
--** ISUIElement:removeFromUIManager
--**
--************************************************************************--
function ISUIElement:removeFromUIManager()
	if self.javaObject == nil then
		return;
	end

	UIManager.RemoveElement(self.javaObject);
	self.removed = true;
end
function ISUIElement:backMost()
    if self.javaObject == nil then
        self:instantiate();
    end

    self.javaObject:backMost();
end
--************************************************************************--
--** ISUIElement:addScrollBar
--**
--************************************************************************--
function ISUIElement:addScrollBars(addHorizontal)
	self.vscroll = ISScrollBar:new(self, true);
	self.vscroll:initialise();
	self:addChild(self.vscroll);
	if addHorizontal then
		self.hscroll = ISScrollBar:new(self, false)
		self.hscroll:initialise()
		self:addChild(self.hscroll)
	end
end

--************************************************************************--
--** ISUIElement:isVScrollBarVisible
--**
--************************************************************************--
function ISUIElement:isVScrollBarVisible()
	return self.vscroll and (self.vscroll:getHeight() < self:getScrollHeight())
end

--************************************************************************--
--** ISUIElement:getParent
--**
--************************************************************************--
function ISUIElement:getParent()
	return self.parent
end

--************************************************************************--
--** ISUIElement:getChildren
--**
--************************************************************************--
function ISUIElement:getChildren()
	return self.children;
end

--************************************************************************--
--** ISUIElement:addChild
--**
--************************************************************************--
function ISUIElement:addChild(otherElement)
	if self.javaObject == nil then
		self:instantiate();
	end
	if otherElement.javaObject == nil then
		otherElement:instantiate();
	end
	if self.children == nil then
		self.children = {}
		self.ID = ISUIElement.IDMax;
		ISUIElement.IDMax = ISUIElement.IDMax + 1;
	end
	if otherElement.children == nil then
		otherElement.children = {}
		otherElement.ID = ISUIElement.IDMax;
		ISUIElement.IDMax = ISUIElement.IDMax + 1;
	end
	self.children[otherElement.ID] = otherElement;
	----print("Adding child control to parent.");
	self.javaObject:AddChild(otherElement.javaObject);
	otherElement.parent = self;
end

--************************************************************************--
--** ISUIElement:removeChild
--**
--************************************************************************--
function ISUIElement:removeChild(otherElement)

	if self.javaObject == nil then
		return;
	end

	self.children[otherElement.ID] = nil;
--	--print("Attempting to remove: "..otherElement.javaObject);

	if(otherElement.javaObject ~= nil) then
	--	--print("Calling to remove: "..otherElement.javaObject);
		self.javaObject:RemoveChild(otherElement.javaObject);
	end
end

--************************************************************************--
--** ISUIElement:clearChildren
--**
--************************************************************************--
function ISUIElement:clearChildren()
	if self.javaObject == nil then
		return;
	end
	self.children = {}
	self.javaObject:ClearChildren();
end

--************************************************************************--
--** ISUIElement:onMouseWheel
--**
--************************************************************************--
function ISUIElement:onMouseWheel(del)
    return false;
end
--************************************************************************--
--** ISUIElement:onMouseUp
--**
--************************************************************************--
function ISUIElement:onMouseUp(x, y)
   if self.vscroll ~= nil then
      self.vscroll:onMouseUp(x,y)
   end
end

function ISUIElement:setOnMouseDoubleClick(target, onmousedblclick)
    self.onMouseDoubleClick = onmousedblclick;
    self.target = target;
end

--************************************************************************--
	--** ISUIElement:onRightMouseUpOutside
	--**
	--************************************************************************--
function ISUIElement:onRightMouseUpOutside(x, y)

end

--************************************************************************--
--** ISUIElement:onRightMouseDownOutside
--**
--************************************************************************--
function ISUIElement:onRightMouseDownOutside(x, y)

end
--************************************************************************--
--** ISUIElement:onMouseUpOutside
--**
--************************************************************************--
function ISUIElement:onMouseUpOutside(x, y)
   if self.vscroll ~= nil then
      self.vscroll:onMouseUp(x,y)
   end
end
--************************************************************************--
--** ISUIElement:onMouseDownOutside
--**
--************************************************************************--
function ISUIElement:onMouseDownOutside(x, y)

end

--************************************************************************--
--** ISUIElement:onMouseDown
--**
--************************************************************************--
function ISUIElement:onFocus(x, y)
   if self.parent == nil then
	  self:bringToTop();
   end
end

--************************************************************************--
--** ISUIElement:onRightMouseUp
--**
--************************************************************************--
function ISUIElement:bringToTop()
	if self.javaObject == nil then
		return;
	end

	self.javaObject:bringToTop();
end


--************************************************************************--
--** ISUIElement:onRightMouseUp
--**
--************************************************************************--
function ISUIElement:onRightMouseUp(x, y)

end

--************************************************************************--
--** ISUIElement:onRightMouseDown
--**
--************************************************************************--
function ISUIElement:onRightMouseDown(x, y)

end

--************************************************************************--
--** ISUIElement:onMouseMove
--**
--************************************************************************--
function ISUIElement:onMouseMove(dx, dy)

end

--************************************************************************--
--** ISUIElement:onMouseMoveOutside
--**
--************************************************************************--
function ISUIElement:onMouseMoveOutside(dx, dy)

end

--************************************************************************--
--** ISUIElement:containsPoint
--**
--************************************************************************--
function ISUIElement:containsPoint(x, y)
	if x >= self.x and x < self.x + self.width and y >= self.y and y < self.y + self.height then
		return true;
	end

	return false;
end

--************************************************************************--
--** ISUIElement:containsPoint
--**
--************************************************************************--
function ISUIElement:containsPointLocal(x, y)
	if x >= 0 and x < self.width and y >= 0 and y < self.height then
		return true;
	end

	return false;
end



function ISUIElement:shrinkY(y)
	y = 1.0 - y;
	self.height = self:getHeight();
	local newY = self:getY();

	newY = newY + ((self.height*y)/2);

	self:setHeight(self:getHeight() - (self.height*y));
	self:setY(newY);
end

function ISUIElement:shrinkX(x)

	x = 1.0 - x;
	self.width = self:getWidth();
	local newX = self:getX();

	newX = newX + ((self.width*x) / 2);

	self:setWidth(self:getWidth() - (self.width*x));
	self:setX(newX);
end

--************************************************************************--
--** ISUIElement:update
--**
--************************************************************************--
function ISUIElement:update()

end

--************************************************************************--
--** ISUIElement:prerender
--**
--************************************************************************--
function ISUIElement:prerender()

end

--************************************************************************--
--** ISUIElement:render
--**
--************************************************************************--
function ISUIElement:render()

end

function ISUIElement:setScrollWidth(w)
	self.scrollwidth = w;
	self:updateScrollbars();
end

function ISUIElement:setScrollHeight(h)
	if self.javaObject == nil then
		return;
	end
	self.javaObject:setScrollHeight(h)
	self:updateScrollbars();
end

function ISUIElement:getScrollWidth()
	return self.scrollwidth;
end

function ISUIElement:getScrollHeight()
	if self.javaObject == nil then
		return 0
	end
	return self.javaObject:getScrollHeight()
end

function ISUIElement:setScrollChildren(b)
    if self.javaObject == nil then
        return;
    end

    self.javaObject:setScrollChildren(b);

    self:updateScrollbars();
end

function ISUIElement:getScrollChildren()
    if self.javaObject == nil then
        return;
    end

    return self.javaObject:getScrollChildren();

end


function ISUIElement:setScrollWithParent(b)
    if self.javaObject == nil then
        return;
    end

    self.javaObject:setScrollWithParent(b);

    self:updateScrollbars();
end

function ISUIElement:getScrollWithParent()
    if self.javaObject == nil then
        return;
    end

    return self.javaObject:getScrollWithParent();

end


function ISUIElement:setYScroll(y)
	if self.javaObject == nil then
		return;
	end

    if -y > self:getScrollHeight() - (self:getScrollAreaHeight()) then
        y = -(self:getScrollHeight() - (self:getScrollAreaHeight()));
    end
    if -y < 0 then
        y = 0;
    end
   -- print("setting scroll to "..y)
	self.javaObject:setYScroll(y);

    self:updateScrollbars();
end

function ISUIElement:updateScrollbars()

    if self.vscroll ~= nil then
        self.vscroll:updatePos();
    end

    local y = self.javaObject:getYScroll();

    if -y > self:getScrollHeight() - (self:getScrollAreaHeight()) then
        y = -(self:getScrollHeight() - (self:getScrollAreaHeight()));
    end
    if -y < 0 then
        y = 0;
    end

    self.javaObject:setYScroll(y);

end

function ISUIElement:setXScroll(x)
	if self.javaObject == nil then
		return;
	end

	self.javaObject:setXScroll(x);
end

function ISUIElement:getScrollAreaWidth()
	if self:isVScrollBarVisible() then
		return self:getWidth() - self.vscroll:getWidth()
	end
	return self:getWidth()
end

function ISUIElement:getScrollAreaHeight()
	if self.hscroll then
		return self:getHeight() - self.hscroll:getHeight()
	end
	return self:getHeight()
end

function ISUIElement:wrapInCollapsableWindow(title, resizable, subClass)

	local titleBarHeight = ISCollapsableWindow.TitleBarHeight()
	local resizeWidgetHeight = (resizable == nil or resizable == true) and 8 or 0
	subClass = subClass or ISCollapsableWindow
	local o = subClass:new(self.x, self.y, self.width, self.height + titleBarHeight + resizeWidgetHeight);
	o.title = title;
	o:setResizable(resizable == nil or resizable == true)
	o:initialise();
	o:instantiate();
	if self.javaObject ~= nil then
		self:removeFromUIManager();
	end
	o:addChild(self);
	-- must setX/Y after addChild(), or getKeepOnScreen() code may restrict x/y
	self:setX(0);
	self:setY(o:titleBarHeight());
	o.nested = self;
	return o;
end

function ISUIElement:isRemoved()
	return self.removed;
end

function ISUIElement:setRemoved(bremove)
	self.removed = bremove;
end

function ISUIElement:setUIName(name)
	if self.javaObject == nil then
		self:instantiate()
	end
	self.javaObject:setUIName(name)
end

function ISUIElement:getUIName(name)
	if self.javaObject == nil then
		self:instantiate()
	end
	return self.javaObject:getUIName()
end

function ISUIElement:toString()
	local name = self:getUIName()
	if name == "" then return self.Type..'('..tostring(self)..')' end
	return name
end

function ISUIElement:drawProgressBar(x, y, w, h, f, fg)
    if f < 0.0 then f = 0.0 end
    if f > 1.0 then f = 1.0 end
    local done = math.floor(w * f)
    if f > 0 then done = math.max(done, 1) end
    self:drawRect(x, y, done, h, fg.a, fg.r, fg.g, fg.b);
    local bg = {r=0.15, g=0.15, b=0.15, a=1.0};
    self:drawRect(x + done, y, w - done, h, bg.a, bg.r, bg.g, bg.b);
end

function ISUIElement:stayOnSplitScreen(playerNum)
    if getNumActivePlayers() > 1 then
        local sL = getPlayerScreenLeft(playerNum);
        local sT = getPlayerScreenTop(playerNum);
        local sW = getPlayerScreenWidth(playerNum);
        local sH = getPlayerScreenHeight(playerNum);
        if self:getX() < sL then self:setX(sL); end
        if self:getY() < sT then self:setY(sT); end
        if self:getX()+self:getWidth() > sL+sW then self:setX((sL+sW)-self:getWidth()); end
        if self:getY()+self:getHeight() > sT+sH then self:setY((sT+sH)-self:getHeight()); end
    end
end

function ISUIElement:setWantKeyEvents(want)
	if self.javaObject == nil then
		self.wantKeyEvents = want
	else
		self.wantKeyEvents = nil
		self.javaObject:setWantKeyEvents(want)
	end
end

function ISUIElement:setForceCursorVisible(force)
	if self.javaObject == nil then
		self.forceCursorVisible = force
	else
		self.forceCursorVisible = nil
		self.javaObject:setForceCursorVisible(force)
	end
end

--************************************************************************--
--** ISUIElement:new
--**
--************************************************************************--
function ISUIElement:new (x, y, width, height)
   local o = {}
   setmetatable(o, self)
   self.__index = self

   local maxY = getCore():getScreenHeight();
   local maxX = getCore():getScreenWidth();

   o.x = math.max(0, math.min(x, maxX - width));
   o.y = math.max(0, math.min(y, maxY - height));
   o.width = width;
   o.height = height;
   o.anchorLeft = true;
   o.anchorRight = false;
   o.anchorTop = true;
   o.anchorBottom = false;
   o.dock = "none";
   o.minimumWidth = 0;
   o.minimumHeight = 0;
   o.scrollwidth = 0;
   o.removed = false;
   return o
end
