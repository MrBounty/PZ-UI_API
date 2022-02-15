

---@class YouHaveOneDay
YouHaveOneDay = {}

YouHaveOneDay.Add = function()
    addChallenge(YouHaveOneDay);
end

YouHaveOneDay.AddPlayer = function(playerNum, playerObj)
end

function YouHaveOneDay.RemovePlayer(playerObj)
end

YouHaveOneDay.Init = function()
end

YouHaveOneDay.OnGameStart = function()
    getCore():setZombieGroupSound(false);

    local modal = ISModalRichText:new(getCore():getScreenWidth()/2 - 165, getCore():getScreenHeight()/2 - 45, 330, 90, getText("Challenge_YouHaveOneDayInfoBox"), false, nil, nil, 0);
    modal:initialise();
    modal:addToUIManager();
    if JoypadState.players[1] then
        JoypadState.players[1].focus = modal
    end

    getGameTime():getModData()["hordeNumber"] = 75;
    getGameTime():getModData()["hoursBeforeSpawn"] = 24;
end

YouHaveOneDay.OnPlayerUpdate = function(zombie)
end

YouHaveOneDay.OnZombieUpdate = function(zombie)
    if getPlayer():getHoursSurvived() < YouHaveOneDay.hoursBeforeSpawn then
        zombie:setUseless(true);
    else
        zombie:setUseless(false);
        if ZombRand(3) == 0 then
            zombie:Hit(nil,getPlayer(),0,true,0);
        end
    end
end

YouHaveOneDay.EveryHours = function()
    if not YouHaveOneDay.hordeSpawned and getPlayer():getHoursSurvived() > getGameTime():getModData()["hoursBeforeSpawn"] then
        for i,v in ipairs(YouHaveOneDay.hordes) do
            createHordeFromTo(v.x, v.y, getPlayer():getX(), getPlayer():getY(), getGameTime():getModData()["hordeNumber"])
        end
        getGameTime():getModData()["hordeNumber"] = getGameTime():getModData()["hordeNumber"] + 50;
        getGameTime():getModData()["hoursBeforeSpawn"] = getGameTime():getModData()["hoursBeforeSpawn"] + 24;
        --                    spawnHorde(getPlayer():getX() - 13, getPlayer():getY() - 13, getPlayer():getX() - 7, getPlayer():getY() - 7, 100);
        --        spawnHorde(getPlayer():getX() + 21, getPlayer():getY() + 21, getPlayer():getX() + 15, getPlayer():getY() + 15, 100);
--        spawnHorde(getPlayer():getX() + 21, getPlayer():getY() -13, getPlayer():getX() +15, getPlayer():getY() -7, 100);
--        spawnHorde(getPlayer():getX() - 13, getPlayer():getY() +21, getPlayer():getX() -7, getPlayer():getY() + 15, 100);
        addSound(getPlayer(), getPlayer():getX(), getPlayer():getY(), 0, 100, 100);
        YouHaveOneDay.hordeSpawned = true;
    end
end

YouHaveOneDay.EveryDays = function()
    YouHaveOneDay.hordeSpawned = false;
end

YouHaveOneDay.OnInitWorld = function()
    SandboxVars.Zombies = 1;
    SandboxVars.Distribution = 1;
    SandboxVars.DayLength = 3;
    SandboxVars.StartMonth = 7;
    SandboxVars.StartTime = 2;
    SandboxVars.WaterShutModifier = 7;
    SandboxVars.ElecShutModifier = 7;
    SandboxVars.FoodLoot = 4;
    SandboxVars.CannedFoodLoot = 4;
    SandboxVars.RangedWeaponLoot = 4;
    SandboxVars.AmmoLoot = 4;
    SandboxVars.SurvivalGearsLoot = 4;
    SandboxVars.MechanicsLoot = 4;
    SandboxVars.LiteratureLoot = 4;
    SandboxVars.MedicalLoot = 4;
    SandboxVars.WeaponLoot = 4;
    SandboxVars.OtherLoot = 4;
    SandboxVars.Temperature = 3;
    SandboxVars.Rain = 3;
    SandboxVars.ErosionSpeed = 5
    SandboxVars.XpMultiplier = "1.0";
    SandboxVars.Farming = 3;
    SandboxVars.NatureAbundance = 5;
    SandboxVars.PlantResilience = 3;
    SandboxVars.PlantAbundance = 3;
    SandboxVars.Alarm = 3;
    SandboxVars.LockedHouses = 3;
    SandboxVars.FoodRotSpeed = 3;
    SandboxVars.FridgeFactor = 3;
    SandboxVars.LootRespawn = 1;
    SandboxVars.StatsDecrease = 3;
    SandboxVars.StarterKit = false;
    SandboxVars.TimeSinceApo = 1;
    SandboxVars.DecayingCorpseHealthImpact = 1
    SandboxVars.MultiHitZombies = false;

    SandboxVars.ZombieConfig.PopulationMultiplier = 4.0

--    Events.OnZombieUpdate.Add(YouHaveOneDay.OnZombieUpdate);
    Events.OnGameStart.Add(YouHaveOneDay.OnGameStart);

    Events.EveryHours.Add(YouHaveOneDay.EveryHours);
    Events.EveryDays.Add(YouHaveOneDay.EveryDays);
        --    Events.OnPlayerUpdate.Add(YouHaveOneDay.OnPlayerUpdate);
end

YouHaveOneDay.Render = function()

end

YouHaveOneDay.id = "YouHaveOneDay";
YouHaveOneDay.image = "media/lua/client/LastStand/YouHaveOneDay.png";
YouHaveOneDay.world = "challengemaps/KnoxCounty";
YouHaveOneDay.gameMode = "You Have One Day";
YouHaveOneDay.xcell = 0;
YouHaveOneDay.ycell = 0;
YouHaveOneDay.x = 41;
YouHaveOneDay.y = 22;
YouHaveOneDay.z = 1;

YouHaveOneDay.hordeSpawned = false;
YouHaveOneDay.hordePark = {x=76,y=67};
YouHaveOneDay.hordeFlat = {x=143,y=94};
YouHaveOneDay.hordeWood = {x=35,y=198};
YouHaveOneDay.hordeDiner = {x=191,y=123};
YouHaveOneDay.hordeTownArea = {x=231,y=53};
YouHaveOneDay.hordeOffice = {x=262,y=215};
YouHaveOneDay.hordeWoodCamp = {x=123,y=276};
YouHaveOneDay.hordeGasStation = {x=128,y=145};
YouHaveOneDay.hordes = {YouHaveOneDay.hordePark, YouHaveOneDay.hordeFlat, YouHaveOneDay.hordeWood, YouHaveOneDay.hordeDiner, YouHaveOneDay.hordeTownArea, YouHaveOneDay.hordeOffice, YouHaveOneDay.hordeWoodCamp, YouHaveOneDay.hordeGasStation};

Events.OnChallengeQuery.Add(YouHaveOneDay.Add)
