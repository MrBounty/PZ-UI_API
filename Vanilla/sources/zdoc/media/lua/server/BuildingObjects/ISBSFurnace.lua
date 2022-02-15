--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

---@class ISBSFurnace : ISBuildingObject
ISBSFurnace = ISBuildingObject:derive("ISStoneFurnace");

--************************************************************************--
--** ISStoneFurnace:new
--**
--************************************************************************--
function ISBSFurnace:create(x, y, z, north, sprite)
    local cell = getWorld():getCell();
    self.sq = cell:getGridSquare(x, y, z);
    self.javaObject = BSFurnace.new(cell, self.sq, self.sprite, self.litSprite);
    buildUtil.consumeMaterial(self);

    self.javaObject:transmitCompleteItemToServer();
end

function ISBSFurnace:removeFromGround(square)
    for i = 0, square:getSpecialObjects():size() do
        local thump = square:getSpecialObjects():get(i);
        if instanceof(thump, "BSFurnace") then
            square:transmitRemoveItemFromSquare(thump);
            square:getObjects():remove(thump);
            square:getSpecialObjects():remove(thump);
        end
    end
end

function ISBSFurnace:new(name, sprite, litSprite)
    local o = {};
    setmetatable(o, self);
    self.__index = self;
    o:init();
    o:setSprite(sprite);
    o:setNorthSprite(sprite);
    o.litSprite = litSprite;
    o.noNeedHammer = true;
    o.name = name;
    o.blockAllTheSquare = true;
    o.canBeAlwaysPlaced = true;
    return o;
end

function ISBSFurnace:isValid(square)
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

function ISBSFurnace:render(x, y, z, square)
    ISBuildingObject.render(self, x, y, z, square)
end
