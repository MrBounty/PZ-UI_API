--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

-- TODO NOTE: disabled this class during climate/weather update, moved some functionallity from here over to server/Climate/ClimateMain.lua ~turbo

---@class season
season = {};
season.previousHour = -1;
season.previousMonth = -1;
season.previousDay = -1;
season.loaded = false;
season.min = -1;
season.max = -1;
season.currentTemp = -1;
season.moonDay = 0;
season.previousWeatherHour = 0;
season.currentTempModifier = 0;
season.weatherIcon = nil;
season.moon = "false";
season.weather = "normal";
season.moonCycle = 10;
season.moonPhase = "2";
season.ambientMaxGoal = 0.6;
season.ambientMinGoal = 0.0;
season.gameTime = {};
season.moonAmbient = 0.0;
--season.bodyTemperature = 37.0;
season.rainToday = 0;

season.wantNoise = getDebug()
local noise = function(msg)
	if (season.wantNoise) then print('season: '..msg) end
end

season.EveryTenMinutes = function()
    --[[
	if isClient() then
		season.updateBodyTemperature()
		season.updateWeatherIcon()
		return
	end

	-- every 10 min we update our ambient depending on the weather
	-- we up the body temperature too
	season.updateAmbient();
	season.updateBodyTemperature();
	-- if rain as stop
	if season.weather == "rain" and not RainManager.isRaining() then
		season.weather = "cloud";
		-- remove our previous weather image
--		season.weatherIcon:removeImages();
		-- update the weather icon
	--	season.updateWeatherIcon();
	end
	local sec = math.floor(GameTime:getInstance():getTimeOfDay() * 3600);
	local currentHour = math.floor(sec / 3600);
	-- we get our temperature, depending on the hour
	if(currentHour ~= season.previousHour) then
		local month = seasonProps.monthTemp[GameTime:getInstance():getMonth() + 1];
		season.previousHour = currentHour;
		-- if we changed our month, we reset the dawn and dusk (so the sun will be late to rise in winter, etc.)
		if GameTime:getInstance():getMonth() ~= season.previousMonth then
			season.previousMonth = GameTime:getInstance():getMonth();
            GameTime:getInstance():setDawn(month.dawn);
            GameTime:getInstance():setDusk(month.dusk);
		end
		-- every day we get the min/max temp of the month and randomize it (-3/+3)
		if season.previousDay ~= GameTime:getInstance():getDay() then
			season.previousDay = GameTime:getInstance():getDay();
			season.min = ZombRand((month.min - 3), (month.min + 4));
			season.max = ZombRand((month.max - 3), (month.max + 4));

			-- check for sandBox option
			local tempMod = SandboxVars.Temperature;
			-- from very cold to hot
			if tempMod == 1 then
				season.min = season.min - 15;
				season.max = season.max - 15;
			elseif tempMod == 2 then
				season.min = season.min - 8;
				season.max = season.max - 8;
			elseif tempMod == 4 then
				season.min = season.min + 8;
				season.max = season.max + 8;
			elseif tempMod == 5 then
				season.min = season.min + 15;
				season.max = season.max + 15;
			end

			season.rainToday = 0;
            GameTime:getInstance():setThunderDay(ZombRand(20) == 0);
		end
		-- we calcul our temperature gain per hour
		local tempPerHour = (season.max - season.min) / 12;
		-- at 3am the temp start to grow, at 15 it drop
		local hourLeft = 0;
		if currentHour >= 3 and currentHour <= 15 then
			hourLeft = 15 - currentHour;
		elseif currentHour >= 0 and currentHour <= 3 then
			hourLeft = (currentHour + 24) - 15;
		else
			hourLeft = currentHour - 15;
		end
		season.currentTemp = season.max - (hourLeft * tempPerHour);
		-- if it was raining, we may stop it (1 to 5 chance)
		season.updateRain();
--		-- remove our previous weather image
--		season.weatherIcon:removeImages();
		-- moon cycle
		season.updateMoonState(currentHour, month);
		-- update the weather
		season.updateWeather(month, currentHour);
		-- update the weather icon
	--	season.updateWeatherIcon();
		getWorld():setGlobalTemperature(round2(season.currentTemp, 2) + season.currentTempModifier);
		season.save();
    end
    getWorld():transmitWeather()
    --]]
end

-- update the body temperature a bit every 10min, to get close to the exterior temp + clothes temp
season.updateBodyTemperature = function()
    --[[
	if isServer() then
		return
	end
	local players = IsoPlayer.getPlayers()
	for i=0,players:size()-1 do
		local player = players:get(i)
		if player and not player:isDead() then
			local heatSourceTemp = getCell():getHeatSourceTemperature(player:getX(), player:getY(), player:getZ())
			-- can up the maximum body Temp only by 10 (so you can't set up 50 campfire and be always hot, even during high winter)
			heatSourceTemp = math.min(heatSourceTemp, 10)
			local vehicleTemp = VehicleUtils.getBodyTemperature(player)
			local goalTemp = player:getPlayerClothingTemperature() + getWorld():getGlobalTemperature() + heatSourceTemp + vehicleTemp
			local playerTemp = player:getTemperature()
--		print("temp ", vehicleTemp, goalTemp, playerTemp);
--			noise(goalTemp..' '..playerTemp)
			if goalTemp ~= playerTemp then
				-- increase the body T° by 10% of our goal temp every 10min, if you're in a car bump this to 60% (way faster to heat/cold)
				local increase = 0.1;
				if player:getVehicle() then increase = 0.6; end
--				print("gonna inc by", ((goalTemp - playerTemp) * increase))
				player:setTemperature(playerTemp + ((goalTemp - playerTemp) * increase));
			end
		end
	end
	--]]
end

-- update the ambient (light outside), a bit every 10 minutes
-- for exemple if it was sunny and now it's cloudy, we need to slowly down the light
season.updateAmbient = function()
    --[[
    GameTime:getInstance():setMoon(season.moonAmbient);
    if round2(GameTime:getInstance():getAmbientMax(), 3) < season.ambientMaxGoal then
        GameTime:getInstance():setAmbientMax(GameTime:getInstance():getAmbientMax() + 0.02);
       -- season.gameTime:setNightMax(GameTime:getInstance():getNightMax() - 0.02);
        if GameTime:getInstance():getAmbientMax() > season.ambientMaxGoal then
            GameTime:getInstance():setAmbientMax(season.ambientMaxGoal);
       --     GameTime:getInstance():setNightMax(season.ambientMaxGoal);
        end

	elseif round2(GameTime:getInstance():getAmbientMax(), 3) > season.ambientMaxGoal then
        GameTime:getInstance():setAmbientMax(GameTime:getInstance():getAmbientMax() - 0.02);
      --  GameTime:getInstance():setNightMax(GameTime:getInstance():getNightMax() + 0.02);
         if GameTime:getInstance():getAmbientMax() < season.ambientMaxGoal then
             GameTime:getInstance():setAmbientMax(season.ambientMaxGoal);
     --        GameTime:getInstance():setMoon(season.moonAmbient);
         end
    end

 	if round2(GameTime:getInstance():getAmbientMin(), 3) < season.ambientMinGoal then
        GameTime:getInstance():setAmbientMin(GameTime:getInstance():getAmbientMin() + 0.02);
       --  GameTime:getInstance():setNightMin(GameTime:getInstance():getNightMin() - 0.02);
         if GameTime:getInstance():getAmbientMin() > season.ambientMinGoal then
             GameTime:getInstance():setAmbientMin(season.ambientMinGoal);
         --    GameTime:getInstance():setMoon(season.moonAmbient);
         end
     elseif round2(GameTime:getInstance():getAmbientMin(), 3) > season.ambientMinGoal then
        GameTime:getInstance():setAmbientMin(GameTime:getInstance():getAmbientMin() - 0.02);
       -- GameTime:getInstance():setNightMin(GameTime:getInstance():getNightMin() + 0.02);
         if GameTime:getInstance():getAmbientMin() < season.ambientMinGoal then
             GameTime:getInstance():setAmbientMin(season.ambientMinGoal);
           --  GameTime:getInstance():setMoon(season.moonAmbient);
         end
    end
    --]]
end


-- 1 to 5 chance to stop the rain every hour
season.updateRain = function()
    --[[
	local randomStopRain = 5;
	-- check the sand box rain modifier
	if SandboxVars.Rain == 1 then -- very dry
		randomStopRain = 0;
	elseif SandboxVars.Rain == 1 then -- dry
		randomStopRain = 2;
	elseif SandboxVars.Rain == 1 then -- rainy
		randomStopRain = 9;
	elseif SandboxVars.Rain == 1 then -- very rainy
		randomStopRain = 15;
	end
	if season.weather == "rain" then
		if ZombRand(randomStopRain) == 0 then
            if not isClient() then
				RainManager.stopRaining()
				getGameTime():getModData()["isRaining"] = false
				noise("rain stopped")
			end
		end
	end
	-]]
end

-- choose a random weather
season.updateWeather = function(month, currentHour)
    --[[
	-- we can't have more than 1 weather change every 4 hours
	if season.previousWeatherHour == 0 then
		-- we randomize a number between 0 and 100, and check to what weather it correspond for this month
		local weatherChange = ZombRand(1,101);
		table.sort(month.weather, function(a,b) return a.chance<b.chance end)
		local previousChance = 0;
		for i,v in ipairs(month.weather) do
			-- if we've randomized this weather
			if  weatherChange >= previousChance and weatherChange <= v.chance then
				season.weather = v.name;
			end
			previousChance = v.chance;
		end
		-- if it's moon time, we don't have sunny weather, so if we have it, we change it to normal
		if season.weather == "sunny" and season.moon == "true" then
			season.weather = "normal";
		end
		-- check the sand box rain modifier
		if "rain" == season.weather then
			if SandboxVars.Rain == 1 and ZombRand(1) == 0 then -- very dry
				season.weather = "normal";
			elseif SandboxVars.Rain == 2 and ZombRand(3) == 0 then -- dry
				season.weather = "normal";
			end
		else
			if SandboxVars.Rain == 4 and ZombRand(3) == 0 then -- rainy
				season.weather = "rain";
			elseif SandboxVars.Rain == 5 and ZombRand(1) == 0 then -- very rainy
				season.weather = "rain";
			end
		end
		getWorld():setWeather(season.weather);
		season.ambientMaxGoal = seasonProps.weather[season.weather].ambientMax;
		season.ambientMinGoal = seasonProps.weather[season.weather].ambientMin;
		if SandboxVars.NightDarkness == 1 then
			season.ambientMinGoal = season.ambientMinGoal * 0.3;
		elseif SandboxVars.NightDarkness == 2 then
			season.ambientMinGoal = season.ambientMinGoal * 0.7;
		elseif SandboxVars.NightDarkness == 4 then
			season.ambientMinGoal = season.ambientMinGoal * 1.3;
		end
		if season.moon == "true" then
			season.ambientMinGoal = season.ambientMinGoal + season.moonAmbient;
			if season.ambientMinGoal > season.ambientMaxGoal then
				season.ambientMinGoal = season.ambientMaxGoal
			end
		end
        GameTime:getInstance():setViewDistMax(seasonProps.weather[season.weather].view);
		season.currentTempModifier = seasonProps.weather[season.weather].temp;
		season.previousWeatherHour = 2;
		-- if it's raining, we set it
		if "rain" == season.weather then
			-- if it was already raining today, we lower the chance of getting rain again
			if season.rainToday > 1 then
				if ZombRand(season.rainToday * 5) == 0 then
					season.startRain();
				else
					season.weather = "cloud";
				end
			else
				season.startRain();
			end
        elseif RainManager.isRaining() then
            if not isClient() then
				RainManager.stopRaining();
				getGameTime():getModData()["isRaining"] = false
				noise("rain stopped")
			end
        end
        triggerEvent("OnChangeWeather", season.weather);
    else
		season.previousWeatherHour = season.previousWeatherHour - 1;
	end
	--]]
end

-- start the rain with some random intensity for the rain
season.startRain = function()
    --[[
    if isClient() then return; end
	-- rand max is the intensity of the rain at start
	-- rand min is the max intensity of the rain, 0 mean great rain, 20 for example mean a little rain
	RainManager.setRandRainMax((ZombRand(10) + 25));
	RainManager.setRandRainMin(ZombRand(5));
	RainManager.startRaining();
	season.rainToday = season.rainToday + 1;
	getGameTime():getModData()["isRaining"] = true
	noise("rain started")
	--]]
end

-- if it's moon time, we set it, so we can update our moon phase + set the icons of the moon
season.updateMoonState = function(currentHour, month)
    --[[
	if currentHour == month.moonTime then
		season.moon = "true";
		triggerEvent("OnDusk"); -- we notify that we're on dusk for modding purpose
		season.updateMoonPhase();
	elseif currentHour == month.sunTime then
		season.moon = "false";
		triggerEvent("OnDawn"); -- we notify that we're on dawn for modding purpose
	end
	--]]
end

-- switch the moon cycle (from full to empty)
-- it does change the night dark intensity too
season.updateMoonPhase = function()
    --[[
	season.moonCycle = season.moonCycle + 1;
	if season.moonCycle > 29 then
		season.moonCycle = 0;
	end
	if season.moonCycle < 4 then
		season.moonPhase = "0";
		season.moonAmbient = 0.0;
	elseif season.moonCycle < 8 then
		season.moonPhase = "1";
		season.moonAmbient = 0.2;
	elseif season.moonCycle < 12 then
		season.moonPhase = "2";
		season.moonAmbient = 0.4;
	elseif season.moonCycle < 15 then
		season.moonPhase = "3";
		season.moonAmbient = 0.5;
	elseif season.moonCycle < 18 then
		season.moonPhase = "4";
		season.moonAmbient = 0.7;
	elseif season.moonCycle < 22 then
		season.moonPhase = "5";
		season.moonAmbient = 0.5;
	elseif season.moonCycle < 26 then
		season.moonPhase = "6";
		season.moonAmbient = 0.4;
	else
		season.moonPhase = "7";
		season.moonAmbient = 0.2;
    end
    --]]
end

-- change the weather display icon depending on the weather
-- set the moon icon too, depending on his current phase
season.updateWeatherIcon = function()
	-- first moon icon
--	season.weatherIcon:removeMoon();
--	if "true" == season.moon then
---		season.weatherIcon:addMoon("media/ui/weather/Weather_Moon_Phase_" .. season.moonPhase .. ".png");
--	end
	-- then weather icon
	--if "rain" == season.weather then
--		season.weatherIcon:addImage("media/ui/weather/Weather_Cloud_Heavy.png");
		--season.weatherIcon:addImage("media/ui/weather/Weather_Rain.png");
	--elseif "sunny" == season.weather then
--		season.weatherIcon:addImage("media/ui/weather/Weather_Sun.png");
	--elseif "cloud" == season.weather then
--		season.weatherIcon:addImage("media/ui/weather/Weather_Cloud_Light.png");
	--end
end

-- load our datas
season.load = function ()
    --[[
	noise('loading GameTime modData')
	season.gameTime = GameTime:getInstance();
	local datas = season.gameTime:getModData();
	season.min = datas["minTemp"];
	season.max = datas["maxTemp"];
	season.moon = datas["moon"] or "false";
	season.currentTemp = datas["currentTemp"];
	season.moonCycle = datas["moonCycle"] or 10;
	season.moonPhase = datas["moonPhase"] or "2";
	season.weather = datas["weather"] or "normal";
	season.previousWeatherHour = datas["previousWeatherHour"] or 0;
--	season.bodyTemperature = datas["bodyTemperature"] or 37.0;
	season.rainToday = datas["rainToday"] or 0;
	if season.currentTemp ~= nil then
		local sec = math.floor(season.gameTime:getTimeOfDay() * 3600);
		local currentHour = math.floor(sec / 3600);
		getWorld():setGlobalTemperature(season.currentTemp);
		season.previousDay = season.gameTime:getDay();
		season.previousHour = currentHour;
		season.previousMonth = season.gameTime:getMonth();
	end
	if datas["isRaining"] then
		season.startRain();
	end
	--season.weatherIcon = ISWeather:new(0, 5, 43, 43);
	--season.weatherIcon:initialise();
	--season.weatherIcon:addToUIManager();
	season.updateMoonPhase();
	season.updateWeatherIcon();
--    season.startRain();
----]]
end

-- save all our datas
season.save = function()
    --[[
	local datas = GameTime:getInstance():getModData();
	datas["minTemp"] = season.min;
	datas["maxTemp"] = season.max;
	datas["currentTemp"] = getWorld():getGlobalTemperature();
	datas["moon"] = season.moon;
	datas["moonCycle"] = season.moonCycle;
	datas["weather"] = season.weather;
	datas["previousWeatherHour"] = season.previousWeatherHour;
--	datas["bodyTemperature"] = season.bodyTemperature;
	datas["rainToday"] = season.rainToday;
	--]]
end

season.OnGameTimeLoaded = function()
    --[[
	if not isClient() then
		season.OnGameStart()
	end
	--]]
end

season.OnGameStart = function()
    --[[
	season.load()
	season.ambientMaxGoal = seasonProps.weather[season.weather].ambientMax;
	season.ambientMinGoal = seasonProps.weather[season.weather].ambientMin;
	if season.moon == "true" then
		season.ambientMinGoal = season.ambientMinGoal + season.moonAmbient;
		if season.ambientMinGoal > season.ambientMaxGoal then
			season.ambientMinGoal = season.ambientMaxGoal
		end
	end
	GameTime:getInstance():setAmbientMax(season.ambientMaxGoal)
	GameTime:getInstance():setAmbientMin(season.ambientMinGoal)
    --]]
end

-- round java style
function round2(num, idp)
  return tonumber(string.format("%." .. (idp or 0) .. "f", num))
end

--Events.EveryTenMinutes.Add(season.EveryTenMinutes);

--Events.OnGameTimeLoaded.Add(season.OnGameTimeLoaded);
--Events.OnGameStart.Add(season.OnGameStart);
