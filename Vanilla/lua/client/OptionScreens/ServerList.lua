--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 23/01/14
-- Time: 12:49
-- To change this template use File | Settings | File Templates.
--

ServerList = ISPanelJoypad:derive("ServerList");
ServerList.pingedList = {};
ServerList.refreshTime = 0
ServerList.refreshInterval = 5

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

function ServerList:create()
    local bottomPad = 6
    local buttonsHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

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
    if isPublicServerListAllowed() then
        self.tabs:addView(getText("UI_servers_publicServer"), ISUIElement:new(0,0,100,100))
    end
    
    self.listbox = ISScrollingListBox:new(listX, listTop, listWid, self.height-bottomPad-buttonsHgt-bottomPad-listTop);
    self.listbox:initialise();
    self.listbox:instantiate();
    self.listbox:setAnchorLeft(true);
    self.listbox:setAnchorRight(true);
    self.listbox:setAnchorTop(true);
    self.listbox:setAnchorBottom(true);
    self.listbox.itemheight = 110;
    self.listbox.doDrawItem = ServerList.drawMap;
    self.listbox:setOnMouseDoubleClick(self, ServerList.clickNext);
    self.listbox:setOnMouseDownFunction(self, ServerList.onClickServer);
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

    if getSteamModeActive() then
        self.localIPLabel = ISLabel:new(labelX, y, labelHgt, getText("UI_servers_LocalIP"), 1, 1, 1, 1, UIFont.Medium, true);
        self.localIPLabel:initialise();
        self.localIPLabel:instantiate();
        self.localIPLabel:setAnchorLeft(false);
        self.localIPLabel:setAnchorRight(true);
        self.localIPLabel:setAnchorTop(true);
        self.localIPLabel:setAnchorBottom(false);
        self.scrollPanel:addChild(self.localIPLabel);
        y = y + labelHgt + gapLabelY;

        self.localIPEntry = ISTextEntryBox:new("", entryX, y, entrySize, entryHgt);
        self.localIPEntry:initialise();
        self.localIPEntry:instantiate();
        self.localIPEntry:setAnchorLeft(false);
        self.localIPEntry:setAnchorRight(true);
        self.localIPEntry:setAnchorTop(true);
        self.localIPEntry:setAnchorBottom(false);
        self.localIPEntry:setTooltip(getText("UI_servers_LocalIP_tt"))
        self.scrollPanel:addChild(self.localIPEntry);
        y = y + entryHgt + gapEntryY;
    end

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

-- Steam ID label/field
--[[

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

    y = y + entryHgt

    y = y + 20

    self.eraseBtn = ISButton:new(labelX, y, 70, buttonsHgt, getText("UI_servers_erase"), self, ServerList.onOptionMouseDown);
    self.eraseBtn.internal = "ERASE";
    self.eraseBtn:initialise();
    self.eraseBtn:instantiate();
    self.eraseBtn:setAnchorLeft(false);
    self.eraseBtn:setAnchorRight(true);
    self.eraseBtn:setAnchorTop(false);
    self.eraseBtn:setAnchorBottom(false);
    self.scrollPanel:addChild(self.eraseBtn);

    self.addBtn = ISButton:new(self.scrollPanel.width - 70, y, 70, buttonsHgt, getText("UI_servers_add"), self, ServerList.onOptionMouseDown);
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

    local buttonY = self.height - bottomPad - buttonsHgt

    self.backButton = ISButton:new(16, buttonY, 100, buttonsHgt, getText("UI_btn_back"), self, ServerList.onOptionMouseDown);
    self.backButton.internal = "BACK";
    self.backButton:initialise();
    self.backButton:instantiate();
    self.backButton:setAnchorLeft(true);
    self.backButton:setAnchorTop(false);
    self.backButton:setAnchorBottom(true);
    self.backButton:setWidthToTitle(100, true)
    self:addChild(self.backButton);

    self.playButton = ISButton:new(self.listbox.x + self.listbox.width - 100, buttonY, 100, buttonsHgt, getText("UI_servers_joinServer"), self, ServerList.onOptionMouseDown);
    self.playButton.internal = "NEXT";
    self.playButton:initialise();
    self.playButton:instantiate();
    self.playButton:setAnchorLeft(false);
    self.playButton:setAnchorRight(true);
    self.playButton:setAnchorTop(false);
    self.playButton:setAnchorBottom(true);
    self.playButton:setWidthToTitle(100, true)
    self.playButton:setX(self.listbox:getRight() - self.playButton:getWidth())
    self:addChild(self.playButton);

    self.publicBtn = ISButton:new(self.backButton:getRight() + 10, buttonY, 100, buttonsHgt, getText("UI_servers_publicServer"), self, ServerList.onOptionMouseDown);
    self.publicBtn.internal = "PUBLICSERVERS";
    self.publicBtn:initialise();
    self.publicBtn:instantiate();
    self.publicBtn:setAnchorLeft(true);
    self.publicBtn:setAnchorRight(false);
    self.publicBtn:setAnchorTop(false);
    self.publicBtn:setAnchorBottom(true);

    if false and isPublicServerListAllowed() then
        self:addChild(self.publicBtn);
    end

    self.refreshBtn = ISButton:new(self.publicBtn:getRight() + 10, buttonY, 90, buttonsHgt, getText("UI_servers_refresh"), self, ServerList.onOptionMouseDown);
    self.refreshBtn.internal = "REFRESH";
    self.refreshBtn:initialise();
    self.refreshBtn:instantiate();
    self.refreshBtn:setAnchorLeft(true);
    self.refreshBtn:setAnchorRight(false);
    self.refreshBtn:setAnchorTop(false);
    self.refreshBtn:setAnchorBottom(true);
    if getSteamModeActive() then
        self:addChild(self.refreshBtn);
    end

    self.deleteBtn = ISButton:new(self.playButton.x - 16 - 70, buttonY, 70, buttonsHgt, getText("UI_servers_delete"), self, ServerList.onOptionMouseDown);
    self.deleteBtn.internal = "DELETE";
    self.deleteBtn:initialise();
    self.deleteBtn:instantiate();
    self.deleteBtn:setAnchorLeft(false);
    self.deleteBtn:setAnchorRight(true);
    self.deleteBtn:setAnchorTop(false);
    self.deleteBtn:setAnchorBottom(true);
    self:addChild(self.deleteBtn);
    self.deleteBtn:setWidthToTitle(70)
    self.deleteBtn:setX(self.playButton:getX() - 16 - self.deleteBtn:getWidth())

    self.getModBtn = ISButton:new(self.listbox.x + self.listbox.width/2, self.backButton.y - bottomPad + 10, 70, buttonsHgt, "Get the mod", self, ServerList.onOptionMouseDown);
    self.getModBtn.internal = "GETMOD";
    self.getModBtn:initialise();
    self.getModBtn:instantiate();
    self.getModBtn:setAnchorLeft(false);
    self.getModBtn:setAnchorRight(true);
    self.getModBtn:setAnchorTop(false);
    self.getModBtn:setAnchorBottom(true);
    self:addChild(self.getModBtn);
    self.getModBtn:setVisible(false);
	
	--self:insertNewLineOfButtons(self.listbox);
	--self:insertNewLineOfButtons(self.backButton, self.playButton);

    self:refreshList()
    if #self.listbox.items > 0 then
        self:fillFields(self.listbox.items[1].item.server);
    end
end

function ServerList:onMouseDown_Tabs(x, y)
    if self:getMouseY() >= 0 and self:getMouseY() < self.tabHeight then
        local tabIndex = self:getTabIndexAtX(self:getMouseX())
        if tabIndex == 2 then
            MainScreen.instance.joinServer:setVisible(false)
            MainScreen.instance.joinPublicServer:setVisible(true)
            if getTimestamp() - PublicServerList.refreshTime >= 60 then
                MainScreen.instance.joinPublicServer:refreshList()
            end
        end
    end
end

function ServerList:prerender()
ServerList.instance = self
self.listbox.doDrawItem = self.drawMap
    ISPanelJoypad.prerender(self);

--    self:drawTextCentre( getText("UI_servers_serverlist"), self.width / 2 - 100, 10, 1, 1, 1, 1, UIFont.Large);

    self:drawTextCentre(getText("UI_servers_addToFavorite"), self.scrollPanel:getX() + self.scrollPanel:getWidth() / 2, 10, 1, 1, 1, 1, UIFont.Large);


    if self.listbox.selected ~= -1 and #self.listbox.items > 0 then
        self.deleteBtn:setEnable(true);
    else
        self.deleteBtn:setEnable(false);
    end

    local fieldsOK = self:checkFields()

    local find = false;

    for i,v in ipairs(self.listbox.items) do
        if v.item.server:getName() == self.serverNameEntry:getText() then
            find = true;
            break;
        end
    end

    if find then
        self.addBtn.title = getText("UI_servers_save");
        self.addBtn.internal = "SAVE";
    else
        self.addBtn.title = getText("UI_servers_add");
        self.addBtn.internal = "ADD";
    end
    self.addBtn:setWidthToTitle(70);
    self.addBtn:setX(self.scrollPanel.width - 17 - self.addBtn:getWidth())

    if (getTimestamp() - ServerList.refreshTime) < ServerList.refreshInterval then
        self.refreshBtn:setEnable(false);
        self.refreshBtn:setTitle(getText("UI_servers_refresh") .. " " .. (ServerList.refreshInterval - math.floor(getTimestamp() - ServerList.refreshTime)));
    else
        self.refreshBtn:setEnable(true);
        self.refreshBtn:setTitle(getText("UI_servers_refresh"));
    end

end

function ServerList:onOptionMouseDown(button, x, y)
    self.getModBtn:setVisible(false);
    if button.internal == "PUBLICSERVERS" then
        self:setVisible(false);
        MainScreen.instance.joinPublicServer:setVisible(true);
        if getTimestamp() - PublicServerList.refreshTime >= 60 then
            MainScreen.instance.joinPublicServer:refreshList();
        end
    end
    if button.internal == "BACK" then
--        getCore():ResetLua(true, "exitJoinServer")
        self:setVisible(false);
        MainScreen.instance.joinServer:setVisible(false);
        MainScreen.instance.bottomPanel:setVisible(true);
        if self.joyfocus then
            self.joyfocus.focus = MainScreen.instance
            updateJoypadFocus(self.joyfocus)
        end
    end
    if button.internal == "REFRESH" then
        -- Quick refresh updates player counts
        if getSteamModeActive() then
            for i=1,#self.listbox.items do
                local item = self.listbox.items[i]
                local server = item.item.server
                if server:getIp() and server:getIp() ~= "" and tonumber(server:getPort()) then
                    steamRequestServerDetails(server:getIp(), tonumber(server:getPort()))
                end
            end
        	ServerList.refreshTime = getTimestamp()
        end
    end
    if button.internal == "ADD" then
        if self:checkFields() then
            self:trimFields()
            local newServer = Server.new();
            newServer:setName(self.serverNameEntry:getText() or "");
            newServer:setIp(self.serverEntry:getText() or "");
            if getSteamModeActive() then
                newServer:setLocalIP(self.localIPEntry:getText() or "")
            end
--            newServer:setSteamId(self.steamIdEntry:getText() or "");
            newServer:setPort(self.portEntry:getText() or "");
            newServer:setServerPassword(self.serverPasswordEntry:getInternalText() or "")
            newServer:setDescription(self.descEntry:getText() or "");
            newServer:setUserName(self.usernameEntry:getText() or "");
            newServer:setPwd(self.passwordEntry:getInternalText() or "");
            self:addServerToList(newServer);
            self:writeServerOnFile(newServer, true);
        end
    end
    if button.internal == "ERASE" then
        self:erase();
    end
    if button.internal == "DELETE" then
        self.listbox:removeItem(self.listbox.items[self.listbox.selected].text);
        self:emptyServerFile()
        for i,v in ipairs(self.listbox.items) do
            self:writeServerOnFile(v.item.server, true);
        end
        self:erase();
        if #self.listbox.items > 0 then
            local item = self.listbox.items[self.listbox.selected]
            self:fillFields(item.item.server)
        end
    end
    if button.internal == "SAVE" then
        if not self:checkFields() then return end
        self.listbox:removeItem(self.serverNameEntry:getText());
        self:emptyServerFile()
        for i,v in ipairs(self.listbox.items) do
            self:writeServerOnFile(v.item.server, true);
        end
        self:trimFields()
        local newServer = Server.new();
        newServer:setName(self.serverNameEntry:getText() or "");
        newServer:setIp(self.serverEntry:getText() or "");
        if getSteamModeActive() then
            newServer:setLocalIP(self.localIPEntry:getText() or "")
        end
--        newServer:setSteamId(self.steamIdEntry:getText() or "");
        newServer:setPort(self.portEntry:getText() or "");
        newServer:setServerPassword(self.serverPasswordEntry:getInternalText() or "")
        newServer:setDescription(self.descEntry:getText() or "");
        newServer:setUserName(self.usernameEntry:getText() or "");
        newServer:setPwd(self.passwordEntry:getInternalText() or "");
        self:addServerToList(newServer);

        self:writeServerOnFile(newServer, true);
    end
    if button.internal == "NEXT" then
        self:clickNext();
    end
    if button.internal == "GETMOD" then
        openUrl(button.url);
    end
    self.listbox:sort();
end

function ServerList:checkFields()
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
    
--    self.steamIdEntry:setValid(true)

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
        self.usernameEntry:setTooltip(getText("UI_servers_notvalid_username"))
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

    self.playButton:setEnable(valid and (self.listbox.selected > 0))
    self.playButton:setTooltip(tooltip)
    
    self.addBtn:setEnable(valid)
    self.addBtn:setTooltip(tooltip)
    
    return valid;
end

function ServerList:clickNext()
    if not self.playButton:isEnabled() then
        return;
    end
--    stopPing();
--    ServerList.pingIndex = 1;
--    if not self.passwordEntry:getInternalText() or self.passwordEntry:getInternalText() == "" then
--        local modal = ISModalDialog:new((getCore():getScreenWidth() / 2) - (250 / 2), (getCore():getScreenHeight() / 2) - (150 / 2), 250, 150, getText("UI_servers_needPwd"), false);
--        modal:initialise()
--        modal:addToUIManager()
--        modal:setAlwaysOnTop(true)
--        if self.joyfocus then
--            self.joyfocus.focus = modal
--            updateJoypadFocus(self.joyfocus)
--        end
--        return;
--    end
    getCore():setNoSave(false);
    local server = self.listbox.items[self.listbox.selected].item.server
    local localIP = getSteamModeActive() and self.localIPEntry:getText() or ""
    ConnectToServer.instance:connect(self, server:getName(), self.usernameEntry:getText(), self.passwordEntry:getInternalText(),
        self.serverEntry:getText(), localIP, self.portEntry:getText(), self.serverPasswordEntry:getInternalText());
end

function ServerList:emptyServerFile(server, append)
    local fileOutput = getFileWriter(getServerListFile(), false, false)
    fileOutput:close();
end

function ServerList:writeServerOnFile(server, append)
    local fileOutput = getFileWriter(getServerListFile(), true, append);
    fileOutput:write("name=" .. server:getName() .. "\r\n");
    fileOutput:write("ip=" .. server:getIp() .. "\r\n");
    if getSteamModeActive() then
        fileOutput:write("localip=" .. server:getLocalIP() .. "\r\n");
    end
    fileOutput:write("port=" .. server:getPort() .. "\r\n");
    fileOutput:write("serverpassword=" .. server:getServerPassword() .. "\r\n");
    fileOutput:write("description=" .. server:getDescription() .. "\r\n");
    fileOutput:write("user=" .. server:getUserName() .. "\r\n");
    fileOutput:write("password=" .. server:getPwd() .. "\r\n");
    fileOutput:close();
end

function ServerList:erase()
    self.serverNameEntry:setText("");
    self.serverEntry:setText("127.0.0.1");
    self.portEntry:setText("16261");
    self.serverPasswordEntry:setText("");
    self.descEntry:setText("");
    self.usernameEntry:setText("");
    self.passwordEntry:setText("");
--    self.steamIdEntry:setText("");
end

function ServerList:onClickServer(item)
    self:fillFields(item.server);

    if getSteamModeActive() and item.responded then
        steamRequestServerDetails(item.server:getIp(), tonumber(item.server:getPort()))
    end
end

function ServerList:fillFields(item)
    self.serverNameEntry:setText(item:getName());
    self.serverEntry:setText(item:getIp());
    if getSteamModeActive() then
        self.localIPEntry:setText(item:getLocalIP());
    end
--    self.steamIdEntry:setText(item:getSteamId());
    self.portEntry:setText(item:getPort());
    self.serverPasswordEntry:setText(item:getServerPassword())
    self.descEntry:setText(item:getDescription());
    self.usernameEntry:setText(item:getUserName());
    self.passwordEntry:setText(item:getPwd());
end

function ServerList:trimFields(item)
    self.serverNameEntry:setText(self.serverNameEntry:getText():trim());
    self.serverEntry:setText(self.serverEntry:getText():trim());
    if getSteamModeActive() then
        self.localIPEntry:setText(self.localIPEntry:getText():trim());
    end
--    self.steamIdEntry:setText(self.steamIdEntry:getText():trim());
    self.portEntry:setText(self.portEntry:getText():trim());
    self.serverPasswordEntry:setText(self.serverPasswordEntry:getInternalText():trim())
    self.descEntry:setText(self.descEntry:getText():trim());
    self.usernameEntry:setText(self.usernameEntry:getText():trim());
    self.passwordEntry:setText(self.passwordEntry:getInternalText():trim());
end

function ServerList:drawMap(y, item, alt)
    local server = item.item.server
    local isMouseOver = self.mouseoverselected == item.index and not self:isMouseOverScrollBar()
    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15);
    elseif isMouseOver then
        self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.95, 0.05, 0.05, 0.05);
    end
    self:drawRectBorder(0, (y), self:getWidth(), item.height-1, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b);

--    local connection = "Off";
--    if ServerList.pingedList[server:getIp()] ~= nil then
--        connection = "On";
--    end
    local dx = 0
    if server:isPasswordProtected() then
        dx = self.lockTexture:getWidth() + 8
        local largeFontHgt = getTextManager():getFontFromEnum(UIFont.Large):getLineHeight()
        self:drawTexture(self.lockTexture, 20, y + 15 + (largeFontHgt - self.lockTexture:getHeight()) / 2, 1, 1, 1, 1)
    end
    self:drawText(server:getName() .. " (" .. server:getIp() .. ":" .. server:getPort() .. ") ", 20+dx, y+15, 0.9, 0.9, 0.9, 0.9, UIFont.Large);

    if server:getUserName() and server:getUserName() ~= "" then
        self:drawText(getText("UI_servers_LogAs") .. server:getUserName(), 20, y+50, 0.9, 0.9, 0.9, 0.9, UIFont.Small);
    end

    local richText = item.item.richText
    if richText:getWidth() ~= self:getWidth() - 17 then
        richText:setWidth(self:getWidth() - 17)
        richText:paginate()
    end
    local yy = y + 70
    richText:render(0, yy, self)
    yy = yy + richText:getHeight()

    local smallFontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()
    if item.item.responded then
        if server:isOpen() then
            self:drawText(getText("UI_servers_WhitelistOff"), 20, yy, 0.9, 0.9, 0.9, 0.9, UIFont.Small);
        else
            self:drawText(getText("UI_servers_WhitelistOn"), 20, yy, 0.9, 0.9, 0.9, 0.9, UIFont.Small);
        end
        self:drawText(getText("UI_servers_Ping", server:getPing()), 20, yy+smallFontHgt, 0.9, 0.9, 0.9, 0.9, UIFont.Small);
        self:drawText(getText("UI_servers_players") .. server:getPlayers() .. " / " .. server:getMaxPlayers(), self:getWidth()  / 2, yy, 0.9, 0.9, 0.9, 0.9, UIFont.Small)
        local version = server:getVersion() or "???"
        self:drawText(getText("UI_servers_version") .. version, self:getWidth()  / 2, yy+smallFontHgt, 0.9, 0.9, 0.9, 0.9, UIFont.Small)
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
    elseif getSteamModeActive() then
        self:drawText(getText("UI_servers_not_responding"), 20, yy, 0.9, 0.9, 0.9, 0.9, UIFont.Small);
        yy = yy + FONT_HGT_SMALL
    end

    if ServerList.pingedList[server:getIp()] ~= nil then
        self:drawText("Users : " .. ServerList.pingedList[server:getIp()].users, 20, y+70, 0.9, 0.9, 0.9, 0.9, UIFont.Small);
    end

    self.itemheightoverride[item.text] = yy + 12 - y

    y = y + self.itemheightoverride[item.text];

    return y;
end

function ServerList:onGainJoypadFocus(joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData)
    self:setISButtonForA(self.playButton)
    self:setISButtonForB(self.backButton)
end

function ServerList:onJoypadDirRight(joypadData)
	if not isPublicServerListAllowed() then return end
	MainScreen.instance.joinServer:setVisible(false)
	MainScreen.instance.joinPublicServer:setVisible(true, joypadData)
	if getTimestamp() - PublicServerList.refreshTime >= 60 then
		MainScreen.instance.joinPublicServer:refreshList()
	end
end

function ServerList:onJoypadDirDown(joypadData)
	self.listbox:onJoypadDirDown();
end

function ServerList:onJoypadDirUp(joypadData)
	self.listbox:onJoypadDirUp();
end

function ServerList:initialise()
    ISPanelJoypad.initialise(self);
end

--************************************************************************--
--** ISPanel:instantiate
--**
--************************************************************************--
function ServerList:instantiate()
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

function ServerList:new(x, y, width, height)
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
    ServerList.instance = o;
    o.NoLabel = false;
    o.anchorBottom = false;
    return o;
end

function ServerList.onResetLua(reason)
	if reason == "ConnectedToServer" then
        reactivateJoypadAfterResetLua()
        local joypadData = JoypadState.getMainMenuJoypad()
        if joypadData then
            joypadData.focus = nil
            joypadData.lastfocus = nil
            JoypadState.forceActivate = joypadData.id
        end
		if DebugScenarios.instance ~= nil then
			MainScreen.instance:removeChild(DebugScenarios.instance)
			DebugScenarios.instance = nil
		end
		MainScreen.instance.bottomPanel:setVisible(false);
--		MainScreen.instance.joinServer:pingServers(true)
--		MainScreen.instance.joinServer:setVisible(true)
	end
end

Events.OnResetLua.Add(ServerList.onResetLua)

ServerList.pingIndex = 1;

function ServerList:pingServers(init)
    -- Both ServerList and PublicServerList have Events.ServerPinged callbacks
    if not ServerList.instance:getIsVisible() then return end

--    if init then
--        ServerList.pingedList = {};
--        ServerList.pingIndex = 1;
--    end
--    if ServerList.pingIndex <= #self.listbox.items then
--       local v = self.listbox.items[ServerList.pingIndex];
--        if v then
--            ServerList.pingIndex = ServerList.pingIndex + 1;
--            ping(v.item.server:getUserName(), v.item.server:getPwd(), v.item.server:getIp(), v.item.server:getPort());
--        end
--    else
--        stopPing();
--    end
end

function ServerList:setServerDescription(item)
    local text = item.server:getDescription()
    text = text:gsub("<", "&lt"):gsub(">", "&gt")
    text = text:gsub("\\n", "\n")
    text = " <RGB:0.8,0.8,0.8> " .. text
    item.richText:setText(text)
    item.richText:paginate()
end

function ServerList:setServerMods(item)
    if item.server:getMods() and item.server:getMods() ~= "" then
        local mods = item.server:getMods()
        if getSteamModeActive() then
            mods = mods:gsub(";", ",")
        end
        mods = mods:gsub(",", ", ")
        local text = getText("UI_servers_mods") .. mods:gsub(";", ", "):gsub("<", "&lt"):gsub(">", "&gt")
        text = " <RGB:0.8,0.8,0.8> " .. text
        item.modsText = ISRichTextLayout:new(self:getWidth()-17)
        item.modsText:setText(text)
        item.modsText:paginate()
    else
        item.modsText = nil
    end
end

function ServerList:addServerToList(server)
    local item = {}
    item.server = server
    
    item.richText = ISRichTextLayout:new(self:getWidth()-17)
    item.richText:initialise()
    self:setServerDescription(item)

    self:setServerMods(item)

    if getSteamModeActive() then
        item.responded = false
        item.rules = nil
    end
    
    self.listbox:addItem(server:getName(), item);
	self.listbox.selected = #self.listbox.items;
end

function ServerList:refreshList()
    self.listbox:clear()
    local servers = getServerList();
    for _,server in ipairs(servers) do
        self:addServerToList(server)
        if getSteamModeActive() and tonumber(server:getPort()) then
            steamRequestServerDetails(server:getIp(), tonumber(server:getPort()))
        end
    end
    self.listbox:sort()
end

ServerList.ServerPinged = function(ip, users)
--    if ServerList.pingedList[ip] == nil then
--        ServerList.pingedList[ip] = {};
--    end
--    ServerList.pingedList[ip].users = users;
--    forceDisconnect();
--    ServerList.instance:pingServers(false);
end

function ServerList.OnSteamServerResponded2(host, port, server2)
--    print('OnSteamServerResponded2 ' .. host .. ' ' .. tostring(port))
    local self = ServerList.instance
    local items = self.listbox.items
    for i=1,#items do
        local server = items[i].item.server
        if server:getIp() == host and server:getPort() == tostring(port) then
            items[i].item.responded = true
            server:setPing(server2:getPing())
            server:setPlayers(server2:getPlayers())
            server:setMaxPlayers(server2:getMaxPlayers())
            server:setMods(server2:getMods())
            server:setPasswordProtected(server2:isPasswordProtected())
--            self:setServerMods(items[i].item)
            if i == self.listbox.selected then
                self:fillFields(server)
            end
        end
    end
    steamRequestServerRules(host, port)
end

function ServerList.OnSteamServerFailedToRespond2(host, port)
    local self = ServerList.instance
    local items = self.listbox.items
    for i=1,#items do
        local server = items[i].item.server
        if server:getIp() == host and server:getPort() == tostring(port) then
            items[i].item.responded = false
        end
    end
end

function ServerList.OnSteamRulesRefreshComplete(host, port, rules)
--    print('OnSteamRulesRefreshComplete ' .. host .. ' ' .. tostring(port))
    local self = ServerList.instance
    local items = self.listbox.items
    for i=1,#items do
        local server = items[i].item.server
        if server:getIp() == host and server:getPort() == tostring(port) then
            items[i].item.rules = rules
            if rules.description then
                server:setDescription(rules.description)
                self:setServerDescription(items[i].item)
            end
            if rules.version then
                server:setVersion(rules.version)
            end
            if i == self.listbox.selected then
                self:fillFields(server)
            end
            server:setOpen(rules.open == "1")
            if rules.mods then
                server:setMods(rules.mods)
            end
            self:setServerMods(items[i].item)
        end
    end
end

function ServerList:onJoypadBeforeDeactivate(joypadData)
    self.backButton:clearJoypadButton()
    self.playButton:clearJoypadButton()
end

Events.ServerPinged.Add(ServerList.ServerPinged);
if getSteamModeActive() then
    LuaEventManager.AddEvent("OnSteamServerResponded2")
    LuaEventManager.AddEvent("OnSteamServerFailedToRespond2")
    LuaEventManager.AddEvent("OnSteamRulesRefreshComplete")
    Events.OnSteamServerResponded2.Add(ServerList.OnSteamServerResponded2)
    Events.OnSteamServerFailedToRespond2.Add(ServerList.OnSteamServerFailedToRespond2)
    Events.OnSteamRulesRefreshComplete.Add(ServerList.OnSteamRulesRefreshComplete)
end
