--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

BootstrapConnectPopup = ISPanel:derive("BootstrapConnectPopup")

function BootstrapConnectPopup:create()
	self.connectLabel = ISLabel:new(self.width / 2, 96, 50, "", 1, 1, 1, 1, UIFont.Medium, true)
	self.connectLabel:initialise()
	self.connectLabel:instantiate()
	self.connectLabel:setAnchorLeft(true)
	self.connectLabel:setAnchorRight(true)
	self.connectLabel:setAnchorTop(true)
	self.connectLabel:setAnchorBottom(false)
	self.connectLabel.center = true
	self:addChild(self.connectLabel)

	self.backBtn = ISButton:new((self.width - 70) / 2, self.height - 50, 70, 25, getText("UI_btn_back"), self, self.onBackButton)
	self.backBtn.internal = "BACK"
	self.backBtn:initialise()
	self.backBtn:instantiate()
	self.backBtn:setAnchorLeft(true)
	self.backBtn:setAnchorRight(true)
	self.backBtn:setAnchorTop(true)
	self.backBtn:setAnchorBottom(false)
	self:addChild(self.backBtn)

	BootstrapConnectPopup.instance = self

	if getSteamModeActive() then
		Events.OnSteamServerResponded2.Add(
			function(host, port, server2)
				BootstrapConnectPopup.instance:OnSteamServerResponded2(host, port, server2)
			end
		)
		Events.OnSteamServerFailedToRespond2.Add(
			function(host, port)
				BootstrapConnectPopup.instance:OnSteamServerFailedToRespond2(host, port)
			end
		)
	end
end

function BootstrapConnectPopup:prerender()
	BootstrapConnectPopup.instance = self
	ISPanel.prerender(self)
	self:drawTextCentre(getText("UI_BootstrapConnectPopup_Title"), self.width / 2, 20, 1, 1, 1, 1, UIFont.Large)
	self.backBtn:setEnable(not self.connecting)
end

function BootstrapConnectPopup:onBackButton()
	self:setVisible(false)
	MainScreen.instance.bottomPanel:setVisible(true)
end

function BootstrapConnectPopup:connect(host, port, serverPassword)
	if not type(port) == "string" then error "port must be a string" end
	if getSteamModeActive() then
		if isValidSteamID(host) then
			-- When the coop host sends an invite, the +connect string contains the server's Steam ID
			-- instead of an IP address.
			ConnectToServer.instance:connectCoop(self, host)
			return
		end
		-- We have the server's IP and port.  Now we must determine if the server we are joining
		-- is a coop server or not.  That is accomplished by querying the server details and
		-- checking if the string ";hosted" appears in the server's tags.
		self.connecting = true
		self.connectLabel.name = getText("UI_servers_Connecting")
		self.host = host
		self.port = tonumber(port)
		self.serverPassword = serverPassword
		self:setVisible(true)
		if not steamRequestServerDetails(host, tonumber(port)) then
			self.connecting = false
			self.connectLabel.name = "steamRequestServerDetails() returned false"
		end
	else
		if isValidSteamID(host) then
			-- A Steam coop host invited a non-Steam client to join. Fail!
			-- I couldn't make this situation happen.  When the client accepts the invite,
			-- Steam launches the game without the Launch Options (such as -nosteam).
			self:setVisible(true)
			self.connectLabel.name = getText("UI_mainscreen_InviteFromSteamToNoSteam")
			return
		end
		ServerConnectPopup.instance:setServer(host, port, serverPassword)
		ServerConnectPopup.instance:setVisible(true)
	end
end

function BootstrapConnectPopup:OnSteamServerResponded2(host, port, server2)
	if self.connecting and host == self.host and port == self.port then
		self.connecting = false
		self:setVisible(false)
		if server2:isHosted() then
			ConnectToServer.instance:connectCoop(self, server2:getSteamId())
		else
			ServerConnectPopup.instance:setServer(host, tostring(port), self.serverPassword)
			ServerConnectPopup.instance:setVisible(true)
		end
	end
end

function BootstrapConnectPopup:OnSteamServerFailedToRespond2(host, port)
	if self.connecting and host == self.host and port == self.port then
		self.connecting = false
		self.connectLabel.name = getText("UI_servers_ServerFailedToRespond")
	end
end
