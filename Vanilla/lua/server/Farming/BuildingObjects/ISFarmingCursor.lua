--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "BuildingObjects/ISBuildingObject"

ISFarmingCursor = ISBuildingObject:derive("ISFarmingCursor");

function ISFarmingCursor:create(x, y, z, north, sprite)
	local sq = getWorld():getCell():getGridSquare(x, y, z)
	local playerObj = self.character
	local object = self:getObjectList()[self.objectIndex]

	local x = getPlayerScreenLeft(self.player)
	local y = getPlayerScreenTop(self.player)
	local w = getPlayerScreenWidth(self.player)
	local h = getPlayerScreenHeight(self.player)
	x = x + w / 2
	y = y + h / 2
	if getNumActivePlayers() > 1 then
		x = x - w / 4
		y = y - w / 4
	end

	local context = ISContextMenu.get(self.player, x, y);
	local worldobjects = { sq:getObjects():get(0) }
	ISFarmingMenu.doFarmingMenu2(self.player, context, worldobjects, false)
	if not context:getIsVisible() then
		context:setVisible(true)
	end
	JoypadState.players[self.player+1].focus = context
	context.mouseOver = 1
end

function ISFarmingCursor:rotateKey(key)
end

function ISFarmingCursor:isValid(square)
	self.renderX = square:getX()
	self.renderY = square:getY()
	self.renderZ = square:getZ()
	return #self:getObjectList() > 0
end

function ISFarmingCursor:render(x, y, z, square)
	if not self.floorSprite then
		self.floorSprite = IsoSprite.new()
		self.floorSprite:LoadFramesNoDirPageSimple('media/ui/FloorTileCursor.png')
	end

	if not self:isValid(square) then
		self.floorSprite:RenderGhostTileRed(x, y, z)
		return
	end
	self.floorSprite:RenderGhostTileColor(x, y, z, 0, 1, 0, 0.8)
end

function ISFarmingCursor:onJoypadPressButton(joypadIndex, joypadData, button)
	if button == Joypad.AButton or button == Joypad.BButton then
		return ISBuildingObject.onJoypadPressButton(self, joypadIndex, joypadData, button)
	end
end

function ISFarmingCursor:getAPrompt()
	if #self:getObjectList() > 0 then
		return getText("ContextMenu_Farming")
	end
end

function ISFarmingCursor:getLBPrompt()
	return nil
end

function ISFarmingCursor:getRBPrompt()
	return nil
end

function ISFarmingCursor:getObjectList()
	local square = getCell():getGridSquare(self.renderX, self.renderY, self.renderZ)
	if not square then return {} end
	local objects = {}
	local plant = CFarmingSystem.instance:getLuaObjectOnSquare(square)
	if plant and ISFarmingMenu.doFarmingMenu2(self.player, nil, { square:getObjects():get(0) }, true) then
		table.insert(objects, plant)
	end
	return objects
end

function ISFarmingCursor:new(character)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o:init()
	o.character = character
	o.player = character:getPlayerNum()
	o.skipBuildAction = true
	o.noNeedHammer = false
	o.skipWalk = true
	o.dragNilAfterPlace = true
	o.renderX = -1
	o.renderY = -1
	o.renderZ = -1
	return o
end

