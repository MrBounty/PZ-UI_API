--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

---@class ISVehicleHSV : ISCollapsableWindow
ISVehicleHSV = ISCollapsableWindow:derive("ISVehicleHSV")

function ISVehicleHSV:addLabel(_x, _y, _title, _font, _bLeft)
    local FONT_HGT = getTextManager():getFontHeight(_font)
    local label = ISLabel:new(_x, _y, FONT_HGT, _title, 1, 1, 1, 1.0, _font, _bLeft==nil and true or _bLeft)
    label:initialise()
    label:instantiate()
--    label.customData = _data
    self:addChild(label)
    return label:getY() + label:getHeight(), label
end

function ISVehicleHSV:addSlider(_x, _y, _w, _h, _func)
    local slider = ISSliderPanel:new(_x, _y, _w, _h, self, _func )
    slider:initialise()
    slider:instantiate()
--    slider.valueLabel = true
--    slider.customData = _data
    self:addChild(slider)
    return slider:getY() + slider:getHeight(), slider
end

function ISVehicleHSV:callbackAngleX(value, slider)
end

function ISVehicleHSV:callbackAngleY(value, slider)
end

function ISVehicleHSV:callbackAngleZ(value, slider)
end

function ISVehicleHSV:createChildren()
	ISCollapsableWindow.createChildren(self)

	self.scriptName = ISLabel:new(10, self:titleBarHeight() + 10, FONT_HGT_SMALL, "Script: ", 1, 1, 1, 1, UIFont.Small, true)
	self:addChild(self.scriptName)

	self:addLabel(10, self.scriptName:getBottom() + 16, "Hue", UIFont.Small, true)
	local y,slider = self:addSlider(80, self.scriptName:getBottom() + 16, 200, 20, self.callbackAngleX)
	slider:setValues(0, 100, 1, 10, true)
	self.colorHue = slider

	self:addLabel(10, y + 16, "Saturation", UIFont.Small, true)
	y,slider = self:addSlider(80, y + 16, 200, 20, self.callbackAngleY)
	slider:setValues(0, 100, 1, 10, true)
	self.colorSaturation = slider

	self:addLabel(10, y + 16, "Value", UIFont.Small, true)
	y,slider = self:addSlider(80, y + 16, 200, 20, self.callbackAngleZ)
	slider:setValues(0, 100, 1, 10, true)
	self.colorValue = slider

	local button = ISButton:new(10, y + 30, 60, 16, "Red", self, self.onButtonRed)
	button:initialise()
	button:instantiate()
	button.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(button)

	local button2 = ISButton:new(button:getRight() + 10, y + 30, 60, 16, "Blue", self, self.onButtonBlue)
	button2:initialise()
	button2:instantiate()
	button2.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(button2)

	local button3 = ISButton:new(button2:getRight() + 10, y + 30, 60, 16, "White", self, self.onButtonWhite)
	button3:initialise()
	button3:instantiate()
	button3.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(button3)

	local button4 = ISButton:new(button3:getRight() + 10, y + 30, 60, 16, "Black", self, self.onButtonBlack)
	button4:initialise()
	button4:instantiate()
	button4.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(button4)

	local button5 = ISButton:new(10, button:getBottom() + 10, 60, 16, "Other", self, self.onButtonOther)
	button5:initialise()
	button5:instantiate()
	button5.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(button5)
end

function ISVehicleHSV:onButtonRed()
	if not self.vehicle then return end
	self.colorHue:setCurrentValue(ZombRand(0, 3))
	self.colorSaturation:setCurrentValue(ZombRand(85, 100))
	self.colorValue:setCurrentValue(ZombRand(55, 85))
end

function ISVehicleHSV:onButtonBlue()
	if not self.vehicle then return end
	self.colorHue:setCurrentValue(ZombRand(55, 61))
	self.colorSaturation:setCurrentValue(ZombRand(85, 100))
	self.colorValue:setCurrentValue(ZombRand(65, 75))
end

function ISVehicleHSV:onButtonWhite()
	if not self.vehicle then return end
	self.colorHue:setCurrentValue(15) -- ZombRand(0, 100)
	self.colorSaturation:setCurrentValue(ZombRand(000, 10))
	self.colorValue:setCurrentValue(ZombRand(70, 80))
end

function ISVehicleHSV:onButtonBlack()
	if not self.vehicle then return end
	self.colorHue:setCurrentValue(ZombRand(0, 100))
	self.colorSaturation:setCurrentValue(ZombRand(0, 10))
	self.colorValue:setCurrentValue(ZombRand(10, 25))
end

function ISVehicleHSV:onButtonOther()
	if not self.vehicle then return end
	self.colorHue:setCurrentValue(ZombRand(0, 100))
	self.colorSaturation:setCurrentValue(ZombRand(60, 75))
	self.colorValue:setCurrentValue(ZombRand(30, 70))
end

function ISVehicleHSV:clearVehicle()
	self.vehicle = nil
	self.scriptName.name = "No vehicle selected"
end

function ISVehicleHSV:prerender()
	ISCollapsableWindow.prerender(self)
	if self.vehicle and (self.vehicle:getMovingObjectIndex() < 0) then
		self:clearVehicle()
	end
	if self.vehicle then
		self.vehicle:setColorHSV(self.colorHue.currentValue / 100, self.colorSaturation.currentValue / 100, self.colorValue.currentValue / 100)
		local x = 20
		self.colorHue:drawText(tostring(self.colorHue.currentValue), x, -20, 1, 1, 1, 1, UIFont.Small)
		self.colorSaturation:drawText(tostring(self.colorSaturation.currentValue), x, -20, 1, 1, 1, 1, UIFont.Small)
		self.colorValue:drawText(tostring(self.colorValue.currentValue), x, -20, 1, 1, 1, 1, UIFont.Small)
	end
end

function ISVehicleHSV:close()
	if self.vehicle then
		self:clearVehicle()
	end
	ISCollapsableWindow.close(self)
end

function ISVehicleHSV:setVehicle(vehicle)
	self.vehicle = vehicle
	self.script = vehicle and vehicle:getScript() or nil
	if self.vehicle then
		self.scriptName.name = 'Script: '..self.script:getName()
		self.colorHue:setCurrentValue(self.vehicle:getColorHue() * 100)
		self.colorSaturation:setCurrentValue(self.vehicle:getColorSaturation() * 100)
		self.colorValue:setCurrentValue(self.vehicle:getColorValue() * 100)
	end
end

function ISVehicleHSV:new()
	local width = 300
	local height = 270
	local x = (getCore():getScreenWidth() / 2) - (width / 2)
	local y = (getCore():getScreenHeight() / 2) - (height / 2)

	local o = ISCollapsableWindow:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o:setResizable(false)
	o.title = "Vehicle HSV"
	return o
end

local ui = nil

function debugVehicleColor(vehicle)
	ui = ui or ISVehicleHSV:new()
	ui:setVisible(true)
	ui:addToUIManager()
	ui:setVehicle(vehicle or getSpecificPlayer(0):getVehicle())
end

