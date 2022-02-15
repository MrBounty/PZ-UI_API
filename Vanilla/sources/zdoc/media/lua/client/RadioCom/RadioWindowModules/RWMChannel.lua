--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "RadioCom/RadioWindowModules/RWMPanel"

---@class RWMChannel : RWMPanel
RWMChannel = RWMPanel:derive("RWMChannel");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

function RWMChannel:initialise()
    ISPanel.initialise(self)
end

function RWMChannel:createChildren()
    self.editPresetPanel = RWMSubEditPreset:new (0, 0, self.width, 0, self, RWMChannel.onChildSave);
    self.editPresetPanel:initialise();
    self:addChild(self.editPresetPanel);

    local y = 0;
    self.comboBox = ISComboBox:new (10, 5, self.width-20, FONT_HGT_SMALL + 2 * 2, self, RWMChannel.comboChange)
    self.comboBox:initialise();
    self:addChild(self.comboBox);

    y = self.comboBox:getY() + self.comboBox:getHeight();

    local btnHgt = FONT_HGT_SMALL + 1 * 2

    self.tuneInButton = ISButton:new(10, y+5, (self.width-30)/2,btnHgt,getText("IGUI_RadioTuneIn"),self, RWMChannel.doTuneInButton);
    self.tuneInButton:initialise();
    self.tuneInButton.backgroundColor = {r=0, g=0, b=0, a=0.0};
    self.tuneInButton.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.tuneInButton.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self:addChild(self.tuneInButton);

    local x = self.tuneInButton:getX() + self.tuneInButton:getWidth();
    self.addPresetButton = ISButton:new(x+10, y+5, (self.width-30)/2,btnHgt,getText("IGUI_RadioAddPreset"),self, RWMChannel.doAddPresetButton);
    self.addPresetButton:initialise();
    self.addPresetButton.backgroundColor = {r=0, g=0, b=0, a=0.0};
    self.addPresetButton.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.addPresetButton.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self:addChild(self.addPresetButton);

    y = self.tuneInButton:getY() + self.tuneInButton:getHeight();

    self.editPresetButton = ISButton:new(10, y+5, (self.width-30)/2,btnHgt,getText("IGUI_RadioEditPreset"),self, RWMChannel.doEditPresetButton);
    self.editPresetButton:initialise();
    self.editPresetButton.backgroundColor = {r=0, g=0, b=0, a=0.0};
    self.editPresetButton.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.editPresetButton.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self:addChild(self.editPresetButton);

    x = self.editPresetButton:getX() + self.editPresetButton:getWidth();

    self.deletePresetButton = ISButton:new(x+10, y+5, (self.width-30)/2,btnHgt,getText("IGUI_RadioRemovePreset"),self, RWMChannel.doDeletePresetButton);
    self.deletePresetButton:initialise();
    self.deletePresetButton.backgroundColor = {r=0, g=0, b=0, a=0.0};
    self.deletePresetButton.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.deletePresetButton.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self:addChild(self.deletePresetButton);

    self:setPanelMode(false, true);
end

function RWMChannel:setPanelMode(_edit, _ignoreParent)
    self.editMode = _edit;

    for k,v in pairs(self.children) do
        if v ~= self.editPresetPanel then
            v:setVisible(not _edit);
        else
            v:setVisible(_edit);
        end
    end
    if _edit then
        self:setHeight( self.editPresetPanel:getHeight()); --a > b and a or b);
    else
        self:setHeight(self.editPresetButton:getY()+self.editPresetButton:getHeight()+5);
    end

    if self.parent and (not _ignoreParent) then
        self.parent:calculateHeights();
    end
end

function RWMChannel:round(num, idp)
    local mult = 10^(idp or 0);
    return math.floor(num * mult + 0.5) / mult;
end

function RWMChannel:onChildSave(_freq, _name)
    local index = self.comboBox.selected;
    if self.presets then
        if _name == nil or _name == "" then _name = "Unnamed"; end
        if self.presetMode == "add" then
            self.presets:add(PresetEntry.new(_name, _freq*self.frequencyDivider));
            index = self.presets:size();
        elseif self.presetMode == "edit" then
            if self.selectedPreset then
                self.selectedPreset:setName(_name);
                self.selectedPreset:setFrequency(_freq*self.frequencyDivider);
            end
        end
        if self.deviceData then
            self.deviceData:transmitPresets();
        end
    end
    self.presetMode = nil;
    self:readPresets(index);
    self:setPanelMode(false);
end

function RWMChannel:isValidPresets()
    if self.presets and self.comboBox.selected > 0 and self.comboBox.selected-1 < self.presets:size() then
        return true;
    end
end

function RWMChannel:comboChange()
    if self:isValidPresets() then
        self.selectedPreset = self.presets:get( self.comboBox.selected-1 );
    end
end

function RWMChannel:doTuneInButton()
    if self:isValidPresets() and self.player and self.device then
        local p = self.presets:get( self.comboBox.selected-1 );
        if self:doWalkTo() then
            ISTimedActionQueue.add(ISRadioAction:new("SetChannel",self.player, self.device, p:getFrequency() ));
        end
    end
end

function RWMChannel:doAddPresetButton()
    if self.presets and self.deviceData and self.deviceData:getDevicePresets() then
        if self.presets:size() < self.deviceData:getDevicePresets():getMaxPresets() then
            self.presetMode = "add";
            local half = (self.deviceData:getMaxChannelRange()-self.deviceData:getMinChannelRange())/2;
            self.editPresetPanel:setValues( "", half/self.frequencyDivider, self.deviceData:getMinChannelRange()/self.frequencyDivider, self.deviceData:getMaxChannelRange()/self.frequencyDivider, 0.2, 2 );
            self:setPanelMode(true);
        end
    end
end

function RWMChannel:doEditPresetButton()
    if self:isValidPresets() and self.deviceData then
        local p = self.presets:get( self.comboBox.selected-1 );
        self.selectedPreset = p;
        self.presetMode = "edit";
        self.editPresetPanel:setValues( p:getName(), p:getFrequency()/self.frequencyDivider, self.deviceData:getMinChannelRange()/self.frequencyDivider, self.deviceData:getMaxChannelRange()/self.frequencyDivider, 0.2, 2 );
        self:setPanelMode(true);
    end
end

function RWMChannel:doDeletePresetButton()
    if self:isValidPresets() then
        local p = self.presets:get( self.comboBox.selected-1 );
        self.presets:remove( p );
        self.comboBox.selected = self.comboBox.selected-1;
        if self.deviceData then
            self.deviceData:transmitPresets();
        end
    end
    self:readPresets(self.comboBox.selected);
end

function RWMChannel:addComboOption( _freq, _name )
    --print("Add option", _freq, _name, self.frequencyDivider);
    local f = tostring( self:round(_freq/self.frequencyDivider, 1) ) .. " MHz ";
    local s = f .. _name;
    if getTextManager():MeasureStringX(UIFont.Small, s) > self.comboBox:getWidth() then
        local ss = "";
        for i = 1, #s do
            ss = ss .. s:sub(i,i);
            if getTextManager():MeasureStringX(UIFont.Small, ss) > self.comboBox:getWidth() - 45 then
                ss = ss .. "...";
                break;
            end
        end
        s = ss;
    end
    self.comboBox:addOption(s);
end

function RWMChannel:readPresets( _selected )
    self.comboBox.options = {};
    self.comboBox.selected = 0;
    if self.deviceData and self.deviceData:getDevicePresets() and self.deviceData:getDevicePresets():getPresets() then
        self.presets = self.deviceData:getDevicePresets():getPresets();
        for i = 0, self.presets:size()-1 do
            local p = self.presets:get(i);
            self:addComboOption(p:getFrequency(), p:getName());
            if not _selected and p:getFrequency() == self.deviceData:getChannel() then
                self.comboBox.selected = i+1;
            end
        end
        if _selected and _selected > 0 and _selected <= self.presets:size() then
            self.comboBox.selected = _selected;
        end
    end
end

function RWMChannel:clear()
    RWMPanel.clear(self);
    self.presets = nil;
    self.comboBox.options = {};
    self.comboBox.selected = 0;
    self:setPanelMode(false);
end

function RWMChannel:readFromObject( _player, _deviceObject, _deviceData, _deviceType )
    if _deviceData:getIsTelevision() or _deviceData:isNoTransmit() then
        return false;
    end
    RWMPanel.readFromObject(self, _player, _deviceObject, _deviceData, _deviceType );
    self:readPresets();
    return true;
end

function RWMChannel:prerender()
    ISPanel.prerender(self);
end

function RWMChannel:update()
    ISPanel.update(self);

    if self.comboBox and self.comboBox.expanded == true and self.lastModeExpanded == false then
        local a,b = self.comboBox:getY()+self.comboBox:getHeight()+5, self.editPresetButton:getY()+self.editPresetButton:getHeight()+5;
        self:setHeight( a > b and a or b);
        --self:setHeight(self.comboBox:getY()+self.comboBox:getHeight()+5);
        if self.parent then
            self.parent:calculateHeights();
        end
        self.lastModeExpanded = true;
    elseif self.comboBox and self.comboBox.expanded == false and self.lastModeExpanded == true then
        self:setPanelMode(self.editMode);
        self.lastModeExpanded = false;
    end

    if self.deviceData and self.deviceData:getIsTurnedOn() then
        self.tuneInButton:setEnable(true);
        self.addPresetButton:setEnable(true);
        self.editPresetButton:setEnable(true);
        self.deletePresetButton:setEnable(true);
    else
        self.tuneInButton:setEnable(false);
        self.addPresetButton:setEnable(false);
        self.editPresetButton:setEnable(false);
        self.deletePresetButton:setEnable(false);
    end
end

function RWMChannel:render()
    ISPanel.render(self);
    if self.focusElement then
        local x,y,w,h = self.focusElement:getX(),self.focusElement:getY(),self.focusElement:getWidth(),self.focusElement:getHeight();
        self:drawRectBorder(x, y, w, h, 0.4, 0.2, 1.0, 1.0);
        self:drawRectBorder(x+1, y+1, w-2, h-2, 0.4, 0.2, 1.0, 1.0);
    end
end

function RWMChannel:setParent( _parent )
    self.parent = _parent;
end

function RWMChannel:getParent()
    return self.parent;
end

function RWMChannel:clearJoypadFocus(joypadData)
    if self.editMode then
        self.editPresetPanel:clearJoypadFocus(joypadData);
        self:setPanelMode(false);
    end
end

function RWMChannel:onJoypadDown(button)
    local overrideLB, overrideRB = false, false;
    if self.editMode then
        overrideLB, overrideRB = self.editPresetPanel:onJoypadDown(button);
    else
        if button == Joypad.AButton then
            if self.focusElement then --and self.focusElement.isCombobox then
                self.focusElement:forceClick();
                self.focusElement:setJoypadFocused(false);
                self.focusElement = nil;
            else
                if self.deviceData:getIsTurnedOn() then
                    self:doTuneInButton();
                end
            end
        elseif button == Joypad.BButton then
            if not self.focusElement and self.deviceData:getIsTurnedOn() then
                self:doEditPresetButton();
            end
        elseif button == Joypad.YButton then
            if not self.focusElement and self.deviceData:getIsTurnedOn() then
                self:doAddPresetButton();
            end
        elseif button == Joypad.XButton then
            if not self.focusElement and self.deviceData:getIsTurnedOn() then
                self:doDeletePresetButton();
            end
        elseif button == Joypad.RBumper then
            if self.focusElement == self.comboBox then
                self.focusElement:setJoypadFocused(false);
                self.focusElement = nil;
            else
                self.focusElement = self.comboBox;
                self.focusElement.expanded = true;
                self.focusElement:showPopup();
                self.focusElement:setJoypadFocused(true);
            end
            overrideRB = true;
        end
    end
    if button == Joypad.LBumper then
        if self.focusElement then
            self.focusElement:setJoypadFocused(false);
            self.focusElement = nil;
        end
    end
    return overrideLB, overrideRB; --overrides RBumper
end

function RWMChannel:getAPrompt()
    if self.editMode then
        return self.editPresetPanel:getAPrompt()
    else
        if self.focusElement then
            return getText("IGUI_RadioSelectChannel");
        elseif self.deviceData:getIsTurnedOn() then
            return getText("IGUI_RadioTuneIn");
        end
    end
    return nil;
end
function RWMChannel:getBPrompt()
    if self.editMode then
        return self.editPresetPanel:getBPrompt()
    else
        if self.focusElement then
        elseif self.deviceData:getIsTurnedOn() then
            return getText("IGUI_RadioEditPreset");
        end
    end
    return nil;
end
function RWMChannel:getXPrompt()
    if self.editMode then
        return self.editPresetPanel:getXPrompt()
    else
        if self.focusElement then
        elseif self.deviceData:getIsTurnedOn() then
            return getText("IGUI_RadioRemovePreset");
        end
    end
    return nil;
end
function RWMChannel:getYPrompt()
    if self.editMode then
        return self.editPresetPanel:getYPrompt()
    else
        if self.focusElement then
        elseif self.deviceData:getIsTurnedOn() then
            return getText("IGUI_RadioAddPreset");
        end
    end
    return nil;
end
function RWMChannel:getRBPrompt()
    if self.editMode then
        return getText("IGUI_RadioSelectOuter");
    else
        if self.focusElement == self.comboBox then
            return getText("IGUI_RadioDeselectChannelList");
        else
            return getText("IGUI_RadioSelectChannelList");
        end
    end
end


function RWMChannel:new (x, y, width, height )
    local o = RWMPanel:new(x, y, width, height);
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
    o.fontheight = getTextManager():MeasureStringY(UIFont.Small, "AbdfghijklpqtyZ")+2;
    o.editMode = false;
    o.frequencyDivider = 1000;
    o.parent = nil;
    o.lastModeExpanded = false;
    return o
end
