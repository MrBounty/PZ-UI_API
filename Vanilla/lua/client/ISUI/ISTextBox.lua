--***********************************************************
--**              	  ROBERT JOHNSON                       **
--***********************************************************

ISTextBox = ISPanelJoypad:derive("ISTextBox");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

--************************************************************************--
--** ISTextBox:initialise
--**
--************************************************************************--

function ISTextBox:initialise()
    ISPanelJoypad.initialise(self);

	local fontHgt = FONT_HGT_SMALL
	local buttonWid1 = getTextManager():MeasureStringX(UIFont.Small, "Ok") + 12
	local buttonWid2 = getTextManager():MeasureStringX(UIFont.Small, "Cancel") + 12
	local buttonWid = math.max(math.max(buttonWid1, buttonWid2), 100)
	local buttonHgt = math.max(fontHgt + 6, 25)
	local padBottom = 10

    self.yes = ISButton:new((self:getWidth() / 2)  - 5 - buttonWid, self:getHeight() - padBottom - buttonHgt, buttonWid, buttonHgt, getText("UI_Ok"), self, ISTextBox.onClick);
    self.yes.internal = "OK";
    self.yes:initialise();
    self.yes:instantiate();
    self.yes.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.yes);

    self.no = ISButton:new((self:getWidth() / 2) + 5, self:getHeight() - padBottom - buttonHgt, buttonWid, buttonHgt, getText("UI_Cancel"), self, ISTextBox.onClick);
    self.no.internal = "CANCEL";
    self.no:initialise();
    self.no:instantiate();
    self.no.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.no);

    self.fontHgt = FONT_HGT_MEDIUM
    local inset = 2
    local height = inset + self.fontHgt * self.numLines + inset
    self.entry = ISTextEntryBox:new(self.defaultEntryText, self:getWidth() / 2 - ((self:getWidth() - 40) / 2), (self:getHeight() - height) / 2, self:getWidth() - 40, height);
    self.entry.font = UIFont.Medium
    self.entry:initialise();
    self.entry:instantiate();
    self.entry:setMaxLines(self.maxLines)
    self.entry:setMultipleLine(self.multipleLine)
    self:addChild(self.entry);

    self.colorBtn = ISButton:new(self.entry.x + self.entry.width + 5, self.entry.y, height, height, "", self, ISTextBox.onColorPicker);
    self.colorBtn:initialise();
    self.colorBtn.backgroundColor = {r = 1, g = 1, b = 1, a = 1};
    self:addChild(self.colorBtn);
    self.colorBtn:setVisible(false);

    self.colorPicker = ISColorPicker:new(0, 0)
    self.colorPicker:initialise()
    self.colorPicker.pickedTarget = self
    self.colorPicker.resetFocusTo = self
    self.currentColor = ColorInfo.new(1,1,1,1);
    self.colorPicker:setInitialColor(self.currentColor);
    self.colorPicker:addToUIManager();
    self.colorPicker:setVisible(false);
    self.colorPicker.otherFct = true;
    self.colorPicker.parent = self;
end

function ISTextBox:onColorPicker(button)
    self.colorPicker:setX(getMouseX() - 100);
    self.colorPicker:setY(getMouseY() - 20);
    self.colorPicker.pickedFunc = ISTextBox.onPickedColor;
    self.colorPicker:setVisible(true);
    self.colorPicker:bringToTop();
end

function ISTextBox:onPickedColor(color, mouseUp)
    self.currentColor = ColorInfo.new(color.r, color.g, color.b,1);
    self.colorBtn.backgroundColor = {r = color.r, g = color.g, b = color.b, a = 1};
    self.entry.javaObject:setTextColor(self.currentColor);
    self.colorPicker:setVisible(false);
end

function ISTextBox:enableColorPicker()
    self.colorBtn:setVisible(true);
    self.entry:setWidth(self.entry:getWidth() - self.entry:getHeight());
    self.colorBtn:setX(self.entry:getX() + self.entry:getWidth() + 5);
end

function ISTextBox:setOnlyNumbers(onlyNumbers)
    self.entry:setOnlyNumbers(onlyNumbers);
end

function ISTextBox:setMultipleLine(multiple)
    self.multipleLine = multiple
end

function ISTextBox:setNumberOfLines(numLines)
    self.numLines = numLines
end

function ISTextBox:setMaxLines(max)
    self.maxLines = max
end

function ISTextBox:setValidateFunction(target, func, arg1, arg2)
	self.validateTarget = target
	self.validateFunc = func
	self.validateArgs = { arg1, arg2 }
end

function ISTextBox:setValidateTooltipText(text)
	self.validateTooltipText = text
end

function ISTextBox:destroy()
	UIManager.setShowPausedMessage(true);
	self:setVisible(false);
	self:removeFromUIManager();
--	if UIManager.getSpeedControls() then
--		UIManager.getSpeedControls():SetCurrentGameSpeed(1);
--	end
end

function ISTextBox:onClick(button)
    if self.player and JoypadState.players[self.player+1] then
        setJoypadFocus(self.player, nil)
    elseif self.joyfocus and self.joyfocus.focus == self then
        self.joyfocus.focus = nil
    end
    if self.onclick ~= nil then
        self.onclick(self.target, button, self.param1, self.param2, self.param3, self.param4);
    end
    if not self.showError then
        self:destroy();
    end
end

function ISTextBox:titleBarHeight()
	return 16
end

function ISTextBox:prerender()
	self.backgroundColor.a = 0.8
	self.entry.backgroundColor.a = 0.8

	self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);

    local th = self:titleBarHeight()
    self:drawTextureScaled(self.titlebarbkg, 2, 1, self:getWidth() - 4, th - 2, 1, 1, 1, 1);

	self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

	local fontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()
	self:drawTextCentre(self.text, self:getWidth() / 2, self.entry:getY() - 8 - fontHgt, 1, 1, 1, 1, UIFont.Small);

    if self.showError then
        local fontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()
        self:drawTextCentre(self.errorMsg, self:getWidth() / 2, self.entry:getY() + 50 - fontHgt, 1, 0, 0, 1, UIFont.Small);
    end

    self:updateButtons();
end

function ISTextBox:showErrorMessage(show, errorMsg)
    self.showError = show
    self.errorMsg = errorMsg
end

function ISTextBox:updateButtons()
    self.yes:setEnable(true);
    self.yes.tooltip = nil;
    local text = self.entry:getText()
    if self.validateFunc and not self.validateFunc(self.validateTarget, text, self.validateArgs[1], self.validateArgs[2]) then
        self.yes:setEnable(false);
        self.yes.tooltip = self.validateTooltipText;
    end
    if self.maxChars and self.entry:getInternalText():len() > self.maxChars then
        self.yes:setEnable(false);
        self.yes.tooltip = getText("IGUI_TextBox_TooManyChar", self.maxChars);
    end
    if self.noEmpty and string.trim(self.entry:getInternalText()) == "" then
        self.yes:setEnable(false);
        self.yes.tooltip = getText("IGUI_TextBox_CantBeEmpty");
    end
end

--************************************************************************--
--** ISTextBox:render
--**
--************************************************************************--
function ISTextBox:render()

end

function ISTextBox:onMouseMove(dx, dy)
    self.mouseOver = true
    if self.moving then
        self:setX(self.x + dx)
        self:setY(self.y + dy)
        self:bringToTop()
    end
end

function ISTextBox:onMouseMoveOutside(dx, dy)
    self.mouseOver = false
    if self.moving then
        self:setX(self.x + dx)
        self:setY(self.y + dy)
        self:bringToTop()
    end
end

function ISTextBox:onMouseDown(x, y)
    if not self:getIsVisible() then
        return
    end
    self.downX = x
    self.downY = y
    self.moving = true
    self:bringToTop()
end

function ISTextBox:onMouseUp(x, y)
    if not self:getIsVisible() then
        return;
    end
    self.moving = false
    if ISMouseDrag.tabPanel then
        ISMouseDrag.tabPanel:onMouseUp(x,y)
    end
    ISMouseDrag.dragView = nil
end

function ISTextBox:onMouseUpOutside(x, y)
    if not self:getIsVisible() then
        return
    end
    self.moving = false
    ISMouseDrag.dragView = nil
end

function ISTextBox:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData)
	self:setISButtonForA(self.yes)
	self:setISButtonForB(self.no)
	self.joypadButtons = {}
end

function ISTextBox:onJoypadDown(button, joypadData)
	if button == Joypad.AButton then
		if not self.yes:isEnabled() then
			return
		end
		self.yes.player = self.player
		self.yes.onclick(self.yes.target, self.yes)
		if joypadData.focus == self then
			if self.player and JoypadState.players[self.player+1] then
				setJoypadFocus(self.player, nil)
			else
				joypadData.focus = nil
			end
		end
		self:destroy()
	end
	if button == Joypad.BButton then
		self.no.player = self.player
		self.no.onclick(self.no.target, self.no)
		if joypadData.focus == self then
			if self.player and JoypadState.players[self.player+1] then
				setJoypadFocus(self.player, nil)
			else
				joypadData.focus = nil
			end
		end
		self:destroy()
	end
end

--************************************************************************--
--** ISTextBox:new
--**
--************************************************************************--
function ISTextBox:new(x, y, width, height, text, defaultEntryText, target, onclick, player, param1, param2, param3, param4)
	local o = {}
	o = ISPanelJoypad:new(x, y, width, height);
	setmetatable(o, self)
    self.__index = self
	local playerObj = player and getSpecificPlayer(player) or nil
	if y == 0 then
		if playerObj and playerObj:getJoypadBind() ~= -1 then
			o.y = getPlayerScreenTop(player) + (getPlayerScreenHeight(player) - height) / 2
		else
			o.y = o:getMouseY() - (height / 2)
		end
		o:setY(o.y)
	end
	if x == 0 then
		if playerObj and playerObj:getJoypadBind() ~= -1 then
			o.x = getPlayerScreenLeft(player) + (getPlayerScreenWidth(player) - width) / 2
		else
			o.x = o:getMouseX() - (width / 2)
		end
		o:setX(o.x)
	end
	o.name = nil;
    o.backgroundColor = {r=0, g=0, b=0, a=0.5};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
	o.width = width;
    local txtWidth = getTextManager():MeasureStringX(UIFont.Small, text) + 10;
    if width < txtWidth then
        o.width = txtWidth;
    end
	o.height = height;
	o.anchorLeft = true;
	o.anchorRight = true;
	o.anchorTop = true;
	o.anchorBottom = true;
	o.text = text;
	o.target = target;
	o.onclick = onclick;
	o.player = player
    o.param1 = param1;
    o.param2 = param2;
    o.param3 = param3;
    o.param4 = param4;
    o.defaultEntryText = defaultEntryText;
    o.titlebarbkg = getTexture("media/ui/Panel_TitleBar.png");
    o.numLines = 1
    o.maxLines = 1
    o.multipleLine = false
    return o;
end

function ISTextBox:close()
	ISPanelJoypad.close(self)
	if JoypadState.players[self.player+1] then
		setJoypadFocus(self.player, nil)
	end
end

