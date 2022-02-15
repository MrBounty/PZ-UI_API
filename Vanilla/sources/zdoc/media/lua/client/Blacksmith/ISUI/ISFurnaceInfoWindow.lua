require "ISUI/ISCollapsableWindow"

---@class ISFurnaceInfoWindow : ISCollapsableWindow
ISFurnaceInfoWindow = ISCollapsableWindow:derive("ISFurnaceInfoWindow")
ISFurnaceInfoWindow.windows = {}

function ISFurnaceInfoWindow:createChildren()
	ISCollapsableWindow.createChildren(self)
	self.panel = ISToolTip:new()
	self.panel.followMouse = false
	self.panel:initialise()
	self:setObject(self.object)
	self:addView(self.panel)
end

function ISFurnaceInfoWindow:update()
	ISCollapsableWindow.update(self)

	if self:getIsVisible() and (not self.object or self.object:getObjectIndex() == -1) then
		if self.joyfocus then
			self.joyfocus.focus = nil
			updateJoypadFocus(self.joyfocus)
		end
		self:removeFromUIManager()
		return
	end

	if self.fuel ~= self.object:getFuelAmount() or self.fireStartedBool ~= self.object:isFireStarted() or self.heat ~= self.object:getHeat() then
		self:setObject(self.object)
	end
	self:setWidth(self.panel:getWidth())
	self:setHeight(self:titleBarHeight() + self.panel:getHeight())
end

function ISFurnaceInfoWindow:setObject(object)
	self.object = object
	self.panel:setName("Stone Furnace")
	self.panel:setTexture(object:getTextureName())
	self.fuel = object:getFuelAmount()
    self.fireStartedBool = object:isFireStarted();
    if self.fireStartedBool then
        self.fireStarted = "True";
    else
        self.fireStarted = "False";
    end
    self.heat = object:getHeat();
	self.panel.description = getText("IGUI_Generator_FuelAmount", luautils.round(self.fuel,2)) .. " <LINE> Fire Started: " .. self.fireStarted .. " <LINE> Heat: " .. luautils.round(self.heat,2) .. "%";
    if self.heat < 25 then
        self.panel.description = self.panel.description .. " <LINE> " .. getText("IGUI_Furnace_Bellows")
    end
end

function ISFurnaceInfoWindow:onGainJoypadFocus(joypadData)
	self.drawJoypadFocus = true
end

function ISFurnaceInfoWindow:onJoypadDown(button)
	if button == Joypad.BButton then
		self:removeFromUIManager()
		setJoypadFocus(self.playerNum, nil)
	end
end

function ISFurnaceInfoWindow:close()
	self:removeFromUIManager()
end

function ISFurnaceInfoWindow:new(x, y, character, object)
	local width = 320
	local height = 16 + 64 + 16 + 16
	local o = ISCollapsableWindow:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.character = character
	o.playerNum = character:getPlayerNum()
	o.object = object
	o:setResizable(false)
	return o
end
