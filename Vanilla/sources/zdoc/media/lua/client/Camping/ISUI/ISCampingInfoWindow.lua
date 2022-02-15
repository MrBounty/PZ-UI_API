require "ISUI/ISCollapsableWindow"
require 'Camping/CCampfireSystem'

---@class ISCampingInfoWindow : ISCollapsableWindow
ISCampingInfoWindow = ISCollapsableWindow:derive("ISCampingInfoWindow")
ISCampingInfoWindow.windows = {}

function ISCampingInfoWindow:createChildren()
	ISCollapsableWindow.createChildren(self)
	self.panel = ISToolTip:new()
	self.panel.followMouse = false
	self.panel:initialise()
	self:setObject(self.object)
	self:addView(self.panel)
end

function ISCampingInfoWindow:update()
	ISCollapsableWindow.update(self)

	if self:getIsVisible() and (not self.object or self.object:getObjectIndex() == -1) then
		if self.joyfocus then
			self.joyfocus.focus = nil
			updateJoypadFocus(self.joyfocus)
		end
		self:removeFromUIManager()
		return
	end
	self.campfireTable = CCampfireSystem.instance:getLuaObjectOnSquare(self.object:getSquare())
	if self.fuelAmount ~= self.campfireTable.fuelAmt or
			self.spriteName ~= self.object:getSpriteName() or
			self.isLit ~= self.campfireTable.isLit then
		self:setObject(self.object)
	end
	self:setWidth(self.panel:getWidth())
	self:setHeight(self:titleBarHeight() + self.panel:getHeight())
end

function ISCampingInfoWindow:onJoypadDown(button)
	if button == Joypad.BButton then
		self:removeFromUIManager()
		setJoypadFocus(self.playerNum, nil)
	end
end

function ISCampingInfoWindow:setObject(campfireObject)
	self.object = campfireObject
	self.campfireTable = CCampfireSystem.instance:getLuaObjectOnSquare(self.object:getSquare())
	local fireState;
	if self.campfireTable.isLit == true then
		fireState = getText("IGUI_Fireplace_Burning")
	else
		fireState = getText("IGUI_Fireplace_Unlit")
	end
	self.panel:setName(getText("IGUI_Campfire_Campfire"))
	self.spriteName = campfireObject:getSpriteName()
	self.fuelAmount = self.campfireTable.fuelAmt
	self.isLit = self.campfireTable.isLit == true
	self.panel:setTexture(self.spriteName)
	self.panel.description = getText("IGUI_BBQ_FuelAmount", ISCampingMenu.timeString(luautils.round(self.fuelAmount))) .. " (" .. fireState .. ")"
end

function ISCampingInfoWindow:onGainJoypadFocus(joypadData)
	self.drawJoypadFocus = true
end

function ISCampingInfoWindow:close()
	self:removeFromUIManager()
end

function ISCampingInfoWindow:new(x, y, character, campfireObject, campfireTable)
	local width = 320
	local height = 16 + 64 + 16 + 16
	local o = ISCollapsableWindow:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.playerNum = character:getPlayerNum()
	o.campfireTable = campfireTable
	o.object = campfireObject
	o:setResizable(false)
	return o
end
