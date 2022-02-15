--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "BuildingObjects/ISBuildingObject"
require "FireFighting/FireFighting"

ISExtinguishCursor = ISBuildingObject:derive("ISExtinguishCursor");

function ISExtinguishCursor:create(x, y, z, north, sprite)
	local playerObj = self.character
	if not FireFighting.isExtinguisher(self.extinguisher) or not playerObj:getInventory():contains(self.extinguisher) then
		self.extinguisher = FireFighting.getExtinguisher(playerObj)
		if not self.extinguisher then return end
	end
	x,y,z = self:getTopLeftOfSquares(x, y, z)
	local squares = self:getSquares(x, y, z)
	ISWorldObjectContextMenu.equip2(playerObj, playerObj:getPrimaryHandItem(), self.extinguisher, true);
	local usesPerSquare = FireFighting.getExtinguisherUses(self.extinguisher)
	ISTimedActionQueue.add(ISPutOutFire:new(playerObj, squares, self.extinguisher, usesPerSquare, 50));
end

function ISExtinguishCursor:walkTo(x, y, z)
	local playerObj = self.character
	x,y,z = self:getTopLeftOfSquares(x, y, z)
	local squares = self:getSquares(x, y, z)
	if self.character:getJoypadBind() == -1 then
		local closestSq = self:getClosestSquare(squares)
		if playerObj:getCurrentSquare() == closestSq then
			return true
		end
		local adjacent = AdjacentFreeTileFinder.Find(closestSq, self.character)
		if not adjacent then return false end
		ISTimedActionQueue.add(ISWalkToTimedAction:new(playerObj, adjacent))
	end
	return true
end

function ISExtinguishCursor:isValid(square)
	local x,y,z = self:getTopLeftOfSquares(square:getX(), square:getY(), square:getZ())
	return self:isValidArea(x, y, z)
end

function ISExtinguishCursor:isValidArea(x, y, z)
	-- If the current item was used up, find another
	if not FireFighting.isExtinguisher(self.extinguisher) or not self.character:getInventory():contains(self.extinguisher) then
		self.extinguisher = FireFighting.getExtinguisher(self.character)
		if not self.extinguisher then return false end
	end
	local squares = self:getSquares(x, y, z)
	for _,square2 in ipairs(squares) do
		if square2:isCouldSee(self.character:getPlayerNum()) then
			if FireFighting.isSquareToExtinguish(square2) then
				return true
			end
		end
	end
	return false
end

function ISExtinguishCursor:isRunningAction()
    local actionQueue = ISTimedActionQueue.getTimedActionQueue(self.character);
    return actionQueue and actionQueue.queue and actionQueue.queue[1]
end

function ISExtinguishCursor:getTopLeftOfSquares(x, y, z)
	if self.character:getJoypadBind() ~= -1 then
		local cx,cy = math.floor(self.character:getX()), math.floor(self.character:getY())
		if self.character:isOnFire() then
			return cx,cy,z
		end
		local dir = self.character:getDir()
		if     dir == IsoDirections.N  then   x,y = cx,   cy-2
		elseif dir == IsoDirections.NE then   x,y = cx+1, cy-2
		elseif dir == IsoDirections.E  then   x,y = cx+1, cy
		elseif dir == IsoDirections.SE then   x,y = cx+1, cy+1
		elseif dir == IsoDirections.S  then   x,y = cx,   cy+1
		elseif dir == IsoDirections.SW then   x,y = cx-2, cy+1
		elseif dir == IsoDirections.W  then   x,y = cx-2, cy
		elseif dir == IsoDirections.NW then   x,y = cx-2, cy-2
		end
	end
	return x,y,z
end

function ISExtinguishCursor:render(x, y, z, square)
	if self:isRunningAction() then return end
	x,y,z = self:getTopLeftOfSquares(x, y, z)
	local bValid = self:isValidArea(x, y, z)
	if bValid then
		renderIsoCircle(x + 1, y + 1, z, 1, 0, 1, 0, 0.5, 1)
	else
		renderIsoCircle(x + 1, y + 1, z, 1, 1, 0, 0, 0.5, 1)
	end
	--[[
	if not self.floorSprite then
		self.floorSprite = IsoSprite.new()
		self.floorSprite:LoadFramesNoDirPageSimple('media/ui/FloorTileCursor.png')
	end
	--]]
	local squares = self:getSquares(x, y, z)
	for _,square2 in ipairs(squares) do
		if not bValid or not square2:isCouldSee(self.character:getPlayerNum()) then
--			self.floorSprite:RenderGhostTileRed(square2:getX(), square2:getY(), square2:getZ())
		else
--			self.floorSprite:RenderGhostTileColor(square2:getX(), square2:getY(), square2:getZ(), 0, 1, 0, 0.8)
			local objects = self:getFireObjects(square2)
			for _,object in ipairs(objects) do
				if not ISExtinguishCursor._colorInfo then
					ISExtinguishCursor._colorInfo = ColorInfo.new(1.0, 0.0, 0.0, 1.0)
				end
				object:setHighlighted(true)
				object:setHighlightColor(ISExtinguishCursor._colorInfo)
			--[[
				for j=1,object:getAttachedAnimSprite():size() do
					local spriteInst = object:getAttachedAnimSprite():get(j-1)
					if spriteInst then
						spriteInst:RenderGhostTileColor(square2:getX(), square2:getY(), square2:getZ(), 1.0, 0.0, 0.0, 1.0)
					end
				end
			--]]
			end
		end
	end
	if self.character:getJoypadBind() ~= -1 then return end
	if #squares == 0 then return end
	local closestSq = self:getClosestSquare(squares)
	local playerSq = self.character:getCurrentSquare()
	if playerSq == closestSq then
		renderIsoCircle(playerSq:getX() + 0.5, playerSq:getY() + 0.5, playerSq:getZ(), 0.5, 1, 1, 1, 0.8, 1)
--		self.floorSprite:RenderGhostTileColor(playerSq:getX(), playerSq:getY(), playerSq:getZ(), 1, 1, 1, 0.8)
	else
		local adjacent = AdjacentFreeTileFinder.Find(closestSq, self.character)
		if adjacent then
			renderIsoCircle(adjacent:getX() + 0.5, adjacent:getY() + 0.5, adjacent:getZ(), 0.5, 1, 1, 1, 0.8, 1)
--			self.floorSprite:RenderGhostTileColor(adjacent:getX(), adjacent:getY(), adjacent:getZ(), 1, 1, 1, 0.8)
		end
	end
end

function ISExtinguishCursor:onJoypadPressButton(joypadIndex, joypadData, button)
	if button == Joypad.AButton or button == Joypad.BButton then
		return ISBuildingObject.onJoypadPressButton(self, joypadIndex, joypadData, button)
	end
end

function ISExtinguishCursor:getAPrompt()
	return self.canBeBuild and getText("ContextMenu_ExtinguishFire") or nil
end

function ISExtinguishCursor:getLBPrompt()
	return nil
end

function ISExtinguishCursor:getRBPrompt()
	return nil
end

-- If you want to decrease/increase the area affected, do it here.
function ISExtinguishCursor:getSquares(x, y, z)
	local squares = {}
	local square = getCell():getGridSquare(x, y, z)
	--[[
	if square then
		table.insert(squares, square)
	end
	for dy = -1,1 do
		for dx = -1,1 do
			if dx ~= 0 or dy ~= 0 then
				local square = getCell():getGridSquare(x+dx, y+dy, z)
				if square then
					table.insert(squares, square)
				end
			end
		end
	end
	--]]
	local square = getCell():getGridSquare(x, y, z)
	if square then
		table.insert(squares, square)
	end
	square = getCell():getGridSquare(x + 1, y, z)
	if square then
		table.insert(squares, square)
	end
	square = getCell():getGridSquare(x, y + 1, z)
	if square then
		table.insert(squares, square)
	end
	square = getCell():getGridSquare(x + 1, y + 1, z)
	if square then
		table.insert(squares, square)
	end
	return squares
end

function ISExtinguishCursor:getClosestSquare(squares)
	local closest = nil
	local closestDist = 1000000
	for _,square2 in ipairs(squares) do
		local dist = IsoUtils.DistanceTo(self.character:getX(), self.character:getY(), square2:getX() + 0.5, square2:getY() + 0.5)
		if dist < closestDist then
			closest = square2
			closestDist = dist
		end
	end
	return closest
end

function ISExtinguishCursor:getFireObjects(square)
	local objects = {}
	for i=1,square:getObjects():size() do
		local object = square:getObjects():get(i-1)
		if instanceof(object, "IsoFire") and not object:isPermanent() then
			table.insert(objects, object)
		end
	end
--[[
	-- Would like to higlight burning characters, but:
	-- 1) Need proper positioning of the attached fire sprite
	-- 2) Can't render character 'sprite' because it might be a model or
	--    multiple sprites
	for i=1,square:getMovingObjects():size() do
		local chr = square:getMovingObjects():get(i-1)
		if instanceof(chr, "IsoGameCharacter") and chr:isOnFire() then
			table.insert(objects, chr)
		end
	end
--]]
	return objects
end

function ISExtinguishCursor:new(character, extinguisher)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o:init()
	o.character = character
	o.player = character:getPlayerNum()
	o.skipBuildAction = true
	o.noNeedHammer = true
	o.skipWalk = true
	o.renderFloorHelper = true
--	o.dragNilAfterPlace = true
	o.extinguisher = extinguisher
	return o
end

