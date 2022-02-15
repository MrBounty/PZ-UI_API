ISObjectClickHandler = {}

ISObjectClickHandler.doRDoubleClick = function (object, x, y)

    local sq = object:getSquare();
    if instanceof(object, "IsoMovingObject") then
        sq = object:getCurrentSquare();
    end

    if not sq:isSeen(0) then
        return;
    end

end

local function tableContains( _table, _item )
    for _,v in pairs(_table) do
        if v == _item then return true end
    end
    return false
end

ISObjectClickHandler.doRClick = function (object, x, y)
    local sq = object:getSquare();
    if instanceof(object, "IsoMovingObject") then
        sq = object:getCurrentSquare();
    end

    local objects = {}
    if (sq and sq:isSeen(0)) or instanceof(object, "IsoWindow") or instanceof(object, "IsoDoor") or instanceof(object, "IsoThumpable") or instanceof(object, "IsoTree") then
        table.insert(objects, object);
    end
    local doorTrans = IsoObjectPicker.Instance:PickDoor(x, y, true)
    if doorTrans ~= nil and not tableContains(objects, doorTrans) then
        table.insert(objects, doorTrans)
    end
    local window = IsoObjectPicker.Instance:PickWindow(x, y)
    if window ~= nil then
        table.insert(objects, window)
    end
    local windowFrame = IsoObjectPicker.Instance:PickWindowFrame(x, y)
    if windowFrame ~= nil then
        table.insert(objects, windowFrame)
    end
    local thump = IsoObjectPicker.Instance:PickThumpable(x, y)
    if thump ~= nil then
        table.insert(objects, thump)
    end
    local tree = IsoObjectPicker.Instance:PickTree(x, y)
    if tree then
        table.insert(objects, tree)
    end
    if #objects == 0 then return end
    -- If curtains, then put the window and vice versa (remember curtains are sometimes on a different tile to window, curtainsS on one tile north of windowN for e.g.)
    --ISWorldObjectContextMenu.createMenu(0, objects, x, y);
    ISContextManager.getInstance().createWorldMenu( 0, object, objects, x, y );
end

ISObjectClickHandler.doDoubleClick = function (object, x, y)

    local sq = object:getSquare();
    if instanceof(object, "IsoMovingObject") then
       sq = object:getCurrentSquare();
    end

    if not sq:isSeen(0) then
        return;
    end
    if object:getContainer() ~= nil then
        local parent = object:getSquare();

        if parent ~= nil then
            local dist = IsoUtils.DistanceTo2D(parent:getX()+0.5, parent:getY()+0.5, getSpecificPlayer(0):getX(), getSpecificPlayer(0):getY());
            if not AdjacentFreeTileFinder.isTileOrAdjacent(getSpecificPlayer(0):getCurrentSquare(), parent) then
                local adjacent = AdjacentFreeTileFinder.Find(parent, getSpecificPlayer(0));
                if adjacent ~= nil then
                    --print("clearing action queue.")
                --    ISTimedActionQueue.clear(getPlayer());
                    --print("adding walk to adjacent.")
                --    ISTimedActionQueue.add(ISWalkToTimedAction:new(getPlayer(), adjacent));
                    --print("adding open container.")
                --    ISTimedActionQueue.add(ISOpenContainerTimedAction:new(getPlayer(), object:getContainer(), 60, x, y));
                    return;
                else
                    --print("No adjacent found, cancelling.")
                    return;
                end

            else
                --print("adding open container.")
            --    ISTimedActionQueue.add(ISOpenContainerTimedAction:new(getPlayer(), object:getContainer(), 60, x, y));

            end
        end
    else
	  --  ISTimedActionQueue.clear(getPlayer());
	 --   ISTimedActionQueue.add(ISWalkToTimedAction:new(getPlayer(), sq));

    end
end

function ISObjectClickHandler.doClickCurtain(object, playerNum, playerObj)
    if not object:canInteractWith(playerObj) then return false end
    object:ToggleDoor(playerObj)
    return true
end

function ISObjectClickHandler.doClickDoor(object, playerNum, playerObj)
    if false then
        ISWorldObjectContextMenu.onOpenCloseDoor(nil, object, playerNum)
        return true
    end
    local playerSq = playerObj:getCurrentSquare()
    if object:getSquare():getZ() ~= playerSq:getZ() then return false end
    if not object:isAdjacentToSquare(playerSq) then return false end
    if isKeyDown(Keyboard.KEY_LSHIFT) then
        if object:HasCurtains() and (playerSq == object:getSheetSquare()) then
            object:toggleCurtain()
        end
        return true
    end
    object:ToggleDoor(playerObj)
    return true
end

function ISObjectClickHandler.doClickLightSwitch(object, playerNum, playerObj)
    if false then
        ISWorldObjectContextMenu.onToggleLight(nil, object, playerNum)
        return true
    end
    local playerSq = playerObj:getCurrentSquare()
    if object:getSquare():DistToProper(playerObj) >= 2 then return false end
    if playerSq:isWallTo(object:getSquare()) then return false end
    object:toggle()
    return true
end

function ISObjectClickHandler.doClickThumpable(object, playerNum, playerObj)
    local playerSq = playerObj:getCurrentSquare()
    if object:getSquare():getZ() ~= playerSq:getZ() then return false end
    if not object:isDoor() then return false end
    if not object:isAdjacentToSquare(playerSq) then return false end
    object:ToggleDoor(playerObj)
    return true
end

function ISObjectClickHandler.doClickWindow(object, playerNum, playerObj)
    if isKeyDown(Keyboard.KEY_LSHIFT) then
        local curtain = object:HasCurtains()
        if curtain and curtain:canInteractWith(playerObj) then
            curtain:ToggleDoor(playerObj)
        end
        return true
    end
    if object:isInvincible() then return true end
    local playerSq = playerObj:getCurrentSquare()
    if (playerSq ~= object:getSquare()) and (playerSq ~= object:getOppositeSquare()) then return true end
    if object:getBarricadeForCharacter(playerObj) then return true end
    if object:isDestroyed() then
        if not object:isBarricaded() then
            playerObj:climbThroughWindow(object)
        end
        return true
    end
    if object:IsOpen() then
        playerObj:closeWindow(object)
    else
        playerObj:openWindow(object)
    end
    return true
end

function ISObjectClickHandler.doClickSpecificObject(object, playerNum, playerObj)
    local isPaused = UIManager.getSpeedControls() and UIManager.getSpeedControls():getCurrentGameSpeed() == 0
    if isPaused then return false end

    if not playerObj or playerObj:isDead() then return false end

    if not playerObj:getCurrentSquare() then return false end

    if playerObj:isAiming() then return false end

    if playerObj:isIgnoreContextKey() then return false end

    if instanceof(object, "IsoCurtain") then
        return ISObjectClickHandler.doClickCurtain(object, playerNum, playerObj)
    end
    if instanceof(object, "IsoDoor") then
        return ISObjectClickHandler.doClickDoor(object, playerNum, playerObj)
    end
    if instanceof(object, "IsoLightSwitch") then
        return ISObjectClickHandler.doClickLightSwitch(object, playerNum, playerObj)
    end
    if instanceof(object, "IsoThumpable") then
        return ISObjectClickHandler.doClickThumpable(object, playerNum, playerObj)
    end
    if instanceof(object, "IsoWindow") then
        return ISObjectClickHandler.doClickWindow(object, playerNum, playerObj)
    end
    return false
end

ISObjectClickHandler.doClick = function (object, x, y)
    local sq = object:getSquare();
    if instanceof(object, "IsoMovingObject") then
        sq = object:getCurrentSquare();
    end

    if not sq:isSeen(0) then
        return;
    end

    local playerNum = 0
    local playerObj = getSpecificPlayer(playerNum)

    local isPaused = UIManager.getSpeedControls() and UIManager.getSpeedControls():getCurrentGameSpeed() == 0
    if getCore():getGameMode() ~= "Tutorial" and not isPaused and instanceof(object, "IsoWaveSignal") and playerObj:isAlive() and not playerObj:IsAiming() and
            playerObj:getCurrentSquare() and object:getSquare() and
            playerObj:DistToSquared(object:getX() + 0.5, object:getY() + 0.5) < 1.5 * 1.5 and
            not playerObj:getCurrentSquare():isSomethingTo(object:getSquare()) then
        ISRadioWindow.activate(playerObj, object)
        return
    end

    if isClient() and SafeHouse.isSafeHouse(sq, playerObj:getUsername(), true) and
            not getServerOptions():getBoolean("SafehouseAllowLoot") then
        return
    end

    if object:getContainer() and not playerObj:IsAiming() then
        if instanceof(object, "IsoThumpable") and object:isLockedToCharacter(playerObj) then
            return
        end
        local parent = object:getSquare();

        if parent ~= nil then
            local playerSq = playerObj:getCurrentSquare()
            if not playerSq or math.abs(playerSq:getX() - parent:getX()) > 1 or
                    math.abs(playerSq:getY() - parent:getY()) > 1 or
                    playerSq:getZ() ~= parent:getZ() or
                    playerSq:isBlockedTo(parent) then
                -- out of range
            else
                --print("adding open container.")
              -- ISTimedActionQueue.add(ISOpenContainerTimedAction:new(getPlayer(), object:getContainer(), 20, x, y));
                if not object:getContainer():isExplored() then

                    if not isClient() then
                        ItemPicker.fillContainer(object:getContainer(), playerObj);
                    else
                        object:getContainer():requestServerItemsForContainer();
                    end
                    object:getContainer():setExplored(true);
                end
                local panel2 = getPlayerLoot(0);

                panel2:setNewContainer(object:getContainer());
                panel2:setVisible(true);
                panel2.lootAll:setVisible(true);
                panel2.collapseCounter = 0;
                if panel2.isCollapsed then
                    panel2.isCollapsed = false;
                    panel2:clearMaxDrawHeight();
                    panel2.collapseCounter = -30;
                end
            end
        end
    end
end

-- master function for handling object clicks
ISObjectClickHandler.onObjectLeftMouseButtonDown = function(object, x, y)
    local playerNum = 0
    local playerObj = getSpecificPlayer(playerNum)

    if not playerObj or playerObj:isDead() then
        return
    end
    
    local self = ISObjectClickHandler
    if self.lastClickTime ~= nil and UIManager.isDoubleClick(self.downx, self.downy, x, y, self.lastClickTime) then
        self.isDoubleClick = true
    else
        self.isDoubleClick = false
    end
    self.lastClickTime = getTimestampMs()
    self.clickTime = 0;

    self.downx = x;
    self.downy = y;

    if object ~= nil then

        self.downObject = object;

        if ISObjectClickHandler.doClickSpecificObject(object, playerNum, playerObj) then
            return
        end

        --doOldContainerUI(object, x, y);
    end

end
-- master function for handling object clicks
ISObjectClickHandler.onObjectLeftMouseButtonUp = function(object, x, y)

    if ISObjectClickHandler.isDoubleClick then
        if object ~= nil and ISObjectClickHandler.downObject == object then
            ISObjectClickHandler.downObject = nil;

            ISObjectClickHandler.doDoubleClick(object, x, y);
        end
    else
        if object ~= nil and ISObjectClickHandler.downObject == object then
            ISObjectClickHandler.downObject = nil;

            ISObjectClickHandler.doClick(object, x, y);
        end

    end

end

-- master function for handling object clicks
ISObjectClickHandler.onObjectRightMouseButtonDown = function(object, x, y)
    local self = ISObjectClickHandler
    if self.lastRClickTime ~= nil and UIManager.isDoubleClick(self.downx, self.downy, x, y, self.lastRClickTime) then
        self.isDoubleClick = true
    else
        self.isDoubleClick = false
    end
    self.lastRClickTime = getTimestampMs()
    self.rclickTime = 0;

    self.downx = x;
    self.downy = y;

    if object ~= nil then

        self.rdownObject = object;

        --doOldContainerUI(object, x, y);
    end

end
-- master function for handling object clicks
ISObjectClickHandler.onObjectRightMouseButtonUp = function(object, x, y)

    if ISObjectClickHandler.isDoubleClick then
        if object ~= nil and ISObjectClickHandler.rdownObject == object then
            ISObjectClickHandler.rdownObject = nil;

            ISObjectClickHandler.doRDoubleClick(object, x, y);
        end
    else
        local timeHeldDown = getTimestampMs() - (ISObjectClickHandler.lastRClickTime or 0)
        if object ~= nil and ISObjectClickHandler.rdownObject == object and timeHeldDown < 500 then
            ISObjectClickHandler.rdownObject = nil;

            ISObjectClickHandler.doRClick(object, x, y);
        end

    end

end

-- master function for handling object clicks
ISObjectClickHandler.onTick = function()

    if ISObjectClickHandler.rclickTime == nil then
        ISObjectClickHandler.rclickTime = 0;
    end
    ISObjectClickHandler.rclickTime = ISObjectClickHandler.rclickTime + 1;

    if ISObjectClickHandler.clickTime == nil then
        ISObjectClickHandler.clickTime = 0;
    end
    ISObjectClickHandler.clickTime = ISObjectClickHandler.clickTime + 1;

end

--Events.OnTick.Add(ISObjectClickHandler.onTick);
Events.OnObjectLeftMouseButtonUp.Add(ISObjectClickHandler.onObjectLeftMouseButtonUp);
Events.OnObjectLeftMouseButtonDown.Add(ISObjectClickHandler.onObjectLeftMouseButtonDown);
Events.OnObjectRightMouseButtonUp.Add(ISObjectClickHandler.onObjectRightMouseButtonUp);
Events.OnObjectRightMouseButtonDown.Add(ISObjectClickHandler.onObjectRightMouseButtonDown);
