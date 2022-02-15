--[[---------------------------------------------
-------------------------------------------------
--
-- ISForageAction
--
-- eris
--
-------------------------------------------------
--]]---------------------------------------------
require "Foraging/forageSystem";
require "TimedActions/ISBaseTimedAction";
---@class ISForageAction : ISBaseTimedAction
ISForageAction = ISBaseTimedAction:derive("ISForageAction");
-------------------------------------------------
-------------------------------------------------
function ISForageAction:isValid()
	return (self.manager and self.manager.forageIcons[self.forageIcon.iconID]) and true or false;
end
-------------------------------------------------
-------------------------------------------------
function ISForageAction:update()
	self.character:setMetabolicTarget(Metabolics.LightWork);
end
-------------------------------------------------
-------------------------------------------------
function ISForageAction:stop()
	ISBaseTimedAction.stop(self);
end
-------------------------------------------------
-------------------------------------------------
function ISForageAction:start()
	self.action:setUseProgressBar(false);
	if self.discardItems then
		self:perform();
	else
		self:setActionAnim("Forage");
		self:setOverrideHandModels(nil, nil);
	end;
end
-------------------------------------------------
-------------------------------------------------
function ISForageAction:perform()
	self.forageIcon:setIsBeingRemoved(true);
	self.manager:removeItem(self.forageIcon);
	self.manager:removeIcon(self.forageIcon);
	self:forage();
	ISBaseTimedAction.perform(self); -- remove from queue / start next.
end
-------------------------------------------------
-------------------------------------------------
function ISForageAction:forage()
	forageSystem.doFatiguePenalty(self.character);
	forageSystem.doEndurancePenalty(self.character);
	forageSystem.giveItemXP(self.character, self.itemDef);
	---
	local itemsAdded = forageSystem.addOrDropItems(self.character, self.targetContainer, self.itemDef, self.forageIcon.itemCount, self.discardItems);
	local itemsTable = {};
	for i = 0, itemsAdded:size() - 1 do
		local item = itemsAdded:get(i);
		if not itemsTable[item:getFullType()] then itemsTable[item:getFullType()] = {item = item, count = 0}; end;
		itemsTable[item:getFullType()].count = itemsTable[item:getFullType()].count + 1;
	end;
	---
	local itemTexture;
	for _, itemData in pairs(itemsTable) do
		local item = itemData.item;
		local count = itemData.count;
		if getTexture(item:getTexture():getName()) then
			itemTexture = "[img=media/textures/"..tostring(item:getTexture():getName()).."]";
		else
			itemTexture = "[img="..tostring(item:getTexture():getName()).."]";
		end;
		if not self.discardItems then
			table.insert(self.manager.haloNotes,itemTexture.."    "..count.. " "..item:getDisplayName());
		end;
	end;
end
-------------------------------------------------
-------------------------------------------------
function ISForageAction:new(_forageIcon, _targetContainer, _discardItems)
	local o = {}
	setmetatable(o, self)
	self.__index = self;
	---
	o.forageIcon = _forageIcon;
	o.zoneData = _forageIcon.zoneData;
	o.manager = _forageIcon.manager;
	o.character = _forageIcon.character;
	o.itemDef = _forageIcon.itemDef;
	o.targetContainer = _targetContainer;
	o.discardItems = _discardItems;
	---
	if _discardItems then
		o.stopOnWalk = true;
		o.stopOnRun = true;
		o.maxTime = 10;
	else
		o.stopOnWalk = false;
		o.stopOnRun = true;
		o.maxTime = 50;
	end
	o.currentTime = 0;
	o.started = started;
--    if character:isTimedActionInstant() then
--        o.maxTime = 5;
--    end
	return o;
end
-------------------------------------------------
-------------------------------------------------
