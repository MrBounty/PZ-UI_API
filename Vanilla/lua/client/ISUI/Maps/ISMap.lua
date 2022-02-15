--***********************************************************
--**              	  ROBERT JOHNSON                       **
--***********************************************************

require "ISUI/ISPanelJoypad"

ISMap = ISPanelJoypad:derive("ISMap");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_HANDWRITTEN = getTextManager():getFontHeight(UIFont.Handwritten)
ISMapWrapper = ISCollapsableWindow:derive("ISMapWrapper");
ISMapWrapper.__index = ISMapWrapper;

-- It appears the original loot-map images were created starting with PNG files
-- created by CartoZed or WorldEd scaled down by this factor.  So one PNG pixel
-- equals this many game-world squares.
ISMap.SCALE = 0.666

function ISMapWrapper:new(x, y, width, height)
	local o = ISCollapsableWindow.new(self, x, y, width, height)
	return o
end

function ISMap:instantiate()
	self.javaObject = UIWorldMap.new(self)
	self.mapAPI = self.javaObject:getAPIv1()
	self.javaObject:setX(self.x)
	self.javaObject:setY(self.y)
	self.javaObject:setWidth(self.width)
	self.javaObject:setHeight(self.height)
	self.javaObject:setAnchorLeft(self.anchorLeft)
	self.javaObject:setAnchorRight(self.anchorRight)
	self.javaObject:setAnchorTop(self.anchorTop)
	self.javaObject:setAnchorBottom(self.anchorBottom)
	self.mapAPI:setBoolean("Isometric", false)
	self.mapAPI:setMapItem(self.mapObj)
	self:createChildren()
end

function ISMap:createChildren()
    self.symbolsUI = ISWorldMapSymbols:new(self.width - 10 - 200, 10, 200, 200, self)
    self:addChild(self.symbolsUI)
    self.symbolsUI:setVisible(false)

    local buttonHgt = FONT_HGT_SMALL + 6
    local buttonPadBottom = 4
    local buttonY = self.height - buttonPadBottom - buttonHgt

    self.ok = ISButton:new(10, buttonY, 100, buttonHgt, getText("UI_Close"), self, ISMap.onButtonClick);
    self.ok.internal = "OK";
    self.ok:initialise();
    self.ok:instantiate();
    self.ok.borderColor = {r=1, g=1, b=1, a=0.4};
    self:addChild(self.ok);

    self.editSymbolsBtn = ISButton:new(self.ok:getRight() + 10, buttonY, 150, buttonHgt, getText("IGUI_Map_EditMarkings"), self, ISMap.onButtonClick);
    self.editSymbolsBtn.internal = "SYMBOLS";
    self.editSymbolsBtn:initialise();
    self.editSymbolsBtn:instantiate();
    self.editSymbolsBtn.borderColor = {r=1, g=1, b=1, a=0.4};
    self:addChild(self.editSymbolsBtn);

    self.scaleBtn = ISButton:new(self.editSymbolsBtn:getRight() + 10, buttonY, 50, buttonHgt, getText("IGUI_Map_Scale"), self, ISMap.onButtonClick);
    self.scaleBtn.internal = "SCALE";
    self.scaleBtn:initialise();
    self.scaleBtn:instantiate();
    self.scaleBtn.borderColor = {r=1, g=1, b=1, a=0.4};
    self:addChild(self.scaleBtn);

    -- Joypad only
    self.placeSymbBtn = ISButton:new(self.editSymbolsBtn:getRight() + 10, buttonY, 150, buttonHgt, getText("IGUI_Map_PlaceSymbol"), self, ISMap.onButtonClick);
    self.placeSymbBtn.internal = "PLACESYMBOL";
    self.placeSymbBtn:initialise();
    self.placeSymbBtn:instantiate();
    self.placeSymbBtn.borderColor = {r=1, g=1, b=1, a=0.4};
	self.placeSymbBtn:setVisible(false)
    self:addChild(self.placeSymbBtn);
end

function ISMap:destroy()
    self.symbolsUI:undisplay()
    self:setVisible(false);
    self:removeFromUIManager();
    if JoypadState.players[self.playerNum+1] then
        getSpecificPlayer(self.playerNum):setBannedAttacking(false)
        setJoypadFocus(self.playerNum, nil)
    end
end

function ISMapWrapper:isKeyConsumed(key)
    return key == Keyboard.KEY_ESCAPE;
end

function ISMapWrapper:onKeyPress(key)
    if self.mapUI.symbolsUI:onKeyPress(key) then
        return
    end
    return
end

function ISMapWrapper:onKeyRelease(key)
    if self.mapUI.symbolsUI:onKeyRelease(key) then
        return
    end
    if key == Keyboard.KEY_ESCAPE then
        self:close();
    end
    if key == Keyboard.KEY_S then
        LootMaps.callLua("Init", self.mapUI)
    end
end

function ISMap:onButtonClick(button)
    local player = self.character:getPlayerNum()
    if button.internal == "OK" then
        self.wrap:onKeyRelease(Keyboard.KEY_ESCAPE)
    end
    if button.internal == "SYMBOLS" then
        if JoypadState.players[player+1] then
            if self.symbolsUI:isVisible() then
                setJoypadFocus(player, self.symbolsUI)
            else
                self.symbolsUI:setVisible(true)
                setJoypadFocus(player, self.symbolsUI)
            end
            return
        end
        if self.symbolsUI:isVisible() then
            self.symbolsUI:undisplay()
            self.symbolsUI:setVisible(false)
        else
            self.symbolsUI:setVisible(true)
        end
    end
    if button.internal == "SCALE" then
		self.mapAPI:resetView()
    end
    if button.internal == "REMOVALL" then
		self.mapAPI:getSymbolsAPI():clear()
    end
    if button.internal == "PLACESYMBOL" then
        -- Joypad only
        self.symbolsUI:onJoypadDownInMap(Joypad.AButton, self.joyfocus)
    end
end

function ISMap:onMouseDown(x, y)
    if self.symbolsUI:onMouseDownMap(x, y) then
        return true
    end
    self.draggingStartingX = x
    self.draggingStartingY = y
    self.dragStartCX = self.mapAPI:getCenterWorldX()
    self.dragStartCY = self.mapAPI:getCenterWorldY()
    self.dragStartZoomF = self.mapAPI:getZoomF()
    self.dragStartWorldX = self.mapAPI:uiToWorldX(x, y)
    self.dragStartWorldY = self.mapAPI:uiToWorldY(x, y)
    self.dragging = true;
    self.dragMoved = false
end

function ISMapWrapper:setVisible(bVisible)
    if self.javaObject then
        self.mapUI.character:playSound(bVisible and "MapOpen" or "MapClose")
        self.javaObject:setVisible(bVisible)
        if not bVisible then
            self:removeFromUIManager()
            self.mapUI:destroy();
        end
    end
end

function ISMap:onMouseUp(x, y)
    if self.modal then
        self.modal:setVisible(false);
        self.modal:removeFromUIManager();
    end
    self.dragging = false;
    self.draggingStartingX = 0;
    self.draggingStartingY = 0;
    if self.symbolsUI:onMouseUpMap(x, y) then
        return true
    end
end

function ISMap:canWrite()
    local inv = self.character:getInventory();
    return inv:contains("Pen", true) or inv:contains("Pencil", true) or inv:contains("BluePen", true) or inv:contains("RedPen", true)
end

function ISMap:canErase()
    return self.character:getInventory():contains("Eraser", true);
end

function ISMap:onConfirmRemove(button, note)
    if button.internal == "YES" then
        self.mapAPI:getSymbolsAPI():removeSymbolByIndex(note)
        self.character:playSound("MapRemoveMarking")
    end
	self.getJoypadFocus = true;
end

function ISMap:onMouseUpOutside()
--    print("OnMouseUpOutside");
    self.dragging = false;
end

function ISMap:onMouseWheel(del)
    self.mapAPI:zoomAt(self:getMouseX(), self:getMouseY(), del)
    return true;
end

function ISMap:onMouseMove(dx, dy)
    if self.symbolsUI:onMouseMoveMap(dx, dy) then
        return true
    end
    if self.dragging then
        local mouseX = self:getMouseX()
        local mouseY = self:getMouseY()
        if not self.dragMoved and math.abs(mouseX - self.draggingStartingX) <= 4 and math.abs(mouseY - self.draggingStartingY) <= 4 then
            return
        end
        self.dragMoved = true
        local worldX = self.mapAPI:uiToWorldX(mouseX, mouseY, self.dragStartZoomF, self.dragStartCX, self.dragStartCY)
        local worldY = self.mapAPI:uiToWorldY(mouseX, mouseY, self.dragStartZoomF, self.dragStartCX, self.dragStartCY)
		self.mapAPI:centerOn(self.dragStartCX + self.dragStartWorldX - worldX, self.dragStartCY + self.dragStartWorldY - worldY)
    end
end

function ISMap:onMouseMoveOutside(dx, dy)
    if self.dragging then
        local mouseX = self:getMouseX()
        local mouseY = self:getMouseY()
        if not self.dragMoved and math.abs(mouseX - self.draggingStartingX) <= 4 and math.abs(mouseY - self.draggingStartingY) <= 4 then
            return
        end
        self.dragMoved = true
        local worldX = self.mapAPI:uiToWorldX(mouseX, mouseY, self.dragStartZoomF, self.dragStartCX, self.dragStartCY)
        local worldY = self.mapAPI:uiToWorldY(mouseX, mouseY, self.dragStartZoomF, self.dragStartCX, self.dragStartCY)
		self.mapAPI:centerOn(self.dragStartCX + self.dragStartWorldX - worldX, self.dragStartCY + self.dragStartWorldY - worldY)
    end
end

function ISMap:onRightMouseDown(x, y)
    if self.symbolsUI:onRightMouseDownMap(x, y) then
        return true
    end
    return false
end

function ISMap:prerender()
    if self.joyfocus then
        self:drawRect(0, self.ok:getY() - 4, self.placeSymbBtn:getRight() + 4, self.height - (self.ok:getY() - 4), 1.0, 0.5, 0.5, 0.5)
    end
end

function ISMap:render()
--    ISPanelJoypad.render(self);

    if not self.wrap.isCollapsed then
        local stencilBottom = self.ok:getY() - 4 -- don't draw symbols over the buttons

        self:setStencilRect(0, 0, self.width, stencilBottom)

        if (self.playerNum ~= 0) or (JoypadState.players[self.playerNum+1] ~= nil and not wasMouseActiveMoreRecentlyThanJoypad()) then
            self:drawTexture(self.cross, self.width/2-12, self.height/2-12, 1, 1,1,1);
        end

        self:clearStencilRect()
    end
end

function ISMapWrapper:prerender()
    ISCollapsableWindow.prerender(self);
end

function ISMapWrapper:render()
--    print("render col")
    ISCollapsableWindow.render(self);
    -- Moved this from update() so it is called more often
	self.mapUI:updateJoypad();
end

function ISMapWrapper:close()
	self:setVisible(false)
	self:removeFromUIManager()
    if JoypadState.players[self.mapUI.playerNum+1] then
        setJoypadFocus(self.mapUI.playerNum, nil)
    end
end

function ISMap:renderJoypadIcons()
    local didSymbol = false
    if (self.playerNum == 0) and (JoypadState.players[self.playerNum+1] == nil or wasMouseActiveMoreRecentlyThanJoypad()) then
        return didSymbol
    end
    return didSymbol
end

function ISMap:update()
    ISPanelJoypad.update(self);
    if not self.character:getInventory():contains(self.mapObj, true) then
		self.wrap:close()
		return
	end

	if not self.setMapData then
		self.setMapData = true
		self:initMapData()
		self.mapAPI:resetView()
	end
    
    if not self.character:getInventory():contains(self.mapObj, true) then self.wrap:setVisible(false) end

    self:updateButtons();
end

function ISMap:initMapData()
	-- Call a lua function to initialize the map data and style.
	LootMaps.callLua("Init", self)

	-- Reveal the area of this map on the world map.
	MapUtils.revealKnownArea(self)
end

function ISMap:updateJoypad()
	if self.getJoypadFocus then
		self.getJoypadFocus = false;
		if JoypadState.players[self.playerNum+1] then
			setJoypadFocus(self.playerNum, self)
		end
	end

	local currentTimeMs = getTimestampMs()
	self.updateMS = self.updateMS or currentTimeMs
	local dt = currentTimeMs - self.updateMS
	self.updateMS = currentTimeMs

	if self.joyfocus == nil then return end

	local cx = self.mapAPI:getCenterWorldX()
	local cy = self.mapAPI:getCenterWorldY()

	if isJoypadLTPressed(self.joyfocus.id, Joypad.LBumper) then
		if not self.LBumperZoom then
			self.LBumperZoom = self.mapAPI:getZoomF()
		end
		if self.LBumperZoom >= self.mapAPI:getZoomF() then
			self.LBumperZoom = self.mapAPI:getZoomF() - 1.0
			self.mapAPI:zoomAt(self.width / 2, self.height / 2, 2)
		end
	else
		self.LBumperZoom = nil
	end
	if isJoypadRTPressed(self.joyfocus.id, Joypad.RBumper) then
		if not self.RBumperZoom then
			self.RBumperZoom = self.mapAPI:getZoomF()
		end
		if self.RBumperZoom <= self.mapAPI:getZoomF() then
			self.RBumperZoom = self.mapAPI:getZoomF() + 1.0
			self.mapAPI:zoomAt(self.width / 2, self.height / 2, -2)
		end
	else
		self.RBumperZoom = nil
	end

	local x = getControllerPovX(self.joyfocus.id);
	local y = getControllerPovY(self.joyfocus.id);
	if x ~= 0 then
		if not self.povXms then
			self.povXms = currentTimeMs
		else
			if currentTimeMs - self.povXms <= 150 then
				x = 0
			end
		end
	else
		self.povXms = nil
	end
	if y ~= 0 then
		if not self.povYms then
			self.povYms = currentTimeMs
		else
			if currentTimeMs - self.povYms <= 150 then
				y = 0
			end
		end
	else
		self.povYms = nil
	end
	if x~=0 or y ~= 0 then
		local scale = self.mapAPI:getWorldScale()
		local scrollDelta = (dt / 1000) * (500 / scale)
		local snap = 1
		if x < 0 then
			cx = math.floor((cx + scrollDelta * x) / snap) * snap
		elseif x > 0 then
			cx = math.ceil((cx + scrollDelta * x) / snap) * snap
		end
		if y < 0 then
			cy = math.floor((cy + scrollDelta * y) / snap) * snap
		elseif y > 0 then
			cy = math.ceil((cy + scrollDelta * y) / snap) * snap
		end
		self.mapAPI:centerOn(cx, cy)
	end
end

function ISMap:updateButtons()
    self.editSymbolsBtn.enable = self:canWrite() or self:canErase()
    if self.symbolsUI.currentTool then
        self.ok:setTitle(getText("UI_Cancel"))
    else
        self.ok:setTitle(getText("UI_Close"))
    end
    local text = self.symbolsUI:getJoypadAButtonText()
    if text then
        self.placeSymbBtn.enable = true
        self.placeSymbBtn:setTitle(text)
    else
        self.placeSymbBtn.enable = false
    end
    if not self.editSymbolsBtn.enable then
        self.editSymbolsBtn.tooltip = getText("Tooltip_Map_CantWrite");
    else
        self.editSymbolsBtn.tooltip = nil;
    end
--[[
    if not self.removenoteBtn.enable then
        self.removenoteBtn.tooltip = getText("Tooltip_Map_CantErase");
    else
        self.removenoteBtn.tooltip = nil;
    end
    if self.addingNote then
        self.addnoteBtn:setTitle(getText("UI_Cancel"));
    else
        self.addnoteBtn:setTitle(getText("IGUI_Map_AddNote"));
    end
    if self.mapAPI:getSymbolsAPI():getSymbolCount() == 0 then
        self.removenoteBtn.enable = false;
    end
    if self.removingElement then
        if self.joyfocus then
            self.placeSymbBtn:setTitle(getText("IGUI_Map_RemoveElement"));
            self.placeSymbBtn.enable = self.mouseOverNote or self.mouseOverSymbol
        end
        self.removenoteBtn:setTitle(getText("UI_Cancel"));
    else
        self.placeSymbBtn:setTitle(getText("IGUI_Map_AddSymbol"));
        self.removenoteBtn:setTitle(getText("IGUI_Map_RemoveElement"));
    end
    if self.symbolList then
        self.symbolBtn:setTitle(getText("UI_Cancel"));
    elseif JoypadState.players[self.playerNum+1] then
        self.symbolBtn:setTitle(getText("IGUI_Map_ChooseSymbol"))
    else
        self.symbolBtn:setTitle(getText("IGUI_Map_AddSymbol"));
    end
--]]
end

function ISMap:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData)
	--self.joypadIndexY = 1
	--self.joypadIndex = 1
	--self.joypadButtons = self.joypadButtonsY[self.joypadIndexY]
	--self.joypadButtons[self.joypadIndex]:setJoypadFocused(true)
	self.scaleBtn:setVisible(false)
	self.placeSymbBtn:setVisible(true)
	self.ok:setJoypadButton(Joypad.Texture.BButton)
--	self.ok.textColor = {r=0.0, g=0.0, b=0.0, a=1.0};
	self.editSymbolsBtn:setJoypadButton(Joypad.Texture.XButton)
--	self.symbolBtn.textColor = {r=0.0, g=0.0, b=0.0, a=1.0};
	self.placeSymbBtn:setJoypadButton(Joypad.Texture.AButton)
--	self.placeSymbBtn.textColor = {r=0.0, g=0.0, b=0.0, a=1.0};
--	self.removenoteBtn:setJoypadButton(Joypad.Texture.YButton)
--	self.removenoteBtn.textColor = {r=0.0, g=0.0, b=0.0, a=1.0};
end

function ISMap:onJoypadDown(button, joypadData)
	ISPanelJoypad.onJoypadDown(self, button)
	if button == Joypad.BButton then
		self.wrap:onKeyRelease(Keyboard.KEY_ESCAPE)
	end
	if button == Joypad.XButton then
		self.editSymbolsBtn:forceClick()
	end
	if button == Joypad.AButton then
		self.symbolsUI:onJoypadDownInMap(button, joypadData)
--		self.placeSymbBtn:forceClick()
	end
	if button == Joypad.YButton then
--		self.removenoteBtn:forceClick()
	end
end

--************************************************************************--
--** ISMap:new
--**
--************************************************************************--
function ISMap:new(x, y, width, height, map, player)
	local o = ISPanelJoypad.new(self, x, y, width, height);
    o.character = getSpecificPlayer(player);
    o.playerNum = player;
	o.anchorLeft = true;
	o.anchorRight = true;
	o.anchorTop = true;
	o.anchorBottom = true;
    o.dragging = false;
    o.mapObj = map;
    o.textCursor = getTexture("media/ui/LootableMaps/textCursor.png");
	o.cross = getTexture("media/ui/LootableMaps/mapCross.png");
    o.symbolTexList = {};
    o.draggingStartingX = 0;
    o.draggingStartingY = 0;
	o.selectedColor = nil;
	o.getJoypadFocus = false
    return o;
end

