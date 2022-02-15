--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 13/08/2021
-- Time: 10:25
-- To change this template use File | Settings | File Templates.
--

--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isServer() then return end

require "ISBuildingObject"

ISPlace3DItemCursor = ISBuildingObject:derive("ISPlace3DItemCursor");

local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

function ISPlace3DItemCursor:create(x, y, z, north, sprite)
    local drop = false;
    if self.itemSq and luautils.walkAdj(self.chr, self.itemSq, true) then
        drop = true;
    end
    if not drop then return; end
    for i,v in ipairs(self.items) do
--        if not v:getWorldItem() or not AdjacentFreeTileFinder.isTileOrAdjacent(self.selectedSqDrop, v:getWorldItem():getSquare()) then
            ISWorldObjectContextMenu.transferIfNeeded(self.chr, v)
--        end
    end
    if self.chr:getJoypadBind() == -1 then
        self.placeAll = isShiftKeyDown()
    end
    if self.placeAll then
        if luautils.walkAdj(self.chr, self.selectedSqDrop, true) then
            for i,v in ipairs(self.items) do
                if self.chr:isEquipped(v) then
                    ISTimedActionQueue.add(ISUnequipAction:new(self.chr, v, 1));
                end
                ISTimedActionQueue.add(ISDropWorldItemAction:new (self.chr, v, self.selectedSqDrop, self.render3DItemXOffset, self.render3DItemYOffset, self.render3DItemZOffset, self.render3DItemRot));
            end
        end
    else
        local item = table.remove(self.items, 1)
        if luautils.walkAdj(self.chr, self.selectedSqDrop, true) then
            if self.chr:isEquipped(item) then
                ISTimedActionQueue.add(ISUnequipAction:new(self.chr, item, 1));
            end
            ISTimedActionQueue.add(ISDropWorldItemAction:new(self.chr, item, self.selectedSqDrop, self.render3DItemXOffset, self.render3DItemYOffset, self.render3DItemZOffset, self.render3DItemRot));
        end
        if #self.items > 0 then
            getCell():setDrag(self, self.chr:getPlayerNum())
        end
    end

end

function ISPlace3DItemCursor:isValid(square)
    if not self.previousSq then
        self.previousSq = square;
    end
    if self.previousSq ~= square then -- reset the selected high when changing sq
        self.previousSq = square;
        self.surfaceSelected = 1;
    end
    if self.chr:getCharacterActions():isEmpty() then
        self.chr:faceLocation(square:getX(), square:getY())
    end
--    print("render X/Y", self.render3DItemXOffset, self.render3DItemYOffset, self.render3DItemRot)
    if not square:isCouldSee(self.chr:getPlayerNum()) then
        return false
    end
    if square:isWallTo(self.chr:getCurrentSquare()) or square:isWindowTo(self.chr:getCurrentSquare()) then
        return false
    end
    local surface = self:getSurface(square)
    if (surface == 0) and (square:isSolid() or square:isSolidTrans() or not square:TreatAsSolidFloor()) then
        return false
    end
    return true;
end

function ISPlace3DItemCursor:render(x, y, z, square)
    if not self.RENDER_SPRITE_FLOOR then
        self.RENDER_SPRITE_FLOOR = IsoSprite.new()
        self.RENDER_SPRITE_FLOOR:LoadFramesNoDirPageSimple('media/ui/FloorTileCursor.png')
    end
    if not square or not self:isValid(square) then
        self.RENDER_SPRITE_FLOOR:RenderGhostTileColor(x, y, z, 1.0, 0.0, 0.0, 0.2)
        return
    end
    self.RENDER_SPRITE_FLOOR:RenderGhostTileColor(x, y, z, 1.0, 1.0, 1.0, 0.2)

    self:checkRotateKey()
    self:checkSelectSurfaceKey()
    self:checkRotateJoypad()

    if self.surfaceKeyTimer then
        self.surfaceKeyTimer = self.surfaceKeyTimer - 1;
        if self.surfaceKeyTimer == 0 then
            self.surfaceKeyTimer = nil;
        end
    end

    local worldX = x + 0.5
    local worldY = y + 0.5
    if self.chr:getPlayerNum() == 0 and self.chr:getJoypadBind() == -1 then
        worldX = screenToIsoX(self.player, getMouseX(), getMouseY(), math.floor(self.chr:getZ()))
        worldY = screenToIsoY(self.player, getMouseX(), getMouseY(), math.floor(self.chr:getZ()))
    end
    local sq = getSquare(worldX, worldY, self.chr:getZ());
    if not sq then
        return;
    end
    self.render3DItemXOffset = worldX - sq:getX();
    self.render3DItemYOffset = worldY - sq:getY();
    self.render3DItemZOffset = self:getSurface(sq);
    if square:HasStairs() then
        self.render3DItemZOffset = square:getApparentZ(self.render3DItemXOffset, self.render3DItemYOffset)
    end
    self.selectedSqDrop = sq;
    if self.placeAll then
        for i,v in ipairs(self.items) do
            -- ensure you're not too far
            local sq = nil;
            if v:getWorldItem() then
                sq = v:getWorldItem():getSquare();
            end
            if v:getOutermostContainer() and v:getOutermostContainer():getParent() then
                sq = v:getOutermostContainer():getParent():getSquare();
            end
            self.itemSq = sq;
            Render3DItem(v, sq, worldX, worldY, self.selectedSqDrop:getZ() + self.render3DItemZOffset, self:clamp(self.render3DItemRot));
        end
    else
        local item = self.items[1]
        local sq = nil;
        if item:getWorldItem() then
            sq = item:getWorldItem():getSquare();
        end
        if item:getOutermostContainer() and item:getOutermostContainer():getParent() then
            sq = item:getOutermostContainer():getParent():getSquare();
        end
        self.itemSq = sq;
        Render3DItem(item, sq, worldX, worldY, self.selectedSqDrop:getZ() + self.render3DItemZOffset, self:clamp(self.render3DItemRot));
    end

--    ISPlace3DItemCursor.panel:drawText("Press R/Shift-R to rotate", 0, 0, 1, 1, 1, 1, UIFont.Medium);

--    ISEquippedItem.instance:drawText(getText("IGUI_Place3DItem_Rotate", getKeyName(getCore():getKey("Rotate building")), getKeyName(42)), 0, 200, 1, 1, 1, 1, UIFont.Small);
--    if self.surfacesPossible and #self.surfacesPossible > 1 then
--        ISEquippedItem.instance:drawText(getText("IGUI_Place3DItem_Surface", getKeyName(getCore():getKey("Toggle mode"))), 0, 220, 1, 1, 1, 1, UIFont.Small);
--    end
end

-- For keyboard & mouse user.
function ISPlace3DItemCursor:drawPrompt(playerNum, ui)
    if playerNum ~= 0 then return end
    if JoypadState.players[playerNum+1] then return end
    local screenX = getPlayerScreenLeft(playerNum)
    local screenY = getPlayerScreenTop(playerNum)
    local screenW = getPlayerScreenWidth(playerNum)
    local screenH = getPlayerScreenHeight(playerNum)
    local y = screenH - (FONT_HGT_MEDIUM * 4)
    local textW = getTextManager():MeasureStringX(UIFont.Medium, getText("IGUI_Place3DItem_Rotate"))
    local hotBar = getPlayerHotbar(playerNum)
    if hotBar and hotBar:isReallyVisible() and (screenX + screenW - 30 - textW < hotBar:getRight()) then
        y = hotBar:getY() - FONT_HGT_MEDIUM * 3
    end
    ui:drawTextRight(getText("IGUI_Place3DItem_Rotate", getKeyName(getCore():getKey("Rotate building")), getKeyName(42)), screenX + screenW - 30, y, 1, 1, 1, 1, UIFont.NewMedium);
    y = y + FONT_HGT_MEDIUM;
    if self.surfacesPossible and #self.surfacesPossible > 1 then
        ui:drawTextRight(getText("IGUI_Place3DItem_Surface", getKeyName(getCore():getKey("Toggle mode"))), screenX + screenW - 30, y, 1, 1, 1, 1, UIFont.NewMedium);
    end
end

function ISPlace3DItemCursor:getSurface(square)
    -- check if we have a surface, so we can do a z offset to make items goes on this surface
    -- we list all our possible surface height, allow Shift to switch thru them
--    local surface = 0
    self.surfacesPossible = {};
    for i=1,square:getObjects():size() do
        local object = square:getObjects():get(i-1);
        if object:getSurfaceOffsetNoTable() > 0 then
            -- the surface is in pixel, set for the 1X texture, so we *2 (192 pixels is 1X texture height)
            local newSurf = (object:getSurfaceOffsetNoTable() / 96);
            local add = true;
            for x,j in ipairs(self.surfacesPossible) do
                if j == newSurf then
                    add = false;
                    break;
                end
            end
            if add then
                table.insert(self.surfacesPossible, newSurf)
            end
--            if newSurf > surface then
--                surface = newSurf;
--            end
        end
    end
--    print(self.surfacesPossible[2])
--    print("total possible surface: ")
--    for x,j in ipairs(self.surfacesPossible) do
--        print(x, j)
--    end
    return self.surfacesPossible[self.surfaceSelected] or 0;
end

function ISPlace3DItemCursor:rotateDelta()
    return 0.75 * getGameTime():getMultiplier() / 1.6
end

function ISPlace3DItemCursor:clamp(rot)
    -- Clamp to 10-degree intervals
    return round(rot / 5, 0) * 5
end

function ISPlace3DItemCursor:checkSelectSurfaceKey() -- switch between our possible surface height when pressing Shift
    if self.surfaceKeyTimer then
        return;
    end
    if self.chr:getPlayerNum() ~= 0 then return end
    if self.chr:getJoypadBind() ~= -1 then return end
    local pressed = isKeyDown(getCore():getKey("Toggle mode"))
    if(pressed) then
        self.surfaceKeyTimer = 10;
        self.surfaceSelected = self.surfaceSelected + 1;
        if self.surfaceSelected > #self.surfacesPossible then
            self.surfaceSelected = 1;
        end
    end
end

function ISPlace3DItemCursor:checkRotateKey()
    if self.chr:getPlayerNum() ~= 0 then return end
    if self.chr:getJoypadBind() ~= -1 then return end
    local pressed = isKeyDown(getCore():getKey("Rotate building"))
    local reverse = isShiftKeyDown()
    self:handleRotate(pressed, reverse)
end

function ISPlace3DItemCursor:checkRotateJoypad()
    if self.chr:getJoypadBind() == -1 then return end
    -- FIXME: the way the model rotates depends on the up-axis
    local reverse = isJoypadLBPressed(self.chr:getJoypadBind())
    local pressed = isJoypadRBPressed(self.chr:getJoypadBind()) or reverse
    self:handleRotate(pressed, reverse)
end

function ISPlace3DItemCursor:handleRotate(pressed, reverse)
    if pressed then
        self.rotateReverse = reverse
        if not self.rotatePressed then
            self.rotatePressed = true
            self.rotateStart = getTimestampMs()
            return
        elseif getTimestampMs() - self.rotateStart > 250 then
            self.rotating = true
        else
            return
        end
        local rot = self.render3DItemRot;
        if reverse then
            rot = rot - 5 * self:rotateDelta();
        else
            rot = rot + 5 * self:rotateDelta();
        end
        if rot < 0 then
            rot = 360;
        end
        if rot > 360 then
            rot = 0;
        end
        self.render3DItemRot = rot;
    else
        if self.rotatePressed then
            self.rotatePressed = false
            local rot = self.render3DItemRot;
            if not self.rotating then
                 -- Quick tap/release rotates 10 degrees
                if self.rotateReverse then
                    rot = rot - 5;
                else
                    rot = rot + 5;
                end
                if rot < 0 then
                    rot = 360;
                end
                if rot > 360 then
                    rot = 0;
                end
            end
            self.render3DItemRot = self:clamp(rot)
        end
        self.rotating = false
    end
end

function ISPlace3DItemCursor:checkRotateJoypad()
    if self.chr:getJoypadBind() == -1 then return end
    -- FIXME: the way the model rotates depends on the up-axis
    local reverse = isJoypadLBPressed(self.chr:getJoypadBind())
    local pressed = isJoypadRBPressed(self.chr:getJoypadBind()) or reverse
    self:handleRotate(pressed, reverse)
end

function ISPlace3DItemCursor:onJoypadPressButton(joypadIndex, joypadData, button)
    if button == Joypad.XButton then
        self.placeAll = not self.placeAll
    else
        ISBuildingObject.onJoypadPressButton(self, joypadIndex, joypadData, button)
    end
end

function ISPlace3DItemCursor:getAPrompt()
    return getText("ContextMenu_PlaceItemOnGround")
end

function ISPlace3DItemCursor:getXPrompt()
    return getText(self.placeAll and "ContextMenu_PlaceAll" or "ContextMenu_PlaceOne")
end

function ISPlace3DItemCursor:new(character, items)
    local o = {}
    setmetatable(o, self)
    self.__index = self
    o:init()
    o.chr = character
    o.character = character
    o.items = items;
    o.player = character:getPlayerNum()
    o.skipBuildAction = true
    o.noNeedHammer = false
    o.skipWalk2 = true
    o.dragNilAfterPlace = true
    o.isYButtonResetCursor = true
    o.render3DItemRot = 0;
    o.surfaceSelected = 1;
    o.placeAll = false
    o.previousSq = nil;
    o.isPlace3DCursor = true;
    return o
end

