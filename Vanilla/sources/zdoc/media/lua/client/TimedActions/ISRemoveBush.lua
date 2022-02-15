--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISRemoveBush : ISBaseTimedAction
ISRemoveBush = ISBaseTimedAction:derive("ISRemoveBush")

function ISRemoveBush:isValid()
	if self.wallVine and not self:getWallVineObject(self.square) then
		return false
	end
	if not self.wallVine and not self:getBushObject(self.square) then
		return false
	end
	return (self.weapon and self.weapon:getCondition() > 0) or not self.weapon;
end

function ISRemoveBush:waitToStart()
	if self.wallVine then
		local object = self:getWallVineObject(self.square)
		if object then
			self.character:faceThisObject(object)
		end
	else
		self.character:faceLocation(self.square:getX(), self.square:getY())
	end
	return self.character:shouldBeTurning()
end

function ISRemoveBush:update()
	if self.wallVine then
		local object = self:getWallVineObject(self.square)
		if object then self.character:faceThisObject(object) end
	else
		self.character:faceLocation(self.square:getX(), self.square:getY())
	end

	self.spriteFrame = self.character:getSpriteDef():getFrame()
--	self:setJobDelta(1 - self.tree:getHealth() / self.tree:getMaxHealth())

    self.character:setMetabolicTarget(Metabolics.DiggingSpade);
end

function ISRemoveBush:start()
    self.weapon = self.character:getPrimaryHandItem()
	addSound(self.character, self.character:getX(), self.character:getY(), self.character:getZ(), 20, 10)
	if self.weapon then
		if self.weapon:getScriptItem():getCategories():contains("Axe") then
			self:setActionAnim("RemoveBushAxe")
		elseif self.weapon:getScriptItem():getCategories():contains("LongBlade") then
			self:setActionAnim("RemoveBushLongBlade")
		elseif self.weapon:getScriptItem():getCategories():contains("SmallBlade") then
			self:setActionAnim("RemoveBushKnife")
		else
			self:setActionAnim("RemoveBush")
		end
	else
		self:setActionAnim("RemoveBush")
	end
end

function ISRemoveBush:stop()
    ISBaseTimedAction.stop(self)
end

function ISRemoveBush:animEvent(event, parameter)
	if event == 'Chop' then
		self.square:playSound("ChopTree");
		addSound(self.character, self.character:getX(), self.character:getY(), self.character:getZ(), 20, 1)
		self:useEndurance()
		if self.weapon and ZombRand(self.weapon:getConditionLowerChance() * 4) == 0 then
			self.weapon:setCondition(self.weapon:getCondition() - 1)
			ISWorldObjectContextMenu.checkWeapon(self.character)
		end
	end
end

function ISRemoveBush:getBushObject(square)
	if not square then return nil end
	for i=1,square:getObjects():size() do
		local o = square:getObjects():get(i-1)
		if o:getSprite() and o:getSprite():getProperties() and o:getSprite():getProperties():Is(IsoFlagType.canBeCut) then
			return o
		end
	end
	return nil
end

function ISRemoveBush:getWallVineObject(square)
	if not square then return nil end
	for i=0,square:getObjects():size()-1 do
		local object = square:getObjects():get(i);
		local attached = object:getAttachedAnimSprite()
		if attached then
			for n=1,attached:size() do
				local sprite = attached:get(n-1)
--					if sprite and sprite:getParentSprite() and sprite:getParentSprite():getProperties():Is(IsoFlagType.canBeCut) then
				if sprite and sprite:getParentSprite() and sprite:getParentSprite():getName() and luautils.stringStarts(sprite:getParentSprite():getName(), "f_wallvines_") then
					return object, n-1
				end
			end
		end
	end
	return nil
end

function ISRemoveBush:perform()
	local sq = self.square
	local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ(), wallVine = self.wallVine }
	sendClientCommand(self.character, 'object', 'removeBush', args)

    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self)
end

function ISRemoveBush:useEndurance()
	if self.weapon and self.weapon:isUseEndurance() then
		local use = self.weapon:getWeight() * self.weapon:getFatigueMod(self.character) * self.character:getFatigueMod() * self.weapon:getEnduranceMod() * 0.1
		local useChargeDelta = 1.0
		use = use * useChargeDelta * 0.041
		if self.weapon:isTwoHandWeapon() and self.character:getSecondaryHandItem() ~= self.weapon then
			use = use + self.weapon:getWeight() / 1.5 / 10 / 20
		end
		self.character:getStats():setEndurance(self.character:getStats():getEndurance() - use)
	end
end

function ISRemoveBush:new(character, square, wallVine)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.square = square
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = 100
	o.spriteFrame = 0
	o.wallVine = wallVine
    if character:isTimedActionInstant() then
        o.maxTime = 1;
    end
	return o
end
