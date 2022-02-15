--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

require "Map/SGlobalObjectSystem"

---@class SCampfireSystem : SGlobalObjectSystem
SCampfireSystem = SGlobalObjectSystem:derive("SCampfireSystem")

function SCampfireSystem:new()
	local o = SGlobalObjectSystem.new(self, "campfire")
	return o
end

function SCampfireSystem:initSystem()
	SGlobalObjectSystem.initSystem(self)

	-- Specify GlobalObjectSystem fields that should be saved.
	self.system:setModDataKeys(nil)
	
	-- Specify GlobalObject fields that should be saved.
	self.system:setObjectModDataKeys({'exterior', 'isLit', 'fuelAmt'})

	self:convertOldModData()
end

function SCampfireSystem:newLuaObject(globalObject)
	return SCampfireGlobalObject:new(self, globalObject)
end

function SCampfireSystem:isValidModData(modData)
	return modData ~= nil and modData.fuelAmt ~= nil
end

function SCampfireSystem:isValidIsoObject(isoObject)
	return isoObject and isoObject:getName() == "Campfire"
end

function SCampfireSystem:convertOldModData()
	-- If the gos_xxx.bin file existed, don't touch GameTime modData in case mods are using it.
	if self.system:loadedWorldVersion() ~= -1 then return end
	
	local modData = GameTime:getInstance():getModData()
	if modData.camping and modData.camping.campfires then
		self:noise('converting old-style GameTime modData')
		for _,campfire in pairs(modData.camping.campfires) do
			if not self.system:getObjectAt(campfire.x, campfire.y, campfire.z) then
				local globalObject = self.system:newObject(campfire.x, campfire.y, campfire.z)
				for k,v in pairs(campfire) do
					globalObject:getModData()[k] = v
				end
			end
		end
		modData.camping.campfires = nil
		for k,v in pairs(modData.camping) do
			self:noise("copied "..tostring(k).."="..tostring(v))
			camping.playerData[k] = v
		end
		modData.camping = nil
		self:noise('converted '..self:getLuaObjectCount()..' campfires')
	end
end

-- add a campfire to the ground
function SCampfireSystem:addCampfire(grid)
	if not grid then return end
	if self:getIsoObjectOnSquare(grid) then return nil end

	local luaObject = self:newLuaObjectOnSquare(grid)
	luaObject:initNew()
	luaObject:addObject()
	luaObject:addContainer()
	luaObject:getIsoObject():transmitCompleteItemToClients()

	self:noise("#campfires="..self:getLuaObjectCount())
	luaObject:saveData()
	return luaObject;
end

-- remove a camp fire
function SCampfireSystem:removeCampfire(luaObject)
	if not luaObject then return end
	luaObject:transferItemsToGround()
	luaObject:removeFireObject()
	-- This call also removes the luaObject because of the OnObjectAboutToBeRemoved event
	luaObject:removeIsoObject()
end

local function stringStarts(_string,_start)
    return string.sub(_string,1,string.len(_start))==_start
end

-- we lower by 1 the firelvl (every 3 hours if no more wood)
function SCampfireSystem:lowerFirelvl()
	for i=1,self:getLuaObjectCount() do
		local luaObject = self:getLuaObjectByIndex(i)
		local square = luaObject:getSquare()
		if square then
			luaObject.exterior = square:isOutside()
			if not luaObject.exterior and ZombRand(5) == 0 and luaObject.isLit then
                -- new spread: indoor fireplaces only spread to adjecent wood/carpet floors
                local newSquare = getCell():getGridSquare(luaObject.x + ZombRand(-1,1), luaObject.y + ZombRand(-1,1), luaObject.z);
                if newSquare and newSquare~=square then
                    local addFire = false;
                    --check for burnable floors
                    local floor = newSquare:getFloor();
                    if floor and floor:getSprite() and floor:getSprite():getName() then
                        local name = floor:getSprite():getName();
                        local n = floor:getSprite():getSheetGridIdFromName();
                        if n>=0 and name then
                            if (stringStarts(name,"floors_interior_tilesandwood") and n>=40) or
                                    stringStarts(name,"floors_interior_carpet") or
                                    stringStarts(name,"carpet") then
                                addFire = true;
                            end
                        end
                    end
                    if addFire then
                        IsoFireManager.StartFire(getCell(), newSquare, true, 100, 500);
                    end
                end
				--[[ OLD: local newSquare = getCell():getGridSquare(luaObject.x + 1, luaObject.y, luaObject.z);
				if newSquare then
					IsoFireManager.StartFire(getCell(), newSquare, true, 100, 500);
                end
                --]]
			end
		end
	end
end

function SCampfireSystem:lowerFuelAmount()
	for i=1,self:getLuaObjectCount() do
		local luaObject = self:getLuaObjectByIndex(i)
		if luaObject.isLit then
			local amt = 1
--			if RainManager.isRaining() and luaObject.exterior then amt = 2 end
			luaObject.fuelAmt = math.max(luaObject.fuelAmt - amt, 0)
			luaObject:changeFireLvl()
		end
	end
end

-- attract the zombie to the campfire and warm the player
function SCampfireSystem:nearCamp(delay)
	if isClient() then return end
	for i=1,self:getLuaObjectCount() do
		local luaObject = self:getLuaObjectByIndex(i)
		local gridSquare = luaObject:getSquare()
		-- if campfire is burning (and still there, I mean not destroy because of streaming)
		if luaObject.isLit and gridSquare then
			-- zombie are attracted 10 from the 10 next tile if fireLvl is at 1, or 20 if it's at 2
			local noiseRadius = luaObject:fireRadius()
			addSound(getPlayer(), gridSquare:getX(),gridSquare:getY(),gridSquare:getZ(), noiseRadius, noiseRadius);
		end
	end
end

function SCampfireSystem:OnClientCommand(command, playerObj, args)
	SCampfireSystemCommand(command, playerObj, args)
end

SGlobalObjectSystem.RegisterSystemClass(SCampfireSystem)

local function EveryTenMinutes()
	SCampfireSystem.instance:nearCamp(0)
	SCampfireSystem.instance:lowerFirelvl()
end


local function EveryOneMinute()
	SCampfireSystem.instance:lowerFuelAmount()
end

Events.EveryOneMinute.Add(EveryOneMinute)
Events.EveryTenMinutes.Add(EveryTenMinutes)

