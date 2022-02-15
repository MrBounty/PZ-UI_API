require "Reloading/ISReloadable"

---@class ISReloadableWeapon : ISReloadable
ISReloadableWeapon = ISReloadable:derive("ISReloadableWeapon");

--************************************************************************--
--** ISReloadableWeapon:initialise
--**
--************************************************************************--
function ISReloadableWeapon:initialise()

end

--************************************************************************--
--** ISReloadableWeapon:new
--**
--************************************************************************--
function ISReloadableWeapon:new()
	local o = {}
	o = ISReloadable:new();
	setmetatable(o, self)
    self.__index = self
   return o
end


--************************************************************************--
--** ISReloadableWeapon:isLoaded
--**
--** Returns whether or not the gun will fire when the mouse is next
--** clicked
--**
--************************************************************************--
function ISReloadableWeapon:isLoaded()
	return true;
end

--************************************************************************--
--** ISReloadableWeapon:fireShot
--**
--** Action performed when a shot is fired. Should typically decrease
--** the current amount of ammo in the weapon
--**
--************************************************************************--
function ISReloadableWeapon:fireShot()
	-- do nothing
end

--************************************************************************--
--** ISReloadableWeapon:canReload
--**
--** Whether the character attempting to reload has the necessary
--** prerequisites to perform the reload action. Called prior to
--** the timed action and not to be confused with isReloadValid
--**
--************************************************************************--
function ISReloadableWeapon:canReload()
	return true;
end

--************************************************************************--
--** ISReloadableWeapon:isReloadValid
--**
--** Function for the TimedAction that determines whether the reload
--** action is still valid. If the player does something that should
--** interrupt the action, this should return false
--**
--** @param char - the character performing the action. Must not be nil
--** @param square - not used
--** @param difficulty - the difficulty level
--**
--** @return true if the action may continue to be performed
--**
--************************************************************************--
function ISReloadableWeapon:isReloadValid(char, square, difficulty)
	return true;
end

--************************************************************************--
--** ISReloadableWeapon:reloadStart
--**
--** Function that should be performed upon the start of the timed action
--** Considers the difficulty and performs the corresponding difficulty
--** reload action
--**
--** @param char - the character performing the action. Must not be nil
--** @param square - not used
--** @param difficulty - the difficulty level
--**
--************************************************************************--
function ISReloadableWeapon:reloadStart(char, square, difficulty)

end


--************************************************************************--
--** ISReloadableWeapon:reloadPerform
--**
--** Function that should be performed upon successful completion of the
--** timed action. Considers the difficulty and performs the corresponding
--** reload action
--**
--** @param char - the character performing the action. Must not be nil
--** @param square - not used
--** @param difficulty - the difficulty level
--**
--************************************************************************--
function ISReloadableWeapon:reloadPerform(char, square, difficulty)
	-- do nothing
end

--************************************************************************--
--** ISReloadableWeapon:getReloadText
--**
--** Returns the string that should be displayed in the UI reload option
--**
--** @return - the string that should be displayed
--**
--************************************************************************--
function ISReloadableWeapon:getReloadText()
	return getText('ContextMenu_Reload');
end

--************************************************************************--
--** ISReloadablWeapon:canRack
--**
--** Return true if the weapon should be racked to chamber a fresh round.
--** On Easy/Normal, return false if there is already a round chambered.
--**
--************************************************************************--
function ISReloadableWeapon:canRack(chr)
	if self.emptyShellChambered == 1 then return true end
	if ReloadManager[1]:getDifficulty() < 3 or chr:getJoypadBind() ~= -1 then
		return self.roundChambered == 0 and self.currentCapacity > 0
	end
	return true
end

--************************************************************************--
--** ISReloadableWeapon:syncItemToReloadable
--**
--** Function that copies details from an Item's modData to the instance
--** of this ISReloadableWeapon
--**
--** @param item - the item from which the reloadable information
--** should be retrieved
--**
--************************************************************************--
function ISReloadableWeapon:syncItemToReloadable(item)
    ISReloadable.syncItemToReloadable(self, item)
	self.defaultAmmo = item:getAmmoType();
end

--************************************************************************--
--** ISReloadableWeapon:syncReloadableToItem
--**
--** Function that copies details from the instance of this 
--** ISReloadableWeapon to an Item's modData
--**
--** @param item - the item to which the reloadable information
--** should be copied
--**
--************************************************************************--
function ISReloadableWeapon:syncReloadableToItem(item)
	ISReloadable.syncReloadableToItem(self, item)
end

--************************************************************************--
--** ISReloadableWeapon:setupReloadable
--**
--** Function that initialises all the required modData on an item.
--**
--** @param weapon - the weapon to setup
--** @param v - the lua table containing the key value pairs to attach
--** to the modData
--************************************************************************--
function ISReloadableWeapon:setupReloadable(item, v)
	if not getCore():isNewReloading() then
		ISReloadable.setupReloadable(self, item, v);
		local modData = item:getModData();
		modData.defaultAmmo = item:getAmmoType();
		item:setAmmoType(nil);
		modData.defaultSwingSound = item:getSwingSound();
	end
end
