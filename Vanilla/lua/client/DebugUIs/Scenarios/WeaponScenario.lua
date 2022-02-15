if debugScenarios == nil then
	debugScenarios = {}
end


debugScenarios.WeaponScenario = {
	name = "Whole Lotta Weapon",
--	forceLaunch = true, -- use this to force the launch of THIS scenario right after main menu was loaded, save more clicks! Don't do multiple scenarii with this options
	--      startLoc = {x=13538, y=5759, z=0 }, -- Mall
	--    startLoc = {x=10145, y=12763, z=0 }, -- lighting test
	--    startLoc = {x=3645, y=8564, z=0 }, -- Roadtrip start
	--          startLoc = {x=10645, y=10437, z=0 }, -- Police station
	--      startLoc = {x=13926, y=5877, z=0 }, -- Mall bookstore
	--    startLoc = {x=10862, y=10290, z=0 }, -- Mul
	--        startLoc = {x=10580,y=11193,z=0}, -- car crash
	--    startLoc = {x=11515, y=8830, z=0 }, -- DIxie gas station
	--    startLoc = {x=10657, y=10625, z=0 }, -- Muldraugh gas station
	--    startLoc = {x=5796, y=5384, z=0 }, -- Junkyard
	--    startLoc = {x=10835, y=10144, z=0 }, -- middle of muldraugh
	--    startLoc = {x=6476, y=5263, z=0 }, -- pharma
	--        startLoc = {x=10631, y=9750, z=0 }, -- fossoil
	--    startLoc = {x=5438, y=5886, z=0 }, -- gas2go
	--    startLoc = {x=10181, y=12783, z=0 }, -- laundromat
	--    startLoc = {x=8128, y=11729, z=1 }, -- fire dept lvl 1
	startLoc = {x=11778, y=6749, z=0 }, -- other
	setSandbox = function()
--		SandboxVars.VehicleEasyUse = true;
		--        SandboxVars.ChanceHasGas = 1;*
		--        SandboxVars.InitialGas = 2;
		SandboxVars.Zombies = 1;
		--        SandboxVars.Distribution = 1;
		--        SandboxVars.DayLength = 3;
		--        SandboxVars.StartMonth = 12;
		--        SandboxVars.WaterShutModifier = -1;
		--        SandboxVars.ElecShutModifier = -1;
		--        SandboxVars.StartTime = 2;
		--    SandboxVars.TimeSinceApo = 7;
		--        SandboxVars.CarSpawnRate = 2;
		--        SandboxVars.LockedCar = 1;
		--    SandboxVars.CarAlarm = 1;
		--    SandboxVars.ChanceHasGas = 1;
		--    SandboxVars.InitialGas = 2;
		--    SandboxVars.CarGeneralCondition = 1;
		--    SandboxVars.RecentlySurvivorVehicles = 1;
		--        SandboxVars.Zombies = 1;
		--        SandboxVars.AllowExteriorGenerator = true;
--		SandboxVars.FoodLoot = 1;
--		SandboxVars.WeaponLoot = 1;
--		SandboxVars.OtherLoot = 1;
		
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
			Sight = 2,
			Hearing = 2,
			Smell = 2,
		}
		--        SandboxVars.Temperature = 3;
		--        SandboxVars.Rain = 3;
		--        --    SandboxVars.erosion = 12
		--        SandboxVars.ErosionSpeed = 1
		--        SandboxVars.XpMultiplier = "1.0";
		--        SandboxVars.Farming = 3;z
		--        SandboxVars.NatureAbundance = 5;
		--        SandboxVars.PlantResilience = 3;
		--        SandboxVars.PlantAbundance = 3;
		--        SandboxVars.Alarm = 3;
		--        SandboxVars.LockedHouses = 3;
		--        SandboxVars.FoodRotSpeed = 3;
		--        SandboxVars.FridgeFactor = 3;
		--        SandboxVars.ZombiesRespawn = 2;
		--        SandboxVars.LootRespawn = 1;
		--        SandboxVars.StatsDecrease = 3;
		--        SandboxVars.StarterKit = false;
		--        SandboxVars.TimeSinceApo = 13;
		--
		--
	end,
	onStart = function()
	-- one handed
		getPlayer():getInventory():AddItem("Base.ChairLeg");
		getPlayer():getInventory():AddItem("Base.ClumbHammer");
		getPlayer():getInventory():AddItem("Base.FlintKnife");
		getPlayer():getInventory():AddItem("Base.IcePick");
		getPlayer():getInventory():AddItem("Base.LetterOpener");
		getPlayer():getInventory():AddItem("Base.Scissors");
		getPlayer():getInventory():AddItem("Base.HandAxe");
		getPlayer():getInventory():AddItem("Base.HandFork");
		getPlayer():getInventory():AddItem("Base.HandScythe");
		getPlayer():getInventory():AddItem("Base.Machete");
		getPlayer():getInventory():AddItem("Base.PipeWrench");
		getPlayer():getInventory():AddItem("Base.Saxophone");
		getPlayer():getInventory():AddItem("Base.Scalpel");
		getPlayer():getInventory():AddItem("Base.Wrench");
		getPlayer():getInventory():AddItem("Base.BallPeenHammer");
		getPlayer():getInventory():AddItem("Base.Drumstick");
		getPlayer():getInventory():AddItem("Base.BadmintonRacket");
		getPlayer():getInventory():AddItem("Base.Violin");
		getPlayer():getInventory():AddItem("Base.Trumpet");
		getPlayer():getInventory():AddItem("Base.Banjo");
		getPlayer():getInventory():AddItem("Base.PickAxeHandleSpiked");
		getPlayer():getInventory():AddItem("Base.PickAxeHandle");
		getPlayer():getInventory():AddItem("Base.Plunger");
		getPlayer():getInventory():AddItem("Base.Flute");
		getPlayer():getInventory():AddItem("Base.TennisRacket");
		getPlayer():getInventory():AddItem("Base.LeadPipe");
	-- two handed
		getPlayer():getInventory():AddItem("Base.ChairLeg");
		getPlayer():getInventory():AddItem("Base.Broom");
		getPlayer():getInventory():AddItem("Base.CanoePadel");
		getPlayer():getInventory():AddItem("Base.CanoePadelX2");
		getPlayer():getInventory():AddItem("Base.GuitarAcoustic");
		getPlayer():getInventory():AddItem("Base.GuitarElectricBassRed");
		getPlayer():getInventory():AddItem("Base.GuitarElectricBassBlue");
		getPlayer():getInventory():AddItem("Base.GuitarElectricBassBlack");
		getPlayer():getInventory():AddItem("Base.GuitarElectricRed");
		getPlayer():getInventory():AddItem("Base.GuitarElectricBlue");
		getPlayer():getInventory():AddItem("Base.GuitarElectricBlack");
		getPlayer():getInventory():AddItem("Base.LaCrosseStick");
		getPlayer():getInventory():AddItem("Base.IceHockeyStick");
		getPlayer():getInventory():AddItem("Base.HockeyStick");
		getPlayer():getInventory():AddItem("Base.PickAxe");
		getPlayer():getInventory():AddItem("Base.Shovel");
		getPlayer():getInventory():AddItem("Base.Shovel2");
		getPlayer():getInventory():AddItem("Base.Sledgehammer2");
		getPlayer():getInventory():AddItem("Base.SnowShovel");
		getPlayer():getInventory():AddItem("Base.WoodAxe");
		getPlayer():getInventory():AddItem("Base.Rake");
		getPlayer():getInventory():AddItem("Base.LeafRake");
	-- Spear
		getPlayer():getInventory():AddItem("Base.GardenFork");
		getPlayer():getInventory():AddItem("Base.SpearBreadKnife");
		getPlayer():getInventory():AddItem("Base.SpearKnife");
		getPlayer():getInventory():AddItem("Base.SpearCrafted");
		getPlayer():getInventory():AddItem("Base.SpearFork");
		getPlayer():getInventory():AddItem("Base.SpearHandFork");
		getPlayer():getInventory():AddItem("Base.SpearHuntingKnife");
		getPlayer():getInventory():AddItem("Base.SpearLetterOpener");
		getPlayer():getInventory():AddItem("Base.SpearMachete");
		getPlayer():getInventory():AddItem("Base.SpearScalpel");
		getPlayer():getInventory():AddItem("Base.SpearScissors");
		getPlayer():getInventory():AddItem("Base.SpearScrewdriver");
		getPlayer():getInventory():AddItem("Base.SpearSpoon");
	end
}
