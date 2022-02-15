--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

Vehicles = {}
Vehicles.CheckEngine = {}
Vehicles.CheckOperate = {}
Vehicles.ContainerAccess = {}
Vehicles.Create = {}
Vehicles.Init = {}
Vehicles.InstallComplete = {}
Vehicles.InstallTest = {}
Vehicles.UninstallComplete = {}
Vehicles.UninstallTest = {}
Vehicles.Update = {}
Vehicles.Use = {}
-- Used to not always send temp when it's changing
Vehicles.elaspedMinutesForHeater = {};
Vehicles.elaspedMinutesForEngine = {};

-- Jerry Cans are typically 20L, 10L, or 5L
Vehicles.JerryCanLitres = 10

function Vehicles.ContainerAccess.TruckBed(vehicle, part, chr)
	if chr:getVehicle() then return false end
	if not vehicle:isInArea(part:getArea(), chr) then return false end
	local trunkDoor = vehicle:getPartById("TrunkDoor") or vehicle:getPartById("DoorRear")
	if trunkDoor and trunkDoor:getDoor() then
		if not trunkDoor:getInventoryItem() then return true end
		if not trunkDoor:getDoor():isOpen() then return false end
	end
--	if part:getInventoryItem() and not chr:getInventory():haveThisKeyId(vehicle:getKeyId()) then return false end
	return true
end

function Vehicles.ContainerAccess.TruckBedOpen(vehicle, part, chr)
	if chr:getVehicle() then return false end
	if not vehicle:isInArea(part:getArea(), chr) then return false end
	return true
end

function Vehicles.ContainerAccess.Seat(vehicle, part, chr)
	if not part:getInventoryItem() then return false; end
	local seat = part:getContainerSeatNumber()
	-- Can't put stuff on an occupied seat.
	if seat ~= -1 and vehicle:getCharacter(seat) then
		return false
	end
	if chr:getVehicle() == vehicle then
		-- Can the seated player reach the other seat?
		return vehicle:canSwitchSeat(vehicle:getSeat(chr), seat) and
				not vehicle:getCharacter(seat)
	elseif chr:getVehicle() then
		-- Can't reach seat from inside a different vehicle.
		return false
	else
		if not vehicle:isInArea(part:getArea(), chr) then return false end
		local doorPart = vehicle:getPassengerDoor(seat)
		if doorPart and doorPart:getDoor() and not doorPart:getDoor():isOpen() then
			return false
		end
		-- Rear seats in a 2-door vehicle for example.
		if not doorPart and vehicle:getPartById("DoorFrontLeft") then return false end
		-- Door is uninstalled/open, or an exterior seat (motorcycle?)
		return true
	end
end

function Vehicles.ContainerAccess.GloveBox(vehicle, part, chr)
	if chr:getVehicle() == vehicle then
		local seat = vehicle:getSeat(chr)
		-- Can the seated player reach the passenger seat?
		-- Only character in front seat can access it
		return seat == 1 or seat == 0;
	elseif chr:getVehicle() then
		-- Can't reach from inside a different vehicle.
		return false
	else
		-- Standing outside the vehicle.
		if not vehicle:isInArea(part:getArea(), chr) then return false end
		local doorPart = vehicle:getPartById("DoorFrontRight")
		if doorPart and doorPart:getDoor() and not doorPart:getDoor():isOpen() then
			return false
		end
		return true
	end
end

function Vehicles.ContainerAccess.GasTank(vehicle, part, chr)
	if chr:getVehicle() then return false end
	if not vehicle:isInArea(part:getArea(), chr) then return false end
	return true
end

function Vehicles.Create.Battery(vehicle, part)
	local item = VehicleUtils.createPartInventoryItem(part);
	if SandboxVars.VehicleEasyUse then
		item:setUsedDelta(1);
		return;
	end
	if vehicle:isGoodCar() then
		item:setUsedDelta(ZombRandFloat(0.8,1));
		return;
	end
	local tot = (getGameTime():getWorldAgeHours() / 5000);
	tot = tot + (((getSandboxOptions():getTimeSinceApo() - 1) * 30 * 24) / 4500);
	tot = ZombRandFloat(tot - 0.15, tot + 0.15);
	tot = 1 - tot;
	tot = math.min(tot, 1)
	item:setUsedDelta(math.max(0, tot));
end

function Vehicles.Create.Door(vehicle, part)
	local item = VehicleUtils.createPartInventoryItem(part);
	if SandboxVars.VehicleEasyUse then
		part:getDoor():setOpen(false);
		part:getDoor():setLocked(false);
		part:getDoor():setLockBroken(false);
		return;
	end
	if vehicle:isGoodCar() then
		part:getDoor():setOpen(false);
		part:getDoor():setLocked(SandboxVars.LockedCar ~= 1);
		part:getDoor():setLockBroken(false);
		return;
	end
	local chance = 50;
	if SandboxVars.LockedCar == 1 then
		chance = -1;
	elseif SandboxVars.LockedCar == 2 then
		chance = 15;
	elseif SandboxVars.LockedCar == 3 then
		chance = 30;
	elseif SandboxVars.LockedCar == 5 then
		chance = 65;
	elseif SandboxVars.LockedCar == 6 then
		chance = 80;
	end
	local doorFrontLeft = vehicle:getPartById("DoorFrontLeft");
	if doorFrontLeft and doorFrontLeft:getDoor() and doorFrontLeft ~= part then
		part:getDoor():setOpen(doorFrontLeft:getDoor():isOpen());
		part:getDoor():setLocked(doorFrontLeft:getDoor():isLocked())
		part:getDoor():setLockBroken(ZombRand(1,100) < 5 ) -- 5%
		return
	end
	local locked = false;
	if ZombRand(100) <= chance then
		locked = true;
	else -- car is open, no alarm
		part:getVehicle():setAlarmed(false);
	end
	part:getDoor():setOpen(false);
	part:getDoor():setLocked(locked);
	part:getDoor():setLockBroken(ZombRand(1,100) < 5 ) -- 5%
end

function Vehicles.Create.TrunkDoor(vehicle, part)
	local item = VehicleUtils.createPartInventoryItem(part)
	if SandboxVars.VehicleEasyUse then
		part:getDoor():setOpen(false)
		part:getDoor():setLocked(false)
		part:getDoor():setLockBroken(false)
		return
	end
	if vehicle:isGoodCar() then
		part:getDoor():setOpen(false)
		part:getDoor():setLocked(SandboxVars.LockedCar ~= 1)
		part:getDoor():setLockBroken(false)
		return
	end
	local chance = 50
	if SandboxVars.LockedCar == 1 then
		chance = -1
	elseif SandboxVars.LockedCar == 2 then
		chance = 15
	elseif SandboxVars.LockedCar == 3 then
		chance = 30
	elseif SandboxVars.LockedCar == 5 then
		chance = 65
	elseif SandboxVars.LockedCar == 6 then
		chance = 80
	end
	local locked = ZombRand(100) <= chance
	part:getDoor():setOpen(false)
	part:getDoor():setLocked(locked)
	part:getDoor():setLockBroken(ZombRand(1,100) < 5 ) -- 5%
end

function Vehicles.Create.GasTank(vehicle, part)
	local invItem = VehicleUtils.createPartInventoryItem(part)

	if not invItem then return; end
	if SandboxVars.VehicleEasyUse then
--		invItem:setItemCapacity(invItem:getMaxCapacity());
		part:setContainerContentAmount(part:getContainerCapacity());
--		part:setContainerContentAmount(0.2);
		return;
	end
	if vehicle:isGoodCar() then
		part:setContainerContentAmount(ZombRand(part:getContainerCapacity()/1.5, part:getContainerCapacity()));
		return;
	end
	-- First, we check if there will be gas in there
	local initialChance = 70;
	if SandboxVars.ChanceHasGas == 1 then
		initialChance = 20;
	end
	if SandboxVars.ChanceHasGas == 3 then
		initialChance = 95;
	end

	-- next, we fill it, depending on the initial gas sanbox option
	if ZombRand(100) <= initialChance then
		local minGas = ZombRand(3,part:getContainerCapacity()/3);
		local maxGas = ZombRand(part:getContainerCapacity()/3, part:getContainerCapacity()/2);
		if SandboxVars.InitialGas == 1 then
			minGas = 1;
			maxGas = ZombRand(2,part:getContainerCapacity()/5);
		elseif SandboxVars.InitialGas == 2 then
			minGas = 1;
			maxGas = ZombRand(4,part:getContainerCapacity()/4);
		elseif SandboxVars.InitialGas == 4 then
			minGas = ZombRand(5, part:getContainerCapacity()/2);
			maxGas = ZombRand(part:getContainerCapacity()/2, part:getContainerCapacity());
		elseif SandboxVars.InitialGas == 5 then
			minGas = ZombRand(8, part:getContainerCapacity()/2);
			maxGas = part:getContainerCapacity();
		end
		local gas = ZombRand(minGas, maxGas);
		part:setContainerContentAmount(gas);
	else
		part:setContainerContentAmount(0);
	end
end

function Vehicles.Create.Engine(vehicle, part)
	if SandboxVars.VehicleEasyUse then
		vehicle:setEngineFeature(100, 30, vehicle:getScript():getEngineForce());
		return;
	end
	part:setRandomCondition(nil);
	local type = VehicleType.getTypeFromName(vehicle:getVehicleType());
	local engineQuality = 100;
	if type then
		local baseQuality = vehicle:getScript():getEngineQuality() * type:getBaseVehicleQuality();
		-- bit of randomize
		engineQuality = ZombRand(baseQuality - 10, baseQuality + 10);
	end
	engineQuality = ZombRand(engineQuality - 5, engineQuality + 5);
	engineQuality = math.max(engineQuality, 0)
	engineQuality = math.min(engineQuality, 100)

	local engineLoudness = vehicle:getScript():getEngineLoudness() or 100;
	engineLoudness = engineLoudness * (SandboxVars.ZombieAttractionMultiplier or 1);

	local qualityBoosted = engineQuality * 1.6;
	if qualityBoosted > 100 then qualityBoosted = 100; end
	local qualityModifier = math.max(0.6, ((qualityBoosted) / 100));
	local enginePower = vehicle:getScript():getEngineForce() * qualityModifier;

	vehicle:setEngineFeature(engineQuality, engineLoudness, enginePower);
end

function Vehicles.Create.Headlight(vehicle, part)
	local item = VehicleUtils.createPartInventoryItem(part)
	-- xOffset,yOffset,distance,intensity,dot,focusing
	-- NOTE: distance,intensity,focusing values are ignored, instead they are
	-- set based on part condition.
	if part:getId() == "HeadlightLeft" then
		part:createSpotLight(0.5, 2.0, 8.0+ZombRand(16.0), 0.75, 0.96, ZombRand(200))
	elseif part:getId() == "HeadlightRight" then
		part:createSpotLight(-0.5, 2.0, 8.0+ZombRand(16.0), 0.75, 0.96, ZombRand(200))
	end
end

function Vehicles.Create.Radio(vehicle, part)
	local deviceData = part:createSignalDevice()
	deviceData:setIsTwoWay(false)
	deviceData:setTransmitRange(0)
	deviceData:setMicRange(0)
	deviceData:setBaseVolumeRange(10)
	deviceData:setIsPortable(false)
	deviceData:setIsTelevision(false)
	deviceData:setMinChannelRange(88000)
	deviceData:setMaxChannelRange(108000)
	deviceData:setIsBatteryPowered(false)
	deviceData:setIsHighTier(false)
	deviceData:setUseDelta(0.007)
	deviceData:generatePresets()
	deviceData:setRandomChannel()
	local invItem = VehicleUtils.createPartInventoryItem(part);
end

function Vehicles.Create.Default(vehicle, part)
	local invItem = VehicleUtils.createPartInventoryItem(part);
end

function Vehicles.Create.Tire(vehicle, part)
	local item = VehicleUtils.createPartInventoryItem(part)
	if vehicle:isGoodCar() then
		part:setContainerContentAmount(ZombRand(part:getContainerCapacity()/1.5, part:getContainerCapacity()), false, true);
		return;
	end
	local capacity = ZombRand((part:getContainerCapacity() - (part:getContainerCapacity() / 3)), part:getContainerCapacity());
	part:setContainerContentAmount(capacity, false, true);
end

function Vehicles.Create.Brake(vehicle, part)
	local item = VehicleUtils.createPartInventoryItem(part)
end

function Vehicles.Create.Window(vehicle, part)
	local item = VehicleUtils.createPartInventoryItem(part)
end

function Vehicles.Init.Door(vehicle, part)
end

function Vehicles.Init.Headlight(vehicle, part)
end

function Vehicles.Init.Tire(vehicle, part)
end

function Vehicles.Init.Window(vehicle, part)
end

function Vehicles.Update.EngineDoor(vehicle, part, elapsedMinutes)
	
end

function Vehicles.Update.GasTank(vehicle, part, elapsedMinutes)
	local invItem = part:getInventoryItem();
	if not invItem then return; end
	local amount = part:getContainerContentAmount()
	if elapsedMinutes > 0 and amount > 0 and vehicle:isEngineRunning() then
		local amountOld = amount
		-- calcul how much gas is used, based mainly on engine speed, engine quality & mass.
		local gasMultiplier = 90000;
		-- heater consume more gas
		local heater = vehicle:getHeater();
		if heater and heater:getModData().active then
			gasMultiplier = gasMultiplier + 5000;
		end
		-- if quality is 60, we do: 100 - 60 = 40; 40/2 = 20; 20/100=0.2; 0.2+1 = 1.2 : our multiplier;
		local qualityMultiplier = ((100 - vehicle:getEngineQuality()) / 200) + 1;
		local massMultiplier =  ((math.abs(1000 - vehicle:getScript():getMass())) / 300) + 1;
		-- the closer we are to change shift, the less we consume gas
		local speedToNextTransmission = ((vehicle:getMaxSpeed() / vehicle:getScript():getGearRatioCount()) * 0.71) * vehicle:getTransmissionNumber();
		local speedMultiplier = (speedToNextTransmission - vehicle:getCurrentSpeedKmHour()) * 350;
		-- if vehicle is stopped, we half the value of gas consummed
		if math.floor(vehicle:getCurrentSpeedKmHour()) > 0 then
			gasMultiplier = gasMultiplier / qualityMultiplier / massMultiplier;
		else
			gasMultiplier = (gasMultiplier / qualityMultiplier) * 2;
			speedMultiplier = 1;
		end
		-- we're at max gear, cap general gas consumption
		if speedMultiplier < 800 then
			speedMultiplier = 800;
		end
		
	
--		local engineSpeed = math.min(vehicle:getEngineSpeed(), 6000)
--		local engineSpeedCalc = 6000 - engineSpeed;

--		local newAmount = engineSpeedCalc / gasMultiplier;
		local newAmount = (speedMultiplier / gasMultiplier)  * SandboxVars.CarGasConsumption;
		newAmount =  newAmount * (vehicle:getEngineSpeed()/2500.0);
		amount = amount - elapsedMinutes * newAmount;
	
		-- if your gas tank is in bad condition, you can simply lose fuel
		if part:getCondition() < 70 then
			if ZombRand(part:getCondition() * 2) == 0 then
				amount = amount - 0.01;
			end
		end
	
		part:setContainerContentAmount(amount, false, true);
		amount = part:getContainerContentAmount();
		local precision = (amount < 0.5) and 2 or 1
		if VehicleUtils.compareFloats(amountOld, amount, precision) then
			vehicle:transmitPartModData(part)
		end
	end
end

function Vehicles.Update.Battery(vehicle, part, elapsedMinutes)
	if part:getInventoryItem() then
		local chargeOld = part:getInventoryItem():getUsedDelta()
		local charge = chargeOld
		-- Starting the engine drains the battery
		local engineStarted = vehicle:isEngineRunning()
		if engineStarted and not part:getModData().engineStarted then
			charge = charge - 0.025
		end
		part:getModData().engineStarted = engineStarted
		-- Running the engine charges the battery
		if elapsedMinutes > 0 and vehicle:isEngineRunning() then
			charge = math.min(charge + elapsedMinutes * 0.001, 1.0)
		end
		-- Having a generator & the engine not running charge the battery
		if not vehicle:isEngineRunning() and vehicle:getSquare() and vehicle:getSquare():haveElectricity() then
			charge = math.min(charge + elapsedMinutes * 0.001, 1.0)
		end
		if charge ~= chargeOld then
			part:getInventoryItem():setUsedDelta(charge)
			if VehicleUtils.compareFloats(chargeOld, charge, 2) then
				vehicle:transmitPartUsedDelta(part)
			end
		end
	end
	-- Hack, there's no Lightbar part.
	Vehicles.Update.Lightbar(vehicle, part, elapsedMinutes)
end

-- update engine T°, high T° (bad condition engine door, heater...) can cause damage to the engine
function Vehicles.Update.Engine(vehicle, part, elapsedMinutes)
	if not Vehicles.elaspedMinutesForEngine[vehicle:getId()] then
		Vehicles.elaspedMinutesForEngine[vehicle:getId()] = 0;
	end
	local partData = part:getModData()
	if not tonumber(partData.temperature) then
		partData.temperature = 0
	end
	local previousTemp = partData.temperature;
	if vehicle:isEngineRunning() then
		local max = 100;
		local increment = ZombRand(0,3);
		local engineDoor = vehicle:getPartById("EngineDoor");
		-- having no engine door or in bad condition will make the engine goes in high T°
		if engineDoor then
			if not engineDoor:getInventoryItem() then
				max = 200;
			else
				max = 100 + ((100 - engineDoor:getCondition()) / 3)
			end
		end
		partData.temperature = math.min(partData.temperature + increment * elapsedMinutes, max)
	elseif partData.temperature > 0 then
		partData.temperature = math.max(partData.temperature - 2 * elapsedMinutes, 0)
	end
	Vehicles.elaspedMinutesForEngine[vehicle:getId()] = Vehicles.elaspedMinutesForEngine[vehicle:getId()] + elapsedMinutes;
	if isServer() and VehicleUtils.compareFloats(previousTemp, partData.temperature, 2) and Vehicles.elaspedMinutesForEngine[vehicle:getId()] > 2 then
		Vehicles.elaspedMinutesForEngine[vehicle:getId()] = 0;
		vehicle:transmitPartModData(part);
	end
	-- stop updating parts when T° reach 0
	if partData.temperature <= 0 and not vehicle:isEngineRunning() and not vehicle:getDriver() then
		vehicle:setNeedPartsUpdate(false);
	end
end

function Vehicles.Update.Headlight(vehicle, part, elapsedMinutes)
	local light = part:getLight()
	if not light then return end
	local active = vehicle:getHeadlightsOn()
	if active and (not part:getInventoryItem() or vehicle:getBatteryCharge() <= 0.0) then
		active = false
--		vehicle:setHeadlightsOn(VehicleUtils.anyWorkingHeadlights(vehicle))
	end
	part:setLightActive(active)
	if active and not vehicle:isEngineRunning() then
		VehicleUtils.chargeBattery(vehicle, -0.000025 * elapsedMinutes)
	end
	-- TODO: burn out eventually
end

function Vehicles.Update.Heater(vehicle, part, elapsedMinutes)
	if not Vehicles.elaspedMinutesForHeater[vehicle:getId()] then
		Vehicles.elaspedMinutesForHeater[vehicle:getId()] = 0;
	end
	local pc = vehicle:getPartById("PassengerCompartment")
	local engine = vehicle:getPartById("Engine")
	if not pc or not engine then return end
	local pcData = pc:getModData()
	if not tonumber(pcData.temperature) then
		pcData.temperature = 0.0
	end
	local partData = part:getModData()
	if not tonumber(partData.temperature) then
		partData.temperature = 0
	end
--	print(elapsedMinutes)
	local tempInc = 0.5 + (math.min(engine:getModData().temperature / 100, 0.7))
	local previousTemp = pcData.temperature;
--		print("heater temp " .. partData.temperature .. " - " .. pcData.temperature .. " - " .. tempInc)
	if partData.active and vehicle:isEngineRunning() and engine:getModData().temperature > 30 and ((partData.temperature > 0 and pcData.temperature <= partData.temperature) or (partData.temperature < 0 and pcData.temperature >= partData.temperature)) then
		if partData.temperature > 0 then
			pcData.temperature = math.min(pcData.temperature + tempInc * elapsedMinutes, partData.temperature)
		else
			pcData.temperature = math.max(pcData.temperature - tempInc * elapsedMinutes, partData.temperature)
		end
		if partData.temperature > 0 and pcData.temperature > partData.temperature then
			pcData.temperature = partData.temperature
		end
		if partData.temperature < 0 and pcData.temperature < partData.temperature then
			pcData.temperature = partData.temperature
		end
	else
		if pcData.temperature > 0 then
			pcData.temperature = math.max(pcData.temperature - 0.1 * elapsedMinutes, 0)
		else
			pcData.temperature = math.min(pcData.temperature + 0.1 * elapsedMinutes, 0)
		end
	end
	if partData.active and vehicle:isEngineRunning() then
		VehicleUtils.chargeBattery(vehicle, -0.000035 * elapsedMinutes)
	end
	Vehicles.elaspedMinutesForHeater[vehicle:getId()] = Vehicles.elaspedMinutesForHeater[vehicle:getId()] + elapsedMinutes;
	if isServer() and VehicleUtils.compareFloats(previousTemp, pcData.temperature, 2) and Vehicles.elaspedMinutesForHeater[vehicle:getId()] > 2 then
		Vehicles.elaspedMinutesForHeater[vehicle:getId()] = 0;
		vehicle:transmitPartModData(pc);
	end
end

-- Hack, there is no Lightbar part.
function Vehicles.Update.Lightbar(vehicle, part, elapsedMinutes)
	if not vehicle:hasLightbar() then return end
	local activeLights = vehicle:getLightbarLightsMode() > 0
	local activeSiren = vehicle:getLightbarSirenMode() > 0
	-- Check anti-griefing Sandbox option.
	if activeSiren and vehicle:sirenShutoffTimeExpired() then
		vehicle:setLightbarSirenMode(0)
		activeSiren = false
	end
	if vehicle:getBatteryCharge() <= 0.0 then
		if activeLights then
--			vehicle:setLightbarLightsMode(0)
			activeLights = false
		end
		if activeSiren then
--			vehicle:setLightbarSirenMode(0)
			activeSiren = false
		end
	end
	-- Siren and lightbar drain the battery when the engine is off.
	if not vehicle:isEngineRunning() then
		if activeLights then
			VehicleUtils.chargeBattery(vehicle, -0.000025 * elapsedMinutes)
		end
		if activeSiren then
			VehicleUtils.chargeBattery(vehicle, -0.000025 * elapsedMinutes)
		end
	end
end

-- windows open = cool down a bit the passengers
-- all windows closed = heat up a bit the passengers
function Vehicles.Update.PassengerCompartment(vehicle, part, elapsedMinutes)
	local pc = vehicle:getPartById("PassengerCompartment")
	local heater = vehicle:getHeater();
	if not pc or not heater then return end
	local pcData = pc:getModData()
	if not pcData.windowtemperature then pcData.windowtemperature = 0.0; end
	if not pcData.temperature then pcData.temperature = 0.0; end
	if vehicle:windowsOpen() > 0 then
		pcData.temperature = math.max(pcData.temperature - (0.1*vehicle:windowsOpen()) * elapsedMinutes, -5);
		pcData.windowtemperature = math.max(pcData.windowtemperature - (0.1*vehicle:windowsOpen()) * elapsedMinutes, 0);
	else
		if not heater:getModData().active then
			pcData.windowtemperature = math.min(pcData.windowtemperature + 0.1 * elapsedMinutes, 5);
		else
			pcData.windowtemperature = math.max(pcData.windowtemperature - 0.1 * elapsedMinutes, 0);
		end
	end
		-- IDEA: toxicity by piping exhaust
end

function Vehicles.Update.Radio(vehicle, part, elapsedMinutes)
	local deviceData = part:getDeviceData()
	if deviceData and deviceData:getIsTurnedOn() and not vehicle:isEngineRunning() then
		VehicleUtils.chargeBattery(vehicle, -0.000025 * elapsedMinutes)
	end
end

-- lower brake condition when braking, bit special compared to others as we upadte less often we need to keep track of what speed you were when u started braking (done this on the java side)
function Vehicles.Update.Brakes(vehicle, part, elapsedMinutes)
	if vehicle:isEngineRunning() and vehicle:getBrakeSpeedBetweenUpdate() > 0 and part:getInventoryItem() then
		local speedMod = (math.min(80, vehicle:getBrakeSpeedBetweenUpdate()) / 20);
		if ZombRandFloat(0, 100) < speedMod then
			part:setCondition(part:getCondition() - 1);
			vehicle:transmitPartCondition(part);
			vehicle:updatePartStats();
		end
	end
end

Vehicles.newSystemConditionLowerMult = 4;

function Vehicles.LowerCondition(vehicle, part, elapsedMinutes)
	if vehicle:isEngineRunning() and vehicle:getCurrentSpeedKmHour() > 10 and part:getInventoryItem() then
		local chance = part:getInventoryItem():getConditionLowerNormal()*Vehicles.newSystemConditionLowerMult;
		if vehicle:isDoingOffroad() then chance = part:getInventoryItem():getConditionLowerOffroad()*Vehicles.newSystemConditionLowerMult / vehicle:getOffroadEfficiency(); end
		
		-- will also depend on speed/current steering
		chance = chance + (vehicle:getCurrentSpeedKmHour() / 200);
		chance = chance + math.abs(vehicle:getCurrentSteering() / 2)
		
		if part:getCondition() > 0 and ZombRandFloat(0, 100) < chance then
			part:setCondition(part:getCondition() - 1);
			vehicle:transmitPartCondition(part);
			vehicle:updatePartStats();
		end
		return chance;
	end
	return 0;
end

-- lower the suspension condition depending on your speed/current steering
function Vehicles.Update.Suspension(vehicle, part, elapsedMinutes)
	Vehicles.LowerCondition(vehicle, part, elapsedMinutes);
end

function Vehicles.Update.Muffler(vehicle, part, elapsedMinutes)
	Vehicles.LowerCondition(vehicle, part, elapsedMinutes);
end

-- lower the tire condition depending on your speed/current steering
-- also handle tire explosing because of condition/lack of air here
function Vehicles.Update.Tire(vehicle, part, elapsedMinutes)
	if vehicle:isEngineRunning() and vehicle:getCurrentSpeedKmHour() > 10 and part:getInventoryItem() then
		local chance = Vehicles.LowerCondition(vehicle, part, elapsedMinutes);
		
		-- randomly losing air
		if part:getContainerContentAmount() > 0 and ZombRandFloat(0, 100) < (chance / 2) then
			part:setContainerContentAmount(part:getContainerContentAmount() - 1, false, true);
		end
		
		-- chance of tire explosing
		-- because of it not containing enough air
		if part:getContainerContentAmount() < 5 then
			local contentMod = (part:getInventoryItem():getMaxCapacity() - part:getContainerContentAmount())  / 350;
			if part:getContainerContentAmount() == 0 or ZombRandFloat(0, 100) < contentMod then
				vehicle:getSquare():AddWorldInventoryItem(part:getInventoryItem(), 0,0,0);
				VehicleUtils.RemoveTire(part, false);
			end
		end
	
		-- then because of condition
		if part:getCondition() < 15 then
			local condMod = (100 - part:getCondition())  / 350;
			if part:getCondition() == 0 or ZombRandFloat(0, 100) < condMod then VehicleUtils.RemoveTire(part, true); end
		end
	end
end

-- having a damaged trunk or no trunk at all will make item go on floor
function Vehicles.Update.TrunkDoor(vehicle, part, elapsedMinutes)
	local speedNeeded = 20;
	local steeringModifier = 5;
	-- opened trunk like pickup require more speed/steering
--	if not part:getItemType() then
--		speedNeeded = 60;
--		steeringModifier = 2.5;
--	end
	
	if vehicle:isEngineRunning() and part:getItemContainer() and vehicle:getCurrentSpeedKmHour() > speedNeeded and not part:getItemContainer():getItems():isEmpty() then
		-- if u don't have a trunk door or is in poor condition
		if not part:getInventoryItem() or part:getInventoryItem():getCondition() < 60 then
			-- the more you turn hard, the more chance you'll have to drop item
			local chanceToDropItem = math.abs(vehicle:getCurrentSteering()) * steeringModifier;
			-- the more damaged your trunk is, the more chance you'll have to drop item
			if part:getInventoryItem() then
				chanceToDropItem = chanceToDropItem + ((100 - part:getInventoryItem():getCondition()) / 100)
			else -- no trunk
				chanceToDropItem = chanceToDropItem + 1;
			end
			-- drop item!
			if ZombRandFloat(0, 100) < chanceToDropItem then
				local item = part:getItemContainer():getItems():get(ZombRand(0, part:getItemContainer():getItems():size()-1));
				vehicle:getSquare():AddWorldInventoryItem(item, 0,0,0);
				part:getItemContainer():Remove(item);
			end
		end
	end
end

function Vehicles.Use.Door(vehicle, part, character)
	for seat=0,vehicle:getMaxPassengers()-1 do
		if vehicle:getPassengerDoor(seat) == part then
			ISVehicleMenu.onEnter(character, vehicle, seat)
			break
		end
		if vehicle:getPassengerDoor2(seat) == part then
			ISVehicleMenu.onEnter2(character, vehicle, seat)
			break
		end
	end
end

function Vehicles.Use.EngineDoor(vehicle, part, character)
	if not part:getInventoryItem() then
		local ui = getPlayerMechanicsUI(character:getPlayerNum())
		if not ui then return end
		if ui:isReallyVisible() then
			ui:close()
		else
			ISTimedActionQueue.add(ISOpenMechanicsUIAction:new(character, vehicle, nil))
		end
	elseif part:getDoor():isOpen() then
		local ui = getPlayerMechanicsUI(character:getPlayerNum())
		local closed = false
		if ui and ui:isReallyVisible() then
			if ui.usedHood == part then closed = true end
			ui:close()
		end
		if not closed then
			ISTimedActionQueue.add(ISCloseVehicleDoor:new(character, vehicle, part))
		end
	else
		-- The hood is magically unlocked if any door/window is broken/open/uninstalled.
		-- If the player can get in the vehicle, they can pop the hood, no key required.
		if part:getDoor():isLocked() and VehicleUtils.RequiredKeyNotFound(part, character) then
			ISTimedActionQueue.add(ISUnlockVehicleDoor:new(character, part))
		end
		ISTimedActionQueue.add(ISOpenVehicleDoor:new(character, vehicle, part))
		ISTimedActionQueue.add(ISOpenMechanicsUIAction:new(character, vehicle, part))
	end
end

function Vehicles.Use.TrunkDoor(vehicle, part, character)
	if not part:getInventoryItem() then return end
	if part:getDoor():isOpen() then
		ISTimedActionQueue.add(ISCloseVehicleDoor:new(character, vehicle, part))
	else
		if part:getDoor():isLocked() then
			ISTimedActionQueue.add(ISUnlockVehicleDoor:new(character, part))
		end
		ISTimedActionQueue.add(ISOpenVehicleDoor:new(character, vehicle, part))
	end
end

function Vehicles.CheckEngine.GasTank(vehicle, part)
	return part:getInventoryItem() and round(part:getContainerContentAmount(), 3) > 0
end

function Vehicles.CheckEngine.Engine(vehicle, part)
	return part:getCondition() > 0;
end

function Vehicles.CheckOperate.Tire(vehicle, part)
if true then return true end -- allow driving on removed/flat tires
	local item = part:getInventoryItem()
	return item and (part:getContainerContentAmount() > part:getContainerCapacity() * 0.75) or false
end

function Vehicles.InstallTest.Default(vehicle, part, chr)
	if ISVehicleMechanics.cheat then return true; end
	local keyvalues = part:getTable("install")
	if not keyvalues then return false end
	if part:getInventoryItem() then return false end
	if not part:getItemType() or part:getItemType():isEmpty() then return false end
	local typeToItem = VehicleUtils.getItems(chr:getPlayerNum())
	if keyvalues.requireInstalled then
		local split = keyvalues.requireInstalled:split(";");
		for i,v in ipairs(split) do
			if not vehicle:getPartById(v) or not vehicle:getPartById(v):getInventoryItem() then return false; end
		end
	end
	if not VehicleUtils.testProfession(chr, keyvalues.professions) then return false end
	-- allow all perk, but calculate success/failure risk
--	if not VehicleUtils.testPerks(chr, keyvalues.skills) then return false end
	if not VehicleUtils.testRecipes(chr, keyvalues.recipes) then return false end
	if not VehicleUtils.testTraits(chr, keyvalues.traits) then return false end
	if not VehicleUtils.testItems(chr, keyvalues.items, typeToItem) then return false end
	-- if doing mechanics on this part require key but player doesn't have it, we'll check that door or windows aren't unlocked also
	if VehicleUtils.RequiredKeyNotFound(part, chr) then
		return false;
	end
	return true
end

-- Basic test + engine need to not be stopped to uninstall battery
function Vehicles.UninstallTest.Battery(vehicle, part, chr)
	local canDo = Vehicles.UninstallTest.Default(vehicle, part, chr);
	if not canDo then
		return false;
	end
	if vehicle:isEngineStarted() or vehicle:isEngineRunning() then
		print("ENGINE RUNNING YOOO")
		return false;
	end
	return true;
end

function Vehicles.UninstallTest.Default(vehicle, part, chr)
	if ISVehicleMechanics.cheat then return true; end
	local keyvalues = part:getTable("uninstall")
	if not keyvalues then return false end
	if not part:getInventoryItem() then return false end
	if not part:getItemType() or part:getItemType():isEmpty() then return false end
	local typeToItem = VehicleUtils.getItems(chr:getPlayerNum())
	if keyvalues.requireUninstalled and (vehicle:getPartById(keyvalues.requireUninstalled) and vehicle:getPartById(keyvalues.requireUninstalled):getInventoryItem()) then
		return false;
	end
	if not VehicleUtils.testProfession(chr, keyvalues.professions) then return false end
	-- allow all perk, but calculate success/failure risk
--	if not VehicleUtils.testPerks(chr, keyvalues.skills) then return false end
	if not VehicleUtils.testRecipes(chr, keyvalues.recipes) then return false end
	if not VehicleUtils.testTraits(chr, keyvalues.traits) then return false end
	if not VehicleUtils.testItems(chr, keyvalues.items, typeToItem) then return false end
	if keyvalues.requireEmpty and round(part:getContainerContentAmount(), 3) > 0 then return false end
	local seatNumber = part:getContainerSeatNumber()
	local seatOccupied = (seatNumber ~= -1) and vehicle:isSeatOccupied(seatNumber)
	if keyvalues.requireEmpty and seatOccupied then return false end
	-- if doing mechanics on this part require key but player doesn't have it, we'll check that door or windows aren't unlocked also
	if VehicleUtils.RequiredKeyNotFound(part, chr) then
		return false
	end
	return true
end

function Vehicles.InstallComplete.Door(vehicle, part)
	local item = part:getInventoryItem()
	if not item then return end
	part:getDoor():setLocked(false)
	part:getDoor():setLockBroken(item:getModData().lockBroken or false)
	vehicle:transmitPartDoor(part)
	vehicle:doDamageOverlay()
end

function Vehicles.UninstallComplete.Door(vehicle, part, item)
	if not item then return end
	item:getModData().lockBroken = part:getDoor():isLockBroken()
	vehicle:transmitPartDoor(part)
	vehicle:doDamageOverlay()
end

function Vehicles.InstallComplete.Tire(vehicle, part)
	local wheelIndex = part:getWheelIndex()
	vehicle:setTireRemoved(wheelIndex, false)
end

function Vehicles.UninstallComplete.Tire(vehicle, part, item)
	local wheelIndex = part:getWheelIndex()
	vehicle:setTireRemoved(wheelIndex, true)
end

function Vehicles.InstallComplete.Window(vehicle, part)
	if part:getWindow() then
--		part:getWindow():setHealth(100)
		vehicle:transmitPartWindow(part)
	end
	vehicle:doDamageOverlay();
end

function Vehicles.InstallComplete.Default(vehicle, part)
	vehicle:doDamageOverlay();
end

function Vehicles.UninstallComplete.Default(vehicle, part, item)
	vehicle:doDamageOverlay();
end

-----

VehicleUtils = {}

function VehicleUtils.getContainers(playerNum)
	local containers = {}
	for _,v in ipairs(getPlayerInventory(playerNum).inventoryPane.inventoryPage.backpacks) do
		table.insert(containers, v.inventory)
	end
	for _,v in ipairs(getPlayerLoot(playerNum).inventoryPane.inventoryPage.backpacks) do
		table.insert(containers, v.inventory)
	end
	return containers
end

function VehicleUtils.getItems(playerNum)
	local containers = VehicleUtils.getContainers(playerNum)
	local typeToItem = {}
	for _,container in ipairs(containers) do
		for i=1,container:getItems():size() do
			local item = container:getItems():get(i-1)
			if item:getCondition() > 0 then
				typeToItem[item:getFullType()] = typeToItem[item:getFullType()] or {}
				table.insert(typeToItem[item:getFullType()], item)
				-- This isn't needed for Radios any longer.  There was a bug setting
				-- the item type to Radio.worldSprite, but that no longer happens.
				if instanceof(item, "Moveable") and item:getWorldSprite() then
					local fullType = item:getScriptItem():getFullName()
					if fullType ~= item:getFullType() then
						typeToItem[fullType] = typeToItem[fullType] or {}
						table.insert(typeToItem[fullType], item)
					end
				end
			end
		end
	end
	return typeToItem
end

function VehicleUtils.split(string, pattern)
	local ss = string:split(pattern)
	return ss[1],ss[2],ss[3],ss[4] -- Lua unpack()
end

function VehicleUtils.testProfession(chr, professions)
	if not professions or professions == "" then return true end
	for _,profession in ipairs(professions:split(";")) do
		if chr:getDescriptor():getProfession() == profession then return true end
	end
	return false
end

function VehicleUtils.testPerks(chr, perks)
	if not perks or perks == "" then return true end
	for _,perk in ipairs(perks:split(";")) do
		local name,level = VehicleUtils.split(perk, ":")
		if chr:getPerkLevel(Perks.FromString(name)) < tonumber(level) then return false end
	end
	return true
end

function VehicleUtils.testTraits(chr, traits)
	if not traits or traits == "" then return true end
	for _,trait in ipairs(traits:split(";")) do
		if not chr:getTraits():contains(trait) then return false end
	end
	return true
end

function VehicleUtils.testRecipes(chr, recipes)
	if not recipes or recipes == "" then return true end
	for _,recipe in ipairs(recipes:split(";")) do
		if not chr:isRecipeKnown(recipe) then return false end
	end
	return true
end

function VehicleUtils.testItems(chr, items, typeToItem)
	if not items then return true end
	for _,item in pairs(items) do
		if not typeToItem[item.type] then return false end
		if item.count then
		end
	end
	return true
end

function VehicleUtils.createPartInventoryItem(part)
	if not part:getItemType() or part:getItemType():isEmpty() then return nil end
	local item;
	if not part:getInventoryItem() then
		local v = part:getVehicle();
--		if not part:isSpecificItem() then
			local chosenKey = ""
			for i=1,part:getItemType():size() do
				chosenKey = chosenKey .. part:getItemType():get(i-1) .. ';'
			end
			local itemType = v:getChoosenParts():get(chosenKey);
			-- never init this part, we choose a random part in the itemtype available, so every tire will be the same, every seats... (no 2 normal tire and 2 sports tire e.g)
			-- part quality is always in the same order, 0 = bad, max = good
			-- we random the part quality depending on the engine quality
			if not itemType then
				for i=0, part:getItemType():size() - 1 do
					if ZombRand(100) > (100 - (100/part:getItemType():size())) or i == part:getItemType():size() - 1 then
						itemType = part:getItemType():get(i);
						-- removed old brake
						itemType = itemType:gsub("Base.OldBrake", "Base.NormalBrake");
						v:getChoosenParts():put(chosenKey, itemType);
						break;
					end
				end
			end
			item = InventoryItemFactory.CreateItem(itemType);
			local conditionMultiply = 100/item:getConditionMax();
			if part:getContainerCapacity() and part:getContainerCapacity() > 0 then
				item:setMaxCapacity(part:getContainerCapacity());
			end
			item:setConditionMax(item:getConditionMax()*conditionMultiply);
			item:setCondition(item:getCondition()*conditionMultiply);
--		else
--			item = InventoryItemFactory.CreateItem(part:getItemType():get(0));
--		end
--		if not item then return; end
		part:setRandomCondition(item);
		part:setInventoryItem(item)
	end
	return part:getInventoryItem()
end

function VehicleUtils.getInsideTemperature(player)
	local vehicle = player:getVehicle()
	if not vehicle then return 0.0 end
	return vehicle:getInsideTemperature();
	--local part = vehicle:getPartById("PassengerCompartment")
	--if not part then return 0.0 end
	--return (part:getModData().temperature or 0.0) + (part:getModData().windowtemperature or 0.0);
end

function VehicleUtils.compareFloats(a, b, precision)
	if (a == 0.0) ~= (b == 0.0) then return true end
	if (a == 1.0) ~= (b == 1.0) then return true end
	return round(a, precision) ~= round(b, precision)
end

-- Give a chance to lower condition upon uninstalling a vehicle part (depend on required skill && player's mechanic skill)
function VehicleUtils.lowerUninstalledItemCondition(part, item, mechanicSkill, chr)
	--	item:setCondition(item:getCondition() - 1);
	local tbl = part:getTable("uninstall")
	if tbl and tbl.skills and tbl.skills ~= "" then
		for _,perk in ipairs(luautils.split(tbl.skills, ";")) do
			local name,level = VehicleUtils.split(perk, ":")
			if mechanicSkill < 7 then
				local diff = 10 - (mechanicSkill - level);
				if ZombRand(10) < (diff/2) then
					item:setCondition(math.max(item:getCondition() - 1, 0));
					chr:playSound("PZ_MetalSnap");
				end
			end
		end
	end
end

function VehicleUtils.chargeBattery(vehicle, delta)
	local battery = vehicle:getBattery()
	if not battery then return end
	if not battery:getInventoryItem() then return end
	local chargeOld = battery:getInventoryItem():getUsedDelta()
	local charge = chargeOld
	charge = math.max(charge + delta, 0.0)
	charge = math.min(charge + delta, 1.0)
	if charge ~= chargeOld then
		battery:getInventoryItem():setUsedDelta(charge)
		if VehicleUtils.compareFloats(chargeOld, charge, 2) then
			vehicle:transmitPartUsedDelta(battery)
		end
	end
end

function VehicleUtils.getChildWindow(part)
	if not part then return nil end
	return part:getChildWindow()
end

function VehicleUtils.callLua(functionName, arg1, arg2, arg3, arg4)
	if functionName:find(".") then
		local t = _G
		local ss = functionName:split("\\.")
		for i=1,#ss-1 do
			t = t[ss[i]]
		end
		local func = t[ss[#ss]]
		return func(arg1, arg2, arg3, arg4)
	else
		return _G[functionName](arg1, arg2, arg3, arg4)
	end
end

function VehicleUtils.OnUseVehicle(character, vehicle, pressedNotTapped)
	pressedNotTapped = false;
	if character:getVehicle() == vehicle then
		if pressedNotTapped then
			if vehicle:isDriver(character) and vehicle:isEngineWorking() then
				if vehicle:isEngineRunning() then
					ISVehicleMenu.onShutOff(character)
				else
					ISVehicleMenu.onStartEngine(character)
				end
			end
		else
			ISVehicleMenu.onExit(character)
		end
	else
		local part = vehicle:getUseablePart(character)
		if part then
			VehicleUtils.callLua(part:getLuaFunction("use"), vehicle, part, character)
			return
		end
		local seat = vehicle:getBestSeat(character)
		if seat == -1 then return end
		ISVehicleMenu.onEnter(character, vehicle, seat)
	end
end

function VehicleUtils.RemoveTire(part, explosion)
	part:setInventoryItem(nil);
	part:getVehicle():transmitPartItem(part);
	part:setModelVisible("InflatedTirePlusWheel", false);
	part:getVehicle():setTireRemoved(part:getWheelIndex(), true);
	if explosion then
		part:getVehicle():playSound("VehicleTireExplode");
	end
end

function VehicleUtils.OnVehicleHorn(character, vehicle, pressed)
	print("OnVehicleHorn pressed=" .. tostring(pressed))
end

function VehicleUtils.CheckForUnlockedDoorsWindows(vehicle)
	for i=0,vehicle:getPartCount() do
		local part = vehicle:getPartByIndex(i);
		if not part then return false; end
		if part:getId() ~= "EngineDoor" and part:getDoor() and (part:getDoor():isOpen() or not part:getDoor():isLocked() or not part:getInventoryItem()) then return true; end
		if part:getWindow() and (part:getWindow():isOpen() or part:getWindow():isDestroyed() or not part:getInventoryItem()) then return true; end
	end
	return false;
end

function VehicleUtils.RequiredKeyNotFound(part, chr)
	if not part then return false end
	local vehicle = part:getVehicle()
	if not part:getScriptPart():isMechanicRequireKey() then return false end
	if SandboxVars.VehicleEasyUse then return false end
	if ISVehicleMechanics.cheat then return false end
	if VehicleUtils.CheckForUnlockedDoorsWindows(vehicle) then return false end
	if chr and chr:getInventory():haveThisKeyId(vehicle:getKeyId()) then return false end
	return true
end

function VehicleUtils.anyWorkingHeadlights(vehicle)
	if vehicle:getBatteryCharge() <= 0 then return false end
	local frontLeft = vehicle:getPartById("HeadlightLeft")
	if frontLeft and frontLeft:getLight() and frontLeft:getInventoryItem() then return true end
	local frontRight = vehicle:getPartById("HeadlightRight")
	if frontRight and frontRight:getLight() and frontRight:getInventoryItem() then return true end
	local rearLeft = vehicle:getPartById("HeadlightRearLeft")
	if rearLeft and rearLeft:getInventoryItem() then return true end
	local rearRight = vehicle:getPartById("HeadlightRearRight")
	if rearRight and rearRight:getInventoryItem() then return true end
	return false
end

function VehicleUtils.UninstallPart(part, chr)
	local item = part:getInventoryItem();
--	VehicleUtils.lowerUninstalledItemCondition(part, item, chr:getPerkLevel(Perks.Mechanics), chr);
	local keyvalues = part:getTable("uninstall");
	local perks = keyvalues.skills;
	local success, failure = VehicleUtils.calculateInstallationSuccess(perks, chr);
	local fail = false;
	if ISVehicleMechanics.cheat then success = 100; failure = 0; end
	if ZombRand(100) < success then
		chr:addMechanicsItem(part:getInventoryItem():getID() .. part:getVehicle():getMechanicalID() .. "0", part, getGameTime():getCalender():getTimeInMillis());
		local content = part:getContainerContentAmount();
		part:setInventoryItem(nil);
		chr:getInventory():AddItem(item);
		item:setItemCapacity(content);
		if keyvalues and keyvalues.complete then
			VehicleUtils.callLua(keyvalues.complete, part:getVehicle(), part, item);
		end
	elseif ZombRand(100) < failure and part:getCondition() > 1 then
		part:setCondition(part:getCondition() - ZombRand(5,10));
		chr:playSound("PZ_MetalSnap", false);
		fail = true;
	else
		fail = true;
	end
	local ui = getPlayerMechanicsUI(0);
	if ui and ui:isReallyVisible() then
		if fail then ui:startFlashRed();
		else ui:startFlashGreen(); end
	end
end

function VehicleUtils.InstallPart(part, item, chr)
	local keyvalues = part:getTable("install");
	local perks = keyvalues.skills;
	local success, failure = VehicleUtils.calculateInstallationSuccess(perks, chr);
	local fail = false;
	if ISVehicleMechanics.cheat then success = 100; failure = 0; end
	if ZombRand(100) < success then
		part:setInventoryItem(item, chr:getPerkLevel(Perks.Mechanics))
		chr:addMechanicsItem(part:getInventoryItem():getID() .. part:getVehicle():getMechanicalID() .. "1", part, getGameTime():getCalender():getTimeInMillis());
		if keyvalues and keyvalues.complete then
			VehicleUtils.callLua(keyvalues.complete, part:getVehicle(), part)
		end
	elseif ZombRand(100) < failure and item:getCondition() > 1 then
		item:setCondition(item:getCondition() - ZombRand(5,10));
		chr:playSound("PZ_MetalSnap", false);
		chr:getInventory():AddItem(item);
		fail = true;
	else
		chr:getInventory():AddItem(item);
		fail = true;
	end
	local ui = getPlayerMechanicsUI(0);
	if ui and ui:isReallyVisible() then
		if fail then ui:startFlashRed();
		else ui:startFlashGreen(); end
	end
end

function VehicleUtils.calculateInstallationSuccess(perks, chr, chrPerks)
	local success = 100;
	local failure = 0;
--	local perkLvl = tonumber(chr:getPerkLevel(Perks.FromString(name)));
--	if perkLvl < 8 then
--		success = 95;
--		failure = 0;
--	end
	if perks and perks ~= "" then
		for _,perk in ipairs(perks:split(";")) do
			local name,level = VehicleUtils.split(perk, ":")
			local level = tonumber(level)
			local perkLvl = chrPerks and chrPerks[name] or chr:getPerkLevel(Perks.FromString(name))
			if perkLvl < level then
				success = success - (20 + (level - perkLvl) * 15);
				failure = failure + (20 + (level - perkLvl) * 15);
			end
		end
	end
	success = math.min(math.max(success, 0), 100)
	failure = math.min(math.max(failure, 0), 100)
	return success, failure;
end

function VehicleUtils.getPerksTableForChr(perks, chr)
	local ret = {}
	if perks and perks ~= "" then
		for _,perk in ipairs(perks:split(";")) do
			local name,level = VehicleUtils.split(perk, ":")
			ret[name] = chr:getPerkLevel(Perks.FromString(name))
		end
	end
	return ret
end

LuaEventManager.AddEvent("OnUseVehicle")
LuaEventManager.AddEvent("OnVehicleHorn")
Events.OnUseVehicle.Add(VehicleUtils.OnUseVehicle)
Events.OnVehicleHorn.Add(VehicleUtils.OnVehicleHorn)

