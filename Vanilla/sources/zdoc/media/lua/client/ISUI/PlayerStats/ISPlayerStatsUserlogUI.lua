--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 21/09/16
-- Time: 10:19
-- To change this template use File | Settings | File Templates.
--

--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "ISUI/ISPanel"

---@class ISPlayerStatsUserlogUI : ISPanel
ISPlayerStatsUserlogUI = ISPanel:derive("ISPlayerStatsUserlogUI");


--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISPlayerStatsUserlogUI:initialise()
    ISPanel.initialise(self);
    self:create();
end


function ISPlayerStatsUserlogUI:setVisible(visible)
    --    self.parent:setVisible(visible);
    self.javaObject:setVisible(visible);
    for _,v in ipairs(self.windows) do
        v:removeFromUIManager();
    end
end

function ISPlayerStatsUserlogUI:render()
    local z = 20;

    self:drawText(self.chr:getUsername() .. " logs", self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, self.chr:getUsername() .. " logs") / 2), z, 1,1,1,1, UIFont.Medium);

    self:drawRectBorder(0,0, self.width, self.height,1,self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawRectBorder(self.userlogList.x, self.userlogList.y - self.userlogList.itemheight, self.userlogList:getWidth(), self.userlogList.itemheight + 1, 1, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawRect(self.userlogList.x, self.userlogList.y - self.userlogList.itemheight, self.userlogList:getWidth(), self.userlogList.itemheight + 1, self.listHeaderColor.a, self.listHeaderColor.r, self.listHeaderColor.g, self.listHeaderColor.b);
    self:drawRect(self.userlogList.x + 100, 1 + self.userlogList.y - self.userlogList.itemheight, 1, self.userlogList.itemheight,1,self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawRect(self.userlogList.x + 170, 1 + self.userlogList.y - self.userlogList.itemheight, 1, self.userlogList.itemheight,1,self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawRect(self.userlogList.x + 240, 1 + self.userlogList.y - self.userlogList.itemheight, 1, self.userlogList.itemheight,1,self.borderColor.r, self.borderColor.g, self.borderColor.b);

    self:drawText(getText("IGUI_PlayerStats_Type"), self.userlogList.x + 10, self.userlogList.y - 20, 1,1,1,1,UIFont.Small);
    self:drawText(getText("IGUI_PlayerStats_IssuedBy"), self.userlogList.x + 110, self.userlogList.y - 20, 1,1,1,1,UIFont.Small);
    self:drawText(getText("IGUI_PlayerStats_Amount"), self.userlogList.x + 180, self.userlogList.y - 20, 1,1,1,1,UIFont.Small);
    self:drawText(getText("IGUI_PlayerStats_Reason"), self.userlogList.x + 250, self.userlogList.y - 20, 1,1,1,1,UIFont.Small);
end

function ISPlayerStatsUserlogUI:create()

    self.userlogList = ISScrollingListBox:new(10, 70, self.width - 20, self.height - 130);
    self.userlogList:initialise();
    self.userlogList:instantiate();
    self.userlogList.itemheight = 22;
    self.userlogList.selected = 0;
    self.userlogList.joypadParent = self;
    self.userlogList.font = UIFont.NewSmall;
    self.userlogList.doDrawItem = self.drawLog;
    self.userlogList.drawBorder = true;
    self:addChild(self.userlogList);

    local btnWid = 100
    local btnHgt = 25
    local padBottom = 10

    self.ok = ISButton:new(10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Ok"), self, ISPlayerStatsUserlogUI.onOptionMouseDown);
    self.ok.internal = "OK";
    self.ok:initialise();
    self.ok:instantiate();
    self.ok.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.ok);

    self.addLog = ISButton:new(self:getWidth() - btnWid - 10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("IGUI_PlayerStats_AddLog"), self, ISPlayerStatsUserlogUI.onOptionMouseDown);
    self.addLog.internal = "ADD";
    self.addLog:initialise();
    self.addLog:instantiate();
    self.addLog.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.addLog);

    self.removeLog = ISButton:new(self.addLog.x - btnWid - 10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("ContextMenu_Remove"), self, ISPlayerStatsUserlogUI.onOptionMouseDown);
    self.removeLog.internal = "REMOVE";
    self.removeLog:initialise();
    self.removeLog:instantiate();
    self.removeLog.enable = false;
    self.removeLog.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.removeLog);

    self:populateLogList();
end

ISPlayerStatsUserlogUI.populateLogList = function(self)
    for _,v in ipairs(self.userlogs) do
        self.userlogList:addItem(self.chr:getUsername(), v);
    end
end

function ISPlayerStatsUserlogUI:drawLog(y, item, alt)
    local a = 0.9;

    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    self:drawRect(100, y-1, 1, self.itemheight,1,self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawRect(170, y-1, 1, self.itemheight,1,self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawRect(240, y-1, 1, self.itemheight,1,self.borderColor.r, self.borderColor.g, self.borderColor.b);

        if self.selected == item.index then
            self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
            ISPlayerStatsUserlogUI.instance.removeLog.enable = true;
            ISPlayerStatsUserlogUI.instance.selectedLog = item;
        end

    self:drawText(item.item.type, 10, y + 2, 1, 1, 1, a, self.font);
    self:drawText(item.item.issuedBy, 110, y + 2, 1, 1, 1, a, self.font);
    self:drawText(item.item.amount .. "", 180, y + 2, 1, 1, 1, a, self.font);
    self:drawText(item.item.text, 250, y + 2, 1, 1, 1, a, self.font);

    return y + self.itemheight;
end

function ISPlayerStatsUserlogUI:onOptionMouseDown(button, x, y)
    if button.internal == "OK" then
        self:setVisible(false);
        self:removeFromUIManager();
        if self.onclick ~= nil then
            self.onclick(self.target, button, self.comboList[self.combo.selected]);
        end
    end
    if button.internal == "ADD" then
        local modal = ISTextBox:new(self.x + 200, self.y + 200, 280, 180, getText("IGUI_PlayerStats_AddLogOn", self.chr:getUsername()), "", nil, ISPlayerStatsUserlogUI.onAddLog, self.chr:getPlayerNum());
        modal:initialise();
        modal:addToUIManager();
        modal.moveWithMouse = true;
        table.insert(ISPlayerStatsUserlogUI.instance.windows, modal);
    end
    if button.internal == "REMOVE" then
        local modal = ISModalDialog:new(0,0, 250, 150, getText("IGUI_PlayerStats_RemoveLogConfirm"), true, nil, ISPlayerStatsUserlogUI.onRemoveLog, self.chr:getPlayerNum());
        modal:initialise()
        modal:addToUIManager()
        modal.moveWithMouse = true;
        table.insert(ISPlayerStatsUserlogUI.instance.windows, modal);
    end
    if button.internal == "CANCEL" then
        self:setVisible(false);
        self:removeFromUIManager();
    end
end

function ISPlayerStatsUserlogUI:onRemoveLog(button, player)
    if button.internal == "YES" and ISPlayerStatsUserlogUI.instance.selectedLog then
        removeUserlog(ISPlayerStatsUserlogUI.instance.chr:getUsername(), ISPlayerStatsUserlogUI.instance.selectedLog.item.type, ISPlayerStatsUserlogUI.instance.selectedLog.item.text);
        ISPlayerStatsUserlogUI.instance.userlogList:removeItem(ISPlayerStatsUserlogUI.instance.selectedLog.index);
        ISPlayerStatsUserlogUI.instance.removeLog.enable = true;
        ISPlayerStatsUserlogUI.instance.selectedLog = nil;
        requestUserlog(ISPlayerStatsUserlogUI.instance.chr:getUsername());
    end
end


function ISPlayerStatsUserlogUI:onAddLog(button, player)
    if button.internal == "OK" then
        if button.parent.entry:getText() and button.parent.entry:getText() ~= "" then
            addUserlog(ISPlayerStatsUserlogUI.instance.chr:getUsername(), UserlogType.AdminLog:toString(), button.parent.entry:getText());
--            self.userlogList:addItem(player:getUsername(), {type = UserlogType.AdminLog:toString(), text = button.parent.entry:getText()});
            requestUserlog(ISPlayerStatsUserlogUI.instance.chr:getUsername());
        end
    end
end

function ISPlayerStatsUserlogUI:new(x, y, width, height, target, onclick, player, userlogs)
    local o = {};
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self);
    self.__index = self;
    o.variableColor={r=0.9, g=0.55, b=0.1, a=1};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.target = target;
    o.onclick = onclick;
    o.chr = player;
    o.userlogs = userlogs;
    o.zOffsetSmallFont = 25;
    o.moveWithMouse = true;
    o.listHeaderColor = {r=0.4, g=0.4, b=0.4, a=0.3};
    ISPlayerStatsUserlogUI.instance = o;
    o.windows = {};
    return o;
end

ISPlayerStatsUserlogUI.receiveUserLog = function(username, logs)
    if not ISPlayerStatsUserlogUI.instance or username ~= ISPlayerStatsUserlogUI.instance.chr:getUsername() then return; end

    ISPlayerStatsUserlogUI.instance.userlogs = {};
    for i=0,logs:size()-1 do
        local log = logs:get(i);
        table.insert(ISPlayerStatsUserlogUI.instance.userlogs, {type = log:getType(), text = log:getText(), issuedBy = log:getIssuedBy(), amount = log:getAmount()});
    end

    ISPlayerStatsUserlogUI.instance.userlogList:clear();
    for _,v in ipairs(ISPlayerStatsUserlogUI.instance.userlogs) do
        ISPlayerStatsUserlogUI.instance.userlogList:addItem(ISPlayerStatsUserlogUI.instance.chr:getUsername(), v);
    end
end

Events.OnReceiveUserlog.Add(ISPlayerStatsUserlogUI.receiveUserLog);
