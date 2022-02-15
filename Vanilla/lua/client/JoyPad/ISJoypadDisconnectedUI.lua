--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

ISJoypadDisconnectedUI = ISUIElement:derive("ISJoypadDisconnectedUI");

local FONT_HGT_LARGE = getTextManager():getFontHeight(UIFont.Large)

function ISJoypadDisconnectedUI:createChildren()
	local x = getPlayerScreenLeft(self.playerNum)
	local y = getPlayerScreenTop(self.playerNum)
	local w = getPlayerScreenWidth(self.playerNum)
	local h = getPlayerScreenHeight(self.playerNum)

	local labelText = getText("IGUI_Controller_Disconnected1")
	local label = ISLabel:new(0, 0, math.ceil(FONT_HGT_LARGE * 1.5), labelText, 1.0, 1.0, 1.0, 1.0, UIFont.Large, true)
	label:initialise()
	label:instantiate()
	self:addChild(label)
	self.label = label
	local labelWidth = label.width + 32

	if self.playerNum == 0 then
		local text = getText("IGUI_Controller_Disconnected2")
		local textWidth = getTextManager():MeasureStringX(UIFont.Large, text)
		local buttonWidth = textWidth + 32
		local buttonHeight = math.ceil(FONT_HGT_LARGE * 1.5)
		local button = ISButton:new(0, label:getBottom() + 20, buttonWidth, buttonHeight, text, self, self.useKeyboardMouse)
		button:initialise()
		button:instantiate()
		self:addChild(button)
		self.buttonKBM = button

		local width = math.max(labelWidth, button.width)
		label:setX(width / 2 - labelWidth / 2 + 32 / 2)
		button:setX(width / 2 - button.width / 2)
	end

	self:setWidth(math.max(labelWidth, self.buttonKBM and self.buttonKBM.width or 0))
	self:setHeight(self.buttonKBM and self.buttonKBM:getBottom() or label:getBottom())
	self:setX(x + w / 2 - self.width / 2)
	self:setY(y + h / 2 - self.height - 40)
end

function ISJoypadDisconnectedUI:prerender()
    self:drawRect(self.label.x - 32 / 2, self.label.y, self.label.width + 32, self.label.height, 0.75, 0.0, 0.0, 0.0)
	ISUIElement.prerender(self)
end

function ISJoypadDisconnectedUI:update()
	local x = getPlayerScreenLeft(self.playerNum)
	local y = getPlayerScreenTop(self.playerNum)
	local w = getPlayerScreenWidth(self.playerNum)
	local h = getPlayerScreenHeight(self.playerNum)
	self:setX(x + w / 2 - self.width / 2)
	self:setY(y + h / 2 - self.height - 40)
end

function ISJoypadDisconnectedUI:useKeyboardMouse()
	self:removeFromUIManager()
	JoypadState.useKeyboardMouse()
end

function ISJoypadDisconnectedUI:renderControllerDisconnected()
    local x = getPlayerScreenLeft(self.player)
    local y = getPlayerScreenTop(self.player)
    local w = getPlayerScreenWidth(self.player)
    local h = getPlayerScreenHeight(self.player)
    local text1 = getText("IGUI_Controller_Disconnected1")
    local textWidth1 = getTextManager():MeasureStringX(UIFont.Large, text1)
    local boxWidth = textWidth1 + 32
    local boxHeight = math.ceil(FONT_HGT_LARGE * 1.5)
    local pausedDY = 40
    local midY = y + h / 2 - pausedDY - boxHeight / 2
    self:drawRect(x + w / 2 - boxWidth / 2, midY - boxHeight / 2, boxWidth, boxHeight, 0.75, 0.0, 0.0, 0.0)
    self:drawTextCentre(text1, x + w / 2, midY - FONT_HGT_LARGE / 2, 1, 1, 1, 1, UIFont.Large)
end

function ISJoypadDisconnectedUI:new(playerNum)
	o = ISUIElement.new(self, 0, 0, 1, 1)
	o.playerNum = playerNum
	return o
end
