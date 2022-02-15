--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISRackFirearm : ISBaseTimedAction
ISRackFirearm = ISBaseTimedAction:derive("ISRackFirearm")

function ISRackFirearm:isValid()
	return true
end

function ISRackFirearm:start()
	if not ISReloadWeaponAction.canRack(self.gun) then
		self:forceComplete()
		return
	end
	
	-- Setup IsPerformingAction & the current anim we want (check in AnimSets LoadHandgun.xml for example)
	self:setAnimVariable("WeaponReloadType", self.gun:getWeaponReloadType())

	-- we asked to rack, we gonna remove bullets if one is chambered or load one is no one is chambered
	-- chamber gun will need to be racked to remove bullet, otherwise we play the unload anim
	if self.gun:haveChamber() then
		self:setAnimVariable("isRacking", true)
	else
		self:setAnimVariable("isUnloading", true)
	end

	self:setAnimVariable("RackAiming", self.character:isAiming())

	self:setOverrideHandModels(self.gun, nil)
	self:setActionAnim(CharacterActionAnims.Reload)
	self.character:reportEvent("EventReloading");

	self:ejectSpentRounds()

	self:initVars()
end

function ISRackFirearm:update()
end

-- Rack to get a bullet (from the chamber) or unjam the gun
function ISRackFirearm:rackBullet()
	if self.gun:haveChamber() then
		-- rack give one bullet & put another one back in the chamber
		-- don't give back bullet if jammed
		if not self.gun:isJammed() and self.gun:isRoundChambered() then
			self:removeBullet()
		end
		self.gun:setRoundChambered(false)
		self.gun:setJammed(false)
		if self.gun:getCurrentAmmoCount() >= self.gun:getAmmoPerShoot() then
			self.gun:setRoundChambered(true)
			self.gun:setCurrentAmmoCount(self.gun:getCurrentAmmoCount() - self.gun:getAmmoPerShoot())
		end
	else
		-- rack non chamber gun give a bullet back
		-- don't give back bullet if jammed
		if not self.gun:isJammed() and self.gun:getCurrentAmmoCount() > 0 then
			self:removeBullet()
			self.gun:setCurrentAmmoCount(self.gun:getCurrentAmmoCount() - self.gun:getAmmoPerShoot())
		end
		self.gun:setJammed(false)
	end
end

function ISRackFirearm:removeBullet()
	local newBullet = InventoryItemFactory.CreateItem(self.gun:getAmmoType())
	self.character:getInventory():AddItem(newBullet)
end

function ISRackFirearm:ejectSpentRounds()
	if self.gun:getSpentRoundCount() > 0 then
		self.gun:setSpentRoundCount(0)
	elseif self.gun:isSpentRoundChambered() then
		self.gun:setSpentRoundChambered(false)
	else
		return
	end
	if self.gun:getShellFallSound() then
		self.character:getEmitter():playSound(self.gun:getShellFallSound())
	end
end

function ISRackFirearm:initVars()
	ISReloadWeaponAction.setReloadSpeed(self.character, true)
end

function ISRackFirearm:stop()
	if self.playedEjectAmmoStartSound and self.gun:getEjectAmmoStopSound() then
		self.character:playSound(self.gun:getEjectAmmoStopSound());
	end
	self.character:clearVariable("isLoading")
	self.character:clearVariable("isRacking")
	self.character:clearVariable("isUnloading")
	self.character:clearVariable("WeaponReloadType")
	self.character:clearVariable("RackAiming")
	ISBaseTimedAction.stop(self)
end

function ISRackFirearm:perform()
	if self.playedEjectAmmoStartSound and self.gun:getEjectAmmoStopSound() then
		self.character:playSound(self.gun:getEjectAmmoStopSound());
	end
	self.character:clearVariable("isLoading")
	self.character:clearVariable("isRacking")
	self.character:clearVariable("isUnloading")
	self.character:clearVariable("WeaponReloadType")
	self.character:clearVariable("RackAiming")
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISRackFirearm:animEvent(event, parameter)
	if event == 'unloadFinished' then
		self:rackBullet()
		self:forceComplete()
	end
	if event == 'rackBullet' then
		self:rackBullet()
	end
	if event == 'rackingFinished' then
		-- Racking is done, we can exit out timedaction
		self:forceComplete()
	end
	if event == 'playReloadSound' then
		if parameter == 'rack' then
			if self.gun:getRackSound() then
				self.character:playSound(self.gun:getRackSound())
			end
		end
		if parameter == 'ejectAmmoStart' then
			if not self.playedEjectAmmoStartSound and self.gun:getEjectAmmoStartSound() then
				self.playedEjectAmmoStartSound = true;
				self.character:playSound(self.gun:getEjectAmmoStartSound());
			end
			return
		end
		if parameter == 'unload' then
			if self.gun:getEjectAmmoSound() then
				self.character:playSound(self.gun:getEjectAmmoSound())
			end
		end
	end
	if event == 'changeWeaponSprite' then
		if parameter and parameter ~= '' then
			if parameter ~= 'original' then
				self:setOverrideHandModels(parameter, nil)
			else
				self:setOverrideHandModels(self.gun:getWeaponSprite(), nil)
			end
		end
	end
end

function ISRackFirearm:new(character, gun)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnAim = false
	o.stopOnWalk = false
	o.stopOnRun = true
	o.maxTime = -1
	o.useProgressBar = false
	o.gun = gun
	return o
end	
