--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISRadialMenu"

---@class ISEmoteRadialMenu : ISBaseObject
ISEmoteRadialMenu = ISBaseObject:derive("ISEmoteRadialMenu")

function ISEmoteRadialMenu:init()
	ISEmoteRadialMenu.defaultMenu = {};
	ISEmoteRadialMenu.defaultMenu["friendly"] = {};
	ISEmoteRadialMenu.defaultMenu["friendly"].name = getText("IGUI_Emote_Friendly");
	ISEmoteRadialMenu.defaultMenu["friendly"].subMenu = {};
	ISEmoteRadialMenu.defaultMenu["friendly"].subMenu["wavehi"] = getText("IGUI_Emote_WaveHi");
	ISEmoteRadialMenu.defaultMenu["friendly"].subMenu["wavebye"] = getText("IGUI_Emote_Bye");
	ISEmoteRadialMenu.defaultMenu["friendly"].subMenu["clap"] = getText("IGUI_Emote_Clap");
	ISEmoteRadialMenu.defaultMenu["friendly"].subMenu["thumbsup"] = getText("IGUI_Emote_ThumbsUp");
	ISEmoteRadialMenu.defaultMenu["friendly"].subMenu["thankyou"] = getText("IGUI_Emote_ThankYou");
	ISEmoteRadialMenu.defaultMenu["hostile"] = {};
	ISEmoteRadialMenu.defaultMenu["hostile"].name = getText("IGUI_Emote_Hostile");
	ISEmoteRadialMenu.defaultMenu["hostile"].subMenu = {};
	ISEmoteRadialMenu.defaultMenu["hostile"].subMenu["insult"] = getText("IGUI_Emote_Insult");
	ISEmoteRadialMenu.defaultMenu["hostile"].subMenu["stop"] = getText("IGUI_Emote_Stop");
	ISEmoteRadialMenu.defaultMenu["hostile"].subMenu["surrender"] = getText("IGUI_Emote_Surrender");
	ISEmoteRadialMenu.defaultMenu["hostile"].subMenu["thumbsdown"] = getText("IGUI_Emote_ThumbsDown");
	ISEmoteRadialMenu.defaultMenu["group"] = {};
	ISEmoteRadialMenu.defaultMenu["group"].name = getText("IGUI_Emote_Group");
	ISEmoteRadialMenu.defaultMenu["group"].subMenu = {};
	ISEmoteRadialMenu.defaultMenu["group"].subMenu["followme"] = getText("IGUI_Emote_FollowMe");
	ISEmoteRadialMenu.defaultMenu["group"].subMenu["comehere"] = getText("IGUI_Emote_ComeHere");
	ISEmoteRadialMenu.defaultMenu["group"].subMenu["yes"] = getText("IGUI_Emote_Yes");
	ISEmoteRadialMenu.defaultMenu["group"].subMenu["no"] = getText("IGUI_Emote_No");
	ISEmoteRadialMenu.defaultMenu["group"].subMenu["shrug"] = getText("IGUI_Emote_Shrug");
	ISEmoteRadialMenu.defaultMenu["group"].subMenu["undecided"] = getText("IGUI_Emote_Undecided");
	ISEmoteRadialMenu.defaultMenu["signal"] = {};
	ISEmoteRadialMenu.defaultMenu["signal"].name = getText("IGUI_Emote_Signal");
	ISEmoteRadialMenu.defaultMenu["signal"].subMenu = {};
	ISEmoteRadialMenu.defaultMenu["signal"].subMenu["ceasefire"] = getText("IGUI_Emote_CeaseFire");
	ISEmoteRadialMenu.defaultMenu["signal"].subMenu["signalok"] = getText("IGUI_Emote_Ok");
	ISEmoteRadialMenu.defaultMenu["signal"].subMenu["moveout"] = getText("IGUI_Emote_MoveOut");
	ISEmoteRadialMenu.defaultMenu["signal"].subMenu["freeze"] = getText("IGUI_Emote_Freeze");
	ISEmoteRadialMenu.defaultMenu["signal"].subMenu["followbehind"] = getText("IGUI_Emote_FollowBehind");
	ISEmoteRadialMenu.defaultMenu["signal"].subMenu["signalfire"] = getText("IGUI_Emote_Fire");
	ISEmoteRadialMenu.defaultMenu["signal"].subMenu["comefront"] = getText("IGUI_Emote_Come");
	ISEmoteRadialMenu.defaultMenu["signal"].subMenu["salute"] = getText("IGUI_Emote_Salute");
--	ISEmoteRadialMenu.defaultMenu["signal"].subMenu["contactR90"] = "Contact";

--	ISEmoteRadialMenu.defaultMenu["wavehi"] = {};
--	ISEmoteRadialMenu.defaultMenu["wavehi"].name = "Wave Hi";

	ISEmoteRadialMenu.defaultMenu["shout"] = {};
	ISEmoteRadialMenu.defaultMenu["shout"].name = getText("IGUI_Emote_Shout");
	
	-- some anims can have variant, we'll pick one randomly in this list
	ISEmoteRadialMenu.variants = {};
	ISEmoteRadialMenu.variants["wavehi"] = {"wavehi", "wavehi02", "wavebye"};
	ISEmoteRadialMenu.variants["comehere"] = {"comehere", "comehere02"};
	ISEmoteRadialMenu.variants["stop"] = {"stop", "stop02"};
	ISEmoteRadialMenu.variants["clap"] = {"clap", "clap02"};
	ISEmoteRadialMenu.variants["salute"] = {"saluteformal", "salutecasual"};
	
	ISEmoteRadialMenu.icons = {};
	ISEmoteRadialMenu.icons["friendly"] = getTexture("media/ui/emotes/thumbsup.png");
	ISEmoteRadialMenu.icons["hostile"] = getTexture("media/ui/emotes/thumbsdown.png");
	ISEmoteRadialMenu.icons["signal"] = getTexture("media/ui/emotes/moveout.png");
	ISEmoteRadialMenu.icons["group"] = getTexture("media/ui/emotes/followme.png");
	ISEmoteRadialMenu.icons["thumbsup"] = getTexture("media/ui/emotes/thumbsup.png");
	ISEmoteRadialMenu.icons["thumbsdown"] = getTexture("media/ui/emotes/thumbsdown.png");
	ISEmoteRadialMenu.icons["config"] = getTexture("media/ui/emotes/gears.png");
	ISEmoteRadialMenu.icons["back"] = getTexture("media/ui/emotes/back.png");
	ISEmoteRadialMenu.icons["wavebye"] = getTexture("media/ui/emotes/wavebye.png");
	ISEmoteRadialMenu.icons["shout"] = getTexture("media/ui/Traits/trait_talkative.png");
	ISEmoteRadialMenu.icons["comefront"] = getTexture("media/ui/emotes/comefromfront.png");
	ISEmoteRadialMenu.icons["comehere"] = getTexture("media/ui/emotes/comehere.png");
	ISEmoteRadialMenu.icons["followme"] = getTexture("media/ui/emotes/followme.png");
	ISEmoteRadialMenu.icons["insult"] = getTexture("media/ui/emotes/insult.png");
	ISEmoteRadialMenu.icons["moveout"] = getTexture("media/ui/emotes/moveout.png");
	ISEmoteRadialMenu.icons["stop"] = getTexture("media/ui/emotes/stop.png");
	ISEmoteRadialMenu.icons["surrender"] = getTexture("media/ui/emotes/surrender.png");
	ISEmoteRadialMenu.icons["ceasefire"] = getTexture("media/ui/emotes/ceasefire.png");
	ISEmoteRadialMenu.icons["clap"] = getTexture("media/ui/emotes/clap.png");
	ISEmoteRadialMenu.icons["signalfire"] = getTexture("media/ui/emotes/fire.png");
	ISEmoteRadialMenu.icons["followbehind"] = getTexture("media/ui/emotes/followbehind.png");
	ISEmoteRadialMenu.icons["freeze"] = getTexture("media/ui/emotes/freeze.png");
	ISEmoteRadialMenu.icons["no"] = getTexture("media/ui/emotes/no.png");
	ISEmoteRadialMenu.icons["salute"] = getTexture("media/ui/emotes/salute.png");
	ISEmoteRadialMenu.icons["shrug"] = getTexture("media/ui/emotes/shrug.png");
	ISEmoteRadialMenu.icons["thankyou"] = getTexture("media/ui/emotes/thankyou.png");
	ISEmoteRadialMenu.icons["undecided"] = getTexture("media/ui/emotes/undecided.png");
	ISEmoteRadialMenu.icons["wavebye"] = getTexture("media/ui/emotes/wavebye.png");
	ISEmoteRadialMenu.icons["wavehi"] = getTexture("media/ui/emotes/wavehello.png");
	ISEmoteRadialMenu.icons["yes"] = getTexture("media/ui/emotes/yes.png");
	ISEmoteRadialMenu.icons["signalok"] = getTexture("media/ui/emotes/yes.png");
	
	ISEmoteRadialMenu.menu = ISEmoteRadialMenu.defaultMenu;
end

function ISEmoteRadialMenu:new(character)
	local o = ISBaseObject.new(self)
	o.character = character
	o.playerNum = character:getPlayerNum()
	ISUIEmoteConfig:readFile();
	return o
end

function ISEmoteRadialMenu:display()
	local menu = getPlayerRadialMenu(self.playerNum)
	self:center()
	menu:addToUIManager()
	if JoypadState.players[self.playerNum+1] then
		menu:setHideWhenButtonReleased(Joypad.DPadDown)
		setJoypadFocus(self.playerNum, menu)
		self.character:setJoypadIgnoreAimUntilCentered(true)
	end
end

function ISEmoteRadialMenu:center()
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

function ISEmoteRadialMenu:fillMenu(submenu)
	local menu = getPlayerRadialMenu(self.playerNum)
	menu:clear()

	local icon = nil;
	if not submenu then -- base menu with all categories
--		menu:addSlice("Friendly", nil, self.fillMenu, self, "friendly")
		for i,v in pairs(ISEmoteRadialMenu.menu) do
			icon = nil;
			if ISEmoteRadialMenu.icons[i] then
				icon = ISEmoteRadialMenu.icons[i];
			end
			if v.subMenu then -- stuff with submenu
				menu:addSlice(v.name, icon, self.fillMenu, self, i)
			else -- stuff for rapid access
				menu:addSlice(v.name, icon, self.emote, self, i)
			end
		end
--		menu:addSlice("Config", ISEmoteRadialMenu.icons["config"], self.configEmote, self)
	else
		for i,v in pairs(ISEmoteRadialMenu.menu[submenu].subMenu) do
			icon = nil;
			if ISEmoteRadialMenu.icons[i] then
				icon = ISEmoteRadialMenu.icons[i];
			end
			menu:addSlice(v, icon, self.emote, self, i)
		end
		menu:addSlice(getText("IGUI_Emote_Back"), ISEmoteRadialMenu.icons["back"], self.fillMenu, self)
	end

	self:display()
end

function ISEmoteRadialMenu:emote(emote)
	-- check for variant of the same anim (like wave hi could be wavehi or wavehi02)
	if ISEmoteRadialMenu.variants[emote] then
		emote = ISEmoteRadialMenu.variants[emote][ZombRand(#ISEmoteRadialMenu.variants[emote])+1];
	end
	self.character:playEmote(emote);
	if emote == "shout" then
		self.character:Callout(false);
	end
end

function ISEmoteRadialMenu:configEmote()
	local ui = ISUIEmoteConfig:new(0, 0, self.character);
	ui:initialise();
	ui:addToUIManager();
end

----------------------------------

local STATE = {}
STATE[1] = {}
STATE[2] = {}
STATE[3] = {}
STATE[4] = {}

function ISEmoteRadialMenu.checkKey(key)
	if not (key == getCore():getKey("Emote") or key == getCore():getKey("Shout")) then
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
	return true
end

function ISEmoteRadialMenu.onKeyPressed(key)
	if not ISEmoteRadialMenu.checkKey(key) then
		return
	end
	if getCore():getGameMode() == "Tutorial" and key ~= getCore():getKey("Shout") then
		return;
	end
	local radialMenu = getPlayerRadialMenu(0)
	if getCore():getOptionRadialMenuKeyToggle() and radialMenu:isReallyVisible() then
		STATE[1].radialWasVisible = true
		radialMenu:removeFromUIManager()
		return
	end
	STATE[1].keyPressedMS = getTimestampMs()
	STATE[1].radialWasVisible = false
end

function ISEmoteRadialMenu.onKeyRepeat(key)
	if not ISEmoteRadialMenu.checkKey(key) then
		return
	end
	if getCore():getGameMode() == "Tutorial" and key ~= getCore():getKey("Shout") then
		return;
	end
	if STATE[1].radialWasVisible then
		return
	end
	if not STATE[1].keyPressedMS then
		return
	end
	local playerObj = getSpecificPlayer(0);
	local radialMenu = getPlayerRadialMenu(0)
	local delay = 450
	if (getTimestampMs() - STATE[1].keyPressedMS >= delay) and key == getCore():getKey("Emote") and not playerObj:getVehicle() then
		if not radialMenu:isReallyVisible() then
			local frm = ISEmoteRadialMenu:new(playerObj)
			frm:fillMenu()
		end
	end
end

function ISEmoteRadialMenu.onKeyReleased(key)
	if not ISEmoteRadialMenu.checkKey(key) then
		return
	end
	if getCore():getGameMode() == "Tutorial" and key ~= getCore():getKey("Shout") then
		return;
	end
	if not STATE[1].keyPressedMS then
		return
	end
	local playerObj = getSpecificPlayer(0);
	local radialMenu = getPlayerRadialMenu(0)
	if radialMenu:isReallyVisible() or STATE[1].radialWasVisible then
		if not getCore():getOptionRadialMenuKeyToggle() then
			radialMenu:removeFromUIManager()
		end
		return
	end
	
	local delay = 450
	if (getTimestampMs() - STATE[1].keyPressedMS < delay) and key == getCore():getKey("Shout") and not playerObj:getVehicle() then
		playerObj:Callout(true);
	end
end

local function OnGameStart()
	Events.OnKeyStartPressed.Add(ISEmoteRadialMenu.onKeyPressed)
	Events.OnKeyKeepPressed.Add(ISEmoteRadialMenu.onKeyRepeat)
	Events.OnKeyPressed.Add(ISEmoteRadialMenu.onKeyReleased)
end

Events.OnGameStart.Add(OnGameStart)
