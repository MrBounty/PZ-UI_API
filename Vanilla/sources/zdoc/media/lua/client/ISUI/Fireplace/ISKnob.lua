--***********************************************************
--**              	  ROBERT JOHNSON                       **
--***********************************************************

---@class ISKnob : ISUIElement
ISKnob = ISUIElement:derive("ISKnob");
ISKnob.messages = {};

--************************************************************************--
--** ISKnob:initialise
--**
--************************************************************************--

function ISKnob:initialise()
    ISUIElement.initialise(self);
end

function ISKnob:render()
    self:drawTextCentre(self.title, self.width/2, 0, 1, 1, 1, 1, UIFont.Small)
    if self.valuesBg then
		if self.joypadFocused then
			self:drawTexture(self.valuesBg, 0, 20, 1, 1, 1, 1);
		else
			self:drawTexture(self.valuesBg, 0, 20, 1, 0.85, 0.85, 0.85);
		end
    end
	
	
	
    if self.values[self.selected] then
		local centerX = self.width / 2
		local centerY = self.height / 2
		if self.valuesBg then
			centerY = centerY + 20
		end
        self:DrawTextureAngle(self.tex, centerX, centerY, self.values[self.selected].angle);
    end
    ISUIElement.render(self);
--    for i,v in ipairs(self.values) do
--        self:drawTextCentre(self.values[i].value .. "", self.values[i].textX, self.values[i].textY, 1,1,1,1,UIFont.Small)
--    end
end

function ISKnob:prerender()

end

function ISKnob:addValue(angle, value)
    local newValue = {};
    newValue.angle =  angle;
    newValue.value = value;
--    newValue.textX = getCore():getXAngle(self.tex:getWidth(), angle + 45);
--    newValue.textY = getCore():getYAngle(self.tex:getWidth(), angle + 45);
    table.insert(self.values, newValue);

    if self.selected == 0 then
        self.selected = 1;
    end

    self.amplitude = 360 - (360 / #self.values);
end

function ISKnob:forceClick()

end

function ISKnob:setJoypadFocused(focused)
    self.joypadFocused = focused;
end

function ISKnob:onJoypadDirUp(joypadData)
    if self.selected < #self.values then
        self.selected = self.selected + 1
    else
        self.selected = 1
    end
    if self.onMouseUpFct and  self.lastValue ~= self.selected then
        self.onMouseUpFct(self.target, self);
        self.lastValue = self.selected;
    end
end

function ISKnob:onJoypadDirDown(joypadData)
    if self.selected > 1 then
        self.selected = self.selected - 1;
    else
        self.selected = #self.values
    end
    if self.onMouseUpFct and  self.lastValue ~= self.selected then
        self.onMouseUpFct(self.target, self);
        self.lastValue = self.selected;
    end
end

function ISKnob:getValue()
    return self.values[self.selected].value;
end

function ISKnob:setKnobPosition(value)
    for i=1,#self.values do
        if value == self.values[i].value then
            self.selected = i
            self.lastValue = i
            return
        end
    end
    -- Oven time remaining may be any number of minutes.
    for i=#self.values,2,-1 do
        local prev = self.values[i - 1].value
        local knobVal = self.values[i].value
        if value >= prev + (knobVal - prev) / 2 then
            self.selected = i;
            self.lastValue = self.selected;
            return
        end
    end
    self.selected = 1
    self.lastValue = self.selected
end

function ISKnob:onMouseDown(x, y)
--    setMouseXY(self:getAbsoluteX() + ((self.amplitude / #self.values) * (self.selected - 1)), self.height/2 + self:getAbsoluteY())
    self.draggingX = self:getMouseX();
    self.originalX = self:getMouseX();
    self.dragging = true;
end

function ISKnob:onMouseUp(x, y)
    self.dragging = false;
    if self.onMouseUpFct and  self.lastValue ~= self.selected then
        self.onMouseUpFct(self.target, self);
        self.lastValue = self.selected;
    end
end


function ISKnob:onMouseUpOutside(x, y)
    self.dragging = false;
    if self.onMouseUpFct and self.lastValue ~= self.selected then
        self.onMouseUpFct(self.target, self);
        self.lastValue = self.selected;
    end
end

function ISKnob:onMouseMoveOutside(dx, dy)
    self:onMouseMove(dx, dy);
end

function ISKnob:onMouseMove(dx, dy)
    if self.dragging then
		local centerX = self.width / 2
		local centerY = self.height / 2 + 20
		local mouseX = self:getMouseX()
		local mouseY = self:getMouseY()
		local radians = math.atan2(mouseY - centerY, mouseX - centerX) + math.pi
		-- 0 = north, 90 = west, 180 = south, 270 = east
		local degrees = (radians * 180 / math.pi + 270) % 360

		-- Microwave/Oven timers go from 0 to 270 degrees.
		-- Vehicle ac/heater goes from 270 to 0 to 90
		local last = self.values[#self.values].angle
		if degrees >= last + (360 - last) / 2 then
			self.selected = 1
			return
		end
		local prev = 0
		for i=1,#self.values do
			local cur = self.values[i].angle
			local next = (i == #self.values) and 360 or self.values[i+1].angle
			if (degrees >= prev + (cur - prev) / 2) and (degrees < cur + (next - cur) / 2) then
				self.selected = i
				break
			end
			prev = cur
		end
    end
end

--************************************************************************--
--** ISKnob:new
--**
--************************************************************************--
function ISKnob:new(x, y, tex, valuesBg, title, player)
    local o = {}
    if valuesBg then
        width = valuesBg:getWidthOrig();
        height = valuesBg:getHeightOrig();
    else
        width = tex:getWidthOrig();
        height = tex:getHeightOrig();
    end
    o = ISUIElement:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.width = width;
    o.height = height;
    o.player = player;
    o.selected = 0;
    o.amplitude = 180;
    o.values = {};
    o.valuesBg = valuesBg;
    o.title = title;
	o.joypadFocused = false
	o.isKnob = true;
    o.tex = tex;
    return o;
end
