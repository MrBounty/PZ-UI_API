--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

ISPacketCounts = ISPanel:derive("ISPacketCounts")
ISPacketCountsList = ISPanel:derive("ISPacketCountsPanel")

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

local PacketNames = {}
PacketNames[1] = "ServerPulse"
PacketNames[2] = "Login"
PacketNames[3] = "ChunkRequest"
PacketNames[4] = "KeepAlive"
PacketNames[5] = "Vehicles"
PacketNames[6] = "PlayerConnect"
PacketNames[7] = "PlayerUpdateInfo"
PacketNames[8] = "CreateZombie"
PacketNames[9] = "MetaGrid"
PacketNames[10] = "ZombieUpdateInfo"
PacketNames[11] = "Helicopter"
PacketNames[12] = "SyncIsoObject"
PacketNames[13] = "PlayerTimeout"
PacketNames[14] = "SteamGeneric"
PacketNames[15] = "ServerMap"
PacketNames[16] = "PassengerMap"
PacketNames[17] = "AddItemToMap"
PacketNames[18] = "SentChunk"
PacketNames[19] = "SyncClock"
PacketNames[20] = "AddInventoryItemToContainer"
PacketNames[21] = "ConnectionDetails"
PacketNames[22] = "RemoveInventoryItemFromContainer"
PacketNames[23] = "RemoveItemFromSquare"
PacketNames[24] = "RequestLargeAreaZip"
PacketNames[25] = "Equip"
PacketNames[26] = "HitCharacter"
PacketNames[27] = "AddCoopPlayer"
PacketNames[28] = "WeaponHit"
PacketNames[29] = "ZombieDie"
PacketNames[30] = "KillZombie"
PacketNames[31] = "SandboxOptions"
PacketNames[32] = "SmashWindow"
PacketNames[33] = "PlayerDeath"
PacketNames[34] = "RequestZipList"
PacketNames[35] = "ItemStats"
PacketNames[36] = "NotRequiredInZip"
PacketNames[37] = "RequestData"
PacketNames[38] = "Chat"
PacketNames[39] = "SendDeadZombie"
PacketNames[40] = "AccessDenied"
PacketNames[41] = "SendDamage"
PacketNames[42] = "Bandage"
PacketNames[43] = "EatFood"
PacketNames[44] = "RequestItemsForContainer"
PacketNames[45] = "Drink"
PacketNames[46] = "SyncAlarmClock"
PacketNames[47] = "PacketCounts"
PacketNames[48] = "SendModData"
PacketNames[49] = "RemoveContestedItemsFromInventory"
PacketNames[50] = "ScoreboardUpdate"
PacketNames[51] = "ReceiveModData"
PacketNames[52] = "ServerQuit"
PacketNames[53] = "PlaySound"
PacketNames[54] = "WorldSound"
PacketNames[55] = "AddAmbient"
PacketNames[56] = "SyncClothing"
PacketNames[57] = "ClientCommand"
PacketNames[58] = "ObjectModData"
PacketNames[59] = "ObjectChange"
PacketNames[60] = "BloodSplatter"
PacketNames[61] = "ZombieSound"
PacketNames[62] = "ZombieDescriptors"
PacketNames[63] = "SlowFactor"
PacketNames[64] = "Weather"
PacketNames[65] = "SyncPlayerInventory"
PacketNames[67] = "RequestPlayerData"
PacketNames[68] = "RemoveCorpseFromMap"
PacketNames[69] = "AddCorpseToMap"
PacketNames[75] = "StartFire"
PacketNames[76] = "UpdateItemSprite"
PacketNames[77] = "StartRain"
PacketNames[78] = "StopRain"
PacketNames[79] = "WorldMessage"
PacketNames[80] = "getModData"
PacketNames[81] = "ReceiveCommand"
PacketNames[83] = "Kicked"
PacketNames[84] = "ExtraInfo"
PacketNames[85] = "AddItemInInventory"
PacketNames[86] = "ChangeSafety"
PacketNames[87] = "Ping"
PacketNames[88] = "WriteLog"
PacketNames[89] = "AddXP"
PacketNames[90] = "UpdateOverlaySprite"
PacketNames[91] = "Checksum"
PacketNames[92] = "ConstructedZone"
PacketNames[94] = "RegisterZone"
PacketNames[97] = "WoundInfection"
PacketNames[98] = "Stitch"
PacketNames[99] = "Disinfect"
PacketNames[100] = "AdditionalPain"
PacketNames[101] = "RemoveGlass"
PacketNames[102] = "Splint"
PacketNames[103] = "RemoveBullet"
PacketNames[104] = "CleanBurn"
PacketNames[105] = "SyncThumpable"
PacketNames[106] = "SyncDoorKey"
PacketNames[107] = "AddXpCommand"
PacketNames[108] = "Teleport"
PacketNames[109] = "RemoveBlood"
PacketNames[110] = "AddExplosiveTrap"
PacketNames[111] = "RemoveSpecialObjectFromSquare"
PacketNames[112] = "BodyDamageUpdate"
PacketNames[114] = "SyncSafehouse"
PacketNames[115] = "SledgehammerDestroy"
PacketNames[116] = "StopFire"
PacketNames[117] = "Cataplasm"
PacketNames[118] = "AddAlarm"
PacketNames[119] = "PlaySoundEveryPlayer"
PacketNames[120] = "SyncFurnace"
PacketNames[121] = "SendCustomColor"
PacketNames[122] = "SyncCompost"
PacketNames[123] = "ChangePlayerStats"
PacketNames[124] = "AddXpFromPlayerStatsUI"
PacketNames[125] = "AddLevelUpPoint"
PacketNames[126] = "SyncXP"
PacketNames[127] = "PacketTypeShort"
PacketNames[128] = "UserLog"
PacketNames[129] = "AddUserLog"
PacketNames[130] = "RemoveUserLog"
PacketNames[131] = "AddWarningPoint"
PacketNames[132] = "MessageForAdmin"
PacketNames[133] = "WakeUpPlayer"
PacketNames[134] = "SendTransactionID"
PacketNames[135] = "GetDBSchema"
PacketNames[136] = "GetTableResult"
PacketNames[137] = "ExecuteQuery"
PacketNames[138] = "ChangeTextColor"
PacketNames[139] = "SyncNonPvpZone"
PacketNames[140] = "SyncFaction"
PacketNames[141] = "SendFactionInvite"
PacketNames[142] = "AcceptedFactionInvite"
PacketNames[143] = "AddTicket"
PacketNames[144] = "ViewTickets"
PacketNames[145] = "RemoveTicket"
PacketNames[146] = "RequestTrading"
PacketNames[147] = "TradingUIAddItem"
PacketNames[148] = "TradingUIRemoveItem"
PacketNames[149] = "TradingUIUpdateStatus"
PacketNames[150] = "SendItemListNet"
PacketNames[151] = "ChunkObjectState"
PacketNames[152] = "ReadAnnotedMap"
PacketNames[153] = "RequestInventory"
PacketNames[154] = "SendInventory"
PacketNames[155] = "InvMngReqItem"
PacketNames[156] = "InvMngGetItem"
PacketNames[157] = "InvMngRemoveItem"
PacketNames[158] = "StartPause"
PacketNames[159] = "StopPause"
PacketNames[160] = "TimeSync"
PacketNames[161] = "SyncIsoObjectReq"
PacketNames[162] = "PlayerSave"
PacketNames[163] = "SyncWorldObjectsReq"
PacketNames[164] = "SyncObjects"
PacketNames[171] = "SpawnRegion"
PacketNames[172] = "PlayerDamageFromCarCrash"

function ISPacketCountsList:prerender()
--	self:drawRect(0, -self:getYScroll(), self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b)
	self:setStencilRect(0, 0, self.width, self.height)

	local fontHgt = FONT_HGT_SMALL

	local packetCounts = getPacketCounts(self.parent.category.selected)
	if not packetCounts then return end
	
	local x = 0
	local y = 0
	for i=1,256 do
		local name = PacketNames[i-1] or tostring(i-1)
		if self.parent.currentCounts[i] ~= packetCounts[i] then
			self:drawText(name..": ", x, y, 0, 1, 0, 1, UIFont.Small)
			self:drawTextRight(tostring(packetCounts[i]), x + (self.width - 0) / 4 - 20, y, 0, 1, 0, 1, UIFont.Small)
		else
			self:drawText(name..": ", x, y, 1, 1, 1, 1, UIFont.Small)
			self:drawTextRight(tostring(packetCounts[i]), x + (self.width - 0) / 4 - 20, y, 1, 1, 1, 1, UIFont.Small)
		end
		y = y + fontHgt
		if i > 0 and i % 64 == 0 then
			y = 0
			x = x + (self.width - 0) / 4
		end
	end

	self:setScrollHeight(fontHgt * 64)
	self:clearStencilRect();
end

function ISPacketCountsList:onMouseWheel(del)
	local fontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()
	self:setYScroll(self:getYScroll() - (del * fontHgt * 4))
	return true
end

function ISPacketCountsList:new(x, y, width, height)
	o = ISPanel:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	return o
end

-----

function ISPacketCounts:createChildren()
	local btnWid = 100
	local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

	self.category = ISComboBox:new(20, 20, 400, FONT_HGT_SMALL + 2 * 2, self, self.onSelectCategory)
	self.category:initialise()
	self.category:addOption("Server: Packets from all clients")
	self.category:addOption("Client: Packets from server")
	self:addChild(self.category)

	local y = self.category:getBottom() + 10
	self.listbox = ISPacketCountsList:new(20, y, self.width - 20 * 2, self.height - 20 - btnHgt - y)
	self.listbox:initialise()
	self.listbox:instantiate()
	self:addChild(self.listbox)
	self.listbox:addScrollBars()

	self.update = ISButton:new(self.width - 20 - btnWid - 20 - btnWid, self.height - 10 - btnHgt,
		btnWid, btnHgt, "UPDATE", self, self.onUpdate)
	self.update.anchorTop = false
	self.update.anchorBottom = true
	self.update:initialise()
	self.update:instantiate()
	self.update.borderColor = {r=0.4, g=0.4, b=0.4, a=0.9}
	self:addChild(self.update)

	self.close = ISButton:new(self.width - 20 - btnWid, self.height - 10 - btnHgt,
		btnWid, btnHgt, getText("UI_btn_close"), self, self.onClose)
	self.close.anchorTop = false
	self.close.anchorBottom = true
	self.close:initialise()
	self.close:instantiate()
	self.close.borderColor = {r=0.4, g=0.4, b=0.4, a=0.9}
	self:addChild(self.close)
end

function ISPacketCounts:render()
	ISPacketCounts.instance = self -- for script reloading
	ISPanel.render(self)
end

function ISPacketCounts:onSelectCategory()
	self:onUpdate()
end

function ISPacketCounts:onUpdate()
	local packetCounts = getPacketCounts(self.category.selected)
	for i=1,256 do
		self.currentCounts[i] = packetCounts and packetCounts[i] or 0
	end
	if self.category.selected == 1 then
		requestPacketCounts()
	end
end

function ISPacketCounts:onClose()
	self:closeSelf()
end

function ISPacketCounts:closeSelf()
	self:setVisible(false)
	self:removeFromUIManager()
end

function ISPacketCounts:new(x, y, width, height)
	o = ISPanel:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.borderColor = {r=0.4, g=0.4, b=0.4, a=1}
	o.backgroundColor = {r=0, g=0, b=0, a=0.8}
	o.moveWithMouse = true
	o.currentCounts = {}
	for i=1,256 do
		o.currentCounts[i] = 0
	end
	ISPacketCounts.instance = o
	return o
end

