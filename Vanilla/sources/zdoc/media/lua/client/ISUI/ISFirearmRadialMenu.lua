--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISRadialMenu"

---@class ISFirearmRadialMenu : ISBaseObject
ISFirearmRadialMenu = ISBaseObject:derive("ISFirearmRadialMenu")

-----

local BaseCommand = ISBaseObject:derive("BaseCommand")

function BaseCommand:new(frm)
    local o = ISBaseObject.new(self)
    o.frm = frm
    o.character = frm.character
    return o
end

function BaseCommand:getWeapon()
	return self.frm:getWeapon()
end

function BaseCommand:fillMenu(menu, weapon)
	error "forgot to derive fillMenu()"
end

-----

local CInsertMagazine = BaseCommand:derive("CInsertMagazine")

function CInsertMagazine:new(frm)
	local o = BaseCommand.new(self, frm)
	return o
end

function CInsertMagazine:fillMenu(menu, weapon)
	if weapon:isContainsClip() then return end
	if not weapon:getMagazineType() then return end
	local magazine = weapon:getBestMagazine(self.character)
	if not magazine then return end
	local text = getText("IGUI_FirearmRadial_InsertMagazine")
	local xln = "IGUI_FirearmRadial_AmmoCount"
	local textCount = getText(xln, magazine:getCurrentAmmoCount(), magazine:getMaxAmmo())
	text = text .. '\n' .. textCount
	menu:addSlice(text, getTexture("media/ui/FirearmRadial_InsertMagazine.png"), self.invoke, self)
end

function CInsertMagazine:invoke()
	local weapon = self:getWeapon()
	if not weapon then return end
	if weapon:isContainsClip() then return end
	local magazine = weapon:getBestMagazine(self.character)
	if not magazine then return end
	ISInventoryPaneContextMenu.transferIfNeeded(self.character, magazine)
	ISTimedActionQueue.add(ISInsertMagazine:new(self.character, weapon, magazine))
end

-----

local CEjectMagazine = BaseCommand:derive("CEjectMagazine")

function CEjectMagazine:new(frm)
	local o = BaseCommand.new(self, frm)
	return o
end

function CEjectMagazine:fillMenu(menu, weapon)
	if not weapon:isContainsClip() then return end
	local text = getText("IGUI_FirearmRadial_EjectMagazine")
	local xln = weapon:isRoundChambered() and "IGUI_FirearmRadial_AmmoCountChambered" or "IGUI_FirearmRadial_AmmoCount"
	local textCount = getText(xln, weapon:getCurrentAmmoCount(), weapon:getMaxAmmo())
	text = text .. '\n' .. textCount
	text = text:gsub("\\n", "\n")
	menu:addSlice(text, getTexture("media/ui/FirearmRadial_EjectMagazine.png"), self.invoke, self)
end

function CEjectMagazine:invoke()
	local weapon = self:getWeapon()
	if not weapon then return end
	if not weapon:isContainsClip() then return end
	ISTimedActionQueue.add(ISEjectMagazine:new(self.character, weapon))
end

-----

local CLoadBulletsInMagazine = BaseCommand:derive("CLoadBulletsInMagazine")

function CLoadBulletsInMagazine:new(frm)
	local o = BaseCommand.new(self, frm)
	return o
end

local function predicateNotFullMagazine(item, magazineType)
	return (item:getType() == magazineType or item:getFullType() == magazineType) and item:getCurrentAmmoCount() < item:getMaxAmmo()
end

local function predicateFullestMagazine(item1, item2)
	return item1:getCurrentAmmoCount() - item2:getCurrentAmmoCount()
end

function CLoadBulletsInMagazine:getMagazine(weapon)
	if not weapon:getMagazineType() then return nil end
	local inventory = self.character:getInventory()
	return inventory:getBestEvalArgRecurse(predicateNotFullMagazine, predicateFullestMagazine, weapon:getMagazineType())
end

function CLoadBulletsInMagazine:hasBulletsForMagazine(magazine)
	local inventory = self.character:getInventory()
	return inventory:getCountTypeRecurse(magazine:getAmmoType()) > 0
end

function CLoadBulletsInMagazine:fillMenu(menu, weapon)
	local magazine = self:getMagazine(weapon)
	if not magazine then return end
	if not self:hasBulletsForMagazine(magazine) then return end
	local text = getText("IGUI_FirearmRadial_LoadBulletsIntoMagazine")
	local xln = "IGUI_FirearmRadial_AmmoCount"
	local textCount = getText(xln, magazine:getCurrentAmmoCount(), magazine:getMaxAmmo())
	text = text .. '\\n' .. textCount
	text = text:gsub('\\n', '\n')
	menu:addSlice(text, getTexture("media/ui/FirearmRadial_BulletsIntoMagazine.png"), self.invoke, self)
end

function CLoadBulletsInMagazine:invoke()
	local weapon = self:getWeapon()
	if not weapon then return end
	local magazine = self:getMagazine(weapon)
	if not magazine then return end
	if not self:hasBulletsForMagazine(magazine) then return end
	ISInventoryPaneContextMenu.transferIfNeeded(self.character, magazine)
	local ammoCount = ISInventoryPaneContextMenu.transferBullets(self.character, magazine:getAmmoType(), magazine:getCurrentAmmoCount(), magazine:getMaxAmmo())
	if ammoCount == 0 then return end
	ISTimedActionQueue.add(ISLoadBulletsInMagazine:new(self.character, magazine, ammoCount))
end

-----

local CLoadRounds = BaseCommand:derive("CLoadRounds")

function CLoadRounds:new(frm)
	local o = BaseCommand.new(self, frm)
	return o
end

function CLoadRounds:hasBullets(weapon)
	local inventory = self.character:getInventory()
	return inventory:getCountTypeRecurse(weapon:getAmmoType()) > 0
end

function CLoadRounds:fillMenu(menu, weapon)
	if weapon:getMagazineType() then return end
	if weapon:getCurrentAmmoCount() >= weapon:getMaxAmmo() then return end
	if not self:hasBullets(weapon) then return end
	local text = getText("IGUI_FirearmRadial_LoadRounds")
	local xln = weapon:isRoundChambered() and "IGUI_FirearmRadial_AmmoCountChambered" or "IGUI_FirearmRadial_AmmoCount"
	local textCount = getText(xln, weapon:getCurrentAmmoCount(), weapon:getMaxAmmo())
	text = text .. '\\n' .. textCount
	text = text:gsub('\\n', '\n')
	menu:addSlice(text, getTexture("media/ui/FirearmRadial_BulletsIntoFirearm.png"), self.invoke, self)
end

function CLoadRounds:invoke()
	local weapon = self:getWeapon()
	if not weapon then return end
	if weapon:getMagazineType() then return end
	if weapon:getCurrentAmmoCount() >= weapon:getMaxAmmo() then return end
	if not self:hasBullets(weapon) then return end
	ISInventoryPaneContextMenu.transferBullets(self.character, weapon:getAmmoType(), weapon:getCurrentAmmoCount(), weapon:getMaxAmmo())
	ISTimedActionQueue.add(ISReloadWeaponAction:new(self.character, weapon))
end

-----

local CUnloadRounds = BaseCommand:derive("CUnloadRounds")

function CUnloadRounds:new(frm)
	local o = BaseCommand.new(self, frm)
	return o
end

function CUnloadRounds:fillMenu(menu, weapon)
	if weapon:getMagazineType() then return end
	if weapon:getCurrentAmmoCount() == 0 then return end
	local text = getText("IGUI_FirearmRadial_UnloadRounds")
	local xln = weapon:isRoundChambered() and "IGUI_FirearmRadial_AmmoCountChambered" or "IGUI_FirearmRadial_AmmoCount"
	local textCount = getText(xln, weapon:getCurrentAmmoCount(), weapon:getMaxAmmo())
	text = text .. '\\n' .. textCount
	text = text:gsub('\\n', '\n')
	menu:addSlice(text, getTexture("media/ui/FirearmRadial_BulletsFromFirearm.png"), self.invoke, self)
end

function CUnloadRounds:invoke()
	local weapon = self:getWeapon()
	if not weapon then return end
	if weapon:getMagazineType() then return end
	if weapon:getCurrentAmmoCount() == 0 then return end
	ISTimedActionQueue.add(ISUnloadBulletsFromFirearm:new(self.character, weapon, false))
end

-----

local CRack = BaseCommand:derive("CRack")

function CRack:new(frm)
	local o = BaseCommand.new(self, frm)
	return o
end

function CRack:fillMenu(menu, weapon)
	if not ISReloadWeaponAction.canRack(weapon) then return end
	local xln = weapon:isRoundChambered() and "IGUI_FirearmRadial_AmmoCountChambered" or "IGUI_FirearmRadial_AmmoCount"
	local textCount = getText(xln, weapon:getCurrentAmmoCount(), weapon:getMaxAmmo())
	if weapon:isJammed() then
		local text = getText("IGUI_FirearmRadial_Unjam")
		text = text .. '\n' .. textCount
		menu:addSlice(text, getTexture("media/ui/FirearmRadial_Unjam.png"), self.invoke, self)
	elseif not weapon:haveChamber() or weapon:isRoundChambered() then
		local xln = weapon:haveChamber() and "IGUI_FirearmRadial_Rack" or "IGUI_FirearmRadial_UnloadRound"
		local text = getText(xln)
		text = text .. '\n' .. textCount
		menu:addSlice(text, getTexture("media/ui/FirearmRadial_Rack.png"), self.invoke, self)
	elseif weapon:haveChamber() then
		local text = getText("IGUI_FirearmRadial_ChamberRound")
		text = text .. '\n' .. textCount
		menu:addSlice(text, getTexture("media/ui/FirearmRadial_ChamberRound.png"), self.invoke, self)
	end
end

function CRack:invoke()
	local weapon = self:getWeapon()
	if not weapon then return end
	if not ISReloadWeaponAction.canRack(weapon) then return end
	ISTimedActionQueue.add(ISRackFirearm:new(self.character, weapon))
end

-----

function ISFirearmRadialMenu:center()
	local menu = getPlayerRadialMenu(self.playerNum)

	local x = getPlayerScreenLeft(self.playerNum)
	local y = getPlayerScreenTop(self.playerNum)
	local w = getPlayerScreenWidth(self.playerNum)
	local h = getPlayerScreenHeight(self.playerNum)

	x = x + w / 2
	y = y + h / 2

	menu:setX(x - menu:getWidth() / 2)
	menu:setY(y - menu:getHeight() / 2)
end

function ISFirearmRadialMenu:getWeapon()
	local weapon = self.character:getPrimaryHandItem()
	if not weapon then return nil end
	if not instanceof(weapon, "HandWeapon") then return nil end
	if not weapon:isRanged() then return nil end
	return weapon
end

function ISFirearmRadialMenu:fillMenu()
	local menu = getPlayerRadialMenu(self.playerNum)
	menu:clear()
	local weapon = self:getWeapon()
	if not weapon then return end

	local commands = {}
	if weapon:getMagazineType() then
		if weapon:isContainsClip() then
			table.insert(commands, CEjectMagazine:new(self))
		else
			table.insert(commands, CInsertMagazine:new(self))
		end
		table.insert(commands, CLoadBulletsInMagazine:new(self))
	else
		table.insert(commands, CLoadRounds:new(self))
		table.insert(commands, CUnloadRounds:new(self))
	end
	table.insert(commands, CRack:new(self))

	for _,command in ipairs(commands) do
		local count = #menu.slices
		command:fillMenu(menu, weapon)
		if count == #menu.slices then
			menu:addSlice(nil, nil, nil)
		end
	end
end

function ISFirearmRadialMenu:display()
	local menu = getPlayerRadialMenu(self.playerNum)
	self:center()
	menu:addToUIManager()
	if JoypadState.players[self.playerNum+1] then
		menu:setHideWhenButtonReleased(Joypad.RBumper)
		setJoypadFocus(self.playerNum, menu)
		self.character:setJoypadIgnoreAimUntilCentered(true)
	end
end

function ISFirearmRadialMenu:new(character)
	local o = ISBaseObject.new(self)
	o.character = character
	o.playerNum = character:getPlayerNum()
	return o
end

-----

function ISFirearmRadialMenu.checkWeapon(playerObj)
	local weapon = playerObj:getPrimaryHandItem()
	if not weapon then return false end
	if not instanceof(weapon, "HandWeapon") then return false end
	if not weapon:isRanged() then return false end
	return true
end

-----

function ISFirearmRadialMenu.getBestLBButtonAction(buttonPrompt)
--	buttonPrompt:setLBPrompt("RACK", nil, buttonPrompt.player)
	return false
end

local STATE = {}
STATE[1] = {}
STATE[2] = {}
STATE[3] = {}
STATE[4] = {}

local function onJoypadReload(buttonPrompt)
	STATE[buttonPrompt.player+1].rbPressedMS = getTimestampMs()
end

function ISFirearmRadialMenu.getBestRBButtonAction(buttonPrompt)
	if JoypadState.disableReload then
		return false;
	end
	local playerObj = getSpecificPlayer(buttonPrompt.player)
	if playerObj:isAiming() or playerObj:isLookingWhileInVehicle() then return false end
	local queue = ISTimedActionQueue.queues[playerObj]
	if queue and #queue.queue > 0 then
		return false
	end
	if not ISFirearmRadialMenu.checkWeapon(playerObj) then
		return
	end
	buttonPrompt:setRBPrompt("RELOAD", onJoypadReload)
	return true
end

function ISFirearmRadialMenu.onRepeatRBumper(buttonPrompt)
	local radialMenu = getPlayerRadialMenu(buttonPrompt.player)
	local playerObj = getSpecificPlayer(buttonPrompt.player)
	if playerObj:isAiming() or playerObj:isLookingWhileInVehicle() then
		STATE[buttonPrompt.player+1].rbPressedMS = nil
		return false
	end
	if radialMenu:isReallyVisible() then
		return
	end
	if not STATE[buttonPrompt.player+1].rbPressedMS then return end
	local delay = 500
	if getCore():getOptionReloadRadialInstant() then
		delay = 0
	end
	if getTimestampMs() - STATE[buttonPrompt.player+1].rbPressedMS >= delay then
		local frm = ISFirearmRadialMenu:new(playerObj)
		frm:fillMenu()
		frm:display()
		setPlayerMovementActive(buttonPrompt.player, false)
	end
end

function ISFirearmRadialMenu.onJoypadButtonReleased(buttonPrompt, button)
	if button == Joypad.RBumper then
		local rbPressedMS = STATE[buttonPrompt.player+1].rbPressedMS
		if not rbPressedMS then return end
		STATE[buttonPrompt.player+1].rbPressedMS = nil
		local radialMenu = getPlayerRadialMenu(buttonPrompt.player)
		if radialMenu:isReallyVisible() then return end
		if getTimestampMs() - rbPressedMS < 500 then
			local playerObj = getSpecificPlayer(buttonPrompt.player)
			local weapon = playerObj:getPrimaryHandItem()
			ISReloadWeaponAction.BeginAutomaticReload(playerObj, weapon)
		end
	end
end

-----

function ISFirearmRadialMenu.checkKey(key)
	if key ~= getCore():getKey("ReloadWeapon") then
		return false
	end
	if UIManager.getSpeedControls() and (UIManager.getSpeedControls():getCurrentGameSpeed() == 0) then
		return false
	end
	local playerObj = getSpecificPlayer(0)
	if not playerObj or playerObj:isDead() then
		return false
	end
	local queue = ISTimedActionQueue.queues[playerObj]
	if queue and #queue.queue > 0 then
		return false
	end
	if getCell():getDrag(0) then
		return false
	end
	return true
end

function ISFirearmRadialMenu.onKeyPressed(key)
	if not ISFirearmRadialMenu.checkKey(key) then
		return
	end
	local radialMenu = getPlayerRadialMenu(0)
	if getCore():getOptionRadialMenuKeyToggle() and radialMenu:isReallyVisible() then
		STATE[1].radialWasVisible = true
		radialMenu:removeFromUIManager()
		return
	end
	if not ISFirearmRadialMenu.checkWeapon(getSpecificPlayer(0)) then
		return
	end
	STATE[1].keyPressedMS = getTimestampMs()
	STATE[1].radialWasVisible = false
end

function ISFirearmRadialMenu.onKeyRepeat(key)
	if not ISFirearmRadialMenu.checkKey(key) then
		return
	end
	if not ISFirearmRadialMenu.checkWeapon(getSpecificPlayer(0)) then
		return
	end
	if STATE[1].radialWasVisible then
		return
	end
	if not STATE[1].keyPressedMS then
		return
	end
	local radialMenu = getPlayerRadialMenu(0)
	local delay = 500
	if getCore():getOptionReloadRadialInstant() then
		delay = 0
	end
	if (getTimestampMs() - STATE[1].keyPressedMS >= delay) and not radialMenu:isReallyVisible() then
		local frm = ISFirearmRadialMenu:new(getSpecificPlayer(0))
		frm:fillMenu()
		frm:display()
	end
end

function ISFirearmRadialMenu.onKeyReleased(key)
	if not ISFirearmRadialMenu.checkKey(key) then
		return
	end
	if not STATE[1].keyPressedMS then
		return
	end
	local radialMenu = getPlayerRadialMenu(0)
	if radialMenu:isReallyVisible() or STATE[1].radialWasVisible then
		if not getCore():getOptionRadialMenuKeyToggle() then
			radialMenu:removeFromUIManager()
		end
		return
	end
	if not ISFirearmRadialMenu.checkWeapon(getSpecificPlayer(0)) then
		return
	end
	if getCore():getOptionReloadRadialInstant() then
		return
	end
	if getTimestampMs() - STATE[1].keyPressedMS < 500 then
		local playerObj = getSpecificPlayer(0)
		local weapon = playerObj:getPrimaryHandItem()
		ISReloadWeaponAction.BeginAutomaticReload(playerObj, weapon)
	end
end

local function OnGameStart()
	Events.OnKeyStartPressed.Add(ISFirearmRadialMenu.onKeyPressed)
	Events.OnKeyKeepPressed.Add(ISFirearmRadialMenu.onKeyRepeat)
	Events.OnKeyPressed.Add(ISFirearmRadialMenu.onKeyReleased)
end

Events.OnGameStart.Add(OnGameStart)

