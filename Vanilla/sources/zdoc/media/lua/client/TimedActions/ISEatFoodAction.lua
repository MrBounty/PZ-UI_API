--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISEatFoodAction : ISBaseTimedAction
ISEatFoodAction = ISBaseTimedAction:derive("ISEatFoodAction");

function ISEatFoodAction:isValid()
	if self.item:getRequireInHandOrInventory() then
		if not self:getRequiredItem() then
			return false
		end
	end
	return self.character:getInventory():contains(self.item);
end

function ISEatFoodAction:update()
	self.item:setJobDelta(self:getJobDelta());
    if self.eatSound ~= "" and self.eatAudio ~= 0 and not self.character:getEmitter():isPlaying(self.eatAudio) then
        self.eatAudio = self.character:getEmitter():playSound(self.eatSound);
--        self.eatAudio = getSoundManager():PlayWorldSoundWav( self.eatSound, self.character:getCurrentSquare(), 0.5, 2, 0.5, true);
    end
end

function ISEatFoodAction:start()
	if self.eatSound ~= '' then
        self.eatAudio = self.character:getEmitter():playSound(self.eatSound);
--		self.eatAudio = getSoundManager():PlayWorldSoundWav( self.eatSound, self.character:getCurrentSquare(), 0.5, 2, 0.5, true);
    end
	if self.item:getCustomMenuOption() then
		self.item:setJobType(self.item:getCustomMenuOption())
	else
		self.item:setJobType(getText("ContextMenu_Eat"));
	end
	self.item:setJobDelta(0.0);

	local secondItem = nil;
	if self.item:getEatType() and self.item:getEatType() ~= "" then
		-- for can or 2handed, add a fork or a spoon if we have them otherwise we'll use default eat action
		-- use 2handforced if you don't want this to happen (like eating a burger..)
		if self.item:getEatType() == "can" or self.item:getEatType() == "candrink" or self.item:getEatType() == "2hand" or self.item:getEatType() == "plate" or self.item:getEatType() == "2handbowl" then
			local spoon  = self.character:getInventory():getItemFromType("Base.Spoon");
			local fork = self.character:getInventory():getItemFromType("Base.Fork");

			if self.item:getEatType() == "2handbowl" and spoon then
				self:setAnimVariable("FoodType", "2handbowl");
				secondItem = spoon;
			elseif self.item:getEatType() == "2handbowl" then
				self:setAnimVariable("FoodType", "drink");
			else
				secondItem = spoon or fork;
				if secondItem then
					if self.item:getEatType() == "plate" then
						self:setAnimVariable("FoodType", "plate");
					else
						self:setAnimVariable("FoodType", "can");
					end
				elseif self.item:getEatType() == "2hand" then
					self:setAnimVariable("FoodType", "2hand");
				elseif self.item:getEatType() == "plate" then
					self:setAnimVariable("FoodType", "plate");
				elseif self.item:getEatType() == "candrink" then
					self:setAnimVariable("FoodType", "drink");
				end
			end
		else
			self:setAnimVariable("FoodType", self.item:getEatType());
		end
	end
	self:setOverrideHandModels(secondItem, self.item);
	if self.item:getEatType() == "Pot" then
		self:setOverrideHandModels(self.item, nil);
	end
	if self.item:getCustomMenuOption() == getText("ContextMenu_Drink") and self.item:getEatType() ~= "2handbowl" then
		self:setActionAnim(CharacterActionAnims.Drink);
	else
		self:setActionAnim(CharacterActionAnims.Eat);
	end
	self.character:reportEvent("EventEating");
end

function ISEatFoodAction:stop()
    ISBaseTimedAction.stop(self);
	if self.eatAudio ~= 0 and self.character:getEmitter():isPlaying(self.eatAudio) then
		self.character:stopOrTriggerSound(self.eatAudio);
	end
    self.item:setJobDelta(0.0);
	local applyEat = true;
	if self.item and self.item:getFullType()=="Base.Cigarettes" then
		applyEat = false; -- dont apply cigarette effects when action cancelled.
	end
    if applyEat and self.character:getInventory():contains(self.item) then
		self:eat(self.item, self:getJobDelta());
    end
end

function ISEatFoodAction:perform()
    if self.eatAudio ~= 0 and self.character:getEmitter():isPlaying(self.eatAudio) then
        self.character:stopOrTriggerSound(self.eatAudio);
    end
    if self.item:getHungChange() ~= 0 then
        -- This is now a looping sound, don't play it here
--        self.character:getEmitter():playSound("Swallowing");
    end
    self.item:getContainer():setDrawDirty(true);
    self.item:setJobDelta(0.0);
    self.character:Eat(self.item, self.percentage);
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISEatFoodAction:getRequiredItem()
	if not self.item:getRequireInHandOrInventory() then
		return
	end
	local types = self.item:getRequireInHandOrInventory()
	for i=1,types:size() do
		local fullType = moduleDotType(self.item:getModule(), types:get(i-1))
		local item2 = self.character:getInventory():getFirstType(fullType)
		if item2 then
			return item2
		end
	end
	return nil
end

function ISEatFoodAction:eat(food, percentage)
    -- calcul the percentage ate
    if percentage > 0.95 then
        percentage = 1.0;
    end
    percentage = self.percentage * percentage;
    self.character:Eat(self.item, percentage);
end

function ISEatFoodAction:new (character, item, percentage)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.stopOnWalk = false;
	o.stopOnRun = true;
    o.percentage = percentage;
    if not o.percentage then
        o.percentage = 1;
    end

	o.maxTime = math.abs(item:getBaseHunger() * 150 * o.percentage) * 8;

    if o.maxTime > math.abs(item:getHungerChange() * 150 * 8) then
        o.maxTime = math.abs(item:getHungerChange() * 150 * 8);
    end

	local hungerConsumed = math.abs(item:getBaseHunger() * o.percentage * 100);
	local eatingLoop = 1;
	if hungerConsumed >= 30 then
		eatingLoop = 2;
	end
	if hungerConsumed >= 80 then
		eatingLoop = 3;
	end
	
	local timerForOne = 232;
	if o.item:getCustomMenuOption() == getText("ContextMenu_Drink") then
		hungerConsumed = math.abs(item:getThirstChange() * o.percentage * 100);
		timerForOne = 171;
		if hungerConsumed >= 3 then
			eatingLoop = 2;
		end
		if hungerConsumed >= 6 then
			eatingLoop = 3;
		end
	end

	o.maxTime = timerForOne * eatingLoop;
	
	-- Cigarettes don't reduce hunger
	if hungerConsumed == 0 then o.maxTime = 460 end
	if item:getEatType() == "popcan" then
		o.maxTime = 160;
	end

    o.eatSound = item:getCustomEatSound() or "Eating";
    o.eatAudio = 0

--	local w = item:getActualWeight();
--    if w > 3 then w = 3; end;
--
--    o.maxTime = o.maxTime * w;

    o.ignoreHandsWounds = true;
	return o
end
