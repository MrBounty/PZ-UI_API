--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISUIElement"

ISMiniMapOuter = ISPanelJoypad:derive("ISMiniMapOuter")
ISMiniMapInner = ISUIElement:derive("ISMiniMapInner")

function ISMiniMapInner:instantiate()
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
	self:createChildren()
end

function ISMiniMapInner:prerenderHack()
	if self.dragging then return end
	local playerObj = getSpecificPlayer(self.playerNum)
	if not playerObj then return end
	local vehicle = playerObj:getVehicle()
	if vehicle then
		self.mapAPI:centerOn(vehicle:getX(), vehicle:getY())
	else
		self.mapAPI:centerOn(playerObj:getX(), playerObj:getY())
	end
end

function ISMiniMapInner:onMouseDown(x, y)
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

function ISMiniMapInner:onMouseUp(x, y)
	if self.dragging then
		self.dragging = false
		if self.dragMoved then return end
		ISWorldMap.ToggleWorldMap(self.playerNum)
	end
end

function ISMiniMapInner:onMouseUpOutside(x, y)
	self:onMouseUp(x, y)
end

function ISMiniMapInner:onMouseMove(dx, dy)
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
		return true
	end
	return false
end

function ISMiniMapInner:onMouseMoveOutside(dx, dy)
	return self:onMouseMove(dx, dy)
end

function ISMiniMapInner:onMouseWheel(del)
	self.mapAPI:zoomAt(self.width / 2, self.height / 2, del)
	return true
end

function ISMiniMapInner:onRightMouseDown(x, y)
	self.rightMouseDown = true
end

function ISMiniMapInner:onRightMouseUp(x, y)
	if not self.rightMouseDown then return end
	self.rightMouseDown = false

	local playerNum = 0
	local playerObj = getSpecificPlayer(0)
	if not playerObj then return end
	local context = ISContextMenu.get(playerNum, x + self:getAbsoluteX(), y + self:getAbsoluteY())

	local worldX = self.mapAPI:uiToWorldX(x, y)
	local worldY = self.mapAPI:uiToWorldY(x, y)
	if getDebug() and getWorld():getMetaGrid():isValidChunk(worldX / 10, worldY / 10) then
		option = context:addOption("Teleport Here", self, self.onTeleport, worldX, worldY)
	end

	if context.numOptions == 1 then
		context:setVisible(false)
	end
end

function ISMiniMapInner:onRightMouseUpOutside(x, y)
	self.rightMouseDown = false
end

function ISMiniMapInner:onTeleport(worldX, worldY)
	local playerObj = getSpecificPlayer(0)
	if not playerObj then return end
	playerObj:setX(worldX)
	playerObj:setY(worldY)
	playerObj:setZ(0.0)
	playerObj:setLx(worldX)
	playerObj:setLy(worldY)
end

function ISMiniMapInner:new(x, y, width, height, playerNum)
	local o = ISUIElement.new(self, x, y, width, height)
	o.playerNum = playerNum
	return o
end

-----

function ISMiniMapOuter:createChildren()
	self.inner = ISMiniMapInner:new(self.borderSize, self.borderSize, self.width - self.borderSize * 2,
		self.height - self.borderSize * 2 - self.bottomHeight, self.playerNum)
	self:addChild(self.inner)

	local btnWid = 31
	local btnHgt = self.bottomHeight - 1

	self.button1 = ISButton:new(self.borderSize, self.inner:getBottom() + 1, btnWid, btnHgt, "M", self, ISMiniMapOuter.onButton1)
	self.button1.borderColor = {r=0.4, g=0.4, b=0.4, a=1}
	self:addChild(self.button1)

	self.button2 = ISButton:new(self.button1:getRight() + 2, self.button1.y, btnWid, btnHgt, "-", self, function(self) end)
	self.button2.borderColor = {r=0.4, g=0.4, b=0.4, a=1}
	self.button2:setRepeatWhilePressed(ISMiniMapOuter.onButton2)
	self:addChild(self.button2)

	self.button3 = ISButton:new(self.button2:getRight() + 2, self.button1.y, btnWid, btnHgt, "+", self, function(self) end)
	self.button3.borderColor = {r=0.4, g=0.4, b=0.4, a=1}
	self.button3:setRepeatWhilePressed(ISMiniMapOuter.onButton3)
	self:addChild(self.button3)

	self.button4 = ISButton:new(self.button3:getRight() + 2, self.button1.y, btnWid, btnHgt, "~", self, ISMiniMapOuter.onButton4)
	self.button4.borderColor = {r=0.4, g=0.4, b=0.4, a=1}
	self:addChild(self.button4)

	self.button5 = ISButton:new(self.button4:getRight() + 2, self.button1.y, btnWid, btnHgt, "S", self, ISMiniMapOuter.onButton5)
	self.button5.borderColor = {r=0.4, g=0.4, b=0.4, a=1}
	self:addChild(self.button5)

	self.button6 = ISButton:new(self.button5:getRight() + 2, self.button1.y, btnWid, btnHgt, "X", self, ISMiniMapOuter.onButton6)
	self.button6.borderColor = {r=0.4, g=0.4, b=0.4, a=1}
	self:addChild(self.button6)

	self:insertNewLineOfButtons(self.button1, self.button2, self.button3, self.button4, self.button5, self.button6)
	self.joypadIndex = 1
	self.joypadIndexY = 1
end

function ISMiniMapOuter:prerender()
	ISPanelJoypad.prerender(self)
	self:setPosition()
	self:drawRectStatic(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b)
	self:drawRectBorderStatic(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b)

	-- This centers on the player position, but prerender() is called after
	-- the map is rendered.
	self.inner:prerenderHack()
end

function ISMiniMapOuter:render()
	ISPanelJoypad.render(self)

	if self.joyfocus then
		self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.4, 0.2, 1.0, 1.0);
		self:drawRectBorder(1, 1, self:getWidth()-2, self:getHeight()-2, 0.4, 0.2, 1.0, 1.0);
	end
end

function ISMiniMapOuter:setPosition()
	local sx = getPlayerScreenLeft(self.playerNum)
	local sy = getPlayerScreenTop(self.playerNum)
	local sw = getPlayerScreenWidth(self.playerNum)
	local sh = getPlayerScreenHeight(self.playerNum)
	if JoypadState.players[self.playerNum+1] then
		self:setX(sx + sw - 10 - self.width)
		self:setY(sy + sh - 100 - self.height)
	elseif getNumActivePlayers() > 1 then
		self:setX(sx + sw - 10 - self.width)
		self:setY(sy + sh - 100 - self.height)
	else
		self:setX(sx + sw - 10 - self.width)
		self:setY(sy + sh - 10 - self.height)
	end
end

function ISMiniMapOuter:onButton1()
	ISWorldMap.ToggleWorldMap(self.playerNum)
end

function ISMiniMapOuter:onButton2()
	self.inner.mapAPI:zoomAt(self.inner.width / 2, self.inner.height/ 2, 1)
end

function ISMiniMapOuter:onButton3()
	self.inner.mapAPI:zoomAt(self.inner.width / 2, self.inner.height/ 2, -1)
end

function ISMiniMapOuter:onButton4()
	self.inner.mapAPI:setBoolean("Isometric", not self.inner.mapAPI:getBoolean("Isometric"))
end

function ISMiniMapOuter:onButton5()
	self.inner.mapAPI:setBoolean("Symbols", not self.inner.mapAPI:getBoolean("Symbols"))
end

function ISMiniMapOuter:onButton6()
	ISMiniMap.ToggleMiniMap(self.playerNum)
end

function ISMiniMapOuter:saveSettings()
	if self.playerNum ~= 0 then return end
	local settings = WorldMapSettings.getInstance()
	local mapAPI = self.inner.mapAPI
	settings:setDouble("MiniMap.Zoom", mapAPI:getZoomF())
	settings:setBoolean("MiniMap.Isometric", mapAPI:getBoolean("Isometric"))
	settings:setBoolean("MiniMap.ShowSymbols", mapAPI:getBoolean("Symbols"))
	settings:save()
end

function ISMiniMapOuter:restoreSettings()
	if self.playerNum ~= 0 then return end
	local settings = WorldMapSettings.getInstance()
	if settings:getFileVersion() ~= 1 then return end
	local mapAPI = self.inner.mapAPI
	local zoom = settings:getDouble("MiniMap.Zoom", 0.0)
	local isometric = settings:getBoolean("MiniMap.Isometric")
	local showSymbols = settings:getBoolean("MiniMap.ShowSymbols")
	mapAPI:setZoom(zoom)
	mapAPI:setBoolean("Isometric", isometric)
	mapAPI:setBoolean("Symbols", showSymbols)
end

function ISMiniMapOuter:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData)
	self:restoreJoypadFocus(joypadData)
end

function ISMiniMapOuter:onLoseJoypadFocus(joypadData)
	ISPanelJoypad.onLoseJoypadFocus(self, joypadData)
	self:clearJoypadFocus(joypadData)
end

function ISMiniMapOuter:onJoypadDown(button, joypadData)
	if button == Joypad.BButton then
--		self:clearJoypadFocus(joypadData)
		setJoypadFocus(self.playerNum, nil)
		return
	end
	ISPanelJoypad.onJoypadDown(self, button, joypadData)
end

function ISMiniMapOuter:new(x, y, width, height, playerNum)
	local o = ISPanelJoypad.new(self, x, y, width, height)
	o.playerNum = playerNum
	o.backgroundColor = {r=0, g=0, b=0, a=0.8}
	o.borderColor = {r=0.4, g=0.4, b=0.4, a=1}
	o.borderSize = 2
	o.bottomHeight = getTextManager():getFontHeight(UIFont.Small)
	return o
end

-----

ISMiniMap = {}

function ISMiniMap.IsAllowed()
	if getCore():getGameMode() == "Tutorial" then return false end
	if not ISWorldMap.IsAllowed() then return end
	return SandboxVars.Map and (SandboxVars.Map.AllowMiniMap == true) or false
end

function ISMiniMap.InitPlayer(playerNum)
	local width = 200
	local height = 200 + 20
	local sx = getPlayerScreenLeft(playerNum)
	local sy = getPlayerScreenTop(playerNum)
	local sw = getPlayerScreenWidth(playerNum)
	local sh = getPlayerScreenHeight(playerNum)
	local MINIMAP = ISMiniMapOuter:new(sx + sw - 10 - width, sy + sh - 10 - height, width, height, playerNum)
	MINIMAP:initialise()
	MINIMAP:instantiate()

	local INNER = MINIMAP.inner

	local dirs = getLotDirectories()
	for i=1,dirs:size() do
--[[
		local file = 'media/maps/'..dirs:get(i-1)..'/worldmap-forest.xml'
		if fileExists(file) then
			INNER.mapAPI:addData(file)
		end
--]]
		local file = 'media/maps/'..dirs:get(i-1)..'/worldmap.xml'
		if fileExists(file) then
			INNER.mapAPI:addData(file)
		end
		INNER.mapAPI:addImages('media/maps/'..dirs:get(i-1))
	end
	INNER.mapAPI:setBoundsFromWorld()
	INNER.mapAPI:setZoom(19)

	INNER.mapAPI:setBoolean("HideUnvisited", true)
	INNER.mapAPI:setBoolean("Players", true)
	INNER.mapAPI:setBoolean("Symbols", false)
	INNER.mapAPI:setBoolean("MiniMapSymbols", true)

	MapUtils.initDefaultStyleV1(INNER)

	MINIMAP:restoreSettings()

	local settings = WorldMapSettings.getInstance()
	if settings:getBoolean("MiniMap.StartVisible") then
		MINIMAP:addToUIManager()
	end

	return MINIMAP
end

function ISMiniMap.ToggleMiniMap(playerNum)
	local mm = getPlayerMiniMap(playerNum)
	if not mm then return end
	local startVisible = false
	if mm:isReallyVisible() then
		if mm.joyfocus then
			mm:clearJoypadFocus(mm.joyfocus)
			setJoypadFocus(playerNum, nil)
		end
		mm:removeFromUIManager()
		startVisible = false
	else
		mm:addToUIManager()
		startVisible = true
	end
	if playerNum == 0 then
		local settings = WorldMapSettings.getInstance()
		settings:setBoolean("MiniMap.StartVisible", startVisible)
	end
end

function ISMiniMap.FocusMiniMap(playerNum)
	local mm = getPlayerMiniMap(playerNum)
	if not mm then return end
	if not mm:isReallyVisible() then
		ISMiniMap.ToggleMiniMap(playerNum)
	end
	setJoypadFocus(playerNum, mm)
end

function ISMiniMap.Recreate(playerNum)
	getPlayerMiniMap(playerNum):removeFromUIManager()
	getPlayerData(playerNum).miniMap = ISMiniMap.InitPlayer(playerNum)
end
