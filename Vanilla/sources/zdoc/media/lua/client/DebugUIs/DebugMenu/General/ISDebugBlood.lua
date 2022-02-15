---@class ISDebugBlood : ISDebugSubPanelBase
ISDebugBlood = ISDebugSubPanelBase:derive("ISDebugBlood")

function ISDebugBlood:initialise()
	ISDebugSubPanelBase.initialise(self)
end

function ISDebugBlood:createChildren()
	ISDebugSubPanelBase.createChildren(self)

	local x,y,w, obj = 10,10,self.width-30

	self:initHorzBars(x, w)

	y, obj = ISDebugUtils.addLabel(self, "float_title", x+(w/2), y, "Blood on Body, not Clothes", UIFont.Medium)
	obj.center = true
	y = ISDebugUtils.addHorzBar(self, y+5)+5

	local barMod = 3
	local y2, label, value, slider
	self.partToSlider = {}
	for i=0,BloodBodyPartType.MAX:index()-1 do
		local part = BloodBodyPartType.FromIndex(i)
		y2,label = ISDebugUtils.addLabel(self, part, x, y, part:name(), UIFont.Small)
		y2,value = ISDebugUtils.addLabel(self, part, x+(w/2)-20, y, "0", UIFont.Small, false)
		y,slider = ISDebugUtils.addSlider(self, part, x+(w/2), y, w/2,18, self.onSliderChange)
		slider:setValues(0.0, 1.0, 0.01, 0.01, true)
--		slider:setCurrentValue(getPlayer():getHumanVisual():getBlood(part))
		self.partToSlider[part] = slider
		y = ISDebugUtils.addHorzBar(self, math.max(y,y2)+barMod)+barMod
	end
	
	y = y+5
	y,obj = ISDebugUtils.addButton(self, "zeroAll", x, y, 200, 16, "ZERO ALL", self.onZeroAll)

	y = y+5
	y,obj = ISDebugUtils.addButton(self, "addRandom", x, y, 200, 16, "ADD RANDOM", self.onRandomBlood)
end

function ISDebugBlood:prerender()
	ISDebugSubPanelBase.prerender(self)
	if self.updateTime + 500 < getTimestampMs() then
		self:setSliderValues()
	end
end

function ISDebugBlood:setSliderValues()
	local playerObj = getSpecificPlayer(0)
	self.updateTime = getTimestampMs()
	for i=0,BloodBodyPartType.MAX:index()-1 do
		local part = BloodBodyPartType.FromIndex(i)
		local slider = self.partToSlider[part]
		local newValue = playerObj:getHumanVisual():getBlood(part)
		self.ignoreSlider = true
		slider:setCurrentValue(newValue)
		self.ignoreSlider = false
	end
end

function ISDebugBlood:onSliderChange(_newval, _slider)
	local playerObj = getSpecificPlayer(0)
	if self.ignoreSlider then return end
	local part = _slider.customData
	playerObj:getHumanVisual():setBlood(part, _newval)
	playerObj:resetModelNextFrame()
	triggerEvent("OnClothingUpdated", playerObj)
end

function ISDebugBlood:onZeroAll()
	local playerObj = getSpecificPlayer(0)
	playerObj:getHumanVisual():removeBlood()
	playerObj:resetModelNextFrame()
	triggerEvent("OnClothingUpdated", playerObj)
	self:setSliderValues()
end

function ISDebugBlood:onRandomBlood()
	local playerObj = getSpecificPlayer(0)
	playerObj:addBlood(null, false, true, false)
	triggerEvent("OnClothingUpdated", playerObj)
	self:setSliderValues()
end

function ISDebugBlood:new(x, y, width, height, doStencil)
	local o = ISDebugSubPanelBase:new(x, y, width, height, doStencil)
	setmetatable(o, self)
	self.__index = self
	o.updateTime = 0
	return o
end

