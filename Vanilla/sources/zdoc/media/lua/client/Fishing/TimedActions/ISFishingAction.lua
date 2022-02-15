--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISFishingAction : ISBaseTimedAction
ISFishingAction = ISBaseTimedAction:derive("ISFishingAction");

function ISFishingAction:isValid()
	local actionQueue = ISTimedActionQueue.getTimedActionQueue(self.character)
	local lastAction = actionQueue.queue[#actionQueue.queue]
	if lastAction and (lastAction.Type ~= "ISFishingAction") then
		return false
    end
    
	if self.rod ~= self.character:getPrimaryHandItem() then return false end
	return self.lure == self.character:getSecondaryHandItem() or not self.lure;
end

function ISFishingAction:update()
    self.rod:setJobDelta(self:getJobDelta());
    -- Don't start a new strike animation close to the end of the action.
    if self:getJobDelta() < 0.75 then
        self.splashTimer = self.splashTimer - (getGameTime():getMultiplier() / 1.6);
    end
    if self.splashTimer <= 0 then
        if not self.usingSpear then
            if not self.firstCast then
                self.character:setVariable("FishingStage", "strike");
            end
            self.character:playSound("LureHitWater");
            addSound(self.character, self.character:getX(), self.character:getY(), self.character:getZ(), 20, 1)
            --        getSoundManager():PlayWorldSound("waterSplash", false, self.character:getSquare(), 1, 20, 1, false)
            self.splashTimer = ZombRand(150, 300);
        else
            self.character:setVariable("FishingStage", "spearStrike");
            self.character:playSound("StrikeWithFishingSpear");
            addSound(self.character, self.character:getX(), self.character:getY(), self.character:getZ(), 20, 1)
            self.splashTimer = ZombRand(150, 300);
        end
        self.firstCast = false;
    end

    -- add some boredom if you're not skilled
    if self.fishingLvl < 3 then
        self.character:getBodyDamage():setBoredomLevel(self.character:getBodyDamage():getBoredomLevel() + (ZomboidGlobals.BoredomDecrease * 0.01 * getGameTime():getMultiplier()))
    end

    self.character:setMetabolicTarget(Metabolics.UsingTools);
end


function ISFishingAction:start()
    self.rod:setJobType(getText("ContextMenu_Fishing"));
    self.rod:setJobDelta(0.0);
    self.pole = self.rod;
    self.usingSpear = false;
    if WeaponType.getWeaponType(self.rod) == WeaponType.spear then
        self.usingSpear = true;
        self.splashTimer = ZombRand(50, 150);
        self.character:setVariable("FishingStage", (self.stage == "cast") and "spearCast" or self.stage);
        -- The previous action did a strike at the very end.
        if self.stage == "spearStrike" then
            self.character:playSound("StrikeWithFishingSpear");
        end
    end
    if self.character:isSitOnGround() and self.usingSpear then
        self.character:setVariable("sitonground", false);
        self.character:setVariable("forceGetUp", true);
    end
    if not self.usingSpear then
        --self.character:PlayAnimUnlooped("Attack_" .. self.pole:getSwingAnim());
        --self.character:SetAnimFrame(0, false);
        --self.character:getSpriteDef():setFrameSpeedPerFrame(0.1);
        if self.stage == "cast" then
            self.splashTimer = 40 + ZombRand(10); -- splash timer is the SFX delay
            addSound(self.character, self.character:getX(), self.character:getY(), self.character:getZ(), 20, 1)
        else
            -- Finish "reel" from previous action.
            self.character:setVariable("ReelDone", false)
            self.castAfterReel = true;
            self.splashTimer = 10000;
        end
--        getSoundManager():PlayWorldSound("waterSplash", false, self.character:getSquare(), 1, 20, 1, false)
--        self.character:playSound("CastFishingLine");
        self.character:SetVariable("FishingStage", self.stage);
    end
    self.character:faceThisObject(self.tile);

    if self.character:getCurrentState()~=FishingState.instance() then
        self.character:reportEvent("EventFishing");
    end
end

function ISFishingAction:stop()
    self.character:PlayAnim("Idle");
    self.rod:setJobDelta(0.0);
    self.character:clearVariable("FishingStage")
    self.character:clearVariable("ReelDone")
    self.character:SetVariable("FishingFinished","true");
    ISBaseTimedAction.stop(self);
end

function ISFishingAction:perform()

    self.rod:setJobDelta(0.0);

    self.character:PlayAnim("Idle");

    if self.usingSpear then
        self.splashTimer = 0;
    end

    -- get the fishing zone to see how many fishes left
    local updateZone = self:getFishingZone();
    if updateZone then
        local fishLeft = tonumber(updateZone:getName());
        if getGametimeTimestamp() - updateZone:getLastActionTimestamp() > 20000 then
            fishLeft = math.max(ZombRand(10,25) + self.fishingZoneIncrease, 0);
            updateZone:setName(tostring(fishLeft));
            updateZone:setOriginalName(tostring(fishLeft));
        end
        if fishLeft == 0 then
            self.character:SetVariable("FishingFinished","true");
            -- needed to remove from queue / start next.
            ISBaseTimedAction.perform(self);
           return;
        end
    end


    local caughtFish = false;
    if self:attractFish() then -- caught something !
        local fish = self:getFish();
        if updateZone then
            local fishLeft = tonumber(updateZone:getName());
            updateZone:setName(tostring(fishLeft - 1));
            updateZone:setLastActionTimestamp(getGametimeTimestamp());
            if isClient() then updateZone:sendToServer() end
        end
        caughtFish = true;
    else
        if ZombRand(9) == 0 then -- give some xp even for a fail
            self.character:getXp():AddXP(Perks.Fishing, 1);
        end
        if self.lureProperties and ZombRand(100) <= self.lureProperties.chanceOfBreak then -- maybe remove the lure
            self.character:getSecondaryHandItem():Use();
            self.character:setSecondaryHandItem(nil);
        end
    end

    if not updateZone then -- register a new fishing zone
        local nbrOfFish = math.max(ZombRand(10,25) + self.fishingZoneIncrease, 0);
        local x,y,z = self.tile:getSquare():getX(), self.tile:getSquare():getY(), self.tile:getSquare():getZ()
        local updateZone = getWorld():registerZone(tostring(nbrOfFish), "Fishing", x - 20, y - 20, z, 40, 40);
        updateZone:setOriginalName(tostring(nbrOfFish));
        updateZone:setLastActionTimestamp(getGametimeTimestamp());
        if isClient() then updateZone:sendToServer() end
    end
    
    if self.fishingUI then
        self.fishingUI:updateZoneProgress(updateZone);
    end

    local newAction = nil;
    if not self.usingSpear then
        local lure = ISWorldObjectContextMenu.getFishingLure(self.character, self.rod)
        if lure then
            ISWorldObjectContextMenu.equip(self.character, self.character:getSecondaryHandItem(), lure:getType(), false);
            newAction = ISFishingAction:new(self.character, self.tile, self.rod, lure, self.fishingUI);
        end
    else
        newAction = ISFishingAction:new(self.character, self.tile, self.rod, nil, self.fishingUI);
    end

    if newAction then
        ISTimedActionQueue.add(newAction);
    end

    if not self.usingSpear then
        if newAction then
            if caughtFish then
                newAction.stage = "reel";
--                print(" - TRIGGER: strike (newcast & caughtfish)")
            else
                newAction.stage = "cast";
--                print(" - TRIGGER: cast (newcast & nocaught)")
            end
        else
            if caughtFish then
                self.character:SetVariable("FishingStage","strikeEnd");
--                print(" - TRIGGER: strikeEnd (nonewcast & caughtfish)")
            else
                self.character:SetVariable("FishingFinished","true");
--                print(" - TRIGGER: FishingFinished = true (nonewcast & nocaught)")
            end
        end
    else
        if newAction then
            if caughtFish then
                newAction.stage = "spearStrike";
                --                print(" - TRIGGER: strike (newcast & caughtfish)")
            else
                newAction.stage = "spearIdle";
                --                print(" - TRIGGER: cast (newcast & nocaught)")
            end
        else
            if caughtFish then
                self.character:SetVariable("FishingStage","spearStrike");
                --                print(" - TRIGGER: strikeEnd (nonewcast & caughtfish)")
            else
                self.character:SetVariable("FishingFinished","true");
                --                print(" - TRIGGER: FishingFinished = true (nonewcast & nocaught)")
            end
        end
    end

    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self);
end

function ISFishingAction:getFishingZone()
    ISFishingAction.zones_array:clear();
    local zones = getWorld():getMetaGrid():getZonesAt(self.tile:getSquare():getX(), self.tile:getSquare():getY(), self.tile:getSquare():getZ(), ISFishingAction.zones_array);
    if zones then
        for i=0,zones:size()-1 do
            if zones:get(i):getType() == "Fishing" then
                return zones:get(i);
            end
        end
    end
    return nil
end

ISFishingAction.zones_array = ArrayList.new()

function ISFishingAction.getFishingZoneFixed(x, y, z)
    ISFishingAction.zones_array:clear();
    local zones = getWorld():getMetaGrid():getZonesAt(x, y, z, ISFishingAction.zones_array);
    if zones then
        for i=0,zones:size()-1 do
            if zones:get(i):getType() == "Fishing" then
                return zones:get(i);
            end
        end
    end
    return nil
end

-- get a fish by the number
-- if plastic lure : 15/100 it's a big, 25/100 medium and 60/100 it's a little/lure fish
-- if living lure : 20/100 it's a big, 30/100 it's a medium and 50/100 it's a little/lure fish
function ISFishingAction:getFish()
    local fishItem = nil;
    local fishSizeNumber = ZombRand(100);
    local fish = {};
    -- we gonna determine the fish size and give player's xp
    -- first, if we have a plastic lure
    if self.plasticLure then
        if fishSizeNumber <= 15 then
            fish.size = "Big";
            self.character:getXp():AddXP(Perks.Fishing, 7);
        elseif fishSizeNumber <= 25 then
            fish.size = "Medium";
            self.character:getXp():AddXP(Perks.Fishing, 5);
        else
            fish.size = "Small";
            self.character:getXp():AddXP(Perks.Fishing, 3);
        end
    else -- living lure size
        if fishSizeNumber <= 20 then
            fish.size = "Big";
            self.character:getXp():AddXP(Perks.Fishing, 7);
        elseif fishSizeNumber <= 30 then
            fish.size = "Medium";
            self.character:getXp():AddXP(Perks.Fishing, 5);
        else
            fish.size = "Small";
            self.character:getXp():AddXP(Perks.Fishing, 3);
        end
    end
    fish.fish = self:getFishByLure();
    if fish.fish.name then -- if no name then it's a "trash" item
    -- then we may broke our line
        if not self:brokeLine(fish) then
            -- we gonna create our fish
            fishItem = self:createFish(fish, fish.fish);
--            getSoundManager():PlayWorldSound("getFish", false, self.character:getSquare(), 1, 20, 1, false)
            self.character:playSound("CatchFish");
            addSound(self.character, self.character:getX(), self.character:getY(), self.character:getZ(), 20, 1)
        end
    else
        fishItem = InventoryItemFactory.CreateItem(fish.fish.item);
        local inv = self:getUsedInventory(fishItem);
        inv:AddItem(fishItem);
        if not self.usingSpear then
--            getSoundManager():PlayWorldSound("getFish", false, self.character:getSquare(), 1, 20, 1, false)
            self.character:playSound("CatchTrashWithRod");
            addSound(self.character, self.character:getX(), self.character:getY(), self.character:getZ(), 20, 1)
        end
    end

    -- remove the lure
    if not self.plasticLure and self.character:getSecondaryHandItem() then
        self.character:getSecondaryHandItem():Use();
        self.character:setSecondaryHandItem(nil);
    end
    
    if self.fishingUI then
        self.fishingUI:setFish(fishItem);
    end
    
    return fish;
end

function ISFishingAction:animEvent(event, parameter)
    if event == "ReelDone" then
        if self.castAfterReel then
            self.castAfterReel = false
            self.character:setVariable("FishingStage", "cast")
            self.splashTimer = 40 + ZombRand(10);
        elseif not self.character:isVariable("FishingStage", "cast") then
            self.character:setVariable("FishingStage", "idle")
        end
    end
end

-- Visual ratio of fish caught and trash caught using player level
function ISFishingAction:getFishByLure()
    local item = 0;
    local MaxTrashRate = 0.4;
    local MinTrashRate = 0.15;
    local DampingConstant = 0.3;
    local trashRate = MaxTrashRate;
    for i = 0,self.fishingLvl do
        trashDelta = trashRate - MinTrashRate
        trashRate = trashRate - (trashDelta*DampingConstant)
    end
    if ZombRandFloat(0.0,1.0) < trashRate then
        item = Fishing.trashItems[ZombRand(#Fishing.trashItems) + 1];
    else
        item = Fishing.fishes[ZombRand(#Fishing.fishes) + 1];
    end

    for i,v in ipairs(item.lure) do
        if (self.lure and v == self.lure:getType()) or self.usingSpear then
            return item;
        end
    end
    return self:getFishByLure(); -- (could cause stack overflow if caught in infinite loop when lure is invalid for any fish)
end

-- create the fish we just get
-- we randomize is weight and size according to his size
-- then we set his new name
function ISFishingAction:createFish(fishType, fish)
    --    local fish = Fishing.fishes[fishType.fishType];
    local fishToCreate = InventoryItemFactory.CreateItem(fish.item);
    local baseWeightLb = fishToCreate:getActualWeight();
    local size = nil;
    local maxSize = nil;
    local minSize = nil;
    local weightKg = nil;
    local baseScale = 1;
    -- now we set the size (for the name) and weight (for hunger) according to his size (little, medium and big)
    if fishType.size == "Small" then
        size = ZombRand(fish.little.minSize, fish.little.maxSize);
        maxSize = fish.little.maxSize;
        minSize = fish.little.minSize;
        weightKg = size / fish.little.weightChange;
    elseif fishType.size == "Medium" then
        size = ZombRand(fish.medium.minSize, fish.medium.maxSize);
        maxSize = fish.medium.maxSize;
        minSize = fish.medium.minSize;
        weightKg = size / fish.medium.weightChange;
        baseScale = 1.2;
    else
        size = ZombRand(fish.big.minSize, fish.big.maxSize);
        maxSize = fish.big.maxSize;
        minSize = fish.big.minSize;
        weightKg = size / fish.big.weightChange;
        baseScale = 1.4;
    end
    local scaleMod = (((size - minSize) + 1) / ((maxSize - minSize) + 1) / 2);
--    print("got a fish ", fishType.size, size, minSize, maxSize, " % ", scaleMod, "full scale: ", (baseScale + scaleMod));
    local nutritionFactor = 2.2 * weightKg / baseWeightLb;
    fishToCreate:setCalories(fishToCreate:getCalories() * nutritionFactor);
    fishToCreate:setLipids(fishToCreate:getLipids() * nutritionFactor);
    fishToCreate:setCarbohydrates(fishToCreate:getCarbohydrates() * nutritionFactor);
    fishToCreate:setProteins(fishToCreate:getProteins() * nutritionFactor);
    fishToCreate:setWorldScale(scaleMod + baseScale);

    -- the fish name is like : Big Trout - 26cm
    if not fish.noNameChange then
        fishToCreate:setName(getText("IGUI_Fish_" .. fishType.size) .. " " .. getText("IGUI_Fish_" .. string.gsub(fish.name, "%s+", "")) .. " - " .. size .. "cm");
    end

    -- hunger reduction is weight of the fish div by 6, and set it to negative
    fishToCreate:setBaseHunger(- weightKg / 6);
    fishToCreate:setHungChange(fishToCreate:getBaseHunger());
    -- weight is kg * 2.2 (in pound)
    fishToCreate:setActualWeight(weightKg * 2.2);
    fishToCreate:setCustomWeight(true)
    local inv = self:getUsedInventory(fishToCreate);
    inv:AddItem(fishToCreate);
    
    return fishToCreate;
end

function ISFishingAction:getUsedInventory(item)
    if not self.fishingUI or (self.fishingUI:getSelectedBag() == self.character:getInventory()) then return self.character:getInventory(); end
    --[[for i=1,self.fishingUI.bagOptions:getNumOptions() do
        local container = self.fishingUI.bagOptions:getOptionData(i)
        if self.fishingUI.bagOptions:isSelected(i) and (container:getCapacityWeight() + item:getWeight()) <= container:getCapacity() then
            return container;
        end
    end--]]
    local container = self.fishingUI:getSelectedBag():getItemContainer();
    if(container ~= nil and (container:getCapacityWeight() + item:getWeight()) <= container:getCapacity()) then
        return container;
    else return self.character:getInventory(); end
end

------ broken line risk : (every skills pts lower by 1 this number)
-------- * big fish 9/100 risk to broke the line
-------- * medium fish 6/100
-------- * little fish 2/100
function ISFishingAction:brokeLine(fish)
    local brokenLineNumber = ZombRand(100);
    local breakRodeNumber = 0;
    if fish.size == "Small" then
        breakRodeNumber = 8 - self.fishingLvl;
    elseif fish.size == "Medium" then
        breakRodeNumber = 12 - self.fishingLvl;
    else
        breakRodeNumber = 22 - self.fishingLvl;
    end
    if not string.match(self.pole:getType(), "TwineLine") then -- a rod with twine line have more chance to break
        breakRodeNumber = breakRodeNumber - 2;
    end
    if breakRodeNumber < 0 then
        breakRodeNumber = 0;
    end
    if self.pole:getType() == "CraftedFishingRod" then -- a crafted rode have more chance to break
        breakRodeNumber = breakRodeNumber + 3;
    end
    if self.usingSpear then -- spear have more chance to break
        breakRodeNumber = breakRodeNumber + 5;
    end
    if brokenLineNumber <= breakRodeNumber then
        return self:brokeThisLine();
    end
    return false;
end

function ISFishingAction:brokeThisLine()
    if self.pole:getType() == "CraftedFishingRod" or self.pole:getType() == "CraftedFishingRodTwineLine" then
        self.character:getInventory():AddItem("Base.WoodenStick");
        self.character:playSound("BreakFishingLine")
    elseif self.pole:getType() == "FishingRod" or self.pole:getType() == "FishingRodTwineLine" then
        self.character:getInventory():AddItem("Base.FishingRodBreak");
        self.character:playSound("BreakFishingLine")
    elseif self.usingSpear then
        self.pole:setCondition(self.pole:getCondition() - 1)
        if self.pole:isBroken() then
            self.character:removeFromHands(self.pole);
            self.character:getInventory():setDrawDirty(true);
            self:forceStop();
            return true
        end
        return false
    end
    self.character:removeFromHands(self.pole);
    self.character:getInventory():Remove(self.pole);
    if self.lure then
        self.character:getInventory():Remove(self.lure);
        self.character:setSecondaryHandItem(nil);
    end
    self:forceStop();
--    getSoundManager():PlaySound("waterSplash", false, 1, 0)
    return true;
end

-- Depend on what lure you used :
-- Living lure is easier but can escape and always removed after getting something
-- Plastic lure are for good fisherman, almost never disapear but harder to get something
function ISFishingAction:attractFish()
    local attractNumber = ZombRand(self.attractNumber);
    -- a bit more chance during dawn and dusk
    local currentHour = math.floor(math.floor(GameTime:getInstance():getTimeOfDay() * 3600) / 3600);
    if (currentHour >= 4 and currentHour <= 6) or (currentHour >= 18 and currentHour <= 20) then
        attractNumber = attractNumber - 10;
    end
    if self.usingSpear then -- less chance to catch something with a spear
        attractNumber = attractNumber + 10;
    end
    -- harder chance of getting fish during winter
    if (getGameTime():getMonth() + 1) >= 11 or (getGameTime():getMonth() + 1) <= 2 then
        attractNumber = attractNumber + 20;
    end
    -- start with plastic lure
    if self.plasticLure and attractNumber <= (10 + (self.fishingLvl * 2.5)) then
        return true;
    elseif not self.plasticLure and attractNumber <= (20 + (self.fishingLvl * 1.5)) then
        return true;
    end
--    return true;
    return false;
end

function ISFishingAction:new(character, tile, rod, lure, fishingUI)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.stopOnWalk = true;
	o.stopOnRun = true;
    o.fishingUI = fishingUI;
    o.fishingLvl = character:getPerkLevel(Perks.Fishing);
    o.rod = rod
    o.lure = lure
    if o.lure then
        o.lureProperties = Fishing.lure[o.lure:getType()];
        o.plasticLure = o.lureProperties.plastic;
    else
        o.plasticLure = true;
    end
    if not o.lure then -- spear fishing
        o.maxTime = 300 + ZombRand(300) - (o.fishingLvl * 5)
    else
        if o.plasticLure then
            o.maxTime = 700 + ZombRand(300) - (o.fishingLvl * 5);
        else
            o.maxTime = 500 + ZombRand(300) - (o.fishingLvl * 5);
        end
    end
    o.tile = tile;
    o.stage = "cast";
    o.firstCast = true;
    o.splashTimer = 0;

    -- sandbox settings
    o.fishingZoneIncrease = 0;
    if SandboxVars.NatureAbundance == 1 then -- very poor
        o.fishingZoneIncrease = -10;
    elseif SandboxVars.NatureAbundance == 2 then -- poor
        o.fishingZoneIncrease = -5;
    elseif SandboxVars.NatureAbundance == 4 then -- abundant
        o.fishingZoneIncrease = 5;
    elseif SandboxVars.NatureAbundance == 5 then -- very abundant
        o.fishingZoneIncrease = 10;
    end

    o.attractNumber = 100;
    if SandboxVars.NatureAbundance == 1 then -- very poor
        o.attractNumber = 140;
    elseif SandboxVars.NatureAbundance == 2 then -- poor
        o.attractNumber = 120;
    elseif SandboxVars.NatureAbundance == 4 then -- abundant
        o.attractNumber = 80;
    elseif SandboxVars.NatureAbundance == 5 then -- very abundant
        o.attractNumber = 60;
    end

    o.caloriesModifier = 1.2;

    return o;
end
