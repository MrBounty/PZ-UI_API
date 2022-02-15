--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require('ISUI/ISScrollingListBox')
require('Vehicles/ISUI/ISUI3DScene')

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

AttachmentEditorUI = ISPanel:derive("AttachmentEditorUI")

AttachmentEditorUI_Scene = ISUI3DScene:derive("AttachmentEditorUI_Scene")
AttachmentEditorUI_SwitchView = ISUI3DScene:derive("AttachmentEditorUI_SwitchView")
local Scene = AttachmentEditorUI_Scene
local SwitchView = AttachmentEditorUI_SwitchView

function SwitchView:prerender()
	if self:isMouseOver() or (self:getView() == self.editor.scene:getView()) then
		self.borderColor.r = 0.8
		self.borderColor.g = 0.8
		self.borderColor.b = 0.8
	else
		self.borderColor.r = 0.4
		self.borderColor.g = 0.4
		self.borderColor.b = 0.4
	end
	ISUI3DScene.prerender(self)
end

function SwitchView:onMouseDown(x, y)
	self.editor.prevView = self:getView()
	self.editor.scene:setView(self:getView())
end

function SwitchView:onMouseMove(dx, dy)
	if self.editor.mouseOverView ~= self then
		if self.editor.mouseOverView then
			self.editor.mouseOverView:onMouseMoveOutside(-1, -1)
		end
		self.editor.mouseOverView = self
		self.editor.prevView = self.editor.scene:getView()
		self.editor.scene:setView(self:getView())
	end
end

function SwitchView:onMouseMoveOutside(dx, dy)
	if self.editor.mouseOverView == self then
		self.editor.mouseOverView = nil
		self.editor.scene:setView(self.editor.prevView)
		self.editor.prevView = nil
	end
end

function SwitchView:onMouseWheel(del)
	return true
end

function SwitchView:new(editor, x, y, width, height)
	local o = ISUI3DScene.new(self, x, y, width, height)
	o.editor = editor
	return o
end

-----

local function vectorComponentToString(n, scale)
	return tostring(round(n * scale, 3))
end

local function vectorToString(v, scale)
	return vectorComponentToString(v:x(), scale) .. " " .. vectorComponentToString(v:y(), scale) .. " " .. vectorComponentToString(v:z(), scale)
end

local function drawVector(ui, label, x, y, vx, vy, vz)
	ui:drawText(label, x, y, 1, 1, 1, 1, UIFont.Small)
	x = x + getTextManager():MeasureStringX(UIFont.Small, label)

	local dx = getTextManager():MeasureStringX(UIFont.Small, "99.9999")
	str = vectorComponentToString(vx, 1)
	ui:drawText(str, x, y, 1, 0, 0, 1, UIFont.Small)
	x = x + dx

	str = vectorComponentToString(vy, 1)
	ui:drawText(str, x, y, 0, 1, 0, 1, UIFont.Small)
	x = x + dx

	str = vectorComponentToString(vz, 1)
	ui:drawText(str, x, y, 0, 0.5, 1, 1, UIFont.Small)
end

local function alignVectorToGrid(v, gridMult)
	local vx = math.floor(v:x() * gridMult + 0.5) / gridMult
	local vy = math.floor(v:y() * gridMult + 0.5) / gridMult
	local vz = math.floor(v:z() * gridMult + 0.5) / gridMult
	v:setComponent(0, vx)
	v:setComponent(1, vy)
	v:setComponent(2, vz)
	return v
end

-----

AttachmentEditorUI_ListBox = ISScrollingListBox:derive("AttachmentEditorUI_ListBox")
local ListBox = AttachmentEditorUI_ListBox

function ListBox:prerender()
	ISScrollingListBox.prerender(self)
	if self:isMouseOver() and isKeyPressed(Keyboard.KEY_A) then
		if self:getSelectedCount() == #self.items then
			self:clearSelection()
		elseif self.selectionMode == "multi" then
			for _,item in ipairs(self.items) do
				item.selected = true
			end
		end
	end
end

function ListBox:onMouseDown(x, y)
	if #self.items == 0 then return end
	local row = self:rowAt(x, y)
	if row == -1 then
		self:clearSelection()
		return
	end
	if self.selectionMode == "single" then
		if not self.items[row].selected then
			self:clearSelection()
		end
	elseif not isShiftKeyDown() then
		self:clearSelection()
	end
	self.items[row].selected = not self.items[row].selected
end

function ListBox:clearSelection()
	for _,item in ipairs(self.items) do
		item.selected = false
	end
end

function ListBox:setSelectedRow(row)
	self:clearSelection()
	if self.items[row] then self.items[row].selected = true end
end

function ListBox:setSelectedRows(rows)
	self:clearSelection()
	for _,row in ipairs(rows) do
		local item = self.items[row]
		if item then item.selected = true end
	end
end

function ListBox:getSelectedItems()
	local selected = {}
	for _,item in ipairs(self.items) do
		if item.selected then
			table.insert(selected, item)
		end
	end
	return selected
end

function ListBox:getSelectedCount()
	local selected = 0
	for _,item in ipairs(self.items) do
		if item.selected then
			selected = selected + 1
		end
	end
	return selected
end

function ListBox:iteratorSelected()
	return ipairs(self:getSelectedItems())
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
	o.selectionMode = "multi"
	return o
end

-----

AttachmentEditorUI_EditPanel = ISPanel:derive("AttachmentEditorUI_EditPanel")
local EditPanel = AttachmentEditorUI_EditPanel

function EditPanel:updateEditor()
end

function EditPanel:prerenderEditor()
end

function EditPanel:toUI()
end

function EditPanel:onSceneMouseDown(x, y)
end

function EditPanel:onGizmoStart()
end

function EditPanel:onGizmoChanged(delta)
end

function EditPanel:onGizmoAccept()
end

function EditPanel:onGizmoCancel()
end

function EditPanel:createList(x, y, w, h)
	local list = ListBox:new(x or 0, y or 0, w or self.width, h or self.height)
	self:addChild(list)
	return list
end

function EditPanel:java0(func)
	return self.parent.scene.javaObject:fromLua0(func)
end

function EditPanel:java1(func, arg0)
	return self.parent.scene.javaObject:fromLua1(func, arg0)
end

function EditPanel:java2(func, arg0, arg1)
	return self.parent.scene.javaObject:fromLua2(func, arg0, arg1)
end

function EditPanel:java3(func, arg0, arg1, arg2)
	return self.parent.scene.javaObject:fromLua3(func, arg0, arg1, arg2)
end

function EditPanel:java4(func, arg0, arg1, arg2, arg3)
	return self.parent.scene.javaObject:fromLua4(func, arg0, arg1, arg2, arg3)
end

function EditPanel:java6(func, arg0, arg1, arg2, arg3, arg4, arg5)
	return self.parent.scene.javaObject:fromLua6(func, arg0, arg1, arg2, arg3, arg4, arg5)
end

function EditPanel:java9(func, arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8)
	return self.parent.scene.javaObject:fromLua9(func, arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8)
end

function EditPanel:new(x, y, width, height)
	local o = ISPanel.new(self, x, y, width, height)
	return o
end

-----

AttachmentEditorUI_EditAttachment = EditPanel:derive("AttachmentEditorUI_EditAttachment")
local EditAttachment = AttachmentEditorUI_EditAttachment

function EditAttachment:createChildren()
	local buttonPadY = 4
	local buttonHgt = FONT_HGT_MEDIUM + 8

	local comboHeight = FONT_HGT_MEDIUM + 3 * 2
	local combo = ISComboBox:new(0, 0, self.width, comboHeight, self, self.onComboAddModel)
	combo.noSelectionText = "ADD MODEL"
	combo:setEditable(true)
	self:addChild(combo)
	self.comboAddModel = combo

	local scripts = getScriptManager():getAllModelScripts()
	local sorted = {}
	for i=1,scripts:size() do
		local script = scripts:get(i-1)
		table.insert(sorted, script:getFullType())
		if script:getFullType() == "Base.FemaleBody" then
			self.femaleBodyScript = script
		end
		if script:getFullType() == "Base.MaleBody" then
			self.maleBodyScript = script
		end
	end
	table.sort(sorted)
	for _,scriptName in ipairs(sorted) do
		combo:addOption(scriptName)
	end
	combo.selected = 0 -- ADD MODEL

	local itemHeight = FONT_HGT_SMALL + 2
	self.list = self:createList(0, combo:getBottom() + buttonPadY, self.width, itemHeight * 6)
	self.list.itemheight = itemHeight
	self.list.doDrawItem = function(self, y, item, alt) return self.parent.doDrawItem(self, y, item, alt) end
	self.list.onRightMouseDown = function(self, x, y) return self.parent.onRightMouseDownList1(self.parent, x, y) end
	self.list.selectionMode = "single"

	local button0 = ISButton:new(10, self.list:getBottom() + buttonPadY, self.width - 10 * 2, buttonHgt, "REMOVE FROM SCENE", self, self.onRemoveModel)
	button0:setEnable(false)
	self:addChild(button0)
	self.buttonRemoveModel = button0

	self.comboPlayer = ISComboBox:new(0, button0:getBottom() + 10, self.width, comboHeight, self, self.onComboPlayerModel)
	self.comboPlayer.noSelectionText = "PLAYER MODEL"
	self:addChild(self.comboPlayer)
	self.comboPlayer:addOption("None")
	self.comboPlayer:addOption("Female")
	self.comboPlayer:addOption("Male")
	self.comboPlayer.selected = 0 -- PLAYER MODEL

	self.list2 = self:createList(0, self.comboPlayer:getBottom() + 20, self.width, 24 * 6)
	self.list2.doDrawItem = self.doDrawItem2
	self.list2.itemheight = FONT_HGT_SMALL * 3 + 2
	self.list2.selectionMode = "single"

	self.belowList = ISPanel:new(0, self.list2:getBottom() + buttonPadY, self.width, 100)
	self.belowList:noBackground()
	self:addChild(self.belowList);

	self.nameEntry = ISTextEntryBox:new("", 10, 0, self.width - 10 * 2, buttonHgt)
	self.nameEntry.font = UIFont.Medium
	self.nameEntry.onCommandEntered = function(self) self.parent.parent:onNameEntered() end
	self.belowList:addChild(self.nameEntry)

	local button1 = ISButton:new(10, self.nameEntry:getBottom() + buttonPadY, (self.width - 10 * 3) / 2, buttonHgt, "NEW", self, self.onNewAttachment)
	self.belowList:addChild(button1)
	button1:setEnable(false)
	self.buttonNewAttachment = button1

	local button2 = ISButton:new(button1:getRight() + 10, button1:getY(), button1.width, buttonHgt, "DELETE", self, self.onDeleteAttachment)
	button2:setEnable(false)
	self.belowList:addChild(button2)
	self.buttonDeleteAttachment = button2

	self.gizmo = "translate"
	local button3 = ISButton:new(10, button2:getBottom() + buttonPadY, self.width - 20, buttonHgt, "TRANSLATE", self, self.onToggleGizmo)
	self.belowList:addChild(button3)
	self.button3 = button3

	self.transformMode = "Global"
	local button4 = ISButton:new(10, button3:getBottom() + buttonPadY, self.width - 20, buttonHgt, "GLOBAL", self, self.onToggleGlobalLocal)
	self.belowList:addChild(button4)
	self.button4 = button4

	self.belowList:setHeight(self.button4:getBottom())
	self:setHeight(self.belowList:getBottom())
end

function EditAttachment:doLayout()
	local top = self.list2:getY()
	local labelTop = self.parent:getHeight() - self.parent.bottomPanel:getHeight() - 30
	local bottom = labelTop - 20 - self.belowList.height - 4
	self.list2:setHeight(bottom - top)
	self.belowList:setY(self.list2:getBottom() + 4)
	self:setHeight(self.belowList:getBottom())
end

function EditAttachment:onComboAddModel()
	local scriptName = self.comboAddModel:getOptionText(self.comboAddModel.selected)
	for _,item in ipairs(self.list.items) do
		local modelScript = item.item
		if modelScript:getFullType() == scriptName then
			return
		end
	end
	self.comboAddModel.selected = 0 -- ADD MODEL
	self:java2("createModel", scriptName, scriptName)
	self:toUI()
end

function EditAttachment:onSceneMouseDown(x, y)
	local boneName = self:pickCharacterBone()
	if boneName == "" then
		self.selectedBone = nil
	else
		self.selectedBone = boneName
	end
end

function EditAttachment:onRemoveModel(button, x, y)
	for _,item in self.list:iteratorSelected() do
		local modelScript = item.item
		if not self:isBodyScript(modelScript) then
			self:java1("removeModel", modelScript:getFullType())
		end
	end
	self:toUI()
end

function EditAttachment:onComboPlayerModel()
	self:java2("setObjectVisible", "character1", self.comboPlayer.selected > 1)
	self:java2("setCharacterFemale", "character1", self.comboPlayer.selected == 2)
	self.comboPlayer.selected = 0 -- PLAYER MODEL
	self:toUI()
end

function EditAttachment:onNameEntered()
	local attach = self:getSelectedAttachments()[1]
	if not attach then return end
	local text = self.nameEntry:getInternalText():trim()
	if text == "" then
		self.nameEntry:setText(attach:getId())
		return
	end
	local attach2 = self.selectedModelScript:getAttachmentById(text)
	if attach2 then
		self.nameEntry:setText(attach:getId())
		return
	end
	attach:setId(text)
end

function EditAttachment:getUniqueAttachmentId(modelScript)
	for i=1,100 do
		local id = "attachment"..tostring(i)
		if not modelScript:getAttachmentById(id) then
			return id
		end
	end
	error "too many attachments"
end

function EditAttachment:isBodyScript(modelScript)
	if modelScript == nil then
		return false
	end
	return (modelScript == self.femaleBodyScript) or (modelScript == self.maleBodyScript)
end

function EditAttachment:onNewAttachment(button, x, y)
	local modelScript = self.selectedModelScript
	local id = self:getUniqueAttachmentId(modelScript)
	local attach = nil
	if self:isBodyScript(modelScript) then
		if not modelScript:getAttachmentById(self.selectedBone) then
			id = self.selectedBone
		end
		attach = modelScript:addAttachment(ModelAttachment.new(id))
		attach:setBone(self.selectedBone)
	else
		attach = self:java2("addAttachment", modelScript:getFullType(), id)
	end
	self.selectedModelScript = nil
	self:setSelectedModel(modelScript)
	self.list2:clearSelection()
	local index = self.list2:indexOf(id)
	self.list2.items[index].selected = true
	self.list2:ensureVisible(index)
end

function EditAttachment:onDeleteAttachment(button, x, y)
	for _,item in self.list:iteratorSelected() do
		local modelScript = item.item
		for _,item2 in self.list2:iteratorSelected() do
			if self:isBodyScript(modelScript) then
				modelScript:removeAttachment(item2.item)
			else
				self:java2("removeAttachment", modelScript:getFullType(), item2.item:getId())
			end
		end
	end
	self.selectedModelScript = nil
end

function EditAttachment:onToggleGizmo()
	if self.gizmo == "translate" then
		self.gizmo = "rotate"
		self.button3.title = "ROTATE"
	else
		self.gizmo = "translate"
		self.button3.title = "TRANSLATE"
	end
end

function EditAttachment:onToggleGlobalLocal()
	if self.transformMode == "Global" then
		self.transformMode = "Local"
		self.button4.title = "LOCAL"
	else
		self.transformMode = "Global"
		self.button4.title = "GLOBAL"
	end
end

function EditAttachment:doDrawItem(y, item, alt)
	local modelScript = item.item
	
	local x = 4
	local indent = 16

	local isMouseOver = self.mouseoverselected == item.index and not self:isMouseOverScrollBar()
	if item.selected then
		self:drawRect(0, y, self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
	elseif isMouseOver then
		self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.25, 1.0, 1.0, 1.0)
	end

	self:drawText(modelScript:getName(), x, y, 1, 1, 1, 1, UIFont.Small)
	y = y + FONT_HGT_SMALL

	self:drawRect(x, y, self.width - 4 * 2, 2, 1.0, 0.5, 0.5, 0.5)
	y = y + 2
	return y
end

function EditAttachment:doDrawItem2(y, item, alt)
	local attach = item.item
	
	local x = 4
	local indent = 16

	local isMouseOver = self.mouseoverselected == item.index and not self:isMouseOverScrollBar()
	if item.selected then
		self:drawRect(0, y, self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
	elseif isMouseOver then
		self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.25, 1.0, 1.0, 1.0)
	end

	local id = attach:getId()
	if attach:getBone() then
		id = id .. " (" .. attach:getBone() .. ")"
	end
	self:drawText(id, x, y, 1, 1, 1, 1, UIFont.Small)
	y = y + FONT_HGT_SMALL

	local offset = attach:getOffset()
	drawVector(self, "offset:   ", x + indent, y, offset:x(), offset:y(), offset:z())
	y = y + FONT_HGT_SMALL

	local rotate = attach:getRotate()
	drawVector(self, "rotate:   ", x + indent, y, rotate:x(), rotate:y(), rotate:z())
	y = y + FONT_HGT_SMALL

	self:drawRect(x, y, self.width - 4 * 2, 2, 1.0, 0.5, 0.5, 0.5)
	y = y + 2
	return y
end

function EditAttachment:onRightMouseDownList1(x, y)
	local row = self.list:rowAt(x, y)
	local item = self.list.items[row]
	if not item then return end
	local child = item.item
	local childName = self:isBodyScript(child) and "character1" or child:getFullType()
	
	local player = 0
	local context = ISContextMenu.get(player, self.list:getAbsoluteX() + x, self.list:getAbsoluteY() + y)
	context:removeFromUIManager()
	context:addToUIManager()
	local parentMenu = context:getNew(context)
	parentMenu:addOption("None", self, self.onSetObjectParent, childName, nil, nil, nil)
	for _,item in ipairs(self.list.items) do
		local parent = item.item
		if parent ~= child then
			local parentName = self:isBodyScript(parent) and "character1" or parent:getFullType()
			if parent:getAttachmentCount() > 0 then
				local attachmentMenu = context:getNew(parentMenu)
				attachmentMenu:addOption("<no attachment>", self, self.onSetObjectParent, childName, nil, parentName, nil)
				local attachments = {}
				for i=1,parent:getAttachmentCount() do
					local attach = parent:getAttachment(i-1)
					table.insert(attachments, attach)
				end
				table.sort(attachments, function(a,b) return not string.sort(a:getId(), b:getId()) end)
				for _,attach in ipairs(attachments) do
					attachmentMenu:addOption(attach:getId(), self, self.onSetObjectParent, childName, attach:getId(), parentName, attach:getId())
				end
				parentMenu:addSubMenu(parentMenu:addOption(parent:getFullType()), attachmentMenu)
			else
				parentMenu:addOption(parent:getFullType(), self, self.onSetObjectParent, childName, nil, parentName, nil)
			end
		end
	end
	context:addSubMenu(context:addOption("Set Parent"), parentMenu)
	local optionRotate = context:addOption("Auto-Rotate Y", self, self.onSetObjectAutoRotate, childName)
	context:setOptionChecked(optionRotate, self:java1("getObjectAutoRotate", childName))
end

function EditAttachment:onSetObjectParent(child, childAttachment, parent, parentAttachment)
	self:java4("setObjectParent", child, childAttachment, parent, parentAttachment)
end

function EditAttachment:onSetObjectAutoRotate(child)
	self:java2("setObjectAutoRotate", child, not self:java1("getObjectAutoRotate", child))
end

function EditAttachment:prerenderEditor()
	self.list2.doDrawItem = self.doDrawItem2
	
	self:pickCharacterBone()

	local mouseOver = self.list.items[self.list.mouseoverselected]
	local item = mouseOver or self.list:getSelectedItems()[1]
	if not item then
		self:setSelectedModel(nil)
		self:setSelectedAttachment(nil)
		self.buttonNewAttachment:setEnable(false)
		return
	end

	if mouseOver then
		self.parent.wroteScriptLabel:setName("Right-click for options")
		self.parent.wroteScriptTime = getTimestampMs() - 4950
	end

	local modelScript = item.item
	self:setSelectedModel(modelScript)

	local showGizmo = false

	if self:isBodyScript(modelScript) then
		if self.selectedBone then
			self:java3("setGizmoOrigin", "bone", "character1", self.selectedBone)
			self:java2("addBoneAxis", "character1", self.selectedBone)
			-- can't transform a bone
		else
			local bone = self:pickCharacterBone()
			if bone ~= "" then self:java2("setHighlightBone", "character1", bone) end
			self:java2("setGizmoOrigin", "character", "character1")
			-- Don't transform objects that are parented to others.
			if not self:java1("getObjectParent", "character1") then
				showGizmo = true
			end
		end
	else
		self:java2("setGizmoOrigin", "model", modelScript:getFullType())
		-- Don't transform objects that are parented to others.
		if not self:java1("getObjectParent", modelScript:getFullType()) then
			showGizmo = true
		end
	end

	local mouseOver2 = self.list2.items[self.list2.mouseoverselected]
	local attach = mouseOver2 and mouseOver2.item or self:getSelectedAttachments()[1]
	self:setSelectedAttachment(attach)

	if attach then
		self:java1("setGizmoPos", attach:getOffset())
		self:java1("setGizmoRotate", attach:getRotate())
		if self:isBodyScript(modelScript) then
			self:java3("setGizmoOrigin", "bone", "character1", attach:getBone())
		else
			local parentAttachName = self:java1("getObjectParentAttachment", modelScript:getFullType())
			if parentAttachName then
				local parentName = self:java1("getObjectParent", modelScript:getFullType())
				self:java4("setGizmoOrigin", "attachment", modelScript:getFullType(), parentName, parentAttachName)
				self:java1("setSelectedAttachment", attach:getId())
			end
		end
		self:java6("addAxis", attach:getOffset():x(), attach:getOffset():y(), attach:getOffset():z(),
			attach:getRotate():x(), attach:getRotate():y(), attach:getRotate():z())
		showGizmo = true
	end

	-- Transform the selected object when no attachment is selected.
	if showGizmo then
		self:java1("setGizmoVisible", self.gizmo)
		self:java1("setTransformMode", self.transformMode)
	end

	if self:isBodyScript(self.selectedModelScript) then
		self.buttonNewAttachment:setEnable(self.selectedBone ~= nil)
	else
		self.buttonNewAttachment:setEnable(true)
	end
end

function EditAttachment:setSelectedModel(modelScript)
	if self.selectedModelScript == modelScript then
		return
	end
	self.selectedModelScript = modelScript
	self.buttonRemoveModel:setEnable(modelScript ~= nil and not self:isBodyScript(modelScript))
	if not modelScript then
		self.list2:clear()
		return
	end
	local attachIds = {}
	for _,item in self.list2:iteratorSelected() do
		attachIds[item.item:getId()] = true
	end
	self.list2:clear()
	self.list2:setScrollHeight(0)
	for i=1,modelScript:getAttachmentCount() do
		local attach = modelScript:getAttachment(i-1)
		self.list2:addItem(attach:getId(), attach)
		if attachIds[attach:getId()] then
			self.list2.items[i].selected = true
		end
	end
	self.list2:sort()
	if self.list2:getSelectedCount() == 0 then
--		self.list2:setSelectedRow(1)
	end
end

function EditAttachment:setSelectedAttachment(attach)
	if self.selectedAttachment == attach then
		return
	end
	self.selectedAttachment = attach
	self.buttonDeleteAttachment:setEnable(attach ~= nil)
	if not attach then
		self.nameEntry:clear()
		return
	end
	self.nameEntry:setText(attach:getId())
end

function EditAttachment:pickCharacterBone()
	if not self:java1("isObjectVisible", "character1") then
		return ""
	end
	if not self:isBodyScript(self.selectedModelScript) then
		return ""
	end
	local boneName = self:java3("pickCharacterBone", "character1", self.parent.scene:getMouseX(), self.parent.scene:getMouseY())
	self.parent.wroteScriptTime = getTimestampMs() - 4950
	self.parent.wroteScriptLabel:setName(boneName)
	return boneName
end

function EditAttachment:toUI()
	EditPanel.toUI(self)
	self.list:clear()
	if self:java1("isObjectVisible", "character1") then
		self.list:addItem("PLAYER", self:java1("isCharacterFemale", "character1") and self.femaleBodyScript or self.maleBodyScript)
	end
	for i=1,self:java0("getModelCount") do
		local modelScript = self:java1("getModelScript", i-1)
		self.list:addItem(modelScript:getName(), modelScript)
	end
	self.list:setSelectedRow(1)
end

function EditAttachment:getSelectedAttachments()
	local selected = {}
	if self.selectedModelScript then
		local modelScript = self.selectedModelScript
		for _,item2 in self.list2:iteratorSelected() do
			local attach = modelScript:getAttachmentById(item2.item:getId())
			if attach then
				table.insert(selected, attach)
			end
		end
	end
	return selected
end

function EditAttachment:getSelectedAttachmentIds()
	local selected = {}
	for _,item2 in self.list2:iteratorSelected() do
		table.insert(selected, item2.item:getId())
	end
	return selected
end

function EditAttachment:onGizmoStart()
	self.originalOffset = {}
	self.originalRotate = {}
	if not self.selectedModelScript then
		return
	end
	-- When no attachment is selected, transform the selected object
	if self.list2:getSelectedCount() == 0 then
		local objectName = self:isBodyScript(self.selectedModelScript) and "character1" or self.selectedModelScript:getFullType()
		if self.gizmo == "translate" then
			local trans = Vector3f.new(self:java1("getObjectTranslation", objectName))
			self.originalOffset[self.selectedModelScript] = alignVectorToGrid(trans, self:java0("getGridMult"))
		end
		if self.gizmo == "rotate" then
			self.originalRotate[self.selectedModelScript] = Vector3f.new(self:java1("getObjectRotation", objectName))
		end
		return
	end
	for _,attach in ipairs(self:getSelectedAttachments()) do
		if self.gizmo == "translate" then
			self.originalOffset[attach] = alignVectorToGrid(Vector3f.new(attach:getOffset()), self:java0("getGridMult"))
		end
		if self.gizmo == "rotate" then
			self.originalRotate[attach] = Vector3f.new(attach:getRotate())
		end
	end
end

function EditAttachment:onGizmoChanged(delta)
	if self.parent.scene.gizmoAxis == "None" then return end
	if not self.selectedModelScript then
		return
	end
	if self.list2:getSelectedCount() == 0 then
		local objectName = self:isBodyScript(self.selectedModelScript) and "character1" or self.selectedModelScript:getFullType()
		if self.gizmo == "translate" then
			self:java1("getObjectTranslation", objectName):set(self.originalOffset[self.selectedModelScript]):add(delta)
		end
		if self.gizmo == "rotate" then
			local rotation = self:java1("getObjectRotation", objectName)
			self:java2("applyDeltaRotation", rotation:set(self.originalRotate[self.selectedModelScript]), delta)
		end
		return
	end
	for _,attach in ipairs(self:getSelectedAttachments()) do
		if self.gizmo == "translate" then
			attach:getOffset():set(self.originalOffset[attach]):add(delta)
		end
		if self.gizmo == "rotate" then
			self:java2("applyDeltaRotation", attach:getRotate():set(self.originalRotate[attach]), delta)
		end
	end
end

function EditAttachment:onGizmoCancel()
	if not self.selectedModelScript then
		return
	end
	if self.list2:getSelectedCount() == 0 then
		local objectName = self:isBodyScript(self.selectedModelScript) and "character1" or self.selectedModelScript:getFullType()
		if self.gizmo == "translate" then
			self:java1("getObjectTranslation", objectName):set(self.originalOffset[self.selectedModelScript])
		end
		if self.gizmo == "rotate" then
			local rotation = self:java1("getObjectRotation", objectName)
			rotation:set(self.originalRotate[self.selectedModelScript])
		end
		return
	end
	for _,attach in ipairs(self:getSelectedAttachments()) do
		if self.gizmo == "translate" then
			attach:getOffset():set(self.originalOffset[attach])
		end
		if self.gizmo == "rotate" then
			attach:getRotate():set(self.originalRotate[attach])
		end
	end
end

function EditAttachment:new(x, y, width, height)
	local o = EditPanel.new(self, x, y, width, height)
	o:noBackground()
	return o
end

-----

function Scene:prerenderEditor()
	self.javaObject:fromLua1("setGizmoVisible", "none")
	self.javaObject:fromLua1("setGizmoOrigin", "none")
	if not self.zeroVector then self.zeroVector = Vector3f.new() end
	self.javaObject:fromLua1("setGizmoPos", self.zeroVector)
	self.javaObject:fromLua1("setGizmoRotate", self.zeroVector)
	self.javaObject:fromLua0("clearAABBs")
	self.javaObject:fromLua0("clearAxes")
	self.javaObject:fromLua0("clearHighlightBone")
	self.javaObject:fromLua1("setSelectedAttachment", nil)
end

function Scene:prerender()
	ISUI3DScene.prerender(self)
end

function Scene:onMouseDown(x, y)
	ISUI3DScene.onMouseDown(self, x, y)
	self.gizmoAxis = self.javaObject:fromLua2("testGizmoAxis", x, y)
	if self.gizmoAxis ~= "None" then
		local scenePos = self.javaObject:fromLua0("getGizmoPos")
		self.gizmoStartScenePos = alignVectorToGrid(Vector3f.new(scenePos), self.javaObject:fromLua0("getGridMult"))
		self.gizmoClickScenePos = alignVectorToGrid(self.javaObject:uiToScene(x, y, 0, Vector3f.new()), self.javaObject:fromLua0("getGridMult"))
		self.javaObject:fromLua3("startGizmoTracking", x, y, self.gizmoAxis)
		self:onGizmoStart()
	else
		self.parent.editUI.current:onSceneMouseDown(x, y)
	end
end

function Scene:onMouseMove(dx, dy)
	if self.gizmoAxis == "None" then
		ISUI3DScene.onMouseMove(self, dx, dy)
	else
		local x,y = self:getMouseX(),self:getMouseY()
		local newPos = alignVectorToGrid(self.javaObject:uiToScene(x, y, 0, Vector3f.new()), self.javaObject:fromLua0("getGridMult"))
		newPos:sub(self.gizmoClickScenePos)
		newPos:add(self.gizmoStartScenePos)
		self.javaObject:fromLua2("dragGizmo", x, y)
	end
end

function Scene:onMouseUp(x, y)
	ISUI3DScene.onMouseUp(self, x, y)
	if self.gizmoAxis ~= "None" then
		self.gizmoAxis = "None"
		self.javaObject:fromLua0("stopGizmoTracking")
		self:onGizmoAccept()
	end
end

function Scene:onMouseUpOutside(x, y)
	self:onMouseUp()
end

function Scene:onRightMouseDown(x, y)
	if self.gizmoAxis ~= "None" then
		self.gizmoAxis = "None"
		self.javaObject:fromLua0("stopGizmoTracking")
		self.mouseDown = false
		self.javaObject:fromLua1("setGizmoPos", self.gizmoStartScenePos)
		self:onGizmoCancel()
	end
end

function Scene:onGizmoStart()
	self.parent.editUI.current:onGizmoStart()
end

function Scene:onGizmoChanged(delta)
	if self.gizmoAxis == "None" then return end -- cancelled via onRightMouseUp
	self.parent.editUI.current:onGizmoChanged(delta)
end

function Scene:onGizmoAccept()
	self.parent.editUI.current:onGizmoAccept()
end

function Scene:onGizmoCancel()
	self.parent.editUI.current:onGizmoCancel()
end

function Scene:new(x, y, width, height)
	local o = ISUI3DScene.new(self, x, y, width, height)
	o.gizmoAxis = "None"
	return o
end

-----

function AttachmentEditorUI:createChildren()
	self.scene = Scene:new(0, 0, self.width, self.height)
	self.scene:initialise()
	self.scene:instantiate()
	self.scene:setAnchorRight(true)
	self.scene:setAnchorBottom(true)
	self:addChild(self.scene)

	self.scene.javaObject:fromLua1("setMaxZoom", 20)
	self.scene.javaObject:fromLua1("setZoom", 10)
	self.scene.javaObject:fromLua1("setGizmoScale", 1.0 / 5.0)
	
	self.scene.javaObject:fromLua1("createCharacter", "character1")
	self.scene.javaObject:fromLua2("setCharacterAlpha", "character1", 1.0)
	self.scene.javaObject:fromLua2("setCharacterAnimSet", "character1", "player-avatar")
	self.scene.javaObject:fromLua2("setCharacterClearDepthBuffer", "character1", false)
	self.scene.javaObject:fromLua2("setCharacterShowBones", "character1", true)
	self.scene.javaObject:fromLua2("setObjectVisible", "character1", false)

	local viewW = 150
	local viewH = 100
	self.bottomPanel = ISPanel:new(0, self.height - viewH, self.width, viewH)
	self.bottomPanel:setAnchorTop(false)
	self.bottomPanel:setAnchorLeft(false)
	self.bottomPanel:setAnchorRight(false)
	self.bottomPanel:setAnchorBottom(true)
	self.bottomPanel:noBackground()
	self:addChild(self.bottomPanel)

	local viewNames = {'Left', 'Right', 'Top', 'Bottom', 'Front', 'Back'}
	local viewX = (self.width - #viewNames * viewW - (#viewNames - 1) * 10) / 2
	local viewY = 0
	self.views = {}
	for _,viewName in ipairs(viewNames) do
		local view = SwitchView:new(self, viewX, viewY, viewW, viewH)
		view:initialise()
		view:instantiate()
		view:setAnchorTop(false)
		view:setAnchorRight(false)
		view:setView(viewName)
		view.javaObject:fromLua1("setMaxZoom", 14)
		view.javaObject:fromLua1("setZoom", 14)
		view.javaObject:fromLua1("setDrawGrid", false)
		view.javaObject:fromLua1("createCharacter", "character1")
		view.javaObject:fromLua2("setCharacterAnimSet", "character1", "player-editor")
		view.javaObject:fromLua2("setCharacterAlpha", "character1", 1.0)
		if viewName ~= "Top" and viewName ~= "Bottom" then
			view.javaObject:fromLua2("dragView", 0, viewH * 2)
		end
		self.bottomPanel:addChild(view)
		table.insert(self.views, view)
		viewX = viewX + viewW + 10
	end

	self.editUI = {}
	self.editUI.current = nil

	local ui = EditAttachment:new(10, 10, 250, self.height - 100)
	ui:setVisible(false)
	self:addChild(ui)
	self.editUI.attachments = ui

	local buttonHgt = FONT_HGT_MEDIUM + 8;
	local button = ISButton:new(10, self.bottomPanel.height - 10 - buttonHgt, 80, buttonHgt, "SAVE", self, self.onSave)
	self.bottomPanel:addChild(button)

	local button2 = ISButton:new(button:getRight() + 10, self.bottomPanel.height - 10 - buttonHgt, 80, buttonHgt, "EXIT", self, self.onExit)
	self.bottomPanel:addChild(button2)

	local label = ISLabel:new(10, self.bottomPanel.y - 30, FONT_HGT_SMALL, "", 1, 1, 1, 1, UIFont.Small, true)
	label:setAnchorTop(false)
	label:setAnchorBottom(true)
	self:addChild(label)
	self.wroteScriptLabel = label

	local buttonWid = 60
	local buttonHgt = FONT_HGT_SMALL + 8
	local buttonScale1 = ISButton:new(self.width - 10 - buttonWid, self.bottomPanel.height - 10 - buttonHgt, buttonWid, buttonHgt, "0.001", self, self.onGridMult1)
	self.bottomPanel:addChild(buttonScale1)
	self.buttonScale1 = buttonScale1

	local buttonScale2 = ISButton:new(buttonScale1.x - 10 - buttonWid, buttonScale1.y, buttonWid, buttonHgt, "0.005", self, self.onGridMult2)
	self.bottomPanel:addChild(buttonScale2)
	self.buttonScale2 = buttonScale2

	local buttonScale3 = ISButton:new(buttonScale2.x - 10 - buttonWid, buttonScale1.y, buttonWid, buttonHgt, "0.01", self, self.onGridMult3)
	self.bottomPanel:addChild(buttonScale3)
	self.buttonScale3 = buttonScale3

	self.scene.javaObject:fromLua1("setGridMult", 10)
	self.buttonScale1:setEnable(false)

	self.editUI.attachments:doLayout()
	self:setEditUI(self.editUI.attachments)
end

function AttachmentEditorUI:onResolutionChange(oldw, oldh, neww, newh)
	self:setWidth(neww)
	self:setHeight(newh)
	self.bottomPanel:setX(self.width / 2 - self.bottomPanel.width / 2)
	self.editUI.attachments:doLayout()
end

function AttachmentEditorUI:update()
	ISPanel.update(self)
	if self.width ~= getCore():getScreenWidth() or self.height ~= getCore():getScreenHeight() then
		self:onResolutionChange(self.width, self.height, getCore():getScreenWidth(), getCore():getScreenHeight())
	end
end

function AttachmentEditorUI:prerender()
	ISPanel.prerender(self)
	self.scene:prerenderEditor()
	if self.editUI and self.editUI.current then
		self.editUI.current:prerenderEditor()
	end
	if (self.wroteScriptLabel.name ~= "") and (self.wroteScriptTime + 5000 < getTimestampMs()) then
		self.wroteScriptLabel:setName("")
	end
end

function AttachmentEditorUI:setEditUI(ui)
	if self.editUI.current then
		self.editUI.current:setVisible(false)
	end
	self.editUI.current = ui
	if ui then
		ui:toUI()
		ui:setVisible(true)
	end
end

function AttachmentEditorUI:onSave(button, x, y)
	local item = self.editUI.attachments.list:getSelectedItems()[1]
	if item then
		getAttachmentEditorState():fromLua1("writeScript", item.item:getFullType())
	end
end

function AttachmentEditorUI:onExit(button, x, y)
	getAttachmentEditorState():fromLua0("exit")
end

function AttachmentEditorUI:onGridMult1(button, x, y)
	self.buttonScale1:setEnable(false)
	self.buttonScale2:setEnable(true)
	self.buttonScale3:setEnable(true)
	self.scene.javaObject:fromLua1("setGridMult", 10)
end

function AttachmentEditorUI:onGridMult2(button, x, y)
	self.buttonScale1:setEnable(true)
	self.buttonScale2:setEnable(false)
	self.buttonScale3:setEnable(true)
	self.scene.javaObject:fromLua1("setGridMult", 2)
end

function AttachmentEditorUI:onGridMult3(button, x, y)
	self.buttonScale1:setEnable(true)
	self.buttonScale2:setEnable(true)
	self.buttonScale3:setEnable(false)
	self.scene.javaObject:fromLua1("setGridMult", 1)
end

-- Called from Java
function AttachmentEditorUI:showUI()
end

-- Called from Java
function AttachmentEditorUI:wroteScript(fileName)
	self.wroteScriptTime = getTimestampMs()
	self.wroteScriptLabel:setName(fileName)
end

function AttachmentEditorUI:new(x, y, width, height)
	local o = ISPanel.new(self, x, y, width, height)
	o:setAnchorRight(true)
	o:setAnchorBottom(true)
	o:noBackground()
	getAttachmentEditorState():setTable(o)
	return o
end

function AttachmentEditorState_InitUI()
	local UI = AttachmentEditorUI:new(0, 0, getCore():getScreenWidth(), getCore():getScreenHeight())
	AttachmentEditorState_UI = UI
	UI:setVisible(true)
	UI:addToUIManager()
end

