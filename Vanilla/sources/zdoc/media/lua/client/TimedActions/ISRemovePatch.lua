--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISRemovePatch : ISBaseTimedAction
ISRemovePatch = ISBaseTimedAction:derive("ISRemovePatch");

function ISRemovePatch:isValid()
	return self.character:getInventory():contains(self.clothing) and self.character:getInventory():contains(self.needle) and self.clothing:getPatchType(self.part) ~= nil
end

function ISRemovePatch:update()
	
end

function ISRemovePatch:start()
	self:setActionAnim(CharacterActionAnims.Craft);
end

function ISRemovePatch:stop()
    ISBaseTimedAction.stop(self);
end

function ISRemovePatch:perform()

	-- chance to get the patch back
	if ZombRand(100) < ISRemovePatch.chanceToGetPatchBack(self.character) then
		local patch = self.clothing:getPatchType(self.part);
		local fabricType = ClothingPatchFabricType.fromIndex(patch:getFabricType());
		local item = InventoryItemFactory.CreateItem(ClothingRecipesDefinitions["FabricType"][fabricType:getType()].material);
		self.character:getInventory():addItem(item);
		self.character:getXp():AddXP(Perks.Tailoring, 3);
	end
	
	self.character:getXp():AddXP(Perks.Tailoring, 1);
		
    self.clothing:removePatch(self.part);
	self.character:resetModel();
	triggerEvent("OnClothingUpdated", self.character)

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

ISRemovePatch.chanceToGetPatchBack = function(character)
	local baseChance = 10;
	baseChance = baseChance + (character:getPerkLevel(Perks.Tailoring) * 5);
	return baseChance;
end

function ISRemovePatch:new(character, clothing, part, needle)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
    o.clothing = clothing;
	o.part = part;
	o.needle = needle;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = 150 - (character:getPerkLevel(Perks.Tailoring) * 6);
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	return o;
end
