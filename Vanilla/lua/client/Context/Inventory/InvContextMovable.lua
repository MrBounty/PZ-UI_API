--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

ISInventoryMenuElements = ISInventoryMenuElements or {};

function ISInventoryMenuElements.ContextMovable()
    local self 					= ISMenuElement.new();
    self.invMenu			    = ISContextManager.getInstance().getInventoryMenu();

    function self.init()
    end

    function self.createMenu( _item )
        if instanceof(_item, "Moveable") then
            if _item:getContainer() ~= self.invMenu.inventory then
                return;
            end

            if self.invMenu.player:getPrimaryHandItem() ~= _item and self.invMenu.player:getSecondaryHandItem() ~= _item then
                self.invMenu.context:addOption(getText("IGUI_PlaceObject"), self.invMenu, self.openMovableCursor, _item );
            end
        end
    end

    function self.openMovableCursor( _p, _item )
        local mo = ISMoveableCursor:new(_p.player);
        getCell():setDrag(mo, mo.player);
        mo:setMoveableMode("place");
        mo:tryInitialItem(_item);
    end

    return self;
end

