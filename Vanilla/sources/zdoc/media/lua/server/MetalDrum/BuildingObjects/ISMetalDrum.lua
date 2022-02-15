--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "BuildingObjects/ISBuildingObject"

-- this class extend ISBuildingObject, it's a class to help you drag around/place an item in the world
---@class ISMetalDrum : ISBuildingObject
ISMetalDrum = ISBuildingObject:derive("ISMetalDrum");
ISMetalDrum.waterScale = 4
ISMetalDrum.waterMax = 200 * ISMetalDrum.waterScale

function ISMetalDrum:create(x, y, z, north, sprite)
    local cell = getWorld():getCell();
    self.sq = cell:getGridSquare(x, y, z);
    self.javaObject = IsoThumpable.new(cell, self.sq, sprite, north, self);
    buildUtil.setInfo(self.javaObject, self);
    buildUtil.consumeMaterial(self);
    self.javaObject:setMaxHealth(200);
    self.javaObject:setBreakSound("BreakObject");
    self.sq:AddSpecialObject(self.javaObject);
    self.javaObject:getModData()["waterMax"] = self.waterMax;
    self.javaObject:getModData()["waterAmount"] = 0;
    self.javaObject:setSpecialTooltip(true)
    local barrel = {};
    barrel.x = self.sq:getX();
    barrel.y = self.sq:getY();
    barrel.z = self.sq:getZ();
    barrel.waterAmount = 0;
    barrel.waterMax = ISMetalDrum.waterMax;
    barrel.exterior = self.sq:isOutside()
    self.javaObject:transmitCompleteItemToServer();
    -- OnObjectAdded event will create the SRainBarrelGlobalObject on the server.
    -- This is only needed for singleplayer which doesn't trigger OnObjectAdded.
    triggerEvent("OnObjectAdded", self.javaObject)
end

function ISMetalDrum:new(player, sprite)
    local o = {};
    setmetatable(o, self);
    self.__index = self;
    o:init();
    o:setSprite(sprite);
    o:setNorthSprite(sprite);
    o.noNeedHammer = true;
    o.name = "MetalDrum";
    o.player = player;
    o.dismantable = true;
    return o;
end

-- our barrel can be placed on this square ?
-- this function is called everytime you move the mouse over a grid square, you can for example not allow building inside house..
function ISMetalDrum:isValid(square)
    if not square then return false end
    if square:isSolid() or square:isSolidTrans() then return false end
    if square:HasStairs() then return false end
    if square:HasTree() then return false end
    if not square:getMovingObjects():isEmpty() then return false end
    if not square:TreatAsSolidFloor() then return false end
    if not self:haveMaterial(square) then return false end
    for i=1,square:getObjects():size() do
        local obj = square:getObjects():get(i-1)
        if self:getSprite() == obj:getTextureName() then return false end
    end
    if buildUtil.stairIsBlockingPlacement( square, true ) then return false; end
    return true
end

-- called after render the ghost objects
-- the ISBuildingObject only render 1 sprite (north, south...), for example for stairs I can render the 2 others tile for stairs here
-- if I return false, the ghost render will be in red and I couldn't build the item
function ISMetalDrum:render(x, y, z, square)
    ISBuildingObject.render(self, x, y, z, square)
end

