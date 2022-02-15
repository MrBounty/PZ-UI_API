--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 21/09/16
-- Time: 10:19
-- To change this template use File | Settings | File Templates.
--

--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "ISUI/ISPanel"

ISServerOptionsChange = ISPanel:derive("ISServerOptionsChange");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISServerOptionsChange:initialise()
    ISPanel.initialise(self);
    self:create();
end


function ISServerOptionsChange:setVisible(visible)
    --    self.parent:setVisible(visible);
    self.javaObject:setVisible(visible);
end

function ISServerOptionsChange:render()
    local z = 20;

    self:drawText(getText("IGUI_PlayerStats_ChangeServerOptionTitle"), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, getText("IGUI_PlayerStats_ChangeServerOptionTitle")) / 2), z, 1,1,1,1, UIFont.Medium);
    z = z + 50;
    self:drawText(self.option:getName(), 10, z, 1,1,1,1, UIFont.Small);

    if self.errorTxt then
        self:drawText(self.errorTxt, 10, 100, 1,0,0,1, UIFont.Small);
    end

    self:updateButtons();
end

function ISServerOptionsChange:create()
    local btnWid = 100
    local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local padBottom = 10

    local y = 70;

    if instanceof(self.option, "BooleanConfigOption") then
        self:setWidth(400);
        local comboWid = getTextManager():MeasureStringX(UIFont.Small, "false") + 30
        self.booleanOption = ISComboBox:new(getTextManager():MeasureStringX(UIFont.Small, self.option:getName()) + 20, y, comboWid, FONT_HGT_SMALL + 2 * 2, nil,nil);
        self.booleanOption:initialise();
        self.booleanOption:addOption("true");
        self.booleanOption:addOption("false");
        self.booleanOption.parent = self;
        self.defaultBool = 1;
        if not self.option:getValue() then
            self.booleanOption.selected = 2;
            self.defaultBool = 2;
        end
        self:addChild(self.booleanOption);
    end

    if instanceof(self.option, "DoubleConfigOption") or instanceof(self.option, "IntegerConfigOption") or instanceof(self.option, "StringConfigOption") then
        local size = getTextManager():MeasureStringX(UIFont.Small, self.option:getValueAsString()) + 20;
        if size > (self:getWidth() - (getTextManager():MeasureStringX(UIFont.Small, self.option:getName() ) + 30)) then
            size = (self:getWidth() - (getTextManager():MeasureStringX(UIFont.Small, self.option:getName()) + 30));
        end
        self.entry = ISTextEntryBox:new(self.option:getValueAsString(), getTextManager():MeasureStringX(UIFont.Small, self.option:getName()) + 20, y, size, FONT_HGT_SMALL + 2 * 2);
        self.entry.font = UIFont.Small
        self.entry:initialise();
        self.entry:instantiate();
        if instanceof(self.option, "DoubleConfigOption") or instanceof(self.option, "IntegerConfigOption") then
            self.entry:setOnlyNumbers(true);
            self.entry.min = self.option:getMin();
            self.entry.max = self.option:getMax();
        end
        self:addChild(self.entry);
        self.defaultText = self.option:getValueAsString();
    end

    self.saveBtn = ISButton:new(10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_characreation_BuildSave"), self, ISServerOptionsChange.onOptionMouseDown);
    self.saveBtn.internal = "SAVE";
    self.saveBtn:initialise();
    self.saveBtn:instantiate();
    self.saveBtn.borderColor = self.buttonBorderColor;
    self.saveBtn.enable = false;
    self:addChild(self.saveBtn);

    self.cancel = ISButton:new(self:getWidth() - btnWid - 10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Cancel"), self, ISServerOptionsChange.onOptionMouseDown);
    self.cancel.internal = "CANCEL";
    self.cancel:initialise();
    self.cancel:instantiate();
    self.cancel.borderColor = self.buttonBorderColor;
    self:addChild(self.cancel);

    self.resetBtn = ISButton:new(self.cancel.x - btnWid - 5, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("IGUI_PlayerStats_ResetToDefault"), self, ISServerOptionsChange.onOptionMouseDown);
    self.resetBtn.internal = "RESET";
    self.resetBtn:initialise();
    self.resetBtn:instantiate();
    self.resetBtn.borderColor = self.buttonBorderColor;
    self.resetBtn:setX(self.cancel.x - 10 - self.resetBtn.width)
    self:addChild(self.resetBtn);

end

function ISServerOptionsChange:updateButtons()
    self.saveBtn.enable = false;
    self.resetBtn.enable = false;
    if self.entry then
        if self.entry:getText() ~= self.defaultText then
            self.saveBtn.enable = true;
        end
        if self.entry:getText() ~= self.option:getDefaultValue() then
            self.resetBtn.enable = true;
        end
    end
    if self.booleanOption then
        if self.booleanOption.selected ~= self.defaultBool then
            self.saveBtn.enable = true;
        end

        if (not self.option:getDefaultValue() and self.booleanOption.selected == 1) or (self.option:getDefaultValue() and self.booleanOption.selected == 2) then
            self.resetBtn.enable = true;
        end
    end
end

function ISServerOptionsChange:onOptionMouseDown(button, x, y)

    if button.internal == "SAVE" then
        self.errorTxt = nil;
        if self.onclick ~= nil then
            if self.booleanOption then
                local newValue = "false";
                if self.booleanOption.selected == 1 then
                    newValue = "true";
                end
                self.onclick(self.target, button.parent.option, newValue);
            end
            if self.entry then
                if self.entry.min then
                    if tonumber(self.entry:getText()) < self.entry.min then
                        self.errorTxt = "The minimum value for this option is " .. self.entry.min;
                        return;
                    end
                end
                if self.entry.max then
                    if tonumber(self.entry:getText()) > self.entry.max then
                        self.errorTxt = "The maximum value for this option is " .. self.entry.max;
                        return;
                    end
                end
                self.onclick(self.target, button.parent.option, self.entry:getText());
            end
            self:setVisible(false);
            self:removeFromUIManager();
        end
    end
    if button.internal == "RESET" then
        if self.booleanOption then
            if not self.option:getDefaultValue() then
                self.booleanOption.selected = 2;
            else
                self.booleanOption.selected = 1;
            end
        end
        if self.entry then
            self.entry:setText(tostring(button.parent.option:getDefaultValue()));
        end
    end
    if button.internal == "CANCEL" then
        self:setVisible(false);
        self:removeFromUIManager();
    end

    self.target.modifying = false;
end

function ISServerOptionsChange:new(x, y, width, height, target, onclick, option)
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
    o.target = target;
    o.onclick = onclick;
    o.option = option;
    o.defaultBool = 1;
    o.defaultText = "";

    return o;
end
