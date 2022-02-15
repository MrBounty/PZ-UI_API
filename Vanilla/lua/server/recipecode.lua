--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

Recipe = {}
Recipe.GetItemTypes = {}
Recipe.OnCanPerform = {}
Recipe.OnCreate = {}
Recipe.OnGiveXP = {}
Recipe.OnTest = {}

function Recipe.OnCreate.HotCuppa(items, result, player) -- auto cook the hot drink
    result:setCooked(true);
    result:setHeat(2.5);
end

function Recipe.GetItemTypes.FishMeat(scriptItems)
    scriptItems:addAll(getScriptManager():getItemsTag("FishMeat"))
end

function Recipe.GetItemTypes.Rice(scriptItems)
    scriptItems:addAll(getScriptManager():getItemsTag("RiceRecipe"))
end

function Recipe.GetItemTypes.CanOpener(scriptItems)
	scriptItems:addAll(getScriptManager():getItemsTag("CanOpener"))
end

function Recipe.GetItemTypes.Hammer(scriptItems)
	scriptItems:addAll(getScriptManager():getItemsTag("Hammer"))
end

function Recipe.GetItemTypes.Saw(scriptItems)
	scriptItems:addAll(getScriptManager():getItemsTag("Saw"))
end

function Recipe.GetItemTypes.Screwdriver(scriptItems)
	scriptItems:addAll(getScriptManager():getItemsTag("Screwdriver"))
end

-- Default function to award XP when using a recipe.
function Recipe.OnGiveXP.Default(recipe, ingredients, result, player)
	for i=1,ingredients:size() do
		if ingredients:get(i-1):getType() == "Plank" or ingredients:get(i-1):getType() == "Log" then
			player:getXp():AddXP(Perks.Woodwork, 1)
		end
	end
	if instanceof(result, "Food") then
		player:getXp():AddXP(Perks.Cooking, 3)
	elseif result:getType() == "Plank" then
		player:getXp():AddXP(Perks.Woodwork, 3)
    end
end

function Recipe.OnGiveXP.DismantleElectronics(recipe, ingredients, result, player)
    player:getXp():AddXP(Perks.Electricity, 2);
end

function Recipe.OnGiveXP.None(recipe, ingredients, result, player)
end

function Recipe.OnGiveXP.SawLogs(recipe, ingredients, result, player)
    if player:getPerkLevel(Perks.Woodwork) <= 3 then
        player:getXp():AddXP(Perks.Woodwork, 3);
    else
        player:getXp():AddXP(Perks.Woodwork, 1);
    end
end

function Recipe.OnGiveXP.WoodWork5(recipe, ingredients, result, player)
    player:getXp():AddXP(Perks.Woodwork, 5);
end

function Recipe.OnGiveXP.Cooking3(recipe, ingredients, result, player)
    player:getXp():AddXP(Perks.Cooking, 3);
end

function Recipe.OnGiveXP.Cooking10(recipe, ingredients, result, player)
    player:getXp():AddXP(Perks.Cooking, 10);
end

function Recipe.OnGiveXP.MetalWelding10(recipe, ingredients, result, player)
    player:getXp():AddXP(Perks.MetalWelding, 10);
end

function Recipe.OnGiveXP.MetalWelding15(recipe, ingredients, result, player)
    player:getXp():AddXP(Perks.MetalWelding, 15);
end

function Recipe.OnGiveXP.MetalWelding20(recipe, ingredients, result, player)
    player:getXp():AddXP(Perks.MetalWelding, 20);
end

function Recipe.OnGiveXP.MetalWelding25(recipe, ingredients, result, player)
    player:getXp():AddXP(Perks.MetalWelding, 25);
end

function Recipe.OnGiveXP.Blacksmith10(recipe, ingredients, result, player)
    player:getXp():AddXP(Perks.Blacksmith, 10);
end

function Recipe.OnGiveXP.Blacksmith15(recipe, ingredients, result, player)
    player:getXp():AddXP(Perks.Blacksmith, 15);
end

function Recipe.OnGiveXP.Blacksmith20(recipe, ingredients, result, player)
    player:getXp():AddXP(Perks.Blacksmith, 25);
end

function Recipe.OnGiveXP.Blacksmith25(recipe, ingredients, result, player)
    player:getXp():AddXP(Perks.Blacksmith, 25);
end

-- check that the water isn't tainted when used in a recipe
function Recipe.OnTest.NotTaintedWater(item)
    if item:isWaterSource() then
        if item:isTaintedWater() then return false; end
    end
    return true;
end

-- check when refilling the blowtorch that blowtorch is not full and propane tank not empty
function Recipe.OnTest.RefillBlowTorch(item)
    if item:getType() == "BlowTorch" then
        if item:getUsedDelta() == 1 then return false; end
    elseif item:getType() == "PropaneTank" then
        if item:getUsedDelta() == 0 then return false; end
    end
    return true;
end

-- Fill entirely the blowtorch with the remaining propane
function Recipe.OnCreate.RefillBlowTorch(items, result, player)
    local previousBT = nil;
    local propaneTank = nil;
    for i=0, items:size()-1 do
       if items:get(i):getType() == "BlowTorch" then
           previousBT = items:get(i);
       elseif items:get(i):getType() == "PropaneTank" then
           propaneTank = items:get(i);
       end
    end
    result:setUsedDelta(previousBT:getUsedDelta() + result:getUseDelta() * 30);

    while result:getUsedDelta() < 1 and propaneTank:getUsedDelta() > 0 do
        result:setUsedDelta(result:getUsedDelta() + result:getUseDelta() * 30);
        propaneTank:Use();
    end

    if result:getUsedDelta() > 1 then
        result:setUsedDelta(1);
    end
end

function Recipe.OnCreate.OpenBoxOfJars(items, result, player)
    player:getInventory():AddItems("Base.JarLid", 6);
end

-- change result quality depending on your BS skill and the tools used
function BSItem_OnCreate(items, result, player)
    local ballPeen = player:getInventory():contains("BallPeenHammer");

    if instanceof(result, "HandWeapon") then
        local condPerc = ZombRand(5 + (player:getPerkLevel(Perks.Blacksmith) * 5), 10 + (player:getPerkLevel(Perks.Blacksmith) * 10));
        if not ballPeen then
            condPerc = condPerc - 20;
        end
        if condPerc < 5 then
            condPerc = 5;
        elseif condPerc > 100 then
            condPerc = 100;
        end
        result:setCondition(round(result:getConditionMax() * (condPerc/100)));
    end
end

-- Return true if recipe is valid, false otherwise
function Recipe.OnTest.TorchBatteryRemoval (sourceItem, result)
	return sourceItem:getUsedDelta() > 0;
end

-- When creating item in result box of crafting panel.
function Recipe.OnCreate.TorchBatteryRemoval(items, result, player)
	for i=0, items:size()-1 do
		local item = items:get(i)
		-- we found the battery, we change his used delta according to the battery
		if item:getType() == "Torch" or item:getType() == "HandTorch" or item:getType() == "Rubberducky2" then
			result:setUsedDelta(item:getUsedDelta());
			-- then we empty the torch used delta (his energy)
			item:setUsedDelta(0);
		end
	end
end

-- Return true if recipe is valid, false otherwise
function Recipe.OnTest.TorchBatteryInsert(sourceItem, result)
	if sourceItem:getType() == "Torch" or sourceItem:getType() == "HandTorch" or sourceItem:getType() == "Rubberducky2" then
		return sourceItem:getUsedDelta() == 0; -- Only allow the battery inserting if the flashlight has no battery left in it.
	end
	return true -- the battery
end

-- When creating item in result box of crafting panel.
function Recipe.OnCreate.TorchBatteryInsert(items, result, player)
  for i=0, items:size()-1 do
	-- we found the battery, we change his used delta according to the battery
	if items:get(i):getType() == "Battery" then
		result:setUsedDelta(items:get(i):getUsedDelta());
	end
  end
end

function Recipe.OnCreate.DismantleFlashlight(items, result, player)
	for i=1,items:size() do
		local item = items:get(i-1)
		if item:getType() == "Torch" or item:getType() == "HandTorch" then
			if item:getUsedDelta() > 0 then
				local battery = player:getInventory():AddItem("Base.Battery")
				if battery then
					battery:setUsedDelta(item:getUsedDelta())
				end
			end
			break
		end
	end
end

function Recipe.OnCreate.MakeBowlOfSoup4(items, result, player)
    local addType = "Base.Pot"
    for i=0,items:size() - 1 do
        local item = items:get(i)
        if item:getType() == "PotOfSoup" or item:getType() == "PotOfSoupRecipe" or item:getType() == "RicePan" or item:getType() == "PastaPan"or item:getType() == "PastaPot"or item:getType() == "RicePot" or item:getType() == "WaterPotRice" then
            result:setBaseHunger(item:getBaseHunger() / 4);
            result:setHungChange(item:getBaseHunger() / 4);
            result:setThirstChange(item:getThirstChange() / 4);
            result:setBoredomChange(item:getBoredomChange() / 4);
            result:setUnhappyChange(item:getUnhappyChange() / 4);
            result:setCarbohydrates(item:getCarbohydrates() / 4);
            result:setLipids(item:getLipids() / 4);
            result:setProteins(item:getProteins() / 4);
            result:setCalories(item:getCalories() / 4);
            result:setTaintedWater(item:isTaintedWater())
            if string.contains(item:getType(), "Pan") then
                addType = "Base.Saucepan"
            end
        end
    end
    player:getInventory():AddItem(addType);
end

function Recipe.OnCreate.MakeBowlOfSoup2(items, result, player)
    local addType = "Base.Pot"
    for i=0,items:size() - 1 do
        local item = items:get(i)
        if item:getType() == "PotOfSoup" or item:getType() == "PotOfSoupRecipe" or item:getType() == "RicePan" or item:getType() == "PastaPan" or item:getType() == "PastaPot"or item:getType() == "RicePot" or item:getType() == "WaterPotRice" then
            result:setBaseHunger(item:getBaseHunger() / 2);
            result:setHungChange(item:getBaseHunger() / 2);
            result:setThirstChange(item:getThirstChange() / 2);
            result:setBoredomChange(item:getBoredomChange() / 2);
            result:setUnhappyChange(item:getUnhappyChange() / 2);
            result:setCarbohydrates(item:getCarbohydrates() / 2);
            result:setLipids(item:getLipids() / 2);
            result:setProteins(item:getProteins() / 2);
            result:setCalories(item:getCalories() / 2);
            result:setTaintedWater(item:isTaintedWater())
            if string.contains(item:getType(), "Pan") then
                addType = "Base.Saucepan"
            end
        end
    end
    player:getInventory():AddItem(addType);
end

function Recipe.OnCreate.AddBaseIngredientToCookingVessel(items, result, player)
    for i=0,items:size() - 1 do
        local item = items:get(i)
        if item:getType() == "WaterSaucepan" or item:getType() == "WaterPot" then
            result:setCondition(item:getCondition());
        end
    end
end

function Recipe.OnCreate.OpenCandyPackage(items, result, player)
    player:getInventory():AddItem("Base.MintCandy");
    player:getInventory():AddItem("Base.MintCandy");
    player:getInventory():AddItem("Base.MintCandy");
    player:getInventory():AddItem("Base.MintCandy");
    player:getInventory():AddItem("Base.MintCandy");
    player:getInventory():AddItem("Base.MintCandy");
end

function Recipe.OnCreate.MakeBowlOfStew4(items, result, player)
    for i=0,items:size() - 1 do
        local item = items:get(i)
        if item:getType() == "PotOfStew" then
            result:setBaseHunger(item:getBaseHunger() / 4);
            result:setHungChange(item:getBaseHunger() / 4);
            result:setThirstChange(item:getThirstChange() / 4);
            result:setBoredomChange(item:getBoredomChange() / 4);
            result:setUnhappyChange(item:getUnhappyChange() / 4);
            result:setCarbohydrates(item:getCarbohydrates() / 4);
            result:setLipids(item:getLipids() / 4);
            result:setProteins(item:getProteins() / 4);
            result:setCalories(item:getCalories() / 4);
            result:setTaintedWater(item:isTaintedWater())
        end
    end
    player:getInventory():AddItem("Base.Pot");
end

function LightCandle_OnCreate(items, result, player)
    for i=0,items:size() - 1 do
        local item = items:get(i)
        if item:getType() == "Candle" then
            result:setUsedDelta(item:getUsedDelta());
            result:setCondition(item:getCondition());
            result:setFavorite(item:isFavorite());
            if player:getPrimaryHandItem() == player:getSecondaryHandItem() then
                player:setPrimaryHandItem(nil)
            end
            player:setSecondaryHandItem(result);
            result:setActivated(true); --ensure the candle emits light upon creation
        end
    end
end

function ExtinguishCandle_OnCreate(items, result, player)
    for i=0,items:size() - 1 do
        local item = items:get(i)
        if item:getType() == "CandleLit" then
            result:setUsedDelta(item:getUsedDelta());
            result:setCondition(item:getCondition());
            result:setFavorite(item:isFavorite());
        end
    end
end


function Recipe.OnCreate.MakeBowlOfStew2(items, result, player)
    for i=0,items:size() - 1 do
        local item = items:get(i)
        if item:getType() == "PotOfStew" then
            result:setBaseHunger(item:getBaseHunger() / 2);
            result:setHungChange(item:getBaseHunger() / 2);
            result:setThirstChange(item:getThirstChange() / 2);
            result:setBoredomChange(item:getBoredomChange() / 2);
            result:setUnhappyChange(item:getUnhappyChange() / 2);
            result:setCarbohydrates(item:getCarbohydrates() / 2);
            result:setLipids(item:getLipids() / 2);
            result:setProteins(item:getProteins() / 2);
            result:setCalories(item:getCalories() / 2);
            result:setTaintedWater(item:isTaintedWater())
        end
    end
    player:getInventory():AddItem("Base.Pot");
end

function Recipe.OnTest.SliceBreadDough(sourceItem, result)
    if sourceItem:getFullType() == "Base.BreadDough" then
        return sourceItem:isCooked()
    end
    return true
end

function Recipe.OnCreate.SlicePie(items, result, player)
    for i=0,items:size() - 1 do
        local item = items:get(i)
        if item:getType() == "PieWholeRaw" or item:getType() == "CakeRaw" then
            result:setBaseHunger(item:getBaseHunger() / 5);
            result:setHungChange(item:getBaseHunger() / 5);
            result:setBoredomChange(item:getBoredomChange() / 5)
            result:setUnhappyChange(item:getUnhappyChange() / 5)
            result:setCalories(item:getCalories() / 5)
            result:setCarbohydrates(item:getCarbohydrates() / 5)
            result:setLipids(item:getLipids() / 5)
            result:setProteins(item:getProteins() / 5)
        end
    end
    player:getInventory():AddItem("Base.BakingPan");
end

function Recipe.OnTest.CutFish(sourceItem, result)
    if instanceof(sourceItem, "Food") then
        return sourceItem:getActualWeight() > 0.6
    end
    return true
end

function Recipe.OnCreate.CutFish(items, result, player)
    local fish = nil;
    for i=0,items:size() - 1 do
        if instanceof(items:get(i), "Food") then
            fish = items:get(i);
            break;
        end
    end
    if fish then
        local hunger = math.max(fish:getBaseHunger(), fish:getHungChange())
        result:setBaseHunger(hunger / 2);
        result:setHungChange(hunger / 2);
        result:setActualWeight((fish:getActualWeight() * 0.9) / 2)
        result:setWeight(result:getActualWeight());
        result:setCustomWeight(true)
        result:setCarbohydrates(fish:getCarbohydrates() / 2);
        result:setLipids(fish:getLipids() / 2);
        result:setProteins(fish:getProteins() / 2);
        result:setCalories(fish:getCalories() / 2);
        result:setCooked(fish:isCooked());
    end
end

function Recipe.OnTest.CutFillet(sourceItem, result)
    if instanceof(sourceItem, "Food") then
        return sourceItem:getActualWeight() > 1.0
    end
    return true
end

function Recipe.OnCreate.CutFillet(items, result, player)
    local fillet = nil
    for i=0,items:size() - 1 do
        if items:get(i):getType() == "FishFillet" then
            fillet = items:get(i)
            break
        end
    end
    if fillet then
        local hunger = math.max(fillet:getBaseHunger(), fillet:getHungChange())
        fillet:setBaseHunger(hunger * 0.5)
        fillet:setHungChange(fillet:getBaseHunger())
        fillet:setActualWeight(fillet:getActualWeight() * 0.5)

        result:setBaseHunger(fillet:getBaseHunger())
        result:setHungChange(fillet:getBaseHunger())
        result:setActualWeight(fillet:getActualWeight())
        result:setWeight(result:getActualWeight())
        result:setCustomWeight(true)
        result:setCarbohydrates(fillet:getCarbohydrates());
        result:setLipids(fillet:getLipids());
        result:setProteins(fillet:getProteins());
        result:setCalories(fillet:getCalories());
    end
end

function Recipe.OnCreate.CutAnimal(items, result, player)
    local anim = nil;
    for i=0,items:size() - 1 do
        if instanceof(items:get(i), "Food") then
            anim = items:get(i);
            break;
        end
    end
    if anim then
        local new_hunger = anim:getBaseHunger() * 1.05;
        if(new_hunger < -100) then
            new_hunger = -100;
        end
        result:setBaseHunger(new_hunger);
        result:setHungChange(new_hunger);

        result:setCustomWeight(true);
        result:setWeight(anim:getWeight() * 0.7);
        result:setActualWeight(anim:getActualWeight() * 0.7);

        result:setLipids(anim:getLipids() * 0.75);
        result:setProteins(anim:getProteins() * 0.75);
        result:setCalories(anim:getCalories() * 0.75);
        result:setCarbohydrates(anim:getCarbohydrates() * 0.75);
    end
end

-- give the bowl back
function Recipe.OnCreate.PutCakeBatterInBakingPan(items,result,player)
    player:getInventory():AddItem("Base.Bowl");
end

-- set the age of the food to the can, you need to cook it to have a 2-3 months preservation
function Recipe.OnCreate.CannedFood(items, result, player)
    local food = nil;
    for i=0,items:size() - 1 do
        if instanceof(items:get(i), "Food") then
            if not food or (food:getAge() < items:get(i):getAge()) then
                food = items:get(i);
--                print("got food with age " .. food:getAge())
            end
        end
    end
--    print("new jared food age " .. food:getAge() .. " and max age " .. food:getOffAgeMax());
    result:setAge(food:getAge());
    result:setOffAgeMax(food:getOffAgeMax());
    result:setOffAge(food:getOffAge());
end

-- set back the age of the food and give the jar back
function Recipe.OnCreate.OpenCannedFood(items, result, player)
    local jar = items:get(0);
    local aged = jar:getAge() / jar:getOffAgeMax();

    result:setAge(result:getOffAgeMax() * aged);

    player:getInventory():AddItem("Base.EmptyJar");

--    print("you're new food have age " .. result:getAge());
end

----- CannedFood_OnCooked IS NOT RECIPE CODE. IT IS CALLED BY Food.update() -----
-- you cook your can, now set the correct food age/max age
function CannedFood_OnCooked(cannedFood)
    local aged = cannedFood:getAge() / cannedFood:getOffAgeMax();
    cannedFood:setOffAgeMax(90);
    cannedFood:setOffAge(60);
    cannedFood:setAge(cannedFood:getOffAgeMax() * aged);
--    print("new jared food age " .. cannedFood:getAge() .. " and max age " .. cannedFood:getOffAgeMax());
end

-- give back the rope used
function Recipe.OnCreate.SplitLogStack(items, result, player)
    player:getInventory():AddItem("Base.Rope");
    player:getInventory():AddItem("Base.Rope");
end

function Recipe.OnCreate.Dismantle(items, result, player)
    player:getInventory():AddItem("Base.ElectronicsScrap");
end

function Recipe.OnCreate.Dismantle2(items, result, player)
    player:getInventory():AddItem("Base.ElectronicsScrap");
    player:getInventory():AddItem("Base.ElectronicsScrap");
end

function Recipe.OnCreate.SpikedBat(items, result, player)
    for i=1,items:size() do
        local item = items:get(i-1)
        if item:getType() == "BaseballBat" then
            result:setCondition(item:getCondition())
            break
        end
    end
end

function Recipe.OnCreate.OpenEggCarton(items, result, player)
    result:setAge(items:get(0):getAge());
end

--[[ ############# Radio stuff ############## --]]

function Recipe.OnCreate.DismantleRadioSpecial(items, result, player, selectedItem)
    local success = 50 + (player:getPerkLevel(Perks.Electricity)*5);
    if ZombRand(0,100)<success then
        player:getInventory():AddItem("Radio.ScannerModule");
    end
    DismantleRadioTwoWay_OnCreate(items, result, player, selectedItem);
end

function Recipe.OnCreate.DismantleRadioHAM(items, result, player, selectedItem)
    local success = 50 + (player:getPerkLevel(Perks.Electricity)*5);
    if ZombRand(0,100)<success then
        player:getInventory():AddItem("Radio.RadioTransmitter");
    end
    if ZombRand(0,100)<success then
        player:getInventory():AddItem("Base.LightBulbGreen");
    end
    DismantleRadio_OnCreate(items, result, player, selectedItem);
end

function Recipe.OnCreate.DismantleRadioTwoWay(items, result, player, selectedItem)
    local success = 50 + (player:getPerkLevel(Perks.Electricity)*5);
    if ZombRand(0,100)<success then
        player:getInventory():AddItem("Radio.RadioTransmitter");
    end
    if ZombRand(0,100)<success then
        player:getInventory():AddItem("Base.LightBulbGreen");
    end
    DismantleRadio_OnCreate(items, result, player, selectedItem);
end

function Recipe.OnCreate.DismantleRadio(items, result, player, selectedItem)           --TODO adding return items/chance based on selectedItem value
    local success = 50 + (player:getPerkLevel(Perks.Electricity)*5);
    for i=1,ZombRand(1,4) do
        local r = ZombRand(1,4);
        if r==1 then
            player:getInventory():AddItem("Base.ElectronicsScrap");
        elseif r==2 then
            player:getInventory():AddItem("Radio.ElectricWire");
        elseif r==3 then
            player:getInventory():AddItem("Base.Aluminum");
        end
    end
    if ZombRand(0,100)<success then
        player:getInventory():AddItem("Base.Amplifier");
    end
    if ZombRand(0,100)<success then
        player:getInventory():AddItem("Base.LightBulb");
    end
    if ZombRand(0,100)<success then
        player:getInventory():AddItem("Radio.RadioReceiver");
    end
    --if selectedItem then
        --print("Main item "..selectedItem:getName());
    --end
    for i=1,items:size() do
        local item = items:get(i-1)
        if instanceof(item, "Radio") then
            item:getDeviceData():getBattery(player:getInventory())
            item:getDeviceData():getHeadphones(player:getInventory())
            break
        end
    end
end

function Recipe.OnCreate.DismantleRadioTV(items, result, player, selectedItem)
    local success = 50 + (player:getPerkLevel(Perks.Electricity)*5);
    for i=1,ZombRand(1,6) do
        local r = ZombRand(1,4);
        if r==1 then
            player:getInventory():AddItem("Base.ElectronicsScrap");
        elseif r==2 then
            player:getInventory():AddItem("Radio.ElectricWire");
        elseif r==3 then
            player:getInventory():AddItem("Base.Aluminum");
        end
    end
    if ZombRand(0,100)<success then
        player:getInventory():AddItem("Base.Amplifier");
    end
    if ZombRand(0,100)<success then
        player:getInventory():AddItem("Base.LightBulb");
    end
    if selectedItem then
        --print("Main item "..selectedItem:getName());
        if selectedItem:getType()~="TvAntique" then
            if ZombRand(0,100)<success then
                player:getInventory():AddItem("Base.LightBulbRed");
            end
            if ZombRand(0,100)<success then
                player:getInventory():AddItem("Base.LightBulbGreen");
            end
        end
    end
end

function Recipe.OnGiveXP.DismantleRadio(recipe, ingredients, result, player)
    player:getXp():AddXP(Perks.Electricity, 2);
end

local function getRandomValue(valmin, valmax, perkLevel)
    local range = valmax-valmin;
    local r = ZombRandFloat(range*((perkLevel-1)/10),range*(perkLevel/10));
    return valmin+r;
end

function Recipe.OnCreate.RadioCraft(items, result, player, selectedItem)
    --TransmitRange		= 5000,
    if result and result:getDeviceData() then
        local data = result:getDeviceData();
        local perk = player:getPerkLevel(Perks.Electricity);
        local perkInvert = 10-perk;
        result:setActualWeight(getRandomValue(1.5,3.0,perk));
        result:setWeight(result:getActualWeight())
        result:setCustomWeight(true)
        data:setUseDelta(getRandomValue(0.007,0.030,perkInvert));
        data:setBaseVolumeRange(getRandomValue(8,16,perk));
        data:setMinChannelRange(getRandomValue(200,88000,perkInvert));
        data:setMaxChannelRange(getRandomValue(108000,1000000,perkInvert));
        data:setTransmitRange(getRandomValue(500,5000,perk));
        data:setHasBattery(false);
        data:setPower(0);
        data:transmitBattryChange();
        if perk == 10 then
            if ZombRand(0,100)<25 then --on max level 25% chance to craft a hightier device. Superior range, very low power consumption.
                data:setIsHighTier(true);
                data:setTransmitRange(ZombRand(5500,7500));
                data:setUseDelta(ZombRand(0.002,0.007));
            end
        end
    end
end
function Recipe.OnGiveXP.RadioCraft(recipe, ingredients, result, player)
    player:getXp():AddXP(Perks.Electricity, player:getPerkLevel(Perks.Electricity)*5);
end

----- OnEat_Cigarettes IS NOT RECIPE CODE.  IT IS CALLED BY IsoGameCharacter.Eat() -----
-- smoking cigarettes gives more bonus to a smoker
function OnEat_Cigarettes(food, character, percent)
    local script = food:getScriptItem()
    percent = percent * (food:getStressChange() * 100) / script:getStressChange()
    local bodyDamage = character:getBodyDamage()
    local stats = character:getStats()
    if character:HasTrait("Smoker") then
        bodyDamage:setUnhappynessLevel(bodyDamage:getUnhappynessLevel() - 10 * percent);
        if bodyDamage:getUnhappynessLevel() < 0 then
            bodyDamage:setUnhappynessLevel(0);
        end
        stats:setStress(stats:getStress() - 10 * percent);
        if stats:getStress() < 0 then
            stats:setStress(0);
        end
        local reduceSFC = stats:getMaxStressFromCigarettes()
        stats:setStressFromCigarettes(stats:getStressFromCigarettes() - reduceSFC * percent);
        character:setTimeSinceLastSmoke(stats:getStressFromCigarettes() / stats:getMaxStressFromCigarettes());
    else
--        bodyDamage:setUnhappynessLevel(bodyDamage:getUnhappynessLevel() + 5);
--        if bodyDamage:getUnhappynessLevel() > 100 then
--            bodyDamage:setUnhappynessLevel(100);
--        end
        bodyDamage:setFoodSicknessLevel(bodyDamage:getFoodSicknessLevel() + 14 * percent);
        if bodyDamage:getFoodSicknessLevel() > 100 then
            bodyDamage:setFoodSicknessLevel(100);
        end
    end
end

function Recipe.GetItemTypes.CraftSheetRope(scriptItems)
    local allScriptItems = getScriptManager():getAllItems()
    for i=1,allScriptItems:size() do
        local scriptItem = allScriptItems:get(i-1)
        if (scriptItem:getType() == Type.Clothing) and scriptItem:getFabricType() then
            local crd = ClothingRecipesDefinitions["FabricType"][scriptItem:getFabricType()]
            if crd and not crd.noSheetRope then
                scriptItems:add(scriptItem)
            end
        elseif ClothingRecipesDefinitions[scriptItem:getName()] then
            scriptItems:add(scriptItem) -- Base.Sheet
        end
    end
end

local function RipClothing_GetItemTypes_XXX(scriptItems, fabricType)
    if not ClothingRecipesDefinitions["FabricType"][fabricType] then
        return
    end
    local allScriptItems = getScriptManager():getAllItems()
    for i=1,allScriptItems:size() do
        local scriptItem = allScriptItems:get(i-1)
        if (scriptItem:getType() == Type.Clothing) and (scriptItem:getFabricType() == fabricType) then
            if ClothingRecipesDefinitions[scriptItem:getName()] then
                -- ignore
            else
                scriptItems:add(scriptItem)
            end
        end
    end
end

function Recipe.GetItemTypes.RipClothing_Cotton(scriptItems)
    RipClothing_GetItemTypes_XXX(scriptItems, "Cotton")
end

function Recipe.GetItemTypes.RipClothing_Denim(scriptItems)
    RipClothing_GetItemTypes_XXX(scriptItems, "Denim")
end

function Recipe.GetItemTypes.RipClothing_Leather(scriptItems)
    RipClothing_GetItemTypes_XXX(scriptItems, "Leather")
end

function Recipe.GetItemTypes.RipSheets(scriptItems)
    local allScriptItems = getScriptManager():getAllItems()
    for i=1,allScriptItems:size() do
        local scriptItem = allScriptItems:get(i-1)
        if scriptItem:getType() ~= Type.Clothing then
            if ClothingRecipesDefinitions[scriptItem:getName()] then
                scriptItems:add(scriptItem)
            end
        end
    end
end

-- Code copied from TimedActions/ISRipClothing.lua
function Recipe.OnCreate.RipClothing(items, result, player, selectedItem)
    local item = items:get(0) -- assumes any tool comes after this in recipes.txt

    -- either we come from clothingrecipesdefinitions or we simply check number of covered parts by the clothing and add
    local materials = nil
    local nbrOfCoveredParts = nil
    local maxTime = 0 -- TODO: possibly allow recipe to call Lua function to get maxTime for actions
    if ClothingRecipesDefinitions[item:getType()] then
        local recipe = ClothingRecipesDefinitions[item:getType()]
        materials = luautils.split(recipe.materials, ":");
        maxTime = tonumber(materials[2]) * 20;
    elseif ClothingRecipesDefinitions["FabricType"][item:getFabricType()] then
        materials = {};
        materials[1] = ClothingRecipesDefinitions["FabricType"][item:getFabricType()].material;
        nbrOfCoveredParts = item:getNbrOfCoveredParts();
        local minMaterial = 2;
        local maxMaterial = nbrOfCoveredParts;
        if nbrOfCoveredParts == 1 then
            minMaterial = 1;
        end
    
        local nbr = ZombRand(minMaterial, maxMaterial + 1);
        nbr = nbr + (player:getPerkLevel(Perks.Tailoring) / 2);
        if nbr > nbrOfCoveredParts then
            nbr = nbrOfCoveredParts;
        end
        materials[2] = nbr;
    
        maxTime = nbrOfCoveredParts * 20;
    else
        error "Recipe.OnCreate.RipClothing"
    end

    for i=1,tonumber(materials[2]) do
        local item2;
        local dirty = false;
        if instanceof(item, "Clothing") then
            dirty = (ZombRand(100) <= item:getDirtyness()) or (ZombRand(100) <= item:getBloodlevel());
        end
        if not dirty then
            item2 = InventoryItemFactory.CreateItem(materials[1]);
        elseif getScriptManager():FindItem(materials[1] .. "Dirty") then
            item2 = InventoryItemFactory.CreateItem(materials[1] .. "Dirty");
        else
            item2 = InventoryItemFactory.CreateItem(materials[1])
        end
        player:getInventory():AddItem(item2);
    end

    -- add thread sometimes, depending on tailoring level
    if ZombRand(7) < player:getPerkLevel(Perks.Tailoring) + 1 then
        local max = 2;
        if nbrOfCoveredParts then
            max = nbrOfCoveredParts;
            if max > 6 then
                max = 6;
            end
        end
        max = ZombRand(2, max);
        local thread = InventoryItemFactory.CreateItem("Base.Thread");
        for i=1,10-max do
            thread:Use();
        end
        player:getInventory():AddItem(thread);
        player:getXp():AddXP(Perks.Tailoring, 1);
    end
end

-- check clothings are dirty or bloody
function Recipe.OnTest.WashClothing(sourceItem, result)
    if instanceof(sourceItem, "Clothing") then
        return sourceItem:isDirty() or sourceItem:isBloody();
    else
        return true;
    end
end

-- wash the clothing!
function Recipe.OnCreate.WashClothing(items, result, player, selectedItem)
    for i=0,items:size() - 1 do
        if instanceof (items:get(i), "Clothing") then
            items:get(i):setDirtyness(0);
            items:get(i):setBloodLevel(0);
            return;
        end
    end
end

-- get the spear, lower its condition according to woodwork perk level
-- also lower the used knife condition
function Recipe.OnCreate.CreateSpear(items, result, player, selectedItem)
    local conditionMax = 2 + player:getPerkLevel(Perks.Woodwork);
    conditionMax = ZombRand(conditionMax, conditionMax + 2);
    if conditionMax > result:getConditionMax() then
        conditionMax = result:getConditionMax();
    end
    if conditionMax < 2 then
        conditionMax = 2;
    end
    result:setCondition(conditionMax)
    
    for i=0,items:size() - 1 do
        if instanceof (items:get(i), "HandWeapon") and items:get(i):getCategories():contains("SmallBlade") then
            items:get(i):setCondition(items:get(i):getCondition() - 1);
        end
        if items:get(i):getType() == "SharpedStone" and ZombRand(3) == 0 then
            player:getInventory():Remove(items:get(i))
        end
    end
end

-- get a mix of spear & upgrade item to do a correct condition of the result
-- we take the craftedSpear condition and substract the attached weapon condition
function Recipe.OnCreate.UpgradeSpear(items, result, player, selectedItem)
    local conditionMax = 0;
    for i=0,items:size() - 1 do
        if items:get(i):getType() == "SpearCrafted" then
            conditionMax = items:get(i):getCondition()
        end
    end
    
    for i=0,items:size() - 1 do
        if instanceof (items:get(i), "HandWeapon") and items:get(i):getType() ~= "SpearCrafted" then
            conditionMax = conditionMax - ((items:get(i):getConditionMax() - items:get(i):getCondition())/2)
        end
    end
    
    if conditionMax > result:getConditionMax() then
        conditionMax = result:getConditionMax();
    end
    if conditionMax < 2 then
        conditionMax = 2;
    end

    result:setCondition(conditionMax);
end

-- when we reclaim the weapon from a spear we get the weapon back
-- we also want to return the spear with appropriate condition
function Recipe.OnCreate.DismantleSpear(items, result, player, selectedItem)
    local conditionMax = selectedItem:getCondition();

    if conditionMax > selectedItem:getConditionMax() then
        conditionMax = selectedItem:getConditionMax();
    end
    if conditionMax < 2 then
        conditionMax = 2;
    end
    local spear = player:getInventory():AddItem("Base.SpearCrafted");
    spear:setCondition(conditionMax);
end

function Recipe.OnCreate.SliceWatermelon(items, result, player)
    for i=0,items:size() - 1 do
        local item = items:get(i)
        if item:getType() == "Watermelon" then
            result:setBaseHunger(item:getBaseHunger() / 10);
            result:setHungChange(item:getHungChange() / 10);
            result:setBoredomChange(item:getBoredomChange() / 10)
            result:setUnhappyChange(item:getUnhappyChange() / 10)
            result:setCalories(item:getCalories() / 10)
            result:setCarbohydrates(item:getCarbohydrates() / 10)
            result:setLipids(item:getLipids() / 10)
            result:setProteins(item:getProteins() / 10)
        end
    end
end

function Recipe.OnCreate.SliceBread(items, result, player)
    for i=0,items:size() - 1 do
        local item = items:get(i)
        if item:getType() == "Bread" or items:get(i):getType() == "BreadDough" then
            result:setBaseHunger(item:getBaseHunger() / 3);
            result:setHungChange(item:getHungChange() / 3);
            result:setBoredomChange(item:getBoredomChange() / 3)
            result:setUnhappyChange(item:getUnhappyChange() / 3)
            result:setCalories(item:getCalories() / 3)
            result:setCarbohydrates(item:getCarbohydrates() / 3)
            result:setLipids(item:getLipids() / 3)
            result:setProteins(item:getProteins() / 3)
        end
    end
end

function Recipe.OnCreate.SliceHam(items, result, player)
    for i=0,items:size() - 1 do
        local item = items:get(i)
        if item:getType() == "Ham" then
            result:setBaseHunger(item:getBaseHunger() / 6);
            result:setHungChange(item:getHungChange() / 6);
            result:setBoredomChange(item:getBoredomChange() / 6)
            result:setUnhappyChange(item:getUnhappyChange() / 6)
            result:setCalories(item:getCalories() / 6)
            result:setCarbohydrates(item:getCarbohydrates() / 6)
            result:setLipids(item:getLipids() / 6)
            result:setProteins(item:getProteins() / 6)
        end
    end
end

function Recipe.OnCreate.OpenUmbrella(items, result, player, umbrella, firstHand, secondHand)
    result:setCondition(umbrella:getCondition());
    if secondHand then
        player:setSecondaryHandItem(result);
    elseif firstHand and not secondHand then
        player:setPrimaryHandItem(result);
    end
end

function Recipe.OnCreate.CloseUmbrella(items, result, player, umbrella, firstHand, secondHand)
    result:setCondition(umbrella:getCondition());
    if secondHand or firstHand then
        if not player:getPrimaryHandItem() then
            player:setPrimaryHandItem(result);
        end
        player:setSecondaryHandItem(result);
    end
end

function Recipe.GetItemTypes.DismantleDigitalWatch(scriptItems)
    local allScriptItems = getScriptManager():getAllItems();
    for i=1,allScriptItems:size() do
        local scriptItem = allScriptItems:get(i-1);
        if (scriptItem:getType() == Type.AlarmClockClothing) and string.contains(scriptItem:getName(), "Digital") then
            scriptItems:add(scriptItem);
        end
    end
end

-- Example OnCanPeform function.
function Recipe.OnCanPerform.HockeyMaskSmashBottle(recipe, playerObj)
	local wornItem = playerObj:getWornItem("MaskEyes")
	return (wornItem ~= nil) and (wornItem:getType() == "Hat_HockeyMask")
end

-- only clean if not cooked, to avoid mistake instead of clicking "get muffins"
function Recipe.OnCanPerform.CleanMuffin(recipe, playerObj, item)
    return item and not item:isCooked();
end

-- Muffins need to be cooked first
function Recipe.OnCanPerform.GetMuffin(recipe, playerObj, item)
    return item and item:isCooked();
end

function Recipe.OnCreate.GetMuffin(items, result, player, selectedItem)
    result:setBaseHunger(selectedItem:getBaseHunger() / 6);
    result:setCalories(selectedItem:getCalories() / 6);
    result:setProteins(selectedItem:getProteins() / 6);
    result:setLipids(selectedItem:getLipids() / 6);
    result:setCarbohydrates(selectedItem:getCarbohydrates() / 6);

    player:getInventory():AddItem("Base.MuffinTray");
    player:getInventory():Remove(selectedItem);
end

function Recipe.OnCreate.DynamicMovable(items, result, player, selectedItem)
    if instanceof(selectedItem, "Moveable") then
        local sprite = selectedItem:getWorldSprite();
        --print("onCreate sprite = "..tostring(sprite));
        local props = ISMoveableSpriteProps.new( sprite );

        local items = props:getScrapItemsList(player);

        local added = 0;

        for k,v in ipairs(items.usable) do
            --print(" - adding usable = "..tostring(v));
            local item 	= instanceItem( v );
            if item then
                if props.keyId and props.keyId ~= -1 then
                    if item:getType() == "Doorknob" then
                        item:setKeyId(props.keyId)
                    end
                end
                player:getInventory():AddItem(item);
                added = added +1;
            end
        end
        for k,v in ipairs(items.unusable) do
            --print(" - adding unusable = "..tostring(v));
            if v then
                player:getInventory():AddItem(v);
            end
        end

        props:scrapHaloNoteCheck(player, added)
    else
        print("Recipe.OnCreate.DynamicMovable, this isnt a movable item?")
    end
end
function Recipe.OnGiveXP.DynamicMovable(recipe, ingredients, result, player)
    if instanceof(recipe, "MovableRecipe") then
        local sprite = recipe:getWorldSprite();
        --print("onXp sprite = "..tostring(sprite));
        local props = ISMoveableSpriteProps.new( sprite );
        props:scrapGiveXp(player, false);
    else
        print("Recipe.OnGiveXP.DynamicMovable, this isnt a Movable recipe?")
    end
end

--Open sealed bag of produce to get contents + empty sack.
function Recipe.OnCreate.OpenSackProduce(items, result, player)
    result:setAge(items:get(0):getAge());
    player:getInventory():AddItem("EmptySandbag");
end

-- These functions are defined to avoid breaking mods.
DefaultRecipe_OnGiveXP = Recipe.OnGiveXP.Default
NoXP_OnGiveXP = Recipe.OnGiveXP.None
CannedFood_OnCreate = Recipe.OnCreate.CannedFood
CheckTaintedWater_OnTest = Recipe.OnTest.NotTaintedWater
CloseUmbrella = Recipe.OnCreate.CloseUmbrella
CreateSpear_OnCreate = Recipe.OnCreate.CreateSpear
CutAnimal_OnCreate = Recipe.OnCreate.CutAnimal
CutFillet_OnCreate = Recipe.OnCreate.CutFillet
CutFillet_TestIsValid = Recipe.OnTest.CutFillet
CutFish_OnCreate = Recipe.OnCreate.CutFish
CutFish_TestIsValid = Recipe.OnTest.CutFish
Dismantle_OnCreate = Recipe.OnCreate.Dismantle
Dismantle2_OnCreate = Recipe.OnCreate.Dismantle2
DismantleRadio_OnCreate = Recipe.OnCreate.DismantleRadio
DismantleRadio_OnGiveXP = Recipe.OnGiveXP.DismantleRadio
DismantleElectronics_OnGiveXP = Recipe.OnGiveXP.DismantleElectronics
DismantleRadioHAM_OnCreate = Recipe.OnCreate.DismantleRadioHAM
DismantleRadioSpecial_OnCreate = Recipe.OnCreate.DismantleRadioSpecial
DismantleRadioTV_OnCreate = Recipe.OnCreate.DismantleRadioTV
DismantleRadioTwoWay_OnCreate = Recipe.OnCreate.DismantleRadioTwoWay
DismantleSpear_OnCreate = Recipe.OnCreate.DismantleSpear
Give10BSXP = Recipe.OnGiveXP.Blacksmith10
Give15BSXP = Recipe.OnGiveXP.Blacksmith15
Give20BSXP = Recipe.OnGiveXP.Blacksmith20
Give25BSXP = Recipe.OnGiveXP.Blacksmith25
Give3CookingXP = Recipe.OnGiveXP.Cooking3
Give10CookingXP = Recipe.OnGiveXP.Cooking10
Give10MWXP = Recipe.OnGiveXP.MetalWelding10
Give15MWXP = Recipe.OnGiveXP.MetalWelding15
Give20MWXP = Recipe.OnGiveXP.MetalWelding20
Give25MWXP = Recipe.OnGiveXP.MetalWelding25
Give5WoodworkXP = Recipe.OnGiveXP.WoodWork5
GiveSawLogsXP = Recipe.OnGiveXP.SawLogs
MakeBowlOfSoup2_OnCreate = Recipe.OnCreate.MakeBowlOfSoup2
MakeBowlOfSoup4_OnCreate = Recipe.OnCreate.MakeBowlOfSoup4
MakeBowlOfStew4_OnCreate = Recipe.OnCreate.MakeBowlOfStew4
OnOpenBoxOfJars = Recipe.OnCreate.OpenBoxOfJars
OnPutCakeBatterInBaking = Recipe.OnCreate.PutCakeBatterInBakingPan
OpenCandyPackage_OnCreate = Recipe.OnCreate.OpenCandyPackage
OpenCannedFood_OnCreate = Recipe.OnCreate.OpenCannedFood
OpenEggCarton_OnCreate = Recipe.OnCreate.OpenEggCarton
OpenUmbrella = Recipe.OnCreate.OpenUmbrella
RadioCraft_OnCreate = Recipe.OnCreate.RadioCraft
RadioCraft_OnGiveXP = Recipe.OnGiveXP.RadioCraft
RefillBlowTorch_OnCreate = Recipe.OnCreate.RefillBlowTorch
RefillBlowTorch_OnTest = Recipe.OnTest.RefillBlowTorch
RipClothing_OnCreate = Recipe.OnCreate.RipClothing
SliceBread_OnCreate = Recipe.OnCreate.SliceBread
SliceBreadDough_TestIsValid = Recipe.OnTest.SliceBreadDough
SliceHam_OnCreate = Recipe.OnCreate.SliceHam
SlicePie_OnCreate = Recipe.OnCreate.SlicePie
SliceWatermelon_OnCreate = Recipe.OnCreate.SliceWatermelon
SpikedBat_OnCreate = Recipe.OnCreate.SpikedBat
SplitLogsStack2_OnCreate = Recipe.OnCreate.SplitLogStack2
SplitLogsStack3_OnCreate = Recipe.OnCreate.SplitLogStack3
SplitLogsStack4_OnCreate = Recipe.OnCreate.SplitLogStack4
TorchBatteryInsert_OnCreate = Recipe.OnCreate.TorchBatteryInsert
TorchBatteryInsert_TestIsValid = Recipe.OnTest.TorchBatteryInsert
TorchBatteryRemoval_OnCreate = Recipe.OnCreate.TorchBatteryRemoval
TorchBatteryRemoval_TestIsValid = Recipe.OnTest.TorchBatteryRemoval
TorchDismantle_OnCreate = Recipe.OnCreate.DismantleFlashlight
UpgradeSpear_OnCreate = Recipe.OnCreate.UpgradeSpear
WashClothing_OnCreate = Recipe.OnCreate.WashClothing
WashClothing_TestIsValid = Recipe.OnTest.WashClothing
