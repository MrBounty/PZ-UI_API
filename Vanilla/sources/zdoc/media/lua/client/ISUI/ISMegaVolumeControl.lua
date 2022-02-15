--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanel"

---@class ISMegaVolumeControl : ISPanel
ISMegaVolumeControl = ISPanel:derive("ISMegaVolumeControl")

function ISMegaVolumeControl:onMouseDown(x, y)
	local volume = self:getVolumeAtX(self:getMouseX())
	self:setVolume(volume)
	self.dragging = true
	self:setCapture(true)
end

function ISMegaVolumeControl:onMouseUp(x, y)
	self.dragging = false
	self:setCapture(false)
end

function ISMegaVolumeControl:onMouseUpOutside(x, y)
	self.dragging = false
	self:setCapture(false)
end

function ISMegaVolumeControl:onMouseMove(dx, dy)
	if self.dragging then
		local volume = self:getVolumeAtX(self:getMouseX())
		self:setVolume(volume)
	end
end

function ISMegaVolumeControl:getVolumeAtX(x)
	local padX = 8
	local oneTenth = math.floor((self:getWidth() - padX * 2) / 11)
	padX = padX + (self:getWidth() - padX * 2 - oneTenth * 11) / 2
	local volume = math.floor(((x - padX) + oneTenth / 2) / oneTenth)
	if volume < 0 then return 0 end
	if volume > 11 then return 11 end
	return volume
end

function ISMegaVolumeControl:prerender()
	ISPanel.prerender(self)

	self.fade:setFadeIn(self.joypadFocused or self.dragging or self:isMouseOver())
	self.fade:update()

	if self:isMouseOver() and self.tooltip then
		local text = self.tooltip
		if not self.tooltipUI then
			self.tooltipUI = ISToolTip:new()
			self.tooltipUI:setOwner(self)
			self.tooltipUI:setVisible(false)
			self.tooltipUI:setAlwaysOnTop(true)
		end
		if not self.tooltipUI:getIsVisible() then
			self.tooltipUI:addToUIManager()
			self.tooltipUI:setVisible(true)
		end
		self.tooltipUI.description = self.tooltip
		self.tooltipUI:setX(self:getMouseX() + 23)
		self.tooltipUI:setY(self:getMouseY() + 23)
	else
		if self.tooltipUI and self.tooltipUI:getIsVisible() then
			self.tooltipUI:setVisible(false)
			self.tooltipUI:removeFromUIManager()
		end
    end
end

function ISMegaVolumeControl:render()
	self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b)
	local alpha = math.min(self.borderColor.a + 0.2 * self.fade:fraction(), 1.0)
	self:drawRectBorder(0, 0, self.width, self.height, alpha, self.borderColor.r, self.borderColor.g, self.borderColor.b)

	local padX = 8
	local oneTenth = math.floor((self:getWidth() - padX * 2) / 11)
	local sliderWidth = 16
	local sliderPadX = 1
	local sliderPadY = 4
	padX = padX + (self:getWidth() - padX * 2 - oneTenth * 11) / 2
	local sliderX = padX + oneTenth * self.volume - sliderWidth / 2
	local rgb1 = 0.1 + 0.1 * self.fade:fraction()
	local rgb2 = 0.3 + 0.1 * self.fade:fraction()
	self:drawRect(2, sliderPadY, padX - sliderPadX - 2, self:getHeight() - sliderPadY * 2,
		1, rgb2, rgb2, rgb2)
	for i=1,11 do
		local rgb = (i <= self.volume) and rgb2 or rgb1
		local eleven_vol_red = 0.0
		if ((self.volume > 10) and (i > 10)) then
			eleven_vol_red =  0.5 
		end
		self:drawRect(padX + (i-1) * oneTenth + sliderPadX, sliderPadY,
			oneTenth - sliderPadX * 2, self:getHeight() - sliderPadY * 2,
			1, rgb+eleven_vol_red, rgb, rgb)
	end

	local x = padX + oneTenth * 11 + sliderPadX
	self:drawRect(x, sliderPadY, self:getWidth() - x - 2, self:getHeight() - sliderPadY * 2,
		1, rgb1, rgb1, rgb1)

	self:drawRect(sliderX, 2, sliderWidth, self:getHeight() - 2 * 2, 1.0, 0.5, 0.5, 0.5)

	self:drawRect(sliderX, 2, sliderWidth, 1, 1.0, 0.75, 0.75, 0.75)
	self:drawRect(sliderX + sliderWidth - 1, 2, 1, self:getHeight() - 2 * 2, 1.0, 0.25, 0.25, 0.25)
	self:drawRect(sliderX, 2, 1, self:getHeight() - 2 * 2, 1.0, 0.75, 0.75, 0.75)
	self:drawRect(sliderX, self:getHeight() - 2 - 1, sliderWidth, 1, 1.0, 0.25, 0.25, 0.25)
end

function ISMegaVolumeControl:getVolume()
	return self.volume
end

function ISMegaVolumeControl:setVolume(volume)
	if volume >= 0 and volume <= 11 and volume ~= self.volume then
		self.volume = volume
		if self.targetFunc then
			self.targetFunc(self.target, self, self.volume)
		end
	end
end

function ISMegaVolumeControl:setJoypadFocused(focused)
	self.joypadFocused = focused;
end

function ISMegaVolumeControl:onJoypadDirLeft(joypadData)
	self:setVolume(self.volume - 1)
end

function ISMegaVolumeControl:onJoypadDirRight(joypadData)
	self:setVolume(self.volume + 1)
end

function ISMegaVolumeControl:new(x, y, width, height, target, targetFunc)
	local o = ISPanel:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.backgroundColor = {r=0, g=0, b=0, a=1}
	o.borderColor = {r=1, g=1, b=1, a=0.5}
	o.volume = 0
	o.target = target
	o.targetFunc = targetFunc
	o.fade = UITransition.new()
	o.isSlider = true
	return o
end
