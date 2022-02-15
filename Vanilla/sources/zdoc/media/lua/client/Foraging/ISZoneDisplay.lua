-------------------------------------------------
-------------------------------------------------
--
-- ISZoneDisplay
--
-- eris
--
-------------------------------------------------
-------------------------------------------------
local zdImage = ISPanel:derive("zdImage");
---@class ISZoneDisplay : ISPanel
ISZoneDisplay = ISPanel:derive("ISZoneDisplay");
-------------------------------------------------
-------------------------------------------------
local zdTex = {
    frame = {
        stars = getTexture("media/textures/Foraging/ISZoneDisplay/zd_stars.png"),
        clouds = getTexture("media/textures/Foraging/ISZoneDisplay/zd_clouds.png"),
        fog1 = getTexture("media/textures/Foraging/ISZoneDisplay/zd_fog1.png"),
        frame = getTexture("media/textures/Foraging/ISZoneDisplay/zd_frame.png"),
        -- fog2 = getTexture("media/textures/ISZoneDisplay/zd_fog2.png"),
        -- fog3 = getTexture("media/textures/ISZoneDisplay/zd_fog3.png"),
    },
    moons = {
        moon0 = getTexture("media/textures/Foraging/ISZoneDisplay/moon/zd_moon0.png"),
        moon1 = getTexture("media/textures/Foraging/ISZoneDisplay/moon/zd_moon1.png"),
        moon2 = getTexture("media/textures/Foraging/ISZoneDisplay/moon/zd_moon2.png"),
        moon3 = getTexture("media/textures/Foraging/ISZoneDisplay/moon/zd_moon3.png"),
        moon4 = getTexture("media/textures/Foraging/ISZoneDisplay/moon/zd_moon4.png"),
        moon5 = getTexture("media/textures/Foraging/ISZoneDisplay/moon/zd_moon5.png"),
        moon6 = getTexture("media/textures/Foraging/ISZoneDisplay/moon/zd_moon6.png"),
        moon7 = getTexture("media/textures/Foraging/ISZoneDisplay/moon/zd_moon7.png"),
    },
    world = {
        sun = getTexture("media/textures/Foraging/ISZoneDisplay/zd_sun.png"),
        moon = getTexture("media/textures/Foraging/ISZoneDisplay/zd_moon.png"),
    },
    zone = {
        Forest          = getTexture("media/textures/Foraging/ISZoneDisplay/zones/Forest.png"),
        DeepForest      = getTexture("media/textures/Foraging/ISZoneDisplay/zones/DeepForest.png"),
        Farm            = getTexture("media/textures/Foraging/ISZoneDisplay/zones/Farm.png"),
        FarmLand        = getTexture("media/textures/Foraging/ISZoneDisplay/zones/FarmLand.png"),
        Nav             = getTexture("media/textures/Foraging/ISZoneDisplay/zones/Nav.png"),
        TownZone        = getTexture("media/textures/Foraging/ISZoneDisplay/zones/TownZone.png"),
        TrailerPark     = getTexture("media/textures/Foraging/ISZoneDisplay/zones/TrailerPark.png"),
        Unknown         = getTexture("media/textures/Foraging/ISZoneDisplay/zones/Unknown.png"),
        Vegitation      = getTexture("media/textures/Foraging/ISZoneDisplay/zones/Vegitation.png"),
    },
};
-------------------------------------------------
-------------------------------------------------
local floor, sin, antiPi = math.floor, math.sin, 0-math.pi;
-------------------------------------------------
-------------------------------------------------
function ISZoneDisplay:updateMoonPosition(_dawn, _dusk, _timeOfDay)
    local nightLength = (24 - _dusk) + _dawn;
    local nightRemaining;
    local beforeMidnight = false;
    if _timeOfDay > _dusk then
        nightRemaining = (24 - _timeOfDay) + _dawn;
        beforeMidnight = true;
    else
        nightRemaining = _dawn - _timeOfDay;
    end;
    local nightRatio = (nightRemaining / nightLength);
    if nightRatio > 0.5 then
        self.stars:setAlpha(1 - nightRatio);
    else
        self.stars:setAlpha(nightRatio);
    end;
    local height = self.height * 0.33;
    self.moon:setX(math.max(self.width - self.moon:getWidth() - (self.width * nightRatio), 0));
    self.moon:setY(math.max( height + (height * sin(nightRatio * antiPi)), 0));
end
-------------------------------------------------
-------------------------------------------------
function ISZoneDisplay:updateSunPosition(_dawn, _dusk, _timeOfDay)
    local dayLength = _dusk - _dawn;
    local dayRemaining = _dusk - _timeOfDay;
    local dayRatio = (dayRemaining / dayLength);
    local height = self.height * 0.33;
    self.sun:setX(math.max(self.width - self.sun:getWidth() - (self.width * dayRatio), 0));
    self.sun:setY(math.max( height + (height * sin(dayRatio * antiPi)), 0));
end
-------------------------------------------------
-------------------------------------------------
local function getZoneType(_x, _y)
    local zones = getWorld():getMetaGrid():getZonesAt(_x, _y, 0);
    if zones then
        for i = 0, zones:size() - 1 do
            local zone = zones:get(i);
            if zone then
                if forageSystem.zoneDefs[zone:getType()] then
                    return zone:getType();
                end;
            end;
        end;
    end;
    return "Unknown";
end
-------------------------------------------------
-------------------------------------------------
function ISZoneDisplay:doFadeStep()
    for target, element in pairs(self.fadeElements) do
        if self.fadeTarget == target then
            element:setAlphaTarget(1);
        else
            element:setAlphaTarget(0);
        end;
    end;
end
-------------------------------------------------
-------------------------------------------------
function ISZoneDisplay:canSeeThroughObject(_object)
    if (not _object) or _object:getObjectIndex() == -1 then return false; end;
    local object = _object;
    if instanceof(object, "IsoWindow") then
        local curtains = object:HasCurtains();
        if curtains then
            if curtains:IsOpen() then
                return true;
            end;
        else
            return true;
        end;
    end;
    if instanceof(object, "IsoDoor") then
        if object:getProperties() and object:getProperties():Is("doorTrans") then
            if not object:getProperties():Is("GarageDoor") then
                local curtains = object:HasCurtains();
                if curtains then
                    if curtains:IsOpen() then
                        return true;
                    end;
                else
                    return true;
                end;
            end;
        end;
    end;
    return false;
end

function ISZoneDisplay:canSeeOutside()
    if self.character:isOutside() then return true; end;
    local playerDirection = self.character:getDir();
    if playerDirection then
        local plX, plY, plZ = self.character:getX(), self.character:getY(), self.character:getZ();
        local plSquare = self.character:getCurrentSquare();

        local squareTable = {};
        for x = -1, 1 do
            for y = -1, 1 do
                table.insert(squareTable, getCell():getGridSquare(plX + x, plY + y, plZ));
            end;
        end;

        local directionTable = {};
        directionTable[IsoDirections.N]      = {squareTable[1], squareTable[4], squareTable[7]};
        directionTable[IsoDirections.S]      = {squareTable[3], squareTable[6], squareTable[9]};
        directionTable[IsoDirections.E]      = {squareTable[7], squareTable[8], squareTable[9]};
        directionTable[IsoDirections.W]      = {squareTable[1], squareTable[2], squareTable[3]};
        directionTable[IsoDirections.NE]     = {squareTable[4], squareTable[7], squareTable[8]};
        directionTable[IsoDirections.NW]     = {squareTable[1], squareTable[2], squareTable[4]};
        directionTable[IsoDirections.SE]     = {squareTable[6], squareTable[8], squareTable[9]};
        directionTable[IsoDirections.SW]     = {squareTable[2], squareTable[3], squareTable[6]};

        if directionTable[playerDirection] then
            for _, square in ipairs(directionTable[playerDirection]) do
                if square and square:isOutside() then
                    if not plSquare:isBlockedTo(square) then return true; end;
                    if square:isCanSee(self.player) then return true; end;
                    if plSquare:isWindowTo(square) or plSquare:isDoorTo(square) then
                        local objects = square:getObjects();
                        for i = 0, objects:size() - 1 do
                            local object = objects:get(i);
                            if object and self:canSeeThroughObject(object) then
                                return true;
                            end;
                        end;
                        local objects = plSquare:getObjects();
                        for i = 0, objects:size() - 1 do
                            local object = objects:get(i);
                            if object and self:canSeeThroughObject(object) then
                                return true;
                            end;
                        end;
                    end;
                end;
            end;
        end;
    end;
    return false;
end
-------------------------------------------------
-------------------------------------------------
function ISZoneDisplay:updateLocation()
    if not self.canSeeSky then
        self.sun:setAlphaTarget(0);
        self.moon:setAlphaTarget(0);
        self.stars:setAlphaTarget(0);
        self.clouds:setAlphaTarget(0);
        self.fog1:setAlphaTarget(0);
    end;
    local zoneType = getZoneType(self.character:getX(), self.character:getY());
    self.currentZone = zoneType;
    if self.fadeTarget ~= zoneType then
        self.fadeTarget = zoneType;
        --self.parent:setTitle(getText("UI_investigate_area_window_title") .. ": " .. zoneType);
    end;
end
-------------------------------------------------
-------------------------------------------------
function ISZoneDisplay:getTooltipText()
    local fuzzyChanceTable = {
        [1] = {text = getText"Sandbox_Rarity_option1", chance = 5},
        [2] = {text = getText"Sandbox_Rarity_option2", chance = 20},
        [3] = {text = getText"Sandbox_Rarity_option4", chance = 50},
        [4] = {text = getText"Sandbox_Rarity_option5", chance = 1000},
    };
    local text = "";
    self.tooltip:setName(getText("IGUI_SearchMode_Zone_Names_"..self.currentZone));
    if self.currentZone == "Unknown" then
        text = text .. " <LINE> " .. getText("IGUI_SearchMode_Window_Tooltip_Nothing_In_Area");
    else
        local catDefs = forageSystem.catDefs;
        local chanceTable = {};
        local totalChance = 0;
        local zoneChance;
        local categoryName;
        for catName, catDef in pairs(catDefs) do
            zoneChance = catDef.zoneChance[self.currentZone];
            if zoneChance and zoneChance > 0 then
                --show exact categories in forage debug mode
                if ISSearchManager.showDebug then
                    categoryName = catName;
                else
                    categoryName = getText("IGUI_SearchMode_Categories_"..catDef.typeCategory);
                end;
                if not chanceTable[categoryName] then
                    chanceTable[categoryName] = 0;
                end;
                totalChance = totalChance + zoneChance;
                chanceTable[categoryName] = chanceTable[categoryName] + zoneChance;
            end;
        end;
        text = text .. " <LINE> " .. getText("IGUI_SearchMode_Window_Tooltip_Categories_In_Area").. " <LINE> ";
        local chanceFormatted;
        local fuzzyChance = "Unknown";
        local chanceTableSorted = {};
        for k, v in pairs(chanceTable) do
            table.insert(chanceTableSorted, {name = k, chance = v});
        end;
        table.sort(chanceTableSorted, function (a, b)return a.chance > b.chance; end);
        for _, chanceCategory in ipairs(chanceTableSorted) do
            local exactChance = (chanceCategory.chance / totalChance);
            for _, fuzzyTable in ipairs(fuzzyChanceTable) do
                if (exactChance * 100) <= fuzzyTable.chance then
                    fuzzyChance = fuzzyTable.text;
                    break;
                end;
            end;
            chanceFormatted = string.format("%.2f", exactChance * 100);
            text = text .. " <LINE> <RGB:1,1,1> <TEXT> ".. chanceCategory.name ..":  <RGB:"..0.5-exactChance..","..0.5+exactChance..",0> ";
            if ISSearchManager.showDebug then
                text = text  .. " : " .. chanceFormatted .. "%";
            else
                text = text  .. fuzzyChance;
            end;
        end;
    end;
    return text;
end
-------------------------------------------------
-------------------------------------------------
function ISZoneDisplay:updateTooltip()
    if not self.tooltip then
        self.tooltip = ISToolTip:new();
        self.tooltip:setOwner(self);
        self.tooltip:setVisible(false);
        self.tooltip:setAlwaysOnTop(true);
    end;
    if self:isMouseOver() and self.tooltip then
        if not self.tooltip:getIsVisible() then
            self.tooltip:addToUIManager();
            self.tooltip:setVisible(true);
        end;
        self.tooltip.description = self:getTooltipText();
        --self.tooltip:setTexture(self.zones[self.currentZone]);
        self.tooltip:setX(self:getMouseX() + 23);
        self.tooltip:setY(self:getMouseY() + 23);
    else
        if self.tooltip and self.tooltip:getIsVisible() then
            self.tooltip:setVisible(false);
            self.tooltip:removeFromUIManager();
        end;
    end;
end
-------------------------------------------------
-------------------------------------------------
function ISZoneDisplay:update()
    if not self:getIsVisible() then return; end;
    if self.parent and (not self.parent:getIsVisible()) then return; end;
    --self.clouds:setAlphaTarget(1);
    --self.fog1:setAlphaTarget(1);
    self:updateData();
    if self.timeOfDay < self.dawn or self.timeOfDay > self.dusk then
        local moonColor = math.max(self.moonBright, 0.5);
        self.moon:setColor(moonColor, moonColor, moonColor);
        self.sun:setAlphaTarget(0);
        self.moon:setAlphaTarget(1);
        self.stars:setAlphaTarget(0);
        self:updateMoonPosition(self.dawn, self.dusk, self.timeOfDay);
    else
        local sunColor = math.max(self.sunBright, 0.5);
        self.sun:setColor(1, 1, sunColor);
        self.sun:setAlphaTarget(1);
        self.moon:setAlphaTarget(0);
        self.stars:setAlphaTarget(0);
        self:updateSunPosition(self.dawn, self.dusk, self.timeOfDay);
    end;
    self:doFadeStep();
    self:updateLocation();
    self:updateTooltip();
    self.updateTick = self.updateTick + 1;
    if self.updateTick % 5 == 0 then self.canSeeSky = self:canSeeOutside(); end;
    if self.updateTick >= self.updateTickMax then self.updateTick = 0; end;
end
-------------------------------------------------
-------------------------------------------------
function ISZoneDisplay:isLeapYear(_yearNum)
    return (_yearNum % 4 == 0) and ((_yearNum % 400 == 0) or (_yearNum % 100 ~= 0));
end

function ISZoneDisplay:updateMoonPhase()
    local currentPhase = self.climateMoon:getCurrentMoonPhase() or 0;
    self.moon.texture = zdTex.moons["moon"..currentPhase];
end
-------------------------------------------------
-------------------------------------------------
function ISZoneDisplay:updateData()
    local gameTime = self.gameTime;
    local climateManager = self.climateManager;
    local season = climateManager:getSeason();
    self.timeOfDay = gameTime:getTimeOfDay();
    self.dawn = season:getDawn();
    self.noon = season:getDayHighNoon();
    self.dusk = season:getDusk();
    self.cloudIntensity = climateManager:getCloudIntensity();
    self.fogIntensity = climateManager:getFogIntensity();
    self.sunBright = climateManager:getDayLightStrength();
    self.moonBright = climateManager:getNightStrength();
    local globalLight = climateManager:getGlobalLight();
    local extLight = globalLight:getExterior();
    self.backgroundColor = {
        r = extLight:getRedFloat(),
        g = extLight:getGreenFloat(),
        b = extLight:getBlueFloat(),
        a = 0.7,
    };
    self.clouds:setAlphaTarget(math.min(self.cloudIntensity, 0.95));
    self.fog1:setAlphaTarget(math.min(self.fogIntensity, 0.9));
    self.clouds:setGreyscale(1 - (self.cloudIntensity / 2));
    self:updateMoonPhase();
end
-------------------------------------------------
-------------------------------------------------
function ISZoneDisplay:initialise()
    ISPanel.initialise(self);
    for imageName, imageTex in pairs(zdTex.zone) do
        self[imageName] = zdImage:new(self,0, 0, self.width, self.height, imageTex);
        self[imageName]:initialise();
        self[imageName]:setAlpha(0);
        self[imageName]:setAlphaTarget(0);
        self:addChild(self[imageName]);
        self.zdImages[imageName] = self[imageName];
        self.fadeElements[imageName] = self[imageName];
    end;
    for imageName, imageTex in pairs(zdTex.world) do
        self[imageName] = zdImage:new(self,0, 0, 24, 24, imageTex);
        self[imageName]:initialise();
        self:addChild(self[imageName]);
        self.zdImages[imageName] = self[imageName];
    end;
    for imageName, imageTex in pairs(zdTex.frame) do
        self[imageName] = zdImage:new(self,0, 0, self.width, self.height, imageTex);
        self[imageName]:initialise();
        self[imageName]:setAlpha(0);
        self[imageName]:setAlphaTarget(0);
        self:addChild(self[imageName]);
        self.zdImages[imageName] = self[imageName];
    end;
    self.frame:setAlpha(1);
    self.frame:setAlphaTarget(1);
    self.frame:setColor(0.4, 0.4, 0.4);
    self:update();
    self.canSeeSky = self:canSeeOutside();
end
-------------------------------------------------
-------------------------------------------------
function ISZoneDisplay:close()    self:setVisible(false); self:removeFromUIManager();     end;

function ISZoneDisplay:new(_parent)
    local yPos = _parent and _parent:titleBarHeight() + 2 or 20;
    local o = ISPanel:new(0, yPos, 100, 40);
    setmetatable(o, self);
    self.__index = self;

    o.x = 0;
    o.y = yPos;
    o.width = 300;
    o.height = 120;
    o.moveWithMouse = false;

    o.showBackground = true;
    o.showBorder = true;
    o.backgroundColor = {r=0.5, g=0.5, b=0.5, a=0.7};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=0};
    o.alpha = 1;

    o.zdImages = {};
    o.fadeTarget = "TownZone";
    o.fadeElements = {};

    o.timeOfDay = 0;
    o.dawn = 0;
    o.noon = 0;
    o.dusk = 0;

    o.timeString = "";
    o.moonPhase = "";

    o.cloudIntensity = 0;
    o.fogIntensity = 0;
    o.sunBright = 0;
    o.moonBright = 0;

    o.canSeeSky = false;
    o.updateTick = 0;
    o.updateTickMax = 100;

    --todo: co-op support
    o.player = 0;
    o.character = getSpecificPlayer(0);
    o.gameTime = getGameTime();
    o.climateManager = getClimateManager();
    o.climateMoon = getClimateMoon();

    o.currentZone = "Unknown";

    o:initialise();

    return o;
end
-------------------------------------------------
-------------------------------------------------
function zdImage:initialise()         ISPanel.initialise(self);   end;
function zdImage:getAlpha()           return self.backgroundColor.a;    end;
function zdImage:getAlphaTarget()     return self.alphaTarget;          end;
function zdImage:setAlpha(_a)         self.backgroundColor.a = _a;      end;
function zdImage:setAlphaTarget(_a)   self.alphaTarget = _a;            end;
-------------------------------------------------
-------------------------------------------------
function zdImage:setGreyscale(_rgb)
    self.backgroundColor.r = _rgb;
    self.backgroundColor.g = _rgb;
    self.backgroundColor.b = _rgb;
end

function zdImage:setColor(_r,_g,_b)
    self.backgroundColor.r = _r;
    self.backgroundColor.g = _g;
    self.backgroundColor.b = _b;
end
-------------------------------------------------
-------------------------------------------------
function zdImage:update()
   if self:getAlpha() <= self.alphaTarget then self:setAlpha(math.min(self:getAlpha() + self.alphaStep, 1)); end;
   if self:getAlpha() >= self.alphaTarget then self:setAlpha(math.max(self:getAlpha() - self.alphaStep, 0)); end;
end

function zdImage:prerender() end;
function zdImage:render()
    local bgc = self.backgroundColor;
    self:drawTextureScaled(self.texture, 0, 0, self.width, self.height, bgc.a, bgc.r, bgc.g, bgc.b);
end
-------------------------------------------------
-------------------------------------------------
function zdImage:new(zoneDisplay, x, y, width, height, texture)
    local o = {};
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self);
    self.__index = self
    o:noBackground();
    o.x = x;
    o.y = y;
    o.zoneDisplay = zoneDisplay;
    o.texture = texture;
    o.backgroundColor = {r=1, g=1, b=1, a=1};
    o.borderColor = {r=1, g=1, b=1, a=0};
    o.width = width;
    o.height = height;
    o.alphaTarget = 1;
    o.alphaStep = 0.05;
    return o;
end
-------------------------------------------------
-------------------------------------------------
