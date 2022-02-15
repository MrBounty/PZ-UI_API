--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

---@class ISWoodenContainer : ISBuildingObject
ISWoodenContainer = ISBuildingObject:derive("ISWoodenContainer");

--************************************************************************--
--** ISWoodenContainer:new
--**
--************************************************************************--
function ISWoodenContainer:create(x, y, z, north, sprite)
	local cell = getWorld():getCell();
	self.sq = cell:getGridSquare(x, y, z);
	self.javaObject = IsoThumpable.new(cell, self.sq, sprite, north, self);
	buildUtil.setInfo(self.javaObject, self);
	buildUtil.consumeMaterial(self);
	-- the wooden wall have 200 base health + 100 per carpentry lvl
	self.javaObject:setMaxHealth(self:getHealth());
	self.javaObject:setHealth(self.javaObject:getMaxHealth());
	-- the sound that will be played when our door frame will be broken
	self.javaObject:setBreakSound("BreakObject");

	local sharedSprite = getSprite(self:getSprite())
	if self.sq and sharedSprite and sharedSprite:getProperties():Is("IsStackable") then
		local props = ISMoveableSpriteProps.new(sharedSprite)
		self.javaObject:setRenderYOffset(props:getTotalTableHeight(self.sq))
	end

	-- add the item to the ground
    self.sq:AddSpecialObject(self.javaObject);
	self.javaObject:transmitCompleteItemToServer();
end

function ISWoodenContainer:new(sprite, northSprite)
	local o = {};
	setmetatable(o, self);
	self.__index = self;
	o:init();
	o:setSprite(sprite);
	o:setNorthSprite(northSprite);
	o.isContainer = true;
	o.blockAllTheSquare = true;
	o.name = "Wooden Crate";
	o.dismantable = true;
	o.canBeAlwaysPlaced = true;
    o.canBeLockedByPadlock = true;
	o.buildLow = true;
	return o;
end

-- return the health of the new container, it's 200 + 100 per carpentry lvl
function ISWoodenContainer:getHealth()
	return 200 + buildUtil.getWoodHealth(self);
end

function ISWoodenContainer:isValid(square)
    if buildUtil.stairIsBlockingPlacement( square, true ) then return false; end
	if not self:haveMaterial(square) then return false end
	local sharedSprite = getSprite(self:getSprite())
	if square and sharedSprite and sharedSprite:getProperties():Is("IsStackable") then
		local props = ISMoveableSpriteProps.new(sharedSprite)
		return props:canPlaceMoveable("bogus", square, nil)
	end
	return ISBuildingObject.isValid(self, square);
end

function ISWoodenContainer:render(x, y, z, square)
	ISBuildingObject.render(self, x, y, z, square)
end
