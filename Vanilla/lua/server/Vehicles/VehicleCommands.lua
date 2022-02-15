--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

local VehicleCommands = {}
local Commands = {}

VehicleCommands.wantNoise = getDebug() or false

local noise = function(msg)
	if VehicleCommands.wantNoise then
--		print('VehicleCommands: '..msg)
	end
end

function Commands.hotwireEngine(player, args)
	local electricSkill = args.electricSkill;
	player:getVehicle():tryHotwire(electricSkill);
end

function Commands.startEngine(player, args)
	local vehicle = player:getVehicle()
	local haveKey = args.haveKey;
	if vehicle then
		if vehicle:isDriver(player) then
			if true or vehicle:isEngineWorking() then
				vehicle:tryStartEngine(haveKey)
			else
				noise('engine not working')
			end
		else
			noise('player not driver')
		end
	else
		noise('player not in vehicle')
	end
end

function Commands.shutOff(player, args)
	local vehicle = player:getVehicle()
	if vehicle then
		if vehicle:isDriver(player) then
			vehicle:shutOff()
		else
			noise('player not driver')
		end
	else
		noise('player not in vehicle')
	end
end

function Commands.installPart(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		local part = vehicle:getPartById(args.part)
		if not part then
			noise('no such part '..tostring(args.part))
			return
		end
		local keyvalues = part:getTable("install");
		local perks = keyvalues.skills;
		local success, failure = VehicleUtils.calculateInstallationSuccess(perks, player, args.perks);
		local item = args.item
		if not instanceof(item, "InventoryItem") then
			noise('item is nil')
			return
		end
		if ZombRand(100) < success then
			part:setInventoryItem(item, args.mechanicSkill)
			local tbl = part:getTable("install")
			if tbl and tbl.complete then
				VehicleUtils.callLua(tbl.complete, vehicle, part)
			end
			vehicle:transmitPartItem(part)
			player:sendObjectChange('mechanicActionDone', { success = true, vehicleId = vehicle:getId(), partId = part:getId(), itemId = item:getID(), installing = true })
		elseif ZombRand(100) < failure then
			item:setCondition(item:getCondition() - ZombRand(5,10));
			player:sendObjectChange('addItem', { item = item })
			playServerSound("PZ_MetalSnap", player:getCurrentSquare());
			player:sendObjectChange('mechanicActionDone', { success = false, vehicleId = vehicle:getId(), partId = part:getId(), itemId = item:getID(), installing = true })
		else
			player:sendObjectChange('addItem', { item = item })
			player:sendObjectChange('mechanicActionDone', { success = false, vehicleId = vehicle:getId(), partId = part:getId(), itemId = item:getID(), installing = true })
		end
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.uninstallPart(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		local part = vehicle:getPartById(args.part)
		if not part then
			noise('no such part '..tostring(args.part))
			return
		end
		local keyvalues = part:getTable("install");
		local perks = keyvalues.skills;
		local success, failure = VehicleUtils.calculateInstallationSuccess(perks, player, args.perks);
		local item = part:getInventoryItem()
		if not item then
			noise('part already uninstalled '..args.part)
			return
		end
		if ZombRand(100) < success then
			--VehicleUtils.lowerUninstalledItemCondition(part, item, args.mechanicSkill, player)
			part:setInventoryItem(nil)
			item:setItemCapacity(args.contentAmount);
			local tbl = part:getTable("uninstall")
			if tbl and tbl.complete then
				VehicleUtils.callLua(tbl.complete, vehicle, part, item)
			end
			vehicle:transmitPartItem(part)
			player:sendObjectChange('addItem', { item = item })
			player:sendObjectChange('mechanicActionDone', { success = true, vehicleId = vehicle:getId(), partId = part:getId(), itemId = item:getID(), installing = false})
		elseif ZombRand(failure) < 100 then
			part:setCondition(part:getCondition() - ZombRand(5,10));
			vehicle:transmitPartCondition(part)
			playServerSound("PZ_MetalSnap", player:getCurrentSquare());
			player:sendObjectChange('mechanicActionDone', { success = false, vehicleId = vehicle:getId(), partId = part:getId(), itemId = item:getID(), installing = false})
		end
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.fixPart(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		local part = vehicle:getPartById(args.part)
		if not part then
			noise('no such part '..tostring(args.part))
			return
		end
		local item = part:getInventoryItem()
		if item then
			part:setCondition(args.condition)
			item:setCondition(args.condition)
			item:setHaveBeenRepaired(args.haveBeenRepaired)
			part:doInventoryItemStats(item, part:getMechanicSkillInstaller())
			if part:isContainer() and not part:getItemContainer() then
				-- Changing condition might change capacity.
				-- This limits content amount to max capacity.
				part:setContainerContentAmount(part:getContainerContentAmount())
			end
			vehicle:updatePartStats()
			vehicle:updateBulletStats()
			vehicle:transmitPartCondition(part)
			vehicle:transmitPartItem(part)
			vehicle:transmitPartModData(part)
		else
			noise('part item is missing'..args.part)
		end
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.repairEngine(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		local part = vehicle:getPartById("Engine")
		if not part then
			noise('no such part Engine')
			return
		end
		local condPerPart = 1 + (args.skillLevel / 2)
		if condPerPart > 5 then condPerPart = 5 end
		local done = 0
		for i=1,args.numberOfParts do
			part:setCondition(part:getCondition() + condPerPart)
			done = done + 1
			if part:getCondition() >= 100 then
				part:setCondition(100)
				break
			end
		end
		if done > 0 then
			if args.giveXP then
				player:sendObjectChange('addXp', { perk = Perks.Mechanics:index(), xp = done, noMultiplier = false })
			end
			player:sendObjectChange('removeItemType', { type = 'Base.EngineParts', count = done })
			vehicle:transmitPartCondition(part)
		end
		player:sendObjectChange('mechanicActionDone', { success = (done > 0), vehicleId = vehicle:getId(), partId = part:getId(), itemId = -1, installing = true })
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.takeEngineParts(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		local part = vehicle:getPartById("Engine")
		if not part then
			noise('no such part Engine')
			return
		end
		local cond = part:getCondition()
		local condForPart = math.max(20 - args.skillLevel, 5)
		condForPart = ZombRand(condForPart / 3, condForPart)
		local numParts = math.floor(cond / condForPart)
		if numParts > 0 then
			player:sendObjectChange('addItemOfType', { type = 'Base.EngineParts', count = numParts })
		end
		part:setCondition(0)
		vehicle:transmitPartCondition(part)
		player:sendObjectChange('mechanicActionDone', { success = (numParts > 0), vehicleId = vehicle:getId(), partId = part:getId(), itemId = -1, installing = true })
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.setPartCondition(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		local part = vehicle:getPartById(args.part)
		if not part then
			noise('no such part '..tostring(args.part))
			return
		end
		part:setCondition(args.condition)
		vehicle:transmitPartCondition(part)
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.setContainerContentAmount(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		local part = vehicle:getPartById(args.part)
		if not part then
			noise('no such part '..tostring(args.part))
			return
		end
		part:setContainerContentAmount(args.amount)
		vehicle:transmitPartModData(part)
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.toggleHeater(player, args)
	local vehicle = player:getVehicle();
	if vehicle then
		local part = vehicle:getPartById("Heater");
		if part then
			part:getModData().active = args.on;
			part:getModData().temperature = args.temp;
			vehicle:transmitPartModData(part);
		end
	else
		noise('player not in vehicle');
	end
end

function Commands.setHeadlightsOn(player, args)
	local vehicle = player:getVehicle()
	if vehicle then
		vehicle:setHeadlightsOn(args.on)
	else
		noise('player not in vehicle')
	end
end

function Commands.setTrunkLocked(player, args)
	local vehicle = player:getVehicle()
	if vehicle then
		vehicle:setTrunkLocked(args.locked)
	else
		noise('player not in vehicle')
	end
end

function Commands.setStoplightsOn(player, args)
	local vehicle = player:getVehicle()
	if vehicle then
		vehicle:setStoplightsOn(args.on)
	else
		noise('player not in vehicle')
	end
end

function Commands.setTirePressure(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		local part = vehicle:getPartById(args.part)
		if not part then
			noise('no such part '..tostring(args.part))
			return
		end
		part:setContainerContentAmount(args.psi, true, true)
		local wheelIndex = part:getWheelIndex()
		-- TODO: sync inflation
		vehicle:setTireInflation(wheelIndex, part:getContainerContentAmount() / part:getContainerCapacity())
		vehicle:transmitPartModData(part)
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.setDoorLocked(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		local part = vehicle:getPartById(args.part)
		if not part then
			noise('no such part '..tostring(args.part))
			return
		end
		if not part:getDoor() then
			noise('part ' .. args.part .. ' has no door')
			return
		end
		if not part:getDoor():isLockBroken() then
			part:getDoor():setLocked(args.locked)
		end
		--vehicle:toggleLockedDoor(part, player, args.locked)
		vehicle:transmitPartDoor(part)
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.setDoorOpen(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		local part = vehicle:getPartById(args.part)
		if not part then
			noise('no such part '..tostring(args.part))
			return
		end
		if not part:getDoor() then
			noise('part ' .. args.part .. ' has no door')
			return
		end
		part:getDoor():setOpen(args.open)
		vehicle:transmitPartDoor(part)
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.setWindowOpen(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		local part = vehicle:getPartById(args.part)
		if not part then
			noise('no such part '..tostring(args.part))
			return
		end
		if not part:getWindow() then
			noise('part ' .. args.part .. ' has no window')
			return
		end
		part:getWindow():setOpen(args.open)
		vehicle:transmitPartWindow(part)
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.damageWindow(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		local part = vehicle:getPartById(args.part)
		if not part then
			noise('no such part '..tostring(args.part))
			return
		end
		if not part:getWindow() then
			noise('part ' .. args.part .. ' has no window')
			return
		end
		part:getWindow():damage(tonumber(args.amount))
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.onHorn(player, args)
	local vehicle = player:getVehicle()
	local state = args.state;
	if vehicle then
		if state == "start" then
			vehicle:onHornStart();
		end
		if state == "stop" then
			vehicle:onHornStop();
		end
	else
		noise('player not in vehicle')
	end
end

function Commands.onBackSignal(player, args)
	local vehicle = player:getVehicle()
	local state = args.state;
	if vehicle then
		if state == "start" then
			vehicle:onBackMoveSignalStart();
		end
		if state == "stop" then
			vehicle:onBackMoveSignalStop();
		end
	else
		noise('player not in vehicle')
	end
end

function Commands.setLightbarLightsMode(player, args)
	local vehicle = player:getVehicle()
	local mode = tonumber(args.mode);
	if vehicle then
		vehicle:setLightbarLightsMode(mode)
	else
		noise('player not in vehicle')
	end
end

function Commands.setLightbarSirenMode(player, args)
	local vehicle = player:getVehicle()
	local mode = tonumber(args.mode);
	if vehicle then
		vehicle:setLightbarSirenMode(mode)
		vehicle:setSirenStartTime(getGameTime():getWorldAgeHours())
	else
		noise('player not in vehicle')
	end
end

function Commands.putKeyInIgnition(player, args)
	local vehicle = player:getVehicle()
	if vehicle and vehicle:isDriver(player) then
		vehicle:putKeyInIgnition(args.key)
	else
		noise('player not driving vehicle')
	end
end

function Commands.removeKeyFromIgnition(player, args)
	local vehicle = player:getVehicle()
	if vehicle and vehicle:isDriver(player) then
		vehicle:removeKeyFromIgnition()
	else
		noise('player not driving vehicle')
	end
end

function Commands.putKeyOnDoor(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		vehicle:putKeyOnDoor(args.key)
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.removeKeyFromDoor(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		vehicle:removeKeyFromDoor()
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.crash(player, args)
	local vehicle = getVehicleById(args.vehicle);
	if vehicle then
		vehicle:crash(args.amount, args.front);
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.getKey(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		local item = vehicle:createVehicleKey()
		if item then
			player:sendObjectChange("addItem", { item = item })
		end
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.repair(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		vehicle:repair()
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.setBloodIntensity(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		vehicle:setBloodIntensity(args.id, args.intensity)
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.setRust(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		vehicle:setRust(args.rust)
		vehicle:transmitRust()
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.repairPart(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		local part = vehicle:getPartById(args.part)
		if not part then
			noise('no such part '..tostring(args.part))
			return
		end
		part:repair()
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.remove(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		vehicle:permanentlyRemove()
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end
function Commands.configHeadlight(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		local part = vehicle:getPartById(args.part)
		if not part then
			noise('no such part '..tostring(args.part))
			return
		end
		if args.dir == 1 then
			part:getLight():setFocusingUp()
		else
			part:getLight():setFocusingDown()
		end
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

function Commands.cheatHotwire(player, args)
	local vehicle = getVehicleById(args.vehicle)
	if vehicle then
		vehicle:cheatHotwire(args.hotwired, args.broken)
	else
		noise('no such vehicle id='..tostring(args.vehicle))
	end
end

VehicleCommands.OnClientCommand = function(module, command, player, args)
	if module == 'vehicle' and Commands[command] then
		local argStr = ''
		args = args or {}
		for k,v in pairs(args) do
			argStr = argStr..' '..k..'='..tostring(v)
		end
		noise('received '..module..' '..command..' '..tostring(player)..argStr)
		Commands[command](player, args)
	end
end

Events.OnClientCommand.Add(VehicleCommands.OnClientCommand)
