--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanel"

---@class ISRadioButtons : ISPanel
ISRadioButtons = ISPanel:derive("ISRadioButtons")

function ISRadioButtons:render()
	local y = 0
	local totalHgt = #self.options * self.itemHgt
	y = y + (self.height - totalHgt) / 2
	local textDY = (self.itemHgt - self.fontHgt) / 2
	local boxDY = (self.itemHgt - self.boxSize) / 2
	self._textColor = self._textColor or { r = 1, g = 1, b = 1, a = 1 }
	for i,option in ipairs(self.options) do
		if self.joypadFocused and self.joypadIndex == i then
			self:drawRectBorder(self.leftMargin - 2, y + boxDY - 2, self.width + 2, self.boxSize + 4, 1.0, 0.6, 0.6, 0.6)
		end
		local r,g,b,a = 0.3, 0.3, 0.3, 1.0
		if self:isMouseOver() and (self.mouseOverIndex == i) and option.enabled then
			r,g,b,a = 0.6, 0.6, 0.6, 1.0
		elseif self.joypadFocused and self.joypadIndex == i then
			r,g,b,a = 0.6, 0.6, 0.6, 1.0
		elseif (i == self.selected) and option.enabled then
			r,g,b,a = 0.4, 0.4, 0.4, 1.0
		end
		self:drawTexture(self.textureCircle, self.leftMargin, y + boxDY, a, r, g, b)
		if self.selected == i and option.enabled then
			self:drawTexture(self.textureIndicator, self.leftMargin, y + boxDY, 1.0, 0.0, 1.0, 0.0)
		end

		local textColor = self._textColor
		self:getTextColor(i, textColor)

		local texture = option.texture
		if texture then
			local imgW = 20
			local imgH = 20
			if texture:getWidth() < 32 then
				imgW = imgW / (32 / texture:getWidth())
			end
			if texture:getHeight() < 32 then
				imgH = imgH / (32 / texture:getHeight())
			end

			self:drawTextureScaled(texture, self.leftMargin + self.boxSize + self.textGap, y + boxDY, imgW, imgH, 1, 1, 1, 1)
			self:drawText(option.text, self.leftMargin + self.boxSize + self.textGap + 25, y + textDY, textColor.r, textColor.g, textColor.b, textColor.a, self.font)
		else
			self:drawText(option.text, self.leftMargin + self.boxSize + self.textGap, y + textDY, textColor.r, textColor.g, textColor.b, textColor.a, self.font)
		end
		y = y + self.itemHgt
	end

	if self.enable and self:isMouseOver() and self.mouseOverIndex ~= -1 and self.tooltip then
		local text = self.tooltip
		if not self.tooltipUI then
			self.tooltipUI = ISToolTip:new()
			self.tooltipUI:setOwner(self)
			self.tooltipUI:setVisible(false)
			self.tooltipUI:setAlwaysOnTop(true)
		end
		if not self.tooltipUI:getIsVisible() then
			if string.contains(self.tooltip, "\n") then
				self.tooltipUI.maxLineWidth = 1000 -- don't wrap the lines
			else
				self.tooltipUI.maxLineWidth = 300
			end
			self.tooltipUI:addToUIManager()
			self.tooltipUI:setVisible(true)
		end
		self.tooltipUI.description = text
		self.tooltipUI:setX(self:getMouseX() + 23)
		self.tooltipUI:setY(self:getMouseY() + 23)
	else
		if self.tooltipUI and self.tooltipUI:getIsVisible() then
			self.tooltipUI:setVisible(false)
			self.tooltipUI:removeFromUIManager()
		end
	end
end

function ISRadioButtons:getTextColor(index, color)
	local option = self.options[index]
	if not option.enabled then
		color.r = 0.5
		color.g = 0.5
		color.b = 0.5
		color.a = self.choicesColor.a
	else
		color.r = self.choicesColor.r
		color.g = self.choicesColor.g
		color.b = self.choicesColor.b
		color.a = self.choicesColor.a
	end
end

function ISRadioButtons:onMouseUp(x, y)
	if self.enable and (self.mouseOverIndex >= 1) and (self.mouseOverIndex <= #self.options) then
		if not self.options[self.mouseOverIndex].enabled then
			return false
		end
		if self.selected == self.mouseOverIndex then
			return false
		end
		self.selected = self.mouseOverIndex
		if self.changeOptionFunc ~= nil and self.changeOptionTarget ~= nil then
			self.changeOptionFunc(self.changeOptionTarget, self, self.selected,
				self.changeOptionArgs[1], self.changeOptionArgs[2],
				self.changeOptionArgs[3], self.changeOptionArgs[4])
		elseif self.changeOptionFunc ~= nil then
			self.changeOptionFunc(self, self.selected,
				self.changeOptionArgs[1], self.changeOptionArgs[2],
				self.changeOptionArgs[3], self.changeOptionArgs[4])
		end
	end

	return false
end

function ISRadioButtons:onMouseDown(x, y)
	return false
end

function ISRadioButtons:onMouseMove(dx, dy)
	local x = self:getMouseX()
	local y = self:getMouseY()
	if (x >= 0) and (y >= 0) and (x < self.width) and (y < self.height) then
		local totalHgt = #self.options * self.itemHgt
		y = y - (self.height - totalHgt) / 2
		y = y / self.itemHgt
		y = math.floor(y + 1)
		self.mouseOverIndex = y
	else
		self.mouseOverIndex = -1
	end
end

function ISRadioButtons:onMouseMoveOutside(dx, dy)
	self.mouseOverIndex = -1
end

function ISRadioButtons:addOption(text, data, texture, enabled)
	local option = {}
	option.text = text
	option.data = data
	option.texture = texture
	if enabled ~= nil then
		option.enabled = enabled
	else
		option.enabled = true;
	end
	table.insert(self.options, option)
	if texture then
		self.itemHgt = math.max(self.boxSize, self.fontHgt, self.textureSize) + self.itemGap
	end
	self:setHeight(#self.options * self.itemHgt)
	if self.autoWidth then
		local w = self.leftMargin + self.boxSize + self.textGap + getTextManager():MeasureStringX(self.font, text)
		if texture then
			w = w + 32
		end
		if w > self:getWidth() then
			self:setWidth(w)
		end
	end
	if self.selected == -1 then
		self.selected = 1
	end
	return #self.options
end

function ISRadioButtons:checkIndex(index)
	local index = tonumber(index)
	if (not index) or (index < 1) or (index > #self.options) then error "invalid index" end
	return index
end

function ISRadioButtons:setOptionText(index, text)
	index = self:checkIndex(index)
	self.options[index].text = text
end

function ISRadioButtons:getOptionText(index)
	index = self:checkIndex(index)
	return self.options[index].text
end

function ISRadioButtons:setOptionData(index, data)
	index = self:checkIndex(index)
	self.options[index].data = data
end

function ISRadioButtons:getOptionData(index)
	index = self:checkIndex(index)
	return self.options[index].data
end

function ISRadioButtons:setOptionTexture(index, texture)
	index = self:checkIndex(index)
	self.options[index].texture = texture
end

function ISRadioButtons:getOptionTexture(index)
	index = self:checkIndex(index)
	return self.options[index].texture
end

function ISRadioButtons:setOptionEnabled(index, enabled)
	index = self:checkIndex(index)
	self.options[index].enabled = enabled
end

function ISRadioButtons:isOptionEnabled(index)
	index = self:checkIndex(index)
	return self.options[index].enabled
end

function ISRadioButtons:clear()
	table.wipe(self.options)
	self.selected = -1
	self.mouseOverIndex = -1
	self.joypadIndex = 1
	self:setHeight(0)
end

function ISRadioButtons:isEmpty()
	return #self.options == 0
end

function ISRadioButtons:getNumOptions()
	return #self.options
end

function ISRadioButtons:setFont(font)
	self.font = font
	self.fontHgt = getTextManager():getFontFromEnum(self.font):getLineHeight()
end

function ISRadioButtons:setWidthToFit()
	local textX = self.leftMargin + self.boxSize + self.textGap
	local maxWid = 0
	for _,option in ipairs(self.options) do
		local wid = textX + getTextManager():MeasureStringX(self.font, option.text)
		if option.texture then
			wid = wid + 32
		end
		maxWid = math.max(maxWid, wid)
	end
	self:setWidth(maxWid)
end

function ISRadioButtons:setSelected(index)
	index = self:checkIndex(index)
	self.selected = index
end

function ISRadioButtons:isSelected(index)
	index = self:checkIndex(index)
	return self.selected == index
end

function ISRadioButtons:setJoypadFocused(focused)
	self.joypadFocused = focused
end

function ISRadioButtons:onJoypadDirUp(joypadData)
	self.joypadIndex = self.joypadIndex - 1
	if self.joypadIndex < 1 then
		self.joypadIndex = #self.options
	end
end

function ISRadioButtons:onJoypadDirDown(joypadData)
	self.joypadIndex = self.joypadIndex + 1
	if self.joypadIndex > #self.options then
		self.joypadIndex = 1
	end
end

function ISRadioButtons:forceClick()
	if not self.options[self.joypadIndex].enabled then
		return
	end
	self.selected = self.joypadIndex
	if self.changeOptionFunc ~= nil and self.changeOptionTarget ~= nil then
		self.changeOptionFunc(self.changeOptionTarget, self, self.selected,
			self.changeOptionArgs[1], self.changeOptionArgs[2],
			self.changeOptionArgs[3], self.changeOptionArgs[4])
	elseif self.changeOptionFunc ~= nil then
		self.changeOptionFunc(self, self.selected,
			self.changeOptionArgs[1], self.changeOptionArgs[2],
			self.changeOptionArgs[3], self.changeOptionArgs[4])
	end
end

function ISRadioButtons:new (x, y, width, height, target, changeOptionFunc, arg1, arg2, arg3, arg4)
	local o = ISPanel.new(self, x, y, width, height)
	o:noBackground()
	o.textureCircle = getTexture("media/ui/RadioButtonCircle.png")
	o.textureIndicator = getTexture("media/ui/RadioButtonIndicator.png")
	o.choicesColor = {r=0.7, g=0.7, b=0.7, a=1}
	o.anchorLeft = true
	o.anchorRight = false
	o.anchorTop = true
	o.anchorBottom = false
	o.options = {}
	o.leftMargin = 0
	o.boxSize = 16
	o.textGap = 4
	o.textureSize = 20
	o.font = UIFont.Small
	o.fontHgt = getTextManager():getFontHeight(o.font)
	o.itemGap = 4
	o.itemHgt = math.max(o.boxSize, o.fontHgt) + o.itemGap
	o.isRadioButtons = true
	o.tooltip = nil
	o.joypadIndex = 1
	o.changeOptionFunc = changeOptionFunc
	o.changeOptionTarget = target
	o.changeOptionArgs = { arg1, arg2, arg3, arg4 }
	o.enable = true
	o.autoWidth = false
	o.selected = -1
	o.mouseOverIndex = -1
	return o
end

