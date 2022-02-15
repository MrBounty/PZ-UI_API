require "ISBaseObject"

---@class MoveToState : ISBaseState
MoveToState = ISBaseState:derive("MoveToState");

function MoveToState:enter()
    self.moverInst:PathTo(self.x, self.y, self.z, false);
end

function MoveToState:during()
    self.moverInst:PathTo(self.x, self.y, self.z, false);
end

function MoveToState:exit()

end

function MoveToState:setTarget(x, y, z)
    self.x = x;
    self.y = y;
    self.z = z;
end

function MoveToState:isFinished()
    return self.x == self.moverInst:getX() and self.y == self.moverInst:getY() and self.z == self.moverInst:getZ();
end

function MoveToState:new (mover, x, y, z)
    local o = {}
    setmetatable(o, self)
    self.__index = self

    o.mover = mover;
    o.x = x;
    o.y = y;
    o.z = z;
    return o
end

