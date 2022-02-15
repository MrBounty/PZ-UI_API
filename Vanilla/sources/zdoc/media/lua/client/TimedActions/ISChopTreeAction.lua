--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISChopTreeAction : ISBaseTimedAction
ISChopTreeAction = ISBaseTimedAction:derive("ISChopTreeAction")

function ISChopTreeAction:isValid()
	return self.tree ~= nil and self.tree:getObjectIndex() >= 0 and
			self.character:CanAttack() and
			self.character:getPrimaryHandItem() ~= nil and
            self.character:getPrimaryHandItem():getScriptItem():getCategories():contains("Axe")
end

function ISChopTreeAction:waitToStart()
	self.character:faceThisObject(self.tree)
	return self.character:shouldBeTurning()
end

function ISChopTreeAction:update()
    self.axe:setJobDelta(self:getJobDelta());

	self.character:faceThisObject(self.tree)

    if instanceof(self.character, "IsoPlayer") then
        self.character:setMetabolicTarget(Metabolics.ForestryAxe);
    end
end

function ISChopTreeAction:start()
    self.axe = self.character:getPrimaryHandItem()
--    self.action:setTime(math.max((self.tree:getHealth() / self.axe:getTreeDamage()) * 62, 30)) --62 is as close as I can get to making the timer sync up. -Fox
    self.axe:setJobType(getText("ContextMenu_Chop_Tree"));
    self.axe:setJobDelta(0.0);

	if self.character:isTimedActionInstant() then
		self.tree:setHealth(1)
    end

    self:setActionAnim(CharacterActionAnims.Chop_tree);

    self:setOverrideHandModels(self.axe, nil)
end

function ISChopTreeAction:stop()
    self.axe:setJobDelta(0.0);

    ISBaseTimedAction.stop(self)
end

function ISChopTreeAction:perform()
    self.axe:setJobDelta(0.0);

    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self)
end

function ISChopTreeAction:animEvent(event, parameter)
	if event == 'ChopTree' then
		self.tree:WeaponHit(self.character, self.axe)
		self:useEndurance()
		if ZombRand(self.axe:getConditionLowerChance() * 2 + self.character:getMaintenanceMod() * 2) == 0 then
			self.axe:setCondition(self.axe:getCondition() - 1)
			ISWorldObjectContextMenu.checkWeapon(self.character);
		else
			self.character:getXp():AddXP(Perks.Maintenance, 1)
		end
		if self.tree:getObjectIndex() == -1 then
			self:forceComplete()
		end
	end
end

function ISChopTreeAction:useEndurance()
	if self.axe:isUseEndurance() then
		local use = self.axe:getWeight() * self.axe:getFatigueMod(self.character) * self.character:getFatigueMod() * self.axe:getEnduranceMod() * 0.1
		local useChargeDelta = 1.0
		use = use * useChargeDelta * 0.041
		if self.axe:isTwoHandWeapon() and self.character:getSecondaryHandItem() ~= self.axe then
			use = use + self.axe:getWeight() / 1.5 / 10 / 20
		end
		self.character:getStats():setEndurance(self.character:getStats():getEndurance() - use)
	end
end

function ISChopTreeAction:new(character, tree)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.tree = tree
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = -1
	o.spriteFrame = 0
    o.caloriesModifier = 8;
	o.forceProgressBar = true;
	return o
end
