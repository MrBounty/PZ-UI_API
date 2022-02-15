--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

ISWalkToCursor = ISBuildingObject:derive("ISWalkToCursor")


function ISWalkToCursor:create(x, y, z, north, sprite)
	local square = getWorld():getCell():getGridSquare(x, y, z)
	ISTimedActionQueue.clear(self.character)
	ISTimedActionQueue.add(ISWalkToTimedAction:new(self.character, square))
end

function ISWalkToCursor:isValid(square)
	return square:TreatAsSolidFloor()
end

function ISWalkToCursor:render(x, y, z, square)
	if not ISWalkToCursor.floorSprite then
		ISWalkToCursor.floorSprite = IsoSprite.new()
		ISWalkToCursor.floorSprite:LoadFramesNoDirPageSimple('media/ui/FloorTileCursor.png')
	end
	local r,g,b,a = 0.0,1.0,0.0,0.8
	if not self:isValid(square) then
		r = 1.0
		g = 0.0
	end
	ISWalkToCursor.floorSprite:RenderGhostTileColor(x, y, z, r, g, b, a)
end

function ISWalkToCursor:new(sprite, northSprite, character)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o:init()
	o:setSprite(sprite)
	o:setNorthSprite(northSprite)
	o.character = character
	o.player = character:getPlayerNum()
	o.noNeedHammer = true
	o.skipBuildAction = true
	return o
end

