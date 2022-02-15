--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************


require "ISUI/ISCollapsableWindow"

---@class ThunderDebug : ISCollapsableWindow
ThunderDebug = ISCollapsableWindow:derive("ThunderDebug");
ThunderDebug.instance = nil;
ThunderDebug.shiftDown = 0;
ThunderDebug.eventsAdded = false;

local enabled = true; --getDebug();

--[[
function ThunderDebug.OnKeepKeyDown(key)
    --backspace 13, shift 42, 54
    --print("KeyKeepDown = "..tostring(key));
    if key==42 or key==54 then
        ThunderDebug.shiftDown = 4;
    end
end

function ThunderDebug.OnKeyDown(key)
    --backspace 14, shift 42, 54
    --print("KeyDown = "..tostring(key));
    if ThunderDebug.shiftDown>0 and key ==12 then
        ThunderDebug.OnOpenPanel();
    end
end--]]

function ThunderDebug.OnOpenPanel()
    if ThunderDebug.instance==nil then
        ThunderDebug.instance = ThunderDebug:new (100, 100, 1000, 1000, getPlayer());
        ThunderDebug.instance:initialise();
        ThunderDebug.instance:instantiate();
    end

    ThunderDebug.instance:addToUIManager();
    ThunderDebug.instance:setVisible(true);

    if not ThunderDebug.eventsAdded then
        Events.OnClimateTickDebug.Add(ThunderDebug.OnClimateTickDebug);
        Events.OnThunderEvent.Add(ThunderDebug.OnThunderEvent);
        ThunderDebug.eventsAdded = true;
    end

    return ThunderDebug.instance;
end

function ThunderDebug:initialise()
    ISCollapsableWindow.initialise(self);
end


function ThunderDebug:createChildren()
    ISCollapsableWindow.createChildren(self);

    local y = self:titleBarHeight();

    self:addPoi("RoseWood",8214,11610);
    self:addPoi("March Ridge",10250,12700);
    self:addPoi("Muldraugh",10700,9950);
    self:addPoi("WestPoint",11560,6850);
    self:addPoi("The Mall",13950,5800);
    self:addPoi("Riverside",6300,5300);
    self:addPoi("Smalltown",7200,8300);

    self.labels = {};
    for k,v in ipairs(self.poi) do
        local lbl = ISLabel:new(v.x, v.y+15, 16, v.name, 0, 0.5, 0, 1.0, UIFont.Small, true);
        lbl.center = true;
        lbl:initialise();
        lbl:instantiate();
        self:addChild(lbl);
        table.insert(self.labels,lbl);
    end

    y = y+10;
    --self:setHeight(y);
end

function ThunderDebug:addPoi(_name, _x, _y)
    local x,y = self:convertCoords(_x, _y);
    table.insert(self.poi, {
        name = _name,
        x = x,
        y = y,
    });
end

function ThunderDebug:convertCoords(_x, _y)
    local x,y=1000-(_y/18.7),_x/18.7;
    return x,y;
end

function ThunderDebug:updateValues(_mgr)
    self.thunderStorm = _mgr:getThunderStorm();
end

function ThunderDebug.OnClimateTickDebug(mgr)
    if ThunderDebug.instance then
        ThunderDebug.instance:updateValues(mgr);
    end
end

function ThunderDebug:addThunderEvent(_x,_y,strike,light,rumble)
    local x,y = self:convertCoords(_x,_y);
    local filled = false;
    for k,v in ipairs(self.strikes) do
        if v.timer<=0 then
            v.x=x;
            v.y=y;
            v.strike = strike;
            v.lightning = light;
            v.rumble = rumble;
            v.timer = 1.0;
            filled = true;
            break;
        end
    end
    if not filled then
        table.insert(self.strikes, {
            x=x,
            y=y,
            strike = strike,
            lightning = light,
            rumble = rumble,
            timer = 1.0,
        });
    end
end

function ThunderDebug.OnThunderEvent(x,y,strike,light,rumble)
    if ThunderDebug.instance then
        ThunderDebug.instance:addThunderEvent(x,y,strike,light,rumble);
    end
end



function ThunderDebug:onResize()
    ISUIElement.onResize(self);
    local th = self:titleBarHeight();
end

function ThunderDebug:update()
    ISCollapsableWindow.update(self);

    if ThunderDebug.shiftDown>0 then
        ThunderDebug.shiftDown = ThunderDebug.shiftDown-1;
    end
end

function ThunderDebug:prerender()
    self:stayOnSplitScreen();
    ISCollapsableWindow.prerender(self);
end

function ThunderDebug:stayOnSplitScreen()
    ISUIElement.stayOnSplitScreen(self, self.playerNum)
end


function ThunderDebug:render()
    ISCollapsableWindow.render(self);

    if ThunderDebug.shiftDown>0 then
        ThunderDebug.shiftDown = ThunderDebug.shiftDown-1;
    end

    for k,v in ipairs(self.poi) do
        self:drawRect(v.x-5, v.y-5, 11, 11, 1.0, 0.0, 0.3, 0.0);
    end

    local px,py = self:convertCoords(getPlayer():getX(),getPlayer():getY());
    self:drawRect(px-2, py-2, 5, 5, 1.0, 1.0, 0.0, 0.0);

    if self.thunderStorm then
        for i=0,self.thunderStorm:getClouds():size()-1 do
            local cloud = self.thunderStorm:getClouds():get(i);
            if cloud:isRunning() then
                local x,y = self:convertCoords(cloud:getCurrentX(),cloud:getCurrentY());
                self:drawRect(x-3, y-3, 7, 7, 1.0, 0.6, 0.6, 1.0);

                x,y = self:convertCoords(cloud:getCurrentX()-(cloud:getRadius()/2),cloud:getCurrentY()-(cloud:getRadius()/2));
                self:drawRectBorder( x-(cloud:getRadius()/18.7), y, (cloud:getRadius()/18.7), (cloud:getRadius()/18.7), 1.0, 0.6, 0.6, 1.0);
            end
        end
    end

    for i=1,#self.strikes do
        local d = self.strikes[i];
        if d.timer > 0 then
            d.timer = d.timer-(1/(5*60));
            if d.lightning then
                self:drawRect(d.x-2, d.y-2, 4, 4, 1.0, d.timer, d.timer, d.timer);
            else
                self:drawRect(d.x-2, d.y-2, 4, 4, 1.0, 0, 0, d.timer);
            end
        end
    end
    --self.richtext:clearStencilRect();

    --[[
    local w,h = self:getWidth(), self:getHeight();

    local c = self.greyCol;
    local sx,sy = (w/2)-300, (h/2)-100;

    local interval = h/self.gridVertSpacing;
    for i = 1, self.gridVertSpacing-1 do
        self:drawRect(1, i*interval, w-2, 1, c.a, c.r, c.g, c.b);
    end
    interval = w/self.gridHorzSpacing;
    for i = 1, self.gridHorzSpacing-1 do
        self:drawRect(i*interval, 1, 1, h-2, c.a, c.r, c.g, c.b);
    end
    --]]
end


function ThunderDebug:close()
    ISCollapsableWindow.close(self)
    if JoypadState.players[self.playerNum+1] then
        setJoypadFocus(self.playerNum, nil)
    end
    ThunderDebug.instance = nil;
    self:removeFromUIManager();
    self:clear();
end

function ThunderDebug:clear()
    self.currentTile = nil;
end



function ThunderDebug:new (x, y, width, height, player)
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
    o.backgroundColor = {r=0, g=0, b=0, a=0.0};
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
    o.title = "Thunderbug";
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
    o.poi = {};
    o.strikes = {};
    ISDebugMenu.RegisterClass(self);
    return o
end

--[[
if enabled then
    Events.OnCustomUIKey.Add(ThunderDebug.OnKeyDown);
    Events.OnKeyKeepPressed.Add(ThunderDebug.OnKeepKeyDown);
    Events.OnClimateTickDebug.Add(ThunderDebug.OnClimateTickDebug);
    Events.OnThunderEvent.Add(ThunderDebug.OnThunderEvent);
    --Events.OnObjectLeftMouseButtonUp.Add(ThunderDebug.onMouseButtonUp);
end--]]
