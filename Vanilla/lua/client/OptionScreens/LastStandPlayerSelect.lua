require "ISUI/ISPanel"
require "ISUI/ISButton"
require "ISUI/ISInventoryPane"
require "ISUI/ISResizeWidget"
require "ISUI/ISMouseDrag"

require "defines"

LastStandPlayerSelect = ISPanelJoypad:derive("LastStandPlayerSelect");
LastStandPlayerSelect.playerSelected = nil;

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)
local FONT_HGT_LARGE = getTextManager():getFontHeight(UIFont.Large)

function LastStandPlayerSelect:initialise()
	ISPanelJoypad.initialise(self);
end


	--************************************************************************--
	--** ISPanel:instantiate
	--**
	--************************************************************************--
function LastStandPlayerSelect:instantiate()

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
end

function LastStandPlayerSelect:getAllSavedPlayers()
	local ret = {}
	local playerList = getLastStandPlayerFileNames();
	if playerList then
		for i=0,playerList:size()-1 do
			local fileName = playerList:get(i);
			local reader = LastStandPlayerFile:new()
			local newPlayer = reader:load(fileName)
			if newPlayer then -- save the last player found
				table.insert(ret, newPlayer)
			else
				print('LastStandPlayerSelect:getAllSavedPlayers() : ' .. fileName .. ' : ' .. (reader.error or 'unknown error'))
			end
		end
	end
	return ret
end

function LastStandPlayerSelect:createSurvivorDescFromData(newPlayer)
	local newDesc = SurvivorFactory.CreateSurvivor();
	newDesc:setForename(newPlayer.forename);
	newDesc:setSurname(newPlayer.surname);
	newDesc:setFemale(newPlayer.female);
	local profession = ProfessionFactory.getProfession(newPlayer.profession)
	if profession then
		newDesc:setProfession(newPlayer.profession)
		newDesc:setProfessionSkills(profession)
	end
	if newPlayer.female then newDesc:getExtras():clear() end -- remove the beard
	return newDesc
end

function LastStandPlayerSelect:createPlayerList()
	local players = self:getAllSavedPlayers()
	for _,newPlayer in ipairs(players) do
		local newDesc = self:createSurvivorDescFromData(newPlayer)
		local avatar = IsoSurvivor.new(newDesc, nil, 0, 0, 0)
		avatar:PlayAnimWithSpeed("Idle", 0.3)
		avatar:setDir(IsoDirections.SE)
		avatar:reloadSpritePart()
		self.playersDesc["Player" .. newPlayer.forename .. newPlayer.surname] = avatar
		self.listbox:addItem(newPlayer.forename .. newPlayer.surname, newPlayer)
	end
end

function LastStandPlayerSelect:getColor(line)
	local result = {};
	result.r = tonumber(string.split(line, ",")[1]);
	result.g = tonumber(string.split(line, ",")[2]);
	result.b = tonumber(string.split(line, ",")[3]);
	return result;
end

function LastStandPlayerSelect:create()
	local btnWid = 100
	local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
	local padBottom = 10
	local btnPadX = 16

	local listY = 10 + FONT_HGT_LARGE + 10
	self.listbox = ISScrollingListBox:new(16, listY, self.width-32, self.height - padBottom - btnHgt - padBottom - listY);
	self.listbox:initialise();
	self.listbox:instantiate();
	self.listbox:setAnchorLeft(true);
	self.listbox:setAnchorRight(true);
	self.listbox:setAnchorTop(true);
	self.listbox:setAnchorBottom(true);
	self.listbox.itemheight = 160;
	self.listbox.drawBorder = true
	self.listbox.doDrawItem = LastStandPlayerSelect.drawMap;
	self.listbox.onGainJoypadFocus = LastStandPlayerSelect.onGainJoypadFocus_child;
	self.listbox.onLoseJoypadFocus = LastStandPlayerSelect.onLoseJoypadFocus_child;
	self.listbox.onJoypadBeforeDeactivate = LastStandPlayerSelect.onJoypadBeforeDeactivate_child;
	self.listbox:setOnMouseDoubleClick(self, LastStandPlayerSelect.onDblClickPlayer);

	self:createPlayerList();

--~ 	for i, k in ipairs(LastStandChallenge) do
--~ 		self.listbox:addItem(k.name, k);
--~ 	end
	self:addChild(self.listbox);

	local btnY = self.height - padBottom - btnHgt
	
	self.backButton = ISButton:new(16, btnY, btnWid, btnHgt, getText("UI_btn_back"), self, LastStandPlayerSelect.onOptionMouseDown);
	self.backButton.internal = "BACK";
	self.backButton:initialise();
	self.backButton:instantiate();
	self.backButton:setAnchorLeft(true);
	self.backButton:setAnchorTop(false);
	self.backButton:setAnchorBottom(true);
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1};

	self.backButton:setFont(UIFont.Small);
	self.backButton:ignoreWidthChange();
	self.backButton:ignoreHeightChange();
	self:addChild(self.backButton);

	self.playButton = ISButton:new(self.width - 16 - btnWid, btnY, btnWid, btnHgt, getText("UI_btn_play"), self, LastStandPlayerSelect.onOptionMouseDown);
	self.playButton.internal = "PLAY";
	self.playButton:initialise();
	self.playButton:instantiate();
	self.playButton:setAnchorLeft(false);
	self.playButton:setAnchorRight(true);
	self.playButton:setAnchorTop(false);
	self.playButton:setAnchorBottom(true);
	self.playButton.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.playButton);

	self.newButton = ISButton:new(self.playButton.x - btnPadX - btnWid, btnY, btnWid, btnHgt, getText("UI_btn_new"), self, LastStandPlayerSelect.onOptionMouseDown);
	self.newButton.internal = "NEW";
	self.newButton:initialise();
	self.newButton:instantiate();
	self.newButton:setAnchorLeft(false);
	self.newButton:setAnchorRight(true);
	self.newButton:setAnchorTop(false);
	self.newButton:setAnchorBottom(true);
	self.newButton.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.newButton);

	self.deleteButton = ISButton:new(self.newButton.x - btnPadX - btnWid, btnY, btnWid, btnHgt, getText("UI_btn_delete"), self, LastStandPlayerSelect.onOptionMouseDown);
	self.deleteButton.internal = "DELETE";
	self.deleteButton:initialise();
	self.deleteButton:instantiate();
	self.deleteButton:setAnchorLeft(false);
	self.deleteButton:setAnchorRight(true);
	self.deleteButton:setAnchorTop(false);
	self.deleteButton:setAnchorBottom(true);
	self.deleteButton.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.deleteButton);

	self:setVisible(false);

--~ 	avatar = IsoSurvivor.new(SurvivorFactory.CreateSurvivor(), nil, 0, 0, 0);
--~ 	avatar:PlayAnimWithSpeed("Run", 0.3);
--~ 	avatar:reloadSpritePart();
end

function LastStandPlayerSelect:render()
--~ 	avatar:drawAt(200,200);
end

function LastStandPlayerSelect:drawMap(y, item, alt)
	local isMouseOver = self.mouseoverselected == item.index and not self:isMouseOverScrollBar()
	if self.selected == item.index then
		self:drawRect(0, (y), self:getWidth(), self.itemheight-1, 0.3, 0.7, 0.35, 0.15);
	elseif isMouseOver then
		self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.95, 0.05, 0.05, 0.05);
	end
	local x = 0;

	self:drawRect( 16, y+16, 128, 128, 1, 0, 0, 0);
	local avatar = LastStandPlayerSelect.instance.playersDesc["Player" .. item.item.forename .. item.item.surname];
	self:drawRectBorder( 16, y+16, 128, 128, 0.3, 1, 1, 1);
	self:drawRectBorder(0, (y), self:getWidth(), self.itemheight-1, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b);

	self:drawText(item.item.forename .. " " .. item.item.surname, 160, (y)+15, 0.9, 0.9, 0.9, 0.9, UIFont.Large);

	x = 160;
	local y1 = y + 15 + FONT_HGT_LARGE + 4
	for i,v in ipairs(item.item.traits) do
		local trait = TraitFactory.getTrait(v);
		if trait and trait:getTexture() then
			self:drawTexture(trait:getTexture(), x, y1, 1,1,1,1);
			x = x + trait:getTexture():getWidth() + 2
		end
	end
	y1 = y1 + 16 + 4

	self:drawText(getText("UI_challengeplayer_PlayedTime", item.item.playedTime), 160, y1, 0.7, 0.7, 0.7, 0.7, UIFont.Small);
	y1 = y1 + FONT_HGT_SMALL
	self:drawText(getText("UI_challengeplayer_XP", item.item.globalXp), 160, y1, 0.7, 0.7, 0.7, 0.7, UIFont.Small);
	y1 = y1 + FONT_HGT_SMALL

	local prof = ProfessionFactory.getProfession(item.item.profession);
	if prof and prof:getTexture() then
		self:drawTexture(prof:getTexture(), self.width-80, y + 15, 1,1,1,1);
	end


--~ 	self:drawTextRight(item.item.lastPlayed, self:getWidth() - 10, (y)+self.itemheight-20, 0.7, 0.7, 0.7, 0.7, UIFont.Small);
	--self:drawTexture(self.worldimage, 16, y+16, 1, 1, 1, 1);

	self.itemheightoverride[item.text] = self.itemheight;

--	avatar:drawAt(self:getAbsoluteX() + 16 + 128 / 2, self:getAbsoluteY() + y + 16 + 128 / 2 + 117 + self:getYScroll());

	y = y + self.itemheightoverride[item.text];

	return y;
end

function LastStandPlayerSelect:prerender()
	LastStandPlayerSelect.instance = self
	self.listbox.doDrawItem = self.drawMap
	
	ISPanelJoypad.prerender(self);
	self:drawTextCentre(getText("UI_challengeplayer_title"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Large);
end

function LastStandPlayerSelect:onOptionMouseDown(button, x, y)
	local sel = self.listbox.items[self.listbox.selected];
	 if button.internal == "BACK" then
		MainScreen.instance.soloScreen:setVisible(true, self.joyfocus);
		MainScreen.instance.lastStandPlayerSelect:setVisible(false);
	 end
	 if button.internal == "PLAY" then
		LastStandPlayerSelect.clickPlay();
	 end
	 if button.internal == "NEW" then
		if MapSpawnSelect.instance:hasChoices() then
			MapSpawnSelect.instance:fillList();
			MapSpawnSelect.instance.previousScreen = "LastStandPlayerSelect"
			MapSpawnSelect.instance:setVisible(true, self.joyfocus);
		else
			MainScreen.instance.charCreationProfession.previousScreen = "LastStandPlayerSelect"
			MainScreen.instance.charCreationProfession:setVisible(true, self.joyfocus);
		end
		MainScreen.instance.lastStandPlayerSelect:setVisible(false);
	 end
	 if button.internal == "DELETE" then
		local modal = ISModalDialog:new((getCore():getScreenWidth() / 2) - 130, (getCore():getScreenHeight() / 2) - 60, 260, 120, getText("UI_challengeplayer_delete"), true, self, LastStandPlayerSelect.onDeleteModalClick);
		modal:initialise();
		modal:addToUIManager();
		if self.joyfocus then
			self.joyfocus.focus = modal;
			modal.removeIfJoypadDeactivated = true;
--			updateJoypadFocus(self.joyfocus);
		end
	 end
end

function LastStandPlayerSelect:onDeleteModalClick(button)
	if LastStandPlayerSelect.instance.joyfocus then
		LastStandPlayerSelect.instance.joyfocus.focus = LastStandPlayerSelect.instance.listbox
	end
	if button.internal == "YES" then
		local sel = LastStandPlayerSelect.instance.listbox.items[LastStandPlayerSelect.instance.listbox.selected];
		table.remove(LastStandPlayerSelect.instance.listbox.items, LastStandPlayerSelect.instance.listbox.selected);
		deletePlayerSave(sel.text);
		if #LastStandPlayerSelect.instance.listbox.items == 0 then -- no more player saved, we redirect to the character creation screen
			if MapSpawnSelect.instance:hasChoices() then
				MapSpawnSelect.instance:fillList();
				MapSpawnSelect.instance.previousScreen = "LastStandPlayerSelect"
				MapSpawnSelect.instance:setVisible(true, self.joyfocus);
			else
				MainScreen.instance.charCreationProfession.previousScreen = "LastStandPlayerSelect"
				MainScreen.instance.charCreationProfession:setVisible(true, self.joyfocus);
			end
			MainScreen.instance.lastStandPlayerSelect:setVisible(false);
		end
	end
end

LastStandPlayerSelect.initWorld = function()
	if LastStandPlayerSelect.playerSelected then
		getWorld():setLuaPlayerDesc(LastStandPlayerSelect.instance.playersDesc["Player" .. LastStandPlayerSelect.playerSelected.forename .. LastStandPlayerSelect.playerSelected.surname]:getDescriptor());
		getWorld():getLuaTraits():clear()
		for i,v in ipairs(LastStandPlayerSelect.playerSelected.traits) do
			getWorld():addLuaTrait(v);
		end
	end
end

-- add the different saved skills level
LastStandPlayerSelect.newGame = function(player, square)
	if LastStandPlayerSelect.playerSelected then
		if globalChallenge.id ~= "Challenge2" then return end -- FIXME
		local skillMap = { Blunt=Perks.Blunt, Blade=Perks.Axe, Carpentry=Perks.Woodwork }
		for perkName,level in pairs(LastStandPlayerSelect.playerSelected.skills) do
			local perk = skillMap[perkName]
			if perk then
				while player:getPerkLevel(perk) < level do
					player:LevelPerk(perk);
				end
				luautils.updatePerksXp(perk, player);
			end
		end
		player:getModData()["challenge2BoostGoldLevel"] = LastStandPlayerSelect.playerSelected.boostGoldLevel or 1;
		player:getModData()["challenge2StartingGoldLevel"] = LastStandPlayerSelect.playerSelected.startingGoldLevel or 0;
		player:getModData()["challenge2BoostXpLevel"] = LastStandPlayerSelect.playerSelected.boostXpLevel or 1;
	end
end

function LastStandPlayerSelect:onDblClickPlayer()
	LastStandPlayerSelect.clickPlay();
end

LastStandPlayerSelect.clickPlay = function()
	local sel = LastStandPlayerSelect.instance.listbox.items[LastStandPlayerSelect.instance.listbox.selected];
	MainScreen.instance.desc = LastStandPlayerSelect.instance.playersDesc["Player" .. sel.item.forename .. sel.item.surname]:getDescriptor();
	LastStandPlayerSelect.playerSelected = sel.item;
	LastStandPlayerSelect.playerSelected.playedTime = LastStandPlayerSelect.playerSelected.playedTime + 1;
	MainScreen.instance.lastStandPlayerSelect:setVisible(false);
--[[
	-- menu activated via joypad, we disable the joypads and will re-set them automatically when the game is started
	if LastStandPlayerSelect.instance.joyfocus then
		local joypadData = LastStandPlayerSelect.instance.joyfocus
		joypadData.focus = nil;
		updateJoypadFocus(joypadData);
		JoypadState.count = 0
		JoypadState.players = {};
		JoypadState.joypads = {};
		JoypadState.forceActivate = joypadData.id;
	end
--]]
    GameWindow.doRenderEvent(false);
	forceChangeState(GameLoadingState.new());
end

function LastStandPlayerSelect:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData);
	joypadData.focus = self.listbox;
	updateJoypadFocus(joypadData);
end

function LastStandPlayerSelect:onJoypadBeforeDeactivate(joypadData)
	-- Focus is on listbox
	self.joyfocus = nil
end

function LastStandPlayerSelect:onGainJoypadFocus_child(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData);
	self:setISButtonForA(self.parent.playButton);
	self:setISButtonForB(self.parent.backButton);
	self:setISButtonForX(self.parent.newButton);
	self:setISButtonForY(self.parent.deleteButton);
end

function LastStandPlayerSelect:onLoseJoypadFocus_child(joypadData)
	ISPanelJoypad.onLoseJoypadFocus(self, joypadData)
	self.parent.playButton:clearJoypadButton()
	self.parent.backButton:clearJoypadButton()
	self.parent.newButton:clearJoypadButton()
	self.parent.deleteButton:clearJoypadButton()
end

function LastStandPlayerSelect:onJoypadBeforeDeactivate_child(joypadData)
	self.parent:onJoypadBeforeDeactivate(joypadData)
end

function LastStandPlayerSelect:new (x, y, width, height)
	local o = {}
	--o.data = {}
	o = ISPanelJoypad.new(self, x, y, width, height);
	o.x = x;
	o.y = y;
	o.backgroundColor = {r=0, g=0, b=0, a=0.3};
	o.borderColor = {r=1, g=1, b=1, a=0.2};
	o.width = width;
	o.height = height;
	o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;
	o.itemheightoverride = {}
	o.selected = 1;
	o.playersDesc = {};
	LastStandPlayerSelect.instance = o;
	return o
end

Events.OnInitWorld.Add(LastStandPlayerSelect.initWorld);
Events.OnNewGame.Add(LastStandPlayerSelect.newGame);
