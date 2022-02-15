--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "OptionScreens/CharacterCreationMain"

---@class CoopCharacterCreationMain : CharacterCreationMain
CoopCharacterCreationMain = CharacterCreationMain:derive("CoopCharacterCreationMain")

function CoopCharacterCreationMain:onOptionMouseDown(button, x, y)
	if button.internal == "BACK" then
		self:setVisible(false)
		CharacterCreationProfession.instance:setVisible(true, self.joyfocus)
	end
	if button.internal == "NEXT" then
		self:setVisible(false)
		CoopCharacterCreation.instance:accept()
	end
	if button.internal == "RANDOM" then
		CharacterCreationHeader.instance:onOptionMouseDown(button, x, y)
	end
end

function CoopCharacterCreationMain:new(x, y, width, height)
	local o = CharacterCreationMain.new(self, x, y, width, height)
	o:setUIName("CoopCharacterCreationMain")
	return o
end
