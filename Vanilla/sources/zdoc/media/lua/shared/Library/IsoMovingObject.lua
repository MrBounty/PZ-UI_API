---@class IsoMovingObject : zombie.iso.IsoMovingObject
---@field public treeSoundMgr IsoMovingObject.TreeSoundManager
---@field public MAX_ZOMBIES_EATING int
---@field private IDCount int
---@field private tempo JVector2
---@field public noDamage boolean
---@field public last IsoGridSquare
---@field public lx float
---@field public ly float
---@field public lz float
---@field public nx float
---@field public ny float
---@field public x float
---@field public y float
---@field public z float
---@field public def IsoSpriteInstance
---@field protected current IsoGridSquare
---@field protected hitDir JVector2
---@field protected ID int
---@field protected movingSq IsoGridSquare
---@field protected solid boolean
---@field protected width float
---@field protected shootable boolean
---@field protected Collidable boolean
---@field protected scriptnx float
---@field protected scriptny float
---@field protected ScriptModule String
---@field protected movementLastFrame JVector2
---@field protected weight float
---@field bOnFloor boolean
---@field private closeKilled boolean
---@field private collideType String
---@field private lastCollideTime float
---@field private TimeSinceZombieAttack int
---@field private collidedE boolean
---@field private collidedN boolean
---@field private CollidedObject IsoObject
---@field private collidedS boolean
---@field private collidedThisFrame boolean
---@field private collidedW boolean
---@field private CollidedWithDoor boolean
---@field private collidedWithVehicle boolean
---@field private destroyed boolean
---@field private firstUpdate boolean
---@field private impulsex float
---@field private impulsey float
---@field private limpulsex float
---@field private limpulsey float
---@field private hitForce float
---@field private hitFromAngle float
---@field private PathFindIndex int
---@field private StateEventDelayTimer float
---@field private thumpTarget Thumpable
---@field private bAltCollide boolean
---@field private lastTargettedBy IsoZombie
---@field private feelersize float
---@field public bOutline boolean[]
---@field public outlineColor ColorInfo[]
---@field private eatingZombies ArrayList|Unknown
---@field private zombiesDontAttack boolean
IsoMovingObject = {}

---@public
---@return float @the lz
function IsoMovingObject:getLz() end

---@public
---@param PathFindIndex int @the PathFindIndex to set
---@return void
function IsoMovingObject:setPathFindIndex(PathFindIndex) end

---@public
---@param scriptnx float @the scriptnx to set
---@return void
function IsoMovingObject:setScriptnx(scriptnx) end

---@private
---@return void
function IsoMovingObject:slideAwayFromWalls() end

---@public
---@return boolean @the CollidedWithDoor
function IsoMovingObject:isCollidedWithDoor() end

---@public
---@param CollidedObject IsoObject @the CollidedObject to set
---@return void
function IsoMovingObject:setCollidedObject(CollidedObject) end

---Overrides:
---
---getSquare in class IsoObject
---@public
---@return IsoGridSquare
function IsoMovingObject:getSquare() end

---Overrides:
---
---getZ in class IsoObject
---@public
---@return float
function IsoMovingObject:getZ() end

---@public
---@return void
function IsoMovingObject:postupdate() end

---@public
---@param hitForce float @the hitForce to set
---@return void
function IsoMovingObject:setHitForce(hitForce) end

---@public
---@return float @the nx
function IsoMovingObject:getNx() end

---@private
---@param arg0 int
---@param arg1 int
---@return boolean
function IsoMovingObject:isInLoadedArea(arg0, arg1) end

---@private
---@return void
function IsoMovingObject:checkHitHoppable() end

---@public
---@return boolean
function IsoMovingObject:isZombiesDontAttack() end

---@public
---@param solid boolean @the solid to set
---@return void
function IsoMovingObject:setSolid(solid) end

---@public
---@return String
function IsoMovingObject:getCollideType() end

---@public
---@param collidedW boolean @the collidedW to set
---@return void
function IsoMovingObject:setCollidedW(collidedW) end

---@public
---@param y float @the y to set
---@return void
function IsoMovingObject:setY(y) end

---@public
---@param collidedS boolean @the collidedS to set
---@return void
function IsoMovingObject:setCollidedS(collidedS) end

---@public
---@return boolean @the collidedS
function IsoMovingObject:isCollidedS() end

---@public
---@return float @the feelersize
function IsoMovingObject:getFeelersize() end

---@public
---@param nx float @the nx to set
---@return void
function IsoMovingObject:setNx(nx) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoMovingObject:load(arg0, arg1, arg2) end

---@public
---@return float
function IsoMovingObject:getScreenX() end

---@public
---@param StateEventDelayTimer float @the StateEventDelayTimer to set
---@return void
function IsoMovingObject:setStateEventDelayTimer(StateEventDelayTimer) end

---@public
---@return boolean @the bAltCollide
function IsoMovingObject:isbAltCollide() end

---@public
---@param arg0 IsoMovingObject
---@return boolean
function IsoMovingObject:isPushedByForSeparate(arg0) end

---@public
---@param ID int @the ID to set
---@return void
function IsoMovingObject:setID(ID) end

---@public
---@param weight float @the weight to set
---@return void
function IsoMovingObject:setWeight(weight) end

---Overrides:
---
---isCharacter in class IsoObject
---@public
---@return boolean
function IsoMovingObject:isCharacter() end

---@public
---@param arg0 String
---@return void
function IsoMovingObject:setCollideType(arg0) end

---@public
---@param collidedN boolean @the collidedN to set
---@return void
function IsoMovingObject:setCollidedN(collidedN) end

---@public
---@param bAltCollide boolean @the bAltCollide to set
---@return void
function IsoMovingObject:setbAltCollide(bAltCollide) end

---@public
---@param firstUpdate boolean @the firstUpdate to set
---@return void
function IsoMovingObject:setFirstUpdate(firstUpdate) end

---@public
---@param arg0 JVector2
---@return void
function IsoMovingObject:MoveUnmodded(arg0) end

---@public
---@param aIDCount int @the IDCount to set
---@return void
function IsoMovingObject:setIDCount(aIDCount) end

---@private
---@param arg0 int
---@return boolean
function IsoMovingObject:DoCollide(arg0) end

---@public
---@return void
function IsoMovingObject:separate() end

---@public
---@param x float @the x to set
---@return void
function IsoMovingObject:setX(x) end

---@public
---@return IWorldRegion
function IsoMovingObject:getMasterRegion() end

---@public
---@return float @the StateEventDelayTimer
function IsoMovingObject:getStateEventDelayTimer() end

---@public
---@return boolean @the shootable
function IsoMovingObject:isShootable() end

---@public
---@param arg0 HandWeapon
---@param arg1 IsoGameCharacter
---@param arg2 float
---@param arg3 boolean
---@param arg4 float
---@return float
function IsoMovingObject:Hit(arg0, arg1, arg2, arg3, arg4) end

---@public
---@return boolean @the Collidable
function IsoMovingObject:isCollidable() end

---@public
---@param last IsoGridSquare @the last to set
---@return void
function IsoMovingObject:setLast(last) end

---@public
---@param ly float @the ly to set
---@return void
function IsoMovingObject:setLy(ly) end

---@public
---@param arg0 IsoMovingObject
---@return boolean
function IsoMovingObject:isEatingOther(arg0) end

---@public
---@param arg0 boolean
---@return void
function IsoMovingObject:setZombiesDontAttack(arg0) end

---@public
---@return void
function IsoMovingObject:DoCollideWorE() end

---@public
---@return boolean @the collidedE
function IsoMovingObject:isCollidedE() end

---@public
---@param collidedThisFrame boolean @the collidedThisFrame to set
---@return void
function IsoMovingObject:setCollidedThisFrame(collidedThisFrame) end

---@public
---@param destroyed boolean @the destroyed to set
---@return void
function IsoMovingObject:setDestroyed(destroyed) end

---@public
---@param arg0 float
---@return void
function IsoMovingObject:setLastCollideTime(arg0) end

---@public
---@param arg0 IsoMovingObject
---@return float
function IsoMovingObject:getDistanceSq(arg0) end

---@public
---@return boolean
function IsoMovingObject:isPushableForSeparate() end

---@public
---@return void
function IsoMovingObject:doStairs() end

---@public
---@return float @the scriptny
function IsoMovingObject:getScriptny() end

---@public
---@return IsoBuilding
function IsoMovingObject:getCurrentBuilding() end

---@public
---@return float @the limpulsey
function IsoMovingObject:getLimpulsey() end

---@public
---@return ArrayList|Unknown
function IsoMovingObject:getEatingZombies() end

---@public
---@param closeKilled boolean
---@return void
function IsoMovingObject:setCloseKilled(closeKilled) end

---@public
---@return boolean @the collidedN
function IsoMovingObject:isCollidedN() end

---@public
---@return float
---@overload fun(bDoNoises:boolean)
function IsoMovingObject:getGlobalMovementMod() end

---@public
---@param bDoNoises boolean
---@return float
function IsoMovingObject:getGlobalMovementMod(bDoNoises) end

---@public
---@return float
---@overload fun(x:float, y:float)
function IsoMovingObject:getWeight() end

---@public
---@param x float
---@param y float
---@return float
function IsoMovingObject:getWeight(x, y) end

---@private
---@return void
function IsoMovingObject:checkBreakHoppable() end

---@public
---@return float @the impulsey
function IsoMovingObject:getImpulsey() end

---@public
---@return boolean @the collidedW
function IsoMovingObject:isCollidedW() end

---Overrides:
---
---removeFromWorld in class IsoObject
---@public
---@return void
function IsoMovingObject:removeFromWorld() end

---Overrides:
---
---onMouseRightClick in class IsoObject
---@public
---@param lx int
---@param ly int
---@return void
function IsoMovingObject:onMouseRightClick(lx, ly) end

---Overrides:
---
---onMouseRightReleased in class IsoObject
---@public
---@return void
function IsoMovingObject:onMouseRightReleased() end

---@public
---@param limpulsey float @the limpulsey to set
---@return void
function IsoMovingObject:setLimpulsey(limpulsey) end

---@public
---@return void
function IsoMovingObject:DoCollideNorS() end

---@public
---@return IsoGridSquare @the last
function IsoMovingObject:getLastSquare() end

---@public
---@return IsoGridSquare @the current
function IsoMovingObject:getCurrentSquare() end

---@public
---@return boolean
function IsoMovingObject:isCloseKilled() end

---@public
---@return JVector2 @the hitDir
function IsoMovingObject:getHitDir() end

---@public
---@return boolean
function IsoMovingObject:getNoDamage() end

---@public
---@return boolean
function IsoMovingObject:isOnFloor() end

---@public
---@param TimeSinceZombieAttack int @the TimeSinceZombieAttack to set
---@return void
function IsoMovingObject:setTimeSinceZombieAttack(TimeSinceZombieAttack) end

---@public
---@return float
function IsoMovingObject:getScreenY() end

---@public
---@param impulsex float @the impulsex to set
---@return void
function IsoMovingObject:setImpulsex(impulsex) end

---@public
---@return void
function IsoMovingObject:preupdate() end

---@public
---@param hitFromAngle float @the hitFromAngle to set
---@return void
function IsoMovingObject:setHitFromAngle(hitFromAngle) end

---Overrides:
---
---getY in class IsoObject
---@public
---@return float
function IsoMovingObject:getY() end

---@public
---@return boolean
function IsoMovingObject:isCollidedWithVehicle() end

---@public
---@return IsoZombie @the lastTargettedBy
function IsoMovingObject:getLastTargettedBy() end

---@public
---@param CollidedWithDoor boolean @the CollidedWithDoor to set
---@return void
function IsoMovingObject:setCollidedWithDoor(CollidedWithDoor) end

---@public
---@return float @the lx
function IsoMovingObject:getLx() end

---@public
---@return boolean @the collidedThisFrame
function IsoMovingObject:isCollidedThisFrame() end

---@public
---@param collidedE boolean @the collidedE to set
---@return void
function IsoMovingObject:setCollidedE(collidedE) end

---@public
---@param dir JVector2
---@return void
function IsoMovingObject:Move(dir) end

---@public
---@param arg0 IsoGameCharacter
---@return String
function IsoMovingObject:getBumpedType(arg0) end

---@public
---@param ScriptModule String @the ScriptModule to set
---@return void
function IsoMovingObject:setScriptModule(ScriptModule) end

---@public
---@return boolean @the firstUpdate
function IsoMovingObject:isFirstUpdate() end

---@public
---@param hitDir JVector2 @the hitDir to set
---@return void
function IsoMovingObject:setHitDir(hitDir) end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function IsoMovingObject:setEatingZombies(arg0) end

---@private
---@return void
function IsoMovingObject:doTreeNoises() end

---Overrides:
---
---removeFromSquare in class IsoObject
---@public
---@return void
function IsoMovingObject:removeFromSquare() end

---@public
---@param bOnFloor boolean
---@return void
function IsoMovingObject:setOnFloor(bOnFloor) end

---@public
---@return IsoObject @the CollidedObject
function IsoMovingObject:getCollidedObject() end

---@public
---@param lx float @the lx to set
---@return void
function IsoMovingObject:setLx(lx) end

---@public
---@return float @the hitForce
function IsoMovingObject:getHitForce() end

---@public
---@return IsoGridSquare
function IsoMovingObject:getFuturWalkedSquare() end

---@public
---@return void
function IsoMovingObject:setMovingSquareNow() end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function IsoMovingObject:save(arg0, arg1) end

---@public
---@param other IsoMovingObject
---@return float
---@overload fun(x:int, y:int)
function IsoMovingObject:DistTo(other) end

---@public
---@param x int
---@param y int
---@return float
function IsoMovingObject:DistTo(x, y) end

---@private
---@return boolean
function IsoMovingObject:checkVaultOver() end

---@public
---@return float @the ny
function IsoMovingObject:getNy() end

---@public
---@param lastTargettedBy IsoZombie @the lastTargettedBy to set
---@return void
function IsoMovingObject:setLastTargettedBy(lastTargettedBy) end

---@public
---@param arg0 IsoObject
---@return float
function IsoMovingObject:DistToProper(arg0) end

---@public
---@return void
function IsoMovingObject:Despawn() end

---@public
---@return boolean @the solid
function IsoMovingObject:isSolid() end

---@public
---@param current IsoGridSquare @the current to set
---@return void
function IsoMovingObject:setCurrent(current) end

---@public
---@param impulsey float @the impulsey to set
---@return void
function IsoMovingObject:setImpulsey(impulsey) end

---@public
---@param limpulsex float @the limpulsex to set
---@return void
function IsoMovingObject:setLimpulsex(limpulsex) end

---@public
---@return JVector2 @the movementLastFrame
function IsoMovingObject:getMovementLastFrame() end

---@public
---@param other IsoMovingObject
---@return int
function IsoMovingObject:compareToY(other) end

---@public
---@param thumpTarget Thumpable @the thumpTarget to set
---@return void
function IsoMovingObject:setThumpTarget(thumpTarget) end

---@public
---@return float @the width
function IsoMovingObject:getWidth() end

---@public
---@param moveForwardVec JVector2
---@return JVector2
---@overload fun(arg0:JVector2, arg1:IsoDirections)
function IsoMovingObject:getVectorFromDirection(moveForwardVec) end

---@public
---@param arg0 JVector2
---@param arg1 IsoDirections
---@return JVector2
function IsoMovingObject:getVectorFromDirection(arg0, arg1) end

---Specified by:
---
---getPathFindIndex in interface Mover
---@public
---@return int
function IsoMovingObject:getPathFindIndex() end

---Overrides:
---
---renderlast in class IsoObject
---@public
---@return void
function IsoMovingObject:renderlast() end

---@private
---@return void
function IsoMovingObject:checkHitWall() end

---@public
---@return float @the scriptnx
function IsoMovingObject:getScriptnx() end

---@public
---@param arg0 IsoMovingObject
---@return float
---@overload fun(arg0:float, arg1:float)
function IsoMovingObject:DistToSquared(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@return float
function IsoMovingObject:DistToSquared(arg0, arg1) end

---@public
---@return float @the impulsex
function IsoMovingObject:getImpulsex() end

---@public
---@param shootable boolean @the shootable to set
---@return void
function IsoMovingObject:setShootable(shootable) end

---@public
---@return IsoMetaGrid.Zone
function IsoMovingObject:getCurrentZone() end

---@public
---@return String @the ScriptModule
function IsoMovingObject:getScriptModule() end

---@public
---@param dist float
---@return IsoGridSquare
function IsoMovingObject:getFeelerTile(dist) end

---@public
---@param Collidable boolean @the Collidable to set
---@return void
function IsoMovingObject:setCollidable(Collidable) end

---@public
---@return float
function IsoMovingObject:distToNearestCamCharacter() end

---@public
---@param width float @the width to set
---@return void
function IsoMovingObject:setWidth(width) end

---@public
---@return IsoBuilding
function IsoMovingObject:getBuilding() end

---@public
---@return float
function IsoMovingObject:getLastCollideTime() end

---@public
---@return boolean @the destroyed
function IsoMovingObject:isDestroyed() end

---@public
---@param scriptny float @the scriptny to set
---@return void
function IsoMovingObject:setScriptny(scriptny) end

---@public
---@param arg0 boolean
---@return void
function IsoMovingObject:setNoDamage(arg0) end

---@public
---@return int @the TimeSinceZombieAttack
function IsoMovingObject:getTimeSinceZombieAttack() end

---@public
---@return int @the IDCount
function IsoMovingObject:getIDCount() end

---Overrides:
---
---getObjectName in class IsoObject
---@public
---@return String
function IsoMovingObject:getObjectName() end

---Overrides:
---
---getX in class IsoObject
---@public
---@return float
function IsoMovingObject:getX() end

---@public
---@return boolean
function IsoMovingObject:isCollided() end

---@public
---@param movementLastFrame JVector2 @the movementLastFrame to set
---@return void
function IsoMovingObject:setMovementLastFrame(movementLastFrame) end

---Overrides:
---
---getFacingPosition in class IsoObject
---@public
---@param pos JVector2
---@return JVector2
function IsoMovingObject:getFacingPosition(pos) end

---@public
---@param obj IsoObject
---@return void
function IsoMovingObject:collideWith(obj) end

---@public
---@return void
function IsoMovingObject:ensureOnTile() end

---@public
---@return Thumpable
function IsoMovingObject:getThumpTarget() end

---@public
---@param z float @the z to set
---@return void
function IsoMovingObject:setZ(z) end

---@public
---@param feelersize float @the feelersize to set
---@return void
function IsoMovingObject:setFeelersize(feelersize) end

---@public
---@param ny float @the ny to set
---@return void
function IsoMovingObject:setNy(ny) end

---@public
---@return boolean
function IsoMovingObject:isSolidForSeparate() end

---@public
---@param arg0 Vector3
---@return Vector3
function IsoMovingObject:getPosition(arg0) end

---@public
---@return float @the ly
function IsoMovingObject:getLy() end

---@public
---@return float @the hitFromAngle
function IsoMovingObject:getHitFromAngle() end

---Specified by:
---
---getID in interface Mover
---@public
---@return int
function IsoMovingObject:getID() end

---@public
---@return byte
function IsoMovingObject:canHaveMultipleHits() end

---@private
---@return void
function IsoMovingObject:Collided() end

---@public
---@param other IsoMovingObject
---@param bForced boolean
---@return void
function IsoMovingObject:spotted(other, bForced) end

---@public
---@param lz float @the lz to set
---@return void
function IsoMovingObject:setLz(lz) end

---@public
---@return float @the limpulsex
function IsoMovingObject:getLimpulsex() end

---Overrides:
---
---update in class IsoObject
---@public
---@return void
function IsoMovingObject:update() end
