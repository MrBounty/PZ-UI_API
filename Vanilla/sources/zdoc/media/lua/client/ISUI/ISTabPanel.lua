--***********************************************************
--**                    ROBERT JOHNSON                     **
--** A collapsable window with multiple tab, can be dragged out or in to make new tab **
--***********************************************************

require "ISUI/ISPanel"

---@class ISTabPanel : ISPanel
ISTabPanel = ISPanel:derive("ISTabPanel");
ISTabPanel.tabSelected = nil;
ISTabPanel.tabUnSelected = nil;
ISTabPanel.xMouse = -1;
ISTabPanel.yMouse = -1;
ISTabPanel.mouseOut = false;
ISTabPanel.viewDragging = nil;

--************************************************************************--
--** ISTabPanel:initialise
--**
--************************************************************************--

function ISTabPanel:initialise()
	ISPanel.initialise(self);
end


function ISTabPanel:updateSmoothScrolling()
	if not self.smoothScrollTargetX then return end
	local dx = self.smoothScrollTargetX - self.smoothScrollX
	local inset = 1
	local maxXScroll = self:getWidthOfAllTabs() - (self:getWidth() - inset * 2)
	if maxXScroll < 0 then maxXScroll = 0 end
	local frameRateFrac = UIManager.getMillisSinceLastRender() / 33.3
	local targetX = self.smoothScrollX + dx * 0.25 * frameRateFrac
	if targetX > 0 then targetX = 0 end
	if targetX < -maxXScroll then targetX = -maxXScroll end
	if math.abs(targetX - self.smoothScrollTargetX) > 1 then
		self.scrollX = math.floor(targetX)
		self.smoothScrollX = targetX
--		print(dx .. "," .. self.scrollX .. "," .. self.smoothScrollTargetX .. "," .. maxXScroll)
	else
		self.scrollX = self.smoothScrollTargetX
--		print(dx .. "," .. self.scrollX .. "," .. self.smoothScrollTargetX .. "," .. maxXScroll)
		self.smoothScrollTargetX = nil
	end
end

function ISTabPanel:ensureVisible(index)
    if not index or index < 1 or index > #self.viewList then return end
	if not self.smoothScrollTargetX then self.smoothScrollX = self.scrollX end
	local inset = 1
	local x = self:getTabX(index, 0) - inset
	local tabWidth = self.equalTabWidth and self.maxLength or self.viewList[index].tabWidth
	if x < 0-self.scrollX then
		self.smoothScrollTargetX = 0 - x
	elseif x + tabWidth > 0 - self.scrollX + (self.width - inset * 2) then
		self.smoothScrollTargetX = 0 - (x + tabWidth - (self.width - inset * 2))
	end
end

function ISTabPanel:setTabsTransparency(alpha)
    self.tabTransparency = alpha;
end

function ISTabPanel:setTextTransparency(alpha)
    self.textTransparency = alpha;
end

function ISTabPanel:prerender()
	-- if the mouse is over the tab panel and we got a tab to drag, we gonna display it outside
	if ISTabPanel.mouseOut and ISTabPanel.viewDragging and not ISMouseDrag.dragView then
		self:clearStencilRect();
		self:setStencilRect(0 - self:getAbsoluteX(), 0 - self:getAbsoluteY(), getCore():getScreenWidth(), getCore():getScreenHeight());
		self:drawRectBorder(self:getMouseX(), self:getMouseY(), ISTabPanel.viewDragging.view:getWidth(), ISTabPanel.viewDragging.view:getHeight(), 1,1,1,1);
		self:clearStencilRect();
	end
	self:updateSmoothScrolling()
end

--************************************************************************--
--** ISTabPanel:render
--**
--************************************************************************--
function ISTabPanel:render()
	local newViewList = {};
	local tabDragSelected = -1;
	if self.draggingTab and not self.isDragging and ISTabPanel.xMouse > -1 and ISTabPanel.xMouse ~= self:getMouseX() then -- do we move the mouse since we have let the left button down ?
		self.isDragging = self.allowDraggingTabs;
	end
	local tabWidth = self.maxLength
	local inset = 1 -- assumes a 1-pixel window border on the left to avoid
	local gap = 1 -- gap between tabs
	if self.isDragging and not ISTabPanel.mouseOut then
		-- we fetch all our view to remove the tab of the view we're dragging
		for i,viewObject in ipairs(self.viewList) do
			if i ~= (self.draggingTab + 1) then
				table.insert(newViewList, viewObject);
			else
				ISTabPanel.viewDragging = viewObject;
			end
		end
		-- in wich tab slot are we dragging our tab
		tabDragSelected = self:getTabIndexAtX(self:getMouseX()) - 1;
		tabDragSelected = math.min(#self.viewList - 1, math.max(tabDragSelected, 0))
		-- we draw a white rectangle to show where our tab is going to be
		self:drawRectBorder(inset + (tabDragSelected * (tabWidth + gap)), 0, tabWidth, self.tabHeight - 1, 1,1,1,1);
	else -- no dragging, we display all our tabs
		newViewList = self.viewList;
	end
	-- our principal rect, wich display our different view
	self:drawRect(0, self.tabHeight, self.width, self.height - self.tabHeight, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
	self:drawRectBorder(0, self.tabHeight, self.width, self.height - self.tabHeight, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	local x = inset;
	if self.centerTabs and (self:getWidth() >= self:getWidthOfAllTabs()) then
		x = (self:getWidth() - self:getWidthOfAllTabs()) / 2
	else
		x = x + self.scrollX
	end
	local widthOfAllTabs = self:getWidthOfAllTabs()
	local overflowLeft = self.scrollX < 0
	local overflowRight = x + widthOfAllTabs > self.width
    local blinkTabsAlphaNotUpdated = true;
	if widthOfAllTabs > self.width then
		self:setStencilRect(0, 0, self.width, self.tabHeight)
	end
	for i,viewObject in ipairs(newViewList) do
		tabWidth = self.equalTabWidth and self.maxLength or viewObject.tabWidth
		-- if we drag a tab over an existing one, we move the other
		if tabDragSelected ~= -1 and i == (tabDragSelected + 1) then
			x = x + tabWidth + gap;
		end
		-- if this tab is the active one, we make the tab btn lighter
		if viewObject.name == self.activeView.name and not self.isDragging and not ISTabPanel.mouseOut then
			self:drawTextureScaled(ISTabPanel.tabSelected, x, 0, tabWidth, self.tabHeight - 1, self.tabTransparency,1,1,1);
        else
            local alpha = self.tabTransparency;
            local shouldBlink = false;
            if self.blinkTabs then
                for j,tab in ipairs(self.blinkTabs) do
                    if tab and tab == viewObject.name then
                        shouldBlink = true;
                    end
                end
            end
            if (self.blinkTab and self.blinkTab == viewObject.name) or (shouldBlink and blinkTabsAlphaNotUpdated) then
                blinkTabsAlphaNotUpdated = false;
                if not self.blinkTabAlpha then
                    self.blinkTabAlpha = self.tabTransparency;
                    self.blinkTabAlphaIncrease = false;
                end

                if not self.blinkTabAlphaIncrease then
                    self.blinkTabAlpha = self.blinkTabAlpha - 0.1 * self.tabTransparency * (UIManager.getMillisSinceLastRender() / 33.3);
                    if self.blinkTabAlpha < 0 then
                        self.blinkTabAlpha = 0;
                        self.blinkTabAlphaIncrease = true;
                    end
                else
                    self.blinkTabAlpha = self.blinkTabAlpha + 0.1 * self.tabTransparency * (UIManager.getMillisSinceLastRender() / 33.3);
                    if self.blinkTabAlpha > self.tabTransparency then
                        self.blinkTabAlpha = self.tabTransparency;
                        self.blinkTabAlphaIncrease = false;
                    end
                end
                alpha = self.blinkTabAlpha;
                self:drawTextureScaled(ISTabPanel.tabUnSelected, x, 0, tabWidth, self.tabHeight - 1, self.tabTransparency,1,1,1);
                self:drawRect(x, 0, tabWidth, self.tabHeight - 1, alpha, 1, 1, 1);
            elseif shouldBlink then
                alpha = self.blinkTabAlpha;
                self:drawTextureScaled(ISTabPanel.tabUnSelected, x, 0, tabWidth, self.tabHeight - 1, self.tabTransparency,1,1,1);
                self:drawRect(x, 0, tabWidth, self.tabHeight - 1, alpha, 1, 1, 1);
            else
			    self:drawTextureScaled(ISTabPanel.tabUnSelected, x, 0, tabWidth, self.tabHeight - 1, self.tabTransparency,1,1,1);
			    if self:getMouseY() >= 0 and self:getMouseY() < self.tabHeight and self:isMouseOver() and self:getTabIndexAtX(self:getMouseX()) == i then
					viewObject.fade:setFadeIn(true)
				else
					viewObject.fade:setFadeIn(false)
			    end
			    viewObject.fade:update()
				self:drawTextureScaled(ISTabPanel.tabSelected, x, 0, tabWidth, self.tabHeight - 1, 0.2 * viewObject.fade:fraction(),1,1,1);
           end
		end
		self:drawTextCentre(viewObject.name, x + (tabWidth / 2), 3, 1, 1, 1, self.textTransparency, UIFont.Small);
		x = x + tabWidth + gap;
	end
	local butPadX = 3
	if overflowLeft then
		local tex = getTexture("media/ui/ArrowLeft.png")
		local butWid = tex:getWidthOrig() + butPadX * 2
		self:drawRect(inset, 0, butWid, self.tabHeight, 1, 0, 0, 0)
		self:drawRectBorder(inset, 0, butWid, self.tabHeight, 1, 1, 1, 1)
		self:drawTexture(tex, inset + butPadX, (self.tabHeight - tex:getHeight()) / 2, 1, 1, 1, 1)
	end
	if overflowRight then
		local tex = getTexture("media/ui/ArrowRight.png")
		local butWid = tex:getWidthOrig() + butPadX * 2
		self:drawRect(self.width - inset - butWid, 0, butWid, self.tabHeight, 1, 0, 0, 0)
		self:drawRectBorder(self.width - inset - butWid, 0, butWid, self.tabHeight, 1, 1, 1, 1)
		self:drawTexture(tex, self.width - butWid + butPadX, (self.tabHeight - tex:getHeight()) / 2, 1, 1, 1, 1)
	end
	if widthOfAllTabs > self.width then
		self:clearStencilRect()
	end
	-- we draw a ghost of our tab we currently dragging
	if self.draggingTab and self.isDragging and not ISTabPanel.mouseOut then
		if self.draggingTab > 0 then
			self:drawTextureScaled(ISTabPanel.tabSelected, inset + (self.draggingTab * (tabWidth + gap)) + (self:getMouseX() - ISTabPanel.xMouse), 0, tabWidth, self.tabHeight - 1, 0.8,1,1,1);
			self:drawTextCentre(ISTabPanel.viewDragging.name, inset + (self.draggingTab * (tabWidth + gap)) + (self:getMouseX() - ISTabPanel.xMouse) + (tabWidth / 2), 3, 1, 1, 1, 1, UIFont.Normal);
		else
			self:drawTextureScaled(ISTabPanel.tabSelected, inset + (self:getMouseX() - ISTabPanel.xMouse), 0, tabWidth, self.tabHeight - 1, 0.8,1,1,1);
			self:drawTextCentre(ISTabPanel.viewDragging.name, inset + (self:getMouseX() - ISTabPanel.xMouse) + (tabWidth / 2), 3, 1, 1, 1, 1, UIFont.Normal);
		end
    end
end

function ISTabPanel:onMouseDown(x, y)
	if self.isDragging then
		ISTabPanel.redoTab(self);
	elseif self:getMouseY() >= 0 and self:getMouseY() < self.tabHeight then
		if self:getScrollButtonAtX(x) == "left" then
			self:onMouseWheel(-1)
			return
		end
		if self:getScrollButtonAtX(x) == "right" then
			self:onMouseWheel(1)
			return
		end
		local tabIndex = self:getTabIndexAtX(self:getMouseX())
		if tabIndex >= 1 and tabIndex <= #self.viewList and ISTabPanel.xMouse == -1 and ISTabPanel.yMouse == -1 then -- if we clicked on a tab, the first time we set up the x,y of the mouse, so next time we can see if the player moved the mouse (moved the tab)
			ISTabPanel.xMouse = self:getMouseX();
			ISTabPanel.yMouse = self:getMouseY();
			self.draggingTab = tabIndex - 1;
			local clickedTab = self.viewList[self.draggingTab + 1];
			getSoundManager():playUISound("UIActivateTab")
			self:activateView(clickedTab.name)
		end
	end
end

-- if you drag a tab outside the panel
function ISTabPanel:onMouseMoveOutside(dx, dy)
	if self.draggingTab and self.allowTornOffTabs then
		ISTabPanel.mouseOut = true;
	end
end

-- if you drag a tab inside the panel
function ISTabPanel:onMouseMove(dx, dy)
	ISTabPanel.mouseOut = false;
	if ISMouseDrag.dragView and not self.draggingTab then
		-- we create a "false" view so we can drag it easily
		ISTabPanel.viewDraggin = {};
		ISTabPanel.viewDraggin.view = ISMouseDrag.dragView;
		if ISTabPanel.viewDraggin.view:getTitle() then
			ISTabPanel.viewDraggin.name = ISTabPanel.viewDraggin.view:getTitle();
		else
			ISTabPanel.viewDraggin.name = "Tab " .. #self.viewList + 1;
		end
		table.insert(self.viewList, ISTabPanel.viewDraggin);
		self.draggingTab = #self.viewList - 1;
		self.isDragging = true;
		ISTabPanel.xMouse = self:getMouseX();
		ISTabPanel.fromOutside = true;
		ISMouseDrag.tabPanel = self;
	end
end


-- if you dragged a tab out of the panel and release the button, we place the view here
function ISTabPanel:onMouseUpOutside(x, y)
	if ISTabPanel.mouseOut and self.isDragging and ISTabPanel.viewDragging then
		local newViewList = {};
		for i,viewObject in ipairs(self.viewList) do
			if i ~= (self.draggingTab + 1) then
				table.insert(newViewList, viewObject);
			end
		end
		self.viewList = newViewList;
		self.draggingTab = nil;
		ISTabPanel.xMouse = -1;
		ISTabPanel.yMouse = -1;
		self.isDragging = false;
		ISTabPanel.mouseOut = false;
--~ 		local newView = ISTabPanel.viewDragging.view:new(0,0,ISTabPanel.viewDragging.view.width,ISTabPanel.viewDragging.height);
--~ 		local newView = ISCharacterInfo:new(0, 0, self.width, self.height-8);
		-- we start to remove the view from our tab panel
		self:removeChild(ISTabPanel.viewDragging.view);
		local newWindow = ISCollapsableWindow:new(self:getMouseX() + self:getAbsoluteX(), self:getMouseY() + self:getAbsoluteY(), ISTabPanel.viewDragging.view:getWidth(), ISTabPanel.viewDragging.view:getHeight());
		newWindow:initialise();
		newWindow:addToUIManager();
		newWindow:addView(ISTabPanel.viewDragging.view);
		newWindow:setTitle(ISTabPanel.viewDragging.name);
		local tornOff = ISTabPanel.viewDragging.view
		ISTabPanel.viewDragging = nil;
		local clickedView = nil;
		-- if we doesn't have any view anymore, we close this tab panel and his parent
		if #self.viewList == 0 then
			if self.parent then
				self.parent:setVisible(false);
				self.parent:setRemoved(true);
			end
			self:setVisible(false);
			self:removeFromUIManager();
		else
			for ind,value in ipairs(self.viewList) do
				-- we get the view we clicked on
				clickedView = value;
				break;
			end
			-- if we clicked on another view, we display it and make the previous one not visible
			if clickedView then
				clickedView.view:setVisible(true);
				self.activeView = clickedView;
			end
		end
		if self.tabTornOff ~= nil then
			self.tabTornOff(self.tabTornOffTarget, tornOff, newWindow)
		end
	else
		self.draggingTab = nil
		self.isDragging = false;
		ISTabPanel.xMouse = -1;
		ISTabPanel.yMouse = -1;
		ISTabPanel.mouseOut = false;
	end
end

ISTabPanel.redoTab = function(self)
	local newView = {};
	if ISTabPanel.fromOutside then
		-- we now remove our false view created when we mouse over this tab panel with our collapsable window
		local trueViewList = {};
		for i,viewObject in ipairs(self.viewList) do
			if viewObject.name ~= ISTabPanel.viewDragging.view:getTitle() then
				table.insert(trueViewList, viewObject);
			end
		end
		self.viewList = trueViewList;
--~ 		print(#self.viewList);
		-- we remove all the child view from the collapsable window and add them to our own tab panel
		for i,v in pairs(ISTabPanel.viewDragging.view:getViews()) do
			self:addChild(v);
			v:setY(self.y + self.tabHeight);
			v:setX(self.x);
			newView.view = v;
			newView.name = ISTabPanel.viewDragging.view:getTitle();
			table.insert(self.viewList, self:getTabIndexAtX(self:getMouseX()), newView);
--~ 			print(#self.viewList);
		end
		ISTabPanel.viewDragging.view:clearChildren();
		ISTabPanel.viewDragging.view:setVisible(false);
		ISTabPanel.viewDragging.view:removeFromUIManager();
	-- we re do all our tab in the list to display them in the new order we choose (by dragging a tab)
	else
		local newViewList = {};
		for i,viewObject in ipairs(self.viewList) do
			if i ~= (self.draggingTab + 1) then
				table.insert(newViewList, viewObject);
			else
				newView = viewObject;
			end
		end
		local tabIndex = self:getTabIndexAtX(self:getMouseX()) - 1
		if tabIndex >= 0 then
			tabIndex = math.min(tabIndex, #self.viewList - 1)
			table.insert(newViewList, tabIndex + 1, newView);
			self.viewList = newViewList;
		end
	end
	-- reset the dragging
	self.activeView.view:setVisible(false);
	newView.view:setVisible(true);
	self.activeView = newView;
	self.draggingTab = nil;
	ISTabPanel.xMouse = -1;
	ISTabPanel.yMouse = -1;
	self.isDragging = false;
	ISTabPanel.viewDragging = nil;
end

--************************************************************************--
--** ISTabPanel:onMouseUp
--**
--************************************************************************--
function ISTabPanel:onMouseUp(x, y)
	if self.isDragging then
		ISTabPanel.redoTab(self);
	else
		self.draggingTab = nil;
		ISTabPanel.xMouse = -1;
		ISTabPanel.yMouse = -1;
		self.isDragging = false;
		ISTabPanel.viewDragging = nil;
		-- when we click somewhere on our tab panel
		-- first test, do we click on the tab height ?
		if self:getMouseY() >= 0 and self:getMouseY() < self.tabHeight then
			if self:getScrollButtonAtX(self:getMouseX()) then
				return
			end
			-- we get the # of the clicked tab
			local tabNb = self:getTabIndexAtX(self:getMouseX());
			-- do we really click on a tab ?
			if tabNb <= #self.viewList then
				-- do we clicked on a different view than the current one ?
				local clickedView = nil;
				for ind,value in ipairs(self.viewList) do
					-- we get the view we clicked on
					if ind == tabNb then
						clickedView = value;
						break;
					end
				end
				-- if we clicked on another view, we display it and make the previous one not visible
				if clickedView and clickedView.name ~= self.activeView.name then
					self:activateView(clickedView.name)
				end
			end
		end
	end
end

function ISTabPanel:onMouseWheel(del)
	if self:isMouseOver() and self:getMouseY() < self.tabHeight then
		if not self.smoothScrollTargetX then self.smoothScrollX = self.scrollX end
		local inset = 1
		local scrollIndex = self:getTabIndexAtX(inset, self.smoothScrollTargetX or self.scrollX)
		self.smoothScrollTargetX = 0
		if scrollIndex == -1 then return true end
		if del > 0 then
			scrollIndex = scrollIndex + 1
		else
			if scrollIndex > 1 then
				scrollIndex = scrollIndex - 1
			end
		end
		local gap = 1
		local maxXScroll = self:getWidthOfAllTabs() - (self:getWidth() - inset * 2)
		for ind,view in ipairs(self.viewList) do
			if scrollIndex > ind then
				local tabWidth = self.equalTabWidth and self.maxLength or view.tabWidth
				self.smoothScrollTargetX = self.smoothScrollTargetX - tabWidth - gap
				if self.smoothScrollTargetX <= -maxXScroll then
					self.smoothScrollTargetX = -maxXScroll
					break
				end
			end
		end
		return true
	end
	return false
end

function ISTabPanel:getView(viewName)
	for ind,value in ipairs(self.viewList) do
		-- we get the view we want to display
		if value.name == viewName then
			return value.view;
		end
	end
	return nil;
end

function ISTabPanel:activateView(viewName)
	for ind,value in ipairs(self.viewList) do
		-- we get the view we want to display
		if value.name == viewName then
			self.activeView.view:setVisible(false);
			value.view:setVisible(true);
			self.activeView = value;
			self:ensureVisible(ind)

			if self.parent and self.parent.infoButton then
				if self.activeView.view.infoText then
					self.parent:setInfo(self.activeView.view.infoText)
				else
					self.parent:setInfo(nil)
				end
			end

			if self.onActivateView and self.target then
				self.onActivateView(self.target, self);
			end

			return true;
		end
	end
	return false;
	-- if we clicked on another view, we display it and make the previous one not visible
--~ 	if clickedView and clickedView.name ~= self.activeView.name then
--~ 		self.activeView.view:setVisible(false);
--~ 		clickedView.view:setVisible(true);
--~ 		self.activeView = clickedView;
--~ 	end
end

function ISTabPanel:getActiveView()
	if self.activeView then
		return self.activeView.view
	end
end

function ISTabPanel:getActiveViewIndex()
	if self.activeView then
		for index,value in ipairs(self.viewList) do
			if value == self.activeView then
				return index
			end
		end
	end
	return nil
end

-- add a view to our tab panel
function ISTabPanel:addView(name, view)
	local viewObject = {};
	viewObject.name = name;
	viewObject.view = view;
	viewObject.tabWidth = getTextManager():MeasureStringX(UIFont.Small, name) + self.tabPadX;
	viewObject.fade = UITransition.new()
	table.insert(self.viewList, viewObject);
	-- the view have to be under our tab
	view:setY(self.tabHeight);
--	view:initialise();
	self:addChild(view);
	view.parent = self;
	-- the 1st view will be default visible
	if #self.viewList == 1 then
		view:setVisible(true);
		self.activeView = viewObject;
		self.maxLength = viewObject.tabWidth;
	else
		view:setVisible(false);
		if viewObject.tabWidth > self.maxLength then
			self.maxLength = viewObject.tabWidth;
		end
	end
end

function ISTabPanel:removeView(view)
	local newViewList = {};
	for _,viewObject in ipairs(self.viewList) do
		if viewObject.view ~= view then
			table.insert(newViewList, viewObject);
		end
	end
	if self.activeView and self.activeView.view == view then
		self.activeView = newViewList[1]
	end
	self.viewList = newViewList;
	self:removeChild(view);
end

function ISTabPanel:setEqualTabWidth(equal)
	self.equalTabWidth = equal
end

function ISTabPanel:setCenterTabs(center)
	self.centerTabs = center
end

function ISTabPanel:getWidthOfAllTabs()
	local gap = 1
	local width = (#self.viewList - 1) * gap
	if self.equalTabWidth then
		return width + #self.viewList * self.maxLength
	end
	for _,viewObject in ipairs(self.viewList) do
		width = width + viewObject.tabWidth
	end
	return width
end

function ISTabPanel:getTabX(tabIndex, scrollX)
	if tabIndex < 1 or tabIndex > #self.viewList then
		return -1
	end
	local inset = 1
	local gap = 1
	local left = inset
	if self.centerTabs and (self:getWidth() >= self:getWidthOfAllTabs()) then
		left = (self:getWidth() - self:getWidthOfAllTabs()) / 2
	else
		left = left + (scrollX or self.scrollX)
	end
	for i=1,tabIndex-1 do
		local viewObject = self.viewList[i]
		local tabWidth = self.equalTabWidth and self.maxLength or viewObject.tabWidth
		left = left + tabWidth + gap
	end
	return left
end

function ISTabPanel:getTabIndexAtX(x, scrollX)
	local inset = 1
	local gap = 1
	local left = inset
	if self.centerTabs and (self:getWidth() >= self:getWidthOfAllTabs()) then
		left = (self:getWidth() - self:getWidthOfAllTabs()) / 2
	else
		left = left + (scrollX or self.scrollX)
	end
	for index,viewObject in ipairs(self.viewList) do
		local tabWidth = self.equalTabWidth and self.maxLength or viewObject.tabWidth
		if x >= left and x < left + tabWidth + gap then
			return index
		end
		left = left + tabWidth + gap
	end
	return -1
end

function ISTabPanel:getScrollButtonAtX(x)
	local inset = 1
	local butPadX = 3
	local butWid = butPadX + getTexture("media/ui/ArrowRight.png"):getWidth() + butPadX
	local overflowLeft = self.scrollX < 0
	local overflowRight = inset + self.scrollX + self:getWidthOfAllTabs() > self.width
	if overflowLeft and x < inset + butWid then
		return "left"
	end
	if overflowRight and x >= self.width - inset - butWid then
		return "right"
	end
	return nil
end

function ISTabPanel:setOnTabTornOff(target, method)
	self.tabTornOffTarget = target
	self.tabTornOff = method
end

function ISTabPanel:new (x, y, width, height)
	local o = {};
	o = ISPanel:new(x, y, width, height);
	setmetatable(o, self);
	self.__index = self;
	o.x = x;
	o.y = y;
	o.backgroundColor = {r=0, g=0, b=0, a=0};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.tabTransparency = 1.0;
    o.textTransparency = 1.0;
	o.width = width;
	o.height = height;
	o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;
	o.viewList = {};
	o.activeView = nil;
    o.blinkTabs = {};
	o:noBackground();
	ISTabPanel.tabSelected = getTexture("media/ui/XpSystemUI/tab_selected.png");
	ISTabPanel.tabUnSelected = getTexture("media/ui/XpSystemUI/tab_unselected.png");
	o.draggingTab = nil;
	o.isDragging = false
--~ 	o.parent = nil;
	o.tabTornOffTarget = nil
	o.tabTornOff = nil
	o.maxLength = 0
	o.tabHeight = getTextManager():getFontHeight(UIFont.Small) + 6
	o.tabPadX = 20
	o.allowDraggingTabs = false
	o.allowTornOffTabs = false
	o.equalTabWidth = true
	o.centerTabs = false
	o.scrollX = 0
	return o;
end


	ISTabPanel.tabSelected = getTexture("media/ui/XpSystemUI/tab_selected.png");
	ISTabPanel.tabUnSelected = getTexture("media/ui/XpSystemUI/tab_unselected.png");

