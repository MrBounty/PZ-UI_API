--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanelJoypad"

CoopUserName = ISPanelJoypad:derive("CoopUserName")

function CoopUserName:createChildren()
	local padX = 16
	local btnWid = 100
	local btnHgt = 25
	local btnPadY = 5
	local titleHgt = 80

	self.fontHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local inset = 2
	local width = 300
	local height = inset + self.fontHgt + inset
	self.entry = ISTextEntryBox:new("", (self:getWidth() - width) / 2, (self:getHeight() - height) / 2, width, height)
	self.entry.font = UIFont.Medium
	self.entry:initialise()
	self.entry:instantiate()
	self:addChild(self.entry)

	self.backButton = ISButton:new(padX, self.height - btnPadY - btnHgt, 100, 25, getText("UI_btn_back"), self, self.clickBack)
	self.backButton.internal = "BACK"
	self.backButton:initialise()
	self.backButton:instantiate()
	self.backButton:setAnchorLeft(true)
	self.backButton:setAnchorTop(false)
	self.backButton:setAnchorBottom(true)
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.backButton)

	self.nextButton = ISButton:new(self.width - 116, self.height - btnPadY - btnHgt, 100, 25, getText("UI_btn_next"), self, self.clickNext)
	self.nextButton.internal = "NEXT"
	self.nextButton:initialise()
	self.nextButton:instantiate()
	self.nextButton:setAnchorLeft(false)
	self.nextButton:setAnchorRight(true)
	self.nextButton:setAnchorTop(false)
	self.nextButton:setAnchorBottom(true)
	self.nextButton:setEnable(true) -- sets the hard-coded border color
	self:addChild(self.nextButton)
end

function CoopUserName:shouldShow()
	return isClient() and CoopCharacterCreation.instance.playerIndex > 0
end

function CoopUserName:getUserName()
	return self.entry:getText()
end

function CoopUserName:beforeShow()
	local playerIndex = CoopCharacterCreation.instance.playerIndex
	local player = getSpecificPlayer(playerIndex)
	self.entry:setText(player and player:getUsername() or ("Player"..(playerIndex + 1)))
end

function CoopUserName:clickBack()
	self:setVisible(false)
	CoopCharacterCreation.instance:cancel()
end

function CoopUserName:clickNext()
	self:setVisible(false)
	if CoopMapSpawnSelect.instance:hasChoices() then
		CoopMapSpawnSelect.instance:fillList()
		CoopMapSpawnSelect.instance:setVisible(true, self.joyfocus)
	else
		CoopMapSpawnSelect.instance:useDefaultSpawnRegion()
		CoopCharacterCreationMain.instance:setVisible(true, self.joyfocus)
	end
end

function CoopUserName:render()
CoopUserName.instance = self
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_servers_username"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Large)
	if isValidUserName(self:getUserName()) then
		self.nextButton:setEnable(true)
		self.nextButton:setTooltip(nil)
	else
		self.nextButton:setEnable(false)
		self.nextButton:setTooltip(getText("UI_servers_err_username"))
	end
end

function CoopUserName:onGainJoypadFocus(joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData)
--    joypadData.focus = self.listbox
--    updateJoypadFocus(joypadData)
    self:setISButtonForA(self.nextButton)
    self:setISButtonForB(self.backButton)
end

function CoopUserName:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o:setUIName("CoopUserName")
	CoopUserName.instance = o
	return o
end

