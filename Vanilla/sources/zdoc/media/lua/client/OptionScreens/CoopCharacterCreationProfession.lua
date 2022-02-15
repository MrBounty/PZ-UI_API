--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "OptionScreens/CharacterCreationProfession"

---@class CoopCharacterCreationProfession : CharacterCreationProfession
CoopCharacterCreationProfession = CharacterCreationProfession:derive("CoopCharacterCreationProfession")

local _onOptionMouseDown = CharacterCreationProfession.onOptionMouseDown
function CoopCharacterCreationProfession:onOptionMouseDown(button, x, y)
	if button.internal == "BACK" then
		self:setVisible(false)
		if CoopMapSpawnSelect.instance:hasChoices() then
			CoopMapSpawnSelect.instance:setVisible(true, self.joyfocus)
			return
		end
		if CoopUserName.instance:shouldShow() then
			CoopUserName.instance:setVisible(true, self.joyfocus)
			return
		end
		CoopCharacterCreation.instance:cancel()
	end
	if button.internal == "NEXT" then
		self:setVisible(false)
		CharacterCreationMain.instance:setVisible(true, self.joyfocus)
	end
	if button.internal ~= "BACK" and button.internal ~= "NEXT" then
		CharacterCreationProfession.onOptionMouseDown(self, button, x, y)
	end
end

function CoopCharacterCreationProfession:new(x, y, width, height)
	local o = CharacterCreationProfession.new(self, x, y, width, height)
	o:setUIName("CoopCharacterCreationProfession")
	return o
end

