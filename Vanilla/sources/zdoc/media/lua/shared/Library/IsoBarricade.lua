---@class IsoBarricade : zombie.iso.objects.IsoBarricade
---@field public MAX_PLANKS int
---@field public PLANK_HEALTH int
---@field private plankHealth int[]
---@field public METAL_HEALTH int
---@field public METAL_HEALTH_DAMAGED int
---@field private metalHealth int
---@field public METAL_BAR_HEALTH int
---@field private metalBarHealth int
IsoBarricade = {}

---@public
---@param arg0 BarricadeAble
---@param arg1 IsoGameCharacter
---@return IsoBarricade
function IsoBarricade:GetBarricadeForCharacter(arg0, arg1) end

---@public
---@return boolean
function IsoBarricade:canAddPlank() end

---@private
---@return void
function IsoBarricade:chooseSprite() end

---Overrides:
---
---TestVision in class IsoObject
---@public
---@param from IsoGridSquare
---@param to IsoGridSquare
---@return IsoObject.VisionResult
function IsoBarricade:TestVision(from, to) end

---@public
---@return boolean
function IsoBarricade:isMetal() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoBarricade:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 InventoryItem
---@return void
function IsoBarricade:addMetalBar(arg0, arg1) end

---@public
---@param arg0 BarricadeAble
---@param arg1 IsoGameCharacter
---@return IsoBarricade
---@overload fun(arg0:BarricadeAble, arg1:boolean)
function IsoBarricade:AddBarricadeToObject(arg0, arg1) end

---@public
---@param arg0 BarricadeAble
---@param arg1 boolean
---@return IsoBarricade
function IsoBarricade:AddBarricadeToObject(arg0, arg1) end

---@public
---@param arg0 IsoGridSquare
---@param arg1 IsoDirections
---@return IsoBarricade
function IsoBarricade:GetBarricadeOnSquare(arg0, arg1) end

---@public
---@return boolean
function IsoBarricade:isBlockVision() end

---@public
---@param arg0 IsoGameCharacter
---@return InventoryItem
function IsoBarricade:removeMetal(arg0) end

---@public
---@param arg0 int
---@return void
function IsoBarricade:DamageBarricade(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 InventoryItem
---@return void
function IsoBarricade:addPlank(arg0, arg1) end

---Specified by:
---
---Thump in interface Thumpable
---@public
---@param thumper IsoMovingObject
---@return void
function IsoBarricade:Thump(thumper) end

---@public
---@return BarricadeAble
function IsoBarricade:getBarricadedObject() end

---@public
---@param arg0 int
---@return void
function IsoBarricade:Damage(arg0) end

---@public
---@param owner IsoGameCharacter
---@param weapon HandWeapon
---@return void
function IsoBarricade:WeaponHit(owner, weapon) end

---Overrides:
---
---TestCollide in class IsoObject
---@public
---@param obj IsoMovingObject
---@param from IsoGridSquare
---@param to IsoGridSquare
---@return boolean
function IsoBarricade:TestCollide(obj, from, to) end

---@public
---@param arg0 String
---@param arg1 KahluaTable
---@param arg2 ByteBuffer
---@return void
function IsoBarricade:saveChange(arg0, arg1, arg2) end

---@public
---@return int
function IsoBarricade:getNumPlanks() end

---@public
---@param arg0 BarricadeAble
---@param arg1 IsoGameCharacter
---@return IsoBarricade
function IsoBarricade:GetBarricadeOppositeCharacter(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@return Thumpable
function IsoBarricade:getThumpableFor(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return InventoryItem
function IsoBarricade:removePlank(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 InventoryItem
---@return void
function IsoBarricade:addMetal(arg0, arg1) end

---@public
---@return boolean
function IsoBarricade:isMetalBar() end

---@public
---@param arg0 JVector2
---@return JVector2
function IsoBarricade:getFacingPosition(arg0) end

---@public
---@return float
function IsoBarricade:getThumpCondition() end

---Overrides:
---
---getObjectName in class IsoObject
---@public
---@return String
function IsoBarricade:getObjectName() end

---@public
---@param arg0 String
---@param arg1 ByteBuffer
---@return void
function IsoBarricade:loadChange(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@return InventoryItem
function IsoBarricade:removeMetalBar(arg0) end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function IsoBarricade:save(arg0, arg1) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoBarricade:load(arg0, arg1, arg2) end

---Specified by:
---
---isDestroyed in interface Thumpable
---@public
---@return boolean
function IsoBarricade:isDestroyed() end
