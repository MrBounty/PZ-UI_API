require "ISUI/ISPanelJoypad"
require "ISUI/ISButton"
require "ISUI/ISMouseDrag"
require "TimedActions/ISTimedActionQueue"
require "TimedActions/ISEatFoodAction"



ISScrollingListBox = ISPanelJoypad:derive("ISScrollingListBox");
ISScrollingListBox.joypadListIndex = 1;

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

--
-- Created by IntelliJ IDEA.
-- User: LEMMY
-- Date: 10/10/12
-- Time: 03:48
-- To change this template use File | Settings | File Templates.
--

--

--************************************************************************--
--** ISInventoryPane:initialise
--**
--************************************************************************--

function ISScrollingListBox:initialise()
    ISPanelJoypad.initialise(self);
end

function ISScrollingListBox:setJoypadFocused(focused, joypadData)
    if focused then
        joypadData.focus = self;
        updateJoypadFocus(joypadData);
        if self.selected == -1 then
            self.selected = 1;
            if self.resetSelectionOnChangeFocus then
                if self.items[self.selectedBeforeReset] then
                    self.selected = self.selectedBeforeReset
                end
                self.selectedBeforeReset = nil
            end
            if self.onmousedown and self.items[self.selected] then
                self.onmousedown(self.target, self.items[self.selected].item);
            end
        end
    end
    self.joypadFocused = focused;
end

function ISScrollingListBox:onJoypadDirRight(joypadData)
    if self.joypadParent then
        self.joypadParent:onJoypadDirRight(joypadData);
    end
end

function ISScrollingListBox:onJoypadDirLeft(joypadData)
    if self.joypadParent then
        self.joypadParent:onJoypadDirLeft(joypadData);
    end
end

--************************************************************************--
--** ISPanel:instantiate
--**
--************************************************************************--
function ISScrollingListBox:instantiate()

	--self:initialise();
	self.javaObject = UIElement.new(self);
	self.javaObject:setX(self.x);
	self.javaObject:setY(self.y);
	self.javaObject:setHeight(self.height);
	self.javaObject:setWidth(self.width);
	self.javaObject:setAnchorLeft(self.anchorLeft);
	self.javaObject:setAnchorRight(self.anchorRight);
	self.javaObject:setAnchorTop(self.anchorTop);
	self.javaObject:setAnchorBottom(self.anchorBottom);
	self:addScrollBars();
end

function ISScrollingListBox:rowAt(x, y)
	local y0 = 0
	for i,v in ipairs(self.items) do
		if not v.height then v.height = self.itemheight end -- compatibililty
		if y >= y0 and y < y0 + v.height then
			return i
		end
		y0 = y0 + v.height
	end
	return -1
end

function ISScrollingListBox:topOfItem(index)
	local y = 0
	for k,v in ipairs(self.items) do
		if k == index then
			return y
		end
		y = y + v.height
	end
	return -1
end

function ISScrollingListBox:prevVisibleIndex(index)
	if index <= 1 then return -1 end
	for i=index-1,1,-1 do
		local item = self.items[i]
		if item and item.height and item.height > 0 then
			return i
		end
	end
	return -1
end

function ISScrollingListBox:nextVisibleItem(index)
	if index >= #self.items then return -1 end
	for i=index+1,#self.items do
		if self.items[i] and self.items[i].height and self.items[i].height > 0 then
			return i
		end
	end
	return -1
end

function ISScrollingListBox:isMouseOverScrollBar()
	return self:isVScrollBarVisible() and self.vscroll:isMouseOver()
end

function ISScrollingListBox:onMouseMove(dx, dy)
	if self:isMouseOverScrollBar() then return end
	self.mouseoverselected = self:rowAt(self:getMouseX(), self:getMouseY())
end

function ISScrollingListBox:onMouseMoveOutside(x, y)
	self.mouseoverselected = -1;
end

function ISScrollingListBox:onMouseUpOutside(x, y)
	if self.vscroll then
		self.vscroll.scrolling = false;
	end
end

function ISScrollingListBox:onMouseUp(x, y)
	if self.vscroll then
		self.vscroll.scrolling = false;
	end
end

function ISScrollingListBox:addItem(name, item)
    local i = {}
    i.text=name;
    i.item=item;
	i.tooltip = nil;
    i.itemindex = self.count + 1;
	i.height = self.itemheight
    table.insert(self.items, i);
    self.count = self.count + 1;
    self:setScrollHeight(self:getScrollHeight()+i.height);
    return i;
end

function ISScrollingListBox:insertItem(index, name, item)
	local i = {}
	i.text = name
	i.item = item
	i.tooltip = nil
	i.height = self.itemheight
	if #self.items == 0 or index > #self.items then
		i.itemindex = 1
		table.insert(self.items, i)
	elseif index < 1 then
		i.itemindex = 1
		table.insert(self.items, 1, i)
	else
		i.itemindex = index
		table.insert(self.items, index, i)
	end
	self.count = self.count + 1
	self:setScrollHeight(self:getScrollHeight() + i.height)
	return i
end

function ISScrollingListBox:removeItem(itemText)
	for i,v in ipairs(self.items) do
		if v.text == itemText then
			table.remove(self.items, i);
			self.count = self.count - 1;
			if not v.height then v.height = self.itemheight end -- compatibililty
			self:setScrollHeight(self:getScrollHeight()-v.height);
			if self.selected > self.count then
				self.selected = self.count
			end
            return v;
		end
	end
    return nil;
end

function ISScrollingListBox:removeItemByIndex(itemIndex)
	if itemIndex >= 1 and itemIndex <= #self.items then
		local item = self.items[itemIndex]
		table.remove(self.items, itemIndex)
		self.count = self.count - 1
		if not item.height then item.height = self.itemheight end -- compatibililty
		self:setScrollHeight(self:getScrollHeight() - item.height)
		if self.selected > self.count then
			self.selected = self.count
		end
		return item
	end
	return nil
end


function ISScrollingListBox:removeFirst()
    if self.count == 0 then return end
    local item = self.items[1]
    table.remove(self.items, 0);
    self.count = self.count - 1;
	if not item.height then item.height = self.itemheight end -- compatibililty
    self:setScrollHeight(self:getScrollHeight()-item.height);
end

function ISScrollingListBox:size()
    return self.count;
end

function ISScrollingListBox:setOnMouseDownFunction(target, onmousedown)
	self.onmousedown = onmousedown;
	self.target = target;
end

function ISScrollingListBox:setOnMouseDoubleClick(target, onmousedblclick)
	self.onmousedblclick = onmousedblclick;
	self.target = target;
end

function ISScrollingListBox:doDrawItem(y, item, alt)
	if not item.height then item.height = self.itemheight end -- compatibililty
    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15);

    end
	self:drawRectBorder(0, (y), self:getWidth(), item.height, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	local itemPadY = self.itemPadY or (item.height - self.fontHgt) / 2
	self:drawText(item.text, 15, (y)+itemPadY, 0.9, 0.9, 0.9, 0.9, self.font);
	y = y + item.height;
	return y;

end
function ISScrollingListBox:clear()
	self.items = {}
	self.selected = 1;
	self.itemheightoverride = {}
    self.count = 0;
end

function ISScrollingListBox:onMouseWheel(del)
	local yScroll = self.smoothScrollTargetY or self:getYScroll()
	local topRow = self:rowAt(0, -yScroll)
	if self.items[topRow] then
		if not self.smoothScrollTargetY then self.smoothScrollY = self:getYScroll() end
		local y = self:topOfItem(topRow)
		if del < 0 then
			if yScroll == -y and topRow > 1 then
				local prev = self:prevVisibleIndex(topRow)
				y = self:topOfItem(prev)
			end
			self.smoothScrollTargetY = -y;
		else
			self.smoothScrollTargetY = -(y + self.items[topRow].height);
		end
	else
		self:setYScroll(self:getYScroll() - (del*18));
	end
    return true;
end

function ISScrollingListBox:scrollToSelected()

end

function ISScrollingListBox.sortByName(a, b)
    return not string.sort(a.text, b.text);

end
function ISScrollingListBox:sort()
    table.sort(self.items, ISScrollingListBox.sortByName);
    for i,item in ipairs(self.items) do
        item.itemindex = i;
    end
end

function ISScrollingListBox:updateTooltip()
	local row = -1
	local lx = getMouseX() - self:getAbsoluteX()
	local ly = getMouseY() - self:getAbsoluteY()
	local sbarWid = 0
	if self.vscroll and self.vscroll:getHeight() < self:getScrollHeight() then
		sbarWid = self.vscroll:getWidth()
	end
	if lx >= 0 and lx < self.width - sbarWid and ly >= 0 and ly < self.height then
		row = self:rowAt(self:getMouseX(), self:getMouseY())
		-- Hack - don't show tooltip if another window is in front
		local root = self.parent or self
		while root.parent do
			root = root.parent
		end
		local uis = UIManager.getUI()
		for i=1,uis:size() do
			local ui = uis:get(i-1)
			if ui:isMouseOver() and (not self.tooltipUI or ui ~= self.tooltipUI.javaObject) and ui ~= root.javaObject then
				row = -1
				break
			end
		end
	end
	if self.items[row] and self.items[row].tooltip then
		local text = self.items[row].tooltip
		if not self.tooltipUI then
			self.tooltipUI = ISToolTip:new()
			self.tooltipUI:setOwner(self)
			self.tooltipUI:setVisible(false)
			self.tooltipUI:setAlwaysOnTop(true)
			self.tooltipUI.maxLineWidth = 1000 -- don't wrap the lines
		end
		if not self.tooltipUI:getIsVisible() then
			self.tooltipUI:addToUIManager()
			self.tooltipUI:setVisible(true)
		end
		self.tooltipUI.description = text
		self.tooltipUI:setX(self:getMouseX() + 23)
		self.tooltipUI:setY(self:getMouseY() + 23)
	else
		if self.tooltipUI and self.tooltipUI:getIsVisible() then
			self.tooltipUI:setVisible(false)
			self.tooltipUI:removeFromUIManager()
		end
    end
end

function ISScrollingListBox:updateSmoothScrolling()
	if not self.smoothScrollTargetY or #self.items == 0 then return end
	local dy = self.smoothScrollTargetY - self.smoothScrollY
	local maxYScroll = self:getScrollHeight() - self:getHeight()
	local frameRateFrac = UIManager.getMillisSinceLastRender() / 33.3
	local itemHeightFrac = 160 / (self:getScrollHeight() / #self.items)
	local targetY = self.smoothScrollY + dy * math.min(0.5, 0.25 * frameRateFrac * itemHeightFrac)
	if frameRateFrac > 1 then
		targetY = self.smoothScrollY + dy * math.min(1.0, math.min(0.5, 0.25 * frameRateFrac * itemHeightFrac) * frameRateFrac)
	end
	if targetY > 0 then targetY = 0 end
	if targetY < -maxYScroll then targetY = -maxYScroll end
	if math.abs(targetY - self.smoothScrollY) > 0.1 then
		self:setYScroll(targetY)
		self.smoothScrollY = targetY
	else
		self:setYScroll(self.smoothScrollTargetY)
		self.smoothScrollTargetY = nil
		self.smoothScrollY = nil
	end
end

function ISScrollingListBox:prerender()
	if self.items == nil then
		return;
	end

	local stencilX = 0
	local stencilY = 0
	local stencilX2 = self.width
	local stencilY2 = self.height

    self:drawRect(0, -self:getYScroll(), self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
	if self.drawBorder then
		self:drawRectBorder(0, -self:getYScroll(), self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b)
		stencilX = 1
		stencilY = 1
		stencilX2 = self.width - 1
		stencilY2 = self.height - 1
	end

	if self:isVScrollBarVisible() then
		stencilX2 = self.vscroll.x + 3 -- +3 because the scrollbar texture is narrower than the scrollbar width
	end

	-- This is to handle this listbox being inside a scrolling parent.
	if self.parent and self.parent:getScrollChildren() then
		stencilX = self.javaObject:clampToParentX(self:getAbsoluteX() + stencilX) - self:getAbsoluteX()
		stencilX2 = self.javaObject:clampToParentX(self:getAbsoluteX() + stencilX2) - self:getAbsoluteX()
		stencilY = self.javaObject:clampToParentY(self:getAbsoluteY() + stencilY) - self:getAbsoluteY()
		stencilY2 = self.javaObject:clampToParentY(self:getAbsoluteY() + stencilY2) - self:getAbsoluteY()
	end
	self:setStencilRect(stencilX, stencilY, stencilX2 - stencilX, stencilY2 - stencilY)

	local y = 0;
	local alt = false;

--	if self.selected ~= -1 and self.selected < 1 then
--		self.selected = 1
	if self.selected ~= -1 and self.selected > #self.items then
		self.selected = #self.items
	end

	local altBg = self.altBgColor

	self.listHeight = 0;
	 local i = 1;
	 for k, v in ipairs(self.items) do
		if not v.height then v.height = self.itemheight end -- compatibililty

		 if alt and altBg then
			self:drawRect(0, y, self:getWidth(), v.height-1, altBg.r, altBg.g, altBg.b, altBg.a);
		 else

		 end
		 v.index = i;
		 local y2 = self:doDrawItem(y, v, alt);
		 self.listHeight = y2;
		 v.height = y2 - y
		 y = y2

		 alt = not alt;
		 i = i + 1;
		
	 end

	self:setScrollHeight((y));
	self:clearStencilRect();
	if self.doRepaintStencil then
		self:repaintStencilRect(stencilX, stencilY, stencilX2 - stencilX, stencilY2 - stencilY)
	end

	local mouseY = self:getMouseY()
	self:updateSmoothScrolling()
	if mouseY ~= self:getMouseY() and self:isMouseOver() then
		self:onMouseMove(0, self:getMouseY() - mouseY)
	end
	self:updateTooltip()
	
	if #self.columns > 0 then
--		print(self:getScrollHeight())
		self:drawRectBorderStatic(0, 0 - self.itemheight, self.width, self.itemheight - 1, 1, self.borderColor.r, self.borderColor.g, self.borderColor.b);
		self:drawRectStatic(0, 0 - self.itemheight - 1, self.width, self.itemheight-2,self.listHeaderColor.a,self.listHeaderColor.r, self.listHeaderColor.g, self.listHeaderColor.b);
		local dyText = (self.itemheight - FONT_HGT_SMALL) / 2
		for i,v in ipairs(self.columns) do
			self:drawRectStatic(v.size, 0 - self.itemheight, 1, self.itemheight + math.min(self.height, self.itemheight * #self.items - 1), 1, self.borderColor.r, self.borderColor.g, self.borderColor.b);
			if v.name then
				self:drawText(v.name, v.size + 10, 0 - self.itemheight - 1 + dyText - self:getYScroll(), 1,1,1,1,UIFont.Small);
			end
		end
	end
end

function ISScrollingListBox:onMouseDoubleClick(x, y)
	if self.onmousedblclick and self.items[self.selected] ~= nil then
		self.onmousedblclick(self.target, self.items[self.selected].item);
	end
end


function ISScrollingListBox:onMouseDown(x, y)
	if #self.items == 0 then return end
	local row = self:rowAt(x, y)

	if row > #self.items then
		row = #self.items;
	end
	if row < 1 then
		row = 1;
	end

	-- RJ: If you select the same item it unselect it
	--if self.selected == y then
	--if self.selected == y then
		--self.selected = -1;
		--return;
	--end

	getSoundManager():playUISound("UISelectListItem")

	self.selected = row;

	if self.onmousedown then
		self.onmousedown(self.target, self.items[self.selected].item);
	end
end


function ISScrollingListBox:onJoypadDirUp()
    self.selected = self.selected - 1

    if self.selected <= 0 then
        self.selected = self.count;
    end

    getSoundManager():playUISound("UISelectListItem")

    self:ensureVisible(self.selected)

	if self.onmousedown and self.items[self.selected] then
		self.onmousedown(self.target, self.items[self.selected].item);
	end
end

function ISScrollingListBox:onJoypadDirDown()
        self.selected = self.selected + 1
        if self.selected > self.count then
            self.selected = 1;
        end

    getSoundManager():playUISound("UISelectListItem")

    self:ensureVisible(self.selected)

	if self.onmousedown and self.items[self.selected] then
		self.onmousedown(self.target, self.items[self.selected].item);
	end
end

function ISScrollingListBox:ensureVisible(index)
    if not index or index < 1 or index > #self.items then return end
    local y = 0
    local height = 0
    for k, v in ipairs(self.items) do
		if k == index then
			height = v.height
			break
		end
		y = y + v.height
	end
--	print('y='..y..' top='..self:getYScroll()..' bottom='..(self:getYScroll() + self.height))
	if not self.smoothScrollTargetY then self.smoothScrollY = self:getYScroll() end
	if y < 0-self:getYScroll() then
		self.smoothScrollTargetY = 0 - y
	elseif y + height > 0 - self:getYScroll() + self.height then
		self.smoothScrollTargetY = 0 - (y + height - self.height)
	end
end

function ISScrollingListBox:render()
    if self.joypadFocused then
        self:drawRectBorder(0, -self:getYScroll(), self:getWidth(), self:getHeight(), 0.4, 0.2, 1.0, 1.0);
        self:drawRectBorder(1, 1-self:getYScroll(), self:getWidth()-2, self:getHeight()-2, 0.4, 0.2, 1.0, 1.0);
    end
end

function ISScrollingListBox:onJoypadDown(button, joypadData)
    if button == Joypad.AButton and self.onmousedblclick then
		if (#self.items > 0) and (self.selected ~= -1) then
			local previousSelected = self.selected;
			self.onmousedblclick(self.target, self.items[self.selected].item);
			self.selected = previousSelected;
		end
    elseif button == Joypad.BButton and self.joypadParent then
        self.joypadFocused = false;
        joypadData.focus = self.joypadParent;
        updateJoypadFocus(joypadData);
    else
        ISPanelJoypad.onJoypadDown(self, button);
    end
end

function ISScrollingListBox:onLoseJoypadFocus(joypadData)
    ISPanelJoypad.onLoseJoypadFocus(self, joypadData)
    if self.resetSelectionOnChangeFocus then
        self.selectedBeforeReset = self.selected
        self.selected = -1;
    end
end

function ISScrollingListBox:setFont(font, padY)
    self.font = UIFont[font] or font
    self.fontHgt = getTextManager():getFontFromEnum(self.font):getLineHeight()
    self.itemPadY = padY
    self.itemheight = self.fontHgt + (self.itemPadY or 0) * 2;
end

function ISScrollingListBox:addColumn(columnName, size)
	table.insert(self.columns, {name = columnName, size = size});
end

	--************************************************************************--
--** ISInventoryPane:new
--**
--************************************************************************--
function ISScrollingListBox:new (x, y, width, height)
	local o = {}
	--o.data = {}
	o = ISPanelJoypad:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self
	o.x = x;
	o.y = y;
	o:noBackground();
	o.backgroundColor = {r=0, g=0, b=0, a=0.8};
	o.borderColor = {r=0.4, g=0.4, b=0.4, a=0.9};
	o.altBgColor = {r=0.2, g=0.3, b=0.2, a=0.1 }
	o.listHeaderColor = {r=0.4, g=0.4, b=0.4, a=0.3};
	-- Since these were broken before, don't draw them by default
	o.altBgColor = nil
	o.drawBorder = false
	o.width = width;
	o.height = height;
	o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;
	o.font = UIFont.Large
	o.fontHgt = getTextManager():getFontFromEnum(o.font):getLineHeight()
	o.itemPadY = 7
	o.itemheight = o.fontHgt + o.itemPadY * 2;
	o.selected = 1;
    o.count = 0;
	o.itemheightoverride = {}
	o.items = {}
	o.columns = {};
	return o
end

