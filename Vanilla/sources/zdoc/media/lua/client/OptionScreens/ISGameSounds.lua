--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanelJoypad"

local MainPanel = ISPanelJoypad:derive("GameSoundsMainPanel")

function MainPanel:prerender()
	local dragging = nil
	local mouseOver = nil
	for _,child in pairs(self:getChildren()) do
		if child:isMouseOver() then
			if child.Type == "ISGameSoundVolumeControl" then
				mouseOver = child.label
			elseif child.Type == "ISSpeakerButton" then
				mouseOver = child.label
			end
		end
		if child.Type == "ISGameSoundVolumeControl" and child.dragging then
			dragging = child.label
		end
		if child.Type == "ISGameSoundVolumeControl" and self.joyfocus and child.joypadFocused then
			dragging = child.label
		end
	end
	mouseOver = dragging or mouseOver
	if mouseOver ~= self.highlightedLabel then
		if self.highlightedLabel then
			self.highlightedLabel:setColor(1, 1, 1)
		end
		self.highlightedLabel = mouseOver
		if self.highlightedLabel then
			self.highlightedLabel:setColor(0, 1, 1)
		end
	end

	self:setStencilRect(0, 0, self:getWidth(), self:getHeight())
	ISPanelJoypad.prerender(self)
end

function MainPanel:render()
	ISPanelJoypad.render(self)
	self:clearStencilRect()

	local ui = self.parent.parent
	if ui.previewControl and ui.previewControl:getParent() == self then
		if GameSounds.isPreviewPlaying() then
			self.stopSound:setX(ui.previewControl.speaker:getRight() + 8)
			self.stopSound:setY(ui.previewControl.speaker:getY())
			self.stopSound.control = ui.previewControl
			self.stopSound:setVisible(true)
		else
			self.stopSound:setVisible(false)
		end
	else
		self.stopSound:setVisible(false)
	end
end

function MainPanel:onResize(width, height)
	ISPanelJoypad.onResize(self, width, height)
	-- FIXME: Need a better way to handle things being centered.
	local minX,maxX = 100000,-100000
	for _,child in pairs(self:getChildren()) do
		if child ~= self.vscroll and child ~= self.stopSound then
			if child.Type == "ISLabel" then
				minX = math.min(minX, child.originalX - self.parent.parent.maxLabelWidth)
				maxX = math.max(maxX, child:getRight())
			else
				minX = math.min(minX, child.x)
				maxX = math.max(maxX, child:getRight())
			end
		end
	end
	local padLeft = (self.width - (maxX - minX)) / 2
	for _,child in pairs(self:getChildren()) do
		if child ~= self.vscroll and child ~= self.stopSound then
			child:setX(padLeft + (child.x - minX))
			if child.Type == "ISLabel" then
				child.originalX = child:getRight()
			end
		end
	end
end

function MainPanel:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData)
	self:setISButtonForB(self.parent.parent.buttonClose)
	if self.joypadIndexY == 0 then
		if #self.joypadButtonsY > 0 then
			self.joypadIndex = 1
			self.joypadIndexY = 1
			self.joypadButtons = self.joypadButtonsY[self.joypadIndexY]
			if self.joypadIndex > #self.joypadButtons then
				self.joypadIndex = #self.joypadButtons
			end
		end
	end
	if self.joypadButtons and self.joypadButtons[self.joypadIndex] then
		self.joypadButtons[self.joypadIndex]:setJoypadFocused(true, joypadData)
	end
end

function MainPanel:onLoseJoypadFocus(joypadData)
	self:clearJoypadFocus()
	ISPanelJoypad.onLoseJoypadFocus(self, joypadData)
end

function MainPanel:onJoypadBeforeDeactivate(joypadData)
	self.parent.parent:onJoypadBeforeDeactivate(joypadData)
end

function MainPanel:onJoypadDown(button, joypadData)
	if button == Joypad.LBumper or button == Joypad.RBumper then
		local viewIndex = self.parent:getActiveViewIndex()
		if button == Joypad.LBumper then
			if viewIndex == 1 then
				viewIndex = #self.parent.viewList
			else
				viewIndex = viewIndex - 1
			end
		elseif button == Joypad.RBumper then
			if viewIndex == #self.parent.viewList then
				viewIndex = 1
			else
				viewIndex = viewIndex + 1
			end
		end
		self.parent:activateView(self.parent.viewList[viewIndex].name)
		joypadData.focus = self.parent:getActiveView()
		updateJoypadFocus(joypadData)
--[[
	elseif button == Joypad.AButton then
		local volumeCtl = self.joypadButtons[self.joypadIndex]
		self.parent.parent.onPlaySound({ self.parent.parent, volumeCtl })
	elseif button == Joypad.XButton then
		self.parent.parent.onStopSound(self.parent.parent)
--]]
	else
		ISPanelJoypad.onJoypadDown(self, button, joypadData)
	end
end

function MainPanel:onJoypadDirLeft(joypadData)
	local volumeCtl = self.joypadButtons[self.joypadIndex]
	volumeCtl:setVolume(volumeCtl:getVolume() - 0.05)
end

function MainPanel:onJoypadDirRight(joypadData)
	local volumeCtl = self.joypadButtons[self.joypadIndex]
	volumeCtl:setVolume(volumeCtl:getVolume() + 0.05)
end

function MainPanel:new(x, y, width, height)
	local o = ISPanelJoypad.new(self, x, y, width, height)
	o.borderColor = {r=0, g=0, b=0, a=0}
	o:noBackground()
	o.highlightedLabel = nil
	return o
end

-- -- -- -- --

---@class ISGameSounds : ISPanelJoypad
ISGameSounds = ISPanelJoypad:derive("ISGameSounds")

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

function ISGameSounds:addCombo(x, y, w, h, name, options, selected, target, onchange)

	h = FONT_HGT_SMALL + 3 * 2

	local label = ISLabel:new(x, y + self.addY, h, name, 1, 1, 1, 1, UIFont.Small)
	label:initialise()
	self.mainPanel:addChild(label)
	local panel2 = ISComboBox:new(x+20, y + self.addY, w, h, target, onchange)
	panel2:initialise()

	for i, k in ipairs(options) do
		panel2:addOption(k)
	end

	panel2.selected = selected
	self.mainPanel:addChild(panel2)
	self.mainPanel:insertNewLineOfButtons(panel2)
	self.addY = self.addY + h + 6
	return panel2
end

local function DummyMouseUp(x, y)
end

function ISGameSounds:addVolumeControl(x, y, w, h, name, volume, target, onchange)
	local fontHgt = FONT_HGT_SMALL

	local label = ISLabel:new(x, y + self.addY, fontHgt, name, 1, 1, 1, 1, UIFont.Small)
	label:initialise()
	self.mainPanel:addChild(label)

	local volumeCtl = ISGameSoundVolumeControl:new(x+20, y + self.addY + (math.max(fontHgt, h) - h) / 2, w, h, target, onchange)
	volumeCtl:initialise()
	volumeCtl:setVolume(volume)
	volumeCtl.selected = selected
	volumeCtl.default = selected
	volumeCtl.label = label
	self.mainPanel:addChild(volumeCtl)

--[[
	local speaker = ISSpeakerButton:new(volumeCtl:getRight() + 10, volumeCtl.y, h, h, self.onPlaySound, { self, volumeCtl })
	speaker:initialise()
	self.mainPanel:addChild(speaker)
	speaker.onMouseDown = ISSpeakerButton.onMouseUp
	speaker.onMouseUp = DummyMouseUp
	speaker.label = label
	speaker.control = volumeCtl
	volumeCtl.speaker = speaker
--]]
	self.mainPanel:insertNewLineOfButtons(volumeCtl)

	self.addY = self.addY + math.max(fontHgt, h) + 6
	return volumeCtl
end

function ISGameSounds:onMouseWheel(del)
	local panel = self.tabs:getActiveView()
	panel:setYScroll(panel:getYScroll() - (del * 40))
	return true
end

function ISGameSounds:addPage(name)
	self.mainPanel = MainPanel:new(0, 48, self:getWidth(), self:getHeight() - (48 * 2) - self.tabs.tabHeight)
	self.mainPanel:initialise()
	self.mainPanel:instantiate()
	self.mainPanel:setAnchorRight(true)
	self.mainPanel:setAnchorLeft(true)
	self.mainPanel:setAnchorTop(true)
	self.mainPanel:setAnchorBottom(true)
	self.mainPanel:setScrollChildren(true)
	self.mainPanel.javaObject:setRenderClippedChildren(false)
	self.mainPanel:addScrollBars()

	-- Show this only when a sound is playing.
	local speaker = ISSpeakerButton:new(0, 0, 20, 20, self.onStopSound, self)
	speaker.isMute = true
	speaker:initialise()
	speaker.onMouseDown = ISSpeakerButton.onMouseUp
	speaker.onMouseUp = function(x,y) end
	self.mainPanel:addChild(speaker)
	speaker:setVisible(false)
	self.mainPanel.stopSound = speaker

	self.tabs:addView(name, self.mainPanel)
end

function ISGameSounds:createChildren()
	self.tabs = ISTabPanel:new(0, 48, self.width, self.height - 48 * 2)
	self.tabs:initialise()
	self.tabs:setAnchorBottom(true)
	self.tabs:setAnchorRight(true)
	self.tabs.target = self
	self.tabs:setEqualTabWidth(false)
	self.tabs.tabPadX = 40
	self.tabs:setCenterTabs(true)
	self:addChild(self.tabs)

	self.maxLabelWidth = 0
	local categories = GameSounds.getCategories()
	for i=1,categories:size() do
		local tabName = categories:get(i-1)
		tabName = getTextOrNull("GameSound_Category_"..tabName) or tabName
		self:addPage(tabName)

		local splitpoint = self:getWidth() / 2.5
		local y = 0
		self.addY = 8
		local comboWidth = 400 + 8 * 2

		local sounds = GameSounds.getSoundsInCategory(categories:get(i-1))
		for i=1,sounds:size() do
			local gameSound = sounds:get(i-1)
			local volume = gameSound:getUserVolume()
			local label = getTextOrNull("GameSound_"..gameSound:getName()) or gameSound:getName()
			local control = self:addVolumeControl(splitpoint, y, comboWidth, 20, label, volume, self, self.onVolumeChanged)
			control.gameSound = gameSound
			-- Make all labels the same width.
			self.maxLabelWidth = math.max(self.maxLabelWidth, getTextManager():MeasureStringX(UIFont.Small, label))
		end

		self.mainPanel:setScrollHeight(self.addY)
	end

	local buttonHgt = math.max(FONT_HGT_SMALL + 3 * 2, 25)
	local buttonWid = 100
	self.buttonClose = ISButton:new(self.width / 2 - buttonWid / 2, self.tabs:getBottom() + 10, buttonWid, buttonHgt, getText("UI_btn_close"), self, self.onClose)
	self.buttonClose:initialise()
	self.buttonClose.anchorTop = false
	self.buttonClose.anchorBottom = true
	self:addChild(self.buttonClose)

	if getDebug() then
		self.buttonReload = ISButton:new(0, self.buttonClose.y, buttonWid, buttonHgt, getText("GameSound_ButtonReload"), self, self.onReload)
		self.buttonReload:initialise()
		self.buttonReload.anchorLeft = false
		self.buttonReload.anchorTop = false
		self.buttonReload.anchorRight = true
		self.buttonReload.anchorBottom = true
		self.buttonReload.tooltip = getText("GameSound_ButtonReload_tt")
		self:addChild(self.buttonReload)
		self.buttonReload:setWidthToTitle(buttonWid)
		self.buttonReload:setX(self.width - 20 - self.buttonReload.width)
	end
end

function ISGameSounds:onVolumeChanged(control, volume)
	if not control.gameSound then return end -- inside addVolumeControl()
	control.gameSound:setUserVolume(volume)
	self.volumesChanged = true
end

function ISGameSounds.onPlaySound(args)
	if not MainScreen.instance.inGame then
		getSoundManager():StopMusic()
	end
	local self = args[1]
	local gameSound = args[2].gameSound
--	getSoundManager():PlaySound(gameSound:getName(), false, 1.0)
	self:onStopSound()
	GameSounds.previewSound(gameSound:getName())
	self.previewControl = args[2]
end

function ISGameSounds:onStopSound()
	if self.previewControl then
		GameSounds.stopPreview()
		self.previewControl.speaker.isMute = false
		self.previewControl = nil
	end
end

function ISGameSounds:onClose()
	self:onStopSound()
	if self.volumesChanged then
		GameSounds.saveINI()
		self.volumesChanged = false
	end
	self:setVisible(false)
	MainOptions.instance:setVisible(true, self.joyfocus)
end

function ISGameSounds:onReload()
	reloadSoundFiles()
	for _,child in pairs(self:getChildren()) do
		self:removeChild(child)
	end
	self:createChildren()
end

function ISGameSounds:onReturnToGame()
	self:onStopSound()
	if self.volumesChanged then
		GameSounds.saveINI()
		self.volumesChanged = false
	end
end

function ISGameSounds:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData)
	local panel = self.tabs:getActiveView()
	joypadData.focus = panel
	updateJoypadFocus(joypadData)
end

function ISGameSounds:onJoypadBeforeDeactivate(joypadData)
	-- The focus is actually in one of the tabs (MainPanel)
	self.buttonClose:clearJoypadButton()
	self.joyfocus = nil
end

function ISGameSounds:new(x, y, width, height)
	local o = ISPanelJoypad.new(self, x, y, width, height)
	o.backgroundColor = {r=0, g=0, b=0, a=0.9}
	o.borderColor = {r=1, g=1, b=1, a=0.2}
	o.anchorRight = true
	o.anchorBottom = true
	return o
end

