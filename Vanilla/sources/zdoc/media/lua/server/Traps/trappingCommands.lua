require "Traps/STrapSystem"

if isClient() then return end

local Commands = {}

local function noise(message) STrapSystem.instance:noise(message) end

local function getTrapAt(x, y, z)
	return STrapSystem.instance:getLuaObjectAt(x, y, z)
end

function Commands.add(player, args)
	local trap = getTrapAt(args.x, args.y, args.z)
	if trap then
		noise('client placed > 1 trap at '..args.x..','..args.y..','..args.z)
	else
		-- The client created the trap object already and sent it to the server.
		-- It would be better if the server created the trap object, not the client.
		local square = getCell():getGridSquare(args.x, args.y, args.z)
		local object = STrapSystem.findTrapObject(square)
		if object then
			STrapSystem.loadObject(object)
		end
	end
end

function Commands.remove(player, args)
	local trap = getTrapAt(args.x, args.y, args.z)
	if trap then
		local item = InventoryItemFactory.CreateItem(trap.trapType)
		player:sendObjectChange('addItem', { item = item })
		trap:removeBait(player)
		trap:removeIsoObject()
	else
		noise('no trap found at '..args.x..','..args.y..','..args.z)
	end
end

function Commands.removeAnimal(player, args)
	local trap = getTrapAt(args.x, args.y, args.z)
	if trap then
		if trap.animal.type then
			trap:removeAnimal(player)
		else
			noise('no animal in trap at '..args.x..','..args.y..','..args.z)
		end
	else
		noise('no trap found at '..args.x..','..args.y..','..args.z)
	end
end

function Commands.addBait(player, args)
	local trap = getTrapAt(args.x, args.y, args.z)
	if trap then
		trap:addBait(args.bait, args.age, args.baitAmountMulti, player)
	else
		noise('no trap found at '..args.x..','..args.y..','..args.z)
	end
end

function Commands.removeBait(player, args)
	local trap = getTrapAt(args.x, args.y, args.z)
	if trap then
		trap:removeBait(player)
	else
		noise('no trap found at '..args.x..','..args.y..','..args.z)
	end
end

function Commands.destroy(player, args)
	local trap = getTrapAt(args.x, args.y, args.z)
	if trap then
		local square = getWorld():getCell():getGridSquare(args.x, args.y, args.z);
		if square then
			trap:spawnDestroyItems(square, trap:getIsoObject());
			trap:removeIsoObject();
		else
			trap.destroyed = true;
		end
	else
		noise('no trap found at '..args.x..','..args.y..','..args.z)
	end
end

---@class STrapSystemCommands
STrapSystemCommands = Commands

