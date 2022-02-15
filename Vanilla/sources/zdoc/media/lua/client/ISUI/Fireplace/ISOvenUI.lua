require "ISUI/ISPanelJoypad"
--***********************************************************
--**              	  ROBERT JOHNSON                       **
--***********************************************************

---@class ISOvenUI : ISPanelJoypad
ISOvenUI = ISPanelJoypad:derive("ISOvenUI");
ISOvenUI.messages = {};

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

--************************************************************************--
--** ISOvenUI:initialise
--**
--************************************************************************--

function ISOvenUI:initialise()
    ISPanel.initialise(self);
    local btnWid = 100
    local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local padBottom = 10

    self.tempKnob = ISKnob:new(20,40,self.knobTex, getTexture("media/ui/Knobs/KnobBGFarhenOvenTemp.png"), getText("IGUI_Temperature"), self.character);
    self.tempKnob:initialise();
    self.tempKnob:instantiate();
    self.tempKnob.onMouseUpFct = ISOvenUI.ChangeKnob;
    self.tempKnob.target = self;
    self:addChild(self.tempKnob);

    self.tempType = ISTickBox:new(20, self.tempKnob.y + self.tempKnob.height + 10, getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_Oven_Fahrenheit")) + 20, 18, "", self, ISOvenUI.onChangeTempType);
    self.tempType:initialise();
    self.tempType:instantiate();
    self.tempType:addOption(getText("IGUI_Oven_Fahrenheit"));
    self.tempType:addOption(getText("IGUI_Oven_Celsius"));
    self.tempType.selected[1] = not getCore():isCelsius();
    self.tempType.selected[2] = getCore():isCelsius();
    self:addChild(self.tempType);

    self:changeTempType();

    local texBG = getTexture("media/ui/Knobs/KnobBGOvenTimer.png")
    self.timerKnob = ISKnob:new(self.width-20-texBG:getWidthOrig(),40,self.knobTex, texBG, getText("IGUI_Timer"), self.character);
    self.timerKnob:initialise();
    self.timerKnob:instantiate();
    self.timerKnob.onMouseUpFct = ISOvenUI.ChangeKnob;
    self.timerKnob.target = self;
    self:addChild(self.timerKnob);

    self.ok = ISButton:new(10, self.tempType:getBottom() + 10, btnWid, btnHgt, getText("ContextMenu_Turn_On"), self, ISOvenUI.onClick);
    self.ok.internal = "OK";
    self.ok:initialise();
    self.ok:instantiate();
    self.ok.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.ok);

    self.close = ISButton:new(self:getWidth() - btnWid - 10, self.tempType:getBottom() + 10, btnWid, btnHgt, getText("UI_Cancel"), self, ISOvenUI.onClick);
    self.close.internal = "CLOSE";
    self.close:initialise();
    self.close:instantiate();
    self.close.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.close);

    self:setHeight(self.close:getBottom() + padBottom)

    self:addKnobValues();
    self:updateButtons();
	
	self:insertNewLineOfButtons(self.tempKnob, self.timerKnob, self.tempType)
    self:insertNewLineOfButtons(self.ok, self.close)
end

function ISOvenUI:onChangeTempType(clickedOption, enabled)
    self.tempType.selected[1] = false;
    self.tempType.selected[2] = false;
    self.tempType.selected[clickedOption] = true;
    getCore():setCelsius(self.tempType.selected[2]);
    getCore():saveOptions();
    self:changeTempType();
end

function ISOvenUI:changeTempType()
    if not getCore():isCelsius() then -- farenheit
        self.tempKnob.valuesBg = getTexture("media/ui/Knobs/KnobBGFarhenOvenTemp.png");
    else -- celsius
        self.tempKnob.valuesBg = getTexture("media/ui/Knobs/KnobBGCelciusOvenTemp.png");
    end
end

function ISOvenUI:ChangeKnob()
    self.oven:setMaxTemperature(self.tempKnob:getValue());
    self.oven:setTimer(self.timerKnob:getValue() * 60);
    local sync = false
    -- send updated info once we finish dragging the knobs
    if not self.timerKnob.dragging and not self.tempKnob.dragging then
        self.oven:sync();
        sync = true
    end
    self.oven:syncSpriteGridObjects(false, sync)
end

function ISOvenUI:update()
    self:updateButtons();
    if self.character:DistTo(self.oven:getX(), self.oven:getY()) > 3 then
        self:setVisible(false);
        self:removeFromUIManager();
    end
end

function ISOvenUI:updateButtons()
    if not self.timerKnob.dragging then
        if self.oven:isRunningFor() > 0 then
            self.timerKnob:setKnobPosition(math.ceil((self.oven:getTimer() - self.oven:isRunningFor()) / 60));
        else
            self.timerKnob:setKnobPosition(self.oven:getTimer() / 60);
        end
    end
    if not self.tempKnob.dragging then
        self.tempKnob:setKnobPosition(self.oven:getMaxTemperature());
    end
    if self.oven:Activated() then
       self.ok:setTitle(getText("ContextMenu_Turn_Off"))
    else
        self.ok:setTitle(getText("ContextMenu_Turn_On"))
    end
    self.ok:setEnable(self.oven:getContainer() and self.oven:getContainer():isPowered())
end

function ISOvenUI:addKnobValues()
    self.tempKnob:addValue(0, 0);
    self.tempKnob:addValue(45, 50);
    self.tempKnob:addValue(90, 100);
    self.tempKnob:addValue(135, 150);
    self.tempKnob:addValue(180, 200);
    self.tempKnob:addValue(225, 250);
    self.tempKnob:addValue(270, 300);

    self.timerKnob:addValue(0, 0);
    self.timerKnob:addValue(18, 1);
    self.timerKnob:addValue(36, 2);
    self.timerKnob:addValue(54, 3);
    self.timerKnob:addValue(72, 4);
    self.timerKnob:addValue(90, 5);
    self.timerKnob:addValue(108, 10);
    self.timerKnob:addValue(126, 15);
    self.timerKnob:addValue(144, 20);
    self.timerKnob:addValue(162, 25);
    self.timerKnob:addValue(180, 30);
    self.timerKnob:addValue(198, 40);
    self.timerKnob:addValue(216, 50);
    self.timerKnob:addValue(234, 60);
    self.timerKnob:addValue(252, 90);
    self.timerKnob:addValue(270, 120);
end

function ISOvenUI:render()
    ISPanelJoypad.render(self);
--    print(self.oven:getContainer():getTemprature());
--    self:drawTextCentre(math.floor(self.oven:getCurrentTemperature()) .. "", self.width/2, 5, 1, 1, 1, 1, self.font);
end

function ISOvenUI:prerender()
    ISPanelJoypad.prerender(self);
end

function ISOvenUI:onClick(button)
    if button.internal == "CLOSE" then
        self:setVisible(false);
        self:removeFromUIManager();
		local player = self.character:getPlayerNum()
        if JoypadState.players[player+1] then
            setJoypadFocus(player, self.prevFocus)
        end
    end
    if button.internal == "OK" then
        self.oven:setTimer(self.timerKnob:getValue() * 60);
        self.oven:setMaxTemperature(self.tempKnob:getValue());
        self.oven:Toggle();
    end
end

function ISOvenUI:onGainJoypadFocus(joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData)
    self.joypadIndexY = 1
    self.joypadIndex = 1
    self.joypadButtons = self.joypadButtonsY[self.joypadIndexY]
    self.joypadButtons[self.joypadIndex]:setJoypadFocused(true)
    self:setISButtonForA(self.ok)
    self:setISButtonForB(self.close)
end

function ISOvenUI:onJoypadDown(button)
    ISPanelJoypad.onJoypadDown(self, button)
    if button == Joypad.AButton then
        self:onClick(self.ok)
    end
    if button == Joypad.BButton then
        self:onClick(self.close)
    end
end

--************************************************************************--
--** ISOvenUI:new
--**
--************************************************************************--
function ISOvenUI:new(x, y, width, height, oven, character)
    local o = {}
    o = ISPanelJoypad:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
	local player = character:getPlayerNum()
    if y == 0 then
        o.y = getPlayerScreenTop(player) + (getPlayerScreenHeight(player) - height) / 2
        o:setY(o.y)
    end
    if x == 0 then
        o.x = getPlayerScreenLeft(player) + (getPlayerScreenWidth(player) - width) / 2
        o:setX(o.x)
    end
    o.backgroundColor.a = 0.75
    o.width = width;
    o.height = height;
    o.character = character;
    o.oven = oven;
    o.moveWithMouse = true;
    o.knobTex = getTexture("media/ui/Knobs/KnobDial.png");
    o.anchorLeft = true;
    o.anchorRight = true;
    o.anchorTop = true;
    o.anchorBottom = true;
    return o;
end
