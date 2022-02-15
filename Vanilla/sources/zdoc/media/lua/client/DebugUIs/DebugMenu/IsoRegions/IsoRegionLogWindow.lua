--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "ISUI/ISPanel"

---@class IsoRegionLogWindow : ISPanel
IsoRegionLogWindow = ISPanel:derive("IsoRegionLogWindow");
IsoRegionLogWindow.instance = nil;

local function roundstring(_val)
    return tostring(ISDebugUtils.roundNum(_val,2));
end

function IsoRegionLogWindow.OnOpenPanel()
    if IsoRegionLogWindow.instance==nil then
        IsoRegionLogWindow.instance = IsoRegionLogWindow:new (100, 100, 1200, 600, "IsoRegions Logs");
        IsoRegionLogWindow.instance:initialise();
        IsoRegionLogWindow.instance:instantiate();
    end

    IsoRegionLogWindow.instance:addToUIManager();
    IsoRegionLogWindow.instance:setVisible(true);

    return IsoRegionLogWindow.instance;
end

function IsoRegionLogWindow:initialise()
    ISPanel.initialise(self);

    self.firstTableName = false;
end

function IsoRegionLogWindow:createChildren()
    ISPanel.createChildren(self);

    ISDebugUtils.addLabel(self, {}, 10, 20, "IsoRegions Logs", UIFont.Medium, true)

    self.tableNamesList = ISScrollingListBox:new(10, 50, 1180, self.height - 100);
    self.tableNamesList:initialise();
    self.tableNamesList:instantiate();
    self.tableNamesList.itemheight = 22;
    self.tableNamesList.selected = 0;
    self.tableNamesList.joypadParent = self;
    self.tableNamesList.font = UIFont.NewSmall;
    self.tableNamesList.doDrawItem = self.drawTableNameList;
    self.tableNamesList.drawBorder = true;
    self.tableNamesList.onmousedown = IsoRegionLogWindow.OnTableNamesListMouseDown;
    self.tableNamesList.target = self;
    self:addChild(self.tableNamesList);

    local y, obj = ISDebugUtils.addButton(self,"close",self.width-200,self.height-40,180,20,getText("IGUI_CraftUI_Close"),IsoRegionLogWindow.onClickClose);
    self:populateList();
end

function IsoRegionLogWindow:onClickClose()
    self:close();
end


function IsoRegionLogWindow:OnTableNamesListMouseDown(item)
    --self:populateInfoList(item);
end

function IsoRegionLogWindow:populateList()
    local logger = IsoRegions.getLogger();
    local logs = logger:getLogs();

    self.tableNamesList:clear();

    logger:unsetDirtyUI();

    if logs:size()==0 then
        --self:populateInfoList(nil);
        return;
    end

    for i=0, logs:size()-1 do
        local log = logs:get(i);

        --print("found table name = "..tostring(name));
        self.tableNamesList:addItem("log", {txt = log:getStr(), type = log:getType(), col = log:getColor()});
    end

    self.firstTableName=logs:get(0);

    self.tableNamesList:ensureVisible(#self.tableNamesList.items);
end

function IsoRegionLogWindow:drawTableNameList(y, item, alt)
    local a = 0.9;

    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
    end

    local c = item.item.col;

    self:drawText( item.item.txt, 10, y + 2, c:getRedFloat(), c:getGreenFloat(), c:getBlueFloat(), a, self.font);

    return y + self.itemheight;
end

function IsoRegionLogWindow:prerender()
    ISPanel.prerender(self);
    --self:populateList();
end

function IsoRegionLogWindow:update()
    ISPanel.update(self);

    local logger = IsoRegions.getLogger();
    if logger:isDirtyUI() then
        self:populateList();
    end
end

function IsoRegionLogWindow:close()
    self:setVisible(false);
    self:removeFromUIManager();
    IsoRegionLogWindow.instance = nil
end

function IsoRegionLogWindow:new(x, y, width, height, title)
    local o = {};
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self);
    self.__index = self;
    o.variableColor={r=0.9, g=0.55, b=0.1, a=1};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.buttonBorderColor = {r=0.7, g=0.7, b=0.7, a=0.5};
    o.zOffsetSmallFont = 25;
    o.moveWithMouse = true;
    o.panelTitle = title;
    ISDebugMenu.RegisterClass(self);
    return o;
end
