--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISCraftAction : ISBaseTimedAction
ISCraftAction = ISBaseTimedAction:derive("ISCraftAction");

function ISCraftAction:isValid()
	return RecipeManager.IsRecipeValid(self.recipe, self.character, self.item, self.containers) and not self.character:isDriving();
end

function ISCraftAction:update()
	self.item:setJobDelta(self:getJobDelta());
	--    if self.recipe:getSound() and (not self.craftSound or not self.craftSound:isPlaying()) then
	--        self.craftSound = getSoundManager():PlayWorldSoundWav(self.recipe:getSound(), self.character:getCurrentSquare(), 0, 2, 1, true);
	--    end
	self.character:setMetabolicTarget(Metabolics.UsingTools);
end

function ISCraftAction:start()
	if self.recipe:getSound() then
		self.craftSound = self.character:getEmitter():playSound(self.recipe:getSound());
	end
	self.item:setJobType(self.recipe:getName());
	self.item:setJobDelta(0.0);
	if self.recipe:getProp1() or self.recipe:getProp2() then
		self:setOverrideHandModels(self:getPropItemOrModel(self.recipe:getProp1()), self:getPropItemOrModel(self.recipe:getProp2()))
	end
	if self.recipe:getAnimNode() then
		self:setActionAnim(self.recipe:getAnimNode());
	else
		self:setActionAnim(CharacterActionAnims.Craft);
	end

	--	self.character:reportEvent("EventCrafting");
end

function ISCraftAction:stop()
	if self.craftSound and self.character:getEmitter():isPlaying(self.craftSound) then
		self.character:getEmitter():stopSound(self.craftSound);
	end
	self.item:setJobDelta(0.0);
	ISBaseTimedAction.stop(self);
end

function ISCraftAction:perform()
	if self.craftSound and self.character:getEmitter():isPlaying(self.craftSound) then
		self.character:getEmitter():stopSound(self.craftSound);
	end
	if self.container:getType() == "floor" then
		self.fromFloor = true;
	else
		self.fromFloor = false;
	end
	self.container:setDrawDirty(true);
	self.item:setJobDelta(0.0);
	local resultItemCreated = RecipeManager.PerformMakeItem(self.recipe, self.item, self.character, self.containers);
	if resultItemCreated and instanceof(resultItemCreated, "DrainableComboItem") and self.recipe:getResult():getDrainableCount() > 0 then
		resultItemCreated:setUsedDelta(resultItemCreated:getUseDelta() * self.recipe:getResult():getDrainableCount());
	end
	if resultItemCreated and self.recipe:getResult():getCount() > 1 then
		-- FIXME: this does not call the recipe's OnCreate lua function
		local itemsAdded = self.container:AddItems(resultItemCreated:getFullType(), self.recipe:getResult():getCount());
		-- now we modify the variables of the item created, for example if you create a nailed baseball bat, it'll have the condition of the used baseball bat
		if itemsAdded and instanceof(resultItemCreated, "Food") then
			for i=0, itemsAdded:size()-1 do
				local newItem = itemsAdded:get(i);
				newItem:setCooked(resultItemCreated:isCooked());
				newItem:setRotten(resultItemCreated:isRotten());
				newItem:setBurnt(resultItemCreated:isBurnt());
				newItem:setAge(resultItemCreated:getAge());
				newItem:setHungChange(resultItemCreated:getHungChange());
				newItem:setBaseHunger(resultItemCreated:getBaseHunger());
				newItem:setBoredomChange(resultItemCreated:getBoredomChange());
				newItem:setUnhappyChange(resultItemCreated:getUnhappyChange());
				newItem:setPoisonDetectionLevel(resultItemCreated:getPoisonDetectionLevel());
				newItem:setPoisonPower(resultItemCreated:getPoisonPower());
				newItem:setCarbohydrates(resultItemCreated:getCarbohydrates());
				newItem:setLipids(resultItemCreated:getLipids());
				newItem:setProteins(resultItemCreated:getProteins());
				newItem:setCalories(resultItemCreated:getCalories());
				newItem:setTaintedWater(resultItemCreated:isTaintedWater());
				newItem:setActualWeight(resultItemCreated:getActualWeight());
				newItem:setWeight(resultItemCreated:getWeight());
				newItem:setCustomWeight(resultItemCreated:isCustomWeight());
			end
		end
		if itemsAdded and instanceof(resultItemCreated, "HandWeapon") then
			for i=0, itemsAdded:size()-1 do
				local newItem = itemsAdded:get(i);
				newItem:setCondition(resultItemCreated:getCondition());
			end
		end
		if itemsAdded and self.fromFloor then
			for i=1,itemsAdded:size() do
				self.character:getCurrentSquare():AddWorldInventoryItem(itemsAdded:get(i-1),
					(self.character:getX() - math.floor(self.character:getX())) + ZombRandFloat(0.1,0.5),
					(self.character:getY() - math.floor(self.character:getY())) + ZombRandFloat(0.1,0.5),
					self.character:getZ() - math.floor(self.character:getZ()))
				-- NOTE: AddWorldInventoryItem() sets the item's container to null
				itemsAdded:get(i-1):setContainer(self.container)
			end
		end
		if itemsAdded and not self.fromFloor then
			for i=1,itemsAdded:size() do
				self:addOrDropItem(itemsAdded:get(i-1))
			end
		end
	elseif resultItemCreated then
		if self.fromFloor then
			self.character:getCurrentSquare():AddWorldInventoryItem(resultItemCreated,
				self.character:getX() - math.floor(self.character:getX()) + ZombRandFloat(0.1,0.5),
				self.character:getY() - math.floor(self.character:getY()) + ZombRandFloat(0.1,0.5),
				self.character:getZ() - math.floor(self.character:getZ()))
			self.container:AddItem(resultItemCreated)
		else
			self:addOrDropItem(resultItemCreated)
		end
	end

	ISInventoryPage.dirtyUI()

	if self.onCompleteFunc then
		local args = self.onCompleteArgs
		self.onCompleteFunc(args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8])
	end

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISCraftAction:getPropItemOrModel(propStr)
	if not propStr then return nil end
	if propStr == "" then return nil end
	if not propStr:contains("Source=") then return propStr end
	local sourceIndex = tonumber(propStr:sub(propStr:find("=") + 1))
	if not sourceIndex or (sourceIndex < 1) or (sourceIndex > self.recipe:getSource():size()) then return nil end
	local items = RecipeManager.getSourceItemsNeeded(self.recipe, sourceIndex - 1, self.character, self.containers, self.item, nil, nil)
	if items:isEmpty() then return nil end
	-- It would be best to use the item instead of the model, so any blood/etc appears as expected.
	-- But things like flashlights have animation masks which break the "Dismantle Flashlight" animation, for example.
	-- So return the model name instead of the item.  Returning the item does work, though.
	return items:get(0):getStaticModel()
end

function ISCraftAction:addOrDropItem(item)
	local inv = self.character:getInventory()
	if not inv:contains(item) then
		inv:AddItem(item)
	end
	if inv:getCapacityWeight() > inv:getEffectiveCapacity(self.character) then
		if inv:contains(item) then
			inv:Remove(item)
		end
		self.character:getCurrentSquare():AddWorldInventoryItem(item,
			self.character:getX() - math.floor(self.character:getX()),
			self.character:getY() - math.floor(self.character:getY()),
			self.character:getZ() - math.floor(self.character:getZ()))
	end
end

function ISCraftAction:setOnComplete(func, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8)
	self.onCompleteFunc = func
	self.onCompleteArgs = { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8 }
end

function ISCraftAction:new(character, item, time, recipe, container, containers)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.recipe = recipe;
	o.container = container;
	-- If the recipe can be done from the floor, then we can use items from nearby containers.
	-- If the recipe cannot be done from the floor, then all the items must already be in the player's inventory.
	o.containers = recipe:isCanBeDoneFromFloor() and containers or nil;
	o.stopOnWalk = recipe:isStopOnWalk();
	o.stopOnRun = recipe:isStopOnRun();
	o.maxTime = time;
	if character:isTimedActionInstant() then
		o.maxTime = 1;
	end
	o.jobType = recipe:getName();
	o.forceProgressBar = true;
	return o;
end
