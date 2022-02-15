require "TimedActions/ISBaseTimedAction"

---@class ISReloadAction : ISBaseTimedAction
ISReloadAction = ISBaseTimedAction:derive("ISReloadAction")

function ISReloadAction:isValid()
	if self.reloadable then
		return self.reloadable:isReloadValid(self.character, self.square, self.mgr:getDifficulty())
	end
	return false
end

function ISReloadAction:update()
end

function ISReloadAction:start()
	if self.reloadable then
		self:setActionAnim(CharacterActionAnims.Reload);
		self:setAnimVariable("WeaponReloadType", self.reloadable.type);
		self:setAnimVariable("1WeaponReloadType", self.reloadable.type);
		self.reloadable:reloadStart(self.character, self.square, self.mgr:getDifficulty());
	end
end

function ISReloadAction:stop()
	self.mgr:stopReload()
	ISBaseTimedAction.stop(self)
end

function ISReloadAction:perform()
	self.reloadable:reloadPerform(self.character, self.square, self.mgr:getDifficulty(), self.reloadWeapon)
	self.mgr.reloadable = self.reloadable -- goes nil sometimes
	self.mgr:stopReloadSuccess()
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISReloadAction:new(reloadManager, char, square, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	-- Required fields
	o.character = char
	o.stopOnWalk = false
	o.stopOnRun = true
	o.stopOnAim = false
	-- Custom fields
    o.mgr = reloadManager
    local moodles = char:getMoodles();
    local panicLevel = moodles:getMoodleLevel(MoodleType.Panic);
    if instanceof(reloadManager.reloadWeapon, "HandWeapon") then
        o.maxTime = reloadManager.reloadWeapon:getReloadTime() * char:getReloadingMod() + panicLevel*30
    else
        o.maxTime = time;
    end
    o.reloadable = reloadManager.reloadable
	o.reloadWeapon = reloadManager.reloadWeapon
	o.square = square
	return o
end

