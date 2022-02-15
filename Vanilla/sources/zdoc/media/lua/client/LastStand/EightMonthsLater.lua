---@class EightMonthsLater
EightMonthsLater = {}

EightMonthsLater.PreloadInit = function()
--SandboxVars.Zombies = 2;
  --  SandboxVars.ZombieLore.Speed = 1;
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
    SandboxVars.MultiHitZombies = false;
    
end

EightMonthsLater.AddPlayer = function(playerNum, playerObj)

    if getCore():isDedicated() then return end

    -- print(p);
    local pl = playerObj;

    print("adding challenge inventory");
--  local torch = pl:getInventory():AddItem("Base.Torch");

--local pistol = pl:getInventory():AddItem("Base.Schoolbag");
--  pl:getInventory():AddItems("Base.ShotgunShells", 5);
--   torch:setActivated(true);
--    torch:setLightStrength(torch:getLightStrength() / 1.5);
--  pl:setSecondaryHandItem(torch);
-- pl:setPrimaryHandItem(pistol);
end

function EightMonthsLater.RemovePlayer(playerObj)
end

EightMonthsLater.Init = function()
    EightMonthsLater.wave = 0;
    EightMonthsLater.waveTime = 0;
    Events.OnTick.Add(EightMonthsLater.Tick)
    EightMonthsLater.FillContainers();
    LastStandData.zombieList = LuaList:new();
end

EightMonthsLater.Render = function()

--~ 	getTextManager():DrawStringRight(UIFont.Small, getCore():getOffscreenWidth() - 20, 20, "Zombies left : " .. (EightMonthsLater.zombiesSpawned - EightMonthsLater.deadZombie), 1, 1, 1, 0.8);

--~ 	getTextManager():DrawStringRight(UIFont.Small, (getCore():getOffscreenWidth()*0.9), 40, "Next wave : " .. tonumber(((60*60) - EightMonthsLater.waveTime)), 1, 1, 1, 0.8);
end

EightMonthsLater.Tick = function()
    if getPlayer() == nil then return end;

end

function EightMonthsLater.onBackButtonWheel(playerNum, dir)
end

EightMonthsLater.name = "One Year Later";
EightMonthsLater.description = "Nature has begun to reclaim the Earth. Can you survive in this dead world?";
EightMonthsLater.completionText = "Survive a week to unlock the next challenge.";
EightMonthsLater.image = "media/lua/client/LastStand/EightMonthsLater.png";
EightMonthsLater.gameMode = "One Year Later";
EightMonthsLater.world = "Muldraugh, KY";
EightMonthsLater.xcell = 36;
EightMonthsLater.ycell = 31;
EightMonthsLater.x = 21;
EightMonthsLater.y = 111;
EightMonthsLater.z = 0;
EightMonthsLater.hourOfDay = 7;
Events.OnChallengeQuery.Add(EightMonthsLater.Add)
