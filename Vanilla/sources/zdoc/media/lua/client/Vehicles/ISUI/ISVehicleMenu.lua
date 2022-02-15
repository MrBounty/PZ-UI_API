--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

---@class ISVehicleMenu
ISVehicleMenu = {}

function ISVehicleMenu.OnFillWorldObjectContextMenu(player, context, worldobjects, test)
	local playerObj = getSpecificPlayer(player)
	local vehicle = playerObj:getVehicle()
	if not vehicle then
		if JoypadState.players[player+1] then
			local px = playerObj:getX()
			local py = playerObj:getY()
			local pz = playerObj:getZ()
			local sqs = {}
			sqs[1] = getCell():getGridSquare(px, py, pz)
			local dir = playerObj:getDir()
			if (dir == IsoDirections.N) then        sqs[2] = getCell():getGridSquare(px-1, py-1, pz); sqs[3] = getCell():getGridSquare(px, py-1, pz);   sqs[4] = getCell():getGridSquare(px+1, py-1, pz);
			elseif (dir == IsoDirections.NE) then   sqs[2] = getCell():getGridSquare(px, py-1, pz);   sqs[3] = getCell():getGridSquare(px+1, py-1, pz); sqs[4] = getCell():getGridSquare(px+1, py, pz);
			elseif (dir == IsoDirections.E) then    sqs[2] = getCell():getGridSquare(px+1, py-1, pz); sqs[3] = getCell():getGridSquare(px+1, py, pz);   sqs[4] = getCell():getGridSquare(px+1, py+1, pz);
			elseif (dir == IsoDirections.SE) then   sqs[2] = getCell():getGridSquare(px+1, py, pz);   sqs[3] = getCell():getGridSquare(px+1, py+1, pz); sqs[4] = getCell():getGridSquare(px, py+1, pz);
			elseif (dir == IsoDirections.S) then    sqs[2] = getCell():getGridSquare(px+1, py+1, pz); sqs[3] = getCell():getGridSquare(px, py+1, pz);   sqs[4] = getCell():getGridSquare(px-1, py+1, pz);
			elseif (dir == IsoDirections.SW) then   sqs[2] = getCell():getGridSquare(px, py+1, pz);   sqs[3] = getCell():getGridSquare(px-1, py+1, pz); sqs[4] = getCell():getGridSquare(px-1, py, pz);
			elseif (dir == IsoDirections.W) then    sqs[2] = getCell():getGridSquare(px-1, py+1, pz); sqs[3] = getCell():getGridSquare(px-1, py, pz);   sqs[4] = getCell():getGridSquare(px-1, py-1, pz);
			elseif (dir == IsoDirections.NW) then   sqs[2] = getCell():getGridSquare(px-1, py, pz);   sqs[3] = getCell():getGridSquare(px-1, py-1, pz); sqs[4] = getCell():getGridSquare(px, py-1, pz);
			end
			for _,sq in ipairs(sqs) do
				vehicle = sq:getVehicleContainer()
				if vehicle then
					return ISVehicleMenu.FillMenuOutsideVehicle(player, context, vehicle, test)
				end
			end
			return
		end
		vehicle = IsoObjectPicker.Instance:PickVehicle(getMouseXScaled(), getMouseYScaled())
		if vehicle then
			return ISVehicleMenu.FillMenuOutsideVehicle(player, context, vehicle, test)
		end
		return
	end
end

function ISVehicleMenu.showRadialMenu(playerObj)
	local isPaused = UIManager.getSpeedControls() and UIManager.getSpeedControls():getCurrentGameSpeed() == 0
	if isPaused then return end

	local vehicle = playerObj:getVehicle()
	if not vehicle then
		ISVehicleMenu.showRadialMenuOutside(playerObj)
		return
	end

	local menu = getPlayerRadialMenu(playerObj:getPlayerNum())
	menu:clear()

	if menu:isReallyVisible() then
		if menu.joyfocus then
			setJoypadFocus(playerObj:getPlayerNum(), nil)
		end
		menu:undisplay()
		return
	end

	menu:setX(getPlayerScreenLeft(playerObj:getPlayerNum()) + getPlayerScreenWidth(playerObj:getPlayerNum()) / 2 - menu:getWidth() / 2)
	menu:setY(getPlayerScreenTop(playerObj:getPlayerNum()) + getPlayerScreenHeight(playerObj:getPlayerNum()) / 2 - menu:getHeight() / 2)

	local texture = Joypad.Texture.AButton
	
	local seat = vehicle:getSeat(playerObj)

	menu:addSlice(getText("IGUI_SwitchSeat"), getTexture("media/ui/vehicles/vehicle_changeseats.png"), ISVehicleMenu.onShowSeatUI, playerObj, vehicle )

	if vehicle:isDriver(playerObj) and vehicle:isEngineWorking() then
		if vehicle:isEngineRunning() then
			menu:addSlice(getText("ContextMenu_VehicleShutOff"), getTexture("media/ui/vehicles/vehicle_ignitionOFF.png"), ISVehicleMenu.onShutOff, playerObj)
		else
			if vehicle:isEngineStarted() then
--				menu:addSlice("Ignition", getTexture("media/ui/vehicles/vehicle_ignitionON.png"), ISVehicleMenu.onStartEngine, playerObj)
			else
				if (SandboxVars.VehicleEasyUse) then
					menu:addSlice(getText("ContextMenu_VehicleStartEngine"), getTexture("media/ui/vehicles/vehicle_ignitionON.png"), ISVehicleMenu.onStartEngine, playerObj)
				elseif not vehicle:isHotwired() and (playerObj:getInventory():haveThisKeyId(vehicle:getKeyId()) or vehicle:isKeysInIgnition()) then
					menu:addSlice(getText("ContextMenu_VehicleStartEngine"), getTexture("media/ui/vehicles/vehicle_ignitionON.png"), ISVehicleMenu.onStartEngine, playerObj)
				elseif not vehicle:isHotwired() and ((playerObj:getPerkLevel(Perks.Electricity) >= 1 and playerObj:getPerkLevel(Perks.Mechanics) >= 2) or playerObj:HasTrait("Burglar"))then
--					menu:addSlice("Hotwire Vehicle", getTexture("media/ui/vehicles/vehicle_ignitionON.png"), ISVehicleMenu.onHotwire, playerObj)
				elseif vehicle:isHotwired() then
					menu:addSlice(getText("ContextMenu_VehicleStartEngine"), getTexture("media/ui/vehicles/vehicle_ignitionON.png"), ISVehicleMenu.onStartEngine, playerObj)
				else
--					menu:addSlice("You need keys or\nelectricity level 1 and\nmechanic level 2\nto hotwire", getTexture("media/ui/vehicles/vehicle_ignitionOFF.png"), nil, playerObj)
				end
			end
		end
	end

	if vehicle:isDriver(playerObj) and
			not vehicle:isHotwired() and
			not vehicle:isEngineStarted() and
			not vehicle:isEngineRunning() and
			not SandboxVars.VehicleEasyUse and
			not vehicle:isKeysInIgnition() and
			not playerObj:getInventory():haveThisKeyId(vehicle:getKeyId()) then
		if ((playerObj:getPerkLevel(Perks.Electricity) >= 1 and playerObj:getPerkLevel(Perks.Mechanics) >= 2) or playerObj:HasTrait("Burglar")) then
			menu:addSlice(getText("ContextMenu_VehicleHotwire"), getTexture("media/ui/vehicles/vehicle_ignitionON.png"), ISVehicleMenu.onHotwire, playerObj)
		else
			menu:addSlice(getText("ContextMenu_VehicleHotwireSkill"), getTexture("media/ui/vehicles/vehicle_ignitionOFF.png"), nil, playerObj)
		end
	end

	if vehicle:isDriver(playerObj) and vehicle:hasHeadlights() then
		if vehicle:getHeadlightsOn() then
			menu:addSlice(getText("ContextMenu_VehicleHeadlightsOff"), getTexture("media/ui/vehicles/vehicle_lightsOFF.png"), ISVehicleMenu.onToggleHeadlights, playerObj)
		else
			menu:addSlice(getText("ContextMenu_VehicleHeadlightsOn"), getTexture("media/ui/vehicles/vehicle_lightsON.png"), ISVehicleMenu.onToggleHeadlights, playerObj)
		end
	end

	if vehicle:getPartById("Heater") then
		local tex = getTexture("media/ui/vehicles/vehicle_temperatureHOT.png")
		if (vehicle:getPartById("Heater"):getModData().temperature or 0) < 0 then
			tex = getTexture("media/ui/vehicles/vehicle_temperatureCOLD.png")
		end
		if vehicle:getPartById("Heater"):getModData().active then
			menu:addSlice(getText("ContextMenu_VehicleHeaterOff"), tex, ISVehicleMenu.onToggleHeater, playerObj )
		else
			menu:addSlice(getText("ContextMenu_VehicleHeaterOn"), tex, ISVehicleMenu.onToggleHeater, playerObj )
		end
	end
	
	if vehicle:isDriver(playerObj) and vehicle:hasHorn() then
		menu:addSlice(getText("ContextMenu_VehicleHorn"), getTexture("media/ui/vehicles/vehicle_horn.png"), ISVehicleMenu.onHorn, playerObj)
	end
	
	if (vehicle:hasLightbar()) then
		menu:addSlice(getText("ContextMenu_VehicleLightbar"), getTexture("media/ui/vehicles/vehicle_lightbar.png"), ISVehicleMenu.onLightbar, playerObj)
	end

	if seat <= 1 then -- only front seats can access the radio
		for partIndex=1,vehicle:getPartCount() do
			local part = vehicle:getPartByIndex(partIndex-1)
			if part:getDeviceData() and part:getInventoryItem() then
				menu:addSlice(getText("IGUI_DeviceOptions"), getTexture("media/ui/vehicles/vehicle_speakersON.png"), ISVehicleMenu.onSignalDevice, playerObj, part)
			end
		end
	end

	local door = vehicle:getPassengerDoor(seat)
	local windowPart = VehicleUtils.getChildWindow(door)
	if windowPart and (not windowPart:getItemType() or windowPart:getInventoryItem()) then
		local window = windowPart:getWindow()
		if window:isOpenable() and not window:isDestroyed() then
			if window:isOpen() then
				option = menu:addSlice(getText("ContextMenu_Close_window"), getTexture("media/ui/vehicles/vehicle_windowCLOSED.png"), ISVehiclePartMenu.onOpenCloseWindow, playerObj, windowPart, false)
			else
				option = menu:addSlice(getText("ContextMenu_Open_window"), getTexture("media/ui/vehicles/vehicle_windowOPEN.png"), ISVehiclePartMenu.onOpenCloseWindow, playerObj, windowPart, true)
			end
		end
	end

	local locked = vehicle:isAnyDoorLocked()
	if JoypadState.players[playerObj:getPlayerNum()+1] then
		-- Hack: Mouse players click the trunk icon in the dashboard.
		locked = locked or vehicle:isTrunkLocked()
	end
	if locked then
		menu:addSlice(getText("ContextMenu_Unlock_Doors"), getTexture("media/ui/vehicles/vehicle_lockdoors.png"), ISVehiclePartMenu.onLockDoors, playerObj, vehicle, false)
	else
		menu:addSlice(getText("ContextMenu_Lock_Doors"), getTexture("media/ui/vehicles/vehicle_lockdoors.png"), ISVehiclePartMenu.onLockDoors, playerObj, vehicle, true)
	end
	
--	menu:addSlice("Honk", texture, { playerObj, ISVehicleMenu.onHonk })
	if vehicle:getCurrentSpeedKmHour() > 1 then
		menu:addSlice(getText("ContextMenu_VehicleMechanicsStopCar"), getTexture("media/ui/vehicles/vehicle_repair.png"), nil, playerObj, vehicle )
	else
	menu:addSlice(getText("ContextMenu_VehicleMechanics"), getTexture("media/ui/vehicles/vehicle_repair.png"), ISVehicleMenu.onMechanic, playerObj, vehicle )
	end
	if (not isClient() or getServerOptions():getBoolean("SleepAllowed")) then
		local doSleep = true;
		if playerObj:getStats():getFatigue() <= 0.3 then
			menu:addSlice(getText("IGUI_Sleep_NotTiredEnough"), getTexture("media/ui/vehicles/vehicle_sleep.png"), nil, playerObj, vehicle)
			doSleep = false;
		elseif vehicle:getCurrentSpeedKmHour() > 1 or vehicle:getCurrentSpeedKmHour() < -1 then
			menu:addSlice(getText("IGUI_PlayerText_CanNotSleepInMovingCar"), getTexture("media/ui/vehicles/vehicle_sleep.png"), nil, playerObj, vehicle)
			doSleep = false;
		else
			-- Sleeping pills counter those sleeping problems
			if playerObj:getSleepingTabletEffect() < 2000 then
				-- In pain, can still sleep if really tired
				if playerObj:getMoodles():getMoodleLevel(MoodleType.Pain) >= 2 and playerObj:getStats():getFatigue() <= 0.85 then
					menu:addSlice(getText("ContextMenu_PainNoSleep"), getTexture("media/ui/vehicles/vehicle_sleep.png"), nil, playerObj, vehicle)
					doSleep = false;
					-- In panic
				elseif playerObj:getMoodles():getMoodleLevel(MoodleType.Panic) >= 1 then
					menu:addSlice(getText("ContextMenu_PanicNoSleep"), getTexture("media/ui/vehicles/vehicle_sleep.png"), nil, playerObj, vehicle)
					doSleep = false;
					-- tried to sleep not so long ago
				elseif (playerObj:getHoursSurvived() - playerObj:getLastHourSleeped()) <= 1 then
					menu:addSlice(getText("ContextMenu_NoSleepTooEarly"), getTexture("media/ui/vehicles/vehicle_sleep.png"), nil, playerObj, vehicle)
					doSleep = false;
				end
			end
		end
		if doSleep then
			menu:addSlice(getText("ContextMenu_Sleep"), getTexture("media/ui/vehicles/vehicle_sleep.png"), ISVehicleMenu.onSleep, playerObj, vehicle);
		end
	end
	menu:addSlice(getText("IGUI_ExitVehicle"), getTexture("media/ui/vehicles/vehicle_exit.png"), ISVehicleMenu.onExit, playerObj)

	menu:addToUIManager()

	if JoypadState.players[playerObj:getPlayerNum()+1] then
		menu:setHideWhenButtonReleased(Joypad.DPadUp)
		setJoypadFocus(playerObj:getPlayerNum(), menu)
		playerObj:setJoypadIgnoreAimUntilCentered(true)
	end
end

function ISVehicleMenu.getVehicleToInteractWith(playerObj)
	local vehicle = playerObj:getVehicle()
	if not vehicle then
		vehicle = playerObj:getUseableVehicle()
	end
	if not vehicle then
		vehicle = playerObj:getNearVehicle()
--[[
		local px = playerObj:getX()
		local py = playerObj:getY()
		local pz = playerObj:getZ()
		local sqs = {}
		sqs[1] = getCell():getGridSquare(px, py, pz)
		local dir = playerObj:getDir()
		if (dir == IsoDirections.N) then        sqs[2] = getCell():getGridSquare(px-1, py-1, pz); sqs[3] = getCell():getGridSquare(px, py-1, pz);   sqs[4] = getCell():getGridSquare(px+1, py-1, pz);
		elseif (dir == IsoDirections.NE) then   sqs[2] = getCell():getGridSquare(px, py-1, pz);   sqs[3] = getCell():getGridSquare(px+1, py-1, pz); sqs[4] = getCell():getGridSquare(px+1, py, pz);
		elseif (dir == IsoDirections.E) then    sqs[2] = getCell():getGridSquare(px+1, py-1, pz); sqs[3] = getCell():getGridSquare(px+1, py, pz);   sqs[4] = getCell():getGridSquare(px+1, py+1, pz);
		elseif (dir == IsoDirections.SE) then   sqs[2] = getCell():getGridSquare(px+1, py, pz);   sqs[3] = getCell():getGridSquare(px+1, py+1, pz); sqs[4] = getCell():getGridSquare(px, py+1, pz);
		elseif (dir == IsoDirections.S) then    sqs[2] = getCell():getGridSquare(px+1, py+1, pz); sqs[3] = getCell():getGridSquare(px, py+1, pz);   sqs[4] = getCell():getGridSquare(px-1, py+1, pz);
		elseif (dir == IsoDirections.SW) then   sqs[2] = getCell():getGridSquare(px, py+1, pz);   sqs[3] = getCell():getGridSquare(px-1, py+1, pz); sqs[4] = getCell():getGridSquare(px-1, py, pz);
		elseif (dir == IsoDirections.W) then    sqs[2] = getCell():getGridSquare(px-1, py+1, pz); sqs[3] = getCell():getGridSquare(px-1, py, pz);   sqs[4] = getCell():getGridSquare(px-1, py-1, pz);
		elseif (dir == IsoDirections.NW) then   sqs[2] = getCell():getGridSquare(px-1, py, pz);   sqs[3] = getCell():getGridSquare(px-1, py-1, pz); sqs[4] = getCell():getGridSquare(px, py-1, pz);
		end
		for _,sq in ipairs(sqs) do
			local vehicle2 = sq:getVehicleContainer()
			if vehicle2 then
				vehicle = vehicle2
				break
			end
		end
--]]
	end
	return vehicle
end

function ISVehicleMenu.showRadialMenuOutside(playerObj)
	if playerObj:getVehicle() then return end

	local playerIndex = playerObj:getPlayerNum()
	local menu = getPlayerRadialMenu(playerIndex)

	-- For keyboard, toggle visibility
	if menu:isReallyVisible() then
		if menu.joyfocus then
			setJoypadFocus(playerIndex, nil)
		end
		menu:undisplay()
		return
	end

	menu:clear()

	local vehicle = ISVehicleMenu.getVehicleToInteractWith(playerObj)

	if vehicle then
		menu:addSlice(getText("ContextMenu_VehicleMechanics"), getTexture("media/ui/vehicles/vehicle_repair.png"), ISVehicleMenu.onMechanic, playerObj, vehicle)
		
		if vehicle:getScript() and vehicle:getScript():getPassengerCount() > 0 then
			menu:addSlice(getText("IGUI_EnterVehicle"), getTexture("media/ui/vehicles/vehicle_changeseats.png"), ISVehicleMenu.onShowSeatUI, playerObj, vehicle )
		end
		
		ISVehicleMenu.FillPartMenu(playerIndex, nil, menu, vehicle)
	
		local doorPart = vehicle:getUseablePart(playerObj)
		if doorPart and doorPart:getDoor() and doorPart:getInventoryItem() then
			local isHood = doorPart:getId() == "EngineDoor"
			local isTrunk = doorPart:getId() == "TrunkDoor" or doorPart:getId() == "DoorRear"
			if doorPart:getDoor():isOpen() then
				local label = "ContextMenu_Close_door"
				if isHood then label = "IGUI_CloseHood" end
				if isTrunk then label = "IGUI_CloseTrunk" end
				menu:addSlice(getText(label), getTexture("media/ui/vehicles/vehicle_exit.png"), ISVehicleMenu.onCloseDoor, playerObj, doorPart)
			else
				local label = "ContextMenu_Open_door"
				if isHood then label = "IGUI_OpenHood" end
				if isTrunk then label = "IGUI_OpenTrunk" end
				menu:addSlice(getText(label), getTexture("media/ui/vehicles/vehicle_exit.png"), ISVehicleMenu.onOpenDoor, playerObj, doorPart)
				if vehicle:canUnlockDoor(doorPart, playerObj) then
					label = "ContextMenu_UnlockDoor"
					if isHood then label = "IGUI_UnlockHood" end
					if isTrunk then label = "IGUI_UnlockTrunk" end
					menu:addSlice(getText(label), getTexture("media/ui/vehicles/vehicle_lockdoors.png"), ISVehicleMenu.onUnlockDoor, playerObj, doorPart)
				elseif vehicle:canLockDoor(doorPart, playerObj) then
					label = "ContextMenu_LockDoor"
					if isHood then label = "IGUI_LockHood" end
					if isTrunk then label = "IGUI_LockTrunk" end
					menu:addSlice(getText(label), getTexture("media/ui/vehicles/vehicle_lockdoors.png"), ISVehicleMenu.onLockDoor, playerObj, doorPart)
				end
			end
		end

		local part = vehicle:getClosestWindow(playerObj);
		if part then
			local window = part:getWindow()
			if not window:isDestroyed() and not window:isOpen() then
				menu:addSlice(getText("ContextMenu_Vehicle_Smashwindow", getText("IGUI_VehiclePart" .. part:getId())),
					getTexture("media/ui/vehicles/vehicle_smash_window.png"),
					ISVehiclePartMenu.onSmashWindow, playerObj, part)
			end
		end

		ISVehicleMenu.doTowingMenu(playerObj, vehicle, menu)
	end
	
	menu:setX(getPlayerScreenLeft(playerIndex) + getPlayerScreenWidth(playerIndex) / 2 - menu:getWidth() / 2)
	menu:setY(getPlayerScreenTop(playerIndex) + getPlayerScreenHeight(playerIndex) / 2 - menu:getHeight() / 2)
	menu:addToUIManager()
	if JoypadState.players[playerObj:getPlayerNum()+1] then
		menu:setHideWhenButtonReleased(Joypad.DPadUp)
		setJoypadFocus(playerObj:getPlayerNum(), menu)
		playerObj:setJoypadIgnoreAimUntilCentered(true)
	end
end

function ISVehicleMenu.doTowingMenu(playerObj, vehicle, menu)
	if vehicle:getVehicleTowing() then
		menu:addSlice(getText("ContextMenu_Vehicle_DetachTrailer"), getTexture("media/ui/ZoomOut.png"), ISVehicleMenu.onDetachTrailer, playerObj, vehicle, vehicle:getTowAttachmentSelf())
		return
	end
	if vehicle:getVehicleTowedBy() then
		menu:addSlice(getText("ContextMenu_Vehicle_DetachTrailer"), getTexture("media/ui/ZoomOut.png"), ISVehicleMenu.onDetachTrailer, playerObj, vehicle:getVehicleTowedBy(), vehicle:getVehicleTowedBy():getTowAttachmentSelf())
		return
	end

	local attachmentA, attachmentB = "trailer", "trailer"
	local vehicleB = ISVehicleTrailerUtils.getTowableVehicleNear(vehicle:getSquare(), vehicle, attachmentA, attachmentB)
	if vehicleB then
		local aName = ISVehicleMenu.getVehicleDisplayName(vehicle)
		local bName = ISVehicleMenu.getVehicleDisplayName(vehicleB)
		local attachNameA = getText("IGUI_TrailerAttachName_" .. attachmentA)
		local attachNameB = getText("IGUI_TrailerAttachName_" .. attachmentB)
		local burntA = string.contains(vehicle:getScriptName(), "Burnt")
		local trailerA = string.contains(vehicle:getScriptName(), "Trailer")
		local trailerB = string.contains(vehicleB:getScriptName(), "Trailer")
		local vehicleTowing = vehicle
		if burntA or trailerA then
			vehicleTowing = vehicleB
		end
		local text = getText("ContextMenu_Vehicle_AttachVehicle", aName, bName, attachNameA, attachNameB);
		if trailerA or trailerB then
			text = getText("ContextMenu_Vehicle_AttachTrailer");
		end
		menu:addSlice(text, getTexture("media/ui/ZoomIn.png"), ISVehicleMenu.onAttachTrailer, playerObj, vehicleTowing, attachmentA, attachmentB)
		return
	end

	attachmentA, attachmentB = "trailerfront", "trailerfront"
	vehicleB = ISVehicleTrailerUtils.getTowableVehicleNear(vehicle:getSquare(), vehicle, attachmentA, attachmentB)
	if vehicleB then
		local aName = ISVehicleMenu.getVehicleDisplayName(vehicleB)
		local bName = ISVehicleMenu.getVehicleDisplayName(vehicle)
		local attachNameA = getText("IGUI_TrailerAttachName_" .. attachmentA)
		local attachNameB = getText("IGUI_TrailerAttachName_" .. attachmentB)
		local text = getText("ContextMenu_Vehicle_AttachVehicle", aName, bName, attachNameA, attachNameB);
		menu:addSlice(text, getTexture("media/ui/ZoomIn.png"), ISVehicleMenu.onAttachTrailer, playerObj, vehicle, attachmentB, attachmentA)
		return
	end

	attachmentA, attachmentB = "trailer", "trailerfront"
	vehicleB = ISVehicleTrailerUtils.getTowableVehicleNear(vehicle:getSquare(), vehicle, attachmentA, attachmentB)
	if vehicleB then
		local aName = ISVehicleMenu.getVehicleDisplayName(vehicle)
		local bName = ISVehicleMenu.getVehicleDisplayName(vehicleB)
		local attachNameA = getText("IGUI_TrailerAttachName_" .. attachmentA)
		local attachNameB = getText("IGUI_TrailerAttachName_" .. attachmentB)
		local attachName = getText("IGUI_TrailerAttachName_" .. attachmentA)
		local text = getText("ContextMenu_Vehicle_AttachVehicle", aName, bName, attachNameA, attachNameB);
		menu:addSlice(text, getTexture("media/ui/ZoomIn.png"), ISVehicleMenu.onAttachTrailer, playerObj, vehicle, attachmentA, attachmentB)
		return
	end

	attachmentA, attachmentB = "trailerfront", "trailer"
	vehicleB = ISVehicleTrailerUtils.getTowableVehicleNear(vehicle:getSquare(), vehicle, attachmentA, attachmentB)
	if vehicleB then
		local aName = ISVehicleMenu.getVehicleDisplayName(vehicleB)
		local bName = ISVehicleMenu.getVehicleDisplayName(vehicle)
		local attachNameA = getText("IGUI_TrailerAttachName_" .. attachmentA)
		local attachNameB = getText("IGUI_TrailerAttachName_" .. attachmentB)
		local text = getText("ContextMenu_Vehicle_AttachVehicle", aName, bName, attachNameA, attachNameB);
		menu:addSlice(text, getTexture("media/ui/ZoomIn.png"), ISVehicleMenu.onAttachTrailer, playerObj, vehicleB, attachmentB, attachmentA)
		return
	end
end

local TowMenu = {}

function TowMenu.isBurnt(vehicle)
	return string.contains(vehicle:getScriptName(), "Burnt")
end

function TowMenu.isTrailer(vehicle)
	return string.contains(vehicle:getScriptName(), "Trailer")
end

function TowMenu.attachBurntToOther(playerObj, vehicle, menu)
	local attachmentA, attachmentB = "trailer", "trailer"
	local vehicleB = ISVehicleTrailerUtils.getTowableVehicleNear(vehicle:getSquare(), vehicle, attachmentA, attachmentB)

	if not vehicleB then
		attachmentA, attachmentB = "trailer", "trailerfront"
		vehicleB = ISVehicleTrailerUtils.getTowableVehicleNear(vehicle:getSquare(), vehicle, attachmentA, attachmentB)
	end

	if not vehicleB then
		attachmentA, attachmentB = "trailerfront", "trailer"
		vehicleB = ISVehicleTrailerUtils.getTowableVehicleNear(vehicle:getSquare(), vehicle, attachmentA, attachmentB)
	end

	if not vehicleB then
		attachmentA, attachmentB = "trailerfront", "trailerfront"
		vehicleB = ISVehicleTrailerUtils.getTowableVehicleNear(vehicle:getSquare(), vehicle, attachmentA, attachmentB)
	end

	if vehicleB then
		if TowMenu.isBurnt(vehicleB) then
			TowMenu.addOption(playerObj, menu, vehicle, vehicleB, attachmentA, attachmentB)
		elseif TowMenu.isTrailer(vehicleB) then
			TowMenu.addOption(playerObj, menu, vehicle, vehicleB, attachmentA, attachmentB)
		else
			TowMenu.addOption(playerObj, menu, vehicleB, vehicle, attachmentB, attachmentA)
		end
	end
end

function TowMenu.attachTrailerToOther(playerObj, vehicle, menu)
	local attachmentA, attachmentB = "trailer", "trailer"
	local vehicleB = ISVehicleTrailerUtils.getTowableVehicleNear(vehicle:getSquare(), vehicle, attachmentA, attachmentB)

	if not vehicleB then
		attachmentA, attachmentB = "trailer", "trailerfront"
		vehicleB = ISVehicleTrailerUtils.getTowableVehicleNear(vehicle:getSquare(), vehicle, attachmentA, attachmentB)
	end

	if vehicleB then
		if TowMenu.isBurnt(vehicleB) then
			TowMenu.addOption(playerObj, menu, vehicleB, vehicle, attachmentB, attachmentA)
		elseif TowMenu.isTrailer(vehicleB) then
			TowMenu.addOption(playerObj, menu, vehicle, vehicleB, attachmentA, attachmentB)
		else
			TowMenu.addOption(playerObj, menu, vehicleB, vehicle, attachmentB, attachmentA)
		end
	end
end

function TowMenu.attachVehicleToOther(playerObj, vehicle, menu)
	local attachmentA, attachmentB = "trailer", "trailer"
	local vehicleB = ISVehicleTrailerUtils.getTowableVehicleNear(vehicle:getSquare(), vehicle, attachmentA, attachmentB)

	if not vehicleB then
		attachmentA, attachmentB = "trailer", "trailerfront"
		vehicleB = ISVehicleTrailerUtils.getTowableVehicleNear(vehicle:getSquare(), vehicle, attachmentA, attachmentB)
	end

	if not vehicleB then
		attachmentA, attachmentB = "trailerfront", "trailer"
		vehicleB = ISVehicleTrailerUtils.getTowableVehicleNear(vehicle:getSquare(), vehicle, attachmentA, attachmentB)
	end

	if not vehicleB then
		attachmentA, attachmentB = "trailerfront", "trailerfront"
		vehicleB = ISVehicleTrailerUtils.getTowableVehicleNear(vehicle:getSquare(), vehicle, attachmentA, attachmentB)
	end

	if vehicleB then
		if TowMenu.isBurnt(vehicleB) then
			TowMenu.addOption(playerObj, menu, vehicle, vehicleB, attachmentA, attachmentB)
		elseif TowMenu.isTrailer(vehicleB) then
			TowMenu.addOption(playerObj, menu, vehicle, vehicleB, attachmentA, attachmentB)
		else
			TowMenu.addOption(playerObj, menu, vehicle, vehicleB, attachmentA, attachmentB)
		end
	end
end

function TowMenu.addOption(playerObj, menu, vehicleA, vehicleB, attachmentA, attachmentB)
	local aName = ISVehicleMenu.getVehicleDisplayName(vehicleA)
	local bName = ISVehicleMenu.getVehicleDisplayName(vehicleB)
	local text = getText("ContextMenu_Vehicle_AttachTrailer", bName, aName);
	menu:addSlice(text, getTexture("media/ui/ZoomIn.png"), ISVehicleMenu.onAttachTrailer, playerObj, vehicleA, attachmentA, attachmentB)
end

function ISVehicleMenu.doTowingMenu(playerObj, vehicle, menu)
	if vehicle:getVehicleTowing() then
		local bName = ISVehicleMenu.getVehicleDisplayName(vehicle:getVehicleTowing())
		menu:addSlice(getText("ContextMenu_Vehicle_DetachTrailer", bName), getTexture("media/ui/ZoomOut.png"), ISVehicleMenu.onDetachTrailer, playerObj, vehicle, vehicle:getTowAttachmentSelf())
		return
	end

	if vehicle:getVehicleTowedBy() then
		local aName = ISVehicleMenu.getVehicleDisplayName(vehicle)
		menu:addSlice(getText("ContextMenu_Vehicle_DetachTrailer", aName), getTexture("media/ui/ZoomOut.png"), ISVehicleMenu.onDetachTrailer, playerObj, vehicle:getVehicleTowedBy(), vehicle:getVehicleTowedBy():getTowAttachmentSelf())
		return
	end

	if TowMenu.isBurnt(vehicle) then
		TowMenu.attachBurntToOther(playerObj, vehicle, menu)
	elseif TowMenu.isTrailer(vehicle) then
		TowMenu.attachTrailerToOther(playerObj, vehicle, menu)
	else
		TowMenu.attachVehicleToOther(playerObj, vehicle, menu)
	end
end

function ISVehicleMenu.FillMenuOutsideVehicle(player, context, vehicle, test)
	local playerObj = getSpecificPlayer(player)
--[[
	local subOption = context:addOption("Vehicle")
	local vehicleMenu = ISContextMenu:getNew(context)
	context:addSubMenu(subOption, vehicleMenu)
	vehicleMenu:addOption("Info", playerObj, ISVehicleMenu.onInfo, vehicle)
--]]
--	context:addOption("Vehicle Info", playerObj, ISVehicleMenu.onInfo, vehicle)
	ISVehicleMenu.FillPartMenu(player, context, nil, vehicle);
	
	context:addOption(getText("ContextMenu_VehicleMechanics"), playerObj, ISVehicleMenu.onMechanic, vehicle);
	
	local part = vehicle:getClosestWindow(playerObj);
	if part then
		local window = part:getWindow()
		if not window:isDestroyed() and not window:isOpen() then
			context:addOption(getText("ContextMenu_Vehicle_Smashwindow", getText("IGUI_VehiclePart" .. part:getId())), playerObj, ISVehiclePartMenu.onSmashWindow, part)
		end
	end
	
	-- remove burnt vehicles
	if string.match(vehicle:getScript():getName(), "Burnt") or string.match(vehicle:getScript():getName(), "Smashed") then
		local option = context:addOption(getText("ContextMenu_RemoveBurntVehicle"), playerObj, ISVehicleMenu.onRemoveBurntVehicle, vehicle);
		local toolTip = ISToolTip:new();
		toolTip:initialise();
		toolTip:setVisible(false);
		option.toolTip = toolTip;
		toolTip:setName(getText("ContextMenu_RemoveBurntVehicle"));
		toolTip.description = getText("Tooltip_removeBurntVehicle") .. " <LINE> <LINE> ";
		
		if playerObj:getInventory():containsTypeRecurse("WeldingMask") then
			toolTip.description = toolTip.description .. " <LINE> <RGB:1,1,1> " .. getItemNameFromFullType("Base.WeldingMask") .. " 1/1";
		else
			toolTip.description = toolTip.description .. " <LINE> <RGB:1,0,0> " .. getItemNameFromFullType("Base.WeldingMask") .. " 0/1";
			option.notAvailable = true;
		end
		
		local blowTorch = ISBlacksmithMenu.getBlowTorchWithMostUses(playerObj:getInventory());
		if blowTorch then
			local blowTorchUseLeft = blowTorch:getDrainableUsesInt();
			if blowTorchUseLeft >= 20 then
				toolTip.description = toolTip.description .. " <LINE> <RGB:1,1,1> " .. getItemNameFromFullType("Base.BlowTorch") .. getText("ContextMenu_Uses") .. " " .. blowTorchUseLeft .. "/" .. 20;
			else
				toolTip.description = toolTip.description .. " <LINE> <RGB:1,0,0> " .. getItemNameFromFullType("Base.BlowTorch") .. getText("ContextMenu_Uses") .. " " .. blowTorchUseLeft .. "/" .. 20;
				option.notAvailable = true;
			end
		else
			toolTip.description = toolTip.description .. " <LINE> <RGB:1,0,0> " .. getItemNameFromFullType("Base.BlowTorch") .. " 0/5";
			option.notAvailable = true;
		end
	end

	if ISWashVehicle.hasBlood(vehicle) then
		local option = context:addOption(getText("ContextMenu_Vehicle_Wash"), playerObj, ISVehicleMenu.onWash, vehicle);
		local toolTip = ISToolTip:new();
		toolTip:initialise();
		toolTip:setVisible(false);
		toolTip:setName(getText("Tooltip_Vehicle_WashTitle"));
		toolTip.description = getText("Tooltip_Vehicle_WashWaterRequired1", 100 / ISWashVehicle.BLOOD_PER_WATER);
		local waterAvailable = ISWashVehicle.getWaterAmountForPlayer(playerObj);
		option.notAvailable = waterAvailable <= 0
		if waterAvailable == 1 then
			toolTip.description = toolTip.description .. " <BR> " .. getText("Tooltip_Vehicle_WashWaterRequired2");
		else
			toolTip.description = toolTip.description .. " <BR> " .. getText("Tooltip_Vehicle_WashWaterRequired3", waterAvailable);
		end
		option.toolTip = toolTip;
	end

	local vehicleMenu = nil
	if getCore():getDebug() or ISVehicleMechanics.cheat or (isClient() and isAdmin()) then
		local subOption = context:addOption("[DEBUG] Vehicle")
		vehicleMenu = ISContextMenu:getNew(context)
		context:addSubMenu(subOption, vehicleMenu)
	end
	
	if getCore():getDebug() then
		vehicleMenu:addOption("Reload Vehicle Textures", vehicle:getScript():getName(), reloadVehicleTextures)
		if ISVehicleMechanics.cheat then
			vehicleMenu:addOption("ISVehicleMechanics.cheat=false", playerObj, ISVehicleMechanics.onCheatToggle)
		else
			vehicleMenu:addOption("ISVehicleMechanics.cheat=true", playerObj, ISVehicleMechanics.onCheatToggle)
		end
		vehicleMenu:addOption("Roadtrip UI", playerObj, ISVehicleMenu.onRoadtrip);
		vehicleMenu:addOption("Vehicle Angles UI", playerObj, ISVehicleMenu.onDebugAngles, vehicle);
		vehicleMenu:addOption("Vehicle HSV UI", playerObj, ISVehicleMenu.onDebugColor, vehicle);
		vehicleMenu:addOption("Vehicle Blood UI", playerObj, ISVehicleMenu.onDebugBlood, vehicle);
		vehicleMenu:addOption("Vehicle Editor", playerObj, ISVehicleMenu.onDebugEditor, vehicle);
		if not isClient() then
			ISVehicleMenu.addSetScriptMenu(vehicleMenu, playerObj, vehicle)
		end
	end
	
	if getCore():getDebug() or ISVehicleMechanics.cheat or (isClient() and isAdmin()) then
		vehicleMenu:addOption("Remove vehicle", playerObj, ISVehicleMechanics.onCheatRemove, vehicle);
	end
end

function ISVehicleMenu.getVehicleDisplayName(vehicle)
	local name = getText("IGUI_VehicleName" .. vehicle:getScript():getName())
	if string.match(vehicle:getScript():getName(), "Burnt") then
		local unburnt = string.gsub(vehicle:getScript():getName(), "Burnt", "")
		if getTextOrNull("IGUI_VehicleName" .. unburnt) then
			name = getText("IGUI_VehicleName" .. unburnt)
		end
		name = getText("IGUI_VehicleNameBurntCar", name)
	end
	return name
end

local function predicateBlowTorch(item)
	return item:getType() == "BlowTorch" and item:getDrainableUsesInt() >= 20
end

function ISVehicleMenu.onRemoveBurntVehicle(player, vehicle)
	if luautils.walkAdj(player, vehicle:getSquare()) then
		ISWorldObjectContextMenu.equip(player, player:getPrimaryHandItem(), predicateBlowTorch, true);
		local mask = player:getInventory():getFirstTypeRecurse("WeldingMask");
		if mask then
			ISInventoryPaneContextMenu.wearItem(mask, player:getPlayerNum());
		end
		ISTimedActionQueue.add(ISRemoveBurntVehicle:new(player, vehicle));
	end
end

function ISVehicleMenu.onRoadtrip(playerObj)
	local ui = ISVehicleRoadtripDebug:new(0, 0, playerObj);
	ui:initialise();
	ui:addToUIManager();
end

function ISVehicleMenu.onDebugAngles(playerObj, vehicle)
	debugVehicleAngles(vehicle)
end

function ISVehicleMenu.onDebugColor(playerObj, vehicle)
	debugVehicleColor(vehicle)
end

function ISVehicleMenu.onDebugBlood(playerObj, vehicle)
	debugVehicleBloodUI(vehicle)
end

function ISVehicleMenu.onDebugEditor(playerObj, vehicle)
	showVehicleEditor(vehicle:getScript():getFullName())
end

function ISVehicleMenu.addSetScriptMenu(context, playerObj, vehicle)
	local option = context:addOption("Set Script", nil, nil)
	local subMenu = ISContextMenu:getNew(context)
	context:addSubMenu(option, subMenu)

	local optionBurnt = context:addOption("Set Script (Burnt)", nil, nil)
	local subMenuBurnt = ISContextMenu:getNew(context)
	context:addSubMenu(optionBurnt, subMenuBurnt)

	local optionSmashed = context:addOption("Set Script (Smashed)", nil, nil)
	local subMenuSmashed = ISContextMenu:getNew(context)
	context:addSubMenu(optionSmashed, subMenuSmashed)

	local scripts = getScriptManager():getAllVehicleScripts()
	local sorted = {}
	for i=1,scripts:size() do
		local script = scripts:get(i-1)
		table.insert(sorted, script)
	end
	table.sort(sorted, function(a,b) return not string.sort(a:getName(), b:getName()) end)
	for _,script in ipairs(sorted) do
		if script:getPartCount() == 0 then
			subMenuBurnt:addOption(script:getName(), playerObj, ISVehicleMenu.onDebugSetScript, vehicle, script:getFullName())
		elseif string.match(script:getName(), "Smashed") then
			subMenuSmashed:addOption(script:getName(), playerObj, ISVehicleMenu.onDebugSetScript, vehicle, script:getFullName())
		else
			subMenu:addOption(script:getName(), playerObj, ISVehicleMenu.onDebugSetScript, vehicle, script:getFullName())
		end
	end
end

function ISVehicleMenu.onDebugSetScript(playerObj, vehicle, scriptName)
	vehicle:setScriptName(scriptName)
	vehicle:scriptReloaded()
	vehicle:setSkinIndex(ZombRand(vehicle:getSkinCount()))
	vehicle:repair() -- so engine loudness/power/quality are recalculated
end

function ISVehicleMenu.onMechanic(playerObj, vehicle)
	local ui = getPlayerMechanicsUI(playerObj:getPlayerNum())
	if ui:isReallyVisible() then
		ui:close()
		return
	end

	local engineHood = nil;
	local cheat = getCore():getDebug() and getDebugOptions():getBoolean("Cheat.Vehicle.MechanicsAnywhere")
	if ISVehicleMechanics.cheat or (isClient() and isAdmin()) or cheat then
		ISTimedActionQueue.add(ISOpenMechanicsUIAction:new(playerObj, vehicle))
		return;
	end
	engineHood = vehicle:getPartById("EngineDoor");
	if playerObj:getVehicle() then
		ISVehicleMenu.onExit(playerObj)
	end
--		local closestDist;
--		local closestPart;
--		for i=0,vehicle:getPartCount()-1 do
--			local part = vehicle:getPartByIndex(i);
--			if (part:getCategory() == "tire" or part:getCategory() == "bodywork") and (not closestDist or closestDist > vehicle:getAreaDist(part:getArea(), playerObj))then
----				print("TIRE: ", part:getId(), " CLOSER");
--				closestDist = vehicle:getAreaDist(part:getArea(), playerObj);
--				closestPart = part;
--			end
--		end
	if engineHood then
		ISTimedActionQueue.add(ISPathFindAction:pathToVehicleArea(playerObj, vehicle, engineHood:getArea()))
		if not engineHood:getDoor() or not engineHood:getInventoryItem() then
			engineHood = nil
		end
		if engineHood and not engineHood:getDoor():isOpen() then
			-- The hood is magically unlocked if any door/window is broken/open/uninstalled.
			-- If the player can get in the vehicle, they can pop the hood, no key required.
			if engineHood:getDoor():isLocked() and VehicleUtils.RequiredKeyNotFound(vehicle:getPartById("Engine"), playerObj) then
				ISTimedActionQueue.add(ISUnlockVehicleDoor:new(playerObj, engineHood))
			end
			ISTimedActionQueue.add(ISOpenVehicleDoor:new(playerObj, vehicle, engineHood))
		end
	else
		-- Burned vehicles and trailers don't have a hood
		ISTimedActionQueue.add(ISPathFindAction:pathToVehicleAdjacent(playerObj, vehicle))
	end
	ISTimedActionQueue.add(ISOpenMechanicsUIAction:new(playerObj, vehicle, engineHood))
--	local ui = ISVehicleMechanics:new(0,0,playerObj,vehicle);
--	ui:initialise();
--	ui:addToUIManager();
--	local ui = getPlayerMechanicsUI(playerObj:getPlayerNum());
--	if ui:isReallyVisible() then
--		ui:close()
--		return
--	end
--	ui.vehicle = vehicle;
--	ui.usedHood = usedHood
--	ui:initParts();
--	ui:setVisible(true, JoypadState.players[playerObj:getPlayerNum()+1])
--	ui:addToUIManager()
	
--print("ONMECHANIC")
	-- get the closest tire to the player
--	local closestDist;
--	local closestPart;
--	for i=0,vehicle:getPartCount()-1 do
--		local part = vehicle:getPartByIndex(i);
--		if part:getCategory() == "tire" and (not closestDist or closestDist < vehicle:getAreaDist(part:getArea(), playerObj))then
--			print("TIRE: ", part:getId(), " CLOSER");
--			closestDist = vehicle:getAreaDist(part:getArea(), playerObj);
--			closestPart = part:getId();
--		end
--	end
--	local tire = vehicle:getPartById(closestPart);
--	ISTimedActionQueue.add(ISPathFindAction:pathToVehicleArea(playerObj, vehicle, tire:getArea()))
--	closestPart = ISVehicleMenu.getNextTire(closestPart);
--	local tire = vehicle:getPartById(closestPart);
--	ISTimedActionQueue.add(ISPathFindAction:pathToVehicleArea(playerObj, vehicle, tire:getArea()))
--	closestPart = ISVehicleMenu.getNextTire(closestPart);
--	local tire = vehicle:getPartById(closestPart);
--	ISTimedActionQueue.add(ISPathFindAction:pathToVehicleArea(playerObj, vehicle, tire:getArea()))
--	closestPart = ISVehicleMenu.getNextTire(closestPart);
--	local tire = vehicle:getPartById(closestPart);
--	ISTimedActionQueue.add(ISPathFindAction:pathToVehicleArea(playerObj, vehicle, tire:getArea()))
--	local tire = vehicle:getPartById("TireFrontRight");
--	ISTimedActionQueue.add(ISPathFindAction:pathToVehicleArea(playerObj, vehicle, tire:getArea()))
end

-- cicle thru each tires clockwise
function ISVehicleMenu.getNextTire(currentTire)
	if currentTire == "TireFrontLeft" then return "TireFrontRight"; end
	if currentTire == "TireFrontRight" then return "TireRearRight"; end
	if currentTire == "TireRearRight" then return "TireRearLeft"; end
	if currentTire == "TireRearLeft" then return "TireFrontLeft"; end
end

function ISVehicleMenu.FillPartMenu(playerIndex, context, slice, vehicle)
	local playerObj = getSpecificPlayer(playerIndex);
	if playerObj:DistToProper(vehicle) >= 4 then
		return
	end
	local typeToItem = VehicleUtils.getItems(playerIndex)
	for i=1,vehicle:getPartCount() do
		local part = vehicle:getPartByIndex(i-1)
		if not vehicle:isEngineStarted() and part:isContainer() and part:getContainerContentType() == "Gasoline" then
			if typeToItem["Base.PetrolCan"] and part:getContainerContentAmount() < part:getContainerCapacity() then
				if slice then
					slice:addSlice(getText("ContextMenu_VehicleAddGas"), getTexture("media/ui/vehicles/vehicle_add_gas.png"), ISVehiclePartMenu.onAddGasoline, playerObj, part)
				else
					context:addOption(getText("ContextMenu_VehicleAddGas"), playerObj,ISVehiclePartMenu.onAddGasoline, part)
				end
			end
			if ISVehiclePartMenu.getGasCanNotFull(playerObj, typeToItem) and part:getContainerContentAmount() > 0 then
				if slice then
					slice:addSlice(getText("ContextMenu_VehicleSiphonGas"), getTexture("media/ui/vehicles/vehicle_siphon_gas.png"), ISVehiclePartMenu.onTakeGasoline, playerObj, part)
				else
					context:addOption(getText("ContextMenu_VehicleSiphonGas"), playerObj, ISVehiclePartMenu.onTakeGasoline, part)
				end
			end
			local fuelStation = ISVehiclePartMenu.getNearbyFuelPump(vehicle)
			if fuelStation then
				local square = fuelStation:getSquare();
				if square and ((SandboxVars.AllowExteriorGenerator and square:haveElectricity()) or (SandboxVars.ElecShutModifier > -1 and GameTime:getInstance():getNightsSurvived() < SandboxVars.ElecShutModifier)) then
					if square and part:getContainerContentAmount() < part:getContainerCapacity() then
						if slice then
							slice:addSlice(getText("ContextMenu_VehicleRefuelFromPump"), getTexture("media/ui/vehicles/vehicle_refuel_from_pump.png"), ISVehiclePartMenu.onPumpGasoline, playerObj, part)
						else
							context:addOption(getText("ContextMenu_VehicleRefuelFromPump"), playerObj, ISVehiclePartMenu.onPumpGasoline, part)
						end
					end
				end
			end
		end
	end
end

function ISVehicleMenu.onSwitchSeat(playerObj, seatTo)
	local vehicle = playerObj:getVehicle()
	if not vehicle then return end
	ISTimedActionQueue.add(ISSwitchVehicleSeat:new(playerObj, seatTo))
end

function ISVehicleMenu.onToggleHeadlights(playerObj)
	local vehicle = playerObj:getVehicle()
	if not vehicle then return end
	sendClientCommand(playerObj, 'vehicle', 'setHeadlightsOn', { on = not vehicle:getHeadlightsOn() })
end

function ISVehicleMenu.onToggleTrunkLocked(playerObj)
	local vehicle = playerObj:getVehicle();
	if not vehicle then return end
	sendClientCommand(playerObj, 'vehicle', 'setTrunkLocked', { locked = not vehicle:isTrunkLocked() });
end

function ISVehicleMenu.onToggleHeater(playerObj)
	local playerNum = playerObj:getPlayerNum()
	if not ISVehicleMenu.acui then
		ISVehicleMenu.acui = {}
	end
	local ui = ISVehicleMenu.acui[playerNum]
	if not ui or ui.character ~= playerObj then
		ui = ISVehicleACUI:new(0,0,playerObj)
		ui:initialise()
		ui:instantiate()
		ISVehicleMenu.acui[playerNum] = ui
	end
	if ui:isReallyVisible() then
		ui:removeFromUIManager()
		if JoypadState.players[playerNum+1] then
			setJoypadFocus(playerNum, nil)
		end
	else
		ui:setVehicle(playerObj:getVehicle())
		ui:addToUIManager()
		if JoypadState.players[playerNum+1] then
			JoypadState.players[playerNum+1].focus = ui
		end
	end
end

function ISVehicleMenu.onSignalDevice(playerObj, part)
	ISRadioWindow.activate(playerObj, part)
end

function ISVehicleMenu.onStartEngine(playerObj)
--	local vehicle = playerObj:getVehicle()
--	if not vehicle then return end
--	if not vehicle:isEngineWorking() then return end
--	if not vehicle:isDriver(playerObj) then return end
	ISTimedActionQueue.add(ISStartVehicleEngine:new(playerObj))
end

function ISVehicleMenu.onHotwire(playerObj)
	ISTimedActionQueue.add(ISHotwireVehicle:new(playerObj))
end

function ISVehicleMenu.onShutOff(playerObj)
--	local vehicle = playerObj:getVehicle()
--	if not vehicle then return end
--	if not vehicle:isEngineStarted() then return end
--	if not vehicle:isDriver(playerObj) then return end
	ISTimedActionQueue.add(ISShutOffVehicleEngine:new(playerObj))
end

function ISVehicleMenu.onInfo(playerObj, vehicle)
	local ui = getPlayerVehicleUI(playerObj:getPlayerNum())
	ui:setVehicle(vehicle)
	ui:setVisible(true)
	ui:bringToTop()
	if JoypadState.players[playerObj:getPlayerNum()+1] then
		JoypadState.players[playerObj:getPlayerNum()+1].focus = ui
	end
end

function ISVehicleMenu.onSleep(playerObj, vehicle)
	if vehicle:getCurrentSpeedKmHour() > 1 or vehicle:getCurrentSpeedKmHour() < -1 then
		playerObj:Say(getText("IGUI_PlayerText_CanNotSleepInMovingCar"))
		return;
	end
	local playerNum = playerObj:getPlayerNum()
	local modal = ISModalDialog:new(0,0, 250, 150, getText("IGUI_ConfirmSleep"), true, nil, ISVehicleMenu.onConfirmSleep, playerNum, playerNum, nil);
	modal:initialise()
	modal:addToUIManager()
	if JoypadState.players[playerNum+1] then
		setJoypadFocus(playerNum, modal)
	end
end

function ISVehicleMenu.onConfirmSleep(this, button, player, bed)
	if button.internal == "YES" then
		ISWorldObjectContextMenu.onSleepWalkToComplete(player, nil)
	end
end

function ISVehicleMenu.onOpenDoor(playerObj, part)
	local vehicle = part:getVehicle()
	if part:getDoor():isLocked() then
		-- The hood is magically unlocked if any door/window is broken/open/uninstalled.
		-- If the player can get in the vehicle, they can pop the hood, no key required.
		if not (part:getId() == "EngineDoor" and VehicleUtils.RequiredKeyNotFound(part, playerObj)) then
			ISTimedActionQueue.add(ISUnlockVehicleDoor:new(playerObj, part))
		end
	end
	ISTimedActionQueue.add(ISOpenVehicleDoor:new(playerObj, part:getVehicle(), part))
	if part:getId() == "EngineDoor" then
		ISTimedActionQueue.add(ISOpenMechanicsUIAction:new(playerObj, vehicle, part))
	end
end

function ISVehicleMenu.onCloseDoor(playerObj, part)
	ISTimedActionQueue.add(ISCloseVehicleDoor:new(playerObj, part:getVehicle(), part))
end

function ISVehicleMenu.onLockDoor(playerObj, part)
	ISTimedActionQueue.add(ISLockVehicleDoor:new(playerObj, part))
end

function ISVehicleMenu.onUnlockDoor(playerObj, part)
	ISTimedActionQueue.add(ISUnlockVehicleDoor:new(playerObj, part))
end

function ISVehicleMenu.onWash(playerObj, vehicle)
	local area = ISWashVehicle.chooseArea(playerObj, vehicle)
	if not area then return end
	ISTimedActionQueue.add(ISPathFindAction:pathToVehicleArea(playerObj, vehicle, area.area))
	ISTimedActionQueue.add(ISWashVehicle:new(playerObj, vehicle, area.id, area.area))
end

local SORTVARS = {
	pos = Vector3f.new()
}
local function distanceToPassengerPosition(seat)
	local script = SORTVARS.vehicle:getScript()
	local outside = SORTVARS.vehicle:getPassengerPosition(seat, "outside")
	local worldPos = SORTVARS.vehicle:getWorldPos(outside:getOffset(), SORTVARS.pos)
	return SORTVARS.playerObj:DistTo(worldPos:x(), worldPos:y())
end
local function getClosestSeat(playerObj, vehicle, seats)
	if #seats == 0 then
		return nil
	end
	-- Sort by distance from the player to the 'outside' position.
	SORTVARS.playerObj = playerObj
	SORTVARS.vehicle = vehicle
	table.sort(seats, function(a,b)
		local distA = distanceToPassengerPosition(a)
		local distB = distanceToPassengerPosition(b)
		return distA < distB
	end)
	return seats[1]
end

-- BaseVehicle.isEnterBlocked() returns true for passengers with no "outside"
-- position, which is the case for VanSeats' rear seats that are not accessible
-- by any door.  The player must enter through a front or middle door then
-- switch to the rear seat.
function ISVehicleMenu.getBestSwitchSeatEnter(playerObj, vehicle, seat)
	local seats = {}
	for seat2=0,vehicle:getMaxPassengers()-1 do
		if seat ~= seat2 and
				vehicle:canSwitchSeat(seat2, seat) and
				not vehicle:isSeatOccupied(seat2) and
				not vehicle:isEnterBlocked(playerObj, seat2) then
			table.insert(seats, seat2)
		end
	end
	return getClosestSeat(playerObj, vehicle, seats)
end

function ISVehicleMenu.getBestSwitchSeatExit(playerObj, vehicle, seat)
	local seats = {}
	for seat2=0,vehicle:getMaxPassengers()-1 do
		if seat ~= seat2 and
				vehicle:canSwitchSeat(seat, seat2) and
				not vehicle:isSeatOccupied(seat2) and
				not vehicle:isExitBlocked(seat2) then
			table.insert(seats, seat2)
		end
	end
	return getClosestSeat(playerObj, vehicle, seats)
end

function ISVehicleMenu.moveItemsOnSeat(seat, newSeat, playerObj, moveThem, itemListIndex)
--	if moveThem then print("moving item on seat from", seat:getId(), "to", newSeat:getId()) end
	local itemList = {};
	local actualWeight = newSeat:getItemContainer():getCapacityWeight();
	for i=itemListIndex,seat:getItemContainer():getItems():size() -1 do
		local item = seat:getItemContainer():getItems():get(i);
		actualWeight = actualWeight + item:getUnequippedWeight();
		if newSeat:getItemContainer():hasRoomFor(playerObj, actualWeight) then
			table.insert(itemList, item);
		else
			break;
		end
	end
	if moveThem then
		for i,v in ipairs(itemList) do
			ISTimedActionQueue.add(ISInventoryTransferAction:new (playerObj, v, seat:getItemContainer(), newSeat:getItemContainer(), 10));
--			seat:getItemContainer():Remove(v);
--			newSeat:getItemContainer():AddItem(v);
		end
	end
	return #itemList + itemListIndex;
end

function ISVehicleMenu.tryMoveItemsFromSeat(playerObj, vehicle, seat, moveThem, doEnter, seatTo, itemListIndex)
	local currentSeat = vehicle:getPartForSeatContainer(seat);
	if currentSeat:getItemContainer():getItems():isEmpty() then return 0; end
	local newSeat = vehicle:getPartById(seatTo);
	if not newSeat then return 0; end
	if newSeat == currentSeat or (vehicle:getCharacter(newSeat:getContainerSeatNumber()) and playerObj ~= vehicle:getCharacter(newSeat:getContainerSeatNumber())) then return 0; end
	if newSeat then
		local movedItems = ISVehicleMenu.moveItemsOnSeat(currentSeat, newSeat, playerObj, moveThem, itemListIndex);
		if doEnter and (movedItems == currentSeat:getItemContainer():getItems():size() or movedItems == currentSeat:getItemContainer():getItems():isEmpty()) then
			ISVehicleMenu.processEnter(playerObj, vehicle, seat);
			return movedItems;
		end
		return movedItems;
	end
	return 0;
end

function ISVehicleMenu.moveItemsFromSeat(playerObj, vehicle, seat, moveThem, doEnter)
	-- if items are on the seats we'll try to move them to another empty seat, first rear seat then middle, then front left seats, never on driver's seat
	-- first rear seats
	local currentSeat = vehicle:getPartForSeatContainer(seat);
	local movedItems = ISVehicleMenu.tryMoveItemsFromSeat(playerObj, vehicle, seat, moveThem, doEnter, "SeatRearLeft", 0);
	if movedItems == currentSeat:getItemContainer():getItems():size() then return true; end
	movedItems = ISVehicleMenu.tryMoveItemsFromSeat(playerObj, vehicle, seat, moveThem, doEnter, "SeatRearRight", movedItems);
	if movedItems == currentSeat:getItemContainer():getItems():size() then return true; end
	movedItems = ISVehicleMenu.tryMoveItemsFromSeat(playerObj, vehicle, seat, moveThem, doEnter, "SeatFrontRight", movedItems);
	if movedItems == currentSeat:getItemContainer():getItems():size() then return true; end
	movedItems = ISVehicleMenu.tryMoveItemsFromSeat(playerObj, vehicle, seat, moveThem, doEnter, "SeatMiddleLeft", movedItems);
	if movedItems == currentSeat:getItemContainer():getItems():size() then return true; end
	movedItems = ISVehicleMenu.tryMoveItemsFromSeat(playerObj, vehicle, seat, moveThem, doEnter, "SeatMiddleRight", movedItems);
	if movedItems == currentSeat:getItemContainer():getItems():size() then return true; end
	return false;
end

function ISVehicleMenu.onEnter(playerObj, vehicle, seat)
	if vehicle:isSeatOccupied(seat) then
		if vehicle:getCharacter(seat) then
			playerObj:Say(getText("IGUI_PlayerText_VehicleSomeoneInSeat"))
		else
			if not ISVehicleMenu.moveItemsFromSeat(playerObj, vehicle, seat, true, true) then
				playerObj:Say(getText("IGUI_PlayerText_VehicleItemInSeat"))
			end
		end
	else
		if isShiftKeyDown() then
			ISVehicleMenu.processShiftEnter(playerObj, vehicle, seat)
		elseif vehicle:isPassengerUseDoor2(playerObj, seat) then
			ISVehicleMenu.processEnter2(playerObj, vehicle, seat);
		else 
			ISVehicleMenu.processEnter(playerObj, vehicle, seat);
		end
	end
end

function ISVehicleMenu.processShiftEnter(playerObj, vehicle, seat)
	if seat == 0 then
		ISVehicleMenu.processEnter(playerObj, vehicle, seat);
	else
		if not vehicle:isSeatInstalled(0) or not vehicle:isSeatInstalled(seat) then
			playerObj:Say(getText("IGUI_PlayerText_VehicleSeatRemoved"))
		elseif not vehicle:canSwitchSeat(seat, 0) then
			ISVehicleMenu.processEnter(playerObj, vehicle, 0)
		elseif vehicle:isEnterBlocked(playerObj, seat) then
			local seat2 = ISVehicleMenu.getBestSwitchSeatEnter(playerObj, vehicle, seat)
			if seat2 then
				ISVehicleMenu.onEnterAux(playerObj, vehicle, seat2)
				ISTimedActionQueue.add(ISSwitchVehicleSeat:new(playerObj, 0))
			end
		else
			ISVehicleMenu.onEnterAux(playerObj, vehicle, seat)
			ISTimedActionQueue.add(ISSwitchVehicleSeat:new(playerObj, 0))
		end
	end
end

function ISVehicleMenu.processEnter(playerObj, vehicle, seat)
	if not vehicle:isSeatInstalled(seat) then
		playerObj:Say(getText("IGUI_PlayerText_VehicleSeatRemoved"))
	elseif not playerObj:isBlockMovement() then
		if vehicle:isEnterBlocked(playerObj, seat) then
			local seat2 = ISVehicleMenu.getBestSwitchSeatEnter(playerObj, vehicle, seat)
			if seat2 then
				ISVehicleMenu.onEnterAux(playerObj, vehicle, seat2)
				ISTimedActionQueue.add(ISSwitchVehicleSeat:new(playerObj, seat))
			end
		else
			ISVehicleMenu.onEnterAux(playerObj, vehicle, seat)
		end
	end
end

function ISVehicleMenu.onEnterAux(playerObj, vehicle, seat)
		ISTimedActionQueue.add(ISPathFindAction:pathToVehicleSeat(playerObj, vehicle, seat))
		local doorPart = vehicle:getPassengerDoor(seat)
		if doorPart and doorPart:getDoor() and doorPart:getInventoryItem() then
			local door = doorPart:getDoor()
			if door:isLocked() then
				-- if the keys on on the car, we take them and open
				if vehicle:isKeyIsOnDoor() then
					local key = vehicle:getCurrentKey()
					vehicle:setKeyIsOnDoor(false);
					vehicle:setCurrentKey(nil)
					playerObj:getInventory():AddItem(key)
					if isClient() then
						sendClientCommand(playerObj, 'vehicle', 'removeKeyFromDoor', { vehicle = vehicle:getId() })
					end
				else
					ISTimedActionQueue.add(ISUnlockVehicleDoor:new(playerObj, doorPart, seat))
				end
			end
			if not door:isOpen() then
				ISTimedActionQueue.add(ISOpenVehicleDoor:new(playerObj, vehicle, doorPart))
			end
			ISTimedActionQueue.add(ISEnterVehicle:new(playerObj, vehicle, seat))
			ISTimedActionQueue.add(ISCloseVehicleDoor:new(playerObj, vehicle, seat))
		else
			ISTimedActionQueue.add(ISEnterVehicle:new(playerObj, vehicle, seat))
		end
end

function ISVehicleMenu.onEnter2(playerObj, vehicle, seat)
	if vehicle:isSeatOccupied(seat) then
		if vehicle:getCharacter(seat) then
			playerObj:Say(getText("IGUI_PlayerText_VehicleSomeoneInSeat"))
		else
			if not ISVehicleMenu.moveItemsFromSeat(playerObj, vehicle, seat, true, true) then
				playerObj:Say(getText("IGUI_PlayerText_VehicleItemInSeat"))
			end
		end
	else
		ISVehicleMenu.processEnter2(playerObj, vehicle, seat);
	end
end

function ISVehicleMenu.processEnter2(playerObj, vehicle, seat)
	if not vehicle:isSeatInstalled(seat) then
		playerObj:Say(getText("IGUI_PlayerText_VehicleSeatRemoved"))
	elseif not playerObj:isBlockMovement() then
		if vehicle:isEnterBlocked2(playerObj, seat) then
			local seat2 = ISVehicleMenu.getBestSwitchSeatEnter(playerObj, vehicle, seat)
			if seat2 then
				ISVehicleMenu.onEnterAux(playerObj, vehicle, seat2)
				ISTimedActionQueue.add(ISSwitchVehicleSeat:new(playerObj, seat))
			end
		else
			ISVehicleMenu.onEnterAux2(playerObj, vehicle, seat)
		end
	end
end

function ISVehicleMenu.onEnterAux2(playerObj, vehicle, seat)
		ISTimedActionQueue.add(ISPathFindAction:pathToVehicleSeat(playerObj, vehicle, seat))
		local doorPart = vehicle:getPassengerDoor2(seat)
		if doorPart and doorPart:getDoor() and doorPart:getInventoryItem() then
			local door = doorPart:getDoor()
			if door:isLocked() then
				-- if the keys on on the car, we take them and open
				if vehicle:isKeyIsOnDoor() then
					local key = vehicle:getCurrentKey()
					vehicle:setKeyIsOnDoor(false);
					vehicle:setCurrentKey(nil)
					playerObj:getInventory():AddItem(key)
					if isClient() then
						sendClientCommand(playerObj, 'vehicle', 'removeKeyFromDoor', { vehicle = vehicle:getId() })
					end
				else
					ISTimedActionQueue.add(ISUnlockVehicleDoor:new(playerObj, doorPart, seat))
				end
			end
			if not door:isOpen() then
				ISTimedActionQueue.add(ISOpenVehicleDoor:new(playerObj, vehicle, doorPart))
			end
			ISTimedActionQueue.add(ISEnterVehicle:new(playerObj, vehicle, seat))
			ISTimedActionQueue.add(ISCloseVehicleDoor:new(playerObj, vehicle, doorPart))
		else
			ISTimedActionQueue.add(ISEnterVehicle:new(playerObj, vehicle, seat))
		end
end

function ISVehicleMenu.onExit(playerObj, seatFrom)
    local vehicle = playerObj:getVehicle()
    vehicle:updateHasExtendOffsetForExit(playerObj)
	if (not playerObj:isBlockMovement()) then
		
		if not vehicle then return end
		if vehicle:getCurrentSpeedKmHour() > 1 or vehicle:getCurrentSpeedKmHour() < -1 then 
			playerObj:Say(getText("IGUI_PlayerText_CanNotExitFromMovingCar"))
            vehicle:updateHasExtendOffsetForExitEnd(playerObj)
			return 
		end
		seatFrom = seatFrom or vehicle:getSeat(playerObj)
		if vehicle:isExitBlocked(seatFrom) then
			local seatTo = ISVehicleMenu.getBestSwitchSeatExit(playerObj, vehicle, seatFrom)
			if seatTo then
				ISTimedActionQueue.add(ISSwitchVehicleSeat:new(playerObj, seatTo))
				ISVehicleMenu.onExitAux(playerObj, seatTo)
				return
			end
		else
			ISVehicleMenu.onExitAux(playerObj, seatFrom)
		end
	end
end

function ISVehicleMenu.onExitAux(playerObj, seat)
	local vehicle = playerObj:getVehicle()
	local doorPart = vehicle:getPassengerDoor(seat)
	if doorPart and doorPart:getDoor() and doorPart:getInventoryItem() then
		local door = doorPart:getDoor()
		if door:isLocked() then
			ISTimedActionQueue.add(ISUnlockVehicleDoor:new(playerObj, doorPart))
		end
		if not door:isOpen() then
			ISTimedActionQueue.add(ISOpenVehicleDoor:new(playerObj, vehicle, seat))
		end
		ISTimedActionQueue.add(ISExitVehicle:new(playerObj))
		ISTimedActionQueue.add(ISCloseVehicleDoor:new(playerObj, vehicle, doorPart))
	else
		ISTimedActionQueue.add(ISExitVehicle:new(playerObj))
	end
end

function ISVehicleMenu.onShowSeatUI(playerObj, vehicle)
	local isPaused = UIManager.getSpeedControls() and UIManager.getSpeedControls():getCurrentGameSpeed() == 0
	if isPaused then return end

	local playerNum = playerObj:getPlayerNum()
	if not ISVehicleMenu.seatUI then
		ISVehicleMenu.seatUI = {}
	end
	local ui = ISVehicleMenu.seatUI[playerNum]
	if not ui or ui.character ~= playerObj then
		ui = ISVehicleSeatUI:new(0, 0, playerObj)
		ui:initialise()
		ui:instantiate()
		ISVehicleMenu.seatUI[playerNum] = ui
	end
	if ui:isReallyVisible() then
		ui:removeFromUIManager()
		if JoypadState.players[playerNum+1] then
			setJoypadFocus(playerNum, nil)
		end
	else
		ui:setVehicle(vehicle)
		ui:addToUIManager()
		if JoypadState.players[playerNum+1] then
			JoypadState.players[playerNum+1].focus = ui
		end
	end
end

function ISVehicleMenu.onWalkPath(playerObj)
	ISTimedActionQueue.add(ISPathFindAction:new(playerObj))
end

function ISVehicleMenu.onKeyPressed(key)
	local playerObj = getSpecificPlayer(0)
	if not playerObj then return end
	if playerObj:isDead() then return end
	local vehicle = playerObj:getVehicle()
	if not vehicle then
		vehicle = ISVehicleMenu.getVehicleToInteractWith(playerObj)
		if vehicle then
			if key == getCore():getKey("VehicleMechanics") then
				ISVehicleMenu.onMechanic(playerObj, vehicle)
				return
			end
		end
		return
	end
	if key == getCore():getKey("StartVehicleEngine") then
		if vehicle:isEngineRunning() then
			ISVehicleMenu.onShutOff(playerObj)
		else
			ISVehicleMenu.onStartEngine(playerObj)
		end
	elseif key == getCore():getKey("VehicleHeater") then
		ISVehicleMenu.onToggleHeater(playerObj)
	elseif key == getCore():getKey("VehicleMechanics") then
		ISVehicleMenu.onMechanic(playerObj, vehicle)
	elseif key == getCore():getKey("VehicleHorn") then
		if vehicle:isDriver(playerObj) then
			ISVehicleMenu.onHornStop(playerObj)
		end
	end
	-- Could be same as VehicleHorn key
	if key == getCore():getKey("Shout") then
		if not vehicle:isDriver(playerObj) then
			playerObj:Callout()
		end
	end
end

function ISVehicleMenu.onKeyStartPressed(key)
	local playerObj = getSpecificPlayer(0)
	if not playerObj then return end
	if playerObj:isDead() then return end
	if key == getCore():getKey("VehicleHorn") then
		local vehicle = playerObj:getVehicle()
		if vehicle and vehicle:isDriver(playerObj) then
			ISVehicleMenu.onHornStart(playerObj)
		end
	elseif key == getCore():getKey("VehicleSwitchSeat") then
		local vehicle = ISVehicleMenu.getVehicleToInteractWith(playerObj)
		if vehicle and vehicle:getScript() and vehicle:getScript():getWheelCount() > 0 then
			ISVehicleMenu.onShowSeatUI(playerObj, vehicle)
		end
	end
end

function ISVehicleMenu.onHorn(playerObj)
	ISTimedActionQueue.add(ISHorn:new(playerObj))
end

function ISVehicleMenu.onHornStart(playerObj)
--	print "onHornStart"
	local vehicle = playerObj:getVehicle()
	if vehicle:getBatteryCharge() <= 0.0 then return end
	if isClient() then
		sendClientCommand(playerObj, 'vehicle', 'onHorn', {state="start"})
	else
		vehicle:onHornStart();
	end
end

function ISVehicleMenu.onHornStop(playerObj)
--	print "onHornStop"
	local vehicle = playerObj:getVehicle()
	if isClient() then
		sendClientCommand(playerObj, 'vehicle', 'onHorn', {state="stop"})
	else
		vehicle:onHornStop();
	end
end

function ISVehicleMenu.onLightbar(playerObj)
	ISTimedActionQueue.add(ISLightbarUITimedAction:new(playerObj))
end

function ISVehicleMenu.onAttachTrailer(playerObj, vehicle, attachmentA, attachmentB)
	local square = vehicle:getCurrentSquare()
	local vehicleB = ISVehicleTrailerUtils.getTowableVehicleNear(square, vehicle, attachmentA, attachmentB)
	if not vehicleB then return end
	local nextAction = ISAttachTrailerToVehicle:new(playerObj, vehicle, vehicleB, attachmentA, attachmentB)
	if not ISVehicleTrailerUtils.walkToTrailer(playerObj, vehicle, attachmentA, nextAction) then return end
end

function ISVehicleMenu.onDetachTrailer(playerObj, vehicle, attachmentA)
	local nextAction = ISDetachTrailerFromVehicle:new(playerObj, vehicle, attachmentA)
	if not ISVehicleTrailerUtils.walkToTrailer(playerObj, vehicle, attachmentA, nextAction) then return end
end

Events.OnFillWorldObjectContextMenu.Add(ISVehicleMenu.OnFillWorldObjectContextMenu)
Events.OnKeyPressed.Add(ISVehicleMenu.onKeyPressed);
Events.OnKeyStartPressed.Add(ISVehicleMenu.onKeyStartPressed);

