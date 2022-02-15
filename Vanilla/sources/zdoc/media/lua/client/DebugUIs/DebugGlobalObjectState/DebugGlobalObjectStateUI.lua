--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISScrollingListBox"
require "RadioCom/ISUIRadio/ISSliderPanel"
require "DebugUIs/DebugChunkState/ISSectionedPanel"
require "DebugUIS/DebugGlobalObjectState/DebugGlobalObjectState_PropertiesPanel"
require "DebugUIS/DebugGlobalObjectState/DebugGlobalObjectState_Tools"

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

---@class DebugGlobalObjectStateUI : ISPanel
DebugGlobalObjectStateUI = ISPanel:derive("DebugGlobalObjectStateUI")

-----

function DebugGlobalObjectStateUI:SystemList_doDrawItem(y, item, alt)
	local system = item.item
	
	local x = 4

	if self.selected == item.index then
		self:drawRect(0, y, self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
	end

	self:drawText(item.text, x, y, 1, 1, 1, 1, self.font)
	local text = string.format("%d objects", system:getObjectCount())
	self:drawText(text, self.width / 2, y, 1, 1, 1, 1, self.font)
	y = y + self.fontHgt

	self:drawRect(x, y, self.width - 4 * 2, 1, 1.0, 0.5, 0.5, 0.5)
	y = y + 2

	return y
end

-----

function DebugGlobalObjectStateUI:ObjectList_doDrawItem(y, item, alt)
	local x = 4

	if self.selected == item.index then
		self:drawRect(0, y, self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
	end

	local r,g,b,a = 1,1,1,1
	local data = item.item
	local globalObject = data.system:getObjectAt(data.x, data.y, data.z)
	if not globalObject then
		r,g,b = 1.0,0.0,0.0
	elseif not globalObject:getModData():getIsoObject() then
		r,g,b = 0.5,0.5,0.5
	end
	
	self:drawText(item.text, x, y, r, g, b, a, self.font)
	y = y + self.fontHgt

	self:drawRect(x, y, self.width - 4 * 2, 1, 1.0, 0.5, 0.5, 0.5)
	y = y + 2

	return y
end

function DebugGlobalObjectStateUI:ObjectList_OnMouseDoubleClick(item)
	local x,y,z = item.x, item.y, item.z
	local playerObj = getSpecificPlayer(0)
	playerObj:setX(x + 0.5)
	playerObj:setY(y + 0.5)
	playerObj:setZ(z)
	playerObj:setLx(x + 0.5)
	playerObj:setLy(y + 0.5)
	playerObj:setLz(z)
	self.zLevelSlider:setCurrentValue(z)
end

-----

function DebugGlobalObjectStateUI:createChildren()
	self.zLevelSlider = ISSliderPanel:new(10, 10, 150, 20, self, self.onChangeZLevel)
	self.zLevelSlider:initialise()
	self.zLevelSlider:setCurrentValue(self.gameState:fromLua0("getZ"), true)
	self.zLevelSlider:setValues(0, 7, 1, 1, true)
	self:addChild(self.zLevelSlider)
	local top = self.zLevelSlider:getBottom() + 10

	if not isClient() then
		local width = 250
		local height = FONT_HGT_MEDIUM + 3 * 2
		local combo = ISComboBox:new(10, self.zLevelSlider:getBottom() + 10, width, height, self, self.onComboClientServer)
		combo:initialise()
		combo:instantiate()
		combo:setAnchorLeft(false)
		self:addChild(combo)
		combo:addOption("Client")
		combo:addOption("Server")
		self.comboClientServer = combo
		top = self.comboClientServer:getBottom() + 10
	end

	self.objectSections = ISSectionedPanel:new(10, top, 250, self.height - 10 - top)
	self.objectSections.anchorBottom = true
	self.objectSections.maintainHeight = false
	self:addChild(self.objectSections)
	self.objectSections:setScrollChildren(true)
	self.objectSections:addScrollBars()

	self.systemList = ISScrollingListBox:new(0, 0, 250, 100)
	self.systemList.state = self
	self.systemList.doDrawItem = function(self, y, item, alt) return self.state.SystemList_doDrawItem(self, y, item, alt) end
	self.systemList:setFont(UIFont.DebugConsole, 4)
	self.systemList.itemheight = self.systemList.fontHgt + 2
	self.systemList:setHeight(self.systemList.fontHgt * 15)
	self.systemList.doRepaintStencil = true
	self.objectSections:addSection(self.systemList, "Systems")

	self:setSystemList()

	self.objectList = ISScrollingListBox:new(0, 0, 250, 100)
	self.objectList.state = self
	self.objectList.doDrawItem = function(self, y, item, alt) return self.state.ObjectList_doDrawItem(self, y, item, alt) end
	self.objectList:setOnMouseDoubleClick(self, self.ObjectList_OnMouseDoubleClick)
	self.objectList:setFont(UIFont.DebugConsole, 4)
	self.objectList.itemheight = self.objectList.fontHgt + 2
	self.objectList:setHeight(self.objectList.fontHgt * 15)
	self.objectSections:addSection(self.objectList, "Global Objects")

	self.propertiesPanel = DebugGlobalObjectState_PropertiesPanel:new(self.width - 250 - 10, 10, 250, self.height - 10 - 10)
	self.propertiesPanel.anchorBottom = true
	self.propertiesPanel.maintainHeight = false
	self:addChild(self.propertiesPanel)
	self.propertiesPanel:setScrollChildren(true)
	self.propertiesPanel:addScrollBars()

	local toolHgt = 30
	self.toolBar = ISPanel:new(self.width / 2 - 60 / 2, 10, 60, toolHgt)
	self.toolBar:noBackground()
	self:addChild(self.toolBar)
	
	local button3 = ISButton:new(0, 0, 60, toolHgt, "EXIT", self, self.onExit)
	self.toolBar:addChild(button3)
	self.buttonExit = button3

	self.toolBar:setWidth(button3:getRight())
	self.toolBar:setX(self.width / 2 - self.toolBar.width / 2)
end

function DebugGlobalObjectStateUI:onChangeZLevel(value, slider)
	self.gameState:fromLua1("setZ", value)
end

function DebugGlobalObjectStateUI:onComboClientServer()
	self.showServer = not isClient() and (self.comboClientServer.selected == 2)
	self:setSystemList()
end

function DebugGlobalObjectStateUI:onExit()
	self.gameState:fromLua0("exit")
end

function DebugGlobalObjectStateUI:onMouseDown(x, y)
	ISPanel.onMouseDown(self, x, y)

	self.mouseDown = true
	self.mouseMoved = false
	self.mouseDownX = x
	self.mouseDownY = y

	if self.currentTool then
		self.currentTool:onMouseDown(x, y)
	end
	return false
end

function DebugGlobalObjectStateUI:onMouseMove(dx, dy)
	ISPanel.onMouseMove(self, dx, dy)
	if self.mouseDown then
		if not self.mouseMoved then
			if math.abs(self.mouseDownX - self:getMouseX()) > 50 or math.abs(self.mouseDownY - self:getMouseY()) > 50 then
				self.mouseMoved = true
			end
		end
	elseif not self:isMouseOverChild() then
		self:updateObjectList()
	end
	if self.currentTool then
		self.currentTool:onMouseMove(dx, dy)
	end
	return false
end

function DebugGlobalObjectStateUI:isMouseOverChild()
	local children = self.javaObject:getControls()
	for i=children:size(),1,-1 do
		if children:get(i-1):getTable():isMouseOver() then
			return true
		end
	end
	return false
end

function DebugGlobalObjectStateUI:onMouseUp(x, y)
	ISPanel.onMouseUp(self, x, y)
	self.mouseDown = false
	if not self.mouseMoved then
		local square,sqx,sqy,sqz = self:pickSquare(x, y)
		if square and square == self.selectedSquare then
			self.selectedSquare = nil
		elseif square ~= self.selectedSquare then
			self.selectedSquare = square
			self:setObjectList(square, sqx, sqy, sqz)
		end
	end
	if self.currentTool then
		self.currentTool:onMouseUp(x, y)
	end
	return false
end

function DebugGlobalObjectStateUI:onMouseUpOutside(x, y)
	ISPanel.onMouseUpOutside(self, x, y)
	self.mouseDown = false
	if self.currentTool then
		self.currentTool:onMouseUp(x, y)
	end
	return false
end

function DebugGlobalObjectStateUI:onMouseWheel(del)
	local playerIndex = self.gameState:fromLua0("getPlayerIndex")
	if playerIndex == 0 then return false end -- UIManager already does this
	getCore():doZoomScroll(playerIndex, del)
	return false
end

function DebugGlobalObjectStateUI:onResolutionChange(oldw, oldh, neww, newh)
	self:setWidth(neww)
	self:setHeight(newh)
	self.toolBar:setX(self.width / 2 - self.toolBar.width / 2)
end

function DebugGlobalObjectStateUI:update()
	ISPanel.update(self)
	if self.width ~= getCore():getScreenWidth() or self.height ~= getCore():getScreenHeight() then
		self:onResolutionChange(self.width, self.height, getCore():getScreenWidth(), getCore():getScreenHeight())
	end
	self:updateSelectedSystem()
	self:updateSelectedObject()
	self:updateObjectProps()
end

function DebugGlobalObjectStateUI:pickSquare(x, y)
	local playerIndex = self.gameState:fromLua0("getPlayerIndex")
	local z = self.gameState:fromLua0("getZ")
	local worldX = screenToIsoX(playerIndex, self:getMouseX(), self:getMouseY(), z)
	local worldY = screenToIsoY(playerIndex, self:getMouseX(), self:getMouseY(), z)
	return getCell():getGridSquare(worldX, worldY, z), worldX, worldY, z
end

function DebugGlobalObjectStateUI:setSystemList()
	self.systemList:clear()
	local GlobalObjects = self.showServer and SGlobalObjects or CGlobalObjects
	for i=1,GlobalObjects.getSystemCount() do
		local system = GlobalObjects.getSystemByIndex(i-1)
		self.systemList:addItem(system:getName(), system)
	end
end

function DebugGlobalObjectStateUI:updateSelectedSystem()
	local item = self.systemList.items[self.systemList.selected]
	if item then
		local system = item.item
		if system == self.selectedSystem then return end
		self.selectedSystem = system
		self.objectList:clear()
		for i=1,system:getObjectCount() do
			local object = system:getObjectByIndex(i-1)
			local item = { system = system, x = object:getX(), y = object:getY(), z = object:getZ() }
			self.objectList:addItem(system:getName()..' #'..tostring(i), item)
		end
	else
		if not self.selectedSystem then return end
		self.selectedSystem = nil
		self.objectList:clear()
	end

	self.propertiesPanel:setSystem(self.selectedSystem)
end

function DebugGlobalObjectStateUI:updateSelectedObject()
	local item = self.objectList.items[self.objectList.selected]
	if item then
		if item.item == self.selectedObject then return end
		self.selectedObject = item.item
		local object = item.item.system:getObjectAt(item.item.x, item.item.y, item.item.z)
		self.propertiesPanel:setObject(object)
	else
		if not self.selectedObject then return end
		self.selectedObject = nil
		self.propertiesPanel:setObject(nil)
	end
end

function DebugGlobalObjectStateUI:updateObjectList()
--	if self.selectedSquare then return end
	local square,x,y,z = self:pickSquare(self:getMouseX(), self:getMouseY())
	self:setObjectList(square, x, y, z)
end

function DebugGlobalObjectStateUI:setObjectList(square, x, y, z)
	if not square then return end
	local GlobalObjects = self.showServer and SGlobalObjects or CGlobalObjects
	for i=1,GlobalObjects.getSystemCount() do
		local system = GlobalObjects.getSystemByIndex(i-1)
		local globalObject = system:getObjectAt(x, y, z)
		if globalObject then
			self.systemList.selected = i
			self:updateSelectedSystem()
			for j=1,system:getObjectCount() do
				globalObject = system:getObjectByIndex(j-1)
				local item = self.objectList.items[j].item
				if globalObject:getX() == square:getX() and globalObject:getY() == square:getY() and globalObject:getZ() == square:getZ() then
					self.objectList.selected = j
					self.objectList:ensureVisible(j)
					break
				end
			end
		end
	end
end

function DebugGlobalObjectStateUI:updateObjectProps()
--[[
	local item = self.objectList.items[self.objectList.mouseoverselected] or self.objectList.items[self.objectList.selected]
	local object = item and item.item or nil
	self.objPropsPanel:setObject(object)
--]]
end

-- Called from Java
function DebugGlobalObjectStateUI:showUI()
end

function DebugGlobalObjectStateUI:new(x, y, width, height, gameState)
	local o = ISPanel.new(self, x, y, width, height)
	o.gameState = gameState
	gameState:setTable(o)
	o:setAnchorRight(true)
	o:setAnchorBottom(true)
	o:noBackground()
	o.dragCameraTool = DebugGlobalObjectStateUI_DragCameraTool:new(o)
	o.currentTool = o.dragCameraTool
	return o
end

-- Called from Java
function DebugGlobalObjectState_InitUI(gameState)
	local UI = DebugGlobalObjectStateUI:new(0, 0, getCore():getScreenWidth(), getCore():getScreenHeight(), gameState)
	DebugGlobalObjectState_UI = UI
	UI:setVisible(true)
	UI:addToUIManager()
end


