require "ISUI/ISPanel"

---@class ISButton : ISPanel
ISButton = ISPanel:derive("ISButton");

--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISButton:initialise()
	ISPanel.initialise(self);
end

--************************************************************************--
--** ISButton:onMouseMove
--**
--************************************************************************--
function ISButton:onMouseMove(dx, dy)
	self.mouseOver = self:isMouseOver();
end

--************************************************************************--
--** ISButton:onMouseMoveOutside
--**
--************************************************************************--
function ISButton:onMouseMoveOutside(dx, dy)
	self.mouseOver = false;
	if self.onmouseoutfunction then
		self.onmouseoutfunction(self.target, self, dx, dy);
	end
end

function ISButton:setJoypadFocused(focused)
    self.joypadFocused = focused;
end

--************************************************************************--
--** ISButton:onMouseUp
--**
--************************************************************************--
function ISButton:onMouseUp(x, y)

    if not self:getIsVisible() then
        return;
    end
    local process = false;
    if self.pressed == true then
        process = true;
    end
    self.pressed = false;
     if self.onclick == nil then
        return;
    end
    if self.enable and (process or self.allowMouseUpProcessing) then
        getSoundManager():playUISound(self.sounds.activate)
        self.onclick(self.target, self, self.onClickArgs[1], self.onClickArgs[2], self.onClickArgs[3], self.onClickArgs[4]);
	    --print(self.title);
    end

end

function ISButton:onMouseUpOutside(x, y)

    self.pressed = false;
end
--************************************************************************--
--** ISButton:onMouseDown
--**
--************************************************************************--
function ISButton:onMouseDown(x, y)
	if not self:getIsVisible() then
		return;
	end
    self.pressed = true;
    if self.onmousedown == nil or not self.enable then
		return;
    end
	self.onmousedown(self.target, self, x, y);
end

function ISButton:onMouseDoubleClick(x, y)
	return self:onMouseDown(x, y)
end

function ISButton:forceClick()
    if not self:getIsVisible() or not self.enable then
        return;
    end
    if self.repeatWhilePressedFunc then
		return self.repeatWhilePressedFunc(self.target, self)
    end
    getSoundManager():playUISound(self.sounds.activate)
    self.onclick(self.target, self, self.onClickArgs[1], self.onClickArgs[2], self.onClickArgs[3], self.onClickArgs[4]);
end

function ISButton:setJoypadButton(texture)
    self.isJoypad = true;
    self.joypadTexture = texture;
end

function ISButton:clearJoypadButton()
    self.isJoypad = false;
    self.joypadTexture = nil;
end

--************************************************************************--
--** ISButton:render
--**
--************************************************************************--
function ISButton:prerender()
	if self.displayBackground and not self.isJoypad then
		-- Checking self:isMouseOver() in case the button is becoming visible again.
		self.fade:setFadeIn((self.mouseOver and self:isMouseOver()) and self.enable or self.joypadFocused or false)
		self.fade:update()
		local f = self.fade:fraction()
		local fill = self.backgroundColorMouseOver
		if self.pressed then
			self.backgroundColorPressed = self.backgroundColorPressed or {}
			self.backgroundColorPressed.r = self.backgroundColorMouseOver.r * 0.5
			self.backgroundColorPressed.g = self.backgroundColorMouseOver.g * 0.5
			self.backgroundColorPressed.b = self.backgroundColorMouseOver.b * 0.5
			self.backgroundColorPressed.a = self.backgroundColorMouseOver.a
			fill = self.backgroundColorPressed
		end
		self:drawRect(0, 0, self.width, self.height,
			fill.a * f + self.backgroundColor.a * (1 - f),
			fill.r * f + self.backgroundColor.r * (1 - f),
			fill.g * f + self.backgroundColor.g * (1 - f),
			fill.b * f + self.backgroundColor.b * (1 - f));
        self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    end
	if self.joypadFocused then
		self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	end
	if self.displayBackground and self.blinkBG and not self.mouseOver then
		if not self.blinkBGAlpha then
			self.blinkBGAlpha = 1
			self.blinkBGAlphaIncrease = false
		end
		if not self.blinkBGAlphaIncrease then
			self.blinkBGAlpha = self.blinkBGAlpha - 0.1 * (UIManager.getMillisSinceLastRender() / 33.3)
			if self.blinkBGAlpha < 0 then
				self.blinkBGAlpha = 0;
				self.blinkBGAlphaIncrease = true
			end
		else
			self.blinkBGAlpha = self.blinkBGAlpha + 0.1 * (UIManager.getMillisSinceLastRender() / 33.3)
			if self.blinkBGAlpha > 1 then
				self.blinkBGAlpha = 1
				self.blinkBGAlphaIncrease = false
			end
		end
		local f = self.blinkBGAlpha
		self:drawRect(0, 0, self.width, self.height,
			self.backgroundColorMouseOver.a * f + self.backgroundColor.a * (1 - f),
			self.backgroundColorMouseOver.r * f + self.backgroundColor.r * (1 - f),
			self.backgroundColorMouseOver.g * f + self.backgroundColor.g * (1 - f),
			self.backgroundColorMouseOver.b * f + self.backgroundColor.b * (1 - f));
	end
	self:updateTooltip()
end

function ISButton:setImage(image)
	self.image = image;
end

function ISButton:forceImageSize(width, height)
    self.forcedWidthImage = width;
    self.forcedHeightImage = height;
end

function ISButton:setOverlayText(text)
	self.overlayText = text;
end

function ISButton:render()
--    print("btn:render")
	if self.image ~= nil then
--        print("btn:image")
        local alpha = self.textureColor.a;
        if self.blinkImage then
            if not self.blinkImageAlpha then
                self.blinkImageAlpha = 1;
                self.blinkImageAlphaIncrease = false;
            end

            if not self.blinkImageAlphaIncrease then
                self.blinkImageAlpha = self.blinkImageAlpha - 0.1 * (UIManager.getMillisSinceLastRender() / 33.3);
                if self.blinkImageAlpha < 0 then
                    self.blinkImageAlpha = 0;
                    self.blinkImageAlphaIncrease = true;
                end
            else
                self.blinkImageAlpha = self.blinkImageAlpha + 0.1 * (UIManager.getMillisSinceLastRender() / 33.3);
                if self.blinkImageAlpha > 1 then
                    self.blinkImageAlpha = 1;
                    self.blinkImageAlphaIncrease = false;
                end
            end

            alpha = self.blinkImageAlpha;
        end
        if self.forcedWidthImage and self.forcedHeightImage then
            self:drawTextureScaledAspect(self.image, (self.width / 2) - (self.forcedWidthImage / 2), (self.height / 2) - (self.forcedHeightImage / 2),self.forcedWidthImage,self.forcedHeightImage, alpha, self.textureColor.r, self.textureColor.g, self.textureColor.b);
        elseif self.image:getWidthOrig() <= self.width and self.image:getHeightOrig() <= self.height then
            self:drawTexture(self.image, (self.width / 2) - (self.image:getWidthOrig() / 2), (self.height / 2) - (self.image:getHeightOrig() / 2), alpha, self.textureColor.r, self.textureColor.g, self.textureColor.b);
        else
            self:drawTextureScaledAspect(self.image, 0, 0, self.width, self.height, alpha, self.textureColor.r, self.textureColor.g, self.textureColor.b);
        end
	end
	local textW = getTextManager():MeasureStringX(self.font, self.title)
	local height = getTextManager():MeasureStringY(self.font, self.title)
	local x = self.width / 2 - textW / 2;
	if self.isJoypad and self.joypadTexture then
		local texWH = self.joypadTextureWH
		local texX = x - 5 - texWH
		local texY = self.height / 2 - 20 / 2
		texX = math.max(5, texX)
		x = texX + texWH + 5
		self:drawTextureScaled(self.joypadTexture,texX,texY,texWH,texWH,1,1,1,1);
	end
	if self.enable then
		self:drawText(self.title, x, (self.height / 2) - (height/2) + self.yoffset, self.textColor.r, self.textColor.g, self.textColor.b, self.textColor.a, self.font);
	elseif self.displayBackground and not self.isJoypad and self.joypadFocused then
		self:drawText(self.title, x, (self.height / 2) - (height/2) + self.yoffset, 0, 0, 0, 1, self.font);
	else
		self:drawText(self.title, x, (self.height / 2) - (height/2) + self.yoffset, 0.3, 0.3, 0.3, 1, self.font);
	end
	if self.overlayText then
		self:drawTextRight(self.overlayText, self.width, self.height - 10, 1, 1, 1, 0.5, UIFont.Small);
	end
	-- call the onMouseOverFunction
	if (self.mouseOver and self.onmouseover) then
		self.onmouseover(self.target, self, x, y);
    end

    if self.textureOverride then
        self:drawTexture(self.textureOverride, (self.width /2) - (self.textureOverride:getWidth() / 2), (self.height /2) - (self.textureOverride:getHeight() / 2), 1, 1, 1, 1);
    end

    if false and self.mouseOver and self.tooltip then
        self:drawRect(self:getMouseX() + 23, self:getMouseY() + 23, getTextManager():MeasureStringX(UIFont.Small, self.tooltip) + 24, 32+24, 0.7, 0.05, 0.05, 0.05);
        self:drawRectBorder(self:getMouseX()  + 23, self:getMouseY() + 23, getTextManager():MeasureStringX(UIFont.Small, self.tooltip) + 24, 32+24, 0.5, 0.9, 0.9, 1);
        self:drawText(self.tooltip, self:getMouseX()  + 23 + 12, self:getMouseY() + 23 + 12, 1,1,1,1);
    end
end

function ISButton:setFont(font)
	self.font = font;
end

function ISButton:getTitle()
	return self.title;
end

function ISButton:setTitle(title)
	self.title = title;
end

function ISButton:setOnMouseOverFunction(onmouseover)
	self.onmouseover = onmouseover;
end

function ISButton:setOnMouseOutFunction(onmouseout)
	self.onmouseoutfunction = onmouseout;
end

function ISButton:setDisplayBackground(background)
	self.displayBackground = background;
end

function ISButton:update()
	ISUIElement.update(self)
	if self.enable and self.pressed and self.target and self.repeatWhilePressedFunc then
		if not self.pressedTime then
			self.pressedTime = getTimestampMs()
			self.repeatWhilePressedFunc(self.target, self)
		else
			local ms = getTimestampMs()
			if ms - self.pressedTime > 500 then
				self.pressedTime = ms
				self.repeatWhilePressedFunc(self.target, self)
			end
		end
	else
		self.pressedTime = nil
	end
end

function ISButton:updateTooltip()
	if self:isMouseOver() and self.tooltip then
		local text = self.tooltip
		if not self.tooltipUI then
			self.tooltipUI = ISToolTip:new()
			self.tooltipUI:setOwner(self)
			self.tooltipUI:setVisible(false)
			self.tooltipUI:setAlwaysOnTop(true)
		end
		if not self.tooltipUI:getIsVisible() then
			if string.contains(self.tooltip, "\n") then
				self.tooltipUI.maxLineWidth = 1000 -- don't wrap the lines
			else
				self.tooltipUI.maxLineWidth = 300
			end
			self.tooltipUI:addToUIManager()
			self.tooltipUI:setVisible(true)
		end
		self.tooltipUI.description = text
		self.tooltipUI:setDesiredPosition(getMouseX(), self:getAbsoluteY() + self:getHeight() + 8)
	else
		if self.tooltipUI and self.tooltipUI:getIsVisible() then
			self.tooltipUI:setVisible(false)
			self.tooltipUI:removeFromUIManager()
		end
    end
end

function ISButton:setRepeatWhilePressed(func)
	self.repeatWhilePressedFunc = func
end

function ISButton:setBackgroundRGBA(r, g, b, a)
	self.backgroundColor.r = r
	self.backgroundColor.g = g
	self.backgroundColor.b = b
	self.backgroundColor.a = a
end

function ISButton:setBackgroundColorMouseOverRGBA(r, g, b, a)
	self.backgroundColorMouseOver.r = r
	self.backgroundColorMouseOver.g = g
	self.backgroundColorMouseOver.b = b
	self.backgroundColorMouseOver.a = a
end

function ISButton:setBorderRGBA(r, g, b, a)
	self.borderColor.r = r
	self.borderColor.g = g
	self.borderColor.b = b
	self.borderColor.a = a
end

function ISButton:setTextureRGBA(r, g, b, a)
	self.textureColor.r = r
	self.textureColor.g = g
	self.textureColor.b = b
	self.textureColor.a = a
end

function ISButton:setEnable(bEnabled)
	self.enable = bEnabled;
	if not self.borderColorEnabled then
		self.borderColorEnabled = { r = self.borderColor.r, g = self.borderColor.g, b = self.borderColor.b, a = self.borderColor.a }
	end
	if bEnabled then
		self:setTextureRGBA(1, 1, 1, 1)
		self:setBorderRGBA(
			self.borderColorEnabled.r,
			self.borderColorEnabled.g,
			self.borderColorEnabled.b,
			self.borderColorEnabled.a)
	else
		self:setTextureRGBA(0.3, 0.3, 0.3, 1.0)
		self:setBorderRGBA(0.7, 0.1, 0.1, 0.7)
	end
end

function ISButton:isEnabled()
	return self.enable;
end

function ISButton:setTooltip(tooltip)
    self.tooltip = tooltip;
end

function ISButton:setWidthToTitle(minWidth, isJoypad)
	local width = getTextManager():MeasureStringX(self.font, self.title) + 10
	if isJoypad then
		width = width + 5 + self.joypadTextureWH
	end
	width = math.max(width, minWidth or 0)
	if width ~= self.width then
		self:setWidth(width)
	end
end

function ISButton:setOnClick(func, arg1, arg2, arg3, arg4)
	self.onclick = func
	self.onClickArgs = { arg1, arg2, arg3, arg3 }
end

function ISButton:setSound(which, soundName)
	self.sounds[which] = soundName
end

--************************************************************************--
--** ISButton:new
--**
--************************************************************************--
function ISButton:new (x, y, width, height, title, clicktarget, onclick, onmousedown, allowMouseUpProcessing)

	local o = {}
	--o.data = {}
	o = ISPanel:new(x, y, width, height);
	setmetatable(o, self)
    self.__index = self
	o.x = x;
	o.y = y;
	o.font = UIFont.Small;
	o.borderColor = {r=0.7, g=0.7, b=0.7, a=1};
	o.backgroundColor = {r=0, g=0, b=0, a=1.0};
	o.backgroundColorMouseOver = {r=0.3, g=0.3, b=0.3, a=1.0};
    o.textureColor = {r=1.0, g=1.0, b=1.0, a=1.0};
    o.textColor = {r=1.0, g=1.0, b=1.0, a=1.0};
    if width < (getTextManager():MeasureStringX(UIFont.Small, title) + 10) then
        width = getTextManager():MeasureStringX(UIFont.Small, title) + 10;
    end
    o.width = width;
    o.height = height;
	o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;
	o.mouseOver = false;
	o.displayBackground = true;
	o.title = title;
	o.onclick = onclick;
	o.onClickArgs = {}
	o.target = clicktarget;
	o.onmousedown = onmousedown;
	o.enable = true;
    o.tooltip = nil;
    o.isButton = true;
    o.allowMouseUpProcessing = allowMouseUpProcessing;
    o.yoffset = 0;
    o.fade = UITransition.new()
    o.joypadTextureWH = 20
    o.sounds = {}
    o.sounds.activate = "UIActivateButton"
   return o
end
