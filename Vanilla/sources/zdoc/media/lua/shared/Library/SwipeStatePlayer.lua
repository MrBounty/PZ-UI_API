---@class SwipeStatePlayer : zombie.ai.states.SwipeStatePlayer
---@field private _instance SwipeStatePlayer
---@field PARAM_LOWER_CONDITION Integer
---@field PARAM_ATTACKED Integer
---@field private HitList2 ArrayList|Unknown
---@field private tempVector2_1 JVector2
---@field private tempVector2_2 JVector2
---@field private dotList ArrayList|Unknown
---@field private bHitOnlyTree boolean
---@field public hitInfoPool ObjectPool|Unknown
---@field private Comparator SwipeStatePlayer.CustomComparator
---@field tempVector3_1 Vector3
---@field tempVector3_2 Vector3
---@field tempVectorBonePos Vector3
---@field movingStatic ArrayList|Unknown
---@field private tempVector4f Vector4f
---@field private windowVisitor SwipeStatePlayer.WindowVisitor
SwipeStatePlayer = {}

---@public
---@param arg0 IsoLivingCharacter
---@param arg1 HandWeapon
---@param arg2 boolean
---@param arg3 ArrayList|Unknown
---@param arg4 ArrayList|Unknown
---@return void
function SwipeStatePlayer:calcValidTargets(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param owner IsoGameCharacter
---@param weapon HandWeapon
---@return void
function SwipeStatePlayer:ConnectSwing(owner, weapon) end

---@public
---@return SwipeStatePlayer
function SwipeStatePlayer:instance() end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 IsoMovingObject
---@param arg2 HandWeapon
---@return void
function SwipeStatePlayer:smashWindowBetween(arg0, arg1, arg2) end

---Overrides:
---
---enter in class State
---@public
---@param owner IsoGameCharacter
---@return void
function SwipeStatePlayer:enter(owner) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 SwipeStatePlayer.LOSVisitor
---@return LosUtil.TestResults
function SwipeStatePlayer:los(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 IsoMovingObject
---@param arg1 HandWeapon
---@param arg2 IsoGameCharacter
---@return void
function SwipeStatePlayer:splash(arg0, arg1, arg2) end

---@private
---@param arg0 IsoGameCharacter
---@return void
function SwipeStatePlayer:filterTargetsByZ(arg0) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 IsoMovingObject
---@param arg2 Vector4f
---@return void
---@overload fun(arg0:IsoGameCharacter, arg1:Vector3, arg2:JVector2, arg3:Vector4f)
---@overload fun(arg0:IsoGameCharacter, arg1:IsoMovingObject, arg2:String, arg3:JVector2, arg4:Vector4f)
---@overload fun(arg0:IsoGameCharacter, arg1:HandWeapon, arg2:IsoMovingObject, arg3:boolean, arg4:Vector4f)
function SwipeStatePlayer:getNearestTargetPosAndDot(arg0, arg1, arg2) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 Vector3
---@param arg2 JVector2
---@param arg3 Vector4f
---@return void
function SwipeStatePlayer:getNearestTargetPosAndDot(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 IsoMovingObject
---@param arg2 String
---@param arg3 JVector2
---@param arg4 Vector4f
---@return void
function SwipeStatePlayer:getNearestTargetPosAndDot(arg0, arg1, arg2, arg3, arg4) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 HandWeapon
---@param arg2 IsoMovingObject
---@param arg3 boolean
---@param arg4 Vector4f
---@return boolean
function SwipeStatePlayer:getNearestTargetPosAndDot(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 IsoLivingCharacter
---@param arg1 AttackVars
---@return void
function SwipeStatePlayer:CalcAttackVars(arg0, arg1) end

---@private
---@param arg0 IsoLivingCharacter
---@param arg1 HandWeapon
---@param arg2 IsoMovingObject
---@param arg3 float
---@return HitInfo
function SwipeStatePlayer:calcValidTarget(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 HitInfo
---@param arg2 HitInfo
---@return boolean
function SwipeStatePlayer:isProneTargetBetter(arg0, arg1, arg2) end

---@private
---@param arg0 IsoPlayer
---@param arg1 float
---@param arg2 boolean
---@param arg3 String
---@param arg4 AttackVars
---@return void
function SwipeStatePlayer:doAttack(arg0, arg1, arg2, arg3, arg4) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 boolean
---@param arg2 AttackVars
---@param arg3 ArrayList|Unknown
---@return void
function SwipeStatePlayer:CalcHitListWeapon(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 IsoMovingObject
---@param arg1 IsoMovingObject
---@return boolean
function SwipeStatePlayer:isWindowBetween(arg0, arg1) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 boolean
---@return int
function SwipeStatePlayer:calcDamageToVehicle(arg0, arg1, arg2) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 HandWeapon
---@param arg2 HitInfo
---@return int
function SwipeStatePlayer:CalcHitChance(arg0, arg1, arg2) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function SwipeStatePlayer:exit(arg0) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 HandWeapon
---@param arg2 ArrayList|Unknown
---@return void
function SwipeStatePlayer:CalcHitListWindow(arg0, arg1, arg2) end

---@public
---@param weapon HandWeapon
---@param owner IsoGameCharacter
---@return void
function SwipeStatePlayer:WeaponLowerCondition(weapon, owner) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 IsoZombie
---@param arg2 long
---@return void
function SwipeStatePlayer:setParameterCharacterHitResult(arg0, arg1, arg2) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 boolean
---@param arg2 AttackVars
---@param arg3 ArrayList|Unknown
---@return void
function SwipeStatePlayer:CalcHitListShove(arg0, arg1, arg2, arg3) end

---Overrides:
---
---execute in class State
---@public
---@param owner IsoGameCharacter
---@return void
function SwipeStatePlayer:execute(owner) end

---@private
---@param arg0 IsoMovingObject
---@param arg1 IsoMovingObject
---@return IsoWindow
---@overload fun(arg0:int, arg1:int, arg2:int, arg3:int, arg4:int)
function SwipeStatePlayer:getWindowBetween(arg0, arg1) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@return IsoWindow
function SwipeStatePlayer:getWindowBetween(arg0, arg1, arg2, arg3, arg4) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 HandWeapon
---@param arg2 float
---@param arg3 float
---@param arg4 boolean
---@param arg5 ArrayList|Unknown
---@return void
function SwipeStatePlayer:removeUnhittableTargets(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 IsoMovingObject
---@param arg1 String
---@param arg2 Vector3
---@return Vector3
function SwipeStatePlayer:getBoneWorldPos(arg0, arg1, arg2) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 HandWeapon
---@param arg2 IsoGameCharacter
---@param arg3 int
---@param arg4 float
---@return int
function SwipeStatePlayer:DoSwingCollisionBoneCheck(arg0, arg1, arg2, arg3, arg4) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 HandWeapon
---@return boolean
function SwipeStatePlayer:CheckObjectHit(arg0, arg1) end

---@public
---@param arg0 IsoMovingObject
---@return boolean
function SwipeStatePlayer:isStanding(arg0) end

---@public
---@param arg0 IsoMovingObject
---@return boolean
function SwipeStatePlayer:isProne(arg0) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 HandWeapon
---@param arg2 IsoGridSquare
---@param arg3 boolean
---@param arg4 boolean
---@return boolean
function SwipeStatePlayer:checkObjectHit(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 IsoMovingObject
---@return boolean
function SwipeStatePlayer:checkPVP(arg0, arg1) end

---@public
---@param weapon HandWeapon
---@param owner IsoGameCharacter
---@return void
function SwipeStatePlayer:changeWeapon(weapon, owner) end

---@private
---@param arg0 IsoGameCharacter
---@return HandWeapon
function SwipeStatePlayer:GetWeapon(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimEvent
---@return void
function SwipeStatePlayer:animEvent(arg0, arg1) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 ArrayList|Unknown
---@param arg2 float
---@return boolean
function SwipeStatePlayer:shouldIgnoreProneZombies(arg0, arg1, arg2) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 boolean
---@param arg2 AttackVars
---@param arg3 ArrayList|Unknown
---@return void
function SwipeStatePlayer:CalcHitList(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 HandWeapon
---@param arg2 float
---@param arg3 float
---@param arg4 HitInfo
---@param arg5 boolean
---@return boolean
function SwipeStatePlayer:isUnhittableTarget(arg0, arg1, arg2, arg3, arg4, arg5) end
