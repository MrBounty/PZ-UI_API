--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

---@class ISMoveableTools
ISMoveableTools = {};

function ISMoveableTools.getScrapableObjects( _char, _square )
    local movePropList = {};
    if _char ~= nil and _square ~= nil then
        local objects = ISMoveableTools.getObjectList( _square );
        for k,v in ipairs(objects) do
            if v.object and v.moveProps and v.moveProps.canScrap and v.moveProps:canScrapObject(_char) then
                table.insert(movePropList, v);
            end
        end
    end
    return movePropList;
end

-- gets a list of spriteProps FromObject
function ISMoveableTools.getObjectList( _square )
    local objects = {};
    if _square then
        for i = _square:getObjects():size(),1,-1 do
            local obj = _square:getObjects():get(i-1);
            local moveProps = ISMoveableSpriteProps.fromObject( obj );
            if moveProps then
                table.insert(objects, { object = obj, moveProps = moveProps });
            end
        end
    end
    return objects;
end

-- supply player, square of object, isoobject, ISMoveableSpriteProps
-- if valid returns ISMoveableSpriteProps otherwise nil
function ISMoveableTools.canPlayerPickUpMoveable( _char, _square, _object, _moveProps )
    if _square and _char and _object and _moveProps then
        if _moveProps and _moveProps:canPickUpMoveable( _char, _square, _object ) then
            return _moveProps;
        end
    end
    return nil;
end

-- supply player, square of object, isoobject
-- if valid returns ISMoveableSpriteProps otherwise nil
function ISMoveableTools.canPlayerPickUpObject( _char, _square, _object )
    if _square and _char and _object then
        local moveProps, isOverlay = ISMoveableTools.isObjectMoveable(_object);
        if moveProps and moveProps:canPickUpMoveable( _char, _square, _object ) then
            return moveProps, isOverlay;
        end
    end
    return nil;
end

-- check if a object is movable, if so returns the ISMoveableSpriteProps otherwise nil. note: also supplies a seconds parameter true/false wether the found object is a spriteoverlay
function ISMoveableTools.isObjectMoveable(  _object )
    if _object then
        local moveProps = ISMoveableSpriteProps.new(_object:getSprite());
        if moveProps and moveProps.isMoveable then
            return moveProps, false;
        elseif moveProps and moveProps.spriteProps then
            if moveProps.spriteProps:Is("WallNW") or moveProps.spriteProps:Is("WallN") or moveProps.spriteProps:Is("WallW") then
                local sprList = _object:getChildSprites();
                if sprList then
                    local list_size 	= sprList:size();
                    if list_size > 0 then
                        for i=list_size-1, 0, -1 do
                            local sprite = sprList:get(i):getParentSprite();
                            local moveProps2 = ISMoveableSpriteProps.new( sprite );
                            if moveProps2.isMoveable and moveProps2.type == "WallOverlay" then
                                return moveProps2, true;
                            end
                        end
                    end
                end
            end
        end
    end
    return nil;
end

-- Returns a list of moveable objects on square, for each element in the resulting array the Object, ISMoveableSpriteProps and IsOverlay are stored.
function ISMoveableTools.getMoveableList( _square )
    local objects = {};
    if _square then
        for i = _square:getObjects():size(),1,-1 do
            local obj = _square:getObjects():get(i-1);
            local moveProps, isOverlay = ISMoveableTools.isObjectMoveable(obj);
            if moveProps then
                table.insert(objects, { object = obj, moveProps = moveProps, isOverlay = isOverlay });
            end
        end
    end
    return objects;
end

-- ##########################################################################################################################

