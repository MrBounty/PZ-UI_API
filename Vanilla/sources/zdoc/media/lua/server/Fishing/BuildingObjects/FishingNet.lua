require "BuildingObjects/ISBuildingObject"

--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

---@class fishingNet : ISBuildingObject
fishingNet = ISBuildingObject:derive("fishingNet");

--************************************************************************--
--** fishingNet:new
--**
--************************************************************************--
function fishingNet:create(x, y, z, north, sprite)
    local grid = getCell():getGridSquare(x, y, z);
    local net = IsoObject.new(grid, sprite, "FishingNet");
    grid:AddTileObject(net)
    net:transmitCompleteItemToServer();
    fishingNet.doTimestamp(net);
    local item = self.character:getPrimaryHandItem()
    if item and item:getType() == "FishingNet" then
        self.character:setPrimaryHandItem(nil)
        self.character:getInventory():Remove(item)
    else
        item = self.character:getSecondaryHandItem()
        if item and item:getType() == "FishingNet" then
            self.character:setSecondaryHandItem(nil)
            self.character:getInventory():Remove(item)
        else
            self.character:getInventory():RemoveOneOf("FishingNet");
        end
    end
--    getSoundManager():PlayWorldSound("waterSplash", false, self.character:getSquare(), 1, 20, 1, false)
    self.character:playSound("PlaceFishingNet");
    getCell():setDrag(nil, self.player);
end

function fishingNet:new(player)
    local o = {}
    setmetatable(o, self)
    self.__index = self
    o:init();
    o:setNorthSprite("constructedobjects_01_14");
    o:setSprite("constructedobjects_01_15");
    o.character = player
    o.player = player:getPlayerNum();
    o.skipBuildAction = true;
    o.skipWalk = true;
    return o;
end

function fishingNet:isValid(square, north)
    if not self.character:getInventory():contains("FishingNet") then return false end
    return square:DistToProper(self.character:getCurrentSquare()) < 5 and square:getProperties():Is(IsoFlagType.water);
--    return true;
end

function fishingNet:render(x, y, z, square)
    ISBuildingObject.render(self, x, y, z, square)
end

fishingNet.doTimestamp = function(net)
    net:getSquare():getModData()["fishingNetTS"] = getGameTime():getCalender():getTimeInMillis();
    net:getSquare():transmitModdata();
end

fishingNet.remove = function(net, player)
    net:getSquare():transmitRemoveItemFromSquare(net);
    player:getInventory():AddItem("Base.FishingNet");
--    getSoundManager():PlayWorldSound("getFish", false, player:getSquare(), 1, 20, 1, false)
    player:playSound("RemoveFishingNet");
end

fishingNet.checkTrap = function(player, trap, hours)
    -- the fishnet can broke !
    local fishCaught = false;
    if hours > 15 and ZombRand(5) == 0 then
--        getSoundManager():PlayWorldSound("getFish", false, player:getSquare(), 1, 20, 1, false)
        player:playSound("CheckFishingNet");
        trap:getSquare():transmitRemoveItemFromSquare(trap);
        player:getInventory():AddItem("Base.BrokenFishingNet");
        return;
    end
    if hours > 20 then
        hours = 20;
    end
    for i=1,hours do
        if ZombRand(4) == 0 then
            player:getInventory():AddItem("Base.BaitFish");
            fishCaught = true;
        end
    end
    if fishCaught then
        fishCaught = false;
        player:getXp():AddXP(Perks.Fishing, 1);
    end
    fishingNet.doTimestamp(trap);
end
