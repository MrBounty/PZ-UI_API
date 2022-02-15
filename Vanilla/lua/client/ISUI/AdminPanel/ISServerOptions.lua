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

ISServerOptions = ISPanel:derive("ISServerOptions");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISServerOptions:initialise()
    ISPanel.initialise(self);
    self:create();
end


function ISServerOptions:setVisible(visible)
    --    self.parent:setVisible(visible);
    self.javaObject:setVisible(visible);
end

function ISServerOptions:render()
    local z = 20;

    self:drawText(getText("IGUI_PlayerStats_ServerOptionTitle"), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, getText("IGUI_PlayerStats_ServerOptionTitle")) / 2), z, 1,1,1,1, UIFont.Medium);
    z = z + 30;

    self:drawText(getText("IGUI_DbViewer_Filters"), self.datas.x, self.datas.y + self.datas.height + 6, 1,1,1,1, UIFont.Small);
end

function ISServerOptions:onMouseMove(dx, dy)
    ISPanel.onMouseMove(self, dx, dy)

    local x = self:getMouseX();
    local y = self:getMouseY();
    self.changeBtn:setVisible(false)

    if self.player:getAccessLevel() == "Admin" and not self.modifying and x >= self.datas:getX() + 35 and x <= self.datas:getX() + (self.datas:getWidth() - 40) and y >= self.datas:getY() and y <= self.datas:getY() + self.datas:getHeight() then
        y = self.datas:rowAt(self.datas:getMouseX(), self.datas:getMouseY())
        if self.datas.items[y] then
            self.changeBtn:setVisible(true);
            self.changeBtn:setY(self.datas.y + self.datas:topOfItem(y) + self.datas:getYScroll());
            self.changeBtn:setX(self:getMouseX() - (self.changeBtn.width/2))
            if getText("UI_ServerOption_" .. self.datas.items[y].text .. "_tooltip") == "" then self:hideTooltip(); return; end
            if not self.tooltip then
                local tooltip = ISToolTip:new();
                tooltip:initialise();
                tooltip.description = getText("UI_ServerOption_" .. self.datas.items[y].text .. "_tooltip");
                self.tooltip = tooltip;
                self.tooltip:setVisible(true);
                self.tooltip:addToUIManager();
                self.tooltip.followMouse = true;
            else
                self.tooltip.description = getText("UI_ServerOption_" .. self.datas.items[y].text .. "_tooltip");
            end
--            if getText("UI_ServerOption_" .. self.datas.items[y].text .. "_tooltip") == getText("UI_ServerOption_" .. self.datas.items[y].text .. "_tooltip") or getText("UI_ServerOption_" .. self.datas.items[y].text .. "_tooltip") == "" then
--                self:hideTooltip();
--            end
        else
            self:hideTooltip();
        end
    else
        self:hideTooltip();
    end
end

function ISServerOptions:hideTooltip()
    if self.tooltip then
        self.tooltip:setVisible(false);
        self.tooltip:removeFromUIManager();
        self.tooltip = nil;
    end
end

function ISServerOptions:create()
    local btnWid = 150
    local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local padBottom = 10

    local y = 70;
    self.datas = ISScrollingListBox:new(10, y, self.width - 20, self.height - 160);
    self.datas:initialise();
    self.datas:instantiate();
    self.datas.itemheight = FONT_HGT_SMALL + 2 * 2;
    self.datas.selected = 0;
    self.datas.joypadParent = self;
    self.datas.font = UIFont.NewSmall;
    self.datas.doDrawItem = self.drawDatas;
    self.datas.drawBorder = true;
    self.datas.parent = self;
    self:addChild(self.datas);

    local textWid = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_DbViewer_Filters"))
    local entryHgt = FONT_HGT_SMALL + 2 * 2
    self.filterEntry = ISTextEntryBox:new("", self.datas.x + textWid + 6, self.datas.y + self.datas.height + 6, 200, entryHgt);
    self.filterEntry:initialise();
    self.filterEntry:instantiate();
    self.filterEntry:setText("");
    self:addChild(self.filterEntry);
    self.filterEntry:setClearButton(true)
    self.filterEntry.onTextChange = ISServerOptions.populateList;

    y = y + 30;

    local btnHgt2 = FONT_HGT_SMALL + 2 * 2
    self.changeBtn = ISButton:new(self.width - 100, 0, 50, btnHgt2, getText("IGUI_PlayerStats_Change"), self, ISServerOptions.onOptionMouseDown);
    self.changeBtn.internal = "CHANGE";
    self.changeBtn:initialise();
    self.changeBtn:instantiate();
    self.changeBtn.borderColor = self.buttonBorderColor;
    self.changeBtn:setVisible(false);
    self:addChild(self.changeBtn);

    self.saveBtn = ISButton:new(10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("IGUI_PlayerStats_ReloadOptions"), self, ISServerOptions.onOptionMouseDown);
    self.saveBtn.internal = "RELOAD";
    self.saveBtn.tooltip = getText("IGUI_PlayerStats_ReloadOptionsTooltip");
    self.saveBtn:initialise();
    self.saveBtn:instantiate();
    self.saveBtn.enable = false;
    self.saveBtn.borderColor = self.buttonBorderColor;
    self:addChild(self.saveBtn);

    self.cancel = ISButton:new(self:getWidth() - btnWid - 10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("IGUI_CraftUI_Close"), self, ISServerOptions.onOptionMouseDown);
    self.cancel.internal = "CANCEL";
    self.cancel:initialise();
    self.cancel:instantiate();
    self.cancel.borderColor = self.buttonBorderColor;
    self:addChild(self.cancel);
    
    if self.player:getAccessLevel() ~= "Admin" then
        self.changeBtn:setVisible(false);
        self.saveBtn:setVisible(false);
    end

    self:populateList();
end

function ISServerOptions:populateList()
    ISServerOptions.instance.datas:clear();
    local options = ServerOptions.getInstance():getPublicOptions()
    local sorted = {}
    for i=1,options:size() do
        local option = options:get(i-1)
        table.insert(sorted, option)
    end
    table.sort(sorted, function(a,b) return not string.sort(a, b) end)
    local filterText = ISServerOptions.instance.filterEntry:getInternalText():trim():lower()
    if filterText ~= "" then
        for _,option in ipairs(sorted) do
            if string.contains(string.lower(option), filterText) then
                ISServerOptions.instance.datas:addItem(option, ServerOptions.getInstance():getOptionByName(option))
            end
        end
    else
        for _,option in ipairs(sorted) do
            ISServerOptions.instance.datas:addItem(option, ServerOptions.getInstance():getOptionByName(option))
        end
    end
end

function ISServerOptions:drawDatas(y, item, alt)
    local a = 0.9;

    if alt then
        self:drawRect(0, (y), self:getWidth(), self.itemheight, 0.3, 0.6, 0.5, 0.5);
    end

    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    self:drawText(item.item:getName(), 10, y + 2, 1, 1, 1, a, self.font);
    self:drawText(item.item:getValueAsString(), self.width / 2, y + 2, 1, 1, 1, a, self.font);

    return y + self.itemheight;
end

function ISServerOptions:onServerOptionChange(option, newValue)
    newValue = string.gsub(newValue, "\"", "\\\"");
    if newValue ~= option:getValueAsString() then
        self.saveBtn.enable = true;
        SendCommandToServer("/changeoption " .. option:getName() .. " \"" .. newValue .. "\"");
        for i=1,#self.datas.items do
            local item = self.datas.items[i].item;
            if item:getName() == option:getName() then
                item:asConfigOption():setValueFromObject(newValue);
            end
        end
    end
end

function ISServerOptions:onOptionMouseDown(button, x, y)
    if button.internal == "CHANGE" then
        local y = self:getMouseY() + (-self.datas:getYScroll());
        y = y - 70;
        y = y / self.datas.itemheight;
        y = math.floor(y + 1);
        local modal = ISServerOptionsChange:new(self:getMouseX() - 30, self:getMouseY() - 30, 600, 250, self, ISServerOptions.onServerOptionChange, self.datas.items[y].item)
        modal:initialise();
        modal:addToUIManager();
        self.modifying = true;
    end

    if button.internal == "CANCEL" then
        if self.saveBtn.enable then
            local modal = ISModalDialog:new(0,0, 350, 150, getText("IGUI_PlayerStats_ConfirmNonSaveServerOptions"), true, nil, ISServerOptions.onConfirmLeave);
            modal:initialise()
            modal:addToUIManager()
            modal.ui = self;
            modal.moveWithMouse = true;
        else
            self:setVisible(false);
            self:removeFromUIManager();
        end
    end

    if button.internal == "RELOAD" then
        SendCommandToServer("/reloadoptions");
        self.saveBtn.enable = false;
    end
end

function ISServerOptions:onConfirmLeave(button)
    if button.internal == "YES" then
        SendCommandToServer("/reloadoptions");
    end
    button.parent.ui:setVisible(false);
    button.parent.ui:removeFromUIManager();
end

function ISServerOptions:new(x, y, width, height, player)
    local o = {};
    x = getCore():getScreenWidth() / 2 - (width / 2);
    y = getCore():getScreenHeight() / 2 - (height / 2);
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self);
    self.__index = self;
    o.player = player;
    o.variableColor={r=0.9, g=0.55, b=0.1, a=1};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.buttonBorderColor = {r=0.7, g=0.7, b=0.7, a=0.5};
    o.zOffsetSmallFont = 25;
    o.moveWithMouse = true;
    o.modifying = false;
    ISServerOptions.instance = o;

    return o;
end
