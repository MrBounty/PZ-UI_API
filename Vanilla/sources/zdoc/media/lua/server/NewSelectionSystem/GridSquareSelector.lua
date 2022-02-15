
---@class GridSquareSelector
GridSquareSelector = {}

GridSquareSelector.MouseUp = function()
    if getPlayer() == nil then
        return;
    end

    local mx = getMouseX();
    local my = getMouseY();

    local z = getPlayer():getZ();
    local wx, wy = ISCoordConversion.ToWorld(mx, my, z);

    local gridsquare = getCell():getGridSquare(wx, wy, z);

    print "determining if ground is walkable"
    while z >= 0 and (gridsquare == nil or not gridsquare:TreatAsSolidFloor()) do
        z = z - 1;
        wx, wy = ISCoordConversion.ToWorld(mx, my, z);

        gridsquare = getCell():getGridSquare(wx, wy, z);

    end

    if z < 0 then
        return;
    end

    print "success: ground is walkable. Starting pathfind."

    if gridsquare ~= nil and  gridsquare:TreatAsSolidFloor() then
        ISTimedActionQueue.clear(getPlayer());
        ISTimedActionQueue.add(ISWalkToTimedAction:new(getPlayer(), gridsquare));
    end
end

GridSquareSelector.RenderUI = function()
    if getPlayer() == nil then
        return;
    end

    local mx = getMouseX();
    local my = getMouseY();

    local wx, wy = ISCoordConversion.ToWorld(mx, my, getPlayer():getZ());

    wx = math.floor(wx);
    wy = math.floor(wy);

    mx, my = ISCoordConversion.ToScreen(wx, wy, getPlayer():getZ());

    local tex = getTexture("media/ui/TargetPos.png");

    -- not sure why -35 instead of -32 is necessary but hey ho.
    getRenderer():render(tex, mx - 35, my - 96, 64, 128, 1, 1, 1, 0.4);
end

--Events.OnPreUIDraw.Add(GridSquareSelector.RenderUI);
--Events.OnMouseDown.Add(GridSquareSelector.MouseUp);
