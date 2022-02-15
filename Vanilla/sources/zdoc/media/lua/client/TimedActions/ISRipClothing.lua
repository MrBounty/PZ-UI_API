--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISRipClothing : ISBaseTimedAction
ISRipClothing = ISBaseTimedAction:derive("ISRipClothing");

function ISRipClothing:isValid()
	return self.character:getInventory():contains(self.item);
end

function ISRipClothing:update()
	self.item:setJobDelta(self:getJobDelta());
    self.character:setMetabolicTarget(Metabolics.HeavyDomestic);end

function ISRipClothing:start()
	self:setActionAnim("RipSheets");
	self.character:getEmitter():playSound("ClothesRipping");
	self:setOverrideHandModels(nil, nil);
	if self.isSheetRope then
		self.item:setJobType(getRecipeDisplayName("Craft Sheet Rope"));
	else
		self.item:setJobType(getRecipeDisplayName("Rip clothing"));
	end
	self.item:setJobDelta(0.0);
end

function ISRipClothing:stop()
    self.item:setJobDelta(0.0);
    ISBaseTimedAction.stop(self);
end

function ISRipClothing:perform()
    self.item:setJobDelta(0.0);
	self.character:getInventory():Remove(self.item);
	if self.isSheetRope then
		self.character:getInventory():AddItem(InventoryItemFactory.CreateItem("SheetRope"));
	else
		for i=1, tonumber(self.materials[2]) do
			local item;
			local dirty = false;
			if instanceof(self.item, "Clothing") then
				dirty = ZombRand(100) <= self.item:getDirtyness() or ZombRand(100) <= self.item:getBloodlevel();
			end
			if not dirty then
				item = InventoryItemFactory.CreateItem(self.materials[1]);
			else
				item = InventoryItemFactory.CreateItem(self.materials[1] .. "Dirty");
				if not item then item = InventoryItemFactory.CreateItem(self.materials[1]); end
			end
			self.character:getInventory():AddItem(item);
		end
		-- add thread sometimes, depending on tailoring level
		if ZombRand(7) < self.character:getPerkLevel(Perks.Tailoring) + 1 then
			local max = 2;
			if self.nbrOfCoveredParts then
				max = self.nbrOfCoveredParts;
				if max > 6 then
					max = 6;
				end
			end
			max = ZombRand(2, max);
			local thread = InventoryItemFactory.CreateItem("Base.Thread");
			for i=1, 10-max do
				thread:Use();
			end
			self.character:getInventory():AddItem(thread);
			self.character:getXp():AddXP(Perks.Tailoring, 1);
		end
	end
	
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISRipClothing:new(character, item, isSheetRope)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.recipe = ClothingRecipesDefinitions[item:getType()];
	-- either we come from clothingrecipesdefinitions or we simply check number of covered parts by the clothing and add
	if not o.recipe and ClothingRecipesDefinitions["FabricType"][item:getFabricType()] then
		o.materials = {};
		o.materials[1] = ClothingRecipesDefinitions["FabricType"][item:getFabricType()].material;
		local nbrOfCoveredParts = item:getNbrOfCoveredParts();
		local minMaterial = 2;
		local maxMaterial = nbrOfCoveredParts;
		if nbrOfCoveredParts == 1 then
			minMaterial = 1;
		end
	
		local nbr = ZombRand(minMaterial, maxMaterial + 1);
		nbr = nbr + (character:getPerkLevel(Perks.Tailoring) / 2);
		if nbr > nbrOfCoveredParts then
			nbr = nbrOfCoveredParts;
		end
		o.materials[2] = nbr;
	
		o.maxTime = nbrOfCoveredParts * 20;
		o.nbrOfCoveredParts = nbrOfCoveredParts;
	else
		o.materials = luautils.split(o.recipe.materials, ":");
		o.maxTime = tonumber(o.materials[2]) * 20;
	end
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.isSheetRope = isSheetRope;
	if o.character:isTimedActionInstant() then
		o.maxTime = 1;
	end
	return o;
end
