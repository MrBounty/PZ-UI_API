--***********************************************************
--**                    ROBERT JOHNSON                     **
--**              Panel wich display all our skills        **
--***********************************************************

require "ISUI/ISPanelJoypad"

---@class ISChallenge2WeaponRepairWindow : ISPanelJoypad
ISChallenge2WeaponRepairWindow = ISPanelJoypad:derive("ISChallenge2WeaponRepairWindow");


--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISChallenge2WeaponRepairWindow:initialise()
	ISPanelJoypad.initialise(self);
	self:create();
end

function ISChallenge2WeaponRepairWindow:render()
	local y = 42;

	self:drawText(self.char:getDescriptor():getForename().." "..self.char:getDescriptor():getSurname(), 20, y, 1,1,1,1, UIFont.Medium);
	y = y + 25;
	self:drawText(getText("Challenge_Challenge2_Money", Challenge2.playersMoney[self.playerId]), 20, y, 1,1,1,1, UIFont.Small);
end

function ISChallenge2WeaponRepairWindow:create()
	local y = 90;

	local label = ISLabel:new(16, y, 20, getText("Challenge_Challenge2_RepairWeapon"), 1, 1, 1, 0.8, UIFont.Small, true);
	self:addChild(label);

	local rect = ISRect:new(16, y + 20, 230, 1, 0.6, 0.6, 0.6, 0.6);
	self:addChild(rect);
end

function ISChallenge2WeaponRepairWindow:onOptionMouseDown(button, x, y)
	-- manage the item
	if button.internal == "repair" then
		button.item:setCondition(button.item:getConditionMax());
		Challenge2.playersMoney[self.playerId] = Challenge2.playersMoney[self.playerId] - button.cost;
	end
	ISChallenge2UpgradeTab.instance[self.playerId]:reloadButtons();
end

function ISChallenge2WeaponRepairWindow:reloadButtons()
	-- first we remove every buttons
	for i,v in ipairs(self.buttons) do
		self:removeChild(v);
		v:removeFromUIManager();
	end
	self.buttons = {};


	-- fetch all the item in the player inventory to find weapon with a condition under 1
	for i=0, self.char:getInventory():getItems():size() - 1 do
		local item = self.char:getInventory():getItems():get(i);
		if instanceof(item, "HandWeapon") then -- found a weapon slightly damaged
			-- calcul the money needed to repair it
			local cost = math.ceil((100 - item:getCurrentCondition()) * 2);
			-- add a new button
			local button = ISButton:new(16, 90 + ((#self.buttons + 1) * 30), 100, 25, getText("Challenge_Challenge2_ItemButton", item:getName(), cost), self, ISChallenge2WeaponRepairWindow.onOptionMouseDown);
			button.internal = "repair";
			button.item = item;
			button.cost = cost;
			button:initialise();
			button:instantiate();
			button.borderColor = {r=1, g=1, b=1, a=0.1};

			button:setFont(UIFont.Small);
			button:ignoreWidthChange();
			button:ignoreHeightChange();
			self:addChild(button);
			table.insert(self.buttons, button);

			-- disable this button if the condition is ok or if you don't have enough money
			if item:getCurrentCondition() == 100 or Challenge2.playersMoney[self.playerId] < button.cost then
				button:setEnable(false);
			end
		end
	end

	self:loadJoypadButtons()
end

function ISChallenge2WeaponRepairWindow:loadJoypadButtons()
	self:clearJoypadFocus()
	self.joypadButtonsY = {}
	for n = 1,#self.buttons do
		self:insertNewLineOfButtons(self.buttons[n])
	end
	if #self.buttons > 0 then
		self.joypadIndex = 1
		self.joypadIndexY = 1
		self.joypadButtons = self.joypadButtonsY[self.joypadIndexY]
		self.joypadButtons[self.joypadIndex]:setJoypadFocused(true)
	end
end

function ISChallenge2WeaponRepairWindow:onJoypadDown(button, joypadData)
	if button == Joypad.AButton then
		ISPanelJoypad.onJoypadDown(self, button, joypadData)
	end
	if button == Joypad.BButton then
		ISChallenge2UpgradeTab.instance[self.playerId]:setVisible(false)
		joypadData.focus = nil
	end
	if button == Joypad.LBumper then
		ISChallenge2UpgradeTab.instance[self.playerId]:onJoypadDown(button, joypadData)
	end
	if button == Joypad.RBumper then
		ISChallenge2UpgradeTab.instance[self.playerId]:onJoypadDown(button, joypadData)
	end
end

function ISChallenge2WeaponRepairWindow:new(x, y, width, height, player)
	local o = {};
	o = ISPanelJoypad:new(x, y, width, height);
	o:noBackground();
	setmetatable(o, self);
    self.__index = self;
	o.char = getSpecificPlayer(player);
	o.playerId = player;
	o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
	o.backgroundColor = {r=0, g=0, b=0, a=0.8};
	o.buttons = {};
   return o;
end
