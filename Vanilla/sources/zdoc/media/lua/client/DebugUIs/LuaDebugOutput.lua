require "ISUI/ISPanel"

---@class LuaDebugOutput : ISPanel
LuaDebugOutput = ISPanel:derive("LuaDebugOutput");

function LuaDebugOutput:createChildren()

	self.outputListbox = ISScrollingListBox:new(0, 0, self.width, self.height);
	self.outputListbox:initialise();
	self.outputListbox:instantiate();
	self.outputListbox:setAnchorLeft(true);
	self.outputListbox:setAnchorRight(true);
	self.outputListbox:setAnchorTop(true);
	self.outputListbox:setAnchorBottom(true);
	self.outputListbox.itemheight = 18;
	self.outputListbox.doDrawItem = LuaDebugOutput.doDrawItem;
	self:addChild(self.outputListbox);
end


function LuaDebugOutput:doDrawItem(y, item, alt)
	if self.selected == item.index then
		self:drawRect(0, (y), self:getWidth(), self.itemheight-1, 0.1, 0.35, 0.35, 0.15);

	end
	if item.logtype == "debug" then
		self:drawText(item.text, 10, (y)+2, 0.9, 0.9, 0.9, 1.0, UIFont.Small);
	elseif item.logtype == "error" then
		self:drawText(item.text, 10, (y)+2, 0.9, 0.4, 0.4, 1.0, UIFont.Small);
    end
    y = y + self.itemheight;

    return y;
end

function LuaDebugOutput:debug(text)
    self.outputListbox:addItem(text, {}).logtype = "debug";
    if self.outputListbox:size() > 50 then self.outputListbox:removeFirst() end
    self.outputListbox:setYScroll(-1000000000000);
end


function LuaDebugOutput:clear()
    self.outputListbox:clear();
    self.outputListbox:setYScroll(-1000000000000);
end

function LuaDebugOutput:error(text)
	self.outputListbox:addItem(text, {}).logtype = "error";
    if self.outputListbox:size() > 50 then self.outputListbox:removeFirst() end
    self.outputListbox:setYScroll(-1000000000000);
end

function LuaDebugOutput:new (x, y, width, height)
	local o = ISPanel:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self
	o.x = x;
	o.y = y;
	o.backgroundColor = {r=0, g=0, b=0, a=1};
	o.borderColor = {r=1, g=1, b=1, a=0.7};
	LuaDebugOutput.instance = o;
    o.cat = {}
	return o
end

function pzerror(text, cat)
    if LuaDebugOutput.instance == nil then
        return;
    end
    if cat ~= nil and LuaDebugOutput.instance.cat[cat] ~= true then
        return
    end
    print(text);

    LuaDebugOutput.instance:error(text);

end

function pzdebugenabled(cat, enabled)
    LuaDebugOutput.instance.cat[cat] = enabled;
end

function pzdebug(text, cat)
    if LuaDebugOutput.instance == nil then
        return;
    end
    if cat ~= nil and LuaDebugOutput.instance.cat[cat] ~= true then
        return
    end
    print(text);
    LuaDebugOutput.instance:debug(text);
end

statCount = 1;
DoTickDebugLuaDebugOutputWindow = function ()

    statCount = statCount + 1;
    if(statCount > 60) then
        LuaDebugOutput.instance:clear();

        local sq = getPlayer():getCurrentSquare();
        pzdebug(sq:getX()..", "..sq:getY()..", "..sq:getZ(), nil);
        for i=0, sq:getObjects():size() - 1 do
           local o = sq:getObjects():get(i):getTextureName();
           pzdebug(o, nil);

        end
        statCount = 0;
    end



end
DoDebugLuaDebugOutputWindow = function ()

	local panel2 = LuaDebugOutput:new(800, 0, 400, 400);
	panel2:initialise();
	local wrap = panel2:wrapInCollapsableWindow("Debug Output");
	wrap:addToUIManager();
    pzdebugenabled("npc1", true);

 end

--Events.OnGameStart.Add(DoDebugLuaDebugOutputWindow);
--Events.OnTick.Add(DoTickDebugLuaDebugOutputWindow);
--Events.OnMainMenuEnter.Add(DoDebugLuaDebugOutputWindow);

