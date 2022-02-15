--***********************************************************
--**              	  ROBERT JOHNSON                       **
--**            UI display with a question or text         **
--**          can display a yes/no button or ok btn        **
--***********************************************************

ISSafehouseUI = ISPanel:derive("ISSafehouseUI");
ISSafehouseUI.messages = {};
ISSafehouseUI.inviteDialogs = {}

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

--************************************************************************--
--** ISSafehouseUI:initialise
--**
--************************************************************************--

function ISSafehouseUI:initialise()
    ISPanel.initialise(self);
    local btnWid = 100
    local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local btnHgt2 = FONT_HGT_SMALL
    local padBottom = 10

    self.no = ISButton:new(self:getWidth() - btnWid - 10, 0, btnWid, btnHgt, getText("UI_Ok"), self, ISSafehouseUI.onClick);
    self.no.internal = "OK";
    self.no:initialise();
    self.no:instantiate();
    self.no.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.no);

    local nameLbl = ISLabel:new(10, 20, FONT_HGT_SMALL, getText("IGUI_SafehouseUI_Title"), 1, 1, 1, 1, UIFont.Small, true)
    nameLbl:initialise()
    nameLbl:instantiate()
    self:addChild(nameLbl)

    self.title = ISLabel:new(nameLbl:getRight() + 8, nameLbl.y, FONT_HGT_SMALL, self.safehouse:getTitle(), 0.6, 0.6, 0.8, 1.0, UIFont.Small, true)
    self.title:initialise()
    self.title:instantiate()
    self:addChild(self.title)

    self.changeTitle = ISButton:new(0, nameLbl.y, 70, btnHgt2, getText("IGUI_PlayerStats_Change"), self, ISSafehouseUI.onClick);
    self.changeTitle.internal = "CHANGETITLE";
    self.changeTitle:initialise();
    self.changeTitle:instantiate();
    self.changeTitle.borderColor = self.buttonBorderColor;
    self:addChild(self.changeTitle);

    local ownerLbl = ISLabel:new(10, nameLbl:getBottom() + 10, FONT_HGT_SMALL, getText("IGUI_SafehouseUI_Owner"), 1, 1, 1, 1, UIFont.Small, true)
    ownerLbl:initialise()
    ownerLbl:instantiate()
    self:addChild(ownerLbl)

    self.owner = ISLabel:new(ownerLbl:getRight() + 8, ownerLbl.y, FONT_HGT_SMALL, "", 0.6, 0.6, 0.8, 1.0, UIFont.Small, true)
    self.owner:initialise()
    self.owner:instantiate()
    self:addChild(self.owner)

    local posLbl = ISLabel:new(10, ownerLbl:getBottom() + 7, FONT_HGT_SMALL, getText("IGUI_SafehouseUI_Pos"), 1, 1, 1, 1, UIFont.Small, true)
    posLbl:initialise()
    posLbl:instantiate()
    self:addChild(posLbl)

    self.pos = ISLabel:new(posLbl:getRight() + 8, posLbl.y, FONT_HGT_SMALL, getText("IGUI_SafehouseUI_Pos2", self.safehouse:getX(), self.safehouse:getY()), 0.6, 0.6, 0.8, 1.0, UIFont.Small, true)
    self.pos:initialise()
    self.pos:instantiate()
    self:addChild(self.pos)

    self.releaseSafehouse = ISButton:new(10, 0, 70, btnHgt, getText("IGUI_SafehouseUI_Release"), self, ISSafehouseUI.onClick);
    self.releaseSafehouse.internal = "RELEASE";
    self.releaseSafehouse:initialise();
    self.releaseSafehouse:instantiate();
    self.releaseSafehouse.borderColor = self.buttonBorderColor;
    self:addChild(self.releaseSafehouse);
    self.releaseSafehouse.parent = self;
    self.releaseSafehouse:setVisible(false);

    self.changeOwnership = ISButton:new(0, ownerLbl.y, 70, btnHgt2, getText("IGUI_SafehouseUI_ChangeOwnership"), self, ISSafehouseUI.onClick);
    self.changeOwnership.internal = "CHANGEOWNERSHIP";
    self.changeOwnership:initialise();
    self.changeOwnership:instantiate();
    self.changeOwnership.borderColor = self.buttonBorderColor;
    self:addChild(self.changeOwnership);
    self.changeOwnership.parent = self;
    self.changeOwnership:setVisible(false);

    local playersLbl = ISLabel:new(10, posLbl:getBottom() + 20, FONT_HGT_SMALL, getText("IGUI_SafehouseUI_Players"), 1, 1, 1, 1, UIFont.Small, true)
    playersLbl:initialise()
    playersLbl:instantiate()
    self:addChild(playersLbl)

    self.playerList = ISScrollingListBox:new(10, playersLbl:getBottom(), self.width - 20, (FONT_HGT_SMALL + 2 * 2) * 8);
    self.playerList:initialise();
    self.playerList:instantiate();
    self.playerList.itemheight = FONT_HGT_SMALL + 2 * 2;
    self.playerList.selected = 0;
    self.playerList.joypadParent = self;
    self.playerList.font = UIFont.NewSmall;
    self.playerList.doDrawItem = self.drawPlayers;
    self.playerList.drawBorder = true;
    self:addChild(self.playerList);

    self.removePlayer = ISButton:new(0, self.playerList.y + self.playerList.height + 5, 70, btnHgt2, getText("ContextMenu_Remove"), self, ISSafehouseUI.onClick);
    self.removePlayer.internal = "REMOVEPLAYER";
    self.removePlayer:initialise();
    self.removePlayer:instantiate();
    self.removePlayer.borderColor = self.buttonBorderColor;
    self.removePlayer:setWidthToTitle(70)
    self.removePlayer:setX(self.playerList:getRight() - self.removePlayer.width)
    self:addChild(self.removePlayer);
    self.removePlayer.enable = false;
    if not self.isOwner then
        self.removePlayer:setVisible(false);
    end

    self.quitSafehouse = ISButton:new(0, self.playerList.y + self.playerList.height + 5, 70, btnHgt2, getText("IGUI_SafehouseUI_QuitSafehouse"), self, ISSafehouseUI.onClick);
    self.quitSafehouse.internal = "QUITSAFE";
    self.quitSafehouse:initialise();
    self.quitSafehouse:instantiate();
    self.quitSafehouse.borderColor = self.buttonBorderColor;
    self.quitSafehouse:setWidthToTitle(70)
    self.quitSafehouse:setX(self.playerList:getRight() - self.quitSafehouse.width)
    self:addChild(self.quitSafehouse);
    if self.isOwner then
        self.quitSafehouse:setVisible(false);
    end

    self.addPlayer = ISButton:new(self.playerList.x, self.playerList.y + self.playerList.height + 5, 70, btnHgt2, getText("IGUI_SafehouseUI_AddPlayer"), self, ISSafehouseUI.onClick);
    self.addPlayer.internal = "ADDPLAYER";
    self.addPlayer:initialise();
    self.addPlayer:instantiate();
    self.addPlayer.borderColor = self.buttonBorderColor;
    self:addChild(self.addPlayer);

    self.respawn = ISTickBox:new(10, self.addPlayer:getBottom() + 10, getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_SafehouseUI_Respawn")) + 20, 18, "", self, ISSafehouseUI.onClickRespawn);
    self.respawn:initialise();
    self.respawn:instantiate();
    self.respawn.selected[1] = self.safehouse:isRespawnInSafehouse(self.player:getUsername());
    self.respawn:addOption(getText("IGUI_SafehouseUI_Respawn"));
    self:addChild(self.respawn);
    self.respawn.safehouseUI = self;
    if not getServerOptions():getBoolean("SafehouseAllowRespawn") then
        self.respawn.enable = false;
    end

    self.no:setY(self.respawn:getBottom() + 20)
    self.releaseSafehouse:setY(self.respawn:getBottom() + 20)
    self:setHeight(self.no:getBottom() + padBottom)

    self:populateList();

end

function ISSafehouseUI:onClickRespawn(clickedOption, enabled)
    self.safehouse:setRespawnInSafehouse(enabled, self.player:getUsername());
end

function ISSafehouseUI:populateList()
    self.playerList:clear();
    for i=0,self.safehouse:getPlayers():size()-1 do
--        if self.safehouse:getPlayers():get(i) ~= self.player:getUsername() then
            local newPlayer = {};
            newPlayer.name = self.safehouse:getPlayers():get(i);
            self.playerList:addItem(newPlayer.name, newPlayer);
--        end
    end
end

function ISSafehouseUI:drawPlayers(y, item, alt)
    local a = 0.9;

--    self.parent.removePlayer.enable = false;
--    self.parent.selectedPlayer = nil;
    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

--    self:drawRect(100, y-1, 1, self.itemheight,1,self.borderColor.r, self.borderColor.g, self.borderColor.b);
--    self:drawRect(170, y-1, 1, self.itemheight,1,self.borderColor.r, self.borderColor.g, self.borderColor.b);
--    self:drawRect(240, y-1, 1, self.itemheight,1,self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
--        if self.parent.isOwner then
--            self.parent.removePlayer.enable = true;
--        end
--        self.parent.selectedPlayer = item.item.name;
    end

    self:drawText(item.item.name, 10, y + 2, 1, 1, 1, a, self.font);

    return y + self.itemheight;
end

function ISSafehouseUI:render()
    self:updateButtons();

    self.removePlayer.enable = false;
    if self.playerList.selected > 0 then
        if self.isOwner then
            self.removePlayer.enable = true;
        end
        self.selectedPlayer = self.playerList.items[self.playerList.selected].item.name;
        if self.selectedPlayer == self.player:getUsername() then
            self.removePlayer.enable = false;
        end
    else
        self.selectedPlayer = nil;
    end
end

function ISSafehouseUI:prerender()
    ISSafehouseUI.instance = self  -- to support reloading in lua debugger
    
    local z = 20;
    local splitPoint = 100;
    local x = 10;
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
--    self:drawText(self.safehouse:getTitle(), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, self.safehouse:getTitle()) / 2), z, 1,1,1,1, UIFont.Medium);
    self.title:setName(self.safehouse:getTitle())
    self.changeTitle:setX(self.title:getRight() + 10)
    z = z + 30;
--    self:drawText(getText("IGUI_SafehouseUI_Owner"), x, z, 1,1,1,1, UIFont.Small);
--    self:drawText(self.safehouse:getOwner(), splitPoint, z, 1,1,1,1, UIFont.Small);
    self.owner:setName(self.safehouse:getOwner())
    if self.isOwner then
        self.releaseSafehouse:setVisible(true);
        self.changeOwnership:setVisible(true);
        self.changeOwnership:setX(self.owner:getRight() + 10);
    end
--    self:drawText(getText("IGUI_SafehouseUI_Players"), x, self.playerList.y - FONT_HGT_SMALL, 1,1,1,1, UIFont.Small);
end

function ISSafehouseUI:updateButtons()
    if not self.isOwner then
        self.releaseSafehouse:setVisible(false);
        self.changeOwnership:setVisible(false);
        self.removePlayer.enable = false;
        self.addPlayer.enable = false;
        self.changeTitle.enable = false;
    else
        self.quitSafehouse:setVisible(false);
    end
end

function ISSafehouseUI:onClick(button)
    if button.internal == "OK" then
        self:close();
    end
    if button.internal == "RELEASE" then
        self.safehouse:removeSafeHouse(getPlayerFromUsername(self.safehouse:getOwner()));
        self:close();
    end
    if button.internal == "REMOVEPLAYER" then
        local modal = ISModalDialog:new(0,0, 350, 150, getText("IGUI_SafehouseUI_RemoveConfirm", self.selectedPlayer), true, nil, ISSafehouseUI.onRemovePlayerFromSafehouse);
        modal:initialise()
        modal:addToUIManager()
        modal.ui = self;
        modal.moveWithMouse = true;
    end
    if button.internal == "ADDPLAYER" then
        local safehouseUI = ISSafehouseAddPlayerUI:new(getCore():getScreenWidth() / 2 - 200,getCore():getScreenHeight() / 2 - 175, 400, 350, self.safehouse, self.player);
        safehouseUI:initialise()
        safehouseUI:addToUIManager()
        safehouseUI.safehouseUI = self;
        self.addPlayerUI = safehouseUI;
    end
    if button.internal == "CHANGETITLE" then
        local modal = ISTextBox:new(self.x + 200, 200, 280, 180, getText("IGUI_SafehouseUI_ChangeTitle"), self.safehouse:getTitle(), nil, ISSafehouseUI.onChangeTitle);
        modal.safehouse = self.safehouse;
        modal:initialise();
        modal:addToUIManager();
    end
    if button.internal == "CHANGEOWNERSHIP" then
        local safehouseUI = ISSafehouseAddPlayerUI:new(getCore():getScreenWidth() / 2 - 200,getCore():getScreenHeight() / 2 - 175, 400, 350, self.safehouse, self.player);
        safehouseUI.changeOwnership = true;
        safehouseUI:initialise()
        safehouseUI:addToUIManager()
        safehouseUI.safehouseUI = self;
    end
    if button.internal == "QUITSAFE" then
        local modal = ISModalDialog:new(0,0, 350, 150, getText("IGUI_SafehouseUI_QuitSafeConfirm", self.selectedPlayer), true, nil, ISSafehouseUI.onQuitSafehouse);
        modal:initialise()
        modal:addToUIManager()
        modal.ui = self;
        modal.moveWithMouse = true;
    end
end

function ISSafehouseUI:close()
    self:setVisible(false)
    self:removeFromUIManager()
    ISSafehouseUI.instance = nil
end

function ISSafehouseUI:onChangeTitle(button)
    if button.internal == "OK" then
        button.parent.safehouse:setTitle(button.parent.entry:getText());
        button.parent.safehouse:syncSafehouse();
    end
end

function ISSafehouseUI:onQuitSafehouse(button)
    if button.internal == "YES" then
        if not getServerOptions():getBoolean("SafehouseAllowTrepass") then
            if button.parent.ui.player:getX() >= button.parent.ui.safehouse:getX() - 1 and button.parent.ui.player:getX() < button.parent.ui.safehouse:getX2() + 1 and button.parent.ui.player:getY() >= button.parent.ui.safehouse:getY() - 1 and button.parent.ui.player:getY() < button.parent.ui.safehouse:getY2() + 1 then
                button.parent.ui.safehouse:kickOutOfSafehouse(button.parent.ui.player);
            end
        end
    end
    button.parent.ui.safehouse:removePlayer(button.parent.ui.player:getUsername());
    button.parent.ui:close();
end

function ISSafehouseUI:onRemovePlayerFromSafehouse(button, player)
    if button.internal == "YES" then
        if not getServerOptions():getBoolean("SafehouseAllowTrepass") then
            local players = getOnlinePlayers();
            for i=1,players:size() do
                local player = players:get(i-1)
                if player:getUsername() == button.parent.ui.selectedPlayer then
                    if player:getX() >= button.parent.ui.safehouse:getX() - 1 and player:getX() < button.parent.ui.safehouse:getX2() + 1 and player:getY() >= button.parent.ui.safehouse:getY() - 1 and player:getY() < button.parent.ui.safehouse:getY2() + 1 then
                        button.parent.ui.safehouse:kickOutOfSafehouse(player);
                        break;
                    end
                end
            end
        end
        button.parent.ui.safehouse:removePlayer(button.parent.ui.selectedPlayer);
        button.parent.ui:populateList();
    end
end

--************************************************************************--
--** ISSafehouseUI:new
--**
--************************************************************************--
function ISSafehouseUI:new(x, y, width, height, safehouse, player)
    local o = {}
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    if y == 0 then
        o.y = o:getMouseY() - (height / 2)
        o:setY(o.y)
    end
    if x == 0 then
        o.x = o:getMouseX() - (width / 2)
        o:setX(o.x)
    end
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.width = width;
    o.height = height;
    o.player = player;
    o.safehouse = safehouse;
    o.moveWithMouse = true;
    ISSafehouseUI.instance = o;
    o.buttonBorderColor = {r=0.7, g=0.7, b=0.7, a=0.5};
    o.isOwner = safehouse:isOwner(player) or player:getAccessLevel() ~= "None";
    return o;
end

function ISSafehouseUI.OnSafehousesChanged()
    if ISSafehouseUI.instance then
        local safehouse = ISSafehouseUI.instance.safehouse
        if not SafeHouse.getSafehouseList():contains(safehouse) then
            ISSafehouseUI.instance:close()
        else
            ISSafehouseUI.instance:populateList();
        end
    end
end

ISSafehouseUI.ReceiveSafehouseInvite = function(safehouse, host)
    if ISSafehouseUI.inviteDialogs[host] then
        if ISSafehouseUI.inviteDialogs[host]:isReallyVisible() then return end
        ISSafehouseUI.inviteDialogs[host] = nil
    end

    if not SafeHouse.hasSafehouse(getPlayer()) then
        local modal = ISModalDialog:new(getCore():getScreenWidth() / 2 - 175,getCore():getScreenHeight() / 2 - 75, 350, 150, getText("IGUI_SafehouseUI_Invitation", host), true, nil, ISSafehouseUI.onAnswerSafehouseInvite);
        modal:initialise()
        modal:addToUIManager()
        modal.safehouse = safehouse;
        modal.host = host;
        modal.moveWithMouse = true;
        ISSafehouseUI.inviteDialogs[host] = modal
    end
end

function ISSafehouseUI:onAnswerSafehouseInvite(button)
    ISSafehouseUI.inviteDialogs[button.parent.host] = nil
    if button.internal == "YES" then
        button.parent.safehouse:addPlayer(getPlayer():getUsername());
        acceptSafehouseInvite(button.parent.safehouse, button.parent.host)
    end
end

ISSafehouseUI.AcceptedSafehouseInvite = function(safehouseName, host)
    if ISSafehouseUI.instance and ISSafehouseUI.instance:isVisible() and safehouseName == ISSafehouseUI.instance.safehouse:getTitle() then
        if ISSafehouseUI.instance.addPlayerUI and ISSafehouseUI.instance.addPlayerUI:isVisible() then
            ISSafehouseUI.instance.addPlayerUI:populateList();
        end
        ISSafehouseUI.instance:populateList();
    end
end

Events.OnSafehousesChanged.Add(ISSafehouseUI.OnSafehousesChanged)
Events.ReceiveSafehouseInvite.Add(ISSafehouseUI.ReceiveSafehouseInvite);
Events.AcceptedSafehouseInvite.Add(ISSafehouseUI.AcceptedSafehouseInvite);