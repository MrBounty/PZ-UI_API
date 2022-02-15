--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISBaseObject"

---@class ISDPadWheels
ISDPadWheels = {}

local function isEquippedOrAttached(playerObj, item)
	return playerObj:isEquipped(item) or playerObj:isAttachedItem(item)
end

function ISDPadWheels.onDisplayLeft(joypadData)
	local isPaused = UIManager.getSpeedControls() and UIManager.getSpeedControls():getCurrentGameSpeed() == 0
	if isPaused then return end

	local playerIndex = joypadData.player
	local playerObj = getSpecificPlayer(playerIndex)

	local menu = getPlayerRadialMenu(playerIndex)
	menu:clear()

	local bestLight,bestLightEquipped = nil
	local inv = playerObj:getInventory():getItems()
	for i=1,inv:size() do
		local item = inv:get(i-1)
		if instanceof(item, "HandWeapon") and item:getCondition() > 0 then
			menu:addSlice(item:getDisplayName(), item:getTex(), ISDPadWheels.onEquipPrimary, playerObj, item)
		elseif item:canEmitLight() then
			if not bestLight then
				bestLight = item
			elseif bestLight:getLightStrength() < item:getLightStrength() then
				bestLight = item
			end
			if isEquippedOrAttached(playerObj, item) then
				if not bestLightEquipped then
					bestLightEquipped = item
				elseif bestLightEquipped:getLightStrength() < item:getLightStrength() then
					bestLightEquipped = item
				end
			end
		end
	end

	bestLight = bestLightEquipped or bestLight
	if bestLight then
		menu:addSlice(bestLight:getDisplayName(), bestLight:getTex(), ISDPadWheels.onToggleLight, playerObj, bestLight)
	end

	menu:setX(getPlayerScreenLeft(playerIndex) + getPlayerScreenWidth(playerIndex) / 2 - menu:getWidth() / 2)
	menu:setY(getPlayerScreenTop(playerIndex) + getPlayerScreenHeight(playerIndex) / 2 - menu:getHeight() / 2)
	menu:addToUIManager()
	menu:setHideWhenButtonReleased(Joypad.DPadLeft)
	setJoypadFocus(playerIndex, menu)
	playerObj:setJoypadIgnoreAimUntilCentered(true)
end

function ISDPadWheels.onDisplayRight(joypadData)
	local isPaused = UIManager.getSpeedControls() and UIManager.getSpeedControls():getCurrentGameSpeed() == 0
	if isPaused then return end

	local playerIndex = joypadData.player
	local playerObj = getSpecificPlayer(playerIndex)

	local menu = getPlayerRadialMenu(playerIndex)
	menu:clear()

	if ISWorldMap.IsAllowed() then
		menu:addSlice(getText("IGUI_WorldMap_Toggle"), getTexture("media/textures/worldMap/Map_On.png"), ISWorldMap.ToggleWorldMap, playerIndex)
	end
	if getPlayerMiniMap(playerIndex) then
		menu:addSlice(getText("IGUI_MiniMap_Toggle"), getTexture("media/textures/worldMap/Map_On.png"), ISMiniMap.ToggleMiniMap, playerIndex)
		menu:addSlice(getText("IGUI_MiniMap_Focus"), getTexture("media/textures/worldMap/Map_On.png"), ISMiniMap.FocusMiniMap, playerIndex)
	end

	menu:setX(getPlayerScreenLeft(playerIndex) + getPlayerScreenWidth(playerIndex) / 2 - menu:getWidth() / 2)
	menu:setY(getPlayerScreenTop(playerIndex) + getPlayerScreenHeight(playerIndex) / 2 - menu:getHeight() / 2)
	menu:addToUIManager()
	menu:setHideWhenButtonReleased(Joypad.DPadRight)
	setJoypadFocus(playerIndex, menu)
	playerObj:setJoypadIgnoreAimUntilCentered(true)
end

function ISDPadWheels.onDisplayUp(joypadData)
	ISVehicleMenu.showRadialMenu(getSpecificPlayer(joypadData.player))
end

function ISDPadWheels.onDisplayDown(joypadData)
	local isPaused = UIManager.getSpeedControls() and UIManager.getSpeedControls():getCurrentGameSpeed() == 0
	if isPaused then return end

	local playerIndex = joypadData.player
	local playerObj = getSpecificPlayer(playerIndex)

	local erm = ISEmoteRadialMenu:new(playerObj)
	erm:fillMenu()
end

function ISDPadWheels.onEquipPrimary(playerObj, item)
	-- FIXME: ISInventoryPaneContextMenu checks for injured hands (code is broken though)
	if item and item == playerObj:getPrimaryHandItem() then
		ISTimedActionQueue.add(ISUnequipAction:new(playerObj, playerObj:getPrimaryHandItem(), 50));
	else
		ISTimedActionQueue.add(ISEquipWeaponAction:new(playerObj, item, 50, true, item:isTwoHandWeapon()));
	end
end

function ISDPadWheels.onToggleLight(playerObj, item)
	if not isEquippedOrAttached(playerObj, item) then
		ISTimedActionQueue.add(ISEquipWeaponAction:new(playerObj, item, 50, false))
	else
		item:setActivated(not item:isActivated())
	end
end

function ISDPadWheels.onShout(playerObj)
	playerObj:Callout()
end

