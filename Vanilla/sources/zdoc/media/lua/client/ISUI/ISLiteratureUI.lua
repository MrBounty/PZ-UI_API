--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISCollapsableWindowJoypad"
require "ISUI/ISScrollingListBox"
require "ISUI/ISTabPanel"

---@class ISLiteratureUI : ISCollapsableWindowJoypad
ISLiteratureUI = ISCollapsableWindowJoypad:derive("ISLiteratureUI")
ISLiteratureList = ISScrollingListBox:derive("ISListeratureList")
ISLiteratureMediaList = ISScrollingListBox:derive("ISListeratureMediaList")

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

local LITERATURE_HIDDEN = {}

function ISLiteratureUI.SetItemHidden(fullType, hidden)
	if type(fullType) ~= 'string' or not string.contains(fullType, '.') then return end
	LITERATURE_HIDDEN[fullType] = hidden and true or nil
end

ISLiteratureUI.SetItemHidden('Base.BookBlacksmith1', true)
ISLiteratureUI.SetItemHidden('Base.BookBlacksmith2', true)
ISLiteratureUI.SetItemHidden('Base.BookBlacksmith3', true)
ISLiteratureUI.SetItemHidden('Base.BookBlacksmith4', true)
ISLiteratureUI.SetItemHidden('Base.BookBlacksmith5', true)
ISLiteratureUI.SetItemHidden('Base.SmithingMag1', true)
ISLiteratureUI.SetItemHidden('Base.SmithingMag2', true)
ISLiteratureUI.SetItemHidden('Base.SmithingMag3', true)
ISLiteratureUI.SetItemHidden('Base.SmithingMag4', true)

-----

function ISLiteratureList:doDrawItem(y, item, alt)
	if y + self:getYScroll() >= self.height then return y + item.height end
	if y + item.height + self:getYScroll() <= 0 then return y + item.height end
--[[
	if self.selected == item.index then
		self:drawRect(0, y, self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
	end
--]]
	self:drawRectBorder(0, y, self:getWidth(), item.height, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b)

	local texture = item.item:getNormalTexture()
	if texture then
		local texWidth = texture:getWidthOrig()
		local texHeight = texture:getHeightOrig()
		local a = 1
		if texWidth <= 32 and texHeight <= 32 then
			self:drawTexture(texture,6+(32-texWidth)/2,y+(item.height-texHeight)/2,a,1,1,1)
		else
			self:drawTextureScaledAspect(texture,6,y+(item.height-texHeight)/2,32,32,a,1,1,1)
		end
	end

	local itemPadY = (item.height - self.fontHgt) / 2
	local r,g,b,a = 0.5,0.5,0.5,1.0
	local skillBook = SkillBook[item.item:getSkillTrained()]
	if skillBook then
		if (item.item:getNumberOfPages() > 0) and (self.character:getAlreadyReadPages(item.item:getFullName()) == item.item:getNumberOfPages()) then
			r,g,b = 1.0,1.0,1.0
		elseif item.item:getMaxLevelTrained() <= self.character:getPerkLevel(skillBook.perk) + 1 then
			-- The book hasn't been read, but the character has the skill levels.
			r,g,b = 1.0,1.0,1.0
		end
	else
		if self.character:getAlreadyReadBook():contains(item.item:getFullName()) then
			r,g,b = 1.0,1.0,1.0
		elseif (item.item:getTeachedRecipes() ~= nil) and self.character:getKnownRecipes():containsAll(item.item:getTeachedRecipes()) then
			r,g,b = 1.0,1.0,1.0
		end
	end
	self:drawText(item.text, 6 + 32 + 6, y+itemPadY, r, g, b, a, self.font)

	y = y + item.height
	return y;
end

function ISLiteratureList:new(x, y, width, height, character)
	local o = ISScrollingListBox.new(self, x, y, width, height)
	o.character = character
	return o
end

-----

function ISLiteratureMediaList:doDrawItem(y, item, alt)
	if y + self:getYScroll() >= self.height then return y + item.height end
	if y + item.height + self:getYScroll() <= 0 then return y + item.height end

	self:drawRectBorder(0, y, self:getWidth(), item.height, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b)

	local texture = self.scriptItem and self.scriptItem:getNormalTexture() or nil
	if texture then
		local texWidth = texture:getWidthOrig()
		local texHeight = texture:getHeightOrig()
		local a = 1
		if texWidth <= 32 and texHeight <= 32 then
			self:drawTexture(texture,6+(32-texWidth)/2,y+(item.height-texHeight)/2,a,1,1,1)
		else
			self:drawTextureScaledAspect(texture,6,y+(item.height-texHeight)/2,32,32,a,1,1,1)
		end
	end

	local itemPadY = (item.height - self.fontHgt) / 2
	local r,g,b,a = 0.5,0.5,0.5,1.0
	if getZomboidRadio():getRecordedMedia():hasListenedToAll(self.character, item.item) then
		r,g,b = 1.0,1.0,1.0
	end
	self:drawText(item.text, 6 + 32 + 6, y+itemPadY, r, g, b, a, self.font)

	y = y + item.height
	return y;
end

function ISLiteratureMediaList:new(x, y, width, height, character)
	local o = ISScrollingListBox.new(self, x, y, width, height)
	o.character = character
	o.scriptItem = nil
	return o
end

-----

function ISLiteratureUI:createChildren()
	ISCollapsableWindowJoypad.createChildren(self)

	local th = self:titleBarHeight()
	local rh = self:resizeWidgetHeight()

	self.tabs = ISTabPanel:new(0, th, self.width, self.height-th-rh)
	self.tabs:setAnchorRight(true)
	self.tabs:setAnchorBottom(true)
	self.tabs:setEqualTabWidth(false)
	self:addChild(self.tabs)

	-- BOOKS

	local listbox1 = ISLiteratureList:new(0, 0, self.tabs.width, self.tabs.height - self.tabs.tabHeight, self.character)
	listbox1:setAnchorRight(true)
	listbox1:setAnchorBottom(true)
	listbox1:setFont(UIFont.Small, 2)
	listbox1.itemheight = math.max(32, FONT_HGT_SMALL) + 2 * 2
	self.tabs:addView(getText("IGUI_LiteratureUI_Skills"), listbox1)
	self.listbox1 = listbox1

	-- RECIPES

	local listbox2 = ISLiteratureList:new(0, 0, self.width, self.tabs.height - self.tabs.tabHeight, self.character)
	listbox2:setAnchorRight(true)
	listbox2:setAnchorBottom(true)
	listbox2:setFont(UIFont.Small, 2)
	listbox2.itemheight = math.max(32, FONT_HGT_SMALL) + 2 * 2
	self.tabs:addView(getText("IGUI_LiteratureUI_Recipes"), listbox2)
	self.listbox2 = listbox2

	-- RECORDED MEDIA

	local categories = getZomboidRadio():getRecordedMedia():getCategories()
	self.listboxMedia = {}
	for i=1,categories:size() do
		local category = categories:get(i-1)
		local listbox3 = ISLiteratureMediaList:new(0, 0, self.width, self.tabs.height - self.tabs.tabHeight, self.character)
		listbox3:setAnchorRight(true)
		listbox3:setAnchorBottom(true)
		listbox3:setFont(UIFont.Small, 2)
		listbox3.itemheight = math.max(32, FONT_HGT_SMALL) + 2 * 2
		self.tabs:addView(getText("IGUI_LiteratureUI_RecordedMedia_"..category), listbox3)
		self.listboxMedia[i] = listbox3
	end

	self.resizeWidget2:bringToTop()
	self.resizeWidget:bringToTop()

	self:setLists()
end

function ISLiteratureUI:close()
	self:removeFromUIManager()
end

function ISLiteratureUI:setLists()
	local skillBooks = {}
	local other = {}
	local media = {}
	local allItems = getScriptManager():getAllItems()
	for i=1,allItems:size() do
		local item = allItems:get(i-1)
		if item:getType() == Type.Literature then
			if SkillBook[item:getSkillTrained()] then
				table.insert(skillBooks, item)
			elseif item:getTeachedRecipes() ~= nil then
				table.insert(other, item)
			end
		end
		local mediaCategory = item:getRecordedMediaCat()
		if mediaCategory then
			media[mediaCategory] = media[mediaCategory] or {}
			table.insert(media[mediaCategory], item)
		end
	end

	local sortFunc = function(a,b)
		return not string.sort(a:getDisplayName(), b:getDisplayName())
	end

	table.sort(skillBooks, sortFunc)
	self.listbox1:clear()
	for _,item in ipairs(skillBooks) do
		if not LITERATURE_HIDDEN[item:getFullName()] then
			self.listbox1:addItem(item:getDisplayName(), item)
		end
	end

	table.sort(other, sortFunc)
	self.listbox2:clear()
	for _,item in ipairs(other) do
		if not LITERATURE_HIDDEN[item:getFullName()] then
			self.listbox2:addItem(item:getDisplayName(), item)
		end
	end

	self:setMediaLists(media)
end

function ISLiteratureUI:setMediaLists(scriptItems)
	local categories = getZomboidRadio():getRecordedMedia():getCategories()
	for i=1,categories:size() do
		local category = categories:get(i-1)
		self.listboxMedia[i].scriptItem = scriptItems[category] and scriptItems[category][1] or nil
		local mediaType = RecordedMedia.getMediaTypeForCategory(category)
		local list = getZomboidRadio():getRecordedMedia():getAllMediaForType(mediaType)
		for j=1,list:size() do
			local mediaData = list:get(j-1)
			if mediaData:getCategory() == category then
				local title = nil
				if mediaData:hasTitle() then
					title = mediaData:getTranslatedTitle()
					if mediaData:hasSubTitle() and (mediaData:getSubtitleEN() ~= "Home VHS") then
						title = title .. ' ' .. mediaData:getTranslatedSubTitle()
					end
				elseif mediaData:hasSubTitle() then
					title = mediaData:getTranslatedSubTitle()
				else
					title = mediaData:getTranslatedItemDisplayName()
				end
				self.listboxMedia[i]:addItem(title, mediaData)
			end
		end
		self.listboxMedia[i]:sort()
	end
end

function ISLiteratureUI:prerender()
	ISCollapsableWindowJoypad.prerender(self)
	
	local infoPanel = getPlayerInfoPanel(self.playerNum)
	if not infoPanel or (self.owner ~= infoPanel.charScreen) then
		-- Player UI was destroyed
		self:removeFromUIManager()
	end
end

function ISLiteratureUI:onGainJoypadFocus(joypadData)
	ISCollapsableWindowJoypad.onGainJoypadFocus(self, joypadData)
	self.drawJoypadFocus = true
end

function ISLiteratureUI:onLoseJoypadFocus(joypadData)
	ISCollapsableWindowJoypad.onLoseJoypadFocus(self, joypadData)
	self.drawJoypadFocus = false
end

function ISLiteratureUI:onJoypadDown(button)
	if button == Joypad.BButton then
		self:close()
		setJoypadFocus(self.playerNum, self.owner)
	end
	if button == Joypad.LBumper or button == Joypad.RBumper then
		if #self.tabs.viewList < 2 then return end
		local viewIndex = self.tabs:getActiveViewIndex()
		if button == Joypad.LBumper then
			if viewIndex == 1 then
				viewIndex = #self.tabs.viewList
			else
				viewIndex = viewIndex - 1
			end
		end
		if button == Joypad.RBumper then
			if viewIndex == #self.tabs.viewList then
				viewIndex = 1
			else
				viewIndex = viewIndex + 1
			end
		end
		self.tabs:activateView(self.tabs.viewList[viewIndex].name)
--		setJoypadFocus(self.playerNum, self.tabs:getActiveView())
	end
end

function ISLiteratureUI:onJoypadDirUp(button)
	local listbox = self.tabs:getActiveView()
	local row = listbox:rowAt(5, 5 - listbox:getYScroll())
	row = row - math.floor((listbox.height / 2) / listbox.itemheight)
	row = math.max(row, 1)
	listbox:ensureVisible(row)
end

function ISLiteratureUI:onJoypadDirDown(button)
	local listbox = self.tabs:getActiveView()
	local row = listbox:rowAt(5, listbox.height - 5 - listbox:getYScroll())
	row = row + math.floor((listbox.height / 2) / listbox.itemheight)
	row = math.min(row, listbox:size())
	listbox:ensureVisible(row)
end

function ISLiteratureUI:new(x, y, width, height, character, owner)
	local o = ISCollapsableWindowJoypad.new(self, x, y, width, height)
	o:setTitle(getText("IGUI_LiteratureUI_Title"))
	o.character = character
	o.playerNum = character:getPlayerNum()
	o.owner = owner
	return o
end	
