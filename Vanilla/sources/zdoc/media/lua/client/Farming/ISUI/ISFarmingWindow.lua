--***********************************************************
--**                    ROBERT JOHNSON                     **
--** Simple collapsable window wich handle our farming info panel **
--***********************************************************

require "ISUI/ISCollapsableWindow"

---@class ISFarmingWindow : ISCollapsableWindow
ISFarmingWindow = ISCollapsableWindow:derive("ISFarmingWindow");


function ISFarmingWindow:initialise()

	ISCollapsableWindow.initialise(self);
end

function ISFarmingWindow:visible(visible)
	ISFarmingWindow.instance:setVisible(visible);
end


--************************************************************************--
--** ISPanel:instantiate
--**
--************************************************************************--
function ISFarmingWindow:createChildren()
	ISCollapsableWindow.createChildren(self);
	local th = self:titleBarHeight()
	self.farmingPanel = ISFarmingInfo:new(0, th, self.width, self.height-th, self.character, self.plant);
	self.farmingPanel:initialise();
	self:addChild(self.farmingPanel);
end

function ISFarmingWindow:new (x, y, width, height, character, plant)
	local width = ISFarmingInfo.RequiredWidth()
	local o = ISCollapsableWindow.new(self, x, y, width, height);
	o:setResizable(false)
	o.title = getText("Farming_Plant_Information");
	self.farmingPanel = {};
	o.character = character
	o.plant = plant;
	return o
end
