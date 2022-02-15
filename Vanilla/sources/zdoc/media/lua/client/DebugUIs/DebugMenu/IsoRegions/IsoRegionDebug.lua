--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************


require "ISUI/ISCollapsableWindow"

---@class IsoRegionDebug : ISCollapsableWindow
IsoRegionDebug = ISCollapsableWindow:derive("IsoRegionDebug");
IsoRegionDebug.instance = nil;
IsoRegionDebug.shiftDown = 0;

--[[
local enabled = true; --getDebug();

function IsoRegionDebug.OnKeepKeyDown(key)
    --backspace 13, shift 42, 54
    --print("KeyKeepDown = "..tostring(key));
    if key==42 or key==54 then
        IsoRegionDebug.shiftDown = 4;
    end
end

function IsoRegionDebug.OnKeyDown(key)
    --backspace 14, shift 42, 54
    --print("KeyDown = "..tostring(key));
    if IsoRegionDebug.shiftDown>0 and key ==11 then
        IsoRegionDebug.OnOpenPanel();
    end
end
--]]

function IsoRegionDebug.OnOpenPanel()
    if IsoRegionDebug.instance==nil then
        IsoRegionDebug.instance = IsoRegionDebug:new (100, 100, 1000, 1000, getPlayer());
        IsoRegionDebug.instance:initialise();
        IsoRegionDebug.instance:instantiate();
    end

    IsoRegionDebug.instance:addToUIManager();
    IsoRegionDebug.instance:setVisible(true);

    return IsoRegionDebug.instance;
end

function IsoRegionDebug:initialise()
    ISCollapsableWindow.initialise(self);
end


function IsoRegionDebug:createChildren()
    ISCollapsableWindow.createChildren(self);

    local y = self:titleBarHeight();

    self.canvasY = y;
    --y = y+10;
    --self:setHeight(y);
    local camW = 60*10;
    local camH = 60*10;
    self.cam = {
        x = 0,
        y = 0,
        width = camW,
        height = camH,
        centerX = camW/2,
        centerY = camH/2,
        chunkWidth = 10,
        chunkHeight = 10,
        chunkPixDim = 60,
    };
    y = y + self.cam.height;
    self.palpha = 1.0;
    self.palphaUp = false;
    self.regionMode = 0;
    self.showGrid = true;
    self.showInvalidRegions = true;

    local x = 0;

    self.buttonToggleRegions = ISButton:new(x+5, y+2, self.cam.width-10,18,"showing: Master regions",self, IsoRegionDebug.onButton);
    self.buttonToggleRegions:initialise();
    self.buttonToggleRegions.backgroundColor = {r=0, g=0.0, b=0, a=1.0};
    self.buttonToggleRegions.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.buttonToggleRegions.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self.buttonToggleRegions.btnStringID = "ToggleRegions";
    self.buttonToggleRegions.btnCode = self.regionMode;
    self:addChild(self.buttonToggleRegions);
    --self:onButton(self.buttonToggleRegions);

    y = self.buttonToggleRegions:getY() + self.buttonToggleRegions:getHeight();


    self.buttonToggleGrid = ISButton:new(x+5, y+2, self.cam.width-10,18,"showing grid = TRUE",self, IsoRegionDebug.onButton);
    self.buttonToggleGrid:initialise();
    self.buttonToggleGrid.backgroundColor = {r=0, g=0.8, b=0, a=1.0};
    self.buttonToggleGrid.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.buttonToggleGrid.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self.buttonToggleGrid.btnStringID = "ToggleGrid";
    self.buttonToggleGrid.btnCode = self.showGrid;
    self:addChild(self.buttonToggleGrid);

    y = self.buttonToggleGrid:getY() + self.buttonToggleGrid:getHeight();

    self.buttonToggleInvalidRegions = ISButton:new(x+5, y+2, self.cam.width-10,18,"showing invalid regions = TRUE",self, IsoRegionDebug.onButton);
    self.buttonToggleInvalidRegions:initialise();
    self.buttonToggleInvalidRegions.backgroundColor = {r=0, g=0.8, b=0, a=1.0};
    self.buttonToggleInvalidRegions.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.buttonToggleInvalidRegions.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self.buttonToggleInvalidRegions.btnStringID = "ToggleInvalidRegions";
    self.buttonToggleInvalidRegions.btnCode = self.showInvalidRegions;
    self:addChild(self.buttonToggleInvalidRegions);

    y = self.buttonToggleInvalidRegions:getY() + self.buttonToggleInvalidRegions:getHeight();

    local booltxt = IsoRegions.isDebugLoadAllChunks() and "TRUE" or "FALSE";
    self.buttonToggleLoadAllChunks = ISButton:new(x+5, y+2, self.cam.width-10,18,"load all chunks around plr = "..booltxt,self, IsoRegionDebug.onButton);
    self.buttonToggleLoadAllChunks:initialise();
    if IsoRegions.isDebugLoadAllChunks() then
        self.buttonToggleLoadAllChunks.backgroundColor = {r=0.0, g=0.8, b=0, a=1.0};
    else
        self.buttonToggleLoadAllChunks.backgroundColor = {r=0.8, g=0.0, b=0, a=1.0};
    end
    self.buttonToggleLoadAllChunks.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.buttonToggleLoadAllChunks.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self.buttonToggleLoadAllChunks.btnStringID = "ToggleLoadAllChunks";
    self.buttonToggleLoadAllChunks.btnCode = self.showGrid;
    self:addChild(self.buttonToggleLoadAllChunks);

    y = self.buttonToggleLoadAllChunks:getY() + self.buttonToggleLoadAllChunks:getHeight();

    self.buttonReset = ISButton:new(x+5, y+2, self.cam.width-10,18,"DESTROY CURRENT DATASET",self, IsoRegionDebug.onButton);
    self.buttonReset:initialise();
    self.buttonReset.backgroundColor = {r=0, g=0.0, b=0, a=1.0};
    self.buttonReset.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1};
    self.buttonReset.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3};
    self.buttonReset.btnStringID = "ResetData";
    self.buttonReset.btnCode = self.showGrid;
    self:addChild(self.buttonReset);

    y = self.buttonReset:getY() + self.buttonReset:getHeight();

    self:setWidth(self.cam.width);
    self:setHeight(y+self:resizeWidgetHeight());
end

function IsoRegionDebug:onButton(_btn)
    if _btn.btnStringID=="ToggleRegions" then
        self.buttonToggleRegions.btnCode = self.buttonToggleRegions.btnCode + 1;
        if self.buttonToggleRegions.btnCode >= 3 then
            self.buttonToggleRegions.btnCode = 0;
        end
        if self.buttonToggleRegions.btnCode == 0 then
            self.buttonToggleRegions.title = "showing: Master regions";
            self.regionMode = 0;
        elseif self.buttonToggleRegions.btnCode == 1 then
            self.buttonToggleRegions.title = "showing: Chunk regions";
            self.regionMode = 1;
        elseif self.buttonToggleRegions.btnCode == 2 then
            self.buttonToggleRegions.title = "showing: Blueprint";
            self.regionMode = 2;
        end
    end
    if _btn.btnStringID == "ToggleGrid" then
        self.showGrid = not self.showGrid;
        if self.showGrid then
            self.buttonToggleGrid.title = "showing grid = TRUE";
            self.buttonToggleGrid.backgroundColor = {r=0, g=0.8, b=0, a=1.0};
        else
            self.buttonToggleGrid.title = "showing grid = FALSE";
            self.buttonToggleGrid.backgroundColor = {r=0.8, g=0.0, b=0, a=1.0};
        end
    end
    if _btn.btnStringID == "ToggleInvalidRegions" then
        self.showInvalidRegions = not self.showInvalidRegions;
        if self.showInvalidRegions then
            self.buttonToggleInvalidRegions.title = "showing invalid regions = TRUE";
            self.buttonToggleInvalidRegions.backgroundColor = {r=0, g=0.8, b=0, a=1.0};
        else
            self.buttonToggleInvalidRegions.title = "showing invalid regions = FALSE";
            self.buttonToggleInvalidRegions.backgroundColor = {r=0.8, g=0.0, b=0, a=1.0};
        end
    end
    if _btn.btnStringID == "ToggleLoadAllChunks" then
        IsoRegions.setDebugLoadAllChunks(not IsoRegions.isDebugLoadAllChunks());
        if IsoRegions.isDebugLoadAllChunks() then
            self.buttonToggleLoadAllChunks.title = "load all chunks around plr = TRUE";
            self.buttonToggleLoadAllChunks.backgroundColor = {r=0, g=0.8, b=0, a=1.0};
        else
            self.buttonToggleLoadAllChunks.title = "load all chunks around plr = FALSE";
            self.buttonToggleLoadAllChunks.backgroundColor = {r=0.8, g=0.0, b=0, a=1.0};
        end
    end
    if _btn.btnStringID == "ResetData" then
        IsoRegions.ResetAllDataDebug();
    end
end


function IsoRegionDebug:convertCoords(_x, _y)
    local x,y=1000-(_y/18.7),_x/18.7;
    return x,y;
end

function IsoRegionDebug:onResize()
    ISUIElement.onResize(self);
    local th = self:titleBarHeight();
end

function IsoRegionDebug:update()
    ISCollapsableWindow.update(self);

    if IsoRegionDebug.shiftDown>0 then
        IsoRegionDebug.shiftDown = IsoRegionDebug.shiftDown-1;
    end
end

function IsoRegionDebug:prerender()
    self:stayOnSplitScreen();
    ISCollapsableWindow.prerender(self);
end

function IsoRegionDebug:stayOnSplitScreen()
    ISUIElement.stayOnSplitScreen(self, self.playerNum)
end

function IsoRegionDebug:onMouseDown(x, y)
    --print("Mouse over x="..tostring(x)..", y="..tostring(y));
    local sx,sy = 60, self.canvasY+60;
    local ex,ey = sx+((self.cam.chunkWidth-2)*60),sy+((self.cam.chunkHeight-2)*60);
    if x>=sx and x<ex and y>=sy and y<ey then
        local plr = getPlayer(self.playerNum);
        local plrX, plrY,plrZ = plr:getX(),plr:getY(),math.floor(plr:getZ());

        x=x-sx;
        y=y-sy;

        x=x-self.cam.centerX; --note: is bigger then current rectangle, substracts 1 chunk too many
        y=y-self.cam.centerY;

        x=x/6;
        y=y/6;

        self.mouseInfo.x = math.floor(plrX+x)+self.cam.chunkWidth; -- +10, fix cam.centerX/Y offset
        self.mouseInfo.y = math.floor(plrY+y)+self.cam.chunkHeight;
        self.mouseInfo.z = plrZ;

        self.mouseInfo.lastClick = self.mouseInfo.lastClickTxt.." x="..tostring(self.mouseInfo.x)..", y="..tostring(self.mouseInfo.y)..", z="..tostring(self.mouseInfo.z);
        --local cx,cy = (plrChunkX-chunksHalfWidth)+x, (plrChunkY-chunksHalfHeight)+y;
        --local cwx,cwy = cx*self.cam.chunkWidth,cy*self.cam.chunkHeight;
        --local sx,sy = self.cam.centerX+((cwx-plrX)*6), self.cam.centerY+((cwy-plrY)*6);

        local chunkRegion = IsoRegions.getChunkRegion(self.mouseInfo.x, self.mouseInfo.y, self.mouseInfo.z);
        if chunkRegion then
            self.mouseInfo.regionID = chunkRegion:getID();
            local worldRegion = chunkRegion:getIsoWorldRegion();
            self.mouseInfo.worldRegionID = worldRegion and worldRegion:getID() or -1;
            self.mouseInfo.region = chunkRegion; --IsoRegions.getNeighborIDs(chunkRegion);

            local panel = IsoRegionDetails.OnOpenPanel();
            panel:readRegion( self.mouseInfo.x, self.mouseInfo.y, self.mouseInfo.z, chunkRegion );
        else
            self.mouseInfo.regionID = -1;
            self.mouseInfo.worldRegionID = -1;
            self.mouseInfo.region = nil;
        end
        return true;
    end
    ISCollapsableWindow.onMouseDown(self,x, y);
end


function IsoRegionDebug:render()
    ISCollapsableWindow.render(self);

    if IsoRegionDebug.shiftDown>0 then
        IsoRegionDebug.shiftDown = IsoRegionDebug.shiftDown-1;
    end

    local plr = getPlayer(self.playerNum);
    local plrX, plrY,plrZ = plr:getX(),plr:getY(),math.floor(plr:getZ());
    local plrChunkX, plrChunkY = math.floor(plrX/self.cam.chunkWidth), math.floor(plrY/self.cam.chunkHeight);

    self:drawRect(0, self.canvasY+0, self.cam.width, self.cam.height, 1, 0.0, 0.0, 0.0);
    --9x9 surrounding player
    local chunksHalfWidth, chunksHalfHeight = (self.cam.chunkWidth-2)/2, (self.cam.chunkHeight-2)/2;
    local chunk,square,chunkRegion,worldRegion,col,alpha;
    for y = 0, self.cam.chunkHeight-2 do
        for x=0, self.cam.chunkWidth-2 do
            local cx,cy = (plrChunkX-chunksHalfWidth)+x, (plrChunkY-chunksHalfHeight)+y;
            local cwx,cwy = cx*self.cam.chunkWidth,cy*self.cam.chunkHeight;
            local sx,sy = self.cam.centerX+((cwx-plrX)*6), self.cam.centerY+((cwy-plrY)*6);
            sy = sy + self.canvasY;
            --self:drawRectBorder( sx, sy, self.cam.chunkPixDim, self.cam.chunkPixDim, 1.0, 0.4, 0.4, 0.4);

            chunk = IsoRegions.getDataChunk(cx,cy);
            if chunk then
                for y = 0, self.cam.chunkHeight-1 do
                    for x=0, self.cam.chunkWidth-1 do
                        square = chunk:getSquare(x,y,plrZ,true);
                        chunk:setSelectedFlags(x,y,plrZ);
                        if square>=0 then
                            if self.regionMode==0 or self.regionMode==1 then
                                chunkRegion = chunk:getRegion(x,y,plrZ); --square:getRegion();
                                if chunkRegion then
                                    if self.regionMode==1 then
                                        alpha = 1;
                                        if self.mouseInfo.regionID>=0 then
                                            alpha = 0.1;
                                            if chunkRegion:getID()==self.mouseInfo.regionID then
                                                alpha=1;
                                            elseif self.mouseInfo.region~=nil and self.mouseInfo.region:containsConnectedNeighbor(chunkRegion) then
                                                alpha=0.4;
                                            end
                                        end
                                        if self.showInvalidRegions or chunkRegion:getIsEnclosed() then
                                            col = chunkRegion:getColor();
                                            --if col then
                                                self:drawRect(sx+(x*6), sy+(y*6), 6, 6, alpha, col:getRedFloat(), col:getGreenFloat(), col:getBlueFloat());
                                            --end
                                        end
                                    else
                                        worldRegion = chunkRegion:getIsoWorldRegion();
                                        if worldRegion then
                                            alpha = 1;
                                            if self.mouseInfo.worldRegionID>=0 then
                                                alpha = 0.1;
                                                if worldRegion:getID()==self.mouseInfo.worldRegionID then
                                                    alpha=1;
                                                end
                                            end
                                            if self.showInvalidRegions or worldRegion:isEnclosed() then
                                                col = worldRegion:getColor();
                                                self:drawRect(sx+(x*6), sy+(y*6), 6, 6, alpha, col:getRedFloat(), col:getGreenFloat(), col:getBlueFloat());
                                            end
                                        end
                                    end
                                end
                            else
                                if chunk:selectedHasFlags(IsoRegions.BIT_HAS_FLOOR) then
                                    self:drawRect(sx+(x*6), sy+(y*6), 6, 6, 1, 0.392, 0.584, 0.929);
                                end
                            end
                            if chunk:selectedHasFlags(IsoRegions.BIT_WALL_N) or chunk:selectedHasFlags(IsoRegions.BIT_PATH_WALL_N) then
                                self:drawRect(sx+(x*6), sy+(y*6), 6, 2, 1, 1.0, 1.0, 1.0);
                            end
                            if chunk:selectedHasFlags(IsoRegions.BIT_WALL_W) or chunk:selectedHasFlags(IsoRegions.BIT_PATH_WALL_W) then
                                self:drawRect(sx+(x*6), sy+(y*6), 2, 6, 1, 1.0, 1.0, 1.0);
                            end
                        end
                    end
                end
                if self.showGrid then self:drawRectBorder( sx, sy, self.cam.chunkPixDim, self.cam.chunkPixDim, 1.0, 0.1, 0.9, 0.1); end
            else
                if self.showGrid then self:drawRectBorder( sx, sy, self.cam.chunkPixDim, self.cam.chunkPixDim, 1.0, 0.4, 0.4, 0.4); end
            end
        end
    end


    if self.palphaUp then
        self.palpha = self.palpha + 0.05;
        if self.palpha>1.0 then
            self.palpha = 1.0;
            self.palphaUp = false;
        end
    else
        self.palpha = self.palpha - 0.05;
        if self.palpha<0.0 then
            self.palpha = 0.0;
            self.palphaUp = true;
        end
    end

    --borders
    self:drawRect(0, self.canvasY+0, self.cam.width, self.cam.chunkPixDim, 1, 0.2, 0.2, 0.2);
    self:drawRect(0, self.canvasY+(self.cam.height-self.cam.chunkPixDim), self.cam.width, self.cam.chunkPixDim, 1, 0.2, 0.2, 0.2);
    self:drawRect(0, self.canvasY+self.cam.chunkPixDim, self.cam.chunkPixDim, self.cam.height-(self.cam.chunkPixDim*2), 1, 0.2, 0.2, 0.2);
    self:drawRect(self.cam.width-self.cam.chunkPixDim, self.canvasY+self.cam.chunkPixDim, self.cam.chunkPixDim, self.cam.height-(self.cam.chunkPixDim*2), 1, 0.2, 0.2, 0.2);

    --player
    self:drawRect(self.cam.centerX-2, self.canvasY+(self.cam.centerY-2), 5, 5, self.palpha, 0.8, 0.0, 0.0);

    --info
    self:drawTextCentre(self.mouseInfo.lastClick, self.cam.width/2, self.canvasY+(self.cam.height-self.cam.chunkPixDim)+5, 1.0, 1.0, 1.0, 1.0, UIFont.Small);
    local curTxt = "Player: x="..tostring(math.floor(plrX))..", y="..tostring(math.floor(plrY))..", z="..tostring(plrZ);
    self:drawTextCentre(curTxt, self.cam.width/2, self.canvasY+(self.cam.height-self.cam.chunkPixDim)+25, 1.0, 1.0, 1.0, 1.0, UIFont.Small);
end




function IsoRegionDebug:close()
    ISCollapsableWindow.close(self)
    if JoypadState.players[self.playerNum+1] then
        setJoypadFocus(self.playerNum, nil)
    end
    IsoRegionDebug.instance = nil;
    self:removeFromUIManager();
    self:clear();
end

function IsoRegionDebug:clear()
    self.currentTile = nil;
end



function IsoRegionDebug:new (x, y, width, height, player)
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
    o.backgroundColor = {r=0, g=0, b=0, a=0.0};
    o.greyCol = { r=0.4,g=0.4,b=0.4,a=1};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.pin = true;
    o.isCollapsed = false;
    o.collapseCounter = 0;
    o.title = "IsoRegionDebug";
    --o.viewList = {}
    o.resizable = true;
    o.drawFrame = true;

    o.currentTile = nil;
    o.richtext = nil;
    o.overrideBPrompt = true;
    o.subFocus = nil;
    o.hotKeyPanels = {};
    o.isJoypadWindow = false;

    o.hourStamp = -1;
    o.dayStamp = -1;
    o.monthStamp = -1;
    o.year = -1;
    o.poi = {};
    o.strikes = {};
    o.mouseInfo = {
        ui_x = 0,
        ui_y = 0,
        x = 0,
        y = 0,
        z = 0,
        inRectangle = false,
        clicked = false,
        lastClickTxt = "LastMouseClick: ";
        lastClick = "LastMouseClick: ";
        regionID = -1;
        worldRegionID = -1;
        neighbors = nil;
    };
    ISDebugMenu.RegisterClass(self);
    return o
end

--[[
if enabled then
    Events.OnCustomUIKey.Add(IsoRegionDebug.OnKeyDown);
    Events.OnKeyKeepPressed.Add(IsoRegionDebug.OnKeepKeyDown);
    --Events.OnClimateTick.Add(IsoRegionDebug.OnClimateTick);
    --Events.OnThunderEvent.Add(IsoRegionDebug.OnThunderEvent);
    --Events.OnObjectLeftMouseButtonUp.Add(IsoRegionDebug.onMouseButtonUp);
end--]]
