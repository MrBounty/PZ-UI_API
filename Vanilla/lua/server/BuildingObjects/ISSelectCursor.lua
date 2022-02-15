ISSelectCursor = ISBuildingObject:derive("ISSelectCursor")

function ISSelectCursor:create(x, y, z, north, sprite)
	getCell():setDrag(nil, self.player)
	self.ui:onSquareSelected(getWorld():getCell():getGridSquare(x, y, z))
end

function ISSelectCursor:isValid(square)
	return self.ui.cursor ~= nil;
end

function ISSelectCursor:render(x, y, z, square)
	if not ISSelectCursor.floorSprite then
		ISSelectCursor.floorSprite = IsoSprite.new()
		ISSelectCursor.floorSprite:LoadFramesNoDirPageSimple('media/ui/FloorTileCursor.png')
	end
	local r,g,b,a = 0.0,1.0,0.0,0.8
	if not self:isValid(square) then
		r = 1.0
		g = 0.0
	end
	ISSelectCursor.floorSprite:RenderGhostTileColor(x, y, z, r, g, b, a)
end

function ISSelectCursor:new(character, ui, onSquareSelected)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o:init()
	o.ui = ui;
	o.onSquareSelected = onSquareSelected;
	o.character = character
	o.player = character:getPlayerNum()
	o.noNeedHammer = true
	o.skipBuildAction = true
	return o
end
