--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "ISUI/ISPanel"

---@class ISWhitelistViewer : ISPanel
ISWhitelistViewer = ISPanel:derive("ISWhitelistViewer");
ISWhitelistViewer.bottomInfoHeight = 40;

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

function ISWhitelistViewer:initialise()
    ISPanel.initialise(self);
end


function ISWhitelistViewer:render()
    ISPanel.render(self);
    local z = 10;
    self:drawText(getText("IGUI_DbViewer_DbViewer"), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, getText("IGUI_DbViewer_DbViewer")) / 2), z, 1,1,1,1, UIFont.Medium);

    if self.activeView then
        if self.activeView.loading then
            self.refreshBtn.enable = false;
        else
            self.refreshBtn.enable = true;
        end
    end

    self:refreshButtons();
end

function ISWhitelistViewer:onActivateView()
--    self.panel.activeView.view.recipes:ensureVisible(self.panel.activeView.view.recipes.selected);
--    print("clicked on", self.panel.activeView.view.tableName)
    getTableResult(self.panel.activeView.view.tableName, self.panel.activeView.view.entriesPerPages);
    self.activeView = self.panel.activeView.view;
    self.activeView:clear();
end

function ISWhitelistViewer:refreshButtons()
    if self.activeView then
        if self.canModify then
            self.delete.enable = self.activeView.datas.selected > 0;
            self.modify.enable = self.activeView.datas.selected > 0;
        end
    end
end

function ISWhitelistViewer:createChildren()
    ISPanel.createChildren(self);

    local btnWid = 100
    local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local padBottom = 10

    self.panel = ISTabPanel:new(1, 50, self.width - 2, self.height - 50 - 1);
    self.panel:initialise();
    self.panel.borderColor = { r = 0, g = 0, b = 0, a = 0};
    self.panel.onActivateView = ISWhitelistViewer.onActivateView;
    self.panel.target = self;
--    self.panel:setEqualTabWidth(false)
    self:addChild(self.panel);

    self.close = ISButton:new(self:getWidth() - 100 - 10, self:getHeight() - btnHgt - padBottom, btnWid, btnHgt, getText("IGUI_CraftUI_Close"), self, ISWhitelistViewer.onOptionMouseDown);
    self.close.internal = "CLOSE";
    self.close:initialise();
    self.close:instantiate();
    self.close.borderColor = self.buttonBorderColor;
    self:addChild(self.close);

    self.refreshBtn = ISButton:new(self:getWidth() - 200 - 15, self:getHeight() - btnHgt - padBottom, btnWid, btnHgt, getText("IGUI_DbViewer_Refresh"), self, ISWhitelistViewer.onOptionMouseDown);
    self.refreshBtn.internal = "REFRESH";
    self.refreshBtn:initialise();
    self.refreshBtn:instantiate();
    self.refreshBtn.borderColor = self.buttonBorderColor;
    self:addChild(self.refreshBtn);

    self.modify = ISButton:new(10, self:getHeight() - btnHgt - padBottom, btnWid, btnHgt, getText("IGUI_DbViewer_Modify"), self, ISWhitelistViewer.onOptionMouseDown);
    self.modify.internal = "MODIFY";
    self.modify:initialise();
    self.modify:instantiate();
    self.modify.enable = false;
    self.modify.borderColor = self.buttonBorderColor;
    self:addChild(self.modify);

    self.delete = ISButton:new(15 + btnWid, self:getHeight() - btnHgt - padBottom, btnWid, btnHgt, getText("IGUI_DbViewer_Delete"), self, ISWhitelistViewer.onOptionMouseDown);
    self.delete.internal = "DELETE";
    self.delete:initialise();
    self.delete:instantiate();
    self.delete.borderColor = self.buttonBorderColor;
    self.delete.enable = false;
    self:addChild(self.delete);
end

function ISWhitelistViewer:onOptionMouseDown(button, x, y)
    if button.internal == "CLOSE" then
        self:closeSelf()
    end
    if button.internal == "MODIFY" then
        local modal = ISWhitelistModifyRow:new(0, 0, 1000, 200, self.activeView);
        modal:initialise();
        modal:addToUIManager();
    end
    if button.internal == "REFRESH" then
        self.activeView:clear();
        self.activeView:clearFilters();
        getTableResult(self.activeView.tableName, self.activeView.entriesPerPages);
    end

    if button.internal == "DELETE" then
        local modal = ISModalDialog:new(0,0, 250, 150, getText("IGUI_DbViewer_DeleteConfirm"), true, nil, ISWhitelistViewer.onRemove, nil, self.activeView);
        modal:initialise()
        modal:addToUIManager()
        modal.moveWithMouse = true;
    end
end

function ISWhitelistViewer:onRemove(button, view)
    if button.internal == "YES" then
        local query = {}
        local nbr = 0;
        for _,v in pairs(view.columns) do
            if view.datas.items[view.datas.selected].item.datas[v["name"]] ~= "" then
                table.insert(query, v.name)
                table.insert(query, view.datas.items[view.datas.selected].item.datas[v.name])
            end
        end
        local params = {}
        local queryStr = "DELETE FROM " .. view.tableName .. " WHERE ";
        for i=1,#query,2 do
            queryStr = queryStr .. query[i] .. " = ?"
            table.insert(params, query[i+1])
            if i < #query - 1 then
                queryStr = queryStr .. " AND "
            end
        end
        print(queryStr)
        executeQuery(queryStr, params);
        view:clear();
        getTableResult(view.tableName, view.entriesPerPages);
    end
end

function ISWhitelistViewer:refresh()
    for i,l in pairs(self.schema) do
--        print("doing ", i)
        local cat1 = ISWhitelistTable:new(0, 0, self.panel.width, self.panel.height - self.panel.tabHeight, i);
        cat1.columns = l;
        cat1:initialise();
        self.panel:addView(i, cat1);
        cat1.parent = self;
        if not self.activeView then
            self.activeView = cat1;
            getTableResult(i, cat1.entriesPerPages);
            cat1.loading = true;
        end
    end
end

ISWhitelistViewer.receiveDBSchema = function(schema)
--    print("GOT SCHEMA")
    ISWhitelistViewer.instance.schema = schema;
    ISWhitelistViewer.instance:refresh();
end

function ISWhitelistViewer:closeSelf()
    self:setVisible(false)
    self:removeFromUIManager()
end

function ISWhitelistViewer:new (x, y, width, height)
    local o = {};
    x = (getCore():getScreenWidth() / 2) - (width / 2);
    y = (getCore():getScreenHeight() / 2) - (height / 2);
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self);
    o.schema = {};
    o.buttonBorderColor = {r=0.7, g=0.7, b=0.7, a=0.5};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};

    self.__index = self;
    o.moveWithMouse = true;
    o.canModify = getAccessLevel() == "admin";
    getDBSchema();
    ISWhitelistViewer.instance = o;
    return o;
end

Events.OnGetDBSchema.Add(ISWhitelistViewer.receiveDBSchema);
