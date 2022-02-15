--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISUIElement"

ISUI3DModel = ISUIElement:derive("ISUI3DModel")

function ISUI3DModel:instantiate()
	self.javaObject = UI3DModel.new(self)
	self.javaObject:setX(self.x)
	self.javaObject:setY(self.y)
	self.javaObject:setWidth(self.width)
	self.javaObject:setHeight(self.height)
	self.javaObject:setAnchorLeft(self.anchorLeft)
	self.javaObject:setAnchorRight(self.anchorRight)
	self.javaObject:setAnchorTop(self.anchorTop)
	self.javaObject:setAnchorBottom(self.anchorBottom)
end

function ISUI3DModel:prerender()
	ISUIElement.prerender(self)
	if self.javaObject then
		self.javaObject:setAnimate(self.animateWhilePaused or not isGamePaused())
	end
end

function ISUI3DModel:onMouseDown(x, y)
	self.mouseDown = true
	self.dragX = 0
	self:setCapture(true)
	return true
end

function ISUI3DModel:onMouseMove(dx, dy)
	if self.mouseDown then
		self.dragX = self.dragX + dx
		if math.abs(self.dragX) > 40 then
			local dir = self:getDirection()
			dir = IsoDirectionSet.rotate(dir, (self.dragX < 0) and -1 or 1)
			self:setDirection(dir)
			self.dragX = 0
		end
	end
	return true
end

function ISUI3DModel:onMouseMoveOutside(dx, dy)
	if self.mouseDown then
		-- This shouldn't happen, but the way setCapture() works is broken.
		self.dragX = self.dragX + dx
		if math.abs(self.dragX) > 40 then
			local dir = self:getDirection()
			dir = IsoDirectionSet.rotate(dir, (self.dragX < 0) and -1 or 1)
			self:setDirection(dir)
			self.dragX = 0
		end
	end
end

function ISUI3DModel:onMouseUp(x, y)
	if self.mouseDown then
		self.mouseDown = false
		self.dragX = 0
		self:setCapture(false)
	end
	return true
end

function ISUI3DModel:onMouseUpOutside(x, y)
	if self.mouseDown then
		self.mouseDown = false
		self.dragX = 0
		self:setCapture(false)
	end
	return true
end

function ISUI3DModel:setAnimateWhilePaused(animate)
	self.animateWhilePaused = animate
end

function ISUI3DModel:setDirection(dir)
	self.javaObject:setDirection(dir)
end

function ISUI3DModel:getDirection()
	return self.javaObject:getDirection()
end

-- clothing.xml <m_Name>name</m_Name>: Chef, Foreman, etc
function ISUI3DModel:setOutfitName(name, female, zombie)
	self.javaObject:setOutfitName(name, female, zombie)
end

function ISUI3DModel:setCharacter(character)
	self.javaObject:setCharacter(character)
end

function ISUI3DModel:setSurvivorDesc(survivorDesc)
	self.javaObject:setSurvivorDesc(survivorDesc)
end

-- AnimSet state: idle, sprint, etc
function ISUI3DModel:setState(state)
	self.javaObject:setState(state)
end

function ISUI3DModel:reportEvent(event)
	self.javaObject:reportEvent(event)
end

function ISUI3DModel:setIsometric(iso)
	self.javaObject:setIsometric(iso)
end

function ISUI3DModel:setDoRandomExtAnimations(doExt)
	self.javaObject:setDoRandomExtAnimations(doExt)
end

function ISUI3DModel:setZoom(zoom)
	self.javaObject:setZoom(zoom);
end

function ISUI3DModel:setYOffset(yoffset)
	self.javaObject:setYOffset(yoffset);
end

function ISUI3DModel:setXOffset(xoffset)
	self.javaObject:setXOffset(xoffset);
end

function ISUI3DModel:new(x, y, width, height)
	local o = ISUIElement.new(self, x, y, width, height)
	o.animateWhilePaused = false
	return o
end

