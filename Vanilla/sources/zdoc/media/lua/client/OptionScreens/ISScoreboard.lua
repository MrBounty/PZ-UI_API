require "ISUI/ISScrollingListBox"

---@class ISScoreboard : ISPanelJoypad
ISScoreboard = ISPanelJoypad:derive("ISScoreboard")
ISScoreboard.buttonsList = {};

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_LARGE = getTextManager():getFontHeight(UIFont.Large)

function ISScoreboard:initialise()
    ISPanelJoypad.initialise(self)
end

function ISScoreboard:fillList(usernames, displayNames, steamIDs)
    self.maxNameWid = 0
    self.listbox:clear()
    for i = 0,usernames:size()-1 do
        local username = usernames:get(i)
        local displayName = displayNames:get(i)
        local data = {}
        if getSteamModeActive() then
            data.steamID = steamIDs:get(i)
            data.profileName = getSteamProfileNameFromSteamID(data.steamID)
            data.avatar = getSteamAvatarFromSteamID(data.steamID)
        end
        local item = self.listbox:addItem(displayName, data)
        if ISScoreboard.isAdmin and username ~= displayName then
            item.tooltip = username
        end
        local textWid = getTextManager():MeasureStringX(UIFont.Large, displayName)
        self.maxNameWid = math.max(self.maxNameWid, textWid)
    end
    table.sort(self.listbox.items, function(a,b)
        return not string.sort(a.text, b.text)
    end)
end

function ISScoreboard.onScoreboardUpdate(usernames, displayNames, steamIDs)
    ISScoreboard.isAdmin = canModifyPlayerScoreboard() or CoopServer:isRunning();
    ISScoreboard.instance:fillList(usernames, displayNames, steamIDs);
    ISScoreboard.instance.playersConnected = usernames:size();
end

function ISScoreboard:onOptionMouseDown(button, x, y)
    if button.internal == "CLOSE" then
        self:setVisible(false);
        MainScreen.instance.bottomPanel:setVisible(true);
        if self.joyfocus then
            self.joyfocus.focus = MainScreen.instance
            updateJoypadFocus(self.joyfocus)
        end
    elseif button.internal == "REFRESH" then
        scoreboardUpdate();
    end
end

function ISScoreboard:update()
    ISPanelJoypad.update(self)
    if self.listbox:getYScroll() ~= self.listboxYScroll then
        self.listbox.mouseoverselected = self.listbox:rowAt(self.listbox:getMouseX(), self.listbox:getMouseY())
--        self:doAdminButtons()
        self.listboxYScroll = self.listbox:getYScroll()
    end
end

function ISScoreboard:prerender()
    self.backgroundColor.a = 0.8
    ISPanelJoypad.prerender(self)
end

function ISScoreboard:render()
ISScoreboard.instance = self
self.listbox.doDrawItem = self.drawMap
    ISPanelJoypad.render(self)
    self:drawTextCentre(getText("UI_mainscreen_scoreboard") .. " ( " .. getText("UI_Scoreboard_PlayerConnected", self.playersConnected) .. " )", self.width / 2, 10, 1, 1, 1, 1, UIFont.Large);
    self:drawRectBorder(self.listbox:getX(), self.listbox:getY(), self.listbox:getWidth(), self.listbox:getHeight(), 0.9, 0.4, 0.4, 0.4)
end

function ISScoreboard:onGainJoypadFocus(joypadData)
ISScoreboard.instance = self
    ISPanelJoypad.onGainJoypadFocus(self, joypadData)
    self.listbox:setISButtonForB(self.closeButton)
    self.listbox:setISButtonForX(self.refreshButton)
    joypadData.focus = self.listbox
end

-- Do the admin buttons
function ISScoreboard:doAdminButtons()
    self.kickButton.enable = false;
    self.banButton.enable = false;
    if self.banIpButton then self.banIpButton.enable = false; end
    self.teleportButton.enable = false;
    self.teleportToYouButton.enable = false;
    self.invisibleButton.enable = false;
    self.godmodButton.enable = false;
    self.voipmuteButton.enable = false;
    self.muteButton.enable = false;

    self.kickButton:setVisible(ISScoreboard.isAdmin);
    self.banButton:setVisible(ISScoreboard.isAdmin);
    if self.banIpButton then self.banIpButton:setVisible(ISScoreboard.isAdmin); end
    self.godmodButton:setVisible(ISScoreboard.isAdmin);
    self.invisibleButton:setVisible(ISScoreboard.isAdmin);
    self.teleportButton:setVisible(ISScoreboard.isAdmin);
    self.teleportToYouButton:setVisible(ISScoreboard.isAdmin);

    if self.selectedPlayer and ISScoreboard.isAdmin then
        local dy = self.listbox:getYScroll()
		local username = self.selectedPlayer
        if username ~= getSpecificPlayer(0):getDisplayName() then
            self.kickButton.enable = not (isAccessLevel("gm") or isAccessLevel("observer"));
            self.banButton.enable = (isAccessLevel("admin") or isAccessLevel("moderator"));
            self.teleportButton.enable = true;
            self.teleportToYouButton.enable = true;
            self.invisibleButton.enable = not isAccessLevel("observer");
            self.godmodButton.enable = not isAccessLevel("observer");
            self.voipmuteButton.enable = true;
            self.muteButton.enable = true;

            if self.banIpButton then
                self.banIpButton.enable = (isAccessLevel("admin") or isAccessLevel("moderator"));
            end

            local muted = ISChat.instance:isMuted(username)
            self.muteButton:setTitle(muted and getText("UI_Scoreboard_Unmute") or getText("UI_Scoreboard_Mute"))
            self.muteButton:setX(self.buttonPos[self.muteButton].x)
            self.muteButton:setY(self.buttonPos[self.muteButton].y)

            local voipmuted = VoiceManager:playerGetMute(username)
            self.voipmuteButton:setTitle(voipmuted and getText("UI_Scoreboard_VOIPUnmute") or getText("UI_Scoreboard_VOIPMute"))
            self.voipmuteButton:setX(self.buttonPos[self.voipmuteButton].x)
            self.voipmuteButton:setY(self.buttonPos[self.voipmuteButton].y)
        else
            self.invisibleButton.enable = not isAccessLevel("observer");
            self.godmodButton.enable = not isAccessLevel("observer");
        end
    elseif self.selectedPlayer then
        local dy = self.listbox:getYScroll()
        local username = self.selectedPlayer
        if username ~= getSpecificPlayer(0):getDisplayName() then
            self.voipmuteButton.enable = true;
            self.muteButton.enable = true;

            local muted = ISChat.instance:isMuted(username)
            self.muteButton:setTitle(muted and getText("UI_Scoreboard_Unmute") or getText("UI_Scoreboard_Mute"))
            self.muteButton:setX(self.listbox.x + self.listbox.width + 10);
            self.muteButton:setY(self.listbox.y);

            local voipmuted = VoiceManager:playerGetMute(username)
            self.voipmuteButton:setTitle(voipmuted and getText("UI_Scoreboard_VOIPUnmute") or getText("UI_Scoreboard_VOIPMute"))
            self.voipmuteButton:setX(self.muteButton:getRight() + 10);
            self.voipmuteButton:setY(self.listbox.y);

        end
    end
end

function ISScoreboard:onMouseMove(dx, dy)
    ISPanelJoypad.onMouseMove(dx, dy)
--    self:doAdminButtons()
end

function ISScoreboard:onContext(button)
    local username = self.selectedPlayer;
    username = '"'..username..'"'
    if button.internal == "KICK" then
        SendCommandToServer("/kickuser " .. username);
    elseif button.internal == "BAN" then
        SendCommandToServer("/banuser " .. username);
    elseif button.internal == "BANIP" then
        SendCommandToServer("/banuser " .. username .. " -ip");
    elseif button.internal == "GODMOD" then
        SendCommandToServer("/godmod " .. username);
    elseif button.internal == "INVISIBLE" then
        SendCommandToServer("/invisible " .. username);
    elseif button.internal == "TELEPORT" then
        SendCommandToServer("/teleport " .. username);
    elseif button.internal == "TELEPORTTOYOU" then
        SendCommandToServer("/teleport " .. username .. " \"" .. getPlayer():getDisplayName() .. "\"");
    elseif button.internal == "MUTE" then
        ISChat.instance:mute(self.selectedPlayer)
		self:doAdminButtons()
    elseif button.internal == "VOIPMUTE" then
        VoiceManager:playerSetMute(self.selectedPlayer)
        self:doAdminButtons()
    end
end

function ISScoreboard:drawMap(y, item, alt)
    if not item.height then item.height = self.itemheight end -- compatibililty
    local isMouseOver = self.mouseoverselected == item.index and not self:isMouseOverScrollBar()
    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15);
        if self.parent.selectedPlayer ~= item.text then
            self.parent.selectedPlayer = item.text;
            self.parent:doAdminButtons()
        end
    elseif isMouseOver then
        self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.95, 0.05, 0.05, 0.05);
    end
    self:drawRectBorder(0, (y), self:getWidth(), item.height, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    local fontHgtLarge = FONT_HGT_LARGE
    if getSteamScoreboard() then
        local avatarW = 32
        local avatarH = 32
        if item.item.avatar then
            self:drawTexture(item.item.avatar, 4, y + (item.height - 32) / 2, avatarW, avatarH, 1, 1, 1, 1)
        end
        self:drawText(item.text, 4 + avatarW + 8, y+(item.height-fontHgtLarge)/2, 0.9, 0.9, 0.9, 0.9, UIFont.Large);
        if self.mouseoverselected ~= item.index then
            local fontHgtSmall = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()
            local profileText = getText("UI_Scoreboard_SteamName", item.item.profileName)
            self:drawText(profileText, 4 + avatarW + 8 + self.parent.maxNameWid + 20, y+(item.height - fontHgtSmall)/2, 0.9, 0.9, 0.9, 0.9, UIFont.Small);
            if ISScoreboard.isAdmin then
                local textWid2 = getTextManager():MeasureStringX(UIFont.Small, profileText)
                self:drawText(getText("UI_Scoreboard_SteamID", item.item.steamID), 4 + avatarW + 8 + self.parent.maxNameWid + 20 + textWid2 + 20, y+(item.height - fontHgtSmall)/2, 0.9, 0.9, 0.9, 0.9, UIFont.Small);
            end
        end
        item.height = 2 + avatarH + 2
    else
        self:drawText(item.text, 24, y+(item.height-fontHgtLarge)/2, 0.9, 0.9, 0.9, 0.9, UIFont.Large);
        item.height = self.fontHgt + (self.itemPadY or 0) * 2
    end
    self.itemheight = item.height
    y = y + item.height;
    return y;
end

function ISScoreboard:create()
    local pad = 16
    local btnWid = 100
    local butWid = 100
    local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local titleHgt = 10 + FONT_HGT_LARGE + 5
    local avatarH = 32

    self.listbox = ISScrollingListBox:new(pad, titleHgt, (self.width-pad*3) - (butWid*2), self.height-pad*2-btnHgt-titleHgt);
    self.listbox:initialise()
    self.listbox.backgroundColor.a = 0.0
    self.listbox:setAnchorRight(true)
    self.listbox:setAnchorBottom(true)
    self.listbox.doDrawItem = ISScoreboard.drawMap;
    if getSteamScoreboard() then
        self.listbox.itemheight = 2 + avatarH + 2
    end
    self:addChild(self.listbox);

    self.closeButton = ISButton:new(pad, self.height - pad - btnHgt, btnWid, btnHgt, getText("UI_btn_back"), self, ISScoreboard.onOptionMouseDown);
    self.closeButton.internal = "CLOSE";
    self.closeButton:initialise();
    self.closeButton:setAnchorLeft(true);
    self.closeButton:setAnchorRight(false);
    self.closeButton:setAnchorTop(false);
    self.closeButton:setAnchorBottom(true);
    self.closeButton.borderColor = {r=1, g=1, b=1, a=0.1};
    self.closeButton:setFont(UIFont.Small);
    self:addChild(self.closeButton);

    self.refreshButton = ISButton:new(self.width - pad - btnWid, self.height - pad - btnHgt, btnWid, btnHgt, getText("UI_servers_refresh"), self, ISScoreboard.onOptionMouseDown);
    self.refreshButton.internal = "REFRESH";
    self.refreshButton:initialise();
    self.refreshButton:setAnchorLeft(false);
    self.refreshButton:setAnchorRight(true);
    self.refreshButton:setAnchorTop(false);
    self.refreshButton:setAnchorBottom(true);
    self.refreshButton.borderColor = {r=1, g=1, b=1, a=0.1};
    self.refreshButton:setFont(UIFont.Small);
    self:addChild(self.refreshButton);

    local buttons = {}

    local butX = self.listbox.x + self.listbox.width + 10;
    local butGap = 2
    local butY = self.listbox.y;
    local btnHgt2 = math.max(FONT_HGT_SMALL + 3 * 2, 25)
    self.kickButton = ISButton:new(butX, butY, butWid, btnHgt2, getText("UI_Scoreboard_Kick"), self, ISScoreboard.onContext);
    self.kickButton:initialise();
    self.kickButton.internal = "KICK";
    self.kickButton:setAnchorLeft(false)
    self.kickButton:setAnchorRight(true)
    self:addChild(self.kickButton);
--    self.kickButton:setVisible(false);
    self.kickButton.borderColor.a = 0.3;
    table.insert(buttons, self.kickButton)
    
    self.banButton = ISButton:new(butX + butWid + 10, butY, butWid, btnHgt2, getText("UI_Scoreboard_Ban"), self, ISScoreboard.onContext);
    self.banButton:initialise();
    self.banButton.internal = "BAN";
    self.banButton:setAnchorLeft(false)
    self.banButton:setAnchorRight(true)
    self:addChild(self.banButton);
--    self.banButton:setVisible(false);
    self.banButton.borderColor.a = 0.3;
    table.insert(buttons, self.banButton)
    butY = butY + btnHgt2 + 2;

    if not getSteamModeActive() then
        self.banIpButton = ISButton:new(butX, butY, butWid, btnHgt2, getText("UI_Scoreboard_BanIp"), self, ISScoreboard.onContext);
        self.banIpButton:initialise();
        self.banIpButton.internal = "BANIP";
        self.banIpButton:setAnchorLeft(false)
        self.banIpButton:setAnchorRight(true)
        self:addChild(self.banIpButton);
--        self.banIpButton:setVisible(false);
        self.banIpButton.borderColor.a = 0.3;
        table.insert(buttons, self.banIpButton)
    end

    self.godmodButton = ISButton:new(butX + butWid + 10, butY, butWid, btnHgt2, getText("UI_Scoreboard_GodMod"), self, ISScoreboard.onContext);
    self.godmodButton:initialise();
    self.godmodButton.internal = "GODMOD";
    self.godmodButton:setAnchorLeft(false)
    self.godmodButton:setAnchorRight(true)
    self:addChild(self.godmodButton);
--    self.godmodButton:setVisible(false);
    self.godmodButton.borderColor.a = 0.3;
    table.insert(buttons, self.godmodButton)
    butY = butY + btnHgt2 + 2;

    self.invisibleButton = ISButton:new(butX, butY, butWid, btnHgt2, getText("UI_Scoreboard_Invisible"), self, ISScoreboard.onContext);
    self.invisibleButton:initialise();
    self.invisibleButton.internal = "INVISIBLE";
    self.invisibleButton:setAnchorLeft(false)
    self.invisibleButton:setAnchorRight(true)
    self:addChild(self.invisibleButton);
--    self.invisibleButton:setVisible(false);
    self.invisibleButton.borderColor.a = 0.3;
    table.insert(buttons, self.invisibleButton)

    self.teleportButton = ISButton:new(butX + butWid + 10, butY, butWid, btnHgt2, getText("UI_Scoreboard_Teleport"), self, ISScoreboard.onContext);
    self.teleportButton:initialise();
    self.teleportButton.internal = "TELEPORT";
    self.teleportButton:setAnchorLeft(false)
    self.teleportButton:setAnchorRight(true)
    self:addChild(self.teleportButton);
--    self.teleportButton:setVisible(false);
    self.teleportButton.borderColor.a = 0.3;
    table.insert(buttons, self.teleportButton)
    butY = butY + btnHgt2 + 2;

    self.teleportToYouButton = ISButton:new(butX, butY, butWid, btnHgt2, getText("UI_Scoreboard_TeleportToYou"), self, ISScoreboard.onContext);
    self.teleportToYouButton:initialise();
    self.teleportToYouButton.internal = "TELEPORTTOYOU";
    self.teleportToYouButton:setAnchorLeft(false)
    self.teleportToYouButton:setAnchorRight(true)
    self:addChild(self.teleportToYouButton);
--    self.teleportToYouButton:setVisible(false);
    self.teleportToYouButton.borderColor.a = 0.3;
    table.insert(buttons, self.teleportToYouButton)

    self.muteButton = ISButton:new(butX + butWid + 10, butY, butWid, btnHgt2, getText("UI_Scoreboard_Mute"), self, ISScoreboard.onContext);
    self.muteButton:initialise();
    self.muteButton.internal = "MUTE";
--    self.muteButton:setVisible(false);
    self.muteButton:setAnchorLeft(false)
    self.muteButton:setAnchorRight(true)
    self.muteButton.borderColor.a = 0.3;
    self:addChild(self.muteButton);
    table.insert(buttons, self.muteButton)
    butY = butY + btnHgt2 + 2;
	
	self.voipmuteButton = ISButton:new(butX, butY, butWid, btnHgt2, getText("UI_Scoreboard_VOIPMute"), self, ISScoreboard.onContext);
    self.voipmuteButton:initialise();
    self.voipmuteButton.internal = "VOIPMUTE";
    self.voipmuteButton:setAnchorLeft(false)
    self.voipmuteButton:setAnchorRight(true)
--    self.voipmuteButton:setVisible(false);
    self.voipmuteButton.borderColor.a = 0.3;
    self:addChild(self.voipmuteButton);
    table.insert(buttons, self.voipmuteButton)

    self.buttonPos = {}

    local maxWid = 0
    for _,button in ipairs(buttons) do
        maxWid = math.max(maxWid, button.width)
    end
    local newButX = self.width - 10 - maxWid - 10 - maxWid
    for _,button in ipairs(buttons) do
        button:setWidth(maxWid)
        if button.x > butX + 1 then
            button:setX(newButX + maxWid + 10)
        else
            button:setX(newButX)
        end
        self.buttonPos[button] = { x = button.x, y = button.y }
    end
    self.listbox:setWidth(newButX - 10 - self.listbox.x)

--    if not ISScoreboard.isAdmin then
--        self.kickButton:setVisible(false);
--        self.banButton:setVisible(false);
--        if self.banIpButton then self.banIpButton:setVisible(false); end
--        self.godmodButton:setVisible(false);
--        self.invisibleButton:setVisible(false);
--        self.teleportButton:setVisible(false);
--        self.teleportToYouButton:setVisible(false);
--        self.muteButton:setVisible(false);
--        self.voipmuteButton:setVisible(false)
----        self.statsBtn:setVisible(false);
--    end

    scoreboardUpdate();
end

function ISScoreboard:new(x, y, width, height)
    local o = ISPanelJoypad.new(self, x, y, width, height)
    ISScoreboard.instance = o;
    o.playersConnected = 0;
    o.selectedPlayer = nil;
    return o;
end

Events.OnScoreboardUpdate.Add(ISScoreboard.onScoreboardUpdate)

function ISScoreboard.recreate()
    if MainScreen.instance.scoreboard:getIsVisible() then
        MainScreen.instance.bottomPanel:setVisible(true)
    end
    MainScreen.instance:removeChild(MainScreen.instance.scoreboard)
    scoreboard = ISScoreboard:new(0, 0, MainScreen.instance.width, MainScreen.instance.height)
    MainScreen.instance.scoreboard = scoreboard
    scoreboard:initialise()
    scoreboard:instantiate()
    scoreboard:setVisible(false)
    scoreboard:setAnchorRight(true)
    scoreboard:setAnchorBottom(true)
    MainScreen.instance:addChild(scoreboard)
    local scaleX = 0.6
    local scaleY = 0.7
    if MainScreen.instance.width <= 1024 then
        scaleX = 0.95
        scaleY = 0.95
    end
    scoreboard:shrinkX(scaleX)
    scoreboard:shrinkY(scaleY)
    scoreboard:ignoreWidthChange()
    scoreboard:ignoreHeightChange()
    scoreboard:create()
end
