--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

---@class AStormIsComing
AStormIsComing = {}

AStormIsComing.Add = function()
    addChallenge(AStormIsComing);
end

AStormIsComing.OnGameStart = function()
    --[[
    local modal = ISModalRichText:new(getCore():getScreenWidth()/2 - 100, getCore():getScreenHeight()/2 - 50, 200, 100, getText("Challenge_AStormIsComingInfoBox"), false, nil, nil, 0);
    modal:initialise();
    modal:addToUIManager();
    if JoypadState.players[1] then
        JoypadState.players[1].focus = modal
    end
    --]]
end

AStormIsComing.OnInitSeasons = function(_season)
    _season:init(
        25, --aprox miami florida
        16, --min
        30, --max
        4, --amount of degrees temp can go lower or higher then mean
        _season:getSeasonLag(),
        _season:getHighNoon(),
        _season:getSeedA(),
        _season:getSeedB(),
        _season:getSeedC()
    );
end

AStormIsComing.OnInitWorld = function()
    SandboxVars.Zombies = 1;
    SandboxVars.Distribution = 1;
    SandboxVars.DayLength = 3;
    SandboxVars.StartMonth = 7;
    SandboxVars.StartTime = 2;
    SandboxVars.WaterShutModifier = 7;
    SandboxVars.ElecShutModifier = 7;
    SandboxVars.FoodLoot = 1;
    SandboxVars.CannedFoodLoot = 1;
    SandboxVars.RangedWeaponLoot = 1;
    SandboxVars.AmmoLoot = 1;
    SandboxVars.SurvivalGearsLoot = 1;
    SandboxVars.MechanicsLoot = 1;
    SandboxVars.LiteratureLoot = 1;
    SandboxVars.MedicalLoot = 1;
    SandboxVars.WeaponLoot = 1;
    SandboxVars.OtherLoot = 1;
    SandboxVars.Temperature = 3;
    SandboxVars.Rain = 3;
    --	    SandboxVars.erosion = 12
    SandboxVars.ErosionSpeed = 1
    SandboxVars.XpMultiplier = "1.0";
    SandboxVars.Farming = 3;
    SandboxVars.NatureAbundance = 5;
    SandboxVars.PlantResilience = 3;
    SandboxVars.PlantAbundance = 3;
    SandboxVars.Alarm = 3;
    SandboxVars.LockedHouses = 5;
    SandboxVars.FoodRotSpeed = 3;
    SandboxVars.FridgeFactor = 3;
    SandboxVars.LootRespawn = 1;
    SandboxVars.StatsDecrease = 3;
    SandboxVars.StarterKit = false;
    SandboxVars.TimeSinceApo = 1;
    SandboxVars.MultiHitZombies = false;

    SandboxVars.ZombieConfig.PopulationMultiplier = 4.0

    Events.OnGameStart.Add(AStormIsComing.OnGameStart);
    --Events.EveryDays.Add(AStormIsComing.EveryDays);
    Events.EveryTenMinutes.Add(AStormIsComing.EveryTenMinutes);
    Events.OnInitSeasons.Add(AStormIsComing.OnInitSeasons);
end

AStormIsComing.EveryTenMinutes = function()
    getClimateManager():triggerCustomWeather(0.95, true);
end


AStormIsComing.RemovePlayer = function(p)
end

AStormIsComing.AddPlayer = function(playerNum, playerObj)

end

AStormIsComing.Render = function()
end

AStormIsComing.id = "AStormIsComing";
AStormIsComing.image = "media/lua/client/LastStand/AStormIsComing.png";
AStormIsComing.gameMode = "A Storm is Coming";
AStormIsComing.world = "Muldraugh, KY";
AStormIsComing.xcell = 36;
AStormIsComing.ycell = 31;
AStormIsComing.x = 21;
AStormIsComing.y = 111;
AStormIsComing.z = 0;

AStormIsComing.spawns = {
    {xcell=11+25, ycell=9+25, x=62, y=47}, -- Medium house2
    {xcell=11+25, ycell=8+25, x=116, y=232}, -- little house2
    {xcell=11+25, ycell=8+25, x=3, y=173}, -- little house2
    {xcell=11+25, ycell=8+25, x=118, y=229}, -- little house2
    {xcell=11+25, ycell=6+25, x=142, y=72},
    {xcell=11+25, ycell=6+25, x=151, y=190},
}

AStormIsComing.hourOfDay = 7;

Events.OnChallengeQuery.Add(AStormIsComing.Add)



