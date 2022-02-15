--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

---@class ISVehicleAngles : ISCollapsableWindow
ISVehicleAngles = ISCollapsableWindow:derive("ISVehicleAngles")

function ISVehicleAngles:addLabel(_x, _y, _title, _font, _bLeft)
    local FONT_HGT = getTextManager():getFontHeight(_font)
    local label = ISLabel:new(_x, _y, FONT_HGT, _title, 1, 1, 1, 1.0, _font, _bLeft==nil and true or _bLeft)
    label:initialise()
    label:instantiate()
--    label.customData = _data
    self:addChild(label)
    return label:getY() + label:getHeight(), label
end

function ISVehicleAngles:addSlider(_x, _y, _w, _h, _func)
    local slider = ISSliderPanel:new(_x, _y, _w, _h, self, _func )
    slider:initialise()
    slider:instantiate()
--    slider.valueLabel = true
--    slider.customData = _data
    self:addChild(slider)
    return slider:getY() + slider:getHeight(), slider
end

function ISVehicleAngles:callbackHeight(value, slider)
end

function ISVehicleAngles:callbackAngleX(value, slider)
end

function ISVehicleAngles:callbackAngleY(value, slider)
end

function ISVehicleAngles:callbackAngleZ(value, slider)
end

function ISVehicleAngles:createChildren()
	ISCollapsableWindow.createChildren(self)

	self.scriptName = ISLabel:new(10, self:titleBarHeight() + 10, FONT_HGT_SMALL, "Script: ", 1, 1, 1, 1, UIFont.Small, true)
	self:addChild(self.scriptName)

	self:addLabel(10, self.scriptName:getBottom() + 16, "Angle X", UIFont.Small, true)
	local y,slider = self:addSlider(80, self.scriptName:getBottom() + 16, 200, 20, self.callbackAngleX)
	slider:setValues(0, 360, 5, 10, true)
	self.angleX = slider

	self:addLabel(10, y + 16, "Angle Y", UIFont.Small, true)
	y,slider = self:addSlider(80, y + 16, 200, 20, self.callbackAngleY)
	slider:setValues(0, 360, 5, 10, true)
	self.angleY = slider

	self:addLabel(10, y + 16, "Angle Z", UIFont.Small, true)
	y,slider = self:addSlider(80, y + 16, 200, 20, self.callbackAngleZ)
	slider:setValues(0, 360, 5, 10, true)
	self.angleZ = slider

	self:addLabel(10, y + 30, "Height", UIFont.Small, true)
	local y,slider = self:addSlider(80, y + 30, 200, 20, self.callbackHeight)
	self.sliderZ = slider

	local button = ISButton:new(10, y + 30, 60, 16, "Upright", self, self.onButtonLevel)
	button:initialise()
	button:instantiate()
	button.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(button)

	local button2 = ISButton:new(button:getRight() + 10, y + 30, 60, 16, "Left", self, self.onButtonLeft)
	button2:initialise()
	button2:instantiate()
	button2.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(button2)

	local button3 = ISButton:new(button2:getRight() + 10, y + 30, 60, 16, "Bottom", self, self.onButtonBottom)
	button3:initialise()
	button3:instantiate()
	button3.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(button3)

	local button4 = ISButton:new(button3:getRight() + 10, y + 30, 60, 16, "Drop", self, self.onButtonDrop)
	button4:initialise()
	button4:instantiate()
	button4.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(button4)
end

function ISVehicleAngles:onButtonLevel()
	if not self.vehicle then return end
	self.angleX:setCurrentValue(0 + 180)
	self.angleZ:setCurrentValue(0 + 180)
end

function ISVehicleAngles:onButtonLeft()
	if not self.vehicle then return end
	self.angleX:setCurrentValue(0 + 180)
	self.angleY:setCurrentValue(-45 + 180)
	self.angleZ:setCurrentValue(30 + 180)
	self.sliderZ:setCurrentValue(0.5 * 100)
end

function ISVehicleAngles:onButtonBottom()
	if not self.vehicle then return end
	self.angleX:setCurrentValue(0 + 180)
	self.angleY:setCurrentValue(-45 + 180)
	self.angleZ:setCurrentValue(120 + 180)
	self.sliderZ:setCurrentValue(0.5 * 100)
end

function ISVehicleAngles:onButtonDrop()
	if not self.vehicle then return end
	self.vehicle:setPhysicsActive(true)
	self:clearVehicle()
end

function ISVehicleAngles:clearVehicle()
	self.vehicle = nil
	self.scriptName.name = "No vehicle selected"
end

function ISVehicleAngles:prerender()
	ISCollapsableWindow.prerender(self)
	if self.vehicle and (self.vehicle:getMovingObjectIndex() < 0) then
		self:clearVehicle()
	end
	if self.vehicle then
		self.vehicle:setPhysicsActive(false)
		self.vehicle:setDebugZ(self.sliderZ.currentValue / 100)
		self.vehicle:setAngles(self.angleX.currentValue - 180, self.angleY.currentValue - 180, self.angleZ.currentValue - 180)
		local x = 20
		self.angleX:drawText(tostring(self.angleX.currentValue - 180), x, -20, 1, 1, 1, 1, UIFont.Small)
		self.angleY:drawText(tostring(self.angleY.currentValue - 180), x, -20, 1, 1, 1, 1, UIFont.Small)
		self.angleZ:drawText(tostring(self.angleZ.currentValue - 180), x, -20, 1, 1, 1, 1, UIFont.Small)
	end
end

function ISVehicleAngles:close()
	if self.vehicle then
		self:clearVehicle()
	end
	ISCollapsableWindow.close(self)
end

function ISVehicleAngles:setVehicle(vehicle)
	self.vehicle = vehicle
	self.script = vehicle and vehicle:getScript() or nil
	if self.vehicle then
		self.scriptName.name = 'Script: '..self.script:getName()
		self.angleX:setCurrentValue(self.vehicle:getAngleX() + 180)
		self.angleY:setCurrentValue(self.vehicle:getAngleY() + 180)
		self.angleZ:setCurrentValue(self.vehicle:getAngleZ() + 180)
		self.sliderZ:setCurrentValue(self.vehicle:getDebugZ() * 100)
	end
end

function ISVehicleAngles:new()
	local width = 300
	local height = 270
	local x = (getCore():getScreenWidth() / 2) - (width / 2)
	local y = (getCore():getScreenHeight() / 2) - (height / 2)

	local o = ISCollapsableWindow:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o:setResizable(false)
	o.title = "Vehicle Angles"
	return o
end

local ui = nil

function debugVehicleAngles(vehicle)
	ui = ui or ISVehicleAngles:new()
	ui:setVisible(true)
	ui:addToUIManager()
	ui:setVehicle(vehicle or getSpecificPlayer(0):getVehicle())
end

