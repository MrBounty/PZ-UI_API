--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

---@class ISMediaInfo : ISCollapsableWindow
ISMediaInfo = ISCollapsableWindow:derive("ISMediaInfo");
ISMediaInfo.instance = nil;

function ISMediaInfo.openPanel(_playerNum, _text)
    if _text then
        --ISMediaInfo.instance = nil;
        if ISMediaInfo.instance then
            ISMediaInfo.instance.richText:setText(_text);
            ISMediaInfo.instance.richText:paginate();
        else
            ISMediaInfo.instance = ISMediaInfo:new(0, 0, 280, 320, _playerNum, _text);
            ISMediaInfo.instance:initialise();
            ISMediaInfo.instance:instantiate();

            ISMediaInfo.instance:addToUIManager();
            ISMediaInfo.instance:setVisible(true);
        end

        return ISMediaInfo.instance;
    end
end

function ISMediaInfo:initialise()
    ISCollapsableWindow.initialise(self);
end

function ISMediaInfo:createChildren()
    ISCollapsableWindow.createChildren(self);

    local btnWid = 100
    local btnHgt = 25
    local pad = 10

    self.buttonOK = ISButton:new((self:getWidth() / 2) - (btnWid/2), self:getHeight() - pad - btnHgt, btnWid, btnHgt, getText("UI_Ok"), self, ISMediaInfo.onClick);
    self.buttonOK.internal = "OK";
    self.buttonOK:initialise();
    self.buttonOK:instantiate();
    self.buttonOK.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.buttonOK);

    local height = self:getHeight() - btnHgt - (pad*4);
    self.richText = ISRichTextPanel:new(self:getWidth() / 2 - ((self:getWidth() - 20) / 2), pad*2, self:getWidth() - 20, height);
    self.richText.text = self.text;
    self.richText.borderColor = {r=1, g=1, b=1, a=0.4};
    self.richText.autosetheight = false;
    self.richText.defaultFont = UIFont.Small;
    self.richText.clip = true;
    self.richText:initialise();
    self.richText:instantiate();
    self.richText:addScrollBars(false);
    self.richText:paginate();
    self:addChild(self.richText);
end

function ISMediaInfo:destroy()
    self:setVisible(false);
    self:removeFromUIManager();
    ISMediaInfo.instance = nil;
    if JoypadState.players[self.playerNum+1] then
        local inv = getPlayerInventory(self.playerNum)
        setJoypadFocus(self.playerNum, inv:isReallyVisible() and inv or nil)
    end
end

function ISMediaInfo:onClick(button)
    if button==self.buttonOK then
        self:destroy();
    end
end

--collapsablewindow override
function ISMediaInfo:close()
    self:destroy();
end

function ISMediaInfo:prerender()
    self.pinButton:setVisible(false);
    self.collapseButton:setVisible(false);
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
end

function ISMediaInfo:render()

end

function ISMediaInfo:onGainJoypadFocus(joypadData)
    self.buttonOK:setJoypadButton(Joypad.Texture.AButton)
end

function ISMediaInfo:onJoypadDown(button)
    if button == Joypad.AButton then
        self.buttonOK:forceClick()
    end
end

--************************************************************************--
--** ISMediaInfo:new
--** size 280, 320
--************************************************************************--
function ISMediaInfo:new(x, y, width, height, playerNum, text)
    local o = {}
    o = ISCollapsableWindow:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    if y == 0 then
        o.y = (getCore():getScreenHeight() / 2) - (height / 2);
        o:setY(o.y);
    end
    if x == 0 then
        o.x = (getCore():getScreenWidth() / 2) - (width / 2);
        o:setX(o.x);
    end
    o.name = nil;
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = true;
    o.anchorTop = true;
    o.anchorBottom = true;
    o.text = text;
    o.fontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight();
    o.playerNum = playerNum;
    return o;
end
