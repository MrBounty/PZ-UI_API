--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

---@class FireFighting
FireFighting = {}

-- The number of InventoryItem.Use() calls per square extinguished.
function FireFighting.getExtinguisherUses(item)
	if not item then
		return 10000
	end
	if item:getType() == "Extinguisher" then
		return 1
	end
	if item:getType() == "Sandbag" or item:getType() == "Gravelbag" or item:getType() == "Dirtbag" then
		return 1
	end
	if item:isWaterSource() then
		return 10
	end
	return 10000
end

function FireFighting.isExtinguisher(item)
	if not item then return false end
	if item:getType() == "Extinguisher" then
		return item:getDrainableUsesInt() >= FireFighting.getExtinguisherUses(item)
	end
	if item:getType() == "Sandbag" or item:getType() == "Gravelbag" or item:getType() == "Dirtbag" then
		return item:getDrainableUsesInt() >= FireFighting.getExtinguisherUses(item)
	end
	if item:isWaterSource() then
		return item:getDrainableUsesInt() >= FireFighting.getExtinguisherUses(item)
	end
	return false
end

function FireFighting.getExtinguisher(playerObj)
	local primary = playerObj:getPrimaryHandItem()
	if FireFighting.isExtinguisher(primary) then return primary end

	local secondary = playerObj:getSecondaryHandItem()
	if FireFighting.isExtinguisher(secondary) then return secondary end
	
	local extinguisher = nil
	local bagOfX = nil
	local waterSource = nil

	for i=1,playerObj:getInventory():getItems():size() do
		local item = playerObj:getInventory():getItems():get(i-1)
		if FireFighting.isExtinguisher(item) then
			if item:isWaterSource() then
				if not waterSource then
					waterSource = item
				end
			end
			if item:getType() == "Extinguisher" then
				if not extinguisher then
					extinguisher = item
				end
			end
			if item:getType() == "Sandbag" or item:getType() == "Gravelbag" or item:getType() == "Dirtbag" then
				if not bagOfX then
					bagOfX = item
				end
			end
		end
	end

	return extinguisher or bagOfX or waterSource
end

function FireFighting.isSquareToExtinguish(square)
	if not square then
		return false
	end
	if square:Is(IsoFlagType.burning) then
		for i=1,square:getObjects():size() do
			local object = square:getObjects():get(i-1)
			if instanceof(object, "IsoFire") and not object:isPermanent() then
				return true
			end
		end
	end
	for i=1,square:getMovingObjects():size() do
		local chr = square:getMovingObjects():get(i-1)
		if instanceof(chr, "IsoGameCharacter") and chr:isOnFire() then
			return true
		end
	end
	return false
end

function FireFighting.getSquareToExtinguish(square)
	if FireFighting.isSquareToExtinguish(square) then
		return square
	end
	local x,y,z = square:getX(),square:getY(),square:getZ()
	for dy = -1,1 do
		for dx = -1,1 do
			if dx ~= 0 or dy ~= 0 then
				square = getCell():getGridSquare(x+dx, y+dy, z)
				if FireFighting.isSquareToExtinguish(square) then
					return square
				end
			end
		end
	end
end

