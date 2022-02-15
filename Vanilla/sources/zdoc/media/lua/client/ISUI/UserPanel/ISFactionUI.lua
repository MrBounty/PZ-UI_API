--***********************************************************
--**              	  ROBERT JOHNSON                       **
--**            UI display with a question or text         **
--**          can display a yes/no button or ok btn        **
--***********************************************************

---@class ISFactionUI : ISPanel
ISFactionUI = ISPanel:derive("ISFactionUI");
ISFactionUI.messages = {};
ISFactionUI.inviteDialogs = {}

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)
local FONT_HGT_LARGE = getTextManager():getFontHeight(UIFont.Large)

--************************************************************************--
--** ISFactionUI:initialise
--**
--************************************************************************--

function ISFactionUI:initialise()
    ISPanel.initialise(self);
    local btnWid = 100
    local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local btnHgt2 = FONT_HGT_SMALL
    local padBottom = 10

    local ownerLbl = ISLabel:new(10, 20 + FONT_HGT_MEDIUM + 20, FONT_HGT_SMALL, getText("IGUI_SafehouseUI_Owner"), 1, 1, 1, 1, UIFont.Small, true)
    ownerLbl:initialise()
    ownerLbl:instantiate()
    self:addChild(ownerLbl)

    self.owner = ISLabel:new(ownerLbl:getRight() + 8, ownerLbl.y, FONT_HGT_SMALL, "", 0.6, 0.6, 0.8, 1.0, UIFont.Small, true)
    self.owner:initialise()
    self.owner:instantiate()
    self:addChild(self.owner)

    self.no = ISButton:new(self:getWidth() - btnWid - 10, 0, btnWid, btnHgt, getText("UI_Ok"), self, ISFactionUI.onClick);
    self.no.internal = "OK";
    self.no:initialise();
    self.no:instantiate();
    self.no.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.no);

    self.changeTitle = ISButton:new(0, self.owner.y, 70, btnHgt2, getText("IGUI_PlayerStats_Change"), self, ISFactionUI.onClick);
    self.changeTitle.internal = "CHANGETITLE";
    self.changeTitle:initialise();
    self.changeTitle:instantiate();
    self.changeTitle.borderColor = self.buttonBorderColor;
    self.changeTitle:setVisible(false);
    self:addChild(self.changeTitle);

    self.releaseFaction = ISButton:new(10, 0, 70, btnHgt, getText("IGUI_FactionUI_Remove"), self, ISFactionUI.onClick);
    self.releaseFaction.internal = "REMOVE";
    self.releaseFaction:initialise();
    self.releaseFaction:instantiate();
    self.releaseFaction.borderColor = self.buttonBorderColor;
    self:addChild(self.releaseFaction);
    self.releaseFaction.parent = self;
    self.releaseFaction:setVisible(false);

    self.changeOwnership = ISButton:new(0, ownerLbl.y, 70, btnHgt2, getText("IGUI_SafehouseUI_ChangeOwnership"), self, ISFactionUI.onClick);
    self.changeOwnership.internal = "CHANGEOWNERSHIP";
    self.changeOwnership:initialise();
    self.changeOwnership:instantiate();
    self.changeOwnership.borderColor = self.buttonBorderColor;
    self:addChild(self.changeOwnership);
    self.changeOwnership.parent = self;
    self.changeOwnership:setVisible(false);

    local tagLbl = ISLabel:new(10, ownerLbl:getBottom() + 10, FONT_HGT_SMALL, getText("IGUI_FactionUI_Tag"), 1, 1, 1, 1, UIFont.Small, true)
    tagLbl:initialise()
    tagLbl:instantiate()
    self:addChild(tagLbl)

    self.tag = ISLabel:new(tagLbl:getRight() + 8, tagLbl.y, FONT_HGT_SMALL, "", 0.6, 0.6, 0.8, 1.0, UIFont.Small, true)
    self.tag:initialise()
    self.tag:instantiate()
    self:addChild(self.tag)

    self.changeTag = ISButton:new(0, tagLbl.y, 70, btnHgt2, getText("IGUI_PlayerStats_Change"), self, ISFactionUI.onClick);
    self.changeTag.internal = "CHANGETAG";
    self.changeTag:initialise();
    self.changeTag:instantiate();
    self.changeTag.borderColor = self.buttonBorderColor;
    self:addChild(self.changeTag);
    self.changeTag.parent = self;
    self.changeTag:setVisible(false);

    self.tagColor = ISButton:new(0, tagLbl.y, btnHgt2, btnHgt2, "", self, ISFactionUI.onTagColor);
--    self.tagColor.internal = color;
    self.tagColor:initialise();
    self.tagColor.backgroundColor = {r = self.faction:getTagColor():getR(), g = self.faction:getTagColor():getG(), b = self.faction:getTagColor():getB(), a = 1};
    self:addChild(self.tagColor);
    self.tagColorBtn = self.tagColor;
    self.tagColor.factionUI = self;

    self.colorPicker = ISColorPicker:new(0, 0)
    self.colorPicker:initialise()
    self.colorPicker.pickedTarget = self
    self.colorPicker.resetFocusTo = self
    self.colorPicker:setInitialColor(self.faction:getTagColor());

    local playersLbl = ISLabel:new(10, tagLbl:getBottom() + 20, FONT_HGT_SMALL, getText("IGUI_SafehouseUI_Players"), 1, 1, 1, 1, UIFont.Small, true)
    playersLbl:initialise()
    playersLbl:instantiate()
    self:addChild(playersLbl)

    self.playerList = ISScrollingListBox:new(10, playersLbl:getBottom(), self.width - 20, (FONT_HGT_SMALL + 2 * 2) * 8);
    self.playerList:initialise();
    self.playerList:instantiate();
    self.playerList:setAnchorRight(true)
    self.playerList.itemheight = FONT_HGT_SMALL + 2 * 2;
    self.playerList.selected = 0;
    self.playerList.joypadParent = self;
    self.playerList.font = UIFont.NewSmall;
    self.playerList.doDrawItem = self.drawPlayers;
    self.playerList.drawBorder = true;
    self:addChild(self.playerList);

    self.removePlayer = ISButton:new(0, self.playerList.y + self.playerList.height + 5, 70, btnHgt2, getText("ContextMenu_Remove"), self, ISFactionUI.onClick);
    self.removePlayer.internal = "REMOVEPLAYER";
    self.removePlayer:initialise();
    self.removePlayer:instantiate();
    self.removePlayer.borderColor = self.buttonBorderColor;
    self.removePlayer:setWidthToTitle(70)
    self.removePlayer:setX(self.playerList:getRight() - self.removePlayer.width)
    self:addChild(self.removePlayer);
    self.removePlayer.enable = false;
    if not self.isOwner and not self.isAdmin then
--        self.removePlayer:setVisible(false);
    end

    self.showTag = ISTickBox:new(10, self.removePlayer:getBottom() + 20, getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_FactionUI_ShowTag")) + 20, 18, "", self, ISFactionUI.onClickShowTag);
    self.showTag:initialise();
    self.showTag:instantiate();
    self.showTag.selected[1] = self.player:isShowTag();
    self.showTag:addOption(getText("IGUI_FactionUI_ShowTag"));
    self:addChild(self.showTag);
    self.showTag.factionUI = self;

    self.factionPvp = ISTickBox:new(10, self.showTag.y + self.showTag.height + 5, getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_FactionUI_FactionPvp")) + 20, 18, "", self, ISFactionUI.onClickFactionPvp);
    self.factionPvp:initialise();
    self.factionPvp:instantiate();
    self.factionPvp.selected[1] = self.player:isFactionPvp();
    self.factionPvp:addOption(getText("IGUI_FactionUI_FactionPvp"));
    self:addChild(self.factionPvp);
    self.factionPvp.factionUI = self;
    self.factionPvp.tooltip = getText("IGUI_FactionUI_FactionPvpTooltip");

    self.quitFaction = ISButton:new(self.releaseFaction:getRight() + 10, 0, 70, btnHgt, getText("IGUI_SafehouseUI_QuitFaction"), self, ISFactionUI.onClick);
    self.quitFaction.internal = "QUITFACTION";
    self.quitFaction:initialise();
    self.quitFaction:instantiate();
    self.quitFaction.borderColor = self.buttonBorderColor;
    self:addChild(self.quitFaction);
    if self.isOwner then
        self.quitFaction:setVisible(false);
    end

    self.addPlayer = ISButton:new(self.playerList.x, self.playerList.y + self.playerList.height + 5, 70, btnHgt2, getText("IGUI_SafehouseUI_AddPlayer"), self, ISFactionUI.onClick);
    self.addPlayer.internal = "ADDPLAYER";
    self.addPlayer:initialise();
    self.addPlayer:instantiate();
    self.addPlayer.borderColor = self.buttonBorderColor;
    self:addChild(self.addPlayer);

    self.no:setY(self.factionPvp:getBottom() + 20)
    self.releaseFaction:setY(self.no.y)
    self.quitFaction:setY(self.no.y)
    self:setHeight(self.no:getBottom() + padBottom)

    self:populateList();

end

function ISFactionUI:onClickShowTag(clickedOption, enabled)
    self.player:setShowTag(enabled);
    sendPlayerStatsChange(self.player);
end

function ISFactionUI:onClickFactionPvp(clickedOption, enabled)
    self.player:setFactionPvp(enabled);
    sendPlayerStatsChange(self.player);
end

function ISFactionUI:onTagColor(button)
    self.colorPicker:setX(10);
    self.colorPicker:setY(20);
    self.colorPicker.pickedFunc = ISFactionUI.onPickedTagColor;
    self.colorPicker:setInitialColor(self.faction:getTagColor());
    self:addChild(self.colorPicker)
end

function ISFactionUI:onPickedTagColor(color, mouseUp)
    ISFactionUI.instance.tagColor.backgroundColor = { r=color.r, g=color.g, b=color.b, a = 1 }
    ISFactionUI.instance.faction:setTagColor(ColorInfo.new(color.r, color.g, color.b,1));
    ISFactionUI.instance.faction:syncFaction();
end

function ISFactionUI:populateList()
    self.playerList:clear();
    for i=0,self.faction:getPlayers():size()-1 do
--        if self.safehouse:getPlayers():get(i) ~= self.player:getUsername() then
            local newPlayer = {};
            newPlayer.name = self.faction:getPlayers():get(i);
            self.playerList:addItem(newPlayer.name, newPlayer);
--        end
    end
end

function ISFactionUI:drawPlayers(y, item, alt)
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

function ISFactionUI:render()
    self:updateButtons();

    self.removePlayer.enable = false;
    if self.playerList.selected > 0 then
        if self.isOwner or self.isAdmin then
            self.removePlayer.enable = true;
        end
        self.selectedPlayer = self.playerList.items[self.playerList.selected].item.name;
    else
        self.selectedPlayer = nil;
    end
    self.tagColor:setVisible(self.isOwner or self.isAdmin);
end

function ISFactionUI:prerender()
    local z = 20;
    local splitPoint = 100;
    local x = 10;
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawText(self.faction:getName(), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, self.faction:getName()) / 2), z, 1,1,1,1, UIFont.Medium);
    self.changeTitle:setY(z);
    self.changeTitle:setX(self.width/2 + ((getTextManager():MeasureStringX(UIFont.Medium, self.faction:getName()) / 2) + 10))
    z = z + FONT_HGT_MEDIUM + 20;
--    self:drawText(getText("IGUI_SafehouseUI_Owner"), x, z, 1,1,1,1, UIFont.Small);
--    local textWid = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_SafehouseUI_Owner"))
--    self:drawText(self.faction:getOwner(), x + textWid + 8, z, 1,1,1,1, UIFont.Small);
--    local textWid2 = getTextManager():MeasureStringX(UIFont.Small, self.faction:getOwner())
    self.owner:setName(self.faction:getOwner())
    if self.isOwner or self.isAdmin then
        self.releaseFaction:setVisible(true);
--        self.releaseFaction:setX(self.owner:getRight() + 8)
--        self.releaseFaction:setY(z);
        self.changeOwnership:setVisible(true);
        self.changeOwnership:setX(self.owner:getRight() + 8)
--        self.changeOwnership:setY(z);
    end
    z = z + FONT_HGT_SMALL + 8;
    if self.isOwner or self.isAdmin or self.faction:getTag() then
--        self:drawText(getText("IGUI_FactionUI_Tag"), x, z, 1,1,1,1, UIFont.Small);
--        self:drawText(self.faction:getTag(), splitPoint, z, 1,1,1,1, UIFont.Small);
        self.tag:setName(self.faction:getTag() or "")
        if self.isOwner or self.isAdmin then
            self.changeTag:setVisible(true);
            self.changeTag:setX(self.tag:getRight() + 8);
--            self.changeTag:setY(z);
            self.tagColor:setX(self.changeTag:getRight() + 8)
--            self.tagColor:setY(z);
            self.tagColor:setVisible(true);
        end
    end
--    self:drawText(getText("IGUI_SafehouseUI_Players"), x, self.playerList.y - FONT_HGT_SMALL, 1,1,1,1, UIFont.Small);
end

function ISFactionUI:updateButtons()
    if not self.isOwner and not self.isAdmin then
        self.releaseFaction:setVisible(false);
        self.changeOwnership:setVisible(false);
        self.removePlayer.enable = false;
        self.addPlayer.enable = false;
        self.changeTitle.enable = false;
    else
	    self.releaseFaction:setVisible(true);
        self.changeOwnership:setVisible(true);
        self.removePlayer.enable = true;
        self.addPlayer.enable = true;
        self.changeTitle.enable = true;
    end
    if self.faction:isMember(self.player:getUsername()) then
        self.quitFaction:setVisible(true);
    else
        self.quitFaction:setVisible(false);
    end

    self.changeTag.enable = false;
    self.tagColor.enable = false;
    -- add + 1 because the owner is not count in the players list
    if self.faction:getPlayers():size() + 1 >= getServerOptions():getInteger("FactionPlayersRequiredForTag") then
        self.changeTag.enable = true;
        self.tagColor.enable = true;
    end
end

function ISFactionUI:onClick(button)
    if button.internal == "OK" then
        self:close()
    end
    if button.internal == "REMOVE" then
        self.faction:removeFaction();
        self:close()
    end
    if button.internal == "REMOVEPLAYER" then
        local modal = ISModalDialog:new(0,0, 350, 150, getText("IGUI_FactionUI_RemoveConfirm", self.selectedPlayer), true, nil, ISFactionUI.onRemovePlayerFromFaction);
        modal:initialise()
        modal:addToUIManager()
        modal.ui = self;
        modal.moveWithMouse = true;
    end
    if button.internal == "ADDPLAYER" then
        local factionUI = ISFactionAddPlayerUI:new(getCore():getScreenWidth() / 2 - 200,getCore():getScreenHeight() / 2 - 175, 400, 350, self.faction, self.player);
        factionUI:initialise()
        factionUI:addToUIManager()
        factionUI.factionUI = self;
        self.addPlayerUI = factionUI;
    end
    if button.internal == "CHANGETITLE" then
        local modal = ISTextBox:new(self.x + 200, 200, 280, 180, getText("IGUI_SafehouseUI_ChangeTitle"), self.faction:getName(), nil, ISFactionUI.onChangeTitle);
        modal.faction = self.faction;
        modal:initialise();
        modal:addToUIManager();
    end
    if button.internal == "CHANGETAG" then
        local modal = ISCreateFactionTagUI:new(self.x + 200, 200, 280, 180, self.player, self.faction);
        modal:initialise();
        modal:addToUIManager();
    end
    if button.internal == "CHANGEOWNERSHIP" then
        local factionUI = ISFactionAddPlayerUI:new(getCore():getScreenWidth() / 2 - 200,getCore():getScreenHeight() / 2 - 175, 400, 350, self.faction, self.player);
        factionUI.changeOwnership = true;
        factionUI:initialise()
        factionUI:addToUIManager()
        factionUI.factionUI = self;
    end
    if button.internal == "QUITFACTION" then
        local modal = ISModalDialog:new(0,0, 350, 150, getText("IGUI_FactionUI_QuitSafeConfirm", self.selectedPlayer), true, nil, ISFactionUI.onQuitFaction);
        modal:initialise()
        modal:addToUIManager()
        modal.ui = self;
        modal.moveWithMouse = true;
    end
end

function ISFactionUI:close()
    self:setVisible(false)
    self:removeFromUIManager()
end

function ISFactionUI:onChangeTitle(button)
    if button.internal == "OK" then
        button.parent.faction:setName(button.parent.entry:getText());
        button.parent.faction:syncFaction();
    end
end

function ISFactionUI:onChangeTag(button)
    if button.internal == "OK" then
        button.parent.faction:setTag(button.parent.entry:getText());
        button.parent.faction:syncFaction();
    end
end

function ISFactionUI:onQuitFaction(button)
    if button.internal == "YES" then
        button.parent.ui.faction:removePlayer(button.parent.ui.player:getUsername());
        button.parent.ui:setVisible(false);
        button.parent.ui:removeFromUIManager();
    end
end

function ISFactionUI:onRemovePlayerFromFaction(button, player)
    if button.internal == "YES" then
        button.parent.ui.faction:removePlayer(button.parent.ui.selectedPlayer);
        button.parent.ui:populateList();
    end
end

--************************************************************************--
--** ISFactionUI:new
--**
--************************************************************************--
function ISFactionUI:new(x, y, width, height, faction, player)
    local o = {}
    x = getCore():getScreenWidth() / 2 - (width / 2);
    y = getCore():getScreenHeight() / 2 - (height / 2);
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.width = width;
    o.height = height;
    o.player = player;
    o.faction = faction;
    o.moveWithMouse = true;
    ISFactionUI.instance = o;
    o.buttonBorderColor = {r=0.7, g=0.7, b=0.7, a=0.5};
    o.isOwner = faction:isOwner(player:getUsername())
    o.isAdmin = player:getAccessLevel() ~= "None"
    return o;
end

function ISFactionUI:onAnswerFactionInvite(button)
    ISFactionUI.inviteDialogs[button.parent.host] = nil
    if button.internal == "YES" then
        -- FIXME: It seems strange the invited player adds herself to the faction, the host should do that.
        -- FIXME: Need to update the faction ui if it's visible.
        button.parent.faction:addPlayer(getPlayer():getUsername());
        acceptFactionInvite(button.parent.faction, button.parent.host)
    end
end

ISFactionUI.ReceiveFactionInvite = function(factionName, host)
    if ISFactionUI.inviteDialogs[host] then
        if ISFactionUI.inviteDialogs[host]:isReallyVisible() then return end
        ISFactionUI.inviteDialogs[host] = nil
    end
    -- FIXME: This can appear overtop MainScreen
    if not Faction.getPlayerFaction(getPlayer()) then
        local modal = ISModalDialog:new(getCore():getScreenWidth() / 2 - 175,getCore():getScreenHeight() / 2 - 75, 350, 150, getText("IGUI_FactionUI_Invitation", host, factionName), true, nil, ISFactionUI.onAnswerFactionInvite);
        modal:initialise()
        modal:addToUIManager()
        modal.faction = Faction.getFaction(factionName);
        modal.host = host;
        modal.moveWithMouse = true;
        ISFactionUI.inviteDialogs[host] = modal
    end
end

ISFactionUI.AcceptedFactionInvite = function(factionName, host)
    if ISFactionUI.instance and ISFactionUI.instance:isVisible() and factionName == ISFactionUI.instance.faction:getName() then
        if ISFactionUI.instance.addPlayerUI and ISFactionUI.instance.addPlayerUI:isVisible() then
            ISFactionUI.instance.addPlayerUI:populateList();
        end
        ISFactionUI.instance:populateList();
    end
end

ISFactionUI.SyncFaction = function(factionName)
    if ISFactionUI.instance and ISFactionUI.instance:isVisible() and factionName == ISFactionUI.instance.faction:getName() then
        if ISFactionUI.instance.addPlayerUI and ISFactionUI.instance.addPlayerUI:isVisible() then
            ISFactionUI.instance.addPlayerUI:populateList();
        end
        local faction = ISFactionUI.instance.faction
		ISFactionUI.instance.isOwner = faction:isOwner(ISFactionUI.instance.player:getUsername())
        ISFactionUI.instance.tagColor.backgroundColor = {r = faction:getTagColor():getR(), g = faction:getTagColor():getG(), b = faction:getTagColor():getB(), a = 1}
        ISFactionUI.instance:populateList();
		ISFactionUI.instance:updateButtons();
    end
end

Events.SyncFaction.Add(ISFactionUI.SyncFaction);
Events.ReceiveFactionInvite.Add(ISFactionUI.ReceiveFactionInvite);
Events.AcceptedFactionInvite.Add(ISFactionUI.AcceptedFactionInvite);

