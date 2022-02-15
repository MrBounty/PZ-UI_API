--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************


require "ISUI/ISCollapsableWindow"

---@class WindDebug : ISCollapsableWindow
WindDebug = ISCollapsableWindow:derive("WindDebug");
WindDebug.instance = nil;
WindDebug.shiftDown = 0;

function WindDebug.OnOpenPanel()
    if WindDebug.instance==nil then
        WindDebug.instance = WindDebug:new (100, 100, 900, 300, getPlayer());
        WindDebug.instance:initialise();
        WindDebug.instance:instantiate();
    end

    WindDebug.instance:addToUIManager();
    WindDebug.instance:setVisible(true);

    return WindDebug.instance;
end

function WindDebug:initialise()
    ISCollapsableWindow.initialise(self);
end


function WindDebug:createChildren()
    ISCollapsableWindow.createChildren(self);

    self:initVariables();
    local th = self:titleBarHeight();

    local y = th;
    local x = 25;

    self.buttonM1 = ISButton:new(x+5, y+5, 30,18,"M1",self, WindDebug.onButton);
    self.buttonM1:initialise();
    self.buttonM1.backgroundColor = {r=0, g=0.8, b=0, a=1.0};
    self.buttonM1.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.buttonM1.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self:addChild(self.buttonM1);

    y = self.buttonM1:getY() + self.buttonM1:getHeight();

    self.historyM1 = ValuePlotter:new(x+5,y+5,600,200,600);
    self.historyM1:initialise();
    self.historyM1:instantiate();
    --self.historyM1:defineVariable("temperature", {r=0, g=0, b=1.0, a=1.0}, -50, 50);
    self:addChild(self.historyM1);
    self.historyM1:setVisible(true);


    self.charts = {};
    table.insert(self.charts, self.historyM1);
    table.insert(self.charts, self.historyH1);
    table.insert(self.charts, self.historyD1);
    local vinfo;
    for i=1, #self.charts do
        for j=1, #self.varInfo do
            vinfo = self.varInfo[j];
            self.charts[i]:defineVariable(vinfo.desc, vinfo.col, vinfo.min, vinfo.max);
        end

        --self.charts[i]:setHorzLine(0.10,{r=0.1, g=0.1, b=0.1, a=1});
        self.charts[i]:setHorzLine(0.125,{r=0.05, g=0.05, b=0.05, a=1});
        self.charts[i]:setHorzLine(0.25,{r=0.1, g=0.1, b=0.1, a=1});
        --self.charts[i]:setHorzLine(0.30,{r=0.1, g=0.1, b=0.1, a=1});
        self.charts[i]:setHorzLine(0.375,{r=0.05, g=0.05, b=0.05, a=1});
        self.charts[i]:setHorzLine(0.50,{r=0.1, g=0.1, b=0.1, a=1});
        --self.charts[i]:setHorzLine(0.60,{r=0.1, g=0.1, b=0.1, a=1});
        self.charts[i]:setHorzLine(0.625,{r=0.05, g=0.05, b=0.05, a=1});
        self.charts[i]:setHorzLine(0.75,{r=0.1, g=0.1, b=0.1, a=1});
        --self.charts[i]:setHorzLine(0.80,{r=0.1, g=0.1, b=0.1, a=1});
        self.charts[i]:setHorzLine(0.875,{r=0.05, g=0.05, b=0.05, a=1});
    end

    y = self.historyM1:getY() + self.historyM1:getHeight();

    local tY = self.historyM1:getY()-12;
    local tH = self.historyM1:getHeight();
    local tX = self.historyM1:getX();
    local tW = self.historyM1:getWidth();

    self.chartLabelsLeft = {};
    self.chartLabelsLeftTxt = {"1.0","0.5","0.0","-0.5","-1.0"};
    self.chartLabelsRight = {};
    self.chartLabelsRightTxt = {"50 C","25 C","0 C","-25 C","-50 C"};
    for i=1,5 do
        local id = i-1;
        local lbl = ISLabel:new(tX-5, tY+((tH/4)*id), 16, self.chartLabelsLeftTxt[i], 1, 1, 1, 1.0, UIFont.Small, false);
        lbl:initialise();
        lbl:instantiate();
        self:addChild(lbl);
        table.insert(self.chartLabelsLeft,lbl);

        local lbl2 = ISLabel:new(tX+tW+5, tY+((tH/4)*id), 16, self.chartLabelsRightTxt[i], 1, 1, 1, 1.0, UIFont.Small, true);
        lbl2:initialise();
        lbl2:instantiate();
        self:addChild(lbl2);
        table.insert(self.chartLabelsRight,lbl2);
    end

    y=y+10;
    local cacheY, cacheX = y, x;

    y = th+2; --self.historyM1:getY()-2;
    x = self.historyM1:getX() + self.historyM1:getWidth() + 45;
    local vars = self.historyM1:getVars();
    for i=1,#vars do
        local btn = ISButton:new(x+5, y+5, 45,18,"toggle",self, WindDebug.onButtonToggle);
        btn:initialise();
        btn.backgroundColor = vars[i].enabled and {r=0, g=0.8, b=0, a=1.0} or {r=0.0, g=0, b=0, a=0.0};
        btn.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
        btn.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
        btn.toggleVarID = i;
        btn.toggleVarName = vars[i].name;
        btn.toggleVal = vars[i].enabled;
        self:addChild(btn);

        local pnl = ISPanel:new(x+10+45,y+5,18,18);
        pnl:initialise();
        pnl.backgroundColor = vars[i].color;
        self:addChild(pnl);

        local lbl = ISLabel:new(x+15+45+18, y+5, 16, vars[i].name, 1, 1, 1, 1.0, UIFont.Small, true);
        lbl:initialise();
        lbl:instantiate();
        self:addChild(lbl);

        y = btn:getY() + btn:getHeight();
    end

    y = y+10;
    self:setHeight(y>cacheY and y or cacheY);
end

function WindDebug:initVariables()
    self.varInfo = {};
    self.colTable = {};
    self:addColor(1.0,1.0,0.9);
    self:addColor(0.4,0.4,0.4);
    local cc = {1.0,0.5,0.0};
    for ri=1,#cc do
        for gi=1,#cc do
            for bi=1,#cc do
                if (not (ri==1 and gi==1 and bi==1)) and (not (ri==3 and gi==3 and bi==3)) then
                    self:addColor(cc[ri],cc[gi],cc[bi]);
                end
            end
        end
    end

    --self:addVarInfo("","",-1,1,"");
    self:addVarInfo("windNoiseBase","WindNoiseBase",-1,1,"getDayLightStrength");
    self:addVarInfo("windNoiseFinal","WindNoiseFinal",-1,1,"getDayLightStrength");
    self:addVarInfo("windTickFinal","WindTickFinal",-1,1,"getDayLightStrength");
end

function WindDebug:addColor(_r,_g,_b)
    table.insert(self.colTable,{r=_r,g=_g,b=_b,a=1.0})
end

function WindDebug:addVarInfo(_name,_desc,_min,_max,_func)
    table.insert(self.varInfo,{
        name = _name,
        desc = _desc,
        min = _min,
        max = _max,
        func = _func,
        col = {1,1,1,1};
    });
    self.varInfo[#self.varInfo].col = self.colTable[#self.varInfo];
end


function WindDebug:onButton(_btn)
    if _btn.title=="M1" then
        self.historyM1:setVisible(true);
        self.buttonM1.backgroundColor = {r=0, g=0.8, b=0, a=1.0};
        self.historyH1:setVisible(false);
        self.buttonH1.backgroundColor = {r=0, g=0, b=0, a=0.0};
        self.historyD1:setVisible(false);
        self.buttonD1.backgroundColor = {r=0, g=0, b=0, a=0.0};
    end
end

function WindDebug:onButtonToggle(_btn)
    if _btn.toggleVarName then
        _btn.toggleVal = not _btn.toggleVal;
        self.historyM1:setVariableEnabled(_btn.toggleVarName,_btn.toggleVal);
        if _btn.toggleVal then
            _btn.backgroundColor = {r=0, g=0.8, b=0, a=1.0};
        else
            _btn.backgroundColor = {r=0, g=0, b=0, a=0.0};
        end
    end
end


function WindDebug:onResize()
    ISUIElement.onResize(self);
    local th = self:titleBarHeight();
    --self.richtext:setWidth(self.width);
    --self.richtext:setHeight(self.height-(th+10));
end

local ctr = 0;
function WindDebug:update()
    ISCollapsableWindow.update(self);

    if WindDebug.shiftDown>0 then
        WindDebug.shiftDown = WindDebug.shiftDown-1;
    end

    ctr = ctr+1;
    if ctr>15 then
        ctr = 0;
        local dataset = {
            ClimateManager.getWindNoiseBase(),
            ClimateManager.getWindNoiseFinal(),
            ClimateManager.getWindTickFinal()
        };
        self.historyM1:addPlotPoint(dataset, false);
    end
end

function WindDebug:prerender()
    self:stayOnSplitScreen();
    ISCollapsableWindow.prerender(self);
end

function WindDebug:stayOnSplitScreen()
    ISUIElement.stayOnSplitScreen(self, self.playerNum)
end


function WindDebug:render()
    ISCollapsableWindow.render(self);

    if WindDebug.shiftDown>0 then
        WindDebug.shiftDown = WindDebug.shiftDown-1;
    end
end


function WindDebug:close()
    ISCollapsableWindow.close(self);
    if JoypadState.players[self.playerNum+1] then
        setJoypadFocus(self.playerNum, nil)
    end
    WindDebug.instance = nil;
    self:removeFromUIManager();
    self:clear();
end

function WindDebug:clear()
    self.currentTile = nil;
end



function WindDebug:new (x, y, width, height, player)
    local o = {}
    --o.data = {}
    o = ISCollapsableWindow:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.player = player;
    o.playerNum = player:getPlayerNum();
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.25};
    o.greyCol = { r=0.4,g=0.4,b=0.4,a=1};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.pin = true;
    o.isCollapsed = false;
    o.collapseCounter = 0;
    o.title = "Debug Wind Tick";
    --o.viewList = {}
    o.resizable = true;
    o.drawFrame = true;

    o.currentTile = nil;
    o.richtext = nil;
    o.overrideBPrompt = true;
    o.subFocus = nil;
    o.hotKeyPanels = {};
    o.isJoypadWindow = false;

    o.hourStamp = -1;
    o.dayStamp = -1;
    o.monthStamp = -1;
    o.year = -1;
    ISDebugMenu.RegisterClass(self);
    return o
end

--[[
if enabled then
    Events.OnCustomUIKey.Add(WindDebug.OnKeyDown);
    Events.OnKeyKeepPressed.Add(WindDebug.OnKeepKeyDown);
    Events.OnClimateTickDebug.Add(WindDebug.OnClimateTickDebug);
    --Events.OnObjectLeftMouseButtonUp.Add(WindDebug.onMouseButtonUp);
end--]]
