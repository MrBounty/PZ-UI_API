
---@class ISCoordConversion
ISCoordConversion = {}

ISCoordConversion.ToScreen = function(x, y, z, cz)

    if cz == nil then
        cz = 0;
    end

	local sx = IsoUtils.XToScreen( x, y, z, cz );
	local sy = IsoUtils.YToScreen( x, y, z, cz );

	sx = sx - getCameraOffX();
	sy = sy - getCameraOffY();

	return sx, sy;
end

ISCoordConversion.ToWorld = function(x, y, z)

	local wx = IsoUtils.XToIso( x, y, z );
	local wy = IsoUtils.YToIso( x, y, z );

 --   --print("Output of ToWorld: "..wx..", "..wy)
	return wx, wy;
end
