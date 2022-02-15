-------------------------------------------------------
--              ROBERT JOHNSON'S LUA UTILS           --
-------------------------------------------------------

luautils = {};

-- startWith java style !
luautils.stringStarts = function(String,Start)
	return string.sub(String, 1, string.len(Start)) == Start;
end

luautils.stringEnds = function(String, End)
	return String:sub(-End:len()) == End;
end

luautils.trim = function(s)
  return (s:gsub("^%s*(.-)%s*$", "%1"))
end

-- split java style
luautils.split = function(pString, pPattern)
   local Table = {};
   local fpat = "(.-)" .. pPattern;
   local last_end = 1;
   local s, e, cap = pString:find(fpat, 1);
   while s do
      if s ~= 1 or cap ~= "" then
		table.insert(Table,cap);
      end
      last_end = e+1;
      s, e, cap = pString:find(fpat, last_end);
   end
   if last_end <= #pString then
      cap = pString:sub(last_end);
      table.insert(Table, cap);
   end
   return Table
end

function luautils.indexOf(table1, value)
	for i,v in ipairs(table1) do
		if v == value then
			return i
		end
	end
	return -1
end

-- get all the tile in the range of the startingGrid
luautils.getNextTiles = function(cell, startingGrid, range)
	local result = {};
	local rangeX = startingGrid:getX() - range;
	if(rangeX < 0) then
		rangeX = 0;
	end
	local rangeY = startingGrid:getY() - range;
	if(rangeY < 0) then
		rangeY = 0;
	end
	local rangeX2 = startingGrid:getX() + range;
	local rangeY2 = startingGrid:getY() + range;
	for x2=rangeX, rangeX2 do
		for y2=rangeY, rangeY2 do
			local nextGrid = cell:getGridSquare(x2, y2, startingGrid:getZ());
			if nextGrid then
				table.insert(result, #result, nextGrid);
			end
		end
	end
	return result;
end

function luautils.walkAdj(playerObj, square, keepActions)
	if not keepActions then
		ISTimedActionQueue.clear(playerObj);
	end
--	if not AdjacentFreeTileFinder.isTileOrAdjacent(playerObj:getCurrentSquare(), square) then
		-- Avoid walking to already near spot
		local diffX = math.abs(square:getX() + 0.5 - playerObj:getX());
		local diffY = math.abs(square:getY() + 0.5 - playerObj:getY());
		if diffX <= 1.6 and diffY <= 1.6 then
			return true;
		end
        square = luautils.getCorrectSquareForWall(playerObj, square);
		local adjacent = AdjacentFreeTileFinder.Find(square, playerObj);
		if adjacent ~= nil then
			ISTimedActionQueue.add(ISWalkToTimedAction:new(playerObj, adjacent));
			return true;
		else
			return  false;
		end
--	else
--		return true;
--	end
end

-- taking other side of the wall if we're behind a collidable item
luautils.getCorrectSquareForWall = function(playerObj, square)
    if square:Is(IsoFlagType.collideW) and playerObj:getX() < square:getX() then
        return getCell():getGridSquare(square:getX() - 1, square:getY(), square:getZ());
    end
    if square:Is(IsoFlagType.collideN) and playerObj:getY() < square:getY() then
        return getCell():getGridSquare(square:getX(), square:getY() - 1, square:getZ());
    end
    return square;
end

function luautils.walkAdjWall(playerObj, square, north, keepActions)
	if not keepActions then
		ISTimedActionQueue.clear(playerObj);
	end
--	if not AdjacentFreeTileFinder.isTileOrAdjacent(playerObj:getCurrentSquare(), square) then
		local adjacent = AdjacentFreeTileFinder.FindWall(square, north, playerObj);
		if adjacent ~= nil then
			ISTimedActionQueue.add(ISWalkToTimedAction:new(playerObj, adjacent));
			return true;
		else
			return  false;
		end
--	else
--		return true;
--	end
end

function luautils.walkAdjWindowOrDoor(playerObj, square, item, keepActions)
	if not keepActions then
		ISTimedActionQueue.clear(playerObj);
	end
--	if not AdjacentFreeTileFinder.isTileOrAdjacent(playerObj:getCurrentSquare(), square) then
		local adjacent = AdjacentFreeTileFinder.FindWindowOrDoor(square, item, playerObj);
		if adjacent ~= nil then
			if adjacent == playerObj:getCurrentSquare() then
				return true;
			end
			ISTimedActionQueue.add(ISWalkToTimedAction:new(playerObj, adjacent));
			return true;
		else
			return  false;
		end
--	else
--		return true;
--	end
end

function luautils.walkToContainer(container, playerNum)
	if container:getType() == "floor" then
		return true
	end
	local playerObj = getSpecificPlayer(playerNum)
	if container:getParent() and container:getParent():getSquare():DistToProper(playerObj:getCurrentSquare()) < 2 then
		return true;
	end

	if container:isInCharacterInventory(playerObj) then
		return true
	end
	local isoObject = container:getParent()
	if not isoObject or not isoObject:getSquare() then
		return true
	end
	if instanceof(isoObject, "BaseVehicle") then
		if playerObj:getVehicle() == isoObject then
			return true
		end
		if playerObj:getVehicle() then
			error "luautils.walkToContainer()"
		end
		local part = container:getVehiclePart()
		if part and part:getArea() then
			if part:getVehicle():canAccessContainer(part:getIndex(), playerObj) then
				return true
			end
			if part:getDoor() and part:getInventoryItem() then
				-- TODO: open the door if needed
			end
			ISTimedActionQueue.add(ISPathFindAction:pathToVehicleArea(playerObj, part:getVehicle(), part:getArea()))
			return true
		end
		error "luautils.walkToContainer()"
	end
	if instanceof(isoObject, "IsoDeadBody") then
		return true
	end
	
	local adjacent = AdjacentFreeTileFinder.Find(isoObject:getSquare(), playerObj)
	if not adjacent then
		return false
	end
	if adjacent == playerObj:getCurrentSquare() then
		return true
	end
	ISTimedActionQueue.clear(playerObj)
	ISTimedActionQueue.add(ISWalkToTimedAction:new(playerObj, adjacent))
	return true
end

function luautils.haveToBeTransfered(player, item, dontWalk)
	if item and item:getContainer() ~= nil and item:getContainer() ~= player:getInventory() then
		if dontWalk then return true; end
		luautils.walkToContainer(item:getContainer(), player:getPlayerNum())
		return true
	else
		return false;
	end
end


function round(num, idp)
  local mult = 10^(idp or 0)
  return math.floor(num * mult + 0.5) / mult
end

function luautils.round(num, idp)
    return round(num,idp);
end

function luautils.updatePerksXp(perks, player)
	local level = player:getPerkLevel(perks);
	player:getXp():setXPToLevel(perks, level);
end

-------------------------------------------------------
--              ROBOMAT'S LUA UTILS           --
-------------------------------------------------------

---
-- This function tries to equip the passed items as
-- primary or secondary items. It will return the items
-- that had been originally equipped.
-- Alternatively you can pass on Strings (e.g.: "Base.Screwdriver") and
-- the function will try to find that item in the player's inventory and equip it.
--
-- @param _player - The player who equips the items.
-- @param _primItemToEquip - The item to equip in the primary slot.
-- @param _scndItemToEquip - The item to equip in the secondary slot.
--
-- @author RoboMat
--
function luautils.equipItems(_player, _primItemToEquip, _scndItemToEquip)
    local player = _player;
    local primItem = _primItemToEquip;
    local scndItem = _scndItemToEquip;

    -- If we didn't receive an actual item we use the string
    -- to find an appropriate item in the inventory.
    if type(primItem) == 'string' then
        primItem = player:getInventory():FindAndReturn(primItem);
    end
    if type(scndItem) == 'string' then
        scndItem = player:getInventory():FindAndReturn(scndItem);
    end

    -- Store the currently equipped items.
    local storePrim = player:getPrimaryHandItem();
    local storeScnd = player:getSecondaryHandItem();

    -- Equip the new items if necessary.
    if primItem then
        if not storePrim or storePrim ~= primItem then
            ISTimedActionQueue.add(ISEquipWeaponAction:new(player, primItem, 25, true));
        end
    end
    if scndItem then
        if not storeScnd or storeScnd ~= scndItem then
            ISTimedActionQueue.add(ISEquipWeaponAction:new(player, scndItem, 25, false));
        end
    end

    -- Return the stored items in case we want to re-equip them later on.
    return storePrim, storeScnd;
end


---
-- Shows a modal window that informs the player about something and only has
-- an okay button to be closed.
--
-- @param _text - The text to display on the modal
-- @param _centered - If set to true the modal will be centered (optional)
-- @param _width - The width of the window (optional)
-- @param _height - The height of the window (optional)
--
-- @author RoboMat
--
function luautils.okModal(_text, _centered, _width, _height, _posX, _posY)
    local posX = _posX or 0;
    local posY = _posY or 0;
    local width = _width or 230;
    local height = _height or 120;
    local centered = _centered;
    local txt = _text;
    local core = getCore();

    -- center the modal if necessary
    if centered then
        posX = core:getScreenWidth() * 0.5 - width * 0.5;
        posY = core:getScreenHeight() * 0.5 - height * 0.5;
    end

    local modal = ISModalDialog:new(posX, posY, width, height, txt, false, nil, nil);
    modal:initialise();
    modal:addToUIManager();
end

---
-- Based on the walkTo function from RJ's luautils.
--
-- @param _player - Player who should move his buttocks.
-- @param _object - The object / tile to walk to.
-- @param _cancelTA - Determines wether timed actions should be canceled
-- before the player starts to walk.
--
-- @author RoboMat
-- @since 2.0.0
--
function luautils.walkToObject(_player, _object, _cancelTA)
    local player = _player;
    local object = _object;
    local tile = _object:getSquare();
    local cancel = _cancelTA;

    -- Abort all current Timed Actions.
    if cancel then
        ISTimedActionQueue.clear(player);
    end

    -- Pathfinding and starting the actual walking.
    if not AdjacentFreeTileFinder.isTileOrAdjacent(player:getCurrentSquare(), tile) then
        local adjacent = AdjacentFreeTileFinder.FindWindowOrDoor(tile, object, player);
        if adjacent then
            ISTimedActionQueue.add(ISWalkToTimedAction:new(player, adjacent));
            return true;
        end
        return false;
    end
    return true;
end

---
-- Ported the WeaponLowerCondition(...) function of the
-- SwipeState.class to lua. Use this to damage your weapons.
-- If the weapon breaks during the execution of this function
-- it checks in which hand the item was equipped and tries to
-- replace it with the next best weapon.
--
-- @param _weapon - The weapon / item to damage.
-- @param _character - The player who is carrying the weapon.
-- @param _replace - (Optional) Wether or not to replace the item if it is broken.
-- @param _chance - (Optional) The chance for the weapon to be damaged. If omitted it uses the conditionLowerChance of the item.
--
-- @author RoboMat
--
function luautils.weaponLowerCondition(_weapon, _character, _replace, _chance)
    local weapon = _weapon;
    local chance = _chance or weapon:getConditionLowerChance();

    -- Random chance to damage the weapon based on the stats.
    if ZombRand(chance) == 0 then
        local replace = _replace or true;
        local condition = weapon:getCondition() - 1;
        weapon:setCondition(condition);

        -- If the weapon breaks unequip it and get a new one instead.
        if condition <= 0 and replace then
            local char = _character;
            local descriptor = char:getDescriptor();
            local newWeapon = char:getInventory():getBestWeapon(descriptor);

            -- Checks in which hand the item was equipped.
            local pos = luautils.isEquipped(weapon, char);

            -- Replace it with the new weapon.
            if pos == 1 then
                ISTimedActionQueue.add(ISEquipWeaponAction:new(char, newWeapon, 25, true));
            elseif pos == 2 then
                ISTimedActionQueue.add(ISEquipWeaponAction:new(char, newWeapon, 25, false));
            elseif pos == 3 then -- TODO
                ISTimedActionQueue.add(ISEquipWeaponAction:new(char, newWeapon, 25, false));
            end
        end
    end
end

---
-- Checks if the item is equipped as the primary or
-- secondary weapon of the player. Returns 1 if the
-- weapon is the primary, 2 if it is the secondary,
-- 3 if it is equipped in both hands at the same time
-- and zero if it is not equipped.
--
-- @param _item - The item to check for.
-- @param _player - The player to search through.
--
-- @return 1 if in primary hand.
-- @return 2 if in secondary hand.
-- @return 3 if in both hands.
-- @return 0 if not equipped.
--
-- @author RoboMat
--
function luautils.isEquipped(_item, _player)
    local p = _player;
    local i = _item;
    local prim = p:getPrimaryHandItem();
    local scnd = p:getSecondaryHandItem();

    if prim == i and scnd == i then
        return 3;
    elseif prim == i then
        return 1;
    elseif scnd == i then
        return 2;
    end
    return 0;
end

function luautils.split(inputstr, sep)
    if sep == nil then
        sep = "%s"
    end
    local t={} ; i=1
    for str in string.gmatch(inputstr, "([^"..sep.."]+)") do
        t[i] = str
        i = i + 1
    end
    return t
end

function luautils.getConditionRGB(condition)
	local r = ((100 - condition) / 100) ;
	local g = (condition / 100);
	return {r = r, g = g, b = 0};
end
