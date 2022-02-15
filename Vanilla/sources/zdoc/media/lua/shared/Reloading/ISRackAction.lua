require "TimedActions/ISBaseTimedAction"

---@class ISRackAction : ISBaseTimedAction
ISRackAction = ISBaseTimedAction:derive("ISRackAction")

function ISRackAction:isValid()
	return true
end

function ISRackAction:update()
end

function ISRackAction:start()
	self.action:setUseProgressBar(getCore():getOptionRackProgress())
	self.reloadable:rackingStart(self.character, self.square, self.mgr.reloadWeapon)
end

function ISRackAction:stop()
	self.mgr:stopRacking()
	ISBaseTimedAction.stop(self)
end

function ISRackAction:perform()
	self.reloadable:rackingPerform(self.character, self.square, self.reloadWeapon)
	self.mgr:stopRacking()
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)

	-- reduce weapon's weight by the bullet's
	local ammo = ScriptManager.instance:getItem(self.reloadWeapon:getAmmoType());
	self.reloadWeapon:setActualWeight(self.reloadWeapon:getActualWeight() - ammo:getActualWeight());
end

function ISRackAction:new(reloadManager, char, square, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	-- Required fields
	o.character = char
	o.stopOnWalk = false
	o.stopOnRun = false
	o.stopOnAim = false
	o.maxTime = time
	-- Custom fields
	o.mgr = reloadManager
	o.reloadable = reloadManager.reloadable
	o.reloadWeapon = reloadManager.reloadWeapon
	o.square = square
	return o
end


