--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISAddCompost : ISBaseTimedAction
ISAddCompost = ISBaseTimedAction:derive("ISAddCompost")

local COMPOST_PER_BAG = 10
local SCRIPT_ITEM = ScriptManager.instance:FindItem("Base.CompostBag")
local USES_PER_BAG = 1.0 / SCRIPT_ITEM:getUseDelta()
local COMPOST_PER_USE = COMPOST_PER_BAG / USES_PER_BAG

function ISAddCompost:isValid()
	return self.compost:getCompost() + COMPOST_PER_USE <= 100 and
		self.character:getInventory():contains(self.item) and
		self.item:getDrainableUsesInt() > 0
end

function ISAddCompost:update()
	self.character:faceThisObject(self.compost)
	self.character:setMetabolicTarget(Metabolics.HeavyWork)
end

function ISAddCompost:start()
	self:setActionAnim(CharacterActionAnims.Pour)
	self:setOverrideHandModels(self.item, nil)
end

function ISAddCompost:stop()
	ISBaseTimedAction.stop(self)
end

function ISAddCompost:perform()
	local amount = math.min(100 - self.compost:getCompost())
	local uses = math.floor(amount / COMPOST_PER_USE)
	uses = math.min(uses, self.item:getDrainableUsesInt())
	for i=1,uses do
		self.item:Use()
		self.compost:setCompost(self.compost:getCompost() + COMPOST_PER_USE)
	end
	self.compost:updateSprite()
	if isClient() then
		self.compost:syncCompost()
	end
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISAddCompost:new(character, compost, item)
	local o = ISBaseTimedAction.new(self, character)
	o.compost = compost
	o.item = item
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = 150
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	return o
end	
