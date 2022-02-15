--***********************************************************
--**                      ROBERT JOHNSON                         **
--** Panel with all the character information (skills, health..) **
--***********************************************************

require "ISUI/ISCollapsableWindow"
require "ISUI/ISLayoutManager"

ISCharacterInfoWindow = ISCollapsableWindow:derive("ISCharacterInfoWindow");
ISCharacterInfoWindow.view = {};

function ISCharacterInfoWindow:initialise()
	ISCollapsableWindow.initialise(self);
end

--~ function ISCharacterInfoWindow:setVisible(bVisible)
--~ 	if not bVisible then
--~ 		self.healthView:setVisible(bVisible);
--~ 		self.charScreen:setVisible(bVisible);
--~ 		self.characterView:setVisible(bVisible);
--~ 	end
--~ 	self.javaObject:setVisible(bVisible);
--~ end

function ISCharacterInfoWindow:isActive(viewName)
	-- first test, is the view still inside our tab panel ?
	for ind,value in ipairs(self.panel.viewList) do
		-- we get the view we want to display
		if value.name == viewName then
			return value.view:getIsVisible() and self:getIsVisible();
		end
	end
	-- if not (if we dragged it outside our tab panel), we look for it
	for i,v in pairs(ISCharacterInfoWindow.view) do
		if v:getTitle() == viewName then
			return v:getIsVisible();
		end
	end
	return false;
end


function ISCharacterInfoWindow:toggleView(viewName)
	-- if we haven't found our view in the tab panel, it's because someone dragged it outside
--~ 	if not self.panel:activateView(viewName) then
--~ 		for i,v in pairs(ISCharacterInfoWindow.view) do
--~ 			if v.name == viewName then
--~ 				print("found view : " .. v.name .. " visible ");
--~ 				print(v.view:getIsVisible());
--~ 				v.view:setVisible(not v.view:getIsVisible());

--~ 			end
--~ 		end
--~ 	else

--~ 	end
	local view = self.panel:getView(viewName);
	if view then
        if view.infoText then
           self:setInfo(view.infoText);
        else
            self:setInfo(nil);
        end
		if self:getIsVisible() then
			if view == self.panel:getActiveView() then
				self:close()
			else
				self.panel:activateView(viewName)
			end
		else
			self.panel:activateView(viewName)
			self:setVisible(true)
			self:addToUIManager()
		end
	else
		for i,v in pairs(ISCharacterInfoWindow.view) do
			if v:getTitle() == viewName then
				v:setVisible(not v:getIsVisible())
			end
		end
	end
end

function ISCharacterInfoWindow:onJoypadDown(button)
	if button == Joypad.LBumper or button == Joypad.RBumper then
		if #self.panel.viewList < 2 then return end
		local viewIndex = self.panel:getActiveViewIndex()
		if button == Joypad.LBumper then
			if viewIndex == 1 then
				viewIndex = #self.panel.viewList
			else
				viewIndex = viewIndex - 1
			end
		end
		if button == Joypad.RBumper then
			if viewIndex == #self.panel.viewList then
				viewIndex = 1
			else
				viewIndex = viewIndex + 1
			end
		end
		self.panel:activateView(self.panel.viewList[viewIndex].name)
		setJoypadFocus(self.playerNum, self.panel:getActiveView())
	end
end

function ISCharacterInfoWindow:createChildren()
	ISCollapsableWindow.createChildren(self);
	local th = self:titleBarHeight()
	local rh = self:resizeWidgetHeight()
	self.panel = ISTabPanel:new(0, th, self.width, self.height-th-rh);
	self.panel:initialise();
    self.panel.tabPadX = 15;
    self.panel.equalTabWidth = false;
--~ 	self.panel.allowDraggingTab = false;
	self:addChild(self.panel);
	self.panel:setOnTabTornOff(self, ISCharacterInfoWindow.onTabTornOff)

	self.charScreen = ISCharacterScreen:new(0, 8, 420, 250, self.playerNum);
	self.charScreen:initialise()
	self.panel:addView(xpSystemText.info, self.charScreen)

	self.characterView = ISCharacterInfo:new(0, 8, self.width, self.height-8, self.playerNum);
	self.characterView:initialise()
    self.characterView.infoText = getText("UI_SkillPanel");
	self.panel:addView(xpSystemText.skills, self.characterView)

	self.healthView = ISHealthPanel:new(getSpecificPlayer(self.playerNum), 0, 8, self.width, self.height-8)
	self.healthView:initialise()
    self.healthView.infoText = getText("UI_HealthPanel");
	self.panel:addView(xpSystemText.health, self.healthView)
	
	self.protectionView = ISCharacterProtection:new(0, 8, self.width, self.height-8, self.playerNum)
	self.protectionView:initialise()
	self.protectionView.infoText = getText("UI_ProtectionPanel");
	self.panel:addView(xpSystemText.protection, self.protectionView)

    self.clothingView = ISClothingInsPanel:new(getSpecificPlayer(self.playerNum), 0, 8, self.width, self.height-8)
    self.clothingView:initialise()
    self.clothingView.infoText = getText("UI_ClothingInsPanel");
    self.panel:addView(xpSystemText.clothingIns, self.clothingView)

	-- Set the correct size before restoring the layout.  Currently, ISCharacterScreen:render sets the height/width.
	self:setWidth(self.charScreen.width)
	self:setHeight(self.charScreen.height);
	if self.playerNum == 0 then
		ISLayoutManager.RegisterWindow('charinfowindow', ISCharacterInfoWindow, self)
	end
    self.visibleOnStartup = self:getIsVisible() -- hack, see ISPlayerDataObject.lua
    if getCore():getGameMode() == "Tutorial" then self:setVisible(false); end
end

function ISCharacterInfoWindow:render()
	ISCollapsableWindow.render(self)

	if JoypadState.players[self.playerNum+1] then
		for _,view in pairs(self.panel.viewList) do
			if JoypadState.players[self.playerNum+1].focus == view.view then
				self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.4, 0.2, 1.0, 1.0);
				self:drawRectBorder(1, 1, self:getWidth()-2, self:getHeight()-2, 0.4, 0.2, 1.0, 1.0);
				break
			end
		end
	end
end

function ISCharacterInfoWindow:close()
	self:setVisible(false)
	self:removeFromUIManager() -- so update() isn't called
	if JoypadState.players[self.playerNum+1] then
		setJoypadFocus(self.playerNum, nil)
	end
end

function ISCharacterInfoWindow:onTabTornOff(view, window)
	table.insert(ISCharacterInfoWindow.view, window)
	if self.playerNum == 0 and view == self.charScreen then
		ISLayoutManager.RegisterWindow('charinfowindow.info', ISCollapsableWindow, window)
	end
	if self.playerNum == 0 and view == self.characterView then
		ISLayoutManager.RegisterWindow('charinfowindow.skills', ISCollapsableWindow, window)
	end
	if self.playerNum == 0 and view == self.healthView then
		ISLayoutManager.RegisterWindow('charinfowindow.health', ISCollapsableWindow, window)
    end
    if self.playerNum == 0 and view == self.clothingView then
        ISLayoutManager.RegisterWindow('charinfowindow.clothingIns', ISCollapsableWindow, window)
    end
	if self.playerNum == 0 and view == self.protectionView then
		ISLayoutManager.RegisterWindow('charinfowindow.protection', ISCollapsableWindow, window)
	end
	window:setResizable(false)
end

function ISCharacterInfoWindow:RestoreLayout(name, layout)
    ISLayoutManager.DefaultRestoreWindow(self, layout)
	local floating = { info = true, skills = true, health = true, protection = true, clothingIns = true  }
    if layout.tabs ~= nil then
		local tabs = string.split(layout.tabs, ',')
		for k,v in pairs(tabs) do
			if v == 'info' then
				floating.info = false
			elseif v == 'skills' then
				floating.skills = false
			elseif v == 'health' then
				floating.health = false
			elseif v == 'protection' then
				floating.protection = false
            elseif v == 'clothingIns' then
                floating.clothingIns = false
			end		end
	else
		floating.info = false
		floating.skills = false
		floating.health = false
		floating.protection = false
        floating.clothingIns = false	end
	if floating.info then
		self.panel:removeView(self.charScreen)
		local newWindow = ISCollapsableWindow:new(0, 0, self.charScreen:getWidth(), self.charScreen:getHeight());
		newWindow:initialise();
		newWindow:addToUIManager();
		newWindow:addView(self.charScreen);
		newWindow:setTitle(xpSystemText.info);
		self:onTabTornOff(self.charScreen, newWindow)
	end
	if floating.skills then
		self.panel:removeView(self.characterView)
		-- ISCharacterInfo:render() sets the desired window size, BIG CHEAT to get it now 
		local width = self.characterView.txtLen + 180
		local height = (110 + PerkFactory.PerkList:size() * 20) + 8
		local newWindow = ISCollapsableWindow:new(0, 0, width, height);
		newWindow:initialise();
		newWindow:addToUIManager();
		newWindow:addView(self.characterView);
		newWindow:setTitle(xpSystemText.skills);
		self:onTabTornOff(self.characterView, newWindow)
	end
	if floating.health then
		self.panel:removeView(self.healthView)
		-- ISHealthPanel:render() sets the desired window size, BIG CHEAT to get it now 
		local width = self.healthView.healthPanel:getWidth()
		local height = self.healthView.healthPanel:getHeight() + 30
		local newWindow = ISCollapsableWindow:new(0, 0, width, height);
		newWindow:initialise();
		newWindow:addToUIManager();
		newWindow:addView(self.healthView);
		newWindow:setTitle(xpSystemText.health);
		self:onTabTornOff(self.healthView, newWindow)
    end
    if floating.clothingIns and false then
        self.panel:removeView(self.clothingView)
        -- ISHealthPanel:render() sets the desired window size, BIG CHEAT to get it now
        local width = self.clothingView:getWidth()
        local height = self.clothingView:getHeight() + 30
        local newWindow = ISCollapsableWindow:new(0, 0, width, height);
        newWindow:initialise();
        newWindow:addToUIManager();
        newWindow:addView(self.clothingView);
        newWindow:setTitle(xpSystemText.clothingIns);
        self:onTabTornOff(self.clothingView, newWindow)
    end
	if floating.porotection then
		self.panel:removeView(self.protectionView)
		local newWindow = ISCollapsableWindow:new(0, 0, self.protectionView:getWidth(), self.protectionView:getHeight());
		newWindow:initialise();
		newWindow:addToUIManager();
		newWindow:addView(self.protectionView);
		newWindow:setTitle(xpSystemText.protection);
		self:onTabTornOff(self.protectionView, newWindow)
	end
	if layout.current and not floating[layout.current] then
		self.panel:activateView(xpSystemText[layout.current])
	end
end

function ISCharacterInfoWindow:SaveLayout(name, layout)
    ISLayoutManager.DefaultSaveWindow(self, layout)
    layout.width = nil
    layout.height = nil
    layout.current = nil
	local tabs = {}
	if self.charScreen.parent == self.panel then
		table.insert(tabs, 'info')
		if self.charScreen == self.panel:getActiveView() then
			layout.current = 'info'
		end
	end
	if self.characterView.parent == self.panel then
		table.insert(tabs, 'skills')
		if self.characterView == self.panel:getActiveView() then
			layout.current = 'skills'
		end
	end
	if self.healthView.parent == self.panel then
		table.insert(tabs, 'health')
		if self.healthView == self.panel:getActiveView() then
			layout.current = 'health'
		end
    end
    if self.clothingView.parent == self.panel then
        table.insert(tabs, 'clothingIns')
        if self.clothingView == self.panel:getActiveView() then
            layout.current = 'clothingIns'
        end
    end
	if self.protectionView.parent == self.panel then
		table.insert(tabs, 'protection')
		if self.protectionView == self.panel:getActiveView() then
			layout.current = 'protection'
		end
	end
	layout.tabs = table.concat(tabs, ',')
end

function ISCharacterInfoWindow:new (x, y, width, height, playerNum)
	local o = {};
	o = ISCollapsableWindow:new(x, y, width, height);
	setmetatable(o, self);
	self.__index = self;
--	o:noBackground();
	o:setResizable(false)
	o.visibleOnStartup = false
	o.playerNum = playerNum
	ISCharacterInfoWindow.instance = o;
	return o;
end

function ISCharacterInfoWindow.OnClothingUpdated(chr)
	if instanceof(chr, 'IsoPlayer') and chr:isLocalPlayer() and getPlayerInfoPanel(chr:getPlayerNum()) then
		getPlayerInfoPanel(chr:getPlayerNum()).charScreen.refreshNeeded = true
	end
end

Events.OnClothingUpdated.Add(ISCharacterInfoWindow.OnClothingUpdated)

