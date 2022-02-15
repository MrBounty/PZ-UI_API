--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanel"

---@class ISVolumeIndicator : ISPanel
ISVolumeIndicator = ISPanel:derive("ISVolumeIndicator")


function ISVolumeIndicator:prerender()
	ISPanel.prerender(self)
	getCore():setTestingMicrophone(true)
end

function ISVolumeIndicator:render()
	self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b)
	local alpha = math.min(self.borderColor.a + 0.2 * self.fade:fraction(), 1.0)
	self:drawRectBorder(0, 0, self.width, self.height, alpha, self.borderColor.r, self.borderColor.g, self.borderColor.b)

	self.volume =  math.min(10, math.max(0, getCore():getMicVolumeIndicator()))
	self.iserror =  getCore():getMicVolumeError()
	self.serverVOIPEnable =  getCore():getServerVOIPEnable()
	
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
		local vol_red = 0.0
		local vol_green = 0.0
		if ((i <= self.volume) and (i <= 7)) then 
			vol_red = 0.7
			vol_green = 0.7
		end
		if ((i <= self.volume) and (i > 7)and (i <= 9)) then
			vol_green = 0.7
		end
		if ((i <= self.volume) and (i > 9)) then
			vol_red = 0.7
		end
		self:drawRect(padX + (i-1) * oneTenth + sliderPadX, sliderPadY,
			oneTenth - sliderPadX * 2, self:getHeight() - sliderPadY * 2,
			1, rgb1+vol_red, rgb1+vol_green, rgb1)
	end

	local x = padX + oneTenth * 10 + sliderPadX
	self:drawRect(x, sliderPadY, self:getWidth() - x - 2, self:getHeight() - sliderPadY * 2,
		1, rgb1, rgb1, rgb1)

	if ( self.iserror ) then
		self:drawText("Error! Check record device frequency.", 5, 1, 1, 0.5, 0.5, 1);
	end
	if ( not self.serverVOIPEnable ) then
		self:drawText("Warning! Server disabled VOIP function.", 5, 1, 1, 0.5, 0.5, 1);
	end

end


function ISVolumeIndicator:new(x, y, width, height, target, targetFunc)
	local o = ISPanel:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.backgroundColor = {r=0, g=0, b=0, a=1}
	o.borderColor = {r=1, g=1, b=1, a=0.5}
	o.volume = 0
	o.iserror = false
	o.target = target
	o.targetFunc = targetFunc
	o.fade = UITransition.new()
	o.isSlider = false
	return o
end
