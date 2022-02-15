--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 16/02/2017
-- Time: 11:23
-- To change this template use File | Settings | File Templates.
--

---@class StashDebug : ISPanel
StashDebug = ISPanel:derive("StashDebug");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

--************************************************************************--
--** StashDebug:initialise
--**
--************************************************************************--

function StashDebug:initialise()
    ISPanel.initialise(self);
    local btnWid1 = getTextManager():MeasureStringX(UIFont.Small, getText("UI_Cancel")) + 10
    local btnWid2 = getTextManager():MeasureStringX(UIFont.Small, "Spawn") + 10
    local btnWid3 = getTextManager():MeasureStringX(UIFont.Small, "Reinitialize") + 10
    local btnWid = math.max(btnWid1, btnWid2, btnWid3, 100)
    local btnHgt = math.max(25, FONT_HGT_SMALL + 2 * 2)
    local padBottom = 10

    self.no = ISButton:new(10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Cancel"), self, StashDebug.onClick);
    self.no.internal = "CANCEL";
    self.no.anchorTop = false
    self.no.anchorBottom = true
    self.no:initialise();
    self.no:instantiate();
    self.no.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.no);

    self.datas = ISScrollingListBox:new(10, 50, self.width - 20, self.height - padBottom - btnHgt - 2 - btnHgt - 2 - 50);
    self.datas:initialise();
    self.datas:instantiate();
    self.datas.selected = 0;
    self.datas.joypadParent = self;
    self.datas.doDrawItem = self.drawDatas;
    self.datas.drawBorder = true;
    self:addChild(self.datas);
    self.datas:setFont(UIFont.Small, 2)

    self.viewBtn = ISButton:new(self:getWidth() - btnWid - 10,  self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, "Spawn", self, StashDebug.onClick);
    self.viewBtn.internal = "SPAWN";
    self.viewBtn.anchorTop = false
    self.viewBtn.anchorBottom = true
    self.viewBtn:initialise();
    self.viewBtn:instantiate();
    self.viewBtn.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.viewBtn);
    self.viewBtn.enable = false;

    self.reinitBtn = ISButton:new(self:getWidth() - btnWid - 10,  self.viewBtn.y - btnHgt - 2, btnWid, btnHgt, "Reinitialize", self, StashDebug.onClick);
    self.reinitBtn.internal = "REINIT";
    self.reinitBtn.anchorTop = false
    self.reinitBtn.anchorBottom = true
    self.reinitBtn:initialise();
    self.reinitBtn:instantiate();
    self.reinitBtn.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.reinitBtn);

    self:populateList();

end

function StashDebug:populateList()
    self.datas:clear();
    for i=0,StashSystem.getPossibleStashes():size()-1 do
        local stash = StashSystem.getStash(StashSystem.getPossibleStashes():get(i):getName());
        self.datas:addItem(stash:getName(), stash);
    end
end

function StashDebug:drawDatas(y, item, alt)
    local a = 0.9;

    --    self.parent.selectedStash = nil;
    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
        self.parent.viewBtn.enable = true;
        self.parent.selectedStash = item.item;
    end

    self:drawText( item.item:getName(), 10, y + 2, 1, 1, 1, a, self.font);

    return y + self.itemheight;
end

function StashDebug:prerender()
    local z = (self.datas.y - FONT_HGT_MEDIUM) / 2;
    local splitPoint = 100;
    local x = 10;
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawTextCentre("Stash Debug UI", self.width/2, z, 1,1,1,1, UIFont.Medium);
end

function StashDebug:onClick(button)
    if button.internal == "CANCEL" then
--        self:setVisible(false);
        self:removeFromUIManager();
    end
    if button.internal == "SPAWN" then
        if self.selectedStash then
            local map = InventoryItemFactory.CreateItem(self.selectedStash:getItem());
            StashSystem.doStashItem(self.selectedStash, map);
            getPlayer():getInventory():AddItem(map);
            local mapUI = ISMap:new(0, 0, 0, 0, map, 0);
--            mapUI:initialise();
--            mapUI:addToUIManager();
--            local wrap = mapUI:wrapInCollapsableWindow(map:getName());
--            wrap:addToUIManager();
--            wrap:setResizable(false);
--            wrap:setInfo(getText("IGUI_Map_Info"));
--            mapUI.wrap = wrap;
--            wrap.render = ISMap.renderWrap;
--            wrap.prerender = ISMap.prerenderWrap;
--            wrap.setVisible = ISMap.setWrapVisible;
--            wrap.mapUI = mapUI;
--            mapUI.render = ISMap.noRender;
--            mapUI.prerender = ISMap.noRender;
            map:doBuildingStash();
            getPlayer():setX(self.selectedStash:getBuildingX() + 2);
            getPlayer():setY(self.selectedStash:getBuildingY() + 2);
            getPlayer():setLx(self.selectedStash:getBuildingX() + 2);
            getPlayer():setLy(self.selectedStash:getBuildingY() + 2);
            self:populateList();
        end
    end
    if button.internal == "REINIT" then
        StashSystem.reinit();
        self:populateList();
    end

end

function StashDebug.OnOpenPanel()
    if not StashDebug.instance then
        local ui = StashDebug:new(0, 0, 300, 600);
        ui:initialise();
        StashDebug.instance = ui;
    else
        StashDebug.instance:populateList();
    end
    StashDebug.instance:addToUIManager();
end

--************************************************************************--
--** StashDebug:new
--**
--************************************************************************--
function StashDebug:new(x, y, width, height)
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
    o.selectedStash = nil;
    o.moveWithMouse = true;
    ISDebugMenu.RegisterClass(self);
    return o;
end
