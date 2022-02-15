--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "DebugUIs/DebugChunkState/ISSectionedPanel"

local FONT_HGT_CONSOLE = getTextManager():getFontHeight(UIFont.DebugConsole)

---@class DebugChunkStateUI_SquarePropsHandler : ISPanel
DebugChunkStateUI_SquarePropsHandler = ISPanel:derive("DebugChunkStateUI_SquarePropsHandler")
local SquarePropsHandler = DebugChunkStateUI_SquarePropsHandler

function SquarePropsHandler:playerIndex()
	return self.gameState:fromLua0("getPlayerIndex")
end

function SquarePropsHandler:setSquare(square, x, y, z)
	self.square = square
	self.squareX = x
	self.squareY = y
	self.squareZ = z
	return false
end

function SquarePropsHandler:prerender()
	self.addLineX = 4
	self.addLineY = 0
end

function SquarePropsHandler:render()
	self:render1()
	self:postrender()
end

function SquarePropsHandler:render1()
end

function SquarePropsHandler:postrender()
	self:setHeight(self.addLineY)
	self.parent:calculateHeights()
end

function SquarePropsHandler:addLine(text, arg0, arg1, arg2, arg3, arg4)
	if type(arg0) == "boolean" then arg0 = tostring(arg0) end
	if type(arg1) == "boolean" then arg1 = tostring(arg1) end
	if type(arg2) == "boolean" then arg2 = tostring(arg2) end
	if type(arg3) == "boolean" then arg3 = tostring(arg3) end
	if type(arg4) == "boolean" then arg4 = tostring(arg4) end
	self:drawText(string.format(text, arg0, arg1, arg2, arg3, arg4), self.addLineX, self.addLineY, 1, 1, 1, 1, UIFont.DebugConsole)
	self.addLineY = self.addLineY + FONT_HGT_CONSOLE
end

function SquarePropsHandler:new(x, y, width, height, gameState)
	local o = ISPanel.new(self, x, y, width, height)
	o.gameState = gameState
	o.object = nil
	return o
end

-----

local SPH_misc = SquarePropsHandler:derive("DebugChunkStateUI_SPH_misc")

function SPH_misc:setSquare(square, x, y, z)
	self.square = square
	self.squareX = x
	self.squareY = y
	self.squareZ = z
	self.zones = getZones(x, y, z)
	self.vehicleZone = getVehicleZoneAt(x, y, z)
	return true
end

function SPH_misc:render1()
	local square = self.square
	local pn = self.gameState:fromLua0("getPlayerIndex")

	self:addLine("x,y,z = "..tostring(self.squareX)..", "..tostring(self.squareY)..", "..tostring(self.squareZ))
	self:addLine("cell =" .. math.floor(self.squareX/300) .. ", " .. math.floor(self.squareY/300));
	if self.zones then
		for i=1,self.zones:size() do
			local zone = self.zones:get(i-1)
			self:addLine("zone = %s / %s%s", zone:getName(), zone:getType(), (square ~= nil and zone == square:getZone()) and " *" or "")
		end
	else
		self:addLine("zone = nil")
	end
	if self.vehicleZone then
		local zone = self.vehicleZone
		self:addLine("vehicle zone = %s / %s", zone:getName(), zone:getType())
	end
--[[
	local zone = square and square:getZone() or nil
	if zone then
		self:addLine("zone = %s / %s", zone:getName(), zone:getType())
	else
		self:addLine("zone = nil")
	end
--]]

	if square then
		self:addLine("seen = %s  couldSee = %s  canSee = %s", square:getSeen(pn), square:isCouldSee(pn), square:getCanSee(pn))
		self:addLine("darkMulti = %.4f  targetDarkMulti = %.4f", square:getDarkMulti(pn), square:getTargetDarkMulti(pn))
		self:addLine("getVertLight() = %x, %x, %x, %x" , square:getVertLight(0, pn), square:getVertLight(1, pn), square:getVertLight(2, pn), square:getVertLight(3, pn))
--		self:addLine("cutaway = " .. tostring(square:getPlayerCutawayFlags(pn))
		self:addLine("puddles = %.4f", square:getPuddlesInGround(), 4)
		if square:getRoom() then
			local roomDef = square:getRoom():getRoomDef()
			self:addLine("buildingID = %d", square:getBuilding():getID())
			self:addLine("roomID = %d", square:getRoomID())
			self:addLine("roomDef.name = %s", roomDef:getName())
			self:addLine("bExplored = %s", roomDef:isExplored())
			self:addLine("alarm = %s", square:getBuilding():getDef():isAlarmed())
			local switches = square:getRoom():getLightSwitches()
			local switchCount = switches:size()
			self:addLine("#switches = %d", switchCount)
		else
			local roomDef = getWorld():getMetaGrid():getRoomAt(square:getX(), square:getY(), square:getZ())
			if roomDef then
				self:addLine("roomDef.ID = %d", roomDef:getID())
				self:addLine("roomDef.name = %s", roomDef:getName())
			else
				self:addLine("roomID = %d  room = nil", square:getRoomID())
			end
		end
		local building = square:getRoofHideBuilding()
		if building then
			self:addLine("roof-hide building = %d", building:getDef():getID())
		end
		local light = getCell():getLightSourceAt(self.squareX, self.squareY, self.squareZ)
		if light then
			local building = light:getLocalToBuilding()
			self:addLine("light  active = %s  hydro = %s  localToBuilding = %d", light:isActive(), light:isHydroPowered(), building and building:getID() or -1)
		end
		self:addLine("exterior = %s", square:Is(IsoFlagType.exterior))
		self:addLine("haveElectricity = %s", square:haveElectricity())
		self:addLine("TreatAsSolidFloor = %s", square:TreatAsSolidFloor())
		self:addLine("solid = %s", square:Is(IsoFlagType.solid))
		self:addLine("solidTrans = %s", square:Is(IsoFlagType.solidtrans))
		self:addLine("burning = %s", square:Is(IsoFlagType.burning))
		self:addLine("burntOut = %s", square:Is(IsoFlagType.burntOut))
		self:addLine("HasRaindrop = %s", square:Is(IsoFlagType.HasRaindrop))
	end

	self:setHeight(self.addLineY)
end

-----

local SPH_modData = SquarePropsHandler:derive("DebugChunkStateUI_SPH_modData")

function SPH_modData:setSquare(square, x, y, z)
	self.square = square
	self.squareX = x
	self.squareY = y
	self.squareZ = z
	return square ~= nil and square:hasModData() -- and not isEmpty
end

function SPH_modData:render1()
	local square = self.square
	for k,v in pairs(square:getModData()) do
		self:addLine("%s = %s", k, v)
	end
end

-----

local SPH_properties = SquarePropsHandler:derive("DebugChunkStateUI_SPH_properties")

function SPH_properties:setSquare(square, x, y, z)
	self.square = square
	self.squareX = x
	self.squareY = y
	self.squareZ = z
	if square == nil then
		return false
	end
	local props = square:getProperties()
	return (props ~= nil) and (not props:getPropertyNames():isEmpty() or not props:getFlagsList():isEmpty())
end

function SPH_properties:render1()
	local square = self.square
	local props = square:getProperties()
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

local SPH_matrix = SquarePropsHandler:derive("DebugChunkStateUI_SPH_matrix")

function SPH_matrix:setSquare(square, x, y, z)
	self.square = square
	self.squareX = x
	self.squareY = y
	self.squareZ = z
	return self.square ~= nil
end

function SPH_matrix:render1()
	local square = self.square
	local pn = self.gameState:fromLua0("getPlayerIndex")

	self.font = UIFont.DebugConsole
	self.fontHgt = FONT_HGT_CONSOLE

	local side = 20
	self:drawText("nav[]", self.addLineX, self.addLineY, 1, 1, 1, 1, self.font)
	self:drawText("reachable", self.addLineX + side * 3 + 30, self.addLineY, 1, 1, 1, 1, self.font)
	self.addLineY = self.addLineY + self.fontHgt
	self:draw9x9(self.addLineX, self.addLineY,
		square:getAdjacentSquare(IsoDirections.NW), square:getAdjacentSquare(IsoDirections.N), square:getAdjacentSquare(IsoDirections.NE),
		square:getAdjacentSquare(IsoDirections.W), false, square:getAdjacentSquare(IsoDirections.E),
		square:getAdjacentSquare(IsoDirections.SW), square:getAdjacentSquare(IsoDirections.S), square:getAdjacentSquare(IsoDirections.SE))

	self:draw9x9(self.addLineX + side * 3 + 30, self.addLineY,
		square:getAdjacentPathSquare(IsoDirections.NW), square:getAdjacentPathSquare(IsoDirections.N), square:getAdjacentPathSquare(IsoDirections.NE),
		square:getAdjacentPathSquare(IsoDirections.W), false, square:getAdjacentPathSquare(IsoDirections.E),
		square:getAdjacentPathSquare(IsoDirections.SW), square:getAdjacentPathSquare(IsoDirections.S), square:getAdjacentPathSquare(IsoDirections.SE))
	self.addLineY = self.addLineY + side * 3

	self:drawText("collide", self.addLineX, self.addLineY, 1, 1, 1, 1, self.font)
	self:drawText("collide (below)", self.addLineX + side * 3 + 30, self.addLineY, 1, 1, 1, 1, self.font)
	self:drawText("collide (above)", self.addLineX + side * 3 + 30 + side * 3 + 30, self.addLineY, 1, 1, 1, 1, self.font)
	self.addLineY = self.addLineY + self.fontHgt
	self:draw9x9(self.addLineX, self.addLineY,
		square:getCollideMatrix(-1, -1, 0), square:getCollideMatrix(0, -1, 0), square:getCollideMatrix(1, -1, 0),
		square:getCollideMatrix(-1, 0, 0), square:getCollideMatrix(0, 0, 0), square:getCollideMatrix(1, 0, 0),
		square:getCollideMatrix(-1, 1, 0), square:getCollideMatrix(0, 1, 0), square:getCollideMatrix(1, 1, 0))
	self:draw9x9(self.addLineX + side * 3 + 30, self.addLineY,
		square:getCollideMatrix(-1, -1, -1), square:getCollideMatrix(0, -1, -1), square:getCollideMatrix(1, -1, -1),
		square:getCollideMatrix(-1, 0, -1), square:getCollideMatrix(0, 0, -1), square:getCollideMatrix(1, 0, -1),
		square:getCollideMatrix(-1, 1, -1), square:getCollideMatrix(0, 1, -1), square:getCollideMatrix(1, 1, -1))
	self:draw9x9(self.addLineX + side * 3 + 30 + side * 3 + 30, self.addLineY,
		square:getCollideMatrix(-1, -1, 1), square:getCollideMatrix(0, -1, 1), square:getCollideMatrix(1, -1, 1),
		square:getCollideMatrix(-1, 0, 1), square:getCollideMatrix(0, 0, 1), square:getCollideMatrix(1, 0, 1),
		square:getCollideMatrix(-1, 1, 1), square:getCollideMatrix(0, 1, 1), square:getCollideMatrix(1, 1, 1))
	self.addLineY = self.addLineY + side * 3

	self:drawText("path", self.addLineX, self.addLineY, 1, 1, 1, 1, self.font)
	self:drawText("path (below)", self.addLineX + side * 3 + 30, self.addLineY, 1, 1, 1, 1, self.font)
	self:drawText("path (above)", self.addLineX + side * 3 + 30 + side * 3 + 30, self.addLineY, 1, 1, 1, 1, self.font)
	self.addLineY = self.addLineY + self.fontHgt
	self:draw9x9(self.addLineX, self.addLineY,
		not square:getPathMatrix(-1, -1, 0), not square:getPathMatrix(0, -1, 0), not square:getPathMatrix(1, -1, 0),
		not square:getPathMatrix(-1, 0, 0), not square:getPathMatrix(0, 0, 0), not square:getPathMatrix(1, 0, 0),
		not square:getPathMatrix(-1, 1, 0), not square:getPathMatrix(0, 1, 0), not square:getPathMatrix(1, 1, 0))
	self:draw9x9(self.addLineX + side * 3 + 30, self.addLineY,
		not square:getPathMatrix(-1, -1, -1), not square:getPathMatrix(0, -1, -1), not square:getPathMatrix(1, -1, -1),
		not square:getPathMatrix(-1, 0, -1), not square:getPathMatrix(0, 0, -1), not square:getPathMatrix(1, 0, -1),
		not square:getPathMatrix(-1, 1, -1), not square:getPathMatrix(0, 1, -1), not square:getPathMatrix(1, 1, -1))
	self:draw9x9(self.addLineX + side * 3 + 30 + side * 3 + 30, self.addLineY,
		not square:getPathMatrix(-1, -1, 1), not square:getPathMatrix(0, -1, 1), not square:getPathMatrix(1, -1, 1),
		not square:getPathMatrix(-1, 0, 1), not square:getPathMatrix(0, 0, 1), not square:getPathMatrix(1, 0, 1),
		not square:getPathMatrix(-1, 1, 1), not square:getPathMatrix(0, 1, 1), not square:getPathMatrix(1, 1, 1))
	self.addLineY = self.addLineY + side * 3

	self:drawText("vision", self.addLineX, self.addLineY, 1, 1, 1, 1, self.font)
	self:drawText("vision (below)", self.addLineX + side * 3 + 30, self.addLineY, 1, 1, 1, 1, self.font)
	self:drawText("vision (above)", self.addLineX + side * 3 + 30 + side * 3 + 30, self.addLineY, 1, 1, 1, 1, self.font)
	self.addLineY = self.addLineY + self.fontHgt
	self:draw9x9(self.addLineX, self.addLineY,
		not square:getVisionMatrix(-1, -1, 0), not square:getVisionMatrix(0, -1, 0), not square:getVisionMatrix(1, -1, 0),
		not square:getVisionMatrix(-1, 0, 0), not square:getVisionMatrix(0, 0, 0), not square:getVisionMatrix(1, 0, 0),
		not square:getVisionMatrix(-1, 1, 0), not square:getVisionMatrix(0, 1, 0), not square:getVisionMatrix(1, 1, 0))
	self:draw9x9(self.addLineX + side * 3 + 30, self.addLineY,
		not square:getVisionMatrix(-1, -1, -1), not square:getVisionMatrix(0, -1, -1), not square:getVisionMatrix(1, -1, -1),
		not square:getVisionMatrix(-1, 0, -1), not square:getVisionMatrix(0, 0, -1), not square:getVisionMatrix(1, 0, -1),
		not square:getVisionMatrix(-1, 1, -1), not square:getVisionMatrix(0, 1, -1), not square:getVisionMatrix(1, 1, -1))
	self:draw9x9(self.addLineX + side * 3 + 30 + side * 3 + 30, self.addLineY,
		not square:getVisionMatrix(-1, -1, 1), not square:getVisionMatrix(0, -1, 1), not square:getVisionMatrix(1, -1, 1),
		not square:getVisionMatrix(-1, 0, 1), not square:getVisionMatrix(0, 0, 1), not square:getVisionMatrix(1, 0, 1),
		not square:getVisionMatrix(-1, 1, 1), not square:getVisionMatrix(0, 1, 1), not square:getVisionMatrix(1, 1, 1))
	self.addLineY = self.addLineY + side * 3

	self:setHeight(self.addLineY)
end

function SPH_matrix:draw9x9(x, y, nw, n, ne, w, at, e, sw, s, se)
	local side = 20
	self:drawRect(x, y, side * 3, side * 3, 1.0, 0.5, 0.5, 0.5)
	self:draw9x9aux(x, y, -1, -1, nw)
	self:draw9x9aux(x, y, 0, -1, n)
	self:draw9x9aux(x, y, 1, -1, ne)
	self:draw9x9aux(x, y, -1, 0, w)
	self:draw9x9aux(x, y, 0, 0, at)
	self:draw9x9aux(x, y, 1, 0, e)
	self:draw9x9aux(x, y, -1, 1, sw)
	self:draw9x9aux(x, y, 0, 1, s)
	self:draw9x9aux(x, y, 1, 1, se)
end

function SPH_matrix:draw9x9aux(x, y, dx, dy, isSet)
	if not isSet then return end
	local side = 20
	local a,r,g,b = 1.0, 0.5, 0.5, 0.5
	if isSet then r = 0.0; b = 0.0 end
	self:drawRect(x + (dx + 1) * side, y + (dy + 1) * side, side, side, a, r, g, b)
end

-----

DebugChunkStateUI_SquarePanel = ISSectionedPanel:derive("DebugChunkStateUI_SquarePanel")
local SquarePanel = DebugChunkStateUI_SquarePanel

function SquarePanel:createSections()
	self:addSection(SPH_misc:new(0, 0, self.width, 50, self.debugChunkState.gameState), "details")
	self:addSection(SPH_properties:new(0, 0, self.width, 50, self.debugChunkState.gameState), "properties")
	self:addSection(SPH_modData:new(0, 0, self.width, 50, self.debugChunkState.gameState), "modData")
	self:addSection(SPH_matrix:new(0, 0, self.width, 50, self.debugChunkState.gameState), "collide / path / vision")
	for _,section in ipairs(self.sections) do
		section.enabled = false
	end
end

function SquarePanel:setSquare(square, x, y, z)
	self.square = square
	self.squareX = math.floor(x)
	self.squareY = math.floor(y)
	self.squareZ = z

	for _,section in ipairs(self.sections) do
		section.enabled = section.panel:setSquare(square, self.squareX, self.squareY, self.squareZ)
	end
end

function SquarePanel:new(x, y, width, height, debugChunkState)
	local o = ISSectionedPanel.new(self, x, y, width, height)
	o.backgroundColor.a = 0.8
	o.debugChunkState = debugChunkState
	o.font = UIFont.DebugConsole
	o.fontHgt = getTextManager():getFontHeight(o.font)
	return o
end
