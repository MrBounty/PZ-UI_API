if debugScenarios == nil then
	debugScenarios = {}
end


debugScenarios.Trailer3Scenario_GasStation = {
	name = "LIFE 3 - GARAGE",
--	forceLaunch = true, -- use this to force the launch of THIS scenario right after main menu was loaded, save more clicks! Don't do multiple scenarii with this options
	startLoc = {x=13758, y=5665, z=0 },
	setSandbox = function()
		
		SandboxVars.Speed = 3;
		SandboxVars.Zombies = 5;
		SandboxVars.Distribution = 1;
		SandboxVars.Survivors = 1;
		SandboxVars.DayLength = 3;
		SandboxVars.StartYear = 1;
		SandboxVars.StartMonth = 7;
		SandboxVars.StartDay = 9;
		SandboxVars.StartTime = 3;
		SandboxVars.VehicleEasyUse = true;
		SandboxVars.WaterShutModifier = 30;
		SandboxVars.ElecShutModifier = 30;
		SandboxVars.WaterShut = 2;
		SandboxVars.ElecShut = 2;
		SandboxVars.FoodLoot = 2;
		SandboxVars.WeaponLoot = 2;
		SandboxVars.OtherLoot = 2;
		SandboxVars.Temperature = 3;
		SandboxVars.Rain = 3;
		SandboxVars.ErosionSpeed = 3;
		SandboxVars.XpMultiplier = 1.0;
		SandboxVars.StatsDecrease = 3;
		SandboxVars.NatureAbundance = 3;
		SandboxVars.Alarm = 1;
		SandboxVars.LockedHouses = 6;
		SandboxVars.SurvivorHouseChance = 1;
		SandboxVars.VehicleStoryChance = 1;
		SandboxVars.FoodRotSpeed = 3;
		SandboxVars.FridgeFactor = 3;
		SandboxVars.Farming = 3;
		SandboxVars.LootRespawn = 1;
		SandboxVars.StarterKit = false;
		SandboxVars.Nutrition = true;
		SandboxVars.TimeSinceApo = 1;
		SandboxVars.PlantResilience = 3;
		SandboxVars.PlantAbundance = 3;
		SandboxVars.EndRegen = 3;
		SandboxVars.CarSpawnRate = 3;
		SandboxVars.LockedCar = 3;
		SandboxVars.CarAlarm = 2;
		SandboxVars.ChanceHasGas = 1;
		SandboxVars.InitialGas = 2;
		SandboxVars.CarGeneralCondition = 1;
		SandboxVars.RecentlySurvivorVehicles = 1;
		
		SandboxVars.ZombieLore = {
			Speed = 2,
			Strength = 2,
			Toughness = 2,
			Transmission = 1,
			Mortality = 5,
			Reanimate = 3,
			Cognition = 3,
			Memory = 2,
			Decomp = 1,
			Sight = 3,
			Hearing = 3,
			Smell = 2,
			ThumpNoChasing = 0,
		}
	end,
	onStart = function()
		local chr = getPlayer();
		local inv = chr:getInventory();
		local visual = chr:getHumanVisual();
		chr:setFemale(false);
		chr:getDescriptor():setFemale(false);
	
--		local immutableColor = ImmutableColor.new(color.r, color.g, color.b, 1)
--		desc:getHumanVisual():setHairColor(immutableColor)
--		desc:getHumanVisual():setBeardColor(immutableColor)
		
		-- reset
		chr:clearWornItems();
		chr:getInventory():clear();
		
		-- chr visual
		visual:setBeardModel("Long");
		visual:setHairModel("Messy");
		local immutableColor = ImmutableColor.new(0.105, 0.09, 0.086, 1)
		visual:setHairColor(immutableColor)
		visual:setBeardColor(immutableColor)
		visual:setSkinTextureIndex(2);
		chr:resetModel();
		
		chr:setGhostMode(true);
	
		-- wpn
		wpn = inv:AddItem("Base.Axe");
		chr:setPrimaryHandItem(wpn);
		chr:setSecondaryHandItem(wpn);
		holster = inv:AddItem("Base.HolsterDouble");
		chr:setWornItem(holster:getBodyLocation(), holster);
		wpn = inv:AddItem("Base.Revolver_Long");
		
		chr:setAttachedItem("Holster Left", wpn);
		wpn:setAttachedSlot(2);
		wpn:setAttachedSlotType("HolsterLeft");
		wpn:setAttachedToModel("Holster Left");
		
--		inv:AddItem("Base.EmptyPetrolCan");
	
		-- clothing
		clothe = inv:AddItem("Base.Tshirt_DefaultTEXTURE_TINT");
		local color = ImmutableColor.new(0.25, 0.36, 0.36, 1)
		clothe:getVisual():setTint(color);
		chr:setWornItem(clothe:getBodyLocation(), clothe);
		clothe = inv:AddItem("Base.Jacket_Black");
		chr:setWornItem(clothe:getBodyLocation(), clothe);
		clothe = inv:AddItem("Base.Bag_ALICEpack_Army");
		chr:setClothingItem_Back(clothe);
		clothe = inv:AddItem("Base.Trousers_CamoGreen");
		chr:setWornItem(clothe:getBodyLocation(), clothe);
		clothe = inv:AddItem("Base.Socks_Ankle");
		chr:setWornItem(clothe:getBodyLocation(), clothe);
		clothe = inv:AddItem("Base.Shoes_BlackBoots");
		chr:setWornItem(clothe:getBodyLocation(), clothe);
		clothe = inv:AddItem("Base.Gloves_LeatherGlovesBlack");
		chr:setWornItem(clothe:getBodyLocation(), clothe);
	
		-- car
		local car = addVehicleDebug("Base.SUV", IsoDirections.S, nil, getCell():getGridSquare(13760, 5669, 0));
		car:repair();
		car:getPartById("EngineDoor"):setCondition(10);
		car:getPartById("DoorFrontLeft"):setCondition(10);
		car:getPartById("DoorRearRight"):setCondition(10);
		car:getPartById("GasTank"):setContainerContentAmount(0);
		car:setColor(0.8, 0, 0.02);
		inv:AddItem(car:createVehicleKey());
	
		-- petrol on ground
		getCell():getGridSquare(13749, 5665, 0):AddWorldInventoryItem("Base.PetrolCan", 0, 0, 0);
		getCell():getGridSquare(13747, 5667, 0):AddWorldInventoryItem("Base.PetrolCan", 0, 0, 0);
		getCell():getGridSquare(13745, 5669, 0):AddWorldInventoryItem("Base.PetrolCan", 0, 0, 0);
			
		-- use a static rng map for erosion so we always have same bushes/etc.
		useStaticErosionRand(true);
	
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
	end,

	onLoadGS = function(sq)
		if not debugScenarios.Trailer3Scenario_GasStation.horde1 and sq:getX() == 13725 and sq:getY() == 5648 then
			debugScenarios.Trailer3Scenario_GasStation.horde1 = true;
			addZombiesInOutfitArea(sq:getX(), sq:getY(), sq:getX() + 20, sq:getY() + 20, 0, 30, nil, nil);
		end
		if not debugScenarios.Trailer3Scenario_GasStation.horde2 and sq:getX() == 13719 and sq:getY() == 5664 then
			debugScenarios.Trailer3Scenario_GasStation.horde2 = true;
			addZombiesInOutfitArea(sq:getX(), sq:getY(), sq:getX() + 20, sq:getY() + 20, 0, 30, nil, nil);
		end
		if not debugScenarios.Trailer3Scenario_GasStation.horde3 and sq:getX() == 13734 and sq:getY() == 5646 then
			debugScenarios.Trailer3Scenario_GasStation.horde3 = true;
			addZombiesInOutfitArea(sq:getX(), sq:getY(), sq:getX() + 7, sq:getY() + 7, 0, 5, "Gas2Go", nil);
		end
		if not debugScenarios.Trailer3Scenario_GasStation.horde3 and sq:getX() == 13734 and sq:getY() == 5646 then
			debugScenarios.Trailer3Scenario_GasStation.horde3 = true;
			addZombiesInOutfitArea(sq:getX(), sq:getY(), sq:getX() + 10, sq:getY() + 10, 0, 10, nil, nil);
		end
		if not debugScenarios.Trailer3Scenario_GasStation.horde3 and sq:getX() == 13735 and sq:getY() == 5658 then
			debugScenarios.Trailer3Scenario_GasStation.horde3 = true;
			addZombiesInOutfitArea(sq:getX(), sq:getY(), sq:getX() + 7, sq:getY() + 7, 0, 3, "Gas2Go", nil);
		end
		if not debugScenarios.Trailer3Scenario_GasStation.horde4 and sq:getX() == 13746 and sq:getY() == 5635 then
			debugScenarios.Trailer3Scenario_GasStation.horde4 = true;
			addZombiesInOutfitArea(sq:getX(), sq:getY(), sq:getX() + 15, sq:getY() + 15, 0, 30, nil, nil);
		end
		if not debugScenarios.Trailer3Scenario_GasStation.horde5 and sq:getX() == 13733 and sq:getY() == 5658 then
			debugScenarios.Trailer3Scenario_GasStation.horde5 = true;
			addZombiesInOutfitArea(sq:getX(), sq:getY(), sq:getX() + 7, sq:getY() + 10, 0, 10, "Gas2Go", nil);
		end
		if not debugScenarios.Trailer3Scenario_GasStation.horde6 and sq:getX() == 13741 and sq:getY() == 5681 then
			debugScenarios.Trailer3Scenario_GasStation.horde6 = true;
			addZombiesInOutfitArea(sq:getX(), sq:getY(), sq:getX() + 7, sq:getY() + 7, 0, 10, "Gas2Go", nil);
		end
		if not debugScenarios.Trailer3Scenario_GasStation.horde7 and sq:getX() == 13741 and sq:getY() == 5681 then
			debugScenarios.Trailer3Scenario_GasStation.horde7 = true;
			addZombiesInOutfitArea(sq:getX(), sq:getY(), sq:getX() + 10, sq:getY() + 10, 0, 20, nil, nil);
		end
		if not debugScenarios.Trailer3Scenario_GasStation.horde8 and sq:getX() == 13775 and sq:getY() == 5664 then
			debugScenarios.Trailer3Scenario_GasStation.horde8 = true;
			addZombiesInOutfitArea(sq:getX(), sq:getY(), sq:getX() + 20, sq:getY() + 20, 0, 50, nil, nil);
		end
	end
}