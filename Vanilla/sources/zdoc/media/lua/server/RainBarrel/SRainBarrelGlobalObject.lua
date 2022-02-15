--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

require "Map/SGlobalObject"

---@class SRainBarrelGlobalObject : SGlobalObject
SRainBarrelGlobalObject = SGlobalObject:derive("SRainBarrelGlobalObject")

function SRainBarrelGlobalObject:new(luaSystem, globalObject)
	local o = SGlobalObject.new(self, luaSystem, globalObject)
	return o
end

function SRainBarrelGlobalObject:initNew()
	self.exterior = false
	self.taintedWater = false
	self.waterAmount = 0
	self.waterMax = RainCollectorBarrel.largeWaterMax
end

function SRainBarrelGlobalObject:stateFromIsoObject(isoObject)
	self.exterior = isoObject:getSquare():isOutside()
	self.taintedWater = isoObject:isTaintedWater()
	self.waterAmount = isoObject:getWaterAmount()
	self.waterMax = isoObject:getModData().waterMax

	-- Sanity check
	if not self.waterMax then
		local spriteName = isoObject:getSprite() and isoObject:getSprite():getName()
		if spriteName == "carpentry_02_54" or spriteName == "carpentry_02_55" then
			self.waterMax = RainCollectorBarrel.smallWaterMax
		elseif spriteName == "carpentry_02_52" or spriteName == "carpentry_02_53" then
			self.waterMax = RainCollectorBarrel.largeWaterMax
		else
			self.waterMax = RainCollectorBarrel.smallWaterMax
		end
	end

	-- ISTakeWaterAction was fixed to consider storage capacity of water containers.
	-- Update old rainbarrels with 40/100 capacity to 160/400 capacity.
	if self.waterMax == 40 then self.waterMax = RainCollectorBarrel.smallWaterMax end
	if self.waterMax == 100 then self.waterMax = RainCollectorBarrel.largeWaterMax end

	isoObject:getModData().waterMax = self.waterMax
	self:changeSprite()
	isoObject:transmitModData()
end

function SRainBarrelGlobalObject:stateToIsoObject(isoObject)
	-- Sanity check
	if not self.waterAmount then
		self.waterAmount = 0
	end
	if not self.waterMax then
		local spriteName = isoObject:getSprite() and isoObject:getSprite():getName()
		if spriteName == "carpentry_02_54" or spriteName == "carpentry_02_55" then
			self.waterMax = RainCollectorBarrel.smallWaterMax
		elseif spriteName == "carpentry_02_52" or spriteName == "carpentry_02_53" then
			self.waterMax = RainCollectorBarrel.largeWaterMax
		else
			self.waterMax = RainCollectorBarrel.smallWaterMax
		end
	end

	-- ISTakeWaterAction was fixed to consider storage capacity of water containers.
	-- Update old rainbarrels with 40/100 capacity to 160/400 capacity.
	if self.waterMax == 40 then self.waterMax = RainCollectorBarrel.smallWaterMax end
	if self.waterMax == 100 then self.waterMax = RainCollectorBarrel.largeWaterMax end

	self.exterior = isoObject:getSquare():isOutside()

	if not self.taintedWater then
		self.taintedWater = self.waterAmount > 0 and self.exterior
	end
	isoObject:setTaintedWater(self.taintedWater)

	isoObject:setWaterAmount(self.waterAmount) -- FIXME? OnWaterAmountChanged happens here
	isoObject:getModData().waterMax = self.waterMax
	self:changeSprite()
	isoObject:transmitModData()
end

function SRainBarrelGlobalObject:changeSprite()
	local isoObject = self:getIsoObject()
	if not isoObject then return end
	local spriteName = nil
	if self.waterMax == RainCollectorBarrel.smallWaterMax then
		if self.waterAmount >= self.waterMax * 0.75 then
			spriteName = "carpentry_02_55"
		else
			spriteName = "carpentry_02_54"
		end
	elseif self.waterMax == RainCollectorBarrel.largeWaterMax then
		if self.waterAmount >= self.waterMax * 0.75 then
			spriteName = "carpentry_02_53"
		else
			spriteName = "carpentry_02_52"
		end
	end
	if spriteName and (not isoObject:getSprite() or spriteName ~= isoObject:getSprite():getName()) then
		self:noise('sprite changed to '..spriteName..' at '..self.x..','..self.y..','..self.z)
		isoObject:setSprite(spriteName)
		isoObject:transmitUpdatedSpriteToClients()
	end
end

