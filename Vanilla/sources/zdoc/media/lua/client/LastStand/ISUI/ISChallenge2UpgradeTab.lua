--***********************************************************
--**                      ROBERT JOHNSON                         **
--** Panel with all the character information (skills, health..) **
--***********************************************************

require "ISUI/ISCollapsableWindow"

---@class ISChallenge2UpgradeTab : ISCollapsableWindow
ISChallenge2UpgradeTab = ISCollapsableWindow:derive("ISChallenge2UpgradeTab");
ISChallenge2UpgradeTab.instance = {}

function ISChallenge2UpgradeTab:initialise()
	ISCollapsableWindow.initialise(self);
end

function ISChallenge2UpgradeTab:createChildren()
	ISCollapsableWindow.createChildren(self);
	local th = self:titleBarHeight()
	local rh = self:resizeWidgetHeight()
	self.panel = ISTabPanel:new(0, th, self.width, self.height-th-rh);
	self.panel:initialise();
	self:addChild(self.panel);

	self.playerScreen = ISChallenge2PlayerUpWindow:new(0, 8, 320, 600, self.playerId);
	self.playerScreen:initialise();
	self.panel:addView(getText("Challenge_Challenge2_TabPlayer"), self.playerScreen);

	self.weaponScreen = ISChallenge2WeaponUpWindow:new(0, 8, 320, 600, self.playerId);
	self.weaponScreen:initialise();
	self.panel:addView(getText("Challenge_Challenge2_Weapons"), self.weaponScreen);

	self.itemScreen = ISChallenge2VariousItemWindow:new(0, 8, 320, 600, self.playerId);
	self.itemScreen:initialise();
	self.panel:addView(getText("Challenge_Challenge2_TabItems"), self.itemScreen);

	self.repairScreen = ISChallenge2WeaponRepairWindow:new(0, 8, 320, 600, self.playerId);
	self.repairScreen:initialise();
	self.panel:addView(getText("Challenge_Challenge2_TabRepair"), self.repairScreen);

	self.playerScreen:setVisible(true);
end

function ISChallenge2UpgradeTab:render()
	ISCollapsableWindow.render(self)

	if JoypadState.players[self.playerId+1] then
		self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), 0.4, 0.2, 1.0, 1.0);
		self:drawRectBorder(1, 1, self:getWidth()-2, self:getHeight()-2, 0.4, 0.2, 1.0, 1.0);
	end
end

function ISChallenge2UpgradeTab:reloadButtons()
	self.playerScreen:reloadButtons();
	self.weaponScreen:reloadButtons();
	self.itemScreen:reloadButtons();
	self.repairScreen:reloadButtons();
end

function ISChallenge2UpgradeTab:onGainJoypadFocus(joypadData)
	ISCollapsableWindow.onGainJoypadFocus(self, joypadData)
	joypadData.focus = self.panel:getActiveView()
end

function ISChallenge2UpgradeTab:onJoypadDown(button, joypadData)
	if button == Joypad.LBumper or button == Joypad.RBumper then
		if #self.panel.viewList < 2 then return end
		local viewIndex
		for i,v in ipairs(self.panel.viewList) do
			if v.view == self.panel:getActiveView() then
				viewIndex = i
				break
			end
		end
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
--		setJoypadFocus(self.playerId, self.panel:getActiveView())
		joypadData.focus = self.panel:getActiveView()
	end
end

function ISChallenge2UpgradeTab:close()
	ISCollapsableWindow.close(self)
	if JoypadState.players[self.playerId+1] then
		setJoypadFocus(self.playerId, nil)
	end
end

function ISChallenge2UpgradeTab:new (x, y, width, height, player)
	local o = {};
	o = ISCollapsableWindow:new(x, y, width, height);
	setmetatable(o, self);
	self.__index = self;
--	o:noBackground();
	o:setTitle(getText("Challenge_Challenge2_WindowTitle"))
	o.playerId = player;
	ISChallenge2UpgradeTab.instance[player] = o;
	return o;
end



