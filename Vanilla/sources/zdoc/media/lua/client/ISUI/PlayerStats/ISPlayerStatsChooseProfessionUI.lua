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

---@class ISPlayerStatsChooseProfessionUI : ISPanel
ISPlayerStatsChooseProfessionUI = ISPanel:derive("ISPlayerStatsChooseProfessionUI");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISPlayerStatsChooseProfessionUI:initialise()
    ISPanel.initialise(self);
    self:create();
end


function ISPlayerStatsChooseProfessionUI:setVisible(visible)
    --    self.parent:setVisible(visible);
    self.javaObject:setVisible(visible);
end

function ISPlayerStatsChooseProfessionUI:render()
    local z = 20;

    self:drawText(getText("IGUI_PlayerStats_ChangeProfTitle"), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, getText("IGUI_PlayerStats_ChangeProfTitle")) / 2), z, 1,1,1,1, UIFont.Medium);
end

function ISPlayerStatsChooseProfessionUI:create()
    for i=0,ProfessionFactory.getProfessions():size()-1 do
        local prof = ProfessionFactory.getProfessions():get(i);
--        if self.chr:getDescriptor():getProfession() and prof:getType() ~= self.chr:getDescriptor():getProfession() then
            table.insert(self.comboList, prof);
--        end
    end

    self.combo = ISComboBox:new(10, 20 + FONT_HGT_MEDIUM + 20, 160, FONT_HGT_SMALL + 3 * 2, nil,nil);
    self.combo:initialise();
    self:addChild(self.combo);

    self:populateComboList();

    local btnWid = 100
    local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local padBottom = 10

    self.ok = ISButton:new((self:getWidth() / 2) - 100 - 5, self.combo:getBottom() + 20, btnWid, btnHgt, getText("UI_Ok"), self, ISPlayerStatsChooseProfessionUI.onOptionMouseDown);
    self.ok.internal = "OK";
    self.ok:initialise();
    self.ok:instantiate();
    self.ok.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.ok);

    self.cancel = ISButton:new((self:getWidth() / 2) + 5, self.combo:getBottom() + 20, btnWid, btnHgt, getText("UI_Cancel"), self, ISPlayerStatsChooseProfessionUI.onOptionMouseDown);
    self.cancel.internal = "CANCEL";
    self.cancel:initialise();
    self.cancel:instantiate();
    self.cancel.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.cancel);

    self:setHeight(self.ok:getBottom() + padBottom)
end

function ISPlayerStatsChooseProfessionUI:populateComboList()
    self.combo:clear();
    local tooltipMap = {};
    for _,v in ipairs(self.comboList) do
        self.combo:addOption(v:getLabel());
        if v:getType() == self.chr:getDescriptor():getProfession() then
           self.combo.selected = _;
        end
        tooltipMap[v:getLabel()] = v:getDescription();
    end
    self.combo:setToolTipMap(tooltipMap);
    self.combo:setWidthToOptions()
end

function ISPlayerStatsChooseProfessionUI:onOptionMouseDown(button, x, y)
    if button.internal == "OK" then
        self:setVisible(false);
        self:removeFromUIManager();
        if self.onclick ~= nil then
            self.onclick(self.target, button, self.comboList[self.combo.selected]);
        end
    end
    if button.internal == "CANCEL" then
        self:setVisible(false);
        self:removeFromUIManager();
    end
end

function ISPlayerStatsChooseProfessionUI:new(x, y, width, height, target, onclick, player)
    local o = {};
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self);
    self.__index = self;
    o.variableColor={r=0.9, g=0.55, b=0.1, a=1};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.target = target;
    o.onclick = onclick;
    o.chr = player;
    o.comboList = {};
    o.zOffsetSmallFont = 25;
    o.moveWithMouse = true;
    return o;
end
