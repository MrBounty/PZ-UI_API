--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISPadlockAction : ISBaseTimedAction
ISPadlockAction = ISBaseTimedAction:derive("ISPadlockAction");

function ISPadlockAction:isValid()
	return true;
end

function ISPadlockAction:update()
end

function ISPadlockAction:start()
end

function ISPadlockAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISPadlockAction:perform()
    if self.lock then
        self.thump:setLockedByPadlock(true);
        self.thump:setKeyId(self.padlock:getKeyId());
        local keys = self.character:getInventory():AddItems("Base.KeyPadlock", self.padlock:getNumberOfKey());
        for i=0,keys:size()-1 do
            keys:get(i):setKeyId(self.padlock:getKeyId());
        end
        self.character:getInventory():Remove(self.padlock);
    else
        self.thump:setLockedByPadlock(false);
        local padlock = self.character:getInventory():AddItem("Base.Padlock");
        local keyToUse = self.character:getInventory():haveThisKeyId(self.thump:getKeyId());
        padlock:setNumberOfKey(1);
        padlock:setKeyId(keyToUse:getKeyId());
        keyToUse:getContainer():Remove(keyToUse);
        self.thump:setKeyId(-1);
    end
    self.pdata.lootInventory:refreshBackpacks();
    self.pdata.playerInventory:refreshBackpacks();
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISPadlockAction:new(character, thump, padlock, pdata, lock)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.thump = thump;
    o.padlock = padlock;
    o.pdata = pdata;
    o.lock = lock
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = 0;
	return o;
end
