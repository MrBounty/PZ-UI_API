--***********************************************************
--**                LEMMY/ROBERT JOHNSON                   **
--***********************************************************

require "ISUI/ISPanel"

ISContextMenu = ISPanel:derive("ISContextMenu");

--************************************************************************--
--** ISContextMenu:initialise
--**
--************************************************************************--

function ISContextMenu:initialise()
	ISPanel.initialise(self);
end

function ISContextMenu:isMouseOut()
	return self.mouseOut;
end

--************************************************************************--
--** ISContextMenu:onMouseMove
--**
--************************************************************************--
function ISContextMenu:onMouseMove(dx, dy)
	self.mouseOut = false;
	if self:topmostMenuWithMouse(getMouseX(), getMouseY()) ~= self then return end
	local mouseY = self:getMouseY()
	local dy = (self:getScrollHeight() > self:getScrollAreaHeight()) and self.scrollIndicatorHgt or 0
	mouseY = math.max(self.padTopBottom + dy - self:getYScroll(), mouseY)
	mouseY = math.min(self.padTopBottom + dy + self:getScrollAreaHeight() - 1 - self:getYScroll(), mouseY)
	local index = self:getIndexAt(0, mouseY)
	if index ~= -1 then
		if self.subMenu and (index ~= self.mouseOver) then
			self.subMenu:hideSelfAndChildren2()
			self.subMenu = nil
		end
	end
	self.mouseOver = index
end

function ISContextMenu:hideSelfAndChildren2()
	self:setVisible(false)
	self.mouseOver = -1
	for i=1,#self.options do
		if self.options[i].subOption then
			if self:getSubMenu(self.options[i].subOption) then
				self:getSubMenu(self.options[i].subOption):hideSelfAndChildren2()
			end
		end
	end
end

--************************************************************************--
--** ISContextMenu:onMouseMoveOutside
--**
--************************************************************************--
function ISContextMenu:onMouseMoveOutside(dx, dy)
	if self.player == 0 then
		self.mouseOut = true;
		self:hideToolTip()
	end
end

--************************************************************************--
--** ISContextMenu:onMouseUp
--**
--************************************************************************--
function ISContextMenu:onMouseUp(x, y)
	if self:getScrollHeight() > self:getScrollAreaHeight() then
		if y < self.padTopBottom + self.scrollIndicatorHgt - self:getYScroll() then
			self:setYScroll(self:getYScroll() + self.itemHgt)
			self:onMouseMove(0, 0)
			return
		end
		if y >= self.padTopBottom + self.scrollIndicatorHgt + self:getScrollAreaHeight() - self:getYScroll() then
			self:setYScroll(self:getYScroll() - self.itemHgt)
			self:onMouseMove(0, 0)
			return
		end
	end
	if self.mouseOver ~= -1 and self:getIsVisible() then
		--print("calling option : " .. self.options[self.mouseOver].name);
		-- we call the function if we have one
		local option = self.options[self.mouseOver]
		if option ~= nil and option.onSelect ~= nil and not option.notAvailable then
            ISContextMenu.globalPlayerContext = self.player;
			self:closeAll()
			option.onSelect(option.target, option.param1, option.param2, option.param3, option.param4, option.param5, option.param6, option.param7, option.param8, option.param9, option.param10);
		end
		if option ~= nil and option.subOption ~= nil then
			if self:isOptionSingleMenu() then
				local subMenu = self:getSubMenu(option.subOption)
				subMenu.mouseOver = -1
				self:displaySubMenu(subMenu, option)
			end
		end
	end
end

function ISContextMenu:onMouseWheel(del)
	self:setYScroll(self:getYScroll() - del * self.itemHgt)
	self:onMouseMove(0, 0)
	return true
end

function ISContextMenu:ensureVisible()
	if not self.mouseOver then return end
	if self:getScrollHeight() <= self:getScrollAreaHeight() then return end
	local topY = self.padTopBottom + ((self:getScrollHeight() > self:getScrollAreaHeight()) and self.scrollIndicatorHgt or 0)
	local topItem = math.floor((topY - self:getYScroll()) / self.itemHgt) + 1
	local numVisibleItems = math.floor(self:getScrollAreaHeight() / self.itemHgt)
	local bottomItem = topItem + numVisibleItems - 1
	if self.mouseOver < topItem then
		self:setYScroll(0 - (self.mouseOver - 1) * self.itemHgt)
	elseif self.mouseOver > bottomItem then
		self:setYScroll(0 - (self.mouseOver - numVisibleItems) * self.itemHgt)
	end
end

function ISContextMenu:onFocus(x, y)
	-- do not call bringToTop(), otherwise the root context menu is drawn before
	-- its child menus, and render() sets the visibility of the submenu that the
	-- mouse is over
end

function ISContextMenu:onJoypadDirUp()
    if self.subMenu then
        self.subMenu:hideAndChildren()
        self.subMenu = nil
    end
    if self.mouseOver == nil then self.mouseOver = 1; end
    self.mouseOver = self.mouseOver - 1;
    if self.mouseOver <= 0 then
        self.mouseOver = self.numOptions - 1;
    end
    self:hideToolTip()
    self.mouseOut = false
    self:ensureVisible()
end

function ISContextMenu:onJoypadDirDown()
    if self.subMenu then
        self.subMenu:hideAndChildren()
        self.subMenu = nil
    end
    if self.mouseOver == nil then self.mouseOver = 1; end
    self.mouseOver = self.mouseOver + 1;
    if self.mouseOver >= self.numOptions then
        self.mouseOver = 1;
    end
    self:hideToolTip()
    self.mouseOut = false
    self:ensureVisible()
end

function ISContextMenu:onJoypadDirLeft()
    if self.parent then
		if self:isOptionSingleMenu() then
			local option = self.options[self.mouseOver]
			if option and option.isDefaultOption then
				option.onSelect(option.target, option.param1, option.param2, option.param3, option.param4, option.param5, option.param6, option.param7, option.param8, option.param9, option.param10)
				return
			end
			self:displayAncestor(self.parent)
			return
		end
		if self.subMenu then
			self.subMenu:hideAndChildren()
			self.subMenu = nil
		end
		self.mouseOver = nil
		self:hideToolTip()
		setJoypadFocus(self.player, self.parent)
	end
end

function ISContextMenu:onJoypadDirRight()
	local option = self.options[self.mouseOver]
	if option ~= nil and option.onSelect == nil and option.subOption ~= nil then
		self:hideToolTip()
		local subMenu = self:getSubMenu(option.subOption)
		if self:isOptionSingleMenu() then
			subMenu.mouseOver = 1 + subMenu:getDefaultOptionCount()
			self:displaySubMenu(subMenu, option)
			return
		end
		subMenu.forceVisible = true
		subMenu.mouseOver = 1
		setJoypadFocus(self.player, subMenu)
		subMenu:ensureVisible()
	end
end

function ISContextMenu:hideSelf()
    self:setVisible(false)
    self.visibleCheck = false
    self.forceVisible = false
    self:hideToolTip()
end

function ISContextMenu:hideAndChildren()
    self:hideSelf()
    for _,option in ipairs(self.options) do
        if option.subOption and self:getSubMenu(option.subOption) then
            self:getSubMenu(option.subOption):hideAndChildren()
        end
    end
    -- FIXME: this only works for the global context menu for each player
    if self.instanceMap == nil or #self.instanceMap == 0 then return;end
    for k, v in ipairs(self.instanceMap) do
        v:hideAndChildren();
    end
end

function ISContextMenu:showTooltip(option)
	if self.toolTip and (self.toolTip ~= option.toolTip) then
		self:hideToolTip()
	end
	if not self.toolTip and option.toolTip then
		self.toolTip = option.toolTip
		self.toolTip:setVisible(true)
		self.toolTip:addToUIManager()
		self.toolTip.followMouse = not self.joyfocus
	end
end

function ISContextMenu:hideToolTip()
	if self.toolTip ~= nil then
		self.toolTip:removeFromUIManager()
		self.toolTip:setVisible(false)
		self.toolTip = nil
	end
end

function ISContextMenu:onJoypadDown(button)
    if button == Joypad.AButton then
        if self.mouseOver > 0 and self:getIsVisible() then
            --print("calling option : " .. self.options[self.mouseOver].name);
            -- we call the function if we have one
            local option = self.options[self.mouseOver]
            if option ~= nil and option.onSelect == nil  and option.subOption ~= nil then
                local subMenu = self:getSubMenu(option.subOption)
                subMenu.mouseOver = 1;
                if self:isOptionSingleMenu() then
                    subMenu.mouseOver = subMenu.mouseOver + subMenu:getDefaultOptionCount()
                    self:displaySubMenu(subMenu, option)
                else
                    setJoypadFocus(self.player, subMenu)
                end
            elseif option ~= nil and option.onSelect ~= nil and not option.notAvailable then
                ISContextMenu.globalPlayerContext = self.player;
                self:closeAll();
                option.onSelect(option.target, option.param1, option.param2, option.param3, option.param4, option.param5, option.param6, option.param7, option.param8, option.param9, option.param10);
            end
        end
    end
    if button == Joypad.BButton then
        --[[
        if self:isOptionSingleMenu() then
            if self.parent then
                self:displayAncestor(self.parent)
                return
            end
        end
        --]]
        self:closeAll()
    end
end

function ISContextMenu:closeAll()
	self:hideAndChildren()
	local isJoypad = JoypadState.players[self.player+1]
	local parent = self.parent
	if isJoypad and (parent == nil) then
		setJoypadFocus(self.player, self.origin)
	end
	while parent do
		parent:setVisible(false)
		if isJoypad and (parent.parent == nil) then
			setJoypadFocus(self.player, parent.origin)
		end
		parent = parent.parent
	end
end

function ISContextMenu:onMouseDownOutside(x, y)
	if self.player == 0 then
		local forceVisible = self.forceVisible
		self:hideSelf()
		self.forceVisible = forceVisible
	end
end
--************************************************************************--
--** ISContextMenu:onMouseDown
--**
--************************************************************************--
function ISContextMenu:onMouseDown(x, y)

end
--************************************************************************--
--** ISContextMenu:render
--**
--************************************************************************--
function ISContextMenu:prerender()
	if self:isEmpty() then
		self:setX(100000); -- cheap hack for now ��
		return;
	end

	self:addDefaultOptions() -- delayed add

	if ((self.mouseOver == nil) or (self.mouseOver == -1) or (self.slideGoalY ~= nil)) and not JoypadState.players[self.player+1] and
			(self:topmostMenuWithMouse(getMouseX(), getMouseY()) == self) then
		self.mouseOver = self:getIndexAt(self:getMouseX(), self:getMouseY())
	end

	self:updateSlideGoalX()
	self:updateSlideGoalY()
end

function ISContextMenu:render()
	self.visibleCheck = true;

	if self:isEmpty() then
		return;
	end

	local y = self.padTopBottom;
	local dy = 0
	if self:getScrollHeight() > self:getScrollAreaHeight() then
		dy = self.scrollIndicatorHgt
		y = y + dy
	end

	self:drawRect(0, dy - self:getYScroll(), self.width, self.height - dy * 2, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
	self:drawRect(0, dy - self:getYScroll(), 17, self.height - dy * 2, 0.6, 0.1, 0.1, 0.1);
	self:drawRectBorderStatic(0, dy, self.width, self.height - dy * 2, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b)

	local highestWid = 0;

	local textDY = (self.itemHgt - self.fontHgt) / 2

	local backArrowDX = math.max(17 - getTextManager():MeasureStringX(self.font, "<"), 0) / 2

	self.currentOptionRect = { x = self.x, y = self.y + y + self:getYScroll(), width = 100, height = self.itemHgt }
	local c = 1;
	local sizeMap = {};
	local offTop,offBottom = false,false
	local showTooltip = false
	for i,k in ipairs(self.options) do
        if k.disable then return; end
		local sub = nil;
		if y + self:getYScroll() < 0 then
			-- off the top
			offTop = true
		elseif y + self:getYScroll() + self.itemHgt > self.scrollIndicatorHgt + self.padTopBottom + self:getScrollAreaHeight() then
			-- off the bottom
			offBottom = true
		elseif self.mouseOver == c then
			if k.notAvailable then
				self:drawRect(0, y, self.width, self.itemHgt, 0.1, 0.05, 0.05, 0.05);
				self:drawRectBorder(0, y, self.width, self.itemHgt, 0.15, 0.9, 0.9, 1);
				self:drawText(k.name, 24, y+textDY, 1, 0.2, 0.2, 0.85, self.font);
                if k.subOption ~= nil then
                    self:drawTextRight(">", self.width - 4, y+textDY, 1, 0.2, 0.2, 0.85, self.font);
                end
			else
				self:drawRect(0, y, self.width, self.itemHgt, 0.8, 0.5, 0.5, 0.5);
				self:drawRectBorder(0, y, self.width, self.itemHgt, 0.15, 0.9, 0.9, 1);
				self:drawText(k.name, 24, y+textDY, 1, 1, 1, 1, self.font);
			end
--~ 			if k.textDisplay then
--~ 			if ISContextMenu.toolTip ~= nil then
--~ 				ISContextMenu.toolTip:removeFromUIManager();
--~ 				ISContextMenu.toolTip:setVisible(false);
--~ 				ISContextMenu.toolTip = nil;
--~ 			elseif k.toolTip and not self:isMouseOut() then
--~ 				ISContextMenu.toolTip = k.toolTip;
--~ 				ISContextMenu.toolTip:setVisible(true);
--~ 				ISContextMenu.toolTip:addToUIManager();
--~ 			end

			local isMouseOut = self:isMouseOut()
			if JoypadState.players[self.player+1] and not self.joyfocus then
				isMouseOut = true
			end
			if k.toolTip and not isMouseOut then
				self:showTooltip(k)
				showTooltip = true
			end
--~ 				self:drawText(k.textDisplay, self:getMouseX(), self:getMouseY(), 1, 1, 1, 1, UIFont.Normal);
--~ 			end
			if k.isDefaultOption then
				self:drawText("<", backArrowDX, y + textDY, 1, 0.8, 0.8, 0.9, self.font);
			end
			-- if we have a subOption, we set it visible
			if k.subOption ~= nil then
				self:drawTextRight(">", self.width - 4, y+textDY, 1, 1, 1, 1, self.font);
				if self:isOptionSingleMenu() then
					-- nothing
				elseif self.forceVisible then
					self.subMenu = self:getSubMenu(k.subOption);
					self.subMenu:setVisible(true);
					-- is position is next to our selected option
					dy = (self.subMenu:getScrollHeight() > self.subMenu:getScrollAreaHeight()) and self.scrollIndicatorHgt or 0
					self:getSubMenu(k.subOption):setY(y - self.subMenu.padTopBottom + self.y + self:getYScroll() - dy);
					table.insert(sizeMap, self:getSubMenu(k.subOption));
				end
			end
			self.currentOptionRect = { x = self.x, y = self.y + y + self:getYScroll(), width = 100, height = self.itemHgt }
		else
			if k.notAvailable then
				self:drawText(k.name, 24, y+textDY, 1, 0.2, 0.2, 0.85, self.font);
                if k.subOption ~= nil then
                    self:drawTextRight(">", self.width - 4, y+textDY, 1, 0.2, 0.2, 0.85, self.font);
                end
            else
                if self.blinkOption == k.name then
                    if not self.blinkAlpha then
                        self.blinkAlpha = 1;
                        self.blinkAlphaIncrease = false;
                    end

                    if not self.blinkAlphaIncrease then
                        self.blinkAlpha = self.blinkAlpha - 0.1 * (UIManager.getMillisSinceLastRender() / 33.3);
                        if self.blinkAlpha < 0 then
                            self.blinkAlpha = 0;
                            self.blinkAlphaIncrease = true;
                        end
                    else
                        self.blinkAlpha = self.blinkAlpha + 0.1 * (UIManager.getMillisSinceLastRender() / 33.3);
                        if self.blinkAlpha > 1 then
                            self.blinkAlpha = 1;
                            self.blinkAlphaIncrease = false;
                        end
                    end

                    self:drawRect(0, y, self.width, self.itemHgt, self.blinkAlpha, 1, 1, 1);
                end
				self:drawText(k.name, 24, y+textDY, 1, 0.8, 0.8, 0.9, self.font);
				if k.isDefaultOption then
					self:drawText("<", backArrowDX, y + textDY, 1, 0.8, 0.8, 0.9, self.font);
				end
				if k.subOption ~= nil then
					self:drawTextRight(">", self.width - 4, y+textDY, 1, 0.8, 0.8, 0.9, self.font);
				end
			end
		end
		if k.checkMark then
			self:drawTexture(self.tickTexture, (17 - self.tickTexture:getWidthOrig()) / 2, y + (self.itemHgt - self.tickTexture:getHeightOrig()) / 2, 1, 1, 1, 1)
		end
		if k.isDefaultOption and self.options[i+1] and not self.options[i+1].isDefaultOption then
			self:drawRect(0, y + self.itemHgt - 1, self.width, 1, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b)
		end
		y = y + self.itemHgt;
		local w = getTextManager():MeasureStringX(self.font, k.name);
		if(w > highestWid) then
			highestWid = w;
		end
		c = c + 1;
	end

	if offTop then
		self:drawRect(0, 0 - self:getYScroll(), self.width, self.scrollIndicatorHgt, 0.8, 0.1, 0.1, 0.1);
		local x = self.width / 2 - 14 - 7
		self:drawTexture(self.arrowUp, x, 0 - self:getYScroll(), 1, 1, 1, 1)
		self:drawTexture(self.arrowUp, x + 14, 0 - self:getYScroll(), 1, 1, 1, 1)
		self:drawTexture(self.arrowUp, x + 14 * 2, 0 - self:getYScroll(), 1, 1, 1, 1)
	end
	if offBottom then
		self:drawRect(0, self.height - self.scrollIndicatorHgt - self:getYScroll(), self.width, self.scrollIndicatorHgt, 0.8, 0.1, 0.1, 0.1);
		local x = self.width / 2 - 14 - 7
		self:drawTexture(self.arrowDown, x, self.height - self.scrollIndicatorHgt - self:getYScroll(), 1, 1, 1, 1)
		self:drawTexture(self.arrowDown, x + 14, self.height - self.scrollIndicatorHgt - self:getYScroll(), 1, 1, 1, 1)
		self:drawTexture(self.arrowDown, x + 14 * 2, self.height - self.scrollIndicatorHgt - self:getYScroll(), 1, 1, 1, 1)
	end

	if self.toolTip then
        self.toolTip:setContextMenu(self);
        self.toolTip:setVisible(true);
--~ 		self:drawText(displayText, self:getMouseX(), self:getMouseY(), 1, 1, 1, 1, UIFont.Normal);
	end

	local ww = highestWid + 24 + 40;
	if(ww<100) then
		ww = 100;
	end
	self:setWidth(ww);
	self.currentOptionRect.width = ww

	-- we make his x at the edge of the current menu
	-- place submenu to the left if there is no room on the right
	for _,subMenu in ipairs(sizeMap) do
		local subMenuWidth = subMenu:calcWidth()
		if self.parent ~= nil and self.x < self.parent.x then
			subMenu:setX(self.x - subMenuWidth - 1)
		elseif self.x + ww + 1 + subMenuWidth > getCore():getScreenWidth() then
			subMenu:setX(self.x - subMenuWidth - 1)
		else
			subMenu:setX(self.x + ww + 1)
		end
	end

	if not showTooltip and self.player == 0 then
		self:hideToolTip()
	end
end

function ISContextMenu:calcHeight()
	local itemsHgt = (self.numOptions - 1) * self.itemHgt
	local screenHgt = getCore():getScreenHeight()
--	screenHgt = 200
	if itemsHgt + self.padTopBottom * 2 > screenHgt then
		local numVisibleItems = math.floor((screenHgt - self.padTopBottom * 2 - self.scrollIndicatorHgt * 2) / self.itemHgt)
		self.scrollAreaHeight = numVisibleItems * self.itemHgt
		self:setHeight(self.scrollAreaHeight + self.padTopBottom * 2 + self.scrollIndicatorHgt * 2)
		self:setScrollHeight(itemsHgt)
	else
		self.scrollAreaHeight = itemsHgt
		self:setHeight(itemsHgt + self.padTopBottom * 2)
		self:setScrollHeight(itemsHgt)
	end
end

function ISContextMenu:calcWidth()
	local maxWidth = 0
	for _,k in ipairs(self.options) do
		local w = getTextManager():MeasureStringX(self.font, k.name)
		if(w > maxWidth) then
			maxWidth = w
		end
	end
	return math.max(maxWidth + 24 + 40, 100)
end

function ISContextMenu:isOptionSingleMenu()
	return getCore():getOptionSingleContextMenu(self.player)
end

function ISContextMenu:isAnyVisible()
	if self:isVisible() then return true end
	if self:isOptionSingleMenu() and self.instanceMap then
		for _,subMenu in ipairs(self.instanceMap) do
			if subMenu:isVisible() then
				return true
			end
		end
	end
	return false
end

function ISContextMenu:getIndexForSubMenu(subMenu)
	for i,option in ipairs(self.options) do
		if option.subOption and (self:getSubMenu(option.subOption) == subMenu) then
			return i
		end
	end
	return -1
end

function ISContextMenu:addDefaultOptions()
	-- Default options could be added when addSubMenu() is called, however some code
	-- will remove a submenu option if the submenu is empty.  It won't be empty if
	-- default options were added.
	if self.addedDefaultOptions then return end
	self.addedDefaultOptions = true

	if not self:isOptionSingleMenu() then return end

	-- Fix for long time it takes to add options to ISWorldObjectContextMenu.
	self.slideGoalTime = getTimestampMs()

	local ancestor = self.parent
	local child = self
	while ancestor do
		local index = ancestor:getIndexForSubMenu(child)
		if index == -1 then
			print("ERROR: ISContextMenu:getNew() was called with the wrong parent menu")
		else
			local option = ancestor.options[index]
			option = self:addOptionOnTop(option.name, self, ISContextMenu.displayAncestor, ancestor)
			option.isDefaultOption = true
		end
		child = ancestor
		ancestor = ancestor.parent
	end

	if self.slideGoalY then
		-- Fix for keepOnScreen=true changing the y position.
		self:setY(self:getY())
		self.slideGoalY = self:getY()
		self:setY(self.slideGoalY - self.slideGoalDY)
	end
end

function ISContextMenu:getDefaultOptionCount()
	self:addDefaultOptions() -- delayed add
	local count = 0
	for i,option in ipairs(self.options) do
		if option.isDefaultOption then
			count = count + 1
		else
			break
		end
	end
	return count
end

function ISContextMenu:setSlideGoalX(startX, finalX)
	self:setX(finalX)
	if not self:isOptionSingleMenu() then return end
	if not JoypadState.players[self.player+1] then return end
	self:setX(startX)
	self.slideGoalX = finalX
	self.slideGoalTime = getTimestampMs()
end

function ISContextMenu:setSlideGoalY(startY, finalY)
	self:setY(finalY)
	if not self:isOptionSingleMenu() then return end
	if JoypadState.players[self.player+1] then return end
	self:setY(startY)
	self.slideGoalY = finalY
	self.slideGoalDY = finalY - startY
	self.slideGoalTime = getTimestampMs()
end

function ISContextMenu:updateSlideGoalX()
	if not self.slideGoalX then return end
	if self.slideGoalX < self:getX() then
		local dt = math.min(getTimestampMs() - self.slideGoalTime, 100)
		self:setX(self.slideGoalX + (self:getX() - self.slideGoalX) * (1 - dt / 100))
		if self:getX() - self.slideGoalX < 1 then
			self.slideGoalX = nil
		end
	elseif self.slideGoalX > self:getX() then
		local dt = math.min(getTimestampMs() - self.slideGoalTime, 100)
		self:setX(self.slideGoalX - (self.slideGoalX - self:getX()) * (1 - dt / 100))
		if self.slideGoalX - self:getX() < 1 then
			self.slideGoalX = nil
		end
	end
end

function ISContextMenu:updateSlideGoalY()
	if not self.slideGoalY then return end
	if self.slideGoalY < self:getY() then
		local dt = math.min(getTimestampMs() - self.slideGoalTime, 100)
		self:setY(self.slideGoalY + (self:getY() - self.slideGoalY) * (1 - dt / 100))
		if self:getY() - self.slideGoalY < 1 then
			self.slideGoalY = nil
		end
	elseif self.slideGoalY > self:getY() then
		local dt = math.min(getTimestampMs() - self.slideGoalTime, 100)
		self:setY(self.slideGoalY - (self.slideGoalY - self:getY()) * (1 - dt / 100))
		if self.slideGoalY - self:getY() < 1 then
			self.slideGoalY = nil
		end
	end
end

local SLIDEX = 50
local SLIDEY = 10

function ISContextMenu:displayAncestor(ancestor)
	self:hideSelf()
	local ancestorY = self:getRootY()
	ancestor:setVisible(true)
	ancestor:setSlideGoalX(self:getX() - SLIDEX, self:getX())
	ancestor:setSlideGoalY(ancestorY + SLIDEY, ancestorY)
	if JoypadState.players[self.player+1] then
		setJoypadFocus(self.player, ancestor)
	else
		ancestor.mouseOver = -1
	end
end

function ISContextMenu:displaySubMenu(subMenu, option)
	self:hideSelf()

	local subY = self:getRootY()
	if not JoypadState.players[self.player+1] then
		subMenu:addDefaultOptions()
		if (subY + subMenu:getHeight() <= self:getItemY(option.id)) or (subY >= self:getItemY(option.id + 1)) then
			local itemY = self:getItemY(option.id) - subMenu:getDefaultOptionCount() * subMenu.itemHgt
			local dy = (subMenu:getScrollHeight() > subMenu:getScrollAreaHeight()) and self.scrollIndicatorHgt or 0
			subY = itemY - subMenu.padTopBottom - dy
		end
		-- Fix for keepOnScreen=true changing the y position.
		subMenu:setY(subY)
		subY = subMenu:getY()
	end
	
	subMenu:setSlideGoalX(self:getX() + SLIDEX, self:getX())
	subMenu:setSlideGoalY(subY - SLIDEY, subY)

	subMenu:setVisible(true)

	if JoypadState.players[self.player+1] then
		subMenu.mouseOut = false
		setJoypadFocus(self.player, subMenu)
	elseif self.player == 0 then
		subMenu.mouseOut = subMenu:topmostMenuWithMouse(getMouseX(), getMouseY()) ~= subMenu
	end
end

function ISContextMenu:getRootY()
	local ancestor = self
	while ancestor.parent do
		ancestor = ancestor.parent
	end
	return ancestor:getY()
end

function ISContextMenu:getItemY(index)
	local y = self.y + self.padTopBottom
	if self:getScrollHeight() > self:getScrollAreaHeight() then
		y = y + self.scrollIndicatorHgt
	end
	return y + (index - 1) * self.itemHgt + self:getYScroll()
end

function ISContextMenu:getIndexAt(x, y)
	if (x < 0) or (x >= self:getWidth()) then
		return -1
	end
	local dy = (self:getScrollHeight() > self:getScrollAreaHeight()) and self.scrollIndicatorHgt or 0
	local index = math.floor((y - self.padTopBottom - dy) / self.itemHgt + 1)
	if index >= 1 and index < self.numOptions then
		return index
	end
	return -1
end

function ISContextMenu:topmostMenuWithMouse(x, y)
	local contextMenu = getPlayerContextMenu(self.player)
	if not contextMenu then return nil end
	local menu = nil
	if self == contextMenu then
		if self:isVisible() and x >= self.x and x < self.x + self.width and y >= self.y and y < self.y + self.height then
			menu = self
		end
	end
	for i=1,#contextMenu.instanceMap do
		local m = contextMenu.instanceMap[i]
		if m:isVisible() and x >= m.x and x < m.x + m.width and y >= m.y and y < m.y + m.height then
			menu = m
		end
	end
	return menu
end

function ISContextMenu:allocOption(name, target, onSelect, param1, param2, param3, param4, param5, param6, param7, param8, param9, param10)
	if #self.optionPool == 0 then
		table.insert(self.optionPool, {})
	end
	local option = table.remove(self.optionPool, #self.optionPool)
	table.wipe(option)
	option.id = self.numOptions
	option.name = name
	option.onSelect = onSelect
	option.target = target
	option.param1 = param1
	option.param2 = param2
	option.param3 = param3
	option.param4 = param4
	option.param5 = param5
	option.param6 = param6
	option.param7 = param7
	option.param8 = param8
	option.param9 = param9
	option.param10 = param10
	option.subOption = nil
	return option
end

function ISContextMenu:addOption(name, target, onSelect, param1, param2, param3, param4, param5, param6, param7, param8, param9, param10)
	if getCore():getGameMode() == "Tutorial" then
		if self:getOptionFromName(name) then
			return;
		end
	end
	local option = self:allocOption(name, target, onSelect, param1, param2, param3, param4, param5, param6, param7, param8, param9, param10);
	self.options[self.numOptions] = option;
	self.numOptions = self.numOptions + 1;
	self:calcHeight()
	self:setWidth(self:calcWidth())
	return option;
end

function ISContextMenu:getOptionFromName(name)
	for i,v in ipairs(self.options) do
		if v.name == name then
			return v;
		end
	end
end

function ISContextMenu:addOptionOnTop(name, target, onSelect, param1, param2, param3, param4, param5, param6, param7, param8, param9, param10)
	
	local newOptions = {};
	for i,v in ipairs(self.options) do
		v.id = v.id + 1;
		newOptions[v.id] = v;
	end
	
	self.options = newOptions;
	local option = self:allocOption(name, target, onSelect, param1, param2, param3, param4, param5, param6, param7, param8, param9, param10);
	option.id = 1;
	self.options[1] = option;
	self.numOptions = self.numOptions + 1;
	self:calcHeight()
	self:setWidth(self:calcWidth())
	return option;
end

function ISContextMenu:removeLastOption()
    table.insert(self.optionPool, self.options[self.numOptions - 1])
    self.options[self.numOptions - 1] =  nil;
    self.numOptions = self.numOptions -1;
	self:calcHeight()
end

function ISContextMenu:addSubMenu(option, menu)
	option.subOption = menu.subOptionNums;
end

function ISContextMenu:addActionsOption(text, getActionsFunction, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10)
	local character = getSpecificPlayer(self.player)
	return self:addOption(text, character, ISTimedActionQueue.queueActions, getActionsFunction, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10)
end

function ISContextMenu:setOptionChecked(option, checked)
	option.checkMark = checked
end

function ISContextMenu:clear()
	for _,option in ipairs(self.options) do
		table.insert(self.optionPool, option)
	end
	table.wipe(self.options)
	self.numOptions = 1;
	self.mouseOver = -1
	self.subMenu = nil
	self:setHeight(0)
	self.addedDefaultOptions = false
end

function ISContextMenu:isEmpty()
	return self.numOptions == 1
end

function ISContextMenu:getScrollAreaHeight()
	return self.scrollAreaHeight or 0
end

function ISContextMenu:setFont(font)
	self.font = font or UIFont.Medium
	self.fontHgt = getTextManager():getFontHeight(self.font)
	self.itemHgt = self.fontHgt + self.padY * 2
end

function ISContextMenu:setFontFromOption()
	local font = getCore():getOptionContextMenuFont()
	if font == "Large" then
		self:setFont(UIFont.Large)
	elseif font == "Small" then
		self:setFont(UIFont.Small)
	else
		self:setFont(UIFont.Medium)
	end
end

--************************************************************************--
--** ISContextMenu:new
--**
--************************************************************************--
function ISContextMenu:new (x, y, width, height, zoom)
	local o = {}
	--o.data = {}
	o = ISPanel:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self
	o.x = x;
	o.y = y;
	o.zoom = zoom;
	o.font = UIFont.Medium;
	o.padY = 6
	o.fontHgt = getTextManager():getFontFromEnum(o.font):getLineHeight()
	o.itemHgt = o.fontHgt + o.padY * 2
	o.padTopBottom = 0
	o.borderColor = {r=1, g=1, b=1, a=0.15};
	o.backgroundColor = {r=0.1, g=0.1, b=0.1, a=0.7};
	o.backgroundColorMouseOver = {r=0.3, g=0.3, b=0.3, a=1.0};
	o.width = width;
	o.height = height;
	o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;
	o.parent = {};
	-- Must override ISUIElement getKeepOnScreen because parent ~= null for sub menus
	o.keepOnScreen = true
	o.options = {};
	o.numOptions = 1;
	o.optionPool = {};
	o.visibleCheck = false;
    o.forceVisible = true;
    o.toolTip = nil;
	o.subOptionNums = 0;
    o.player = 0;
    o.scrollIndicatorHgt = 14
    o.arrowUp = getTexture("media/ui/ArrowUp.png")
    o.arrowDown = getTexture("media/ui/ArrowDown.png")
	o.tickTexture = getTexture("Quest_Succeed")

	return o
end

ISContextMenu.get = function(player, x, y)
    local context = getPlayerContextMenu(player);
	context:hideAndChildren();
    context:setVisible(true);
    context:clear();
    context:setFontFromOption()
	context.forceVisible = true;
    context.parent = nil;
    context:setSlideGoalX(x + 20, x)
    context:setSlideGoalY(y - SLIDEY, y)
    context:bringToTop();
    context:setVisible(true);
    context.visibleCheck = true;
    if context.instanceMap then
        for _,v in pairs(context.instanceMap) do
            v:setVisible(false)
            v:removeFromUIManager()
            table.insert(context.subMenuPool, v)
        end
        table.wipe(context.instanceMap)
    end
    context.instanceMap = context.instanceMap or {}
    context.subMenuPool = context.subMenuPool or {}
    context.subOptionNums = 0;
    context.subInstance = nil
    context.subMenu = nil;
	context.player = player;
	context:setForceCursorVisible(player == 0)
	return context;
end

function ISContextMenu:getNew(parentContext)
	local context = getPlayerContextMenu(parentContext.player);
    if #context.subMenuPool == 0 then
        context.subInstance = ISContextMenu:new(0, 0, 1, 1, 1.5);
    else
        context.subInstance = table.remove(context.subMenuPool, #context.subMenuPool)
    end
    context.subInstance:initialise();
    context.subInstance:instantiate();
    context.subInstance:addToUIManager();
    context.subInstance:clear();
    context.subInstance:setFontFromOption()
    context.subInstance:setX(parentContext:getX());
    context.subInstance:setY(parentContext:getY());
    context.subInstance.parent = parentContext;
    context.subInstance.forceVisible = true;
    context.subInstance:setVisible(false);
    context.subInstance:bringToTop();
	context.subInstance.player = parentContext.player;
	context:setForceCursorVisible(parentContext.player == 0)
	context.subOptionNums = context.subOptionNums + 1;
	context.subInstance.subOptionNums = context.subOptionNums;
    context.instanceMap[context.subOptionNums] = context.subInstance;
	return context.subInstance;
end

function ISContextMenu:getSubMenu(num)
	return getPlayerContextMenu(self.player):getSubInstance(num)
end

function ISContextMenu:getSubInstance(num)
	return self.instanceMap[num];
end

ISContextMenu.wantNoise = false
ISContextMenu.noise = function(msg)
	if (ISContextMenu.wantNoise) then print('ISContextMenu: '..msg) end
end


