require "ISUI/ISCollapsableWindow"

---@class ISFireplaceInfoWindow : ISCollapsableWindow
ISFireplaceInfoWindow = ISCollapsableWindow:derive("ISFireplaceInfoWindow")
ISFireplaceInfoWindow.windows = {}

function ISFireplaceInfoWindow:createChildren()
	ISCollapsableWindow.createChildren(self)
	self.panel = ISToolTip:new()
	self.panel.followMouse = false
	self.panel:initialise()
	self:setObject(self.object)
	self:addView(self.panel)
end

function ISFireplaceInfoWindow:update()
	ISCollapsableWindow.update(self)

	if self:getIsVisible() and (not self.object or self.object:getObjectIndex() == -1) then
		if self.joyfocus then
			self.joyfocus.focus = nil
			updateJoypadFocus(self.joyfocus)
		end
		self:removeFromUIManager()
		return
	end

	if self.fuelAmount ~= self.object:getFuelAmount() or
			self.spriteName ~= self.object:getTextureName() or
			self.isLit ~= self.object:isLit() then
		self:setObject(self.object)
	end
	self:setWidth(self.panel:getWidth())
	self:setHeight(self:titleBarHeight() + self.panel:getHeight())
end

function ISFireplaceInfoWindow:onJoypadDown(button)
	if button == Joypad.BButton then
		self:removeFromUIManager()
		setJoypadFocus(self.playerNum, nil)
	end
end

function ISFireplaceInfoWindow:setObject(fireplace)
	self.object = fireplace
	local fireState;
	if fireplace:isLit() then
		fireState = getText("IGUI_Fireplace_Burning")
	elseif fireplace:isSmouldering() then
		fireState = getText("IGUI_Fireplace_Smouldering")
	else
		fireState = getText("IGUI_Fireplace_Unlit")
	end
	self.panel:setName(getText("IGUI_Fireplace_Fireplace"))
	self.spriteName = fireplace:getTextureName()
	self.fuelAmount = fireplace:getFuelAmount()
	self.isLit = fireplace:isLit()
	self.panel:setTexture(self.spriteName)
	self.panel.description = getText("IGUI_BBQ_FuelAmount", ISCampingMenu.timeString(luautils.round(self.fuelAmount))) .. " (" .. fireState .. ")"
end

function ISFireplaceInfoWindow:onGainJoypadFocus(joypadData)
	self.drawJoypadFocus = true
end

function ISFireplaceInfoWindow:close()
	self:removeFromUIManager()
end

function ISFireplaceInfoWindow:new(x, y, character, fireplace)
	local width = 320
	local height = 16 + 64 + 16 + 16
	local o = ISCollapsableWindow:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.playerNum = character:getPlayerNum()
	o.object = fireplace
	o:setResizable(false)
	return o
end
