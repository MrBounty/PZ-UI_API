require "ISUI/ISUIElement"

ISTextEntryBox = ISUIElement:derive("ISTextEntryBox");


--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISTextEntryBox:initialise()
	ISUIElement.initialise(self);
end

function ISTextEntryBox:onCommandEntered()
end

function ISTextEntryBox:onTextChange()
end

function ISTextEntryBox:ignoreFirstInput()
	self.javaObject:ignoreFirstInput();
end

function ISTextEntryBox:setOnlyNumbers(onlyNumbers)
    self.javaObject:setOnlyNumbers(onlyNumbers);
end
--************************************************************************--
--** ISPanel:instantiate
--**
--************************************************************************--
function ISTextEntryBox:instantiate()
	--self:initialise();
	self.javaObject = UITextBox2.new(self.font, self.x, self.y, self.width, self.height, self.title, false);
	self.javaObject:setTable(self);
	self.javaObject:setX(self.x);
	self.javaObject:setY(self.y);
	self.javaObject:setHeight(self.height);
	self.javaObject:setWidth(self.width);
	self.javaObject:setAnchorLeft(self.anchorLeft);
	self.javaObject:setAnchorRight(self.anchorRight);
	self.javaObject:setAnchorTop(self.anchorTop);
	self.javaObject:setAnchorBottom(self.anchorBottom);
	self.javaObject:setEditable(true);
	--self.javaObject:setText(self.title);

end
function ISTextEntryBox:getText()
	return self.javaObject:getText();
end

function ISTextEntryBox:setEditable(editable)
    self.javaObject:setEditable(editable);
    if editable then
        self.borderColor = {r=0.4, g=0.4, b=0.4, a=1}
    else
        self.borderColor = {r=0.4, g=0.4, b=0.4, a=0.5}
    end
end

function ISTextEntryBox:setSelectable(enable)
	self.javaObject:setSelectable(enable)
end

function ISTextEntryBox:setMultipleLine(multiple)
    self.javaObject:setMultipleLine(multiple);
end

function ISTextEntryBox:setMaxLines(max)
    self.javaObject:setMaxLines(max);
end

function ISTextEntryBox:setClearButton(hasButton)
    self.javaObject:setClearButton(hasButton);
end

function ISTextEntryBox:setText(str)
    if not str then
        str = "";
    end
	self.javaObject:SetText(str);
	self.title = str;
end
function ISTextEntryBox:onPressDown()
end
function ISTextEntryBox:onPressUp()
end
function ISTextEntryBox:focus()
	return self.javaObject:focus();
end
function ISTextEntryBox:unfocus()
	return self.javaObject:unfocus();
end

function ISTextEntryBox:getInternalText()
	return self.javaObject:getInternalText();
end

function ISTextEntryBox:setMasked(b)
	return self.javaObject:setMasked(b);
end

function ISTextEntryBox:setMaxTextLength(length)
	self.javaObject:setMaxTextLength(length);
end

function ISTextEntryBox:setForceUpperCase(forceUpperCase)
	self.javaObject:setForceUpperCase(forceUpperCase);
end

--************************************************************************--
--** ISPanel:render
--**
--************************************************************************--
function ISTextEntryBox:prerender()

	self.fade:setFadeIn(self:isMouseOver() or self.javaObject:isFocused())
	self.fade:update()

	self:drawRectStatic(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
	if self.borderColor.a == 1 then
		local rgb = math.min(self.borderColor.r + 0.2 * self.fade:fraction(), 1.0)
		self:drawRectBorderStatic(0, 0, self.width, self.height, self.borderColor.a, rgb, rgb, rgb);
	else -- setValid(false)
		local r = math.min(self.borderColor.r + 0.2 * self.fade:fraction(), 1.0)
		self:drawRectBorderStatic(0, 0, self.width, self.height, self.borderColor.a, r, self.borderColor.g, self.borderColor.b)
	end

    if self:isMouseOver() and self.tooltip then
        local text = self.tooltip;
        if not self.tooltipUI then
            self.tooltipUI = ISToolTip:new()
            self.tooltipUI:setOwner(self)
            self.tooltipUI:setVisible(false)
        end
        if not self.tooltipUI:getIsVisible() then
            if string.contains(self.tooltip, "\n") then
                self.tooltipUI.maxLineWidth = 1000 -- don't wrap the lines
            else
                self.tooltipUI.maxLineWidth = 300
            end
            self.tooltipUI:addToUIManager()
            self.tooltipUI:setVisible(true)
            self.tooltipUI:setAlwaysOnTop(true)
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

function ISTextEntryBox:onMouseMove(dx, dy)
	self.mouseOver = true
end

function ISTextEntryBox:onMouseMoveOutside(dx, dy)
	self.mouseOver = false
end

function ISTextEntryBox:onMouseWheel(del)
	self:setYScroll(self:getYScroll() - (del*40))
	return true;
end

function ISTextEntryBox:clear()
	self.javaObject:clearInput();
end

function ISTextEntryBox:setHasFrame(hasFrame)
	self.javaObject:setHasFrame(hasFrame)
end

function ISTextEntryBox:setFrameAlpha(alpha)
	self.javaObject:setFrameAlpha(alpha);
end

function ISTextEntryBox:getFrameAlpha()
	return self.javaObject:getFrameAlpha();
end

function ISTextEntryBox:setValid(valid)
	if valid then
		self.borderColor = {r=0.4, g=0.4, b=0.4, a=1}
	else
		self.borderColor = {r=0.7, g=0.1, b=0.1, a=0.7}
	end
end

function ISTextEntryBox:setTooltip(text)
	self.tooltip = text and text:gsub("\\n", "\n") or nil
end

function ISTextEntryBox:selectAll()
	self.javaObject:selectAll()
end

--************************************************************************--
--** ISPanel:new
--**
--************************************************************************--
function ISTextEntryBox:new (title, x, y, width, height)
	local o = {}
	--o.data = {}
	o = ISUIElement:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self
	o.x = x;
	o.y = y;
	o.title = title;
	o.backgroundColor = {r=0, g=0, b=0, a=0.5};
	o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
	o.width = width;
	o.height = height;
    o.keeplog = false;
    o.logIndex = 0;
	o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;
	o.fade = UITransition.new()
	o.font = UIFont.Small
    o.currentText = title;
	return o
end

