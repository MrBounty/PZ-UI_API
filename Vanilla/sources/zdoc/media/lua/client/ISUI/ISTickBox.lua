require "ISUI/ISPanel"

---@class ISTickBox : ISPanel
ISTickBox = ISPanel:derive("ISTickBox");

--************************************************************************--
--** ISRadioOption:initialise
--**
--************************************************************************--

function ISTickBox:initialise()
	ISPanel.initialise(self);
end



--************************************************************************--
--** ISRadioOption:render
--**
--************************************************************************--
function ISTickBox:prerender()
	if self.background then
		self:drawRectStatic(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
	end
end

function ISTickBox:setJoypadFocused(focused)
    self.joypadFocused = focused;
end

function ISTickBox:onJoypadDirUp(joypadData)
	self.joypadIndex = self.joypadIndex - 1
	if self.joypadIndex < 1 then
		self.joypadIndex = #self.options
	end
end

function ISTickBox:onJoypadDirDown(joypadData)
	self.joypadIndex = self.joypadIndex + 1
	if self.joypadIndex > #self.options then
		self.joypadIndex = 1
	end
end

function ISTickBox:forceClick()
    if self.disabledOptions[self.optionsIndex[self.joypadIndex]] then
        return
    end
    if self.onlyOnePossibility then
        self.selected = {}
    end

    getSoundManager():playUISound("UIToggleTickBox")
    self.selected[self.joypadIndex] = not self.selected[self.joypadIndex];

    if self.changeOptionMethod ~= nil then
        self.changeOptionMethod(self.changeOptionTarget, self.joypadIndex, self.selected[self.joypadIndex],
            self.changeOptionArgs[1], self.changeOptionArgs[2], self);
    end
end

function ISTickBox:setSelected(index, selected)
	local index = tonumber(index)
	if not index or index < 1 or index > #self.options then error "invalid index" end
	self.selected[index] = selected
end

function ISTickBox:isSelected(index)
	local index = tonumber(index)
	if not index or index < 1 or index > #self.options then error "invalid index" end
	return self.selected[index] == true
end

--************************************************************************--
--** ISRadioOption:render
--**
--************************************************************************--
function ISTickBox:render()
	local y = 0;
	local c = 1;
	local totalHgt = #self.options * self.itemHgt
	y = y + (self.height - totalHgt) / 2
	local textDY = (self.itemHgt - self.fontHgt) / 2
	local boxDY = (self.itemHgt - self.boxSize) / 2
	self._textColor = self._textColor or { r = 1, g = 1, b = 1, a = 1 }
    for i,v in ipairs(self.options) do
		local disabled = false;
		if self.disabledOptions[v] then
			disabled = true;
		end
		if self:isMouseOver() and (self.mouseOverOption == c) and not self.disabledOptions[self.optionsIndex[self.mouseOverOption]] then
            self:drawRect(self.leftMargin, y+boxDY, self.boxSize, self.boxSize, 1.0, 0.3, 0.3, 0.3);
		else
			self:drawRectBorder(self.leftMargin, y+boxDY, self.boxSize, self.boxSize, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
        end

        if self.joypadFocused and self.joypadIndex == c then
            self:drawRectBorder(self.leftMargin - 2, y+boxDY - 2, self.width + 2, self.boxSize + 4, 1.0, 0.6, 0.6, 0.6);
            self:drawRect(self.leftMargin, y+boxDY, self.boxSize, self.boxSize, 1.0, 0.3, 0.3, 0.3);
        end

      	if self.selected[c] == true then
			self:drawTexture(self.tickTexture, self.leftMargin + 3, y+boxDY+2, 1, 1, 1, 1);
		end

		local textColor = self._textColor
		self:getTextColor(i, textColor)

        if self.textures[i] then
            local imgW = 20;
            local imgH = 20;
            if self.textures[i]:getWidth() < 32 then
                imgW = imgW / (32/self.textures[i]:getWidth());
            end
            if self.textures[i]:getHeight() < 32 then
                imgH = imgH / (32/self.textures[i]:getHeight());
            end

            self:drawTextureScaled(self.textures[i], self.leftMargin + self.boxSize + self.textGap, y+boxDY, imgW,imgH, 1, 1, 1, 1);
			self:drawText(v, self.leftMargin + self.boxSize + self.textGap + 25, y+textDY, textColor.r, textColor.g, textColor.b, textColor.a, self.font);
		else
			self:drawText(v, self.leftMargin + self.boxSize + self.textGap, y+textDY, textColor.r, textColor.g, textColor.b, textColor.a, self.font);
        end
		y = y + self.itemHgt;
		c = c + 1;
    end

    if self.enable and self:isMouseOver() and self.mouseOverOption and self.mouseOverOption ~= 0 and self.tooltip then
        local text = self.tooltip;
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

function ISTickBox:getTextColor(index, color)
	local text = self.optionsIndex[index]
	if self.disabledOptions[text] then
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

--************************************************************************--
--** ISTickBox:onMouseUp
--**
--************************************************************************--
function ISTickBox:onMouseUp(x, y)
	if self.enable and self.mouseOverOption ~= nil and self.mouseOverOption > 0 and self.mouseOverOption < self.optionCount then
		if self.disabledOptions[self.optionsIndex[self.mouseOverOption]] then
			return;
		end
		getSoundManager():playUISound("UIToggleTickBox")
        if self.onlyOnePossibility then
           self.selected = {};
        end
		if self.selected[self.mouseOverOption] == nil then
			self.selected[self.mouseOverOption] = true;
		else
			self.selected[self.mouseOverOption] = not self.selected[self.mouseOverOption];
		end
        if self.changeOptionMethod ~= nil then
            self.changeOptionMethod(self.changeOptionTarget, self.mouseOverOption, self.selected[self.mouseOverOption],
                self.changeOptionArgs[1], self.changeOptionArgs[2], self);
        end
	end

	return false;
end
function ISTickBox:onMouseDown(x, y)

	return false;
end

--************************************************************************--
--** ISTickBox:onMouseMove
--**
--************************************************************************--
function ISTickBox:onMouseMove(dx, dy)
	local x = self:getMouseX();
	local y = self:getMouseY();
	if x >= 0 and y >= 0 and x<=self.width and y <= self.height then
		local totalHgt = #self.options * self.itemHgt
		y = y - (self.height - totalHgt) / 2
		y = y / self.itemHgt;
		y = math.floor(y + 1);
		self.mouseOverOption = y;
--        print(self.mouseOverOption);
	else
		self.mouseOverOption = 0;
    end


end

--************************************************************************--
--** ISRadioOption:onMouseMoveOutside
--**
--************************************************************************--
function ISTickBox:onMouseMoveOutside(dx, dy)
	self.mouseOverOption = 0;
end

function ISTickBox:disableOption(name, disable)
	self.disabledOptions[name] = disable;
end

function ISTickBox:clearOptions()
	self.disabledOptions = {};
	self.selected = {}
	self.options = {};
	self.optionsIndex = {};
	self.textures = {};
	self.optionData = {};
	self.optionCount = 1;
end

function ISTickBox:addOption(name, data, texture)

	table.insert(self.options, name);
    self.textures[self.optionCount] = texture;
	self.optionData[self.optionCount] = data;
	self.optionsIndex[self.optionCount] =  name;
	self.optionCount = self.optionCount + 1;
	self:setHeight(#self.options * self.itemHgt);
	if self.autoWidth then
		local w = self.leftMargin + self.boxSize + self.textGap + getTextManager():MeasureStringX(self.font, name)
		if texture then
			w = w + 32;
		end
		if w > self:getWidth() then
			self:setWidth(w)
		end
	end
	return self.optionCount - 1;
end

function ISTickBox:getOptionCount()
	return self.optionCount - 1;
end

function ISTickBox:getOptionData(index)
	if index < 1 or index > self:getOptionCount() then
		error "invalid index"
	end
	return self.optionData[index];
end

function ISTickBox:setFont(font)
	self.font = font
	self.fontHgt = getTextManager():getFontFromEnum(self.font):getLineHeight()
end

function ISTickBox:setWidthToFit()
	local textX = self.leftMargin + self.boxSize + self.textGap
	local maxWid = 0
	for i,option in ipairs(self.options) do
		local wid = textX + getTextManager():MeasureStringX(self.font, option)
		if self.textures[i] then
			wid = wid + 32
		end
		maxWid = math.max(maxWid, wid)
	end
	self:setWidth(maxWid)
end

function ISTickBox:new (x, y, width, height, name, changeOptionTarget, changeOptionMethod, changeOptionArg1, changeOptionArg2)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.x = x;
	o.y = y;
	o.width = width;
	o.height = height;
	o.tickTexture = getTexture("Quest_Succeed");
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
	o.optionData = {}
	o.selected = {}
	o.leftMargin = 0;
	o.boxSize = 16
	o.textGap = 4;
    o.textures = {};
	o.font = UIFont.Small
    o.fontHgt = getTextManager():getFontHeight(o.font)
	o.itemGap = 4
	o.itemHgt = math.max(o.boxSize, o.fontHgt) + o.itemGap
    o.isTickBox = true;
    o.tooltip = nil;
	o.joypadIndex = 1;
	o.changeOptionMethod = changeOptionMethod;
	o.changeOptionTarget = changeOptionTarget;
	o.changeOptionArgs = { changeOptionArg1, changeOptionArg2 }
	o.enable = true;
	o.disabledOptions = {};
	o.optionsIndex = {};
	return o
end

