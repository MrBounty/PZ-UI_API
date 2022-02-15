--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "ISUI/ISPanel"

---@class ForecasterDebug : ISPanel
ForecasterDebug = ISPanel:derive("ForecasterDebug");
ForecasterDebug.instance = nil;

local stages = {
    [0] = "STAGE_START",
    [1] = "STAGE_SHOWERS",
    [2] = "STAGE_HEAVY_PRECIP",
    [3] = "STAGE_STORM",
    [4] = "STAGE_CLEARING",
    [5] = "STAGE_MODERATE",
    [6] = "STAGE_DRIZZLE",
    [7] = "STAGE_BLIZZARD",
    [8] = "STAGE_TROPICAL_STORM",
    [9] = "STAGE_INTERMEZZO",
    [10] = "STAGE_MODDED",
    [11] = "STAGE_KATEBOB_STORM",
}

local function roundstring(_val)
    return tostring(ISDebugUtils.roundNum(_val,2));
end

function ForecasterDebug.OnOpenPanel()
    if ForecasterDebug.instance==nil then
        ForecasterDebug.instance = ForecasterDebug:new (100, 100, 640, 600, "Climate forecaster debugger");
        ForecasterDebug.instance:initialise();
        ForecasterDebug.instance:instantiate();
    end

    ForecasterDebug.instance:addToUIManager();
    ForecasterDebug.instance:setVisible(true);

    return ForecasterDebug.instance;
end

function ForecasterDebug:initialise()
    ISPanel.initialise(self);

    self.clim = getClimateManager();
    self.forecaster = self.clim:getClimateForecaster();
    self.firstForecast = false;
end

function ForecasterDebug:createChildren()
    ISPanel.createChildren(self);

    ISDebugUtils.addLabel(self, {}, 10, 20, "Climate forecaster debugger", UIFont.Medium, true)

    self.daysList = ISScrollingListBox:new(10, 50, 200, self.height - 100);
    self.daysList:initialise();
    self.daysList:instantiate();
    self.daysList.itemheight = 22;
    self.daysList.selected = 0;
    self.daysList.joypadParent = self;
    self.daysList.font = UIFont.NewSmall;
    self.daysList.doDrawItem = self.drawDayList;
    self.daysList.drawBorder = true;
    self.daysList.onmousedown = ForecasterDebug.OnDaysListMouseDown;
    self.daysList.target = self;
    self:addChild(self.daysList);

    self.infoList = ISScrollingListBox:new(220, 50, 400, self.height - 100);
    self.infoList:initialise();
    self.infoList:instantiate();
    self.infoList.itemheight = 22;
    self.infoList.selected = 0;
    self.infoList.joypadParent = self;
    self.infoList.font = UIFont.NewSmall;
    self.infoList.doDrawItem = self.drawInfoList;
    self.infoList.drawBorder = true;
    self:addChild(self.infoList);

    local y, obj = ISDebugUtils.addButton(self,"close",self.width-200,self.height-40,180,20,getText("IGUI_CraftUI_Close"),ForecasterDebug.onClickClose);

    self:populateList();
end

function ForecasterDebug:onClickClose()
    self:close();
end

function ForecasterDebug:OnDaysListMouseDown(item)
    self:populateInfoList(item);
end

function ForecasterDebug:populateList()
    local forecasts = self.forecaster:getForecasts();

    if self.firstForecast and self.firstForecast==forecasts:get(0) then
        return;
    end

    self.daysList:clear();

    for i=0, forecasts:size()-1 do
        local forecast = forecasts:get(i);

        local prefix = forecast:getIndexOffset()==0 and "[TODAY]" or ("[offset "..tostring(forecast:getIndexOffset()).."]");
        local name = prefix .. " :: " .. forecast:getName()

        self.daysList:addItem(name, forecast);
    end

    self.firstForecast=forecasts:get(0);

    self:populateInfoList(self.firstForecast);
end

function ForecasterDebug:drawDayList(y, item, alt)
    local a = 0.9;

    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if item.item:getIndexOffset()<0 then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.2, 0.80, 0.80, 0.80);
    end

    if item.item:isWeatherStarts() or item.item:getWeatherOverlap()~=nil then
        -- this day has weather, color it differently
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.45, 0.45, 0.85);
    end

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
    end

    --local prefix = item.item:getIndexOffset()==0 and "TODAY" or tostring(item.item:getIndexOffset());
    --self:drawText( prefix .. " :: " .. item.item:getName(), 10, y + 2, 1, 1, 1, a, self.font);
    if item.item:isHasFog() then
        self:drawText( item.text .. " (F)", 10, y + 2, 0.8, 1, 0.75, a, self.font);
    else
        self:drawText( item.text, 10, y + 2, 1, 1, 1, a, self.font);
    end

    return y + self.itemheight;
end

function ForecasterDebug:formatVal(_value, _func, _func2)
    return _func2 and (_func2(_func(_value))) or (_func(_value));
end

function ForecasterDebug:printForecastValue(_name, _value, _func, _func2)
    local formatFunc = _func or roundstring;
    self.infoList:addItem("--- ".._name.." ----", nil);
    --self.infoList:addItem("Min: "..self:formatVal(_value:getTotalMin(), formatFunc, _func2), nil);
    --self.infoList:addItem("Max: "..self:formatVal(_value:getTotalMax(), formatFunc, _func2), nil);
    self.infoList:addItem("  Min/Max: "..self:formatVal(_value:getTotalMin(), formatFunc, _func2).." / "..self:formatVal(_value:getTotalMax(), formatFunc, _func2), nil);
    self.infoList:addItem("  Mean: "..self:formatVal(_value:getTotalMean(), formatFunc, _func2), nil);
    self.infoList:addItem("  Day time Min/Max: "..self:formatVal(_value:getDayMin(), formatFunc, _func2).." / "..self:formatVal(_value:getDayMax(), formatFunc, _func2), nil);
    self.infoList:addItem("  Day time Mean: "..self:formatVal(_value:getDayMean(), formatFunc, _func2), nil);
    self.infoList:addItem("  Night time Min/Max: "..self:formatVal(_value:getNightMin(), formatFunc, _func2).." / "..self:formatVal(_value:getNightMax(), formatFunc, _func2), nil);
    self.infoList:addItem("  Night time Mean: "..self:formatVal(_value:getNightMean(), formatFunc, _func2), nil);
end

function ForecasterDebug:populateInfoList(_forecast)
    self.infoList:clear();

    self.infoList:addItem(_forecast:getName(), nil);
    self.infoList:addItem("Dawn: "..roundstring(_forecast:getDawn()), nil);
    self.infoList:addItem("Dusk: "..roundstring(_forecast:getDusk()), nil);
    self.infoList:addItem("Day light hours: "..roundstring(_forecast:getDayLightHours()), nil);
    self:printForecastValue("Temperature", _forecast:getTemperature());
    self:printForecastValue("Humidity", _forecast:getHumidity());
    self:printForecastValue("Wind direction", _forecast:getWindDirection(), ClimateManager.getWindAngleString);
    self:printForecastValue("Wind speed (KpH)", _forecast:getWindPower(), ClimateManager.ToKph, roundstring);
    self:printForecastValue("Cloudiness", _forecast:getCloudiness());

    self.infoList:addItem("-----------------------------", nil);
    self.infoList:addItem("Has fog: "..tostring(_forecast:isHasFog()), nil);
    self.infoList:addItem("Fog strength: "..roundstring(_forecast:getFogStrength()), nil);
    self.infoList:addItem("Fog duration: "..roundstring(_forecast:getFogDuration()), nil);
    self.infoList:addItem("Airfront type (at day start): "..tostring(_forecast:getAirFrontString()), nil);
    --self.infoList:addItem("Weather wind: "..roundstring(_forecast:getWeatherWind()), nil);
    local weatherOverlaps = _forecast:getWeatherOverlap()~=nil and true or false;

    if _forecast:isWeatherStarts() or weatherOverlaps then
        self.infoList:addItem("Chance on snow: "..tostring(_forecast:isChanceOnSnow()), nil);
        self.infoList:addItem("Day has heavy rain: "..tostring(_forecast:isHasHeavyRain()), nil);
        self.infoList:addItem("Day has storm: "..tostring(_forecast:isHasStorm()), nil);
        self.infoList:addItem("Day has tropical storm: "..tostring(_forecast:isHasTropicalStorm()), nil);
        self.infoList:addItem("Day has blizzard: "..tostring(_forecast:isHasBlizzard()), nil);

        self.infoList:addItem("--- TODAY'S WEATHERSTAGE TYPES ---", nil);
        local s = _forecast:getWeatherStages();
        for i=0, s:size()-1 do
            local index = s:get(i);
            local name = stages[index] or "UNKNOWN";
            self.infoList:addItem("Stage ["..tostring(i).."]: "..tostring(name), nil);
        end
    end

    if _forecast:isWeatherStarts() then
        self.infoList:addItem("--- WEATHER STARTS ---", nil);
        self.infoList:addItem("Weather starts: "..tostring(_forecast:isWeatherStarts()), nil);
        self:populateWeatherInfoList(_forecast);
    end

    if weatherOverlaps then
        self.infoList:addItem("--- WEATHER OVERLAP ---", nil);
        self.infoList:addItem("IsWeatherOverlap: true", nil);
        self:populateWeatherInfoList(_forecast:getWeatherOverlap(), true);
    end
end

function ForecasterDebug:populateWeatherInfoList(_forecast, _isOverlap)
    if _isOverlap then
        self.infoList:addItem("Weather start = ".._forecast:getName(), nil);
    end
    self.infoList:addItem("Start time: "..roundstring(_forecast:getWeatherStartTime()), nil);

    local period = _forecast:getWeatherPeriod();

    self.infoList:addItem("Duration: "..roundstring(period:getDuration()), nil);
    self.infoList:addItem("Strength: "..roundstring(period:getTotalStrength()), nil);
    local frontType = period:getFrontType()==-1 and "COLD" or "WARM";
    self.infoList:addItem("Front type: "..tostring(frontType), nil);

    self.infoList:addItem("--- FULL WEATHER PATTERN ---", nil);
    local s = period:getWeatherStages();
    for i=0, s:size()-1 do
        local index = s:get(i):getStageID();
        local name = stages[index] or "UNKNOWN";
        self.infoList:addItem("Stage ["..tostring(i).."]: "..tostring(name), nil);
    end

end

function ForecasterDebug:drawInfoList(y, item, alt)
    local a = 0.9;

    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
    end

    self:drawText( item.text, 10, y + 2, 1, 1, 1, a, self.font);

    return y + self.itemheight;
end

function ForecasterDebug:prerender()
    ISPanel.prerender(self);
    self:populateList();
end

function ForecasterDebug:update()
    ISPanel.update(self);
end

function ForecasterDebug:close()
    self:setVisible(false);
    self:removeFromUIManager();
    ForecasterDebug.instance = nil
end

function ForecasterDebug:new(x, y, width, height, title)
    local o = {};
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self);
    self.__index = self;
    o.variableColor={r=0.9, g=0.55, b=0.1, a=1};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.buttonBorderColor = {r=0.7, g=0.7, b=0.7, a=0.5};
    o.zOffsetSmallFont = 25;
    o.moveWithMouse = true;
    o.panelTitle = title;
    ISDebugMenu.RegisterClass(self);
    return o;
end


