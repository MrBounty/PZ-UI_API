require "ISBaseObject"

ISBaseTimedAction = ISBaseObject:derive("ISBaseTimedAction");

ISBaseTimedAction.IDMax = 1;



function ISBaseTimedAction:isValid()

end

function ISBaseTimedAction:update()

end

function ISBaseTimedAction:forceComplete()
    self.action:forceComplete();
end

function ISBaseTimedAction:forceStop()
    self.action:forceStop();
end

function ISBaseTimedAction:getJobDelta()
	return self.action:getJobDelta();
end

function ISBaseTimedAction:resetJobDelta()
	return self.action:resetJobDelta();
end

function ISBaseTimedAction:waitToStart()
	return false
end

function ISBaseTimedAction:start()

end

function ISBaseTimedAction:stop()
    ISTimedActionQueue.getTimedActionQueue(self.character):resetQueue();
end

function ISBaseTimedAction:perform()

	ISTimedActionQueue.getTimedActionQueue(self.character):onCompleted(self);

end

function ISBaseTimedAction:create()
	self.maxTime = self:adjustMaxTime(self.maxTime);
	self.action = LuaTimedActionNew.new(self, self.character);
end

function ISBaseTimedAction:begin()
	self:create();
--	print("action created.");
	self.character:StartAction(self.action);
--	print("action called.");
end

function ISBaseTimedAction:setCurrentTime(time)
	self.action:setCurrentTime(time);
end

function ISBaseTimedAction:setTime(time)
	self.maxTime = time;
end

function ISBaseTimedAction:adjustMaxTime(maxTime)
	if maxTime ~= -1 then
		-- add a slight maxtime if the character is unhappy
		maxTime = maxTime + ((self.character:getMoodles():getMoodleLevel(MoodleType.Unhappy)) * 10)

		-- add more time if the character have his hands wounded
		if not self.ignoreHandsWounds then
			for i=BodyPartType.ToIndex(BodyPartType.Hand_L), BodyPartType.ToIndex(BodyPartType.ForeArm_R) do
				local part = self.character:getBodyDamage():getBodyPart(BodyPartType.FromIndex(i));
				maxTime = maxTime + part:getPain();
			end
		end

		-- Apply a multiplier based on body temperature.
		maxTime = maxTime * self.character:getTimedActionTimeModifier();
	end
	return maxTime;
end

function ISBaseTimedAction:setActionAnim(_action, _displayItemModels)
    if _displayItemModels~=nil then
        self.action:setActionAnim(_action, _displayItemModels);
    else
        self.action:setActionAnim(_action);
    end
end

function ISBaseTimedAction:setOverrideHandModels(_primaryHand, _secondaryHand, _resetModel)
	self.action:setOverrideHandModelsObject(_primaryHand, _secondaryHand, _resetModel or true)
end

function ISBaseTimedAction:setAnimVariable(_key, _val)
    self.action:setAnimVariable(_key, _val);
end

function ISBaseTimedAction:addAfter(action)
	local queue,action1 = ISTimedActionQueue.addAfter(self, action)
	return action1
end

function ISBaseTimedAction:new (character)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.stopOnWalk = false;
	o.stopOnRun = false;
	o.stopOnAim = true;
    o.caloriesModifier = 1;
	o.maxTime = -1;
	return o
end
