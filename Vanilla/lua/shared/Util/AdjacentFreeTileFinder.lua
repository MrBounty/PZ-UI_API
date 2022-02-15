--***********************************************************
--**                LEMMY/ROBERT JOHNSON                   **
--***********************************************************

AdjacentFreeTileFinder = {}

-- returns true if test is not blocked from src by a wall.
AdjacentFreeTileFinder.privTrySquareForWalls = function(src, test)

    if src == nil or test == nil then return false; end

    if src:getX() < test:getX() and src:getY() == test:getY() then
        if test:Is("DoorWallW") and not test:isDoorBlockedTo(src) then return true; end
        if test:Is(IsoFlagType.cutW) or test:Is(IsoFlagType.collideW) then return false; end
    end
    if src:getX() > test:getX() and src:getY() == test:getY()  then
        if src:Is("DoorWallW") and not src:isDoorBlockedTo(test) then return true; end
        if src:Is(IsoFlagType.cutW)  or src:Is(IsoFlagType.collideW) then return false; end
    end

    if src:getY() < test:getY() and src:getX() == test:getX()  then
        if test:Is("DoorWallN") and not test:isDoorBlockedTo(src) then return true; end
        if test:Is(IsoFlagType.cutN)  or test:Is(IsoFlagType.collideN)  then return false; end
    end
    if src:getY() > test:getY() and src:getX() == test:getX()  then
        if src:Is("DoorWallN") and not src:isDoorBlockedTo(test) then return true; end
        if src:Is(IsoFlagType.cutN)  or src:Is(IsoFlagType.collideN) then return false; end
    end

    if src:getX() ~= test:getX() and src:getY() ~= test:getY() then
        if not AdjacentFreeTileFinder.privTrySquareForWalls2(src, test:getX(), src:getY(), src:getZ() or
            not AdjacentFreeTileFinder.privTrySquareForWalls2(src, src:getX(), test:getY(), src:getZ()) or
            not AdjacentFreeTileFinder.privTrySquareForWalls2(test, test:getX(), src:getY(), src:getZ()) or
            not AdjacentFreeTileFinder.privTrySquareForWalls2(test, src:getX(), test:getY(), src:getZ())) then
            return false
        end
    end

    return true;
end

AdjacentFreeTileFinder.privTrySquareForWalls2 = function(src, x, y, z)
    return AdjacentFreeTileFinder.privTrySquareForWalls(src, getCell():getGridSquare(x, y, z))
end

-- returns true if test is adjacent to src.
AdjacentFreeTileFinder.privTrySquare = function(src, test)

     -- if either is null, its not adjacent.
     if(src == nil or test == nil) then return false; end

     -- if either are on a different floor, not adjacent.
     if src:getZ() ~= test:getZ() then return false; end

     -- if there is a wall between the two tiles, not adjacent.
     if not AdjacentFreeTileFinder.privTrySquareForWalls(src, test) then
         return false;
    end

     -- if the test one is solid / not walkable, not adjacent.
    -- if there is no floor on test, not adjacent.
    if not AdjacentFreeTileFinder.privCanStand(test) then
       return false;
    end



     -- adjacent!
    return true;

end

-- returns true if b is either the same tile as a, or is adjacent to a.
AdjacentFreeTileFinder.isTileOrAdjacent8 = function(a, b)

--print("Testing is tile or adjacent");

    if a == b then
        --print("Same tile.")
        return true;
    end

    if(math.abs(a:getX() - b:getX()) > 1) then
        --print("Further than 1 away")
        return false;
    end
    if(math.abs(a:getY() - b:getY()) > 1) then
        --print("Further than 1 away")
        return false;
    end

    local res = AdjacentFreeTileFinder.privTrySquare(a, b);
    if res == true then
        --print("Is adjacent")
    else
        --print("Is not adjacent")
    end
    return res;
end
-- returns true if b is either the same tile as a, or is adjacent to a.
AdjacentFreeTileFinder.isTileOrAdjacent = function(a, b)

--print("Testing is tile or adjacent");

    if a == b then
        --print("Same tile.")
        return true;
    end

    if(math.abs(a:getX() - b:getX()) > 1) then
        --print("Further than 1 away")
        return false;
    end
    if(math.abs(a:getY() - b:getY()) > 1) then
        --print("Further than 1 away")
        return false;
    end

    local res = AdjacentFreeTileFinder.privTrySquare(a, b);
    if res == true then
        --print("Is adjacent")
    else
        --print("Is not adjacent")
    end
    return res;
end

-- find a free tile that's adjacent to gridSquare and return it.

AdjacentFreeTileFinder.Find = function(gridSquare, playerObj)
    local choices = {}
    local choicescount = 1;
    -- first try straight lines (N/S/E/W)
    local a = gridSquare:getAdjacentSquare(IsoDirections.W)
    local b = gridSquare:getAdjacentSquare(IsoDirections.E)
    local c = gridSquare:getAdjacentSquare(IsoDirections.N)
    local d = gridSquare:getAdjacentSquare(IsoDirections.S)

    -- for each of them, test that square then if it's 'adjacent' then add it to the table for picking.
    if AdjacentFreeTileFinder.privTrySquare(gridSquare, a) then table.insert(choices, a); choicescount = choicescount + 1; end
    if AdjacentFreeTileFinder.privTrySquare(gridSquare, b) then table.insert(choices,  b); choicescount = choicescount + 1;end
    if AdjacentFreeTileFinder.privTrySquare(gridSquare, c) then  table.insert(choices, c); choicescount = choicescount + 1;end
    if AdjacentFreeTileFinder.privTrySquare(gridSquare, d) then table.insert(choices, d); choicescount = choicescount + 1; end

    -- only do diags if no other choices.
    if choicescount == 1 then
        -- now do diags.
        a = gridSquare:getAdjacentSquare(IsoDirections.NW)
        b = gridSquare:getAdjacentSquare(IsoDirections.NE)
        c = gridSquare:getAdjacentSquare(IsoDirections.SW)
        d = gridSquare:getAdjacentSquare(IsoDirections.SE)

        if AdjacentFreeTileFinder.privTrySquare(gridSquare, a) then  table.insert(choices, a); choicescount = choicescount + 1; end
        if AdjacentFreeTileFinder.privTrySquare(gridSquare, b) then  table.insert(choices,  b); choicescount = choicescount + 1;end
        if AdjacentFreeTileFinder.privTrySquare(gridSquare, c) then  table.insert(choices, c); choicescount = choicescount + 1;end
        if AdjacentFreeTileFinder.privTrySquare(gridSquare, d) then  table.insert(choices, d); choicescount = choicescount + 1; end

    end

    -- if we have multiple choices, pick the one closest to the player
    if choicescount > 1 then
       local lowestdist = 100000;
       local distchoice = nil;

       for i, k in ipairs(choices) do
          local dist = k:DistToProper(playerObj);
          if dist < lowestdist then
              lowestdist = dist;
              distchoice = k;
          end
       end

        return distchoice;
    end
    return nil;
end

local function tryAddChoice(square, dir, choices)
    local adjacent = square:getAdjacentSquare(dir)
    if adjacent and AdjacentFreeTileFinder.privTrySquare(square, adjacent) then
        table.insert(choices, adjacent)
    end
end

AdjacentFreeTileFinder.FindClosest = function(gridSquare, playerObj)
    local choices = {}
    tryAddChoice(gridSquare, IsoDirections.NW, choices)
    tryAddChoice(gridSquare, IsoDirections.N, choices)
    tryAddChoice(gridSquare, IsoDirections.NE, choices)
    tryAddChoice(gridSquare, IsoDirections.E, choices)
    tryAddChoice(gridSquare, IsoDirections.SE, choices)
    tryAddChoice(gridSquare, IsoDirections.S, choices)
    tryAddChoice(gridSquare, IsoDirections.SW, choices)
    tryAddChoice(gridSquare, IsoDirections.W, choices)
    if #choices == 0 then
        return nil
    end
    local minDist = 100000
    local closestSq = nil
    for _,square in ipairs(choices) do
        local dist = square:DistToProper(playerObj)
        if dist < minDist then
            minDist = dist
            closestSq = square
        end
    end
    return closestSq
end

local function getClosestChoice(choices, playerObj)
    local minDist = 100000
    local closest = nil
    for _,square in ipairs(choices) do
        local dist = square:DistTo(playerObj)
        if dist < minDist then
            minDist = dist
            closest = square
        end
    end
    return closest
end

AdjacentFreeTileFinder.FindWindowOrDoor = function(gridSquare, window, playerObj)
	local choices = {};

    if AdjacentFreeTileFinder.privGetNorth(gridSquare, window) then
        local n = gridSquare:getAdjacentSquare(IsoDirections.N);
        if instanceof(window, "IsoCurtain") and window:getType() == IsoObjectType.curtainS then n = nil end
        if n and playerObj:getCurrentSquare():getRoom() == n:getRoom() and AdjacentFreeTileFinder.privTrySquareWindow(gridSquare, n) then
            table.insert(choices, n)
        end
        if playerObj:getCurrentSquare():getRoom() == gridSquare:getRoom() and AdjacentFreeTileFinder.privCanStand(gridSquare) then
            table.insert(choices, gridSquare)
        end
        if #choices == 0 then
            if n and AdjacentFreeTileFinder.privTrySquareWindow(gridSquare, n) then
                table.insert(choices, n)
            end
            if AdjacentFreeTileFinder.privCanStand(gridSquare) then
                table.insert(choices, gridSquare)
            end
        end
    else
        local w = gridSquare:getAdjacentSquare(IsoDirections.W);
        if instanceof(window, "IsoCurtain") and window:getType() == IsoObjectType.curtainE then w = nil end
        if w and playerObj:getCurrentSquare():getRoom() == w:getRoom() and AdjacentFreeTileFinder.privTrySquareWindow(gridSquare, w) then
            table.insert(choices, w)
        end
        if playerObj:getCurrentSquare():getRoom() == gridSquare:getRoom() and AdjacentFreeTileFinder.privCanStand(gridSquare) then
            table.insert(choices, gridSquare)
        end
        if #choices == 0 then
            if w and AdjacentFreeTileFinder.privTrySquareWindow(gridSquare, w) then
                table.insert(choices, w)
            end
            if AdjacentFreeTileFinder.privCanStand(gridSquare) then
                table.insert(choices, gridSquare)
            end
        end
    end

    -- only do diags if no other choices.
    if #choices == 0 then
        -- now do diags.
        local a = gridSquare:getAdjacentSquare(IsoDirections.NW)
        local b = gridSquare:getAdjacentSquare(IsoDirections.NE)
        local c = gridSquare:getAdjacentSquare(IsoDirections.SW)
        local d = gridSquare:getAdjacentSquare(IsoDirections.SE)

        if AdjacentFreeTileFinder.privTrySquareWindow(gridSquare, a) then table.insert(choices, a) end
        if AdjacentFreeTileFinder.privTrySquareWindow(gridSquare, b) then table.insert(choices, b) end
        if AdjacentFreeTileFinder.privTrySquareWindow(gridSquare, c) then table.insert(choices, c) end
        if AdjacentFreeTileFinder.privTrySquareWindow(gridSquare, d) then table.insert(choices, d) end

    end

    -- if we have multiple choices, pick the one closest to the player
    return getClosestChoice(choices, playerObj)
end

AdjacentFreeTileFinder.FindWall = function(gridSquare, north, playerObj)
    local choices = {};
    
    if north then
        local n = gridSquare:getAdjacentSquare(IsoDirections.N);
        if n and playerObj:getCurrentSquare():getRoom() == n:getRoom() and AdjacentFreeTileFinder.privTrySquareWindow(gridSquare, n) then
            table.insert(choices, n)
        end
        if playerObj:getCurrentSquare():getRoom() == gridSquare:getRoom() and AdjacentFreeTileFinder.privCanStand(gridSquare) then
            table.insert(choices, gridSquare)
        end
        if #choices == 0 then
            if n and AdjacentFreeTileFinder.privTrySquareWindow(gridSquare, n) then
                table.insert(choices, n)
            end
            if AdjacentFreeTileFinder.privCanStand(gridSquare) then
                table.insert(choices, gridSquare)
            end
        end
    else
        local w = gridSquare:getAdjacentSquare(IsoDirections.W);
        if w and playerObj:getCurrentSquare():getRoom() == w:getRoom() and AdjacentFreeTileFinder.privTrySquareWindow(gridSquare, w) then
            table.insert(choices, w)
        end
        if playerObj:getCurrentSquare():getRoom() == gridSquare:getRoom() and AdjacentFreeTileFinder.privCanStand(gridSquare) then
            table.insert(choices, gridSquare)
        end
        if #choices == 0 then
            if w and AdjacentFreeTileFinder.privTrySquareWindow(gridSquare, w) then
                table.insert(choices, w)
            end
            if AdjacentFreeTileFinder.privCanStand(gridSquare) then
                table.insert(choices, gridSquare)
            end
        end
    end
    
    -- only do diags if no other choices.
    if #choices == 0 then
        -- now do diags.
        local a = gridSquare:getAdjacentSquare(IsoDirections.NW)
        local b = gridSquare:getAdjacentSquare(IsoDirections.NE)
        local c = gridSquare:getAdjacentSquare(IsoDirections.SW)
        local d = gridSquare:getAdjacentSquare(IsoDirections.SE)
        
        if AdjacentFreeTileFinder.privTrySquareWindow(gridSquare, a) then table.insert(choices, a) end
        if AdjacentFreeTileFinder.privTrySquareWindow(gridSquare, b) then table.insert(choices, b) end
        if AdjacentFreeTileFinder.privTrySquareWindow(gridSquare, c) then table.insert(choices, c) end
        if AdjacentFreeTileFinder.privTrySquareWindow(gridSquare, d) then table.insert(choices, d) end
    
    end
    
    -- if we have multiple choices, pick the one closest to the player
    return getClosestChoice(choices, playerObj)
end

function AdjacentFreeTileFinder.FindEdge(gridSquare, dir, playerObj, preferSameSquare)
    local choices = {}
    if AdjacentFreeTileFinder.privCanStand(gridSquare) then
        if preferSameSquare then return gridSquare end
        table.insert(choices, gridSquare)
    end
    
    local n = gridSquare:getAdjacentSquare(IsoDirections.N)
    local s = gridSquare:getAdjacentSquare(IsoDirections.S)
    local w = gridSquare:getAdjacentSquare(IsoDirections.W)
    local e = gridSquare:getAdjacentSquare(IsoDirections.E)
    if dir == IsoDirections.N or dir == IsoDirections.S then
        tryAddChoice(gridSquare, IsoDirections.W, choices)
        tryAddChoice(gridSquare, IsoDirections.E, choices)
    end
    if dir == IsoDirections.W or dir == IsoDirections.E then
        tryAddChoice(gridSquare, IsoDirections.N, choices)
        tryAddChoice(gridSquare, IsoDirections.S, choices)
    end
    return getClosestChoice(choices, playerObj)
end

-- returns true if test is adjacent to src.
AdjacentFreeTileFinder.privTrySquareWindow = function(src, test)

    if test == src then return true; end
     -- if either is null, its not adjacent.
     if(src == nil or test == nil) then return false; end

     -- if either are on a different floor, not adjacent.
     if src:getZ() ~= test:getZ() then return false; end

    -- is diag test and src are on diagnel, test straight lines between them
    if test:getX() ~= src:getX() and test:getY() ~= src:getY() then
        local betweenA = getCell():getGridSquare(test:getX(), src:getY(), test:getZ());
        local betweenB = getCell():getGridSquare(src:getX(), test:getY(), test:getZ());

        -- must be no interfering walls on either of the directions
        if(not AdjacentFreeTileFinder.privTrySquareForWalls(src, betweenA) or not AdjacentFreeTileFinder.privTrySquareForWalls(src, betweenB)) then
            return false;
        end
        if(not AdjacentFreeTileFinder.privTrySquareForWalls(test, betweenA) or not AdjacentFreeTileFinder.privTrySquareForWalls(test, betweenB)) then
            return false;
        end
    end

    if not AdjacentFreeTileFinder.privCanStand(test) then
        return false
    end

     -- adjacent!
    return true;

end

AdjacentFreeTileFinder.privCanStand = function(test)
	if not test then return false end

    if test:Is(IsoFlagType.solid) then
        return false;
    end

    -- Player can stand on solidtrans squares adjacent to windows.
    if test:Is(IsoFlagType.solidtrans) then
        local hasWindow = false
        if test:Is(IsoFlagType.windowW) or test:Is(IsoFlagType.windowN) then
            hasWindow = true
        end
        if not hasWindow then
            local s = test:getAdjacentSquare(IsoDirections.S)
            if s and s:Is(IsoFlagType.windowN) then
                hasWindow = true
            end
        end
        if not hasWindow then
            local e = test:getAdjacentSquare(IsoDirections.E)
            if e and e:Is(IsoFlagType.windowW) then
                hasWindow = true
            end
        end
        if not hasWindow then
            return false
        end
    end

    if not test:TreatAsSolidFloor() then
       return false;
    end

    return true
end

AdjacentFreeTileFinder.privGetNorth = function(gridSquare, object)
    if object and object.getNorth then
        return object:getNorth()
    end
    return gridSquare and (gridSquare:Is(IsoFlagType.cutN) or gridSquare:Is(IsoFlagType.collideN))
end
