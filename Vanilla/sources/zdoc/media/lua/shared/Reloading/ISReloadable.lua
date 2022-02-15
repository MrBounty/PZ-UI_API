require "ISBaseObject"

---@class ISReloadable : ISBaseObject
ISReloadable = ISBaseObject:derive("ISReloadable");

--************************************************************************--
--** ISReloadable:initialise
--**
--************************************************************************--
function ISReloadable:initialise()

end

--************************************************************************--
--** ISReloadable:new
--**
--************************************************************************--
function ISReloadable:new()
	local o = {}
	setmetatable(o, self)
    self.__index = self
   return o
end

--************************************************************************--
--** ISReloadable:isLoaded
--**
--** Returns whether or not the gun will fire when the mouse is next
--** clicked
--**
--************************************************************************--
function ISReloadable:isLoaded()
	return false;
end

--************************************************************************--
--** ISReloadable:fireShot
--**
--** Action performed when a shot is fired. Should typically decrease
--** the current amount of ammo in the weapon
--**
--************************************************************************--
function ISReloadable:fireShot()
	-- do nothing
end

--************************************************************************--
--** ISReloadable:canReload
--**
--** Whether the character attempting to reload has the necessary
--** prerequisites to perform the reload action. Called prior to
--** the timed action and not to be confused with isReloadValid
--**
--************************************************************************--
function ISReloadable:canReload(chr)
	return true;
end

--************************************************************************--
--** ISReloadable:isReloadValid
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
function ISReloadable:isReloadValid(char, square, difficulty)
	return true;
end

--************************************************************************--
--** ISReloadable:reloadStart
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
function ISReloadable:reloadStart(char, square, difficulty)

end


--************************************************************************--
--** ISReloadable:reloadPerform
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
function ISReloadable:reloadPerform(char, square, difficulty)
	return false;
end

--************************************************************************--
--** ISReloadable:getReloadText
--**
--** Returns the string that should be displayed in the UI reload option
--**
--** @return - the string that should be displayed
--**
--************************************************************************--
function ISReloadable:getReloadText()
	return getText('ContextMenu_Reload');
end

--************************************************************************--
--** ISReloadableWeapon:isChainReloading
--**
--** Returns true if a reload action should be performed immediately after
--** this one. Does not take into account ammunition availability
--**
--************************************************************************--
function ISReloadable:isChainReloading()
	return true;
end

--************************************************************************--
--** ISReloadable:getReloadTime
--**
--** Returns the time take to perform a single reload action
--**
--************************************************************************--
function ISReloadable:getReloadTime()
	return self.reloadTime;
end

--************************************************************************--
--** ISReloadable:canRack
--************************************************************************--
function ISReloadable:canRack(chr)
	return false;
end

local soundToGameSound = {
	bulletInRifle = 'BulletInRifle',
	bulletOutVarmint = 'BulletOutVarmint',
	stormyRevolverInsertRound = 'StormyRevolverInsertRound',
	pumpaction = 'ShotgunPumpAction',
	stormyShotgunClick = 'StormyShotgunClick',
	stormyRevolverInsertRound = 'StormyShotgunInsertRound',
	stormy9mmClick = 'Stormy9mmClick',
	stormy9mmClipEject = 'Stormy9mmClipEject',
	stormy9mmRack = 'Stormy9mmRack',
	stormy9mmClipLoad = 'Stormy9mmClipLoad',
}

--************************************************************************--
--** ISReloadable:syncItemToReloadable
--**
--** Function that copies details from an Item's modData to the instance
--** of this ISReloadableWeapon
--**
--** @param item - the item from which the reloadable information
--** should be retrieved
--**
--************************************************************************--
function ISReloadable:syncItemToReloadable(item)
	local modData = item:getModData();
	if(modData.reloadClass ~= nil) then
		-- Compatibility: convert old sound names to GameSound names
		for k,v in pairs(modData) do
			if soundToGameSound[v] then
				modData[k] = soundToGameSound[v]
			end
		end
		self.type = modData.type or item:getType();
		self.moduleName = modData.moduleName
		if(self.moduleName == nil) then
			self.moduleName = 'Base'
		end
		self.reloadClass = modData.reloadClass;
		self.ammoType = modData.ammoType;
		self.loadStyle = modData.reloadStyle;
		self.ejectSound = modData.ejectSound;
		self.clickSound = modData.clickSound;
		self.insertSound = modData.insertSound;
		self.rackSound = modData.rackSound;
		self.maxCapacity = modData.maxCapacity or item:getClipSize();
		self.reloadTime = modData.reloadTime or item:getReloadTime();
		self.rackTime = modData.rackTime;
		self.currentCapacity = modData.currentCapacity;
--		self.reloadText = modData.reloadText;
	end
end

--************************************************************************--
--** ISReloadable:syncReloadableToItem
--**
--** Function that copies details from the instance of this
--** ISReloadable to an Item's modData
--**
--** @param item - the item to which the reloadable information
--** should be copied
--**
--************************************************************************--
function ISReloadable:syncReloadableToItem(item)
	local modData = item:getModData();
	modData.type = self.type;
	modData.currentCapacity = self.currentCapacity;
end

--************************************************************************--
--** ISReloadable:setupReloadable
--**
--** Function that initialises all the required modData on an item.
--**
--** @param weapon - the weapon to setup
--** @param v - the lua table containing the key value pairs to attach
--** to the modData
--************************************************************************--
function ISReloadable:setupReloadable(item, v)
	local modData = item:getModData();
	modData.type = v.type;
	modData.moduleName = v.moduleName;
	modData.reloadClass = v.reloadClass;
	modData.ammoType = v.ammoType;
	modData.loadStyle = v.reloadStyle;
	modData.ejectSound = v.ejectSound;
	modData.clickSound = v.clickSound;
	modData.insertSound = v.insertSound;
	modData.rackSound = v.rackSound;
	modData.maxCapacity = v.maxCapacity or item:getClipSize();
	modData.reloadTime = v.reloadTime or item:getReloadTime();
	modData.rackTime = v.rackTime;
	modData.currentCapacity = 0;
--	modData.reloadText = v.reloadText;
end

function ISReloadable:printItemDetails(item)
    print('***************************************************************');
    print('Weapon state:');
    print('***************************************************************');
    local modData = item:getModData();
    local outString  = '';
        if(modData.type ~= nil) then
            outString = outString..', type: '..modData.type;
        else
            outString = outString..', type == nil';
        end
        if(modData.reloadClass ~= nil) then
            outString = outString..', reloadClass: '..modData.reloadClass;
        else
            outString = outString..', reloadClass == nil';
        end
		if(modData.ammoType ~= nil) then
            outString = outString..', ammoType: '..modData.ammoType;
        else
            outString = outString..', ammoType == nil';
        end
        if(modData.loadStyle ~= nil) then
            outString = outString..', loadStyle: '..modData.loadStyle;
        else
            outString = outString..', loadStyle == nil';
        end
        if(modData.ejectSound ~= nil) then
            outString = outString..', ejectSound: '..modData.ejectSound;
        else
            outString = outString..', ejectSound == nil';
        end
		if(modData.clickSound ~= nil) then
            outString = outString..', clickSound: '..modData.clickSound;
        else
            outString = outString..', clickSound == nil';
        end
        if(modData.insertSound ~= nil) then
            outString = outString..', insertSound: '..modData.insertSound;
        else
            outString = outString..', insertSound == nil';
        end
        if(modData.rackSound ~= nil) then
            outString = outString..', rackSound: '..modData.rackSound;
        else
            outString = outString..', rackSound == nil';
        end
        if(modData.maxCapacity ~= nil) then
            outString = outString..', maxCapacity: '..modData.maxCapacity;
        else
            outString = outString..', maxCapacity == nil';
        end
        if(modData.reloadTime ~= nil) then
            outString = outString..', reloadTime: '..modData.reloadTime;
        else
            outString = outString..', reloadTime == nil';
        end
        if(modData.rackTime ~= nil) then
            outString = outString..', rackTime: '..modData.rackTime;
        else
            outString = outString..', rackTime == nil';
        end
        if(modData.currentCapacity ~= nil) then
            outString = outString..', currentCapacity: '..modData.currentCapacity;
        else
            outString = outString..', currentCapacity == nil';
        end
        if(modData.reloadText ~= nil) then
            outString = outString..', reloadText: '..modData.reloadText;
        else
            outString = outString..', reloadText == nil';
        end
        print(outString);
end

function ISReloadable:printReloadableDetails()
print('***************************************************************');
    print('Reloadable state');
    print('***************************************************************');
        local outString  = '';
        if(self.type ~= nil) then
            outString = outString..', type: '..self.type;
        else
            outString = outString..', type == nil';
        end
        if(self.reloadClass ~= nil) then
            outString = outString..', reloadClass: '..self.reloadClass;
        else
            outString = outString..', reloadClass == nil';
        end
		if(self.ammoType ~= nil) then
            outString = outString..', ammoType: '..self.ammoType;
        else
            outString = outString..', ammoType == nil';
        end
        if(self.loadStyle ~= nil) then
            outString = outString..', loadStyle: '..self.loadStyle;
        else
            outString = outString..', loadStyle == nil';
        end
        if(self.ejectSound ~= nil) then
            outString = outString..', ejectSound: '..self.ejectSound;
        else
            outString = outString..', ejectSound == nil';
        end
		if(self.clickSound ~= nil) then
            outString = outString..', clickSound: '..self.clickSound;
        else
            outString = outString..', clickSound == nil';
        end
        if(self.insertSound ~= nil) then
            outString = outString..', insertSound: '..self.insertSound;
        else
            outString = outString..', insertSound == nil';
        end
        if(self.rackSound ~= nil) then
            outString = outString..', rackSound: '..self.rackSound;
        else
            outString = outString..', rackSound == nil';
        end
        if(self.maxCapacity ~= nil) then
            outString = outString..', maxCapacity: '..self.maxCapacity;
        else
            outString = outString..', maxCapacity == nil';
        end
        if(self.reloadTime ~= nil) then
            outString = outString..', reloadTime: '..self.reloadTime;
        else
            outString = outString..', reloadTime == nil';
        end
        if(self.rackTime ~= nil) then
            outString = outString..', rackTime: '..self.rackTime;
        else
            outString = outString..', rackTime == nil';
        end
        if(self.currentCapacity ~= nil) then
            outString = outString..', currentCapacity: '..self.currentCapacity;
        else
            outString = outString..', currentCapacity == nil';
        end
        if(self.reloadText ~= nil) then
            outString = outString..', reloadText: '..self.reloadText;
        else
            outString = outString..', reloadText == nil';
        end
        print(outString);
end
