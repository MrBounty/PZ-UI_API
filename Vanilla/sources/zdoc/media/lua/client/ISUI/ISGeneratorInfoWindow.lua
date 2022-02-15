require "ISUI/ISCollapsableWindow"

---@class ISGeneratorInfoWindow : ISCollapsableWindow
ISGeneratorInfoWindow = ISCollapsableWindow:derive("ISGeneratorInfoWindow")
ISGeneratorInfoWindow.windows = {}

function ISGeneratorInfoWindow:createChildren()
	ISCollapsableWindow.createChildren(self)
	self.panel = ISToolTip:new()
	self.panel.followMouse = false
	self.panel:initialise()
	self:setObject(self.object)
	self:addView(self.panel)
end

function ISGeneratorInfoWindow:update()
	ISCollapsableWindow.update(self)
	
	self.panel.maxLineWidth = 400
	self.panel.description = ISGeneratorInfoWindow.getRichText(self.object, true);

	if self:getIsVisible() and (not self.object or self.object:getObjectIndex() == -1) then
		if self.joyfocus then
			self.joyfocus.focus = nil
			updateJoypadFocus(self.joyfocus)
		end
		self:removeFromUIManager()
		return
	end

	if self.fuel ~= self.object:getFuel() or self.condition ~= self.object:getCondition() then
		self:setObject(self.object)
	end
	self:setWidth(self.panel:getWidth())
	self:setHeight(self:titleBarHeight() + self.panel:getHeight())
end

function ISGeneratorInfoWindow:setObject(object)
	self.object = object
	self.panel:setName(getText("IGUI_Generator_TypeGas"))
	self.panel:setTexture(object:getTextureName())
	self.fuel = object:getFuel()
	self.condition = object:getCondition()
--	self.panel.description = ISGeneratorInfoWindow.getRichText(object, true)
end

function ISGeneratorInfoWindow.getRichText(object, displayStats)
	local square = object:getSquare()
	if not displayStats then
		local text = " <INDENT:10> "
		if square and not square:isOutside() and square:getBuilding() then
			text = text .. " <RED> " .. getText("IGUI_Generator_IsToxic")
		end
		return text
	end
	local fuel = math.ceil(object:getFuel())
	local condition = object:getCondition()
	local text = getText("IGUI_Generator_FuelAmount", fuel) .. " <LINE> " .. getText("IGUI_Generator_Condition", condition) .. " <LINE> "
	if object:isActivated() then
		text = text ..  " <LINE> " .. getText("IGUI_PowerConsumption") .. ": <LINE> ";
		text = text .. " <INDENT:10> "
		local items = object:getItemsPowered()
		for i=0,items:size()-1 do
			text = text .. "   " .. items:get(i) .. " <LINE> ";
		end
		text = text .. getText("IGUI_Total") .. ": " .. luautils.round(object:getTotalPowerUsing(), 2) .. " L/h <LINE> ";
	end
	if square and not square:isOutside() and square:getBuilding() then
		text = text .. " <LINE> <RED> " .. getText("IGUI_Generator_IsToxic")
	end
	return text
end

function ISGeneratorInfoWindow:onGainJoypadFocus(joypadData)
	self.drawJoypadFocus = true
end

function ISGeneratorInfoWindow:onJoypadDown(button)
	if button == Joypad.BButton then
		self:removeFromUIManager()
		setJoypadFocus(self.playerNum, nil)
	end
end

function ISGeneratorInfoWindow:close()
	self:removeFromUIManager()
end

function ISGeneratorInfoWindow:new(x, y, character, object)
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
