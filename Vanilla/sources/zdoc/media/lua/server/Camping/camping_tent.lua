-----------------------------------------------------------------------
--                          ROBERT JOHNSON                           --
-----------------------------------------------------------------------

---@class camping
camping = {}

camping.tentSprites = {
	sheet = {
		frontLeft = "TileIndieStoneTentFrontLeft",
		backLeft = "TileIndieStoneTentBackLeft",
		frontRight = "TileIndieStoneTentFrontRight",
		backRight = "TileIndieStoneTentBackRight"
	},
	tarp = {
		frontLeft = "camping_01_3",
		backLeft = "camping_01_2",
		frontRight = "camping_01_0",
		backRight = "camping_01_1"
	},
}

function camping.findTentSprites(sprite)
	for _,sprites in pairs(camping.tentSprites) do
		if sprite == sprites.frontLeft or sprite == sprites.backLeft or
				sprite == sprites.frontRight or sprite == sprites.backRight then
			return sprites
		end
	end
	return nil
end

local function addTentObject(grid, sprite, modData)

	if not grid then return; end
	local tent = IsoThumpable.new(getCell(), grid, sprite, false, {})
	tent:setName("Tent")
	tent:setThumpDmg(1)
	tent:setHealth(10)
	tent:setMaxHealth(10)
	tent:setBlockAllTheSquare(true)
	tent:setIsThumpable(true)
	if modData then
		tent:setModData(modData)
	end
	grid:AddSpecialObject(tent)
	tent:transmitCompleteItemToClients()
	return tent
end

-- add a tent to the ground
function camping.addTent(grid, sprite)
	if not grid then return end
	if camping.findTentObject(grid) then return end

	local tentSprites = camping.findTentSprites(sprite)
	if not tentSprites then return end

	local modData = {}
	modData["need:Base.Tarp"] = 1
	-- FIXME: could be Stake instead of TentPeg.
	modData["need:camping.TentPeg"] = 4
	modData["need:Base.WoodenStick"] = 2

	local tent = addTentObject(grid, sprite, modData)

	local dx = 0
	local dy = 0
	if sprite == tentSprites.frontLeft then
		sprite = tentSprites.backLeft
		dx = -1
	elseif sprite == tentSprites.frontRight then
		sprite = tentSprites.backRight
		dy = -1
	else
		error "expected front tent sprite"
	end

	grid = getCell():getGridSquare(grid:getX() + dx, grid:getY() + dy, grid:getZ())
	tent = addTentObject(grid, sprite, nil)

	return tent;
end

local function spriteNameOf(object)
	if not object then return nil end
	if not object:getSprite() then return nil end
	return object:getSprite():getName() or object:getSpriteName()
end

-- remove a tent
function camping.removeTent(tent)
	local objects = camping.getTentObjects(tent)
	for _,object in ipairs(objects) do
		local square = object:getSquare()
		square:transmitRemoveItemFromSquare(object)
	end
end

function camping.destroyTent(tent)
	local objects = camping.getTentObjects(tent)
	for _,object in ipairs(objects) do
		local square = object:getSquare()
		if object:hasModData() then
			-- Copied from ISBuildingObject.onDestroy()
			for index,value in pairs(object:getModData()) do
				if luautils.stringStarts(index, "need:") then
					local itemFullType = luautils.split(index, ":")[2]
					for i=1,tonumber(value) do
						if ZombRand(2) == 0 then
							-- item destroyed
						else
							square:AddWorldInventoryItem(itemFullType, 0.0, 0.0, 0.0)
						end
					end
				end
			end
		end
		square:transmitRemoveItemFromSquare(object)
	end
end

function camping.getTentObjects(tent)
	local objects = {}
	if not tent then return objects end

	local grid = tent:getSquare()
	if not grid then return objects end

	local spriteName = spriteNameOf(tent)
	local tentSprites = camping.findTentSprites(spriteName)
	if not tentSprites then return objects end

	table.insert(objects, tent)

	local dx = 0
	local dy = 0
	if spriteName == tentSprites.frontLeft then
		dx = -1
	elseif spriteName == tentSprites.backLeft then
		dx = 1
	elseif spriteName == tentSprites.frontRight then
		dy = -1
	elseif spriteName == tentSprites.backRight then
		dy = 1
	end

	grid = getCell():getGridSquare(grid:getX() + dx, grid:getY() + dy, grid:getZ())
	tent = camping.findTentObject(grid)
	if tent then
		table.insert(objects, tent)
	end

	return objects
end

function camping.isTentObject(object)
	if not object then return false end
	if not object:getSprite() then return false end
	local spriteName = spriteNameOf(object)
	return camping.findTentSprites(spriteName) ~= nil
--	return object:getObjectName() == "Tent"
end

function camping.findTentObject(square)
	if not square then return nil end
	for i=0,square:getObjects():size()-1 do
		local object = square:getObjects():get(i)
		if camping.isTentObject(object) then
			return object
		end
	end
	return nil
end

function camping.tentAt(x, y, z)
	return camping.findTentObject(getCell():getGridSquare(x, y, z))
end

-- return the tent on the gridSquare the player is standing on
-- or from the gridsquare in parameter (if from context menu for example)
camping.getCurrentTent = function(grid)
	if not grid then return nil end
	return camping.findTentObject(grid)
end

if isClient() then return end

local function noise(message) if getDebug() then print('tent: '..message) end end

local function OnClientCommand(module, command, player, args)
	if module ~= 'camping' then return end
	local argStr = ''
	for k,v in pairs(args) do argStr = argStr..' '..k..'='..v end
	noise('OnClientCommand '..module..' '..command..' '..argStr)
	if command == 'addTent' then
		local gs = getCell():getGridSquare(args.x, args.y, args.z)
		if gs then
			camping.addTent(gs, args.sprite)
		end
	elseif command == 'removeTent' then
		local tent = camping.tentAt(args.x, args.y, args.z)
		if tent then
			camping.removeTent(tent)
			local kit = InventoryItemFactory.CreateItem("camping.CampingTentKit")
			player:sendObjectChange('addItem', { item = kit } )
		end
	end
end

Events.OnClientCommand.Add(OnClientCommand)

