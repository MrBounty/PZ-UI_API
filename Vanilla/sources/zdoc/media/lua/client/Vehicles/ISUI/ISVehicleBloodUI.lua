--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

---@class ISVehicleBloodUI : ISCollapsableWindow
ISVehicleBloodUI = ISCollapsableWindow:derive("ISVehicleBloodUI")

function ISVehicleBloodUI:addLabel(_x, _y, _title, _font, _bLeft)
    local FONT_HGT = getTextManager():getFontHeight(_font)
    local label = ISLabel:new(_x, _y, FONT_HGT, _title, 1, 1, 1, 1.0, _font, _bLeft==nil and true or _bLeft)
    label:initialise()
    label:instantiate()
--    label.customData = _data
    self:addChild(label)
    return label:getY() + label:getHeight(), label
end

function ISVehicleBloodUI:addSlider(_x, _y, _w, _h, _func)
    local slider = ISSliderPanel:new(_x, _y, _w, _h, self, _func )
    slider:initialise()
    slider:instantiate()
--    slider.valueLabel = true
--    slider.customData = _data
    self:addChild(slider)
    return slider:getY() + slider:getHeight(), slider
end

function ISVehicleBloodUI:callbackBlood(value, slider)
end

function ISVehicleBloodUI:createChildren()
	ISCollapsableWindow.createChildren(self)

	self.scriptName = ISLabel:new(10, self:titleBarHeight() + 10, FONT_HGT_SMALL, "Script: ", 1, 1, 1, 1, UIFont.Small, true)
	self:addChild(self.scriptName)

	self.sliderBlood = {}
	
	self:addLabel(10, self.scriptName:getBottom() + 30, "Blood Front", UIFont.Small, true)
	local y,slider = self:addSlider(80, self.scriptName:getBottom() + 30, 200, 20, self.callbackBlood)
	slider:setValues(0, 100, 1, 10, true)
	self.sliderBlood.Front = slider

	self:addLabel(10, y + 16, "Blood Rear", UIFont.Small, true)
	y,slider = self:addSlider(80, y + 16, 200, 20, self.callbackBlood)
	slider:setValues(0, 100, 1, 10, true)
	self.sliderBlood.Rear = slider

	self:addLabel(10, y + 16, "Blood Left", UIFont.Small, true)
	y,slider = self:addSlider(80, y + 16, 200, 20, self.callbackBlood)
	slider:setValues(0, 100, 1, 10, true)
	self.sliderBlood.Left = slider

	self:addLabel(10, y + 16, "Blood Right", UIFont.Small, true)
	y,slider = self:addSlider(80, y + 16, 200, 20, self.callbackBlood)
	slider:setValues(0, 100, 1, 10, true)
	self.sliderBlood.Right = slider

	tickBox = ISTickBox:new(10, y + 20, 10, 20, "", nil, nil);
	tickBox:initialise();
	tickBox:instantiate();
	tickBox.autoWidth = true
	self:addChild(tickBox);
	tickBox:addOption("All Vehicles");
	self.tickBoxAll = tickBox
end

function ISVehicleBloodUI:clearVehicle()
	self.vehicle = nil
	self.scriptName.name = "No vehicle selected"
end

function ISVehicleBloodUI:prerender()
	ISCollapsableWindow.prerender(self)
	if self.vehicle and (self.vehicle:getMovingObjectIndex() < 0) then
		self:clearVehicle()
	end
	if self.vehicle then
		local x = 20
		local vehicles = getCell():getVehicles()
		for _,id in ipairs({"Front", "Rear", "Left", "Right"}) do
			if self.tickBoxAll:isSelected(1) then
				for i=1,vehicles:size() do
					local vehicle = vehicles:get(i-1)
					vehicle:setBloodIntensity(id, self.sliderBlood[id].currentValue / 100)
				end
			else
				self.vehicle:setBloodIntensity(id, self.sliderBlood[id].currentValue / 100)
			end
			self.sliderBlood[id]:drawText(tostring(self.sliderBlood[id].currentValue), x, -20, 1, 1, 1, 1, UIFont.Small)
		end
	end
end

function ISVehicleBloodUI:close()
	if self.vehicle then
		self:clearVehicle()
	end
	ISCollapsableWindow.close(self)
end

function ISVehicleBloodUI:setVehicle(vehicle)
	self.vehicle = vehicle
	self.script = vehicle and vehicle:getScript() or nil
	if self.vehicle then
		self.scriptName.name = 'Script: '..self.script:getName()
		for _,id in ipairs({"Front", "Rear", "Left", "Right"}) do
			self.sliderBlood[id]:setCurrentValue(self.vehicle:getBloodIntensity(id) * 100)
		end
	end
end

function ISVehicleBloodUI:new()
	local width = 300
	local height = 270
	local x = (getCore():getScreenWidth() / 2) - (width / 2)
	local y = (getCore():getScreenHeight() / 2) - (height / 2)

	local o = ISCollapsableWindow:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o:setResizable(false)
	o.title = "Vehicle Blood"
	return o
end

local ui = nil

function debugVehicleBloodUI(vehicle)
	ui = ui or ISVehicleBloodUI:new()
	ui:setVisible(true)
	ui:addToUIManager()
	ui:setVehicle(vehicle or getSpecificPlayer(0):getVehicle())
end

