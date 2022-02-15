--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

require "Map/SGlobalObjectSystem"

---@class SRainBarrelSystem : SGlobalObjectSystem
SRainBarrelSystem = SGlobalObjectSystem:derive("SRainBarrelSystem")

function SRainBarrelSystem:new()
	local o = SGlobalObjectSystem.new(self, "rainbarrel")
	return o
end

function SRainBarrelSystem:initSystem()
	SGlobalObjectSystem.initSystem(self)

	-- Specify GlobalObjectSystem fields that should be saved.
	self.system:setModDataKeys({})
	
	-- Specify GlobalObject fields that should be saved.
	self.system:setObjectModDataKeys({'exterior', 'taintedWater', 'waterAmount', 'waterMax'})

	self:convertOldModData()
end

function SRainBarrelSystem:newLuaObject(globalObject)
	return SRainBarrelGlobalObject:new(self, globalObject)
end

function SRainBarrelSystem:isValidIsoObject(isoObject)
	return instanceof(isoObject, "IsoThumpable") and isoObject:getName() == "Rain Collector Barrel"
end

function SRainBarrelSystem:convertOldModData()
	-- If the gos_xxx.bin file existed, don't touch GameTime modData in case mods are using it.
	if self.system:loadedWorldVersion() ~= -1 then return end
	
	-- Global rainbarrel data was never saved anywhere.
	-- Rainbarrels wouldn't update unless they had been loaded in a session.
--	local modData = GameTime:getInstance():getModData()
end

function SRainBarrelSystem:checkRain()
	if not RainManager.isRaining() then return end
	for i=1,self:getLuaObjectCount() do
		local luaObject = self:getLuaObjectByIndex(i)
		if luaObject.waterAmount < luaObject.waterMax then
			local square = luaObject:getSquare()
			if square then
				luaObject.exterior = square:isOutside()
			end
			if luaObject.exterior then
				luaObject.waterAmount = math.min(luaObject.waterMax, luaObject.waterAmount + 1 * RainCollectorBarrel.waterScale)
				luaObject.taintedWater = true
				local isoObject = luaObject:getIsoObject()
				if isoObject then -- object might have been destroyed
					self:noise('added rain to barrel at '..luaObject.x..","..luaObject.y..","..luaObject.z..' waterAmount='..luaObject.waterAmount)
					isoObject:setTaintedWater(true)
					isoObject:setWaterAmount(luaObject.waterAmount)
					isoObject:transmitModData()
				end
			end
		end
	end
end

SGlobalObjectSystem.RegisterSystemClass(SRainBarrelSystem)

-- -- -- -- --

local noise = function(msg)
	SRainBarrelSystem.instance:noise(msg)
end

-- every 10 minutes we check if it's raining, to fill our water barrel
local function EveryTenMinutes()
	SRainBarrelSystem.instance:checkRain()
end

local function OnWaterAmountChange(object, prevAmount)
	if not object then return end
	local luaObject = SRainBarrelSystem.instance:getLuaObjectAt(object:getX(), object:getY(), object:getZ())
	if luaObject then
		noise('waterAmount changed to '..object:getWaterAmount()..' tainted='..tostring(object:isTaintedWater())..' at '..luaObject.x..','..luaObject.y..','..luaObject.z)
		luaObject.waterAmount = object:getWaterAmount()
		luaObject.taintedWater = object:isTaintedWater()
		luaObject:changeSprite(object)
	end
end


-- every 10 minutes we check if it's raining, to fill our water barrel
Events.EveryTenMinutes.Add(EveryTenMinutes)

Events.OnWaterAmountChange.Add(OnWaterAmountChange)

