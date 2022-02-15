--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISScrollingListBox"
require "DebugUIs/DebugChunkState/ISSectionedPanel"

local FONT_HGT_CONSOLE = getTextManager():getFontHeight(UIFont.DebugConsole)

---@class DebugChunkStateUI_ObjPropsHandler : ISPanel
DebugChunkStateUI_ObjPropsHandler = ISPanel:derive("DebugChunkStateUI_ObjPropsHandler")
local ObjPropsHandler = DebugChunkStateUI_ObjPropsHandler

function ObjPropsHandler:playerIndex()
	return self.gameState:fromLua0("getPlayerIndex")
end

function ObjPropsHandler:setObject(object)
	self.object = object
	return false
end

function ObjPropsHandler:prerender()
	self.addLineX = 4
	self.addLineY = 0
end

function ObjPropsHandler:render()
	self:render1()
	self:postrender()
end

function ObjPropsHandler:render1()
end

function ObjPropsHandler:postrender()
	self:setHeight(self.addLineY)
	self.parent:calculateHeights()
end

function ObjPropsHandler:addLine(text, arg0, arg1, arg2, arg3, arg4)
	if type(arg0) == "boolean" or type(arg0) == "table" then arg0 = tostring(arg0) end
	if type(arg1) == "boolean" or type(arg1) == "table" then arg1 = tostring(arg1) end
	if type(arg2) == "boolean" or type(arg2) == "table" then arg2 = tostring(arg2) end
	if type(arg3) == "boolean" or type(arg3) == "table" then arg3 = tostring(arg3) end
	if type(arg4) == "boolean" or type(arg4) == "table" then arg4 = tostring(arg4) end
	self:drawText(string.format(text, arg0, arg1, arg2, arg3, arg4), self.addLineX, self.addLineY, 1, 1, 1, 1, UIFont.DebugConsole)
	self.addLineY = self.addLineY + FONT_HGT_CONSOLE
end

function ObjPropsHandler:new(x, y, width, height, gameState)
	local o = ISPanel.new(self, x, y, width, height)
	o.gameState = gameState
	o.object = nil
	return o
end

-----

local OPH_render = ObjPropsHandler:derive("DebugChunkStateUI_OPH_render")
function OPH_render:setObject(object)
	self.object = object
	return true
end
function OPH_render:render1()
	local obj = self.object
	local pn = self:playerIndex()
	if obj:getSprite() then
		self:addLine("sprite.name = %s", obj:getSprite():getName() or "")
		self:addLine("sprite.type = %s", obj:getSprite():getType():toString())
		self:addLine("sprite.ID = %d", obj:getSprite():getID())
	end
	self:addLine("spriteName = %s", obj:getSpriteName() or "")
	self:addLine("alpha = %.3f", obj:getAlpha(pn))
	self:addLine("targetAlpha = %.3f", obj:getTargetAlpha(pn))
	self:addLine("renderYOffset = %d", obj:getRenderYOffset())
end

local OPH_overlaySprite = ObjPropsHandler:derive("DebugChunkStateUI_OPH_overlaySprite")
function OPH_overlaySprite:setObject(object)
	self.object = object
	return object:getOverlaySprite() ~= nil
end
function OPH_overlaySprite:render1()
	local obj = self.object
	if obj:getOverlaySprite() then
		self:addLine("sprite.name = %s", obj:getOverlaySprite():getName() or "")
		self:addLine("sprite.type = %s", obj:getOverlaySprite():getType():toString())
		self:addLine("sprite.ID = %d", obj:getOverlaySprite():getID())
		local color = obj:getOverlaySpriteColor()
		if color then
			self:addLine("rgba = %.3f,%.3f,%.3f,%.3f", color:getR(), color:getG(), color:getB(), color:getA())
		end
	end
end

local OPH_AttachedAnimSprite = ObjPropsHandler:derive("DebugChunkStateUI_OPH_AttachedAnimSprite")
function OPH_AttachedAnimSprite:setObject(object)
	self.object = object
	return object:getAttachedAnimSprite() ~= nil and not object:getAttachedAnimSprite():isEmpty()
end
function OPH_AttachedAnimSprite:render1()
	local obj = self.object
	if obj:getAttachedAnimSprite() then
		local sprites = obj:getAttachedAnimSprite()
		for i=1,sprites:size() do
			local inst = sprites:get(i-1)
			local sprite = inst:getParentSprite()
			self:addLine("attached[%d]", i - 1)
			self:addLine("    name = %s", sprite:getName())
			self:addLine("    type = %s", sprite:getType():toString())
			self:addLine("    ID = %d", sprite:getID())
			self:addLine("    tint = %.3f,%.3f,%.3f", inst:getTintR(), inst:getTintG(), inst:getTintB())
			self:addLine("    alpha = %.3f", inst:getAlpha())
			self:addLine("    bCopyTargetAlpha = %s", inst:isCopyTargetAlpha())
			self:addLine("    bMultiplyObjectAlpha = %s", inst:isMultiplyObjectAlpha())
		end
	end
end

local OPH_ModData = ObjPropsHandler:derive("DebugChunkStateUI_OPH_ModData")
function OPH_ModData:setObject(object)
	self.object = object
	return object:hasModData() --and not object:getModData():isEmpty()
end
function OPH_ModData:render1()
	local obj = self.object
	if not obj:hasModData() then
		return
	end
	for k,v in pairs(obj:getModData()) do
		self:addLine("%s = %s", k, v)
	end
end

local OPH_SpriteProperties = ObjPropsHandler:derive("DebugChunkStateUI_OPH_SpriteProperties")

function OPH_SpriteProperties:setObject(object)
	self.object = object
	if object:getSprite() == nil then
		return false
	end
	local props = object:getSprite():getProperties()
	return (props ~= nil) and (not props:getPropertyNames():isEmpty() or not props:getFlagsList():isEmpty())
end

function OPH_SpriteProperties:render1()
	local obj = self.object
	local props = obj:getSprite():getProperties()
	local names = props:getPropertyNames()
	for i=1,names:size() do
		self:addLine("%s = %s", names:get(i-1), props:Val(names:get(i-1)))
	end
	if not names:isEmpty() then
		self.addLineY = self.addLineY + FONT_HGT_CONSOLE
	end
	local flags = props:getFlagsList()
	for i=1,flags:size() do
		self:addLine("flag: %s", flags:get(i-1):toString())
	end
end

-----

local OPH = {}
local function deriveOPH(name)
	local oph = ObjPropsHandler:derive("DebugChunkStateUI_" .. name)
	oph.className = name
	oph.setObject = function(self, object)
		self.object = object
		return instanceof(object, self.className)
	end
	table.insert(OPH, oph)
	return oph
end

local OPH_BaseVehicle = deriveOPH("BaseVehicle")
function OPH_BaseVehicle:render1()
	local obj = self.object
	if obj:getScript() then
		self:addLine("script = %s", obj:getScript():getFullName())
	end
end

local OPH_IsoFire = deriveOPH("IsoFire")
function OPH_IsoFire:render1()
	local obj = self.object
	self:addLine("Energy = %d", obj:getEnergy())
end

local OPH_IsoPlayer = deriveOPH("IsoPlayer")
function OPH_IsoPlayer:render1()
	local obj = self.object
	self:addLine("state = %s", obj:getCurrentState() and obj:getCurrentState():getClass():getSimpleName() or "none")
end

local OPH_IsoThumpable = deriveOPH("IsoThumpable")
function OPH_IsoThumpable:render1()
	local obj = self.object
	self:addLine("BreakSound = %s", obj:getBreakSound())
	self:addLine("ThumpSound = %s", obj:getThumpSound())
end

local OPH_IsoWindow = deriveOPH("IsoWindow")
function OPH_IsoWindow:render1()
	local obj = self.object
	self:addLine("canAddSheetRope = %s", obj:canAddSheetRope())
	self:addLine("PermaLocked = %s", obj:isPermaLocked())
end

local OPH_IsoZombie = deriveOPH("IsoZombie")
function OPH_IsoZombie:render1()
	local obj = self.object
	if obj:getCurrentState() == nil then
		self:addLine("state = none")
	elseif instanceof(obj:getCurrentState(), "State") then
		self:addLine("state = %s", obj:getCurrentState():getClass():getSimpleName())
	else
		-- setExposed() not called for this state
		self:addLine("state = %s", tostring(obj:getCurrentState()))
	end
	self:addLine("dir = %s", obj:getDir():toString())
end

-----

DebugChunkStateUI_ObjPropsPanel = ISSectionedPanel:derive("DebugChunkStateUI_ObjPropsPanel")
local ObjPropsPanel = DebugChunkStateUI_ObjPropsPanel

function ObjPropsPanel:createChildren()
	for _,oph in ipairs(OPH) do
		self:addSection(oph:new(0, 0, self.width, 50, self.debugChunkState.gameState), oph.className)
	end
	self:addSection(OPH_render:new(0, 0, self.width, 50, self.debugChunkState.gameState), "rendering")
	self:addSection(OPH_overlaySprite:new(0, 0, self.width, 50, self.debugChunkState.gameState), "overlaySprite")
	self:addSection(OPH_AttachedAnimSprite:new(0, 0, self.width, 50, self.debugChunkState.gameState), "AttachedAnimSprite")
	self:addSection(OPH_ModData:new(0, 0, self.width, 50, self.debugChunkState.gameState), "modData")
	self:addSection(OPH_SpriteProperties:new(0, 0, self.width, 50, self.debugChunkState.gameState), "sprite.properties")
	for _,section in ipairs(self.sections) do
		section.enabled = false
	end
end

function ObjPropsPanel:setObject(object)
	if object == self.object then return end
	self.object = object
	self:clear()
	if not self.object then return end

	for _,section in ipairs(self.sections) do
		section.enabled = section.panel:setObject(object)
	end
end

function ObjPropsPanel:new(x, y, width, height, debugChunkState)
	local o = ISSectionedPanel.new(self, x, y, width, height)
	o.debugChunkState = debugChunkState
	return o
end

