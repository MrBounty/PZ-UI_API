--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISScrollingListBox"
require "DebugUIs/DebugChunkState/ISSectionedPanel"

local FONT_HGT_CONSOLE = getTextManager():getFontHeight(UIFont.DebugConsole)

-----

DebugGlobalObjectState_BasePropertiesPanel = ISPanel:derive("DebugGlobalObjectState_BasePropertiesPanel")
local BasePropertiesPanel = DebugGlobalObjectState_BasePropertiesPanel

function BasePropertiesPanel:prerender()
	self.addLineX = 4
	self.addLineY = 0
end

function BasePropertiesPanel:render()
	local stencilX = 0
	local stencilY = 0
	local stencilX2 = self.width
	local stencilY2 = self.height

	if self:isVScrollBarVisible() then
		stencilX2 = self.vscroll.x + 3 -- +3 because the scrollbar texture is narrower than the scrollbar width
	end

	if self.parent and self.parent:getScrollChildren() then
		stencilX = self.javaObject:clampToParentX(self:getAbsoluteX() + stencilX) - self:getAbsoluteX()
		stencilX2 = self.javaObject:clampToParentX(self:getAbsoluteX() + stencilX2) - self:getAbsoluteX()
		stencilY = self.javaObject:clampToParentY(self:getAbsoluteY() + stencilY) - self:getAbsoluteY()
		stencilY2 = self.javaObject:clampToParentY(self:getAbsoluteY() + stencilY2) - self:getAbsoluteY()
	end

	self:setStencilRect(stencilX, stencilY, stencilX2 - stencilX, stencilY2 - stencilY)

	self:render1()
	self:postrender()

	self:repaintStencilRect(stencilX, stencilY, stencilX2 - stencilX, stencilY2 - stencilY)
end

function BasePropertiesPanel:postrender()
	self:setHeight(math.min(self.addLineY + 20, 300))
	self:setScrollHeight(self.addLineY + 20)
	self.parent:calculateHeights()

	self:clearStencilRect()
end

function BasePropertiesPanel:onMouseWheel(del)
	self:setYScroll(self:getYScroll() - (del * FONT_HGT_CONSOLE * 2))
    return true
end

function BasePropertiesPanel:addLine(text, arg0, arg1, arg2, arg3, arg4)
	if type(arg0) == "boolean" or type(arg0) == "table" or type(arg0) == "userdata" then arg0 = tostring(arg0) end
	if type(arg1) == "boolean" or type(arg1) == "table" or type(arg1) == "userdata" then arg1 = tostring(arg1) end
	if type(arg2) == "boolean" or type(arg2) == "table" or type(arg2) == "userdata" then arg2 = tostring(arg2) end
	if type(arg3) == "boolean" or type(arg3) == "table" or type(arg3) == "userdata" then arg3 = tostring(arg3) end
	if type(arg4) == "boolean" or type(arg4) == "table" or type(arg4) == "userdata" then arg4 = tostring(arg4) end
	self:drawText(string.format(text, arg0, arg1, arg2, arg3, arg4), self.addLineX, self.addLineY, 1, 1, 1, 1, UIFont.DebugConsole)
	self.addLineY = self.addLineY + FONT_HGT_CONSOLE
end

function BasePropertiesPanel:renderKeyValue(k, v)
	self:addLine("%s = %s", k, v)
	if type(v) == "table" then
		self.addLineX = self.addLineX + 10
		for k2,v2 in pairs(v) do
			self:renderKeyValue(k2, v2)
		end
		self.addLineX = self.addLineX - 10
	end
end

function BasePropertiesPanel:new(x, y, w, h)
	local o = ISPanel.new(self, x, y, w, h)
	o:addScrollBars()
	return o
end

-----

DebugGlobalObjectState_SystemPanel = BasePropertiesPanel:derive("DebugGlobalObjectState_SystemPanel")
local SystemPanel = DebugGlobalObjectState_SystemPanel

function SystemPanel:render1()
	if not self.system then return end
--	self:addLine("name = %s", self.system:getName())
	local modData = self.system:getModData()
	for k,v in pairs(modData) do
		self:renderKeyValue(k, v)
	end
end

function SystemPanel:setSystem(system)
	self.system = system
end

function SystemPanel:new(x, y, w, h)
	local o = BasePropertiesPanel.new(self, x, y, w, h)
	return o
end

-----

DebugGlobalObjectState_GlobalObjectPanel = BasePropertiesPanel:derive("DebugGlobalObjectState_GlobalObjectPanel")
local GlobalObjectPanel = DebugGlobalObjectState_GlobalObjectPanel

function GlobalObjectPanel:render1()
	if not self.system then return end
	local globalObject = self.system:getObjectAt(self.objectPos.x, self.objectPos.y, self.objectPos.z)
	if not globalObject then return end
	local modData = globalObject:getModData()
	for k,v in pairs(modData) do
		if k == "luaSystem" then
			self:addLine("%s = %s", k, v)
		else
			self:renderKeyValue(k, v)
		end
	end
end

function GlobalObjectPanel:setObject(globalObject)
	if globalObject then
		self.system = globalObject:getSystem()
		self.objectPos = { x = globalObject:getX(), y = globalObject:getY(), z = globalObject:getZ() }
	else
		self.system = nil
		self.objectPos = nil
	end
end

-----

DebugGlobalObjectState_IsoObjectPanel = BasePropertiesPanel:derive("DebugGlobalObjectState_IsoObjectPanel")
local IsoObjectPanel = DebugGlobalObjectState_IsoObjectPanel

function IsoObjectPanel:render1()
	if not self.system then return end
	local isoObject = self.system:getModData():getIsoObjectAt(self.objectPos.x, self.objectPos.y, self.objectPos.z)
	if not isoObject then return end
	local modData = isoObject:getModData()
	for k,v in pairs(modData) do
		self:renderKeyValue(k, v)
	end
end

function IsoObjectPanel:setObject(globalObject)
	if globalObject then
		self.system = globalObject:getSystem()
		self.objectPos = { x = globalObject:getX(), y = globalObject:getY(), z = globalObject:getZ() }
	else
		self.system = nil
		self.objectPos = nil
	end
end

-----

local OPH = {}

local function deriveOPH(name)
	local luaClass = "DebugGlobalObjectState_" .. name
	_G[luaClass] = BasePropertiesPanel:derive(luaClass)
	local oph = _G[luaClass]
	oph.className = name
	oph.setObject = function(self, globalObject)
		if globalObject then
			self.system = globalObject:getSystem()
			local x,y,z = globalObject:getX(), globalObject:getY(), globalObject:getZ()
			self.objectPos = { x = x, y = y, z = z }
			local isoObject = self.system:getModData():getIsoObjectAt(x, y, z)
			return instanceof(isoObject, self.className)
		end
		self.system = nil
		self.objectPos = nil
		return false
	end
	table.insert(OPH, oph)
	return oph
end

local OPH_IsoGenerator = deriveOPH("IsoGenerator")
function OPH_IsoGenerator:render1()
	if not self.system then return end
	local isoObject = self.system:getModData():getIsoObjectAt(self.objectPos.x, self.objectPos.y, self.objectPos.z)
	if not isoObject then return end
	self:addLine("Activated = %s", isoObject:isActivated())
	self:addLine("Condition = %d", isoObject:getCondition())
	self:addLine("Fuel = %.2f", isoObject:getFuel())

	self:addLine("connected locations: %d", isoObject:getConnectedLocationCount())
	self.addLineX = self.addLineX + 10
	for i=1,isoObject:getConnectedLocationCount() do
		local pos = isoObject:getConnectedLocation(i-1)
		self:addLine("x,y,z=%d,%d,%d", pos:getX(), pos:getY(), pos:getZ())
	end
	self.addLineX = self.addLineX - 10

	self:addLine("connected building:")
	self.addLineX = self.addLineX + 10
	local cb = isoObject:getConnectedBuilding()
	self:addLine("cellX=%d, cellY=%d, buildingID=%d", cb:getCellX(), cb:getCellY(), cb:getBuildingID())
	self.addLineX = self.addLineX - 10
end

-----

---@class DebugGlobalObjectState_PropertiesPanel : ISSectionedPanel
DebugGlobalObjectState_PropertiesPanel = ISSectionedPanel:derive("DebugGlobalObjectState_PropertiesPanel")
local PropertiesPanel = DebugGlobalObjectState_PropertiesPanel

function PropertiesPanel:createChildren()
	self:addSection(SystemPanel:new(0, 0, self.width, 200, self.state), "System ModData")
	self:addSection(GlobalObjectPanel:new(0, 0, self.width, 200, self.state), "GlobalObject ModData")
	self:addSection(IsoObjectPanel:new(0, 0, self.width, 200, self.state), "IsoObject ModData")
	for _,oph in ipairs(OPH) do
		self:addSection(oph:new(0, 0, self.width, 50), oph.className)
	end
	self:setScrollChildren(true)
end

function PropertiesPanel:setSystem(system)
	if system == self.system then return end
	self.system = system
	self.sections[1].panel:setSystem(system)
end

function PropertiesPanel:setObject(globalObject)
	if globalObject == self.object then return end
	self.object = globalObject
	self.sections[2].panel:setObject(globalObject)
	self.sections[3].panel:setObject(globalObject)
	for i=4,#self.sections do
		local section = self.sections[i]
		section.enabled = section.panel:setObject(globalObject)
	end
end

function PropertiesPanel:new(x, y, width, height, state)
	local o = ISSectionedPanel.new(self, x, y, width, height)
	o.state = state
	o:addScrollBars()
	return o
end

