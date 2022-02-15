require"ISUI/ISPanelJoypad"

---@class ISColorPicker : ISPanelJoypad
ISColorPicker = ISPanelJoypad:derive("ISColorPicker");

function ISColorPicker:render()
	ISPanelJoypad.render(self)
	for i,color in ipairs(self.colors) do
		local col = (i-1) % self.columns
		local row = math.floor((i-1) / self.columns)
		self:drawRect(self.borderSize + col * self.buttonSize, self.borderSize + row * self.buttonSize, self.buttonSize, self.buttonSize, 1.0, color.r, color.g, color.b)
	end
	for col=1,self.columns do
		self:drawRect(self.borderSize + col * self.buttonSize, self.borderSize, 1, self.buttonSize * self.rows, 1.0, 0.0, 0.0, 0.0)
	end
	for row=1,self.rows do
		self:drawRect(self.borderSize, self.borderSize + row * self.buttonSize, self.buttonSize * self.columns, 1, 1.0, 0.0, 0.0, 0.0)
	end

	local col = (self.index-1) % self.columns
	local row = math.floor((self.index-1) / self.columns)
	self:drawRectBorder(self.borderSize + col * self.buttonSize, self.borderSize + row * self.buttonSize, self.buttonSize + 1, self.buttonSize + 1, 1.0, 1.0, 1.0, 1.0)

	if self.joyfocus then
		self:drawRectBorder(0, -self:getYScroll(), self:getWidth(), self:getHeight(), 0.4, 0.2, 1.0, 1.0);
		self:drawRectBorder(1, 1-self:getYScroll(), self:getWidth()-2, self:getHeight()-2, 0.4, 0.2, 1.0, 1.0);
	end
end

function ISColorPicker:onMouseDown(x, y)
	self.mouseDown = true
	self:onMouseMove(0, 0)
	return true
end

function ISColorPicker:onMouseDownOutside(x, y)
	self:removeSelf()
	return true
end

function ISColorPicker:onMouseMove(dx, dy)
    if self.otherFct then return true; end
	if not self.mouseDown then return true end
	local x = self:getMouseX()
	local y = self:getMouseY()
	local col = math.floor((x - self.borderSize) / self.buttonSize)
	local row = math.floor((y - self.borderSize) / self.buttonSize)
	if col < 0 then col = 0 end
	if col >= self.columns then col = self.columns - 1 end
	if row < 0 then row = 0 end
	if row >= self.rows then row = self.rows - 1 end
	local index = col + row * self.columns + 1
	if index == self.index then return true end
	self.index = index
	if self.pickedFunc then
		self.pickedFunc(self.pickedTarget, self.colors[self.index], false, self.pickedArgs[1], self.pickedArgs[2], self.pickedArgs[3], self.pickedArgs[4])
	end
	return true
end

function ISColorPicker:onMouseUp(x, y)
	if self.mouseDown then
		self.mouseDown = false
        if self.otherFct then
		    self:picked2(true)
        else
            self:picked(true)
        end
	end
	return true
end

function ISColorPicker:picked2(hide)
    if hide then
        self:removeSelf()
    end
    local x = self:getMouseX()
    local y = self:getMouseY()
    local col = math.floor((x - self.borderSize) / self.buttonSize)
    local row = math.floor((y - self.borderSize) / self.buttonSize)
    if col < 0 then col = 0 end
    if col >= self.columns then col = self.columns - 1 end
    if row < 0 then row = 0 end
    if row >= self.rows then row = self.rows - 1 end
    self.index = col + row * self.columns + 1
    if self.pickedFunc then
        self.pickedFunc(self.pickedTarget, self.colors[self.index], false, self.pickedArgs[1], self.pickedArgs[2], self.pickedArgs[3], self.pickedArgs[4])
    end
end

function ISColorPicker:onMouseUpOutside(x, y)
	return self:onMouseUp(x, y)
end

function ISColorPicker:onJoypadDirLeft(joypadData)
	local col = (self.index-1) % self.columns
	if col > 0 then self.index = self.index - 1 else self.index = self.index + self.columns - 1 end
	self:picked(false)
end

function ISColorPicker:onJoypadDirRight(joypadData)
	local col = (self.index-1) % self.columns
	if col < self.columns-1 then self.index = self.index + 1 else self.index = self.index - self.columns + 1 end
	self:picked(false)
end

function ISColorPicker:onJoypadDirUp(joypadData)
	local row = math.floor((self.index-1) / self.columns)
	if row > 0 then self.index = self.index - self.columns else self.index = self.index + self.columns * (self.rows - 1) end
	self:picked(false)
end

function ISColorPicker:onJoypadDirDown(joypadData)
	local row = math.floor((self.index-1) / self.columns)
	if row < self.rows-1 then self.index = self.index + self.columns else self.index = self.index - self.columns * (self.rows - 1) end
	self:picked(false)
end

function ISColorPicker:onJoypadDown(button)
	if button == Joypad.AButton then
		self:picked(true)
	end
	if button == Joypad.BButton then
		self:removeSelf()
	end
end

function ISColorPicker:removeSelf()
	if self.parent then
		self.parent:removeChild(self)
	else
		self:removeFromUIManager()
	end
	if self.joyfocus then
		self.joyfocus.focus = self.resetFocusTo
	end
end

function ISColorPicker:picked(hide)
	if hide then
		self:removeSelf()
	end
	if self.pickedFunc then
		self.pickedFunc(self.pickedTarget, self.colors[self.index], mouseUp, self.pickedArgs[1], self.pickedArgs[2], self.pickedArgs[3], self.pickedArgs[4])
	end
end

function ISColorPicker:setInitialColor(initial)
	local d = 10000000
	for index,color in ipairs(self.colors) do
		local dr = math.abs(initial:getR() - color.r)
		local dg = math.abs(initial:getG() - color.g)
		local db = math.abs(initial:getB() - color.b)
		if dr + dg + db < d then
			d = dr + dg + db
			self.index = index
		end
	end
end

function ISColorPicker:setPickedFunc(func, arg1, arg2, arg3, arg4)
	self.pickedFunc = func
	self.pickedArgs = { arg1, arg2, arg3, arg3 }
end

function ISColorPicker:setColors(colors, columns, rows)
	self.colors = colors
	self.columns = columns
	self.rows = rows
	local width = columns * self.buttonSize + self.borderSize * 2
	local height = rows * self.buttonSize + self.borderSize * 2
	self:setWidth(width)
	self:setHeight(height)
	self.index = math.min(self.index, #colors)
end

function ISColorPicker:new(x, y, HSBFactor)
	local buttonSize = 20
	local borderSize = 12
	local columns = 18
	local rows = 12
	local width = columns * buttonSize + borderSize * 2
	local height = rows * buttonSize + borderSize * 2
	local o = ISPanelJoypad.new(self, x, y, width, height)
	o.backgroundColor.a = 1
	o.borderSize = borderSize
	o.buttonSize = buttonSize
	o.columns = columns
	o.rows = rows
	o.index = 1
	o.pickedArgs = {}

	o.colors = {}
	local i = 0
	local newColor = Color.new(1.0, 1.0, 1.0, 1.0);
	for red = 0,255,51 do
		for green = 0,255,51 do
			for blue = 0,255,51 do
				local col = i % columns
				local row = math.floor(i / columns)
				if row % 2 == 0 then row = row / 2 else row = math.floor(row / 2) + 6 end
				newColor:set(red / 255, green / 255, blue / 255, 1.0)
				if col == columns-1 and row == rows-1 then
					-- Pure white
					newColor:set(1.0, 1.0, 1.0, 1.0)
				elseif HSBFactor then
					newColor:changeHSBValue(HSBFactor.h, HSBFactor.s, HSBFactor.b);
				end
--				o.colors[col + row * columns + 1] = { r = red/255, g = green/255, b = blue/255 }
				o.colors[col + row * columns + 1] = { r = newColor:getRedFloat(), g = newColor:getGreenFloat(), b = newColor:getBlueFloat() }
				i = i + 1
			end
		end
	end

	return o
end
