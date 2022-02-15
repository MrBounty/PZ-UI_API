--***********************************************************
--**                    ROBERT JOHNSON                     **
--**      Contextual inventory menu for farming stuff      **
--***********************************************************

---@class ISFarmingMenu
ISFarmingMenu = {};
ISFarmingMenu.info = {};
ISFarmingMenu.GardeningSprayMilk = nil;
ISFarmingMenu.GardeningSprayCigarettes = nil;
ISFarmingMenu.cheat = false

local function predicateDrainableUsesInt(item, count)
	return item:getDrainableUsesInt() >= count
end

local function predicateNotBroken(item)
	return not item:isBroken()
end

local function predicateDigPlow(item)
	return not item:isBroken() and item:hasTag("DigPlow")
end

ISFarmingMenu.doFarmingMenu = function(player, context, worldobjects, test)

	if test and ISWorldObjectContextMenu.Test then return true end

	if JoypadState.players[player+1] then
		local playerObj = getSpecificPlayer(player)
		local playerInv = playerObj:getInventory()
		if ISFarmingMenu.canDigHere(worldobjects) then
			local handItem = ISFarmingMenu.getShovel(playerObj);
			if handItem then
				if test then return ISWorldObjectContextMenu.setTest() end
				context:addOption(getText("ContextMenu_Dig"), worldobjects, ISFarmingMenu.onPlow, playerObj, handItem)
			else
				if not playerObj:getBodyDamage():getBodyPart(BodyPartType.Hand_L):HasInjury() and not playerObj:getBodyDamage():getBodyPart(BodyPartType.Hand_R):HasInjury() then
					if test then return ISWorldObjectContextMenu.setTest() end
					local option = context:addOption(getText("ContextMenu_DigWithHands"), worldobjects, ISFarmingMenu.onPlow, playerObj, nil)
					local tooltip = ISWorldObjectContextMenu.addToolTip();
					tooltip.description = getText("ContextMenu_DigWithHandsTT");
					option.toolTip = tooltip;
				else
					if test then return ISWorldObjectContextMenu.setTest() end
					local option = context:addOption(getText("ContextMenu_DigWithHands"), worldobjects, ISFarmingMenu.onPlow, playerObj, nil)
					option.notAvailable = true;
					local tooltip = ISWorldObjectContextMenu.addToolTip();
					tooltip.description = getText("ContextMenu_DamagedHands");
					option.toolTip = tooltip;
				end
			end
		end
		for i,v in ipairs(worldobjects) do
			local plant = CFarmingSystem.instance:getLuaObjectOnSquare(v:getSquare())
			if plant then
				if test then return ISWorldObjectContextMenu.setTest() end
				context:addOption(getText("ContextMenu_Farming"), plant:getSquare(), ISFarmingMenu.onJoypadFarming, player)
				return
			end
		end
		return
	end

	return ISFarmingMenu.doFarmingMenu2(player, context, worldobjects, test)
end

ISFarmingMenu.getShovel = function(player)
	local playerInv = player:getInventory();
	-- first check if we have a shovel equipped
	local handItem = player:getPrimaryHandItem()
	if handItem and predicateDigPlow(handItem) then
		return handItem
	end
	-- if not, check if there's a shovel in inventory
	return playerInv:getFirstEvalRecurse(predicateDigPlow)
end

ISFarmingMenu.itemSortByName = function(a,b)
    return not string.sort(a:getName(), b:getName());
end

ISFarmingMenu.doFarmingMenu2 = function(player, context, worldobjects, test)
	local playerObj = getSpecificPlayer(player)
	local playerInv = playerObj:getInventory()

	ISFarmingMenu.GardeningSprayMilk = nil;
	ISFarmingMenu.GardeningSprayCigarettes = nil;

	local fertilizer = false;
	local shovel = ISFarmingMenu.getShovel(playerObj)
	local handItem = playerObj:getPrimaryHandItem();
	local canSeed = false;
	local canWater = false;
	local cureMildew = false;
	local cureFlies = false;
	local info = false;
	local canHarvest = false;
    local sq = nil;
    local player = playerObj;
	local currentPlant = nil;
	for i,v in ipairs(worldobjects) do
		local plant = CFarmingSystem.instance:getLuaObjectOnSquare(v:getSquare())
		if plant then
			if playerInv:containsTypeRecurse("Fertilizer") or playerInv:containsTypeRecurse("CompostBag") then -- fertilizer
				fertilizer = true;
			end
			if plant.state == "plow" then -- sow seed
				canSeed = true;
			end
			if plant.state == "seeded" then -- water the plant
				canWater = true;
			end
			if plant.state ~= "plow" then -- info
				info = true;
			end
			-- disease
			if plant.mildewLvl > 0 then -- mildew
				cureMildew = true;
			end
			if plant.fliesLvl > 0 then -- flies
				cureFlies = true;
			end
			-- harvest
			if plant:canHarvest() then
				canHarvest = true;
			end
			currentPlant = plant
			sq = v:getSquare();
			break
		end
	end

    if not JoypadState.players[player:getPlayerNum()+1] and ISFarmingMenu.canDigHere(worldobjects) and not player:getVehicle() then
        if shovel then
            if test then return ISWorldObjectContextMenu.setTest() end
            context:addOption(getText("ContextMenu_Dig"), worldobjects, ISFarmingMenu.onPlow, player, shovel);
        else
            if(not player:getBodyDamage():getBodyPart(BodyPartType.Hand_L):HasInjury() and not player:getBodyDamage():getBodyPart(BodyPartType.Hand_R):HasInjury()) then
                if test then return ISWorldObjectContextMenu.setTest() end
                local option = context:addOption(getText("ContextMenu_DigWithHands"), worldobjects, ISFarmingMenu.onPlow, player, nil)
                local tooltip = ISWorldObjectContextMenu.addToolTip();
                tooltip.description = getText("ContextMenu_DigWithHandsTT");
                option.toolTip = tooltip;
            else
                if test then return ISWorldObjectContextMenu.setTest() end
                local option = context:addOption(getText("ContextMenu_DigWithHands"), worldobjects, ISFarmingMenu.onPlow, player, nil)
                option.notAvailable = true;
                local tooltip = ISWorldObjectContextMenu.addToolTip();
                tooltip.description = getText("ContextMenu_DamagedHands");
                option.toolTip = tooltip;
            end
        end
    end

	if fertilizer then
		if test then return ISWorldObjectContextMenu.setTest() end
		context:addOption(getText("ContextMenu_Fertilize"), worldobjects, ISFarmingMenu.onFertilize, handItem, currentPlant, sq, player);
	end
	if shovel and currentPlant then
		if test then return ISWorldObjectContextMenu.setTest() end
        context:addOption(getText("ContextMenu_Remove"), worldobjects, ISFarmingMenu.onShovel, currentPlant, player, sq);
    end
	if info then
		if test then return ISWorldObjectContextMenu.setTest() end
		context:addOption(getText("ContextMenu_Info"), worldobjects, ISFarmingMenu.onInfo, currentPlant, sq, player);
	end
	if canHarvest then
		if test then return ISWorldObjectContextMenu.setTest() end
		context:addOption(getText("ContextMenu_Harvest"), worldobjects, ISFarmingMenu.onHarvest, currentPlant, sq, player);
	end
	-- plant seed subMenu
	if canSeed then
		if test then return ISWorldObjectContextMenu.setTest() end
		ISFarmingMenu:doSeedMenu(context, currentPlant, sq, player)
	end
	-- water your plant
	if canWater and currentPlant.waterLvl < 100 then
		local waterSources = {}
		if handItem and handItem:isWaterSource() and math.floor(handItem:getUsedDelta()/handItem:getUseDelta()) > 0 then
			table.insert(waterSources, handItem)
		else
			for i = 0, playerInv:getItems():size() - 1 do
				local item = playerInv:getItems():get(i);
				if item:isWaterSource() and math.floor(item:getUsedDelta()/item:getUseDelta()) > 0 then
					table.insert(waterSources, item)
				end
			end
		end
		-- we get how many use we can do on our item
		if #waterSources > 0 then
			if test then return ISWorldObjectContextMenu.setTest() end
			local waterOption = context:addOption(getText("ContextMenu_Water"), worldobjects, nil);
			local subMenuWater = context
			if #waterSources > 1 then
				subMenuWater = context:getNew(context);
				context:addSubMenu(waterOption, subMenuWater);
				table.sort(waterSources, ISFarmingMenu.itemSortByName)
			end
			for index,handItem in ipairs(waterSources) do
				local use = math.floor(handItem:getUsedDelta()/handItem:getUseDelta());
				-- prepare subMenu for water (we make a submenu for every lvl 5 by 5)
				local subMenu = context:getNew(subMenuWater);
				-- if waterLvl missing is below the max use of the water plant (so we can't have the option for 40 water if the plant have 80)
				local missingWaterUse = math.ceil((100 - currentPlant.waterLvl) / 5);
				if missingWaterUse < use then
					use = missingWaterUse;
				end
				subMenu:addOption(getText("ContextMenu_Full", use * 5), worldobjects, ISFarmingMenu.onWater, use, handItem, player, currentPlant, sq);
				if use > 10 then
					use = 10
				else
					use = use - 1
				end
				for i=use,1,-1 do
					subMenu:addOption(tostring(i * 5), worldobjects, ISFarmingMenu.onWater, i, handItem, player, currentPlant, sq);
				end
				if #waterSources > 1 then
					waterOption = subMenuWater:addOption(handItem:getName(), worldobjects, nil);
				end
				-- we add the subMenu to our current option (Water)
				context:addSubMenu(waterOption, subMenu);
			end
		end
	end
	-- disease
	if cureMildew or cureFlies then
		-- we try to get the cure for mildew
		if not handItem or handItem:getType() ~= "GardeningSprayMilk" or (handItem:getDrainableUsesInt() == 0) then
			ISFarmingMenu.GardeningSprayMilk = playerInv:getFirstTypeEvalArgRecurse("GardeningSprayMilk", predicateDrainableUsesInt, 1)
		else
			ISFarmingMenu.GardeningSprayMilk = handItem;
		end
		-- we try to get the cure for flies
		if not handItem or handItem:getType() ~= "GardeningSprayCigarettes" or (handItem:getDrainableUsesInt() == 0) then
			ISFarmingMenu.GardeningSprayCigarettes = playerInv:getFirstTypeEvalArgRecurse("GardeningSprayCigarettes", predicateDrainableUsesInt, 1)
		else
			ISFarmingMenu.GardeningSprayCigarettes = handItem;
		end
--		if ISFarmingMenu.GardeningSprayMilk or ISFarmingMenu.GardeningSprayCigarettes then
		if test then return ISWorldObjectContextMenu.setTest() end
		local diseaseOption = context:addOption(getText("ContextMenu_Treat_Problem"), worldobjects, nil);
		local subMenuCure = context:getNew(context);
		context:addSubMenu(diseaseOption, subMenuCure);
		if currentPlant.mildewLvl > 0 then
			if ISFarmingMenu.GardeningSprayMilk then
				-- we get how many use we can do on our item
				local use = ISFarmingMenu.GardeningSprayMilk:getDrainableUsesInt()
				if use > 0 then
					-- prepare subMenu for mildew
					local mildewMenu = subMenuCure:addOption(getText("Farming_Mildew"), worldobjects, nil);
					-- now submenu for lvl of mildew you want to cure (5 by 5)
					local subMenuMildew = context:getNew(subMenuCure);
					if use > 10 then
						use = 10;
					end
					local mildewLvl = 0;
					for i=1, use do
						mildewLvl = i * 5;
						subMenuMildew:addOption(mildewLvl .. "", worldobjects, ISFarmingMenu.onMildewCure, i, sq, player);
					end
					context:addSubMenu(mildewMenu, subMenuMildew);
				else
					local flieMenu = subMenuCure:addOption(getText("Farming_Mildew"), worldobjects, nil);
					flieMenu.notAvailable = true;
					local tooltip = ISWorldObjectContextMenu.addToolTip();
					local spray = InventoryItemFactory.CreateItem("GardeningSprayMilk"):getDisplayName();
					tooltip.description = getText("Farming_MissingItem", spray);
					flieMenu.toolTip = tooltip;
				end
			end
		end
		if currentPlant.fliesLvl > 0  then
			if ISFarmingMenu.GardeningSprayCigarettes then
				-- we get how many use we can do on our item
				local use = ISFarmingMenu.GardeningSprayCigarettes:getDrainableUsesInt()
				if use > 0 then
					-- prepare subMenu for mildew
					local flieMenu = subMenuCure:addOption(getText("Farming_Pest_Flies"), worldobjects, nil);
					-- now submenu for lvl of flies you want to cure (5 by 5)
					local subMenuFlie = context:getNew(subMenuCure);
					if use > 10 then
						use = 10;
					end
					local fliesLvl = 0;
					for i=1, use do
						fliesLvl = i * 5;
						subMenuFlie:addOption(fliesLvl .. "", worldobjects, ISFarmingMenu.onFliesCure, i, sq, player);
					end
					context:addSubMenu(flieMenu, subMenuFlie);
				end
			else
				local flieMenu = subMenuCure:addOption(getText("Farming_Pest_Flies"), worldobjects, nil);
				flieMenu.notAvailable = true;
				local tooltip = ISWorldObjectContextMenu.addToolTip();
				local spray = getScriptManager():FindItem("farming.GardeningSprayCigarettes"):getDisplayName();
				tooltip.description = getText("Farming_MissingItem", spray);
				flieMenu.toolTip = tooltip;
			end
		end
--		end
	end
	if ISFarmingMenu.cheat and currentPlant then
		if test then return ISWorldObjectContextMenu.setTest() end
		local option = context:addOption("Cheat", worldobjects, nil);
		local subMenu = context:getNew(context);
		context:addSubMenu(option, subMenu);
		subMenu:addOption("Grow", worldobjects, ISFarmingMenu.onCheatGrow, currentPlant);
		subMenu:addOption("Water To Max", worldobjects, ISFarmingMenu.onCheatWater, currentPlant);
		subMenu:addOption("Zero Water", worldobjects, ISFarmingMenu.onCheat, currentPlant, { var = 'waterLvl', count = -currentPlant.waterLvl });
		subMenu:addOption("Flies +5", worldobjects, ISFarmingMenu.onCheat, currentPlant, { var = 'fliesLvl', count = 5 });
		subMenu:addOption("Flies -5", worldobjects, ISFarmingMenu.onCheat, currentPlant, { var = 'fliesLvl', count = -5 });
		subMenu:addOption("Mildew +5", worldobjects, ISFarmingMenu.onCheat, currentPlant, { var = 'mildewLvl', count = 5 });
		subMenu:addOption("Mildew -5", worldobjects, ISFarmingMenu.onCheat, currentPlant, { var = 'mildewLvl', count = -5 });
		subMenu:addOption("Aphids +5", worldobjects, ISFarmingMenu.onCheat, currentPlant, { var = 'aphidLvl', count = 5 });
		subMenu:addOption("Aphids -5", worldobjects, ISFarmingMenu.onCheat, currentPlant, { var = 'aphidLvl', count = -5 });
	end
end

ISFarmingMenu.canDigHere = function(worldObjects)
	local squares = {}
	local didSquare = {}
	for _,worldObj in ipairs(worldObjects) do
		if not didSquare[worldObj:getSquare()] then
			table.insert(squares, worldObj:getSquare())
			didSquare[worldObj:getSquare()] = true
		end
	end
	for _,square in ipairs(squares) do
		if CFarmingSystem.instance:getLuaObjectOnSquare(square) then
			return false
		end
		for i=1,square:getObjects():size() do
			local obj = square:getObjects():get(i-1);
			if obj:getTextureName() and (luautils.stringStarts(obj:getTextureName(), "floors_exterior_natural") or
					luautils.stringStarts(obj:getTextureName(), "blends_natural_01")) then
				return true
			end
		end
	end
	return false
end

ISFarmingMenu.canPlow = function(seedAvailable, typeOfSeed, option)
	local tooltip = ISToolTip:new();
	tooltip:initialise();
	tooltip:setVisible(false);
	option.toolTip = tooltip;
	tooltip:setName(getText("Farming_" .. typeOfSeed));
	local result = true;
	tooltip.description = getText("Farming_Tooltip_MinWater") .. farming_vegetableconf.props[typeOfSeed].waterLvl .. "";
	if farming_vegetableconf.props[typeOfSeed].waterLvlMax then
		tooltip.description = tooltip.description .. " <LINE> " .. getText("Farming_Tooltip_MaxWater") ..  farming_vegetableconf.props[typeOfSeed].waterLvlMax;
	end
	tooltip.description = tooltip.description .. " <LINE> " .. getText("Farming_Tooltip_TimeOfGrow") .. math.floor((farming_vegetableconf.props[typeOfSeed].timeToGrow * 7) / 24) .. " " .. getText("IGUI_Gametime_days");
    local waterPlus = "";
    if farming_vegetableconf.props[typeOfSeed].waterLvlMax then
        waterPlus = "-" .. farming_vegetableconf.props[typeOfSeed].waterLvlMax;
    end
    tooltip.description = tooltip.description .. " <LINE> " .. getText("Farming_Tooltip_AverageWater") .. farming_vegetableconf.props[typeOfSeed].waterLvl .. waterPlus;
	local rgb = "";
	if seedAvailable < farming_vegetableconf.props[typeOfSeed].seedsRequired then
		result = false;
		rgb = "<RGB:1,0,0>";
	end
	tooltip.description = tooltip.description .. " <LINE> " .. rgb .. getText("Farming_Tooltip_RequiredSeeds") .. seedAvailable .. "/" .. farming_vegetableconf.props[typeOfSeed].seedsRequired;
	tooltip:setTexture(farming_vegetableconf.props[typeOfSeed].texture);
	if not result then
		option.onSelect = nil;
		option.notAvailable = true;
    end
    tooltip:setWidth(170);
end

function ISFarmingMenu.isValidPlant(plant)
	if not plant then return false end
	plant:updateFromIsoObject()
	return plant:getIsoObject() ~= nil
end

function ISFarmingMenu.walkToPlant(playerObj, square)
	if ISFarmingMenu.cheat or playerObj:isTimedActionInstant() then
		return true;
	end
	if AdjacentFreeTileFinder.isTileOrAdjacent(playerObj:getCurrentSquare(), square) then
		return true
	end
	local adjacent = AdjacentFreeTileFinder.Find(square, playerObj)
	if adjacent == nil then return false end
	ISTimedActionQueue.add(ISWalkToTimedAction:new(playerObj, adjacent))
	return true
end

ISFarmingMenu.onHarvest = function(worldobjects, plantToharvest, sq, playerObj)
	if ISFarmingMenu.walkToPlant(playerObj, sq) then
		ISTimedActionQueue.add(ISHarvestPlantAction:new(playerObj, plantToharvest, 100))
	end
	if playerObj:getJoypadBind() ~= -1 then
		return
	end
	ISFarmingMenu.cursor = ISFarmingCursorMouse:new(playerObj, ISFarmingMenu.onHarvestSquareSelected, ISFarmingMenu.isHarvestValid)
	getCell():setDrag(ISFarmingMenu.cursor, playerObj:getPlayerNum())
end

function ISFarmingMenu:onHarvestSquareSelected()
	local cursor = ISFarmingMenu.cursor;
	local plant = CFarmingSystem.instance:getLuaObjectOnSquare(cursor.sq)

	if not ISFarmingMenu.walkToPlant(cursor.character, cursor.sq) then
		return;
	end
	
	ISTimedActionQueue.add(ISHarvestPlantAction:new(cursor.character, plant, 100));
end

function ISFarmingMenu:isHarvestValid()
	if not ISFarmingMenu.cursor then return false; end
	local cursor = ISFarmingMenu.cursor;
	local plant = CFarmingSystem.instance:getLuaObjectOnSquare(cursor.sq)
	local plantName = ISFarmingMenu.getPlantName(plant);
	cursor.tooltipTxt = plantName .. " ";

	if not ISFarmingMenu.isValidPlant(plant) or (plant.state ~= "seeded") then
		cursor.tooltipTxt = "<RGB:1,0,0> " .. getText("Farming_Tooltip_NotAPlant");
		return false;
	end
	
	if not plant:canHarvest() then
		cursor.tooltipTxt = plantName .. " <LINE> <RGB:1,0,0> " .. getText("Farming_Tooltip_NotReadyToHarvest");
		return false;
	end
	
	return true;
end

ISFarmingMenu.onMildewCure = function(worldobjects, uses, sq, playerObj)
	if not ISFarmingMenu.walkToPlant(playerObj, sq) then
		return;
	end
	ISWorldObjectContextMenu.equip(playerObj, playerObj:getPrimaryHandItem(), ISFarmingMenu.GardeningSprayMilk, true)
	ISTimedActionQueue.add(ISCureMildewAction:new(playerObj, ISFarmingMenu.GardeningSprayMilk, uses, CFarmingSystem.instance:getLuaObjectOnSquare(sq), 10 * (uses * 5)));
end

ISFarmingMenu.onFliesCure = function(worldobjects, uses, sq, playerObj)
	if not ISFarmingMenu.walkToPlant(playerObj, sq) then
		return;
	end
	ISWorldObjectContextMenu.equip(playerObj, playerObj:getPrimaryHandItem(), ISFarmingMenu.GardeningSprayCigarettes, true)
	ISTimedActionQueue.add(ISCureFliesAction:new(playerObj, ISFarmingMenu.GardeningSprayCigarettes, uses, CFarmingSystem.instance:getLuaObjectOnSquare(sq), 10 * (uses * 5)));
end

ISFarmingMenu.onInfo = function(worldobjects, plant, sq, playerObj)
	if ISFarmingMenu.walkToPlant(playerObj, sq) then
		ISTimedActionQueue.add(ISPlantInfoAction:new(playerObj, plant))
	end
	if playerObj:getJoypadBind() ~= -1 then
		return
	end
	ISFarmingMenu.cursor = ISFarmingCursorMouse:new(playerObj, ISFarmingMenu.onInfoSquareSelected, ISFarmingMenu.isInfoValid)
	getCell():setDrag(ISFarmingMenu.cursor, playerObj:getPlayerNum())
end

function ISFarmingMenu:onInfoSquareSelected()
	local cursor = ISFarmingMenu.cursor;
	local plant = CFarmingSystem.instance:getLuaObjectOnSquare(cursor.sq)

	if not ISFarmingMenu.walkToPlant(cursor.character, cursor.sq) then
		return;
	end
	
	ISTimedActionQueue.add(ISPlantInfoAction:new(cursor.character, plant))
end

function ISFarmingMenu:isInfoValid()
	if not ISFarmingMenu.cursor then return false; end
	local cursor = ISFarmingMenu.cursor;
	local plant = CFarmingSystem.instance:getLuaObjectOnSquare(cursor.sq)
	return plant ~= nil and plant.typeOfSeed ~= "none"
end

ISFarmingMenu.onWater = function(worldobjects, uses, handItem, playerObj, plant, sq)
	local use = math.floor(handItem:getUsedDelta()/handItem:getUseDelta());

	-- if waterLvl missing is below the max use of the water plant (so we can't have the option for 40 water if the plant have 80)
	local missingWaterUse = math.ceil((100 - plant.waterLvl) / 5);
	if missingWaterUse < use then
		use = missingWaterUse;
	end
	if use > 10 then
		use = 10
	end
	
	if use > uses then
		use = uses;
	end

	if playerObj:getPrimaryHandItem() ~= handItem then
		ISTimedActionQueue.add(ISEquipWeaponAction:new(playerObj, handItem, 50, true));
	end
	if not ISFarmingMenu.walkToPlant(playerObj, sq) then
		return;
	end
	ISTimedActionQueue.add(ISWaterPlantAction:new(playerObj, handItem, use, sq, 20 + (6 * use)));
	if playerObj:getJoypadBind() ~= -1 then
		return
	end
	ISFarmingMenu.cursor = ISFarmingCursorMouse:new(playerObj, ISFarmingMenu.onWaterSquareSelected, ISFarmingMenu.isWaterValid)
	getCell():setDrag(ISFarmingMenu.cursor, playerObj:getPlayerNum())
	ISFarmingMenu.cursor.handItem = handItem;
	ISFarmingMenu.cursor.uses = uses;
end

function ISFarmingMenu:onWaterSquareSelected()
	local cursor = ISFarmingMenu.cursor;
	local plant = CFarmingSystem.instance:getLuaObjectOnSquare(cursor.sq)
	local use = math.floor(cursor.handItem:getUsedDelta()/cursor.handItem:getUseDelta());

	-- if waterLvl missing is below the max use of the water plant (so we can't have the option for 40 water if the plant have 80)
	local missingWaterUse = math.ceil((100 - plant.waterLvl) / 5);
	if missingWaterUse < use then
		use = missingWaterUse;
	end
	if use > 10 then
		use = 10
	end
	
	if use > cursor.uses then
		use = cursor.uses;
	end

	if cursor.character:getPrimaryHandItem() ~= cursor.handItem then
		ISTimedActionQueue.add(ISEquipWeaponAction:new(cursor.character, cursor.handItem, 50, true));
	end
	if not ISFarmingMenu.walkToPlant(cursor.character, cursor.sq) then
		return;
	end
	ISTimedActionQueue.add(ISWaterPlantAction:new(cursor.character, cursor.handItem, use, cursor.sq, 20 + (6 * use)));
end

ISFarmingMenu.getPlantName = function(plant)
	if not plant then
		return "";
	end
	
	if plant:getObject() then
		return plant:getObject():getObjectName();
	else
		return "Dead " .. getText("Farming_" .. plant.typeOfSeed);
	end
end

function ISFarmingMenu:isWaterValid()
	if not ISFarmingMenu.cursor then return false; end
	local valid = true;
	local cursor = ISFarmingMenu.cursor;
	cursor.tooltipTxt = "";
	local plant = CFarmingSystem.instance:getLuaObjectOnSquare(cursor.sq)
	local plantName = ISFarmingMenu.getPlantName(plant);
	
	if not ISFarmingMenu.isValidPlant(plant) then
		cursor.tooltipTxt = "<RGB:1,0,0> " .. getText("Farming_Tooltip_NotAPlant");
		return false;
	end
	
	if plant and plant.state ~= "seeded" then
		cursor.tooltipTxt = "<RGB:1,0,0> " .. getText("Farming_Tooltip_FurrowNeedsSeed");
		return false;
	end
	
	cursor.tooltipTxt = plantName .. " <LINE> ";
	local use = 0;
	if cursor.handItem then
		use = math.floor(cursor.handItem:getUsedDelta()/cursor.handItem:getUseDelta());
	end
	
	-- if waterLvl missing is below the max use of the water plant (so we can't have the option for 40 water if the plant have 80)
	local missingWaterUse = math.ceil((100 - plant.waterLvl) / 5);
	if missingWaterUse < use then
		use = missingWaterUse;
	end
	if use > 10 then
		use = 10
	end
	
	if missingWaterUse <= 0 then
		cursor.tooltipTxt = plantName .. " <LINE> <RGB:1,0,0> " .. getText("Farming_Tooltip_PlantHasEnoughWater");
		return false;
	end

	if use > cursor.uses then
		use = cursor.uses;
	end
	
	if use <= 0 then
		-- try to reselect another water source
		local playerInv = cursor.character:getInventory();
		local waterSources = {};
		cursor.handItem = nil;
		for i = 0, playerInv:getItems():size() - 1 do
			local item = playerInv:getItems():get(i);
			if item:isWaterSource() and math.floor(item:getUsedDelta()/item:getUseDelta()) > 0 then
				table.insert(waterSources, item)
			end
		end
		-- we'll select the best water source available, one with required water we want, otherwise the best next one
		if #waterSources > 0 then
			for index,handItem in ipairs(waterSources) do
				local use = math.floor(handItem:getUsedDelta()/handItem:getUseDelta());
				if use >= cursor.uses then
					cursor.handItem = handItem;
					return true;
				else
					if not cursor.handItem or use > math.floor(handItem:getUsedDelta()/handItem:getUseDelta())then
						cursor.handItem = handItem;
					end
				end
			end
		end
			
		cursor.tooltipTxt = "<RGB:1,0,0> " .. getText("Farming_Tooltip_NotEnoughWaterInInventory");
		return false;
	end

	local farmingLevel = CFarmingSystem.instance:getXp(cursor.character)
	local water_rgb = ISFarmingInfo.getWaterLvlColor(plant, farmingLevel)
	water_rgb = string.format(" <RGB:%.2f,%.2f,%.2f> ", water_rgb.r, water_rgb.g, water_rgb.b)
	if farmingLevel <= 4 then
		cursor.tooltipTxt = cursor.tooltipTxt .. getText("Farming_Water_levels") .. ": " .. water_rgb .. ISFarmingInfo.getWaterLvl(plant, farmingLevel) .. " <LINE> ";
	else
		cursor.tooltipTxt = cursor.tooltipTxt .. getText("Farming_Water_levels") .. ": " .. water_rgb .. tostring(100-(missingWaterUse* 5)) .. " / 100 <LINE> ";
	end
	cursor.tooltipTxt = cursor.tooltipTxt .. " <RGB:1,1,1> " .. getText("Farming_Tooltip_ItemUsed") .. ": " .. cursor.handItem:getDisplayName() .. " (" ..  tostring(use * 5) .. " / " .. tostring(math.floor(cursor.handItem:getUsedDelta()/cursor.handItem:getUseDelta()) * 5) .. ")";
	
	return valid;
end

function ISFarmingMenu:doSeedMenu(context, plant, sq, playerObj)
	local seedOption = context:addOption(getText("ContextMenu_Sow_Seed"), nil, nil)
	local subMenu = context:getNew(context)
	context:addSubMenu(seedOption, subMenu)

	-- Sort seed types by display name.
	local typeOfSeedList = {}
	for typeOfSeed,props in pairs(farming_vegetableconf.props) do
		table.insert(typeOfSeedList, { typeOfSeed = typeOfSeed, props = props, text = getText("Farming_" .. typeOfSeed) })
	end
	table.sort(typeOfSeedList, function(a,b) return not string.sort(a.text, b.text) end)

	for _,tos in ipairs(typeOfSeedList) do
		local typeOfSeed = tos.typeOfSeed
		local option = subMenu:addOption(tos.text, playerObj, ISFarmingMenu.onSeed, typeOfSeed, plant, sq)
		local nbOfSeed = playerObj:getInventory():getCountTypeRecurse(tos.props.seedName)
		ISFarmingMenu.canPlow(nbOfSeed, typeOfSeed, option)
	end
end

function ISFarmingMenu.onSeed(playerObj, typeOfSeed, plant, sq)
	if ISFarmingMenu.walkToPlant(playerObj, sq) then
		local playerInv = playerObj:getInventory()
		local props = farming_vegetableconf.props[typeOfSeed]
		local items = playerInv:getSomeTypeRecurse(props.seedName, props.seedsRequired)
		ISInventoryPaneContextMenu.transferIfNeeded(playerObj, items)
		local seeds = {}
		for i=1,items:size() do
			local item = items:get(i-1)
			table.insert(seeds, items:get(i-1))
		end
		ISTimedActionQueue.add(ISSeedAction:new(playerObj, seeds, props.seedsRequired, typeOfSeed, plant, 40))
	end
	if playerObj:getJoypadBind() ~= -1 then
		return
	end
	ISFarmingMenu.cursor = ISFarmingCursorMouse:new(playerObj, ISFarmingMenu.onSeedSquareSelected, ISFarmingMenu.isSeedValid)
	getCell():setDrag(ISFarmingMenu.cursor, playerObj:getPlayerNum())
	ISFarmingMenu.cursor.typeOfSeed = typeOfSeed;
end

function ISFarmingMenu:onSeedSquareSelected()
	local cursor = ISFarmingMenu.cursor;
	if not ISFarmingMenu.walkToPlant(cursor.character, cursor.sq) then
		return
	end
	
	local plant = CFarmingSystem.instance:getLuaObjectOnSquare(cursor.sq)
	local playerInv = cursor.character:getInventory()
	local props = farming_vegetableconf.props[cursor.typeOfSeed]
	local items = playerInv:getSomeTypeRecurse(props.seedName, props.seedsRequired)
	ISInventoryPaneContextMenu.transferIfNeeded(cursor.character, items)

	local seeds = {}
	for i=1,items:size() do
		local item = items:get(i-1)
		table.insert(seeds, items:get(i-1))
	end
	
	ISTimedActionQueue.add(ISSeedAction:new(cursor.character, seeds, props.seedsRequired, cursor.typeOfSeed, plant, 40))
end

function ISFarmingMenu:isSeedValid()
	local valid = true;
	local cursor = ISFarmingMenu.cursor;
	
	local playerInv = cursor.character:getInventory()
	local props = farming_vegetableconf.props[cursor.typeOfSeed]
--	local items = playerInv:getSomeTypeRecurse(props.seedName, props.seedsRequired)
	local items = playerInv:getItemsFromType(props.seedName);
	cursor.tooltipTxt = getText("ContextMenu_Sow_Seed") .. ": "
	local color = " <RGB:1,1,1> "
	local seedText
	local scriptItem = getScriptManager():FindItem(props.seedName)
	if scriptItem then
		seedText = scriptItem:getDisplayName() .. " (";
	else
		seedText = cursor.typeOfSeed .. " seeds (";
	end
	seedText = seedText .. items:size() .. " / " .. props.seedsRequired  .. ")";
	if items:size() < props.seedsRequired then
		color = " <RGB:1,0,0> "
		valid = false;
	end
	cursor.tooltipTxt = cursor.tooltipTxt .. color .. seedText

	local plant = CFarmingSystem.instance:getLuaObjectOnSquare(cursor.sq)
	local plantName = ISFarmingMenu.getPlantName(plant);

	if not ISFarmingMenu.isValidPlant(plant) then
		cursor.tooltipTxt = cursor.tooltipTxt .. " <LINE> <RGB:1,0,0> " .. getText("Farming_Tooltip_NotAFurrow");
		valid = false;
	end
	
	if plant and plant.state ~= "plow" then
		cursor.tooltipTxt = " <RGB:1,1,1> " .. plantName .. " <LINE> " ..  cursor.tooltipTxt .. " <LINE> <RGB:1,0,0> " .. getText("Farming_Tooltip_FurrowHasSeeds");
		valid = false;
	end

	return valid;
end

ISFarmingMenu.onPlow = function(worldobjects, player, handItem)
	local bo = farmingPlot:new(handItem, player);
	bo.player = player:getPlayerNum()
	getCell():setDrag(bo, bo.player)
end

ISFarmingMenu.onShovel = function(worldobjects, plant, player, sq)
    if not ISFarmingMenu.walkToPlant(player, sq) then
        return;
    end
    local handItem = ISWorldObjectContextMenu.equip(player, player:getPrimaryHandItem(), ISFarmingMenu.getShovel(player), true);
    ISTimedActionQueue.add(ISShovelAction:new(player, handItem, plant, 40));
end

ISFarmingMenu.onFertilize = function(worldobjects, handItem, plant, sq, playerObj)
    -- close the farming info window to avoid concurrent gorwing phase problem
    if not ISFarmingMenu.walkToPlant(playerObj, sq) then
        return;
    end
    if playerObj:getInventory():containsTypeRecurse("CompostBag") then
        handItem = ISWorldObjectContextMenu.equip(playerObj, handItem, "CompostBag", true);
    else
        handItem = ISWorldObjectContextMenu.equip(playerObj, handItem, "Fertilizer", true);
    end
	ISTimedActionQueue.add(ISFertilizeAction:new(playerObj, handItem, plant, 40));

	if playerObj:getJoypadBind() ~= -1 then
		return
	end

	ISFarmingMenu.cursor = ISFarmingCursorMouse:new(playerObj, ISFarmingMenu.onFertilizeSquareSelected, ISFarmingMenu.isFertilizeValid)
	getCell():setDrag(ISFarmingMenu.cursor, playerObj:getPlayerNum())
end

function ISFarmingMenu:isFertilizeValid()
	if not ISFarmingMenu.cursor then return false; end
	local valid = true
	local cursor = ISFarmingMenu.cursor
	local playerObj = cursor.character
	local playerInv = playerObj:getInventory()
	local plant = CFarmingSystem.instance:getLuaObjectOnSquare(cursor.sq)
	local plantName = ISFarmingMenu.getPlantName(plant)
	
	if not ISFarmingMenu.isValidPlant(plant) then
		cursor.tooltipTxt = "<RGB:1,0,0> " .. getText("Farming_Tooltip_NotAPlant")
		return false
	end

	cursor.tooltipTxt = plantName .. " <LINE> ";
	cursor.tooltipTxt = cursor.tooltipTxt .. getText("Farming_Fertilized") .. " : " .. plant.fertilizer

	if playerInv:containsTypeRecurse("Fertilizer") or playerInv:containsTypeRecurse("CompostBag") then
		return true
	end

	return false
end

function ISFarmingMenu:onFertilizeSquareSelected()
	local cursor = ISFarmingMenu.cursor
	local playerObj = cursor.character
	if not ISFarmingMenu.walkToPlant(playerObj, cursor.sq) then
		return
	end
	local plant = CFarmingSystem.instance:getLuaObjectOnSquare(cursor.sq)
	local handItem = playerObj:getPrimaryHandItem() 
	if playerObj:getInventory():containsTypeRecurse("CompostBag") then
		handItem = ISWorldObjectContextMenu.equip(playerObj, handItem, "CompostBag", true)
	else
		handItem = ISWorldObjectContextMenu.equip(playerObj, handItem, "Fertilizer", true)
	end
	ISTimedActionQueue.add(ISFertilizeAction:new(playerObj, handItem, plant, 40))
end

ISFarmingMenu.onCheatGrow = function(worldobjects, plant)
	local hours = CFarmingSystem.instance.hoursElapsed - plant.nextGrowing
	local args = { var = 'nextGrowing', count = hours }
	ISFarmingMenu.onCheat(worldobjects, plant, args)
end

function ISFarmingMenu.onCheatWater(worldobjects, plant)
	local diff = 100 - plant.waterLvl
	if plant.waterNeededMax then
		diff = plant.waterNeededMax - plant.waterLvl
	end
	local args = { var = 'waterLvl', count = diff }
	ISFarmingMenu.onCheat(worldobjects, plant, args)
end

function ISFarmingMenu.onJoypadFarming(square, player)
	local bo = ISFarmingCursor:new(getSpecificPlayer(player))
	getCell():setDrag(bo, bo.player)
	bo.xJoypad = square:getX()
	bo.yJoypad = square:getY()
	bo.zJoypad = square:getZ()
	return
end

ISFarmingMenu.onCheat = function(worldobjects, plant, args)
	args.x = plant.x
	args.y = plant.y
	args.z = plant.z
	CFarmingSystem.instance:sendCommand(getSpecificPlayer(0), 'cheat', args)
end

ISFarmingMenu.getShovelAnim = function(item)
	if not item then
		return CharacterActionAnims.Dig
	end
	if item:getType() == "HandShovel" or item:getType() == "HandFork" then
		return CharacterActionAnims.DigTrowel
	elseif item:getType() == "GardenHoe" then
		return CharacterActionAnims.DigHoe
	elseif item:getType() == "PickAxe" then
		return CharacterActionAnims.DigPickAxe
	else
		return CharacterActionAnims.DigShovel
	end
end

Events.OnFillWorldObjectContextMenu.Add(ISFarmingMenu.doFarmingMenu);
