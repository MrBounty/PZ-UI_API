require "Reloading/ISReloadableWeapon"
require "Reloading/ISReloadableMagazine"

---@class ISSemiAutoWeapon : ISReloadableWeapon
ISSemiAutoWeapon =  ISReloadableWeapon:derive("ISSemiAutoWeapon");

--************************************************************************--
--** ISSemiAutoWeapon:initialise
--**
--************************************************************************--
function ISSemiAutoWeapon:initialise()

end

--************************************************************************--
--** ISSemiAutoWeapon:new
--**
--************************************************************************--
function ISSemiAutoWeapon:new()
	local o = {}
	--o.data = {}
	o = ISReloadableWeapon:new();
    setmetatable(o, self)
    self.__index = self
	o.removeClipText = getText('ContextMenu_EjectMagazine');
	o.insertClipText = getText('ContextMenu_InsertMagazine');
	o.reloadText = getText('ContextMenu_Reload');
	return o;
end

--************************************************************************--
--** ISSemiAutoWeapon:isLoaded
--**
--** Returns whether or not the gun will fire when the mouse is next
--** clicked
--**
--************************************************************************--
function ISSemiAutoWeapon:isLoaded(difficulty)
	return self.roundChambered == 1
end

--************************************************************************--
--** ISSemiAutoWeapon:fireShot
--**
--** Action performed when a shot is fired. Should typically decrease
--** the current amount of ammo in the weapon
--**
--************************************************************************--
function ISSemiAutoWeapon:fireShot(weapon, difficulty)
	self.roundChambered = 0;
	if self.currentCapacity > 0 then
		self.currentCapacity = self.currentCapacity - 1;
		self.roundChambered = 1;
	end
	self:syncReloadableToItem(weapon)
end

--************************************************************************--
--** ISSemiAutoWeapon:canReload
--**
--** Whether the character attempting to reload has the necessary
--** prerequisites to perform the reload action. Called prior to
--** the timed action and not to be confused with isReloadValid
--**
--************************************************************************--
function ISSemiAutoWeapon:canReload(chr)
	if(difficulty == 1) then
		if(chr:getInventory():FindAndReturn(ReloadUtil:getClipData(self.ammoType).ammoType)) then
			return true;
		end
	elseif(self.containsClip == 1 or chr:getInventory():FindAndReturn(self.ammoType) ~= nil) then
		return true;
	end
	return false;
end

--************************************************************************--
--** ISSemiAutoWeapon:isReloadValid
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
function ISSemiAutoWeapon:isReloadValid(char, square, difficulty)
	if(difficulty == 3) then
		return self:isReloadValidHard(char, square);
	elseif(difficulty == 1) then
		return self:isReloadValidEasy(char, square);
	else
		return self:isReloadValidNormal(char, square);
	end
end

function ISSemiAutoWeapon:isReloadValidEasy(char, square)
	if self.maxCapacity == self.currentCapacity then
		return false
	end
	if(char:getInventory():FindAndReturn(ReloadUtil:getClipData(self.ammoType).ammoType)) then
		return true;
	end
	return false;
end

function ISSemiAutoWeapon:isReloadValidNormal(char, square)
		-- TODO make sure the player still has the weapon on their person
	if(self.containsClip == 1
	or char:getInventory():FindAndReturn(self.ammoType) ~= nil) then
		return true;
	end
	self.reloadInProgress = false;
	return false;
end

function ISSemiAutoWeapon:isReloadValidHard(char, square)
	return true;
end

--************************************************************************--
--** ISSemiAutoWeapon:reloadStart
--**
--** Function that should be performed upon the start of the timed action
--** Considers the difficulty and performs the corresponding reload
--** action
--**
--** @param char - the character performing the action. Must not be nil
--** @param square - not used
--** @param difficulty - the difficulty level
--**
--************************************************************************--
function ISSemiAutoWeapon:reloadStart(char, square, difficulty)
	if(difficulty == 1) then
		self:reloadStartEasy(char, square);
	else
		self:reloadStartNormal(char, square);
	end
end


function ISSemiAutoWeapon:reloadStartEasy(char, square)
	self.reloadInProgress = true;
--	getSoundManager():PlayWorldSound(self.ejectSound, char:getSquare(), 0, 10, 1.0, false);
    char:playSound(self.ejectSound)
end

function ISSemiAutoWeapon:reloadStartNormal(char, square)
	self.reloadInProgress = true;
	if(self.containsClip == 1) then
--		getSoundManager():PlayWorldSound(self.ejectSound, char:getSquare(), 0, 10, 1.0, false);
        char:playSound(self.ejectSound)
	else
--		getSoundManager():PlayWorldSound(self.insertSound, char:getSquare(), 0, 10, 1.0, false);
        char:playSound(self.insertSound)
	end
end

--************************************************************************--
--** ISSemiAutoWeapon:reloadPerform
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
function ISSemiAutoWeapon:reloadPerform(char, square, difficulty, weapon)
	if(difficulty == 1) then
		self:reloadPerformEasy(char, square, weapon)
	else
		self:reloadPerformNormal(char, square, weapon)
	end
end

function ISSemiAutoWeapon:reloadPerformEasy(char, square, weapon)
	-- Does find and return give an abilty to determine size of stack?
	local amountToRemove = self.maxCapacity - self.currentCapacity;
	local inventoryAmmoCount = 0;
	local amountRemoved = 0;
	while((char:getInventory():FindAndReturn(ReloadUtil:getClipData(self.ammoType).ammoType) ~= nil)
	and amountRemoved < amountToRemove) do
		char:getInventory():RemoveOneOf(ReloadUtil:getClipData(self.ammoType).ammoType);
        ISInventoryPage.dirtyUI();
		amountRemoved = amountRemoved + 1;
	end
	self.currentCapacity = self.currentCapacity + amountRemoved;
--	getSoundManager():PlayWorldSound(self.insertSound, char:getSquare(), 0, 10, 1.0, false);
    char:playSound(self.insertSound)
	self:syncReloadableToItem(weapon);
	char:getXp():AddXP(Perks.Reloading, 1);
end

function ISSemiAutoWeapon:reloadPerformNormal(char, square, weapon)
	if(self.containsClip == 1) then
		-- we are ejecting the current clip
		local clip = self:createMagazine();
		self.currentCapacity = 0;
		char:getInventory():AddItem(clip);
        ISInventoryPage.dirtyUI();
		self.containsClip = 0;
		self.reloadInProgress = false;
	else
		-- we are insterting a new clip
		local clip = nil;
		local mostAmmo = -1;
        local items = char:getInventory():getItems();
		-- look for the magazine in the inventory with the most ammo
        for i = 0, items:size()-1 do
            local currentItem = items:get(i);
			-- This may be the first time the item is used
			-- best call setupMagazine and see if it's a clip
			local requiredClipData = ReloadUtil:getClipData(self.ammoType)
			local currentReloadable = ReloadUtil:setupMagazine(currentItem, requiredClipData, char);
			-- Was the item a clip?
			if(currentReloadable ~= nil) then
				if(currentReloadable.clipType == requiredClipData.clipType) then
				    -- check the amount of ammo
					if(currentReloadable.currentCapacity > mostAmmo) then
						clip = currentItem;
						mostAmmo = currentReloadable.currentCapacity;
					end
				end
			end
		end
		if (clip ~= nil) then
			self.currentCapacity = clip:getModData().currentCapacity;
			char:getInventory():Remove(clip);
            ISInventoryPage.dirtyUI();
			self.reloadInProgress = false;
			self.containsClip = 1;
			char:getXp():AddXP(Perks.Reloading, 1);
		end
	end
	self:syncReloadableToItem(weapon);
	return false;
end

--************************************************************************--
--** ISSemiAutoWeapon:rackingStart
--**
--** Function that should be performed upon the start of the timed action
--**
--** @param char - the character performing the action. Must not be nil
--** @param square - not used
--**
--************************************************************************--
function ISSemiAutoWeapon:rackingStart(char, square, weapon)
--    getSoundManager():PlayWorldSound(self.rackSound, char:getSquare(), 0, 10, 1.0, false);
    char:playSound(self.rackSound)
end

--************************************************************************--
--** ISSemiAutoWeapon:rackingPerform
--**
--** Function that should be performed upon successful completion of the
--** timed action
--**
--** @param char - the character performing the action. Must not be nil
--** @param square - not used
--** @param weapon - the item being reloaded
--**
--************************************************************************--
function ISSemiAutoWeapon:rackingPerform(char, square, weapon)
	if self.roundChambered == 1 and char:getCurrentSquare() then
		local clipData = ReloadUtil:getClipData(self.ammoType)
		local round = InventoryItemFactory.CreateItem(clipData.moduleName .. '.' .. clipData.ammoType)
		if round then
			char:getCurrentSquare():AddWorldInventoryItem(round, 0, 0, 0)
			ISInventoryPage.dirtyUI()
		end
	end
    self.roundChambered = 0;
    if(self.currentCapacity > 0) then
        self.currentCapacity = self.currentCapacity - 1;
        self.roundChambered = 1;
	end
	self:syncReloadableToItem(weapon);
end

--************************************************************************--
--** ISSemiAutoWeapon:getRackTime
--**
--** Returns the time take to perform the reload action
--**
--************************************************************************--
function ISSemiAutoWeapon:getRackTime()
	return self.rackTime;
end

--************************************************************************--
--** ISSemiAutoWeapon:createMagazine
--**
--** Creates a new magazine for this weapon type containing as many rounds
--** as there are in this weapon
--**
--************************************************************************--
function ISSemiAutoWeapon:createMagazine()
--	local magazine = InventoryItemFactory.CreateItem(self.moduleName, self.clipName, self.ammoType, self.clipIcon);
	local magazine = InventoryItemFactory.CreateItem(self.moduleName .. '.' .. self.ammoType);
	self:setupMagazine(magazine);
	magazine:getModData().currentCapacity = self.currentCapacity;
	return magazine;
end

--************************************************************************--
--** ISSemiAutoWeapon:setupMagazine
--**
--** Sets up the ISReloadableMagazine on the InventoryItem
--**
--** @param magazine the InventoryItem to add the ISReloadableMagazine to
--**
--************************************************************************--
function ISSemiAutoWeapon:setupMagazine(magazine)
	local magazineData = ReloadUtil:getClipData(self.ammoType)
	ReloadUtil:setupMagazine(magazine, magazineData, self.playerObj);
end

--************************************************************************--
--** ISSemiAutoWeapon:getReloadText
--**
--** Returns the string that should be displayed in the UI reload option
--**
--** @return - the string that should be displayed
--**
--************************************************************************--
function ISSemiAutoWeapon:getReloadText()
	if(ReloadManager[1]:getDifficulty() == 1) then
		return self.reloadText;
	end
	if(self.containsClip == 1) then
		return self.removeClipText;
	else
		return self.insertClipText;
	end;
end

--************************************************************************--
--** ISSemiAutoWeapon:isChainReloading
--**
--** Returns true if a reload action should be performed immediately after
--** this one. Does not take into account ammunition availability
--**
--************************************************************************--
function ISSemiAutoWeapon:isChainReloading()
	return false;
end

--************************************************************************--
--** ISSemiAutoWeapon:getReloadTime
--**
--** Returns the time take to perform a single reload action
--**
--************************************************************************--
function ISSemiAutoWeapon:getReloadTime()
	if(ReloadManager[1]:getDifficulty() == 1) then
		return self.reloadTime*2;
	end
	return self.reloadTime;
end

--************************************************************************--
--** ISSemiAutoWeapon:syncItemToReloadable
--**
--** Function that copies details from an Item's modData to the instance
--** of this ISSemiAutoWeapon
--**
--** @param weapon - the weapon from which the reloadable information
--** should be retrieved
--**
--************************************************************************--
function ISSemiAutoWeapon:syncItemToReloadable(weapon)
	local modData = weapon:getModData();
	 -- handle switching difficulty
	if ReloadManager[1]:getDifficulty() == 1 then
		modData.containsClip = 1
	end
	ISReloadableWeapon.syncItemToReloadable(self, weapon);
	self.roundChambered = modData.roundChambered;
	self.containsClip = modData.containsClip;
	self.clipName = modData.clipName;
	self.clipIcon = modData.clipIcon;
end

--************************************************************************--
--** ISSemiAutoWeapon:syncReloadableToItem
--**
--** Function that copies details from the instance of this
--** ISSemiAutoWeapon to an Item's modData
--**
--** @param weapon - the weapon to which the reloadable information
--** should be copied
--**
--************************************************************************--
function ISSemiAutoWeapon:syncReloadableToItem(weapon)
	 -- handle switching difficulty
	if ReloadManager[1]:getDifficulty() == 1 then
		self.containsClip = 1
	end
	ISReloadableWeapon.syncReloadableToItem(self, weapon);
	local modData = weapon:getModData();
	modData.roundChambered = self.roundChambered;
	modData.containsClip = self.containsClip;
end

--************************************************************************--
--** ISSemiAutoWeapon:setupReloadable
--**
--** Function that initialises all the required modData on an item.
--**
--** @param weapon - the weapon to setup
--** @param v - the lua table containing the key value pairs to attach
--** to the modData
--************************************************************************--
function ISSemiAutoWeapon:setupReloadable(weapon, v)
	ISReloadableWeapon.setupReloadable(self, weapon, v);
	local modData = weapon:getModData();
	modData.roundChambered = 0;
	modData.containsClip = 1;
	modData.clipName = v.clipName;
	modData.clipIcon = v.clipIcon;
end

function ISSemiAutoWeapon:printWeaponDetails(item)
    self:printItemDetails(item);
    local modData = item:getModData();
    local outString  = '';
    if(modData.roundChambered ~= nil) then
        outString = outString..', roundChambered: '..modData.roundChambered;
    else
        outString = outString..', roundChambered == nil';
    end
    if(modData.containsClip ~= nil) then
        outString = outString..', containsClip: '..modData.containsClip;
    else
        outString = outString..', containsClip == nil';
    end
    if(modData.clipName ~= nil) then
        outString = outString..', clipName: '..modData.clipName;
    else
        outString = outString..', clipName == nil';
    end
    if(modData.clipIcon ~= nil) then
        outString = outString..', clipIcon: '..modData.clipIcon;
    else
        outString = outString..', clipIcon == nil';
    end
    print(outString);
    print('***************************************************************');
    print();
    print();
end


function ISSemiAutoWeapon:printReloadableWeaponDetails()
    self:printReloadableDetails();
    local outString  = '';
    if(self.roundChambered ~= nil) then
        outString = outString..', roundChambered: '..self.roundChambered;
    else
        outString = outString..', roundChambered == nil';
    end
    if(self.containsClip ~= nil) then
        outString = outString..', containsClip: '..self.containsClip;
    else
        outString = outString..', containsClip == nil';
    end
    if(self.clipName ~= nil) then
        outString = outString..', clipName: '..self.clipName;
    else
        outString = outString..', clipName == nil';
    end
    if(self.clipIcon ~= nil) then
        outString = outString..', clipIcon: '..self.clipIcon;
    else
        outString = outString..', clipIcon == nil';
    end
    print(outString);
    print('***************************************************************');
    print();
    print();
end
