--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISWashYourself : ISBaseTimedAction
ISWashYourself = ISBaseTimedAction:derive("ISWashYourself");

function ISWashYourself:isValid()
	return true;
end

function ISWashYourself:update()
	self.character:faceThisObjectAlt(self.sink)
    self.character:setMetabolicTarget(Metabolics.LightDomestic);
end

function ISWashYourself:start()
	self:setActionAnim("WashFace")
	self:setOverrideHandModels(nil, nil)
	self.sound = self.character:playSound("WashYourself")
	self.character:reportEvent("EventWashClothing");
end

function ISWashYourself:stopSound()
	if self.sound and self.character:getEmitter():isPlaying(self.sound) then
		self.character:stopOrTriggerSound(self.sound)
	end
end

function ISWashYourself:stop()
	self:stopSound()
    ISBaseTimedAction.stop(self);
end

function ISWashYourself:perform()
	self:stopSound()
	local visual = self.character:getHumanVisual()
	local waterUsed = 0
	for i=1,BloodBodyPartType.MAX:index() do
		local part = BloodBodyPartType.FromIndex(i-1)
		if self:washPart(visual, part) then
			waterUsed = waterUsed + 1
			if waterUsed >= self.sink:getWaterAmount() then
				break
			end
		end
	end
	self.character:resetModelNextFrame();
	sendVisual(self.character);
	triggerEvent("OnClothingUpdated", self.character)

	-- remove makeup
	self:removeAllMakeup()

	ISTakeWaterAction.SendTakeWaterCommand(self.character, self.sink, waterUsed)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISWashYourself:washPart(visual, part)
	if visual:getBlood(part) + visual:getDirt(part) <= 0 then
		return false
	end
	if visual:getBlood(part) > 0 then
		-- Soap is used for blood but not for dirt.
		for _,soap in ipairs(self.soaps) do
			if soap:getRemainingUses() > 0 then
				soap:Use()
				break
			end
		end
	end
	visual:setBlood(part, 0)
	visual:setDirt(part, 0)
	return true
end

function ISWashYourself:removeAllMakeup()
	local item = self.character:getWornItem("MakeUp_FullFace");
	self:removeMakeup(item);
	item = self.character:getWornItem("MakeUp_Eyes");
	self:removeMakeup(item);
	item = self.character:getWornItem("MakeUp_EyesShadow");
	self:removeMakeup(item);
	item = self.character:getWornItem("MakeUp_Lips");
	self:removeMakeup(item);
end

function ISWashYourself:removeMakeup(item)
	if item then
		self.character:removeWornItem(item);
		self.character:getInventory():Remove(item);
	end
end

function ISWashYourself.GetRequiredSoap(character)
	local units = 0
	local visual = character:getHumanVisual()
	for i=1,BloodBodyPartType.MAX:index() do
		local part = BloodBodyPartType.FromIndex(i-1)
		-- Soap is used for blood but not for dirt.
		if visual:getBlood(part) > 0 then
			units = units + 1
		end
	end
	return units
end

function ISWashYourself.GetRequiredWater(character)
	local units = 0
	local visual = character:getHumanVisual()
	for i=1,BloodBodyPartType.MAX:index() do
		local part = BloodBodyPartType.FromIndex(i-1)
		if visual:getBlood(part) + visual:getDirt(part) > 0 then
			units = units + 1
		end
	end
	return units
end

function ISWashYourself:new(character, sink, soapList)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.sink = sink;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	local waterUnits = math.min(ISWashYourself.GetRequiredWater(character), sink:getWaterAmount());
	o.maxTime = waterUnits * 70;
	o.soaps = soapList;
	o.forceProgressBar = true;
	if ISWashYourself.GetRequiredSoap(character) > ISWashClothing.GetSoapRemaining(soapList) then
		o.maxTime = o.maxTime * 1.8;
	end
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	return o;
end
