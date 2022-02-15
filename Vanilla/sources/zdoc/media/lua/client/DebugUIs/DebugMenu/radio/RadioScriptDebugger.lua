--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "ISUI/ISPanel"

---@class RadioScriptDebugger : ISPanel
RadioScriptDebugger = ISPanel:derive("RadioScriptDebugger");
RadioScriptDebugger.instance = nil;

local function roundstring(_val)
    return tostring(ISDebugUtils.roundNum(_val,2));
end

function RadioScriptDebugger.OnOpenPanel(_radioChannel)
    if RadioScriptDebugger.instance==nil then
        RadioScriptDebugger.instance = RadioScriptDebugger:new (100, 100, 1000, 600, "Channel script debugger");
        RadioScriptDebugger.instance.channel = _radioChannel;
        RadioScriptDebugger.instance:initialise();
        RadioScriptDebugger.instance:instantiate();
    end

    RadioScriptDebugger.instance:addToUIManager();
    RadioScriptDebugger.instance:setVisible(true);

    RadioScriptDebugger.instance.channel = _radioChannel;

    return RadioScriptDebugger.instance;
end

function RadioScriptDebugger:initialise()
    ISPanel.initialise(self);

    self.radio = getZomboidRadio();
    self.scriptManager = self.radio:getScriptManager();
end

function RadioScriptDebugger:createChildren()
    ISPanel.createChildren(self);

    ISDebugUtils.addLabel(self, {}, 10, 20, "Channel script debugger - "..tostring(self.channel:GetName()), UIFont.Medium, true)

    self.infoList = ISScrollingListBox:new(10, 50, 280, self.height - 100);
    self.infoList:initialise();
    self.infoList:instantiate();
    self.infoList.itemheight = 22;
    self.infoList.selected = 0;
    self.infoList.joypadParent = self;
    self.infoList.font = UIFont.NewSmall;
    self.infoList.doDrawItem = self.drawInfoList;
    self.infoList.drawBorder = true;
    self:addChild(self.infoList);

    self.channelsList = ISScrollingListBox:new(300, 50, 170, self.height - 100);
    self.channelsList:initialise();
    self.channelsList:instantiate();
    self.channelsList.itemheight = 22;
    self.channelsList.selected = 0;
    self.channelsList.joypadParent = self;
    self.channelsList.font = UIFont.NewSmall;
    self.channelsList.doDrawItem = self.drawChannelList;
    self.channelsList.drawBorder = true;
    self.channelsList.onmousedown = RadioScriptDebugger.OnDaysListMouseDown;
    self.channelsList.target = self;
    self:addChild(self.channelsList);

    self.broadcastList = ISScrollingListBox:new(480, 50, 510, self.height - 100);
    self.broadcastList:initialise();
    self.broadcastList:instantiate();
    self.broadcastList.itemheight = 22;
    self.broadcastList.selected = 0;
    self.broadcastList.joypadParent = self;
    self.broadcastList.font = UIFont.NewSmall;
    self.broadcastList.doDrawItem = self.drawBroadcastList;
    self.broadcastList.drawBorder = true;
    self:addChild(self.broadcastList);

    --[[
    self.infoList = ISScrollingListBox:new(220, 50, 400, self.height - 100);
    self.infoList:initialise();
    self.infoList:instantiate();
    self.infoList.itemheight = 22;
    self.infoList.selected = 0;
    self.infoList.joypadParent = self;
    self.infoList.font = UIFont.NewSmall;
    self.infoList.doDrawItem = self.drawInfoList;
    self.infoList.drawBorder = true;
    self:addChild(self.infoList);
    --]]
    local y, obj = ISDebugUtils.addButton(self,"close",self.width-200,self.height-40,180,20,getText("IGUI_CraftUI_Close"),RadioScriptDebugger.onClickClose);

    self:populateInfoList(self.channel);
    self:populateList();
end

function RadioScriptDebugger:onClickClose()
    self:close();
end

function RadioScriptDebugger:OnDaysListMouseDown(item)
    --self:populateInfoList(item);
    self:populateBroadcastList(item);
end

function RadioScriptDebugger:populateList(_force)
    self.channelsList:clear();

    if self.channel then
        local script = self.channel:getCurrentScript();

        if script then
            local bclist = script:getBroadcastList();

            for i=0, bclist:size()-1 do
                local bc = bclist:get(i);

                self.channelsList:addItem(bc:getID(), bc);
            end
        end
    end
end

function RadioScriptDebugger:drawChannelList(y, item, alt)
    local a = 0.9;

    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
    end

    self:drawText( item.text, 10, y + 2, 1, 1, 1, a, self.font);

    return y + self.itemheight;
end

function RadioScriptDebugger:populateInfoList(_radioChannel)
    self.infoList:clear();

    if self.channel then
        local script = self.channel:getCurrentScript();

        if script then
            self.infoList:addItem(_radioChannel:GetName(), nil);
            self.infoList:addItem("Name: "..tostring(script:GetName()), nil);
            self.infoList:addItem("GUID: "..tostring(script:GetGUID()), nil);
            self.infoList:addItem("Startday stamp: "..tostring(script:getStartDayStamp()), nil);
            self.infoList:addItem("Startday: "..tostring(script:getStartDay()), nil);
            self.infoList:addItem("Loop min: "..tostring(script:getLoopMin()), nil);
            self.infoList:addItem("Loop max: "..tostring(script:getLoopMax()), nil);

            self.infoList:addItem("--- EXIT OPTIONS ---", nil);
            local exits = script:getExitOptions();

            for i=0, exits:size()-1 do
                local exit = exits:get(i);
                self.infoList:addItem("Scriptname: "..tostring(exit:getScriptname()), nil);
                self.infoList:addItem("Change: "..tostring(exit:getChance()), nil);
                self.infoList:addItem("Start delay: "..tostring(exit:getStartDelay()), nil);
                self.infoList:addItem("-------------------", nil);
            end
        end
    end
end

function RadioScriptDebugger:drawInfoList(y, item, alt)
    local a = 0.9;

    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
    end

    self:drawText( item.text, 10, y + 2, 1, 1, 1, a, self.font);

    return y + self.itemheight;
end

function RadioScriptDebugger:populateBroadcastList(_bc)
    self.broadcastList:clear();

    local bc = _bc;

    if bc then
        local lines = bc:getLines();
        for i=0, lines:size()-1 do
            self.broadcastList:addItem(lines:get(i):getText(), lines:get(i));
        end
    end
    --GetPlayerIsListening()
end

function RadioScriptDebugger:drawBroadcastList(y, item, alt)
    local a = 0.9;

    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
    end

    self:drawText( item.text, 10, y + 2, item.item:getR(), item.item:getG(), item.item:getB(), a, self.font);

    return y + self.itemheight;
end


function RadioScriptDebugger:prerender()
    ISPanel.prerender(self);
    self:populateList();
end

function RadioScriptDebugger:update()
    ISPanel.update(self);
end

function RadioScriptDebugger:close()
    self:setVisible(false);
    self:removeFromUIManager();
    RadioScriptDebugger.instance = nil
end

function RadioScriptDebugger:new(x, y, width, height, title)
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
