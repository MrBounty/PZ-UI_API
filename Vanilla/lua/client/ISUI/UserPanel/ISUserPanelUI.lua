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

ISUserPanelUI = ISPanel:derive("ISUserPanelUI");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)
local FONT_HGT_LARGE = getTextManager():getFontHeight(UIFont.Large)

--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISUserPanelUI:initialise()
    ISPanel.initialise(self);
    self:create();
end


function ISUserPanelUI:setVisible(visible)
    --    self.parent:setVisible(visible);
    self.javaObject:setVisible(visible);
end

function ISUserPanelUI:render()
    local z = 20;

    self:drawText(getText("UI_mainscreen_userpanel"), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, getText("UI_mainscreen_userpanel")) / 2), z, 1,1,1,1, UIFont.Medium);
    z = z + 30;

    self:updateButtons();

end

function ISUserPanelUI:create()
    local btnWid = 150
    local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local padBottom = 10

    local y = 70;

    self.factionBtn = ISButton:new(10, y, btnWid, btnHgt, getText("UI_userpanel_factionpanel"), self, ISUserPanelUI.onOptionMouseDown);
    self.factionBtn.internal = "FACTIONPANEL";
    self.factionBtn:initialise();
    self.factionBtn:instantiate();
    self.factionBtn.borderColor = self.buttonBorderColor;
    self:addChild(self.factionBtn);
    y = y + btnHgt + 5;

    if SafeHouse.hasSafehouse(self.player) then
        self.safehouseBtn = ISButton:new(10, y, btnWid, btnHgt, getText("IGUI_SafehouseUI_Safehouse"), self, ISUserPanelUI.onOptionMouseDown);
        self.safehouseBtn.internal = "SAFEHOUSEPANEL";
        self.safehouseBtn:initialise();
        self.safehouseBtn:instantiate();
        self.safehouseBtn.borderColor = self.buttonBorderColor;
        self:addChild(self.safehouseBtn);
        y = y + btnHgt + 5;
    end

    self.ticketsBtn = ISButton:new(10, y, btnWid, btnHgt, getText("UI_userpanel_tickets"), self, ISUserPanelUI.onOptionMouseDown);
    self.ticketsBtn.internal = "TICKETS";
    self.ticketsBtn:initialise();
    self.ticketsBtn:instantiate();
    self.ticketsBtn.borderColor = self.buttonBorderColor;
    self:addChild(self.ticketsBtn);
    y = y + btnHgt + 5;

    if not Faction.isAlreadyInFaction(self.player) then
        self.factionBtn.title = getText("IGUI_FactionUI_CreateFaction");
        if not Faction.canCreateFaction(self.player) then
            self.factionBtn.enable = false;
            self.factionBtn.tooltip = getText("IGUI_FactionUI_FactionSurvivalDay", getServerOptions():getInteger("FactionDaySurvivedToCreate"));
        end
        self.factionBtn:setWidthToTitle(self.factionBtn.width)
    end
    
    self.serverOptionBtn = ISButton:new(10, y, btnWid, btnHgt, getText("IGUI_AdminPanel_SeeServerOptions"), self, ISUserPanelUI.onOptionMouseDown);
    self.serverOptionBtn.internal = "SERVEROPTIONS";
    self.serverOptionBtn:initialise();
    self.serverOptionBtn:instantiate();
    self.serverOptionBtn.borderColor = self.buttonBorderColor;
    self:addChild(self.serverOptionBtn);
    y = y + btnHgt + 5;

    local width = 0
    for _,child in pairs(self:getChildren()) do
        width = math.max(width, child:getWidth())
    end
    for _,child in pairs(self:getChildren()) do
        child:setWidth(width)
    end

    self:setWidth(10 + width + 20 + width + 10)

    self.cancel = ISButton:new((self:getWidth() / 2) + 5, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_btn_close"), self, ISUserPanelUI.onOptionMouseDown);
    self.cancel.internal = "CANCEL";
    self.cancel:initialise();
    self.cancel:instantiate();
    self.cancel.borderColor = self.buttonBorderColor;
    self:addChild(self.cancel);
end

function ISUserPanelUI:updateButtons()
    if not Faction.isAlreadyInFaction(self.player) then
        self.factionBtn.title = getText("IGUI_FactionUI_CreateFaction");
        if not Faction.canCreateFaction(self.player) then
            self.factionBtn.enable = false;
            self.factionBtn.tooltip = getText("IGUI_FactionUI_FactionSurvivalDay", getServerOptions():getInteger("FactionDaySurvivedToCreate"));
        end
    else
        self.factionBtn.title = getText("UI_userpanel_factionpanel");
    end
end

function ISUserPanelUI:onOptionMouseDown(button, x, y)
    if button.internal == "SAFEHOUSEPANEL" then
        if SafeHouse.hasSafehouse(self.player) then
            local modal = ISSafehouseUI:new(getCore():getScreenWidth() / 2 - 250, getCore():getScreenHeight() / 2 - 225, 500, 450, SafeHouse.hasSafehouse(self.player), self.player);
            modal:initialise();
            modal:addToUIManager();
        end
    end
    if button.internal == "FACTIONPANEL" then
        if ISFactionUI.instance then
            ISFactionUI.instance:close()
        end
        if ISCreateFactionUI.instance then
            ISCreateFactionUI.instance:close()
        end
        if Faction.isAlreadyInFaction(self.player) then
            local modal = ISFactionUI:new(getCore():getScreenWidth() / 2 - 250, getCore():getScreenHeight() / 2 - 225, 500, 450, Faction.getPlayerFaction(self.player), self.player);
            modal:initialise();
            modal:addToUIManager();
        else
            local modal = ISCreateFactionUI:new(self.x + 100, self.y + 100, 350, 250, self.player)
            modal:initialise();
            modal:addToUIManager();
        end
    end
    if button.internal == "TICKETS" then
        if ISTicketsUI.instance then
            ISTicketsUI.instance:close()
        end
        local modal = ISTicketsUI:new(getCore():getScreenWidth() / 2 - 250, getCore():getScreenHeight() / 2 - 225, 500, 450, self.player);
        modal:initialise();
        modal:addToUIManager();
    end
    if button.internal == "SERVEROPTIONS" then
        if ISServerOptions.instance then
            ISServerOptions.instance:close()
        end
        local ui = ISServerOptions:new(50,50,600,600, self.player)
        ui:initialise();
        ui:addToUIManager();
    end
    if button.internal == "CANCEL" then
        self:close()
    end
end

function ISUserPanelUI:close()
    self:setVisible(false)
    self:removeFromUIManager()
    ISUserPanelUI.instance = nil;
end

function ISUserPanelUI:new(x, y, width, height, player)
    local o = {};
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self);
    self.__index = self;
    self.player = player;
    o.variableColor={r=0.9, g=0.55, b=0.1, a=1};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.buttonBorderColor = {r=0.7, g=0.7, b=0.7, a=0.5};
    o.zOffsetSmallFont = 25;
    o.moveWithMouse = true;
    ISUserPanelUI.instance = o
    return o;
end
