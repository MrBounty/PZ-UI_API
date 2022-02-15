require "TimedActions/ISBaseTimedAction"

---@class ISAddSheetRope : ISBaseTimedAction
ISAddSheetRope = ISBaseTimedAction:derive("ISAddSheetRope");

function ISAddSheetRope:isValid()
    local inv = self.character:getInventory()
    if IsoWindowFrame.isWindowFrame(self.window) then
        local numRequired = IsoWindowFrame.countAddSheetRope(self.window)
        return IsoWindowFrame.canAddSheetRope(self.window) and (inv:getNumberOfItem("SheetRope") >= numRequired or inv:getNumberOfItem("Rope") >= numRequired)
    else
        local numRequired = self.window:countAddSheetRope()
        return self.window:canAddSheetRope() and (inv:getNumberOfItem("SheetRope") >= numRequired or inv:getNumberOfItem("Rope") >= numRequired)
    end
end

function ISAddSheetRope:waitToStart()
    self.character:faceThisObject(self.window)
    return self.character:shouldBeTurning()
end

function ISAddSheetRope:update()
    self.character:faceThisObject(self.window)
    self.character:setMetabolicTarget(Metabolics.HeavyDomestic);
end

function ISAddSheetRope:start()
    self:setActionAnim("Loot")
    self.character:SetVariable("LootPosition", "Mid")
end

function ISAddSheetRope:stop()
    ISBaseTimedAction.stop(self);
end

function ISAddSheetRope:perform()
    local numRequired
    if IsoWindowFrame.isWindowFrame(self.window) then
        numRequired = IsoWindowFrame.countAddSheetRope(self.window)
    else
        numRequired = self.window:countAddSheetRope()
    end
    local inv = self.character:getInventory()
    local numSheetRope = inv:getNumberOfItem("Base.SheetRope")
    local numRope = inv:getNumberOfItem("Base.Rope")
    local itemType = (numSheetRope >= numRequired) and "SheetRope" or "Rope"
    local obj = self.window
    local index = obj:getObjectIndex()
    local args = { x=obj:getX(), y=obj:getY(), z=obj:getZ(), index=index, itemType=itemType }
    sendClientCommand(self.character, 'object', 'addSheetRope', args)

    -- needed to remove from queue / start next.
    ISBaseTimedAction.perform(self);
end

function ISAddSheetRope:new(character, window)
    local o = {}
    setmetatable(o, self)
    self.__index = self
    o.character = character;
    o.stopOnWalk = true;
    o.stopOnRun = true;
    o.maxTime = 20;
    -- custom fields
    o.window = window;
    return o;
end
