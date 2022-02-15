--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

--[[
require "DebugUIs/DebugMenu/Base/ISDebugPanelBase";

---@class ISPlayerDebug : ISDebugPanelBase
ISPlayerDebug = ISDebugPanelBase:derive("ISPlayerDebug");
ISPlayerDebug.instance = nil;

function ISPlayerDebug.OnOpenPanel()
    return ISDebugPanelBase.OnOpenPanel(ISPlayerDebug, 100, 100, 800, 600, "GENERAL DEBUGGERS");
end

function ISPlayerDebug:new(x, y, width, height, title)
    local o = {};
    o = ISDebugPanelBase:new(x, y, width, height, title);
    setmetatable(o, self);
    self.__index = self;
    return o;
end

function ISPlayerDebug:initialise()
    ISPanel.initialise(self);
    self:registerPanel("Stats and Body",ISStatsAndBody);
end
--]]
-------------------------------------------------------------------------------------
--[[
function ISPlayerDebug.OnOpenPanel()
    if ISPlayerDebug.instance==nil then
        ISPlayerDebug.instance = ISPlayerDebug:new (100, 100, 800, 600, getPlayer());
        ISPlayerDebug.instance:initialise();
        ISPlayerDebug.instance:instantiate();
    end

    ISPlayerDebug.instance:addToUIManager();
    ISPlayerDebug.instance:setVisible(true);

    return ISPlayerDebug.instance;
end

function ISPlayerDebug:initialise()
    ISPanel.initialise(self);
end

function ISPlayerDebug:createChildren()
    ISPanel.createChildren(self);

    local x,y = 10, 10;
    local w,h = 180, 20;
    local margin = 10;

    local y, stats = ISDebugUtils.addButton(self,"statsbody",x,y,w,h,"Stats and Body",ISPlayerDebug.onClick);
    --local y, obj = self:addButton("weather",x,y+margin,w,h,"Weather");
    local y, obj = ISDebugUtils.addButton(self,"close",x,y+margin,w,h,"Close",ISPlayerDebug.onClick);

    x,y = 200, 10;
    w,h = self.width-210, self.height-20;

    self.panels = {};

    self:addPanel(x, y, w, h, ISStatsAndBody, stats);

    if self.panels[1] then
        self.panels[1]:setEnabled(true);
        self.panels[1]:setVisible(true);
    end
end

function ISPlayerDebug:addPanel(_x,_y,_w,_h,_panel,_button)
    local panel = _panel:new(_x,_y,_w,_h);
    panel.button = _button;
    panel.panelBase = _panel;
    panel:initialise();
    panel:instantiate();
    panel:setAnchorRight(true);
    panel:setAnchorLeft(true);
    panel:setAnchorTop(true);
    panel:setAnchorBottom(true);
    panel.moveWithMouse = true;
    panel.render = ISPlayerDebug.subPanelRender;
    panel.prerender = ISPlayerDebug.subPanelPreRender;
    panel:addScrollBars();
    panel.vscroll:setVisible(true);
    self:addChild(panel);
    panel:setScrollChildren(true);

    panel.onMouseWheel = ISDebugUtils.onMouseWheel;

    panel:setEnabled(false);
    panel:setVisible(false);
    table.insert(self.panels, panel);
end

function ISPlayerDebug:subPanelPreRender()
    self:setStencilRect(0,0,self:getWidth(),self:getHeight());
    self.panelBase.prerender(self);
end

function ISPlayerDebug:subPanelRender()
    ISPanel.render(self);
    self:clearStencilRect();
end


function ISPlayerDebug:onClick(_button)
    if _button.customData == "close" then
        self:close();
        return;
    end
    for k,v in ipairs(self.panels) do
        if v.button==_button then
            v:setEnabled(true);
            v:setVisible(true);
        else
            v:setEnabled(false);
            v:setVisible(false);
        end
    end
end

function ISPlayerDebug:update()
    ISPanel.update(self);
end

function ISPlayerDebug:close()
    self:setVisible(false);
    self:removeFromUIManager();
    ISPlayerDebug.instance = nil
end

function ISPlayerDebug:new(x, y, width, height)
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
    return o;
end
--]]
