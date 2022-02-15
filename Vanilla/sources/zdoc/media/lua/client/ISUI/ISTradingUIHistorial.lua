--***********************************************************
--**              	  ROBERT JOHNSON                       **
--**            UI display with a question or text         **
--**          can display a yes/no button or ok btn        **
--***********************************************************

---@class ISTradingUIHistorical : ISPanel
ISTradingUIHistorical = ISPanel:derive("ISTradingUIHistorical");
ISTradingUIHistorical.messages = {};

--************************************************************************--
--** ISTradingUIHistorical:initialise
--**
--************************************************************************--

function ISTradingUIHistorical:initialise()
    ISPanel.initialise(self);
    local btnWid = 100
    local btnHgt = 25
    local btnHgt2 = 18
    local padBottom = 10

    self.no = ISButton:new(self:getWidth() - btnWid - 10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("IGUI_CraftUI_Close"), self, ISTradingUIHistorical.onClick);
    self.no.internal = "OK";
    self.no.anchorTop = false
    self.no.anchorBottom = true
    self.no:initialise();
    self.no:instantiate();
    self.no.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.no);

    self.list = ISScrollingListBox:new(10, 40, self.width - 20, self.height - 80);
    self.list:initialise();
    self.list:instantiate();
    self.list.itemheight = 22;
    self.list.selected = 0;
    self.list.joypadParent = self;
    self.list.font = UIFont.NewSmall;
    self.list.doDrawItem = self.drawList;
    self.list.drawBorder = true;
    self:addChild(self.list);

    self:populateList();
end

function ISTradingUIHistorical:populateList(list)
    if list then
        self.msgList = list;
    end
    self.list:clear();

    for i,v in ipairs(self.msgList) do
       self.list:addItem(v.message, v);
    end
end

function ISTradingUIHistorical:drawList(y, item, alt)
    local a = 0.9;
    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    local r,g,b = 1,1,1;
    if item.item.add then
        r,g,b = 0.2,1,0.2;
    elseif item.item.remove then
        r,g,b = 1,0.2,0.2;
    end
    self:drawText(item.item.message, 10, y + 2,r,g,b, a, self.font);
    return y + self.itemheight;
end

function ISTradingUIHistorical:render()
end

function ISTradingUIHistorical:prerender()
    local z = 10;
    local splitPoint = 100;
    local x = 10;
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawText(getText("IGUI_ISTradingUIHistorical_Title", self.otherPlayer:getDisplayName()), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, getText("IGUI_ISTradingUIHistorical_Title")) / 2), z, 1,1,1,1, UIFont.Medium);
end

function ISTradingUIHistorical:onClick(button)
    if button.internal == "OK" then
        self:setVisible(false);
        self:removeFromUIManager();
    end
end

--************************************************************************--
--** ISTradingUIHistorical:new
--**
--************************************************************************--
function ISTradingUIHistorical:new(x, y, width, height, list, otherPlayer)
    local o = {}
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.width = width;
    o.height = height;
    o.msgList = list;
    o.otherPlayer = otherPlayer;
    o.moveWithMouse = true;
    ISTradingUIHistorical.instance = o;
    return o;
end
