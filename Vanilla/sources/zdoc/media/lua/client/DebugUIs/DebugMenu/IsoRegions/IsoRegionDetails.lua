--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************


require "ISUI/ISCollapsableWindow"

---@class IsoRegionDetails : ISCollapsableWindow
IsoRegionDetails = ISCollapsableWindow:derive("IsoRegionDetails");
IsoRegionDetails.instance = nil;
IsoRegionDetails.shiftDown = 0;
IsoRegionDetails.lastX = false;
IsoRegionDetails.lastY = false;

function IsoRegionDetails.OnOpenPanel()
    if IsoRegionDetails.instance==nil then
        local x = IsoRegionDetails.lastX or 0;
        local y = IsoRegionDetails.lastY or 100;
        IsoRegionDetails.instance = IsoRegionDetails:new (x, y, 200, 500, getPlayer());
        IsoRegionDetails.instance:initialise();
        IsoRegionDetails.instance:instantiate();
    end

    IsoRegionDetails.instance:addToUIManager();
    IsoRegionDetails.instance:setVisible(true);

    return IsoRegionDetails.instance;
end

function IsoRegionDetails:initialise()
    ISCollapsableWindow.initialise(self);
end


function IsoRegionDetails:createChildren()
    ISCollapsableWindow.createChildren(self);

    local y = self:titleBarHeight();

    self.canvasY = y;

    self.richtext = ISRichTextPanel:new(0, y, self.width, self.height);
    self.richtext:initialise();

    self:addChild(self.richtext);

    self.richtext.background = false;
    self.richtext.autosetheight = false;
    self.richtext.clip = true
    self.richtext:addScrollBars();

    self.richtext.text = "";
    self.richtext:paginate();

    y = self.richtext:getY() + self.richtext:getHeight();

    self:setHeight(y+self:resizeWidgetHeight());
end

function IsoRegionDetails:readRegion( _x, _y, _z, _o )
    self:clear();

    if _o~=nil and self.richtext~=nil then
        self.tmpTxt = "";

        self:addLine("X",_x);
        self:addLine("Y",_y);
        self:addLine("Z",_z);
        local gs = getCell():getGridSquare(_x,_y,_z);
        --local ds = IsoRegions.getDataSquare(_x,_y,_z);
        self:addLine("hasGridsquare",gs and "true" or "false");
        if gs then
            self:addLine("isSolidFloor",gs:Is(IsoFlagType.solidfloor) and "true" or "false");
            --self:addLine("hasDataSquare",gs:getDataSquare() and "true" or "false");
            self:addLine("hasWorldRegion",gs:getIsoWorldRegion() and tostring(gs:getIsoWorldRegion():getID()) or "false");
        else
            self:addLine("isSolidFloor","false");
            --[[self:addLine("found DataSquare",ds and "true" or "false");
            if ds then
                self:addLine("found Region",ds:getRegion() and tostring(ds:getRegion():getID()) or "false");
                local mr = ds:getRegion() and ds:getRegion():getIsoWorldRegion();
                if mr then
                    self:addLine("found WorldRegion",mr and tostring(mr:getID()) or "false");
                end
            end--]]
        end

        local ds = IsoRegions.getSquareFlags(_x,_y,_z);
        local chunk = IsoRegions.getDataChunk(_x/10,_y/10);
        print("ds = "..tostring(ds)..", chunk = "..tostring(chunk))
        if ds>=0 and chunk then
            chunk:setSelectedFlags(_x% 10,_y% 10,_z);
            self:addTitle("BitFlags");
            if chunk:selectedHasFlags(IsoRegions.BIT_WALL_N) then self:addLine("BIT_WALL_N",""); end
            if chunk:selectedHasFlags(IsoRegions.BIT_WALL_W) then self:addLine("BIT_WALL_W",""); end
            if chunk:selectedHasFlags(IsoRegions.BIT_PATH_WALL_N) then self:addLine("BIT_PATH_WALL_N",""); end
            if chunk:selectedHasFlags(IsoRegions.BIT_PATH_WALL_W) then self:addLine("BIT_PATH_WALL_W",""); end
            if chunk:selectedHasFlags(IsoRegions.BIT_HAS_FLOOR) then self:addLine("BIT_HAS_FLOOR",""); end
            if chunk:selectedHasFlags(IsoRegions.BIT_STAIRCASE) then self:addLine("BIT_STAIRCASE",""); end
            if chunk:selectedHasFlags(IsoRegions.BIT_HAS_ROOF) then self:addLine("BIT_HAS_ROOF",""); end
        end

        if instanceof(_o, "IsoChunkRegion") then
            self:addTitle("ChunkRegion");
            self:addLine("ID",_o:getID());
            self:addLine("SquareSize",_o:getSquareSize());
            self:addLine("RoofCnt",_o:getRoofCnt());
            self:addLine("Z-layer",_o:getzLayer());
            self:addLine("Enclosed",_o:getIsEnclosed() and "true" or "false");
            self:addLine("ChunkBorderConnCnt",_o:getChunkBorderSquaresCnt());

            local neighbors = _o:getDebugConnectedNeighborCopy();
            self:addTitle("ChunkRegion - Neighbors");
            self:addLine("Count",neighbors and neighbors:size() or 0);
            if neighbors and neighbors:size()>0 then
                for i=0,neighbors:size()-1 do
                    self:addLine("neighbor-id",neighbors:get(i):getID());
                end
            end

            local mr = _o:getIsoWorldRegion();
            self:addTitle("WorldRegion");
            self:addLine("Found",mr and "yes" or "no");
            if mr then
                self:addLine("ID",mr:getID());
                self:addLine("ChunkRegions",mr:size());
                self:addLine("SquareSize",mr:getSquareSize());
                self:addLine("RoofCnt",mr:getRoofCnt());
                self:addLine("Enclosed",mr:isEnclosed());
            end

            local neighbors = mr:getDebugConnectedNeighborCopy();
            self:addTitle("WorldRegion - Neighbors");
            self:addLine("Count",neighbors and neighbors:size() or 0);
            if neighbors and neighbors:size()>0 then
                for i=0,neighbors:size()-1 do
                    self:addLine("neighbor-id",neighbors:get(i):getID());
                    if i>20 then
                        self:addLine("skipping (>20) #",neighbors:size()-20);
                        break;
                    end
                end
            end

            local regions = mr:getDebugIsoChunkRegionCopy();
            self:addTitle("WorldRegion - regions");
            if regions then
                for i=0,regions:size()-1 do
                    self:addLine("region-id",regions:get(i):getID());
                    if i>20 then
                        self:addLine("skipping (>20) #",regions:size()-20);
                        break;
                    end
                end
            end
        end

        self.richtext.text = self.tmpTxt;
        self.richtext:paginate();
    end
end


function IsoRegionDetails:addTitle(_title)
    self.tmpTxt = self.tmpTxt .. " <H2> <ORANGE> "..tostring(_title).." <LINE> ";
end

function IsoRegionDetails:addLine(_prefix, _line)
    --if _prefix:len()<40 then
    --_prefix = _prefix .. string.rep(" ",40-_prefix:len());
    --end
    self.tmpTxt = self.tmpTxt .. " <TEXT> "..tostring(_prefix)..": "..tostring(_line).." <LINE> ";
end

function IsoRegionDetails:addLineEnd()
    --if _prefix:len()<40 then
    --_prefix = _prefix .. string.rep(" ",40-_prefix:len());
    --end
    self.tmpTxt = self.tmpTxt .." <LINE> ";
end

function IsoRegionDetails:onButton(_btn)
end


function IsoRegionDetails:onResize()
    ISUIElement.onResize(self);
    local th = self:titleBarHeight();
    self.richtext:setWidth(self.width);
    self.richtext:setHeight(self.height-(th+self:resizeWidgetHeight()));
end

function IsoRegionDetails:update()
    ISCollapsableWindow.update(self);
    IsoRegionDetails.lastX = self:getAbsoluteX();
    IsoRegionDetails.lastY = self:getAbsoluteY();
end

function IsoRegionDetails:prerender()
    self:stayOnSplitScreen();
    ISCollapsableWindow.prerender(self);
end

function IsoRegionDetails:stayOnSplitScreen()
    ISUIElement.stayOnSplitScreen(self, self.playerNum)
end

function IsoRegionDetails:render()
    ISCollapsableWindow.render(self);

end


function IsoRegionDetails:close()
    ISCollapsableWindow.close(self)
    if JoypadState.players[self.playerNum+1] then
        setJoypadFocus(self.playerNum, nil)
    end
    IsoRegionDetails.instance = nil;
    self:removeFromUIManager();
    self:clear();
end

function IsoRegionDetails:clear()
end



function IsoRegionDetails:new (x, y, width, height, player)
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
    o.title = "IsoRegionDetails";
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
