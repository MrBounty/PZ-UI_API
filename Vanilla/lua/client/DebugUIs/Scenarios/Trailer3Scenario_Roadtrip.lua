if debugScenarios == nil then
	debugScenarios = {}
end


debugScenarios.Trailer3Scenario_Roadtrip = {
	name = "LIFE 3 - ROADTRIP",
--	forceLaunch = true, -- use this to force the launch of THIS scenario right after main menu was loaded, save more clicks! Don't do multiple scenarii with this options
	startLoc = {x=14146, y=5318, z=0 },
	setSandbox = function()
		
		SandboxVars.Speed = 3;
		SandboxVars.Zombies = 3;
		SandboxVars.Distribution = 1;
		SandboxVars.Survivors = 1;
		SandboxVars.DayLength = 3;
		SandboxVars.StartYear = 1;
		SandboxVars.StartMonth = 7;
		SandboxVars.StartDay = 9;
		SandboxVars.StartTime = 6;
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
		SandboxVars.CarSpawnRate = 3;
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
		local car = addVehicleDebug("Base.SUV", IsoDirections.N, nil, getCell():getGridSquare(14146, 5316, 0));
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
	
		-- start thunderstorm
		clim:triggerKateBobIntroStorm(14146, 5316, 20, 0.6, 2, 180, 0.7);
		
--		setPuddles(0.3);
	end,

	onLoadGS = function(sq)
		-- crashed ambulance
		if not debugScenarios.Trailer3Scenario_Roadtrip.ambulanceSpawned and sq:getX() == 14136 and sq:getY() == 5236 then
			local car = addVehicleDebug("Base.VanAmbulance", IsoDirections.W, nil, sq);
			car:setGeneralPartCondition(0.2, 80);
			debugScenarios.Trailer3Scenario_Roadtrip.ambulanceSpawned = true;
			addZombiesInOutfitArea(14136, 5236, 14140, 5239, 0, 4, "HazardSuit", nil);
		end
		-- hordes
		if not debugScenarios.Trailer3Scenario_Roadtrip.horde1 and sq:getX() == 14133 and sq:getY() == 5247 then
			debugScenarios.Trailer3Scenario_Roadtrip.horde1 = true;
			addZombiesInOutfitArea(14133, 5247, 14140, 5252, 0, 6, nil, nil);
		end
		if not debugScenarios.Trailer3Scenario_Roadtrip.horde2 and sq:getX() == 14148 and sq:getY() == 5259 then
			debugScenarios.Trailer3Scenario_Roadtrip.horde2 = true;
			addZombiesInOutfitArea(14148, 5259, 14160, 5265, 0, 6, nil, nil);
		end
		if not debugScenarios.Trailer3Scenario_Roadtrip.horde3 and sq:getX() == 14133 and sq:getY() == 5272 then
			debugScenarios.Trailer3Scenario_Roadtrip.horde3 = true;
			addZombiesInOutfitArea(14133, 5272, 14140, 5278, 0, 6, nil, nil);
		end
		if not debugScenarios.Trailer3Scenario_Roadtrip.horde4 and sq:getX() == 14148 and sq:getY() == 5217 then
			debugScenarios.Trailer3Scenario_Roadtrip.horde4 = true;
			addZombiesInOutfitArea(14148, 5217, 14155, 5225, 0, 6, nil, nil);
		end
		if not debugScenarios.Trailer3Scenario_Roadtrip.horde5 and sq:getX() == 14132 and sq:getY() == 5198 then
			debugScenarios.Trailer3Scenario_Roadtrip.horde5 = true;
			addZombiesInOutfitArea(14132, 5198, 14145, 5205, 0, 9, nil, nil);
		end
		if not debugScenarios.Trailer3Scenario_Roadtrip.horde6 and sq:getX() == 14148 and sq:getY() == 5158 then
			debugScenarios.Trailer3Scenario_Roadtrip.horde6 = true;
			addZombiesInOutfitArea(14148, 5158, 14156, 5170, 0, 8, nil, nil);
			
			local thunder = getClimateManager():getThunderStorm();
			thunder:triggerThunderEvent(14148, 5158, true, true, true)
		end
		if not debugScenarios.Trailer3Scenario_Roadtrip.horde7 and sq:getX() == 14143 and sq:getY() == 5251 then
			debugScenarios.Trailer3Scenario_Roadtrip.horde7 = true;
			addZombiesInOutfitArea(14143, 5251, 14147, 5255, 0, 4, nil, nil);
		end
		-- police blockade
		if not debugScenarios.Trailer3Scenario_Roadtrip.police and sq:getX() == 14143 and sq:getY() == 5172 then
			debugScenarios.Trailer3Scenario_Roadtrip.police = true;
			-- police vehicle
			local car = addVehicleDebug("Base.CarLightsPolice", IsoDirections.W, nil, getCell():getGridSquare(14145, 5177, 0));
			car:repair();
			car:setHeadlightsOn(true);
			car:setLightbarLightsMode(2);
			addZombiesInOutfitArea(14141, 5173, 14150, 5179, 0, 6, "Police", nil);
			-- blockade
			getCell():getGridSquare(14142, 5179, 0):AddTileObject(IsoObject.new(sq, "construction_01_8")); getCell():getGridSquare(14143, 5179, 0):AddTileObject(IsoObject.new(sq, "construction_01_8")); getCell():getGridSquare(14144, 5179, 0):AddTileObject(IsoObject.new(sq, "construction_01_8")); getCell():getGridSquare(14145, 5179, 0):AddTileObject(IsoObject.new(sq, "construction_01_8")); getCell():getGridSquare(14146, 5179, 0):AddTileObject(IsoObject.new(sq, "construction_01_8")); getCell():getGridSquare(14147, 5179, 0):AddTileObject(IsoObject.new(sq, "construction_01_8")); getCell():getGridSquare(14148, 5179, 0):AddTileObject(IsoObject.new(sq, "construction_01_8"));
			-- bodies
			local sq = getCell():getGridSquare(14147, 5181, 0);
			createRandomDeadBody(sq, 10);addBloodSplat(sq, 15);
			local sq = getCell():getGridSquare(14146, 5183, 0);
			createRandomDeadBody(sq, 10);addBloodSplat(sq, 15);
			local sq = getCell():getGridSquare(14145, 5182, 0);
			createRandomDeadBody(sq, 10);addBloodSplat(sq, 15);
			local sq = getCell():getGridSquare(14143, 5183, 0);
			createRandomDeadBody(sq, 10);addBloodSplat(sq, 15);
			local sq = getCell():getGridSquare(14145, 5185, 0);
			createRandomDeadBody(sq, 10);addBloodSplat(sq, 15);
			local sq = getCell():getGridSquare(14147, 5184, 0);
			createRandomDeadBody(sq, 10);addBloodSplat(sq, 15);
			local thunder = getClimateManager():getThunderStorm();
			thunder:triggerThunderEvent(14144, 5177, true, true, true)
		end
	end
}
