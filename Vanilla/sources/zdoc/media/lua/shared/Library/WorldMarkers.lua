---@class WorldMarkers : zombie.iso.WorldMarkers
---@field private CIRCLE_TEXTURE_SCALE float
---@field public instance WorldMarkers
---@field private NextGridSquareMarkerID int
---@field private NextHomingPointID int
---@field private gridSquareMarkers List|Unknown
---@field private homingPoints WorldMarkers.PlayerHomingPointList[]
---@field private directionArrows WorldMarkers.DirectionArrowList[]
---@field private stCol ColorInfo
---@field private playerScreen WorldMarkers.PlayerScreen
---@field private intersectPoint WorldMarkers.Point
---@field private arrowStart WorldMarkers.Point
---@field private arrowEnd WorldMarkers.Point
---@field private arrowLine WorldMarkers.Line
WorldMarkers = {}

---@public
---@param arg0 int
---@return WorldMarkers.DirectionArrow
function WorldMarkers:getDirectionArrow(arg0) end

---@public
---@param arg0 IsoPlayer
---@return void
function WorldMarkers:removeAllDirectionArrows(arg0) end

---@public
---@param arg0 IsoPlayer
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 String
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 float
---@return WorldMarkers.DirectionArrow
function WorldMarkers:addDirectionArrow(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@return void
function WorldMarkers:debugRender() end

---@private
---@param arg0 Texture
---@param arg1 float
---@param arg2 float
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 float
---@param arg7 float
---@param arg8 float
---@param arg9 float
---@param arg10 float
---@return void
function WorldMarkers:DrawTextureAngle(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) end

---@public
---@param arg0 IsoPlayer
---@param arg1 int
---@param arg2 int
---@return WorldMarkers.PlayerHomingPoint
---@overload fun(arg0:IsoPlayer, arg1:int, arg2:int, arg3:float, arg4:float, arg5:float, arg6:float)
---@overload fun(arg0:IsoPlayer, arg1:int, arg2:int, arg3:String, arg4:float, arg5:float, arg6:float, arg7:float, arg8:boolean, arg9:int)
function WorldMarkers:addPlayerHomingPoint(arg0, arg1, arg2) end

---@public
---@param arg0 IsoPlayer
---@param arg1 int
---@param arg2 int
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@return WorldMarkers.PlayerHomingPoint
function WorldMarkers:addPlayerHomingPoint(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 IsoPlayer
---@param arg1 int
---@param arg2 int
---@param arg3 String
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 boolean
---@param arg9 int
---@return WorldMarkers.PlayerHomingPoint
function WorldMarkers:addPlayerHomingPoint(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) end

---@public
---@param arg0 IsoCell.PerPlayerRender
---@param arg1 int
---@param arg2 int
---@return void
function WorldMarkers:renderGridSquareMarkers(arg0, arg1, arg2) end

---@public
---@param arg0 IsoPlayer
---@return void
function WorldMarkers:removeAllHomingPoints(arg0) end

---@public
---@param arg0 IsoGridSquare
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 boolean
---@param arg5 float
---@return WorldMarkers.GridSquareMarker
---@overload fun(arg0:String, arg1:String, arg2:IsoGridSquare, arg3:float, arg4:float, arg5:float, arg6:boolean, arg7:float)
---@overload fun(arg0:String, arg1:String, arg2:IsoGridSquare, arg3:float, arg4:float, arg5:float, arg6:boolean, arg7:float, arg8:float, arg9:float, arg10:float)
function WorldMarkers:addGridSquareMarker(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 IsoGridSquare
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 boolean
---@param arg7 float
---@return WorldMarkers.GridSquareMarker
function WorldMarkers:addGridSquareMarker(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 IsoGridSquare
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 boolean
---@param arg7 float
---@param arg8 float
---@param arg9 float
---@param arg10 float
---@return WorldMarkers.GridSquareMarker
function WorldMarkers:addGridSquareMarker(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) end

---@public
---@return void
function WorldMarkers:update() end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return int
function WorldMarkers:GetDistance(arg0, arg1, arg2, arg3) end

---@public
---@return void
function WorldMarkers:reset() end

---@public
---@param arg0 int
---@return boolean
---@overload fun(arg0:WorldMarkers.PlayerHomingPoint)
function WorldMarkers:removeHomingPoint(arg0) end

---@public
---@param arg0 WorldMarkers.PlayerHomingPoint
---@return boolean
function WorldMarkers:removeHomingPoint(arg0) end

---@public
---@param arg0 IsoPlayer
---@param arg1 WorldMarkers.DirectionArrow
---@return boolean
---@overload fun(arg0:IsoPlayer, arg1:int)
function WorldMarkers:removePlayerDirectionArrow(arg0, arg1) end

---@public
---@param arg0 IsoPlayer
---@param arg1 int
---@return boolean
function WorldMarkers:removePlayerDirectionArrow(arg0, arg1) end

---@public
---@param arg0 int
---@return boolean
---@overload fun(arg0:WorldMarkers.DirectionArrow)
function WorldMarkers:removeDirectionArrow(arg0) end

---@public
---@param arg0 WorldMarkers.DirectionArrow
---@return boolean
function WorldMarkers:removeDirectionArrow(arg0) end

---@private
---@return void
function WorldMarkers:updateDirectionArrows() end

---@public
---@param arg0 IsoPlayer
---@param arg1 WorldMarkers.PlayerHomingPoint
---@return boolean
---@overload fun(arg0:IsoPlayer, arg1:int)
function WorldMarkers:removePlayerHomingPoint(arg0, arg1) end

---@public
---@param arg0 IsoPlayer
---@param arg1 int
---@return boolean
function WorldMarkers:removePlayerHomingPoint(arg0, arg1) end

---@public
---@param arg0 WorldMarkers.Line
---@param arg1 WorldMarkers.Line
---@param arg2 WorldMarkers.Point
---@return boolean
function WorldMarkers:intersectLineSegments(arg0, arg1, arg2) end

---@public
---@param arg0 boolean
---@return void
function WorldMarkers:renderDirectionArrow(arg0) end

---@private
---@param arg0 float
---@return float
function WorldMarkers:angleDegrees(arg0) end

---@private
---@return void
function WorldMarkers:updateHomingPoints() end

---@public
---@param arg0 int
---@return WorldMarkers.PlayerHomingPoint
function WorldMarkers:getHomingPoint(arg0) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return float
function WorldMarkers:getAngle(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 WorldMarkers.GridSquareMarker
---@return boolean
---@overload fun(arg0:int)
function WorldMarkers:removeGridSquareMarker(arg0) end

---@public
---@param arg0 int
---@return boolean
function WorldMarkers:removeGridSquareMarker(arg0) end

---@public
---@return void
function WorldMarkers:init() end

---@private
---@return void
function WorldMarkers:updateGridSquareMarkers() end

---@public
---@return void
function WorldMarkers:renderHomingPoint() end

---@public
---@param arg0 int
---@return WorldMarkers.GridSquareMarker
function WorldMarkers:getGridSquareMarker(arg0) end

---@public
---@return void
function WorldMarkers:render() end
