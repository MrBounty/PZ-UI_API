---@class WeaponType : zombie.inventory.types.WeaponType
---@field public barehand WeaponType
---@field public twohanded WeaponType
---@field public onehanded WeaponType
---@field public heavy WeaponType
---@field public knife WeaponType
---@field public spear WeaponType
---@field public handgun WeaponType
---@field public firearm WeaponType
---@field public throwing WeaponType
---@field public chainsaw WeaponType
---@field public type String
---@field public possibleAttack List|Unknown
---@field public canMiss boolean
---@field public isRanged boolean
WeaponType = {}

---@public
---@param arg0 String
---@return WeaponType
function WeaponType:valueOf(arg0) end

---@public
---@param arg0 HandWeapon
---@return WeaponType
---@overload fun(arg0:IsoGameCharacter)
function WeaponType:getWeaponType(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return WeaponType
function WeaponType:getWeaponType(arg0) end

---@public
---@return String
function WeaponType:getType() end

---@public
---@return WeaponType[]
function WeaponType:values() end
