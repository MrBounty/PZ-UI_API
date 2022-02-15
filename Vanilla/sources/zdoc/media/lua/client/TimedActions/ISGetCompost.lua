--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISGetCompost : ISBaseTimedAction
ISGetCompost = ISBaseTimedAction:derive("ISGetCompost");

local COMPOST_PER_BAG = 10
local SCRIPT_ITEM = ScriptManager.instance:FindItem("Base.CompostBag")
local USES_PER_BAG = 1.0 / SCRIPT_ITEM:getUseDelta()
local COMPOST_PER_USE = COMPOST_PER_BAG / USES_PER_BAG

function ISGetCompost:isValid()
    return (self.compost:getCompost() >= COMPOST_PER_USE) and
        self.character:getInventory():contains(self.item);
end

function ISGetCompost:update()
    self.character:faceThisObject(self.compost)
    self.character:setMetabolicTarget(Metabolics.HeavyWork);
end

function ISGetCompost:start()
    self:setActionAnim("Loot")
    self.character:SetVariable("LootPosition", "Mid")
end

function ISGetCompost:stop()
    ISBaseTimedAction.stop(self);
end

function ISGetCompost:perform()
    local amount = self.compost:getCompost()
    local uses = math.floor(amount / COMPOST_PER_USE)
    if self.item:getType() == "CompostBag" then
        uses = math.min(uses, USES_PER_BAG - self.item:getDrainableUsesInt())
        self.item:setUsedDelta(self.item:getUsedDelta() + self.item:getUseDelta() * uses)
    else
        self.character:removeFromHands(self.item);
        self.character:getInventory():Remove(self.item);
        local compostBag = self.character:getInventory():AddItem("Base.CompostBag");
        uses = math.min(uses, USES_PER_BAG)
        compostBag:setUsedDelta(compostBag:getUseDelta() * uses);
        self.character:setPrimaryHandItem(compostBag);
    end
    self.compost:setCompost(self.compost:getCompost() - uses * COMPOST_PER_USE);
    self.compost:updateSprite();
    if isClient() then
        self.compost:syncCompost();
    end
    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self);
end

function ISGetCompost:new(character, compost, item, time)
    local o = ISBaseTimedAction.new(self, character)
    o.compost = compost;
    o.item = item;
    o.stopOnWalk = true;
    o.stopOnRun = true;
    o.maxTime = time;
    if o.character:isTimedActionInstant() then o.maxTime = 1; end
    return o;
end    
