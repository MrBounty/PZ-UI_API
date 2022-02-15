--***********************************************************
--**              	  ROBERT JOHNSON                       **
--**            UI display with a question or text         **
--**          can display a yes/no button or ok btn        **
--***********************************************************

---@class ISAdminMessage : ISPanel
ISAdminMessage = ISPanel:derive("ISAdminMessage");
ISAdminMessage.messages = {};

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

--************************************************************************--
--** ISAdminMessage:initialise
--**
--************************************************************************--

function ISAdminMessage:initialise()
    ISPanel.initialise(self);
    local btnWid = 100
    local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local padBottom = 10

    if self.px > 0 then
        self.yes = ISButton:new((self:getWidth() / 2) - btnWid - 5, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Yes"), self, ISAdminMessage.onClick);
        self.yes.internal = "YES";
        self.yes.anchorTop = false
        self.yes.anchorBottom = true
        self.yes:initialise();
        self.yes:instantiate();
        self.yes.borderColor = {r=1, g=1, b=1, a=0.1};
        self:addChild(self.yes);

        self.no = ISButton:new((self:getWidth() / 2) + 5, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_No"), self, ISAdminMessage.onClick);
        self.no.internal = "NO";
        self.no.anchorTop = false
        self.no.anchorBottom = true
        self.no:initialise();
        self.no:instantiate();
        self.no.borderColor = {r=1, g=1, b=1, a=0.1};
        self:addChild(self.no);
    else
        self.yes = ISButton:new((self:getWidth() / 2) - btnWid / 2, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Ok"), self, ISAdminMessage.onClick);
        self.yes.internal = "OK";
        self.yes.anchorTop = false
        self.yes.anchorBottom = true
        self.yes:initialise();
        self.yes:instantiate();
        self.yes.borderColor = {r=1, g=1, b=1, a=0.1};
        self:addChild(self.yes);
    end

    self.chatText = ISRichTextPanel:new(2, 2, self.width - 4, self.height - 30);
    self.chatText.marginRight = self.chatText.marginLeft;
    self.chatText:initialise();

    self:addChild(self.chatText);
    self.chatText.background = false;
    self.chatText.text = self.text;
    self.chatText:paginate();
end

function ISAdminMessage:updateButtons()
    local btnHgt = 25
    local padBottom = 10

    self.yes:setY(self:getHeight() - padBottom - btnHgt);
    if self.no then
        self.no:setY(self:getHeight() - padBottom - btnHgt);
    end
end

function ISAdminMessage:destroy()
    UIManager.setShowPausedMessage(true);
    self:setVisible(false);
    if self.destroyOnClick then
        self:removeFromUIManager();
    end
    if UIManager.getSpeedControls() then
        UIManager.getSpeedControls():SetCurrentGameSpeed(1);
    end
end

function ISAdminMessage:onClick(button)
    for _,v in ipairs(ISAdminMessage.messages) do
        if v == self then
            table.remove(ISAdminMessage.messages, _);
        end
    end
    ISAdminMessage.RecalculPositions();
    self:destroy();
    if button.internal == "YES" then
        getPlayer():setX(self.px);
        getPlayer():setY(self.py);
        getPlayer():setZ(self.pz);
        getPlayer():setLx(self.px);
        getPlayer():setLy(self.py);
        getPlayer():setLz(self.pz);
    end
end

function ISAdminMessage:prerender()
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
end

function ISAdminMessage:onMouseDown(x, y)
    -- FIXME: this prevents clicks being passed to windows behind, but need to swallow clicks outside and mouse-move events as well
    return true
end

function ISAdminMessage:update()
    local btnHgt = FONT_HGT_SMALL + 3 * 2
    local padBottom = 10
    local minHeight = self.chatText:getY() + self.chatText:getHeight() + btnHgt + padBottom
    if self:getHeight() < minHeight then
        local dh = minHeight - self:getHeight()
        self:setHeight(minHeight)
        self:setY(self:getY() - dh / 2)
    end
end

--************************************************************************--
--** ISAdminMessage:new
--**
--************************************************************************--
function ISAdminMessage:new(x, y, width, height, text, px,py,pz)
    local o = {}
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    if y == 0 then
        o.y = o:getMouseY() - (height / 2)
        o:setY(o.y)
    end
    if x == 0 then
        o.x = o:getMouseX() - (width / 2)
        o:setX(o.x)
    end
    o.name = nil;
    o.backgroundColor = {r=0, g=0, b=0, a=0.5};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = true;
    o.anchorTop = true;
    o.anchorBottom = true;
    o.text = text;
    o.yes = nil;
    o.px = px;
    o.py = py;
    o.pz = pz;
    o.no = nil;
    o.destroyOnClick = true;
    return o;
end

ISAdminMessage.AddAdminMessage = function(message,x,y,z)
    local modal = nil;
    if x > -1 then
        message = message .. " <LINE> <LINE> Teleport?";
    end
    print("GET MSG", message)
    modal = ISAdminMessage:new(getCore():getScreenWidth() - 260, ((getCore():getScreenHeight() / 2) - 60), 250, 120, message, x,y,z);
    modal:initialise();
    modal:addToUIManager();
    table.insert(ISAdminMessage.messages, modal);
    ISAdminMessage.RecalculPositions();
end

ISAdminMessage.RecalculPositions = function()
    for _,v in ipairs(ISAdminMessage.messages) do
        v:setY((getCore():getScreenHeight() / 2) - (#ISAdminMessage.messages * 60) + (_ - 1) * 120);
    end
end

Events.OnAdminMessage.Add(ISAdminMessage.AddAdminMessage)

