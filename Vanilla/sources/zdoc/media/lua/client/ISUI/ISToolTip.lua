require "ISUI/ISPanel"

---@class ISToolTip : ISPanel
ISToolTip = ISPanel:derive("ISToolTip");


--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISToolTip:initialise()
	ISPanel.initialise(self);
end

function ISToolTip:instantiate()
	ISPanel.instantiate(self)
	self.javaObject:setConsumeMouseEvents(false)
end

function ISToolTip:setName(name)
	self.name = name;
end

function ISToolTip:setContextMenu(contextMenu)
	self.contextMenu = contextMenu;
end

function ISToolTip:setTexture(textureName)
	self.texture = getTexture(textureName);
end

function ISToolTip:onMouseDown(x, y)
	return false
end

function ISToolTip:onMouseUp(x, y)
	return false
end

function ISToolTip:onRightMouseDown(x, y)
	return false
end

function ISToolTip:onRightMouseUp(x, y)
	return false
end

function ISToolTip:prerender()
	if self.owner and not self.owner:isReallyVisible() then
		self:removeFromUIManager()
		self:setVisible(false)
		return
	end
	self:doLayout()
	self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
end


--************************************************************************--
--** ISPanel:render
--**
--************************************************************************--
function ISToolTip:render()

	local mx = getMouseX() + 32
	local my = getMouseY() + 10
	if not self.followMouse then
		mx = self:getX()
		my = self:getY()
	end
	if self.desiredX and self.desiredY then
		mx = self.desiredX
		my = self.desiredY
	end
	self:setX(mx)
	self:setY(my)

	if self.contextMenu and self.contextMenu.joyfocus then
		local playerNum = self.contextMenu.player
		self:setX(getPlayerScreenLeft(playerNum) + 60);
		self:setY(getPlayerScreenTop(playerNum) + 60);
	elseif self.contextMenu and self.contextMenu.currentOptionRect then
		if self.contextMenu.currentOptionRect.height > 32 then
			self:setY(my + self.contextMenu.currentOptionRect.height)
		end
		self:adjustPositionToAvoidOverlap(self.contextMenu.currentOptionRect)
	elseif self.owner and self.owner.isButton then
		local ownerRect = { x = self.owner:getAbsoluteX(), y = self.owner:getAbsoluteY(), width = self.owner.width, height = self.owner.height }
		self:adjustPositionToAvoidOverlap(ownerRect)
	end

	-- big rectangle (our background)
	self:drawRect(0, 0, self.width, self.height, 0.7, 0.05, 0.05, 0.05)
	self:drawRectBorder(0, 0, self.width, self.height, 0.5, 0.9, 0.9, 1)

	-- render texture
	if self.texture then
		local widthTexture = self.texture:getWidth()
		local heightTexture = self.texture:getHeight()
		local textureY = self.name and 35 or 5
		self:drawTextureScaled(self.texture, 8, textureY, widthTexture, heightTexture, 1, 1, 1, 1)
	end

	-- render name
	if self.name then
		self:drawText(self.name, 8, 5, 1, 1, 1, 1, UIFont.Medium)
	end

	self:renderContents()
	
	-- render a how to rotate message at the bottom if needed
	if self.footNote then
		local fontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()
		self:drawTextCentre(self.footNote, self:getWidth() / 2, self:getHeight() - fontHgt - 4, 1, 1, 1, 1, UIFont.Small)
	end
end

function ISToolTip:doLayout()
	local textX = 10
	local textY = 0
	local textWidth = 0
	local textHeight = 0

	local textureX = 8
	local textureY = 5
	local textureWidth = 0
	local textureHeight = 0
	if self.texture then
		textureWidth = self.texture:getWidth()
		textureHeight = self.texture:getHeight() + 5
		textX = textureX + textureWidth + 10
	end

	local nameX = 8
	local nameWidth = 0
	local nameHeight = 0
	if self.name then
		nameWidth = getTextManager():MeasureStringX(UIFont.Medium, self.name) + 50
		nameHeight = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
		textureY = 35
		textY = 25
	end

	textWidth, textHeight = self:layoutContents(textX, textY, math.max(220, nameWidth))

	local myWidth = 220
	local myHeight = math.max(nameHeight, math.max(textureY + textureHeight, textY + textHeight))

	if myWidth < textX + textWidth then
		myWidth = textX + textWidth
    end
	if myWidth < nameWidth then
		myWidth = nameWidth
	end

--	if self.texture and myHeight < textureHeight + 40 then
--		myHeight = textureHeight + 40
--	end

	if self.footNote then
		local noteWidth = getTextManager():MeasureStringX(UIFont.Small, self.footNote)
		if myWidth < noteWidth then myWidth = noteWidth end
		local fontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()
		myHeight = myHeight + fontHgt + 4
	end

	self:setWidth(myWidth + 20)
	self:setHeight(myHeight)
end

function ISToolTip.GetFont()
	if getCore():getOptionTooltipFont() == "Large" then
		return UIFont.Large
	elseif getCore():getOptionTooltipFont() == "Medium" then
		return UIFont.Medium
	end
	return UIFont.NewSmall
end

function ISToolTip:layoutContents(x, y, myWidth)
	local textWidth = 0
	local textHeight = 0

	if self.description ~= "" then
		self.descriptionPanel.defaultFont = ISToolTip.GetFont()
		self.descriptionPanel.text = self.description
		local widthScale = getTextManager():getFontHeight(self.descriptionPanel.defaultFont) / 15
		if self.maxLineWidth then
			self.descriptionPanel.maxLineWidth = self.maxLineWidth * widthScale
			self.descriptionPanel:setWidth(self.descriptionPanel.marginLeft + self.descriptionPanel.marginRight)
		else
			self.descriptionPanel:setWidth(math.max(180 * widthScale, myWidth - x))
		end
		self.descriptionPanel:paginate()

		local maxLineWidth = 0
		for i,v in ipairs(self.descriptionPanel.lines) do
			local lineWidth = self.descriptionPanel.lineX[i] + getTextManager():MeasureStringX(self.descriptionPanel.defaultFont, v);
			if lineWidth > maxLineWidth then
				maxLineWidth = lineWidth
			end
		end
		local panelWidth = maxLineWidth + self.descriptionPanel.marginLeft + self.descriptionPanel.marginRight
		if panelWidth > self.descriptionPanel:getWidth() then
			self.descriptionPanel:setWidth(panelWidth)
			self.descriptionPanel:paginate()
--		elseif panelWidth < self.descriptionPanel:getWidth() then
--			self.descriptionPanel:setWidth(panelWidth)
--			self.descriptionPanel:paginate()
		end
		
		textWidth = self.descriptionPanel:getWidth()
		textHeight = self.descriptionPanel:getHeight()
	end

	return textWidth, textHeight
end

function ISToolTip:renderContents()
	if self.description ~= "" then
		if self.texture then
			self.descriptionPanel:setX(self:getAbsoluteX() + 8 + self.texture:getWidth() + 10);
		else
			self.descriptionPanel:setX(self:getAbsoluteX() + 10);
		end
		local y = 0
		if self.name then
			y = 25
		end
		self.descriptionPanel:setY(self:getAbsoluteY() + y)
		self.descriptionPanel:prerender()
		self.descriptionPanel:render()
	end
end

function ISToolTip:setDesiredPosition(x, y)
	self.desiredX = x
	self.desiredY = y
	if not x or not y then return end
	self:setX(x)
	self:setY(y)
	if self.owner and self.owner.isButton then
		local ownerRect = { x = self.owner:getAbsoluteX(), y = self.owner:getAbsoluteY(), width = self.owner.width, height = self.owner.height }
		self:adjustPositionToAvoidOverlap(ownerRect)
	end
end

function ISToolTip:adjustPositionToAvoidOverlap(avoidRect)
	local myRect = { x = self.x, y = self.y, width = self.width, height = self.height }

	if self.contextMenu and not self.contextMenu.joyfocus and self.contextMenu.currentOptionRect then
		myRect.y = avoidRect.y
		local r = self:placeRight(myRect, avoidRect)
		if self:overlaps(r, avoidRect) then
			r = self:placeLeft(myRect, avoidRect)
			if self:overlaps(r, avoidRect) then
				r = self:placeAbove(myRect, avoidRect)
			end
		end
		self:setX(r.x)
		self:setY(r.y)
		return
	end

	if self:overlaps(myRect, avoidRect) then
		local r = self:placeRight(myRect, avoidRect)
		if self:overlaps(r, avoidRect) then
			r = self:placeAbove(myRect, avoidRect)
			if self:overlaps(r, avoidRect) then
				r = self:placeLeft(myRect, avoidRect)
			end
		end
		self:setX(r.x)
		self:setY(r.y)
	end
end

function ISToolTip:overlaps(r1, r2)
	return r1.x + r1.width > r2.x and r1.x < r2.x + r2.width and
			r1.y + r1.height > r2.y and r1.y < r2.y + r2.height
end

function ISToolTip:placeLeft(r1, r2)
	local r = r1
	r.x = math.max(0, r2.x - r.width - 8)
	return r
end

function ISToolTip:placeRight(r1, r2)
	local r = r1
	r.x = r2.x + r2.width + 8
	r.x = math.min(r.x, getCore():getScreenWidth() - r.width)
	return r
end

function ISToolTip:placeAbove(r1, r2)
	local r = r1
	r.y = r2.y - r.height - 8
	r.y = math.max(0, r.y)
	return r
end

function ISToolTip:setOwner(ui)
	self.owner = ui
end

local function setRGBA(rgba, r, g, b, a)
    rgba.r = r
    rgba.g = g
    rgba.b = b
    rgba.a = a
    return rgba
end

function ISToolTip:reset()
    self:setVisible(false)
    self:noBackground()
    self.name = nil
    self.description = ""
    self.texture = nil
    self.footNote = nil
    setRGBA(self.borderColor, 0.4, 0.4, 0.4, 1.0)
    setRGBA(self.backgroundColor, 0.0, 0.0, 0.0, 0.0)
    self.width = 0
    self.height = 0
    self.maxLineWidth = nil
    self.desiredX = nil
    self.desiredY = nil
    self.anchorLeft = true
    self.anchorRight = false
    self.anchorTop = true
    self.anchorBottom = false
    self.descriptionPanel.marginLeft = 0
    self.descriptionPanel.marginRight = 0
    setRGBA(self.descriptionPanel.backgroundColor, 0, 0, 0, 0.3)
    setRGBA(self.descriptionPanel.borderColor, 1, 1, 1, 0.1)
    self.owner = nil
    self.contextMenu = nil
    self.followMouse = true
end

--************************************************************************--
--** ISPanel:new
--**
--************************************************************************--
function ISToolTip:new()
   local o = ISPanel.new(self, 0, 0, 0, 0);
   o:noBackground();
   o.name = nil;
   o.description = "";
   o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
   o.backgroundColor = {r=0, g=0, b=0, a=0};
   o.width = 0;
   o.height = 0;
   o.anchorLeft = true;
   o.anchorRight = false;
   o.anchorTop = true;
   o.anchorBottom = false;
   -- description panel
   o.descriptionPanel = ISRichTextPanel:new(0, 0, 0, 0);
   o.descriptionPanel.marginLeft = 0
   o.descriptionPanel.marginRight = 0
   o.descriptionPanel:initialise();
   o.descriptionPanel:instantiate();
   o.descriptionPanel:noBackground();
   o.descriptionPanel.backgroundColor = {r=0, g=0, b=0, a=0.3};
   o.descriptionPanel.borderColor = {r=1, g=1, b=1, a=0.1};
   o.owner = nil
   o.followMouse = true
   return o;
end

