--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanel"

local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

---@class DebugChunkState_VehicleStoryPanel : ISPanel
DebugChunkState_VehicleStoryPanel = ISPanel:derive("DebugChunkState_VehicleStoryPanel")
local VehicleStoryPanel = DebugChunkState_VehicleStoryPanel

function VehicleStoryPanel:createChildren()
	local comboHeight = FONT_HGT_MEDIUM + 3 * 2
	local combo = ISComboBox:new(0, 0, self.width, comboHeight, self, self.onChangeStory)
	self:addChild(combo)
	self.combo = combo

	local stories = getWorld():getRandomizedVehicleStoryList()
	local sorted = {}
	for i=1,stories:size() do
		table.insert(sorted, stories:get(i-1):getName())
	end
	table.sort(sorted)
	for _,name in ipairs(sorted) do
		combo:addOption(name)
	end

	combo:select(self.debugChunkState.gameState:fromLua0("getVehicleStory"))
end

function VehicleStoryPanel:update()
	ISPanel.update(self)
end

function VehicleStoryPanel:render()
	ISPanel.render(self)
end

function VehicleStoryPanel:onChangeStory()
	self.debugChunkState.gameState:fromLua1("setVehicleStory", self.combo:getSelectedText())
end

function VehicleStoryPanel:new(x, y, width, height, debugChunkState)
	local comboHeight = FONT_HGT_MEDIUM + 3 * 2
	height = comboHeight
	local o = ISPanel.new(self, x, y, width, height)
	o.backgroundColor.a = 0.8
	o.debugChunkState = debugChunkState
	return o
end



