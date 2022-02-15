--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "Map/CGlobalObject"

---@class CTrapGlobalObject : CGlobalObject
CTrapGlobalObject = CGlobalObject:derive("CTrapGlobalObject")

function CTrapGlobalObject:new(luaSystem, globalObject)
	local o = CGlobalObject.new(self, luaSystem, globalObject)
	return o
end

function CTrapGlobalObject:fromModData(modData)
	for k,v in pairs(modData) do
		self[k] = v
	end

	if self.trapBait == "" then
		self.bait = nil
	else
		self.bait = self.trapBait
	end

	self.animal = {}
	local animalType = modData["animal"]
	for i,v in ipairs(Animals) do
		if v.type == animalType then
			self.animal = v
		end
	end
end

function CTrapGlobalObject:checkForWallExploit(square)
	if square then
		local north = getWorld():getCell():getGridSquare(square:getX(), square:getY()-1, square:getZ());
		local south = getWorld():getCell():getGridSquare(square:getX(), square:getY()+1, square:getZ());
		local east = getWorld():getCell():getGridSquare(square:getX()+1, square:getY(), square:getZ());
		local west = getWorld():getCell():getGridSquare(square:getX()-1, square:getY(), square:getZ());
		if square:isHoppableTo(north) or square:isHoppableTo(south) or square:isHoppableTo(east) or square:isHoppableTo(west) then
			return true;
		end
		if square:hasWindowOrWindowFrame() or (south and south:hasWindowOrWindowFrame()) or (east and east:hasWindowOrWindowFrame()) then
			return true;
		end
		return false;
	end
	return false;
end

