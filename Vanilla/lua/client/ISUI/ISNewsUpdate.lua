--***********************************************************
--**              	  ROBERT JOHNSON                       **
--**            UI display with a question or text         **
--**          can display a yes/no button or ok btn        **
--***********************************************************

ISNewsUpdate = ISPanelJoypad:derive("ISNewsUpdate");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

--************************************************************************--
--** ISNewsUpdate:initialise
--**
--************************************************************************--

function ISNewsUpdate:initialise()
    ISPanelJoypad.initialise(self);

    local fontHgtMedium = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
    self.versionCombo = ISComboBox:new((self.width - 200) / 2, 16, 200, 3 + fontHgtMedium + 3, self, self.onVersionSelected)
    self.versionCombo.font = UIFont.Medium
    self.versionCombo:initialise()
    self.versionCombo:instantiate()
    self:addChild(self.versionCombo)

    local versions = Translator.getNewsVersions()
    self.versionCombo.selected = versions:size()
    for i=1,versions:size() do
        local version = versions:get(i-1)
        self.versionCombo:addOptionWithData(version, version)
        if version == getCore():getVersionNumber() then
            self.versionCombo.selected = i
        end
    end

    local btnWid = 100
    local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local padBottom = 10
    if self.yesno then
        self.yes = ISButton:new((self:getWidth() / 2) - btnWid - 5, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Yes"), self, ISNewsUpdate.onClick);
        self.yes.internal = "YES";
        self.yes.anchorTop = false
        self.yes.anchorBottom = true
        self.yes:initialise();
        self.yes:instantiate();
        self.yes.borderColor = {r=1, g=1, b=1, a=0.1};
        self:addChild(self.yes);

        self.no = ISButton:new((self:getWidth() / 2) + 5, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_No"), self, ISNewsUpdate.onClick);
        self.no.internal = "NO";
        self.no.anchorTop = false
        self.no.anchorBottom = true
        self.no:initialise();
        self.no:instantiate();
        self.no.borderColor = {r=1, g=1, b=1, a=0.1};
        self:addChild(self.no);
    else
        self.ok = ISButton:new((self:getWidth() / 2) - btnWid / 2, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_btn_close"), self, ISNewsUpdate.onClick);
        self.ok.internal = "OK";
        self.ok.anchorTop = false
        self.ok.anchorBottom = true
        self.ok:initialise();
        self.ok:instantiate();
        self.ok.borderColor = {r=1, g=1, b=1, a=0.1};
        self:addChild(self.ok);
    end

    local chatY = self.versionCombo:getBottom() + 16
    self.chatText = ISRichTextPanel:new(0, chatY, self.width - 15, self.height - 60 - chatY);
    self.chatText.marginRight = self.chatText.marginLeft;
    self.chatText:initialise();

    self:addChild(self.chatText);
    self.chatText.background = false;
    self.chatText.autosetheight = false;
    self.chatText.clip = true
    self.chatText:addScrollBars();

    if not versions:isEmpty() then
        self:onVersionSelected()
    end
end

function ISNewsUpdate:onVersionSelected()
    local version = self.versionCombo.options[self.versionCombo.selected].data
    self.chatText.text = Translator.getText("News_" .. version .. "_Disclaimer")
    self.chatText:paginate()
end

function ISNewsUpdate:updateButtons()
    local btnHgt = 25
    local padBottom = 10
    if self.yesno then
        self.yes:setY(self:getHeight() - padBottom - btnHgt);
        self.no:setY(self:getHeight() - padBottom - btnHgt);
    else
        self.ok:setY(self:getHeight() - padBottom - btnHgt);
    end
end

function ISNewsUpdate:render()
    ISPanel.render(self);
end

function ISNewsUpdate:destroy()
    self:setVisible(false);
    if self.destroyOnClick then
        self:removeFromUIManager();
    end
    if self.player and JoypadState.players[self.player+1] then
        setJoypadFocus(self.player, nil);
    end
end

function ISNewsUpdate:onClick(button)
    self:destroy();
    if self.onclick ~= nil then
        self.onclick(self.target, button, self.param1, self.param2);
    end
end

function ISNewsUpdate:prerender()
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    --	self:drawTextCentre(self.text, self:getWidth() / 2, (self:getHeight() / 2) - 10, 1, 1, 1, 1, UIFont.Small);
    self:drawTextRight(getText("UI_NewsVersion"), self.versionCombo:getX() - 8, self.versionCombo:getY() + 3, 1, 1, 1, 1, UIFont.Medium)
end

function ISNewsUpdate:onMouseDown(x, y)
    --	ISPanelJoypad.onMouseDown(self, x, y)
    -- FIXME: this prevents clicks being passed to windows behind, but need to swallow clicks outside and mouse-move events as well
    return true
end

function ISNewsUpdate:onGainJoypadFocus(joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData);
    if self.yesno then
        self:setISButtonForA(self.yes)
        self:setISButtonForB(self.no)
        self.yes.mouseOver = true;
    else
        self:setISButtonForA(self.ok)
        self.ok.mouseOver = true;
    end
end

function ISNewsUpdate:onJoypadDown(button)
    if button == Joypad.AButton then
        if self.yesno then
            if self.yes.mouseOver then
                self.yes.player = self.player;
                self.yes.onclick(self.yes.target, self.yes);
            else
                self.no.player = self.player;
                self.no.onclick(self.no.target, self.no);
            end
        else
            self.ok.onclick(self.ok.target, self.ok);
        end
    end
    if button == Joypad.BButton then
        if self.yesno then
            self.no.player = self.player;
            self.no.onclick(self.no.target, self.no);
        else
            self.ok.onclick(self.ok.target, self.ok);
        end
    end
end

function ISNewsUpdate:onJoypadDirRight()
    if self.yesno then
        self.no.mouseOver = true;
        self.yes.mouseOver = false;
    end
end

function ISNewsUpdate:onJoypadDirLeft()
    if self.yesno then
        self.no.mouseOver = false;
        self.yes.mouseOver = true;
    end
end

function ISNewsUpdate:update()
    ISPanelJoypad.update(self)
--    local minHeight = self.chatText:getY() + self.chatText:getHeight() + 30
--    if self:getHeight() < minHeight then
--        local dh = minHeight - self:getHeight()
--        self:setHeight(minHeight)
--        self:setY(self:getY() - dh / 2)
--    end
    if self.alwaysOnTop then
        self:bringToTop();
    end
end

--************************************************************************--
--** ISNewsUpdate:new
--**
--************************************************************************--
function ISNewsUpdate:new(x, y, width, height, yesno, target, onclick, player, param1, param2)
    local o = {}
    o = ISPanelJoypad:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    local playerObj = player and getSpecificPlayer(player) or nil
    o.name = nil;
    o.backgroundColor = {r=0, g=0, b=0, a=0.5};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = true;
    o.anchorTop = true;
    o.anchorBottom = true;
    o.yesno = yesno;
    o.target = target;
    o.onclick = onclick;
    o.yes = nil;
    o.player = player;
    o.no = nil;
    o.ok = nil;
    o.param1 = param1;
    o.param2 = param2;
    o.destroyOnClick = false;
    return o;
end

