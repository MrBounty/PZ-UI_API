require "ISUI/ISPanelJoypad"

---@class ISAlarmClockDialog : ISPanelJoypad
ISAlarmClockDialog = ISPanelJoypad:derive("ISAlarmClockDialog");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

--************************************************************************--
--** ISAlarmClockDialog:initialise
--**
--************************************************************************--

function ISAlarmClockDialog:initialise()
    ISPanelJoypad.initialise(self);
    local btnWid = getTextManager():MeasureStringX(UIFont.Small, "999")
    local btnGap = 2
    local buttonsY = 10 + FONT_HGT_SMALL + 10

    local btnUpDownHgt = FONT_HGT_SMALL
    local entryHgt = FONT_HGT_SMALL + 2 * 2
    
    self.button1p = ISButton:new((self:getWidth() / 2) - btnGap - btnWid, buttonsY, btnWid, btnUpDownHgt, "^", self, ISAlarmClockDialog.onClick);
    self.button1p.internal = "HOURSPLUS";
    self.button1p:initialise();
    self.button1p:instantiate();
    self.button1p.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.button1p);

    self.hours = ISTextEntryBox:new("0", self:getWidth() / 2 - btnGap - btnWid, self.button1p:getBottom() + 2, btnWid, entryHgt);
    self.hours:initialise();
    self.hours:instantiate();
    self:addChild(self.hours);

    self.button1m = ISButton:new(self:getWidth() / 2 - btnGap - btnWid, self.hours:getBottom() + 2, btnWid, btnUpDownHgt, "v", self, ISAlarmClockDialog.onClick);
    self.button1m.internal = "HOURSMINUS";
    self.button1m:initialise();
    self.button1m:instantiate();
    self.button1m.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.button1m);

    --
    self.button2p = ISButton:new(self:getWidth() / 2 + btnGap, buttonsY, btnWid, btnUpDownHgt, "^", self, ISAlarmClockDialog.onClick);
    self.button2p.internal = "MINPLUS";
    self.button2p:initialise();
    self.button2p:instantiate();
    self.button2p.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.button2p);

    self.mins = ISTextEntryBox:new("0", self:getWidth() / 2 + btnGap, self.button2p:getBottom() + 2, btnWid, entryHgt);
    self.mins:initialise();
    self.mins:instantiate();
    self:addChild(self.mins);

    self.button2m = ISButton:new(self:getWidth() / 2 + btnGap, self.mins:getBottom() + 2, btnWid, btnUpDownHgt,"v", self, ISAlarmClockDialog.onClick);
    self.button2m.internal = "MINMINUS";
    self.button2m:initialise();
    self.button2m:instantiate();
    self.button2m.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.button2m);

    local alarmHgt = FONT_HGT_SMALL + 4
    local okHgt = alarmHgt

    self.setAlarm = ISTickBox:new(0, self.button2m:getBottom() + 10, 20, alarmHgt, "", nil, nil)
    self.setAlarm:initialise()
    self:addChild(self.setAlarm)
    self.setAlarm:addOption(getText("UI_Alarm"))
    self.setAlarm:setWidthToFit()
    self.setAlarm:setX((self.width - self.setAlarm.width) / 2)

    --
    self.ok = ISButton:new((self:getWidth() - 100) / 2, self.setAlarm:getBottom() + 10, 100, okHgt, getText("UI_Ok"), self, ISAlarmClockDialog.onClick);
    self.ok.internal = "OK";
    self.ok:initialise();
    self.ok:instantiate();
    self.ok.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.ok);

    self:setHeight(self.ok:getBottom() + 5)

    self.hours:setText(self.alarm:getHour() .. "");
    if self.alarm:getMinute() == 0 then
        self.mins:setText(self.alarm:getMinute() .. "0");
    else
        self.mins:setText(self.alarm:getMinute() .. "");
    end

    self.setAlarm:setSelected(1, self.alarm:isAlarmSet())

    self:insertNewLineOfButtons(self.button1p, self.button2p)
    self:insertNewLineOfButtons(self.button1m, self.button2m)
	self:insertNewLineOfButtons(self.setAlarm)
    self:insertNewLineOfButtons(self.ok)
end

function ISAlarmClockDialog:destroy()
    UIManager.setShowPausedMessage(true);
    self:setVisible(false);
    self:removeFromUIManager();
    if UIManager.getSpeedControls() then
        UIManager.getSpeedControls():SetCurrentGameSpeed(1);
    end
end

function ISAlarmClockDialog:onClick(button)
    if button.internal == "OK" then
        self.alarm:setAlarmSet(self.setAlarm:isSelected(1));
        self.alarm:setHour(tonumber(self.hours:getText()))
        self.alarm:setMinute(tonumber(self.mins:getText()))
		self.alarm:syncAlarmClock()
        self:destroy();
        if JoypadState.players[self.player+1] then
            setJoypadFocus(self.player, self.prevFocus)
        end
    end
    if button.internal == "HOURSPLUS" then
        self:incrementHour(self.hours);
    end
    if button.internal == "HOURSMINUS" then
        self:decrementHour(self.hours);
    end
    if button.internal == "MINPLUS" then
        self:incrementMin(self.mins);
    end
    if button.internal == "MINMINUS" then
        self:decrementMin(self.mins);
    end
end

function ISAlarmClockDialog:incrementHour(number)
    local newNumber = tonumber(number:getText()) + 1;
    if newNumber > 23 then
        newNumber = 0;
    end
    number:setText(newNumber .. "");
end

function ISAlarmClockDialog:decrementHour(number)
    local newNumber = tonumber(number:getText()) - 1;
    if newNumber < 0 then
        newNumber = 23;
    end
    number:setText(newNumber .. "");
end

function ISAlarmClockDialog:incrementMin(number)
    local newNumber = tonumber(number:getText()) + 10;
    if newNumber > 50 then
        newNumber = 0;
    end
    if newNumber == 0 then
        number:setText(newNumber .. "0");
    else
        number:setText(newNumber .. "");
    end
end

function ISAlarmClockDialog:decrementMin(number)
    local newNumber = tonumber(number:getText()) - 10;
    if newNumber < 0 then
        newNumber = 50;
    end
    if newNumber == 0 then
        number:setText(newNumber .. "0");
    else
        number:setText(newNumber .. "");
    end
end

function ISAlarmClockDialog:prerender()
    self.backgroundColor.a = 0.8
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawTextCentre(getText("IGUI_SetAlarm"), self:getWidth()/2, 10, 1, 1, 1, 1, UIFont.Small);
end

--************************************************************************--
--** ISAlarmClockDialog:render
--**
--************************************************************************--
function ISAlarmClockDialog:render()

end

function ISAlarmClockDialog:update()
    ISPanelJoypad.update(self)
    if self.ok.joypadFocused then
        self.ok:setJoypadButton(Joypad.Texture.AButton)
    else
        self.ok.joypadTexture = nil
        self.ok.isJoypad = false
    end
    if math.abs(self.character:getX() - self.playerX) > 0.1 or math.abs(self.character:getY() - self.playerY) > 0.1 then
        self:destroy()
    end
end

function ISAlarmClockDialog:onGainJoypadFocus(joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData)
    self.joypadIndexY = 1
    self.joypadIndex = 1
    self.joypadButtons = self.joypadButtonsY[self.joypadIndexY]
    self.joypadButtons[self.joypadIndex]:setJoypadFocused(true)
end

function ISAlarmClockDialog:onJoypadDown(button)
    ISPanelJoypad.onJoypadDown(self, button)
    if button == Joypad.BButton then
        self:onClick(self.ok)
    end
end

function ISAlarmClockDialog:getCode()
    local n1 = tonumber(self.number1:getText()) * 100
    local n2 = tonumber(self.number2:getText()) * 10
    local n3 = tonumber(self.number3:getText())
    return n1 + n2 + n3
end

--************************************************************************--
--** ISAlarmClockDialog:new
--**
--************************************************************************--
function ISAlarmClockDialog:new(x, y, width, height, player, alarm)
    local o = {}
    o = ISPanelJoypad:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    local playerObj = player and getSpecificPlayer(player) or nil
    o.character = playerObj;
    o.name = nil;
    o.backgroundColor = {r=0, g=0, b=0, a=0.5};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    if y == 0 then
        o.y = getPlayerScreenTop(player) + (getPlayerScreenHeight(player) - height) / 2
        o:setY(o.y)
    end
    if x == 0 then
        o.x = getPlayerScreenLeft(player) + (getPlayerScreenWidth(player) - width) / 2
        o:setX(o.x)
    end
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = true;
    o.anchorTop = true;
    o.anchorBottom = true;
    o.player = player;
    o.playerX = playerObj:getX()
    o.playerY = playerObj:getY()
    o.alarm = alarm;
    return o;
end

