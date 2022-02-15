--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "ISUI/ISPanel"

---@class FloatArrayPlotter : ISPanel
FloatArrayPlotter = ISPanel:derive("FloatArrayPlotter");

function FloatArrayPlotter:initialise()
    ISPanel.initialise(self);
    self.plotColor = {r=0,g=1,b=0,a=1};
end

function FloatArrayPlotter:createChildren()
end

function FloatArrayPlotter:update()
    ISPanel.update(self);
end

function FloatArrayPlotter:prerender()
    ISPanel.prerender(self);
end

function FloatArrayPlotter:render()
    ISPanel.render(self);

    local w = self:getWidth();
    local h = self:getHeight();

    local c;

    for i=1,#self.horzBars do
        c = self.horzBars[i].col;
        self:drawRect(1, h*self.horzBars[i].val, w-2, 1, c.a, c.r, c.g, c.b);
    end

    if self.data then
        local c = self.plotColor;
        local w = w-2;
        local last = false;
        local dy, dh;
        for i=0,self.data:size() do
            local val = self.data:get(i);
            dy = self.height-(val*self.height);
            dh = 1;
            if last and i>=1 then
                if last>val then
                    dy = self.height-(last*self.height);
                    dh = (last*self.height)-(val*self.height);
                elseif last<val then
                    dh = (val*self.height)-(last*self.height);
                end
                if dh<1 then dh=1; end
            end
            self:drawRect(w, dy, 1, dh, c.a, c.r, c.g, c.b);
            w = w-1;
            last = val;
            if w<=1 then break; end
        end
    end
end

function FloatArrayPlotter:setData(_data)
    self.data = _data;
end


--add horizontal line
function FloatArrayPlotter:setHorzLine(value,col)
    table.insert(self.horzBars,{val=value,col=col});
end


function FloatArrayPlotter:new (x, y, width, height, maxPlotPoints)
    local o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.background = true;
    o.backgroundColor = {r=0, g=0, b=0, a=1.0};
    o.borderColor = {r=0.8, g=0.8, b=0.8, a=1};
    o.gridColor = {r=0.0, g=0.3, b=0.0, a=1};
    o.greyCol = { r=0.4,g=0.4,b=0.4,a=1};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;

    o.doGridLines = true;
    o.gridHorzSpacing = 20;
    o.gridVertSpacing = 2;
    o.maxPlotPoints = width;
    o.indexPointer = 1;
    o.vertBars = {};
    o.horzBars = {};
    return o
end
