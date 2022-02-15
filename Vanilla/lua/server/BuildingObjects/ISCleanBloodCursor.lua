--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

ISCleanBloodCursor = ISBuildingObject:derive("ISCleanBloodCursor")

function ISCleanBloodCursor:create(x, y, z, north, sprite)
	local square = getWorld():getCell():getGridSquare(x, y, z)
	ISWorldObjectContextMenu.doCleanBlood(self.character, square)
end

function ISCleanBloodCursor:isValid(square)
	return ISWorldObjectContextMenu.canCleanBlood(self.character, square)
end

function ISCleanBloodCursor:render(x, y, z, square)
	if not ISCleanBloodCursor.floorSprite then
		ISCleanBloodCursor.floorSprite = IsoSprite.new()
		ISCleanBloodCursor.floorSprite:LoadFramesNoDirPageSimple('media/ui/FloorTileCursor.png')
	end
	local r,g,b,a = 0.0,1.0,0.0,0.8
	if self:isValid(square) == false then
		r = 1.0
		g = 0.0
	end
	ISCleanBloodCursor.floorSprite:RenderGhostTileColor(x, y, z, r, g, b, a)
end

function ISCleanBloodCursor:new(sprite, northSprite, character)
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

