--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISUIElement"

---@class ISUI3DScene : ISUIElement
ISUI3DScene = ISUIElement:derive("ISUI3DScene")

function ISUI3DScene:instantiate()
	self.javaObject = UI3DScene.new(self)
	self.javaObject:setX(self.x)
	self.javaObject:setY(self.y)
	self.javaObject:setWidth(self.width)
	self.javaObject:setHeight(self.height)
	self.javaObject:setAnchorLeft(self.anchorLeft)
	self.javaObject:setAnchorRight(self.anchorRight)
	self.javaObject:setAnchorTop(self.anchorTop)
	self.javaObject:setAnchorBottom(self.anchorBottom)
end

function ISUI3DScene:prerender()
	if self.background then
		self:drawRectStatic(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b)
		self:drawRectBorderStatic(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b)
	end
end

function ISUI3DScene:onMouseDown(x, y)
	self.mouseDown = true
end

function ISUI3DScene:onMouseMove(dx, dy)
	if self.mouseDown then
		self.javaObject:fromLua2("dragView", dx, dy)
	end
end

function ISUI3DScene:onMouseUp(x, y)
	self.mouseDown = false
end

function ISUI3DScene:onMouseUpOutside(x, y)
	self.mouseDown = false
end

function ISUI3DScene:onMouseWheel(del)
	self.javaObject:fromLua1("zoom", del)
	return true
end

function ISUI3DScene:setView(name)
	self.javaObject:fromLua1("setView", name)
end

function ISUI3DScene:getView()
	return self.javaObject:fromLua0("getView")
end

function ISUI3DScene:new(x, y, width, height)
	local o = ISUIElement.new(self, x, y, width, height)
	o.background = true
	o.backgroundColor = {r=0.25, g=0.25, b=0.25, a=1}
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1}
	return o
end

