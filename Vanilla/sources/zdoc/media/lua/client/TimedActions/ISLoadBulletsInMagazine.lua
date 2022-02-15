--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISLoadBulletsInMagazine : ISBaseTimedAction
ISLoadBulletsInMagazine = ISBaseTimedAction:derive("ISLoadBulletsInMagazine")

function ISLoadBulletsInMagazine:isValid()
	return self.character:getInventory():contains(self.magazine)
end

function ISLoadBulletsInMagazine:start()
	if not self.character:getInventory():containsWithModule(self.magazine:getAmmoType()) then
		self:forceStop();
		return;
	end
	self.ammoCountStart = self.magazine:getCurrentAmmoCount()
	self.magazine:setJobDelta(0.0)
	self:setOverrideHandModels(nil, "GunMagazine");
	self:setActionAnim(CharacterActionAnims.InsertBullets);
	self:initVars()
end

function ISLoadBulletsInMagazine:update()
	local remaining = self.ammoCount - (self.magazine:getCurrentAmmoCount() - self.ammoCountStart)
	self.magazine:setJobType(getText("ContextMenu_InsertBulletsInMagazine", remaining))
	self.magazine:setJobDelta((self.magazine:getCurrentAmmoCount() - self.ammoCountStart) / self.ammoCount)
	if self.loadFinished then
		self:setOverrideHandModels(nil, nil);
		self:forceComplete();
		return;
	end
	self.character:setMetabolicTarget(Metabolics.LightDomestic);
end

function ISLoadBulletsInMagazine:initVars()
	ISReloadWeaponAction.setReloadSpeed(self.character, false)
end

function ISLoadBulletsInMagazine:isLoadFinished()
	return self.magazine:getCurrentAmmoCount() >= self.magazine:getMaxAmmo() or
		not self.character:getInventory():containsWithModule(self.magazine:getAmmoType())
end

function ISLoadBulletsInMagazine:animEvent(event, parameter)
	if event == 'InsertBulletSound' then
		if self:isLoadFinished() then
			-- Fix for looping animation events arriving after loading finished.
			-- That's why 'PlaySound' isn't used instead.
			return
		end
		self.character:playSound(parameter);
	elseif event == 'InsertBullet' then
		if self:isLoadFinished() then
			-- Fix for looping animation events arriving after loading finished.
			return
		end
		local chance = 5;
		local xp = 1;
		if self.character:getPerkLevel(Perks.Reloading) < 5 then
			chance = 2;
			xp = 4;
		end
		if ZombRand(chance) == 0 then
			self.character:getXp():AddXP(Perks.Reloading, xp);
		end
		self.character:getInventory():RemoveOneOf(self.magazine:getAmmoType());
		self.magazine:setCurrentAmmoCount(self.magazine:getCurrentAmmoCount() + 1);
	elseif event == 'loadFinished' then
		if self:isLoadFinished() then
			self.loadFinished = true
		end
	elseif event == 'playReloadSound' then
		if parameter == 'insertAmmoStart' then
			if not self.playedInsertAmmoStartSound and self.magazine:getInsertAmmoStartSound() then
				self.playedInsertAmmoStartSound = true;
				self.character:playSound(self.gun:getInsertAmmoStartSound());
			end
		end
	end
end


function ISLoadBulletsInMagazine:stop()
	self.magazine:setJobDelta(0.0)
	ISBaseTimedAction.stop(self)
end

function ISLoadBulletsInMagazine:perform()
	self.magazine:setJobDelta(0.0)
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISLoadBulletsInMagazine:new(character, magazine, ammoCount)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnAim = false
	o.stopOnWalk = false
	o.stopOnRun = true
	o.maxTime = -1
	o.magazine = magazine;
	o.useProgressBar = false;
	o.ammoCount = ammoCount
	return o
end
