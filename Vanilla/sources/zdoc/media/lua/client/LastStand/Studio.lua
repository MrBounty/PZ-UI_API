

---@class Studio
Studio = {}

Studio.Add = function()
    addChallenge(Studio);
end

Studio.AddPlayer = function(playerNum, playerObj)
end

function Studio.RemovePlayer(playerObj)
end

Studio.Init = function()
end

Studio.OnGameStart = function()
end


Studio.OnInitWorld = function()
    SandboxVars.Zombies = 3;
--    SandboxVars.Distribution = 1;
--    SandboxVars.DayLength = 3;
--    SandboxVars.StartMonth = 7;
    SandboxVars.StartTime = 7;
--    SandboxVars.WaterShutModifier = 7;
--    SandboxVars.ElecShutModifier = 7;
--    SandboxVars.FoodLoot = 4;
--    SandboxVars.WeaponLoot = 4;
--    SandboxVars.OtherLoot = 4;
--    SandboxVars.Temperature = 3;
--    SandboxVars.Rain = 3;
--    SandboxVars.ErosionSpeed = 5
--    SandboxVars.XpMultiplier = "1.0";
--    SandboxVars.Farming = 3;
--    SandboxVars.NatureAbundance = 5;
--    SandboxVars.PlantResilience = 3;
--    SandboxVars.PlantAbundance = 3;
--    SandboxVars.Alarm = 3;
--    SandboxVars.LockedHouses = 3;
--    SandboxVars.FoodRotSpeed = 3;
--    SandboxVars.FridgeFactor = 3;
--    SandboxVars.LootRespawn = 1;
--    SandboxVars.StatsDecrease = 3;
--    SandboxVars.StarterKit = false;
--    SandboxVars.TimeSinceApo = 1;
--    SandboxVars.DecayingCorpseHealthImpact = 1
--
--    SandboxVars.ZombieConfig.PopulationMultiplier = 4.0
    SandboxVars.MultiHitZombies = false;
--
----    Events.OnZombieUpdate.Add(Studio.OnZombieUpdate);
--    Events.OnGameStart.Add(Studio.OnGameStart);
--
--    Events.EveryHours.Add(Studio.EveryHours);
--    Events.EveryDays.Add(Studio.EveryDays);
        --    Events.OnPlayerUpdate.Add(Studio.OnPlayerUpdate);
    
    SandboxVars.ZombieLore = {
        Speed = 1,
        Strength = 1,
        Toughness = 2,
        Transmission = 1,
        Mortality = 3,
        Reanimate = 3,
        Cognition = 2,
        Memory = 2,
        Decomp = 1,
        Sight = 2,
        Hearing = 2,
        Smell = 2,
        ThumpNoChasing = 0,
        ActiveOnly = 3,
        --			ZombiesDragDown = false;
    }
end

Studio.Render = function()

end

Studio.OnNewGame = function()
    if getCore():getGameMode() == Studio.gameMode then
        getPlayer():getInventory():AddItem("Base.Torch");
    end
end

Studio.id = "Studio";
Studio.image = "media/lua/client/LastStand/Studio.png";
Studio.world = "challengemaps/Studio";
Studio.gameMode = "Studio";
Studio.xcell = 1;
Studio.ycell = 1;
Studio.x = 183;
Studio.y = 118;
Studio.z = 0;

Events.OnChallengeQuery.Add(Studio.Add)
Events.OnNewGame.Add(Studio.OnNewGame)
