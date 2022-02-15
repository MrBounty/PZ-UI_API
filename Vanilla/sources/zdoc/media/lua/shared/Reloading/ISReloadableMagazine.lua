require "Reloading/ISReloadable"

---@class ISReloadableMagazine : ISReloadable
ISReloadableMagazine = ISReloadable:derive("ISReloadableMagazine");

--************************************************************************--
--** ISReloadableMagazine:initialise
--**
--************************************************************************--
function ISReloadableMagazine:initialise()

end

--************************************************************************--
--** ISReloadableMagazine:new
--**
--************************************************************************--
function ISReloadableMagazine:new()
	local o = {}
	--o.data = {}
	o = ISReloadable:new();
    setmetatable(o, self);
    self.__index = self;
	return o;
end

--************************************************************************--
--** ISReloadableMagazine:canReload
--**
--** Whether the character attempting to reload has the necessary
--** prerequisites to perform the reload action. Called prior to
--** the timed action and not to be confused with isReloadValid
--**
--************************************************************************--
function ISReloadableMagazine:canReload(chr)
	if(self.currentCapacity < self.maxCapacity
	and chr:getInventory():FindAndReturn(self.ammoType) ~= nil) then
		return true;
	end
	return false;
end

--************************************************************************--
--** ISReloadableMagazine:isReloadValid
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
function ISReloadableMagazine:isReloadValid(char, square, difficulty)
	if(self.currentCapacity < self.maxCapacity
		and char:getInventory():FindAndReturn(self.ammoType) ~= nil) then
			return true;
	end
	self.reloadInProgress = false;
	return false;
end

--************************************************************************--
--** ISReloadableMagazine:reloadStart
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
function ISReloadableMagazine:reloadStart(char, square, difficulty)
	self.reloadInProgress = true;
end

--************************************************************************--
--** ISShotgunWeapon:reloadPerform
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
function ISReloadableMagazine:reloadPerform(char, square, difficulty, magazine)
--	getSoundManager():PlayWorldSound(self.insertSound, char:getSquare(), 0, 10, 1.0, false);
    char:playSound(self.insertSound);
	self.currentCapacity = self.currentCapacity + 1;
	-- remove the necessary ammo
	char:getInventory():RemoveOneOf(self.ammoType);
	self.reloadInProgress = false;
	self:syncReloadableToItem(magazine);
	if(self.currentCapacity == self.maxCapacity) then
		return false;
	end
	return true;
end

--************************************************************************--
--** ISReloadableMagazine:rackingStart
--**
--** Function that should be performed upon the start of the timed action
--**
--** @param char - the character performing the action. Must not be nil
--** @param square - not used
--**
--************************************************************************--
function ISReloadableMagazine:rackingStart(char, square, weapon)
--    getSoundManager():PlayWorldSound(self.rackSound, char:getSquare(), 0, 10, 1.0, false);
    char:playSound(self.rackSound);
end

--************************************************************************--
--** ISReloadableMagazine:rackingPerform
--**
--** Function that should be performed upon successful completion of the
--** timed action
--**
--** @param char - the character performing the action. Must not be nil
--** @param square - not used
--** @param weapon - the item being reloaded
--**
--************************************************************************--
function ISReloadableMagazine:rackingPerform(char, square, weapon)
    if(self.currentCapacity > 0) then
        self.currentCapacity = self.currentCapacity - 1;
        char:getInventory():AddItem('Base.'..self.ammoType);
        ISInventoryPage.dirtyUI();
	    self:syncReloadableToItem(weapon);
	end
end

--************************************************************************--
--** ISReloadableMagazine:getRackTime
--**
--** Returns the time take to perform the reload action
--**
--************************************************************************--
function ISReloadableMagazine:getRackTime()
	return self.rackTime;
end

--************************************************************************--
--** ISReloadableMagazine:syncItemToReloadable
--**
--** Function that copies details from an Item's modData to the instance
--** of this ISReloadableMagazine
--**
--** @param item - the item from which the reloadable information
--** should be retrieved
--**
--************************************************************************--
function ISReloadableMagazine:syncItemToReloadable(item)
	ISReloadable.syncItemToReloadable(self, item);
	local modData = item:getModData();
	self.clipType = modData.clipType;
end

--************************************************************************--
--** ISReloadableMagazine:syncReloadableToItem
--**
--** Function that copies details from the instance of this 
--** ISReloadableMagazine to an Item's modData
--**
--** @param item - the item to which the reloadable information
--** should be copied
--**
--************************************************************************--
function ISReloadableMagazine:syncReloadableToItem(item)
	ISReloadable.syncReloadableToItem(self, item);
	local modData = item:getModData();
	modData.clipType = self.clipType;
end

--************************************************************************--
--** ISReloadableMagazine:setupReloadable
--**
--** Function that initialises all the required modData on an item.
--**
--** @param item - the item to setup
--** @param v - the lua table containing the key value pairs to attach
--** to the modData
--************************************************************************--
function ISReloadableMagazine:setupReloadable(item, v)
	ISReloadable.setupReloadable(self, item, v);
	local modData = item:getModData();
	modData.clipType = v.clipType;
end
