--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "ISUI/ISPanel"

ZomboidRadioDebug = ISPanel:derive("ZomboidRadioDebug");
ZomboidRadioDebug.instance = nil;

local function roundstring(_val)
    return tostring(ISDebugUtils.roundNum(_val,2));
end

function ZomboidRadioDebug.OnOpenPanel()
    if ZomboidRadioDebug.instance==nil then
        ZomboidRadioDebug.instance = ZomboidRadioDebug:new (100, 100, 1000, 600, "Zomboid radio debugger");
        ZomboidRadioDebug.instance:initialise();
        ZomboidRadioDebug.instance:instantiate();
    end

    ZomboidRadioDebug.instance:addToUIManager();
    ZomboidRadioDebug.instance:setVisible(true);

    return ZomboidRadioDebug.instance;
end

function ZomboidRadioDebug:initialise()
    ISPanel.initialise(self);

    self.radio = getZomboidRadio();
    self.scriptManager = self.radio:getScriptManager();
end

function ZomboidRadioDebug:createChildren()
    ISPanel.createChildren(self);

    ISDebugUtils.addLabel(self, {}, 10, 20, "Zomboid radio debugger", UIFont.Medium, true)

    self.channelsList = ISScrollingListBox:new(10, 50, 150, self.height - 100);
    self.channelsList:initialise();
    self.channelsList:instantiate();
    self.channelsList.itemheight = 22;
    self.channelsList.selected = 0;
    self.channelsList.joypadParent = self;
    self.channelsList.font = UIFont.NewSmall;
    self.channelsList.doDrawItem = self.drawChannelList;
    self.channelsList.drawBorder = true;
    self.channelsList.onmousedown = ZomboidRadioDebug.OnDaysListMouseDown;
    self.channelsList.target = self;
    self:addChild(self.channelsList);

    self.infoList = ISScrollingListBox:new(170, 50, 300, self.height - 100);
    self.infoList:initialise();
    self.infoList:instantiate();
    self.infoList.itemheight = 22;
    self.infoList.selected = 0;
    self.infoList.joypadParent = self;
    self.infoList.font = UIFont.NewSmall;
    self.infoList.doDrawItem = self.drawInfoList;
    self.infoList.drawBorder = true;
    self:addChild(self.infoList);

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

    local y, obj = ISDebugUtils.addButton(self,"script",170,self.height-40,300,20,"View channel script",ZomboidRadioDebug.onViewScript);

    local y, obj = ISDebugUtils.addButton(self,"close",self.width-200,self.height-40,180,20,getText("IGUI_CraftUI_Close"),ZomboidRadioDebug.onClickClose);

    local y, obj = ISDebugUtils.addButton(self,"refresh",self.width-400,self.height-40,180,20,"Refresh",ZomboidRadioDebug.onClickRefresh);

    self:populateList();
end

function ZomboidRadioDebug:onClickClose()
    self:close();
end

function ZomboidRadioDebug:onClickRefresh()
    self:populateList(true);
end

function ZomboidRadioDebug:onViewScript()
    if self.currentChannel then
        RadioScriptDebugger.OnOpenPanel(self.currentChannel);
    end
end

function ZomboidRadioDebug:OnDaysListMouseDown(item)
    self:populateInfoList(item);
    self:populateBroadcastList(item);
end

function ZomboidRadioDebug:populateList(_force)
    local channels = self.scriptManager:getChannelsList();

    if (not _force) and self.channelsSize and self.channelsSize==channels:size() then
        return
    end

    self.channelsList:clear();

    for i=0, channels:size()-1 do
        local channel = channels:get(i);

        local prefix = channel:GetFrequency();
        local name = channel:GetName(); -- prefix .. " : " .. channel:GetName();

        self.channelsList:addItem(name, channel);
    end

    self.channelsSize = channels:size();

    self:populateInfoList(channels:get(0));
    self:populateBroadcastList(channels:get(0));
end

function ZomboidRadioDebug:drawChannelList(y, item, alt)
    local a = 0.9;

    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    --[[if item.item:getIndexOffset()<0 then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.2, 0.80, 0.80, 0.80);
    end--]]

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
    end

    self:drawText( item.text, 10, y + 2, 1, 1, 1, a, self.font);

    return y + self.itemheight;
end

function ZomboidRadioDebug:populateInfoList(_radioChannel)
    self.infoList:clear();

    self.currentChannel = _radioChannel;
    self.infoList:addItem(_radioChannel:GetName(), nil);
    self.infoList:addItem("GUID: "..tostring(_radioChannel:getGUID()), nil);
    self.infoList:addItem("frequency: "..tostring(_radioChannel:GetFrequency()), nil);
    self.infoList:addItem("isTv: "..tostring(_radioChannel:IsTv()), nil);
    self.infoList:addItem("Category: "..tostring(_radioChannel:GetCategory()), nil);
    self.infoList:addItem("Plr listening: "..tostring(_radioChannel:GetPlayerIsListening()), nil);
    self.infoList:addItem("Has active script: "..tostring(_radioChannel:getCurrentScript()~=nil), nil);
    self.infoList:addItem("Has active broadcast: "..tostring(_radioChannel:getAiringBroadcast()~=nil), nil);
    self.infoList:addItem("Current scriptloop: "..tostring(_radioChannel:getCurrentScriptLoop()), nil);
    self.infoList:addItem("Max loops: "..tostring(_radioChannel:getCurrentScriptMaxLoops()), nil);
    --GetPlayerIsListening()
end

function ZomboidRadioDebug:drawInfoList(y, item, alt)
    local a = 0.9;

    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
    end

    self:drawText( item.text, 10, y + 2, 1, 1, 1, a, self.font);

    return y + self.itemheight;
end

function ZomboidRadioDebug:populateBroadcastList(_radioChannel)
    self.broadcastList:clear();

    local bc = _radioChannel:getAiringBroadcast();

    if not bc then
        local script = _radioChannel:getCurrentScript();
        if script then
            bc = script:getValidAirBroadcastDebug();
        end
    end

    if bc then
        local lines = bc:getLines();
        for i=0, lines:size()-1 do
            self.broadcastList:addItem(lines:get(i):getText(), lines:get(i));
        end
    end
    --GetPlayerIsListening()
end

function ZomboidRadioDebug:drawBroadcastList(y, item, alt)
    local a = 0.9;

    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
    end

    self:drawText( item.text, 10, y + 2, item.item:getR(), item.item:getG(), item.item:getB(), a, self.font);

    return y + self.itemheight;
end


function ZomboidRadioDebug:prerender()
    ISPanel.prerender(self);
    self:populateList();
end

function ZomboidRadioDebug:update()
    ISPanel.update(self);
end

function ZomboidRadioDebug:close()
    self:setVisible(false);
    self:removeFromUIManager();
    ZomboidRadioDebug.instance = nil
end

function ZomboidRadioDebug:new(x, y, width, height, title)
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




