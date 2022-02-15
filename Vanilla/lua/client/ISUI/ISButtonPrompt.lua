require "ISUI/ISUIElement"

ISButtonPrompt = ISUIElement:derive("ISButtonPrompt");

local FONT_HGT_LARGE = getTextManager():getFontHeight(UIFont.Large)

--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISButtonPrompt:initialise()
    ISUIElement.initialise(self);
end

--************************************************************************--
--** ISButtonPrompt:render
--**
--************************************************************************--
function ISButtonPrompt:prerender()

    if self.player ~= nil and (getFocusForPlayer(self.player) ~= nil and not getFocusForPlayer(self.player).overrideBPrompt) then
        return;
    end

    local joypadData = self.player and JoypadState.players[self.player+1] or nil
    local joypadID = (joypadData and joypadData.player) and joypadData.id or nil

    if not joypadID then
        -- Player 0 reverted to keyboard and mouse
        return
    end

    if joypadID and not isJoypadConnected(joypadID) then
        return
    end

    local focus = getFocusForPlayer(self.player);
    if focus and focus.overrideBPrompt then
        if focus.isValidPrompt and focus:isValidPrompt() then
            self:setAPrompt(nil, nil, nil);
            self:setBPrompt(nil, nil, nil);
            self:setXPrompt(nil, nil, nil);
            self:setYPrompt(nil, nil, nil);
            self:setLBPrompt(nil, nil, nil);
            self:setRBPrompt(nil, nil, nil);
            if focus.getAPrompt and focus:getAPrompt() then self:setAPrompt(focus:getAPrompt(), nil); end
            if focus.getBPrompt and focus:getBPrompt() then self:setBPrompt(focus:getBPrompt(), nil); end
            if focus.getXPrompt and focus:getXPrompt() then self:setXPrompt(focus:getXPrompt(), nil); end
            if focus.getYPrompt and focus:getYPrompt() then self:setYPrompt(focus:getYPrompt(), nil); end
            if focus.getLBPrompt and focus:getLBPrompt() then self:setLBPrompt(focus:getLBPrompt(), nil); end
            if focus.getRBPrompt and focus:getRBPrompt() then self:setRBPrompt(focus:getRBPrompt(), nil); end
        end
    end

    local x = getPlayerScreenLeft(self.player);
    local y = getPlayerScreenTop(self.player);
    local w = getPlayerScreenWidth(self.player);
    local h = getPlayerScreenHeight(self.player);

    self.lmargin = 16;
    self.rmargin = 16;

    local clock = UIManager.getClock()
    local clockWidth = clock:getWidth()
    if clock and clock:isVisible() and getNumActivePlayers() > 1 and y + h > clock:getY() then
        if x < clock:getX() then
            self.rmargin = self.rmargin + clockWidth / 2
        else
            self.lmargin = self.lmargin + clockWidth / 2
        end
    end

    self.x1 = x;
    self.y1 = y + (h-64) - 16;
    self.w1 = (w / 2);
    self.h1 = (64);
    self.x2 = (x+(w/2));
    self.y2 = self.y1
    self.w2 = self.w1
    self.h2 = self.h1

    local fontHgt = getTextManager():getFontFromEnum(UIFont.NewLarge):getLineHeight()
    local buttonWid = 32
    local buttonHgt = 32
    local rowHgt = 32
    local shift = 16
    local textPadX = 11

    x = self.x1 + self.lmargin;
    if self.xPrompt ~= nil then
        buttonHgt = self.buttonX:getHeight()
        local dx,dy = 0,0
        if joypadID and isJoypadPressed(joypadID, getJoypadXButton(joypadID)) then dx,dy = 3,3 end
        self:drawTexture(self.buttonX, dx + x, dy + self.y1 + rowHgt + (rowHgt - buttonHgt) / 2, 0.9, 1, 1, 1);
        self:drawText(self.xPrompt, x + buttonWid + textPadX, self.y1 + rowHgt + (rowHgt - fontHgt) / 2, 1, 1, 1, 0.9, UIFont.NewLarge);
    end
    if self.yPrompt ~= nil then
        buttonHgt = self.buttonY:getHeight()
        self:drawTexture(self.buttonY, x + shift, self.y1 + (rowHgt - buttonHgt) / 2, 0.9, 1, 1, 1);
        self:drawText(self.yPrompt, x + buttonWid + textPadX + shift, self.y1 + (rowHgt - fontHgt) / 2, 1, 1, 1, 0.9, UIFont.NewLarge);
    end
    if self.lbPrompt ~= nil then
        buttonHgt = self.buttonLB:getHeight()
        self:drawTexture(self.buttonLB, x + shift, self.y1 - rowHgt + (rowHgt - buttonHgt) / 2, 0.9, 1, 1, 1);
        self:drawText(self.lbPrompt, x + buttonWid + textPadX + shift, self.y1 - rowHgt + (rowHgt - fontHgt) / 2, 1, 1, 1, 0.9, UIFont.NewLarge);
    end

    if self.player > 0 and getCell():getDrag(self.player) and getCell():getDrag(self.player).isMoveableCursor then
        local tex;
        if getCell():getDrag(self.player):getMoveableMode() == "pickup" then
            tex = self.movableIconPickup;
        elseif getCell():getDrag(self.player):getMoveableMode() == "place" then
            tex = self.movableIconPlace;
        elseif getCell():getDrag(self.player):getMoveableMode() == "rotate" then
            tex = self.movableIconRotate;
         elseif getCell():getDrag(self.player):getMoveableMode() == "scrap" then
            tex = self.movableIconScrap;
       end

        if tex then
            buttonHgt = tex:getHeightOrig();
            local shiftmod = (self.buttonLB:getWidth()/2) - (tex:getWidthOrig()/2);
            self:drawTexture(tex, x + shift + shiftmod, self.y1 - rowHgt * 2 + (rowHgt - buttonHgt) / 2, 0.9, 1, 1, 1);
        end
    end

    x = self.x2 + self.w2 - self.rmargin - buttonWid;
    if self.aPrompt ~= nil then
        buttonHgt = self.buttonA:getHeight()
        local dx,dy = 0,0
        if joypadID and isJoypadPressed(joypadID, getJoypadAButton(joypadID)) then dx,dy = 3,3 end
        self:drawTexture(self.buttonA, x - shift + dx, self.y1 + rowHgt + (rowHgt - buttonHgt) / 2 + dy, 0.9, 1, 1, 1);
        self:drawTextRight(self.aPrompt, x - textPadX - shift, self.y1 + rowHgt + (rowHgt - fontHgt) / 2, 1, 1, 1, 0.9, UIFont.NewLarge);
    end
    if self.bPrompt ~= nil then
        buttonHgt = self.buttonB:getHeight()
        self:drawTexture(self.buttonB, x, self.y1 + (rowHgt - buttonHgt) / 2, 0.9, 1, 1, 1);
        self:drawTextRight(self.bPrompt, x - textPadX, self.y1 + (rowHgt - fontHgt) / 2, 1, 1, 1, 0.9, UIFont.NewLarge);
    end
    if self.rbPrompt ~= nil then
        buttonHgt = self.buttonRB:getHeight()
        self:drawTexture(self.buttonRB, x, self.y1 - rowHgt + (rowHgt - buttonHgt) / 2, 0.9, 1, 1, 1);
        self:drawTextRight(self.rbPrompt, x - textPadX, self.y1 - rowHgt + (rowHgt - fontHgt) / 2, 1, 1, 1, 0.9, UIFont.NewLarge);
    end
end

function ISButtonPrompt:update()
    if not self.player then return end
    local joypadData = JoypadState.players[self.player+1]
    if not joypadData or not joypadData.isActive then return end
    if isJoypadRBPressed(joypadData.id) then
        ISFirearmRadialMenu.onRepeatRBumper(self)
    end
end

function ISButtonPrompt:isLootIcon()
	return self.isLoot;
end

function ISButtonPrompt:interact(worldobjects)
    local playerObj = getSpecificPlayer(self.player)
    if playerObj:getVehicle() then
        return
    end
    
    worldobjects = self:getXButtonObjects(nil)
    if not worldobjects then return end
    
    local s = getSpecificPlayer(self.player):getCurrentSquare();
    local x = isoToScreenX(self.player, s:getX(), s:getY(), s:getZ());
    local y = isoToScreenY(self.player, s:getX(), s:getY(), s:getZ());
    if getNumActivePlayers() > 1 then
        x = x - getPlayerScreenWidth(self.player) / 4
        y = y - getPlayerScreenHeight(self.player) / 4
    end
--
--    local menu = ISWorldObjectContextMenu.createMenu(self.player, worldobjects.items, x, y);
    local menu = ISContextManager.getInstance().createWorldMenu( self.player, nil, worldobjects.items, x, y );
    menu.origin = nil;
    if menu:getIsVisible() == false then
        menu:setVisible(true);
    end
    if menu.numOptions > 1 then
        setJoypadFocus(self.player, menu)
        menu.mouseOver = 1;
    end
end

function ISButtonPrompt:climbFence()
    local player = getSpecificPlayer(self.player);
    local dir = player:getDir();
    player:hopFence(dir, false);
end

function ISButtonPrompt:climbOverWall(dir)
    local playerObj = getSpecificPlayer(self.player)
    playerObj:climbOverWall(dir)
end

function ISButtonPrompt:climbInWindow(window)
    local player = getSpecificPlayer(self.player);
    if IsoWindowFrame.isWindowFrame(window) then
        player:climbThroughWindowFrame(window);
    else
        player:climbThroughWindow(window);
    end
end

function ISButtonPrompt:openWindow(window)
    local player = getSpecificPlayer(self.player);
    player:openWindow(window);
end

function ISButtonPrompt:closeWindow(window)
    local player = getSpecificPlayer(self.player);
    player:closeWindow(window);
end

function ISButtonPrompt:openDoor(door)
    local player = getSpecificPlayer(self.player);
    door:ToggleDoor(player);
end

function ISButtonPrompt:smashWindow(window)
    local player = getSpecificPlayer(self.player);
    player:smashWindow(window);
end
function ISButtonPrompt:sleep()

--~ 	local player = getSpecificPlayer(self.player);
    ISWorldObjectContextMenu.onSleep(nil, self.player, true);
end

function ISButtonPrompt:cmdShowInventory()
    local playerObj = getSpecificPlayer(self.player)
    local ui = getPlayerInventory(self.player)
    ui:setVisible(true)
    local joypadData = JoypadState.players[self.player+1]
    joypadData.focus = ui
    updateJoypadFocus(joypadData)
    playerObj:setBannedAttacking(true)
end

function ISButtonPrompt:cmdShowLoot()
    local playerObj = getSpecificPlayer(self.player)
    local ui = getPlayerLoot(self.player)
    ui:setVisible(true)
    local joypadData = JoypadState.players[self.player+1]
    joypadData.focus = ui
    updateJoypadFocus(joypadData)
    playerObj:setBannedAttacking(true)
end

function ISButtonPrompt:cmdToggleLight(light)
    local playerObj = getSpecificPlayer(self.player)
    ISTimedActionQueue.add(ISToggleLightAction:new(playerObj, light))
end

function ISButtonPrompt:cmdToggleStove(stove)
    local playerObj = getSpecificPlayer(self.player)
    ISTimedActionQueue.add(ISToggleStoveAction:new(playerObj, stove))
end

function ISButtonPrompt:openDeviceOptions(device)
    local playerObj = getSpecificPlayer(self.player);
    ISRadioWindow.activate( playerObj, device, false );
end

function ISButtonPrompt:cmdUseVehicle(vehicle, part)
    local playerObj = getSpecificPlayer(self.player)
    VehicleUtils.callLua(part:getLuaFunction("use"), vehicle, part, playerObj)
end

function ISButtonPrompt:cmdEnterVehicle(vehicle, seat)
    local playerObj = getSpecificPlayer(self.player)
    ISVehicleMenu.onEnter(playerObj, vehicle, seat)
end

function ISButtonPrompt:cmdCloseVehicleDoor(playerObj, part)
    ISVehicleMenu.onCloseDoor(playerObj, part)
end

function ISButtonPrompt:cmdOpenVehicleDoor(playerObj, part)
    if part:getId() == "EngineDoor" and part:getLuaFunction("use") then
        VehicleUtils.callLua(part:getLuaFunction("use"), part:getVehicle(), part, playerObj)
        return
    end
    ISVehicleMenu.onOpenDoor(playerObj, part)
end

function ISButtonPrompt:cmdExitVehicle()
    local playerObj = getSpecificPlayer(self.player)
    ISVehicleMenu.onExit(playerObj)
end

function ISButtonPrompt:stopAction()
    local playerObj = getSpecificPlayer(self.player)
    stopDoingActionThatCanBeCancelled(playerObj)
end

function ISButtonPrompt:getBestAButtonAction(dir)

    if UIManager.getSpeedControls() and UIManager.getSpeedControls():getCurrentGameSpeed() == 0 then
        self:setAPrompt(nil, nil, nil);
        return;
    end

    if dir == nil then
        self:setAPrompt(nil, nil, nil);
    end

    if getCell():getDrag(self.player) then
        self:setAPrompt(getCell():getDrag(self.player):getAPrompt(), nil);
    end

    local playerObj = getSpecificPlayer(self.player)
    
    if playerObj:getIgnoreMovement() or playerObj:isAsleep() then return end

    local vehicle = playerObj:getVehicle()
    if vehicle then
        self:setAPrompt(getText("IGUI_ExitVehicle"), self.cmdExitVehicle)
        return
    end

    local square1 = playerObj:getCurrentSquare();
    if square1 == nil then return; end

    if dir == nil then
        dir = playerObj:getDir();
    end

    if dir == IsoDirections.NE then
        self:testAButtonAction(IsoDirections.N);
        self:testAButtonAction(IsoDirections.E);
    elseif dir == IsoDirections.SE then
        self:testAButtonAction(IsoDirections.S);
        self:testAButtonAction(IsoDirections.E);
    elseif dir == IsoDirections.SW then
        self:testAButtonAction(IsoDirections.S);
        self:testAButtonAction(IsoDirections.W);
    elseif dir == IsoDirections.NW then
        self:testAButtonAction(IsoDirections.N);
        self:testAButtonAction(IsoDirections.W);
    else
        self:testAButtonAction(dir);
    end

    if self.aPrompt then return end

    -- Nothing was found in the direction the player is facing.
    -- Try a door, window or windowframe behind the player.
    local dir1 = nil
    local dir2 = nil
    if dir == IsoDirections.NW then
        dir1 = IsoDirections.S
        dir2 = IsoDirections.E
    elseif dir == IsoDirections.NE then
        dir1 = IsoDirections.S
        dir2 = IsoDirections.W
    elseif dir == IsoDirections.SE then
        dir1 = IsoDirections.N
        dir2 = IsoDirections.W
    elseif dir == IsoDirections.SW then
        dir1 = IsoDirections.N
        dir2 = IsoDirections.E
    else
        dir1 = dir:RotLeft(4) -- 180 degrees
    end
    local obj = nil
    if dir1 ~= nil then
        obj = playerObj:getContextDoorOrWindowOrWindowFrame(dir1)
        if obj then
            self:doAButtonDoorOrWindowOrWindowFrame(dir1, obj)
            return
        end
    end
    if dir2 ~= nil then
        obj = playerObj:getContextDoorOrWindowOrWindowFrame(dir2)
        if obj then
            self:doAButtonDoorOrWindowOrWindowFrame(dir1, obj)
            return
        end
    end
end

function ISButtonPrompt:testAButtonAction(dir)

    local playerObj = getSpecificPlayer(self.player)
    local square1 = playerObj:getCurrentSquare();
    local square2 = square1:getAdjacentSquare(dir);
    if square2 == nil then return; end

    if self.aPrompt == nil then
        local obj = playerObj:getContextDoorOrWindowOrWindowFrame(dir)
        if obj then
            self:doAButtonDoorOrWindowOrWindowFrame(dir, obj)
        end
    end
        
    if self.aPrompt == nil and square1:getRoom() then
        if (SandboxVars.ElecShutModifier > -1 and getGameTime():getNightsSurvived() < SandboxVars.ElecShutModifier) or square1:haveElectricity() then
            -- Light switch on the player's square
            for i=1,square1:getObjects():size() do
                local object = square1:getObjects():get(i-1)
                if instanceof(object, "IsoLightSwitch") then
                    if object:isActivated() then
                        self:setAPrompt(getText("ContextMenu_Turn_Off"), self.cmdToggleLight, object)
                    else
                        self:setAPrompt(getText("ContextMenu_Turn_On"), self.cmdToggleLight, object)
                    end
                    break
                end
            end
            -- Light switch on adjacent solidtrans square
            if self.aPrompt == nil and square2:getRoom() and not square2:isSomethingTo(square1) and square2:Is(IsoFlagType.solidtrans) then
                for i=1,square2:getObjects():size() do
                    local object = square2:getObjects():get(i-1)
                    if instanceof(object, "IsoLightSwitch") then
                        if object:isActivated() then
                            self:setAPrompt(getText("ContextMenu_Turn_Off"), self.cmdToggleLight, object)
                        else
                            self:setAPrompt(getText("ContextMenu_Turn_On"), self.cmdToggleLight, object)
                        end
                        break
                    end
                end
            end
        end
    end

    if self.aPrompt == nil and square1:getRoom() then
        if (SandboxVars.ElecShutModifier > -1 and getGameTime():getNightsSurvived() < SandboxVars.ElecShutModifier) or square1:haveElectricity() then
            -- Stove on adjacent square
            for i=1,square2:getObjects():size() do
                local object = square2:getObjects():get(i-1)
                if instanceof(object, "IsoStove") and not ISWorldObjectContextMenu.isSomethingTo(object, self.player) then
                    if object:Activated() then
                        self:setAPrompt(getText("ContextMenu_Turn_Off"), self.cmdToggleStove, object)
                    else
                        self:setAPrompt(getText("ContextMenu_Turn_On"), self.cmdToggleStove, object)
                    end
                    break
                end
            end
        end
    end

    if self.aPrompt == nil then
        local vehicle = playerObj:getUseableVehicle()
        if vehicle then
            local part = vehicle:getUseablePart(playerObj)
            if part then
                if part:getDoor() and part:getInventoryItem() then
                    local isHood = part:getId() == "EngineDoor"
                    local isTrunk = part:getId() == "TrunkDoor" or part:getId() == "DoorRear"
                    local seatForDoor = -1
                    if not isHood and not isTrunk then
                        for seat=1,vehicle:getMaxPassengers() do
                            if vehicle:getPassengerDoor(seat-1) == part then
                                seatForDoor = seat-1
                                break
                            end
                            if vehicle:getPassengerDoor2(seat-1) == part then
                                seatForDoor = seat-1
                                break
                            end
                        end
                    end
                    if seatForDoor ~= -1 then
                        self:setAPrompt(getText("IGUI_EnterVehicle"), self.cmdEnterVehicle, vehicle, seatForDoor)
                    elseif part:getDoor():isOpen() then
                        local label = "ContextMenu_Close_door"
                        if isHood then label = "IGUI_CloseHood" end
                        if isTrunk then label = "IGUI_CloseTrunk" end
                        self:setAPrompt(getText(label), self.cmdCloseVehicleDoor, playerObj, part)
                    else
                        local label = "ContextMenu_Open_door"
                        if isHood then label = "IGUI_OpenHood" end
                        if isTrunk then label = "IGUI_OpenTrunk" end
                        self:setAPrompt(getText(label), self.cmdOpenVehicleDoor, playerObj, part)
                    end
                end
            else
                local seat = vehicle:getBestSeat(playerObj)
                if seat ~= -1 then
                    self:setAPrompt(getText("IGUI_EnterVehicle"), self.cmdEnterVehicle, vehicle, seat)
                end
            end
        end
    end

    if self.aPrompt == nil then
        local device = nil;
        if playerObj:getPrimaryHandItem() and instanceof(playerObj:getPrimaryHandItem(), "Radio") then
            device = playerObj:getPrimaryHandItem();
        end
        if not device and (playerObj:getSecondaryHandItem() and instanceof(playerObj:getSecondaryHandItem(), "Radio")) then
            device = playerObj:getSecondaryHandItem();
        end
        if device and not getCore():getGameMode() == "Tutorial" then
            self:setAPrompt(getText("IGUI_DeviceOptions"), self.openDeviceOptions, device)
        end
    end
end

function ISButtonPrompt:doAButtonDoorOrWindowOrWindowFrame(dir, obj)

    local playerObj = getSpecificPlayer(self.player)
    local square1 = playerObj:getCurrentSquare()
    local square2 = square1:getAdjacentSquare(dir)

    if instanceof(obj, "IsoDoor") then
        if obj:isDestroyed() then
            -- nothing
        elseif obj:IsOpen() then
            self:setAPrompt(getText("ContextMenu_Close_door"), ISButtonPrompt.openDoor, obj)
        else
            self:setAPrompt(getText("ContextMenu_Open_door"), ISButtonPrompt.openDoor, obj)
        end
        return
    end

    if instanceof(obj, "IsoThumpable") and obj:isDoor() then
        if obj:IsOpen() then
            self:setAPrompt(getText("ContextMenu_Close_door"), ISButtonPrompt.openDoor, obj)
        else
            self:setAPrompt(getText("ContextMenu_Open_door"), ISButtonPrompt.openDoor, obj)
        end
        return
    end

    if instanceof(obj, "IsoWindow") and not obj:isInvincible() then
        if obj:isDestroyed() or obj:getBarricadeForCharacter(playerObj) then
            -- nothing
        elseif obj:IsOpen() then
            self:setAPrompt(getText("ContextMenu_Close_window"), ISButtonPrompt.closeWindow, obj)
        elseif not obj:getSprite() or not obj:getSprite():getProperties():Is("WindowLocked") then
            self:setAPrompt(getText("ContextMenu_Open_window"), ISButtonPrompt.openWindow, obj)
        end
        return
    end
end

function ISButtonPrompt:getBestBButtonAction(dir)

    if UIManager.getSpeedControls() and UIManager.getSpeedControls():getCurrentGameSpeed() == 0 then
        self:setBPrompt(nil, nil, nil);
        return;
    end

    if dir == nil then
        self:setBPrompt(nil, nil, nil);
    end

    if getCell():getDrag(self.player) then
        self:setBPrompt(getText("UI_Cancel"), nil);
        return
    end

    local playerObj = getSpecificPlayer(self.player)

    if isPlayerDoingActionThatCanBeCancelled(playerObj) then
        self:setBPrompt(getText("UI_Cancel"), ISButtonPrompt.stopAction);
        return
    end

    if playerObj:getIgnoreMovement() or playerObj:isAsleep() then return end

    local vehicle = playerObj:getVehicle()
    if vehicle then
        if vehicle:isDriver(playerObj) and vehicle:isEngineRunning() then
            self:setBPrompt(getText("IGUI_VehicleApplyBrakes"))
        else
            self:setBPrompt(nil, nil, nil)
        end
        return
    end

    if playerObj:isSprinting() then
        self:setBPrompt(getText("IGUI_StopSprint"), nil)
        return
    elseif playerObj:isRunning() and playerObj:canSprint() then
        self:setBPrompt(getText("IGUI_StartSprint"), nil)
        return
    end

    local square1 = playerObj:getCurrentSquare();
    if square1 == nil then return; end

    if dir == nil then
        dir = getSpecificPlayer(self.player):getDir();
    end

    if dir == IsoDirections.NE then
        self:testBButtonAction(IsoDirections.N)
        self:testBButtonAction(IsoDirections.E)
    elseif dir == IsoDirections.SE then
        self:testBButtonAction(IsoDirections.S)
        self:testBButtonAction(IsoDirections.E)
    elseif dir == IsoDirections.SW then
        self:testBButtonAction(IsoDirections.S)
        self:testBButtonAction(IsoDirections.W)
    elseif dir == IsoDirections.NW then
        self:testBButtonAction(IsoDirections.N)
        self:testBButtonAction(IsoDirections.W)
    else
        self:testBButtonAction(dir)
    end

    if self.bPrompt then return end

    -- Nothing was found in the direction the player is facing.
    -- Try a door, window or windowframe behind the player.
    local dir1 = nil
    local dir2 = nil
    if dir == IsoDirections.NW then
        dir1 = IsoDirections.S
        dir2 = IsoDirections.E
    elseif dir == IsoDirections.NE then
        dir1 = IsoDirections.S
        dir2 = IsoDirections.W
    elseif dir == IsoDirections.SE then
        dir1 = IsoDirections.N
        dir2 = IsoDirections.W
    elseif dir == IsoDirections.SW then
        dir1 = IsoDirections.N
        dir2 = IsoDirections.E
    else
        dir1 = dir:RotLeft(4) -- 180 degrees
    end
    local obj = nil
    if dir1 ~= nil then
        obj = playerObj:getContextDoorOrWindowOrWindowFrame(dir1)
        if obj then
            self:doBButtonDoorOrWindowOrWindowFrame(dir1, obj)
            return
        end
    end
    if dir2 ~= nil then
        obj = playerObj:getContextDoorOrWindowOrWindowFrame(dir2)
        if obj then
            self:doBButtonDoorOrWindowOrWindowFrame(dir1, obj)
            return
        end
    end
end

function ISButtonPrompt:testBButtonAction(dir)

    if self.bPrompt then return end
    
    local playerObj = getSpecificPlayer(self.player)

    local obj = playerObj:getContextDoorOrWindowOrWindowFrame(dir)

    if instanceof(obj, "IsoWindow") then
        if obj:canClimbThrough(playerObj) and not playerObj:isIgnoreContextKey() then
            self:setBPrompt(getText("ContextMenu_Climb_through"), ISButtonPrompt.climbInWindow, obj)
            return
        else
            local barricade = obj:getBarricadeForCharacter(playerObj)
            if not obj:IsOpen() and not obj:isSmashed() and not barricade and not JoypadState.disableSmashWindow then
                self:setBPrompt(getText("ContextMenu_Smash_window"), ISButtonPrompt.smashWindow, obj)
                return
            end
        end
    end

    if instanceof(obj, "IsoThumpable") then
        if obj:isWindow() and obj:canClimbThrough(playerObj) and not playerObj:isIgnoreContextKey() then
            self:setBPrompt(getText("ContextMenu_Climb_through"), ISButtonPrompt.climbInWindow, obj)
            return
        elseif obj:isHoppable() and obj:canClimbOver(playerObj) and not playerObj:isIgnoreContextKey() then
            self:setBPrompt(getText("ContextMenu_Climb_through"), ISButtonPrompt.climbInWindow, obj)
            return
        end
    end

    if IsoWindowFrame.isWindowFrame(obj) then
        if IsoWindowFrame.canClimbThrough(obj, playerObj) and not playerObj:isIgnoreContextKey() then
            self:setBPrompt(getText("ContextMenu_Climb_through"), ISButtonPrompt.climbInWindow, obj)
            return
        end
    end

    if playerObj:hopFence(dir, true) and not playerObj:isIgnoreContextKey() and not JoypadState.disableClimbOver then
        self:setBPrompt(getText("ContextMenu_Climb_over"), ISButtonPrompt.climbFence);
        return
    end

    if playerObj:canClimbOverWall(dir) and not playerObj:isIgnoreContextKey() and not JoypadState.disableClimbOver then
        self:setBPrompt(getText("ContextMenu_Climb_over"), ISButtonPrompt.climbOverWall, dir)
        return
    end
end

function ISButtonPrompt:doBButtonDoorOrWindowOrWindowFrame(dir, obj)

    local playerObj = getSpecificPlayer(self.player)
    local square1 = playerObj:getCurrentSquare()
    local square2 = square1:getAdjacentSquare(dir)

    if instanceof(obj, "IsoWindow") and obj:canClimbThrough(playerObj) and not playerObj:isIgnoreContextKey() then
        self:setBPrompt(getText("ContextMenu_Climb_through"), ISButtonPrompt.climbInWindow, obj)
        return
    end

    if instanceof(obj, "IsoThumpable") and obj:isWindow() and obj:canClimbThrough(playerObj) and not playerObj:isIgnoreContextKey() then
        self:setBPrompt(getText("ContextMenu_Climb_through"), ISButtonPrompt.climbInWindow, obj)
        return
    end

    if IsoWindowFrame.isWindowFrame(obj) and IsoWindowFrame.canClimbThrough(obj, playerObj) and not playerObj:isIgnoreContextKey() then
        self:setBPrompt(getText("ContextMenu_Climb_through"), ISButtonPrompt.climbInWindow, obj)
        return
    end
end

function ISButtonPrompt:getBestYButtonAction(dir)

    if UIManager.getSpeedControls() and UIManager.getSpeedControls():getCurrentGameSpeed() == 0 then
        self:setYPrompt(nil, nil, nil);
        return;
    end

    if dir == nil then
        self:setYPrompt(nil, nil, nil);
    end

    if getCell():getDrag(self.player) then
        self:setYPrompt(getCell():getDrag(self.player):getYPrompt(), nil);
        return;
    end

    if JoypadState.players[self.player+1].disableYInventory then
        return
    end

    local playerObj = getSpecificPlayer(self.player)
    if playerObj:getVehicle() then
        self.isLoot = false
        self:setYPrompt(getText("IGUI_Controller_Inventory"), ISButtonPrompt.cmdShowInventory, nil)
        return
    end

    if ISButtonPrompt.test == nil then
        ISButtonPrompt.test = {}
        ISButtonPrompt.test.sqs = {}
    end

    local sqs = ISButtonPrompt.test.sqs;
    ISButtonPrompt.test.square = getSpecificPlayer(self.player):getCurrentSquare();
    local square=ISButtonPrompt.test.square;
    if square == nil then return; end
    local cx = square:getX();
    local cy = square:getY();
    local cz = square:getZ();

    if dir == nil then
        dir = getSpecificPlayer(self.player):getDir();
    end

    if(dir == IsoDirections.N) then         sqs[2] = getCell():getGridSquare(cx-1, cy-1, cz); sqs[3] = getCell():getGridSquare(cx, cy-1, cz); sqs[4] = getCell():getGridSquare(cx+1, cy-1, cz);
    elseif (dir == IsoDirections.NE) then   sqs[2] = getCell():getGridSquare(cx, cy-1, cz); sqs[3] = getCell():getGridSquare(cx+1, cy-1, cz); sqs[4] = getCell():getGridSquare(cx+1, cy, cz);
    elseif (dir == IsoDirections.E) then    sqs[2] = getCell():getGridSquare(cx+1, cy-1, cz); sqs[3] = getCell():getGridSquare(cx+1, cy, cz); sqs[4] = getCell():getGridSquare(cx+1, cy+1, cz);
    elseif (dir == IsoDirections.SE) then   sqs[2] = getCell():getGridSquare(cx+1, cy, cz); sqs[3] = getCell():getGridSquare(cx+1, cy+1, cz); sqs[4] = getCell():getGridSquare(cx, cy+1, cz);
    elseif (dir == IsoDirections.S) then    sqs[2] = getCell():getGridSquare(cx+1, cy+1, cz); sqs[3] = getCell():getGridSquare(cx, cy+1, cz); sqs[4] = getCell():getGridSquare(cx-1, cy+1, cz);
    elseif (dir == IsoDirections.SW) then   sqs[2] = getCell():getGridSquare(cx, cy+1, cz); sqs[3] = getCell():getGridSquare(cx-1, cy+1, cz); sqs[4] = getCell():getGridSquare(cx-1, cy, cz);
    elseif (dir == IsoDirections.W) then    sqs[2] = getCell():getGridSquare(cx-1, cy+1, cz); sqs[3] = getCell():getGridSquare(cx-1, cy, cz); sqs[4] = getCell():getGridSquare(cx-1, cy-1, cz);
    elseif (dir == IsoDirections.NW) then   sqs[2] = getCell():getGridSquare(cx-1, cy, cz); sqs[3] = getCell():getGridSquare(cx-1, cy-1, cz); sqs[4] = getCell():getGridSquare(cx, cy-1, cz);
    end

    if sqs[2] == nil then return; end

    sqs[1] = square;

    local loot = false;
    for x = 1, 4 do
        if loot then break; end
        local gs = sqs[x];

        -- stop grabbing thru walls...
        if gs ~= getSpecificPlayer(self.player):getCurrentSquare() and getSpecificPlayer(self.player):getCurrentSquare():isWallTo(gs) then
            gs = nil
        end

        if gs ~= nil then

            --for y = -1, 1 do
            local obs = gs:getObjects();
            local sobs =  gs:getStaticMovingObjects();
            local wobs = gs:getWorldObjects();

            if wobs ~= nil then
                if not wobs:isEmpty() then
                    loot = true
                    break
                end
                for i = 0, wobs:size()-1 do
                    local o = wobs:get(i);
                    if instanceof(o, "IsoWorldInventoryObject") then
                        loot = true;
                        break;
                    end
                end
            end

            for i = 0, sobs:size()-1 do
                local so = sobs:get(i);

                if so:getContainer() ~= nil then
                    loot = true;
                    break;

                end

            end
            for i = 0, obs:size()-1 do
                local o = obs:get(i);


                if o:getContainer() ~= nil then
                    loot = true;
                    break;

                end
             end
        end
    end

    if loot then
	    self.isLoot = true;
        self:setYPrompt(getText("IGUI_Controller_Loot"), ISButtonPrompt.cmdShowLoot, nil);

    else
		if not getSpecificPlayer(self.player):isAsleep() then
			self.isLoot = false;
			self:setYPrompt(getText("IGUI_Controller_Inventory"), ISButtonPrompt.cmdShowInventory, nil);
        end
    end
end


function ISButtonPrompt:getBestXButtonAction(dir)
    if UIManager.getSpeedControls() and UIManager.getSpeedControls():getCurrentGameSpeed() == 0 then
        self:setXPrompt(nil, nil, nil);
        return;
    end

    if dir == nil then
        self:setXPrompt(nil, nil, nil);
    end

    local drag = getCell():getDrag(self.player)
    if drag then
        if drag.getXPrompt then
            self:setXPrompt(drag:getXPrompt(), nil, nil);
        else
            self:setXPrompt(nil, nil, nil);
        end
        return;
    end

    local playerObj = getSpecificPlayer(self.player)
    local vehicle = playerObj:getVehicle()
    if vehicle then
        if vehicle:isDriver(playerObj) then
            self:setXPrompt(getText("IGUI_Controller_CruiseControl"), ISVehicleRegulator.onJoypadPressX, JoypadState.players[self.player+1])
        end
        return
    end

    if getSpecificPlayer(self.player):isAsleep() then return end

    -- The context-menu code is far too slow to be called every frame.
    -- So always display the 'interact' prompt, even if pressing X has no effect.
    self:setXPrompt(getText("IGUI_Controller_Interact"), ISButtonPrompt.interact, objects)
end

function ISButtonPrompt:getXButtonObjects(dir)

    if self.list == nil then
        self.list = LuaList:new();

    end
    local objects = self.list;

    objects:clear();

    if ISButtonPrompt.test == nil then
        ISButtonPrompt.test = {}
        ISButtonPrompt.test.sqs = {}
    end

    local sqs = ISButtonPrompt.test.sqs;
    ISButtonPrompt.test.square = getSpecificPlayer(self.player):getCurrentSquare();
    local square=ISButtonPrompt.test.square;
    if square == nil then return; end
    local cx = square:getX();
    local cy = square:getY();
    local cz = square:getZ();

    if dir == nil then
        dir = getSpecificPlayer(self.player):getDir();
    end

    if(dir == IsoDirections.N) then         sqs[2] = getCell():getGridSquare(cx-1, cy-1, cz); sqs[3] = getCell():getGridSquare(cx, cy-1, cz); sqs[4] = getCell():getGridSquare(cx+1, cy-1, cz);
    elseif (dir == IsoDirections.NE) then   sqs[2] = getCell():getGridSquare(cx, cy-1, cz); sqs[3] = getCell():getGridSquare(cx+1, cy-1, cz); sqs[4] = getCell():getGridSquare(cx+1, cy, cz);
    elseif (dir == IsoDirections.E) then    sqs[2] = getCell():getGridSquare(cx+1, cy-1, cz); sqs[3] = getCell():getGridSquare(cx+1, cy, cz); sqs[4] = getCell():getGridSquare(cx+1, cy+1, cz);
    elseif (dir == IsoDirections.SE) then   sqs[2] = getCell():getGridSquare(cx+1, cy, cz); sqs[3] = getCell():getGridSquare(cx+1, cy+1, cz); sqs[4] = getCell():getGridSquare(cx, cy+1, cz);
    elseif (dir == IsoDirections.S) then    sqs[2] = getCell():getGridSquare(cx+1, cy+1, cz); sqs[3] = getCell():getGridSquare(cx, cy+1, cz); sqs[4] = getCell():getGridSquare(cx-1, cy+1, cz);
    elseif (dir == IsoDirections.SW) then   sqs[2] = getCell():getGridSquare(cx, cy+1, cz); sqs[3] = getCell():getGridSquare(cx-1, cy+1, cz); sqs[4] = getCell():getGridSquare(cx-1, cy, cz);
    elseif (dir == IsoDirections.W) then    sqs[2] = getCell():getGridSquare(cx-1, cy+1, cz); sqs[3] = getCell():getGridSquare(cx-1, cy, cz); sqs[4] = getCell():getGridSquare(cx-1, cy-1, cz);
    elseif (dir == IsoDirections.NW) then   sqs[2] = getCell():getGridSquare(cx-1, cy, cz); sqs[3] = getCell():getGridSquare(cx-1, cy-1, cz); sqs[4] = getCell():getGridSquare(cx, cy-1, cz);
    end

    if sqs[2] == nil then return; end

    sqs[1] = square;

    for x = 1, 4 do

        local gs = sqs[x];

        -- stop grabbing thru walls...
        if gs ~= square and square:isWallTo(gs) and not square:isHoppableTo(gs) then
            gs = nil
        end

        if gs ~= nil then

            --for y = -1, 1 do
            local obs = gs:getObjects();

            for i = 0, obs:size()-1 do
                local o = obs:get(i);
                if instanceof(o, "IsoRaindrop") then

                elseif instanceof(o, "IsoRainSplash") then

                else
                    --print("Find "..tostring(o:getSprite():getName()))
                    objects:add(o);
                    break
                end
            end

            local obs = gs:getSpecialObjects();

            for i = 0, obs:size()-1 do
                local o = obs:get(i);
                    objects:add(o);
                    break
            end

            obs = gs:getMovingObjects()
            for i = 0, obs:size()-1 do
                local o = obs:get(i);
                -- Medical Check
                if instanceof(o, "IsoPlayer") and o ~= getSpecificPlayer(self.player) then
                    objects:add(o)
                end
            end
        end
    end

    --local hit = ISWorldObjectContextMenu.createMenu(self.player, objects.items,0, 0, true);
    local hit = ISContextManager.getInstance().createWorldMenu( self.player, nil, objects.items, 0, 0, true );

    if hit then
--        hit:setVisible(false);
--        print("MENU CREATED");
    end

    if not objects:isEmpty() then
--        print("OBJECT NOT EMPTY");
    end

    if not objects:isEmpty() and hit then
        return objects
    end
end

function ISButtonPrompt:getBestLBButtonAction(dir)
    if UIManager.getSpeedControls() and UIManager.getSpeedControls():getCurrentGameSpeed() == 0 then
        self:setLBPrompt(nil, nil, nil);
        return;
    end

    if getCell():getDrag(self.player) then
        self:setLBPrompt(getCell():getDrag(self.player):getLBPrompt(), nil, nil);
    elseif ISFirearmRadialMenu.getBestLBButtonAction(self) then
    else
        self:setLBPrompt(nil, nil, nil);
    end
end

function ISButtonPrompt:getBestRBButtonAction(dir)
    if UIManager.getSpeedControls() and UIManager.getSpeedControls():getCurrentGameSpeed() == 0 then
        self:setRBPrompt(nil, nil, nil);
        return;
    end

    if getCell():getDrag(self.player) then
        self:setRBPrompt(getCell():getDrag(self.player):getRBPrompt(), nil, nil);
    elseif ISFirearmRadialMenu.getBestRBButtonAction(self) then
    else
        self:setRBPrompt(nil, nil, nil);
    end
end

function ISButtonPrompt:onAPress()
    if self.aFunc then
        self:aFunc(self.aParams[1], self.aParams[2], self.aParams[3], self.aParams[4])
    end
end

function ISButtonPrompt:onBPress()
    if self.bFunc then
        self:bFunc(self.bParams[1], self.bParams[2], self.bParams[3], self.bParams[4])
    end
end

function ISButtonPrompt:onXPress()
    if self.xFunc then
        self:xFunc(self.xParams[1], self.xParams[2], self.xParams[3], self.xParams[4])
    end
end

function ISButtonPrompt:onYPress()
    if self.yFunc then
        self:yFunc(self.yParams[1], self.yParams[2], self.yParams[3], self.yParams[4])
    end
end

function ISButtonPrompt:onLBPress()
    if self.lbFunc then
        self:lbFunc(self.lbParams[1], self.lbParams[2], self.lbParams[3], self.lbParams[4])
    end
end

function ISButtonPrompt:onRBPress()
    if self.rbFunc then
        self:rbFunc(self.rbParams[1], self.rbParams[2], self.rbParams[3], self.rbParams[4])
    end
end

function ISButtonPrompt:setAPrompt(str, func, param1, param2, param3, param4)
    self.aPrompt = str;
    self.aFunc = func;
    self.aParams = {param1, param2, param3, param4}
end

function ISButtonPrompt:setBPrompt(str, func, param1, param2, param3, param4)
    self.bPrompt = str;
    self.bFunc = func;
    self.bParams = {param1, param2, param3, param4}
end

function ISButtonPrompt:setXPrompt(str, func, param1, param2, param3, param4)
    self.xPrompt = str;
    self.xFunc = func;
    self.xParams = {param1, param2, param3, param4}
end

function ISButtonPrompt:setYPrompt(str, func, param1, param2, param3, param4)
    self.yPrompt = str;
    self.yFunc = func;
    self.yParams = {param1, param2, param3, param4}
end

function ISButtonPrompt:setLBPrompt(str, func, param1, param2, param3, param4)
    self.lbPrompt = str;
    self.lbFunc = func;
    self.lbParams = {param1, param2, param3, param4}
end

function ISButtonPrompt:setRBPrompt(str, func, param1, param2, param3, param4)
    self.rbPrompt = str;
    self.rbFunc = func;
    self.rbParams = {param1, param2, param3, param4}
end

function ISButtonPrompt:onJoypadButtonReleased(button)
    ISFirearmRadialMenu.onJoypadButtonReleased(self, button)
end

--************************************************************************--
--** ISButtonPrompt:new
--**
--************************************************************************--
function ISButtonPrompt:new (player)
    local o = {}
    --o.data = {}
    o = ISUIElement:new(0, 0, 1, 1);
    setmetatable(o, self)
    self.__index = self
    o.x = 0;
    o.y = 0;
    o.background = true;
    o.backgroundColor = {r=0, g=0, b=0, a=0.0};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=0};
    o.width = 1;
    o.height = 1;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.player = player;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.buttonA =  Joypad.Texture.AButton;
    o.buttonB =  Joypad.Texture.BButton;
    o.buttonX =  Joypad.Texture.XButton;
    o.buttonY =  Joypad.Texture.YButton;
    o.buttonLB =  Joypad.Texture.LBumper;
    o.buttonRB =  Joypad.Texture.RBumper;

    o.movableIconPickup = getTexture("media/ui/Furniture_Pickup.png");
    o.movableIconPlace = getTexture("media/ui/Furniture_Place.png");
    o.movableIconRotate = getTexture("media/ui/Furniture_Rotate.png");
    o.movableIconScrap = getTexture("media/ui/Furniture_Disassemble.png");
 --   o:setAPrompt("Close window");
   -- o:setBPrompt("Climb through");
  --  o:setXPrompt("Interact");
  --  o:setYPrompt("Loot");
    return o
end

