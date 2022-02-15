require "Movers/ISBaseMover"

---@class Rabbit : ISBaseMover
Rabbit = ISBaseMover:derive("Rabbit");

function Rabbit:init()
    print("Initing rabbit");
    self.sprite:LoadFramesNoDirPageSimple("Item_DeadRat");
end

function Rabbit:update()

    if ZombRand(100)==0 then
        self:changeState(MoveToState:new(self, ZombRand(getPlayer():getX()-20, getPlayer():getX()+20), ZombRand(getPlayer():getY()-20, getPlayer():getY()+20), 0));
    end

    ISBaseMover.update(self);
end

function Rabbit:postrender(col, bDoAttached)

end

function Rabbit:new ()
    local o = ISBaseMover:new();
    setmetatable(o, self)
    self.__index = self
    o.javaObject = IsoLuaMover:new(o);
    o.sprite = o:getSprite();
    o:init();
    return o
end

function testCreateRabbit()

    local rabbit = Rabbit:new();

    rabbit:placeInWorld(getPlayer():getX(), getPlayer():getY()+1, getPlayer():getZ())
    rabbit:changeState(MoveToState:new(rabbit, ZombRand(getPlayer():getX()-20, getPlayer():getX()+20), ZombRand(getPlayer():getY()-20, getPlayer():getY()+20), 0));

end

--Events.OnGameStart.Add(testCreateRabbit);
