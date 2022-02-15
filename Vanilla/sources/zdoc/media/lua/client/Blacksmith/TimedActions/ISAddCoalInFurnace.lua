--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISAddCoalInFurnace : ISBaseTimedAction
ISAddCoalInFurnace = ISBaseTimedAction:derive("ISAddCoalInFurnace");

function ISAddCoalInFurnace:isValid()
--	camping.updateClientCampfire(self.campfire)
	local playerInv = self.character:getInventory()
	return playerInv:contains(self.coal)
end

function ISAddCoalInFurnace:update()
	self.coal:setJobDelta(self:getJobDelta());

    self.character:setMetabolicTarget(Metabolics.HeavyDomestic);
end

function ISAddCoalInFurnace:start()
	self.coal:setJobType(getText("ContextMenu_Add_fuel_to_fire"));
	self.coal:setJobDelta(0.0);
end

function ISAddCoalInFurnace:stop()
	ISBaseTimedAction.stop(self);
    self.coal:setJobDelta(0.0);
    self.furnace:syncFurnace();
end

function ISAddCoalInFurnace:perform()
	self.coal:getContainer():setDrawDirty(true);
    self.coal:setJobDelta(0.0);
    local use = math.floor(self.coal:getUsedDelta()/self.coal:getUseDelta());
    for i=0,use do
        if self.furnace:getFuelAmount() == 100 then
            break;
        end
        self.furnace:addFuel(10);
        self.coal:Use();
    end
    self.furnace:syncFurnace();
--	camping.lightMyFire(self.campfire, self.character, self.lighter, self.petrol)
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISAddCoalInFurnace:new(furnace, coal, character)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = 100;
	-- custom fields
	o.furnace = furnace
	o.coal = coal;
	return o;
end
