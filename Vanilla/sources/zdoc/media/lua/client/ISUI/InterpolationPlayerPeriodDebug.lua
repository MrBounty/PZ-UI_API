--***********************************************************
--**                    THE INDIE STONE                    **
--**				    Author: Yuri				       **
--***********************************************************


require "ISUI/ISCollapsableWindow"

---@class InterpolationPlayerPeriodDebug : ISCollapsableWindow
InterpolationPlayerPeriodDebug = ISCollapsableWindow:derive("InterpolationPlayerPeriodDebug");
InterpolationPlayerPeriodDebug.instance = nil;
InterpolationPlayerPeriodDebug.shiftDown = 0;
InterpolationPlayerPeriodDebug.eventsAdded = false;


function InterpolationPlayerPeriodDebug.OnOpenPanel(player)
    if InterpolationPlayerPeriodDebug.instance==nil then
        InterpolationPlayerPeriodDebug.instance = InterpolationPlayerPeriodDebug:new (100, 100, 900, 300, getPlayer());
        InterpolationPlayerPeriodDebug.instance:initialise();
        InterpolationPlayerPeriodDebug.instance:instantiate();
    end

    InterpolationPlayerPeriodDebug.instance:addToUIManager();
    InterpolationPlayerPeriodDebug.instance:setVisible(true);

	InterpolationPlayerPeriodDebug.instance:setPlayer(player)

    return InterpolationPlayerPeriodDebug.instance;
end

function InterpolationPlayerPeriodDebug:initialise()
    ISCollapsableWindow.initialise(self);
end


function InterpolationPlayerPeriodDebug:createChildren()
    ISCollapsableWindow.createChildren(self);
	
	self.labels = {};

    self:initVariables();
    local th = self:titleBarHeight();

    local y = th;
    local x = 25;

    self.historyM1 = ValuePlotter:new(x+5,y+5,600,300,600);
    self.historyM1:initialise();
    self.historyM1:instantiate();
    self:addChild(self.historyM1);
    self.historyM1:setVisible(true);

    local vinfo;
	for j=1, #self.varInfo do
		vinfo = self.varInfo[j];
		self.historyM1:defineVariable(vinfo.desc, vinfo.col, vinfo.min, vinfo.max);
	end


	self.historyM1:setHorzLine(9.0,{r=0.1, g=0.1, b=0.1, a=1});
	self.historyM1:setHorzLine(11.0,{r=0.1, g=0.1, b=0.1, a=1});
	self.historyM1:setHorzLine(7.0,{r=0.1, g=0.1, b=0.1, a=1});

    y = self.historyM1:getY() + self.historyM1:getHeight();

    y=y+10;
    local cacheY, cacheX = y, x;

    y = self.historyM1:getY()-2;
    x = self.historyM1:getX() + self.historyM1:getWidth() + 45;
    local vars = self.historyM1:getVars();
    for i=1,#vars do
        local btn = ISButton:new(x+5, y+5, 45,18,"toggle",self, InterpolationPlayerPeriodDebug.onButtonToggle);
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
	
	local labelWidth = 250
	x = self.historyM1:getX()
	y = self.historyM1:getY()+self.historyM1:getHeight()
	y = y+8;
    y = self:addLabelValue(x+5, y, labelWidth, "value","RealX","RealX:",0);
	y = self:addLabelValue(x+5, y, labelWidth, "value","RealY","RealY:",0);
	y = self:addLabelValue(x+5, y, labelWidth, "value","X","X:",0);
	y = self:addLabelValue(x+5, y, labelWidth, "value","Y","Y:",0);
	y = self:addLabelValue(x+5, y, labelWidth, "value","TargetX","TargetX:",0);
	y = self:addLabelValue(x+5, y, labelWidth, "value","TargetY","TargetY:",0);
	y = y+8;
	y = self:addLabelValue(x+5, y, labelWidth, "value","PathLength","PathLength:",0);
	y = self:addLabelValue(x+5, y, labelWidth, "value","TargetLength","TargetLength:",0);
	y = y+8;
	y = self:addLabelValue(x+5, y, labelWidth, "value","ServerT","ServerT:",0);
	y = self:addLabelValue(x+5, y, labelWidth, "value","TargetT","TargetT:",0);
	y = y+10;
    self:setHeight(y>cacheY and y or cacheY);
	
	x = self.historyM1:getX()+ labelWidth
	y = self.historyM1:getY()+self.historyM1:getHeight()
	y = y+8;
    y = self:addLabelValue(x+5, y, labelWidth, "value","serverActionState","serverActionState:",0);
	y = self:addLabelValue(x+5, y, labelWidth, "value","clientActionState","clientActionState:",0);
	y = y+8;
    y = self:addLabelValue(x+5, y, labelWidth, "value","serverAnimationState","serverAnimationState:",0);
	y = self:addLabelValue(x+5, y, labelWidth, "value","clientAnimationState","clientAnimationState:",0);
	y = y+8;
    y = self:addLabelValue(x+5, y, labelWidth, "value","finderProgress","finderProgress:",0);
	y = self:addLabelValue(x+5, y, labelWidth, "value","usePathFind","usePathFind:",0);
    
end

function InterpolationPlayerPeriodDebug:addLabel(_curX, _curY, _labelID, _title)
    if not self.labels[_labelID] then
        local label = {};
        label.titleLabel = ISLabel:new(_curX, _curY, 16, _title, 1, 1, 1, 1.0, UIFont.Small, true);
        label.titleLabel:initialise();
        label.titleLabel:instantiate();
        self:addChild(label.titleLabel);

        self.labels[_labelID] = label;

        _curY = label.titleLabel:getY() + label.titleLabel:getHeight();
    else
        print("Cannot add label: "..tostring(_labelID));
    end
    return _curY;
end

function InterpolationPlayerPeriodDebug:addLabelValue(_curX, _curY, _width, _type, _labelID, _title, _defaultVal)
    if not self.labels[_labelID] then
        local label = {};
        label.titleLabel = ISLabel:new(_curX, _curY, 16, _title, 1, 1, 1, 1.0, UIFont.Small, true);
        label.titleLabel:initialise();
        label.titleLabel:instantiate();
        self:addChild(label.titleLabel);

        if _type=="value" then
            label.valueLabel = ISLabel:new(_curX + 2.0*_width/3.0, _curY, 16, tostring(_defaultVal), 1, 1, 1, 1.0, UIFont.Small, true);
            label.valueLabel:initialise();
            label.valueLabel:instantiate();
            self:addChild(label.valueLabel);
        end

        self.labels[_labelID] = label;

        _curY = label.titleLabel:getY() + label.titleLabel:getHeight();
    else
        print("Cannot add label: "..tostring(_labelID));
    end
    return _curY;
end

function InterpolationPlayerPeriodDebug:getTitleLabel(_labelID)
    if self.labels[_labelID] then
        return self.labels[_labelID].titleLabel;
    end
end

function InterpolationPlayerPeriodDebug:getValueLabel(_labelID)
    if self.labels[_labelID] then
        return self.labels[_labelID].valueLabel;
    end
end

function InterpolationPlayerPeriodDebug:initVariables()
    self.varInfo = {};
    self.colTable = {};
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

	self:addVarInfo("RealX","RealX",-100,100,"RealX");
	self:addVarInfo("RealY","RealY",-100,100,"RealY");
	self:addVarInfo("X","X",-100,100,"X");
	self:addVarInfo("Y","Y",-100,100,"Y");
	self:addVarInfo("TargetX","TargetX",-100,100,"TargetX");
	self:addVarInfo("TargetY","TargetY",-100,100,"TargetY");
	self:addVarInfo("PathLength","PathLength",-100,100,"PathLength");
	self:addVarInfo("TargetLength","TargetLength",-100,100,"TargetLength");
	self:addVarInfo("ServerT","ServerT",-100,100,"ServerT");
	self:addVarInfo("TargetT","TargetT",-100,100,"TargetT");

end

function InterpolationPlayerPeriodDebug:addColor(_r,_g,_b)
    table.insert(self.colTable,{r=_r,g=_g,b=_b,a=1.0})
end

function InterpolationPlayerPeriodDebug:addVarInfo(_name,_desc,_min,_max,_func)
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

function InterpolationPlayerPeriodDebug:updateValues()
    
    if self.clearOnNextRun then
        --clear
        self.historyM1:clearHistory();
        self.currentStage = nil;
        self.clearOnNextRun = false;
    end

    local dataset = {};
    local vinfo;
    for i=1,#self.varInfo do
        vinfo = self.varInfo[i];
        if type(vinfo.func)=="function" then
            table.insert(dataset,vinfo.func());
        elseif type(vinfo.func)=="string" and self.playerInfo[vinfo.func] then
			local value = tonumber(tostring(self.playerInfo[vinfo.func]))
			table.insert(dataset, value);
        else
        end
    end
	

    self.historyM1:addPlotPoint(dataset, false);
	
	local minmaxX = self.historyM1:calcMinMax(1);
	minmaxX = self.historyM1:calcMinMax(3, minmaxX);
	minmaxX = self.historyM1:calcMinMax(5, minmaxX);
	local minmaxY = self.historyM1:calcMinMax(2);
	minmaxY = self.historyM1:calcMinMax(4, minmaxY);
	minmaxY = self.historyM1:calcMinMax(6, minmaxY);
	local minmaxT = self.historyM1:calcMinMax(9);
	minmaxT = self.historyM1:calcMinMax(10, minmaxT);
	self.historyM1:applyMinMax(minmaxX, 1); -- RealX
	self.historyM1:applyMinMax(minmaxY, 2); -- RealY
	self.historyM1:applyMinMax(minmaxX, 3); -- X
	self.historyM1:applyMinMax(minmaxY, 4); -- Y
	self.historyM1:applyMinMax(minmaxX, 5); -- TargetX
	self.historyM1:applyMinMax(minmaxY, 6); -- TargetY
	self.historyM1:applyMinMax(minmaxT, 9); -- ServerT
	self.historyM1:applyMinMax(minmaxT, 10); -- TargetT
	
	self:getValueLabel("RealX").name = tostring(self.playerInfo.RealX);
	self:getValueLabel("RealY").name = tostring(self.playerInfo.RealY);
	self:getValueLabel("X").name = tostring(self.playerInfo.X);
	self:getValueLabel("Y").name = tostring(self.playerInfo.Y);
	self:getValueLabel("TargetX").name = tostring(self.playerInfo.TargetX);
	self:getValueLabel("TargetY").name = tostring(self.playerInfo.TargetY);
	self:getValueLabel("TargetT").name = tostring(self.playerInfo.TargetT);
	self:getValueLabel("ServerT").name = tostring(self.playerInfo.ServerT);
	self:getValueLabel("PathLength").name = tostring(self.playerInfo.PathLength);
	self:getValueLabel("TargetLength").name = tostring(self.playerInfo.TargetLength);
	self:getValueLabel("serverActionState").name = tostring(self.playerInfo.serverActionState);
	self:getValueLabel("clientActionState").name = tostring(self.playerInfo.clientActionState);
	self:getValueLabel("serverAnimationState").name = tostring(self.playerInfo.serverAnimationState);
	self:getValueLabel("clientAnimationState").name = tostring(self.playerInfo.clientAnimationState);
	self:getValueLabel("finderProgress").name = tostring(self.playerInfo.finderProgress);
	self:getValueLabel("usePathFind").name = tostring(self.playerInfo.usePathFind);
end

function InterpolationPlayerPeriodDebug:onMouseWheel(del)
	self.historyM1.maxPlotPoints = self.historyM1.maxPlotPoints + del;
	if self.historyM1.maxPlotPoints < 10 then
		self.historyM1.maxPlotPoints = 10
		return false
	end
	return true
end


function InterpolationPlayerPeriodDebug:onButtonToggle(_btn)
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


function InterpolationPlayerPeriodDebug:onResize()
    ISUIElement.onResize(self);
    local th = self:titleBarHeight();
end

function InterpolationPlayerPeriodDebug:update()
    ISCollapsableWindow.update(self);
	self.playerInfo = getPlayerInfo(self.player);
	if InterpolationPlayerPeriodDebug.instance then
		InterpolationPlayerPeriodDebug.instance:updateValues();
	end
    if InterpolationPlayerPeriodDebug.shiftDown>0 then
        InterpolationPlayerPeriodDebug.shiftDown = InterpolationPlayerPeriodDebug.shiftDown-1;
    end
end

function InterpolationPlayerPeriodDebug:prerender()
    self:stayOnSplitScreen();
    ISCollapsableWindow.prerender(self);
end

function InterpolationPlayerPeriodDebug:stayOnSplitScreen()
    ISUIElement.stayOnSplitScreen(self, self.playerNum)
end


function InterpolationPlayerPeriodDebug:render()
    ISCollapsableWindow.render(self);

    if InterpolationPlayerPeriodDebug.shiftDown>0 then
        InterpolationPlayerPeriodDebug.shiftDown = InterpolationPlayerPeriodDebug.shiftDown-1;
    end
end

function InterpolationPlayerPeriodDebug:setPlayer(player)
	self.player = player;
end

function InterpolationPlayerPeriodDebug:close()
    ISCollapsableWindow.close(self);
    if JoypadState.players[self.playerNum+1] then
        setJoypadFocus(self.playerNum, nil)
    end
    InterpolationPlayerPeriodDebug.instance = nil;
    self:removeFromUIManager();
    self:clear();
end

function InterpolationPlayerPeriodDebug:clear()
    self.currentTile = nil;
end



function InterpolationPlayerPeriodDebug:new (x, y, width, height, player)
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
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
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
    o.title = "Interpolation Player Period Debug";
    --o.viewList = {}
    o.resizable = true;
    o.drawFrame = true;

    o.currentTile = nil;
    o.richtext = nil;
    o.overrideBPrompt = true;
    o.subFocus = nil;
	o.player = nil;
    o.hotKeyPanels = {};
    o.isJoypadWindow = false;

    o.hourStamp = -1;
    o.dayStamp = -1;
    o.monthStamp = -1;
    o.year = -1;
	
	o.playerInfo = {};
    return o
end
--local function InterpolationPlayerPeriodDebugOntick()
    --getPlayer():getBodyDamage():RestoreToFullHealth();
--end

