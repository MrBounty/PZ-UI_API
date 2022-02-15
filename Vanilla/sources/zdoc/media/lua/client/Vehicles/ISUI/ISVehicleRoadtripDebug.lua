--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 04/12/2017
-- Time: 10:19
-- To change this template use File | Settings | File Templates.
--

require "ISUI/ISPanelJoypad"

---@class ISVehicleRoadtripDebug : ISCollapsableWindow
ISVehicleRoadtripDebug = ISCollapsableWindow:derive("ISVehicleRoadtripDebug");

--************************************************************************--
--** ISVehicleRoadtripDebug:initialise
--**
--************************************************************************--

function ISVehicleRoadtripDebug:createChildren()
	local btnWid = 100
	local btnHgt = 25
	local padBottom = 10
	
	ISCollapsableWindow.createChildren(self)
	
	self.start = ISButton:new(10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, "Start", self, ISVehicleRoadtripDebug.startRoadtrip);
	self.start.internal = "START";
	self.start.anchorTop = false
	self.start.anchorBottom = true
	self.start:initialise();
	self.start:instantiate();
	self.start.borderColor = {r=1, g=1, b=1, a=0.1};
	self.start:setEnable(false);
	self:addChild(self.start);
		
	self.stop = ISButton:new(self:getWidth() - btnWid - 10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, "Stop", self, ISVehicleRoadtripDebug.stopRoadtrip);
	self.stop.internal = "STOP";
	self.stop.anchorTop = false
	self.stop.anchorBottom = true
	self.stop:initialise();
	self.stop:instantiate();
	self.stop.borderColor = {r=1, g=1, b=1, a=0.1};
	self.stop:setEnable(false);
	self:addChild(self.stop);
end


function ISVehicleRoadtripDebug:update()
	ISCollapsableWindow.update(self);
	
	self.vehicle = self.character:getVehicle();
	
	if self.vehicle and self.vehicle:getDriver() and not self.startedTrip then
		self.start:setEnable(true);
	end
	
	if self.startedTrip then
		self.stop:setEnable(true);
	end
end
	
function ISVehicleRoadtripDebug:startRoadtrip()
	self.start:setEnable(false);
	self.stop:setEnable(true);
	self.startedTrip = true;
	self.startTimer = Calendar.getInstance():getTimeInMillis();
	self.stopTimer = nil;
	
	if self.vehicle:getPartById("GasTank") then
		self.initialGas = self.vehicle:getPartById("GasTank"):getContainerContentAmount();
	else
		self.initialGas = 0;
	end
	
	if self.vehicle:getPartById("Battery") and self.vehicle:getPartById("Battery"):getInventoryItem() then
		self.initialBattery = self.vehicle:getPartById("Battery"):getInventoryItem():getUsedDelta();
	else
		self.initialBattery = 0;
	end
		
	self.partsCondition = {};
	for i=0,self.vehicle:getPartCount()-1 do
		local part = self.vehicle:getPartByIndex(i);
		local newPart = {};
		newPart.item = part:getInventoryItem();
		newPart.condition = part:getCondition();
		newPart.id = part:getId();
		self.partsCondition[part:getId()] = newPart;
	end
	
	self.previousSq = self.vehicle:getSquare();
	self.totalDist = 0;
end

function ISVehicleRoadtripDebug:stopRoadtrip()
	self.start:setEnable(true);
	self.stop:setEnable(false);
	self.startedTrip = false;
	self.stopTimer = Calendar.getInstance():getTimeInMillis();
end

function ISVehicleRoadtripDebug:render()
	ISCollapsableWindow.render(self);
	local startingListY = 20;
	local y = 20;
	local x = 10;
	if self.startTimer then
		local cal = Calendar.getInstance();
		if self.stopTimer then
			cal:setTimeInMillis(self.stopTimer - self.startTimer);
		else
			cal:setTimeInMillis(Calendar.getInstance():getTimeInMillis() - self.startTimer);
		end
		local sec = cal:get(Calendar.SECOND);
		if sec < 10 then sec = "0" .. sec;  end
		self:drawText("Real time: " .. cal:get(Calendar.MINUTE) .. ":" .. sec, x, y, 1, 1, 1, 1, UIFont.Small);
		y = y + 30;
		startingListY = startingListY + 30;
		
		if self.vehicle:getPartById("Battery") and self.vehicle:getPartById("Battery"):getInventoryItem() then
			local chargeChange = round((self.vehicle:getPartById("Battery"):getInventoryItem():getUsedDelta() - self.initialBattery) * 100, 2);
			if chargeChange ~= 0 then
				local batteryTxt = "Battery charge: " .. round(self.vehicle:getPartById("Battery"):getInventoryItem():getUsedDelta()*100, 2) .. "%";
				self:drawText(batteryTxt, x, y, 1, 1, 1, 1, UIFont.Small);
				if chargeChange > 0 then
					self:drawText(" (+" .. chargeChange .. "%)", x + getTextManager():MeasureStringX(UIFont.Small, batteryTxt), y, 0.2, 1, 0.2, 1, UIFont.Small);
				else
					self:drawText(" (" .. chargeChange .. "%)", x + getTextManager():MeasureStringX(UIFont.Small, batteryTxt), y, 1, 0.2, 0.2, 1, UIFont.Small);
				end
			else
				self:drawText("Battery charge: " .. round(self.vehicle:getPartById("Battery"):getInventoryItem():getUsedDelta()*100, 2) .. "%", x, y, 1, 1, 1, 1, UIFont.Small);
			end
			y = y + 20;
			startingListY = startingListY + 20;
		end
		if self.vehicle:getPartById("GasTank") then
			local fuelChange = round(self.vehicle:getPartById("GasTank"):getContainerContentAmount()-self.initialGas, 2);
			local fuelTxt = "Fuel: " .. round(self.vehicle:getPartById("GasTank"):getContainerContentAmount(),2);
			self:drawText(fuelTxt, x, y, 1, 1, 1, 1, UIFont.Small);
			if fuelChange ~= 0 then
				if fuelChange > 0 then
					self:drawText(" (+" .. fuelChange .. ")", x + getTextManager():MeasureStringX(UIFont.Small, fuelTxt), y, 0.2, 1, 0.2, 1, UIFont.Small);
				else
					self:drawText(" (" .. fuelChange .. ")", x + getTextManager():MeasureStringX(UIFont.Small, fuelTxt), y, 1, 0.2, 0.2, 1, UIFont.Small);
				end
			end
			y = y + 20;
			startingListY = startingListY + 20;
		end
	end
	
	if self.startedTrip then
		self.totalDist = self.totalDist + self.previousSq:DistToProper(self.vehicle:getSquare());
		self.previousSq = self.vehicle:getSquare();
		self:drawText("Total distance: ~" .. round(self.totalDist/100,2) .. " km", x, y, 1, 1, 1, 1, UIFont.Small);
		y = y + 30;
		startingListY = startingListY + 30;
	end
	
	if self.partsCondition and self.vehicle then
		for i,v in pairs(self.partsCondition) do
			local testPart = self.vehicle:getPartById(i);
			local partName = getText("IGUI_VehiclePart" .. v.id);
			if testPart then
				if v.item and not testPart:getInventoryItem() then
					self:drawText(partName .. ": missing item", x, y, 1, 0.2, 0.2, 1, UIFont.Small);
					x,y = self:updateXY(x,y,startingListY);
				elseif v.condition ~= testPart:getCondition() then
					self:drawText(partName .. ": condition = ", x, y, 1, 1, 1, 1, UIFont.Small);
					self:drawText("" .. (testPart:getCondition() - v.condition), x + getTextManager():MeasureStringX(UIFont.Small, partName .. ": condition = "), y, 1, 0.2, 0.2, 1, UIFont.Small);
					x,y = self:updateXY(x,y,startingListY);
				end
			else
				self:drawText(partName .. ": missing", x, y, 1, 0.2, 0.2, 1, UIFont.Small);
				x,y = self:updateXY(x,y,startingListY);
			end
		end
	end
end

function ISVehicleRoadtripDebug:updateXY(x,y,startingListY)
	y = y + 15;
	if y > self:getHeight() - 40 then
		y = startingListY;
		x = x + 200;
	end
	return x,y;
end

--************************************************************************--
--** ISVehicleRoadtripDebug:new
--**
--************************************************************************--
function ISVehicleRoadtripDebug:new(x, y, character)
	local o = {}
	local width = 500;
	local height = 350;
	o = ISCollapsableWindow:new(x, y, width, height);
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
	o.moveWithMouse = true;
	o.anchorLeft = true;
	o.anchorRight = true;
	o.anchorTop = true;
	o.anchorBottom = true;
	return o;
end
