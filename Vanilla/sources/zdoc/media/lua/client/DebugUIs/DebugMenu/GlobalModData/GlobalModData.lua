--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "ISUI/ISPanel"

GlobalModDataDebug = ISPanel:derive("GlobalModDataDebug");
GlobalModDataDebug.instance = nil;

local function roundstring(_val)
    return tostring(ISDebugUtils.roundNum(_val,2));
end

function GlobalModDataDebug.OnOpenPanel()
    if GlobalModDataDebug.instance==nil then
        GlobalModDataDebug.instance = GlobalModDataDebug:new (100, 100, 840, 600, "Global ModData Debugger");
        GlobalModDataDebug.instance:initialise();
        GlobalModDataDebug.instance:instantiate();
    end

    GlobalModDataDebug.instance:addToUIManager();
    GlobalModDataDebug.instance:setVisible(true);

    return GlobalModDataDebug.instance;
end

function GlobalModDataDebug:initialise()
    ISPanel.initialise(self);

    self.firstTableName = false;
end

function GlobalModDataDebug:createChildren()
    ISPanel.createChildren(self);

    ISDebugUtils.addLabel(self, {}, 10, 20, "Global ModData Debugger", UIFont.Medium, true)

    self.tableNamesList = ISScrollingListBox:new(10, 50, 200, self.height - 100);
    self.tableNamesList:initialise();
    self.tableNamesList:instantiate();
    self.tableNamesList.itemheight = 22;
    self.tableNamesList.selected = 0;
    self.tableNamesList.joypadParent = self;
    self.tableNamesList.font = UIFont.NewSmall;
    self.tableNamesList.doDrawItem = self.drawTableNameList;
    self.tableNamesList.drawBorder = true;
    self.tableNamesList.onmousedown = GlobalModDataDebug.OnTableNamesListMouseDown;
    self.tableNamesList.target = self;
    self:addChild(self.tableNamesList);

    self.infoList = ISScrollingListBox:new(220, 50, 600, self.height - 100);
    self.infoList:initialise();
    self.infoList:instantiate();
    self.infoList.itemheight = 22;
    self.infoList.selected = 0;
    self.infoList.joypadParent = self;
    self.infoList.font = UIFont.NewSmall;
    self.infoList.doDrawItem = self.drawInfoList;
    self.infoList.drawBorder = true;
    self:addChild(self.infoList);

    local y, obj = ISDebugUtils.addButton(self,"close",self.width-200,self.height-40,180,20,getText("IGUI_CraftUI_Close"),GlobalModDataDebug.onClickClose);
    y, obj = ISDebugUtils.addButton(self,"refresh",self.width-400,self.height-40,180,20,"Refresh",GlobalModDataDebug.onClickRefresh);

    self:populateList();
end

function GlobalModDataDebug:onClickClose()
    self:close();
end

function GlobalModDataDebug:onClickRefresh()
    self:populateList();
end

function GlobalModDataDebug:OnTableNamesListMouseDown(item)
    self:populateInfoList(item);
end

function GlobalModDataDebug:populateList()
    local tableNames = ModData.getTableNames();

    --if self.firstTableName and self.firstTableName==tableNames:get(0) then --todo remove this?
    --    return;
    --end

    self.tableNamesList:clear();

    if tableNames:size()==0 then
        self:populateInfoList(nil);
        return;
    end

    for i=0, tableNames:size()-1 do
        local name = tableNames:get(i);

        --print("found table name = "..tostring(name));
        self.tableNamesList:addItem(name, name);
    end

    self.firstTableName=tableNames:get(0);

    self:populateInfoList(self.firstTableName);
end

function GlobalModDataDebug:drawTableNameList(y, item, alt)
    local a = 0.9;

    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
    end

    self:drawText( item.text, 10, y + 2, 1, 1, 1, a, self.font);

    return y + self.itemheight;
end

function GlobalModDataDebug:formatVal(_value, _func, _func2)
    return _func2 and (_func2(_func(_value))) or (_func(_value));
end

function GlobalModDataDebug:parseTable(_t, _ident)
    if not _ident then
        _ident = "";
    end
    local s;
    for k,v in pairs(_t) do
        if type(v)=="table" then
            s = tostring(_ident).."["..tostring(k).."] -> ";
            self.infoList:addItem(s, nil);
            self:parseTable(v, _ident.."    ");
        else
            s = tostring(_ident).."["..tostring(k).."] -> "..tostring(v);
            self.infoList:addItem(s, nil);
        end
    end
end

function GlobalModDataDebug:populateInfoList(_name)
    self.infoList:clear();

    if not _name then
        self.infoList:addItem("No data.", nil);
        return;
    end
    --print("Attempting to draw table = "..tostring(_name));

    local modData = ModData.get(_name);

    if modData then
        self:parseTable(modData, "");
        --[[
        local s;
        for k,v in pairs(modData) do
            s = "["..tostring(k).."] -> "..tostring(v);
            self.infoList:addItem(s, nil);
        end
        --]]
    else
        self.infoList:addItem("Table not found.", nil);
    end
end


function GlobalModDataDebug:drawInfoList(y, item, alt)
    local a = 0.9;

    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
    end

    self:drawText( item.text, 10, y + 2, 1, 1, 1, a, self.font);

    return y + self.itemheight;
end

function GlobalModDataDebug:prerender()
    ISPanel.prerender(self);
    --self:populateList();
end

function GlobalModDataDebug:update()
    ISPanel.update(self);
end

function GlobalModDataDebug:close()
    self:setVisible(false);
    self:removeFromUIManager();
    GlobalModDataDebug.instance = nil
end

function GlobalModDataDebug:new(x, y, width, height, title)
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


