require "ISUI/ISPanel"

---@class ISImage : ISPanel
ISImage = ISPanel:derive("ISImage");


--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISImage:initialise()
	ISPanel.initialise(self);
end

function ISImage:getTexture()
	return self.texture;
end

function ISImage:setMouseOverText(text)
	self.mouseovertext = text;
end

function ISImage:setColor(r,g,b)
	self.backgroundColor.r = r;
	self.backgroundColor.g = g;
	self.backgroundColor.b = b;
end

function ISImage:onMouseMoveOutside(dx, dy)
	self.mouseover = false;
end

function ISImage:onMouseMove(dx, dy)
	self.mouseover = self:isMouseOver(); -- handle windows in front
end

function ISImage:onMouseUp(x,y)
	if self.onclick then
		self.onclick(self.target, self);
	end
end

--************************************************************************--
--** ISPanel:render
--**
--************************************************************************--
function ISImage:prerender()
	if self.texture then
		if self.scaledWidth and self.scaledHeight then
			self:drawTextureScaled(self.texture, 0, 0, self.scaledWidth, self.scaledHeight,  self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
		else
			self:drawTexture(self.texture, 0, 0, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
		end
	end
	if self.name then
		self:drawText(self.name, 0, (self.height/2), self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b, self.backgroundColor.a, self.font);
	end
    if self.textureOverride then
        self:drawTexture(self.textureOverride, (self.width /2) - (self.textureOverride:getWidth() / 2), (self.height /2) - (self.textureOverride:getHeight() / 2), self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    end
	if false and self.mouseover and self.mouseovertext then
		local width = getTextManager():MeasureStringX(UIFont.Small, self.mouseovertext) + 8
		local height = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight() + 2
		self:drawRect(0, self:getHeight() + 1, width, height, 0.7, 0.05, 0.05, 0.05)
		self:drawRectBorder(0, self:getHeight() + 1, width, height, 0.5, 0.9, 0.9, 1)
		self:drawText(self.mouseovertext, 4, self:getHeight() + 2, 1,1,1,1, UIFont.Small);
	end
	self:updateTooltip()
end

function ISImage:updateTooltip()
	if self.mouseover and self.mouseovertext then
		local text = self.mouseovertext
		if not self.tooltipUI then
			self.tooltipUI = ISToolTip:new()
			self.tooltipUI:setOwner(self)
			self.tooltipUI:setVisible(false)
			self.tooltipUI:setAlwaysOnTop(true)
			self.tooltipUI.maxLineWidth = 1000 -- don't wrap the lines
		end
		if not self.tooltipUI:getIsVisible() then
			self.tooltipUI:addToUIManager()
			self.tooltipUI:setVisible(true)
		end
		self.tooltipUI.description = text
		self.tooltipUI:setDesiredPosition(self:getAbsoluteX(), self:getAbsoluteY() + self:getHeight())
	else
		if self.tooltipUI and self.tooltipUI:getIsVisible() then
			self.tooltipUI:setVisible(false)
			self.tooltipUI:removeFromUIManager()
		end
    end
end

--************************************************************************--
--** ISPanel:new
--**
--************************************************************************--
function ISImage:new (x, y, width, height, texture)
	local o = {};
	o = ISPanel:new(x, y, width, height);
	setmetatable(o, self);
	self.__index = self
	o:noBackground();
	o.x = x;
	o.y = y;
	o.texture = texture;
    o.textureOverride = nil;
	o.backgroundColor = {r=1, g=1, b=1, a=1};
	o.borderColor = {r=1, g=1, b=1, a=0.7};
	o.width = width;
	o.height = height;
	o.mouseover = false;
	o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;
	o.font = UIFont.Small
	return o
end
