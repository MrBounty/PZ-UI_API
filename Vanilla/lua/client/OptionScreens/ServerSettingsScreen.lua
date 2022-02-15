--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanelJoypad"
require "ISUI/ISComboBox"
require "ISUI/ISScrollingListBox"
require "ISUI/ISTextEntryBox"

ServerSettingsScreen = ISPanelJoypad:derive("ServerSettingsScreen")
local Page1 = ISPanelJoypad:derive("Page1")
local Page2 = ISPanelJoypad:derive("Page2")
local Page3 = ISPanelJoypad:derive("Page3")
local Page4 = ISPanelJoypad:derive("Page4")
local Page5 = ISPanelJoypad:derive("Page5")
local Page6 = ISPanelJoypad:derive("Page6")
local Page7 = ISPanelJoypad:derive("Page7")
local ServerSettingsScreenPanel = ISPanelJoypad:derive("ServerSettingsScreenPanel")
local SpawnRegionsPanel = ISPanelJoypad:derive("SpawnRegionsPanel")
local SpawnRegionsListBox = ISScrollingListBox:derive("SpawnRegionsListBox")
local SpawnPointsListBox = ISScrollingListBox:derive("SpawnPointsListBox")
local ServerSettingsScreenGroupBox = ServerSettingsScreenPanel:derive("ServerSettingsScreenGroupBox")
local ServerSettingsScreenModsPanel = ISPanelJoypad:derive("ServerSettingsScreenModsPanel")
local ServerSettingsScreenModsListBox = ISScrollingListBox:derive("ServerSettingsScreenModsListBox")
local ServerSettingsScreenMapsPanel = ISPanelJoypad:derive("ServerSettingsScreenMapsPanel")
local ServerSettingsScreenMapsListBox = ISScrollingListBox:derive("ServerSettingsScreenMapsListBox")
local ServerSettingsScreenWorkshopPanel = ISPanelJoypad:derive("ServerSettingsScreenWorkshopPanel")
local ServerSettingsScreenWorkshopListBox = ISScrollingListBox:derive("ServerSettingsScreenWorkshopListBox")
local SandboxPresetPanel = ISPanelJoypad:derive("SandboxPresetPanel")

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

local function getTooltipText(name)
	local tooltip = getTextOrNull(name)
	if tooltip then
		tooltip = tooltip:gsub("\\n", "\n")
		tooltip = tooltip:gsub("\\\"", "\"")
	end
	return tooltip
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function ServerSettingsScreenPanel:prerender()
	ISPanelJoypad.prerender(self)
	if not self.entryText then
		self.entryText = {}
	end
	local nonDefaultOptions = self._instance.nonDefaultOptions
	for _,settingName in ipairs(self.settingNames) do
		local label = self.labels[settingName]
		local control = self.controls[settingName]
		if label and control then
			label:setColor(1, 1, 1)
			local option = nonDefaultOptions:getOptionByName(settingName)
			if option and (option:getValue() ~= option:getDefaultValue()) then
				label:setColor(1, 1, 0)
			end
		end
		if control and control.Type == "ISTextEntryBox" then
			local text = control:getText()
			if text ~= self.entryText[settingName] then
				self.entryText[settingName] = text
				local option = getSandboxOptions():getOptionByName(settingName)
				if option then
					if option:isValidString(text) then
						control.borderColor.a = 1
						control.borderColor.g = 0.4
						control.borderColor.b = 0.4
					else
						control.borderColor.a = 0.9
						control.borderColor.g = 0.0
						control.borderColor.b = 0.0
					end
					--[[
					if text == tostring(option:getDefaultValue()) then
						self.labels[settingName].r = 1
						self.labels[settingName].g = 1
					else
						self.labels[settingName].r = 0.2
						self.labels[settingName].g = 0.2
					end
					--]]
				end
			end
		end
	end
	local x1,y1,x2,y2 = 1,1,self.width-1,self.height-1
	if self.isGroupBoxContentsPanel then
		y1 = self.parent.tickBox:getHeight() / 2
		y2 = self.height-6
	end
	self:setStencilRect(x1, y1, x2 - x1 + 1, y2 - y1 + 1)
end

function ServerSettingsScreenPanel:render()
	self:drawRectBorderStatic(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b)
	ISPanelJoypad.render(self)
	self:clearStencilRect()
	if self.isGroupBoxContentsPanel then
		self:repaintStencilRect(0, 0, self.width, self.height)
	end
end

function ServerSettingsScreenPanel:onMouseWheel(del)
	if self:getScrollHeight() > 0 then
		local children = self:getChildren()
		for _,child in pairs(children) do
			if child:getHeight() < child:getScrollHeight() and child:isMouseOver() then
				return false
			end
		end
		self:setYScroll(self:getYScroll() - (del * 40))
		return true
	end
	return false
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function ServerSettingsScreenGroupBox:new(x, y, width, height, tickBoxLabel)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.tickBoxLabel = tickBoxLabel
	o.settings = nil
	o.category = nil
	return o
end

function ServerSettingsScreenGroupBox:createChildren()
	local tickBox = ISTickBox:new(20, 4, 100, entryHgt, "", self, self.onTicked)
	tickBox.backgroundColor.a = 1
	tickBox.background = true
	tickBox.choicesColor = {r=1, g=1, b=1, a=1}
	tickBox.leftMargin = 2
	tickBox:setFont(UIFont.Medium)
	tickBox:addOption(self.tickBoxLabel)
	tickBox:setWidthToFit()
	tickBox.selected[1] = true
	self.tickBox = tickBox

	local scrollBarWidth = 13
	local cover = ISPanel:new(13, tickBox:getBottom(), self:getWidth() - 13 * 2 - scrollBarWidth, self:getHeight() - 13 - tickBox:getBottom())
	cover.borderColor.a = 0
	cover:setAnchorRight(true)
	cover:setAnchorBottom(true)
	cover:initialise()
	cover:instantiate()
	cover.javaObject:setConsumeMouseEvents(true)
	self.cover = cover

	local groupY = tickBox:getY() + tickBox:getHeight() / 2
	local contents = ServerSettingsScreenPanel:new(12, groupY, self:getWidth() - 12 * 2, self:getHeight() - 12 - groupY)
	contents.borderColor.a = 0.6
	contents:setAnchorRight(true)
	contents:setAnchorBottom(true)
	contents.settingNames = {}
	contents.isGroupBoxContentsPanel = true
	contents._instance = self._instance
	self.contents = contents

	self:addChild(contents)
	self:addChild(cover)
	self:addChild(tickBox)
end

function ServerSettingsScreenGroupBox:onTicked(index, selected)
	if selected then
		local options = (self.category == "INI") and self.settings:getServerOptions() or self.settings:getSandboxOptions()
		for _,settingName in ipairs(self.settingNames) do
			local option = options:getOptionByName(settingName)
			local control = self.controls[settingName]
			if control.Type == "ISComboBox" then
				control.selected = option:getDefaultValue()
			elseif control.Type == "ISTickBox" then
				control.selected[1] = option:getDefaultValue()
			else
				error "unhandled control type"
			end
		end
		self.cover:setVisible(true)
	else
		self.cover:setVisible(false)
	end
end

function ServerSettingsScreenGroupBox:settingsToUI(settings, category)
	self.settings = settings
	self.category = category
	local options = (self.category == "INI") and self.settings:getServerOptions() or self.settings:getSandboxOptions()
	local allDefault = true
	for _,settingName in ipairs(self.settingNames) do
		local option = options:getOptionByName(settingName)
		local control = self.controls[settingName]
		if control.Type == "ISComboBox" then
			if control.selected ~= option:getDefaultValue() then
				allDefault = false
				break
			end
		elseif control.Type == "ISTickBox" then
			if control.selected[1] ~= option:getDefaultValue() then
				allDefault = false
				break
			end
		else
			error "unhandled control type"
		end
	end
	self.cover:setVisible(allDefault)
	self.tickBox.selected[1] = allDefault
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function SpawnRegionsPanel:createChildren()
	local buttonWid = 200
	local buttonX = self.width - 24 - buttonWid
	local buttonY = 24
	local buttonHgt = 40
	local buttonGapY = 10
	
	self.listbox = SpawnRegionsListBox:new(24, 24, self.width - buttonWid - 24 * 3, self.height - 24 * 3 - 40)
	self.listbox:initialise()
	self.listbox:instantiate()
	self.listbox:setAnchorLeft(true)
	self.listbox:setAnchorRight(true)
	self.listbox:setAnchorTop(true)
	self.listbox:setAnchorBottom(true)
	self.listbox:setFont("Medium", 4)
	self.listbox.drawBorder = true
	self.listbox:setScrollChildren(true)
	self:addChild(self.listbox)
	self.listbox:createChildren()

	local fontHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local entryHgt = fontHgt + 2 * 2

	local label = ISLabel:new(24, self.listbox:getBottom() + 16, entryHgt, "SpawnPoint", 1, 1, 1, 1, UIFont.Medium, true)
	label:setAnchorTop(false)
	label:setAnchorBottom(true)
	self:addChild(label)
	local entry = ISTextEntryBox:new("", label:getRight() + 8, label:getY(), 300, entryHgt)
	entry:setAnchorTop(false)
	entry:setAnchorBottom(true)
	entry.font = UIFont.Medium
	entry:initialise()
	entry:instantiate()
	entry.tooltip = getTooltipText("UI_ServerOption_SpawnPoint_tooltip")
	self:addChild(entry)
	self.entry = entry

	local button = ISButton:new(buttonX, buttonY, buttonWid, buttonHgt, getText("UI_ServerSettings_ButtonAddRegion"), self, self.onButtonAddRegion)
	button:initialise()
	button:setAnchorLeft(false)
	button:setAnchorTop(false)
	button:setAnchorRight(true)
	button:setAnchorBottom(false)
	button.borderColor = {r=1, g=1, b=1, a=0.2}
	button:setFont(UIFont.Medium)
	self:addChild(button)
	self.buttonAdd = button

	buttonY = buttonY + buttonHgt + buttonGapY
	button = ISButton:new(buttonX, buttonY, buttonWid, buttonHgt, getText("UI_ServerSettings_ButtonRemoveRegion"), self, self.onButtonRemoveRegion)
	button:initialise()
	button:setAnchorLeft(false)
	button:setAnchorTop(false)
	button:setAnchorRight(true)
	button:setAnchorBottom(false)
	button.borderColor = {r=1, g=1, b=1, a=0.2}
	button:setFont(UIFont.Medium)
	self:addChild(button)
	self.buttonRemove = button

	buttonY = buttonY + buttonHgt + buttonGapY
	button = ISButton:new(buttonX, buttonY, buttonWid, buttonHgt, getText("UI_ServerSettings_ButtonEditRegion"), self, self.onButtonEditRegion)
	button:initialise()
	button:setAnchorLeft(false)
	button:setAnchorTop(false)
	button:setAnchorRight(true)
	button:setAnchorBottom(false)
	button.borderColor = {r=1, g=1, b=1, a=0.2}
	button:setFont(UIFont.Medium)
	self:addChild(button)
	self.buttonEdit = button
end

function SpawnRegionsPanel:addToList(name, file)
	local item = {}
	item.name = name
	item.file = file
	self.listbox:addItem('XXX', item)
end

function SpawnRegionsPanel:onButtonAddRegion()
	self:addToList("", "")
	self.listbox.selected = #self.listbox.items
end

function SpawnRegionsPanel:onButtonRemoveRegion()
	if self.listbox.items[self.listbox.selected] then
		self.listbox.currentItem = nil
		self.listbox:removeItemByIndex(self.listbox.selected)
	end
end

function SpawnRegionsPanel:onButtonEditRegion()
	local listbox = self.listbox
	self.parent:setVisible(false)
	self.parent.parent.pageSpawnPoints.settings = self.settings
	self.parent.parent.pageSpawnPoints.region = listbox.items[self.listbox.selected].item
	self.parent.parent.pageSpawnPoints:aboutToShow()
	self.parent.parent.pageSpawnPoints:setVisible(true)
end

function SpawnRegionsPanel:prerender()
	ISPanelJoypad.prerender(self)
	local item = self.listbox.items[self.listbox.selected]
	local validFile = item ~= nil and
		not luautils.stringStarts(item.item.file, "media") and
		not item.item.file:contains("/") and
		not item.item.file:contains("\\") and
		item.item.file:sub(-15) == "spawnpoints.lua"
		self.buttonEdit:setEnable(validFile)
	if not item or validFile then
		self.buttonEdit.tooltip = nil
	elseif luautils.stringStarts(item.item.file, "media") then
		self.buttonEdit.tooltip = getTooltipText("UI_ServerSettings_ButtonEditRegion_tooltip1")
	else
		self.buttonEdit.tooltip = getTooltipText("UI_ServerSettings_ButtonEditRegion_tooltip2")
	end
end

function SpawnRegionsPanel:notify(message, arg1, arg2, arg3, arg4)
	local mapFolder = arg1
	local file = "media/maps/" .. mapFolder .. "/spawnpoints.lua"
	if message == "addedMap" then
		for i=1,#self.listbox.items do
			if self.listbox.items[i].item.file == file then
				return
			end
		end
		self:addToList(mapFolder, file)
	end
	if message == "removedMap" then
		for i=#self.listbox.items,1,-1 do
			if self.listbox.items[i].item.file == file then
				self.listbox.currentItem = nil
				self.listbox:removeItemByIndex(i)
			end
		end
	end
end

function SpawnRegionsPanel:setSettings(settings)
	self.settings = settings
	self.listbox.currentItem = nil
	self.listbox:clear()
	for i=1,settings:getNumSpawnRegions() do
		self:addToList(settings:getSpawnRegionName(i-1), settings:getSpawnRegionFile(i-1))
	end
	self.entry:setText(settings:getServerOptions():getOptionByName("SpawnPoint"):getValue())
end

function SpawnRegionsPanel:settingsFromUI()
	local item = self.listbox.items[self.listbox.currentItem]
	if item then
		item.item.name = self.listbox.entryName:getText()
		item.item.file = self.listbox.entryFile:getText()
	end
	self.settings:clearSpawnRegions()
	for i=1,#self.listbox.items do
		local item = self.listbox.items[i].item
		self.settings:addSpawnRegion(item.name, item.file)
	end
	self.settings:getServerOptions():getOptionByName("SpawnPoint"):setValue(self.entry:getText())
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function ServerSettingsScreenModsPanel:createChildren()
	local fontHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	
	local label = ISLabel:new(24, 24, fontHgt, getText("UI_ServerSettings_ListOfMods"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label)

	self.listbox = ServerSettingsScreenModsListBox:new(24, label:getBottom() + 4, math.min(self.width - 24 * 2, 400), self.height - 24 * 2)
	self.listbox:initialise()
	self.listbox:instantiate()
	self.listbox:setAnchorLeft(true)
	self.listbox:setAnchorRight(false)
	self.listbox:setAnchorTop(true)
	self.listbox:setAnchorBottom(false)
	self.listbox:setFont("Medium", 4)
	self.listbox.drawBorder = true
	self.listbox:setHeight(self.listbox.itemheight * 8)
	self.listbox:ignoreHeightChange()
	self.listbox.vscroll:setHeight(self.listbox.height)
	self:addChild(self.listbox)

	label = ISLabel:new(self.listbox:getX(), self.listbox:getBottom() + 12, fontHgt, getText("UI_ServerSettings_AddInstalledMod"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label)

	local comboBox = ISComboBox:new(label:getX(), label:getBottom() + 4, self.listbox:getWidth(), fontHgt + 4, self, self.onAddInstalledMod)
	comboBox:setToolTipMap({ defaultTooltip = getTooltipText("UI_ServerSettings_AddInstalledMod_tooltip") })
	self:addChild(comboBox)
	self.comboBox = comboBox

	label = ISLabel:new(self.listbox:getX(), comboBox:getBottom() + 12, 20, getText("UI_ServerSettings_AddOtherMod"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label)
	
	local entry = ISTextEntryBox:new("", self.listbox:getX(), label:getBottom() + 4, self.listbox:getWidth(), fontHgt + 4)
	entry.font = UIFont.Medium
	entry.tooltip = getTooltipText("UI_ServerSettings_AddOtherMod_tooltip")
	entry.onCommandEntered = self.onAddOtherMod
	self:addChild(entry)
end

function ServerSettingsScreenModsPanel:setSettings(settings)
	self.settings = settings
	self.listbox:clear()

	local modsString = settings:getServerOptions():getOptionByName("Mods"):getValue()
	local modIDs = string.split(modsString, ";")
	for _,modID in ipairs(modIDs) do
		if modID ~= "" then
			self:addModToList(modID)
		end
	end
end

function ServerSettingsScreenModsPanel:settingsFromUI()
	local modsString = self:modListToString()
	self.settings:getServerOptions():getOptionByName("Mods"):setValue(modsString)
end

function ServerSettingsScreenModsPanel:modListToString()
	local modsString = ""
	for _,item in ipairs(self.listbox.items) do
		if modsString ~= "" then
			modsString = modsString .. ";"
		end
		modsString = modsString .. item.item.modID
	end
	return modsString
end

function ServerSettingsScreenModsPanel:addModToList(modID)
	local item = {}
	item.modID = modID
	item.modInfo = self.modInfoByID[modID]
	if item.modInfo then
		self.listbox:addItem(item.modInfo:getName(), item)
	else
		self.listbox:addItem(modID, item)
	end
end

function ServerSettingsScreenModsPanel:onAddInstalledMod()
	local modInfo = self.comboBox.options[self.comboBox.selected].data
	if self:findModInList(modInfo:getId()) then return end
	self:addModToList(modInfo:getId())
	self.listbox.selected = #self.listbox.items
	self.listbox:ensureVisible(self.listbox.selected)
	local modsString = self:modListToString()
	self.pageEdit:notify("addedMod", modInfo:getId(), modsString)
end

function ServerSettingsScreenModsPanel:onAddOtherMod()
	local modID = self:getText()
	if modID == "" or string.contains(modID, ";") then
		return
	end
	if self.parent:findModInList(modID) then return end
	self.parent:addModToList(modID)
	self.parent.listbox.selected = #self.parent.listbox.items
	self.parent.listbox:ensureVisible(self.parent.listbox.selected)
	self.javaObject:SetText("")
	local modsString = self.parent:modListToString()
	self.parent.pageEdit:notify("addedMod", modID, modsString)
end

function ServerSettingsScreenModsPanel:onRemoveMod(index)
	local modID = self.listbox.items[index].item.modID
	self.listbox:removeItemByIndex(index)
	local modsString = self:modListToString()
	self.pageEdit:notify("removedMod", modID, modsString)
end

function ServerSettingsScreenModsPanel:findModInList(modID)
	for i,item in ipairs(self.listbox.items) do
		if item.item.modID == modID then
			return i
		end
	end
	return nil
end

function ServerSettingsScreenModsPanel:notify(message, arg1, arg2, arg3, arg4)
	if message == "addedWorkshopItem" then
		local workshopID = arg1
		local mods = getSteamWorkshopItemMods(workshopID)
		if mods then
			for i=1,mods:size() do
				local modID = mods:get(i-1):getId()
				if not self:findModInList(modID) then
					self:addModToList(modID)
					self.listbox.selected = #self.listbox.items
					self.listbox:ensureVisible(self.listbox.selected)
					self.pageEdit:notify("addedMod", modID, self:modListToString())
				end
			end
		end
	end
	if message == "removedWorkshopItem" then
		local workshopID = arg1
		local mods = getSteamWorkshopItemMods(workshopID)
		if mods then
			for i=1,mods:size() do
				local modID = mods:get(i-1):getId()
				local removed = false
				local index = self:findModInList(modID)
				while index do
					self:onRemoveMod(index)
					index = self:findModInList(modID)
				end
				if removed then
					self.pageEdit:notify("removedMod", modID, self:modListToString())
				end
			end
		end
	end
end

function ServerSettingsScreenModsPanel:aboutToShowStartScreen()
	self.comboBox.options = {}
	self.modInfoByID = {}
	local modDirectories = getModDirectoryTable()
	for index,dirName in ipairs(modDirectories) do
		local modInfo = getModInfo(dirName)
		if modInfo then
			self.comboBox:addOptionWithData(modInfo:getName(), modInfo)
			self.modInfoByID[modInfo:getId()] = modInfo
		end
	end
	table.sort(self.comboBox.options, function(a,b) return not string.sort(a.text, b.text) end)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function ServerSettingsScreenModsListBox:prerender()
	self.mouseOverButtonIndex = nil
	ISScrollingListBox.prerender(self)
end

function ServerSettingsScreenModsListBox:doDrawItem(y, item, alt)
	self:drawRectBorder(0, y, self:getWidth(), self.itemheight - 1, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b)

	if self.selected == item.index then
		self:drawRect(0, y, self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15)
	elseif self.mouseoverselected == item.index and not self:isMouseOverScrollBar() then
		self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 4, 0.95, 0.05, 0.05, 0.05);
	end

	local smallFontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()
	
	local dy = (self.itemheight - getTextManager():getFontFromEnum(self.font):getLineHeight()) / 2
	self:drawText(item.text, 8, y + dy, 0.9, 0.9, 0.9, 0.9, self.font)
	if item.item.modInfo then
		local x = 8 + getTextManager():MeasureStringX(self.font, item.text) + 8
		dy = (self.itemheight - smallFontHgt) / 2
		self:drawText("[" .. item.item.modInfo:getId() .. "]", x, y + dy, 0.6, 0.6, 0.6, 0.9, UIFont.Small)
	end

	if not self.disableRemove and self.mouseoverselected == item.index and not self:isMouseOverScrollBar() then
		local textRemove = getText("UI_btn_remove")
		local textRemoveWid = getTextManager():MeasureStringX(UIFont.Small, textRemove)
		local buttonWid = 8 + textRemoveWid + 8
		local buttonHgt = smallFontHgt + 4
		local scrollBarWid = self:isVScrollBarVisible() and 13 or 0
		local buttonX = self.width - 4 - scrollBarWid - buttonWid
		local buttonY = y + (item.height - buttonHgt) / 2
		local isMouseOverButton = (self:getMouseX() > buttonX - 8)
		if isMouseOverButton then
			self:drawRect(buttonX, buttonY, buttonWid, buttonHgt, 1, 0.85, 0, 0)
			self.mouseOverButtonIndex = item.index
		else
			self:drawRect(buttonX, buttonY, buttonWid, buttonHgt, 1, 0.50, 0.50, 0.50)
		end
		self:drawTextCentre(textRemove, buttonX +  buttonWid / 2, y + (item.height - smallFontHgt) / 2 , 0, 0, 0, 1)
	end

	return y + item.height
end

function ServerSettingsScreenModsListBox:onMouseDown(x, y)
	if self.mouseOverButtonIndex then
		self.parent:onRemoveMod(self.mouseOverButtonIndex)
	else
		ISScrollingListBox.onMouseDown(self, x, y)
	end
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function ServerSettingsScreenMapsPanel:createChildren()
	local fontHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	
	local label = ISLabel:new(24, 24, fontHgt, getText("UI_ServerSettings_ListOfMaps"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label)

	local buttonWid = 200
	local buttonHgt = 40
	local buttonGapY = 10

	self.listbox = ServerSettingsScreenMapsListBox:new(24, label:getBottom() + 4, math.min(self.width - 24 * 3 - buttonWid, 400), self.height - 24 * 2)
	self.listbox:initialise()
	self.listbox:instantiate()
	self.listbox:setAnchorLeft(true)
	self.listbox:setAnchorRight(false)
	self.listbox:setAnchorTop(true)
	self.listbox:setAnchorBottom(false)
	self.listbox:setFont("Medium", 4)
	self.listbox.drawBorder = true
	self.listbox:setHeight(self.listbox.itemheight * 8)
	self.listbox:ignoreHeightChange()
	self.listbox.vscroll:setHeight(self.listbox.height)
	self:addChild(self.listbox)

	local buttonX = self.listbox:getRight() + 24
	local buttonY = self.listbox:getY()

	local button = ISButton:new(buttonX, buttonY, buttonWid, buttonHgt, getText("UI_ServerSettings_ButtonMoveUp"), self, self.onButtonMoveUp)
	button:initialise()
	button:setAnchorLeft(true)
	button:setAnchorTop(false)
	button:setAnchorRight(false)
	button:setAnchorBottom(false)
	button.borderColor = {r=1, g=1, b=1, a=0.2}
	button:setFont(UIFont.Medium)
	button.tooltip = getTooltipText("UI_ServerSettings_ButtonMoveUp_tooltip")
	self:addChild(button)
	self.buttonMoveUp = button

	buttonY = buttonY + buttonHgt + buttonGapY
	button = ISButton:new(buttonX, buttonY, buttonWid, buttonHgt, getText("UI_ServerSettings_ButtonMoveDown"), self, self.onButtonMoveDown)
	button:initialise()
	button:setAnchorLeft(true)
	button:setAnchorTop(false)
	button:setAnchorRight(false)
	button:setAnchorBottom(false)
	button.borderColor = {r=1, g=1, b=1, a=0.2}
	button:setFont(UIFont.Medium)
	self:addChild(button)
	self.buttonMoveDown = button

	label = ISLabel:new(self.listbox:getX(), self.listbox:getBottom() + 12, fontHgt, getText("UI_ServerSettings_AddInstalledMap"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label)

	local comboBox = ISComboBox:new(label:getX(), label:getBottom() + 4, self.listbox:getWidth(), fontHgt + 4, self, self.onAddInstalledMap)
	comboBox:setToolTipMap({ defaultTooltip = getText("UI_ServerSettings_AddInstalledMap_tooltip") })
	self:addChild(comboBox)
	self.comboBox = comboBox

	label = ISLabel:new(self.listbox:getX(), comboBox:getBottom() + 12, 20, getText("UI_ServerSettings_AddOtherMap"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label)
	
	local entry = ISTextEntryBox:new("", self.listbox:getX(), label:getBottom() + 4, self.listbox:getWidth(), fontHgt + 4)
	entry.font = UIFont.Medium
	entry.tooltip = getTooltipText("UI_ServerSettings_AddOtherMap_tooltip")
	entry.onCommandEntered = self.onAddOtherMap
	self:addChild(entry)
end

function ServerSettingsScreenMapsPanel:prerender()
	ISPanelJoypad.prerender(self)
	self.buttonMoveUp:setEnable(self.listbox.selected > 1)
	self.buttonMoveDown:setEnable(self.listbox.selected < #self.listbox.items)
end

function ServerSettingsScreenMapsPanel:onButtonMoveUp()
	if self.listbox.selected > 1 then
		local index = self.listbox.selected
		local mapFolder = self.listbox.items[index].item.mapFolder
		self.listbox:removeItemByIndex(index)
		self:addMapToList(mapFolder, index - 1)
		self.listbox.selected = index - 1
		self.listbox:ensureVisible(index - 1)
	end
end

function ServerSettingsScreenMapsPanel:onButtonMoveDown()
	if self.listbox.selected < #self.listbox.items then
		local index = self.listbox.selected
		local mapFolder = self.listbox.items[index].item.mapFolder
		self.listbox:removeItemByIndex(index)
		self:addMapToList(mapFolder, index + 1)
		self.listbox.selected = index + 1
		self.listbox:ensureVisible(index + 1)
	end
end

function ServerSettingsScreenMapsPanel:setSettings(settings)
	self.settings = settings
	self.listbox:clear()

	local modsString = settings:getServerOptions():getOptionByName("Mods"):getValue()
	self:fillComboBox(modsString)

	local mapString = settings:getServerOptions():getOptionByName("Map"):getValue()
	local mapFolders = string.split(mapString, ";")
	for _,mapFolder in ipairs(mapFolders) do
		if mapFolder ~= "" then
			self:addMapToList(mapFolder)
		end
	end
end

function ServerSettingsScreenMapsPanel:settingsFromUI()
	local mapString = ""
	for _,item in ipairs(self.listbox.items) do
		if mapString ~= "" then
			mapString = mapString .. ";"
		end
		mapString = mapString .. item.item.mapFolder
	end
	self.settings:getServerOptions():getOptionByName("Map"):setValue(mapString)
end

function ServerSettingsScreenMapsPanel:notify(message, arg1, arg2, arg3, arg4)
	if message == "addedMod" or message == "removedMod" then
		local modID = arg1
		local modsString = arg2
		self:fillComboBox(modsString)
		local mapFolders = getMapFoldersForMod(modID)
		if message == "addedMod" then
			if mapFolders then
				local insertAt = 1
				for i=1,mapFolders:size() do
					local mapFolder = mapFolders:get(i-1)
					if not self:findMapInList(mapFolder) then
						self:addMapToList(mapFolder, insertAt)
						insertAt = insertAt + 1
						self.listbox.selected = 1
						self.listbox:ensureVisible(self.listbox.selected)
						self.pageEdit:notify("addedMap", mapFolder)
					end
				end
			end
		end
		if message == "removedMod" then
			if mapFolders then
				for i=1,mapFolders:size() do
					local mapFolder = mapFolders:get(i-1)
					local index = self:findMapInList(mapFolder)
					while index do
						self.listbox:removeItemByIndex(index)
						index = self:findMapInList(mapFolder)
					end
					self.pageEdit:notify("removedMap", mapFolder)
				end
			end
		end
	end
end

function ServerSettingsScreenMapsPanel:fillComboBox(modsString)
	self.comboBox.options = {}
	self.comboBox:addOption("Muldraugh, KY")
	-- Riverside, Rosewood, and West Point do not need to be included since
	-- those map folders do not have objects.lua files of their own.
--	self.comboBox:addOption("West Point, KY")
	local modIDs = string.split(modsString, ";")
	for _,modID in ipairs(modIDs) do
		local modInfo = (modID ~= "") and getModInfoByID(modID) or ""
		if modInfo then
			local mapFolders = getMapFoldersForMod(modID)
			if mapFolders then
				for i=1,mapFolders:size() do
					self.comboBox:addOption(mapFolders:get(i-1))
				end
			end
		end
	end
	self.comboBox.selected = 1
end

function ServerSettingsScreenMapsPanel:findMapInList(mapFolder)
	for i,item in ipairs(self.listbox.items) do
		if item.item.mapFolder == mapFolder then
			return i
		end
	end
	return nil
end

function ServerSettingsScreenMapsPanel:addMapToList(mapFolder, index)
	local item = {}
	item.mapFolder = mapFolder
	self.listbox:insertItem(index and index or 10000, mapFolder, item)
end

function ServerSettingsScreenMapsPanel:onAddInstalledMap()
	local mapFolder = self.comboBox.options[self.comboBox.selected]
	if self:findMapInList(mapFolder) then return end
	self:addMapToList(mapFolder)
	self.listbox.selected = #self.listbox.items
	self.listbox:ensureVisible(self.listbox.selected)
	self.pageEdit:notify("addedMap", mapFolder)
end

function ServerSettingsScreenMapsPanel:onAddOtherMap()
	local mapFolder = self:getText()
	if mapFolder == "" or string.contains(mapFolder, ";") then
		return
	end
	if self.parent:findMapInList(mapFolder) then return end
	self.parent:addMapToList(mapFolder)
	self.parent.listbox.selected = #self.parent.listbox.items
	self.parent.listbox:ensureVisible(self.parent.listbox.selected)
	self.javaObject:SetText("")
	self.parent.pageEdit:notify("addedMap", mapFolder)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function ServerSettingsScreenMapsListBox:prerender()
	self.mouseOverButtonIndex = nil
	ISScrollingListBox.prerender(self)
end

function ServerSettingsScreenMapsListBox:doDrawItem(y, item, alt)
	self:drawRectBorder(0, y, self:getWidth(), self.itemheight - 1, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b)

	if self.selected == item.index then
		self:drawRect(0, y, self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15)
	elseif self.mouseoverselected == item.index and not self:isMouseOverScrollBar() then
		self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 4, 0.95, 0.05, 0.05, 0.05);
	end

	local smallFontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()
	
	local dy = (self.itemheight - getTextManager():getFontFromEnum(self.font):getLineHeight()) / 2
	self:drawText(item.text, 8, y + dy, 0.9, 0.9, 0.9, 0.9, self.font)
	if item.item.otherText then
		local x = 8 + getTextManager():MeasureStringX(self.font, item.text) + 8
		dy = (self.itemheight - smallFontHgt) / 2
		self:drawText("[" .. item.item.otherText .. "]", x, y + dy, 0.6, 0.6, 0.6, 0.9, UIFont.Small)
	end

	if self.mouseoverselected == item.index and not self:isMouseOverScrollBar() then
		local textRemove = getText("UI_btn_remove")
		local textRemoveWid = getTextManager():MeasureStringX(UIFont.Small, textRemove)
		local buttonWid = 8 + textRemoveWid + 8
		local buttonHgt = smallFontHgt + 4
		local scrollBarWid = self:isVScrollBarVisible() and 13 or 0
		local buttonX = self.width - 4 - scrollBarWid - buttonWid
		local buttonY = y + (item.height - buttonHgt) / 2
		local isMouseOverButton = (self:getMouseX() > buttonX - 8)
		if isMouseOverButton then
			self:drawRect(buttonX, buttonY, buttonWid, buttonHgt, 1, 0.85, 0, 0)
			self.mouseOverButtonIndex = item.index
		else
			self:drawRect(buttonX, buttonY, buttonWid, buttonHgt, 1, 0.50, 0.50, 0.50)
		end
		self:drawTextCentre(textRemove, buttonX +  buttonWid / 2, y + (item.height - smallFontHgt) / 2 , 0, 0, 0, 1)
	end

	return y + item.height
end

function ServerSettingsScreenMapsListBox:onMouseDown(x, y)
	if self.mouseOverButtonIndex then
		self:onRemoveItem(self.mouseOverButtonIndex)
	else
		ISScrollingListBox.onMouseDown(self, x, y)
	end
end

function ServerSettingsScreenMapsListBox:onRemoveItem(index)
	local mapFolder = self.items[index].text
	self:removeItemByIndex(index)
	self.parent.pageEdit:notify("removedMap", mapFolder)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function ServerSettingsScreenWorkshopPanel:createChildren()
	local fontHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	
	local label = ISLabel:new(24, 24, fontHgt, getText("UI_ServerSettings_ListOfWorkshopItems"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label)

	self.listbox = ServerSettingsScreenWorkshopListBox:new(24, label:getBottom() + 4, math.min(self.width - 24 * 2, 400), self.height - 24 * 2)
	self.listbox:initialise()
	self.listbox:instantiate()
	self.listbox:setAnchorLeft(true)
	self.listbox:setAnchorRight(false)
	self.listbox:setAnchorTop(true)
	self.listbox:setAnchorBottom(false)
	self.listbox:setFont("Medium", 4)
	self.listbox.drawBorder = true
	self.listbox:setHeight(self.listbox.itemheight * 8)
	self.listbox:ignoreHeightChange()
	self.listbox.vscroll:setHeight(self.listbox.height)
	self:addChild(self.listbox)

	label = ISLabel:new(self.listbox:getX(), self.listbox:getBottom() + 12, fontHgt, getText("UI_ServerSettings_AddInstalledWorkshopItem"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label)

	local comboBox = ISComboBox:new(label:getX(), label:getBottom() + 4, self.listbox:getWidth(), fontHgt + 4, self, self.onAddInstalledItem)
	comboBox:setToolTipMap({ defaultTooltip = getTooltipText("UI_ServerSettings_AddInstalledWorkshopItem_tooltip") })
	self:addChild(comboBox)
	self.comboBox = comboBox

	label = ISLabel:new(self.listbox:getX(), comboBox:getBottom() + 12, 20, getText("UI_ServerSettings_AddOtherWorkshopItem"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label)
	
	local entry = ISTextEntryBox:new("", self.listbox:getX(), label:getBottom() + 4, self.listbox:getWidth(), fontHgt + 4)
	entry.font = UIFont.Medium
	entry.tooltip = getTooltipText("UI_ServerSettings_AddOtherWorkshopItem_tooltip")
	entry.onCommandEntered = self.onAddOtherItem
	self.entry = entry
	self:addChild(entry)
end

function ServerSettingsScreenWorkshopPanel:prerender()
	ISPanelJoypad.prerender(self)
	if self.entry:getText() == "" or self:isValidWorkshopID(self.entry:getText()) then
		self.entry.borderColor.a = 1
		self.entry.borderColor.g = 0.4
		self.entry.borderColor.b = 0.4
	else
		self.entry.borderColor.a = 0.9
		self.entry.borderColor.g = 0.0
		self.entry.borderColor.b = 0.0
	end
end

function ServerSettingsScreenWorkshopPanel:setSettings(settings)
	self.settings = settings
	self.listbox:clear()

	local idsString = settings:getServerOptions():getOptionByName("WorkshopItems"):getValue()
	local workshopIDs = string.split(idsString, ";")
	for _,workshopID in ipairs(workshopIDs) do
		if workshopID ~= "" then
			self:addItemToList(workshopID)
		end
	end
end

function ServerSettingsScreenWorkshopPanel:settingsFromUI()
	local modsString = self:itemListToString()
	self.settings:getServerOptions():getOptionByName("WorkshopItems"):setValue(modsString)
end

function ServerSettingsScreenWorkshopPanel:aboutToShowStartScreen()
	self.comboBox.options = {}
	self.comboBox.workshopIDToIndex = {}
	self.itemDetails = {}
	local workshopIDs = getSteamWorkshopItemIDs()
	if not workshopIDs or workshopIDs:isEmpty() then
		return
	end
	for i=1,workshopIDs:size() do
		local workshopID = workshopIDs:get(i-1)
		self.comboBox:addOptionWithData(workshopID, workshopID)
		self.comboBox.workshopIDToIndex[workshopID] = i
	end
	querySteamWorkshopItemDetails(workshopIDs, self.onItemQueryFinished, self)
end

function ServerSettingsScreenWorkshopPanel:itemListToString()
	local idsString = ""
	for _,item in ipairs(self.listbox.items) do
		if idsString ~= "" then
			idsString = idsString .. ";"
		end
		idsString = idsString .. item.item.workshopID
	end
	return idsString
end

function ServerSettingsScreenWorkshopPanel:addItemToList(workshopID)
	local item = {}
	item.workshopID = workshopID
	self.listbox:addItem(workshopID, item)
end

function ServerSettingsScreenWorkshopPanel:isValidWorkshopID(workshopID)
	return workshopID and not string.contains(workshopID, ";") and isValidSteamID(workshopID)
end

function ServerSettingsScreenWorkshopPanel:onAddInstalledItem()
	local workshopID = self.comboBox.options[self.comboBox.selected].data
	self:addItemToList(workshopID)
	self.listbox.selected = #self.listbox.items
	self.listbox:ensureVisible(self.listbox.selected)
	self.pageEdit:notify("addedWorkshopItem", workshopID)
end

function ServerSettingsScreenWorkshopPanel:onAddOtherItem()
	local workshopID = self:getText()
	if not self.parent:isValidWorkshopID(workshopID) then
		return
	end
	self.parent:addItemToList(workshopID)
	self.parent.listbox.selected = #self.parent.listbox.items
	self.parent.listbox:ensureVisible(self.parent.listbox.selected)
	self.javaObject:SetText("")
end

function ServerSettingsScreenWorkshopPanel:onRemoveItem(index)
	local workshopID = self.listbox.items[index].item.workshopID
	self.listbox:removeItemByIndex(index)
	self.pageEdit:notify("removedWorkshopItem", workshopID)
end

function ServerSettingsScreenWorkshopPanel:onItemQueryFinished(status, info)
	if status == "Completed" then
		for i=1,info:size() do
			local details = info:get(i-1)
			local workshopID = details:getIDString()
			self.itemDetails[workshopID] = details
			local index = self.comboBox.workshopIDToIndex[workshopID]
			self.comboBox.options[index].text = details:getTitle()
		end
		table.sort(self.comboBox.options, function(a,b) return not string.sort(a.text, b.text) end)
		self.comboBox.workshopIDToIndex = {}
		for i=1,#self.comboBox.options do
			local option = self.comboBox.options[i]
			self.comboBox.workshopIDToIndex[option.data] = i
		end
	end
	if status == "NotCompleted" then
	end
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function ServerSettingsScreenWorkshopListBox:prerender()
	self.mouseOverButtonIndex = nil
	ISScrollingListBox.prerender(self)
end

function ServerSettingsScreenWorkshopListBox:doDrawItem(y, item, alt)
	self:drawRectBorder(0, y, self:getWidth(), self.itemheight - 1, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b)

	if self.selected == item.index then
		self:drawRect(0, y, self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15)
	elseif self.mouseoverselected == item.index and not self:isMouseOverScrollBar() then
		self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 4, 0.95, 0.05, 0.05, 0.05);
	end

	local smallFontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()
	
	local dy = (self.itemheight - getTextManager():getFontFromEnum(self.font):getLineHeight()) / 2
	local details = self.parent.itemDetails[item.item.workshopID]
	if details then
		local x = 8 + getTextManager():MeasureStringX(self.font, details:getTitle()) + 8
		self:drawText(details:getTitle(), 8, y + dy, 0.9, 0.9, 0.9, 0.9, self.font)
		dy = (self.itemheight - smallFontHgt) / 2
		self:drawText("[" .. item.item.workshopID .. "]", x, y + dy, 0.6, 0.6, 0.6, 0.9, UIFont.Small)
	else
		self:drawText(item.text, 8, y + dy, 0.9, 0.9, 0.9, 0.9, self.font)
	end

	if self.mouseoverselected == item.index and not self:isMouseOverScrollBar() then
		local textRemove = getText("UI_btn_remove")
		local textRemoveWid = getTextManager():MeasureStringX(UIFont.Small, textRemove)
		local buttonWid = 8 + textRemoveWid + 8
		local buttonHgt = smallFontHgt + 4
		local scrollBarWid = self:isVScrollBarVisible() and 13 or 0
		local buttonX = self.width - 4 - scrollBarWid - buttonWid
		local buttonY = y + (item.height - buttonHgt) / 2
		local isMouseOverButton = (self:getMouseX() > buttonX - 8)
		if isMouseOverButton then
			self:drawRect(buttonX, buttonY, buttonWid, buttonHgt, 1, 0.85, 0, 0)
			self.mouseOverButtonIndex = item.index
		else
			self:drawRect(buttonX, buttonY, buttonWid, buttonHgt, 1, 0.50, 0.50, 0.50)
		end
		self:drawTextCentre(textRemove, buttonX +  buttonWid / 2, y + (item.height - smallFontHgt) / 2 , 0, 0, 0, 1)
	end

	return y + item.height
end

function ServerSettingsScreenWorkshopListBox:onMouseDown(x, y)
	if self.mouseOverButtonIndex then
		self.parent:onRemoveItem(self.mouseOverButtonIndex)
	else
		ISScrollingListBox.onMouseDown(self, x, y)
	end
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function SpawnRegionsListBox:createChildren()
	self.entryName = ISTextEntryBox:new('', 0, 0, 100, 20)
	self.entryName.font = UIFont.Medium
	self.entryName:initialise()
	self.entryName:instantiate()
	self:addChild(self.entryName)
	self.entryName:setVisible(false)

	self.entryFile = ISTextEntryBox:new('', 0, 0, 100, 20)
	self.entryFile.font = UIFont.Medium
	self.entryFile:initialise()
	self.entryFile:instantiate()
	self:addChild(self.entryFile)
	self.entryFile:setVisible(false)
end

function SpawnRegionsListBox:prerender()
	if self.items[self.selected] then
		if self.currentItem ~= self.selected then
			self.currentItem = self.selected
			self:positionEntries()
		end
		if self.currentItem then
			self.items[self.currentItem].item.name = self.entryName:getText()
			self.items[self.currentItem].item.file = self.entryFile:getText()
		end
	elseif self.currentItem then
		self.items[self.currentItem].item.name = self.entryName:getText()
		self.items[self.currentItem].item.file = self.entryFile:getText()
		self.entryName:setVisible(false)
		self.entryFile:setVisible(false)
		self.currentItem = nil
	elseif self.entryName:getIsVisible() then
		self.entryName:setVisible(false)
		self.entryFile:setVisible(false)
	end
	if self.scrollBarVisible ~= self:isVScrollBarVisible() then
		self.scrollBarVisible = self:isVScrollBarVisible()
		self:positionEntries()
	end
	ISScrollingListBox.prerender(self)
    self:setStencilRect(0, 0, self:getWidth(), self:getHeight())
end

function SpawnRegionsListBox:render()
	ISScrollingListBox.render(self)
	self:clearStencilRect()
end

function SpawnRegionsListBox:doDrawItem(y, item, alt)
	self:drawRectBorder(0, y, self:getWidth(), item.height - 1, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b)
	if self.selected == item.index then
		self:drawRect(0, y, self:getWidth(), item.height - 1, 0.3, 0.7, 0.35, 0.15)
	end

	local label1 = 'name'
	local label2 = 'file'
	local label1Wid = getTextManager():MeasureStringX(self.font, label1)
	local label2Wid = getTextManager():MeasureStringX(self.font, label2)
	local fontHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local entryInset = 2
	local entryHgt = entryInset + fontHgt + entryInset

	self:drawText(label1, 8, y + 4 + (entryHgt - fontHgt) / 2, 0.9, 0.9, 0.9, 0.9, self.font)
	self:drawText(label2, 8, y + 4 + entryHgt + 4 + (entryHgt - fontHgt) / 2, 0.9, 0.9, 0.9, 0.9, self.font)
	if self.currentItem ~= item.index then
		local x = 8 + math.max(label1Wid, label2Wid) + 8 + entryInset
		self:drawText(item.item.name, x, y + 4 + (entryHgt - fontHgt) / 2, 0.9, 0.9, 0.9, 0.9, self.font)
		self:drawText(item.item.file, x, y + 4 + entryHgt + 4 + (entryHgt - fontHgt) / 2, 0.9, 0.9, 0.9, 0.9, self.font)
	end

	item.height = 4 + entryHgt + 4 + entryHgt + 4
	return y + item.height
end

function SpawnRegionsListBox:positionEntries()
	local label1 = 'name'
	local label2 = 'file'
	local label1Wid = getTextManager():MeasureStringX(self.font, label1)
	local label2Wid = getTextManager():MeasureStringX(self.font, label2)
	local fontHgt = getTextManager():getFontFromEnum(self.font):getLineHeight()
	local entryHgt = 2 + fontHgt + 2

	local x = 8 + math.max(label1Wid, label2Wid) + 8
	local y = 4 + (self.selected - 1) * (4 + entryHgt + 4 + entryHgt + 4)

	local scrollBarWidth = self:isVScrollBarVisible() and self.vscroll:getWidth() or 0
	
	local item = self.items[self.selected].item
	
	self.entryName:setX(x)
	self.entryName:setY(y)
	self.entryName:setWidth(self.width - scrollBarWidth - 8 - x)
	self.entryName:setHeight(entryHgt)
	self.entryName:setText(item.name)
	
	self.entryFile:setX(x)
	self.entryFile:setY(y + entryHgt + 4)
	self.entryFile:setWidth(self.width - scrollBarWidth - 8 - x)
	self.entryFile:setHeight(entryHgt)
	self.entryFile:setText(item.file)

	self.entryName:setVisible(true)
	self.entryFile:setVisible(true)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function SandboxPresetPanel:createChildren()
	local fontHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	
	local label = ISLabel:new(24, 24, fontHgt, getText("UI_ServerSettings_ListOfPresets"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label)

	self.listbox = ServerSettingsScreenModsListBox:new(24, label:getBottom() + 4, math.min(self.width - 24 * 2, 400), self.height - 24 * 2)
	self.listbox:initialise()
	self.listbox:instantiate()
	self.listbox:setAnchorLeft(true)
	self.listbox:setAnchorRight(false)
	self.listbox:setAnchorTop(true)
	self.listbox:setAnchorBottom(false)
	self.listbox:setFont("Medium", 4)
	self.listbox.drawBorder = true
	self.listbox:setHeight(self.listbox.itemheight * 8)
	self.listbox:ignoreHeightChange()
	self.listbox.vscroll:setHeight(self.listbox.height)
	self.listbox.disableRemove = true
	self:addChild(self.listbox)

	local buttonX = self.listbox:getRight() + 24
	local buttonY = self.listbox:getY()
	local buttonWid = 200
	local buttonHgt = 40

	local button = ISButton:new(buttonX, buttonY, buttonWid, buttonHgt, getText("UI_ServerSettings_ButtonApplyPreset"), self, self.onButtonApplyPreset)
	button:initialise()
	button:setAnchorLeft(true)
	button:setAnchorTop(false)
	button:setAnchorRight(false)
	button:setAnchorBottom(false)
	button.borderColor = {r=1, g=1, b=1, a=0.2}
	button:setFont(UIFont.Medium)
	button.tooltip = getTooltipText("UI_ServerSettings_ButtonApplyPreset_tooltip")
	self:addChild(button)
	self.buttonApplyPreset = button
end

function SandboxPresetPanel:prerender()
	ISPanelJoypad.prerender(self)
	self.buttonApplyPreset:setEnable(self.listbox.items[self.listbox.selected] ~= nil)
end

function SandboxPresetPanel:onButtonApplyPreset()
	local options = self.settings:getSandboxOptions() -- modifying original options, FIXME?
	options:resetToDefault()
	local data = self.listbox.items[self.listbox.selected].item
	if data.userDefined then
		options:loadPresetFile(data.fileName)
	else
		options:loadGameFile(data.fileName)
	end
	self.pageEdit:settingsToUIAux("Sandbox", options)
end

function SandboxPresetPanel:addPresetToList(fileName, text, userDefined)
	local item = {}
	item.fileName = fileName
	item.userDefined = userDefined
	self.listbox:addItem(text, item)
end

function SandboxPresetPanel:setSettings(settings)
	self.settings = settings
	self.listbox:clear()
	self:addPresetToList("Apocalypse", getText("UI_NewGame_Apocalypse"), false)
	self:addPresetToList("Survivor", getText("UI_NewGame_Survivor"), false)
	self:addPresetToList("Builder", getText("UI_NewGame_Builder"), false)
	self:addPresetToList("Beginner", getText("UI_NewGame_InitialInfection"), false)
	self:addPresetToList("FirstWeek", getText("UI_NewGame_OneWeekLater"), false)
	self:addPresetToList("Survival", getText("UI_NewGame_Survival"), false)
	self:addPresetToList("SixMonthsLater", getText("UI_NewGame_SixMonths"), false)
	local presets = getSandboxPresets()
	if presets then
		for i=1,presets:size() do
			local fileName = presets:get(i-1)
			self:addPresetToList(fileName, fileName, true)
		end
	end
end

function SandboxPresetPanel:settingsFromUI()
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function Page1:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.anchorBottom = true
	o.anchorRight = true
	return o
end

function Page1:create()
	local padX = 96
	local listY = 128

	local labelHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local label1 = ISLabel:new(padX, math.min(96, listY - labelHgt - 4), labelHgt, getText("UI_ServerSettings_ListOfSettings", getServerSettingsManager():getSettingsFolder()), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label1)

	self.listbox = ISScrollingListBox:new(padX, listY, 350, self.height - listY - 96)
	self.listbox:initialise()
	self.listbox:setAnchorLeft(true)
	self.listbox:setAnchorRight(false)
	self.listbox:setAnchorTop(true)
	self.listbox:setAnchorBottom(true)
	self.listbox:setFont("Medium", 4)
	self.listbox.drawBorder = true
	self.listbox.doDrawItem = self.doDrawItem
	self.listbox:setOnMouseDoubleClick(self, self.onDoubleClickListBox)
	self:addChild(self.listbox)

	local buttonX = self.listbox:getRight() + 32
	local buttonY = self.listbox:getY()
	local buttonHgt = 48
	local buttonWid = 400
	local buttonGapY = 24

	local button = ISButton:new(buttonX, buttonY, buttonWid, buttonHgt, getText("UI_ServerSettings_ButtonNew"), self, self.onButtonNew)
	button:initialise()
	button:setAnchorLeft(true)
	button:setAnchorTop(false)
	button:setAnchorBottom(false)
	button.borderColor = {r=1, g=1, b=1, a=0.2}
	button:setFont(UIFont.Medium)
	self:addChild(button)
	self.buttonNew = button

	buttonY = buttonY + buttonHgt + buttonGapY
	button = ISButton:new(buttonX, buttonY, buttonWid, buttonHgt, getText("UI_ServerSettings_ButtonEdit"), self, self.onButtonEdit)
	button:initialise()
	button:setAnchorLeft(true)
	button:setAnchorTop(false)
	button:setAnchorBottom(false)
	button.borderColor = {r=1, g=1, b=1, a=0.2}
	button:setFont(UIFont.Medium)
	self:addChild(button)
	self.buttonEdit = button

	buttonY = buttonY + buttonHgt + buttonGapY
	button = ISButton:new(buttonX, buttonY, buttonWid, buttonHgt, getText("UI_ServerSettings_ButtonDuplicate"), self, self.onButtonDuplicate)
	button:initialise()
	button:setAnchorLeft(true)
	button:setAnchorTop(false)
	button:setAnchorBottom(false)
	button.borderColor = {r=1, g=1, b=1, a=0.2}
	button:setFont(UIFont.Medium)
	self:addChild(button)
	self.buttonDuplicate = button

	buttonY = buttonY + buttonHgt + buttonGapY
	button = ISButton:new(buttonX, buttonY, buttonWid, buttonHgt, getText("UI_ServerSettings_ButtonRename"), self, self.onButtonRename)
	button:initialise()
	button:setAnchorLeft(true)
	button:setAnchorTop(false)
	button:setAnchorBottom(false)
	button.borderColor = {r=1, g=1, b=1, a=0.2}
	button:setFont(UIFont.Medium)
	self:addChild(button)
	self.buttonRename = button

	buttonY = buttonY + buttonHgt + buttonGapY
	button = ISButton:new(buttonX, buttonY, buttonWid, buttonHgt, getText("UI_ServerSettings_ButtonDelete"), self, self.onButtonDelete)
	button:initialise()
	button:setAnchorLeft(true)
	button:setAnchorTop(false)
	button:setAnchorBottom(false)
	button.borderColor = {r=1, g=1, b=1, a=0.2}
	button:setFont(UIFont.Medium)
	self:addChild(button)
	self.buttonDelete = button

	buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

	self.backButton = ISButton:new(16, self.height - 5 - buttonHgt, 100, buttonHgt, getText("UI_btn_back"), self, self.onButtonBack)
	self.backButton.internal = "BACK"
	self.backButton:initialise()
	self.backButton:setAnchorLeft(true)
	self.backButton:setAnchorTop(false)
	self.backButton:setAnchorBottom(true)
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.backButton)
end

function Page1:updateWhenVisible()
	local itemSelected = self.listbox.items[self.listbox.selected] ~= nil
	self.buttonEdit:setEnable(itemSelected)
	self.buttonDuplicate:setEnable(itemSelected)
	self.buttonRename:setEnable(itemSelected)
	self.buttonDelete:setEnable(itemSelected)
end

function Page1:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_ServerSettings_Title1"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	if getDebug() then self:drawText("DEBUG: Page1", 8, 8, 0.5, 0.5, 0.5, 1, UIFont.Small) end
	self:updateWhenVisible()
end

function Page1:onDoubleClickListBox(item)
	if self.listbox.items[self.listbox.selected] then
		self:onButtonEdit()
	end
end

function Page1:doDrawItem(y, item, alt)
	self:drawRectBorder(0, y, self:getWidth(), self.itemheight - 1, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b)

	if self.selected == item.index then
		self:drawRect(0, y, self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15)
	end

	local dy = (self.itemheight - getTextManager():getFontFromEnum(self.font):getLineHeight()) / 2
	self:drawText(item.text, 8, y + dy, 0.9, 0.9, 0.9, 0.9, self.font)

	return y + item.height
end

function Page1:aboutToShow()
	getServerSettingsManager():readAllSettings()
	self.listbox:clear()
	for i=1,getServerSettingsManager():getSettingsCount() do
		local settings = getServerSettingsManager():getSettingsByIndex(i-1)
		self.listbox:addItem(settings:getName(), settings)
		if self.parent.initialSelectedSettings == settings:getName() then
			self.listbox.selected = i
		end
	end
	if self.listbox.items[self.listbox.selected] then
		self.listbox:ensureVisible(self.listbox.selected)
	end
	self.parent.initialSelectedSettings = nil
end

function Page1:onButtonNew()
	self:setVisible(false)
	self.parent.pageNew:aboutToShow()
	self.parent.pageNew:setVisible(true)
end

function Page1:onButtonEdit()
	self:setVisible(false)
	self.parent.pageEdit.settings = self.listbox.items[self.listbox.selected].item
	self.parent.pageEdit.settings:loadFiles()
	self.parent.pageEdit:aboutToShow()
	self.parent.pageEdit:setVisible(true)
end

function Page1:onButtonDuplicate()
	self:setVisible(false)
	self.parent.pageDuplicate.settings = self.listbox.items[self.listbox.selected].item
	self.parent.pageDuplicate:aboutToShow()
	self.parent.pageDuplicate:setVisible(true)
end

function Page1:onButtonRename()
	self:setVisible(false)
	self.parent.pageRename.settings = self.listbox.items[self.listbox.selected].item
	self.parent.pageRename:aboutToShow()
	self.parent.pageRename:setVisible(true)
end

function Page1:onButtonDelete()
	self:setVisible(false)
	self.parent.pageDelete.settings = self.listbox.items[self.listbox.selected].item
	self.parent.pageDelete:aboutToShow()
	self.parent.pageDelete:setVisible(true)
end

function Page1:onButtonBack()
	self.parent:setVisible(false)
	local prevScreen = self.parent.prevScreen
	if prevScreen then
		self.parent.prevScreen = nil
		if self.parent.joyfocus then
			self.parent.joyfocus.focus = prevScreen
			updateJoypadFocus(self.parent.joyfocus)
		end
		prevScreen:aboutToShow()
		prevScreen:setVisible(true)
	else
		MainScreen.instance.bottomPanel:setVisible(true)
		if self.parent.joyfocus then
			self.parent.joyfocus.focus = MainScreen.instance
			updateJoypadFocus(self.parent.joyfocus)
		end
	end
end

function Page1:onGainJoypadFocus(joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData)
    self:setISButtonForB(self.backButton)
end

function Page1:onLoseJoypadFocus(joypadData)
    ISPanelJoypad.onLoseJoypadFocus(self, joypadData)
    self.backButton:clearJoypadButton()
end

function Page1:onJoypadBeforeDeactivate(joypadData)
	self.parent:onJoypadBeforeDeactivate(joypadData)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function Page2:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.anchorBottom = true
	o.anchorRight = true
	return o
end

function Page2:create()
	local padX = 96

	local labelHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local label1 = ISLabel:new(padX, 96, labelHgt, getText("UI_ServerSettings_LabelNewName"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label1)

	local entryWid = 400
	local entryHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight() + 2 * 2

	self.entry = ISTextEntryBox:new("xyz", label1:getX(), label1:getBottom() + 2, entryWid, entryHgt)
	self.entry.font = UIFont.Medium
	self.entry:initialise()
	self.entry:instantiate()
	self:addChild(self.entry)

	local label2 = ISLabel:new(padX, self.entry:getBottom() + 48, labelHgt, getText("UI_ServerSettings_LabelNewFiles"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label2)
	self.newFilesX = padX + 32
	self.newFilesY = label2:getBottom() + 4

	local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

	self.backButton = ISButton:new(self.width / 2 - 15 - 100, self.height - 5 - buttonHgt, 100, buttonHgt, getText("UI_btn_back"), self, self.onButtonBack)
	self.backButton:initialise()
	self.backButton:setAnchorLeft(true)
	self.backButton:setAnchorTop(false)
	self.backButton:setAnchorBottom(true)
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.backButton)

	self.nextButton = ISButton:new(self.width / 2 + 15, self.height - 5 - buttonHgt, 100, buttonHgt, getText("UI_btn_next"), self, self.onButtonNext)
	self.nextButton:initialise()
	self.nextButton:setAnchorLeft(true)
	self.nextButton:setAnchorTop(false)
	self.nextButton:setAnchorBottom(true)
	self.nextButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.nextButton)
end

function Page2:updateWhenVisible()
	local newName = self.entry:getText()
	if self.checkName ~= newName then
		self.checkName = newName
		local nameValid = getServerSettingsManager():isValidNewName(newName)
		if nameValid then
			self.entry.borderColor.a = 1
			self.entry.borderColor.g = 0.4
			self.entry.borderColor.b = 0.4
		else
			self.entry.borderColor.a = 0.9
			self.entry.borderColor.g = 0.0
			self.entry.borderColor.b = 0.0
		end
		self.nextButton:setEnable(nameValid)
	end
end

function Page2:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_ServerSettings_Title2"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	if getDebug() then self:drawText("DEBUG: Page2", 8, 8, 0.5, 0.5, 0.5, 1, UIFont.Small) end

	local prefix = getServerSettingsManager():getNameInSettingsFolder(self.entry:getText())
	local suffixes = getServerSettingsManager():getSuffixes()
	local y = self.newFilesY
	local labelHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	for i=1,suffixes:size() do
		if suffixes:get(i-1) ~= "_zombies.ini" then
			self:drawText(prefix .. suffixes:get(i-1), self.newFilesX, y, 1, 1, 1, 1, UIFont.Medium)
			y = y + labelHgt
		end
	end

	self:updateWhenVisible()
end

function Page2:aboutToShow()
	self.checkName = nil
	if getServerSettingsManager():isValidNewName("servertest") then
		self.entry:setText("servertest")
	else
		local n = 1
		while not getServerSettingsManager():isValidNewName("servertest" .. n) do
			n = n + 1
		end
		self.entry:setText("servertest" .. n)
	end
end

DefaultServerSettings = {}

function DefaultServerSettings:insertUnique(_table, value)
	for _,v in ipairs(_table) do
		if v == value then return end
	end
	table.insert(_table, value)
end

function DefaultServerSettings:setServerOptionValue(settings, option, _table)
	local value = ""
	for _,v in ipairs(_table) do
		if value ~= "" then value = value .. ";" end
		value = value .. v
	end
	settings:getServerOptions():getOptionByName(option):setValue(value)
end

function DefaultServerSettings:setDefaultsFromSingleplayer(settings)
	settings:resetToDefault()
	if true then return end
	if getSteamModeActive() then
		local addModIDs = {}
		local addWorkshopIDs = {}
		local addMapFolders = {}
		local addSpawnRegions = {}
		local activeModIDs = getActivatedMods()
		for i=1,activeModIDs:size() do
			local modID = activeModIDs:get(i-1)
			local modInfo = getModInfoByID(modID)
			if modInfo and modInfo:getWorkshopID() then
				self:insertUnique(addModIDs, modID)
				local workshopID = modInfo:getWorkshopID()
				self:insertUnique(addWorkshopIDs, workshopID)
				local mapFolders = getMapFoldersForMod(modID)
				if mapFolders then
					for j=1,mapFolders:size() do
						local mapFolder = mapFolders:get(j-1)
						self:insertUnique(addMapFolders, mapFolder)
						if spawnpointsExistsForMod(modID, mapFolder) then
							self:insertUnique(addSpawnRegions, mapFolder)
						end
					end
				end
			end
		end
		if #addWorkshopIDs > 0 then
			self:setServerOptionValue(settings, "WorkshopItems", addWorkshopIDs)
		end
		if #addModIDs > 0 then
			self:setServerOptionValue(settings, "Mods", addModIDs)
		end
		if #addMapFolders > 0 then
			local mapFolders = settings:getServerOptions():getOptionByName("Map"):getValue()
			self:insertUnique(addMapFolders, mapFolders)
			self:setServerOptionValue(settings, "Map", addMapFolders)
		end
		if #addSpawnRegions > 0 then
			for _,mapFolder in ipairs(addSpawnRegions) do
				settings:addSpawnRegion(mapFolder, "media/maps/" .. mapFolder .. "/spawnpoints.lua")
			end
		end
	end
end

function Page2:onButtonBack()
	self:setVisible(false)
	self.parent.pageStart:setVisible(true)
end

function Page2:onButtonNext()
	local settings = ServerSettings.new(self.entry:getText())
	if not settings:isValid() then
		local modal = ISModalDialog:new(getCore():getScreenWidth() / 2 - 175,getCore():getScreenHeight() / 2 - 75, 250, 150, settings:getErrorMsg(), false);
		modal:initialise()
		modal:addToUIManager()
	else
		self:setVisible(false)
		DefaultServerSettings:setDefaultsFromSingleplayer(settings)
		self.parent.pageEdit.settings = settings
		self.parent.pageEdit:aboutToShow()
		self.parent.pageEdit:setVisible(true)
	end
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function Page3:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.anchorBottom = true
	o.anchorRight = true
	o.nonDefaultOptions = SandboxOptions:new()
	return o
end

local SettingsTable

function Page3:create()
	local labelHgt = getTextManager():getFontFromEnum(UIFont.Title):getLineHeight()
	local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

	self.listbox = ISScrollingListBox:new(24, labelHgt + 24, 300, self.height - 60 - labelHgt - 24)
	self.listbox:initialise()
	self.listbox:setAnchorLeft(true)
	self.listbox:setAnchorRight(false)
	self.listbox:setAnchorTop(true)
	self.listbox:setAnchorBottom(true)
	self.listbox:setFont("Medium", 4)
	self.listbox.drawBorder = true
	self.listbox.doDrawItem = self.doDrawItem
	self.listbox:setOnMouseDownFunction(self, self.onMouseDownListBox)
	self:addChild(self.listbox)

	self.controls = {}
	self.groupBox = {}
	self.customui = {}
	for _,category in ipairs(SettingsTable) do
		self.controls[category.name] = {}
		self.groupBox[category.name] = {}
		local item = {}
		item.category = category
		self.listbox:addItem(category.name, item)
		for _,page in ipairs(category.pages) do
			if not page.steamOnly or getSteamModeActive() then
				item = {}
				item.page = page
				item.panel = self:createPanel(category, page)
				if page.customui then
					table.insert(self.customui, item.panel)
				end
				self.listbox:addItem(page.name, item)
			end
		end
	end

	self.buttonCancel = ISButton:new(self.width / 2 - 15 - 100, self.height - 5 - buttonHgt, 100, buttonHgt, getText("UI_btn_cancel"), self, self.onButtonCancel)
	self.buttonCancel:initialise()
	self.buttonCancel:setAnchorLeft(true)
	self.buttonCancel:setAnchorTop(false)
	self.buttonCancel:setAnchorBottom(true)
	self.buttonCancel.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.buttonCancel)

	self.buttonAccept = ISButton:new(self.width / 2 + 15, self.height - 5 - buttonHgt, 100, buttonHgt, getText("UI_btn_save"), self, self.onButtonSave)
	self.buttonAccept:initialise()
	self.buttonAccept:setAnchorLeft(true)
	self.buttonAccept:setAnchorTop(false)
	self.buttonAccept:setAnchorBottom(true)
	self.buttonAccept.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.buttonAccept)

	self.listbox.selected = 2
	self:onMouseDownListBox(self.listbox.items[2].item)
end

function Page3:createPanel(category, page)
	if page.customui then
		local panel = page.customui:new(self.listbox:getRight() + 24, self.listbox:getY(), self.width - 24 - self.listbox:getRight() - 24, self.listbox:getHeight())
		panel:initialise()
		panel:instantiate()
		panel:setAnchorRight(true)
		panel:setAnchorBottom(true)
		panel.pageEdit = self
		return panel
	end

	local panel
	if page.groupBox then
		panel = ServerSettingsScreenGroupBox:new(self.listbox:getRight() + 24, self.listbox:getY(),
			self.width - 24 - self.listbox:getRight() - 24, self.listbox:getHeight(),
			getText("Sandbox_" .. page.groupBox))
		self.groupBox[category.name][page.groupBox] = panel
	else
		panel = ServerSettingsScreenPanel:new(self.listbox:getRight() + 24, self.listbox:getY(), self.width - 24 - self.listbox:getRight() - 24, self.listbox:getHeight())
	end
	panel._instance = self
	panel:initialise()
	panel:instantiate()
	panel:setAnchorRight(true)
	panel:setAnchorBottom(true)
	panel.settingNames = {}
	panel.labels = {}
	panel.controls = {}

	local fontHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local entryHgt = fontHgt + 2 * 2

	local labels = {}
	local controls = {}
	for _,setting in ipairs(page.settings) do
		local settingName = setting.translatedName
		local tooltip = setting.tooltip
		if tooltip then
			tooltip = tooltip:gsub("\\n", "\n")
			tooltip = tooltip:gsub("\\\"", "\"")
		end
		local label = nil
		local control = nil
		if setting.name == "WaterShut" or setting.name == "ElecShut" then
			-- ignore
		elseif setting.type == "checkbox" then
			label = ISLabel:new(0, 0, entryHgt, settingName, 1, 1, 1, 1, UIFont.Medium)
			control = ISTickBox:new(0, 0, 100, entryHgt, "", nil, nil)
			control:addOption("")
			control.selected[1] = setting.default
			control.tooltip = tooltip
		elseif setting.type == "entry" or setting.type == "string" then
			label = ISLabel:new(0, 0, entryHgt, settingName, 1, 1, 1, 1, UIFont.Medium)
			control = ISTextEntryBox:new(setting.text, 0, 0, 300, entryHgt)
			control.font = UIFont.Medium
			control.tooltip = tooltip
			control:initialise()
			control:instantiate()
			control:setOnlyNumbers(setting.onlyNumbers or false)
		elseif setting.type == "enum" then
			label = ISLabel:new(0, 0, entryHgt, settingName, 1, 1, 1, 1, UIFont.Medium)
			control = ISComboBox:new(0, 0, 200, entryHgt, self, self.onComboBoxSelected, category.name, setting.name)
			if tooltip then
				control.tooltip = { defaultTooltip = tooltip }
			end
			control:initialise()
			for index,value in ipairs(setting.values) do
				control:addOption(value)
				if index == setting.default then
					control.selected = index
				end
			end
		elseif setting.type == "spinbox" then
			label = ISLabel:new(0, 0, entryHgt, settingName, 1, 1, 1, 1, UIFont.Medium)
			control = ISSpinBox:new(0, 0, 200, entryHgt, nil, nil)
			control:initialise()
			control:instantiate()
			if setting.name == "StartYear" then
				local firstYear = getSandboxOptions():getFirstYear()
				for i=1,100 do
					control:addOption(tostring(firstYear + i - 1))
				end
			elseif setting.name == "StartDay" then
			end
		elseif setting.type == "text" then
			label = ISLabel:new(0, 0, entryHgt, settingName, 1, 1, 1, 1, UIFont.Medium)
			control = ISTextEntryBox:new(setting.text, 0, 0, 300, 4 + fontHgt * 4 + 4)
			control.font = UIFont.Medium
			control:initialise()
			control:instantiate()
			control:setMultipleLine(true)
			control:setMaxLines(64)
			control:addScrollBars()
		end
		if label and control then
			label.tooltip = tooltip
			table.insert(labels, label)
			table.insert(controls, control)
			self.controls[category.name][setting.name] = control
			table.insert(panel.settingNames, setting.name)
			panel.labels[setting.name] = label
			panel.controls[setting.name] = control
		else
--			error "no label or control"
		end
	end
	local labelWidth = 0
	for _,label in ipairs(labels) do
		labelWidth = math.max(labelWidth, label:getWidth())
	end
	local x = 24
	local y = 12
	local addControlsTo = panel
	if page.groupBox then
		addControlsTo = panel.contents
		y = math.max(12, panel.tickBox.height / 2)
	end
	addControlsTo:setScrollChildren(true)
	addControlsTo:addScrollBars()
	addControlsTo.vscroll.doSetStencil = true
	for i=1,#labels do
		local label = labels[i]
		addControlsTo:addChild(label)
		label:setX(x + labelWidth - label:getWidth())
		label:setY(y)
		y = y + math.max(label:getHeight(), controls[i]:getHeight()) + 6;
	end
	y = 12
	if page.groupBox then
		y = math.max(12, panel.tickBox.height / 2)
	end
	for i=1,#controls do
		local label = labels[i]
		local control = controls[i]
		addControlsTo:addChild(control)
		control:setX(x + labelWidth + 16)
		control:setY(y)
		y = y + math.max(label:getHeight(), control:getHeight()) + 6;
        addControlsTo:setScrollHeight(y)
	end
	return panel
end

function Page3:doDrawItem(y, item, alt)
	self:drawRectBorder(0, y, self:getWidth(), self.itemheight - 1, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b)

	if item.item.category then
		self:drawRect(0, y, self:getWidth(), self.itemheight - 1, 0.3, 0.5, 0.5, 0.5)
		local tex = getTexture("media/ui/ArrowDown.png")
		self:drawTexture(tex, 4, y + (item.height - tex:getHeight()) / 2, 1, 1, 1, 1)
	elseif self.selected == item.index then
		self:drawRect(0, y, self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15)
	elseif self.mouseoverselected == item.index and not self:isMouseOverScrollBar() then
		self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 4, 0.95, 0.05, 0.05, 0.05);
	end

	local dx = item.item.page and 32 or 20
	local dy = (self.itemheight - getTextManager():getFontFromEnum(self.font):getLineHeight()) / 2
	self:drawText(item.text, dx, y + dy, 0.9, 0.9, 0.9, 0.9, self.font)

	return y + item.height
end

function Page3:onMouseDownListBox(item)
	if item.page then
		if self.currentPanel then
			self:removeChild(self.currentPanel)
			self.currentPanel = nil
		end
		if item.panel then
			self:addChild(item.panel)
			item.panel:setWidth(self.width - 24 - self.listbox:getRight() - 24)
			item.panel:setHeight(self.listbox:getHeight())
			self.currentPanel = item.panel
		end
	end
end

function Page3:onComboBoxSelected(combo, categoryName, optionName)
    if optionName == "Zombies" then
        local Zombies = combo.selected
        local popMult = { "4.0", "2.0", "1.0", "0.35", "0.0" }
        self.controls[categoryName]["ZombieConfig.PopulationMultiplier"]:setText(popMult[Zombies])
    end
end

function Page3:syncStartDay()
	local year = getSandboxOptions():getFirstYear();
	local month = self.controls.Sandbox.StartMonth.selected
	if self.selectedYear == year and self.selectedMonth == month then return end
	self.selectedYear = year
	self.selectedMonth = month
	
	local lastDay = getGameTime():daysInMonth(year, month - 1)
	local t = {}
	for i=1,lastDay do table.insert(t, tostring(i)) end
	self.controls.Sandbox.StartDay.options = t
	if self.controls.Sandbox.StartDay.selected > lastDay then
		self.controls.Sandbox.StartDay.selected = lastDay
	end
end

function Page3:updateWhenVisible()
	self:syncStartDay()
end

function Page3:prerender()
	ISPanelJoypad.prerender(self)
	-- This is used to highlight options with non-default values.
	self:settingsFromUIAux("Sandbox", self.nonDefaultOptions)
end

function Page3:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_ServerSettings_Title3", self.settings:getName()), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	if getDebug() then self:drawText("DEBUG: Page3", 8, 8, 0.5, 0.5, 0.5, 1, UIFont.Small) end
	self:updateWhenVisible()
end

function Page3:settingsToUIAux(category, options)
	for i=1,options:getNumOptions() do
		local option = options:getOptionByIndex(i-1)
		local control = self.controls[category][option:getName()]
		if control then
			if option:getType() == "boolean" then
				control.selected[1] = option:getValue()
			elseif option:getType() == "double" then
				control:setText(option:getValueAsString())
			elseif option:getType() == "enum" then
				control.selected = option:getValue()
			elseif option:getType() == "integer" then
				control:setText(option:getValueAsString())
			elseif option:getType() == "string" then
				control:setText(option:getValue())
			elseif option:getType() == "text" then
				control:setText(option:getValue())
			end
		end
	end
	for _,groupBox in pairs(self.groupBox[category]) do
		groupBox:settingsToUI(self.settings, category)
	end
end

function Page3:settingsToUI()
	self:settingsToUIAux("INI", self.settings:getServerOptions())
	self:settingsToUIAux("Sandbox", self.settings:getSandboxOptions())
end

function Page3:settingsFromUIAux(category, options)
	for i=1,options:getNumOptions() do
		local option = options:getOptionByIndex(i-1)
		local control = self.controls[category][option:getName()]
		if control then
			if option:getType() == "boolean" then
				option:setValue(control.selected[1] == true)
			elseif option:getType() == "double" then
				option:parse(control:getText())
			elseif option:getType() == "enum" then
				option:setValue(control.selected)
			elseif option:getType() == "integer" then
				option:parse(control:getText())
			elseif option:getType() == "string" then
				option:setValue(control:getText())
			elseif option:getType() == "text" then
				option:setValue(control:getText())
			end
		end
	end
end

function Page3:settingsFromUI()
	self:settingsFromUIAux("INI", self.settings:getServerOptions())
	self:settingsFromUIAux("Sandbox", self.settings:getSandboxOptions())
	for _,panel in ipairs(self.customui) do
		panel:settingsFromUI()
	end
end

function Page3:settingValueChanged(category, settingName, newValue)
	for _,panel in ipairs(self.customui) do
		if panel.settingValueChanged then
			panel:settingValueChanged(category, settingName, newValue)
		end
	end
end

function Page3:notify(message, arg1, arg2, arg3, arg4)
	for _,panel in ipairs(self.customui) do
		if panel.notify then
			panel:notify(message, arg1, arg2, arg3, arg4)
		end
	end
end

function Page3:aboutToShowStartScreen()
	for _,panel in ipairs(self.customui) do
		if panel.aboutToShowStartScreen then
			panel:aboutToShowStartScreen()
		end
	end
end

function Page3:aboutToShow()
	self:settingsToUI()
	for _,panel in ipairs(self.customui) do
		panel:setSettings(self.settings)
	end
end

function Page3:onButtonCancel()
	self:setVisible(false)
	self.parent.pageStart:setVisible(true)
end

function Page3:onButtonSave()
	self:settingsFromUI()
	self.settings:saveFiles()
	self:setVisible(false)
	self.parent.initialSelectedSettings = self.settings:getName()
	self.parent.pageStart:aboutToShow()
	self.parent.pageStart:setVisible(true)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function Page5:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.anchorBottom = true
	o.anchorRight = true
	return o
end

function Page5:create()
	local padX = 96

	local labelHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local label1 = ISLabel:new(padX, 96, labelHgt, getText("UI_ServerSettings_LabelRename"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label1)

	local entryWid = 400
	local entryHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight() + 2 * 2

	self.entry = ISTextEntryBox:new("xyz", label1:getX(), label1:getBottom() + 2, entryWid, entryHgt)
	self.entry.font = UIFont.Medium
	self.entry:initialise()
	self.entry:instantiate()
	self:addChild(self.entry)

	local label2 = ISLabel:new(padX, self.entry:getBottom() + 48, labelHgt, getText("UI_ServerSettings_LabelAffectedFiles"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label2)
	self.newFilesX = padX + 32
	self.newFilesY = label2:getBottom() + 4

	local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

	self.buttonCancel = ISButton:new(self.width / 2 - 15 - 100, self.height-64 + 8, 100, buttonHgt, getText("UI_btn_cancel"), self, self.onButtonCancel)
	self.buttonCancel:initialise()
	self.buttonCancel:setAnchorLeft(true)
	self.buttonCancel:setAnchorTop(false)
	self.buttonCancel:setAnchorBottom(true)
	self.buttonCancel.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.buttonCancel)

	self.buttonAccept = ISButton:new(self.width / 2 + 15, self.height-64 + 8, 100, buttonHgt, getText("UI_btn_rename"), self, self.onButtonAccept)
	self.buttonAccept:initialise()
	self.buttonAccept:setAnchorLeft(true)
	self.buttonAccept:setAnchorTop(false)
	self.buttonAccept:setAnchorBottom(true)
	self.buttonAccept.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.buttonAccept)
end

function Page5:updateWhenVisible()
	local newName = self.entry:getText()
	if self.checkName ~= newName then
		self.checkName = newName
		local nameValid = newName ~= self.settings:getName() and getServerSettingsManager():isValidNewName(newName)
		if nameValid then
			self.entry.borderColor.a = 1
			self.entry.borderColor.g = 0.4
			self.entry.borderColor.b = 0.4
		else
			self.entry.borderColor.a = 0.9
			self.entry.borderColor.g = 0.0
			self.entry.borderColor.b = 0.0
		end
		self.buttonAccept:setEnable(nameValid)
	end
end

function Page5:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_ServerSettings_Title5", self.settings:getName()), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	if getDebug() then self:drawText("DEBUG: Page5", 8, 8, 0.5, 0.5, 0.5, 1, UIFont.Small) end

	local oldName = self.settings:getName()
	local prefix = getServerSettingsManager():getNameInSettingsFolder(oldName)
	local suffixes = getServerSettingsManager():getSuffixes()
	local y = self.newFilesY
	local labelHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local maxWid = 0
	for i=1,suffixes:size() do
		if serverFileExists(oldName .. suffixes:get(i-1)) then
			local text = prefix .. suffixes:get(i-1)
			self:drawText(text, self.newFilesX, y, 1, 1, 1, 1, UIFont.Medium)
			y = y + labelHgt
			local textWid = getTextManager():MeasureStringX(UIFont.Medium, text)
			maxWid = math.max(maxWid, textWid)
		end
	end
	y = self.newFilesY
	for i=1,suffixes:size() do
		if serverFileExists(oldName .. suffixes:get(i-1)) then
			local text1 = "--->"
			local text2 = self.entry:getText() .. suffixes:get(i-1)
			if suffixes:get(i-1) == "_zombies.ini" then
				text2 = getText("UI_ServerSettings_WillBeDeleted")
			end
			local text1Wid = getTextManager():MeasureStringX(UIFont.Medium, text1)
			self:drawText(text1, self.newFilesX + maxWid + 16, y, 1, 1, 1, 1, UIFont.Medium)
			self:drawText(text2, self.newFilesX + maxWid + 16 + text1Wid + 16, y, 1, 1, 1, 1, UIFont.Medium)
			y = y + labelHgt
		end
	end
	
	self:updateWhenVisible()
end

function Page5:aboutToShow()
	self.checkName = nil
	self.entry:setText(self.settings:getName())
end

function Page5:onButtonCancel()
	self:setVisible(false)
	self.parent.pageStart:setVisible(true)
end

function Page5:onButtonAccept()
	local newName = self.entry:getText()
	if getServerSettingsManager():isValidNewName(newName) then
		self.settings:rename(newName)
	end
	self:setVisible(false)
	self.parent.pageStart:aboutToShow()
	self.parent.pageStart:setVisible(true)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function Page4:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.anchorBottom = true
	o.anchorRight = true
	return o
end

function Page4:create()
	local padX = 96

	local labelHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local label1 = ISLabel:new(padX, 96, labelHgt, getText("UI_ServerSettings_LabelDuplicate"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label1)

	local entryWid = 400
	local entryHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight() + 2 * 2

	self.entry = ISTextEntryBox:new("xyz", label1:getX(), label1:getBottom() + 2, entryWid, entryHgt)
	self.entry.font = UIFont.Medium
	self.entry:initialise()
	self.entry:instantiate()
	self:addChild(self.entry)

	local label2 = ISLabel:new(padX, self.entry:getBottom() + 48, labelHgt, getText("UI_ServerSettings_LabelAffectedFiles"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label2)
	self.newFilesX = padX + 32
	self.newFilesY = label2:getBottom() + 4

	local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

	self.buttonCancel = ISButton:new(self.width / 2 - 15 - 100, self.height-64 + 8, 100, buttonHgt, getText("UI_btn_cancel"), self, self.onButtonCancel)
	self.buttonCancel:initialise()
	self.buttonCancel:setAnchorLeft(true)
	self.buttonCancel:setAnchorTop(false)
	self.buttonCancel:setAnchorBottom(true)
	self.buttonCancel.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.buttonCancel)

	self.buttonAccept = ISButton:new(self.width / 2 + 15, self.height-64 + 8, 100, buttonHgt, getText("UI_btn_duplicate"), self, self.onButtonAccept)
	self.buttonAccept:initialise()
	self.buttonAccept:setAnchorLeft(true)
	self.buttonAccept:setAnchorTop(false)
	self.buttonAccept:setAnchorBottom(true)
	self.buttonAccept.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.buttonAccept)
end

function Page4:updateWhenVisible()
	local newName = self.entry:getText()
	if self.checkName ~= newName then
		self.checkName = newName
		local nameValid = newName ~= self.settings:getName() and getServerSettingsManager():isValidNewName(newName)
		if nameValid then
			self.entry.borderColor.a = 1
			self.entry.borderColor.g = 0.4
			self.entry.borderColor.b = 0.4
		else
			self.entry.borderColor.a = 0.9
			self.entry.borderColor.g = 0.0
			self.entry.borderColor.b = 0.0
		end
		self.buttonAccept:setEnable(nameValid)
	end
end

function Page4:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_ServerSettings_Title4", self.settings:getName()), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	if getDebug() then self:drawText("DEBUG: Page4", 8, 8, 0.5, 0.5, 0.5, 1, UIFont.Small) end

	local oldName = self.settings:getName()
	local prefix = getServerSettingsManager():getNameInSettingsFolder(oldName)
	local suffixes = getServerSettingsManager():getSuffixes()
	local y = self.newFilesY
	local labelHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local maxWid = 0
	for i=1,suffixes:size() do
		if serverFileExists(oldName .. suffixes:get(i-1)) then
			local text = prefix .. suffixes:get(i-1)
			self:drawText(text, self.newFilesX, y, 1, 1, 1, 1, UIFont.Medium)
			y = y + labelHgt
			local textWid = getTextManager():MeasureStringX(UIFont.Medium, text)
			maxWid = math.max(maxWid, textWid)
		end
	end
	y = self.newFilesY
	for i=1,suffixes:size() do
		if serverFileExists(oldName .. suffixes:get(i-1)) then
			local text1 = "--->"
			local text2 = self.entry:getText() .. suffixes:get(i-1)
			if suffixes:get(i-1) == "_zombies.ini" then
				text2 = getText("UI_ServerSettings_WillBeDeleted")
			end
			local text1Wid = getTextManager():MeasureStringX(UIFont.Medium, text1)
			self:drawText(text1, self.newFilesX + maxWid + 16, y, 1, 1, 1, 1, UIFont.Medium)
			self:drawText(text2, self.newFilesX + maxWid + 16 + text1Wid + 16, y, 1, 1, 1, 1, UIFont.Medium)
			y = y + labelHgt
		end
	end
	
	self:updateWhenVisible()
end

function Page4:aboutToShow()
	self.checkName = nil
	if getServerSettingsManager():isValidNewName(self.settings:getName() .. "-Copy") then
		self.entry:setText(self.settings:getName() .. "-Copy")
	else
		local n = 2
		while not getServerSettingsManager():isValidNewName(self.settings:getName() .. "-Copy-" .. n) do
			n = n + 1
		end
		self.entry:setText(self.settings:getName() .. "-Copy-" .. n)
	end
end

function Page4:onButtonCancel()
	self:setVisible(false)
	self.parent.pageStart:setVisible(true)
end

function Page4:onButtonAccept()
	local newName = self.entry:getText()
	if getServerSettingsManager():isValidNewName(newName) then
		self.settings:duplicateFiles(newName)
	end
	self:setVisible(false)
	self.parent.pageStart:aboutToShow()
	self.parent.pageStart:setVisible(true)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function Page6:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.anchorBottom = true
	o.anchorRight = true
	return o
end

function Page6:create()
	local padX = 96

	local labelHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local label1 = ISLabel:new(padX, 96, labelHgt, getText("UI_ServerSettings_LabelAffectedFiles"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label1)
	self.newFilesX = padX + 32
	self.newFilesY = label1:getBottom() + 4

	local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

	self.buttonCancel = ISButton:new(self.width / 2 - 15 - 100, self.height-64 + 8, 100, buttonHgt, getText("UI_btn_cancel"), self, self.onButtonCancel)
	self.buttonCancel:initialise()
	self.buttonCancel:setAnchorLeft(true)
	self.buttonCancel:setAnchorTop(false)
	self.buttonCancel:setAnchorBottom(true)
	self.buttonCancel.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.buttonCancel)

	self.buttonAccept = ISButton:new(self.width / 2 + 15, self.height-64 + 8, 100, buttonHgt, getText("UI_btn_delete"), self, self.onButtonAccept)
	self.buttonAccept:initialise()
	self.buttonAccept:setAnchorLeft(true)
	self.buttonAccept:setAnchorTop(false)
	self.buttonAccept:setAnchorBottom(true)
	self.buttonAccept.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.buttonAccept)
end

function Page6:updateWhenVisible()
end

function Page6:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_ServerSettings_Title6", self.settings:getName()), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	if getDebug() then self:drawText("DEBUG: Page6", 8, 8, 0.5, 0.5, 0.5, 1, UIFont.Small) end

	local prefix = getServerSettingsManager():getNameInSettingsFolder(self.settings:getName())
	local suffixes = getServerSettingsManager():getSuffixes()
	local y = self.newFilesY
	local labelHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local maxWid = 0
	for i=1,suffixes:size() do
		if serverFileExists(self.settings:getName() .. suffixes:get(i-1)) then
			local text = prefix .. suffixes:get(i-1)
			self:drawText(text, self.newFilesX, y, 1, 1, 1, 1, UIFont.Medium)
			y = y + labelHgt
			local textWid = getTextManager():MeasureStringX(UIFont.Medium, text)
			maxWid = math.max(maxWid, textWid)
		end
	end
	y = self.newFilesY
	for i=1,suffixes:size() do
		if serverFileExists(self.settings:getName() .. suffixes:get(i-1)) then
			local text1 = "--->"
			local text2 = getText("UI_ServerSettings_WillBeDeleted")
			local text1Wid = getTextManager():MeasureStringX(UIFont.Medium, text1)
			self:drawText(text1, self.newFilesX + maxWid + 16, y, 1, 1, 1, 1, UIFont.Medium)
			self:drawText(text2, self.newFilesX + maxWid + 16 + text1Wid + 16, y, 1, 1, 1, 1, UIFont.Medium)
			y = y + labelHgt
		end
	end

	self:updateWhenVisible()
end

function Page6:aboutToShow()
end

function Page6:onButtonCancel()
	self:setVisible(false)
	self.parent.pageStart:setVisible(true)
end

function Page6:onButtonAccept()
	self.settings:deleteFiles()
	self:setVisible(false)
	self.parent.pageStart:aboutToShow()
	self.parent.pageStart:setVisible(true)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function Page7:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.anchorBottom = true
	o.anchorRight = true
	return o
end

function Page7:create()
	local labelHgt = getTextManager():getFontFromEnum(UIFont.Title):getLineHeight()
	local fontHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	
	local label = ISLabel:new(24, labelHgt + 24, fontHgt, getText("UI_ServerSettings_ListOfSpawnProfessions"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label)

	local buttonWid = 200
	local buttonHgt = 40
	local buttonGapY = 10

	self.profListBox = ServerSettingsScreenMapsListBox:new(24, label:getBottom() + 4, math.min(self.width - 24 * 3 - buttonWid, 400), self.height - 24 * 2)
	self.profListBox:initialise()
	self.profListBox:instantiate()
	self.profListBox:setAnchorLeft(true)
	self.profListBox:setAnchorRight(false)
	self.profListBox:setAnchorTop(true)
	self.profListBox:setAnchorBottom(false)
	self.profListBox:setFont("Medium", 4)
	self.profListBox.drawBorder = true
	self.profListBox:setHeight(self.profListBox.itemheight * 8)
	self.profListBox:ignoreHeightChange()
	self.profListBox.vscroll:setHeight(self.profListBox.height)
	self.profListBox:setOnMouseDownFunction(self, self.onMouseDownProfession)
	self.profListBox.onRemoveItem = self.onRemoveProfession
	self:addChild(self.profListBox)

	label = ISLabel:new(self.profListBox:getX(), self.profListBox:getBottom() + 12, fontHgt, getText("UI_ServerSettings_AddSpawnProfession"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label)

	local comboBox = ISComboBox:new(label:getX(), label:getBottom() + 4, self.profListBox:getWidth(), fontHgt + 4, self, self.onAddProfession)
	comboBox:setToolTipMap({ defaultTooltip = getText("UI_ServerSettings_AddSpawnProfession_tooltip") })
	self:addChild(comboBox)
	self.profComboBox = comboBox

	label = ISLabel:new(self.profListBox:getX(), comboBox:getBottom() + 12, 20, getText("UI_ServerSettings_AddOtherSpawnProfession"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label)

	local entry = ISTextEntryBox:new("", self.profListBox:getX(), label:getBottom() + 4, self.profListBox:getWidth(), fontHgt + 4)
	entry.font = UIFont.Medium
	entry.tooltip = getTooltipText("UI_ServerSettings_AddOtherSpawnProfession_tooltip")
	entry.onCommandEntered = self.onAddOtherProfession
	self:addChild(entry)

	label = ISLabel:new(self.profListBox:getRight() + 24, labelHgt + 24, fontHgt, getText("UI_ServerSettings_ListOfSpawnPoints"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label)

	self.pointListBox = ServerSettingsScreenMapsListBox:new(self.profListBox:getRight() + 24, self.profListBox:getY(), math.min(self.width - 24 * 3 - buttonWid, 400), self.height - 24 * 2)
	self.pointListBox:initialise()
	self.pointListBox:instantiate()
	self.pointListBox:setAnchorLeft(true)
	self.pointListBox:setAnchorRight(false)
	self.pointListBox:setAnchorTop(true)
	self.pointListBox:setAnchorBottom(false)
	self.pointListBox:setFont("Medium", 4)
	self.pointListBox.drawBorder = true
	self.pointListBox:setHeight(self.pointListBox.itemheight * 8)
	self.pointListBox:ignoreHeightChange()
	self.pointListBox.vscroll:setHeight(self.pointListBox.height)
	self.pointListBox.onRemoveItem = self.onRemovePoint
	self:addChild(self.pointListBox)

	label = ISLabel:new(self.pointListBox:getX(), self.pointListBox:getBottom() + 12, 20, getText("UI_ServerSettings_AddSpawnPoint"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label)

	entry = ISTextEntryBox:new("", self.pointListBox:getX(), label:getBottom() + 4, self.pointListBox:getWidth(), fontHgt + 4)
	entry.font = UIFont.Medium
	entry.tooltip = getTooltipText("UI_ServerSettings_AddSpawnPoint_tooltip")
	entry.onCommandEntered = self.onAddPoint
	self:addChild(entry)

	self.buttonCancel = ISButton:new(self.width / 2 - 15 - 100, self.height-64 + 8, 100, 25, getText("UI_btn_cancel"), self, self.onButtonCancel)
	self.buttonCancel:initialise()
	self.buttonCancel:setAnchorLeft(true)
	self.buttonCancel:setAnchorTop(false)
	self.buttonCancel:setAnchorBottom(true)
	self.buttonCancel.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.buttonCancel)

	self.buttonAccept = ISButton:new(self.width / 2 + 15, self.height-64 + 8, 100, 25, getText("UI_btn_save"), self, self.onButtonAccept)
	self.buttonAccept:initialise()
	self.buttonAccept:setAnchorLeft(true)
	self.buttonAccept:setAnchorTop(false)
	self.buttonAccept:setAnchorBottom(true)
	self.buttonAccept.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.buttonAccept)
end

function Page7:updateWhenVisible()
	local hasUnemployed = false
	local noPoints = false
	for _,item in ipairs(self.profListBox.items) do
		if item.item.professionType == "unemployed" then
			hasUnemployed = true
		end
		if #item.item.points == 0 then
			noPoints = true
		end
	end
	if hasUnemployed and not noPoints then
		self.buttonAccept:setEnable(true)
		self.buttonAccept.tooltip = nil
	else
		self.buttonAccept:setEnable(false)
		self.buttonAccept.tooltip = getTooltipText("UI_ServerSettings_UnemployedRequired")
	end
end

function Page7:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_ServerSettings_Title7", self.region.file), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	if getDebug() then self:drawText("DEBUG: Page7", 8, 8, 0.5, 0.5, 0.5, 1, UIFont.Small) end
	self:updateWhenVisible()
end

function Page7:aboutToShow()
	self:fillProfessionList()
	self:fillPointList()
	self:fillProfessionCombo()
end

function Page7:fillProfessionList()
	local professionsTable = self.settings:loadSpawnPointsFile(self.region.file)
	self.profListBox:clear()
	if professionsTable then
		for professionType,pointsTable in pairs(professionsTable) do
			self:addProfessionToList(professionType, pointsTable, false)
		end
	end
end

function Page7:addProfessionToList(professionType, pointsTable, select)
	local profession = ProfessionFactory.getProfession(professionType)
	local data = {}
	data.professionType = professionType
	data.points = pointsTable
	data.otherText = profession and professionType
	self.profListBox:addItem(profession and profession:getLabel() or professionType, data)
	if select then
		self.profListBox.selected = #self.profListBox.items
		self.profListBox:ensureVisible(self.profListBox.selected)
		self:fillPointList()
	end
end

function Page7:isProfessionInList(professionType)
	for _,item in ipairs(self.profListBox.items) do
		if item.item.professionType == professionType then
			return true
		end
	end
	return false
end

function Page7:fillProfessionCombo()
	self.profComboBox:clear()
	local professions = ProfessionFactory.getProfessions()
	local sorted = {}
	for i=1,professions:size() do
		local profession = professions:get(i-1)
		table.insert(sorted, profession)
	end
	table.sort(sorted, function(a,b) return not string.sort(a:getLabel(), b:getLabel()) end)
	for _,profession in ipairs(sorted) do
		if not self:isProfessionInList(profession:getType()) then
			self.profComboBox:addOptionWithData(profession:getLabel(), profession)
		end
	end
	self.profComboBox.selected = 1
end

function Page7:fillPointList()
	self.pointListBox:clear()
	local item = self.profListBox.items[self.profListBox.selected]
	if not item then return end
	local pointsTable = item.item.points
	for _,pointTable in ipairs(pointsTable) do
		self:addPointToList(pointTable, false)
	end
end

function Page7:addPointToList(pointTable, select)
	local x = pointTable.worldX * 300 + pointTable.posX
	local y = pointTable.worldY * 300 + pointTable.posY
	local z = pointTable.posZ
	self.pointListBox:addItem(x .. "," .. y .. "," .. z, {})
	if select then
		self.pointListBox.selected = #self.pointListBox.items
		self.pointListBox:ensureVisible(self.pointListBox.selected)
	end
end

function Page7:onMouseDownProfession(data)
	self:fillPointList()
end

function Page7:onAddProfession()
	local profession = self.profComboBox.options[self.profComboBox.selected].data
	self:addProfessionToList(profession:getType(), {}, true)
	self:fillProfessionCombo()
end

function Page7:onAddOtherProfession()
	local professionType = self:getText()
	if professionType == "" or self.parent:isProfessionInList(professionType) then return end
	self:setText("")
	self.parent:addProfessionToList(professionType, {}, true)
	self.parent:fillProfessionCombo()
end

function Page7:onRemoveProfession(index)
	local data = self.items[index].item
	self:removeItemByIndex(index)
	self.parent:fillPointList()
	self.parent:fillProfessionCombo()
end

function Page7:onAddPoint()
	if not self.parent.profListBox.items[self.parent.profListBox.selected] then return end
	local ss = self:getText():split(",")
	if #ss ~= 3 then return end
	local x = tonumber(ss[1])
	local y = tonumber(ss[2])
	local z = tonumber(ss[3])
	if not x or x < 0 or not y or y < 0 or not z or z < 0 or z > 7 then return end
	self:setText("")
	local point = { worldX = math.floor(x / 300), worldY = math.floor(y / 300), posX = (x - math.floor(x / 300) * 300), posY = (y - math.floor(y / 300) * 300), posZ = z }
	local data = self.parent.profListBox.items[self.parent.profListBox.selected].item
	table.insert(data.points, point)
	self.parent:addPointToList(point, true)
end

function Page7:onRemovePoint(index)
	self:removeItemByIndex(index)
	local data = self.parent.profListBox.items[self.parent.profListBox.selected].item
	table.remove(data.points, index)
end

function Page7:onButtonCancel()
	self:setVisible(false)
	self.parent.pageEdit:setVisible(true)
end

function Page7:onButtonAccept()
	local professions = {}
	for _,item in ipairs(self.profListBox.items) do
		professions[item.item.professionType] = item.item.points
	end
	self.settings:saveSpawnPointsFile(self.region.file, professions)
	self:setVisible(false)
--	self.parent.pageEdit:aboutToShow()
	self.parent.pageEdit:setVisible(true)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function ServerSettingsScreen:create()
	self.pageStart = Page1:new(0, 0, self.width, self.height)
	self.pageStart:create()
	self:addChild(self.pageStart)

	self.pageNew = Page2:new(0, 0, self.width, self.height)
	self.pageNew:create()
	self:addChild(self.pageNew)

	self.pageEdit = Page3:new(0, 0, self.width, self.height)
	self.pageEdit:create()
	self:addChild(self.pageEdit)

	self.pageDuplicate = Page4:new(0, 0, self.width, self.height)
	self.pageDuplicate:create()
	self:addChild(self.pageDuplicate)

	self.pageRename = Page5:new(0, 0, self.width, self.height)
	self.pageRename:create()
	self:addChild(self.pageRename)

	self.pageDelete = Page6:new(0, 0, self.width, self.height)
	self.pageDelete:create()
	self:addChild(self.pageDelete)

	self.pageSpawnPoints = Page7:new(0, 0, self.width, self.height)
	self.pageSpawnPoints:create()
	self:addChild(self.pageSpawnPoints)
end

function ServerSettingsScreen:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	ServerSettingsScreen.instance = o
	return o
end

function ServerSettingsScreen:aboutToShow()
	self.pageNew:setVisible(false)
	self.pageEdit:setVisible(false)
	self.pageDuplicate:setVisible(false)
	self.pageRename:setVisible(false)
	self.pageDelete:setVisible(false)
	self.pageSpawnPoints:setVisible(false)

	self.pageEdit:aboutToShowStartScreen()

	self.pageStart:aboutToShow()
	self.pageStart:setVisible(true)
end

function ServerSettingsScreen:onResolutionChange(oldw, oldh, neww, newh)
end

function ServerSettingsScreen:onGainJoypadFocus(joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData)
    joypadData.focus = self.pageStart
    updateJoypadFocus(joypadData)
end

function ServerSettingsScreen.getSandboxSettingsTable()
	local temp = SettingsTable[2].pages[1]
	SettingsTable[2].pages[1] = nil -- remove customui page
	local _table = copyTable(SettingsTable[2].pages)
	SettingsTable[2].pages[1] = temp
	table.remove(_table, 1) -- Presets page
	return _table
end

function ServerSettingsScreen:onJoypadBeforeDeactivate(joypadData)
	-- focus is on self.pageStart
	self.joyfocus = nil
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

-- declared 'local' above
SettingsTable = {
	{
		name = "INI",
		pages = {
			{
				name = "Details",
				settings = {
					{ name = "DefaultPort" },
					{ name = "PublicName" },
					{ name = "PublicDescription" },
					{ name = "Public" },
					{ name = "Password" },
					{ name = "PauseEmpty" },
					{ name = "ResetID" },
				},
			},
			{
				name = "Steam",
				steamOnly = true,
				settings = {
					{ name = "SteamPort1" },
					{ name = "SteamVAC" },
					{ name = "MaxAccountsPerUser" },
					{ name = "SteamScoreboard" },
				},
			},
			{
				name = "SteamWorkshop",
				steamOnly = true,
				customui = ServerSettingsScreenWorkshopPanel,
				settings = {},
			},
			{
				name = "Mods",
				customui = ServerSettingsScreenModsPanel,
				settings = {},
			},
			{
				name = "Map",
				customui = ServerSettingsScreenMapsPanel,
				settings = {},
			},
			{
				name = "SpawnRegions",
				settings = {},
				customui = SpawnRegionsPanel,
			},
			{
				name = "Players",
				settings = {
					{ name = "MaxPlayers" },
					{ name = "Open" },
					{ name = "AutoCreateUserInWhiteList" },
					{ name = "DropOffWhiteListAfterDeath" },
					{ name = "DisplayUserName" },
					{ name = "SpawnItems" },
					{ name = "PingFrequency" },
					{ name = "PingLimit" },
					{ name = "KickFastPlayers" },
					{ name = "ServerPlayerID" },
					{ name = "SleepAllowed" },
					{ name = "SleepNeeded" },
					{ name = "PlayerRespawnWithSelf" },
					{ name = "PlayerRespawnWithOther" },
					{ name = "AllowTradeUI", },
					{ name = "RemovePlayerCorpsesOnCorpseRemoval" },
					{ name = "TrashDeleteAll" },
					{ name = "PVPMeleeWhileHitReaction" },
					{ name = "MouseOverToSeeDisplayName" },
					{ name = "HidePlayersBehindYou" },
					{ name = "PlayerBumpPlayer" },
				},
			},
			{
				name = "Admin",
				settings = {
					{ name = "ClientCommandFilter" },
					{ name = "DisableRadioStaff" },
					{ name = "DisableRadioAdmin" },
					{ name = "DisableRadioGM" },
					{ name = "DisableRadioOverseer" },
					{ name = "DisableRadioModerator" },
					{ name = "DisableRadioInvisible" },
				},
			},
			{
				name = "Fire",
				settings = {
					{ name = "NoFire" },
				},
			},
			{
				name = "PVP",
				settings = {
					{ name = "PVP" },
					{ name = "SafetySystem" },
					{ name = "ShowSafety" },
					{ name = "SafetyToggleTimer" },
					{ name = "SafetyCooldownTimer" },
					{ name = "PVPMeleeDamageModifier" },
					{ name = "PVPFirearmDamageModifier" },
				},
			},
			{
				name = "Loot",
				settings = {
					{ name = "HoursForLootRespawn" },
					{ name = "MaxItemsForLootRespawn" },
					{ name = "ConstructionPreventsLootRespawn" },
					{ name = "ItemNumbersLimitPerContainer" },
				},
			},
			{
				name = "Faction",
				settings = {
					{ name = "Faction" },
					{ name = "FactionDaySurvivedToCreate" },
					{ name = "FactionPlayersRequiredForTag" },
				},
			},
			{
				name = "Safehouse",
				settings = {
					{ name = "AdminSafehouse" },
					{ name = "PlayerSafehouse" },
					{ name = "SafehouseAllowTrepass" },
					{ name = "SafehouseAllowFire" },
					{ name = "SafehouseAllowLoot" },
					{ name = "SafehouseAllowRespawn" },
					{ name = "SafehouseDaySurvivedToClaim" },
					{ name = "SafeHouseRemovalTime" },
					{ name = "DisableSafehouseWhenPlayerConnected", }
				},
			},
			{
				name = "Chat",
				settings = {
					{ name = "GlobalChat" },
					{ name = "LogLocalChat" },
					{ name = "AnnounceDeath" },
					{ name = "ServerWelcomeMessage" },
				},
			},
			{
				name = "RCON",
				settings = {
					{ name = "RCONPort" },
					{ name = "RCONPassword" },
				},
			},
			{
				name = "Discord",
				settings = {
					{ name = "DiscordEnable" },
					{ name = "DiscordToken" },
					{ name = "DiscordChannel" },
				},
			},
			{
				name = "UPnP",
				settings = {
					{ name = "UPnP" },
					{ name = "UPnPLeaseTime" },
					{ name = "UPnPZeroLeaseTimeFallback" },
					{ name = "UPnPForce" },
				},
			},
			{
				name = "Other",
				settings = {
					{ name = "DoLuaChecksum" },
					{ name = "AllowDestructionBySledgehammer" },
					{ name = "MinutesPerPage" },
					{ name = "nightlengthmodifier" },
					{ name = "SaveWorldEveryMinutes" },
                    { name = "FastForwardMultiplier" },
                    { name = "PlayerSaveOnDamage" },
                    { name = "SaveTransactionID" },
					{ name = "BloodSplatLifespanDays" },
					{ name = "AllowNonAsciiUsername" },
				},
			},
			{
				name = "Vehicles",
				settings = {
					{ name = "PhysicsDelay" },
					{ name = "SpeedLimit" },
				},
			},
			{
				name = "Voice",
				settings = {
					{ name = "VoiceEnable" },
					{ name = "VoiceComplexity" },
					{ name = "VoicePeriod" },
					{ name = "VoiceSampleRate" },
					{ name = "VoiceBuffering" },
					{ name = "VoiceMinDistance" },
					{ name = "VoiceMaxDistance" },
					{ name = "Voice3D" },
				},
			},
		}
	},
	{
		name = "Sandbox",
		pages = {
			{
				title = getText("UI_ServerSettingsGroup_SandboxPresets"),
				settings = {},
				customui = SandboxPresetPanel,
			},
			{
				name = "PopulationOptions",
				settings = {
					{ name = "Zombies" },
					{ name = "Distribution" },
				}
			},
			{
				name = "TimeOptions",
				settings = {
					{ name = "DayLength" },
					{ name = "StartMonth" },
					{ name = "StartDay" },
					{ name = "StartTime" },
				},
			},
			{
				name = "WorldOptions",
				settings = {
					{ name = "WaterShutModifier" },
					{ name = "ElecShutModifier" },
					{ name = "WaterShut" },
					{ name = "ElecShut" },
					{ name = "Alarm" },
					{ name = "LockedHouses" },
					{ name = "FoodRotSpeed" },
					{ name = "FridgeFactor" },
					{ name = "DaysForRottenFoodRemoval" },
					{ name = "LootRespawn" },
					{ name = "SeenHoursPreventLootRespawn" },
					{ name = "WorldItemRemovalList" },
					{ name = "HoursForWorldItemRemoval" },
					{ name = "ItemRemovalListBlacklistToggle" },
					{ name = "TimeSinceApo" },
					{ name = "NightDarkness" },
					{ name = "FireSpread" },
					{ name = "AllowExteriorGenerator" },
					{ name = "FuelStationGas" },
				},
			},
			{
				name = "NatureOptions",
				settings = {
					{ name = "Temperature" },
					{ name = "Rain" },
					{ name = "ErosionSpeed" },
					{ name = "ErosionDays" },
					{ name = "Farming" },
					{ name = "PlantResilience" },
					{ name = "PlantAbundance" },
					{ name = "NatureAbundance" },
					{ name = "CompostTime" },
                    { name = "MaxFogIntensity" },
                    { name = "MaxRainFxIntensity" },
                    { name = "EnableSnowOnGround" },
				},
			},
            {
                name = "SadisticAIDirector",
                settings = {
                    { name = "Helicopter" },
                    { name = "MetaEvent" },
                    { name = "SleepingEvent" },
                },
            },
			{
				name = "Meta",
				settings = {
					{ name = "GeneratorSpawning" },
					{ name = "GeneratorFuelConsumption" },
					{ name = "SurvivorHouseChance" },
					{ name = "VehicleStoryChance" },
					{ name = "ZoneStoryChance" },
					{ name = "AnnotatedMapChance" },
					{ name = "HoursForCorpseRemoval" },
					{ name = "DecayingCorpseHealthImpact" },
					{ name = "BloodLevel" },
				},
			},
			{
				name = "LootRarity",
				settings = {
					{ name = "FoodLoot"},
					{ name = "CannedFoodLoot"},
					{ name = "WeaponLoot" },
					{ name = "RangedWeaponLoot" },
					{ name = "AmmoLoot" },
					{ name = "MedicalLoot" },
					{ name = "SurvivalGearsLoot" },
					{ name = "MechanicsLoot"},
					{ name = "LiteratureLoot"},
					{ name = "OtherLoot" },
				},
			},
			{
				name = "Character",
				settings = {
					{ name = "XpMultiplier" },
					{ name = "StatsDecrease" },
					{ name = "EndRegen" },
					{ name = "Nutrition" },
					{ name = "StarterKit" },
					{ name = "CharacterFreePoints" },
					{ name = "ConstructionBonusPoints" },
					{ name = "InjurySeverity" },
					{ name = "BoneFracture" },
					{ name = "ClothingDegradation" },
					{ name = "RearVulnerability" },
					{ name = "MultiHitZombies" },
					{ name = "AttackBlockMovements" },
					{ name = "AllClothesUnlocked" },
				},
			},
			{
				name = "Map",
				settings = {
					{ name = "Map.AllowMiniMap" },
					{ name = "Map.AllowWorldMap" },
					{ name = "Map.MapAllKnown" },
				},
			},
			{
				name = "Vehicle",
				settings = {
					{ name = "EnableVehicles" },
					{ name = "VehicleEasyUse" },
					{ name = "RecentlySurvivorVehicles" },
					{ name = "ZombieAttractionMultiplier" },
					{ name = "CarSpawnRate" },
					{ name = "ChanceHasGas" },
					{ name = "InitialGas" },
					{ name = "CarGasConsumption" },
					{ name = "LockedCar" },
					{ name = "CarGeneralCondition" },
					{ name = "TrafficJam" },
					{ name = "CarAlarm" },
					{ name = "PlayerDamageFromCrash" },
					{ name = "CarDamageOnImpact" },
					{ name = "SirenShutoffHours" },
					{ name = "DamageToPlayerFromHitByACar" },
				},
			},
			{
				name = "ZombieLore",
				groupBox = "ProperZombies",
				settings = {
					{ name = "ZombieLore.Speed" },
					{ name = "ZombieLore.Strength" },
					{ name = "ZombieLore.Toughness" },
					{ name = "ZombieLore.Transmission" },
					{ name = "ZombieLore.Mortality" },
					{ name = "ZombieLore.Reanimate" },
					{ name = "ZombieLore.Cognition" },
					{ name = "ZombieLore.CrawlUnderVehicle" },
					{ name = "ZombieLore.Memory" },
					{ name = "ZombieLore.Decomp" },
					{ name = "ZombieLore.Sight" },
					{ name = "ZombieLore.Hearing" },
--					{ name = "ZombieLore.Smell" },
					{ name = "ZombieLore.ThumpNoChasing" },
					{ name = "ZombieLore.ThumpOnConstruction" },
					{ name = "ZombieLore.ActiveOnly" },
					{ name = "ZombieLore.TriggerHouseAlarm" },
					{ name = "ZombieLore.ZombiesDragDown" },
					{ name = "ZombieLore.ZombiesFenceLunge" },
				},
			},
			{
				name = "ZombieAdvanced",
				settings = {
					{ name = "ZombieConfig.PopulationMultiplier" },
					{ name = "ZombieConfig.PopulationStartMultiplier" },
					{ name = "ZombieConfig.PopulationPeakMultiplier" },
					{ name = "ZombieConfig.PopulationPeakDay" },
					{ name = "ZombieConfig.RespawnHours" },
					{ name = "ZombieConfig.RespawnUnseenHours" },
					{ name = "ZombieConfig.RespawnMultiplier" },
					{ name = "ZombieConfig.RedistributeHours" },
					{ name = "ZombieConfig.FollowSoundDistance" },
					{ name = "ZombieConfig.RallyGroupSize" },
					{ name = "ZombieConfig.RallyTravelDistance" },
					{ name = "ZombieConfig.RallyGroupSeparation" },
					{ name = "ZombieConfig.RallyGroupRadius" },
				}
			},
		},
	},
}

local serverOptions = ServerOptions:new()
local missedSettings = {}
for i=1,serverOptions:getNumOptions() do
	missedSettings[serverOptions:getOptionByIndex(i-1):getName()] = true
end
for _,page in ipairs(SettingsTable[1].pages) do
	page.name = getText("UI_ServerSettingGroup_" .. page.name)
	for _,setting in ipairs(page.settings) do
		local option = serverOptions:getOptionByName(setting.name)
		if not option then error('unknown server option "' .. setting.name .. "'") end
		option = option:asConfigOption()
		setting.translatedName = setting.name -- option:getTranslatedName()
		setting.tooltip = option:getTooltip()
		if option:getType() == "boolean" then
			setting.type = "checkbox"
			setting.default = option:getDefaultValue()
		elseif option:getType() == "double" then
			setting.type = "entry"
			setting.text = option:getValueAsString()
			setting.onlyNumbers = false -- TODO: UITextBox2 handle floating-point
		elseif option:getType() == "enum" then
			setting.type = "enum"
			setting.values = {}
			for k=1,option:getNumValues() do
				table.insert(setting.values, option:getValueTranslationByIndex(k))
			end
			setting.default = option:getDefaultValue();
		elseif option:getType() == "integer" then
			setting.type = "entry"
			setting.text = option:getValueAsString()
			setting.onlyNumbers = true
		elseif option:getType() == "string" then
			setting.type = "string"
			setting.text = option:getValueAsString()
		elseif option:getType() == "text" then
			setting.type = "text"
			setting.text = option:getValueAsString()
		else
			error("unknown server option class " .. tostring(option:getType()))
		end
		missedSettings[option:getName()] = nil
	end
end

local pageByName = {}
for _,page in ipairs(SettingsTable[2].pages) do
	local pageName = page.title or page.name
	pageByName[pageName] = page
end

for i=1,getSandboxOptions():getNumOptions() do
	local option = getSandboxOptions():getOptionByIndex(i-1)
	if option:isCustom() and option:getPageName() ~= nil then
		local page = pageByName[option:getPageName()]
		if not page then
			page = {}
			page.name = option:getPageName()
			page.settings = {}
			table.insert(SettingsTable[2].pages, page)
			pageByName[page.name] = page
		end
		table.insert(page.settings, { name = option:getName() })
	end
	missedSettings[option:getName()] = true
end

for _,page in ipairs(SettingsTable[2].pages) do
	page.name = page.title or getText("Sandbox_" .. page.name)
	for _,setting in ipairs(page.settings) do
		local option = getSandboxOptions():getOptionByName(setting.name)
		if not option then error('unknown sandbox option "' .. setting.name .. "'") end
--		option = option:asConfigOption()
		setting.translatedName = option:getTranslatedName()
		setting.tooltip = option:getTooltip()
		if option:getType() == "boolean" then
			setting.type = "checkbox"
			setting.default = option:getDefaultValue()
		elseif option:getType() == "double" then
			setting.type = "entry"
			setting.text = option:getValueAsString()
			setting.onlyNumbers = false -- TODO: UITextBox2 handle floating-point
		elseif option:getType() == "enum" then
			setting.type = "enum"
			setting.values = {}
			for k=1,option:getNumValues() do
				if setting.name == "StartYear" then
					table.insert(setting.values, tostring(getSandboxOptions():getFirstYear() + k - 1))
				elseif setting.name == "StartDay" then
					table.insert(setting.values, tostring(k))
				else
					table.insert(setting.values, option:getValueTranslationByIndex(k))
				end
			end
			setting.default = option:getDefaultValue();
		elseif option:getType() == "integer" then
			setting.type = "entry"
			setting.text = option:getValueAsString()
			setting.onlyNumbers = true
		elseif option:getType() == "string" then
			setting.type = "string"
			setting.text = option:getValue()
		elseif option:getType() == "text" then
			setting.type = "text"
			setting.text = option:getValue()
		else
			error("unknown sandbox option type " .. tostring(option:getType()))
		end
		missedSettings[option:getName()] = nil
	end
end
for key,value in pairs(missedSettings) do
	print('MISSING in SettingsTable: ' .. key)
end
