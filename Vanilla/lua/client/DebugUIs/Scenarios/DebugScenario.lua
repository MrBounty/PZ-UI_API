if debugScenarios == nil then
    debugScenarios = {}
end


debugScenarios.DebugScenario = {
    name = "Basic Debug Scenario",
    --forceLaunch = true, -- use this to force the launch of THIS scenario right after main menu was loaded, save more clicks! Don't do multiple scenarii with this options
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
        SandboxVars.VehicleEasyUse = true;
--        SandboxVars.ChanceHasGas = 1;*
--        SandboxVars.InitialGas = 2;
        SandboxVars.Zombies = 5;
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
        SandboxVars.FoodLoot = 1;
        SandboxVars.WeaponLoot = 1;
        SandboxVars.OtherLoot = 1;
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
        getPlayer():LevelPerk(Perks.Cooking);getPlayer():LevelPerk(Perks.Cooking);getPlayer():LevelPerk(Perks.Cooking);getPlayer():LevelPerk(Perks.Cooking);getPlayer():LevelPerk(Perks.Cooking);
        getPlayer():LevelPerk(Perks.Electricity);
        getPlayer():LevelPerk(Perks.Electricity);
        getPlayer():LevelPerk(Perks.Woodwork);getPlayer():LevelPerk(Perks.Woodwork);getPlayer():LevelPerk(Perks.Woodwork);getPlayer():LevelPerk(Perks.Woodwork);getPlayer():LevelPerk(Perks.Woodwork);getPlayer():LevelPerk(Perks.Woodwork);getPlayer():LevelPerk(Perks.Woodwork);
        getPlayer():LevelPerk(Perks.Woodwork);getPlayer():LevelPerk(Perks.Woodwork);getPlayer():LevelPerk(Perks.Woodwork);getPlayer():LevelPerk(Perks.Woodwork);getPlayer():LevelPerk(Perks.Woodwork);getPlayer():LevelPerk(Perks.Woodwork);getPlayer():LevelPerk(Perks.Woodwork);
        --        getPlayer():getInventory():AddItem("Base.DigitalWatch2")
        --        getPlayer():getInventory():AddItem("Base.MushroomGeneric1")getPlayer():getInventory():AddItem("Base.MushroomGeneric1")getPlayer():getInventory():AddItem("Base.MushroomGeneric1")getPlayer():getInventory():AddItem("Base.MushroomGeneric1")
        --        getPlayer():getInventory():AddItem("Base.Axe")
        --        getPlayer():getInventory():AddItem("Base.BakingTrayBread");
        --        getPlayer():getInventory():AddItem("Base.DuctTape");getPlayer():getInventory():AddItem("Base.DuctTape");getPlayer():getInventory():AddItem("Base.DuctTape");getPlayer():getInventory():AddItem("Base.DuctTape")
        --        getPlayer():getInventory():AddItem("Base.DeadBird");getPlayer():getInventory():AddItem("Base.DeadBird");getPlayer():getInventory():AddItem("Base.DeadBird");getPlayer():getInventory():AddItem("Base.DeadBird");getPlayer():getInventory():AddItem("Base.DeadBird");getPlayer():getInventory():AddItem("Base.DeadBird");getPlayer():getInventory():AddItem("Base.DeadBird");
        --        getPlayer():getInventory():AddItem("Base.Bacon")
        --        getPlayer():getInventory():AddItem("Base.KitchenKnife")
        --        getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");getPlayer():getInventory():AddItem("Base.Plank");
        --        getPlayer():getInventory():AddItem("Base.Nails");getPlayer():getInventory():AddItem("Base.Nails");getPlayer():getInventory():AddItem("Base.Nails");getPlayer():getInventory():AddItem("Base.Nails");getPlayer():getInventory():AddItem("Base.Nails");getPlayer():getInventory():AddItem("Base.Nails");getPlayer():getInventory():AddItem("Base.Nails");getPlayer():getInventory():AddItem("Base.Nails");getPlayer():getInventory():AddItem("Base.Nails");getPlayer():getInventory():AddItem("Base.Nails");
        --        getPlayer():getInventory():AddItem("Base.BlowTorch");getPlayer():getInventory():AddItem("Base.WeldingMask");
        --        getPlayer():getBodyDamage():getBodyPart(BodyPartType.ForeArm_L):setFractureTime(100);
--        getPlayer():getInventory():AddItem("Base.Trousers");
        getPlayer():getInventory():AddItem("Base.SpearKnife");
                local d = getPlayer():getInventory():AddItem("Base.BaseballBat");
--        local d = getPlayer():getInventory():AddItem("Base.BaseballBatNails");
--        local d = getPlayer():getInventory():AddItem("Base.ButterKnife");
--        local d = getPlayer():getInventory():AddItem("Base.Crowbar");
--        local d = getPlayer():getInventory():AddItem("Base.Axe");
--        local d = getPlayer():getInventory():AddItem("Base.CraftedFishingRod");
--        local d = getPlayer():getInventory():AddItem("Base.FishingRod");
--        local d = getPlayer():getInventory():AddItem("Base.Fork");
--        local d = getPlayer():getInventory():AddItem("Base.Pan");
        local d = getPlayer():getInventory():AddItem("Base.HuntingKnife");
--        local d = getPlayer():getInventory():AddItem("Base.KitchenKnife");
--        local d = getPlayer():getInventory():AddItem("Base.PlankNail");
--        local d = getPlayer():getInventory():AddItem("Base.Plank");
--        local d = getPlayer():getInventory():AddItem("Base.Poolcue");
--        local d = getPlayer():getInventory():AddItem("Base.RollingPin");
--        local d = getPlayer():getInventory():AddItem("Base.Scissors");
--        local d = getPlayer():getInventory():AddItem("Base.Screwdriver");
--        local d = getPlayer():getInventory():AddItem("farming.Shovel");
        local d = getPlayer():getInventory():AddItem("Base.Sledgehammer");
        local d = getPlayer():getInventory():AddItem("Base.Shotgun");
        local d = getPlayer():getInventory():AddItem("Base.Pistol");
--        local d = getPlayer():getInventory():AddItem("Base.Spoon");
--        local d = getPlayer():getInventory():AddItem("Base.AxeStone");
--        local d = getPlayer():getInventory():AddItem("Base.HammerStone");
--        local d = getPlayer():getInventory():AddItem("Base.WoodenLance");
--        local d = getPlayer():getInventory():AddItem("Base.HockeyStick");
--        local d = getPlayer():getInventory():AddItem("Base.Pencil");
--        local d = getPlayer():getInventory():AddItem("Base.Golfclub");
        local d = getPlayer():getInventory():AddItem("Base.Hat_GolfHat");
        
        
        local d = getPlayer():getInventory():AddItem("Base.Hammer");
        getPlayer():getInventory():AddItem("Base.ShotgunShells");getPlayer():getInventory():AddItem("Base.ShotgunShells");getPlayer():getInventory():AddItem("Base.ShotgunShells");getPlayer():getInventory():AddItem("Base.ShotgunShells");getPlayer():getInventory():AddItem("Base.ShotgunShells");getPlayer():getInventory():AddItem("Base.ShotgunShells");getPlayer():getInventory():AddItem("Base.ShotgunShells");getPlayer():getInventory():AddItem("Base.ShotgunShells");getPlayer():getInventory():AddItem("Base.ShotgunShells");getPlayer():getInventory():AddItem("Base.ShotgunShells");getPlayer():getInventory():AddItem("Base.ShotgunShells");getPlayer():getInventory():AddItem("Base.ShotgunShells");getPlayer():getInventory():AddItem("Base.ShotgunShells");getPlayer():getInventory():AddItem("Base.ShotgunShells");
--        local d = getPlayer():getInventory():AddItem("Base.Bag_ALICEpack");
--        local d = getPlayer():getInventory():AddItem("Base.KitchenKnife");
        --        d:setCondition(0);
        --        getPlayer():getInventory():AddItem("Base.WhiskeyFull");getPlayer():getInventory():AddItem("Base.WhiskeyFull");getPlayer():getInventory():AddItem("Base.WhiskeyFull");getPlayer():getInventory():AddItem("Base.WhiskeyFull");getPlayer():getInventory():AddItem("Base.WhiskeyFull");getPlayer():getInventory():AddItem("Base.WhiskeyFull");getPlayer():getInventory():AddItem("Base.WhiskeyFull");getPlayer():getInventory():AddItem("Base.WhiskeyFull");getPlayer():getInventory():AddItem("Base.WhiskeyFull");getPlayer():getInventory():AddItem("Base.WhiskeyFull");getPlayer():getInventory():AddItem("Base.WhiskeyFull");
    
    end
}
