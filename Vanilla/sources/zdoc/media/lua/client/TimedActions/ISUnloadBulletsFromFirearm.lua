--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISUnloadBulletsFromFirearm : ISBaseTimedAction
ISUnloadBulletsFromFirearm = ISBaseTimedAction:derive("ISUnloadBulletsFromFirearm")

function ISUnloadBulletsFromFirearm:isValid()
	return true
end

function ISUnloadBulletsFromFirearm:start()
	self.gun:setJobType(getText("IGUI_JobType_UnloadBulletsFromFirearm"))
	self:setActionAnim(CharacterActionAnims.Reload)
	self:setAnimVariable("WeaponReloadType", self.gun:getWeaponReloadType())
	self:setAnimVariable("isUnloading", true)
	self.ammoCountStart = self.gun:getCurrentAmmoCount()
	self.gun:setJobDelta(0.0)
	self:setOverrideHandModels(self.gun, nil)
--	self:setOverrideHandModels(nil, "GunMagazine")
	self:initVars()
end

function ISUnloadBulletsFromFirearm:update()
	self.gun:setJobDelta((self.ammoCountStart - self.gun:getCurrentAmmoCount()) / self.ammoCountStart)
	if self.unloadFinished then
		self.gun:setCurrentAmmoCount(0)
		self:forceComplete()
		return
	end
	self.character:setMetabolicTarget(Metabolics.LightDomestic)
end

function ISUnloadBulletsFromFirearm:initVars()
	ISReloadWeaponAction.setReloadSpeed(self.character, false)
end

function ISUnloadBulletsFromFirearm:animEvent(event, parameter)
	if event == 'playReloadSound' then
		if parameter == 'ejectAmmoStart' then
			if not self.playedEjectAmmoStartSound and self.gun:getEjectAmmoStartSound() then
				self.playedEjectAmmoStartSound = true;
				self.character:playSound(self.gun:getEjectAmmoStartSound());
			end
			return
		end
		if self.gun:getCurrentAmmoCount() <= 0 then
			-- Fix for looping animation events arriving after loading finished.
			-- That's why 'PlaySound' isn't used instead.
			return
		end
		if self.gun:getEjectAmmoSound() then
			self.character:playSound(self.gun:getEjectAmmoSound())
		end
		local count = 1
		if self.gun:isInsertAllBulletsReload() then
			-- Double-barrel shotgun loads/unloads 2 at once.
			count = self.gun:getCurrentAmmoCount()
		end
		while self.gun:getCurrentAmmoCount() > 0 do
			local newBullet = InventoryItemFactory.CreateItem(self.gun:getAmmoType())
			self.character:getInventory():AddItem(newBullet)
			self.gun:setCurrentAmmoCount(self.gun:getCurrentAmmoCount() - 1)
			count = count - 1
			if count == 0 then
				break
			end
		end
		self.unloadFinished = false
	elseif event == 'unloadFinished' then
		if self.gun:getCurrentAmmoCount() <= 0 then
			self.unloadFinished = true
		end
	elseif event == 'changeWeaponSprite' then
		if parameter and parameter ~= '' then
			if parameter ~= 'original' then
				self:setOverrideHandModels(parameter, nil)
			else
				self:setOverrideHandModels(self.gun:getWeaponSprite(), nil)
			end
		end
	end
end

function ISUnloadBulletsFromFirearm:stop()
	if self.gun:getEjectAmmoStopSound() then
		self.character:playSound(self.gun:getEjectAmmoStopSound());
	end
	self.gun:setJobDelta(0.0)
	self.character:clearVariable("isUnloading");
	ISBaseTimedAction.stop(self)
end

function ISUnloadBulletsFromFirearm:perform()
	if self.gun:getEjectAmmoStopSound() then
		self.character:playSound(self.gun:getEjectAmmoStopSound());
	end
	self.gun:setJobDelta(0.0)
	self.character:clearVariable("isUnloading");
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISUnloadBulletsFromFirearm:new(character, gun)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnAim = false
	o.stopOnWalk = false
	o.stopOnRun = true
	o.maxTime = -1
	o.gun = gun
	o.useProgressBar = false
	return o
end
