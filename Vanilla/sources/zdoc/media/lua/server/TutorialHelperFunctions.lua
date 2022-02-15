---@class TutorialHelperFunctions
TutorialHelperFunctions = {}

TutorialHelperFunctions.replaceInContainer = function(x, y, z, type, items)

	local obj = getCell():getGridSquare(x, y, z):getContainerItem(type);

	if obj ~= nil then
		local objCon = obj:getContainer();
		objCon:clear();
		for i, k in pairs(items) do
			objCon:AddItem(k);
		end
	end


end
