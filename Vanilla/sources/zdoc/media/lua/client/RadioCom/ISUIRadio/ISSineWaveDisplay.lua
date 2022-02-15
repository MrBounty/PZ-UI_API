--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "ISUI/ISPanel"

---@class ISSineWaveDisplay : ISPanel
ISSineWaveDisplay = ISPanel:derive("ISSineWaveDisplay");

function ISSineWaveDisplay:initialise()
    ISPanel.initialise(self)
end

function ISSineWaveDisplay:createChildren()
end

function ISSineWaveDisplay:update()
    ISPanel.update(self);

    local isPaused = UIManager.getSpeedControls() and UIManager.getSpeedControls():getCurrentGameSpeed() == 0
    if isPaused then return end

    if self.isOn then
        local p,w,h = self.waveParams, self:getWidth(), self:getHeight();
        -- sine wave
        local ticks = UIManager.getSecondsSinceLastUpdate() * 30
        self.waveCntr = self.waveCntr + ticks
        if (not self.wave) or self.waveCntr >= self.waveUpdInt or self.hasChanged then
            local height = ZombRand((h/2)*p.minH,(h/2)*p.maxH);
            self.wave = self:getWaveData(ZombRand(100*p.minLen,100*p.maxLen),-height,height);
            self.waveUpdInt = ZombRand(p.minSpeed,p.maxSpeed);
            self.offsetUpdInt = ZombRand(p.minUpd,p.maxUpd);
            self.waveCntr = 0;
            self.hasChanged = false;
        end

        self.offsetCntr = self.offsetCntr + ticks;
        if self.offsetCntr >= self.offsetUpdInt then
            self.offsetCntr = 0;
            self.offset = self.offset + 1;
            if self.offset >= #self.wave-1 then self.offset = 0; end
        end
    end
end

function ISSineWaveDisplay:prerender()
    ISPanel.prerender(self);
end

function ISSineWaveDisplay:toggleOn( _b )
    self.isOn = _b;
end

function ISSineWaveDisplay:render()
    ISPanel.render(self);
    local p,w,h = self.waveParams, self:getWidth(), self:getHeight();

    -- grid lines
    if self.doGridLines then
        local c = self.isOn and self.gridColor or self.greyCol;
        local interval = h/self.gridVertSpacing;
        for i = 1, self.gridVertSpacing-1 do
            self:drawRect(1, i*interval, w-2, 1, c.a, c.r, c.g, c.b);
        end
        interval = w/self.gridHorzSpacing;
        for i = 1, self.gridHorzSpacing-1 do
            self:drawRect(i*interval, 1, 1, h-2, c.a, c.r, c.g, c.b);
        end
    end

    -- sine wave
    --[[
    self.waveCntr= self.waveCntr + 1;
    if (not self.wave) or self.waveCntr >= self.waveUpdInt or self.hasChanged then
        local height = ZombRand((h/2)*p.minH,(h/2)*p.maxH);
        self.wave = self:getWaveData(ZombRand(100*p.minLen,100*p.maxLen),-height,height);
        self.waveUpdInt = ZombRand(p.minSpeed,p.maxSpeed);
        self.offsetUpdInt = ZombRand(p.minUpd,p.maxUpd);
        self.waveCntr = 0;
        self.hasChanged = false;
    end

    self.offsetCntr = self.offsetCntr + 1;
    if self.offsetCntr >= self.offsetUpdInt then
        self.offsetCntr = 0;
        self.offset = self.offset + 1;
        if self.offset >= #self.wave-1 then self.offset = 0; end
    end
    --]]

    -- It's possible update() hasn't been called yet.
    if not self.wave then return end

    if self.isOn then
        local mean = (h/2);
        for i = 0, w-1 do
            local index = ((i+self.offset) % (#self.wave-1))+1;
            if self.wave[index] then
                self:drawRect(i, mean+self.wave[index], 1, 1, 1.0, 0.0, 1.0, 0.0);
            else
                print("Missing index: "..tostring(index).." max indexes: "..tostring(#self.wave));
            end
        end
    end
end

function ISSineWaveDisplay:setWaveParameters(_minLen, _maxLen,_minHeight, _maxHeight, _minUpdFreq, _maxUpdFreq, _minScrollSpeed, _maxScrollSpeed)
    self.waveParams.minLen = _minLen;
    self.waveParams.maxLen = _maxLen;
    self.waveParams.minH = _minHeight;
    self.waveParams.maxH = _maxHeight;
    self.waveParams.minUpd = _minUpdFreq;
    self.waveParams.maxUpd = _maxUpdFreq;
    self.waveParams.minSpeed = _minScrollSpeed;
    self.waveParams.maxSpeed = _maxScrollSpeed;
    self.hasChanged = true;
end

local function clerp( _t, _a, _b )
    local t2 = (1-math.cos(_t*math.pi))/2;
    return(_a*(1-t2)+_b*t2);
end

function ISSineWaveDisplay:getWaveData(_len,_minH,_maxH)
    local tab = {};
    local flip = false;
    for i = 1, _len do
        if(i>_len/2) then flip = true; end
        local ii = flip and i-(_len/2) or i;
        table.insert(tab, clerp(ii*(1/(_len/2)),flip and _maxH or _minH, flip and _minH or _maxH));
    end
    return tab;
end


function ISSineWaveDisplay:new (x, y, width, height)
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
    o.waveParams = { minLen = 0.2, maxLen = 0.6, minHeight = 0.1, maxHeight = 1.0, minUpd = 1, maxUpd = 10, minSpeed = 1, maxSpeed = 10 };

    o.offsetCntr = 0;
    o.offsetUpdInt = 4;
    o.offset = 0;
    o.waveCntr = 0;
    o.waveUpdInt = 30;
    o.hasChanged = false;
    o.isOn = true;
    return o
end
