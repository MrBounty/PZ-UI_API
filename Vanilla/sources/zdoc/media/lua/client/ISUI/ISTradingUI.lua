--***********************************************************
--**              	  ROBERT JOHNSON                       **
--***********************************************************

---@class ISTradingUI : ISPanel
ISTradingUI = ISPanel:derive("ISTradingUI");
ISTradingUI.messages = {};
ISTradingUI.CoolDownMessage = 300;
ISTradingUI.States = {};
ISTradingUI.States.PlayerClosedWindow = 0;
ISTradingUI.States.SealOffer = 1;
ISTradingUI.States.UnSealOffer = 2;
ISTradingUI.States.FinalizeDeal = 3;
ISTradingUI.MaxItems = 20;

--************************************************************************--
--** ISTradingUI:initialise
--**
--************************************************************************--

function ISTradingUI:initialise()
    ISPanel.initialise(self);
    local btnWid = 100
    local btnHgt = 25
    local btnHgt2 = 18
    local padBottom = 10
    local listWidh = (self.width / 2) - 15;
    local listHeight = 250;

    self.infoBtn = ISButton:new(10, 10, 70, 25, getText("UI_InfoBtn"), self, ISTradingUI.onClick);
    self.infoBtn.internal = "INFO";
    self.infoBtn:initialise();
    self.infoBtn:instantiate();
    self.infoBtn.borderColor = { r = 1, g = 1, b = 1, a = 0.4 };
    self:addChild(self.infoBtn);

    self.no = ISButton:new(10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("UI_Cancel"), self, ISTradingUI.onClick);
    self.no.internal = "CANCEL";
    self.no:initialise();
    self.no:instantiate();
    self.no.borderColor = {r=1, g=1, b=1, a=0.4};
    self:addChild(self.no);

    self.yourOfferDatas = ISScrollingListBox:new(10, 70, listWidh, listHeight);
    self.yourOfferDatas:initialise();
    self.yourOfferDatas:instantiate();
    self.yourOfferDatas.itemheight = 22;
    self.yourOfferDatas.selected = 0;
    self.yourOfferDatas.joypadParent = self;
    self.yourOfferDatas.font = UIFont.NewSmall;
    self.yourOfferDatas.doDrawItem = self.drawOffer;
    self.yourOfferDatas.onMouseUp = self.yourOfferMouseUp;
    self.yourOfferDatas.drawBorder = true;
    self:addChild(self.yourOfferDatas);

    self.remove = ISButton:new(self.yourOfferDatas.x + self.yourOfferDatas.width - btnWid, self.yourOfferDatas.y + self.yourOfferDatas.height + 5, btnWid, btnHgt - 3, getText("IGUI_TicketUI_RemoveTicket"), self, ISTradingUI.onClick);
    self.remove.internal = "REMOVE";
    self.remove:initialise();
    self.remove:instantiate();
    self.remove.borderColor = {r=1, g=1, b=1, a=0.4};
    self:addChild(self.remove);

    self.hisOfferDatas = ISScrollingListBox:new(self.yourOfferDatas.x + self.yourOfferDatas.width + 10, self.yourOfferDatas.y, listWidh, listHeight);
    self.hisOfferDatas:initialise();
    self.hisOfferDatas:instantiate();
    self.hisOfferDatas.itemheight = 22;
    self.hisOfferDatas.selected = 0;
    self.hisOfferDatas.joypadParent = self;
    self.hisOfferDatas.font = UIFont.NewSmall;
    self.hisOfferDatas.doDrawItem = self.drawOffer;
    self.hisOfferDatas.drawBorder = true;
    self:addChild(self.hisOfferDatas);

    self.sealOffer = ISTickBox:new(self.yourOfferDatas.x, self.yourOfferDatas.y + self.yourOfferDatas.height + 30, getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_TradingUI_MaxItemReached")) + 20, 18, "", self, ISTradingUI.onSealOffer);
    self.sealOffer:initialise();
    self.sealOffer:instantiate();
    self.sealOffer.selected[1] = false;
    self.sealOffer:addOption(getText("IGUI_TradingUI_SealOffer"));
    self:addChild(self.sealOffer);
    self.sealOffer.tooltip = getText("IGUI_TradingUI_SealOfferTooltip");

    self.historic = ISButton:new(self.hisOfferDatas.x + self.hisOfferDatas.width - (math.max(btnWid, getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_TradingUI_Historical")) + 10)), self.hisOfferDatas.y + self.hisOfferDatas.height + 5, btnWid, btnHgt - 3, getText("IGUI_TradingUI_Historical"), self, ISTradingUI.onClick);
    self.historic.internal = "HISTORIC";
    self.historic:initialise();
    self.historic:instantiate();
    self.historic.borderColor = {r=1, g=1, b=1, a=0.4};
    self:addChild(self.historic);

    self.acceptDeal = ISButton:new(self.hisOfferDatas.x + self.hisOfferDatas.width - (math.max(btnWid, getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_TradingUI_AcceptDeal")) + 10)), self.no.y, btnWid, btnHgt - 3, getText("IGUI_TradingUI_AcceptDeal"), self, ISTradingUI.onClick);
    self.acceptDeal.internal = "ACCEPTDEAL";
    self.acceptDeal:initialise();
    self.acceptDeal:instantiate();
    self.acceptDeal.tooltip = getText("IGUI_TradingUI_AcceptDealTooltip");
    self.acceptDeal.borderColor = {r=1, g=1, b=1, a=0.4};
    self:addChild(self.acceptDeal);
end

function ISTradingUI:onSealOffer(clickedOption, enabled)
    if enabled then
        tradingUISendUpdateState(self.player, self.otherPlayer, ISTradingUI.States.SealOffer);
    else
        tradingUISendUpdateState(self.player, self.otherPlayer, ISTradingUI.States.UnSealOffer);
    end
end

function ISTradingUI:addItemToYourOffer(item)
    if #self.yourOfferDatas.items + 1 > ISTradingUI.MaxItems then
        self:setHistoryMessage(getText("IGUI_TradingUI_MaxItemReached", ISTradingUI.MaxItems), false, false, false);
        return;
    end
    if luautils.haveToBeTransfered(self.player, item) then
        self:setHistoryMessage(getText("IGUI_TradingUI_ItemNeedTransfer", item:getName()), false, false, false);
        return;
    end
    if self.player:isEquipped(item) or self.player:isEquippedClothing(item) then
        self:setHistoryMessage(getText("IGUI_TradingUI_ItemNeedUnequip", item:getName()), false, false, false);
        return;
    end
    if item:isFavorite() then
        self:setHistoryMessage(getText("IGUI_TradingUI_CantAddFavorite"), false, false, false);
        return;
    end
    local add = true;
    for i,v in ipairs(self.yourOfferDatas.items) do
        if v.item == item then
            add = false;
            break;
        end
    end
    if add then
        self.yourOfferDatas:addItem(item:getName(), item);
        tradingUISendAddItem(self.player, self.otherPlayer, item);
        self.otherSealedOffer = false;
        if #self.yourOfferDatas.items == 1 then
            self.yourOfferDatas.selected = 1;
        end
    end
end

-- add item from your inventory to your offer
function ISTradingUI:yourOfferMouseUp(x, y)
    if self.parent.sealOffer.selected[1] then
        return;
    end
    if self.vscroll then
        self.vscroll.scrolling = false;
    end
    local counta = 1;
    if ISMouseDrag.dragging then
        for i,v in ipairs(ISMouseDrag.dragging) do
           counta = 1;
           if instanceof(v, "InventoryItem") then
                self.parent:addItemToYourOffer(v);
           else
               if  v.invPanel.collapsed[v.name] then
                   counta = 1;
                   for i2,v2 in ipairs(v.items) do
                       if counta > 1 then
                           self.parent:addItemToYourOffer(v2);
                       end
                       counta = counta + 1;
                   end
               end
           end

        end
    end
end

function ISTradingUI:populateList()
end

function ISTradingUI:update()
    if not getPlayerByOnlineID(self.otherPlayer:getOnlineID()) then
        self.blockingMessage = getText("IGUI_TradingUI_ClosedTrade", self.otherPlayer:getDisplayName())
    end
    if not self.blockingMessage and (math.abs(getPlayerByOnlineID(self.otherPlayer:getOnlineID()):getX() - self.player:getX()) > 2 or
            math.abs(getPlayerByOnlineID(self.otherPlayer:getOnlineID()):getY() - self.player:getY()) > 2) then
        self.blockingMessage2 = getText("IGUI_TradingUI_TooFarAway", self.otherPlayer:getDisplayName())
        return;
    else
        self.blockingMessage2 =nil;
    end
    -- check if an item isn't in player's inventory, then we remove it
    for i,v in ipairs(self.yourOfferDatas.items) do
       if luautils.haveToBeTransfered(self.player, v.item) then
           self:removeItem(v);
           break;
       end
    end
    if not self:isVisible() then
        if self.historicalUI and self.historicalUI:isVisible() then
            self.historicalUI:setVisible(false);
            self.historicalUI:removeFromUIManager();
        end
        if self.infoRichText and self.infoRichText:isVisible() then
            self.infoRichText:setVisible(false);
            self.infoRichText:removeFromUIManager();
        end
    end
end

function ISTradingUI:removeItem(item)
    tradingUISendRemoveItem(self.player, self.otherPlayer, item.index);
    self.yourOfferDatas:removeItemByIndex(item.index);
    ISTradingUI.instance.otherSealedOffer = false;
end

function ISTradingUI:drawOffer(y, item, alt)
    local a = 0.9;

    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    if self.selected == item.index and not self.parent.sealOffer.selected[1] then
        if self == self.parent.yourOfferDatas then
            self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
            self.parent.selectedItem = item;
        end
    end

    self:drawTextureScaledAspect(item.item:getTex(), 5, y + 2, 18, 18, 1, item.item:getR(), item.item:getG(), item.item:getB());
    self:drawText(item.text, 25, y + 2, 1, 1, 1, a, self.font);

    return y + self.itemheight;
end

function ISTradingUI:prerender()
    local z = 15;
    local splitPoint = 100;
    local x = 10;
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawText(getText("IGUI_TradingUI_Title"), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, getText("IGUI_TradingUI_Title")) / 2), z, 1,1,1,1, UIFont.Medium);
    self:drawText(getText("IGUI_TradingUI_YourOffer"), self.yourOfferDatas.x, self.yourOfferDatas.y - 32, 1,1,1,1, UIFont.Small);
    self:drawText(getText("IGUI_TradingUI_HisOffer", self.otherPlayer:getDisplayName()), self.hisOfferDatas.x, self.hisOfferDatas.y - 32, 1,1,1,1, UIFont.Small);

    local yourItems = getText("IGUI_TradingUI_Items", #self.yourOfferDatas.items, ISTradingUI.MaxItems);
    self:drawText(yourItems, self.yourOfferDatas.x, self.yourOfferDatas.y - 20, 1,1,1,1, UIFont.Small);
    local hisItems = getText("IGUI_TradingUI_Items", #self.hisOfferDatas.items, ISTradingUI.MaxItems);
    self:drawText(hisItems, self.hisOfferDatas.x, self.hisOfferDatas.y - 20, 1,1,1,1, UIFont.Small);

    if self.otherSealedOffer then
        self:drawText(getText("IGUI_TradingUI_OtherPlayerSealedOffer", self.otherPlayer:getDisplayName()), self.sealOffer.x, self.sealOffer.y + self.sealOffer.height + 5, 0.2,1,0.2,1, UIFont.Small);
    end
    z = z + 30;
end

function ISTradingUI:updateTooltip()
    local x = self:getMouseX();
    local y = self:getMouseY();
    local item = nil;
    if x >= self.hisOfferDatas:getX() and x <= self.hisOfferDatas:getX() + self.hisOfferDatas:getWidth() and y >= self.hisOfferDatas:getY() and y <= self.hisOfferDatas:getY() + self.hisOfferDatas:getHeight() then
        y = self.hisOfferDatas:rowAt(self.hisOfferDatas:getMouseX(), self.hisOfferDatas:getMouseY())
        if self.hisOfferDatas.items[y] then
            item = self.hisOfferDatas.items[y];
        end
    end
    if x >= self.yourOfferDatas:getX() and x <= self.yourOfferDatas:getX() + self.yourOfferDatas:getWidth() and y >= self.yourOfferDatas:getY() and y <= self.yourOfferDatas:getY() + self.yourOfferDatas:getHeight() then
        y = self.yourOfferDatas:rowAt(self.yourOfferDatas:getMouseX(), self.yourOfferDatas:getMouseY())
        if self.yourOfferDatas.items[y] then
            item = self.yourOfferDatas.items[y];
        end
    end
    if item then
        if self.toolRender then
            self.toolRender:setItem(item.item);
            if not self:getIsVisible() then
                self.toolRender:setVisible(false);
            else
                self.toolRender:setVisible(true);
                self.toolRender:addToUIManager();
                self.toolRender:bringToTop();
            end
        else
            self.toolRender = ISToolTipInv:new(item.item);
            self.toolRender:initialise();
            self.toolRender:addToUIManager();
            if not self:getIsVisible() then
                self.toolRender:setVisible(true);
            end
            self.toolRender:setOwner(self);
            self.toolRender:setCharacter(self.player);
            self.toolRender:setX(self:getMouseX());
            self.toolRender:setY(self:getMouseY());
            self.toolRender.followMouse = true;
        end
    else
        if self.toolRender then
            self.toolRender:setVisible(false)
        end
    end


end

function ISTradingUI:updateButtons()
    self.remove.enable = false;
    self.sealOffer.enable = false;
    self.acceptDeal.enable = false;
    self.historic.enable = false;
    self.infoBtn.enable = false;
    self.acceptDeal.tooltip = nil;
    if self.blockingMessage or self.blockingMessage2 then
        return;
    end
    self.infoBtn.enable = true;
    if #self.yourOfferDatas.items > 0 and self.selectedItem and not self.sealOffer.selected[1] then
        self.remove.enable = true;
    end
    self.sealOffer.enable = true;
    self.historic.enable = true;
    if self.sealOffer.selected[1] and self.otherSealedOffer then
        if #self.yourOfferDatas.items > 0 or #self.hisOfferDatas.items > 0 then
            self.acceptDeal.enable = true;
        else
            self.acceptDeal.tooltip = getText("IGUI_TradingUI_TradeAtLeastOne");
        end
    elseif not self.blockingMessage then
        self.acceptDeal.tooltip = getText("IGUI_TradingUI_AcceptDealTooltip");
    end
end

function ISTradingUI:render()
    self:updateButtons();
    self:updateTooltip();
    local doRect = true;
    if self.sealOffer.selected[1] then
        self:drawRect(self.yourOfferDatas.x, self.yourOfferDatas.y, self.yourOfferDatas.width, self.yourOfferDatas.height, 0.8, 0, 0, 0);
        self:drawText(getText("IGUI_TradingUI_OfferSealed"), (self.yourOfferDatas.x + self.yourOfferDatas.width)/2 - (getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_TradingUI_OfferSealed")) / 2), self.yourOfferDatas.y + (self.yourOfferDatas.height/2) - 5, 1,1,1,1, UIFont.Small);
    end
    if self.blockingMessage or self.blockingMessage2 then
        self:drawRect(0, 0, self.width, self.height - 35, 0.8, 0, 0, 0);
        self:drawText(self.blockingMessage or self.blockingMessage2, self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, self.blockingMessage or self.blockingMessage2) / 2), (self.height / 2) - 5, 1,1,1,1, UIFont.Medium);
        self.no:setTitle(getText("IGUI_CraftUI_Close"))
        doRect = false;
    end

    if doRect then
        self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    end

    if self.historyMessageCD > 0 then
       self.historyMessageCD = self.historyMessageCD - 1;
       self:drawText(self.historyMessage, self.width/2 - (getTextManager():MeasureStringX(UIFont.Small, self.historyMessage) / 2), self.no.y - 17, 1,1,1,1, UIFont.Small);
    end
end

function ISTradingUI:onClick(button)
    if button.internal == "INFO" then
        if not self.infoRichText then
            self.infoRichText = ISModalRichText:new(getCore():getScreenWidth()/2-400,getCore():getScreenHeight()/2-300,600,600,getText("UI_TradingUIHelp"), false);
            self.infoRichText.destroyOnClick = false;
            self.infoRichText:initialise();
            self.infoRichText:addToUIManager();
            self.infoRichText.chatText:paginate();
            self.infoRichText.backgroundColor = {r=0, g=0, b=0, a=1};
            self.infoRichText:setHeight(self.infoRichText.chatText:getHeight() + 40);
            self.infoRichText:setY(getCore():getScreenHeight()/2-(self.infoRichText:getHeight()/2));
            self.infoRichText:setVisible(true);
        else
            self.infoRichText:setVisible(not self.infoRichText:getIsVisible());
            self.infoRichText:bringToTop();
        end
    end
    if button.internal == "CANCEL" then
        if self.historicalUI and self.historicalUI:isVisible() then
            self.historicalUI:setVisible(false);
            self.historicalUI:removeFromUIManager();
        end
        self:setVisible(false);
        self:removeFromUIManager();
        tradingUISendUpdateState(self.player, self.otherPlayer, ISTradingUI.States.PlayerClosedWindow);
    end
    if button.internal == "REMOVE" then
        self:removeItem(self.selectedItem)
    end
    if button.internal == "HISTORIC" then
        if not self.historicalUI or not self.historicalUI:isVisible() then
            local ui = ISTradingUIHistorical:new(self.x + self.width + 5, self.y + self.height / 2 - 150, 300, 300, self.historical, self.otherPlayer)
            ui:initialise();
            ui:addToUIManager();
            self.historicalUI = ui;
        else
            self.historicalUI:bringToTop();
        end
    end
    if button.internal == "ACCEPTDEAL" then
        tradingUISendUpdateState(self.player, self.otherPlayer, ISTradingUI.States.FinalizeDeal);
        self:finalizeDeal();
    end
end

function ISTradingUI:finalizeDeal()
    local itemsToGive = {};
    for i,v in ipairs(self.yourOfferDatas.items) do
        table.insert(itemsToGive, v.item);
    end
    local itemsToReceive = {};
    for i,v in ipairs(self.hisOfferDatas.items) do
        table.insert(itemsToReceive, v.item);
    end
    self:setVisible(false);
    self:removeFromUIManager();
    ISTimedActionQueue.add(ISFinalizeDealAction:new(self.player, self.otherPlayer, itemsToGive, itemsToReceive, (#itemsToGive + #itemsToReceive)));
end

--************************************************************************--
--** ISTradingUI:new
--**
--************************************************************************--
function ISTradingUI:new(x, y, width, height, player, otherPlayer)
    local o = {}
    x = getCore():getScreenWidth() / 2 - (width / 2);
    y = getCore():getScreenHeight() / 2 - (height / 2);
    width = 350;
    height = 450;
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    player:StopAllActionQueue();
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.listHeaderColor = {r=0.4, g=0.4, b=0.4, a=0.3};
    o.width = width;
    o.height = height;
    o.player = player;
    o.otherPlayer = otherPlayer;
    o.moveWithMouse = true;
    o.yourOffer = nil;
    o.hisOffer = nil;
    o.selectedItem = nil;
    o.pendingRequest = false;
    o.historyMessage = nil;
    o.historyMessageCD = 0;
    o.blockingMessage = nil;
    o.otherSealedOffer = false;
    o.historical = {};
    ISTradingUI.instance = o;
    return o;
end

function ISTradingUI:onAnswerTradeRequest(button)
    if button.internal == "YES" then
        if ISTradingUI.instance and ISTradingUI.instance:isVisible() then
            tradingUISendUpdateState(ISTradingUI.instance.player, ISTradingUI.instance.otherPlayer, ISTradingUI.States.PlayerClosedWindow);
            ISTradingUI.instance:setVisible(false);
            ISTradingUI.instance:removeFromUIManager();
        end
        ISTradingUI.tradeQuestionUI = nil;
        local ui = ISTradingUI:new(50,50,500,550, getPlayer(), button.parent.requester)
        ui:initialise();
        ui:addToUIManager();
    end
    acceptTrading(getPlayer(), button.parent.requester, button.internal == "YES");
end

-- someone ask us to trade
ISTradingUI.ReceiveTradeRequest = function (requester)
    local modal = ISModalDialog:new(getCore():getScreenWidth() / 2 - 175,getCore():getScreenHeight() / 2 - 75, 350, 150, getText("IGUI_TradingUI_RequestTrade", requester:getDisplayName()), true, nil, ISTradingUI.onAnswerTradeRequest);
    modal:initialise()
    modal:addToUIManager()
    modal.requester = requester;
    modal.moveWithMouse = true;
    ISTradingUI.tradeQuestionUI = modal;
end

-- the other player accepted the trade!
ISTradingUI.AcceptedTrade = function(accepted)
    if ISTradingUI.instance and ISTradingUI.instance:isVisible() then
        ISTradingUI.instance.pendingRequest = false;
        ISTradingUI.instance.blockingMessage = nil;
        if not accepted then
            ISTradingUI.instance.blockingMessage = getText("IGUI_TradingUI_RefusedTrade", ISTradingUI.instance.otherPlayer:getDisplayName());
        end
    end
end

-- other player has added a new item to his offer
ISTradingUI.OtherAddNewItem = function(player, item)
    if ISTradingUI.instance and ISTradingUI.instance:isVisible() then
        ISTradingUI.instance.hisOfferDatas:addItem(item:getName(), item);
        ISTradingUI.instance:setHistoryMessage(getText("IGUI_TradingUI_AddedItem", player:getDisplayName(), item:getName()), true, true, false);
        ISTradingUI.instance.sealOffer.selected[1] = false;
        ISTradingUI.instance.otherSealedOffer = false;
    end
end

-- other player removed an item from his offer
ISTradingUI.RemoveItem = function(player, index)
    if ISTradingUI.instance and ISTradingUI.instance:isVisible() then
        local itemRemoved = ISTradingUI.instance.hisOfferDatas.items[index];
        ISTradingUI.instance.hisOfferDatas:removeItemByIndex(index);
        ISTradingUI.instance:setHistoryMessage(getText("IGUI_TradingUI_RemovedItem", player:getDisplayName(), itemRemoved.item:getName()), true, false, true);
        ISTradingUI.instance.sealOffer.selected[1] = false;
        ISTradingUI.instance.otherSealedOffer = false;
    end
end

function ISTradingUI:setHistoryMessage(message, publishInHistorical, add, remove)
    if self and self:isVisible() then
        self.historyMessage = message;
        local hMessage = {};
        hMessage.message = message;
        hMessage.add = add;
        hMessage.remove = remove;
        self.historyMessageCD = ISTradingUI.CoolDownMessage;
        if publishInHistorical then
            table.insert(self.historical, hMessage);
            if self.historicalUI and self.historicalUI:isVisible() then
                self.historicalUI:populateList(self.historical);
            end
        end
    end
end

-- other player did something on his tradeUI (closed, seal his offer, finalize deal...)
ISTradingUI.UpdateState = function(player, state)
    if state == ISTradingUI.States.PlayerClosedWindow then
        if ISTradingUI.tradeQuestionUI and ISTradingUI.tradeQuestionUI:isVisible() then
            ISTradingUI.tradeQuestionUI:setVisible(false);
            ISTradingUI.tradeQuestionUI:removeFromUIManager();
        elseif ISTradingUI.instance and ISTradingUI.instance:isVisible() and ISTradingUI.instance.otherPlayer == player then
            ISTradingUI.instance.blockingMessage = getText("IGUI_TradingUI_ClosedTrade", player:getDisplayName());
        end
    end
    if ISTradingUI.instance and ISTradingUI.instance:isVisible() then
        if state == ISTradingUI.States.SealOffer then
            ISTradingUI.instance.otherSealedOffer = true;
            ISTradingUI.instance:setHistoryMessage(getText("IGUI_TradingUI_OtherPlayerSealedOffer", ISTradingUI.instance.otherPlayer:getDisplayName()), true, false, false);
        end
        if state == ISTradingUI.States.UnSealOffer then
            ISTradingUI.instance.otherSealedOffer = false;
            ISTradingUI.instance:setHistoryMessage(getText("IGUI_TradingUI_OtherPlayerUnSealedOffer", ISTradingUI.instance.otherPlayer:getDisplayName()), true, false, false);
        end
        if state == ISTradingUI.States.FinalizeDeal then
            ISTradingUI.instance:finalizeDeal();
        end
    end
end

Events.RequestTrade.Add(ISTradingUI.ReceiveTradeRequest)
Events.AcceptedTrade.Add(ISTradingUI.AcceptedTrade)
Events.TradingUIAddItem.Add(ISTradingUI.OtherAddNewItem)
Events.TradingUIRemoveItem.Add(ISTradingUI.RemoveItem)
Events.TradingUIUpdateState.Add(ISTradingUI.UpdateState)
