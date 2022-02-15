--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISExitVehicle : ISBaseTimedAction
ISExitVehicle = ISBaseTimedAction:derive("ISExitVehicle")

function ISExitVehicle:isValid()
	return self.character:getVehicle() ~= nil
end

function ISExitVehicle:update()
	local vehicle = self.character:getVehicle()
	local seat = vehicle:getSeat(self.character)
	vehicle:playPassengerAnim(seat, "exit")
	if self.character:GetVariable("ExitAnimationFinished") == "true" then
		self.character:ClearVariable("ExitAnimationFinished")
		self.character:ClearVariable("bExitingVehicle")
		self:forceComplete()
	end
end

function ISExitVehicle:start()
	self.action:setBlockMovementEtc(true) -- ignore 'E' while exiting
	local vehicle = self.character:getVehicle()
	local seat = vehicle:getSeat(self.character)
--	if vehicle:isDriver(self.character) and vehicle:isEngineRunning() then
--		if isClient() then
--			sendClientCommand(self.character, 'vehicle', 'shutOff', {})
--		else
--			vehicle:shutOff()
--		end
--	end
	self.character:SetVariable("bExitingVehicle", "true")
	vehicle:playPassengerSound(seat, "exit")
end

function ISExitVehicle:stop()
	self.character:clearVariable("bExitingVehicle")
	self.character:clearVariable("ExitAnimationFinished")
	local vehicle = self.character:getVehicle()
	local seat = vehicle:getSeat(self.character)
	vehicle:playPassengerAnim(seat, "idle")
	ISBaseTimedAction.stop(self)
end

function ISExitVehicle:perform()
	local vehicle = self.character:getVehicle()
	local seat = vehicle:getSeat(self.character)
--	if vehicle:isDriver(self.character) and vehicle:isEngineRunning() then
--		if isClient() then
--			sendClientCommand(self.character, 'vehicle', 'shutOff', {})
--		else
--			vehicle:shutOff()
--		end
--	end
	vehicle:exit(self.character)
	vehicle:setCharacterPosition(self.character, seat, "outside")
	self.character:PlayAnim("Idle")
	triggerEvent("OnExitVehicle", self.character)
    vehicle:updateHasExtendOffsetForExitEnd(self.character);
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISExitVehicle:new(character)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.maxTime = -1
	return o
end

