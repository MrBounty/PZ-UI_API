require "ISBaseObject"

---@class ISTimedActionQueue : ISBaseObject
ISTimedActionQueue = ISBaseObject:derive("ISTimedActionQueue");

ISTimedActionQueue.IDMax = 1;

ISTimedActionQueue.queues = {}

function ISTimedActionQueue:addToQueue (action)

	if isKeyDown(Keyboard.KEY_ESCAPE) then
		self:clearQueue()
	end

    local count = #self.queue;
    table.insert(self.queue, action );
    --self.queue[ISTimedActionQueue.IDMax] = {id = ISTimedActionQueue.IDMax, character = character, action = action};
    --print("action inserted. found "..count.." items on queue.");

    -- none in queue, so go!
    if count == 0 then
        self.current = action;
        action:begin();
        --print("action started.");
        --	ISTimedActionQueue.IDMax = ISTimedActionQueue.IDMax + 1;
    end

end

function ISTimedActionQueue:indexOf(action)
    for i,v in ipairs(self.queue) do
        if v == action then
            return i
        end
    end
    return -1
end

function ISTimedActionQueue:removeFromQueue(action)
    local i = self:indexOf(action)
    if i ~= -1 then
        table.remove(self.queue, i)
    end
end

function ISTimedActionQueue:clearQueue()
    table.wipe(self.queue)
end

function ISTimedActionQueue:onCompleted(action)
--~ 	print "removing completed action.";
	self:removeFromQueue(action)

	self.current = self.queue[1]

	if self.current then
--~ 		print ("starting next queued action: "..i);
		self.current:begin()
	end
end


function ISTimedActionQueue:resetQueue()
    --print("Clearing action queue.");
	table.wipe(self.queue)
	self.current = nil;
end

function ISTimedActionQueue:tick()
	local action = self.queue[1]
	if action == nil then
		self:clearQueue()
		return
	end
	if not action.character:getCharacterActions():contains(action.action) then
		print('bugged action, cleared queue', action.action)
		self:resetQueue()
		return
	end
	if action.action:hasStalled() then
		self:onCompleted(action)
		return
	end
end

--************************************************************************--
--** ISTimedActionQueue:new
--**
--************************************************************************--
function ISTimedActionQueue:new (character)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.queue = {}
	ISTimedActionQueue.queues[character] = o;
	return o
end

ISTimedActionQueue.getTimedActionQueue = function(character)
	local queue = ISTimedActionQueue.queues[character];
	if queue == nil then
		queue = ISTimedActionQueue:new(character);
	end

	return queue;
end

ISTimedActionQueue.add = function(action)
	if action.ignoreAction then
		return;
	end
	if instanceof(action.character, "IsoGameCharacter") and action.character:isAsleep() then
		return;
	end
	local queue = ISTimedActionQueue.getTimedActionQueue(action.character);

	local current = queue.queue[1];
	if current and (current.Type == "ISQueueActionsAction") and current.isAddingActions then
		table.insert(queue.queue, current.indexToAdd, action);
		current.indexToAdd = current.indexToAdd + 1;
		return queue;
	end

	queue:addToQueue(action);
	return queue;
end

ISTimedActionQueue.addAfter = function(previousAction, action)
	if action.ignoreAction then
		return nil;
	end
    if instanceof(action.character, "IsoGameCharacter") and action.character:isAsleep() then
        return nil;
    end
    local queue = ISTimedActionQueue.getTimedActionQueue(action.character);
    local i = queue:indexOf(previousAction)
    if i ~= -1 then
        table.insert(queue.queue, i + 1, action);
        return queue,action;
    end
    return nil
end

ISTimedActionQueue.queueActions = function(character, addActionsFunction, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10)
    local action = ISQueueActionsAction:new(character, addActionsFunction, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10)
    return ISTimedActionQueue.add(action)
end

ISTimedActionQueue.clear = function(character)
    --print("Stopping current action.")
    character:StopAllActionQueue();
    --print("Clearing queue.")
    local queue = ISTimedActionQueue.getTimedActionQueue(character);
    queue:clearQueue();
    return queue;
end

ISTimedActionQueue.hasAction = function(action)
    if action == nil then return false end
    local queue = ISTimedActionQueue.queues[action.character]
    if queue == nil then return false end
    return queue:indexOf(action) ~= -1
end

local STATES = {}
STATES[ClimbThroughWindowState.instance()] = true
STATES[ClimbOverFenceState.instance()] = true
STATES[ClimbOverWallState.instance()] = true
STATES[ClimbSheetRopeState.instance()] = true
STATES[ClimbDownSheetRopeState.instance()] = true
STATES[CloseWindowState.instance()] = true
STATES[OpenWindowState.instance()] = true

ISTimedActionQueue.isPlayerDoingAction = function(playerObj)
    if not playerObj then return false end
    if playerObj:isDead() then return false end
    if not playerObj:getCharacterActions():isEmpty() then return true end
    local state = playerObj:getCurrentState()
    if STATES[state] then return true end
    return false
end

-- master function for stalled queues
ISTimedActionQueue.onTick = function()

    for _,queue in pairs(ISTimedActionQueue.queues) do
        queue:tick()
    end

    if not getCore():getOptionTimedActionGameSpeedReset() then
        return
    end
    
    local isDoingAction = false
    for playerNum = 1,getNumActivePlayers() do
        local playerObj = getSpecificPlayer(playerNum-1)
        if ISTimedActionQueue.isPlayerDoingAction(playerObj) then
            isDoingAction = true
            break
        end
    end

    if isDoingAction then
        ISTimedActionQueue.shouldResetGameSpeed = true
    elseif ISTimedActionQueue.shouldResetGameSpeed then
        ISTimedActionQueue.shouldResetGameSpeed = false
        if UIManager.getSpeedControls() and (UIManager.getSpeedControls():getCurrentGameSpeed() > 1) then
            UIManager.getSpeedControls():SetCurrentGameSpeed(1)
        end
    end
end

Events.OnTick.Add(ISTimedActionQueue.onTick);

