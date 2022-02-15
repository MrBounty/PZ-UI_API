---@class IsoThumpable : zombie.iso.objects.IsoThumpable
---@field private _table KahluaTable
---@field private modData KahluaTable
---@field public isDoor Boolean
---@field public isDoorFrame Boolean
---@field public breakSound String
---@field private isCorner boolean
---@field private isFloor boolean
---@field private blockAllTheSquare boolean
---@field public Locked boolean
---@field public MaxHealth int
---@field public Health int
---@field public PushedMaxStrength int
---@field public PushedStrength int
---@field closedSprite IsoSprite
---@field public north boolean
---@field private thumpDmg int
---@field private crossSpeed float
---@field public open boolean
---@field openSprite IsoSprite
---@field private destroyed boolean
---@field private canBarricade boolean
---@field public canPassThrough boolean
---@field private isStairs boolean
---@field private isContainer boolean
---@field private dismantable boolean
---@field private canBePlastered boolean
---@field private paintable boolean
---@field private isThumpable boolean
---@field private isHoppable boolean
---@field private lightSourceRadius int
---@field private lightSourceLife int
---@field private lightSourceXOffset int
---@field private lightSourceYOffset int
---@field private lightSourceOn boolean
---@field private lightSource IsoLightSource
---@field private lightSourceFuel String
---@field private lifeLeft float
---@field private lifeDelta float
---@field private haveFuel boolean
---@field private updateAccumulator float
---@field private lastUpdateHours float
---@field public keyId int
---@field private lockedByKey boolean
---@field public lockedByPadlock boolean
---@field private canBeLockByPadlock boolean
---@field public lockedByCode int
---@field public OldNumPlanks int
---@field public thumpSound String
---@field public tempo JVector2
IsoThumpable = {}

---Overrides:
---
---update in class IsoObject
---@public
---@return void
function IsoThumpable:update() end

---Overrides:
---
---TestVision in class IsoObject
---@public
---@param from IsoGridSquare
---@param to IsoGridSquare
---@return IsoObject.VisionResult
function IsoThumpable:TestVision(from, to) end

---@public
---@return float
function IsoThumpable:getLifeLeft() end

---@public
---@param pIsDoor boolean
---@return void
---@overload fun(pIsDoor:Boolean)
function IsoThumpable:setIsDoor(pIsDoor) end

---@public
---@param pIsDoor Boolean
---@return void
function IsoThumpable:setIsDoor(pIsDoor) end

---@public
---@param lifeDelta float
---@return void
function IsoThumpable:setLifeDelta(lifeDelta) end

---@public
---@return boolean
function IsoThumpable:isDoorFrame() end

---Overrides:
---
---getKeyId in class IsoObject
---@public
---@return int
function IsoThumpable:getKeyId() end

---@public
---@param lightSourceYOffset int
---@return void
function IsoThumpable:setLightSourceYOffset(lightSourceYOffset) end

---@public
---@param arg0 IsoGridSquare
---@return boolean
function IsoThumpable:isAdjacentToSquare(arg0) end

---@public
---@return boolean
function IsoThumpable:isObstructed() end

---@public
---@return boolean
function IsoThumpable:isHoppable() end

---@public
---@param arg0 int
---@return void
function IsoThumpable:setHealth(arg0) end

---@public
---@return void
function IsoThumpable:ToggleDoorSilent() end

---Overrides:
---
---saveChange in class IsoObject
---@public
---@param change String
---@param tbl KahluaTable
---@param bb ByteBuffer
---@return void
function IsoThumpable:saveChange(change, tbl, bb) end

---Overrides:
---
---setKeyId in class IsoObject
---@public
---@param keyId int
---@return void
---@overload fun(keyId:int, doNetwork:boolean)
function IsoThumpable:setKeyId(keyId) end

---@public
---@param keyId int
---@param doNetwork boolean
---@return void
function IsoThumpable:setKeyId(keyId, doNetwork) end

---Overrides:
---
---addToWorld in class IsoObject
---@public
---@return void
function IsoThumpable:addToWorld() end

---@public
---@param arg0 IsoGameCharacter
---@return Thumpable
function IsoThumpable:getThumpableFor(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return IsoGridSquare
function IsoThumpable:getAddSheetSquare(arg0) end

---@public
---@return boolean
function IsoThumpable:haveSheetRope() end

---@public
---@param canBeLockByPadlock boolean
---@return void
function IsoThumpable:setCanBeLockByPadlock(canBeLockByPadlock) end

---Can you pass through the item, if false we gonna test the collide default to false (so it collide)
---@public
---@param pCanPassThrough boolean
---@return void
function IsoThumpable:setCanPassThrough(pCanPassThrough) end

---@public
---@param arg0 boolean
---@param arg1 byte
---@param arg2 UdpConnection
---@param arg3 ByteBuffer
---@return void
function IsoThumpable:syncIsoObject(arg0, arg1, arg2, arg3) end

---@public
---@param isHoppable boolean
---@return void
function IsoThumpable:setHoppable(isHoppable) end

---@public
---@return void
function IsoThumpable:destroy() end

---Numbers of zeds need to hurt the object default 8
---@public
---@param pThumpDmg Integer
---@return void
function IsoThumpable:setThumpDmg(pThumpDmg) end

---Overrides:
---
---onMouseLeftClick in class IsoObject
---@public
---@param x int
---@param y int
---@return boolean
function IsoThumpable:onMouseLeftClick(x, y) end

---@public
---@return boolean
function IsoThumpable:canBeLockByPadlock() end

---@public
---@param chr IsoGameCharacter
---@return InventoryItem
function IsoThumpable:removeCurrentFuel(chr) end

---@public
---@param arg0 IsoGameCharacter
---@return IsoBarricade
function IsoThumpable:getBarricadeForCharacter(arg0) end

---@public
---@return IsoGridSquare
function IsoThumpable:getIndoorSquare() end

---@public
---@param lightSourceOn boolean
---@return void
function IsoThumpable:setLightSourceOn(lightSourceOn) end

---@public
---@param modData KahluaTable
---@return void
function IsoThumpable:setModData(modData) end

---@public
---@return boolean
function IsoThumpable:isBarricadeAllowed() end

---@public
---@param chr IsoGameCharacter
---@return void
function IsoThumpable:addSheet(chr) end

---@public
---@param arg0 boolean
---@return IsoDirections
function IsoThumpable:getSpriteEdge(arg0) end

---@public
---@return int
function IsoThumpable:getMaxHealth() end

---Overrides:
---
---setTable in class IsoObject
---@public
---@param _table KahluaTable @the table to set
---@return void
function IsoThumpable:setTable(_table) end

---@public
---@param arg0 int
---@return void
function IsoThumpable:setMaxHealth(arg0) end

---@public
---@return boolean
function IsoThumpable:isFloor() end

---@public
---@param toggle boolean
---@return void
function IsoThumpable:toggleLightSource(toggle) end

---@public
---@param chr IsoGameCharacter
---@return boolean
function IsoThumpable:isLockedToCharacter(chr) end

---The sound that be played if this object is broken default "breakdoor"
---@public
---@param pBreakSound String
---@return void
function IsoThumpable:setBreakSound(pBreakSound) end

---Overrides:
---
---TestPathfindCollide in class IsoObject
---@public
---@param obj IsoMovingObject
---@param from IsoGridSquare
---@param to IsoGridSquare
---@return boolean
function IsoThumpable:TestPathfindCollide(obj, from, to) end

---Overrides:
---
---getTable in class IsoObject
---@public
---@return KahluaTable @the table
function IsoThumpable:getTable() end

---@public
---@return boolean
function IsoThumpable:isThumpable() end

---@public
---@param arg0 IsoGameCharacter
---@return IsoBarricade
function IsoThumpable:getBarricadeOppositeCharacter(arg0) end

---@public
---@return String
function IsoThumpable:getThumpSound() end

---@public
---@return int
function IsoThumpable:getLightSourceLife() end

---@public
---@return boolean
function IsoThumpable:haveFuel() end

---@public
---@return boolean
function IsoThumpable:isWindow() end

---@public
---@return boolean
function IsoThumpable:IsOpen() end

---@public
---@return boolean
function IsoThumpable:isStairs() end

---@public
---@param arg0 String
---@return void
function IsoThumpable:setThumpSound(arg0) end

---@public
---@return float
function IsoThumpable:getLifeDelta() end

---@public
---@return IsoGridSquare
function IsoThumpable:getOppositeSquare() end

---@public
---@return boolean
function IsoThumpable:isDoor() end

---Overrides:
---
---TestCollide in class IsoObject
---@public
---@param obj IsoMovingObject
---@param from IsoGridSquare
---@param to IsoGridSquare
---@return boolean
function IsoThumpable:TestCollide(obj, from, to) end

---Overrides:
---
---getModData in class IsoObject
---@public
---@return KahluaTable
function IsoThumpable:getModData() end

---@public
---@return IsoCurtain
function IsoThumpable:HasCurtains() end

---@private
---@return String
function IsoThumpable:getSoundPrefix() end

---@public
---@param canBePlastered boolean
---@return void
function IsoThumpable:setCanBePlastered(canBePlastered) end

---@private
---@return int
function IsoThumpable:calcLightSourceX() end

---Overrides:
---
---hasModData in class IsoObject
---@public
---@return boolean
function IsoThumpable:hasModData() end

---@public
---@return boolean
function IsoThumpable:isLocked() end

---@public
---@return int
function IsoThumpable:getLockedByCode() end

---@public
---@param lightSourceXOffset int
---@return void
function IsoThumpable:setLightSourceXOffset(lightSourceXOffset) end

---@private
---@param arg0 BaseCharacterSoundEmitter
---@param arg1 String
---@return void
function IsoThumpable:playDoorSound(arg0, arg1) end

---@public
---@return int
function IsoThumpable:getLightSourceRadius() end

---@public
---@param pCorner boolean
---@return void
function IsoThumpable:setCorner(pCorner) end

---Can you barricade/unbarricade the item default true
---@public
---@param pCanBarricade boolean
---@return void
function IsoThumpable:setCanBarricade(pCanBarricade) end

---@public
---@param chr IsoGameCharacter
---@return void
function IsoThumpable:ToggleDoorActual(chr) end

---@public
---@return IsoGridSquare
function IsoThumpable:getInsideSquare() end

---@public
---@param lightSourceFuel String
---@return void
function IsoThumpable:setLightSourceFuel(lightSourceFuel) end

---@public
---@return boolean
function IsoThumpable:isCanPassThrough() end

---@public
---@return boolean
function IsoThumpable:isBlockAllTheSquare() end

---@public
---@param chr IsoGameCharacter
---@return void
function IsoThumpable:ToggleDoor(chr) end

---@public
---@return boolean
function IsoThumpable:isLockedByKey() end

---@public
---@param radius int
---@param offsetX int
---@param offsetY int
---@param offsetZ int
---@param life int
---@param lightSourceFuel String
---@param baseItem InventoryItem
---@param chr IsoGameCharacter
---@return void
function IsoThumpable:createLightSource(radius, offsetX, offsetY, offsetZ, life, lightSourceFuel, baseItem, chr) end

---Overrides:
---
---setSprite in class IsoObject
---@public
---@param sprite String
---@return void
function IsoThumpable:setSprite(sprite) end

---@public
---@return float
function IsoThumpable:getCrossSpeed() end

---@public
---@return boolean
function IsoThumpable:isDismantable() end

---@public
---@return boolean
function IsoThumpable:isPaintable() end

---@public
---@return boolean
function IsoThumpable:canAddSheetRope() end

---@public
---@return boolean
function IsoThumpable:getNorth() end

---@public
---@return int
function IsoThumpable:getLightSourceYOffset() end

---@public
---@param pIsContainer boolean
---@return void
function IsoThumpable:setIsContainer(pIsContainer) end

---@public
---@return boolean
function IsoThumpable:IsStrengthenedByPushedItems() end

---Specified by:
---
---Thump in interface Thumpable
---@public
---@param thumper IsoMovingObject
---@return void
function IsoThumpable:Thump(thumper) end

---@private
---@return int
function IsoThumpable:calcLightSourceY() end

---@public
---@param lightSource IsoLightSource
---@return void
function IsoThumpable:setLightSource(lightSource) end

---@public
---@param haveFuel boolean
---@return void
function IsoThumpable:setHaveFuel(haveFuel) end

---@public
---@return boolean
function IsoThumpable:canBePlastered() end

---@public
---@param pCrossSpeed float
---@return void
function IsoThumpable:setCrossSpeed(pCrossSpeed) end

---Overrides:
---
---loadChange in class IsoObject
---@public
---@param change String
---@param bb ByteBuffer
---@return void
function IsoThumpable:loadChange(change, bb) end

---@public
---@param lightSourceRadius int
---@return void
function IsoThumpable:setLightSourceRadius(lightSourceRadius) end

---@public
---@return boolean
function IsoThumpable:isCorner() end

---@public
---@param chr IsoGameCharacter
---@return IsoGridSquare
function IsoThumpable:getOtherSideOfDoor(chr) end

---@public
---@param lightSourceLife int
---@return void
function IsoThumpable:setLightSourceLife(lightSourceLife) end

---@public
---@return int
function IsoThumpable:getLightSourceXOffset() end

---@param arg0 int
---@return void
function IsoThumpable:Damage(arg0) end

---Can you barricade/unbarricade the item
---@public
---@return boolean
function IsoThumpable:getCanBarricade() end

---@public
---@param pStairs boolean
---@return void
function IsoThumpable:setIsStairs(pStairs) end

---Overrides:
---
---getObjectName in class IsoObject
---@public
---@return String
function IsoThumpable:getObjectName() end

---@public
---@return int
function IsoThumpable:getHealth() end

---Overrides:
---
---removeFromWorld in class IsoObject
---@public
---@return void
function IsoThumpable:removeFromWorld() end

---@public
---@param arg0 IsoGameCharacter
---@return boolean
function IsoThumpable:canClimbOver(arg0) end

---@public
---@param lockedByCode int
---@return void
function IsoThumpable:setLockedByCode(lockedByCode) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoThumpable:load(arg0, arg1, arg2) end

---Specified by:
---
---isDestroyed in interface Thumpable
---@public
---@return boolean
function IsoThumpable:isDestroyed() end

---@public
---@param lockedByPadlock boolean
---@return void
function IsoThumpable:setLockedByPadlock(lockedByPadlock) end

---@public
---@return void
function IsoThumpable:syncIsoThumpable() end

---@public
---@param arg0 IsoGameCharacter
---@return boolean
function IsoThumpable:canClimbThrough(arg0) end

---@public
---@param lock boolean
---@return void
function IsoThumpable:setIsLocked(lock) end

---Overrides:
---
---getFacingPosition in class IsoObject
---@public
---@param pos JVector2
---@return JVector2
function IsoThumpable:getFacingPosition(pos) end

---@public
---@return String
function IsoThumpable:getLightSourceFuel() end

---@public
---@param thumpable boolean
---@return void
function IsoThumpable:setIsThumpable(thumpable) end

---@public
---@return int
function IsoThumpable:countAddSheetRope() end

---@public
---@return IsoObject
function IsoThumpable:getRenderEffectMaster() end

---@public
---@return String
function IsoThumpable:getBreakSound() end

---@public
---@param lockedByKey boolean
---@return void
function IsoThumpable:setLockedByKey(lockedByKey) end

---@public
---@param blockAllTheSquare boolean
---@return void
function IsoThumpable:setBlockAllTheSquare(blockAllTheSquare) end

---@public
---@param arg0 String
---@return void
function IsoThumpable:setSpriteFromName(arg0) end

---@public
---@return boolean
function IsoThumpable:isLightSourceOn() end

---@public
---@return boolean
function IsoThumpable:isLockedByPadlock() end

---@public
---@return IsoBarricade
function IsoThumpable:getBarricadeOnSameSquare() end

---@public
---@return boolean
function IsoThumpable:isBarricaded() end

---@public
---@return IsoSprite
function IsoThumpable:getOpenSprite() end

---@public
---@param arg0 IsoPlayer
---@param arg1 String
---@return boolean
function IsoThumpable:addSheetRope(arg0, arg1) end

---@public
---@param owner IsoGameCharacter
---@param weapon HandWeapon
---@return void
function IsoThumpable:WeaponHit(owner, weapon) end

---@public
---@param sprite IsoSprite
---@return void
function IsoThumpable:setClosedSprite(sprite) end

---@public
---@param dismantable boolean
---@return void
function IsoThumpable:setIsDismantable(dismantable) end

---@public
---@param lifeLeft float
---@return void
function IsoThumpable:setLifeLeft(lifeLeft) end

---@public
---@param paintable boolean
---@return void
function IsoThumpable:setPaintable(paintable) end

---@public
---@param item InventoryItem
---@param chr IsoGameCharacter
---@return InventoryItem
function IsoThumpable:insertNewFuel(item, chr) end

---@public
---@return int
function IsoThumpable:getThumpDmg() end

---@public
---@param sprite IsoSprite
---@return void
function IsoThumpable:setOpenSprite(sprite) end

---@public
---@param pIsDoorFrame boolean
---@return void
function IsoThumpable:setIsDoorFrame(pIsDoorFrame) end

---@public
---@param pIsFloor boolean
---@return void
function IsoThumpable:setIsFloor(pIsFloor) end

---@public
---@return IsoLightSource
function IsoThumpable:getLightSource() end

---@public
---@return float
function IsoThumpable:getThumpCondition() end

---@public
---@param arg0 ByteBufferWriter
---@return void
function IsoThumpable:syncIsoObjectSend(arg0) end

---@public
---@return IsoBarricade
function IsoThumpable:getBarricadeOnOppositeSquare() end

---@public
---@param isHoppable boolean
---@return void
function IsoThumpable:setIsHoppable(isHoppable) end

---@public
---@param player IsoPlayer
---@return boolean
function IsoThumpable:removeSheetRope(player) end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function IsoThumpable:save(arg0, arg1) end
