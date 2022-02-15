--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanelJoypad"
require "ISUI/Maps/ISMap"
require "ISUI/Maps/ISWorldMapSymbols"

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_HANDWRITTEN = getTextManager():getFontHeight(UIFont.Handwritten)

-----

WorldMapOptions = ISCollapsableWindow:derive("WorldMapOptions")

function WorldMapOptions:onTickBox(index, selected, option)
	option:setValue(selected)
end

function WorldMapOptions:onCommandEntered(entry, option)
	option:parse(entry:getText())
end

function WorldMapOptions:createChildren()
	local fontHgt = FONT_HGT_SMALL
	local entryHgt = fontHgt + 2 * 2

	local x = 12
	local y = self:titleBarHeight() + 6
	local maxWidth = 0

	self.doubleBoxes = {}
	self.tickBoxes = {}

	for i=1,self.map.mapAPI:getOptionCount() do
		local option = self.map.mapAPI:getOptionByIndex(i-1)
		if option:getType() == "boolean" then
			local tickBox = ISTickBox:new(x, y, self.width, entryHgt, "", self, self.onTickBox, option)
			tickBox:initialise()
			tickBox:addOption(option:getName(), option)
			tickBox:setSelected(1, option:getValue())
			tickBox:setWidthToFit()
			self:addChild(tickBox)
			maxWidth = math.max(maxWidth, tickBox:getRight())
			table.insert(self.tickBoxes, i, tickBox)
			y = y + entryHgt + 6
		end
		if option:getType() == "double" then
			local label = ISLabel:new(x, y, entryHgt, option:getName(), 1, 1, 1, 1, UIFont.Small, true)
			self:addChild(label)
			local entry = ISTextEntryBox:new("", label:getRight()+4, y, 100, entryHgt)
			entry.onCommandEntered = function(self) self.parent:onCommandEntered(entry, option) end
			self:addChild(entry)
			entry:setOnlyNumbers(true)
			maxWidth = math.max(maxWidth, entry:getRight())
			table.insert(self.doubleBoxes, i, entry)
			y = y + entryHgt + 6
		end
		if self.y + y + entryHgt + 6 >= getCore():getScreenHeight() then
			x = x + maxWidth
			y = self:titleBarHeight() + 6
			maxWidth = 0
		end
	end

	local width = 0
	local height = 0
	for _,child in pairs(self:getChildren()) do
		width = math.max(width, child:getRight())
		height = math.max(height, child:getBottom())
	end
	self:setWidth(width + 12)
	self:setHeight(height + self:resizeWidgetHeight())
end

function WorldMapOptions:synchUI()
	for i=1,self.map.mapAPI:getOptionCount() do
		local option = self.map.mapAPI:getOptionByIndex(i-1)
		if option:getType() == "boolean" then
			self.tickBoxes[i]:setSelected(1, option:getValue())
		end
		if option:getType() == "double" then
			self.doubleBoxes[i]:setText(option:getValueAsString())
		end
	end
end

function WorldMapOptions:onMouseDownOutside(x, y)
	if self:isMouseOver() then
		return -- click in ISTextEntryBox
	end
	if self.parent.optionBtn:isMouseOver() then
		return
	end
	self:setVisible(false)
end

function WorldMapOptions:new(x, y, width, height, map)
	local o = ISCollapsableWindow.new(self, x, y, width, height)
	o.backgroundColor = {r=0, g=0, b=0, a=1.0}
	o.resizable = false
	o.map = map
	return o
end

-----

ISWorldMapButtonPanel = ISPanelJoypad:derive("ISWorldMapButtonPanel")

function ISWorldMapButtonPanel:render()
	ISPanelJoypad.render(self)
	if self.joyfocus then
		local children = self:getVisibleChildren(self.joypadIndexY)
		local child = children[self.joypadIndex]
		if child then
			self:drawRectBorder(child.x, child.y, child.width, child.height, 0.4, 0.2, 1.0, 1.0);
			self:drawRectBorder(child.x-1, child.y-1, child.width+2, child.height+2, 0.4, 0.2, 1.0, 1.0);
		end
	end
end

function ISWorldMapButtonPanel:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData)
	self:restoreJoypadFocus()
end

function ISWorldMapButtonPanel:onLoseJoypadFocus(joypadData)
	ISPanelJoypad.onLoseJoypadFocus(self, joypadData)
	self:clearJoypadFocus()
end

function ISWorldMapButtonPanel:onJoypadDown(button, joypadData)
	if button == Joypad.BButton or button == Joypad.YButton then
		setJoypadFocus(joypadData.player, self.parent)
		return
	end
	ISPanelJoypad.onJoypadDown(self, button, joypadData)
end

function ISWorldMapButtonPanel:new(x, y, width, height)
	local o = ISPanelJoypad.new(self, x, y, width, height)
	o:noBackground()
	return o
end

-----

ISWorldMap = ISPanelJoypad:derive("ISWorldMap")

function ISWorldMap:instantiate()
	self.javaObject = UIWorldMap.new(self)
	self.mapAPI = self.javaObject:getAPIv1()
	self.mapAPI:setMapItem(MapItem.getSingleton())
	self.javaObject:setX(self.x)
	self.javaObject:setY(self.y)
	self.javaObject:setWidth(self.width)
	self.javaObject:setHeight(self.height)
	self.javaObject:setAnchorLeft(self.anchorLeft)
	self.javaObject:setAnchorRight(self.anchorRight)
	self.javaObject:setAnchorTop(self.anchorTop)
	self.javaObject:setAnchorBottom(self.anchorBottom)
	self.javaObject:setWantKeyEvents(true)
	self:createChildren()
end

function ISWorldMap:createChildren()
	self.symbolsUI = ISWorldMapSymbols:new(self.width - 20 - 200, 20, 200, self.height - 40 * 2, self)
	self.symbolsUI:initialise()
	self.symbolsUI:setAnchorLeft(false)
	self.symbolsUI:setAnchorRight(true)
	self:addChild(self.symbolsUI)

	local btnSize = self.texViewIsometric and self.texViewIsometric:getWidth() or 48
	local btnCount = 6

	self.buttonPanel = ISWorldMapButtonPanel:new(self.width - 20 - (btnSize * btnCount + 20 * (btnCount - 1)), self.height - 20 - btnSize, btnSize * btnCount + 20 * (btnCount - 1), btnSize)
	self.buttonPanel.anchorLeft = false
	self.buttonPanel.anchorRight = true
	self.buttonPanel.anchorTop = false
	self.buttonPanel.anchorBottom = true
	self:addChild(self.buttonPanel)

	self.closeBtn = ISButton:new(self.buttonPanel.width - btnSize, 0, btnSize, btnSize, "X", self, self.close)
	self.buttonPanel:addChild(self.closeBtn)

	self.symbolsBtn = ISButton:new(self.closeBtn.x - 20 - btnSize, 0, btnSize, btnSize, "S", self, self.onToggleSymbols)
	self.buttonPanel:addChild(self.symbolsBtn)

	self.centerBtn = ISButton:new(self.symbolsBtn.x - 20 - btnSize, 0, btnSize, btnSize, "C", self, self.onCenterOnPlayer)
	self.buttonPanel:addChild(self.centerBtn)

	self.perspectiveBtn = ISButton:new(self.centerBtn.x - 20 - btnSize, 0, btnSize, btnSize, "", self, self.onChangePerspective)
	self.perspectiveBtn:setImage(self.isometric and self.texViewIsometric or self.texViewOrthographic)
	self.buttonPanel:addChild(self.perspectiveBtn)

	self.pyramidBtn = ISButton:new(self.perspectiveBtn.x - 20 - btnSize, 0, btnSize, btnSize, "", self, self.onTogglePyramid)
	self.pyramidBtn:setImage(self.texViewPyramid)
	self.buttonPanel:addChild(self.pyramidBtn)
	self.pyramidBtn:setVisible(getDebug())

	if getDebug() or (isClient() and (getAccessLevel() == "admin")) then
		self.optionBtn = ISButton:new(self.pyramidBtn.x - 20 - btnSize, 0, btnSize, btnSize, "OPT", self, self.onChangeOptions)
		self.buttonPanel:addChild(self.optionBtn)
	end

	self.buttonPanel:insertNewLineOfButtons(self.pyramidBtn, self.perspectiveBtn, self.centerBtn, self.symbolsBtn, self.closeBtn)
	self.buttonPanel.joypadIndex = 1
	self.buttonPanel.joypadIndexY = 1
end

function ISWorldMap:render()
	getWorld():setDrawWorld(false)

	local INSET = 0
	local w = getCore():getScreenWidth() - INSET * 2
	local h = getCore():getScreenHeight() - INSET * 2
	if self.width ~= w or self.height ~= h then
		self:setWidth(w)
		self:setHeight(h)
	end

	self.isometric = self.mapAPI:getBoolean("Isometric")
	self.perspectiveBtn:setImage(self.isometric and self.texViewIsometric or self.texViewOrthographic)

	self:updateJoypad()

	if self.playerNum and ((self.playerNum ~= 0) or (JoypadState.players[self.playerNum+1] ~= nil and not wasMouseActiveMoreRecentlyThanJoypad())) then
		self:drawTexture(self.cross, self.width/2-12, self.height/2-12, 1, 1,1,1);
	end

	ISPanelJoypad.render(self)
end

function ISWorldMap:onMouseDown(x, y)
	if self.symbolsUI:onMouseDownMap(x, y) then
		return true
	end
	self.dragging = true
	self.dragMoved = false
	self.dragStartX = x
	self.dragStartY = y
	self.dragStartCX = self.mapAPI:getCenterWorldX()
	self.dragStartCY = self.mapAPI:getCenterWorldY()
	self.dragStartZoomF = self.mapAPI:getZoomF()
	self.dragStartWorldX = self.mapAPI:uiToWorldX(x, y)
	self.dragStartWorldY = self.mapAPI:uiToWorldY(x, y)
	return true
end

function ISWorldMap:onMouseMove(dx, dy)
	if self.symbolsUI:onMouseMoveMap(dx, dy) then
		return true
	end
	if self.dragging then
		local mouseX = self:getMouseX()
		local mouseY = self:getMouseY()
		if not self.dragMoved and math.abs(mouseX - self.dragStartX) <= 4 and math.abs(mouseY - self.dragStartY) <= 4 then
			return
		end
		self.dragMoved = true
		local worldX = self.mapAPI:uiToWorldX(mouseX, mouseY, self.dragStartZoomF, self.dragStartCX, self.dragStartCY)
		local worldY = self.mapAPI:uiToWorldY(mouseX, mouseY, self.dragStartZoomF, self.dragStartCX, self.dragStartCY)
		self.mapAPI:centerOn(self.dragStartCX + self.dragStartWorldX - worldX, self.dragStartCY + self.dragStartWorldY - worldY)
	end
	return true
end

function ISWorldMap:onMouseMoveOutside(dx, dy)
	return self:onMouseMove(dx, dy)
end

function ISWorldMap:onMouseUp(x, y)
	self.dragging = false
	if self.symbolsUI:onMouseUpMap(x, y) then
		return true
	end
	return true
end

function ISWorldMap:onMouseUpOutside(x, y)
	self.dragging = false
	if self.symbolsUI:onMouseUpMap(x, y) then
		return true
	end
	return true
end

function ISWorldMap:onMouseWheel(del)
	self.mapAPI:zoomAt(self:getMouseX(), self:getMouseY(), del)
	return true
end

function ISWorldMap:onRightMouseDown(x, y)
	if self.symbolsUI:onRightMouseDownMap(x, y) then
		return true
	end
	return false
end

function ISWorldMap:onRightMouseUp(x, y)
	if self.symbolsUI:onRightMouseUpMap(x, y) then
		return true
	end
	if not getDebug() and not (isClient() and (getAccessLevel() == "admin")) then
		return false
	end
	local playerNum = 0
	local playerObj = getSpecificPlayer(0)
	if not playerObj then return end -- Debug in main menu
	local context = ISContextMenu.get(playerNum, x + self:getAbsoluteX(), y + self:getAbsoluteY())

	local option = context:addOption("Show Cell Grid", self, function(self) self:setShowCellGrid(not self.showCellGrid) end)
	context:setOptionChecked(option, self.showCellGrid)

	option = context:addOption("Show Tile Grid", self, function(self) self:setShowTileGrid(not self.showTileGrid) end)
	context:setOptionChecked(option, self.showTileGrid)

	self.hideUnvisitedAreas = self.mapAPI:getBoolean("HideUnvisited")
	option = context:addOption("Hide Unvisited Areas", self, function(self) self:setHideUnvisitedAreas(not self.hideUnvisitedAreas) end)
	context:setOptionChecked(option, self.hideUnvisitedAreas)

	option = context:addOption("Isometric", self, function(self) self:setIsometric(not self.isometric) end)
	context:setOptionChecked(option, self.isometric)

	-- DEV: Apply the style again after reloading ISMapDefinitions.lua
	option = context:addOption("Reapply Style", self,
		function(self)
			MapUtils.initDefaultStyleV1(self)
			MapUtils.overlayPaper(self)
		end)

	local worldX = self.mapAPI:uiToWorldX(x, y)
	local worldY = self.mapAPI:uiToWorldY(x, y)
	if getWorld():getMetaGrid():isValidChunk(worldX / 10, worldY / 10) then
		option = context:addOption("Teleport Here", self, self.onTeleport, worldX, worldY)
	end

	return true
end

function ISWorldMap:onToggleSymbols()
	if self.symbolsUI:isVisible() then
		self.symbolsUI:undisplay()
		self.symbolsUI:setVisible(false)
	else
		self.symbolsUI:setVisible(true)
	end
end

function ISWorldMap:onChangePerspective()
	self:setIsometric(not self.isometric)
end

function ISWorldMap:onCenterOnPlayer()
	if not self.character then
		self.mapAPI:resetView()
		return
	end
	self.mapAPI:centerOn(self.character:getX(), self.character:getY())
end

function ISWorldMap:onTogglePyramid()
	if self.mapAPI:getBoolean("ImagePyramid") then
		self.mapAPI:setBoolean("ImagePyramid", false)
		self.mapAPI:setBoolean("Features", true)
	else
		self.mapAPI:setBoolean("ImagePyramid", true)
		self.mapAPI:setBoolean("Features", false)
	end
end

function ISWorldMap:onChangeOptions(button)
	if self.optionsUI == nil then
		local ui = WorldMapOptions:new(self.width - 300, button.y - 300, 300, 300, self)
		self:addChild(ui)
		ui:setVisible(false)
		self.optionsUI = ui
	end
	if self.optionsUI:isVisible() then
		self.optionsUI:setVisible(false)
		return
	end
	self.optionsUI:synchUI()
	self.optionsUI:setX(math.min(self.width - 20 - self.optionsUI.width, button.parent.x + button.x))
	self.optionsUI:setY(button.parent.y + button.y - self.optionsUI.height)
	self.optionsUI:setVisible(true)
end

function ISWorldMap:onTeleport(worldX, worldY)
	local playerObj = getSpecificPlayer(0)
	if not playerObj then return end
	playerObj:setX(worldX)
	playerObj:setY(worldY)
	playerObj:setZ(0.0)
	playerObj:setLx(worldX)
	playerObj:setLy(worldY)
end

function ISWorldMap:setHideUnvisitedAreas(hide)
	self.hideUnvisitedAreas = hide
	self.mapAPI:setBoolean("HideUnvisited", hide)
end

function ISWorldMap:setIsometric(iso)
	self.isometric = iso
	self.mapAPI:setBoolean("Isometric", iso)
end

function ISWorldMap:setShowCellGrid(show)
	self.showCellGrid = show
	self.mapAPI:setBoolean("CellGrid", show)
end

function ISWorldMap:setShowTileGrid(show)
	self.showTileGrid = show
	self.mapAPI:setBoolean("TileGrid", show)
end

function ISWorldMap:setShowPlayers(show)
	self.showPlayers = show
	self.mapAPI:setBoolean("Players", show)
end

function ISWorldMap:close()
	self:saveSettings()
	self.symbolsUI:undisplay()
	self:setVisible(false)
	self:removeFromUIManager()
	if getSpecificPlayer(0) then
		getWorld():setDrawWorld(true)
	end
	for i=1,getNumActivePlayers() do
		if getSpecificPlayer(i-1) then
			getSpecificPlayer(i-1):setBlockMovement(false)
		end
	end
	if JoypadState.players[self.playerNum+1] then
		setJoypadFocus(self.playerNum, nil)
	end
	if MainScreen.instance and not MainScreen.instance.inGame then
		-- Debug in main menu
		self:setHideUnvisitedAreas(true)
		ISWorldMap_instance = nil
		WorldMapVisited.Reset()
	end
end

function ISWorldMap:isKeyConsumed(key)
	if key == Keyboard.KEY_ESCAPE or key == getCore():getKey("Toggle UI") then return true end
	if key == Keyboard.KEY_C then return true end
	if key == Keyboard.KEY_S then return true end
	return false
end

function ISWorldMap:onKeyPress(key)
	if self.symbolsUI:onKeyPress(key) then
		return
	end
end

function ISWorldMap:onKeyRelease(key)
	if self.symbolsUI:onKeyRelease(key) then
		return
	end
	if key == Keyboard.KEY_ESCAPE or key == getCore():getKey("Toggle UI") then
		self:close()
	end
	if key == Keyboard.KEY_C then
		self:onCenterOnPlayer()
	end
	if key == Keyboard.KEY_S then
		self:onToggleSymbols()
	end
--[[
	if key == Keyboard.KEY_X then
		self:close()
	end
--]]
end

function ISWorldMap:updateJoypad()
	if self.getJoypadFocus then
		self.getJoypadFocus = false;
		if JoypadState.players[self.playerNum+1] then
			setJoypadFocus(self.playerNum, self)
		end
	end

	local currentTimeMs = getTimestampMs()
	self.updateMS = self.updateMS or currentTimeMs
	local dt = currentTimeMs - self.updateMS
	self.updateMS = currentTimeMs

	if self.joyfocus == nil then return end

	local cx = self.mapAPI:getCenterWorldX()
	local cy = self.mapAPI:getCenterWorldY()

	if isJoypadLTPressed(self.joyfocus.id, Joypad.LBumper) then
		if not self.LBumperZoom then
			self.LBumperZoom = self.mapAPI:getZoomF()
		end
		if self.LBumperZoom >= self.mapAPI:getZoomF() then
			self.LBumperZoom = self.mapAPI:getZoomF() - 1.0
			self.mapAPI:zoomAt(self.width / 2, self.height / 2, 2)
		end
	else
		self.LBumperZoom = nil
	end
	if isJoypadRTPressed(self.joyfocus.id, Joypad.RBumper) then
		if not self.RBumperZoom then
			self.RBumperZoom = self.mapAPI:getZoomF()
		end
		if self.RBumperZoom <= self.mapAPI:getZoomF() then
			self.RBumperZoom = self.mapAPI:getZoomF() + 1.0
			self.mapAPI:zoomAt(self.width / 2, self.height / 2, -2)
		end
	else
		self.RBumperZoom = nil
	end

	local x = getControllerPovX(self.joyfocus.id);
	local y = getControllerPovY(self.joyfocus.id);
	if x == 0 then
		x = getJoypadMovementAxisX(self.joyfocus.id)
		if (x > -0.5 and x < 0.5) then x = 0 end
	end
	if y == 0 then
		y = getJoypadMovementAxisY(self.joyfocus.id)
		if (y > -0.5 and y < 0.5) then y = 0 end
	end
	if x ~= 0 then
		if not self.povXms then
			self.povXms = currentTimeMs
		else
			if currentTimeMs - self.povXms <= 150 then
				x = 0
			end
		end
	else
		self.povXms = nil
	end
	if y ~= 0 then
		if not self.povYms then
			self.povYms = currentTimeMs
		else
			if currentTimeMs - self.povYms <= 150 then
				y = 0
			end
		end
	else
		self.povYms = nil
	end
	if self.mapAPI:getBoolean("Isometric") then
		if x ~= 0 and y ~= 0 then
			if x > 0 and y > 0 then
				y = 0
			elseif x < 0 and y < 0 then
				y = 0
			else
				x = 0
			end
		elseif x ~= 0 then
			y = -x
		elseif y ~= 0 then
			x = y
		end
	end
	if x~=0 or y ~= 0 then
		local scale = self.mapAPI:getWorldScale()
		local scrollDelta = (dt / 1000) * (500 / scale)
		local snap = 1
		if x < 0 then
			cx = math.floor((cx + scrollDelta * x) / snap) * snap
		elseif x > 0 then
			cx = math.ceil((cx + scrollDelta * x) / snap) * snap
		end
		if y < 0 then
			cy = math.floor((cy + scrollDelta * y) / snap) * snap
		elseif y > 0 then
			cy = math.ceil((cy + scrollDelta * y) / snap) * snap
		end
		self.mapAPI:centerOn(cx, cy)
	end
end

function ISWorldMap:onJoypadDown(button, joypadData)
	if button == Joypad.AButton then
		self.symbolsUI:onJoypadDownInMap(button, joypadData)
	end
	if button == Joypad.BButton then
		if self.symbolsUI:onKeyRelease(Keyboard.KEY_ESCAPE) then
			return
		end
		self:close()
	end
	if button == Joypad.XButton then
		if self.symbolsUI:isVisible() then
			setJoypadFocus(joypadData.player, self.symbolsUI)
		else
			self.symbolsUI:setVisible(true)
			setJoypadFocus(joypadData.player, self.symbolsUI)
		end
	end
	if button == Joypad.YButton then
		setJoypadFocus(joypadData.player, self.buttonPanel)
	end
end

function ISWorldMap:saveSettings()
	if not MainScreen.instance or not MainScreen.instance.inGame then return end
	local settings = WorldMapSettings.getInstance()
	settings:setDouble("WorldMap.CenterX", self.mapAPI:getCenterWorldX())
	settings:setDouble("WorldMap.CenterY", self.mapAPI:getCenterWorldY())
	settings:setDouble("WorldMap.Zoom", self.mapAPI:getZoomF())
	settings:setBoolean("WorldMap.Isometric", self.mapAPI:getBoolean("Isometric"))
	settings:setBoolean("WorldMap.ShowSymbolsUI", self.symbolsUI:isVisible())
	settings:save()
end

function ISWorldMap:restoreSettings()
	if not MainScreen.instance or not MainScreen.instance.inGame then return end
	local settings = WorldMapSettings.getInstance()
	if settings:getFileVersion() ~= 1 then return end
	local centerX = settings:getDouble("WorldMap.CenterX", 0.0)
	local centerY = settings:getDouble("WorldMap.CenterY", 0.0)
	local zoom = settings:getDouble("WorldMap.Zoom", 0.0)
	local isometric = settings:getBoolean("WorldMap.Isometric")
	local showSymbolsUI = settings:getBoolean("WorldMap.ShowSymbolsUI")
	self.mapAPI:centerOn(centerX, centerY)
	self.mapAPI:setZoom(zoom)
	self.mapAPI:setBoolean("Isometric", isometric)
	if self.mapAPI:getDataCount() == 0 and self.mapAPI:getImagesCount() > 0 then
		self.mapAPI:setBoolean("ImagePyramid", true)
		self.mapAPI:setBoolean("Features", false)
	end
	self.symbolsUI:setVisible(showSymbolsUI)
end

function ISWorldMap:initDataAndStyle()
	local mapAPI = self.mapAPI
	if MainScreen.instance.inGame then
		MapUtils.initDefaultMapData(self)
		mapAPI:setBoundsFromWorld()
		self.hideUnvisitedAreas = true
	else
		-- TEST in main menu
		MapUtils.initDirectoryMapData(self, 'media/maps/Muldraugh, KY')
		mapAPI:setBoundsFromData()
		local markers = mapAPI:getMarkersAPI()
		markers:addGridSquareMarker(11342, 6779, 50, 1.0, 1.0, 0.0, 1.0)
		self.hideUnvisitedAreas = false
	end
	MapUtils.initDefaultStyleV1(self)
	MapUtils.overlayPaper(self)
end

function ISWorldMap:new(x, y, width, height)
	local o = ISPanelJoypad.new(self, x, y, width, height)
	o:noBackground()
	o.anchorRight = true
	o.anchorBottom = true
	o.showCellGrid = false
	o.showTileGrid = false
	o.showPlayers = true
	o.hideUnvisitedAreas = false
	o.isometric = true
	o.character = nil
	o.playerNum = character and character:getPlayerNum() or 0
	o.cross = getTexture("media/ui/LootableMaps/mapCross.png")
	o.texViewIsometric = getTexture("media/textures/worldMap/ViewIsometric.png")
	o.texViewOrthographic = getTexture("media/textures/worldMap/ViewOrtho.png")
	o.texViewPyramid = getTexture("media/textures/worldMap/ViewPyramid.png")
	return o
end

-----

function ISWorldMap.IsAllowed()
	if getCore():getGameMode() == "Tutorial" then return false end
	return SandboxVars.Map and (SandboxVars.Map.AllowWorldMap == true) or false
end

function ISWorldMap.ShowWorldMap(playerNum)
	if not ISWorldMap.IsAllowed() then
		return
	end
	if not ISWorldMap_instance then
		local INSET = 0
		ISWorldMap_instance = ISWorldMap:new(INSET, INSET, getCore():getScreenWidth() - INSET * 2, getCore():getScreenHeight() - INSET * 2)
		ISWorldMap_instance:initialise()
		ISWorldMap_instance:instantiate()
--		ISWorldMap_instance:setAlwaysOnTop(true) -- Breaks context menu
		ISWorldMap_instance.character = getSpecificPlayer(playerNum)
		ISWorldMap_instance.playerNum = playerNum
		ISWorldMap_instance.symbolsUI.character = getSpecificPlayer(playerNum)
		ISWorldMap_instance.symbolsUI.playerNum = playerNum
		ISWorldMap_instance.symbolsUI:checkInventory()
		ISWorldMap_instance:initDataAndStyle()
		ISWorldMap_instance:setHideUnvisitedAreas(ISWorldMap_instance.hideUnvisitedAreas)
		ISWorldMap_instance:setShowPlayers(ISWorldMap_instance.showPlayers)
		ISWorldMap_instance:setShowCellGrid(ISWorldMap_instance.showCellGrid)
		ISWorldMap_instance:setShowTileGrid(ISWorldMap_instance.showTileGrid)
		ISWorldMap_instance:setIsometric(ISWorldMap_instance.isometric)
		ISWorldMap_instance.mapAPI:resetView()
		if ISWorldMap_instance.character then
			ISWorldMap_instance.mapAPI:centerOn(ISWorldMap_instance.character:getX(), ISWorldMap_instance.character:getY())
			ISWorldMap_instance.mapAPI:setZoom(18.0)
		end
		ISWorldMap_instance:restoreSettings()
		ISWorldMap_instance:addToUIManager()
		ISWorldMap_instance.getJoypadFocus = true
		for i=1,getNumActivePlayers() do
			if getSpecificPlayer(i-1) then
				getSpecificPlayer(i-1):setBlockMovement(true)
			end
		end
		return
	end

	ISWorldMap_instance.character = getSpecificPlayer(playerNum)
	ISWorldMap_instance.playerNum = playerNum
	ISWorldMap_instance.symbolsUI.character = getSpecificPlayer(playerNum)
	ISWorldMap_instance.symbolsUI.playerNum = playerNum
	ISWorldMap_instance.symbolsUI:checkInventory()
	ISWorldMap_instance:setVisible(true)
	ISWorldMap_instance:addToUIManager()
	ISWorldMap_instance.getJoypadFocus = true

	if MainScreen.instance.inGame then
		for i=1,getNumActivePlayers() do
			if getSpecificPlayer(i-1) then
				getSpecificPlayer(i-1):setBlockMovement(true)
			end
		end
	else
		ISWorldMap_instance:setHideUnvisitedAreas(false)
	end
end

function ISWorldMap.HideWorldMap(playerNum)
	if not ISWorldMap.IsAllowed() then
		return
	end
	ISWorldMap_instance:close()
end

function ISWorldMap.ToggleWorldMap(playerNum)
	if not ISWorldMap.IsAllowed() then
		return
	end

	-- Forbid showing the map when a splitscreen player has died.
	if ISPostDeathUI and ISPostDeathUI.instance and #ISPostDeathUI.instance > 0 then
		return
	end
	
	if ISWorldMap_instance and ISWorldMap_instance:isVisible() then
		ISWorldMap.HideWorldMap(playerNum)
	else
		local playerObj = getSpecificPlayer(playerNum)
		if playerObj then
			ISTimedActionQueue.clear(playerObj)
			ISTimedActionQueue.add(ISReadWorldMap:new(playerObj))
		else
			-- Debug: In the main menu
			ISWorldMap.ShowWorldMap(playerNum)
		end
	end
end

local KEYSTATE = {}

function ISWorldMap.checkKey(key)
	if key ~= getCore():getKey("Map") then
		return false
	end
	if not ISWorldMap.IsAllowed() then
		return false
	end
	if getCore():getGameMode() == "Tutorial" then
		return false
	end
	if MainScreen.instance and not MainScreen.instance.inGame then
		-- For debugging the map in the main menu without starting a game.
		return getDebug()
	end
	if UIManager.getSpeedControls() and (UIManager.getSpeedControls():getCurrentGameSpeed() == 0) then
		return false
	end
	local playerObj = getSpecificPlayer(0)
	if not playerObj or playerObj:isDead() then
		return false
	end
--[[
	local queue = ISTimedActionQueue.queues[playerObj]
	if queue and #queue.queue > 0 then
		return false
	end
	if getCell():getDrag(0) then
		return false
	end
--]]
	return true
end

function ISWorldMap.onKeyStartPressed(key)
	if not ISWorldMap.checkKey(key) then return end
	if MainScreen.instance and not MainScreen.instance.inGame then
		-- For debugging the map in the main menu without starting a game.
		return
	end
	local radialMenu = getPlayerRadialMenu(0)
	if getCore():getOptionRadialMenuKeyToggle() and radialMenu:isReallyVisible() then
		KEYSTATE.radialWasVisible = true
		radialMenu:removeFromUIManager()
		return
	end
	KEYSTATE.keyPressedMS = getTimestampMs()
	KEYSTATE.radialWasVisible = false
end

function ISWorldMap.onKeyKeepPressed(key)
	if not ISWorldMap.checkKey(key) then return end
	if MainScreen.instance and not MainScreen.instance.inGame then
		-- For debugging the map in the main menu without starting a game.
		return
	end
	if KEYSTATE.radialWasVisible then
		return
	end
	if not KEYSTATE.keyPressedMS then
		return
	end
	local playerNum = 0
	local radialMenu = getPlayerRadialMenu(playerNum)
	local delay = 500
	if (getTimestampMs() - KEYSTATE.keyPressedMS >= delay) and not radialMenu:isReallyVisible() then
		radialMenu:clear()
		radialMenu:addSlice(getText("IGUI_WorldMap_Toggle"), getTexture("media/textures/worldMap/Map_On.png"), ISWorldMap.ToggleWorldMap, playerNum)
		if getPlayerMiniMap(playerNum) then
			radialMenu:addSlice(getText("IGUI_MiniMap_Toggle"), getTexture("media/textures/worldMap/Map_On.png"), ISMiniMap.ToggleMiniMap, playerNum)
		end
		radialMenu:center()
		radialMenu:addToUIManager()
		if JoypadState.players[playerNum+1] then
--			menu:setHideWhenButtonReleased(Joypad.RBumper)
			setJoypadFocus(playerNum, radialMenu)
			getSpecificPlayer(playerNum):setJoypadIgnoreAimUntilCentered(true)
		end
	end
end

function ISWorldMap.onKeyReleased(key)
	if not ISWorldMap.checkKey(key) then return end
--[[
	if not KEYSTATE.keyPressedMS then
		return
	end
--]]
	if MainScreen.instance and not MainScreen.instance.inGame then
		-- For debugging the map in the main menu without starting a game.
		ISWorldMap.ToggleWorldMap(0)
		return
	end
	local playerNum = 0
	local radialMenu = getPlayerRadialMenu(playerNum)
	if radialMenu:isReallyVisible() or KEYSTATE.radialWasVisible then
		if not getCore():getOptionRadialMenuKeyToggle() then
			radialMenu:removeFromUIManager()
		end
		return
	end
	ISWorldMap.ToggleWorldMap(playerNum)
end

function ISWorldMap.OnPlayerDeath(playerObj)
	if ISWorldMap_instance and ISWorldMap_instance:isVisible() then
		ISWorldMap.HideWorldMap(0)
	end
end

Events.OnKeyStartPressed.Add(ISWorldMap.onKeyStartPressed)
Events.OnKeyKeepPressed.Add(ISWorldMap.onKeyKeepPressed)
Events.OnKeyPressed.Add(ISWorldMap.onKeyReleased)
Events.OnPlayerDeath.Add(ISWorldMap.OnPlayerDeath)

