--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require('ISUI/ISScrollingListBox')
require('Vehicles/ISUI/ISUI3DScene')

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

AnimationClipViewer = ISPanel:derive("AnimationClipViewer")

-----

AnimationClipViewer_ListBox = ISScrollingListBox:derive("AnimationClipViewer_ListBox")
local ListBox = AnimationClipViewer_ListBox

function ListBox:prerender()
	ISScrollingListBox.prerender(self)
	local clipName = self.items[self.selected] and self.items[self.selected].text
	if clipName and (clipName ~= self.selectedClipName) then
		self.selectedClipName = clipName
		self.parent.scene.javaObject:fromLua2("setCharacterAnimationClip", "character1", clipName)
	end
end

function ListBox:doDrawItem(y, item, alt)
    if y + self:getYScroll() + self.itemheight < 0 or y + self:getYScroll() >= self.height then
        return y + self.itemheight
    end
    return ISScrollingListBox.doDrawItem(self, y, item, alt)
end

function ListBox:onMouseDown(x, y)
	ISScrollingListBox.onMouseDown(self, x, y)
end

function ListBox:indexOf(text)
	for i,item in ipairs(self.items) do
		if item.text == text then
			return i
		end
	end
	return -1
end

function ListBox:new(x, y, width, height)
	local o = ISScrollingListBox.new(self, x, y, width, height)
	return o
end

-----

AnimationClipViewer_OptionsPanel = ISPanel:derive("AnimationClipViewer_OptionsPanel")
local OptionsPanel = AnimationClipViewer_OptionsPanel

function OptionsPanel:createChildren()
	local tickBox = ISTickBox:new(10, 10, 300, 500, "", self, self.onTickBox, option)
	tickBox:initialise()
	self:addChild(tickBox)
	local gameState = getAnimationViewerState()
	for i=1,gameState:getOptionCount() do
		local option = gameState:getOptionByIndex(i-1)
		tickBox:addOption(option:getName(), option)
		tickBox:setSelected(i, option:getValue())
	end
	tickBox:setWidthToFit()
	self.tickBox = tickBox
end

function OptionsPanel:onTickBox(index, selected)
	local option = self.tickBox.optionData[index]
	option:setValue(selected)
	if option:getName() == "DrawGrid" then
		self.parent.scene.javaObject:fromLua1("setDrawGrid", selected)
	end
	if option:getName() == "Isometric" then
		self.parent:resetView()
	end
	if option:getName() == "UseDeferredMovement" then
		self.parent.scene.javaObject:fromLua2("setCharacterUseDeferredMovement", "character1", selected)
	end
end

function OptionsPanel:onMouseDownOutside(x, y)
	if self:isMouseOver() then return end
	self:setVisible(false)
--	self:removeFromUIManager()
end

function OptionsPanel:new(x, y, width, height)
	local o = ISPanel.new(self, x, y, width, height)
	o.backgroundColor.a = 0.8
	return o
end

-----

AnimationClipViewer_Scene = ISUI3DScene:derive("AnimationClipViewer_Scene")
local Scene = AnimationClipViewer_Scene

function Scene:prerenderEditor()
	self.javaObject:fromLua1("setGizmoVisible", "none")
	self.javaObject:fromLua1("setGizmoOrigin", "none")
	if not self.zeroVector then self.zeroVector = Vector3f.new() end
	self.javaObject:fromLua1("setGizmoPos", self.zeroVector)
	self.javaObject:fromLua1("setGizmoRotate", self.zeroVector)
	self.javaObject:fromLua0("clearAABBs")
	self.javaObject:fromLua0("clearAxes")
	self.javaObject:fromLua1("setSelectedAttachment", nil)
end

function Scene:prerender()
	ISUI3DScene.prerender(self)
end

function Scene:onMouseDown(x, y)
	ISUI3DScene.onMouseDown(self, x, y)
	local gameState = getAnimationViewerState()
	self.rotate = not isKeyDown(Keyboard.KEY_LSHIFT)
end

function Scene:onMouseMove(dx, dy)
	if self.rotate then
		local current = self.javaObject:fromLua0("getViewRotation")
		local rx = current:x() + 0
		local ry = current:y() + dx / 2
		local rz = current:z() + 0
		if getAnimationViewerState():getBoolean("Isometric") then
			rx = 30
			rz = 0
		end
		self.javaObject:fromLua3("setViewRotation", rx, ry, rz)
		return
	end
	ISUI3DScene.onMouseMove(self, dx, dy)
end

function Scene:onMouseUp(x, y)
	ISUI3DScene.onMouseUp(self, x, y)
	self.rotate = false
end

function Scene:onMouseUpOutside(x, y)
	self:onMouseUp()
end

function Scene:onRightMouseDown(x, y)
end

function Scene:new(x, y, width, height)
	local o = ISUI3DScene.new(self, x, y, width, height)
	return o
end

-----

AnimationClipViewer_Timeline = ISPanel:derive("AnimationClipViewer_Timeline")
local Timeline = AnimationClipViewer_Timeline

function Timeline:render()
	ISPanel.render(self)
	local scene = self.parent.parent.scene
	local time = scene.javaObject:fromLua1("getCharacterAnimationTime", "character1")
	local duration = scene.javaObject:fromLua1("getCharacterAnimationDuration", "character1")
	if not time or not duration then return end

	if self.parent.parent.listBox.selectedClipName ~= self.selectedClipName then
		self.selectedClipName = self.parent.parent.listBox.selectedClipName
		self.keyframeTimes = scene.javaObject:fromLua2("getCharacterAnimationKeyframeTimes", "character1", self.keyframeTimes)
	end
	local times = self.keyframeTimes
	for i=1,times:size() do
		self:drawRect(self.width * times:get(i-1) / duration, 0, 1, self.height, 1.0, 0.5, 0.5, 0.5)
	end

	self:drawRect(self.width * time / duration, 0, 1, self.height, 1.0, 1.0, 1.0, 1.0)
end

function Timeline:onMouseDown(x, y)
	local scene = self.parent.parent.scene
	local duration = scene.javaObject:fromLua1("getCharacterAnimationDuration", "character1")
	if not duration then return end
	local xFraction = x / self.width
	self.parent.parent.animate = false
	scene.javaObject:fromLua2("setCharacterAnimate", "character1", false)
	scene.javaObject:fromLua2("setCharacterAnimationTime", "character1", duration * xFraction)
	self.dragging = true
	self:setCapture(true)
end

function Timeline:onMouseMove(dx, dy)
	if not self.dragging then return end
	local scene = self.parent.parent.scene
	local duration = scene.javaObject:fromLua1("getCharacterAnimationDuration", "character1")
	if not duration then return end
	local xFraction = self:getMouseX() / self.width
	xFraction = math.max(xFraction, 0.0)
	xFraction = math.min(xFraction, 1.0)
	scene.javaObject:fromLua2("setCharacterAnimationTime", "character1", duration * xFraction)
end

function Timeline:onMouseMoveOutside(dx, dy)
	self:onMouseMove(dx, dy)
end

function Timeline:onMouseUp(x, y)
	self.dragging = false
	self:setCapture(false)
end

function Timeline:onMouseUpOutside(x, y)
	self.dragging = false
	self:setCapture(false)
end

function Timeline:new(x, y, width, height)
	local o = ISPanel.new(self, x, y, width, height)
	o.borderColor.a = 0.0
	return o
end

-----

function AnimationClipViewer:createChildren()
	local gameState = getAnimationViewerState()

	self.scene = Scene:new(0, 0, self.width, self.height)
	self.scene:initialise()
	self.scene:instantiate()
	self.scene:setAnchorRight(true)
	self.scene:setAnchorBottom(true)
	self:addChild(self.scene)

	self:resetView()

	if gameState:getBoolean("Isometric") then
		self.scene.javaObject:fromLua2("dragView", 0, self.height / 4)
	else
		self.scene.javaObject:fromLua2("dragView", 0, self.height / 3)
	end

	self.scene.javaObject:fromLua1("setMaxZoom", 20)
	self.scene.javaObject:fromLua1("setZoom", 7)
	self.scene.javaObject:fromLua1("setGizmoScale", 1.0 / 5.0)

	self.scene.javaObject:fromLua1("setDrawGrid", gameState:getBoolean("DrawGrid"))
	self.scene.javaObject:fromLua1("setDrawGridAxes", true)
	self.scene.javaObject:fromLua1("setGridPlane", "XZ")
	
	self.scene.javaObject:fromLua1("createCharacter", "character1")
	self.scene.javaObject:fromLua2("setCharacterAlpha", "character1", 1.0)
	self.scene.javaObject:fromLua2("setCharacterAnimate", "character1", self.animate)
	self.scene.javaObject:fromLua2("setCharacterAnimSet", "character1", "player-editor")
	self.scene.javaObject:fromLua2("setCharacterState", "character1", "runtime")
	self.scene.javaObject:fromLua2("setCharacterClearDepthBuffer", "character1", false)
	self.scene.javaObject:fromLua2("setCharacterShowBones", "character1", false)
	self.scene.javaObject:fromLua2("setCharacterUseDeferredMovement", "character1", gameState:getBoolean("UseDeferredMovement"))
	self.scene.javaObject:fromLua2("setObjectVisible", "character1", true)

	self:createToolbar()

	local bottomH = 100

	self.filter = ISTextEntryBox:new("", 10, 10, 250, FONT_HGT_MEDIUM + 2 * 2)
	self.filter.font = UIFont.Medium
	self:addChild(self.filter)
	self.filter:setClearButton(true)

	local listY = self.filter:getBottom() + 4
	local listBox = ListBox:new(10, listY, 250, self.height - bottomH - 10 - listY)
	listBox:setAnchorBottom(true)
	self:addChild(listBox)
	listBox:setFont(UIFont.Small, 2)
	self.listBox = listBox

	self:setClipList()

	self.bottomPanel = ISPanel:new(0, self.height - bottomH, self.width, bottomH)
	self.bottomPanel:setAnchorTop(false)
	self.bottomPanel:setAnchorLeft(false)
	self.bottomPanel:setAnchorRight(false)
	self.bottomPanel:setAnchorBottom(true)
	self.bottomPanel:noBackground()
	self:addChild(self.bottomPanel)

	local timeline = Timeline:new(10, 0, self.width - 10 * 2, 50)
	self.bottomPanel:addChild(timeline)
	self.timeline = timeline

	self.speedScale = ISSliderPanel:new(self.width / 2 - 400 / 2, self.bottomPanel.y - 30, 400, 20, self, self.onSpeedScaleChanged)
	self.speedScale.anchorTop = false
	self.speedScale.anchorBottom = true
	self.speedScale:setValues(0.0, 5.0, 0.1, 1.0)
	self.speedScale:setCurrentValue(1.0, true)
	self:addChild(self.speedScale)

	local buttonHgt = FONT_HGT_MEDIUM + 8

	local button2 = ISButton:new(10, self.bottomPanel.height - 10 - buttonHgt, 80, buttonHgt, "EXIT", self, self.onExit)
	self.bottomPanel:addChild(button2)
end

function AnimationClipViewer:createToolbar()
	local toolHgt = 30
	self.toolBar = ISPanel:new(0, 10, 300, toolHgt)
	self.toolBar:noBackground()
	self:addChild(self.toolBar)

	local button = ISButton:new(0, 0, 60, toolHgt, "OPTIONS", self, self.onOptions)
	self.toolBar:addChild(button)
	self.buttonOptions = button

	self.toolBar:setWidth(button:getRight())
	self.toolBar:setX(self.width / 2 - self.toolBar.width / 2)

	self.optionsPanel = OptionsPanel:new(0, 0, 300, 400)
	self.optionsPanel:setVisible(false)
	self:addChild(self.optionsPanel)
end

function AnimationClipViewer:onSpeedScaleChanged(speed, slider)
end

function AnimationClipViewer:resetView()
	self.scene:setView("UserDefined")
	local gameState = getAnimationViewerState()
	if gameState:getBoolean("Isometric") then
		self.scene.javaObject:fromLua3("setViewRotation", 30.0, 45.0, 0.0)
		self.scene.javaObject:fromLua1("setGridPlane", "XZ")
	else
		self.scene.javaObject:fromLua3("setViewRotation", 0.0, 0.0, 0.0)
		self.scene.javaObject:fromLua1("setGridPlane", "XY")
	end
end

function AnimationClipViewer:setClipList()
	self.listBox:clear()
	local filterText = string.trim(self.filter:getInternalText())
	local clips = getAnimationViewerState():fromLua0("getClipNames")
	for i=1,clips:size() do
		local clipName = clips:get(i-1)
		if string.contains(string.lower(clipName), filterText) then
			self.listBox:addItem(clipName, clipName)
		end
	end
end

function AnimationClipViewer:onResolutionChange(oldw, oldh, neww, newh)
	self:setWidth(neww)
	self:setHeight(newh)
	self.bottomPanel:setX(self.width / 2 - self.bottomPanel.width / 2)

	self.speedScale:setX(self.width / 2 - self.speedScale.width / 2)
end

function AnimationClipViewer:update()
	ISPanel.update(self)
	if self.width ~= getCore():getScreenWidth() or self.height ~= getCore():getScreenHeight() then
		self:onResolutionChange(self.width, self.height, getCore():getScreenWidth(), getCore():getScreenHeight())
	end

	local filterText = string.trim(self.filter:getInternalText())
	if self.filterText ~= filterText then
		self.filterText = filterText
		self:setClipList()
	end

	local speed = self.speedScale:getCurrentValue()
	self.scene.javaObject:fromLua2("setCharacterAnimationSpeed", "character1", speed)
end

function AnimationClipViewer:prerender()
	ISPanel.prerender(self)
	self.scene:prerenderEditor()

	self.scene.javaObject:fromLua6("addAxis", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
end

function AnimationClipViewer:render()
	ISPanel.render(self)
	local time = self:getCurrentTime()
	local duration = self:getDuration()
	
	local text = string.format("SECONDS:   %.2f / %.2f", time, duration)
	self:drawText(text, self.width / 2 - 150, self.height - 30, 1.0, 1.0, 1.0, 1.0, UIFont.Medium)

	text = string.format("FRACTION:   %.2f", time / duration)
	self:drawText(text, self.width / 2 + 50, self.height - 30, 1.0, 1.0, 1.0, 1.0, UIFont.Medium)

	local frame = self:getCurrentFrame()
	local lastFrame = self:getLastFrame()
	text = string.format("FRAME:   %d / %d", math.min(frame, lastFrame), lastFrame)
	self:drawText(text, self.width / 2 + 200, self.height - 30, 1.0, 1.0, 1.0, 1.0, UIFont.Medium)

	text = string.format("SpeedScale:    %.2f", self.speedScale:getCurrentValue())
	self:drawText(text, self.speedScale.x, self.speedScale.y - 8 - FONT_HGT_MEDIUM, 1.0, 1.0, 1.0, 1.0, UIFont.Medium)
end

function AnimationClipViewer:onKeyPress(key)
	local fps = self:getFPS()
	if key == Keyboard.KEY_LEFT then
		local time = self:getCurrentTime()
		local frame = self:getCurrentFrame() - 1
		if time * fps - frame > 0.1 then frame = frame + 1 end
		self.scene.javaObject:fromLua2("setCharacterAnimationTime", "character1", math.max(frame - 1, 0) / fps)
	end
	if key == Keyboard.KEY_RIGHT then
		local time = self:getCurrentTime()
		local frame = self:getCurrentFrame() - 1
		local lastFrame = self:getLastFrame()
		self.scene.javaObject:fromLua2("setCharacterAnimationTime", "character1", math.min(frame + 1, lastFrame - 1) / fps)
	end
	if key == Keyboard.KEY_SPACE then
		self.animate = not self.animate
		self.scene.javaObject:fromLua2("setCharacterAnimate", "character1", self.animate)
	end
end

function AnimationClipViewer:getFPS()
	return 30
end

function AnimationClipViewer:getDuration()
	return self.scene.javaObject:fromLua1("getCharacterAnimationDuration", "character1") or 1.0
end

function AnimationClipViewer:getCurrentTime()
	return self.scene.javaObject:fromLua1("getCharacterAnimationTime", "character1") or 0.0
end

function AnimationClipViewer:getCurrentFrame()
	local fps = self:getFPS()
	local time = self:getCurrentTime()
	return math.floor(time * fps + 0.01) + 1
end

function AnimationClipViewer:getLastFrame()
	local fps = self:getFPS()
	local duration = self:getDuration()
	return luautils.round(duration * fps) + 1
end

function AnimationClipViewer:onOptions()
	self.optionsPanel:setX(self.buttonOptions:getAbsoluteX())
	self.optionsPanel:setY(self.buttonOptions:getAbsoluteY() + self.buttonOptions:getHeight())
	self.optionsPanel:setVisible(true)
end

function AnimationClipViewer:onExit(button, x, y)
	getAnimationViewerState():fromLua0("exit")
end

-- Called from Java
function AnimationClipViewer:showUI()
end

function AnimationClipViewer:new(x, y, width, height)
	local o = ISPanel.new(self, x, y, width, height)
	o:setAnchorRight(true)
	o:setAnchorBottom(true)
	o:noBackground()
	o:setWantKeyEvents(true)
	o.animate = true
	getAnimationViewerState():setTable(o)
	return o
end

-- Called from Java
function AnimationViewerState_InitUI()
	local UI = AnimationClipViewer:new(0, 0, getCore():getScreenWidth(), getCore():getScreenHeight())
	AnimationViewerState_UI = UI
	UI:setVisible(true)
	UI:addToUIManager()
end

