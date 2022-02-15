--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISWashClothing : ISBaseTimedAction
ISWashClothing = ISBaseTimedAction:derive("ISWashClothing");

function ISWashClothing:isValid()
	if self.sink:getWaterAmount() < ISWashClothing.GetRequiredWater(self.item) then
		return false
	end
	return true
end

function ISWashClothing:update()
	self.item:setJobDelta(self:getJobDelta())
	self.character:faceThisObjectAlt(self.sink)
    self.character:setMetabolicTarget(Metabolics.HeavyDomestic);
end

function ISWashClothing:start()
	self:setActionAnim("Loot")
	self:setAnimVariable("LootPosition", "");
	self:setOverrideHandModels(nil, nil)
	self.sound = self.character:playSound("WashClothing")
	self.character:reportEvent("EventWashClothing");
end

function ISWashClothing:stopSound()
	if self.sound and self.character:getEmitter():isPlaying(self.sound) then
		self.character:stopOrTriggerSound(self.sound)
	end
end

function ISWashClothing:stop()
	self:stopSound()
	self.item:setJobDelta(0.0)
    ISBaseTimedAction.stop(self);
end

function ISWashClothing.GetSoapRemaining(soaps)
	local total = 0
	for _,soap in ipairs(soaps) do
		total = total + soap:getRemainingUses()
	end
	return total
end

function ISWashClothing.GetRequiredSoap(item)
	local total = 0
	if instanceof(item, "Clothing") then
		local coveredParts = BloodClothingType.getCoveredParts(item:getBloodClothingType())
		if coveredParts then
			for i=1,coveredParts:size() do
				local part = coveredParts:get(i-1)
				if item:getBlood(part) > 0 then
					total = total + 1
				end
			end
		end
	else
		if item:getBloodLevel() > 0 then
			total = total + 1
		end
	end
	return total
end

function ISWashClothing.GetRequiredWater(item)
	return 10
end

function ISWashClothing:useSoap(item, part)
	local blood = 0;
	if part then
		blood = item:getBlood(part);
	else
		blood = item:getBloodLevel();
	end
	if blood > 0 then
		for i,soap in ipairs(self.soaps) do
			if soap:getRemainingUses() > 0 then
				soap:Use();
				return true;
			end
		end
	else
		return true;
	end
	return false;
end

function ISWashClothing:perform()
	self:stopSound()
	self.item:setJobDelta(0.0)
	local item = self.item;
	local water = ISWashClothing.GetRequiredWater(item)
	if instanceof(item, "Clothing") then
		local coveredParts = BloodClothingType.getCoveredParts(item:getBloodClothingType())
		if coveredParts then
			for j=0,coveredParts:size()-1 do
				if self.noSoap == false then
					self:useSoap(item, coveredParts:get(j));
				end
				item:setBlood(coveredParts:get(j), 0);
				item:setDirt(coveredParts:get(j), 0);
			end
		end
		item:setWetness(100);
		item:setDirtyness(0);
	else
		self:useSoap(item, nil);
	end
	item:setBloodLevel(0);
	
	self.character:resetModel();
	sendClothing(self.character);
	if self.character:isPrimaryHandItem(item) then
		self.character:setPrimaryHandItem(item);
	end
	if self.character:isSecondaryHandItem(item) then
		self.character:setSecondaryHandItem(item);
	end
	triggerEvent("OnClothingUpdated", self.character)
	
	local obj = self.sink
	if instanceof (obj, "Drainable") then
	 self.obj:setUsedDelta(self.startUsedDelta + (self.endUsedDelta - self.startUsedDelta) * self:getJobDelta());
	end
	ISTakeWaterAction.SendTakeWaterCommand(self.character, self.sink, water)
	
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISWashClothing:new(character, sink, soapList, item, bloodAmount, dirtAmount, noSoap)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.sink = sink;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.item = item;
	o.maxTime = ((bloodAmount + dirtAmount) * 15);
	if o.maxTime > 500 then
		o.maxTime = 500;
	end
	if noSoap == true then
		o.maxTime = o.maxTime * 5;
	end
	if o.maxTime > 800 then
		o.maxTime = 800;
	end
	if o.maxTime < 100 then
		o.maxTime = 100;
	end
	o.soaps = soapList;
	o.noSoap = noSoap
	o.forceProgressBar = true;
	if character:isTimedActionInstant() then
		o.maxTime = 1;
	end
	return o;
end
