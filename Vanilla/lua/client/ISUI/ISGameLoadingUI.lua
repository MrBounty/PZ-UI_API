--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

ISGameLoadingUI = ISPanelJoypad:derive("ISGameLoadingUI")

function ISGameLoadingUI:createChildren()
	local buttonWid = 250
	local buttonHgt = 40
	
	local buttonX = (self.width - buttonWid) / 2;
	local buttonY = self.height - 70 - buttonHgt;
	
	
	local buttonGapY = 12
	
--	local button = ISButton:new(buttonX, buttonY, buttonWid, buttonHgt, getText("IGUI_PostDeath_Exit"), self, self.onExit)
--	self:configButton(button)
--	self:addChild(button)
--	self.buttonExit = button
--	buttonY = buttonY + buttonHgt + buttonGapY

	local button = ISButton:new(buttonX, buttonY, buttonWid, buttonHgt, getText("IGUI_PostDeath_Quit"), self, self.onExitToDesktop)
	self:configButton(button)
	self:addChild(button)
	self.buttonQuit = button;
end

function ISGameLoadingUI:configButton(button)
	button.anchorLeft = false
	button.anchorTop = false
	button.backgroundColor.a = 0.8
	button.borderColor.a = 0.3
end

function ISGameLoadingUI:onExitToDesktop()
	getCore():quit()
end

function ISGameLoadingUI:onExit()
	self:removeFromUIManager()
	getCore():exitToMenu()
end

function ISGameLoadingUI:onGainJoypadFocus(joypadData)
--	self:setISButtonForA(self.buttonExit)
	self:setISButtonForA(self.buttonQuit)
end

function ISGameLoadingUI:onJoypadDown(button)
	ISPanelJoypad.onJoypadDown(self, button)
end

function ISGameLoadingUI:new(status)
	local x,y,w,h = 0,0,getCore():getScreenWidth(),getCore():getScreenHeight()
	local o = ISPanelJoypad.new(self, x, y, w, h)
	o.backgroundColor.a = 0.0
	o.status = status
	ISGameLoadingUI.instance = o
	return o
end

function ISGameLoadingUI.OnJoypadActivateUI(id)
	local controller = JoypadState.controllers[id]
	if controller.joypad ~= nil then return end
	local joypadData = JoypadState.joypads[1]
	controller:setJoypad(joypadData)
	joypadData.focus = ISGameLoadingUI.instance
end

function ISGameLoadingUI_OnGameLoadingUI(status)
--	MainScreen.instance:removeFromUIManager()
	local ui = ISGameLoadingUI:new(status)
	ui:initialise()
	ui:instantiate()
	ui:addToUIManager()
	GameWindow.doRenderEvent(true)

	-- All events are cleared in GameLoadingState to avoid surprises.
	-- Register only the events needed by this UI.
	LuaEventManager.AddEvent("OnJoypadActivateUI")
	LuaEventManager.AddEvent("OnRenderTick")
	Events.OnRenderTick.Add(onJoypadRenderTick)

    local joypadData = JoypadState.joypads[1]
    local controller = joypadData.controller
	if controller then
		-- Controller was activated in the main menu.
		ISGameLoadingUI.OnJoypadActivateUI(joypadData.id)
		updateJoypadFocus(joypadData)
	else
		-- Controller wasn't activated yet.
		Events.OnJoypadActivateUI.Add(ISGameLoadingUI.OnJoypadActivateUI)
	end
end

