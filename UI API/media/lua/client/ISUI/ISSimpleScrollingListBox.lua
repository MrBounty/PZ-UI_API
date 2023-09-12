require "ISUI/ISScrollingListBox"

ISSimpleScrollingListBox = ISScrollingListBox:derive("ISSimpleScrollingListBox");

function ISSimpleScrollingListBox:setPositionAndSize()
    self.pxlW = self.parentUI.elemW[self.line][self.column];
    self.pxlX = self.parentUI.elemX[self.line][self.column];
    self.pxlH = self.parentUI.elemH[self.line];

    self:setX(self.pxlX);
    self:setY(self.pxlY);
    self:setWidth(self.pxlW);
    self:setHeight(self.pxlH);
    self:setFont(UIFont.Small, 2);

    if self.simpleItems[1] ~= nil then
        for name, item in ipairs(self.simpleItems) do
            self:addItem(item, item);
        end
    else
        for name, item in pairs(self.simpleItems) do
            self:addItem(name, item);
        end
    end
end

function ISSimpleScrollingListBox:render()
    ISScrollingListBox.render(self)
    if self.border then
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.5, 1, 1, 1);
    end
end

function ISSimpleScrollingListBox:doDrawItem(y, item, alt)
	if not item.height then item.height = self.itemheight end -- compatibililty
    if y < -self:getYScroll() then return y + item.height; end -- Not draw if out of box
    if y > self:getHeight()-self:getYScroll() then return y + item.height; end -- Not draw if out of box

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15);
    end
	self:drawRectBorder(0, (y), self:getWidth(), item.height, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	local itemPadY = self.itemPadY or (item.height - self.fontHgt) / 2
	self:drawText(item.text, 15, (y)+itemPadY, 0.9, 0.9, 0.9, 0.9, self.font);
	y = y + item.height;
	return y;

end

function ISSimpleScrollingListBox:new(parentUI, simpleItems, deselectOnClick)
    local o = {};
    o = ISScrollingListBox:new(0, 0, 1, 1);
    setmetatable(o, self);
    self.__index = self;

    o.parentUI = parentUI;
    o.line = line;
    o.column = column;
    o.border = false;

    -- Parent and position
    o.parentUI = parentUI;
    o.line = parentUI.lineAct;
    o.column = parentUI.columnAct;
    o.pxlY = parentUI.yAct;
    o.simpleItems = simpleItems;
    o.deselectOnClick = deselectOnClick or false
    return o;
end

-- Commun function

function ISSimpleScrollingListBox:setBorder(v)
    self.border = v;
end

-- Simple element function
function ISSimpleScrollingListBox:getValue()
    return self.items[self.selected].text, self.items[self.selected].item;
end

function ISSimpleScrollingListBox:setitems(v)
    self:clear();
    self.simpleItems = v;
    self.selected = -1
    for index, value in ipairs(self.simpleItems) do
        self:addItem(value);
    end
end

function ISSimpleScrollingListBox:setWidthPercent(w)
    self.isWidthForce = true;
    self.pxlW = w * getCore():getScreenWidth();
end

function ISSimpleScrollingListBox:setWidthPixel(w)
    self.isWidthForce = true;
    self.pxlW = w;
end

--FIX FOR BUG: when clicking in the list area with no entries, the script would select the first entry.
function ISScrollingListBox.onMouseDown(self, x, y)
	if #self.items == 0 then return end
	local row = self:rowAt(x, y)
    
    local playUiSound = true

	if row > #self.items then
		row = #self.items;
	end

    if row > -1 and self.selected > -1 and self.deselectOnClick and row == self.selected then
        self.selected = -1
        playUiSound = false
    elseif row < 1 then
		row = self.selected;
        playUiSound = false
	end

    if playUiSound then
	    getSoundManager():playUISound("UISelectListItem")
    end

	self.selected = row;

	if self.onmousedown then
		self.onmousedown(self.target, self.items[self.selected].item);
	end
end