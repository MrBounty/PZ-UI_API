--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISScrollingListBox"

MapSpawnSelect = ISPanelJoypad:derive("MapSpawnSelect")

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

function MapSpawnSelect:initialise()
	ISPanelJoypad.initialise(self)
end

function MapSpawnSelect:getFixedSpawnRegion()
	if not isClient() then return nil end
	local spawnPoint = getServerOptions():getOption("SpawnPoint")
	if not spawnPoint or #spawnPoint:split(",") ~= 3 then
		return nil
    end
	local xyz = spawnPoint:split(",")
	local x = tonumber(xyz[1])
	local y = tonumber(xyz[2])
	local z = tonumber(xyz[3])
	if x and y and z and (x ~= 0 or y ~= 0) then
		local worldX = math.floor(x / 300)
		local worldY = math.floor(y / 300)
		local posX = x - worldX * 300
		local posY = y - worldY * 300
		return { {
			name = getText("UI_mapspawn_ServerSpawnPoint"), points = {
				unemployed = {
					{ worldX = worldX, worldY = worldY, posX = posX, posY = posY, posZ = z },
				},
			}
		} }
	end
	return nil
end

function MapSpawnSelect:getSafehouseSpawnRegion()
	if not isClient() then return nil end
	if not getServerOptions():getBoolean("SafehouseAllowRespawn") then return nil end
	local username = getClientUsername()
	if MainScreen.instance.inGame then
		if CoopCharacterCreation.instance.playerIndex > 0 then
			username = CoopUserName.instance:getUserName()
		end
	end
	for i=0,SafeHouse.getSafehouseList():size()-1 do
		local safe = SafeHouse.getSafehouseList():get(i);
		if safe:isRespawnInSafehouse(username) and safe:getPlayers():contains(username) or safe:getOwner() == username then
			x = safe:getX() + (safe:getH() / 2);
			y = safe:getY() + (safe:getW() / 2);
			z = 0;
			local worldX = math.floor(x / 300)
			local worldY = math.floor(y / 300)
			local posX = x - worldX * 300
			local posY = y - worldY * 300
			return { {
				name = getText("UI_mapspawn_Safehouse"), points = {
					unemployed = {
						{ worldX = worldX, worldY = worldY, posX = posX, posY = posY, posZ = z },
					},
				}
			} }
		end
	end
	return nil
end

function MapSpawnSelect:getChallengeSpawnRegion()
	if not getCore():isChallenge() then return nil end
	return LastStandData.getSpawnRegion()
end

function MapSpawnSelect:getSpawnRegions()
	return self:getSafehouseSpawnRegion() or self:getFixedSpawnRegion() or self:getChallengeSpawnRegion() or SpawnRegionMgr.getSpawnRegions()
end

function MapSpawnSelect:hasChoices()
	local regions = self:getSpawnRegions()
	return regions and #regions > 1
end

function MapSpawnSelect:useDefaultSpawnRegion()
	self.selectedRegion = nil
	local regions = self:getSpawnRegions()
	if not regions or #regions == 0 then return end
	self.selectedRegion = regions[1]
	return self.selectedRegion
end

function MapSpawnSelect:fillList()
	self.listbox:clear()
	local regions = self:getSpawnRegions()
	if not regions then return end
	for _,v in ipairs(regions) do
		local info = getMapInfo(v.name)
		if info then
			local item = {};
			item.name = info.title or "NO TITLE";
			item.region = v;
			item.dir = v.name;
			item.desc = info.description or "NO DESCRIPTION";
			item.worldimage = info.thumb;
			self.listbox:addItem(item.name, item);
		else
			local item = {}
			item.name = v.name;
			item.region = v;
			item.dir = "";
			item.desc = "";
			item.worldimage = nil;
			self.listbox:addItem(item.name, item);
		end
	end
	self.listbox:sort()

	self:hideOrShowSaveName()
end

function MapSpawnSelect:hideOrShowSaveName()
	-- There is no "Save Name" field when creating a co-op player
	if MainScreen.instance.inGame then return end

	-- When loading an existing save, don't display "Save Name" field
	if MainScreen.instance.createWorld and not getCore():isChallenge() then
		self.startY = 110
		self.textEntryLabel:setVisible(true)
		self.textEntry:setVisible(true)
	else
		self.startY = 80
		self.textEntryLabel:setVisible(false)
		self.textEntry:setVisible(false)
	end

	self.listbox:setY(self.startY)
	self.listbox:setHeight(self.height-30-30-self.startY)
end

function MapSpawnSelect:onOptionMouseDown(button, x, y)
	self:setVisible(false)
	if button.internal == "BACK" then
		self:clickBack()
	elseif button.internal == "NEXT" then
		self:clickNext()
	end
end

function MapSpawnSelect:onDblClick()
	self.nextButton:forceClick()
end

function MapSpawnSelect:clickBack()
	if getWorld():getGameMode() == "Multiplayer" then
		backToSinglePlayer()
		getCore():ResetLua("default", "exitJoinServer")
	elseif self.previousScreen == "LastStandPlayerSelect" then
		self.previousScreen = nil
		LastStandPlayerSelect.instance:setVisible(true, self.joyfocus)
	elseif self.previousScreen == "LoadGameScreen" then
		MainScreen.resetLuaIfNeeded()
		self.previousScreen = nil
		LoadGameScreen.instance:setSaveGamesList()
		MainScreen.instance.loadScreen:setVisible(true, self.joyfocus)
	elseif self.previousScreen == "NewGameScreen" then
		self.previousScreen = nil
		MainScreen.instance.soloScreen:setVisible(true, self.joyfocus)
	elseif self.previousScreen == "WorldSelect" then
		self.previousScreen = nil
		MainScreen.instance.worldSelect:setVisible(true, self.joyfocus)
	end
end

function MapSpawnSelect:clickNext()
	self.selectedRegion = self.listbox.items[self.listbox.selected].item.region
	self:setVisible(false)
	if MainScreen.instance.createWorld then
		getWorld():setWorld(sanitizeWorldName(self.textEntry:getText()));
	end
	if getWorld():getGameMode() == "Sandbox" and not checkSaveFileExists("map_sand.bin") then
		MainScreen.instance.sandOptions.previousScreen = "MapSpawnSelect";
		MainScreen.instance.sandOptions:setVisible(true, self.joyfocus)
	else
		MainScreen.instance.charCreationProfession.previousScreen = "MapSpawnSelect";
		MainScreen.instance.charCreationProfession:setVisible(true, self.joyfocus)
	end
end

function MapSpawnSelect:prerender()
	ISPanelJoypad.prerender(self)
	local enable = true
	if self.textEntry and self.textEntry:isVisible() then
		local rawText = self.textEntry:getText()
		local worldName = sanitizeWorldName(rawText)
		if worldName == "" or worldName ~= rawText or luautils.stringStarts(worldName, ".") or luautils.stringEnds(worldName, ".") then
			enable = false
		else
			local checkExistName = getWorld():getGameMode() .. getFileSeparator() .. worldName
			if checkExistName ~= self.checkExistsName then
				self.checkExistsName = checkExistName
				self.checkExist = checkSaveFolderExists(checkExistName)
			end
			if self.checkExist then
				enable = false
			end
		end
	end
	if self.textEntry then
		self.textEntry:setValid(enable)
	end
	self.nextButton:setEnable(enable)
end

function MapSpawnSelect:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_mapspawn_title"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	self:drawRectBorder(self.listbox:getX(), self.listbox:getY(), self.listbox:getWidth(), self.listbox:getHeight(), 0.9, 0.4, 0.4, 0.4)
	
	local selectedItem = self.listbox.items[self.listbox.selected].item;
	
	local thumbHeight = 0;
	local thumbPadY = 0;
	if selectedItem.worldimage ~= nil then
		thumbHeight = selectedItem.worldimage:getHeight();
		thumbPadY = 10;
		local BreakPoint = ((self.width/4)*3) - selectedItem.worldimage:getHeight()/2;
		self:drawTexture(selectedItem.worldimage, BreakPoint, self.startY, 1, 1, 1, 1);
		self:drawRectBorder( BreakPoint, self.startY, selectedItem.worldimage:getWidth(), selectedItem.worldimage:getHeight(), 0.3, 1, 1, 1);
		--        self:drawTexture(item.worldimage, MapSelecter.padXY, y+MapSelecter.padXY, 1, 1, 1, 1);
	end
	local descRectWidth = self.width - 37 - (self.width/2 + 30)
	local descRectHeight = self.height - 60 - (self.startY + thumbHeight + thumbPadY)
	self.richText:setX(self.width/2 + 30)
	self.richText:setY(self.startY + thumbHeight + thumbPadY)
	self.richText:setWidth(descRectWidth)
	self.richText:setHeight(descRectHeight)
	self.richText:setVisible(true);
	self.richText.text = selectedItem.desc or "";
	self.richText:paginate();
	self:drawRectBorder( self.richText.x, self.richText.y, self.richText:getWidth(), self.richText:getHeight(), 0.3, 1, 1, 1);
end

function MapSpawnSelect:doDrawItem(y, item, alt)
	local isMouseOver = self.mouseoverselected == item.index and not self:isMouseOverScrollBar()
	if self.selected == item.index then
		self:drawRect(0, (y), self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
	elseif isMouseOver then
		self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.95, 0.05, 0.05, 0.05);
	end
	self:drawRectBorder(0, (y), self:getWidth(), item.height, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b)
	local fontHgt = getTextManager():getFontFromEnum(UIFont.Large):getLineHeight()
	local textY = y + (item.height - fontHgt) / 2
	self:drawText(item.text, 15, textY, 0.9, 0.9, 0.9, 0.9, UIFont.Large)
	y = y + item.height
	return y
end

function MapSpawnSelect:onGainJoypadFocus(joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData)
    joypadData.focus = self.listbox
    updateJoypadFocus(joypadData)
    self.listbox:setISButtonForA(self.nextButton)
    self.listbox:setISButtonForB(self.backButton)
end

function MapSpawnSelect:onJoypadBeforeDeactivate_child(joypadData)
	self.parent:onJoypadBeforeDeactivate(joypadData)
end

function MapSpawnSelect:onJoypadBeforeDeactivate(joypadData)
	self.backButton:clearJoypadButton()
	self.nextButton:clearJoypadButton()
	-- focus is on listbox
	self.joyfocus = nil
end

function MapSpawnSelect:create()
	local padX = 16
	local btnWid = 100
	local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
	local btnPadY = 5
	local titleHgt = 80

	if not MainScreen.instance.inGame then -- don't show savefile entry in splitscreen
	
	self.textEntryLabel = ISLabel:new(20, 48, 50, getText("UI_mapselecter_savename"), 1, 1, 1, 1, UIFont.Medium, true);
	self.textEntryLabel:initialise();
	self.textEntryLabel:instantiate();
	self.textEntryLabel:setAnchorLeft(true);
	self.textEntryLabel:setAnchorRight(true);
	self.textEntryLabel:setAnchorTop(false);
	self.textEntryLabel:setAnchorBottom(false);
	self:addChild(self.textEntryLabel);
	
	local inset = 2
	self.textEntry = ISTextEntryBox:new("", self.textEntryLabel:getRight() + 17, self.textEntryLabel.y + (self.textEntryLabel.height - (FONT_HGT_MEDIUM + inset * 2)) / 2, self.width-(self.textEntryLabel:getRight() + 17) - 16, 18);
	self.textEntry.font = UIFont.Medium
	self.textEntry:initialise();
	self.textEntry:instantiate();
	self.textEntry:setAnchorLeft(true);
	self.textEntry:setAnchorRight(true);
	self.textEntry:setAnchorTop(true);
	self.textEntry:setAnchorBottom(false);
	self:addChild(self.textEntry);
	local sdf = SimpleDateFormat.new("dd-MM-yyyy_hh-mm-ss");
	self.textEntry:setText(sdf:format(Calendar.getInstance():getTime()));

	end -- not MainScreen.instance.inGame
	
--	self.listbox = ISScrollingListBox:new(padX, titleHgt, self.width-padX*2, self.height-btnPadY-btnHgt-24-titleHgt)
--	self.listbox:initialise()
--	self.listbox:setAnchorRight(true)
--	self.listbox:setAnchorBottom(true)
--	self.listbox.doDrawItem = MapSpawnSelect.doDrawItem
--	self.listbox:setOnMouseDoubleClick(self, MapSpawnSelect.onDblClick)
--	self:addChild(self.listbox)
	
	self.listbox = ISScrollingListBox:new(16, self.startY, self.width/2, self.height-30-30-self.startY);
	self.listbox:initialise();
	self.listbox:instantiate();
	self.listbox:setAnchorLeft(true);
	self.listbox:setAnchorTop(true);
	self.listbox:setAnchorBottom(true);
	self:addChild(self.listbox);
	self.listbox.itemheight = 50;
	self.listbox.doDrawItem = MapSpawnSelect.doDrawItem
	self.listbox:setOnMouseDoubleClick(self, MapSpawnSelect.onDblClick)
	self.listbox.onJoypadBeforeDeactivate = MapSpawnSelect.onJoypadBeforeDeactivate_child
	self.listbox.backgroundColor  = {r=0, g=0, b=0, a=0.5};
	
	self.richText = ISRichTextPanel:new(16, 10, 500,200);
	self.richText.marginRight = 20
	self.richText.autosetheight = false;
	self.richText.clip = true
	self.richText:initialise();
	self.richText.background = true;
	self.richText:setAnchorBottom(true);
	self.richText:setAnchorRight(true);
	self.richText:setVisible(false);
	self.richText.backgroundColor  = {r=0, g=0, b=0, a=0.5};
	self:addChild(self.richText);
	self.richText:addScrollBars()

	self.backButton = ISButton:new(padX, self.height - btnPadY - btnHgt, 100, btnHgt, getText("UI_btn_back"), self, MapSpawnSelect.onOptionMouseDown)
	self.backButton.internal = "BACK"
	self.backButton:initialise()
	self.backButton:instantiate()
	self.backButton:setAnchorLeft(true)
	self.backButton:setAnchorTop(false)
	self.backButton:setAnchorBottom(true)
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.backButton)

	self.nextButton = ISButton:new(self.width - 116, self.height - btnPadY - btnHgt, 100, btnHgt, getText("UI_btn_next"), self, MapSpawnSelect.onOptionMouseDown)
	self.nextButton.internal = "NEXT"
	self.nextButton:initialise()
	self.nextButton:instantiate()
	self.nextButton:setAnchorLeft(false)
	self.nextButton:setAnchorRight(true)
	self.nextButton:setAnchorTop(false)
	self.nextButton:setAnchorBottom(true)
	self.nextButton:setEnable(true) -- sets the hard-coded border color
	self:addChild(self.nextButton)
end

function MapSpawnSelect:new(x, y, width, height)
	local o = ISPanelJoypad.new(self, x, y, width, height)
	o.selectedRegion = nil
	o.previousScreen = 'NewGameScreen'
	o.addY = 0;
	o.startY = MainScreen.instance.inGame and 80 or 110;
	MapSpawnSelect.instance = o
	return o
end

