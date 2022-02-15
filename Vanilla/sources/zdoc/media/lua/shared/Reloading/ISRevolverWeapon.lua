require "Reloading/ISReloadableWeapon"

---@class ISRevolverWeapon : ISReloadableWeapon
ISRevolverWeapon =  ISReloadableWeapon:derive("ISRevolverWeapon");

--************************************************************************--
--** ISRevolverWeapon:initialise
--**
--************************************************************************--
function ISRevolverWeapon:initialise()

end

--************************************************************************--
--** ISRevolverWeapon:new
--**
--************************************************************************--
function ISRevolverWeapon:new()
	local o = {}
	--o.data = {}
	o = ISReloadableWeapon:new();
    setmetatable(o, self)
    self.__index = self
	return o;
end

--************************************************************************--
--** ISRevolverWeapon:isLoaded
--**
--** Returns whether or not the gun will fire when the mouse is next
--** clicked
--**
--************************************************************************--
function ISRevolverWeapon:isLoaded()
	return true;
end

--************************************************************************--
--** ISRevolverWeapon:isLoaded
--**
--** Action performed when a shot is fired. Should typically decrease
--** the current amount of ammo in the weapon
--**
--************************************************************************--
function ISRevolverWeapon:fireShot()
	self.chambers[currentChamber] = 0;
	self.rotateCylinder();
end

--************************************************************************--
--** ISRevolverWeapon:canReload
--**
--** Whether the character attempting to reload has the necessary
--** prerequisites to perform the reload action. Called prior to
--** the timed action and not to be confused with isReloadValid
--**
--************************************************************************--
function ISRevolverWeapon:canReload(chr)
	local chamberIsFull = true;
	-- loop through the chambers and check whether
	-- any are empty
	for t = 1, 6 do
		if(self.chambers[t] == self.EMPTY) then
			chamberIsFull = false;
			t = t+1;
		end
		if(chamberIsFull == false) then
			return true;
		end
	end
end

--************************************************************************--
--** ISRevolverWeapon:isReloadValid
--**
--** Function for the TimedAction that determines whether the reload
--** action is still valid. If the player does something that should
--** interrupt the action, this should return false
--**
--** @param char - the character performing the action. Must not be nil
--** @param square - not used
--**
--** @return true if the action may continue to be performed
--**
--************************************************************************--
function ISRevolverWeapon:isReloadValid(char, square)
	local weapon = char:getPrimaryHandItem();
	if(weapon ~= nil) then
		if(char:getInventory():FindAndReturn(getAmmoType(weapon)) ~= nil) then
			return true;
		end
	end
	return false;
end

--************************************************************************--
--** ISRevolverWeapon:reloadStart
--**
--** Function that should be performed upon the start of the timed action
--**
--** @param char - the character performing the action. Must not be nil
--** @param square - not used
--**
--************************************************************************--
function ISRevolverWeapon:reloadStart(char, square)
	reloadInProgress = true;
end

--************************************************************************--
--** ISRevolverWeapon:reloadPerform
--**
--** Function that should be performed upon successful completion of the
--** timed action
--**
--** @param char - the character performing the action. Must not be nil
--** @param square - not used
--**
--************************************************************************--
function ISRevolverWeapon:reloadPerform(char, square)
	local weapon = char:getPrimaryHandItem();
    char:playSound(self.insertRoundSound)
--	getSoundManager():PlayWorldSound(self.insertRoundSound, char:getSquare(), 0, 10, 1.0, false);
	local roundHasBeenInserted = false;
	while (roundHasBeenInserted == false) do
		if(self.chambers[self.currentChamber] == self.EMPTY) then
			setCylinderChamber(weapon, getCurrentCapacity(weapon), self.LIVE);
			roundHasBeenInserted = true;
		end
		self:rotateCylinder(weapon);
	end
	-- remove the necessary ammo
	char:getInventory():RemoveOneOf(self.ammoType);
	reloadInProgress = false;
end

--************************************************************************--
--** ISRevolverWeapon:rotateCylinder
--**
--** Increases the currentChamber by 1 making sure it never exceeds 6.
--** Attempting to increase past six restores the currentChamber value
--** to 1
--**
--************************************************************************--
function ISRevolverWeapon:rotateCylinder()
	self.currentChamber = self.currentChamber + 1;
	if(self.currentChamber > 6) then
		self.currentChamber = 1;
	end
end

--************************************************************************--
--** ISRevolverWeapon:spinCylinder
--**
--** Sets the currentChamber a random number between 1 and 6
--**
--************************************************************************--
function ISRevolverWeapon:spinCylinder()
	self.currentChamber = ZombRand(1, 6);
end
