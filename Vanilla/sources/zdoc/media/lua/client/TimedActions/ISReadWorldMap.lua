--***********************************************************
--**                   THE INDIE STONE                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISReadWorldMap : ISBaseTimedAction
ISReadWorldMap = ISBaseTimedAction:derive("ISReadWorldMap");

function ISReadWorldMap:isValid()
	return ISWorldMap.IsAllowed()
end

function ISReadWorldMap:update()
end

function ISReadWorldMap:start()
	self:setAnimVariable("ReadType", "newspaper")
	self:setActionAnim(CharacterActionAnims.Read)
    self:setOverrideHandModels(nil, "Base.Newspaper");
--	self.character:reportEvent("EventRead")
end

function ISReadWorldMap:stop()
	ISBaseTimedAction.stop(self)
end

function ISReadWorldMap:perform()
	ISWorldMap.ShowWorldMap(self.playerNum)

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self)
end

function ISReadWorldMap:new(character)
	local o = ISBaseTimedAction.new(self, character)
	o.stopOnWalk = true
	o.stopOnRun = true
	o.maxTime = 50
	if character:isTimedActionInstant() then
		o.maxTime = 1
	end
	o.playerNum = character:getPlayerNum()
	return o
end

