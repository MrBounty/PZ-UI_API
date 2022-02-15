if debugScenarios == nil then
    debugScenarios = {}
end


debugScenarios.MarkR = {
    name = "MarkR",
    forceLaunch = false,
  --startLoc = {x=10745, y=9412, z=0 },
    startLoc = {x=11882, y=6901, z=0 },
    setSandbox = function()
        SandboxVars.Zombies = 1;
        SandboxVars.Distribution = 1;
        SandboxVars.DayLength = 3;
        SandboxVars.StartMonth = 12;
        SandboxVars.StartTime = 2;
        SandboxVars.WaterShutModifier = -1;
        SandboxVars.ElecShutModifier = -1;
        SandboxVars.FoodLoot = 1;
        SandboxVars.WeaponLoot = 1;
        SandboxVars.OtherLoot = 1;
        SandboxVars.Temperature = 3;
        SandboxVars.Rain = 3;
        --    SandboxVars.erosion = 12
        SandboxVars.ErosionSpeed = 1
        SandboxVars.XpMultiplier = "1.0";
        SandboxVars.Farming = 3;
        SandboxVars.NatureAbundance = 5;
        SandboxVars.PlantResilience = 3;
        SandboxVars.PlantAbundance = 3;
        SandboxVars.Alarm = 3;
        SandboxVars.LockedHouses = 3;
        SandboxVars.FoodRotSpeed = 3;
        SandboxVars.FridgeFactor = 3;
        SandboxVars.ZombiesRespawn = 2;
        SandboxVars.LootRespawn = 1;
        SandboxVars.StatsDecrease = 3;
        SandboxVars.StarterKit = false;
        SandboxVars.TimeSinceApo = 13;


    end,
    onStart = function()

    end
}
