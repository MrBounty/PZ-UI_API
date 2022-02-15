--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 08/06/2017
-- Time: 09:25
-- To change this template use File | Settings | File Templates.
--

--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

ISEmptyGraves = ISBuildingObject:derive("ISEmptyGraves");

--************************************************************************--
--** ISEmptyGraves:new
--**
--************************************************************************--
function ISEmptyGraves:create(x, y, z, north, sprite)
	local cell = getWorld():getCell();
	self.sq = cell:getGridSquare(x, y, z);
	self:setInfo(self.sq, north, sprite, cell, "sprite1");
	
	local xa, ya, za = self:getSquare2Pos(self.sq, north)
	local squareA = getCell():getGridSquare(xa, ya, za);
	if squareA == nil then
		squareA = IsoGridSquare.new(getCell(), nil, xa, ya, za);
		getCell():ConnectNewSquare(squareA, false);
	end
	
	local spriteAName = north and self.northSprite2 or self.sprite2
	self:setInfo(squareA, north, spriteAName, cell, "sprite2");

	if self.sq:getZone() then
		self.sq:getZone():setHaveConstruction(true);
	end
end

function ISEmptyGraves:walkTo(x, y, z)
	local playerObj = getSpecificPlayer(self.player)
	local square = getCell():getGridSquare(x, y, z)
	local square2 = self:getSquare2(square, self.north)
	if square:DistToProper(playerObj) < square2:DistToProper(playerObj) then
		return luautils.walkAdj(playerObj, square)
	end
	return luautils.walkAdj(playerObj, square2)
end

function ISEmptyGraves:setInfo(square, north, sprite, cell, spriteType)
	for i=0,square:getObjects():size()-1 do
		local object = square:getObjects():get(i);
		if object:getProperties() and object:getProperties():Is(IsoFlagType.canBeRemoved) then
			square:transmitRemoveItemFromSquare(object)
			square:RemoveTileObject(object);
			break
		end
	end
	square:disableErosion();
	local args = { x = square:getX(), y = square:getY(), z = square:getZ() }
	sendClientCommand(nil, 'erosion', 'disableForSquare', args)
	
	self.javaObject = IsoThumpable.new(cell, square, sprite, north, self);
	square:RecalcAllWithNeighbours(true);
	self.javaObject:setName("EmptyGraves");
	self.javaObject:setCanBarricade(false);
	self.javaObject:setIsThumpable(false)
	self.javaObject:getModData()["spriteType"] = spriteType;
	self.javaObject:getModData()["corpses"] = 0;
	self.javaObject:getModData()["filled"] = false;
	square:AddSpecialObject(self.javaObject);
	self.javaObject:transmitCompleteItemToServer();
end

function ISEmptyGraves:new(sprite1, sprite2, northSprite1, northSprite2, shovel)
	local o = {};
	setmetatable(o, self);
	self.__index = self;
	o:init();
	o:setSprite(sprite1);
	o:setNorthSprite(northSprite1);
	o.sprite2 = sprite2;
	o.northSprite2 = northSprite2;
	o.noNeedHammer = true;
	o.ignoreNorth = true;
	o.equipBothHandItem = shovel;
	o.maxTime = 150;
	o.actionAnim = ISFarmingMenu.getShovelAnim(shovel);
	o.craftingBank = "Shoveling";
	return o;
end

-- return the health of the new stairs, it's 500 + 100 per carpentry lvl
function ISEmptyGraves:getHealth()
	return 500 + buildUtil.getWoodHealth(self);
end

function ISEmptyGraves:render(x, y, z, square)
	-- render the first part
	local spriteName = self:getSprite()
	local sprite = IsoSprite.new()
	sprite:LoadFramesNoDirPageSimple(spriteName)
	
	local floor = square:getFloor();
	if not floor then
		return;
	end
	local spriteFree = ISBuildingObject.isValid(self, square) and floor:getTextureName() and (luautils.stringStarts(floor:getTextureName(), "floors_exterior_natural") or luautils.stringStarts(floor:getTextureName(), "blends_natural_01"));

	spriteFree = spriteFree and ISEmptyGraves.shovelledFloorCanDig(square);

	if spriteFree and z==0 then
		sprite:RenderGhostTile(x, y, z);
	else
		sprite:RenderGhostTileRed(x, y, z);
	end

	local spriteAName = self.northSprite2;
	local spriteAFree = true;
	
	-- we get the x and y of our next tile (depend if we're placing the graves north or not)
	local xa, ya = self:getSquare2Pos(square, self.north)
	
	-- if we're not north we also change our sprite
	if not self.north then
		spriteAName = self.sprite2;
	end
	
	local squareA = getCell():getGridSquare(xa, ya, z);
	if squareA == nil and getWorld():isValidSquare(xa, ya, z) then
		squareA = IsoGridSquare.new(getCell(), nil, xa, ya, z);
		getCell():ConnectNewSquare(squareA, false);
	end
	
	local floorA = squareA:getFloor();
	if not floorA then
		return;
	end
	-- test if the square are free to add our graves
	if not buildUtil.canBePlace(self, squareA) or not (luautils.stringStarts(floorA:getTextureName(), "floors_exterior_natural") or luautils.stringStarts(floorA:getTextureName(), "blends_natural_01")) then
		spriteAFree = false;
	end
	spriteAFree = spriteAFree and ISEmptyGraves.shovelledFloorCanDig(squareA);
	-- render our second stage of the graves
	local spriteA = IsoSprite.new();
	spriteA:LoadFramesNoDirPageSimple(spriteAName);
	if spriteAFree and z==0 then
		spriteA:RenderGhostTile(xa, ya, z);
	else
		spriteA:RenderGhostTileRed(xa, ya, z);
	end
end

function ISEmptyGraves:isValid(square)
	if square:getZ() > 0 then
		return false
	end
	local floor = square:getFloor();
	if not ISBuildingObject.isValid(self, square) or
			not (luautils.stringStarts(floor:getTextureName(), "floors_exterior_natural") or
			luautils.stringStarts(floor:getTextureName(), "blends_natural_01")) then
		return false
	end
	local xa, ya, za = self:getSquare2Pos(square, self.north)
	local squareA = getCell():getGridSquare(xa, ya, za)
	if not squareA or not buildUtil.canBePlace(self, squareA) then
		return false
	end
	local floorA = squareA:getFloor();
	if not (luautils.stringStarts(floorA:getTextureName(), "floors_exterior_natural") or luautils.stringStarts(floorA:getTextureName(), "blends_natural_01")) then
		return false;
	end
	if not (ISEmptyGraves.shovelledFloorCanDig(square) or ISEmptyGraves.shovelledFloorCanDig(squareA)) then
		return false;
	end
	
	return true
end

function ISEmptyGraves.shovelledFloorCanDig(square)
	if (not square) or (not square:getFloor()) then
		return false;
	end
	if square:isInARoom() then
		return false;
	end
	local floor = square:getFloor();
	local sprites = floor:getModData() and floor:getModData().shovelledSprites;
	if sprites then
		for i=1,#sprites do
			local sprite = sprites[i];
			if luautils.stringStarts(sprite, "floors_exterior_natural") or luautils.stringStarts(sprite, "blends_natural_01") then
				return true;
			end
		end
		return false;
	else
		return true;
	end
	--return false;
end

function ISEmptyGraves:getSquare2Pos(square, north)
	local x = square:getX()
	local y = square:getY()
	local z = square:getZ()
	if north then
		y = y - 1
	else
		x = x - 1
	end
	return x, y, z
end

function ISEmptyGraves:getSquare2(square, north)
	local x, y, z = self:getSquare2Pos(square, north)
	return getCell():getGridSquare(x, y, z)
end

function ISEmptyGraves.canDigHere(worldObjects)
	local squares = {}
	local didSquare = {}
	for _,worldObj in ipairs(worldObjects) do
		if not didSquare[worldObj:getSquare()] then
			table.insert(squares, worldObj:getSquare())
			didSquare[worldObj:getSquare()] = true
		end
	end
	for _,square in ipairs(squares) do
		if square:getZ() > 0 then
			return false
		end
		local floor = square:getFloor()
		if floor and floor:getTextureName() and
				(luautils.stringStarts(floor:getTextureName(), "floors_exterior_natural") or
				luautils.stringStarts(floor:getTextureName(), "blends_natural_01")) then
			return true
		end
	end
	return false
end

function ISEmptyGraves.getMaxCorpses(grave)
	return 5
end

function ISEmptyGraves.isGraveFullOfCorpses(grave)
	if not grave or (grave:getName() ~= "EmptyGraves") then return false end
	return grave:getModData()["corpses"] >= ISEmptyGraves.getMaxCorpses(grave)
end

function ISEmptyGraves.isGraveFilledIn(grave)
	if not grave or (grave:getName() ~= "EmptyGraves") then return false end
	if grave:getModData()["filled"] then return true end

	-- Backwards compatibility: getModData().filled doesn't exist.
	local sprite = grave:getSprite()
	if not sprite or not sprite:getName() then return false end
	return sprite:getName() == "location_community_cemetary_01_40" or
		sprite:getName() == "location_community_cemetary_01_41" or
		sprite:getName() == "location_community_cemetary_01_42" or
		sprite:getName() == "location_community_cemetary_01_43"
end

