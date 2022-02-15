require "ISUI/ISPanel"

---@class ISSpinBox : ISPanel
ISSpinBox = ISPanel:derive("ISSpinBox")

function ISSpinBox:addOption(option)
	table.insert(self.options, option)
	if self.selected == 0 then
		self.selected = 1
	end
end

function ISSpinBox:createChildren()
	self.leftButton = ISButton:new(1, 1, 15, self.height - 2, "", self, nil)
	self.leftButton.internal = "LESS"
	self.leftButton:setImage(getTexture("media/ui/ArrowLeft.png"))
	self.leftButton:setRepeatWhilePressed(ISSpinBox.onButton)
	self:addChild(self.leftButton)

	self.rightButton = ISButton:new(self.width - 15 - 1, 1, 15, self.height - 2, "", self, nil)
	self.rightButton.internal = "MORE"
	self.rightButton:setImage(getTexture("media/ui/ArrowRight.png"))
	self.rightButton:setRepeatWhilePressed(ISSpinBox.onButton)
	self:addChild(self.rightButton)
end

function ISSpinBox:onButton(button)
	if button.internal == "LESS" then
		if self.selected > 1 then
			self.selected = self.selected - 1
		end
	end
	if button.internal == "MORE" then
		if self.selected < #self.options then
			self.selected = self.selected + 1
		end
	end
	if self.targetFunc then
		self.targetFunc(self.target, self);
	end
end

function ISSpinBox:prerender()
	self.leftButton:setEnable(self.selected > 1)
	self.rightButton:setEnable(self.selected < #self.options)
	self.leftButton.borderColor.a = 0
	self.rightButton.borderColor.a = 0

    self.fade:setFadeIn(self.joypadFocused or self:isMouseOver())
    self.fade:update()

	self:drawRectStatic(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    local alpha = math.min(self.borderColor.a + 0.2 * self.fade:fraction(), 1.0)
	self:drawRectBorderStatic(0, 0, self.width, self.height, alpha, self.borderColor.r, self.borderColor.g, self.borderColor.b);

	if self:isMouseOver() and self.tooltip then
		local text = self.tooltip
		if not self.tooltipUI then
			self.tooltipUI = ISToolTip:new()
			self.tooltipUI:setOwner(self)
			self.tooltipUI:setVisible(false)
			self.tooltipUI:setAlwaysOnTop(true)
		end
		if not self.tooltipUI:getIsVisible() then
			self.tooltipUI:addToUIManager()
			self.tooltipUI:setVisible(true)
		end
		self.tooltipUI.description = self.tooltip
		self.tooltipUI:setX(self:getMouseX() + 23)
		self.tooltipUI:setY(self:getMouseY() + 23)
	else
		if self.tooltipUI and self.tooltipUI:getIsVisible() then
			self.tooltipUI:setVisible(false)
			self.tooltipUI:removeFromUIManager()
		end
    end
end

function ISSpinBox:render()
	if self.options[self.selected] then
		local fontHgt = getTextManager():getFontFromEnum(UIFont.Small):getLineHeight()
		self:drawTextCentre(self.options[self.selected], self.width / 2, (self.height / 2) - (fontHgt / 2), 1, 1, 1, 1)
	end
end

function ISSpinBox:new(x, y, width, height, target, targetFunc)
	local o = ISPanel:new(x, y, width, height)
	setmetatable(o, self)
	self.__index = self
	o.backgroundColor = {r=0, g=0, b=0, a=1}
	o.borderColor = {r=1, g=1, b=1, a=0.5}
	o.options = {}
	o.selected = 0
	o.target = target
	o.targetFunc = targetFunc
	o.fade = UITransition.new()
	return o
end
