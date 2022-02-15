--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISBaseObject"

DebugGlobalObjectStateUI_Tool = ISBaseObject:derive("DebugGlobalObjectStateUI_Tool")
local Tool = DebugGlobalObjectStateUI_Tool

function Tool:onMouseDown(x, y)
end

function Tool:onMouseMove(dx, dy)
end

function Tool:onMouseUp(x, y)
end

function Tool:java0(func)
	return self.ui.gameState:fromLua0(func)
end

function Tool:java1(func, arg0)
	return self.ui.gameState:fromLua1(func, arg0)
end

function Tool:java2(func, arg0, arg1)
	return self.ui.gameState:fromLua2(func, arg0, arg1)
end

function Tool:new(ui)
	local o = ISBaseObject.new(self)
	o.ui = ui
	return o
end

-----

DebugGlobalObjectStateUI_DragCameraTool = Tool:derive("DebugGlobalObjectStateUI_DragCameraTool")
local DragCameraTool = DebugGlobalObjectStateUI_DragCameraTool

function DragCameraTool:onMouseDown(x, y)
	self.mouseDown = true
	self.startDragX = self:java0("getCameraDragX")
	self.startDragY = self:java0("getCameraDragY")
	self.startScreenX = x
	self.startScreenY = y
end

function DragCameraTool:onMouseMove(dx, dy)
	if self.mouseDown then
		local playerIndex = self:java0("getPlayerIndex")
		local worldX1 = screenToIsoX(playerIndex, self.startScreenX, self.startScreenY, 0)
		local worldY1 = screenToIsoY(playerIndex, self.startScreenX, self.startScreenY, 0)
		local screenX = self.ui:getMouseX()
		local screenY = self.ui:getMouseY()
		local worldX2 = screenToIsoX(playerIndex, screenX, screenY, 0)
		local worldY2 = screenToIsoY(playerIndex, screenX, screenY, 0)
		self:java2("dragCamera", self.startDragX + worldX2 - worldX1, self.startDragY + worldY2 - worldY1)
	end
end

function DragCameraTool:onMouseUp(x, y)
	self.mouseDown = false
end

function DragCameraTool:new(ui)
	local o = Tool.new(self, ui)
	return o
end

