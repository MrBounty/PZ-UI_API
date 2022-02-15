require "ISUI/ISPanel"
require "ISUI/ISButton"
require "ISUI/ISInventoryPane"
require "ISUI/ISResizeWidget"
require "ISUI/ISMouseDrag"

require "defines"

SandboxOptionsScreen = ISPanelJoypad:derive("SandboxOptionsScreen");
local SandboxOptionsScreenListBox = ISScrollingListBox:derive("SandboxOptionsScreenListBox")
local SandboxOptionsScreenPanel = ISPanelJoypad:derive("SandboxOptionsScreenPanel")
local SandboxOptionsScreenPresetPanel = ISPanelJoypad:derive("SandboxOptionsScreenPresetPanel")
local SandboxOptionsScreenGroupBox = SandboxOptionsScreenPanel:derive("SandboxOptionsScreenGroupBox")

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

function SandboxOptionsScreenListBox:doDrawItem(y, item, alt)
    self:drawRectBorder(0, y, self:getWidth(), self.itemheight - 1, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b)

    if item.item.category then
        self:drawRect(0, y, self:getWidth(), self.itemheight - 1, 0.3, 0.5, 0.5, 0.5)
        local tex = getTexture("media/ui/ArrowDown.png")
        self:drawTexture(tex, 4, y + (item.height - tex:getHeight()) / 2, 1, 1, 1, 1)
    elseif self.selected == item.index then
        self:drawRect(0, y, self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15)
    elseif self.mouseoverselected == item.index and not self:isMouseOverScrollBar() then
        self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 4, 0.95, 0.05, 0.05, 0.05);
    end

    local dx = 16
    local dy = (self.itemheight - getTextManager():getFontFromEnum(self.font):getLineHeight()) / 2
    self:drawText(item.text, dx, y + dy, 0.9, 0.9, 0.9, 0.9, self.font)

    return y + item.height
end

function SandboxOptionsScreenListBox:onLoseJoypadFocus(joypadData)
    ISPanelJoypad.onLoseJoypadFocus(self, joypadData)
end

function SandboxOptionsScreenListBox:onJoypadDown(button, joypadData)
    if button == Joypad.BButton then
        joypadData.focus = self.parent
        updateJoypadFocus(joypadData)
    end
end

function SandboxOptionsScreenListBox:onJoypadDirLeft(joypadData)
    joypadData.focus = self.parent.presetPanel
    updateJoypadFocus(joypadData)
end

function SandboxOptionsScreenListBox:onJoypadDirRight(joypadData)
    joypadData.focus = self.parent.currentPanel
    updateJoypadFocus(joypadData)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function SandboxOptionsScreenPanel:prerender()
    ISPanelJoypad.prerender(self)
    if not self.entryText then
        self.entryText = {}
    end
    local nonDefaultOptions = self._instance.nonDefaultOptions
    for _,settingName in ipairs(self.settingNames) do
        local label = self.labels[settingName]
        local control = self.controls[settingName]
        if label and control then
            label:setColor(1, 1, 1)
            local option = nonDefaultOptions:getOptionByName(settingName)
            if option and (option:getValue() ~= option:getDefaultValue()) then
                label:setColor(1, 1, 0)
            end
        end
        if control and control.Type == "ISTextEntryBox" then
            local text = control:getText()
            if text ~= self.entryText[settingName] then
                self.entryText[settingName] = text
                local option = getSandboxOptions():getOptionByName(settingName)
                if option then
                    if option:isValidString(text) then
                        control.borderColor.a = 1
                        control.borderColor.g = 0.4
                        control.borderColor.b = 0.4
                    else
                        control.borderColor.a = 0.9
                        control.borderColor.g = 0.0
                        control.borderColor.b = 0.0
                    end
                end
            end
        end
    end
    local x1,y1,x2,y2 = 1,1,self.width-1,self.height-1
    if self.isGroupBoxContentsPanel then
        y1 = self.parent.tickBox:getHeight() / 2
        y2 = self.height-6
    end
    self:setStencilRect(x1, y1, x2 - x1 + 1, y2 - y1 + 1)
end

function SandboxOptionsScreenPanel:render()
    self:drawRectBorderStatic(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b)
    ISPanelJoypad.render(self)
    self:clearStencilRect()
    if self.isGroupBoxContentsPanel then
        self:repaintStencilRect(0, 0, self.width, self.height)
    end
end

function SandboxOptionsScreenPanel:onMouseWheel(del)
    if self:getScrollHeight() > 0 then
        self:setYScroll(self:getYScroll() - (del * 40))
        return true
    end
end

function SandboxOptionsScreenPanel:onGainJoypadFocus(joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData)
    if self.joypadButtons[self.joypadIndex] then
        self.joypadButtons[self.joypadIndex]:setJoypadFocused(true, joypadData)
    end
end

function SandboxOptionsScreenPanel:onLoseJoypadFocus(joypadData)
    ISPanelJoypad.onLoseJoypadFocus(self, joypadData)
    self:clearJoypadFocus()
end

function SandboxOptionsScreenPanel:onJoypadDown(button, joypadData)
    if button == Joypad.BButton and not self:isFocusOnControl() then
        joypadData.focus = self.parent
        updateJoypadFocus(joypadData)
    else
        ISPanelJoypad.onJoypadDown(self, button, joypadData)
    end
end

function SandboxOptionsScreenPanel:onJoypadDirLeft(joypadData)
    if not self:isFocusOnControl() then
        joypadData.focus = self.parent.listbox
        updateJoypadFocus(joypadData)
    end
end

function SandboxOptionsScreenPanel:onJoypadDirRight(joypadData)
    if not self:isFocusOnControl() then
        joypadData.focus = self.parent.presetPanel
        updateJoypadFocus(joypadData)
    end
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function SandboxOptionsScreenGroupBox:new(x, y, width, height, tickBoxLabel)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.tickBoxLabel = tickBoxLabel
	o.settings = nil
	return o
end

function SandboxOptionsScreenGroupBox:createChildren()
	local tickBox = ISTickBox:new(20, 4, 100, entryHgt, "", self, self.onTicked)
	tickBox.backgroundColor.a = 1
	tickBox.background = true
	tickBox.choicesColor = {r=1, g=1, b=1, a=1}
	tickBox.leftMargin = 2
	tickBox:setFont(UIFont.Medium)
	tickBox:addOption(self.tickBoxLabel)
	tickBox:setWidthToFit()
	tickBox.selected[1] = true
	self.tickBox = tickBox

	local scrollBarWidth = 17
	local cover = ISPanel:new(13, tickBox:getBottom(), self:getWidth() - 13 * 2 - scrollBarWidth, self:getHeight() - 13 - tickBox:getBottom())
	cover.borderColor.a = 0
	cover:setAnchorRight(true)
	cover:setAnchorBottom(true)
	cover:initialise()
	cover:instantiate()
	cover.javaObject:setConsumeMouseEvents(true)
	self.cover = cover

	local groupY = tickBox:getY() + tickBox:getHeight() / 2
	local contents = SandboxOptionsScreenPanel:new(12, groupY, self:getWidth() - 12 * 2, self:getHeight() - 12 - groupY)
	contents.borderColor.a = 0.6
	contents:setAnchorRight(true)
	contents:setAnchorBottom(true)
	contents.settingNames = {}
	contents.isGroupBoxContentsPanel = true
	contents._instance = self._instance
	self.contents = contents

	self:addChild(contents)
	self:addChild(cover)
	self:addChild(tickBox)
end

function SandboxOptionsScreenGroupBox:onTicked(index, selected)
	if selected then
		local options = self.settings
		for _,settingName in ipairs(self.settingNames) do
			local option = options:getOptionByName(settingName)
			local control = self.controls[settingName]
			if control.Type == "ISComboBox" then
				control.selected = option:getDefaultValue()
			elseif control.Type == "ISTickBox" then
				control.selected[1] = option:getDefaultValue()
			else
				error "unhandled control type"
			end
		end
		self.cover:setVisible(true)
	else
		self.cover:setVisible(false)
	end
	self:setJoypadButtons()
end

function SandboxOptionsScreenGroupBox:settingsToUI(settings)
	self.settings = settings
	local options = settings
	local allDefault = true
	for _,settingName in ipairs(self.settingNames) do
		local option = options:getOptionByName(settingName)
		local control = self.controls[settingName]
		if control.Type == "ISComboBox" then
			if control.selected ~= option:getDefaultValue() then
				allDefault = false
				break
			end
		elseif control.Type == "ISTickBox" then
			if control.selected[1] ~= option:getDefaultValue() then
				allDefault = false
				break
			end
		else
			error "unhandled control type"
		end
	end
	self.cover:setVisible(allDefault)
	self.tickBox.selected[1] = allDefault
	self:setJoypadButtons()
end

function SandboxOptionsScreenGroupBox:setJoypadButtons()
	if self.joypadIndexY > 1 then
		self:clearJoypadFocus()
	end
	self.joypadButtonsY = {}
	self:insertNewLineOfButtons(self.tickBox)
	if not self.tickBox.selected[1] then
		for _,control in pairs(self.controls) do
			self:insertNewLineOfButtons(control)
		end
	end
	self.joypadIndex = 1
	self.joypadIndexY = 1
	self.joypadButtons = self.joypadButtonsY[1]
end

function SandboxOptionsScreenGroupBox:ensureVisible()
    if not self.joyfocus then return end
    local child = self.joypadButtons[self.joypadIndex]
    if not child or child == self.tickBox then return end
    local y = child:getY()
    if y - 40 < 0 - self.contents:getYScroll() then
        self.contents:setYScroll(0 - y + 40)
    elseif y + child:getHeight() + 40 > 0 - self.contents:getYScroll() + self.contents:getHeight() then
        self.contents:setYScroll(0 - (y + child:getHeight() + 40 - self.contents:getHeight()))
    end
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function SandboxOptionsScreenPresetPanel:onGainJoypadFocus(joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData)
    if self.joypadButtons[self.joypadIndex] then
        self.joypadButtons[self.joypadIndex]:setJoypadFocused(true, joypadData)
    end
end

function SandboxOptionsScreenPresetPanel:onLoseJoypadFocus(joypadData)
    ISPanelJoypad.onLoseJoypadFocus(self, joypadData)
    self:clearJoypadFocus()
end

function SandboxOptionsScreenPresetPanel:onJoypadDown(button, joypadData)
    if button == Joypad.BButton and not self:isFocusOnControl() then
        joypadData.focus = self.parent
        updateJoypadFocus(joypadData)
    else
        ISPanelJoypad.onJoypadDown(self, button, joypadData)
    end
end

function SandboxOptionsScreenPresetPanel:onJoypadDirUp(joypadData)
    if self:isFocusOnControl() then
        ISPanelJoypad.onJoypadDirUp(self, joypadData)
    else
        joypadData.focus = self.parent.listbox
        updateJoypadFocus(joypadData)
    end
end

function SandboxOptionsScreenPresetPanel:onJoypadDirLeft(joypadData)
    ISPanelJoypad.onJoypadDirLeft(self, joypadData)
end

function SandboxOptionsScreenPresetPanel:onJoypadDirRight(joypadData)
    ISPanelJoypad.onJoypadDirRight(self, joypadData)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --


function SandboxOptionsScreen:initialise()
    ISPanelJoypad.initialise(self);
end


--************************************************************************--
--** ISPanel:instantiate
--**
--************************************************************************--
function SandboxOptionsScreen:instantiate()

    --self:initialise();
    self.javaObject = UIElement.new(self);
    self.javaObject:setX(self.x);
    self.javaObject:setY(self.y);
    self.javaObject:setHeight(self.height);
    self.javaObject:setWidth(self.width);
    self.javaObject:setAnchorLeft(self.anchorLeft);
    self.javaObject:setAnchorRight(self.anchorRight);
    self.javaObject:setAnchorTop(self.anchorTop);
    self.javaObject:setAnchorBottom(self.anchorBottom);
end

function SandboxOptionsScreen:syncStartDay()
	local year = getSandboxOptions():getFirstYear()
	local month = self.controls.StartMonth.selected
	if self.selectedYear == year and self.selectedMonth == month then return end
	self.selectedYear = year
	self.selectedMonth = month
	
	local lastDay = getGameTime():daysInMonth(year, month - 1)
	local t = {}
	for i=1,lastDay do table.insert(t, tostring(i)) end
	self.controls.StartDay.options = t
	if self.controls.StartDay.selected > lastDay then
        self.controls.StartDay.selected = lastDay
	end
end

function SandboxOptionsScreen:create()

	local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

    self.backButton = ISButton:new(16, self.height - 5 - buttonHgt, 100, buttonHgt, getText("UI_btn_back"), self, self.onOptionMouseDown);
    self.backButton.internal = "BACK";
    self.backButton:initialise();
    self.backButton:instantiate();
    self.backButton:setAnchorLeft(true);
    self.backButton:setAnchorTop(false);
    self.backButton:setAnchorBottom(true);
    self.backButton.borderColor = {r=1, g=1, b=1, a=0.1};

    self.backButton:setFont(UIFont.Small);
    self.backButton:ignoreWidthChange();
    self.backButton:ignoreHeightChange();
    self:addChild(self.backButton);

    self.playButton = ISButton:new(self.width - 116, self.height - 5 - buttonHgt, 100, buttonHgt, getText("UI_btn_next"), self, self.onOptionMouseDown);
    self.playButton.internal = "PLAY";
    self.playButton:initialise();
    self.playButton:instantiate();
    self.playButton:setAnchorLeft(false);
    self.playButton:setAnchorRight(true);
    self.playButton:setAnchorTop(false);
    self.playButton:setAnchorBottom(true);
    self.playButton:setEnable(true); -- sets the hard-coded border color
    self:addChild(self.playButton);

    self.presetPanel = SandboxOptionsScreenPresetPanel:new(0, self.height - 5 - buttonHgt, 100, buttonHgt)
    self.presetPanel:noBackground()
    self.presetPanel:setAnchorTop(false)
    self.presetPanel:setAnchorBottom(true)
    self:addChild(self.presetPanel)

    local labelX = 0
    local label = ISLabel:new(labelX, 3, FONT_HGT_SMALL, getText("Sandbox_SavedPresets"), 1, 1, 1, 1, UIFont.Medium, true);
    label:initialise();
    self.presetPanel:addChild(label);

    self.presetList = ISComboBox:new(labelX + getTextManager():MeasureStringX(UIFont.Medium, label:getName()) + 20, 0, 200, buttonHgt, self, self.onPresetChange);
    self.presetList:initialise();
    self.presetPanel:addChild(self.presetList);

	local buttonHgt2 = math.max(20, FONT_HGT_SMALL + 3 * 2)

    self.savePresetButton = ISButton:new(self.presetList.x + self.presetList.width + 10, 0, 50, buttonHgt2, getText("Sandbox_SaveButton"), self, self.onOptionMouseDown);
    self.savePresetButton.internal = "SAVEPRESET";
    self.savePresetButton:initialise();
    self.savePresetButton:instantiate();
    self.savePresetButton:setFont(UIFont.Small);
    self.savePresetButton:ignoreWidthChange();
    self.savePresetButton:ignoreHeightChange();
    self.presetPanel:addChild(self.savePresetButton);

    self.deletePresetButton = ISButton:new(self.savePresetButton:getRight() + 10, 0, 50, buttonHgt2, getText("UI_characreation_BuildDel"), self, self.onOptionMouseDown)
    self.deletePresetButton.internal = "DELETEPRESET"
    self.deletePresetButton:initialise()
    self.deletePresetButton:setFont(UIFont.Small)
    self.deletePresetButton:ignoreWidthChange()
    self.deletePresetButton:ignoreHeightChange()
    self.presetPanel:addChild(self.deletePresetButton)

    self.presetPanel:setWidth(self.deletePresetButton:getRight())
    if getDebug() then
        self.devPresetButton = ISButton:new(self.deletePresetButton:getRight() + 10, 0, 50, buttonHgt2, "*DEV*", self, self.onOptionMouseDown)
        self.devPresetButton.internal = "DEVPRESET"
        self.devPresetButton:initialise()
        self.devPresetButton:setFont(UIFont.Small)
        self.devPresetButton:ignoreWidthChange()
        self.devPresetButton:ignoreHeightChange()
        self.presetPanel:addChild(self.devPresetButton)
        self.presetPanel:setWidth(self.devPresetButton:getRight())
   end
    self.presetPanel:setX(self.width / 2 - self.presetPanel:getWidth() / 2)

    self.presetPanel:insertNewLineOfButtons(self.presetList, self.savePresetButton, self.deletePresetButton, self.devPresetButton)
    self.presetPanel.joypadIndex = 1
    self.presetPanel.joypadIndexY = 1

    local titleHgt = getTextManager():getFontFromEnum(UIFont.Title):getLineHeight()
    self.listbox = SandboxOptionsScreenListBox:new(24, 10 + titleHgt + 10, 300, self.height - 50 - 10 - titleHgt - 10)
    self.listbox:initialise()
    self.listbox:setAnchorLeft(true)
    self.listbox:setAnchorRight(false)
    self.listbox:setAnchorTop(true)
    self.listbox:setAnchorBottom(true)
    self.listbox:setFont("Medium", 4)
    self.listbox.drawBorder = true
    self.listbox:setOnMouseDownFunction(self, self.onMouseDownListbox)
    self:addChild(self.listbox)

    self.controls = {}
    self.groupBox = {}
    local SettingsTable = ServerSettingsScreen.getSandboxSettingsTable()
    for _,page in ipairs(SettingsTable) do
        local item = {}
        item.page = page
        item.panel = self:createPanel(page)
        self.listbox:addItem(page.name, item)
    end

    self.defaultPreset = self:getDefaultPreset();
    self:setVisible(false);
    self:loadPresets();
    for i,preset in ipairs(self.presets) do
        if preset.name == self.defaultPreset.name then
            self.presetList.selected = i
            break
        end
    end
    self:onPresetChange()

    self:onMouseDownListbox(self.listbox.items[1].item)
end

function SandboxOptionsScreen:createPanel(page)
    local panel
    if page.groupBox then
        panel = SandboxOptionsScreenGroupBox:new(self.listbox:getRight() + 24, self.listbox:getY(),
            self.width - 24 - self.listbox:getRight() - 24, self.listbox:getHeight(),
            getText("Sandbox_" .. page.groupBox))
        self.groupBox[page.groupBox] = panel
    else
        panel = SandboxOptionsScreenPanel:new(self.listbox:getRight() + 24, self.listbox:getY(), self.width - 24 - self.listbox:getRight() - 24, self.listbox:getHeight())
    end
    panel._instance = self
    panel:initialise()
    panel:instantiate()
    panel:setAnchorRight(true)
    panel:setAnchorBottom(true)
    panel.settingNames = {}
    panel.labels = {}
    panel.controls = {}

    local fontHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
    local entryHgt = fontHgt + 2 * 2

    local labels = {}
    local controls = {}
    for _,setting in ipairs(page.settings) do
        local settingName = setting.translatedName
        local tooltip = setting.tooltip
        if tooltip then
            tooltip = tooltip:gsub("\\n", "\n")
            tooltip = tooltip:gsub("\\\"", "\"")
        end
        local label = nil
        local control = nil
        if not getDebug() and (setting.name == "WaterShutModifier" or setting.name == "ElecShutModifier") then
            -- ignore
        elseif setting.type == "checkbox" then
            label = ISLabel:new(0, 0, entryHgt, settingName, 1, 1, 1, 1, UIFont.Medium)
            control = ISTickBox:new(0, 0, 100, entryHgt, "", nil, nil)
            control:addOption("")
            control.selected[1] = setting.default
            if setting.tooltip then
                control.tooltip = setting.tooltip
            end
        elseif setting.type == "entry" or setting.type == "string" then
            if getDebug() and (setting.name == "WaterShutModifier" or setting.name == "ElecShutModifier") then
                settingName = "*DEV* " .. setting.name
            end
            label = ISLabel:new(0, 0, entryHgt, settingName, 1, 1, 1, 1, UIFont.Medium)
            control = ISTextEntryBox:new(setting.text, 0, 0, 300, entryHgt)
            control.font = UIFont.Medium
            control.tooltip = tooltip
            control:initialise()
            control:instantiate()
            control:setOnlyNumbers(setting.onlyNumbers or false)
        elseif setting.type == "enum" then
            label = ISLabel:new(0, 0, entryHgt, settingName, 1, 1, 1, 1, UIFont.Medium)
            control = ISComboBox:new(0, 0, 200, entryHgt, self, self.onComboBoxSelected, setting.name)
            if tooltip then
                control.tooltip = { defaultTooltip = tooltip }
            end
            control:initialise()
            for index,value in ipairs(setting.values) do
                control:addOption(value)
                if index == setting.default then
                    control.selected = index
                end
            end
        elseif setting.type == "spinbox" then
            label = ISLabel:new(0, 0, entryHgt, settingName, 1, 1, 1, 1, UIFont.Medium)
            control = ISSpinBox:new(0, 0, 200, entryHgt, nil, nil)
            control:initialise()
            control:instantiate()
            if setting.name == "StartYear" then
                local firstYear = getSandboxOptions():getFirstYear()
                for i=1,100 do
                    control:addOption(tostring(firstYear + i - 1))
                end
            elseif setting.name == "StartDay" then
            end
        elseif setting.type == "text" then
            label = ISLabel:new(0, 0, entryHgt, settingName, 1, 1, 1, 1, UIFont.Medium)
            control = ISTextEntryBox:new(setting.text, 0, 0, 300, 4 + fontHgt * 4 + 4)
            control.font = UIFont.Medium
            control:initialise()
            control:instantiate()
            control:setMultipleLine(true)
            control:setMaxLines(64)
            control:addScrollBars()
        end
        if label and control then
            label.tooltip = tooltip
            table.insert(labels, label)
            table.insert(controls, control)
            self.controls[setting.name] = control
            table.insert(panel.settingNames, setting.name)
            panel.labels[setting.name] = label
            panel.controls[setting.name] = control
        else
--			error "no label or control"
        end
    end
    local labelWidth = 0
    for _,label in ipairs(labels) do
        labelWidth = math.max(labelWidth, label:getWidth())
    end
    local x = 24
    local y = 12
    local addControlsTo = panel
    if page.groupBox then
        addControlsTo = panel.contents
        y = math.max(12, panel.tickBox.height / 2)
    end
    addControlsTo:setScrollChildren(true)
    addControlsTo:addScrollBars()
    addControlsTo.vscroll.doSetStencil = true
    for i=1,#labels do
        local label = labels[i]
        addControlsTo:addChild(label)
        label:setX(x + labelWidth - label:getWidth())
        label:setY(y)
        y = y + math.max(label:getHeight(), controls[i]:getHeight()) + 6;
    end
    y = 12
    if page.groupBox then
        y = math.max(12, panel.tickBox.height / 2)
    end
    for i=1,#controls do
        local label = labels[i]
        local control = controls[i]
        addControlsTo:addChild(control)
        control:setX(x + labelWidth + 16)
        control:setY(y)
        y = y + math.max(label:getHeight(), control:getHeight()) + 6
        if control.isCombobox or control.isTickBox then
            panel:insertNewLineOfButtons(control)
        end
        addControlsTo:setScrollHeight(y)
    end
    if #panel.joypadButtonsY > 0 then
        panel.joypadIndex = 1
        panel.joypadIndexY = 1
        panel.joypadButtons = panel.joypadButtonsY[1]
    end
    return panel
end

function SandboxOptionsScreen:settingsToUI(options)
	for i=1,options:getNumOptions() do
		local option = options:getOptionByIndex(i-1)
		local control = self.controls[option:getName()]
		if control then
			if option:getType() == "boolean" then
				control.selected[1] = option:getValue()
			elseif option:getType() == "double" then
				control:setText(option:getValueAsString())
			elseif option:getType() == "enum" then
				control.selected = option:getValue()
			elseif option:getType() == "integer" then
				control:setText(option:getValueAsString())
			elseif option:getType() == "string" then
				control:setText(option:getValue())
			elseif option:getType() == "text" then
				control:setText(option:getValue())
			end
		end
	end
	for _,groupBox in pairs(self.groupBox) do
		groupBox:settingsToUI(options)
	end
end

function SandboxOptionsScreen:settingsFromUI(options)
	for i=1,options:getNumOptions() do
		local option = options:getOptionByIndex(i-1)
		local control = self.controls[option:getName()]
		if control then
			if option:getType() == "boolean" then
				option:setValue(control.selected[1] == true)
			elseif option:getType() == "double" then
				option:parse(control:getText())
			elseif option:getType() == "enum" then
				option:setValue(control.selected)
			elseif option:getType() == "integer" then
				option:parse(control:getText())
			elseif option:getType() == "string" then
				option:setValue(control:getText())
			elseif option:getType() == "text" then
				option:setValue(control:getText())
			end
		end
	end
end
        
function SandboxOptionsScreen:onMouseDownListbox(item)
	if item.page then
		if self.currentPanel then
			self:removeChild(self.currentPanel)
			self.currentPanel = nil
		end
		if item.panel then
			self:addChild(item.panel)
			item.panel:setWidth(self.width - 24 - self.listbox:getRight() - 24)
			item.panel:setHeight(self.listbox:getHeight())
			self.currentPanel = item.panel
		end
	end
end

function SandboxOptionsScreen:onResolutionChange(oldw, oldh, neww, newh)
    self.presetPanel:setX(self.width / 2 - self.presetPanel:getWidth() / 2)
end

function SandboxOptionsScreen:onPresetChange()
    local presetLoaded = self.presets[self.presetList.selected];
    if presetLoaded then
        self:settingsToUI(presetLoaded.options)
    end
end

function SandboxOptionsScreen:getDefaultPreset()
    if self.defaultPreset then
        return self.defaultPreset
    end
    local newPreset = {};
    newPreset.name = "Apocalypse";
    newPreset.options = SandboxOptions:new()
    return newPreset;
end

local function copyPreset(orig)
    local copy = {}
    copy.name = orig.name
    copy.options = orig.options:newCopy()
    return copy
end

function SandboxOptionsScreen:loadPresets()
    self.presetList.options = {};
    self.presets = {};

    self.presetList:addOption(getText("UI_NewGame_Apocalypse"));
    table.insert(self.presets, copyPreset(self:getApocalypsePreset()));
    self.presetList:addOption(getText("UI_NewGame_Survivor"));
    table.insert(self.presets, copyPreset(self:getSurvivorPreset()));
    self.presetList:addOption(getText("UI_NewGame_Builder"));
    table.insert(self.presets, copyPreset(self:getBuilderPreset()));

    self.presetList:addOption(getText("UI_NewGame_InitialInfection"));
    table.insert(self.presets, copyPreset(self:getBeginnerPreset()));
    self.presetList:addOption(getText("UI_NewGame_OneWeekLater"));
    table.insert(self.presets, copyPreset(self:getNormalPreset()));
    self.presetList:addOption(getText("UI_NewGame_Survival"));
    table.insert(self.presets, copyPreset(self:getSurvivalPreset()));
    self.presetList:addOption(getText("UI_NewGame_SixMonths"));
    table.insert(self.presets, copyPreset(self:getHardPreset()));

    local presets = getSandboxPresets();
    if presets then
        for i=1,presets:size() do
            local newPreset = {}
            newPreset.name = presets:get(i-1)
            newPreset.userDefined = true
            newPreset.options = SandboxOptions:new()
            newPreset.options:loadPresetFile(newPreset.name)
            table.insert(self.presets, newPreset)
            self.presetList:addOption(newPreset.name);
        end
    end

    if self.presetList.selected > #self.presetList.options then
        self.presetList.selected = #self.presetList.options
    end
    self:onPresetChange()
end

function SandboxOptionsScreen:getNormalPreset()
    local newPreset = {};
    newPreset.name = "FirstWeek";
    newPreset.options = SandboxOptions:new()
    newPreset.options:loadGameFile(newPreset.name)
    return newPreset;
end

function SandboxOptionsScreen:getSurvivalPreset()
    local newPreset = {};
    newPreset.name = "Survival";
    newPreset.options = SandboxOptions:new()
    newPreset.options:loadGameFile(newPreset.name)
    return newPreset;
end

function SandboxOptionsScreen:getHardPreset()
    local newPreset = {};
    newPreset.name = "SixMonthsLater";
    newPreset.options = SandboxOptions:new()
    newPreset.options:loadGameFile(newPreset.name)
    return newPreset;
end

function SandboxOptionsScreen:getBeginnerPreset()
    local newPreset = {};
    newPreset.name = "Beginner";
    newPreset.options = SandboxOptions:new()
    newPreset.options:loadGameFile(newPreset.name)
    return newPreset;
end

function SandboxOptionsScreen:getApocalypsePreset()
    local newPreset = {};
    newPreset.name = "Apocalypse";
    newPreset.options = SandboxOptions:new()
    newPreset.options:loadGameFile(newPreset.name)
    return newPreset;
end

function SandboxOptionsScreen:getSurvivorPreset()
    local newPreset = {};
    newPreset.name = "Survivor";
    newPreset.options = SandboxOptions:new()
    newPreset.options:loadGameFile(newPreset.name)
    return newPreset;
end

function SandboxOptionsScreen:getBuilderPreset()
    local newPreset = {};
    newPreset.name = "Builder";
    newPreset.options = SandboxOptions:new()
    newPreset.options:loadGameFile(newPreset.name)
    return newPreset;
end

function SandboxOptionsScreen:subPanelPreRender()
    self:setStencilRect(0,0,self:getWidth(),self:getHeight());

    ISPanel.prerender(self);
end

function SandboxOptionsScreen:subPanelRender()
    ISPanel.render(self);
    self:clearStencilRect();
end

function SandboxOptionsScreen:prerender()
SandboxOptionsScreen.instance = self
--[[
    if self.properTickbox.selected[1] == true then
        self.speed.selected = 2;
        self.strength.selected = 2;
        self.tough.selected = 2;
        self.decomp.selected = 1;
        self.trans.selected = 1;
        self.reanim.selected = 3;
        self.zombtime.selected = 5;
        self.sight.selected = 2;
        self.memory.selected = 2;
        self.doors.selected = 2;
        self.hearing.selected = 2;
        self.smell.selected = 2;
        self.zombDisPanel:setVisible(true);
        self.zombPanel.background = false
    else
        self.zombDisPanel:setVisible(false);
        self.zombPanel.background = true
    end
--]]
    self:syncStartDay()

--    self.zombDisPanel:bringToTop();
    ISPanelJoypad.prerender(self);
    self:drawTextCentre(getText("UI_optionscreen_SandboxOptions"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title);

    local deleteOK = false
    if self.presets[self.presetList.selected] then
        deleteOK = self.presets[self.presetList.selected].userDefined
        if getDebug() then
            self.devPresetButton:setEnable(not deleteOK)
        end
    end
    self.deletePresetButton:setEnable(deleteOK)

    -- This is used to highlight options with non-default values.
    self:settingsFromUI(self.nonDefaultOptions)
end

function SandboxOptionsScreen:render()
    ISPanelJoypad.render(self)

    if self.listbox.joyfocus then
        local ui = self.listbox
        self:drawRectBorder(ui:getX(), ui:getY(), ui:getWidth(), ui:getHeight(), 0.4, 0.2, 1.0, 1.0)
        self:drawRectBorder(ui:getX()+1, ui:getY()+1, ui:getWidth()-2, ui:getHeight()-2, 0.4, 0.2, 1.0, 1.0)
    elseif self.currentPanel.joyfocus then
        local ui = self.currentPanel
        self:drawRectBorder(ui:getX(), ui:getY(), ui:getWidth(), ui:getHeight(), 0.4, 0.2, 1.0, 1.0)
        self:drawRectBorder(ui:getX()+1, ui:getY()+1, ui:getWidth()-2, ui:getHeight()-2, 0.4, 0.2, 1.0, 1.0)
    elseif self.presetPanel.joyfocus then
        local ui = self.presetPanel
        self:drawRectBorder(ui:getX() - 4, ui:getY() - 4, ui:getWidth() + 4 + 3, ui:getHeight() + 4 + 3, 0.4, 0.2, 1.0, 1.0)
        self:drawRectBorder(ui:getX() - 3, ui:getY() - 3, ui:getWidth() + 3 + 2, ui:getHeight() + 4 + 2, 0.4, 0.2, 1.0, 1.0)
    end
end

function SandboxOptionsScreen:setSandboxVars()
    local options = getSandboxOptions()
    self:settingsFromUI(options)
    local waterShut = options:getOptionByName("WaterShut"):getValue()
    local elecShut = options:getOptionByName("ElecShut"):getValue()
    options:set("WaterShutModifier", options:randomWaterShut(waterShut))
    options:set("ElecShutModifier", options:randomElectricityShut(elecShut))
    options:toLua()
end

function SandboxOptionsScreen:onOptionMouseDown(button, x, y)
    if button.internal == "BACK" then
        self:setVisible(false);
        if MainScreen.instance.createWorld or MapSpawnSelect.instance:hasChoices() then
            MapSpawnSelect.instance:setVisible(true, self.joyfocus)
            return
        end
        if WorldSelect.instance:hasChoices() then
            WorldSelect.instance:setVisible(true, self.joyfocus)
            return
        end
    end
    if button.internal == "PLAY" then
        MainScreen.instance.sandOptions:setVisible(false);
        MainScreen.instance.charCreationProfession.previousScreen = "SandboxOptionsScreen"
        MainScreen.instance.charCreationProfession:setVisible(true, self.joyfocus);
        self:setSandboxVars();
    end
    if button.internal == "SAVEPRESET" then
        local name = "New"
        if self.presets[self.presetList.selected] and self.presets[self.presetList.selected].userDefined then
            name = self.presets[self.presetList.selected].name or "New"
        end
        local modal = ISTextBox:new((getCore():getScreenWidth() / 2) - 140, (getCore():getScreenHeight() / 2) - 90, 280, 180, getText("Sandbox_SavePrompt"), name, self, self.onSavePreset);
        modal.backgroundColor.a = 0.9
        modal:initialise();
        modal:setAlwaysOnTop(true)
        modal:setCapture(true)
        modal:setValidateFunction(self, self.onValidateSavePreset)
        modal:addToUIManager();
        if self.presetPanel.joyfocus then
            modal.param1 = self.presetPanel.joyfocus
            self.presetPanel.joyfocus.focus = modal
            updateJoypadFocus(self.presetPanel.joyfocus)
        end
    end
    if button.internal == "DELETEPRESET" then
        local preset = self.presets[self.presetList.selected]
        if preset and preset.userDefined then
            self:deletePresetStep1(preset)
        end
    end
    if button.internal == "DEVPRESET" then
        local preset = self.presets[self.presetList.selected]
        if preset and not preset.userDefined then
            local screenW = getCore():getScreenWidth()
            local screenH = getCore():getScreenHeight()
            local modal = ISModalDialog:new((screenW - 230) / 2, (screenH - 120) / 2, 230, 120,
                "Overwrite media/lua/shared/Sandbox/" .. preset.name .. ".lua?", true, self, self.onSaveDeveloperPreset);
            modal.backgroundColor.a = 0.9
            modal:initialise()
            modal:setCapture(true)
            modal:setAlwaysOnTop(true)
            modal:addToUIManager()
            if self.presetPanel.joyfocus then
                modal.param1 = self.presetPanel.joyfocus
                self.presetPanel.joyfocus.focus = modal
                updateJoypadFocus(self.presetPanel.joyfocus)
            end
        end
    end
end

function SandboxOptionsScreen:onSavePreset(button, joypadData)
    local modal = button.parent;
    if joypadData then
        joypadData.focus = self.presetPanel
        updateJoypadFocus(joypadData)
    end
    if button.internal == "OK" then
        local name = button.parent.entry:getText()
        if SandboxOptions.isValidPresetName(name) then
            modal:destroy()
            local options = SandboxOptions:new()
            self:settingsFromUI(options)
            options:savePresetFile(name)
            self:loadPresets()
            for i,preset in ipairs(self.presets) do
                if preset.name == name then
                    self.presetList.selected = i
                    self:onPresetChange()
                    break
                end
            end
        else
            -- Let player know an invalid character was entered
            modal:showErrorMessage(true, getText("Sandbox_PresetName_Error"))
        end
    elseif button.internal == "CANCEL" then
        modal:destroy()
    end
end

function SandboxOptionsScreen:deletePresetStep1(preset)
    local screenW = getCore():getScreenWidth()
    local screenH = getCore():getScreenHeight()
    local modal = ISModalDialog:new((screenW - 230) / 2, (screenH - 120) / 2, 230, 120, getText("Sandbox_DeletePresetPrompt", preset.name), true, self, self.deletePresetStep2);
    modal.backgroundColor.a = 0.9
    modal:initialise()
    modal:setCapture(true)
    modal:setAlwaysOnTop(true)
    modal:addToUIManager()
    if self.presetPanel.joyfocus then
        modal.param1 = self.presetPanel.joyfocus
        self.presetPanel.joyfocus.focus = modal
        updateJoypadFocus(self.presetPanel.joyfocus)
    end
end

function SandboxOptionsScreen:deletePresetStep2(button, joypadData)
    if joypadData then
        joypadData.focus = self.presetPanel
        updateJoypadFocus(joypadData)
    end
    if button.internal == "NO" then return end
    
    local preset = self.presets[self.presetList.selected]
    if preset and preset.userDefined then
        deleteSandboxPreset(preset.name)
        self:loadPresets()
    end
end

function SandboxOptionsScreen:onValidateSavePreset(text)
    return SandboxOptions.isValidPresetName(text)
end

function SandboxOptionsScreen:onSaveDeveloperPreset(button, joypadData)
    if joypadData then
        joypadData.focus = self.presetPanel
        updateJoypadFocus(joypadData)
    end
    if button.internal == "NO" then return end
    local preset = self.presets[self.presetList.selected]
    if preset and not preset.userDefined then
        self:settingsFromUI(preset.options)
        preset.options:saveGameFile(preset.name)
        self:loadPresets()
    end
end

function SandboxOptionsScreen:onComboBoxSelected(combo, optionName)
    if optionName == "Zombies" then
        local Zombies = combo.selected
        local popMult = { "4.0", "3.0", "2.0", "1.0", "0.35", "0.0" }
        self.controls["ZombieConfig.PopulationMultiplier"]:setText(popMult[Zombies])
    end
end

function SandboxOptionsScreen:onGroupBox(index, selected, groupBoxName)
	local groupBox = self.groupBox[groupBoxName]
	local options = getSandboxOptions()
	if selected then
		for _,settingName in ipairs(groupBox.panel.settingNames) do
			local option = options:getOptionByName(settingName)
			local control = groupBox.panel.controls[settingName]
			if control.Type == "ISComboBox" then
				control.selected = option:getDefaultValue()
			elseif control.Type == "ISTickBox" then
				control.selected[1] = option:getDefaultValue()
			else
				error "unhandled control type"
			end
		end
		groupBox.cover:setVisible(true)
	else
		groupBox.cover:setVisible(false)
	end
end

SandboxOptionsScreen.load = function()
	SandboxVars.Temperature = getSandboxOptions():getTemperatureModifier();
	SandboxVars.Rain = getSandboxOptions():getRainModifier();
	SandboxVars.WaterShutModifier = getSandboxOptions():getWaterShutModifier();
	SandboxVars.ElecShutModifier = getSandboxOptions():getElecShutModifier();
	SandboxVars.FoodLoot = getSandboxOptions():getFoodLootModifier();
	if SandboxVars.FoodLoot == 1 then
		ZomboidGlobals.FoodLootModifier = 0.2 -- extremely rare
	elseif SandboxVars.FoodLoot == 2 then
		ZomboidGlobals.FoodLootModifier = 0.6 -- rare
	elseif SandboxVars.FoodLoot == 3 then
		ZomboidGlobals.FoodLootModifier = 1.0 -- normal
	elseif SandboxVars.FoodLoot == 4 then
		ZomboidGlobals.FoodLootModifier = 2.0 -- common
	elseif SandboxVars.FoodLoot == 5 then
		ZomboidGlobals.FoodLootModifier = 3.0 -- abundant
    end
    SandboxVars.WeaponLoot = getSandboxOptions():getWeaponLootModifier();
    if SandboxVars.WeaponLoot == 1 then
        ZomboidGlobals.WeaponLootModifier = 0.2 -- extremely rare
    elseif SandboxVars.WeaponLoot == 2 then
        ZomboidGlobals.WeaponLootModifier = 0.6 -- rare
    elseif SandboxVars.WeaponLoot == 3 then
        ZomboidGlobals.WeaponLootModifier = 1.0 -- normal
    elseif SandboxVars.WeaponLoot == 4 then
        ZomboidGlobals.WeaponLootModifier = 2.0 -- common
    elseif SandboxVars.WeaponLoot == 5 then
        ZomboidGlobals.WeaponLootModifier = 3.0 -- abundant
    end
    SandboxVars.OtherLoot = getSandboxOptions():getOtherLootModifier();
    if SandboxVars.OtherLoot == 1 then
        ZomboidGlobals.OtherLootModifier = 0.2 -- extremely rare
    elseif SandboxVars.OtherLoot == 2 then
        ZomboidGlobals.OtherLootModifier = 0.6 -- rare
    elseif SandboxVars.OtherLoot == 3 then
        ZomboidGlobals.OtherLootModifier = 1.0 -- normal
    elseif SandboxVars.OtherLoot == 4 then
        ZomboidGlobals.OtherLootModifier = 2.0 -- common
    elseif SandboxVars.OtherLoot == 5 then
        ZomboidGlobals.OtherLootModifier = 3.0 -- abundant
    end
end

function SandboxOptionsScreen:setVisible(visible, joypadData)
    ISPanelJoypad.setVisible(self, visible, joypadData)
    if not visible then
        self.hadJoypadFocus = true
    end
end

function SandboxOptionsScreen:onGainJoypadFocus(joypadData)
    if self.hadJoypadFocus then
        ISPanelJoypad.onGainJoypadFocus(self, joypadData)
        self:setISButtonForA(self.playButton)
        self:setISButtonForB(self.backButton)
    else
        self.hadJoypadFocus = true
        joypadData.focus = self.listbox
        updateJoypadFocus(joypadData)
    end
end

function SandboxOptionsScreen:onLoseJoypadFocus(joypadData)
    ISPanelJoypad.onLoseJoypadFocus(self, joypadData)
    self.playButton.isJoypad = false
    self.backButton:clearJoypadButton()
    self.ISButtonY = nil
end

function SandboxOptionsScreen:onJoypadDirUp(joypadData)
    joypadData.focus = self.listbox
    updateJoypadFocus(joypadData)
end

function SandboxOptionsScreen:onJoypadDirLeft(joypadData)
    joypadData.focus = self.presetPanel
    updateJoypadFocus(joypadData)
end

function SandboxOptionsScreen:onJoypadDirRight(joypadData)
    joypadData.focus = self.presetPanel
    updateJoypadFocus(joypadData)
end

function SandboxOptionsScreen:new(x, y, width, height)
    local o = {}
    o = ISPanelJoypad:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.backgroundColor = {r=0, g=0, b=0, a=0.5};
    o.borderColor = {r=1, g=1, b=1, a=0.2};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.addY = 0;
    o.nonDefaultOptions = SandboxOptions:new();
    SandboxOptionsScreen.instance = o;
    return o
end

--Events.OnGameStart.Add(SandboxOptionsScreen.load);
