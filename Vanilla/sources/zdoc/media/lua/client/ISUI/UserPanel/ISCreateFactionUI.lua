--***********************************************************
--**              	  ROBERT JOHNSON                       **
--**            UI display with a question or text         **
--**          can display a yes/no button or ok btn        **
--***********************************************************

---@class ISCreateFactionUI : ISPanel
ISCreateFactionUI = ISPanel:derive("ISCreateFactionUI");
ISCreateFactionUI.messages = {};

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)
local FONT_HGT_LARGE = getTextManager():getFontHeight(UIFont.Large)

--************************************************************************--
--** ISCreateFactionUI:initialise
--**
--************************************************************************--

function ISCreateFactionUI:initialise()
    ISPanel.initialise(self);
    local btnWid = 100
    local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local padBottom = 10

    local nameLbl = ISLabel:new(10, 20 + FONT_HGT_MEDIUM + 20, FONT_HGT_SMALL + 2 * 2, getText("IGUI_FactionUI_FactionName"), 1, 1, 1, 1, UIFont.Small, true)
    nameLbl:initialise()
    nameLbl:instantiate()
    self:addChild(nameLbl)

    self.entry = ISTextEntryBox:new("", nameLbl:getRight() + 8, nameLbl.y, 150, FONT_HGT_SMALL + 2 * 2);
    self.entry.font = UIFont.Small;
    self.entry:initialise();
    self.entry:instantiate();
    self:addChild(self.entry);

    self.no = ISButton:new(self:getWidth() - btnWid - 10, self.entry:getBottom() + 20, btnWid, btnHgt, getText("UI_Cancel"), self, ISCreateFactionUI.onClick);
    self.no.internal = "CANCEL";
    self.no:initialise();
    self.no:instantiate();
    self.no.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.no);

    self.ok = ISButton:new(10, self.no.y, btnWid, btnHgt, getText("UI_Ok"), self, ISCreateFactionUI.onClick);
    self.ok.internal = "OK";
    self.ok:initialise();
    self.ok:instantiate();
    self.ok.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.ok);

    self:setHeight(self.no:getBottom() + padBottom)
end


function ISCreateFactionUI:render()
    self:updateButtons();
end

function ISCreateFactionUI:prerender()
    local z = 20;
    local splitPoint = 100;
    local x = 10;
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawText(getText("IGUI_FactionUI_CreateFaction"), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, getText("IGUI_FactionUI_CreateFaction")) / 2), z, 1,1,1,1, UIFont.Medium);

--    z = z + FONT_HGT_MEDIUM + 10;
--    self:drawText(getText("IGUI_FactionUI_FactionName"), 10, z + 2, 1,1,1,1, UIFont.Small);
--    self.entry:setY(z)
--    self.entry:setX(getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_FactionUI_FactionName")) + 15);
end

function ISCreateFactionUI:updateButtons()
    self.ok.enable = true;
    self.ok.tooltip = nil;
    local factionName = self.entry:getInternalText();
    if Faction.factionExist(factionName) then
        self.ok.enable = false;
        self.ok.tooltip = getText("IGUI_FactionUI_FactionAlreadyExist");
    end
    if factionName:len() < 3 or factionName:len() > 15 then
        self.ok.enable = false;
        self.ok.tooltip = getText("IGUI_FactionUI_FactionNameTooLongShort");
    end
    if not Faction.canCreateFaction(self.player) then
        self.ok.enable = false;
        self.ok.tooltip = getText("IGUI_FactionUI_FactionSurvivalDay", getServerOptions():getInteger("FactionDaySurvivedToCreate"));
    end
end

function ISCreateFactionUI:onClick(button)
    if button.internal == "CANCEL" then
        self:close()
    end
    if button.internal == "OK" then
        local faction = Faction.createFaction(self.entry:getInternalText(), self.player:getUsername());
        self:setVisible(false);
        self:removeFromUIManager();
        local modal = ISFactionUI:new(getCore():getScreenWidth() / 2 - 250, getCore():getScreenHeight() / 2 - 225, 500, 450, faction, self.player);
        modal:initialise();
        modal:addToUIManager();
    end
end

function ISCreateFactionUI:close()
    self:setVisible(false)
    self:removeFromUIManager()
end

--************************************************************************--
--** ISCreateFactionUI:new
--**
--************************************************************************--
function ISCreateFactionUI:new(x, y, width, height, player)
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
    o.moveWithMouse = true;
    ISCreateFactionUI.instance = o;
    o.buttonBorderColor = {r=0.7, g=0.7, b=0.7, a=0.5};
    return o;
end
