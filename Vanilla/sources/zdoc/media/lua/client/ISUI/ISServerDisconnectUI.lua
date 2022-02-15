--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

---@class ISServerDisconnectUI : ISPanelJoypad
ISServerDisconnectUI = ISPanelJoypad:derive("ISServerDisconnectUI")

function ISServerDisconnectUI:createChildren()
	local buttonWid = 250
	local buttonHgt = 40
	local buttonGapY = 12
	local totalHgt = (buttonHgt * 2) + (buttonGapY * 1)
	local buttonX = (self.width - buttonWid) / 2
	local buttonY = self.height - 40 - totalHgt

	local button = ISButton:new(buttonX, buttonY, buttonWid, buttonHgt, getText("IGUI_PostDeath_Exit"), self, self.onToMainMenu)
	self:configButton(button)
	self:addChild(button)
	self.buttonExit = button

	buttonY = buttonY + buttonHgt + buttonGapY

	button = ISButton:new(buttonX, buttonY, buttonWid, buttonHgt, getText("IGUI_PostDeath_Quit"), self, self.onToDesktop)
	self:configButton(button)
	self:addChild(button)
	self.buttonQuit = button
end

function ISServerDisconnectUI:configButton(button)
	button.anchorLeft = false
	button.anchorTop = false
	button.anchorBottom = true
	button.backgroundColor.a = 0.8
	button.borderColor.a = 0.3
end

function ISServerDisconnectUI:onToMainMenu()
	self:removeFromUIManager()
	getCore():exitToMenu()
end

function ISServerDisconnectUI:onToDesktop()
	self:removeFromUIManager()
	getCore():quitToDesktop()
end

function ISServerDisconnectUI:onGainJoypadFocus(joypadData)
	self:setISButtonForA(self.buttonExit)
	self:setISButtonForB(self.buttonQuit)
end

function ISServerDisconnectUI:onJoypadDown(button)
	ISPanelJoypad.onJoypadDown(self, button)
end

function ISServerDisconnectUI:onResolutionChange(oldw, oldh, neww, newh)
	self:setWidth(neww)
	self:setHeight(newh)
	self.buttonExit:setX((neww - self.buttonExit.width) / 2)
	self.buttonQuit:setX((neww - self.buttonQuit.width) / 2)
end

function ISServerDisconnectUI:new(reason)
	local x,y,w,h = 0,0,getCore():getScreenWidth(),getCore():getScreenHeight()
	local o = ISPanelJoypad:new(x, y, w, h)
	setmetatable(o, self)
	self.__index = self
	o:noBackground()
	o.reason = reason
	ISServerDisconnectUI.instance = o
	return o
end

function ISServerDisconnectUI_OnServerDisconnectUI(reason)
	local ui = ISServerDisconnectUI:new(reason)
	ui:initialise()
	ui:instantiate()
	ui:addToUIManager()

	-- All events are cleared in ServerDisconnectState to avoid surprises.
	Events.OnRenderTick.Add(onJoypadRenderTick)

	if JoypadState.players[0+1] then
		setJoypadFocus(0, ui)
	end

	Events.OnResolutionChange.Add(
		function(oldw, oldh, neww, newh)
			ui:onResolutionChange(oldw, oldh, neww, newh)
		end
	)
end

