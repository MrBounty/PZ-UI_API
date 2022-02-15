--***********************************************************
--**              	  ROBERT JOHNSON                       **
--***********************************************************

---@class ISFactionAddPlayerUI : ISPanel
ISFactionAddPlayerUI = ISPanel:derive("ISFactionAddPlayerUI");
ISFactionAddPlayerUI.messages = {};

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)
local FONT_HGT_LARGE = getTextManager():getFontHeight(UIFont.Large)

--************************************************************************--
--** ISFactionAddPlayerUI:initialise
--**
--************************************************************************--

function ISFactionAddPlayerUI:initialise()
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

    self.no = ISButton:new(10, self.playerList:getBottom() + 20, btnWid, btnHgt, getText("UI_Cancel"), self, ISFactionAddPlayerUI.onClick);
    self.no.internal = "CANCEL";
    self.no:initialise();
    self.no:instantiate();
    self.no.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.no);

    self.addPlayer = ISButton:new(0, self.no.y, btnWid, btnHgt, getText("IGUI_SafehouseUI_AddPlayer"), self, ISFactionAddPlayerUI.onClick);
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
    
    scoreboardUpdate();
end

function ISFactionAddPlayerUI:populateList()
    self.playerList:clear();
    self.addPlayer.enable = false;
    if not self.scoreboard then return end
    for i=1,self.scoreboard.usernames:size() do
        local username = self.scoreboard.usernames:get(i-1)
        local displayName = self.scoreboard.displayNames:get(i-1)
        local doIt = false;
        if self.changeOwnership then
            doIt = not self.faction:isOwner(username);
        else
            doIt = username ~= self.player:getUsername() and not self.faction:isMember(username);
        end
        if doIt then
            local newPlayer = {};
            newPlayer.name = username;
			if self.changeOwnership then 
				local alreadyFaction = self.faction:isMember(username);
				if not alreadyFaction then
				   newPlayer.tooltip = getText("IGUI_FactionUI_NoMember");
				end
			else
				local alreadyFaction = Faction.isAlreadyInFaction(username);
				if alreadyFaction then
				   newPlayer.tooltip = getText("IGUI_FactionUI_AlreadyHaveFaction");
				end
			end
            local index = self.playerList:addItem(displayName, newPlayer);
            if newPlayer.tooltip then
                if self.playerList.items[i] then
                    self.playerList.items[i].tooltip = newPlayer.tooltip;
                end
            end
        end
    end
end

function ISFactionAddPlayerUI:drawPlayers(y, item, alt)
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
            self.parent.selectedPlayer = nil;
        else
            self.parent.addPlayer.enable = true;
            self.parent.selectedPlayer = item.item.name;
        end
    end

    self:drawText(item.item.name, 10, y + 2, 1, 1, 1, a, self.font);

    return y + self.itemheight;
end

function ISFactionAddPlayerUI:prerender()
    local z = 20;
    local splitPoint = 100;
    local x = 10;
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawText(getText("IGUI_SafehouseUI_ConnectedPlayers"), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, getText("IGUI_SafehouseUI_ConnectedPlayers")) / 2), z, 1,1,1,1, UIFont.Medium);
    z = z + 30;
end

function ISFactionAddPlayerUI:onClick(button)
    if button.internal == "CANCEL" then
        self:setVisible(false);
        self:removeFromUIManager();
    end
    if button.internal == "ADDPLAYER" then
        if not self.changeOwnership then
--            self.faction:addPlayer(self.selectedPlayer);
--            self.addPlayer.enable = false;
--            self:populateList();
--            self.factionUI:populateList();
            local modal = ISModalDialog:new(0,0, 350, 150, getText("IGUI_FactionUI_InvitationSent",self.selectedPlayer), false, nil, nil);
            modal:initialise()
            modal:addToUIManager()
            sendFactionInvite(self.faction, self.player, self.selectedPlayer);
        else
            self.faction:setOwner(self.selectedPlayer);
			self.factionUI.isOwner = false;
            self.factionUI:populateList();
			self.factionUI:updateButtons();
            self:setVisible(false);
            self:removeFromUIManager();
            self.faction:syncFaction();
        end
    end
end

--************************************************************************--
--** ISFactionAddPlayerUI:new
--**
--************************************************************************--
function ISFactionAddPlayerUI:new(x, y, width, height, faction, player)
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
    o.faction = faction;
    o.moveWithMouse = true;
    ISFactionAddPlayerUI.instance = o;
    o.isOwner = faction:isOwner(player:getUsername()) or player:getAccessLevel() ~= "None";
    return o;
end


function ISFactionAddPlayerUI.OnScoreboardUpdate(usernames, displayNames, steamIDs)
    if ISFactionAddPlayerUI.instance then
        ISFactionAddPlayerUI.instance.scoreboard = {}
        ISFactionAddPlayerUI.instance.scoreboard.usernames = usernames
        ISFactionAddPlayerUI.instance.scoreboard.displayNames = displayNames
        ISFactionAddPlayerUI.instance.scoreboard.steamIDs = steamIDs
        ISFactionAddPlayerUI.instance:populateList()
    end
end

ISFactionAddPlayerUI.OnMiniScoreboardUpdate = function()
    if ISFactionAddPlayerUI.instance then
        scoreboardUpdate()
    end
end

Events.OnScoreboardUpdate.Add(ISFactionAddPlayerUI.OnScoreboardUpdate)
Events.OnMiniScoreboardUpdate.Add(ISFactionAddPlayerUI.OnMiniScoreboardUpdate)
