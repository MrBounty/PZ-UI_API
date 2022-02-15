---@class OpeningHours
OpeningHours = {}

OpeningHours.Add = function()
	addChallenge(OpeningHours);
end

OpeningHours.OnGameStart = function()
    getCore():setZombieGroupSound(false);

--    AddNoiseToken(getPlayer():getCurrentSquare(), 9000);
--    addSound(getPlayer(), getPlayer():getX(), getPlayer():getY(), getPlayer():getZ(), 800, 800);

    local modal = ISModalRichText:new(getCore():getScreenWidth()/2 - 100, getCore():getScreenHeight()/2 - 50, 200, 100, getText("Challenge_OpeningHoursInfoBox"), false, nil, nil, 0);
    modal:initialise();
    modal:addToUIManager();
    if JoypadState.players[1] then
        JoypadState.players[1].focus = modal
    end

    getGameTime():setTimeOfDay(8.7);

    getPlayer():setDir(IsoDirections.E);

--    spawnHorde(13853,5913,13864,5940,500);
end

OpeningHours.OnInitWorld = function()
	SandboxVars.Zombies = 1;
	SandboxVars.Distribution = 1;
	SandboxVars.DayLength = 3;
	SandboxVars.StartMonth = 7;
	SandboxVars.StartTime = 1;
	SandboxVars.WaterShutModifier = 14;
	SandboxVars.ElecShutModifier = 14;
	SandboxVars.FoodLoot = 5;
	SandboxVars.WeaponLoot = 5;
	SandboxVars.OtherLoot = 5;
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
	SandboxVars.LootRespawn = 1;
	SandboxVars.StatsDecrease = 3;
	SandboxVars.StarterKit = false;
	SandboxVars.TimeSinceApo = 1;
    SandboxVars.DecayingCorpseHealthImpact = 1
    SandboxVars.MultiHitZombies = false;

	SandboxVars.ZombieConfig.PopulationMultiplier = 4.0

	Events.OnGameStart.Add(OpeningHours.OnGameStart);
    Events.EveryDays.Add(OpeningHours.EveryDays);
    Events.EveryTenMinutes.Add(OpeningHours.EveryTenMinutes);
    Events.OnPlayerUpdate.Add(OpeningHours.OnPlayerUpdate);
end

OpeningHours.ResetMetalGates = function(area)
    for k,v in pairs(OpeningHours.metalGates[area]) do
        getGameTime():getModData()["metalgates"..v.x..v.y] = false;
    end
end

OpeningHours.OnPlayerUpdate = function()
    local doIt = true;
    if getGameTime():getHour() >= 9 and getGameTime():getHour() < 21 and not OpeningHours.openTime then
        OpeningHours.ResetMetalGates("south");
        OpeningHours.ResetMetalGates("north");
        OpeningHours.ResetMetalGates("west");
        OpeningHours.openTime = true;
--        AddNoiseToken(getPlayer():getCurrentSquare(), 9000);
--        AddWorldSound(getPlayer(), 150);
        addSound(getPlayer(), 13986, 5833, getPlayer():getZ(), 600, 600);
    end
    if (getGameTime():getHour() >= 21 or getGameTime():getHour() < 9) and OpeningHours.openTime then
        OpeningHours.ResetMetalGates("south");
        OpeningHours.ResetMetalGates("north");
        OpeningHours.ResetMetalGates("west");
        OpeningHours.openTime = false;
--        AddWorldSound(getPlayer(), 150);
    end


    if not getGameTime():getModData()["addedMissingMetalGate"] then -- force adding a missing metal gate on the mall
        local sq = getCell():getGridSquare(13941,5745, 0);
        if sq then
            local object = IsoObject.new(getCell(), sq, "location_shop_mall_01_19");
            sq:getObjects():add(object);
            sq:RecalcProperties();
            getGameTime():getModData()["addedMissingMetalGate"] = true;
        end
    end

    if doIt then
        -- spawn the zed if needed
        OpeningHours.CheckHordeSpawn("southSpawn", OpeningHours.southSpawn, OpeningHours.southSpawnX, OpeningHours.southSpawnY, OpeningHours.southSpawnZ);
        OpeningHours.CheckHordeSpawn("westSpawn", OpeningHours.westSpawn, OpeningHours.westSpawnX, OpeningHours.westSpawnY, OpeningHours.westSpawnZ);
        OpeningHours.CheckHordeSpawn("northSpawn1", OpeningHours.northSpawn1, OpeningHours.northSpawn1X, OpeningHours.northSpawn1Y, OpeningHours.northSpawn1Z);
        OpeningHours.CheckHordeSpawn("northSpawn2", OpeningHours.northSpawn2, OpeningHours.northSpawn2X, OpeningHours.northSpawn2Y, OpeningHours.northSpawn2Z);
        OpeningHours.CheckHordeSpawn("northUpstairsSpawn1", OpeningHours.northUpstairsSpawn1, OpeningHours.northUpstairsSpawn1X, OpeningHours.northUpstairsSpawn1Y, OpeningHours.northUpstairsSpawn1Z);

        -- check automatical door
        for k,v in pairs(OpeningHours.doors) do
            OpeningHours.CheckMetalGates(k);
            for l,j in pairs(v) do
                local sq = getCell():getGridSquare(j.x, j.y, 0);
                if sq then
                    for i=0,sq:getSpecialObjects():size()-1 do
                        local door = sq:getSpecialObjects():get(i);
                        if instanceof(door, "IsoDoor") then
                            if not getGameTime():getModData()["reinforceDoor" .. j.x .. "," .. j.y] then
                                getGameTime():getModData()["reinforceDoor" .. j.x .. "," .. j.y] = true;
                                door:setHealth(5000);
                            end
                            if getGameTime():getHour() >= 9 and getGameTime():getHour() < 21 and not door:IsOpen() then
                               door:ToggleDoorSilent();
                               break;
                            end
                            if (getGameTime():getHour() >= 21 or getGameTime():getHour() < 9) and door:IsOpen() then
                                door:ToggleDoorSilent();
                                break;
                            end
                        end
                    end
                end
            end
        end
   end
end

OpeningHours.CheckMetalGates = function(area)
    local add = false;
    local sprite = "location_shop_mall_01_18";
    if area == "north" or area == "south" then
        sprite = "location_shop_mall_01_19";
    end
    if (getGameTime():getHour() >= 21 or getGameTime():getHour() < 9) then
        add = true;
    end
    if OpeningHours.metalGates[area] then
        for k,v in pairs(OpeningHours.metalGates[area]) do
                local sq = getCell():getGridSquare(v.x, v.y, 0);
                if sq and not getGameTime():getModData()["metalgates"..v.x..v.y] then
                    if add then
                        local found = false;
                        for i=0,sq:getObjects():size()-1 do
                            local metalBar = sq:getObjects():get(i);
                            if metalBar:getSprite() and (metalBar:getSprite():getName() == "location_shop_mall_01_18" or
                                    metalBar:getSprite():getName() == "location_shop_mall_01_19") then
                                found = true;
                                break;
                            end
                        end
                        if not found then
                            getGameTime():getModData()["metalgates"..v.x..v.y] = true;
                            local object = IsoObject.new(getCell(), sq, sprite);
                            sq:getObjects():add(object);
                        end
                    else
                        for i=0,sq:getObjects():size()-1 do
                            local metalBar = sq:getObjects():get(i);
                            if metalBar:getSprite() and (metalBar:getSprite():getName() == "location_shop_mall_01_18" or
                                    metalBar:getSprite():getName() == "location_shop_mall_01_19") then
                                sq:getObjects():remove(metalBar);
                                getGameTime():getModData()["metalgates"..v.x..v.y] = true;
                                break;
                            end
                        end
                    end
                end
        end
    end
end

OpeningHours.CheckHordeSpawn = function(regionName, regionSpawn, regionSpawnX, regionSpawnY, regionSpawnZ)
    if not getGameTime():getModData()[regionName] then
        local sq = getCell():getGridSquare(regionSpawnX, regionSpawnY, regionSpawnZ);
        if sq then
            getGameTime():getModData()[regionName] = true;
            spawnHorde(regionSpawn.x,regionSpawn.y,regionSpawn.x2,regionSpawn.y2,regionSpawnZ, 250);
--            createHordeFromTo(regionSpawn.x,regionSpawn.y, getPlayer():getX(), getPlayer():getY(), 500)
--            AddWorldSound(getPlayer(), 150);
            addSound(getPlayer(), 13986, 5833, getPlayer():getZ(), 600, 600);
--            AddNoiseToken(getPlayer():getCurrentSquare(), 600);
        end
    end
end

OpeningHours.EveryDays = function()
  -- Every 3 days, we spawn zeds again
  OpeningHours.dayCount = OpeningHours.dayCount + 1;
  if OpeningHours.dayCount == OpeningHours.dayToRespawn then
      OpeningHours.dayCount = 0;
      getGameTime():getModData()["southSpawn"] = false;
      getGameTime():getModData()["westSpawn"] = false;
      getGameTime():getModData()["northSpawn1"] = false;
      getGameTime():getModData()["northSpawn2"] = false;
  end
end

OpeningHours.EveryTenMinutes = function()
    -- attract zed a bit to make it more difficult
    if ZombRand(10) == 0 then
        addSound(getPlayer(), getPlayer():getX(), getPlayer():getY(), getPlayer():getZ(), 100, 100);
    end
end

OpeningHours.RemovePlayer = function(p)

end

OpeningHours.AddPlayer = function(playerNum, playerObj)

end

OpeningHours.Render = function()

--~ 	getTextManager():DrawStringRight(UIFont.Small, getCore():getOffscreenWidth() - 20, 20, "Zombies left : " .. (EightMonthsLater.zombiesSpawned - EightMonthsLater.deadZombie), 1, 1, 1, 0.8);

--~ 	getTextManager():DrawStringRight(UIFont.Small, (getCore():getOffscreenWidth()*0.9), 40, "Next wave : " .. tonumber(((60*60) - EightMonthsLater.waveTime)), 1, 1, 1, 0.8);
end

OpeningHours.id = "OpeningHours";
OpeningHours.image = "media/lua/client/LastStand/OpeningHours.png";
OpeningHours.gameMode = "Opening Hours";
OpeningHours.world = "Muldraugh, KY";
OpeningHours.xcell = 46;
OpeningHours.ycell = 19;
OpeningHours.x = 115;
OpeningHours.y = 48;
OpeningHours.z = 0;
OpeningHours.southSpawn = {x=13923,y=5919,x2=13960,y2=5919};
OpeningHours.southSpawnX = 13942;
OpeningHours.southSpawnY = 5935;
OpeningHours.southSpawnZ = 0;
OpeningHours.westSpawn = {x=13857,y=5810,x2=13867,y2=5843};
OpeningHours.westSpawnX = 13855;
OpeningHours.westSpawnY = 5826;
OpeningHours.westSpawnZ = 0;
OpeningHours.northSpawn1 = {x=13885,y=5730,x2=13911,y2=5744};
OpeningHours.northSpawn1X = 13901;
OpeningHours.northSpawn1Y = 5730;
OpeningHours.northSpawn1Z = 0;
OpeningHours.northSpawn2 = {x=13911,y=5730,x2=13941,y2=5744};
OpeningHours.northSpawn2X = 13922;
OpeningHours.northSpawn2Y = 5730;
OpeningHours.northSpawn2Z = 0;
OpeningHours.northUpstairsSpawn1 = {x=13888,y=5813,x2=13898,y2=5821};
OpeningHours.northUpstairsSpawn1X = 13893;
OpeningHours.northUpstairsSpawn1Y = 5817;
OpeningHours.northUpstairsSpawn1Z = 2;

OpeningHours.doors = {south = {door1={x=13934,y=5919},door2={x=13935,y=5919},door3={x=13948,y=5919},door4={x=13949,y=5919}},
                      north = {door5={x=13901,y=5745},door6={x=13902,y=5745},door7={x=13915,y=5745},door8={x=13916,y=5745},door9={x=13932,y=5745},door10={x=13933,y=5745}},
                      west = {door1={x=13868,y=5836},door2={x=13868,y=5835},door3={x=13868,y=5823},door4={x=13868,y=5822}}};

OpeningHours.metalGates = {west = {{x=13868,y=5841}, {x=13868,y=5840}, {x=13868,y=5839}, {x=13868,y=5838}, {x=13868,y=5833}, {x=13868,y=5832}, {x=13868,y=5831}, {x=13868,y=5830}, {x=13868,y=5829}, {x=13868,y=5828}, {x=13868,y=5828}, {x=13868,y=5827}, {x=13868,y=5826}, {x=13868,y=5825}, {x=13868,y=5820}, {x=13868,y=5819}, {x=13868,y=5818}, {x=13868,y=5817}},
                           north = {{x=13893,y=5745}, {x=13894,y=5745}, {x=13895,y=5745}, {x=13896,y=5745}, {x=13897,y=5745}, {x=13898,y=5745}, {x=13899,y=5745}, {x=13904,y=5745}, {x=13904,y=5745}, {x=13905,y=5745}, {x=13906,y=5745}, {x=13907,y=5745}, {x=13908,y=5745}, {x=13909,y=5745}, {x=13910,y=5745}, {x=13911,y=5745}, {x=13912,y=5745}, {x=13913,y=5745}, {x=13918,y=5745}, {x=13919,y=5745}, {x=13920,y=5745}, {x=13921,y=5745}, {x=13922,y=5745}, {x=13923,y=5745}, {x=13924,y=5745}, {x=13925,y=5745}, {x=13926,y=5745}, {x=13927,y=5745}, {x=13928,y=5745}, {x=13929,y=5745}, {x=13930,y=5745}, {x=13935,y=5745}, {x=13936,y=5745}, {x=13937,y=5745}, {x=13938,y=5745}, {x=13939,y=5745}, {x=13940,y=5745}, {x=13941,y=5745}},
                           south = {{x=13927,y=5919}, {x=13928,y=5919}, {x=13929,y=5919}, {x=13930,y=5919}, {x=13931,y=5919}, {x=13932,y=5919}, {x=13937,y=5919}, {x=13938,y=5919}, {x=13939,y=5919}, {x=13940,y=5919}, {x=13941,y=5919}, {x=13942,y=5919}, {x=13943,y=5919}, {x=13944,y=5919}, {x=13945,y=5919}, {x=13946,y=5919}, {x=13951,y=5919}, {x=13952,y=5919}, {x=13953,y=5919}, {x=13954,y=5919}, {x=13955,y=5919}, {x=13956,y=5919}}};


OpeningHours.dayCount = 0;
OpeningHours.hourOfDay = 21;
OpeningHours.openTime = false;
OpeningHours.dayToRespawn = 3;

-- Events.OnChallengeQuery.Add(OpeningHours.Add)

