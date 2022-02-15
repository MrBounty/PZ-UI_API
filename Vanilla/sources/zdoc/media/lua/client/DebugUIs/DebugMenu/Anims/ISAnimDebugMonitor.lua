--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************


require "ISUI/ISCollapsableWindow"

---@class ISAnimDebugMonitor : ISCollapsableWindow
ISAnimDebugMonitor = ISCollapsableWindow:derive("ISAnimDebugMonitor");

function ISAnimDebugMonitor.OnOpenPanel()
    if ISAnimDebugMonitor.instance==nil then
        ISAnimDebugMonitor.instance = ISAnimDebugMonitor:new (300, 100, 500, 750, getPlayer());
        ISAnimDebugMonitor.instance:initialise();
        ISAnimDebugMonitor.instance:instantiate();
    end

    ISAnimDebugMonitor.instance:addToUIManager();
    ISAnimDebugMonitor.instance:setVisible(true);


    return ISAnimDebugMonitor.instance;
end

function ISAnimDebugMonitor:initialise()
    ISCollapsableWindow.initialise(self);

    self.cGreen = { r=0,g=0.3,b=0,a=1};
    self.cRed = { r=0.3,g=0,b=0,a=1};
end


function ISAnimDebugMonitor:createChildren()
    ISCollapsableWindow.createChildren(self);

    local y, margin = self:titleBarHeight()+5, 5;
    local obj;

    ISDebugUtils.initHorzBars(self,10,self.width-20);

    --self.buttons = {"Show layers","Active nodes", "Anim tracks", "Show variables", "Show floats"};

    local xmarg = 10;
    local bw = (self.width/2)-(xmarg*2);
    local bw1 = (bw-xmarg)/2;
    local btnh = 16;

    y, self.buttonToggleMonitor = ISDebugUtils.addButton(self,"toggle_monitor",xmarg,y,self.width-(xmarg*2),btnh,"Toggle character monitor",ISAnimDebugMonitor.onClick);
    y = y+margin;

    local cacheY = y;
    y, self.buttonLayers = ISDebugUtils.addButton(self,"show_layers",xmarg,y,bw,btnh,"Show layers",ISAnimDebugMonitor.onClick);
    y, self.buttonVariables = ISDebugUtils.addButton(self,"show_variables",xmarg+bw+(xmarg*2),cacheY,bw,btnh,"Show variables",ISAnimDebugMonitor.onClick);

    y = y+margin;

    cacheY = y;
    y, self.buttonActiveNodes = ISDebugUtils.addButton(self,"active_nodes",xmarg,y,bw1,btnh,"Active nodes",ISAnimDebugMonitor.onClick);
    y, self.buttonAnimTracks = ISDebugUtils.addButton(self,"anim_tracks",xmarg+bw1+xmarg,cacheY,bw1,btnh,"Anim tracks",ISAnimDebugMonitor.onClick);
    y, self.buttonStamps = ISDebugUtils.addButton(self,"show_ticks",xmarg+bw+(xmarg*2),cacheY,bw,btnh,"Show tick stamps",ISAnimDebugMonitor.onClick);

    y = y+margin;

    y = ISDebugUtils.addHorzBar(self,y)+5;

    y, self.labelVars = ISDebugUtils.addLabel(self, "varedit_title", self.width*0.5, y, "EDIT VARIABLES", UIFont.Small);
    self.labelVars.center = true;
    y = y+3;

    cacheY = y;
    y, self.comboVars = ISDebugUtils.addComboBox(self,"combo_vars",xmarg,y,(self.width/2)-(xmarg*2),UIFont.Small,ISAnimDebugMonitor.onCombo);
    y, self.entryBoxValue = ISDebugUtils.addTextEntryBox(self, "entry_value", "", (self.width/2)+xmarg, cacheY, (self.width/2)-(xmarg*2), 18);
    self.entryBoxValue.backgroundColor = {r=0, g=0, b=0, a=0.75};
    y = y+margin;

    cacheY = y;
    y, self.buttonClearVar = ISDebugUtils.addButton(self,"varedit_clear",xmarg,y,bw,btnh,"Clear variable",ISAnimDebugMonitor.onClick);
    y, self.buttonSetVar = ISDebugUtils.addButton(self,"varedit_set",xmarg+bw+(xmarg*2),cacheY,bw,btnh,"Set variable",ISAnimDebugMonitor.onClick);

    y = y+margin;

    --y, self.buttonLayers = ISDebugUtils.addButton(self,"varedit_set",xmarg,y,bw,btnh,"Set",ISAnimDebugMonitor.onClick);
    --y, self.buttonLayers = ISDebugUtils.addButton(self,"varedit_del",xmarg,y,bw,btnh,"Clear",ISAnimDebugMonitor.onClick);

    y = ISDebugUtils.addHorzBar(self,y)+5;

    y, self.labelFloat = ISDebugUtils.addLabel(self, "float_title", self.width*0.5, y, "FLOAT VARIABLES", UIFont.Small);
    self.labelFloat.center = true;
    y = y+3;

    y, self.comboFloats = ISDebugUtils.addComboBox(self,"combo_floats",xmarg,y,self.width-(xmarg*2),UIFont.Small,ISAnimDebugMonitor.onCombo);
    self.comboFloats:addOption("- NONE -");
    self.comboFloats.selected = 1;
    y = y+margin;

    y, self.labelFloatInfo = ISDebugUtils.addLabel(self, "float_info", self.width*0.5, y, "selected float: none [min: -1.0, max: 1.0]", UIFont.Small);
    self.labelFloatInfo.center = true;
    y = y+3;

    self.floatPlotter = FloatArrayPlotter:new(xmarg,y,self.width-(xmarg*2),100);
    self.floatPlotter:initialise();

    self.floatPlotter:setHorzLine(0.125,{r=0.05, g=0.05, b=0.05, a=1});
    self.floatPlotter:setHorzLine(0.25,{r=0.1, g=0.1, b=0.1, a=1});
    --self.charts[i]:setHorzLine(0.30,{r=0.1, g=0.1, b=0.1, a=1});
    self.floatPlotter:setHorzLine(0.375,{r=0.05, g=0.05, b=0.05, a=1});
    self.floatPlotter:setHorzLine(0.50,{r=0.2, g=0.2, b=0.2, a=1});
    --self.charts[i]:setHorzLine(0.60,{r=0.1, g=0.1, b=0.1, a=1});
    self.floatPlotter:setHorzLine(0.625,{r=0.05, g=0.05, b=0.05, a=1});
    self.floatPlotter:setHorzLine(0.75,{r=0.1, g=0.1, b=0.1, a=1});
    --self.charts[i]:setHorzLine(0.80,{r=0.1, g=0.1, b=0.1, a=1});
    self.floatPlotter:setHorzLine(0.875,{r=0.05, g=0.05, b=0.05, a=1});
    self:addChild(self.floatPlotter);

    y = self.floatPlotter:getY() + self.floatPlotter:getHeight();

    y = ISDebugUtils.addHorzBar(self,y+5)+5;

    local h = self.height-y-self:resizeWidgetHeight()-5;
    --[[
    self.subPanel = ISAnimLoggerOutput:new(10, y, self.width-20, h);
    self.subPanel:initialise();
    self.subPanel:instantiate();
    self.subPanel:setAnchorRight(true);
    self.subPanel:setAnchorLeft(true);
    self.subPanel:setAnchorTop(true);
    self.subPanel:setAnchorBottom(true);
    self.subPanel.moveWithMouse = true;
    self.subPanel.doStencilRender = true;
    self.subPanel:addScrollBars();
    self.subPanel.vscroll:setVisible(true);
    self:addChild(self.subPanel);
    self.subPanel:setScrollChildren(true);
    self.subPanel.onMouseWheel = ISDebugUtils.onMouseWheel;
    --]]

    self.richtext = ISRichTextPanel:new(xmarg, y, self.width-20, h);
    self.richtext:initialise();

    self:addChild(self.richtext);

    self.richtext.backgroundColor = {r=0, g=0, b=0, a=1};
    --self.richtext.background = false;
    self.richtext.autosetheight = false;
    self.richtext.clip = true
    self.richtext:addScrollBars();

    self.clearText = "No monitor attached.";
    self.richtext.text = self.clearText;
    self.richtext:paginate();

    y = y+10;
    --self:setHeight(y);

    self:toggleEditEnabled(false);

    self.buttonToggleMonitor.backgroundColor = self.cRed;

    self.init = false;
    self.selectedVar = false;
end

function ISAnimDebugMonitor:scrollToBottom()
    local yscroll = -(self.richtext:getScrollHeight() - (self.richtext:getScrollAreaHeight()));
    self.richtext:setYScroll( yscroll );
end

function ISAnimDebugMonitor:clearLog()
    self.richtext.text = self.clearText;
    self.richtext:paginate();
    self:scrollToBottom();
end

function ISAnimDebugMonitor:toggleEditEnabled(_b)
    self.editEnabled = _b;
    self.buttonLayers:setEnable(_b);
    self.buttonActiveNodes:setEnable(_b);
    self.buttonAnimTracks:setEnable(_b);
    self.buttonVariables:setEnable(_b);
    self.buttonStamps:setEnable(_b);

    self.entryBoxValue:setText("");
    self.entryBoxValue:setEditable(_b);
    self.buttonClearVar:setEnable(_b);
    self.buttonSetVar:setEnable(_b);
    --self.comboVars.selected = 1;
    --self.comboFloats.selected = 1;
    if _b == false then
        self.buttonLayers.backgroundColor = {r=0, g=0, b=0, a=1.0};
        self.buttonActiveNodes.backgroundColor = {r=0, g=0, b=0, a=1.0};
        self.buttonAnimTracks.backgroundColor = {r=0, g=0, b=0, a=1.0};
        self.buttonVariables.backgroundColor = {r=0, g=0, b=0, a=1.0};
        self.buttonStamps.backgroundColor = {r=0, g=0, b=0, a=1.0};

        self.buttonClearVar.backgroundColor = {r=0, g=0, b=0, a=1.0};
        self.buttonSetVar.backgroundColor = {r=0, g=0, b=0, a=1.0};
    end
end

function ISAnimDebugMonitor:onClick(_button)
    if self.buttonToggleMonitor==_button then
        local char = getPlayer();
        local smallestDist = 100000;
    --  for x=-5,5 do
      --      for y=-5,5 do
        --        local sq = getCell():getGridSquare(char:getX() + x,char:getY() + y,char:getZ());
          --      for i=0,sq:getMovingObjects():size()-1 do
            --        local moving = sq:getMovingObjects():get(i);
              --      if instanceof(moving, "IsoZombie") then
--
  --                      local dist = char:DistTo(getPlayer());
--
  --                      if(dist < smallestDist) then
    --                        char = moving;
      --                      smallestDist = dist;
        --                end
--
  --                  end
    --            end
      --          if instanceof(char, "IsoZombie") then break; end
        --    end
          --  if instanceof(char, "IsoZombie") then break; end
        --end

        self.monitor = char:getDebugMonitor();
        if self.monitor then
            self.monitor = nil;
            self.buttonToggleMonitor.backgroundColor = self.cRed;

            self:toggleEditEnabled(false);

            --self.subPanel:clear();
            self:clearLog();
            self.floatPlotter:setData(nil);
        else
            self.monitor = AnimatorDebugMonitor.new(char);
            self.buttonToggleMonitor.backgroundColor = self.cGreen;

            self:toggleEditEnabled(true);
        end
        char:setDebugMonitor(self.monitor);
        --self.subPanel:setMonitor(self.monitor);
        return;
    elseif self.buttonClearVar==_button then
        local sel = self.comboVars.options[self.comboVars.selected];
        local char = getPlayer();
        if sel then
            char:ClearVariable(sel);
            self.entryBoxValue:setText("");
        end
    elseif self.buttonSetVar==_button then
        local sel = self.comboVars.options[self.comboVars.selected];
        local val = self.entryBoxValue:getText();
        local char = getPlayer();
        if sel and val then
            char:SetVariable(sel, val);
        end
    end

    local monitor = self.monitor;
    if monitor then
        if self.buttonLayers==_button then
            local index = 1;
            local val = not monitor:getFilter(index);
            monitor:setFilter(index,val);
            if not val then
                self.oldNodesVal = {monitor:getFilter(2)};
                monitor:setFilter(2,val);
                self.oldTracksVal = {monitor:getFilter(3)};
                monitor:setFilter(3,val);
                self.buttonActiveNodes:setEnable(false);
                self.buttonAnimTracks:setEnable(false);
            else
                self.buttonActiveNodes:setEnable(true);
                self.buttonAnimTracks:setEnable(true);
                if self.oldNodesVal then monitor:setFilter(2,self.oldNodesVal[1]); end
                if self.oldTracksVal then monitor:setFilter(3,self.oldTracksVal[1]); end
            end
        elseif self.buttonActiveNodes==_button then
            local index = 2;
            local val = not monitor:getFilter(index);
            monitor:setFilter(index,val);
        elseif self.buttonAnimTracks==_button then
            local index = 3;
            local val = not monitor:getFilter(index);
            monitor:setFilter(index,val);
        elseif self.buttonVariables==_button then
            local index = 4;
            local val = not monitor:getFilter(index);
            monitor:setFilter(index,val);
        elseif self.buttonStamps==_button then
            monitor:setDoTickStamps(not monitor:isDoTickStamps());
        end
    end
end

function ISAnimDebugMonitor:onCombo(_combo)
    if not self.editEnabled then return; end
    if self.comboFloats == _combo then
        local sel = _combo.options[_combo.selected];
        --print("combo selected "..tostring(sel))
        if sel and sel~="- NONE -" and self.monitor then
            self.monitor:setSelectedVariable(sel);
            self.selectedVar = sel;
        else
            self.selectedVar = false;
        end
    elseif self.comboVars == _combo then
        local sel = _combo.options[_combo.selected];
        if not sel then return; end
        local char = getPlayer();
        local val = char:getVariableString(sel);
        if val then
            self.entryBoxValue:setText(val);
        end
    end
end

function ISAnimDebugMonitor:onResize()
    ISUIElement.onResize(self);
    local th = self:titleBarHeight();
end

function ISAnimDebugMonitor:update()
    ISCollapsableWindow.update(self);

    if not self.editEnabled then
        self.comboVars.selected = 1;
        self.comboFloats.selected = 1;
    end

    if self.monitor then
        if self.monitor:IsDirty() then
            self.richtext.text = self.monitor:getLogString();
            self.richtext:paginate();
            self:scrollToBottom();
        end

        self.buttonLayers.backgroundColor = self.monitor:getFilter(1) and self.cGreen or self.cRed;
        self.buttonActiveNodes.backgroundColor = self.monitor:getFilter(2) and self.cGreen or self.cRed;
        self.buttonAnimTracks.backgroundColor = self.monitor:getFilter(3) and self.cGreen or self.cRed;
        self.buttonVariables.backgroundColor = self.monitor:getFilter(4) and self.cGreen or self.cRed;
        self.buttonStamps.backgroundColor = self.monitor:isDoTickStamps() and self.cGreen or self.cRed;

        if self.monitor:IsDirtyFloatList() or (not self.init) then
            local sel = self.comboFloats.options[self.comboFloats.selected];

            self.comboFloats:clear();
            local list = self.monitor:getFloatNames();
            self.comboFloats:addOption("- NONE -");
            local newindex = 1;
            for i =0,list:size()-1 do
                local name = list:get(i);
                self.comboFloats:addOption(name);
                if sel and sel==name then
                    newindex = i+2;
                end
            end
            self.comboFloats.selected = newindex;
        end

        if AnimatorDebugMonitor:isKnownVarsDirty() or (not self.init) then
            local sel = self.comboVars.options[self.comboVars.selected];

            self.comboVars:clear();
            local list = AnimatorDebugMonitor:getKnownVariables();

            local newindex = 0;
            for i =0,list:size()-1 do
                local name = list:get(i);
                self.comboVars:addOption(name);
                if sel and sel==name then
                    newindex = i+1;
                end
            end
            self.comboVars.selected = newindex;
        end

        local floats = false;
        if self.selectedVar then
            local v = self.selectedVar;
            if self.monitor:getSelectedVariable() and self.monitor:getSelectedVariable()==v then
                local min = self.monitor:getSelectedVarMinFloat();
                local max = self.monitor:getSelectedVarMaxFloat();
                local cur = self.monitor:getSelectedVariableFloat();
                min = ISDebugUtils.roundNum(min,5);
                max = ISDebugUtils.roundNum(max,5);
                cur = ISDebugUtils.roundNum(cur,5);
                self.labelFloatInfo.name = "selected float: "..v.." = "..tostring(cur).." [min: "..min..", max: "..max.."]";
                floats = self.monitor:getSelectedVarFloatList();
            else
                self.selectedVar = false;
            end
        end

        if not self.selectedVar then
            self.labelFloatInfo.name = "selected float: none [min: -1.0, max: 1.0]";
        end

        self.floatPlotter:setData(floats);

        self.init = true;
    end
end

function ISAnimDebugMonitor:prerender()
    self:stayOnSplitScreen();
    ISCollapsableWindow.prerender(self);

    ISDebugUtils.renderHorzBars(self);
end

function ISAnimDebugMonitor:stayOnSplitScreen()
    ISUIElement.stayOnSplitScreen(self, self.playerNum)
end


function ISAnimDebugMonitor:render()
    ISCollapsableWindow.render(self);
end


function ISAnimDebugMonitor:close()
    ISCollapsableWindow.close(self)
    if JoypadState.players[self.playerNum+1] then
        setJoypadFocus(self.playerNum, nil)
    end
    self:removeFromUIManager();
    self:clear();
end

function ISAnimDebugMonitor:clear()
end



function ISAnimDebugMonitor:new (x, y, width, height, player)
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
    o.backgroundColor = {r=0, g=0, b=0, a=0.6};
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
    o.title = "Animator Debug Monitor";
    o.resizable = false;
    o.drawFrame = true;

    o.currentTile = nil;
    o.richtext = nil;
    o.overrideBPrompt = true;
    o.subFocus = nil;
    o.hotKeyPanels = {};
    o.isJoypadWindow = false;
    ISDebugMenu.RegisterClass(self);
    return o
end

--[[
if enabled then
    Events.OnCustomUIKey.Add(ISAnimDebugMonitor.OnKeyDown);
    Events.OnKeyKeepPressed.Add(ISAnimDebugMonitor.OnKeepKeyDown);
    Events.OnClimateTickDebug.Add(ISAnimDebugMonitor.OnClimateTickDebug);
    Events.OnThunderEvent.Add(ISAnimDebugMonitor.OnThunderEvent);
    --Events.OnObjectLeftMouseButtonUp.Add(ISAnimDebugMonitor.onMouseButtonUp);
end--]]
