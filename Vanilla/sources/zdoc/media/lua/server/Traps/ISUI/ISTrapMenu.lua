--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 06/02/14
-- Time: 10:22
-- To change this template use File | Settings | File Templates.
--

---@class ISTrapMenu
ISTrapMenu = {};

ISTrapMenu.doTrapMenu = function(player, context, worldobjects, test)
    local placeTrap = false;
    local trapList = {};
    local placedTrap = nil;
    local playerInv = getSpecificPlayer(player):getInventory();
    for i,v in ipairs(worldobjects) do
--        if v:getName() == "Trap" then
            placedTrap = CTrapSystem.instance:getLuaObjectAt(v:getX(), v:getY(), v:getZ());
            if placedTrap then break end
--        end
    end

    if not placedTrap then
        -- check the player inventory to add a trap
        for i = 0, playerInv:getItems():size() - 1 do
            local vItem = playerInv:getItems():get(i);
            if vItem:isTrap() then
                table.insert(trapList, vItem);
            end
        end
    end

    if #trapList > 0 then
        if test then return ISWorldObjectContextMenu.setTest() end
        local trapOption = context:addOption(getText("ContextMenu_Place_Trap"), worldobjects, nil);
        local subMenuTrap = context:getNew(context);
        context:addSubMenu(trapOption, subMenuTrap);
        for i,v in ipairs(trapList) do
            subMenuTrap:addOption(v:getName(), worldobjects, ISTrapMenu.onPlaceTrap, v, getSpecificPlayer(player));
        end
    end

    -- interact with the trap
    if placedTrap then
        -- add bait
        if not placedTrap.bait and not placedTrap.animal.type and playerInv:contains("Type:Food") then
            local alreadyAddedItems = {};
            local items = {}
            for i = 0, playerInv:getItems():size() - 1 do
                local vItem = playerInv:getItems():get(i);
                if instanceof(vItem, "Food") and (vItem:getHungerChange() <= -0.05 or vItem:getType() == "Worm") and not vItem:isCooked() and
                        not alreadyAddedItems[vItem:getName()] and not vItem:haveExtraItems() and
                        (vItem:getCustomMenuOption() ~= "Drink") then
                    table.insert(items, vItem)
                    alreadyAddedItems[vItem:getName()] = true;
                end
            end
            if #items > 0 then
                if test then return ISWorldObjectContextMenu.setTest() end
                local baitOption = context:addOption(getText("ContextMenu_Add_Bait"), worldobjects, nil);
                local subMenuBait = context:getNew(context);
                context:addSubMenu(baitOption, subMenuBait);
                for _,vItem in ipairs(items) do
                    subMenuBait:addOption(vItem:getName(), worldobjects, ISTrapMenu.onAddBait, vItem, placedTrap, getSpecificPlayer(player));
                end
            end
        elseif placedTrap.bait then
            if test then return ISWorldObjectContextMenu.setTest() end
--            print("found bait, day " .. placedTrap.baitDay);
            context:addOption(getText("ContextMenu_Remove_Bait"), worldobjects, ISTrapMenu.onRemoveBait, placedTrap, getSpecificPlayer(player));
        end
        if placedTrap.animal.type then
            if test then return ISWorldObjectContextMenu.setTest() end
            context:addOption(getText("ContextMenu_Check_Trap"), worldobjects, ISTrapMenu.onCheckTrap, placedTrap, getSpecificPlayer(player));
        end
        if not placedTrap.animal.type then
            if test then return ISWorldObjectContextMenu.setTest() end
            context:addOption(getText("ContextMenu_Remove_Trap"), worldobjects, ISTrapMenu.onRemoveTrap, placedTrap, getSpecificPlayer(player));
        end
    end
end

ISTrapMenu.onRemoveBait = function(worldobjects, trap, player)
    if luautils.walkAdj(player, trap:getSquare()) then
        ISTimedActionQueue.add(ISRemoveBaitAction:new(player, trap, 20));
    end
end

ISTrapMenu.onAddBait = function(worldobjects, bait, trap, player)
    if luautils.walkAdj(player, trap:getSquare()) then
        ISTimedActionQueue.add(ISAddBaitAction:new(player, bait, trap, 20));
    end
end

ISTrapMenu.onCheckTrap = function(worldobjects, trap, player)
    if luautils.walkAdj(player, trap:getSquare()) then
        ISTimedActionQueue.add(ISCheckTrapAction:new(player, trap, 40));
    end
end

ISTrapMenu.onRemoveTrap = function(worldobjects, trap, player)
    if luautils.walkAdj(player, trap:getSquare()) then
        ISTimedActionQueue.add(ISRemoveTrapAction:new(player, trap, 40));
    end
end

ISTrapMenu.onPlaceTrap = function(worldobjects, trap, player)
    getCell():setDrag(TrapBO:new(player, trap), player:getPlayerNum());
end

Events.OnFillWorldObjectContextMenu.Add(ISTrapMenu.doTrapMenu);
