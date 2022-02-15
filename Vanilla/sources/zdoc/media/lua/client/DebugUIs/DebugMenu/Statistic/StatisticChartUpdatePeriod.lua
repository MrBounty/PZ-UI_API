--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: Yuri				           **
--***********************************************************


require "DebugUIs/DebugMenu/Statistic/StatisticChart"

---@class StatisticChartUpdatePeriod : StatisticChart
StatisticChartUpdatePeriod = StatisticChart:derive("StatisticChartUpdatePeriod");
StatisticChartUpdatePeriod.instance = nil;
StatisticChartUpdatePeriod.shiftDown = 0;
StatisticChartUpdatePeriod.eventsAdded = false;

function StatisticChartUpdatePeriod.doInstance()
    if StatisticChartUpdatePeriod.instance==nil then
        StatisticChartUpdatePeriod.instance = StatisticChartUpdatePeriod:new (100, 100, 900, 400, getPlayer());
        StatisticChartUpdatePeriod.instance:initialise();
        StatisticChartUpdatePeriod.instance:instantiate();
    end
    StatisticChartUpdatePeriod.instance:addToUIManager();
    StatisticChartUpdatePeriod.instance:setVisible(false);
    return StatisticChartUpdatePeriod.instance;
end

function StatisticChartUpdatePeriod.OnOpenPanel()
    if StatisticChartUpdatePeriod.instance==nil then
        StatisticChartUpdatePeriod.instance = StatisticChartUpdatePeriod:new (100, 100, 900, 400, getPlayer());
        StatisticChartUpdatePeriod.instance:initialise();
        StatisticChartUpdatePeriod.instance:instantiate();
    end

    StatisticChartUpdatePeriod.instance:addToUIManager();
    StatisticChartUpdatePeriod.instance:setVisible(true);
	StatisticChartUpdatePeriod.instance.title = "Statistic Chart Update Period"
    return StatisticChartUpdatePeriod.instance;
end

StatisticChartUpdatePeriod.OnServerStatisticReceived = function()
	if StatisticChartUpdatePeriod.instance~=nil then
        StatisticChartUpdatePeriod.instance:updateValues()
    end
end

Events.OnServerStatisticReceived.Add(StatisticChartUpdatePeriod.OnServerStatisticReceived);

function StatisticChartUpdatePeriod:createChildren()
    StatisticChart.createChildren(self);
	
	local labelWidth = 250
	x = self.historyM1:getX()
	y = self.historyM1:getY()+self.historyM1:getHeight()
	y = y+8;
    y = self:addLabelValue(x+5, y, labelWidth, "value","Min","Min:",0);
	y = self:addLabelValue(x+5, y, labelWidth, "value","Max","Max:",0);
	y = self:addLabelValue(x+5, y, labelWidth, "value","AVG","AVG:",0);
end

function StatisticChartUpdatePeriod:updateValues()
	StatisticChart.updateValues(self);
	local minmax1 = self.historyM1:calcMinMax(1);
	local minmax1 = self.historyM1:calcMinMax(2, minmax1);
	local minmax1 = self.historyM1:calcMinMax(3, minmax1);
	self.historyM1:applyMinMax(minmax1, 1);
	self.historyM1:applyMinMax(minmax1, 2);
	self.historyM1:applyMinMax(minmax1, 3);
end

function StatisticChartUpdatePeriod:initVariables()
	StatisticChart.initVariables(self);
	
	self:addVarInfo("Min","Min",-1,1000,"minUpdatePeriod");
	self:addVarInfo("Max","Max",-1,1000,"maxUpdatePeriod");
	self:addVarInfo("AVG","AVG",-1,1000,"avgUpdatePeriod");
end
