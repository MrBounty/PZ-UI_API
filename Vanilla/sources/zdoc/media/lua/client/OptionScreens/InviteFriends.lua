--***********************************************************
--**                    Gennadii Potapov                   **
--***********************************************************

require "ISUI/ISScrollingListBox"

---@class InviteFriends : ISPanelJoypad
InviteFriends = ISPanelJoypad:derive("InviteFriends")

function InviteFriends:initialise()
	ISPanelJoypad.initialise(self)
end

function InviteFriends:hasChoices()
	local friends = getFriendsList()
	return friends and #friends > 1
end

function InviteFriends:fillList()
	self.listbox:clear()
	self.listbox.maxFriendWidth = 0
	local friends = getFriendsList()
	if not friends then return end
	table.sort(friends, function(a,b) return a:getName()<b:getName() end)
    local online = {};
    local offline = {};
    local others = {};
	for _,v in ipairs(friends) do
        local filter = string.trim(self.filterEntry:getInternalText());
        local add = true;
        if filter and filter ~= "" and not string.contains(string.lower(v:getName()), string.lower(filter)) then
            add = false;
        end
        if add then
            if v:getState() == "Online" or v:getState() == "LookingToTrade" or v:getState() == "LookingToPlay" then
                table.insert(online, v);
            elseif v:getState() == "Offline" then
                table.insert(offline, v);
            else
                table.insert(others, v);
            end
            local textWid = getTextManager():MeasureStringX(UIFont.Medium, v:getName())
            self.listbox.maxFriendWidth = math.max(self.listbox.maxFriendWidth, textWid)
        end
    end
    for _,v in ipairs(online) do
        self.listbox:addItem(v:getName(), v)
    end
    for _,v in ipairs(others) do
        self.listbox:addItem(v:getName(), v)
    end
    for _,v in ipairs(offline) do
        self.listbox:addItem(v:getName(), v)
    end
    self:loadInvitedFile()
end

function InviteFriends:refreshList()
	local selected = self.listbox.items[self.listbox.selected]
	local steamID = selected and selected.item:getSteamID()
	self:saveInvitedFile()
	self:fillList()
	if selected then
		for i=1,#self.listbox.items do
			if self.listbox.items[i].item:getSteamID() == steamID then
				self.listbox.selected = i
				break
			end
		end
	end
end

function InviteFriends:onOptionMouseDown(button, x, y)
	if button.internal == "BACK" then
		self:clickBack()
	elseif button.internal == "INVITE" then
		self:clickInvite()
	elseif button.internal == "ALLOW" then
		self:toggleAllowDeny(self.listbox.selected)
	end
end

function InviteFriends:onDblClick()
	if not self.listbox.mouseOverButtonIndex then
		self:clickInvite()
	end
end

function InviteFriends:clickBack()
	self:saveInvitedFile()
	MainScreen.instance.inviteFriends:setVisible(false);
	MainScreen.instance.bottomPanel:setVisible(true);
	if self.joyfocus then
		self.joyfocus.focus = MainScreen.instance
		updateJoypadFocus(self.joyfocus)
	end
end

function InviteFriends:clickInvite()
	self.selectedFriend = self.listbox.items[self.listbox.selected].item;
	self.invited[self.selectedFriend:getSteamID()] = true
	inviteFriend(self.selectedFriend:getSteamID());
	self.statusLabel.name = getText("UI_InviteFriends_Invited", self.selectedFriend:getName());
	self.inviteTime = getTimestamp()
end

if getDebug() then -- aids reloading the lua file
function InviteFriends:update()
	InviteFriends.instance = self
	self.listbox.doDrawItem = self.doDrawItem
end
end

function InviteFriends:prerender()
    self.backgroundColor.a = 0.8
	self.listbox.mouseOverButtonIndex = nil
	ISPanelJoypad.prerender(self)
	InviteFriends.instance = self
	self.listbox.doDrawItem = self.doDrawItem
	self.listbox.onMouseDown = self.onMouseDown_ListBox
	if self.inviteTime and (getTimestamp() > self.inviteTime + 3) then
		self.statusLabel.name = ""
		self.inviteTime = nil
	end
	if self.listbox.items[self.listbox.selected] then
		self.inviteButton:setEnable(true)
		if self.listbox.joyfocus then
			local steamID = self.listbox.items[self.listbox.selected].item:getSteamID()
			if self.invited[steamID] then
				self.allowButton.title = getText("UI_InviteFriends_ButtonDeny")
			else
				self.allowButton.title = getText("UI_InviteFriends_ButtonAllow")
			end
		end
		self.inviteButton.tooltip = self.listbox.items[self.listbox.selected].text
	else
		self.inviteButton:setEnable(false)
		self.inviteButton.tooltip = nil
	end
end

function InviteFriends:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_InviteFriends_Title"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Large)
	self:drawRectBorder(self.listbox:getX(), self.listbox:getY(), self.listbox:getWidth(), self.listbox:getHeight(), 0.9, 0.4, 0.4, 0.4)

    self:drawText(getText("IGUI_CraftUI_Name_Filter"), self.filterEntry.x, self.filterEntry.y - 18, 1, 1, 1, 1, UIFont.Small)
end

function InviteFriends:doDrawItem(y, item, alt)
	if self.selected == item.index then
		self:drawRect(0, (y), self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
	end
	self:drawRectBorder(0, (y), self:getWidth(), item.height, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b)
    local fontHgtLarge = getTextManager():getFontFromEnum(UIFont.Large):getLineHeight()
	local avatarW = 32
	local avatarH = 32
	if item.item:getAvatar() then
		self:drawTexture(item.item:getAvatar(), 4, y + (item.height - 32) / 2, avatarW, avatarH, 1, 1, 1, 1)
	end
	self:drawText(item.text, 4 + avatarW + 8, y + (item.height - fontHgtLarge) / 2, 0.9, 0.9, 0.9, 0.9, UIFont.Large)
    local fontHgtMedium = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local x = 4 + avatarW + 8 + self.maxFriendWidth + 48
    local r,g,b = 0.6,0.6,0.6;
    if item.item:getState() == "Offline" then
        r,g,b = 0.8,0.2,0.2;
    elseif item.item:getState() == "Online" then
        r,g,b = 0.2,0.8,0.2;
    else
        r,g,b = 0.8,0.6,0.1;
    end
	self:drawText(getText("UI_FriendState_" .. item.item:getState()), x, y + (item.height - fontHgtMedium) / 2,r,g,b, 0.9, UIFont.Medium)

	if self.parent.isCoopHost then
		local isInvited = self.parent.invited[item.item:getSteamID()]
		local text = isInvited and getText("UI_InviteFriends_Allow") or getText("UI_InviteFriends_Deny")
		local textWid1 = getTextManager():MeasureStringX(UIFont.Small, getText("UI_InviteFriends_Allow"))
		local textWid2 = getTextManager():MeasureStringX(UIFont.Small, getText("UI_InviteFriends_Deny"))
		local textWid = math.max(textWid1, textWid2)
		local smallFontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()
		local buttonWid = 8 + textWid + 8
		local buttonHgt = smallFontHgt + 4
		local scrollBarWid = self:isVScrollBarVisible() and 13 or 0
		local buttonX = self.width - 4 - scrollBarWid - buttonWid
		local buttonY = y + (item.height - buttonHgt) / 2
		local isMouseOverButton = (self.mouseoverselected == item.index) and not self:isMouseOverScrollBar() and (self:getMouseX() > buttonX - 8)
		if isMouseOverButton then
			local argb = isInvited and { 1, 0, 0.85, 0 } or { 1, 0.5, 0.5, 0.5 }
			self:drawRect(buttonX, buttonY, buttonWid, buttonHgt, argb[1], argb[2], argb[3], argb[4])
			self.mouseOverButtonIndex = item.index
		else
			local argb = isInvited and { 1, 0, 0.65, 0 } or { 1, 0.3, 0.3, 0.3 }
			self:drawRect(buttonX, buttonY, buttonWid, buttonHgt, argb[1], argb[2], argb[3], argb[4])
		end
		self:drawTextCentre(text, buttonX +  buttonWid / 2, y + (item.height - smallFontHgt) / 2 , 0, 0, 0, 1)
	end

	y = y + item.height
	return y
end

function InviteFriends:onMouseDown_ListBox(x, y)
	if self.mouseOverButtonIndex then
		self.parent:toggleAllowDeny(self.mouseOverButtonIndex)
	else
		ISScrollingListBox.onMouseDown(self, x, y)
	end
end

function InviteFriends:toggleAllowDeny(index)
	if self.listbox.items[index] then
		local steamID = self.listbox.items[index].item:getSteamID()
		if self.invited[steamID] then
			self.invited[steamID] = nil
			if self.isCoopHost then
				CoopServer:sendMessage("invite-remove", steamID)
			end
		else
			self.invited[steamID] = true
			if self.isCoopHost then
				CoopServer:sendMessage("invite-add", steamID)
			end
		end
	end
end

function InviteFriends:onGainJoypadFocus(joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData)
    joypadData.focus = self.listbox
    updateJoypadFocus(joypadData)
    self.listbox:setISButtonForA(self.inviteButton)
    self.listbox:setISButtonForB(self.backButton)
	if self.isCoopHost then
		self.allowButton:setVisible(true)
		self.listbox:setISButtonForX(self.allowButton)
	end
end

function InviteFriends.OnSteamFriendStatusChanged(steamID)
	if not InviteFriends.instance then return end
	if not InviteFriends.instance:getIsVisible() then return end
	InviteFriends.instance:refreshList()
end

function InviteFriends:loadInvitedFile()
	self.invited = {}
	if not self.isCoopHost then return end
	local reader = getFileReader("invited.ini", true)
	while true do
		local line = reader:readLine()
		if not line then
			reader:close()
			break
		end
		local steamID = line:trim()
		if isValidSteamID(steamID) then
			self.invited[steamID] = true
			CoopServer:sendMessage("invite-add", steamID)
		end
	end
end

function InviteFriends:saveInvitedFile()
	if not self.isCoopHost then return end
	local writer = getFileWriter("invited.ini", true, false)
	for steamID,_ in pairs(self.invited) do
		writer:write(steamID .. "\r\n")
	end
	writer:close()
end

function InviteFriends:create()
	local pad = 16
	local btnWid = 100
	local btnHgt = 25
	local titleHgt = 10 + getTextManager():getFontFromEnum(UIFont.Large):getLineHeight() + 5
	local avatarH = 32

	self.listbox = ISScrollingListBox:new(pad, titleHgt, self.width-pad*2, self.height-50-pad*2-btnHgt-titleHgt)
	self.listbox:initialise()
    self.listbox.backgroundColor.a = 0.0
	self.listbox:setAnchorRight(true)
	self.listbox:setAnchorBottom(true)
	self.listbox.doDrawItem = InviteFriends.doDrawItem
	self.listbox.onMouseDOwn = self.onMouseDown_ListBox
	self.listbox:setOnMouseDoubleClick(self, InviteFriends.onDblClick)
	self.listbox.itemheight = 2 + avatarH + 2
	self:addChild(self.listbox)

    self.filterEntry = ISTextEntryBox:new("", self.listbox.x, self.listbox.y + self.listbox.height + 25, 100, 18);
    self.filterEntry:initialise();
    self.filterEntry:instantiate();
    self.filterEntry:setText("");
    self.filterEntry.onTextChange = InviteFriends.filter;
    self:addChild(self.filterEntry);

	self.backButton = ISButton:new(16, self.height - pad - btnHgt, 100, 25, getText("UI_btn_back"), self, InviteFriends.onOptionMouseDown)
	self.backButton.internal = "BACK"
	self.backButton:initialise()
	self.backButton:instantiate()
	self.backButton:setAnchorLeft(true)
	self.backButton:setAnchorTop(false)
	self.backButton:setAnchorBottom(true)
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.backButton)

	self.inviteButton = ISButton:new(self.width - 116, self.height - pad - btnHgt, 100, 25, getText("UI_InviteFriends_ButtonInvite"), self, InviteFriends.onOptionMouseDown)
	self.inviteButton.internal = "INVITE"
	self.inviteButton:initialise()
	self.inviteButton:instantiate()
	self.inviteButton:setAnchorLeft(false)
	self.inviteButton:setAnchorRight(true)
	self.inviteButton:setAnchorTop(false)
	self.inviteButton:setAnchorBottom(true)
	self.inviteButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.inviteButton)

	self.allowButton = ISButton:new(self.inviteButton:getX() - 24 - 100, self.height - pad - btnHgt, 100, 25, getText("UI_InviteFriends_ButtonAllow"), self, InviteFriends.onOptionMouseDown)
	self.allowButton.internal = "ALLOW"
	self.allowButton:initialise()
	self.allowButton:instantiate()
	self.allowButton:setAnchorLeft(false)
	self.allowButton:setAnchorRight(true)
	self.allowButton:setAnchorTop(false)
	self.allowButton:setAnchorBottom(true)
	self.allowButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.allowButton)
	self.allowButton:setVisible(false)

	self.statusLabel = ISLabel:new(self.listbox.x + self.listbox.width/2, self.height - pad - 40, 40, "", 1, 1, 1, 1, UIFont.Medium);
	self.statusLabel:initialise();
	self.statusLabel:instantiate();
	self.statusLabel:setAnchorLeft(false);
	self.statusLabel:setAnchorRight(false);
	self.statusLabel:setAnchorTop(false);
	self.statusLabel:setAnchorBottom(true);
	self.statusLabel.center = true;
	self:addChild(self.statusLabel);

	self.isCoopHost = CoopServer:isRunning()
	self:loadInvitedFile()
end

function InviteFriends:filter()
    InviteFriends.instance:fillList();
end

function InviteFriends:new(x, y, width, height)
	local o = ISPanelJoypad.new(self, x, y, width, height)
	o.selectedFriend = nil
	InviteFriends.instance = o
	return o
end

if getSteamModeActive() then
	LuaEventManager.AddEvent("OnSteamFriendStatusChanged")
	Events.OnSteamFriendStatusChanged.Add(InviteFriends.OnSteamFriendStatusChanged)
end
