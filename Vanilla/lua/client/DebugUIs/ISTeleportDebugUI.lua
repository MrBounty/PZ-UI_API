--***********************************************************
--**              	  ROBERT JOHNSON                       **
--***********************************************************

ISTeleportDebugUI = ISPanelJoypad:derive("ISTeleportDebugUI");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

--************************************************************************--
--** ISTeleportDebugUI:initialise
--**
--************************************************************************--

function ISTeleportDebugUI:initialise()
	ISPanelJoypad.initialise(self);
	
	local fontHgt = FONT_HGT_SMALL
	local buttonWid1 = getTextManager():MeasureStringX(UIFont.Small, "Ok") + 12
	local buttonWid2 = getTextManager():MeasureStringX(UIFont.Small, "Cancel") + 12
	local buttonWid = math.max(math.max(buttonWid1, buttonWid2), 100)
	local buttonHgt = math.max(fontHgt + 6, 25)
	local padBottom = 10
	
	self.yes = ISButton:new((self:getWidth() / 2)  - 5 - buttonWid, self:getHeight() - padBottom - buttonHgt, buttonWid, buttonHgt, getText("UI_Ok"), self, ISTeleportDebugUI.onClick);
	self.yes.internal = "OK";
	self.yes:initialise();
	self.yes:instantiate();
	self.yes.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.yes);
	
	self.no = ISButton:new((self:getWidth() / 2) + 5, self:getHeight() - padBottom - buttonHgt, buttonWid, buttonHgt, getText("UI_Close"), self, ISTeleportDebugUI.onClick);
	self.no.internal = "CANCEL";
	self.no:initialise();
	self.no:instantiate();
	self.no.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.no);
	
	self.fontHgt = FONT_HGT_MEDIUM
	local inset = 2
	local height = inset + self.fontHgt * self.numLines + inset
	self.entryX = ISTextEntryBox:new(round(self.player:getX(), 0) .. "", 10, (self:getHeight() - height) / 2, (self:getWidth() / 2) - 20, height);
	self.entryX.font = UIFont.Medium
	self.entryX:initialise();
	self.entryX:instantiate();
	self.entryX:setOnlyNumbers(true);
	self:addChild(self.entryX);
	
	self.entryY = ISTextEntryBox:new(round(self.player:getY(), 0) .. "", (self:getWidth() / 2), (self:getHeight() - height) / 2, (self:getWidth() / 2) - 20, height);
	self.entryY.font = UIFont.Medium
	self.entryY:initialise();
	self.entryY:instantiate();
	self.entryY:setOnlyNumbers(true);
	self:addChild(self.entryY);
end

function ISTeleportDebugUI:destroy()
	UIManager.setShowPausedMessage(true);
	self:setVisible(false);
	self:removeFromUIManager();
end

function ISTeleportDebugUI:onClick(button)
	if button.internal == "CANCEL" then
		self:destroy();
		return;
	end
	if self.onclick ~= nil then
		self.onclick(self.target, self.entryX:getInternalText(), self.entryY:getInternalText());
	end
end

function ISTeleportDebugUI:titleBarHeight()
	return 16
end

function ISTeleportDebugUI:prerender()
	self.backgroundColor.a = 0.8
	self.entryX.backgroundColor.a = 0.8
	self.entryY.backgroundColor.a = 0.8
	
	self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
	
	local th = self:titleBarHeight()
	self:drawTextureScaled(self.titlebarbkg, 2, 1, self:getWidth() - 4, th - 2, 1, 1, 1, 1);
	
	self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	
	self:drawTextCentre("Teleport", self:getWidth() / 2, 20, 1, 1, 1, 1, UIFont.NewLarge);
	
	self:updateButtons();
end

function ISTeleportDebugUI:updateButtons()
	self.yes:setEnable(true);
	self.yes.tooltip = nil;
	if string.trim(self.entryX:getInternalText()) == "" or string.trim(self.entryY:getInternalText()) == "" then
		self.yes:setEnable(false);
		self.yes.tooltip = getText("IGUI_TextBox_CantBeEmpty");
	end
end

--************************************************************************--
--** ISTeleportDebugUI:render
--**
--************************************************************************--
function ISTeleportDebugUI:render()

end

function ISTeleportDebugUI:onMouseMove(dx, dy)
	self.mouseOver = true
	if self.moving then
		self:setX(self.x + dx)
		self:setY(self.y + dy)
		self:bringToTop()
	end
end

function ISTeleportDebugUI:onMouseMoveOutside(dx, dy)
	self.mouseOver = false
	if self.moving then
		self:setX(self.x + dx)
		self:setY(self.y + dy)
		self:bringToTop()
	end
end

function ISTeleportDebugUI:onMouseDown(x, y)
	if not self:getIsVisible() then
		return
	end
	self.downX = x
	self.downY = y
	self.moving = true
	self:bringToTop()
end

function ISTeleportDebugUI:onMouseUp(x, y)
	if not self:getIsVisible() then
		return;
	end
	self.moving = false
	if ISMouseDrag.tabPanel then
		ISMouseDrag.tabPanel:onMouseUp(x,y)
	end
	ISMouseDrag.dragView = nil
end

function ISTeleportDebugUI:onMouseUpOutside(x, y)
	if not self:getIsVisible() then
		return
	end
	self.moving = false
	ISMouseDrag.dragView = nil
end

--************************************************************************--
--** ISTeleportDebugUI:new
--**
--************************************************************************--
function ISTeleportDebugUI:new(x, y, width, height, player, target, onclick)
	local o = {}
	o = ISPanelJoypad:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self
	if y == 0 then
		o.y = o:getMouseY() - (height / 2)
		o:setY(o.y)
	end
	if x == 0 then
		o.x = o:getMouseX() - (width / 2)
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
	o.target = target;
	o.onclick = onclick;
	o.player = player;
	o.titlebarbkg = getTexture("media/ui/Panel_TitleBar.png");
	o.numLines = 1
	o.maxLines = 1
	o.multipleLine = false
	return o;
end
