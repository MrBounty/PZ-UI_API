require "BuildingObjects/ISBuildingObject"

--***********************************************************
--**                    ROBERT JOHNSON                     **
--** Help you to place your campfire by let you dragging a ghost render of the tent around **
--***********************************************************

campingCampfire = ISBuildingObject:derive("campingCampfire");

--************************************************************************--
--** campingCampfire:new
--**
--************************************************************************--
function campingCampfire:create(x, y, z, north, sprite)
	local sq = getWorld():getCell():getGridSquare(x, y, z);
	ISTimedActionQueue.add(ISPlaceCampfireAction:new(self.character, sq, self.character:getInventory():FindAndReturn("CampfireKit"), 0));
end

function campingCampfire:onTimedActionStart(action)
	ISBuildingObject.onTimedActionStart(self, action)
	action.character:SetVariable("LootPosition", "Low")
	action:setOverrideHandModels(nil, nil)
    action.character:reportEvent("EventLootItem");
end

function campingCampfire:new(character)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o:init()
	o:setNorthSprite("camping_01_6");
	o:setSprite("camping_01_6");
    self.character = character;
    o.noNeedHammer = true;
	o.actionAnim = "Loot";
--~ 	o:setDragNilAfterPlace(true);
	return o;
end

function campingCampfire:isValid(square, north)
	local result = self:isSquareFree(square)
	if result then
		result = self.character:getInventory():contains("CampfireKit");
	end
	return result;
end

function campingCampfire:render(x, y, z, square)
	ISBuildingObject.render(self, x, y, z, square)
end

function campingCampfire:getAPrompt()
    if self.canBeBuild then
        return campingText.placeCampfire
    end
end

function campingCampfire:getLBPrompt()
    return nil
end

function campingCampfire:getRBPrompt()
    return nil
end

function campingCampfire:isSquareFree(square)
    if not square then return false end
    if square:getMovingObjects():size() > 0 then return false end
    if square:getStaticMovingObjects():size() > 0 then return false end
    for i=0,square:getObjects():size()-1 do
        local object = square:getObjects():get(i)
        if object:getSprite() and not object:getSprite():getProperties():Is(IsoFlagType.solidfloor) then
            if object:getType() == IsoObjectType.tree or object:getType() == IsoObjectType.wall then
                return false;
            end
            return not square:isSolidTrans();
        end
    end
    return true
end

