--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

---@class ISRainPanel : ISPanel
ISRainPanel = ISPanel:derive("ISRainPanel")

function ISRainPanel:createChildren()
	local label = ISLabel:new(self.width / 3, 16, 25, "Intensity", 1, 1, 1, 1, UIFont.Medium, false)
	label:initialise()
	self:addChild(label)

	self.intensity = ISSpinBox:new(label:getRight() + 8, 16, 80, 25, self, self.changeIntensity)
	self.intensity.font = UIFont.Medium
	self:addChild(self.intensity)
	self.intensity:addOption("Weather")
	self.intensity:addOption("1")
	self.intensity:addOption("2")
	self.intensity:addOption("3")
	self.intensity:addOption("4")
	self.intensity:addOption("5")
	self.intensity.selected = 1

	label = ISLabel:new(self.width / 3, self.intensity:getBottom() + 16, 25, "Speed", 1, 1, 1, 1, UIFont.Medium, false)
	label:initialise()
	self:addChild(label)

	self.speed = ISSpinBox:new(label:getRight() + 8, self.intensity:getBottom() + 16, 80, 25, self, self.changeSpeed)
	self.speed.font = UIFont.Medium
	self:addChild(self.speed)
	for i=1,10 do
		self.speed:addOption(tostring(i))
	end
	self.speed.selected = 6

	label = ISLabel:new(self.width / 3, self.speed:getBottom() + 16, 25, "Alpha", 1, 1, 1, 1, UIFont.Medium, false)
	label:initialise()
	self:addChild(label)

	self.alpha = ISTextEntryBox:new("0.6", label:getRight() + 8, label:getY(), 80, 25)
	self.alpha.font = UIFont.Medium
	self.alpha.onCommandEntered = self.onChangeAlpha
	self:addChild(self.alpha)
	self.alpha:setOnlyNumbers(true)

	self.reloadButton = ISButton:new((self.width - 150) / 2, self.alpha:getBottom() + 16, 150, 25, "Reload Textures", self, self.reloadTextures);
	self.reloadButton.internal = "RELOAD";
	self.reloadButton:initialise();
	self.reloadButton:instantiate();
	self:addChild(self.reloadButton);
end

function ISRainPanel:changeIntensity()
	rainConfig("intensity", self.intensity.selected - 1)
end

function ISRainPanel:changeSpeed()
	rainConfig("speed", self.speed.selected)
end

function ISRainPanel:onChangeAlpha()
	local alpha = tonumber(self:getText())
	if alpha >= 0 and alpha <= 1 then
		rainConfig("alpha", alpha * 100)
	end
end

function ISRainPanel:reloadTextures()
	rainConfig("reloadTextures", 0)
end

function addRainPanel()
	local panel = ISRainPanel:new(getCore():getScreenWidth() - 300, getCore():getScreenHeight() - 300, 280, 200)
	local window = panel:wrapInCollapsableWindow()
	window:addToUIManager()
end

