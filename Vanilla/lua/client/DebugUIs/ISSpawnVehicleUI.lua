ISSpawnVehicleUI = ISPanelJoypad:derive("ISSpawnVehicleUI");

function ISSpawnVehicleUI:initialise()
	ISPanelJoypad.initialise(self);

	local y=60

	self.vehicle = ISComboBox:new(10, y, 180, 20)
	self.vehicle:initialise()
	self:addChild(self.vehicle)
	self.vehicles = getAllVehicles(false);
	for i=0, self.vehicles:size()-1 do
		local text = "";
		self.vehicle:addOptionWithData(self.vehicles:get(i) .. text, self.vehicles:get(i));
	end

	y=y+40

	self.boolOptions = ISTickBox:new(10, y, 200, 20, "", self, ISSpawnVehicleUI.onBoolOptionsChange);
	self.boolOptions:initialise()
	self:addChild(self.boolOptions)
	self.boolOptions:addOption("Key");
	self.boolOptions.selected[1] = true
	self.boolOptions:addOption("Repair");
	self.boolOptions.selected[2] = true
	y=y+80

	self.spawn = ISButton:new(10, self.height-35, 80, 25, "Spawn", self, ISSpawnVehicleUI.onClick);
	self.spawn.anchorTop = false
	self.spawn.anchorBottom = true
	self.spawn.internal = "SPAWN";
	self.spawn:initialise();
	self.spawn:instantiate();
	self.spawn.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.spawn);

	self.close = ISButton:new(110, self.height-35, 80, 25, "Close", self, ISSpawnVehicleUI.onClick);
	self.close.anchorTop = false
	self.close.anchorBottom = true
	self.close.internal = "CLOSE";
	self.close:initialise();
	self.close:instantiate();
	self.close.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.close);
end

function ISSpawnVehicleUI:onBoolOptionsChange(index, selected)
	---print("ISSpawnVehicleUI"..tostring(index).." is "..tostring(selected))
end

function ISSpawnVehicleUI:destroy()
	UIManager.setShowPausedMessage(true);
	self:setVisible(false);
	self:removeFromUIManager();
end

function ISSpawnVehicleUI:getVehicle()
	return self.vehicle.options[self.vehicle.selected].data;
end

function ISSpawnVehicleUI:onClick(button)
	if self.player ~= nil then
		if button.internal == "SPAWN" then
			self.player:setDir(IsoDirections.W)
			if isClient() then
				local command = string.format("/addvehicle %s", tostring(self:getVehicle()))
				SendCommandToServer(command)
			else
				addVehicle(tostring(self:getVehicle()))
			end
		elseif button.internal == "CLOSE" then
			local vehicle = self.player:getNearVehicle()
			if vehicle ~= nil then
				if self.boolOptions.selected[2] == true then
					sendClientCommand(self.player, "vehicle", "repair", { vehicle = vehicle:getId() })
				end
				if self.boolOptions.selected[1] == true then
					sendClientCommand(self.player, "vehicle", "getKey", { vehicle = vehicle:getId() })
				end
			end
			self:destroy();
		end
	end
end

function ISSpawnVehicleUI:titleBarHeight()
	return 16
end

function ISSpawnVehicleUI:prerender()
	self.backgroundColor.a = 0.8

	self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
	
	local th = self:titleBarHeight()
	self:drawTextureScaled(self.titlebarbkg, 2, 1, self:getWidth() - 4, th - 2, 1, 1, 1, 1);
	
	self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	
	self:drawTextCentre("Spawn Vehicle", self:getWidth() / 2, 20, 1, 1, 1, 1, UIFont.NewLarge);
end

function ISSpawnVehicleUI:render()

end

function ISSpawnVehicleUI:onMouseMove(dx, dy)
	self.mouseOver = true
	if self.moving then
		self:setX(self.x + dx)
		self:setY(self.y + dy)
		self:bringToTop()
	end
end

function ISSpawnVehicleUI:onMouseMoveOutside(dx, dy)
	self.mouseOver = false
	if self.moving then
		self:setX(self.x + dx)
		self:setY(self.y + dy)
		self:bringToTop()
	end
end

function ISSpawnVehicleUI:onMouseDown(x, y)
	if not self:getIsVisible() then
		return
	end
	self.downX = x
	self.downY = y
	self.moving = true
	self:bringToTop()
end

function ISSpawnVehicleUI:onMouseUp(x, y)
	if not self:getIsVisible() then
		return;
	end
	self.moving = false
	if ISMouseDrag.tabPanel then
		ISMouseDrag.tabPanel:onMouseUp(x,y)
	end
	ISMouseDrag.dragView = nil
end

function ISSpawnVehicleUI:onMouseUpOutside(x, y)
	if not self:getIsVisible() then
		return
	end
	self.moving = false
	ISMouseDrag.dragView = nil
end

function ISSpawnVehicleUI:new(x, y, width, height, player, target)
	local o = {}
	o = ISPanelJoypad:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self
	if y == 0 then
		o.y = o:getMouseY() - (height / 2)
		o:setY(o.y)
	end
	if x == 0 then
		o.x = o:getMouseX() - (width / 2)
		o:setX(o.x)
	end
	o.name = nil;
	o.backgroundColor = {r=0, g=0, b=0, a=0.5};
	o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
	o.width = width;
	local txtWidth = getTextManager():MeasureStringX(UIFont.Small, text) + 10;
	if width < txtWidth then
		o.width = txtWidth;
	end
	o.height = height;
	o.anchorLeft = true;
	o.anchorRight = true;
	o.anchorTop = true;
	o.anchorBottom = true;
	o.target = target;
	o.player = player;
	o.titlebarbkg = getTexture("media/ui/Panel_TitleBar.png");
	return o;
end
