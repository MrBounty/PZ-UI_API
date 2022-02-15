if debugScenarios == nil then
	debugScenarios = {}
end


debugScenarios.Trailer2_LimpWPScenario = {
	name = "LIFE 2 - END",
--	forceLaunch = true, -- use this to force the launch of THIS scenario right after main menu was loaded, save more clicks! Don't do multiple scenarii with this options
	startLoc = {x=11865, y=6899, z=0 }, -- WP shopping street
	setSandbox = function()
		
		SandboxVars.Speed = 3;
		SandboxVars.Zombies = 3;
		SandboxVars.Distribution = 1;
		SandboxVars.Survivors = 1;
		SandboxVars.DayLength = 3;
		SandboxVars.StartYear = 1;
		SandboxVars.StartMonth = 7;
		SandboxVars.StartDay = 9;
		SandboxVars.StartTime = 4;
		SandboxVars.VehicleEasyUse = true;
		SandboxVars.WaterShutModifier = 30;
		SandboxVars.ElecShutModifier = 30;
		SandboxVars.WaterShut = 2;
		SandboxVars.ElecShut = 2;
		SandboxVars.FoodLoot = 2;
		SandboxVars.WeaponLoot = 2;
		SandboxVars.OtherLoot = 2;
		SandboxVars.Temperature = 3;
		SandboxVars.Rain = 3;
		SandboxVars.ErosionSpeed = 3;
		SandboxVars.XpMultiplier = 1.0;
		SandboxVars.StatsDecrease = 3;
		SandboxVars.NatureAbundance = 3;
		SandboxVars.Alarm = 1;
		SandboxVars.LockedHouses = 6;
		SandboxVars.SurvivorHouseChance = 1;
		SandboxVars.VehicleStoryChance = 1;
		SandboxVars.FoodRotSpeed = 3;
		SandboxVars.FridgeFactor = 3;
		SandboxVars.Farming = 3;
		SandboxVars.LootRespawn = 1;
		SandboxVars.StarterKit = false;
		SandboxVars.Nutrition = true;
		SandboxVars.TimeSinceApo = 1;
		SandboxVars.PlantResilience = 3;
		SandboxVars.PlantAbundance = 3;
		SandboxVars.EndRegen = 3;
		SandboxVars.CarSpawnRate = 3;
		SandboxVars.LockedCar = 3;
		SandboxVars.CarAlarm = 2;
		SandboxVars.ChanceHasGas = 1;
		SandboxVars.InitialGas = 2;
		SandboxVars.CarGeneralCondition = 1;
		SandboxVars.RecentlySurvivorVehicles = 1;
		
		SandboxVars.ZombieLore = {
			Speed = 2,
			Strength = 2,
			Toughness = 2,
			Transmission = 1,
			Mortality = 5,
			Reanimate = 3,
			Cognition = 3,
			Memory = 2,
			Decomp = 1,
			Sight = 3,
			Hearing = 3,
			Smell = 2,
			ThumpNoChasing = 0,
		}
	end,
	onStart = function()
		local chr = getPlayer();
		local inv = chr:getInventory();
		local visual = chr:getHumanVisual();
		chr:setFemale(false);
		chr:getDescriptor():setFemale(false);
		
		-- reset
		chr:clearWornItems();
		chr:getInventory():clear();
		
		-- chr visual
		visual:setBeardModel("Full");
		visual:setHairModel("Messy");
		local immutableColor = ImmutableColor.new(0.105, 0.09, 0.086, 1)
		visual:setHairColor(immutableColor)
		visual:setBeardColor(immutableColor)
		visual:setSkinTextureIndex(2);
		chr:resetModel();
		
		-- wpn
		wpn = inv:AddItem("Base.Crowbar");
		chr:setPrimaryHandItem(wpn);
		chr:setSecondaryHandItem(wpn);
		
		chr:setGhostMode(true);
		chr:setInvincible(true);
		chr:LevelPerk(Perks.Sneak);chr:LevelPerk(Perks.Sneak);chr:LevelPerk(Perks.Sneak);chr:LevelPerk(Perks.Sneak);chr:LevelPerk(Perks.Sneak);chr:LevelPerk(Perks.Sneak);
		chr:LevelPerk(Perks.Lightfoot);chr:LevelPerk(Perks.Lightfoot);chr:LevelPerk(Perks.Lightfoot);chr:LevelPerk(Perks.Lightfoot);chr:LevelPerk(Perks.Lightfoot);chr:LevelPerk(Perks.Lightfoot);
		chr:LevelPerk(Perks.Blunt);chr:LevelPerk(Perks.Blunt);chr:LevelPerk(Perks.Blunt);chr:LevelPerk(Perks.Blunt);chr:LevelPerk(Perks.Blunt);chr:LevelPerk(Perks.Blunt);
		
		-- surround zeds
		--		addZombiesInOutfit(11901, 6945, 0, 3, nil, nil);
		--		addZombiesInOutfit(11851, 6901, 0, 3, nil, nil);
		--		addZombiesInOutfit(11874, 6898, 0, 2, nil, nil);
		--		addZombiesInOutfit(11882, 6902, 0, 2, nil, nil);
		--		addZombiesInOutfit(11887, 6894, 0, 2, nil, nil);
		--		addZombiesInOutfit(11898, 6902, 0, 3, nil, nil);
		-- eating zeds
		addZombiesEating(11876, 6894, 0, 3, false);
		addBloodSplat(getCell():getGridSquare(11876, 6894, 0), 20);
		-- sitting zeds
		addZombieSitting(11871, 6893, 0);
		addZombieSitting(11893, 6892, 0);
		
		-- clothing
		clothe = inv:AddItem("Base.Tshirt_DefaultTEXTURE_TINT");
		local color = ImmutableColor.new(0.25, 0.36, 0.36, 1)
		clothe:getVisual():setTint(color);
		chr:setWornItem(clothe:getBodyLocation(), clothe);
		clothe = inv:AddItem("Base.HoodieUP_WhiteTINT");
		local color = ImmutableColor.new(0.70, 0.80, 0.47, 1)
		clothe:getVisual():setTint(color);
		chr:setWornItem(clothe:getBodyLocation(), clothe);
		clothe = inv:AddItem("Base.Bag_DuffelBag");
		clothe:getVisual():setTextureChoice(1);
		chr:setClothingItem_Back(clothe);
		clothe = inv:AddItem("Base.Trousers_Denim");
		clothe:getVisual():setTextureChoice(2);
		chr:setWornItem(clothe:getBodyLocation(), clothe);
		clothe = inv:AddItem("Base.Socks_Ankle");
		chr:setWornItem(clothe:getBodyLocation(), clothe);
		clothe = inv:AddItem("Base.Shoes_BlueTrainers");
		chr:setWornItem(clothe:getBodyLocation(), clothe);
		
		-- some blood on player
		chr:addBlood(BloodBodyPartType.Torso_Upper, true, true, false);
		chr:addBlood(BloodBodyPartType.Torso_Upper, true, true, false);
		chr:addBlood(BloodBodyPartType.Torso_Upper, true, true, false);
		chr:addBlood(BloodBodyPartType.UpperLeg_L, true, true, false);
		chr:addBlood(BloodBodyPartType.UpperLeg_L, true, true, false);
		chr:addBlood(BloodBodyPartType.LowerLeg_R, true, true, false);
		chr:addBlood(BloodBodyPartType.LowerLeg_R, true, true, false);
		chr:addBlood(BloodBodyPartType.UpperLeg_R, true, true, false);
		chr:addBlood(BloodBodyPartType.UpperLeg_R, true, true, false);
		chr:addBlood(BloodBodyPartType.Groin, true, true, false);
		chr:addBlood(BloodBodyPartType.ForeArm_L, true, true, false);
		chr:addBlood(BloodBodyPartType.UpperArm_R, true, true, false);
		-- holes
		chr:addHole(BloodBodyPartType.Torso_Lower);
		chr:addHole(BloodBodyPartType.LowerLeg_R);
		chr:addHole(BloodBodyPartType.ForeArm_R);
		-- limp
		chr:getBodyDamage():getBodyPart(BodyPartType.LowerLeg_R):setScratched(true, true);
		chr:getBodyDamage():getBodyPart(BodyPartType.LowerLeg_R):setCut(true);
		
		-- use a static rng map for erosion so we always have same bushes/etc.
		useStaticErosionRand(true);
	
		-- climate
		local clim = getClimateManager();
		local w = clim:getWeatherPeriod();
		if w:isRunning() then
			clim:stopWeatherAndThunder();
		end
		
		-- remove fog
		local var = clim:getClimateFloat(5);
		var:setEnableOverride(true);
		var:setOverride(0, 1);
	end,
	
	onLoadGS = function(sq)
	
	end
}