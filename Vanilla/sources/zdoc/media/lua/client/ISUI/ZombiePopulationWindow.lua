require "ISUI/ISCollapsableWindow"

---@class ZombiePopulationWindow : ISCollapsableWindow
ZombiePopulationWindow = ISCollapsableWindow:derive("ZombiePopulationWindow")

function ZombiePopulationWindow:initialise()
	ISCollapsableWindow.initialise(self)
	self.title = "Zombie Population"
end

function ZombiePopulationWindow:createChildren()
	ISCollapsableWindow.createChildren(self)

	self.renderPanel = ISPanel:new(0, 0, self.width, self.height - self:titleBarHeight() - self:resizeWidgetHeight())
	self.renderPanel.render = ZombiePopulationWindow.renderTex
	self.renderPanel:initialise()
--	self.renderPanel:instantiate()
	self.renderPanel.onMouseDown = ZombiePopulationWindow.onMapMouseDown
	self.renderPanel.onMouseUp = ZombiePopulationWindow.onMapMouseUp
	self.renderPanel.onMouseUpOutside = ZombiePopulationWindow.onMapMouseUpOutside
	self.renderPanel.onMouseMove = ZombiePopulationWindow.onMapMouseMove
	self.renderPanel.onRightMouseDown = ZombiePopulationWindow.onMapRightMouseDown
	self.renderPanel.onRightMouseUp = ZombiePopulationWindow.onMapRightMouseUp
	self.renderPanel.onRightMouseUpOutside = ZombiePopulationWindow.onMapRightMouseUpOutside
	self.renderPanel.onMouseWheel = ZombiePopulationWindow.onRenderMouseWheel
	self.renderPanel:setAnchorRight(true)
	self.renderPanel:setAnchorBottom(true)
	self.renderPanel.parent = self
	self.renderPanel.renderer = zpopNewRenderer()
	self.renderPanel.renderer:load()
	self:addView(self.renderPanel)
end

function ZombiePopulationWindow:close()
	self:removeFromUIManager()
end

function ZombiePopulationWindow:onMapMouseDown(x, y)
	x = self.renderer:uiToWorldX(x)
	y = self.renderer:uiToWorldY(y)
--	addVirtualZombie(x, y)
	if isKeyDown(Keyboard.KEY_LSHIFT) then
		self.settingPath = true
		self.renderer:setWallFollowerStart(x, y)
		self.renderer:setWallFollowerEnd(x, y)
		return true
	end
	if isKeyDown(Keyboard.KEY_LCONTROL) then
		self.renderer:setWallFollowerEnd(x, y)
		return true
	end
	return false
end

function ZombiePopulationWindow:onMapMouseUp(x, y)
	if self.settingPath then
		self.settingPath = false
	end
end

function ZombiePopulationWindow:onMapMouseUpOutside(x, y)
	if self.settingPath then
		self.settingPath = false
	end
end

function ZombiePopulationWindow:onMapMouseMove(dx, dy)
	self.javaObject:setConsumeMouseEvents(self.panning or false)
	if self.panning then
		if not self.mouseMoved and (math.abs(self:getMouseX() - self.mouseDownX) > 4 or math.abs(self:getMouseY() - self.mouseDownY) > 4) then
			self.mouseMoved = true
		end
		if self.mouseMoved then
			self.parent.xpos = self.parent.xpos - ((dx)/self.parent.zoom)
			self.parent.ypos = self.parent.ypos - ((dy)/self.parent.zoom)
		end
		return true
	end
	if self.settingPath then
		local x = self.renderer:uiToWorldX(self:getMouseX())
		local y = self.renderer:uiToWorldY(self:getMouseY())
		self.renderer:wallFollowerMouseMove(x, y)
	end
	return false
end

function ZombiePopulationWindow:onMapRightMouseDown(x, y)
	self.mouseMoved = false
	self.mouseDownX = x
	self.mouseDownY = y
	self.panning = true
	return true
end

function ZombiePopulationWindow:onAddWorldSound(worldX, worldY)
	addSound(getPlayer(), math.floor(worldX), math.floor(worldY), 0, 100, 100)
end

function ZombiePopulationWindow:onTeleport(worldX, worldY)
	local player = getSpecificPlayer(0)
	player:setX(worldX)
	player:setY(worldY)
	player:setLx(worldX)
	player:setLy(worldY)
end

function ZombiePopulationWindow:onChangeOption(option)
	option:setValue(not option:getValue())
	self.renderPanel.renderer:save()
end

function ZombiePopulationWindow:onMapRightMouseUp(x, y)
	self.panning = false
	if not self.mouseMoved then
		local playerNum = 0
		local cellX = self.renderer:uiToWorldX(x) / 300
		local cellY = self.renderer:uiToWorldY(y) / 300
		cellX = math.floor(cellX)
		cellY = math.floor(cellY)
		local context = ISContextMenu.get(playerNum, x + self:getAbsoluteX(), y + self:getAbsoluteY())
		context:addOption("Clear Zombies", cellX, zpopClearZombies, cellY)
		context:addOption("Spawn Time To Zero", cellX, zpopSpawnTimeToZero, cellY)
		context:addOption("Spawn Now", cellX, zpopSpawnNow, cellY)
		local worldX = self.renderer:uiToWorldX(x)
		local worldY = self.renderer:uiToWorldY(y)
		context:addOption("World Sound (100)", self.parent, ZombiePopulationWindow.onAddWorldSound, worldX, worldY)
		context:addOption("Teleport Here", self.parent, ZombiePopulationWindow.onTeleport, worldX, worldY)

		local subMenu = context:getNew(context)
		for i=1,self.renderer:getOptionCount() do
			local debugOption = self.renderer:getOptionByIndex(i-1)
			local option = subMenu:addOption(debugOption:getName(), self.parent, ZombiePopulationWindow.onChangeOption, debugOption)
			if debugOption:getType() == "boolean" then
				subMenu:setOptionChecked(option, debugOption:getValue())
			end
		end
		local subMenuOption = context:addOption("Display", nil, nil)
		context:addSubMenu(subMenuOption, subMenu)
	end
	return true
end

function ZombiePopulationWindow:onMapRightMouseUpOutside(x, y)
	self.panning = false
	return true
end

function ZombiePopulationWindow:onRenderMouseWheel(del)
	if del > 0 then
		self.parent.zoom = self.parent.zoom * 0.8
	else
		self.parent.zoom = self.parent.zoom * 1.2
	end
	if self.parent.zoom > 30 then self.parent.zoom = 30 end
	return true
end

function ZombiePopulationWindow:renderTex()
	self:setStencilRect(0, 0, self:getWidth(), self:getHeight())
	self.renderer:render(self.javaObject, self.parent.zoom, self.parent.xpos, self.parent.ypos)
	self:clearStencilRect()
end

function ZombiePopulationWindow:new(x, y, width, height)
	local o = ISCollapsableWindow:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.backgroundColor = {r=0, g=0, b=0, a=1.0}
	o.xpos = getSpecificPlayer(0):getX()
	o.ypos =  getSpecificPlayer(0):getY()
	o.zoom = 1
    ISDebugMenu.RegisterClass(self);
	return o
end

function ZombiePopulationWindow.OnOpenPanel()
	if ZombiePopulationWindow.instance == nil then
		local ui = ZombiePopulationWindow:new(20, 20, 400, 400)
		ui:initialise()
		ui:instantiate()
		ZombiePopulationWindow.instance = ui
	end
	ZombiePopulationWindow.instance:addToUIManager()
end

function newZombiePopulationWindow()
	local ui = ZombiePopulationWindow:new(20, 20, 400, 400)
	ui:initialise()
	ui:instantiate()
	ui:addToUIManager()
end

