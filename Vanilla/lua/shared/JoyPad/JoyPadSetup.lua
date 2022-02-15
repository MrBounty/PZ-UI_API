--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isServer() then return end

JoypadState = {}
JoypadState.controllers = {}
JoypadState.players = {}
JoypadState.joypads = {}
JoypadState.forceActivate = nil;

Joypad = {}
Joypad.AButton = 0;
Joypad.BButton = 1;
Joypad.XButton = 2;
Joypad.YButton = 3;
Joypad.LBumper = 4;
Joypad.RBumper = 5;
Joypad.Back = 6
Joypad.Start = 7
Joypad.Other = 8

Joypad.DPadLeft = 100
Joypad.DPadRight = 101
Joypad.DPadUp = 102
Joypad.DPadDown = 103

Joypad.Texture = {}
Joypad.Texture.AButton = getTexture("media/ui/xbox/XBOX_A.png")
Joypad.Texture.BButton = getTexture("media/ui/xbox/XBOX_B.png")
Joypad.Texture.XButton = getTexture("media/ui/xbox/XBOX_X.png")
Joypad.Texture.YButton = getTexture("media/ui/xbox/XBOX_Y.png")
Joypad.Texture.LBumper = getTexture("media/ui/xbox/xbox_lb.png")
Joypad.Texture.RBumper = getTexture("media/ui/xbox/xbox_rb.png")
Joypad.Texture.DPadLeft = getTexture("media/ui/xbox/XBOX_dpad_left.png")
Joypad.Texture.DPadRight = getTexture("media/ui/xbox/XBOX_dpad_right.png")
Joypad.Texture.DPadUp = getTexture("media/ui/xbox/XBOX_dpad_up.png")
Joypad.Texture.DPadDown = getTexture("media/ui/xbox/XBOX_dpad_down.png")
Joypad.Texture.LTrigger = getTexture("media/ui/xbox/xbox_lefttrigger.png")
Joypad.Texture.RTrigger = getTexture("media/ui/xbox/xbox_righttrigger.png")

joypad = {}
joypad.wantNoise = getDebug()

local noise = function(fmt, arg1, arg2, arg3, arg4)
    if not joypad.wantNoise then return end
    local msg = string.format(fmt, arg1, arg2, arg3, arg4)
    print('joypad: '..tostring(msg))
end

local function uiToString(ui)
    return ui and ui:toString() or "nil"
end

-----

JoypadControllerData = ISBaseObject:derive("JoypadControllerData")

function JoypadControllerData:setJoypad(joypadData)
    if joypadData.controller then
        joypadData.controller:clearJoypad()
    end
    self.joypad = joypadData
    joypadData.id = self.id
    joypadData.controller = self
end

function JoypadControllerData:clearJoypad()
    local joypad = self.joypad
    self.joypad = nil
    if joypad then
        joypad:clearController()
    end
end

function JoypadControllerData:new(id)
    local o = ISBaseObject.new(self)
    o.id = id
    o.pressed = {}
    o.wasPressed = {}
    for n = 1,getButtonCount(id) do
        o.pressed[n-1] = isJoypadPressed(id, n-1)
    end
    o.connected = isJoypadConnected(id)
    o.joypad = nil
    return o
end

-----

JoypadData = ISBaseObject:derive("JoypadData")

function JoypadData:setController(controller)
    if controller.joypad then
        controller.joypad:clearController()
    end
    self.id = controller.id
    self.controller = controller
    controller.joypad = self
end

function JoypadData:clearController()
    local controller = self.controller
    self.id = -1
    self.controller = nil
    if controller then
        controller:clearJoypad()
    end
end

function JoypadData:setActive(isActive)
    self.isActive = isActive
end

function JoypadData:new()
    local o = ISBaseObject.new(self)
    o.id = -1 -- same as controller.id
    o.player = nil
    o.controller = nil
    o.focus = nil
    o.lastfocus = nil
    o.prevfocus = nil
    o.prevprevfocus = nil
    o.isActive = false
    o.inMainMenu = false
    o.listBox = nil
    return o
end

-----

-- GLFW supports 16 controllers.
for i=1,16 do
    JoypadState.controllers[i-1] = JoypadControllerData:new(i-1)
end

for i=1,getMaxActivePlayers() do
    JoypadState.joypads[i] = JoypadData:new()
end

-----

function getFocusForPlayer(playerNum)
    local joypadData = JoypadState.players[playerNum+1]
    return joypadData and joypadData.focus or nil;
end

function JoypadControllerData:onPauseButtonPressed()
    local joypadData = self.joypad
    if UIManager.getSpeedControls() and not isClient() then
        if UIManager.getSpeedControls():getCurrentGameSpeed() == 0 or getGameTime():getTrueMultiplier() > 1 then
            if MainScreen.instance and MainScreen.instance.inGame and MainScreen.instance:isReallyVisible() then
                -- return to game below
            elseif joypadData.pauseKeyTime and (joypadData.pauseKeyTime + 750 > getTimestampMs()) then
                -- double-tap, go to main menu below
            else
                UIManager.getSpeedControls():ButtonClicked("Play")
                return
            end
        else
            joypadData.pauseKeyTime = getTimestampMs()
            UIManager.getSpeedControls():ButtonClicked("Pause")
            return
        end
    end
    if MainScreen.instance and MainScreen.instance.inGame then
        ISUIHandler.setVisibleAllUI(MainScreen.instance:isVisible())
        if MainScreen.instance:isVisible() then
            MainScreen.instance:setVisible(false)
            MainScreen.instance:removeFromUIManager()
        else
            MainScreen.instance:setVisible(true)
            MainScreen.instance:addToUIManager()
        end
        if MainScreen.instance:isVisible() then
            getCell():setDrag(nil, 0)
            setGameSpeed(0)
            setShowPausedMessage(false)
            JoypadState.saveAllFocus()
            joypadData.focus = MainScreen.instance
            joypadData.inMainMenu = true
            MainScreen.instance:onEnterFromGame()
        else
            MainScreen.instance:onReturnToGame()
            setGameSpeed(1);
            setShowPausedMessage(true)
            joypadData.inMainMenu = false
            JoypadState.restoreAllFocus()
        end
    end
end

function JoypadControllerData:onPressButtonNoFocus(button)
    local joypadData = self.joypad

    local activeWhilePaused = joypadData.activeWhilePaused

    local displayListBox = button == Joypad.AButton and not joypadData.player and not IsoPlayer.allPlayersDead()
    if displayListBox then
        activeWhilePaused = true
    end
    
    if not activeWhilePaused and isGamePaused() and button ~= Joypad.Start and button ~= Joypad.Back then
        return
    end

    -----
    -- Case 1: In the main menu.
    -----

    if MainScreen.instance and MainScreen.instance:isReallyVisible() then
        -- Activating a controller in the main menu does not display the JoypadListBox.
        -- Also, the controller is not assigned to any player.
        if button == Joypad.AButton then
            local focus = MainScreen.instance:getCurrentFocusForController()
            if focus == nil then return end
            joypadData.inMainMenu = true
            joypadData.focus = focus
            updateJoypadFocus(joypadData)
            return
        end
        return
    end

    -----
    -- Case 2: In game.
    -----

    if joypadData.player and getCell() and getCell():getDrag(joypadData.player) then
        getCell():getDrag(joypadData.player):onJoypadPressButton(joypadIndex, joypadData, button);
        return;
    end

    if displayListBox then
        local playerNum
        if joypadData == JoypadState.joypads[1] then
            playerNum = 0
        elseif joypadData == JoypadState.joypads[2] then
            playerNum = 1
        elseif joypadData == JoypadState.joypads[3] then
            playerNum = 2
        else
            playerNum = 3
        end
        joypadData.listBox = ISJoypadListBox.Create(playerNum, joypadData)
        joypadData.listBox:fill()
        joypadData.listBox:setVisible(true)
        joypadData.listBox:addToUIManager()
        joypadData.focus = joypadData.listBox
        joypadData.activeWhilePaused = true
        return
    end

    if button == Joypad.Back and joypadData.player and getSpecificPlayer(joypadData.player) then
        local wheel = getPlayerBackButtonWheel(joypadData.player)
        wheel:addCommands()
        wheel:addToUIManager(true)
        wheel:setVisible(true)
        setJoypadFocus(joypadData.player, wheel)
        getSpecificPlayer(joypadData.player):setJoypadIgnoreAimUntilCentered(true)
        return
    end

    if button == Joypad.Start and joypadData.player and getSpecificPlayer(joypadData.player) then
        self:onPauseButtonPressed()
        return
    end

    if joypadData.player and getPlayerData(joypadData.player) then
        local buts = getButtonPrompts(joypadData.player)
        if button == Joypad.AButton then
            buts:onAPress()
        end
        if button == Joypad.BButton then
            buts:onBPress()
        end
        if button == Joypad.XButton then
            buts:onXPress()
        end
        if button == Joypad.YButton then
            buts:onYPress()
        end
        if button == Joypad.LBumper then
            buts:onLBPress()
        end
        if button == Joypad.RBumper then
            buts:onRBPress()
        end
    end
end

function JoypadControllerData:onPressButton(button)
    local joypadData = self.joypad

    if not joypadData then return end

    if not joypadData.focus then
        self:onPressButtonNoFocus(button)
        return
    end
    if MainScreen.instance and MainScreen.instance.inGame and MainScreen.instance:isReallyVisible() then
        if button == Joypad.Start and joypadData.focus == MainScreen.instance then
            self:onPauseButtonPressed()
        else
            joypadData.focus:onJoypadDown(button, joypadData)
        end
        return
    end

    if not joypadData.activeWhilePaused and isGamePaused() then
        return;
    end

    joypadData.focus:onJoypadDown(button, joypadData);
end

function JoypadControllerData:onReleaseButton(button)
    local joypadData = self.joypad

    -- This controller isn't assigned to any player or the main menu.
    if not joypadData then
        return
    end

    if not joypadData.player then
        return
    end

    if getPlayerData(joypadData.player) then
        local buts = getButtonPrompts(joypadData.player)
        buts:onJoypadButtonReleased(button)
        local wheel = getPlayerBackButtonWheel(joypadData.player)
        if joypadData.focus == wheel then
            wheel:onJoypadButtonReleased(button, joypadData)
            return
        end
        wheel = getPlayerRadialMenu(joypadData.player)
        if joypadData.focus == wheel then
            wheel:onJoypadButtonReleased(button, joypadData)
            return
        end
    end
    if button == Joypad.XButton then
        ISVehicleRegulator.onJoypadReleaseX(joypadData)
    end
end

function getJoypadFocus(playerID)
	local joypadData = JoypadState.players[playerID+1];
	return joypadData and joypadData.focus or nil;
end

function setJoypadFocus(playerID, control)
    local joypadData = JoypadState.players[playerID+1];
    if not joypadData then return end

    noise("set focus to %s for player %d", uiToString(control), playerID)

    if control ~= nil and control ~= joypadData.focus then
        noise("focus changed - bumping down prevs");
        noise("current: %s", uiToString(joypadData.focus));
        noise("prev: %s", uiToString(joypadData.prevfocus));
        joypadData.prevprevfocus = joypadData.prevfocus;
        joypadData.prevfocus = joypadData.focus;
    end
    if control then
        noise("new: %s", uiToString(control));
    end
    joypadData.focus = control;

--  updateJoypadFocus(joypadData);
end

function setPrevFocusForPlayer(playerID)
    local joypadData = JoypadState.players[playerID+1];
    if not joypadData then return end
    noise("set focus to previous for player %d", playerID);
    noise("current: %s", uiToString(joypadData.focus));
    noise("prev: %s", uiToString(joypadData.prevfocus));
    joypadData.focus = joypadData.prevfocus;
    joypadData.prevfocus = joypadData.prevprevfocus;
    joypadData.prevprevfocus = nil;
    -- joypadData.lastfocus = nil;

    --  updateJoypadFocus(joypadData);
end


function setPrevPrevFocusForPlayer(playerID)
    local joypadData = JoypadState.players[playerID+1];
    if not joypadData then return end
    noise("set focus to previous-previous for player %d", playerID);
    noise("current: %s", uiToString(joypadData.focus));
    noise("prev: %s", uiToString(joypadData.prevfocus));
    joypadData.focus = joypadData.prevprevfocus;
    joypadData.prevfocus = nil;
    joypadData.prevprevfocus = nil;
    -- joypadData.lastfocus = nil;

    --  updateJoypadFocus(joypadData);
end

function updateJoypadFocus(joypadData)

    if joypadData.lastfocus ~= joypadData.focus then
        noise("change focus from %s to %s",
            uiToString(joypadData.lastfocus),
            uiToString(joypadData.focus))
        local lastfocus = joypadData.lastfocus
        joypadData.lastfocus = nil
        if joypadData.focus ~= nil then
            noise("focus in %s", uiToString(joypadData.focus))
            joypadData.focus:onGainJoypadFocus(joypadData);
        end
        if lastfocus ~= nil then
            noise("focus out %s", uiToString(lastfocus))
            lastfocus:onLoseJoypadFocus(joypadData);
        end
    end

    if joypadData.player ~= nil and getSpecificPlayer(joypadData.player) then
        if joypadData.focus ~= nil then
            joypadData.lastactualfocus = joypadData.focus;
            setPlayerMovementActive(joypadData.player, false);
        else
            setPlayerMovementActive(joypadData.player, true);
        end
        joypadData.lastfocus = joypadData.focus;

        if joypadData.focus == nil and getPlayerData(joypadData.player) then
    
            if JoypadState.disableControllerPrompt then
                joypadData.lastfocus = joypadData.focus;
                return;
            end
            
            local buts = getButtonPrompts(joypadData.player);

            if buts ~= nil then
                buts:getBestAButtonAction(nil);
                buts:getBestBButtonAction(nil);
                buts:getBestYButtonAction(nil);
                buts:getBestXButtonAction(nil);
                buts:getBestLBButtonAction(nil);
                buts:getBestRBButtonAction(nil);
            end
        end
    else
        joypadData.lastfocus = joypadData.focus;
    end
    
    if JoypadState.disableMovement then
        setPlayerMovementActive(joypadData.player, false);
    end
end

function JoypadControllerData:onPressUp()
    if not self.joypad then
        return
    end
    local joypadData = self.joypad
    if joypadData.focus ~= nil then
        if joypadData.focus:isVisible() then
            joypadData.focus:onJoypadDirUp(joypadData);
        end
        return
    end
    if not joypadData.player then
        return
    end
    if getCell():getDrag(joypadData.player) then
        getCell():getDrag(joypadData.player):onJoypadDirUp(joypadData);
    elseif ISVehicleRegulator.onJoypadPressUp(joypadData) then
        -- increase regulator speed
    else
        ISDPadWheels.onDisplayUp(joypadData)
--        local playerObj = getSpecificPlayer(joypadData.player)
--        ItemBindingHandler.equipBestWeapon(playerObj, "Firearm")
    end
end

function JoypadControllerData:onReleaseUp()
    if not self.joypad then
        return
    end
    local joypadData = self.joypad
    if not joypadData.player then
        return
    end
    local wheel = getPlayerRadialMenu(joypadData.player)
    if joypadData.focus == wheel and wheel:isVisible() then
        wheel:onJoypadButtonReleased(Joypad.DPadUp, joypadData)
    end
end

function JoypadControllerData:onPressDown()
    if not self.joypad then
        return
    end
    local joypadData = self.joypad
    if joypadData.focus ~= nil then
        if joypadData.focus:isVisible() then
            joypadData.focus:onJoypadDirDown(joypadData);
        end
        return
    end
    if not joypadData.player then
        return
    end
    if getCell():getDrag(joypadData.player) then
        getCell():getDrag(joypadData.player):onJoypadDirDown(joypadData);
    elseif ISVehicleRegulator.onJoypadPressDown(joypadData) then
        -- decrease regulator speed
    else
        ISDPadWheels.onDisplayDown(joypadData)
    end
end

function JoypadControllerData:onReleaseDown()
    if not self.joypad then
        return
    end
    local joypadData = self.joypad
    if not joypadData.player then
        return
    end
    local wheel = getPlayerRadialMenu(joypadData.player)
    if joypadData.focus == wheel and wheel:isVisible() then
        wheel:onJoypadButtonReleased(Joypad.DPadDown, joypadData)
    end
end

function JoypadControllerData:onPressLeft()
    if not self.joypad then
        return
    end
    local joypadData = self.joypad
    if joypadData.focus ~= nil then
        if joypadData.focus:isVisible() then
            joypadData.focus:onJoypadDirLeft(joypadData);
        end
        return
    end
    if not joypadData.player then
        return
    end
    if getCell():getDrag(joypadData.player) then
        getCell():getDrag(joypadData.player):onJoypadDirLeft(joypadData);
    else
        ISDPadWheels.onDisplayLeft(joypadData)
--        local playerObj = getSpecificPlayer(joypadData.player)
--        ItemBindingHandler.equipBestWeapon(playerObj, "Swinging")
    end
end

function JoypadControllerData:onReleaseLeft()
    if not self.joypad then
        return
    end
    local joypadData = self.joypad
    if not joypadData.player then
        return
    end
    local wheel = getPlayerRadialMenu(joypadData.player)
    if joypadData.focus == wheel and wheel:isVisible() then
        wheel:onJoypadButtonReleased(Joypad.DPadLeft, joypadData)
    end
end

function JoypadControllerData:onPressRight()
    if not self.joypad then
        return
    end
    local joypadData = self.joypad
    if joypadData.focus ~= nil then
        if joypadData.focus:isVisible() then
            joypadData.focus:onJoypadDirRight(joypadData);
        end
        return
    end
    if not joypadData.player then
        return
    end
    if getCell():getDrag(joypadData.player) then
        getCell():getDrag(joypadData.player):onJoypadDirRight(joypadData);
    else
        ISDPadWheels.onDisplayRight(joypadData)
--        local playerObj = getSpecificPlayer(joypadData.player)
--        ItemBindingHandler.equipBestWeapon(playerObj, "Stab")
    end
end

function JoypadControllerData:onReleaseRight()
    if not self.joypad then
        return
    end
    local joypadData = self.joypad
    if not joypadData.player then
        return
    end
    local wheel = getPlayerRadialMenu(joypadData.player)
    if joypadData.focus == wheel and wheel:isVisible() then
        wheel:onJoypadButtonReleased(Joypad.DPadRight, joypadData)
    end
end

local function translateButton(joypad, button)
    if button == getJoypadAButton(joypad) then return Joypad.AButton end
    if button == getJoypadBButton(joypad) then return Joypad.BButton end
    if button == getJoypadXButton(joypad) then return Joypad.XButton end
    if button == getJoypadYButton(joypad) then return Joypad.YButton end
    if button == getJoypadLBumper(joypad) then return Joypad.LBumper end
    if button == getJoypadRBumper(joypad) then return Joypad.RBumper end
    if button == getJoypadBackButton(joypad) then return Joypad.Back end
    if button == getJoypadStartButton(joypad) then return Joypad.Start end
    return Joypad.Other
end

function JoypadControllerData:update(time)
    if not self.connected then
        return
    end

    local i = self.id
    local v = self
    local t = time
    
    if isJoypadDown(i) then
        if not v.down then	v.timedown = t; v.timedownproc = 0 end
        v.down = true
        v.dtdown = t - v.timedown
        v.dtprocdown = t - v.timedownproc
    else 
        v.timedown = 0
        v.down = false
    end

    if isJoypadUp(i) then
        if not v.up then	v.timeup = t; v.timeupproc = 0 end
        v.up = true
        v.dtup = t - v.timeup
        v.dtprocup = t - v.timeupproc
    else 
        v.timeup = 0
        v.up = false
    end

    if isJoypadLeft(i) then
        if not v.left then
            v.timeleft = t
            v.timeleftproc = 0
        end
        v.left = true
        v.dtleft = t - v.timeleft
        v.dtprocleft = t - v.timeleftproc
    else 
        v.timeleft = 0
        v.left = false
    end

    if isJoypadRight(i) then
        if not v.right then
            v.timeright = t
            v.timerightproc = 0
        end
        v.right = true
        v.dtright = t - v.timeright
        v.dtprocright = t - v.timerightproc
    else 
        v.timeright = 0
        v.right = false
    end
    
    --print("DEBUG: v.down="..tostring(v.down).." v.dtdown="..tostring(v.dtdown).." v.timedown="..tostring(v.timedown).." v.dtprocdown="..tostring(v.dtprocdown))
    --print("DEBUG: v.up="..tostring(v.up).." v.dtup="..tostring(v.dtup).." v.timeup="..tostring(v.timeup).." v.dtprocup="..tostring(v.dtprocup))

    if v.down and v.dtprocdown>300 then
        v:onPressDown();
        v.timedownproc = t
    elseif v.down and v.dtdown>900 and v.dtprocdown>110 then
        v:onPressDown();
        v.timedownproc = t
    elseif v.down and v.dtdown>3000 and v.dtprocdown>50 then
        v:onPressDown();
        v.timedownproc = t
    elseif not v.down and v.timedownproc ~= 0 then
        v:onReleaseDown()
        v.timedownproc = 0
    end

    if v.up and v.dtprocup>300 then
        v:onPressUp();
        v.timeupproc = t
    elseif v.up and v.dtup>900 and v.dtprocup>110 then
        v:onPressUp();
        v.timeupproc = t
    elseif v.up and v.dtup>3000 and v.dtprocup>50 then
        v:onPressUp();
        v.timeupproc = t
    elseif not v.up and v.timeupproc ~= 0 then
        v:onReleaseUp()
        v.timeupproc = 0
    end

    if v.left and v.dtprocleft > 300 then
        v:onPressLeft()
        v.timeleftproc = t
    elseif v.left and v.dtleft > 900 and v.dtprocleft > 110 then
        v:onPressLeft()
        v.timeleftproc = t
    elseif v.left and v.dtleft > 3000 and v.dtprocleft > 50 then
        v:onPressLeft()
        v.timeleftproc = t
    elseif not v.left and v.timeleftproc ~= 0 then
        v:onReleaseLeft()
        v.timeleftproc = 0
    end

    if v.right and v.dtprocright > 300 then
        v:onPressRight()
        v.timerightproc = t
    elseif v.right and v.dtright > 900 and v.dtprocright > 110 then
        v:onPressRight()
        v.timerightproc = t
    elseif v.right and v.dtright > 3000 and v.dtprocright > 50 then
        v:onPressRight()
        v.timerightproc = t
    elseif not v.right and v.timerightproc ~= 0 then
        v:onReleaseRight()
        v.timerightproc = 0
    end

    for n = 0,getButtonCount(i)-1 do
        if v.pressed[n] == nil then v.pressed[n] = true; end
        v.wasPressed[n] = v.pressed[n];
        v.pressed[n] = isJoypadPressed(i, n);
        if v.pressed[n] and not v.wasPressed[n] then
            local button = translateButton(v.id, n)
            v:onPressButton(button)
        elseif v.wasPressed[n] and not v.pressed[n] then
            local button = translateButton(v.id, n)
            v:onReleaseButton(button)
        end
    end

    if v.joypad then
        updateJoypadFocus(v.joypad)
    end
end

function onJoypadRenderTick(ticks)
    if JoypadState.controllerTest then return end
    local t = getTimestampMs()
    for i=1,16 do
        local controller = JoypadState.controllers[i-1]
        controller:update(t)
    end
end

function JoypadState.onGamepadConnect(id)
    JoypadState.controllers[id].connected = true
end

function JoypadState.onGamepadDisconnect(id)
    JoypadState.controllers[id].connected = false
end

function onJoypadActivate(id)
    if JoypadState.controllerTest then return end
--    if getCore():getGameMode() == "Tutorial" then return end
    local controller = JoypadState.controllers[id]
    if controller.joypad ~= nil then return end

    noise("activate %d", id)
    
    if MainScreen.instance and MainScreenInstance.inGame then
        local numPlayers = getNumActivePlayers() + 1
        local maxPlayer = math.min(numPlayers, getMaxActivePlayers())
        for i=1,maxPlayer do
            if not JoypadState.players[i] then
                joypadData = JoypadState.joypads[i]
                break
            end
        end
        if not joypadData then
            -- All player slots have controllers.
            return
        end
    else
        joypadData = JoypadState.joypads[1]
    end

    joypadData:setActive(true)
    controller:setJoypad(joypadData)
end

function onJoypadActivateUI(id)
    onJoypadActivate(id)
end

function onJoypadBeforeDeactivate(id)
    local joypadData = JoypadState.controllers[id].joypad
    if joypadData == nil then
        return
    end
    if joypadData.focus and joypadData.focus.onJoypadBeforeDeactivate then
        joypadData.focus:onJoypadBeforeDeactivate(joypadData)
    end
end

function onJoypadDeactivate(id)
    local controller = JoypadState.controllers[id]
    local joypadData = controller.joypad
    if joypadData == nil then
        return
    end
    joypadData:setActive(false)
    if joypadData.inMainMenu and joypadData.focus ~= nil and MainScreen.instance and MainScreen.instance:isReallyVisible() then
        joypadData.focus:onLoseJoypadFocus(joypadData)
        joypadData.focus = nil
        joypadData.lastfocus = nil
    end
    if joypadData.listBox then
        if joypadData.focus == joypadData.listBox then
            joypadData.focus = nil
            joypadData.lastfocus = nil
        end
        joypadData.listBox:removeFromUIManager()
        joypadData.listBox = nil
    end
    if joypadData.player == nil then
        return
    end
    local ui = ISJoypadDisconnectedUI:new(joypadData.player)
    ui:setAlwaysOnTop(true)
    ui:addToUIManager()
    joypadData.disconnectedUI = ui
end

function onJoypadBeforeReactivate(id)
    local joypadData = JoypadState.controllers[id].joypad
    if joypadData == nil then
        return
    end
    if joypadData.focus and joypadData.focus.onJoypadBeforeReactivate then
        joypadData.focus:onJoypadBeforeReactivate(joypadData)
    end
end

function onJoypadReactivate(id)
    local controller = JoypadState.controllers[id]
    local joypadData = controller.joypad
    if joypadData == nil then return end
    joypadData:setActive(true)
    if joypadData.disconnectedUI then
        joypadData.disconnectedUI:removeFromUIManager()
        joypadData.disconnectedUI = nil
    end
    if joypadData.focus and joypadData.focus.onJoypadReactivate then
        joypadData.focus:onJoypadReactivate(joypadData)
    end
    if joypadData.inMainMenu then
        if MainScreen.instance and MainScreen.instance:isReallyVisible() then
            local focus = MainScreen.instance:getCurrentFocusForController()
            if focus == nil then return end
            joypadData.focus = focus
            updateJoypadFocus(joypadData)
            return
        end
    end
end

-- Player 0 controller was disconnected, and they chose to use keyboard and mouse.
function JoypadState.useKeyboardMouse()
    local playerNum = 0
    local joypadData = JoypadState.players[playerNum+1]
    JoypadState.players[playerNum+1] = nil
    joypadData.player = nil
    if joypadData.focus ~= nil then
        joypadData.focus:onLoseJoypadFocus(joypadData)
        joypadData.focus = nil
    end
    local playerObj = getSpecificPlayer(playerNum)
    if playerObj then
        -- See inventory handling in JoypadControllerData:onPressButton().
        playerObj:setBannedAttacking(false)
    end
    if joypadData.listBox then
        joypadData.listBox:removeFromUIManager()
        joypadData.listBox = nil
    end
    revertToKeyboardAndMouse()
end

function JoypadState.getMainMenuJoypad()
    for _,joypadData in ipairs(JoypadState.joypads) do
        if joypadData.inMainMenu then
            return joypadData
        end
    end
    return nil
end

function JoypadState.saveAllFocus()
    JoypadState.saveFocus = {}
    for i,joypadData in pairs(JoypadState.joypads) do
        JoypadState.saveFocus[i] = joypadData.focus
    end
end

function JoypadState.restoreAllFocus()
    if not JoypadState.saveFocus then return end
    for i,joypadData in pairs(JoypadState.joypads) do
        if JoypadState.saveFocus[i] and JoypadState.saveFocus[i]:isVisible() then
            joypadData.focus = JoypadState.saveFocus[i]
        else
            joypadData.focus = nil
        end
    end
    table.wipe(JoypadState.saveFocus)
end

function JoypadState.onPlayerDeath(playerObj)
    local playerNum = playerObj:getPlayerNum()
    local joypadData = JoypadState.players[playerNum+1]
    if joypadData then
        noise('removing joypad player %d', playerNum)
        joypadData.player = nil
        joypadData.focus = nil
        joypadData.lastfocus = nil
        joypadData.prevfocus = nil
        joypadData.prevprevfocus = nil
    end
end

function JoypadState.onCoopJoinFailed(playerNum)
    local joypadData = JoypadState.players[playerNum+1]
    if joypadData then
        joypadData.focus = nil
        if joypadData.player then error "joypadData.player ~= nil" end
    end
end

JoypadState.onGameStart = function()
    local playerNum = 0
    local joypadData = JoypadState.joypads[playerNum+1]
    local controller = joypadData.controller
    if controller then
        noise("force activate")
        joypadData.inMainMenu = false
        joypadData.focus = nil
        joypadData.lastfocus = nil
        joypadData.prevfocus = nil
        joypadData.prevprevfocus = nil
        joypadData.player = playerNum
        JoypadState.players[playerNum+1] = joypadData
        local playerObj = getSpecificPlayer(playerNum)
        setPlayerJoypad(playerNum, joypadData.id, playerObj, nil)
        createPlayerData(playerNum)
        -- FIXME: obsolete?
        getPlayerInventory(playerNum):setController(joypadData.id)
        getPlayerLoot(playerNum):setController(joypadData.id)
--[[
        -- Display JoypadListBox.
        controller:onPressButton(Joypad.AButton)
        updateJoypadFocus(joypadData)
        -- Take over player 1.
        controller:onPressButton(Joypad.AButton)
--]]
    end
end

function JoypadState.onRenderUI()
    if JoypadState.debugUI == nil then
        JoypadState.debugUI = ISJoypadDebugUI:new()
        JoypadState.debugUI:initialise()
        JoypadState.debugUI:instantiate()
    end
    JoypadState.debugUI:render()
end

Events.OnGamepadConnect.Add(JoypadState.onGamepadConnect)
Events.OnGamepadDisconnect.Add(JoypadState.onGamepadDisconnect)
Events.OnJoypadActivate.Add(onJoypadActivate);
Events.OnJoypadActivateUI.Add(onJoypadActivateUI);
Events.OnJoypadBeforeDeactivate.Add(onJoypadBeforeDeactivate);
Events.OnJoypadDeactivate.Add(onJoypadDeactivate);
Events.OnJoypadBeforeReactivate.Add(onJoypadBeforeReactivate);
Events.OnJoypadReactivate.Add(onJoypadReactivate);
Events.OnRenderTick.Add(onJoypadRenderTick);
Events.OnGameStart.Add(JoypadState.onGameStart);
--Events.OnPlayerDeath.Add(JoypadState.onPlayerDeath);
Events.OnCoopJoinFailed.Add(JoypadState.onCoopJoinFailed)
Events.OnJoypadRenderUI.Add(JoypadState.onRenderUI)
