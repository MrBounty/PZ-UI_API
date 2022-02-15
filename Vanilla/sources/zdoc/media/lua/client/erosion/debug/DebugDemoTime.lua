--[[
  UtilsDemoTime.lua - 2014.
  Author: Kees "TurboTuTone" Bekkema.
  ]]
local screenX,screenY = 0,0;
local function screenInit() screenX,screenY = getCore():getScreenWidth(), getCore():getScreenHeight(); end
local function screenSetter( _ox, _oy, x, y ) screenX, screenY = x,y; end
Events.OnGameBoot.Add( screenInit );
Events.OnResolutionChange.Add( screenSetter );

local instance = false;

EDebug = EDebug or {};

function EDebug.DemoTime( _erosionMain )
    if instance then
        return instance;
    end

    local publ              = {};
    local priv              = {};

    priv.ErosionMain        = _erosionMain;
    priv.startMonth         = _erosionMain:getConfig():getDebug():getStartMonth();
    priv.startDay           = _erosionMain:getConfig():getDebug():getStartDay();
    priv.Seasons            = priv.ErosionMain:getSeasons();
    priv.tmpCounter 		= 0;
    priv.isRunning			= true;

    priv.processSpeed 		= 10;

    -- classes and functions
    priv.Point 				= Point2D;
    priv.ChunkReader 		= false;
    priv.Rand				= ZombRand;
    priv.Floor				= math.floor;

    -- chunks
    priv.chunk				= false;
    priv.chunkPos			= false;
    priv.chunkModData		= false;
    priv.gameTime           = false;
    priv.textMngr           = false;
    priv.chunkTileWidth 	= 10;
    priv.p_chunkCurrent 	= priv.Point:new(-100000, -100000);
    priv.p_chunkLast 		= priv.Point:new(-100000, -100000);
    priv.p_chunkDirCenter 	= priv.Point:new(-100000, -100000);
    priv.p_chunkMoveDir 	= priv.Point:new(0, 0);
    priv.p_chunkBox 		= priv.Point:new(1, 1);

    priv.p_workingOn 	    = priv.Point:new(0, 0);

    priv.globalErosion		= 100;
    priv.newTick            = 0;
    priv.tick               = 0;

    priv.monthDays          = { 30, 27, 30, 29, 30, 29, 30, 30, 29, 30, 29, 30 };

    priv.txt                = {};
    priv.pause              = true;
    priv.dispMap            = true;
    priv.dispDebug          = true;
    priv.dispText           = true;
    priv.disptext1          = "";
    priv.disptext2          = "";

    function priv.keyPressed( _key )
        --map on/off = o, 24, pause = p, 25, freq back/forward = [], 26/27, debugtext on/off = L, 38, text on/off = K, 37
        print("pressed key = "..tostring(_key));
        if _key == Keyboard.KEY_P then
            priv.pause = not priv.pause;
        elseif _key == Keyboard.KEY_O then
            priv.dispMap = not priv.dispMap;
        elseif _key == Keyboard.KEY_RBRACKET or _key == Keyboard.KEY_LBRACKET  then
            if _key == Keyboard.KEY_RBRACKET then
                priv.gameTime:setMonth(priv.gameTime:getMonth() + 1);
                if priv.gameTime:getMonth() >= 12 then
                    priv.gameTime:setMonth(0);
                    priv.gameTime:setYear(priv.gameTime:getYear() + 1);
                end
            elseif _key == Keyboard.KEY_LBRACKET then
                priv.gameTime:setMonth(priv.gameTime:getMonth() - 1);
                if priv.gameTime:getMonth() < 0 then
                    priv.gameTime:setMonth(11);
                    priv.gameTime:setYear(priv.gameTime:getYear() - 1);
                end
            end
            if priv.gameTime:getDay() >= priv.gameTime:daysInMonth(priv.gameTime:getYear(), priv.gameTime:getMonth()) then
                priv.gameTime:setDay(priv.gameTime:daysInMonth(priv.gameTime:getYear(), priv.gameTime:getMonth())-1);
            end
            --priv.ErosionMain:mainTimer();
        elseif _key == Keyboard.KEY_L then
            priv.dispDebug = not priv.dispDebug;
        elseif _key == Keyboard.KEY_K then
            priv.dispText = not priv.dispText;
        end
    end

    function publ.GameStart()
        priv.gameTime				= GameTime:getInstance();
        priv.gameTime:setDay( priv.startDay );
        priv.gameTime:setMonth( priv.startMonth );
        priv.textMngr 		        = getTextManager();

        -- set starting position
        priv.ChunkReader 			= EDebug.ChunkReader( priv.chunkTileWidth );

        local chunkX 				= priv.Floor( getPlayer():getX() / priv.chunkTileWidth);
        local chunkY 				= priv.Floor( getPlayer():getY() / priv.chunkTileWidth);
        priv.p_chunkCurrent 		= priv.Point:new( chunkX, chunkY );
        priv.p_chunkLast 			= priv.Point:new( chunkX, chunkY );
        priv.p_chunkDirCenter 		= priv.Point:new( chunkX, chunkY );

        -- set chunkreader start pos
        priv.ChunkReader.SetNextRing( 0 );
        priv.ChunkReader.ResetChunkReader( priv.p_chunkCurrent, priv.p_chunkMoveDir );

        --Events.EveryTenMinutes.Add( self_MainTimer );
        Events.OnPlayerUpdate.Add( 	priv.UpdatePlayerChunk 	);
        Events.OnTick.Add( 			priv.DebugQue 		);
        Events.EveryTenMinutes.Add( priv.timeReset );
        Events.OnPreUIDraw.Add( 	priv.draw 		);
        Events.OnKeyPressed.Add(	priv.keyPressed			);

        priv.txt[1] = "CurrentTick: " .. tostring(0);
        priv.updateTxt()
        --self_isRunning 				= true;
        return true;
    end

    function priv.roundStr(num, idp)
        return tostring( string.format("%." .. (idp or 0) .. "f", num) );
    end

    function priv.updateTxt()
        priv.txt[1] = "CurrentTick: " .. tostring(priv.ErosionMain:getEtick()) .. " - db " .. tostring(priv.tick);
        priv.txt[2] = "WinterMaxDayLight hours: " .. priv.roundStr( priv.Seasons:getMaxDaylightWinter(), 2 );
        priv.txt[3] = "SummerMaxDayLight hours: " .. priv.roundStr( priv.Seasons:getMaxDaylightSummer(), 2 );
        priv.txt[4] = "Dusk time: " .. priv.roundStr( priv.Seasons:getDusk(), 2 );
        priv.txt[5] = "Dawn time: " .. priv.roundStr( priv.Seasons:getDawn(), 2 );
        priv.txt[6] = "Daylight hours:" .. priv.roundStr( priv.Seasons:getDaylight(), 2 );
        priv.txt[7] = "Temperature: " .. priv.roundStr( priv.Seasons:getDayTemperature(), 2 );
        priv.txt[8] = "Mean Temperature: " .. priv.roundStr( priv.Seasons:getDayMeanTemperature(), 2 );
        priv.txt[9] = "Season: " .. tostring( priv.Seasons:getSeasonName() );
        priv.txt[10] = "Season day: " .. priv.roundStr( priv.Seasons:getSeasonDay() );
        priv.txt[11] = "Season total days: " .. priv.roundStr( priv.Seasons:getSeasonDays() );
        priv.txt[12] = "Season strength: " .. priv.roundStr( priv.Seasons:getSeasonStrength(), 3 );
        priv.txt[13] = "Day noise value: " .. priv.roundStr( priv.Seasons:getDayNoiseVal(), 3 );
        priv.txt[14] = "isRainDay: " .. tostring( priv.Seasons:isRainDay() );
        priv.txt[15] = "rainDayStrength: " .. priv.roundStr( priv.Seasons:getRainDayStrength(), 3 );
        priv.txt[16] = "Average Rain days per year: " .. tostring( priv.Seasons:getRainYearAverage() );
        priv.txt[17] = "isThunderDay: " .. tostring( priv.Seasons:isThunderDay() );
        priv.txt[18] = "isSunnyDay: " .. tostring( priv.Seasons:isSunnyDay() );

        local date = tostring(priv.gameTime:getDay()+1).."-"..tostring(priv.gameTime:getMonth()+1).."-"..tostring(priv.gameTime:getYear());
        local perc = priv.ErosionMain:getEtick() <=100 and priv.ErosionMain:getEtick() or 100
        priv.disptxt1 = "Day: "..date..", Nature reclamation: " .. tostring( perc ).. "%";

        local str = "Extreme";
        local strval = priv.Seasons:getSeasonStrength();
        if strval > 0.8 then
            str = "Extremely strong";
        elseif strval > 0.6 then
            str = "Very strong";
        elseif strval > 0.4 then
            str = "Strong";
        elseif strval > 0.2 then
            str = "Above average";
        elseif strval > -0.2 then
            str = "Average";
        elseif strval > -0.4 then
            str = "Below average";
        elseif strval > -0.6 then
            str = "Weak";
        elseif strval > -0.8 then
            str = "Very weak";
        else
            str = "Extremely weak";
        end
        priv.disptxt2 = str .. " " .. tostring( priv.Seasons:getSeasonName() ) .. ", elapsed: " .. priv.roundStr( (priv.Seasons:getSeasonDay()/priv.Seasons:getSeasonDays())*100, 2 ) .. "%";
    end

    local coltable = { Winter = {r=1.0,g=1.0,b=1.0}, Spring = {r=1.0,g=1.0,b=0.0}, Summer = {r=0.0,g=1.0,b=0.0}, Autumn = {r=1.0,g=0.0,b=0.0} };
    function priv.draw()
        if priv.dispDebug then
            local px, py = 30, 200;
            for i = 1, 18, 1 do
                local cpy = py + (16 * (i-1));
                priv.textMngr:DrawString(UIFont.Small, px-1, cpy-1, priv.txt[i], 0.0, 0.0, 0.0, 0.5);
                priv.textMngr:DrawString(UIFont.Small, px+1, cpy+1, priv.txt[i], 0.0, 0.0, 0.0, 0.5);
                priv.textMngr:DrawString(UIFont.Small, px, cpy, priv.txt[i], 1.0, 1.0, 1.0, 1.0);
            end
        end
        if priv.dispMap then
            priv.drawChunks();
        end
        if priv.dispText then
            local centerX, centerY = screenX/2,screenY/2;
            local y, txt = screenY - 150, priv.disptxt1;
            priv.textMngr:DrawStringCentre(UIFont.Large, centerX-1, y-1, txt, 0.0, 0.0, 0.0, 1.0);
            priv.textMngr:DrawStringCentre(UIFont.Large, centerX+1, y+1, txt, 0.0, 0.0, 0.0, 1.0);
            priv.textMngr:DrawStringCentre(UIFont.Large, centerX, y, txt, 1.0, 1.0, 1.0, 1.0);

            local c = coltable[priv.Seasons:getSeasonName()] or {r=1.0,g=0.0,b=0.0} ;
            local y, txt = screenY - 125, priv.disptxt2;
            priv.textMngr:DrawStringCentre(UIFont.Large, centerX-1, y-1, txt, 0.0, 0.0, 0.0, 1.0);
            priv.textMngr:DrawStringCentre(UIFont.Large, centerX+1, y+1, txt, 0.0, 0.0, 0.0, 1.0);
            priv.textMngr:DrawStringCentre(UIFont.Large, centerX, y, txt, c.r, c.g, c.b, 1.0);
        end
        if priv.pause then
            local centerX, centerY = screenX/2,screenY/2;
            local y, txt = screenY-200, "Simulation paused, press P to toggle";
            priv.textMngr:DrawStringCentre(UIFont.Large, centerX-1, y-1, txt, 0.0, 0.0, 0.0, 1.0);
            priv.textMngr:DrawStringCentre(UIFont.Large, centerX+1, y+1, txt, 0.0, 0.0, 0.0, 1.0);
            priv.textMngr:DrawStringCentre(UIFont.Large, centerX, y, txt, 1.0, 0.0, 0.0, 1.0);
        end
    end

    function priv.drawChunks()
        --priv.p_workingOn.x
        local cx, cy = priv.p_chunkCurrent.x, priv.p_chunkCurrent.y;
        local centerX, centerY = screenX/2,screenY/2;
        local sx, sy = centerX-260, centerY-260;
        local xi, yi = 0,0;
        for y = cy-3, cy+3, 1 do
            for x = cx-3, cx+3, 1 do
                local px,py = sx+(xi*75), sy+(yi*75);
                if x == priv.p_workingOn.x and y == priv.p_workingOn.y then
                    priv.textMngr:DrawString(UIFont.Small, px, py, tostring(x) .. ":" .. tostring(y), 1.0, 0.0, 0.0, 1.0);
                else
                    priv.textMngr:DrawString(UIFont.Small, px, py, tostring(x) .. ":" .. tostring(y), 1.0, 1.0, 1.0, 1.0);
                end
                xi = xi +1;
            end
            xi = 0;
            yi = yi +1;
        end
    end

    function priv.timeReset()
        priv.gameTime:setTimeOfDay(13.00);
    end

    function priv.increaseTime()
        if priv.newTick == priv.tick then
            return;
        else
            priv.tick = priv.newTick;
            --priv.txt[1] = "CurrentTick: " .. tostring(priv.ErosionMain:getEtick()) .. " - db " .. tostring(priv.tick);
            if priv.ErosionMain:getEtick() > 100 then
                -- start increasing the date for seasons

                priv.gameTime:setDay( priv.gameTime:getDay() + 1);
                if priv.gameTime:getDay() >= priv.gameTime:daysInMonth(priv.gameTime:getYear(), priv.gameTime:getMonth()) then
                    priv.gameTime:setDay(0);
                    priv.gameTime:setMonth(priv.gameTime:getMonth() + 1);
                    if priv.gameTime:getMonth() >= 12 then
                        priv.gameTime:setMonth(0);
                        priv.gameTime:setYear(priv.gameTime:getYear() + 1);
                    end
                end
            end
            priv.ErosionMain:mainTimer();
            if priv.dispDebug or priv.dispText then
                priv.updateTxt();
            end

            priv.ChunkReader.SetNextRing( 0 );
            priv.ChunkReader.ResetChunkReader( priv.p_chunkCurrent, priv.p_chunkMoveDir );
        end
    end

    local counter = 0;
    function priv.DebugQue( _ticks )
        --if priv.pause then return end;
        counter = counter + 1;
        local player = getPlayer();
        player:setHealth(1.0);
        if not priv.pause then
            priv.increaseTime();
        end

        if not priv.chunk then
            priv.chunk, priv.chunkPos, priv.chunkModData = priv.ChunkReader.GetNextChunk( priv.tick );
            if not priv.chunk then
                --if counter < EROSION_DEBUG_TICK then return end
                counter = 0;
                if not priv.pause then
                    priv.newTick = priv.tick + 1;
                end
                return;
            end;
        end

        --print("working on chunk: ", priv.chunkPos.x, priv.chunkPos.y )
        priv.p_workingOn.x, priv.p_workingOn.y = priv.chunkPos.x, priv.chunkPos.y;

        for tileY = 0, 9, 1 do
            for tileX = 0, 9, 1 do
                local sq = priv.chunk:getGridSquare(tileX, tileY, 0);
                if sq then
                    ErosionMain.LoadGridsquare( sq );
                end
            end
        end
        ErosionMain.ChunkLoaded(priv.chunk)
        priv.chunk, priv.chunkPos, priv.chunkModData = false, false, false;
    end

    function priv.printPos()
        print("# Tick: ", priv.tick);
        print("# BoxMove, center: ",priv.p_chunkDirCenter);
        print("- ChunkMove: ",priv.p_chunkCurrent," last: ",priv.p_chunkLast);
        print("- DirChange: ",priv.p_chunkMoveDir);
        print("-Garbage: ", collectgarbage("count"));
    end

    function priv.UpdatePlayerChunk(_player)
        local chunkX = priv.Floor( _player:getX() / priv.chunkTileWidth );
        local chunkY = priv.Floor( _player:getY() / priv.chunkTileWidth );

        if chunkX ~= priv.p_chunkCurrent.x or chunkY ~= priv.p_chunkCurrent.y then
            local moved_to = priv.Point:new( chunkX, chunkY );

            if moved_to < (priv.p_chunkDirCenter - priv.p_chunkBox) or moved_to > (priv.p_chunkDirCenter + priv.p_chunkBox) then
                priv.p_chunkDirCenter.setPoint( priv.p_chunkCurrent );
            end

            priv.p_chunkMoveDir.setPoint( moved_to - priv.p_chunkDirCenter );

            priv.p_chunkLast.setPoint( priv.p_chunkCurrent );
            priv.p_chunkCurrent.setPoint( moved_to );

            priv.ChunkReader.ResetChunkReader( priv.p_chunkCurrent, priv.p_chunkMoveDir );

            --priv.printPos();
        end
    end

    instance = publ;
    return publ;
end

local function OnGameStart()
    if isServer() or isClient() then return end
    local main = ErosionMain.getInstance()
    if not main:getConfig():getDebug():getEnabled() then return end
    EDebug.DemoTime(main):GameStart()
end
Events.OnGameStart.Add( OnGameStart );

