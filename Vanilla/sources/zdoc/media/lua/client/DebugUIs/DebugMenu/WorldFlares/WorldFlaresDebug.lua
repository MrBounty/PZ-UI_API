--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "ISUI/ISPanel"

---@class WorldFlaresDebug : ISPanel
WorldFlaresDebug = ISPanel:derive("WorldFlaresDebug");
WorldFlaresDebug.instance = nil;

local function roundstring(_val)
    return tostring(ISDebugUtils.roundNum(_val,2));
end

function WorldFlaresDebug.OnOpenPanel()
    if WorldFlaresDebug.instance==nil then
        WorldFlaresDebug.instance = WorldFlaresDebug:new (100, 100, 400, 600, "Flares debugger");
        WorldFlaresDebug.instance:initialise();
        WorldFlaresDebug.instance:instantiate();
    end

    WorldFlaresDebug.instance:addToUIManager();
    WorldFlaresDebug.instance:setVisible(true);

    return WorldFlaresDebug.instance;
end

function WorldFlaresDebug:initialise()
    ISPanel.initialise(self);

    self.flareCount = false;
    self.colExt = { r=1, g=1, b=1 };
    self.colInt = { r=1, g=1, b=1 };
    self.flareID = -1;
end

function WorldFlaresDebug:createChildren()
    ISPanel.createChildren(self);

    ISDebugUtils.addLabel(self, {}, 10, 20, "World Flares Debugger", UIFont.Medium, true);

    local boxWidth = 100;
    local x1, x2 = 10, 75;
    local y, obj = 50, false;

    local tickOptions = {};
    table.insert(tickOptions, { text = "[DEBUG] Show range.", ticked = WorldFlares.getDebugDraw() });
    y, obj = ISDebugUtils.addTickBox(self,{},x1,y,175,ISDebugUtils.FONT_HGT_SMALL,"Debug range",tickOptions,WorldFlaresDebug.onTicked);

    y = y + 5;

    _, obj = ISDebugUtils.addLabel(self, {}, x1, y, "Lifetime:", UIFont.Small, true);
    y, self.entryBoxLifeTime = ISDebugUtils.addTextEntryBox(self, {}, "60", x2, y, boxWidth, 20);
    self.entryBoxLifeTime:setOnlyNumbers(true);

    y = y + 3;
    _, obj = ISDebugUtils.addLabel(self, {}, x1, y, "Range:", UIFont.Small, true);
    y, self.entryBoxRange = ISDebugUtils.addTextEntryBox(self, {}, "50", x2, y, boxWidth, 20);
    self.entryBoxRange:setOnlyNumbers(true);

    y = y + 3;
    _, obj = ISDebugUtils.addLabel(self, {}, x1, y, "Windspeed:", UIFont.Small, true);
    y, self.entryBoxWindspeed = ISDebugUtils.addTextEntryBox(self, {}, "0.00014", x2, y, boxWidth, 20);
    self.entryBoxWindspeed:setOnlyNumbers(true);

    y = y + 3;

    y, obj = ISDebugUtils.addLabel(self, {}, x1, y, "ExteriorColor:", UIFont.Small, true);

    y = y + 3;

    self.colExtBoxY = y;

    y = y + 23;

    x2 = 25;
    boxWidth = 175;

    self.colBoxWidth = boxWidth;

    local objLabel;
    --_, objLabel = ISDebugUtils.addLabel(self, {}, x1, y, "0", UIFont.Small, false);
    y, obj = ISDebugUtils.addSlider(self, "ext_r", x1, y, boxWidth, 18, WorldFlaresDebug.onSliderChange);
    --obj.pretext = "G_exterior: ";
    obj.valueLabel = objLabel;
    obj:setValues(0, 1, 0.01, 0.01);
    obj:setCurrentValue(1);

    y = y + 3;

    --_, objLabel = ISDebugUtils.addLabel(self, {}, x1, y, "0", UIFont.Small, false);
    y, obj = ISDebugUtils.addSlider(self, "ext_g", x1, y, boxWidth, 18, WorldFlaresDebug.onSliderChange);
    --obj.pretext = "G_exterior: ";
    obj.valueLabel = objLabel;
    obj:setValues(0, 1, 0.01, 0.01);
    obj:setCurrentValue(1);

    y = y + 3;

    --_, objLabel = ISDebugUtils.addLabel(self, {}, x1, y, "0", UIFont.Small, false);
    y, obj = ISDebugUtils.addSlider(self, "ext_b", x1, y, boxWidth, 18, WorldFlaresDebug.onSliderChange);
    --obj.pretext = "G_exterior: ";
    obj.valueLabel = objLabel;
    obj:setValues(0, 1, 0.01, 0.01);
    obj:setCurrentValue(1);

    y = y + 3;

    y, obj = ISDebugUtils.addLabel(self, {}, x1, y, "InteriorColor:", UIFont.Small, true);

    y = y + 3;

    self.colIntBoxY = y;

    y = y + 23;

    local objLabel;
    --_, objLabel = ISDebugUtils.addLabel(self, {}, x1, y, "0", UIFont.Small, false);
    y, obj = ISDebugUtils.addSlider(self, "int_r", x1, y, boxWidth, 18, WorldFlaresDebug.onSliderChange);
    --obj.pretext = "G_exterior: ";
    obj.valueLabel = objLabel;
    obj:setValues(0, 1, 0.01, 0.01);
    obj:setCurrentValue(1);

    y = y + 3;

    --_, objLabel = ISDebugUtils.addLabel(self, {}, x1, y, "0", UIFont.Small, false);
    y, obj = ISDebugUtils.addSlider(self, "int_g", x1, y, boxWidth, 18, WorldFlaresDebug.onSliderChange);
    --obj.pretext = "G_exterior: ";
    obj.valueLabel = objLabel;
    obj:setValues(0, 1, 0.01, 0.01);
    obj:setCurrentValue(1);

    y = y + 3;

    --_, objLabel = ISDebugUtils.addLabel(self, {}, x1, y, "0", UIFont.Small, false);
    y, obj = ISDebugUtils.addSlider(self, "int_b", x1, y, boxWidth, 18, WorldFlaresDebug.onSliderChange);
    --obj.pretext = "G_exterior: ";
    obj.valueLabel = objLabel;
    obj:setValues(0, 1, 0.01, 0.01);
    obj:setCurrentValue(1);

    y = y + 3;

    y, obj = ISDebugUtils.addButton(self,"close",x1,y,boxWidth,20,"add flare",WorldFlaresDebug.onClickAddFlare);

    y = y + 3;

    self.windowY = y;

    self:setHeight(self.windowY+40);
    self:setWidth(570);

    self.flaresList = ISScrollingListBox:new(x1+boxWidth+10, 50, 150, self.height - 100);
    self.flaresList:initialise();
    self.flaresList:instantiate();
    self.flaresList.itemheight = 22;
    self.flaresList.selected = 0;
    self.flaresList.joypadParent = self;
    self.flaresList.font = UIFont.NewSmall;
    self.flaresList.doDrawItem = self.drawFlaresList;
    self.flaresList.drawBorder = true;
    self.flaresList.onmousedown = WorldFlaresDebug.OnFlaresListMouseDown;
    self.flaresList.target = self;
    self:addChild(self.flaresList);

    local bx, by = self.flaresList:getX(), self.flaresList:getY()+self.flaresList:getHeight()+10;
    local bw, bh = self.flaresList:getWidth(), 20;
    ISDebugUtils.addButton(self,"close",bx,by,bw,20,"delete flares",WorldFlaresDebug.onClickDeleteFlares);

    self.infoX = self.flaresList:getX() + self.flaresList:getWidth()+20;
    self.infoY = 50;
    self.infoWidth = self:getWidth() - self.infoX;

    local y, obj = ISDebugUtils.addButton(self,"close",self.width-200,self.height-40,180,20,getText("IGUI_CraftUI_Close"),WorldFlaresDebug.onClickClose);

    self:populateList();
end

function WorldFlaresDebug:onTicked(_index, _selected, _arg1, _arg2, _tickbox)
    --local v = _tickbox.customData;
    --v.lua.cheat = not v.lua.cheat;
    print("index = "..tostring(_index))
    print("selected = "..tostring(_selected))
    print("arg1 = "..tostring(_arg1))
    print("arg2 = "..tostring(_arg2))
    WorldFlares.setDebugDraw(_selected);
end

function WorldFlaresDebug:onSliderChange(_newVal, _slider)
    if _slider.customData=="ext_r" then
        self.colExt.r = _newVal;
    end
    if _slider.customData=="ext_g" then
        self.colExt.g = _newVal;
    end
    if _slider.customData=="ext_b" then
        self.colExt.b = _newVal;
    end
    if _slider.customData=="int_r" then
        self.colInt.r = _newVal;
    end
    if _slider.customData=="int_g" then
        self.colInt.g = _newVal;
    end
    if _slider.customData=="int_b" then
        self.colInt.b = _newVal;
    end

end

function WorldFlaresDebug:onClickDeleteFlares()
    WorldFlares.Clear();
end

function WorldFlaresDebug:onClickAddFlare()
    local lifeTime = self.entryBoxLifeTime:getInternalText();
    lifeTime = (not lifeTime or lifeTime=="") and 60 or tonumber(lifeTime);
    local range = self.entryBoxRange:getInternalText();
    range = (not range or range=="") and 50 or tonumber(range);
    local windspeed = self.entryBoxWindspeed:getInternalText();
    windspeed = (not windspeed or windspeed=="") and -1 or tonumber(windspeed);
    if windspeed<0 then
        windspeed = 0;
    end
    local plr = getPlayer(0);
    local x = plr:getX();
    local y = plr:getY();
    local c = self.colExt;
    local c2 = self.colInt;

    lifeTime = lifeTime*60;
    WorldFlares.launchFlare(lifeTime, x, y, range, windspeed, c.r, c.g, c.b, c2.r, c2.g, c2.b);
end

function WorldFlaresDebug:onClickClose()
    self:close();
end

function WorldFlaresDebug:OnFlaresListMouseDown(item)
    --self:populateInfoList(item);
    self.flareID = item:getId();
end

function WorldFlaresDebug:populateList()
    local count = WorldFlares.getFlareCount();

    if self.flareCount and self.flareCount==count then
        return;
    end

    self.flaresList:clear();

    for i=0, count-1 do
        local flare = WorldFlares.getFlare(i);

        local id = flare:getId();
        self.flaresList:addItem("Flare ["..tostring(id).."]", flare);
    end

    self.flareCount = count;
end

function WorldFlaresDebug:drawFlaresList(y, item, alt)
    local a = 0.9;

    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
    end

    local flare = item.item;

    --drawRect( x, y, w, h, a, r, g, b)
    local alpha = 1.0;
    local c = flare:getColor():getExterior();
    self:drawRect( self:getWidth()-62, (y+2), 20, self.itemheight - 6, alpha, c:getRedFloat(), c:getGreenFloat(), c:getBlueFloat());
    c = flare:getColor():getInterior();
    self:drawRect( self:getWidth()-40, (y+2), 20, self.itemheight - 6, alpha, c:getRedFloat(), c:getGreenFloat(), c:getBlueFloat());

    --local prefix = item.item:getIndexOffset()==0 and "TODAY" or tostring(item.item:getIndexOffset());
    --self:drawText( prefix .. " :: " .. item.item:getName(), 10, y + 2, 1, 1, 1, a, self.font);
    --if item.item:isHasFog() then
    --    self:drawText( item.text .. " (F)", 10, y + 2, 0.8, 1, 0.75, a, self.font);
    --else
        self:drawText( item.text, 10, y + 2, 1, 1, 1, a, self.font);
    --end

    return y + self.itemheight;
end


function WorldFlaresDebug:prerender()
    ISPanel.prerender(self);
    self:populateList();

    self:drawRect( 10, self.colExtBoxY, self.colBoxWidth, 20, 1.0, self.colExt.r, self.colExt.g, self.colExt.b);
    self:drawRect( 10, self.colIntBoxY, self.colBoxWidth, 20, 1.0, self.colInt.r, self.colInt.g, self.colInt.b);

    local x = self.infoX;
    local y = self.infoY;
    local w = self.infoWidth-10;
    local a = 1.0;

    local flare = false;
    if self.flareID and self.flareID>=0 then
        flare = WorldFlares.getFlareID(self.flareID);
    end

    if flare then
        local yOffset = 16;
        self:drawText( "Flare ["..tostring(flare:getId()).."]", x, y, 1, 1, 1, a, UIFont.Small);
        y = y+yOffset;
        self:drawText( "x = "..roundstring(flare:getX()), x, y, 1, 1, 1, a, UIFont.Small);
        y = y+yOffset;
        self:drawText( "y = "..roundstring(flare:getY()), x, y, 1, 1, 1, a, UIFont.Small);
        y = y+yOffset;
        self:drawText( "range = "..tostring(flare:getRange()), x, y, 1, 1, 1, a, UIFont.Small);
        y = y+yOffset;
        self:drawText( "windspeed = "..roundstring(flare:getWindSpeed()), x, y, 1, 1, 1, a, UIFont.Small);
        y = y+yOffset;
        self:drawText( "intensity(b) = "..roundstring(flare:getIntensity()), x, y, 1, 1, 1, a, UIFont.Small);
        y = y+yOffset;
        self:drawText( "maxLifetime = "..roundstring(flare:getMaxLifeTime()), x, y, 1, 1, 1, a, UIFont.Small);
        y = y+yOffset;
        self:drawText( "lifetime = "..roundstring(flare:getLifeTime()), x, y, 1, 1, 1, a, UIFont.Small);
        y = y+yOffset;
        self:drawText( "percent = "..roundstring(flare:getPercent()), x, y, 1, 1, 1, a, UIFont.Small);
        y = y+yOffset;
        self:drawText( "intensity = "..roundstring(flare:getIntensityPlayer(0)), x, y, 1, 1, 1, a, UIFont.Small);
        y = y+yOffset;
        self:drawText( "lerp = "..roundstring(flare:getLerpPlayer(0)), x, y, 1, 1, 1, a, UIFont.Small);
        y = y+yOffset;
        self:drawText( "distMod = "..roundstring(flare:getDistModPlayer(0)), x, y, 1, 1, 1, a, UIFont.Small);
        y = y+yOffset;
        self:drawText( "Flare color:", x, y, 1, 1, 1, a, UIFont.Small);
        y = y+yOffset;
        local fcol = flare:getColorPlayer(0);
        local c = fcol:getExterior();
        local h = 20;
        self:drawRect( x, y, (w/2)-2, h, 1.0, c:getRedFloat(), c:getGreenFloat(), c:getBlueFloat());
        c = fcol:getInterior();
        self:drawRect( x+(w/2)+4, y, (w/2)-2, h, 1.0, c:getRedFloat(), c:getGreenFloat(), c:getBlueFloat());
        y = y+h+5;
        self:drawText( "Final color:", x, y, 1, 1, 1, a, UIFont.Small);
        y = y+yOffset;
        fcol = flare:getOutColorPlayer(0);
        c = fcol:getExterior();
        self:drawRect( x, y, (w/2)-2, h, 1.0, c:getRedFloat(), c:getGreenFloat(), c:getBlueFloat());
        c = fcol:getInterior();
        self:drawRect( x+(w/2)+4, y, (w/2)-2, h, 1.0, c:getRedFloat(), c:getGreenFloat(), c:getBlueFloat());
    end
end

function WorldFlaresDebug:update()
    ISPanel.update(self);
end

function WorldFlaresDebug:close()
    self:setVisible(false);
    self:removeFromUIManager();
    WorldFlaresDebug.instance = nil
end

function WorldFlaresDebug:new(x, y, width, height, title)
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


