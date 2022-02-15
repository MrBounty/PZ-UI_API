require "ISUI/ISCollapsableWindow"

---@class ISBBQInfoWindow : ISCollapsableWindow
ISBBQInfoWindow = ISCollapsableWindow:derive("ISBBQInfoWindow")
ISBBQInfoWindow.windows = {}

function ISBBQInfoWindow:createChildren()
	ISCollapsableWindow.createChildren(self)
	self.panel = ISToolTip:new()
	self.panel.followMouse = false
	self.panel:initialise()
	self:setObject(self.object)
	self:addView(self.panel)
end

function ISBBQInfoWindow:update()
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

function ISBBQInfoWindow:onJoypadDown(button)
	if button == Joypad.BButton then
		self:removeFromUIManager()
		setJoypadFocus(self.playerNum, nil)
	end
end

function ISBBQInfoWindow:setObject(bbq)
	self.object = bbq
	self.panel:setName(bbq:isPropaneBBQ() and getText("IGUI_BBQ_TypePropane") or getText("IGUI_BBQ_TypeCharcoal"))
	self.spriteName = bbq:getTextureName()
	self.fuelAmount = bbq:getFuelAmount()
	self.isLit = bbq:isLit()
	self.panel:setTexture(self.spriteName)
	local fireState;
	if bbq:isLit() then
		fireState = getText("IGUI_Fireplace_Burning")
	elseif bbq:isSmouldering() then
		fireState = getText("IGUI_Fireplace_Smouldering")
	else
		fireState = getText("IGUI_Fireplace_Unlit")
	end
	self.panel.description = getText("IGUI_BBQ_FuelAmount", ISCampingMenu.timeString(self.fuelAmount)) .. " (" .. fireState .. ")"
	if bbq:isPropaneBBQ() and not bbq:hasPropaneTank() then
		self.panel.description = self.panel.description .. " <LINE> <RGB:1,0,0> " .. getText("IGUI_BBQ_NeedsPropaneTank")
	end
end

function ISBBQInfoWindow:onGainJoypadFocus(joypadData)
	self.drawJoypadFocus = true
end

function ISBBQInfoWindow:close()
	self:removeFromUIManager()
end

function ISBBQInfoWindow:new(x, y, character, bbq)
	local width = 320
	local height = 16 + 64 + 16 + 16
	local o = ISCollapsableWindow:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.playerNum = character:getPlayerNum()
	o.object = bbq
	o:setResizable(false)
	return o
end
