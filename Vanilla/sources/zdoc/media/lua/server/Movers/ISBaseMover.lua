require "ISBaseObject"

---@class ISBaseMover : ISBaseObject
ISBaseMover = ISBaseObject:derive("ISBaseMover");

ISBaseMover.IDMax = 1;

function ISBaseMover:init()

end

function ISBaseMover:getSprite()
    return self.javaObject:getSprite();
end

function ISBaseMover:playAnim(name, seconds, looped, animate)
    self.javaObject:playAnim(name, seconds, looped, animate);
end

function ISBaseMover:changeState(state)
    self.states:changeState(state);
end

function ISBaseMover:update()
end

function ISBaseMover:postrender(col, bDoAttached)

end

function ISBaseMover:placeInWorld(x, y, z)

    if not getCell():getObjectList():contains(self.javaObject) then
        getCell():addMovingObject(self.javaObject);
    end

    self.javaObject:setX(x);
    self.javaObject:setY(y);
    self.javaObject:setZ(z);


end

function ISBaseMover:removeFromWorld()

   getCell():Remove(self.javaObject);

end

function ISBaseMover:new ()
    local o = {}
    setmetatable(o, self)
    self.__index = self
    o.javaObject = IsoLuaMover:new(o);
    o.sprite = o:getSprite();
    o.states = ISMoverStateMachine:new(o);
    o:init();

    return o
end

