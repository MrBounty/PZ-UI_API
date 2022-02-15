--***********************************************************
--**                    ROBERT JOHNSON                     **
--**       Here we gonna handle all the xp add/lose        **
--***********************************************************

xpUpdate = {};
xpUpdate.characterInfo = nil;
-- timer to see how much time since last strength xp gain, if it's too long we start losing xp

-- used everytime the player move
xpUpdate.onPlayerMove = function()
	local player = getPlayer();
	local xp = player:getXp();
	-- pacing/sprinting xp
	-- if you're running and your endurance has changed
	if (player:IsRunning() or player:isSprinting()) and player:getStats():getEndurance() > player:getStats():getEndurancewarn() then
		-- you may gain 1 xp in sprinting or fitness
		if xpUpdate.randXp() then
			xp:AddXP(Perks.Fitness, 1);
		end
		if xpUpdate.randXp() then
			xp:AddXP(Perks.Sprinting, 1);
		end
	end
	-- aiming while moving, gain nimble xp (move faster in aiming mode)
	if player:isAiming() and xpUpdate.randXp() then
		xp:AddXP(Perks.Nimble, 1);
	end
	-- if you're walking with a lot of stuff, you may gain in Strength
	if player:getInventoryWeight() > player:getMaxWeight() * 0.5 then
		if xpUpdate.randXp() then
			xp:AddXP(Perks.Strength, 2);
		end
	end
end

-- when you or a npc try to hit a tree
xpUpdate.OnWeaponHitTree = function(owner, weapon)
	if weapon and weapon:getType() ~= "BareHands" then
		owner:getXp():AddXP(Perks.Strength, 2);
	end
end

-- when you or a npc try to hit something
xpUpdate.onWeaponHitXp = function(owner, weapon, hitObject, damage)
    local isShove = false
    if hitObject:isOnFloor() == false and weapon:getType() == "BareHands" then
        isShove = true
    end
	local exp = 1 * damage * 0.9;
	if exp > 3 then
		exp = 3;
	end
	-- add info of favourite weapon
	local modData = owner:getModData();
    if isShove == false then
        if modData["Fav:"..weapon:getName()] == nil then
            modData["Fav:"..weapon:getName()] = 1;
        else
            modData["Fav:"..weapon:getName()] = modData["Fav:"..weapon:getName()] + 1;
        end
    end
	-- if you sucessful swing your non ranged weapon
	if owner:getStats():getEndurance() > owner:getStats():getEndurancewarn() and not weapon:isRanged() then
		owner:getXp():AddXP(Perks.Fitness, 1);
	end
	-- we add xp depending on how many target you hit
	if not weapon:isRanged() and owner:getLastHitCount() > 0 then
		owner:getXp():AddXP(Perks.Strength, owner:getLastHitCount());
	end
	-- add xp for ranged weapon
	if weapon:isRanged() then
		local xp = owner:getLastHitCount();
		if owner:getPerkLevel(Perks.Aiming) < 5 then
			xp = xp * 2.7;
		end
		owner:getXp():AddXP(Perks.Aiming, xp);
	end
	-- add either blunt or blade xp (blade xp's perk name is Axe)
	if owner:getLastHitCount() > 0 and not weapon:isRanged() then
		if weapon:getScriptItem():getCategories():contains("Axe") then
			owner:getXp():AddXP(Perks.Axe, exp);
		end
		if weapon:getScriptItem():getCategories():contains("Blunt") then
			owner:getXp():AddXP(Perks.Blunt, exp);
		end
		if weapon:getScriptItem():getCategories():contains("Spear") then
			owner:getXp():AddXP(Perks.Spear, exp);
		end
		if weapon:getScriptItem():getCategories():contains("LongBlade") then
			owner:getXp():AddXP(Perks.LongBlade, exp);
		end
		if weapon:getScriptItem():getCategories():contains("SmallBlade") then
			owner:getXp():AddXP(Perks.SmallBlade, exp);
		end
		if weapon:getScriptItem():getCategories():contains("SmallBlunt") then
			owner:getXp():AddXP(Perks.SmallBlunt, exp);
		end
	end
end

-- get xp when you craft something
xpUpdate.onMakeItem = function(item, resultItem, recipe)
	if instanceof(resultItem, "Food") then
		getPlayer():getXp():AddXP(Perks.Cooking, 3);
	end
--~ 	if resultItem:getType():contains("Plank") then
--~ 		getPlayer():getXp():AddXP(Perks.Woodwork, 3);
--~ 	end
--~ 	if item:getType():contains("Plank") then
--~ 		getPlayer():getXp():AddXP(Perks.Woodwork, 3);
--~ 	end

end

-- if we press the toggle skill panel key we gonna display the character info screen
xpUpdate.displayCharacterInfo = function(key)
	local playerObj = getSpecificPlayer(0)
	if getGameSpeed() == 0 or not playerObj or playerObj:isDead() then
		return;
	end
	if not getPlayerData(0) then return end
	if key == getCore():getKey("Toggle Skill Panel") then
		xpUpdate.characterInfo = getPlayerInfoPanel(playerObj:getPlayerNum());
		xpUpdate.characterInfo:toggleView(xpSystemText.skills);
	end
	if key == getCore():getKey("Toggle Health Panel") then
		xpUpdate.characterInfo = getPlayerInfoPanel(playerObj:getPlayerNum());
		xpUpdate.characterInfo:toggleView(xpSystemText.health);
        xpUpdate.characterInfo.healthView.doctorLevel = playerObj:getPerkLevel(Perks.Doctor);
	end
	if key == getCore():getKey("Toggle Info Panel") then
		xpUpdate.characterInfo = getPlayerInfoPanel(playerObj:getPlayerNum());
		xpUpdate.characterInfo:toggleView(xpSystemText.info);
	end
	if key == getCore():getKey("Toggle Clothing Protection Panel") then
		xpUpdate.characterInfo = getPlayerInfoPanel(playerObj:getPlayerNum());
		xpUpdate.characterInfo:toggleView(xpSystemText.protection);
	end
end

-- do we get xp ?
xpUpdate.randXp = function()
	return ZombRand(700 * GameTime:getInstance():getInvMultiplier()) == 0;
end

-- handle when you gain xp, we gonna apply the xp multiplier
xpUpdate.addXp = function(owner, type, amount)
	-- reset our strength/fitness timer

	local modData = xpUpdate.getModData(owner)

	if type == Perks.Strength and amount > 0 then
		modData.strengthUpTimer = modData.strengthUpTimer - 3000; -- allow us to not lose strength xp for a time if we still train
		if modData.strengthUpTimer < -50000 then
			modData.strengthUpTimer = -50000;
		end
	end

	if type == Perks.Fitness and amount > 0 then
		modData.fitnessUpTimer = modData.fitnessUpTimer - 3000; -- allow us to not lose fitness xp for a time if we still train
		if modData.fitnessUpTimer < -50000 then
			modData.fitnessUpTimer = -50000;
		end
	end
end

-- when you gain a level you could win or lose perks
xpUpdate.levelPerk = function(owner, perk, level, addBuffer)
	-- first Strength skill, grant you some traits that gonna help you to carry more stuff, hitting harder, etc.
	if perk == Perks.Strength then
		-- we start to remove all previous Strength related traits
		owner:getTraits():remove("Weak");
		owner:getTraits():remove("Feeble");
		owner:getTraits():remove("Stout");
		owner:getTraits():remove("Strong");

        -- now we add trait depending on your current lvl
        if level >= 0 and level <= 1 then
            owner:getTraits():add("Weak");
        elseif level >= 2 and level <= 4 then
            owner:getTraits():add("Feeble");
        elseif level >= 6 and level <= 8 then
            owner:getTraits():add("Stout");
        elseif level >= 9 then
            owner:getTraits():add("Strong");
        end
	end

	-- then Fitness skill, grant you some traits that gonna help you to run faster, recovery faster, etc..
	if perk == Perks.Fitness then
		-- we start to remove all previous Fitness related traits
        owner:getTraits():remove("Unfit");
        owner:getTraits():remove("Out of Shape");
		owner:getTraits():remove("Fit");
		owner:getTraits():remove("Athletic");

		-- now we add trait depending on your current lvl
		if level >= 0 and level <= 1 then
			owner:getTraits():add("Unfit");
		elseif level >= 2 and level <= 4 then
			owner:getTraits():add("Out of Shape");
		elseif level >= 6 and level <= 8 then
			owner:getTraits():add("Fit");
		elseif level >= 9 then
			owner:getTraits():add("Athletic");
		end
	end

	-- we reset the xp multiplier for this perk
--	owner:getXp():getMultiplierMap():remove(perk);

	-- we add a "buffer" xp, so if you just get your lvl but you're still losing xp (if you've been lazy for a moment), you won't lose your lvl at the next tick
	if addBuffer then
--		owner:getXp():AddXP(perk, 5, false);
	end
end

xpUpdate.checkForLosingLevel = function(playerObj, perk)
	local info = playerObj:getPerkInfo(perk);
	if info then
		local level = info:getLevel()
		if level >= 1 and level <= 10 and playerObj:getXp():getXP(perk) < PerkFactory.getPerk(perk):getTotalXpForLevel(level) then
			playerObj:LoseLevel(perk);
        end
	end
end

xpUpdate.everyTenMinutes = function()
	for playerIndex=0,getNumActivePlayers()-1 do
		local playerObj = getSpecificPlayer(playerIndex)
		if playerObj and not playerObj:isDead() then
			local modData = xpUpdate.getModData(playerObj)
			-- strength stuff
			modData.strengthUpTimer = modData.strengthUpTimer + 10;

			-- if we've been lazy for too long, we start losing xp, every 1200 tick we lose 1 xp
			if modData.strengthUpTimer > 20000 and modData.strengthMod ~= math.floor(modData.strengthUpTimer / 1200) then
				modData.strengthMod = math.floor(modData.strengthUpTimer / 1200);
				if playerObj:getXp():getXP(Perks.Strength) > 0 then
					playerObj:getXp():AddXP(Perks.Strength, -1);
				end
				xpUpdate.checkForLosingLevel(playerObj, Perks.Strength);
			end
			if modData.strengthUpTimer > 31000 then -- it's caped to a 30000 timer, so if you've been lazy for a lot of days, it's not so long to get in shape again
				modData.strengthUpTimer = 0;
			end
			-- fitness stuff
			modData.fitnessUpTimer = modData.fitnessUpTimer + 10;
			-- if we've been lazy for too long, we start losing xp, every 1200 tick we lose 1 xp
			if modData.fitnessUpTimer > 20000 and modData.fitnessMod ~= math.floor(modData.fitnessUpTimer / 1200) then
				modData.fitnessMod = math.floor(modData.fitnessUpTimer / 1200);
				if playerObj:getXp():getXP(Perks.Fitness) > 0 then
					playerObj:getXp():AddXP(Perks.Fitness, -1);
				end
			end
			if modData.fitnessUpTimer > 31000 then -- it's caped to a 30000 timer, so if you've been lazy for a lot of days, it's not so long to get in shape again
				modData.fitnessUpTimer = 0;
			end
		end
    end
end

-- load our losing xp timer
xpUpdate.getModData = function(playerObj)
    if playerObj then
		local modData = playerObj:getModData()
		modData.strengthUpTimer = tonumber(modData.strengthUpTimer) or -50000
		modData.strengthMod = modData.strengthMod or 0
		modData.fitnessUpTimer = tonumber(modData.fitnessUpTimer) or -50000
		modData.fitnessMod = modData.fitnessMod or 0
		return modData
	end
	return nil
end

xpUpdate.onNewGame = function(playerObj, square)
	-- spawn with a belt
	local belt = playerObj:getInventory():AddItem("Base.Belt2");
	playerObj:setWornItem(belt:getBodyLocation(), belt);
    if SandboxVars.StarterKit then
        local bag = playerObj:getInventory():AddItem("Base.Bag_Schoolbag");
        local bat = bag:getItemContainer():AddItem("Base.BaseballBat");
        bat:setCondition(7);
        local hammer = bag:getItemContainer():AddItem("Base.Hammer");
        hammer:setCondition(5);
        playerObj:getInventory():AddItem("Base.WaterBottleFull");
        playerObj:getInventory():AddItem("Base.Crisps");

        playerObj:setClothingItem_Back(bag);
    end
    if getWorld():getDifficulty() == "Easy" then
        local bag =  playerObj:getInventory():FindAndReturn("Base.Bag_Schoolbag");
        if not bag then
            bag = playerObj:getInventory():AddItem("Base.Bag_Schoolbag");
            playerObj:getInventory():AddItem("Base.WaterBottleFull");
            bag:getItemContainer():AddItem("Base.BaseballBat");
            bag:getItemContainer():AddItem("Base.Hammer");
            playerObj:getInventory():AddItem("Base.Crisps");
            playerObj:setClothingItem_Back(bag);
        end
        bag:getItemContainer():AddItem("Base.Saw");
        playerObj:getInventory():AddItem("Base.Crisps2");
        playerObj:getInventory():AddItem("Base.Crisps3");
    elseif getWorld():getDifficulty() == "Normal" then
        local bag =  playerObj:getInventory():FindAndReturn("Base.Bag_Schoolbag");
        if not bag then
            bag = playerObj:getInventory():AddItem("Base.Bag_Schoolbag");
            local bat = bag:getItemContainer():AddItem("Base.BaseballBat");
            bat:setCondition(7);
            local hammer = bag:getItemContainer():AddItem("Base.Hammer");
            hammer:setCondition(5);
            playerObj:setClothingItem_Back(bag);
        end
        playerObj:getInventory():AddItem("Base.WaterBottleFull");
        playerObj:getInventory():AddItem("Base.Crisps");
    elseif getWorld():getDifficulty() == "Hard" then
        playerObj:getInventory():AddItem("Base.WaterBottleFull");
        playerObj:getInventory():AddItem("Base.Crisps");
	end
	
	playerObj:getFitness():init();
end

-- temp, need to remove this & option when everyone got it basically...
xpUpdate.OnGameStart = function()
	if not getCore():gotNewBelt() then
		local playerObj = getSpecificPlayer(0);
		local belt = playerObj:getInventory():AddItem("Base.Belt2");
		playerObj:setWornItem(belt:getBodyLocation(), belt);
		getCore():setGotNewBelt(true);
		getCore():saveOptions();
		local hotbar = getPlayerHotbar(0)
		if hotbar then
			hotbar:refresh();
		end
	end
end

Events.EveryTenMinutes.Add(xpUpdate.everyTenMinutes);

Events.OnPlayerMove.Add(xpUpdate.onPlayerMove);

Events.OnWeaponHitXp.Add(xpUpdate.onWeaponHitXp);

Events.OnWeaponHitTree.Add(xpUpdate.OnWeaponHitTree);

--Events.OnMakeItem.Add(xpUpdate.onMakeItem);

Events.OnKeyPressed.Add(xpUpdate.displayCharacterInfo);

Events.AddXP.Add(xpUpdate.addXp);

Events.LevelPerk.Add(xpUpdate.levelPerk);

Events.OnNewGame.Add(xpUpdate.onNewGame);

Events.OnGameStart.Add(xpUpdate.OnGameStart);
