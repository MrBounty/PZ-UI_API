require "ISUI/ISRichTextPanel"
require "ISUI/ISPanelJoypad"

---@class ISServerSavingMessage : ISPanelJoypad
ISServerSavingMessage = ISPanelJoypad:derive("ISServerSavingMessage");

function ISServerSavingMessage:initialise()
    ISPanel.initialise(self);

    local panel = ISRichTextPanel:new(0,0, self.width, self.height - 30);
    panel:initialise();

    self:addChild(panel);
    self.richtext = panel;
    self.richtext.background = false;
    self.richtext.text = self.text;
    self.richtext:paginate();
end

function ISServerSavingMessage:destroy()

end

function ISServerSavingMessage:prerender()
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

end

function ISServerSavingMessage:render()

end

ISServerSavingMessage.showPauseMessage = function()
    local width = 225;
    local height = 250;
    local x = getCore():getScreenWidth() / 2 - width / 2;
    local y = getCore():getScreenHeight() / 2 - 200;
    local text = "<CENTRE> <SIZE:medium> Server saving game. Please wait. <LINE> <LEFT> <IMAGE:media/ui/spiffo/packing.png> <LINE>";
    modal = ISServerSavingMessage:new(x, y, width, height, text);
    modal:initialise();
    modal:addToUIManager();
end

ISServerSavingMessage.showSavingFinishMessage = function()
    if modal then
        modal:removeFromUIManager();
    end
end

function ISServerSavingMessage:new(x, y, width, height, text)
    local o = {}
    o = ISPanelJoypad:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.backgroundColor = {r=0, g=0, b=0, a=0.5};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.width = width;
    o.height = height;
    o.text = text;
    o.anchorLeft = true;
    o.anchorRight = true;
    o.anchorTop = true;
    o.anchorBottom = true;
    return o;
end

Events.OnServerStartSaving.Add(ISServerSavingMessage.showPauseMessage);
Events.OnServerFinishSaving.Add(ISServerSavingMessage.showSavingFinishMessage);
