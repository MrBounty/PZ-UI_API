-----------------------------------------------------------------------
--                          FARMING MOD                              --
--                      CODE BY ROBERT JOHNSON                       --
--                       TEXTURES BY THUZTOR                         --
-----------------------------------------------------------------------
--                          OFFICIAL TOPIC                           --
--  http://www.theindiestone.com/community/viewtopic.php?f=33&t=8675 --
--                                                                   --
-----------------------------------------------------------------------

---@class farming_vegetableconf
farming_vegetableconf = {};

-- fetch our item in the container, if it's the vegetable we want, we add seeds to it
function getNbOfSeed(nbOfSeed, typeOfPlant, container)
	local result = 0;
	for i = 0, container:getItems():size() - 1 do
		local item = container:getItems():get(i);
		if item:getType() == typeOfPlant then
			result = result + nbOfSeed;
		end
	end
	return result;
end

-- return the number of vegtable you gain with your xp
-- every 10 points over 50 health you plant have = 1 more vegetable
function getVegetablesNumber(min, max, minAutorized, maxAutorized, plant)
	local healthModifier = math.floor((plant.health - 50) /10);
	if healthModifier < 0 then
		healthModifier = 0;
    end

    local vegModifier = 0;
    if SandboxVars.PlantAbundance == 1 then -- very poor
        vegModifier = -4;
    elseif SandboxVars.PlantAbundance == 2 then -- poor
        vegModifier = -2;
    elseif SandboxVars.PlantAbundance == 4 then -- abundant
        vegModifier = 3;
    elseif SandboxVars.PlantAbundance == 5 then -- very abundant
        vegModifier = 5;
    end

	local minV = min + healthModifier + vegModifier;
	local maxV = max + healthModifier + vegModifier;
	if minV > (minAutorized + vegModifier) then
		minV = minAutorized + vegModifier;
	end
	if maxV > (maxAutorized  + vegModifier) then
		maxV = maxAutorized + vegModifier;
	end
	-- I have to add 1 to the maxV, don't know why but the zombRand never take the last digit (ex, between 5 and 10, you'll never have 10...)
	local nbOfVegetable = ZombRand(minV, maxV + 1);
	-- every 10 pts of aphid lower by 1 the vegetable you'll get
	local aphidModifier = math.floor(plant.aphidLvl/10);
	nbOfVegetable = nbOfVegetable - aphidModifier;
	return nbOfVegetable;
end


function calcNextGrowing(nextGrowing, nextTime)
	if nextGrowing then
		return nextGrowing;
	end
    if SandboxVars.Farming == 1 then -- very fast
        nextTime = nextTime / 3;
    end
    if SandboxVars.Farming == 2 then -- fast
        nextTime = nextTime / 1.5;
    end
    if SandboxVars.Farming == 4 then -- slow
        nextTime = nextTime * 1.5;
    end
    if SandboxVars.Farming == 5 then -- very slow
        nextTime = nextTime * 3;
    end
	return SFarmingSystem.instance.hoursElapsed + nextTime;
end

function growNext(planting, typeOfTile, nameOfTile, nextGrowing, howManyTime)
	planting.nextGrowing = calcNextGrowing(nextGrowing, howManyTime)
	planting:setObjectName(nameOfTile)
	planting:setSpriteName(typeOfTile)
	return planting
end

farming_vegetableconf.calcWater = function(waterMin, waterLvl)
	if waterLvl and waterMin then
		-- 1 test, our water lvl is > of our waterMin, it's ok, your plant can grow !
		if waterLvl >= waterMin then
			return 0;
		-- 2 test, our waterLvl is less than 10% less than waterMin, your plant can grow but with more hours (ex : lvlMin 70, you have 65 -> your plant grow + 10 hours)
		elseif waterLvl >= math.floor(waterMin /  1.10) then
			return waterMin - waterLvl;
		-- 3 test, our waterLvl is > 30% less than required waterLvl, your plant can't grow, and the next growing state will be in 5 hours
		elseif waterLvl >= math.floor(waterMin /  1.30) then
			return -1;
		-- if the waterLvl is the plant < 30% less than requiredLvl, the plant will be dead !
		else
			return -2;
		end
	else -- if we're here, it's because of waterMax, we gonna return 0, it's ok, we don't need waterMax for now :)
		return 0;
	end
end

-- check if the disease will up the next time the plant grow
farming_vegetableconf.calcDisease = function(diseaseLvl)
	if diseaseLvl > 0 then
		-- < 10 it's ok
		if diseaseLvl < 10 then
			return 0;
		-- < 30 -> diseaseLvl = hours in supplement for next growing phase
		elseif diseaseLvl < 30 then
			return diseaseLvl;
		elseif diseaseLvl < 60 then -- plant don't grow if disease between 30 and 60
			return -1;
		else -- plant die if disease > 60
			return -2;
		end
	end
	return 0;
end


-- get the object name depending on his current phase
farming_vegetableconf.getObjectName = function(plant)
	if plant.state == "plow" then return getText("Farming_Plowed_Land") end
	if plant.state == "destroy" then return getText("Farming_Destroyed") .. " " .. getText("Farming_" .. plant.typeOfSeed) end
	if plant.state == "dry" then return getText("Farming_Receding") .. " " .. getText("Farming_" .. plant.typeOfSeed) end
	if plant.state == "rotten" then return getText("Farming_Rotten") .. " " .. getText("Farming_" .. plant.typeOfSeed) end
	if plant.nbOfGrow <= 1 then
		return getText("Farming_Seedling") .. " " .. getText("Farming_" ..plant.typeOfSeed);
	elseif plant.nbOfGrow <= 4 then
		return getText("Farming_Young") .. " " .. getText("Farming_" ..plant.typeOfSeed);
	elseif plant.nbOfGrow == 5 then
        if plant.typeOfSeed == "Tomato" then
            return getText("Farming_Young") .. " " .. getText("Farming_" ..plant.typeOfSeed);
        end
		if plant.typeOfSeed == "Strawberry plant" or plant.typeOfSeed == "Potatoes" then
			return getText("Farming_In_bloom") .. " " .. getText("Farming_" ..plant.typeOfSeed);
		else
			return getText("Farming_Ready_for_Harvest") .. " " .. getText("Farming_" ..plant.typeOfSeed);
		end
	elseif plant.nbOfGrow == 6 then
		return getText("Farming_Seed-bearing") .. " " .. getText("Farming_" ..plant.typeOfSeed);
	end
	return "Mystery Plant"
end

farming_vegetableconf.getSpriteName = function(plant)
	if plant.state == "plow" then return "vegetation_farming_01_1" end
	if plant.nbOfGrow <= 0 then
		return farming_vegetableconf.sprite[plant.typeOfSeed][1]
	elseif plant.nbOfGrow <= 4 then
		return farming_vegetableconf.sprite[plant.typeOfSeed][1 + plant.nbOfGrow]
	elseif plant.nbOfGrow == 5 then
		return farming_vegetableconf.sprite[plant.typeOfSeed][6]
	elseif plant.nbOfGrow == 6 then
		return farming_vegetableconf.sprite[plant.typeOfSeed][7]
	else -- rotten
		return farming_vegetableconf.sprite[plant.typeOfSeed][8]
	end
end

-- Carrots
-- Need 12 seeds
-- Need 80 water at phase 1, minimum 70 at 2, then between 35 and 65
-- Grow in 17 days (68h per phase)
farming_vegetableconf.growCarrots = function(planting, nextGrowing, updateNbOfGrow)
	local nbOfGrow = planting.nbOfGrow;
	local water = farming_vegetableconf.calcWater(planting.waterNeeded, planting.waterLvl);
	local waterMax = farming_vegetableconf.calcWater(planting.waterLvl, planting.waterNeededMax);
	local diseaseLvl = farming_vegetableconf.calcDisease(planting.mildewLvl);
	if nbOfGrow == 0 then -- young
		planting = growNext(planting, farming_vegetableconf.getSpriteName(planting), farming_vegetableconf.getObjectName(planting), nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + waterMax + diseaseLvl);
		planting.waterNeeded = 70;
	elseif (nbOfGrow <= 4) then -- young
		if water >= 0 and waterMax >= 0 and diseaseLvl >= 0 then
			planting = growNext(planting, farming_vegetableconf.getSpriteName(planting), farming_vegetableconf.getObjectName(planting), nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + waterMax + diseaseLvl);
			planting.waterNeeded = farming_vegetableconf.props[planting.typeOfSeed].waterLvl;
			planting.waterNeededMax = farming_vegetableconf.props[planting.typeOfSeed].waterLvlMax;
		else
			badPlant(water, waterMax, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (nbOfGrow == 5) then -- mature
		if(water >= 0 and waterMax >= 0 and diseaseLvl >= 0) then
			planting.nextGrowing = calcNextGrowing(nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + waterMax + diseaseLvl);
			planting:setObjectName(farming_vegetableconf.getObjectName(planting))
			planting:setSpriteName(farming_vegetableconf.getSpriteName(planting))
			planting.hasVegetable = true;
		else
			badPlant(water, waterMax, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (nbOfGrow == 6) then -- mature with seed
		if(water >= 0 and waterMax >= 0 and diseaseLvl >= 0) then
			planting.nextGrowing = calcNextGrowing(nextGrowing, 248);
			planting:setObjectName(farming_vegetableconf.getObjectName(planting))
			planting:setSpriteName(farming_vegetableconf.getSpriteName(planting))
			planting.hasVegetable = true;
			planting.hasSeed = true;
		else
			badPlant(water, waterMax, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (planting.state ~= "rotten") then -- rotten
		planting:rottenThis()
	end
	return planting;
end

-- Broccoli
-- need 6 seeds
-- need to have more than 70 water lvl
-- need 4 weeks (112 hours per phase)
farming_vegetableconf.growBroccoli = function(planting, nextGrowing, updateNbOfGrow)
	local nbOfGrow = planting.nbOfGrow;
	local water = farming_vegetableconf.calcWater(planting.waterNeeded, planting.waterLvl);
	local diseaseLvl = farming_vegetableconf.calcDisease(planting.mildewLvl);
	if (nbOfGrow == 0) then -- young
			planting = growNext(planting, farming_vegetableconf.getSpriteName(planting), farming_vegetableconf.getObjectName(planting), nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + diseaseLvl);
			planting.waterNeeded = 70;
	elseif (nbOfGrow <= 4) then -- young
		if(water >= 0 and diseaseLvl >= 0) then
			planting = growNext(planting, farming_vegetableconf.getSpriteName(planting), farming_vegetableconf.getObjectName(planting), nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + diseaseLvl);
			planting.waterNeeded = farming_vegetableconf.props[planting.typeOfSeed].waterLvl;
		else
			badPlant(water, nil, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (nbOfGrow == 5) then -- mature
		if(water >= 0 and diseaseLvl >= 0) then
			planting.nextGrowing = calcNextGrowing(nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + diseaseLvl);
			planting:setObjectName(farming_vegetableconf.getObjectName(planting))
			planting:setSpriteName(farming_vegetableconf.getSpriteName(planting))
			planting.hasVegetable = true;
		else
			badPlant(water, nil, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (nbOfGrow == 6) then -- mature with seed
		if(water >= 0) then
			planting.nextGrowing = calcNextGrowing(nextGrowing, 248);
			planting:setObjectName(farming_vegetableconf.getObjectName(planting))
			planting:setSpriteName(farming_vegetableconf.getSpriteName(planting))
			planting.hasVegetable = true;
			planting.hasSeed = true;
		else
			badPlant(water, nil, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (planting.state ~= "rotten") then -- rotten
		planting:rottenThis()
	end
	return planting;
end

-- Radishes
-- Need 6 seeds
-- Need water lvl between 45 and 75
-- Need 2 weeks (56h per phase)
farming_vegetableconf.growRedRadish = function(planting, nextGrowing, updateNbOfGrow)
	local nbOfGrow = planting.nbOfGrow;
	local water = farming_vegetableconf.calcWater(planting.waterNeeded, planting.waterLvl);
	local waterMax = farming_vegetableconf.calcWater(planting.waterLvl, planting.waterNeededMax);
	local diseaseLvl = farming_vegetableconf.calcDisease(planting.mildewLvl);
	if(nbOfGrow == 0) then -- young
		planting = growNext(planting, farming_vegetableconf.getSpriteName(planting), farming_vegetableconf.getObjectName(planting), nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + waterMax + diseaseLvl);
		planting.waterNeeded = 70;
	elseif (nbOfGrow <= 4) then -- young
		if(water >= 0 and waterMax >= 0) then
			planting = growNext(planting, farming_vegetableconf.getSpriteName(planting), farming_vegetableconf.getObjectName(planting), nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + waterMax + diseaseLvl);
			planting.waterNeeded = farming_vegetableconf.props[planting.typeOfSeed].waterLvl;
			planting.waterNeededMax = farming_vegetableconf.props[planting.typeOfSeed].waterLvlMax;
		else
			badPlant(water, waterMax, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (nbOfGrow == 5) then -- mature
		if(water >= 0 and waterMax >= 0) then
			planting.nextGrowing = calcNextGrowing(nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + waterMax + diseaseLvl);
			planting:setObjectName(farming_vegetableconf.getObjectName(planting))
			planting:setSpriteName(farming_vegetableconf.getSpriteName(planting))
			planting.hasVegetable = true;
		else
			badPlant(water, waterMax, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (nbOfGrow == 6) then -- mature with seed
		if(water >= 0 and waterMax >= 0) then
			planting.nextGrowing = calcNextGrowing(nextGrowing, 224);
			planting:setObjectName(farming_vegetableconf.getObjectName(planting))
			planting:setSpriteName(farming_vegetableconf.getSpriteName(planting))
			planting.hasVegetable = true;
			planting.hasSeed = true;
		else
			badPlant(water, waterMax, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (planting.state ~= "rotten") then -- rotten
		planting:rottenThis()
	end
	return planting;
end

-- Strawberry
-- Need 12 seeds
-- Need water lvl over 85
-- need 3 weeks to grow (84h per phase)
-- On harvest, don't disapear but go again on phase 2
farming_vegetableconf.growStrewberries = function(planting, nextGrowing, updateNbOfGrow)
	local nbOfGrow = planting.nbOfGrow;
	local water = farming_vegetableconf.calcWater(planting.waterNeeded, planting.waterLvl);
	local diseaseLvl = farming_vegetableconf.calcDisease(planting.mildewLvl);
	if(nbOfGrow == 0) then -- young
		planting = growNext(planting, farming_vegetableconf.getSpriteName(planting), farming_vegetableconf.getObjectName(planting), nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + diseaseLvl);
		planting.waterNeeded = 85;
	elseif (nbOfGrow <= 4) then -- young
		if(water >= 0 and diseaseLvl >= 0) then
			planting = growNext(planting, farming_vegetableconf.getSpriteName(planting), farming_vegetableconf.getObjectName(planting), nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + diseaseLvl);
			planting.waterNeeded = farming_vegetableconf.props[planting.typeOfSeed].waterLvl;
		else
			badPlant(water, nil, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (nbOfGrow == 5) then -- bloom
		if(water >= 0 and diseaseLvl >= 0) then
			planting = growNext(planting, farming_vegetableconf.getSpriteName(planting), farming_vegetableconf.getObjectName(planting), nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + diseaseLvl);
		else
			badPlant(water, nil, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (nbOfGrow == 6) then -- mature with seed
		if(water >= 0 and diseaseLvl >= 0) then
			planting.nextGrowing = calcNextGrowing(nextGrowing, 236);
			planting:setObjectName(farming_vegetableconf.getObjectName(planting))
			planting:setSpriteName(farming_vegetableconf.getSpriteName(planting))
			planting.hasVegetable = true;
			planting.hasSeed = true;
		else
			badPlant(water, nil, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (planting.state ~= "rotten") then -- rotten
		planting:rottenThis()
	end
	return planting;
end

-- Tomatos
-- Need 4 seeds and 4 sticks
-- Need water lvl over 75
-- Need 17 days weeks (68h per phase)
farming_vegetableconf.growTomato = function(planting, nextGrowing, updateNbOfGrow)
	local nbOfGrow = planting.nbOfGrow;
	local water = farming_vegetableconf.calcWater(planting.waterNeeded, planting.waterLvl);
	local diseaseLvl = farming_vegetableconf.calcDisease(planting.mildewLvl);
	if(nbOfGrow == 0) then -- young
		planting = growNext(planting, farming_vegetableconf.getSpriteName(planting), farming_vegetableconf.getObjectName(planting), nextGrowing,farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + diseaseLvl);
		planting.waterNeeded = 80;
	elseif (nbOfGrow <= 4) then -- young
		if(water >= 0 and diseaseLvl >= 0) then
			planting = growNext(planting, farming_vegetableconf.getSpriteName(planting), farming_vegetableconf.getObjectName(planting), nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + diseaseLvl);
			planting.waterNeeded = farming_vegetableconf.props[planting.typeOfSeed].waterLvl;
		else
			badPlant(water, nil, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (nbOfGrow == 5) then -- bloom (don't have vegetable)
		if(water >= 0 and diseaseLvl >= 0) then
			planting = growNext(planting, farming_vegetableconf.getSpriteName(planting), farming_vegetableconf.getObjectName(planting), nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + diseaseLvl);
		else
			badPlant(water, nil, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (nbOfGrow == 6) then -- mature with seed
		if(water >= 0 and diseaseLvl >= 0) then
			planting.nextGrowing = calcNextGrowing(nextGrowing, 248);
			planting:setObjectName(farming_vegetableconf.getObjectName(planting))
			planting:setSpriteName(farming_vegetableconf.getSpriteName(planting))
			planting.hasVegetable = true;
			planting.hasSeed = true;
		else
			badPlant(water, nil, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (planting.state ~= "rotten") then -- rotten
		planting:rottenThis()
	end
	return planting;
end

-- Potatos
-- Need 4 seeds
-- Water lvl over 65
-- Need 3 weeks (84h per phase)
farming_vegetableconf.growPotato = function(planting, nextGrowing, updateNbOfGrow)
	local nbOfGrow = planting.nbOfGrow;
	local water = farming_vegetableconf.calcWater(planting.waterNeeded, planting.waterLvl);
	local diseaseLvl = farming_vegetableconf.calcDisease(planting.mildewLvl);
	if(nbOfGrow == 0) then -- young
		planting = growNext(planting, farming_vegetableconf.getSpriteName(planting), farming_vegetableconf.getObjectName(planting), nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + diseaseLvl);
		planting.waterNeeded = 70;
	elseif (nbOfGrow <= 4) then -- young
		if(water >= 0 and diseaseLvl >= 0) then
			planting = growNext(planting, farming_vegetableconf.getSpriteName(planting), farming_vegetableconf.getObjectName(planting), nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + diseaseLvl);
			planting.waterNeeded = farming_vegetableconf.props[planting.typeOfSeed].waterLvl;
			planting.waterNeededMax = farming_vegetableconf.props[planting.typeOfSeed].waterLvlMax;
		else
			badPlant(water, nil, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (nbOfGrow == 5) then -- mature
		if(water >= 0 and diseaseLvl >= 0) then
			planting.nextGrowing = calcNextGrowing(nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + diseaseLvl);
			planting:setObjectName(farming_vegetableconf.getObjectName(planting))
			planting:setSpriteName(farming_vegetableconf.getSpriteName(planting))
			planting.hasVegetable = true;
		else
			badPlant(water, nil, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (nbOfGrow == 6) then -- mature with seed
		if(water >= 0 and diseaseLvl >= 0) then
			planting.nextGrowing = calcNextGrowing(nextGrowing, 48);
			planting:setObjectName(farming_vegetableconf.getObjectName(planting))
			planting:setSpriteName(farming_vegetableconf.getSpriteName(planting))
			planting.hasVegetable = true;
			planting.hasSeed = true;
		else
			badPlant(water, nil, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (planting.state ~= "rotten") then -- rotten
		planting:rottenThis()
	end
	return planting;
end

-- Cabbage
-- Need 9 seeds
-- Water lvl over 85
-- Need 4 weeks to grow (112h per phase)
farming_vegetableconf.growCabbage = function(planting, nextGrowing, updateNbOfGrow)
	local nbOfGrow = planting.nbOfGrow;
	local water = farming_vegetableconf.calcWater(planting.waterNeeded, planting.waterLvl);
	local diseaseLvl = farming_vegetableconf.calcDisease(planting.mildewLvl);
	if(nbOfGrow <= 0) then -- young
		nbOfGrow = 0;
		planting.nbOfGrow = 0;
		planting = growNext(planting, farming_vegetableconf.getSpriteName(planting), farming_vegetableconf.getObjectName(planting), nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + diseaseLvl);
		planting.waterNeeded = 80;
	elseif (nbOfGrow <= 4) then -- young
		if(water >= 0 and diseaseLvl >= 0) then
			planting = growNext(planting, farming_vegetableconf.getSpriteName(planting), farming_vegetableconf.getObjectName(planting), nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + diseaseLvl);
			planting.waterNeeded = farming_vegetableconf.props[planting.typeOfSeed].waterLvl;
		else
			badPlant(water, nil, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (nbOfGrow == 5) then -- mature
		if(water >= 0 and diseaseLvl >= 0) then
			planting.nextGrowing = calcNextGrowing(nextGrowing, farming_vegetableconf.props[planting.typeOfSeed].timeToGrow + water + diseaseLvl);
			planting:setObjectName(farming_vegetableconf.getObjectName(planting))
			planting:setSpriteName(farming_vegetableconf.getSpriteName(planting))
			planting.hasVegetable = true;
		else
			badPlant(water, nil, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (nbOfGrow == 6) then -- mature with seed
		if(water >= 0 and diseaseLvl >= 0) then
			planting.nextGrowing = calcNextGrowing(nextGrowing, 48);
			planting:setObjectName(farming_vegetableconf.getObjectName(planting))
			planting:setSpriteName(farming_vegetableconf.getSpriteName(planting))
			planting.hasVegetable = true;
			planting.hasSeed = true;
		else
			badPlant(water, nil, diseaseLvl, planting, nextGrowing, updateNbOfGrow);
		end
	elseif (planting.state ~= "rotten") then -- rotten
		planting:rottenThis()
	end
	return planting;
end

-- if we doesn't take well care of our plant
-- if the plant is not well watered or have a little disease, the growing is reported (50 hours)
-- if disease is too much (> 60) then the plant die
-- if water or waterMax == -2 (too much or too less watered), the growing is reported to 100 hours and lose a bunch of health
function badPlant(water, waterMax, diseaseLvl, plant, nextGrowing, updateNbOfGrow)
	if not waterMax then
		waterMax = 1;
	end
	-- if we're here, it's because we didn't take well care of our plant, so we notice it, we'll have less xp from this plant
	if water <= -1 or waterMax <= -1 then
		plant.badCare = true;
    end

    if diseaseLvl == -2 then -- our plant is dead if disease > 60
        plant:dryThis();
        return;
    end

	if water == -1 or waterMax == -1 or diseaseLvl == -1 then-- we report our growing
		plant.nextGrowing = calcNextGrowing(nextGrowing, 30);
		if updateNbOfGrow then
			plant.nbOfGrow = plant.nbOfGrow -1;
        end
        return;
    end

    plant.nextGrowing = calcNextGrowing(nextGrowing, 50);
    plant.health = plant.health - 4;
    if updateNbOfGrow then
        plant.nbOfGrow = plant.nbOfGrow -1;
    end
end

farming_vegetableconf.icons = {}
farming_vegetableconf.icons["Carrots"] = "Item_Carrots";
farming_vegetableconf.icons["Broccoli"] = "Item_Broccoli";
farming_vegetableconf.icons["Radishes"] = "Item_TZ_LRRadish";
farming_vegetableconf.icons["Strawberry plant"] = "Item_TZ_Strewberry";
farming_vegetableconf.icons["Tomato"] = "Item_TZ_Tomato";
farming_vegetableconf.icons["Potatoes"] = "Item_TZ_Potato";
farming_vegetableconf.icons["Cabbages"] = "Item_TZ_CabbageLettuce";

farming_vegetableconf.props = {};
-- Carrots (12 to 16 weeks to grow)
farming_vegetableconf.props["Carrots"] = {};
farming_vegetableconf.props["Carrots"].seedsRequired = 12;
farming_vegetableconf.props["Carrots"].texture = "vegetation_farming_01_38";
farming_vegetableconf.props["Carrots"].waterLvl = 35;
farming_vegetableconf.props["Carrots"].waterLvlMax = 85;
farming_vegetableconf.props["Carrots"].timeToGrow = ZombRand(50,55);
farming_vegetableconf.props["Carrots"].minVeg = 3;
farming_vegetableconf.props["Carrots"].maxVeg = 6;
farming_vegetableconf.props["Carrots"].minVegAutorized = 10;
farming_vegetableconf.props["Carrots"].maxVegAutorized = 15;
farming_vegetableconf.props["Carrots"].vegetableName = "Base.Carrots";
farming_vegetableconf.props["Carrots"].seedName = "farming.CarrotSeed";
farming_vegetableconf.props["Carrots"].seedPerVeg = 3;
--~ -- Broccoli (20 to 24 weeks to grow)
farming_vegetableconf.props["Broccoli"] = {};
farming_vegetableconf.props["Broccoli"].waterLvl = 70;
farming_vegetableconf.props["Broccoli"].seedsRequired = 6;
farming_vegetableconf.props["Broccoli"].texture = "vegetation_farming_01_30";
farming_vegetableconf.props["Broccoli"].timeToGrow = ZombRand(103, 117);
farming_vegetableconf.props["Broccoli"].minVeg = 2;
farming_vegetableconf.props["Broccoli"].maxVeg = 4;
farming_vegetableconf.props["Broccoli"].minVegAutorized = 6;
farming_vegetableconf.props["Broccoli"].maxVegAutorized = 8;
farming_vegetableconf.props["Broccoli"].vegetableName = "Base.Broccoli";
farming_vegetableconf.props["Broccoli"].seedName = "farming.BroccoliSeed";
farming_vegetableconf.props["Broccoli"].seedPerVeg = 3;
-- Radishes (6 to 8 weeks to grow)
farming_vegetableconf.props["Radishes"] = {};
farming_vegetableconf.props["Radishes"].seedsRequired = 6;
farming_vegetableconf.props["Radishes"].texture = "vegetation_farming_01_54";
farming_vegetableconf.props["Radishes"].waterLvl = 45;
farming_vegetableconf.props["Radishes"].waterLvlMax = 85;
farming_vegetableconf.props["Radishes"].timeToGrow = ZombRand(56, 62);
farming_vegetableconf.props["Radishes"].minVeg = 4;
farming_vegetableconf.props["Radishes"].maxVeg = 9;
farming_vegetableconf.props["Radishes"].minVegAutorized = 11;
farming_vegetableconf.props["Radishes"].maxVegAutorized = 15;
farming_vegetableconf.props["Radishes"].vegetableName = "farming.RedRadish";
farming_vegetableconf.props["Radishes"].seedName = "farming.RedRadishSeed";
farming_vegetableconf.props["Radishes"].seedPerVeg = 4;
-- Strawberry (24 to 28 weeks to grow)
farming_vegetableconf.props["Strawberry plant"] = {};
farming_vegetableconf.props["Strawberry plant"].seedsRequired = 12;
farming_vegetableconf.props["Strawberry plant"].texture = "vegetation_farming_01_62";
farming_vegetableconf.props["Strawberry plant"].waterLvl = 85;
farming_vegetableconf.props["Strawberry plant"].timeToGrow = ZombRand(103, 131);
farming_vegetableconf.props["Strawberry plant"].minVeg = 4;
farming_vegetableconf.props["Strawberry plant"].maxVeg = 6;
farming_vegetableconf.props["Strawberry plant"].minVegAutorized = 8;
farming_vegetableconf.props["Strawberry plant"].maxVegAutorized = 14;
farming_vegetableconf.props["Strawberry plant"].vegetableName = "farming.Strewberrie";
farming_vegetableconf.props["Strawberry plant"].seedName = "farming.StrewberrieSeed";
farming_vegetableconf.props["Strawberry plant"].seedPerVeg = 3;
-- Tomatos (16 to 20 weeks to grow)
farming_vegetableconf.props["Tomato"] = {};
farming_vegetableconf.props["Tomato"].seedsRequired = 4;
farming_vegetableconf.props["Tomato"].texture = "vegetation_farming_01_70";
farming_vegetableconf.props["Tomato"].waterLvl = 75;
farming_vegetableconf.props["Tomato"].timeToGrow = ZombRand(89, 103);
farming_vegetableconf.props["Tomato"].minVeg = 4;
farming_vegetableconf.props["Tomato"].maxVeg = 5;
farming_vegetableconf.props["Tomato"].minVegAutorized = 6;
farming_vegetableconf.props["Tomato"].maxVegAutorized = 10;
farming_vegetableconf.props["Tomato"].vegetableName = "farming.Tomato";
farming_vegetableconf.props["Tomato"].seedName = "farming.TomatoSeed";
farming_vegetableconf.props["Tomato"].seedPerVeg = 2;
-- Potatos (16 to 20 weeks to grow)
farming_vegetableconf.props["Potatoes"] = {};
farming_vegetableconf.props["Potatoes"].seedsRequired = 4;
farming_vegetableconf.props["Potatoes"].texture = "vegetation_farming_01_46";
farming_vegetableconf.props["Potatoes"].waterLvl = 65;
farming_vegetableconf.props["Potatoes"].timeToGrow = ZombRand(89, 103);
farming_vegetableconf.props["Potatoes"].minVeg = 3;
farming_vegetableconf.props["Potatoes"].maxVeg = 4;
farming_vegetableconf.props["Potatoes"].minVegAutorized = 5;
farming_vegetableconf.props["Potatoes"].maxVegAutorized = 9;
farming_vegetableconf.props["Potatoes"].vegetableName = "farming.Potato";
farming_vegetableconf.props["Potatoes"].seedName = "farming.PotatoSeed";
farming_vegetableconf.props["Potatoes"].seedPerVeg = 3;
-- Cabbage Lettuce (6 to 8 weeks to grow)
farming_vegetableconf.props["Cabbages"] = {};
farming_vegetableconf.props["Cabbages"].seedsRequired = 9;
farming_vegetableconf.props["Cabbages"].texture = "vegetation_farming_01_21";
farming_vegetableconf.props["Cabbages"].waterLvl = 85;
farming_vegetableconf.props["Cabbages"].timeToGrow = ZombRand(46, 52);
farming_vegetableconf.props["Cabbages"].vegetableName = "farming.Cabbage";
farming_vegetableconf.props["Cabbages"].seedName = "farming.CabbageSeed";
farming_vegetableconf.props["Cabbages"].seedPerVeg = 3;
farming_vegetableconf.props["Cabbages"].minVeg = 4;
farming_vegetableconf.props["Cabbages"].maxVeg = 6;
farming_vegetableconf.props["Cabbages"].minVegAutorized = 9;
farming_vegetableconf.props["Cabbages"].maxVegAutorized = 11;

farming_vegetableconf.sprite = {}
farming_vegetableconf.sprite["Carrots"] = {
"vegetation_farming_01_32",
"vegetation_farming_01_33",
"vegetation_farming_01_34",
"vegetation_farming_01_35",
"vegetation_farming_01_36",
"vegetation_farming_01_37",
"vegetation_farming_01_38",
"vegetation_farming_01_39"
}
farming_vegetableconf.sprite["Broccoli"] = {
"vegetation_farming_01_24",
"vegetation_farming_01_25",
"vegetation_farming_01_26",
"vegetation_farming_01_27",
"vegetation_farming_01_28",
"vegetation_farming_01_30",
"vegetation_farming_01_29",
"vegetation_farming_01_31"
}
farming_vegetableconf.sprite["Radishes"] = {
"vegetation_farming_01_48",
"vegetation_farming_01_49",
"vegetation_farming_01_50",
"vegetation_farming_01_51",
"vegetation_farming_01_52",
"vegetation_farming_01_54",
"vegetation_farming_01_53",
"vegetation_farming_01_55"
}
farming_vegetableconf.sprite["Strawberry plant"] = {
"vegetation_farming_01_56",
"vegetation_farming_01_57",
"vegetation_farming_01_58",
"vegetation_farming_01_59",
"vegetation_farming_01_60",
"vegetation_farming_01_61",
"vegetation_farming_01_62",
"vegetation_farming_01_63"
}
farming_vegetableconf.sprite["Tomato"] = {
"vegetation_farming_01_64",
"vegetation_farming_01_65",
"vegetation_farming_01_66",
"vegetation_farming_01_67",
"vegetation_farming_01_68",
"vegetation_farming_01_69",
"vegetation_farming_01_70",
"vegetation_farming_01_71"
}
farming_vegetableconf.sprite["Potatoes"] = {
"vegetation_farming_01_40",
"vegetation_farming_01_41",
"vegetation_farming_01_42",
"vegetation_farming_01_43",
"vegetation_farming_01_44",
"vegetation_farming_01_46",
"vegetation_farming_01_45",
"vegetation_farming_01_47"
}
farming_vegetableconf.sprite["Cabbages"] = {
"vegetation_farming_01_16",
"vegetation_farming_01_17",
"vegetation_farming_01_18",
"vegetation_farming_01_19",
"vegetation_farming_01_20",
"vegetation_farming_01_22",
"vegetation_farming_01_21",
"vegetation_farming_01_23"
}

