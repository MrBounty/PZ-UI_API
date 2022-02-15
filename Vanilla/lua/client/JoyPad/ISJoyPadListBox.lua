--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISScrollingListBox"

ISJoypadListBox = ISScrollingListBox:derive("ISJoypadListBox")

function ISJoypadListBox:fill()
	local listBox = self
	listBox:clear()
	listBox:setScrollHeight(0)
	for i=1,getMaxActivePlayers() do
		local playerObj = getSpecificPlayer(i-1)
		if JoypadState.players[i] == nil and playerObj and playerObj:isAlive() then
			listBox:addItem(getText("IGUI_Controller_TakeOverPlayer", i), { cmd = "takeover", playerNum = i-1 })
		end
	end
	if not isDemo() then
		listBox:addItem(getText("IGUI_Controller_AddNewPlayer"), { cmd = "addnew" })
		if Core.isLastStand() then
			local players = LastStandPlayerSelect:getAllSavedPlayers()
			for _,player in ipairs(players) do
				local inUse = false
				for playerNum=0,getNumActivePlayers()-1 do
					local playerObj = getSpecificPlayer(playerNum)
					if playerObj and not playerObj:isDead() then
						if playerObj:getDescriptor():getForename() == player.forename and playerObj:getDescriptor():getSurname() == player.surname then
							inUse = true
							break
						end
					end
				end
				if not inUse then
					local label = getText("IGUI_Controller_AddSavedPlayer", player.forename, player.surname)
					listBox:addItem(label, { cmd = "addsaved", player = player })
				end
			end
		else
			local players = IsoPlayer.getAllSavedPlayers()
			for n=1,players:size() do
				local playerObj = players:get(n-1)
				if not playerObj:isSaveFileInUse() and playerObj:isSaveFileIPValid() then
					local label = getText("IGUI_Controller_AddSavedPlayer", playerObj:getDescriptor():getForename(), playerObj:getDescriptor():getSurname())
					listBox:addItem(label, { cmd = "addsaved", player = playerObj })
				end
			end
		end
	end
	listBox:addItem(getText("UI_Cancel"), { cmd = "cancel" })
	listBox.selected = 1
	listBox:setHeight(math.min(listBox:getScrollHeight(), getCore():getScreenHeight()))
end

function ISJoypadListBox:invoke()
	local joypadData = self.joypadData
	local item = self.items[self.selected].item
	if item.cmd == "takeover" then
		self:cmdTakeOver(item.playerNum)
	elseif item.cmd == "addnew" then
		self:cmdAddNew()
	elseif item.cmd == "addsaved" then
		self:cmdAddSaved(item.player)
	elseif item.cmd == "cancel" then
		self:cmdCancel()
	end
end

function ISJoypadListBox:cmdTakeOver(playerNum)
	self:setVisible(false)
	self:removeFromUIManager()
	local joypadData = self.joypadData
	joypadData.focus = nil
	joypadData.lastfocus = nil
	joypadData.listBox = nil
	local controller = joypadData.controller
	local playerObj = getSpecificPlayer(playerNum)
	if not (playerObj and playerObj:isAlive()) then return end
	local joypadData = JoypadState.joypads[playerNum+1]
	JoypadState.players[playerNum+1] = joypadData
	joypadData:setController(controller)
	joypadData:setActive(true)
	joypadData.player = playerNum
	setPlayerJoypad(playerNum, controller.id, playerObj, nil)
	createPlayerData(playerNum)
	-- FIXME: obsolete?
	getPlayerInventory(playerNum):setController(controller.id)
	getPlayerLoot(playerNum):setController(controller.id)
end

function ISJoypadListBox:cmdAddNew()
	self:setVisible(false)
	self:removeFromUIManager()
	local joypadData = self.joypadData
	joypadData.focus = nil
	joypadData.lastfocus = nil
	joypadData.listBox = nil
	CoopCharacterCreation.newPlayer(joypadData.id, joypadData)
end

function ISJoypadListBox:cmdAddSaved(player)
	self:setVisible(false)
	self:removeFromUIManager()
	local joypadData = self.joypadData
	local controller = self.joypadData.controller
	joypadData.focus = nil
	joypadData.lastfocus = nil
	joypadData.listBox = nil
	if isClient() then
		local x = (getCore():getScreenWidth() - 260) / 2
		local y = (getCore():getScreenHeight() - 120) / 2
		local username = player:getModData().username or ("Player" .. joypadData.player)
		local modal = ISTextBox:new(x, y, 260, 120, getText("UI_servers_username"), username, self, ISJoypadListBox.getUserNameCallback, joypadData.player, player)
		modal:initialise()
		modal:addToUIManager()
		joypadData.focus = modal
		return
	end
	local playerNum = nil
	for i=0,getMaxActivePlayers()-1 do
		local playerObj = getSpecificPlayer(i)
		if not playerObj or playerObj:isDead() then
			playerNum = i
			break
		end
	end
	-- Set this if you want to force the player number (as long as there's no player there).
	-- playerNum = 3
	local joypadData = JoypadState.joypads[playerNum+1]
	JoypadState.players[playerNum+1] = joypadData
	joypadData:setController(controller)
	joypadData:setActive(true)
	joypadData.player = playerNum
	if Core.isLastStand() then
		local desc = LastStandPlayerSelect:createSurvivorDescFromData(player)
		getWorld():setLuaPlayerDesc(desc)
		for i,v in ipairs(player.traits) do
			getWorld():addLuaTrait(v)
		end
		player = nil
	end
	setPlayerJoypad(playerNum, joypadData.id, player, nil)
end

function ISJoypadListBox:getUserNameCallback(button, playerObj)
	local controller = self.joypadData.controller
	if button.internal == "OK" then
		local username = button.parent.entry:getText()
		if username and username ~= "" then
			local playerNum = nil
			for i=0,getMaxActivePlayers()-1 do
				local player = getSpecificPlayer(i)
				if not player or player:isDead() then
					playerNum = i
					break
				end
			end
			-- Set this if you want to force the player number (as long as there's no player there).
			-- playerNum = 3
			local joypadData = JoypadState.joypads[playerNum+1]
			JoypadState.players[playerNum+1] = joypadData
			joypadData:setController(controller)
			joypadData:setActive(true)
			joypadData.player = playerNum
			setPlayerJoypad(playerNum, joypadData.id, playerObj, username)
		else
			joypadData.focus = nil
		end
	end
	if button.internal == "CANCEL" then
		joypadData.focus = nil
	end
end

function ISJoypadListBox:cmdCancel()
	self:setVisible(false)
	self:removeFromUIManager()
	local joypadData = self.joypadData
	joypadData.focus = nil
	joypadData.lastfocus = nil
	joypadData.listBox = nil
end

function ISJoypadListBox:onJoypadDown(button, joypadData)
	if button == Joypad.AButton then
		joypadData.activeWhilePaused = nil
		self:invoke()
	end
	if button == Joypad.BButton then
		joypadData.activeWhilePaused = nil
		self:cmdCancel()
	end
end

function ISJoypadListBox:new(x, y, w, h, playerNum, joypadData)
	local o = ISScrollingListBox.new(self, x, y, w, h)
	o.playerNum = playerNum
	o.joypadData = joypadData
	return o
end

function ISJoypadListBox.Create(playerNum, joypadData)
	if joypadData.player then error "joypadData.player ~= nil" end
	local listBox = ISJoypadListBox:new(0, 0, 200, 400, playerNum, joypadData)
	listBox:initialise()
	listBox:instantiate()
	listBox:setUIName("JoypadListbox"..joypadData.id)
	listBox:setAnchorLeft(true)
	listBox:setAnchorRight(true)
	listBox:setAnchorTop(true)
	listBox:setAnchorBottom(true)
	listBox:setAlwaysOnTop(true)
	listBox.selected = 1
	listBox:setController(joypadData.id);
	return listBox
end
