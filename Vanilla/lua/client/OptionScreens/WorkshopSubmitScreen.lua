--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanelJoypad"
require "ISUI/ISComboBox"
require "ISUI/ISScrollingListBox"
require "ISUI/ISTextEntryBox"

WorkshopSubmitScreen = ISPanelJoypad:derive("WorkshopSubmit")
local Page1 = ISPanelJoypad:derive("Page1")
local Page2 = ISPanelJoypad:derive("Page2")
local Page3 = ISPanelJoypad:derive("Page3")
local Page4 = ISPanelJoypad:derive("Page4")
local Page5 = ISPanelJoypad:derive("Page5")
local Page6 = ISPanelJoypad:derive("Page6")
local Page7 = ISPanelJoypad:derive("Page7")
local Page8 = ISPanelJoypad:derive("Page8")
local Page9 = ISPanelJoypad:derive("Page9")
local Page10 = ISPanelJoypad:derive("Page10")
local TagsList = ISScrollingListBox:derive("TagsList")

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

local TEST = false
WorkshopSubmitScreen.TEST = TEST

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function Page1:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.anchorBottom = true
	o.anchorRight = true
	return o
end

function Page1:create()
	local padX = 96
	local labelHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local label1 = ISLabel:new(padX, 64, labelHgt, getText("UI_WorkshopSubmit_ContentFolder"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label1)

	local label2 = ISLabel:new(padX, label1:getBottom() + 2, labelHgt, Core.getMyDocumentFolder() .. getFileSeparator() .. "Workshop", 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label2)

	self.listbox = ISScrollingListBox:new(padX, 128, self.width - padX * 2, 400)
	self.listbox:initialise()
	self.listbox:setAnchorLeft(true)
	self.listbox:setAnchorRight(true)
	self.listbox:setAnchorTop(true)
	self.listbox:setAnchorBottom(true)
	self.listbox:setFont("Medium", 4)
	self.listbox.drawBorder = true
	self.listbox.doDrawItem = self.doDrawItem
	self.listbox:setOnMouseDownFunction(self, self.onMouseDownListbox)
	self:addChild(self.listbox)

	local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

	self.backButton = ISButton:new(16, self.height - 5 - buttonHgt, 100, buttonHgt, getText("UI_btn_back"), self, self.onButtonBack)
	self.backButton.internal = "BACK"
	self.backButton:initialise()
	self.backButton:setAnchorLeft(true)
	self.backButton:setAnchorTop(false)
	self.backButton:setAnchorBottom(true)
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.backButton)

	self.nextButton = ISButton:new(self.width - 16 - 100, self.height - 5 - buttonHgt, 100, buttonHgt, getText("UI_btn_next"), self, self.onButtonNext)
	self.nextButton.internal = "NEXT"
	self.nextButton:initialise()
	self.nextButton:setAnchorLeft(false)
	self.nextButton:setAnchorRight(true)
	self.nextButton:setAnchorTop(false)
	self.nextButton:setAnchorBottom(true)
	self.nextButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.nextButton)
end

function Page1:updateWhenVisible()
	self.nextButton:setEnable(self.listbox.items[self.listbox.selected] ~= nil)
end

function Page1:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_WorkshopSubmit_Title1"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	if getDebug() then self:drawTextCentre("DEBUG: Page1", self.width / 2, self.height - 20, 0.5, 0.5, 0.5, 1, UIFont.Small) end
	self:updateWhenVisible()
end

function Page1:doDrawItem(y, item, alt)
	self:drawRectBorder(0, y, self:getWidth(), self.itemheight - 1, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b)

	if self.selected == item.index then
		self:drawRect(0, y, self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15)
	end

	local dy = (self.itemheight - getTextManager():getFontFromEnum(self.font):getLineHeight()) / 2
	self:drawText(item.text, 8, y + dy, 0.9, 0.9, 0.9, 0.9, self.font)

	return y + item.height
end

function Page1:onMouseDownListbox(item)
end

function Page1:aboutToShow()
	self.listbox:clear()
	local stagedItems = getSteamWorkshopStagedItems()
	for i=1,stagedItems:size() do
		self.listbox:addItem(stagedItems:get(i-1):getFolderName(), stagedItems:get(i-1))
	end
end

function Page1:onButtonBack()
	self:setVisible(false)
	self.parent.page9:setVisible(true, self.joyfocus)
end

function Page1:onButtonNext()
	local item = self.listbox.items[self.listbox.selected].item
	self.parent.item = item
	self:setVisible(false)
	local err = item:validateContents()
	if err then
		self.parent.page8:setFields(err, self)
		self.parent.page8:setVisible(true, self.joyfocus)
	else
		self.parent.page2:setWorkshopItem(item)
		self.parent.page2:setVisible(true, self.joyfocus)
	end
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function Page2:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.anchorBottom = true
	o.anchorRight = true
	return o
end

function Page2:create()
	local entryWid = 400
	local padX = (self.width - (entryWid + 32 + 256)) / 2
	local labelHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local label1 = ISLabel:new(padX, 64, labelHgt, getText("UI_WorkshopSubmit_ItemTitle"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label1)

	local entryHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight() + 2 * 2
	self.titleEntry = ISTextEntryBox:new("", padX, label1:getBottom() + 2, entryWid, entryHgt)
	self.titleEntry.font = UIFont.Medium
	self.titleEntry:initialise()
	self:addChild(self.titleEntry)
	
	local label2 = ISLabel:new(padX, self.titleEntry:getBottom() + 8, labelHgt, getText("UI_WorkshopSubmit_ItemDescription"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label2)

	local inset = 4
	self.description = ISTextEntryBox:new("", padX, label2:getBottom() + 2, entryWid, labelHgt * 4 + inset * 2)
	self.description.font = UIFont.Medium
	self.description:initialise()
	self.description:instantiate()
	self.description:setEditable(false)
	self.description:setMultipleLine(true)
	self.description:addScrollBars()
	self:addChild(self.description)

	self.editDescription = ISButton:new(padX, self.description:getBottom() + 4, 10, entryHgt, getText("UI_WorkshopSubmit_EditDescription"), self, self.onButtonEditDescription)
	self.editDescription:initialise()
	self.editDescription:setX(self.description:getRight() - self.editDescription:getWidth())
	self.editDescription.borderColor = {r=1, g=1, b=1, a=0.25}
	self:addChild(self.editDescription)

	local label6 = ISLabel:new(padX, self.editDescription:getBottom() + 12, labelHgt, getText("UI_WorkshopSubmit_ItemTags"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label6)

	local itemHgt = labelHgt + 4
	self.tags = TagsList:new(padX, label6:getBottom() + 2, entryWid / 2, itemHgt * 4)
	self.tags:initialise()
	self.tags:setAnchorLeft(true)
	self.tags:setAnchorRight(false)
	self.tags:setAnchorTop(true)
	self.tags:setAnchorBottom(false)
	self:addChild(self.tags)

	local label3 = ISLabel:new(padX, self.tags:getBottom() + 12, labelHgt, getText("UI_WorkshopSubmit_ItemVisibility"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label3)

	self.visibility = ISComboBox:new(padX, label3:getBottom() + 2, 200, labelHgt)
	self.visibility:addOption(getText("UI_WorkshopSubmit_VisibilityPublic"))
	self.visibility:addOption(getText("UI_WorkshopSubmit_VisibilityFriendsOnly"))
	self.visibility:addOption(getText("UI_WorkshopSubmit_VisibilityPrivate"))
	self.visibility:addOption(getText("UI_WorkshopSubmit_VisibilityUnlisted"))
	self:addChild(self.visibility)
--[[
	local label4 = ISLabel:new(padX, self.visibility:getBottom() + 12, labelHgt, getText("UI_WorkshopSubmit_ItemID"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label4)
	
	self.IDEntry = ISTextEntryBox:new("", padX, label4:getBottom() + 2, entryWid, entryHgt)
	self.IDEntry.font = UIFont.Medium
	self.IDEntry:initialise()
	self:addChild(self.IDEntry)
--]]

	local label5 = ISLabel:new(self.titleEntry:getRight() + 32, 64, labelHgt, getText("UI_WorkshopSubmit_ItemPreview"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label5)

	local dy = (self.height - 30 - self.visibility:getBottom()) / 3
	local uis = self:getChildren()
	for _,ui in pairs(uis) do
		ui:setY(ui:getY() + dy)
	end

	local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

	self.overlayButton = ISButton:new(self.width / 2 - entryWid / 2, self.height - 5 - buttonHgt, entryWid, buttonHgt, getText("UI_WorkshopSubmit_OverlayButton"), self, self.onButtonOverlay)
	self.overlayButton.internal = "OVERLAY"
	self.overlayButton:initialise()
	self.overlayButton.borderColor = {r=1, g=1, b=1, a=0.25}
	self:addChild(self.overlayButton)

	self.backButton = ISButton:new(16, self.height - 5 - buttonHgt, 100, buttonHgt, getText("UI_btn_back"), self, self.onButtonBack)
	self.backButton.internal = "BACK"
	self.backButton:initialise()
	self.backButton:setAnchorLeft(true)
	self.backButton:setAnchorTop(false)
	self.backButton:setAnchorBottom(true)
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.backButton)

	self.nextButton = ISButton:new(self.width - 16 - 100, self.height - 5 - buttonHgt, 100, buttonHgt, getText("UI_btn_next"), self, self.onButtonNext)
	self.nextButton.internal = "NEXT"
	self.nextButton:initialise()
	self.nextButton:setAnchorLeft(false)
	self.nextButton:setAnchorRight(true)
	self.nextButton:setAnchorTop(false)
	self.nextButton:setAnchorBottom(true)
	self.nextButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.nextButton)
end

function Page2:updateWhenVisible()
	local valid = self:validate()
	self.nextButton:setEnable(valid)
end

function Page2:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_WorkshopSubmit_Title2"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	if getDebug() then self:drawTextCentre("DEBUG: Page2", self.width / 2, self.height + 2, 0.5, 0.5, 0.5, 1, UIFont.Small) end

	local x = self.titleEntry:getRight() + 32
	local y = self.titleEntry:getY()
	self:drawRectBorder(x, y, 256 + 4 * 2, 256 + 4 * 2, 1, 0.4, 0.4, 0.4)
	self:drawRectBorder(x + 1, y + 1, 256 + 4 * 2 - 2, 256 + 4 * 2 - 2, 1, 0.4, 0.4, 0.4)
	self:drawTextureScaledAspect(self.preview, x + 4, y + 4, 256, 256, 1, 1, 1, 1)

	self:updateWhenVisible()
end

function Page2:setWorkshopItem(item)
	self.item = item
	
	self.titleEntry:setText(item:getTitle() or "")
	self.description:setText(item:getDescription() or "")

	if item:getVisibility() == "public" then
		self.visibility.selected = 1
	elseif item:getVisibility() == "friendsOnly" then
		self.visibility.selected = 2
	elseif item:getVisibility() == "private" then
		self.visibility.selected = 3
	elseif item:getVisibility() == "unlisted" then
		self.visibility.selected = 4
	else
		self.visibility.selected = 1
	end

--	self.IDEntry:setText(item:getID())

	local allowedTags = SteamWorkshopItem.getAllowedTags()
	self.tags:setTags(allowedTags)
	self.tags:setCheckedTags(item:getTags())

	self.overlayButton:setEnable(item:getID() ~= nil and isSteamOverlayEnabled())

	self.preview = getTexture(self.item:getPreviewImage())
end

function Page2:validate()
	if string.trim(self.titleEntry:getText()) == "" then return false end
	if string.trim(self.description:getText()) == "" then return false end
	return true
end

function Page2:onButtonEditDescription()
	self:setVisible(false)
	self.parent.page6:setFields(getText("UI_WorkshopSubmit_TitleEditDescription"), self.description:getText(), self)
	self.parent.page6:setVisible(true, self.joyfocus)
end

function Page2:onTextChanged(text)
	self.description:setText(text)
end

function Page2:onButtonOverlay()
	activateSteamOverlayToWorkshopItem(self.item:getID())
end

function Page2:onButtonBack()
	self:setVisible(false)
	self.parent.page1:setVisible(true, self.joyfocus)
end

function Page2:onButtonNext()
	self.item:setTitle(self.titleEntry:getText())
	self.item:setDescription(self.description:getText())
	self.item:setTags(self.tags:getCheckedTags())
	self.item:setVisibilityInteger(self.visibility.selected - 1)
	self.item:writeWorkshopTxt()
	
	self:setVisible(false)
	if self.item:getID() then
		self.parent.page5:setFields(self)
		self.parent.page5:setVisible(true, self.joyfocus)
	else
		self.parent.page3:setVisible(true, self.joyfocus)
	end
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function Page3:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.anchorBottom = true
	o.anchorRight = true
	return o
end

function Page3:create()
	local padX = 24
	local labelHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local label1 = ISLabel:new(padX, 64, labelHgt, getText("UI_WorkshopSubmit_UnknownItem"), 1, 1, 1, 1, UIFont.Medium, true)
--	self:addChild(label1)

	local buttonWid = 500
	local buttonHgt = math.max(48, FONT_HGT_MEDIUM * 2 + 3 * 2)
	local buttonGapY = 24

	local title2 = getText("UI_WorkshopSubmit_BtnExistingItem"):gsub("\\n", "\n")
	local title2Wid = getTextManager():MeasureStringX(UIFont.Medium, title2) + 10
	buttonWid = math.max(buttonWid, title2Wid)
	
	self.button1 = ISButton:new(self.width / 2 - buttonWid / 2, 64, buttonWid, buttonHgt, getText("UI_WorkshopSubmit_BtnNewItem"):gsub("\\n", "\n"), self, self.onButtonCreate)
	self.button1.internal = "CREATE"
	self.button1:initialise()
	self.button1:setAnchorLeft(true)
	self.button1:setAnchorTop(true)
	self.button1:setAnchorBottom(false)
	self.button1.borderColor = {r=1, g=1, b=1, a=0.25}
	self.button1:setFont(UIFont.Medium)
	self:addChild(self.button1)

	self.button2 = ISButton:new(self.width / 2 - buttonWid / 2, self.button1:getBottom() + 24, buttonWid, buttonHgt, title2, self, self.onButtonEnterID)
	self.button2.internal = "ENTERID"
	self.button2:initialise()
	self.button2:setAnchorLeft(true)
	self.button2:setAnchorTop(true)
	self.button2:setAnchorBottom(false)
	self.button2.borderColor = {r=1, g=1, b=1, a=0.25}
	self.button2:setFont(UIFont.Medium)
	self:addChild(self.button2)

	local dy = (self.height - 30 - self.button2:getBottom()) / 3
	local uis = self:getChildren()
	for _,ui in pairs(uis) do
		ui:setY(ui:getY() + dy)
	end

    local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

	self.backButton = ISButton:new(16, self.height - 5 - buttonHgt, 100, buttonHgt, getText("UI_btn_back"), self, self.onButtonBack)
	self.backButton.internal = "BACK"
	self.backButton:initialise()
	self.backButton:setAnchorLeft(true)
	self.backButton:setAnchorTop(false)
	self.backButton:setAnchorBottom(true)
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.backButton)
end

function Page3:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_WorkshopSubmit_Title3"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	if getDebug() then self:drawTextCentre("DEBUG: Page3", self.width / 2, self.height - 20, 0.5, 0.5, 0.5, 1, UIFont.Small) end
end

function Page3:onButtonCreate()
	self:setVisible(false)
	self.parent.item:setID(null)
	self.parent.page5:setFields(self)
	self.parent.page5:setVisible(true, self.joyfocus)
end

function Page3:onButtonEnterID()
	self:setVisible(false)
	self.parent.page4:setFields()
	self.parent.page4:setVisible(true, self.joyfocus)
end

function Page3:onButtonBack()
	self:setVisible(false)
	self.parent.page2:setVisible(true, self.joyfocus)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function Page4:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.anchorBottom = true
	o.anchorRight = true
	return o
end

function Page4:create()
--	local padX = 24
	local labelHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
--	local label1 = ISLabel:new(padX, 64, labelHgt, getText("UI_WorkshopSubmit_UnknownItem"), 1, 1, 1, 1, UIFont.Medium, true)
--	self:addChild(label1)
	
	local entryWid = 400
	local entryHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight() + 2 * 2

	local label4 = ISLabel:new(self.width / 2 - entryWid / 2, 64, labelHgt, getText("UI_WorkshopSubmit_ItemID"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label4)

	self.IDEntry = ISTextEntryBox:new("", label4:getX(), label4:getBottom() + 2, entryWid, entryHgt)
	self.IDEntry.font = UIFont.Medium
	self.IDEntry:initialise()
	self.IDEntry:instantiate()
	self.IDEntry:setOnlyNumbers(true)
	self:addChild(self.IDEntry)

	local dy = (self.height - 30 - self.IDEntry:getBottom()) / 3
	local uis = self:getChildren()
	for _,ui in pairs(uis) do
		ui:setY(ui:getY() + dy)
	end

    local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

	self.backButton = ISButton:new(16, self.height - 5 - buttonHgt, 100, buttonHgt, getText("UI_btn_back"), self, self.onButtonBack)
	self.backButton.internal = "BACK"
	self.backButton:initialise()
	self.backButton:setAnchorLeft(true)
	self.backButton:setAnchorTop(false)
	self.backButton:setAnchorBottom(true)
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.backButton)

	self.nextButton = ISButton:new(self.width - 16 - 100, self.height - 5 - buttonHgt, 100, buttonHgt, getText("UI_btn_next"), self, self.onButtonNext)
	self.nextButton.internal = "NEXT"
	self.nextButton:initialise()
	self.nextButton:setAnchorLeft(false)
	self.nextButton:setAnchorRight(true)
	self.nextButton:setAnchorTop(false)
	self.nextButton:setAnchorBottom(true)
	self.nextButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.nextButton)
end

function Page4:updateWhenVisible()
	local item = self.parent.item
	local oldID = item:getID()
	item:setID(self.IDEntry:getText())
	local valid = item:getID() ~= nil
	self.nextButton:setEnable(valid)
	if not valid then
		item:setID(oldID)
	end
end

function Page4:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_WorkshopSubmit_Title4"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	if getDebug() then self:drawTextCentre("DEBUG: Page4", self.width / 2, self.height - 20, 0.5, 0.5, 0.5, 1, UIFont.Small) end
	self:updateWhenVisible()
end

function Page4:setFields()
	self.IDEntry:setText(self.parent.item:getID() or "")
	self.nextButton:setEnable(self.parent.item:getID() ~= nil)
end

function Page4:onButtonBack()
	self:setVisible(false)
	self.parent.page3:setVisible(true, self.joyfocus)
end

function Page4:onButtonNext()
	self.parent.item:setID(self.IDEntry:getText())
	self.parent.item:writeWorkshopTxt()
	
	self:setVisible(false)
	self.parent.item:setID(self.IDEntry:getText())
	self.parent.page5:setFields(self)
	self.parent.page5:setVisible(true, self.joyfocus)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function Page5:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.anchorBottom = true
	o.anchorRight = true
	return o
end

function Page5:create()
	local entryWid = 400
	local padX = (self.width - (entryWid)) / 2
	local labelHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local label1 = ISLabel:new(padX, 64, labelHgt, getText("UI_WorkshopSubmit_ItemTitle"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label1)

	local entryHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight() + 2 * 2
	self.titleEntry = ISTextEntryBox:new("", padX, label1:getBottom() + 2, entryWid, entryHgt)
	self.titleEntry.font = UIFont.Medium
	self.titleEntry:initialise()
	self.titleEntry:instantiate()
	self.titleEntry:setEditable(false)
	self:addChild(self.titleEntry)

	local label4 = ISLabel:new(padX, self.titleEntry:getBottom() + 12, labelHgt, getText("UI_WorkshopSubmit_ItemID"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(label4)
	
	self.IDEntry = ISTextEntryBox:new("", padX, label4:getBottom() + 2, entryWid, entryHgt)
	self.IDEntry.font = UIFont.Medium
	self.IDEntry:initialise()
	self.IDEntry:instantiate()
	self.IDEntry:setEditable(false)
	self:addChild(self.IDEntry)

	local buttonWid = 500
	local buttonHgt = 48
	local buttonGapY = 24
	local labelGapY = 12
	local allHgt = buttonHgt + buttonGapY + buttonHgt + labelGapY + labelHgt
	local buttonMinY = self.IDEntry:getBottom()
	local buttonY = buttonMinY + (self.height - 30 - buttonMinY - allHgt) / 3

	self.button1 = ISButton:new(self.width / 2 - buttonWid / 2, buttonY, buttonWid, buttonHgt, getText("UI_WorkshopSubmit_BtnChangelog"):gsub("\\n", "\n"), self, self.onButtonChangelog)
	self.button1.internal = "CHANGELOG"
	self.button1:initialise()
--	self.button1:setAnchorLeft(true)
--	self.button1:setAnchorTop(false)
--	self.button1:setAnchorBottom(true)
	self.button1.borderColor = {r=1, g=1, b=1, a=0.25}
	self.button1:setFont(UIFont.Medium)
	self:addChild(self.button1)

	self.button2 = ISButton:new(self.width / 2 - buttonWid / 2, self.button1:getBottom() + buttonGapY, buttonWid, buttonHgt, getText("UI_WorkshopSubmit_BtnPublish"):gsub("\\n", "\n"), self, self.onButtonPublish)
	self.button2.internal = "PUBLISH"
	self.button2:initialise()
--	self.button2:setAnchorLeft(true)
--	self.button2:setAnchorTop(false)
--	self.button2:setAnchorBottom(true)
	self.button2.borderColor = {r=1, g=1, b=1, a=0.25}
	self.button2:setFont(UIFont.Medium)
	self:addChild(self.button2)

	local label5 = ISLabel:new(0, self.button2:getBottom() + 12, labelHgt, getText("UI_WorkshopSubmit_Legal1"), 1, 1, 1, 1, UIFont.Small, true)
	local label6 = ISLabel:new(0, self.button2:getBottom() + 12, labelHgt, getText("UI_WorkshopSubmit_Legal2"), 0, 1, 1, 1, UIFont.Small, true)
	label6.onMouseDown = self.onMouseDownLegal
	local totalWidth = label5:getWidth() + label6:getWidth()
	label5:setX(self.width / 2 - totalWidth / 2)
	label6:setX(label5:getRight())
	self:addChild(label5)
	self:addChild(label6)

	local dy = (self.height - 30 - label6:getBottom()) / 3
	local uis = self:getChildren()
	for _,ui in pairs(uis) do
		ui:setY(ui:getY() + dy)
	end

	local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

	self.backButton = ISButton:new(16, self.height - 5 - buttonHgt, 100, buttonHgt, getText("UI_btn_back"), self, self.onButtonBack)
	self.backButton.internal = "BACK"
	self.backButton:initialise()
	self.backButton:setAnchorLeft(true)
	self.backButton:setAnchorTop(false)
	self.backButton:setAnchorBottom(true)
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.backButton)
end

function Page5:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_WorkshopSubmit_Title5"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	if getDebug() then self:drawTextCentre("DEBUG: Page5", self.width / 2, self.height - 20, 0.5, 0.5, 0.5, 1, UIFont.Small) end
end

function Page5:setFields(prevPage)
	self.prevPage = prevPage
	
	local item = self.parent.item
	self.titleEntry:setText(item:getTitle())

	self.IDEntry:setText(item:getID() or getText("UI_WorkshopSubmit_NewID"))
end

function Page5:onMouseDownLegal(x, y)
	if isSteamOverlayEnabled() then
		activateSteamOverlayToWebPage("http://steamcommunity.com/sharedfiles/workshoplegalagreement")
	else
		openUrl("http://steamcommunity.com/sharedfiles/workshoplegalagreement")
	end
end

function Page5:onButtonChangelog()
	self:setVisible(false)
	self.parent.page6:setFields(getText("UI_WorkshopSubmit_TitleEditChangeNotes"), self.parent.item:getChangeNote(), self)
	self.parent.page6:setVisible(true, self.joyfocus)
end

function Page5:onTextChanged(text)
	self.parent.item:setChangeNote(text)
end

function Page5:onButtonPublish()
	self:setVisible(false)
	local err = self.parent.item:validateContents()
	if err then
		self.parent.page8:setFields(err, self)
		self.parent.page8:setVisible(true, self.joyfocus)
	else
		self.parent.page7:setFields()
		self.parent.page7:setVisible(true, self.joyfocus)
	end
end

function Page5:onButtonBack()
	self:setVisible(false)
	self.prevPage:setVisible(true, self.joyfocus)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function Page6:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.anchorBottom = true
	o.anchorRight = true
	return o
end

function Page6:create()
	self.entry = ISTextEntryBox:new("", 64, 64, self.width - 64 * 2, self.height - 64 * 2)
	self.entry.font = UIFont.Medium
	self.entry:initialise()
	self.entry:instantiate()
	self.entry:setAnchorBottom(true)
	self.entry:setAnchorRight(true)
	self.entry:setMultipleLine(true)
	self.entry:setMaxLines(512)
--	self.entry:setMaxTextLength(8000)
	self.entry:addScrollBars()
	self:addChild(self.entry)

	local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

	self.backButton = ISButton:new(self.width / 2 - 15 - 100, self.height-64 + 8, 100, buttonHgt, getText("UI_btn_cancel"), self, self.onButtonCancel)
	self.backButton.internal = "CANCEL"
	self.backButton:initialise()
	self.backButton:setAnchorLeft(true)
	self.backButton:setAnchorTop(false)
	self.backButton:setAnchorBottom(true)
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.backButton)

	self.nextButton = ISButton:new(self.width / 2 + 15, self.height-64 + 8, 100, buttonHgt, getText("UI_btn_accept"), self, self.onButtonAccept)
	self.nextButton.internal = "ACCEPT"
	self.nextButton:initialise()
	self.nextButton:setAnchorLeft(true)
	self.nextButton:setAnchorTop(false)
	self.nextButton:setAnchorBottom(true)
	self.nextButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.nextButton)
end

function Page6:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(self.title, self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	if getDebug() then self:drawTextCentre("DEBUG: Page6", self.width / 2, self.height - 20, 0.5, 0.5, 0.5, 1, UIFont.Small) end
end

function Page6:setFields(title, text, prevPage)
	self.title = title
	self.entry:setText(text)
	self.prevPage = prevPage
end

function Page6:onButtonAccept()
	self:setVisible(false)
	self.prevPage:onTextChanged(self.entry:getText())
	self.prevPage:setVisible(true, self.joyfocus)
end

function Page6:onButtonCancel()
	self:setVisible(false)
	self.prevPage:setVisible(true, self.joyfocus)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function Page7:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.anchorBottom = true
	o.anchorRight = true
	return o
end

function Page7:create()
	local buttonWid = 400
	local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

	self.overlayButton = ISButton:new(self.width / 2 - buttonWid / 2, 64 + (32 - 25) / 2, buttonWid, buttonHgt, getText("UI_WorkshopSubmit_OverlayButton"), self, self.onButtonOverlay)
	self.overlayButton.internal = "OVERLAY"
	self.overlayButton:initialise()
	self.overlayButton.borderColor = {r=1, g=1, b=1, a=0.25}
	self:addChild(self.overlayButton)

	local y = self.overlayButton:getBottom() + 6
	self.log = ISTextEntryBox:new("", 64, y, self.width - 64 * 2, self.height - 64 - y)
	self.log.font = UIFont.Medium
	self.log:initialise()
	self.log:instantiate()
	self.log:setAnchorBottom(true)
	self.log:setAnchorRight(true)
	self.log:setMultipleLine(true)
	self.log:setEditable(false)
	self.log:addScrollBars()
	self:addChild(self.log)


	self.closeButton = ISButton:new(self.width / 2 - 100 / 2, self.height-64 + 8, 100, buttonHgt, getText("UI_btn_close"), self, self.onButtonClose)
	self.closeButton.internal = "CLOSE"
	self.closeButton:initialise()
	self.closeButton:setAnchorLeft(true)
	self.closeButton:setAnchorTop(false)
	self.closeButton:setAnchorBottom(true)
	self.closeButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.closeButton)
end

function Page7:updateWhenVisible()
	if self.state == "create" then
		-- Ask Steam to create a new item.
		if TEST or self.item:create() then
			self:LOG("success requesting Steam Workshop to create a new item")
			self.state = "createWait"
		else
			self:LOG("error requesting Steam Workshop to create a new item")
			self.state = "createFail"
		end
	elseif self.state == "createWait" then
		-- Waiting for new workshop item to be created.
		-- We'll get either OnSteamWorkshopItemCreated or OnSteamWorkshopItemNotCreated.
		if TEST then
			triggerEvent("OnSteamWorkshopItemCreated", "1234", true)
--			triggerEvent("OnSteamWorkshopItemNotCreated", 666)
		end
	elseif self.state == "createFail" then
		-- Failed, do nothing further.
		self.closeButton:setEnable(true)
		self.state = nil
		self:LOG("finished")
	elseif self.state == "update" then
		-- Submit an update for the item.
		if TEST or self.item:submitUpdate() then
			self:LOG("success requesting Steam to update the item")
			self:LOG("there is no way to stop the update once it has started")
			self.state = "updateWait"
			if TEST then
				self.updateTimeMax = 5 * getPerformance():getFramerate()
				self.updateTime = 0
			end
		else
			self:LOG("error requesting Steam to update the item")
			self.state = "updateFail"
		end
	elseif self.state == "updateWait" then
		-- Waiting for update to Steam to complete.
		if TEST then
			self.updateTime = self.updateTime + 1
			if self.updateTime >= self.updateTimeMax then
				triggerEvent("OnSteamWorkshopItemUpdated", true)
--				triggerEvent("OnSteamWorkshopItemNotUpdated", 666)
			end
		end
	elseif self.state == "updateFail" then
		-- Failed, do nothing further.
		self.closeButton:setEnable(true)
		self.state = nil
		self:LOG("finished")
	elseif self.state == "done" then
		-- Mission accomplished.
		self.overlayButton:setVisible(isSteamOverlayEnabled())
		self.closeButton:setEnable(true)
		self.state = nil
		self:LOG("finished")
	end
end

function Page7:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_WorkshopSubmit_Title7"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	if getDebug() then self:drawTextCentre("DEBUG: Page7", self.width / 2, self.height - 20, 0.5, 0.5, 0.5, 1, UIFont.Small) end
	self:updateWhenVisible()

	-- Display progress while updating.
	if self.state == "updateWait" then
		local progressHgt = 24
		local progress = { processed = 0, total = 1 }
		if TEST then
			progress.processed = self.updateTime
			progress.total = self.updateTimeMax
		else
			self.item:getUpdateProgress(progress)
		end
		local fraction = progress.processed / progress.total
		local fg = { r = 1, g = 1, b = 1, a = 0.5 }
		self:drawProgressBar(64, 64 + (32 - progressHgt) / 2, self.log.width, progressHgt, fraction, fg)
	end
end

function Page7:setFields()
	self.item = self.parent.item
	if self.item:getID() then
		self.log:setText("start update of existing item ID=" .. self.item:getID())
		self.state = "update"
	else
		self.log:setText("start creation of new item")
		self.state = "create"
	end
	self.overlayButton:setVisible(false)
	self.closeButton:setEnable(false)
end

function Page7:LOG(msg)
	-- TODO: write to WorkshopLog.txt as well
	self.log:setText(self.log:getText() .. "\n" .. msg)
end

function Page7:OnSteamWorkshopItemCreated(itemID, bUserNeedsToAcceptWorkshopLegalAgreement)
	self:LOG("new workshop item successfully created with ID=" .. itemID)
	self.item:setID(itemID)
	self.item:writeWorkshopTxt()
	if bUserNeedsToAcceptWorkshopLegalAgreement then
		self:LOG("user must agree to the Workshop legal agreement")
	else
		self:LOG("user has already agreed to the Workshop legal agreement")
	end
	self.state = "update"
end

function Page7:OnSteamWorkshopItemNotCreated(result)
	self:LOG("failed to create new workshop item, result=" .. tostring(result))
	self.state = "createFail"
end

function Page7:OnSteamWorkshopItemUpdated(bUserNeedsToAcceptWorkshopLegalAgreement)
	self:LOG("success updating item")
	if bUserNeedsToAcceptWorkshopLegalAgreement then
		-- This can happen if the user updates an item they did not create.
		self:LOG("user must agree to the Workshop legal agreement")
	else
		self:LOG("user has already agreed to the Workshop legal agreement")
	end
	self.state = "done"
end

function Page7:OnSteamWorkshopItemNotUpdated(result)
	self.state = "updateFail"
	self:LOG("failed to update workshop item, result=" .. tostring(result))
end

function Page7:onButtonOverlay()
	activateSteamOverlayToWorkshopItem(self.item:getID())
end

function Page7:onButtonClose()
	self.parent:onButtonBack()
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function Page8:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.anchorBottom = true
	o.anchorRight = true
	return o
end

function Page8:create()
	self.backButton = ISButton:new(16, self.height-30, 100, 25, getText("UI_btn_back"), self, self.onButtonBack)
	self.backButton.internal = "BACK"
	self.backButton:initialise()
	self.backButton:setAnchorLeft(true)
	self.backButton:setAnchorTop(false)
	self.backButton:setAnchorBottom(true)
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.backButton)
end

function Page8:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_WorkshopSubmit_Title8"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	if getDebug() then self:drawTextCentre("DEBUG: Page8", self.width / 2, self.height - 20, 0.5, 0.5, 0.5, 1, UIFont.Small) end

	self:drawTextCentre(self.error, self.width / 2, self.height / 3, 1, 1, 1, 1, UIFont.Medium)
end

function Page8:setFields(err, prevPage)
	self.error = getText("UI_WorkshopError_" .. err, self.parent.item:getExtendedErrorInfo(err)):gsub("\\n", "\n")
	self.prevPage = prevPage
end

function Page8:onButtonBack()
	self:setVisible(false)
	self.prevPage:setVisible(true, self.joyfocus)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function Page9:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.anchorBottom = true
	o.anchorRight = true
	return o
end

function Page9:create()
	local buttonWid = 500
	local buttonHgt = 48
	local buttonGapY = 24
	
	self.button1 = ISButton:new(self.width / 2 - buttonWid / 2, 64, buttonWid, buttonHgt, getText("UI_WorkshopSubmit_BtnOpenWorkshopOverlay"):gsub("\\n", "\n"), self, self.onButtonOpenWorkshopOverlay)
	self.button1:initialise()
	self.button1.borderColor = {r=1, g=1, b=1, a=0.25}
	self.button1:setFont(UIFont.Medium)
	self:addChild(self.button1)
	
	self.button2 = ISButton:new(self.width / 2 - buttonWid / 2, self.button1:getBottom() + buttonGapY, buttonWid, buttonHgt, getText("UI_WorkshopSubmit_BtnOpenWorkshopUserOverlay"):gsub("\\n", "\n"), self, self.onButtonOpenWorkshopUserOverlay)
	self.button2:initialise()
	self.button2.borderColor = {r=1, g=1, b=1, a=0.25}
	self.button2:setFont(UIFont.Medium)
	self:addChild(self.button2)
--[[
	self.button3 = ISButton:new(self.width / 2 - buttonWid / 2, self.button2:getBottom() + buttonGapY, buttonWid, buttonHgt, getText("UI_WorkshopSubmit_BtnViewSubscriptions"):gsub("\\n", "\n"), self, self.onButtonViewSubscriptions)
	self.button3:initialise()
	self.button3.borderColor = {r=1, g=1, b=1, a=0.25}
	self.button3:setFont(UIFont.Medium)
	self:addChild(self.button3)
--]]
	self.button4 = ISButton:new(self.width / 2 - buttonWid / 2, self.button2:getBottom() + buttonGapY, buttonWid, buttonHgt, getText("UI_WorkshopSubmit_BtnCreateAndUpdate"):gsub("\\n", "\n"), self, self.onButtonCreateAndUpdate)
	self.button4:initialise()
	self.button4.borderColor = {r=1, g=1, b=1, a=0.25}
	self.button4:setFont(UIFont.Medium)
	self:addChild(self.button4)

	local dy = (self.height - 30 - self.button4:getBottom()) / 3
	local uis = self:getChildren()
	for _,ui in pairs(uis) do
		ui:setY(ui:getY() + dy)
	end

	local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

	self.backButton = ISButton:new(16, self.height - 5 - buttonHgt, 100, buttonHgt, getText("UI_btn_back"), self, self.onButtonBack)
	self.backButton.internal = "BACK"
	self.backButton:initialise()
	self.backButton:setAnchorLeft(true)
	self.backButton:setAnchorTop(false)
	self.backButton:setAnchorBottom(true)
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.backButton)
end

function Page9:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_WorkshopSubmit_Title9"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	if getDebug() then self:drawTextCentre("DEBUG: Page9", self.width / 2, self.height - 20, 0.5, 0.5, 0.5, 1, UIFont.Small) end
end

function Page9:aboutToShow()
	self.button1:setEnable(isSteamOverlayEnabled())
	self.button2:setEnable(isSteamOverlayEnabled())
end

function Page9:onButtonOpenWorkshopOverlay()
	activateSteamOverlayToWorkshop()
end

function Page9:onButtonOpenWorkshopUserOverlay()
	activateSteamOverlayToWorkshopUser()
end

function Page9:onButtonViewSubscriptions()
	self:setVisible(false)
	self.parent.page10:aboutToShow()
	self.parent.page10:setVisible(true, self.joyfocus)
end

function Page9:onButtonCreateAndUpdate()
	self:setVisible(false)
	self.parent.page1:aboutToShow()
	self.parent.page1:setVisible(true, self.joyfocus)
end

function Page9:onButtonBack()
	self.parent:onButtonBack()
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function Page10:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.anchorBottom = true
	o.anchorRight = true
	return o
end

function Page10:create()

	self.backButton = ISButton:new(16, self.height-30, 100, 25, getText("UI_btn_back"), self, self.onButtonBack)
	self.backButton.internal = "BACK"
	self.backButton:initialise()
	self.backButton:setAnchorLeft(true)
	self.backButton:setAnchorTop(false)
	self.backButton:setAnchorBottom(true)
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.backButton)
end

function Page10:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_WorkshopSubmit_Title10"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	if getDebug() then self:drawTextCentre("DEBUG: Page10", self.width / 2, self.height - 20, 0.5, 0.5, 0.5, 1, UIFont.Small) end
end

function Page10:aboutToShow()
end

function Page10:onButtonBack()
	self:setVisible(false)
	self.parent.page9:setVisible(true, self.joyfocus)
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function TagsList:onMouseDown(x, y)
	if not self.enabled then return end
	if #self.items == 0 then return end
	local row = self:rowAt(x, y)
	if row > #self.items then
		row = #self.items
	end
	if row < 1 then
		row = 1
	end
	self.selected = row

	if x < self.leftMargin + self.boxSize then
		local tag = self.items[self.selected].text
		self:setChecked(tag, not self:isChecked(tag))
	end
end

function TagsList:onMouseDoubleClick(x, y)
	return self:onMouseDown(x, y)
end

function TagsList:doDrawItem(y, item, alt)
	local boxDY = (item.height - self.boxSize) / 2
	local textDY = (item.height - self.fontHgt) / 2
	if self.enabled and item.index == self.mouseoverselected then
		self:drawRect(0, y, self.width, item.height, 0.1, 1, 1, 1, 1)
	end
	self:drawRectBorder(self.leftMargin, y + boxDY, self.boxSize, self.boxSize, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b)
	if self.checked[item.text] then
		self:drawTexture(self.tickTexture, self.leftMargin + 3, y + boxDY + 2, 1, 1, 1, 1)
	end
	self:drawText(item.text, self.leftMargin + self.boxSize + self.textGap, y, 1, 1, 1, 1, self.font)
	return y + item.height
end

function TagsList:setTags(tags)
	self:clear()
	for i=1,tags:size() do
		self:addItem(tags:get(i-1), nil)
	end
end

function TagsList:setChecked(tag, checked)
	self.checked[tag] = checked
end

function TagsList:isChecked(tag)
	return self.checked[tag] == true
end

function TagsList:setCheckedTags(tags)
	for i=1,#self.items do
		local tag = self.items[i].text
		self:setChecked(tag, tags:contains(tag))
	end
end

function TagsList:getCheckedTags()
	local ret = ArrayList.new()
	for i=1,#self.items do
		local tag = self.items[i].text
		if self:isChecked(tag) then
			ret:add(tag)
		end
	end
	return ret
end

function TagsList:setEnabled(enabled)
	self.enabled = enabled
end

function TagsList:new(x, y, width, height)
	local o = ISScrollingListBox:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o:setFont("Medium", 2)
	o.drawBorder = true
	o.checked = {}
	o.leftMargin = 5
	o.boxSize = 16
	o.textGap = 4
	o.tickTexture = getTexture("Quest_Succeed")
	o.enabled = true
	return o
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function WorkshopSubmitScreen:create()
	self.page1 = Page1:new(0, 0, self.width, self.height)
	self.page1:create()
	self:addChild(self.page1)

	self.page2 = Page2:new(0, 0, self.width, self.height)
	self.page2:create()
	self:addChild(self.page2)

	self.page3 = Page3:new(0, 0, self.width, self.height)
	self.page3:create()
	self:addChild(self.page3)

	self.page4 = Page4:new(0, 0, self.width, self.height)
	self.page4:create()
	self:addChild(self.page4)

	self.page5 = Page5:new(0, 0, self.width, self.height)
	self.page5:create()
	self:addChild(self.page5)

	self.page6 = Page6:new(0, 0, self.width, self.height)
	self.page6:create()
	self:addChild(self.page6)

	self.page7 = Page7:new(0, 0, self.width, self.height)
	self.page7:create()
	self:addChild(self.page7)

	self.page8 = Page8:new(0, 0, self.width, self.height)
	self.page8:create()
	self:addChild(self.page8)

	self.page9 = Page9:new(0, 0, self.width, self.height)
	self.page9:create()
	self:addChild(self.page9)

	self.page10 = Page10:new(0, 0, self.width, self.height)
	self.page10:create()
	self:addChild(self.page10)
end

function WorkshopSubmitScreen:render()
	ISPanelJoypad.render(self)
end

function WorkshopSubmitScreen:fillList()
	self.page1:setVisible(false)
	self.page2:setVisible(false)
	self.page3:setVisible(false)
	self.page4:setVisible(false)
	self.page5:setVisible(false)
	self.page6:setVisible(false)
	self.page7:setVisible(false)
	self.page8:setVisible(false)
	self.page9:setVisible(true)
	self.page10:setVisible(false)
	self.page9:aboutToShow()
end

function WorkshopSubmitScreen:onButtonBack()
	self:setVisible(false)
	MainScreen.instance.bottomPanel:setVisible(true)
	if self.joyfocus then
		self.joyfocus.focus = MainScreen.instance
		updateJoypadFocus(self.joyfocus)
	end
end

function WorkshopSubmitScreen.OnSteamWorkshopItemCreated(itemID, bUserNeedsToAcceptWorkshopLegalAgreement)
	WorkshopSubmitScreen.instance.page7:OnSteamWorkshopItemCreated(itemID, bUserNeedsToAcceptWorkshopLegalAgreement)
end

function WorkshopSubmitScreen.OnSteamWorkshopItemNotCreated(result)
	WorkshopSubmitScreen.instance.page7:OnSteamWorkshopItemNotCreated(result)
end

function WorkshopSubmitScreen.OnSteamWorkshopItemUpdated(bUserNeedsToAcceptWorkshopLegalAgreement)
	WorkshopSubmitScreen.instance.page7:OnSteamWorkshopItemUpdated(bUserNeedsToAcceptWorkshopLegalAgreement)
end

function WorkshopSubmitScreen.OnSteamWorkshopItemNotUpdated(result)
	WorkshopSubmitScreen.instance.page7:OnSteamWorkshopItemNotUpdated(result)
end

function WorkshopSubmitScreen:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	WorkshopSubmitScreen.instance = o
	return o
end

if TEST or getSteamModeActive() then
	LuaEventManager.AddEvent("OnSteamWorkshopItemCreated")
	LuaEventManager.AddEvent("OnSteamWorkshopItemNotCreated")
	LuaEventManager.AddEvent("OnSteamWorkshopItemUpdated")
	LuaEventManager.AddEvent("OnSteamWorkshopItemNotUpdated")
	Events.OnSteamWorkshopItemCreated.Add(WorkshopSubmitScreen.OnSteamWorkshopItemCreated)
	Events.OnSteamWorkshopItemNotCreated.Add(WorkshopSubmitScreen.OnSteamWorkshopItemNotCreated)
	Events.OnSteamWorkshopItemUpdated.Add(WorkshopSubmitScreen.OnSteamWorkshopItemUpdated)
	Events.OnSteamWorkshopItemNotUpdated.Add(WorkshopSubmitScreen.OnSteamWorkshopItemNotUpdated)
end
