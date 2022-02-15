--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanel"

local FONT_HGT_CONSOLE = getTextManager():getFontHeight(UIFont.DebugConsole)

DebugChunkState_ObjectPickerPanel = ISPanel:derive("DebugChunkState_ObjectPickerPanel")
local ObjectPickerPanel = DebugChunkState_ObjectPickerPanel

function ObjectPickerPanel:update()
	ISPanel.update(self)
	self.lastPicked = UIManager.getLastPicked()
end

function ObjectPickerPanel:render()
	ISPanel.render(self)
	self.addLineX = 8
	self.addLineY = 4
	
	local isoObject = self.lastPicked -- UIManager.getLastPicked()
	if isoObject == nil then
		self:addLine("LastPicked = nil")
	elseif isoObject:getSprite() == nil then
		self:addLine("LastPicked.getSprite() = nil ???")
	else
		self:addLine("LastPicked = %s", tostring(isoObject:getSprite():getName() or isoObject:getSpriteName()))
	end

	local playerIndex = self.debugChunkState.gameState:fromLua0("getPlayerIndex")
	local zoom = getCore():getZoom(playerIndex)

	isoObject = IsoObjectPicker.Instance:PickCorpse(getMouseX(), getMouseY())
	self:addLine("Corpse = %s", tostring(isoObject))

	isoObject = IsoObjectPicker.Instance:PickThumpable(getMouseX(), getMouseY())
	self:addLine("Thumpable = %s", tostring(isoObject))

	isoObject = IsoObjectPicker.Instance:PickTree(getMouseX(), getMouseY())
	self:addLine("Tree = %s", tostring(isoObject))

	isoObject = IsoObjectPicker.Instance:PickVehicle(getMouseX() * zoom, getMouseY() * zoom)
	self:addLine("Vehicle = %s", tostring(isoObject))

	isoObject = IsoObjectPicker.Instance:PickWindow(getMouseX(), getMouseY())
	self:addLine("Window = %s", tostring(isoObject))

	isoObject = IsoObjectPicker.Instance:PickWindowFrame(getMouseX(), getMouseY())
	self:addLine("WindowFrame = %s", tostring(isoObject))
end

function ObjectPickerPanel:addLine(text, arg0, arg1, arg2, arg3, arg4)
	if type(arg0) == "boolean" then arg0 = tostring(arg0) end
	if type(arg1) == "boolean" then arg1 = tostring(arg1) end
	if type(arg2) == "boolean" then arg2 = tostring(arg2) end
	if type(arg3) == "boolean" then arg3 = tostring(arg3) end
	if type(arg4) == "boolean" then arg4 = tostring(arg4) end
	self:drawText(string.format(text, arg0, arg1, arg2, arg3, arg4), self.addLineX, self.addLineY, 1, 1, 1, 1, UIFont.DebugConsole)
	self.addLineY = self.addLineY + FONT_HGT_CONSOLE
end

function ObjectPickerPanel:new(x, y, width, height, debugChunkState)
	height = 4 + FONT_HGT_CONSOLE * 7 + 4
	local o = ISPanel.new(self, x, y, width, height)
	o.backgroundColor.a = 0.8
	o.debugChunkState = debugChunkState
	return o
end



