--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISInsertMagazine : ISBaseTimedAction
ISInsertMagazine = ISBaseTimedAction:derive("ISInsertMagazine")

function ISInsertMagazine:isValid()
	if not self.loadFinished then
		if self.gun:isContainsClip() then return false end
		if not self.character:getInventory():contains(self.magazine) then return false end
	end
	return self.character:getPrimaryHandItem() == self.gun
end

function ISInsertMagazine:start()
	self:setAnimVariable("WeaponReloadType", self.gun:getWeaponReloadType())
	self:setAnimVariable("isLoading", true)
	self:setOverrideHandModels(self.gun, nil)
	self:setActionAnim(CharacterActionAnims.Reload)
	self.character:reportEvent("EventReloading");
	self:initVars()
end

function ISInsertMagazine:update()
	-- FIXME: jobDelta is always zero since maxTime is -1
	self.gun:setJobDelta(self:getJobDelta())
	self.magazine:setJobDelta(self:getJobDelta())
end

function ISInsertMagazine:initVars()
	ISReloadWeaponAction.setReloadSpeed(self.character, false)
end

function ISInsertMagazine:loadAmmo()
	-- we insert a new clip only if we're in the motion of loading
	self.character:getInventory():Remove(self.magazine)
	self.character:removeFromHands(self.magazine)
	self.gun:setCurrentAmmoCount(self.magazine:getCurrentAmmoCount())
	self.gun:setContainsClip(true)
	self.character:clearVariable("isLoading")
	-- we rack only if no round is chambered
	if not self.gun:isRoundChambered() and self.gun:getCurrentAmmoCount() >= self.gun:getAmmoPerShoot() then
		ISTimedActionQueue.addAfter(self, ISRackFirearm:new(self.character, self.gun))
	end
	self:forceComplete()
end

function ISInsertMagazine:animEvent(event, parameter)
	-- Loading clip is done, we're moving to racking if needed
	if event == 'loadFinished' then
		self.loadFinished = true
		local chance = 3
		local xp = 1
		if self.character:getPerkLevel(Perks.Reloading) < 5 then
			chance = 1
			xp = 4
		end
		if ZombRand(chance) == 0 then
			self.character:getXp():AddXP(Perks.Reloading, xp)
		end
		self:loadAmmo()
	end
	if event == 'playReloadSound' then
		if parameter == 'load' then
			if self.gun:getInsertAmmoSound() and self.gun:getCurrentAmmoCount() < self.gun:getMaxAmmo() then
				self.character:playSound(self.gun:getInsertAmmoSound())
			end
		elseif parameter == 'insertAmmoStart' then
			if self.gun:getInsertAmmoStartSound() then
				self.character:playSound(self.gun:getInsertAmmoStartSound());
			end
		end
	end
end

function ISInsertMagazine:stop()
	if self.gun:getInsertAmmoStopSound() then
		self.character:playSound(self.gun:getInsertAmmoStopSound());
	end
	self.gun:setJobDelta(0.0)
	self.magazine:setJobDelta(0.0)
	self.character:clearVariable("isLoading")
	self.character:clearVariable("WeaponReloadType")
	ISBaseTimedAction.stop(self)
end

function ISInsertMagazine:perform()
	if self.gun:getInsertAmmoStopSound() then
		self.character:playSound(self.gun:getInsertAmmoStopSound());
	end
	self.gun:setJobDelta(0.0)
	self.magazine:setJobDelta(0.0)
	self.character:clearVariable("isLoading")
	self.character:clearVariable("WeaponReloadType")
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISInsertMagazine:new(character, gun, magazine)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnWalk = false
	o.stopOnRun = true
	o.stopOnAim = false
	o.maxTime = -1
	o.useProgressBar = false
	o.gun = gun
	o.magazine = magazine
	o.loadFinished = false
	return o
end

