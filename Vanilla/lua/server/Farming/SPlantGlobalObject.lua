--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

-----------------------------------------------------------------------
--                          FARMING MOD                              --
--                      CODE BY ROBERT JOHNSON                       --
--                       TEXTURES BY THUZTOR                         --
-----------------------------------------------------------------------
--                          OFFICIAL TOPIC                           --
--  http://www.theindiestone.com/community/viewtopic.php?f=33&t=8675 --
--                                                                   --
-----------------------------------------------------------------------

if isClient() then return end

require "Map/SGlobalObject"

SPlantGlobalObject = SGlobalObject:derive("SPlantGlobalObject")

function SPlantGlobalObject:new(luaSystem, globalObject)
	local o = SGlobalObject.new(self, luaSystem, globalObject)
	return o;
end

function SPlantGlobalObject.initModData(modData)
	modData.state = "plow"
	modData.nbOfGrow = -1
	modData.typeOfSeed = "none"
	modData.fertilizer = 0
	modData.mildewLvl = 0
	modData.aphidLvl = 0
	modData.fliesLvl = 0
	modData.waterLvl = 0
	modData.waterNeeded = 0
	modData.waterNeededMax = nil
	modData.lastWaterHour = 0
	modData.hasSeed = false
	modData.hasVegetable = false
	modData.health = SFarmingSystem.instance:getHealth()
	modData.badCare = false
	modData.exterior = true
	modData.spriteName = "vegetation_farming_01_1"
	modData.objectName = getText("Farming_Plowed_Land");
end

function SPlantGlobalObject:initNew()
	SPlantGlobalObject.initModData(self)
end

function SPlantGlobalObject:stateFromIsoObject(isoObject)
	self:initNew()
	self:fromModData(isoObject:getModData())
	self.objectName = isoObject:getName()
	self.spriteName = isoObject:getSpriteName()

	-- MapObjects-related code (see MOFarming.lua) might have changed the
	-- isoObject when it was loaded, we must sync with clients.
	if isServer() then
		isoObject:sendObjectChange('name')
		isoObject:sendObjectChange('sprite')
		isoObject:transmitModData()
	end
end

function SPlantGlobalObject:stateToIsoObject(isoObject)
	self.exterior = self:getSquare():isOutside()
	if tonumber(self.aphidLvl) then self.aphidLvl = math.min(self.aphidLvl, 100) end
	if tonumber(self.fliesLvl) then self.fliesLvl = math.min(self.fliesLvl, 100) end
	if tonumber(self.mildewLvl) then self.mildewLvl = math.min(self.mildewLvl, 100) end

	if self.lastWaterHour and self.lastWaterHour > SFarmingSystem.instance.hoursElapsed then
		if farming_vegetableconf.props[self.typeOfSeed] then
			self.nextGrowing = SFarmingSystem.instance.hoursElapsed + farming_vegetableconf.props[self.typeOfSeed].timeToGrow
		end
		self.lastWaterHour = SFarmingSystem.instance.hoursElapsed
		self:noise('reset lastWaterHour/nextGrowing on plant '..luaObject.x..','..luaObject.y)
	end

	isoObject:setName(self.objectName)
	isoObject:setSprite(self.spriteName)
	self:toModData(isoObject:getModData())

	if isServer() then
		isoObject:sendObjectChange('name')
		isoObject:sendObjectChange('sprite')
		isoObject:transmitModData()
	end
end

function SPlantGlobalObject:getObject()
	return self:getIsoObject()
end

function SPlantGlobalObject:setObjectName(objectName)
	if objectName == self.objectName then return end
	self.objectName = objectName
	local object = self:getObject()
	if object then
		object:setName(self.objectName)
		if isServer() then
			object:sendObjectChange('name')
		end
		-- objectName is stored in modData
		self:toModData(object:getModData())
		-- also update GameTime modData
	end
end

function SPlantGlobalObject:setSpriteName(spriteName)
	if spriteName == self.spriteName then return end
	self.spriteName = spriteName
	local object = self:getObject()
	if object then
		object:setSprite(self.spriteName)
		if isServer() then
			object:sendObjectChange('sprite')
		end
		-- spriteName is stored in modData
		self:toModData(object:getModData())
		-- also update GameTime modData
	end
end

function SPlantGlobalObject:isAlive()
	return self.state ~= "destroy" and self.state ~= "dry" and self.state ~= "rotten"
end

function SPlantGlobalObject:canHarvest()
	return self:isAlive() and self.hasVegetable
end

function SPlantGlobalObject:addObject()
	if self:getObject() then return end
	local square = self:getSquare()
	if not square then return end
	local object = IsoObject.new(square, self.spriteName, self.objectName)
	object:setSpecialTooltip(true);
	self:toModData(object:getModData())
	square:AddTileObject(object)
	object:transmitCompleteItemToClients()
end

function SPlantGlobalObject:removeObject()
	self:removeIsoObject()
end

-- if the plant doesn't have mildew, maybe it will have it !
-- base risk for mildew is 2%, but every pt of water below the required water lvl add 1% risk
function SPlantGlobalObject:mildew()
	local mildewNumber = 2
	local waterBelow = self.waterNeeded - self.waterLvl
	-- our plant is well watered
	if(waterBelow <= 0) then
		waterBelow = 0
	end
	mildewNumber = mildewNumber + waterBelow
	if SandboxVars.PlantResilience == 1 then -- very high
		mildewNumber = mildewNumber - 8;
	elseif SandboxVars.PlantResilience == 2 then -- high
		mildewNumber = mildewNumber - 4;
	elseif SandboxVars.PlantResilience == 4 then -- low
		mildewNumber = mildewNumber + 4;
	elseif SandboxVars.PlantResilience == 5 then -- very low
		mildewNumber = mildewNumber + 8;
	end
	if mildewNumber < 0 then
		mildewNumber = 0;
	end
	-- random !
	local mildewRisk = ZombRand(mildewNumber, 101)
	-- you got mildew !
	if(mildewRisk == 99) then
		self.mildewLvl = 1
	end
end

-- if the plant doesn't have aphid, maybe it will have it !
-- base risk for aphid is 2%, but every pt of water below the required water lvl add 1% risk
function SPlantGlobalObject:aphid()
	local aphidNumber = 2
	local waterBelow = self.waterNeeded - self.waterLvl
	-- our plant is well watered
	if(waterBelow <= 0) then
		waterBelow = 0
	end
	aphidNumber = aphidNumber + waterBelow
	if SandboxVars.PlantResilience == 1 then -- very high
		aphidNumber = aphidNumber - 8;
	elseif SandboxVars.PlantResilience == 2 then -- high
		aphidNumber = aphidNumber - 4;
	elseif SandboxVars.PlantResilience == 4 then -- low
		aphidNumber = aphidNumber + 4;
	elseif SandboxVars.PlantResilience == 5 then -- very low
		aphidNumber = aphidNumber + 8;
	end
	if aphidNumber < 0 then
		aphidNumber = 0;
	end
	-- random !
	-- I have to set 101 to the max, don't know why but the zombRand never take the last digit (ex, between 5 and 10, you'll never have 10...)
	local aphidRisk = ZombRand(aphidNumber, 101)
	-- you got aphid !
	if(aphidRisk == 99) then
		self.aphidLvl = 1
	end
end

-- if the plant doesn't have flies, maybe it will have it !
-- base risk for flies is 2%, but every pt of water below the required water lvl add 1% risk
function SPlantGlobalObject:flies()
	local fliesNumber = 2
	local waterBelow = self.waterNeeded - self.waterLvl
	-- our plant is well watered
	if(waterBelow <= 0) then
		waterBelow = 0
	end
	fliesNumber = fliesNumber + waterBelow
	if SandboxVars.PlantResilience == 1 then -- very high
		fliesNumber = fliesNumber - 8;
	elseif SandboxVars.PlantResilience == 2 then -- high
		fliesNumber = fliesNumber - 4;
	elseif SandboxVars.PlantResilience == 4 then -- low
		fliesNumber = fliesNumber + 4;
	elseif SandboxVars.PlantResilience == 5 then -- very low
		fliesNumber = fliesNumber + 8;
	end
	if fliesNumber < 0 then
		fliesNumber = 0;
	end
	-- random !
	-- I have to set 101 to the max, don't know why but the zombRand never take the last digit (ex, between 5 and 10, you'll never have 10...)
	local fliesRisk = ZombRand(fliesNumber, 101)
	-- you got flies !
	if(fliesRisk == 99) then
		self.fliesLvl = 1
	end
end

-- up the disease by a number, it's the double if your plant is not well watered
function SPlantGlobalObject:upDisease()
	-- mildew lvl up by 1 or 0.5 if your plant is well watered
	if self.mildewLvl ~= nil and self.mildewLvl > 0 then
		local water = farming_vegetableconf.calcWater(self.waterNeeded, self.waterLvl)
		if(water >= 0) then
			self.mildewLvl = self.mildewLvl + 0.5
		else
			self.mildewLvl = self.mildewLvl + 1
		end
		self.mildewLvl = math.min(self.mildewLvl, 100)
	end
	-- flies lvl up by 1 or 0.5 if your plant is well watered
	if self.fliesLvl ~= nil and self.fliesLvl > 0 then
		local water = farming_vegetableconf.calcWater(self.waterNeeded, self.waterLvl)
		if(water >= 0) then
			self.fliesLvl = self.fliesLvl + 0.5
		else
			self.fliesLvl = self.fliesLvl + 1
		end
		self.fliesLvl = math.min(self.fliesLvl, 100)
	end
	-- aphid is different than mildew, you'll have to deshydrate your plant to lower the aphid lvl
	if self.aphidLvl ~= nil and self.aphidLvl > 0 then
		local water = farming_vegetableconf.calcWater(self.waterNeeded, self.waterLvl)
		if water == -1 or water == -2 then
			self.aphidLvl = self.aphidLvl - 2
			if self.aphidLvl < 0 then
				self.aphidLvl = 0
			end
		else
			self.aphidLvl = self.aphidLvl + 1
			self.aphidLvl = math.min(self.aphidLvl, 100)
		end
	end
end

-- lower by 1 the waterLvl of all our plant
function SPlantGlobalObject:lowerWaterLvl(plant)
	if self.waterLvl ~= nil then
		-- flies make water dry more quickly, every 10 pts of flies, water lower by 1 more pts
		local waterFliesChanger = math.floor(self.fliesLvl / 10)
		self.waterLvl = self.waterLvl - 1 - waterFliesChanger
		if self.waterLvl < 0 then
			self.waterLvl = 0
		end
	end
end

-- if we add mildew spray
function SPlantGlobalObject:cureMildew(mildewCureSource, uses)
	for i=1,uses do
		if mildewCureSource then
			mildewCureSource:Use()
		end
		self.mildewLvl = self.mildewLvl - 5
		if self.mildewLvl < 0 then
			self.mildewLvl = 0
		end
	end
	self:saveData()
end

-- if we add insecticide spray
function SPlantGlobalObject:cureFlies(fliesCureSource, uses)
	for i=1,uses do
		if fliesCureSource then
			fliesCureSource:Use()
		end
		self.fliesLvl = self.fliesLvl - 5
		if self.fliesLvl < 0 then
			self.fliesLvl = 0
		end
	end
	self:saveData()
end

-- if we water our plant
-- we gonna add 5 water lvl for the plant for each use of the water source
function SPlantGlobalObject:water(waterSource, uses)
	for i=1,uses do
		if self.waterLvl < 100 then
			if waterSource then
				if waterSource:getUsedDelta() > 0 then
					waterSource:Use()
				end
			end
			self.waterLvl = self.waterLvl + 5
			if self.waterLvl > 100 then
				self.waterLvl = 100
			end
		end
	end
	-- we notice the hour of our last water, because if we don't water the plant every 48 hours, she die
	self.lastWaterHour = SFarmingSystem.instance.hoursElapsed;
	self:saveData()
end

-- fertilize the plant, more than 4 doses and your plant die ! (no mercy !)
function SPlantGlobalObject:fertilize(fertilizer)
	if self.state ~= "plow" and self:isAlive() then
		if self.fertilizer < 4  then
			self.fertilizer = self.fertilizer + 1
			self.nextGrowing = self.nextGrowing - 20
			if self.nextGrowing < 1 then
				self.nextGrowing = 1
			end
		else -- too much fertilizer and our plant die !
			self:rottenThis()
		end
		self:saveData()
	end
end

-- check the stat to make them ok (if water < 0 we set it to 0 for example)
-- if health < 0 the plant is dead (dry)
function SPlantGlobalObject:checkStat()
	if self.state ~= "plow" and self.nbOfGrow > 1 then
		if self.waterLvl <= 0 then
			self:dryThis()
		elseif self.waterLvl > 100 then
			self.waterLvl = 100
		end
		if self.health <= 0 then
			self:dryThis()
		end
	end
	if self.waterLvl < 0 then
		self.waterLvl = 0
	end
	if self.health < 0 then
		self.health = 0
	elseif self.health > 100 then
		self.health = 100
	end
end

-- make the plant dry (no more water !)
function SPlantGlobalObject:dryThis()
	if self.typeOfSeed == "Tomato" then
		self:setSpriteName("vegetation_farming_01_6")
	else
		self:setSpriteName("vegetation_farming_01_5")
	end
	self.state = "dry"
	self:setObjectName(farming_vegetableconf.getObjectName(self))
	self:deadPlant()
end

function SPlantGlobalObject:rottenThis()
	local texture = nil
	if self.typeOfSeed == "Carrots" then
		texture = "vegetation_farming_01_13"
	elseif self.typeOfSeed == "Broccoli" then
		texture = "vegetation_farming_01_23"
	elseif self.typeOfSeed == "Strawberry plant" then
		texture = "vegetation_farming_01_63"
	elseif self.typeOfSeed == "Radishes" then
		texture = "vegetation_farming_01_39"
	elseif self.typeOfSeed == "Tomato" then
		texture = "vegetation_farming_01_71"
	elseif self.typeOfSeed == "Potatoes" then
		texture = "vegetation_farming_01_47"
	elseif self.typeOfSeed == "Cabbages" then
		texture = "vegetation_farming_01_31"
	end
	self:setSpriteName(texture)
	self.state = "rotten"
	self:setObjectName(farming_vegetableconf.getObjectName(self))
	self:deadPlant()
end

-- destroy the plant (someone walked on it :))
function SPlantGlobalObject:destroyThis()
	-- tomato has different smashed tile
	if self.typeOfSeed == "Tomato" then
		self:setSpriteName("vegetation_farming_01_14")
	else
		self:setSpriteName("vegetation_farming_01_13")
	end
	self.state = "destroy"
	self:setObjectName(farming_vegetableconf.getObjectName(self))
	self:deadPlant()
	self:saveData()
end

function SPlantGlobalObject:seed(typeOfSeed)
	-- will set the first growing state for the type of seed
	self.nbOfGrow = 0
	self.state = "seeded"
	self.typeOfSeed = typeOfSeed
	-- You have 48 hours to water your plant, if not, she will be dry
	self.lastWaterHour = SFarmingSystem.instance.hoursElapsed
	self.waterNeeded = 0
	SFarmingSystem.instance:growPlant(self, nil, true)
	self:saveData()
end

function SPlantGlobalObject:deadPlant()
	self.nextGrowing = 0
	self.waterLvl = 0
	self.nbOfGrow = 0
	self.mildewLvl = 0
	self.aphidLvl = 0
	self.fliesLvl = 0
	self.health = 0
end

function SPlantGlobalObject:addIcon()
end

function SPlantGlobalObject:saveData()
	local isoObject = self:getIsoObject()
	if isoObject then
		self:toModData(isoObject:getModData())
		isoObject:transmitModData()
	end
end

function SPlantGlobalObject:fromModData(modData)
	self.state = modData.state
	self.nbOfGrow = modData.nbOfGrow
	self.typeOfSeed = modData.typeOfSeed
	self.fertilizer = modData.fertilizer
	self.mildewLvl = modData.mildewLvl
	self.aphidLvl = modData.aphidLvl
	self.fliesLvl = modData.fliesLvl
	self.waterLvl = modData.waterLvl
	self.waterNeeded = modData.waterNeeded
	self.waterNeededMax = modData.waterNeededMax
	self.lastWaterHour = modData.lastWaterHour
	self.nextGrowing = modData.nextGrowing
	self.hasSeed = modData.hasSeed == "true" or modData.hasSeed == true
	self.hasVegetable = modData.hasVegetable == "true" or modData.hasVegetable == true
	self.health = modData.health
	self.badCare = modData.badCare == "true" or modData.badCare == true
	self.exterior = modData.exterior == true or modData.exterior == nil
	self.spriteName = modData.spriteName
	self.objectName = modData.objectName
	if not self.spriteName then -- old-style modData
		self.spriteName = farming_vegetableconf.getSpriteName(self)
	end
	if not self.objectName then -- old-style modData
		self.objectName = farming_vegetableconf.getObjectName(self)
	end
end

function SPlantGlobalObject:toModData(modData)
	modData.state = self.state
	modData.nbOfGrow = self.nbOfGrow
	modData.typeOfSeed = self.typeOfSeed
	modData.fertilizer = self.fertilizer
	modData.mildewLvl = self.mildewLvl
	modData.aphidLvl = self.aphidLvl
	modData.fliesLvl = self.fliesLvl
	modData.waterLvl = self.waterLvl
	modData.waterNeeded = self.waterNeeded
	modData.waterNeededMax = self.waterNeededMax
	modData.lastWaterHour = self.lastWaterHour
	modData.nextGrowing = self.nextGrowing
	modData.hasSeed = self.hasSeed
	modData.hasVegetable = self.hasVegetable
	modData.health = self.health
	modData.badCare = self.badCare
	modData.exterior = self.exterior
	modData.spriteName = self.spriteName
	modData.objectName = self.objectName
end

function SPlantGlobalObject:fromObject(object)
	self:initFromIsoObject(object)
end

function SPlantGlobalObject:loadGridSquare()
	local isoObject = self:getObject()
	if not isoObject then return end
	self:loadIsoObject(isoObject)
end

