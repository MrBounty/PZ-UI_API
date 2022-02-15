--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

ISRemovePlantCursor = ISBuildingObject:derive("ISRemovePlantCursor")

local function predicateCutPlant(item)
    return not item:isBroken() and item:hasTag("CutPlant")
end

function ISRemovePlantCursor:create(x, y, z, north, sprite)
	local square = getWorld():getCell():getGridSquare(x, y, z)
	if self.removeType == "bush" then
		ISWorldObjectContextMenu.doRemovePlant(self.character, square, false)
	elseif self.removeType == "grass" then
		ISWorldObjectContextMenu.doRemoveGrass(self.character, square)
	elseif self.removeType == "wallVine" then
		ISWorldObjectContextMenu.doRemovePlant(self.character, square, true)
	end
end

function ISRemovePlantCursor:isValid(square)
	if self.removeType == "bush" or self.removeType == "wallVine" then
		local playerInv = self.character:getInventory()
		local hasCuttingTool = playerInv:containsEvalRecurse(predicateCutPlant)
		if not hasCuttingTool then
			return false
		end
	end
	return self:getRemovableObject(square) ~= nil
end

function ISRemovePlantCursor:render(x, y, z, square)
	if not ISRemovePlantCursor.floorSprite then
		ISRemovePlantCursor.floorSprite = IsoSprite.new()
		ISRemovePlantCursor.floorSprite:LoadFramesNoDirPageSimple('media/ui/FloorTileCursor.png')
	end
	local r,g,b,a = 0.0,1.0,0.0,0.8
	if self:isValid(square) then
		self:getRemovableObject(square):setHighlighted(true)
		self:getRemovableObject(square):setHighlightColor(0.0, 1.0, 0.0, 1.0)
	else
		r = 1.0
		g = 0.0
	end
	ISRemovePlantCursor.floorSprite:RenderGhostTileColor(x, y, z, r, g, b, a)
end

function ISRemovePlantCursor:getRemovableObject(square)
	for i=1,square:getObjects():size() do
		local o = square:getObjects():get(i-1)
		if self.removeType == "bush" then
			if o:getSprite() and o:getSprite():getProperties() and o:getSprite():getProperties():Is(IsoFlagType.canBeCut) then
				return o
			end
		elseif self.removeType == "grass" then
			if o:getSprite() and o:getSprite():getProperties() and o:getSprite():getProperties():Is(IsoFlagType.canBeRemoved) then
				return o
			end
		elseif self.removeType == "wallVine" then
			local attached = o:getAttachedAnimSprite()
			if attached then
				for n=1,attached:size() do
					local sprite = attached:get(n-1)
					if sprite and sprite:getParentSprite() and sprite:getParentSprite():getName() and
						luautils.stringStarts(sprite:getParentSprite():getName(), "f_wallvines_") then
						return o
					end
				end
			end
		end
	end
	return nil
end

function ISRemovePlantCursor:getAPrompt()
	if self.removeType == "bush" then return getText("ContextMenu_RemoveBush") end
	if self.removeType == "grass" then return getText("ContextMenu_RemoveGrass") end
	if self.removeType == "wallVine" then return getText("ContextMenu_RemoveWallVine") end
	return nil
end

function ISRemovePlantCursor:getLBPrompt()
	return nil
end

function ISRemovePlantCursor:getRBPrompt()
	return nil
end

function ISRemovePlantCursor:new(character, removeType)
	local o = ISBuildingObject.new(self)
	o:init()
	o.character = character
	o.player = character:getPlayerNum()
	o.noNeedHammer = true
	o.skipBuildAction = true
	o.isYButtonResetCursor = true
	o.removeType = removeType
	return o
end


