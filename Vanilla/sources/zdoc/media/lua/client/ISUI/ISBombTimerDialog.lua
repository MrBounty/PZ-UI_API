--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanelJoypad"

---@class ISBombTimerDialog : ISPanelJoypad
ISBombTimerDialog = ISPanelJoypad:derive("ISBombTimerDialog")

function ISBombTimerDialog:initialise()
	ISPanel.initialise(self)

	local fontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()

	local buttonWid = getTextManager():MeasureStringX(UIFont.Small, "+10") + 3 + 3
	local spinWidth = getTextManager():MeasureStringX(UIFont.Small, "1000") * 2
	local spinX = (self:getWidth() - spinWidth) / 2
	local spinY = 50
	local spinHeight = fontHgt + 4

	self.button1m = ISButton:new(spinX - 3 - buttonWid - 3 - buttonWid - 3 - buttonWid, spinY, buttonWid, spinHeight, "-1", self, ISBombTimerDialog.onButton)
	self.button1m.internal = "MINUS1"
	self.button1m:initialise()
	self.button1m:instantiate()
	self.button1m.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.button1m)

	self.button5m = ISButton:new(spinX - 3 - buttonWid - 3 - buttonWid, spinY, buttonWid, spinHeight, "-5", self, ISBombTimerDialog.onButton)
	self.button5m.internal = "MINUS5"
	self.button5m:initialise()
	self.button5m:instantiate()
	self.button5m.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.button5m)

	self.button10m = ISButton:new(spinX - 3 - buttonWid, spinY, buttonWid, spinHeight, "-10", self, ISBombTimerDialog.onButton)
	self.button10m.internal = "MINUS10"
	self.button10m:initialise()
	self.button10m:instantiate()
	self.button10m.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.button10m)

	self.button1p = ISButton:new(spinX + spinWidth + 3, spinY, buttonWid, spinHeight, "+1", self, ISBombTimerDialog.onButton)
	self.button1p.internal = "PLUS1"
	self.button1p:initialise()
	self.button1p:instantiate()
	self.button1p.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.button1p)

	self.button5p = ISButton:new(spinX + spinWidth + 3 + buttonWid + 3, spinY, buttonWid, spinHeight, "+5", self, ISBombTimerDialog.onButton)
	self.button5p.internal = "PLUS5"
	self.button5p:initialise()
	self.button5p:instantiate()
	self.button5p.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.button5p)

	self.button10p = ISButton:new(spinX + spinWidth + 3 + buttonWid + 3 + buttonWid + 3, spinY, buttonWid, spinHeight, "+10", self, ISBombTimerDialog.onButton)
	self.button10p.internal = "PLUS10"
	self.button10p:initialise()
	self.button10p:instantiate()
	self.button10p.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.button10p)

	local textBox = ISTextEntryBox:new(tostring(self.time), spinX, spinY, spinWidth, spinHeight)
	textBox:initialise()
	textBox:instantiate()
    textBox:setOnlyNumbers(true);
	self:addChild(textBox)
	self.textBox = textBox

	local buttonWid1 = getTextManager():MeasureStringX(UIFont.Small, getText("UI_Ok")) + 12
	local buttonWid2 = getTextManager():MeasureStringX(UIFont.Small, getText("UI_Cancel")) + 12
	local buttonHgt = fontHgt + 6

	self.yes = ISButton:new((self:getWidth() / 2) - 5 - buttonWid1, textBox:getY() + textBox:getHeight() + 20, buttonWid1, buttonHgt, getText("UI_Ok"), self, ISBombTimerDialog.onButton)
	self.yes.internal = "OK"
	self.yes:initialise()
--	self.yes:instantiate()
	self.yes.borderColor = {r=1, g=1, b=1, a=0.25}
	self:addChild(self.yes)

	self.no = ISButton:new((self:getWidth() / 2) + 5, self.yes:getY(), buttonWid2, buttonHgt, getText("UI_Cancel"), self, ISBombTimerDialog.onButton)
	self.no.internal = "CANCEL"
	self.no:initialise()
--	self.no:instantiate()
	self.no.borderColor = {r=1, g=1, b=1, a=0.25}
	self:addChild(self.no)

	self:setHeight(self.yes:getY() + self.yes:getHeight() + 12)

	self:insertNewLineOfButtons(self.button1m, self.button5m, self.button10m, self.button1p, self.button5p, self.button10p)
	self:insertNewLineOfButtons(self.yes, self.no)
end

function ISBombTimerDialog:destroy()
	UIManager.setShowPausedMessage(true)
	self:setVisible(false)
	self:removeFromUIManager()
	if UIManager.getSpeedControls() then
		UIManager.getSpeedControls():SetCurrentGameSpeed(1)
	end
end

function ISBombTimerDialog:onButton(button)
	if button.internal == "OK" or button.internal == "CANCEL" then
		self:destroy()
		if JoypadState.players[self.playerNum+1] then
			setJoypadFocus(self.playerNum, self.prevFocus)
		end
		self.targetFunc(self.target, button, self.param1, self.param2, self.param3, self.param4)
	end
	if button.internal == "MINUS1" then
		self:addTime(-1)
	end
	if button.internal == "MINUS5" then
		self:addTime(-5)
	end
	if button.internal == "MINUS10" then
		self:addTime(-10)
	end
	if button.internal == "PLUS1" then
		self:addTime(1)
	end
	if button.internal == "PLUS5" then
		self:addTime(5)
	end
	if button.internal == "PLUS10" then
		self:addTime(10)
	end
end

function ISBombTimerDialog:prerender()
	self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b)
	self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b)
	self:drawTextCentre(self.prompt, self:getWidth() / 2, 20, 1, 1, 1, 1, UIFont.Small)
end

function ISBombTimerDialog:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData)
	self.joypadIndexY = 2
	self.joypadIndex = 1
	self.yes:setJoypadFocused(true)
end

function ISBombTimerDialog:onJoypadDown(button)
	ISPanelJoypad.onJoypadDown(self, button)
	if button == Joypad.BButton then
		self:onButton(self.no)
	end
end

function ISBombTimerDialog:render()
	ISPanelJoypad.render(self)
--	if self.player:getX() ~= self.playerX or self.player:getY() ~= self.playerY then
--		self:destroy()
--	end
end

function ISBombTimerDialog:addTime(seconds)
	local current = tonumber(self.textBox:getText()) or 0
	local newTime = current + seconds
	if newTime < 1 then newTime = 1 end
	self.textBox:setText(tostring(newTime))
end

function ISBombTimerDialog:getTime()
	return tonumber(self.textBox:getText()) or 0
end

function ISBombTimerDialog:new(x, y, width, height, prompt, time, player, target, targetFunc, param1, param2, param3, param4)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.player = player
	o.playerNum = player:getPlayerNum()
	o.playerX = player:getX()
	o.playerY = player:getY()
	if y == 0 then
		o.y = getPlayerScreenTop(o.playerNum) + (getPlayerScreenHeight(o.playerNum) - height) / 2
		o:setY(o.y)
	end
	if x == 0 then
		o.x = getPlayerScreenLeft(o.playerNum) + (getPlayerScreenWidth(o.playerNum) - width) / 2
		o:setX(o.x)
	end
	o.name = nil
	o.backgroundColor = {r=0, g=0, b=0, a=0.75}
	o.borderColor = {r=0.4, g=0.4, b=0.4, a=1}
	o.width = width
	local txtWidth = getTextManager():MeasureStringX(UIFont.Small, prompt) + 10
	if width < txtWidth then
		o.width = txtWidth
	end
	o.height = height
	o.anchorLeft = true
	o.anchorRight = true
	o.anchorTop = true
	o.anchorBottom = true
	o.prompt = prompt
	o.time = time
	o.target = target
	o.targetFunc = targetFunc
    o.param1 = param1
    o.param2 = param2
    o.param3 = param3
    o.param4 = param4
	return o
end


