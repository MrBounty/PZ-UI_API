--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "BuildingObjects/ISBuildingObject"

ISDestroyCursor = ISBuildingObject:derive("ISDestroyCursor");

function ISDestroyCursor:create(x, y, z, north, sprite)
	local sq = getWorld():getCell():getGridSquare(x, y, z)
	local player = self.character
	local destroy = self:getObjectList()[self.objectIndex]
	if self.dismantle then
		ISTimedActionQueue.add(ISDismantleAction:new(player, destroy))
	else
		ISWorldObjectContextMenu.equip(player, player:getPrimaryHandItem(), self.sledgehammer:getType(), true)
		ISTimedActionQueue.add(ISDestroyStuffAction:new(player, destroy))
	end
end

function ISDestroyCursor:walkTo(x, y, z)
	local square = getCell():getGridSquare(x, y, z)
	local playerObj = self.character
	local destroy = self:getObjectList()[self.objectIndex]
	if instanceof(destroy, "IsoWindow") or instanceof(destroy, "IsoDoor") or instanceof(destroy, "IsoBarricade") or
			self:_isDoorFrame(destroy) or self:_isWall(destroy) then
		return luautils.walkAdjWindowOrDoor(playerObj, square, destroy, true)
	end
	return luautils.walkAdj(playerObj, square, true)
end

function ISDestroyCursor:_isWall(object)
	if object and object:getProperties() then
		return object:getProperties():Is(IsoFlagType.cutW) or object:getProperties():Is(IsoFlagType.cutN)
	end
	return false
end

function ISDestroyCursor:_isDoorFrame(object)
	return object and (object:getType() == IsoObjectType.doorFrW or object:getType() == IsoObjectType.doorFrN)
end

function ISDestroyCursor:_isDoorN(object)
	if object and instanceof(object, "IsoDoor") and object:getNorth() then return true end
	if object and instanceof(object, "IsoThumpable") and object:isDoor() and object:getNorth() then return true end
	return false
end

function ISDestroyCursor:_isDoorW(object)
	if object and instanceof(object, "IsoDoor") and not object:getNorth() then return true end
	if object and instanceof(object, "IsoThumpable") and object:isDoor() and not object:getNorth() then return true end
	return false
end

function ISDestroyCursor:_isDoorWallN(object)
	return object and object:getProperties() and object:getProperties():Is("DoorWallN")
end

function ISDestroyCursor:_isDoorWallW(object)
	return object and object:getProperties() and object:getProperties():Is("DoorWallW")
end

function ISDestroyCursor:rotateKey(key)
	if key == getCore():getKey("Rotate building") then
		self.objectIndex = self.objectIndex + 1
		local objects = self:getObjectList()
		if self.objectIndex > #objects then
			self.objectIndex = 1
		end
	end
end

function ISDestroyCursor:isValid(square)
	self.renderX = square:getX()
	self.renderY = square:getY()
	self.renderZ = square:getZ()
	return #self:getObjectList() > 0
end

function ISDestroyCursor:render(x, y, z, square)
	if not self.floorSprite then
		self.floorSprite = IsoSprite.new()
		self.floorSprite:LoadFramesNoDirPageSimple('media/ui/FloorTileCursor.png')
	end

	self.currentObject = nil
	if not self:isValid(square) then
		self.floorSprite:RenderGhostTileRed(x, y, z)
		return
	end
	self.floorSprite:RenderGhostTileColor(x, y, z, 0.0, 1.0, 0.0, 0.8)

	if self.currentSquare ~= square then
		self.objectIndex = 1
		self.currentSquare = square
	end

	self.renderX = x
	self.renderY = y
	self.renderZ = z

	local objects = self:getObjectList()
	if self.objectIndex > #objects then self.objectIndex = #objects end
	if self.objectIndex >= 1 and self.objectIndex <= #objects then
		local object = objects[self.objectIndex]
		local color = {r=1,g=0,b=0}
		if object:getProperties():Is(IsoFlagType.solidfloor) then
			color = {r=0,g=1,b=0}
		end
		local stairObjects = buildUtil.getStairObjects(object)
		local doubleDoorObjects = buildUtil.getDoubleDoorObjects(object)
		local garageDoorObjects = buildUtil.getGarageDoorObjects(object)
		local graveObjects = buildUtil.getGraveObjects(object)
		if #stairObjects > 0 then
			for i=1,#stairObjects do
				stairObjects[i]:getSprite():RenderGhostTileColor(stairObjects[i]:getX(), stairObjects[i]:getY(), stairObjects[i]:getZ(), color.r, color.g, color.b, 0.8)
			end
		elseif #doubleDoorObjects > 0 then
			for i=1,#doubleDoorObjects do
				local dd = doubleDoorObjects[i]
				dd:getSprite():RenderGhostTileColor(dd:getX(), dd:getY(), dd:getZ(), color.r, color.g, color.b, 0.8)
			end
		elseif #garageDoorObjects > 0 then
			for i=1,#garageDoorObjects do
				local dd = garageDoorObjects[i]
				dd:getSprite():RenderGhostTileColor(dd:getX(), dd:getY(), dd:getZ(), color.r, color.g, color.b, 0.8)
			end
		elseif #graveObjects > 0 then
			for i=1,#graveObjects do
				local dd = graveObjects[i]
				dd:getSprite():RenderGhostTileColor(dd:getX(), dd:getY(), dd:getZ(), color.r, color.g, color.b, 0.8)
			end
		else
			local offsetX,offsetY = 0,(object:getRenderYOffset() * Core.getTileScale())
			object:getSprite():RenderGhostTileColor(x, y, z, offsetX, offsetY, color.r, color.g, color.b, 0.8)
		end
		self.currentObject = object
	end
end

function ISDestroyCursor:onJoypadPressButton(joypadIndex, joypadData, button)
	local playerObj = getSpecificPlayer(joypadData.player)

	if button == Joypad.AButton or button == Joypad.BButton then
		return ISBuildingObject.onJoypadPressButton(self, joypadIndex, joypadData, button)
	end

	if button == Joypad.RBumper then
		self.objectIndex = self.objectIndex + 1
		local objects = self:getObjectList()
		if self.objectIndex > #objects then
			self.objectIndex = 1
		end
	end

	if button == Joypad.LBumper then
		self.objectIndex = self.objectIndex - 1
		if self.objectIndex < 1 then
			local objects = self:getObjectList()
			self.objectIndex = #objects
		end
	end
end

function ISDestroyCursor:getAPrompt()
	if self.currentObject then
		if self.currentObject:getProperties():Is(IsoFlagType.solidfloor) then
			return getText("ContextMenu_DestroyFloor")
		end
		return getText("ContextMenu_Destroy")
	end
	return nil
end

function ISDestroyCursor:getLBPrompt()
	if #self:getObjectList() > 1 then
		return "Previous Object"
	end
	return nil
end

function ISDestroyCursor:getRBPrompt()
	if #self:getObjectList() > 1 then
		return "Next Object"
	end
	return nil
end

function ISDestroyCursor:couldSeeOpposite(object, square)
	if object:getProperties():Is(IsoFlagType.cutN) or object:getProperties():Is(IsoFlagType.collideN) or self:_isDoorN(object) or
			(object:getType() == IsoObjectType.doorFrN) then
		local sq = getCell():getGridSquare(square:getX(), square:getY() - 1, square:getZ())
		return sq and sq:isCouldSee(self.player)
	end
	if object:getProperties():Is(IsoFlagType.cutW) or object:getProperties():Is(IsoFlagType.collideW) or self:_isDoorW(object) or
			(object:getType() == IsoObjectType.doorFrW) then
		local sq = getCell():getGridSquare(square:getX() - 1, square:getY(), square:getZ())
		return sq and sq:isCouldSee(self.player)
	end
	return false
end

function ISDestroyCursor:canDestroy(object)
	-- No destroying door-wall that has a door.
	if self:_isDoorWallN(object) or self:_isDoorWallW(object) then
		local isNorth = self:_isDoorWallN(object)
		local objects = object:getSquare():getObjects()
		for i=1,objects:size() do
			local object2 = objects:get(i-1)
			if isNorth and self:_isDoorN(object2) and object ~= object2 then return false end
			if (not isNorth) and self:_isDoorW(object2) and object ~= object2 then return false end
		end
	end
	if self.dismantle then
		return object and instanceof(object, "IsoThumpable") and object:isDismantable()
	end
	if not object or not object:getSquare() or not object:getSprite() then return false end
	if instanceof(object, "IsoWorldInventoryObject") then return false end
	if instanceof(object, "IsoTree") then return false end
	local square = object:getSquare()
	local props = object:getProperties()
	if not props then return false end

	-- No sledgehammering the daffodils.
	if props:Is(IsoFlagType.vegitation) then return false end

	-- Sheetropes
	if props:Is(IsoFlagType.climbSheetTopW) or props:Is(IsoFlagType.climbSheetTopE) or
			props:Is(IsoFlagType.climbSheetTopN) or props:Is(IsoFlagType.climbSheetTopS) or
			props:Is(IsoFlagType.climbSheetW) or props:Is(IsoFlagType.climbSheetE) or
			props:Is(IsoFlagType.climbSheetN) or props:Is(IsoFlagType.climbSheetS) then
		return false
	end

	-- No destroying the floor tile at the top of a staircase.
	-- The floor tile isn't visible when climbing the stairs.
	-- This is to stop mp griefers.
	if object:getZ() > 0 and self:isFloorAtTopOfStairs(object) then return false end

	local spriteName = object:getSprite():getName()
	if spriteName then
		-- advertising billboard base
		if spriteName == "advertising_01_14" then return false end
		-- 2-story street-light base tile
		if spriteName == "lighting_outdoor_01_16" then return false end
		if spriteName == "lighting_outdoor_01_17" then return false end
		-- Don't destroy water tiles.
		if luautils.stringStarts(spriteName, 'blends_natural_02') then return false end
		-- FIXME: these tiles should have 'vegitation' flag.
		if luautils.stringStarts(spriteName, 'blends_grassoverlays') then return false end
		if luautils.stringStarts(spriteName, 'd_') then return false end
		if luautils.stringStarts(spriteName, 'e_') then return false end
		if luautils.stringStarts(spriteName, 'f_') then return false end
		if luautils.stringStarts(spriteName, 'vegetation_') and not luautils.stringStarts(spriteName, 'vegetation_indoor') then return false end

		if luautils.stringStarts(spriteName, 'trash_01') then return false end
		if luautils.stringStarts(spriteName, 'street_curbs') then return false end
	end

	-- Destroy doors, walls and windows from the opposite square too
	if not square:isCouldSee(self.player) then
		if self:couldSeeOpposite(object, square) then return true end
		return false
	end

	if props:Is(IsoFlagType.solidfloor) then return object:getZ() > 0 end
	return true
end

function ISDestroyCursor:isFloorAtTopOfStairs(object)
	local props = object:getProperties()
	if not props:Is(IsoFlagType.solidfloor) then return false end
	local square = getCell():getGridSquare(object:getX() + 1, object:getY(), object:getZ() - 1)
	if square and square:Has(IsoObjectType.stairsTW) then return true end
	square = getCell():getGridSquare(object:getX(), object:getY() + 1, object:getZ() - 1)
	if square and square:Has(IsoObjectType.stairsTN) then return true end
	return false
end

function ISDestroyCursor:getObjectList()
	local square = getCell():getGridSquare(self.renderX, self.renderY, self.renderZ)
	if not square then return {} end
	local objects = {}
	for i = square:getObjects():size(),1,-1 do
		local destroy = square:getObjects():get(i-1)
		if self:canDestroy(destroy) then
			table.insert(objects, destroy)
		end
	end
	return objects
end

function ISDestroyCursor:new(character, dismantle, sledgehammer)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o:init()
--	o:setSprite("tileThuztorFarming1_plowedLand")
	o.character = character
	o.player = character:getPlayerNum()
	o.skipBuildAction = true
	o.noNeedHammer = false
	o.skipWalk = true
	o.renderFloorHelper = true
--	o.dragNilAfterPlace = true
	o.dismantle = dismantle
	o.sledgehammer = sledgehammer;
	o.objectIndex = 1
	o.renderX = -1
	o.renderY = -1
	o.renderZ = -1
	return o
end

