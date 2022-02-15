-- note this file is a WIP and has no effect in game at the moment.
---@class ISBuildingBlueprintManager
ISBuildingBlueprintManager = {}

ISBuildingBlueprintManager.MouseDown = function (x, y)

end

ISBuildingBlueprintManager.MouseMove = function (x, y, wx, wy)

end

ISBuildingBlueprintManager.RenderUI = function ()

	if getPlayer() == nil then
		return;
	end
	local x = getMouseX();
	local y = getMouseY();

	x, y = ISCoordConversion.ToWorld(x, y, getPlayer():getZ());

	local ix = x;
	local iy = y;

	if( ix <= 0.5) then
		ix = math.floor(ix);
	else
		ix = math.ceil(ix);
	end

	if( iy <= 0.5) then
		iy = math.floor(iy);
	else
		iy = math.ceil(iy);
	end

	x, y = ISCoordConversion.ToScreen(ix, iy, getPlayer():getZ());

	local renderer = getRenderer();
	----print("got renderer");
	renderer:renderRect( x-2, y-2, 4, 4, 1.0, 1.0, 1.0, 1.0);

	renderer:renderPoly( x, y,
		x + 32, y - 16,
		x + 32, (y - 96) - 16,
		x, y - 96,
		0.3, 0.5, 1, 0.4 );
end

--Events.OnMouseDown.Add(ISBuildingBlueprintManager.MouseDown);
--Events.OnMouseDown.Add(ISBuildingBlueprintManager.MouseMove);
--Events.OnPreUIDraw.Add(ISBuildingBlueprintManager.RenderUI);
