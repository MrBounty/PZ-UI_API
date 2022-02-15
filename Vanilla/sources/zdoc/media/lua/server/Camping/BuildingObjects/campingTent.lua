require "BuildingObjects/ISBuildingObject"

--***********************************************************
--**                    ROBERT JOHNSON                     **
--** Help you to place your tent by let you dragging a ghost render of the tent around **
--***********************************************************

---@class campingTent : ISBuildingObject
campingTent = ISBuildingObject:derive("campingTent");

--************************************************************************--
--** campingTent:new
--**
--************************************************************************--
function campingTent:create(x, y, z, north, sprite)
	local sq = getWorld():getCell():getGridSquare(x, y, z);
	ISTimedActionQueue.add(ISAddTentAction:new(self.character, sq, self.character:getInventory():FindAndReturn("CampingTentKit"), sprite, 0));
end

function campingTent:walkTo(x, y, z)
	local playerObj = getSpecificPlayer(self.player)
	local square = getCell():getGridSquare(x, y, z)
	local square2 = self:getSquare2(square, self.north)
	if square:DistToProper(playerObj) < square2:DistToProper(playerObj) then
		return luautils.walkAdj(playerObj, square)
	end
	return luautils.walkAdj(playerObj, square2)
end

function campingTent:onTimedActionStart(action)
	ISBuildingObject.onTimedActionStart(self, action)
	self.character:SetVariable("LootPosition", "Mid")
	action:setOverrideHandModels(nil, nil)
	-- ISBuildAction is running, ISAddTentAction completes instantly
	action.character:faceLocation(action.square:getX(), action.square:getY())
end

function campingTent:new(character, tentSprites)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o:init()
	o:setNorthSprite(tentSprites.frontLeft);
	o:setSprite(tentSprites.frontRight);
	o.tentSprites = tentSprites
    o.character = character;
    o.noNeedHammer = true;
    o.actionAnim = "Loot"
--~ 	o:setDragNilAfterPlace(true);
	return o;
end

function campingTent:isValid(square)
	local valid = self:isSquareFree(square)
	if valid and not self.character:getInventory():contains("CampingTentKit") then
		valid = false
	end
	local square2 = self:getSquare2(square, self.north)
	if valid and not (square2 and self:isSquareFree(square2) and not square:isSomethingTo(square2)) then
		valid = false
	end
--~ 	local result = false;
--~ 	for i = 0, square:getObjects():size() - 1 do
--~ 		local item = square:getObjects():get(i);
--~ 		if luautils.stringStarts(item:getTextureName(), "floors_exterior")  or luautils.stringStarts(item:getTextureName(), "blends_natural")then
--~ 			result = true;
--~ 			break;
--~ 		end
--~ 	end
--~ 	if result then
--~ 		result = square:getSpecialObjects():size() == 0;
--~ 	end
	return valid
end

function campingTent:render(x, y, z, square)
	-- render the first part
	ISBuildingObject.render(self, x, y, z, square)

	-- name of our 2nd tent sprite
	local sprite2Name = self.tentSprites.backLeft;
	if not self.north then
		sprite2Name = self.tentSprites.backRight;
	end

	-- render the 2nd part of the tent
	local sprite2 = IsoSprite.new();
	sprite2:LoadFramesNoDirPageSimple(sprite2Name);
	x, y, z = self:getSquare2Pos(square, self.north)
	if self:isValid(square) then
		sprite2:RenderGhostTile(x, y, z);
	else
		sprite2:RenderGhostTileRed(x, y, z);
	end
end

function campingTent:isSquareFree(square)
	if not square then return false end
	if square:getMovingObjects():size() > 0 then return false end
	if square:getStaticMovingObjects():size() > 0 then return false end
	if square:isVehicleIntersecting() then return false end
	for i=0,square:getObjects():size()-1 do
		local object = square:getObjects():get(i)
		if object:getSprite() and not object:getSprite():getProperties():Is(IsoFlagType.solidfloor) then
            if object:getType() == IsoObjectType.tree or object:getType() == IsoObjectType.wall then
                return false;
            end
		    return not square:isSolidTrans();
		end
	end
	return true
end

function campingTent:getSquare2Pos(square, north)
	local x = square:getX()
	local y = square:getY()
	local z = square:getZ()
	if north then
		x = x - 1
	else
		y = y - 1
	end
	return x, y, z
end

function campingTent:getSquare2(square, north)
	local x, y, z = self:getSquare2Pos(square, north)
	return getCell():getGridSquare(x, y, z)
end

