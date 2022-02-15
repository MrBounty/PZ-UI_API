--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISStopAlarmClockAction : ISBaseTimedAction
ISStopAlarmClockAction = ISBaseTimedAction:derive("ISStopAlarmClockAction");

function ISStopAlarmClockAction:isValid()
    return true;
end

function ISStopAlarmClockAction:update()
end

function ISStopAlarmClockAction:start()
end

function ISStopAlarmClockAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISStopAlarmClockAction:perform()
    self.alarm:stopRinging()
    self.alarm:syncStopRinging()
    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self);
end

function ISStopAlarmClockAction:new(character, alarm, time)
	local o = ISBaseTimedAction.new(self, character);
	o.alarm = alarm;
	o.stopOnWalk = alarm:getWorldItem() ~= nil;
	o.stopOnRun = true;
	o.maxTime = time;
	return o;
end
