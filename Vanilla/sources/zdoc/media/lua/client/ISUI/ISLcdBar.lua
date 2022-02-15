--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "ISUI/ISPanel"

---@class ISLcdBar : ISPanel
ISLcdBar = ISPanel:derive("ISLcdBar");

ISLcdBar.charW = 8;
ISLcdBar.charH = 12;
ISLcdBar.indexes = " !\"#$%&'()*+,-./0123456789:;<=>?@abcdefghijklmnopqrstuvwxyz[\\]^_";
ISLcdBar.unsupported = { "CH", "CN", "JP", "KO", "RU", "TH", "UA"};
ISLcdBar.special = {"[","%","("} --these characters need a '%' appended in string.find

function ISLcdBar:initialise()
    ISPanel.initialise(self)

    local lang = Translator.getLanguage():name();

    -- unsupported languages mode is not implemented
    -- instead the device panel (which uses this LCD bar) always passes the English language strings
    -- and sets self.textMode to false.
    for k,v in ipairs(ISLcdBar.unsupported) do
        if lang==v then
            self.textMode = true;
        end
    end
end

function ISLcdBar:createChildren()
end

function ISLcdBar:update()
    ISPanel.update(self);

    local isPaused = UIManager.getSpeedControls() and UIManager.getSpeedControls():getCurrentGameSpeed() == 0
    if isPaused then return end

    if self.isOn and self.doScroll then
        local ticks = UIManager.getSecondsSinceLastUpdate();
        self.posCounter = self.posCounter+ticks;

        if self.posCounter>0.35 then
            self.posCounter = 0;
            self.pos = self.pos + 1;
            if (not self.text) or self.pos>self.text:len()-1 then
                self.pos = 0;
            end
        end
    end
end

function ISLcdBar:prerender()
    ISPanel.prerender(self);
end

function ISLcdBar:toggleOn( _b )
    if self.isOn~=_b then
        self.isOn = _b;
        self.pos = 0;
    end
end

function ISLcdBar:renderChar( _pos, _index, _r, _g, _b, _a )
    if self.javaObject ~= nil then
        local yoffset = 0;
        if _index>=32 then
            _index = _index-32;
            yoffset = ISLcdBar.charH;
        end
        local ind = _index*ISLcdBar.charW;
        local pos = _pos*ISLcdBar.charW;
        self.javaObject:DrawSubTextureRGBA(self.lcdfont,
                ind, yoffset, ISLcdBar.charW, ISLcdBar.charH,
                pos+2, 2, ISLcdBar.charW, ISLcdBar.charH,
                _r, _g, _b, _a);
    end
end

function ISLcdBar:isSpecial( _char )
    for i=1,#ISLcdBar.special do
        if ISLcdBar.special[i]==_char then
            return true;
        end
    end
    return false;
end

function ISLcdBar:printChar( _pos, _char )
    local index = 0;
    if self:isSpecial(_char) then
        index = string.find(ISLcdBar.indexes, "%".._char:lower());
    else
        index = string.find(ISLcdBar.indexes, _char:lower());
    end
    if _char=="." then
        index = 15;
    end
    self:renderChar(_pos, index and index-1 or 0, self.ledTextColor.r, self.ledTextColor.g, self.ledTextColor.b, self.ledTextColor.a);
end

function ISLcdBar:render()
    ISPanel.render(self);

    if self.isOn then
        self:renderBackground(self.ledColor.r, self.ledColor.g, self.ledColor.b, self.ledColor.a)
    else
        self:renderBackground(self.ledColor.r, self.ledColor.g, self.ledColor.b, 0.5)
    end

    if self.isOn then
        if self.text then
            for i=0, self.lcdwidth-1 do
                local p = self.pos+i;
                if p>self.text:len()-1 then
                    p = p-self.text:len();
                end
                self:printChar(i, self.text:sub(p+1,p+1))
            end
        end
    end
end

function ISLcdBar:renderBackground(_r, _g, _b, _a)
    for i=0, self.lcdwidth-1 do
        local pos = i*ISLcdBar.charW;
        --(texture, x, y, w, h, a, r, g, b)
        self:drawTextureScaled(self.lcdback, pos+2, 2, ISLcdBar.charW, ISLcdBar.charH, _a, _r, _g, _b);
    end
end

function ISLcdBar:setDoScroll(_b)
    self.doScroll = _b;
    if not self.doScroll then
        self.pos = 0;
    end
end

function ISLcdBar:setTextMode(_b)
    self.textMode = _b;
end

function ISLcdBar:setText(_text)
    if self.textCache~=_text then
        self.textCache = _text;
        self.text = _text;
        self.pos = 0;

        if self.textMode then
            return;
        end

        if self.text then
            -- this reverts accents to ascii letter as the LCD font doesnt support the former
            self.text = RecordedMedia.toAscii(self.text);
        end
        if self.text and self.text:len()<self.lcdwidth then
            for i=0,self.lcdwidth-self.text:len() do
                self.text = self.text.." ";
            end
        end
        --print("Lcd text set to '"..tostring(self.text).."'");
        --print("indexes len = "..tostring(#ISLcdBar.indexes))
    end
end

function ISLcdBar:new (x, y, charWidth)
    local w = (charWidth*ISLcdBar.charW)+4;
    local h = ISLcdBar.charH+4;
    local o = ISPanel:new(x, y, w, h);
    setmetatable(o, self)
    self.__index = self
    o.charW = ISLcdBar.charW;
    o.charH = ISLcdBar.charH;
    o.x = x;
    o.y = y;
    o.background = true;
    o.backgroundColor = {r=0, g=0, b=0, a=1.0};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.gridColor = {r=0.0, g=0.3, b=0.0, a=1};
    o.greyCol = { r=0.4,g=0.4,b=0.4,a=1};
    o.width = w;
    o.height = h;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;

    o.ledColor = { r=1, g=1, b=1 , a=1};
    o.ledTextColor = { r=0, g=0, b=0, a=1 };
    o.lcdwidth = charWidth;
    o.isOn = true;
    o.lcdfont = getTexture("media/ui/LCD_Display/LCDfont.png");
    o.lcdback = getTexture("media/ui/LCD_Display/LCDfont_background_greyscale.png");
    o.pos = 0;
    o.posCounter = 0;
    o.doScroll = false;
    return o
end
