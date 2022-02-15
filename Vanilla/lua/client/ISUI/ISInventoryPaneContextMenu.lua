--***********************************************************
--**                LEMMY/ROBERT JOHNSON                   **
--***********************************************************

require "ISUI/ISToolTip"

ISInventoryPaneContextMenu = {}
ISInventoryPaneContextMenu.tooltipPool = {}
ISInventoryPaneContextMenu.tooltipsUsed = {}

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

local function predicateNotBroken(item)
	return not item:isBroken()
end

-- MAIN METHOD FOR CREATING RIGHT CLICK CONTEXT MENU FOR INVENTORY ITEMS
ISInventoryPaneContextMenu.createMenu = function(player, isInPlayerInventory, items, x, y, origin)
    if getCore():getGameMode() == "Tutorial" then
        Tutorial1.createInventoryContextMenu(player, isInPlayerInventory, items ,x ,y);
        return;
    end
    if ISInventoryPaneContextMenu.dontCreateMenu then return; end

	-- if the game is paused, we don't show the item context menu
	if UIManager.getSpeedControls():getCurrentGameSpeed() == 0 then
		return;
	end

    -- items is a list that could container either InventoryItem objects, OR a table with a list of InventoryItem objects in .items
    -- Also there is a duplicate entry first in the list, so ignore that.

    --print("Context menu for player "..player);
    --print("Creating context menu for inventory items");
    local context = ISContextMenu.get(player, x, y);
    -- avoid doing action while trading (you could eat half an apple and still trade it...)
--    if ISTradingUI.instance and ISTradingUI.instance:isVisible() then
--        context:addOption(getText("IGUI_TradingUI_CantRightClick"), nil, nil);
--        return;
--    end

    context.origin = origin;
	local itemsCraft = {};
    local c = 0;
    local isAllFood = true;
	local isWeapon = nil;
	local isHandWeapon = nil;
	local isAllPills = true;
	local clothing;
	local recipe = nil;
    local evorecipe = nil;
    local baseItem = nil;
	local isAllLiterature = true;
	local canBeActivated = nil;
	local isAllBandage = true;
	local unequip = nil;
    local isReloadable = false;
	local waterContainer = nil;
	local canBeDry = nil;
	local canBeEquippedBack = nil;
	local twoHandsItem = nil;
    local brokenObject = nil;
    local canBeRenamed = nil;
    local canBeRenamedFood = nil;
    local pourOnGround = nil
    local canBeWrite = nil;
    local force2Hands = nil;
    local remoteController = nil;
    local remoteControllable = nil;
    local generator = nil;
    local corpse = nil;
    local alarmClock = nil;
    local inPlayerInv = nil;
    local drainable = nil;
    local map = nil;
    local carBattery = nil;
    local carBatteryCharger = nil;
    local clothingRecipe = nil;
    local clothingItemExtra = nil;
    local magazine = nil;
    local bullet = nil;
    local hairDye = nil;
    local makeup = nil;

    local playerObj = getSpecificPlayer(player)
    local playerInv = playerObj:getInventory()

	ISInventoryPaneContextMenu.removeToolTip();

	getCell():setDrag(nil, player);

    for _,tooltip in ipairs(ISInventoryPaneContextMenu.tooltipsUsed) do
        table.insert(ISInventoryPaneContextMenu.tooltipPool, tooltip);
    end
--    print('reused ',#ISInventoryPaneContextMenu.tooltipsUsed,' inventory tooltips')
    table.wipe(ISInventoryPaneContextMenu.tooltipsUsed);

    local containerList = ISInventoryPaneContextMenu.getContainers(playerObj)
    local testItem = nil;
    local editItem = nil;
    for i,v in ipairs(items) do
        testItem = v;
        if not instanceof(v, "InventoryItem") then
            --print(#v.items);
            if #v.items == 2 then
                editItem = v.items[1];
            end
            testItem = v.items[1];
        else
            editItem = v
        end
        if instanceof(testItem, "Key") or testItem:getType() == "KeyRing" then
            canBeRenamed = testItem;
        end
        if testItem:getClothingItemExtraOption() then
            clothingItemExtra = testItem;
        end
		if not testItem:isCanBandage() then
			isAllBandage = false;
		end
        if testItem:getCategory() ~= "Food" or testItem:getScriptItem():isCantEat() then
            isAllFood = false;
        end
		if testItem:getCategory() == "Clothing" then
            clothing = testItem;
        end
		if testItem:getType() == "DishCloth" or testItem:getType() == "BathTowel" and playerObj:getBodyDamage():getWetness() > 0 then
			canBeDry = true;
        end
        if testItem:isHairDye() then
            hairDye = testItem;
        end
        if testItem:getMakeUpType() then
            makeup = testItem;
        end
        if testItem:isBroken() or testItem:getCondition() < testItem:getConditionMax() then
            brokenObject = testItem;
        end
        if instanceof(testItem, "DrainableComboItem") then
            drainable = testItem;
        end
        if testItem:getContainer() and testItem:getContainer():isInCharacterInventory(playerObj) then
            inPlayerInv = testItem;
        end
        if testItem:getMaxAmmo() > 0 and not instanceof(testItem, "HandWeapon") then
            magazine = testItem;
        end
        if testItem:getDisplayCategory() == "Ammo" then
            bullet = testItem;
        end
        if playerObj:isEquipped(testItem) then
			unequip = testItem;
        end
        if ISInventoryPaneContextMenu.startWith(testItem:getType(), "CarBattery") and testItem:getType() ~= "CarBatteryCharger" then
            carBattery = testItem;
        end
        if testItem:getType() == "CarBatteryCharger" then
            carBatteryCharger = testItem;
        end
        if testItem:IsMap() then
            map = testItem;
        end
		if testItem:getCategory() ~= "Literature" or testItem:canBeWrite() then
            isAllLiterature = false;
        end
        if testItem:getCategory() == "Literature" and testItem:canBeWrite() then
            canBeWrite = testItem;
        end
		if testItem:canBeActivated() and (playerObj:isHandItem(testItem) or playerObj:isAttachedItem(testItem)) then
            canBeActivated = testItem;
        end
		-- all items can be equiped
		if (instanceof(testItem, "HandWeapon") and testItem:getCondition() > 0) or (instanceof(testItem, "InventoryItem") and not instanceof(testItem, "HandWeapon")) then
            isWeapon = testItem;
        end
        if instanceof(testItem, "HandWeapon") then
            isHandWeapon = testItem
        end
        -- remote controller
        if testItem:isRemoteController() then
            remoteController = testItem;
        end
        if isHandWeapon and isHandWeapon:canBeRemote() then
            remoteControllable = isHandWeapon;
        end
		if instanceof(testItem, "InventoryContainer") and testItem:canBeEquipped() == "Back" and not playerObj:isEquippedClothing(testItem) then
			canBeEquippedBack = testItem;
        end
        if instanceof(testItem, "InventoryContainer") then
            canBeRenamed = testItem;
        end
        if testItem:getType() == "Generator" then
            generator = testItem;
        end
        if testItem:getType() == "CorpseMale" or testItem:getType() == "CorpseFemale" then
            corpse = testItem;
        end
        if instanceof(testItem, "AlarmClock") or instanceof(testItem, "AlarmClockClothing") then
            alarmClock = testItem;
        end
        if instanceof(testItem, "Food")  then -- Check if it's a recipe from the evolved recipe and have at least 3 ingredient, so we can name them
            for i=0,getEvolvedRecipes():size()-1 do
                local evoRecipeTest = getEvolvedRecipes():get(i);
                if evoRecipeTest:isResultItem(testItem) and testItem:haveExtraItems() and testItem:getExtraItems():size() >= 3 then
                    canBeRenamedFood = testItem;
                end
            end
        end
		if testItem:isTwoHandWeapon() and testItem:getCondition() > 0 then
			twoHandsItem = testItem;
        end
        if testItem:isRequiresEquippedBothHands() and testItem:getCondition() > 0 then
            force2Hands = testItem;
        end
        --> Stormy
		if(not getCore():isNewReloading() and ReloadUtil:isReloadable(testItem, playerObj)) then
			isReloadable = true;
        end
		-->> Stormy
		if not ISInventoryPaneContextMenu.startWith(testItem:getType(), "Pills") then
            isAllPills = false;
        end
        if testItem:isWaterSource() then
            waterContainer = testItem;
        end
        if not instanceof(testItem, "Literature") and ISInventoryPaneContextMenu.canReplaceStoreWater(testItem) then
            pourOnGround = testItem
        end
        -- if item is not a clothing, use ClothingRecipesDefinitions
        if not playerObj:isEquippedClothing(testItem) and (ClothingRecipesDefinitions[testItem:getType()] or (testItem:getFabricType() and instanceof(testItem, "Clothing"))) then
            clothingRecipe = testItem;
        end
        evorecipe = RecipeManager.getEvolvedRecipe(testItem, playerObj, containerList, true);
        if evorecipe then
            baseItem = testItem;
        end
        itemsCraft[c + 1] = testItem;

        c = c + 1;
        -- you can equip only 1 weapon
        if c > 1 then
            --~ 			isWeapon = false;
            isHandWeapon = nil
            isAllLiterature = false;
            canBeActivated = nil;
            isReloadable = false;
            unequip = nil;
            canBeEquippedBack = nil;
            brokenObject = nil;
        end
    end

    triggerEvent("OnPreFillInventoryObjectContextMenu", player, context, items);

    context.blinkOption = ISInventoryPaneContextMenu.blinkOption;

    if editItem and c == 1 and ((isClient() and playerObj:getAccessLevel() ~= "None" and playerObj:getAccessLevel() ~= "Observer") and playerObj:getInventory():contains(editItem, true) or isDebugEnabled()) then
        context:addOption(getText("ContextMenu_EditItem"), items, ISInventoryPaneContextMenu.onEditItem, playerObj, testItem);
    end

    -- check the recipe
    if #itemsCraft > 0 then
        local sameType = true
        for i=2,#itemsCraft do
            if itemsCraft[i]:getFullType() ~= itemsCraft[1]:getFullType() then
                sameType = false
                break
            end
        end
        if sameType then
            recipe = RecipeManager.getUniqueRecipeItems(itemsCraft[1], playerObj, containerList);
        end
    end


    if c == 0 then
        return;
    end
    local loot = getPlayerLoot(player);
--~ 	context:addOption("Information", items, ISInventoryPaneContextMenu.onInformationItems);
	if not isInPlayerInventory then
        ISInventoryPaneContextMenu.doGrabMenu(context, items, player);
    end
    if evorecipe then
        ISInventoryPaneContextMenu.doEvorecipeMenu(context, items, player, evorecipe, baseItem, containerList);
    end
    
    if(isInPlayerInventory and loot.inventory ~= nil and loot.inventory:getType() ~= "floor" ) and playerObj:getJoypadBind() == -1 then
        if ISInventoryPaneContextMenu.isAnyAllowed(loot.inventory, items) and not ISInventoryPaneContextMenu.isAllFav(items) then
            local label = loot.title and getText("ContextMenu_PutInContainer", loot.title) or getText("ContextMenu_Put_in_Container")
            context:addOption(label, items, ISInventoryPaneContextMenu.onPutItems, player);
        end
    end

    -- Move To
    local moveItems = ISInventoryPane.getActualItems(items)
    if #moveItems > 0 and playerObj:getJoypadBind() ~= -1 then
        local subMenu = nil
        local moveTo0 = ISInventoryPaneContextMenu.canUnpack(moveItems, player)
        local moveToWorn = {}
        local wornItems = playerObj:getWornItems()
        for i=1,wornItems:size() do
            local item = wornItems:get(i-1):getItem()
            local moveTo1 = ISInventoryPaneContextMenu.canMoveTo(moveItems, item, player)
            if moveTo1 then
                table.insert(moveToWorn, moveTo1)
            end
        end
        local moveTo2 = ISInventoryPaneContextMenu.canMoveTo(moveItems, playerObj:getPrimaryHandItem(), player)
        local moveTo3 = ISInventoryPaneContextMenu.canMoveTo(moveItems, playerObj:getSecondaryHandItem(), player)
        local moveTo4 = ISInventoryPaneContextMenu.canMoveTo(moveItems, ISInventoryPage.floorContainer[player+1], player)
        local keyRings = {}
        local inventoryItems = playerObj:getInventory():getItems()
        for i=1,inventoryItems:size() do
            local item = inventoryItems:get(i-1)
            if item:getType() == "KeyRing" and ISInventoryPaneContextMenu.canMoveTo(moveItems, item, player) then
                table.insert(keyRings, item)
            end
        end
        local putIn = isInPlayerInventory and
                        loot.inventory and loot.inventory:getType() ~= "floor" and
                        ISInventoryPaneContextMenu.isAnyAllowed(loot.inventory, items) and
                        not ISInventoryPaneContextMenu.isAllFav(moveItems)
        if moveTo0 or (#moveToWorn > 0) or moveTo2 or moveTo3 or moveTo4 or (#keyRings > 0) or putIn then
            local option = context:addOption(getText("ContextMenu_Move_To"))
            local subMenu = context:getNew(context)
            context:addSubMenu(option, subMenu)
            local subOption
            if moveTo0 then
                subOption = subMenu:addOption(getText("ContextMenu_MoveToInventory"), moveItems, ISInventoryPaneContextMenu.onMoveItemsTo, playerInv, player)
                if not ISInventoryPaneContextMenu.hasRoomForAny(playerObj, playerInv, moveItems) then
                    subOption.notAvailable = true
                end
            end
            for _,moveTo in ipairs(moveToWorn) do
                subOption = subMenu:addOption(moveTo:getName(), moveItems, ISInventoryPaneContextMenu.onMoveItemsTo, moveTo:getInventory(), player)
                if not ISInventoryPaneContextMenu.hasRoomForAny(playerObj, moveTo, moveItems) then
                    subOption.notAvailable = true
                end
            end
            if moveTo2 then
                subOption = subMenu:addOption(moveTo2:getName(), moveItems, ISInventoryPaneContextMenu.onMoveItemsTo, moveTo2:getInventory(), player)
                if not ISInventoryPaneContextMenu.hasRoomForAny(playerObj, moveTo2, moveItems) then
                    subOption.notAvailable = true
                end
            end
            if moveTo3 then
                subOption = subMenu:addOption(moveTo3:getName(), moveItems, ISInventoryPaneContextMenu.onMoveItemsTo, moveTo3:getInventory(), player)
                if not ISInventoryPaneContextMenu.hasRoomForAny(playerObj, moveTo3, moveItems) then
                    subOption.notAvailable = true
                end
            end
            for _,moveTo in ipairs(keyRings) do
                subOption = subMenu:addOption(moveTo:getName(), moveItems, ISInventoryPaneContextMenu.onMoveItemsTo, moveTo:getInventory(), player)
                if not ISInventoryPaneContextMenu.hasRoomForAny(playerObj, moveTo, moveItems) then
                    subOption.notAvailable = true
                end
            end
            if putIn then
                subOption = subMenu:addOption(loot.title and loot.title or getText("ContextMenu_MoveToContainer"), moveItems, ISInventoryPaneContextMenu.onPutItems, player)
                if not ISInventoryPaneContextMenu.hasRoomForAny(playerObj, loot.inventory, moveItems) then
                    subOption.notAvailable = true
                end
            end
            if moveTo4 then
                subOption = subMenu:addOption(getText("ContextMenu_Floor"), moveItems, ISInventoryPaneContextMenu.onMoveItemsTo, moveTo4, player)
                if not ISInventoryPaneContextMenu.hasRoomForAny(playerObj, moveTo4, moveItems) then
                    subOption.notAvailable = true
                end
            end
        end

        if isInPlayerInventory then
            context:addOption(getText("IGUI_invpage_Transfer_all"), getPlayerInventory(player), ISInventoryPage.transferAll)
        else
            context:addOption(getText("IGUI_invpage_Loot_all"), loot, ISInventoryPage.lootAll)
        end
    end

    if #moveItems and playerObj:getJoypadBind() == -1 then
        if ISInventoryPaneContextMenu.canUnpack(moveItems, player) then
            context:addOption(getText("ContextMenu_Unpack"), moveItems, ISInventoryPaneContextMenu.onMoveItemsTo, playerObj:getInventory(), player)
        end
    end

    if inPlayerInv then
       if inPlayerInv:isFavorite() then
           context:addOption(getText("ContextMenu_Unfavorite"), moveItems, ISInventoryPaneContextMenu.onFavorite, inPlayerInv, false)
       else
           context:addOption(getText("IGUI_CraftUI_Favorite"), moveItems, ISInventoryPaneContextMenu.onFavorite, inPlayerInv, true)
       end
    end

    if not inPlayerInv and playerObj:getJoypadBind() ~= -1 then
        ISInventoryPaneContextMenu.doStoveMenu(context, player)
        ISInventoryPaneContextMenu.doTrashCanMenu(context, player)
    end

    if canBeEquippedBack then
        local option = context:addOption(getText("ContextMenu_Equip_on_your_Back"), items, ISInventoryPaneContextMenu.onWearItems, player);
        if playerObj:getClothingItem_Back() then
            local tooltip = ISInventoryPaneContextMenu.addToolTip()
            tooltip.description = getText("Tooltip_ReplaceWornItems") .. " <LINE> <INDENT:20> "
            tooltip.description = tooltip.description .. playerObj:getClothingItem_Back():getDisplayName()
            option.toolTip = tooltip
        end
    end

    if isAllFood then
        -- Some items have a custom menu option, such as "Smoke" or "Drink" instead of "Eat".
        -- If the selected items have different menu options, don't add any eat option.
        -- If a food item has no hunger reduction (like Cigarettes) it is impossible to eat
        -- some percentage, so we shouldn't show the submenu in such cases.
        local foodItems = ISInventoryPane.getActualItems(items)
        local foodByCmd = {}
        local cmd = nil
        local hungerNotZero = 0
        for i,k in ipairs(foodItems) do
            cmd = k:getCustomMenuOption() or getText("ContextMenu_Eat")
            foodByCmd[cmd] = true
            if k:getHungChange() < 0 then
                hungerNotZero = hungerNotZero + 1
            end
        end
        local cmdCount = 0
        for k,v in pairs(foodByCmd) do
            cmdCount = cmdCount + 1
        end
        if cmdCount == 1 then
            if hungerNotZero > 0 then
                local eatOption = context:addOption(cmd, items, nil)
                if playerObj:getMoodles():getMoodleLevel(MoodleType.FoodEaten) >= 3 and playerObj:getNutrition():getCalories() >= 1000 then
                    local tooltip = ISInventoryPaneContextMenu.addToolTip();
                    eatOption.notAvailable = true;
                    tooltip.description = getText("Tooltip_CantEatMore");
                    eatOption.toolTip = tooltip;
                else
                    local subMenuEat = context:getNew(context)
                    context:addSubMenu(eatOption, subMenuEat)
                    local option = subMenuEat:addOption(getText("ContextMenu_Eat_All"), items, ISInventoryPaneContextMenu.onEatItems, 1, player)
                    ISInventoryPaneContextMenu.addEatTooltip(option, foodItems, 1.0)
                    option = subMenuEat:addOption(getText("ContextMenu_Eat_Half"), items, ISInventoryPaneContextMenu.onEatItems, 0.5, player)
                    ISInventoryPaneContextMenu.addEatTooltip(option, foodItems, 0.5)
                    option = subMenuEat:addOption(getText("ContextMenu_Eat_Quarter"), items, ISInventoryPaneContextMenu.onEatItems, 0.25, player)
                    ISInventoryPaneContextMenu.addEatTooltip(option, foodItems, 0.25)
                end
            elseif cmd ~= getText("ContextMenu_Eat") then
                ISInventoryPaneContextMenu.doEatOption(context, cmd, items, player, playerObj, foodItems);
            end
        end
    end
    if generator then
        if not playerObj:isHandItem(generator) then
            context:addOption(getText("ContextMenu_GeneratorTake"), playerObj, ISInventoryPaneContextMenu.equipHeavyItem, generator);
        end
    elseif corpse then
        if not playerObj:isHandItem(corpse) then
            context:addOption(getText("ContextMenu_Grab_Corpse"), playerObj, ISInventoryPaneContextMenu.equipHeavyItem, corpse);
        end
    elseif twoHandsItem and not playerObj:isItemInBothHands(twoHandsItem) then
        context:addOption(getText("ContextMenu_Equip_Two_Hands"), items, ISInventoryPaneContextMenu.OnTwoHandsEquip, player);
    elseif force2Hands and not playerObj:isItemInBothHands(force2Hands) then
        context:addOption(getText("ContextMenu_Equip_Two_Hands"), items, ISInventoryPaneContextMenu.OnTwoHandsEquip, player);
    end
    if isWeapon and not isAllFood and not force2Hands and not clothing then
        ISInventoryPaneContextMenu.doEquipOption(context, playerObj, isWeapon, items, player);
    end
    -- weapon upgrades
    isWeapon = isHandWeapon -- to allow upgrading broken weapons
    local hasScrewdriver = playerInv:containsTagEvalRecurse("Screwdriver", predicateNotBroken)
    if isWeapon and instanceof(isWeapon, "HandWeapon") and hasScrewdriver then
        -- add parts
        local weaponParts = getSpecificPlayer(player):getInventory():getItemsFromCategory("WeaponPart");
        if weaponParts and not weaponParts:isEmpty() then
            local subMenuUp = context:getNew(context);
            local doIt = false;
            local addOption = false;
            local alreadyDoneList = {};
            for i=0, weaponParts:size() - 1 do
                local part = weaponParts:get(i);
                if part:getMountOn():contains(isWeapon:getFullType()) and not alreadyDoneList[part:getName()] then
                    if (part:getPartType() == "Scope") and not isWeapon:getScope() then
                        addOption = true;
                    elseif (part:getPartType() == "Clip") and not isWeapon:getClip() then
                        addOption = true;
                    elseif (part:getPartType() == "Sling") and not isWeapon:getSling() then
                        addOption = true;
                    elseif (part:getPartType() == "Stock") and not isWeapon:getStock() then
                        addOption = true;
                    elseif (part:getPartType() == "Canon") and not isWeapon:getCanon() then
                        addOption = true;
                    elseif (part:getPartType() == "RecoilPad") and not isWeapon:getRecoilpad() then
                        addOption = true;
                    end
                end
                if addOption then
                    doIt = true;
                    subMenuUp:addOption(weaponParts:get(i):getName(), isWeapon, ISInventoryPaneContextMenu.onUpgradeWeapon, part, getSpecificPlayer(player));
                    addOption = false;
                    alreadyDoneList[part:getName()] = true;
                end
            end
            if doIt then
                local upgradeOption = context:addOption(getText("ContextMenu_Add_Weapon_Upgrade"), items, nil);
                context:addSubMenu(upgradeOption, subMenuUp);
            end
        end
        -- remove parts
        if hasScrewdriver and (isWeapon:getScope() or isWeapon:getClip() or isWeapon:getSling() or isWeapon:getStock() or isWeapon:getCanon() or isWeapon:getRecoilpad()) then
            local removeUpgradeOption = context:addOption(getText("ContextMenu_Remove_Weapon_Upgrade"), items, nil);
            local subMenuRemove = context:getNew(context);
            context:addSubMenu(removeUpgradeOption, subMenuRemove);
            if isWeapon:getScope() then
                subMenuRemove:addOption(isWeapon:getScope():getName(), isWeapon, ISInventoryPaneContextMenu.onRemoveUpgradeWeapon, isWeapon:getScope(), getSpecificPlayer(player));
            end
            if isWeapon:getClip() then
                subMenuRemove:addOption(isWeapon:getClip():getName(), isWeapon, ISInventoryPaneContextMenu.onRemoveUpgradeWeapon, isWeapon:getClip(), getSpecificPlayer(player));
            end
            if isWeapon:getSling() then
                subMenuRemove:addOption(isWeapon:getSling():getName(), isWeapon, ISInventoryPaneContextMenu.onRemoveUpgradeWeapon, isWeapon:getSling(), getSpecificPlayer(player));
            end
            if isWeapon:getStock() then
                subMenuRemove:addOption(isWeapon:getStock():getName(), isWeapon, ISInventoryPaneContextMenu.onRemoveUpgradeWeapon, isWeapon:getStock(), getSpecificPlayer(player));
            end
            if isWeapon:getCanon() then
                subMenuRemove:addOption(isWeapon:getCanon():getName(), isWeapon, ISInventoryPaneContextMenu.onRemoveUpgradeWeapon, isWeapon:getCanon(), getSpecificPlayer(player));
            end
            if isWeapon:getRecoilpad() then
                subMenuRemove:addOption(isWeapon:getRecoilpad():getName(), isWeapon, ISInventoryPaneContextMenu.onRemoveUpgradeWeapon, isWeapon:getRecoilpad(), getSpecificPlayer(player));
            end
        end
    end

    if isHandWeapon and isHandWeapon:getExplosionTimer() > 0 then
        if isHandWeapon:getSensorRange() == 0 then
            context:addOption(getText("ContextMenu_TrapSetTimerExplosion"), isHandWeapon, ISInventoryPaneContextMenu.onSetBombTimer, player);
        else
            context:addOption(getText("ContextMenu_TrapSetTimerActivation"), isHandWeapon, ISInventoryPaneContextMenu.onSetBombTimer, player);
        end
    end
    -- place trap/bomb
    if isHandWeapon and isHandWeapon:canBePlaced() then
        context:addOption(getText("ContextMenu_TrapPlace", isHandWeapon:getName()), isHandWeapon, ISInventoryPaneContextMenu.onPlaceTrap, getSpecificPlayer(player));
    end
    -- link remote controller
    if remoteControllable then
        for i = 0, playerObj:getInventory():getItems():size() -1 do
            local item = playerObj:getInventory():getItems():get(i);
            if item:isRemoteController() and (item:getRemoteControlID() == -1 or item:getRemoteControlID() ~= remoteControllable:getRemoteControlID()) then
                context:addOption(getText("ContextMenu_TrapControllerLinkTo", item:getName()), remoteControllable, ISInventoryPaneContextMenu.OnLinkRemoteController, item, player);
            end
        end
        if remoteControllable:getRemoteControlID() ~= -1 then
            context:addOption(getText("ContextMenu_TrapControllerReset"), remoteControllable, ISInventoryPaneContextMenu.OnResetRemoteControlID, player);
        end
    end
    -- remote controller
    if remoteController then
        for i = 0, playerObj:getInventory():getItems():size() -1 do
            local item = playerObj:getInventory():getItems():get(i);
            if instanceof(item, "HandWeapon") and item:canBeRemote() and (item:getRemoteControlID() == -1 or item:getRemoteControlID() ~= remoteController:getRemoteControlID()) then
                context:addOption(getText("ContextMenu_TrapControllerLinkTo", item:getName()), item, ISInventoryPaneContextMenu.OnLinkRemoteController, remoteController, player);
            end
        end
        if remoteController:getRemoteControlID() ~= -1 then
            context:addOption(getText("ContextMenu_TrapControllerTrigger"), remoteController, ISInventoryPaneContextMenu.OnTriggerRemoteController, player);
            context:addOption(getText("ContextMenu_TrapControllerReset"), remoteController, ISInventoryPaneContextMenu.OnResetRemoteControlID, player);
        end
    end
    
    if isHandWeapon and instanceof(isHandWeapon, "HandWeapon") and isHandWeapon:getFireModePossibilities() and isHandWeapon:getFireModePossibilities():size() > 1 then
        ISInventoryPaneContextMenu.doChangeFireModeMenu(playerObj, isHandWeapon, context);
    end

    if isHandWeapon and instanceof(isHandWeapon, "HandWeapon") and getCore():isNewReloading() then
        ISInventoryPaneContextMenu.doReloadMenuForWeapon(playerObj, isHandWeapon, context);
        magazine = nil
        bullet = nil
    end
    
    if magazine and isInPlayerInventory then
        ISInventoryPaneContextMenu.doReloadMenuForMagazine(playerObj, magazine, context);
        ISInventoryPaneContextMenu.doMagazineMenu(playerObj, magazine, context);
        bullet = nil
    end
    if bullet and isInPlayerInventory then
        ISInventoryPaneContextMenu.doReloadMenuForBullets(playerObj, bullet, context);
    end

        --> Stormy
	if isInPlayerInventory and isReloadable then
		local item = items[1];
		-- if it's a header, we get our first item (the selected one)
		if not instanceof(items[1], "InventoryItem") then
			item = items[1].items[1];
		end
		context:addOption(ReloadUtil:getReloadText(item, playerObj), items, ISInventoryPaneContextMenu.OnReload, player);
	end
	-->> Stormy

    if waterContainer and (playerObj:getStats():getThirst() > 0.1) then
        ISInventoryPaneContextMenu.doDrinkForThirstMenu(context, playerObj, waterContainer)
    end

		-- Crowley
	local pourInto = {}
	if c == 1 and waterContainer ~= nil then
		for i = 0, getSpecificPlayer(player):getInventory():getItems():size() -1 do
			local item = getSpecificPlayer(player):getInventory():getItems():get(i);
			if item ~= waterContainer and item:canStoreWater() and not item:isWaterSource() then
				table.insert(pourInto, item)
			elseif item ~= waterContainer and item:canStoreWater() and item:isWaterSource() and instanceof(item, "DrainableComboItem") and (1 - item:getUsedDelta()) >= item:getUseDelta() then
				table.insert(pourInto, item)
			end
		end
		if #pourInto > 0 then
			local subMenuOption = context:addOption(getText("ContextMenu_Pour_into"), items, nil);
			local subMenu = context:getNew(context)
			context:addSubMenu(subMenuOption, subMenu)
			for _,item in ipairs(pourInto) do
                if instanceof(item, "DrainableComboItem") then
					local subOption = subMenu:addOption(item:getName(), items, ISInventoryPaneContextMenu.onTransferWater, waterContainer, item, player);
					local tooltip = ISInventoryPaneContextMenu.addToolTip()
					local tx = getTextManager():MeasureStringX(tooltip.font, getText("ContextMenu_WaterName") .. ":") + 20
					tooltip.description = string.format("%s: <SETX:%d> %d / %d",
						getText("ContextMenu_WaterName"), tx, item:getDrainableUsesInt(), 1.0 / item:getUseDelta() + 0.0001)
					if item:isTaintedWater() then
						tooltip.description = tooltip.description .. " <BR> <RGB:1,0.5,0.5> " .. getText("Tooltip_item_TaintedWater")
					end
					subOption.toolTip = tooltip
                else
                    subMenu:addOption(item:getName(), items, ISInventoryPaneContextMenu.onTransferWater, waterContainer, item, player);
                end
			end
		end

		context:addOption(getText("ContextMenu_Pour_on_Ground"), items, ISInventoryPaneContextMenu.onEmptyWaterContainer, waterContainer, player);
	end
	-- /Crowley

	if c == 1 then
		ISInventoryPaneContextMenu.checkConsolidate(drainable, player, context, pourInto);
	end

	if c == 1 and pourOnGround and not waterContainer then
		context:addOption(getText("ContextMenu_Pour_on_Ground"), items, ISInventoryPaneContextMenu.onDumpContents, pourOnGround, 100.0, player);
	end

	if isAllPills then
		context:addOption(getText("ContextMenu_Take_pills"), items, ISInventoryPaneContextMenu.onPillsItems, player);
    end
	if isAllLiterature and not getSpecificPlayer(player):getTraits():isIlliterate() then
        ISInventoryPaneContextMenu.doLiteratureMenu(context, items, player)
    end
    if clothing and clothing:getCoveredParts():size() > 0 then
        context:addOption(getText("IGUI_invpanel_Inspect"), playerObj, ISInventoryPaneContextMenu.onInspectClothing, clothing);
--        ISInventoryPaneContextMenu.doClothingPatchMenu(player, clothing, context);
    end
	if clothing and not unequip then
        ISInventoryPaneContextMenu.doWearClothingMenu(player, clothing, items, context);
	end

	local addDropOption = true
	if unequip and isForceDropHeavyItem(unequip) then
		context:addOption(getText("ContextMenu_Drop"), items, ISInventoryPaneContextMenu.onUnEquip, player);
		addDropOption = false
	elseif unequip then
		context:addOption(getText("ContextMenu_Unequip"), items, ISInventoryPaneContextMenu.onUnEquip, player);
	end

	-- recipe dynamic context menu
	if recipe ~= nil then
		ISInventoryPaneContextMenu.addDynamicalContextMenu(itemsCraft[1], context, recipe, player, containerList);
    end
	if canBeActivated ~= nil and (not instanceof(canBeActivated, "Drainable") or canBeActivated:getUsedDelta() > 0) then
        if (canBeActivated:getType() ~= "CandleLit") then
            local txt = getText("ContextMenu_Turn_On");
            if canBeActivated:isActivated() then
                txt = getText("ContextMenu_Turn_Off");
            end
            context:addOption(txt, canBeActivated, ISInventoryPaneContextMenu.onActivateItem, player);
        end
	end
	if isAllBandage then
        ISInventoryPaneContextMenu.doBandageMenu(context, items, player);
	end
	-- dry yourself with a towel
	if canBeDry then
		context:addOption(getText("ContextMenu_Dry_myself"), items, ISInventoryPaneContextMenu.onDryMyself, player);
    end
    if hairDye and playerObj:getHumanVisual():getHairModel() and playerObj:getHumanVisual():getHairModel() ~= "Bald" then
        context:addOption(getText("ContextMenu_DyeHair"), hairDye, ISInventoryPaneContextMenu.onDyeHair, playerObj, false);
    end
    if hairDye and playerObj:getHumanVisual():getBeardModel() and playerObj:getHumanVisual():getBeardModel() ~= "" then
        context:addOption(getText("ContextMenu_DyeBeard"), hairDye, ISInventoryPaneContextMenu.onDyeHair, playerObj, true);
    end
    if makeup then
        ISInventoryPaneContextMenu.doMakeUpMenu(context, makeup, playerObj)
    end
    if isInPlayerInventory and addDropOption and playerObj:getJoypadBind() == -1 and
            not ISInventoryPaneContextMenu.isAllFav(items) and
            not ISInventoryPaneContextMenu.isAllNoDropMoveable(items) then
        context:addOption(getText("ContextMenu_Drop"), items, ISInventoryPaneContextMenu.onDropItems, player);
    end

    ISInventoryPaneContextMenu.doPlace3DItemOption(items, playerObj, context)

    if brokenObject then
        local fixingList = FixingManager.getFixes(brokenObject);
        if not fixingList:isEmpty() then
            local fixOption = context:addOption(getText("ContextMenu_Repair") .. getItemNameFromFullType(brokenObject:getFullType()), items, nil);
            local subMenuFix = ISContextMenu:getNew(context);
            context:addSubMenu(fixOption, subMenuFix);
            for i=0,fixingList:size()-1 do
                ISInventoryPaneContextMenu.buildFixingMenu(brokenObject, player, fixingList:get(i), fixOption, subMenuFix)
            end
        end
    end
    if alarmClock and alarmClock:isDigital() then
        if alarmClock:isRinging() then
            context:addOption(getText("ContextMenu_StopAlarm"), alarmClock, ISInventoryPaneContextMenu.onStopAlarm, player);
        end
        context:addOption(getText("ContextMenu_SetAlarm"), alarmClock, ISInventoryPaneContextMenu.onSetAlarm, player);
    end
    if clothingItemExtra then
        ISInventoryPaneContextMenu.doClothingItemExtraMenu(context, clothingItemExtra, playerObj);
    end
    if canBeRenamed then
        context:addOption(getText("ContextMenu_RenameBag"), canBeRenamed, ISInventoryPaneContextMenu.onRenameBag, player);
    end
    if canBeRenamedFood then
        context:addOption(getText("ContextMenu_RenameFood") .. canBeRenamedFood:getName(), canBeRenamedFood, ISInventoryPaneContextMenu.onRenameFood, player);
    end
    if canBeWrite then
		local editable = playerInv:containsTagRecurse("Write")
		if canBeWrite:getLockedBy() and canBeWrite:getLockedBy() ~= playerObj:getUsername() then
			editable = false
		end
		if not editable then
			context:addOption(getText("ContextMenu_Read_Note", canBeWrite:getName()), canBeWrite, ISInventoryPaneContextMenu.onWriteSomething, false, player);
		else
			context:addOption(getText("ContextMenu_Write_Note", canBeWrite:getName()), canBeWrite, ISInventoryPaneContextMenu.onWriteSomething, true, player);
		end
    end
    if map then
        context:addOption(getText("ContextMenu_CheckMap"), map, ISInventoryPaneContextMenu.onCheckMap, player);
        context:addOption(getText("ContextMenu_RenameMap"), map, ISInventoryPaneContextMenu.onRenameMap, player);
    end

--	local carBatteryCharger = playerObj:getInventory():getItemFromType("CarBatteryCharger")
	if carBatteryCharger then
		context:addOption(getText("ContextMenu_CarBatteryCharger_Place"), playerObj, ISInventoryPaneContextMenu.onPlaceCarBatteryCharger, carBatteryCharger)
    end
    if clothingRecipe then
        ISInventoryPaneContextMenu.doClothingRecipeMenu(playerObj, clothingRecipe, items, context);
    end
    
    ISHotbar.doMenuFromInventory(player, testItem, context);

    -- use the event (as you would 'OnTick' etc) to add items to context menu without mod conflicts.
    triggerEvent("OnFillInventoryObjectContextMenu", player, context, items);

    return context;
end

ISInventoryPaneContextMenu.createMenuNoItems = function(playerNum, isLoot, x, y)

    if ISInventoryPaneContextMenu.dontCreateMenu then return end

    if UIManager.getSpeedControls():getCurrentGameSpeed() == 0 then return end

    local playerObj = getSpecificPlayer(playerNum)
    
    local loot = getPlayerLoot(playerNum)

    local context = ISContextMenu.get(playerNum, x, y)

    triggerEvent("OnPreFillInventoryContextMenuNoItems", playerNum, context, isLoot)

    if isLoot and playerObj:getJoypadBind() ~= -1 then
        ISInventoryPaneContextMenu.doStoveMenu(context, playerNum)
        ISInventoryPaneContextMenu.doTrashCanMenu(context, playerNum)
    end

    triggerEvent("OnFillInventoryContextMenuNoItems", playerNum, context, isLoot)

    if context.numOptions == 1 then
        context:setVisible(false)
        return nil
    end

    return context
end

function ISInventoryPaneContextMenu.doStoveMenu(context, playerNum)
    local loot = getPlayerLoot(playerNum)
    
    -- Microwave, Stove, ClothingWasher, ClothingDryer
    if loot.toggleStove:isVisible() then
        context:addOption(loot.toggleStove.title, loot, ISInventoryPage.toggleStove)
    end

    if loot.inventoryPane.inventory and getCore():getGameMode() ~= "LastStand" then
        local stove = loot.inventoryPane.inventory:getParent()
        if instanceof(stove, "IsoStove") and stove:getContainer() and stove:getContainer():isPowered() then
            if stove:getContainer():getType() == "microwave" then
                context:addOption(getText("ContextMenu_StoveSetting"), nil, ISWorldObjectContextMenu.onMicrowaveSetting, stove, playerNum)
            elseif stove:getContainer():getType() == "stove" then
                context:addOption(getText("ContextMenu_StoveSetting"), nil, ISWorldObjectContextMenu.onStoveSetting, stove, playerNum)
            end
        end
    end
end

function ISInventoryPaneContextMenu.doTrashCanMenu(context, playerNum)
    local loot = getPlayerLoot(playerNum)

    if loot.removeAll:isVisible() then
        context:addOption(loot.removeAll.title, loot, ISInventoryPage.removeAll)
    end
end

function ISInventoryPaneContextMenu.doLiteratureMenu(context, items, player)
    local readOption = context:addOption(getText("ContextMenu_Read"), items, ISInventoryPaneContextMenu.onLiteratureItems, player);
    if getSpecificPlayer(player):isAsleep() then
        readOption.notAvailable = true;
        local tooltip = ISInventoryPaneContextMenu.addToolTip();
        tooltip.description = getText("ContextMenu_NoOptionSleeping");
        readOption.toolTip = tooltip;
    end
end
    
function ISInventoryPaneContextMenu.doBandageMenu(context, items, player)
    -- we get all the damaged body part
    local bodyPartDamaged = ISInventoryPaneContextMenu.haveDamagePart(player);
    -- if any part is damaged, we gonna create a sub menu with them
    if #bodyPartDamaged > 0 then
        local bandageOption = context:addOption(getText("ContextMenu_Apply_Bandage"), bodyPartDamaged, nil);
        -- create a new contextual menu
        local subMenuBandage = context:getNew(context);
        -- we add our new menu to the option we want (here bandage)
        context:addSubMenu(bandageOption, subMenuBandage);
        for i,v in ipairs(bodyPartDamaged) do
            subMenuBandage:addOption(BodyPartType.getDisplayName(v:getType()), items, ISInventoryPaneContextMenu.onApplyBandage, v, player);
        end
    end
end

function ISInventoryPaneContextMenu.canRipItem(playerObj, item)
    if playerObj:isEquippedClothing(item) or item:isFavorite() then
        return false
    end
    if item:getFabricType() and instanceof(item, "Clothing") then
        local fabricType = ClothingRecipesDefinitions["FabricType"][item:getFabricType()]
        if not fabricType then
            return false
        end
        if fabricType.tools and not playerObj:getInventory():getItemFromType(fabricType.tools, true, true) then
            return false
        end
        return true
    end
    if ClothingRecipesDefinitions[item:getType()] then
        return true
    end
    return false
end

ISInventoryPaneContextMenu.doClothingRecipeMenu = function(playerObj, clothing, items, context)
	if true then return end -- this is handled by recipes now

    -- check if we need tools for this
    if clothing:getFabricType() and instanceof(clothing, "Clothing") then
        local option = context:addOption(getRecipeDisplayName("Rip clothing"), playerObj, ISInventoryPaneContextMenu.onRipClothing, items)
        -- Item is favourited, add tooltip
        if clothing:isFavorite() then
            local tooltip = ISInventoryPaneContextMenu.addToolTip();
            tooltip.description = getText("ContextMenu_CantRipFavourite");
            option.toolTip = tooltip;
        end
        -- Tool is needed
        local tools = ClothingRecipesDefinitions["FabricType"][clothing:getFabricType()].tools;
        if tools and not playerObj:getInventory():getItemFromType(tools, true, true) then
            option.notAvailable = true;
            local tooltip = ISInventoryPaneContextMenu.addToolTip();
            local toolItem = InventoryItemFactory.CreateItem(tools);
            tooltip.description = getText("ContextMenu_Require", toolItem:getDisplayName());
            option.toolTip = tooltip;
            return;
        end
        if not ClothingRecipesDefinitions["FabricType"][clothing:getFabricType()].noSheetRope then
            context:addOption(getRecipeDisplayName("Craft Sheet Rope"), playerObj, ISInventoryPaneContextMenu.onCraftSheetRope, items)
        end
        return;
    else
        context:addOption(getRecipeDisplayName("Rip sheets"), playerObj, ISInventoryPaneContextMenu.onRipClothing, items)
    end
    context:addOption(getRecipeDisplayName("Craft Sheet Rope"), playerObj, ISInventoryPaneContextMenu.onCraftSheetRope, items)
end

ISInventoryPaneContextMenu.onInspectClothing = function(playerObj, clothing)
	if luautils.haveToBeTransfered(playerObj, clothing) then
		local action = ISInventoryTransferAction:new(playerObj, clothing, clothing:getContainer(), playerObj:getInventory())
		action:setOnComplete(ISInventoryPaneContextMenu.onInspectClothingUI, playerObj, clothing)
		ISTimedActionQueue.add(action)
	else
		ISInventoryPaneContextMenu.onInspectClothingUI(playerObj, clothing)
	end
end

ISInventoryPaneContextMenu.onInspectClothingUI = function(player, clothing)
    local playerNum = player:getPlayerNum()
    if ISGarmentUI.windows[playerNum] then
        ISGarmentUI.windows[playerNum]:close();
    end
    local window = ISGarmentUI:new(-1, 500, player, clothing);
    window:initialise();
    window:addToUIManager();
    ISGarmentUI.windows[playerNum] = window
    if JoypadState.players[playerNum+1] then
        window.prevFocus = JoypadState.players[playerNum+1].focus
        setJoypadFocus(playerNum, window)
    end
end

ISInventoryPaneContextMenu.doClothingPatchMenu = function(player, clothing, context)
    local playerObj = getSpecificPlayer(player);

--    if clothing:getHolesNumber() == 0 then
--        return;
--    end

    if not clothing:getFabricType() then
        return;
    end
    

    -- you need thread and needle
    local thread = playerObj:getInventory():getItemFromType("Thread", true, true);
    local needle = playerObj:getInventory():getItemFromType("Needle", true, true);
    local fabric1 = playerObj:getInventory():getItemFromType("RippedSheets", true, true);
    local fabric2 = playerObj:getInventory():getItemFromType("DenimStrips", true, true);
    local fabric3 = playerObj:getInventory():getItemFromType("LeatherStrips", true, true);
    if not thread or not needle or (not fabric1 and not fabric2 and not fabric3) then
        local patchOption = context:addOption(getText("ContextMenu_Patch"));
        patchOption.notAvailable = true;
        local tooltip = ISInventoryPaneContextMenu.addToolTip();
        tooltip.description = getText("ContextMenu_CantRepair");
        patchOption.toolTip = tooltip;
        return;
    end

    local patchOption = context:addOption(getText("ContextMenu_Patch"));
    local subMenuPatch = context:getNew(context);
    context:addSubMenu(patchOption, subMenuPatch);
    
    -- we first gonna display repair, then upgrade then remove patches
    local repairOption = subMenuPatch:addOption(getText("ContextMenu_PatchHole"));
    local subMenuRepair = context:getNew(subMenuPatch);
    subMenuPatch:addSubMenu(repairOption, subMenuRepair);

    local upgradeOption = subMenuPatch:addOption(getText("ContextMenu_AddPadding"));
    local subMenuUpgrade = context:getNew(subMenuPatch);
    subMenuPatch:addSubMenu(upgradeOption, subMenuUpgrade);
    
    local removeOption = subMenuPatch:addOption(getText("ContextMenu_RemovePatch"));
    local subMenuRemove = context:getNew(subMenuPatch);
    subMenuPatch:addSubMenu(removeOption, subMenuRemove);
    
    local coveredParts = clothing:getCoveredParts();
    for i=0, coveredParts:size() - 1 do
        local part = coveredParts:get(i);
        local hole = clothing:getVisual():getHole(part);
        local subMenuToUse = subMenuUpgrade;
--        if hole and hole > 0 then
        local text = part:getDisplayName();
        if hole and hole > 0 then
            subMenuToUse = subMenuRepair;
        end
    
        -- removing patch
        local removePatch;
        local patch = clothing:getPatchType(part);
        if patch then
            removePatch = true;
            local option = subMenuRemove:addOption(patch:getFabricTypeName(), playerObj, ISInventoryPaneContextMenu.removePatch, clothing, part, thread, needle)
            local tooltip = ISInventoryPaneContextMenu.addToolTip();
            tooltip.description = getText("Tooltip_GetPatchBack", ISRemovePatch.chanceToGetPatchBack(playerObj)) .. " <RGB:1,0,0> " .. getText("Tooltip_ScratchDefense")  .. " -" .. patch:getScratchDefense() .. " <LINE> " .. getText("Tooltip_BiteDefense") .. " -" .. patch:getBiteDefense();
            option.toolTip = tooltip;
        else -- adding patch
            local partOption = subMenuToUse:addOption(text);
            local subMenuPart = context:getNew(subMenuToUse);
            subMenuToUse:addSubMenu(partOption, subMenuPart);
        
            if fabric1 then
                local option = subMenuPart:addOption(fabric1:getDisplayName(), playerObj, ISInventoryPaneContextMenu.repairClothing, clothing, part, fabric1, thread, needle)
                local tooltip = ISInventoryPaneContextMenu.addToolTip();
                if clothing:canFullyRestore(playerObj, part, fabric1) then
                    tooltip.description = getText("IGUI_perks_Tailoring") .. " :" .. playerObj:getPerkLevel(Perks.Tailoring) .. " <LINE> <RGB:0,1,0> " .. getText("Tooltip_FullyRestore");
                else
                    tooltip.description = getText("IGUI_perks_Tailoring") .. " :" .. playerObj:getPerkLevel(Perks.Tailoring) .. " <LINE> <RGB:0,1,0> " .. getText("Tooltip_ScratchDefense")  .. " +" .. Clothing.getScratchDefenseFromItem(playerObj, fabric1) .. " <LINE> " .. getText("Tooltip_BiteDefense") .. " +" .. Clothing.getBiteDefenseFromItem(playerObj, fabric1);
                end
                option.toolTip = tooltip;
            end
            if fabric2 then
                local option = subMenuPart:addOption(fabric2:getDisplayName(), playerObj, ISInventoryPaneContextMenu.repairClothing, clothing, part, fabric2, thread, needle)
                local tooltip = ISInventoryPaneContextMenu.addToolTip();
                if clothing:canFullyRestore(playerObj, part, fabric2) then
                    tooltip.description = getText("IGUI_perks_Tailoring") .. " :" .. playerObj:getPerkLevel(Perks.Tailoring) .. " <LINE> <RGB:0,1,0> " .. getText("Tooltip_FullyRestore");
                else
                    tooltip.description = getText("IGUI_perks_Tailoring") .. " :" .. playerObj:getPerkLevel(Perks.Tailoring) .. " <LINE>  <RGB:0,1,0> " .. getText("Tooltip_ScratchDefense")  .. " +" .. Clothing.getScratchDefenseFromItem(playerObj, fabric2) .. " <LINE> " .. getText("Tooltip_BiteDefense") .. " +" .. Clothing.getBiteDefenseFromItem(playerObj, fabric2);
                end
                option.toolTip = tooltip;
            end
            if fabric3 then
                local option = subMenuPart:addOption(fabric3:getDisplayName(), playerObj, ISInventoryPaneContextMenu.repairClothing, clothing, part, fabric3, thread, needle)
                local tooltip = ISInventoryPaneContextMenu.addToolTip();
                if clothing:canFullyRestore(playerObj, part, fabric3) then
                    tooltip.description = getText("IGUI_perks_Tailoring") .. " :" .. playerObj:getPerkLevel(Perks.Tailoring) .. " <LINE> <RGB:0,1,0> " .. getText("Tooltip_FullyRestore");
                else
                    tooltip.description = getText("IGUI_perks_Tailoring") .. " :" .. playerObj:getPerkLevel(Perks.Tailoring) .. " <LINE>  <RGB:0,1,0> " .. getText("Tooltip_ScratchDefense")  .. " +" .. Clothing.getScratchDefenseFromItem(playerObj, fabric3) .. " <LINE> " .. getText("Tooltip_BiteDefense") .. " +" .. Clothing.getBiteDefenseFromItem(playerObj, fabric3);
                end
                option.toolTip = tooltip;
            end
        end
--        end
    end

    if #subMenuRepair.options == 0 then
        repairOption.subOption = nil;
        repairOption.notAvailable = true;
        local tooltip = ISInventoryPaneContextMenu.addToolTip();
        tooltip.description = getText("Tooltip_NothingToRepair");
        repairOption.toolTip = tooltip;
    end
    
    if #subMenuRemove.options == 0 then
        removeOption.subOption = nil;
        removeOption.notAvailable = true;
        local tooltip = ISInventoryPaneContextMenu.addToolTip();
        tooltip.description = getText("Tooltip_NothingToRemove");
        removeOption.toolTip = tooltip;
    end
    
    if #subMenuUpgrade.options == 0 then
        upgradeOption.subOption = nil;
        upgradeOption.notAvailable = true;
        local tooltip = ISInventoryPaneContextMenu.addToolTip();
        tooltip.description = getText("Tooltip_NothingToUpgrade");
        upgradeOption.toolTip = tooltip;
    end
end

ISInventoryPaneContextMenu.removePatch = function(player, clothing, part, needle)
    if luautils.haveToBeTransfered(player, needle) then
        ISTimedActionQueue.add(ISInventoryTransferAction:new(player, needle, needle:getContainer(), player:getInventory()))
    end
    if luautils.haveToBeTransfered(player, clothing) then
        ISTimedActionQueue.add(ISInventoryTransferAction:new(player, clothing, clothing:getContainer(), player:getInventory()))
    end
    
    ISTimedActionQueue.add(ISRemovePatch:new(player, clothing, part, needle));
end

ISInventoryPaneContextMenu.removeAllPatches = function(player, clothing, parts, needle)
    for i=1, #parts do
        local part = parts[i];
        local patch = clothing:getPatchType(part);
        if patch then
            ISInventoryPaneContextMenu.removePatch(player, clothing, part, needle);
        end
    end
end

ISInventoryPaneContextMenu.repairClothing = function(player, clothing, part, fabric, thread, needle)
    -- if you piled up tailor job we ensure we get a correct fabric
    if fabric == nil then fabric = player:getInventory():getItemFromType(fabric:getType(), true, true); end
    if thread == nil then thread = player:getInventory():getItemFromType(thread:getType(), true, true); end

    if fabric == nil or thread == nil then return end
    if luautils.haveToBeTransfered(player, fabric) then
        ISTimedActionQueue.add(ISInventoryTransferAction:new(player, fabric, fabric:getContainer(), player:getInventory()))
    end
    if luautils.haveToBeTransfered(player, thread) then
        ISTimedActionQueue.add(ISInventoryTransferAction:new(player, thread, thread:getContainer(), player:getInventory()))
    end
    if luautils.haveToBeTransfered(player, needle) then
        ISTimedActionQueue.add(ISInventoryTransferAction:new(player, needle, needle:getContainer(), player:getInventory()))
    end
    if luautils.haveToBeTransfered(player, clothing) then
        ISTimedActionQueue.add(ISInventoryTransferAction:new(player, clothing, clothing:getContainer(), player:getInventory()))
    end
    
    ISTimedActionQueue.add(ISRepairClothing:new(player, clothing, part, fabric, thread, needle));
end

ISInventoryPaneContextMenu.repairAllClothing = function(player, clothing, parts, fabric, thread, needle, onlyHoles)

    local fabricArray = player:getInventory():getItemsFromType(fabric:getType(), true);
    local fabricCount = player:getInventory():getItemCount(fabric:getType(), true);
    local threadArray = player:getInventory():getItemsFromType(thread:getType(), true);
    local threadCount = player:getInventory():getItemCount(thread:getType(), true);

    local successfulActionsAdded = 0;
    local currentThreadUsed = 0;

    for i=1, #parts do

        local part = parts[i];
        local hole = clothing:getVisual():getHole(part) > 0;
        local patch = clothing:getPatchType(part);

        -- Amendment to avoid error when the thread's uses are over
        if (successfulActionsAdded > 0) and ((threadArray:get(currentThreadUsed):getUsedDelta() - (successfulActionsAdded * 0.1)) < 0.1) then
            currentThreadUsed = currentThreadUsed + 1;
            if(currentThreadUsed >= threadCount) then return; end
        end

        if hole and onlyHoles then -- Patch all holes
            ISInventoryPaneContextMenu.repairClothing(player, clothing, part, fabricArray:get(successfulActionsAdded), threadArray:get(currentThreadUsed), needle);
            successfulActionsAdded = successfulActionsAdded + 1;
        elseif (not patch) and (not hole) and (not onlyHoles) then -- Pad every non-hole
            ISInventoryPaneContextMenu.repairClothing(player, clothing, part, fabricArray:get(successfulActionsAdded), threadArray:get(currentThreadUsed), needle);
            successfulActionsAdded = successfulActionsAdded + 1;
        end

        if(successfulActionsAdded >= fabricCount) then return; end
    end

end

ISInventoryPaneContextMenu.doWearClothingTooltip = function(playerObj, newItem, currentItem, option)
	local replaceItems = {};
	local previousBiteDefense = 0;
	local previousScratchDefense = 0;
	local previousCombatModifier = 0;
	local wornItems = playerObj:getWornItems()
	local bodyLocationGroup = wornItems:getBodyLocationGroup()
	local location = newItem:IsClothing() and newItem:getBodyLocation() or newItem:canBeEquipped()

	for i=1,wornItems:size() do
		local wornItem = wornItems:get(i-1)
		local item = wornItem:getItem()
		if (newItem:getBodyLocation() == wornItem:getLocation()) or bodyLocationGroup:isExclusive(location, wornItem:getLocation()) then
			if item ~= newItem and item ~= currentItem then
				table.insert(replaceItems, item);
			end
			if item:IsClothing() then
				previousBiteDefense = previousBiteDefense + item:getBiteDefense();
				previousScratchDefense = previousScratchDefense + item:getScratchDefense();
				previousCombatModifier = previousCombatModifier + item:getCombatSpeedModifier();
			end
		end
	end

	local newBiteDefense = 0;
	local newScratchDefense = 0;
	local newCombatModifier = 0;

	if newItem:IsClothing() then
		newBiteDefense = newItem:getBiteDefense();
		newScratchDefense = newItem:getScratchDefense();
		newCombatModifier = newItem:getCombatSpeedModifier();
	end

	if #replaceItems == 0 and newBiteDefense == 0 and newScratchDefense == 0 and previousBiteDefense == 0 and previousScratchDefense == 0 then
		return nil
	end
	
	local tooltip = ISInventoryPaneContextMenu.addToolTip();
	tooltip.maxLineWidth = 1000

	if #replaceItems > 0 then
		tooltip.description = tooltip.description .. getText("Tooltip_ReplaceWornItems") .. " <LINE> <INDENT:20> ";
		for _,item in ipairs(replaceItems) do
			tooltip.description = tooltip.description .. item:getDisplayName() .. " <LINE> ";
		end
		tooltip.description = tooltip.description .. " <INDENT:0> ";
	end

	local font = ISToolTip.GetFont()

	local labelWidth = 0
	labelWidth = math.max(labelWidth, getTextManager():MeasureStringX(font, getText("Tooltip_BiteDefense") .. ":"));
	labelWidth = math.max(labelWidth, getTextManager():MeasureStringX(font, getText("Tooltip_ScratchDefense") .. ":"));
--	labelWidth = math.max(labelWidth, getTextManager():MeasureStringX(font, getText("Tooltip_CombatSpeed") .. ":"));

	local text;

	-- bite defense
	if newBiteDefense > 0 or previousBiteDefense > 0 then
		local r,g,b = 0,1,0;
		local plus = "+";
		if previousBiteDefense > 0 and previousBiteDefense > newBiteDefense then
			r,g,b = 1,0,0;
			plus = "";
		end
		text = string.format(" <RGB:%.2f,%.2f,%.2f> %s: <SETX:%d> %d (%s%d) <LINE> ",
			r, g, b, getText("Tooltip_BiteDefense"), labelWidth + 10,
			newBiteDefense,
			plus,
			newBiteDefense - previousBiteDefense);
		tooltip.description = tooltip.description .. text;
	end
	
	-- scratch defense
	if newScratchDefense > 0 or previousScratchDefense > 0 then
		local r,g,b = 0,1,0;
		local plus = "+";
		if previousScratchDefense > 0 and previousScratchDefense > newScratchDefense then
			r,g,b = 1,0,0;
			plus = "";
		end
		text = string.format(" <RGB:%.2f,%.2f,%.2f> %s: <SETX:%d> %d (%s%d) <LINE> ",
			r, g, b, getText("Tooltip_ScratchDefense"), labelWidth + 10,
			newScratchDefense,
			plus,
			newScratchDefense - previousScratchDefense);
		tooltip.description = tooltip.description .. text;
	end

--[[
	-- combat speed -- TODO: Better calcul!
	if previousCombatModifier > 0 and previousCombatModifier > newCombatModifier then
		text = " <RGB:0,1,0> " .. getText("Tooltip_CombatSpeed") .. ": +";
		text = " <RGB:1,0,0> " .. getText("Tooltip_CombatSpeed") .. ": ";
	end
	tooltip.description = tooltip.description ..  text .. newCombatModifier-previousCombatModifier;
--]]

	option.toolTip = tooltip;

	return replaceItems;
end

ISInventoryPaneContextMenu.doWearClothingMenu = function(player, clothing, items, context)
    -- extra submenu generate the "Wear" submenu in doClothingItemExtraMenu
    if clothing:getClothingExtraSubmenu() then
        return
    end

    local playerObj = getSpecificPlayer(player);

    local option = context:addOption(getText("ContextMenu_Wear"), items, ISInventoryPaneContextMenu.onWearItems, player);
    ISInventoryPaneContextMenu.doWearClothingTooltip(playerObj, clothing, clothing, option);
end

ISInventoryPaneContextMenu.doChangeFireModeMenu = function(playerObj, weapon, context)
    local firemodeOption = context:addOption(getText("ContextMenu_ChangeFireMode"))
    local subMenuFiremode = context:getNew(context)
    context:addSubMenu(firemodeOption, subMenuFiremode)
    for i=0, weapon:getFireModePossibilities():size() - 1 do
        local firemode = weapon:getFireModePossibilities():get(i);
        if firemode ~= weapon:getFireMode() then
            subMenuFiremode:addOption(getText("ContextMenu_FireMode_" .. firemode), playerObj, ISInventoryPaneContextMenu.onChangefiremode, weapon, firemode);
        end
    end
end

ISInventoryPaneContextMenu.onChangefiremode = function(playerObj, weapon, newfiremode)
    weapon:setFireMode(newfiremode);
    playerObj:setVariable("FireMode", newfiremode);
    if "Burst" == newfiremode then
        weapon:setAmmoPerShoot(3);
    end
end

ISInventoryPaneContextMenu.doReloadMenuForBullets = function(playerObj, bullet, context)
    for i=0, playerObj:getInventory():getItems():size()-1 do
        -- test magazines
        local item = playerObj:getInventory():getItems():get(i);
        if not instanceof(item, "HandWeapon") and item:getAmmoType() == bullet:getFullType() then
            if item:getCurrentAmmoCount() < item:getMaxAmmo() then
                local ammoCount = playerObj:getInventory():getItemCountRecurse(item:getAmmoType())
                if ammoCount > item:getMaxAmmo() then
                    ammoCount = item:getMaxAmmo()
                end
                if ammoCount > item:getMaxAmmo() - item:getCurrentAmmoCount() then
                    ammoCount = item:getMaxAmmo() - item:getCurrentAmmoCount()
                end
                local insertOption = context:addOption(getText("ContextMenu_InsertBulletsInMagazine", ammoCount), playerObj, ISInventoryPaneContextMenu.onLoadBulletsInMagazine, item, ammoCount)
                local tooltip = ISInventoryPaneContextMenu.addToolTip();
                local toolItem = InventoryItemFactory.CreateItem(item:getGunType());
                tooltip.description =
                        (getText("ContextMenu_Magazine") .. ": " .. getText(item:getDisplayName()) .. "\n"..
                        getText("ContextMenu_GunType") .. ": " .. getText(toolItem:getDisplayName()) .. "\n" ..
                        getText("Tooltip_weapon_AmmoCount") .. ": " .. item:getCurrentAmmoCount() .. "/" .. item:getMaxAmmo());
                insertOption.toolTip = tooltip;
            end
        elseif instanceof(item, "HandWeapon") and not item:getMagazineType() and item:getAmmoType() == bullet:getFullType() then
            ISInventoryPaneContextMenu.doBulletMenu(playerObj, item, context)
        end
    end
end

ISInventoryPaneContextMenu.doReloadMenuForMagazine = function(playerObj, magazine, context)
    local weapons = playerObj:getInventory():getItemsFromCategory("Weapon");
    for i=1,weapons:size() do
        local weapon = weapons:get(i-1)
        if weapon:getMagazineType() == magazine:getFullType() and not weapon:isContainsClip() then
            local insertOption = context:addOption(getText("ContextMenu_InsertMagazine"), playerObj, ISInventoryPaneContextMenu.onInsertMagazine, weapon, magazine);
            local tooltip = ISInventoryPaneContextMenu.addToolTip();
            tooltip.description = (getText("ContextMenu_GunType") .. ": " .. getText(weapon:getDisplayName()));
            insertOption.toolTip = tooltip;
        end
    end
end

ISInventoryPaneContextMenu.doBulletMenu = function(playerObj, weapon, context)
    local bulletAvail = playerObj:getInventory():getItemCountRecurse(weapon:getAmmoType());
    local bulletNeeded = weapon:getMaxAmmo() - weapon:getCurrentAmmoCount();
    local bulletName = getScriptManager():FindItem(weapon:getAmmoType()):getDisplayName();
    if bullets == 0 then
        bulletNeeded = 0;
    end
    if bulletNeeded > bulletAvail then
        bulletNeeded = bulletAvail;
    end
    local insertOption = context:addOption(getText("ContextMenu_InsertBullets", bulletNeeded, bulletName, weapon:getDisplayName()), playerObj, ISInventoryPaneContextMenu.onLoadBulletsIntoFirearm, weapon);
    if bulletNeeded <= 0 then
        insertOption.notAvailable = true;
    end

    if weapon:getCurrentAmmoCount() > 0 then
        context:addOption(getText("ContextMenu_UnloadRounds", weapon:getDisplayName()), playerObj, ISInventoryPaneContextMenu.onUnloadBulletsFromFirearm, weapon);
    end
end

ISInventoryPaneContextMenu.doReloadMenuForWeapon = function(playerObj, weapon, context)
    if weapon:getMagazineType() then
        if weapon:isContainsClip() then -- eject current clip
            context:addOption(getText("ContextMenu_EjectMagazine"), playerObj, ISInventoryPaneContextMenu.onEjectMagazine, weapon);
        else -- insert a new clip
            local clip = weapon:getBestMagazine(playerObj);
            local insertOption = context:addOption(getText("ContextMenu_InsertMagazine"), playerObj, ISInventoryPaneContextMenu.onInsertMagazine, weapon, clip);
            if not clip then
                local clip = InventoryItemFactory.CreateItem(weapon:getMagazineType());
                insertOption.notAvailable = true;
                local tooltip = ISInventoryPaneContextMenu.addToolTip();
                tooltip.description = getText("ContextMenu_NoMagazineFound", clip:getDisplayName());
                insertOption.toolTip = tooltip;
            else
                local tooltip = ISInventoryPaneContextMenu.addToolTip();
                tooltip.description = (getText("ContextMenu_Magazine") .. ": " .. getText(clip:getDisplayName()));
                insertOption.toolTip = tooltip
            end
        end
    elseif weapon:getAmmoType() then
        ISInventoryPaneContextMenu.doBulletMenu(playerObj, weapon, context)
    end
    if weapon:isJammed() then -- unjam
        context:addOption(getText("ContextMenu_Unjam", weapon:getDisplayName()), playerObj, ISInventoryPaneContextMenu.onRackGun, weapon);
    elseif ISReloadWeaponAction.canRack(weapon) then
        local text = weapon:haveChamber() and "ContextMenu_Rack" or "ContextMenu_UnloadRoundFrom"
        context:addOption(getText(text, weapon:getDisplayName()), playerObj, ISInventoryPaneContextMenu.onRackGun, weapon);
    end
end

function ISInventoryPaneContextMenu.transferIfNeeded(playerObj, item)
	if instanceof(item, "InventoryItem") then
		if luautils.haveToBeTransfered(playerObj, item) then
			ISTimedActionQueue.add(ISInventoryTransferAction:new(playerObj, item, item:getContainer(), playerObj:getInventory()))
		end
	elseif instanceof(item, "ArrayList") then
		local items = item
		for i=1,items:size() do
			local item = items:get(i-1)
			if luautils.haveToBeTransfered(playerObj, item) then
				ISTimedActionQueue.add(ISInventoryTransferAction:new(playerObj, item, item:getContainer(), playerObj:getInventory()))
			end
		end
	end
end

ISInventoryPaneContextMenu.onEjectMagazine = function(playerObj, weapon)
    ISInventoryPaneContextMenu.equipWeapon(weapon, true, false, playerObj:getPlayerNum())
    ISTimedActionQueue.add(ISEjectMagazine:new(playerObj, weapon));
end

ISInventoryPaneContextMenu.transferBullets = function(playerObj, ammoType, currentAmmo, maxAmmo)
    local inventory = playerObj:getInventory()
    local ammoCount = inventory:getItemCountRecurse(ammoType)
    ammoCount = math.min(ammoCount, maxAmmo - currentAmmo)
    if ammoCount <= 0 then return 0 end
    local items = inventory:getSomeTypeRecurse(ammoType, ammoCount)
    ISInventoryPaneContextMenu.transferIfNeeded(playerObj, items)
    return ammoCount
end

ISInventoryPaneContextMenu.onInsertMagazine = function(playerObj, weapon, magazine)
    ISInventoryPaneContextMenu.transferIfNeeded(playerObj, magazine)
    ISInventoryPaneContextMenu.equipWeapon(weapon, true, false, playerObj:getPlayerNum())
    ISTimedActionQueue.add(ISInsertMagazine:new(playerObj, weapon, magazine));
end

ISInventoryPaneContextMenu.onRackGun = function(playerObj, weapon)
    ISInventoryPaneContextMenu.equipWeapon(weapon, true, false, playerObj:getPlayerNum())
    ISTimedActionQueue.add(ISRackFirearm:new(playerObj, weapon));
end

ISInventoryPaneContextMenu.onLoadBulletsIntoFirearm = function(playerObj, weapon)
    ISInventoryPaneContextMenu.transferBullets(playerObj, weapon:getAmmoType(), weapon:getCurrentAmmoCount(), weapon:getMaxAmmo())
    ISInventoryPaneContextMenu.equipWeapon(weapon, true, false, playerObj:getPlayerNum())
    ISTimedActionQueue.add(ISReloadWeaponAction:new(playerObj, weapon));
end

ISInventoryPaneContextMenu.onUnloadBulletsFromFirearm = function(playerObj, weapon)
    ISInventoryPaneContextMenu.equipWeapon(weapon, true, false, playerObj:getPlayerNum())
    ISTimedActionQueue.add(ISUnloadBulletsFromFirearm:new(playerObj, weapon))
end

ISInventoryPaneContextMenu.doMagazineMenu = function(playerObj, magazine, context)
    if magazine:getCurrentAmmoCount() < magazine:getMaxAmmo() then
        local ammoCount = playerObj:getInventory():getItemCountRecurse(magazine:getAmmoType());
        if ammoCount > magazine:getMaxAmmo() then
            ammoCount = magazine:getMaxAmmo();
        end
        if ammoCount > magazine:getMaxAmmo() - magazine:getCurrentAmmoCount() then
            ammoCount = magazine:getMaxAmmo() - magazine:getCurrentAmmoCount();
        end
        if ammoCount == 0 then
            local option = context:addOption(getText("ContextMenu_NoBullets", ammoCount));
            option.notAvailable = true;
        else
            context:addOption(getText("ContextMenu_InsertBulletsInMagazine", ammoCount), playerObj, ISInventoryPaneContextMenu.onLoadBulletsInMagazine, magazine, ammoCount);
        end
    end
    
    if magazine:getCurrentAmmoCount() > 0 then
        context:addOption(getText("ContextMenu_UnloadMagazine"), playerObj, ISInventoryPaneContextMenu.onUnloadBulletsFromMagazine, magazine);
    end
end

ISInventoryPaneContextMenu.onLoadBulletsInMagazine = function(playerObj, magazine, ammoCount)
    ISInventoryPaneContextMenu.transferIfNeeded(playerObj, magazine)
    local items = playerObj:getInventory():getSomeTypeRecurse(magazine:getAmmoType(), ammoCount)
    ISInventoryPaneContextMenu.transferIfNeeded(playerObj, items)
    if ammoCount > 0 then
        ISTimedActionQueue.add(ISLoadBulletsInMagazine:new(playerObj, magazine, ammoCount))
    end
end

ISInventoryPaneContextMenu.onUnloadBulletsFromMagazine = function(playerObj, magazine)
    ISInventoryPaneContextMenu.transferIfNeeded(playerObj, magazine)
    ISTimedActionQueue.add(ISUnloadBulletsFromMagazine:new(playerObj, magazine))
end

ISInventoryPaneContextMenu.getEvoItemCategories = function(items, evorecipe)
    local catList = {};
    for i=0,items:size() -1 do
        local evoItem = items:get(i);
        if instanceof(evoItem, "Food") then
			local foodType = evoItem:getFoodType();
			if foodType and evorecipe:needToBeCooked(evoItem) then
				if not catList[foodType] then catList[foodType] = {}; end
				table.insert(catList[foodType], evoItem);
			end
		end
    end
    return catList;
end

ISInventoryPaneContextMenu.onPlaceCarBatteryCharger = function(playerObj, carBatteryCharger)
	ISInventoryPaneContextMenu.transferIfNeeded(playerObj, carBatteryCharger)
	ISTimedActionQueue.add(ISPlaceCarBatteryChargerAction:new(playerObj, carBatteryCharger, 100))
end

ISInventoryPaneContextMenu.onRipClothing = function(playerObj, items)
	items = ISInventoryPane.getActualItems(items)
	local items2 = {}
	for _,item in ipairs(items) do
		if ISInventoryPaneContextMenu.canRipItem(playerObj, item) then
			ISInventoryPaneContextMenu.transferIfNeeded(playerObj, item)
			table.insert(items2, item)
        end
	end
	for _,item in ipairs(items2) do
        ISTimedActionQueue.add(ISRipClothing:new(playerObj, item))
	end
end


ISInventoryPaneContextMenu.onCraftSheetRope = function(playerObj, items)
	items = ISInventoryPane.getActualItems(items)
	local items2 = {}
	for _,item in ipairs(items) do
		if not playerObj:isEquippedClothing(item) and (ClothingRecipesDefinitions[item:getType()] or ClothingRecipesDefinitions["FabricType"][item:getFabricType()]) then
			ISInventoryPaneContextMenu.transferIfNeeded(playerObj, item)
			table.insert(items2, item)
		end
	end
	for _,item in ipairs(items2) do
		ISTimedActionQueue.add(ISRipClothing:new(playerObj, item, true))
	end
end

ISInventoryPaneContextMenu.addItemInEvoRecipe = function(subMenuRecipe, baseItem, evoItem, extraInfo, evorecipe2, player)
    local txt = getText("ContextMenu_From_Ingredient", evoItem:getName(), extraInfo);
    if evorecipe2:isResultItem(baseItem) then
        txt = getText("ContextMenu_Add_Ingredient", evoItem:getName(), extraInfo);
    end
    txt = string.trim(txt)
    local option = subMenuRecipe:addOption(txt, evorecipe2, ISInventoryPaneContextMenu.onAddItemInEvoRecipe, baseItem, evoItem, player);
    local tooltip = ISInventoryPaneContextMenu.addToolTip();
    if instanceof(evoItem,"Food") and evoItem:getFreezingTime() > 0 then
        option.notAvailable = true;
        tooltip.description = getText("ContextMenu_CantAddFrozenFood");
        option.toolTip = tooltip;
    end
    if not evorecipe2:needToBeCooked(evoItem) then
        option.notAvailable = true;
        tooltip.description = tooltip.description .. getText("ContextMenu_NeedCooked");
        option.toolTip = tooltip;
    end
end

local function formatFoodValue(f)
	return string.format("%+.2f", f)
end

ISInventoryPaneContextMenu.addEatTooltip = function(option, items, percent)
	local item = items[1]

	-- Figure out the fraction of the *remaining amount* that is used.
	-- If we already ate 1/4, and are eating 1/4 this time, then percentage=0.33.
	-- If we already ate 1/2 and are eating 1/2 this time, then percentage=1.0.
	if (item:getBaseHunger() ~= 0.0) and (item:getHungChange() ~= 0.0) then
		local hungChange = item:getBaseHunger() * percent
		local usedPercent = hungChange / item:getHungChange()
		percent = PZMath.clamp_01(usedPercent)
	end

	local texts = {}
	if item:getHungerChange() ~= 0.0 then
		table.insert(texts, getText("Tooltip_food_Hunger"))
		table.insert(texts, formatFoodValue(item:getHungerChange() * 100.0 * percent))
		table.insert(texts, true)
	end
	if item:getThirstChange() ~= 0.0 then
		table.insert(texts, getText("Tooltip_food_Thirst"))
		table.insert(texts, formatFoodValue(item:getThirstChange() * 100.0 * percent))
		table.insert(texts, item:getThirstChange() < 0)
	end
	if item:getUnhappyChange() ~= 0.0 then
		table.insert(texts, getText("Tooltip_food_Unhappiness"))
		table.insert(texts, formatFoodValue(item:getUnhappyChange() * percent))
		table.insert(texts, item:getUnhappyChange() < 0)
	end

	local notes = {}
	if item:isbDangerousUncooked() and not item:isCooked() and not item:isBurnt() then
		table.insert(notes, getText("Tooltip_food_Dangerous_uncooked"))
		table.insert(notes, false)
	end
	if (item:isGoodHot() or item:isBadCold()) and (item:getHeat() < 1.3) then
		table.insert(notes, getText("Tooltip_food_BetterHot"))
		table.insert(notes, true)
	end
	if item:isCookedInMicrowave() then
		table.insert(notes, getText("Tooltip_food_CookedInMicrowave"))
		table.insert(notes, true)
	end

	if #texts == 0 and #notes == 0 then return end
	local font = ISToolTip.GetFont()
	local maxLabelWidth = 0
	for i=1,#texts,3 do
		local label = texts[(i-1)+1]
		maxLabelWidth = math.max(maxLabelWidth, getTextManager():MeasureStringX(font, label))
	end
	local tooltip = ISInventoryPaneContextMenu.addToolTip();
	for i=1,#texts,3 do
		local label = texts[(i-1)+1]
		local value = texts[(i-1)+2]
		local good = texts[(i-1)+3]
		tooltip.description = string.format("%s <RGB:1,1,1> %s: <SETX:%d> <%s> %s <LINE> ", tooltip.description, label, maxLabelWidth + 10, good and "GREEN" or "RED", value)
	end
	for i=1,#notes,2 do
		local label = notes[(i-1)+1]
		local good = notes[(i-1)+2]
		tooltip.description = string.format("%s <%s> %s <LINE> ", tooltip.description, good and "RGB:1,1,1" or "RED", label)
	end
	option.toolTip = tooltip;
end

ISInventoryPaneContextMenu.doEatOption = function(context, cmd, items, player, playerObj, foodItems)
    local eatOption = context:addOption(cmd, items, ISInventoryPaneContextMenu.onEatItems, 1, player)
    if foodItems[1] and foodItems[1]:getRequireInHandOrInventory() then
        local list = foodItems[1]:getRequireInHandOrInventory();
        local found = false;
        local required = "";
        for i=0,list:size()-1 do
            local fullType = moduleDotType(foodItems[1]:getModule(), list:get(i))
            if playerObj:getInventory():containsTypeRecurse(fullType) then
                found = true;
                break;
            end
            required = required .. getItemNameFromFullType(fullType);
            if i < list:size()-1 then
                required = required .. "/";
            end
        end
        if not found then
            eatOption.notAvailable = true
            local tooltip = ISInventoryPaneContextMenu.addToolTip();
            tooltip.description = getText("ContextMenu_Require", required);
            eatOption.toolTip = tooltip;
        end
    end
end

ISInventoryPaneContextMenu.checkConsolidate = function(drainable, player, context, previousPourInto)
    -- Check if we could consolidate drainable
    local consolidateList = {};
    if drainable and drainable:canConsolidate() then
        local otherDrainables = getSpecificPlayer(player):getInventory():getItemsFromType(drainable:getType());
        for i=0,otherDrainables:size() - 1 do
            local otherDrain = otherDrainables:get(i);
            if otherDrain ~= drainable and otherDrain:getUsedDelta() < 1 then
				local addIt = true;
				for i,v in ipairs(previousPourInto) do
					if v == otherDrain then
						addIt = false;
						break;
					end
				end
				if addIt then
                	table.insert(consolidateList, otherDrain);
				end
            end
        end
    end

    if #consolidateList > 0 then
        local optionName = getText("ContextMenu_Pour_into");
        if drainable:getConsolidateOption() then
            optionName = getText(drainable:getConsolidateOption());
        end
        local consolidateOption = context:addOption(optionName, nil, nil)
        local subMenuConsolidate = context:getNew(context)
        context:addSubMenu(consolidateOption, subMenuConsolidate)
        for _,intoItem in pairs(consolidateList) do
            subMenuConsolidate:addOption(intoItem:getName() .. " (" .. math.floor(intoItem:getUsedDelta() * 100) .. getText("ContextMenu_FullPercent") .. ")", drainable, ISInventoryPaneContextMenu.onConsolidate, intoItem, player)
        end
    end
end

ISInventoryPaneContextMenu.onConsolidate = function(drainable, intoItem, player)
    ISTimedActionQueue.add(ISConsolidateDrainable:new(player, drainable, intoItem, 90));
end

ISInventoryPaneContextMenu.OnTriggerRemoteController = function(remoteController, player)
    local playerObj = getSpecificPlayer(player);
    local args = { id=remoteController:getRemoteControlID(), range=remoteController:getRemoteRange() }
    sendClientCommand(playerObj, 'object', 'triggerRemote', args)
--[[
    if isClient() then
        local args = { id=remoteController:getRemoteControlID(), range=remoteController:getRemoteRange() }
        sendClientCommand(playerObj, 'object', 'triggerRemote', args)
    else
        IsoTrap.triggerRemote(playerObj, remoteController:getRemoteControlID(), remoteController:getRemoteRange())
    end
--]]
end

ISInventoryPaneContextMenu.OnLinkRemoteController = function(itemToLink, remoteController, player)
    local playerObj = getSpecificPlayer(player)
    if remoteController:getRemoteControlID() == -1 then
        remoteController:setRemoteControlID(ZombRand(100000));
    end
    itemToLink:setRemoteControlID(remoteController:getRemoteControlID());
    ISInventoryPage.dirtyUI();
end

ISInventoryPaneContextMenu.isAllFav = function (items)
    items = ISInventoryPane.getActualItems(items)
    for i,k in ipairs(items) do
        if not k:isFavorite() then
            return false
        end
    end
    return true
end

function ISInventoryPaneContextMenu.isAnyAllowed(container, items)
    items = ISInventoryPane.getActualItems(items)
    for i,k in ipairs(items) do
        if container:isItemAllowed(k) then
            return true
        end
    end
    return false
end

function ISInventoryPaneContextMenu.isAllNoDropMoveable(items)
    items = ISInventoryPane.getActualItems(items)
    for _,item in ipairs(items) do
        if not instanceof(item, "Moveable") or item:CanBeDroppedOnFloor() then
            return false
        end
    end
    return true
end

ISInventoryPaneContextMenu.OnResetRemoteControlID = function(item, player)
    local playerObj = getSpecificPlayer(player)
    item:setRemoteControlID(-1)
end

ISInventoryPaneContextMenu.doDrinkForThirstMenu = function(context, playerObj, waterContainer)
    local thirst = playerObj:getStats():getThirst()
    local units = math.min(math.ceil(thirst / 0.1), 10)
    units = math.min(units, waterContainer:getDrainableUsesInt())
    local option = context:addOption(getText("ContextMenu_Drink"), waterContainer, ISInventoryPaneContextMenu.onDrinkForThirst, playerObj)
    local tooltip = ISInventoryPaneContextMenu.addToolTip()
    local tx1 = getTextManager():MeasureStringX(tooltip.font, getText("Tooltip_food_Thirst") .. ":") + 20
    local tx2 = getTextManager():MeasureStringX(tooltip.font, getText("ContextMenu_WaterName") .. ":") + 20
    local tx = math.max(tx1, tx2)
    tooltip.description = string.format("%s: <SETX:%d> -%d / %d <LINE> %s: <SETX:%d> %d / %d",
        getText("Tooltip_food_Thirst"), tx, math.min(units * 10, thirst * 100), thirst * 100,
        getText("ContextMenu_WaterName"), tx, waterContainer:getDrainableUsesInt(), 1.0 / waterContainer:getUseDelta() + 0.0001)
    if waterContainer:isTaintedWater() then
        tooltip.description = tooltip.description .. " <BR> <RGB:1,0.5,0.5> " .. getText("Tooltip_item_TaintedWater")
    end
    option.toolTip = tooltip
--[[
    local drinkOption = context:addOption(getText("ContextMenu_Drink"), items, nil)
    local subMenuDrink = context:getNew(context)
    context:addSubMenu(drinkOption, subMenuDrink)
    subMenuDrink:addOption(getText("ContextMenu_Eat_All"), items, ISInventoryPaneContextMenu.onDrink, waterContainer, 1, player)
    subMenuDrink:addOption(getText("ContextMenu_Eat_Half"), items, ISInventoryPaneContextMenu.onDrink, waterContainer, 0.5, player)
    subMenuDrink:addOption(getText("ContextMenu_Eat_Quarter"), items, ISInventoryPaneContextMenu.onDrink, waterContainer, 0.25, player)
--]]
end

ISInventoryPaneContextMenu.onDrinkForThirst = function(waterContainer, playerObj)
    local thirst = playerObj:getStats():getThirst()
    local units = math.min(math.ceil(thirst / 0.1), 10)
    units = math.min(units, waterContainer:getDrainableUsesInt())
    ISInventoryPaneContextMenu.transferIfNeeded(playerObj, waterContainer)
    ISTimedActionQueue.add(ISDrinkFromBottle:new(playerObj, waterContainer, units))
end

ISInventoryPaneContextMenu.onDrink = function(items, waterContainer, percentage, player)
	local playerObj = getSpecificPlayer(player)
    ISInventoryPaneContextMenu.transferIfNeeded(playerObj, waterContainer)
-- how much use we have in the bottle
    local useLeft = waterContainer:getUsedDelta() / waterContainer:getUseDelta();
    ISTimedActionQueue.add(ISDrinkFromBottle:new(getSpecificPlayer(player), waterContainer, useLeft * percentage));
end

ISInventoryPaneContextMenu.onAddItemInEvoRecipe = function(recipe, baseItem, usedItem, player)
    local playerObj = getSpecificPlayer(player);
    local returnToContainer = {};
    if not playerObj:getInventory():contains(usedItem) then -- take the item if it's not in our inventory
        ISTimedActionQueue.add(ISInventoryTransferAction:new(playerObj, usedItem,usedItem:getContainer(), playerObj:getInventory(), nil));
        table.insert(returnToContainer, usedItem);
    end
    if not playerObj:getInventory():contains(baseItem) then -- take the base item if it's not in our inventory
        ISTimedActionQueue.add(ISInventoryTransferAction:new(playerObj, baseItem, baseItem:getContainer(), playerObj:getInventory(), nil));
        table.insert(returnToContainer, baseItem);
    end
    ISTimedActionQueue.add(ISAddItemInRecipe:new(playerObj, recipe, baseItem, usedItem, (70 - playerObj:getPerkLevel(Perks.Cooking))));
    ISCraftingUI.ReturnItemsToOriginalContainer(playerObj, returnToContainer)
end

ISInventoryPaneContextMenu.buildFixingMenu = function(brokenObject, player, fixing, fixOption, subMenuFix, vehiclePart)
    local tooltip = ISInventoryPaneContextMenu.addToolTip();
    tooltip.description = "";
--    fixOption.toolTip = tooltip;
    tooltip.texture = brokenObject:getTex();
    tooltip:setName(brokenObject:getName());
    -- fetch all the fixer item to build the sub menu and tooltip
    for i=0,fixing:getFixers():size()-1 do
        local fixer = fixing:getFixers():get(i);
        -- if you have this item in your main inventory
        local fixerItem = fixing:haveThisFixer(getSpecificPlayer(player), fixer, brokenObject);
        -- now test the required skill if needed
        local skillDescription = " ";
        if fixer:getFixerSkills() then
            for j=0,fixer:getFixerSkills():size()-1 do
                skillDescription = skillDescription .. PerkFactory.getPerk(Perks.FromString(fixer:getFixerSkills():get(j):getSkillName())):getName() .. "=" .. fixer:getFixerSkills():get(j):getSkillLevel() .. ",";
            end
        end
        local subOption = ISInventoryPaneContextMenu.addFixerSubOption(brokenObject, player, fixing, fixer, subMenuFix, vehiclePart);
        local add = "";
    
        if fixer:getNumberOfUse() > 1 then
            add = "="..fixer:getNumberOfUse();
        end
        if fixerItem then
            tooltip.description = tooltip.description .. " <LINE> " .. fixerItem:getName() .. add .. skillDescription;
        else
            tooltip.description = tooltip.description .. " <LINE> <RGB:1,0,0> " .. fixer:getFixerName() .. add .. skillDescription;
            subOption.notAvailable = true
        end
    end
end

ISInventoryPaneContextMenu.getContainers = function(character)
    if not character then return end
    local playerNum = character and character:getPlayerNum() or -1;
    -- get all the surrounding inventory of the player, gonna check for the item in them too
    local containerList = ArrayList.new();
    for i,v in ipairs(getPlayerInventory(playerNum).inventoryPane.inventoryPage.backpacks) do
        containerList:add(v.inventory);
    end
    for i,v in ipairs(getPlayerLoot(playerNum).inventoryPane.inventoryPage.backpacks) do
        containerList:add(v.inventory);
    end
    return containerList;
end

ISInventoryPaneContextMenu.addFixerSubOption = function(brokenObject, player, fixing, fixer, subMenuFix, vehiclePart)
    local usedItem = InventoryItemFactory.CreateItem(fixing:getModule():getName() .. "." .. fixer:getFixerName());
    local fixOption = null;
    local tooltip = ISInventoryPaneContextMenu.addToolTip();
    local itemName
    if usedItem then
        tooltip.texture = usedItem:getTex();
        itemName = getItemNameFromFullType(usedItem:getFullType())
        fixOption = subMenuFix:addOption(fixer:getNumberOfUse() .. " " .. itemName, brokenObject, ISInventoryPaneContextMenu.onFix, player, fixing, fixer, vehiclePart);
    else
        fixOption = subMenuFix:addOption(fixer:getNumberOfUse() .. " " .. fixer:getFixerName(), brokenObject, ISInventoryPaneContextMenu.onFix, player, fixing, fixer, vehiclePart);
        itemName = fixer:getFixerName()
    end
	tooltip:setName(itemName);
    local condPercentRepaired = FixingManager.getCondRepaired(brokenObject, getSpecificPlayer(player), fixing, fixer);
    local color1 = "<RED>";
    if condPercentRepaired > 15 and condPercentRepaired <= 25 then
        color1 = "<ORANGE>";
    elseif condPercentRepaired > 25 then
        color1 = "<GREEN>";
    end
    local chanceOfSucess = 100 - FixingManager.getChanceOfFail(brokenObject, getSpecificPlayer(player), fixing, fixer);
    local color2 = "<RED>";
    if chanceOfSucess > 15 and chanceOfSucess <= 40 then
        color2 = "<ORANGE>";
    elseif chanceOfSucess > 40 then
        color2 = "<GREEN>";
    end
    tooltip.description = " " .. color1 .. " " .. getText("Tooltip_potentialRepair") .. " " .. math.ceil(condPercentRepaired) .. "%";
    tooltip.description = tooltip.description .. " <LINE> " .. color2 .. " " .. getText("Tooltip_chanceSuccess") .. " " .. math.ceil(chanceOfSucess) .. "%";

	tooltip.description = tooltip.description .. " <LINE> <LINE> <RGB:1,1,1> " .. getText("Tooltip_craft_Needs") .. ": <LINE> "
    -- do you have the global item
    local add = "";
    if fixing:getGlobalItem() then
        local globalItem = fixing:haveGlobalItem(getSpecificPlayer(player));
        local uses = fixing:countUses(getSpecificPlayer(player), fixing:getGlobalItem(), nil)
        if globalItem then
            tooltip.description = tooltip.description .. " <LINE> " .. globalItem:getName() .. " " .. uses .. "/" .. fixing:getGlobalItem():getNumberOfUse() .. " <LINE> ";
        else
            local globalItem = InventoryItemFactory.CreateItem(fixing:getModule():getName() .. "." .. fixing:getGlobalItem():getFixerName());
            local name = fixing:getGlobalItem():getFixerName();
            if globalItem then name = globalItem:getName(); end
            tooltip.description = tooltip.description .. " <LINE> <RGB:1,0,0> " .. name .. " " .. uses .. "/" .. fixing:getGlobalItem():getNumberOfUse() .. " <LINE> ";
            fixOption.notAvailable = true
        end
    end
	local uses = fixing:countUses(getSpecificPlayer(player), fixer, brokenObject)
	if uses >= fixer:getNumberOfUse() then color1 = " <RGB:1,1,1> " else color1 = " <RED> " end
	tooltip.description = tooltip.description .. color1 .. itemName .. " " .. uses .. "/" .. fixer:getNumberOfUse()
	if fixer:getFixerSkills() then
		local skills = fixer:getFixerSkills()
		for j=0,skills:size()-1 do
			local skill = skills:get(j)
			local perk = Perks.FromString(skill:getSkillName())
			local perkLvl = getSpecificPlayer(player):getPerkLevel(perk)
			if perkLvl >= skill:getSkillLevel() then color1 = " <RGB:1,1,1> " else color1 = " <RED> " end
			tooltip.description = tooltip.description .. " <LINE> " .. color1 .. PerkFactory.getPerk(perk):getName() .. " " .. perkLvl .. "/" .. skill:getSkillLevel()
		end
	end

    fixOption.toolTip = tooltip;
    return fixOption
end

ISInventoryPaneContextMenu.onFix = function(brokenObject, player, fixing, fixer, vehiclePart)
    local playerObj = getSpecificPlayer(player);
    local playerInv = playerObj:getInventory();

    if not vehiclePart then
        ISInventoryPaneContextMenu.transferIfNeeded(playerObj, brokenObject);
    end

    local items = fixing:getRequiredItems(playerObj, fixer, brokenObject);
    if not items then return end;
    ISInventoryPaneContextMenu.transferIfNeeded(playerObj, items);

    ISTimedActionQueue.add(ISFixAction:new(playerObj, brokenObject, 60, fixing, fixer, vehiclePart));
end

ISInventoryPaneContextMenu.onDyeHair = function(hairDye, playerObj, beard)
    ISInventoryPaneContextMenu.transferIfNeeded(playerObj, hairDye)
    ISTimedActionQueue.add(ISDyeHair:new(playerObj, hairDye, beard, 120));
end

ISInventoryPaneContextMenu.onDryMyself = function(towels, player)
    towels = ISInventoryPane.getActualItems(towels)
    for i,k in ipairs(towels) do
        ISInventoryPaneContextMenu.dryMyself(k, player)
        break
    end
end

ISInventoryPaneContextMenu.onSetBombTimer = function(trap, player)
    local text = getText("IGUI_TimerSecondsBeforeExplosion");
    if trap:getSensorRange() > 0 then
        text = getText("IGUI_TimerSecondsBeforeActivation");
    end
    local modal = ISBombTimerDialog:new(0, 0, 280, 180, text, trap:getExplosionTimer(), getSpecificPlayer(player), nil, ISInventoryPaneContextMenu.onSetBombTimerClick, getSpecificPlayer(player), trap);
    modal:initialise();
    modal:addToUIManager();
    if JoypadState.players[player+1] then
        modal.prevFocus = JoypadState.players[player+1].focus
        JoypadState.players[player+1].focus = modal;
    end
end

function ISInventoryPaneContextMenu:onSetBombTimerClick(button, player, item)
    if button.internal == "OK" then
        local seconds = button.parent:getTime()
        if seconds > 0 then
            item:setExplosionTimer(seconds)
        end
    end
end

ISInventoryPaneContextMenu.onStopAlarm = function(alarm, player)
    local playerObj = getSpecificPlayer(player);
    local sq = alarm:getAlarmSquare()
	if playerObj == nil or sq == nil then
		alarm:stopRinging()
		return
	end
    if alarm:isInPlayerInventory() or luautils.walkAdj(playerObj, sq) then
        if not alarm:getWorldItem() and (alarm:getContainer() ~= playerObj:getInventory()) then
            ISTimedActionQueue.add(ISInventoryTransferAction:new(playerObj, alarm, alarm:getContainer(), playerObj:getInventory()))
        end
        ISTimedActionQueue.add(ISStopAlarmClockAction:new(playerObj, alarm, 20));
    end
end

ISInventoryPaneContextMenu.onSetAlarm = function(alarm, player)
    local playerObj = getSpecificPlayer(player);
    if not alarm:getWorldItem() and (alarm:getContainer() ~= playerObj:getInventory()) then
        local action = ISInventoryTransferAction:new(playerObj, alarm, alarm:getContainer(), playerObj:getInventory())
        action:setOnComplete(ISInventoryPaneContextMenu.onSetAlarm, alarm, player)
        ISTimedActionQueue.add(action)
        return
    end
    local modal = ISAlarmClockDialog:new(0, 0, 230, 160, player, alarm);
    modal:initialise();
    modal:addToUIManager();
    if JoypadState.players[player+1] then
        modal.prevFocus = getPlayerInventory(player)
        setJoypadFocus(player, modal)
    end
end

ISInventoryPaneContextMenu.onRenameMap = function(map, player)
    local modal = ISTextBox:new(0, 0, 280, 180, getText("ContextMenu_NameThisBag"), map:getName(), nil, ISInventoryPaneContextMenu.onRenameBagClick, player, getSpecificPlayer(player), map);
    modal:initialise();
    modal:addToUIManager();
    if JoypadState.players[player+1] then
        setJoypadFocus(player, modal)
    end
end

ISInventoryPaneContextMenu.onRenameBag = function(bag, player)
    local modal = ISTextBox:new(0, 0, 280, 180, getText("ContextMenu_NameThisBag"), bag:getName(), nil, ISInventoryPaneContextMenu.onRenameBagClick, player, getSpecificPlayer(player), bag);
    modal:initialise();
    modal:addToUIManager();
    if JoypadState.players[player+1] then
        setJoypadFocus(player, modal)
    end
end

ISInventoryPaneContextMenu.onRenameFood = function(food, player)
    local modal = ISTextBox:new(0, 0, 280, 180, getText("ContextMenu_RenameFood") .. food:getName(), food:getDisplayName(), nil, ISInventoryPaneContextMenu.onRenameFoodClick, player, getSpecificPlayer(player), food);
    modal:initialise();
    modal:addToUIManager();
    if JoypadState.players[player+1] then
        setJoypadFocus(player, modal)
    end
end

ISInventoryPaneContextMenu.onCheckMap = function(map, player)
    local playerObj = getSpecificPlayer(player)
    if luautils.haveToBeTransfered(playerObj, map) then
        local action = ISInventoryTransferAction:new(playerObj, map, map:getContainer(), playerObj:getInventory())
        action:setOnComplete(ISInventoryPaneContextMenu.onCheckMap, map, player)
        ISTimedActionQueue.add(action)
        return
    end

    if JoypadState.players[player+1] then
        local inv = getPlayerInventory(player)
        local loot = getPlayerLoot(player)
        inv:setVisible(false)
        loot:setVisible(false)
    end

    local titleBarHgt = ISCollapsableWindow.TitleBarHeight()
    local x = getPlayerScreenLeft(player) + 20
    local y = getPlayerScreenTop(player) + 20
    local width = getPlayerScreenWidth(player) - 20 * 2
    local height = getPlayerScreenHeight(player) - 20 * 2 - titleBarHgt

    local mapUI = ISMap:new(x, y, width, height, map, player);
    mapUI:initialise();
    local wrap = mapUI:wrapInCollapsableWindow(map:getName(), false, ISMapWrapper);
    wrap:setInfo(getText("IGUI_Map_Info"));
    wrap:setWantKeyEvents(true);
    mapUI.wrap = wrap;
    wrap.mapUI = mapUI;
--    mapUI.render = ISMap.noRender;
--    mapUI.prerender = ISMap.noRender;
    map:doBuildingStash();
    wrap:setVisible(true);
    wrap:addToUIManager();
	if JoypadState.players[player+1] then
        setJoypadFocus(player, mapUI)
    end
end

ISInventoryPaneContextMenu.onWriteSomething = function(notebook, editable, player)
    local fontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()
    local height = 110 + (15 * fontHgt);
    local modal = ISUIWriteJournal:new(0, 0, 280, height, nil, ISInventoryPaneContextMenu.onWriteSomethingClick, getSpecificPlayer(player), notebook, notebook:seePage(1), notebook:getName(), 15, editable, notebook:getPageToWrite());
    modal:initialise();
    modal:addToUIManager();
    if JoypadState.players[player+1] then
        setJoypadFocus(player, modal)
    end
end

function ISInventoryPaneContextMenu:onWriteSomethingClick(button)
    if button.internal == "OK" then
        for i,v in ipairs(button.parent.newPage) do
            button.parent.notebook:addPage(i,v);
        end
        button.parent.notebook:setName(button.parent.title:getText());
        button.parent.notebook:setCustomName(true);
    end
end

function ISInventoryPaneContextMenu:onRenameFoodClick(button, player, item)
    local playerNum = player:getPlayerNum()
    if button.internal == "OK" then
        if button.parent.entry:getText() and button.parent.entry:getText() ~= "" then
            item:setName(button.parent.entry:getText());
            item:setCustomName(true);
            local pdata = getPlayerData(playerNum);
            pdata.playerInventory:refreshBackpacks();
            pdata.lootInventory:refreshBackpacks();
        end
    end
    if JoypadState.players[playerNum+1] then
        setJoypadFocus(playerNum, getPlayerInventory(playerNum))
    end
end

function ISInventoryPaneContextMenu:onRenameBagClick(button, player, item)
    local playerNum = player:getPlayerNum()
    if button.internal == "OK" then
        if button.parent.entry:getText() and button.parent.entry:getText() ~= "" then
            item:setName(button.parent.entry:getText());
            local pdata = getPlayerData(playerNum);
            pdata.playerInventory:refreshBackpacks();
            pdata.lootInventory:refreshBackpacks();
        end
    end
    if JoypadState.players[playerNum+1] then
        setJoypadFocus(playerNum, getPlayerInventory(playerNum))
    end
end

ISInventoryPaneContextMenu.dryMyself = function(item, player)
	-- if towel isn't in main inventory, put it there first.
	local playerObj = getSpecificPlayer(player)
	ISInventoryPaneContextMenu.transferIfNeeded(playerObj, item)
	-- dry yourself
	-- how many use left on the towel
	local useLeft = math.ceil(item:getUsedDelta() * 10);
	ISTimedActionQueue.add(ISDryMyself:new(playerObj, item, (useLeft * 20) + 20));
end

ISInventoryPaneContextMenu.onApplyBandage = function(bandages, bodyPart, player)
	bandages = ISInventoryPane.getActualItems(bandages)
	for i,k in ipairs(bandages) do
		ISInventoryPaneContextMenu.applyBandage(k, bodyPart, player)
		break
	end
end

-- apply a bandage on a body part, loot it first if it's not in the player's inventory
ISInventoryPaneContextMenu.applyBandage = function(item, bodyPart, player)
	-- if bandage isn't in main inventory, put it there first.
	local playerObj = getSpecificPlayer(player)
    ISInventoryPaneContextMenu.transferIfNeeded(playerObj, item)
	-- apply bandage
	ISTimedActionQueue.add(ISApplyBandage:new(playerObj, playerObj, item, bodyPart, true));
end

-- look for any damaged body part on the player
ISInventoryPaneContextMenu.haveDamagePart = function(playerId)
	local result = {};
	local bodyParts = getSpecificPlayer(playerId):getBodyDamage():getBodyParts();
	-- fetch all the body part
	for i=0,BodyPartType.ToIndex(BodyPartType.MAX) - 1 do
		local bodyPart = bodyParts:get(i);
		-- if it's damaged
		if bodyPart:scratched() or bodyPart:deepWounded() or bodyPart:bitten() or bodyPart:stitched() or bodyPart:bleeding() or bodyPart:isBurnt() and not bodyPart:bandaged() then
			table.insert(result, bodyPart);
		end
	end
	return result;
end

ISInventoryPaneContextMenu.onLiteratureItems = function(items, player)
	items = ISInventoryPane.getActualItems(items)
	for i,k in ipairs(items) do
		ISInventoryPaneContextMenu.readItem(k, player)
		break;
    end
end

-- read a book, loot it first if it's not in the player's inventory
ISInventoryPaneContextMenu.readItem = function(item, player)
	local playerObj = getSpecificPlayer(player)
	if item:getContainer() == nil then
		return
	end
	ISInventoryPaneContextMenu.transferIfNeeded(playerObj, item)
	-- read
	ISTimedActionQueue.add(ISReadABook:new(playerObj, item, 150));
end

ISInventoryPaneContextMenu.onUnEquip = function(items, player)
	items = ISInventoryPane.getActualItems(items)
	for i,k in ipairs(items) do
		ISInventoryPaneContextMenu.unequipItem(k, player)
    end
end

ISInventoryPaneContextMenu.unequipItem = function(item, player)
    if not getSpecificPlayer(player):isEquipped(item) then return end
    if item ~= nil and item:getType() == "CandleLit" then item = ISInventoryPaneContextMenu.litCandleExtinguish(item, player) end
    ISTimedActionQueue.add(ISUnequipAction:new(getSpecificPlayer(player), item, 50));
end

ISInventoryPaneContextMenu.onWearItems = function(items, player)
    items = ISInventoryPane.getActualItems(items)
    local typeDone = {}; -- we keep track of what type of clothes we already wear to avoid wearing 2 times the same type (click on a stack of socks, select wear and you'll wear them 1 by 1 otherwise)
    for i,k in pairs(items) do
        if not typeDone[k:getBodyLocation()] then
            if k:getBodyLocation() == "Hat" or k:getBodyLocation() == "FullHat" then
                local playerObj = getSpecificPlayer(player);
                local wornItems = playerObj:getWornItems()
                for j=1,wornItems:size() do
                    local wornItem = wornItems:get(j-1)
                    if (wornItem:getLocation() == "SweaterHat" or wornItem:getLocation() == "JacketHat") then
                        for i=0, wornItem:getItem():getClothingItemExtraOption():size()-1 do
                            if wornItem:getItem():getClothingItemExtraOption():get(i) == "DownHoodie" then
                                ISInventoryPaneContextMenu.onClothingItemExtra(wornItem:getItem(), wornItem:getItem():getClothingItemExtra():get(i), playerObj);
                            end
                        end
                    end
                end
            end
            ISInventoryPaneContextMenu.wearItem(k, player)
            typeDone[k:getBodyLocation()] = true;
        end
    end
end

ISInventoryPaneContextMenu.onActivateItem = function(light, player)
	light:setActivated(not light:isActivated());
end

-- Wear a clothe, loot it first if it's not in the player's inventory
ISInventoryPaneContextMenu.wearItem = function(item, player)
	-- if clothing isn't in main inventory, put it there first.
	local playerObj = getSpecificPlayer(player)
	ISInventoryPaneContextMenu.transferIfNeeded(playerObj, item)
	-- wear the clothe
	ISTimedActionQueue.add(ISWearClothing:new(playerObj, item, 50));
end

ISInventoryPaneContextMenu.onPutItems = function(items, player)
	local playerObj = getSpecificPlayer(player)
	local playerLoot = getPlayerLoot(player).inventory
	items = ISInventoryPane.getActualItems(items)
	local doWalk = true
	for i,k in ipairs(items) do
		if playerLoot:isItemAllowed(k) and not k:isFavorite() then
			if doWalk then
				if not luautils.walkToContainer(playerLoot, player) then
					break
				end
				doWalk = false
			end
			ISTimedActionQueue.add(ISInventoryTransferAction:new(playerObj, k, k:getContainer(), playerLoot))
		end
	end
end

ISInventoryPaneContextMenu.canMoveTo = function(items, dest, player)
    local playerObj = getSpecificPlayer(player)
    if instanceof(dest, "InventoryContainer") then
        local container = dest:getInventory()
        for i,item in ipairs(items) do
            if item == dest then return nil end
            if container:contains(item) then return nil end
            if not container:isItemAllowed(item) then return nil end
            if item:isFavorite() and not container:isInCharacterInventory(playerObj) then return nil end
        end
        return dest
    end
    if instanceof(dest, "ItemContainer") and dest:getType() == "floor" then
        for i,item in ipairs(items) do
            if item == dest then return nil end
            if dest:getItems():contains(item) then return nil end
            if item:isFavorite() and not dest:isInCharacterInventory(playerObj) then return nil end
        end
        return dest
    end
    return nil
end

ISInventoryPaneContextMenu.hasRoomForAny = function(playerObj, container, items)
    if instanceof(container, "InventoryContainer") then
        container = container:getInventory()
    end
    if container == nil then
        return false
    end
    if container:getType() == "floor" then
        -- TODO: All checks that ISInventoryTransferAction:getNotFullFloorSquare() does.
        return true
    end
    if #items == 1 then
        return container:hasRoomFor(playerObj, items[1])
    end
    local minWeight = 100000
    for _,item in ipairs(items) do
        minWeight = math.min(minWeight, item:getUnequippedWeight())
    end
    return container:hasRoomFor(playerObj, minWeight)
end

ISInventoryPaneContextMenu.canUnpack = function(items, player)
    local playerObj = getSpecificPlayer(player)
    for i,item in ipairs(items) do
        if playerObj:getInventory():contains(item) then return false end
        if not playerObj:getInventory():contains(item, true) then return false end
--        if not item:getContainer():isInCharacterInventory(playerObj) then return false end
--        if item:isFavorite() then return false; end
    end
    return true
end

ISInventoryPaneContextMenu.onFavorite = function(items, item2, fav)
    for i,item in ipairs(items) do
        item:setFavorite(fav);
    end
end

ISInventoryPaneContextMenu.onMoveItemsTo = function(items, dest, player)
    if dest:getType() == "floor" then
        return ISInventoryPaneContextMenu.onDropItems(items, player)
    end
    local playerObj = getSpecificPlayer(player)
	if not luautils.walkToContainer(dest, player) then
		return
	end
    for i,item in ipairs(items) do
        if playerObj:isEquipped(item) then
            ISTimedActionQueue.add(ISUnequipAction:new(playerObj, item, 50));
        end
        ISTimedActionQueue.add(ISInventoryTransferAction:new(playerObj, item, item:getContainer(), dest))
    end
end

ISInventoryPaneContextMenu.canAddManyItems = function(recipe, selectedItem, playerObj)
    local container = selectedItem:getContainer();
    if not recipe:isCanBeDoneFromFloor() then
        container = playerObj:getInventory()
    end
    if isClient() and not instanceof(container:getParent(), "IsoGameCharacter") and getServerOptions():getInteger("ItemNumbersLimitPerContainer") > 0 and selectedItem:getContainer() then
        local createdItem = InventoryItemFactory.CreateItem(recipe:getResult():getFullType())
        if createdItem then
            local totalCount = createdItem:getCount() * recipe:getResult():getCount()
            if totalCount + container:getItems():size()+1 > getServerOptions():getInteger("ItemNumbersLimitPerContainer") then
                return false;
            end
        end
    end
    --        end
    return true;
end

----- ----- ----- ----- -----

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local IMAGE_SIZE = 20

local CraftTooltip = ISToolTip:derive("CraftTooltip")
CraftTooltip.tooltipPool = {}
CraftTooltip.tooltipsUsed = {}

ISRecipeTooltip = CraftTooltip

function CraftTooltip:addText(x, y, text, r, g, b)
	r = r or 1
	g = g or 1
	b = b or 1

	local numLines = 1
	local p = string.find(text, "\n")
	while p do
		numLines = numLines + 1
		p = string.find(text, "\n", p + 4)
	end

	local width = getTextManager():MeasureStringX(UIFont.Small, text)
	table.insert(self.contents, { type = "text", x = x, y = y, width = width, height = FONT_HGT_SMALL * numLines, text = text, r = r, g = g, b = b })
end

function CraftTooltip:addImage(x, y, textureName)
	table.insert(self.contents, { type = "image", x = x, y = y, width = IMAGE_SIZE, height = IMAGE_SIZE, texture = getTexture(textureName) })
end

function CraftTooltip:getSingleSourceText(source)
	local txt = ""
	if source:isDestroy() then
		txt = getText("IGUI_CraftUI_SourceDestroy")
		-- Hack for "Refill Propane Torch" and similar
		local itemFullType = source:getItems():get(0)
		local resultFullType = self.recipe:getResult():getFullType()
		if itemFullType == resultFullType then
			txt = getText("IGUI_CraftUI_SourceKeep")
		end
		-- Hack for "Clean Bandage" / "Sterilize Bandage"
		if string.contains(itemFullType, "Bandage") and string.contains(resultFullType, "Bandage") then
			txt = getText("IGUI_CraftUI_SourceUse")
		end
		-- Hack for "Clean Rag" / "Sterilize Rag"
		if string.contains(itemFullType, "RippedSheets") and string.contains(resultFullType, "RippedSheets") then
			txt = getText("IGUI_CraftUI_SourceUse")
        end
        -- Hack for "Clean Denim/Leather Strips" / "Sterilize Denim/Leather Strips"
        if string.contains(itemFullType, "LeatherStrips") and string.contains(resultFullType, "LeatherStrips") or (string.contains(itemFullType, "DenimStrips") and string.contains(resultFullType, "DenimStrips")) then
            txt = getText("IGUI_CraftUI_SourceUse")
        end
	elseif source:isKeep() then
		txt = getText("IGUI_CraftUI_SourceKeep")
	else
		txt = getText("IGUI_CraftUI_SourceUse")
	end
	return txt
end

-- Return true if item2's type is in item1's getClothingExtraItem() list.
function CraftTooltip:isExtraClothingItemOf(item1, item2)
	local scriptItem = getScriptManager():FindItem(item1.fullType)
	if not scriptItem then
		return false
	end
	local extras = scriptItem:getClothingItemExtra()
	if not extras then
		return false
	end
	local moduleName = scriptItem:getModule():getName()
	for i=1,extras:size() do
		local extra = extras:get(i-1)
		local fullType = moduleDotType(moduleName, extra)
		if item2.fullType == fullType then
			return true
		end
	end
	return false
end

function CraftTooltip:isWaterSource(item, count)
	return instanceof(item, "DrainableComboItem") and item:isWaterSource() and item:getDrainableUsesInt() >= count
end

-- Duplicate of ISCraftingUI:getContainers()
function CraftTooltip:getContainers()
	if not self.character then return end
	self.playerNum = self.character:getPlayerNum()
	-- get all the surrounding inventory of the player, gonna check for the item in them too
	self.containerList = self.containerList or ArrayList.new()
	self.containerList:clear()
	for i,v in ipairs(getPlayerInventory(self.playerNum).inventoryPane.inventoryPage.backpacks) do
		self.containerList:add(v.inventory)
	end
	for i,v in ipairs(getPlayerLoot(self.playerNum).inventoryPane.inventoryPage.backpacks) do
		self.containerList:add(v.inventory)
	end
end

-- Duplicate of ISCraftingUI:getAvailableItemsType()
function CraftTooltip:getAvailableItemsType()
	local result = self.typesAvailable or {}
	table.wipe(result)
	local recipe = self.recipe
	local items = RecipeManager.getAvailableItemsAll(recipe, self.character, self.containerList, nil, nil)
	for i=0, recipe:getSource():size()-1 do
		local source = recipe:getSource():get(i)
		local sourceItemTypes = {}
		for k=1,source:getItems():size() do
			local sourceFullType = source:getItems():get(k-1)
			sourceItemTypes[sourceFullType] = true
		end
		for x=0,items:size()-1 do
			local item = items:get(x)
			if sourceItemTypes["Water"] and self:isWaterSource(item, source:getCount()) then
				result["Water"] = (result["Water"] or 0) + item:getDrainableUsesInt()
			elseif sourceItemTypes[item:getFullType()] then
				local count = 1
				if not source:isDestroy() and item:IsDrainable() then
					count = item:getDrainableUsesInt()
				end
				if not source:isDestroy() and instanceof(item, "Food") then
					if source:getUse() > 0 then
						count = -item:getHungerChange() * 100
					end
				end
				result[item:getFullType()] = (result[item:getFullType()] or 0) + count
			end
		end
	end
	self.typesAvailable = result
end

function CraftTooltip:layoutContents(x, y)
	if self.contents then
		return self.contentsWidth, self.contentsHeight
	end

	self:getContainers()
	self:getAvailableItemsType()
	
	self.contents = {}
	local marginLeft = 20
	local marginTop = 10
	local marginBottom = 10
	local y1 = y + marginTop
	local lineHeight = math.max(FONT_HGT_SMALL, 20 + 2)
	local textDY = (lineHeight - FONT_HGT_SMALL) / 2
	local imageDY = (lineHeight - IMAGE_SIZE) / 2
	local singleSources = {}
	local multiSources = {}
	local allSources = {}

	for j=1,self.recipe:getSource():size() do
		local source = self.recipe:getSource():get(j-1)
		if source:getItems():size() == 1 then
			table.insert(singleSources, source)
		else
			table.insert(multiSources, source)
		end
	end

	-- Display singleSources before multiSources
	for _,source in ipairs(singleSources) do
		table.insert(allSources, source)
	end

	for _,source in ipairs(multiSources) do
		table.insert(allSources, source)
	end

	local maxSingleSourceLabelWidth = 0
	for _,source in ipairs(singleSources) do
		local txt = self:getSingleSourceText(source)
		local width = getTextManager():MeasureStringX(UIFont.Small, txt)
		maxSingleSourceLabelWidth = math.max(maxSingleSourceLabelWidth, width)
	end

	for _,source in ipairs(allSources) do
		local txt = ""
		local x1 = x + marginLeft
		if source:getItems():size() > 1 then
			if source:isDestroy() then
				txt = getText("IGUI_CraftUI_SourceDestroyOneOf")
			elseif source:isKeep() then
				txt = getText("IGUI_CraftUI_SourceKeepOneOf")
			else
				txt = getText("IGUI_CraftUI_SourceUseOneOf")
			end
			self:addText(x1, y1 + textDY, txt)
			y1 = y1 + lineHeight
		else
			txt = self:getSingleSourceText(source)
			self:addText(x1, y1 + textDY, txt)
			x1 = x1 + maxSingleSourceLabelWidth + 10
		end

		local itemDataList = {}

		for k=1,source:getItems():size() do
			local itemData = {}
			itemData.fullType = source:getItems():get(k-1)
			itemData.available = true
			local item = nil
			if itemData.fullType == "Water" then
				item = ISInventoryPaneContextMenu.getItemInstance("Base.WaterDrop")
			else
				item = ISInventoryPaneContextMenu.getItemInstance(itemData.fullType)
                --this reads the worldsprite so the generated item will have correct icon
                if instanceof(item, "Moveable") and instanceof(self.recipe, "MovableRecipe") then
                    item:ReadFromWorldSprite(self.recipe:getWorldSprite());
                end
			end
			itemData.texture = ""
			if item then
				itemData.texture = item:getTex():getName()
				if itemData.fullType == "Water" then
					if source:getCount() == 1 then
						itemData.name = getText("IGUI_CraftUI_CountOneUnit", getText("ContextMenu_WaterName"))
					else
						itemData.name = getText("IGUI_CraftUI_CountUnits", getText("ContextMenu_WaterName"), source:getCount())
					end
				elseif source:getItems():size() > 1 then -- no units
					itemData.name = item:getDisplayName()
				elseif not source:isDestroy() and item:IsDrainable() then
					if source:getCount() == 1 then
						itemData.name = getText("IGUI_CraftUI_CountOneUnit", item:getDisplayName())
					else
						itemData.name = getText("IGUI_CraftUI_CountUnits", item:getDisplayName(), source:getCount())
					end
				elseif not source:isDestroy() and source:getUse() > 0 then -- food
					if source:getUse() == 1 then
						itemData.name = getText("IGUI_CraftUI_CountOneUnit", item:getDisplayName())
					else
						itemData.name = getText("IGUI_CraftUI_CountUnits", item:getDisplayName(), source:getUse())
					end
				elseif source:getCount() > 1 then
					itemData.name = getText("IGUI_CraftUI_CountNumber", item:getDisplayName(), source:getCount())
				else
					itemData.name = item:getDisplayName()
				end
			else
				itemData.name = itemData.fullType
			end
			local countAvailable = self.typesAvailable[itemData.fullType] or 0
			if countAvailable < source:getCount() then
				itemData.available = false
				itemData.r = 0.54
				itemData.g = 0.54
				itemData.b = 0.54
			end
			table.insert(itemDataList, itemData)
		end

		table.sort(itemDataList, function(a,b)
			if a.available and not b.available then return true end
			if not a.available and b.available then return false end
			return not string.sort(a.name, b.name)
		end)

		-- Hack for "Dismantle Digital Watch" and similar recipes.
		-- Recipe sources include both left-hand and right-hand versions of the same item.
		-- We only want to display one of them.
		---[[
		for j=1,#itemDataList do
			local item = itemDataList[j]
			for k=#itemDataList,j+1,-1 do
				local item2 = itemDataList[k]
				if self:isExtraClothingItemOf(item, item2) then
					table.remove(itemDataList, k)
				end
			end
		end
		--]]

		for i,itemData in ipairs(itemDataList) do
			local x2 = x1
			if source:getItems():size() > 1 then
				x2 = x2 + 20
			end
			if itemData.texture ~= "" then
				self:addImage(x2, y1 + imageDY, itemData.texture)
				x2 = x2 + IMAGE_SIZE + 6
			end
			self:addText(x2, y1 + textDY, itemData.name, itemData.r, itemData.g, itemData.b)
			y1 = y1 + lineHeight

			if i == 10 and i < #itemDataList then
				self:addText(x2, y1 + textDY, getText("Tooltip_AndNMore", #itemDataList - i))
				y1 = y1 + lineHeight
				break
			end
		end
	end

	if self.recipe:getTooltip() then
		local x1 = x + marginLeft
		local tooltip = getText(self.recipe:getTooltip())
		self:addText(x1, y1 + 8, tooltip)
	end

	self.contentsX = x
	self.contentsY = y
	self.contentsWidth = 0
	self.contentsHeight = 0
	for _,v in ipairs(self.contents) do
		self.contentsWidth = math.max(self.contentsWidth, v.x + v.width - x)
		self.contentsHeight = math.max(self.contentsHeight, v.y + v.height + marginBottom - y)
	end
	return self.contentsWidth, self.contentsHeight
end

function CraftTooltip:renderContents()
	for _,v in ipairs(self.contents) do
		if v.type == "image" then
			self:drawTextureScaledAspect(v.texture, v.x, v.y, v.width, v.height, 1, 1, 1, 1)
		elseif v.type == "text" then
			self:drawText(v.text, v.x, v.y, v.r, v.g, v.b, 1, UIFont.Small)
		end
	end
	if false then
		self:drawRectBorder(self.contentsX, self.contentsY, self.contentsWidth, self.contentsHeight, self.height, 0.5, 0.9, 0.9, 1)
	end
end

function CraftTooltip:reset()
	ISToolTip.reset(self)
	self.contents = nil
end

function CraftTooltip:new()
	local o = ISToolTip.new(self)
	return o
end

function CraftTooltip.addToolTip()
	local pool = CraftTooltip.tooltipPool
	if #pool == 0 then
		table.insert(pool, CraftTooltip:new())
	end
	local tooltip = table.remove(pool, #pool)
	tooltip:reset()
	table.insert(CraftTooltip.tooltipsUsed, tooltip)
	return tooltip;
end

function CraftTooltip.releaseAll()
	for _,tooltip in ipairs(CraftTooltip.tooltipsUsed) do
		table.insert(CraftTooltip.tooltipPool, tooltip)
	end
--    print('reused ',#CraftTooltip.tooltipsUsed,' craft tooltips')
	table.wipe(CraftTooltip.tooltipsUsed)
end

----- ----- ----- ----- -----

ISInventoryPaneContextMenu.addDynamicalContextMenu = function(selectedItem, context, recipeList, player, containerList)
    CraftTooltip.releaseAll()
    local playerObj = getSpecificPlayer(player)
	for i=0,recipeList:size() -1 do
        local recipe = recipeList:get(i)
        -- check if we have multiple item like this
        local numberOfTimes = RecipeManager.getNumberOfTimesRecipeCanBeDone(recipe, playerObj, containerList, selectedItem)
		local resultItem = InventoryItemFactory.CreateItem(recipe:getResult():getFullType());
        local option = nil;
        local subMenuCraft = nil;
        if selectedItem:getType() == "Candle" and numberOfTimes >= 2 then
            numberOfTimes = 1; --prevent players from lighting more than one candle at a time
        end
        if numberOfTimes ~= 1 then
            subMenuCraft = context:getNew(context);
            option = context:addOption(recipe:getName(), selectedItem, nil);
            context:addSubMenu(option, subMenuCraft);
            if playerObj:isDriving() then
                option.notAvailable = true;
            else
                local subOption = subMenuCraft:addOption(getText("ContextMenu_One"), selectedItem, ISInventoryPaneContextMenu.OnCraft, recipe, player, false);
                local tooltip = CraftTooltip.addToolTip();
                tooltip.character = playerObj
                tooltip.recipe = recipe
                -- add it to our current option
                tooltip:setName(recipe:getName());
                if resultItem:getTexture() and resultItem:getTexture():getName() ~= "Question_On" then
                    tooltip:setTexture(resultItem:getTexture():getName());
                end
                subOption.toolTip = tooltip;
            
                if numberOfTimes > 1 then
                    subOption = subMenuCraft:addOption(getText("ContextMenu_AllWithCount", numberOfTimes), selectedItem, ISInventoryPaneContextMenu.OnCraft, recipe, player, true);
                else
                    subOption = subMenuCraft:addOption(getText("ContextMenu_All"), selectedItem, ISInventoryPaneContextMenu.OnCraft, recipe, player, true);
                end
                subOption.toolTip = tooltip;
            end
        else
            option = context:addOption(recipe:getName(), selectedItem, ISInventoryPaneContextMenu.OnCraft, recipe, player, false);

            -- special handling for if the player already has a lit candle in their inventory
            local inventoryItems = playerObj:getInventory():getItems()
            for j=1,inventoryItems:size() do
                local item = inventoryItems:get(j-1)
                if item:getType() == "CandleLit" and selectedItem:getType() == "Candle" then
                    option.notAvailable = true;
                    local tooltip = ISInventoryPaneContextMenu.addToolTip();
                    tooltip.description = getText("Tooltip_CantCraftSecondLitCandle");
                    option.toolTip = tooltip;
                    return
                end
            end

        end
        -- limit doing a recipe that add multiple items if the dest container has an item limit
        if not ISInventoryPaneContextMenu.canAddManyItems(recipe, selectedItem, playerObj) then
            option.notAvailable = true;
            if subMenuCraft then
                for i,v in ipairs(subMenuCraft.options) do
                    v.notAvailable = true;
                    local tooltip = ISInventoryPaneContextMenu.addToolTip();
                    tooltip.description = getText("Tooltip_CantCraftDriving"); -- FIXME: wrong translation
                    v.toolTip = tooltip;
                end
            end
            local tooltip = ISInventoryPaneContextMenu.addToolTip();
            tooltip.description = getText("Tooltip_CantCraftDriving"); -- FIXME: wrong translation
            option.toolTip = tooltip;
            return;
        end
        if playerObj:isDriving() then
            option.notAvailable = true;
            local tooltip = ISInventoryPaneContextMenu.addToolTip();
            tooltip.description = getText("Tooltip_CantCraftDriving");
            option.toolTip = tooltip;
            return
        end
        if subMenuCraft == nil and recipe:getNumberOfNeededItem() > 0 then
			local tooltip = CraftTooltip.addToolTip();
			tooltip.character = playerObj
			tooltip.recipe = recipe
			-- add it to our current option
			tooltip:setName(recipe:getName());
			if resultItem:getTexture() and resultItem:getTexture():getName() ~= "Question_On" then
				tooltip:setTexture(resultItem:getTexture():getName());
			end
			option.toolTip = tooltip;
		end
	end
end

ISInventoryPaneContextMenu.addToolTip = function()
	local pool = ISInventoryPaneContextMenu.tooltipPool
	if #pool == 0 then
		table.insert(pool, ISToolTip:new())
	end
	local tooltip = table.remove(pool, #pool)
	tooltip:reset()
	table.insert(ISInventoryPaneContextMenu.tooltipsUsed, tooltip)
	return tooltip;
end

ISInventoryPaneContextMenu.OnCraft = function(selectedItem, recipe, player, all)
	local playerObj = getSpecificPlayer(player)
	local containers = ISInventoryPaneContextMenu.getContainers(playerObj)
	local container = selectedItem:getContainer()
	if not recipe:isCanBeDoneFromFloor() then
		container = playerObj:getInventory()
	end
	local items = RecipeManager.getAvailableItemsNeeded(recipe, playerObj, containers, selectedItem, nil)
	local returnToContainer = {}; -- keep track of items we moved to put them back to their original container
	if not recipe:isCanBeDoneFromFloor() then
		for i=1,items:size() do
			local item = items:get(i-1)
			if item:getContainer() ~= playerObj:getInventory() then
				ISTimedActionQueue.add(ISInventoryTransferAction:new(playerObj, item, item:getContainer(), playerObj:getInventory(), nil))
				table.insert(returnToContainer, item)
			end
		end
	end

    -- in case of movable dismantling equip tools:
    if instanceof(recipe, "MovableRecipe") then
        local primaryTool = RecipeManager.GetMovableRecipeTool(true, recipe, selectedItem, playerObj, containers);
        if primaryTool then
            ISWorldObjectContextMenu.equip(playerObj, playerObj:getPrimaryHandItem(), primaryTool, true)
        end

        local secondaryTool = RecipeManager.GetMovableRecipeTool(false, recipe, selectedItem, playerObj, containers);
        if secondaryTool then
            ISWorldObjectContextMenu.equip(playerObj, playerObj:getSecondaryHandItem(), secondaryTool, false)
        end
    end

	local action = ISCraftAction:new(playerObj, selectedItem, recipe:getTimeToMake(), recipe, container, containers)
	if all then
		action:setOnComplete(ISInventoryPaneContextMenu.OnCraftComplete, action, recipe, playerObj, container, containers, selectedItem)
	end
	ISTimedActionQueue.add(action)

    -- add back their item to their original container
    ISCraftingUI.ReturnItemsToOriginalContainer(playerObj, returnToContainer)
end

ISInventoryPaneContextMenu.OnCraftComplete = function(completedAction, recipe, playerObj, container, containers, selectedItem)
	if not RecipeManager.IsRecipeValid(recipe, playerObj, nil, containers) then return end
	local items = RecipeManager.getAvailableItemsNeeded(recipe, playerObj, containers, nil, nil)
	if items:isEmpty() then return end
    if not ISInventoryPaneContextMenu.canAddManyItems(recipe, items:get(0), playerObj) then
        return;
    end
	local previousAction = completedAction
	local returnToContainer = {}
	if not recipe:isCanBeDoneFromFloor() then
		for i=1,items:size() do
			local item = items:get(i-1)
			if item:getContainer() ~= playerObj:getInventory() then
				local action = ISInventoryTransferAction:new(playerObj, item, item:getContainer(), playerObj:getInventory(), nil)
				if not action.ignoreAction then
					ISTimedActionQueue.addAfter(previousAction, action)
					previousAction = action
				end
				table.insert(returnToContainer, item)
			end
		end
	end
	local action = ISCraftAction:new(playerObj, items:get(0), recipe:getTimeToMake(), recipe, container, containers)
	action:setOnComplete(ISInventoryPaneContextMenu.OnCraftComplete, action, recipe, playerObj, container, containers)
    ISTimedActionQueue.addAfter(previousAction, action)
    ISCraftingUI.ReturnItemsToOriginalContainer(playerObj, returnToContainer)
end

ISInventoryPaneContextMenu.eatItem = function(item, percentage, player)
--	if not player then
    local playerObj = getSpecificPlayer(player);
--	end
	-- if food isn't in main inventory, put it there first.
	ISInventoryPaneContextMenu.transferIfNeeded(playerObj, item)
	-- Transfer required items.
	if item:getRequireInHandOrInventory() then
		local itemRequired = nil
		local types = item:getRequireInHandOrInventory()
		for i=1,types:size() do
			local fullType = moduleDotType(item:getModule(), types:get(i-1))
			itemRequired = playerObj:getInventory():getFirstTypeRecurse(fullType)
			if itemRequired then
				ISInventoryPaneContextMenu.transferIfNeeded(playerObj, itemRequired)
				break
			end
		end
		if not itemRequired then return end
	end
    -- Then eat it.
    ISTimedActionQueue.add(ISEatFoodAction:new(playerObj, item, percentage));
end

-- Function that unequip primary weapon and equip the selected weapon
ISInventoryPaneContextMenu.OnPrimaryWeapon = function(items, player)
    local playerObj = getSpecificPlayer(player)
    if playerObj:getPrimaryHandItem() ~= nil and playerObj:getPrimaryHandItem():getType() == "CandleLit" then
        playerObj:setPrimaryHandItem(ISInventoryPaneContextMenu.litCandleExtinguish(playerObj:getPrimaryHandItem(), player))
    end
	items = ISInventoryPane.getActualItems(items)
	for i,k in ipairs(items) do
		if (instanceof(k, "HandWeapon") and k:getCondition() > 0) or (instanceof(k, "InventoryItem") and not instanceof(k, "HandWeapon")) then
			ISInventoryPaneContextMenu.equipWeapon(k, true, false, player)
			break
		end
	end
end

--> Stormy
-- Function that unequip primary weapon and equip the selected weapon
ISInventoryPaneContextMenu.OnReload = function(items, player)
	-- if the item you've selected is not the header
	local weapon = items[1];
	-- if it's a header, we get our first item (the selected one)
	if not instanceof(items[1], "InventoryItem") then
		weapon = items[1].items[1];
	end
	-- do reload
	ReloadManager[player+1]:startReloadFromUi(weapon);
end
-->> Stormy

-- Function that goes through all pills selected and take them.
ISInventoryPaneContextMenu.onPillsItems = function(items, player)
	items = ISInventoryPane.getActualItems(items)
	for i,k in ipairs(items) do
		ISInventoryPaneContextMenu.takePill(k, player)
		break
    end
end

-- Take a pill, loot it first if it's not in the player's inventory
ISInventoryPaneContextMenu.takePill = function(item, player)
	local playerObj = getSpecificPlayer(player);
	-- if pill isn't in main inventory, put it there first.
	ISInventoryPaneContextMenu.transferIfNeeded(playerObj, item)
	-- take the pill
	ISTimedActionQueue.add(ISTakePillAction:new(playerObj, item, 165));
end

ISInventoryPaneContextMenu.OnTwoHandsEquip = function(items, player)
    local playerObj = getSpecificPlayer(player)
    if playerObj:getPrimaryHandItem() ~= nil and playerObj:getPrimaryHandItem():getType() == "CandleLit" then
        playerObj:setPrimaryHandItem(ISInventoryPaneContextMenu.litCandleExtinguish(playerObj:getPrimaryHandItem(), player))
    end
    if playerObj:getSecondaryHandItem() ~= nil and playerObj:getSecondaryHandItem():getType() == "CandleLit" then
        playerObj:setSecondaryHandItem(ISInventoryPaneContextMenu.litCandleExtinguish(playerObj:getSecondaryHandItem(), player))
    end
	items = ISInventoryPane.getActualItems(items)
	for _,item in ipairs(items) do
		ISInventoryPaneContextMenu.equipWeapon(item, false, true, player)
		break
	end
end

-- Function that unequip second weapon and equip the selected weapon
ISInventoryPaneContextMenu.OnSecondWeapon = function(items, player)
    local playerObj = getSpecificPlayer(player)
    if playerObj:getSecondaryHandItem() ~= nil and playerObj:getSecondaryHandItem():getType() == "CandleLit" then
        playerObj:setSecondaryHandItem(ISInventoryPaneContextMenu.litCandleExtinguish(playerObj:getSecondaryHandItem(), player))
    end
	items = ISInventoryPane.getActualItems(items)
	for _,item in ipairs(items) do
		ISInventoryPaneContextMenu.equipWeapon(item, false, false, player)
		break
	end
end

-- Function that equip the selected weapon
ISInventoryPaneContextMenu.equipWeapon = function(weapon, primary, twoHands, player)
	local playerObj = getSpecificPlayer(player)
	-- Drop corpse or generator
	if isForceDropHeavyItem(playerObj:getPrimaryHandItem()) then
		ISTimedActionQueue.add(ISUnequipAction:new(playerObj, playerObj:getPrimaryHandItem(), 50));
	end
	-- if weapon isn't in main inventory, put it there first.
	ISInventoryPaneContextMenu.transferIfNeeded(playerObj, weapon)
    -- Then equip it.
    ISTimedActionQueue.add(ISEquipWeaponAction:new(playerObj, weapon, 50, primary, twoHands));
end

ISInventoryPaneContextMenu.onInformationItems = function(items)
	items = ISInventoryPane.getActualItems(items)
	for i,k in pairs(items) do
		ISInventoryPaneContextMenu.information(k)
		break
	end
end

ISInventoryPaneContextMenu.information = function(item)
--~ 	local tooltip = ObjectTooltip.new();
--~ 	item:DoTooltip(tooltip);
	ISInventoryPaneContextMenu.removeToolTip();
	ISInventoryPaneContextMenu.toolRender = ISToolTipInv:new(item);
	ISInventoryPaneContextMenu.toolRender:initialise();
	ISInventoryPaneContextMenu.toolRender:addToUIManager();
	ISInventoryPaneContextMenu.toolRender:setVisible(true);
end

ISInventoryPaneContextMenu.removeToolTip = function()
	if ISInventoryPaneContextMenu.toolRender then
		ISInventoryPaneContextMenu.toolRender:removeFromUIManager();
		ISInventoryPaneContextMenu.toolRender:setVisible(false);
	end
end

-- Function that goes through all items selected and eats them.
-- eat only 1 of the item list
ISInventoryPaneContextMenu.onEatItems = function(items, percentage, player)
	items = ISInventoryPane.getActualItems(items)
	for i,k in ipairs(items) do
		ISInventoryPaneContextMenu.eatItem(k, percentage, player)
		break
    end
end

ISInventoryPaneContextMenu.onPlaceTrap = function(weapon, player)
    ISTimedActionQueue.add(ISPlaceTrap:new(player, weapon, 50));
end

ISInventoryPaneContextMenu.onRemoveUpgradeWeapon = function(weapon, part, playerObj)
    ISInventoryPaneContextMenu.transferIfNeeded(playerObj, weapon)
    local screwdriver = playerObj:getInventory():getFirstTagEvalRecurse("Screwdriver", predicateNotBroken)
    if screwdriver then
        ISInventoryPaneContextMenu.equipWeapon(screwdriver, true, false, playerObj:getPlayerNum());
        ISTimedActionQueue.add(ISRemoveWeaponUpgrade:new(playerObj, weapon, part, 50));
    end
end

ISInventoryPaneContextMenu.onUpgradeWeapon = function(weapon, part, player)
    ISInventoryPaneContextMenu.transferIfNeeded(player, weapon)
    ISInventoryPaneContextMenu.transferIfNeeded(player, part)
    local screwdriver = player:getInventory():getFirstTagEvalRecurse("Screwdriver", predicateNotBroken)
    if screwdriver then
        ISInventoryPaneContextMenu.equipWeapon(part, false, false, player:getPlayerNum());
        ISInventoryPaneContextMenu.equipWeapon(screwdriver, true, false, player:getPlayerNum());
        ISTimedActionQueue.add(ISUpgradeWeapon:new(player, weapon, part, 50));
    end
end

ISInventoryPaneContextMenu.onDropItems = function(items, player)
	local playerObj = getSpecificPlayer(player)
	items = ISInventoryPane.getActualItems(items)
--	ISInventoryPaneContextMenu.transferItems(items, playerObj:getInventory(), player, true)
	for _,item in ipairs(items) do
		if not item:isFavorite() then
			ISInventoryPaneContextMenu.dropItem(item, player)
		end
	end
end

ISInventoryPaneContextMenu.litCandleExtinguish = function(item, player)
    local playerObj = getSpecificPlayer(player)

    local candle = playerObj:getInventory():AddItem("Base.Candle");
    candle:setUsedDelta(item:getUsedDelta());
    candle:setCondition(item:getCondition());
    candle:setFavorite(item:isFavorite());
    if item == playerObj:getPrimaryHandItem() then
        playerObj:setPrimaryHandItem(candle);
    else
        playerObj:setSecondaryHandItem(candle);
    end
    playerObj:getInventory():Remove(item);
    return candle;
end

ISInventoryPaneContextMenu.dropItem = function(item, player)
    if "Tutorial" == getCore():getGameMode() then
        return;
    end
	local playerObj = getSpecificPlayer(player)

    --lit candles need special handling if they're dropped.
    --this unequips the lit candle and extinguish it, then prepares the new unlit candle to be dropped
    if item:getType() == "CandleLit" and item:isEquipped() then
        item = ISInventoryPaneContextMenu.litCandleExtinguish(item, player)
    end

	if not playerObj:isHandItem(item) then
		local hotbar = getPlayerHotbar(player)
		if hotbar and hotbar:isItemAttached(item) then
			hotbar:removeItem(item, true)
		else
			ISInventoryPaneContextMenu.unequipItem(item, player)
		end
	end
	
	if true then
		-- Don't transfer items to the player's inventory first, since doing so
		-- breaks ISInventoryTransferAction's multi-item transfer thing.
		ISTimedActionQueue.add(ISInventoryTransferAction:new(playerObj, item, item:getContainer(), ISInventoryPage.floorContainer[player + 1]))
		return
	end
	-- if item isn't in main inventory, put it there first.
	ISInventoryPaneContextMenu.transferIfNeeded(playerObj, item)
    ISTimedActionQueue.add(ISInventoryTransferAction:new(playerObj, item, playerObj:getInventory(), ISInventoryPage.floorContainer[player + 1]));
end

ISInventoryPaneContextMenu.onGrabItems = function(items, player)
	local playerInv = getPlayerInventory(player).inventory;
	ISInventoryPaneContextMenu.transferItems(items, playerInv, player)
end

ISInventoryPaneContextMenu.transferItems = function(items, playerInv, player, dontWalk)
	local playerObj = getSpecificPlayer(player)
	items = ISInventoryPane.getActualItems(items)
	for i,k in ipairs(items) do
		if k:getContainer() ~= playerInv and k:getContainer() ~= nil then
			if not dontWalk then
				if not luautils.walkToContainer(k:getContainer(), player) then
					return
				end
				dontWalk = true
			end
			ISTimedActionQueue.add(ISInventoryTransferAction:new(playerObj, k, k:getContainer(), playerInv))
		end
	end
end

ISInventoryPaneContextMenu.onGrabHalfItems = function(items, player)
	local playerObj = getSpecificPlayer(player)
	local playerInv = getPlayerInventory(player).inventory;
	local doWalk = true
	for i,k in ipairs(items) do
		if not instanceof(k, "InventoryItem") then
			local count = math.floor((#k.items - 1) / 2)
			-- first in a list is a dummy duplicate, so ignore it.
			for i2=1,count do
				local k2 = k.items[i2+1]
				if k2:getContainer() ~= playerInv then
					if doWalk then
						if not luautils.walkToContainer(k2:getContainer(), player) then
							return
						end
						doWalk = false
					end
					ISTimedActionQueue.add(ISInventoryTransferAction:new(playerObj, k2, k2:getContainer(), playerInv))
				end
			end
		elseif k:getContainer() ~= playerInv then
			if doWalk then
				if not luautils.walkToContainer(k2:getContainer(), player) then
					return
				end
				doWalk = false
			end
			ISTimedActionQueue.add(ISInventoryTransferAction:new(playerObj, k, k:getContainer(), playerInv))
		end
	end
end

ISInventoryPaneContextMenu.onEditItem = function(items, player, item)
    local ui = ISItemEditorUI:new(50,50,600,600, player, item);
    ui:initialise();
    ui:addToUIManager();
end

ISInventoryPaneContextMenu.onGrabOneItems = function(items, player)
	items = ISInventoryPane.getActualItems(items)
    local playerObj = getSpecificPlayer(player);
    local playerInv = getPlayerInventory(player).inventory;
	for i,k in ipairs(items) do
		if k:getContainer() ~= playerInv then
			if not luautils.walkToContainer(k:getContainer(), player) then
				return
			end
			ISTimedActionQueue.add(ISInventoryTransferAction:new(playerObj, k, k:getContainer(), playerInv));
			return;
		end
	end
end

-- Crowley
-- Pours water from one container into another.
ISInventoryPaneContextMenu.onTransferWater = function(items, itemFrom, itemTo, player)
	--print("Moving water from " .. itemFrom:getName() .. " to " .. itemTo:getName());
	local playerObj = getSpecificPlayer(player)
	if not itemTo:isWaterSource() then
		local newItemType = itemTo:getReplaceOnUseOn();
		newItemType = string.sub(newItemType,13);
		newItemType = itemTo:getModule() .. "." .. newItemType;

        local newItem = InventoryItemFactory.CreateItem(newItemType,0);
        newItem:setFavorite(itemTo:isFavorite());
        newItem:setCondition(itemTo:getCondition());
        playerObj:getInventory():AddItem(newItem);
		if playerObj:getPrimaryHandItem() == itemTo then
			playerObj:setPrimaryHandItem(newItem)
		end
		if playerObj:getSecondaryHandItem() == itemTo then
			playerObj:setSecondaryHandItem(newItem)
		end
		playerObj:getInventory():Remove(itemTo);

        itemTo = newItem;
    end
--
	local waterStorageAvailable = (1 - itemTo:getUsedDelta()) / itemTo:getUseDelta();
	local waterStorageNeeded = itemFrom:getUsedDelta() / itemFrom:getUseDelta();

	local itemFromEndingDelta = 0;
	local itemToEndingDelta = nil;
--
	if waterStorageAvailable >= waterStorageNeeded then
		--Transfer all water to the the second container.
		local waterInA = itemTo:getUsedDelta() / itemTo:getUseDelta();
		local waterInB = itemFrom:getUsedDelta() / itemFrom:getUseDelta();
		local totalWater = waterInA + waterInB;

		itemToEndingDelta = totalWater * itemTo:getUseDelta();
		itemFromEndingDelta = 0;
	end

	if waterStorageAvailable < waterStorageNeeded then
		--Transfer what we can. Leave the rest in the container.
		local waterInB = itemFrom:getUsedDelta() / itemFrom:getUseDelta();
		local waterRemainInB = waterInB - waterStorageAvailable;

		itemFromEndingDelta = waterRemainInB * itemFrom:getUseDelta();
		itemToEndingDelta = 1;
	end

	ISInventoryPaneContextMenu.transferIfNeeded(playerObj, itemFrom)

	ISTimedActionQueue.add(ISTransferWaterAction:new(getSpecificPlayer(player), itemFrom, itemTo, itemFromEndingDelta, itemToEndingDelta));
end
--/Crowley

-- Crowley
-- Empties a water container
ISInventoryPaneContextMenu.onEmptyWaterContainer = function(items, waterSource, player)
	if waterSource ~= nil then
		local playerObj = getSpecificPlayer(player)
		ISInventoryPaneContextMenu.transferIfNeeded(playerObj, waterSource)
		ISTimedActionQueue.add(ISDumpWaterAction:new(playerObj, waterSource));
	end
end
--/Crowley

-- Return true if the given item's ReplaceOnUse type can hold water.
-- The check is recursive to handle RemouladeFull -> RemouladeHalf -> RemouladeEmpty.
ISInventoryPaneContextMenu.canReplaceStoreWater = function(item)
    --	print('testing ' .. item:getFullType())
    if item:getReplaceOnUse() then
        itemType = moduleDotType(item:getModule(), item:getReplaceOnUse())
        return ISInventoryPaneContextMenu.canReplaceStoreWater2(itemType)
    end
    if instanceof(item, "DrainableComboItem") and item:getReplaceOnDeplete() then
        itemType = moduleDotType(item:getModule(), item:getReplaceOnDeplete())
        return ISInventoryPaneContextMenu.canReplaceStoreWater2(itemType)
    end
    return false
end

ISInventoryPaneContextMenu.canReplaceStoreWater2 = function(itemType)
--	print('testing ' .. itemType)
	local item = ScriptManager.instance:FindItem(itemType)
	if item == nil then return false end
	if item:getCanStoreWater() then
		return true
    end
    if item:getReplaceOnUse() then
        itemType = moduleDotType(item:getModuleName(), item:getReplaceOnUse())
        return ISInventoryPaneContextMenu.canReplaceStoreWater2(itemType)
    end
    if (item:getType() == Type.Drainable) and item:getReplaceOnDeplete() then
        itemType = moduleDotType(item:getModuleName(), item:getReplaceOnDeplete())
        return ISInventoryPaneContextMenu.canReplaceStoreWater2(itemType)
    end
    return false
end


ISInventoryPaneContextMenu.onDumpContents = function(items, item, time, player)
	if item ~= nil then
		local playerObj = getSpecificPlayer(player)
		ISInventoryPaneContextMenu.transferIfNeeded(playerObj, item)
		ISTimedActionQueue.add(ISDumpContentsAction:new(playerObj, item, time));
	end
end

ISInventoryPaneContextMenu.startWith = function(String,Start)
	return string.sub(String, 1, string.len(Start)) == Start;
end

ISInventoryPaneContextMenu.getRealEvolvedItemUse = function(evoItem, evorecipe2, cookingLvl)
    if not evoItem or not evorecipe2 or not evorecipe2:getItemRecipe(evoItem) then return; end
    local use = evorecipe2:getItemRecipe(evoItem):getUse();
    if use > math.abs(evoItem:getHungerChange() * 100) then
        use = math.floor(math.abs(evoItem:getHungerChange() * 100));
    end
    if evoItem:isRotten() then
        local baseHunger = evoItem:getBaseHunger() * 100
        if cookingLvl == 7 or cookingLvl == 8 then
            use = math.abs(round(baseHunger - (baseHunger - ((5/100) * baseHunger)), 1));
        else
            use = math.abs(round(baseHunger - (baseHunger - ((10/100) * baseHunger)), 1));
        end
    end
    return use;
end

ISInventoryPaneContextMenu.doEquipOption = function(context, playerObj, isWeapon, items, player)
    -- check if hands if not heavy damaged
    if (not playerObj:isPrimaryHandItem(isWeapon) or (playerObj:isPrimaryHandItem(isWeapon) and playerObj:isSecondaryHandItem(isWeapon))) and not getSpecificPlayer(player):getBodyDamage():getBodyPart(BodyPartType.Hand_R):isDeepWounded() and (getSpecificPlayer(player):getBodyDamage():getBodyPart(BodyPartType.Hand_R):getFractureTime() == 0 or getSpecificPlayer(player):getBodyDamage():getBodyPart(BodyPartType.Hand_R):getSplintFactor() > 0) then
        -- forbid reequipping skinned items to avoid multiple problems for now
        local add = true;
        if playerObj:getSecondaryHandItem() == isWeapon and isWeapon:getScriptItem():getReplaceWhenUnequip() then
            add = false;
        end
        if add then
            context:addOption(getText("ContextMenu_Equip_Primary"), items, ISInventoryPaneContextMenu.OnPrimaryWeapon, player);
        end
    end
    if (not playerObj:isSecondaryHandItem(isWeapon) or (playerObj:isPrimaryHandItem(isWeapon) and playerObj:isSecondaryHandItem(isWeapon))) and not getSpecificPlayer(player):getBodyDamage():getBodyPart(BodyPartType.Hand_L):isDeepWounded() and (getSpecificPlayer(player):getBodyDamage():getBodyPart(BodyPartType.Hand_L):getFractureTime() == 0 or getSpecificPlayer(player):getBodyDamage():getBodyPart(BodyPartType.Hand_L):getSplintFactor() > 0) then
        -- forbid reequipping skinned items to avoid multiple problems for now
        local add = true;
        if playerObj:getPrimaryHandItem() == isWeapon and isWeapon:getScriptItem():getReplaceWhenUnequip() then
            add = false;
        end
        if add then
            context:addOption(getText("ContextMenu_Equip_Secondary"), items, ISInventoryPaneContextMenu.OnSecondWeapon, player);
        end
    end
end

ISInventoryPaneContextMenu.equipHeavyItem = function(playerObj, item)
    if not luautils.walkToContainer(item:getContainer(), playerObj:getPlayerNum()) then
        return
    end
    if playerObj:getPrimaryHandItem() then
        ISTimedActionQueue.add(ISUnequipAction:new(playerObj, playerObj:getPrimaryHandItem(), 50));
    end
    if playerObj:getSecondaryHandItem() and playerObj:getSecondaryHandItem() ~= playerObj:getPrimaryHandItem() then
        ISTimedActionQueue.add(ISUnequipAction:new(playerObj, playerObj:getSecondaryHandItem(), 50));
    end
    ISTimedActionQueue.add(ISEquipHeavyItem:new(playerObj, item, 100));
end

ISInventoryPaneContextMenu.onMakeUp = function(makeup, playerObj)
    local playerNum = playerObj:getPlayerNum()
    if ISMakeUpUI.windows[playerNum+1] then
        ISMakeUpUI.windows[playerNum+1]:setVisible(true);
        ISMakeUpUI.windows[playerNum+1].item = makeup;
        ISMakeUpUI.windows[playerNum+1]:reinit();
    else
        ISMakeUpUI.windows[playerNum+1] = ISMakeUpUI:new(0, 0, makeup, playerObj);
        ISMakeUpUI.windows[playerNum+1]:initialise();
        ISMakeUpUI.windows[playerNum+1]:addToUIManager();
    end
    if JoypadState.players[playerNum+1] then
        ISMakeUpUI.windows[playerNum+1].prevFocus = JoypadState.players[playerNum+1].focus
        JoypadState.players[playerNum+1].focus = ISMakeUpUI.windows[playerNum+1]
    end
end

function ISInventoryPaneContextMenu.doGrabMenu(context, items, player)
    for i,k in pairs(items) do
        if not instanceof(k, "InventoryItem") then
            if isForceDropHeavyItem(k.items[1]) then
                -- corpse or generator
            elseif #k.items > 2 then
                context:addOption(getText("ContextMenu_Grab_one"), items, ISInventoryPaneContextMenu.onGrabOneItems, player);
                context:addOption(getText("ContextMenu_Grab_half"), items, ISInventoryPaneContextMenu.onGrabHalfItems, player);
                context:addOption(getText("ContextMenu_Grab_all"), items, ISInventoryPaneContextMenu.onGrabItems, player);
            else
                context:addOption(getText("ContextMenu_Grab"), items, ISInventoryPaneContextMenu.onGrabItems, player);
            end
            break;
        elseif isForceDropHeavyItem(k) then
            -- corpse or generator
        else
            context:addOption(getText("ContextMenu_Grab"), items, ISInventoryPaneContextMenu.onGrabItems, player);
            break;
        end
    end
end

function ISInventoryPaneContextMenu.doEvorecipeMenu(context, items, player, evorecipe, baseItem, containerList)
    for i=0,evorecipe:size()-1 do
        local listOfAddedItems = {};
        local evorecipe2 = evorecipe:get(i);
        local items = evorecipe2:getItemsCanBeUse(getSpecificPlayer(player), baseItem, containerList);
        if items:size() == 0 then
            break;
        end
        -- check for every item category to add a "add random category" in top of the list
        local catList = ISInventoryPaneContextMenu.getEvoItemCategories(items, evorecipe2);
        local cookingLvl = getSpecificPlayer(player):getPerkLevel(Perks.Cooking);
        local subOption = nil;
        local fromName = getText("ContextMenu_EvolvedRecipe_" .. evorecipe2:getUntranslatedName())
        if evorecipe2:isResultItem(baseItem) then
            subOption = context:addOption(fromName, nil);
        else
            subOption = context:addOption(getText("ContextMenu_Create_From_Ingredient", fromName), nil);
        end
        local subMenuRecipe = context:getNew(context);
        context:addSubMenu(subOption, subMenuRecipe);
        
        for i,v in pairs(catList) do
            if getText("ContextMenu_FoodType_"..i) ~= "ContextMenu_FoodType_"..i then
                local txt = getText("ContextMenu_FromRandom", getText("ContextMenu_FoodType_"..i));
                if evorecipe2:isResultItem(baseItem) then
                    txt = getText("ContextMenu_AddRandom", getText("ContextMenu_FoodType_"..i));
                end
                subMenuRecipe:addOption(txt, evorecipe2, ISInventoryPaneContextMenu.onAddItemInEvoRecipe, baseItem, catList[i][ZombRand(1, #catList[i]+1)], player);
            end
        end
        for i=0,items:size() -1 do
            local evoItem = items:get(i);
            local extraInfo = "";
            if instanceof(evoItem, "Food") then
                if evoItem:isSpice() then
                    extraInfo = getText("ContextMenu_EvolvedRecipe_Spice");
                elseif evoItem:getPoisonLevelForRecipe() then
                    if evoItem:getHerbalistType() and evoItem:getHerbalistType() ~= "" and getSpecificPlayer(player):isRecipeKnown("Herbalist") then
                        extraInfo = getText("ContextMenu_EvolvedRecipe_Poison");
                    end
                    local use = ISInventoryPaneContextMenu.getRealEvolvedItemUse(evoItem, evorecipe2, cookingLvl);
                    if use then
                        extraInfo = extraInfo .. "(" .. use .. ")";
                    end
                elseif not evoItem:isPoison() then
                    local use = ISInventoryPaneContextMenu.getRealEvolvedItemUse(evoItem, evorecipe2, cookingLvl);
                    extraInfo = "(" .. use .. ")";
                    if listOfAddedItems[evoItem:getType()] and listOfAddedItems[evoItem:getType()] == use then
                        evoItem = nil;
                    else
                        listOfAddedItems[evoItem:getType()] = use;
                    end
                end
            end
            if evoItem then
                ISInventoryPaneContextMenu.addItemInEvoRecipe(subMenuRecipe, baseItem, evoItem, extraInfo, evorecipe2, player);
            end
        end
    end
end

ISInventoryPaneContextMenu.doMakeUpMenu = function(context, makeup, playerObj)
    local option = context:addOption(getText("IGUI_MakeUp"), makeup, ISInventoryPaneContextMenu.onMakeUp, playerObj);
    local mirror = false;

    -- check for mirror in inventory
    if playerObj:getInventory():contains("Mirror") then
        mirror = true;
    end
     
    -- check for world mirror
    if not mirror then
        for x=playerObj:getCurrentSquare():getX() - 1, playerObj:getCurrentSquare():getX() + 2 do
            for y=playerObj:getCurrentSquare():getY() - 1, playerObj:getCurrentSquare():getY() + 2 do
                local sq = getCell():getGridSquare(x, y, playerObj:getCurrentSquare():getZ())
                if sq then
                    for i=0, sq:getObjects():size() - 1 do
                        local object = sq:getObjects():get(i);
                        local sprite = object:getSprite();
                        if not sq:isWallTo(playerObj:getCurrentSquare()) and sprite:getProperties():Is("IsMirror") then
                            mirror = true;
                            break;
                        end
                        if object:getAttachedAnimSprite() then
                            for j=0,object:getAttachedAnimSprite():size() - 1 do
                                local sprite = object:getAttachedAnimSprite():get(j):getParentSprite();
                                if not sq:isWallTo(playerObj:getCurrentSquare()) and sprite:getProperties():Is("IsMirror") then
                                    mirror = true;
                                    break;
                                end
                            end
                        end
                    end
                end
            end
        end
    end
    
    if not mirror then
        local tooltip = ISInventoryPaneContextMenu.addToolTip();
        option.notAvailable = true;
        tooltip.description = getText("Tooltip_NeedMirror");
        option.toolTip = tooltip;
    end
end

local function getWornItemInLocation(playerObj, location)
    local wornItems = playerObj:getWornItems()
    local bodyLocationGroup = wornItems:getBodyLocationGroup()
    for i=1,wornItems:size() do
        local wornItem = wornItems:get(i-1)
        if (wornItem:getLocation() == location) or bodyLocationGroup:isExclusive(wornItem:getLocation(), location) then
            return wornItem:getItem()
        end
    end
    return nil
end

function ISInventoryPaneContextMenu.getItemInstance(type)
    local self = ISInventoryPaneContextMenu
    if not self.ItemInstances then self.ItemInstances = {} end
    local item = self.ItemInstances[type]
    if not item then
        item = InventoryItemFactory.CreateItem(type)
        if item then
            self.ItemInstances[type] = item
            self.ItemInstances[item:getFullType()] = item
        end
    end
    return item
end

ISInventoryPaneContextMenu.doClothingItemExtraMenu = function(context, clothingItemExtra, playerObj)
    if (clothingItemExtra:IsClothing() or clothingItemExtra:IsInventoryContainer()) and clothingItemExtra:getClothingExtraSubmenu() then
        local option = context:addOption(getText("ContextMenu_Wear"));
        local subMenu = context:getNew(context);
        context:addSubMenu(option, subMenu);
        context = subMenu;

        local location = clothingItemExtra:IsClothing() and clothingItemExtra:getBodyLocation() or clothingItemExtra:canBeEquipped()
        local existingItem = getWornItemInLocation(playerObj, location)
        if existingItem ~= clothingItemExtra then
            local text = getText("ContextMenu_" .. clothingItemExtra:getClothingExtraSubmenu());
            local option = context:addOption(text, clothingItemExtra, ISInventoryPaneContextMenu.onClothingItemExtra, clothingItemExtra:getType(), playerObj);
            ISInventoryPaneContextMenu.doWearClothingTooltip(playerObj, clothingItemExtra, clothingItemExtra, option);
        end
    end

    for i=0,clothingItemExtra:getClothingItemExtraOption():size()-1 do
        local text = getText("ContextMenu_" .. clothingItemExtra:getClothingItemExtraOption():get(i));
        local itemType = moduleDotType(clothingItemExtra:getModule(), clothingItemExtra:getClothingItemExtra():get(i));
        local item = ISInventoryPaneContextMenu.getItemInstance(itemType);
        local option = context:addOption(text, clothingItemExtra, ISInventoryPaneContextMenu.onClothingItemExtra, itemType, playerObj);
        ISInventoryPaneContextMenu.doWearClothingTooltip(playerObj, item, clothingItemExtra, option);
    end
end

ISInventoryPaneContextMenu.onClothingItemExtra = function(item, extra, playerObj)
    if item:getBodyLocation() == "Hat" or item:getBodyLocation() == "FullHat" then
        local wornItems = playerObj:getWornItems()
        for j=1,wornItems:size() do
            local wornItem = wornItems:get(j-1)
            if (wornItem:getLocation() == "SweaterHat" or wornItem:getLocation() == "JacketHat") then
                for i=0, wornItem:getItem():getClothingItemExtraOption():size()-1 do
                    if wornItem:getItem():getClothingItemExtraOption():get(i) == "DownHoodie" then
                        ISInventoryPaneContextMenu.onClothingItemExtra(wornItem:getItem(), wornItem:getItem():getClothingItemExtra():get(i), playerObj);
                    end
                end
            end
        end
    end
    ISInventoryPaneContextMenu.transferIfNeeded(playerObj, item)
    ISTimedActionQueue.add(ISClothingExtraAction:new(playerObj, item, extra))
end

ISInventoryPaneContextMenu.doPlace3DItemOption = function(items, player, context)
    local all3D = true;
    items = ISInventoryPane.getActualItems(items)
    for _,item in ipairs(items) do
        if not item:getWorldStaticItem() and not instanceof(item, "HandWeapon") and not instanceof(item, "Clothing") then
            all3D = false;
        end
        if all3D and instanceof(item, "Clothing") then
            all3D = item:canBe3DRender();
        end
    end
    if all3D then
        local option = context:addOption(getText("ContextMenu_PlaceItemOnGround"), items, ISInventoryPaneContextMenu.onPlaceItemOnGround, player);
        local testItem = items[1];
        if testItem and #items > 1 and (testItem:getContainer() ~= player:getInventory()) then
            local subMenu = context:getNew(context);
            context:addSubMenu(option, subMenu);
            subMenu:addOption(getText("ContextMenu_PlaceOne"), {items[1]}, ISInventoryPaneContextMenu.onPlaceItemOnGround, player);
            if #items > 2 then
                subMenu:addOption(getText("ContextMenu_PlaceHalf"), {unpack(items, 1, math.ceil(#items / 2))}, ISInventoryPaneContextMenu.onPlaceItemOnGround, player);
            end;
            subMenu:addOption(getText("ContextMenu_PlaceAll"), items, ISInventoryPaneContextMenu.onPlaceItemOnGround, player);
        end;
    end
end

ISInventoryPaneContextMenu.onPlaceItemOnGround = function(items, playerObj)
    local playerNum = playerObj:getPlayerNum()
    if playerObj:getJoypadBind() ~= -1 and getPlayerInventory(playerNum):isVisible() then
        updateJoypadFocus(JoypadState.players[playerNum+1])
        setJoypadFocus(playerNum, nil)
    end
    ISInventoryPaneContextMenu.placeItemCursor = ISPlace3DItemCursor:new(playerObj, items)
    getCell():setDrag(ISInventoryPaneContextMenu.placeItemCursor, playerNum)
end
