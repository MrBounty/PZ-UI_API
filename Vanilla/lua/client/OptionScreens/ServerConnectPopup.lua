--***********************************************************
--**                Gennadii Potapov                       **
--***********************************************************

require "ISUI/ISPanel"
require "ISUI/ISButton"

require "defines"

ServerConnectPopup =  ISPanel:derive("ServerConnectPopup");

function ServerConnectPopup:create()

    local y = self.height/2 - 200;
    local entrySize = 160;
    local labelX = self.width/2 - entrySize/2;
    local labelHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
    local entryX = labelX;
    local entryHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight() + 2 * 2
    local gapLabelY = 2
    local gapEntryY = 10

    y = y + 40;

    local label = ISLabel:new(labelX, y, labelHgt, getText("UI_servers_serverpwd"), 1, 1, 1, 1, UIFont.Medium, true);
    label:initialise();
    label:instantiate();
    label:setAnchorLeft(true);
    label:setAnchorRight(true);
    label:setAnchorTop(true);
    label:setAnchorBottom(false);
    self:addChild(label);

    y = y + labelHgt + gapLabelY;

    local entry = ISTextEntryBox:new("", entryX, y, entrySize, entryHgt);
    entry:initialise();
    entry:instantiate();
    entry:setAnchorLeft(true);
    entry:setAnchorRight(true);
    entry:setAnchorTop(true);
    entry:setAnchorBottom(false);
    entry:setMasked(true);
    entry:setTooltip(getText("UI_servers_serverpwd_tt"))
    self:addChild(entry);
    self.serverPasswordEntry = entry;

    y = y + entryHgt + gapEntryY;

    self.usernameLabel = ISLabel:new(labelX, y, labelHgt, getText("UI_servers_username"), 1, 1, 1, 1, UIFont.Medium, true);
    self.usernameLabel:initialise();
    self.usernameLabel:instantiate();
    self.usernameLabel:setAnchorLeft(true);
    self.usernameLabel:setAnchorRight(true);
    self.usernameLabel:setAnchorTop(true);
    self.usernameLabel:setAnchorBottom(false);
    self:addChild(self.usernameLabel);

    y = y + labelHgt + gapLabelY;

    self.usernameEntry = ISTextEntryBox:new("", entryX, y, entrySize, entryHgt);
    self.usernameEntry:initialise();
    self.usernameEntry:instantiate();
    self.usernameEntry:setAnchorLeft(true);
    self.usernameEntry:setAnchorRight(true);
    self.usernameEntry:setAnchorTop(true);
    self.usernameEntry:setAnchorBottom(false);
    self:addChild(self.usernameEntry);

    y = y + entryHgt + gapEntryY;

    self.passwordLabel = ISLabel:new(labelX, y, labelHgt, getText("UI_servers_pwd"), 1, 1, 1, 1, UIFont.Medium, true);
    self.passwordLabel:initialise();
    self.passwordLabel:instantiate();
    self.passwordLabel:setAnchorLeft(true);
    self.passwordLabel:setAnchorRight(true);
    self.passwordLabel:setAnchorTop(true);
    self.passwordLabel:setAnchorBottom(false);
    self:addChild(self.passwordLabel);

    y = y + labelHgt + gapLabelY;

    self.passwordEntry = ISTextEntryBox:new("", entryX, y, entrySize, entryHgt);
    self.passwordEntry:initialise();
    self.passwordEntry:instantiate();
    self.passwordEntry:setAnchorLeft(true);
    self.passwordEntry:setAnchorRight(true);
    self.passwordEntry:setAnchorTop(true);
    self.passwordEntry:setAnchorBottom(false);
    self.passwordEntry:setMasked(true);
    self.passwordEntry:setTooltip(getText("UI_servers_pwd_tt"))
    self:addChild(self.passwordEntry);

    y = y + entryHgt + 20

    self.cancelBtn = ISButton:new(labelX, y, 70, 25, getText("UI_Cancel"), self, ServerConnectPopup.onOptionMouseDown);
    self.cancelBtn.internal = "CANCEL";
    self.cancelBtn:initialise();
    self.cancelBtn:instantiate();
    self.cancelBtn:setAnchorLeft(true);
    self.cancelBtn:setAnchorRight(true);
    self.cancelBtn:setAnchorTop(true);
    self.cancelBtn:setAnchorBottom(false);
    self:addChild(self.cancelBtn);

    self.connectBtn = ISButton:new(labelX + 90, y, 70, 25, getText("UI_ServerConnectPopup_Connect"), self, ServerConnectPopup.onOptionMouseDown);
    self.connectBtn.internal = "CONNECT";
    self.connectBtn:initialise();
    self.connectBtn:instantiate();
    self.connectBtn:setAnchorLeft(true);
    self.connectBtn:setAnchorRight(true);
    self.connectBtn:setAnchorTop(true);
    self.connectBtn:setAnchorBottom(false);
    self:addChild(self.connectBtn);
end

function strsplit(inputstr, sep)
    if sep == nil then
        sep = "%s"
    end
    local t={} ; local i=1;
    for str in string.gmatch(inputstr, "([^"..sep.."]+)") do
        t[i] = str
        i = i + 1
    end
    return t
end

function ServerConnectPopup:setServer(ip, port, passwordStr)
    if not type(port) == "string" then error "port must be a string" end
    self.ip = ip;
    self.port = port;

    -- Get the most-recently edited account for this server and fill in the fields.
    -- Assumes the most-recently edited account comes last.
    local servers = getServerList();
    for _,server in ipairs(servers) do
        if server:getIp() == self.ip and server:getPort() == self.port then
            self.serverPasswordEntry:setText(server:getServerPassword())
            self.usernameEntry:setText(server:getUserName())
            self.passwordEntry:setText(server:getPwd())
        end
    end

    if passwordStr then
        self.serverPasswordEntry:setText(passwordStr)
    end
end

function ServerConnectPopup:prerender()
    ISPanel.prerender(self);
    self:drawTextCentre(getText("UI_ServerConnectPopup_Label"), self.width / 2, 20, 1, 1, 1, 1, UIFont.Large);
    self:drawTextCentre(self.ip.." : "..self.port, self.width / 2, 40, 1, 1, 1, 1, UIFont.Large);
    self:checkFields()
end

function ServerConnectPopup:initialise()
    ISPanel.initialise(self);
end

function ServerConnectPopup:onOptionMouseDown(button, x, y)
    if button.internal == "CANCEL" then
        ServerConnectPopup.instance:setVisible(false);
        MainScreen.instance.serverConnectPopup:setVisible(false);
        MainScreen.instance.bottomPanel:setVisible(true);
    end

    if button.internal == "CONNECT" then
        if ServerConnectPopup.instance:checkFields() then
            local localIP = ""
            ConnectToServer.instance:connect(self, "", self.usernameEntry:getText(), self.passwordEntry:getInternalText(),
                self.ip, localIP, self.port, self.serverPasswordEntry:getInternalText());
        end
    end
end

function ServerConnectPopup:checkFields()
    ServerConnectPopup.instance.usernameEntry:setValid(true)
    ServerConnectPopup.instance.usernameEntry:setTooltip(getText("UI_servers_username_tt"))
    local valid = true
    local tooltip = nil
    if ServerConnectPopup.instance.usernameEntry:getText():trim() == "" then
        self.usernameEntry:setValid(false)
        tooltip = getText("UI_servers_err_username");
        valid = false;
    end
    self.connectBtn:setEnable(valid)
    self.connectBtn:setTooltip(tooltip)
    return valid;
end


--************************************************************************--
--** ISPanel:instantiate
--**
--************************************************************************--
function ServerConnectPopup:instantiate()
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

function ServerConnectPopup:new(x, y, width, height)
    -- using a virtual 100 height res for doing the UI, so it resizes properly on different rez's.
    local o = {}
    o = ISPanel:new(x, y, width, height);
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
    ServerConnectPopup.instance = o;
    o.NoLabel = false;
    o.anchorBottom = false;
    return o;
end

