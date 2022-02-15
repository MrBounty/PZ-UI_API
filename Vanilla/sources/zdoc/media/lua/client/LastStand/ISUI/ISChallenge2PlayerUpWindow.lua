--***********************************************************
--**                    ROBERT JOHNSON                     **
--**              Panel wich display all our skills        **
--***********************************************************

require "ISUI/ISPanelJoypad"

---@class ISChallenge2PlayerUpWindow : ISPanelJoypad
ISChallenge2PlayerUpWindow = ISPanelJoypad:derive("ISChallenge2PlayerUpWindow");


--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISChallenge2PlayerUpWindow:initialise()
	ISPanelJoypad.initialise(self);
	self:create();
end

function ISChallenge2PlayerUpWindow:render()
	local y = 42;

	self:drawText(self.char:getDescriptor():getForename().." "..self.char:getDescriptor():getSurname(), 20, y, 1,1,1,1, UIFont.Medium);
	y = y + 25;
	self:drawText(getText("Challenge_Challenge2_CurrentXp", self.char:getModData()["challenge2Xp"]), 20, y, 1,1,1,1, UIFont.Small);
end

function ISChallenge2PlayerUpWindow:create()
	local y = 90;

	local label = ISLabel:new(16, y, 20, getText("Challenge_Challenge2_Skills"), 1, 1, 1, 0.8, UIFont.Small, true);
	self:addChild(label);

	local rect = ISRect:new(16, y + 20, 230, 1, 0.6, 0.6, 0.6, 0.6);
	self:addChild(rect);

	y = y + 25;
	button = ISButton:new(16, y, 200, 25, "Blunt Lvl 1 - 0xp", self, ISChallenge2PlayerUpWindow.onOptionMouseDown);
	button.internal = "skills";
	button.perk = Perks.Blunt;
	button.initialCost = 300;
	button.cost = 30;
	button:initialise();
	button:instantiate();
	button.borderColor = {r=1, g=1, b=1, a=0.1};

	button:setFont(UIFont.Small);
	button:ignoreWidthChange();
	button:ignoreHeightChange();
	self:addChild(button);
	table.insert(self.buttons, button);

	y = y + 30;
	button = ISButton:new(16, y, 200, 25, "Blade Lvl 1 - 0xp", self, ISChallenge2PlayerUpWindow.onOptionMouseDown);
	button.internal = "skills";
	button.perk = Perks.Axe;
	button.initialCost = 300;
	button.cost = 30;
	button:initialise();
	button:instantiate();
	button.borderColor = {r=1, g=1, b=1, a=0.1};

	button:setFont(UIFont.Small);
	button:ignoreWidthChange();
	button:ignoreHeightChange();
	self:addChild(button);
	table.insert(self.buttons, button);

	y = y + 30;
	button = ISButton:new(16, y, 200, 25, "Carpentry - 0xp", self, ISChallenge2PlayerUpWindow.onOptionMouseDown);
	button.internal = "skills";
	button.perk = Perks.Woodwork;
	button.initialCost = 300;
	button.cost = 30;
	button:initialise();
	button:instantiate();
	button.borderColor = {r=1, g=1, b=1, a=0.1};

	button:setFont(UIFont.Small);
	button:ignoreWidthChange();
	button:ignoreHeightChange();
	self:addChild(button);
	table.insert(self.buttons, button);

	y = y + 30;

	local label = ISLabel:new(16, y, 20, getText("Challenge_Challenge2_PermanentBonus"), 1, 1, 1, 0.8, UIFont.Small, true);
	self:addChild(label);

	local rect = ISRect:new(16, y + 20, 230, 1, 0.6, 0.6, 0.6, 0.6);
	self:addChild(rect);


	y = y + 25;
	button = ISButton:new(16, y, 200, 25, "5% Gold gain Bonus - 300xp", self, ISChallenge2PlayerUpWindow.onOptionMouseDown);
	button.internal = "goldBonus";
	button.xln = getText("Challenge_Challenge2_GoldBonus")
	button.initialCost = 1000;
	button.cost = 30;
	button.level = 1;
	button:initialise();
	button:instantiate();
	button.borderColor = {r=1, g=1, b=1, a=0.1};

	button:setFont(UIFont.Small);
	button:ignoreWidthChange();
	button:ignoreHeightChange();
	self:addChild(button);
	table.insert(self.buttons, button);

	y = y + 30;
	button = ISButton:new(16, y, 200, 25, "+50 Starting Gold - 300xp", self, ISChallenge2PlayerUpWindow.onOptionMouseDown);
	button.internal = "startingGoldBonus";
	button.initialCost = 1000;
	button.cost = 30;
	button.level = 1;
	button:initialise();
	button:instantiate();
	button.borderColor = {r=1, g=1, b=1, a=0.1};

	button:setFont(UIFont.Small);
	button:ignoreWidthChange();
	button:ignoreHeightChange();
	self:addChild(button);
	table.insert(self.buttons, button);

	y = y + 30;
	button = ISButton:new(16, y, 200, 25, "5% Xp gain Bonus - 300xp", self, ISChallenge2PlayerUpWindow.onOptionMouseDown);
	button.internal = "xpBonus";
	button.xln = getText("Challenge_Challenge2_XpBonus")
	button.initialCost = 1000;
	button.cost = 30;
	button.level = 1;
	button:initialise();
	button:instantiate();
	button.borderColor = {r=1, g=1, b=1, a=0.1};

	button:setFont(UIFont.Small);
	button:ignoreWidthChange();
	button:ignoreHeightChange();
	self:addChild(button);
	table.insert(self.buttons, button);

	self:updateButtonLevel();
end

-- update the level of the differents bonus level, depending on your current bonus
function ISChallenge2PlayerUpWindow:updateButtonLevel()
	local playerObj = getSpecificPlayer(self.playerId)
	local modData = playerObj:getModData()
	for i,v in ipairs(self.buttons) do
		if v.internal == "goldBonus" then
			if tonumber(modData.challenge2BoostGoldLevel) > 1 then
				v.level = (tonumber(modData.challenge2BoostGoldLevel) / 5) + 1;
			end
		end
		if v.internal == "xpBonus" then
			if tonumber(modData.challenge2BoostXpLevel) > 1 then
				v.level = (tonumber(modData.challenge2BoostXpLevel) / 5) + 1;
			end
		end
		if v.internal == "startingGoldBonus" then
			if tonumber(modData.challenge2StartingGoldLevel) > 1 then
				v.level = (tonumber(modData.challenge2StartingGoldLevel) / 20) + 1;
			end
		end
	end
end

function ISChallenge2PlayerUpWindow:onOptionMouseDown(button, x, y)
	local playerObj = self.char
	-- manage the item
	playerObj:getModData()["challenge2Xp"] = playerObj:getModData()["challenge2Xp"] - button.cost;
	if button.internal == "skills" then
		-- we add the xp for this skill, so the xp panel will be updated
		playerObj:LevelPerk(button.perk);
		luautils.updatePerksXp(button.perk, playerObj);
	end
	if button.internal == "goldBonus" then
		playerObj:getModData()["challenge2BoostGoldLevel"] = button.level * 5;
		button.level = button.level + 1;
	end
	if button.internal == "xpBonus" then
		playerObj:getModData()["challenge2BoostXpLevel"] = button.level * 5;
		button.level = button.level + 1;
	end
	if button.internal == "startingGoldBonus" then
		playerObj:getModData()["challenge2StartingGoldLevel"] = button.level * 20;
		button.level = button.level + 1;
	end
	self:reloadButtons();
	saveLastStandPlayerInFile(playerObj);
end

function ISChallenge2PlayerUpWindow:reloadButtons()
	local playerObj = self.char
	for i,v in ipairs(self.buttons) do
		if v.internal == "skills" then
			-- re-calcul the amount of xp needed to upgrade this skill
			local perks = v.perk
			local skillName = PerkFactory.getPerkName(perks);
			v.cost = (playerObj:getPerkLevel(v.perk) + 1) * v.initialCost;
			if (playerObj:getPerkLevel(v.perk) + 1) <= 10 then
				v:setTitle(getText("Challenge_Challenge2_ButtonSkill", skillName, playerObj:getPerkLevel(v.perk) + 1, v.cost));
			else
				v:setTitle(getText("Challenge_Challenge2_ButtonSkillMax", skillName));
			end
			if tonumber(playerObj:getModData()["challenge2Xp"]) < v.cost or playerObj:getPerkLevel(v.perk) == 10 then
				v:setEnable(false);
			else
				v:setEnable(true);
			end
		end
		if (v.internal == "goldBonus" or v.internal == "xpBonus") and not luautils.stringStarts(v:getTitle(), "Max") then
			-- recalcul the % bonus + the cost of this bonus
			local bonusName = v.xln;
			v.cost = v.level * v.initialCost;
			if v.level <= 5 then
				v:setTitle(getText("Challenge_Challenge2_ButtonBonus", v.level * 5, bonusName, v.cost));
			else
				v:setTitle(getText("Challenge_Challenge2_ButtonBonusMax", bonusName));
			end
			if tonumber(playerObj:getModData()["challenge2Xp"]) < v.cost or v.level > 5 then
				v:setEnable(false);
			else
				v:setEnable(true);
			end
		end
		if v.internal == "startingGoldBonus" and not luautils.stringStarts(v:getTitle(), "Max") then
			-- recalcul the % bonus + the cost of this bonus
			v.cost = v.level * v.initialCost;
			if v.level <= 5 then
				v:setTitle(getText("Challenge_Challenge2_ButtonStartGold", v.level * 20, v.cost));
			else
				v:setTitle(getText("Challenge_Challenge2_ButtonStartGoldMax", v.level * 20));
			end
			if tonumber(playerObj:getModData()["challenge2Xp"]) < v.cost or v.level > 5 then
				v:setEnable(false);
			else
				v:setEnable(true);
			end
		end
	end

	self:loadJoypadButtons()
end

function ISChallenge2PlayerUpWindow:loadJoypadButtons()
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

function ISChallenge2PlayerUpWindow:onJoypadDown(button, joypadData)
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

function ISChallenge2PlayerUpWindow:new(x, y, width, height, player)
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
