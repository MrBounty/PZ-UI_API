--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************


require "ISUI/ISCollapsableWindow"

---@class WeatherFXDebug : ISCollapsableWindow
WeatherFXDebug = ISCollapsableWindow:derive("WeatherFXDebug");
WeatherFXDebug.instance = nil;
WeatherFXDebug.shiftDown = 0;
WeatherFXDebug.eventAdded = false;

--local enabled = true; --getDebug();
local morganFreemanMode = false;

local function ClimateDebugOntick()
    if morganFreemanMode then
        getPlayer():getBodyDamage():RestoreToFullHealth();
    end
end
--[[
function WeatherFXDebug.OnKeepKeyDown(key)
    --backspace 13, shift 42, 54
    --print("KeyKeepDown = "..tostring(key));
    if key==42 or key==54 then
        WeatherFXDebug.shiftDown = 4;
    end
end

function WeatherFXDebug.OnKeyDown(key)
    --backspace 13, shift 42, 54
    --print("KeyDown = "..tostring(key));
    if WeatherFXDebug.shiftDown>0 and key ==14 then
        WeatherFXDebug.OnOpenPanel();
    end
end--]]

function WeatherFXDebug.OnOpenPanel()
    WeatherFXDebug.fx = getCell():getWeatherFX();

    if WeatherFXDebug.instance==nil then
        WeatherFXDebug.instance = WeatherFXDebug:new (100, 100, 300, 600, getPlayer());
        WeatherFXDebug.instance:initialise();
        WeatherFXDebug.instance:instantiate();
    end

    WeatherFXDebug.instance:addToUIManager();
    WeatherFXDebug.instance:setVisible(true);

    if not WeatherFXDebug.eventAdded then
        Events.OnTick.Add(ClimateDebugOntick);
        WeatherFXDebug.eventAdded = true;
    end

    return WeatherFXDebug.instance;
end

function WeatherFXDebug:initialise()
    ISCollapsableWindow.initialise(self);
end

function WeatherFXDebug:createChildren()
    ISCollapsableWindow.createChildren(self);

    local th = self:titleBarHeight();

    local y = th;

    self.labelWindIntensity = ISLabel:new(2, y, 16, "Wind intensity:", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelWindIntensity:initialise();
    self.labelWindIntensity:instantiate();
    self:addChild(self.labelWindIntensity);

    --y = self.labelWindIntensity:getY() + self.labelWindIntensity:getHeight();

    self.labelWindIntensityValue = ISLabel:new(self.width/2, y, 16, "0", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelWindIntensityValue:initialise();
    self.labelWindIntensityValue:instantiate();
    self:addChild(self.labelWindIntensityValue);

    y = self.labelWindIntensityValue:getY() + self.labelWindIntensityValue:getHeight();

    self.windIntensitySlider = ISVolumeControl:new(0, y+2, self.width, 10, self, self.onWindIntensityChange); --self.height-(th+10)); --, target, onchange);
    self.windIntensitySlider:initialise();
    self.windIntensitySlider:setVolume(0);
    self:addChild(self.windIntensitySlider);

    y = self.windIntensitySlider:getY() + self.windIntensitySlider:getHeight();

    --[[*******************************************************************************************--]]

    self.labelWindAngle = ISLabel:new(2, y, 16, "Wind angle:", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelWindAngle:initialise();
    self.labelWindAngle:instantiate();
    self:addChild(self.labelWindAngle);

    --y = self.labelWindAngle:getY() + self.labelWindAngle:getHeight();

    self.labelWindAngleValue = ISLabel:new(self.width/2, y, 16, "0", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelWindAngleValue:initialise();
    self.labelWindAngleValue:instantiate();
    self:addChild(self.labelWindAngleValue);

    y = self.labelWindAngleValue:getY() + self.labelWindAngleValue:getHeight();

    self.windAngleSlider = ISVolumeControl:new(0, y+2, self.width, 10, self, self.onWindAngleChange);
    self.windAngleSlider:initialise();
    self.windAngleSlider:setVolume(5);
    self:addChild(self.windAngleSlider);

    y = self.windAngleSlider:getY() + self.windAngleSlider:getHeight();

    --[[*******************************************************************************************--]]

    self.labelCloud = ISLabel:new(2, y, 16, "Cloud Intensity:", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelCloud:initialise();
    self.labelCloud:instantiate();
    self:addChild(self.labelCloud);

    --y = self.labelFog:getY() + self.labelFog:getHeight();

    self.labelCloudValue = ISLabel:new(self.width/2, y, 16, "0", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelCloudValue:initialise();
    self.labelCloudValue:instantiate();
    self:addChild(self.labelCloudValue);

    y = self.labelCloudValue:getY() + self.labelCloudValue:getHeight();

    self.cloudSlider = ISVolumeControl:new(0, y+2, self.width, 10, self, self.onCloudIntensityChange);
    self.cloudSlider:initialise();
    self.cloudSlider:setVolume(0);
    self:addChild(self.cloudSlider);

    y = self.cloudSlider:getY() + self.cloudSlider:getHeight();

    --[[*******************************************************************************************--]]

    self.labelFog = ISLabel:new(2, y, 16, "Fog Intensity:", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelFog:initialise();
    self.labelFog:instantiate();
    self:addChild(self.labelFog);

    --y = self.labelFog:getY() + self.labelFog:getHeight();

    self.labelFogValue = ISLabel:new(self.width/2, y, 16, "0", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelFogValue:initialise();
    self.labelFogValue:instantiate();
    self:addChild(self.labelFogValue);

    y = self.labelFogValue:getY() + self.labelFogValue:getHeight();

    self.fogSlider = ISVolumeControl:new(0, y+2, self.width, 10, self, self.onFogIntensityChange);
    self.fogSlider:initialise();
    self.fogSlider:setVolume(0);
    self:addChild(self.fogSlider);

    y = self.fogSlider:getY() + self.fogSlider:getHeight();

    --[[*******************************************************************************************--]]

    self.labelPrecipitation = ISLabel:new(2, y, 16, "Precipitation:", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelPrecipitation:initialise();
    self.labelPrecipitation:instantiate();
    self:addChild(self.labelPrecipitation);

    --y = self.labelPrecipitation:getY() + self.labelPrecipitation:getHeight();

    self.labelPrecipitationValue = ISLabel:new(self.width/2, y, 16, "0", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelPrecipitationValue:initialise();
    self.labelPrecipitationValue:instantiate();
    self:addChild(self.labelPrecipitationValue);

    y = self.labelPrecipitationValue:getY() + self.labelPrecipitationValue:getHeight();

    self.precipitationSlider = ISVolumeControl:new(0, y+2, self.width, 10, self, self.onPrecipitationChange);
    self.precipitationSlider:initialise();
    self.precipitationSlider:setVolume(0);
    self:addChild(self.precipitationSlider);

    y = self.precipitationSlider:getY() + self.precipitationSlider:getHeight();

    --[[*******************************************************************************************--]]

    self.labelIsSnow = ISLabel:new(2, y, 16, "Precipitation type:", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelIsSnow:initialise();
    self.labelIsSnow:instantiate();
    self:addChild(self.labelIsSnow);

    y = self.labelIsSnow:getY() + self.labelIsSnow:getHeight();

    self.tickBoxIsSnow = ISTickBox:new(2, y+2, self.width-4, 20, "", self, WeatherFXDebug.tickBoxIsSnowChange);
    --self.tickBoxIsSnow.onlyOnePossibility = true;
    self.tickBoxIsSnow.choicesColor = {r=1, g=1, b=1, a=1};
    self.tickBoxIsSnow:initialise();
    self.tickBoxIsSnow:instantiate();
    self.tickBoxIsSnow.selected[1] = getCell():getWeatherFX():getPrecipitationIsSnow();
    self.tickBoxIsSnow:addOption("Precipitation is snow");
    self:addChild(self.tickBoxIsSnow);

    y = self.tickBoxIsSnow:getY() + self.tickBoxIsSnow:getHeight();

    self.btnDebugBounds = ISButton:new(2, y+5, self.width-2 ,18,"DebugBounds Disabled",self, WeatherFXDebug.onButtonDebugBounds);
    self.btnDebugBounds:initialise();
    self.btnDebugBounds.backgroundColor = {r=0.8, g=0, b=0, a=1.0};
    self.btnDebugBounds.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.btnDebugBounds.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self:addChild(self.btnDebugBounds);

    y = self.btnDebugBounds:getY() + self.btnDebugBounds:getHeight();

    self.btnClimate = ISButton:new(2, y+5, self.width-2 ,18,"Climate Enabled",self, WeatherFXDebug.onButtonClimate);
    self.btnClimate:initialise();
    self.btnClimate.backgroundColor = {r=0, g=0.8, b=0, a=1.0};
    self.btnClimate.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.btnClimate.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self:addChild(self.btnClimate);

    y = self.btnClimate:getY() + self.btnClimate:getHeight();

    self.btnNightVision = ISButton:new(2, y+5, self.width-2 ,18,"NightVisionGoggles Disabled",self, WeatherFXDebug.onButtonNightVision);
    self.btnNightVision:initialise();
    self.btnNightVision.backgroundColor = {r=0.8, g=0, b=0, a=1.0};
    self.btnNightVision.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.btnNightVision.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self:addChild(self.btnNightVision);

    y = self.btnNightVision:getY() + self.btnNightVision:getHeight();

    self.btnLaunchFlare = ISButton:new(2, y+5, self.width-2 ,18,"Launch test flare",self, WeatherFXDebug.onButtonLaunchFlare);
    self.btnLaunchFlare:initialise();
    self.btnLaunchFlare.backgroundColor = {r=0.0, g=0, b=0, a=1.0};
    self.btnLaunchFlare.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.btnLaunchFlare.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self:addChild(self.btnLaunchFlare);

    y = self.btnLaunchFlare:getY() + self.btnLaunchFlare:getHeight();

    self.btnGodMode = ISButton:new(2, y+5, self.width-2 ,18,"GodMode = false",self, WeatherFXDebug.onButtonGodMode);
    self.btnGodMode:initialise();
    self.btnGodMode.backgroundColor = {r=0.8, g=0, b=0, a=1.0};
    self.btnGodMode.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.btnGodMode.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self:addChild(self.btnGodMode);

    y = self.btnGodMode:getY() + self.btnGodMode:getHeight();

    --[[*******************************************************************************************--]]

    --[[
    self.labelLightColorR = ISLabel:new(2, y, 16, "LightColorR:", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelLightColorR:initialise();
    self.labelLightColorR:instantiate();
    self:addChild(self.labelLightColorR);

    --y = self.labelPrecipitation:getY() + self.labelPrecipitation:getHeight();

    self.labelLightColorRValue = ISLabel:new(self.width/2, y, 16, "0", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelLightColorRValue:initialise();
    self.labelLightColorRValue:instantiate();
    self:addChild(self.labelLightColorRValue);

    y = self.labelLightColorRValue:getY() + self.labelLightColorRValue:getHeight();

    self.LightColorRSlider = ISVolumeControl:new(0, y+2, self.width, 10, self, self.onLightColorChange);
    self.LightColorRSlider:initialise();
    self.LightColorRSlider:setVolume(0);
    self:addChild(self.LightColorRSlider);

    y = self.LightColorRSlider:getY() + self.LightColorRSlider:getHeight();
    --]]

    --[[*******************************************************************************************--]]

    --[[
    self.labelLightColorG = ISLabel:new(2, y, 16, "LightColorG:", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelLightColorG:initialise();
    self.labelLightColorG:instantiate();
    self:addChild(self.labelLightColorG);

    --y = self.labelPrecipitation:getY() + self.labelPrecipitation:getHeight();

    self.labelLightColorGValue = ISLabel:new(self.width/2, y, 16, "0", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelLightColorGValue:initialise();
    self.labelLightColorGValue:instantiate();
    self:addChild(self.labelLightColorGValue);

    y = self.labelLightColorGValue:getY() + self.labelLightColorGValue:getHeight();

    self.LightColorGSlider = ISVolumeControl:new(0, y+2, self.width, 10, self, self.onLightColorChange);
    self.LightColorGSlider:initialise();
    self.LightColorGSlider:setVolume(0);
    self:addChild(self.LightColorGSlider);

    y = self.LightColorGSlider:getY() + self.LightColorGSlider:getHeight();
    --]]

    --[[*******************************************************************************************--]]

    --[[
    self.labelLightColorB = ISLabel:new(2, y, 16, "LightColorB:", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelLightColorB:initialise();
    self.labelLightColorB:instantiate();
    self:addChild(self.labelLightColorB);

    --y = self.labelPrecipitation:getY() + self.labelPrecipitation:getHeight();

    self.labelLightColorBValue = ISLabel:new(self.width/2, y, 16, "0", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelLightColorBValue:initialise();
    self.labelLightColorBValue:instantiate();
    self:addChild(self.labelLightColorBValue);

    y = self.labelLightColorBValue:getY() + self.labelLightColorBValue:getHeight();

    self.LightColorBSlider = ISVolumeControl:new(0, y+2, self.width, 10, self, self.onLightColorChange);
    self.LightColorBSlider:initialise();
    self.LightColorBSlider:setVolume(0);
    self:addChild(self.LightColorBSlider);

    y = self.LightColorBSlider:getY() + self.LightColorBSlider:getHeight();
    --]]

    --[[*******************************************************************************************--]]
    --[[
    self.labelLightColorA = ISLabel:new(2, y, 16, "LightColorA:", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelLightColorA:initialise();
    self.labelLightColorA:instantiate();
    self:addChild(self.labelLightColorA);

    --y = self.labelPrecipitation:getY() + self.labelPrecipitation:getHeight();

    self.labelLightColorAValue = ISLabel:new(self.width/2, y, 16, "0", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelLightColorAValue:initialise();
    self.labelLightColorAValue:instantiate();
    self:addChild(self.labelLightColorAValue);

    y = self.labelLightColorAValue:getY() + self.labelLightColorAValue:getHeight();

    self.LightColorASlider = ISVolumeControl:new(0, y+2, self.width, 10, self, self.onLightColorChange);
    self.LightColorASlider:initialise();
    self.LightColorASlider:setVolume(0);
    self:addChild(self.LightColorASlider);

    y = self.LightColorASlider:getY() + self.LightColorASlider:getHeight();

    --]]
    --[[*******************************************************************************************--]]

    self.LightColorPanel = ISPanel:new(0, y+2, self.width, 10);
    self.LightColorPanel:initialise();
    self.LightColorPanel.backgroundColor = {r=0.0,g=0.0,b=0.0,a=1.0};
    self:addChild(self.LightColorPanel);

    y = self.LightColorPanel:getY() + self.LightColorPanel:getHeight();

    --[[*******************************************************************************************--]]

    --[[
    self.labelLightIntensity = ISLabel:new(2, y, 16, "LightIntensity:", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelLightIntensity:initialise();
    self.labelLightIntensity:instantiate();
    self:addChild(self.labelLightIntensity);

    --y = self.labelPrecipitation:getY() + self.labelPrecipitation:getHeight();

    self.labelLightIntensityValue = ISLabel:new(self.width/2, y, 16, "0", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelLightIntensityValue:initialise();
    self.labelLightIntensityValue:instantiate();
    self:addChild(self.labelLightIntensityValue);

    y = self.labelLightIntensityValue:getY() + self.labelLightIntensityValue:getHeight();

    self.LightIntensitySlider = ISVolumeControl:new(0, y+2, self.width, 10, self, self.onLightIntensityChange);
    self.LightIntensitySlider:initialise();
    self.LightIntensitySlider:setVolume(0);
    self:addChild(self.LightIntensitySlider);

    y = self.LightIntensitySlider:getY() + self.LightIntensitySlider:getHeight();
    --]]

    --[[*******************************************************************************************--]]

    self.labelDesaturation = ISLabel:new(2, y, 16, "Desaturation:", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelDesaturation:initialise();
    self.labelDesaturation:instantiate();
    self:addChild(self.labelDesaturation);

    --y = self.labelPrecipitation:getY() + self.labelPrecipitation:getHeight();

    self.labelDesaturationValue = ISLabel:new(self.width/2, y, 16, "0", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelDesaturationValue:initialise();
    self.labelDesaturationValue:instantiate();
    self:addChild(self.labelDesaturationValue);

    y = self.labelDesaturationValue:getY() + self.labelDesaturationValue:getHeight();

    self.DesaturationSlider = ISVolumeControl:new(0, y+2, self.width, 10, self, self.onDesaturationChange);
    self.DesaturationSlider:initialise();
    self.DesaturationSlider:setVolume(0);
    self:addChild(self.DesaturationSlider);

    y = self.DesaturationSlider:getY() + self.DesaturationSlider:getHeight();

    --[[*******************************************************************************************--]]
    self.labelDayLightStrength = ISLabel:new(2, y, 16, "DayLightStrength:", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelDayLightStrength:initialise();
    self.labelDayLightStrength:instantiate();
    self:addChild(self.labelDayLightStrength);

    --y = self.labelPrecipitation:getY() + self.labelPrecipitation:getHeight();

    self.labelDayLightStrengthValue = ISLabel:new(self.width/2, y, 16, "0", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelDayLightStrengthValue:initialise();
    self.labelDayLightStrengthValue:instantiate();
    self:addChild(self.labelDayLightStrengthValue);

    y = self.labelDayLightStrengthValue:getY() + self.labelDayLightStrengthValue:getHeight();

    self.DayLightStrengthSlider = ISVolumeControl:new(0, y+2, self.width, 10, self, self.onDayLightStrengthChange);
    self.DayLightStrengthSlider:initialise();
    self.DayLightStrengthSlider:setVolume(0);
    self:addChild(self.DayLightStrengthSlider);

    y = self.DayLightStrengthSlider:getY() + self.DayLightStrengthSlider:getHeight();

    --[[*******************************************************************************************--]]

    self.labelNightStrength = ISLabel:new(2, y, 16, "NightStrength:", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelNightStrength:initialise();
    self.labelNightStrength:instantiate();
    self:addChild(self.labelNightStrength);

    --y = self.labelPrecipitation:getY() + self.labelPrecipitation:getHeight();

    self.labelNightStrengthValue = ISLabel:new(self.width/2, y, 16, "0", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelNightStrengthValue:initialise();
    self.labelNightStrengthValue:instantiate();
    self:addChild(self.labelNightStrengthValue);

    y = self.labelNightStrengthValue:getY() + self.labelNightStrengthValue:getHeight();

    self.NightStrengthSlider = ISVolumeControl:new(0, y+2, self.width, 10, self, self.onNightStrengthChange);
    self.NightStrengthSlider:initialise();
    self.NightStrengthSlider:setVolume(0);
    self:addChild(self.NightStrengthSlider);

    y = self.NightStrengthSlider:getY() + self.NightStrengthSlider:getHeight();

    --[[*******************************************************************************************--]]
    self.labelAmbient = ISLabel:new(2, y, 16, "Ambient:", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelAmbient:initialise();
    self.labelAmbient:instantiate();
    self:addChild(self.labelAmbient);

    --y = self.labelPrecipitation:getY() + self.labelPrecipitation:getHeight();

    self.labelAmbientValue = ISLabel:new(self.width/2, y, 16, "0", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelAmbientValue:initialise();
    self.labelAmbientValue:instantiate();
    self:addChild(self.labelAmbientValue);

    y = self.labelAmbientValue:getY() + self.labelAmbientValue:getHeight();

    self.AmbientSlider = ISVolumeControl:new(0, y+2, self.width, 10, self, self.onAmbientChange);
    self.AmbientSlider:initialise();
    self.AmbientSlider:setVolume(0);
    self:addChild(self.AmbientSlider);

    y = self.AmbientSlider:getY() + self.AmbientSlider:getHeight();

    --[[*******************************************************************************************--]]
    self.labelViewDistance = ISLabel:new(2, y, 16, "ViewDistance:", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelViewDistance:initialise();
    self.labelViewDistance:instantiate();
    self:addChild(self.labelViewDistance);

    --y = self.labelPrecipitation:getY() + self.labelPrecipitation:getHeight();

    self.labelViewDistanceValue = ISLabel:new(self.width/2, y, 16, "0", 1, 1, 1, 1.0, UIFont.Small, true);
    self.labelViewDistanceValue:initialise();
    self.labelViewDistanceValue:instantiate();
    self:addChild(self.labelViewDistanceValue);

    y = self.labelViewDistanceValue:getY() + self.labelViewDistanceValue:getHeight();

    self.ViewDistanceSlider = ISVolumeControl:new(0, y+2, self.width, 10, self, self.onViewDistanceChange);
    self.ViewDistanceSlider:initialise();
    self.ViewDistanceSlider:setVolume(0);
    self:addChild(self.ViewDistanceSlider);

    y = self.ViewDistanceSlider:getY() + self.ViewDistanceSlider:getHeight();

    --[[*******************************************************************************************--]]

    self.btnStorm = ISButton:new(2, y+5, self.width-2 ,18,"Trigger 48h Storm",self, WeatherFXDebug.onButtonDoStorm);
    self.btnStorm:initialise();
    self.btnStorm.backgroundColor = {r=0.0, g=0, b=0, a=1.0};
    self.btnStorm.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.btnStorm.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self:addChild(self.btnStorm);

    y = self.btnStorm:getY() + self.btnStorm:getHeight();

    self.btnTropical = ISButton:new(2, y+5, self.width-2 ,18,"Trigger 48h TropicalStorm",self, WeatherFXDebug.onButtonDoTropical);
    self.btnTropical:initialise();
    self.btnTropical.backgroundColor = {r=0.0, g=0, b=0, a=1.0};
    self.btnTropical.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.btnTropical.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self:addChild(self.btnTropical);

    y = self.btnTropical:getY() + self.btnTropical:getHeight();

    self.btnBlizzard = ISButton:new(2, y+5, self.width-2 ,18,"Trigger 48h Blizzard",self, WeatherFXDebug.onButtonDoBlizzard);
    self.btnBlizzard:initialise();
    self.btnBlizzard.backgroundColor = {r=0.0, g=0, b=0, a=1.0};
    self.btnBlizzard.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.btnBlizzard.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self:addChild(self.btnBlizzard);

    y = self.btnBlizzard:getY() + self.btnBlizzard:getHeight();

    --[[*******************************************************************************************--]]

    self:setHeight(y+self:resizeWidgetHeight()+4);
end

function WeatherFXDebug:onButtonGodMode(_btn)
    if _btn==self.btnGodMode then
        morganFreemanMode = not morganFreemanMode;
        if morganFreemanMode then
            self.btnGodMode.title = "GodMode = true";
            self.btnGodMode.backgroundColor = {r=0, g=0.8, b=0, a=1.0};
        else
            self.btnGodMode.title = "GodMode = false";
            self.btnGodMode.backgroundColor = {r=0.8, g=0, b=0, a=1.0};
        end
    end
end

function WeatherFXDebug:onButtonDebugBounds(_btn)
    if _btn==self.btnDebugBounds then
        local fx = WeatherFXDebug.fx;
        if WeatherFXDebug.fx then
            WeatherFXDebug.fx:setDebugBounds(not WeatherFXDebug.fx:isDebugBounds());
            if WeatherFXDebug.fx:isDebugBounds() then
                self.btnDebugBounds.title = "DebugBounds Enabled";
                self.btnDebugBounds.backgroundColor = {r=0, g=0.8, b=0, a=1.0};
            else
                self.btnDebugBounds.title = "DebugBounds Disabled";
                self.btnDebugBounds.backgroundColor = {r=0.8, g=0, b=0, a=1.0};
            end
        end
    end
end

function WeatherFXDebug:onButtonClimate(_btn)
    if _btn==self.btnClimate then
        local clim = getWorld():getClimateManager();
        if clim then
            clim:setEnabledSimulation(not clim:getEnabledSimulation());
            clim:setEnabledFxUpdate(clim:getEnabledSimulation());
            if clim:getEnabledSimulation() then
                self.btnClimate.title = "Climate Enabled";
                self.btnClimate.backgroundColor = {r=0, g=0.8, b=0, a=1.0};
            else
                self.btnClimate.title = "Climate Disabled";
                self.btnClimate.backgroundColor = {r=0.8, g=0, b=0, a=1.0};
            end
        end
    end
end

function WeatherFXDebug:onButtonDoStorm(_btn)
    if _btn==self.btnStorm then
        local clim = getWorld():getClimateManager();
        if clim then
            clim:triggerCustomWeatherStage(3,48);
        end
    end
end

function WeatherFXDebug:onButtonDoTropical(_btn)
    if _btn==self.btnTropical then
        local clim = getWorld():getClimateManager();
        if clim then
            clim:triggerCustomWeatherStage(8,48);
        end
    end
end

function WeatherFXDebug:onButtonDoBlizzard(_btn)
    if _btn==self.btnBlizzard then
        local clim = getWorld():getClimateManager();
        if clim then
            clim:triggerCustomWeatherStage(7,48);
        end
    end
end

function WeatherFXDebug:onButtonNightVision(_btn)
    if _btn==self.btnNightVision then
        local plr = getPlayer();
        if plr then
            plr:setWearingNightVisionGoggles(not plr:isWearingNightVisionGoggles());
            if plr:isWearingNightVisionGoggles() then
                self.btnNightVision.title = "NightVisionGoggles Enabled";
                self.btnNightVision.backgroundColor = {r=0, g=0.8, b=0, a=1.0};
            else
                self.btnNightVision.title = "NightVisionGoggles Disabled";
                self.btnNightVision.backgroundColor = {r=0.8, g=0, b=0, a=1.0};
            end
        end
    end
end

function WeatherFXDebug:onButtonLaunchFlare(_btn)
    if _btn==self.btnLaunchFlare then
        local clim = getWorld():getClimateManager();
        if clim then
            clim:launchFlare()
        end
    end
end

function WeatherFXDebug:onLightColorChange(_slider, _value)
    --TODO disabled due to changes made in color, need to remove sliders from this panel
    --getClimateManager():setGlobalLight(self.LightColorRSlider.volume*0.1,self.LightColorGSlider.volume*0.1,self.LightColorBSlider.volume*0.1,1.0);
    --self.LightColorPanel.backgroundColor = {r=self.LightColorRSlider.volume*0.1,g=self.LightColorGSlider.volume*0.1,b=self.LightColorBSlider.volume*0.1,a=1.0};
    --self.labelWindIntensityValue.name = ""..tostring(WeatherFXDebug.fx:getWindIntensity());
end

function WeatherFXDebug:onAmbientChange(_slider, _value)
    getClimateManager():setAmbient(_value*0.1);
    --self.labelWindIntensityValue.name = ""..tostring(WeatherFXDebug.fx:getWindIntensity());
end

function WeatherFXDebug:onViewDistanceChange(_slider, _value)
    getClimateManager():setViewDistance((_value*0.1)*30);
    --self.labelWindIntensityValue.name = ""..tostring(WeatherFXDebug.fx:getWindIntensity());
end

function WeatherFXDebug:onDesaturationChange(_slider, _value)
    getClimateManager():setDesaturation(_value*0.1);
    --self.labelWindIntensityValue.name = ""..tostring(WeatherFXDebug.fx:getWindIntensity());
end

function WeatherFXDebug:onLightIntensityChange(_slider, _value)
    --getClimateManager():setGlobalLightIntensity(_value*0.1);
    --self.labelWindIntensityValue.name = ""..tostring(WeatherFXDebug.fx:getWindIntensity());
end

function WeatherFXDebug:onNightStrengthChange(_slider, _value)
    getClimateManager():setNightStrength(_value*0.1);
    --self.labelWindIntensityValue.name = ""..tostring(WeatherFXDebug.fx:getWindIntensity());
end

function WeatherFXDebug:onDayLightStrengthChange(_slider, _value)
    getClimateManager():setDayLightStrength(_value*0.1);
    --self.labelWindIntensityValue.name = ""..tostring(WeatherFXDebug.fx:getWindIntensity());
end

function WeatherFXDebug:onWindIntensityChange(_slider, _value)
    WeatherFXDebug.fx:setWindIntensity(_value*0.1);

    --self.labelWindIntensityValue.name = ""..tostring(WeatherFXDebug.fx:getWindIntensity());
end

function WeatherFXDebug:onWindAngleChange(_slider, _value)
    WeatherFXDebug.fx:setWindAngleIntensity((_value*0.2)-1);
    --self.labelWindAngleValue.name = ""..tostring(WeatherFXDebug.fx:getWindAngleIntensity());
end

function WeatherFXDebug:onPrecipitationChange(_slider, _value)
    WeatherFXDebug.fx:setPrecipitationIntensity(_value*0.1);
    --self.labelPrecipitationValue.name = ""..tostring(WeatherFXDebug.fx:getPrecipitationIntensity());
end

function WeatherFXDebug:onFogIntensityChange(_slider, _value)
    WeatherFXDebug.fx:setFogIntensity(_value*0.1);
end

function WeatherFXDebug:onCloudIntensityChange(_slider, _value)
    WeatherFXDebug.fx:setCloudIntensity(_value*0.1);
end

function WeatherFXDebug:tickBoxIsSnowChange(_optionIndex, _value)
    print("option = "..tostring(_optionIndex)..", value = "..tostring(_value));
    WeatherFXDebug.fx:setPrecipitationIsSnow(_value);
    print("debug = "..tostring(WeatherFXDebug.fx:getPrecipitationIsSnow()));
end

function WeatherFXDebug:onResize()
    ISUIElement.onResize(self);
    local th = self:titleBarHeight();
    --self.richtext:setWidth(self.width);
    --self.richtext:setHeight(self.height-(th+10));
end

function WeatherFXDebug:update()
    ISCollapsableWindow.update(self);

    if WeatherFXDebug.shiftDown>0 then
        WeatherFXDebug.shiftDown = WeatherFXDebug.shiftDown-1;
    end

    self.tickBoxIsSnow.selected[1] = WeatherFXDebug.fx:getPrecipitationIsSnow();
    self.labelFogValue.name = ""..tostring(WeatherFXDebug.fx:getFogIntensity());
    self.labelCloudValue.name = ""..tostring(WeatherFXDebug.fx:getCloudIntensity());
    self.labelWindIntensityValue.name = ""..tostring(WeatherFXDebug.fx:getWindIntensity());
    self.labelWindAngleValue.name = ""..tostring(WeatherFXDebug.fx:getWindAngleIntensity());
    self.labelPrecipitationValue.name = ""..tostring(WeatherFXDebug.fx:getPrecipitationIntensity());

    local col = getClimateManager():getGlobalLight():getExterior();
    --self.labelLightColorRValue.name = ""..tostring(col:getRedFloat());
    --self.labelLightColorGValue.name = ""..tostring(col:getGreenFloat());
    --self.labelLightColorBValue.name = ""..tostring(col:getBlueFloat());
    --self.labelLightColorAValue.name = ""..tostring(col:getAlphaFloat());

    self.LightColorPanel.backgroundColor.r = col:getRedFloat();
    self.LightColorPanel.backgroundColor.g = col:getGreenFloat();
    self.LightColorPanel.backgroundColor.b = col:getBlueFloat();
    self.LightColorPanel.backgroundColor.a = col:getAlphaFloat();

    --self.labelLightIntensityValue.name = ""..tostring(getClimateManager():getGlobalLightIntensity());
    self.labelNightStrengthValue.name = ""..tostring(getClimateManager():getNightStrength());
    self.labelDayLightStrengthValue.name = ""..tostring(getClimateManager():getDayLightStrength());
    self.labelDesaturationValue.name = ""..tostring(getClimateManager():getDesaturation());
    self.labelAmbientValue.name = ""..tostring(getClimateManager():getAmbient());
    self.labelViewDistanceValue.name = ""..tostring(getClimateManager():getViewDistance());
end

function WeatherFXDebug:prerender()
    self:stayOnSplitScreen();
    ISCollapsableWindow.prerender(self);
end

function WeatherFXDebug:stayOnSplitScreen()
    ISUIElement.stayOnSplitScreen(self, self.playerNum)
end


function WeatherFXDebug:render()
    ISCollapsableWindow.render(self);

    --self.richtext:clearStencilRect();
end


function WeatherFXDebug:close()
    ISCollapsableWindow.close(self);
    if JoypadState.players[self.playerNum+1] then
        setJoypadFocus(self.playerNum, nil)
    end
    self:removeFromUIManager();
    self:clear();
end

function WeatherFXDebug:clear()
    self.currentTile = nil;
end


function WeatherFXDebug:new (x, y, width, height, player)
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
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.pin = true;
    o.isCollapsed = false;
    o.collapseCounter = 0;
    o.title = "FX debug";
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
local function ClimateDebugOntick()
    if morganFreemanMode then
        getPlayer():getBodyDamage():RestoreToFullHealth();
    end
end

if enabled then
    Events.OnTick.Add(ClimateDebugOntick);
    Events.OnCustomUIKey.Add(WeatherFXDebug.OnKeyDown);
    Events.OnKeyKeepPressed.Add(WeatherFXDebug.OnKeepKeyDown);
    --Events.OnObjectLeftMouseButtonUp.Add(WeatherFXDebug.onMouseButtonUp);
end--]]
