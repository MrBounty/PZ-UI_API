if debugScenarios == nil then
	debugScenarios = {}
end


debugScenarios.Trailer3Scenario_Fishing = {
	name = "LIFE 3 - FISHING",
--	forceLaunch = true, -- use this to force the launch of THIS scenario right after main menu was loaded, save more clicks! Don't do multiple scenarii with this options
	startLoc = {x=13933, y=4563, z=0 },
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
		wpn = inv:AddItem("Base.FishingRod");
		chr:setPrimaryHandItem(wpn);
		worm = inv:AddItem("Base.Worm");
		chr:setSecondaryHandItem(worm);
		inv:AddItem("Base.Worm");inv:AddItem("Base.Worm");inv:AddItem("Base.Worm");inv:AddItem("Base.Worm");inv:AddItem("Base.Worm");inv:AddItem("Base.Worm");
--		holster = inv:AddItem("Base.HolsterDouble");
--		chr:setWornItem(holster:getBodyLocation(), holster);
--		wpn = inv:AddItem("Base.Revolver_Long");
--
--		chr:setAttachedItem("Holster Left", wpn);
--		wpn:setAttachedSlot(2);
--		wpn:setAttachedSlotType("HolsterLeft");
--		wpn:setAttachedToModel("Holster Left");
		
		getPlayer():LevelPerk(Perks.Fishing);getPlayer():LevelPerk(Perks.Fishing);getPlayer():LevelPerk(Perks.Fishing);getPlayer():LevelPerk(Perks.Fishing);getPlayer():LevelPerk(Perks.Fishing);getPlayer():LevelPerk(Perks.Fishing);getPlayer():LevelPerk(Perks.Fishing);
	
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
--		local car = addVehicleDebug("Base.SUV", IsoDirections.N, nil, getCell():getGridSquare(13930, 4568, 0));
--		car:repair();
--		car:setColor(0.8, 0, 0.02);
--		colorHue = Rand.Next(0.0f, 1.0f); // Don't really care about the hue for this one
--		colorSaturation = Rand.Next(0.0f, 0.1f);
--		colorValue = Rand.Next(0.70f, 0.8f);
--		inv:AddItem(car:createVehicleKey());
	
		-- tent/campfire
--		debugScenarios.Trailer3Scenario_Fishing.addItem(13934, 4566, 0, "camping_01_5");
--		debugScenarios.Trailer3Scenario_Fishing.addItem(13935, 4569, 0, "camping_01_2");
--		debugScenarios.Trailer3Scenario_Fishing.addItem(13936, 4569, 0, "camping_01_3");
		
		-- use a static rng map for erosion so we always have same bushes/etc.
--		useStaticErosionRand(true);
	
		-- climate
--		local clim = getClimateManager();
--		local w = clim:getWeatherPeriod();
--		if w:isRunning() then
--			clim:stopWeatherAndThunder();
--		end
		
		-- remove fog
--		local var = clim:getClimateFloat(5);
--		var:setEnableOverride(true);
--		var:setOverride(0, 1);
	end,

	onLoadGS = function(sq)
		
	end
}

debugScenarios.Trailer3Scenario_Fishing.addItem = function(x, y, z, tile)
	local sq = debugScenarios.Trailer3Scenario_Fishing.getSQ(x, y, z);
	sq:AddTileObject(IsoObject.new(sq, tile));
end

debugScenarios.Trailer3Scenario_Fishing.getSQ = function(x, y, z)
	local sq = getCell():getGridSquare(x, y, z);
	if not sq then
		sq = IsoGridSquare.new(getCell(), nil, x, y, z);
		getCell():ConnectNewSquare(sq, true);
	end
	return sq;
end