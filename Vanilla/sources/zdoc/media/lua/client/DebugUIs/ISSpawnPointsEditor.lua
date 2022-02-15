--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isServer() then return end

require "ISUI/ISCollapsableWindow"
require "ISUI/ISScrollingListBox"

---@class ISSpawnPointsEditor : ISCollapsableWindow
ISSpawnPointsEditor = ISCollapsableWindow:derive("ISSpawnPointsEditor")

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

function ISSpawnPointsEditor:createChildren()
	ISCollapsableWindow.createChildren(self)
	
	local th = self:titleBarHeight()
	local rh = self:resizeWidgetHeight()

	local itemPadY = 3
	local itemHgt = FONT_HGT_SMALL + itemPadY * 2

	self.mapList = ISScrollingListBox:new(4, th + 4, 200, itemHgt * 4)
	self.mapList:initialise()
	self.mapList:instantiate()
	self.mapList.drawBorder = true
	self.mapList:setFont(UIFont.Small, itemPadY)
	self.mapList:setOnMouseDownFunction(self, self.onMapSelected)
	self:addChild(self.mapList)

	self.professionList = ISScrollingListBox:new(4, self.mapList:getBottom() + 4, self.mapList:getWidth(), self.height - rh - 4 - self.mapList:getBottom() - 4)
	self.professionList.anchorBottom = true
	self.professionList:initialise()
	self.professionList:instantiate()
	self.professionList.drawBorder = true
	self.professionList:setFont(UIFont.Small, itemPadY)
	self.professionList:setOnMouseDownFunction(self, self.onProfessionSelected)
	self:addChild(self.professionList)

	self.pointList = ISScrollingListBox:new(self.mapList:getRight() + 4, th + 4, self.width - 4 - self.mapList:getRight() - 4, self.height - rh - th - 4 * 2)
	self.pointList.anchorBottom = true
	self.pointList.anchorRight = true
	self.pointList.doDrawItem = self.doDrawPointListItem
	self.pointList:initialise()
	self.pointList:instantiate()
	self.pointList.drawBorder = true
	self.pointList:setFont(UIFont.Small, itemPadY)
	self.pointList.onRightMouseUp = self.PointList_onRightMouseUp
	self.pointList:setOnMouseDownFunction(self, self.onPointSelected)
	self.pointList:setOnMouseDoubleClick(self, self.onPointDoubleClick)
	self:addChild(self.pointList)

	self.resizeWidget2:bringToTop()
	self.resizeWidget:bringToTop()

	self:fillMapList()
	if self.mapList:size() > 0 then
		self:onMapSelected(self.mapList.items[1].item)
	end
end

function ISSpawnPointsEditor:fillMapList()
	self.mapList:clear()
	if getCore():isChallenge() then
		self.mapList:addItem(getWorld():getMap(), getWorld():getMap())
		return
	end
	for _,directory in ipairs(getMapDirectoryTable()) do
		self.mapList:addItem(directory, directory)
	end
end

function ISSpawnPointsEditor:doDrawPointListItem(y, item, alt)
	local point = item.item.point

	local x = 10

	local isMouseOver = self.mouseoverselected == item.index and not self:isMouseOverScrollBar()
	if self.selected == item.index then
		self:drawRect(0, y, self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
	elseif isMouseOver then
		self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.25, 1.0, 1.0, 1.0)
	end

	local gx = point.worldX * 300 + point.posX + 0.5
	local gy = point.worldY * 300 + point.posY + 0.5
	local gz = point.posZ or 0
	local text = string.format("cell=%d,%d   pos=%d,%d,%d   square=%d,%d,%d", point.worldX, point.worldY, point.posX, point.posY, gz, gx, gy, gz)
	self:drawText(text, x, y + (self.itemheight - self.fontHgt) / 2, 0.9, 0.9, 0.9, 0.9, self.font)

	self:drawRect(0, y + item.height - 1, self.width, 1, 1.0, 0.5, 0.5, 0.5)

	return y + item.height
end

function ISSpawnPointsEditor:onMapSelected(directory)
	self.professionList:clear()
	self.pointList:clear()
	local filename = "media/maps/" .. directory .. "/spawnpoints.lua"
	if not fileExists(filename) then return end
	SpawnPoints = nil -- undefine the function
	reloadLuaFile(filename)
	local spawnPointTable = SpawnPoints()
	for profession,points in pairs(spawnPointTable) do
		for _,point in ipairs(points) do
			point.posZ = point.posZ or 0
		end
		self.professionList:addItem(profession, { profession = profession, points = points })
	end
	if self.professionList:size() > 0 then
		self:onProfessionSelected()
	end
end

function ISSpawnPointsEditor:onProfessionSelected()
	self.pointList:clear()
	local points = self.professionList.items[self.professionList.selected].item.points
	for i,point in ipairs(points) do
		self.pointList:addItem("Point " .. i, { point = point })
	end
	if self.pointList:size() > 0 then
		self:onPointSelected()
	end
end

function ISSpawnPointsEditor:onPointSelected()
end

function ISSpawnPointsEditor:onPointDoubleClick(item)
	local point = item.point
	local playerObj = getSpecificPlayer(0)
	local x = point.worldX * 300 + point.posX + 0.5
	local y = point.worldY * 300 + point.posY + 0.5
	local z = point.posZ or 0

	playerObj:setX(x)
	playerObj:setY(y)
	playerObj:setY(z)
	playerObj:setLx(x)
	playerObj:setLy(y)
	playerObj:setLz(z)
end

function ISSpawnPointsEditor:PointList_onRightMouseUp(x, y)
	local playerNum = 0
	local context = ISContextMenu.get(playerNum, x + self:getAbsoluteX(), y + self:getAbsoluteY())

	local subMenu = context:getNew(context)
	subMenu:addOption("This Profession Only", self.parent, ISSpawnPointsEditor.onSetPointToPlayerPosition, false)
	subMenu:addOption("All Professions", self.parent, ISSpawnPointsEditor.onSetPointToPlayerPosition, true)
	local option = context:addOption("Change Selected Point To Player's Position", nil, nil)
	option.notAvailable = self:size() == 0
	context:addSubMenu(option, subMenu)

	subMenu = context:getNew(context)
	subMenu:addOption("This Profession Only", self.parent, ISSpawnPointsEditor.onRemovePoint, false)
	subMenu:addOption("All Professions", self.parent, ISSpawnPointsEditor.onRemovePoint, true)
	option = context:addOption("Remove Selected Point", nil, nil)
	option.notAvailable = self:size() == 0
	context:addSubMenu(option, subMenu)

	context:addOption("Copy All To Clipboard", self.parent, ISSpawnPointsEditor.onCopyToClipboard)
end

function ISSpawnPointsEditor:isSamePoint(point1, point2)
	return (point1.worldX == point2.worldX) and (point1.worldY == point2.worldY) and
		(point1.posX == point2.posX) and (point1.posY == point2.posY) and (point1.posZ == point2.posZ)
end

function ISSpawnPointsEditor:onSetPointToPlayerPosition(allProfessions)
	local item = self.pointList.items[self.pointList.selected]
	if item == nil then return end
	local pointCopy = copyTable(item.item.point)

	local playerObj = getSpecificPlayer(0)
	local point1 = {}
	point1.worldX = math.floor(playerObj:getX() / 300)
	point1.worldY = math.floor(playerObj:getY() / 300)
	point1.posX = math.floor(playerObj:getX() % 300)
	point1.posY = math.floor(playerObj:getY() % 300)
	point1.posZ = math.floor(playerObj:getZ())

	if allProfessions then
		for _,professionItem in ipairs(self.professionList.items) do
			local points = professionItem.item.points
			for _,point2 in ipairs(points) do
				if self:isSamePoint(pointCopy, point2) then
					point2.worldX = point1.worldX
					point2.worldY = point1.worldY
					point2.posX = point1.posX
					point2.posY = point1.posY
					point2.posZ = point1.posZ
				end
			end
		end
		return
	end

	local point = item.item.point
	point.worldX = point1.worldX
	point.worldY = point1.worldY
	point.posX = point1.posX
	point.posY = point1.posY
	point.posZ = point1.posZ
end

function ISSpawnPointsEditor:onRemovePoint(allProfessions)
	local item = self.pointList.items[self.pointList.selected]
	if item == nil then return end
	local point = item.item.point
	if allProfessions then
		for _,professionItem in ipairs(self.professionList.items) do
			local points = professionItem.item.points
			local newPoints = {}
			for i,point2 in ipairs(points) do
				if not self:isSamePoint(point, point2) then
					table.insert(newPoints, point2)
				end
			end
			professionItem.item.points = newPoints
		end
	else
		local professionItem = self.professionList.items[self.professionList.selected]
		local points = professionItem.item.points
		local newPoints = {}
		for i,point2 in ipairs(points) do
			if not self:isSamePoint(point, point2) then
				table.insert(newPoints, point2)
			end
		end
		professionItem.item.points = newPoints
	end
	self:onProfessionSelected()
end

function ISSpawnPointsEditor:onCopyToClipboard()
	local text = "function SpawnPoints()\n"
	text = text .. "return {\n"
	local sorted = {}
	for i,item in ipairs(self.professionList.items) do
		table.insert(sorted, item)
	end
	table.sort(sorted, function(a,b) return not string.sort(a.text, b.text) end)
	for i,professionItem in ipairs(sorted) do
		local profession = professionItem.item.profession
		local points = professionItem.item.points
		text = text .. "  " .. profession .. " = {\n"
		for j,point in ipairs(points) do
			text = text .. string.format("    { worldX = %d, worldY = %d, posX = %d, posY = %d, posZ = %d }",
				point.worldX, point.worldY, point.posX, point.posY, point.posZ)
			if j == #points then
				text = text .. "\n"
			else
				text = text .. ",\n"
			end
		end
		if i == self.professionList:size() then
			text = text .. "  }\n"
		else
			text = text .. "  },\n"
		end
	end
	text = text .. "}\nend"
	Clipboard.setClipboard(text)
end

function ISSpawnPointsEditor:close()
	self:removeFromUIManager()
end

function ISSpawnPointsEditor:new()
	local o = ISCollapsableWindow.new(self, 100, 100, 550, 400)
	o.title = "Spawn Points"
	return o
end
