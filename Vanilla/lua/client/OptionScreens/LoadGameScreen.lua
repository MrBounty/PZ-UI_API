if isDemo() then return end

require "ISUI/ISPanel"
require "ISUI/ISButton"
require "ISUI/ISInventoryPane"
require "ISUI/ISResizeWidget"
require "ISUI/ISMouseDrag"

require "defines"

LoadGameScreen = ISPanelJoypad:derive("LoadGameScreen");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_LARGE = getTextManager():getFontHeight(UIFont.Large)
local FONT_HGT_TITLE = getTextManager():getFontHeight(UIFont.Title)

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

local SaveInfoPanel = ISPanelJoypad:derive("LoadGameScreen_InfoPanel")

function SaveInfoPanel:createChildren()
    self.richText = ISRichTextPanel:new(0, 10, 200, 200)
    self.richText:initialise()
    self.richText:instantiate()
    self.richText.background = false
    self.richText.marginLeft = 10
    self.richText:setAnchorBottom(true)
    self.richText:setAnchorRight(true)
    self.richText:setVisible(false)
    self.richText:setScrollWithParent(true)
    self:addChild(self.richText)
end

function SaveInfoPanel:setRichText()
	local selectedItem = self.parent.listbox.items[self.parent.listbox.selected]
	if selectedItem == self.richTextItem then return end
	self.richTextItem = selectedItem

	local text = " <H1> " .. selectedItem.item.saveName .. " <LINE> <LINE> <H2> "
	local mode = getTextOrNull("IGUI_Gametime_" .. selectedItem.item.gameMode)
	if not mode then mode = selectedItem.item.gameMode end
	text = text .. getText("IGUI_Gametime_GameMode", mode) .. " <LINE> "
	text = text .. selectedItem.item.lastPlayed .. " <LINE> "

	self.parent:checkMapsAvailable(selectedItem.item)

	local mapName = selectedItem.item.mapName
	local folders = mapName:split(";")
	text = text .. getText("UI_Map") .. " <LINE> <TEXT> <INDENT:20> "
	for _,folder in ipairs(folders) do
		if selectedItem.item.mapMissing[folder] then
			text = text .. " <RED> "
		else
			text = text .. " <TEXT> "
		end
		local info = getMapInfo(folder)
		if info then
			folder = info.title
		end
		text = text .. folder .. " <LINE> "
	end
	text = text .. " <INDENT:0> <H2> "

	text = text .. getText("UI_LoadGameScreen_Mods") .. " <LINE> <TEXT> <INDENT:20> "
	local activeMods = selectedItem.item.activeMods
	if activeMods == nil then
		text = text .. getText("UI_LoadGameScreen_NoModsTxt") .. " <LINE> "
	elseif activeMods:getMods():isEmpty() then
		text = text .. getText("UI_LoadGameScreen_NoMods") .. " <LINE> "
	else
		for i=1,activeMods:getMods():size() do
			local modID = activeMods:getMods():get(i-1)
			local modInfo = getModInfoByID(modID)
			if modInfo == nil then
				text = text .. " <RED> "
			elseif not modInfo:isAvailable() then
				text = text .. " <RED> "
				modID = modInfo:getName()
			else
				text = text .. " <TEXT> "
				modID = modInfo:getName()
			end
			text = text .. modID .. " <LINE> "
		end
	end
	text = text .. " <INDENT:0> <H2> "

	local canNotLoadText = " <LINE> <CENTER> <H1> <RED> " .. getText("UI_worldscreen_SaveCannotBeLoaded")
	local versionText = " <LINE> <LINE> " .. getText("UI_worldscreen_SavefileVersion", selectedItem.item.worldVersion or '???', IsoWorld.getWorldVersion())

	text = text .. getText("UI_WorldVersion") ..selectedItem.item.worldVersion .. " <LINE> "
	local worldVersion = tonumber(selectedItem.item.worldVersion)
	if not worldVersion or not selectedItem.item.mapName then
		text = text .. canNotLoadText .. " <TEXT> <LINE> <RED> " .. getText("UI_mainscreen_SavefileNotFound") .. " <RGB:1,1,1> <H2> <LINE> "
	elseif not selectedItem.item.mapsAvailable then
		text = text .. canNotLoadText .. " <TEXT> <LINE> <RED> " .. getText("UI_worldscreen_MapNotFound") .. " <RGB:1,1,1> <H2> <LINE> "
	elseif selectedItem.item.worldVersion == 0 then
		text = text .. canNotLoadText .. " <TEXT> <RED> " .. getText("UI_worldscreen_SavefileCorrupt") .. " <RGB:1,1,1> <H2> <LINE> "
--	elseif selectedItem.item.worldVersion == 175 then
--		text = text .. canNotLoadText .. " <TEXT> <LINE> <RED> " .. getText("UI_worldscreen_SavefileOld") .. " <LINE> " .. getText("UI_worldscreen_SavefileUseBeta41_50") .. versionText .. " <RGB:1,1,1> <H2> <LINE> "
	elseif selectedItem.item.worldVersion <= 175 then
		text = text .. canNotLoadText .. " <TEXT> <LINE> <RED> " .. getText("UI_worldscreen_SavefileOld") .. versionText .. " <RGB:1,1,1> <H2> <LINE> "
	elseif selectedItem.item.worldVersion > IsoWorld.getWorldVersion() then
		text = text .. canNotLoadText .. " <TEXT> <LINE> <RED> " .. getText("UI_worldscreen_SavefileNewerThanGame") .. versionText .. " <RGB:1,1,1> <H2> <LINE> "
	end

	self.richText.text = text
	self.richText:paginate()
end

function SaveInfoPanel:prerender()
	self:setStencilRect(0, 0, self.width, self.height)

	local selectedItem = self.parent.listbox.items[self.parent.listbox.selected]

	local thumbHeight = 256
	if selectedItem.thumb == nil and selectedItem.thumbMissing ~= true then
		-- FIXME: should unload these after choosing a savegame
		selectedItem.thumb = getTextureFromSaveDir("thumb.png", selectedItem.text)
		if selectedItem.thumb == nil then
			selectedItem.thumbMissing = true
		end
	end

	if selectedItem.thumb then
		thumbHeight = selectedItem.thumb:getHeight()
		local BreakPoint = (self.width - thumbHeight) / 2
		self:drawTexture(selectedItem.thumb, BreakPoint, 10, 1, 1, 1, 1)
	end
	local descRectWidth = self.width
	local descRectHeight = self.height

	-- ISRichTextPanel.drawMargins = true

	self.richText:setX(0)
	self.richText:setY(thumbHeight + 10 + self:getYScroll())
	self.richText:setWidth(self.width - 17)
	self.richText:setVisible(true)

	self:setRichText()

	self:setScrollHeight(thumbHeight + 10 + self.richText:getHeight())
end

function SaveInfoPanel:render()
	self:clearStencilRect()

	if self.joyfocus then
		self:drawRectBorder(0, -self:getYScroll(), self:getWidth(), self:getHeight(), 0.4, 0.2, 1.0, 1.0);
		self:drawRectBorder(1, 1-self:getYScroll(), self:getWidth()-2, self:getHeight()-2, 0.4, 0.2, 1.0, 1.0);
	else
		self:drawRectBorderStatic(0, 0, self.width, self.height, 0.3, 1, 1, 1)
	end
end

function SaveInfoPanel:onMouseWheel(del)
	self:setYScroll(self:getYScroll() - (del * 50))
	return true
end

function SaveInfoPanel:onJoypadDirUp(joypadData)
	self:setYScroll(self:getYScroll() + 48)
end

function SaveInfoPanel:onJoypadDirDown(joypadData)
	self:setYScroll(self:getYScroll() - 48)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

local ConfigPanel = ISPanelJoypad:derive("LoadGameScreen_ConfigPanel")

function ConfigPanel:createChildren()
	local x = 20
	local y = 20
	local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
	local buttonWid = 200

	local richText = self:createRichText(x, y, getText("UI_LoadGameScreen_ChooseModsText"))
	y = richText:getBottom()

	self.buttonMods = self:createButton(x, y, buttonWid, buttonHgt,
		getText("UI_LoadGameScreen_ButtonChooseMods"),
		ConfigPanel.onChooseMods)
	y = self.buttonMods:getBottom()

	-----

	richText = self:createRichText(x, y + 10, getText("UI_LoadGameScreen_ChoosePlayer1"))
	y = richText:getBottom()

	local comboHgt = math.max(buttonHgt, FONT_HGT_SMALL + 3 * 2)
	local combo = ISComboBox:new(x, y, 200, comboHgt, self, self.onPlayerSelected)
	self:addChild(combo)
	combo.disabled = true
	combo.selected = 1
	self.comboPlayer1 = combo
	y = self.comboPlayer1:getBottom()

	self.buttonNewPlayer = ISButton:new(combo:getRight() + 10, combo.y, buttonWid, comboHgt, getText("UI_LoadGameScreen_ButtonNewPlayer"), self, ConfigPanel.onNewPlayer)
	self:addChild(self.buttonNewPlayer)
	y = self.buttonNewPlayer:getBottom()

	-----

	if isDesktopOpenSupported() then
		richText = self:createRichText(x, y + 10, getText("UI_LoadGameScreen_BrowseFilesText"))
		y = richText:getBottom()

		self.buttonBrowse = self:createButton(x, y, buttonWid, buttonHgt,
			getText("UI_LoadGameScreen_ButtonBrowseFiles"),
			ConfigPanel.onBrowseFiles)
		y = self.buttonBrowse:getBottom()
	end

	-----

	if getDebug() then
		local richText = self:createRichText(x, y + 10, "DEBUG: Delete selected files from this savefile.")
		y = richText:getBottom()

		local comboHgt = math.max(buttonHgt, FONT_HGT_SMALL + 3 * 2)
		local combo = ISComboBox:new(x, y, 200, comboHgt)
		self:addChild(combo)
		combo:addOptionWithData("chunkdata_x_y.bin", "DeleteChunkDataXYBin")
		combo:addOptionWithData("map_x_y.bin", "DeleteMapXYBin")
		combo:addOptionWithData("map_meta.bin", "DeleteMapMetaBin")
		combo:addOptionWithData("map_zone.bin", "DeleteMapZoneBin")
		combo:addOptionWithData("map_t.bin", "DeleteMapTBin")
		combo:addOptionWithData("players.db", "DeletePlayersDB")
		combo:addOptionWithData("reanimated.bin", "DeleteReanimatedBin")
		combo:addOptionWithData("vehicles.db", "DeleteVehiclesDB")
		combo:addOptionWithData("z_outfits.bin", "DeleteZOutfitsBin")
		combo:addOptionWithData("zpop_virtual.bin", "DeleteZPopVirtualBin")
		combo:addOptionWithData("zpop_x_y.bin", "DeleteZPopXYBin")
		combo.selected = 1
		self.comboDeleteFiles = combo

		button = ISButton:new(combo:getRight() + 10, combo.y, buttonWid, comboHgt, "DELETE", self, ConfigPanel.onDeleteFiles)
		self:addChild(button)
		y = button:getBottom()
	end

	self:setScrollChildren(true)
	self:setScrollHeight(y + 20)

	self:insertNewLineOfButtons(self.buttonMods)
	self:insertNewLineOfButtons(self.comboPlayer1, self.buttonNewPlayer)
	self:insertNewLineOfButtons(self.buttonBrowse)
	self.joypadIndexY = 1
	self.joypadIndex = 1
	self.joypadButtons = self.joypadButtonsY[self.joypadIndexY]
end

function ConfigPanel:createRichText(x, y, richText1)
	local richText = ISRichTextPanel:new(0, y, self.width, 20)
	richText:initialise()
	richText:instantiate()
	richText:setAnchorRight(true)
	richText.background = false
	richText.marginLeft = 20
	richText:setScrollWithParent(true)
	self:addChild(richText)

	richText.text = richText1
	richText:paginate()
	return richText
end

function ConfigPanel:createButton(x, y, buttonWid, buttonHgt, buttonText, command)
	local button = ISButton:new(x, y, buttonWid, buttonHgt, buttonText, self, command)
--	button:setScrollWithParent(true)
	self:addChild(button)
	return button
end

function ConfigPanel:prerender()
	self:setStencilRect(0, 0, self.width, self.height)
	ISPanelJoypad.prerender(self)
end

function ConfigPanel:render()
	self:clearStencilRect()
	if self.joyfocus then
		self:drawRectBorder(0, -self:getYScroll(), self:getWidth(), self:getHeight(), 0.4, 0.2, 1.0, 1.0);
		self:drawRectBorder(1, 1-self:getYScroll(), self:getWidth()-2, self:getHeight()-2, 0.4, 0.2, 1.0, 1.0);
	end
	ISPanelJoypad.render(self)
end

function ConfigPanel:onChooseMods(button)
	LoadGameScreen.instance:hideConfigPanel()
	LoadGameScreen.instance:setVisible(false)
	local selected = LoadGameScreen.instance.listbox.items[LoadGameScreen.instance.listbox.selected]
	local folder = selected.text
	ModSelector.instance:setExistingSavefile(folder)
	ModSelector.instance:populateListBox(getModDirectoryTable())
	ModSelector.instance:setVisible(true, self.parent.joyfocus)
	ModSelector.showNagPanel()
end

function ConfigPanel:setSavefile(folder, info)
	local combo = self.comboPlayer1
	combo:clear()
	if not info.playerAlive then
		combo:addOptionWithData(getText("UI_LoadGameScreen_NoPlayer1"), nil)
		combo.selected = 1
	end
	combo.disabled = #info.players == 0
	for _,player in ipairs(info.players) do
		if not player.isDead then
			combo:addOptionWithData(player.name, player)
			if player.sqlID == 1 then
				combo.selected = #combo.options
			end
		end
	end
	self.saveFolder = folder
	self.saveInfo = info
end

function ConfigPanel:onPlayerSelected(combo)
	local player = combo:getOptionData(combo.selected)
	if not player then return end -- NONE
	setSavefilePlayer1(self.saveInfo.gameMode, self.saveInfo.saveName, player.sqlID)
	-- Update the list of players
	local info = getSaveInfo(self.saveFolder)
	self.saveInfo.playerAlive = info.playerAlive
	self.saveInfo.players = info.players
	self:setSavefile(self.saveFolder, self.saveInfo)
end

function ConfigPanel:onNewPlayer(combo)
	setSavefilePlayer1(self.saveInfo.gameMode, self.saveInfo.saveName, -1)
	-- Update the list of players
	local info = getSaveInfo(self.saveFolder)
	self.saveInfo.playerAlive = info.playerAlive
	self.saveInfo.players = info.players
	self:setSavefile(self.saveFolder, self.saveInfo)
	-- Create a new character
	if LoadGameScreen.instance.playButton:isEnabled() then
		LoadGameScreen.instance.playButton:forceClick()
	end
end

function ConfigPanel:onBrowseFiles(button)
	local selected = LoadGameScreen.instance.listbox.items[LoadGameScreen.instance.listbox.selected]
	local folder = getAbsoluteSaveFolderName(selected.text)
	showFolderInDesktop(folder)
end

function ConfigPanel:onDeleteFiles(button)
	local selected = LoadGameScreen.instance.listbox.items[LoadGameScreen.instance.listbox.selected]
	local folder = selected.text
	local arg = self.comboDeleteFiles:getOptionData(self.comboDeleteFiles.selected)
	manipulateSavefile(folder, arg)
end

function ConfigPanel:onMouseWheel(del)
	self:setYScroll(self:getYScroll() - (del * 50))
	return true
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function LoadGameScreen:initialise()
	ISPanel.initialise(self);
end


	--************************************************************************--
	--** ISPanel:instantiate
	--**
	--************************************************************************--
function LoadGameScreen:instantiate()

	--self:initialise();
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

function LoadGameScreen:setSaveGamesList()
	self.listbox:clear();
	local dirs = getFullSaveDirectoryTable();
	for i, k in ipairs(dirs) do
		local newSave = {};
		-- we create an item with somes info, like the last time we played that save
		local info = getSaveInfo(k)
		newSave.lastPlayed = getLastPlayedDate(k);
		newSave.worldVersion = info.worldVersion or 'unknown'
		newSave.mapName = info.mapName or 'Muldraugh, KY'
		newSave.activeMods = info.activeMods
		newSave.saveName = info.saveName;
		newSave.gameMode = info.gameMode;
		newSave.playerAlive = info.playerAlive
		newSave.players = info.players
		self.listbox:addItem(k, newSave);--{age = self:getWorldAge(k)});
	end
end

function LoadGameScreen:hasChoices()
	return #self.listbox.items > 0
end

function LoadGameScreen:create()
	local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

	self.listbox = ISScrollingListBox:new(16, self.startY, self.width / 2, self.height - 5 - buttonHgt - self.startY - 10);
	self.listbox:initialise();
	self.listbox:instantiate();
	self.listbox:setAnchorLeft(true);
--	self.listbox:setAnchorRight(true);
	self.listbox:setAnchorTop(true);
	self.listbox:setAnchorBottom(true);
	self.listbox.drawBorder = true
	self.listbox.itemheight = FONT_HGT_SMALL + FONT_HGT_LARGE + 10;
	self.listbox.doDrawItem = LoadGameScreen.drawMap;
	self.listbox:setOnMouseDoubleClick(self, LoadGameScreen.onDblClickWorld);
    self.listbox:setOnMouseDownFunction(self, LoadGameScreen.onClickWorld);
	self.listbox.onGainJoypadFocus = self.onGainJoypadFocus_child
	self.listbox.onLoseJoypadFocus = self.onLoseJoypadFocus_child
	self.listbox.onJoypadBeforeDeactivate = self.onJoypadBeforeDeactivate_child
	self.listbox.onJoypadDirRight = self.onJoypadDirRight_child
	self:addChild(self.listbox);

	self.configPanel = ConfigPanel:new(self.listbox.x, self.listbox.y, self.listbox.width, self.listbox.height)
	self.configPanel:initialise()
	self.configPanel:instantiate()
	self.configPanel:setAnchorLeft(true)
	self.configPanel:setAnchorTop(true)
	self.configPanel:setAnchorBottom(true)
	self.configPanel:addScrollBars()
	self.configPanel.onGainJoypadFocus = self.onGainJoypadFocus_child
	self.configPanel.onLoseJoypadFocus = self.onLoseJoypadFocus_child
	self.configPanel.onJoypadDirRight = self.onJoypadDirRight_child
	self.configPanel.onJoypadBeforeDeactivate = self.onJoypadBeforeDeactivate_child
	self:addChild(self.configPanel)
	self.configPanel:setVisible(false)

	local x = self.listbox:getRight() + 16
	self.infoPanel = SaveInfoPanel:new(x, self.startY, self.width - 16 - x, self.listbox.height)
	self.infoPanel.onGainJoypadFocus = self.onGainJoypadFocus_child
	self.infoPanel.onJoypadDirLeft = self.onJoypadDirLeft_child
	self.infoPanel:initialise()
	self.infoPanel:instantiate()
	self.infoPanel:setAnchorLeft(false)
	self.infoPanel:setAnchorRight(false)
	self.infoPanel:setAnchorTop(true)
	self.infoPanel:setAnchorBottom(true)
	self.infoPanel:addScrollBars()
	self:addChild(self.infoPanel)

	self.backButton = ISButton:new(16, self.height - buttonHgt - 5, 100, buttonHgt, getText("UI_btn_back"), self, LoadGameScreen.onOptionMouseDown);
	self.backButton.internal = "BACK";
	self.backButton:initialise();
	self.backButton:instantiate();
	self.backButton:setAnchorLeft(true);
	self.backButton:setAnchorTop(false);
	self.backButton:setAnchorBottom(true);
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1};
	self.backButton:setFont(UIFont.Small);
	self.backButton:ignoreWidthChange();
	self.backButton:ignoreHeightChange();
	self:addChild(self.backButton);

	self.playButton = ISButton:new(self.width - 116, self.height - buttonHgt - 5, 100, buttonHgt, getText("UI_btn_play"), self, LoadGameScreen.onOptionMouseDown);
	self.playButton.internal = "PLAY";
	self.playButton:initialise();
	self.playButton:instantiate();
	self.playButton:setAnchorLeft(false);
	self.playButton:setAnchorRight(true);
	self.playButton:setAnchorTop(false);
	self.playButton:setAnchorBottom(true);
	self.playButton.borderColor = {r=1, g=1, b=1, a=0.1};
	self.playButton:setSound("activate", nil)
	self:addChild(self.playButton);

	self.configButton = ISButton:new(self.playButton.x - 110, self.height - buttonHgt - 5, 100, buttonHgt, getText("UI_LoadGameScreen_ButtonConfig"), self, LoadGameScreen.onOptionMouseDown);
	self.configButton.internal = "CONFIG";
	self.configButton:initialise();
	self.configButton:instantiate();
	self.configButton:setAnchorLeft(false);
	self.configButton:setAnchorRight(true);
	self.configButton:setAnchorTop(false);
	self.configButton:setAnchorBottom(true);
	self.configButton.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.configButton);

	self.deleteButton = ISButton:new(self.configButton.x - 110, self.height - buttonHgt - 5, 100, buttonHgt, getText("UI_btn_delete"), self, LoadGameScreen.onOptionMouseDown);
	self.deleteButton.internal = "DELETE";
	self.deleteButton:initialise();
	self.deleteButton:instantiate();
	self.deleteButton:setAnchorLeft(false);
	self.deleteButton:setAnchorRight(true);
	self.deleteButton:setAnchorTop(false);
	self.deleteButton:setAnchorBottom(true);
	self.deleteButton.borderColor = {r=1, g=1, b=1, a=0.1};
	self.deleteButton:setFont(UIFont.Small);
	self.deleteButton:ignoreWidthChange();
	self.deleteButton:ignoreHeightChange();
	self:addChild(self.deleteButton);

	self.playButton:setX(self.width - 16 - self.playButton.width)
	self.configButton:setX(self.playButton.x - 10 - self.configButton.width)
	self.deleteButton:setX(self.configButton.x - 10 - self.deleteButton.width)

    self.richText = ISRichTextPanel:new(16, 10, 500,200);
    self.richText:initialise();
    self.richText.background = false;
    self.richText:setAnchorBottom(true);
    self.richText:setAnchorRight(true);
    self.richText:setVisible(false);
    self:addChild(self.richText);

	self:setVisible(false);
end

function LoadGameScreen:drawMap(y, item, alt)
	local isMouseOver = self.mouseoverselected == item.index and not self:isMouseOverScrollBar()
	if self.selected == item.index then
		self:drawRect(0, (y), self:getWidth(), self.itemheight-1, 0.3, 0.7, 0.35, 0.15);
	elseif isMouseOver then
		self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.95, 0.05, 0.05, 0.05);
    end

    local mode = getTextOrNull("IGUI_Gametime_" .. item.item.gameMode)
    if not mode then mode = item.item.gameMode end
    self:drawText(mode, 20, (y)+5, 0.9, 0.9, 0.9, 0.9, UIFont.NewSmall);
	self:drawText(item.item.saveName, 20, (y)+5+FONT_HGT_SMALL, 0.9, 0.9, 0.9, 0.9, UIFont.NewLarge);
    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight-1, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b);

	if not item.item.playerAlive and self.parent.deadTexture then
		local tex = self.parent.deadTexture
		local dy = (self.itemheight - tex:getHeightOrig()) / 2
		self:drawTexture(tex, self:getWidth() - 24 - tex:getWidthOrig(), y + dy, 1, 1, 1, 1)
	end

	self.itemheightoverride[item.text] = self.itemheight;

	y = y + self.itemheightoverride[item.text];

	return y;
end

function LoadGameScreen:render()
	self.deleteButton:setVisible(false);
    self.playButton:setVisible(false);
    if self.listbox.items[self.listbox.selected] then
        self.playButton:setVisible(true);
        self.configButton:setVisible(true);
        self.deleteButton:setVisible(true);
    end
end

function LoadGameScreen:prerender()
	LoadGameScreen.instance = self
	ISPanel.prerender(self);

	self:drawTextCentre(getText("UI_LoadGameScreen_title"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title);

	self:disableBtn()
end

LoadGameScreen.onClickWorld = function()
    if MainScreen.instance.loadScreen.modal then
        MainScreen.instance.loadScreen.modal:setVisible(false);
        MainScreen.instance.loadScreen.modal:removeFromUIManager();
    end
end

function LoadGameScreen:showConfigPanel()
	self.listbox:setVisible(false)
	local item = self.listbox.items[self.listbox.selected]
	self.configPanel:setSavefile(item.text, item.item)
	self.configPanel:setVisible(true, self.joyfocus)
	self.configButton:setTitle(getText("UI_LoadGameScreen_ButtonFiles"))
end

function LoadGameScreen:hideConfigPanel()
	self.listbox:setVisible(true, self.joyfocus)
	self.configPanel:setVisible(false)
	self.configButton:setTitle(getText("UI_LoadGameScreen_ButtonConfig"))
end

function LoadGameScreen:onOptionMouseDown(button, x, y)
	if self.modal then
		self.modal:setVisible(false);
		self.modal = nil;
	end
	if button.internal == "BACK" then
		self:hideConfigPanel()
		self:setVisible(false);
		MainScreen.instance.bottomPanel:setVisible(true);
		if self.joyfocus then
			self.joyfocus.focus = MainScreen.instance;
			updateJoypadFocus(self.joyfocus);
		end
	end
	if button.internal == "PLAY" then
		self:hideConfigPanel()
		self:clickPlay();
	end
	if button.internal == "DELETE" then
		self.modal = ISModalDialog:new((getCore():getScreenWidth() / 2) - 130, (getCore():getScreenHeight() / 2) - 60, 260, 120, getText("UI_worldscreen_deletesave"), true, self, LoadGameScreen.onDeleteModalClick);
		self.modal:initialise();
		self.modal:addToUIManager();
		if self.joyfocus then
			self.joyfocus.focus = self.modal;
			updateJoypadFocus(self.joyfocus);
		end
	end
	if button.internal == "CONFIG" then
		if self.listbox:isVisible() then
			self:showConfigPanel()
		else
			self:hideConfigPanel()
		end
	end
end

function LoadGameScreen:onDblClickWorld()
	self.playButton:forceClick()
end

function LoadGameScreen:clickPlay()
	local sel = self.listbox.items[self.listbox.selected];

	if not sel then
		return
	end

	self:setVisible(false);

	MainScreen.instance.bottomPanel:setVisible(true);

	if self.joyfocus then
		self.joyfocus.focus = MainScreen.instance;
		updateJoypadFocus(self.joyfocus);
	end

	local gameMode = sel.item.gameMode;
	local saveName = sel.item.saveName;

	MainScreen.continueLatestSave(gameMode, saveName);
end

function LoadGameScreen:onDeleteModalClick(button)
	if LoadGameScreen.instance.joyfocus then
		LoadGameScreen.instance.joyfocus.focus = LoadGameScreen.instance.listbox
		updateJoypadFocus(LoadGameScreen.instance.joyfocus)
	end
	if button.internal == "YES" then
		local sel = LoadGameScreen.instance.listbox.items[LoadGameScreen.instance.listbox.selected];
		LoadGameScreen.instance.listbox:removeItemByIndex(LoadGameScreen.instance.listbox.selected)
		deleteSave(sel.text);
        LoadGameScreen.instance.listbox.joypadListIndex = 1;
        LoadGameScreen.instance.listbox.selected = 1;
		if #LoadGameScreen.instance.listbox.items == 0 then
			LoadGameScreen.instance:setVisible(false);
			MainScreen.instance.loadOption:setVisible(false)
			MainScreen.instance.latestSaveOption:setVisible(false)
			MainScreen.instance.soloScreen:setVisible(true, self.joyfocus)
			MainScreen.instance.soloScreen.onMenuItemMouseDown(MainScreen.instance.soloScreen.survival,0,0);
        else
            LoadGameScreen.instance:setVisible(true, self.joyfocus);
        end
    end
end

function LoadGameScreen:onErrorLoadingClick(button)
	if LoadGameScreen.instance.joyfocus then
		LoadGameScreen.instance.joyfocus.focus = LoadGameScreen.instance.listbox
		updateJoypadFocus(LoadGameScreen.instance.joyfocus)
	end
end

function LoadGameScreen:onGainJoypadFocus(joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData);
    if self.listbox:isVisible() then
		joypadData.focus = self.listbox;
		self.listbox.joypadFocused = true;
	else
		joypadData.focus = self.configPanel;
    end
    updateJoypadFocus(joypadData);
end

function LoadGameScreen:onJoypadBeforeDeactivate(joypadData)
	self.playButton:clearJoypadButton()
	self.backButton:clearJoypadButton()
	self.configButton:clearJoypadButton()
	self.deleteButton:clearJoypadButton()
	self.listbox.joypadFocused = false
	self.joyfocus = nil
end

function LoadGameScreen:onGainJoypadFocus_child(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData)
	self:setISButtonForA(self.parent.playButton)
	self:setISButtonForB(self.parent.backButton)
	self:setISButtonForX(self.parent.configButton)
	self:setISButtonForY(self.parent.deleteButton)
	if self == self.parent.listbox then
		self.joypadFocused = true
	end
	if #self.joypadButtons >= 1 and self.joypadIndex <= #self.joypadButtons then
		self.joypadButtons[self.joypadIndex]:setJoypadFocused(true, joypadData)
	end
end

function LoadGameScreen:onLoseJoypadFocus_child(joypadData)
	ISPanelJoypad.onLoseJoypadFocus(self, joypadData)
	self:clearJoypadFocus()
	if self == self.parent.listbox then
		self.joypadFocused = false
	end
end

function LoadGameScreen:onJoypadBeforeDeactivate_child(joypadData)
	self.parent:onJoypadBeforeDeactivate(joypadData)
end

function LoadGameScreen:onJoypadDirLeft_child(joypadData)
	joypadData.focus = self.parent.listbox:isVisible() and self.parent.listbox or self.parent.configPanel
	updateJoypadFocus(joypadData)
end

function LoadGameScreen:onJoypadDirRight_child(joypadData)
	local children = self:getVisibleChildren(self.joypadIndexY)
	if #children > 0 and self.joypadIndex ~= #children then
		ISPanelJoypad.onJoypadDirRight(self)
		return
	end
	self.parent.listbox.joypadFocused = false
	joypadData.focus = self.parent.infoPanel
	updateJoypadFocus(joypadData)
end

function LoadGameScreen:getChallenge(item)
	for i,challenge in ipairs(LastStandChallenge) do
		if challenge.gameMode == item.gameMode then
			return challenge
		end
	end
	return nil
end

function LoadGameScreen:checkChallenge(item)
	local challenge = self:getChallenge(item)
	if challenge then
		if challenge.world ~= item.mapName then
			-- The map is available but is the wrong one for this challenge.
		end
	end
end

function LoadGameScreen:checkMapsAvailable(item)
	if item.mapsAvailable ~= nil then
		return item.mapsAvailable
	end

	item.mapsAvailable = true
	item.mapMissing = {}

	local activeMods = item.activeMods or ActiveMods.getById("default")
	self.mapGroups:createGroups(activeMods, true, true)
	local lotDirs = self.mapGroups:getAllMapsInOrder()

	local mapNames = luautils.split(item.mapName, ";")
	for i=1,#mapNames do
		if not lotDirs:contains(mapNames[i]) then
			item.mapsAvailable = false
			item.mapMissing[mapNames[i]] = true
		end
	end

	return item.mapsAvailable
end

function LoadGameScreen:disableBtn()
	local sel = self.listbox.items[self.listbox.selected]
	if sel and sel.item then
		local worldVersion = tonumber(sel.item.worldVersion)
		if not worldVersion or not sel.item.mapName then
			self.playButton:setEnable(false)
		elseif worldVersion > IsoWorld.getWorldVersion() then
			self.playButton:setEnable(false)
		elseif worldVersion <= 175 then
			self.playButton:setEnable(false)
		elseif not self:checkMapsAvailable(sel.item) then
			self.playButton:setEnable(false)
		else
			self.playButton:setEnable(true)
		end
	else
		self.playButton:setEnable(false)
		self.configButton:setEnable(false)
		self.deleteButton:setEnable(false)
	end
end

function LoadGameScreen:onSavefileModsChanged(folder)
	for _,item in ipairs(self.listbox.items) do
		if item.text == folder then
			local info = getSaveInfo(folder)
			item.item.activeMods = info.activeMods
			item.item.mapsAvailable = nil
			self.infoPanel.richTextItem = nil
			break
		end
	end
end

function LoadGameScreen:onResolutionChange(oldw, oldh, neww, newh)
	self.listbox:setWidth(self.width / 2)
	self.configPanel:setWidth(self.width / 2)
	local x = self.listbox:getRight() + 16
	self.infoPanel:setX(x)
	self.infoPanel:setWidth(self.width - 16 - x)
end

function LoadGameScreen:new(x, y, width, height)
	local o = {}
	--o.data = {}
	o = ISPanelJoypad:new(x, y, width, height);
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
	o.selected = 1;
    o.startY = 10 + FONT_HGT_TITLE + 10;
    o.deadTexture = getTexture("media/ui/Moodles/Moodle_Icon_Dead.png")
	o.mapGroups = MapGroups.new()
	LoadGameScreen.instance = o;
	return o
end

function LoadGameScreen.OnKeyPressed(key)
	if LoadGameScreen.instance and LoadGameScreen.instance:isVisible() then
		local listbox = LoadGameScreen.instance.listbox

		if not listbox:isReallyVisible() then return end

		if (key == Keyboard.KEY_DELETE) then
			LoadGameScreen.instance.deleteButton:forceClick()
		end

		if (key == Keyboard.KEY_UP) then
			listbox.selected = listbox.selected - 1
			if listbox.selected <= 0 then
				listbox.selected = listbox:size()
			end
			listbox:ensureVisible(listbox.selected)
		end

		if key == Keyboard.KEY_DOWN then
			listbox.selected = listbox.selected + 1
			if listbox.selected > listbox:size() then
				listbox.selected = 1
			end
			listbox:ensureVisible(listbox.selected)
		end
	end
end

function LoadGameScreen_onModsModified()
	local self = LoadGameScreen.instance
	if self and self.listbox and self:isReallyVisible() then
		self.infoPanel.richTextItem = nil
		for _,item in ipairs(self.listbox.items) do
			item.item.mapsAvailable = nil
		end
	end
end

Events.OnModsModified.Add(LoadGameScreen_onModsModified)

Events.OnKeyPressed.Add(LoadGameScreen.OnKeyPressed)
