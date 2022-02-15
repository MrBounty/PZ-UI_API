--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

---@class ISLightSource : ISBuildingObject
ISLightSource = ISBuildingObject:derive("ISLightSource");

--************************************************************************--
--** ISLightSource:new
--**
--************************************************************************--
function ISLightSource:create(x, y, z, north, sprite)
	local cell = getWorld():getCell();
	self.sq = cell:getGridSquare(x, y, z);
	self.javaObject = IsoThumpable.new(cell, self.sq, sprite, north, self);
	buildUtil.setInfo(self.javaObject, self);

    -- light stuff
    local offsetX = 0;
    local offsetY = 0;
    if self.east then
        offsetX = self.offsetX;
    elseif self.west then
        offsetX = -self.offsetX;
    elseif self.south then
        offsetY = self.offsetY;
    elseif self.north then
        offsetY = -self.offsetY;
    end
    local baseItem = self.character:getInventory():getFirstTypeRecurse(self.baseItem)
    if not baseItem then
        local itemsOnGround = buildUtil.getMaterialOnGround(self.sq)
        baseItem = itemsOnGround[self.baseItem] and itemsOnGround[self.baseItem][1] or nil
    end
    self.javaObject:createLightSource(self.radius, offsetX, offsetY, 0, 0, self.fuel, baseItem, self.character);
--    self.javaObject:setLifeDelta(0.000009);

    -- set the remaining life to the light source
--    self.javaObject:getLightSource():insertNewFuel(getPlayer():getIventory():FindAndReturn("Torch"), getPlayer());

	buildUtil.consumeMaterial(self);
	self.javaObject:getModData()["need:"..self.baseItem] = "1"
	-- the wooden wall have 100 base health + 100 per carpentry lvl
	self.javaObject:setMaxHealth(self:getHealth());
	self.javaObject:setHealth(self.javaObject:getMaxHealth());
	-- the sound that will be played when our door frame will be broken
	self.javaObject:setBreakSound("BreakObject");
	-- add the item to the ground
    self.sq:AddSpecialObject(self.javaObject);
	self.javaObject:transmitCompleteItemToServer()
end

function ISLightSource:new(sprite, northSprite, player)
	local o = {};
	setmetatable(o, self);
	self.__index = self;
	o:init();
	o:setSprite(sprite);
	o:setNorthSprite(northSprite);
	o.canBarricade = false;
	o.dismantable = true;
    o.character = player;
    o.name = "Lamp on Pillar"
    o.blockAllTheSquare = true;
	return o;
end

-- return the health of the new container, it's 100 + 100 per carpentry lvl
function ISLightSource:getHealth()
	return 100 + buildUtil.getWoodHealth(self);
end

function ISLightSource:isValid(square)
	if not self:haveMaterial(square) then return false end
    if buildUtil.stairIsBlockingPlacement( square, true ) then return false; end
	if square:isVehicleIntersecting() then return false end
	return square:isFree(true)
--	return ISBuildingObject.isValid(self, square, north);
end

function ISLightSource:render(x, y, z, square)
	ISBuildingObject.render(self, x, y, z, square)
end
