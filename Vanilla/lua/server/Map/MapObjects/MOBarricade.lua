--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

local function FindBarricadeAble(square, dir)
	local north = (dir == IsoDirections.N) or (dir == IsoDirections.S)
	if dir == IsoDirections.E then
		square = getCell():getGridSquare(square:getX()+1, square:getY(), square:getZ())
	elseif dir == IsoDirections.S then
		square = getCell():getGridSquare(square:getX(), square:getY()+1, square:getZ())
	end
	for i=1,square:getObjects():size() do
		local object = square:getObjects():get(i-1)
		if instanceof(object, "BarricadeAble") and (north == object:getNorth()) then
			return object
		end
	end
	return nil
end

local function ReplaceExistingObject(object, dir, numPlank, numMetalSheet, numMetalBar)
	local square = object:getSquare()
	local addTo = FindBarricadeAble(square, dir)
	if not addTo then
		return
	end
	local addOpposite = dir==IsoDirections.E or dir==IsoDirections.S
	local barricade = IsoBarricade.AddBarricadeToObject(addTo, addOpposite)
	if not barricade then
		return
	end
	square:transmitRemoveItemFromSquare(object)
	-- TODO: random health
	if numPlank then
		for i=1,numPlank do
			barricade:addPlank(nil, nil)
		end
	elseif numMetalSheet then
		barricade:addMetal(nil, nil)
	elseif numMetalBar then
		barricade:addMetalBar(nil, nil)
	end
	barricade:transmitCompleteItemToClients()
end

-- -- -- -- --

local function PlankWest1(object)
	ReplaceExistingObject(object, IsoDirections.W, 1, nil, nil)
end

local function PlankWest2(object)
	ReplaceExistingObject(object, IsoDirections.W, 2, nil, nil)
end

local function PlankWest3(object)
	ReplaceExistingObject(object, IsoDirections.W, 3, nil, nil)
end

local function PlankWest4(object)
	ReplaceExistingObject(object, IsoDirections.W, 4, nil, nil)
end

-- -- -- -- --

local function PlankNorth1(object)
	ReplaceExistingObject(object, IsoDirections.N, 1, nil, nil)
end

local function PlankNorth2(object)
	ReplaceExistingObject(object, IsoDirections.N, 2, nil, nil)
end

local function PlankNorth3(object)
	ReplaceExistingObject(object, IsoDirections.N, 3, nil, nil)
end

local function PlankNorth4(object)
	ReplaceExistingObject(object, IsoDirections.N, 4, nil, nil)
end

-- -- -- -- --

local function PlankEast1(object)
	ReplaceExistingObject(object, IsoDirections.E, 1, nil, nil)
end

local function PlankEast2(object)
	ReplaceExistingObject(object, IsoDirections.E, 2, nil, nil)
end

local function PlankEast3(object)
	ReplaceExistingObject(object, IsoDirections.E, 3, nil, nil)
end

local function PlankEast4(object)
	ReplaceExistingObject(object, IsoDirections.E, 4, nil, nil)
end

-- -- -- -- --

local function PlankSouth1(object)
	ReplaceExistingObject(object, IsoDirections.S, 1, nil, nil)
end

local function PlankSouth2(object)
	ReplaceExistingObject(object, IsoDirections.S, 2, nil, nil)
end

local function PlankSouth3(object)
	ReplaceExistingObject(object, IsoDirections.S, 3, nil, nil)
end

local function PlankSouth4(object)
	ReplaceExistingObject(object, IsoDirections.S, 4, nil, nil)
end

-- -- -- -- --

local function MetalSheetWest(object)
	ReplaceExistingObject(object, IsoDirections.W, nil, 1, nil)
end

local function MetalSheetNorth(object)
	ReplaceExistingObject(object, IsoDirections.N, nil, 1, nil)
end

local function MetalSheetEast(object)
	ReplaceExistingObject(object, IsoDirections.E, nil, 1, nil)
end

local function MetalSheetSouth(object)
	ReplaceExistingObject(object, IsoDirections.S, nil, 1, nil)
end

-- -- -- -- --

local function MetalBarWest(object)
	ReplaceExistingObject(object, IsoDirections.W, nil, nil, 1)
end

local function MetalBarNorth(object)
	ReplaceExistingObject(object, IsoDirections.N, nil, nil, 1)
end

local function MetalBarEast(object)
	ReplaceExistingObject(object, IsoDirections.E, nil, nil, 1)
end

local function MetalBarSouth(object)
	ReplaceExistingObject(object, IsoDirections.S, nil, nil, 1)
end

-- -- -- -- --

local PRIORITY = 5

MapObjects.OnNewWithSprite("carpentry_01_8", PlankWest1, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_01_10", PlankWest2, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_01_12", PlankWest3, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_01_14", PlankWest4, PRIORITY)

MapObjects.OnNewWithSprite("carpentry_01_9", PlankNorth1, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_01_11", PlankNorth2, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_01_13", PlankNorth3, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_01_15", PlankNorth4, PRIORITY)

MapObjects.OnNewWithSprite("carpentry_01_0", PlankEast1, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_01_2", PlankEast2, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_01_4", PlankEast3, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_01_6", PlankEast4, PRIORITY)

MapObjects.OnNewWithSprite("carpentry_01_1", PlankSouth1, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_01_3", PlankSouth2, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_01_5", PlankSouth3, PRIORITY)
MapObjects.OnNewWithSprite("carpentry_01_7", PlankSouth4, PRIORITY)

-- -- -- -- --

-- Metal sheets have damaged/undamaged sprites.
MapObjects.OnNewWithSprite("constructedobjects_01_24", MetalSheetWest, PRIORITY)
MapObjects.OnNewWithSprite("constructedobjects_01_26", MetalSheetWest, PRIORITY)
MapObjects.OnNewWithSprite("constructedobjects_01_25", MetalSheetNorth, PRIORITY)
MapObjects.OnNewWithSprite("constructedobjects_01_27", MetalSheetNorth, PRIORITY)
MapObjects.OnNewWithSprite("constructedobjects_01_28", MetalSheetEast, PRIORITY)
MapObjects.OnNewWithSprite("constructedobjects_01_30", MetalSheetEast, PRIORITY)
MapObjects.OnNewWithSprite("constructedobjects_01_29", MetalSheetSouth, PRIORITY)
MapObjects.OnNewWithSprite("constructedobjects_01_31", MetalSheetSouth, PRIORITY)

-- -- -- -- --

MapObjects.OnNewWithSprite("constructedobjects_01_55", MetalBarWest, PRIORITY)
MapObjects.OnNewWithSprite("constructedobjects_01_53", MetalBarNorth, PRIORITY)
MapObjects.OnNewWithSprite("constructedobjects_01_52", MetalBarEast, PRIORITY)
MapObjects.OnNewWithSprite("constructedobjects_01_54", MetalBarSouth, PRIORITY)

