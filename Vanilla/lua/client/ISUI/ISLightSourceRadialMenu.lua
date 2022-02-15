--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISRadialMenu"

ISLightSourceRadialMenu = ISBaseObject:derive("ISLightSourceRadialMenu")

local function predicateLightSource(item)
	-- getLightStrength() may be > 0 even though the battery is dead.
	return (item:getLightStrength() > 0) or (item:getFullType() == "Base.Candle")
end

local function comparatorDrainableUsesInt(item1, item2)
    return item1:getDrainableUsesInt() - item2:getDrainableUsesInt()
end

function ISLightSourceRadialMenu:center()
	local menu = getPlayerRadialMenu(self.playerNum)

	local x = getPlayerScreenLeft(self.playerNum)
	local y = getPlayerScreenTop(self.playerNum)
	local w = getPlayerScreenWidth(self.playerNum)
	local h = getPlayerScreenHeight(self.playerNum)

	x = x + w / 2
	y = y + h / 2

	menu:setX(x - menu:getWidth() / 2)
	menu:setY(y - menu:getHeight() / 2)
end

function ISLightSourceRadialMenu:getInsertBatteryRecipe(item, containerList)
	if not item then return nil end
	local playerObj = self.character
	local recipes = RecipeManager.getUniqueRecipeItems(item, playerObj, containerList)
	if not recipes then return nil end
	for i=1,recipes:size() do
		local recipe = recipes:get(i-1)
		if recipe:getResult():getFullType() == item:getFullType() then
			local sourceItem = recipe:findSource(item:getFullType())
			local sourceBattery = recipe:findSource("Base.Battery")
			if sourceItem and sourceItem:isDestroy() and sourceBattery and sourceBattery:isDestroy() then
				local numberOfTimes = RecipeManager.getNumberOfTimesRecipeCanBeDone(recipe, playerObj, containerList, item)
				if numberOfTimes ~= 0 then
					return recipe
				end
			end
		end
	end
	return nil
end

function ISLightSourceRadialMenu:getRemoveBatteryRecipe(item, containerList)
	if not item then return nil end
	local playerObj = self.character
	local recipes = RecipeManager.getUniqueRecipeItems(item, playerObj, containerList)
	if not recipes then return nil end
	for i=1,recipes:size() do
		local recipe = recipes:get(i-1)
		if recipe:getSource():size() == 1 and recipe:getResult():getFullType() == "Base.Battery" then
			local sourceItem = recipe:findSource(item:getFullType())
			if sourceItem and sourceItem:isKeep() then
				local numberOfTimes = RecipeManager.getNumberOfTimesRecipeCanBeDone(recipe, playerObj, containerList, item)
				if numberOfTimes ~= 0 then
					return recipe
				end
			end
		end
	end
	return nil
end

local function replaceItemInTable(itemList, replaced, replaceWith)
	for i,item in ipairs(itemList) do
		if item == replaced then
			itemList[i] = replaceWith
			return true
		end
	end
	return false
end

function ISLightSourceRadialMenu:fillMenu()
	local menu = getPlayerRadialMenu(self.playerNum)
	menu:clear()
	local playerObj = self.character
	local playerInv = playerObj:getInventory()
	local items = playerInv:getAllEvalRecurse(predicateLightSource, ArrayList.new())

	local hasType = {}
	local itemList = {}
	for i=1,items:size() do
		local item = items:get(i-1)
		local fullType = item:getFullType()
		local accept = true
		if hasType[fullType] then
			if (fullType == "Base.Candle") or (fullType == "Base.CandleLit") then
				-- Remove duplicate Candle and CandleLit
				accept = false
			else
				-- Prefer non-dead light source over a dead one
				local other = hasType[fullType]
				if item:canEmitLight() and not other:canEmitLight() then
					if replaceItemInTable(itemList, other, item) then
						accept = false
					end
				else
					accept = false
				end
			end
		end
		if accept then
			hasType[fullType] = item
			table.insert(itemList, item)
		end
	end

	for _,item in ipairs(itemList) do
		if playerObj:isPrimaryHandItem(item) or playerObj:isSecondaryHandItem(item) then
			menu:addSlice(item:getDisplayName() .. "\n" .. getText("ContextMenu_Unequip"), getTexture("media/ui/ZoomOut.png"), ISLightSourceRadialMenu.onEquipLight, self, item)
		else
			menu:addSlice(item:getDisplayName() .. "\n" .. getText("ContextMenu_Equip_Secondary"), item:getTex(), ISLightSourceRadialMenu.onEquipLight, self, item)
		end
	end

	local secondary = playerObj:getSecondaryHandItem()
	if not secondary then return end

	local containerList = ISInventoryPaneContextMenu.getContainers(playerObj)

	if secondary:getFullType() == "Base.Candle" then
		local recipe = getScriptManager():getRecipe("Light Candle")
		if not recipe then return end
		local numberOfTimes = RecipeManager.getNumberOfTimesRecipeCanBeDone(recipe, playerObj, containerList, secondary)
		if numberOfTimes == 0 then return end
		menu:addSlice(secondary:getDisplayName() .. "\n" .. recipe:getName(), getTexture("media/ui/vehicles/vehicle_lightsON.png"), ISLightSourceRadialMenu.onLightCandle, self, secondary)
		return
	end

	if secondary:getFullType() == "Base.CandleLit" then
		local recipe = getScriptManager():getRecipe("Extinguish Candle")
		if not recipe then return end
		local numberOfTimes = RecipeManager.getNumberOfTimesRecipeCanBeDone(recipe, playerObj, containerList, secondary)
		if numberOfTimes == 0 then return end
		menu:addSlice(secondary:getDisplayName() .. "\n" .. recipe:getName(), getTexture("media/ui/vehicles/vehicle_lightsOFF.png"), ISLightSourceRadialMenu.onExtinguishCandle, self, secondary)
		return
	end

	local recipe = self:getInsertBatteryRecipe(secondary, containerList)
	if recipe then
		menu:addSlice(secondary:getDisplayName() .. "\n" .. recipe:getName(), getTexture("media/ui/LightSourceRadial_InsertBattery.png"), ISLightSourceRadialMenu.onInsertBattery, self, secondary)
	end

	recipe = self:getRemoveBatteryRecipe(secondary, containerList)
	if recipe then
		menu:addSlice(secondary:getDisplayName() .. "\n" .. recipe:getName(), getTexture("media/ui/LightSourceRadial_RemoveBattery.png"), ISLightSourceRadialMenu.onRemoveBattery, self, secondary)
	end

	if secondary:canBeActivated() and secondary:canEmitLight() then
		if secondary:isActivated() then
			menu:addSlice(secondary:getDisplayName() .. "\n" .. getText("ContextMenu_Turn_Off"), getTexture("media/ui/vehicles/vehicle_lightsOFF.png"), ISLightSourceRadialMenu.onToggle, self, secondary)
		else
			menu:addSlice(secondary:getDisplayName() .. "\n" .. getText("ContextMenu_Turn_On"), getTexture("media/ui/vehicles/vehicle_lightsON.png"), ISLightSourceRadialMenu.onToggle, self, secondary)
		end
	end
end

function ISLightSourceRadialMenu:display()
	local menu = getPlayerRadialMenu(self.playerNum)
	self:center()
	menu:addToUIManager()
	if JoypadState.players[self.playerNum+1] then
		menu:setHideWhenButtonReleased(Joypad.RBumper)
		setJoypadFocus(self.playerNum, menu)
		self.character:setJoypadIgnoreAimUntilCentered(true)
	end
end

function ISLightSourceRadialMenu:onEquipLight(item)
	local playerObj = self.character
	if playerObj:isPrimaryHandItem(item) or playerObj:isSecondaryHandItem(item) then
		ISTimedActionQueue.add(ISUnequipAction:new(playerObj, item, 50));
	else
		ISInventoryPaneContextMenu.transferIfNeeded(playerObj, item)
		ISTimedActionQueue.add(ISEquipWeaponAction:new(playerObj, item, 50, false, false));
	end
end

function ISLightSourceRadialMenu:onInsertBattery(item)
	local playerObj = self.character
	local containerList = ISInventoryPaneContextMenu.getContainers(playerObj)
	local recipe = self:getInsertBatteryRecipe(item, containerList)
	if not recipe then return end
	ISInventoryPaneContextMenu.OnCraft(item, recipe, self.playerNum, false)
end

function ISLightSourceRadialMenu:onRemoveBattery(item)
	local playerObj = self.character
	local containerList = ISInventoryPaneContextMenu.getContainers(playerObj)
	local recipe = self:getRemoveBatteryRecipe(item, containerList)
	if not recipe then return end
	ISInventoryPaneContextMenu.OnCraft(item, recipe, self.playerNum, false)
end

function ISLightSourceRadialMenu:onToggle(item)
	if item:canBeActivated() then
		item:setActivated(not item:isActivated())
	end
end

function ISLightSourceRadialMenu:onLightCandle(item)
	local playerObj = self.character
	local recipe = getScriptManager():getRecipe("Light Candle")
	if not recipe then return end
	ISInventoryPaneContextMenu.OnCraft(item, recipe, self.playerNum, false)
end

function ISLightSourceRadialMenu:onExtinguishCandle(item)
	local playerObj = self.character
	local recipe = getScriptManager():getRecipe("Extinguish Candle")
	if not recipe then return end
	ISInventoryPaneContextMenu.OnCraft(item, recipe, self.playerNum, false)
end

function ISLightSourceRadialMenu:new(character)
	local o = ISBaseObject.new(self)
	o.character = character
	o.playerNum = character:getPlayerNum()
	return o
end

-----

local STATE = {}
STATE[1] = {}
STATE[2] = {}
STATE[3] = {}
STATE[4] = {}

function ISLightSourceRadialMenu.checkKey(key)
	if key ~= getCore():getKey("Equip/Turn On/Off Light Source") then
		return false
	end
	if isGamePaused() then
		return false
	end
	local playerObj = getSpecificPlayer(0)
	if not playerObj or playerObj:isDead() then
		return false
	end
	local queue = ISTimedActionQueue.queues[playerObj]
	if queue and #queue.queue > 0 then
		return false
	end
	if getCell():getDrag(0) then
		return false
	end
	return true
end

function ISLightSourceRadialMenu.onKeyPressed(key)
	if not ISLightSourceRadialMenu.checkKey(key) then
		return
	end
	local radialMenu = getPlayerRadialMenu(0)
	if getCore():getOptionRadialMenuKeyToggle() and radialMenu:isReallyVisible() then
		STATE[1].radialWasVisible = true
		radialMenu:removeFromUIManager()
		return
	end
	STATE[1].keyPressedMS = getTimestampMs()
	STATE[1].radialWasVisible = false
end

function ISLightSourceRadialMenu.onKeyRepeat(key)
	if not ISLightSourceRadialMenu.checkKey(key) then
		return
	end
	if STATE[1].radialWasVisible then
		return
	end
	if not STATE[1].keyPressedMS then
		return
	end
	local radialMenu = getPlayerRadialMenu(0)
	local delay = 500
	if (getTimestampMs() - STATE[1].keyPressedMS >= delay) and not radialMenu:isReallyVisible() then
		local rm = ISLightSourceRadialMenu:new(getSpecificPlayer(0))
		rm:fillMenu()
		rm:display()
	end
end

function ISLightSourceRadialMenu.onKeyReleased(key)
	if not ISLightSourceRadialMenu.checkKey(key) then
		return
	end
	if not STATE[1].keyPressedMS then
		return
	end
	local radialMenu = getPlayerRadialMenu(0)
	if radialMenu:isReallyVisible() or STATE[1].radialWasVisible then
		if not getCore():getOptionRadialMenuKeyToggle() then
			radialMenu:removeFromUIManager()
		end
		return
	end
	if getTimestampMs() - STATE[1].keyPressedMS < 500 then
		ItemBindingHandler.toggleLight(key)
	end
	STATE[1].keyPressedMS = nil
end

local function OnGameStart()
	Events.OnKeyStartPressed.Add(ISLightSourceRadialMenu.onKeyPressed)
	Events.OnKeyKeepPressed.Add(ISLightSourceRadialMenu.onKeyRepeat)
	Events.OnKeyPressed.Add(ISLightSourceRadialMenu.onKeyReleased)
end

Events.OnGameStart.Add(OnGameStart)

