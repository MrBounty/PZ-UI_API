--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanel"

---@class ISControllerTestPanel : ISPanel
ISControllerTestPanel = ISPanel:derive("ControllerTest")

function ISControllerTestPanel:onControllerSelected()
	JoypadState.controllerTest = false
	self.selectedController = nil
	local controller = self.combo:getOptionData(self.combo.selected)
	if controller < 0 or controller >= getControllerCount() then
		if self.mainOptions then
			self.mainOptions.labelJoypadSensitivity.name = getText("UI_optionscreen_select_gamepad")
			self.mainOptions.btnJoypadSensitivityP:setEnable(false)
			self.mainOptions.btnJoypadSensitivityM:setEnable(false)
		end
		return
	end
	self.selectedController = controller
	JoypadState.controllerTest = true

	self.axisLabelWid = getTextManager():MeasureStringX(UIFont.Small, getText("UI_ControllerTest_Axis", 9))
	self.axisY = self.combo:getBottom() + 12
	self.axisHgt = 20
	self.axisPadY = 2

	self.buttonX = 32
	self.buttonWid = math.max(self.smallFontHgt, 24)
	self.buttonGapX = 4
	self.buttonGapY = 4
	
	if self.mainOptions then
		local controller = self.selectedController
		if not controller then 
			self.mainOptions.btnJoypadSensitivityP:setEnable(false)
			self.mainOptions.btnJoypadSensitivityM:setEnable(false)
			return 
		end
		self.mainOptions.btnJoypadSensitivityP:setEnable(true)
		self.mainOptions.btnJoypadSensitivityM:setEnable(true)
		if getControllerAxisCount(controller)>0 then
			self.mainOptions.labelJoypadSensitivity.name=string.format("%.2f",getControllerDeadZone(controller, 0))
		end
	end
end

function ISControllerTestPanel:joypadSensitivityM()
	local controller = self.selectedController
	if not controller then return end
	local axisCount = getControllerAxisCount(controller)
	for i=1,axisCount do
		local deadZone = getControllerDeadZone(controller, i-1) - 0.05
		if deadZone<0 then
			deadZone = 0;
		end
		if deadZone>1 then
			deadZone = 1;
		end
		setControllerDeadZone(controller, i-1, deadZone)
	end
	self.mainOptions.labelJoypadSensitivity.name=string.format("%.2f",getControllerDeadZone(controller, 0))
	saveControllerSettings(controller)
end

function ISControllerTestPanel:joypadSensitivityP()
	local controller = self.selectedController
	if not controller then return end
	local axisCount = getControllerAxisCount(controller)
	for i=1,axisCount do
		local deadZone = getControllerDeadZone(controller, i-1) + 0.05
		if deadZone<0 then
			deadZone = 0;
		end
		if deadZone>1 then
			deadZone = 1;
		end
		setControllerDeadZone(controller, i-1, deadZone)
	end
	self.mainOptions.labelJoypadSensitivity.name=string.format("%.2f",getControllerDeadZone(controller, 0))
	saveControllerSettings(controller)
end

function ISControllerTestPanel:render()
	self.backgroundColor.a = 0.5

	local controller = self.selectedController
	if not controller then return end
	--JoypadState.controllerTest = true

	local barX = 16 + self.axisLabelWid + 24
	local barWid = 200
	local barHgt = 12
	local axisY = self.axisY
	local axisCount = getControllerAxisCount(controller)
	for i=1,axisCount do
		self:drawText(getText("UI_ControllerTest_Axis", i-1), 16, axisY + (self.axisHgt - self.smallFontHgt) / 2, 1, 1, 1, 1, UIFont.Small)
		local f = (1 + getControllerAxisValue(controller, i-1)) / 2
		local barY = axisY + (self.axisHgt - barHgt) / 2
		self:drawProgressBar(barX, barY, barWid, barHgt, f, { r=0.9,g=0.9,b=0.9,a=1 })
		local deadZone = getControllerDeadZone(controller, i-1)
		self:drawRect(barX + barWid / 2 - barWid / 2 * deadZone, barY, barWid * deadZone, barHgt, 0.5, 0, 0, 1)
		axisY = axisY + self.axisHgt + self.axisPadY
	end

	local isAY = 0
	local buttonX = self.buttonX
	local buttonY = axisY + 12
	local buttonCount = getControllerButtonCount(controller)
	buttonCount = math.min(buttonCount, 40)
	self:drawText(getText("UI_ControllerTest_Buttons"), 16, buttonY, 1, 1, 1, 1, UIFont.Small)
	buttonY = buttonY + self.smallFontHgt + 8
	local buttonsBottom = buttonY
	for i=1,buttonCount do
		local r,g,b = 0.5,0.5,0.5
		if isJoypadPressed(controller,i-1) then
			self:drawRect(buttonX, buttonY, self.buttonWid, self.buttonWid, 1.0, 0.2, 0.2, 0.2)
			r,g,b = 0.9,0.9,0.9
			if i-1 == Joypad.AButton then
				isAY = isAY + 1
			end
			if i-1 == Joypad.YButton then
				isAY = isAY + 1
			end
		end
		self:drawRectBorder(buttonX, buttonY, self.buttonWid, self.buttonWid, 1.0, r, g, b)
		self:drawTextCentre(tostring(i-1), buttonX + self.buttonWid / 2, buttonY + (self.buttonWid - self.smallFontHgt) / 2, 1, 1, 1, 1, UIFont.Small)
		buttonsBottom = buttonY + self.buttonWid
		buttonX = buttonX + self.buttonWid + self.buttonGapX
		if buttonX > self.width - 32 - self.buttonWid then
			buttonX = self.buttonX
			buttonY = buttonY + self.buttonWid + self.buttonGapY
		end
	end
	if isAY == 2 then
		JoypadState.controllerTest = false
		--self.selectedController = nil
		--self.combo.selected = 0
		
	end

	self:drawText(getText("UI_ControllerTest_Pov"), 16, buttonsBottom + 12, 1, 1, 1, 1, UIFont.Small)
	local povY = buttonsBottom + 12 + self.smallFontHgt + 8
	-----
	self:drawText(getText("UI_ControllerTest_PovX"), 32, povY + (self.axisHgt - self.smallFontHgt) / 2, 1, 1, 1, 1, UIFont.Small)
	local f = (1 + getControllerPovX(controller)) / 2
	self:drawProgressBar(16 + self.axisLabelWid + 24, povY + (self.axisHgt - 12) / 2, 200, 12, f, { r=0.9,g=0.9,b=0.9,a=1 })
	povY = povY + self.axisHgt + self.axisPadY
	-----
	self:drawText(getText("UI_ControllerTest_PovY"), 32, povY + (self.axisHgt - self.smallFontHgt) / 2, 1, 1, 1, 1, UIFont.Small)
	local f = (1 + getControllerPovY(controller)) / 2
	self:drawProgressBar(16 + self.axisLabelWid + 24, povY + (self.axisHgt - 12) / 2, 200, 12, f, { r=0.9,g=0.9,b=0.9,a=1 })
	povY = povY + self.axisHgt + self.axisPadY
	-----
	if JoypadState.controllerTest then
		self:drawText(getText("UI_ControllerTest_AY4exit"), 32, povY + (self.axisHgt - self.smallFontHgt) / 2, 1, 0.2, 0.2, 1, UIFont.Small)
		--povY = povY + self.axisHgt + self.axisPadY
	end
end

function ISControllerTestPanel:createChildren()
	local label = ISLabel:new(16, 12, self.smallFontHgt + 3 * 2, getText("UI_ControllerTest_Combo"), 1, 1, 1, 1, UIFont.Small, true)
	label:initialise()
	self:addChild(label)

	local combo = ISComboBox:new(label:getRight() + 8, 12, self.width - 16 - (label:getRight() + 8), self.smallFontHgt + 3 * 2, self, self.onControllerSelected)
	combo:initialise()
	combo:setAnchorRight(true)
	self:addChild(combo)
	self.combo = combo

	self:setControllerCombo()
end

function ISControllerTestPanel:setControllerCombo()
	self.combo:clear()
	self.combo:addOptionWithData(getText("UI_ControllerTest_None"), -1)
	for i=1,getControllerCount() do
		if isControllerConnected(i-1) then
			self.combo:addOptionWithData(getControllerName(i-1), i-1)
		end
	end
end

function ISControllerTestPanel:OnGamepadConnect(index)
	self:setControllerCombo()
end

function ISControllerTestPanel:OnGamepadDisconnect(index)
	JoypadState.controllerTest = false
	self:setControllerCombo()
end

function ISControllerTestPanel:new(x, y, width, height)
	local o = ISPanel:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.axisY = {}
	o.smallFontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()
	return o
end

