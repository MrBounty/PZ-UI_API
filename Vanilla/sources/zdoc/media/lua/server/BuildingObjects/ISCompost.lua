--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

---@class ISCompost : ISBuildingObject
ISCompost = ISBuildingObject:derive("ISCompost");

--************************************************************************--
--** ISCompost:new
--**
--************************************************************************--
function ISCompost:create(x, y, z, north, sprite)
	local cell = getWorld():getCell();
	self.sq = cell:getGridSquare(x, y, z);
	self.javaObject = IsoCompost.new(cell, self.sq);
	buildUtil.consumeMaterial(self);
	self.sq:AddSpecialObject(self.javaObject)
	self.javaObject:syncCompost()
end

function ISCompost:new(name, sprite)
	local o = {};
	setmetatable(o, self);
	self.__index = self;
	o:init();
	o:setSprite(sprite);
	o:setNorthSprite(sprite);
	o.name = name;
	o.canBarricade = false;
	o.dismantable = true;
	o.blockAllTheSquare = true;
	o.canBeAlwaysPlaced = true;
	return o;
end

-- return the health of the new container, it's 100 + 100 per carpentry lvl
function ISCompost:getHealth()
	return 100 + buildUtil.getWoodHealth(self);
end

function ISCompost:isValid(square)
	if not ISBuildingObject.isValid(self, square) then return false; end
	if self.needToBeAgainstWall then
        for i=0,square:getObjects():size()-1 do
           local obj = square:getObjects():get(i);
           if (self.north and obj:getProperties():Is("WallN")) or (not self.north and obj:getProperties():Is("WallW")) then
               return true;
           end
        end
        return false;
    else
        if buildUtil.stairIsBlockingPlacement( square, true ) then return false; end
    end
    return true;
end

function ISCompost:render(x, y, z, square)
	ISBuildingObject.render(self, x, y, z, square)
end
