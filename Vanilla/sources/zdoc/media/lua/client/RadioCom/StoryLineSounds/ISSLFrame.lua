--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "ISUI/ISPanel"

---@class ISSLFrame : ISPanel
ISSLFrame = ISPanel:derive("ISSLFrame");

function round(num, idp)
    local mult = 10^(idp or 0)
    return math.floor(num * mult + 0.5) / mult
end

local function clerp( _t, _a, _b )
    local t2 = (1-math.cos(_t*math.pi))/2;
    return(_a*(1-t2)+_b*t2);
end

function ISSLFrame:initialise()
    ISPanel.initialise(self)
end

function ISSLFrame:createChildren()

    --test data
    local test = {};
    table.insert(test,{t=0,i=0});
    table.insert(test,{t=0.4,i=0.7});
    table.insert(test,{t=1,i=0});
    self:addGridData( "test", Color.new(1,1,1,1), test );

    --self:setStoryEvent(getSLSoundManager():getStorySoundEvent("warzone"));
    self:setStoryEvent(nil);

    self:updateGridRectangle();
    self:updateVisualGrid();
end

function ISSLFrame:isDragging()
    return self.dragInside;
end

function ISSLFrame:onMouseDown(x, y)
    self.dragInside = true;
end

--function ISSLFrame:onMouseDownOutside(x, y)
--self.dragInside = false;
--end

function ISSLFrame:onMouseUpOutside(x, y)
    self:onMouseUp(self:getMouseX(),y);
end
--function ISSLFrame:onMouseUp(x, y)
--self.dragInside = false;
--end

function ISSLFrame:onMouseMove(x, y)
    if not self:getIsVisible() then
        return;
    end
    local r = self.gridRectangle;
    if x>=r.x and x<=r.x2 and y>r.y and y<r.y2 then
    end
end

function ISSLFrame:onMouseMoveOutside(x, y)
    --self.hoverVolume = -1;
    self:onMouseMove(x,y);
end

function ISSLFrame:update()
    ISPanel.update(self);
end

function ISSLFrame:prerender()
    ISPanel.prerender(self);
end

function ISSLFrame:render()
    ISPanel.render(self);
    -- grid
    --local p = self.gridPadding;
    --local gx,gy,gw,gh = x+p.left,y+p.top,w-p.left-p.right,h-p.top-p.bot;
    local r = self.gridRectangle;
    local c = self.borderColor;
    self:drawRect(r.x, r.y, r.w, r.h, c.a, c.r, c.g, c.b);
    if self.doGridLines then
        local c = self.gridColor;
        local interval = r.h/self.gridVertSpacing;
        for i = 0, self.gridVertSpacing do
            local s = tostring(round(1.0-((i*interval)/r.h),1));
            local iy = r.y+(i*interval);
            --local iw = (gy+1) + (gh-2);
            if i~=0 and i~=self.gridVertSpacing then
                self:drawRect(r.x+1, iy, r.w-2, 1, c.a, c.r, c.g, c.b);
            end

            local sw = getTextManager():MeasureStringX(UIFont.Small, s);
            local sh = getTextManager():MeasureStringY(UIFont.Small, s)/2;
            --self:drawText(s, gx-sw-2, iy, 1,1,1,1, UIFont.Small);
            self:drawText(s, r.x-sw-2, iy-sh, 1,1,1,1, UIFont.Small);
            --self:drawText(self.distanceText, x, self.cacheHeight, 1,1,1,1, UIFont.Small);
        end
        interval = r.w/self.gridHorzSpacing;
        for i = 0, self.gridHorzSpacing do
            local s = tostring(round((i*interval)/r.w,1));
            local ix = r.x+(i*interval);
            local ih = (r.y+1) + (r.h-2);

            if i~=0 and i~=self.gridHorzSpacing then
                self:drawRect(ix, r.y+1, 1, r.h-2, c.a, c.r, c.g, c.b);
            end

            local sw = getTextManager():MeasureStringX(UIFont.Small, s)/2;
            --self:drawText(s, ix, ih+2, 1,1,1,1, UIFont.Small);
            self:drawText(s, ix-sw, ih+2, 1,1,1,1, UIFont.Small);
        end
    end

    for i=0,r.w do
        local x = r.x+i;
        self:drawGridData(x,r.y,i/r.w);
    end

    self:drawLinePoints();

end

function ISSLFrame:drawLinePoints()
    local mouseOver = false;
    for k,v in ipairs(self.eventData) do
        if v.dataPoints and v.eventSound then
            for i=0, v.dataPoints:size()-1 do
                local dataPoint     = v.dataPoints:get(i);
                local x,y = self:dataToGrid(dataPoint:getTime(),dataPoint:getIntensity());

                local i1,o1,i2,o2 = 1,3,2,5;
                if mouseOver==false and self:getMouseX()>=x-8 and self:getMouseX()<=x+8 and self:getMouseY()>=y-8 and self:getMouseY()<=y+8 then
                    mouseOver = true;
                    i1,o1,i2,o2 = 3,7,4,9;
                end

                self:drawRect(x-i1, y-i1, o1, o1, 1.0, 1.0, 1.0, 1.0);

                self:drawRect(x-i2, y-i2, o2, 1, 1.0, 0.0, 0.0, 0.0);
                self:drawRect(x-i2, y-i2, 1, o2, 1.0, 0.0, 0.0, 0.0);
                self:drawRect(x+i2, y-i2, 1, o2, 1.0, 0.0, 0.0, 0.0);
                self:drawRect(x-i2, y+i2, o2, 1, 1.0, 0.0, 0.0, 0.0);
            end
        end
    end
end

function ISSLFrame:drawGridData( _x, _y, _t )
    for k,v in ipairs(self.eventData) do
        if v.dataPoints and v.eventSound then
            local c = v.eventSound:getColor();

            for i=0, v.dataPoints:size()-1 do
                local dataPoint     = v.dataPoints:get(i);
                local next 		    = i+1<v.dataPoints:size() and v.dataPoints:get(i+1) or nil;

                if dataPoint and next then
                    if _t>=dataPoint:getTime() and _t<next:getTime() then
                        local t = (_t-dataPoint:getTime())/(next:getTime()-dataPoint:getTime());
                        local intens = clerp(t,dataPoint:getIntensity(),next:getIntensity());
                        local _,y = self:dataToGrid(0,intens);
                        self:drawRect(_x, y, 1, 1, 1.0, c:getRed()/255, c:getGreen()/255, c:getBlue()/255);
                        --self:drawRect(_x, y, 1, 1, 1.0, 1.0, 1.0, 1.0);
                        break;
                    end
                end

            end

        end
    end
end

function ISSLFrame:drawGridDataold( _x, _y, _t )
    for k,v in ipairs(self.eventData) do
        if v.dataPoints and v.eventSound then
            local c = v.eventSound:getColor();
            for i=0, v.dataPoints:size()-1 do
                if i+1<v.dataPoints:size()-1 then
                    local dataPoint     = v.dataPoints:get(i);
                    local next 		    = v.dataPoints:get(i+1);

                    if dataPoint:getTime()>=_t then
                        local t = (_t-dataPoint:getTime())/(next:getTime()-dataPoint:getTime());
                        local intens = clerp(t,dataPoint:getIntensity(),next:getIntensity());
                        local _,y = self:dataToGrid(0,intens);
                        --self:drawRect(_x, y, 1, 1, 1.0, c:getRed()/255, c:getGreen()/255, c:getBlue()/255);
                        self:drawRect(_x, y, 1, 1, 1.0, 1.0, 1.0, 1.0);
                        break;
                    end
                end
            end
        end
    end
end

function ISSLFrame:updateVisualGrid()
    local x = 0;
end

function ISSLFrame:updateGridRectangle()
    local x,y,w,h = 0, 0, self:getWidth(), self:getHeight();
    local p = self.gridPadding;
    local rect = {};
    rect.x = x+p.left;
    rect.y = y+p.top;
    rect.w = w-p.left-p.right;
    rect.h = h-p.top-p.bot;
    rect.x2 = rect.x+w;
    rect.y2 = rect.y+h;
    self.gridRectangle = rect;
    return rect;
end

--intensity/time to grid x,y
function ISSLFrame:dataToGrid( _t, _i )
    local r = self.gridRectangle;
    local x = r.x + (r.w*_t);
    local y = r.y + (r.h*(1-_i)); --local y = r.y + (1-(r.h*_i));
    return x,y;
end
function ISSLFrame:gridToData( _x, _y ) --FIXME make sure input are local coordinates (for mouse input related functionallity)
    local r = self.gridRectangle;
    if _x<r.x then _x=r.x end
    if _x>r.x+r.w then _x=r.x+r.w end
    if _y<r.y then _y=r.y end
    if _y>r.y+r.h then _y=r.y+r.h end
    local t = (_x-r.x)/r.w;
    local i = 1-((_y-r.y)/r.h);
    return t,i
end

function ISSLFrame:setStoryEvent( _event )
    self.storyEvent = _event;
    self.eventData = {};
    if self.storyEvent~=nil then
        local eventSounds = self.storyEvent:getEventSounds();
        if eventSounds~=nil and eventSounds:size()>0 then
            for i=0, eventSounds:size()-1 do
                local eSound 		= eventSounds:get(i);
                local dataPoints    = eSound:getDataPoints();

                table.insert(self.eventData, {eventSound=eSound, dataPoints= dataPoints});

                print(eSound:getColor():getRed().." "..eSound:getColor():getGreen().." "..eSound:getColor():getBlue());
                print(eSound:getName());
                for i=0, dataPoints:size()-1 do
                    local data = dataPoints:get(i);
                    print("time "..tostring(data:getTime()).." intensity "..tostring(data:getIntensity()))
                end
            end
        end
    end
end


function ISSLFrame:addGridData( _name, _col, _t )
    local gridData = {};
    gridData.name = _name;
    gridData.color = _col;
    gridData.dataPoints = _t;
    --[[
    gridData.gridPoints = {};
    for k,v in ipairs(_t) do
        local x,y = self:dataToGrid( v.t, v.i);
        table.insert(gridData.gridPoints, {x=x, y=y});
    end
    --]]
    table.insert(self.gridData,gridData);
end

function ISSLFrame:onResize()
    ISUIElement.onResize(self);
    self:updateGridRectangle();
    self:updateVisualGrid();
end


function ISSLFrame:new (x, y, width, height)
    local o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.background = true;
    o.backgroundColor = {r=0, g=0, b=0, a=1};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;

    o.doGridLines = true;
    o.gridHorzSpacing = 8;
    o.gridVertSpacing = 4;
    o.gridPadding = {top=10,bot=20,left=20,right=10};
    o.gridColor = {r=0.2, g=0.2, b=0.2, a=1};
    o.visualGrid = {};

    o.gridData = {};

    o.fontheight = getTextManager():MeasureStringY(UIFont.Small, "AbdfghijklpqtyZ")+2;
    return o
end

