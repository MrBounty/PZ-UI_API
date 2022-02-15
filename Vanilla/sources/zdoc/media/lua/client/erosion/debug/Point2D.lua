--[[
  Point2D.lua - 2014.
  Author: Kees "TurboTuTone" Bekkema.
  ]]

---@class Point2D
Point2D = {};
Point2D.meta = {
    __tostring 	= function (self) 		return tostring(self.x)..":"..tostring(self.y); 								end,
    __add 		= function (self,targ) 	return self.Point2D:new(self.x + targ.x, self.y + targ.y); 						end,
    __sub 		= function (self,targ) 	return self.Point2D:new(self.x - targ.x, self.y - targ.y); 						end,
    __mul 		= function (self,targ) 	return self.Point2D:new(self.x * targ.x, self.y * targ.y); 						end,
    __div 		= function (self,targ) 	return self.Point2D:new(self.x / targ.x, self.y / targ.y); 						end,
    __unm 		= function (self) 		return self.Point2D:new(-self.x, -self.y); 										end,
    __eq 		= function (self,targ) 	if self.x == targ.x and self.y == targ.y then 	return true; end return false; 	end,
    __lt 		= function (self,targ) 	if self.x < targ.x or self.y < targ.y then 		return true; end return false; 	end,
    __le 		= function (self,targ) 	if self.x <= targ.x and self.y <= targ.y then 	return true; end return false; 	end,
};

function Point2D:new( _x, _y )
    local self_Point	= self;
    local self 			= {};
    self.Point2D		= self_Point;
    self.IsPoint2D 		= true;
    self.x, self.y 		= _x, _y;

    if type(_x) == "table" and _x.IsPoint2D then
        self.x, self.y = _x.x, _x.y;
    end

    function self.copy() 			return self.Point2D:new(self.x, self.y); 	end
    function self.set(x,y) 			self.x, self.y = x, y; 						end
    function self.setPoint(targ) 	self.x, self.y = targ.x, targ.y; 			end

    setmetatable( self, self.Point2D.meta );
    return self;
end
