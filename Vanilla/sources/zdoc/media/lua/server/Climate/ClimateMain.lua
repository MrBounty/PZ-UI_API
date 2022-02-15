--[[
-- Generated file (ClimateMain_20201015_100656)
-- Climate color configuration
-- File should be placed in: media/lua/server/Climate/ClimateMain.lua (remove date stamp)
--]]

---@class ClimateMain
ClimateMain = {};
ClimateMain.versionStamp = "20201015_100656";

local WARM,NORMAL,CLOUDY = 0,1,2;

local SUMMER,FALL,WINTER,SPRING = 0,1,2,3;

function ClimateMain.onClimateManagerInit(_clim)
    local c;
    c = _clim:getColNightNoMoon();
    c:setExterior(0.25,0.25,0.7,0.8);
    c:setInterior(0.06,0.06,0.35,0.4);

    c = _clim:getColNightMoon();
    c:setExterior(0.33,0.33,1.0,0.8);
    c:setInterior(0.12,0.13,0.4,0.4);

    c = _clim:getColFog();
    c:setExterior(0.2,0.2,0.4,0.8);
    c:setInterior(0.1,0.1,0.5,0.5);

    c = _clim:getColFogLegacy();
    c:setExterior(0.3,0.3,0.3,0.8);
    c:setInterior(0.3,0.3,0.3,0.8);

    c = _clim:getColFogNew();
    c:setExterior(0.5,0.5,0.55,0.8);
    c:setInterior(0.4,0.4,0.45,0.7);

    c = _clim:getFogTintStorm();
    c:setExterior(0.5,0.45,0.4,1.0);
    c:setInterior(0.5,0.45,0.4,1.0);

    c = _clim:getFogTintTropical();
    c:setExterior(0.8,0.75,0.55,1.0);
    c:setInterior(0.8,0.75,0.55,1.0);

    local w = _clim:getWeatherPeriod();

    c = w:getCloudColorReddish();
    c:setExterior(0.4,0.05,0.05,0.6);
    c:setInterior(0.21,0.05,0.05,0.5);

    c = w:getCloudColorGreenish();
    c:setExterior(0.14,0.27,0.05,0.6);
    c:setInterior(0.07,0.15,0.02,0.5);

    c = w:getCloudColorBlueish();
    c:setExterior(0.13,0.28,0.3,0.6);
    c:setInterior(0.05,0.15,0.16,0.43);

    c = w:getCloudColorPurplish();
    c:setExterior(0.38,0.12,0.49,0.7);
    c:setInterior(0.26,0.03,0.34,0.37);

    c = w:getCloudColorTropical();
    c:setExterior(0.4,0.2,0.2,0.4);
    c:setInterior(0.4,0.2,0.2,0.4);

    c = w:getCloudColorBlizzard();
    c:setExterior(0.38,0.4,0.5,0.8);
    c:setInterior(0.12,0.13,0.21,0.5);

    -- ###################### Dawn ######################
    _clim:setSeasonColorDawn(WARM,SUMMER,0.57,0.69,0.75,0.75,true);		--exterior
    _clim:setSeasonColorDawn(WARM,SUMMER,0.13,0.32,0.37,0.26,false);		--interior

    _clim:setSeasonColorDawn(CLOUDY,SUMMER,0.41,0.49,0.61,0.66,true);		--exterior
    _clim:setSeasonColorDawn(CLOUDY,SUMMER,0.05,0.14,0.22,0.38,false);		--interior

    _clim:setSeasonColorDawn(WARM,FALL,0.61,0.5,0.72,0.75,true);		--exterior
    _clim:setSeasonColorDawn(WARM,FALL,0.25,0.18,0.35,0.26,false);		--interior

    _clim:setSeasonColorDawn(CLOUDY,FALL,0.45,0.33,0.46,0.68,true);		--exterior
    _clim:setSeasonColorDawn(CLOUDY,FALL,0.23,0.14,0.3,0.37,false);		--interior

    _clim:setSeasonColorDawn(WARM,WINTER,0.48,0.57,0.63,0.75,true);		--exterior
    _clim:setSeasonColorDawn(WARM,WINTER,0.14,0.21,0.32,0.41,false);		--interior

    _clim:setSeasonColorDawn(CLOUDY,WINTER,0.24,0.32,0.4,0.69,true);		--exterior
    _clim:setSeasonColorDawn(CLOUDY,WINTER,0.03,0.06,0.13,0.63,false);		--interior

    _clim:setSeasonColorDawn(WARM,SPRING,0.57,0.66,0.64,0.75,true);		--exterior
    _clim:setSeasonColorDawn(WARM,SPRING,0.08,0.14,0.11,0.54,false);		--interior

    _clim:setSeasonColorDawn(CLOUDY,SPRING,0.39,0.44,0.41,0.73,true);		--exterior
    _clim:setSeasonColorDawn(CLOUDY,SPRING,0.07,0.11,0.08,0.63,false);		--interior

    -- ###################### Day ######################
    _clim:setSeasonColorDay(WARM,SUMMER,0.86,0.82,0.74,0.79,true);		--exterior
    _clim:setSeasonColorDay(WARM,SUMMER,0.41,0.26,0.0,0.14,false);		--interior

    _clim:setSeasonColorDay(CLOUDY,SUMMER,0.55,0.51,0.55,0.84,true);		--exterior
    _clim:setSeasonColorDay(CLOUDY,SUMMER,0.25,0.19,0.26,0.28,false);		--interior

    _clim:setSeasonColorDay(WARM,FALL,0.84,0.7,0.54,0.8,true);		--exterior
    _clim:setSeasonColorDay(WARM,FALL,0.65,0.28,0.05,0.15,false);		--interior

    _clim:setSeasonColorDay(CLOUDY,FALL,0.55,0.44,0.5,0.8,true);		--exterior
    _clim:setSeasonColorDay(CLOUDY,FALL,0.4,0.23,0.31,0.3,false);		--interior

    _clim:setSeasonColorDay(WARM,WINTER,0.71,0.59,0.46,0.75,true);		--exterior
    _clim:setSeasonColorDay(WARM,WINTER,0.34,0.18,0.05,0.31,false);		--interior

    _clim:setSeasonColorDay(CLOUDY,WINTER,0.35,0.35,0.41,0.75,true);		--exterior
    _clim:setSeasonColorDay(CLOUDY,WINTER,0.07,0.07,0.11,0.74,false);		--interior

    _clim:setSeasonColorDay(WARM,SPRING,0.7,0.75,0.65,0.7,true);		--exterior
    _clim:setSeasonColorDay(WARM,SPRING,0.35,0.43,0.3,0.17,false);		--interior

    _clim:setSeasonColorDay(CLOUDY,SPRING,0.45,0.5,0.45,0.7,true);		--exterior
    _clim:setSeasonColorDay(CLOUDY,SPRING,0.07,0.09,0.06,0.52,false);		--interior

    -- ###################### Dusk ######################
    _clim:setSeasonColorDusk(WARM,SUMMER,0.9,0.45,0.2,0.8,true);		--exterior
    _clim:setSeasonColorDusk(WARM,SUMMER,0.24,0.09,0.02,0.79,false);		--interior

    _clim:setSeasonColorDusk(NORMAL,SUMMER,0.57,0.64,0.71,0.8,true);		--exterior
    _clim:setSeasonColorDusk(NORMAL,SUMMER,0.1,0.19,0.26,0.39,false);		--interior

    _clim:setSeasonColorDusk(CLOUDY,SUMMER,0.94,0.4,0.54,0.85,true);		--exterior
    _clim:setSeasonColorDusk(CLOUDY,SUMMER,0.34,0.05,0.12,0.5,false);		--interior

    _clim:setSeasonColorDusk(WARM,FALL,0.8,0.39,0.28,0.85,true);		--exterior
    _clim:setSeasonColorDusk(WARM,FALL,0.37,0.11,0.05,0.48,false);		--interior

    _clim:setSeasonColorDusk(NORMAL,FALL,0.45,0.35,0.48,0.7,true);		--exterior
    _clim:setSeasonColorDusk(NORMAL,FALL,0.16,0.09,0.19,0.47,false);		--interior

    _clim:setSeasonColorDusk(CLOUDY,FALL,0.73,0.32,0.4,0.75,true);		--exterior
    _clim:setSeasonColorDusk(CLOUDY,FALL,0.27,0.09,0.16,0.49,false);		--interior

    _clim:setSeasonColorDusk(WARM,WINTER,0.52,0.4,0.32,0.93,true);		--exterior
    _clim:setSeasonColorDusk(WARM,WINTER,0.21,0.1,0.07,0.44,false);		--interior

    _clim:setSeasonColorDusk(NORMAL,WINTER,0.35,0.4,0.46,0.85,true);		--exterior
    _clim:setSeasonColorDusk(NORMAL,WINTER,0.1,0.15,0.22,0.47,false);		--interior

    _clim:setSeasonColorDusk(CLOUDY,WINTER,0.53,0.3,0.4,0.9,true);		--exterior
    _clim:setSeasonColorDusk(CLOUDY,WINTER,0.24,0.08,0.15,0.53,false);		--interior

    _clim:setSeasonColorDusk(WARM,SPRING,0.64,0.4,0.3,0.88,true);		--exterior
    _clim:setSeasonColorDusk(WARM,SPRING,0.25,0.12,0.06,0.53,false);		--interior

    _clim:setSeasonColorDusk(NORMAL,SPRING,0.43,0.5,0.45,0.75,true);		--exterior
    _clim:setSeasonColorDusk(NORMAL,SPRING,0.1,0.15,0.12,0.73,false);		--interior

    _clim:setSeasonColorDusk(CLOUDY,SPRING,0.56,0.43,0.6,0.85,true);		--exterior
    _clim:setSeasonColorDusk(CLOUDY,SPRING,0.15,0.08,0.2,0.67,false);		--interior

end

Events.OnClimateManagerInit.Add(ClimateMain.onClimateManagerInit);
