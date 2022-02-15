if debugScenarios == nil then
	debugScenarios = {}
end


debugScenarios.Trailer3Scenario = {
	name = "LIFE 3 - START AND ESCAPE",
--	forceLaunch = true, -- use this to force the launch of THIS scenario right after main menu was loaded, save more clicks! Don't do multiple scenarii with this options
	startLoc = {x=7090, y=8371, z=0 }, -- house 2
	setSandbox = function()
		
		SandboxVars.Speed = 3;
		SandboxVars.Zombies = 3;
		SandboxVars.Distribution = 1;
		SandboxVars.Survivors = 1;
		SandboxVars.DayLength = 3;
		SandboxVars.StartYear = 1;
		SandboxVars.StartMonth = 7;
		SandboxVars.StartDay = 9;
		SandboxVars.StartTime = 6;
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
	
--		local immutableColor = ImmutableColor.new(color.r, color.g, color.b, 1)
--		desc:getHumanVisual():setHairColor(immutableColor)
--		desc:getHumanVisual():setBeardColor(immutableColor)
		
		-- reset
		chr:clearWornItems();
		chr:getInventory():clear();
		
		-- chr visual
		visual:setBeardModel("Long");
		visual:setHairModel("Messy");
		local immutableColor = ImmutableColor.new(0.105, 0.09, 0.086, 1)
		visual:setHairColor(immutableColor)
		visual:setBeardColor(immutableColor)
		visual:setSkinTextureIndex(2);
		chr:resetModel();
		
		chr:setGhostMode(true);
	
		-- wpn
		wpn = inv:AddItem("Base.Axe");
		chr:setPrimaryHandItem(wpn);
		chr:setSecondaryHandItem(wpn);
		holster = inv:AddItem("Base.HolsterDouble");
		chr:setWornItem(holster:getBodyLocation(), holster);
		wpn = inv:AddItem("Base.Revolver_Long");
		
		chr:setAttachedItem("Holster Left", wpn);
		wpn:setAttachedSlot(2);
		wpn:setAttachedSlotType("HolsterLeft");
		wpn:setAttachedToModel("Holster Left");
	
		-- clothing
		clothe = inv:AddItem("Base.Tshirt_DefaultTEXTURE_TINT");
		local color = ImmutableColor.new(0.25, 0.36, 0.36, 1)
		clothe:getVisual():setTint(color);
		chr:setWornItem(clothe:getBodyLocation(), clothe);
		clothe = inv:AddItem("Base.Jacket_Black");
		chr:setWornItem(clothe:getBodyLocation(), clothe);
		clothe = inv:AddItem("Base.Bag_ALICEpack_Army");
		chr:setClothingItem_Back(clothe);
		clothe = inv:AddItem("Base.Trousers_CamoGreen");
		chr:setWornItem(clothe:getBodyLocation(), clothe);
		clothe = inv:AddItem("Base.Socks_Ankle");
		chr:setWornItem(clothe:getBodyLocation(), clothe);
		clothe = inv:AddItem("Base.Shoes_BlackBoots");
		chr:setWornItem(clothe:getBodyLocation(), clothe);
		clothe = inv:AddItem("Base.Gloves_LeatherGlovesBlack");
		chr:setWornItem(clothe:getBodyLocation(), clothe);
--		clothe = inv:AddItem("Base.Hat_BonnieHat_CamoGreen");
--		chr:setWornItem(clothe:getBodyLocation(), clothe);
		
		
		-- windows
--		local window1 = getCell():getGridSquare(7094, 8378, 0):getWindow();
--		local window2 = getCell():getGridSquare(7094, 8375, 0):getWindow();
--		local window3 = getCell():getGridSquare(7096, 8372, 0):getWindow();
--		local window4 = getCell():getGridSquare(7096, 8370, 0):getWindow();
--		inv:AddItem("Base.Sheet");inv:AddItem("Base.Sheet");
--		window1:addSheet(chr);
--		window3:addSheet(chr);
--		window1:HasCurtains():ToggleDoorSilent();
--		window3:HasCurtains():ToggleDoorSilent();
	
		-- blood
		local sq = getCell():getGridSquare(7094, 8376, 0);
		sq:AddTileObject(IsoObject.new(sq, "overlay_blood_wall_01_3"));
		local sq = getCell():getGridSquare(7096, 8369, 0);
		sq:AddTileObject(IsoObject.new(sq, "overlay_blood_wall_01_19"));
		local sq = getCell():getGridSquare(7095, 8374, 0);
		sq:AddTileObject(IsoObject.new(sq, "overlay_blood_wall_01_7"));
	
		-- corpses
		local sq = getCell():getGridSquare(7100, 8379, 0);
		createRandomDeadBody(sq, 9);
		addBloodSplat(sq, 17);
		local sq = getCell():getGridSquare(7105, 8387, 0);
		createRandomDeadBody(sq, 8);
		addBloodSplat(sq, 13);
		local sq = getCell():getGridSquare(7096, 8376, 0);
		createRandomDeadBody(sq, 7);
		addBloodSplat(sq, 12);
		local sq = getCell():getGridSquare(7099, 8384, 0);
		createRandomDeadBody(sq, 5);
		addBloodSplat(sq, 15);
		local sq = getCell():getGridSquare(7108, 8381, 0);
		createRandomDeadBody(sq, 6);
		addBloodSplat(sq, 15);
		
		local sq = getCell():getGridSquare(7096, 8378, 0);
		addBloodSplat(sq, 15);
		local sq = getCell():getGridSquare(7099, 8374, 0);
		addBloodSplat(sq, 15);
	
		-- car
		local car = addVehicleDebug("Base.SUV", IsoDirections.E, nil, getCell():getGridSquare(7094, 8388, 0));
		car:repair();
		car:getPartById("EngineDoor"):setCondition(10);
		car:getPartById("DoorFrontLeft"):setCondition(10);
		car:getPartById("DoorRearRight"):setCondition(10);
		car:setColor(0.8, 0, 0.02);
--		colorHue = Rand.Next(0.0f, 1.0f); // Don't really care about the hue for this one
--		colorSaturation = Rand.Next(0.0f, 0.1f);
--		colorValue = Rand.Next(0.70f, 0.8f);
		inv:AddItem(car:createVehicleKey());
	
		-- few survivor items in the house
		-- crates
		local sq = getCell():getGridSquare(7089, 8372, 0);
		sq:AddTileObject(IsoObject.new(sq, "carpentry_01_19"));
		local sq = getCell():getGridSquare(7089, 8372, 0);
		sq:AddTileObject(IsoObject.new(sq, "carpentry_01_20"));
		local sq = getCell():getGridSquare(7093, 8378, 0);
		sq:AddTileObject(IsoObject.new(sq, "carpentry_01_19"));
		-- shelf
		local sq = getCell():getGridSquare(7089, 8373, 0);
		local shelf = IsoThumpable.new(getCell(), sq, "carpentry_02_68", false, ISSimpleFurniture:new("Shelves", "carpentry_02_68", "carpentry_02_68"));
		shelf:setIsContainer(true);
		shelf:getContainer():setType("shelves");
		shelf:getContainer():AddItems("Base.Crowbar", 10);
		sq:AddTileObject(shelf);
		-- rain barrel outside
		local sq = getCell():getGridSquare(7092, 8382, 0);
		sq:AddTileObject(IsoObject.new(sq, "carpentry_02_53"));
		-- generator
--		local sq = getCell():getGridSquare(7095, 8374, 0);
--		sq:AddTileObject(IsoObject.new(sq, "appliances_misc_01_0"));
		-- water
		local sq = getCell():getGridSquare(7093, 8376, 0);
		sq:AddTileObject(IsoObject.new(sq, "location_business_office_generic_01_57"));
		-- american flag
		local sq = getCell():getGridSquare(7089, 8374, 0);
		sq:AddTileObject(IsoObject.new(sq, "walls_decoration_01_17"));
		-- table
		local sq = getCell():getGridSquare(7091, 8378, 0);
		sq:AddTileObject(IsoObject.new(sq, "carpentry_01_60"));
		
		-- walled outside
		-- logs
--		for i=7094,7100 do
--			local sq = getCell():getGridSquare(i, 8382, 0);
--			sq:AddTileObject(IsoObject.new(sq, "carpentry_02_81"));
--		end
		for i=7096,7097 do
			local sq = getCell():getGridSquare(i, 8369, 0);
			sq:AddTileObject(IsoObject.new(sq, "carpentry_02_81"));
		end
		for i=7098,7099 do
			local sq = getCell():getGridSquare(i, 8369, 0);
			sq:AddTileObject(IsoObject.new(sq, "carpentry_02_101"));
		end
		for i=7100,7100 do
			local sq = getCell():getGridSquare(i, 8369, 0);
			sq:AddTileObject(IsoObject.new(sq, "walls_exterior_wooden_01_45"));
		end
		-- logs/plank on the ground
		local sq = getCell():getGridSquare(7098, 8369, 0);
		sq:AddWorldInventoryItem("Base.Plank", ZombRand(0.1, 0.5), ZombRand(0.1, 0.5), 0);
		sq:AddWorldInventoryItem("Base.Log", ZombRand(0.1, 0.5), ZombRand(0.1, 0.5), 0);
		sq:AddWorldInventoryItem("Base.Log", ZombRand(0.1, 0.5), ZombRand(0.1, 0.5), 0);
		local sq = getCell():getGridSquare(7099, 8370, 0);
		sq:AddWorldInventoryItem("Base.Log", ZombRand(0.1, 0.5), ZombRand(0.1, 0.5), 0);
		local sq = getCell():getGridSquare(7097, 8370, 0);
		sq:AddWorldInventoryItem("Base.Hammer", ZombRand(0.1, 0.5), ZombRand(0.1, 0.5), 0);
		-- frames on right
--		for i=8369,8371 do
--			local sq = getCell():getGridSquare(7101, i, 0);
--			sq:AddTileObject(IsoObject.new(sq, "carpentry_02_100"));
--		end
		-- planks
--		for i=8372,8381 do
--			local sq = getCell():getGridSquare(7101, i, 0);
--			if i == 8373 then -- better wall
--				sq:AddTileObject(IsoObject.new(sq, "walls_exterior_wooden_01_40"));
--			elseif i == 8379 then -- frame
--				sq:AddTileObject(IsoObject.new(sq, "walls_exterior_wooden_01_54"));
--			elseif i == 8377 or i == 8376 then -- 2 holes
--				plank = inv:AddItem("Base.Plank");
--				sq:AddWorldInventoryItem(plank, ZombRand(0.1, 0.5), ZombRand(0.1, 0.5), 0);
--				plank = inv:AddItem("Base.Log");
--				sq:AddWorldInventoryItem(plank, ZombRand(0.1, 0.5), ZombRand(0.1, 0.5), 0);
--				plank = inv:AddItem("Base.Log");
--				sq:AddWorldInventoryItem(plank, ZombRand(0.1, 0.5), ZombRand(0.1, 0.5), 0);
----				sq:AddTileObject(IsoObject.new(sq, "walls_exterior_wooden_01_54"));
--			else
--				sq:AddTileObject(IsoObject.new(sq, "walls_exterior_wooden_01_44"));
--			end
--		end
		
		-- some blood on player
		chr:addBlood(BloodBodyPartType.Torso_Upper, true, true, false);
		chr:addBlood(BloodBodyPartType.Torso_Upper, true, true, false);
		chr:addBlood(BloodBodyPartType.UpperLeg_L, true, true, false);
		chr:addBlood(BloodBodyPartType.Groin, true, true, false);
		chr:addBlood(BloodBodyPartType.ForeArm_L, true, true, false);
	
		-- turn off radio
		local sq = getCell():getGridSquare(7087, 8368, 0);
		local radio = nil;
		for i=0,sq:getObjects():size() - 1 do
			local item = sq:getObjects():get(i);
			if instanceof(item, "IsoRadio") then
				radio = item;
				break;
			end
		end
		radio:getDeviceData():setIsTurnedOn(false);
		
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
		
		-- start thunderstorm
		clim:triggerKateBobIntroStorm(7087, 8368, 20, 0.75, 4, 180, 0.9);
	end,

	onLoadGS = function(sq)
		-- barricade window 1
		if sq:getWindow() and sq:getX() == 7094 and sq:getY() == 8375 and sq:getZ() == 0 then
			local window = sq:getWindow();
			local barricade = window:addBarricadesDebug(1, false);
			barricade:DamageBarricade(950);
		end
		-- barricade window 2
		if sq:getWindow() and sq:getX() == 7096 and sq:getY() == 8370 and sq:getZ() == 0 then
			local window = sq:getWindow();
			window:addBarricadesDebug(4, false);
		end
		-- barricade window 3
		if sq:getWindow() and sq:getX() == 7096 and sq:getY() == 8372 and sq:getZ() == 0 then
			local window = sq:getWindow();
			window:addBarricadesDebug(4, false);
		end
		
		if sq:getWindow() and sq:getX() == 7094 and sq:getY() == 8378 and sq:getZ() == 0 then
			local window = sq:getWindow();
			window:addBarricadesDebug(3, true);
		end
	end
}