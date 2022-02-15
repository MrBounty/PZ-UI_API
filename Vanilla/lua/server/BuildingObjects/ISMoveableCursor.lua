--
-- turb
--

require "BuildingObjects/ISBuildingObject"
local moveableModes = {"pickup","place","rotate","scrap"};
local function addMode(_tagTable, _tag, _titleTable, _title)
    table.insert(_tagTable, _tag);
    table.insert(_titleTable, _title);
end

ISMoveableCursor = ISBuildingObject:derive("ISMoveableCursor");
ISMoveableCursor.modes = {};
ISMoveableCursor.modes.tags = {};
ISMoveableCursor.modes.titles = {}
addMode(ISMoveableCursor.modes.tags,"pickup", ISMoveableCursor.modes.titles, getText("IGUI_Pickup"));
addMode(ISMoveableCursor.modes.tags,"place", ISMoveableCursor.modes.titles, getText("IGUI_Place"));
addMode(ISMoveableCursor.modes.tags,"rotate", ISMoveableCursor.modes.titles, getText("IGUI_Rotate"));
addMode(ISMoveableCursor.modes.tags,"scrap", ISMoveableCursor.modes.titles, getText("IGUI_Scrap"));
ISMoveableCursor.cursors = {};
ISMoveableCursor.mode = {}; --nil; --"pickup";
ISMoveableCursor.cacheMode = {}; --nil;
--ISMoveableCursor.cacheInvObject = nil;
ISMoveableCursor.normalColor = { r=0.5, g=0.5, b=0.5 };
ISMoveableCursor.validColor = { r=0.5, g=1, b=0.5 };
ISMoveableCursor.invalidColor = { r=1, g=0, b=0 };

--[[
function ISMoveableCursor.exitCursorKey( _key )
    if _key and type(_key)=="number" then
        ISMoveableCursor.exitCursor( _key );
    end
end
--]]

function ISMoveableCursor.exitCursorKey( _key )
    if getCell() and getCell():getDrag(0) and getCell():getDrag(0).Type and getCell():getDrag(0).Type == "ISMoveableCursor" then
        local disable = false;
        if not _key then
            disable = true;
        else
            if getCore():getKey("Run") == _key then
                disable = true;
            elseif getCore():getKey("Interact") == _key then
                disable = true;
            end
        end

        if disable then
            getCell():setDrag(nil, 0);
        end
    end
end

function ISMoveableCursor:exitCursor()
    getCell():setDrag(nil, self.player);
    if ISMoveableInfoWindow.infoPanels[self.player] then
        ISMoveableInfoWindow.infoPanels[self.player]:removeFromUIManager();
    end
end

Events.OnKeyPressed.Add(ISMoveableCursor.exitCursorKey);
Events.OnKeyKeepPressed.Add(ISMoveableCursor.exitCursorKey);
Events.OnRightMouseDown.Add(ISMoveableCursor.exitCursorKey);

function ISMoveableCursor.changeModeKey( _key, _playerNum, _joyPadTriggered )
    if _joyPadTriggered then
        local playerID = _playerNum or 0;
        if getCell() and getCell():getDrag(playerID) and getCell():getDrag(playerID).Type and
                getCell():getDrag(playerID).Type == "ISMoveableCursor" then
            local playerObj = getSpecificPlayer(playerID)
            local menu = getPlayerRadialMenu(playerID)
            menu:clear()
            menu:addSlice(getText("IGUI_Pickup"), getTexture("media/ui/Furniture_Pickup.png"), ISMoveableCursor.setMoveableMode, getCell():getDrag(playerID), "pickup")
            menu:addSlice(getText("IGUI_Place"), getTexture("media/ui/Furniture_Place.png"), ISMoveableCursor.setMoveableMode, getCell():getDrag(playerID), "place")
            menu:addSlice(getText("IGUI_Rotate"), getTexture("media/ui/Furniture_Rotate.png"), ISMoveableCursor.setMoveableMode, getCell():getDrag(playerID), "rotate")
            menu:addSlice(getText("IGUI_Scrap"), getTexture("media/ui/Furniture_Disassemble.png"), ISMoveableCursor.setMoveableMode, getCell():getDrag(playerID), "scrap")
            menu:setX(getPlayerScreenLeft(playerID) + getPlayerScreenWidth(playerID) / 2 - menu:getWidth() / 2)
            menu:setY(getPlayerScreenTop(playerID) + getPlayerScreenHeight(playerID) / 2 - menu:getHeight() / 2)
            menu:addToUIManager()
            menu:setHideWhenButtonReleased(Joypad.LBumper)
            setJoypadFocus(playerID, menu)
            playerObj:setJoypadIgnoreAimUntilCentered(true)
        end
        return
    end
    if _key == getCore():getKey("Toggle mode") or _joyPadTriggered then
        local playerID = _playerNum or 0;
        for k,v in ipairs(ISMoveableCursor.modes.tags) do
            if v == ISMoveableCursor.mode[playerID] then
                local next = k+1;
                if next > #ISMoveableCursor.modes.tags then
                    next = 1;
                end
                if getCell() and getCell():getDrag(playerID) and getCell():getDrag(playerID).Type and getCell():getDrag(playerID).Type == "ISMoveableCursor" then
                    getCell():getDrag(playerID):setMoveableMode( ISMoveableCursor.modes.tags[next] );
                end
                return;
            end
        end
    end
end

Events.OnKeyPressed.Add(ISMoveableCursor.changeModeKey);

function ISMoveableCursor.clearCacheForAllPlayers()
    for i = 0,3 do
        --print("Attempt to clear for player: "..tostring(i));
        if getCell() and getCell():getDrag(i) and getCell():getDrag(i).Type and getCell():getDrag(i).Type == "ISMoveableCursor" then
            getCell():getDrag(i):clearCache();
        end
    end
end

function ISMoveableCursor:clearCache()
    --if getCell() and getCell():getDrag(0) and getCell():getDrag(0).Type and getCell():getDrag(0).Type == "ISMoveableCursor" then
        --local drag = getCell():getDrag(0);
        if ISMoveableCursor.mode[self.player] == "rotate" then
            self.objectIndex = -1;
        end
        self.objectListCache = nil;
        self.cacheObject = nil;
    --end
end

function ISMoveableCursor:getMoveableMode()
    return ISMoveableCursor.mode[self.player];
end

function ISMoveableCursor:tryInitialItem( _item )
    self.tryInitialInvItem = _item;
end

function ISMoveableCursor:setMoveableMode( _mode )
    if _mode then
        for k,v in ipairs(ISMoveableCursor.modes.tags) do
            if v==_mode then
                ISMoveableCursor.mode[self.player] = _mode;
                --self.moveableMode = _mode;

                if not ISMoveableCursor.cacheMode[self.player] or ISMoveableCursor.cacheMode[self.player] ~= ISMoveableCursor.mode[self.player] then
                    local infoPanel = self:getInfoPanel();
                    local lastbit = ""; --""[br/]'"..Keyboard.getKeyName(getCore():getKey("Toggle Moveable Panel Mode")).."' = change panel display mode."; -- "[br/]'"..Keyboard.getKeyName(getCore():getKey("Toggle mode")).."' = toggle moveable modes.[br/]'"..Keyboard.getKeyName(getCore():getKey("Toggle Moveable Panel Mode")).."' = change panel display mode.";
                    if ISMoveableCursor.mode[self.player] == "pickup" then
                        infoPanel:setHeaderText( getText("IGUI_Pickup"), UIFont.Medium );
                        infoPanel:setFooterText( "'"..Keyboard.getKeyName(getCore():getKey("Rotate building")).."' = "..getText("IGUI_Cycle1").."."..lastbit, UIFont.Small );
                    elseif ISMoveableCursor.mode[self.player] == "place" then
                        infoPanel:setHeaderText( getText("IGUI_Place"), UIFont.Medium );
                        infoPanel:setFooterText( getText("IGUI_HoldButton")..".[br/]'"..Keyboard.getKeyName(getCore():getKey("Rotate building")).."' = "..getText("IGUI_Cycle2").."."..lastbit, UIFont.Small );
                    elseif ISMoveableCursor.mode[self.player] == "rotate" then
                        infoPanel:setHeaderText( getText("IGUI_Rotate"), UIFont.Medium );
                        infoPanel:setFooterText( getText("IGUI_HoldButton")..".[br/]'"..Keyboard.getKeyName(getCore():getKey("Rotate building")).."' = "..getText("IGUI_Cycle3").."."..lastbit, UIFont.Small );
                    elseif ISMoveableCursor.mode[self.player] == "scrap" then
                        infoPanel:setHeaderText( getText("IGUI_Scrap"), UIFont.Medium );
                        infoPanel:setFooterText( "'"..Keyboard.getKeyName(getCore():getKey("Rotate building")).."' = "..getText("IGUI_Cycle1").."."..lastbit, UIFont.Small );
                    end

                    ISMoveableCursor.cacheMode[self.player] = ISMoveableCursor.mode[self.player];
                    self.objectIndex = -1;
                    self.objectListCache = nil;
                    self.cacheObject = nil;
                end
            end
        end
    end
end

function ISMoveableCursor:create(_x, _y, _z, _north, _sprite)
    if self.canCreate and self.currentMoveProps and self.origSpriteName then
        if self.currentMoveProps then
            if ISMoveableDefinitions.cheat or self.currentMoveProps:walkToAndEquip( self.character, self.currentSquare, ISMoveableCursor.mode[self.player] ) then
                ISTimedActionQueue.add(ISMoveablesAction:new(self.character, self.currentSquare, self.currentMoveProps, ISMoveableCursor.mode[self.player], self.origSpriteName, self ));
            end
        end
    end
    self.cursorFacing = nil;
    self.joypadFacing = nil;
    self.objectListCache = nil;
end

function ISMoveableCursor:getInfoPanel()
    if not ISMoveableInfoWindow.infoPanels[self.player] then
        ISMoveableInfoWindow.infoPanels[self.player] = ISMoveableInfoWindow:new(0, 0, self.character );
    end

    return ISMoveableInfoWindow.infoPanels[self.player];
end

local int = 0;
function ISMoveableCursor:setInfoPanel( _square, _object, _moveProps, _customTexture)
    local infoPanel = self:getInfoPanel();
    if self.cursorFacing then -- when holding leftclick and dragging mouse to select a facing disable panel, otherwise panel will glitch visually on mouse over.
        infoPanel:setVisible(false);
        infoPanel:removeFromUIManager();
        self:setJoypadFocus( nil );
        return;
    end
    if _square and _moveProps then
        if not self.cacheObject or self.cacheObject ~= _object or not self.cacheSquare or self.cacheSquare ~= _square then
            infoPanel:setBodyText( _moveProps:getInfoPanelDescription( _square, _object, self.character, ISMoveableCursor.mode[self.player] ), UIFont.Small );
        end
        local mode = ISMoveableCursor.mode[self.player]
        local square = (mode == "pickup" or mode == "scrap") and _square
        infoPanel:setTexture( _customTexture or _moveProps.spriteName, self.canCreate, square, self.yOffset );
        infoPanel:setSquare(_square);
        infoPanel:setDrag(getCell():getDrag(self.player));
        infoPanel:setVisible(true);
        infoPanel:addToUIManager();
        self.cacheSquare = _square;
        self.cacheObject = _object;

    else --currently just set to invisible
        --if self.cacheObject then
            --if _square and not _square:getCanSee() then
                --infoPanel:setBodyText( ISMoveableSpriteProps.addLineToInfoTable( {}, "Cannot see target location.", 255, 0, 0 ), UIFont.Medium, "center" );
            --else
                --infoPanel:setBodyText( ISMoveableSpriteProps.addLineToInfoTable( {}, "No valid options.", 255, 0, 0 ), UIFont.Medium, "center" );
            --end
        --end
        self.cacheObject = nil;
        infoPanel:setTexture( nil, false, nil );
        infoPanel:setSquare(_square);
        infoPanel:setDrag(getCell():getDrag(self.player));
        infoPanel:setVisible(false);
        infoPanel:addToUIManager();
    end
    return infoPanel;
end

function ISMoveableCursor:render( _x, _y, _z, _square )
    self.renderX, self.renderY, self.renderZ = _x, _y, _z;

    local color = self.colorMod or {r=1,g=0,b=0};

    if self.currentMoveProps and self.currentMoveProps.scrapThumpable then
        local stairObjects = buildUtil.getStairObjects(self.currentMoveProps.object)
        if #stairObjects > 0 then
            for i=1,#stairObjects do
                stairObjects[i]:getSprite():RenderGhostTileColor(stairObjects[i]:getX(), stairObjects[i]:getY(), stairObjects[i]:getZ(), color.r, color.g, color.b, 0.8)
            end
        end
    end

    if self.currentMoveProps and self.currentMoveProps.isMultiSprite and self.currentMoveProps.sprite:getSpriteGrid()~=nil then
        if self.origMoveProps then
            local origGrid = self.origMoveProps.sprite:getSpriteGrid();
            local xo = origGrid:getSpriteGridPosX(self.origMoveProps.sprite);
            local yo = origGrid:getSpriteGridPosY(self.origMoveProps.sprite);

            local spriteGrid = self.currentMoveProps.sprite:getSpriteGrid();
            local w = spriteGrid:getWidth();
            local h = spriteGrid:getHeight();
            local wx, wy = _x-xo,_y-yo;
            for x=0, w-1 do
                for y=0,h-1 do
                    local square = getCell():getGridSquare(wx+x, wy+y, _z);
                    if square and square:getFloor() and square:getFloor():getSprite() then
                        square:getFloor():getSprite():RenderGhostTileColor(wx+x, wy+y, _z, 0.75, 1, 0.75, 0.25);
                    end

                    local objSprite = spriteGrid:getSprite(x, y);
                    if objSprite then
                        local yoffset =self.yOffset;
                        if ISMoveableCursor.mode[self.player] == "place" and self.currentMoveProps.name=="Police Sign" then --dirty hack :/
                            yoffset = self.currentMoveProps.surface;
                        end
                        objSprite:RenderGhostTileColor(wx+x, wy+y, _z, 0, yoffset * Core.getTileScale(), color.r, color.g, color.b, 0.8);
                    end
                end
            end
        end
    else
        if self.floorSprite then
            --self.floorSprite:RenderGhostTileColor(_x, _y, _z, color.r, color.g, color.b, 0.5);
            self.floorSprite:RenderGhostTileColor(_x, _y, _z, 0.75, 1, 0.75, 0.25);
        end
        if self.objectSprite then
            if self.currentMoveProps and self.currentMoveProps:canRotateDirection() then
                local dir = IsoDirections.Max
                local cursorFacing = self.cursorFacing or self.joypadFacing
                if cursorFacing then
                    local facing = { "N", "W", "S", "E" }
                    dir = IsoDirections[facing[cursorFacing]]
                end
                if instanceof(self.cacheObject, "Moveable") then
                    if IsoMannequin.isMannequinSprite(self.objectSprite) then
                        IsoMannequin.renderMoveableItem(self.cacheObject, _x, _y, _z, dir)
                        return
                    end
                end
                if instanceof(self.cacheObject, "IsoMannequin") then
                    IsoMannequin.renderMoveableObject(self.cacheObject, _x, _y, _z, dir)
                    return
                end
            end
            self.objectSprite:RenderGhostTileColor(_x, _y, _z, 0, self.yOffset * Core.getTileScale(), color.r, color.g, color.b, 0.8);

            --self.objectSprite:RenderGhostTileColor(_x, _y, _z, 0, self.yOffset, 1, 1, 1, 0.8);
        end
    end
end

function ISMoveableCursor:isValid( _square )
    if not self.floorSprite then
        self.floorSprite = IsoSprite.new();
        self.floorSprite:LoadFramesNoDirPageSimple('media/ui/FloorTileCursor.png');
    end

    self.currentMoveProps   = nil;
    self.origMoveProps      = nil;
    self.canCreate          = nil;
    self.objectSprite       = nil;
    self.origSpriteName     = nil;
    self.colorMod           = {r=1,g=0,b=0};
    self.yOffset            = 0;

    if ISMoveableCursor.mode[self.player] == "pickup" or ISMoveableCursor.mode[self.player] == "rotate" then
        self.objectIndex    = self.currentSquare ~= _square and -1 or self.objectIndex;
    end
    if _square ~= self.currentSquare then
        self.objectListCache = nil;
    end
    self.currentSquare  = _square;

    if self.currentSquare == nil or not self.currentSquare:isCouldSee(self.player) then
        self:setInfoPanel( _square, nil, nil );
        self.cursorFacing = nil;
        self.joypadFacing = nil;
        return false;
    end

    if getPlayerRadialMenu(self.player) and getPlayerRadialMenu(self.player):isReallyVisible() then
        self:setInfoPanel( _square, nil, nil )
        self.cursorFacing = nil
        self.joypadFacing = nil
        return false
    end

    if self.character:getCharacterActions():isEmpty() then
        self.character:faceLocation(_square:getX(), _square:getY())
    end

    if ISMoveableCursor.mode[self.player] == "pickup" then
        local objects = self.objectListCache or self:getObjectList();
        self.objectListCache = objects;

        if #objects > 0 then
            if self.objectIndex > #objects or self.objectIndex < 1 then self.objectIndex = 1 end
            if self.objectIndex >= 1 and self.objectIndex <= #objects then
                local object = not objects[self.objectIndex].isWall and objects[self.objectIndex].object or nil;
                local moveProps = objects[self.objectIndex].moveProps;

                if moveProps and moveProps.sprite then
                    --self:setInfoPanel( _square, object, moveProps );
                    self.currentMoveProps   = moveProps;
                    self.origMoveProps      = moveProps;
                    self.canCreate          = moveProps:canPickUpMoveable( self.character, _square, object );
                    self.colorMod           = ISMoveableCursor.normalColor; --self.canCreate and ISMoveableCursor.normalColor or ISMoveableCursor.invalidColor;
                    self.objectSprite       = nil; --moveProps.sprite; disabled object sprite for pickup
                    self.origSpriteName     = moveProps.spriteName;
                    --self.cursorFacing = nil;
                    self.yOffset            = moveProps:getYOffsetCursor(); -- this is updated in moveprops in canPickUpMoveable function
                    self:setInfoPanel( _square, object, moveProps );
                    return true;
                end
            end
        end
    elseif ISMoveableCursor.mode[self.player] == "place" then
        local objects = self.objectListCache or self:getInventoryObjectList();
        self.objectListCache = objects;

        if #objects > 0 then
            if self.objectIndex > #objects or self.objectIndex < 1 then self.objectIndex = 1 end
            if self.objectIndex >= 1 and self.objectIndex <= #objects then
                local item = objects[self.objectIndex].object;
                local moveProps = objects[self.objectIndex].moveProps;
                self.origMoveProps = moveProps;
                local origName = moveProps.spriteName;

                if moveProps and moveProps:hasFaces() then
                    local faceIndex;
                    if moveProps.isTableTop and not moveProps.ignoreSurfaceSnap then    -- adjustment for tabletops, they should always try to snap first if parent table has faces.
                        faceIndex = moveProps:snapFaceToSquare( _square ) or self.cursorFacing;
                    else
                        faceIndex = self.cursorFacing or moveProps:snapFaceToSquare( _square );
                    end
                    if faceIndex and moveProps:getIndexedFaces()[faceIndex] then
                        local tryMoveProps = ISMoveableSpriteProps.new( moveProps:getIndexedFaces()[faceIndex] );
                        if tryMoveProps and tryMoveProps.isMoveable and tryMoveProps.sprite then
                            --self.faceIndex = faceIndex;
                            moveProps = tryMoveProps;
                        end
                    end
                end

                if moveProps and moveProps.sprite then
                    --self:setInfoPanel( _square, item, moveProps );
                    self.currentMoveProps       = moveProps;
                    self.canCreate              = moveProps:canPlaceMoveable( self.character, _square, item );
                    self.colorMod               = self.canCreate and ISMoveableCursor.normalColor or ISMoveableCursor.invalidColor;
                    self.cacheInvObjectSprite   = item:getWorldSprite();
                    self.objectSprite           = moveProps.sprite;
                    self.origSpriteName         = origName;
                    --self.cursorFacing = nil;
                    self.yOffset                = moveProps:getYOffsetCursor(); -- this is updated in moveprops in canPlaceMoveable function
                    self:setInfoPanel( _square, item, moveProps );
                    return true;
                end

            end
        end
    elseif ISMoveableCursor.mode[self.player] == "rotate" then
        local rotateObject = self.objectListCache or self:getRotateableObject();
        self.objectListCache = rotateObject;
        if rotateObject then
            local object = rotateObject.object;
            local moveProps = rotateObject.moveProps;
            self.origMoveProps = moveProps;
            local origProps = moveProps;
            local origName = moveProps.spriteName;
            if moveProps and moveProps:hasFaces() then
                local faces = moveProps:getIndexedFaces();

                if self.objectIndex < 1 then
                    self.objectIndex = moveProps:getFaceIndex();
                end

                if self.objectIndex > #faces or self.objectIndex < 1 then self.objectIndex = 1 end
                local faceIndex = self.cursorFacing or self.objectIndex;

                if faceIndex >= 1 and faceIndex <= #faces and faces[faceIndex] then
                    local tryMoveProps = ISMoveableSpriteProps.new( faces[faceIndex] );
                    if tryMoveProps and tryMoveProps.isMoveable and tryMoveProps.sprite then
                        --self.faceIndex = faceIndex;
                        moveProps = tryMoveProps;
                    end
                end

                if moveProps and moveProps.sprite then
                    --self:setInfoPanel( _square, object, moveProps, faces[faceIndex] );
                    self.currentMoveProps   = moveProps;
                    self.canCreate          = moveProps:canRotateMoveable( _square, object, origProps ); --FIXME
                    self.colorMod           = self.canCreate and ISMoveableCursor.normalColor or ISMoveableCursor.invalidColor; --ISMoveableCursor.normalColor;
                    self.objectSprite       = moveProps.sprite;
                    self.origSpriteName     = origName;
                    self.yOffset            = moveProps:getYOffsetCursor();
                    self:setInfoPanel( _square, object, moveProps, faces[faceIndex] );
                    --self.cursorFacing = nil;
                    return true;
                end
            end
            if moveProps and moveProps.sprite and moveProps:canRotateDirection() then
                self.currentMoveProps   = moveProps;
                self.canCreate          = moveProps:canRotateMoveable( _square, object, origProps );
                self.colorMod           = self.canCreate and ISMoveableCursor.normalColor or ISMoveableCursor.invalidColor;
                self.objectSprite       = moveProps.sprite;
                self.origSpriteName     = origName;
                self.yOffset            = moveProps:getYOffsetCursor();
                self:setInfoPanel( _square, object, moveProps );
                return true;
            end
        end
    elseif ISMoveableCursor.mode[self.player] == "scrap" then
        local objects = self.objectListCache or self:getScrapObjectList();
        self.objectListCache = objects;
        if #objects > 0 then
            if self.objectIndex > #objects or self.objectIndex < 1 then self.objectIndex = 1 end
            if self.objectIndex >= 1 and self.objectIndex <= #objects then
                local object = objects[self.objectIndex].object;
                local moveProps = objects[self.objectIndex].moveProps;
                if moveProps and moveProps.sprite then
                    self.currentMoveProps   = moveProps;
                    self.origMoveProps      = moveProps;
                    self.canCreate          = moveProps:canScrapObject( self.character ).canScrap;
                    self.colorMod           = ISMoveableCursor.normalColor;
                    self.objectSprite       = nil;
                    self.origSpriteName     = moveProps.spriteName;
                    self.yOffset            = moveProps:getYOffsetCursor();
                    self:setInfoPanel( _square, object, moveProps );
                    return true;
                end
            end
        end
    end

    self:setInfoPanel( _square, nil, nil );
    self.cursorFacing = nil;
    self.joypadFacing = nil;
    return false;
end

function ISMoveableCursor:rotateKey(key, _joypadTriggered)
    if _joypadTriggered and (ISMoveableCursor.mode[self.player] == "place") then
        self.cursorFacing = nil
        self.joypadFacing = nil
    end
    if (key == getCore():getKey("Rotate building") or _joypadTriggered) and not self.cursorFacing then --disable key when rotating with mouse
        if ISMoveableCursor.mode[self.player] == "rotate" and not self.canCreate then
            --return;
        end
        self.cacheInvObjectSprite = nil;
        self.objectIndex = self.objectIndex + 1;

        if ISMoveableCursor.mode[self.player] == "rotate" and _joypadTriggered then
            local rotateObject = self.objectListCache or self:getRotateableObject()
            if rotateObject and rotateObject.moveProps and rotateObject.moveProps:canRotateDirection() then
                if not self.joypadFacing then
                    local dir = rotateObject.object:getDir()
                    local facing = { [IsoDirections.N]=1, [IsoDirections.W]=2, [IsoDirections.S]=3, [IsoDirections.E]=4 }
                    self.joypadFacing = facing[dir]
                end
                self.joypadFacing = self.joypadFacing + 1
                if self.joypadFacing > 4 then
                    self.joypadFacing = 1
                end
            end
        end
    end
end

function ISMoveableCursor:rotateWhilePlacing()
    local objects = self.objectListCache or self:getInventoryObjectList()
    local rotateObject = objects[self.objectIndex]
    if not rotateObject then return end
    if rotateObject.moveProps and rotateObject.moveProps:canRotateDirection() then
        if not self.joypadFacing then
            local dir = instanceof(rotateObject.object, "Moveable") and
                self:getDirectionFromItem(rotateObject.object) or rotateObject.object:getDir()
            local facing = { [IsoDirections.N]=1, [IsoDirections.W]=2, [IsoDirections.S]=3, [IsoDirections.E]=4 }
            self.joypadFacing = facing[dir]
        end
        self.joypadFacing = self.joypadFacing + 1
        if self.joypadFacing > 4 then
            self.joypadFacing = 1
        end
    elseif rotateObject.moveProps and rotateObject.moveProps:hasFaces() then
        if not self.cursorFacing then
            self.cursorFacing = rotateObject.moveProps:getFaceIndex()
        end
        self.cursorFacing = (self.cursorFacing or 0) + 1
        if self.cursorFacing > 4 then
            self.cursorFacing = 1
        end
    end
end

function ISMoveableCursor:getDirectionFromItem(item)
    if instanceof(item, "Moveable") then
        if IsoMannequin.isMannequinSprite(self.objectSprite) then
            return IsoMannequin.getDirectionFromItem(item, self.player)
        end
    end
    return IsoDirections.S
end

function ISMoveableCursor:rotateMouse(x, y)
    if self.currentSquare then
        if ISMoveableCursor.mode[self.player] == "rotate" and not self.canCreate then
            --return;
        end
        -- we start to get the direction the mouse is compared to the selected square for the item
        local difx = x - self.currentSquare:getX();
        local dify = y - self.currentSquare:getY();
        -- west
        if difx < 0 and math.abs(difx) > math.abs(dify) then
            self.cursorFacing = 2;-- "W";
        end
        -- east
        if difx > 0 and math.abs(difx) > math.abs(dify) then
            self.cursorFacing = 4; --"E";
        end
        -- north
        if dify < 0 and math.abs(difx) < math.abs(dify) then
            self.cursorFacing = 1; --"N";
        end
        -- south
        if dify > 0 and math.abs(difx) < math.abs(dify) then
            self.cursorFacing = 3; --"S";
        end
    end
end

function ISMoveableCursor:setJoypadFocus( _window )
    local joypadData = JoypadState.players[self.player+1]
    if joypadData then
        joypadData.focus = _window;
    end
end

function ISMoveableCursor:getRotateableObject()
    local square = self.currentSquare;
    if not square then return false end
    for i = square:getObjects():size(),1,-1 do
        local obj = square:getObjects():get(i-1);
        local moveProps = ISMoveableSpriteProps.new(obj:getSprite());
        if moveProps and moveProps:canManuallyRotate() then
            return { object = obj, moveProps = moveProps };
        end
    end
    return false;
end

function ISMoveableCursor:getInventoryObjectList()
    local objects           = {};
    local spriteBuffer	= {};
    local items 			= self.character:getInventory():getItems();
    local items_size 		= items:size();
    for i=0,items_size-1, 1 do
        local item = items:get(i);
        if instanceof(item, "Moveable") then
            if self.character:getPrimaryHandItem() ~= item and self.character:getSecondaryHandItem() ~= item then
                local moveProps = ISMoveableSpriteProps.new( item:getWorldSprite() );
                if moveProps.isMoveable then
                    local ignoreMulti = false
                    if moveProps.isMultiSprite then
                        local anchorSprite = moveProps.sprite:getSpriteGrid():getAnchorSprite()
                        if spriteBuffer[anchorSprite] then
                            ignoreMulti = true
                        else
                            spriteBuffer[anchorSprite] = true
                            if moveProps.sprite ~= anchorSprite then
                                moveProps = ISMoveableSpriteProps.new(anchorSprite)
                            end
                        end
                    end
                    if not ignoreMulti then
                        table.insert(objects, { object = item, moveProps = moveProps });
                        if self.cacheInvObjectSprite and self.cacheInvObjectSprite == item:getWorldSprite() then
                            self.objectIndex = #objects;
                        end
                    end
                end
            end
        end
    end

    if self.tryInitialInvItem then
        if instanceof(self.tryInitialInvItem, "Moveable") then
            --print("MovablesCursor attempting to set Initial Item: "..self.tryInitialInvItem:getWorldSprite());
            local moveProps = ISMoveableSpriteProps.new(self.tryInitialInvItem:getWorldSprite());
            local sprite = moveProps.sprite;
            if moveProps.isMultiSprite then
                local spriteGrid = moveProps.sprite:getSpriteGrid();
                sprite = spriteGrid:getAnchorSprite();
            end
            local spriteName = sprite:getName();
            for index,table in ipairs(objects) do
                --print("Compare "..table.object:getWorldSprite().." "..spriteName )
                if table.moveProps.sprite == sprite then
                    self.objectIndex = index;
                    self.cacheInvObjectSprite = spriteName;
                    break;
                end
            end
        else
            print("MovablesCursor Initial Item is not a Movable item");
            print(self.tryInitialInvItem);
        end
        self.tryInitialInvItem = nil;
    end

    return objects;
end

function ISMoveableCursor:getObjectList()
    local square = self.currentSquare;
    local objects = {};
    if not square then return objects end
    for i = square:getObjects():size(),1,-1 do
        local obj = square:getObjects():get(i-1);
        local moveProps = ISMoveableSpriteProps.new(obj:getSprite());
        if moveProps and moveProps.isMoveable then
            local add  = true;

            --[[if instanceof(obj,"IsoBarbecue") and obj:isLit() then
                add = false;
            end--]]

            if add then
                table.insert(objects, { object = obj, moveProps = moveProps, isWall = false });
            end
        elseif moveProps and moveProps.spriteProps then
            if moveProps.spriteProps:Is("WallNW") or moveProps.spriteProps:Is("WallN") or moveProps.spriteProps:Is("WallW") then
                local sprList = obj:getChildSprites();
                if sprList then
                    local list_size 	= sprList:size();
                    if list_size > 0 then
                        for i=list_size-1, 0, -1 do
                            local sprite = sprList:get(i):getParentSprite();
                            local moveProps2 = ISMoveableSpriteProps.new( sprite );
                            if moveProps2.isMoveable and moveProps2.type == "WallOverlay" then
                                table.insert(objects, { object = obj, moveProps = moveProps2, isWall = true });
                            end
                        end
                    end
                end
            end
        end
    end
    return objects;
end

function ISMoveableCursor:getScrapObjectList()
    local square = self.currentSquare;
    local objects = {};
    if not square then return objects end
    for i = square:getObjects():size(),1,-1 do
        local obj = square:getObjects():get(i-1);
        local moveProps = ISMoveableSpriteProps.fromObject(obj);
        if moveProps and moveProps.canScrap then
            if moveProps.material and ISMoveableDefinitions:getInstance().isScrapDefinitionValid( moveProps.material ) then
                table.insert(objects, { object = obj, moveProps = moveProps });
            elseif moveProps.scrapThumpable then
                table.insert(objects, { object = obj, moveProps = moveProps });
            end
        end
    end
    return objects;
end

function ISMoveableCursor:onJoypadPressButton(joypadIndex, joypadData, button)
    local playerObj = getSpecificPlayer(joypadData.player)
    if button == Joypad.AButton then
        if self.canBeBuild and self.canCreate then
            self:tryBuild(self.xJoypad, self.yJoypad, self.zJoypad)
        else
            -- Hack to allow opening doors with A button
            local bp = getButtonPrompts(joypadData.player)
            bp:onAPress()
        end
    end

    if button == Joypad.BButton then
        getCell():setDrag(nil, joypadData.player);
    end

    if button == Joypad.XButton then
        if ISMoveableCursor.mode[self.player] == "place" and self.canCreate then
            local object = self.objectListCache[self.objectIndex].object
            local moveProps = self.objectListCache[self.objectIndex].moveProps
            if moveProps and (moveProps:hasFaces() or moveProps:canRotateDirection()) then
                self:rotateWhilePlacing()
            end
        end
    end
    
    if button == Joypad.YButton then
        self.xJoypad = self.character:getCurrentSquare():getX()
        self.yJoypad = self.character:getCurrentSquare():getY()
    end

    if button == Joypad.RBumper then
        self:rotateKey(0, true);
    end

    if button == Joypad.LBumper then
        ISMoveableCursor.changeModeKey( 0, joypadData.player, true );
    end
end

function ISMoveableCursor:getAPrompt()
    if self.canCreate then
        if ISMoveableCursor.mode[self.player] == "pickup" then
            if self.objectListCache and #self.objectListCache >= 1 then
                return getText("IGUI_PickupItem");
            end
        elseif ISMoveableCursor.mode[self.player] == "place" then
            if self.objectListCache and #self.objectListCache >= 1 then
                return getText("IGUI_PlaceObject");
            end
        elseif ISMoveableCursor.mode[self.player] == "rotate" then
            if self.objectListCache then
                return getText("IGUI_ApplyRotation");
            end
        elseif ISMoveableCursor.mode[self.player] == "scrap" then
            if self.objectListCache and #self.objectListCache >= 1 then
                return getText("IGUI_Scrap");
            end
        end
    end
    return nil
end

function ISMoveableCursor:getXPrompt()
    if ISMoveableCursor.mode[self.player] == "place" then
        if self.canCreate and self.objectListCache and (self.objectIndex >= 1) and (self.objectIndex <= #self.objectListCache) then
            local item = self.objectListCache[self.objectIndex].object;
            local moveProps = self.objectListCache[self.objectIndex].moveProps;
            if moveProps and (moveProps:hasFaces() or moveProps:canRotateDirection()) then
                return getText("IGUI_CycleRotation");
            end
        end
    end
    return nil
end

function ISMoveableCursor:getLBPrompt()
    return getText("IGUI_CycleMode");
end

function ISMoveableCursor:getRBPrompt()
    if ISMoveableCursor.mode[self.player] == "pickup" then
        if self.objectListCache and #self.objectListCache > 1 then
            return getText("IGUI_CycleObject");
        end
    elseif ISMoveableCursor.mode[self.player] == "place" then
        if self.objectListCache and #self.objectListCache > 1 then
            return getText("IGUI_CycleItems");
        end
    elseif ISMoveableCursor.mode[self.player] == "rotate" then
        if self.objectListCache and self.canCreate then --and #self.objectListCache >0 then
            return getText("IGUI_CycleRotation");
        end
    elseif ISMoveableCursor.mode[self.player] == "scrap" then
        if self.objectListCache and #self.objectListCache > 1 then
            return getText("IGUI_CycleObject");
        end
    end
	return nil
end

function ISMoveableCursor:new(_character)
    local o = {};
    setmetatable(o, self);
    self.__index = self;
    o:init();
    o.isMoveableCursor = true;
    o.moveableMode = "pickup";
    o.character = _character;
    o.player = _character:getPlayerNum();
    o.skipBuildAction = true;
    o.isYButtonResetCursor = true;
    o.noNeedHammer = false;
    o.skipWalk = true;
    o.renderFloorHelper = true;
    o.objectIndex = 1;
    o.subObjectIndex = 1;
    o.cacheInvObjectSprite = "";
    o.cacheInvObjectIndex = 1;
    o.renderX = -1;
    o.renderY = -1;
    o.renderZ = -1;
    o.yOffset = 0;
    o:setMoveableMode(ISMoveableCursor.mode[o.player] or "pickup");
    ISMoveableCursor.cursors[o.player] = o;
    return o;
end
