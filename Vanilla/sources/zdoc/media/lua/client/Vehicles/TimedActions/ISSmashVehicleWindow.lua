--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISSmashVehicleWindow : ISBaseTimedAction
ISSmashVehicleWindow = ISBaseTimedAction:derive("ISSmashVehicleWindow")

function ISSmashVehicleWindow:isValid()
	return self.window and self.window:isHittable()
end

function ISSmashVehicleWindow:waitToStart()
	self.character:faceThisObject(self.vehicle)
	return self.character:shouldBeTurning()
end

function ISSmashVehicleWindow:update()
--	if self.character:pressedMovement() then
--		self:forceStop()
--		return
--	end
--	self.character:faceThisObject(self.vehicle)
--	local weapon = self.character:getPrimaryHandItem()
--	-- TODO: different animations when inside vehicle
--	if instanceof(weapon, "HandWeapon") and weapon:getSwingAnim() and not weapon:isRanged() then
--		self.character:PlayAnimUnlooped("Attack_" .. weapon:getSwingAnim())
--	else
--		self.character:PlayAnimUnlooped("WindowSmash")
--	end
--	local spriteDef = self.character:getSpriteDef()
--	spriteDef:setFrameSpeedPerFrame(0.18)
--	local frame = spriteDef:getFrame()
--	if self.frame < 5 and frame >= 5 then
--		self.window:hit(self.character)
--	end
--	self.frame = frame
--	if self.character:getSpriteDef():isFinished() then
--		self:forceComplete()
--	end
    self.character:setMetabolicTarget(Metabolics.UsingTools);
end

function ISSmashVehicleWindow:start()
--	self.action:setBlockMovementEtc(true)
--	self.frame = 0
	-- TODO: set Finished=false if already playing Attack_ animation
end

function ISSmashVehicleWindow:stop()
	ISBaseTimedAction.stop(self)
end

function ISSmashVehicleWindow:perform()
	self.character:smashCarWindow(self.part)
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISSmashVehicleWindow:new(character, part, open)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.vehicle = part:getVehicle()
	o.part = part
	o.window = part:getWindow()
	o.open = open
	o.maxTime = 0
	return o
end

