--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "ISUI/ISPanel"
require "ISUI/ISButton"
require "ISUI/ISInventoryPane"
require "ISUI/ISResizeWidget"
require "ISUI/ISMouseDrag"

require "defines"

ModSelector = ISPanelJoypad:derive("ModSelector");

local FONT_HGT_TITLE = getTextManager():getFontHeight(UIFont.Title)
local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)
local FONT_HGT_LARGE = getTextManager():getFontHeight(UIFont.Large)

-----

ModListBox = ISScrollingListBox:derive("ModListbox")

function ModListBox:onMouseDown(x, y)
	-- stop you from changing mods while in mod order UI
	if not self.parent.modorderui or not self.parent.modorderui:isVisible() then
		if #self.items == 0 then return end
		local row = self:rowAt(x, y)
		if row > #self.items then
			row = #self.items
		end
		if row < 1 then
			row = 1
		end
		getSoundManager():playUISound("UISelectListItem")
		if self.mouseOverButton then
			self.parent:forceActivateMods(self.mouseOverButton.item.modInfo, not self.parent:isModActive(self.mouseOverButton.item.modInfo));
		else
			self.selected = row
		end
	end
end

function ModListBox:doDrawItem(y, item, alt)
	local modInfo = item.item.modInfo
	local tex = nil;
	local isMouseOver = self.mouseoverselected == item.index

	if self.selected == item.index then
		self:drawRect(0, (y), self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15);
	elseif isMouseOver and not self:isMouseOverScrollBar() then
		self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.95, 0.05, 0.05, 0.05);
	end

	-- border over text and description
	self:drawRectBorder(0, (y), self:getWidth(), item.height-1, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b);

	local r,g,b,a = 0.9, 0.9, 0.9, 0.9
	if not modInfo:isAvailable() then
		g = 0.0
		b = 0.0
	end
	self:drawText(modInfo:getName(), 10, y + 10, r, g, b, a, UIFont.Medium);

	local itemHgt = 10 + FONT_HGT_MEDIUM + 10

	local textDisabled = getText("UI_mods_ModDisabled")
	local textEnabled = getText("UI_mods_ModEnabled")
	local textDisabledWid = getTextManager():MeasureStringX(UIFont.Small, textDisabled)
	local textEnabledWid = getTextManager():MeasureStringX(UIFont.Small, textEnabled)
	local buttonWid = 8 + math.max(textEnabledWid, textDisabledWid) + 8
	local buttonHgt = FONT_HGT_MEDIUM
	local scrollBarWid = self:isVScrollBarVisible() and 13 or 0
	local buttonX = self.width - 16 - scrollBarWid - buttonWid
	local buttonY = y + 10
	local isMouseOverButton = isMouseOver and ((self:getMouseX() > buttonX - 16) and (self:getMouseX() < self.width - scrollBarWid) and (self:getMouseY() < buttonY + buttonHgt + 16))
	local isJoypadSelected = self.parent.hasJoypadFocus and self.selected == item.index
	
	if self.parent:isModActive(modInfo) then
		if isMouseOverButton then
			self:drawRect(buttonX, buttonY, buttonWid, buttonHgt, 1, 0, 0.85, 0)
			self.mouseOverButton = item
		else
			self:drawRect(buttonX, buttonY, buttonWid, buttonHgt, 1, 0, 0.70, 0)
		end
		self:drawTextCentre(textEnabled, buttonX +  buttonWid / 2, buttonY + (buttonHgt - FONT_HGT_SMALL) / 2 , 0, 0, 0, 1)
	elseif (isMouseOver and not self:isMouseOverScrollBar() or isJoypadSelected) and modInfo:isAvailable() then
		local rgb = (isMouseOverButton or isJoypadSelected) and 0.5 or 0.2
		self:drawRect(buttonX, buttonY, buttonWid, buttonHgt, 1, rgb, rgb, rgb)
		self:drawTextCentre(textDisabled, buttonX + buttonWid / 2, buttonY + (buttonHgt - FONT_HGT_SMALL) / 2 , 0, 0, 0, 1)
		self.mouseOverButton = isMouseOverButton and item or nil
	end

	if not modInfo:isAvailable() then
		local tex = ModSelector.instance.cantTexture
		self:drawTexture(tex, self.width - 16 - scrollBarWid - tex:getWidth(), y + (item.height - tex:getHeight()) / 2, 1, 1, 1, 1);
	end

	if self.parent.hasJoypadFocus and modInfo:isAvailable() and (self.selected == item.index) then
		local fontHgt = FONT_HGT_SMALL
		local tex = self.parent.abutton
		self:drawTextureScaled(tex, buttonX - 8 - buttonHgt, buttonY, buttonHgt, buttonHgt, 1,1,1,1)
	end
	
	y = y + itemHgt;

	return y;
end

function ModListBox:onJoypadDirRight(joypadData)
	self:setJoypadFocused(false, joypadData)
	joypadData.focus = self.parent.infoPanel
	updateJoypadFocus(joypadData)
end

function ModListBox:onJoypadBeforeDeactivate(joypadData)
	self.parent:onJoypadBeforeDeactivate(joypadData)
end

function ModListBox:new(x, y, width, height)
	local o = ISScrollingListBox.new(self, x, y, width, height)
	return o
end

-----

ModThumbnailPanel = ISPanelJoypad:derive("ModThumbnailPanel")

function ModThumbnailPanel:render()
	ISPanelJoypad.render(self)

	if self.modInfo and (self.modInfo:getPosterCount() > 0) then
		self.index = math.max(self.index, 1)
		self.index = math.min(self.index, self.modInfo:getPosterCount())
		local left = 1
		local top = 1
		local alpha = 1.0

		local width = self.thumbnailWidth
		local height = self.thumbnailHeight
		local x = 0
		local y = 0
		for i=1,self.modInfo:getPosterCount() do
			local tex = getTexture(self.modInfo:getPoster(i-1)) or Texture:getWhite()
			if tex == Texture.getWhite() then alpha = 0.1 end
			self:drawRect(x + self.padX, y + self.padY, width, height, 1, 0.1, 0.1, 0.1)
			self:drawTextureScaledAspect(tex, x + self.padX, y + (self.height - height) / 2, width, height, alpha, 1, 1, 1)

			if not self.pressed and self:isMouseOver() and self:getIndexAt(self:getMouseX(), self:getMouseY()) + 1 == i then
				self:drawRectBorder(x + self.padX - 2, y + self.padY - 2, width + 4, height + 4, 1, 0.5, 0.5, 0.5)
			end
			
			x = x + self.padX + width
		end

		if x > self.width then
			self:setXScroll(math.min(self:getXScroll(), 0))
			self:setXScroll(math.max(self:getXScroll(), -(x - self.width)))
		else
			self:setXScroll(0)
		end
	end
end

function ModThumbnailPanel:onMouseDown(x, y)
	self.index = self:getIndexAt(x, y) + 1
	self.pressed = true
end

function ModThumbnailPanel:onMouseUp(x, y)
	self.pressed = false
end

function ModThumbnailPanel:onMouseUpOutside(x, y)
	self.pressed = false
end

function ModThumbnailPanel:onMouseMove(dx, dy)
	if self.pressed then
		self:setXScroll(self:getXScroll() + dx)
	end
end

function ModThumbnailPanel:onMouseMoveOutside(dx, dy)
	if self.pressed then
		self:onMouseMove(dx, dy)
	end
end

function ModThumbnailPanel:getIndexAt(x, y)
	if not self.modInfo or self.modInfo:getPosterCount() == 0 then
		return -1
	end
	local index = (x - 5) / (self.thumbnailWidth + 10)
	index = math.floor(index)
	if index >= self.modInfo:getPosterCount() then
		return -1
	end
	return index
end

function ModThumbnailPanel:setJoypadFocused(focused)
end

function ModThumbnailPanel:setModInfo(modInfo)
	self.modInfo = modInfo
end

function ModThumbnailPanel:new(x, y, width, height)
	local o = ISPanelJoypad.new(self, x, y, width, height)
	o.padX = 10
	o.padY = 8
	o.thumbnailWidth = 116
	o.thumbnailHeight = height - o.padY * 2 -- 64
	o.index = 1
	return o
end

-----

ModPosterPanel = ISPanelJoypad:derive("ModPosterPanel")

function ModPosterPanel:render()
	ISPanelJoypad.render(self)

	if self.modInfo and (self.modInfo:getPosterCount() > 0) then
		local index = self.parent.thumbnailPanel.index
		index = math.max(index, 1)
		index = math.min(index, self.modInfo:getPosterCount())
		local texName = self.modInfo:getPoster(index - 1)
		local tex = getTexture(texName) or Texture.getWhite()
		local left = 1
		local top = 1
		local alpha = 1.0
		if tex == Texture.getWhite() then alpha = 0.1 end
		local scrollBarWid = (self:getScrollHeight() > self.height) and self.vscroll:getWidth() or 0
		self:drawTextureScaledAspect(tex, left, top, self.width - scrollBarWid - left, self.height - 1 - top, alpha, 1, 1, 1)
	end
end

function ModPosterPanel:setJoypadFocused(focused)
end

function ModPosterPanel:setModInfo(modInfo)
	self.modInfo = modInfo
end

function ModPosterPanel:new(x, y, width, height)
	local o = ISPanelJoypad.new(self, x, y, width, height)
	return o
end

-----

ModInfoPanel = ISPanelJoypad:derive("ModInfoPanel")

function ModInfoPanel:createChildren()
	local scrollBarWid = 13

	local panel = ModPosterPanel:new(0, 0, self.width - scrollBarWid, 360)
	panel:setAnchorRight(true)
	self:addChild(panel)
	self.posterPanel = panel

	panel = ModThumbnailPanel:new(0, self.posterPanel:getBottom() - 1, self.width - scrollBarWid, 80)
	panel:setAnchorRight(true)
	self:addChild(panel)
	self.thumbnailPanel = panel

	local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
	local buttonWid = 150

	local button1 = ISButton:new(self.width - scrollBarWid - 16 - buttonWid, panel:getBottom() + 24, buttonWid, buttonHgt, getText("UI_mods_ModEnable"), ModSelector.instance, ModSelector.onOptionMouseDown)
	button1.internal = "TOGGLE"
	button1:initialise()
	button1:instantiate()
	button1:setAnchorLeft(false)
	button1:setAnchorRight(true)
	button1:setAnchorTop(false)
	button1:setAnchorBottom(false)
	button1.borderColor = {r=1, g=1, b=1, a=0.1}
--	button1:setFont(UIFont.Small)
	self:addChild(button1)
	self.buttonToggle = button1

	local button2 = ISButton:new(button1.x - 20 - buttonWid, button1.y, buttonWid, buttonHgt, getText("UI_mods_ModOptions"), ModSelector.instance, ModSelector.onOptionMouseDown)
	button2.internal = "OPTIONS"
	button2:initialise()
	button2:instantiate()
	button2:setAnchorLeft(false)
	button2:setAnchorRight(true)
	button2:setAnchorTop(false)
	button2:setAnchorBottom(false)
	button2.borderColor = {r=1, g=1, b=1, a=0.1}
--	button2:setFont(UIFont.Small)
	button2:setEnable(false)
	button2:setVisible(false)
	self:addChild(button2)
	self.buttonOptions = button2

	local richText = ISRichTextLayout:new(self.width - scrollBarWid)
	self.richText = richText

	self.urlButton = ISButton:new(16, 0, self.width - scrollBarWid - 16 * 2, buttonHgt, getText("UI_mods_OpenWebBrowser"), ModSelector.instance, ModSelector.onOptionMouseDown)
	self.urlButton.internal = "URL"
	self.urlButton.url = ""
	self.urlButton:initialise()
	self.urlButton:instantiate()
	self.urlButton:setAnchorLeft(true)
	self.urlButton:setAnchorRight(true)
	self.urlButton:setAnchorTop(false)
	self.urlButton:setAnchorBottom(false)
	self.urlButton.borderColor = {r=1, g=1, b=1, a=0.1}
--	self.urlButton:setFont(UIFont.Small)
	self.urlButton:setEnable(false)
	self:addChild(self.urlButton)

	local label = ISLabel:new(richText.marginLeft, 0, FONT_HGT_SMALL + 2 * 2, getText("UI_mods_Location"), 1.0, 1.0, 1.0, 1.0, UIFont.Small, true)
	label:setColor(0.7, 0.7, 0.7)
	self:addChild(label)
	self.locationLabel = label
	
	local entryX = label:getRight() + 6
	local entry = ISTextEntryBox:new("", entryX, 0, self.width - scrollBarWid - 16 - entryX, FONT_HGT_SMALL + 2 * 2)
	entry:setAnchorRight(true)
	self:addChild(entry)
	entry:setEditable(false)
	entry:setSelectable(true)
	self.locationEntry = entry

	local y = self.buttonToggle:getBottom() + 16
	self.locationLabel:setY(y)
	self.locationEntry:setY(y)
	self.urlButton:setY(self.locationEntry:getBottom() + 24)
	self:setScrollHeight(self.urlButton:getBottom() + 20)

    self:insertNewLineOfButtons(self.posterPanel)
    self:insertNewLineOfButtons(self.thumbnailPanel)
    self:insertNewLineOfButtons(self.buttonToggle)
    self.joypadIndexY = 1
    self.joypadIndex = 1
end

function ModInfoPanel:onMouseWheel(del)
	self:setYScroll(self:getYScroll() - (del * 40))
	return true
end

function ModInfoPanel:prerender()
	local scrollBarWid = 13
	if self.width - scrollBarWid ~= self.richText.width then
		self.richText:setWidth(self.width - scrollBarWid)
		self.richText:paginate()
	end
	
	self:setStencilRect(0, 0, self:getWidth(), self:getHeight())
	ISPanelJoypad.prerender(self)
end

function ModInfoPanel:render()
	ISPanelJoypad.render(self)

	local x = 0
	local y = self.buttonToggle:getBottom() + 16
	self.richText:render(x, y, self)

    self:clearStencilRect()

	self:drawRectBorderStatic(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b)

	if self.joyfocus then
		self:drawRectBorder(0, -self:getYScroll(), self:getWidth(), self:getHeight(), 0.4, 0.2, 1.0, 1.0);
		self:drawRectBorder(1, 1-self:getYScroll(), self:getWidth()-2, self:getHeight()-2, 0.4, 0.2, 1.0, 1.0);
	end
end

function ModInfoPanel:setModInfo(modInfo)
	if modInfo == self.modInfo then return end
	self.modInfo = modInfo

	self.posterPanel:setModInfo(modInfo)
	self.thumbnailPanel:setModInfo(modInfo)

	local description = " <H2> " .. modInfo:getName() .. " <TEXT> "
	description = description .. " <LINE> "
	description = description .. " <LINE> "

	if modInfo:getDescription() ~= "" then
		description = description .. modInfo:getDescription()
		description = description .. " <LINE> "
		description = description .. " <LINE> "
	end

	local needBlank = false

	local gameVersion = getCore():getGameVersion()

	if modInfo:getVersionMin() and modInfo:getVersionMin():isGreaterThan(gameVersion) then
		description = description .. " <RED> " .. getText("UI_mods_RequiredVersionMin", modInfo:getVersionMin():toString())
		description = description .. " <LINE> "
		needBlank = true
	end

	if modInfo:getVersionMax() and modInfo:getVersionMax():isLessThan(gameVersion) then
		description = description .. " <RED> " .. getText("UI_mods_RequiredVersionMax", modInfo:getVersionMax():toString())
		description = description .. " <LINE> "
		needBlank = true
	end

	if modInfo:getRequire() and not modInfo:getRequire():isEmpty() then
		if needBlank then
			description = description .. " <LINE> "
		end
		description = description .. " <TEXT> " .. getText("UI_mods_require")
		description = description .. " <LINE> <INDENT:20> "
		for i=1,modInfo:getRequire():size() do
			local modID = modInfo:getRequire():get(i-1)
			local modInfo1 = getModInfoByID(modID)
			if modInfo1 == nil then
				description = description .. " <RED> " .. modID
			elseif not modInfo1:isAvailable() then
				description = description .. " <RED> " .. modInfo1:getName()
			else
				description = description .. " <TEXT> " .. modInfo1:getName()
			end
			description = description .. " <LINE> "
		end
		description = description .. " <INDENT:0> "
		needBlank = true
	end

	if needBlank then
		description = description .. " <LINE> "
	end

	description = description .. " <TEXT> " .. getText("UI_mods_ID", modInfo:getId())
	description = description .. " <LINE> "

	if getSteamModeActive() and modInfo:getWorkshopID() then
		description = description .. getText("UI_WorkshopSubmit_ItemID") .. " " .. modInfo:getWorkshopID()
		description = description .. " <LINE> "
	end

	self.richText:setText(description)
	self.richText:paginate()

	local y = self.buttonToggle:getBottom() + 16 + self.richText:getHeight()
	self.locationLabel:setY(y)
	self.locationEntry:setY(y)
	self.locationEntry:setText(modInfo:getDir())
	
	self.urlButton:setY(self.locationEntry:getBottom() + 24)

	self:setScrollHeight(self.urlButton:getBottom() + 20)
end

function ModInfoPanel:onJoypadDirLeft(joypadData)
	self.parent.listbox:setJoypadFocused(true, joypadData)
end

function ModInfoPanel:onLoseJoypadFocus(joypadData)
	self:clearJoypadFocus()
	ISPanelJoypad.onLoseJoypadFocus(self, joypadData)
end

function ModInfoPanel:onJoypadBeforeDeactivate(joypadData)
	self.parent:onJoypadBeforeDeactivate(joypadData)
end

function ModInfoPanel:new(x, y, width, height)
	local o = ISPanelJoypad.new(self, x, y, width, height)
	o.backgroundColor.a = 1.0
	return o
end

-----

function ModSelector:initialise()
    ISPanelJoypad.initialise(self);
end

function ModSelector:getActiveMods()
	if self.loadGameFolder then
		return ActiveMods.getById("currentGame")
	end
	return ActiveMods.getById(self.isNewGame and "currentGame" or "default")
end

function ModSelector:isModActive(modInfo)
	return self:getActiveMods():isModActive(modInfo:getId())
end

function ModSelector:onDblClickMap(item)
	if not self.modorderui or not self.modorderui:isVisible() then
		if self.listbox:isMouseOverScrollBar() then return end
		if self.listbox.mouseOverButton then
			item = self.listbox.mouseOverButton.item
		end
		self:forceActivateMods(item.modInfo, not self:isModActive(item.modInfo));
	end
end

function ModSelector:forceActivateMods(modInfo, activate)
	if modInfo:isAvailable() then
		self:getActiveMods():setModActive(modInfo:getId(), activate);
		-- we also activate the required mod if needed
		if self:isModActive(modInfo) and modInfo:getRequire() then
			for l=0,modInfo:getRequire():size() - 1 do
				for i,k in ipairs(self.listbox.items) do
					local modInfo2 = k.item.modInfo
					if modInfo2:getId() and modInfo2:getId():trim() == modInfo:getRequire():get(l):trim() then
						self:forceActivateMods(modInfo2, self:isModActive(modInfo));
					end
				end
			end
		end
	end
	-- check the "parents" mod to disable
	if not self:isModActive(modInfo) then
		for i,activatedMod in ipairs(self.listbox.items) do
			local modInfo2 = activatedMod.item.modInfo
			if self:isModActive(modInfo2) and modInfo2:getRequire() then
				for l=0,modInfo2:getRequire():size() - 1 do
					if modInfo:getId() == modInfo2:getRequire():get(l):trim() then
						self:forceActivateMods(modInfo2, false);
					end
				end
			end
		end
	end
	self.mapGroups:createGroups(self:getActiveMods(), false)
	self.mapConflicts = self.mapGroups:checkMapConflicts()
end

function ModSelector:onModsEnabledTick(option, selected)
	getCore():setOptionModsEnabled(selected)
end

--************************************************************************--
--** ModSelector:instantiate
--**
--************************************************************************--
function ModSelector:instantiate()
    self.javaObject = UIElement.new(self);
    self.javaObject:setX(self.x);
    self.javaObject:setY(self.y);
    self.javaObject:setHeight(self.height);
    self.javaObject:setWidth(self.width);
    self.javaObject:setAnchorLeft(self.anchorLeft);
    self.javaObject:setAnchorRight(self.anchorRight);
    self.javaObject:setAnchorTop(self.anchorTop);
    self.javaObject:setAnchorBottom(self.anchorBottom);
end

function ModSelector:populateListBox(directories)
	self.listbox:clear();
	local modIDs = {}
	for _,directory in ipairs(directories) do
		local modInfo = getModInfo(directory)
		-- Display the first mod with a given ID.
		-- There may be mods in User\Zomboid\mods, User\Zomboid\Workshop\,
		-- and steamapps\workshop\content\108600\.  The order can be changed
		-- using the -modfolders commandline option.
		if modInfo and not modIDs[modInfo:getId()] then
			local item = {}
			item.modInfo = modInfo
			self.listbox:addItem("", item)
			modIDs[modInfo:getId()] = true
		end
	end

	for i,k in ipairs(self.listbox.items) do
		local modInfo = k.item.modInfo
		if self:isModActive(modInfo) and not modInfo:isAvailable() then
			self:getActiveMods():setModActive(modInfo:getId(), false)
		end
	end

	self.ModsEnabled = getCore():getOptionModsEnabled()
	table.sort(self.listbox.items, function(a,b)
			return not string.sort(a.item.modInfo:getName(), b.item.modInfo:getName())
		end)
	self.mapGroups:createGroups(self:getActiveMods(), false)
	self.mapConflicts = self.mapGroups:checkMapConflicts()
end

function ModSelector:setExistingSavefile(folder)
	self.loadGameFolder = folder
	local info = getSaveInfo(folder)
	local activeMods = info.activeMods or ActiveMods.getById("default")
	ActiveMods.getById("currentGame"):copyFrom(activeMods)

	-- Remember the list of map directories.  The user isn't allowed to change
	-- these by enabling/disabling mods.
	self.loadGameMapName = info.mapName or 'Muldraugh, KY'
end

function ModSelector:create()
    local labelHgt = FONT_HGT_SMALL
    self.smallFontHgt = labelHgt
    local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

    self.topRect = {}
    self.topRect.x = 16
    self.topRect.y = 10 + FONT_HGT_TITLE + 10
    self.topRect.h = 8 + buttonHgt + 8
   
    local listY = self.topRect.y + self.topRect.h + 10
    local listHgt = self.height-(5 + buttonHgt + 10)-listY
    self.listbox = ModListBox:new(16, listY, self.width/2-16, listHgt);
    self.listbox:initialise();
    self.listbox:instantiate();
    self.listbox:setAnchorLeft(true);
    self.listbox:setAnchorRight(true);
    self.listbox:setAnchorTop(true);
    self.listbox:setAnchorBottom(true);
    self.listbox.itemheight = 128;
    self.listbox.drawBorder = true
    self.listbox:setOnMouseDoubleClick(self, ModSelector.onDblClickMap);
    self:addChild(self.listbox);

    self.backButton = ISButton:new(16, self.height-buttonHgt - 5, 100, buttonHgt, getText("UI_btn_back"), self, ModSelector.onOptionMouseDown);
    self.backButton.internal = "DONE";
    self.backButton:initialise();
    self.backButton:instantiate();
    self.backButton:setAnchorLeft(true);
    self.backButton:setAnchorRight(false);
    self.backButton:setAnchorTop(false);
    self.backButton:setAnchorBottom(true);
    self.backButton.borderColor = {r=1, g=1, b=1, a=0.1};
    self.backButton:setFont(UIFont.Small);
    self.backButton:ignoreWidthChange();
    self.backButton:ignoreHeightChange();
    self:addChild(self.backButton);

    local size = getTextManager():MeasureStringX(UIFont.Small, getText("UI_mods_GetModsHere"));
    local size = math.max(size + 10 * 2, 100)
    self.getModButton = ISButton:new(self.width - 16 - 16 - size, self.topRect.y + 8, size, buttonHgt, getText("UI_mods_GetModsHere"), self, ModSelector.onOptionMouseDown);
    self.getModButton.internal = "GETMOD";
    self.getModButton:initialise();
    self.getModButton:instantiate();
    self.getModButton:setAnchorLeft(false);
    self.getModButton:setAnchorRight(true);
    self.getModButton:setAnchorTop(true);
    self.getModButton:setAnchorBottom(false);
    self.getModButton.borderColor = {r=1, g=1, b=1, a=1};
    self.getModButton.backgroundColor = {r=0, g=0.5, b=0.75, a=1.0};
    self.getModButton.backgroundColorMouseOver = {r=0, g=0.65, b=0.85, a=1.0};
    self.getModButton:setFont(UIFont.Small);
    self.getModButton:ignoreWidthChange();
    self.getModButton:ignoreHeightChange();
	if not getSteamModeActive() then
		self.getModButton.tooltip = getText("UI_mods_WorkshopRequiresSteam")
	end
    self:addChild(self.getModButton);

	self.modOrderbtn = ISButton:new(self.width - 16, self.height - buttonHgt - 5, 100, buttonHgt, getText("UI_mods_ModsOrder"), self, ModSelector.onOptionMouseDown);
	self.modOrderbtn.internal = "MODSORDER";
	self.modOrderbtn:initialise();
	self.modOrderbtn:instantiate();
	self.modOrderbtn:setAnchorLeft(false);
	self.modOrderbtn:setAnchorRight(true);
	self.modOrderbtn:setAnchorTop(false);
	self.modOrderbtn:setAnchorBottom(true);
	self.modOrderbtn.borderColor = {r=1, g=1, b=1, a=0.1};
	self.modOrderbtn:setFont(UIFont.Small);
	self.modOrderbtn:ignoreWidthChange();
	self.modOrderbtn:ignoreHeightChange();
	self:addChild(self.modOrderbtn);
	self.modOrderbtn:setWidthToTitle()
	self.modOrderbtn:setX(self.width - 16 - self.modOrderbtn.width)

--[[
	local checkBox = ISTickBox:new(16, self.height - 30, 200, 30, "Enable mods", self, ModSelector.onModsEnabledTick)
	checkBox:initialise()
	checkBox:setAnchorTop(false)
	checkBox:setAnchorBottom(true)
	checkBox:addOption("Enable mods", nil)
	checkBox.leftMargin = 0
	checkBox.selected[1] = getCore():getOptionModsEnabled()
	self:addChild(checkBox)
--]]

	local left = self.listbox:getRight() + 16
	local top = self.listbox:getY()
	local panel = ModInfoPanel:new(left, top, self.width - 16 - left, self.listbox.height)
	panel:setAnchorBottom(true)
	self:addChild(panel)
	panel:addScrollBars()
	panel:setScrollChildren(true)
	self.infoPanel = panel
	self.urlButton = self.infoPanel.urlButton
end

function ModSelector:prerender()
	ModSelector.instance = self
	self:updateButtons();
    self.listbox.mouseOverButton = nil
    ISPanelJoypad.prerender(self);
	if self.listbox.items and self.listbox.items[self.listbox.selected] then
		self.infoPanel:setModInfo(self.listbox.items[self.listbox.selected].item.modInfo)
	end
    self:drawTextCentre(getText("UI_mods_SelectMods"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title);
    self:drawRect(self.topRect.x, self.topRect.y, self.width - 16 * 2, self.topRect.h, 1, 0.0, 0.5, 0.75)
    local labelY = self.topRect.y + (self.topRect.h - FONT_HGT_SMALL) / 2
    local text = getText("UI_mods_Explanation") .. Core.getMyDocumentFolder() .. getFileSeparator() .. "mods" .. getFileSeparator()
    self:drawText(text, self.topRect.x + 16, labelY, 1, 1, 1, 1, UIFont.Small);
end

function ModSelector:updateButtons()
	local item = self.listbox.items[self.listbox.selected]
	if item then
		local modInfo = item.item.modInfo
		self.infoPanel.buttonToggle:setEnable(modInfo:isAvailable())
		self.infoPanel.buttonToggle:setTitle(getText(self:isModActive(modInfo) and "UI_mods_ModDisable" or "UI_mods_ModEnable"))
		self.infoPanel.buttonOptions:setEnable(false)
		if modInfo:getWorkshopID() and isSteamOverlayEnabled() then
			self.urlButton:setEnable(true);
			self.urlButton.workshopID = modInfo:getWorkshopID()
			self.urlButton:setTitle(getText("UI_WorkshopSubmit_OverlayButton"))
		elseif modInfo:getUrl() and modInfo:getUrl() ~= "" then
			self.urlButton:setVisible(true);
			self.urlButton.workshopID = nil
			self.urlButton.url = modInfo:getUrl();
--			self.urlButton:setTitle("URL : " .. modInfo:getUrl());
			self.urlButton:setEnable(true);
			self.urlButton:setTitle(getText("UI_mods_OpenWebBrowser"))
		else
			self.urlButton:setTitle(getText("UI_mods_OpenWebBrowser"))
			self.urlButton:setEnable(false);
		end
	else
		self.infoPanel.buttonToggle:setEnable(false)
		self.infoPanel.buttonOptions:setEnable(false)
	end

	self.modOrderbtn.enable = self.mapConflicts
	if self.modorderui and self.modorderui:isReallyVisible() then
		self.modOrderbtn.blinkBG = false
		self.modOrderbtn.tooltip = nil
	else
		self.modOrderbtn.blinkBG = self.mapConflicts
		self.modOrderbtn.tooltip = self.mapConflicts and getText("UI_mods_ConflictDetected") or nil
	end
end

function ModSelector:onAccept()
	if self.modorderui then
		self.modorderui:removeFromUIManager()
	end
	self:setVisible(false)

	local activeMods = self:getActiveMods()
	-- Remove mod IDs for missing mods from ActiveMods.mods
	activeMods:checkMissingMods()
	-- Remove unused map directories from ActiveMods.mapOrder
	activeMods:checkMissingMaps()

	if self.loadGameFolder then
		local saveFolder = self.loadGameFolder
		self.loadGameFolder = nil
		manipulateSavefile(saveFolder, "WriteModsDotTxt")

		-- Setting 'currentGame' to 'default' in case other places forget to set it
		-- before starting a game (DebugScenarios.lua, etc).
		local defaultMods = ActiveMods.getById("default")
		local currentMods = ActiveMods.getById("currentGame")
		currentMods:copyFrom(defaultMods)

		LoadGameScreen.instance:onSavefileModsChanged(saveFolder)
		LoadGameScreen.instance:setVisible(true, self.joyfocus)
		return
	end

	if self.isNewGame then
		NewGameScreen.instance:setVisible(true, self.joyfocus)
	else
		saveModsFile()

		-- Setting 'currentGame' to 'default' in case other places forget to set it
		-- before starting a game (DebugScenarios.lua, etc).
		local defaultMods = ActiveMods.getById("default")
		local currentMods = ActiveMods.getById("currentGame")
		currentMods:copyFrom(defaultMods)

		MainScreen.instance.bottomPanel:setVisible(true)
		if self.joyfocus then
			self.joyfocus.focus = MainScreen.instance
			updateJoypadFocus(self.joyfocus)
		end
	end

	local reset = self.ModsEnabled ~= getCore():getOptionModsEnabled()
	if ActiveMods.requiresResetLua(activeMods) then
		reset = true
	end
	if reset then
		if self.isNewGame then
			getCore():ResetLua("currentGame", "NewGameMods")
		else
			getCore():ResetLua("default", "modsChanged")
		end
	end
end

function ModSelector:onOptionMouseDown(button, x, y)
	if button.internal == "DONE" then
		self:onAccept()
	elseif button.internal == "TOGGLE" then
		local item = self.listbox.items[self.listbox.selected].item
		self:forceActivateMods(item.modInfo, not self:isModActive(item.modInfo))
	elseif button.internal == "URL" then
		if button.workshopID then
			activateSteamOverlayToWorkshopItem(button.workshopID)
		else
			openUrl(button.url);
		end
    elseif button.internal == "GETMOD" then
		if getSteamModeActive() then
			if isSteamOverlayEnabled() then
				activateSteamOverlayToWorkshop()
			else
				openUrl("steam://url/SteamWorkshopPage/108600")
			end
		else
		openUrl("http://theindiestone.com/forums/index.php/forum/58-mods/");
		end
    elseif button.internal == "MODSORDER" then
		self:setVisible(false)
		self.modorderui = ModOrderUI:new(0, 0, 700, 400);
		self.modorderui:initialise();
		self.modorderui:addToUIManager();
	end
end

function ModSelector:onGainJoypadFocus(joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData);
    self.listbox:setISButtonForB(self.backButton);
    self.infoPanel:setISButtonForB(self.backButton);
--    self.listbox:setJoypadFocused(true, joypadData);
	self.hasJoypadFocus = true
    joypadData.focus = self.listbox;
end

function ModSelector:onResolutionChange(oldw, oldh, neww, newh)
	self.listbox:setWidth(self:getWidth() / 2 - self.listbox:getX())
	self.listbox:recalcSize()
	self.listbox.vscroll:setX(self.listbox:getWidth() - 16)
	local urlX = self:getWidth() / 2 + 16

	self.infoPanel:setWidth(self.width - 20 - urlX)
	self.infoPanel:setX(urlX)
end

function ModSelector:onJoypadBeforeDeactivate(joypadData)
	self.backButton:clearJoypadButton()
	self.hasJoypadFocus = false
	-- focus is on listbox or infoPanel
	self.joyfocus = nil
end

function ModSelector:new(x, y, width, height)
    local o = {}
    --o.data = {}
    o = ISPanelJoypad:new(x, y, width, height);
    ModSelector.instance = o;
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.backgroundColor = {r=0, g=0, b=0, a=0.3};
    o.borderColor = {r=1, g=1, b=1, a=0.2};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.itemheightoverride = {}
    o.tickTexture = getTexture("Quest_Succeed");
	o.cantTexture = getTexture("Quest_Failed");
	o.abutton =  Joypad.Texture.AButton;
    o.selected = 1;
    o.mapGroups = MapGroups.new()
    return o
end

function ModSelector_onModsModified()
	local self = ModSelector.instance
	if self and self.listbox and self:isReallyVisible() then
		local index = self.listbox.selected
		self:populateListBox(getModDirectoryTable())
		if self.listbox.items[index] then
			self.listbox.selected = index
		end
	end
end

Events.OnModsModified.Add(ModSelector_onModsModified)

-----

function ModSelector.showNagPanel()
	if getCore():isModsPopupDone() then
		return
	end
	getCore():setModsPopupDone(true)

	ModSelector.instance:setVisible(false)

	local width = 650
	local height = 400
	local nagPanel = ISModsNagPanel:new(
		(getCore():getScreenWidth() - width)/2,
		(getCore():getScreenHeight() - height)/2,
		width, height)
	nagPanel:initialise()
	nagPanel:addToUIManager()
	nagPanel:setAlwaysOnTop(true)
	local joypadData = JoypadState.getMainMenuJoypad()
	if joypadData then
		joypadData.focus = nagPanel
		updateJoypadFocus(joypadData)
	end
end
