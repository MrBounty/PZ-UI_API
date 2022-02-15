--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require('Vehicles/ISUI/ISUI3DScene')

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

---@class EditVehicleUI : ISPanel
EditVehicleUI = ISPanel:derive("EditVehicleUI")

EditVehicleUI_Scene = ISUI3DScene:derive("EditVehicleUI_Scene")
EditVehicleUI_SwitchView = ISUI3DScene:derive("EditVehicleUI_SwitchView")
local Scene = EditVehicleUI_Scene
local SwitchView = EditVehicleUI_SwitchView

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

local function drawScalar(ui, label, x, y, v)
	ui:drawText(label, x, y, 1, 1, 1, 1, UIFont.Small)
	x = x + getTextManager():MeasureStringX(UIFont.Small, label)

	local dx = getTextManager():MeasureStringX(UIFont.Small, "99.9999")
	str = vectorComponentToString(v, 1)
	ui:drawText(str, x, y, 1, 0, 0, 1, UIFont.Small)
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

local function drawArea(ui, label, x, y, area)
	ui:drawText(label, x, y, 1, 1, 1, 1, UIFont.Small)
	x = x + getTextManager():MeasureStringX(UIFont.Small, label)

	local dx = getTextManager():MeasureStringX(UIFont.Small, "99.9999")
	str = vectorComponentToString(area:getX(), 1)
	ui:drawText(str, x, y, 1, 0, 0, 1, UIFont.Small)
	x = x + dx

	str = vectorComponentToString(area:getY(), 1)
	ui:drawText(str, x, y, 0, 0.5, 1, 1, UIFont.Small)
	x = x + dx

	str = vectorComponentToString(area:getW(), 1)
	ui:drawText(str, x, y, 1, 1, 1, 1, UIFont.Small)
	x = x + dx

	str = vectorComponentToString(area:getH(), 1)
	ui:drawText(str, x, y, 1, 1, 1, 1, UIFont.Small)
end

local function alignVectorToGrid(v)
	local vx = math.floor(v:x() * 100 + 0.5f) / 100
	local vy = math.floor(v:y() * 100 + 0.5f) / 100
	local vz = math.floor(v:z() * 100 + 0.5f) / 100
	v:setComponent(0, vx)
	v:setComponent(1, vy)
	v:setComponent(2, vz)
	return v
end

-----

EditVehicleUI_ListBox = ISScrollingListBox:derive("EditVehicleUI_ListBox")
local ListBox = EditVehicleUI_ListBox

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
	if self.selectionMode == "single" or not isShiftKeyDown() then
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

EditVehicleUI_EditPanel = ISPanel:derive("EditVehicleUI_EditPanel")
local EditPanel = EditVehicleUI_EditPanel

EditVehicleUI_EditArea = EditPanel:derive("EditVehicleUI_EditArea")
EditVehicleUI_EditAttachment = EditPanel:derive("EditVehicleUI_EditAttachment")
EditVehicleUI_EditChassis = EditPanel:derive("EditVehicleUI_EditChassis")
EditVehicleUI_EditPassenger = EditPanel:derive("EditVehicleUI_EditPassenger")
EditVehicleUI_EditPhysics = EditPanel:derive("EditVehicleUI_EditPhysics")
EditVehicleUI_EditWheel = EditPanel:derive("EditVehicleUI_EditWheel")
local EditArea = EditVehicleUI_EditArea
local EditAttachment = EditVehicleUI_EditAttachment
local EditChassis = EditVehicleUI_EditChassis
local EditPassenger = EditVehicleUI_EditPassenger
local EditPhysics = EditVehicleUI_EditPhysics
local EditWheel = EditVehicleUI_EditWheel

function EditPanel:updateEditor()
end

function EditPanel:prerenderEditor()
end

function EditPanel:toUI()
	self.script = self:java1("getVehicleScript", "vehicle")
end

function EditPanel:onGizmoStart()
end

function EditPanel:onGizmoChanged(delta)
end

function EditPanel:onGizmoAccept()
end

function EditPanel:onGizmoCancel()
end

function EditPanel:isScaleVehicle()
--	return self.parent.scene.javaObject:fromLua0("isScaleVehicle")
	return true
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

function EditArea:createChildren()
	local buttonPadY = 4
	local buttonHgt = FONT_HGT_MEDIUM + 8
	
	self.list = self:createList(0, 0, self.width, self.height - buttonHgt * 2 - buttonPadY * 3)
	self.list.doDrawItem = self.doDrawItem

	self.gizmo = "translate"
	local button = ISButton:new(10, self.list:getBottom() + buttonPadY, self.width - 40, buttonHgt, "TRANSLATE", self, self.onToggleGizmo)
	self:addChild(button)
	self.button = button

	button = ISButton:new(10, button:getBottom() + buttonPadY, self.width - 40, buttonHgt, "ALIGN TO EXTENTS", self, self.onAlignToExtents)
	self:addChild(button)
	self.buttonAlign = button
end

function EditArea:onToggleGizmo()
	if self.gizmo == "translate" then
		self.gizmo = "scale"
		self.button.title = "SCALE"
	else
		self.gizmo = "translate"
		self.button.title = "TRANSLATE"
	end
end

function EditArea:onAlignToExtents()
	local extents = self.script:getExtents()
	for _,area in ipairs(self:getSelectedAreas()) do
		if area:getX() > extents:x() / 2 then
			area:setX(extents:x() / 2 + area:getW() / 2)
		elseif area:getX() < -extents:x() / 2 then
			area:setX(-extents:x() / 2 - area:getW() / 2)
		elseif area:getY() > extents:z() / 2 then
			area:setY(extents:z() / 2 + area:getH() / 2)
		elseif area:getY() < -extents:z() / 2 then
			area:setY(-extents:z() / 2 - area:getH() / 2)
		end
	end
end

function EditArea:doDrawItem(y, item, alt)
	local area = item.item
	
	local x = 4
	local indent = 16

	local isMouseOver = self.mouseoverselected == item.index and not self:isMouseOverScrollBar()
	if item.selected then
		self:drawRect(0, y, self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
	elseif isMouseOver then
		self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.25, 1.0, 1.0, 1.0)
	end

	self:drawText(area:getId(), x, y, 1, 1, 1, 1, UIFont.Small)
	y = y + FONT_HGT_SMALL

	local scale = self.parent:isScaleVehicle() and 1 or (1 / self.parent.script:getModelScale())
	drawArea(self, "", x + indent, y, area)
	y = y + FONT_HGT_SMALL

	self:drawRect(x, y, self.width - 4 * 2, 2, 1.0, 0.5, 0.5, 0.5)
	y = y + 2
	return y
end

function EditArea:toUI()
	EditPanel.toUI(self)
	self.list:clear()
	for i=1,self.script:getAreaCount() do
		local area = self.script:getArea(i-1)
		self.list:addItem(area:getId(), area)
	end
end

function EditArea:prerenderEditor()
	self.list.doDrawItem = self.doDrawItem
	local script = self.script
	local scale = 1
	if not self:isScaleVehicle() then
		scale = 1 / script:getModelScale()
	end
	local mouseOver = self.list.items[self.list.mouseoverselected]
	local item = mouseOver or self.list:getSelectedItems()[1]
	if item then
		local area = item.item
		local ox = area:getX() * scale
		local oy = 0 * scale
		local oz = area:getY() * scale
		self:java3("setGizmoXYZ", ox, oy, oz)
		self:java1("setGizmoVisible", self.gizmo)
		self:java2("setGizmoOrigin", "centerOfMass", "vehicle") -- "ground"
	end
	local ext = script:getExtents()
	local com = script:getCenterOfMassOffset()
	self:java6("addAABB", com:x(), com:y(), com:z(), ext:x() * scale, ext:y() * scale, ext:z() * scale)
	for i=1,script:getAreaCount() do
		local area = script:getArea(i-1)
		self:java6("addAABB", area:getX() * scale, 0, area:getY() * scale, area:getW() * scale, 0.1, area:getH() * scale, 0.5, 0.5, 0.5)
	end
	if mouseOver then
		local area = mouseOver.item
		self:java9("addAABB", area:getX() * scale, 0, area:getY() * scale, area:getW() * scale, 0.1, area:getH() * scale, 0, 1, 0)
		self:java3("addAxis", area:getX() * scale, 0 * scale, area:getY() * scale)
	else
		for _,item in self.list:iteratorSelected() do
			local area = item.item
			self:java9("addAABB", area:getX() * scale, 0, area:getY() * scale, area:getW() * scale, 0.1, area:getH() * scale, 0, 1, 0)
			self:java3("addAxis", area:getX() * scale, 0 * scale, area:getY() * scale)
		end
	end
	self.buttonAlign:setEnable(self.list:getSelectedCount() > 0)
end

function EditArea:getSelectedAreas()
	local selected = {}
	for _,item in self.list:iteratorSelected() do
		table.insert(selected, item.item)
	end
	return selected
end

local function alignAreaToGrid(area)
	local vx = math.floor(area:getX() * 100 + 0.5)
	local vy = math.floor(area:getY() * 100 + 0.5)
	local vw = math.floor(area:getW() * 100 + 0.5)
	local vh = math.floor(area:getH() * 100 + 0.5)
	if vw % 2 > 0.001 then vw = vw + 1 end
	if vh % 2 > 0.001 then vh = vh + 1 end
	area:setX(vx / 100)
	area:setY(vy / 100)
	area:setW(vw / 100)
	area:setH(vh / 100)
end

local function alignXYWHToGrid(xywh)
	local vx = math.floor(xywh.x * 100 + 0.5)
	local vy = math.floor(xywh.y * 100 + 0.5)
	local vw = math.floor(xywh.w * 100 + 0.5)
	local vh = math.floor(xywh.h * 100 + 0.5)
	if vw % 2 > 0.001 then vw = vw + 1 end
	if vh % 2 > 0.001 then vh = vh + 1 end
	xywh.x = vx / 100
	xywh.y = vy / 100
	xywh.w = vw / 100
	xywh.h = vh / 100
end

function EditArea:onGizmoStart()
	self.originalXYWH = {}
	for _,area in ipairs(self:getSelectedAreas()) do
		local xywh = { x = area:getX(), y = area:getY(), w = area:getW(), h = area:getH() }
		if not self:isScaleVehicle() then
			xywh.x = xywh.x / self.script:getModelScale()
			xywh.y = xywh.y / self.script:getModelScale()
			xywh.w = xywh.w / self.script:getModelScale()
			xywh.h = xywh.h / self.script:getModelScale()
		end
		alignXYWHToGrid(xywh)
		self.originalXYWH[area] = xywh
	end
end

function EditArea:onGizmoChanged(delta)
	if self.parent.scene.gizmoAxis == "None" then return end
	local script = self.script
	local scale = 1
	if not self:isScaleVehicle() then
		scale = script:getModelScale()
	end
	for _,area in ipairs(self:getSelectedAreas()) do
		local xywh = self.originalXYWH[area]
		if self.gizmo == "scale" then
			area:setW((xywh.w + delta:x() * 2) * scale)
			area:setH((xywh.h + delta:z() * 2) * scale)
		end
		if self.gizmo == "translate" then
			area:setX((xywh.x + delta:x()) * scale)
			area:setY((xywh.y + delta:z()) * scale)
			area:setW(xywh.w * scale)
			area:setH(xywh.h * scale)
		end
	end
end

function EditArea:onGizmoCancel()
	local scale = 1
	if not self:isScaleVehicle() then
		scale = self.script:getModelScale()
	end
	for _,area in ipairs(self:getSelectedAreas()) do
		local xywh = self.originalXYWH[area]
		area:setX(xywh.x * scale)
		area:setY(xywh.y * scale)
		area:setW(xywh.w * scale)
		area:setH(xywh.h * scale)
	end
end

function EditArea:new(x, y, width, height)
	local o = EditPanel.new(self, x, y, width, height)
	return o
end

-----

function EditAttachment:createChildren()
	local buttonPadY = 4
	local buttonHgt = FONT_HGT_MEDIUM + 8

	local itemHeight = FONT_HGT_SMALL + 2
	self.list = self:createList(0, 0, self.width, itemHeight * 6)
	self.list.doDrawItem = self.doDrawItem

	self.belowList = ISPanel:new(0, self.list:getBottom() + buttonPadY, self.width, 100)
	self.belowList:noBackground()
	self:addChild(self.belowList);

	self.nameEntry = ISTextEntryBox:new("", 10, 0, self.width - 10 * 2, buttonHgt)
	self.nameEntry.font = UIFont.Medium
	self.nameEntry.onCommandEntered = function(self) self.parent.parent:onNameEntered() end
	self.belowList:addChild(self.nameEntry)

	local button1 = ISButton:new(10, self.nameEntry:getBottom() + buttonPadY, (self.width - 10 * 3) / 2, buttonHgt, "NEW", self, self.onNewAttachment)
	self.belowList:addChild(button1)
--	button1:setEnable(false)
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

function EditAttachment:doDrawItem(y, item, alt)
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

function EditAttachment:toUI()
	EditPanel.toUI(self)
	local attachIds = {}
	for _,item in self.list:iteratorSelected() do
		attachIds[item.item:getId()] = true
	end
	self.list:clear()
	self.list:setScrollHeight(0)
	for i=1,self.script:getAttachmentCount() do
		local attach = self.script:getAttachment(i-1)
		self.list:addItem(attach:getId(), attach)
		if attachIds[attach:getId()] then
			self.list.items[i].selected = true
		end
	end
	self.list:sort()
end

function EditAttachment:prerenderEditor()
	self.list.doDrawItem = self.doDrawItem
	local script = self.script
	local scale = 1
	if not self:isScaleVehicle() then
		scale = 1 / script:getModelScale()
	end

	local mouseOver = self.list.items[self.list.mouseoverselected]
	local item = mouseOver or self.list:getSelectedItems()[1]
	if not item then
		self:setSelectedAttachment(nil)
		return
	end

	local attach = mouseOver and mouseOver.item or self:getSelectedAttachments()[1]
	self:setSelectedAttachment(attach)

	if attach then
		self:java3("setGizmoXYZ", attach:getOffset():x() * scale, attach:getOffset():y() * scale, attach:getOffset():z() * scale)
		self:java1("setGizmoRotate", attach:getRotate())
		self:java2("setGizmoOrigin", "vehicleModel", "vehicle")
--[[
		local parentAttachName = self:java1("getObjectParentAttachment", modelScript:getFullType())
		if parentAttachName then
			local parentName = self:java1("getObjectParent", modelScript:getFullType())
			self:java4("setGizmoOrigin", "attachment", modelScript:getFullType(), parentName, parentAttachName)
			self:java1("setSelectedAttachment", attach:getId())
		end
--]]
		self:java6("addAxis", attach:getOffset():x(), attach:getOffset():y(), attach:getOffset():z(),
			attach:getRotate():x(), attach:getRotate():y(), attach:getRotate():z())
		self:java1("setGizmoVisible", self.gizmo)
		self:java1("setTransformMode", self.transformMode)
	end
end

function EditAttachment:doLayout()
	local top = self:getY()
	local labelTop = self.parent:getHeight() - self.parent.bottomPanel:getHeight() - 30
	local bottom = labelTop - 20 - self.belowList.height - 4
	self.list:setHeight(bottom - top)
	self.belowList:setY(self.list:getBottom() + 4)
	self:setHeight(self.belowList:getBottom())
end

function EditAttachment:onNameEntered()
	local attach = self:getSelectedAttachments()[1]
	if not attach then return end
	local text = self.nameEntry:getInternalText():trim()
	if text == "" then
		self.nameEntry:setText(attach:getId())
		return
	end
	local attach2 = self.script:getAttachmentById(text)
	if attach2 then
		self.nameEntry:setText(attach:getId())
		return
	end
	attach:setId(text)
end

function EditAttachment:getUniqueAttachmentId(modelScript)
	for i=1,100 do
		local id = "attachment"..tostring(i)
		if not self.script:getAttachmentById(id) then
			return id
		end
	end
	error "too many attachments"
end

function EditAttachment:onNewAttachment(button, x, y)
	local id = self:getUniqueAttachmentId()
	local attach = ModelAttachment.new(id)
	self.script:addAttachment(attach)
	self:toUI()
	local index = self.list:indexOf(id)
	self.list.items[index].selected = true
	self.list:ensureVisible(index)
end

function EditAttachment:onDeleteAttachment(button, x, y)
	for _,item in self.list:iteratorSelected() do
		self.script:removeAttachment(item.item)
	end
	self:toUI()
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

function EditAttachment:getSelectedAttachments()
	local selected = {}
	for _,item2 in self.list:iteratorSelected() do
		local attach = self.script:getAttachmentById(item2.item:getId())
		if attach then
			table.insert(selected, attach)
		end
	end
	return selected
end

function EditAttachment:getSelectedAttachmentIds()
	local selected = {}
	for _,item2 in self.list:iteratorSelected() do
		table.insert(selected, item2.item:getId())
	end
	return selected
end

function EditAttachment:onGizmoStart()
	self.originalOffset = {}
	self.originalRotate = {}
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
	return o
end

-----

function EditChassis:createChildren()
	self.list = self:createList(0, 0, self.width, self.height)
	self.list.doDrawItem = self.doDrawItem
	self.list.selectionMode = "single"
end

function EditChassis:toUI()
	EditPanel.toUI(self)
	self.list:clear()
	self.list:addItem("model.scale", "model.scale")
	self.list:addItem("model.offset", "model.offset")
	self.list:addItem("centerOfMassOffset", "centerOfMassOffset")
	self.list:addItem("extents", "extents")
	self.list:addItem("physicsChassisShape", "physicsChassisShape")
	self.list:addItem("shadowExtents", "shadowExtents")
	self.list:addItem("shadowOffset", "shadowOffset")
end

function EditChassis:doDrawItem(y, item, alt)
	local which = item.item
	
	local x = 4
	local indent = 16

	local isMouseOver = self.mouseoverselected == item.index and not self:isMouseOverScrollBar()
	if item.selected then
		self:drawRect(0, y, self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
	elseif isMouseOver then
		self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.25, 1.0, 1.0, 1.0)
	end

	self:drawText(item.text, x, y, 1, 1, 1, 1, UIFont.Small)
	y = y + FONT_HGT_SMALL

	local scale = self.parent:isScaleVehicle() and 1 or (1 / self.parent.script:getModelScale())
	local offset = nil
	local offset2 = nil
	if which == "model.offset" then
		offset = self.parent.script:getModel():getOffset()
	elseif which == "model.scale" then
		local modelScale = self.parent.script:getModelScale()
		drawScalar(self, "", x + indent, y, modelScale)
	elseif which == "centerOfMassOffset" then
		offset = self.parent.script:getCenterOfMassOffset()
	elseif which == "extents" then
		offset = self.parent.script:getExtents()
	elseif which == "physicsChassisShape" then
		offset = self.parent.script:getPhysicsChassisShape()
	elseif which == "shadowExtents" then
		offset2 = self.parent.script:getShadowExtents()
	elseif which == "shadowOffset" then
		offset2 = self.parent.script:getShadowOffset()
	end
	if offset then
		drawVector(self, "", x + indent, y, offset:x() * scale, offset:y() * scale, offset:z() * scale)
	end
	if offset2 then
		drawVector(self, "", x + indent, y, offset2:x() * scale, 0.0, offset2:y() * scale)
	end
	y = y + FONT_HGT_SMALL

	self:drawRect(x, y, self.width - 4 * 2, 2, 1.0, 0.5, 0.5, 0.5)
	y = y + 2
	return y
end

function EditChassis:prerenderEditor()
	self.list.doDrawItem = self.doDrawItem

	self:addAABB(self.script:getPhysicsChassisShape(), self.script:getCenterOfMassOffset(), 1, 1, 1)

	local item = self.list.items[self.list.mouseoverselected] or self.list:getSelectedItems()[1]
	if item then
		local which = item.item
		local scale = 1
		if not self:isScaleVehicle() then
			scale = self.script:getModelScale()
		end
		if which == "centerOfMassOffset" then
			local offset = self.script:getCenterOfMassOffset()
			local ox,oy,oz = offset:x() / scale, offset:y() / scale, offset:z() / scale
			self:java3("setGizmoXYZ", ox, oy, oz)
			self:java1("setGizmoVisible", "translate")
			self:java2("setGizmoOrigin", "centerOfMass", "vehicle")
		end
		if which == "extents" then
			local offset = self.script:getCenterOfMassOffset()
			local ox,oy,oz = offset:x() / scale, offset:y() / scale, offset:z() / scale
			self:java3("setGizmoXYZ", ox, oy, oz)
			self:java1("setGizmoVisible", "scale")
			self:java2("setGizmoOrigin", "centerOfMass", "vehicle")
			self:addAABB(self.script:getExtents(), self.script:getCenterOfMassOffset(), 0, 1, 0)
		end
		if which == "model.offset" then
			local offset = self.script:getModel():getOffset()
			local ox,oy,oz = offset:x() / scale, offset:y() / scale, offset:z() / scale
			self:java3("setGizmoXYZ", ox, oy, oz)
			self:java1("setGizmoVisible", "translate")
			self:java2("setGizmoOrigin", "centerOfMass", "vehicle")
		end
		if which == "model.scale" then
			local offset = self.script:getCenterOfMassOffset()
			local ox,oy,oz = 0.0, 0.0, 0.0
			self:java3("setGizmoXYZ", ox, oy, oz)
			self:java1("setGizmoVisible", "scale")
			self:java2("setGizmoOrigin", "centerOfMass", "vehicle")
		end
		if which == "physicsChassisShape" then
			local offset = self.script:getCenterOfMassOffset()
			local ox,oy,oz = offset:x() / scale, offset:y() / scale, offset:z() / scale
			self:java3("setGizmoXYZ", ox, oy, oz)
			self:java1("setGizmoVisible", "scale")
			self:java2("setGizmoOrigin", "centerOfMass", "vehicle")
			self:addAABB(self.script:getPhysicsChassisShape(), self.script:getCenterOfMassOffset(), 0, 1, 0)

			for i=1,self.script:getPhysicsShapeCount() do
				local shape = self.script:getPhysicsShape(i-1)
				if shape:getTypeString() == "box" then
					self:java6("addBox3D", shape:getOffset(), shape:getExtents(), shape:getRotate(), 0, 1, 0)
				end
				if shape:getTypeString() == "sphere" then
					self:addSphere(shape:getOffset(), shape:getRadius(), 0, 1, 0)
				end
			end
		end
		if which == "shadowExtents" then
			local offset = self.script:getShadowOffset()
			local extents = self.script:getShadowExtents()
			local ox,oy,oz = offset:x() / scale, 0.0, offset:y() / scale
			self:java3("setGizmoXYZ", ox, oy, oz)
			self:java1("setGizmoVisible", "scale")
			self:java2("setGizmoOrigin", "none")
			self.tempVector3f_1:set(offset:x(), 0.0, offset:y())
			self.tempVector3f_2:set(extents:x(), 0.2, extents:y())
			self:addAABB(self.tempVector3f_2, self.tempVector3f_1, 0, 1, 0)
		end
		if which == "shadowOffset" then
			local offset = self.script:getShadowOffset()
			local extents = self.script:getShadowExtents()
			local ox,oy,oz = offset:x() / scale, 0.0, offset:y() / scale
			self:java3("setGizmoXYZ", ox, oy, oz)
			self:java1("setGizmoVisible", "translate")
			self:java2("setGizmoOrigin", "none")
			self.tempVector3f_1:set(offset:x(), 0.0, offset:y())
			self.tempVector3f_2:set(extents:x(), 0.2, extents:y())
			self:addAABB(self.tempVector3f_2, self.tempVector3f_1, 0, 1, 0)
		end
	end
end

function EditChassis:addAABB(box, offset, r, g, b)
	self:java9("addAABB", offset:x(), offset:y(), offset:z(), box:x(), box:y(), box:z(), r, g, b)
end

function EditChassis:addSphere(offset, radius, r, g, b)
	local extents = self.tempSphereExtents or Vector3f.new()
	extents:setComponent(0, radius * 2)
	extents:setComponent(1, radius * 2)
	extents:setComponent(2, radius * 2)
	self:addAABB(extents, offset, 0, 1, 0)
end

local function alignExtentsToGrid(v)
	local vx = math.floor(v:x() * 100 + 0.5)
	local vy = math.floor(v:y() * 100 + 0.5)
	local vz = math.floor(v:z() * 100 + 0.5)
	if vx % 2 > 0.001 then vx = vx + 1 end
	if vy % 2 > 0.001 then vy = vy + 1 end
	if vz % 2 > 0.001 then vz = vz + 1 end
	v:setComponent(0, vx / 100)
	v:setComponent(1, vy / 100)
	v:setComponent(2, vz / 100)
end

function EditChassis:onGizmoStart()
	local item = self.list.items[self.list.mouseoverselected] or self.list:getSelectedItems()[1]
	if item then
		local which = item.item
		if which == "centerOfMassOffset" then
			self.originalCenterOfMassOffset = Vector3f.new(self.script:getCenterOfMassOffset())
		end
		if which == "extents" then
			self.originalExtents = Vector3f.new(self.script:getExtents())
		end
		if which == "model.offset" then
			self.originalModelOffset = Vector3f.new(self.script:getModel():getOffset())
		end
		if which == "model.scale" then
			self.originalModelScale = self.script:getModelScale()
		end
		if which == "physicsChassisShape" then
			self.originalChassisExtents = Vector3f.new(self.script:getPhysicsChassisShape())
		end
		if which == "shadowExtents" then
			self.originalShadowExtents = Vector2f.new(self.script:getShadowExtents())
		end
		if which == "shadowOffset" then
			self.originalShadowOffset = Vector2f.new(self.script:getShadowOffset())
		end
	end
end

function EditChassis:onGizmoChanged(delta)
	if self.parent.scene.gizmoAxis == "None" then return end
	local item = self.list:getSelectedItems()[1]
	if item then
		local which = item.item
		local scale = 1
		if not self:isScaleVehicle() then
			scale = self.script:getModelScale()
		end
		local scenePos = self:java0("getGizmoPos")
		if which == "centerOfMassOffset" then
			self.script:getCenterOfMassOffset():set(self.originalCenterOfMassOffset):add(delta)
		end
		if which == "extents" then
			local extents = Vector3f.new(delta)
			extents:mul(2 * scale)
			extents:add(self.originalExtents)
			if extents:x() < 0.1 then extents:setComponent(0, 0.1) end
			if extents:y() < 0.1 then extents:setComponent(1, 0.1) end
			if extents:z() < 0.1 then extents:setComponent(2, 0.1) end
			self.script:getExtents():set(extents)
		end
		if which == "model.offset" then
			self.script:getModel():getOffset():set(self.originalModelOffset):add(delta)
		end
		if which == "model.scale" then
			local add = math.max(delta:get(0), math.max(delta:get(1), delta:get(2)))
			local subtract = math.min(delta:get(0), math.min(delta:get(1), delta:get(2)))
			self.script:setModelScale(self.originalModelScale + add + subtract)
		end
		if which == "physicsChassisShape" then
			local extents = Vector3f.new(delta)
			extents:mul(2 * scale)
			extents:add(self.originalChassisExtents)
			if extents:x() < 0.1 then extents:setComponent(0, 0.1) end
			if extents:y() < 0.1 then extents:setComponent(1, 0.1) end
			if extents:z() < 0.1 then extents:setComponent(2, 0.1) end
			self.script:getPhysicsChassisShape():set(extents)
		end
		if which == "shadowExtents" then
			self.script:getShadowExtents():set(self.originalShadowExtents):add(delta:x(), delta:z())
		end
		if which == "shadowOffset" then
			self.script:getShadowOffset():set(self.originalShadowOffset):add(delta:x(), delta:z())
		end
	end
end

function EditChassis:onGizmoCancel()
	local item = self.list.items[self.list.mouseoverselected] or self.list:getSelectedItems()[1]
	if item then
		local which = item.item
		if which == "centerOfMassOffset" then
			self.script:getCenterOfMassOffset():set(self.originalCenterOfMassOffset)
		end
		if which == "extents" then
			self.script:getExtents():set(self.originalExtents)
		end
		if which == "model.offset" then
			self.script:getModel():getOffset():set(self.originalModelOffset)
		end
		if which == "model.scale" then
			self.script:setModelScale(self.originalModelScale)
		end
		if which == "physicsChassisShape" then
			self.script:getPhysicsChassisShape():set(self.originalChassisExtents)
		end
		if which == "shadowExtents" then
			self.script:getShadowExtents():set(self.originalShadowExtents)
		end
		if which == "shadowOffset" then
			self.script:getShadowOffset():set(self.originalShadowOffset)
		end
	end
end

function EditChassis:new(x, y, width, height)
	local o = EditPanel.new(self, x, y, width, height)
	o.tempVector3f_1 = Vector3f:new()
	o.tempVector3f_2 = Vector3f:new()
	return o
end

-----

function EditPassenger:createChildren()
	self.list = self:createList(0, 0, self.width, 24 * 6)
	self.list.doDrawItem = self.doDrawItem

	self.list2 = self:createList(0, self.list:getBottom() + 10, self.width, 24 * 6)
	self.list2.doDrawItem = self.doDrawItem2
	self.list2.itemheight = FONT_HGT_SMALL * 3 + 2
end

function EditPassenger:doDrawItem(y, item, alt)
	local pngr = item.item
	
	local x = 4
	local indent = 16

	local isMouseOver = self.mouseoverselected == item.index and not self:isMouseOverScrollBar()
	if item.selected then
		self:drawRect(0, y, self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
	elseif isMouseOver then
		self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.25, 1.0, 1.0, 1.0)
	end

	self:drawText(pngr:getId(), x, y, 1, 1, 1, 1, UIFont.Small)
	y = y + FONT_HGT_SMALL

	self:drawRect(x, y, self.width - 4 * 2, 2, 1.0, 0.5, 0.5, 0.5)
	y = y + 2
	return y
end

function EditPassenger:doDrawItem2(y, item, alt)
	local posn = item.item
	
	local x = 4
	local indent = 16

	local isMouseOver = self.mouseoverselected == item.index and not self:isMouseOverScrollBar()
	if item.selected then
		self:drawRect(0, y, self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
	elseif isMouseOver then
		self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.25, 1.0, 1.0, 1.0)
	end

	self:drawText(posn:getId(), x, y, 1, 1, 1, 1, UIFont.Small)
	y = y + FONT_HGT_SMALL

	local scale = self.parent:isScaleVehicle() and 1 or (1 / self.parent.script:getModelScale())

	local offset = posn:getOffset()
	drawVector(self, "offset:   ", x + indent, y, offset:x() * scale, offset:y() * scale, offset:z() * scale)
	y = y + FONT_HGT_SMALL

	local rotate = posn:getRotate()
	drawVector(self, "rotate:   ", x + indent, y, rotate:x() * scale, rotate:y() * scale, rotate:z() * scale)
	y = y + FONT_HGT_SMALL

	self:drawRect(x, y, self.width - 4 * 2, 2, 1.0, 0.5, 0.5, 0.5)
	y = y + 2
	return y
end

function EditPassenger:prerenderEditor()
	local item = self.list.items[self.list.mouseoverselected] or self.list:getSelectedItems()[1]
	if item then
		local pngr = item.item
		self:setSelectedPassenger(pngr)
	else
		self:setSelectedPassenger(nil)
	end
	local scale = 1
	if not self:isScaleVehicle() then
		scale = 1 / self.script:getModelScale()
	end
	local item = self.list.items[self.list.mouseoverselected]
	if item and not item.selected then
		local pngr = item.item
		for _,id in ipairs(self:getSelectedPositionIds()) do
			local posn = pngr:getPositionById(id)
			self:java3("addAxis", posn:getOffset():x() * scale, posn:getOffset():y() * scale, posn:getOffset():z() * scale)
		end
	else
		for _,posn in ipairs(self:getSelectedPositions()) do
			self:java3("addAxis", posn:getOffset():x() * scale, posn:getOffset():y() * scale, posn:getOffset():z() * scale)
		end
	end

	self:prerenderEditor2()
end

function EditPassenger:setSelectedPassenger(pngr)
	if self.selectedPassenger == pngr then
		return
	end
	self.selectedPassenger = pngr
	if not pngr then
		self.list2:clear()
		return
	end
	local posnIds = {}
	for _,item in self.list2:iteratorSelected() do
		posnIds[item.item:getId()] = true
	end
	self.list2:clear()
	self.list2:setScrollHeight(0)
	for i=1,pngr:getPositionCount() do
		local posn = pngr:getPosition(i-1)
		self.list2:addItem(posn:getId(), posn)
		if posnIds[posn:getId()] then
			self.list2.items[i].selected = true
		end
	end
	if self.list2:getSelectedCount() == 0 then
		self.list2:setSelectedRow(1)
	end
end

function EditPassenger:prerenderEditor2()
	self.list.doDrawItem = self.doDrawItem
	self.list2.doDrawItem = self.doDrawItem2

	local item = self.list.items[self.list.mouseoverselected] or self.list:getSelectedItems()[1]
	local item2 = self.list2.items[self.list2.mouseoverselected] or self.list2:getSelectedItems()[1]
	if item and item2 then
		local script = self.script
		local modelScale = script:getModelScale()
		local scale = 1
		if not self:isScaleVehicle() then
			scale = 1 / modelScale
		end

		local pngr = item.item
		local posn = item2.item
		local offset = posn:getOffset()
		
		if offset:x() > script:getExtents():x() then
			offset:setComponent(0, script:getExtents():x())
		elseif offset:x() < -script:getExtents():x() then
			offset:setComponent(0, -script:getExtents():x())
		end
		if offset:y() > script:getExtents():y() then
			offset:setComponent(1, script:getExtents():y())
		elseif offset:y() < -script:getExtents():y() then
			offset:setComponent(1, -script:getExtents():y())
		end
		if offset:z() > script:getExtents():z() then
			offset:setComponent(2, script:getExtents():z())
		elseif offset:z() < -script:getExtents():z() then
			offset:setComponent(2, -script:getExtents():z())
		end
		
		local ox = offset:x() * scale
		local oy = offset:y() * scale
		local oz = offset:z() * scale
		self:java3("setGizmoXYZ", ox, oy, oz)

		self:java4("setPassengerPosition", "character", "vehicle", pngr:getId(), posn:getId())
		self:java2("setObjectVisible", "character", true)
		self:java1("setGizmoVisible", "translate")
		self:java2("setGizmoOrigin", "vehicleModel", "vehicle")
	else
		self:java2("setObjectVisible", "character", false)
	end
end

function EditPassenger:toUI()
	EditPanel.toUI(self)
	self.list:clear()
	for i=1,self.script:getPassengerCount() do
		local pngr = self.script:getPassenger(i-1)
		self.list:addItem(pngr:getId(), pngr)
	end
	self.list:setSelectedRow(1)
end

function EditPassenger:getSelectedPositions()
	local selected = {}
	for _,item in self.list:iteratorSelected() do
		local pngr = item.item
		for _,item2 in self.list2:iteratorSelected() do
			local posn = pngr:getPositionById(item2.item:getId())
			if posn then
				table.insert(selected, posn)
			end
		end
	end
	return selected
end

function EditPassenger:getSelectedPositionIds()
	local selected = {}
	for _,item2 in self.list2:iteratorSelected() do
		table.insert(selected, item2.item:getId())
	end
	return selected
end

function EditPassenger:onGizmoStart()
	self.originalOffset = {}
	for _,posn in ipairs(self:getSelectedPositions()) do
		self.originalOffset[posn] = alignVectorToGrid(Vector3f.new(posn:getOffset()))
		if not self:isScaleVehicle() then
			self.originalOffset[posn]:mul(1 / self.script:getModelScale())
		end
	end
end

function EditPassenger:onGizmoChanged(delta)
	if self.parent.scene.gizmoAxis == "None" then return end
	local script = self.script
	local scale = 1
	if not self:isScaleVehicle() then
		scale = script:getModelScale()
	end
	for _,posn in ipairs(self:getSelectedPositions()) do
		posn:getOffset():set(self.originalOffset[posn]):add(delta)
		posn:getOffset():mul(scale)
	end
end

function EditPassenger:onGizmoCancel()
	local scale = 1
	if not self:isScaleVehicle() then
		scale = self.script:getModelScale()
	end
	for _,posn in ipairs(self:getSelectedPositions()) do
		posn:getOffset():set(self.originalOffset[posn]:mul(scale))
	end
end

function EditPassenger:new(x, y, width, height)
	local o = EditPanel.new(self, x, y, width, height)
	o:noBackground()
	return o
end

-----

function EditPhysics:createChildren()
	self.list = self:createList(0, 0, self.width, 24 * 6)
	self.list.selectionMode = "single"
	self.list.doDrawItem = self.doDrawItem

	self.list2 = self:createList(0, self.list:getBottom() + 10, self.width, 24 * 6)
	self.list2.selectionMode = "single"
	self.list2.doDrawItem = self.doDrawItem2
	self.list2.itemheight = FONT_HGT_SMALL * 3 + 2

	local buttonPadY = 4
	local buttonHgt = FONT_HGT_MEDIUM + 8

	self.transformMode = "Global"
	local button4 = ISButton:new(10, self.list2:getBottom() + buttonPadY, self.width - 20, buttonHgt, "GLOBAL", self, self.onToggleGlobalLocal)
	self:addChild(button4)
	self.button4 = button4
end

function EditPhysics:onToggleGlobalLocal()
	if self.transformMode == "Global" then
		self.transformMode = "Local"
		self.button4.title = "LOCAL"
	else
		self.transformMode = "Global"
		self.button4.title = "GLOBAL"
	end
end

function EditPhysics:doDrawItem(y, item, alt)
	local shape = item.item
	
	local x = 4
	local indent = 16

	local isMouseOver = self.mouseoverselected == item.index and not self:isMouseOverScrollBar()
	if item.selected then
		self:drawRect(0, y, self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
	elseif isMouseOver then
		self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.25, 1.0, 1.0, 1.0)
	end

	self:drawText(shape:getTypeString(), x, y, 1, 1, 1, 1, UIFont.Small)
	y = y + FONT_HGT_SMALL

	self:drawRect(x, y, self.width - 4 * 2, 2, 1.0, 0.5, 0.5, 0.5)
	y = y + 2
	return y
end

function EditPhysics:doDrawItem2(y, item, alt)
	local shape = item.item
	
	local x = 4
	local indent = 16

	local isMouseOver = self.mouseoverselected == item.index and not self:isMouseOverScrollBar()
	if item.selected then
		self:drawRect(0, y, self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
	elseif isMouseOver then
		self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.25, 1.0, 1.0, 1.0)
	end

	self:drawText(item.text, x, y, 1, 1, 1, 1, UIFont.Small)
	y = y + FONT_HGT_SMALL

	local scale = self.parent:isScaleVehicle() and 1 or (1 / self.parent.script:getModelScale())

	if item.text == "offset" then
		local v = shape:getOffset()
		drawVector(self, "   ", x + indent, y, v:x() * scale, v:y() * scale, v:z() * scale)
		y = y + FONT_HGT_SMALL
	end

	if item.text == "extents" then
		local v = shape:getExtents()
		drawVector(self, "   ", x + indent, y, v:x() * scale, v:y() * scale, v:z() * scale)
		y = y + FONT_HGT_SMALL
	end

	if item.text == "radius" then
		local radius = shape:getRadius()
		drawScalar(self, "   ", x + indent, y, radius * scale)
		y = y + FONT_HGT_SMALL
	end

	if item.text == "rotate" then
		local rotate = shape:getRotate()
		drawVector(self, "   ", x + indent, y, rotate:x(), rotate:y(), rotate:z())
		y = y + FONT_HGT_SMALL
	end

	self:drawRect(x, y, self.width - 4 * 2, 2, 1.0, 0.5, 0.5, 0.5)
	y = y + 2
	return y
end

function EditPhysics:prerenderEditor()
	local item = self.list.items[self.list.mouseoverselected] or self.list:getSelectedItems()[1]
	if item then
		local shape = item.item
		self:setSelectedShape(shape)
	else
		self:setSelectedShape(nil)
	end
	local scale = 1
	if not self:isScaleVehicle() then
		scale = 1 / self.script:getModelScale()
	end
--	local item = self.list.items[self.list.mouseoverselected]

	for i=1,self.script:getPhysicsShapeCount() do
		local shape = self.script:getPhysicsShape(i-1)
		if item and shape == item.item then
		
		elseif shape:getTypeString() == "box" then
			local offset = shape:getOffset()
			local extents = shape:getExtents()
			self:java6("addBox3D", offset, extents, shape:getRotate(), 1.0, 1.0, 1.0)
		
		elseif shape:getTypeString() == "sphere" then
			self:addSphere(shape:getOffset(), shape:getRadius(), 1.0, 1.0, 1.0)
		end
	end

	if item then
		local shape = item.item
		local offset = shape:getOffset()
		self:java3("addAxis", offset:x() * scale, offset:y() * scale, offset:z() * scale)
		if shape:getTypeString() == "box" then
			local offset = shape:getOffset()
			local extents = shape:getExtents()
			self:java6("addBox3D", offset, extents, shape:getRotate(), 0.0, 1.0, 0.0)
		end
		if shape:getTypeString() == "sphere" then
			self:addSphere(shape:getOffset(), shape:getRadius(), 0.0, 1.0, 0.0)
		end
	end

	self:prerenderEditor2()
end

function EditPhysics:addAABB(extents, offset, r, g, b)
	self:java9("addAABB", offset:x(), offset:y(), offset:z(), extents:x(), extents:y(), extents:z(), r, g, b)
end

function EditPhysics:addSphere(offset, radius, r, g, b)
	local extents = self.tempSphereExtents or Vector3f.new()
	extents:setComponent(0, radius * 2)
	extents:setComponent(1, radius * 2)
	extents:setComponent(2, radius * 2)
	self:addAABB(extents, offset, r, g, b)
end

function EditPhysics:setSelectedShape(shape)
	if self.selectedShape == shape then
		return
	end
	self.selectedShape = shape
	if not shape then
		self.list2:clear()
		return
	end
	local selected = {}
	for index,item in self.list2:iteratorSelected() do
		selected[index] = true
	end
	self.list2:clear()
	self.list2:setScrollHeight(0)

	self.list2:addItem("offset", shape)
	self.list2.items[1].selected = selected[1] ~= nil

	if shape:getTypeString() == "box" then
		self.list2:addItem("extents", shape)
		self.list2.items[2].selected = selected[2] ~= nil

		self.list2:addItem("rotate", shape)
		self.list2.items[3].selected = selected[3] ~= nil
	end

	if shape:getTypeString() == "sphere" then
		self.list2:addItem("radius", shape)
		self.list2.items[1].selected = selected[1] ~= nil
	end

	if self.list2:getSelectedCount() == 0 then
		self.list2:setSelectedRow(1)
	end
end

function EditPhysics:prerenderEditor2()
	self.list.doDrawItem = self.doDrawItem
	self.list2.doDrawItem = self.doDrawItem2

	local item = self.list.items[self.list.mouseoverselected] or self.list:getSelectedItems()[1]
	local item2 = self.list2.items[self.list2.mouseoverselected] or self.list2:getSelectedItems()[1]
	if item and item2 then
		local script = self.script
		local modelScale = script:getModelScale()
		local scale = 1
		if not self:isScaleVehicle() then
			scale = 1 / modelScale
		end
		local shape = item.item
		local offset = shape:getOffset()
		local ox = offset:x() * scale
		local oy = offset:y() * scale
		local oz = offset:z() * scale
		self:java3("setGizmoXYZ", ox, oy, oz)
		self:java2("setGizmoOrigin", "none")
		if shape:getTypeString() == "box" then
			self:java1("setGizmoRotate", shape:getRotate())
		else
			self:java0("clearGizmoRotate")
		end
		self:java1("setTransformMode", self.transformMode)
		if item2.text == "offset" then
			self:java1("setGizmoVisible", "translate")
		end
		if item2.text == "extents" or item2.text == "radius" then
			self:java1("setGizmoVisible", "scale")
		end
		if item2.text == "rotate" then
			self:java1("setGizmoVisible", "rotate")
		end
	end
end

function EditPhysics:toUI()
	EditPanel.toUI(self)
	self.list:clear()
	for i=1,self.script:getPhysicsShapeCount() do
		local shape = self.script:getPhysicsShape(i-1)
		self.list:addItem(shape:getTypeString(), shape)
	end
	self.list:setSelectedRow(1)
end

function EditPhysics:indexOfShape(shape)
	for i=1,self.script:getPhysicsShapeCount() do
		local shape2 = self.script:getPhysicsShape(i-1)
		if shape == shape2 then
			return i-1
		end
	end
	return -1
end

function EditPhysics:getSelectedShapes()
	local selected = {}
	for _,item in self.list:iteratorSelected() do
		local index = self:indexOfShape(item.item)
		if index ~= -1 then
			table.insert(selected, self.script:getPhysicsShape(index))
		end
	end
	return selected
end

local function alignScalarToGrid(s)
	return math.floor(s * 100 + 0.5f) / 100
end

function EditPhysics:onGizmoStart()
	self.originalValue = {}
	for _,shape in ipairs(self:getSelectedShapes()) do
		local t = {}
		t.offset = alignVectorToGrid(Vector3f.new(shape:getOffset()))
		t.extents = alignVectorToGrid(Vector3f.new(shape:getExtents()))
		t.rotate = Vector3f.new(shape:getRotate())
		t.radius = alignScalarToGrid(shape:getRadius())
		if not self:isScaleVehicle() then
			t.offset:mul(1 / self.script:getModelScale())
			t.extents:mul(1 / self.script:getModelScale())
			t.radius = t.radius * (1 / self.script:getModelScale())
		end
		self.originalValue[shape] = t
	end
end

function EditPhysics:onGizmoChanged(delta)
	if self.parent.scene.gizmoAxis == "None" then return end
	local script = self.script
	local scale = 1
	if not self:isScaleVehicle() then
		scale = script:getModelScale()
	end
	local item2 = self.list2:getSelectedItems()[1]
	if not item2 then return end
	for _,shape in ipairs(self:getSelectedShapes()) do
		local val = self.originalValue[shape]
		if item2.text == "offset" then
			shape:getOffset():set(val.offset):add(delta)
			shape:getOffset():mul(scale)
		end
		if item2.text == "extents" then
			local extents = Vector3f.new(delta)
			extents:mul(2 * scale)
			extents:add(val.extents)
			if extents:x() < 0.1 then extents:setComponent(0, 0.1) end
			if extents:y() < 0.1 then extents:setComponent(1, 0.1) end
			if extents:z() < 0.1 then extents:setComponent(2, 0.1) end
			shape:getExtents():set(extents)
		end
		if item2.text == "rotate" then
			self:java2("applyDeltaRotation", shape:getRotate():set(val.rotate), delta)
		end
		if item2.text == "radius" then
			local dxyz = delta:x()
			if math.abs(delta:y()) > math.abs(dxyz) then
				dxyz = delta:y()
			end
			if math.abs(delta:z()) > math.abs(dxyz) then
				dxyz = delta:z()
			end
			shape:setRadius(val.radius + dxyz * scale)
		end
	end
end

function EditPhysics:onGizmoCancel()
	local scale = 1
	if not self:isScaleVehicle() then
		scale = self.script:getModelScale()
	end
	for _,shape in ipairs(self:getSelectedShapes()) do
		local val = self.originalValue[shape]
		shape:getOffset():set(val.offset:mul(scale))
		shape:getExtents():set(val.extents:mul(scale))
		shape:getRotate():set(val.rotate)
		shape:setRadius(val.radius)
	end
end

function EditPhysics:new(x, y, width, height)
	local o = EditPanel.new(self, x, y, width, height)
	o:noBackground()
	return o
end

-----

function EditWheel:createChildren()
	self.list = self:createList()
	self.list.doDrawItem = self.doDrawItem
end

function EditWheel:doDrawItem(y, item, alt)
	local wheel = item.item
	
	local x = 4
	local indent = 16

	local isMouseOver = self.mouseoverselected == item.index and not self:isMouseOverScrollBar()
	if item.selected then
		self:drawRect(0, y, self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
	elseif isMouseOver then
		self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.25, 1.0, 1.0, 1.0)
	end

	self:drawText(wheel:getId(), x, y, 1, 1, 1, 1, UIFont.Small)
	y = y + FONT_HGT_SMALL

	local offset = wheel:getOffset()
	local scale = self.parent:isScaleVehicle() and 1 or (1 / self.parent.script:getModelScale())
	drawVector(self, "offset:   ", x + indent, y, offset:x() * scale, offset:y() * scale, offset:z() * scale)
	y = y + FONT_HGT_SMALL

	self:drawRect(x, y, self.width - 4 * 2, 2, 1.0, 0.5, 0.5, 0.5)
	y = y + 2
	return y
end

function EditWheel:toUI()
	EditPanel.toUI(self)
	self.list:clear()
	for i=1,self.script:getWheelCount() do
		local wheel = self.script:getWheel(i-1)
		self.list:addItem(wheel:getId(), wheel)
	end
end

function EditWheel:prerenderEditor()
	self.list.doDrawItem = self.doDrawItem
	local script = self.script
	local scale = 1
	if not self:isScaleVehicle() then
		scale = 1 / script:getModelScale()
	end
	local mouseOver = self.list.items[self.list.mouseoverselected]
	local item = mouseOver or self.list:getSelectedItems()[1]
	if item then
		local wheel = item.item
		local offset = wheel:getOffset()
		local ox = offset:x() * scale
		local oy = offset:y() * scale
		local oz = offset:z() * scale
		self:java3("setGizmoXYZ", ox, oy, oz)
		self:java1("setGizmoVisible", "translate")
		self:java2("setGizmoOrigin", "vehicleModel", "vehicle")
	end
	if mouseOver then
		local wheel = mouseOver.item
		self:java3("addAxis", wheel:getOffset():x() * scale, wheel:getOffset():y() * scale, wheel:getOffset():z() * scale)
	else
		for _,item in self.list:iteratorSelected() do
			local wheel = item.item
			self:java3("addAxis", wheel:getOffset():x() * scale, wheel:getOffset():y() * scale, wheel:getOffset():z() * scale)
		end
	end
end

function EditWheel:getSelectedWheels()
	local selected = {}
	for _,item in self.list:iteratorSelected() do
		table.insert(selected, item.item)
	end
	return selected
end

function EditWheel:onGizmoStart()
	self.originalOffset = {}
	for _,wheel in ipairs(self:getSelectedWheels()) do
		self.originalOffset[wheel] = alignVectorToGrid(Vector3f.new(wheel:getOffset()))
		if not self:isScaleVehicle() then
			self.originalOffset[wheel]:mul(1 / self.script:getModelScale())
		end
	end
end

function EditWheel:onGizmoChanged(delta)
	if self.parent.scene.gizmoAxis == "None" then return end
	local script = self.script
	local scale = 1
	if not self:isScaleVehicle() then
		scale = script:getModelScale()
	end
	for _,wheel in ipairs(self:getSelectedWheels()) do
		wheel:getOffset():set(self.originalOffset[wheel]):add(delta)
		wheel:getOffset():mul(scale)
	end
end

function EditWheel:onGizmoCancel()
	local scale = 1
	if not self:isScaleVehicle() then
		scale = self.script:getModelScale()
	end
	for _,wheel in ipairs(self:getSelectedWheels()) do
		wheel:getOffset():set(self.originalOffset[wheel]):mul(scale)
	end
end

function EditWheel:new(x, y, width, height)
	local o = EditPanel.new(self, x, y, width, height)
	return o
end

-----

function Scene:prerenderEditor()
	self.javaObject:fromLua1("setGizmoVisible", "none")
	self.javaObject:fromLua1("setGizmoOrigin", "none")
	self.javaObject:fromLua1("setTransformMode", "Global")
	self.javaObject:fromLua0("clearGizmoRotate")
	self.javaObject:fromLua0("clearAABBs")
	self.javaObject:fromLua0("clearAxes")
	self.javaObject:fromLua0("clearBox3Ds")
end

function Scene:prerender()
	ISUI3DScene.prerender(self)
end

function Scene:onMouseDown(x, y)
	ISUI3DScene.onMouseDown(self, x, y)
	self.gizmoAxis = self.javaObject:fromLua2("testGizmoAxis", x, y)
	if self.gizmoAxis ~= "None" then
		local scenePos = self.javaObject:fromLua0("getGizmoPos")
		self.gizmoStartScenePos = alignVectorToGrid(Vector3f.new(scenePos))
		self.gizmoClickScenePos = alignVectorToGrid(self.javaObject:uiToScene(x, y, 0, Vector3f.new()))
		self.javaObject:fromLua3("startGizmoTracking", x, y, self.gizmoAxis)
		self:onGizmoStart()
	end
end

function Scene:onMouseMove(dx, dy)
	if self.gizmoAxis == "None" then
		ISUI3DScene.onMouseMove(self, dx, dy)
	else
		local x,y = self:getMouseX(),self:getMouseY()
		local newPos = alignVectorToGrid(self.javaObject:uiToScene(x, y, 0, Vector3f.new()))
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

function EditVehicleUI:createChildren()
	self.scene = Scene:new(0, 0, self.width, self.height)
	self.scene:initialise()
	self.scene:instantiate()
	self.scene:setAnchorRight(true)
	self.scene:setAnchorBottom(true)
	self:addChild(self.scene)

	self.scene.javaObject:fromLua1("setGizmoScale", 1.0 / 5.0)

	self.scene.javaObject:fromLua1("setDrawGridAxes", true)

	self.scene.javaObject:fromLua1("createVehicle", "vehicle")

	local initialScript = getEditVehicleState():fromLua0("getInitialScript")
	if initialScript then
		self.scene.javaObject:fromLua2("setVehicleScript", "vehicle", initialScript)
	end

	self.scene.javaObject:fromLua1("createCharacter", "character")
	self.scene.javaObject:fromLua2("setObjectVisible", "character", false)

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
	local viewX = (self.width - #viewNames * viewW + (#viewNames - 1) * 10) / 2
	local viewY = 0
	self.views = {}
	for _,viewName in ipairs(viewNames) do
		local view = SwitchView:new(self, viewX, viewY, viewW, viewH)
		view:initialise()
		view:instantiate()
		view:setAnchorTop(false)
		view:setAnchorRight(false)
		view:setAnchorBottom(true)
		view:setView(viewName)
		view.javaObject:fromLua1("setZoom", 4)
		view.javaObject:fromLua1("setDrawGrid", false)
		view.javaObject:fromLua1("createVehicle", "vehicle1")
		self.bottomPanel:addChild(view)
		table.insert(self.views, view)
		viewX = viewX + viewW + 10
	end

	local width = 250
	local height = FONT_HGT_MEDIUM + 3 * 2
	local combo = ISComboBox:new(self.width / 2 - width / 2, 10, width, height, self, self.onComboVehicle)
	combo:setEditable(true)
	combo:initialise()
	combo:instantiate()
	combo:setAnchorLeft(false)
	self:addChild(combo)
	self.scriptCombo = combo

	local scripts = getScriptManager():getAllVehicleScripts()
	local sorted = {}
	for i=1,scripts:size() do
		local script = scripts:get(i-1)
		table.insert(sorted, script:getFullName())
	end
	table.sort(sorted)
	local scriptNameScene = self.scene.javaObject:fromLua1("getVehicleScript", "vehicle"):getFullName()
	for _,scriptName in ipairs(sorted) do
		combo:addOption(scriptName)
		if scriptName == scriptNameScene then
			combo.selected = #combo.options
		end
	end

	combo = ISComboBox:new(10, 10, width, height, self, self.onComboEdit)
	combo:initialise()
	combo:instantiate()
	self:addChild(combo)
	self.editCombo = combo

	self.editUI = {}
	self.editUI.current = nil

	local ui = EditChassis:new(10, combo:getBottom() + 10, 250, 400)
	ui:setVisible(false)
	self:addChild(ui)
	self.editUI.chassis = ui

	ui = EditArea:new(10, combo:getBottom() + 10, 250, 400)
	ui:setVisible(false)
	self:addChild(ui)
	self.editUI.area = ui

	ui = EditAttachment:new(10, combo:getBottom() + 10, 250, 400)
	ui:setVisible(false)
	self:addChild(ui)
	self.editUI.attachment = ui

	ui = EditPassenger:new(10, combo:getBottom() + 10, 250, 400)
	ui:setVisible(false)
	self:addChild(ui)
	self.editUI.passenger = ui

	ui = EditPhysics:new(10, combo:getBottom() + 10, 250, 400)
	ui:setVisible(false)
	self:addChild(ui)
	self.editUI.physics = ui

	ui = EditWheel:new(10, combo:getBottom() + 10, 250, 400)
	ui:setVisible(false)
	self:addChild(ui)
	self.editUI.wheel = ui

	combo:addOptionWithData("Chassis", self.editUI.chassis)
	combo:addOptionWithData("Areas", self.editUI.area)
	combo:addOptionWithData("Attachments", self.editUI.attachment)
	combo:addOptionWithData("Passengers", self.editUI.passenger)
	combo:addOptionWithData("Physics", self.editUI.physics)
	combo:addOptionWithData("Wheels", self.editUI.wheel)

	local buttonHgt = FONT_HGT_MEDIUM + 8;
	local button = ISButton:new(10, self.bottomPanel.height - 10 - buttonHgt, 80, buttonHgt, "SAVE", self, self.onSave)
	self.bottomPanel:addChild(button)

	local button2 = ISButton:new(button:getRight() + 10, button.y, 80, buttonHgt, "EXIT", self, self.onExit)
	self.bottomPanel:addChild(button2)

	local button3 = ISButton:new(10, button.y - 10 - buttonHgt, 80 * 2 + 10, buttonHgt, "RELOAD  ALL", self, self.onReload)
	self.bottomPanel:addChild(button3)

	local label = ISLabel:new(10, self.bottomPanel.y - 30, FONT_HGT_SMALL, "", 1, 1, 1, 1, UIFont.Small, true)
	label:setAnchorTop(false)
	label:setAnchorBottom(true)
	self:addChild(label)
	self.wroteScriptLabel = label

	self:alignEverythingToGrid()

	self.editUI.attachment:doLayout()

	self:setEditUI(self.editUI.chassis)
end

function EditVehicleUI:onResolutionChange(oldw, oldh, neww, newh)
	self:setWidth(neww)
	self:setHeight(newh)
	self.scriptCombo:setX(self.width / 2 - self.scriptCombo.width / 2)
	self.bottomPanel:setX(self.width / 2 - self.bottomPanel.width / 2)
	self.editUI.attachment:doLayout()
end

function EditVehicleUI:update()
	ISPanel.update(self)
	if self.width ~= getCore():getScreenWidth() or self.height ~= getCore():getScreenHeight() then
		self:onResolutionChange(self.width, self.height, getCore():getScreenWidth(), getCore():getScreenHeight())
	end
end

function EditVehicleUI:prerender()
	ISPanel.prerender(self)
	--[[
	local vehicle = getEditVehicleState():fromLua0("getVehicle")
	self.scene:setVehicle(vehicle)
	for _,view in ipairs(self.views) do
		view:setVehicle(vehicle)
	end
	--]]
	self.scene:prerenderEditor()
	if self.editUI and self.editUI.current then
		self.editUI.current:prerenderEditor()
	end
	if (self.wroteScriptLabel.name ~= "") and (self.wroteScriptTime + 5000 < getTimestampMs()) then
		self.wroteScriptLabel:setName("")
	end
end

function EditVehicleUI:onComboEdit()
	local ui = self.editCombo:getOptionData(self.editCombo.selected)
	self:setEditUI(ui)
end

function EditVehicleUI:onComboVehicle()
	local scriptName = self.scriptCombo:getOptionText(self.scriptCombo.selected)
	self:setScript(scriptName)
end

function EditVehicleUI:setScript(scriptName)
	self.scene.javaObject:fromLua2("setVehicleScript", "vehicle", scriptName)
	for _,view in ipairs(self.views) do
		view.javaObject:fromLua2("setVehicleScript", "vehicle1", scriptName)
	end
	self:alignEverythingToGrid()
	self:setEditUI(self.editUI.current)
end

function EditVehicleUI:setEditUI(ui)
	self.scene.javaObject:fromLua2("setObjectVisible", "character", false)
	if self.editUI.current then
		self.editUI.current:setVisible(false)
	end
	self.editUI.current = ui
	if ui then
		ui:toUI()
		ui:setVisible(true)
	end
end

function EditVehicleUI:alignEverythingToGrid()
	local script = self.scene.javaObject:fromLua1("getVehicleScript", "vehicle")
	alignVectorToGrid(script:getModel():getOffset())
	alignVectorToGrid(script:getCenterOfMassOffset())
	alignExtentsToGrid(script:getExtents())
	alignExtentsToGrid(script:getPhysicsChassisShape())
	for i=1,script:getAreaCount() do
		alignAreaToGrid(script:getArea(i-1))
	end
	for i=1,script:getWheelCount() do
		alignVectorToGrid(script:getWheel(i-1):getOffset())
	end
	for i=1,script:getPassengerCount() do
		local pngr = script:getPassenger(i-1)
		for j=1,pngr:getPositionCount() do
			alignVectorToGrid(pngr:getPosition(j-1):getOffset())
		end
	end
end

function EditVehicleUI:onSave(button, x, y)
	local script = self.scene.javaObject:fromLua1("getVehicleScript", "vehicle")
	getEditVehicleState():fromLua1("writeScript", script:getFullName())
	script:toBullet()
end

function EditVehicleUI:onExit(button, x, y)
	getEditVehicleState():fromLua0("exit")
end

function EditVehicleUI:onReload(button, x, y)
	local script = self.scene.javaObject:fromLua1("getVehicleScript", "vehicle")
	local scriptName = script:getFullName()
	reloadVehicles()
	self:setScript(scriptName)
end

-- Called from Java
function EditVehicleUI:showUI()
	local scriptNameScene = self.scene.javaObject:fromLua1("getVehicleScript", "vehicle"):getFullName()
	self.scriptCombo:select(scriptNameScene)
end

-- Called from Java
function EditVehicleUI:wroteScript(fileName)
	self.wroteScriptTime = getTimestampMs()
	self.wroteScriptLabel:setName(fileName)
end

function EditVehicleUI:new(x, y, width, height)
	local o = ISPanel.new(self, x, y, width, height)
	o:setAnchorRight(true)
	o:setAnchorBottom(true)
	o:noBackground()
	getEditVehicleState():setTable(o)
	return o
end

-- Called from Java
function EditVehicleState_InitUI()
	local UI = EditVehicleUI:new(0, 0, getCore():getScreenWidth(), getCore():getScreenHeight())
	EditVehicleState_UI = UI
	UI:setVisible(true)
	UI:addToUIManager()
end
