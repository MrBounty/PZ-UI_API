--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISUIElement"

---@class ISVehicleGauge : ISUIElement
ISVehicleGauge = ISUIElement:derive("ISVehicleGauge")

function ISVehicleGauge:setNeedleWidth(width)
	self.javaObject:setNeedleWidth(width)
end

function ISVehicleGauge:setTexture(texture)
	self.javaObject:setTexture(texture)
end

function ISVehicleGauge:setValue(value)
	self.javaObject:setValue(value)
end

function ISVehicleGauge:instantiate()
	self.javaObject = VehicleGauge.new(self.texture, self.needleX, self.needleY, self.minAngle, self.maxAngle)
	self.javaObject:setTable(self)
	self.javaObject:setX(self.x)
	self.javaObject:setY(self.y)
	self.javaObject:setWidth(self.width)
	self.javaObject:setHeight(self.height)
	self.javaObject:setAnchorLeft(self.anchorLeft)
	self.javaObject:setAnchorRight(self.anchorRight)
	self.javaObject:setAnchorTop(self.anchorTop)
	self.javaObject:setAnchorBottom(self.anchorBottom)
end

function ISVehicleGauge:new(x, y, texture, needleX, needleY, minAngle, maxAngle)
	local o = ISUIElement:new(x, y, texture:getWidth(), texture:getHeight())
	setmetatable(o, self)
	self.__index = self
	o.texture = texture
	o.needleX = needleX
	o.needleY = needleY
	o.minAngle = minAngle
	o.maxAngle = maxAngle
	return o
end
