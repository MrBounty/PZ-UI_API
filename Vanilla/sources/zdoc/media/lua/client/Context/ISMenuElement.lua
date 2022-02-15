--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

---@class ISMenuElement
ISMenuElement = {}

function ISMenuElement.new()
	local self 			= ISMenuContext.new();
	self.zIndex 		= 1000;		-- Z index priority, the lower the number the higher it will appear in the context menu
	self.parentMenu 	= nil;		-- reference to parent menu i.e. ISMenuContextWorld, is automatically set when elements are loaded.
	self.name			= "";
	
	function self.init()
	end	
	
	function self.createMenu()
	end
	
	return self;
end
