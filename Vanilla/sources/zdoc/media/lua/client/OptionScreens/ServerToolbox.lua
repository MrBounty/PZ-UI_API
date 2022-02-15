--***********************************************************
--**                    Gennadii Potapov                   **
--***********************************************************

require "ISUI/ISScrollingListBox"

---@class ServerToolbox : ISPanelJoypad
ServerToolbox = ISPanelJoypad:derive("ServerToolbox")

function ServerToolbox:initialise()
	ISPanelJoypad.initialise(self)
    self.items = {}
    print("*** TOOLBOX INITIALISED ***");
    self.create();
end

function ServerToolbox:hasChoices()
	return #self.items > 1
end

function ServerToolbox:useDefaultSpawnRegion()
	self.selectedItem = nil
	if not self.items or #self.items == 0 then return end
	self.selectedItem = self.items[1]
	return self.selectedItem
end

function ServerToolbox:fillList()
	self.listbox:clear()
	for _,v in ipairs(self.items) do
		self.listbox:addItem(v, v);
	end
end

function ServerToolbox:onOptionMouseDown(button, x, y)
	if button.internal == "BACK" then
		self:clickBack()
	elseif button.internal == "ALLOW" then
		self:clickAllow()
	end
end

function ServerToolbox:onDblClick()
	self:clickAllow()
end

function ServerToolbox:clickBack()
	MainScreen.instance.ServerToolbox:setVisible(false);
	MainScreen.instance.bottomPanel:setVisible(true);
end

function ServerToolbox:clickAllow()
	self.selectedItem = self.listbox.items[self.listbox.selected].item;
	--inviteFriend(self.selectedFriend:getSteamID());
	self.statusLabel.name = self.selectedItem .." allowed!";
end

function ServerToolbox:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre("INCOMING CONNECTIONS", self.width / 2, 10, 1, 1, 1, 1, UIFont.Large)
    if self.listbox ~= nil then
    	self:drawRectBorder(self.listbox:getX(), self.listbox:getY(), self.listbox:getWidth(), self.listbox:getHeight(), 0.9, 0.4, 0.4, 0.4)
    end
end

function ServerToolbox:doDrawItem(y, item, alt)
	if self.selected == item.index then
		self:drawRect(0, (y), self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
	end
	self:drawRectBorder(0, (y), self:getWidth(), item.height, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b)
	self:drawText(item.text, 15, (y)+6, 0.9, 0.9, 0.9, 0.9, UIFont.Large)
	y = y + item.height
	return y
end

function ServerToolbox:onGainJoypadFocus(joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData)
    joypadData.focus = self.listbox
    updateJoypadFocus(joypadData)
    self.listbox:setISButtonForA(self.allowButton)
    self.listbox:setISButtonForB(self.backButton)
end

function ServerToolbox:create()
	local pad = 16
	local btnWid = 100
	local btnHgt = 25
	local titleHgt = 10 + getTextManager():getFontFromEnum(UIFont.Large):getLineHeight() + 5

	self.listbox = ISScrollingListBox:new(pad, titleHgt, self.width-pad*2, self.height-pad*2-btnHgt-titleHgt)
	self.listbox:initialise()
	self.listbox:setAnchorRight(true)
	self.listbox:setAnchorBottom(true)
	self.listbox.doDrawItem = ServerToolbox.doDrawItem
	self.listbox:setOnMouseDoubleClick(self, ServerToolbox.onDblClick)
	self:addChild(self.listbox)

	self.backButton = ISButton:new(16, self.height - pad - btnHgt, 100, 25, getText("UI_btn_back"), self, ServerToolbox.onOptionMouseDown)
	self.backButton.internal = "BACK"
	self.backButton:initialise()
	self.backButton:instantiate()
	self.backButton:setAnchorLeft(true)
	self.backButton:setAnchorTop(false)
	self.backButton:setAnchorBottom(true)
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.backButton)

	self.allowButton = ISButton:new(self.width - 116, self.height - pad - btnHgt, 100, 25, "ALLOW", self, ServerToolbox.onOptionMouseDown)
	self.allowButton.internal = "ALLOW"
	self.allowButton:initialise()
	self.allowButton:instantiate()
	self.allowButton:setAnchorLeft(false)
	self.allowButton:setAnchorRight(true)
	self.allowButton:setAnchorTop(false)
	self.allowButton:setAnchorBottom(true)
	self.allowButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.allowButton)

	self.statusLabel = ISLabel:new(self.listbox.x + self.listbox.width/2, self.height - pad - 40, 40, "", 1, 1, 1, 1, UIFont.Medium);
	self.statusLabel:initialise();
	self.statusLabel:instantiate();
	self.statusLabel:setAnchorLeft(false);
	self.statusLabel:setAnchorRight(false);
	self.statusLabel:setAnchorTop(false);
	self.statusLabel:setAnchorBottom(true);
	self.statusLabel.center = true;
	self:addChild(self.statusLabel);

    print("Server toolbox create");

end

function ServerToolbox:new(x, y, width, height)
	local o = ISPanelJoypad.new(self, x, y, width, height)
	o.selectedItem = nil
	ServerToolbox.instance = o
	return o
end

