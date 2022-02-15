---@class IsoDoor : zombie.iso.objects.IsoDoor
---@field public Health int
---@field public lockedByKey boolean
---@field private haveKey boolean
---@field public Locked boolean
---@field public MaxHealth int
---@field public PushedMaxStrength int
---@field public PushedStrength int
---@field public type IsoDoor.DoorType
---@field closedSprite IsoSprite
---@field public north boolean
---@field gid int
---@field public open boolean
---@field openSprite IsoSprite
---@field private destroyed boolean
---@field private bHasCurtain boolean
---@field private bCurtainInside boolean
---@field private bCurtainOpen boolean
---@field _table KahluaTable
---@field public tempo JVector2
---@field private curtainN IsoSprite
---@field private curtainS IsoSprite
---@field private curtainW IsoSprite
---@field private curtainE IsoSprite
---@field private curtainNopen IsoSprite
---@field private curtainSopen IsoSprite
---@field private curtainWopen IsoSprite
---@field private curtainEopen IsoSprite
---@field private DoubleDoorNorthSpriteOffset int[]
---@field private DoubleDoorWestSpriteOffset int[]
---@field private DoubleDoorNorthClosedXOffset int[]
---@field private DoubleDoorNorthOpenXOffset int[]
---@field private DoubleDoorNorthClosedYOffset int[]
---@field private DoubleDoorNorthOpenYOffset int[]
---@field private DoubleDoorWestClosedXOffset int[]
---@field private DoubleDoorWestOpenXOffset int[]
---@field private DoubleDoorWestClosedYOffset int[]
---@field private DoubleDoorWestOpenYOffset int[]
IsoDoor = {}

---@public
---@return int
function IsoDoor:getHealth() end

---@private
---@param arg0 IsoObject
---@return void
function IsoDoor:toggleGarageDoorObject(arg0) end

---@public
---@param lockedByKey boolean
---@return void
function IsoDoor:setLockedByKey(lockedByKey) end

---@public
---@return boolean
function IsoDoor:isBarricadeAllowed() end

---@public
---@param arg0 IsoGameCharacter
---@return void
---@overload fun(arg0:boolean, arg1:IsoGameCharacter)
function IsoDoor:addSheet(arg0) end

---@public
---@param arg0 boolean
---@param arg1 IsoGameCharacter
---@return void
function IsoDoor:addSheet(arg0, arg1) end

---@public
---@return IsoDoor
function IsoDoor:HasCurtains() end

---@public
---@param arg0 ByteBuffer
---@return void
function IsoDoor:loadState(arg0) end

---@public
---@param arg0 boolean
---@return IsoDirections
function IsoDoor:getSpriteEdge(arg0) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoDoor:prerender2xS(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param chr IsoGameCharacter
---@return IsoGridSquare
function IsoDoor:getOtherSideOfDoor(chr) end

---@public
---@param arg0 ByteBufferWriter
---@return void
function IsoDoor:syncIsoObjectSend(arg0) end

---@public
---@return boolean
function IsoDoor:IsStrengthenedByPushedItems() end

---@public
---@param arg0 IsoObject
---@return IsoObject
function IsoDoor:getGarageDoorFirst(arg0) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoDoor:load(arg0, arg1, arg2) end

---Specified by:
---
---isDestroyed in interface Thumpable
---@public
---@return boolean
function IsoDoor:isDestroyed() end

---@public
---@param arg0 boolean
---@return void
function IsoDoor:setLocked(arg0) end

---@public
---@param chr IsoGameCharacter
---@return void
function IsoDoor:ToggleDoorActual(chr) end

---@public
---@return IsoSprite
function IsoDoor:getOpenSprite() end

---@public
---@param Health int
---@return void
function IsoDoor:setHealth(Health) end

---@public
---@return IsoBarricade
function IsoDoor:getBarricadeOnSameSquare() end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoDoor:postrender1xS(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 IsoObject
---@return boolean
function IsoDoor:destroyGarageDoor(arg0) end

---@public
---@param arg0 IsoObject
---@param arg1 int
---@return IsoObject
function IsoDoor:getDoubleDoorObject(arg0, arg1) end

---Overrides:
---
---getFacingPosition in class IsoObject
---@public
---@param pos JVector2
---@return JVector2
function IsoDoor:getFacingPosition(pos) end

---@public
---@param haveKey boolean
---@return void
function IsoDoor:setHaveKey(haveKey) end

---@public
---@param arg0 boolean
---@return void
function IsoDoor:setCurtainOpen(arg0) end

---@public
---@param arg0 IsoObject
---@param arg1 boolean
---@return void
function IsoDoor:toggleGarageDoor(arg0, arg1) end

---Overrides:
---
---getKeyId in class IsoObject
---@public
---@return int
function IsoDoor:getKeyId() end

---@public
---@param arg0 IsoObject
---@return IsoObject
function IsoDoor:getGarageDoorNext(arg0) end

---Specified by:
---
---Thump in interface Thumpable
---@public
---@param thumper IsoMovingObject
---@return void
function IsoDoor:Thump(thumper) end

---@public
---@return boolean
function IsoDoor:isLocked() end

---@public
---@return boolean
function IsoDoor:isHoppable() end

---@public
---@return IsoGridSquare
function IsoDoor:getSheetSquare() end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoDoor:postrender2xE(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoDoor:postrender2xN(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoDoor:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoDoor:postrender2xW(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@param arg7 Consumer|Unknown
---@return void
function IsoDoor:renderWallTile(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@param chr IsoGameCharacter
---@return void
function IsoDoor:ToggleDoor(chr) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoDoor:prerender1xE(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoDoor:postrender1xE(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoDoor:prerender1xS(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@private
---@param arg0 BaseCharacterSoundEmitter
---@param arg1 String
---@return void
function IsoDoor:playDoorSound(arg0, arg1) end

---@public
---@return void
function IsoDoor:addRandomBarricades() end

---@public
---@param chr IsoGameCharacter
---@return boolean
function IsoDoor:isExteriorDoor(chr) end

---@public
---@return boolean
function IsoDoor:isLockedByKey() end

---@public
---@return int
function IsoDoor:getMaxHealth() end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoDoor:postrender1xN(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@return int
function IsoDoor:checkKeyId() end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoDoor:postrender1xW(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@return boolean
function IsoDoor:IsOpen() end

---@public
---@return boolean
function IsoDoor:isObstructed() end

---@private
---@return String
function IsoDoor:getSoundPrefix() end

---@public
---@param arg0 IsoGameCharacter
---@return IsoBarricade
function IsoDoor:getBarricadeOppositeCharacter(arg0) end

---Overrides:
---
---TestPathfindCollide in class IsoObject
---@public
---@param obj IsoMovingObject
---@param from IsoGridSquare
---@param to IsoGridSquare
---@return boolean
function IsoDoor:TestPathfindCollide(obj, from, to) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 IsoDirections
---@return void
function IsoDoor:postrender(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 boolean
---@param arg1 byte
---@param arg2 UdpConnection
---@param arg3 ByteBuffer
---@return void
function IsoDoor:syncIsoObject(arg0, arg1, arg2, arg3) end

---@public
---@return String
function IsoDoor:getThumpSound() end

---@private
---@return void
function IsoDoor:initCurtainSprites() end

---@public
---@return boolean
function IsoDoor:isCurtainOpen() end

---@public
---@param arg0 IsoGameCharacter
---@return boolean
function IsoDoor:isFacingSheet(arg0) end

---Overrides:
---
---TestCollide in class IsoObject
---@public
---@param obj IsoMovingObject
---@param from IsoGridSquare
---@param to IsoGridSquare
---@return boolean
function IsoDoor:TestCollide(obj, from, to) end

---@public
---@param arg0 IsoObject
---@return boolean
function IsoDoor:isDoubleDoorObstructed(arg0) end

---@public
---@return void
function IsoDoor:syncDoorKey() end

---@public
---@return IsoGridSquare
function IsoDoor:getOppositeSquare() end

---@public
---@param arg0 IsoObject
---@param arg1 boolean
---@return void
function IsoDoor:toggleDoubleDoor(arg0, arg1) end

---@private
---@param arg0 IsoObject
---@return void
function IsoDoor:toggleDoubleDoorObject(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return Thumpable
function IsoDoor:getThumpableFor(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return IsoBarricade
function IsoDoor:getBarricadeForCharacter(arg0) end

---@public
---@return boolean
function IsoDoor:haveKey() end

---@public
---@return void
function IsoDoor:destroy() end

---Overrides:
---
---onMouseLeftClick in class IsoObject
---@public
---@param x int
---@param y int
---@return boolean
function IsoDoor:onMouseLeftClick(x, y) end

---@public
---@param arg0 IsoObject
---@return int
function IsoDoor:getGarageDoorIndex(arg0) end

---@public
---@return float
function IsoDoor:getThumpCondition() end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoDoor:postrender2xS(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoDoor:prerender1xN(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@return IsoBarricade
function IsoDoor:getBarricadeOnOppositeSquare() end

---@public
---@return void
function IsoDoor:toggleCurtain() end

---@public
---@param lock boolean
---@return void
function IsoDoor:setIsLocked(lock) end

---Overrides:
---
---getObjectName in class IsoObject
---@public
---@return String
function IsoDoor:getObjectName() end

---@param arg0 int
---@return void
function IsoDoor:Damage(arg0) end

---Overrides:
---
---TestVision in class IsoObject
---@public
---@param from IsoGridSquare
---@param to IsoGridSquare
---@return IsoObject.VisionResult
function IsoDoor:TestVision(from, to) end

---@public
---@return IsoObject
function IsoDoor:getRenderEffectMaster() end

---@public
---@param arg0 IsoObject
---@return boolean
function IsoDoor:isDoorObstructed(arg0) end

---@public
---@return boolean
function IsoDoor:isBarricaded() end

---@public
---@param arg0 IsoGridSquare
---@return boolean
function IsoDoor:isAdjacentToSquare(arg0) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoDoor:prerender1xW(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 String
---@param arg1 ByteBuffer
---@return void
function IsoDoor:loadChange(arg0, arg1) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoDoor:prerender2xN(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@return boolean
function IsoDoor:getNorth() end

---@public
---@param owner IsoGameCharacter
---@param weapon HandWeapon
---@return void
function IsoDoor:WeaponHit(owner, weapon) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoDoor:prerender2xE(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 IsoGameCharacter
---@return boolean
function IsoDoor:canClimbOver(arg0) end

---@public
---@param arg0 IsoObject
---@return boolean
function IsoDoor:destroyDoubleDoor(arg0) end

---@private
---@param arg0 IsoObject
---@return boolean
function IsoDoor:isGarageDoorObstructed(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function IsoDoor:removeSheet(arg0) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 IsoDirections
---@return void
function IsoDoor:prerender(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoDoor:prerender2xW(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 IsoObject
---@return IsoObject
function IsoDoor:getGarageDoorPrev(arg0) end

---Overrides:
---
---setKeyId in class IsoObject
---@public
---@param keyId int
---@return void
function IsoDoor:setKeyId(keyId) end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function IsoDoor:save(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@return IsoGridSquare
function IsoDoor:getAddSheetSquare(arg0) end

---@private
---@param arg0 float
---@param arg1 float
---@return void
function IsoDoor:checkKeyHighlight(arg0, arg1) end

---@public
---@return void
function IsoDoor:ToggleDoorSilent() end

---@public
---@param arg0 JVector2
---@return JVector2
function IsoDoor:getFacingPositionAlt(arg0) end

---@public
---@param arg0 ByteBuffer
---@return void
function IsoDoor:saveState(arg0) end

---@public
---@param arg0 String
---@param arg1 KahluaTable
---@param arg2 ByteBuffer
---@return void
function IsoDoor:saveChange(arg0, arg1, arg2) end

---@public
---@param arg0 boolean
---@return void
function IsoDoor:transmitSetCurtainOpen(arg0) end

---@public
---@param sprite IsoSprite
---@return void
function IsoDoor:setOpenSprite(sprite) end

---@public
---@param arg0 IsoObject
---@return int
function IsoDoor:getDoubleDoorIndex(arg0) end
