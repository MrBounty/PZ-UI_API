--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISEquipHeavyItem : ISBaseTimedAction
ISEquipHeavyItem = ISBaseTimedAction:derive("ISEquipHeavyItem")

function ISEquipHeavyItem:isValid()
	if not self.item:getContainer() or not self.item:getContainer():contains(self.item) then
		return false
	end
	if self:isAlreadyTransferred(self.item) then
		return false
	end
	if self.character:isItemInBothHands(self.item) then
		return false
	end

	local part = self.item:getContainer():getVehiclePart()
	if part and not part:getVehicle():canAccessContainer(part:getIndex(), self.character) then
		return false
	end

	return true
end

function ISEquipHeavyItem:waitToStart()
	return self.character:shouldBeTurning()
end

function ISEquipHeavyItem:update()
	self.item:setJobDelta(self:getJobDelta())

	if self.item:getContainer():getParent() then
		self.character:faceThisObject(self.item:getContainer():getParent())
	end

	self.character:setMetabolicTarget(Metabolics.HeavyDomestic)
end

function ISEquipHeavyItem:start()
	self.item:setJobType(getText("ContextMenu_Equip_Two_Hands"))
	self.item:setJobDelta(0.0)
	self:setActionAnim("Loot")
	self:setAnimVariable("LootPosition", "")
	self.character:clearVariable("LootPosition")
end

function ISEquipHeavyItem:stop()
	ISBaseTimedAction.stop(self)
	self.item:setJobDelta(0.0)
end

function ISEquipHeavyItem:perform()
	self.item:setJobDelta(0.0)

	forceDropHeavyItems(self.character)

	local srcContainer = self.item:getContainer()
	if isClient() and srcContainer:getType() ~= "TradeUI" and not srcContainer:isInCharacterInventory(self.character) and srcContainer:getType() ~= "floor" then
		srcContainer:removeItemOnServer(self.item)
	end
	srcContainer:DoRemoveItem(self.item)
	self.character:getInventory():AddItem(self.item)

	self.character:setPrimaryHandItem(self.item)
	self.character:setSecondaryHandItem(self.item)

	ISInventoryPage.renderDirty = true

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISEquipHeavyItem:isAlreadyTransferred(item)
	return item:getContainer() == self.character:getInventory()
end

function ISEquipHeavyItem:new(character, item, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = time
	o.item = item
	return o
end	
