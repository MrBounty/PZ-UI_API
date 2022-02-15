--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISWearClothing : ISBaseTimedAction
ISWearClothing = ISBaseTimedAction:derive("ISWearClothing");

function ISWearClothing:isValid()
	return self.character:getInventory():contains(self.item);
end

function ISWearClothing:update()
	self.item:setJobDelta(self:getJobDelta());
end

-- BodyLocation -> String used by AnimSets
WearClothingAnimations = {}
WearClothingAnimations.Belt = "Waist"
WearClothingAnimations.BeltExtra = "Waist"
WearClothingAnimations.Dress = "Legs"
WearClothingAnimations.Ears = "Face"
WearClothingAnimations.EarTop = "Face"
WearClothingAnimations.Eyes = "Face"
WearClothingAnimations.FannyPackBack = "Waist"
WearClothingAnimations.FannyPackFront = "Waist"
WearClothingAnimations.FullHat = "Face"
WearClothingAnimations.Hat = "Face"
WearClothingAnimations.Jacket = "Jacket"
WearClothingAnimations.JacketHat = "Jacket"
WearClothingAnimations.Legs1 = "Legs"
WearClothingAnimations.Mask = "Face"
WearClothingAnimations.MaskEyes = "Face"
WearClothingAnimations.Nose = "Face"
WearClothingAnimations.Pants = "Legs"
WearClothingAnimations.Shoes = "Feet"
WearClothingAnimations.Skirt = "Legs"
WearClothingAnimations.Socks = "Feet"
WearClothingAnimations.Shirt = "Jacket"
WearClothingAnimations.ShortSleeveShirt = "Jacket"
WearClothingAnimations.TankTop = "Pullover"
WearClothingAnimations.Tshirt = "Pullover"

function ISWearClothing:start()
	self.item:setJobType(getText("ContextMenu_Wear") .. ' ' .. self.item:getName());
	self.item:setJobDelta(0.0);
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

function ISWearClothing:stop()
    ISBaseTimedAction.stop(self);
    self.item:setJobDelta(0.0);
end

function ISWearClothing:perform()
    self.item:getContainer():setDrawDirty(true);
    self.item:setJobDelta(0.0);
	if instanceof(self.item, "InventoryContainer") and self.item:canBeEquipped() ~= "" then
		self.character:removeFromHands(self.item);
		self.character:setWornItem(self.item:canBeEquipped(), self.item);
		getPlayerInventory(self.character:getPlayerNum()):refreshBackpacks();
	elseif self.item:getCategory() == "Clothing" then
		if self.item:getBodyLocation() ~= "" then
			self.character:setWornItem(self.item:getBodyLocation(), self.item);

			-- here we handle flating the mohawk!
			if self.character:getHumanVisual():getHairModel():contains("Mohawk") and (self.item:getBodyLocation() == "Hat" or self.item:getBodyLocation() == "FullHat") then
				self.character:getHumanVisual():setHairModel("MohawkFlat");
				self.character:resetModel();
				self.character:resetHairGrowingTime();
			end
		end
	end
	triggerEvent("OnClothingUpdated", self.character)
--~ 	self.character:SetClothing(self.item:getBodyLocation(), self.item:getSpriteName(), self.item:getPalette());
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISWearClothing:new(character, item, time)
	local o = ISBaseTimedAction.new(self, character);
	o.item = item;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	o.fromHotbar = true; -- just to disable hotbar:update() during the wearing
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	return o;
end
