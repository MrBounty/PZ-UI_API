--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISClothingExtraAction : ISBaseTimedAction
ISClothingExtraAction = ISBaseTimedAction:derive("ISClothingExtraAction")

function ISClothingExtraAction:isValid()
	return self.character:getInventory():contains(self.item)
--	return self.character:isEquippedClothing(self.item)
end

function ISClothingExtraAction:waitToStart()
	return false
end

function ISClothingExtraAction:update()
end

function ISClothingExtraAction:start()
	if not self.character:isEquippedClothing(self.item) then
		-- Same time as ISWearClothing
		self.maxTime = 50
		self.action:setTime(self.maxTime)
	end
	self:setActionAnim("WearClothing");
	if self.item:IsClothing() then
		local location = self.item:getBodyLocation()
		self:setAnimVariable("WearClothingLocation", WearClothingAnimations[location] or "")
	elseif self.item:IsInventoryContainer() and self.item:canBeEquipped() ~= "" then
		local location = self.item:canBeEquipped()
		self:setAnimVariable("WearClothingLocation", WearClothingAnimations[location] or "")
	end
	self.character:reportEvent("EventWearClothing");
end

function ISClothingExtraAction:stop()
	ISBaseTimedAction.stop(self)
end

function ISClothingExtraAction:perform()
	local playerObj = self.character
	playerObj:removeFromHands(self.item)
	playerObj:removeWornItem(self.item)
	playerObj:getInventory():Remove(self.item)
	local newItem = self:createItem(self.item, self.extra)
	playerObj:getInventory():AddItem(newItem)
	if newItem:IsInventoryContainer() and newItem:canBeEquipped() ~= "" then
		playerObj:setWornItem(newItem:canBeEquipped(), newItem)
		getPlayerInventory(self.character:getPlayerNum()):refreshBackpacks();
	elseif newItem:IsClothing() then
		playerObj:setWornItem(newItem:getBodyLocation(), newItem)

		-- here we handle flating the mohawk!
		if playerObj:getHumanVisual():getHairModel():contains("Mohawk") and (newItem:getBodyLocation() == "Hat" or newItem:getBodyLocation() == "FullHat") then
			playerObj:getHumanVisual():setHairModel("MohawkFlat");
			playerObj:resetModel();
			playerObj:resetHairGrowingTime();
		end
	end
	triggerEvent("OnClothingUpdated", playerObj)

	ISBaseTimedAction.perform(self)
end

function ISClothingExtraAction:createItem(item, itemType)
	local visual = item:getVisual()
	local newItem = InventoryItemFactory.CreateItem(itemType)
	local newVisual = newItem:getVisual()
	newVisual:setTint(visual:getTint(item:getClothingItem()))
	newVisual:setBaseTexture(visual:getBaseTexture())
	newVisual:setTextureChoice(visual:getTextureChoice())
	newVisual:setDecal(visual:getDecal(item:getClothingItem()))
	if newItem:IsInventoryContainer() and item:IsInventoryContainer() then
		newItem:getItemContainer():setItems(item:getItemContainer():getItems())
		-- Handle renamed bag
		if item:getName() ~= item:getScriptItem():getDisplayName() then
			newItem:setName(item:getName())
		end
	end
--    newItem:setDirtyness(item:getDirtyness())
--    newItem:setTexture(item:getTexture())
	newItem:setColor(item:getColor())
	newVisual:copyDirt(visual)
	newVisual:copyBlood(visual)
	newVisual:copyHoles(visual)
	newVisual:copyPatches(visual)
	if newItem:IsClothing() then
		item:copyPatchesTo(newItem)
		newItem:setWetness(item:getWetness())
	end
	if instanceof(newItem, "AlarmClockClothing") and instanceof(item, "AlarmClockClothing") then
		newItem:setAlarmSet(item:isAlarmSet())
		newItem:setHour(item:getHour())
		newItem:setMinute(item:getMinute())
		newItem:syncAlarmClock()
		-- Network stuff
		-- FIXME: is this done when dropping the watch?
		item:setAlarmSet(false)
		item:syncAlarmClock()
	end
	newItem:setCondition(item:getCondition())
	newItem:setFavorite(item:isFavorite())
	if item:hasModData() then
		newItem:copyModData(item:getModData())
	end
	newItem:synchWithVisual()
	return newItem
end

function ISClothingExtraAction:new(character, item, extra)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = 1
	o.item = item
	o.extra = extra
	if character:isTimedActionInstant() then
		o.maxTime = 1
	end
	return o
end
