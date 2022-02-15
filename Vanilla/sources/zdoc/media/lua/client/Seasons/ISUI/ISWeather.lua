--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "ISUI/ISUIElement"

---@class ISWeather : ISPanel
ISWeather = ISPanel:derive("ISWeather");


--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISWeather:initialise()
	ISPanel.initialise(self);
end


function ISWeather:addImage(image)
	table.insert(self.images,getTexture(image));
end

function ISWeather:addMoon(moonImage)
	self.moon = getTexture(moonImage);
end

function ISWeather:removeMoon()
	self.moon = nil;
end

function ISWeather:removeImages()
	self.images = {};
end


--************************************************************************--
--** ISPanel:render
--**
--************************************************************************--
function ISWeather:prerender()
	local width = 43;
	local x = 43;
	if self.moon then
		width = 88;
		x = 0;
	end
	self:drawRect((Core:getInstance():getOffscreenWidth() - 208) + x, 0, width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
	self:drawRectBorder((Core:getInstance():getOffscreenWidth() - 208) + x, 0, width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	for i,v in pairs(self.images) do
		self:drawTexture(v, (Core:getInstance():getOffscreenWidth() - 208) + x + 1, (self.height / 2) - (v:getHeight() / 2), 1, 1, 1, 1);
	end
	if self.moon then
		self:drawTexture(self.moon, (Core:getInstance():getOffscreenWidth() - 173) + x + 1, (self.height / 2) - (self.moon:getHeight() / 2), 1, 1, 1, 1);
    end

    -- turned it off until can reposition it.
    self:setVisible(false);
end
--************************************************************************--
--** ISPanel:new
--**
--************************************************************************--
function ISWeather:new (x, y, width, height)
	local o = {}
	--o.data = {}
	o = ISPanel:new(x, y, width, height);
	setmetatable(o, self)
    self.__index = self
	o.x = x;
	o.y = y;
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.5};
	o.width = width;
	o.images = {};
	o.moon = nil;
	o.height = height;
	o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;
   return o
end

