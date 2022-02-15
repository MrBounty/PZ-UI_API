--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanel"

local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

if debugScenarios == nil then
    debugScenarios = {}
end

DebugScenarios = ISPanel:derive("DebugScenarios");

selectedDebugScenario = nil;

function DebugScenarios:createChildren()
    self.header = ISLabel:new(self.width / 2, 0, 40, "SCENARIOS", 1,1,1,1, UIFont.Large, true);
    self.header.center = true;
    self:addChild(self.header);

    local listY = self.header:getBottom()
    self.listbox = ISScrollingListBox:new(16, listY, self:getWidth()-32, self:getHeight()-16-listY);
    self.listbox:initialise();
    self.listbox:instantiate();
    self.listbox:setFont(UIFont.Medium, 2);
    self.listbox.drawBorder = true;
    self.listbox.doDrawItem = DebugScenarios.drawItem;
    self.listbox:setOnMouseDownFunction(self, DebugScenarios.onClickOption);
    self:addChild(self.listbox);

    for k,v in pairs(debugScenarios) do
        self.listbox:addItem(v.name, k);
    end

    self:setMaxDrawHeight(self.header:getBottom())
end

function DebugScenarios:prerender()
    local height = self:getHeight();
    if self:getMaxDrawHeight() ~= -1 then
        height = self:getMaxDrawHeight();
    end
    if self.background then
        self:drawRectStatic(0, 0, self.width, height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
        self:drawRectBorderStatic(0, 0, self.width, height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    end
end

function DebugScenarios:drawItem(y, item, alt)

    if self.mouseoverselected == item.index then
        self:drawRect(0, y, self:getWidth(), item.height-1, 0.25,0.5,0.5,0.5);
    end
    self:drawRectBorder(0, y + item.height - 1, self:getWidth(), 1, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    -- the name of the story
    self:drawText(item.text, 16, y + (item.height - FONT_HGT_MEDIUM) / 2, 0.6, 0.7, 0.9, 1.0, UIFont.Medium);

    return y+item.height;
end

function DebugScenarios:onMouseMove(dx, dy)
    ISPanel.onMouseMove(self, dx, dy);
    self:setMaxDrawHeight(self:getHeight());
end

function DebugScenarios:onMouseMoveOutside(dx, dy)
    ISPanel.onMouseMoveOutside(self, dx, dy);
    self:setMaxDrawHeight(self.header:getBottom());
end

function DebugScenarios:new (x, y, width, height)
    local o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.backgroundColor = {r=0.0, g=0.05, b=0.1, a=1.0};
    o.borderColor = {r=1, g=1, b=1, a=0.7};

    return o
end

function DebugScenarios:onClickOption(option)

    local scenario = debugScenarios[option];

    self:launchScenario(scenario);
end

function DebugScenarios:launchScenario(scenario)
    MainScreen.instance:setBeginnerPreset();
    
    if(scenario ~= nil) then
        selectedDebugScenario = scenario;
    end
    
    if selectedDebugScenario.setSandbox ~= nil then
        selectedDebugScenario.setSandbox();
    end
    local worldName = ZombRand(100000)..ZombRand(100000)..ZombRand(100000)..ZombRand(100000);
    getWorld():setWorld(worldName);
    createWorld(worldName);
    GameWindow.doRenderEvent(false);
    Events.OnNewGame.Add(DebugScenarios.ongamestart);
    Events.LoadGridsquare.Add(DebugScenarios.onloadgs);
    forceChangeState(GameLoadingState.new());
    DebugScenarios.instance:setVisible(false);
end

function doDebugScenarios()
    local x = getCore():getScreenWidth() / 2;
    local y = getCore():getScreenHeight() / 2;
    x = x - 250;
    y = y - 250;
    local debug = DebugScenarios:new(x, y, 500, 500);
    MainScreen.instance:addChild(debug);
    DebugScenarios.instance = debug;

    -- check if any scenarios have the forceLaunch option, in this case we launch it directly, save more clicks!
    for i,v in pairs(debugScenarios) do
        if v.forceLaunch and getDebugOptions():getBoolean("DebugScenario.ForceLaunch") then
            DebugScenarios.instance:launchScenario(v);
        end
    end
end

function DebugScenarios:onResolutionChange(oldw, oldh, neww, newh)
	local x = neww / 2;
    local y = newh / 2;
    self:setX(x - 250);
    self:setY(y - 250);
end

function DebugScenarios.onloadgs(sq)
    if selectedDebugScenario and selectedDebugScenario.onLoadGS then
        selectedDebugScenario.onLoadGS(sq);
    end
end

function DebugScenarios.ongamestart()
    if selectedDebugScenario and selectedDebugScenario.onStart then
        CharacterCreationHeader.instance:randomGenericOutfit();
        getPlayer():setDescriptor(MainScreen.instance.desc);
        getPlayer():Dressup(MainScreen.instance.desc);
        selectedDebugScenario.onStart();
    end
end

