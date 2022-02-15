--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

--require 'Camping/camping_fuel'

ISBBQMenu = {}

local function predicateNotEmpty(item)
	return item:getUsedDelta() > 0
end

local function predicateFuel(item, arg)
	local fuelList = arg.fuelList
	local tinderList = arg.tinderList
	local itemCount = arg.itemCount
	local playerObj = arg.playerObj

	if not itemCount[item:getName()] then
		if ISCampingMenu.isValidFuel(item) then
			table.insert(fuelList, item)
		end
		if ISCampingMenu.isValidTinder(item) then
			table.insert(tinderList, item)
		end
		itemCount[item:getName()] = 0
	end

	itemCount[item:getName()] = itemCount[item:getName()] + ISCampingMenu.getFuelItemUses(item)

	return true
end

function ISBBQMenu.OnFillWorldObjectContextMenu(player, context, worldobjects, test)

	if test and ISWorldObjectContextMenu.Test then return true end

	local bbq = nil

	local objects = {}
	for _,object in ipairs(worldobjects) do
		local square = object:getSquare()
		if square then
			for i=1,square:getObjects():size() do
				local object2 = square:getObjects():get(i-1)
				if instanceof(object2, "IsoBarbecue") then
					bbq = object2
				end
			end
		end
	end

	if not bbq then return end

	local playerObj = getSpecificPlayer(player)
	local playerInv = playerObj:getInventory()

	if test then return ISWorldObjectContextMenu.setTest() end
	local option = context:addOption(getText("ContextMenu_BBQInfo"), worldobjects, ISBBQMenu.onDisplayInfo, player, bbq)
	local fireState;
	if bbq:isLit() then
		fireState = getText("IGUI_Fireplace_Burning")
	elseif bbq:isSmouldering() then
		fireState = getText("IGUI_Fireplace_Smouldering")
	else
		fireState = getText("IGUI_Fireplace_Unlit")
	end
	if playerObj:DistToSquared(bbq:getX() + 0.5, bbq:getY() + 0.5) < 2 * 2 then
		option.toolTip = ISWorldObjectContextMenu:addToolTip()
		option.toolTip:setName(bbq:isPropaneBBQ() and getText("IGUI_BBQ_TypePropane") or getText("IGUI_BBQ_TypeCharcoal"))
		option.toolTip.description = getText("IGUI_BBQ_FuelAmount", ISCampingMenu.timeString(bbq:getFuelAmount())) .. " (" .. fireState .. ")"
		if bbq:isPropaneBBQ() and not bbq:hasPropaneTank() then
			option.toolTip.description = option.toolTip.description .. " <LINE> <RGB:1,0,0> " .. getText("IGUI_BBQ_NeedsPropaneTank")
		end
	end

	if bbq:isPropaneBBQ() then
		if bbq:hasFuel() then
			if test then return ISWorldObjectContextMenu.setTest() end
			if bbq:isLit() then
				context:addOption(getText("ContextMenu_Turn_Off"), worldobjects, ISBBQMenu.onToggle, player, bbq)
			else
				context:addOption(getText("ContextMenu_Turn_On"), worldobjects, ISBBQMenu.onToggle, player, bbq)
			end
		end
		local tank = ISBBQMenu.FindPropaneTank(playerObj, bbq)
		if tank then
			if test then return ISWorldObjectContextMenu.setTest() end
			context:addOption(getText("ContextMenu_Insert_Propane_Tank"), worldobjects, ISBBQMenu.onInsertPropaneTank, player, bbq, tank)
		end
		if bbq:hasPropaneTank() then
			if test then return ISWorldObjectContextMenu.setTest() end
			context:addOption(getText("ContextMenu_Remove_Propane_Tank"), worldobjects, ISBBQMenu.onRemovePropaneTank, player, bbq)
		end
		return
	end

	-- Options for adding fuel
	local fuelList = {}
	local tinderList = {}
	local itemCount = {}
	local arg = {
		fuelList = fuelList,
		tinderList = tinderList,
		itemCount = itemCount,
		playerObj = playerObj
	}
	playerInv:getAllEvalArgRecurse(predicateFuel, arg)

	if #fuelList > 0 then
		if test then return ISWorldObjectContextMenu.setTest() end
		local fuelOption = context:addOption(campingText.addFuel, worldobjects, nil)
		local subMenuFuel = ISContextMenu:getNew(context)
		context:addSubMenu(fuelOption, subMenuFuel)

		if #fuelList > 1 then
			local numItems = 0
			local duration = 0
			for _,item in ipairs(fuelList) do
				local count = itemCount[item:getName()]
				duration = duration + (ISCampingMenu.getFuelDurationForItem(item) or 0.0) * count
				numItems = numItems + count
			end
			if numItems > 1 then
				local allOption = subMenuFuel:addActionsOption(getText("ContextMenu_AllWithCount", numItems), ISBBQMenu.onAddAllFuel, bbq)
				local tooltip = ISWorldObjectContextMenu.addToolTip()
				tooltip.description = getText("IGUI_BBQ_FuelAmount", ISCampingMenu.timeString(duration))
				allOption.toolTip = tooltip
			end
		end

		table.sort(fuelList, function(a,b) return not string.sort(a:getName(), b:getName()) end)
		for i,v in ipairs(fuelList) do
			local label = v:getName()
			local count = itemCount[v:getName()]
			if count > 1 then
				label = label..' ('..count..')'
				local subMenu = context:getNew(subMenuFuel)
				local subOption = subMenuFuel:addOption(label)
				subMenuFuel:addSubMenu(subOption, subMenu)

				local subOption1 = subMenu:addActionsOption(getText("ContextMenu_One"), ISBBQMenu.onAddFuel, bbq, v:getFullType())
				local tooltip = ISWorldObjectContextMenu.addToolTip()
				tooltip.description = getText("IGUI_BBQ_FuelAmount", ISCampingMenu.timeString(ISCampingMenu.getFuelDurationForItem(v)))
				subOption1.toolTip = tooltip

				local subOption2 = subMenu:addActionsOption(getText("ContextMenu_AllWithCount", count), ISBBQMenu.onAddMultipleFuel, bbq, v:getFullType())
				local tooltip = ISWorldObjectContextMenu.addToolTip()
				tooltip.description = getText("IGUI_BBQ_FuelAmount", ISCampingMenu.timeString(ISCampingMenu.getFuelDurationForItem(v) * count))
				subOption2.toolTip = tooltip
			else
				local subOption = subMenuFuel:addActionsOption(label, ISBBQMenu.onAddFuel, bbq, v:getFullType())
				local tooltip = ISWorldObjectContextMenu.addToolTip()
				tooltip.description = getText("IGUI_BBQ_FuelAmount", ISCampingMenu.timeString(ISCampingMenu.getFuelDurationForItem(v)))
				subOption.toolTip = tooltip
			end
		end
	end

	-- Options for lighting
	local branch = playerInv:getFirstTypeRecurse("TreeBranch")
	local lighter = playerInv:getFirstTypeRecurse("Lighter")
	local matches = playerInv:getFirstTypeRecurse("Matches")
	local petrol = playerInv:getFirstTypeEvalRecurse("PetrolCan", predicateNotEmpty)
	local percedWood = playerInv:getFirstTypeRecurse("PercedWood")
	local stick = playerInv:getFirstTypeRecurse("WoodenStick")
	
	local lightFromLiterature = nil
	local lightFromPetrol = nil
	local lightFromKindle = nil
	if (#tinderList > 0) and (lighter or matches) and not bbq:isLit() then
		lightFromLiterature = bbq
	end
	if (lighter or matches) and petrol and not bbq:isLit() and bbq:hasFuel() then
		lightFromPetrol = bbq
	end
	if percedWood and not bbq:isLit() and bbq:hasFuel() and (playerObj:getStats():getEndurance() > 0) then
		lightFromKindle = bbq
	end
	if lightFromPetrol or lightFromLiterature or lightFromKindle then
		if test then return ISWorldObjectContextMenu.setTest() end
		local lightOption = context:addOption(campingText.lightCampfire, worldobjects, nil)
		local subMenuLight = ISContextMenu:getNew(context)
		context:addSubMenu(lightOption, subMenuLight)
		if lightFromPetrol then
			if lighter then
				subMenuLight:addOption(petrol:getName()..' + '..lighter:getName(), worldobjects, ISBBQMenu.onLightFromPetrol, player, lighter, petrol, lightFromPetrol)
			end
			if matches then
				subMenuLight:addOption(petrol:getName()..' + '..matches:getName(), worldobjects, ISBBQMenu.onLightFromPetrol, player, matches, petrol, lightFromPetrol)
			end
		end
		table.sort(tinderList, function(a,b) return not string.sort(a:getName(), b:getName()) end)
		for i,v in pairs(tinderList) do
			local label = v:getName()
			local count = itemCount[v:getName()]
			if count > 1 then
				label = label..' ('..count..')'
			end
			if lighter then
				local subOption = subMenuLight:addActionsOption(label..' + '..lighter:getName(), ISBBQMenu.onLightFromLiterature, v:getFullType(), lighter, lightFromLiterature)
				local tooltip = ISWorldObjectContextMenu.addToolTip()
				tooltip.description = getText("IGUI_BBQ_FuelAmount", ISCampingMenu.timeString(ISCampingMenu.getFuelDurationForItem(v)))
				subOption.toolTip = tooltip
			end
			if matches then
				local subOption = subMenuLight:addActionsOption(label..' + '..matches:getName(), ISBBQMenu.onLightFromLiterature, v:getFullType(), matches, lightFromLiterature)
				local tooltip = ISWorldObjectContextMenu.addToolTip()
				tooltip.description = getText("IGUI_BBQ_FuelAmount", ISCampingMenu.timeString(ISCampingMenu.getFuelDurationForItem(v)))
				subOption.toolTip = tooltip
			end
		end
		if lightFromKindle then
			if stick then
				subMenuLight:addOption(percedWood:getName()..' + '..stick:getName(), worldobjects, ISBBQMenu.onLightFromKindle, player, percedWood, stick, lightFromKindle);
			elseif branch then
				subMenuLight:addOption(percedWood:getName()..' + '..branch:getName(), worldobjects, ISBBQMenu.onLightFromKindle, player, percedWood, branch, lightFromKindle);
			else
				local option = subMenuLight:addOption(percedWood:getName(), worldobjects, nil);
				option.notAvailable = true;
				local tooltip = ISWorldObjectContextMenu.addToolTip()
				tooltip:setName(percedWood:getName())
				tooltip.description = getText("Tooltip_lightFireNoStick")
				option.toolTip = tooltip
			end
		end
	end

	if bbq:isLit() then
		if test then return ISWorldObjectContextMenu.setTest() end
		context:addOption(campingText.putOutCampfire, worldobjects, ISBBQMenu.onExtinguish, player, bbq)
	end
end

function ISBBQMenu.onDisplayInfo(worldobjects, player, bbq)
	local playerObj = getSpecificPlayer(player)
	if not AdjacentFreeTileFinder.isTileOrAdjacent(playerObj:getCurrentSquare(), bbq:getSquare()) then
		local adjacent = AdjacentFreeTileFinder.Find(bbq:getSquare(), playerObj)
		if adjacent then
			ISTimedActionQueue.add(ISWalkToTimedAction:new(playerObj, adjacent))
			ISTimedActionQueue.add(ISBBQInfoAction:new(playerObj, bbq))
			return
		end
	else
		ISTimedActionQueue.add(ISBBQInfoAction:new(playerObj, bbq))
	end
end

function ISBBQMenu.FindPropaneTank(player, bbq)
	local tank = player:getInventory():getFirstTypeEvalRecurse("Base.PropaneTank", predicateNotEmpty)
	if tank and tank:getUsedDelta() > 0 then
		return tank
	end
	for y=bbq:getY()-1,bbq:getY()+1 do
		for x=bbq:getX()-1,bbq:getX()+1 do
			local square = getCell():getGridSquare(x, y, bbq:getZ())
			if square and not square:isSomethingTo(bbq:getSquare()) then
				local wobs = square:getWorldObjects()
				for i=0,wobs:size()-1 do
					local o = wobs:get(i)
					if o:getItem():getFullType() == "Base.PropaneTank" then
						if o:getItem():getUsedDelta() > 0 then
							return o
						end
					end
				end
			end
		end
	end
	return nil
end

function ISBBQMenu.onAddFuel(playerObj, bbq, fuelType)
	local playerInv = playerObj:getInventory()
	local fuelItem = playerInv:getFirstTypeEvalRecurse(fuelType, ISCampingMenu.isValidFuel)
	if not fuelItem then return end
	local fuelAmt = ISCampingMenu.getFuelDurationForItem(fuelItem)
	if not fuelAmt or fuelAmt <= 0 then return end
	ISWorldObjectContextMenu.transferIfNeeded(playerObj, fuelItem)
	if luautils.walkAdj(playerObj, bbq:getSquare(), true) then
		if playerObj:isEquipped(fuelItem) then
			ISTimedActionQueue.add(ISUnequipAction:new(playerObj, fuelItem, 50));
		end
		ISTimedActionQueue.add(ISBBQAddFuel:new(playerObj, bbq, fuelItem, fuelAmt, 100))
	end
end

local function addFuel(playerObj, bbq, fuelItems)
	if fuelItems:isEmpty() then return end
	ISInventoryPaneContextMenu.transferIfNeeded(playerObj, fuelItems)
	if not luautils.walkAdj(playerObj, bbq:getSquare(), true) then return end
	for i=1,fuelItems:size() do
		local fuelItem = fuelItems:get(i-1)
		if playerObj:isEquipped(fuelItem) then
			ISTimedActionQueue.add(ISUnequipAction:new(playerObj, fuelItem, 50))
		end
		local fuelAmt = ISCampingMenu.getFuelDurationForItem(fuelItem)
		local uses = ISCampingMenu.getFuelItemUses(fuelItem)
		for j=1,uses do
			ISTimedActionQueue.add(ISBBQAddFuel:new(playerObj, bbq, fuelItem, fuelAmt, 100))
		end
	end
end

ISBBQMenu.onAddMultipleFuel = function(playerObj, bbq, fuelType)
	local fuelItems = ArrayList.new()
	local containers = ISInventoryPaneContextMenu.getContainers(playerObj)
	for i=1,containers:size() do
		local container = containers:get(i-1)
		container:getAllTypeEval(fuelType, ISCampingMenu.isValidFuel, fuelItems)
	end
	addFuel(playerObj, bbq, fuelItems)
end

ISBBQMenu.onAddAllFuel = function(playerObj, bbq)
	local fuelItems = ArrayList.new()
	local containers = ISInventoryPaneContextMenu.getContainers(playerObj)
	for i=1,containers:size() do
		local container = containers:get(i-1)
		container:getAllEval(ISCampingMenu.isValidFuel, fuelItems)
	end
	addFuel(playerObj, bbq, fuelItems)
end

function ISBBQMenu.onLightFromLiterature(playerObj, itemType, lighter, bbq)
	local playerInv = playerObj:getInventory()
	local fuelItem = playerInv:getFirstTypeEvalRecurse(itemType, ISCampingMenu.isValidTinder)
	if not fuelItem then return end
	if luautils.walkAdj(playerObj, bbq:getSquare()) then
		ISWorldObjectContextMenu.transferIfNeeded(playerObj, lighter)
		ISWorldObjectContextMenu.transferIfNeeded(playerObj, fuelItem)
		ISTimedActionQueue.add(ISBBQLightFromLiterature:new(playerObj, fuelItem, lighter, bbq, 100))
	end
end

function ISBBQMenu.onLightFromPetrol(worldobjects, player, lighter, petrol, bbq)
	local playerObj = getSpecificPlayer(player)
	if luautils.walkAdj(playerObj, bbq:getSquare()) then
		ISWorldObjectContextMenu.transferIfNeeded(playerObj, lighter)
		ISWorldObjectContextMenu.transferIfNeeded(playerObj, petrol)
		ISTimedActionQueue.add(ISBBQLightFromPetrol:new(playerObj, bbq, lighter, petrol, 20))
	end
end

function ISBBQMenu.onLightFromKindle(worldobjects, player, percedWood, stickOrBranch, bbq)
	local playerObj = getSpecificPlayer(player)
	if luautils.walkAdj(playerObj, bbq:getSquare(), true) then
		ISTimedActionQueue.add(ISBBQLightFromKindle:new(playerObj, percedWood, stickOrBranch, bbq, 1500))
	end
end

function ISBBQMenu.onExtinguish(worldobjects, player, bbq)
	local playerObj = getSpecificPlayer(player)
	if luautils.walkAdj(playerObj, bbq:getSquare()) then
		ISTimedActionQueue.add(ISBBQExtinguish:new(playerObj, bbq, 60))
	end
end

function ISBBQMenu.onInsertPropaneTank(worldobjects, player, bbq, tank)
	local playerObj = getSpecificPlayer(player)
	local square = bbq:getSquare()
	if instanceof(tank, "IsoWorldInventoryObject") then
		if playerObj:getSquare() ~= tank:getSquare() then
			ISTimedActionQueue.add(ISWalkToTimedAction:new(playerObj, tank:getSquare()))
		end
		ISTimedActionQueue.add(ISBBQInsertPropaneTank:new(playerObj, bbq, tank, 100))
	elseif luautils.walkAdj(playerObj, square) then
		ISWorldObjectContextMenu.transferIfNeeded(playerObj, tank)
		ISTimedActionQueue.add(ISBBQInsertPropaneTank:new(playerObj, bbq, tank, 100))
	end
end

function ISBBQMenu.onRemovePropaneTank(worldobjects, player, bbq, tank)
	local playerObj = getSpecificPlayer(player)
	if luautils.walkAdj(playerObj, bbq:getSquare()) then
		ISTimedActionQueue.add(ISBBQRemovePropaneTank:new(playerObj, bbq, 100))
	end
end

function ISBBQMenu.onToggle(worldobjects, player, bbq, tank)
	local playerObj = getSpecificPlayer(player)
	if luautils.walkAdj(playerObj, bbq:getSquare()) then
		ISTimedActionQueue.add(ISBBQToggle:new(playerObj, bbq, 50))
	end
end

Events.OnFillWorldObjectContextMenu.Add(ISBBQMenu.OnFillWorldObjectContextMenu)

