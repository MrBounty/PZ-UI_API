--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISBuryCorpse : ISBaseTimedAction
ISBuryCorpse = ISBaseTimedAction:derive("ISBuryCorpse");

function ISBuryCorpse:isValid()
	local playerInv = self.character:getInventory()
	return playerInv:containsType("CorpseMale") or playerInv:containsType("CorpseFemale")
end

function ISBuryCorpse:waitToStart()
	self.character:faceThisObject(self.graves)
	return self.character:shouldBeTurning()
end

function ISBuryCorpse:update()
	self.character:faceThisObject(self.graves)

    self.character:setMetabolicTarget(Metabolics.DiggingSpade);
end

function ISBuryCorpse:start()
	self:setActionAnim(CharacterActionAnims.Dig)
end

function ISBuryCorpse:stop()
    ISBaseTimedAction.stop(self);
end

function ISBuryCorpse:perform()
	self.graves:getModData()["corpses"] = self.graves:getModData()["corpses"] + 1;
	self.graves:transmitModData()

	self.character:setPrimaryHandItem(nil)
	self.character:setSecondaryHandItem(nil)
	
	local playerInv = self.character:getInventory()
	if playerInv:containsType("CorpseMale") then
		playerInv:RemoveOneOf("CorpseMale", false)
	elseif playerInv:containsType("CorpseFemale") then
		playerInv:RemoveOneOf("CorpseFemale", false)
	end
	
	local sq1 = self.graves:getSquare();
	local sq2 = nil;
	local sq3 = nil;
	if self.graves:getNorth() then
		if self.graves:getModData()["spriteType"] == "sprite1" then
			sq2 = getCell():getGridSquare(sq1:getX(), sq1:getY() - 1, sq1:getZ());
		elseif self.graves:getModData()["spriteType"] == "sprite2" then
			sq2 = getCell():getGridSquare(sq1:getX(), sq1:getY() + 1, sq1:getZ());
		end
	else
		if self.graves:getModData()["spriteType"] == "sprite1" then
			sq2 = getCell():getGridSquare(sq1:getX() - 1, sq1:getY(), sq1:getZ());
		elseif self.graves:getModData()["spriteType"] == "sprite2" then
			sq2 = getCell():getGridSquare(sq1:getX() + 1, sq1:getY(), sq1:getZ());
		end
	end
	
	self:increaseCorpse(sq2);

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISBuryCorpse:increaseCorpse(square)
	for i=0,square:getSpecialObjects():size()-1 do
		local grave = square:getSpecialObjects():get(i);
		if grave:getName() == "EmptyGraves" then
			grave:getModData()["corpses"] = grave:getModData()["corpses"] + 1;
			grave:transmitModData()
		end
	end
end

function ISBuryCorpse:new(character, graves, time, shovel)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = getSpecificPlayer(character);
	o.graves = graves;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
    o.caloriesModifier = 5;
    o.shovel = shovel
	return o;
end
