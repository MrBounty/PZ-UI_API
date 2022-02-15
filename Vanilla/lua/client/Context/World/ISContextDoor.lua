--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

--[[
ISWorldMenuElements = ISWorldMenuElements or {};

function ISWorldMenuElements.ContextDoor()
    local self 					= ISMenuElement.new();
    self.worldMenu 				= ISContextManager.getInstance().getWorldMenu();
    self.zIndex 		        = 900;

    function self.init()
    end

    function self.createMenu()
        print("door menu!");
        if instanceof(self.worldMenu.object, "IsoDoor") or (self.worldMenu.thump ~= nil and self.worldMenu.isDoor) then  --FIXME broken
            local door = self.worldMenu.object;
            local text = door:IsOpen() and getText("ContextMenu_Close_door") or getText("ContextMenu_Open_door");

            -- a door can be opened/close only if it not barricaded
            if door:getBarricade() == 0 then
                if self.worldMenu.test == true then return true; end
                self.worldMenu.context:addOption("ALT " .. text, self.worldMenu, self.onOpenCloseDoor, door);
            end
            if (self.worldMenu.inventory:contains("Hammer") or self.worldMenu.inventory:contains("HammerStone")) and door:getBarricade() > 0 then
                if self.worldMenu.test == true then return true; end
                self.worldMenu.context:addOption("ALT " .. getText("ContextMenu_Unbarricade"), self.worldMenu, self.onUnbarricade, door);
            end
            -- barricade (hammer on 1st hand, plank on 2nd hand)
            if (self.worldMenu.inventory:contains("Hammer") or self.worldMenu.inventory:contains("HammerStone")) and self.worldMenu.inventory:contains("Plank") and door:getBarricade() < 4 and self.worldMenu.inventory:contains("Nails") then
                if self.worldMenu.test == true then return true; end
                self.worldMenu.context:addOption("ALT " .. getText("ContextMenu_Barricade"), self.worldMenu, self.onBarricade, door);
            end
        end
    end

    function self.onOpenCloseDoor(_p, _door)
        if luautils.walkAdjWindowOrDoor(_p.player, _door:getSquare(), _door) then
            ISTimedActionQueue.add(ISOpenCloseDoor:new(_p.player, _door, 0));
        end
    end

    function self.onUnbarricade(_p, _barricadeObj)
        if luautils.walkAdjWindowOrDoor(_p.player, _barricadeObj:getSquare(), _barricadeObj) then
            if playerObj:getInventory():contains("Hammer") then
                ISWorldObjectContextMenu.equip(_p.player, _p.player:getPrimaryHandItem(), "Hammer", true);
            else
                ISWorldObjectContextMenu.equip(_p.player, _p.player:getPrimaryHandItem(), "HammerStone", true);
            end
            ISTimedActionQueue.add(ISUnbarricadeAction:new(_p.player, _barricadeObj, (100 - (_p.player:getPerkLevel(Perks.Woodwork) * 10))));
        end
    end

    function self.onBarricade(_p, _barricadeObj)
        -- we must check these otherwise ISEquipWeaponAction will get a null item
        if not _p.inventory:FindAndReturn("Hammer") and not _p.inventory:FindAndReturn("HammerStone") then return end
        if not _p.inventory:FindAndReturn("Plank") then return end
        if not _p.inventory:FindAndReturn("Nails") then return end
        local parent = _barricadeObj:getSquare();
        if not AdjacentFreeTileFinder.isTileOrAdjacent(_p.player:getCurrentSquare(), parent) then
            local adjacent = nil;
            if ISWorldObjectContextMenu.isThumpDoor(_barricadeObj) then
                adjacent = AdjacentFreeTileFinder.FindWindowOrDoor(parent, _barricadeObj, _p.player);
            else
                adjacent = AdjacentFreeTileFinder.Find(parent, _p.player);
            end
            if adjacent ~= nil then
                ISTimedActionQueue.clear(_p.player);
                if playerObj:getInventory():contains("Hammer") then
                    ISWorldObjectContextMenu.equip(_p.player, _p.player:getPrimaryHandItem(), "Hammer", true);
                else
                    ISWorldObjectContextMenu.equip(_p.player, _p.player:getPrimaryHandItem(), "HammerStone", true);
                end
                ISWorldObjectContextMenu.equip(_p.player, _p.player:getSecondaryHandItem(), "Plank", false);
                ISTimedActionQueue.add(ISWalkToTimedAction:new(_p.player, adjacent));
                ISTimedActionQueue.add(ISBarricadeAction:new(_p.player, _barricadeObj, (100 - (_p.player:getPerkLevel(Perks.Woodwork) * 10))));
                return;
            else
                return;
            end
        else
            if playerObj:getInventory():contains("Hammer") then
                ISWorldObjectContextMenu.equip(_p.player, _p.player:getPrimaryHandItem(), "Hammer", true);
            else
                ISWorldObjectContextMenu.equip(_p.player, _p.player:getPrimaryHandItem(), "HammerStone", true);
            end
            ISWorldObjectContextMenu.equip(_p.player, _p.player:getSecondaryHandItem(), "Plank", false);
            ISTimedActionQueue.add(ISBarricadeAction:new(_p.player, _barricadeObj, (100 - (_p.player:getPerkLevel(Perks.Woodwork) * 10))));
        end
    end

    return self;
end
--]]