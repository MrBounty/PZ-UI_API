--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 30/04/14
-- Time: 09:57
-- To change this template use File | Settings | File Templates.
--

if isClient() then return end

require "Map/SGlobalObjectSystem"

---@class STrapSystem : SGlobalObjectSystem
STrapSystem = SGlobalObjectSystem:derive("STrapSystem")
STrapSystem.removedCache = nil;

function STrapSystem:new()
	local o = SGlobalObjectSystem.new(self, "trap")
	return o
end

function STrapSystem:initSystem()
	SGlobalObjectSystem.initSystem(self)

	-- Specify GlobalObjectSystem fields that should be saved.
	self.system:setModDataKeys({})
	
	-- Specify GlobalObject fields that should be saved.
	self.system:setObjectModDataKeys({
		'trapType', 'trapBait', 'trapBaitDay', 'lastUpdate', 'baitAmountMulti', 'animal', 'animalHour',
		'openSprite', 'closedSprite', 'zone', 'player', 'trappingSkill', 'destroyed'})

	self:convertOldModData()
end

function STrapSystem:convertOldModData()
	-- If the gos_xxx.bin file existed, don't touch GameTime modData in case mods are using it.
	if self.system:loadedWorldVersion() ~= -1 then return end
	
	local modData = GameTime:getInstance():getModData()
	if not modData.trapping or not modData.trapping.traps then return end
	self:noise('converting old-style GameTime modData')
	for _,trap in pairs(modData.trapping.traps) do
		local globalObject = self.system:newObject(trap.x, trap.y, trap.z)
		-- FIXME: IsoObject:getModData().animal is a string (the type of animal)
		--        but STrapGlobalObject.animal is a table.
		globalObject:getModData().animal = {}
		for k,v in pairs(trap) do
			if k == "animal" then
				for _,animal in ipairs(Animals) do
					if animal.type == v then
						globalObject:getModData().animal = animal
						break
					end
				end
			else
				globalObject:getModData()[k] = v
			end
		end
	end
	modData.trapping.traps = nil
	for k,v in pairs(modData.trapping) do
		self[k] = v
	end
	modData.trapping = nil
	self:noise('converted '..self.system:getObjectCount()..' traps')
end

function STrapSystem:newLuaObject(globalObject)
	return STrapGlobalObject:new(self, globalObject)
end

function STrapSystem:isValidIsoObject(isoObject)
	return instanceof(isoObject, "IsoThumpable") and isoObject:getName() == "Trap"
end

function STrapSystem:OnClientCommand(command, playerObj, args)
	STrapSystemCommands[command](playerObj, args)
end

-- Change age of bait each day
STrapSystem.EveryDays = function()
	for i=1,STrapSystem.instance.system:getObjectCount() do
		local luaObject = STrapSystem.instance.system:getObjectByIndex(i-1):getModData()
		if luaObject.bait then
			luaObject.lastUpdate = getGameTime():getWorldAgeHours() / 24;
			luaObject.trapBaitDay = luaObject.trapBaitDay + 1;
			local isoObject = luaObject:getIsoObject()
			luaObject:toObject(isoObject, true)
		end
	end
end

-- every hour, calcul the chance of getting something
function STrapSystem.checkTrap()
	for i=1,STrapSystem.instance.system:getObjectCount() do
		local luaObject = STrapSystem.instance.system:getObjectByIndex(i-1):getModData()
		local square = getWorld():getCell():getGridSquare(luaObject.x, luaObject.y, luaObject.z)
		luaObject:calculTrap(square)
	end
end

function STrapSystem.isValidModData(modData)
	return modData.trapType ~= nil
end

function STrapSystem.addSound()
	for i=1,STrapSystem.instance.system:getObjectCount() do
		local vB = STrapSystem.instance.system:getObjectByIndex(i-1):getModData()
		local square = getWorld():getCell():getGridSquare(vB.x, vB.y, vB.z);
		vB:addSound(square);
	end
end

function STrapSystem:OnObjectAboutToBeRemoved(isoObject)
	-- This is called *before* self:OnDestroyIsoThumpable() due to
	-- ISBuildingObject.onDestroy() removing the object.
	-- SGlobalObjectSystem.OnObjectAboutToBeRemoved() will remove the STrapGlobalObject
	-- so it should not be accessed after this.
	STrapSystem.removedCache = nil;
	if self:isValidIsoObject(isoObject) then
		local luaObject = self:getLuaObjectOnSquare(isoObject:getSquare())
		if luaObject then
			STrapSystem.removedCache = copyTable(luaObject);
			--luaObject:spawnDestroyItems(isoObject:getSquare());
		end
	end
	SGlobalObjectSystem.OnObjectAboutToBeRemoved(self, isoObject)
end

function STrapSystem:OnDestroyIsoThumpable(isoObject, playerObj)
	if STrapSystem.removedCache and isoObject then
		STrapGlobalObject.SpawnDestroyItems(STrapSystem.removedCache.trapType, isoObject:getSquare(), isoObject)
		STrapSystem.removedCache = nil;
	end
	SGlobalObjectSystem.OnDestroyIsoThumpable(self, isoObject, playerObj)
end

SGlobalObjectSystem.RegisterSystemClass(STrapSystem)

Events.EveryDays.Add(STrapSystem.EveryDays);
Events.EveryHours.Add(STrapSystem.checkTrap);
Events.EveryTenMinutes.Add(STrapSystem.addSound);
