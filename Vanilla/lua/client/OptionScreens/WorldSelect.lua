--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISScrollingListBox"

WorldSelect = ISPanelJoypad:derive("WorldSelect")

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

function WorldSelect:initialise()
	ISPanelJoypad.initialise(self)
end

function WorldSelect:hasChoices()
	if isClient() then return false end
	self.mapGroups:createGroups()
	return self.mapGroups:getNumberOfGroups() > 1
end

function WorldSelect:fillList()
	self.listbox:clear()
	for i=1,self.mapGroups:getNumberOfGroups() do
		local item = {}
		-- TODO: if only vanilla maps, set item.name = "Kentucky, USA"
		item.name = getText("UI_WorldSelect_WorldN", i)
		item.world = i
		item.mapDirs = self.mapGroups:getMapDirectoriesInGroup(i-1)
		if item.mapDirs:size() == 1 then
			local mapDir = item.mapDirs:get(0)
			local info = getMapInfo(mapDir)
			if info and info.title then
				item.name = info.title
			else
				item.name = mapDir
			end
		end
		self.listbox:addItem(item.name, item)
	end
	self:onSelectWorld()
end

function WorldSelect:onSelectWorld()
	self.mapListbox:clear()
	local mapDirs = self.listbox.items[self.listbox.selected].item.mapDirs
	for i=1,mapDirs:size() do
		local mapDir = mapDirs:get(i-1)
		local item = {}
		local info = getMapInfo(mapDir)
		if info and info.title then
			item.name = info.title
		else
			item.name = mapDir
		end
		self.mapListbox:addItem(item.name, item)
	end
	self.mapListbox:sort()
end

function WorldSelect:onOptionMouseDown(button, x, y)
	self:setVisible(false)
	if button.internal == "BACK" then
		self:clickBack()
	elseif button.internal == "NEXT" then
		self:clickNext()
	end
end

function WorldSelect:onDblClick()
	self:clickNext()
end

function WorldSelect:clickBack()
	if getWorld():getGameMode() == "Multiplayer" then
		backToSinglePlayer()
		getCore():ResetLua("default", "exitJoinServer")
	elseif self.previousScreen == "LoadGameScreen" then
		MainScreen.resetLuaIfNeeded()
		self.previousScreen = nil
		LoadGameScreen.instance:setSaveGamesList()
		MainScreen.instance.loadScreen:setVisible(true, self.joyfocus)
	elseif self.previousScreen == "NewGameScreen" then
		self.previousScreen = nil
		MainScreen.instance.soloScreen:setVisible(true, self.joyfocus)
	end
end

function WorldSelect:clickNext()
	self:setVisible(false)
	self.mapGroups:setWorld(self.listbox.selected-1)
	if MainScreen.instance.createWorld or MapSpawnSelect.instance:hasChoices() then
		MapSpawnSelect.instance:fillList()
		MapSpawnSelect.instance.previousScreen = "WorldSelect"
		MapSpawnSelect.instance:setVisible(true, self.joyfocus)
	elseif getWorld():getGameMode() == "Sandbox" then
		MapSpawnSelect.instance:useDefaultSpawnRegion()
		MainScreen.instance.sandOptions:setVisible(true, self.joyfocus)
	else
		MainScreen.instance.charCreationProfession.previousScreen = "WorldSelect"
		MainScreen.instance.charCreationProfession:setVisible(true, self.joyfocus)
	end
end

function WorldSelect:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_WorldSelect_title"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	self:drawRectBorder(self.listbox:getX(), self.listbox:getY(), self.listbox:getWidth(), self.listbox:getHeight(), 0.9, 0.4, 0.4, 0.4)
	self:drawRectBorder(self.mapListbox:getX(), self.mapListbox:getY(), self.mapListbox:getWidth(), self.mapListbox:getHeight(), 0.9, 0.4, 0.4, 0.4)
end

function WorldSelect:doDrawItem(y, item, alt)
	if self.itemsSelectable then
		local isMouseOver = self.mouseoverselected == item.index and not self:isMouseOverScrollBar()
		if self.selected == item.index then
			self:drawRect(0, (y), self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
		elseif isMouseOver then
			self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.95, 0.05, 0.05, 0.05)
		end
	end
	self:drawRectBorder(0, (y), self:getWidth(), item.height, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b)
	local fontHgt = getTextManager():getFontFromEnum(UIFont.Large):getLineHeight()
	local textY = y + (item.height - fontHgt) / 2
	self:drawText(item.text, 15, textY, 0.9, 0.9, 0.9, 0.9, UIFont.Large)
	y = y + item.height
	return y
end

function WorldSelect:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData)
	joypadData.focus = self.listbox
	updateJoypadFocus(joypadData)
	self.listbox:setISButtonForA(self.nextButton)
	self.listbox:setISButtonForB(self.backButton)
end

function WorldSelect:onResolutionChange(oldw, oldh, neww, newh)
	local padX = 16
	self.listbox:setWidth((self.width - padX * 3) / 2)
	self.mapListbox:setX(self.listbox:getRight() + padX)
	self.mapListbox:setWidth(self.width - padX * 3 - self.listbox:getWidth())
end

function WorldSelect:create()
	local padX = 16
	local btnWid = 100
	local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
	local btnPadY = 5
	local startY = 80

	self.listbox = ISScrollingListBox:new(padX, startY, (self.width - padX * 3) / 2, self.height-30-30-startY)
	self.listbox:initialise()
	self.listbox:instantiate()
	self.listbox:setAnchorLeft(true)
	self.listbox:setAnchorRight(false)
	self.listbox:setAnchorTop(true)
	self.listbox:setAnchorBottom(true)
	self.listbox.itemheight = 50
	self.listbox.doDrawItem = WorldSelect.doDrawItem
	self.listbox.itemsSelectable = true
	self.listbox:setOnMouseDownFunction(self, WorldSelect.onSelectWorld)
	self.listbox:setOnMouseDoubleClick(self, WorldSelect.onDblClick)
	self.listbox.backgroundColor  = {r=0, g=0, b=0, a=0.5}
	self:addChild(self.listbox)

	self.mapListbox = ISScrollingListBox:new(self.listbox:getRight() + padX, startY, self.width - padX - self.listbox:getRight() - padX, self.height-30-30-startY)
	self.mapListbox:initialise()
	self.mapListbox:instantiate()
	self.mapListbox:setAnchorLeft(true)
	self.mapListbox:setAnchorRight(false)
	self.mapListbox:setAnchorTop(true)
	self.mapListbox:setAnchorBottom(true)
	self.mapListbox.itemheight = 50
	self.mapListbox.doDrawItem = WorldSelect.doDrawItem
	self.mapListbox.itemsSelectable = false
	self.mapListbox.backgroundColor  = {r=0, g=0, b=0, a=0.5}
	self:addChild(self.mapListbox)

	self.backButton = ISButton:new(padX, self.height - btnPadY - btnHgt, btnWid, btnHgt, getText("UI_btn_back"), self, WorldSelect.onOptionMouseDown)
	self.backButton.internal = "BACK"
	self.backButton:initialise()
	self.backButton:instantiate()
	self.backButton:setAnchorLeft(true)
	self.backButton:setAnchorTop(false)
	self.backButton:setAnchorBottom(true)
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.backButton)

	self.nextButton = ISButton:new(self.width - 116, self.height - btnPadY - btnHgt, btnWid, btnHgt, getText("UI_btn_next"), self, WorldSelect.onOptionMouseDown)
	self.nextButton.internal = "NEXT"
	self.nextButton:initialise()
	self.nextButton:instantiate()
	self.nextButton:setAnchorLeft(false)
	self.nextButton:setAnchorRight(true)
	self.nextButton:setAnchorTop(false)
	self.nextButton:setAnchorBottom(true)
	self.nextButton:setEnable(true) -- sets the hard-coded border color
	self:addChild(self.nextButton)
end

function WorldSelect:new(x, y, width, height)
	local o = ISPanelJoypad.new(self, x, y, width, height)
	o.previousScreen = 'NewGameScreen'
	o.mapGroups = MapGroups.new()
	WorldSelect.instance = o
	return o
end

