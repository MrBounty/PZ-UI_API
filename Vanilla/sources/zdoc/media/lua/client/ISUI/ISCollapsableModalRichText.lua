--***********************************************************
--**              	  ROBERT JOHNSON                       **
--**            UI display with a question or text         **
--**          can display a yes/no button or ok btn        **
--***********************************************************

---@class ISCollapsableModalRichText : ISCollapsableWindow
ISCollapsableModalRichText = ISCollapsableWindow:derive("ISCollapsableModalRichText");

--************************************************************************--
--** ISCollapsableModalRichText:initialise
--**
--************************************************************************--

function ISCollapsableModalRichText:createChildren()
    ISCollapsableWindow.createChildren(self)
end

function ISCollapsableModalRichText:initialise()
    ISCollapsableWindow.initialise(self);
	local btnWid = 100
	local btnHgt = 25
	local padBottom = 10
	if self.yesno then
		self.yes = ISButton:new((self:getWidth() / 2) - btnWid - 5, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Yes"), self, ISCollapsableModalRichText.onClick);
		self.yes.internal = "YES";
		self.yes.anchorTop = false
		self.yes.anchorBottom = true
		self.yes:initialise();
		self.yes:instantiate();
		self.yes.borderColor = {r=1, g=1, b=1, a=0.1};
		self:addChild(self.yes);

		self.no = ISButton:new((self:getWidth() / 2) + 5, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_No"), self, ISCollapsableModalRichText.onClick);
		self.no.internal = "NO";
		self.no.anchorTop = false
		self.no.anchorBottom = true
		self.no:initialise();
		self.no:instantiate();
		self.no.borderColor = {r=1, g=1, b=1, a=0.1};
		self:addChild(self.no);
	else
		self.ok = ISButton:new((self:getWidth() / 2) - btnWid / 2, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Ok"), self, ISCollapsableModalRichText.onClick);
		self.ok.internal = "OK";
		self.ok.anchorTop = false
		self.ok.anchorBottom = true
		self.ok:initialise();
		self.ok:instantiate();
		self.ok.borderColor = {r=1, g=1, b=1, a=0.1};
		self:addChild(self.ok);
    end

    self.chatText = ISRichTextPanel:new(20, 20, self.width - 20, self.height - 30);
    self.chatText.marginRight = self.chatText.marginLeft;
    self.chatText:initialise();

    self:addChild(self.chatText);
    self.chatText.background = false;
    self.chatText.text = self.text;
    self.chatText:paginate();
end

function ISCollapsableModalRichText:updateButtons()
    local btnHgt = 25
    local padBottom = 10
    if self.yesno then
        self.yes:setY(self:getHeight() - padBottom - btnHgt);
        self.no:setY(self:getHeight() - padBottom - btnHgt);
    else
        self.ok:setY(self:getHeight() - padBottom - btnHgt);
    end
end

function ISCollapsableModalRichText:destroy()
	UIManager.setShowPausedMessage(true);
	self:setVisible(false);
    if self.destroyOnClick then
	    self:removeFromUIManager();
    end
	if UIManager.getSpeedControls() then
		UIManager.getSpeedControls():SetCurrentGameSpeed(1);
	end
	if self.player and JoypadState.players[self.player+1] then
		setJoypadFocus(self.player, nil);
	end
end

function ISCollapsableModalRichText:onClick(button)
	self:destroy();
	if self.onclick ~= nil then
		self.onclick(self.target, button, self.param1, self.param2);
	end
end

function ISCollapsableModalRichText:prerender()
	self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
	self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
--	self:drawTextCentre(self.text, self:getWidth() / 2, (self:getHeight() / 2) - 10, 1, 1, 1, 1, UIFont.Small);
end
--
--function ISCollapsableModalRichText:onMouseDown(x, y)
----	ISCollapsableWindow.onMouseDown(self, x, y)
--	-- FIXME: this prevents clicks being passed to windows behind, but need to swallow clicks outside and mouse-move events as well
--	return true
--end

function ISCollapsableModalRichText:onGainJoypadFocus(joypadData)
    ISCollapsableWindow.onGainJoypadFocus(self, joypadData);
	if self.yesno then
		self:setISButtonForA(self.yes)
		self:setISButtonForB(self.no)
		self.yes.mouseOver = true;
	else
		self:setISButtonForA(self.ok)
		self.ok.mouseOver = true;
	end
end

function ISCollapsableModalRichText:onJoypadDown(button)
	if button == Joypad.AButton then
		if self.yesno then
			if self.yes.mouseOver then
				self.yes.player = self.player;
				self.yes.onclick(self.yes.target, self.yes);
			else
				self.no.player = self.player;
				self.no.onclick(self.no.target, self.no);
			end
		else
			self.ok.onclick(self.ok.target, self.ok);
		end
	end
	if button == Joypad.BButton then
		if self.yesno then
			self.no.player = self.player;
			self.no.onclick(self.no.target, self.no);
		else
			self.ok.onclick(self.ok.target, self.ok);
		end
	end
end

function ISCollapsableModalRichText:onJoypadDirRight()
	if self.yesno then
		self.no.mouseOver = true;
		self.yes.mouseOver = false;
	end
end

function ISCollapsableModalRichText:onJoypadDirLeft()
	if self.yesno then
		self.no.mouseOver = false;
		self.yes.mouseOver = true;
	end
end

function ISCollapsableModalRichText:update()
    ISCollapsableWindow.update(self)
--	local minHeight = self.chatText:getY() + self.chatText:getHeight() + 30
--	if self:getHeight() < minHeight then
--		local dh = minHeight - self:getHeight()
--		self:setHeight(minHeight)
--		self:setY(self:getY() - dh / 2)
--	end
end

--************************************************************************--
--** ISCollapsableModalRichText:new
--**
--************************************************************************--
function ISCollapsableModalRichText:new(x, y, width, height, text, yesno, target, onclick, player, param1, param2)
	local o = {}
	o = ISCollapsableWindow:new(x, y, width, height);
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
	o.height = height;
--	o.anchorLeft = true;
--	o.anchorRight = true;
--	o.anchorTop = true;
--	o.anchorBottom = true;
	o.text = text;
	o.yesno = yesno;
	o.target = target;
	o.onclick = onclick;
	o.yes = nil;
    o.player = player;
	o.no = nil;
	o.ok = nil;
	o.param1 = param1;
	o.param2 = param2;
    o:setResizable(false);
    o:setDrawFrame(true);
    return o;
end

function ISCollapsableModalRichText:close()
	ISCollapsableWindow.close(self)
	if JoypadState.players[self.player+1] then
		setJoypadFocus(self.player, nil)
	end
end

