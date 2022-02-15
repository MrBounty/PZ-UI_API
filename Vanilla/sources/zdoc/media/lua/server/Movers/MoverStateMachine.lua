require "ISBaseObject"

---@class ISMoverStateMachine : ISBaseObject
ISMoverStateMachine = ISBaseObject:derive("ISMoverStateMachine");

ISMoverStateMachine.IDMax = 1;

function ISMoverStateMachine:init()

end

function ISMoverStateMachine:getCurrent()
    return self.state;
end

function ISMoverStateMachine:changeState(new)
    if self.state ~= nil then
        self.state:exit();
    end

    self.state = new;
    self.state.moverInst = self.state.mover.javaObject;

    self.state:enter();

end

function ISMoverStateMachine:update()

    if self.state ~= nil then
        self.state:during();
        if self.state:isFinished() then
            self.state:exit();
            self.state = nil;
        end
    end
end

function ISMoverStateMachine:new (mover)
    local o = {}
    setmetatable(o, self)
    self.__index = self
    o.mover = mover;
    o.moverInst = mover.javaObject;
    o:init();
    return o
end

