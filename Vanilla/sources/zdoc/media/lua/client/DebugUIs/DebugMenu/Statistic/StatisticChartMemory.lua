--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: Yuri				           **
--***********************************************************


require "DebugUIs/DebugMenu/Statistic/StatisticChart"

---@class StatisticChartMemory : StatisticChart
StatisticChartMemory = StatisticChart:derive("StatisticChartMemory");
StatisticChartMemory.instance = nil;
StatisticChartMemory.shiftDown = 0;
StatisticChartMemory.eventsAdded = false;


function StatisticChartMemory.doInstance()
    if StatisticChartMemory.instance==nil then
        StatisticChartMemory.instance = StatisticChartMemory:new (100, 100, 900, 400, getPlayer());
        StatisticChartMemory.instance:initialise();
        StatisticChartMemory.instance:instantiate();
    end
    StatisticChartMemory.instance:addToUIManager();
    StatisticChartMemory.instance:setVisible(false);
    return StatisticChartMemory.instance;
end

function StatisticChartMemory.OnOpenPanel()
    if StatisticChartMemory.instance==nil then
        StatisticChartMemory.instance = StatisticChartMemory:new (100, 100, 900, 400, getPlayer());
        StatisticChartMemory.instance:initialise();
        StatisticChartMemory.instance:instantiate();
    end

    StatisticChartMemory.instance:addToUIManager();
    StatisticChartMemory.instance:setVisible(true);
	StatisticChartMemory.instance.title = "Statistic Chart Memory"
    return StatisticChartMemory.instance;
end

StatisticChartMemory.OnServerStatisticReceived = function()
	if StatisticChartMemory.instance~=nil then
        StatisticChartMemory.instance:updateValues()
    end
end

Events.OnServerStatisticReceived.Add(StatisticChartMemory.OnServerStatisticReceived);

function StatisticChartMemory:createChildren()
    StatisticChart.createChildren(self);
	
	local labelWidth = 250
	x = self.historyM1:getX()
	y = self.historyM1:getY()+self.historyM1:getHeight()
	y = y+8;
    y = self:addLabelValue(x+5, y, labelWidth, "value","used","used:",0);
end

function StatisticChartMemory:initVariables()
	StatisticChart.initVariables(self);
	self:addVarInfo("used","used",-1,10000000000,"usedMemory");
end

function StatisticChartMemory:updateValues()
	StatisticChart.updateValues(self);
	local minmax1 = self.historyM1:calcMinMax(1);
	self.historyM1:applyMinMax(minmax1, 1);
end
