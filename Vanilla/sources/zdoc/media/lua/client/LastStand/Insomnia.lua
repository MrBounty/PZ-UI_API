

InsomniaChallenge = {}
InsomniaChallenge.zombiesSpawned = 0;
InsomniaChallenge.deadZombie = 0;

InsomniaChallenge.Add = function()
--    addChallenge(InsomniaChallenge);
end

function removeInsomnia()
    Events.OnPlayerUpdate.Remove(Insomnia.OnPlayerUpdate);
    Events.OnGameStart.Remove(Insomnia.OnGameStart);
    Events.EveryTenMinutes.Remove(Insomnia.EveryTenMinutes);

    Events.OnDawn.Remove(Insomnia.OnDawn);
    Events.OnDusk.Remove(Insomnia.OnDusk);

end

function injectInsomnia()

    ---@class Insomnia
    Insomnia = {}
    Insomnia.Name = 'Insomnia. Its going to be a long night.';
    Insomnia.Author = 'RegularX';
    Insomnia.Version = 'Uno';

    Insomnia.HerdingRange = 100;
    Insomnia.DayTimeHerdOneIn = 6;

    Insomnia.init = function()
        print('Insomnia:: Starting '..Insomnia.Name);
        print('Insomnia:: By '..Insomnia.Author);
        print('Insomnia:: VersionNumero '..Insomnia.Version);
    end

    Insomnia.init();

    Insomnia.OnPlayerUpdate = function(player)
        player:getStats():setFatigue(0);
        if getPlayer():getModData()["IsNight"] == 0 then
            gameTime = GameTime:getInstance();
            gameTime:setViewDist(1);
        else
            gameTime = GameTime:getInstance();
            gameTime:setViewDist(75);
        end

    end

    Insomnia.OnGameStart = function()
        print("Insomnia:: Setting up Default Gear");
        if getPlayer():getModData()["InsomniaGear"] == nil then
            getPlayer():getInventory():AddItem("Base.BigHikingBag");
            getPlayer():getInventory():AddItem("Base.Axe");
            for i = 0, getPlayer():getInventory():getItems():size() - 1 do
                if getPlayer():getInventory():getItems():get(i) ~= nil and getPlayer():getInventory():getItems():get(i):getType() == "BigHikingBag" then
                    container = getPlayer():getInventory():getItems():get(i);
                    container:getInventory():AddItem("Base.Torch");
                    container:getInventory():AddItem("Base.Battery");
                    container:getInventory():AddItem("Base.Battery");
                    container:getInventory():AddItem("Base.Battery");
                    container:getInventory():AddItem("Base.Battery");
                    container:getInventory():AddItem("Base.Battery");
                    container:getInventory():AddItem("Base.Battery");
                    container:getInventory():AddItem("Base.Nails");
                    container:getInventory():AddItem("Base.Nails");
                    container:getInventory():AddItem("Base.Nails");
                    container:getInventory():AddItem("Base.Nails");
                    container:getInventory():AddItem("Base.Nails");
                    container:getInventory():AddItem("Base.Nails");
                    container:getInventory():AddItem("Base.Nails");
                    container:getInventory():AddItem("Base.Hammer");
                    --	container:getInventory():AddItem("Base.Sledgehammer");
                end
            end

            getPlayer():getModData()["IsNight"] = 0;
            getPlayer():getModData()["InsomniaGear"] = 1;
        end
    end

    Insomnia.OnDawn = function()
        print("Insomnia::DAY HAS RISEN");
        getPlayer():getModData()["IsNight"] = 0;
    end

    Insomnia.OnDusk = function()
        print("Insomnia::NIGHT HAS FALLEN");
        getPlayer():getModData()["IsNight"] = 1;
    end

    Insomnia.ZombHerd = function()
        local player = getPlayer();
        local psquare = player:getSquare();

        if psquare == nil then
            return;
        end

        local zomCount = 0;
        print("Insomnia:: tick tock");
        --	getSoundManager():PlayWorldSound("thump6", false, player:getSquare(), 0, 500, 1, false);

        local roundTheClock = ZombRand(6);
        if roundTheClock == 1 then
            psquare = getWorld():getCell():getGridSquare(player:getSquare():getX() + 35, player:getSquare():getY() + 35, player:getSquare():getZ());
        elseif roundTheClock == 2 then
            psquare = getWorld():getCell():getGridSquare(player:getSquare():getX() - 35, player:getSquare():getY() - 35, player:getSquare():getZ());
        elseif roundTheClock == 3 then
            psquare = getWorld():getCell():getGridSquare(player:getSquare():getX() + 35, player:getSquare():getY() - 35, player:getSquare():getZ());
        elseif roundTheClock == 4 then
            psquare = getWorld():getCell():getGridSquare(player:getSquare():getX() - 35, player:getSquare():getY() + 35, player:getSquare():getZ());
        else
            psquare = player:getSquare();
        end

        if psquare == nil then
            print("Insomnia:: psquare not found");
            psquare = player:getSquare();
        end

        local sq1 = Insomnia.getTilesAround(getWorld():getCell(), psquare, Insomnia.HerdingRange);
        for u,j in pairs(sq1) do
            for k,l in pairs(j:getLuaMovingObjectList()) do
                if luautils.stringStarts(l:toString(), "zombie.characters.IsoZombie") then
                    l:setFakeDead(false);
                    l:setForceFakeDead(false);
                    l:Hit(null,player,0,true,0);
                    zomCount = zomCount + 1;
                end
            end
        end

        print("Insomnia:: "..tostring(zomCount).." zoms tapped");
    end

    Insomnia.EveryTenMinutes = function()
        if getPlayer():getModData()["IsNight"] == 1 or ZombRand(Insomnia.DayTimeHerdOneIn) == 0 then
            Insomnia:ZombHerd();
        end
    end

    Insomnia.getTilesAround = function(cell,startingGrid,range)
        local result = {};
        local rangeX = startingGrid:getX() - range;
        if(rangeX < 0) then
            rangeX = 0;
        end
        local rangeY = startingGrid:getY() - range;
        if(rangeY < 0) then
            rangeY = 0;
        end
        local rangeX2 = startingGrid:getX() + range;
        local rangeY2 = startingGrid:getY() + range;
        for x2=rangeX, rangeX2 do
            for y2=rangeY, rangeY2 do
                local nextGrid = cell:getGridSquare(x2, y2, startingGrid:getZ());
                if nextGrid then
                    table.insert(result, #result, nextGrid);
                end
                if (startingGrid:getZ() > 0) then
                    nextGrid = cell:getGridSquare(x2, y2, startingGrid:getZ() - 1);
                    if nextGrid ~= nil then
                        table.insert(result, #result, nextGrid);
                    end
                end
            end
        end
        return result;
    end

    Events.OnPlayerUpdate.Add(Insomnia.OnPlayerUpdate);
    Events.OnGameStart.Add(Insomnia.OnGameStart);
    Events.EveryTenMinutes.Add(Insomnia.EveryTenMinutes);

    Events.OnDawn.Add(Insomnia.OnDawn);
    Events.OnDusk.Add(Insomnia.OnDusk);



end


InsomniaChallenge.PreloadInit = function()
    --SandboxVars.Zombies = 2;
    SandboxVars.ZombieLore.Speed = 1;
    injectInsomnia();
end

InsomniaChallenge.AddPlayer = function(playerNum, playerObj)

    if getCore():isDedicated() then return end

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

function InsomniaChallenge.RemovePlayer(playerObj)
end

InsomniaChallenge.Init = function()
    InsomniaChallenge.wave = 0;
    InsomniaChallenge.waveTime = 0;
    Events.OnTick.Add(InsomniaChallenge.Tick)
    InsomniaChallenge.FillContainers();
    LastStandData.zombieList = LuaList:new();
end

InsomniaChallenge.Render = function()

--~ 	getTextManager():DrawStringRight(UIFont.Small, getCore():getOffscreenWidth() - 20, 20, "Zombies left : " .. (InsomniaChallenge.zombiesSpawned - InsomniaChallenge.deadZombie), 1, 1, 1, 0.8);

--~ 	getTextManager():DrawStringRight(UIFont.Small, (getCore():getOffscreenWidth()*0.9), 40, "Next wave : " .. tonumber(((60*60) - InsomniaChallenge.waveTime)), 1, 1, 1, 0.8);
end

InsomniaChallenge.Tick = function()
    if getPlayer() == nil then return end;

end

function InsomniaChallenge.onBackButtonWheel(playerNum, dir)
end

InsomniaChallenge.name = "Insomnia";
InsomniaChallenge.description = "Zombies are blind during the day. Bloodhounds after the sun goes down. Its going to be a long night.";
InsomniaChallenge.completionText = "Survive the night to unlock the next challenge.";
InsomniaChallenge.image = "media/lua/client/LastStand/InsomniaChallenge.png";
InsomniaChallenge.gameMode = "Insomnia";
InsomniaChallenge.world = "Muldraugh, KY";
InsomniaChallenge.xcell = 35;
InsomniaChallenge.ycell = 33;
InsomniaChallenge.x = 277;
InsomniaChallenge.y = 271;
InsomniaChallenge.z = 0;
InsomniaChallenge.hourOfDay = 7;
Events.OnChallengeQuery.Add(InsomniaChallenge.Add)
