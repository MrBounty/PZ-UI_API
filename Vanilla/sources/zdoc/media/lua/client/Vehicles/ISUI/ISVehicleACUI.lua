--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 04/12/2017
-- Time: 10:19
-- To change this template use File | Settings | File Templates.
--

require "ISUI/ISPanelJoypad"

---@class ISVehicleACUI : ISPanelJoypad
ISVehicleACUI = ISPanelJoypad:derive("ISVehicleACUI");

--************************************************************************--
--** ISVehicleACUI:initialise
--**
--************************************************************************--

function ISVehicleACUI:createChildren()
	local btnWid = 100
	local btnHgt = 25
	local padBottom = 10
	
	self.close = ISButton:new(self:getWidth() - btnWid - 10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Close"), self, ISVehicleACUI.onClick);
	self.close.internal = "CLOSE";
	self.close.anchorTop = false
	self.close.anchorBottom = true
	self.close:initialise();
	self.close:instantiate();
	self.close.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.close);
	
	
	self.tempKnob = ISKnob:new(self:getWidth() / 2 - getTexture("media/ui/Knobs/KnobBG_AC.png"):getWidth() / 2,20,self.knobTex, getTexture("media/ui/Knobs/KnobBG_AC.png"), getText("IGUI_Temperature"), self.character);
	self.tempKnob:initialise();
	self.tempKnob:instantiate();
	self.tempKnob.onMouseUpFct = ISVehicleACUI.changeKnob;
	self.tempKnob.target = self;
	self:addChild(self.tempKnob);
		
	self.ok = ISButton:new(10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("ContextMenu_Turn_On"), self, ISVehicleACUI.onClick);
	self.ok.internal = "OK";
	self.ok.anchorTop = false
	self.ok.anchorBottom = true
	self.ok:initialise();
	self.ok:instantiate();
	self.ok.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.ok);
	
	self:addKnobValues();
	self:updateButtons();
	
	local currentTemp = self.heater:getModData().temperature;
	self.tempKnob:setKnobPosition(currentTemp);
	
	self:insertNewLineOfButtons(self.tempKnob)
	self:insertNewLineOfButtons(self.ok, self.close)
end

function ISVehicleACUI:changeKnob()
	sendClientCommand(self.character, 'vehicle', 'toggleHeater', { on = self.heater:getModData().active, temp = self.tempKnob:getValue() })
end

function ISVehicleACUI:update()
	self:updateButtons();
	self:centerOnScreen()
	if not self.character:getVehicle() then
		self:removeFromUIManager()
	end
end

function ISVehicleACUI:updateButtons()
	if self.vehicle:isEngineRunning() or self.vehicle:isKeysInIgnition() then
		self.ok:setEnable(true);
		self.ok:setTooltip(nil);
	else
		self.ok:setEnable(false);
		self.ok:setTooltip(getText("UI_Vehicle_HeaterNeedKey"));
	end
	if self.heater:getModData().active then
		self.ok:setTitle(getText("ContextMenu_Turn_Off"))
	else
		self.ok:setTitle(getText("ContextMenu_Turn_On"))
	end
end

function ISVehicleACUI:addKnobValues()
	self.tempKnob:addValue(0, 0);
	self.tempKnob:addValue(30, 8);
	self.tempKnob:addValue(60, 15);
	self.tempKnob:addValue(90, 25);
	self.tempKnob:addValue(270, -25);
	self.tempKnob:addValue(300, -15);
	self.tempKnob:addValue(330, -8);
end

function ISVehicleACUI:render()
	ISPanelJoypad.render(self);
	if self.vehicle:windowsOpen() > 0 then
		self:drawTextCentre(getText("UI_Vehicle_WindowOpen"), self:getWidth() / 2, 5, 1, 0, 0, 1, UIFont.Small);
	end
end

function ISVehicleACUI:prerender()
	self.backgroundColor.a = 0.7
	ISPanelJoypad.prerender(self);
end

function ISVehicleACUI:onClick(button)
	if button.internal == "CLOSE" then
		self:undisplay()
	end
	if button.internal == "OK" then
		sendClientCommand(self.character, 'vehicle', 'toggleHeater', { on = not self.heater:getModData().active, temp = self.tempKnob:getValue() })
		self:undisplay()
	end
end

function ISVehicleACUI:undisplay()
	self:removeFromUIManager()
	if JoypadState.players[self.playerNum+1] then
		setJoypadFocus(self.playerNum, nil)
	end
end

function ISVehicleACUI:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData)
	self.joypadIndexY = 1
	self.joypadIndex = 1
	self.joypadButtons = self.joypadButtonsY[self.joypadIndexY]
	self.joypadButtons[self.joypadIndex]:setJoypadFocused(true)
	self:setISButtonForA(self.ok)
	self:setISButtonForB(self.close)
end

function ISVehicleACUI:onJoypadDown(button)
	ISPanelJoypad.onJoypadDown(self, button)
	if button == Joypad.AButton then
		self:onClick(self.ok)
	end
	if button == Joypad.BButton then
		self:onClick(self.close)
	end
end

function ISVehicleACUI:centerOnScreen()
	local width = self:getWidth()
	local height = self:getHeight()
	local x = getPlayerScreenLeft(self.playerNum) + (getPlayerScreenWidth(self.playerNum) - width) / 2
	local y = getPlayerScreenTop(self.playerNum) + (getPlayerScreenHeight(self.playerNum) - height) / 2
	self:setX(x)
	self:setY(y)
end

function ISVehicleACUI:setVehicle(vehicle)
	self.vehicle = vehicle
	self.heater = vehicle:getHeater();
	if not self.heater:getModData().temperature then
		self.heater:getModData().temperature = 0
	end
	local currentTemp = self.heater:getModData().temperature
	self.tempKnob:setKnobPosition(currentTemp)
end

--************************************************************************--
--** ISVehicleACUI:new
--**
--************************************************************************--
function ISVehicleACUI:new(x, y, character)
	local o = {}
	local width = 300;
	local height = 250;
	o = ISPanelJoypad:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self
	o.playerNum = character:getPlayerNum()
	if y == 0 then
		o.y = getPlayerScreenTop(o.playerNum) + (getPlayerScreenHeight(o.playerNum) - height) / 2
		o:setY(o.y)
	end
	if x == 0 then
		o.x = getPlayerScreenLeft(o.playerNum) + (getPlayerScreenWidth(o.playerNum) - width) / 2
		o:setX(o.x)
	end
	o.width = width;
	o.height = height;
	o.character = character;
	o.vehicle = character:getVehicle();
	o.heater = o.vehicle:getHeater();
	if not o.heater:getModData().temperature then o.heater:getModData().temperature = 0 end
	o.moveWithMouse = true;
	o.knobTex = getTexture("media/ui/Knobs/KnobDial.png");
	o.anchorLeft = true;
	o.anchorRight = true;
	o.anchorTop = true;
	o.anchorBottom = true;
	return o;
end
