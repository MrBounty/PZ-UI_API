--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISShovelAction : ISBaseTimedAction
ISShovelAction = ISBaseTimedAction:derive("ISShovelAction");

function ISShovelAction:isValid()
	self.plant:updateFromIsoObject()
	return self.plant:getIsoObject() ~= nil
end

function ISShovelAction:waitToStart()
	self.character:faceThisObject(self.plant:getObject())
	return self.character:shouldBeTurning()
end

function ISShovelAction:update()
	self.item:setJobDelta(self:getJobDelta());
	self.character:faceThisObject(self.plant:getObject())
    self.character:setMetabolicTarget(Metabolics.DiggingSpade);
end

function ISShovelAction:start()
	self.item:setJobType(getText("ContextMenu_Remove"));
	self.item:setJobDelta(0.0);
    if self.plant:getSquare() then
        self.sound = getSoundManager():PlayWorldSound("Shoveling", self.plant:getSquare(), 0, 10, 1, true);
	end
	local anim = ISFarmingMenu.getShovelAnim(self.character:getPrimaryHandItem())
	self:setActionAnim(anim)
end

function ISShovelAction:stop()
    if self.sound and self.sound:isPlaying() then
        self.sound:stop();
    end
    ISBaseTimedAction.stop(self);
    self.item:setJobDelta(0.0);
end

function ISShovelAction:perform()
    if self.sound and self.sound:isPlaying() then
        self.sound:stop();
    end
	self.item:getContainer():setDrawDirty(true);
	self.item:setJobDelta(0.0);

	local sq = self.plant:getSquare()
	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ() }
	CFarmingSystem.instance:sendCommand(self.character, 'removePlant', args)

	ISBaseTimedAction.perform(self);
end

function ISShovelAction:new (character, item, plant, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
    o.plant = plant;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
    o.caloriesModifier = 4;
	return o
end
