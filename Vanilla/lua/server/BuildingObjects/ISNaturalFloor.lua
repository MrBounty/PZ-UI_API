--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

ISNaturalFloor = ISBuildingObject:derive("ISNaturalFloor");

--************************************************************************--
--** ISNaturalFloor:new
--**
--************************************************************************--
function ISNaturalFloor:create(x, y, z, north, sprite)
	self.sq = getWorld():getCell():getGridSquare(x, y, z);
	local floor = self.sq:getFloor()
	local spriteNames = floor:getModData().shovelledSprites or ISNaturalFloor.getFloorSpriteNames(self.sq)
	self.javaObject = self.sq:addFloor(sprite);
	self.javaObject:getModData().shovelled = nil
	self.javaObject:getModData().pouredFloor = self:getFloorType(self.item);
	if spriteNames and #spriteNames > 0 then
		self.javaObject:getModData().shovelledSprites = copyTable(spriteNames)
	end
	self.item:Use();
	-- bag is empty, we'll try to find another one
	local playerInv = self.character:getInventory()
	if not playerInv:containsRecursive(self.item) then
		self.item = playerInv:getFirstTypeRecurse(self.item:getFullType())
	end
	-- refresh backpacks to show equipped empty dirt bags
	getPlayerInventory(self.character:getPlayerNum()):refreshBackpacks();
	getPlayerLoot(self.character:getPlayerNum()):refreshBackpacks();
end

function ISNaturalFloor:new(sprite, northSprite, item, character)
	local o = {};
	setmetatable(o, self);
	self.__index = self;
	o:init();
	o:setSprite(sprite);
	o:setNorthSprite(northSprite);
	o.item = item;
    o.character = character;
    o.player = character:getPlayerNum()
    o.noNeedHammer = true;
    o.actionAnim = CharacterActionAnims.Pour;
	o.floorType = o:getFloorType(item);
	o.craftingBank = "DropSoilFromDirtBag";
	if o.floorType == "gravel" then
		o.craftingBank = "DropSoilFromGravelBag";
	elseif o.floorType == "sand" then
		o.craftingBank = "DropSoilFromSandBag";
	end
	return o;
end

function ISNaturalFloor:isValid(square)
	local playerInv = self.character:getInventory()
	if CFarmingSystem.instance:getLuaObjectOnSquare(square) then
		return false
	end
	if square and square:getProperties() then
		local props = square:getProperties();
		if props:Is(IsoFlagType.water) then
			return false;
		end
	end
	if square then
		local squareFloor = square:getFloor();
		if squareFloor and squareFloor:hasModData() and squareFloor:getModData().pouredFloor and squareFloor:getModData().pouredFloor == self.floorType then
			return false;
		end
	end
	return self.item ~= nil and playerInv:containsRecursive(self.item) and
		square ~= nil and square:getFloor() ~= nil
end

function ISNaturalFloor:render(x, y, z, square)
	ISBuildingObject.render(self, x, y, z, square)

	if not ISNaturalFloor.floorSprite then
		ISNaturalFloor.floorSprite = IsoSprite.new()
		ISNaturalFloor.floorSprite:LoadFramesNoDirPageSimple('media/ui/FloorTileCursor.png')
	end
	local r,g,b,a = 0.0,1.0,0.0,0.8
	if not self:isValid(square) then
		r = 1.0
		g = 0.0
	end
	ISNaturalFloor.floorSprite:RenderGhostTileColor(x, y, z, r, g, b, a)
end

function ISNaturalFloor:walkTo(x, y, z)
	if ISBuildingObject.walkTo(self, x, y, z) then
		ISWorldObjectContextMenu.transferIfNeeded(self.character, self.item)
		return true
	end
	return false
end

function ISNaturalFloor:getFloorType(item)
	if not item then
		return "none";
	end
	if item:getFullType()=="Base.Gravelbag" then
		return "gravel";
	elseif item:getFullType()=="Base.Dirtbag" then
		return "dirt";
	elseif item:getFullType()=="Base.Sandbag" then
		return "sand";
	end
end

function ISNaturalFloor.getFloorSpriteNames(square)
	local sprites = {}
	local floor = square:getFloor()
	if floor then
		local sprite = floor:getSprite()
		if sprite and sprite:getName() then
			table.insert(sprites, sprite:getName())
			local attached = floor:getAttachedAnimSprite()
			if attached then
				for i=1,attached:size() do
					sprite = attached:get(i-1):getParentSprite()
					if sprite and sprite:getName() then
						table.insert(sprites, sprite:getName())
					end
				end
			end
		end
	end
	return sprites
end

