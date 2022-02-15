--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISLockDoor : ISBaseTimedAction
ISLockDoor = ISBaseTimedAction:derive("ISLockDoor");

function ISLockDoor:isValid()
	local keyId = instanceof(self.door, "IsoDoor") and self.door:checkKeyId() or self.door:getKeyId()
	if self.character:getInventory():haveThisKeyId(keyId) then return true end
	if self.door:getProperties():Is("forceLocked") then return false end
	return not self.character:getCurrentSquare():Is(IsoFlagType.exterior)
end

function ISLockDoor:update()
end

function ISLockDoor:start()
	self.character:faceThisObject(self.door)
end

function ISLockDoor:stop()
	if not self:isValid() then
		self.character:faceThisObject(self.door)
		self.character:getEmitter():playSound("DoorIsLocked")
	end
    ISBaseTimedAction.stop(self);
end

function ISLockDoor:getSoundPrefix()
	local sprite = self.door:getSprite()
	if not sprite then return end
	if sprite:getProperties():Is("DoorSound") then
		return sprite:getProperties():Val("DoorSound")
	end
	return "WoodDoor"
end

function ISLockDoor:perform()
	local soundPrefix = self:getSoundPrefix()
    if self.lock then
        self.door:setLockedByKey(true);
        self.character:getEmitter():playSound(soundPrefix .. "Lock");
--        getSoundManager():PlayWorldSound("lockDoor", self.door:getSquare(), 0, 10, 0.7, true);
    else
        self.door:setLockedByKey(false);
        self.character:getEmitter():playSound(soundPrefix .. "Unlock");
--        getSoundManager():PlayWorldSound("unlockDoor", self.door:getSquare(), 0, 10, 0.7, true);
    end

	local doubleDoorObjects = buildUtil.getDoubleDoorObjects(self.door)
	for i=1,#doubleDoorObjects do
		local object = doubleDoorObjects[i]
		object:setLockedByKey(self.lock)
	end

	local garageDoorObjects = buildUtil.getGarageDoorObjects(self.door)
	for i=1,#garageDoorObjects do
		local object = garageDoorObjects[i]
		object:setLockedByKey(self.lock)
	end

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISLockDoor:new(character, door, lock)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.door = door;
    o.lock = lock
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = 0;
	return o;
end
