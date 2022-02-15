--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISRadialMenu"

ISBackButtonWheel = ISRadialMenu:derive("ISBackButtonWheel")

function ISBackButtonWheel:center()
	local x = getPlayerScreenLeft(self.playerNum)
	local y = getPlayerScreenTop(self.playerNum)
	local w = getPlayerScreenWidth(self.playerNum)
	local h = getPlayerScreenHeight(self.playerNum)

	x = x + w / 2
	y = y + h / 2

	self:setX(x - self:getWidth() / 2)
	self:setY(y - self:getHeight() / 2)
end

function ISBackButtonWheel:addCommands()
	local playerObj = getSpecificPlayer(self.playerNum)
	
	self:center()

	self:clear()

	local isPaused = UIManager.getSpeedControls() and UIManager.getSpeedControls():getCurrentGameSpeed() == 0

	if isPaused then
		self:addSlice(nil, nil, nil)
		self:addSlice(nil, nil, nil)
	else
		if not ISBackButtonWheel.disablePlayerInfo then
			self:addSlice(getText("IGUI_BackButton_PlayerInfo"), getTexture("media/ui/Heart2_On.png"), self.onCommand, self, "PlayerInfo")
		else
			self:addSlice(nil, nil, nil)
		end
		if not ISBackButtonWheel.disableCrafting then
			self:addSlice(getText("IGUI_BackButton_Crafting"), getTexture("media/ui/Carpentry_On.png"), self.onCommand, self, "Crafting")
		else
			self:addSlice(nil, nil, nil)
		end
	end

	if getCore():isZoomEnabled() and not getCore():getAutoZoom(self.playerNum) then
		if ISBackButtonWheel.disableZoomIn then
			self:addSlice(nil, nil, nil)
		else
			self:addSlice(getText("IGUI_BackButton_Zoom", getCore():getNextZoom(self.playerNum, -1) * 100), getTexture("media/ui/ZoomIn.png"), self.onCommand, self, "ZoomMinus")
		end
	end

	if UIManager.getSpeedControls() and not isClient() then
		if ISBackButtonWheel.disableTime then
			self:addSlice(nil, nil, nil)
			self:addSlice(nil, nil, nil)
		else
			if UIManager.getSpeedControls():getCurrentGameSpeed() == 0 or getGameTime():getTrueMultiplier() > 1 then
				self:addSlice(getText("IGUI_BackButton_Play"), getTexture("media/ui/Time_Play_Off.png"), self.onCommand, self, "Pause")
			else
				self:addSlice(getText("UI_optionscreen_binding_Pause"), getTexture("media/ui/Time_Pause_Off.png"), self.onCommand, self, "Pause")
			end
	
			local multiplier = getGameTime():getTrueMultiplier()
			if multiplier == 1 or multiplier == 40 then
				self:addSlice(getText("IGUI_BackButton_FF1"), getTexture("media/ui/Time_FFwd1_Off.png"), self.onCommand, self, "FastForward")
			elseif multiplier == 5 then
				self:addSlice(getText("IGUI_BackButton_FF2"), getTexture("media/ui/Time_FFwd2_Off.png"), self.onCommand, self, "FastForward")
			elseif multiplier == 20 then
				self:addSlice(getText("IGUI_BackButton_FF3"), getTexture("media/ui/Time_Wait_Off.png"), self.onCommand, self, "FastForward")
			end
		end
	end

	if Core.isLastStand() then
		self:addSlice(getText("IGUI_BackButton_LastStand"), Joypad.Texture.AButton, self.onCommand, self, "LastStand")
	end

	if getCore():isZoomEnabled() and not getCore():getAutoZoom(self.playerNum) then
		if ISBackButtonWheel.disableZoomOut then
			self:addSlice(nil, nil, nil)
		else
			self:addSlice(getText("IGUI_BackButton_Zoom", getCore():getNextZoom(self.playerNum, 1) * 100), getTexture("media/ui/ZoomOut.png"), self.onCommand, self, "ZoomPlus")
		end
	end
	
	if not isPaused and not playerObj:getVehicle() and not ISBackButtonWheel.disableMoveable then
		self:addSlice(getText("IGUI_BackButton_Movable"), getTexture("media/ui/Furniture_Off2.png"), self.onCommand, self, "MoveFurniture")
	else
		self:addSlice(nil, nil, nil)
	end
	if not isPaused and ISSearchManager.getManager(playerObj) then
		if ISSearchManager.getManager(playerObj).isSearchMode then
			self:addSlice(getText("UI_disable_search_mode"), getTexture("media/textures/Foraging/eyeconOff_Shade_UI.png"), self.onCommand, self, "ForageMode");
		else
			self:addSlice(getText("UI_enable_search_mode"), getTexture("media/textures/Foraging/eyeconOn_Shade_UI.png"), self.onCommand, self, "ForageMode");
		end;
	end;
	if not isPaused and ISSearchManager.getManager(playerObj) then
		if ISSearchManager.getManager(playerObj).isSearchMode then
			for _, icon in pairs(ISSearchManager.getManager(playerObj).closeIcons) do
				self:addSlice(getText("IGUI_Pickup") .. " " .. icon.itemObj:getDisplayName(), icon.texture, self.onCommand, self, "ForageItem");
				break; --only add the first icon found
			end;
		end;
	end;
end

function ISBackButtonWheel:onGainJoypadFocus(joypadData)
	ISRadialMenu.onGainJoypadFocus(self, joypadData)
	self.showPausedMessage = UIManager.isShowPausedMessage()
	UIManager.setShowPausedMessage(false)
end

function ISBackButtonWheel:onLoseJoypadFocus(joypadData)
	ISRadialMenu.onLoseJoypadFocus(self, joypadData)
	UIManager.setShowPausedMessage(self.showPausedMessage)
end

function ISBackButtonWheel:onJoypadDown(button, joypadData)
	ISRadialMenu.onJoypadDown(self, button, joypadData)
end

function ISBackButtonWheel:onCommand(command)
	local focus = nil
	local isPaused = UIManager.getSpeedControls() and UIManager.getSpeedControls():getCurrentGameSpeed() == 0

	local playerObj = getSpecificPlayer(self.playerNum)

	if command == "PlayerInfo" and not isPaused then
		getPlayerInfoPanel(self.playerNum):setVisible(true)
		getPlayerInfoPanel(self.playerNum):addToUIManager()
		focus = getPlayerInfoPanel(self.playerNum).panel:getActiveView()
	elseif command == "Crafting" and not isPaused then
		getPlayerCraftingUI(self.playerNum):setVisible(true)
		focus = getPlayerCraftingUI(self.playerNum)
	elseif command == "MoveFurniture" and not isPaused then
		local mo = ISMoveableCursor:new(getSpecificPlayer(self.playerNum));
		getCell():setDrag(mo, mo.player);
	elseif command == "ZoomPlus" and not getCore():getAutoZoom(self.playerNum) then
		getCore():doZoomScroll(self.playerNum, 1)
	elseif command == "ZoomMinus" and not getCore():getAutoZoom(self.playerNum) then
		getCore():doZoomScroll(self.playerNum, -1)
	elseif command == "Pause" then
		if UIManager.getSpeedControls() and not isClient() then
			if UIManager.getSpeedControls():getCurrentGameSpeed() == 0 or getGameTime():getTrueMultiplier() > 1 then
				UIManager.getSpeedControls():ButtonClicked("Play")
			elseif UIManager.getSpeedControls() then
				UIManager.getSpeedControls():ButtonClicked("Pause")
			end
		end
	elseif command == "FastForward"  then
		if UIManager.getSpeedControls() then
			local multiplier = getGameTime():getTrueMultiplier()
			if multiplier == 1 or multiplier == 40 then
				UIManager.getSpeedControls():ButtonClicked("Fast Forward x 1")
			elseif multiplier == 5 then
				UIManager.getSpeedControls():ButtonClicked("Fast Forward x 2")
			elseif multiplier == 20 then
				UIManager.getSpeedControls():ButtonClicked("Wait")
			end
		end
	elseif command == "LastStand" then
		if Core.isLastStand() then
			JoypadState.players[self.playerNum+1].focus = nil
			doLastStandBackButtonWheel(self.playerNum, 's')
			return
		end
	elseif command == "ForageMode" then
		local manager =	ISSearchManager.getManager(playerObj);
		if manager then
			manager:toggleSearchMode();
		end;
	elseif command == "ForageItem" then
		for _, icon in pairs(ISSearchManager.getManager(playerObj).closeIcons) do
			icon:doForage();
			break; --only pick up the first icon found
		end;
	end

	setJoypadFocus(self.playerNum, focus)
end

function ISBackButtonWheel:new(playerNum)
	local o = ISRadialMenu:new(0, 0, 100, 200, playerNum)
	setmetatable(o, self)
	self.__index = self
	o.playerNum = playerNum
	o:setHideWhenButtonReleased(Joypad.Back)
	return o
end

