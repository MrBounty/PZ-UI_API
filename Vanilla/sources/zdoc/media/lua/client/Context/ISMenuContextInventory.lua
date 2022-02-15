--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

---@class ISMenuContextInventory
ISMenuContextInventory = {};

function ISMenuContextInventory.new()
	local self 		= ISMenuElement.new();
    self.playerNum  = nil;
    self.player     = nil;
    self.context    = nil;
    self.items      = nil;
    self.inventory  = nil;
    self.test       = nil;
    --self.item       = nil;
	
	function self.init()
		self.loadElements( ISInventoryMenuElements );
	end	

	function self.createMenu( _player, _context, _items, _item )
        --print("Inventory Context menu");
        local playerObj 	= getSpecificPlayer(_player);
        self.playerNum      = _player;
        self.player         = playerObj;
        self.context        = _context;
        self.items          = _items;
        self.inventory 	    = playerObj:getInventory();
        self.test 		    = false;
        --self.item           = _item;

        for i,k in pairs(_items) do
            -- inventory item list
            if not instanceof(k, "InventoryItem") then
                for i2,k2 in ipairs(k.items) do
                    if i2 ~= 1 then
                        if instanceof(k2, "InventoryItem") then
                            self.checkInvItem(k2);
                            break;
                        end
                    end
                end
                -- inventory single item select
            elseif instanceof(k, "InventoryItem") then
                self.checkInvItem(k);
            end
            break;
        end
    end

    function self.checkInvItem( _item )
        for index, element in self.elements.indexIterator() do
            element.createMenu( _item );
        end
    end
		
	return self;
end	
