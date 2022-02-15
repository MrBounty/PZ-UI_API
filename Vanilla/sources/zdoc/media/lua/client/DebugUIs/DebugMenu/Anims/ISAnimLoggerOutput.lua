--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "DebugUIs/DebugMenu/Base/ISDebugSubPanelBase";

---@class ISAnimLoggerOutput : ISDebugSubPanelBase
ISAnimLoggerOutput = ISDebugSubPanelBase:derive("ISAnimLoggerOutput");

function ISAnimLoggerOutput:initialise()
    ISPanel.initialise(self);
end

function ISAnimLoggerOutput:createChildren()
    ISPanel.createChildren(self);
    local v, obj;

    local x,y,w = 1,1,self.width-12;

    --self:initHorzBars(x,w);

    self.richtext = ISRichTextPanel:new(x, y, w, self.height-2);
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



    self:setScrollHeight(y+10);

    self.init = false;
end

function ISAnimLoggerOutput:prerender()
    ISDebugSubPanelBase.prerender(self);
end

function ISAnimLoggerOutput:update()
    ISPanel.update(self);

    if self.monitor then
        if self.monitor:IsDirty() then
            self.richtext.text = self.monitor:getLogString();
            self.richtext:paginate();
            self:scrollToBottom();
        end
    end

end

function ISAnimLoggerOutput:clear()
    self.richtext.text = self.clearText;
    self.richtext:paginate();
    self:scrollToBottom();
end

function ISAnimLoggerOutput:setMonitor(_mon)
    self.monitor = _mon;
end

function ISAnimLoggerOutput:scrollToBottom()
    local yscroll = -(self.richtext:getScrollHeight() - (self.richtext:getScrollAreaHeight()));
    self.richtext:setYScroll( yscroll );
end

function ISAnimLoggerOutput:new(x, y, width, height, doStencil)
    local o = {};
    o = ISDebugSubPanelBase:new(x, y, width, height, doStencil);
    setmetatable(o, self);
    self.__index = self;
    return o;
end

