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

ISItemEditorUI = ISPanel:derive("ISItemEditorUI");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISItemEditorUI:initialise()
    ISPanel.initialise(self);
    self:create();
end


function ISItemEditorUI:setVisible(visible)
    --    self.parent:setVisible(visible);
    self.javaObject:setVisible(visible);
end

function ISItemEditorUI:render()
--    local y = 20;
--    local splitPt = 100;
--
--    self:drawText(getText("IGUI_ItemEditor_Title"), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, getText("IGUI_ItemEditor_Title")) / 2),y, 1,1,1,1, UIFont.Medium);
end

function ISItemEditorUI:prerender()
    ISPanel.prerender(self);
    local y = 20;
    local dy = self.dy
    local splitPt = 100;


    self:drawText(getText("IGUI_ItemEditor_Title"), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, getText("IGUI_ItemEditor_Title")) / 2),y, 1,1,1,1, UIFont.Medium);
    y = y + 30;

    self:drawText(getText("IGUI_ItemEditor_ItemType") .. ":", 5, y, 1,1,1,1,UIFont.Small);
    y = y + 30;

    self:drawText(getText("IGUI_Name") .. ":", 5, y, 1,1,1,1,UIFont.Small);
    self.name:setY(y);
    if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_Name")) + 10 then
        splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_Name")) + 10;
    end
    y = y + dy;

    self:drawText(getText("Tooltip_item_Weight") .. ":", 5, y, 1,1,1,1,UIFont.Small);
    self.weight:setY(y);
    if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("Tooltip_item_Weight")) + 10 then
        splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("Tooltip_item_Weight")) + 10;
    end
    y = y + dy;

    self:drawText(getText("IGUI_invpanel_Condition") .. ":", 5, y, 1,1,1,1,UIFont.Small);
    self.condition:setY(y);
    if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_invpanel_Condition")) + 10 then
        splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_invpanel_Condition")) + 10;
    end
    y = y + dy;

    if self.color:isVisible() then
        self:drawText(getText("IGUI_Color") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.color:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_Color")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_Color")) + 10;
        end
        y = y + dy;
    end

    if self.isWeapon then
        self:drawText(getText("IGUI_ItemEditor_MinDmg") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.minDmg:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_MinDmg")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_MinDmg")) + 10;
        end
        y = y + dy;

        self:drawText(getText("IGUI_ItemEditor_MaxDmg") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.maxDmg:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_MaxDmg")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_MaxDmg")) + 10;
        end
        y = y + dy;

        self:drawText(getText("IGUI_ItemEditor_MinAngle") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.minAngle:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_MinAngle")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_MinAngle")) + 10;
        end
        y = y + dy;

        self:drawText(getText("IGUI_ItemEditor_MinRange") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.minRange:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_MinRange")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_MinRange")) + 10;
        end
        y = y + dy;

        self:drawText(getText("IGUI_ItemEditor_MaxRange") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.maxRange:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_MaxRange")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_MaxRange")) + 10;
        end
        y = y + dy;

        self:drawText(getText("IGUI_ItemEditor_AimingTime") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.aimingTime:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_AimingTime")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_AimingTime")) + 10;
        end
        y = y + dy;

        self:drawText(getText("IGUI_ItemEditor_RecoilDelay") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.recoilDelay:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_RecoilDelay")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_RecoilDelay")) + 10;
        end
        y = y + dy;

        self:drawText(getText("IGUI_ItemEditor_ReloadTime") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.reloadTime:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_ReloadTime")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_ReloadTime")) + 10;
        end
        y = y + dy;

        self:drawText(getText("IGUI_ItemEditor_ClipSize") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.clipSize:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_ClipSize")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_ClipSize")) + 10;
        end
        y = y + dy;

        self.minDmg:setX(splitPt);
        self.maxDmg:setX(splitPt);
        self.minAngle:setX(splitPt);
        self.minRange:setX(splitPt);
        self.maxRange:setX(splitPt);
        self.reloadTime:setX(splitPt);
        self.recoilDelay:setX(splitPt);
        self.aimingTime:setX(splitPt);
        self.clipSize:setX(splitPt);
    end

    if self.isFood then
        self:drawText(getText("IGUI_ItemEditor_Age") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.age:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_Age")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_Age")) + 10;
        end
        y = y + dy;
        self:drawText(getText("Tooltip_food_Hunger") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.hunger:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("Tooltip_food_Hunger")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("Tooltip_food_Hunger")) + 10;
        end
        y = y + dy;
        self:drawText(getText("Tooltip_food_Unhappiness") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.unhappy:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("Tooltip_food_Unhappiness")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("Tooltip_food_Unhappiness")) + 10;
        end
        y = y + dy;
        self:drawText(getText("Tooltip_food_Boredom") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.boredom:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("Tooltip_food_Boredom")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("Tooltip_food_Boredom")) + 10;
        end
        y = y + dy;
        self:drawText(getText("IGUI_ItemEditor_PoisonPower") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.poisonPower:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_PoisonPower")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_PoisonPower")) + 10;
        end
        y = y + dy;
        self:drawText(getText("IGUI_ItemEditor_OffAge") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.offAge:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_OffAge")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_OffAge")) + 10;
        end
        y = y + dy;
        self:drawText(getText("IGUI_ItemEditor_OffAgeMax") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.offAgeMax:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_OffAgeMax")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_OffAgeMax")) + 10;
        end
        y = y + dy;
        self:drawText(getText("Tooltip_food_Calories") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.calories:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("Tooltip_food_Calories")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("Tooltip_food_Calories")) + 10;
        end
        y = y + dy;
        self:drawText(getText("Tooltip_food_Prots") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.proteins:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("Tooltip_food_Prots")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("Tooltip_food_Prots")) + 10;
        end
        y = y + dy;
        self:drawText(getText("Tooltip_food_Fat") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.lipids:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("Tooltip_food_Fat")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("Tooltip_food_Fat")) + 10;
        end
        y = y + dy;
        self:drawText(getText("Tooltip_food_Carbs") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.carbs:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("Tooltip_food_Carbs")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("Tooltip_food_Carbs")) + 10;
        end
        y = y + dy;

        self.age:setX(splitPt);
        self.hunger:setX(splitPt);
        self.unhappy:setX(splitPt);
        self.boredom:setX(splitPt);
        self.poisonPower:setX(splitPt);
        self.offAge:setX(splitPt);
        self.offAgeMax:setX(splitPt);
        self.calories:setX(splitPt);
        self.proteins:setX(splitPt);
        self.lipids:setX(splitPt);
        self.carbs:setX(splitPt);
    end
    if self.isDrainable then
        self:drawText(getText("IGUI_ItemEditor_UsedDelta") .. ":", 5, y, 1,1,1,1,UIFont.Small);
        self.usedDelta:setY(y);
        if splitPt < getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_UsedDelta")) + 10 then
            splitPt = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_ItemEditor_UsedDelta")) + 10;
        end
        y = y + dy;

        self.usedDelta:setX(splitPt);
    end

    self.name:setX(splitPt);
    self.weight:setX(splitPt);
    self.condition:setX(splitPt);
    self.color:setX(splitPt);
    self:drawText(self.item:getFullType(), splitPt, 50, 1,1,1,1,UIFont.Small);
end

function ISItemEditorUI:create()
    local btnWid = 150
    local btnHgt = FONT_HGT_SMALL + 2 * 4
    local entryHgt = FONT_HGT_SMALL + 2 * 2
    local padBottom = 10
    local numberWidth = 50;
    local dy = math.max(20, entryHgt)
    self.dy = dy

    local y = 30;

    self.name = ISTextEntryBox:new(self.item:getName(), 10, y, 150, entryHgt);
    self.name:initialise();
    self.name:instantiate();
    self:addChild(self.name);

    self.weight = ISTextEntryBox:new(luautils.round(self.item:getActualWeight(),3) .. "", 10, y, numberWidth, entryHgt);
    self.originalWeight = luautils.round(self.item:getActualWeight(),3);
    self.weight:initialise();
    self.weight:instantiate();
    self.weight.min = 0;
    self.weight:setOnlyNumbers(true);
    self:addChild(self.weight);
    y = y + dy;

    self.condition = ISTextEntryBox:new(self.item:getCondition() .. "", 10, y, numberWidth, entryHgt);
    self.condition:initialise();
    self.condition:instantiate();
    self.condition.min = 0;
    self.condition.max = self.item:getConditionMax();
    self.condition:setOnlyNumbers(true);
    self:addChild(self.condition);
    y = y + dy;

    self.color = ISButton:new(10, y, 18, 18, "", self, ISItemEditorUI.onColor);
    self.originalColorR = self.item:getColorInfo():getR();
    self.originalColorG = self.item:getColorInfo():getG();
    self.originalColorB = self.item:getColorInfo():getB();
    self.originalColorA = self.item:getColorInfo():getA();
    self.color:initialise();
    self.color.backgroundColor = {r = self.item:getColorInfo():getR(), g = self.item:getColorInfo():getG(), b = self.item:getColorInfo():getB(), a = 1};
    self:addChild(self.color);
    self.colorBtn = self.color;
    y = y + dy;

    if not self.item:allowRandomTint() then
        self.color:setVisible(false);
        y = y - dy;
    end

    --************** WEAPON **************--
    if self.isWeapon then
        self.minDmg = ISTextEntryBox:new(luautils.round(self.item:getMinDamage(),3) .. "", 10, y, numberWidth, entryHgt);
        self.minDmg:initialise();
        self.minDmg:instantiate();
        self.minDmg.min = 0;
        self.minDmg:setOnlyNumbers(true);
        self:addChild(self.minDmg);
        y = y + dy;

        self.maxDmg = ISTextEntryBox:new(luautils.round(self.item:getMaxDamage(),3) .. "", 10, y, numberWidth, entryHgt);
        self.maxDmg:initialise();
        self.maxDmg:instantiate();
        self.maxDmg.min = 0;
        self.maxDmg:setOnlyNumbers(true);
        self:addChild(self.maxDmg);
        y = y + dy;

        self.minAngle = ISTextEntryBox:new(luautils.round(self.item:getMinAngle(),3) .. "", 10, y, numberWidth, entryHgt);
        self.minAngle:initialise();
        self.minAngle:instantiate();
        self.minAngle.min = 0;
        self.minAngle:setOnlyNumbers(true);
        self:addChild(self.minAngle);
        y = y + dy;

        local minRangeNbr = luautils.round(self.item:getMinRange(),3);
        if self.item:isRanged() then
            minRangeNbr = luautils.round(self.item:getMinRangeRanged(),3);
        end
        self.minRange = ISTextEntryBox:new(minRangeNbr .. "", 10, y, numberWidth, entryHgt);
        self.minRange:initialise();
        self.minRange:instantiate();
        self.minRange.min = 0;
        self.minRange:setOnlyNumbers(true);
        self:addChild(self.minRange);
        y = y + dy;

        self.maxRange = ISTextEntryBox:new(luautils.round(self.item:getMaxRange(),3) .. "", 10, y, numberWidth, entryHgt);
        self.maxRange:initialise();
        self.maxRange:instantiate();
        self.maxRange.min = 0;
        self.maxRange:setOnlyNumbers(true);
        self:addChild(self.maxRange);
        y = y + dy;

        self.aimingTime = ISTextEntryBox:new(luautils.round(self.item:getAimingTime(),3) .. "", 10, y, numberWidth, entryHgt);
        self.aimingTime:initialise();
        self.aimingTime:instantiate();
        self.aimingTime.min = 0;
        self.aimingTime:setOnlyNumbers(true);
        self.aimingTime:setEditable(self.item:isRanged())
        self:addChild(self.aimingTime);
        y = y + dy;

        self.recoilDelay = ISTextEntryBox:new(luautils.round(self.item:getRecoilDelay(),3) .. "", 10, y, numberWidth, entryHgt);
        self.recoilDelay:initialise();
        self.recoilDelay:instantiate();
        self.recoilDelay.min = 0;
        self.recoilDelay:setOnlyNumbers(true);
        self.recoilDelay:setEditable(self.item:isRanged())
        self:addChild(self.recoilDelay);
        y = y + dy;

        self.reloadTime = ISTextEntryBox:new(luautils.round(self.item:getReloadTime(),3) .. "", 10, y, numberWidth, entryHgt);
        self.reloadTime:initialise();
        self.reloadTime:instantiate();
        self.reloadTime.min = 0;
        self.reloadTime:setOnlyNumbers(true);
        self.reloadTime:setEditable(self.item:isRanged())
        self:addChild(self.reloadTime);
        y = y + dy;

        self.clipSize = ISTextEntryBox:new(self.item:getClipSize() .. "", 10, y, numberWidth, entryHgt);
        self.clipSize:initialise();
        self.clipSize:instantiate();
        self.clipSize.min = 1;
        self.clipSize:setOnlyNumbers(true);
        self.clipSize:setEditable(self.item:isRanged())
        self:addChild(self.clipSize);
        y = y + dy;
    end

    --************** FOOD **************--
    if self.isFood then
        self.age = ISTextEntryBox:new(luautils.round(self.item:getAge(),3) .. "", 10, y, numberWidth, entryHgt);
        self.age:initialise();
        self.age:instantiate();
        self.age.min = 0;
        self.age:setOnlyNumbers(true);
        self:addChild(self.age);
        y = y + dy;

        self.hunger = ISTextEntryBox:new(luautils.round(self.item:getBaseHunger(),3) .. "", 10, y, numberWidth, entryHgt);
        self.hunger:initialise();
        self.hunger:instantiate();
        self.hunger:setOnlyNumbers(true);
        self:addChild(self.hunger);
        y = y + dy;

        self.unhappy = ISTextEntryBox:new(self.item:getUnhappyChange() .. "", 10, y, numberWidth, entryHgt);
        self.unhappy:initialise();
        self.unhappy:instantiate();
        self.unhappy.min = 0;
        self.unhappy:setOnlyNumbers(true);
        self:addChild(self.unhappy);
        y = y + dy;

        self.boredom = ISTextEntryBox:new(self.item:getBoredomChange() .. "", 10, y, numberWidth, entryHgt);
        self.boredom:initialise();
        self.boredom:instantiate();
        self.boredom.min = 0;
        self.boredom:setOnlyNumbers(true);
        self:addChild(self.boredom);
        y = y + dy;

        self.poisonPower = ISTextEntryBox:new(self.item:getPoisonPower() .. "", 10, y, numberWidth, entryHgt);
        self.poisonPower:initialise();
        self.poisonPower:instantiate();
        self.poisonPower.min = 0;
        self.poisonPower:setOnlyNumbers(true);
        self:addChild(self.poisonPower);
        y = y + dy;

        self.offAge = ISTextEntryBox:new(self.item:getOffAge() .. "", 10, y, numberWidth, entryHgt);
        self.offAge:initialise();
        self.offAge:instantiate();
        self.offAge.min = 0;
        self.offAge:setOnlyNumbers(true);
        self:addChild(self.offAge);
        y = y + dy;

        self.offAgeMax = ISTextEntryBox:new(self.item:getOffAgeMax() .. "", 10, y, numberWidth, entryHgt);
        self.offAgeMax:initialise();
        self.offAgeMax:instantiate();
        self.offAgeMax.min = 0;
        self.offAgeMax:setOnlyNumbers(true);
        self:addChild(self.offAgeMax);
        y = y + dy;

        self.calories = ISTextEntryBox:new(self.item:getCalories() .. "", 10, y, numberWidth, entryHgt);
        self.calories:initialise();
        self.calories:instantiate();
        self.calories.min = 0;
        self.calories:setOnlyNumbers(true);
        self:addChild(self.calories);
        y = y + dy;

        self.proteins = ISTextEntryBox:new(self.item:getProteins() .. "", 10, y, numberWidth, entryHgt);
        self.proteins:initialise();
        self.proteins:instantiate();
        self.proteins.min = 0;
        self.proteins:setOnlyNumbers(true);
        self:addChild(self.proteins);
        y = y + dy;

        self.lipids = ISTextEntryBox:new(self.item:getLipids() .. "", 10, y, numberWidth, entryHgt);
        self.lipids:initialise();
        self.lipids:instantiate();
        self.lipids.min = 0;
        self.lipids:setOnlyNumbers(true);
        self:addChild(self.lipids);
        y = y + dy;

        self.carbs = ISTextEntryBox:new(self.item:getCarbohydrates() .. "", 10, y, numberWidth, entryHgt);
        self.carbs:initialise();
        self.carbs:instantiate();
        self.carbs.min = 0;
        self.carbs:setOnlyNumbers(true);
        self:addChild(self.carbs);
        y = y + dy;
    end
    if self.isDrainable then
        self.usedDelta = ISTextEntryBox:new(luautils.round(self.item:getUsedDelta(),3) .. "", 10, y, numberWidth, entryHgt);
        self.usedDelta.tooltip = getText("IGUI_ItemEditor_UsedDeltaTooltip");
        self.usedDelta:initialise();
        self.usedDelta:instantiate();
        self.usedDelta.min = 0;
        self.usedDelta.max = 1;
        self.usedDelta:setOnlyNumbers(true);
        self:addChild(self.usedDelta);
        y = y + dy;
    end

    self:setHeight(y + 150);

    self.save = ISButton:new(5, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("IGUI_RadioSave"), self, ISItemEditorUI.onOptionMouseDown);
    self.save.internal = "SAVE";
    self.save:initialise();
    self.save:instantiate();
    self.save.borderColor = self.buttonBorderColor;
    self:addChild(self.save);

    self.cancel = ISButton:new(self:getWidth() - btnWid - 5, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("IGUI_Exit"), self, ISItemEditorUI.onOptionMouseDown);
    self.cancel.internal = "CANCEL";
    self.cancel:initialise();
    self.cancel:instantiate();
    self.cancel.borderColor = self.buttonBorderColor;
    self:addChild(self.cancel);

    self.colorPicker = ISColorPicker:new(0, 0)
    self.colorPicker:initialise()
    self.colorPicker.pickedTarget = self;
    self.colorPicker.resetFocusTo = self;
    self.colorPicker:setInitialColor(self.item:getColorInfo());


    self:updateButtons(self);
end

function ISItemEditorUI:updateButtons(internalUI)
--    local ui = internalUI or self.parent;
--    ui.save.enable = ui.originalName ~= string.trim(ui.name:getInternalText()) or
--            (ui.originalWeight ~= luautils.round(string.trim(ui.weight:getInternalText()),3)) or
--            (ui.originalColorR ~= ui.color.backgroundColor.r or ui.originalColorG ~= ui.color.backgroundColor.g or ui.originalColorB ~= ui.color.backgroundColor.b) or
--            (ui.isWeapon and (ui.originalMinDmg ~= luautils.round(ui.minDmg:getInternalText(),3) or ui.originalMaxDmg ~= luautils.round(ui.maxDmg:getInternalText(),3)
--                    or ui.originalMinAngle ~= luautils.round(ui.minAngle:getInternalText(),3) or ui.originalAimingTime ~= luautils.round(ui.aimingTime:getInternalText(),3)
--                    or ui.originalMinRange ~= luautils.round(ui.minRange:getInternalText(),3) or ui.originalMaxRange ~= luautils.round(ui.maxRange:getInternalText(),3)
--                    or ui.originalRecoilDelay ~= luautils.round(ui.recoilDelay:getInternalText(),3) or ui.originalReloadTime ~= luautils.round(ui.reloadTime:getInternalText(),3))
--            )

end

function ISItemEditorUI:onOptionMouseDown(button, x, y)
    if button.internal == "SAVE" then
        self.item:setName(string.trim(self.name:getInternalText()));
        if self.originalWeight ~= string.trim(self.weight:getInternalText()) then
            self.item:setActualWeight(tonumber(string.trim(self.weight:getInternalText())));
            self.item:setCustomWeight(true);
        end
        self.item:setCondition(tonumber(string.trim(self.condition:getInternalText())));
        if self.originalColorR ~= self.color.backgroundColor.r or self.originalColorG ~= self.color.backgroundColor.g or self.originalColorB ~= self.color.backgroundColor.b then
            local color = Color.new(self.color.backgroundColor.r, self.color.backgroundColor.g, self.color.backgroundColor.b,1);
            self.item:setColor(color);
            self.item:getVisual():setTint(ImmutableColor.new(color));
            if self.admin:isEquipped(self.item) then
                self.admin:resetModelNextFrame();
            end
            self.item:setCustomColor(true);
        end
        if self.isWeapon then
            self.item:setMinDamage(tonumber(string.trim(self.minDmg:getInternalText())));
            self.item:setMaxDamage(tonumber(string.trim(self.maxDmg:getInternalText())));
            self.item:setMinAngle(tonumber(string.trim(self.minAngle:getInternalText())));
            if self.item:isRanged() then
                self.item:setMinRangeRanged(tonumber(string.trim(self.minRange:getInternalText())));
            else
                self.item:setMinRange(tonumber(string.trim(self.minRange:getInternalText())));
            end
            self.item:setMaxRange(tonumber(string.trim(self.maxRange:getInternalText())));
            self.item:setAimingTime(tonumber(string.trim(self.aimingTime:getInternalText())));
            self.item:setRecoilDelay(tonumber(string.trim(self.recoilDelay:getInternalText())));
            self.item:setReloadTime(tonumber(string.trim(self.reloadTime:getInternalText())));
            self.item:setClipSize(tonumber(string.trim(self.clipSize:getInternalText())));
        end
        if self.isFood then
           self.item:setAge(tonumber(string.trim(self.age:getInternalText())));
           self.item:setBaseHunger(tonumber(string.trim(self.hunger:getInternalText())));
           self.item:setHungChange(self.item:getBaseHunger());
           self.item:setUnhappyChange(tonumber(string.trim(self.unhappy:getInternalText())));
           self.item:setBoredomChange(tonumber(string.trim(self.boredom:getInternalText())));
           self.item:setPoisonPower(tonumber(string.trim(self.poisonPower:getInternalText())));
           self.item:setOffAge(tonumber(string.trim(self.offAge:getInternalText())));
           self.item:setOffAgeMax(tonumber(string.trim(self.offAgeMax:getInternalText())));
           self.item:setCalories(tonumber(string.trim(self.calories:getInternalText())));
           self.item:setLipids(tonumber(string.trim(self.lipids:getInternalText())));
           self.item:setProteins(tonumber(string.trim(self.proteins:getInternalText())));
           self.item:setCarbohydrates(tonumber(string.trim(self.carbs:getInternalText())));
        end
        if self.isDrainable then
            self.item:setUsedDelta(tonumber(string.trim(self.usedDelta:getInternalText())));
        end
        self:setVisible(false);
        self:removeFromUIManager();
    end
    if button.internal == "CANCEL" then
        self:setVisible(false);
        self:removeFromUIManager();
    end
end

function ISItemEditorUI:onColor(button)
    self.colorPicker:setX(button:getAbsoluteX());
    self.colorPicker:setY(button:getAbsoluteY() + button:getHeight());
    self.colorPicker.pickedFunc = ISItemEditorUI.onPickedColor;
    self.colorPicker:setInitialColor(self.item:getColorInfo());
    self.colorPicker:addToUIManager()
end

function ISItemEditorUI:onPickedColor(color, mouseUp)
    self.color.backgroundColor = { r=color.r, g=color.g, b=color.b, a = 1 }
    self:updateButtons(self);
end

function ISItemEditorUI:new(x, y, width, height, admin, item)
    local o = {};
    x = getMouseX() + 10;
    y = getMouseY() + 10;
    o = ISPanel:new(x, y, 400, height);
    setmetatable(o, self);
    self.__index = self;
    o.variableColor={r=0.9, g=0.55, b=0.1, a=1};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.buttonBorderColor = {r=0.7, g=0.7, b=0.7, a=0.5};
    o.zOffsetSmallFont = 25;
    o.moveWithMouse = true;
    o.admin = admin;
    o.item = item;
    ISItemEditorUI.instance = self;
    o.isWeapon = instanceof(item, "HandWeapon");
    o.isFood = instanceof(item, "Food");
    o.isDrainable = instanceof(item, "DrainableComboItem");

    return o;
end
