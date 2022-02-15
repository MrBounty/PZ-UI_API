require "ISBaseObject"

---@class ISBaseState : ISBaseObject
ISBaseState = ISBaseObject:derive("ISBaseState");

function ISBaseState:enter()

end

function ISBaseState:during()

end

function ISBaseState:exit()

end

function ISBaseState:isFinished()
    return true;
end

function ISBaseState:new ()
    local o = {}
    setmetatable(o, self)
    self.__index = self

    return o
end

