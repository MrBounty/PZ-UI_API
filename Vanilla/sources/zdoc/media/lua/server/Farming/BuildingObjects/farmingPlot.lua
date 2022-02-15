require "BuildingObjects/ISBuildingObject"

--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

---@class farmingPlot : ISBuildingObject
farmingPlot = ISBuildingObject:derive("farmingPlot");

--************************************************************************--
--** farmingPlot:new
--**
--************************************************************************--
function farmingPlot:create(x, y, z, north, sprite)
	-- we set up the choosen square for our plot
	local sq = getWorld():getCell():getGridSquare(x, y, z);
	if self.handItem then
		ISInventoryPaneContextMenu.equipWeapon(self.handItem, true, self.handItem:isTwoHandWeapon(), self.character:getPlayerNum())
	end
	ISTimedActionQueue.add(ISPlowAction:new(self.character, sq, self.handItem, 110));
end

function farmingPlot:new(handItem, character)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o:init()
	o:setNorthSprite("vegetation_farming_01_1");
	o:setSprite("vegetation_farming_01_1");
	o.handItem = handItem;
	o.skipBuildAction = true
    o.character = character;
    o.noNeedHammer = true;
	return o;
end

function farmingPlot:isValid(square)
	if not self.handItem then
		if self.character:getBodyDamage():getBodyPart(BodyPartType.Hand_L):HasInjury() or self.character:getBodyDamage():getBodyPart(BodyPartType.Hand_R):HasInjury() then
			return false;
		end
    end
	if CFarmingSystem.instance:getLuaObjectOnSquare(square) then
		return false
	end
	if not square:isFreeOrMidair(true, true) then return false end
	-- farming plot have to be on natural floor (no road, concrete etc.)
	for i = 0, square:getObjects():size() - 1 do
		local item = square:getObjects():get(i);
		-- IsoRaindrop and IsoRainSplash have no sprite/texture
		if item:getTextureName() and (luautils.stringStarts(item:getTextureName(), "floors_exterior_natural") or
				luautils.stringStarts(item:getTextureName(), "blends_natural_01")) then
			return true;
		end
	end
--~ 	if result then
--~ 		result = square:getSpecialObjects():size() == 0;
--~ 	end
	return false;
end

function farmingPlot:getAPrompt()
    if self.canBeBuild then
        return getText("ContextMenu_Dig")
    end
end

function farmingPlot:getLBPrompt()
    return nil
end

function farmingPlot:getRBPrompt()
    return nil
end

function farmingPlot:render(x, y, z, square)
	ISBuildingObject.render(self, x, y, z, square)
end
