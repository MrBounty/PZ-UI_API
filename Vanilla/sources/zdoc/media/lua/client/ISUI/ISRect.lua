require "ISUI/ISUIElement"

---@class ISRect : ISUIElement
ISRect = ISUIElement:derive("ISRect");


--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISRect:initialise()
	ISUIElement.initialise(self);
end

--************************************************************************--
--** ISPanel:render
--**
--************************************************************************--
function ISRect:prerender()
	self:drawRectBorder(0, 0, self.width, self.height, self.a, self.r, self.g, self.b);
end
--************************************************************************--
--** ISPanel:new
--**
--************************************************************************--
function ISRect:new(x, y, width, height, a, r, g, b)
	local o = {}
	--o.data = {}
	o = ISUIElement:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self
	o.x = x;
	o.y = y;
	o.width = width;
	o.height = height;
	o.r = r;
	o.g = g;
	o.b = b;
	o.a = a;
	return o
end
