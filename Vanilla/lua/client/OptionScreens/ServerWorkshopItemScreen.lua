--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanelJoypad"
require "ISUI/ISScrollingListBox"

ServerWorkshopItemScreen = ISPanelJoypad:derive("ServerWorkshopItemScreen")
local WorkshopItemList = ISScrollingListBox:derive("WorkshopItemList")

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

function WorkshopItemList:doDrawItem(y, item, alt)
    local isMouseOver = self.mouseoverselected == item.index and not self:isMouseOverScrollBar()
	if self.selected == item.index then
		self:drawRect(0, (y), self:getWidth(), item.height-1, 0.3, 0.7, 0.35, 0.15)
    elseif isMouseOver then
        self:drawRect(1, y + 1, self:getWidth() - 2, item.height - 2, 0.95, 0.05, 0.05, 0.05);
	end
	self:drawRectBorder(0, (y), self:getWidth(), item.height, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b)
	local textX = 16
	local textPadY = 4
	local textY = y + textPadY
	local textHgt = getTextManager():getFontFromEnum(UIFont.Large):getLineHeight()
	local scrollBarWid = (self:getScrollHeight() > self.height) and self.vscroll:getWidth() or 0
	if item.item.details then
		local textWid = getTextManager():MeasureStringX(UIFont.Large, item.text)
		item.text = item.item.details:getTitle()
		self:drawText(item.text, textX, textY, 1, 1, 1, 1, UIFont.Large)

		local size = (item.item.details:getFileSize() / 1024) / 1024
		size = round(size, 3)
		local sizeTxt = getText("UI_WorkshopServerItemState_FileSize", size)
		local sizeTxtWid = getTextManager():MeasureStringX(UIFont.Medium, sizeTxt)
		self:drawText(sizeTxt, textX, textY + textHgt, 0.8, 0.8, 0.8, 1, UIFont.Medium)

		local state = item.item.details:getState()
		local stateTxt = getText("UI_WorkshopServerItemState_" .. state)
		local stateTextWid = getTextManager():MeasureStringX(UIFont.Medium, stateTxt)
		local stateTxtHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
		local statePadX = 4
		local statePadY = 2
		local stateBoxWid = statePadX * 2 + stateTextWid
		local stateBoxHgt = statePadY * 2 + stateTxtHgt
		local stateBoxX = self.width - 8 - scrollBarWid - stateBoxWid
		local stateBoxY = y + (item.height - stateBoxHgt) / 2
		local r,g,b = 0,0,0.6
		if state == "Installed" then
			g = 0.6
			b = 0
		end
		self:drawRect(stateBoxX, stateBoxY, stateBoxWid, stateBoxHgt, 1, r, g, b)
		self:drawText(stateTxt, stateBoxX + statePadX, stateBoxY + statePadY, 1, 1, 1, 1, UIFont.Medium)

		if state == "Downloading" and item.item.downloaded and item.item.total then
		print('lua Downloading ID=' .. item.item.id)
			local progressHgt = math.min(item.height - 8, 24)
			local fraction = item.item.downloaded / item.item.total
			local fg = { r = 1, g = 1, b = 1, a = 0.5 }
			local barX = textX + textWid + 32
			if barX < textX + sizeTxtWid + 32 then
				barX = textX + sizeTxtWid + 32
			end
			self:drawProgressBar(barX, y + (item.height - progressHgt) / 2, stateBoxX - 32 - barX, progressHgt, fraction, fg)
		end

		item.height = textPadY + textHgt + stateTxtHgt + textPadY
	else
		self:drawText(item.text, textX, y + (item.height - textHgt) / 2, 1, 1, 1, 1, UIFont.Large)
	end
	return y + item.height
end

function WorkshopItemList:new(x, y, width, height)
	local o = ISScrollingListBox:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.drawBorder = true
	o:setFont("Medium", 4)
	return o
end

-----
-----
-----

function ServerWorkshopItemScreen:create()
	local textWid1 = getTextManager():MeasureStringX(UIFont.Small, getText("UI_btn_install"))
	local textWid2 = getTextManager():MeasureStringX(UIFont.Small, getText("UI_btn_disconnect"))
	local buttonWid = math.max(100, math.max(textWid1, textWid2) + 8 * 2)
	local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
	local buttonGapY = 20

	local labelHgt = FONT_HGT_MEDIUM
	self.label = ISLabel:new(64, 64, labelHgt, getText("UI_ServerWorkshopItemScreen_Prompt"), 1, 1, 1, 1, UIFont.Medium, true)
	self:addChild(self.label)

	local itemHgt = labelHgt + 4 * 2
	local listY = self.label:getBottom() + 8
	self.listbox = WorkshopItemList:new(64, listY, self.width - 64 * 2, self.height - buttonGapY * 2 - buttonHgt - listY)
	self.listbox:initialise()
	self.listbox:setAnchorLeft(true)
	self.listbox:setAnchorRight(true)
	self.listbox:setAnchorTop(true)
	self.listbox:setAnchorBottom(true)
	self:addChild(self.listbox)

	local buttonY = self.listbox:getBottom() + buttonGapY
	self.installButton = ISButton:new(self.width / 2 - 15 - buttonWid, buttonY, buttonWid, buttonHgt, getText("UI_btn_install"), self, self.onButtonInstall)
	self.installButton:initialise()
	self.installButton:setAnchorLeft(true)
	self.installButton:setAnchorTop(false)
	self.installButton:setAnchorBottom(true)
	self.installButton.borderColor = {r=1, g=1, b=1, a=0.25}
	self:addChild(self.installButton)

	self.disconnectButton = ISButton:new(self.width / 2 + 15, buttonY, buttonWid, buttonHgt, getText("UI_btn_disconnect"), self, self.onButtonDisconnect)
	self.disconnectButton:initialise()
	self.disconnectButton:setAnchorLeft(true)
	self.disconnectButton:setAnchorTop(false)
	self.disconnectButton:setAnchorBottom(true)
	self.disconnectButton.borderColor = {r=1, g=1, b=1, a=0.25}
	self:addChild(self.disconnectButton)
end

function ServerWorkshopItemScreen:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_ServerWorkshopItemScreen_Title"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title)
	ServerWorkshopItemScreen.instance = self
	self.listbox.doDrawItem = WorkshopItemList.doDrawItem
	self.disconnectButton.onclick = self.onButtonDisconnect
end

function ServerWorkshopItemScreen:onButtonInstall()
	self.installButton:setEnable(false)
	connectToServerStateCallback("install")
end

function ServerWorkshopItemScreen:onButtonDisconnect()
	self:setVisible(false)
	self.prevScreen:setVisible(true, self.joyfocus)
	connectToServerStateCallback("disconnect")
end

function ServerWorkshopItemScreen:setRequiredItems(itemIDs)
	if ConnectToServer.instance:getIsVisible() then
		self.prevScreen = ConnectToServer.instance
	elseif CoopOptionsScreen.instance:getIsVisible() then
		self.prevScreen = CoopOptionsScreen.instance
	end
	self.prevScreen:setVisible(false)
	self.listbox:clear()
	for i=1,itemIDs:size() do
		local itemID = itemIDs:get(i-1)
		local data = {}
		data.id = itemID
		-- All we have now is the item ID.  See updateItemDetails().
		self.listbox:addItem(itemID, data)
	end
	self.installButton:setEnable(false)
	self.label.name = getText("UI_ServerWorkshopItemScreen_Prompt")
	self:setVisible(true)
end

function ServerWorkshopItemScreen:updateItemDetails(detailsList)
	for i=1,detailsList:size() do
		local details = detailsList:get(i-1)
		for j=1,#self.listbox.items do
			local data = self.listbox.items[j].item
			if data.id == details:getIDString() then
				data.details = details
				break
			end
		end
	end
	self.installButton:setEnable(true)
end

function ServerWorkshopItemScreen:progress(id, downloaded, total)
	for i=1,#self.listbox.items do
		local data = self.listbox.items[i].item
		if data.id == id then
			data.downloaded = downloaded
			data.total = total
		end
	end
end

function ServerWorkshopItemScreen:new(x, y, width, height)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	ServerWorkshopItemScreen.instance = o
	return o
end

local function OnServerWorkshopItems(flag, arg1, arg2, arg3, arg4)
	if flag == "Required" then
		ServerWorkshopItemScreen.instance:setRequiredItems(arg1)
	elseif flag == "Details" then
		ServerWorkshopItemScreen.instance:updateItemDetails(arg1)
	elseif flag == "Progress" then
		ServerWorkshopItemScreen.instance:progress(arg1, arg2, arg3)
	elseif flag == "Error" then
		local itemID = arg1
		local errorName = arg2
		local error = getTextOrNull("UI_ServerWorkshopItemError_" .. (errorName or "BOGUS"))
		if not error then
			error = getText("UI_ServerWorkshopItemScreen_Error", errorName or "Unknown")
		end
		if not ServerWorkshopItemScreen.instance:getIsVisible() then
			local detail = getText("UI_WorkshopSubmit_ItemID") .. " " .. itemID
			local detail2 = getTextOrNull("UI_ServerWorkshopItemErrorDetail_" .. (errorName or "BOGUS"))
			if detail2 then
				detail = detail .. "\n" .. detail2
			end
			triggerEvent("OnConnectFailed", error, detail)
			forceDisconnect()
			return
		end
		ServerWorkshopItemScreen.instance.label.name = error
		ServerWorkshopItemScreen.instance.installButton:setEnable(false)
	elseif flag == "Success" then
		ServerWorkshopItemScreen.instance:setVisible(false)
	end
end

if getSteamModeActive() then
	Events.OnServerWorkshopItems.Add(OnServerWorkshopItems)
end
