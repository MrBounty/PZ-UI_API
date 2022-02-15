--***********************************************************
--**              	  ROBERT JOHNSON                       **
--***********************************************************

---@class ISSafehousesList : ISPanel
ISSafehousesList = ISPanel:derive("ISSafehousesList");
ISSafehousesList.messages = {};

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

--************************************************************************--
--** ISSafehousesList:initialise
--**
--************************************************************************--

function ISSafehousesList:initialise()
    ISPanel.initialise(self);
    local btnWid = 100
    local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local padBottom = 10

    self.no = ISButton:new(10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("IGUI_CraftUI_Close"), self, ISSafehousesList.onClick);
    self.no.internal = "CANCEL";
    self.no.anchorTop = false
    self.no.anchorBottom = true
    self.no:initialise();
    self.no:instantiate();
    self.no.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.no);

    local listY = 20 + FONT_HGT_MEDIUM + 20
    self.datas = ISScrollingListBox:new(10, listY, self.width - 20, self.height - padBottom - btnHgt - padBottom - listY);
    self.datas:initialise();
    self.datas:instantiate();
    self.datas.itemheight = FONT_HGT_SMALL + 2 * 2;
    self.datas.selected = 0;
    self.datas.joypadParent = self;
    self.datas.font = UIFont.NewSmall;
    self.datas.doDrawItem = self.drawDatas;
    self.datas.drawBorder = true;
    self:addChild(self.datas);

    self.teleportBtn = ISButton:new(self:getWidth() - btnWid - 10,  self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("IGUI_PlayerStats_Teleport"), self, ISSafehousesList.onClick);
    self.teleportBtn.internal = "TELEPORT";
    self.teleportBtn.anchorTop = false
    self.teleportBtn.anchorBottom = true
    self.teleportBtn:initialise();
    self.teleportBtn:instantiate();
    self.teleportBtn.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.teleportBtn);
    self.teleportBtn.enable = false;

    self.viewBtn = ISButton:new(self.teleportBtn.x - self.teleportBtn.width - 10,  self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("IGUI_PlayerStats_View"), self, ISSafehousesList.onClick);
    self.viewBtn.internal = "VIEW";
    self.viewBtn.anchorTop = false
    self.viewBtn.anchorBottom = true
    self.viewBtn:initialise();
    self.viewBtn:instantiate();
    self.viewBtn.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.viewBtn);
    self.viewBtn.enable = false;

    self:populateList();

end

function ISSafehousesList:populateList()
    self.datas:clear();
    for i=0,SafeHouse.getSafehouseList():size()-1 do
        local safe = SafeHouse.getSafehouseList():get(i);
       self.datas:addItem(safe:getTitle(), safe);
    end
end

function ISSafehousesList:drawDatas(y, item, alt)
    local a = 0.9;

--    self.parent.selectedSafehouse = nil;
    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
        self.parent.teleportBtn.enable = true;
        self.parent.viewBtn.enable = true;
        self.parent.selectedSafehouse = item.item;
    end

    self:drawText(item.item:getTitle() .. " - " .. getText("IGUI_FactionUI_FactionsListPlayers", item.item:getPlayers():size() + 1, item.item:getOwner()), 10, y + 2, 1, 1, 1, a, self.font);

    return y + self.itemheight;
end

function ISSafehousesList:prerender()
    local z = 20;
    local splitPoint = 100;
    local x = 10;
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawText(getText("IGUI_AdminPanel_SeeSafehouses"), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, getText("IGUI_AdminPanel_SeeSafehouses")) / 2), z, 1,1,1,1, UIFont.Medium);
    z = z + 30;
end

function ISSafehousesList:onClick(button)
    if button.internal == "CANCEL" then
        self:close()
    end
    if button.internal == "TELEPORT" then
        self.player:setX(self.selectedSafehouse:getX());
        self.player:setY(self.selectedSafehouse:getY());
        self.player:setZ(0);
        self.player:setLx(self.selectedSafehouse:getX());
        self.player:setLy(self.selectedSafehouse:getY());
        self.player:setLz(0);
    end
    if button.internal == "VIEW" then
        local safehouseUI = ISSafehouseUI:new(getCore():getScreenWidth() / 2 - 250,getCore():getScreenHeight() / 2 - 225, 500, 450, self.selectedSafehouse, self.player);
        safehouseUI:initialise()
        safehouseUI:addToUIManager()
    end
end

function ISSafehousesList:close()
    self:setVisible(false)
    self:removeFromUIManager()
    ISSafehousesList.instance = nil
end

--************************************************************************--
--** ISSafehousesList:new
--**
--************************************************************************--
function ISSafehousesList:new(x, y, width, height, player)
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
    o.selectedFaction = nil;
    o.moveWithMouse = true;
    ISSafehousesList.instance = o;
    return o;
end

function ISSafehousesList.OnSafehousesChanged()
    if ISSafehousesList.instance then
        ISSafehousesList.instance:populateList()
    end
end

Events.OnSafehousesChanged.Add(ISSafehousesList.OnSafehousesChanged)

