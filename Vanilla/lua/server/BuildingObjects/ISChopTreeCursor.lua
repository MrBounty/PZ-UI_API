--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

ISChopTreeCursor = ISBuildingObject:derive("ISChopTreeCursor")

function ISChopTreeCursor:create(x, y, z, north, sprite)
	local square = getWorld():getCell():getGridSquare(x, y, z)
	ISWorldObjectContextMenu.doChopTree(self.character, square:getTree())
end

function ISChopTreeCursor:isValid(square)
	return square:HasTree()
end

function ISChopTreeCursor:render(x, y, z, square)
	if not ISChopTreeCursor.floorSprite then
		ISChopTreeCursor.floorSprite = IsoSprite.new()
		ISChopTreeCursor.floorSprite:LoadFramesNoDirPageSimple('media/ui/FloorTileCursor.png')
	end
	local r,g,b,a = 0.0,1.0,0.0,0.8
	if self:isValid(square) then
		square:getTree():setHighlighted(true)
	else
		r = 1.0
		g = 0.0
	end
	ISChopTreeCursor.floorSprite:RenderGhostTileColor(x, y, z, r, g, b, a)
	IsoTree.setChopTreeCursorLocation(self.player, x, y, z)
end

function ISChopTreeCursor:new(sprite, northSprite, character)
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

