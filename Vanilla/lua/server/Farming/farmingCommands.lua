--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

require "Farming/SFarmingSystem"

local Commands = {}

local function noise(message) SFarmingSystem.instance:noise(message) end

local function getPlantAt(x, y, z)
	return SFarmingSystem.instance:getLuaObjectAt(x, y, z)
end

function Commands.cureFlies(player, args)
	local plant = getPlantAt(args.x, args.y, args.z)
	if plant and plant.fliesLvl > 0 then
		plant:cureFlies(nil, args.uses)
	elseif not plant then
		noise('no plant found at '..args.x..','..args.y..','..args.z)
	end
end

function Commands.cureMildew(player, args)
	local plant = getPlantAt(args.x, args.y, args.z)
	if plant and plant.mildewLvl > 0 then
		plant:cureMildew(nil, args.uses)
	elseif not plant then
		noise('no plant found at '..args.x..','..args.y..','..args.z)
	end
end

function Commands.fertilize(player, args)
	local plant = getPlantAt(args.x, args.y, args.z)
	if plant then
		plant:fertilize(nil)
	else
		noise('no plant found at '..args.x..','..args.y..','..args.z)
	end
end

function Commands.harvest(player, args)
	local plant = getPlantAt(args.x, args.y, args.z)
	if plant and plant:canHarvest() then
		SFarmingSystem.instance:harvest(plant, player)
	else
		noise('no plant found at '..args.x..','..args.y..','..args.z)
	end
end

function Commands.plow(player, args)
	local gridSquare = getCell():getGridSquare(args.x, args.y, args.z)
	if gridSquare then
		local plant = getPlantAt(args.x, args.y, args.z)
		if plant then
			SFarmingSystem.instance:removePlant(plant)
		end
		SFarmingSystem.instance:plow(gridSquare)
	else
		noise('no gridSquare at '..args.x..','..args.y..','..args.z)
	end
end

function Commands.removePlant(player, args)
	local plant = getPlantAt(args.x, args.y, args.z)
	if plant then
		SFarmingSystem.instance:removePlant(plant)
	else
		noise('no plant found at '..args.x..','..args.y..','..args.z)
	end
end

function Commands.seed(player, args)
	local plant = getPlantAt(args.x, args.y, args.z)
	if plant and plant.state == "plow" then
		plant:seed(args.typeOfSeed)
	elseif not plant then
		noise('no plant found at '..args.x..','..args.y..','..args.z)
	else
		noise('ignored, plant not plowed at '..args.x..','..args.y..','..args.z)
	end
end

function Commands.water(player, args)
	local plant = getPlantAt(args.x, args.y, args.z)
	if plant then
		plant:water(nil, args.uses)
	else
		noise('no plant found at '..args.x..','..args.y..','..args.z)
	end
end

function Commands.cheat(player, args)
	local plant = getPlantAt(args.x, args.y, args.z)
	if plant then
		plant[args.var] = plant[args.var] + args.count
		if plant[args.var] < 0 then plant[args.var] = 0 end
		if plant[args.var] > 100 then plant[args.var] = 100 end
		plant:saveData()
	else
		noise('no plant found at '..args.x..','..args.y..','..args.z)
	end
end

SFarmingSystemCommands = Commands

