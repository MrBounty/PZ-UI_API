--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISOpenMechanicsUIAction : ISBaseTimedAction
ISOpenMechanicsUIAction = ISBaseTimedAction:derive("ISOpenMechanicsUIAction")

function ISOpenMechanicsUIAction:isValid()
	return true;
end

function ISOpenMechanicsUIAction:waitToStart()
	if self.character:getVehicle() then return false end
	self.character:faceThisObject(self.vehicle)
	return self.character:shouldBeTurning()
end

function ISOpenMechanicsUIAction:update()
	self.character:faceThisObject(self.vehicle)
end

function ISOpenMechanicsUIAction:start()
	self:setActionAnim("ExamineVehicle");
	self:setOverrideHandModels(nil, nil)
end

function ISOpenMechanicsUIAction:stop()
	ISBaseTimedAction.stop(self)
end

function ISOpenMechanicsUIAction:perform()
	local ui = getPlayerMechanicsUI(self.character:getPlayerNum());
	ui.vehicle = self.vehicle;
	ui.usedHood = self.usedHood
	ui:initParts();
	ui:setVisible(true, JoypadState.players[self.character:getPlayerNum()+1])
	ui:addToUIManager()
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISOpenMechanicsUIAction:new(character, vehicle, usedHood)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.vehicle = vehicle
	o.usedHood = usedHood
	o.maxTime = 200 - (character:getPerkLevel(Perks.Mechanics) * (200/15));
	if vehicle:getScript() and vehicle:getScript():getWheelCount() == 0 then
		o.maxTime = 1
	end
	local cheat = getCore():getDebug() and getDebugOptions():getBoolean("Cheat.Vehicle.MechanicsAnywhere")
	if ISVehicleMechanics.cheat or cheat or character:isTimedActionInstant() then
		o.maxTime = 1
	end
	return o
end

