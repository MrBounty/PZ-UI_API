--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanelJoypad"

---@class ISRadialMenu : ISPanelJoypad
ISRadialMenu = ISPanelJoypad:derive("ISRadialMenu")

function ISRadialMenu:instantiate()
	self.javaObject = RadialMenu.new(self.x, self.y, self.innerRadius, self.outerRadius)
	self.javaObject:setTable(self)
	self.javaObject:setAnchorLeft(self.anchorLeft)
	self.javaObject:setAnchorRight(self.anchorRight)
	self.javaObject:setAnchorTop(self.anchorTop)
	self.javaObject:setAnchorBottom(self.anchorBottom)
	self.javaObject:setForceCursorVisible(self.forceCursorVisible or false);
	for k,v in ipairs(self.slices) do
		self.javaObject:addSlice(v.text, v.texture)
	end
end

function ISRadialMenu:onMouseDown(x, y)
	if self.joyfocus then return end
	self:undisplay()
	local sliceIndex = self.javaObject:getSliceIndexFromMouse(x, y)
	local command = self:getSliceCommand(sliceIndex + 1)
	if command and command[1] then
		command[1](command[2], command[3], command[4], command[5], command[6], command[7])
	end
end

function ISRadialMenu:onMouseDownOutside(x, y)
	if self.joyfocus then return end
	self:undisplay()
end

function ISRadialMenu:clear()
	self.slices = {}
	if self.javaObject then
		self.javaObject:clear()
	end
end

function ISRadialMenu:addSlice(text, texture, command, arg1, arg2, arg3, arg4, arg5, arg6)
	local slice = {}
	slice.text = text
	slice.texture = texture
	slice.command = { command, arg1, arg2, arg3, arg4, arg5, arg6 }
	table.insert(self.slices, slice)
	if self.javaObject then
		self.javaObject:addSlice(text, texture)
	end
end

function ISRadialMenu:setSliceText(sliceIndex, text)
	if sliceIndex < 1 or sliceIndex > #self.slices then return end
	self.slices[sliceIndex].text = text
	if self.javaObject then
		self.javaObject:setSliceText(sliceIndex - 1, text)
	end
end

function ISRadialMenu:setSliceTexture(sliceIndex, texture)
	if sliceIndex < 1 or sliceIndex > #self.slices then return end
	self.slices[sliceIndex].texture = texture
	if self.javaObject then
		self.javaObject:setSliceTexture(sliceIndex - 1, texture)
	end
end

function ISRadialMenu:getSliceCommand(sliceIndex)
	if sliceIndex < 1 or sliceIndex > #self.slices then
		return nil
	end
	return self.slices[sliceIndex].command
end

function ISRadialMenu:center()
	local x = getPlayerScreenLeft(self.playerNum)
	local y = getPlayerScreenTop(self.playerNum)
	local w = getPlayerScreenWidth(self.playerNum)
	local h = getPlayerScreenHeight(self.playerNum)
	
	x = x + w / 2
	y = y + h / 2
	
	self:setX(x - self:getWidth() / 2)
	self:setY(y - self:getHeight() / 2)
end

function ISRadialMenu:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData)
	self.javaObject:setJoypad(joypadData.id)
end

function ISRadialMenu:onJoypadDown(button, joypadData)
	if button == Joypad.BButton or button == Joypad.XButton then
		self:undisplay()
	end
end

function ISRadialMenu:setHideWhenButtonReleased(button)
	self.hideWhenButtonReleased = button
end

function ISRadialMenu:onJoypadButtonReleased(button, joypadData)
	if button == self.hideWhenButtonReleased then
		self:undisplay()
		local sliceIndex = self.javaObject:getSliceIndexFromJoypad(self.joyfocus.id)
		local command = self:getSliceCommand(sliceIndex + 1)
		if command and command[1] then
			command[1](command[2], command[3], command[4], command[5], command[6], command[7])
		end
	end
end

function ISRadialMenu:undisplay()
	self:removeFromUIManager()
	if self.joyfocus then
		self.joyfocus.focus = nil
	end
end

function ISRadialMenu:new(x, y, innerRadius, outerRadius, playerNum)
	local o = ISPanelJoypad.new(self, x, y, outerRadius * 2, outerRadius * 2)
	o.innerRadius = innerRadius
	o.outerRadius = outerRadius
	o.playerNum = playerNum
	o.slices = {}
	o:setForceCursorVisible(playerNum == 0)
	return o
end

