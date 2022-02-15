--***********************************************************
--**              	  ROBERT JOHNSON                       **
--***********************************************************

---@class ModOrderUI : ISPanel
ModOrderUI = ISPanel:derive("ModOrderUI");
ModOrderUI.messages = {};

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

local function getTooltipText(name)
	local tooltip = getTextOrNull(name)
	if tooltip then
		tooltip = tooltip:gsub("\\n", "\n")
		tooltip = tooltip:gsub("\\\"", "\"")
	end
	return tooltip
end

--************************************************************************--
--** ModOrderUI:initialise
--**
--************************************************************************--

function ModOrderUI:initialise()
	ISPanel.initialise(self);
	local btnWid = 100
	local btnWid2 = 160
	local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
	local btnHgt2 = btnHgt
	local padBottom = 10
	
	self.no = ISButton:new(10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Cancel"), self, ModOrderUI.onClick);
	self.no.internal = "CANCEL";
	self.no.anchorTop = false
	self.no.anchorBottom = true
	self.no:initialise();
	self.no:instantiate();
	self.no.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.no);
	
	self.save = ISButton:new(self.width - btnWid - 10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Ok"), self, ModOrderUI.onClick);
	self.save.internal = "SAVE";
	self.save.anchorTop = false
	self.save.anchorBottom = true
	self.save:initialise();
	self.save:instantiate();
	self.save.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.save);

	self.richText = ISRichTextLayout:new(self.width - 10 - btnWid2 - 10 - 10)
	self.richText:setText(getText("UI_ModsConflictsInfo"))
	self.richText:paginate()

	local top = 50 + self.richText:getHeight() + 16
	self.datas = ISScrollingListBox:new(10, top, self.width - 10 - btnWid2 - 10 - 10, self.height - 130 - top);
	self.datas:initialise();
	self.datas:instantiate();
	self.datas.itemheight = FONT_HGT_SMALL + 3 * 2;
	self.datas.selected = 0;
	self.datas.joypadParent = self;
	self.datas.font = UIFont.NewSmall;
	self.datas.doDrawItem = self.drawDatas;
	self.datas.drawBorder = true;
	self:addChild(self.datas);
	
	self.up = ISButton:new(self.width - 10 - btnWid2, self.datas.y, btnWid2, btnHgt, getText("UI_ServerSettings_ButtonMoveUp"), self, ModOrderUI.onClick);
	self.up.internal = "UP";
	self.up.anchorTop = false
	self.up.anchorBottom = true
	self.up:initialise();
	self.up:instantiate();
	self.up.borderColor = {r=1, g=1, b=1, a=0.1};
	self.up.tooltip = getTooltipText("UI_ModsConflicts_ButtonMoveUp_tooltip")
	self:addChild(self.up);
	
	self.down = ISButton:new(self.width - 10 - btnWid2, self.up.y + self.up.height + 4, btnWid2, btnHgt, getText("UI_ServerSettings_ButtonMoveDown"), self, ModOrderUI.onClick);
	self.down.internal = "DOWN";
	self.down.anchorTop = false
	self.down.anchorBottom = true
	self.down:initialise();
	self.down:instantiate();
	self.down.borderColor = {r=1, g=1, b=1, a=0.1};
	self.down.tooltip = getTooltipText("UI_ModsConflicts_ButtonMoveDown_tooltip")
	self:addChild(self.down);
--[[
	self.infoBtn = ISButton:new(self.width - 10 - btnWid2, 15, btnWid2, btnHgt, getText("UI_InfoBtn"), self, ModOrderUI.onClick);
	self.infoBtn.internal = "INFO";
	self.infoBtn:initialise();
	self.infoBtn:instantiate();
	self.infoBtn:setAnchorLeft(true);
	self.infoBtn:setAnchorTop(false);
	self.infoBtn:setAnchorBottom(true);
	self.infoBtn.borderColor = { r = 1, g = 1, b = 1, a = 0.1 };
	self:addChild(self.infoBtn);
--]]
	self:populateList();

	self:setCapture(true)
end

function ModOrderUI:getActiveMods()
	return ActiveMods.getById(self.isNewGame and "currentGame" or "default")
end

function ModOrderUI:populateList()
	self.datas:clear();
	self.mapGroups:createGroups(self:getActiveMods(), false)
	self.mapGroups:checkMapConflicts()
	local list = self.mapGroups:getAllMapsInOrder()
	for i=1,list:size() do
		local mapName = list:get(i-1)
		local conflicts = self.mapGroups:getMapConflicts(mapName)
		if conflicts and not conflicts:isEmpty() then
			local item = {}
			item.name = mapName
			item.conflicts = conflicts
			self.datas:addItem(item.name, item)
		end
	end
end

function ModOrderUI:drawDatas(y, item, alt)
	local a = 0.9;
	
	--    self.parent.selectedFaction = nil;
	self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	
	if self.selected == item.index then
		self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
		self.parent.selectedItem = item.index;
	end

	self:drawText(item.text, 10, y + 2, 1, 1, 1, a, self.font);
	
	return y + self.itemheight;
end

function ModOrderUI:prerender()
	self:bringToTop();
	self:updateButtons();
	local z = 20;
	local splitPoint = 100;
	local x = 10;
	self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
	self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	self:drawText(getText("UI_mods_ModsOrder"), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, getText("UI_mods_ModsOrder")) / 2), z, 1,1,1,1, UIFont.Medium);
	z = z + 30;
	local item = self.datas.items[self.datas.selected]
	if item then
		local conflicts = item.item.conflicts
		for i=0, conflicts:size()-1 do
			self:drawText(conflicts:get(i), self.datas.x, (self.datas.y + self.datas.height + 10 + (i * 15)), 1, 1, 1, 1, self.font);
		end
	end
end

function ModOrderUI:render()
	ISPanel.render(self)
	self.richText:render(self.datas.x, 50, self)
end

function ModOrderUI:onMouseMoveOutside(dx, dy)
	return true
end

function ModOrderUI:onMouseDownOutside(x, y)
	return true
end

function ModOrderUI:onMouseUpOutside(x, y)
	return true
end

function ModOrderUI:updateButtons()
	self.up.enable = true;
	self.down.enable = true;
	if self.selectedItem == 1 then
		self.up.enable = false;
	end
	if self.selectedItem == #self.datas.items then
		self.down.enable = false;
	end
end

function ModOrderUI:onClick(button)
	if button.internal == "CANCEL" then
		self:setVisible(false);
		self:removeFromUIManager();
		ModSelector.instance:setVisible(true)
	end
	if button.internal == "SAVE" then
		local activeMods = self:getActiveMods()
		activeMods:getMapOrder():clear()
		for i=1,#self.datas.items do
			activeMods:getMapOrder():add(self.datas.items[i].text);
		end
		self:setVisible(false);
		self:removeFromUIManager();
		ModSelector.instance:setVisible(true)
	end
	if button.internal == "UP" then
		local item = self.datas.items[self.selectedItem];
		self.datas:removeItem(item.text);
		self.datas:insertItem(self.selectedItem - 1, item.text, item.item);
		self.datas.selected = self.selectedItem - 1
	end
	if button.internal == "DOWN" then
		local item = self.datas.items[self.selectedItem];
		self.datas:removeItem(item.text);
		self.datas:insertItem(self.selectedItem + 1, item.text, item.item);
		self.datas.selected = self.selectedItem + 1
	end
	if button.internal == "INFO" then
		if not self.infoRichText then
			self.infoRichText = ISModalRichText:new(getCore():getScreenWidth()/2-400,getCore():getScreenHeight()/2-300,600,600,getText("UI_ModsConflictsInfo"), false);
			self.infoRichText.destroyOnClick = false;
			self.infoRichText:initialise();
			self.infoRichText:addToUIManager();
			self.infoRichText.chatText:paginate();
			self.infoRichText.backgroundColor = {r=0, g=0, b=0, a=1};
			self.infoRichText:setHeight(self.infoRichText.chatText:getHeight() + 40);
			self.infoRichText:setY(getCore():getScreenHeight()/2-(self.infoRichText:getHeight()/2));
			self.infoRichText:setVisible(true);
			self.infoRichText:setAlwaysOnTop(true);
		else
			self.infoRichText:setVisible(not self.infoRichText:getIsVisible());
			self.infoRichText:bringToTop();
		end
	end
end

--************************************************************************--
--** ModOrderUI:new
--**
--************************************************************************--
function ModOrderUI:new(x, y, width, height)
	local o = {}
	x = ModSelector.instance.x;
	y = ModSelector.instance.y;
	width = ModSelector.instance.width
	height = ModSelector.instance.height
	o = ISPanel:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self
	o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
	o.backgroundColor = {r=0, g=0, b=0, a=0.8};
	o.width = width;
	o.height = height;
	o.selectedFaction = nil;
	o.moveWithMouse = true;
	o.mapGroups = MapGroups.new()
	o.isNewGame = ModSelector.instance.isNewGame
	ModOrderUI.instance = o;
	return o;
end
