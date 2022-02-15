---@class ISResizableButton : ISButton
ISResizableButton = ISButton:derive("ISResizableButton")

function ISResizableButton:onMouseDown(x, y)
	if self.resizeLeft and self.mouseOverResize then
		self.resizing = true
		self:setCapture(true)
		return
	end
	if not self.resizeLeft and self.mouseOverResize then
		self.resizing = true
		self:setCapture(true)
		return
	end
	self.resizing = false
	ISButton.onMouseDown(self, x, y)
end

function ISResizableButton:onMouseUp(x, y)
	self.resizing = false
	self:setCapture(false)
	ISButton.onMouseUp(self, x, y)
end

function ISResizableButton:onMouseUpOutside(x, y)
	self.resizing = false
	self:setCapture(false)
	ISButton.onMouseUpOutside(self, x, y)
end

function ISResizableButton:onMouseMove(dx, dy)
	if self.resizing then
		if self.resizeLeft then
			self:resize(self.width - dx)
		else
			self:resize(self.width + dx)
		end
		return
	end
	ISButton.onMouseMove(self, dx, dy)
	if self.resizeLeft and self:getMouseX() < 4 then
		self.mouseOverResize = true
		self.mouseOver = false -- hack to disable highlighting
	elseif not self.resizeLeft and self:getMouseX() >= self.width - 4 then
		self.mouseOverResize = true
		self.mouseOver = false -- hack to disable highlighting
	else
		self.mouseOverResize = false
	end
end

function ISResizableButton:onMouseMoveOutside(dx, dy)
	self.mouseOverResize = false
	if self.resizing then
		if self.resizeLeft then
			self:resize(self.width - dx)
		else
			self:resize(self.width + dx)
		end
		return
	end
	ISButton.onMouseMoveOutside(self, dx, dy)
end

function ISResizableButton:resize(width)
	width = math.max(width, self.minimumWidth)
	if self.maximumWidth then
		width = math.min(width, self.maximumWidth)
	end
	local dx = width - self.width
	if self.resizeLeft then
		self:setX(self.x - dx)
	end
	self:setWidth(width)
	if self.onresize then
		self.onresize[1](self.onresize[2], self.onresize[3])
	end
end

function ISResizableButton:new(x, y, width, height, title, clicktarget, onclick, onmousedown, allowMouseUpProcessing)
	local o = ISButton.new(self, x, y, width, height, title, clicktarget, onclick, onmousedown, allowMouseUpProcessing)
	self.resizing = false
	self.resizeLeft = false
	self.minimumWidth = getTextManager():MeasureStringX(self.font, self.title) + 4
	self.maximumWidth = nil
	self.onresize = nil
	return o
end

