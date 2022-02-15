if debugScenarios == nil then
	debugScenarios = {}
end


debugScenarios.MP1Scenario = {
	name = "MP 1 - STADION",
--	forceLaunch = true, -- use this to force the launch of THIS scenario right after main menu was loaded, save more clicks! Don't do multiple scenarii with this options
	startLoc = {x=4635, y=7848, z=0 }, 
	setSandbox = function()
		
		SandboxVars.Speed = 3;
		SandboxVars.Zombies = 3;
		SandboxVars.Distribution = 1;
		SandboxVars.Survivors = 1;
		SandboxVars.DayLength = 3;
		SandboxVars.StartYear = 1;
		SandboxVars.StartMonth = 7;
		SandboxVars.StartDay = 9;
		SandboxVars.StartTime = 4;
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
		SandboxVars.FuelStationGas = 4;
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
		--chr:setFemale(false);
		--chr:getDescriptor():setFemale(false);
		
		-- reset
		--chr:clearWornItems();
		--chr:getInventory():clear();
		
		-- chr visual
		--visual:setBeardModel("");
		--visual:setHairModel("Messy");
		--local immutableColor = ImmutableColor.new(0.105, 0.09, 0.086, 1)
		--visual:setHairColor(immutableColor)
		--visual:setBeardColor(immutableColor)
		--visual:setSkinTextureIndex(2);
		--chr:resetModel();
	
		-- wpn
		--pan = inv:AddItem("Base.Pan");
		--chr:setPrimaryHandItem(pan);
		--shotgun = inv:AddItem("Base.Shotgun");
		--shotgun:setCurrentAmmoCount(6);
		--shotgun:setRoundChambered(true);
	
		-- clothing
		--clothe = inv:AddItem("Base.Tshirt_DefaultTEXTURE_TINT");
		--local color = ImmutableColor.new(0.25, 0.36, 0.36, 1)
		--clothe:getVisual():setTint(color);
		--chr:setWornItem(clothe:getBodyLocation(), clothe);
		--clothe = inv:AddItem("Base.Trousers_Denim");
		--clothe:getVisual():setTextureChoice(1);
		--chr:setWornItem(clothe:getBodyLocation(), clothe);
		--clothe = inv:AddItem("Base.Socks_Ankle");
		--chr:setWornItem(clothe:getBodyLocation(), clothe);
		--clothe = inv:AddItem("Base.Shoes_Black");
		--chr:setWornItem(clothe:getBodyLocation(), clothe);
		
		
		
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
	end
}
