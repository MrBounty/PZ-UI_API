--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISFireplaceLightFromPetrol : ISBaseTimedAction
ISFireplaceLightFromPetrol = ISBaseTimedAction:derive("ISFireplaceLightFromPetrol")

function ISFireplaceLightFromPetrol:isValid()
	local playerInv = self.character:getInventory()
	return playerInv:contains(self.petrol) and playerInv:contains(self.lighter) and
			self.lighter:getUsedDelta() > 0 and
			self.petrol:getUsedDelta() > 0 and
			self.fireplace:getObjectIndex() ~= -1 and
			not self.fireplace:isLit() and
			self.fireplace:hasFuel()
end

function ISFireplaceLightFromPetrol:waitToStart()
	self.character:faceThisObject(self.fireplace)
	return self.character:shouldBeTurning()
end

function ISFireplaceLightFromPetrol:update()
	self.petrol:setJobDelta(self:getJobDelta())
	self.character:faceThisObject(self.fireplace)
end

function ISFireplaceLightFromPetrol:start()
	self.petrol:setJobType(campingText.lightCampfire)
	self.petrol:setJobDelta(0.0)
	self.lighter:setJobType(campingText.lightCampfire)
	self.lighter:setJobDelta(0.0)
	self:setActionAnim("Pour")
	-- Don't call setOverrideHandModels() with self.petrol, the right-hand mask
	-- will bork the animation.
	self:setOverrideHandModels(self.petrol:getStaticModel(), nil)
	self.character:EventTakeWater("EventTakeWater");
end

function ISFireplaceLightFromPetrol:stop()
	ISBaseTimedAction.stop(self)
    self.petrol:setJobDelta(0.0)
	self.lighter:setJobDelta(0.0)
end

function ISFireplaceLightFromPetrol:perform()
	self.petrol:getContainer():setDrawDirty(true)
    self.petrol:setJobDelta(0.0)
	self.lighter:setJobDelta(0.0)
    self.lighter:Use()
    self.petrol:Use()
	local fp = self.fireplace
	local args = { x = fp:getX(), y = fp:getY(), z = fp:getZ() }
	sendClientCommand(self.character, 'fireplace', 'light', args)

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISFireplaceLightFromPetrol:new(character, fireplace, lighter, petrol, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = time
	-- custom fields
	o.fireplace = fireplace
	o.lighter = lighter
	o.petrol = petrol
	return o
end
