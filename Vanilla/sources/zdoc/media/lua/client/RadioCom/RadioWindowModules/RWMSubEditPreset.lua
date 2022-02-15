--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "ISUI/ISPanel"

---@class RWMSubEditPreset : ISPanel
RWMSubEditPreset = ISPanel:derive("RWMSubEditPreset");

function RWMSubEditPreset:initialise()
    ISPanel.initialise(self)
end

function RWMSubEditPreset:createChildren()
    self.lineSplit = getTextManager():MeasureStringX(UIFont.Small, "1000.0 MHz");

    self.entryName = ISTextEntryBox:new("", 0, 5, self.width, 18);
    self.entryName:initialise();
    self.entryName:instantiate();
    self.entryName:setText("");
    self:addChild(self.entryName);

    self.frequencySlider = ISSliderPanel:new(0, 5, self.width/4, 18, self, RWMSubEditPreset.onSliderChange );
    self.frequencySlider:initialise();
    self.frequencySlider:instantiate();
    self:addChild(self.frequencySlider);

    self.saveButton = ISButton:new(0, 0, self.width,18,getText("IGUI_RadioSave"),self, RWMSubEditPreset.doSaveButton);
    self.saveButton:initialise();
    self.saveButton.backgroundColor = {r=0, g=0, b=0, a=0.0};
    self.saveButton.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.saveButton.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self:addChild(self.saveButton);

    self:addLinePair( getText("IGUI_Name"), self.entryName );
    self:addLinePair( "1000.0 MHz", self.frequencySlider );
    self:addLinePair( nil, self.saveButton );
    self:calcLinePairs();

end

function RWMSubEditPreset:onSliderChange( value )
    for i=1,#self.linePairs do
        local line = self.linePairs[i];
        if line and line.ui and line.ui == self.frequencySlider then
            line.text = tostring(value) .. " MHz";
        end
    end
end

function RWMSubEditPreset:setValues( name, freq, min, max, step, shift )
    self.entryName:setText(name);
    self.frequencySlider:setValues(min, max, step, shift);
    self.frequencySlider:setCurrentValue(freq);
end

function RWMSubEditPreset:prerender()
    ISPanel.prerender(self);
end

function RWMSubEditPreset:doSaveButton()
    if self.target and self.onSave then
        self.onSave(self.target, self.frequencySlider:getCurrentValue(), self.entryName:getText() );
    end
end

function RWMSubEditPreset:addLinePair( _text, _UIelement )
    local linePair = {};
    if _text then
        linePair.text = _text;
        linePair.textLen = getTextManager():MeasureStringX(UIFont.Small, _text);
    end
    linePair.ui = _UIelement;
    table.insert(self.linePairs, linePair);
end

function RWMSubEditPreset:calcLinePairs()
    local ii = 0;
    self.lineSplit = 0;
    for i=1,#self.linePairs do
        local line = self.linePairs[i];
        if line.textLen and line.textLen > self.lineSplit then
            self.lineSplit = line.textLen;
        end
    end
    self.lineSplit = self.lineSplit + 10;

    for i=1,#self.linePairs do
        local line = self.linePairs[i];
        ii = i-1;
        if line and line.ui then
            if line.text then
                line.ui:setX(self.lineSplit+self.linePadding);
                line.ui:setY(self.linePadding+(ii*(self.linePadding+self.lineHeight)));
                line.ui:setWidth(self.width - self.lineSplit - self.linePadding - 10);
                line.ui:setHeight(self.lineHeight);
            else
                line.ui:setX(self.width/4);
                line.ui:setY(self.linePadding+(ii*(self.linePadding+self.lineHeight)));
                line.ui:setWidth(self.width/2);
                line.ui:setHeight(self.lineHeight);
            end
            if line.ui.paginate then
                line.ui:paginate();
            end
        end
    end
    self:setHeight( self.linePadding+(#self.linePairs*(self.linePadding+self.lineHeight)) );
end

function RWMSubEditPreset:render()
    ISPanel.render(self);
    for i=1,#self.linePairs do
        local line = self.linePairs[i];
        local ii = i-1;
        if line and line.ui then
            if line.text then
                self:drawTextRight(line.text, self.lineSplit, self.linePadding+(ii*(self.linePadding+self.lineHeight)), 1,1,1,1, UIFont.Small);
            end
        end
    end
end

function RWMSubEditPreset:clearJoypadFocus(joypadData)
end

function RWMSubEditPreset:onJoypadDown(button)
    local overrideLB, overrideRB = false,false;
    if button == Joypad.AButton then
        local step = self.joypadSteps[self.joypadStepIndex];
        self.frequencySlider:setCurrentValue( self.frequencySlider:getCurrentValue()+step );
    elseif button == Joypad.BButton then
        local step = self.joypadSteps[self.joypadStepIndex];
        self.frequencySlider:setCurrentValue( self.frequencySlider:getCurrentValue()-step );
    elseif button == Joypad.YButton then
        self:doSaveButton();
    elseif button == Joypad.XButton then
        self.joypadStepIndex = self.joypadStepIndex + 1;
        if self.joypadStepIndex > #self.joypadSteps then
            self.joypadStepIndex = 1;
        end
    end
    return overrideLB, overrideRB;
end

function RWMSubEditPreset:getAPrompt()
    return getText("IGUI_RadioIncFrequency");
end
function RWMSubEditPreset:getBPrompt()
    return getText("IGUI_RadioDecFrequency");
end
function RWMSubEditPreset:getXPrompt()
    return getText("IGUI_RadioToggleStep", tostring(self.joypadSteps[self.joypadStepIndex])); --"Toggle step size, (current: "..tostring(self.joypadSteps[self.joypadStepIndex]).." MHz)";
end
function RWMSubEditPreset:getYPrompt()
    return getText("IGUI_RadioSaveChanges");
end


function RWMSubEditPreset:new (x, y, width, height, target, onSave)
    local o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.background = true;
    o.backgroundColor = {r=0, g=0, b=0, a=0.0};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.linePairs = {};
    o.lineSplit = width/2;
    o.linePadding = 5;
    o.lineHeight = 18;
    o.target = target;
    o.onSave = onSave;
    o.joypadSteps = {0.2,2,5,25};
    o.joypadStepIndex = 1;
    --o.fontheight = getTextManager():MeasureStringY(UIFont.Small, "AbdfghijklpqtyZ")+2;
    return o
end
