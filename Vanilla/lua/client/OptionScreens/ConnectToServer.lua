--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

ConnectToServer = ISPanelJoypad:derive("ConnectToServer")

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)
local FONT_HGT_LARGE = getTextManager():getFontHeight(UIFont.Large)

function ConnectToServer:create()
	local buttonHgt = math.max(FONT_HGT_SMALL + 3 * 2, 25)

	self.title = ISLabel:new(self.width / 2, 20, 50, "???", 1, 1, 1, 1, UIFont.Large, true)
	self.title:initialise()
	self.title:instantiate()
	self.title:setAnchorLeft(true)
	self.title:setAnchorRight(false)
	self.title:setAnchorTop(true)
	self.title:setAnchorBottom(false)
	self.title.center = true
	self:addChild(self.title)

	self.serverName1 = ISLabel:new(self.width / 2 - 6, self.title:getBottom() + 40, FONT_HGT_MEDIUM, getText("UI_ConnectToServer_ServerName"), 0.7, 0.7, 0.7, 1, UIFont.Medium, false)
	self.serverName1:initialise()
	self.serverName1:instantiate()
	self.serverName1:setAnchorLeft(true)
	self.serverName1:setAnchorRight(false)
	self.serverName1:setAnchorTop(true)
	self.serverName1:setAnchorBottom(false)
	self:addChild(self.serverName1)

	self.userName1 = ISLabel:new(self.width / 2 - 6, self.serverName1:getBottom() + 8, FONT_HGT_MEDIUM, getText("UI_ConnectToServer_UserName"), 0.7, 0.7, 0.7, 1, UIFont.Medium, false)
	self.userName1:initialise()
	self.userName1:instantiate()
	self.userName1:setAnchorLeft(true)
	self.userName1:setAnchorRight(false)
	self.userName1:setAnchorTop(true)
	self.userName1:setAnchorBottom(false)
	self:addChild(self.userName1)

	local labelX = self.width / 2 + 6

	self.serverName = ISLabel:new(labelX, self.title:getBottom() + 40, FONT_HGT_MEDIUM, "???", 1, 1, 1, 1, UIFont.Medium, true)
	self.serverName:initialise()
	self.serverName:instantiate()
	self.serverName:setAnchorLeft(true)
	self.serverName:setAnchorRight(false)
	self.serverName:setAnchorTop(true)
	self.serverName:setAnchorBottom(false)
	self:addChild(self.serverName)

	self.userName = ISLabel:new(labelX, self.serverName:getBottom() + 8, FONT_HGT_MEDIUM, "???", 1, 1, 1, 1, UIFont.Medium, true)
	self.userName:initialise()
	self.userName:instantiate()
	self.userName:setAnchorLeft(true)
	self.userName:setAnchorRight(false)
	self.userName:setAnchorTop(true)
	self.userName:setAnchorBottom(false)
	self:addChild(self.userName)

	self.connectLabel = ISLabel:new(self.width / 2, self.userName:getBottom() + 50, FONT_HGT_MEDIUM, "", 1, 1, 1, 1, UIFont.Medium, true)
	self.connectLabel:initialise()
	self.connectLabel:instantiate()
	self.connectLabel:setAnchorLeft(true)
	self.connectLabel:setAnchorRight(false)
	self.connectLabel:setAnchorTop(true)
	self.connectLabel:setAnchorBottom(false)
	self.connectLabel.center = true
	self:addChild(self.connectLabel)

	self.richText = ISRichTextPanel:new(0, self.connectLabel:getBottom() + 150, self.width * 4 / 5, FONT_HGT_MEDIUM * 4)
	self.richText:initialise()
	self.richText:instantiate()
	self.richText:noBackground()
	self.richText:setMargins(0, 0, 0, 0)
	self.richText.font = UIFont.Medium
	self:addChild(self.richText)
	self.richText.text = getSteamModeActive() and getText("UI_ConnectToServer_ReminderSteam") or getText("UI_ConnectToServer_ReminderNoSteam")
	self.richText.text = " <CENTRE> " .. self.richText.text
	self.richText:paginate()
	self.richText:setX(self.width / 2 - self.richText.width / 2)

	self.backBtn = ISButton:new((self.width - 100) / 2, self.height - 50, 100, buttonHgt, getText("UI_btn_back"), self, self.onBackButton)
	self.backBtn.internal = "BACK"
	self.backBtn:initialise()
	self.backBtn:instantiate()
	self.backBtn:setAnchorLeft(true)
	self.backBtn:setAnchorRight(false)
	self.backBtn:setAnchorTop(false)
	self.backBtn:setAnchorBottom(true)
	self:addChild(self.backBtn)

	self.arrowBG = getTexture("media/ui/ArrowRight_Disabled.png")
	self.arrowFG = getTexture("media/ui/ArrowRight.png")

	ConnectToServer.instance = self
end

function ConnectToServer:prerender()
	ISPanelJoypad.prerender(self)
	ConnectToServer.instance = self
end

function ConnectToServer:render()
	ISPanelJoypad.render(self)

	if self.connecting then
		local x = self.width / 2 - 15 * 1.5
		local y = self.connectLabel.y - 25
		self:drawTexture(self.arrowBG, x, y, 1, 1, 1, 1)
		self:drawTexture(self.arrowBG, x + 15, y, 1, 1, 1, 1)
		self:drawTexture(self.arrowBG, x + 30, y, 1, 1, 1, 1)

		local ms = UIManager.getMillisSinceLastRender()
		self.timerMultiplierAnim = (self.timerMultiplierAnim or 0) + ms
		if self.timerMultiplierAnim <= 500 then
			self.animOffset = -1
		elseif self.timerMultiplierAnim <= 1000 then
			self.animOffset = 0
		elseif self.timerMultiplierAnim <= 1500 then
			self.animOffset = 15
		elseif self.timerMultiplierAnim <= 2000 then
			self.animOffset = 30
		else
			self.timerMultiplierAnim = 0
		end
		if self.animOffset > -1 then
			self:drawTexture(self.arrowFG, x + self.animOffset, y, 1, 1, 1, 1)
		end
	end
end

function ConnectToServer:onResize(width, height)
	ISPanelJoypad.onResize(self, width, height)
	if not self.title then return end
	self.title:setX(width / 2)
	self.serverName1:setX(width / 2 - 6 - self.serverName1.width)
	self.userName1:setX(width / 2 - 6 - self.userName1.width)
	self.serverName:setX(width / 2 + 6)
	self.userName:setX(width / 2 + 6)
	self.connectLabel:setX(width / 2)
	self.richText:setX(width / 2 - self.richText.width / 2)
	self.backBtn:setX(width / 2 - self.backBtn.width / 2)
end

function ConnectToServer:onBackButton()
	if self.connecting or isClient() then
		self.connecting = false
		backToSinglePlayer()
	end
	self:setVisible(false)
	if self.isCoop then
		MainScreen.instance.bottomPanel:setVisible(true, self.joyfocus)
	else
		self.previousScreen:setVisible(true, self.joyfocus)
	end
end

function ConnectToServer:connect(previousScreen, serverName, userName, password, IP, localIP, port, serverPassword)
	previousScreen:setVisible(false)
	self:setVisible(true, previousScreen.joyfocus)
	self.previousScreen = previousScreen
	self.title:setName(getText("UI_ConnectToServer_TitleDedicated"))

	self.serverName1:setVisible(true)
	self.serverName:setVisible(true)
	if serverName == "" then
		self.serverName1:setName(getText("UI_ConnectToServer_ServerIP"))
		self.serverName:setName(IP)
	else
		self.serverName1:setName(getText("UI_ConnectToServer_ServerName"))
		self.serverName:setName(serverName)
	end

	self.userName:setName(userName)

	self.connectLabel.name = getText("UI_servers_Connecting")
	self.failMessage = nil
	self.backBtn:setTitle(getText("UI_coopscreen_btn_abort"))
	self.connecting = true
	self.isCoop = false
	self:onResize(self.width, self.height)
	serverConnect(userName, password, IP, localIP, port, serverPassword)
end

function ConnectToServer:connectCoop(previousScreen, serverSteamID)
	previousScreen:setVisible(false)
	self:setVisible(true, previousScreen.joyfocus)
	self.previousScreen = previousScreen
	self.title:setName(getText("UI_ConnectToServer_TitleCoop"))

	self.serverName1:setVisible(false)
	self.serverName:setVisible(false)

	self.userName:setName(getCurrentUserProfileName())

	self.connectLabel.name = getText("UI_servers_Connecting")
	self.failMessage = nil
	self.backBtn:setTitle(getText("UI_coopscreen_btn_abort"))
	self.connecting = true
	self.isCoop = true
	self:onResize(self.width, self.height)
	serverConnectCoop(serverSteamID)
end

function ConnectToServer:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData)
	self:setISButtonForB(self.backBtn)
end

function ConnectToServer:OnConnected()
	if not SystemDisabler.getAllowDebugConnections() and getDebug() and not isAdmin() and not isCoopHost() and
			not SystemDisabler.getOverrideServerConnectDebugCheck() then
		forceDisconnect()
		return
	end
	self.connecting = false
	self:setVisible(false)
	local joypadData = JoypadState.getMainMenuJoypad()
	if not checkSavePlayerExists() then
		if MapSpawnSelect.instance:hasChoices() then
			MapSpawnSelect.instance:fillList()
			MapSpawnSelect.instance:setVisible(true, joypadData)
		elseif WorldSelect.instance:hasChoices() then
			WorldSelect.instance:fillList()
			WorldSelect.instance:setVisible(true, joypadData)
		else
			MapSpawnSelect.instance:useDefaultSpawnRegion()
			MainScreen.instance.charCreationProfession.previousScreen = nil
			MainScreen.instance.charCreationProfession:setVisible(true, joypadData)
		end
	else
		GameWindow.doRenderEvent(false)
--[[
		-- menu activated via joypad, we disable the joypads and will re-set them automatically when the game is started
		if joypadData then
			joypadData.focus = nil
			updateJoypadFocus(joypadData)
			JoypadState.count = 0
			JoypadState.players = {}
			JoypadState.joypads = {}
			JoypadState.forceActivate = joypadData.id
		end
--]]
		forceChangeState(GameLoadingState.new())
	end
end

function ConnectToServer:OnConnectFailed(message, detail)
	-- Other screens have Events.OnConnectFailed callbacks too
	if not self:getIsVisible() then return end

	if message == "ServerWorkshopItemsCancelled" then
		self:onBackButton()
		return
	end

	-- AccessDenied has a message from the server telling the client why the connection is refused.
	-- But it is followed by ID_DISCONNECTION_NOTIFICATION which has no message.
	-- So keep the first message we get after clicking the connect button.
	if not self.failMessage then self.failMessage = message end
	if not message then message = self.failMessage end
	if message and string.match(message, "MODURL=") then
		local test = string.split(message, "MODURL=")
		message = test[1]
--[[
		self.getModBtn:setVisible(true)
		self.getModBtn:setX(5 + (getTextManager():MeasureStringX(UIFont.Medium, message)/2) + self.listbox.x + self.listbox.width/2)
		self.getModBtn.url = test[2]
--]]
	end

	message = message or getText("UI_servers_connectionfailed")

	if detail and detail ~= "" then
		message = message .. "\n" .. detail
	end
	
	self.connectLabel.name = message

	self.backBtn:setTitle(getText("UI_btn_back"))

	self.connecting = false
end

function ConnectToServer:OnConnectionStateChanged(state, message)
	if not self:getIsVisible() then return end
	print(state .. ',' .. tostring(message))
	if state == "Disconnected" then return end
	if state == "Disconnecting" then return end
	if state == "Failed" and message then self.failMessage = getText('UI_servers_'..message); return end
	-- Set connecting to false so we don't draw the > > > in render()
	if state == "Connected" then self.connecting = false end
	self.connectLabel.name = state and getText('UI_servers_'..state) or "???"
end

local function OnConnected()
	ConnectToServer.instance:OnConnected()
end

local function OnConnectFailed(message, detail)
	ConnectToServer.instance:OnConnectFailed(message, detail)
end

local function OnConnectionStateChanged(state, message)
	ConnectToServer.instance:OnConnectionStateChanged(state, message)
end

Events.OnConnected.Add(OnConnected)
Events.OnConnectFailed.Add(OnConnectFailed)
Events.OnDisconnect.Add(OnConnectFailed)
Events.OnConnectionStateChanged.Add(OnConnectionStateChanged)
