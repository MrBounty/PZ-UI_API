--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "ISUI/ISPanel"

ISDebugMenu = ISPanel:derive("ISDebugMenu");
ISDebugMenu.instance = nil;
ISDebugMenu.forceEnable = false;
ISDebugMenu.shiftDown = 0;

function ISDebugMenu:setupButtons()
    self:addButtonInfo("General debuggers", ISGeneralDebug.OnOpenPanel);
    self:addButtonInfo("Climate debuggers", ClimateControlDebug.OnOpenPanel);
    self:addButtonInfo("IsoRegions", IsoRegionsWindow.OnOpenPanel);
    --self:addButtonInfo("IsoRegions [OLD]", IsoRegionDebug.OnOpenPanel);
    self:addButtonInfo("Zombie Population", ZombiePopulationWindow.OnOpenPanel);
    self:addButtonInfo("Player's Stats", ISPlayerStatsUI.OnOpenPanel);
    self:addButtonInfo("Items List", ISItemsListViewer.OnOpenPanel);
    self:addButtonInfo("Stash debuggers", StashDebug.OnOpenPanel);
    --self:addButtonInfo("Anim debuggers", ISAnimsDebugMenu.OnOpenPanel);
    self:addButtonInfo("Anim monitor", ISAnimDebugMonitor.OnOpenPanel);
    self:addButtonInfo("Zomboid Radio", ZomboidRadioDebug.OnOpenPanel);
    self:addButtonInfo("Attachment Editor", showAttachmentEditor);
    self:addButtonInfo("Chunk Debugger", showChunkDebugger);
    self:addButtonInfo("Vehicle Editor", function() showVehicleEditor(nil) end);
    self:addButtonInfo("World Flares", WorldFlaresDebug.OnOpenPanel);
	self:addButtonInfo("Statistic", ISGameStatisticPanel.OnOpenPanel);
    self:addButtonInfo("GlobalModData", GlobalModDataDebug.OnOpenPanel);
end

function ISDebugMenu:addButtonInfo(_title, _func, _marginBot)
    self.buttons = self.buttons or {};

    table.insert(self.buttons, { title = _title, func = _func, marginBot = (_marginBot or 0) })
end

function ISDebugMenu.OnOpenPanel()
    if getCore():getDebug() or ISDebugMenu.forceEnable then
        if ISDebugMenu.instance==nil then
            ISDebugMenu.instance = ISDebugMenu:new (100, 100, 200, 20, getPlayer());
            ISDebugMenu.instance:initialise();
            ISDebugMenu.instance:instantiate();
        end

        ISDebugMenu.instance:addToUIManager();
        ISDebugMenu.instance:setVisible(true);

        return ISDebugMenu.instance;
    end
end

function ISDebugMenu:initialise()
    ISPanel.initialise(self);
end

function ISDebugMenu:createChildren()
    ISPanel.createChildren(self);

    self.buttons = {};
    self:setupButtons();

    local maxWidth = 0
    for k,v in ipairs(self.buttons)  do
        local width = getTextManager():MeasureStringX(UIFont.Small, v.title) + 10
        maxWidth = math.max(maxWidth, width)
    end
    self:setWidth(math.max(self.width, 10 + maxWidth + 10))
    self:ignoreWidthChange()

    if #self.buttons>0 then
        local last = self.buttons[#self.buttons];
        last.marginBot = 10;
    end
    self:addButtonInfo("Close", nil);

    local x,y = 10,10;
    local w,h = self.width-20,20;
    local margin = 5;

    local y, obj = ISDebugUtils.addLabel(self,"Header",x+(w/2),y,"DEBUG MENU",UIFont.Medium);
    obj.center = true;

    y = y+5;

    for k,v in ipairs(self.buttons)  do
        y, obj = ISDebugUtils.addButton(self,v,x,y+margin,w,h,v.title,ISDebugMenu.onClick);
        if v.marginBot and v.marginBot>0 then
            y = y+v.marginBot;
        end
    end

    self:setHeight(y+10);
end

function ISDebugMenu:onClick(_button)
    if _button.customData.func then
        _button.customData.func();
    else
        self:close();
    end
end

function ISDebugMenu:close()
    self:setVisible(false);
    self:removeFromUIManager();
    ISDebugMenu.instance = nil
end

function ISDebugMenu:new(x, y, width, height)
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
    --ISDebugMenu.instance = o
    ISDebugMenu.RegisterClass(self);
    return o;
end

ISDebugMenu.classes = {}

function ISDebugMenu.RegisterClass(_class)
    table.insert(ISDebugMenu.classes, _class);
end

function ISDebugMenu.OnPlayerDeath(playerObj)
    for _,class in ipairs(ISDebugMenu.classes) do
        if class.instance then
            class.instance:setVisible(false);
            class.instance:removeFromUIManager();
            class.instance = nil;
        end
    end
end

Events.OnPlayerDeath.Add(ISDebugMenu.OnPlayerDeath)

