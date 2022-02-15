--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************


require "ISUI/ISCollapsableWindow"

---@class PlayerClimateDebug : ISCollapsableWindow
PlayerClimateDebug = ISCollapsableWindow:derive("PlayerClimateDebug");
PlayerClimateDebug.instance = nil;
PlayerClimateDebug.shiftDown = 0;
PlayerClimateDebug.eventsAdded = false;

--[[
local enabled = true; --getDebug();

function PlayerClimateDebug.OnKeepKeyDown(key)
    --backspace 13, shift 42, 54
    --print("KeyKeepDown = "..tostring(key));
    if key==42 or key==54 then
        PlayerClimateDebug.shiftDown = 4;
    end
end

function PlayerClimateDebug.OnKeyDown(key)
    --backspace 13, shift 42, 54
    --print("KeyDown = "..tostring(key));
    if PlayerClimateDebug.shiftDown>0 and key ==14 then
        PlayerClimateDebug.OnOpenPanel();
    end
end--]]

function PlayerClimateDebug.OnOpenPanel()
    PlayerClimateDebug.fx = getCell():getWeatherFX();
    PlayerClimateDebug.cm = getClimateManager();

    if PlayerClimateDebug.instance==nil then
        PlayerClimateDebug.instance = PlayerClimateDebug:new (100, 100, 400, 600, getPlayer());
        PlayerClimateDebug.instance:initialise();
        PlayerClimateDebug.instance:instantiate();
    end

    PlayerClimateDebug.instance:addToUIManager();
    PlayerClimateDebug.instance:setVisible(true);
    --PlayerClimateDebug.instance:setCapture(false);

    if not PlayerClimateDebug.eventsAdded then
        Events.OnClimateTickDebug.Add(PlayerClimateDebug.onClimateTickDebug);
        --Events.OnTick.Add(PlayerClimateDebug.onTick);
        PlayerClimateDebug.eventsAdded = true;
    end

    return PlayerClimateDebug.instance;
end

function PlayerClimateDebug:initialise()
    ISCollapsableWindow.initialise(self);
end

function PlayerClimateDebug:createChildren()
    ISCollapsableWindow.createChildren(self);

    self:initVariables();

    local th = self:titleBarHeight();

    local y, lbl = th, nil;

    self.labels = {};
    self.tempColor = {r=1.0,g=1.0,b=1.0,a=1.0};
    self.colWhite = {r=1,g=1,b=1,a=1};
    self.clrRed = {r=1,g=0,b=0,a=1};
    self.clrOrangeRed = {r=1.000,g=0.271,b=0,a=1};
    self.clrGold = {r=1.000,g=0.843,b=0,a=1};
    self.clrGreenYellow = {r=0.678,g=1,b=0.184,a=1};
    self.clrGreen = {r=0.000,g=0.502,b=0,a=1};

    --[[*******************************************************************************************--]]
    --[[
    y = self:addLabel(y,"tit_Ext","EXTERNAL AIR","");
    --y, lbl = self:addLabelValue(y,"value","worldTemp","World temperature:",0,nil,"= Celsius");
    --y, lbl = self:addLabelValue(y,"value","heatsourceTemp","HeatSource:",0,nil,"= Celsius");
    --y, lbl = self:addLabelValue(y,"value","vehicleTemp","VehicleTempMod:",0,nil,"= Celsius");
    y, lbl = self:addLabelValue(y,"value","tempBase","B-T:",0,nil,"= Celsius");
    y, lbl = self:addLabelValue(y,"value","isInVehicle","Inside Vehicle:","false",nil);
    y, lbl = self:addLabelValue(y,"value","isInside","Inside Building:","false",nil);
    y, lbl = self:addLabelValue(y,"value","airTemperature","Air Near Player:",0,nil,"= Celsius");
    y, lbl = self:addLabelValue(y,"value","windSpeed","Windspeed:",0,nil,"= Kph");
    --y = self:addLabelValue(y,"value","seasonMidpoint","Midpoint:",0);

    y = y+8;

    y = self:addLabel(y,"tit_Plr","MODIFIERS","");
    y, lbl = self:addLabelValue(y,"value","hunger","Hunger:",0,nil,"= 0.0 to 1.0   [c]");
    lbl.valueLabel.colUnitInv = true;
    y, lbl = self:addLabelValue(y,"value","thirst","Thirst:",0,nil,"= 0.0 to 1.0   [h]");
    lbl.valueLabel.colUnitInv = true;
    y, lbl = self:addLabelValue(y,"value","tired","Tired:",0,nil,"= 0.0 to 1.0   [c,h]");
    lbl.valueLabel.colUnitInv = true;
    y, lbl = self:addLabelValue(y,"value","excercise","Excercise:",0,self.colWhite,"= 0.0 to 1.0   [c,h]");
    y, lbl = self:addLabelValue(y,"value","weight","Weight:",0,self.colWhite,"= Kg           [c,h]");
    --lbl.valueLabel.colUnitInv = true;
    y, lbl = self:addLabelValue(y,"value","fitness","Fitness:",0,nil,"= 0.0 to 1.0   [c,h]");
    lbl.valueLabel.colUnitMinPlus = true;
    y, lbl = self:addLabelValue(y,"value","drunkenness","Drunkenness:",0,nil,"= 0.0 to 1.0   [c]");
    lbl.valueLabel.colUnitInv = true;
    y, lbl = self:addLabelValue(y,"value","sickness","Sickness:",0,nil,"= 0.0 to 1.0   [c,h]");
    lbl.valueLabel.colUnitInv = true;
    y, lbl = self:addLabelValue(y,"value","hasACold","HasACold:",0,nil,"= 0.0 to 1.0   [c,h]");
    lbl.valueLabel.colUnitInv = true;
    --y = self:addLabel(y,"tit_Cloth","CLOTHING MODIFIERS","");
    y, lbl = self:addLabelValue(y,"value","wetness","Wetness:",0,nil,"= 0.0 to 1.0   [cloth,c,h]");
    lbl.valueLabel.colUnitInv = true;
    y, lbl = self:addLabelValue(y,"value","clothing","Clothing:",0,nil,"= 0.0 to 1.0   [c,h]");
    lbl.valueLabel.colUnit = true;
    --y, lbl = self:addLabelValue(y,"value","clothingFinal","Clothing cold resist:",0,nil,"= 0.0 to 1.0   [c]");
    --lbl.valueLabel.colUnit = true;
    --y, lbl = self:addLabelValue(y,"value","clothInvert","Clothing heat resist:",0,nil,"= 0.0 to 1.0   [h]");
    --lbl.valueLabel.colUnit = true;
    --y, lbl = self:addLabelValue(y,"value","clothInvert2","Clothing heat resistII:",0,nil,"= 0.0 to 1.0   [h]");
    --lbl.valueLabel.colUnit = true;

    y = y+8;
    self.colRed = {r=0.392,g=0.584,b=0.929,a=1};
    local c = self.colRed;
    y = self:addLabel(y,"tit_Final","COLD AND HEAT","");
    y, lbl = self:addLabelValue(y,"value","coldBodyResist","Cold Body Resist:",0,nil,"= 0.0 to 2.0");
    lbl.valueLabel.colUnit2 = true;
    y, lbl = self:addLabelValue(y,"value","coldClothResist","Cold Clothing Resist:",0,nil,"= 0.0 to 1.0");
    lbl.valueLabel.colUnit = true;
    y, lbl = self:addLabelValue(y,"value","coldBodyBonus","(body Bonus):",0,c,"= celsius");
    y, lbl = self:addLabelValue(y,"value","coldClothBonus","(cloth Bonus):",0,c,"= celsius");
    y, lbl = self:addLabelValue(y,"value","coldResist","Cold Resist:",0,c,"(final)");
    y, lbl = self:addLabelValue(y,"value","coldStrength","Cold Strength:",0,c);
    y, lbl = self:addLabelValue(y,"value","coldChange","Cold Change:",0,c,"base value");
    y = y+8;
    self.colBlue = {r=0.980,g=0.502,b=0.447,a=1};
    c = self.colBlue;
    y, lbl = self:addLabelValue(y,"value","heatBodyResist","Heat Body Resist:",0,nil,"= 0.0 to 2.0");
    lbl.valueLabel.colUnit2 = true;
    y, lbl = self:addLabelValue(y,"value","heatClothResist","Heat Clothing Resist:",0,nil,"= 0.0 to 1.0");
    lbl.valueLabel.colUnit = true;
    y, lbl = self:addLabelValue(y,"value","heatBodyBonus","(body Bonus):",0,c,"= celsius");
    y, lbl = self:addLabelValue(y,"value","heatClothBonus","(cloth Bonus):",0,c,"= celsius");
    y, lbl = self:addLabelValue(y,"value","heatResist","Heat Resist:",0,c,"(final)");
    y, lbl = self:addLabelValue(y,"value","heatStrength","Heat Strength:",0,c);
    y, lbl = self:addLabelValue(y,"value","heatChange","Heat Change:",0,c,"base value");

    y = y+8;

    y = self:addLabel(y,"tit_PlrTemp","FINAL","");
    y, lbl = self:addLabelValue(y,"value","tickChangePm","Change per min:",0,self.colWhite,"= Celsius per Real Min");
    lbl.valueLabel.colByValue = true;
    y, lbl = self:addLabelValue(y,"value","tickChange","Applied change:",0,self.colWhite,"= Celsius per tick");
    lbl.valueLabel.colByValue = true;
    --y, lbl = self:addLabelValue(y,"value","appliedModTick","Applied change (tick):",0,self.colWhite,"= Celsius");
    --lbl.valueLabel.colByValue = true;
    y, lbl = self:addLabelValue(y,"value","playerTemp","Player body temp:",0,nil,"= Celsius");

    y = y+8;
    y, lbl = self:addLabelValue(y,"value","hypoState","Player hypo state:",0,nil,"");
    y, lbl = self:addLabelValue(y,"value","hyperState","Player hyper state:",0,nil,"");

    y, lbl = self:addLabelValue(y,"value","underCooled","Undercooled:",0,nil,"= 0.0 to 1.0");
    lbl.valueLabel.colUnitInv = true;
    y, lbl = self:addLabelValue(y,"value","overHeated","Overheated:",0,nil,"= 0.0 to 1.0");
    lbl.valueLabel.colUnitInv = true;
    --]]
    --y = y+8;

    --y = self:addLabel(y,"tit_Extra","EXTRA","");
    --y, lbl = self:addLabelValue(y,"value","clothInvert","Clothing invert val:",0,nil,"");

    y = y+8;

    for k,v in ipairs(self.vars) do
        if v.isValue then
            y, lbl = self:addLabelValue(y,"value",v.variable,v.title,v.defaultVal,v.color,v.postfix);
        else
            y = y+8;
            y, lbl = self:addLabel(y,v.variable,v.title,"");
        end
    end

    self:setHeight(y+self:resizeWidgetHeight()+4);
end

function PlayerClimateDebug:initVariables()
    local thermos = self.player:getBodyDamage():getThermoregulator();
    local stats = self.player:getStats();
    local body = self.player:getBodyDamage();
    local nutrition = self.player:getNutrition();
    local climate = getClimateManager();

    self.vars = {};
    self:registerVariable("title_main","MAIN",false);
    self:registerVariable("getMetabolicTarget", "MetabolicTarget:", true, thermos, 0, nil, nil);
    self:registerVariable("getMetabolicRate", "Base MetabolicRate:", true, thermos, 0, nil, nil);
    self:registerVariable("getMetabolicRateReal", "Real MetabolicRate:", true, thermos, 0, nil, nil);
    self:registerVariable("getDbg_totalHeatRaw", "Body HeatIO raw:", true, thermos, 0, nil, nil);
    self:registerVariable("getDbg_totalHeat", "Body HeatIO:", true, thermos, 0, nil, nil);
    self:registerVariable("getBodyHeatDelta", "Body heat delta:", true, thermos, 0, nil, nil);
    self:registerVariable("getSetPoint", "Core setpoint:", true, thermos, 0, nil, nil);
    self:registerVariable("getCoreHeatDelta", "Core heat delta:", true, thermos, 0, nil, nil);
    self:registerVariable("getCoreRateOfChange", "Core rate of change:", true, thermos, 0, nil, nil);
    self:registerVariable("getCoreCelcius", "Core celcius:", true, thermos, 0, nil, nil);
    self:registerVariable("getDbg_primTotal", "Prim total:", true, thermos, 0, nil, nil);
    self:registerVariable("getDbg_secTotal", "Sec total:", true, thermos, 0, nil, nil);

    self:registerVariable("title_resources","BODY RESOURCES",false);
    self:registerVariable("getEnergy", "Energy:", true, thermos, 0, nil, nil);
    self:registerVariable("getEnergyMultiplier", "Energy drain multiplier:", true, thermos, 0, nil, nil);
    self:registerVariable("getBodyFluids", "Fluids:", true, thermos, 0, nil, nil);
    self:registerVariable("getFluidsMultiplier", "Fluids drain multiplier:", true, thermos, 0, nil, nil);

    self:registerVariable("title_other","OTHER",false);
    self:registerVariable("getCatchAColdDelta", "Catch-Cold-Delta:", true, thermos, 0, nil, nil);
    self:registerVariable("getTimedActionTimeModifier", "TimedAction mod:", true, thermos, 0, nil, nil);
    self:registerVariable("getThermalDamage", "Thermal damage:", true, thermos, 0, nil, nil);

    self:registerVariable("title_climate","CLIMATE",false);
    self:registerVariable("getTemperatureAir", "Base air temp:", true, thermos, 0, nil, nil);
    self:registerVariable("getTemperatureAirAndWind", "Air and Wind temp:", true, thermos, 0, nil, nil);
    self:registerVariable("getHumidity", "Humidity:", true, climate, 0, nil, nil);

    self:registerVariable("title_modifiers","MULTIPLIER",false);
    self:registerVariable("getSimulationMultiplier", "Simulation X:", true, thermos, 0, nil, nil);
    self:registerVariable("getDefaultMultiplier", "Default:", true, thermos, 0, nil, nil);
    self:registerVariable("getMetabolicRateIncMultiplier", "MetabolicRateInc:", true, thermos, 0, nil, nil);
    self:registerVariable("getMetabolicRateDecMultiplier", "MetabolicRateDec:", true, thermos, 0, nil, nil);
    self:registerVariable("getBodyHeatMultiplier", "BodyHeat:", true, thermos, 0, nil, nil);
    self:registerVariable("getCoreHeatExpandMultiplier", "CoreHeatExpand:", true, thermos, 0, nil, nil);
    self:registerVariable("getCoreHeatContractMultiplier", "CoreHeatContract:", true, thermos, 0, nil, nil);
    self:registerVariable("getSkinCelciusMultiplier", "SkinCelcius:", true, thermos, 0, nil, nil);

    self:registerVariable("title_stats","STATS",false);
    self:registerVariable("getHunger", "Hunger:", true, stats, 0, nil, nil);
    self:registerVariable("getThirst", "Thirst:", true, stats, 0, nil, nil);
    self:registerVariable("getFatigue", "Fatigue:", true, stats, 0, nil, nil);
    self:registerVariable("getFitness", "Fitness:", true, stats, 0, nil, nil);
    self:registerVariable("getDrunkenness", "Drunk:", true, stats, 0, nil, nil);
    self:registerVariable("getSickness", "Sickness:", true, stats, 0, nil, nil);

    self:registerVariable("title_body","BODY",false);
    self:registerVariable("getWetness", "Wetness:", true, body, 0, nil, nil);
    self:registerVariable("getColdStrength", "Cold-Strength:", true, body, 0, nil, nil);
    self:registerVariable("getCatchACold", "Catch-a-Cold:", true, body, 0, nil, nil);

    self:registerVariable("title_nutrition","NUTRITION",false);
    self:registerVariable("getWeight", "Weight:", true, nutrition, 0, nil, nil);
end

function PlayerClimateDebug:registerVariable(_variable,_title,_isValue, _javaInstance, _defaultVal,_color,_postfix)
    table.insert(self.vars, {
        variable = _variable,
        title = _title,
        isValue = _isValue,
        defaultVal = _defaultVal,
        color = _color,
        postfix = _postfix,
        javaInstance = _javaInstance,
    });
end

local vars = { -- ----------------------------------- OLD UNUSED
    --worldTemp = 0,
    --heatsourceTemp = 0,
    --vehicleTemp = 0,
    tempBase = 0,
    isInside = false,
    isInVehicle = false,
    airTemperature = 0,
    windSpeed = 0,

    hunger = 0,
    thirst = 0,
    tired = 0,
    excercise = 0,
    weight = 0,
    fitness = 0,
    drunkenness = 0,
    sickness = 0,
    hasACold = 0,
    wetness = 0,
    clothing = 0,
    --clothingFinal = 0,
    --windPower = 0,

    coldStrength = 0,
    heatStrength = 0,

    coldBodyResist = 0,
    heatBodyResist = 0,

    coldClothResist = 0,
    heatClothResist = 0,

    coldChange = 0,
    heatChange = 0,

    tickChangePm = 0,
    tickChange = 0,
    --appliedModTick = 0,
    playerTemp = 0,

    coldBodyBonus = 0,
    coldClothBonus = 0,
    heatBodyBonus = 0,
    heatClothBonus = 0,
    underCooled = 0,
    overHeated = 0,

    --extra
    --clothInvert = 0,
    --clothInvert2 = 0,
};

function PlayerClimateDebug.onClimateTickDebug()
    -- NOTE: This panel is now obsolete
    --[[
    local v = Temperature.getPlayerTemperatureVars(getPlayer());
    if not v then return end

    vars.tempBase = getWorld():getGlobalTemperature();

    vars.isInside = v:IsInside();
    vars.isInVehicle = v:IsInVehicle();
    vars.windSpeed = v:getWindSpeed();
    vars.airTemperature = v:getAirTemperature();

    vars.hunger = v:getHunger();
    vars.thirst = v:getThirst();
    vars.tired = v:getTired();
    vars.excercise = v:getExcercise();
    vars.weight = v:getWeight();
    vars.fitness = v:getFitness();
    vars.drunkenness = v:getDrunkenness();
    vars.sickness = v:getSickness();
    vars.hasACold = v:getHasACold();
    vars.wetness = v:getWetness();
    vars.clothing = v:getClothing();

    vars.coldStrength = v:getColdStrength();
    vars.coldBodyResist = v:getBodyColdResist();
    vars.coldClothResist = v:getClothColdResist();
    vars.coldBodyBonus = v:getDebugColdBodyBonus();
    vars.coldClothBonus = v:getDebugColdClothBonus();
    vars.coldResist = v:getColdResist();
    vars.coldChange = v:getColdChange();

    vars.heatStrength = v:getHeatStrength();
    vars.heatBodyResist = v:getBodyHeatResist();
    vars.heatClothResist = v:getClothHeatResist();
    vars.heatBodyBonus = v:getDebugHeatBodyBonus();
    vars.heatClothBonus = v:getDebugHeatClothBonus();
    vars.heatResist = v:getHeatResist();
    vars.heatChange = v:getHeatChange();

    vars.tickChangePm = v:getTickChangePm();
    vars.tickChange = v:getTickChange();

    vars.playerTemp = getPlayer():getTemperature();

    vars.underCooled = v:getUndercooled();
    vars.overHeated = v:getOverheated();
    --]]
end

function PlayerClimateDebug:addLabel(_curY, _labelID, _title)
    if not self.labels[_labelID] then
        local label = {};
        label.titleLabel = ISLabel:new(2, _curY, 16, _title, 1, 1, 1, 1.0, UIFont.Small, true);
        label.titleLabel:initialise();
        label.titleLabel:instantiate();
        self:addChild(label.titleLabel);

        self.labels[_labelID] = label;

        _curY = label.titleLabel:getY() + label.titleLabel:getHeight();
    else
        print("Cannot add label: "..tostring(_labelID));
    end
    return _curY;
end

function PlayerClimateDebug:addLabelValue(_curY, _type, _labelID, _title, _defaultVal, _col, _extension)
    local outLabel = nil;
    if not _col then
        _col = { r=1,g=1,b=1,a=1};
    end
    if not self.labels[_labelID] then
        local label = {};
        label.titleLabel = ISLabel:new(2, _curY, 16, _title, 1, 1, 1, 1.0, UIFont.Small, true);
        label.titleLabel:initialise();
        label.titleLabel:instantiate();
        self:addChild(label.titleLabel);

        if not _col then
            _col = self.colWhite;
        else
            label.hasCustCol = true;
        end

        if _type=="value" then
            label.valueLabel = ISLabel:new(self.width-(self.width/2), _curY, 16, tostring(_defaultVal), _col.r, _col.g, _col.b, 1.0, UIFont.Small, true);
            label.valueLabel:initialise();
            label.valueLabel:instantiate();
            self:addChild(label.valueLabel);
        elseif _type=="color" then
            label.valueLabel = ISPanel:new(self.width-(self.width/2), _curY+4, (self.width/3)-4, 10);
            label.valueLabel:initialise();
            label.valueLabel.backgroundColor = _defaultVal or {r=1.0,g=1.0,b=1.0,a=1.0};
            self:addChild(label.valueLabel);
        end

        if _extension then
            label.extLabel = ISLabel:new(self.width-(self.width/3), _curY, 16, tostring(_extension), 1, 1, 1, 1.0, UIFont.Small, true);
            label.extLabel:initialise();
            label.extLabel:instantiate();
            self:addChild(label.extLabel);
        end

        self.labels[_labelID] = label;
        outLabel = label;

        _curY = label.titleLabel:getY() + label.titleLabel:getHeight();
    else
        print("Cannot add label: "..tostring(_labelID));
    end
    return _curY, outLabel;
end

function PlayerClimateDebug:getTitleLabel(_labelID)
    if self.labels[_labelID] then
        return self.labels[_labelID].titleLabel;
    end
end

function PlayerClimateDebug:getValueLabel(_labelID)
    if self.labels[_labelID] then
        return self.labels[_labelID].valueLabel;
    end
end


function PlayerClimateDebug:onResize()
    ISUIElement.onResize(self);
    local th = self:titleBarHeight();
    --self.richtext:setWidth(self.width);
    --self.richtext:setHeight(self.height-(th+10));
end

local function round(num, numDecimalPlaces)
    local mult = 10^(numDecimalPlaces or 0)
    return math.floor(num * mult + 0.5) / mult
end

function PlayerClimateDebug:update()
    ISCollapsableWindow.update(self);

    for k,v in ipairs(self.vars) do
        if v.isValue then
            local l = self:getValueLabel(v.variable);
            local value = v.javaInstance[v.variable](v.javaInstance);
            l:setName( ""..tostring(round(value,6)) );
        end
    end
end

function PlayerClimateDebug:updateOLD()
    ISCollapsableWindow.update(self);

    if PlayerClimateDebug.shiftDown>0 then
        PlayerClimateDebug.shiftDown = PlayerClimateDebug.shiftDown-1;
    end

    --local vars = ClimateMain.getVars();

    if not vars.isInside then
        self:getValueLabel("isInside").name = "false";
    end

    local c = self.colWhite;
    local doCol = false;
    local val = 1;
    for k,v in pairs(vars) do
        doCol = false;
        if type(v)=="number" then
            local l = self:getValueLabel(k);
            if l then
                if l.colByValue then
                    doCol = true;
                    if v<=0 then
                        c = self.colRed;
                    else
                        c = self.colBlue;
                    end
                elseif l.colUnitInv or l.colUnit or l.colUnit2 or k=="clothingFinal" then
                    doCol = true;
                    val = v;
                    if l.colUnitInv then
                        val = 1-v;
                    end
                    if l.colUnit2 then
                        val = v/2;
                    end
                    if val>0.8 then
                        c = self.clrGreen;
                    elseif val>0.6 then
                        c = self.clrGreenYellow;
                    elseif val>0.4 then
                        c = self.clrGold;
                    elseif val>0.2 then
                        c = self.clrOrangeRed;
                    else
                        c = self.clrRed;
                    end
                elseif l.colUnitMinPlus then
                    doCol = true;
                    val = v;
                    if val>0.6 then
                        c = self.clrGreen;
                    elseif val>0.2 then
                        c = self.clrGreenYellow;
                    elseif val>-0.2 then
                        c = self.clrGold;
                    elseif val>-0.6 then
                        c = self.clrOrangeRed;
                    else
                        c = self.clrRed;
                    end
                elseif k=="playerTemp" then
                    doCol = true;
                    local hypo = getPlayer():getMoodles():getMoodleLevel(MoodleType.Hypothermia);
                    local hyper = getPlayer():getMoodles():getMoodleLevel(MoodleType.Hyperthermia);
                    if hypo==1 or hyper==1 then
                        c = self.clrGreenYellow;
                    elseif hypo==2 or hyper==2 then
                        c = self.clrGold;
                    elseif hypo==3 or hyper==3 then
                        c = self.clrOrangeRed;
                    elseif hypo==4 or hyper==4 then
                        c = self.clrRed;
                    else
                        c = self.clrGreen;
                    end
                    self:getValueLabel("hypoState").name = tostring(hypo);
                    self:getValueLabel("hyperState").name = tostring(hyper);
                end
                if doCol then
                    l.r = c.r;
                    l.b = c.b;
                    l.g = c.g;
                end
                l.name = ""..tostring(round(v,3));
            end
        elseif type(v)=="boolean" then
            --print("v="..tostring(v)..", str="..tostring(v==true and "true" or "false"));
            self:getValueLabel(k).name = v==true and "true" or "false";
        else
            self:getValueLabel(k).name = ""..tostring(v);
        end
    end

end

function PlayerClimateDebug:prerender()
    self:stayOnSplitScreen();
    ISCollapsableWindow.prerender(self);
end

function PlayerClimateDebug:stayOnSplitScreen()
    ISUIElement.stayOnSplitScreen(self, self.playerNum)
end


function PlayerClimateDebug:render()
    ISCollapsableWindow.render(self);

    --self.richtext:clearStencilRect();
end


function PlayerClimateDebug:close()
    ISCollapsableWindow.close(self);
    if JoypadState.players[self.playerNum+1] then
        setJoypadFocus(self.playerNum, nil)
    end
    self:removeFromUIManager();
    self:clear();
end

function PlayerClimateDebug:clear()
    self.currentTile = nil;

    if isDebugEnabled() then
        PlayerClimateDebug.instance = nil;
    end
end


function PlayerClimateDebug:new (x, y, width, height, player)
    local o = {}
    --o.data = {}
    o = ISCollapsableWindow:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.player = player;
    o.playerNum = player:getPlayerNum();
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.5};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.pin = true;
    o.isCollapsed = false;
    o.collapseCounter = 0;
    o.title = "Climate player";
    --o.viewList = {}
    o.resizable = true;
    o.drawFrame = true;

    o.currentTile = nil;
    o.richtext = nil;
    o.overrideBPrompt = true;
    o.subFocus = nil;
    o.hotKeyPanels = {};
    o.isJoypadWindow = false;
    ISDebugMenu.RegisterClass(self);
    return o
end

--[[
if enabled then
    Events.OnCustomUIKey.Add(PlayerClimateDebug.OnKeyDown);
    Events.OnKeyKeepPressed.Add(PlayerClimateDebug.OnKeepKeyDown);
    --Events.OnObjectLeftMouseButtonUp.Add(PlayerClimateDebug.onMouseButtonUp);
end--]]


