--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

---@class AcceptItemFunction
AcceptItemFunction = {}

-- The item.AcceptItemFunction script property for item containers
-- specifies the name of a Lua function that will be called to test
-- whether an item is allowed inside a container.  The function name
-- may contain "." characters. For example:
--     AcceptItemFunction = AcceptItemFunction.FirstAidKit

-- Example: not used
function AcceptItemFunction.FirstAidKit(container, item)
	return item:getStringItemType() == "Medical"
end

-- Example: not used
function AcceptItemFunction.KeyRing(container, item)
	return item:getCategory() == "Key"
end
