--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

---@class TheDescendingFog
TheDescendingFog = {}

TheDescendingFog.Add = function()
    addChallenge(TheDescendingFog);
end

TheDescendingFog.OnGameStart = function()
    --[[
    local modal = ISModalRichText:new(getCore():getScreenWidth()/2 - 100, getCore():getScreenHeight()/2 - 50, 200, 100, getText("Challenge_TheDescendingFogInfoBox"), false, nil, nil, 0);
    modal:initialise();
    modal:addToUIManager();
    if JoypadState.players[1] then
        JoypadState.players[1].focus = modal
    end
    --]]
end

TheDescendingFog.OnInitSeasons = function(_season)
    _season:init(
        _season:getLat(), --aprox miami florida
        _season:getTempMin(), --min
        _season:getTempMax(), --max
        _season:getTempDiff(), --amount of degrees temp can go lower or higher then mean
        _season:getSeasonLag(),
        _season:getHighNoon(),
        _season:getSeedA(),
        _season:getSeedB(),
        _season:getSeedC()
    );
end

TheDescendingFog.OnInitWorld = function()
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

    Events.OnGameStart.Add(TheDescendingFog.OnGameStart);
    --Events.EveryDays.Add(TheDescendingFog.EveryDays);
    Events.EveryTenMinutes.Add(TheDescendingFog.EveryTenMinutes);
    Events.OnInitSeasons.Add(TheDescendingFog.OnInitSeasons);
end

TheDescendingFog.EveryTenMinutes = function()
    --getClimateManager():triggerCustomWeather(0.95, true);
end


TheDescendingFog.RemovePlayer = function(p)
end

TheDescendingFog.AddPlayer = function(playerNum, playerObj)

end

TheDescendingFog.Render = function()
end

TheDescendingFog.id = "TheDescendingFog";
TheDescendingFog.image = "media/lua/client/LastStand/TheDescendingFog.png";
TheDescendingFog.gameMode = "The Descending Fog";
TheDescendingFog.world = "Muldraugh, KY";
TheDescendingFog.xcell = 36;
TheDescendingFog.ycell = 31;
TheDescendingFog.x = 21;
TheDescendingFog.y = 111;
TheDescendingFog.z = 0;

TheDescendingFog.spawns = {
    {xcell=11+25, ycell=9+25, x=62, y=47}, -- Medium house2
    {xcell=11+25, ycell=8+25, x=116, y=232}, -- little house2
    {xcell=11+25, ycell=8+25, x=3, y=173}, -- little house2
    {xcell=11+25, ycell=8+25, x=118, y=229}, -- little house2
    {xcell=11+25, ycell=6+25, x=142, y=72},
    {xcell=11+25, ycell=6+25, x=151, y=190},
}

TheDescendingFog.hourOfDay = 7;

Events.OnChallengeQuery.Add(TheDescendingFog.Add)

