--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISShovelGround : ISBaseTimedAction
ISShovelGround = ISBaseTimedAction:derive("ISShovelGround");

function ISShovelGround:isValid()
	if instanceof(self.emptyBag, "InventoryContainer") then
		if self.emptyBag:getInventory():isEmpty() == false then
			return false
		end
	end
	return self.character:getInventory():contains(self.emptyBag) and
		self.sandTile and self.sandTile:getSprite() and
		--self.sandTile:getSprite():getName() ~= self.newSprite and
		(self.emptyBag:getType() == "EmptySandbag" or self.emptyBag:getUsedDelta() < 1)
end

function ISShovelGround:waitToStart()
	self.character:faceThisObject(self.sandTile)
	return self.character:shouldBeTurning()
end

function ISShovelGround:update()
	self.emptyBag:setJobDelta(self:getJobDelta())
	self.character:faceThisObject(self.sandTile)
    self.character:setMetabolicTarget(Metabolics.DiggingSpade);
end

function ISShovelGround:start()
	local jobTypes = {
		["Base.Dirtbag"] = "ContextMenu_Take_some_dirt",
		["Base.Gravelbag"] = "ContextMenu_Take_some_gravel",
		["Base.Sandbag"] = "ContextMenu_Take_some_sands"
	}
	self.emptyBag:setJobType(getText(jobTypes[self.newBag]))
--    self.sound = getSoundManager():PlayWorldSound("shoveling", self.sandTile:getSquare(), 0, 5, 1, true);
	self:setActionAnim(ISFarmingMenu.getShovelAnim(self.character:getPrimaryHandItem()))
    self.sound = self.character:getEmitter():playSound("Shoveling")
	addSound(self.character, self.character:getX(), self.character:getY(), self.character:getZ(), 10, 1)
end

function ISShovelGround:stop()
	self.emptyBag:setJobDelta(0.0)
    if self.sound ~= 0 and self.character:getEmitter():isPlaying(self.sound) then
        self.character:getEmitter():stopSound(self.sound);
    end
    ISBaseTimedAction.stop(self);
end

function ISShovelGround:perform()
	self.emptyBag:setJobDelta(0.0)
    if self.sound ~= 0 and self.character:getEmitter():isPlaying(self.sound) then
        self.character:getEmitter():stopSound(self.sound);
    end
	local sq = self.sandTile:getSquare()
	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ() }
	sendClientCommand(self.character, 'object', 'shovelGround', args)

	-- FIXME: server should manage the player's inventory
	if self.emptyBag:getType() == "EmptySandbag" then
		local isPrimary = self.character:isPrimaryHandItem(self.emptyBag)
		local isSecondary = self.character:isSecondaryHandItem(self.emptyBag)
		self.character:removeFromHands(self.emptyBag);
		self.character:getInventory():Remove(self.emptyBag);
		local item = self.character:getInventory():AddItem(self.newBag);
		if item ~= nil then
			item:setUsedDelta(item:getUseDelta())
			if isPrimary then
				self.character:setPrimaryHandItem(item)
			end
			if isSecondary then
				self.character:setSecondaryHandItem(item)
			end
		end
	elseif self.emptyBag:getUsedDelta() + self.emptyBag:getUseDelta() <= 1 then
		self.emptyBag:setUsedDelta(self.emptyBag:getUsedDelta() + self.emptyBag:getUseDelta())
    end
    if ZombRand(5) == 0 then
        self.character:getInventory():AddItem("Base.Worm");
    end
	-- refresh backpacks to hide equipped filled dirt bags
	getPlayerInventory(self.character:getPlayerNum()):refreshBackpacks();
	getPlayerLoot(self.character:getPlayerNum()):refreshBackpacks();
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISShovelGround:new(character, emptyBag, sandTile, newSprite, newBag)
	local o = {};
	setmetatable(o, self);
	self.__index = self;
	o.character = character;
	o.emptyBag = emptyBag;
	o.sandTile = sandTile;
    o.newSprite = newSprite;
    o.newBag = newBag;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = 100;
    o.caloriesModifier = 8;
	return o;
end
