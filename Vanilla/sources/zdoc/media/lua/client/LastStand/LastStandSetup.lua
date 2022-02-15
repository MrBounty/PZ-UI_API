---@class LastStandData
LastStandData = {}
LastStandData.chosenChallenge = nil;

local function findCurrentChallenge()
    if not getCore():isChallenge() then return nil end
    for _,challenge in ipairs(LastStandChallenge) do
        if challenge.id == getCore():getChallengeID() then
            return challenge
        end
    end
    return nil
end

function preLoadLastStandInit()
    if getCore():isChallenge() then
        LastStandData.chosenChallenge = findCurrentChallenge();

        globalChallenge = LastStandData.chosenChallenge;
        globalChallenge.OnInitWorld();
--[[
        print("setting last stand spawn point");
        getWorld():setLuaSpawnCellX(globalChallenge.xcell);
        getWorld():setLuaSpawnCellY(globalChallenge.ycell);
        getWorld():setLuaPosX(globalChallenge.x);
        getWorld():setLuaPosY(globalChallenge.y);
        getWorld():setLuaPosZ(globalChallenge.z);
--]]
        Events.OnGameStart.Add(doLastStandInit);
        Events.OnPostUIDraw.Add(doLastStandDraw);
        Events.OnCreatePlayer.Add(doLastStandCreatePlayer);
        Events.OnPlayerDeath.Add(doLastStandPlayerDeath);
    end
end

function doLastStandDraw()
    if globalChallenge ~= nil then
        globalChallenge.Render();
    end

end

local function loadLastStandPlayerVisuals(playerObj)
	local playerTbl = LastStandPlayerSelect.playerSelected
	if not playerTbl then
		-- if it doesn't exist that's because we created a new player
		return
	end
	if playerTbl.humanVisual then
		playerObj:getHumanVisual():loadLastStandString(playerTbl.humanVisual)
		playerObj:getDescriptor():getHumanVisual():copyFrom(playerObj:getHumanVisual())
	end
	if playerTbl.clothingVisuals then
		-- We have to do this after GameLoadingState so all ClothingItems are loaded.
		for _,clothing in ipairs(playerTbl.clothingVisuals) do
			local item = ItemVisual.createLastStandItem(clothing)
			if item then
				playerObj:getInventory():AddItem(item)
				playerObj:setWornItem(item:getBodyLocation(), item)
			end
		end
	end
	-- Clear these so only the original player is affected, not respawns/splitscreen.
	playerTbl.humanVisual = nil
	playerTbl.clothingVisuals = nil
end

function doLastStandInit()
    if getCore():getGameMode() == "LastStand" then
        print("initialising last stand");
        loadLastStandPlayerVisuals(getSpecificPlayer(0))
        getGameTime():setTimeOfDay(globalChallenge.hourOfDay);
        getGameTime():setMinutesPerDay(60 * 24);
        globalChallenge.Init();
		-- save the selected player to a text file
		-- at this stage, we only have 1 player, we're gonna save the other player if they are added via the controller
		saveLastStandPlayerInFile(getPlayer());
    end
end

function saveLastStandPlayerInFile(player)
	local playerTbl = LastStandPlayerSelect.playerSelected
	if not playerTbl then
		-- if it doesn't exist that's because we created a new player
		playerTbl = {}
		playerTbl.playedTime = player:getModData()["playedTime"] or 1
	end
	if not player:getModData()["challenge2Level"] then
		player:getModData()["challenge2Level"] = playerTbl.level or 0;
	end
	if not player:getModData()["challenge2Xp"] then
		player:getModData()["challenge2Xp"] = playerTbl.globalXp or 0;
	end
	if not player:getModData()["challenge2BoostGoldLevel"] then
		player:getModData()["challenge2BoostGoldLevel"] = playerTbl.boostGoldLevel or 1;
	end
	if not player:getModData()["challenge2BoostXpLevel"] then
		player:getModData()["challenge2BoostXpLevel"] = playerTbl.boostXpLevel or 1;
	end
	if not player:getModData()["challenge2StartingGoldLevel"] then
		player:getModData()["challenge2StartingGoldLevel"] = playerTbl.startingGoldLevel or 0;
	end
	local playedTime = playerTbl.playedTime or 1;
	player:getModData()["playedTime"] = playedTime;
	local fileOutput = getFileWriter(getLastStandPlayersDirectory() .. "/player" .. player:getDescriptor():getForename() .. player:getDescriptor():getSurname() .. ".txt", true, false);
	fileOutput:writeln("VERSION=1");
	fileOutput:writeln("Player" .. player:getDescriptor():getForename() .. player:getDescriptor():getSurname());
	fileOutput:writeln("{");
	fileOutput:writeln("  Forename=" .. player:getDescriptor():getForename());
	fileOutput:writeln("  Surname=" .. player:getDescriptor():getSurname());
	fileOutput:writeln("  Profession=" .. player:getDescriptor():getProfession());
	fileOutput:writeln("  Visual=" .. player:getHumanVisual():getLastStandString());
	fileOutput:writeln("  Level=" .. player:getModData()["challenge2Level"]);
	fileOutput:writeln("  GlobalXp=" .. player:getModData()["challenge2Xp"]);
	fileOutput:writeln("  PlayedTime=" .. player:getModData()["playedTime"]);
	if player:getDescriptor():isFemale() then
		fileOutput:writeln("  Female");
	else
		fileOutput:writeln("  Male");
	end
	fileOutput:writeln("}");
	fileOutput:writeln("Clothing");
	fileOutput:writeln("{");
	local itemVisuals = ItemVisuals.new()
	getPlayer():getItemVisuals(itemVisuals)
	for i=1,itemVisuals:size() do
		local saveString = itemVisuals:get(i-1):getLastStandString()
		if saveString then
			fileOutput:writeln("  clothing="..saveString)
		end
	end
	fileOutput:writeln("}");
	fileOutput:writeln("Traits");
	fileOutput:writeln("{");
	for i=0,player:getTraits():size() -1 do
		local trait = player:getTraits():get(i);
		fileOutput:writeln("  addTrait="..trait);
	end
	fileOutput:writeln("}");
	fileOutput:writeln("Skills");
	fileOutput:writeln("{");
	fileOutput:writeln("  addSkills=Blunt," .. player:getPerkLevel(Perks.Blunt));
	fileOutput:writeln("  addSkills=Blade," .. player:getPerkLevel(Perks.Axe));
	fileOutput:writeln("  addSkills=Carpentry," .. player:getPerkLevel(Perks.Woodwork));
	fileOutput:writeln("}");
	fileOutput:writeln("Bonus");
	fileOutput:writeln("{");
	fileOutput:writeln("  addGoldBoostBonus=" .. player:getModData()["challenge2BoostGoldLevel"]);
	fileOutput:writeln("  addStartingGoldBonus=" .. player:getModData()["challenge2StartingGoldLevel"]);
	fileOutput:writeln("  addXpBoostBonus=" .. player:getModData()["challenge2BoostXpLevel"]);
	fileOutput:writeln("}");
	fileOutput:close();
end

function doLastStandCreatePlayer(playerNum, playerObj)
    if getCore():getGameMode() == "LastStand" then
        print("Creating player for last stand");
        if id == 0 then
            loadLastStandPlayerVisuals(playerObj)
        end
    end
    globalChallenge.AddPlayer(playerNum, playerObj);
end

function doLastStandPlayerDeath(playerObj)
    if globalChallenge then
        globalChallenge.RemovePlayer(playerObj)
    end
end

function doLastStandBackButtonWheel(playerNum, dir)
	if globalChallenge then
		globalChallenge.onBackButtonWheel(playerNum, dir)
	end
end

function LastStandData.getSpawnRegion()
    local c = findCurrentChallenge()
    if not c then return nil end
    if c.getSpawnRegion then
        return c.getSpawnRegion()
    end
    return { {
        name = c.name, points = {
            unemployed = {
                { worldX = c.xcell, worldY = c.ycell, posX = c.x, posY = c.y, posZ = c.z },
            }
        }
    } }
end

Events.OnInitWorld.Add(preLoadLastStandInit);
