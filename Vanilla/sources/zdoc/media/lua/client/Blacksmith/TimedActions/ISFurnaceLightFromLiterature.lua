--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISFurnaceLightFromLiterature : ISBaseTimedAction
ISFurnaceLightFromLiterature = ISBaseTimedAction:derive("ISFurnaceLightFromLiterature");

function ISFurnaceLightFromLiterature:isValid()
--	camping.updateClientCampfire(self.campfire)
	return self.furnace ~= nil and
		self.character:getInventory():contains(self.lighter) and
		self.character:getInventory():contains(self.item) and
		not self.furnace:isFireStarted()
end

function ISFurnaceLightFromLiterature:update()
	self.item:setJobDelta(self:getJobDelta());
end

function ISFurnaceLightFromLiterature:start()
	self.item:setJobType(getText("ContextMenu_LitDrum"));
	self.item:setJobDelta(0.0);
end

function ISFurnaceLightFromLiterature:stop()
	ISBaseTimedAction.stop(self);
    self.item:setJobDelta(0.0);
end

function ISFurnaceLightFromLiterature:perform()
	self.item:getContainer():setDrawDirty(true);
    self.item:setJobDelta(0.0);
	self.character:getInventory():Remove(self.item);
	self.lighter:Use();
	local fuelAmt = self.fuelAmt
	if isClient() then
		local cf = self.furnace
		local args = { x = cf.x, y = cf.y, z = cf.z, fuelAmt = fuelAmt }
--		sendClientCommand(self.character, 'camping', 'lightFire', args)
	else
--		camping.addFuel(self.campfire, fuelAmt)
--		camping.lightFire(self.campfire)
        self.furnace:setFireStarted(true);
	end
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISFurnaceLightFromLiterature:new(character, item, lighter, furnace, fuelAmt, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.furnace = furnace;
	o.item = item;
	o.lighter = lighter;
	o.fuelAmt = fuelAmt;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	return o;
end
