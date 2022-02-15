ISFarmingCursorMouse = ISBuildingObject:derive("ISFarmingCursorMouse")

function ISFarmingCursorMouse:create(x, y, z, north, sprite)
--	getCell():setDrag(nil, self.player)
	self:hideTooltip();
	self:onSquareSelected(getWorld():getCell():getGridSquare(x, y, z))
end

function ISFarmingCursorMouse:render(x, y, z, square)
	if not ISFarmingCursorMouse.floorSprite then
		ISFarmingCursorMouse.floorSprite = IsoSprite.new()
		ISFarmingCursorMouse.floorSprite:LoadFramesNoDirPageSimple('media/ui/FloorTileCursor.png')
	end
	local r,g,b,a = 0.0,1.0,0.0,0.8
	if not self:isValid(square) then
		r = 1.0
		g = 0.0
	end
	self.sq = square;
	ISFarmingCursorMouse.floorSprite:RenderGhostTileColor(x, y, z, r, g, b, a)
	
	self:renderTooltip();
end

-- Called by IsoCell.setDrag()
function ISFarmingCursorMouse:deactivate()
	self:hideTooltip();
end

function ISFarmingCursorMouse:hideTooltip()
	if self.tooltip then
		self.tooltip:removeFromUIManager()
		self.tooltip:setVisible(false)
		self.tooltip = nil
	end
end

function ISFarmingCursorMouse:renderTooltip()
	if not self.tooltipTxt then
		self:hideTooltip();
		return;
	end
	
	if not self.tooltip then
		self.tooltip = ISWorldObjectContextMenu.addToolTip();
		self.tooltip:setVisible(true)
		self.tooltip:addToUIManager()
		self.tooltip.followMouse = not self.joyfocus
		self.tooltip.maxLineWidth = 1000
	else
		self.tooltip.description = self.tooltipTxt;
	end
end

function ISFarmingCursorMouse.IsVisible()
	return ISFarmingMenu.cursor and getCell():getDrag(0) == ISFarmingMenu.cursor
end

function ISFarmingCursorMouse:new(character, onSquareSelected, isValid)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o:init()
	o.onSquareSelected = onSquareSelected;
	o.isValid = isValid;
	o.character = character
	o.player = character:getPlayerNum()
	o.noNeedHammer = true
	o.skipBuildAction = true
	return o
end
