--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

---@class ISMenuContext
ISMenuContext = {};

function ISMenuContext.new()
	local self 		= {};
	self.elements 	= ISPriorityTable.new();	-- Specialized table for z index ordering, see Utils folder.
	
	-- needs to be overridden
	function self.createMenu()
	end
	
	-- load elements from a target table, target table must contain functions that return valid ISMenuElements
	function self.loadElements( _targetTable )
		for name, func in pairs(_targetTable) do
			if type(func)=="function" then
				local t = func();
				if t and type(t)=="table" then
					if t.createMenu ~= nil and type(t.createMenu)=="function" then
						t.parentMenu = self;
						t.name = name;
						t.init();
						self.elements.add( name, t, t.zIndex );
					end
				end			
			end
		end
	end
	
	return self;
end
