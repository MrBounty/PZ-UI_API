require "ISUI/ISPanelJoypad"

ISStatisticsUI = ISCollapsableWindow:derive("ISStatisticsUI");

function ISStatisticsUI:createChildren()
    ISCollapsableWindow.createChildren(self)
end

function ISStatisticsUI:close()
    self:setVisible(false);
    self:removeFromUIManager();
    ISStatisticsUI.instance = nil
end

function ISStatisticsUI:prerender()
    ISCollapsableWindow.prerender(self);
end

function ISStatisticsUI:initialise()
    ISCollapsableWindow.initialise(self)
    self.title = getText("IGUI_AdminPanel_HeaderShowStatistics")
end

function ISStatisticsUI:render()

    ISCollapsableWindow.render(self);

    if not self.isCollapsed then

        local statisticsData = getStatistics()
        local ping = getPing()

        local width = 80
        local startX = 120
        local startY = 20
        local a = 0.90
        local tr = 1
        local tg = 1
        local tb = 0.5
        local hr = 1
        local hg = 0.75
        local hb = 0.5
        local la = 0.05
        local lr = 0.9
        local lg = 0.9
        local lb = 0.9

        posX = startX
        posY = startY

        --self:drawTextRight("STATISTICS", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight("Client", posX+width, posY, hr, hg, hb, a, UIFont.NewMedium);
        self:drawTextRight("Server", posX+width+width, posY, hr, hg, hb, a, UIFont.NewMedium);
        posY = posY + 25

        self:drawTextRight("Zombies", posX, posY, hr, hg, hb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawRect(startX-width*1.3, posY, startX+width+width, 1, la*6, lr, lg, lb);
        posY = posY + 5
        self:drawTextRight("Total : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.clientZombiesTotal, posX+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverZombiesTotal, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawRect(posX-width*1.3, posY, posX+width+width, 20, la, lr, lg, lb);
        self:drawTextRight("Loaded : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.clientZombiesLoaded, posX+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverZombiesLoaded, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawTextRight("Simulated : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.clientZombiesSimulated, posX+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverZombiesSimulated, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawRect(posX-width*1.3, posY, posX+width+width, 20, la, lr, lg, lb);
        self:drawTextRight("Culled : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.clientZombiesCulled, posX+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverZombiesCulled, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawTextRight("Authorized : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.clientZombiesAuthorized, posX+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverZombiesAuthorized, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawRect(posX-width*1.3, posY, posX+width+width, 20, la, lr, lg, lb);
        self:drawTextRight("Unauthorized : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.clientZombiesUnauthorized, posX+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverZombiesUnauthorized, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawTextRight("Reusable : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.clientZombiesReusable, posX+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverZombiesReusable, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20

        posX = startX
        posY = posY + 5

        self:drawTextRight("Memory", posX, posY, hr, hg, hb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawRect(startX-width*1.3, posY, startX+width+width, 1, la*6, lr, lg, lb);
        posY = posY + 5
        self:drawTextRight("Free (MB) : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.clientMemFree, posX+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverMemFree, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawTextRight("Used (MB) : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawRect(posX-width*1.3, posY, posX+width+width, 20, la, lr, lg, lb);
        self:drawTextRight(statisticsData.clientMemUsed, posX+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverMemUsed, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawTextRight("Total (MB) : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.clientMemTotal, posX+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverMemTotal, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20

        posX = startX
        posY = posY + 5

        self:drawTextRight("FPS", posX, posY, hr, hg, hb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawRect(startX-width*1.3, posY, startX+width+width, 1, la*6, lr, lg, lb);
        posY = posY + 5
        self:drawTextRight("Main : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.clientFPS, posX+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverFPS, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawRect(posX-width*1.3, posY, posX+width+width, 20, la, lr, lg, lb);
        self:drawTextRight("Networking : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverNetworkingFPS, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20

        posX = startX
        posY = posY + 5

        self:drawTextRight("Network", posX, posY, hr, hg, hb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawRect(startX-width*1.3, posY, startX+width+width, 1, la*6, lr, lg, lb);
        posY = posY + 5
        self:drawTextRight("RX (KB) : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.clientRX, posX+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverRX, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawRect(posX-width*1.3, posY, posX+width+width, 20, la, lr, lg, lb);
        self:drawTextRight("TX (KB) : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.clientTX, posX+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverTX, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawTextRight("Loss (%) : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.clientLoss, posX+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverLoss, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20

        posX = startX
        posY = posY + 5

        self:drawTextRight("Ping", posX, posY, hr, hg, hb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawRect(startX-width*1.3, posY, startX+width+width, 1, la*6, lr, lg, lb);
        posY = posY + 5
        self:drawTextRight("Last : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(ping.lastPing, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawTextRight("Average : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(ping.avgPing, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawTextRight("Minimum : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(ping.minPing, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20

        self:drawTextRight("Packet Ping", posX, posY, hr, hg, hb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawRect(startX-width*1.3, posY, startX+width+width, 1, la*6, lr, lg, lb);
        posY = posY + 5
        self:drawTextRight("Last : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverPingLast, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawRect(posX-width*1.3, posY, posX+width+width, 20, la, lr, lg, lb);
        self:drawTextRight("Average : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverPingAvg, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawTextRight("Loss : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverPingLoss, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawRect(posX-width*1.3, posY, posX+width+width, 20, la, lr, lg, lb);
        self:drawTextRight("Maximum : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverPingMax, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawTextRight("Minimum : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverPingMin, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20

        posX = startX
        posY = posY + 5

        self:drawTextRight("Time", posX, posY, hr, hg, hb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawRect(startX-width*1.3, posY, startX+width+width, 1, la*6, lr, lg, lb);
        posY = posY + 5
        self:drawTextRight("Client : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.clientTime, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawRect(posX-width*1.3, posY, posX+width+width, 20, la, lr, lg, lb);
        self:drawTextRight("Server : ", posX, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight(statisticsData.serverTime, posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20

        posX = startX
        posY = posY + 5

        self:drawTextRight("Revision", posX, posY, hr, hg, hb, a, UIFont.NewMedium);
        posY = posY + 20
        self:drawRect(startX-width*1.3, posY, startX+width+width, 1, la*6, lr, lg, lb);
        posY = posY + 5
        self:drawTextRight("\""..statisticsData.clientRevision.."\"", posX+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        self:drawTextRight("\""..statisticsData.serverRevision.."\"", posX+width+width, posY, tr, tg, tb, a, UIFont.NewMedium);
        posY = posY + 20

    end
end

function ISStatisticsUI:new(x, y, character)
    local o = {}
    local width = 310
    local height = 800
    o = ISCollapsableWindow:new(x, y, width, height)
    setmetatable(o, self)
    self.__index = self
    o.playerNum = character:getPlayerNum()
    if y == 0 then
        o.y = getPlayerScreenTop(o.playerNum) + (getPlayerScreenHeight(o.playerNum) - height) / 2
        o:setY(o.y)
    end
    if x == 0 then
        o.x = getPlayerScreenLeft(o.playerNum) + (getPlayerScreenWidth(o.playerNum) - width) / 2
        o:setX(o.x)
    end
    o.width = width
    o.height = height
    o.chr = character
    o.moveWithMouse = true
    o.anchorLeft = true
    o.anchorRight = true
    o.anchorTop = true
    o.anchorBottom = true
    o.resizable = false
    ISStatisticsUI.instance = o
    return o;
end
