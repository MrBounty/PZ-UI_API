

---@class Challenge2
Challenge2 = {}
Challenge2.zombiesSpawned = 0;
Challenge2.startedWaveCalendar = nil;
Challenge2.prepareTimeCalendar = nil;
Challenge2.timeNeeded = nil;
Challenge2.timeNeededTick = 0;
Challenge2.sdf = SimpleDateFormat.new("mm:ss");
Challenge2.deadZombie = 0;
Challenge2.prepareTime = 2 * 60 * 1000; -- 2 minutes
Challenge2.playersMoney = {};
Challenge2.timeNeededAlpha = 1;
Challenge2.wave = 0;
Challenge2.upgradeScreen = {}
Challenge2.radarPanel = {}

Challenge2.Add = function()
    addChallenge(Challenge2);
end

Challenge2.OnInitWorld = function()
  --  SandboxVars.ZombieLore.Speed = 1;
    SandboxVars.DecayingCorpseHealthImpact = 1
    SandboxVars.Map.AllowMiniMap = false
    SandboxVars.Map.AllowWorldMap = false
end

local function ensureMinPerkLevel(player, perk, level)
	while player:getPerkLevel(perk) < level do
		player:LevelPerk(perk);
		luautils.updatePerksXp(perk, player);
	end
end

Challenge2.AddPlayer = function(playerNum, playerObj)

    if getCore():isDedicated() then return end

	local p = playerNum;
	local pl = playerObj;

	Challenge2.playersMoney[p] = 100 + (pl:getModData()["challenge2StartingGoldLevel"] or 0);

	ensureMinPerkLevel(pl, Perks.Strength, 7);
	ensureMinPerkLevel(pl, Perks.Fitness, 7);

	saveLastStandPlayerInFile(pl);

	Challenge2.radarPanel[p] = RadarPanel.new(p)
	UIManager.AddUI(Challenge2.radarPanel[p])
end

function Challenge2.RemovePlayer(playerObj)
	local playerNum = playerObj:getPlayerNum()
	if Challenge2.upgradeScreen[playerNum] then
		Challenge2.upgradeScreen[playerNum]:removeFromUIManager()
		Challenge2.upgradeScreen[playerNum] = nil
	end
	setAggroTarget(playerNum, -1, -1)
	if Challenge2.radarPanel[p] then
		UIManager.RemoveUI(Challenge2.radarPanel[p])
		Challenge2.radarPanel[p] = nil
	end
end

Challenge2.Init = function()
	Challenge2.wave = 0;
    Events.OnTick.Add(Challenge2.Tick)
	Events.OnZombieDead.Add(Challenge2.onZombieDead)
	Events.OnKeyPressed.Add(Challenge2.onKeyPressed);
end

Challenge2.SpawnZombies = function(count)
-- init wave...
    if getCore():isDedicated() then return end

    local player = getSpecificPlayer(0);
    for n = 0, count - 1 do
		local x = Challenge2.zombieSpawnsRect.x;
		local x2 = Challenge2.zombieSpawnsRect.x2;
		local y = Challenge2.zombieSpawnsRect.y;
		local y2 = Challenge2.zombieSpawnsRect.y2;

		local e = ZombRand(4);

		if e == 0 then y2 = y + 1; end -- north edge
		if e == 1 then y = y2 - 1; end -- south edge
		if e == 2 then x2 = x + 1; end -- west edge
		if e == 3 then x = x2 - 1; end -- east edge

		x = x + (Challenge2.xcell * 300);
		x2 = x2 + (Challenge2.xcell * 300);
		y = y + (Challenge2.ycell * 300);
		y2 = y2 + (Challenge2.ycell * 300);

		createHordeInAreaTo(x, y, x2 - x, y2 - y, player:getX(), player:getY(), 1);
    end
end

Challenge2.Render = function()
	getTextManager():DrawStringRight(UIFont.Small, (getCore():getScreenWidth()*0.9), 20, getText("Challenge_Challenge2_PressKey"), 1, 1, 1, 0.8);
	-- draw the preparation time left
	if Challenge2.prepareTimeCalendar then
		local now = Calendar.getInstance();
		now:setTimeInMillis(Challenge2.prepareTimeCalendar:getTimeInMillis() - now:getTimeInMillis());
		getTextManager():DrawStringRight(UIFont.Small, (getCore():getScreenWidth()*0.9), 40, getText("Challenge_Challenge2_WaveStart", Challenge2.wave, Challenge2.sdf:format(now:getTime())), 1, 1, 1, 0.8);
	end

	-- draw the number of zombies left on the map
	if Challenge2.zombiesSpawned > 0 then
		getTextManager():DrawStringRight(UIFont.Small, (getCore():getScreenWidth()*0.9), 40, getText("Challenge_Challenge2_ZombiesLeft", Challenge2.zombiesSpawned), 1, 1, 1, 0.8);
	end

	-- draw how much time you took to kill all the zeds of this wave
	if Challenge2.timeNeeded then
		Challenge2.timeNeededTick = Challenge2.timeNeededTick + 1;
		getTextManager():DrawStringCentre(UIFont.Large, getCore():getScreenWidth()*0.5, getCore():getScreenHeight()*0.5 - 40, getText("Challenge_Challenge2_WaveFinished", Challenge2.wave - 1, Challenge2.sdf:format(Challenge2.timeNeeded:getTime())), 1, 1, 1, Challenge2.timeNeededAlpha);
		getTextManager():DrawStringCentre(UIFont.Large, getCore():getScreenWidth()*0.5, getCore():getScreenHeight()*0.5     , getText("Challenge_Challenge2_MoneyGained", Challenge2.moneyGained), 1, 1, 1, Challenge2.timeNeededAlpha);
		getTextManager():DrawStringCentre(UIFont.Large, getCore():getScreenWidth()*0.5, getCore():getScreenHeight()*0.5 + 40, getText("Challenge_Challenge2_XPGained", Challenge2.xpGained), 1, 1, 1, Challenge2.timeNeededAlpha);
		if Challenge2.timeNeededTick >= 3 then
			Challenge2.timeNeededTick = 0;
			Challenge2.timeNeededAlpha = Challenge2.timeNeededAlpha - 0.02;
			if Challenge2.timeNeededAlpha <= 0 then
				Challenge2.timeNeededAlpha = 1;
				Challenge2.timeNeeded = nil;
				Challenge2.moneyGained = 0;
				Challenge2.xpGained = 0;
			end
		end
	end

	-- render all the money the player have
	y = 60;
	for i = 0,getNumActivePlayers() - 1 do
		local playerObj = getSpecificPlayer(i)
		if playerObj and not playerObj:isDead() then
			if getNumActivePlayers() == 1 then
				getTextManager():DrawStringRight(UIFont.Small, (getCore():getScreenWidth()*0.9), y, getText("Challenge_Challenge2_Money", Challenge2.playersMoney[i]), 1, 1, 1, 0.8);
			else
				getTextManager():DrawStringRight(UIFont.Small, (getCore():getScreenWidth()*0.9), y, getText("Challenge_Challenge2_Money2", i, Challenge2.playersMoney[i]), 1, 1, 1, 0.8);
			end
			y = y + 20;
		end
	end

	-- draw how much time you're in this wave
	if Challenge2.startedWaveCalendar then
		local now = Calendar.getInstance();
		now:setTimeInMillis(now:getTimeInMillis() - Challenge2.startedWaveCalendar:getTimeInMillis());
		getTextManager():DrawStringRight(UIFont.Small, (getCore():getScreenWidth()*0.9), y, getText("Challenge_Challenge2_WaveDuration", Challenge2.wave, Challenge2.sdf:format(now:getTime())), 1, 1, 1, 0.8);
	end
end

Challenge2.endWave = function()
	Challenge2.timeNeeded = Calendar.getInstance();
	Challenge2.timeNeeded:setTimeInMillis(Calendar.getInstance():getTimeInMillis() - Challenge2.startedWaveCalendar:getTimeInMillis());
	Challenge2.startedWaveCalendar = nil;
	-- add some money to your account, depending on how much time it took you to kill all the zeds + number of zeds on the map
	for i = 0,getNumActivePlayers() - 1 do
		local player = getSpecificPlayer(i)
		if player then
		-- money calcul
		Challenge2.moneyGained = math.floor(((0.2 / (Challenge2.timeNeeded:getTimeInMillis() / 1000)) * 10000) + (1.2 * Challenge2.wave * 20));
		-- add the boost of gold if you have the perk
		Challenge2.moneyGained = Challenge2.moneyGained + math.floor((Challenge2.moneyGained * (tonumber(getSpecificPlayer(i):getModData()["challenge2BoostGoldLevel"]) / 100)));
		local goldPerZed = math.floor((10 + (10 * (tonumber(getSpecificPlayer(i):getModData()["challenge2BoostGoldLevel"]) / 100))));
		-- add the money won for finishing the wave
		Challenge2.playersMoney[i] = Challenge2.playersMoney[i] + Challenge2.moneyGained;
		-- calcul the money gained with the killed zed to display it
		Challenge2.moneyGained = Challenge2.moneyGained + (Challenge2.deadZombie * goldPerZed);
		Challenge2.deadZombie = 0;
		-- xp calcul
		Challenge2.xpGained = math.floor(((0.1 / (Challenge2.timeNeeded:getTimeInMillis() / 1000)) * 10000) + (1.1 * Challenge2.wave * 5));
		-- add the boost of xp if you have the perk
		Challenge2.xpGained = Challenge2.xpGained + math.floor((Challenge2.xpGained * (tonumber(getSpecificPlayer(i):getModData()["challenge2BoostXpLevel"]) / 100)));
		-- add xp to your player
		getSpecificPlayer(i):getModData()["challenge2Xp"] = getSpecificPlayer(i):getModData()["challenge2Xp"] + Challenge2.xpGained;
		end
	end
	-- save your player's stats
	for i = 0,getNumActivePlayers() - 1 do
		local player = getSpecificPlayer(i)
		if player and not player:isDead() then
			saveLastStandPlayerInFile(getSpecificPlayer(i));
		end
		if Challenge2.upgradeScreen[i] then
			Challenge2.upgradeScreen[i]:reloadButtons();
		end
	end
end

Challenge2.Tick = function()
    if getPlayer() == nil then return end;

	-- calcul the preparation time you have (after that, the wave start)
	-- only if you just started, or if you killed all the zeds
	if not Challenge2.prepareTimeCalendar and Challenge2.zombiesSpawned == 0 then
		Challenge2.prepareTimeCalendar = Calendar.getInstance();
		-- for the first wave, you only have 1 min to prepare yourself
		if Challenge2.wave == 0 then
			Challenge2.prepareTimeCalendar:setTimeInMillis(Challenge2.prepareTimeCalendar:getTimeInMillis() + (1 * 60 * 1000));
		else
			Challenge2.prepareTimeCalendar:setTimeInMillis(Challenge2.prepareTimeCalendar:getTimeInMillis() + Challenge2.prepareTime);
		end
		Challenge2.wave = Challenge2.wave + 1;
		-- calcul the time you took to finish this wave
		if Challenge2.startedWaveCalendar then
			Challenge2.endWave();
		end
	end

	-- preparation time ongoing, if it's done we spawn zeds
	if Challenge2.prepareTimeCalendar then
		local now = Calendar.getInstance():getTimeInMillis();
		-- preparation over, let's spawn !
		if now >= Challenge2.prepareTimeCalendar:getTimeInMillis() then
			Challenge2.startedWaveCalendar = Calendar.getInstance();
			Challenge2.prepareTimeCalendar = nil;

			Challenge2.zombiesSpawned = Challenge2.spawnCount[Challenge2.wave];
			if not Challenge2.zombiesSpawned then
				local max = #Challenge2.spawnCount
				Challenge2.zombiesSpawned = 60 + (Challenge2.wave - max) * 10
			end
			Challenge2.SpawnZombies(Challenge2.zombiesSpawned);

			for m = 0, getNumActivePlayers() - 1 do
			  local pl = getSpecificPlayer(m);
			  if pl then
				addSound(pl, pl:getX(), pl:getY(), pl:getZ(), 300, 300);
			  end
			end
		end
	end

	-- handle VirtualZombieManager sometimes removing virtual zombies
--	if getVirtualAndRealZombieCount() < Challenge2.zombiesSpawned then
--		print('replacement zombies spawning')
--		Challenge2.SpawnZombies(Challenge2.zombiesSpawned - getVirtualAndRealZombieCount())
--	end

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

-- lower the global zombies left
Challenge2.onZombieDead = function()
	if Core.isLastStand() then
		Challenge2.zombiesSpawned = Challenge2.zombiesSpawned - 1;
		Challenge2.deadZombie = Challenge2.deadZombie + 1;
		-- every zombie killed, you gain 10 money
		for i = 0,getNumActivePlayers() - 1 do
			local playerObj = getSpecificPlayer(i)
			if playerObj then
	--~ 		getSpecificPlayer(i):getModData()["challenge2Xp"] = getSpecificPlayer(i):getModData()["challenge2Xp"] + 10;
				Challenge2.playersMoney[i] = math.floor(Challenge2.playersMoney[i] + (10 + (10 * (tonumber(playerObj:getModData()["challenge2BoostGoldLevel"]) / 100))));
			end
			if Challenge2.upgradeScreen[i] then
				Challenge2.upgradeScreen[i]:reloadButtons();
			end
		end
	end
end

local function showUpgradeScreen(playerNum)
	if not Challenge2.upgradeScreen[playerNum] then
		local x = getPlayerScreenLeft(playerNum)
		local y = getPlayerScreenTop(playerNum)
		Challenge2.upgradeScreen[playerNum] = ISChallenge2UpgradeTab:new(x+70,y+50,320,608,playerNum)
		Challenge2.upgradeScreen[playerNum]:initialise()
		Challenge2.upgradeScreen[playerNum]:addToUIManager()
		Challenge2.upgradeScreen[playerNum]:setVisible(false)
	end
	Challenge2.upgradeScreen[playerNum]:reloadButtons()
	if Challenge2.upgradeScreen[playerNum]:getIsVisible() then
		Challenge2.upgradeScreen[playerNum]:setVisible(false)
	else
		Challenge2.upgradeScreen[playerNum]:setVisible(true)
	end

	local joypadData = JoypadState.players[playerNum+1]
	if joypadData then
		if Challenge2.upgradeScreen[playerNum]:getIsVisible() then
			joypadData.focus = Challenge2.upgradeScreen[playerNum]
		else
			joypadData.focus = nil
		end
	end
end

Challenge2.onKeyPressed = function(key)
	if Core.isLastStand() then
--~ 		if key == getCore():getKey("Equip/Unequip Handweapon") then
--~ 			if Challenge2.zombiesSpawned > 0 then
--~ 				Challenge2.onZombieDead();
--~ 			end
--~ 		end

		if key == Keyboard.KEY_O then
			if getSpecificPlayer(0) and not getSpecificPlayer(0):isDead() then
				showUpgradeScreen(0)
			end
		end
 
		-- skip the preparation time by pressing enter
		if key == Keyboard.KEY_RETURN and Challenge2.zombiesSpawned == 0 then
			Challenge2.prepareTimeCalendar = Calendar.getInstance();
		end
	end
end

Challenge2.onCreatePlayer = function(playerId)
--~ 	getSpecificPlayer(self.playerId):getModData()["challenge2StartingGoldLevel"];
end

function Challenge2.onBackButtonWheel(playerNum, dir)
	if getSpecificPlayer(playerNum) and not getSpecificPlayer(playerNum):isDead() then
		showUpgradeScreen(playerNum)
	end
end

Challenge2.id = "Challenge2";
Challenge2.image = "media/lua/client/LastStand/Challenge2.png";
Challenge2.world = "challengemaps/Challenge1";
Challenge2.xcell = 0;
Challenge2.ycell = 0;
Challenge2.x = 153;
Challenge2.y = 158;
Challenge2.z = 0;
Challenge2.gameMode = "LastStand";
Challenge2.spawnCount = {3, 6, 10, 16, 24, 32, 38, 40, 45, 47, 50, 54, 56, 58, 64};
Challenge2.wave = 0;
Challenge2.hourOfDay = 3;
Challenge2.alphaTxt = 0;
Challenge2.waveTime = 0;
Challenge2.lastWaveTime = 0;
Challenge2.zombieSpawnsRect = { x = 114, y = 119, x2 = 192, y2 = 200 }

Events.OnChallengeQuery.Add(Challenge2.Add)

