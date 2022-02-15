if debugScenarios == nil then
	debugScenarios = {}
end


debugScenarios.Trailer3Scenario_Building = {
	name = "LIFE 3 - FINAL HOUSE SURVIVE",
--	forceLaunch = true, -- use this to force the launch of THIS scenario right after main menu was loaded, save more clicks! Don't do multiple scenarii with this options
	startLoc = {x=14322, y=4969, z=0 },
	setSandbox = function()
		
		SandboxVars.Speed = 3;
		SandboxVars.Zombies = 6;
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
		SandboxVars.CarSpawnRate = 1;
		SandboxVars.LockedCar = 3;
		SandboxVars.CarAlarm = 2;
		SandboxVars.ChanceHasGas = 1;
		SandboxVars.InitialGas = 2;
		SandboxVars.CarGeneralCondition = 1;
		SandboxVars.RecentlySurvivorVehicles = 1;
		SandboxVars.SurvivorHouseChance = 1;
		SandboxVars.VehicleStoryChance = 1;
		
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
		
		-- car
		local car = addVehicleDebug("Base.SUV", IsoDirections.S, nil, getCell():getGridSquare(14332, 4963, 0));
		car:repair();
		car:getPartById("EngineDoor"):setCondition(10);
		car:getPartById("DoorFrontLeft"):setCondition(10);
		car:getPartById("DoorRearRight"):setCondition(10);
		car:setColor(0.8, 0, 0.02);
		inv:AddItem(car:createVehicleKey());
		
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
	
		-- construction materials
		chr:getInventory():AddItem("Base.Generator");
		chr:getInventory():AddItem("Base.Hammer");
		chr:getInventory():AddItem("Base.Nails");chr:getInventory():AddItem("Base.Nails");
		chr:getInventory():AddItem("farming.HandShovel");
		chr:getInventory():AddItem("farming.WateredCanFull");
		chr:getInventory():AddItem("farming.CarrotBagSeed");
		chr:getInventory():AddItem("Base.Saw");
--		chr:getInventory():AddItem("Base.EmptyPetrolCan");chr:getInventory():AddItem("Base.EmptyPetrolCan");
		
		getPlayer():LevelPerk(Perks.Woodwork);getPlayer():LevelPerk(Perks.Woodwork);getPlayer():LevelPerk(Perks.Woodwork);getPlayer():LevelPerk(Perks.Woodwork);getPlayer():LevelPerk(Perks.Woodwork);getPlayer():LevelPerk(Perks.Woodwork);
		getPlayer():LevelPerk(Perks.Farming);getPlayer():LevelPerk(Perks.Farming);getPlayer():LevelPerk(Perks.Farming);getPlayer():LevelPerk(Perks.Farming);getPlayer():LevelPerk(Perks.Farming);getPlayer():LevelPerk(Perks.Farming);
		
		chr:getKnownRecipes():add("Generator");
		
--		wpn = inv:AddItem("Base.Sledgehammer");
--		chr:setPrimaryHandItem(wpn);
--		chr:setSecondaryHandItem(wpn);
	
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
		
		-- gonna remove items we don't need (fences/bushes..)
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14328,4967,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14328,4966,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14328,4965,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14328,4964,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14328,4963,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14328,4962,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14328,4961,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14328,4960,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14328,4959,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14328,4958,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14328,4957,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14328,4956,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14328,4955,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14328,4954,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14328,4953,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14328,4952,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14327,4967,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14326,4967,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14325,4967,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14324,4967,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14323,4967,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14322,4967,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14321,4967,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14320,4967,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14319,4967,0);
		debugScenarios.Trailer3Scenario_Building.removeItemsButFloor(14318,4967,0);
		
		-- add our south side walled part
		-- west wall
		debugScenarios.Trailer3Scenario_Building.addWall(14318,4962,0, "walls_exterior_wooden_01_40", false);
		debugScenarios.Trailer3Scenario_Building.addWall(14318,4963,0, "walls_exterior_wooden_01_40", false);
		debugScenarios.Trailer3Scenario_Building.addWall(14318,4964,0, "walls_exterior_wooden_01_40", false);
		debugScenarios.Trailer3Scenario_Building.addWall(14318,4965,0, "walls_exterior_wooden_01_40", false);
		debugScenarios.Trailer3Scenario_Building.addWall(14318,4966,0, "walls_exterior_wooden_01_40", false);
		-- north + door
		debugScenarios.Trailer3Scenario_Building.addWall(14318,4967,0, "walls_exterior_wooden_01_41", true);
		debugScenarios.Trailer3Scenario_Building.addWall(14319,4967,0, "walls_exterior_wooden_01_41", true);
		debugScenarios.Trailer3Scenario_Building.addItem(14320,4967,0, "walls_exterior_wooden_01_51");
		debugScenarios.Trailer3Scenario_Building.addDoor(14320,4967,0, "carpentry_01_53", "carpentry_01_55", true);
		debugScenarios.Trailer3Scenario_Building.addWall(14321,4967,0, "walls_exterior_wooden_01_25", true);
		debugScenarios.Trailer3Scenario_Building.addWall(14322,4967,0, "walls_exterior_wooden_01_41", true);
		debugScenarios.Trailer3Scenario_Building.addWall(14323,4967,0, "walls_exterior_wooden_01_41", true);
		debugScenarios.Trailer3Scenario_Building.addWall(14324,4967,0, "walls_exterior_wooden_01_25", true);
		debugScenarios.Trailer3Scenario_Building.addWall(14325,4967,0, "walls_exterior_wooden_01_41", true);
		debugScenarios.Trailer3Scenario_Building.addWall(14326,4967,0, "walls_exterior_wooden_01_27", true);
		-- east wall
		debugScenarios.Trailer3Scenario_Building.addWall(14326,4966,0, "walls_exterior_wooden_01_40", false);
		debugScenarios.Trailer3Scenario_Building.addWall(14326,4965,0, "walls_exterior_wooden_01_40", false);
		debugScenarios.Trailer3Scenario_Building.addWallFrame(14326,4964,0, "carpentry_02_100", false);
		debugScenarios.Trailer3Scenario_Building.addWallFrame(14326,4960,0, "carpentry_02_100", false);
		-- fences on top
		debugScenarios.Trailer3Scenario_Building.addFence(14318,4962,1, "carpentry_02_48", false);
		debugScenarios.Trailer3Scenario_Building.addFence(14318,4963,1, "carpentry_02_48", false);
		debugScenarios.Trailer3Scenario_Building.addFence(14318,4964,1, "carpentry_02_48", false);
		debugScenarios.Trailer3Scenario_Building.addFence(14318,4965,1, "carpentry_02_48", false);
		debugScenarios.Trailer3Scenario_Building.addFence(14318,4966,1, "carpentry_02_48", false);
		debugScenarios.Trailer3Scenario_Building.addFence(14318,4967,1, "carpentry_02_49", true);
		debugScenarios.Trailer3Scenario_Building.addFence(14319,4967,1, "carpentry_02_49", true);
		debugScenarios.Trailer3Scenario_Building.addFence(14320,4967,1, "carpentry_02_49", true);
		debugScenarios.Trailer3Scenario_Building.addFence(14321,4967,1, "carpentry_02_49", true);
		debugScenarios.Trailer3Scenario_Building.addFence(14322,4967,1, "carpentry_02_49", true);
		debugScenarios.Trailer3Scenario_Building.addFence(14323,4967,1, "carpentry_02_49", true);
		debugScenarios.Trailer3Scenario_Building.addFence(14324,4967,1, "carpentry_02_49", true);
		debugScenarios.Trailer3Scenario_Building.addFence(14325,4967,1, "carpentry_02_49", true);
		debugScenarios.Trailer3Scenario_Building.addFence(14326,4966,1, "carpentry_02_48", false);
		debugScenarios.Trailer3Scenario_Building.addFence(14326,4965,1, "carpentry_02_48", false);
		debugScenarios.Trailer3Scenario_Building.addFence(14326,4964,1, "carpentry_02_48", false);
		debugScenarios.Trailer3Scenario_Building.addFence(14326,4963,1, "carpentry_02_48", false);
		debugScenarios.Trailer3Scenario_Building.addFence(14326,4962,1, "carpentry_02_48", false);
		debugScenarios.Trailer3Scenario_Building.addFence(14326,4961,1, "carpentry_02_48", false);
		debugScenarios.Trailer3Scenario_Building.addFence(14326,4960,1, "carpentry_02_48", false);
		-- stairs
		debugScenarios.Trailer3Scenario_Building.addStairs(14318,4965,0, true);
		-- sandbags
		debugScenarios.Trailer3Scenario_Building.addFence(14318,4967,0, "carpentry_02_12", false);
		debugScenarios.Trailer3Scenario_Building.addFence(14318,4968,0, "carpentry_02_12", false);
		debugScenarios.Trailer3Scenario_Building.addFence(14318,4969,0, "carpentry_02_12", false);
		debugScenarios.Trailer3Scenario_Building.addFence(14319,4970,0, "carpentry_02_13", true);
		debugScenarios.Trailer3Scenario_Building.addFence(14320,4970,0, "carpentry_02_13", true);
		debugScenarios.Trailer3Scenario_Building.addFence(14321,4970,0, "carpentry_02_13", true);
		debugScenarios.Trailer3Scenario_Building.addFence(14322,4970,0, "carpentry_02_13", true);
		debugScenarios.Trailer3Scenario_Building.addFence(14323,4967,0, "carpentry_02_12", false);
		debugScenarios.Trailer3Scenario_Building.addFence(14323,4968,0, "carpentry_02_12", false);
		debugScenarios.Trailer3Scenario_Building.addFence(14323,4969,0, "carpentry_02_12", false);
		-- top floor decoration
		debugScenarios.Trailer3Scenario_Building.addItem(14321,4962,1, "carpentry_01_19");
		debugScenarios.Trailer3Scenario_Building.addItem(14320,4964,1, "furniture_seating_outdoor_01_17");
		debugScenarios.Trailer3Scenario_Building.addItem(14321,4964,1, "carpentry_02_9");
		debugScenarios.Trailer3Scenario_Building.addItem(14322,4964,1, "furniture_seating_outdoor_01_17");
		debugScenarios.Trailer3Scenario_Building.addItem(14324,4967,1, "crafted_01_1");
		debugScenarios.Trailer3Scenario_Building.addItem(14324,4967,0, "crafted_01_9");
		
		-- metal walls
		debugScenarios.Trailer3Scenario_Building.addWall(14326,4954,0, "constructedobjects_01_49", true);
		debugScenarios.Trailer3Scenario_Building.addWall(14327,4954,0, "constructedobjects_01_65", true);
		debugScenarios.Trailer3Scenario_Building.addFence(14328,4953,0, "constructedobjects_01_72", false);
		debugScenarios.Trailer3Scenario_Building.addFence(14328,4952,0, "constructedobjects_01_56", false);
		debugScenarios.Trailer3Scenario_Building.addWall(14327,4952,0, "constructedobjects_01_65", true);
		debugScenarios.Trailer3Scenario_Building.addWall(14326,4952,0, "constructedobjects_01_65", true);
		debugScenarios.Trailer3Scenario_Building.addWall(14325,4952,0, "constructedobjects_01_65", true);
		debugScenarios.Trailer3Scenario_Building.addDoor(14324,4952,0, "fixtures_doors_fences_01_25", "fixtures_doors_fences_01_27", true);
		debugScenarios.Trailer3Scenario_Building.addWall(14323,4952,0, "constructedobjects_01_65", true);
		debugScenarios.Trailer3Scenario_Building.addWall(14323,4952,0, "constructedobjects_01_64", false);
		debugScenarios.Trailer3Scenario_Building.addWall(14323,4953,0, "constructedobjects_01_64", false);
	
		-- skull sign
		debugScenarios.Trailer3Scenario_Building.addItem(14316,4972,0, "constructedobjects_signs_01_11");
		debugScenarios.Trailer3Scenario_Building.addItem(14316,4972,0, "constructedobjects_signs_01_36");
		
		-- wood material near non finished hole
		local sq = debugScenarios.Trailer3Scenario_Building.getSQ(14326, 4960, 0);
		sq:AddWorldInventoryItem("Base.Plank", 0.5, 0.5, 0); sq:AddWorldInventoryItem("Base.Plank", 0.5, 0.5, 0.02);sq:AddWorldInventoryItem("Base.Plank", 0.5, 0.5, 0.04);sq:AddWorldInventoryItem("Base.Plank", 0.5, 0.5, 0.06);
		sq:AddWorldInventoryItem("Base.Nails", 0.2, 0.3, 0); sq:AddWorldInventoryItem("Base.Nails", 0.4, 0.1, 0);sq:AddWorldInventoryItem("Base.Nails", 0.2, 0.1, 0);
	
		-- logs to saw
		local sq = debugScenarios.Trailer3Scenario_Building.getSQ(14326, 4974, 0);
		sq:AddWorldInventoryItem("Base.Log", 0, 0, 0);sq:AddWorldInventoryItem("Base.Log", 0.3, 0.3, 0);sq:AddWorldInventoryItem("Base.Log", 0.8, 0.1, 0);
		sq = debugScenarios.Trailer3Scenario_Building.getSQ(14327, 4976, 0);
		sq:AddWorldInventoryItem("Base.Log", 0, 0, 0);sq:AddWorldInventoryItem("Base.Log", 0.3, 0.3, 0);sq:AddWorldInventoryItem("Base.Log", 0.8, 0.1, 0);
		
		-- farming
		-- tomatoes
		debugScenarios.Trailer3Scenario_Building.addItem(14308,4956,0, "vegetation_farming_01_67"); debugScenarios.Trailer3Scenario_Building.addItem(14309,4956,0, "vegetation_farming_01_68");debugScenarios.Trailer3Scenario_Building.addItem(14310,4956,0, "vegetation_farming_01_67");
		debugScenarios.Trailer3Scenario_Building.addItem(14308,4957,0, "vegetation_farming_01_66"); debugScenarios.Trailer3Scenario_Building.addItem(14309,4957,0, "vegetation_farming_01_67");debugScenarios.Trailer3Scenario_Building.addItem(14310,4957,0, "vegetation_farming_01_68");
		-- potatoes
		debugScenarios.Trailer3Scenario_Building.addItem(14308,4959,0, "vegetation_farming_01_42"); debugScenarios.Trailer3Scenario_Building.addItem(14309,4959,0, "vegetation_farming_01_43");debugScenarios.Trailer3Scenario_Building.addItem(14310,4959,0, "vegetation_farming_01_43");
		debugScenarios.Trailer3Scenario_Building.addItem(14308,4960,0, "vegetation_farming_01_43"); debugScenarios.Trailer3Scenario_Building.addItem(14309,4960,0, "vegetation_farming_01_44");debugScenarios.Trailer3Scenario_Building.addItem(14310,4960,0, "vegetation_farming_01_43");
		-- salad
		debugScenarios.Trailer3Scenario_Building.addItem(14308,4962,0, "vegetation_farming_01_20"); debugScenarios.Trailer3Scenario_Building.addItem(14309,4962,0, "vegetation_farming_01_19");debugScenarios.Trailer3Scenario_Building.addItem(14310,4962,0, "vegetation_farming_01_20");
		-- empty
		debugScenarios.Trailer3Scenario_Building.addItem(14308,4963,0, "vegetation_farming_01_1");
		
		-- gathering water
		debugScenarios.Trailer3Scenario_Building.addItem(14326,4957,0, "carpentry_02_53");
		debugScenarios.Trailer3Scenario_Building.addItem(14326,4958,0, "carpentry_02_55");
		sq = debugScenarios.Trailer3Scenario_Building.getSQ(14327, 4956, 0);
		sq:AddWorldInventoryItem("Base.WaterSaucepan", 0, 0, 0);sq:AddWorldInventoryItem("Base.WaterSaucepan", 0.6, 0.6, 0);
	end,
	
	onLoadGS = function(sq)
		-- hack for floor cause the event throwing bug here somehow :/
		if not debugScenarios.Trailer3Scenario_Building.addedFloor and sq:getX() == 14325 and sq:getY() == 4966 then
			debugScenarios.Trailer3Scenario_Building.addedFloor = true;
			-- floor on top
			debugScenarios.Trailer3Scenario_Building.addFloor(14318, 4966, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14319, 4962, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14319, 4962, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14319, 4963, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14319, 4964, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14319, 4965, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14319, 4966, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14320, 4962, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14320, 4962, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14320, 4963, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14320, 4964, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14320, 4965, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14320, 4966, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14321, 4962, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14321, 4962, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14321, 4963, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14321, 4964, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14321, 4965, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14321, 4966, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14322, 4960, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14322, 4961, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14322, 4962, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14322, 4962, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14322, 4963, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14322, 4964, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14322, 4965, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14322, 4966, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14323, 4960, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14323, 4961, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14323, 4962, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14323, 4962, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14323, 4963, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14323, 4964, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14323, 4965, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14323, 4966, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14324, 4960, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14324, 4961, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14324, 4962, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14324, 4962, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14324, 4963, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14324, 4964, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14324, 4965, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14324, 4966, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14325, 4960, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14325, 4961, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14325, 4962, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14325, 4962, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14325, 4963, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14325, 4964, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14325, 4965, 1, "carpentry_02_57");
			debugScenarios.Trailer3Scenario_Building.addFloor(14325, 4966, 1, "carpentry_02_57");
			
			debugScenarios.Trailer3Scenario_Building.addFloor(14323, 4953, 1, "constructedobjects_01_86");
			debugScenarios.Trailer3Scenario_Building.addFloor(14324, 4953, 1, "constructedobjects_01_86");
			debugScenarios.Trailer3Scenario_Building.addFloor(14325, 4953, 1, "constructedobjects_01_86");
			debugScenarios.Trailer3Scenario_Building.addFloor(14326, 4953, 1, "constructedobjects_01_86");
			debugScenarios.Trailer3Scenario_Building.addFloor(14327, 4953, 1, "constructedobjects_01_86");
			debugScenarios.Trailer3Scenario_Building.addFloor(14323, 4952, 1, "constructedobjects_01_86");
			debugScenarios.Trailer3Scenario_Building.addFloor(14324, 4952, 1, "constructedobjects_01_86");
			debugScenarios.Trailer3Scenario_Building.addFloor(14325, 4952, 1, "constructedobjects_01_86");
			debugScenarios.Trailer3Scenario_Building.addFloor(14326, 4952, 1, "constructedobjects_01_86");
			debugScenarios.Trailer3Scenario_Building.addFloor(14327, 4952, 1, "constructedobjects_01_86");
		end
		
		if sq:getWindow() and sq:getX() == 14326 and sq:getY() == 4956 and sq:getZ() == 0 then
			local window = sq:getWindow();
			window:addBarricadesDebug(4, false);
		end
	end

}

debugScenarios.Trailer3Scenario_Building.addFloor = function(x, y, z, tile)
	local sq = debugScenarios.Trailer3Scenario_Building.getSQ(x, y, z);
	sq:addFloor(tile);
end

debugScenarios.Trailer3Scenario_Building.addStairs = function(x, y, z, north)
	local stairs = ISWoodenStairs:new("carpentry_02_88", "carpentry_02_89", "carpentry_02_90", "carpentry_02_96", "carpentry_02_97", "carpentry_02_98", "carpentry_02_94", "carpentry_02_95");
	local sprite = "carpentry_02_88";
	if north then
		sprite = "carpentry_02_96";
	end
	stairs:create(x, y, z, north, sprite)
end

debugScenarios.Trailer3Scenario_Building.addFence = function(x, y, z, tile, north)
	local sq = debugScenarios.Trailer3Scenario_Building.getSQ(x, y, z);
	local thump = IsoThumpable.new(getCell(), sq, tile, nil, north, nil);
	thump:setIsHoppable(true);
	sq:AddSpecialObject(thump);
end

debugScenarios.Trailer3Scenario_Building.addWallFrame = function(x, y, z, tile, north)
	local sq = debugScenarios.Trailer3Scenario_Building.getSQ(x, y, z);
	local thump = IsoThumpable.new(getCell(), sq, tile, nil, north, nil);
	thump:setIsHoppable(true);
	thump:setName("WoodenWallFrame");
	sq:AddSpecialObject(thump);
end

debugScenarios.Trailer3Scenario_Building.addWall = function(x, y, z, tile, north)
	local sq = debugScenarios.Trailer3Scenario_Building.getSQ(x, y, z);
	local thump = IsoThumpable.new(getCell(), sq, tile, nil, north, nil);
	thump:setCanPassThrough(false);
	sq:AddSpecialObject(thump);
end

debugScenarios.Trailer3Scenario_Building.addDoor = function(x, y, z, tile, openTile, north)
	local sq = debugScenarios.Trailer3Scenario_Building.getSQ(x, y, z);
	local thump = IsoThumpable.new(getCell(), sq, tile, openTile, north, nil);
	thump:setIsDoor(true);
	sq:AddSpecialObject(thump);
end

debugScenarios.Trailer3Scenario_Building.addItem = function(x, y, z, tile)
	local sq = debugScenarios.Trailer3Scenario_Building.getSQ(x, y, z);
	sq:AddTileObject(IsoObject.new(sq, tile));
end

debugScenarios.Trailer3Scenario_Building.getSQ = function(x, y, z)
	local sq = getCell():getGridSquare(x, y, z);
	if not sq then
		sq = IsoGridSquare.new(getCell(), nil, x, y, z);
		getCell():ConnectNewSquare(sq, true);
	end
	return sq;
end

debugScenarios.Trailer3Scenario_Building.removeItemsButFloor = function(x, y, z)
	local sq = getCell():getGridSquare(x, y, z);
	local toRemove = {};
	for i=0,sq:getObjects():size()-1 do
		local item = sq:getObjects():get(i);
		if item ~= sq:getFloor() then
			table.insert(toRemove, item);
		end
	end
	for i,v in ipairs(toRemove) do
		sq:RemoveTileObjectErosionNoRecalc(v);
	end
end
