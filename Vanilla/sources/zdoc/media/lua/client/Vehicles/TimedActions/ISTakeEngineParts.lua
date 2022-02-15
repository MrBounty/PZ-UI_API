--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISTakeEngineParts : ISBaseTimedAction
ISTakeEngineParts = ISBaseTimedAction:derive("ISTakeEngineParts")

function ISTakeEngineParts:isValid()
--	return self.vehicle:isInArea(self.part:getArea(), self.character)
	return true;
end

function ISTakeEngineParts:update()
	self.character:faceThisObject(self.vehicle)
	self.item:setJobDelta(self:getJobDelta())

    self.character:setMetabolicTarget(Metabolics.MediumWork);
end

function ISTakeEngineParts:start()
	self.item:setJobType(getText("IGUI_TakeEngineParts"))
end

function ISTakeEngineParts:stop()
	self.item:setJobDelta(0)
	ISBaseTimedAction.stop(self)
end

function ISTakeEngineParts:perform()
	ISBaseTimedAction.perform(self)
	self.item:setJobDelta(0)
	local cond = self.part:getCondition();
	local skill = self.character:getPerkLevel(Perks.Mechanics) - self.vehicle:getScript():getEngineRepairLevel();
	local condForPart = math.max(20 - (skill), 5);
	local args = { vehicle = self.vehicle:getId(), skillLevel = skill }
	sendClientCommand(self.character, 'vehicle', 'takeEngineParts', args)
--[[
--	print("cond for part", condForPart)
	condForPart = ZombRand(condForPart/3, condForPart);
--	print("cond for part 2", condForPart, math.floor(cond / condForPart))
	self.character:getInventory():AddItems("Base.EngineParts", math.floor(cond / condForPart));
	if isClient() then
		local args = { vehicle = self.vehicle:getId() }
		sendClientCommand(self.character, 'vehicle', 'takeEngineParts', args)
	else
		self.part:setCondition(0);
	end
--]]
	if not self.character:getMechanicsItem(self.vehicle:getMechanicalID() .. "3") then
--		print("add exp", math.floor(cond / condForPart)/2)
		self.character:getXp():AddXP(Perks.Mechanics, math.floor(cond / condForPart)/2);
	end
	self.character:addMechanicsItem(self.vehicle:getMechanicalID() .. "3", self.part, getGameTime():getCalender():getTimeInMillis());
end

function ISTakeEngineParts:new(character, part, item, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.vehicle = part:getVehicle()
	o.part = part
	o.item = item
	o.maxTime = time
	o.jobType = getText("IGUI_TakeEngineParts")
	return o
end

