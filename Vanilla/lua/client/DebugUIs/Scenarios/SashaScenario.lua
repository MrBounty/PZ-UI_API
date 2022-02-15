if debugScenarios == nil then
	debugScenarios = {}
end


debugScenarios.SashaScenario = {
	name = "Sasha Debug Scenario",
--	forceLaunch = true, -- use this to force the launch of THIS scenario right after main menu was loaded, save more clicks! Don't do multiple scenarii with this options
	startLoc = {x=10596, y=8818, z=0 }, -- highway muldraugh
--	startLoc = {x=12755, y=1655, z=0 }, -- LOUISVILLE
	setSandbox = function()
		
		SandboxVars.Speed = 3;
		SandboxVars.Zombies = 5; -- 5 = no zombies, 1 = insane (then 2 = low, 3 normal, 4 high..)
		SandboxVars.Distribution = 1;
		SandboxVars.Survivors = 1;
		SandboxVars.DayLength = 3;
		SandboxVars.StartYear = 1;
		SandboxVars.StartMonth = 7;
		SandboxVars.StartDay = 9;
		SandboxVars.StartTime = 2;
		SandboxVars.VehicleEasyUse = true;
		SandboxVars.WaterShutModifier = 14;
		SandboxVars.ElecShutModifier = 14;
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
		SandboxVars.Alarm = 4;
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
		-- adding some cooking level
		getPlayer():LevelPerk(Perks.Cooking);
		getPlayer():LevelPerk(Perks.Cooking);
		getPlayer():LevelPerk(Perks.Cooking);
		-- all the perks are: Agility, Cooking, Melee, Crafting, Fitness, Strength, Blunt, Axe, Sprinting, Lightfoot, Nimble, Sneak, Woodwork, Aiming, Reloading, Farming, Survivalist, Fishing, Trapping, Passiv, Firearm, PlantScavenging, Doctor, Electricity, Blacksmith, MetalWelding, Melting, Mechanics, Spear, Maintenance, SmallBlade, LongBlade, SmallBlunt, Combat,
	
		-- adding an axe!
		getPlayer():getInventory():AddItem("Base.Axe")
		
		-- adding a clip and 3 bullets in it
		local b = getPlayer():getInventory():AddItem("Base.223Clip");
		b:setCurrentAmmoCount(3);
	
		-- adding a broken kitchen knife
		local d = getPlayer():getInventory():AddItem("Base.KitchenKnife");
		d:setCondition(0);
	
		-- adding some recipes (the one you get from magazines)
		getPlayer():getKnownRecipes():add("Basic Mechanics"); getPlayer():getKnownRecipes():add("Intermediate Mechanics"); getPlayer():getKnownRecipes():add("Advanced Mechanics");
	end
}
