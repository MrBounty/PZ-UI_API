---@class PathFindBehavior2 : zombie.vehicles.PathFindBehavior2
---@field private tempVector2 JVector2
---@field private tempVector2f Vector2f
---@field private tempVector2_2 JVector2
---@field private tempVector3f_1 Vector3f
---@field private pointOnPath PathFindBehavior2.PointOnPath
---@field public pathNextIsSet boolean
---@field public pathNextX float
---@field public pathNextY float
---@field public Listeners ArrayList|Unknown
---@field public NPCData PathFindBehavior2.NPCData
---@field private chr IsoGameCharacter
---@field private startX float
---@field private startY float
---@field private startZ float
---@field private targetX float
---@field private targetY float
---@field private targetZ float
---@field private targetXYZ TFloatArrayList
---@field private path PolygonalMap2.Path
---@field private pathIndex int
---@field private isCancel boolean
---@field public bStopping boolean
---@field public walkingOnTheSpot WalkingOnTheSpot
---@field private actualPos ArrayList|Unknown
---@field private actualPool ObjectPool|Unknown
---@field private goal PathFindBehavior2.Goal
---@field private goalCharacter IsoGameCharacter
---@field private goalVehicle BaseVehicle
---@field private goalVehicleArea String
---@field private goalVehicleSeat int
PathFindBehavior2 = {}

---@public
---@return boolean
function PathFindBehavior2:isGoalCharacter() end

---@public
---@return boolean
function PathFindBehavior2:shouldGetUpFromCrawl() end

---@public
---@param arg0 BaseVehicle
---@return void
function PathFindBehavior2:pathToVehicleAdjacent(arg0) end

---@public
---@return float
function PathFindBehavior2:getPathLength() end

---@public
---@return float
function PathFindBehavior2:getTargetZ() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function PathFindBehavior2:pathToLocation(arg0, arg1, arg2) end

---@private
---@param arg0 PolygonalMap2.PathNode
---@param arg1 PolygonalMap2.PathNode
---@param arg2 float
---@return void
function PathFindBehavior2:checkCrawlingTransition(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return void
function PathFindBehavior2:setData(arg0, arg1, arg2) end

---@public
---@return boolean
function PathFindBehavior2:isMovingUsingPathFind() end

---@public
---@return boolean
function PathFindBehavior2:isGoalNone() end

---@public
---@return PathFindBehavior2.BehaviorResult
function PathFindBehavior2:update() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return void
function PathFindBehavior2:pathToLocationF(arg0, arg1, arg2) end

---@public
---@return boolean
function PathFindBehavior2:getIsCancelled() end

---@public
---@return boolean
function PathFindBehavior2:isGoalSound() end

---@public
---@return boolean
function PathFindBehavior2:isStrafing() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 IsoMovingObject
---@param arg4 PolygonalMap2.Path
---@param arg5 PathFindBehavior2.PointOnPath
---@return void
function PathFindBehavior2:closestPointOnPath(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 IsoMovingObject
---@param arg1 float
---@return void
function PathFindBehavior2:moveToDir(arg0, arg1) end

---@public
---@param arg0 KahluaTable
---@return void
function PathFindBehavior2:pathToNearestTable(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return void
function PathFindBehavior2:moveToPoint(arg0, arg1, arg2) end

---@public
---@return void
function PathFindBehavior2:render() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function PathFindBehavior2:pathToCharacter(arg0) end

---@public
---@param arg0 TFloatArrayList
---@return void
function PathFindBehavior2:pathToNearest(arg0) end

---@public
---@return void
function PathFindBehavior2:cancel() end

---@private
---@return void
function PathFindBehavior2:updateWhileRunningPathfind() end

---@public
---@return void
function PathFindBehavior2:reset() end

---@public
---@return boolean
function PathFindBehavior2:isGoalVehicleSeat() end

---@public
---@param arg0 Mover
---@return void
function PathFindBehavior2:Failed(arg0) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return boolean
function PathFindBehavior2:checkDoorHoppableWindow(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return boolean
function PathFindBehavior2:isTargetLocation(arg0, arg1, arg2) end

---@public
---@param arg0 BaseVehicle
---@param arg1 int
---@return void
function PathFindBehavior2:pathToVehicleSeat(arg0, arg1) end

---@public
---@param arg0 PolygonalMap2.Path
---@param arg1 Mover
---@return void
function PathFindBehavior2:Succeeded(arg0, arg1) end

---@public
---@return float
function PathFindBehavior2:getTargetY() end

---@public
---@return boolean
function PathFindBehavior2:isGoalLocation() end

---@public
---@return float
function PathFindBehavior2:getTargetX() end

---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 PathFindBehavior2.PointOnPath
---@return void
function PathFindBehavior2:advanceAlongPath(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 BaseVehicle
---@param arg1 String
---@return void
function PathFindBehavior2:pathToVehicleArea(arg0, arg1) end

---@public
---@return boolean
function PathFindBehavior2:isGoalVehicleAdjacent() end

---@public
---@return boolean
function PathFindBehavior2:isGoalVehicleArea() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function PathFindBehavior2:pathToSound(arg0, arg1, arg2) end

---@public
---@return IsoGameCharacter
function PathFindBehavior2:getTargetChar() end
