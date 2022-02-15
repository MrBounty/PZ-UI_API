require "ISUI/ISPanel"

ISResizeWidget = ISPanel:derive("ISResizeWidget");

--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISResizeWidget:initialise()
	ISPanel.initialise(self);
end


function ISResizeWidget:resize(dx, dy)
	local x = self.target.width+dx;
	local y = self.target.height+dy;

	if(self.target.minimumWidth == nil) then
		self.target.minimumWidth = 0;
		self.target.minimumHeight = 0;
	end

	if(x < self.target.minimumWidth)  then
		x = self.target.minimumWidth;
	end
	if(y < self.target.minimumHeight)  then
		y = self.target.minimumHeight;
	end

	if self.target:getY() + y > getCore():getScreenHeight() then
		y = getCore():getScreenHeight() - self.target:getY()
	end

    self.target.collapseCounter = 0;
	--self:setX(self.x+dx);
	--self:setY(self.y+dy);
	self.target:setWidth(x);
	self.target:setHeight(y);
end

--************************************************************************--
--** ISButton:onMouseMove
--**
--************************************************************************--
function ISResizeWidget:onMouseMove(dx, dy)
	self.mouseOver = true;
	if self.resizing then
		dx = self:getMouseX() - self.downX
		dy = self:getMouseY() - self.downY
		if self.yonly then
			self:resize(0, dy);
		else
			self:resize(dx, dy);
		end
    end
end

--************************************************************************--
--** ISButton:onMouseMoveOutside
--**
--************************************************************************--
function ISResizeWidget:onMouseMoveOutside(dx, dy)
	self.mouseOver = false;
	if self.resizing then
		dx = self:getMouseX() - self.downX
		dy = self:getMouseY() - self.downY
		if self.yonly then
			self:resize(0, dy);
		else
			self:resize(dx, dy);
		end
    end
end

--************************************************************************--
--** ISButton:onMouseUp
--**
--************************************************************************--
function ISResizeWidget:onMouseUp(x, y)
    if not self:getIsVisible() then
        return;
    end

    self.resizing = false;
    self:setCapture(false);
    return true;
end
function ISResizeWidget:onMouseUpOutside(x, y)
    if not self:getIsVisible() then
        return;
    end

    self.resizing = false;
    self:setCapture(false);
    return true;
end

function ISResizeWidget:onMouseDown(x, y)

	if not self:getIsVisible() then
		return;
	end

	self.downX = self:getMouseX();
	self.downY = self:getMouseY();
	self.resizing = true;
	self:setCapture(true);
	return true;
end

--************************************************************************--
--** ISResizeWidget:render
--**
--************************************************************************--
function ISResizeWidget:prerender()
 --   if(self.mouseOver) then
   --     self:drawRect(0, 0, self.width, self.height, self.backgroundColorMouseOver.a, self.backgroundColorMouseOver.r, self.backgroundColorMouseOver.g, self.backgroundColorMouseOver.b);
   -- else
   --     self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
  --  end
  --  self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
end

function ISResizeWidget:render()

end

--************************************************************************--
--** ISResizeWidget:new
--**
--************************************************************************--
function ISResizeWidget:new (x, y, width, height,  resizeTarget, yonly)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.x = x;
	o.y = y;
	o.borderColor = {r=1, g=1, b=1, a=0.7};
	o.backgroundColor = {r=0, g=0, b=0, a=1.0};
	o.backgroundColorMouseOver = {r=0.3, g=0.3, b=0.3, a=1.0};
	o.width = width;
	o.height = height;
	o.anchorLeft = false;
	o.anchorRight = true;
	o.anchorTop = false;
	o.anchorBottom = true;
	o.mouseOver = false;
    o.yonly = yonly;
	o.target = resizeTarget;
	return o
end
