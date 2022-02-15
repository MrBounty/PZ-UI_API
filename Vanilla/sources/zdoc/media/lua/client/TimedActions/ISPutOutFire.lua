--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISPutOutFire : ISBaseTimedAction
ISPutOutFire = ISBaseTimedAction:derive("ISPutOutFire");

function ISPutOutFire:isValid()
	return self.character:getInventory():contains(self.item) and
		FireFighting.isExtinguisher(self.item)
end

function ISPutOutFire:update()
	-- assumes 2x2 area
	if self.character:getJoypadBind() == -1 then
		self.character:faceLocation(self.squares[1]:getX() + 1, self.squares[1]:getY() + 1)
	end
end

function ISPutOutFire:start()
end

function ISPutOutFire:stop()
    ISBaseTimedAction.stop(self);
end

function ISPutOutFire:perform()
	for _,square in pairs(self.squares) do
		if square:isCouldSee(self.character:getPlayerNum()) then
			local used = false
			for i=1,square:getMovingObjects():size() do
				local chr = square:getMovingObjects():get(i-1)
				if instanceof(chr, "IsoGameCharacter") and chr:isOnFire() then
					if isClient() then
						chr:sendStopBurning();
					else
						chr:StopBurning()
					end
					used = true
				end
			end
			if square:Is(IsoFlagType.burning) then
				square:transmitStopFire()
				square:stopFire()
				used = true
			end
			if used and self:useItem() then
				break
			end
		end
	end
    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISPutOutFire:useItem()
	for i=1,self.usesPerSquare do
		self.item:Use()
		if self.item:getDrainableUsesInt() < 1 then
			break
		end
	end
	return self.item:getDrainableUsesInt() < self.usesPerSquare
end

function ISPutOutFire:new(character, squares, item, usesPerSquare, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item
	o.squares = squares;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	if o.character:isTimedActionInstant() then o.maxTime = 1; end
	o.usesPerSquare = usesPerSquare
	return o;
end
