if debugScenarios == nil then
    debugScenarios = {}
end


debugScenarios.Water = {
    name = "Water",
        startLoc = {x=10722,y=9263,z=0}, -- water1
--        startLoc = {x=12287,y=6756,z=0}, -- water2
    setSandbox = function()
        SandboxVars.VehicleEasyUse = true;
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
--        SandboxVars.FoodLoot = 1;
--        SandboxVars.WeaponLoot = 1;
--        SandboxVars.OtherLoot = 1;
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
        getPlayer():getInventory():AddItem("Base.Axe");
        getPlayer():getInventory():AddItem("Base.FishingRod");
        getPlayer():getInventory():AddItem("Base.FishingNet");
        getPlayer():getInventory():AddItem("Base.FishingLine");
        getPlayer():getInventory():AddItem("Base.Paperclip");
        getPlayer():getInventory():AddItems("Base.Worm", 50);
        getPlayer():getInventory():AddItem("Base.Saucepan");
        
        getPlayer():LevelPerk(Perks.Fishing);
        getPlayer():LevelPerk(Perks.Fishing);
        getPlayer():LevelPerk(Perks.Fishing);
        getPlayer():LevelPerk(Perks.Fishing);
        getPlayer():LevelPerk(Perks.Fishing);
    end
}
debugScenarios.Water2 = {
    name = "Water2",
        startLoc = {x=12287,y=6756,z=0}, -- water2
    setSandbox = function()
        SandboxVars.VehicleEasyUse = true;
        SandboxVars.Zombies = 1;
    end,
    onStart = function()
        getPlayer():getInventory():AddItem("Base.Axe");
    end
}
debugScenarios.Water3 = {
    name = "WaterDelta",
        startLoc = {x=12945,y=6900,z=0},
    setSandbox = function()
        SandboxVars.VehicleEasyUse = true;
        SandboxVars.Zombies = 1;
    end,
    onStart = function()
        getPlayer():getInventory():AddItem("Base.Axe");
    end
}
debugScenarios.Water4 = {
    name = "WaterDelta2",
        startLoc = {x=12155,y=6608,z=0},
    setSandbox = function()
        SandboxVars.VehicleEasyUse = true;
        SandboxVars.Zombies = 1;
    end,
    onStart = function()
        getPlayer():getInventory():AddItem("Base.Axe");
    end
}
debugScenarios.Water5 = {
    name = "Water5",
        startLoc = {x=12863,y=6771,z=0},
    setSandbox = function()
        SandboxVars.VehicleEasyUse = true;
        SandboxVars.Zombies = 1;
    end,
    onStart = function()
        getPlayer():getInventory():AddItem("Base.Axe");
    end
}
debugScenarios.Water6 = {
    name = "Water6",
        startLoc = {x=12863,y=6771,z=0},
    setSandbox = function()
        SandboxVars.VehicleEasyUse = true;
        SandboxVars.Zombies = 1;
    end,
    onStart = function()
        getPlayer():getInventory():AddItem("Base.Axe");
    end
}
debugScenarios.Water7 = {
    name = "Swiming pool",
        startLoc = {x=5794,y=6462,z=0},
    setSandbox = function()
        SandboxVars.VehicleEasyUse = true;
        SandboxVars.Zombies = 1;
    end,
    onStart = function()
        getPlayer():getInventory():AddItem("Base.Axe");
    end
}
debugScenarios.Water8 = {
    name = "whirlpool",
        startLoc = {x=13857,y=7426,z=0},
    setSandbox = function()
        SandboxVars.VehicleEasyUse = true;
        SandboxVars.Zombies = 1;
    end,
    onStart = function()
        getPlayer():getInventory():AddItem("Base.Axe");
    end
}
debugScenarios.Water8 = {
    name = "The Mall",
        startLoc = {x=13940,y=5820,z=0},
    setSandbox = function()
        SandboxVars.VehicleEasyUse = true;
        SandboxVars.Zombies = 1;
    end,
    onStart = function()
        getPlayer():getInventory():AddItem("Base.Axe");
    end
}