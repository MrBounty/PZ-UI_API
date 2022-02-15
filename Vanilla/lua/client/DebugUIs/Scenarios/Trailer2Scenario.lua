if debugScenarios == nil then
	debugScenarios = {}
end


debugScenarios.Trailer2Scenario = {
	name = "LIFE 2 - START",
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
	
--		local immutableColor = ImmutableColor.new(color.r, color.g, color.b, 1)
--		desc:getHumanVisual():setHairColor(immutableColor)
--		desc:getHumanVisual():setBeardColor(immutableColor)
		
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
		clothe:getVisual():setTextureChoice(0);
		chr:setWornItem(clothe:getBodyLocation(), clothe);
		clothe = inv:AddItem("Base.Socks_Ankle");
		chr:setWornItem(clothe:getBodyLocation(), clothe);
		clothe = inv:AddItem("Base.Shoes_BlueTrainers");
		chr:setWornItem(clothe:getBodyLocation(), clothe);
		
		
		-- windows
		local window1 = getCell():getGridSquare(7094, 8378, 0):getWindow();
--		local window2 = getCell():getGridSquare(7094, 8375, 0):getWindow();
--		local window3 = getCell():getGridSquare(7096, 8372, 0):getWindow();
--		local window4 = getCell():getGridSquare(7096, 8370, 0):getWindow();
		inv:AddItem("Base.Sheet");inv:AddItem("Base.Sheet");
		window1:addSheet(chr);
--		window3:addSheet(chr);
		window1:HasCurtains():ToggleDoorSilent();
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
		createRandomDeadBody(sq, 5);
		addBloodSplat(sq, 10);
		local sq = getCell():getGridSquare(7105, 8387, 0);
		createRandomDeadBody(sq, 5);
		addBloodSplat(sq, 12);
	
		-- car
		local car = addVehicleDebug("Base.CarNormal", IsoDirections.E, nil, getCell():getGridSquare(7094, 8388, 0));
		car:setColor(0.8, 0.85, 0.02);
		inv:AddItem(car:createVehicleKey());
	
		-- few survivor items in the house
		-- crates
		local sq = getCell():getGridSquare(7089, 8372, 0);
		sq:AddTileObject(IsoObject.new(sq, "carpentry_01_19"));
		local sq = getCell():getGridSquare(7089, 8372, 0);
		sq:AddTileObject(IsoObject.new(sq, "carpentry_01_20"));
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
	end,

	onLoadGS = function(sq)
		-- barricade window 1
		if sq:getWindow() and sq:getX() == 7094 and sq:getY() == 8375 and sq:getZ() == 0 then
			local window = sq:getWindow();
			window:addBarricadesDebug(3, false);
		end
		-- barricade window 2
		if sq:getWindow() and sq:getX() == 7096 and sq:getY() == 8370 and sq:getZ() == 0 then
			local window = sq:getWindow();
			window:addBarricadesDebug(2, false);
		end
		-- barricade window 3
		if sq:getWindow() and sq:getX() == 7096 and sq:getY() == 8372 and sq:getZ() == 0 then
			local window = sq:getWindow();
			window:addBarricadesDebug(4, false);
		end
	end
}