--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

local instance = false;

---@class ISContextManager
ISContextManager = {};

function ISContextManager.getInstance()
    if instance then
        return instance;
    end
	
	local hasInit = false;

	local menuWorld;
	local menuBuild;
	local menuInventory;
    local self = {};
	

	function self.getWorldMenu() 		return menuWorld; 		end
	function self.getBuildMenu() 		return menuBuild; 		end
	function self.getInventoryMenu() 	return menuInventory 	end

	function self.createWorldMenu( _playerNum, _object, _objects, _x, _y, _test )
		local context = menuWorld.createMenu( _playerNum, _object, _objects, _x, _y, _test );
		--menuBuild.createMenu();
        return context;
	end
	
	function self.createInventoryMenu( _player, _context, _items, _item )
		local context = menuInventory.createMenu( _player, _context, _items, _item );
        return context;
	end
	
    function self.init()
		if not hasInit then
			print("Init ISContextManager")
			menuWorld 				= ISMenuContextWorld.new();
			menuWorld.init();
			menuBuild				= ISMenuContextBuild.new();
			menuBuild.init();
			menuInventory			= ISMenuContextInventory.new();	
			menuInventory.init();
			Events.OnFillInventoryObjectContextMenu.Add(self.createInventoryMenu);
			--Events.OnFillWorldObjectContextMenu.Add(self.createWorldMenu);
			hasInit = true;
		end
    end

	instance = self;
    return self;
end

instance = ISContextManager.getInstance();

Events.OnGameStart.Add( instance.init );	
