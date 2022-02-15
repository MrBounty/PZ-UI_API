---@class TutorialData
TutorialData = {}
TutorialData.choosenChallenge = {};

function preLoadTutorialInit()
    if getCore():getGameMode() == "Tutorial" then
        globalTutorial = TutorialData.chosenTutorial;
        getWorld():setLuaSpawnCellX(globalTutorial.xcell);
        getWorld():setLuaSpawnCellY(globalTutorial.ycell);
        getWorld():setLuaPosX(globalTutorial.x);
        getWorld():setLuaPosY(globalTutorial.y);
        getWorld():setLuaPosZ(0);

        globalTutorial.PreloadInit();
    end
end

function doTutorialDraw()
    if globalTutorial ~= nil then
        globalTutorial.Render();
    end

end

function doTutorialInit()
    if getCore():getGameMode() == "Tutorial" then
--        print(" initialising tut");
        getGameTime():setTimeOfDay(globalTutorial.hourOfDay);
        getGameTime():setMinutesPerDay(60 * 24);
        globalTutorial.Init();

    end
end

function savePlayerInFile(player)

end

function doTutorialCreatePlayer(id)
--    print(id);

    -- print(p);
    local pl = getSpecificPlayer(id);

    if getCore():getGameMode() == "Tutorial" then
--        print("Creating player for tut");

        globalTutorial.AddPlayer(pl);
    end

end

Events.OnGameStart.Add(doTutorialInit);
Events.OnPostUIDraw.Add(doTutorialDraw);

Events.OnInitWorld.Add(preLoadTutorialInit);
Events.OnCreatePlayer.Add(doTutorialCreatePlayer);
