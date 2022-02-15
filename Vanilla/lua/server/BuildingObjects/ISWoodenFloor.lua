--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

ISWoodenFloor = ISBuildingObject:derive("ISWoodenFloor");

--************************************************************************--
--** ISWoodenFloor:new
--**
--************************************************************************--
function ISWoodenFloor:create(x, y, z, north, sprite)
	self.sq = getWorld():getCell():getGridSquare(x, y, z);
	self.javaObject = self.sq:addFloor(sprite);
	buildUtil.consumeMaterial(self);
    if self.sq:getZone() then
        self.sq:getZone():setHaveConstruction(true);
    end

    for i=0,self.sq:getObjects():size()-1 do
        local object = self.sq:getObjects():get(i);
        if object:getProperties() and object:getProperties():Is(IsoFlagType.canBeRemoved) then
            self.sq:transmitRemoveItemFromSquare(object)
            self.sq:RemoveTileObject(object);
            break
        end
    end

    self.sq:disableErosion();
    local args = { x = self.sq:getX(), y = self.sq:getY(), z = self.sq:getZ() }
    sendClientCommand(nil, 'erosion', 'disableForSquare', args)
end

function ISWoodenFloor:new(sprite, northSprite)
	local o = {};
	setmetatable(o, self);
	self.__index = self;
	o:init();
	o:setSprite(sprite);
	o:setNorthSprite(northSprite);
	o.buildLow = true;
	o.floor = true;
	return o;
end

function ISWoodenFloor:isValid(square)
	if not self:haveMaterial(square) then return false end
	if square:getZ() > 0 then
		local below = getCell():getGridSquare(square:getX(), square:getY(), square:getZ() - 1)
		if below and below:HasStairs() then
			return false
		end
	end
	for i = 0, square:getObjects():size() - 1 do
		local item = square:getObjects():get(i);
		if (item:getTextureName() and luautils.stringStarts(item:getTextureName(), "vegetation_farming")) or
				(item:getSpriteName() and luautils.stringStarts(item:getSpriteName(), "vegetation_farming")) then
			return false;
		end
	end
	return square:connectedWithFloor();
end

function ISWoodenFloor:render(x, y, z, square)
	ISBuildingObject.render(self, x, y, z, square)
end

