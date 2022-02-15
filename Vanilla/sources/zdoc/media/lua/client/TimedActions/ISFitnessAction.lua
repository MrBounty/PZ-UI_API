--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISFitnessAction : ISBaseTimedAction
ISFitnessAction = ISBaseTimedAction:derive("ISFitnessAction");

function ISFitnessAction:isValid()
	return true;
end

function ISFitnessAction:update()
	if self.character:isClimbing() then
		self:forceStop();
	end
	if self.character:pressedMovement(true) or self.character:getMoodles():getMoodleLevel(MoodleType.Endurance) > ISFitnessUI.enduranceLevelTreshold then
		self.character:setVariable("ExerciseStarted", false);
		self.character:setVariable("ExerciseEnded", true);
	end
	
	if getGameTime():getCalender():getTimeInMillis() > self.endMS then
		self.character:setVariable("ExerciseStarted", false);
		self.character:setVariable("ExerciseEnded", true);
	end
	
	self.character:setMetabolicTarget(self.exeData.metabolics);
end


function ISFitnessAction:start()
	self.action:setUseProgressBar(false)
	if self.character:getCurrentState() ~= FitnessState.instance() then
		self.character:setVariable("ExerciseType", self.exercise);
		self.character:reportEvent("EventFitness");
		self.character:clearVariable("ExerciseStarted");
		self.character:clearVariable("ExerciseEnded");
		
		self.character:reportEvent("EventUpdateFitness");
	end
	
--	self:showHandModel();
end

function ISFitnessAction:showHandModel()
	if self.exeData.item then
		if self.character:getPrimaryHandItem() and self.character:getPrimaryHandItem():getType() == self.exeData.item then
			self:setOverrideHandModels(self.character:getPrimaryHandItem():getStaticModel(), nil);
		elseif self.character:getSecondaryHandItem() and self.character:getSecondaryHandItem():getType() == self.exeData.item then
			self:setOverrideHandModels(nil, self.character:getSecondaryHandItem():getStaticModel());
		end
	else
		self:setOverrideHandModels(nil, nil);
	end
end

function ISFitnessAction:stop()
	self.character:PlayAnim("Idle");
--	self.character:SetVariable("FitnessFinished","true");
	self.character:setVariable("ExerciseEnded", true);
	setGameSpeed(1);
	ISBaseTimedAction.stop(self);
end

function ISFitnessAction:perform()
	self.character:PlayAnim("Idle");
	
--	if self.fitnessUI then
--		self.fitnessUI:updateExercises();
--	end
	
	self.character:setVariable("ExerciseEnded", true);
--	print("REP NBR!:", self.repnb)
	setGameSpeed(1);
	ISBaseTimedAction.perform(self);
end

-- handle endurance loss, regularity, stats boosts, etc.
function ISFitnessAction:exeLooped()
	self.repnb = self.repnb + 1;
	self.fitness:exerciseRepeat();
	self:setFitnessSpeed();
end

function ISFitnessAction:animEvent(event, parameter)
	if parameter == "FitnessFinished=TRUE" then
		self:forceStop();
	end
	if event == "ActiveAnimLooped" then
		self:exeLooped();
		if self.exeData.prop == "switch" then -- switch hand used every X times
			self.switchTime = self.switchTime -1;
			if self.switchTime == 1 then
				self.switchTime = 5;
				if self.switchHandUsed == "right" then
					self.switchHandUsed = "left";
					self.character:setVariable("ExerciseHand", "left");
					self.character:setSecondaryHandItem(self.character:getPrimaryHandItem());
					self.character:setPrimaryHandItem(nil);
				else
					self.switchHandUsed = "right";
					self.character:clearVariable("ExerciseHand");
					self.character:setPrimaryHandItem(self.character:getSecondaryHandItem());
					self.character:setSecondaryHandItem(nil);
				end
			end
		end
		self.character:reportEvent("EventUpdateFitness");
--		print("loopityloop", self.exeData.prop)
	end
end

function ISFitnessAction:setFitnessSpeed()
	self.character:setFitnessSpeed()
end

function ISFitnessAction:new(character, exercise, timeToExe, fitnessUI, exeData)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.fitnessUI = fitnessUI;
	o.exercise = exercise;
	o.timeToExe = timeToExe;
	o.exeData = exeData;
	o.switchTime = 5;
	o.switchHandUsed = "right";
	-- calcul time we need in ingame minutes
	o.startMS = getGameTime():getCalender():getTimeInMillis();
	o.endMS = o.startMS + (timeToExe * 60000)
	o.maxTime = 5000000;
	o.fitness = character:getFitness();
	o.repnb = 0;

	o:setFitnessSpeed();
	o.fitness:setCurrentExercise(exeData.type);
	
	o.caloriesModifier = 3;
	
	return o;
end
