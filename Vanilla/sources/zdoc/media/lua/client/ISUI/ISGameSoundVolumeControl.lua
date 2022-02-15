--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanel"

---@class ISGameSoundVolumeControl : ISPanel
ISGameSoundVolumeControl = ISPanel:derive("ISGameSoundVolumeControl")

function ISGameSoundVolumeControl:instantiate()
	ISPanel.instantiate(self)
	self.javaObject:setConsumeMouseEvents(false)
end

function ISGameSoundVolumeControl:onMouseDown(x, y)
	local volume = self:getVolumeAtX(self:getMouseX())
	self:setVolume(volume / 100)
	self.dragging = true
	self:setCapture(true)
	ISGameSoundVolumeControl.capture = self
end

function ISGameSoundVolumeControl:onMouseUp(x, y)
	self.dragging = false
	self:setCapture(false)
	ISGameSoundVolumeControl.capture = nil
end

function ISGameSoundVolumeControl:onMouseUpOutside(x, y)
	self.dragging = false
	self:setCapture(false)
	ISGameSoundVolumeControl.capture = nil
end

function ISGameSoundVolumeControl:onMouseMove(dx, dy)
	if self.dragging then
		local volume = self:getVolumeAtX(self:getMouseX())
		self:setVolume(volume / 100)
	end
end

function ISGameSoundVolumeControl:getVolumeAtX(x)
	local padX = 8
	local oneTenth = math.floor((self:getWidth() - padX * 2) / (200 / 5))
	padX = padX + (self:getWidth() - padX * 2 - oneTenth * (200 / 5)) / 2
	local volume = math.floor(((x - padX) + oneTenth / 2) / oneTenth)
	volume = volume * 5
	if volume < 0 then return 0 end
	if volume > 200 then return 200 end
	return volume
end

function ISGameSoundVolumeControl:prerender()
	ISPanel.prerender(self)

	local mouseOver = self:isMouseOver()
	if ISGameSoundVolumeControl.capture and ISGameSoundVolumeControl.capture ~= self then
		mouseOver = false
	end

	self.fade:setFadeIn(self.joypadFocused or self.dragging or mouseOver)
	self.fade:update()

	self.tooltip = (mouseOver or self.dragging or self.joypadFocused) and self:getTooltip() or nil

	if self.tooltip then
		local text = self.tooltip
		if not self.tooltipUI then
			self.tooltipUI = ISToolTip:new()
			self.tooltipUI:setOwner(self)
			self.tooltipUI:setVisible(false)
			self.tooltipUI:setAlwaysOnTop(true)
			self.tooltipUI.maxLineWidth = 500
		end
		if not self.tooltipUI:getIsVisible() then
			self.tooltipUI:addToUIManager()
			self.tooltipUI:setVisible(true)
		end
		self.tooltipUI.description = self.tooltip
		if self.joypadFocused then
			self.tooltipUI:setX(self:getAbsoluteX())
			self.tooltipUI:setY(self:getAbsoluteY() + self:getHeight() + 2)
			self.tooltipUI.followMouse = false
		else
			self.tooltipUI:setX(self:getMouseX() + 23)
			self.tooltipUI:setY(self:getMouseY() + 23)
			self.tooltipUI.followMouse = true
		end
	else
		if self.tooltipUI and self.tooltipUI:getIsVisible() then
			self.tooltipUI:setVisible(false)
			self.tooltipUI:removeFromUIManager()
		end
    end
end

function ISGameSoundVolumeControl:render()
	self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b)
	local alpha = math.min(self.borderColor.a + 0.2 * self.fade:fraction(), 1.0)
	self:drawRectBorder(0, 0, self.width, self.height, alpha, self.borderColor.r, self.borderColor.g, self.borderColor.b)

	local padX = 8
	local oneTenth = math.floor((self:getWidth() - padX * 2) / 200)
	local sliderWidth = 8
	local sliderPadX = 1
	local sliderPadY = 4
	padX = padX + (self:getWidth() - padX * 2 - oneTenth * 200) / 2
	local sliderX = padX + oneTenth * self.volume - sliderWidth / 2
	local rgb1 = 0.1 + 0.1 * self.fade:fraction()
	local rgb2 = 0.3 + 0.1 * self.fade:fraction()
	self:drawRect(2, sliderPadY, padX - sliderPadX - 2, self:getHeight() - sliderPadY * 2,
		1, rgb2, rgb2, rgb2)

		self:drawRect(padX, sliderPadY,
			self.volume * oneTenth, self:getHeight() - sliderPadY * 2,
			1, rgb2, rgb2, rgb2)

		self:drawRect(padX + self.volume * oneTenth, sliderPadY,
			(200 - self.volume) * oneTenth, self:getHeight() - sliderPadY * 2,
			1, rgb1, rgb1, rgb1)

	local x = padX + oneTenth * 200 + sliderPadX
	self:drawRect(x, sliderPadY, self:getWidth() - x - 2, self:getHeight() - sliderPadY * 2,
		1, rgb1, rgb1, rgb1)

	self:drawRect(sliderX, 2, sliderWidth, self:getHeight() - 2 * 2, 1.0, 0.5, 0.5, 0.5)

	self:drawRect(sliderX, 2, sliderWidth, 1, 1.0, 0.75, 0.75, 0.75)
	self:drawRect(sliderX + sliderWidth - 1, 2, 1, self:getHeight() - 2 * 2, 1.0, 0.25, 0.25, 0.25)
	self:drawRect(sliderX, 2, 1, self:getHeight() - 2 * 2, 1.0, 0.75, 0.75, 0.75)
	self:drawRect(sliderX, self:getHeight() - 2 - 1, sliderWidth, 1, 1.0, 0.25, 0.25, 0.25)
end

function ISGameSoundVolumeControl:getVolume()
	return self.volume / 100
end

function ISGameSoundVolumeControl:setVolume(volume)
	volume = math.floor(volume * 100 + 0.1)
	if volume >= 0 and volume <= 200 and volume ~= self.volume then
		self.volume = volume
		if self.targetFunc then
			self.targetFunc(self.target, self, self.volume / 100)
		end
	end
end

function ISGameSoundVolumeControl:getTooltip()
	local masterVolumes = {
		Primary = getCore():getOptionSoundVolume() * 10,
		Music = getCore():getOptionMusicVolume() * 10,
		Ambient = getCore():getOptionAmbientVolume() * 10,
		VehicleEngine = getCore():getOptionVehicleEngineVolume() * 10
	}
	local masterVolume = masterVolumes[self.gameSound:getMasterName()] or '???'
	return getText("GameSound_VolumeControlTooltip", math.floor(self:getVolume() * 100 + 0.1), getText("IGUI_SoundName_" .. self.gameSound:getMasterName()), masterVolume):gsub("<br>", "\n")
end

function ISGameSoundVolumeControl:setJoypadFocused(focused)
	self.joypadFocused = focused;
end

function ISGameSoundVolumeControl:onJoypadDirLeft(joypadData)
	self:setVolume(self.volume - 0.5)
end

function ISGameSoundVolumeControl:onJoypadDirRight(joypadData)
	self:setVolume(self.volume + 0.5)
end

function ISGameSoundVolumeControl:new(x, y, width, height, target, targetFunc)
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
