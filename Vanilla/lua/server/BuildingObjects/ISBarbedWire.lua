--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

ISBarbedWire = ISBuildingObject:derive("ISBarbedWire")

function ISBarbedWire:create(x, y, z, north, sprite)
	local cell = getWorld():getCell()
	self.sq = cell:getGridSquare(x, y, z)
	self.javaObject = IsoThumpable.new(cell, self.sq, sprite, north, self)
	buildUtil.setInfo(self.javaObject, self)
	buildUtil.consumeMaterial(self)
	self.javaObject:setMaxHealth(100)
	self.javaObject:setHealth(self.javaObject:getMaxHealth())
	self.javaObject:setName(self.name)
	self.javaObject:setBreakSound("BreakObject")
	self.sq:AddSpecialObject(self.javaObject, self:getObjectIndex(self.sq))
	self.sq:RecalcAllWithNeighbours(true)
	self.javaObject:transmitCompleteItemToServer()
	if self.sq:getZone() then
		self.sq:getZone():setHaveConstruction(true)
	end
end

function ISBarbedWire:onTimedActionStart(action)
	ISBuildingObject.onTimedActionStart(self, action)
end

function ISBarbedWire:new(sprite, northSprite)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o:init()
	o:setSprite(sprite)
	o:setNorthSprite(northSprite)
	o.hoppable = true
	o.canBarricade = false
	o.name = "Barbed Fence"
	o.isWallLike = true
	return o
end

function ISBarbedWire:isValid(square)
	if not self:haveMaterial(square) then return false end
	if self:getBarbedWire(square) ~= -1 then return false end
	if self:getWoodenStake(square) == -1 then
		local north = self.nSprite==2 or self.nSprite==4
		local adjacent1 = square:getAdjacentSquare(north and IsoDirections.W or IsoDirections.N)
		local adjacent2 = square:getAdjacentSquare(north and IsoDirections.E or IsoDirections.S)
		if self:getWoodenStake(adjacent2) == -1 and self:getBarbedWire(adjacent1) == -1 and self:getBarbedWire(adjacent2) == -1 then
			return false
		end
	end
	return true
end

function ISBarbedWire:render(x, y, z, square)
	ISBuildingObject.render(self, x, y, z, square)
end

function ISBarbedWire:getBarbedWire(square)
	if not square then return -1 end
	for i=1,square:getObjects():size() do
		local object = square:getObjects():get(i-1)
		if instanceof(object, "IsoThumpable") and (object:getName() == "Barbed Fence") and object:getNorth() == self.north then
			return i - 1
		end
	end
	return -1
end

function ISBarbedWire:getWoodenStake(square)
	if not square then return -1 end
	for i=1,square:getObjects():size() do
		local object = square:getObjects():get(i-1)
		if instanceof(object, "IsoThumpable") and (object:getName() == "Wooden Stake") then
			return i - 1
		end
	end
	return -1
end

function ISBarbedWire:getObjectIndex(square)
	local index = self:getWoodenStake(square)
	if index ~= -1 then
		return index + 1
	end
	return -1
end	
