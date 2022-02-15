---@class ISBaseObject
ISBaseObject = {};

ISBaseObject.Type = "ISBaseObject";

--************************************************************************--
--** ISBaseObject:initialise
--**
--************************************************************************--
function ISBaseObject:initialise()

end

--************************************************************************--
--** ISBaseObject:derive
--**
--************************************************************************--
function ISBaseObject:derive (type)
    local o = {}
    setmetatable(o, self)
    self.__index = self
	o.Type= type;
    return o
end

function ISBaseObject:new()
	local o = {}
	setmetatable(o, self)
	self.__index = self
	return o
end

