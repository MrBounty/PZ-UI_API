--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISFinalizeDealAction : ISBaseTimedAction
ISFinalizeDealAction = ISBaseTimedAction:derive("ISFinalizeDealAction");

function ISFinalizeDealAction:isValid()
	return true;
end

function ISFinalizeDealAction:update()
end

function ISFinalizeDealAction:start()
end

function ISFinalizeDealAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISFinalizeDealAction:perform()
    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self);
--    print("FINALIZING DEAL ON " .. self.character:getUsername(), #self.itemsToReceive, #self.itemsToGive)
    for i,v in ipairs(self.itemsToReceive) do
--        print("adding", v:getName());
        self.character:getInventory():addItem(v);
    end
    for i,v in ipairs(self.itemsToGive) do
--        print("removing", v:getName());
        self.character:getInventory():Remove(v);
    end
end

function ISFinalizeDealAction:new(player, otherPlayer, itemsToGive, itemsToReceive, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = player;
    o.otherPlayer = otherPlayer;
--    print("NEW FINALIZE DEAL", player:getUsername())
	o.itemsToGive = itemsToGive;
    o.itemsToReceive = itemsToReceive;
	o.stopOnWalk = false;
	o.stopOnRun = false;
	o.maxTime = time;
	return o;
end
