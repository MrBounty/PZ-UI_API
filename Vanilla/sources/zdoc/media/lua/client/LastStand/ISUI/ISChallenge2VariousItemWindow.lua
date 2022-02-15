--***********************************************************
--**                    ROBERT JOHNSON                     **
--**              Panel wich display all our skills        **
--***********************************************************

require "ISUI/ISPanelJoypad"

---@class ISChallenge2VariousItemWindow : ISPanelJoypad
ISChallenge2VariousItemWindow = ISPanelJoypad:derive("ISChallenge2VariousItemWindow");


--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISChallenge2VariousItemWindow:initialise()
	ISPanelJoypad.initialise(self);
	self:create();
end

function ISChallenge2VariousItemWindow:render()
	local y = 42;

	self:drawText(self.char:getDescriptor():getForename().." "..self.char:getDescriptor():getSurname(), 20, y, 1,1,1,1, UIFont.Medium);
	y = y + 25;
	self:drawText(getText("Challenge_Challenge2_Money", Challenge2.playersMoney[self.playerId]), 20, y, 1,1,1,1, UIFont.Small);
end

function ISChallenge2VariousItemWindow:create()
	local y = 90;

	local label = ISLabel:new(16, y, 20, getText("Challenge_Challenge2_Carpentry"), 1, 1, 1, 0.8, UIFont.Small, true);
	self:addChild(label);

	local rect = ISRect:new(16, y + 20, 230, 1, 0.6, 0.6, 0.6, 0.6);
	self:addChild(rect);

	y = y + 25;
	self:createItemButton(y, "Base.Hammer", 70)

	y = y + 30;
	self:createItemButton(y, "Base.Plank", 30)

	y = y + 30;
	self:createItemButton(y, "Base.Nails", 10)

	y = y + 30;
	self:createItemButton(y, "Base.Saw", 40)

	y = y + 30;

	local label = ISLabel:new(16, y, 20, getText("Challenge_Challenge2_Health"), 1, 1, 1, 0.8, UIFont.Small, true);
	self:addChild(label);

	local rect = ISRect:new(16, y + 20, 230, 1, 0.6, 0.6, 0.6, 0.6);
	self:addChild(rect);

	y = y + 25;
	self:createItemButton(y, "Base.RippedSheets", 20)

	y = y + 30;
	self:createItemButton(y, "Base.Pills", 20)

	y = y + 30;
	self:createItemButton(y, "Base.PillsBeta", 20)

	self:loadJoypadButtons()
end

function ISChallenge2VariousItemWindow:createItemButton(y, itemType, cost)
	local item = ScriptManager.instance:getItem(itemType)
	local label = nil
	if item:getCount() > 1 then
		label = getText("Challenge_Challenge2_ItemButton2", item:getDisplayName(), item:getCount(), cost)
	else
		label = getText("Challenge_Challenge2_ItemButton", item:getDisplayName(), cost)
	end
	local button = ISButton:new(16, y, 100, 25, label, self, ISChallenge2VariousItemWindow.onOptionMouseDown);
	button:initialise();
	button.internal = "item";
	button.item = itemType;
	button.cost = cost;
	button.borderColor = {r=1, g=1, b=1, a=0.1};
	button:setFont(UIFont.Small);
	button:ignoreWidthChange();
	button:ignoreHeightChange();
	self:addChild(button);
	table.insert(self.buttons, button);
end

function ISChallenge2VariousItemWindow:onOptionMouseDown(button, x, y)
	-- manage the item
	if button.internal == "item" then
		Challenge2.playersMoney[self.playerId] = Challenge2.playersMoney[self.playerId] - button.cost;
		self.char:getInventory():AddItem(button.item);
	end
	ISChallenge2UpgradeTab.instance[self.playerId]:reloadButtons();
end

function ISChallenge2VariousItemWindow:reloadButtons()
	for i,v in ipairs(self.buttons) do
		if Challenge2.playersMoney[self.playerId] < v.cost then
			v:setEnable(false);
		else
			v:setEnable(true);
		end
	end
end

function ISChallenge2VariousItemWindow:loadJoypadButtons()
	self:clearJoypadFocus()
	self.joypadButtonsY = {}
	for n = 1,#self.buttons do
		self:insertNewLineOfButtons(self.buttons[n])
	end
	self.joypadIndex = 1
	self.joypadIndexY = 1
	self.joypadButtons = self.joypadButtonsY[self.joypadIndexY]
	self.joypadButtons[self.joypadIndex]:setJoypadFocused(true)
end

function ISChallenge2VariousItemWindow:onJoypadDown(button, joypadData)
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

function ISChallenge2VariousItemWindow:new(x, y, width, height, player)
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
