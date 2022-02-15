---@class Tutorial1
Tutorial1 = {}

Tutorial1.Add = function()

end

Tutorial1.PreloadInit = function()
    local female = true;
    local desc = SurvivorFactory.CreateSurvivor(SurvivorType.Neutral, female);
    getWorld():setLuaPlayerDesc(desc);
    getWorld():getLuaTraits():clear();

    SandboxVars.Alarm = 1 -- Never
    SandboxVars.ZombieLore.ThumpNoChasing = true;
    SandboxVars.ZombieLore.Strength = 1;
    SandboxVars.ThumpOnConstruction = true;
end

Tutorial1.AddPlayer = function(p)

    if getCore():isDedicated() then return end

    p:setDir(IsoDirections.SE);
end

Tutorial1.closeBothInv = function()
    Tutorial1.closeLoot();
    Tutorial1.closeInv();
    getPlayer():setBannedAttacking(false);
end

Tutorial1.closeLoot = function()
    getPlayerLoot(0).blink = false;
    getPlayerLoot(0).inventoryPane.highlightItem = nil;
    if JoypadState.players[1] then
        getPlayerLoot(0):setVisible(false);
        setJoypadFocus(0, nil);
    else
        getPlayerLoot(0):collapse();
        getPlayerLoot(0).isCollapsed = true;
        getPlayerLoot(0):setMaxDrawHeight(getPlayerInventory(0):titleBarHeight());
    end
end

Tutorial1.closeInv = function()
    getPlayerInventory(0).blink = false;
    getPlayerInventory(0).inventoryPane.highlightItem = nil;
    if JoypadState.players[1] then
        getPlayerInventory(0):setVisible(false);
        setJoypadFocus(0, nil);
    else
        getPlayerInventory(0):collapse();
        getPlayerInventory(0).isCollapsed = true;
        getPlayerInventory(0):setMaxDrawHeight(getPlayerInventory(0):titleBarHeight());
    end
end

Tutorial1.createInventoryContextMenu = function(player, isInPlayerInventory, items ,x ,y)
    local context = ISContextMenu.get(player, x, y);
    context.blinkOption = ISInventoryPaneContextMenu.blinkOption;
    local testItem = nil;
    for i,v in ipairs(items) do
        if not instanceof(v, "InventoryItem") then
            testItem = v.items[1];
        end
    end
    if testItem then
        if Tutorial1.contextMenuEat and testItem:getType() == "DeadMouse" then
            local eatOption = context:addOption(getText("ContextMenu_Eat"), {testItem});
            local subMenuEat = context:getNew(context)
            context:addSubMenu(eatOption, subMenuEat)
            subMenuEat:addOption(getText("ContextMenu_Eat_All"), {testItem}, ISInventoryPaneContextMenu.onEatItems, 1, player);
            subMenuEat:addOption(getText("ContextMenu_Eat_Half"), {testItem}, ISInventoryPaneContextMenu.onEatItems, 0.5, player)
            subMenuEat:addOption(getText("ContextMenu_Eat_Quarter"), {testItem}, ISInventoryPaneContextMenu.onEatItems, 0.25, player)
        end
        if Tutorial1.contextMenuEquipPrimary and instanceof(testItem, "HandWeapon") then
            context:addOption(getText("ContextMenu_Equip_Primary"), {testItem}, ISInventoryPaneContextMenu.OnPrimaryWeapon, player);
        end
        if Tutorial1.contextMenuEquip2Hand and instanceof(testItem, "HandWeapon") then
            context:addOption(getText("ContextMenu_Equip_Two_Hands"), {testItem}, ISInventoryPaneContextMenu.OnTwoHandsEquip, player);
        end
        if Tutorial1.contextMenuEquipBag and instanceof(testItem, "InventoryContainer") and testItem:canBeEquipped() == "Back" and not getPlayer():isEquipped(testItem) then
            context:addOption(getText("ContextMenu_Equip_on_your_Back"), {testItem}, ISInventoryPaneContextMenu.onWearItems, player);
        end
        if Tutorial1.contextMenuWear then
            if instanceof(testItem, "Clothing") then
                if getSpecificPlayer(player):isEquipped(testItem) then
                    context:addOption(getText("ContextMenu_Unequip"), {testItem}, ISInventoryPaneContextMenu.onUnEquip, player);
                else
                    ISInventoryPaneContextMenu.doWearClothingMenu(player, testItem, items, context);
                end
                
                if testItem:getClothingItemExtraOption() then
                    ISInventoryPaneContextMenu.doClothingItemExtraMenu(context, testItem, getSpecificPlayer(player))
                end
            end
        end
    end
    return context;
end

Tutorial1.createWorldContextMenuFromContext = function(context, worldobjects)
    local chr = getPlayer();
    context.blinkOption = ISInventoryPaneContextMenu.blinkOption;
    local items = {};
    for i,v in ipairs(worldobjects) do
        for i=0,v:getSquare():getObjects():size()-1 do
            table.insert(items, v:getSquare():getObjects():get(i));
        end
    end
    local bottle = (chr:getInventory():FindAndReturn("WaterBottleEmpty") or chr:getInventory():FindAndReturn("WaterBottleFull"));
    for i,v in ipairs(items) do
        if Tutorial1.contextMenuFillBottle and v:hasWater() and bottle then
            context:addOption(getText("ContextMenu_Fill") .. bottle:getName(), worldobjects, ISWorldObjectContextMenu.onTakeWater, v, nil, bottle, chr:getPlayerNum());
            break;
        end
        if Tutorial1.contextMenuOpenCurtain and instanceof(v, "IsoCurtain") or (instanceof(v, "IsoWindow") and v:HasCurtains())then
            if instanceof(v, "IsoWindow") then
                v = v:HasCurtains();
            end
            local text = getText("ContextMenu_Open_curtains");
            if v:IsOpen() then
                text = getText("ContextMenu_Close_curtains");
            end
            if not context:getOptionFromName(text) then
                context:addOption(text, worldobjects, ISWorldObjectContextMenu.onOpenCloseCurtain, v, chr:getPlayerNum());
            end
            break;
        end
    end
    return context;
end

Tutorial1.createWorldContextMenu = function(player, worldobjects, x, y)
    local context = ISContextMenu.get(player, x, y);
    return Tutorial1.createWorldContextMenuFromContext(context, worldobjects);
    
end

-- Return true if the double-click was handled here.
-- Return false if ISInventoryPane should handle the double-click.
Tutorial1.doubleClickInventory = function(inventoryPane, x, y, mouseOverOption)
    if Tutorial1.steps == nil or Tutorial1.steps:isEmpty() then
        return false
    end
    if not inventoryPane.parent.onCharacter then
        return false
    end
    local item = inventoryPane.items[mouseOverOption]
    if not item then
        return false
    end
    if x < inventoryPane.column2 then
        -- Double-clicked the icon to expand/collapse a stack.
        return false
    end
    local items = ISInventoryPane.getActualItems({item})
    for _,item2 in ipairs(items) do
        if instanceof(item2, "HandWeapon") or not Tutorial1.contextMenuEquipPrimary then
            -- Disable double-click to equip or unequip.
            return true
        end
    end
    return false
end

Tutorial1.Init = function()

    Tutorial1.steps = LuaList:new();

    local m1 = Keyboard.getKeyName(getCore():getKey("Forward"))
    local m2 = Keyboard.getKeyName(getCore():getKey("Left"))
    local m3 = Keyboard.getKeyName(getCore():getKey("Backward"))
    local m4 = Keyboard.getKeyName(getCore():getKey("Right"))
    Tutorial1.moveKeys = getText("IGUI_Tutorial1_MovementKeys", m1, m2, m3, m4)

    -- climate
    local clim = getClimateManager();
    local w = clim:getWeatherPeriod();
    if w:isRunning() then
        clim:stopWeatherAndThunder();
    end
    
    -- remove fog
    local var = clim:getClimateFloat(5);
    var:setEnableOverride(true);
    var:setOverride(0, 1);
    
    local chr = getPlayer();
    local inv = chr:getInventory();
    
    local visual = chr:getHumanVisual();
    chr:setFemale(true);
    chr:getDescriptor():setFemale(true);
    chr:getDescriptor():setForename("Jane");
    chr:getDescriptor():setSurname("Doe");
    
    -- reset
    chr:clearWornItems();
    chr:getInventory():clear();
    visual:setSkinTextureIndex(2);
    
    -- chr visual
    local immutableColor = ImmutableColor.new(0.83, 0.67, 0.27, 1)
    visual:setHairModel("Longcurly");
    visual:setHairColor(immutableColor)

    chr:resetModel();
    
    local clothe = inv:AddItem("Base.Tshirt_DefaultTEXTURE_TINT");
    local color = ImmutableColor.new(1, 1, 1, 1)
    clothe:getVisual():setTint(color);
    clothe:synchWithVisual();
    chr:setWornItem(clothe:getBodyLocation(), clothe);
    clothe = inv:AddItem("Base.Shorts_ShortDenim");
    clothe:getVisual():setTextureChoice(2);
    clothe:synchWithVisual();
    chr:setWornItem(clothe:getBodyLocation(), clothe);
    clothe = inv:AddItem("Base.Socks_Ankle");
    clothe:synchWithVisual();
    chr:setWornItem(clothe:getBodyLocation(), clothe);
    clothe = inv:AddItem("Base.Shoes_BlueTrainers");
    clothe:synchWithVisual();
    chr:setWornItem(clothe:getBodyLocation(), clothe);

    chr:setInvincible(true);
    chr:setUnlimitedCarry(true);
    chr:setUnlimitedEndurance(true);

    -- board up all other windows
    Tutorial1.addBarricade(162, 157, 0);
    Tutorial1.addBarricade(158, 160, 0);
    Tutorial1.addBarricade(160, 152, 0);
    Tutorial1.addBarricade(154, 151, 0);
    Tutorial1.addBarricade(151, 151, 0);
    Tutorial1.addBarricade(153, 162, 0);
    Tutorial1.addBarricade(149, 162, 0);
    Tutorial1.addBarricade(147, 159, 0);
    Tutorial1.addBarricade(148, 151, 0);
    Tutorial1.addBarricade(147, 154, 1);
    Tutorial1.addBarricade(149, 151, 1);
    Tutorial1.addBarricade(154, 151, 1);
    Tutorial1.addBarricade(131, 179, 0);
    Tutorial1.addBarricade(129, 183, 0);
    BandageStep.barricadedWindow1 = Tutorial1.addMetalBarricade(179, 147, 0);
    BandageStep.curtain = BandageStep.barricadedWindow1:HasCurtains();
    BandageStep.curtain:ToggleDoorSilent();
    Tutorial1.addMetalBarricade(179, 150, 0);
    Tutorial1.addMetalBarricade(177, 153, 0);
    -- easter egg windows
    Tutorial1.addBarricade(195, 194, 0);
    Tutorial1.addBarricade(197, 199, 0);

    -- add trash can to block player when killing mom
--    local sq = getSquare(169, 157, 0);
--    sq:AddTileObject(IsoObject.getNew(sq, "street_decoration_01_26", nil, false));

    -- lock needed doors
    Tutorial1.lockDoor(131, 181, 0)
    SneakStep.fenceGate = Tutorial1.lockDoor(169, 145, 0)
    FightStep.lockedDoor = Tutorial1.lockDoor(163, 153, 0)
    FightStep.lockedDoor2 = Tutorial1.lockDoor(164, 153, 0)
    BandageStep.lockedDoor = Tutorial1.lockDoor(176, 153, 0)
    -- easter egg door
    Tutorial1.lockDoor(193, 194, 0)
    Tutorial1.lockDoor(106, 128, 0)

    -- gonna remove brothers fences to replace it with IsoThumpable
    Tutorial1.removeFences();
    
    addVehicleDebug("Base.PickupBurnt", IsoDirections.E, -1, getSquare(164, 160, 0));
    addVehicleDebug("Base.CarNormalBurnt", IsoDirections.NE, -1, getSquare(168, 160, 0));
    addVehicleDebug("Base.TaxiBurnt", IsoDirections.N, -1, getSquare(169, 155, 0));

    Tutorial1.marker = getWorldMarkers();

    -- open some doors
--    Tutorial1.openDoor(103, 124, 0, true)
--    Tutorial1.openDoor(106, 124, 0, true)
--    Tutorial1.openDoor(131, 181, 0, false)
    
    if JoypadState.players[1] then
        Tutorial1.joypad = true;
        JoypadState.disableGrab = true;
        JoypadState.disableInvInteraction = true;
        JoypadState.disableYInventory = true;
        JoypadState.disableControllerPrompt = true;
        JoypadState.disableMovement = true;
        JoypadState.disableClimbOver = true;
        JoypadState.disableSmashWindow = true;
        JoypadState.disableReload = true;
        ISBackButtonWheel.disablePlayerInfo = true;
        ISBackButtonWheel.disableCrafting = true;
        ISBackButtonWheel.disableTime = true;
        ISBackButtonWheel.disableMoveable = true;
        ISBackButtonWheel.disableZoomOut = true;
        ISBackButtonWheel.disableZoomIn = true;
    end
    
    
    Tutorial1.steps:add(WelcomeStep:new())
    Tutorial1.steps:add(WalkToAdjacent:new())
    Tutorial1.steps:add(InventoryLootingStep:new());
    Tutorial1.steps:add(InventoryUseStep:new());
    Tutorial1.steps:add(FightStep:new());
    Tutorial1.steps:add(SneakStep:new());
    Tutorial1.steps:add(BandageStep:new());
    Tutorial1.steps:add(ShotgunStep:new());

    Tutorial1.steps:get(0):begin();
    
    SurvivalGuideManager.blockSurvivalGuide = true;
    getPlayer():setCanShout(false);
    getPlayer():setAllowSprint(false);
    getCore():setCollideZombies(false);
    
    ISReloadWeaponAction.disableReloading = true;
    getPlayerInventory(0).transferAll:setVisible(false);
    getPlayerLoot(0).lootAll:setVisible(false);
    
    if SurvivalGuideManager.instance then
        SurvivalGuideManager.instance.panel:setVisible(false);
    end

    Events.OnTick.Add(Tutorial1.Tick)
--    Tutorial1.FillContainers();
end

Tutorial1.openDoor = function(x,y,z, north)
    local sq = getSquare(x, y, z);
    if sq and sq:getDoor(north) then
        local door = sq:getDoor(north);
        door:setLocked(false);
        door:setLockedByKey(false);
        door:ToggleDoorSilent();
    end
end

Tutorial1.collapseInv = function(inv)
    inv:collapse();
    inv.isCollapsed = true;
    inv:setMaxDrawHeight(inv:titleBarHeight());
end

Tutorial1.lockDoor = function(x, y, z)
    local sq = getSquare(x, y, z);
    for i=0, sq:getObjects():size() -1 do
        local obj = sq:getObjects():get(i);
        if instanceof(obj, "IsoDoor") then
            obj:setLockedByKey(true);
            obj:setLocked(true);
            return obj;
        end
    end
end

Tutorial1.unlockDoor = function(x, y, z)
    local sq = getSquare(x, y, z);
    for i=0, sq:getObjects():size() -1 do
        local obj = sq:getObjects():get(i);
        if instanceof(obj, "IsoDoor") then
            obj:setLockedByKey(false);
            obj:setLocked(false);
            return obj;
        end
    end
end

Tutorial1.addBarricade = function(x, y, z)
    local sq = getSquare(x, y, z)
    if sq then
        for i = 0, sq:getObjects():size()-1 do
            local o = sq:getObjects():get(i);
            if instanceof(o, "IsoWindow") then
                o:addBarricadesDebug(ZombRand(2,5), false);
                break;
            end
            if instanceof(o, "IsoDoor") then
                o:addRandomBarricades();
                break;
            end
        end
    end
end

Tutorial1.addMetalBarricade = function(x, y, z)
    local sq = getSquare(x, y, z)
    if sq then
        for i = 0, sq:getObjects():size()-1 do
            local o = sq:getObjects():get(i);
            if instanceof(o, "IsoWindow") then
                o:addBarricadesDebug(ZombRand(2,5), true);
                o:setPermaLocked(true);
                return o;
            end
        end
    end
end

Tutorial1.cratePositions = {{"lootingStuff", "counter", 157, 152, 0}};
Tutorial1.FillContainers = function()
    for k, v in ipairs(Tutorial1.cratePositions) do
        local type = v[1];
        local container = v[2];
        local x = v[3];
        local y = v[4];
        local z = v[5];
        local sq = getCell():getGridSquare(x, y, z);
        if sq ~= nil then
            local objs = sq:getObjects();
            for i = 0, objs:size()-1 do
                local o = objs:get(i);
                local c = o:getContainer();
                if c ~= nil and c:getType() == container then
                    c:emptyIt();
                    if type == "lootingStuff" then
                        Tutorial1.DeadMouseContainer = o;
                        local apple = c:AddItem("Base.DeadMouse");
                        apple:setAge(17);
                        Tutorial1.DeadMouse = apple
                        c:AddItem("Base.WaterBottleEmpty");
                    end
                    c:setExplored(true);
                end
            end
        end
    end
end

Tutorial1.SpawnZombies = function(count)

end

Tutorial1.Render = function()

end

Tutorial1.Tick = function()
    if getPlayer() == nil then return end;

    if Tutorial1.steps == nil or Tutorial1.steps:isEmpty() then return; end
    if JoypadState.players[1] then
        Tutorial1.joypad = true;
    end

    if Tutorial1.steps:get(0):isComplete() then
        Tutorial1.steps:get(0):finish();
        Tutorial1.steps:removeAt(0);

        if not Tutorial1.steps:isEmpty() then
            Tutorial1.steps:get(0):begin();
        end
    end
end

function Tutorial1.removeFences()
    for x=178, 187 do
        local sq = getSquare(x, 153, 0);
        Tutorial1.replaceFence(sq, true);
    end
    
    for y=143, 152 do
        local sq = getSquare(188, y, 0);
        Tutorial1.replaceFence(sq, false);
    end
end

function Tutorial1.replaceFence(sq, north)
--    local sprite = "walls_exterior_wooden_01_24"
--    if north then
--        sprite = "walls_exterior_wooden_01_25"
--    end
    if not sq then return end;
    for i=0, sq:getObjects():size()-1 do
        local obj = sq:getObjects():get(i);
        if obj:getType() == IsoObjectType.wall then
            local newFence = IsoThumpable.new(getCell(), sq, obj:getSprite():getName(), north, ISWoodenWall:new(obj:getSprite():getName(), obj:getSprite():getName(), nil));
            sq:AddTileObject(newFence);
            sq:RemoveTileObject(obj);
            return;
        end
--        if obj:getType() == IsoObjectType.doorW then
--            local newFence = IsoThumpable.new(getCell(), sq, "fencing_01_58", north, ISWoodenWall:new("fencing_01_58", "fencing_01_58", nil));
--            local newFence = IsoThumpable.new(getCell(), sq, sprite, north, ISWoodenWall:new(sprite, sprite, nil));
--            sq:AddTileObject(newFence);
--            sq:RemoveTileObject(obj);
--        end
    end
end
    
Tutorial1.name = "Tutorial1";
--Tutorial1.description = "Prepare for a short, sharp dose of Zomboid. Deadheads are approaching from every angle.\nYou're going to check out, and soon, but just how long can you resist the horde?\nReady that shotgun...";
Tutorial1.image = "media/lua/LastStand/Challenge1.png";
Tutorial1.world = "challengemaps/Tutorial";
Tutorial1.xcell = 0;
Tutorial1.ycell = 0;
Tutorial1.x = 157;
Tutorial1.y = 157;
Tutorial1.hourOfDay = 20;
--Tutorial1.cratePositions = { {"weapons3", "crate", 151, 152, 0},{"weapons2", "crate", 142, 148, 0}, {"weapons1", "crate", 147+3, 151+3, 1}, {"medicine", "crate", 156+3, 144+3, 1}, {"carpentry", "crate", 135, 179, 0}, {"carpentry", "crate", 157, 151, 0}, {"carpentry", "crate", 158, 151, 0}}
--Events.OnChallengeQuery.Add(Tutorial1.Add)
