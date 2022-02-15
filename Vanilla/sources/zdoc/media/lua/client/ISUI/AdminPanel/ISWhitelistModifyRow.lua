--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "ISUI/ISPanel"

---@class ISWhitelistModifyRow : ISPanel
ISWhitelistModifyRow = ISPanel:derive("ISWhitelistModifyRow");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)
local FONT_HGT_LARGE = getTextManager():getFontHeight(UIFont.Large)

function ISWhitelistModifyRow:initialise()
    ISPanel.initialise(self);
end


function ISWhitelistModifyRow:render()
    ISPanel.render(self);
    local z = 10;
    self:drawText(getText("IGUI_DbViewer_ModifyRow"), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, getText("IGUI_DbViewer_ModifyRow")) / 2), z, 1,1,1,1, UIFont.Medium);

    local xoff = 10;
    local yoff = z + FONT_HGT_MEDIUM + 10;
    local rowHgt = FONT_HGT_SMALL + 2 * 2
    self:drawRectBorder(xoff, yoff, self.width - 20, rowHgt, 1, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawRect(xoff, yoff, self.width - 20, rowHgt, self.listHeaderColor.a, self.listHeaderColor.r, self.listHeaderColor.g, self.listHeaderColor.b);
    local x = 0;
    for name, type in pairs(self.columns) do
        if type["name"] ~= "id" then
            self:drawRect(xoff + x, 1 + yoff, 1, rowHgt - 1, 1, self.borderColor.r, self.borderColor.g, self.borderColor.b);

            self:drawText(type["name"], xoff + x + 10, yoff + 1, 1,1,1,1,UIFont.Small);
            if self.columnSize[type["name"]] then
                x = x + self.columnSize[type["name"]];
            else
                x = x + 100;
            end
        end
    end
end

function ISWhitelistModifyRow:new (x, y, width, height, view)
    local o = {};
    if x == 0 and y == 0 then
        x = (getCore():getScreenWidth() / 2) - (width / 2);
        y = (getCore():getScreenHeight() / 2) - (height / 2);
    end
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self);
    o.schema = {};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.listHeaderColor = {r=0.4, g=0.4, b=0.4, a=0.3};
    o.buttonBorderColor = {r=0.7, g=0.7, b=0.7, a=0.5};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.tableName = view.tableName;
    o.columns = view.columns;
    o.columnSize = view.columnSize;
    o.data = view.datas.items[view.datas.selected].item.datas;
    o.moveWithMouse = true;
    o.buttonDatas = {};
    o.view = view;
    o.entriesPerPages = view.entriesPerPages;
    return o;
end

function ISWhitelistModifyRow:createChildren()
    ISPanel.createChildren(self);

    local btnWid = 100
    local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local padBottom = 10

    local width = 10
    for _, type in pairs(self.columns) do
        if type["name"] ~= "id" then
            local size = self.columnSize[type["name"]] + 1;
            width = width + size
        end
    end
    self:setWidth(width + 10)

    self.close = ISButton:new(self:getWidth() - 100 - 10, 0, btnWid, btnHgt, getText("IGUI_CraftUI_Close"), self, ISWhitelistModifyRow.onOptionMouseDown);
    self.close.internal = "CLOSE";
    self.close:initialise();
    self.close:instantiate();
    self.close.borderColor = self.buttonBorderColor;
    self:addChild(self.close);

    self.Update = ISButton:new(10, 0, btnWid, btnHgt, getText("IGUI_DbViewer_Update"), self, ISWhitelistModifyRow.onOptionMouseDown);
    self.Update.internal = "UPDATE";
    self.Update:initialise();
    self.Update:instantiate();
    self.Update.borderColor = self.buttonBorderColor;
    self:addChild(self.Update);

    local x = 10;
    local y = 10 + FONT_HGT_MEDIUM + 10 + FONT_HGT_SMALL + 2 * 2
    local entryHgt = FONT_HGT_MEDIUM + 2 * 2
    for _, type in pairs(self.columns) do
--        print(type["name"], self.data[type["name"]], type["type"]);
        if type["name"] ~= "id" then
            local size = self.columnSize[type["name"]] + 1;
            if _ == #self.columns then
               size = self.width - x - 10;
            end
            if type["type"] == "TEXT" or type["type"] == "INTEGER" then
                self.entry = ISTextEntryBox:new(self.data[type["name"]], x, y, size, entryHgt);
                self.entry.font = UIFont.Medium
                self.entry:initialise();
                self.entry:instantiate();
                self.entry.columnName = type["name"];
                self.entry.type = type["type"];
                if type["type"] == "INTEGER" then
                    self.entry:setOnlyNumbers(true);
                end
                self:addChild(self.entry);
                table.insert(self.buttonDatas, self.entry);
            elseif type["type"] == "BOOLEAN" then
                self.combo = ISComboBox:new(x, y, size, entryHgt, nil,nil);
                self.combo.font = UIFont.Medium
                self.combo:initialise();
                self:addChild(self.combo);
                self.combo:addOption("true");
                self.combo:addOption("false");
                if self.data[type["name"]] == "false" or self.data[type["name"]] == "0" then
                    self.combo.selected = 2;
                end
                self.combo.type = type["type"];
                self.combo.columnName = type["name"];
                table.insert(self.buttonDatas, self.combo);
            end
            x = x + self.columnSize[type["name"]];
        end
    end

    self.close:setY(y + entryHgt + 20)
    self.Update:setY(self.close.y)
    self:setHeight(self.close:getBottom() + padBottom)
end

function ISWhitelistModifyRow:onOptionMouseDown(button, x, y)
    if button.internal == "CLOSE" then
        self:setVisible(false);
        self:removeFromUIManager();
    end
    if button.internal == "UPDATE" then
        local where = " WHERE ";
        local query = "UPDATE " .. self.tableName .. " SET ";
        for _,v in ipairs(self.buttonDatas) do
            local previousData = string.trim(self.data[v.columnName]);
            if v.type == "INTEGER" or v.type == "TEXT" then
                local data = string.trim(v:getText());
                if v.columnName == "accesslevel" then
                    if data == "admin" and getAccessLevel() ~= "admin" then
                        local modal = ISModalDialog:new(self.x + (self.width/2),self.y + (self.height/2), 250, 150, getText("IGUI_DbViewer_CantSetAdmin"), false, nil, nil, nil);
                        modal:initialise()
                        modal:addToUIManager()
                        return;
                    end
                end
                if self.data[v.columnName] and previousData ~= data then
                    query = query .. v.columnName .. " = '" .. data .. "',";
                elseif previousData ~= "" then
                    where = where .. v.columnName .. "='" .. previousData .. "' AND ";
                end
            elseif v.type == "BOOLEAN" then
                local value = "false";
                if v.selected == 1 then
                    value = "true";
                end
                if not previousData or previousData ~= value then
                    query = query .. v.columnName .. " = '" .. value .. "',";
                else
                    where = where .. v.columnName .. " = '" .. value .. "' AND ";
                end
            end
        end
        query = string.sub(query, 0, -2);
        where = string.sub(where, 0, -5);
        query = query .. where;
--        print(query);
        executeQuery(query);
        self.view:clear();
        getTableResult(self.tableName, self.entriesPerPages);
        self:setVisible(false);
        self:removeFromUIManager();
    end
end

function ISWhitelistModifyRow:onOptionMouseDown(button, x, y)
    if button.internal == "CLOSE" then
        self:setVisible(false);
        self:removeFromUIManager();
    end
    if button.internal == "UPDATE" then
        local where = {}
        local query = {}
        for _,v in ipairs(self.buttonDatas) do
            local previousData = string.trim(self.data[v.columnName]);
            if v.type == "INTEGER" or v.type == "TEXT" then
                local data = string.trim(v:getText());
                if v.columnName == "accesslevel" then
                    if data == "admin" and getAccessLevel() ~= "admin" then
                        local modal = ISModalDialog:new(self.x + (self.width/2),self.y + (self.height/2), 250, 150, getText("IGUI_DbViewer_CantSetAdmin"), false, nil, nil, nil);
                        modal:initialise()
                        modal:addToUIManager()
                        return;
                    end
                end
                if self.data[v.columnName] and previousData ~= data then
                    table.insert(query, v.columnName)
                    table.insert(query, data)
                elseif previousData ~= "" then
                    table.insert(where, v.columnName)
                    table.insert(where, previousData)
                end
            elseif v.type == "BOOLEAN" then
                local value = "false";
                if v.selected == 1 then
                    value = "true";
                end
                if not previousData or previousData ~= value then
                    table.insert(query, v.columnName)
                    table.insert(query, value)
                else
                    table.insert(where, v.columnName)
                    table.insert(where, previousData)
                end
            end
        end
        local params = {}
        local queryStr = "UPDATE " .. self.tableName .. " SET "
        for i=1,#query,2 do
            queryStr = queryStr .. query[i] .. " = ?"
            table.insert(params, query[i+1])
            if i < #query - 1 then
                queryStr = queryStr .. ","
            end
        end
        local whereStr = " WHERE "
        for i=1,#where,2 do
            whereStr = whereStr .. where[i] .. " = ?"
            table.insert(params, where[i+1])
            if i < #where - 1 then
                whereStr = whereStr .. " AND "
            end
        end
        queryStr = queryStr .. whereStr;
--        print(queryStr);
        executeQuery(queryStr, params);
        self.view:clear();
        getTableResult(self.tableName, self.entriesPerPages);
        self:setVisible(false);
        self:removeFromUIManager();
    end
end

