

---@class Challenge1
Challenge1 = {}
Challenge1.zombiesSpawned = 0;
Challenge1.deadZombie = 0;

Challenge1.Add = function()
    addChallenge(Challenge1);
end

Challenge1.OnInitWorld = function()
    --SandboxVars.ZombieLore.Speed = 1;
    SandboxVars.DecayingCorpseHealthImpact = 1
    SandboxVars.Map.AllowMiniMap = false
    SandboxVars.Map.AllowWorldMap = false
end

Challenge1.AddPlayer = function(playerNum, playerObj)

    if getCore():isDedicated() then return end

    local pl = playerObj;

	pl:LevelPerk(Perks.Blunt);
	pl:LevelPerk(Perks.Blunt);
	pl:LevelPerk(Perks.Blunt);
	pl:LevelPerk(Perks.Blunt);
	pl:LevelPerk(Perks.Nimble);
	pl:LevelPerk(Perks.Nimble);
	pl:LevelPerk(Perks.Axe);
	pl:LevelPerk(Perks.Axe);
	pl:LevelPerk(Perks.Axe);
	pl:LevelPerk(Perks.Fitness);
	pl:LevelPerk(Perks.Fitness);
	pl:LevelPerk(Perks.Fitness);
	pl:LevelPerk(Perks.Reloading);
	pl:LevelPerk(Perks.Reloading);
	pl:LevelPerk(Perks.Reloading);
	pl:LevelPerk(Perks.Sprinting);
	pl:LevelPerk(Perks.Strength);
	pl:LevelPerk(Perks.Strength);
	pl:LevelPerk(Perks.Aiming);
	pl:LevelPerk(Perks.Aiming);

	luautils.updatePerksXp(Perks.Blunt, pl)
	luautils.updatePerksXp(Perks.Nimble, pl)
	luautils.updatePerksXp(Perks.Axe, pl)
	luautils.updatePerksXp(Perks.Fitness, pl)
	luautils.updatePerksXp(Perks.Reloading, pl)
	luautils.updatePerksXp(Perks.Sprinting, pl)
	luautils.updatePerksXp(Perks.Strength, pl)
	luautils.updatePerksXp(Perks.Aiming, pl)

    print("adding challenge inventory");
  --  local torch = pl:getInventory():AddItem("Base.Torch");

      --local pistol = pl:getInventory():AddItem("Base.Schoolbag");
  --  pl:getInventory():AddItems("Base.ShotgunShells", 5);
 --   torch:setActivated(true);
--    torch:setLightStrength(torch:getLightStrength() / 1.5);
  --  pl:setSecondaryHandItem(torch);
   -- pl:setPrimaryHandItem(pistol);
end

function Challenge1.RemovePlayer(playerObj)
	local playerNum = playerObj:getPlayerNum()
	setAggroTarget(playerNum, -1, -1)
end

Challenge1.Init = function()
    Challenge1.wave = 0;
    Challenge1.waveTime = 0;
    Events.OnTick.Add(Challenge1.Tick)
    Challenge1.FillContainers();
    LastStandData.zombieList = LuaList:new();
end

Challenge1.FillContainers = function()
    for k, v in ipairs(Challenge1.cratePositions) do
        local type = v[1];
        local container = v[2];
        local x = v[3];
        local y = v[4];
        local z = v[5];

        local sq = getCell():getGridSquare(x, y, z);

        if sq ~= nil then
            local objs = sq:getObjects();

            for i = 0, objs:size()-1 do
               local o = objs:get(i);

               local c = o:getContainer();

                if(c ~= nil) then

                    if(c:getType() == container) then
                         if(type == "weapons1") then
	                         c:AddItems("Base.PillsBeta", 4);
                            c:AddItems("Base.Torch", 4);
                         elseif(type == "weapons2") then
                             c:AddItems("Base.Axe", 1);
                         elseif(type == "weapons3") then
                             c:AddItems("Base.Pistol", 1);
                             c:AddItems("Base.9mmClip", 1);
                             c:AddItems("Base.Bullets9mm", 4);
                             c:AddItems("Base.Shotgun", 1);
                             c:AddItems("Base.ShotgunShells", 2);
                             c:AddItems("Base.BaseballBat", 2);
                             c:AddItems("Base.KitchenKnife", 4);
                         elseif(type == "medicine") then
                             c:AddItems("Base.Hammer", 2);
                         elseif(type == "carpentry") then
                             c:AddItems("Base.Hammer", 2);
                             c:AddItems("Base.Nails", 5);
                             c:AddItems("Base.Plank", 10);
                         end

                    end

                    c:setExplored(true);
                end



            end
        end
    end
end

Challenge1.SpawnZombies = function(count)
-- init wave...
    if getCore():isDedicated() then return end

    local player = getSpecificPlayer(0);
    for n = 0, count - 1 do
		while 1 do
			local x = Challenge1.zombieSpawnsRect.x;
			local y = Challenge1.zombieSpawnsRect.y;

			local e = ZombRand(4);

			if e == 0 then x = ZombRand(Challenge1.zombieSpawnsRect.x, Challenge1.zombieSpawnsRect.x2); end
			if e == 1 then x = Challenge1.zombieSpawnsRect.x2;  y = ZombRand(Challenge1.zombieSpawnsRect.y, Challenge1.zombieSpawnsRect.y2); end
			if e == 2 then x = ZombRand(Challenge1.zombieSpawnsRect.x, Challenge1.zombieSpawnsRect.x2); y = Challenge1.zombieSpawnsRect.y2; end
			if e == 3 then y = ZombRand(Challenge1.zombieSpawnsRect.y, Challenge1.zombieSpawnsRect.y2); end

			x = x + (Challenge1.xcell * 300);
			y = y + (Challenge1.ycell * 300);

			-- Implementation detail: VirtualZombieManager will remove any virtual zombies that are too close the the player.
			local dist = IsoUtils.DistanceManhatten(x, y, player:getX(), player:getY())
			if dist > getCell():getWidthInTiles() / 3 + 2 then
				createHordeFromTo(x, y, player:getX(), player:getY(), 2);
				break
			else
				if getDebug() then print('IGNORING TOO-CLOSE SPAWN POINT') end
			end
		end
    end
end

Challenge1.Render = function()

    if Challenge1.alphaTxt > 0 then
        getTextManager():DrawStringCentre(UIFont.Cred1, (getCore():getScreenWidth()*0.5), getCore():getScreenHeight()*0.1, "PREPARE FOR WAVE "..(Challenge1.wave+1), 1, 1, 1, Challenge1.alphaTxt);
		Challenge1.alphaTxt = Challenge1.alphaTxt - 0.01;
    end

--~ 	getTextManager():DrawStringRight(UIFont.Small, getCore():getOffscreenWidth() - 20, 20, "Zombies left : " .. (Challenge1.zombiesSpawned - Challenge1.deadZombie), 1, 1, 1, 0.8);

--~ 	getTextManager():DrawStringRight(UIFont.Small, (getCore():getOffscreenWidth()*0.9), 40, "Next wave : " .. tonumber(((60*60) - Challenge1.waveTime)), 1, 1, 1, 0.8);
end

Challenge1.Tick = function()
    if getPlayer() == nil then return end;

    local prepareTimeEnd = 60*60;
    if Challenge1.wave == 0 then
        prepareTimeEnd = 1;
    end

--~ 	print("wave time : " .. Challenge1.waveTime);
--~ 	print("prepareTime : " .. prepareTimeEnd);

    if Challenge1.waveTime >= prepareTimeEnd and Challenge1.lastWaveTime < prepareTimeEnd then
        print("Wave "..(Challenge1.wave+1).." started.")
        Challenge1.SpawnZombies(Challenge1.spawnCount[Challenge1.wave+1]);
		Challenge1.zombiesSpawned = Challenge1.zombiesSpawned + Challenge1.spawnCount[Challenge1.wave+1];

--~ 		Challenge1.alphaTxt = 1;

        for m = 0, getNumActivePlayers() - 1 do
          local pl = getSpecificPlayer(m);
            if pl then
          addSound(pl, pl:getX(), pl:getY(), pl:getZ(), 300, 300);
            end
        end

    end

    local mul1 = 20;

    if Challenge1.waveTime >= prepareTimeEnd and (Challenge1.waveTime >= prepareTimeEnd + (20*(Challenge1.wave+1)*mul1)) then
        Challenge1.waveTime = 0;
        Challenge1.lastWaveTime = 0;
        Challenge1.wave = Challenge1.wave + 1;
        return;
    end


    for m = 0, getNumActivePlayers() - 1 do
        if ZombRand(500) == 0 then
           -- local pl = getSpecificPlayer(m);

           ---- addSound(pl, pl:getX(), pl:getY(), pl:getZ(), 300, 300);
        end

    end

    Challenge1.lastWaveTime = Challenge1.waveTime;
    Challenge1.waveTime = Challenge1.waveTime + getGameTime():getMultiplier();

	for i=1,getNumActivePlayers() do
		local playerObj = getSpecificPlayer(i-1)
		if playerObj and not playerObj:isDead() then
			setAggroTarget(i-1, playerObj:getX(), playerObj:getY())
			playerObj:getStats():setHunger(0.0)
			playerObj:getStats():setThirst(0.0)
			playerObj:getStats():setFatigue(0.0)
		end
	end
end

function Challenge1.onBackButtonWheel(playerNum, dir)
end

Challenge1.id = "Challenge1";
Challenge1.image = "media/lua/client/LastStand/Challenge1.png";
Challenge1.world = "challengemaps/Challenge1";
Challenge1.xcell = 0;
Challenge1.ycell = 0;
Challenge1.x = 153;
Challenge1.y = 158;
Challenge1.z = 0;
Challenge1.gameMode = "LastStand";
Challenge1.cratePositions = { {"weapons3", "crate", 151, 152, 0},{"weapons2", "crate", 142, 148, 0}, {"weapons1", "crate", 147+3, 151+3, 1}, {"medicine", "crate", 156+3, 144+3, 1}, {"carpentry", "crate", 135, 179, 0}, {"carpentry", "crate", 157, 151, 0}, {"carpentry", "crate", 158, 151, 0}}
Challenge1.spawnCount = {2, 3, 6, 10, 16, 24, 32, 38, 40, 45, 47, 50, 54, 56, 58, 64}
Challenge1.wave = 0;
Challenge1.hourOfDay = 3;
Challenge1.alphaTxt = 0;
Challenge1.waveTime = 0;
Challenge1.lastWaveTime = 0;
Challenge1.zombieSpawnsRect = { x = 114, y = 119, x2 = 192, y2 = 200 }
Events.OnChallengeQuery.Add(Challenge1.Add)
