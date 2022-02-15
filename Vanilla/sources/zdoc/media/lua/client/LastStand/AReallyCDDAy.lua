---@class CDDA
CDDA = {}

CDDA.Add = function()
	addChallenge(CDDA);


end

CDDA.OnGameStart = function()
	local pl = getPlayer();

	if pl:getHoursSurvived() > 0 then return end

	local square = pl:getCurrentSquare();
	print(square)
	if not square then return end
	local room = square:getRoom();
	print(room)
	if not room then return end
	local building = room:getBuilding();
	print(building);
	if not building then return end
	
	for i = 0, 4 do

		local tile = building:getRandomRoom():getRandomSquare();

		if(tile:getRoom() == room) then
			i = i - 1;
		else
			print(tile);

			tile:explode();

		end

	end

end

CDDA.OnInitWorld = function()
	SandboxVars.Zombies = 1;
	SandboxVars.Distribution = 1;
	SandboxVars.DayLength = 3;
	SandboxVars.StartMonth = 12;
	SandboxVars.StartTime = 2;
	SandboxVars.WaterShutModifier = -1;
	SandboxVars.ElecShutModifier = -1;
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
	SandboxVars.TimeSinceApo = 13;
	SandboxVars.MultiHitZombies = false;
	

	SandboxVars.ZombieConfig.PopulationMultiplier = 4.0

	-- FIXME: a number of these spawnpoints are invalid :-(
	if false then
	local rand = ZombRand(0, 13) + 1
	CDDA.xcell = CDDA.spawns[rand].xcell;
	CDDA.ycell = CDDA.spawns[rand].ycell;
	CDDA.x = CDDA.spawns[rand].x;
	CDDA.y = CDDA.spawns[rand].y;
	CDDA.z = CDDA.spawns[rand].z;
	print ("Set to :" .. CDDA.x .. ", "..CDDA.y..", ".. CDDA.z)
	end
	
	Events.OnGameStart.Add(CDDA.OnGameStart);


end

CDDA.RemovePlayer = function(p)

end

CDDA.AddPlayer = function(playerNum, playerObj)
	if playerObj:getHoursSurvived() > 0 then return end

	playerObj:getStats():setDrunkenness(100);

	print("adding challenge inventory");
	playerObj:getInventory():clear();
	playerObj:clearWornItems();
	playerObj:getBodyDamage():setWetness(100);
	playerObj:getBodyDamage():setCatchACold(0.0);
	playerObj:getBodyDamage():setHasACold(true);
	playerObj:getBodyDamage():setColdStrength(20.0);
	playerObj:getBodyDamage():setTimeToSneezeOrCough(0);
	playerObj:setClothingItem_Feet(nil)
	playerObj:setClothingItem_Legs(nil)
	playerObj:setClothingItem_Torso(nil)
	playerObj:getBodyDamage():getBodyPart(BodyPartType.Groin):generateDeepShardWound();
end

CDDA.Render = function()

--~ 	getTextManager():DrawStringRight(UIFont.Small, getCore():getOffscreenWidth() - 20, 20, "Zombies left : " .. (EightMonthsLater.zombiesSpawned - EightMonthsLater.deadZombie), 1, 1, 1, 0.8);

--~ 	getTextManager():DrawStringRight(UIFont.Small, (getCore():getOffscreenWidth()*0.9), 40, "Next wave : " .. tonumber(((60*60) - EightMonthsLater.waveTime)), 1, 1, 1, 0.8);
end

CDDA.id = "AReallyCDDAy";
CDDA.completionText = "Survive a night to unlock next challenge.";
CDDA.image = "media/lua/client/LastStand/AReallyCDDAy.png";
CDDA.gameMode = "A Really CD DA";
CDDA.world = "Muldraugh, KY";
CDDA.xcell = 36;
CDDA.ycell = 31;
CDDA.x = 21;
CDDA.y = 111;
CDDA.z = 0;

CDDA.spawns = {
	{xcell = 35, ycell = 33, x = 288, y = 85, z = 0},
	{xcell = 35, ycell = 32, x = 193, y = 145, z = 0},
	{xcell = 35, ycell = 32, x = 270, y = 88, z = 0},
	{xcell = 35, ycell = 31, x = 240+6, y = 107+6, z = 1},
	{xcell = 39, ycell = 22, x = 91, y = 255, z = 0},
	{xcell = 38, ycell = 22, x = 198+6, y = 271+6, z = 1},
	{xcell = 38, ycell = 22, x = 96, y = 100, z = 1},
	{xcell = 38, ycell = 22, x = 15, y = 65, z = 0},
	{xcell = 37, ycell = 22, x = 112+6, y = 185+6, z = 1},
	{xcell = 37, ycell = 22, x = 111+6, y = 191+6, z = 1},
	{xcell = 35, ycell = 22, x = 133+6, y = 194+6, z = 1},
	{xcell = 36, ycell = 33, x = 33+6, y = 247+6, z = 1},
	{xcell = 38, ycell = 22, x = 273, y = 204+6, z = 1},

}

CDDA.hourOfDay = 7;
Events.OnChallengeQuery.Add(CDDA.Add)

