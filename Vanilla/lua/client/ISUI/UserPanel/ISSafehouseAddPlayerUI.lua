--***********************************************************
--**              	  ROBERT JOHNSON                       **
--**            UI display with a question or text         **
--**          can display a yes/no button or ok btn        **
--***********************************************************

ISSafehouseAddPlayerUI = ISPanel:derive("ISSafehouseAddPlayerUI");
ISSafehouseAddPlayerUI.messages = {};

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

--************************************************************************--
--** ISSafehouseAddPlayerUI:initialise
--**
--************************************************************************--

function ISSafehouseAddPlayerUI:initialise()
    ISPanel.initialise(self);
    local btnWid = 100
    local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local padBottom = 10

    local listY = 20 + FONT_HGT_MEDIUM + 20
    self.playerList = ISScrollingListBox:new(10, listY, self.width - 20, (FONT_HGT_SMALL + 2 * 2) * 8);
    self.playerList:initialise();
    self.playerList:instantiate();
    self.playerList.itemheight = FONT_HGT_SMALL + 2 * 2;
    self.playerList.selected = 0;
    self.playerList.joypadParent = self;
    self.playerList.font = UIFont.NewSmall;
    self.playerList.doDrawItem = self.drawPlayers;
    self.playerList.drawBorder = true;
    self:addChild(self.playerList);

    self.no = ISButton:new(10, self.playerList:getBottom() + 20, btnWid, btnHgt, getText("UI_Cancel"), self, ISSafehouseAddPlayerUI.onClick);
    self.no.internal = "CANCEL";
    self.no:initialise();
    self.no:instantiate();
    self.no.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.no);

    self.addPlayer = ISButton:new(0, self.no.y, btnWid, btnHgt, getText("IGUI_SafehouseUI_AddPlayer"), self, ISSafehouseAddPlayerUI.onClick);
    if self.changeOwnership then
        self.addPlayer.title = getText("IGUI_SafehouseUI_ChangeOwnership");
    end
    self.addPlayer.internal = "ADDPLAYER";
    self.addPlayer:initialise();
    self.addPlayer:instantiate();
    self.addPlayer.borderColor = {r=1, g=1, b=1, a=0.1};
    self.addPlayer:setWidthToTitle(btnWid)
    self.addPlayer:setX(self.playerList:getRight() - self.addPlayer.width)
    self:addChild(self.addPlayer);
    self.addPlayer.enable = false;

    self:setHeight(self.addPlayer:getBottom() + 10)

    scoreboardUpdate()
end

function ISSafehouseAddPlayerUI:populateList()
    self.playerList:clear();
    if not self.scoreboard then return end
    for i=1,self.scoreboard.usernames:size() do
        local username = self.scoreboard.usernames:get(i-1)
        local displayName = self.scoreboard.displayNames:get(i-1)
        local doIt = false;
        if self.changeOwnership then
            doIt = not self.safehouse:getOwner() == username
        else
            doIt = username ~= self.player:getUsername() and not self.safehouse:getPlayers():contains(username);
        end
        if doIt then
            local newPlayer = {};
            newPlayer.username = username;
            local alreadySafe = self.safehouse:alreadyHaveSafehouse(username);
            if alreadySafe and not alreadySafe:getPlayers():contains(username) then
                if alreadySafe:getTitle() ~= "Safehouse" then
                    newPlayer.tooltip = getText("IGUI_SafehouseUI_AlreadyHaveSafehouse", "(" .. alreadySafe:getTitle() .. ")");
                else
                    newPlayer.tooltip = getText("IGUI_SafehouseUI_AlreadyHaveSafehouse" , "");
                end
            end
            local item = self.playerList:addItem(displayName, newPlayer);
            if newPlayer.tooltip then
               item.tooltip = newPlayer.tooltip;
            end
        end
    end
end

function ISSafehouseAddPlayerUI:drawPlayers(y, item, alt)
    local a = 0.9;

--    self.parent.addPlayer.enable = false;
--    self.parent.selectedPlayer = nil;
    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

--    self:drawRect(100, y-1, 1, self.itemheight,1,self.borderColor.r, self.borderColor.g, self.borderColor.b);
--    self:drawRect(170, y-1, 1, self.itemheight,1,self.borderColor.r, self.borderColor.g, self.borderColor.b);
--    self:drawRect(240, y-1, 1, self.itemheight,1,self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
        if item.tooltip then
            self.parent.addPlayer.tooltip = item.tooltip;
            self.parent.addPlayer.enable = false;
         else
            self.parent.addPlayer.enable = true;
            self.parent.selectedPlayer = item.item.username;
        end
    end

    self:drawText(item.text, 10, y + 2, 1, 1, 1, a, self.font);

    return y + self.itemheight;
end

function ISSafehouseAddPlayerUI:prerender()
    local z = 20;
    local splitPoint = 100;
    local x = 10;
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawText(getText("IGUI_SafehouseUI_ConnectedPlayers"), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, getText("IGUI_SafehouseUI_ConnectedPlayers")) / 2), z, 1,1,1,1, UIFont.Medium);
    z = z + 30;
end

function ISSafehouseAddPlayerUI:onClick(button)
    if button.internal == "CANCEL" then
        self:setVisible(false);
        self:removeFromUIManager();
        ISSafehouseAddPlayerUI.instance = nil
    end
    if button.internal == "ADDPLAYER" then
        if not self.changeOwnership then
--            self.safehouse:addPlayer(self.selectedPlayer);
--            self.addPlayer.enable = false;
--            self:populateList();
--            self.safehouseUI:populateList();
            local modal = ISModalDialog:new(0,0, 350, 150, getText("IGUI_FactionUI_InvitationSent",self.selectedPlayer), false, nil, nil);
            modal:initialise()
            modal:addToUIManager()
            sendSafehouseInvite(self.safehouse, self.player, self.selectedPlayer);
        else
            self.safehouse:setOwner(self.selectedPlayer);
            self.safehouse:syncSafehouse();
            if self.player:getX() >= self.safehouse:getX() - 1 and self. player:getX() < self.safehouse:getX2() + 1 and self.player:getY() >= self.safehouse:getY() - 1 and self.player:getY() < self.safehouse:getY2() + 1 then
                self.safehouse:kickOutOfSafehouse(self.player);
            end
            self.safehouseUI:populateList();
            self:setVisible(false);
            self:removeFromUIManager();
            ISSafehouseAddPlayerUI.instance = nil
        end
    end
end

--************************************************************************--
--** ISSafehouseAddPlayerUI:new
--**
--************************************************************************--
function ISSafehouseAddPlayerUI:new(x, y, width, height, safehouse, player)
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
    o.scoreboard = nil
    ISSafehouseAddPlayerUI.instance = o;
    o.isOwner = safehouse:isOwner(player) or player:getAccessLevel() ~= "None";
    return o;
end

function ISSafehouseAddPlayerUI.OnScoreboardUpdate(usernames, displayNames, steamIDs)
    if ISSafehouseAddPlayerUI.instance then
        ISSafehouseAddPlayerUI.instance.scoreboard = {}
        ISSafehouseAddPlayerUI.instance.scoreboard.usernames = usernames
        ISSafehouseAddPlayerUI.instance.scoreboard.displayNames = displayNames
        ISSafehouseAddPlayerUI.instance.scoreboard.steamIDs = steamIDs
        ISSafehouseAddPlayerUI.instance:populateList()
    end
end

ISSafehouseAddPlayerUI.OnMiniScoreboardUpdate = function()
    if ISSafehouseAddPlayerUI.instance then
        scoreboardUpdate()
    end
end

Events.OnScoreboardUpdate.Add(ISSafehouseAddPlayerUI.OnScoreboardUpdate)
Events.OnMiniScoreboardUpdate.Add(ISMiniScoreboardUI.OnMiniScoreboardUpdate)

