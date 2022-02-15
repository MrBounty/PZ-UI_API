--***********************************************************
--**              	  ROBERT JOHNSON                       **
--***********************************************************

---@class ISAddNonPvpZoneUI : ISPanel
ISAddNonPvpZoneUI = ISPanel:derive("ISAddNonPvpZoneUI");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

--************************************************************************--
--** ISAddNonPvpZoneUI:initialise
--**
--************************************************************************--

function ISAddNonPvpZoneUI:initialise()
    ISPanel.initialise(self);
    local btnWid = 100
    local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local btnHgt2 = FONT_HGT_SMALL + 2 * 2
    local padBottom = 10

--    self.parentUI:setVisible(false);

    self.cancel = ISButton:new(self:getWidth() - btnWid - 10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Cancel"), self, ISAddNonPvpZoneUI.onClick);
    self.cancel.internal = "CANCEL";
    self.cancel.anchorTop = false
    self.cancel.anchorBottom = true
    self.cancel:initialise();
    self.cancel:instantiate();
    self.cancel.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.cancel);

    self.ok = ISButton:new(10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("IGUI_PvpZone_AddZone"), self, ISAddNonPvpZoneUI.onClick);
    self.ok.internal = "OK";
    self.ok.anchorTop = false
    self.ok.anchorBottom = true
    self.ok:initialise();
    self.ok:instantiate();
    self.ok.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.ok);

    self.defineStartingPointBtn = ISButton:new(10, 0, btnWid, btnHgt2, getText("IGUI_PvpZone_RedefineStartingPoint"), self, ISAddNonPvpZoneUI.onClick);
    self.defineStartingPointBtn.internal = "DEFINESTARTINGPOINT";
    self.defineStartingPointBtn.anchorTop = false
    self.defineStartingPointBtn.anchorBottom = true
    self.defineStartingPointBtn:initialise();
    self.defineStartingPointBtn:instantiate();
    self.defineStartingPointBtn.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.defineStartingPointBtn);

    local title = "Zone #" .. NonPvpZone.getAllZones():size() + 1;

    self.titleEntry = ISTextEntryBox:new(title, 10, 10, 100, FONT_HGT_SMALL + 2 * 2);
    self.titleEntry:initialise();
    self.titleEntry:instantiate();
    self:addChild(self.titleEntry);
end

function ISAddNonPvpZoneUI:prerender()
    local z = 20;
    local splitPoint = 200;
    local x = 10;
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawText(getText("IGUI_PvpZone_AddZone"), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, getText("IGUI_PvpZone_AddZone")) / 2), z, 1,1,1,1, UIFont.Medium);

    z = z + FONT_HGT_MEDIUM + 20;
    self:drawText(getText("IGUI_PvpZone_ZoneName"), x, z + 2,1,1,1,1,UIFont.Small);
    self.titleEntry:setY(z);
    self.titleEntry:setX(splitPoint);
    z = z + FONT_HGT_SMALL + 15;
    self:drawText(getText("IGUI_PvpZone_StartingPoint"), x, z,1,1,1,1,UIFont.Small);
    self:drawText(luautils.round(self.startingX,0) .. " x " .. luautils.round(self.startingY,0), splitPoint, z,1,1,1,1,UIFont.Small);
    z = z + FONT_HGT_SMALL + 15;
    self:drawText(getText("IGUI_PvpZone_CurrentPoint"), x, z,1,1,1,1,UIFont.Small);
    self:drawText(luautils.round(self.player:getX(),0) .. " x " .. luautils.round(self.player:getY(),0), splitPoint, z,1,1,1,1,UIFont.Small);
    z = z + FONT_HGT_SMALL + 15;
    local startingX = luautils.round(self.startingX,0);
    local startingY = luautils.round(self.startingY,0);
    local endX = luautils.round(self.player:getX(),0);
    local endY = luautils.round(self.player:getY(),0);
    if startingX > endX then
        local x2 = endX;
        endX = startingX;
        startingX = x2;
    end
    if startingY > endY then
        local y2 = endY;
        endY = startingY;
        startingY = y2;
    end
    local width = math.abs(startingX - endX) * 2;
    local height = math.abs(startingY - endY) * 2;
    self:drawText(getText("IGUI_PvpZone_CurrentZoneSize"), x, z,1,1,1,1,UIFont.Small);
    self.size = math.max(luautils.round(width + height,0),1);
    self:drawText(self.size .. "", splitPoint, z,1,1,1,1,UIFont.Small);
    z = z + FONT_HGT_SMALL + 15;
    self.defineStartingPointBtn:setY(z);

    for x2=startingX, endX do
        for y=startingY, endY do
            local sq = getCell():getGridSquare(x2,y,0);
            if sq then
                for n = 0,sq:getObjects():size()-1 do
                    local obj = sq:getObjects():get(n);
                    obj:setHighlighted(true);
                    obj:setHighlightColor(0.6,1,0.6,0.5);
                end
           end
        end
    end

    self:updateButtons();
end

function ISAddNonPvpZoneUI:updateButtons()
    self.ok.enable = self.size > 1;
end

function ISAddNonPvpZoneUI:onClick(button)
    if button.internal == "OK" then
        local doneIt = true;
        if NonPvpZone.getZoneByTitle(self.titleEntry:getInternalText()) then
            doneIt = false;
            local modal = ISModalDialog:new(0,0, 350, 150, getText("IGUI_PvpZone_ZoneAlreadyExistTitle", self.selectedPlayer), false, nil, nil);
            modal:initialise()
            modal:addToUIManager()
            modal.moveWithMouse = true;
        end
        if doneIt then
            self:setVisible(false);
            self:removeFromUIManager();
            NonPvpZone.addNonPvpZone(self.titleEntry:getInternalText(), luautils.round(self.startingX,0), luautils.round(self.startingY,0),luautils.round(self.player:getX(),0),luautils.round(self.player:getY(),0))
        else
            return;
        end
    end
    if button.internal == "DEFINESTARTINGPOINT" then
        self.startingX = self.player:getX();
        self.startingY = self.player:getY();
        self.endX = self.player:getX();
        self.endY = self.player:getY();
        return;
    end
    if button.internal == "CANCEL" then
        self:setVisible(false);
        self:removeFromUIManager();
    end
    self.parentUI:populateList();
    self.parentUI:setVisible(true);
    self.player:setSeeNonPvpZone(false);
end

--************************************************************************--
--** ISAddNonPvpZoneUI:new
--**
--************************************************************************--
function ISAddNonPvpZoneUI:new(x, y, width, height, player)
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
    o.startingX = player:getX();
    o.startingY = player:getY();
    o.endX = player:getX();
    o.endY = player:getY();
    player:setSeeNonPvpZone(true);
    o.moveWithMouse = true;
    ISAddNonPvpZoneUI.instance = o;
    o.buttonBorderColor = {r=0.7, g=0.7, b=0.7, a=0.5};
    return o;
end
