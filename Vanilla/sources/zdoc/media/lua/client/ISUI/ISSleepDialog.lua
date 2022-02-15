--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanelJoypad"

---@class ISSleepDialog : ISPanelJoypad
ISSleepDialog = ISPanelJoypad:derive("ISSleepDialog")

function ISSleepDialog:initialise()
	ISPanel.initialise(self)

	local fontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()

	local spinWidth = 15 + 20 + getTextManager():MeasureStringX(UIFont.Small, "99 hours") + 10 + 15
	local spinHeight = fontHgt + 4
	local spinBox = ISSpinBox:new((self:getWidth() - spinWidth) / 2, 50, spinWidth, spinHeight, target, onchange)
	spinBox:initialise()
	self:addChild(spinBox)

	local fatigue = self.player:getStats():getFatigue()
	local hours = 7
	if fatigue > 0.3 then
		hours = hours + (12 - 7) * ((fatigue - 0.3) / 0.7)
	end
	hours = math.ceil(hours)
	spinBox:addOption(getText("IGUI_Sleep_OneHour"))
	for i=2,hours do
		spinBox:addOption(getText("IGUI_Sleep_NHours", i))
	end
	spinBox.selected = hours
	self.spinBox = spinBox

	local buttonWid1 = getTextManager():MeasureStringX(UIFont.Small, getText("ContextMenu_Sleep")) + 12
	local buttonWid2 = getTextManager():MeasureStringX(UIFont.Small, getText("UI_Cancel")) + 12
	local buttonHgt = fontHgt + 6

	self.yes = ISButton:new((self:getWidth() / 2) - 5 - buttonWid1, spinBox:getY() + spinBox:getHeight() + 20, buttonWid1, buttonHgt, getText("ContextMenu_Sleep"), self, ISSleepDialog.onClick)
	self.yes.internal = "YES"
	self.yes:initialise()
--	self.yes:instantiate()
	self.yes.borderColor = {r=1, g=1, b=1, a=0.25}
	self:addChild(self.yes)

	self.no = ISButton:new((self:getWidth() / 2) + 5, self.yes:getY(), buttonWid2, buttonHgt, getText("UI_Cancel"), self, ISSleepDialog.onClick)
	self.no.internal = "NO"
	self.no:initialise()
--	self.no:instantiate()
	self.no.borderColor = {r=1, g=1, b=1, a=0.25}
	self:addChild(self.no)

	self:setHeight(self.yes:getY() + self.yes:getHeight() + 12)

	self:insertNewLineOfButtons(spinBox.leftButton, spinBox.rightButton)
	self:insertNewLineOfButtons(self.yes, self.no)
end

function ISSleepDialog:destroy()
	UIManager.setShowPausedMessage(true)
	self:setVisible(false)
	self:removeFromUIManager()
	if UIManager.getSpeedControls() then
		UIManager.getSpeedControls():SetCurrentGameSpeed(1)
	end
end

function ISSleepDialog:onClick(button)
	self:destroy()
	if button.internal == "YES" then
		local SleepHours = self.spinBox.selected

		SleepHours = SleepHours + GameTime.getInstance():getTimeOfDay()
		if SleepHours >= 24 then
			SleepHours = SleepHours - 24
		end
		
		self.character:setVariable("ExerciseStarted", false);
		self.character:setVariable("ExerciseEnded", true);

		self.player:setForceWakeUpTime(tonumber(SleepHours))
		self.player:setAsleepTime(0.0)
		self.player:setAsleep(true)
        getSleepingEvent():setPlayerFallAsleep(self.player, tonumber(self.spinBox.selected));

		UIManager.setFadeBeforeUI(self.playerNum, true)
		UIManager.FadeOut(self.playerNum, 1)

		if IsoPlayer.allPlayersAsleep() then
			UIManager.getSpeedControls():SetCurrentGameSpeed(3)
			save(true)
		end

		if JoypadState.players[self.playerNum+1] then
			setJoypadFocus(self.playerNum, nil)
		end
	end
	if button.internal == "NO" then
		if JoypadState.players[self.playerNum+1] then
			setJoypadFocus(self.playerNum, nil)
		end
	end
end

function ISSleepDialog:prerender()
	self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b)
	self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b)
	self:drawTextCentre(self.text, self:getWidth() / 2, 20, 1, 1, 1, 1, UIFont.Small)
end

function ISSleepDialog:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData)
	self.joypadIndexY = 2
	self.joypadIndex = 1
	self.yes:setJoypadFocused(true)
end

function ISSleepDialog:onJoypadDown(button)
	ISPanelJoypad.onJoypadDown(self, button)
	if button == Joypad.BButton then
		self.no.onclick(self.no.target, self.no)
		setJoypadFocus(self.playerNum, nil)
		self:destroy()
	end
end

function ISSleepDialog:update()
	ISPanelJoypad.update(self)
	if self.player:getX() ~= self.playerX or self.player:getY() ~= self.playerY then
		self:destroy()
	end
	local currentAction = ISTimedActionQueue.getTimedActionQueue(self.player)
	if currentAction and currentAction.queue and currentAction.queue[1] then
		self:destroy()
	end
end

function ISSleepDialog:new(x, y, width, height, text, player)
	local o = {}
	o = ISPanelJoypad:new(x, y, width, height)
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
	local txtWidth = getTextManager():MeasureStringX(UIFont.Small, text) + 10
	if width < txtWidth then
		o.width = txtWidth
	end
	o.height = height
	o.anchorLeft = true
	o.anchorRight = true
	o.anchorTop = true
	o.anchorBottom = true
	o.text = text
	o.yes = nil
	o.no = nil
	return o
end

