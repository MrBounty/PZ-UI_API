--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require('ISUI/ISScrollingListBox')
require('ISUI/Maps/Editor/WorldMapEditorMode')
require('ISUI/Maps/Editor/WorldMapEditorResizer')

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

---@class WorldMapEditor : ISUIElement
WorldMapEditor = ISUIElement:derive("WorldMapEditor")

function WorldMapEditor:instantiate()
	self.javaObject = UIWorldMap.new(self)
	self.mapAPI = self.javaObject:getAPIv1()
	self.mapAPI:setMapItem(self.mapItem)
	self.styleAPI = self.mapAPI:getStyleAPI()
	self.symbolsAPI = self.mapAPI:getSymbolsAPI()
	self.javaObject:setX(self.x)
	self.javaObject:setY(self.y)
	self.javaObject:setWidth(self.width)
	self.javaObject:setHeight(self.height)
	self.javaObject:setAnchorLeft(self.anchorLeft)
	self.javaObject:setAnchorRight(self.anchorRight)
	self.javaObject:setAnchorTop(self.anchorTop)
	self.javaObject:setAnchorBottom(self.anchorBottom)
	self:setWantKeyEvents(true)
	self.mapAPI:setBoolean("HideUnvisited", false)
	self.mapAPI:setBoolean("Isometric", false)
	MapUtils.initDefaultStyleV1(self)
	self:createChildren()
end

function WorldMapEditor:createChildren()
	local buttonHgt = FONT_HGT_MEDIUM + 8

	for _,mode in ipairs({'DataFiles', 'Bounds', 'Style', 'Annotations', 'Maps', 'Stashes'}) do
		self.mode[mode] = _G["WorldMapEditorMode_" .. mode]:new(self)
		self:addChild(self.mode[mode])
		self.mode[mode]:setVisible(false)
	end

	self.modeButton = {}

	local button = ISButton:new(10, 10, 100, buttonHgt, "DATA FILES", self, self.onSwitchMode)
	button.mode = "DataFiles"
	button.textColor.a = 0.5
	self:addChild(button)
	self.modeButton.DataFiles = button

	button = ISButton:new(button:getRight() + 10, 10, 100, buttonHgt, "BOUNDS", self, self.onSwitchMode)
	button.mode = "Bounds"
	button.textColor.a = 0.5
	self:addChild(button)
	self.modeButton.Bounds = button

	button = ISButton:new(button:getRight() + 10, 10, 100, buttonHgt, "STYLE", self, self.onSwitchMode)
	button.mode = "Style"
	button.textColor.a = 0.5
	self:addChild(button)
	self.modeButton.Style = button

	button = ISButton:new(button:getRight() + 10, 10, 100, buttonHgt, "ANNOTATIONS", self, self.onSwitchMode)
	button.mode = "Annotations"
	button.textColor.a = 0.5
	self:addChild(button)
	self.modeButton.Annotations = button

	button = ISButton:new(button:getRight() + 10, 10, 100, buttonHgt, "MAPS", self, self.onSwitchMode)
	button.mode = "Maps"
	button.textColor.a = 0.5
	self:addChild(button)
	self.modeButton.Maps = button

	button = ISButton:new(button:getRight() + 10, 10, 100, buttonHgt, "STASHES", self, self.onSwitchMode)
	button.mode = "Stashes"
	button.textColor.a = 0.5
	self:addChild(button)
	self.modeButton.Stashes = button
	
	button = ISButton:new(10, self.height - 10 - buttonHgt, 80, buttonHgt, "EXIT", self, self.onExit)
	self:addChild(button)
	
	button = ISButton:new(button:getRight() + 100, self.height - 10 - buttonHgt, 80, buttonHgt, "LUA TO CLIPBOARD", self, self.onGenerateLuaScript)
	self:addChild(button)

	self:onSwitchMode(self.modeButton.DataFiles)
end

function WorldMapEditor:onResolutionChange(oldw, oldh, neww, newh)
	self:setWidth(neww)
	self:setHeight(newh)
end

function WorldMapEditor:update()
	ISPanel.update(self)
	if self.width ~= getCore():getScreenWidth() or self.height ~= getCore():getScreenHeight() then
		self:onResolutionChange(self.width, self.height, getCore():getScreenWidth(), getCore():getScreenHeight())
	end
end

function WorldMapEditor:render()
	local x = self:getMouseX()
	local y = self:getMouseY()
	local worldX = self.mapAPI:uiToWorldX(x, y)
	local worldY = self.mapAPI:uiToWorldY(x, y)
	self:drawTextCentre(string.format("ZOOM : %.2f     POSITION : %d,%d", self.mapAPI:getZoomF(), worldX, worldY), self.width / 2, self.height - FONT_HGT_SMALL - 10, 0, 0, 0, 1, UIFont.Small)
end

function WorldMapEditor:onMouseDown(x, y)
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

function WorldMapEditor:onMouseMove(dx, dy)
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

function WorldMapEditor:onMouseMoveOutside(dx, dy)
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

function WorldMapEditor:onMouseUp(x, y)
	self.dragging = false
	return true
end

function WorldMapEditor:onMouseUpOutside(x, y)
	self.dragging = false
	return true
end

function WorldMapEditor:onMouseWheel(del)
	return false
end

function WorldMapEditor:onSwitchMode(button)
	local mode = button.mode
	if self.currentMode then
		self.modeButton[self.currentMode].textColor.a = 0.5
		self.mode[self.currentMode]:undisplay()
	end
	self.currentMode = mode
	self.modeButton[mode].textColor.a = 1.0
	self.mode[mode]:display()
end

function WorldMapEditor:isKeyConsumed(key)
	if self.mode[self.currentMode]:isKeyConsumed(key) then
		return true
	end
	return true
end

function WorldMapEditor:onKeyPress(key)
	if self.mode[self.currentMode]:onKeyPress(key) then
		return
	end
	if key == Keyboard.KEY_F8 then
		GameKeyboard.eatKeyPress(key)
		self:close()
		return
	end
	if key == Keyboard.KEY_ESCAPE then
		GameKeyboard.eatKeyPress(key)
		self:close()
		return
	end
end

function WorldMapEditor:onKeyRelease(key)
	if self.mode[self.currentMode]:onKeyRelease(key) then
		return
	end
end

function WorldMapEditor:setDataFiles(fileNames)
	self.mapAPI:clearData()
	for _,fileName in ipairs(fileNames) do
		self.mapAPI:addData(fileName)
	end
--	self.mapAPI:setBoundsFromData()
	self.mapAPI:resetView()
end

function WorldMapEditor:loadSettingsFromMap()
	for modeName,mode in pairs(self.mode) do
		mode:loadSettingsFromMap()
	end
end

function WorldMapEditor:onGenerateLuaScript()
	local script = self.mode[self.currentMode]:generateLuaScript()
	Clipboard.setClipboard(script)
end

function WorldMapEditor:onExit(button, x, y)
	self:close()
end

function WorldMapEditor:close()
	self.mode[self.currentMode]:undisplay()
	self.state:fromLua0("exit")
end

-- Called from Java
function WorldMapEditor:showUI()
	self.mode[self.currentMode]:display()
end

function WorldMapEditor:new(x, y, width, height, javaObject)
	local o = ISUIElement.new(self, x, y, width, height)
	o:setAnchorRight(true)
	o:setAnchorBottom(true)
	o.state = javaObject
	o.mapItem = InventoryItemFactory.CreateItem("Base.Map")
	o.bounds = nil
	o.mode = {}
	javaObject:setTable(o)
	return o
end

-- Called from Java
function WorldMapEditor_InitUI(javaObject)
	local UI = WorldMapEditor:new(0, 0, getCore():getScreenWidth(), getCore():getScreenHeight(), javaObject)
	WorldMapEditor_UI = UI
	UI:setVisible(true)
	UI:addToUIManager()
end


