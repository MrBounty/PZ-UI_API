require "ISBaseObject"
require "Reloading/ISReloadableMagazine"
require "Reloading/ISSemiAutoWeapon"
require "Reloading/ISShotgunWeapon"

---@class ISReloadUtil : ISBaseObject
ISReloadUtil = ISBaseObject:derive("ISReloadUtil");

--************************************************************************--
--** ISReloadUtil:initialise
--**
--************************************************************************--
function ISReloadUtil:initialise()

end

--************************************************************************--
--** ISReloadUtil:new
--**
--************************************************************************--
--************************************************************************--
--** ISReloadUtil:new
--**
--************************************************************************--
function ISReloadUtil:new()
	local o = {}
	setmetatable(o, self)
	self.__index = self
	
	if not getCore():isNewReloading() then
	o.huntingRifle = { type = "HuntingRifle",
		moduleName = 'Base',
		reloadClass = 'ISShotgunWeapon',
		ammoType = '308Bullets',
		rackSound = 'BulletOutVarmint',
		clickSound = 'StormyShotgunClick',
		insertSound = 'BulletInRifle',
		rackTime = 10 };

	o.varmintRifle = { type = "VarmintRifle",
		moduleName = 'Base',
		reloadClass = 'ISShotgunWeapon',
		ammoType = '223Bullets',
		rackSound = 'BulletOutVarmint',
		clickSound = 'StormyShotgunClick',
		insertSound = 'BulletInRifle',
		maxCapacity = 5,
		reloadTime = 10,
		rackTime = 10 };

	o.pistolClip = { type = "BerettaClip",
		moduleName = 'Base',
		reloadClass = 'ISReloadableMagazine',
		clipType = 'BerettaClip',
		ammoType = 'Bullets9mm',
		shootSound = 'none',
		clickSound = nil,
		ejectSound = 'none',
		insertSound = 'StormyRevolverInsertRound',
		rackSound = 'StormyRevolverInsertRound',
		containsClip = 0,
		maxCapacity = 15,
		reloadTime = 30,
		rackTime = 10};


	o.pistol = { type = "Pistol",
		moduleName = 'Base',
		reloadClass = 'ISSemiAutoWeapon',
		ammoType = 'BerettaClip',
		clipName = '9mm Magazine',
		clipIcon = 'BerettaClip',
		clickSound = 'Stormy9mmClick',
		ejectSound = 'Stormy9mmClipEject',
		insertSound = 'Stormy9mmClipLoad',
		rackSound = 'Stormy9mmRack',
		containsClip = 1,
		rackTime = 10,
		clipData = o.pistolClip };

	o.shotgun = { type = "Shotgun",
		moduleName = 'Base',
		reloadClass = 'ISShotgunWeapon',
		ammoType = 'ShotgunShells',
		rackSound = 'ShotgunPumpAction',
		clickSound = 'StormyShotgunClick',
		insertSound = 'StormyShotgunInsertRound',
		rackTime = 10 };

	o.sawnoff = { type = "ShotgunSawnoff",
		moduleName = 'Base',
		reloadClass = 'ISShotgunWeapon',
		ammoType = 'ShotgunShells',
		rackSound = 'ShotgunPumpAction',
		clickSound = 'StormyShotgunClick',
		insertSound = 'StormyShotgunInsertRound',
		maxCapacity = 6,
		reloadTime = 15,
		rackTime = 10};
	
	o.assaultClip = { type = "BerettaClip",
		moduleName = 'Base',
		reloadClass = 'ISReloadableMagazine',
		clipType = 'BerettaClip',
		ammoType = 'Bullets9mm',
		shootSound = 'none',
		clickSound = nil,
		ejectSound = 'none',
		insertSound = 'StormyRevolverInsertRound',
		rackSound = 'StormyRevolverInsertRound',
		containsClip = 0,
		maxCapacity = 15,
		reloadTime = 30,
		rackTime = 10};

	o.assaultRifle = { type = "AssaultRifle",
		moduleName = 'Base',
		reloadClass = 'ISSemiAutoWeapon',
		ammoType = 'BerettaClip',
		clipName = '9mm Magazine',
		clipIcon = 'BerettaClip',
		clickSound = 'Stormy9mmClick',
		ejectSound = 'Stormy9mmClipEject',
		insertSound = 'Stormy9mmClipLoad',
		rackSound = 'Stormy9mmRack',
		containsClip = 1,
		rackTime = 15,
		clipData = o.assaultClip };
	
	o.playerData = {}

	-- If you've created an item above you must add
	-- it to the relevant list here
	o.weaponsList = {o.pistol, o.shotgun, o.sawnoff, o.huntingRifle, o.varmintRifle, o.assaultRifle};
	o.clipList = {o.pistolClip, o.assaultClip};
	end
	return o;
end


--************************************************************************--
--** ISReloadUtil:setUpGun
--**
--** The role of the setupGun is two-fold. First it checks whether the
--** object passed to it has already been set up returning true if it does
--**
--** If this fails, it Loops through the weaponsList and checks whether
--** there is a corresponding table for the weapon passed to it
--**
--** If there is, adds the appropriate ISReloadableWeapon to the weapon's
--** mod data and returns true.
--**
--** If both checks fail. It returns false
--**
--** New ISReloadable implementations must have conditions added to this
--** method
--**
--************************************************************************--
function ISReloadUtil:setUpGun(weapon, playerObj)
	if(weapon == nil) then
		return nil;
	end
	local itemdata = weapon:getModData();
	if itemdata.reloadClass ~= nil then
		local reloadable = self:getReloadableForPlayer(itemdata.reloadClass, playerObj);
		local weaponData = self:getWeaponData(weapon:getType())
		if reloadable and weaponData then
			weapon:getModData().rackSound = weaponData.rackSound
			reloadable.rackSound = weaponData.rackSound
		end
		if(weaponData) then
			return reloadable
		else
			return nil;
		end
	end
	local weaponData = self:getWeaponData(weapon:getType());
	if weaponData then
		local reloadable = self:getReloadableForPlayer(weaponData.reloadClass, playerObj)
		if reloadable then
			reloadable:setupReloadable(weapon, weaponData);
			return reloadable;
		end
	end
	return nil;
end

--************************************************************************--
--** ISReloadUtil:setUpMagazine
--**
--** The role of the setUpMagazine is two-fold. First it checks whether
--** the object passed to it has already been set up returning true if it
--** has
--**
--** If this fails, it Loops through the weaponsList and checks whether
--** there is a corresponding table for the weapon passed to it
--**
--** If there is, it adds the appropriate ISReloadableWeapon to the
--** weapon's mod data and returns true.
--**
--** If both checks fail. It returns false
--**
--** New ISReloadable implementations must have conditions added to this
--** method
--**
--** @param clipData - uses the clipData passed to it to setup the
--** magazine. If no clipData is passed to it, the ISReloadUtil searches
--** for one
--**
--************************************************************************--
function ISReloadUtil:setupMagazine(magazine, clipData, playerObj)
	if(magazine == nil) then
		return nil;
	end
	clipData = clipData or self:getClipData(magazine:getType())
	if clipData and clipData.clipType == magazine:getType() then
		local reloadable = self:getReloadableForPlayer(clipData.reloadClass, playerObj)
		if not reloadable then return nil end
		local itemdata = magazine:getModData();
		if itemdata.reloadClass == nil then
			reloadable:setupReloadable(magazine, clipData);
		end
		reloadable:syncItemToReloadable(magazine);
		return reloadable;
	end
	return nil;
end

--************************************************************************--
--** ISReloadUtil:getReloadableWeapon
--**
--** Returns the ISReloadable attached to this weapon
--**
--************************************************************************--
function ISReloadUtil:getReloadableWeapon(weapon, player)
	return self:syncItemToReloadable(weapon, player);
end

--************************************************************************--
--** ISReloadUtil:isReloadable
--**
--** Returns true if the item is reloadable. This performs a setup if
--** necessary and also checks the player's inventory for the items
--** required to reload.
--**
--************************************************************************--
function ISReloadUtil:isReloadable(item, player)
	local isReloadable = false;
	if(self:getReloadableWeapon(item, player) ~= nil) then
		isReloadable = true;
	elseif (self:setUpGun(item, player)) then
		isReloadable = true;
	elseif(self:setupMagazine(item, nil, player)) then
		isReloadable = true;
	end
	if(isReloadable == true) then
		local reloadable = self:getReloadableWeapon(item, player);
		return reloadable:canReload(player);
	end
	return false;
end

--************************************************************************--
--** ISReloadUtil:getReloadText
--**
--** Returns ui reload text
--**
--************************************************************************--
function ISReloadUtil:getReloadText(item, player)
	return self:getReloadableWeapon(item, player):getReloadText();
end

--************************************************************************--
--** ISReloadUtil:canReload
--**
--** Returns true if the items canReload conditions are met
--**
--************************************************************************--
function ISReloadUtil:canReload(item)
	return item:getModData().reloadableWeapon:canReload(ReloadManager[1]:getDifficulty());
end

--************************************************************************--
--** ISReloadUtil:syncItemToReloadable
--**
--** Sets the information on the appropriate ISReloadable with the values
--** found on the corresponding item.
--**
--** If a new ISReloadable is implemented, a condition must be added to
--** this method
--**
--************************************************************************--
function ISReloadUtil:syncItemToReloadable(item, player)


	local reloadable_g = self:setUpGun(item, player);
	local reloadable_m = self:setupMagazine(item, nil, player);
	local reloadable = nil;

	if (reloadable_g ~= nil) then
		reloadable = reloadable_g;
	else
		reloadable = reloadable_m;
	end

	if reloadable then
		reloadable:syncItemToReloadable(item);
	end
	return reloadable;
end

--************************************************************************--
--** ISReloadUtil:getWeaponData
--**
--************************************************************************--
function ISReloadUtil:getWeaponData(weaponType)


	for i,v in ipairs(self.weaponsList) do
		if(v.type == weaponType) then
			return v;
		end
	end

	return nil;
end

--************************************************************************--
--** ISReloadUtil:getClipData
--**
--************************************************************************--
function ISReloadUtil:getClipData(magazineType)
	for i,v in ipairs(self.clipList) do
		if(v.clipType == magazineType) then
			return v;
		end
	end
	return nil;
end

--************************************************************************--
--** ISReloadUtil:getReloadableForPlayer
--**
--************************************************************************--
function ISReloadUtil:getReloadableForPlayer(reloadClass, playerObj)
	if not self.playerData[playerObj] then
		self.playerData[playerObj] = {}
	end
	if not self.playerData[playerObj][reloadClass] then
		local classDef = _G[reloadClass] -- finds ISShotgunWeapon etc in the globals table
		if not classDef then
			print('ERROR: no such ISReloadable-derived class "' .. reloadClass .. '"')
			return nil
		end
		self.playerData[playerObj][reloadClass] = classDef:new()
		self.playerData[playerObj][reloadClass].playerObj = playerObj
	end
	return self.playerData[playerObj][reloadClass]
end

function ISReloadUtil:addWeaponType(weaponType)
	table.insert(self.weaponsList, weaponType)
end

function ISReloadUtil:addMagazineType(magazineType)
	table.insert(self.clipList, magazineType)
end
