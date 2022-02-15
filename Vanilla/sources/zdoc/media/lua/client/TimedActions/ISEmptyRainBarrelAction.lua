--***********************************************************
--**                   THE INDIE STONE                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISEmptyRainBarrelAction : ISBaseTimedAction
ISEmptyRainBarrelAction = ISBaseTimedAction:derive("ISEmptyRainBarrelAction")

function ISEmptyRainBarrelAction:isValid()
	return self.object:getObjectIndex() ~= -1
end

function ISEmptyRainBarrelAction:waitToStart()
	self.character:faceThisObject(self.object)
	return self.character:shouldBeTurning()
end

function ISEmptyRainBarrelAction:start()
	self.action:setTime(math.max(self.object:getWaterAmount(), 100))
	self.sound = self.character:playSound("GetWaterFromLake")
end

function ISEmptyRainBarrelAction:update()
	self.character:faceThisObject(self.object)
    self.character:setMetabolicTarget(Metabolics.LightDomestic)
end

function ISEmptyRainBarrelAction:stop()
	self:stopSound()
	local waterRemaining = self.object:getWaterAmount() * (1 - self:getJobDelta())

	local obj = self.object
	local args = { x = obj:getX(), y = obj:getY(), z = obj:getZ(), index = obj:getObjectIndex(), amount = waterRemaining }
	sendClientCommand(self.character, 'object', 'setWaterAmount', args)

	ISBaseTimedAction.stop(self)
end

function ISEmptyRainBarrelAction:perform()
	self:stopSound()

	local obj = self.object
	local args = { x = obj:getX(), y = obj:getY(), z = obj:getZ(), index = obj:getObjectIndex(), amount = 0 }
	sendClientCommand(self.character, 'object', 'setWaterAmount', args)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISEmptyRainBarrelAction:stopSound()
	if self.sound and self.character:getEmitter():isPlaying(self.sound) then
		self.character:stopOrTriggerSound(self.sound);
	end
end

function ISEmptyRainBarrelAction:new(character, object)
	local o = ISBaseTimedAction.new(self, character)
	o.object = object
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = 10 -- will set this in start()
	return o
end    	
