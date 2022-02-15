require "ISBaseObject"

---@class LuaList : ISBaseObject
LuaList = ISBaseObject:derive("LuaList");

function LuaList:initialise()

end

function LuaList:add(item)
    if self.debug == true then
        print("add");
    end
    table.insert(self.items, item);
	self.count = self.count + 1;
end

function LuaList:size()
	return self.count;
end


function LuaList:isEmpty()
	return self.count <= 0;
end

function LuaList:clear()
    if self.debug == true then
        print("clearing list");
    end
	self.count = 0;
	self.items = {}
end

function LuaList:get(index)
	return self.items[index+1];
end

function LuaList:sort(f)
   table.sort(self.items, f);
end

function LuaList:foreach(func, param1, param2, param3, param4)
	for k, v in ipairs(self.items) do
		func(v, param1, param2, param3, param4);
	end
end

function LuaList:remove(item)
    if self.debug == true then
        print("removing from list");
    end
    for i = 0, self.count-1 do
        if self:get(i) == item then
            --print("found item to remove");

            table.remove(self.items, i+1);
            self.count = self.count - 1;
            if self.count < 0 then self.count = 0; end
            return item;
        end
    end
    if self.count < 0 then self.count = 0; end
    --print("failed to remove");
end

function LuaList:pop()
    if self.debug == true then
        print("popping from list");
    end
    if self.count == 0 then return nil end;

    local get = self:get(self.count - 1);

    self:remove(get);

    return get;
end

function LuaList:contains(item)
    for i = 0, self.count-1 do
        if self:get(i) == item then
            return true;
        end
    end
    return false;
end

function LuaList:addAll(list)
    if self.debug == true then
        print("add all");
    end
    for i=0, list:size()-1 do
		self:add(list:get(i));
	end
end

function LuaList:removeRandom()
	if self.count == 0 then return nil; end
    if self.debug == true then
        print("removing random");
    end

    local i = ZombRand(self.count)+1;

	local item = self.items[i];

	if item == nil then
		--print("item was nil");
		return nil;
	end
	table.remove(self.items, i);

	self.count = self.count - 1;
	if self.count < 0 then self.count = 0; end
	return item;
end


function LuaList:removeAt(index)
    if self.count == 0 then return nil; end

    local i = index+1;

    local item = self.items[i];

    if item == nil then
        --print("item was nil");
        return nil;
    end
    table.remove(self.items, i);

    self.count = self.count - 1;
    if self.count < 0 then self.count = 0; end
    return item;
end
function LuaList:new ()
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.items = {}
	o.count = 0;
	return o
end
