--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 04/12/2017
-- Time: 10:19
-- To change this template use File | Settings | File Templates.
--

require "ISUI/ISPanelJoypad"

---@class ISRunningDebugUI : ISCollapsableWindow
ISRunningDebugUI = ISCollapsableWindow:derive("ISRunningDebugUI");

--************************************************************************--
--** ISRunningDebugUI:initialise
--**
--************************************************************************--

function ISRunningDebugUI:createChildren()
	local btnWid = 100
	local btnHgt = 25
	local padBottom = 10
	
	ISCollapsableWindow.createChildren(self)
	
	self.start = ISButton:new(10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, "Start timer", self, ISRunningDebugUI.startTrip);
	self.start.internal = "START";
	self.start.anchorTop = false
	self.start.anchorBottom = true
	self.start:initialise();
	self.start:instantiate();
	self.start.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.start);
	
	self.restoreEnd = ISButton:new(self:getWidth() - btnWid - 20, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, "Restore Endurance", self, ISRunningDebugUI.restoreEndurance);
	self.restoreEnd.internal = "RESTOREENDURANCE";
	self.restoreEnd.anchorTop = false
	self.restoreEnd.anchorBottom = true
	self.restoreEnd:initialise();
	self.restoreEnd:instantiate();
	self.restoreEnd.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.restoreEnd);
	
	self:setInfo("Click on start timer to start the trip, once you're done, click on stop timer. \n When you're done, you should click on restore endurance to start from scratch as endurance regen while time pass. \n \n The total current speed variable is updated only when running/sprinting, it could show wrong when idle/walking as it's not used.");
end

function ISRunningDebugUI:restoreEndurance()
	self.chr:getStats():setEndurance(1);
end

function ISRunningDebugUI:update()
	ISCollapsableWindow.update(self);
	
	if self.startedTrip then
		self.start.internal = "STOP";
		self.start.title = "Stop timer";
	end
	
	-- update variables
	if self.startedTrip then
		self.totalEndurance = self.startingEndurance - self.chr:getStats():getEndurance();
	end
end

function ISRunningDebugUI:startTrip()
	-- start
	if not self.startedTrip then
		self.startedTrip = true;
		self.startTimer = Calendar.getInstance():getTimeInMillis();
		self.stopTimer = nil;
		self.totalEndurance = 0;
		self.startingEndurance = self.chr:getStats():getEndurance();
		self.totalDistance = 0;
		self.start.title = "Stop timer";
		self.totalDist = 0;
		self.previousSq = self.chr:getSquare();
	else -- stop
		self.startedTrip = false;
		self.stopTimer = Calendar.getInstance():getTimeInMillis();
		self.start.title = "Start timer";
	end
end

function ISRunningDebugUI:render()
	ISCollapsableWindow.render(self);
	
	local startingListY = 20;
	local y = 30;
	local x = 10;
	self:drawText("Roadtrip" , x, y, 1, 1, 1, 1, UIFont.Medium);
	-- time
	y = y + 30;
	local sec = 0;
	if self.stopTimer or self.startTimer then
		local cal = Calendar.getInstance();
		if self.stopTimer then
			cal:setTimeInMillis(self.stopTimer - self.startTimer);
		else
			cal:setTimeInMillis(Calendar.getInstance():getTimeInMillis() - self.startTimer);
		end
		sec = cal:get(Calendar.SECOND);
		if sec < 10 then sec = "0" .. sec;  end
		self:drawText("Real time: " .. cal:get(Calendar.MINUTE) .. ":" .. sec, x, y, 1, 1, 1, 1, UIFont.Small);
	else
		self:drawText("Real time: " .. sec, x, y, 1, 1, 1, 1, UIFont.Small);
	end
	y = y + 20;
	-- endurance
	self:drawText("Endurance Used: " .. round(self.totalEndurance, 4), x, y, 1, 1, 1, 1, UIFont.Small);
	y = y + 20;
	-- distance
	if self.startedTrip then
		self.totalDist = self.totalDist + self.previousSq:DistToProper(self.chr:getSquare());
		self.previousSq = self.chr:getSquare();
	end
	self:drawText("Total distance: ~" .. round(self.totalDist,2) .. " tiles", x, y, 1, 1, 1, 1, UIFont.Small);
	y = y + 40;
	-- variables
	self:drawText("Running Variables ", x, y, 1, 1, 1, 1, UIFont.Medium);
	y = y + 30;
	self:drawText("Base speed: " .. round(self.chr:calculateBaseSpeed(), 2), x, y, 1, 1, 1, 1, UIFont.Small);
	y = y + 20;
	self:drawText("Clothing running mod speed: " .. round(self.chr:getRunSpeedModifier(), 2), x, y, 1, 1, 1, 1, UIFont.Small);
	y = y + 20;
	self:drawText("Injuries mod speed: " .. round(self.chr:getVariableFloat("WalkInjury", 0), 2), x, y, 1, 1, 1, 1, UIFont.Small);
	y = y + 20;
	self:drawText("Total current speed: " .. round(self.chr:getVariableFloat("WalkSpeed", 0), 2), x, y, 1, 1, 1, 1, UIFont.Small);
end

--************************************************************************--
--** ISRunningDebugUI:new
--**
--************************************************************************--
function ISRunningDebugUI:new(x, y, character)
	local o = {}
	local width = 400;
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
	o.chr = character;
	o.moveWithMouse = true;
	o.anchorLeft = true;
	o.anchorRight = true;
	o.anchorTop = true;
	o.anchorBottom = true;
	o.totalDistance = 0;
	o.totalEndurance = 0;
	o.totalDist = 0;
	return o;
end
