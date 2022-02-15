require "ISUI/ISPanel"

---@class ISRadioOption : ISPanel
ISRadioOption = ISPanel:derive("ISRadioOption");

--************************************************************************--
--** ISRadioOption:initialise
--**
--************************************************************************--

function ISRadioOption:initialise()
	ISPanel.initialise(self);
end



--************************************************************************--
--** ISRadioOption:render
--**
--************************************************************************--
function ISRadioOption:prerender()

end

--************************************************************************--
--** ISRadioOption:render
--**
--************************************************************************--
function ISRadioOption:render()
	local y = 20;
	self:drawText(self.name, 15, 0, 0.9, 0.9, 0.9, 1.0);
	local c = 1;
	for i,v in ipairs(self.options) do
		if self.mouseOverOption == c then
			self:drawRect(0, y-2, self.width, 16, 0.02, 1.0, 1.0, 1.0);
		end
		if self.selected == c then
			self:drawTexture(self.tickTexture, 15, y, 1, 1, 1, 1);
		end
		self:drawText(v, 16+15, y,  self.choicesColor.r, self.choicesColor.g, self.choicesColor.b, self.choicesColor.a);
		y = y + 16;
		c = c + 1;
	end
end

--************************************************************************--
--** ISRadioOption:onMouseUp
--**
--************************************************************************--
function ISRadioOption:onMouseUp(x, y)
	if self.changeOptionMethod ~= nil and self.mouseOverOption ~= nil and self.mouseOverOption > 0 and self.mouseOverOption < self.optionCount then
		self.selected = self.mouseOverOption;
		self.changeOptionMethod(self.changeOptionTarget, self.mouseOverOption);
	end

	return false;
end
function ISRadioOption:onMouseDown(x, y)

	return false;
end

--************************************************************************--
--** ISRadioOption:onMouseMove
--**
--************************************************************************--
function ISRadioOption:onMouseMove(dx, dy)
	local x = self:getMouseX();
	local y = self:getMouseY();
	if x >= 0 and y >= 0 and x<=self.width and y <= self.height then
		y = y - 20;
		y = y / 16;
		y = math.floor(y + 1);
		self.mouseOverOption = y;
	else
		self.mouseOverOption = 0;
	end

end

--************************************************************************--
--** ISRadioOption:onMouseMoveOutside
--**
--************************************************************************--
function ISRadioOption:onMouseMoveOutside(dx, dy)
	self.mouseOverOption = 0;
end


function ISRadioOption:addOption(name)

	self.options[self.optionCount] = name;
	self.optionCount = self.optionCount + 1;
end

function ISRadioOption:new (x, y, width, height, name, changeOptionTarget, changeOptionMethod)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.x = x;
	o.y = y;
	o.width = width;
	o.height = height;
	o.tickTexture = getTexture("media/ui/Quest_Succeed.png");
	o.borderColor = {r=1, g=1, b=1, a=0.2};
	o.backgroundColor = {r=0, g=0, b=0, a=0.5};
	o.choicesColor = {r=0.7, g=0.7, b=0.7, a=1};
	o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;
	o.name = name;
	o.options = {}
	o.optionCount = 1;
	o.selected = 1;
	o.leftMargin = 15;
	o.changeOptionMethod = changeOptionMethod;
	o.changeOptionTarget = changeOptionTarget;
	return o
end

