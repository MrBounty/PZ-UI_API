--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: Yuri				           **
--***********************************************************


require "DebugUIs/DebugMenu/Statistic/StatisticChart"

---@class StatisticChartDiskOperations : StatisticChart
StatisticChartDiskOperations = StatisticChart:derive("StatisticChartDiskOperations");
StatisticChartDiskOperations.instance = nil;
StatisticChartDiskOperations.shiftDown = 0;
StatisticChartDiskOperations.eventsAdded = false;

function StatisticChartDiskOperations.doInstance()
    if StatisticChartDiskOperations.instance==nil then
        StatisticChartDiskOperations.instance = StatisticChartDiskOperations:new (100, 100, 900, 400, getPlayer());
        StatisticChartDiskOperations.instance:initialise();
        StatisticChartDiskOperations.instance:instantiate();
    end
    StatisticChartDiskOperations.instance:addToUIManager();
    StatisticChartDiskOperations.instance:setVisible(false);
    return StatisticChartDiskOperations.instance;
end

function StatisticChartDiskOperations.OnOpenPanel()
    if StatisticChartDiskOperations.instance==nil then
        StatisticChartDiskOperations.instance = StatisticChartDiskOperations:new (100, 100, 900, 400, getPlayer());
        StatisticChartDiskOperations.instance:initialise();
        StatisticChartDiskOperations.instance:instantiate();
    end
    StatisticChartDiskOperations.instance:addToUIManager();
    StatisticChartDiskOperations.instance:setVisible(true);
	StatisticChartDiskOperations.instance.title = "Statistic Chart Disk Operations"
    return StatisticChartDiskOperations.instance;
end

StatisticChartDiskOperations.OnServerStatisticReceived = function()
	if StatisticChartDiskOperations.instance~=nil then
        StatisticChartDiskOperations.instance:updateValues()
    end
end

Events.OnServerStatisticReceived.Add(StatisticChartDiskOperations.OnServerStatisticReceived);

function StatisticChartDiskOperations:createChildren()
    StatisticChart.createChildren(self);
	
	local labelWidth = 250
	x = self.historyM1:getX()
	y = self.historyM1:getY()+self.historyM1:getHeight()
	y = y+8;
    y = self:addLabelValue(x+5, y, labelWidth, "value","load","load:",0);
	y = self:addLabelValue(x+5, y, labelWidth, "value","save","save:",0);
end

function StatisticChartDiskOperations:initVariables()
	StatisticChart.initVariables(self);
	
	self:addVarInfo("load","load",-1,100,"loadCellFromDisk");
	self:addVarInfo("save","save",-1,100,"saveCellToDisk");
end

function StatisticChartDiskOperations:updateValues()
	StatisticChart.updateValues(self);
	local minmax1 = self.historyM1:calcMinMax(1);
	local minmax2 = self.historyM1:calcMinMax(2);
	self.historyM1:applyMinMax(minmax1, 1);
	self.historyM1:applyMinMax(minmax2, 2);
end
