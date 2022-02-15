--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanel"

---@class ISVolumeControl : ISPanel
ISVolumeControl = ISPanel:derive("ISVolumeControl")

function ISVolumeControl:onMouseDown(x, y)
	local volume = self:getVolumeAtX(self:getMouseX())
	self:setVolume(volume)
	self.dragging = true
	self:setCapture(true)
end

function ISVolumeControl:onMouseUp(x, y)
	self.dragging = false
	self:setCapture(false)
end

function ISVolumeControl:onMouseUpOutside(x, y)
	self.dragging = false
	self:setCapture(false)
end

function ISVolumeControl:onMouseMove(dx, dy)
	if self.dragging then
		local volume = self:getVolumeAtX(self:getMouseX())
		self:setVolume(volume)
	end
end

function ISVolumeControl:getVolumeAtX(x)
	local padX = 8
	local oneTenth = math.floor((self:getWidth() - padX * 2) / 10)
	padX = padX + (self:getWidth() - padX * 2 - oneTenth * 10) / 2
	local volume = math.floor(((x - padX) + oneTenth / 2) / oneTenth)
	if volume < 0 then return 0 end
	if volume > 10 then return 10 end
	return volume
end

function ISVolumeControl:prerender()
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

function ISVolumeControl:render()
	self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b)
	local alpha = math.min(self.borderColor.a + 0.2 * self.fade:fraction(), 1.0)
	self:drawRectBorder(0, 0, self.width, self.height, alpha, self.borderColor.r, self.borderColor.g, self.borderColor.b)

	local padX = 8
	local oneTenth = math.floor((self:getWidth() - padX * 2) / 10)
	local sliderWidth = 16
	local sliderPadX = 1
	local sliderPadY = 4
	padX = padX + (self:getWidth() - padX * 2 - oneTenth * 10) / 2
	local sliderX = padX + oneTenth * self.volume - sliderWidth / 2
	local rgb1 = 0.1 + 0.1 * self.fade:fraction()
	local rgb2 = 0.3 + 0.1 * self.fade:fraction()
	self:drawRect(2, sliderPadY, padX - sliderPadX - 2, self:getHeight() - sliderPadY * 2,
		1, rgb2, rgb2, rgb2)
	for i=1,10 do
		local rgb = (i <= self.volume) and rgb2 or rgb1
		self:drawRect(padX + (i-1) * oneTenth + sliderPadX, sliderPadY,
			oneTenth - sliderPadX * 2, self:getHeight() - sliderPadY * 2,
			1, rgb, rgb, rgb)
	end

	local x = padX + oneTenth * 10 + sliderPadX
	self:drawRect(x, sliderPadY, self:getWidth() - x - 2, self:getHeight() - sliderPadY * 2,
		1, rgb1, rgb1, rgb1)

	self:drawRect(sliderX, 2, sliderWidth, self:getHeight() - 2 * 2, 1.0, 0.5, 0.5, 0.5)

	self:drawRect(sliderX, 2, sliderWidth, 1, 1.0, 0.75, 0.75, 0.75)
	self:drawRect(sliderX + sliderWidth - 1, 2, 1, self:getHeight() - 2 * 2, 1.0, 0.25, 0.25, 0.25)
	self:drawRect(sliderX, 2, 1, self:getHeight() - 2 * 2, 1.0, 0.75, 0.75, 0.75)
	self:drawRect(sliderX, self:getHeight() - 2 - 1, sliderWidth, 1, 1.0, 0.25, 0.25, 0.25)
end

function ISVolumeControl:getVolume()
	return self.volume
end

function ISVolumeControl:setVolume(volume)
	if volume >= 0 and volume <= 10 and volume ~= self.volume then
		self.volume = volume
		if self.targetFunc then
			self.targetFunc(self.target, self, self.volume)
		end
	end
end

function ISVolumeControl:setJoypadFocused(focused)
	self.joypadFocused = focused;
end

function ISVolumeControl:onJoypadDirLeft(joypadData)
	self:setVolume(self.volume - 1)
end

function ISVolumeControl:onJoypadDirRight(joypadData)
	self:setVolume(self.volume + 1)
end

function ISVolumeControl:new(x, y, width, height, target, targetFunc)
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
