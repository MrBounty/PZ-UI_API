--***********************************************************
--**              	  ROBERT JOHNSON                       **
--***********************************************************

---@class ISRichTextBox : ISPanelJoypad
ISRichTextBox = ISPanelJoypad:derive("ISRichTextBox");


--************************************************************************--
--** ISRichTextBox:initialise
--**
--************************************************************************--

function ISRichTextBox:initialise()
	ISPanel.initialise(self);

	local fontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()
	local buttonWid1 = getTextManager():MeasureStringX(UIFont.Small, "Ok") + 12
	local buttonWid2 = getTextManager():MeasureStringX(UIFont.Small, "Cancel") + 12
	local buttonWid = math.max(math.max(buttonWid1, buttonWid2), 100)
	local buttonHgt = math.max(fontHgt + 6, 25)
	local padBottom = 10

    self.yes = ISButton:new((self:getWidth() / 2)  - 5 - buttonWid, self:getHeight() - padBottom - buttonHgt, buttonWid, buttonHgt, getText("UI_Ok"), self, ISRichTextBox.onClick);
    self.yes.internal = "OK";
    self.yes:initialise();
    self.yes:instantiate();
    self.yes.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.yes);

    self.no = ISButton:new((self:getWidth() / 2) + 5, self:getHeight() - padBottom - buttonHgt, buttonWid, buttonHgt, getText("UI_Cancel"), self, ISRichTextBox.onClick);
    self.no.internal = "CANCEL";
    self.no:initialise();
    self.no:instantiate();
    self.no.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.no);

    self.fontHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
    local inset = 2
    local height = inset + self.fontHgt + inset
    self.entry = ISTextEntryBox:new(self.defaultEntryText, self:getWidth() / 2 - ((self:getWidth() - 40) / 2), (self:getHeight() - height) / 2, self:getWidth() - 40, height);
    self.entry.font = UIFont.Medium
    self.entry:initialise();
    self.entry:instantiate();
    self:addChild(self.entry);

    self.chatText = ISRichTextPanel:new(2, 2, self.width - 4, self.height - 30);
    self.chatText.marginRight = self.chatText.marginLeft;
    self.chatText:initialise();

    self:addChild(self.chatText);
    self.chatText.background = false;
    self.chatText.text = self.text;
    self.chatText:paginate();
end

function ISRichTextBox:update()
    ISPanelJoypad.update(self)
    local minHeight = self.chatText:getY() + self.chatText:getHeight() + 30
    if self:getHeight() < minHeight then
        local dh = minHeight - self:getHeight()
        self:setHeight(minHeight)
        self:setY(self:getY() - dh / 2)
    end
end

function ISRichTextBox:setOnlyNumbers(onlyNumbers)
    self.entry:setOnlyNumbers(onlyNumbers);
end

function ISRichTextBox:setValidateFunction(target, func, arg1, arg2)
	self.validateTarget = target
	self.validateFunc = func
	self.validateArgs = { arg1, arg2 }
end

function ISRichTextBox:setValidateTooltipText(text)
	self.validateTooltipText = text
end

function ISRichTextBox:destroy()
	UIManager.setShowPausedMessage(true);
	self:setVisible(false);
	self:removeFromUIManager();
	if UIManager.getSpeedControls() then
		UIManager.getSpeedControls():SetCurrentGameSpeed(1);
	end
end

function ISRichTextBox:onClick(button)
	self:destroy();
	if self.onclick ~= nil then
		self.onclick(self.target, button, self.param1, self.param2, self.param3, self.param4);
	end
end

function ISRichTextBox:prerender()
	self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
	self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
--	self:drawTextCentre(self.text, self:getWidth() / 2, (self:getHeight() / 2) - 40, 1, 1, 1, 1, UIFont.Small);

	self.yes:setEnable(true);
	self.yes.tooltip = nil;
	local text = self.entry:getText()
	if self.validateFunc and not self.validateFunc(self.validateTarget, text, self.validateArgs[1], self.validateArgs[2]) then
		self.yes:setEnable(false);
		self.yes.tooltip = self.validateTooltipText;
	end
end

--************************************************************************--
--** ISRichTextBox:render
--**
--************************************************************************--
function ISRichTextBox:render()

end

function ISRichTextBox:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData)
	self:setISButtonForA(self.yes)
	self:setISButtonForB(self.no)
	self.joypadButtons = {}
end

function ISRichTextBox:onJoypadDown(button, joypadData)
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
--** ISRichTextBox:new
--**
--************************************************************************--
function ISRichTextBox:new(x, y, width, height, text, defaultEntryText, target, onclick, player, param1, param2, param3, param4)
	local o = {}
	o = ISPanel:new(x, y, width, height);
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
    return o;
end

