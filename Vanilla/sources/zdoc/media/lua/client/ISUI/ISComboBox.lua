--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanel"
require "ISUI/ISScrollingListBox"
require "ISUI/ISTextEntryBox"

---@class ISComboBox : ISPanel
ISComboBox = ISPanel:derive("ISComboBox");
ISComboBoxEditor = ISTextEntryBox:derive("ISComboBoxEditor");
ISComboBoxPopup = ISScrollingListBox:derive("ISComboBoxPopup");

-----

function ISComboBoxEditor:new(x, y, width, height, comboBox)
    local o = ISTextEntryBox.new(self, "", x, y, width, height)
    o.borderColor.a = 0.0
    o.backgroundColor.a = 0.0
    o.parentCombo = comboBox
    return o
end

function ISComboBoxEditor:onTextChange()
    local filterText = self:getInternalText()
    self.parentCombo:setFilterText(filterText)
end

function ISComboBoxEditor:onOtherKey(key)
    if key == Keyboard.KEY_ESCAPE then
        self.parentCombo.expanded = false
        self.parentCombo:hidePopup()
    end
end

-----

function ISComboBoxPopup:new(x, y, width, height)
    local o = ISScrollingListBox:new(x, y, width, height)
    setmetatable(o, self)
    self.__index = self
    self.parentCombo = nil
    return o
end

function ISComboBoxPopup:prerender()
    if not self.parentCombo:isReallyVisible() then
        -- Hack for gamepad being disconnected
        self:removeFromUIManager()
        return
    end
    self.tooWide = nil
    
    local numVisible = self:size()
    if self.parentCombo:hasFilterText() then
        local filterText = self.parentCombo:getFilterText():lower()
        for i=1,self:size() do
            local text = self.items[i].text:lower()
            if not text:contains(filterText) then
                numVisible = numVisible - 1
            end
        end
    end
--    numVisible = math.max(numVisible, 1)
    self:setScrollHeight(numVisible * self.itemheight)
    self:setHeight(math.min(numVisible, 8) * self.itemheight)
    self.vscroll:setHeight(self.height)

    ISScrollingListBox.prerender(self)
end

function ISComboBoxPopup:render()
    ISScrollingListBox.render(self)
    self:drawRectBorderStatic(-1, -1, self:getWidth() + 2, self:getHeight() + 2, 0.8, 1, 1, 1)

    if self.tooWide then
        local item = self.tooWide
        local y = self.tooWideY
        local textWid = getTextManager():MeasureStringX(self.font, item.text)
        local selectColor = self.parentCombo.backgroundColorMouseOver
        self:drawRect(0, y, 10 + textWid + 8, item.height-1, selectColor.a, selectColor.r, selectColor.g, selectColor.b)
        local itemPadY = self.itemPadY or (item.height - self.fontHgt) / 2
        self:drawText(item.text, 10, y + itemPadY, self.parentCombo.textColor.r, self.parentCombo.textColor.g, self.parentCombo.textColor.b, self.parentCombo.textColor.a, self.font)
    end
end

function ISComboBoxPopup:doDrawItem(y, item, alt)
    if self.parentCombo:hasFilterText() then
        if not item.text:lower():contains(self.parentCombo:getFilterText():lower()) then
            return y
        end
    end
    if item.height == 0 then
        item.height = self.itemheight
    end
    local highlight = (self:isMouseOver() and not self:isMouseOverScrollBar()) and self.mouseoverselected or self.selected
    if self.parentCombo.joypadFocused then
        highlight = self.selected
    end
    if highlight == item.index then
        local selectColor = self.parentCombo.backgroundColorMouseOver
        self:drawRect(0, (y), self:getWidth(), item.height-1, selectColor.a, selectColor.r, selectColor.g, selectColor.b)

        if self:isMouseOver() and not self:isMouseOverScrollBar() then
            local textWid = getTextManager():MeasureStringX(self.font, item.text)
            local scrollBarWid = self:isVScrollBarVisible() and 13 or 0
            if 10 + textWid > self.width - scrollBarWid then
                self.tooWide = item
                self.tooWideY = y
            end
        end
    end
    local itemPadY = self.itemPadY or (item.height - self.fontHgt) / 2
    self:drawText(item.text, 10, y + itemPadY, self.parentCombo.textColor.r, self.parentCombo.textColor.g, self.parentCombo.textColor.b, self.parentCombo.textColor.a, self.font)
    y = y + item.height
    return y
end

function ISComboBoxPopup:onMouseDown(x, y)
	if self.parentCombo.disabled then return false; end
    if not self:isMouseOver() then -- due to setCapture()
        self.parentCombo.expanded = false
        self.parentCombo:hidePopup()
        return
    end
    return true
end

function ISComboBoxPopup:onMouseUp(x, y)
	if self.parentCombo.disabled then return; end
    if self.vscroll then
        self.vscroll.scrolling = false
    end
    if not self:isMouseOver() then -- due to setCapture()
        self.parentCombo.expanded = false
        self.parentCombo:hidePopup()
        return
    end
    if not self.joypadFocused then
        local row = self:rowAt(x, y)
        if row > #self.items then
            row = #self.items
        elseif row < 1 then
            row = 1
        end
        self.parentCombo.selected = row
        self.parentCombo.expanded = false
        self.parentCombo:hidePopup()
        if self.parentCombo.onChange then
            self.parentCombo.onChange(self.parentCombo.target, self.parentCombo, self.parentCombo.onChangeArgs[1], self.parentCombo.onChangeArgs[2])
        end
    end
end

function ISComboBoxPopup:setComboBox(comboBox)
    self:clear()
    for i=1,#comboBox.options do
        self:addItem(comboBox:getOptionText(i), nil)
    end
    self:setYScroll(0)
    self.selected = comboBox.selected
    self:setHeight(math.min(#comboBox.options, 8) * self.itemheight)
    
    self:setX(comboBox:getAbsoluteX())
    self:setWidth(comboBox:getWidth())
    if comboBox.openUpwards or (comboBox:getAbsoluteY() + comboBox:getHeight() + self:getHeight() > getCore():getScreenHeight()) then
        self:setY(comboBox:getAbsoluteY() - self:getHeight())
    else
        self:setY(comboBox:getAbsoluteY() + comboBox:getHeight())
    end
    self.borderColor = { r = comboBox.borderColor.r, g = comboBox.borderColor.g, b = comboBox.borderColor.b, a = comboBox.borderColor.a }
    self.backgroundColor = { r = comboBox.backgroundColor.r, g = comboBox.backgroundColor.g, b = comboBox.backgroundColor.b, a = comboBox.backgroundColor.a }
    self.parentCombo = comboBox
    self:ensureVisible(self.selected)
end

--************************************************************************--
--** ISComboBox:initialise
--**
--************************************************************************--

function ISComboBox:initialise()
	ISPanel.initialise(self);
end

function ISComboBox:createChildren()
    if ISComboBox.SharedPopup then
        self.popup = ISComboBox.SharedPopup
    else
        self.popup = ISComboBoxPopup:new(0, 0, 100, 50)
        self.popup:initialise()
        self.popup:instantiate()
        self.popup:setFont(self.font, 4)
        self.popup:setAlwaysOnTop(true)
        self.popup.drawBorder = true
        self.popup:setCapture(true)
        ISComboBox.SharedPopup = self.popup
    end
end

function ISComboBox:showPopup()
    getSoundManager():playUISound("UIToggleComboBox")
    self.popup:setFont(self.font, 4)
    self.popup:setComboBox(self)
    self.popup:addToUIManager()
end

function ISComboBox:hidePopup()
    getSoundManager():playUISound("UIToggleComboBox")
    self.popup:removeFromUIManager()
    if self.editor and self.editor:isVisible() then
        self.filterText = nil
        self.editor:unfocus()
        self.editor:setVisible(false)
    end
end

function ISComboBox:setJoypadFocused(focused)
    self.joypadFocused = focused;
    if self.expanded and not focused then
        self:hidePopup()
        self.expanded = false
    end
end

function ISComboBox:onJoypadDirUp(joypadData)
    self.popup:onJoypadDirUp(joypadData)
end

function ISComboBox:onJoypadDirDown(joypadData)
    self.popup:onJoypadDirDown(joypadData)
end

function ISComboBox:forceClick()
    if self.disabled then
        if self.expanded then
            self:hidePopup();
        end
        self.expanded = false;
        return;
    end
    self.expanded = not self.expanded;
    if self.expanded then
        self:showPopup()
--        self.joypadIndexFocused = self.selected
    else
        self:hidePopup()
    end
    if not self.expanded then
        self.selected = self.popup.selected;
        if self.onChange then
            self.onChange(self.target, self, self.onChangeArgs[1], self.onChangeArgs[2]);
        end
    end
end

function ISComboBox:prerender()
	if not self.disabled then
		self.fade:setFadeIn(self.joypadFocused or self:isMouseOver())
		self.fade:update()
	end

	self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);

    if self.expanded then
    elseif not self.joypadFocused then
        self:drawRect(0, 0, self.width, self.height, self.backgroundColorMouseOver.a * 0.5 * self.fade:fraction(), self.backgroundColorMouseOver.r, self.backgroundColorMouseOver.g, self.backgroundColorMouseOver.b);
	else
        self:drawRect(0, 0, self.width, self.height, self.backgroundColorMouseOver.a, self.backgroundColorMouseOver.r, self.backgroundColorMouseOver.g, self.backgroundColorMouseOver.b);
    end
    local alpha = math.min(self.borderColor.a + 0.2 * self.fade:fraction(), 1.0)
	if not self.disabled then
		self:drawRectBorder(0, 0, self.width, self.height, alpha, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	else
		self:drawRectBorder(0, 0, self.width, self.height, alpha, 0.5, 0.5, 0.5);
	end

	local fontHgt = getTextManager():getFontHeight(self.font)
	local y = (self.height - fontHgt) / 2
	if self:isEditable() and self.editor and self.editor:isReallyVisible() then
		-- editor is visible, don't draw text
	elseif self.options[self.selected] then
		self:clampStencilRectToParent(0, 0, self.width - self.image:getWidthOrig() - 6, self.height)
		if not self.disabled then
			self:drawText(self:getOptionText(self.selected), 10, y, self.textColor.r, self.textColor.g, self.textColor.b, self.textColor.a, self.font);
		else
			self:drawText(self:getOptionText(self.selected), 10, y, 0.6, 0.6, 0.6, 1, self.font);
		end
		self:clearStencilRect()
	elseif self.noSelectionText then
		self:clampStencilRectToParent(0, 0, self.width - self.image:getWidthOrig() - 6, self.height)
		self:drawText(self.noSelectionText, 10, y, self.textColor.r, self.textColor.g, self.textColor.b, self.textColor.a, self.font);
		self:clearStencilRect()
	end

	if self:isMouseOver() and not self.expanded and self:getOptionTooltip(self.selected) then
		local text = self:getOptionTooltip(self.selected)
		if not self.tooltipUI then
			self.tooltipUI = ISToolTip:new()
			self.tooltipUI:setOwner(self)
			self.tooltipUI:setVisible(false)
			self.tooltipUI:setAlwaysOnTop(true)
		end
		if not self.tooltipUI:getIsVisible() then
			if string.contains(text, "\n") then
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

	if not self.disabled then
    	self:drawTexture(self.image, self.width - self.image:getWidthOrig() - 3, (self.baseHeight / 2) - (self.image:getHeight() / 2), 1, 1, 1, 1)
	else
		self:drawTexture(self.image, self.width - self.image:getWidthOrig() - 3, (self.baseHeight / 2) - (self.image:getHeight() / 2), 1, 0.5, 0.5, 0.5)
	end
end

function ISComboBox:onMouseDown(x, y)
	if self.disabled then return false; end
    self.sawMouseDown = true
	return true
end

--************************************************************************--
--** ISComboBox:onMouseUp
--**
--************************************************************************--
function ISComboBox:onMouseUp(x, y)
    if self.disabled or not self.sawMouseDown then return end
    self.sawMouseDown = false
    self.expanded = not self.expanded;
    if self:isEditable() then
        if not self.editor then
            local inset = 2 -- UITextBox2.getInset()
            local editorX = 10 - 2
            local textY = (self.height - getTextManager():getFontHeight(self.font)) / 2
            local editorY = textY - inset
            self.editor = ISComboBoxEditor:new(editorX, editorY, self.width - self.image:getWidthOrig() - 3, self.height, self)
            self.editor:initialise()
            self.editor:instantiate()
            self:addChild(self.editor)
        end
        self.editor:setWidth(self.width - self.image:getWidthOrig() - 3)
        if self.options[self.selected] then
            self.editor:setText(self:getOptionText(self.selected))
            self.editor:selectAll()
        else
            self.editor:setText("")
        end
        self.editor:setVisible(true)
        self.editor:focus()
    end
    if self.expanded then
        self:showPopup();
    else
        self:hidePopup();
        self.mouseOver = self:isMouseOver()
    end
end
function ISComboBox:onMouseDownOutside(x, y)
    self.sawMouseDown = false
	if self.expanded then
		self.expanded = false;
		self:hidePopup()
	end
end
function ISComboBox:onMouseMove(dx, dy)
	self.mouseOver = true;
end

function ISComboBox:select(option)
	for i=1,#self.options do
   	    if option == self:getOptionText(i) then
	       self.selected = i;
	       return;
		end
	end
end

function ISComboBox:selectData(data)
	for i=1,#self.options do
		if data == self:getOptionData(i) then
			self.selected = i;
			return;
		end
	end
end

function ISComboBox:find(func, arg1, arg2)
	for i=1,#self.options do
		if func(self:getOptionText(i), self:getOptionData(i), arg1, arg2) then
			return i
		end
	end
	return -1
end

function ISComboBox:addOption(option)

	table.insert(self.options, option);

	if self.selected == 0 then
		for i, k in ipairs(self.options) do
			self.selected = i;
			return;
		end
	end
end

function ISComboBox:addOptionWithData(option, data)
	local item = {}
	item.text = option
	item.data = data
	table.insert(self.options, item);
	if self.selected == 0 then
		self.selected = 1
	end
end

function ISComboBox:contains(text)
	for i, k in ipairs(self.options) do
		if k.text == text then
			return true;
		end
	end
	return false;
end

function ISComboBox:getOptionText(index)
	local option = self.options[index]
	if option then
		if type(option) == "table" then
			return option.text
		else
			return option
		end
	end
	return nil
end

function ISComboBox:getOptionData(index)
	local option = self.options[index]
	if type(option) == "table" then
		return option.data
	end
	return nil
end

function ISComboBox:getOptionTooltip(index)
	local option = self.options[index]
	if option then
		if type(option) == "table" then
			if option.tooltip then
				return option.tooltip
			end
			if self.tooltip then
				return self.tooltip[option.text] or self.tooltip["defaultTooltip"]
			end
		else
			if self.tooltip then
				return self.tooltip[option] or self.tooltip["defaultTooltip"]
			end
		end
	end
	return nil
end

function ISComboBox:onMouseMoveOutside(dx, dy)
	self.mouseOver = false;
end

function ISComboBox:setToolTipMap(tooltipmap)
	self.tooltip = tooltipmap;
end

function ISComboBox:getSelectedText()
	return self:getOptionText(self.selected);
end

function ISComboBox:setWidthToOptions(minWidth)
	local width = 0
	for i=1,#self.options do
		local text = self:getOptionText(i)
		width = math.max(width, getTextManager():MeasureStringX(self.font, text or ""))
	end
	width = 10 + width + 5 + self.image:getWidthOrig() + 3
	self:setWidth(math.max(width, minWidth or 0))
end

function ISComboBox:clear()
	self.options = {}
    self.tooltip = {}
end

function ISComboBox:setEditable(editable)
	self.editable = editable
end

function ISComboBox:isEditable()
	return self.editable == true
end

function ISComboBox:setFilterText(text)
    text = text and text:trim() or nil
    if text == "" then text = nil end
    self.filterText = text
end

function ISComboBox:getFilterText()
    return self:isEditable() and self.filterText or nil
end

function ISComboBox:hasFilterText()
    local filterText = self:getFilterText()
    return (filterText ~= nil) and (self.filterText:trim() ~= "")
end

function ISComboBox:new (x, y, width, height, target, onChange, onChangeArg1, onChangeArg2)
	local o = {}
	--o.data = {}
	o = ISPanel:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self
	o.x = x;
	o.y = y;
	o.backgroundColor = {r=0, g=0, b=0, a=1};
	o.backgroundColorMouseOver = {r=0.3, g=0.3, b=0.3, a=1.0};
	o.borderColor = {r=1, g=1, b=1, a=0.5};
	o.font = UIFont.Small
    o.textColor = {r=0.9, g=0.9, b=0.9, a=0.9};
	o.width = width;
	o.height = height;
    o.baseHeight = height;
	o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;
	o.target = target;
	o.onChange = onChange;
	o.onChangeArgs = { onChangeArg1, onChangeArg2 }
	o.options = {};
	o.selected = 0;
	o.expanded = false;
	o.mouseOver = false;
	o.tooltip = nil;
    o.isCombobox = true;
	o.disabled = false;
--    o.joypadIndexFocused = 1;
    o.image = getTexture("media/ui/ArrowDown.png");
    o.fade = UITransition.new()
	o.editable = false
	return o
end



-- Event method to check game mode and create K&B tutorial panel if necessary.
testcomboBox = function ()

	--print("start testing combobox");
	local panel2 = ISComboBox:new(150, 0, 100, 18, nil);
	--print("panel2 newed");

	panel2:initialise();
	--print("panel2 initialised");
	panel2:addOption("1024x768");
	panel2:addOption("1920x1080");
	panel2:addToUIManager();
	--print("panel2 added ToUIManager");

--panel2 = ISInventoryPage:new(300, 300, 400+32, 400, getPlayer():getInventory());
--panel2:initialise();
--panel2:addToUIManager();
end

--Events.OnMainMenuEnter.Add(testcomboBox);

