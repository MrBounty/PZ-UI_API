--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "ISUI/ISPanelJoypad"

ISClothingInsPanel = ISPanelJoypad:derive("ISClothingInsPanel");

ISClothingInsPanel.viewSimpleID = "viewSimple";
ISClothingInsPanel.viewAdvancedID = "viewAdvanced";

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small);
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium);

local function round(num, numDecimalPlaces)
    local mult = 10^(numDecimalPlaces or 0)
    return math.floor(num * mult + 0.5) / mult
end

--************************************************************************--
--** ISPanel:initialise
--**
--private static Color col_0 = new Color(29,34,237); //blue
--private static Color col_25 = new Color(0,255,234); //cyan
--private static Color col_50 = new Color(84,255,55); //green
--private static Color col_75 = new Color(255,246,0); //yellow
--private static Color col_100 = new Color(255,0,0); //red
--************************************************************************--

function ISClothingInsPanel:initialise()
    ISPanelJoypad.initialise(self);
    self:create();
end

function ISClothingInsPanel:createChildren()
    ISPanelJoypad.createChildren(self);

    local y = 8;
    self.bpPanelX = 0;
    self.bpPanelY = y;
    self.bpAnchorX = 123;
    self.bpAnchorY = 50;
    self.bodyPartPanel = ISBodyPartPanel:new(self.player, self.bpPanelX, self.bpPanelY, self, ISClothingInsPanel.setSelection);
    self.bodyPartPanel:initialise();
    self.bodyPartPanel:setEnableSelectLines( true, self.bpAnchorX, self.bpAnchorY );
    self.bodyPartPanel:enableNodes( "media/ui/BodyParts/bps_node_diamond", "media/ui/BodyParts/bps_node_diamond_outline" );
    self.bodyPartPanel:overrideNodeTexture( BodyPartType.Torso_Upper, "media/ui/BodyParts/bps_node_big", "media/ui/BodyParts/bps_node_big_outline" );
    self.bodyPartPanel:setColorScheme(self.colorScheme);

    self:addChild(self.bodyPartPanel);

    y = self.bodyPartPanel:getY() + self.bodyPartPanel:getHeight();

    if isDebugEnabled() then
        local btnx = 10;
        local resetBtn;
        y, resetBtn = ISDebugUtils.addButton(self, {}, btnx, y, 50, 20, "-reset-", ISClothingInsPanel.onResetButton);
    end

    self.mainGroup = {};
    self.nodeGroup = {};


    y = 12;
    --ISLabel:new (x, y, height, name, r, g, b, a, font, bLeft)
    local c = self.btnTextColor;
    local name = getText("IGUI_Temp_CoreTemp");
    self.labelCoreTemp = ISLabel:new(263, y, 16, name, c.r, c.g, c.b, c.a, UIFont.Small, true);
    self.labelCoreTemp.prefixName = name;
    self.labelCoreTemp.center = true;
    self:addMainGroup(self.labelCoreTemp);

    y = y + FONT_HGT_SMALL;
    self.labelCoreTempMid = ISLabel:new(263, y, 16, getText("IGUI_Temp_Normal"), self.defTextColor.r, self.defTextColor.g, self.defTextColor.b, self.defTextColor.a, UIFont.Small, true);
    self.labelCoreTempMid.center = true;
    self:addMainGroup(self.labelCoreTempMid);

    self.labelCoreTempMin = ISLabel:new(135, y, 16, getText("IGUI_Temp_Cold"), self.defTextColor.r, self.defTextColor.g, self.defTextColor.b, self.defTextColor.a, UIFont.Small, true);
    self:addMainGroup(self.labelCoreTempMin);

    self.labelCoreTempMax = ISLabel:new(390, y, 16, getText("IGUI_Temp_Hot"), self.defTextColor.r, self.defTextColor.g, self.defTextColor.b, self.defTextColor.a, UIFont.Small, false);
    self:addMainGroup(self.labelCoreTempMax);

    self.coreTemperatureBar = ISGradientBar:new(135, y + FONT_HGT_SMALL, 255, 10);
    self.coreTemperatureBar:setGradientTexture(getTexture("media/ui/BodyInsulation/heatbar_horz"));
    self.coreTemperatureBar:setValue(0.36);
    self.coreTemperatureBar:setHighlightRadius(25);
    self.coreTemperatureBar:setDoKnob(false);
    self:addMainGroup(self.coreTemperatureBar);

    y = y + FONT_HGT_SMALL + 20;

    name = getText("IGUI_Temp_BodyHeat");
    self.labelCoreHeat = ISLabel:new(263, y, FONT_HGT_SMALL, name, c.r, c.g, c.b, c.a, UIFont.Small, true);
    self.labelCoreHeat.prefixName = name;
    self.labelCoreHeat.center = true;
    self:addMainGroup(self.labelCoreHeat);

    y = y + FONT_HGT_SMALL;
    self.labelCoreHeatMid = ISLabel:new(263, y, FONT_HGT_SMALL, getText("IGUI_Temp_Normal"), self.defTextColor.r, self.defTextColor.g, self.defTextColor.b, self.defTextColor.a, UIFont.Small, true);
    self.labelCoreHeatMid.center = true;
    self:addMainGroup(self.labelCoreHeatMid);

    self.labelCoreHeatMin = ISLabel:new(135, y, FONT_HGT_SMALL, getText("IGUI_Temp_Low"), self.defTextColor.r, self.defTextColor.g, self.defTextColor.b, self.defTextColor.a, UIFont.Small, true);
    self:addMainGroup(self.labelCoreHeatMin);

    self.labelCoreHeatMax = ISLabel:new(390, y, FONT_HGT_SMALL, getText("IGUI_Temp_High"), self.defTextColor.r, self.defTextColor.g, self.defTextColor.b, self.defTextColor.a, UIFont.Small, false);
    self:addMainGroup(self.labelCoreHeatMax);

    self.coreHeatBar = ISGradientBar:new(135, y + FONT_HGT_SMALL, 255, 10);
    self.coreHeatBar:setGradientTexture(getTexture("media/ui/BodyInsulation/heatbar_horz"));
    self.coreHeatBar:setValue(0.1);
    self.coreHeatBar:setHighlightRadius(25);
    self.coreHeatBar:setDoKnob(false);
    self:addMainGroup(self.coreHeatBar);

    y = y + FONT_HGT_SMALL + 20;

    y = y + 10;
    self.currentViewID = ISClothingInsPanel.viewSimpleID;
    self.viewButtons = {};
    self.nodeDetails = {};

    self.mainGroupViews = {};
    self.nodeGroupViews = {};
    self.maxViewsY = y

    self:addView(ISClothingInsPanel.viewSimpleID, self.viewsSimple, y);
    self:addView(ISClothingInsPanel.viewAdvancedID, self.viewsAdvanced, y);

    self.coreRectangleH = self.maxViewsY + 10 - 12;

    --[[
    self.viewButtons = {};
    local btn;
    for i=1,#self.views do
        y, btn = ISDebugUtils.addButton(self, {index = i}, 162, y, 200, 20, self.views[i].title, ISClothingInsPanel.onClickViewButton);
        if i==1 then
            btn.textColor = self.titleColor;
        end
        table.insert(self.viewButtons, btn);
        self:addMainGroup(btn, true);
        y = y+10;
    end

    -- node GrOuP

    y = 12;
    self.nodeDetails = {};
    local view;
    for i=1,#self.views do
        view = self.views[i];

        local t = {};
        local c = self.titleColor;
        local name = string.lower(self.views[i].title) or "none";
        t.labelTitle = ISLabel:new(263, y, 16, name, c.r, c.g, c.b, c.a, UIFont.Small, true);
        t.labelTitle.prefixName = name;
        t.labelTitle.center = true;
        self:addNodeGroup(t.labelTitle);

        y = y+12;
        if view.mid then
            t.labelMid = ISLabel:new(263, y, 16, view.mid, self.defTextColor.r, self.defTextColor.g, self.defTextColor.b, self.defTextColor.a, UIFont.Small, true);
            t.labelMid.center = true;
            self:addNodeGroup(t.labelMid);
        end

        if view.min then
            t.labelMin = ISLabel:new(135, y, 16, view.min, self.defTextColor.r, self.defTextColor.g, self.defTextColor.b, self.defTextColor.a, UIFont.Small, true);
            self:addNodeGroup(t.labelMin);
        end

        if view.max then
            t.labelMax = ISLabel:new(390, y, 16, view.max, self.defTextColor.r, self.defTextColor.g, self.defTextColor.b, self.defTextColor.a, UIFont.Small, false);
            self:addNodeGroup(t.labelMax);
        end

        t.bar = ISGradientBar:new(135, y+14, 255, 10);
        t.bar:setGradientTexture(view.texture);
        t.bar:setValue(0.5);
        if i>=6 then
            t.bar:setValue(0);
        end
        t.bar:setHighlightRadius(25);
        t.bar:setDoKnob(false);
        self:addNodeGroup(t.bar);

        table.insert(self.nodeDetails, t);

        y = y + 31;
    end
    --]]


    y = math.max(320, self.maxViewsY + 20);
    local c = self.btnTextColor;
    self.labelCurrentView = ISLabel:new(200, y, FONT_HGT_MEDIUM, getText("IGUI_Temp_SkinTemperature"), c.r, c.g, c.b, c.a, UIFont.Medium, true);
    self.labelCurrentView.center = true;
    self:addChild(self.labelCurrentView);

    y = y + FONT_HGT_MEDIUM;

    self.labelCurrentViewMid = ISLabel:new(200, y, FONT_HGT_SMALL, "Mid", self.defTextColor.r, self.defTextColor.g, self.defTextColor.b, self.defTextColor.a, UIFont.Small, true);
    self.labelCurrentViewMid.center = true;
    self:addChild(self.labelCurrentViewMid);

    self.labelCurrentViewMin = ISLabel:new(10, y, FONT_HGT_SMALL, "Min", self.defTextColor.r, self.defTextColor.g, self.defTextColor.b, self.defTextColor.a, UIFont.Small, true);
    self:addChild(self.labelCurrentViewMin);

    self.labelCurrentViewMax = ISLabel:new(390, y, FONT_HGT_SMALL, "Max", self.defTextColor.r, self.defTextColor.g, self.defTextColor.b, self.defTextColor.a, UIFont.Small, false);
    self:addChild(self.labelCurrentViewMax);

    self.legendBar = ISGradientBar:new(10, y + FONT_HGT_SMALL, 380, 20);
    self.legendBar:setGradientTexture(getTexture("media/ui/BodyInsulation/heatbar_horz"));
    self.legendBar:setValue(0.5);
    self.legendBar:setHighlightRadius(25);
    self.legendBar:setDoKnob(false);
    self.legendBar:setDarkAlpha(0);
    self:addChild(self.legendBar);

    y, self.toggleAdvBtn = ISDebugUtils.addButton(self, {}, 10, y + FONT_HGT_SMALL + 20 + 4, 380, FONT_HGT_SMALL + 2 * 2, getText("IGUI_Temp_AdvView"), ISClothingInsPanel.onToggleViewStyle);
    self:addChild(self.toggleAdvBtn);

    self.selectedViewIndex = 1;
    self:setViewStyle(ISClothingInsPanel.viewSimpleID, true);
    self:setSelection(nil);
    self:setViewIndex(1);
end

function ISClothingInsPanel:addView(_viewID, _viewTable, _y)
    local y = _y;

    self.mainGroupViews[_viewID] = {};
    self.nodeGroupViews[_viewID] = {};

    self.viewButtons[_viewID] = {};
    local btn;
    for i=1,#_viewTable do
        y, btn = ISDebugUtils.addButton(self, {index = i}, 162, y, 200, FONT_HGT_SMALL + 2 * 2, _viewTable[i].title, ISClothingInsPanel.onClickViewButton);
        if i==1 then
            btn.textColor = self.titleColor;
        end
        table.insert(self.viewButtons[_viewID], btn);
        self:addMainGroup(btn, true, _viewID);
        y = y+10;
    end

    self.maxViewsY = math.max(self.maxViewsY, y - 10)

    -- node GrOuP

    y = 12;
    self.nodeDetails[_viewID] = {};
    local view;
    for i=1,#_viewTable do
        view = _viewTable[i];

        local t = {};
        local c = self.titleColor;
        local name = view.title or "None"; --string.lower(view.title)
        t.labelTitle = ISLabel:new(263, y, 16, name, c.r, c.g, c.b, c.a, UIFont.Small, true);
        t.labelTitle.prefixName = name;
        t.labelTitle.center = true;
        self:addNodeGroup(t.labelTitle, _viewID);

        y = y + FONT_HGT_SMALL;
        if view.mid then
            t.labelMid = ISLabel:new(263, y, 16, view.mid, self.defTextColor.r, self.defTextColor.g, self.defTextColor.b, self.defTextColor.a, UIFont.Small, true);
            t.labelMid.center = true;
            self:addNodeGroup(t.labelMid, _viewID);
        end

        if view.min then
            t.labelMin = ISLabel:new(135, y, 16, view.min, self.defTextColor.r, self.defTextColor.g, self.defTextColor.b, self.defTextColor.a, UIFont.Small, true);
            self:addNodeGroup(t.labelMin, _viewID);
        end

        if view.max then
            t.labelMax = ISLabel:new(390, y, 16, view.max, self.defTextColor.r, self.defTextColor.g, self.defTextColor.b, self.defTextColor.a, UIFont.Small, false);
            self:addNodeGroup(t.labelMax, _viewID);
        end

        t.bar = ISGradientBar:new(135, y + FONT_HGT_SMALL, 255, 10);
        t.bar:setGradientTexture(view.texture);
        t.bar:setValue(0.5);
        if i>=6 then
            t.bar:setValue(0);
        end
        t.bar:setHighlightRadius(25);
        t.bar:setDoKnob(false);
        self:addNodeGroup(t.bar, _viewID);

        table.insert(self.nodeDetails[_viewID], t);

        y = t.bar:getBottom() + 4;

        self.maxViewsY = math.max(self.maxViewsY, y - 4)
    end
end

function ISClothingInsPanel:addMainGroup(_widget, _ignoreAddChild, _view)
    if _view then
        table.insert(self.mainGroupViews[_view], _widget);
    else
        table.insert(self.mainGroup, _widget);
    end
    if not _ignoreAddChild then
        self:addChild(_widget);
    end
end

function ISClothingInsPanel:addNodeGroup(_widget, _view)
    if _view then
        table.insert(self.nodeGroupViews[_view], _widget);
    else
        table.insert(self.nodeGroup, _widget);
    end
    self:addChild(_widget);
end

function ISClothingInsPanel:create()
    self.views = {};
    self.viewsSimple = {};
    self.viewsAdvanced = {};

    local colorScheme = {
        { val = 0.00, color = Color.new(  29/255,  34/255, 237/255, 1 ) },
        { val = 0.25, color = Color.new(   0/255, 255/255, 234/255, 1 ) },
        { val = 0.50, color = Color.new(  84/255, 255/255,  55/255, 1 ) },
        { val = 0.75, color = Color.new( 255/255, 246/255,   0/255, 1 ) },
        { val = 1.00, color = Color.new( 255/255,   0/255,   0/255, 1 ) },
    };
    self.colorScheme = colorScheme;

    local colorSchemeBlues = {
        { val = 0.00, color = Color.new(  84/255, 255/255,  55/255, 1 ) },
        { val = 0.50, color = Color.new(   0/255, 255/255, 234/255, 1 ) },
        { val = 1.00, color = Color.new(  29/255,  34/255, 237/255, 1 ) },
    }

    local tex = getTexture("media/ui/BodyInsulation/heatbar_horz");
    local texBlues = getTexture("media/ui/BodyInsulation/heatbar_horz_blues");
    local texReds = getTexture("media/ui/BodyInsulation/heatbar_horz_reds");

    -- 1
    table.insert(self.viewsAdvanced, {
        title = getText("IGUI_Temp_SkinTemperature"),
        min = getText("IGUI_Temp_Cold"),
        mid = getText("IGUI_Temp_Normal"),
        max = getText("IGUI_Temp_Hot"),
        texture = tex,
        scheme = colorScheme,
        functionName = "getSkinCelcius",
    });

    -- 2
    --[[
    table.insert(self.views, {
        title = "Heat gain/loss",
        min = "loss",
        mid = "stable",
        max = "gain",
        texture = tex,
        scheme = colorScheme,
        functionName = "getHeatDelta",
    });
    --]]

    -- 3
    table.insert(self.viewsAdvanced, {
        title = getText("IGUI_Temp_BloodVessels"),
        min = getText("IGUI_Temp_Vasoconstriction"),
        mid = getText("IGUI_Temp_Normal"),
        max = getText("IGUI_Temp_Vasodilation"),
        texture = tex,
        scheme = colorScheme,
        functionName = "getPrimaryDelta",
    });

    -- 4
    table.insert(self.viewsAdvanced, {
        title = getText("IGUI_Temp_ShiverPerspiration"),
        min = getText("IGUI_Temp_Shivering"),
        mid = getText("IGUI_Temp_Normal"),
        max = getText("IGUI_Temp_Perspiring"),
        texture = tex,
        scheme = colorScheme,
        functionName = "getSecondaryDelta",
    });

    -- 6
    table.insert(self.viewsAdvanced, {
        title = getText("IGUI_Temp_BodyWetness"),
        min = getText("IGUI_Temp_Dry"),
        mid = false,
        max = getText("IGUI_Temp_Soaked"),
        texture = texBlues,
        scheme = colorSchemeBlues,
        functionName = "getBodyWetness",
    });

    -- 7
    table.insert(self.viewsAdvanced, {
        title = getText("IGUI_Temp_ClothingWetness"),
        min = getText("IGUI_Temp_Dry"),
        mid = false,
        max = getText("IGUI_Temp_Soaked"),
        texture = texBlues,
        scheme = colorSchemeBlues,
        functionName = "getClothingWetness",
    });

    -- 5
    table.insert(self.viewsAdvanced, {
        title = getText("IGUI_Temp_Insulation"),
        min = getText("IGUI_Temp_Low"),
        mid = getText("IGUI_Temp_Med"),
        max = getText("IGUI_Temp_High"),
        texture = tex,
        scheme = colorScheme,
        functionName = "getInsulation",
        showValue = true, -- this shows the actual value in the UI panel as postfix to title regardless of debug mode true or not.
    });

    table.insert(self.viewsAdvanced, {
        title = getText("IGUI_Temp_WindResistance"),
        min = getText("IGUI_Temp_Low"),
        mid = getText("IGUI_Temp_Med"),
        max = getText("IGUI_Temp_High"),
        texture = tex,
        scheme = colorScheme,
        functionName = "getWindresist",
        showValue = true,
    });

    -- SIMPLE VIeWS
    table.insert(self.viewsSimple, {
        title = getText("IGUI_Temp_SkinTemperature"),
        min = getText("IGUI_Temp_Cold"),
        mid = getText("IGUI_Temp_Normal"),
        max = getText("IGUI_Temp_Hot"),
        texture = tex,
        scheme = colorScheme,
        functionName = "getSkinCelcius",
    });

    table.insert(self.viewsSimple, {
        title = getText("IGUI_Temp_BodyResponse"),
        min = getText("IGUI_Temp_FightCold"),
        mid = getText("IGUI_Temp_Normal"),
        max = getText("IGUI_Temp_FightHot"),
        texture = tex,
        scheme = colorScheme,
        functionName = "getBodyResponse",
    });

    table.insert(self.viewsSimple, {
        title = getText("IGUI_Temp_Insulation"),
        min = getText("IGUI_Temp_Low"),
        mid = getText("IGUI_Temp_Med"),
        max = getText("IGUI_Temp_High"),
        texture = tex,
        scheme = colorScheme,
        functionName = "getInsulation",
        showValue = true, -- this shows the actual value in the UI panel as postfix to title regardless of debug mode true or not.
    });

    for i=1,#self.viewsAdvanced do
        self.viewsAdvanced[i].functionNameUI = self.viewsAdvanced[i].functionName .. "UI";
    end
    for i=1,#self.viewsSimple do
        self.viewsSimple[i].functionNameUI = self.viewsSimple[i].functionName .. "UI";
    end

    self.views = self.viewsSimple;
end

function ISClothingInsPanel:onResetButton(_btn)
    local thermos = self.player:getBodyDamage():getThermoregulator();
    if thermos then
        thermos:reset();
    end
end

function ISClothingInsPanel:onClickViewButton(_btn)
    self:setViewIndex(_btn.customData.index);
end

function ISClothingInsPanel:onToggleViewStyle(_btn)
    local viewStyle;
    if self.currentViewID==ISClothingInsPanel.viewSimpleID then
        viewStyle = ISClothingInsPanel.viewAdvancedID;
        self.toggleAdvBtn:setTitle(getText("IGUI_Temp_DefView"));
    else
        viewStyle = ISClothingInsPanel.viewSimpleID;
        self.toggleAdvBtn:setTitle(getText("IGUI_Temp_AdvView"));
    end

    self:setViewStyle(viewStyle);
end

function ISClothingInsPanel:setViewStyle(_viewStyle, _force)
    if _viewStyle~=ISClothingInsPanel.viewSimpleID and _viewStyle~=ISClothingInsPanel.viewAdvancedID then
        return;
    end

    if self.currentViewID==_viewStyle and (not _force) then
        return;
    end

    self.currentViewID = _viewStyle;
    local oldViewID;
    if _viewStyle==ISClothingInsPanel.viewSimpleID then
        self.views = self.viewsSimple;
        oldViewID = ISClothingInsPanel.viewAdvancedID;
    else
        self.views = self.viewsAdvanced;
        oldViewID = ISClothingInsPanel.viewSimpleID;
    end

    for i=1,#self.mainGroupViews[oldViewID] do
        self.mainGroupViews[oldViewID][i]:setVisible(false);
    end
    for i=1,#self.nodeGroupViews[oldViewID] do
        self.nodeGroupViews[oldViewID][i]:setVisible(false);
    end

    for i=1,#self.mainGroupViews[self.currentViewID] do
        self.mainGroupViews[self.currentViewID][i]:setVisible(true);
    end
    for i=1,#self.nodeGroupViews[self.currentViewID] do
        self.nodeGroupViews[self.currentViewID][i]:setVisible(true);
    end

    self.bodyPartPanel:deselect();
    self:setSelection(nil);

    self:setViewIndex(1);
end

function ISClothingInsPanel:setViewIndex(_index)
    local index = PZMath.clamp(_index, 1, #self.views);
    self.selectedViewIndex = index;

    local view = self.views[index];

    self.labelCurrentView:setName(view.title);

    if view.min then
        self.labelCurrentViewMin:setName(view.min);
        self.labelCurrentViewMin:setVisible(true);
    else
        self.labelCurrentViewMin:setVisible(false);
    end

    if view.mid then
        self.labelCurrentViewMid:setName(view.mid);
        self.labelCurrentViewMid:setVisible(true);
    else
        self.labelCurrentViewMid:setVisible(false);
    end

    if view.max then
        self.labelCurrentViewMax:setName(view.max);
        self.labelCurrentViewMax:setVisible(true);
    else
        self.labelCurrentViewMax:setVisible(false);
    end

    self.legendBar:setGradientTexture(view.texture);

    self.bodyPartPanel:setColorScheme(view.scheme);

    for i=1,#self.viewButtons[self.currentViewID] do
        if i==index then
            self.viewButtons[self.currentViewID][i].textColor = self.titleColor;
        else
            self.viewButtons[self.currentViewID][i].textColor = self.btnTextColor;
        end
    end

    for i=1,#self.nodeDetails[self.currentViewID] do
        if i==index then
            self.nodeDetails[self.currentViewID][i].labelTitle:setColor(self.titleColor.r,self.titleColor.g,self.titleColor.b);
        else
            self.nodeDetails[self.currentViewID][i].labelTitle:setColor(self.btnTextColor.r,self.btnTextColor.g,self.btnTextColor.b);
        end
    end
end

function ISClothingInsPanel:setSelection(_node)
    for i=1,#self.mainGroup do
        self.mainGroup[i]:setVisible(_node==nil);
    end
    for i=1,#self.nodeGroup do
        self.nodeGroup[i]:setVisible(_node~=nil);
    end

    for i=1,#self.mainGroupViews[self.currentViewID] do
        self.mainGroupViews[self.currentViewID][i]:setVisible(_node==nil);
    end
    for i=1,#self.nodeGroupViews[self.currentViewID] do
        self.nodeGroupViews[self.currentViewID][i]:setVisible(_node~=nil);
    end
    self.selectedBodyPart = _node~=nil and _node.bodyPartType or false;
end

function ISClothingInsPanel:prerender()
    ISPanelJoypad.prerender(self);

    local thermos = self.player:getBodyDamage():getThermoregulator();

    local v;

    self.coreTemperatureBar:setValue(thermos:getCoreTemperatureUI());
    self.coreHeatBar:setValue(thermos:getHeatGenerationUI());

    if isDebugEnabled() then
        v = thermos:getCoreTemperature();
        local nameLabel = self.labelCoreTemp.prefixName .. " (" .. (tostring(round(v,isDebugEnabled() and 5 or 1)) or "0") .. ")";
        if isDebugEnabled() then
            nameLabel = self.labelCoreTemp.prefixName .. " (" .. (tostring(round(v,isDebugEnabled() and 5 or 1)) or "0") .. ", R = " .. tostring(round(thermos:getCoreHeatDelta(),5)) .. ")";
        end
        self.labelCoreTemp:setName(nameLabel)
        v = thermos:getMetabolicRateReal();
        local v2 = thermos:getMetabolicRate();
        self.labelCoreHeat:setName(self.labelCoreHeat.prefixName .. " ( real: " .. (tostring(round(v,isDebugEnabled() and 2 or 1)) or "0") .." / ".. (tostring(round(v2,isDebugEnabled() and 2 or 1)) or "0") .. ")")
    end

    for i=0,BodyPartType.ToIndex(BodyPartType.MAX)-1 do
        local type = BodyPartType.FromIndex(i);

        local thermalNode = thermos:getNodeForType(type);

        if thermalNode then
            v = thermalNode[self.views[self.selectedViewIndex].functionNameUI](thermalNode);
            self.bodyPartPanel:setValue( type, v);
            --self.bodyPartPanel:setValue( type, self:getNodeValue(thermalNode, self.selectedViewIndex));

            if self.selectedBodyPart and self.selectedBodyPart == type then
                for j=1,#self.views do
                    --self.nodeDetails[j].bar:setValue(self:getNodeValue(thermalNode, j));
                    v = thermalNode[self.views[j].functionNameUI](thermalNode);
                    self.nodeDetails[self.currentViewID][j].bar:setValue(v);

                    if isDebugEnabled() or self.views[j].showValue then
                        v = thermalNode[self.views[j].functionName](thermalNode);
                        self.nodeDetails[self.currentViewID][j].labelTitle:setName(self.nodeDetails[self.currentViewID][j].labelTitle.prefixName .. " (" .. (tostring(round(v,isDebugEnabled() and 5 or 2)) or "0") .. ")");
                    end
                end
            end
        end

        --[[if self.selectedViewIndex==1 then -- skin
        elseif self.selectedViewIndex==2 then -- heat
        elseif self.selectedViewIndex==3 then -- bloodvessels
        elseif self.selectedViewIndex==4 then -- shiver/persp
        elseif self.selectedViewIndex==5 then -- insulation
        elseif self.selectedViewIndex==6 then -- body wetness
        elseif self.selectedViewIndex==7 then -- clothing wetness
        end--]]
    end
end

--[[
function ISClothingInsPanel:getNodeValue(_node, _valueType)
    local v = 0;

    if _valueType==1 then -- skin
        v = _node:getSkinCelciusUI();
    elseif _valueType==2 then -- heat
        v = _node:getHeatDeltaUI();
    elseif _valueType==3 then -- bloodvessels
        v = _node:getPrimaryDeltaUI();
    elseif _valueType==4 then -- shiver/persp
        v = _node:getSecondaryDeltaUI();
    elseif _valueType==5 then -- insulation
        v = _node:getInsulationUI();
    elseif _valueType==6 then -- body wetness
        v = _node:getBodyWetnessUI();
    elseif _valueType==7 then -- clothing wetness
        v = _node:getClothingWetnessUI();
    end
    return v;
end--]]

function ISClothingInsPanel:render()
    ISPanelJoypad.render(self);

    if not self.selectedBodyPart then
        self:drawRectBorder(130, 10, 265, self.coreRectangleH, 1.0, 0.5, 0.5, 0.5);
    else
        self:drawRectBorder(130, 10, 265, self.coreRectangleH, 1.0, 0.5, 0.5, 0.5);

        self:drawRect(self.bpAnchorX, self.bpPanelY+self.bpAnchorY-1, 130-self.bpAnchorX, 3, 1.0, 0.0, 0.0, 0.0);
        self:drawRect(self.bpAnchorX, self.bpPanelY+self.bpAnchorY, 130-self.bpAnchorX, 1, 1.0, 1.0, 1.0, 1.0);
    end

    self:setWidthAndParentWidth(400);
    self:setHeightAndParentHeight(self.toggleAdvBtn:getY()+self.toggleAdvBtn:getHeight()+10);
end

function ISClothingInsPanel:update()
    ISPanelJoypad.update(self);
end

function ISClothingInsPanel:onJoypadDown(button)
    if button == Joypad.BButton then
        getPlayerInfoPanel(self.playerNum):toggleView(xpSystemText.clothingIns);
        setJoypadFocus(self.playerNum, nil);
    end
    if button == Joypad.LBumper then
        getPlayerInfoPanel(self.playerNum):onJoypadDown(button);
    end
    if button == Joypad.RBumper then
        getPlayerInfoPanel(self.playerNum):onJoypadDown(button);
    end
end

function ISClothingInsPanel:new(player, x, y, width, height)
    local o = {};
    o = ISPanelJoypad:new(x, y, width, height);
    o:noBackground();
    setmetatable(o, self);
    self.__index = self;
    o.player = player;
    o.playerNum = player:getPlayerNum();
    o.refreshNeeded = true;
    o.bFemale = o.player:isFemale();
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.titleColor = {r=255/255, g=200/255, b=0/255, a=1.0};
    o.defTextColor = {r=0.5, g=0.5, b=0.5, a=1.0};
    o.btnTextColor = {r=1.0, g=1.0, b=1.0, a=1.0};
    o.selectedBodyPart = false;
    return o;
end


