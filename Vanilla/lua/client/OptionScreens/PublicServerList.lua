--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 23/01/14
-- Time: 12:49
-- To change this template use File | Settings | File Templates.
--

PublicServerList = ISPanelJoypad:derive("PublicServerList");
PublicServerList.pingedList = {};
PublicServerList.refreshTime = 0;
PublicServerList.refreshInterval = getSteamModeActive() and 5 or 60;

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

function PublicServerList:create()
    if not isPublicServerListAllowed() then return; end

    local bottomPad = 3
    local buttonsHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local connectLabelHgt = 4 * 2 + getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
    local filterEtcHgt = math.max(FONT_HGT_SMALL, 18)

    local fontScale = FONT_HGT_SMALL / 15
    local entrySize = 200 * fontScale;
    local entryX = self.width - 10 - entrySize;
    local labelX = self.width - 10 - entrySize;

    local listX = 16
    local listWid = self.width - 10 - entrySize - 10 - listX
    local tabHeight = FONT_HGT_SMALL + 6
    local listTop = 14 + tabHeight

    self.tabs = ISTabPanel:new(listX, listTop - tabHeight, listWid, tabHeight);
    self.tabs:initialise();
    self.tabs:setAnchorBottom(false);
    self.tabs:setAnchorRight(true);
    --	self.tabs.borderColor = { r = 0, g = 0, b = 0, a = 0};
    self.tabs.onMouseDown = self.onMouseDown_Tabs;
    self.tabs.onMouseUp = function(x,y) end
--    self.tabs.target = self;
    self.tabs:setEqualTabWidth(false)
    self.tabs.tabPadX = 40
    self.tabs:setCenterTabs(true)
    self.tabs.tabHeight = tabHeight
    self:addChild(self.tabs);

    self.tabs:addView(getText("UI_servers_serverlist"), ISUIElement:new(0,0,100,100))
    self.tabs:addView(getText("UI_servers_publicServer"), ISUIElement:new(0,0,100,100))
    self.tabs:activateView(getText("UI_servers_publicServer"))

    self.listbox = ISScrollingListBox:new(listX, listTop, listWid, self.height-bottomPad-buttonsHgt-bottomPad-connectLabelHgt-bottomPad-filterEtcHgt-bottomPad-listTop);
    self.listbox:initialise();
    self.listbox:instantiate();
    self.listbox:setAnchorLeft(true);
    self.listbox:setAnchorRight(true);
    self.listbox:setAnchorTop(true);
    self.listbox:setAnchorBottom(true);
    self.listbox.itemheight = 110;
    self.listbox.doDrawItem = PublicServerList.drawMap;
    self.listbox:setOnMouseDoubleClick(self, PublicServerList.addServer);
    self.listbox:setOnMouseDownFunction(self, PublicServerList.onClickServer);
    self.listbox.drawBorder = true
    self.listbox.lockTexture = getTexture("media/ui/lock.png")

    self:addChild(self.listbox);

    local y = listTop;
    local entryHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight() + 2 * 2

    local labelHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
    local gapLabelY = 2
	local gapEntryY = 10

    self.scrollPanel = ISPanelJoypad:new(labelX, y, entrySize, self.listbox.height)
    self.scrollPanel:initialise()
    self.scrollPanel:instantiate()
    self.scrollPanel:setAnchorLeft(false);
    self.scrollPanel:setAnchorRight(true);
    self.scrollPanel:setAnchorTop(true);
    self.scrollPanel:setAnchorBottom(true);
    self.scrollPanel:setScrollChildren(true)
    self.scrollPanel:noBackground()
    self.scrollPanel:addScrollBars()
    self.scrollPanel.vscroll.doRepaintStencil = true
    self:addChild(self.scrollPanel)
    self.scrollPanel.prerender = function(self)
        self:setStencilRect(0, 0, self:getWidth(), self:getHeight())
        ISPanelJoypad.prerender(self)
    end
    self.scrollPanel.render = function(self)
        ISPanelJoypad.render(self)
        self:clearStencilRect()
    end
    self.scrollPanel.onMouseWheel = function(self, del)
        if self:getScrollHeight() > 0 then
            self:setYScroll(self:getYScroll() - (del * 40))
            return true
        end
        return false
    end

    entryX = 0
    entrySize = entrySize - 17 - entryX
    labelX = 0
    y = 0

    self.serverNameLabel = ISLabel:new(labelX, y, labelHgt, getText("UI_servers_servername") .. ": ", 1, 1, 1, 1, UIFont.Medium, true);
    self.serverNameLabel:initialise();
    self.serverNameLabel:instantiate();
    self.serverNameLabel:setAnchorLeft(false);
    self.serverNameLabel:setAnchorRight(true);
    self.serverNameLabel:setAnchorTop(true);
    self.serverNameLabel:setAnchorBottom(false);
    self.scrollPanel:addChild(self.serverNameLabel);

    y = y + labelHgt + gapLabelY;

    self.serverNameEntry = ISTextEntryBox:new("", entryX, y, entrySize, entryHgt);
    self.serverNameEntry:initialise();
    self.serverNameEntry:instantiate();
    self.serverNameEntry:setAnchorLeft(false);
    self.serverNameEntry:setAnchorRight(true);
    self.serverNameEntry:setAnchorTop(true);
    self.serverNameEntry:setAnchorBottom(false);
    self.scrollPanel:addChild(self.serverNameEntry);

    y = y + entryHgt + gapEntryY;

    self.serverLabel = ISLabel:new(labelX, y, labelHgt, getText("UI_servers_IP"), 1, 1, 1, 1, UIFont.Medium, true);
    self.serverLabel:initialise();
    self.serverLabel:instantiate();
    self.serverLabel:setAnchorLeft(false);
    self.serverLabel:setAnchorRight(true);
    self.serverLabel:setAnchorTop(true);
    self.serverLabel:setAnchorBottom(false);
    self.scrollPanel:addChild(self.serverLabel);

    y = y + labelHgt + gapLabelY;

    self.serverEntry = ISTextEntryBox:new("127.0.0.1", entryX, y, entrySize, entryHgt);
    self.serverEntry:initialise();
    self.serverEntry:instantiate();
    self.serverEntry:setAnchorLeft(false);
    self.serverEntry:setAnchorRight(true);
    self.serverEntry:setAnchorTop(true);
    self.serverEntry:setAnchorBottom(false);
    self.scrollPanel:addChild(self.serverEntry);

    y = y + entryHgt + gapEntryY;

    self.portLabel = ISLabel:new(labelX, y, labelHgt, getText("UI_servers_Port"), 1, 1, 1, 1, UIFont.Medium, true);
    self.portLabel:initialise();
    self.portLabel:instantiate();
    self.portLabel:setAnchorLeft(false);
    self.portLabel:setAnchorRight(true);
    self.portLabel:setAnchorTop(true);
    self.portLabel:setAnchorBottom(false);
    self.scrollPanel:addChild(self.portLabel);

    y = y + labelHgt + gapLabelY;

    self.portEntry = ISTextEntryBox:new("16261", entryX, y, entrySize, entryHgt);
    self.portEntry:initialise();
    self.portEntry:instantiate();
    self.portEntry:setAnchorLeft(false);
    self.portEntry:setAnchorRight(true);
    self.portEntry:setAnchorTop(true);
    self.portEntry:setAnchorBottom(false);
    self.scrollPanel:addChild(self.portEntry);

    y = y + entryHgt + gapEntryY;
        
    local label = ISLabel:new(labelX, y, labelHgt, getText("UI_servers_serverpwd"), 1, 1, 1, 1, UIFont.Medium, true);
    label:initialise();
    label:instantiate();
    label:setAnchorLeft(false);
    label:setAnchorRight(true);
    label:setAnchorTop(true);
    label:setAnchorBottom(false);
    self.scrollPanel:addChild(label);

    y = y + labelHgt + gapLabelY;

    local entry = ISTextEntryBox:new("", entryX, y, entrySize, entryHgt);
    entry:initialise();
    entry:instantiate();
    entry:setAnchorLeft(false);
    entry:setAnchorRight(true);
    entry:setAnchorTop(true);
    entry:setAnchorBottom(false);
    entry:setMasked(true);
    entry:setTooltip(getText("UI_servers_serverpwd_tt"))
    self.scrollPanel:addChild(entry);
    self.serverPasswordEntry = entry

    y = y + entryHgt + gapEntryY;

--[[
    -- Steam ID label/field

    self.steamIdLabel = ISLabel:new(labelX, y, labelHgt, "Steam ID", 1, 1, 1, 1, UIFont.Medium, true);
    self.steamIdLabel:initialise();
    self.steamIdLabel:instantiate();
    self.steamIdLabel:setAnchorLeft(false);
    self.steamIdLabel:setAnchorRight(true);
    self.steamIdLabel:setAnchorTop(true);
    self.steamIdLabel:setAnchorBottom(false);
    self.steamIdLabel:setVisible(false);
    self:addChild(self.steamIdLabel);

    y = y + labelHgt + gapLabelY;

    self.steamIdEntry = ISTextEntryBox:new("", entryX, y, entrySize, entryHgt);
    self.steamIdEntry:initialise();
    self.steamIdEntry:instantiate();
    self.steamIdEntry:setAnchorLeft(false);
    self.steamIdEntry:setAnchorRight(true);
    self.steamIdEntry:setAnchorTop(true);
    self.steamIdEntry:setAnchorBottom(false);
    self.steamIdEntry:setVisible(false);
    self:addChild(self.steamIdEntry);

    y = y + entryHgt + gapEntryY;
--]]
    self.descLabel = ISLabel:new(labelX, y, labelHgt, getText("UI_servers_desc") .. ": ", 1, 1, 1, 1, UIFont.Medium, true);
    self.descLabel:initialise();
    self.descLabel:instantiate();
    self.descLabel:setAnchorLeft(false);
    self.descLabel:setAnchorRight(true);
    self.descLabel:setAnchorTop(true);
    self.descLabel:setAnchorBottom(false);
    self.scrollPanel:addChild(self.descLabel);

    y = y + labelHgt + gapLabelY;

    self.descEntry = ISTextEntryBox:new("", entryX, y, entrySize, entryHgt);
    self.descEntry:initialise();
    self.descEntry:instantiate();
    self.descEntry:setAnchorLeft(false);
    self.descEntry:setAnchorRight(true);
    self.descEntry:setAnchorTop(true);
    self.descEntry:setAnchorBottom(false);
    self.scrollPanel:addChild(self.descEntry);

    y = y + entryHgt + gapEntryY;

    --    self.usernameLabel = ISLabel:new(labelX, y, 50, getText("UI_servers_username") .. ": ", 1, 1, 1, 1, UIFont.Medium, true);
    self.usernameLabel = ISLabel:new(labelX, y, labelHgt, getText("UI_servers_username"), 1, 1, 1, 1, UIFont.Medium, true);
    self.usernameLabel:initialise();
    self.usernameLabel:instantiate();
    self.usernameLabel:setAnchorLeft(false);
    self.usernameLabel:setAnchorRight(true);
    self.usernameLabel:setAnchorTop(true);
    self.usernameLabel:setAnchorBottom(false);
    self.scrollPanel:addChild(self.usernameLabel);

    y = y + labelHgt + gapLabelY;

    self.usernameEntry = ISTextEntryBox:new("", entryX, y, entrySize, entryHgt);
    self.usernameEntry:initialise();
    self.usernameEntry:instantiate();
    self.usernameEntry:setAnchorLeft(false);
    self.usernameEntry:setAnchorRight(true);
    self.usernameEntry:setAnchorTop(true);
    self.usernameEntry:setAnchorBottom(false);
    self.scrollPanel:addChild(self.usernameEntry);

    y = y + entryHgt + gapEntryY;

    --    self.passwordLabel = ISLabel:new(labelX, y, 50, getText("UI_servers_pwd") .. ": ", 1, 1, 1, 1, UIFont.Medium, true);
    self.passwordLabel = ISLabel:new(labelX, y, labelHgt, getText("UI_servers_pwd"), 1, 1, 1, 1, UIFont.Medium, true);
    self.passwordLabel:initialise();
    self.passwordLabel:instantiate();
    self.passwordLabel:setAnchorLeft(false);
    self.passwordLabel:setAnchorRight(true);
    self.passwordLabel:setAnchorTop(true);
    self.passwordLabel:setAnchorBottom(false);
    self.scrollPanel:addChild(self.passwordLabel);

    y = y + labelHgt + gapLabelY;

    self.passwordEntry = ISTextEntryBox:new("", entryX, y, entrySize, entryHgt);
    self.passwordEntry:initialise();
    self.passwordEntry:instantiate();
    self.passwordEntry:setAnchorLeft(false);
    self.passwordEntry:setAnchorRight(true);
    self.passwordEntry:setAnchorTop(true);
    self.passwordEntry:setAnchorBottom(false);
    self.passwordEntry:setMasked(true);
    self.passwordEntry:setTooltip(getText("UI_servers_pwd_tt"))
    self.scrollPanel:addChild(self.passwordEntry);

    y = y + entryHgt;

    y = y + 20

    self.eraseBtn = ISButton:new(labelX, y, 70, buttonsHgt, getText("UI_servers_erase"), self, PublicServerList.onOptionMouseDown);
    self.eraseBtn.internal = "ERASE";
    self.eraseBtn:initialise();
    self.eraseBtn:instantiate();
    self.eraseBtn:setAnchorLeft(false);
    self.eraseBtn:setAnchorRight(true);
    self.eraseBtn:setAnchorTop(false);
    self.eraseBtn:setAnchorBottom(false);
    self.scrollPanel:addChild(self.eraseBtn);

    self.addBtn = ISButton:new(self.scrollPanel.width - 70 - 10, y, 70, buttonsHgt, getText("UI_servers_save"), self, PublicServerList.onOptionMouseDown);
    self.addBtn.internal = "ADD";
    self.addBtn:initialise();
    self.addBtn:instantiate();
    self.addBtn:setAnchorLeft(false);
    self.addBtn:setAnchorRight(true);
    self.addBtn:setAnchorTop(false);
    self.addBtn:setAnchorBottom(false);
    self.addBtn:setWidthToTitle(70)
    self.addBtn:setX(self.scrollPanel.width - 17 - self.addBtn:getWidth())
    self.scrollPanel:addChild(self.addBtn);

    self.scrollPanel:setScrollHeight(y + buttonsHgt + 10)

    local filterEtcY = self.height-bottomPad-buttonsHgt-bottomPad-connectLabelHgt-bottomPad-filterEtcHgt

    self.versionCheckBox = ISTickBox:new(listX, filterEtcY, 10, filterEtcHgt, "", nil, nil);
    self.versionCheckBox:initialise();
    self.versionCheckBox:instantiate();
    self.versionCheckBox:setAnchorLeft(true);
    self.versionCheckBox:setAnchorRight(false);
    self.versionCheckBox:setAnchorTop(false);
    self.versionCheckBox:setAnchorBottom(true);
    self.versionCheckBox.autoWidth = true
    self:addChild(self.versionCheckBox);
    self.versionCheckBox:addOption(getText("UI_servers_versionCheck"));

    self.emptyServer = ISTickBox:new(self.versionCheckBox:getRight() + 6, filterEtcY, 10, filterEtcHgt, "", nil, nil);
    self.emptyServer:initialise();
    self.emptyServer:instantiate();
    self.emptyServer:setAnchorLeft(true);
    self.emptyServer:setAnchorRight(false);
    self.emptyServer:setAnchorTop(false);
    self.emptyServer:setAnchorBottom(true);
    self.emptyServer.selected[1] = true;
    self.emptyServer.autoWidth = true
    self:addChild(self.emptyServer);
    self.emptyServer:addOption(getText("UI_servers_showEmptyServer"));

    self.whitelistServer = ISTickBox:new(self.emptyServer:getRight() + 6, filterEtcY, 10, filterEtcHgt, "", nil, nil);
    self.whitelistServer:initialise();
    self.whitelistServer:instantiate();
    self.whitelistServer:setAnchorLeft(true);
    self.whitelistServer:setAnchorRight(false);
    self.whitelistServer:setAnchorTop(false);
    self.whitelistServer:setAnchorBottom(true);
    self.whitelistServer.selected[1] = false;
    self.whitelistServer.autoWidth = true
    self:addChild(self.whitelistServer);
    self.whitelistServer:addOption(getText("UI_servers_showWhitelistServer"));

    self.pwdProtected = ISTickBox:new(self.whitelistServer:getRight() + 6, filterEtcY, 10, filterEtcHgt, "", nil, nil);
    self.pwdProtected:initialise();
    self.pwdProtected:instantiate();
    self.pwdProtected:setAnchorLeft(true);
    self.pwdProtected:setAnchorRight(false);
    self.pwdProtected:setAnchorTop(false);
    self.pwdProtected:setAnchorBottom(true);
    self.pwdProtected.selected[1] = false;
    self.pwdProtected.autoWidth = true
    self:addChild(self.pwdProtected);
    self.pwdProtected:addOption(getText("UI_servers_showPwdProtectedServer"));

    self.filterLabel = ISLabel:new(listX, self.versionCheckBox:getBottom() + 2, FONT_HGT_SMALL + 2 * 2, getText("UI_servers_nameFilter"), 1, 1, 1, 1, UIFont.Small, true);
    self.filterLabel:initialise();
    self.filterLabel:instantiate();
    self.filterLabel:setAnchorLeft(true);
    self.filterLabel:setAnchorRight(false);
    self.filterLabel:setAnchorTop(false);
    self.filterLabel:setAnchorBottom(true);
    self:addChild(self.filterLabel);

    self.filterEntry = ISTextEntryBox:new("", self.filterLabel:getRight() + 4, self.filterLabel:getY(), 130, FONT_HGT_SMALL + 2 * 2);
    self.filterEntry:initialise();
    self.filterEntry:instantiate();
    self.filterEntry:setAnchorLeft(true);
    self.filterEntry:setAnchorRight(false);
    self.filterEntry:setAnchorTop(false);
    self.filterEntry:setAnchorBottom(true);
    self:addChild(self.filterEntry);

    local buttonY = self.height - 5 - buttonsHgt

    self.backButton = ISButton:new(16, buttonY, 100, buttonsHgt, getText("UI_btn_back"), self, PublicServerList.onOptionMouseDown);
    self.backButton.internal = "BACK";
    self.backButton:initialise();
    self.backButton:instantiate();
    self.backButton:setAnchorLeft(true);
    self.backButton:setAnchorTop(false);
    self.backButton:setAnchorBottom(true);
    self:addChild(self.backButton);

    self.playButton = ISButton:new(self.listbox.x + self.listbox.width - 100, buttonY, 100, buttonsHgt, getText("UI_servers_joinServer"), self, PublicServerList.onOptionMouseDown);
    self.playButton.internal = "NEXT";
    self.playButton:initialise();
    self.playButton:instantiate();
    self.playButton:setAnchorLeft(false);
    self.playButton:setAnchorRight(true);
    self.playButton:setAnchorTop(false);
    self.playButton:setAnchorBottom(true);
    self.playButton:setWidthToTitle(100)
    self.playButton:setX(self.listbox:getRight() - self.playButton:getWidth())
    self:addChild(self.playButton);

    self.savedBtn = ISButton:new(self.backButton:getRight() + 10, buttonY, 100, buttonsHgt, getText("UI_servers_savedServers"), self, PublicServerList.onOptionMouseDown);
    self.savedBtn.internal = "SAVEDSERVERS";
    self.savedBtn:initialise();
    self.savedBtn:instantiate();
    self.savedBtn:setAnchorLeft(true);
    self.savedBtn:setAnchorRight(false);
    self.savedBtn:setAnchorTop(false);
    self.savedBtn:setAnchorBottom(true);
--    self:addChild(self.savedBtn);

    self.refreshBtn = ISButton:new(self.savedBtn:getRight() + 10, buttonY, 90, buttonsHgt, getText("UI_servers_refresh"), self, PublicServerList.onOptionMouseDown);
    self.refreshBtn.internal = "REFRESH";
    self.refreshBtn:initialise();
    self.refreshBtn:instantiate();
    self.refreshBtn:setAnchorLeft(true);
    self.refreshBtn:setAnchorRight(false);
    self.refreshBtn:setAnchorTop(false);
    self.refreshBtn:setAnchorBottom(true);
    self:addChild(self.refreshBtn);

    if #self.listbox.items > 0 then
        self:fillFields(self.listbox.items[1].item.server);
    end
end

function PublicServerList:onMouseDown_Tabs(x, y)
    if self:getMouseY() >= 0 and self:getMouseY() < self.tabHeight then
        local tabIndex = self:getTabIndexAtX(self:getMouseX())
        if tabIndex == 1 then
            if getSteamModeActive() then
                steamReleaseInternetServersRequest()
            end
            MainScreen.instance.joinPublicServer:setVisible(false)
            MainScreen.instance.joinServer:setVisible(true)
            MainScreen.instance.joinServer:refreshList()
        end
    end
end

function PublicServerList:prerender()
    ISPanelJoypad.prerender(self);

    PublicServerList.instance = self

--    self:drawTextCentre( getText("UI_servers_publicServer"), self.width / 2 - 100, 10, 1, 1, 1, 1, UIFont.Large);

    self:drawTextCentre(getText("UI_servers_addToFavorite"), self.scrollPanel:getX() + self.scrollPanel:getWidth() / 2, 10, 1, 1, 1, 1, UIFont.Large);

    local fieldsOK = self:checkFields()

    local find = false;

    for i,v in ipairs(MainScreen.instance.joinServer.listbox.items) do
        if v.item.server:getName() == self.serverNameEntry:getText() then
            find = true;
            break;
        end
    end

    if fieldsOK and find then
        self.addBtn:setEnable(false);
        self.addBtn:setTooltip(getText("UI_servers_err_saved_server_exists"))
    end

    if (getTimestamp() - PublicServerList.refreshTime) < PublicServerList.refreshInterval then
        self.refreshBtn:setEnable(false);
        self.refreshBtn:setTitle(getText("UI_servers_refresh") .. " " .. (PublicServerList.refreshInterval - math.floor(getTimestamp() - PublicServerList.refreshTime)));
        self.refreshBtn:setWidthToTitle(90)
    else
        self.refreshBtn:setEnable(true);
        self.refreshBtn:setTitle(getText("UI_servers_refresh"));
        self.refreshBtn:setWidthToTitle(90)
    end

    local item = self.listbox.items[self.listbox.selected]
    if self.playButton:isEnabled() and item and item.item.server:getVersion() ~= getCore():getVersionNumber() then
        self.playButton:setEnable(false)
        local tooltip = getText("UI_servers_err_version_mismatch", item.item.server:getVersion(), getCore():getVersionNumber())
        self.playButton:setTooltip(tooltip)
    end
end

function PublicServerList:setServerDescription(item)
    local text = item.server:getDescription()
    text = text:gsub("<", "&lt"):gsub(">", "&gt")
    text = text:gsub("\\n", "\n")
    text = " <RGB:0.8,0.8,0.8> " .. text
    item.richText:setText(text)
    item.richText:paginate()
end

function PublicServerList:setServerMods(item)
    if item.server:getMods() and item.server:getMods() ~= "" then
        local mods = item.server:getMods()
        if getSteamModeActive() then
            mods = mods:gsub(";", ",")
        end
        mods = mods:gsub(",", ", ")
        local text = getText("UI_servers_mods") .. mods:gsub("<", "&lt"):gsub(">", "&gt")
        text = " <RGB:0.8,0.8,0.8> " .. text
        item.modsText = ISRichTextLayout:new(self:getWidth()-17)
        item.modsText:setText(text)
        item.modsText:paginate()
    else
        item.modsText = nil
    end
end

function PublicServerList:addServerToList(server)
    local item = {}
    item.server = server
    
    item.richText = ISRichTextLayout:new(self:getWidth()-17)
    item.richText:initialise()
    self:setServerDescription(item)

    self:setServerMods(item)

    self.listbox:addItem(server:getName(), item);

    if not server:isOpen() then
        return
    end

    if not self.versionCheckBox.selected[1] then
        if server:getVersion() and server:getVersion() ~= getCore():getVersionNumber() then
            return
        end
    end

    if not self.emptyServer.selected[1] then
        if server:getPlayers() and tonumber(server:getPlayers()) == 0 then
            return
        end
    end

    if not self.whitelistServer.selected[1] then
        if not server:isOpen() then
            return
        end
    end

    if not self.pwdProtected.selected[1] then
        if server:isPasswordProtected() then
            return
        end
    end

    if not self.hasVisibleItem then
        self:fillFields(server)
        self.listbox.selected = #self.listbox.items
        self.hasVisibleItem = true
    end
end

function PublicServerList:refreshList()
    self.listbox:clear();
    self.hasVisibleItem = false
    if not isPublicServerListAllowed() then return; end
    if getSteamModeActive() then
        steamRequestInternetServersList()
        PublicServerList.refreshTime = getTimestamp()
        return
    end
    local dirs = getPublicServersList();
    if #dirs == 0 then
        return;
    end
    table.sort(dirs, function(a,b) return tonumber(a:getPlayers())>tonumber(b:getPlayers()) end)
    for i, k in ipairs(dirs) do
        if k:getVersion() == getCore():getVersionNumber() then
            self:addServerToList(k);
        end
    end
    PublicServerList.refreshTime = getTimestamp();
end

function PublicServerList:sortList()
    local sorted = {}
    for _,item in ipairs(self.listbox.items) do
        local item2 = {}
        item2.item = item
        item2.isSpiffoSpace = string.find(item.item.server:getName(), "SpiffoSpace")
        item2.numPlayers = tonumber(item.item.server:getPlayers())
        table.insert(sorted, item2)
    end
    table.sort(sorted, function(a,b)
        if a.isSpiffoSpace then
            if not b.isSpiffoSpace then
                return true
            end
        elseif b.isSpiffoSpace then
            if not a.isSpiffoSpace then
                return false
            end
        end
        return a.numPlayers > b.numPlayers
    end)
    self.listbox.items = {}
    for _,item2 in ipairs(sorted) do
        table.insert(self.listbox.items, item2.item)
    end
end

function PublicServerList:onOptionMouseDown(button, x, y)
    if button.internal == "REFRESH" then
        self:refreshList();
    end
    if button.internal == "SAVEDSERVERS" then
        if getSteamModeActive() then
            steamReleaseInternetServersRequest()
        end
        self:setVisible(false);
        MainScreen.instance.joinServer:setVisible(true);
        MainScreen.instance.joinServer:refreshList();
    end
    if button.internal == "BACK" then
        if getSteamModeActive() then
            steamReleaseInternetServersRequest()
        end
        --        getCore():ResetLua(true, "exitJoinServer")
        PublicServerList.instance:setVisible(false);
        MainScreen.instance.joinServer:setVisible(false);
        MainScreen.instance.bottomPanel:setVisible(true);
        if self.joyfocus then
            self.joyfocus.focus = MainScreen.instance
            updateJoypadFocus(self.joyfocus)
        end
    end
    if button.internal == "ADD" then
        self:addServer();
    end
    if button.internal == "ERASE" then
        self:erase();
    end
    if button.internal == "NEXT" then
        local server = self.listbox.items[self.listbox.selected].item.server
        if server:getVersion() ~= getCore():getVersionNumber() then
        else
            if self:checkFields() then
                if getSteamModeActive() then
                    steamReleaseInternetServersRequest()
                end
                local localIP = ""
                ConnectToServer.instance:connect(self, server:getName(), self.usernameEntry:getText(),
                    self.passwordEntry:getInternalText(), self.serverEntry:getText(),
					localIP, self.portEntry:getText(), self.serverPasswordEntry:getInternalText());
            end
        end
    end
    --self.listbox:sort();
end

function PublicServerList:addServer()
    if PublicServerList.instance:checkFields() then
        PublicServerList.instance:trimFields()
        local newServer = Server.new();
        newServer:setName(self.serverNameEntry:getText() or "");
        newServer:setIp(self.serverEntry:getText() or "");
        newServer:setPort(self.portEntry:getText() or "");
        newServer:setServerPassword(self.serverPasswordEntry:getInternalText() or "")
        newServer:setDescription(self.descEntry:getText() or "");
        newServer:setUserName(self.usernameEntry:getText() or "");
        newServer:setPwd(self.passwordEntry:getInternalText() or "");
--        newServer:setSteamId(self.steamIdEntry:getText() or "");
        self:writeServerOnFile(newServer, true);
        self:setVisible(false);
        MainScreen.instance.joinServer:fillFields(newServer);
        MainScreen.instance.joinServer:setVisible(true);
        MainScreen.instance.joinServer:refreshList();
        for index,item in ipairs(MainScreen.instance.joinServer.listbox.items) do
            if item.text == newServer:getName() then
                MainScreen.instance.joinServer.listbox.selected = index
                break
            end
        end
    end
end

function PublicServerList:checkFields()
    self.usernameEntry:setValid(true)
    self.usernameEntry:setTooltip(getText("UI_servers_username_tt"))
    
    self.passwordEntry:setValid(true)
    self.passwordEntry:setTooltip(getText("UI_servers_pwd_tt"))
    
    self.serverEntry:setValid(true)
    self.serverEntry:setTooltip(nil)
    
    self.portEntry:setValid(true)
    self.portEntry:setTooltip(nil)

--    self.passwordEntry:setValid(true)
--    self.passwordEntry:setTooltip(getText("UI_servers_serverpwd_tt"))

    local valid = true
    local tooltip = nil
    
    if self.passwordEntry:getText():trim() == "" then
        self.passwordEntry:setValid(false)
        self.passwordEntry:setTooltip(getText("UI_servers_err_username_pwd"))
        if valid then
            tooltip = getText("UI_servers_err_username_pwd")
        end
        valid = false;
    end
    
    if self.serverEntry:getText():trim() == "" then
        self.serverEntry:setValid(false)
        self.serverEntry:setTooltip(getText("UI_servers_err_ip"))
        if valid then
            tooltip = getText("UI_servers_err_ip")
        end
        valid = false;
    end
    if not tonumber(self.portEntry:getText():trim()) then
        self.portEntry:setValid(false)
        self.portEntry:setTooltip(getText("UI_servers_err_port"))
        if valid then
            tooltip = getText("UI_servers_err_port")
        end
        valid = false;
    end
    if not isValidUserName(self.usernameEntry:getText()) then
        self.usernameEntry:setValid(false)
        self.usernameEntry:setTooltip(getText("UI_servers_err_username"))
        if valid then
            tooltip = getText("UI_servers_err_username")
        end
        valid = false;
    end
--    if self.passwordEntry:getText():trim() == "" then
--        self.passwordEntry:setValid(false)
--        self.passwordEntry:setTooltip(getText("UI_servers_needPwd"))
--        if valid then
--            tooltip = getText("UI_servers_needPwd")
--        end
--        valid = false;
--    end

    if self.listbox.selected == -1 or #self.listbox.items == 0 then
        self.playButton:setEnable(false)
        self.playButton:setTooltip(nil)
    else
    self.playButton:setEnable(valid)
    self.playButton:setTooltip(tooltip)
    end
    
    self.addBtn:setEnable(valid)
    self.addBtn:setTooltip(tooltip)

    return valid;
end

function PublicServerList:clickNext()
    if not self.playButton:isEnabled() then
        return;
    end
    --    stopPing();
    --    PublicServerList.pingIndex = 1;
    self.connecting = true;
    self.playButton:setEnable(false);
    self.backButton:setEnable(false);
    self.failMessage = nil
    local server = self.listbox.items[self.listbox.selected].item.server
    self.connectLabel.name = getText("UI_servers_Connecting");
    local localIP = ""
    serverConnect(server:getUserName(), server:getPwd(), server:getIp(), localIP, server:getPort(), server:getServerPassword());
end

function PublicServerList:emptyServerFile(server, append)
    local fileOutput = getFileWriter(getServerListFile(), false, false)
    fileOutput:close();
end

function PublicServerList:writeServerOnFile(server, append)
    local fileOutput = getFileWriter(getServerListFile(), true, append);
    fileOutput:write("name=" .. server:getName() .. "\r\n");
    fileOutput:write("ip=" .. server:getIp() .. "\r\n");
    fileOutput:write("port=" .. server:getPort() .. "\r\n");
    fileOutput:write("serverpassword=" .. server:getServerPassword() .. "\r\n");
    fileOutput:write("description=" .. server:getDescription() .. "\r\n");
    fileOutput:write("user=" .. server:getUserName() .. "\r\n");
    fileOutput:write("password=" .. server:getPwd() .. "\r\n");
    fileOutput:close();
end

function PublicServerList:erase()
    self.serverNameEntry:setText("");
    self.serverEntry:setText("127.0.0.1");
    self.portEntry:setText("16261");
    self.serverPasswordEntry:setText("");
    self.descEntry:setText("");
    self.usernameEntry:setText("");
    self.passwordEntry:setText("");
--    self.steamIdEntry:setText("");
end

function PublicServerList:onClickServer(item)
    self:fillFields(item.server);
end

function PublicServerList:fillFields(item)
    self.serverNameEntry:setText(item:getName());
    self.serverEntry:setText(item:getIp());
--    self.steamIdEntry:setText(item:getSteamId());
    self.portEntry:setText(item:getPort());
    self.serverPasswordEntry:setText(item:getServerPassword())
    self.descEntry:setText(item:getDescription());
    self.usernameEntry:setText(item:getUserName());
    self.passwordEntry:setText(item:getPwd());
end

function PublicServerList:trimFields(item)
    self.serverNameEntry:setText(self.serverNameEntry:getText():trim());
    self.serverEntry:setText(self.serverEntry:getText():trim());
--    self.steamIdEntry:setText(self.steamIdEntry:getText():trim());
    self.portEntry:setText(self.portEntry:getText():trim());
    self.serverPasswordEntry:setText(self.serverPasswordEntry:getInternalText():trim())
    self.descEntry:setText(self.descEntry:getText():trim());
    self.usernameEntry:setText(self.usernameEntry:getText():trim());
    self.passwordEntry:setText(self.passwordEntry:getInternalText():trim());
end



function PublicServerList:drawMap(y, item, alt)
    local server = item.item.server
    if not server:isPublic() then
        return y;
    end
    local filter = PublicServerList.instance.filterEntry:getInternalText()
    if string.trim(filter) and not string.contains(string.lower(server:getName()), string.lower(string.trim(filter))) then
        return y;
    end

    if not PublicServerList.instance.versionCheckBox.selected[1] then
        if server:getVersion() and server:getVersion() ~= getCore():getVersionNumber() then
            return y
        end
    end

    if not PublicServerList.instance.emptyServer.selected[1] then
        if server:getPlayers() and tonumber(server:getPlayers()) == 0 then
            return y
        end
    end

    if not PublicServerList.instance.whitelistServer.selected[1] then
        if not server:isOpen() then
            return y
        end
    end

    if not PublicServerList.instance.pwdProtected.selected[1] then
        if server:isPasswordProtected() then
            return y
        end
    end

    if y + self:getYScroll() + item.height < 0 or y + self:getYScroll() >= self.height then
        return y + item.height
    end

    local isMouseOver = self.mouseoverselected == item.index and not self:isMouseOverScrollBar()
    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15);
    elseif isMouseOver then
        self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.95, 0.05, 0.05, 0.05);
    end
    self:drawRectBorder(0, (y), self:getWidth(), item.height-1, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    local dx = 0
    if server:isPasswordProtected() then
        dx = self.lockTexture:getWidth() + 8
        local largeFontHgt = getTextManager():getFontFromEnum(UIFont.Large):getLineHeight()
        self:drawTexture(self.lockTexture, 20, y + 15 + (largeFontHgt - self.lockTexture:getHeight()) / 2, 1, 1, 1, 1)
    end
    self:drawText(server:getName() .. " (" .. string.trim(server:getIp()) .. ":" .. server:getPort() .. ")", 20+dx, y+15, 0.9, 0.9, 0.9, 0.9, UIFont.Large);

    local richText = item.item.richText
    if richText:getWidth() ~= self:getWidth() - 17 then
        richText:setWidth(self:getWidth() - 17)
        richText:paginate()
    end
    local yy = y + 45
    richText:render(0, yy, self)
    yy = yy + richText:getHeight()

    self:drawText(getText("UI_servers_players") .. server:getPlayers() .. " / " .. server:getMaxPlayers(), self.width / 2, yy, 0.9, 0.9, 0.9, 0.9, UIFont.Small);

    self:drawText(getText("UI_servers_version") .. server:getVersion(), self.width / 2, yy+FONT_HGT_SMALL, 0.9, 0.9, 0.9, 0.9, UIFont.Small);

--    if not server:isOpen() then
--        self:drawText("Need Registration", self:getWidth() - 100, y+45, 0.9, 0.9, 0.9, 0.9, UIFont.Small);
--    end

    if server:isOpen() then
        self:drawText(getText("UI_servers_WhitelistOff"), 20, yy, 0.9, 0.9, 0.9, 0.9, UIFont.Small);
    else
        self:drawText(getText("UI_servers_WhitelistOn"), 20, yy, 0.9, 0.9, 0.9, 0.9, UIFont.Small);
    end

    if getSteamModeActive() then
        self:drawText(getText("UI_servers_Ping", server:getPing()), 20, yy+FONT_HGT_SMALL, 0.9, 0.9, 0.9, 0.9, UIFont.Small);
    else
        local min = getText("IGUI_Gametime_minutes");
        if server:getLastUpdate() < 1 then
            min = getText("IGUI_Gametime_minute");
        end
        self:drawText(getText("UI_servers_LastUpdate") .. server:getLastUpdate() .. " " .. min .. " ago", 20, yy+FONT_HGT_SMALL, 0.9, 0.9, 0.9, 0.9, UIFont.Small);
    end

    yy = yy + 2 * FONT_HGT_SMALL

    if item.item.modsText then
        local richText = item.item.modsText
        if richText:getWidth() ~= self:getWidth() - 17 then
            richText:setWidth(self:getWidth() - 17)
            richText:paginate()
        end
        richText:render(0, yy, self)
        yy = yy + richText:getHeight()
    end

    if PublicServerList.pingedList[server:getIp()] ~= nil then
        self:drawText(getText("UI_servers_Users") .. PublicServerList.pingedList[server:getIp()].users, 20, y+70, 0.9, 0.9, 0.9, 0.9, UIFont.Small);
    end

    self.itemheightoverride[item.text] = yy + 12 - y;

    y = y + self.itemheightoverride[item.text];

    return y;
end

function PublicServerList:onGainJoypadFocus(joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData)
    self:setISButtonForA(self.playButton)
    self:setISButtonForB(self.backButton)
	self:setISButtonForX(self.refreshBtn)
end

function PublicServerList:onJoypadDirLeft(joypadData)
	if getSteamModeActive() then
		steamReleaseInternetServersRequest()
	end
	MainScreen.instance.joinPublicServer:setVisible(false)
	MainScreen.instance.joinServer:setVisible(true, joypadData)
	MainScreen.instance.joinServer:refreshList()
end

function PublicServerList:onJoypadDirDown(joypadData)
	self.listbox:onJoypadDirDown();
end

function PublicServerList:onJoypadDirUp(joypadData)
	self.listbox:onJoypadDirUp();
end

function PublicServerList:initialise()
    ISPanelJoypad.initialise(self);
end

--************************************************************************--
--** ISPanel:instantiate
--**
--************************************************************************--
function PublicServerList:instantiate()
    self.javaObject = UIElement.new(self);
    self.javaObject:setX(self.x);
    self.javaObject:setY(self.y);
    self.javaObject:setHeight(self.height);
    self.javaObject:setWidth(self.width);
    self.javaObject:setAnchorLeft(self.anchorLeft);
    self.javaObject:setAnchorRight(self.anchorRight);
    self.javaObject:setAnchorTop(self.anchorTop);
    self.javaObject:setAnchorBottom(self.anchorBottom);
end

function PublicServerList.OnSteamServerResponded(serverIndex)
    local server = steamGetInternetServerDetails(serverIndex)
    if server then
        PublicServerList.instance:addServerToList(server)
        steamRequestServerRules(server:getIp(), tonumber(server:getPort()))
    end
end

function PublicServerList.OnSteamRefreshInternetServers()
    steamReleaseInternetServersRequest()
    PublicServerList.instance:sortList()
end

function PublicServerList.OnSteamRulesRefreshComplete(host, port, rules)
    local self = PublicServerList.instance
    local items = self.listbox.items
    for i=1,#items do
        local server = items[i].item.server
        if server:getIp() == host and server:getPort() == tostring(port) then
            if rules.description then
                server:setDescription(rules.description)
                self:setServerDescription(items[i].item)
                if i == self.listbox.selected then
                    self:fillFields(server)
                end
            end
            if rules.version then
                server:setVersion(rules.version)
            end
            server:setOpen(rules.open == "1")
            server:setPublic(rules.public == "1")
            if rules.mods then
                server:setMods(rules.mods)
            end
            self:setServerMods(items[i].item)
            break
        end
    end
end

function PublicServerList:new(x, y, width, height)
    -- using a virtual 100 height res for doing the UI, so it resizes properly on different rez's.
    local o = {}
    --o.data = {}
    o = ISPanelJoypad:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = 0;
    o.y = 0;
    o.backgroundColor = { r = 0, g = 0, b = 0, a = 0.0 };
    o.borderColor = { r = 1, g = 1, b = 1, a = 0.0 };
    o.itemheightoverride = {};
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    PublicServerList.instance = o;
    o.NoLabel = false;
    o.anchorBottom = false;
    return o;
end

Events.ServerPinged.Add(PublicServerList.ServerPinged);
if getSteamModeActive() then
    LuaEventManager.AddEvent("OnSteamServerResponded")
    LuaEventManager.AddEvent("OnSteamRefreshInternetServers")
    LuaEventManager.AddEvent("OnSteamRulesRefreshComplete")
    Events.OnSteamServerResponded.Add(PublicServerList.OnSteamServerResponded)
    Events.OnSteamRefreshInternetServers.Add(PublicServerList.OnSteamRefreshInternetServers)
    Events.OnSteamRulesRefreshComplete.Add(PublicServerList.OnSteamRulesRefreshComplete)
end

