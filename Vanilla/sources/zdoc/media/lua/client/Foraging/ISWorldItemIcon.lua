--[[---------------------------------------------
-------------------------------------------------
--
-- ISWorldItemIcon
--
-- eris
--
-------------------------------------------------
--]]---------------------------------------------
require "Foraging/forageSystem";
require "ISUI/ISPanel";
require "Foraging/ISBaseIcon";
---@class ISWorldItemIcon : ISBaseIcon
ISWorldItemIcon = ISBaseIcon:derive("ISWorldItemIcon");
-------------------------------------------------
-------------------------------------------------
function ISWorldItemIcon:onRightMouseUp()
	if self.iconClass and self.iconClass == "stashObject" then return false; end;
	return self:doContextMenu();
end;

function ISWorldItemIcon:onRightMouseDown()
	if self.iconClass and self.iconClass == "stashObject" then return false; end;
	return (self:getIsSeen() and self:getAlpha() > 0);
end;
-------------------------------------------------
-------------------------------------------------
function ISWorldItemIcon:doPickup(_x, _y, _contextOption, _targetContainer, _items)
	if _contextOption then _contextOption:hideAndChildren(); end;
	self:getGridSquare();
	if not self.square then return; end;
	---
	local targetContainer = _targetContainer or self.character:getInventory();
	if self.square and luautils.walkAdj(self.character, self.square) then
		local items = _items or {self.itemObj};
		local time = ISWorldObjectContextMenu.grabItemTime(self.character, self.itemObj:getWorldItem());
		for _, itemObj in ipairs(items) do
			if self:isValid() and itemObj and itemObj:getWorldItem() then
				local grabAction = ISGrabItemAction:new(self.character, itemObj:getWorldItem(), time);
				grabAction.destContainer = targetContainer;
				ISTimedActionQueue.add(grabAction);
			end;
		end;
	end;

end
-------------------------------------------------
-------------------------------------------------
function ISWorldItemIcon:isValidWorldItem()
	return (self.itemObj and self.itemObj:getWorldItem()) and true or false;
end

function ISWorldItemIcon:isValid()
	if self:isInRangeOfPlayer(40) then
		if self.iconClass and self.iconClass == "stashObject" then return true; end;
		if self.iconClass and self.iconClass == "worldObject" then
			if self.itemObj and self.itemObj:getWorldItem() then
				return true;
			else
				for _, itemObj in pairs(self.itemObjTable) do
					if itemObj and itemObj:getWorldItem() then
						self.itemObj = itemObj;
					else
						self.itemObjTable[itemObj] = nil;
					end;
				end;
				return self:isValidWorldItem();
			end;
		end;
	end;
	return false;
end
-------------------------------------------------
-------------------------------------------------
function ISWorldItemIcon:updateIsForageable()
	self.isForageable = self:isValid();
	return self.isForageable;
end
-------------------------------------------------
-------------------------------------------------
function ISWorldItemIcon:new(_manager, _icon)
	local o = {};
	o = ISBaseIcon:new(_manager, _icon);
	setmetatable(o, self)
	self.__index = self;
	o.onClickContext = ISWorldItemIcon.doPickup;
	o.onMouseDoubleClick = ISWorldItemIcon.doPickup;
	o.iconClass = nil;
	o.isValidSquare = true;
	o.itemObjTable = _icon.itemObjTable;
	o.container = _icon.itemObj:getContainer();
	o.onMouseDoubleClick = ISWorldItemIcon.doPickup;
	o:initialise();
	return o;
end
