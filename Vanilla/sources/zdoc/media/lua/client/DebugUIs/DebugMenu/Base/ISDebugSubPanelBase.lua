--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "ISUI/ISPanel"

---@class ISDebugSubPanelBase : ISPanel
ISDebugSubPanelBase = ISPanel:derive("ISDebugSubPanelBase");

function ISDebugSubPanelBase:initHorzBars(_x, _width)
    ISDebugUtils.initHorzBars(self,_x,_width);
end

function ISDebugSubPanelBase:prerender()
    if self.doStencilRender then
        self:setStencilRect(0,0,self:getWidth(),self:getHeight());
    end
    ISPanel.prerender(self);

    ISDebugUtils.renderHorzBars(self);
end

function ISDebugSubPanelBase:render()
    ISPanel.render(self);
    if self.doStencilRender then
        self:clearStencilRect();
    end
end

function ISDebugSubPanelBase:close()
end

function ISDebugSubPanelBase:new(x, y, width, height, doStencil)
    local o = {};
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self);
    self.__index = self;
    o.variableColor={r=0.9, g=0.55, b=0.1, a=1};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.buttonBorderColor = {r=0.7, g=0.7, b=0.7, a=0.5};
    o.doStencilRender = doStencil;
    return o;
end

