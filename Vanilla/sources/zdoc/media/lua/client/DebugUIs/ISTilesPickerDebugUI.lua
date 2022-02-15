--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "ISUI/ISPanelJoypad"

---@class ISTilesPickerDebugUI : ISCollapsableWindow
ISTilesPickerDebugUI = ISCollapsableWindow:derive("ISTilesPickerDebugUI");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

-----

ISTilesPickerTilesList = ISPanel:derive("ISTilesPickerTilesList")

function ISTilesPickerTilesList:render()
	ISPanel.render(self)

	self:setStencilRect(0, 0, self.width - self.vscroll.width, self.height)

	local xIndent = 10
	local yIndent = 10
	local texW = 64
	local texH = 128

	local maxRow = 1
	for row=1,32 do
		for col=1,8 do
			local tileName = string.format("%s_%d", self.tileset, (col - 1) + (row - 1) * 8)
			local texture = getTexture(tileName)
			if texture then
				self:drawTextureScaledAspect(texture, xIndent + (col - 1) * texW, yIndent + (row - 1) * texH, texW, texH, 1.0, 1.0, 1.0, 1.0)
				maxRow = math.max(maxRow, row)
			end
		end
	end

	self:setScrollHeight(yIndent + maxRow * texH + yIndent)
	self.vscroll:setX(self.width - self.vscroll.width)
--[[
	local tileNames = getWorld():getAllTiles(self.tileset)
	if tileNames == nil then return end
	local col = 1
	local row = 1
	for i=1,tileNames:size() do
		local tileName = tileNames:get(i-1)
		local texture = getTexture(tileName)
		if texture then
			self:drawTextureScaledAspect(texture, 10 + (col - 1) * 64, 10 + (row - 1) * 128, 64, 128, 1.0, 1.0, 1.0, 1.0)
			col = col + 1
			if col == 9 then
				col = 1
				row = row + 1
			end
		end
	end

	self:setScrollHeight(10 + row * 128 + 10)
	self.vscroll:setX(self.width - self.vscroll.width)
--]]

	col = math.floor((self:getMouseX() - xIndent) / texW)
	row = math.floor((self:getMouseY() - yIndent) / texH)
	if col >= 0 and col < 8 then
		self:drawRectBorder(xIndent + col * texW, yIndent + row * texH, texW, texH, 0.5, 1, 1, 1)
	end

	self:clearStencilRect()
end

function ISTilesPickerTilesList:onMouseWheel(del)
	self:setYScroll(self:getYScroll() - (del * 128));
    return true;
end

function ISTilesPickerTilesList:new(x, y, w, h)
	local o = ISPanel.new(self, x, y, w, h)
	o.backgroundColor.a = 1.0
	o.tileset = nil
	return o
end

-----

function ISTilesPickerDebugUI:createChildren()
	local btnWid = 100
	local btnHgt = 25
	local padBottom = 0
	
	ISCollapsableWindow.createChildren(self)

	local th = self:titleBarHeight()
	local rh = self:resizeWidgetHeight()

	self.filesList = ISScrollingListBox:new(0, th, 200, self.height - rh - th);
	self.filesList.anchorBottom = true;
	self.filesList:initialise();
	self.filesList:instantiate();
	self.filesList.itemheight = FONT_HGT_SMALL + 2;
	self.filesList.selected = 0;
	self.filesList.joypadParent = self;
	self.filesList.font = UIFont.NewSmall;
	self.filesList.doDrawItem = self.drawTilesetList;
	self.filesList.drawBorder = true;
	self:addChild(self.filesList);

	self.tilesList = ISTilesPickerTilesList:new(self.filesList:getRight(), th, self.width - self.filesList:getRight(), self.height - rh - th)
	self.tilesList.anchorRight = true;
	self.tilesList.anchorBottom = true;
	self.tilesList:initialise();
	self.tilesList:instantiate();
	self.tilesList:addScrollBars();
	self:addChild(self.tilesList);
	
--	self.close = ISButton:new(self.width - btnWid - 10, self.add.y, btnWid, btnHgt, "Close", self, ISSpawnHordeUI.close);
--	self.close.anchorTop = false
--	self.close.anchorBottom = true
--	self.close:initialise();
--	self.close:instantiate();
--	self.close.borderColor = {r=1, g=1, b=1, a=0.1};
--	self:addChild(self.close);

	self.resizeWidget2:bringToTop()
	self.resizeWidget:bringToTop()
	
	self:populateList();
end

function ISTilesPickerDebugUI:populateList()
	local maxWidth = 0
	self.filesList:clear();
	local allTilesName = getWorld():getAllTilesName();
	for i=0,allTilesName:size()-1 do
		local name = allTilesName:get(i)
		self.filesList:addItem(name, name);
		local width = getTextManager():MeasureStringX(self.filesList.font, name)
		maxWidth = math.max(maxWidth, width)
	end
	self.filesList:setWidth(10 + maxWidth + 20)
	self.tilesList:setX(self.filesList:getRight())
	self.tilesList:setWidth(self.width - self.tilesList:getX())
end

function ISTilesPickerDebugUI:drawTilesetList(y, item, alt)
	local a = 0.9;
	
	--    self.parent.selectedStash = nil;
	self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	
	if self.selected == item.index then
		self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
	end
	
	self:drawText( item.text, 10, y + (self.itemheight - FONT_HGT_SMALL) / 2, 1, 1, 1, a, self.font);
	
	return y + self.itemheight;
end

function ISTilesPickerDebugUI:onSelectNewSquare()
	self.cursor = ISSelectCursor:new(self.chr, self, self.onSquareSelected)
	getCell():setDrag(self.cursor, self.chr:getPlayerNum())
end

function ISTilesPickerDebugUI:onSquareSelected(square)
	self.cursor = nil;
	self:removeMarker();
	self.selectX = square:getX();
	self.selectY = square:getY();
	self.selectZ = square:getZ();
	self:addMarker(square, self:getRadius() + 1);
end

function ISTilesPickerDebugUI:prerender()
	self.tilesList.tileset = self.filesList.items[self.filesList.selected].item
	ISCollapsableWindow.prerender(self);
--	local radius = (self:getRadius() + 1);
--	if self.marker and (self.marker:getSize() ~= radius) then
--		self.marker:setSize(radius)
--	end
end

function ISTilesPickerDebugUI:render()
	ISCollapsableWindow.render(self);
	
--	self:drawText("Picked Square: " .. self.selectX .. "," .. self.selectY .. "," .. self.selectZ, 10, 25, 1, 1, 1, 1, self.font);
end

function ISTilesPickerDebugUI:addMarker(square, radius)
	self.marker = getWorldMarkers():addGridSquareMarker(square, 0.8, 0.8, 0.0, true, radius);
	self.marker:setScaleCircleTexture(true);
	local texName = nil; -- use default
	self.arrow = getWorldMarkers():addDirectionArrow(self.chr, self.selectX, self.selectY, self.selectZ, texName, 1.0, 1.0, 1.0, 1.0);
end

function ISTilesPickerDebugUI:removeMarker()
	if self.marker then
		self.marker:remove();
		self.marker = nil;
	end
	if self.arrow then
		self.arrow:remove();
		self.arrow = nil;
	end
end

function ISTilesPickerDebugUI:close()
	self:removeMarker();
	self:setVisible(false);
	self:removeFromUIManager();
end

--************************************************************************--
--** ISSpawnHordeUI:new
--**
--************************************************************************--
function ISTilesPickerDebugUI:new(x, y, character, square)
	local o = {}
	local width = 800;
	local height = math.min(1250, getCore():getScreenHeight() - 40);
	o = ISCollapsableWindow:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self
	o.playerNum = character:getPlayerNum()
	if y == 0 then
		o.y = getPlayerScreenTop(o.playerNum) + (getPlayerScreenHeight(o.playerNum) - height) / 2
		o:setY(o.y)
	end
	if x == 0 then
		o.x = getPlayerScreenLeft(o.playerNum) + (getPlayerScreenWidth(o.playerNum) - width) / 2
		o:setX(o.x)
	end
	o.width = width;
	o.height = height;
	o.chr = character;
	o.moveWithMouse = true;
	o.anchorLeft = true;
	o.anchorRight = true;
	o.anchorTop = true;
	o.anchorBottom = true;
--	o:addMarker(square, 1);
	return o;
end
