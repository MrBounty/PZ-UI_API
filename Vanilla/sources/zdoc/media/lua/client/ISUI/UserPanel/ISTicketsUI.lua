--***********************************************************
--**              	  ROBERT JOHNSON                       **
--***********************************************************

---@class ISTicketsUI : ISPanel
ISTicketsUI = ISPanel:derive("ISTicketsUI");
ISTicketsUI.messages = {};

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)
local FONT_HGT_LARGE = getTextManager():getFontHeight(UIFont.Large)

local HEADER_HGT = FONT_HGT_SMALL + 2 * 2

--************************************************************************--
--** ISTicketsUI:initialise
--**
--************************************************************************--

function ISTicketsUI:initialise()
    ISPanel.initialise(self);
    local btnWid = 100
    local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local btnHgt2 = 18
    local padBottom = 10

    self.no = ISButton:new(10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Cancel"), self, ISTicketsUI.onClick);
    self.no.internal = "CANCEL";
    self.no.anchorTop = false
    self.no.anchorBottom = true
    self.no:initialise();
    self.no:instantiate();
    self.no.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.no);

    local y = 20 + FONT_HGT_MEDIUM + HEADER_HGT
    self.datas = ISScrollingListBox:new(10, y, self.width - 20, self.height - padBottom - btnHgt - padBottom - y);
    self.datas:initialise();
    self.datas:instantiate();
    self.datas.itemheight = FONT_HGT_SMALL + 2 * 2;
    self.datas.selected = 0;
    self.datas.joypadParent = self;
    self.datas.font = UIFont.NewSmall;
    self.datas.doDrawItem = self.drawDatas;
    self.datas.drawBorder = true;
    self:addChild(self.datas);

    self.addTicketBtn = ISButton:new(self:getWidth() - btnWid - 10,  self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("IGUI_TicketUI_AddTicket"), self, ISTicketsUI.onClick);
    self.addTicketBtn.internal = "ADDTICKET";
    self.addTicketBtn.anchorTop = false
    self.addTicketBtn.anchorBottom = true
    self.addTicketBtn:initialise();
    self.addTicketBtn:instantiate();
    self.addTicketBtn.borderColor = {r=1, g=1, b=1, a=0.1};
    self.addTicketBtn:setWidthToTitle(btnWid)
    self.addTicketBtn:setX(self.width - self.addTicketBtn.width - 10)
    self:addChild(self.addTicketBtn);

    self:getTickets();

end

function ISTicketsUI:getTickets()
    getTickets(self.player:getUsername());
end

function ISTicketsUI:populateList()
    self.datas:clear();
    for i=0,self.tickets:size()-1 do
        local ticket = self.tickets:get(i);
        local item = {}
        item.ticket = ticket

        item.richText = ISRichTextLayout:new(self.datas:getWidth() - 100 - 10 * 2)
        item.richText.marginLeft = 0
        item.richText.marginTop = 0
        item.richText.marginRight = 0
        item.richText.marginBottom = 0
        item.richText:setText(ticket:getMessage())
        item.richText:initialise()
        item.richText:paginate()

        if ticket:getAnswer() then
            item.richText2 = ISRichTextLayout:new(self.datas:getWidth() - 20 - 10 * 2)
            item.richText2.marginLeft = 0
            item.richText2.marginTop = 0
            item.richText2.marginRight = 0
            item.richText2.marginBottom = 0
            item.richText2:setText(ticket:getAnswer():getAuthor() .. ": " .. ticket:getAnswer():getMessage())
            item.richText2:initialise()
            item.richText2:paginate()
        end

        self.datas:addItem(ticket:getAuthor(), item);
    end
end

function ISTicketsUI:drawDatas(y, item, alt)
    local a = 0.9;
    local answerHeight = 0;

--    self.parent.selectedFaction = nil;
    self:drawRectBorder(0, (y), self:getWidth(), item.height - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), item.height - 1, 0.3, 0.7, 0.35, 0.15);
--        self.parent.viewBtn.enable = true;
--        self.parent.selectedFaction = item.item;
    end

    local ticket = item.item.ticket
    self:drawText(ticket:getTicketID() .. "", 10, y + 2, 1, 1, 1, a, self.font);
    item.item.richText:render(110, y + 2, self)
    local messageHeight = math.max(item.item.richText:getHeight() + 4, self.itemheight)

    if ticket:getAnswer() then
        answerHeight = math.max(item.item.richText2:getHeight() + 4, self.itemheight)
--        self:drawText("Answer", 30, y + 2 + messageHeight, 1, 1, 1, a, self.font);
        item.item.richText2:render(20, y + 2 + messageHeight, self)
        self:drawRect(0, (y + messageHeight), self:getWidth(), answerHeight - 1, 0.15, 1, 1, 1);
    end

    self:drawRect(100, y-1, 1, messageHeight, 1, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    return y + messageHeight + answerHeight;
end

function ISTicketsUI:prerender()
    local z = 10;
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawText(getText("UI_userpanel_tickets"), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, getText("UI_userpanel_tickets")) / 2), z, 1,1,1,1, UIFont.Medium);
end

function ISTicketsUI:render()
    self:drawRectBorder(self.datas.x, self.datas.y - HEADER_HGT, self.datas:getWidth(), HEADER_HGT + 1, 1, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawRect(self.datas.x, 1 + self.datas.y - HEADER_HGT, self.datas.width, HEADER_HGT,self.listHeaderColor.a,self.listHeaderColor.r, self.listHeaderColor.g, self.listHeaderColor.b);
    self:drawRect(self.datas.x + 100, 1 + self.datas.y - HEADER_HGT, 1, HEADER_HGT,1,self.borderColor.r, self.borderColor.g, self.borderColor.b);

    self:drawText("TicketID", self.datas.x + 5, self.datas.y - HEADER_HGT + 2, 1,1,1,1,UIFont.Small);
    self:drawText("Message", self.datas.x + 110, self.datas.y - HEADER_HGT + 2, 1,1,1,1,UIFont.Small);
end

function ISTicketsUI:onClick(button)
    if button.internal == "CANCEL" then
        self:close()
    end
    if button.internal == "ADDTICKET" then
        local inset = 2
        local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
        local titleBarHeight = 16
        local height = titleBarHeight + 8 + FONT_HGT_SMALL + 8 + FONT_HGT_MEDIUM * 5 + inset * 2 + btnHgt + 10 * 2
        height = math.max(height, 200)
        local modal = ISTextBox:new(self.x + 50, 200, 400, height, getText("IGUI_TicketUI_AddTicket"), "", self, ISTicketsUI.onAddTicket);
        modal:setNumberOfLines(5)
        modal:setMaxLines(5)
        modal:setMultipleLine(true)
        modal.changedName = button.changedName;
        modal:initialise();
        modal:addToUIManager();
    end
end

function ISTicketsUI:close()
    self:setVisible(false)
    self:removeFromUIManager()
end

ISTicketsUI.gotTickets = function(tickets)
    if ISTicketsUI.instance and ISTicketsUI.instance:isVisible() then
        ISTicketsUI.instance.tickets = tickets;
        ISTicketsUI.instance:populateList();
    end
end

function ISTicketsUI:onAddTicket(button)
    if button.internal == "OK" then
        if (button.parent.entry:getText() and button.parent.entry:getText() ~= "")then
            addTicket(self.player:getUsername(), button.parent.entry:getText(), -1);
        end
    end
end

--************************************************************************--
--** ISTicketsUI:new
--**
--************************************************************************--
function ISTicketsUI:new(x, y, width, height, player)
    local o = {}
    x = getCore():getScreenWidth() / 2 - (width / 2);
    y = getCore():getScreenHeight() / 2 - (height / 2);
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.listHeaderColor = {r=0.4, g=0.4, b=0.4, a=0.3};
    o.width = width;
    o.height = height;
    o.player = player;
    o.selectedFaction = nil;
    o.moveWithMouse = true;
    o.tickets = nil;
    ISTicketsUI.instance = o;
    return o;
end

Events.ViewTickets.Add(ISTicketsUI.gotTickets)
