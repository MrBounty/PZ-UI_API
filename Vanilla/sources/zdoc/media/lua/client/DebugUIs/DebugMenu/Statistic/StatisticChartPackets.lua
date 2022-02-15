--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: Yuri				           **
--***********************************************************


require "DebugUIs/DebugMenu/Statistic/StatisticChart"

---@class StatisticChartPackets : StatisticChart
StatisticChartPackets = StatisticChart:derive("StatisticChartPackets");
StatisticChartPackets.instance = nil;
StatisticChartPackets.shiftDown = 0;
StatisticChartPackets.eventsAdded = false;

function StatisticChartPackets.doInstance()
    if StatisticChartPackets.instance==nil then
        StatisticChartPackets.instance = StatisticChartPackets:new (100, 100, 900, 400, getPlayer());
        StatisticChartPackets.instance:initialise();
        StatisticChartPackets.instance:instantiate();
    end
    StatisticChartPackets.instance:addToUIManager();
    StatisticChartPackets.instance:setVisible(false);
    return StatisticChartPackets.instance;
end

function StatisticChartPackets.OnOpenPanel()
    if StatisticChartPackets.instance==nil then
        StatisticChartPackets.instance = StatisticChartPackets:new (100, 100, 900, 400, getPlayer());
        StatisticChartPackets.instance:initialise();
        StatisticChartPackets.instance:instantiate();
    end
    StatisticChartPackets.instance:addToUIManager();
    StatisticChartPackets.instance:setVisible(true);
	StatisticChartPackets.instance.title = "Statistic Chart Packets"
    return StatisticChartPackets.instance;
end

StatisticChartPackets.OnServerStatisticReceived = function()
	if StatisticChartPackets.instance~=nil then
        StatisticChartPackets.instance:updateValues()
    end
end

Events.OnServerStatisticReceived.Add(StatisticChartPackets.OnServerStatisticReceived);

function StatisticChartPackets:createChildren()
    StatisticChart.createChildren(self);
	
	local labelWidth = 200
	x = self.historyM1:getX()
	y = self.historyM1:getY()+self.historyM1:getHeight()
	y = y+8;
    y = self:addLabelValue(x+5, y, labelWidth, "value","countIncomePackets","Count income packets:",0);
	y = self:addLabelValue(x+5, y, labelWidth, "value","countIncomeBytes","Count income bytes:",0);
	y = self:addLabelValue(x+5, y, labelWidth, "value","maxIncomeBytesPerSecound","Max Income bps:",0);
	y = self:addLabelValue(x+5, y, labelWidth, "value","countOutcomePackets","Count Outcome Packets:",0);
	y = self:addLabelValue(x+5, y, labelWidth, "value","countOutcomeBytes","Count Outcome Bytes:",0);
	y = self:addLabelValue(x+5, y, labelWidth, "value","maxOutcomeBytesPerSecound","Max Outcome bps:",0);
	x = x+500;
	y = self.historyM1:getY()+self.historyM1:getHeight() + 8;
	y = self:addLabelValue(x+5, y, 300, "value","maxForCountPackets","Max for chart of \"Count Packets\":",0);
	y = self:addLabelValue(x+5, y, 300, "value","maxForCountBytes","Max for chart of \"Count Bytes\":",0);
	y = self:addLabelValue(x+5, y, 300, "value","maxFormaxBPS","Max for chart of \"Max bps\":",0);
end

function StatisticChartPackets:initVariables()
	StatisticChart.initVariables(self);
	self:addVarInfo("countIncomePackets","countIncomePackets",-1,10000000000,"countIncomePackets");
	self:addVarInfo("countIncomeBytes","countIncomeBytes",-1,10000000000,"countIncomeBytes");
	self:addVarInfo("maxIncomeBytesPerSecound","maxIncomeBytesPerSecound",-1,10000000000,"maxIncomeBytesPerSecound");
	self:addVarInfo("countOutcomePackets","countOutcomePackets",-1,10000000000,"countOutcomePackets");
	self:addVarInfo("countOutcomeBytes","countOutcomeBytes",-1,10000000000,"countOutcomeBytes");
	self:addVarInfo("maxOutcomeBytesPerSecound","maxOutcomeBytesPerSecound",-1,10000000000,"maxOutcomeBytesPerSecound");
end

function StatisticChartPackets:updateValues()
	StatisticChart.updateValues(self);
	local minmax1 = self.historyM1:calcMinMax(1);
	local minmax2 = self.historyM1:calcMinMax(2);
	local minmax3 = self.historyM1:calcMinMax(3);
	local minmax1 = self.historyM1:calcMinMax(4, minmax1);
	local minmax2 = self.historyM1:calcMinMax(5, minmax2);
	local minmax3 = self.historyM1:calcMinMax(6, minmax3);
	self.historyM1:applyMinMax(minmax1, 1);
	self.historyM1:applyMinMax(minmax2, 2);
	self.historyM1:applyMinMax(minmax3, 3);
	self.historyM1:applyMinMax(minmax1, 4);
	self.historyM1:applyMinMax(minmax2, 5);
	self.historyM1:applyMinMax(minmax3, 6);
	
	self:getValueLabel("maxForCountPackets").name = tostring(minmax1.max);
	self:getValueLabel("maxForCountBytes").name = tostring(minmax2.max);
	self:getValueLabel("maxFormaxBPS").name = tostring(minmax3.max);
end
