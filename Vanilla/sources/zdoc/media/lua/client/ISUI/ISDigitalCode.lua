--***********************************************************
--**              	  ROBERT JOHNSON                       **
--**            UI display with a question or text         **
--**          can display a yes/no button or ok btn        **
--***********************************************************

require "ISUI/ISPanelJoypad"

---@class ISDigitalCode : ISPanelJoypad
ISDigitalCode = ISPanelJoypad:derive("ISDigitalCode");


--************************************************************************--
--** ISDigitalCode:initialise
--**
--************************************************************************--

function ISDigitalCode:initialise()
	ISPanel.initialise(self);
    self.button1p = ISButton:new((self:getWidth() / 2) - 28, (self:getHeight() / 2) - 25, 16, 16, getText("^"), self, ISDigitalCode.onClick);
    self.button1p.internal = "B1PLUS";
    self.button1p:initialise();
    self.button1p:instantiate();
    self.button1p.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.button1p);

    self.number1 = ISTextEntryBox:new("0", self:getWidth() / 2 - 28, self:getHeight() / 2 -5, 18, 18);
    self.number1:initialise();
    self.number1:instantiate();
    self:addChild(self.number1);

    self.button1m = ISButton:new(self:getWidth() / 2 - 28, (self:getHeight() / 2) + 16, 16, 16, getText("v"), self, ISDigitalCode.onClick);
    self.button1m.internal = "B1MINUS";
    self.button1m:initialise();
    self.button1m:instantiate();
    self.button1m.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.button1m);

    --
    self.button2p = ISButton:new(self:getWidth() / 2 -8, (self:getHeight() / 2) - 25, 16, 16, getText("^"), self, ISDigitalCode.onClick);
    self.button2p.internal = "B2PLUS";
    self.button2p:initialise();
    self.button2p:instantiate();
    self.button2p.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.button2p);

    self.number2 = ISTextEntryBox:new("0", self:getWidth() / 2 -8, self:getHeight() / 2 -5, 18, 18);
    self.number2:initialise();
    self.number2:instantiate();
    self:addChild(self.number2);

    self.button2m = ISButton:new(self:getWidth() / 2 -8, (self:getHeight() / 2) + 16, 16, 16, getText("v"), self, ISDigitalCode.onClick);
    self.button2m.internal = "B2MINUS";
    self.button2m:initialise();
    self.button2m:instantiate();
    self.button2m.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.button2m);

    --
    self.button3p = ISButton:new(self:getWidth() / 2 + 12, (self:getHeight() / 2) - 25, 16, 16, getText("^"), self, ISDigitalCode.onClick);
    self.button3p.internal = "B3PLUS";
    self.button3p:initialise();
    self.button3p:instantiate();
    self.button3p.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.button3p);

    self.number3 = ISTextEntryBox:new("0", self:getWidth() / 2 + 12, self:getHeight() / 2 - 5, 18, 18);
    self.number3:initialise();
    self.number3:instantiate();
    self:addChild(self.number3);

    self.button3m = ISButton:new(self:getWidth() / 2 + 12, (self:getHeight() / 2) + 16, 16, 16, getText("v"), self, ISDigitalCode.onClick);
    self.button3m.internal = "B3MINUS";
    self.button3m:initialise();
    self.button3m:instantiate();
    self.button3m.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.button3m);

    --
    self.ok = ISButton:new((self:getWidth() / 2) - 13, self:getHeight() - 20, 26, 15, getText("UI_Ok"), self, ISDigitalCode.onClick);
    self.ok.internal = "OK";
    self.ok:initialise();
    self.ok:instantiate();
    self.ok.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.ok);

    self:insertNewLineOfButtons(self.button1p, self.button2p, self.button3p)
    self:insertNewLineOfButtons(self.button1m, self.button2m, self.button3m)
    self:insertNewLineOfButtons(self.ok)
end

function ISDigitalCode:destroy()
	UIManager.setShowPausedMessage(true);
	self:setVisible(false);
	self:removeFromUIManager();
	if UIManager.getSpeedControls() then
		UIManager.getSpeedControls():SetCurrentGameSpeed(1);
	end
end

function ISDigitalCode:onClick(button)
    if button.internal == "OK" then
        self:destroy();
        if JoypadState.players[self.player+1] then
            setJoypadFocus(self.player, nil)
        end
        if self.onclick ~= nil then
            self.onclick(self.target, button, self.character, self.padlock, self.thumpable);
        end
    end
    if button.internal == "B1PLUS" then
        self:increment(self.number1);
    end
    if button.internal == "B1MINUS" then
        self:decrement(self.number1);
    end
    if button.internal == "B2PLUS" then
        self:increment(self.number2);
    end
    if button.internal == "B2MINUS" then
        self:decrement(self.number2);
    end
    if button.internal == "B3PLUS" then
        self:increment(self.number3);
    end
    if button.internal == "B3MINUS" then
        self:decrement(self.number3);
    end
end

function ISDigitalCode:increment(number)
    local newNumber = tonumber(number:getText()) + 1;
    if newNumber > 9 then
        newNumber = 9;
    end
    number:setText(newNumber .. "");
end

function ISDigitalCode:decrement(number)
    local newNumber = tonumber(number:getText()) - 1;
    if newNumber < 0 then
        newNumber = 0;
    end
    number:setText(newNumber .. "");
end

function ISDigitalCode:prerender()
	self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
	self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    if self.new then
	    self:drawTextCentre(getText("IGUI_SetCode"), self:getWidth()/2, 10, 1, 1, 1, 1, UIFont.Small);
    else
        self:drawTextCentre(getText("IGUI_EnterCode"), self:getWidth()/2, 10, 1, 1, 1, 1, UIFont.Small);
    end
end

--************************************************************************--
--** ISDigitalCode:render
--**
--************************************************************************--
function ISDigitalCode:render()

end

function ISDigitalCode:update()
    ISPanelJoypad.update(self)
    if self.character:getX() ~= self.playerX or self.character:getY() ~= self.playerY then
        self:destroy()
    end
end

function ISDigitalCode:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData)
	self.joypadIndexY = 1
	self.joypadIndex = 1
	self.joypadButtons = self.joypadButtonsY[self.joypadIndexY]
	self.joypadButtons[self.joypadIndex]:setJoypadFocused(true)
end

function ISDigitalCode:onJoypadDown(button)
	ISPanelJoypad.onJoypadDown(self, button)
	if button == Joypad.BButton then
		self:onClick(self.ok)
	end
end

function ISDigitalCode:getCode()
    local n1 = tonumber(self.number1:getText()) * 100
    local n2 = tonumber(self.number2:getText()) * 10
    local n3 = tonumber(self.number3:getText())
    return n1 + n2 + n3
end

--************************************************************************--
--** ISDigitalCode:new
--**
--************************************************************************--
function ISDigitalCode:new(x, y, width, height, target, onclick, player, padlock, thumpable, new)
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
	o.target = target;
	o.onclick = onclick;
    o.player = player;
    o.playerX = playerObj:getX()
    o.playerY = playerObj:getY()
    o.padlock = padlock;
    o.thumpable = thumpable;
    o.new = new;
    return o;
end

