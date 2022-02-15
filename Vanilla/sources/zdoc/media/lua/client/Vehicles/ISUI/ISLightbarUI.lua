require "ISUI/ISPanelJoypad"
--***********************************************************
--**              	  Yuri Yakovlev                        **
--***********************************************************

---@class ISLightbarUI : ISPanelJoypad
ISLightbarUI = ISPanelJoypad:derive("ISLightbarUI")

-----

function ISLightbarUI:createChildren()
	--ISPanel.createChildren(self)
	
    self.close = ISButton:new(self:getWidth() - 20 - 240, self:getHeight() - 40, 80, 30, getText("UI_Close"), self, ISLightbarUI.onOptionMouseDown);
    self.close.internal = "CLOSE";
    self.close.anchorTop = false
    self.close.anchorBottom = true
    self.close:initialise();
    self.close:instantiate();
    self.close.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.close);
	
	self.btn_sndStandby = ISButton:new(50, 50, 50, 50, "", self, ISLightbarUI.onOptionMouseDown);
--	self.btn_sndStandby:setTooltip("SOUND-STANDBY");
	self.btn_sndStandby.internal = "SND_STANDBY";
	self.btn_sndStandby:initialise();
	self.btn_sndStandby:instantiate();
	self.btn_sndStandby.borderColor = {r=1, g=1, b=1, a=0.1};
	self.btn_sndStandby:setImage(self.texEngine);
	self:addChild(self.btn_sndStandby);
	
	self.btn_sndYelp = ISButton:new(120, 50, 50, 50, "", self, ISLightbarUI.onOptionMouseDown);
--	self.btn_sndYelp:setTooltip("SOUND-YELP");
	self.btn_sndYelp.internal = "SND_YELP";
	self.btn_sndYelp:initialise();
	self.btn_sndYelp:instantiate();
	self.btn_sndYelp.borderColor = {r=1, g=1, b=1, a=0.1};
	self.btn_sndYelp:setImage(self.texEngine);
	self:addChild(self.btn_sndYelp);
	
	self.btn_sndWall = ISButton:new(50, 160, 50, 50, "", self, ISLightbarUI.onOptionMouseDown);
--	self.btn_sndWall:setTooltip("SOUND-WALL");
	self.btn_sndWall.internal = "SND_WALL";
	self.btn_sndWall:initialise();
	self.btn_sndWall:instantiate();
	self.btn_sndWall.borderColor = {r=1, g=1, b=1, a=0.1};
	self.btn_sndWall:setImage(self.texEngine);
	self:addChild(self.btn_sndWall);
	
	self.btn_sndAlarm = ISButton:new(120, 160, 50, 50, "", self, ISLightbarUI.onOptionMouseDown);
--	self.btn_sndAlarm:setTooltip("SOUND-ALARM");
	self.btn_sndAlarm.internal = "SND_ALARM";
	self.btn_sndAlarm:initialise();
	self.btn_sndAlarm:instantiate();
	self.btn_sndAlarm.borderColor = {r=1, g=1, b=1, a=0.1};
	self.btn_sndAlarm:setImage(self.texEngine);
	self:addChild(self.btn_sndAlarm);
	
	self.lightsKnob = ISKnob:new(210,30,getTexture("media/ui/Knobs/KnobDial.png"), getTexture("media/ui/Knobs/KnobBGLightbarSiren.png"), "", self.character);
    self.lightsKnob:initialise();
    self.lightsKnob:instantiate();
    self.lightsKnob.onMouseUpFct = ISLightbarUI.ChangeKnob;
    self.lightsKnob.target = self;
    self:addChild(self.lightsKnob);
	
	self:addKnobValues();
	
	self:insertNewLineOfButtons(self.btn_sndStandby, self.btn_sndYelp, self.lightsKnob)
	self:insertNewLineOfButtons(self.btn_sndWall, self.btn_sndAlarm, self.lightsKnob)
	self:insertNewLineOfButtons(self.close)
end

function ISLightbarUI:prerender()
	self.backgroundColor.a = 0.7
	ISPanelJoypad.prerender(self)
	local lights = self.vehicle:getLightbarLightsMode()
	local siren = self.vehicle:getLightbarSirenMode()
	self:drawTextCentre(getText("IGUI_VehicleLightbar_Title"), self:getWidth() /2, 10, 1, 1, 1, 1, UIFont.Medium)
	if siren == 0 then
		self:drawRect( 50+50/2-4, 50-11, 8, 8, 1, 1, 0.2, 0.2)
	else 
		self:drawRect( 50+50/2-4, 50-11, 8, 8, 1, 0.2, 0.2, 0.2)
	end
	self:drawTextCentre(getText("IGUI_VehicleLightbar_STANDBY"), 50+50/2, 50+50+5, 1, 1, 1, 1, UIFont.Small)
		if siren == 1 then
		self:drawRect( 120+50/2-4, 50-11, 8, 8, 1, 1, 0.2, 0.2)
	else 
		self:drawRect( 120+50/2-4, 50-11, 8, 8, 1, 0.2, 0.2, 0.2)
	end
	self:drawTextCentre(getText("IGUI_VehicleLightbar_YELP"), 120+50/2, 50+50+5, 1, 1, 1, 1, UIFont.Small)
	if siren == 2 then
		self:drawRect( 50+50/2-4, 160-11, 8, 8, 1, 1, 0.2, 0.2)
	else 
		self:drawRect( 50+50/2-4, 160-11, 8, 8, 1, 0.2, 0.2, 0.2)
	end
	self:drawTextCentre(getText("IGUI_VehicleLightbar_WAIL"), 50+50/2, 160+50+5, 1, 1, 1, 1, UIFont.Small)
	if siren == 3 then
		self:drawRect( 120+50/2-4, 160-11, 8, 8, 1, 1, 0.2, 0.2)
	else 
		self:drawRect( 120+50/2-4, 160-11, 8, 8, 1, 0.2, 0.2, 0.2)
	end
	self:drawTextCentre(getText("IGUI_VehicleLightbar_ALARM"), 120+50/2, 160+50+5, 1, 1, 1, 1, UIFont.Small)
	if lights ~= self.lightsKnob:getValue() and self.lightsKnob.dragging == false then
		self.lightsKnob:setKnobPosition(lights)
	end
end

function ISLightbarUI:ChangeKnob()
	self:setLightbarLightsMode(self.lightsKnob:getValue())
end

function ISLightbarUI:setVehicle(vehicle)
	self.vehicle = vehicle
end


function ISLightbarUI:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData)
	self.joypadIndexY = 1
	self.joypadIndex = 1
	self.joypadButtons = self.joypadButtonsY[self.joypadIndexY]
	self.joypadButtons[self.joypadIndex]:setJoypadFocused(true)
	self:setISButtonForB(self.close)
end

function ISLightbarUI:onJoypadDown(button, joypadData)
	ISPanelJoypad.onJoypadDown(self, button)
	if button == Joypad.BButton then
		self:onOptionMouseDown(self.close)
	end
end

function ISLightbarUI:onOptionMouseDown(button, x, y)
	if button.internal == "CLOSE" then
        self:setVisible(false);
        self:removeFromUIManager();
        if JoypadState.players[self.playerNum+1] then
            setJoypadFocus(self.playerNum, nil)
        end
	end
	if button.internal == "SND_STANDBY" then
        self:setLightbarSirenMode(0)
	end
	if button.internal == "SND_YELP" then
        self:setLightbarSirenMode(1)
	end
	if button.internal == "SND_WALL" then
        self:setLightbarSirenMode(2)
	end
	if button.internal == "SND_ALARM" then
        self:setLightbarSirenMode(3)
	end
end

function ISLightbarUI:setLightbarSirenMode(mode)
	sendClientCommand(self.playerObj, 'vehicle', 'setLightbarSirenMode', {mode=mode})
end

function ISLightbarUI:setLightbarLightsMode(mode)
	sendClientCommand(self.playerObj, 'vehicle', 'setLightbarLightsMode', {mode=mode})
end

function ISLightbarUI:addKnobValues()
    self.lightsKnob:addValue(30, 2);
    self.lightsKnob:addValue(90, 3);
    self.lightsKnob:addValue(270, 0);
    self.lightsKnob:addValue(330, 1);

	local lights = self.vehicle:getLightbarLightsMode()
	self.lightsKnob:setKnobPosition(lights)
end

function ISLightbarUI:new(x, y, width, height, playerObj)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.playerNum = playerObj:getPlayerNum()
	o.playerObj = playerObj
	o.vehicle = o.playerObj:getVehicle()
    if y == 0 then
        o.y = getPlayerScreenTop(o.playerNum) + (getPlayerScreenHeight(o.playerNum) - height) / 2
        o:setY(o.y)
    end
    if x == 0 then
        o.x = getPlayerScreenLeft(o.playerNum) + (getPlayerScreenWidth(o.playerNum) - width) / 2
        o:setX(o.x)
    end
	return o
end

